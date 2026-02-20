# MCP Servers Module

> **Purpose:** Configuration architecture and runtime for MCP (Model Context Protocol) servers.  
> **Location:** `mcp-servers/` in the learning-assistant project root.

---

## Table of Contents

- [Overview](#overview)
- [Quick Start](#quick-start)
- [Project Structure](#project-structure)
- [Configuration Guide](#configuration-guide)
  - [Properties File Setup](#properties-file-setup)
  - [API Keys & Secrets](#api-keys--secrets)
  - [Browser Isolation](#browser-isolation)
  - [Server Definitions](#server-definitions)
  - [Profiles](#profiles)
  - [Environment Variable Overrides](#environment-variable-overrides)
- [Adding a New MCP Server](#adding-a-new-mcp-server)
- [Config Architecture](#config-architecture)
  - [Model Classes](#model-classes)
  - [Loader Pipeline](#loader-pipeline)
  - [Validation](#validation)
  - [Profile Resolution](#profile-resolution)
- [Placeholder Convention](#placeholder-convention)
- [Copying to Another Project](#copying-to-another-project)
- [Security](#security)
- [Troubleshooting](#troubleshooting)

> **First-time setup?** See [SETUP.md](SETUP.md) for a step-by-step walkthrough.

---

## Overview

This module provides a **Java-based configuration system** for managing MCP server connections. It handles:

- Loading config from `.properties` files and environment variables
- Storing API keys, location preferences, browser settings, and user preferences
- Defining multiple MCP server connections (GitHub, filesystem, database, custom)
- Named profiles for switching between environments (development, production, testing)
- Validation of all configuration values before use
- Browser isolation to prevent MCP servers from interfering with personal browsing

---

## Quick Start

> **Prerequisite:** JDK 21+ — [Adoptium](https://adoptium.net/) or [Azul Zulu](https://www.azul.com/downloads/)

```bash
# 1. Copy the example config
cp user-config/mcp-config.example.properties user-config/mcp-config.properties

# 2. Replace placeholders (search for "<<<" in the file)
#    At minimum, set your GitHub token:
#    apiKeys.github=ghp_your_actual_token_here
#    server.github.env.GITHUB_TOKEN=ghp_your_actual_token_here

# 3. Or use environment variables instead (recommended for secrets):
export MCP_APIKEYS_GITHUB="ghp_your_actual_token_here"       # Linux/Mac
$env:MCP_APIKEYS_GITHUB = "ghp_your_actual_token_here"       # Windows PowerShell

# 4. Run the application
cd mcp-servers
javac -d out src/**/*.java
java -cp out Main
```

---

## Project Structure

```
mcp-servers/
├── .vscode/
│   ├── settings.json                    ← IDE settings (copy to other projects)
│   ├── launch.json                      ← Run/debug configurations
│   └── extensions.json                  ← Recommended VS Code extensions
│
├── user-config/                          ← ⚙️ DEVELOPER-CONFIGURABLE (your settings)
│   ├── mcp-config.example.properties    ← Full reference template (committed)
│   └── mcp-config.properties            ← Your active config (GITIGNORED)
│
├── src/
│   ├── Main.java                         ← Entry point, loads & prints config
│   └── config/
│       ├── ConfigManager.java            ← Facade: load → merge → parse → validate → resolve
│       │
│       ├── model/                        ← Immutable config records (Java records)
│       │   ├── McpConfiguration.java     ← Root config object
│       │   ├── ApiKeyStore.java          ← Service name → API key map
│       │   ├── LocationPreferences.java  ← Timezone, locale, region
│       │   ├── UserPreferences.java      ← Theme, log level, retries, timeouts
│       │   ├── BrowserPreferences.java   ← Browser executable, profile, launch mode
│       │   ├── ServerDefinition.java     ← Per-server config (transport, command, URL)
│       │   ├── ProfileDefinition.java    ← Named override sets (dev, prod, testing)
│       │   ├── TransportType.java        ← Enum: STDIO, SSE, STREAMABLE_HTTP
│       │   └── package-info.java         ← Package documentation
│       │
│       ├── loader/                       ← Config loading pipeline
│       │   ├── ConfigSource.java         ← Interface for pluggable sources
│       │   ├── PropertiesConfigSource.java  ← Loads from .properties files
│       │   ├── EnvironmentConfigSource.java ← Loads MCP_* environment variables
│       │   └── ConfigParser.java         ← Flat properties → model records
│       │
│       ├── validation/                   ← Config correctness checks
│       │   ├── ConfigValidator.java      ← Validates servers, profiles, transports
│       │   └── ValidationResult.java     ← Error list with reporting
│       │
│       └── exception/                    ← Config-specific exceptions
│           ├── ConfigLoadException.java
│           └── ConfigValidationException.java
│
├── README.md                             ← This file
└── SETUP.md                              ← Step-by-step developer setup guide
```

---

## Configuration Guide

### Properties File Setup

1. **Copy the template:**
   ```bash
   cp user-config/mcp-config.example.properties user-config/mcp-config.properties
   ```

2. **Find all placeholders** — search for `<<<` in the file:
   ```bash
   grep "<<<" user-config/mcp-config.properties
   ```

3. **Replace each `<<<PLACEHOLDER>>>` with your actual value.**

The example file (`mcp-config.example.properties`) contains **extensive inline documentation** for every key — including valid values, format examples, and generation URLs for tokens.

### API Keys & Secrets

| Service | Key Format | Where to Generate |
|---------|-----------|-------------------|
| **GitHub** | `ghp_xxxxxxxxxxxx` (40 chars) | [github.com/settings/tokens](https://github.com/settings/tokens) |
| **OpenAI** | `sk-proj-xxxxxxxxxxxx` | [platform.openai.com/api-keys](https://platform.openai.com/api-keys) |
| **Database** | `postgresql://user:pass@host:port/db` | Your database admin |
| **Slack** | `xoxb-XXXX-XXXX-XXXX` | [api.slack.com/apps](https://api.slack.com/apps) → OAuth |

**Never commit real keys.** Use environment variables:

```bash
# Linux/Mac
export MCP_APIKEYS_GITHUB="ghp_abc123def456ghi789jkl012mno345pqr678"

# Windows PowerShell
$env:MCP_APIKEYS_GITHUB = "ghp_abc123def456ghi789jkl012mno345pqr678"
```

### Browser Isolation

MCP servers that need a browser (OAuth flows, web scraping, previews) can open tabs in your personal browser — disrupting your work. **Create an isolated browser profile:**

**Chrome / Edge:**
1. Open `chrome://settings/manageProfile` (or `edge://settings/profiles`)
2. Click **Add Person** → name it `MCP-Isolated`
3. Set in config: `browser.profile=MCP-Isolated`

**Firefox:**
1. Open `about:profiles`
2. Click **Create a New Profile** → name it `MCP-Isolated`
3. Set in config: `browser.profile=MCP-Isolated`

**Config example:**
```properties
browser.executable=chrome
browser.profile=MCP-Isolated
browser.launchMode=new-window
browser.headless=false
```

### Server Definitions

Each MCP server is configured as a `server.{name}.*` block:

**STDIO server (local subprocess):**
```properties
server.github.name=GitHub MCP Server
server.github.enabled=true
server.github.transport=stdio
server.github.command=npx
server.github.args=-y,@modelcontextprotocol/server-github
server.github.env.GITHUB_TOKEN=ghp_your_token_here
```

**SSE server (remote HTTP endpoint):**
```properties
server.custom-api.name=My Custom Server
server.custom-api.enabled=true
server.custom-api.transport=sse
server.custom-api.url=https://my-server.example.com/mcp/sse
```

**Streamable HTTP server:**
```properties
server.stream.name=Streaming Server
server.stream.enabled=true
server.stream.transport=streamable-http
server.stream.url=https://my-server.example.com/mcp/stream
```

### Profiles

Profiles let you maintain multiple configurations and switch with one line:

```properties
# Switch profile:
config.activeProfile=production

# Define the profile:
profile.production.description=Production with strict settings
profile.production.preferences.logLevel=WARN
profile.production.preferences.timeoutSeconds=15
profile.production.browser.headless=true
```

Profiles **merge** with base config — only specified keys are overridden.

### Environment Variable Overrides

Any config key can be overridden via an environment variable with `MCP_` prefix:

| Config Key | Environment Variable |
|-----------|---------------------|
| `apiKeys.github` | `MCP_APIKEYS_GITHUB` |
| `preferences.logLevel` | `MCP_PREFERENCES_LOGLEVEL` |
| `server.github.command` | `MCP_SERVER_GITHUB_COMMAND` |
| `browser.profile` | `MCP_BROWSER_PROFILE` |

**Precedence (highest wins):**
1. Environment variables (`MCP_*`)
2. Active profile overrides
3. Base properties file

---

## Adding a New MCP Server

### 1. Add to config file

Add a `server.{your-name}.*` block to `user-config/mcp-config.properties`:

```properties
# --- My New Server ---
server.my-server.name=My New MCP Server
server.my-server.enabled=true
server.my-server.transport=stdio
server.my-server.command=npx
server.my-server.args=-y,@my-org/my-mcp-server
server.my-server.env.API_KEY=your_api_key
```

### 2. Verify with validation

The config system automatically validates:
- STDIO servers have a `command` configured
- SSE/HTTP servers have a `url` configured
- Active profile references an existing profile name
- At least one server is defined

### 3. Test

Run the application — the config summary will show your new server:

```
=== MCP Configuration Summary ===
Servers        : 3 defined
  - github [stdio] (enabled)
  - filesystem [stdio] (enabled)
  - my-server [stdio] (enabled)      ← your new server
=================================
```

---

## Config Architecture

### Model Classes

All models are **Java records** — immutable, compact, with defensive copies:

```
McpConfiguration (root)
├── ApiKeyStore             Map<String, String> of service → key
├── LocationPreferences     timezone, locale, region
├── UserPreferences         theme, logLevel, maxRetries, timeoutSeconds, autoConnect
├── BrowserPreferences      executable, profile, account, launchMode, headless, ...
├── Map<String, ServerDefinition>
│   └── ServerDefinition    name, enabled, transport, command, args, url, envVars
│       └── TransportType   STDIO | SSE | STREAMABLE_HTTP
└── Map<String, ProfileDefinition>
    └── ProfileDefinition   name, description, preferences, location, browser, serverOverrides
```

### Loader Pipeline

```
┌──────────────────┐    ┌──────────────────┐    ┌────────────┐
│  Properties File │ →  │  Env Variables   │ →  │   Merged   │
│  (base values)   │    │  (MCP_* prefix)  │    │ Properties │
└──────────────────┘    └──────────────────┘    └──────┬─────┘
                                                       │
                                                       ▼
                                                ┌────────────┐
                                                │ ConfigParser│
                                                │ (flat → model)│
                                                └──────┬─────┘
                                                       │
                                                       ▼
                                                ┌────────────┐
                                                │ Validator  │
                                                └──────┬─────┘
                                                       │
                                                       ▼
                                                ┌────────────────┐
                                                │ Profile Resolve│
                                                │ (merge overrides)│
                                                └────────────────┘
```

### Validation

`ConfigValidator` checks:
- Active profile references an existing profile name
- STDIO servers have a non-empty `command`
- SSE/HTTP servers have a non-empty `url`
- Server names are not blank
- At least one server is defined

### Profile Resolution

When a profile is active, `ConfigManager.resolveEffectiveConfig()`:
1. Starts with base config values
2. Overlays profile preferences (only non-default values from the profile)
3. Overlays profile location preferences
4. Overlays profile browser preferences
5. Merges profile server overrides (profile servers replace same-named base servers)

---

## Placeholder Convention

All placeholder values in config files follow a consistent, searchable pattern:

| Pattern | Meaning | Action |
|---------|---------|--------|
| `<<<YOUR_GITHUB_PERSONAL_ACCESS_TOKEN>>>` | Required — must be replaced | Replace with your actual value |
| `<<<ABSOLUTE_PATH_TO_ALLOWED_DIRECTORY>>>` | Required — must be replaced | Replace with an actual path |
| `<<<https://your-server.example.com/...>>>` | Required if server is enabled | Replace with your server URL |
| _(empty value)_ | Optional — system default used | Leave empty or fill in |
| _(actual value like `dark`, `INFO`, `30`)_ | Sensible default | Change only if needed |

**Quick find:** Search for `<<<` in any config file to find all required placeholders.

---

## Copying to Another Project

This module is designed to be **portable**. Copy these 3 folders into any project:

```bash
# From the learning-assistant root:
cp -r mcp-servers/.vscode     /path/to/target/mcp-servers/.vscode
cp -r mcp-servers/user-config  /path/to/target/mcp-servers/user-config
cp -r mcp-servers/src          /path/to/target/mcp-servers/src
```

**After copying — developer checklist:**

| Step | Action | File to Edit |
|------|--------|-------------|
| 1 | Copy example → active config | `user-config/mcp-config.example.properties` → `user-config/mcp-config.properties` |
| 2 | Replace all `<<<PLACEHOLDER>>>` values | `user-config/mcp-config.properties` |
| 3 | Add gitignore entries | Target project's `.gitignore` |
| 4 | (Optional) Set env vars for secrets | `.vscode/launch.json` or shell profile |
| 5 | (Optional) Create browser isolation profile | Your browser's profile manager |
| 6 | Verify source paths match your layout | `.vscode/settings.json` → `java.project.sourcePaths` |

> **Detailed step-by-step:** See [SETUP.md](SETUP.md)

### What the Developer Must Manually Configure

| File | What to Change | Why |
|------|---------------|-----|
| `user-config/mcp-config.properties` | API keys, server definitions, paths | These are unique to your environment |
| `.vscode/launch.json` | Environment variables for secrets (optional) | Alternative to putting keys in the properties file |
| `.vscode/settings.json` | `java.project.sourcePaths` (only if your project structure differs) | Ensures Java compilation finds source files |
| Target `.gitignore` | Add `mcp-servers/user-config/mcp-config.properties` | Prevents committing your secrets |

---

## Security

| Concern | Protection |
|---------|-----------|
| **API keys in code** | `user-config/mcp-config.properties` is in `.gitignore` — never committed |
| **Template safety** | `user-config/mcp-config.example.properties` contains only `<<<PLACEHOLDER>>>` values |
| **Runtime secrets** | Environment variables (`MCP_*`) override file values and are not logged |
| **Browser isolation** | Dedicated profile prevents cookie/session leakage with personal accounts |
| **Validation** | Config is validated before use — missing required values cause clear errors |

---

## Troubleshooting

| Problem | Solution |
|---------|---------|
| `ConfigLoadException: Config file not found` | Copy `user-config/mcp-config.example.properties` to `user-config/mcp-config.properties` |
| `ConfigValidationException: No MCP servers defined` | Add at least one `server.{name}.*` block |
| `Server uses STDIO but has no command` | Set `server.{name}.command=npx` (or your server binary) |
| `Server uses SSE but has no URL` | Set `server.{name}.url=https://...` |
| `Active profile 'X' is not defined` | Ensure `profile.X.description=...` exists, or set `config.activeProfile=` to empty |
| API key not working | Check env var is set: `echo $MCP_APIKEYS_GITHUB` / `echo $env:MCP_APIKEYS_GITHUB` |
| Browser opens in wrong profile | Set `browser.profile=MCP-Isolated` and create the profile first |
| Placeholder `<<<...>>>` in runtime | You forgot to replace a placeholder — search for `<<<` |
