# MCP Servers Module

> **Purpose:** Configuration architecture and runtime for MCP (Model Context Protocol) servers.  
> **Location:** `mcp-servers/` in the learning-assistant project root.

---

## Table of Contents

- [Overview](#overview)
- [Quick Start](#quick-start)
- [Project Structure](#project-structure)
- [Configuration Guide](#configuration-guide)
  - [Layered Config System](#layered-config-system)
  - [API Keys & Secrets](#api-keys--secrets)
  - [Browser Auto-Isolation](#browser-auto-isolation)
  - [Server Definitions](#server-definitions)
  - [Profiles](#profiles)
  - [Environment Variable Overrides](#environment-variable-overrides)
- [Adding a New MCP Server](#adding-a-new-mcp-server)
- [Automation Scripts](#automation-scripts)
- [Config Architecture](#config-architecture)
  - [Model Classes](#model-classes)
  - [Loader Pipeline](#loader-pipeline)
  - [Validation](#validation)
  - [Profile Resolution](#profile-resolution)
- [Copying to Another Project](#copying-to-another-project)
- [Security](#security)
- [Troubleshooting](#troubleshooting)

> **First-time setup?** See [SETUP.md](SETUP.md) for a step-by-step walkthrough.

---

## Overview

This module provides a **Java-based configuration system** and **MCP server implementations** for learning assistance. It handles:

- Loading config from a **layered properties system** (base → local → env vars)
- Storing API keys, location preferences, browser settings, and user preferences
- Defining multiple MCP server connections (GitHub, filesystem, database, custom)
- Named profiles for switching between environments (development, production, testing)
- Validation of all configuration values before use
- **Automatic browser isolation** — user's personal browser is never touched

### Included MCP Servers

| Server | Description | Docs |
|--------|-------------|------|
| **Learning Resources** | Web scraper + curated vault of 30+ learning resources. Scrape, summarize, search, and browse tutorials, docs, blogs, and more. | [README](src/server/learningresources/README.md) |

---

## Quick Start

> **Prerequisite:** JDK 21+ — [Adoptium](https://adoptium.net/) or [Azul Zulu](https://www.azul.com/downloads/)

```bash
# 1. Run the setup wizard (creates local config, browser data dir):
./scripts/setup.sh              # Linux/macOS/Git Bash
.\scripts\setup.ps1             # Windows PowerShell

# 2. Add your GitHub token (choose one method):
#    a) Edit user-config/mcp-config.local.properties:
#       apiKeys.github=ghp_your_token_here
#    b) Or set env var:
export MCP_APIKEYS_GITHUB="ghp_your_token_here"       # Linux/Mac
$env:MCP_APIKEYS_GITHUB = "ghp_your_token_here"       # Windows

# 3. Build and run:
cd mcp-servers
javac -d out src/Main.java src/config/**/*.java src/server/**/*.java
java -cp out Main

# 4. Try the Learning Resources server:
java -cp out server.learningresources.LearningResourcesServer --demo
java -cp out server.learningresources.LearningResourcesServer --list-tools
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
├── user-config/                          ← ⚙️ Configuration files
│   ├── mcp-config.properties            ← Base config: safe defaults (COMMITTED)
│   ├── mcp-config.local.properties      ← Your secrets & overrides (GITIGNORED)
│   └── mcp-config.local.example.properties  ← Template for the local file (COMMITTED)
│
├── src/
│   ├── Main.java                         ← Entry point, loads & prints config
│   │
│   ├── config/                           ← ⚙️ Configuration system
│   │   ├── ConfigManager.java            ← Facade: load → merge → parse → validate → resolve
│   │   │
│   │   ├── model/                        ← Immutable config records (Java records)
│   │   │   ├── McpConfiguration.java     ← Root config object
│   │   │   ├── ApiKeyStore.java          ← Service name → API key map
│   │   │   ├── LocationPreferences.java  ← Timezone, locale, region
│   │   │   ├── UserPreferences.java      ← Theme, log level, retries, timeouts
│   │   │   ├── BrowserPreferences.java   ← Browser executable, profile, launch mode
│   │   │   ├── ServerDefinition.java     ← Per-server config (transport, command, URL)
│   │   │   ├── ProfileDefinition.java    ← Named override sets (dev, prod, testing)
│   │   │   ├── TransportType.java        ← Enum: STDIO, SSE, STREAMABLE_HTTP
│   │   │   └── package-info.java         ← Package documentation
│   │   │
│   │   ├── loader/                       ← Config loading pipeline
│   │   │   ├── ConfigSource.java         ← Interface for pluggable sources
│   │   │   ├── PropertiesConfigSource.java  ← Loads from .properties files (supports optional)
│   │   │   ├── EnvironmentConfigSource.java ← Loads MCP_* environment variables
│   │   │   └── ConfigParser.java         ← Flat properties → model records
│   │   │
│   │   ├── validation/                   ← Config correctness checks
│   │   │   ├── ConfigValidator.java      ← Validates servers, profiles, transports
│   │   │   └── ValidationResult.java     ← Error list with reporting
│   │   │
│   │   └── exception/                    ← Config-specific exceptions
│   │       ├── ConfigLoadException.java
│   │       └── ConfigValidationException.java
│   │
│   └── server/                           ← 🌐 MCP Server implementations
│       └── learningresources/            ← Learning Resources Server (first MCP server!)
│           ├── LearningResourcesServer.java  ← STDIO entry point (--demo, --list-tools)
│           ├── README.md                     ← Server documentation
│           ├── package-info.java             ← Package overview
│           │
│           ├── model/                    ← Domain models (Java records)
│           │   ├── LearningResource.java ← Core resource: title, url, type, categories
│           │   ├── ContentSummary.java   ← Scraped content summary with difficulty
│           │   ├── ResourceQuery.java    ← Search criteria with factory methods
│           │   ├── ResourceType.java     ← Enum: DOCUMENTATION, TUTORIAL, BLOG, ...
│           │   ├── ResourceCategory.java ← Enum: JAVA, PYTHON, WEB, DEVOPS, ...
│           │   └── package-info.java
│           │
│           ├── scraper/                  ← Web scraping (Java HttpClient)
│           │   ├── WebScraper.java       ← HTTP fetcher with timeouts & redirects
│           │   ├── ContentExtractor.java ← Regex-based HTML → plain text
│           │   ├── ScraperResult.java    ← Raw HTTP response record
│           │   └── ScraperException.java ← Custom scrape failure exception
│           │
│           ├── content/                  ← Content analysis & formatting
│           │   ├── ContentSummarizer.java   ← Scrape → extract → summarize pipeline
│           │   ├── ContentReader.java       ← Full/summary/preview formatters
│           │   └── ReadabilityScorer.java   ← Keyword + sentence + vocab scoring
│           │
│           ├── vault/                    ← Built-in resource library
│           │   ├── ResourceVault.java    ← ConcurrentHashMap store with search
│           │   └── BuiltInResources.java ← 30+ curated resources (Java, Web, DevOps, ...)
│           │
│           └── handler/                  ← MCP tool dispatch
│               ├── ToolHandler.java      ← Routes 7 tools via switch expression
│               ├── SearchHandler.java    ← Vault search, browse, details
│               └── ScrapeHandler.java    ← Scrape → summarize → format
│
├── scripts/                              ← 🔧 Automation scripts
│   ├── setup.sh / setup.ps1             ← Setup wizard (run this first!)
│   ├── common/
│   │   ├── browser/                     ← Browser lifecycle (launch, close, profile)
│   │   ├── auth/                        ← Token validation, OAuth flows
│   │   └── utils/                       ← Shared config reader, health check, validation
│   ├── server-specific/
│   │   └── github/                      ← GitHub MCP server scripts
│   └── README.md                        ← Script framework documentation
│
├── README.md                             ← This file
└── SETUP.md                              ← Step-by-step developer setup guide
```

> **Scripts documentation:** See [scripts/README.md](scripts/README.md) for full usage guide.

---

## Configuration Guide

### Layered Config System

Configuration uses a **3-layer merge strategy** (industry-standard pattern used by Spring Boot, Docker Compose, `.env` files):

| Layer | File | Committed | Purpose |
|:-----:|------|:---------:|---------|
| 1 (lowest) | `mcp-config.properties` | ✅ Yes | Safe defaults, empty secrets, full inline docs |
| 2 | `mcp-config.local.properties` | ❌ No | Developer's secrets and machine-specific overrides |
| 3 (highest) | Environment variables (`MCP_*`) | — | CI/CD, secrets managers, temporary overrides |

**How it works:** Each layer only needs to contain the keys it wants to override. The base config provides comprehensive defaults — the developer only supplies API keys.

### API Keys & Secrets

| Service | Key Format | Where to Generate |
|---------|-----------|-------------------|
| **GitHub** | `ghp_xxxxxxxxxxxx` (40 chars) | [github.com/settings/tokens](https://github.com/settings/tokens) |
| **OpenAI** | `sk-proj-xxxxxxxxxxxx` | [platform.openai.com/api-keys](https://platform.openai.com/api-keys) |
| **Database** | `postgresql://user:pass@host:port/db` | Your database admin |
| **Slack** | `xoxb-XXXX-XXXX-XXXX` | [api.slack.com/apps](https://api.slack.com/apps) → OAuth |

**Set via local config** (recommended):
```properties
# user-config/mcp-config.local.properties
apiKeys.github=ghp_abc123def456ghi789jkl012mno345pqr678
server.github.env.GITHUB_TOKEN=ghp_abc123def456ghi789jkl012mno345pqr678
```

**Or via env vars:**
```bash
export MCP_APIKEYS_GITHUB="ghp_abc123def456ghi789jkl012mno345pqr678"   # Linux/Mac
$env:MCP_APIKEYS_GITHUB = "ghp_abc123def456ghi789jkl012mno345pqr678"   # Windows
```

### Browser Auto-Isolation

MCP scripts **automatically** launch browsers in a dedicated data directory, completely
separate from your personal browser. No manual profile creation needed.

**How it works:**
- Chromium (Chrome/Edge/Brave): `--user-data-dir=<mcp-data-dir>` creates a separate process
- Firefox: `-profile <mcp-data-dir>/firefox-mcp --no-remote` creates a separate instance
- Your personal tabs, cookies, profiles, and accounts are **never touched**

**Default data directories:**

| Platform | Location |
|----------|----------|
| Linux/macOS | `~/.mcp/browser-data` |
| Windows | `%LOCALAPPDATA%\mcp\browser-data` |

**Override:** Set `browser.dataDir` in config or `MCP_BROWSER_DATADIR` env var.

**Ephemeral mode:** Use `--ephemeral` flag for temporary sessions (data deleted on close):
```bash
./scripts/common/browser/launch-browser.sh --ephemeral --url "https://example.com"
```

### Server Definitions

Each MCP server is configured as a `server.{name}.*` block in the base config:

**STDIO server (local subprocess):**
```properties
server.github.name=GitHub MCP Server
server.github.enabled=true
server.github.transport=stdio
server.github.command=npx
server.github.args=-y,@modelcontextprotocol/server-github
server.github.env.GITHUB_TOKEN=              # ← set in local config or env var
```

**SSE server (remote HTTP endpoint):**
```properties
server.custom-api.name=My Custom Server
server.custom-api.enabled=true
server.custom-api.transport=sse
server.custom-api.url=https://my-server.example.com/mcp/sse
```

### Profiles

Profiles let you maintain multiple configurations and switch with one line:

```properties
config.activeProfile=production

profile.production.description=Production with strict settings
profile.production.preferences.logLevel=WARN
profile.production.preferences.timeoutSeconds=15
profile.production.browser.headless=true
```

Profiles **merge** with base config — only specified keys are overridden.

### Environment Variable Overrides

Any config key can be overridden via environment variable with `MCP_` prefix:

| Config Key | Environment Variable |
|-----------|---------------------|
| `apiKeys.github` | `MCP_APIKEYS_GITHUB` |
| `preferences.logLevel` | `MCP_PREFERENCES_LOGLEVEL` |
| `server.github.command` | `MCP_SERVER_GITHUB_COMMAND` |
| `browser.dataDir` | `MCP_BROWSER_DATADIR` |

**Precedence (highest wins):**
1. Environment variables (`MCP_*`)
2. Local config overrides (`mcp-config.local.properties`)
3. Active profile overrides
4. Base config defaults (`mcp-config.properties`)

---

## Adding a New MCP Server

### 1. Add to base config

Add a `server.{your-name}.*` block to `user-config/mcp-config.properties`:

```properties
server.my-server.name=My New MCP Server
server.my-server.enabled=true
server.my-server.transport=stdio
server.my-server.command=npx
server.my-server.args=-y,@my-org/my-mcp-server
server.my-server.env.API_KEY=
```

### 2. Set the secret in local config

Add to `user-config/mcp-config.local.properties`:
```properties
server.my-server.env.API_KEY=your_actual_key_here
```

### 3. Verify

```bash
./scripts/common/utils/validate-config.sh --fix-suggestions
java -cp out Main
```

---

## Automation Scripts

The `scripts/` directory provides cross-platform automation for common MCP operations.

### Setup Wizard

```bash
./scripts/setup.sh              # Linux/macOS
.\scripts\setup.ps1             # Windows
```

### Available Scripts

| Category | Script | Purpose |
|----------|--------|---------|
| **Setup** | `setup.sh / setup.ps1` | One-time setup wizard |
| **Browser** | `launch-browser` | Launch auto-isolated browser |
| **Browser** | `close-browser` | Gracefully stop MCP-managed browser |
| **Browser** | `create-profile` | Create additional profiles in MCP data dir |
| **Auth** | `token-check` | Verify API keys against live APIs |
| **Auth** | `oauth-flow` | OAuth2 authorization code flow |
| **Utils** | `validate-config` | Check config for issues |
| **Utils** | `health-check` | Verify MCP server connectivity |
| **Utils** | `read-config` | Shared library for layered config reading |

### Quick Usage

```bash
# Launch isolated browser
./scripts/common/browser/launch-browser.sh --url "https://github.com"

# Check API tokens
./scripts/common/auth/token-check.sh

# Check server health
./scripts/common/utils/health-check.sh
```

```powershell
# Windows
.\scripts\common\browser\launch-browser.ps1 -Url "https://github.com"
.\scripts\common\auth\Token-Check.ps1
```

> **Full documentation:** See [scripts/README.md](scripts/README.md)

---

## Config Architecture

### Model Classes

All models are **Java records** — immutable, compact, with defensive copies:

```
McpConfiguration (root)                     ← Config System
├── ApiKeyStore             Map<String, String> of service → key
├── LocationPreferences     timezone, locale, region
├── UserPreferences         theme, logLevel, maxRetries, timeoutSeconds, autoConnect
├── BrowserPreferences      executable, profile, account, launchMode, headless, ...
├── Map<String, ServerDefinition>
│   └── ServerDefinition    name, enabled, transport, command, args, url, envVars
│       └── TransportType   STDIO | SSE | STREAMABLE_HTTP
└── Map<String, ProfileDefinition>
    └── ProfileDefinition   name, description, preferences, location, browser, serverOverrides

LearningResourcesServer                     ← Learning Resources Server
├── ResourceVault           ConcurrentHashMap store with search & filter
│   └── BuiltInResources    30+ curated resources (Java, Web, DevOps, AI/ML, ...)
├── ToolHandler             Routes 7 MCP tools via switch expression
│   ├── SearchHandler       Vault search, browse, details, categories
│   └── ScrapeHandler       Web scrape → summarize → format pipeline
├── Model records:
│   ├── LearningResource    title, url, type, categories, tags, difficulty
│   ├── ContentSummary      scraped text, summary, word count, reading time
│   ├── ResourceQuery       search criteria with static factory methods
│   ├── ResourceType        DOCUMENTATION | TUTORIAL | BLOG | VIDEO | ...
│   └── ResourceCategory    JAVA | PYTHON | WEB | DEVOPS | CLOUD | ...
├── WebScraper              Java HttpClient with timeouts, redirects
├── ContentExtractor        Regex-based HTML → plain text
├── ContentSummarizer       Extract → summarize → score pipeline
├── ContentReader           Full / summary / preview formatters
└── ReadabilityScorer       Keyword + sentence + vocabulary scoring
```

### Loader Pipeline

```
┌─────────────────┐   ┌──────────────────┐   ┌──────────────────┐   ┌────────────┐
│  Base Properties │ → │  Local Properties │ → │  Env Variables   │ → │   Merged   │
│  (committed)     │   │  (gitignored)    │   │  (MCP_* prefix)  │   │ Properties │
└─────────────────┘   └──────────────────┘   └──────────────────┘   └──────┬─────┘
                                                                           │
                                                                    ┌──────▼──────┐
                                                                    │ ConfigParser │
                                                                    │ (flat→model) │
                                                                    └──────┬──────┘
                                                                           │
                                                                    ┌──────▼──────┐
                                                                    │  Validator   │
                                                                    └──────┬──────┘
                                                                           │
                                                                    ┌──────▼──────────┐
                                                                    │ Profile Resolve  │
                                                                    │ (merge overrides)│
                                                                    └─────────────────┘
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
2. Overlays profile preferences (only non-default values)
3. Overlays profile location preferences
4. Overlays profile browser preferences
5. Merges profile server overrides

---

## Copying to Another Project

This module is **portable**. Copy these folders:

```bash
cp -r mcp-servers/.vscode     /path/to/target/mcp-servers/.vscode
cp -r mcp-servers/user-config  /path/to/target/mcp-servers/user-config
cp -r mcp-servers/scripts      /path/to/target/mcp-servers/scripts
cp -r mcp-servers/src          /path/to/target/mcp-servers/src
```

Then run `./scripts/setup.sh` and set your API keys. Done.

**Checklist:**

| Step | Action |
|------|--------|
| 1 | Run `./scripts/setup.sh` |
| 2 | Set API keys in `mcp-config.local.properties` or env vars |
| 3 | Add to `.gitignore`: `mcp-servers/user-config/mcp-config.local.properties` |
| 4 | (Optional) Verify `.vscode/settings.json` source paths |

---

## Security

| Concern | Protection |
|---------|-----------|
| **API keys in code** | Secrets go in `mcp-config.local.properties` (gitignored) or env vars |
| **Base config safety** | `mcp-config.properties` contains only safe defaults — empty strings for secrets |
| **Runtime secrets** | Environment variables (`MCP_*`) override file values and are not logged |
| **Browser isolation** | Auto-isolation via `--user-data-dir` — completely separate process |
| **Validation** | Config validated before use — missing required values produce clear errors |

---

## Troubleshooting

| Problem | Solution |
|---------|---------|
| `ConfigLoadException: Config file not found` | Base config should be committed. Run `git checkout -- user-config/mcp-config.properties` |
| `ConfigValidationException: No MCP servers defined` | Add at least one `server.{name}.*` block in base config |
| `Server uses STDIO but has no command` | Set `server.{name}.command=npx` |
| `Server uses SSE but has no URL` | Set `server.{name}.url=https://...` |
| API key not working | Check: `echo $MCP_APIKEYS_GITHUB` or check `mcp-config.local.properties` |
| Browser opens in personal profile | Ensure you use `launch-browser.sh` / `.ps1` (auto-isolation) |
| No browser found | Set `browser.executable` in local config or install Chrome/Edge/Firefox |
| Want temporary browser session | Use `--ephemeral` flag with launch-browser |
