# MCP Server Scripts

Reusable automation scripts for common MCP server operations. All scripts read
configuration from the **layered config system** (env vars > local config > base config).

## Directory Structure

```text
scripts/
├── server.sh                    ← 🔑 Lifecycle manager: start/stop/restart/reset/demo/logs
├── server.ps1                   ← 🔑 Lifecycle manager (Windows PowerShell)
├── setup.sh                     ← Setup wizard (run this first!)
├── setup.ps1                    ← Setup wizard (Windows)
├── common/                      ← Shared across all MCP servers
│   ├── browser/                 ← Browser lifecycle management
│   │   ├── launch-browser.sh    ← Launch auto-isolated browser (Linux/macOS)
│   │   ├── launch-browser.ps1   ← Launch auto-isolated browser (Windows)
│   │   ├── close-browser.sh     ← Stop MCP-managed browser (Linux/macOS)
│   │   ├── close-browser.ps1    ← Stop MCP-managed browser (Windows)
│   │   ├── create-profile.sh    ← Create additional profiles (Linux/macOS)
│   │   └── create-profile.ps1   ← Create additional profiles (Windows)
│   ├── auth/                    ← Authentication and token management
│   │   ├── token-check.sh       ← Verify API tokens are valid (Linux/macOS)
│   │   ├── Token-Check.ps1      ← Verify API tokens are valid (Windows)
│   │   └── oauth-flow.sh        ← OAuth2 authorization code flow helper
│   └── utils/                   ← Shared utilities and libraries
│       ├── read-config.sh       ← Layered config reader library (source in scripts)
│       ├── Read-Config.ps1      ← Layered config reader module (dot-source in scripts)
│       ├── validate-config.sh   ← Validate config for issues
│       └── health-check.sh      ← Check MCP server connectivity
├── server-specific/             ← Per-server scripts
│   ├── github/                  ← GitHub MCP server
│   │   └── test-connection.sh   ← Test GitHub API + server connectivity
│   └── README.md                ← Guide for adding server-specific scripts
├── .browser-pid                 ← (generated) Browser PID tracking file
└── README.md                    ← This file
```

## Quick Start

### 0. Lifecycle management (start/stop/restart)

The fastest way to control running servers from the terminal:

```bash
# Linux/macOS / Git Bash
./scripts/server.sh status                   # see what's running
./scripts/server.sh start  learning-resources
./scripts/server.sh stop   all
./scripts/server.sh restart atlassian
./scripts/server.sh reset  all               # stop → clean build → start
./scripts/server.sh demo   learning-resources  # foreground demo
./scripts/server.sh list-tools atlassian     # print all 27 tools
./scripts/server.sh logs   learning-resources  # tail live log
./scripts/server.sh validate                 # check env + config
```

```powershell
# Windows PowerShell
.\scripts\server.ps1 status
.\scripts\server.ps1 start  learning-resources
.\scripts\server.ps1 stop   all
.\scripts\server.ps1 restart atlassian
.\scripts\server.ps1 reset  all
.\scripts\server.ps1 demo   learning-resources
.\scripts\server.ps1 list-tools atlassian
.\scripts\server.ps1 logs   learning-resources
.\scripts\server.ps1 validate
```

Or use **VS Code Tasks** (`Ctrl+Shift+B` or `Terminal → Run Task`):

| Task label | What it does |
|---|---|
| `mcp-servers: status` | Show which servers are running |
| `mcp-servers: start (learning-resources)` | Start as background process |
| `mcp-servers: start (atlassian)` | Start Atlassian (credentials required) |
| `mcp-servers: start (all)` | Start all servers |
| `mcp-servers: stop (all)` | Stop all running servers |
| `mcp-servers: restart (learning-resources)` | Stop then start |
| `mcp-servers: reset (all)` | Stop → clean build → start all |
| `mcp-servers: demo (learning-resources)` | Foreground demo mode |
| `mcp-servers: list-tools (atlassian)` | Print all 27 tools |
| `mcp-servers: validate` | Check config + environment |
| `mcp-servers: logs (learning-resources)` | Tail live log |

### 1. Run the setup wizard

```bash
./scripts/setup.sh              # Linux/macOS/Git Bash
```

```powershell
.\scripts\setup.ps1             # Windows PowerShell
```

### 2. Set your API keys

Edit `user-config/mcp-config.local.properties` and add your tokens.

### 3. Validate your configuration

```bash
./scripts/common/utils/validate-config.sh --fix-suggestions
```

### 4. Launch an isolated browser

```bash
./scripts/common/browser/launch-browser.sh --url "https://github.com"
```

### 5. Check API tokens

```bash
./scripts/common/auth/token-check.sh
```

### 6. Test server connectivity

```bash
./scripts/common/utils/health-check.sh
```

## Script Categories

### Server Lifecycle Manager (`server.sh / server.ps1`)

The primary tool for controlling MCP server processes manually. VS Code auto-manages servers
via `.vscode/mcp.json` when Copilot calls a tool; use this script for local testing, smoke
checks, and working outside VS Code.

| Command | What it does |
|---------|-------------|
| `status` | Show running / stopped state for all (or named) server |
| `start <name>` | Launch server as background process; PID saved to `.pids/` |
| `stop [name\|all]` | Kill running process(es); clean up PID file |
| `restart <name>` | Atomic stop → start |
| `reset [name\|all]` | Stop → clean build → rebuild → start (nuclear option) |
| `demo <name>` | Run server in foreground demo mode (Ctrl-C to quit) |
| `list-tools <name>` | Print every MCP tool exposed by that server |
| `validate` | Check java, `out/`, config files, API keys |
| `logs <name>` | Tail live log file for that server |

**PID & log files:**
- PIDs: `scripts/.pids/<name>.pid` (auto-cleaned on stop)
- Logs: `scripts/.logs/<name>.log` + `.log.err`

### Setup Wizard (`setup.sh / setup.ps1`)

One-time setup that creates local config, browser data dir, detects browser, validates config.

### Browser Scripts (`common/browser/`)

| Script              | Purpose                                              |
|---------------------|------------------------------------------------------|
| `launch-browser`    | Launch browser with **automatic isolation**          |
| `close-browser`     | Gracefully stop the MCP-managed browser              |
| `create-profile`    | Create additional named profiles in MCP data dir     |

**Key features:**
- **Auto-isolation:** Uses `--user-data-dir` (Chromium) or `-profile` (Firefox) — your personal browser is never touched
- Reads `browser.*` config keys from layered config
- Auto-detects installed browsers (Chrome > Edge > Firefox > Brave)
- Supports: `new-window`, `incognito`, `app-mode`, `headless`
- Saves browser PID for clean shutdown via `close-browser`
- `--ephemeral` flag for temp sessions (no persistence)
- `--data-dir` override for custom browser data location

### Auth Scripts (`common/auth/`)

| Script          | Purpose                                            |
|-----------------|----------------------------------------------------|
| `token-check`   | Verify API keys are valid (GitHub + generic)       |
| `oauth-flow`    | Generic OAuth2 authorization code flow helper      |

**Key features:**
- Validates tokens against actual APIs (not just format)
- Token-check auto-discovers all `apiKeys.*` entries
- OAuth flow includes CSRF protection (state parameter)
- Client secrets read from env vars, NOT config files

### Utility Scripts (`common/utils/`)

| Script            | Purpose                                          |
|-------------------|--------------------------------------------------|
| `read-config`     | Shared library — source in your scripts          |
| `validate-config` | Check config for issues                          |
| `health-check`    | Verify MCP server connectivity                   |

**Key features:**
- `read-config` provides `read_config()`, `require_config()`, `has_config()`
- **Layered reading:** env var > local config > base config > default
- Validate-config checks: file existence, API keys, server definitions, browser config, profiles
- Health-check supports stdio (executable existence) and HTTP (endpoint reachability)

## Configuration Priority

All scripts follow this layered priority order:

1. **Environment variable** — `MCP_<KEY>` (dots → underscores, uppercase)
2. **Local config** — `user-config/mcp-config.local.properties` (gitignored)
3. **Base config** — `user-config/mcp-config.properties` (committed)
4. **Default value** — Provided in script code

Example: `browser.executable` is read as:
1. `MCP_BROWSER_EXECUTABLE` env var
2. `browser.executable=...` in local config
3. `browser.executable=...` in base config
4. Auto-detection fallback

## Cross-Platform Support

| Feature             | Linux/macOS (.sh) | Windows (.ps1) |
|---------------------|:-:|:-:|
| **Server lifecycle (start/stop/restart/reset/demo/logs)** | ✅ | ✅ |
| Setup wizard        | ✅ | ✅ |
| Launch browser      | ✅ | ✅ |
| Close browser       | ✅ | ✅ |
| Create profile      | ✅ | ✅ |
| Token check         | ✅ | ✅ |
| OAuth flow          | ✅ | — |
| Config validation   | ✅ | ✅ (via Git Bash/WSL fallback) |
| Health check        | ✅ | — |
| Config reader lib   | ✅ | ✅ |

> Scripts marked `—` can be added as needed. The `.sh` versions work in
> WSL (Windows Subsystem for Linux) and Git Bash as well.

## Adding New Scripts

### Using the shared config reader

**Bash:**

```bash
#!/usr/bin/env bash
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../../.." && pwd)"
source "$PROJECT_ROOT/scripts/common/utils/read-config.sh"

# Read a value (layered: env > local > base > default)
my_value=$(read_config "my.config.key" "default-value")

# Require a value (exits with error if missing from all layers)
required_value=$(require_config "apikey.myservice" "MyService API key")

# Check if a value exists
if has_config "server.myserver.url"; then
    echo "Server URL is configured"
fi
```

**PowerShell:**

```powershell
. "$PSScriptRoot\..\..\common\utils\Read-Config.ps1"

$value = Read-McpConfig -Key "my.config.key" -Default "default"
$required = Assert-McpConfig -Key "apikey.myservice" -Description "MyService API key"
$exists = Test-McpConfig -Key "server.myserver.url"
```

### Adding server-specific scripts

See [server-specific/README.md](server-specific/README.md) for conventions.
