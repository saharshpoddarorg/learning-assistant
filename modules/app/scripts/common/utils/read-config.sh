#!/usr/bin/env bash
# =============================================================================
# read-config.sh — Shared config-reading library for MCP scripts
# =============================================================================
# Provides the read_config() function used by other scripts to read values
# from the layered configuration system.
#
# Usage (source from another script):
#   source "$(dirname "$0")/../../common/utils/read-config.sh"
#   value=$(read_config "browser.executable" "default-value")
#
# Priority order (highest wins):
#   1. Environment variable (MCP_ prefix, dots→underscores, UPPERCASE)
#   2. Local config (mcp-config.local.properties — gitignored, secrets/overrides)
#   3. Base config  (mcp-config.properties — committed, safe defaults)
#   4. Default value provided by caller
# =============================================================================

# Prevent double-sourcing
if [[ "${_MCP_READ_CONFIG_LOADED:-}" == "true" ]]; then
    return 0 2>/dev/null || true
fi
_MCP_READ_CONFIG_LOADED="true"

# Resolve project root relative to this script
_RC_SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
_RC_PROJECT_ROOT="$(cd "$_RC_SCRIPT_DIR/../../.." && pwd)"

# Allow override via environment
MCP_CONFIG_FILE="${MCP_CONFIG_FILE:-$_RC_PROJECT_ROOT/user-config/mcp-config.properties}"

# Auto-resolve local config file (sibling of base config)
MCP_LOCAL_CONFIG_FILE="${MCP_LOCAL_CONFIG_FILE:-$(dirname "$MCP_CONFIG_FILE")/mcp-config.local.properties}"

# =============================================================================
# _read_from_file <file> <key>
#   Internal helper: reads a single key from a properties file.
#   Returns the value (trimmed) or empty string.
# =============================================================================
_read_from_file() {
    local file="$1"
    local key="$2"
    if [[ -f "$file" ]]; then
        local val
        val=$(grep "^${key}=" "$file" 2>/dev/null | head -1 | cut -d'=' -f2- | sed 's/^[[:space:]]*//;s/[[:space:]]*$//')
        if [[ -n "$val" && "$val" != *"<<<PLACEHOLDER"* ]]; then
            echo "$val"
        fi
    fi
}

# =============================================================================
# read_config <key> [default]
#   Reads a configuration value by key using the layered priority:
#   env var > local config > base config > default.
# =============================================================================
read_config() {
    local key="$1"
    local default="${2:-}"

    # 1. Environment variable (highest priority)
    local env_key="MCP_$(echo "$key" | tr '.' '_' | tr '[:lower:]' '[:upper:]')"
    local env_val="${!env_key:-}"
    if [[ -n "$env_val" ]]; then echo "$env_val"; return; fi

    # 2. Local config (secrets/overrides — gitignored)
    local local_val
    local_val=$(_read_from_file "$MCP_LOCAL_CONFIG_FILE" "$key")
    if [[ -n "$local_val" ]]; then echo "$local_val"; return; fi

    # 3. Base config (committed safe defaults)
    local base_val
    base_val=$(_read_from_file "$MCP_CONFIG_FILE" "$key")
    if [[ -n "$base_val" ]]; then echo "$base_val"; return; fi

    # 4. Default
    echo "$default"
}

# =============================================================================
# read_config_section <prefix>
#   Reads all keys matching a prefix, outputting key=value pairs.
#   Values from local config override base config for the same key.
# =============================================================================
read_config_section() {
    local prefix="$1"
    declare -A seen

    # Local config first (higher priority)
    if [[ -f "$MCP_LOCAL_CONFIG_FILE" ]]; then
        grep "^${prefix}\." "$MCP_LOCAL_CONFIG_FILE" 2>/dev/null | grep -v '^#' | while IFS='=' read -r key value; do
            if [[ -n "$value" && "$value" != *"<<<PLACEHOLDER"* ]]; then
                seen["$key"]=1
                echo "${key}=${value}"
            fi
        done
    fi

    # Base config (fill in keys not already provided by local)
    if [[ -f "$MCP_CONFIG_FILE" ]]; then
        grep "^${prefix}\." "$MCP_CONFIG_FILE" 2>/dev/null | grep -v '^#' | while IFS='=' read -r key value; do
            if [[ -z "${seen[$key]:-}" && -n "$value" && "$value" != *"<<<PLACEHOLDER"* ]]; then
                echo "${key}=${value}"
            fi
        done
    fi
}

# =============================================================================
# has_config <key>
#   Returns 0 (true) if a non-empty value exists for the key.
# =============================================================================
has_config() {
    local val
    val=$(read_config "$1" "")
    [[ -n "$val" ]]
}

# =============================================================================
# require_config <key> <description>
#   Reads a config value, exits with error if not found.
# =============================================================================
require_config() {
    local key="$1"
    local desc="${2:-$key}"
    local val
    val=$(read_config "$key" "")

    if [[ -z "$val" ]]; then
        local env_key="MCP_$(echo "$key" | tr '.' '_' | tr '[:lower:]' '[:upper:]')"
        echo "ERROR: Required config missing: $desc ($key)" >&2
        echo "  Set it in: $MCP_LOCAL_CONFIG_FILE (secrets/overrides)" >&2
        echo "  Or in:     $MCP_CONFIG_FILE (defaults)" >&2
        echo "  Or set env var: $env_key" >&2
        exit 1
    fi

    echo "$val"
}
