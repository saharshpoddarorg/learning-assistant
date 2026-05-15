#!/usr/bin/env bash
# =============================================================================
# create-profile.sh — Create a browser profile within the MCP data directory
# =============================================================================
# Creates a named profile inside the MCP browser data directory so you can
# maintain separate profiles for different MCP use-cases (e.g., testing, OAuth).
#
# NOTE: With auto-isolation (--user-data-dir), launch-browser.sh already
# creates an isolated Default profile automatically. This script is only needed
# if you want ADDITIONAL named profiles within the MCP data dir.
#
# Usage:
#   ./scripts/common/browser/create-profile.sh [--name <profile-name>] [--data-dir <path>]
#
# Options:
#   --name <name>      Profile name (default: MCP-Extra)
#   --data-dir <path>  MCP browser data directory (default: auto-detect)
# =============================================================================

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../../.." && pwd)"

# Source shared config reader
source "$PROJECT_ROOT/scripts/common/utils/read-config.sh"

PROFILE_NAME="MCP-Extra"
DATA_DIR_OVERRIDE=""

# --- Parse arguments ---------------------------------------------------------
while [[ $# -gt 0 ]]; do
    case "$1" in
        --name)     PROFILE_NAME="$2"; shift 2 ;;
        --data-dir) DATA_DIR_OVERRIDE="$2"; shift 2 ;;
        *)          echo "Unknown option: $1"; exit 1 ;;
    esac
done

# --- Resolve MCP data directory -----------------------------------------------
resolve_data_dir() {
    if [[ -n "$DATA_DIR_OVERRIDE" ]]; then
        echo "$DATA_DIR_OVERRIDE"
        return
    fi

    local configured
    configured=$(read_config "browser.dataDir" "")
    if [[ -n "$configured" ]]; then
        echo "$configured"
        return
    fi

    # Platform default
    case "$(uname -s)" in
        Darwin) echo "$HOME/.mcp/browser-data" ;;
        *)      echo "$HOME/.mcp/browser-data" ;;
    esac
}

MCP_DATA_DIR=$(resolve_data_dir)

# --- Detect browser type from config ------------------------------------------
detect_browser_type() {
    local exec_val
    exec_val=$(read_config "browser.executable" "")

    if [[ -n "$exec_val" ]]; then
        local base_name
        base_name=$(basename "$exec_val" | tr '[:upper:]' '[:lower:]')
        if [[ "$base_name" == *"chrome"* ]] || [[ "$base_name" == *"chromium"* ]] || [[ "$base_name" == *"edge"* ]] || [[ "$base_name" == *"brave"* ]]; then
            echo "chromium"
            return
        elif [[ "$base_name" == *"firefox"* ]]; then
            echo "firefox"
            return
        fi
    fi

    # Auto-detect from installed browsers
    for cmd in google-chrome google-chrome-stable chromium-browser microsoft-edge brave-browser; do
        if command -v "$cmd" &>/dev/null; then echo "chromium"; return; fi
    done
    if command -v firefox &>/dev/null; then echo "firefox"; return; fi

    echo "chromium"  # default assumption
}

BROWSER_TYPE=$(detect_browser_type)

echo "=== MCP Browser Profile Setup ==="
echo "Browser Type : $BROWSER_TYPE"
echo "Data Dir     : $MCP_DATA_DIR"
echo "Profile      : $PROFILE_NAME"
echo "=================================="

# --- Create profile within MCP data dir ----------------------------------------
PROFILE_DIR="$MCP_DATA_DIR/$PROFILE_NAME"

if [[ -d "$PROFILE_DIR" ]]; then
    echo "Profile directory already exists: $PROFILE_DIR"
    echo "Use a different --name or delete the existing profile first."
    exit 0
fi

echo "Creating profile directory: $PROFILE_DIR"
mkdir -p "$PROFILE_DIR"

case "$BROWSER_TYPE" in
    chromium)
        # Chromium profiles: creating the dir + First Run sentinel is sufficient
        touch "$PROFILE_DIR/First Run"
        echo "Created Chromium profile directory with First Run sentinel."
        ;;
    firefox)
        # Firefox profiles: create directory (registered on launch via -profile)
        echo "Created Firefox profile directory."
        echo "Use with: launch-browser.sh --data-dir \"$MCP_DATA_DIR\" (profile auto-detected)"
        ;;
esac

echo ""
echo "Profile created successfully."
echo ""
echo "To use this profile with launch-browser:"
echo "  ./scripts/common/browser/launch-browser.sh --profile \"$PROFILE_NAME\""
