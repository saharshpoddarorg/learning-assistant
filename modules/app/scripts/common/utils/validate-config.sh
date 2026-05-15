#!/usr/bin/env bash
# =============================================================================
# validate-config.sh — Validate MCP configuration (layered)
# =============================================================================
# Checks the layered config system for common issues:
#   - Base config file (mcp-config.properties) exists
#   - Local config file (mcp-config.local.properties) exists for secrets
#   - Required API keys are set (in local file, base file, or env vars)
#   - Server definitions are valid
#   - Browser config is valid
#   - Profile references are correct
#
# Usage:
#   ./scripts/common/utils/validate-config.sh [--strict] [--fix-suggestions]
#
# Options:
#   --strict            Treat warnings as errors
#   --fix-suggestions   Show suggested fix for each issue
# =============================================================================

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../../.." && pwd)"

# Source shared config reader (provides layered read_config)
source "$SCRIPT_DIR/read-config.sh"

STRICT=false
FIX_SUGGESTIONS=false
ERRORS=0
WARNINGS=0

# --- Parse arguments ---------------------------------------------------------
while [[ $# -gt 0 ]]; do
    case "$1" in
        --strict)          STRICT=true; shift ;;
        --fix-suggestions) FIX_SUGGESTIONS=true; shift ;;
        *)                 echo "Unknown option: $1"; exit 1 ;;
    esac
done

# --- Helpers ------------------------------------------------------------------
log_error() {
    echo "  ERROR: $1"
    ERRORS=$((ERRORS + 1))
}

log_warn() {
    echo "  WARN:  $1"
    WARNINGS=$((WARNINGS + 1))
}

suggest_fix() {
    if [[ "$FIX_SUGGESTIONS" == "true" ]]; then
        echo "    FIX: $1"
    fi
}

# --- Check config files exist ------------------------------------------------
echo "=== MCP Config Validation ==="
echo "Base config  : $MCP_CONFIG_FILE"
echo "Local config : $MCP_LOCAL_CONFIG_FILE"
echo ""

echo "[1/6] Checking config files..."

if [[ ! -f "$MCP_CONFIG_FILE" ]]; then
    log_error "Base config not found: $MCP_CONFIG_FILE"
    suggest_fix "This file should be committed. Run: git checkout -- user-config/mcp-config.properties"
    echo ""
    echo "FATAL: Cannot continue without base config file."
    exit 1
else
    echo "  OK: Base config exists (committed defaults)"
fi

if [[ ! -f "$MCP_LOCAL_CONFIG_FILE" ]]; then
    log_warn "Local config not found: $MCP_LOCAL_CONFIG_FILE"
    suggest_fix "Run setup script: ./scripts/setup.sh"
    suggest_fix "Or copy template: cp user-config/mcp-config.local.example.properties user-config/mcp-config.local.properties"
else
    echo "  OK: Local config exists (secrets/overrides)"
fi

# --- Check 2: Leftover placeholders (legacy) ---------------------------------
echo "[2/6] Checking for legacy placeholders..."
placeholder_count=0
for file in "$MCP_CONFIG_FILE" "$MCP_LOCAL_CONFIG_FILE"; do
    if [[ -f "$file" ]]; then
        count=$(grep -c '<<<PLACEHOLDER' "$file" 2>/dev/null || echo "0")
        if [[ "$count" -gt 0 ]]; then
            placeholder_count=$((placeholder_count + count))
            log_warn "Found $count unresolved <<<PLACEHOLDER>>> markers in $(basename "$file"):"
            grep -n '<<<PLACEHOLDER' "$file" | while IFS= read -r line; do
                echo "    $line"
            done
            suggest_fix "Replace <<<PLACEHOLDER_...>>> with actual values or remove them"
        fi
    fi
done
if [[ "$placeholder_count" -eq 0 ]]; then
    echo "  OK: No legacy placeholders found"
fi

# --- Check 3: API key presence -----------------------------------------------
echo "[3/6] Checking API keys..."
if has_config "apiKeys.github"; then
    echo "  OK: GitHub API key configured"
else
    log_warn "No GitHub API key found (apiKeys.github)"
    suggest_fix "Set apiKeys.github=ghp_... in mcp-config.local.properties or MCP_APIKEYS_GITHUB env var"
fi

# --- Check 4: Server definitions ----------------------------------------------
echo "[4/6] Checking server definitions..."

# Gather server names from both config files
server_names=""
for file in "$MCP_CONFIG_FILE" "$MCP_LOCAL_CONFIG_FILE"; do
    if [[ -f "$file" ]]; then
        file_servers=$(grep "^server\." "$file" 2>/dev/null | cut -d'.' -f2 | sort -u)
        server_names="$server_names $file_servers"
    fi
done
server_names=$(echo "$server_names" | tr ' ' '\n' | sort -u | tr '\n' ' ')

if [[ -z "$(echo "$server_names" | tr -d '[:space:]')" ]]; then
    log_warn "No server definitions found. Add server.<name>.* entries."
else
    for server_name in $server_names; do
        transport=$(read_config "server.${server_name}.transport" "")
        command_val=$(read_config "server.${server_name}.command" "")
        url_val=$(read_config "server.${server_name}.url" "")

        if [[ -z "$transport" ]]; then
            log_error "Server '$server_name': missing transport type"
            suggest_fix "Add: server.${server_name}.transport=stdio|sse|streamable-http"
            continue
        fi

        case "$transport" in
            stdio)
                if [[ -z "$command_val" ]]; then
                    log_error "Server '$server_name': stdio transport requires 'command'"
                    suggest_fix "Add: server.${server_name}.command=<path-to-executable>"
                fi
                ;;
            sse|streamable-http)
                if [[ -z "$url_val" ]]; then
                    log_error "Server '$server_name': $transport transport requires 'url'"
                    suggest_fix "Add: server.${server_name}.url=http://localhost:8080"
                fi
                ;;
            *)
                log_error "Server '$server_name': invalid transport '$transport' (must be stdio, sse, or streamable-http)"
                ;;
        esac

        echo "  OK: Server '$server_name' ($transport)"
    done
fi

# --- Check 5: Browser config -------------------------------------------------
echo "[5/6] Checking browser preferences..."
browser_exec=$(read_config "browser.executable" "")
browser_port=$(read_config "browser.remoteDebuggingPort" "0")
browser_mode=$(read_config "browser.launchMode" "")

if [[ -n "$browser_exec" && ! -x "$browser_exec" ]] && [[ -n "$browser_exec" && ! -f "$browser_exec" ]]; then
    log_warn "Browser executable not found: $browser_exec"
    suggest_fix "Verify the path or remove to use auto-detection"
fi

if [[ "$browser_port" != "0" ]]; then
    if [[ "$browser_port" -lt 1024 || "$browser_port" -gt 65535 ]]; then
        log_error "Browser debug port out of range: $browser_port (must be 1024-65535)"
    fi
fi

if [[ -n "$browser_mode" ]]; then
    case "$browser_mode" in
        new-window|new-tab|incognito|app-mode) echo "  OK: Launch mode '$browser_mode'" ;;
        *) log_error "Invalid browser.launchMode: '$browser_mode' (must be new-window, new-tab, incognito, or app-mode)" ;;
    esac
fi

# Check MCP browser data directory
browser_data_dir=$(read_config "browser.dataDir" "")
if [[ -n "$browser_data_dir" && ! -d "$browser_data_dir" ]]; then
    log_warn "Browser data directory does not exist yet: $browser_data_dir"
    suggest_fix "It will be auto-created on first browser launch"
fi

# --- Check 6: Profile references ---------------------------------------------
echo "[6/6] Checking profile configuration..."
active_profile=$(read_config "config.activeProfile" "")
if [[ -n "$active_profile" ]]; then
    profile_exists=""
    for file in "$MCP_CONFIG_FILE" "$MCP_LOCAL_CONFIG_FILE"; do
        if [[ -f "$file" ]]; then
            match=$(grep "^profile\.${active_profile}\." "$file" 2>/dev/null | head -1)
            if [[ -n "$match" ]]; then profile_exists="yes"; break; fi
        fi
    done
    if [[ -z "$profile_exists" ]]; then
        log_error "Active profile '$active_profile' is not defined in config"
        suggest_fix "Add profile.${active_profile}.* entries or change config.activeProfile"
    else
        echo "  OK: Active profile '$active_profile' found"
    fi
fi

# --- Summary ------------------------------------------------------------------
echo ""
echo "=== Validation Summary ==="
echo "Errors   : $ERRORS"
echo "Warnings : $WARNINGS"

if [[ "$STRICT" == "true" ]]; then
    total=$((ERRORS + WARNINGS))
else
    total=$ERRORS
fi

if [[ $total -gt 0 ]]; then
    echo "Status   : FAILED"
    exit 1
else
    echo "Status   : PASSED"
    exit 0
fi
