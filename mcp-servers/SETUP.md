# Developer Setup Guide — MCP Servers

> **Time:** ~5 minutes  
> **Goal:** Get the MCP config system running with minimal effort.  
> **Prerequisite:** JDK 21+ ([Adoptium](https://adoptium.net/) or [Azul Zulu](https://www.azul.com/downloads/))

---

## Quick Start (Automated)

The fastest path — run the setup wizard:

```bash
# Linux / macOS / Git Bash
./scripts/setup.sh
```

```powershell
# Windows PowerShell
.\scripts\setup.ps1
```

The setup wizard will:
1. Create `mcp-config.local.properties` from the template (if missing)
2. Create the MCP browser data directory for auto-isolation
3. Auto-detect your browser
4. Check for API keys
5. Print what's ready and what needs attention

**After setup, you only need to do ONE thing:** set your API keys.

---

## Prerequisites

**Java 21+** is required for records, `var`, `Map.ofEntries()`, etc.

```bash
java -version   # Must show 21+

# If multiple JDKs, set JAVA_HOME:
export JAVA_HOME=/path/to/jdk-21        # Linux/Mac
$env:JAVA_HOME = "C:\path\to\jdk-21"     # Windows PowerShell
```

---

## Configuration System

This project uses **layered configuration** — you only need to provide secrets:

| File | Committed | Purpose |
|------|:---------:|---------|
| `user-config/mcp-config.properties` | **Yes** | Base config: safe defaults, empty secrets, full documentation |
| `user-config/mcp-config.local.properties` | No (gitignored) | Your secrets and machine-specific overrides |
| `user-config/mcp-config.local.example.properties` | **Yes** | Tiny template showing what to put in the local file |
| Environment variables (`MCP_*`) | — | Highest priority — overrides both files |

**Precedence (highest wins):**

```
  Environment vars (MCP_*)  →  local config  →  base config  →  defaults
```

---

## Setting API Keys

Choose **one** of these methods:

### Option A: Local config file (recommended for persistence)

Edit `user-config/mcp-config.local.properties`:

```properties
apiKeys.github=ghp_your_actual_token_here
server.github.env.GITHUB_TOKEN=ghp_your_actual_token_here
```

### Option B: Environment variables (recommended for CI/secrets managers)

```bash
# Linux/Mac
export MCP_APIKEYS_GITHUB="ghp_your_actual_token_here"

# Windows PowerShell
$env:MCP_APIKEYS_GITHUB = "ghp_your_actual_token_here"
```

### Option C: VS Code launch config

```jsonc
// In .vscode/launch.json → configurations[0].env:
"env": {
    "MCP_APIKEYS_GITHUB": "ghp_your_actual_token_here"
}
```

| Service | Key Format | Where to Generate |
|---------|-----------|-------------------|
| **GitHub** | `ghp_xxxxxxxxxxxx` | [github.com/settings/tokens](https://github.com/settings/tokens) |
| **OpenAI** | `sk-proj-xxxxxxxxxxxx` | [platform.openai.com/api-keys](https://platform.openai.com/api-keys) |

---

## Browser Isolation

Browser isolation is **automatic** — no manual profile creation needed.

The launch scripts use `--user-data-dir` (Chromium) or `-profile` (Firefox) to create
a completely separate browser instance. Your personal tabs, cookies, profiles, and
accounts are **never touched**.

| | Location |
|---|---|
| **Linux/Mac** | `~/.mcp/browser-data` |
| **Windows** | `%LOCALAPPDATA%\mcp\browser-data` |

Override via `browser.dataDir` in config or `MCP_BROWSER_DATADIR` env var.

---

## Build & Run

```bash
cd mcp-servers
javac -d out src/Main.java src/config/**/*.java
java -cp out Main
```

Or press `F5` in VS Code → select **"Run MCP Config Loader"**.

---

## Validate Configuration

```bash
./scripts/common/utils/validate-config.sh --fix-suggestions
```

---

## Copying to Another Project

Copy these folders to your target project:

```bash
cp -r mcp-servers/.vscode     /path/to/target/mcp-servers/.vscode
cp -r mcp-servers/user-config  /path/to/target/mcp-servers/user-config
cp -r mcp-servers/scripts      /path/to/target/mcp-servers/scripts
cp -r mcp-servers/src          /path/to/target/mcp-servers/src
```

Then run `./scripts/setup.sh` in the target project. That's it.

**Checklist after copying:**
- [ ] Run `./scripts/setup.sh` (creates local config, browser dir)
- [ ] Set API keys in `mcp-config.local.properties` or env vars
- [ ] Add to target `.gitignore`: `mcp-servers/user-config/mcp-config.local.properties`
- [ ] Verify `.vscode/settings.json` → `java.project.sourcePaths` matches your structure
