#!/usr/bin/env bash
# =============================================================================
# server.sh — MCP Server Lifecycle Manager (Linux / macOS / Git Bash)
# =============================================================================
# Controls the two Java MCP server processes in this workspace:
#   learning-resources  →  server.learningresources.LearningResourcesServer
#   atlassian           →  server.atlassian.AtlassianServer
#
# PID files live in scripts/.pids/   Logs live in scripts/.logs/
#
# USAGE
#   ./scripts/server.sh <command> [server]
#
# COMMANDS
#   status  [server|all]   Show which servers are running
#   start    <server>      Start server as a background process
#   stop    [server|all]   Stop running server(s)
#   restart  <server>      Stop then start
#   reset   [server|all]   Stop → clean build → rebuild → start
#   demo     <server>      Foreground demo mode  (Ctrl-C to quit)
#   list-tools <server>    Print all MCP tools and exit
#   validate               Check config + environment
#   logs     <server>      Tail the server log file
#   help                   Show this message
#
# EXAMPLES
#   ./scripts/server.sh status
#   ./scripts/server.sh start  learning-resources
#   ./scripts/server.sh stop   all
#   ./scripts/server.sh restart atlassian
#   ./scripts/server.sh reset  all
#   ./scripts/server.sh demo   learning-resources
#   ./scripts/server.sh list-tools atlassian
#   ./scripts/server.sh logs   learning-resources
#   ./scripts/server.sh validate
# =============================================================================

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
MCP_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
OUT_DIR="$MCP_ROOT/out"
PIDS_DIR="$SCRIPT_DIR/.pids"
LOGS_DIR="$SCRIPT_DIR/.logs"
BUILD_SCRIPT="$MCP_ROOT/build.sh"

mkdir -p "$PIDS_DIR" "$LOGS_DIR"

# ── Colors ────────────────────────────────────────────────────────────────────
if [[ -t 1 ]]; then
    RED='\033[0;31m'; YELLOW='\033[1;33m'; GREEN='\033[0;32m'
    CYAN='\033[0;36m'; GRAY='\033[0;37m'; RESET='\033[0m'
else
    RED=''; YELLOW=''; GREEN=''; CYAN=''; GRAY=''; RESET=''
fi

# ── Server registry ───────────────────────────────────────────────────────────
declare -A SERVER_CLASS SERVER_DESC SERVER_CREDS

SERVER_CLASS[learning-resources]="server.learningresources.LearningResourcesServer"
SERVER_DESC[learning-resources]="Learning Resources — curated vault + smart search + web scraper"
SERVER_CREDS[learning-resources]="false"

SERVER_CLASS[atlassian]="server.atlassian.AtlassianServer"
SERVER_DESC[atlassian]="Atlassian — Jira + Confluence + Bitbucket (27 tools)"
SERVER_CREDS[atlassian]="true"

ALL_SERVERS=("learning-resources" "atlassian")

# ── Helpers ───────────────────────────────────────────────────────────────────
header() { echo -e "\n${CYAN}  $1\n  $(printf '─%.0s' $(seq 1 ${#1}))${RESET}"; }
ok()     { echo -e "  ${GREEN}[OK]${RESET}  $*"; }
warn()   { echo -e "  ${YELLOW}[WARN]${RESET} $*"; }
err()    { echo -e "  ${RED}[ERROR]${RESET} $*"; }
info()   { echo -e "  ${GRAY}$*${RESET}"; }

pid_file() { echo "$PIDS_DIR/$1.pid"; }
log_file() { echo "$LOGS_DIR/$1.log"; }

get_pid() {
    local f; f="$(pid_file "$1")"
    [[ -f "$f" ]] && cat "$f" || echo ""
}

is_running() {
    local pid; pid="$(get_pid "$1")"
    [[ -n "$pid" ]] && kill -0 "$pid" 2>/dev/null
}

find_java() {
    # Load build.env.local overrides
    local env_local="$MCP_ROOT/build.env.local"
    if [[ -f "$env_local" ]]; then
        while IFS='=' read -r key val; do
            [[ "$key" =~ ^[[:space:]]*# ]] && continue
            [[ -z "$key" ]] && continue
            key="${key// /}"; val="${val// /}"
            export "$key=$val"
        done < "$env_local"
    fi

    if [[ -n "${JAVA_HOME:-}" && -x "$JAVA_HOME/bin/java" ]]; then
        echo "$JAVA_HOME/bin/java"
        return
    fi
    command -v java 2>/dev/null || echo ""
}

assert_built() {
    if [[ ! -d "$OUT_DIR" ]]; then
        warn "No compiled output found. Building first..."
        invoke_build
    fi
}

invoke_build() {
    local clean="${1:-}"
    echo -e "  ${CYAN}Building MCP servers...${RESET}"
    local args=()
    [[ "$clean" == "--clean" ]] && args+=("--clean")
    bash "$BUILD_SCRIPT" "${args[@]}"
    ok "Build complete."
}

assert_known_server() {
    [[ -n "${SERVER_CLASS[$1]+_}" ]] && return 0
    err "Unknown server '$1'. Known: ${ALL_SERVERS[*]}"
    exit 1
}

# ── Commands ──────────────────────────────────────────────────────────────────
cmd_status() {
    local filter="${1:-all}"
    header "MCP Server Status"
    local java; java="$(find_java)"
    info "java : ${java:-NOT FOUND}"
    info "out/ : $( [[ -d "$OUT_DIR" ]] && echo "exists" || echo "missing — run build first")"
    echo ""

    for name in "${ALL_SERVERS[@]}"; do
        [[ "$filter" != "all" && "$filter" != "$name" ]] && continue
        if is_running "$name"; then
            echo -e "  ${GREEN}[RUNNING]${RESET}  $name"
            info "  ${SERVER_DESC[$name]}"
            info "  PID : $(get_pid "$name")"
            info "  Log : $(log_file "$name")"
        else
            echo -e "  ${GRAY}[STOPPED]${RESET}  $name"
            info "  ${SERVER_DESC[$name]}"
        fi
        echo ""
    done
}

cmd_start() {
    local name="$1"
    assert_known_server "$name"

    if is_running "$name"; then
        warn "[$name] Already running (PID $(get_pid "$name")). Use 'restart' to bounce it."
        return
    fi

    assert_built

    local java; java="$(find_java)"
    if [[ -z "$java" ]]; then
        err "java not found. Install JDK 21+ and set JAVA_HOME."
        exit 1
    fi

    local main_class="${SERVER_CLASS[$name]}"
    local log; log="$(log_file "$name")"
    local pid_f; pid_f="$(pid_file "$name")"

    echo -e "  ${CYAN}Starting $name...${RESET}"

    # Launch in background, detached
    nohup "$java" -cp "out" "$main_class" >"$log" 2>"$log.err" &
    local proc_pid=$!
    echo "$proc_pid" > "$pid_f"

    sleep 0.8
    if kill -0 "$proc_pid" 2>/dev/null; then
        ok "[$name] Started (PID $proc_pid)"
        info "Log : $log"
    else
        err "[$name] Failed to start. Check log: $log"
        rm -f "$pid_f"
        exit 1
    fi
}

cmd_stop() {
    local filter="${1:-all}"
    local targets=()
    if [[ "$filter" == "all" ]]; then
        targets=("${ALL_SERVERS[@]}")
    else
        assert_known_server "$filter"
        targets=("$filter")
    fi

    for name in "${targets[@]}"; do
        if ! is_running "$name"; then
            info "[$name] Not running."
            continue
        fi
        local pid; pid="$(get_pid "$name")"
        kill "$pid" 2>/dev/null || true
        sleep 0.3
        kill -9 "$pid" 2>/dev/null || true
        rm -f "$(pid_file "$name")"
        echo -e "  ${YELLOW}[$name] Stopped (PID $pid).${RESET}"
    done
}

cmd_restart() {
    local name="$1"
    assert_known_server "$name"
    echo -e "  ${CYAN}Restarting $name...${RESET}"
    cmd_stop "$name"
    sleep 0.4
    cmd_start "$name"
}

cmd_reset() {
    local filter="${1:-all}"
    echo -e "  ${CYAN}Resetting: $filter...${RESET}"
    cmd_stop "$filter"
    invoke_build "--clean"
    if [[ "$filter" == "all" ]]; then
        for name in "${ALL_SERVERS[@]}"; do cmd_start "$name"; done
    else
        cmd_start "$filter"
    fi
}

cmd_demo() {
    local name="$1"
    assert_known_server "$name"
    assert_built
    local java; java="$(find_java)"
    local main_class="${SERVER_CLASS[$name]}"
    echo -e "  ${CYAN}Running $name in demo mode (Ctrl-C to stop)...${RESET}\n"
    cd "$MCP_ROOT"
    exec "$java" -cp "out" "$main_class" --demo
}

cmd_list_tools() {
    local name="$1"
    assert_known_server "$name"
    assert_built
    local java; java="$(find_java)"
    local main_class="${SERVER_CLASS[$name]}"
    echo -e "  ${CYAN}Tools for $name:${RESET}\n"
    cd "$MCP_ROOT"
    "$java" -cp "out" "$main_class" --list-tools
}

cmd_validate() {
    header "Config Validation"

    local java; java="$(find_java)"
    if [[ -n "$java" ]]; then ok "java    : $java"
    else err "java    : NOT FOUND — Install JDK 21+ and set JAVA_HOME"; fi

    if [[ -d "$OUT_DIR" ]]; then ok "out/    : exists"
    else warn "out/    : missing — run build first"; fi

    local validate_sh="$SCRIPT_DIR/common/utils/validate-config.sh"
    if [[ -f "$validate_sh" ]]; then
        echo ""
        bash "$validate_sh" --fix-suggestions || true
    else
        warn "validate-config.sh not found at $validate_sh"
    fi
}

cmd_logs() {
    local name="$1"
    assert_known_server "$name"
    local log; log="$(log_file "$name")"
    if [[ ! -f "$log" ]]; then
        warn "No log file for $name. Start the server first."
        return
    fi
    info "Log: $log (Ctrl-C to stop)"
    tail -f -n 40 "$log"
}

cmd_help() {
    cat <<EOF

  MCP Server Lifecycle Manager
  ─────────────────────────────────────────────────────────────

  USAGE
    ./scripts/server.sh <command> [server]

  COMMANDS
    status  [server|all]        Show which servers are running
    start    <server>           Start server as background process
    stop    [server|all]        Stop running server(s)
    restart  <server>           Stop then start
    reset   [server|all]        Stop → clean build → rebuild → start
    demo     <server>           Run server in foreground demo mode
    list-tools <server>         Print all MCP tools and exit
    validate                    Check config + environment
    logs     <server>           Tail the server log file
    help                        Show this message

  SERVERS
    learning-resources          Curated vault + smart search (no credentials)
    atlassian                   Jira + Confluence + Bitbucket (credentials required)
    all                         All servers (for stop / status / reset)

  EXAMPLES
    ./scripts/server.sh status
    ./scripts/server.sh start  learning-resources
    ./scripts/server.sh stop   all
    ./scripts/server.sh restart atlassian
    ./scripts/server.sh reset  all
    ./scripts/server.sh demo   learning-resources
    ./scripts/server.sh list-tools atlassian
    ./scripts/server.sh logs   learning-resources
    ./scripts/server.sh validate

  NOTE
    VS Code auto-manages these servers via .vscode/mcp.json.
    Use this script for local testing and smoke-checking.
EOF
}

# ── Dispatch ──────────────────────────────────────────────────────────────────
CMD="${1:-help}"
ARG="${2:-all}"

cd "$MCP_ROOT"

case "$CMD" in
    status)     cmd_status     "$ARG" ;;
    start)      cmd_start      "$ARG" ;;
    stop)       cmd_stop       "$ARG" ;;
    restart)    cmd_restart    "$ARG" ;;
    reset)      cmd_reset      "$ARG" ;;
    demo)       cmd_demo       "$ARG" ;;
    list-tools) cmd_list_tools "$ARG" ;;
    validate)   cmd_validate ;;
    logs)       cmd_logs       "$ARG" ;;
    help|-h|--help) cmd_help ;;
    *)
        err "Unknown command '$CMD'. Run './scripts/server.sh help' for usage."
        exit 1
        ;;
esac
