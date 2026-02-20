#!/usr/bin/env bash
# =============================================================================
# health-check.sh — Check MCP server connectivity and readiness
# =============================================================================
# Verifies that configured MCP servers are reachable and responding.
# Supports stdio (process check), SSE, and streamable-http transports.
#
# Usage:
#   ./scripts/common/utils/health-check.sh [--server <name>] [--timeout <seconds>] [--verbose]
#
# Options:
#   --server <name>    Check a specific server (default: all configured servers)
#   --timeout <sec>    Connection timeout in seconds (default: 10)
#   --verbose          Show detailed request/response info
# =============================================================================

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../../.." && pwd)"

# Source shared config reader
source "$SCRIPT_DIR/read-config.sh"

TARGET_SERVER=""
TIMEOUT_SEC=10
VERBOSE=false

# --- Parse arguments ---------------------------------------------------------
while [[ $# -gt 0 ]]; do
    case "$1" in
        --server)  TARGET_SERVER="$2"; shift 2 ;;
        --timeout) TIMEOUT_SEC="$2"; shift 2 ;;
        --verbose) VERBOSE=true; shift ;;
        *)         echo "Unknown option: $1"; exit 1 ;;
    esac
done

# --- Discover servers ---------------------------------------------------------
discover_servers() {
    if [[ -n "$TARGET_SERVER" ]]; then
        echo "$TARGET_SERVER"
        return
    fi

    if [[ ! -f "$MCP_CONFIG_FILE" ]]; then
        echo ""
        return
    fi

    grep "^server\." "$MCP_CONFIG_FILE" 2>/dev/null \
        | cut -d'.' -f2 \
        | sort -u
}

SERVERS=$(discover_servers)

if [[ -z "$SERVERS" ]]; then
    echo "No servers configured. Add server.<name>.* entries to your config."
    exit 1
fi

echo "=== MCP Server Health Check ==="
echo "Timeout: ${TIMEOUT_SEC}s"
echo ""

TOTAL=0
HEALTHY=0
FAILED=0

# --- Check each server -------------------------------------------------------
check_server() {
    local name="$1"
    local transport
    transport=$(read_config "server.${name}.transport" "")

    if [[ -z "$transport" ]]; then
        echo "  [$name] SKIP — no transport defined"
        return
    fi

    TOTAL=$((TOTAL + 1))
    echo -n "  [$name] ($transport) ... "

    case "$transport" in
        stdio)
            local cmd
            cmd=$(read_config "server.${name}.command" "")
            if [[ -z "$cmd" ]]; then
                echo "FAIL — no command configured"
                FAILED=$((FAILED + 1))
                return
            fi

            # Check if the command executable exists
            local exec_path
            exec_path=$(echo "$cmd" | awk '{print $1}')

            if command -v "$exec_path" &>/dev/null || [[ -x "$exec_path" ]]; then
                echo "OK (executable found: $exec_path)"
                HEALTHY=$((HEALTHY + 1))
            else
                echo "FAIL (executable not found: $exec_path)"
                FAILED=$((FAILED + 1))
            fi
            ;;

        sse|streamable-http)
            local url
            url=$(read_config "server.${name}.url" "")
            if [[ -z "$url" ]]; then
                echo "FAIL — no URL configured"
                FAILED=$((FAILED + 1))
                return
            fi

            # HTTP connectivity check
            if command -v curl &>/dev/null; then
                local http_code
                http_code=$(curl -s -o /dev/null -w "%{http_code}" --connect-timeout "$TIMEOUT_SEC" "$url" 2>/dev/null || echo "000")

                if [[ "$VERBOSE" == "true" ]]; then
                    echo ""
                    echo "    URL: $url"
                    echo "    HTTP Status: $http_code"
                fi

                if [[ "$http_code" == "000" ]]; then
                    echo "FAIL (connection refused or timeout)"
                    FAILED=$((FAILED + 1))
                elif [[ "$http_code" -ge 200 && "$http_code" -lt 400 ]]; then
                    echo "OK (HTTP $http_code)"
                    HEALTHY=$((HEALTHY + 1))
                else
                    echo "WARN (HTTP $http_code — server responded but may have issues)"
                    HEALTHY=$((HEALTHY + 1))  # Server is reachable, even if error
                fi
            else
                echo "SKIP (curl not available for HTTP check)"
            fi
            ;;

        *)
            echo "SKIP — unknown transport '$transport'"
            ;;
    esac
}

for server in $SERVERS; do
    check_server "$server"
done

# --- Summary ------------------------------------------------------------------
echo ""
echo "=== Health Check Summary ==="
echo "Total   : $TOTAL"
echo "Healthy : $HEALTHY"
echo "Failed  : $FAILED"

if [[ $FAILED -gt 0 ]]; then
    echo "Status  : DEGRADED"
    exit 1
else
    echo "Status  : ALL HEALTHY"
    exit 0
fi
