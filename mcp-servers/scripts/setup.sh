#!/usr/bin/env bash
# =============================================================================
# setup.sh — One-time setup for MCP server configuration
# =============================================================================
# Automates the developer onboarding flow with minimal intervention:
#   1. Creates mcp-config.local.properties (if missing)
#   2. Creates the MCP browser data directory (auto-isolation)
#   3. Auto-detects installed browser
#   4. Checks for API keys
#   5. Runs config validation
#   6. Prints a summary of what's ready and what needs attention
#
# Usage:
#   ./scripts/setup.sh [--skip-validation] [--non-interactive]
#
# The developer only needs to provide API keys — everything else is automatic.
# =============================================================================

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"

# Source shared config reader
source "$PROJECT_ROOT/scripts/common/utils/read-config.sh"

SKIP_VALIDATION=false
NON_INTERACTIVE=false

while [[ $# -gt 0 ]]; do
    case "$1" in
        --skip-validation) SKIP_VALIDATION=true; shift ;;
        --non-interactive) NON_INTERACTIVE=true; shift ;;
        *)                 echo "Unknown option: $1"; exit 1 ;;
    esac
done

echo "╔═══════════════════════════════════════╗"
echo "║       MCP Server Setup Wizard         ║"
echo "╚═══════════════════════════════════════╝"
echo ""

READY_ITEMS=()
ACTION_ITEMS=()

# =============================================================================
# Step 1: Check base config exists (committed, should always be there)
# =============================================================================
echo "[1/5] Checking base config..."
if [[ -f "$MCP_CONFIG_FILE" ]]; then
    echo "  ✓ Base config exists: $(basename "$MCP_CONFIG_FILE")"
    READY_ITEMS+=("Base config (committed defaults)")
else
    echo "  ✗ Base config MISSING: $MCP_CONFIG_FILE"
    echo "    This file should be in the repo. Run: git checkout -- user-config/mcp-config.properties"
    ACTION_ITEMS+=("Restore base config from git")
fi

# =============================================================================
# Step 2: Create local config file (if missing)
# =============================================================================
echo "[2/5] Checking local config..."
LOCAL_TEMPLATE="$PROJECT_ROOT/user-config/mcp-config.local.example.properties"

if [[ -f "$MCP_LOCAL_CONFIG_FILE" ]]; then
    echo "  ✓ Local config exists: $(basename "$MCP_LOCAL_CONFIG_FILE")"
    READY_ITEMS+=("Local config (secrets/overrides)")
else
    echo "  → Creating local config from template..."
    if [[ -f "$LOCAL_TEMPLATE" ]]; then
        cp "$LOCAL_TEMPLATE" "$MCP_LOCAL_CONFIG_FILE"
        echo "  ✓ Created: $(basename "$MCP_LOCAL_CONFIG_FILE")"
        echo "    Edit this file to add your API keys and secrets."
    else
        # Create minimal local config
        cat > "$MCP_LOCAL_CONFIG_FILE" <<'EOF'
# =============================================================================
# Local Config — YOUR secrets and machine-specific overrides
# =============================================================================
# This file is GITIGNORED. Safe for real values.
# Only include keys you need to override.

# --- API keys (required for the services you use) ---
apiKeys.github=
server.github.env.GITHUB_TOKEN=

# --- Uncomment to override ---
# browser.executable=
# location.timezone=
EOF
        echo "  ✓ Created: $(basename "$MCP_LOCAL_CONFIG_FILE")"
    fi
    ACTION_ITEMS+=("Set API keys in $(basename "$MCP_LOCAL_CONFIG_FILE")")
fi

# =============================================================================
# Step 3: Create MCP browser data directory
# =============================================================================
echo "[3/5] Setting up browser isolation..."
BROWSER_DATA_DIR=$(read_config "browser.dataDir" "")

if [[ -z "$BROWSER_DATA_DIR" ]]; then
    case "$(uname -s)" in
        Darwin) BROWSER_DATA_DIR="$HOME/.mcp/browser-data" ;;
        *)      BROWSER_DATA_DIR="$HOME/.mcp/browser-data" ;;
    esac
fi

if [[ -d "$BROWSER_DATA_DIR" ]]; then
    echo "  ✓ Browser data directory exists: $BROWSER_DATA_DIR"
else
    mkdir -p "$BROWSER_DATA_DIR"
    echo "  ✓ Created browser data directory: $BROWSER_DATA_DIR"
fi
READY_ITEMS+=("Browser auto-isolation (--user-data-dir)")

# =============================================================================
# Step 4: Auto-detect browser
# =============================================================================
echo "[4/5] Detecting browser..."
DETECTED_BROWSER=""

detect_browser() {
    local configured
    configured=$(read_config "browser.executable" "")
    if [[ -n "$configured" ]]; then echo "$configured"; return; fi

    for candidate in google-chrome google-chrome-stable chromium-browser \
                     microsoft-edge firefox brave-browser; do
        if command -v "$candidate" &>/dev/null; then
            echo "$candidate"
            return
        fi
    done

    for app in "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome" \
               "/Applications/Microsoft Edge.app/Contents/MacOS/Microsoft Edge" \
               "/Applications/Firefox.app/Contents/MacOS/firefox"; do
        if [[ -x "$app" ]]; then
            echo "$app"
            return
        fi
    done
}

DETECTED_BROWSER=$(detect_browser)
if [[ -n "$DETECTED_BROWSER" ]]; then
    echo "  ✓ Detected: $DETECTED_BROWSER"
    READY_ITEMS+=("Browser: $(basename "$DETECTED_BROWSER")")
else
    echo "  ✗ No browser found in PATH"
    ACTION_ITEMS+=("Set browser.executable in config or install Chrome/Edge/Firefox")
fi

# =============================================================================
# Step 5: Check API keys
# =============================================================================
echo "[5/5] Checking API keys..."
GITHUB_KEY=$(read_config "apiKeys.github" "")
GITHUB_TOKEN=$(read_config "server.github.env.GITHUB_TOKEN" "")

if [[ -n "$GITHUB_KEY" ]] || [[ -n "$GITHUB_TOKEN" ]]; then
    echo "  ✓ GitHub API key configured"
    READY_ITEMS+=("GitHub API key")
else
    echo "  ✗ No GitHub API key found"
    ACTION_ITEMS+=("Set apiKeys.github in $(basename "$MCP_LOCAL_CONFIG_FILE") or MCP_APIKEYS_GITHUB env var")
fi

# =============================================================================
# Validation (optional)
# =============================================================================
if [[ "$SKIP_VALIDATION" != "true" ]]; then
    echo ""
    echo "--- Running config validation ---"
    "$PROJECT_ROOT/scripts/common/utils/validate-config.sh" --fix-suggestions 2>&1 | sed 's/^/  /' || true
fi

# =============================================================================
# Summary
# =============================================================================
echo ""
echo "╔═══════════════════════════════════════╗"
echo "║          Setup Summary                ║"
echo "╚═══════════════════════════════════════╝"

if [[ ${#READY_ITEMS[@]} -gt 0 ]]; then
    echo ""
    echo "Ready:"
    for item in "${READY_ITEMS[@]}"; do
        echo "  ✓ $item"
    done
fi

if [[ ${#ACTION_ITEMS[@]} -gt 0 ]]; then
    echo ""
    echo "Action needed:"
    for item in "${ACTION_ITEMS[@]}"; do
        echo "  → $item"
    done
else
    echo ""
    echo "All set! Run: cd mcp-servers && javac -d out src/**/*.java && java -cp out Main"
fi

echo ""
echo "Config files:"
echo "  Base (committed) : user-config/mcp-config.properties"
echo "  Local (secrets)  : user-config/mcp-config.local.properties"
echo "  Browser data     : $BROWSER_DATA_DIR"
echo ""
