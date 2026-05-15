#!/usr/bin/env bash
# =============================================================================
# close-browser.sh — Gracefully stop the MCP-managed browser instance
# =============================================================================
# Reads the saved PID from .browser-pid and terminates the process.
# Falls back to force-kill if graceful shutdown times out.
#
# Usage:
#   ./scripts/common/browser/close-browser.sh [--force] [--timeout <seconds>]
#
# Options:
#   --force            Skip graceful shutdown, kill immediately
#   --timeout <sec>    Seconds to wait before force-kill (default: 5)
# =============================================================================

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../../.." && pwd)"
PID_FILE="$PROJECT_ROOT/scripts/.browser-pid"

FORCE=false
TIMEOUT=5

# --- Parse arguments ---------------------------------------------------------
while [[ $# -gt 0 ]]; do
    case "$1" in
        --force)   FORCE=true; shift ;;
        --timeout) TIMEOUT="$2"; shift 2 ;;
        *)         echo "Unknown option: $1"; exit 1 ;;
    esac
done

# --- Check PID file -----------------------------------------------------------
if [[ ! -f "$PID_FILE" ]]; then
    echo "No .browser-pid file found. Browser may not have been launched by MCP scripts."
    exit 0
fi

BROWSER_PID=$(cat "$PID_FILE" | tr -d '[:space:]')

if [[ -z "$BROWSER_PID" ]]; then
    echo "PID file is empty. Cleaning up."
    rm -f "$PID_FILE"
    exit 0
fi

# --- Check if process is running ---------------------------------------------
if ! kill -0 "$BROWSER_PID" 2>/dev/null; then
    echo "Browser (PID $BROWSER_PID) is not running. Cleaning up PID file."
    rm -f "$PID_FILE"
    exit 0
fi

echo "=== MCP Browser Shutdown ==="
echo "PID     : $BROWSER_PID"
echo "Force   : $FORCE"
echo "Timeout : ${TIMEOUT}s"
echo "============================"

# --- Terminate ----------------------------------------------------------------
if [[ "$FORCE" == "true" ]]; then
    echo "Force-killing browser (PID $BROWSER_PID)..."
    kill -9 "$BROWSER_PID" 2>/dev/null || true
else
    echo "Sending SIGTERM to browser (PID $BROWSER_PID)..."
    kill "$BROWSER_PID" 2>/dev/null || true

    # Wait for graceful exit
    elapsed=0
    while kill -0 "$BROWSER_PID" 2>/dev/null && [[ $elapsed -lt $TIMEOUT ]]; do
        sleep 1
        elapsed=$((elapsed + 1))
        echo "  Waiting... (${elapsed}s / ${TIMEOUT}s)"
    done

    if kill -0 "$BROWSER_PID" 2>/dev/null; then
        echo "Graceful shutdown timed out. Force-killing..."
        kill -9 "$BROWSER_PID" 2>/dev/null || true
    fi
fi

echo "Browser stopped."
rm -f "$PID_FILE"
echo "PID file cleaned up."
