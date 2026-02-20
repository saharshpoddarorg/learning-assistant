#!/usr/bin/env bash
# =============================================================================
# launch-browser.sh — Launch an isolated browser for MCP server use
# =============================================================================
# SAFETY: Always uses a dedicated user-data-dir (~/.mcp/browser-data) so the
# user's existing tabs, windows, profiles, cookies, and accounts are NEVER
# touched — even with zero configuration.
#
# Usage:
#   ./scripts/common/browser/launch-browser.sh [--url <url>] [options]
#
# Options:
#   --url <url>         URL to open (default: about:blank)
#   --headless          Override to headless mode
#   --profile <name>    Profile within MCP data dir (default: Default)
#   --debug-port <n>    Override remote debugging port
#   --ephemeral         Use a temp dir (no cookie/session persistence)
#   --data-dir <path>   Override MCP browser data directory
#
# The script can be called:
#   - Manually by the developer (e.g., open a link for MCP server use)
#   - Automatically by an MCP server (internally via subprocess)
#   - From other scripts (sourced or invoked)
# =============================================================================

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../../.." && pwd)"

# Source shared config reader (layered: env > local > base)
source "$PROJECT_ROOT/scripts/common/utils/read-config.sh"

# --- Defaults ----------------------------------------------------------------
URL="about:blank"
HEADLESS_OVERRIDE=""
PROFILE_OVERRIDE=""
DEBUG_PORT_OVERRIDE=""
EPHEMERAL=false
DATA_DIR_OVERRIDE=""

# --- Parse arguments ---------------------------------------------------------
while [[ $# -gt 0 ]]; do
    case "$1" in
        --url)        URL="$2"; shift 2 ;;
        --headless)   HEADLESS_OVERRIDE="true"; shift ;;
        --profile)    PROFILE_OVERRIDE="$2"; shift 2 ;;
        --debug-port) DEBUG_PORT_OVERRIDE="$2"; shift 2 ;;
        --ephemeral)  EPHEMERAL=true; shift ;;
        --data-dir)   DATA_DIR_OVERRIDE="$2"; shift 2 ;;
        *)            echo "Unknown option: $1"; exit 1 ;;
    esac
done

# --- Load browser config -----------------------------------------------------
BROWSER_EXEC=$(read_config "browser.executable" "")
BROWSER_DATA_DIR=$(read_config "browser.dataDir" "")
BROWSER_PROFILE=$(read_config "browser.profile" "")
BROWSER_LAUNCH_MODE=$(read_config "browser.launchMode" "new-window")
BROWSER_HEADLESS=$(read_config "browser.headless" "false")
BROWSER_DEBUG_PORT=$(read_config "browser.remoteDebuggingPort" "0")
BROWSER_WIDTH=$(read_config "browser.windowWidth" "0")
BROWSER_HEIGHT=$(read_config "browser.windowHeight" "0")

# Apply CLI overrides
[[ -n "$PROFILE_OVERRIDE" ]] && BROWSER_PROFILE="$PROFILE_OVERRIDE"
[[ -n "$HEADLESS_OVERRIDE" ]] && BROWSER_HEADLESS="$HEADLESS_OVERRIDE"
[[ -n "$DEBUG_PORT_OVERRIDE" ]] && BROWSER_DEBUG_PORT="$DEBUG_PORT_OVERRIDE"
[[ -n "$DATA_DIR_OVERRIDE" ]] && BROWSER_DATA_DIR="$DATA_DIR_OVERRIDE"

# --- Resolve browser data directory (auto-isolation) --------------------------
resolve_data_dir() {
    if [[ "$EPHEMERAL" == "true" ]]; then
        local tmp_dir
        tmp_dir=$(mktemp -d "${TMPDIR:-/tmp}/mcp-browser-XXXXXXXX")
        echo "$tmp_dir"
        return
    fi

    if [[ -n "$BROWSER_DATA_DIR" ]]; then
        echo "$BROWSER_DATA_DIR"
        return
    fi

    # Platform-specific default
    case "$(uname -s)" in
        Darwin) echo "$HOME/.mcp/browser-data" ;;
        *)      echo "$HOME/.mcp/browser-data" ;;
    esac
}

MCP_DATA_DIR=$(resolve_data_dir)
mkdir -p "$MCP_DATA_DIR"

# --- Detect browser -----------------------------------------------------------
detect_browser() {
    if [[ -n "$BROWSER_EXEC" ]]; then
        echo "$BROWSER_EXEC"
        return
    fi

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

    echo ""
}

BROWSER=$(detect_browser)

if [[ -z "$BROWSER" ]]; then
    echo "ERROR: No browser found. Set browser.executable in config or MCP_BROWSER_EXECUTABLE env var."
    exit 1
fi

echo "=== MCP Browser Launch ==="
echo "Browser    : $BROWSER"
echo "Data Dir   : $MCP_DATA_DIR $([ "$EPHEMERAL" == "true" ] && echo "(ephemeral)")"
echo "Profile    : ${BROWSER_PROFILE:-"Default"}"
echo "Mode       : $BROWSER_LAUNCH_MODE"
echo "Headless   : $BROWSER_HEADLESS"
echo "Debug Port : $BROWSER_DEBUG_PORT"
echo "URL        : $URL"
echo "=========================="

# --- Build command arguments --------------------------------------------------
ARGS=()
BROWSER_BASE=$(basename "$BROWSER" | tr '[:upper:]' '[:lower:]')

if [[ "$BROWSER_BASE" == *"chrome"* ]] || [[ "$BROWSER_BASE" == *"edge"* ]] || [[ "$BROWSER_BASE" == *"chromium"* ]] || [[ "$BROWSER_BASE" == *"brave"* ]]; then
    # --- ISOLATION: dedicated user-data-dir (separate from personal browser) ---
    ARGS+=("--user-data-dir=$MCP_DATA_DIR")

    [[ -n "$BROWSER_PROFILE" ]] && ARGS+=("--profile-directory=$BROWSER_PROFILE")
    [[ "$BROWSER_HEADLESS" == "true" ]] && ARGS+=("--headless=new")
    [[ "$BROWSER_DEBUG_PORT" != "0" ]] && ARGS+=("--remote-debugging-port=$BROWSER_DEBUG_PORT")
    [[ "$BROWSER_WIDTH" != "0" && "$BROWSER_HEIGHT" != "0" ]] && ARGS+=("--window-size=${BROWSER_WIDTH},${BROWSER_HEIGHT}")

    case "$BROWSER_LAUNCH_MODE" in
        new-window)  ARGS+=("--new-window") ;;
        incognito)   ARGS+=("--incognito") ;;
        app-mode)    ARGS+=("--app=$URL"); URL="" ;;
    esac

    ARGS+=("--no-first-run" "--no-default-browser-check")

elif [[ "$BROWSER_BASE" == *"firefox"* ]]; then
    # --- ISOLATION: dedicated profile directory + no-remote ---
    ff_profile_dir="$MCP_DATA_DIR/firefox-mcp"
    mkdir -p "$ff_profile_dir"
    ARGS+=("-profile" "$ff_profile_dir" "--no-remote")

    [[ "$BROWSER_HEADLESS" == "true" ]] && ARGS+=("--headless")
    [[ "$BROWSER_DEBUG_PORT" != "0" ]] && ARGS+=("--start-debugger-server" "$BROWSER_DEBUG_PORT")
    [[ "$BROWSER_WIDTH" != "0" && "$BROWSER_HEIGHT" != "0" ]] && ARGS+=("--width" "$BROWSER_WIDTH" "--height" "$BROWSER_HEIGHT")

    case "$BROWSER_LAUNCH_MODE" in
        new-window)  ARGS+=("--new-window") ;;
        incognito)   ARGS+=("--private-window") ;;
    esac
fi

[[ -n "$URL" ]] && ARGS+=("$URL")

# --- Launch -------------------------------------------------------------------
echo "Launching: $BROWSER ${ARGS[*]}"
"$BROWSER" "${ARGS[@]}" &
BROWSER_PID=$!
echo "Browser PID: $BROWSER_PID"

# Save PID for cleanup scripts
PID_FILE="$PROJECT_ROOT/scripts/.browser-pid"
echo "$BROWSER_PID" > "$PID_FILE"
echo "PID saved to: $PID_FILE"
