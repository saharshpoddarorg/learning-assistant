#!/usr/bin/env bash
# =============================================================================
# token-check.sh — Verify if current API credentials/tokens are valid
# =============================================================================
# Checks configured API keys by attempting lightweight API calls.
# Currently supports: GitHub. Extend by adding check functions below.
#
# Usage:
#   ./scripts/common/auth/token-check.sh [--service <name>] [--verbose]
#
# Options:
#   --service <name>   Check a specific service (default: all configured)
#   --verbose          Show full response details
# =============================================================================

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../../.." && pwd)"

# Source shared config reader
source "$PROJECT_ROOT/scripts/common/utils/read-config.sh"

TARGET_SERVICE=""
VERBOSE=false

# --- Parse arguments ---------------------------------------------------------
while [[ $# -gt 0 ]]; do
    case "$1" in
        --service) TARGET_SERVICE="$2"; shift 2 ;;
        --verbose) VERBOSE=true; shift ;;
        *)         echo "Unknown option: $1"; exit 1 ;;
    esac
done

echo "=== MCP Token Validation ==="
echo ""

TOTAL=0
VALID=0
INVALID=0

# --- Service-specific checks --------------------------------------------------

check_github() {
    local token
    token=$(read_config "apiKeys.github" "")

    if [[ -z "$token" ]]; then
        echo "  [github] SKIP — no API key configured"
        return
    fi

    TOTAL=$((TOTAL + 1))
    echo -n "  [github] Checking... "

    if ! command -v curl &>/dev/null; then
        echo "SKIP (curl not available)"
        return
    fi

    local response
    response=$(curl -s -w "\n%{http_code}" \
        -H "Authorization: token $token" \
        -H "Accept: application/vnd.github.v3+json" \
        "https://api.github.com/user" 2>/dev/null)

    local http_code
    http_code=$(echo "$response" | tail -1)
    local body
    body=$(echo "$response" | sed '$d')

    if [[ "$VERBOSE" == "true" ]]; then
        echo ""
        echo "    HTTP Status: $http_code"
        echo "    Response: $(echo "$body" | head -5)"
    fi

    case "$http_code" in
        200)
            local login
            login=$(echo "$body" | grep '"login"' | head -1 | cut -d'"' -f4)
            echo "VALID (authenticated as: $login)"
            VALID=$((VALID + 1))
            ;;
        401)
            echo "INVALID (bad credentials or expired token)"
            INVALID=$((INVALID + 1))
            ;;
        403)
            echo "WARN (rate limited or forbidden — token may still be valid)"
            VALID=$((VALID + 1))
            ;;
        *)
            echo "UNKNOWN (HTTP $http_code — could not verify)"
            INVALID=$((INVALID + 1))
            ;;
    esac
}

check_generic_http() {
    local service_name="$1"
    local key="apikey.${service_name}"
    local token
    token=$(read_config "$key" "")

    if [[ -z "$token" ]]; then
        return
    fi

    TOTAL=$((TOTAL + 1))
    echo "  [$service_name] API key present (length: ${#token}) — no specific validation available"
    VALID=$((VALID + 1))
}

# --- Run checks ---------------------------------------------------------------
if [[ -n "$TARGET_SERVICE" ]]; then
    case "$TARGET_SERVICE" in
        github) check_github ;;
        *)      check_generic_http "$TARGET_SERVICE" ;;
    esac
else
    # Check all configured API keys
    check_github

    # Discover other API keys
    if [[ -f "$MCP_CONFIG_FILE" ]]; then
        other_keys=$(grep "^apikey\." "$MCP_CONFIG_FILE" 2>/dev/null \
            | cut -d'.' -f2 | cut -d'=' -f1 | sort -u \
            | grep -v "^github$" || true)

        for service in $other_keys; do
            check_generic_http "$service"
        done
    fi
fi

# --- Summary ------------------------------------------------------------------
echo ""
echo "=== Token Check Summary ==="
echo "Total   : $TOTAL"
echo "Valid   : $VALID"
echo "Invalid : $INVALID"

if [[ $INVALID -gt 0 ]]; then
    echo "Status  : ISSUES FOUND"
    exit 1
else
    echo "Status  : ALL VALID"
    exit 0
fi
