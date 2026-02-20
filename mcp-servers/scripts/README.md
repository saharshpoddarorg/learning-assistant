# MCP Server Scripts

Reusable automation scripts for common MCP server operations. All scripts read
configuration from the **layered config system** (env vars > local config > base config).

## Directory Structure

```
scripts/
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
| Setup wizard        | ✅ | ✅ |
| Launch browser      | ✅ | ✅ |
| Close browser       | ✅ | ✅ |
| Create profile      | ✅ | ✅ |
| Token check         | ✅ | ✅ |
| OAuth flow          | ✅ | — |
| Config validation   | ✅ | — |
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
