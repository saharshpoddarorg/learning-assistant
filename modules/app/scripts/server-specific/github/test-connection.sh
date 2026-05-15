#!/usr/bin/env bash
# =============================================================================
# test-connection.sh — Test GitHub MCP server connectivity
# =============================================================================
# Verifies GitHub API access using the configured API key, checks rate limits,
# and validates that the GitHub MCP server is reachable.
#
# Usage:
#   ./scripts/server-specific/github/test-connection.sh [--verbose]
# =============================================================================

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../../.." && pwd)"

# Source shared config reader
source "$PROJECT_ROOT/scripts/common/utils/read-config.sh"

VERBOSE=false
[[ "${1:-}" == "--verbose" ]] && VERBOSE=true

echo "=== GitHub MCP Server Connection Test ==="
echo ""

# --- Check API key ------------------------------------------------------------
GITHUB_TOKEN=$(read_config "apiKeys.github" "")

if [[ -z "$GITHUB_TOKEN" ]]; then
    echo "FAIL: No GitHub API key configured."
    echo "  Set apiKeys.github in config or MCP_APIKEYS_GITHUB env var."
    exit 1
fi

echo -n "[1/3] Authenticating... "

AUTH_RESPONSE=$(curl -s -w "\n%{http_code}" \
    -H "Authorization: token $GITHUB_TOKEN" \
    -H "Accept: application/vnd.github.v3+json" \
    -H "User-Agent: MCP-Config-Test" \
    "https://api.github.com/user" 2>/dev/null)

HTTP_CODE=$(echo "$AUTH_RESPONSE" | tail -1)
BODY=$(echo "$AUTH_RESPONSE" | sed '$d')

if [[ "$HTTP_CODE" == "200" ]]; then
    LOGIN=$(echo "$BODY" | grep '"login"' | head -1 | cut -d'"' -f4)
    echo "OK (user: $LOGIN)"
else
    echo "FAIL (HTTP $HTTP_CODE)"
    if [[ "$VERBOSE" == "true" ]]; then
        echo "  Response: $(echo "$BODY" | head -3)"
    fi
    exit 1
fi

# --- Check rate limits --------------------------------------------------------
echo -n "[2/3] Rate limits... "

RATE_RESPONSE=$(curl -s \
    -H "Authorization: token $GITHUB_TOKEN" \
    -H "Accept: application/vnd.github.v3+json" \
    "https://api.github.com/rate_limit" 2>/dev/null)

REMAINING=$(echo "$RATE_RESPONSE" | grep -o '"remaining":[0-9]*' | head -1 | cut -d':' -f2)
LIMIT=$(echo "$RATE_RESPONSE" | grep -o '"limit":[0-9]*' | head -1 | cut -d':' -f2)

if [[ -n "$REMAINING" && -n "$LIMIT" ]]; then
    echo "OK ($REMAINING / $LIMIT remaining)"
    if [[ "$REMAINING" -lt 100 ]]; then
        echo "  WARNING: Rate limit is low. Consider waiting before heavy usage."
    fi
else
    echo "WARN (could not parse rate limits)"
fi

# --- Check MCP server endpoint (if configured) --------------------------------
echo -n "[3/3] MCP server... "

TRANSPORT=$(read_config "server.github.transport" "")
if [[ -z "$TRANSPORT" ]]; then
    echo "SKIP (no server.github configuration found)"
else
    case "$TRANSPORT" in
        stdio)
            CMD=$(read_config "server.github.command" "")
            EXEC_PATH=$(echo "$CMD" | awk '{print $1}')
            if command -v "$EXEC_PATH" &>/dev/null || [[ -x "$EXEC_PATH" ]]; then
                echo "OK (stdio — executable found: $EXEC_PATH)"
            else
                echo "FAIL (executable not found: $EXEC_PATH)"
            fi
            ;;
        sse|streamable-http)
            URL=$(read_config "server.github.url" "")
            if [[ -n "$URL" ]]; then
                SC=$(curl -s -o /dev/null -w "%{http_code}" --connect-timeout 10 "$URL" 2>/dev/null || echo "000")
                if [[ "$SC" != "000" ]]; then
                    echo "OK ($TRANSPORT — HTTP $SC)"
                else
                    echo "FAIL (connection refused or timeout: $URL)"
                fi
            else
                echo "FAIL (no URL configured for $TRANSPORT transport)"
            fi
            ;;
    esac
fi

echo ""
echo "=== Connection Test Complete ==="
