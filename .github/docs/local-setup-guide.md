# Local Developer Setup Guide — What Lives on Your Machine (Not in the Repo)

> **The single source of truth for setting up your local environment from scratch.**
>
> This repo uses a layered config strategy: safe defaults are committed; secrets and
> machine-specific paths are **gitignored** and live only on your local disk.
> This guide tells you exactly what to create, why, and how.
>
> **Pick your tier:**
> - 🟢 **Tier 1 — Newbie:** You just cloned the repo and want to run something
> - 🟡 **Tier 2 — Amateur:** You're configuring external services (Atlassian, GitHub, etc.)
> - 🔴 **Tier 3 — Pro:** You're running multiple server instances, writing new servers, or doing CI/CD

---

## Table of Contents

1. [The Layered Config Philosophy](#1-the-layered-config-philosophy)
2. [Complete Map — Every Gitignored File](#2-complete-map--every-gitignored-file)
3. [🟢 Tier 1 — Minimal Setup (just build + run)](#3--tier-1--minimal-setup-just-build--run)
4. [🟡 Tier 2 — Service Configuration (Atlassian, GitHub, etc.)](#4--tier-2--service-configuration)
5. [🔴 Tier 3 — Advanced (multiple instances, new servers, CI/CD)](#5--tier-3--advanced-setup)
6. [Config Precedence — How Values Are Resolved](#6-config-precedence--how-values-are-resolved)
7. [Secret Management — Industry Best Practices](#7-secret-management--industry-best-practices)
8. [Troubleshooting — Common Setup Problems](#8-troubleshooting--common-setup-problems)

---

## 1. The Layered Config Philosophy

This project follows the **12-Factor App** pattern (industry standard for
cloud-native applications) and the **Spring Boot** layered config convention:

```
Priority 1 (highest): Environment variables  (MCP_* prefix)
Priority 2:           .local.properties files  (gitignored — your secrets)
Priority 3 (lowest):  .properties files         (committed — safe defaults)
```

**Why three layers?**

| Layer | Who writes it | Committed? | Contains |
|-------|--------------|------------|---------|
| `.properties` | Repository maintainer | ✅ Yes | Safe defaults, structure, comments |
| `.local.properties` | Each developer | ❌ No (gitignored) | Real credentials, machine paths |
| Env vars | CI/CD systems / shell | ❌ No | Same values as .local, for automation |

This means:
- The repo is **safe to push publicly** — no secrets ever land in git
- Each developer independently fills in their own credentials
- CI/CD uses environment variables (no file creation needed)
- Switching between environments (dev, prod) is a line change or an env var

**The same pattern used by:** Spring Boot (`application.properties` +
`application-local.properties`), Dotenv (`.env` + `.env.example`),
Kubernetes Secrets, AWS Parameter Store, HashiCorp Vault.

---

## 2. Complete Map — Every Gitignored File

```
learning-assistant/
│
├── .idea/                              ❌ GITIGNORED — IntelliJ metadata (auto-generated)
├── *.iml                               ❌ GITIGNORED — IntelliJ module files (auto-generated)
│
├── mcp-servers/
│   ├── out/                            ❌ GITIGNORED — compiled .class files (build output)
│   │
│   ├── build.env.local                 ❌ GITIGNORED — your machine's JDK path
│   │   └── template: build.env.example (✅ committed)
│   │
│   ├── scripts/
│   │   └── .browser-pid                ❌ GITIGNORED — PID file written by launch-browser script
│   │
│   └── user-config/
│       ├── mcp-config.properties       ✅ COMMITTED — base defaults, no secrets
│       ├── mcp-config.local.properties ❌ GITIGNORED — YOUR secrets and overrides
│       │   └── template: mcp-config.local.example.properties (✅ committed)
│       │
│       └── servers/
│           └── atlassian/
│               ├── atlassian-config.properties             ✅ COMMITTED — base defaults
│               └── atlassian-config.local.properties       ❌ GITIGNORED — YOUR credentials
│                   └── template: atlassian-config.local.example.properties (✅ committed)
│
├── brain/
│   └── ai-brain/
│       ├── inbox/                      ❌ GITIGNORED — session-scoped scratch notes
│       ├── notes/                      ✅ TRACKED — curated knowledge notes (committed to repo)
│       └── library/                    ✅ TRACKED — permanent reference library (project-organized)
│
└── .env, .env.*                        ❌ GITIGNORED — raw env files (never commit)
```

**Files you MUST create (by copying templates):**

| Must-create file | Copy from | Required for |
|-----------------|-----------|--------------|
| `mcp-servers/build.env.local` | `build.env.example` | Building with `build.ps1` / `build.sh` if your JAVA_HOME isn't set |
| `mcp-servers/user-config/mcp-config.local.properties` | `mcp-config.local.example.properties` | Any external API (GitHub, OpenAI, etc.) |
| `mcp-servers/user-config/servers/atlassian/atlassian-config.local.properties` | `atlassian-config.local.example.properties` | Atlassian server (Jira/Confluence/Bitbucket) |

---

## 3. 🟢 Tier 1 — Minimal Setup (just build + run)

### What you need

Just one file, only if your `JAVA_HOME` environment variable isn't set:

### Step 1 — Create `build.env.local`

```bash
# Windows
copy mcp-servers\build.env.example mcp-servers\build.env.local

# Linux / macOS
cp mcp-servers/build.env.example mcp-servers/build.env.local
```

Edit `build.env.local` and set your JDK path:

```properties
# The only thing this file does — tells build scripts where your JDK 21+ is
JAVA_HOME=C:\path\to\your\jdk-21
```

> **Windows examples:**
> ```
> JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.5.11-hotspot
> JAVA_HOME=E:\JDKs\jdk-21.0.7
> ```
>
> **Linux/macOS examples:**
> ```
> JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
> JAVA_HOME=/opt/homebrew/opt/openjdk@21
> ```

> **Skip this file entirely** if you've already set `JAVA_HOME` as a system environment
> variable. The build scripts check `JAVA_HOME` first before reading `build.env.local`.

### Step 2 — Build

```powershell
# Windows
cd mcp-servers
.\build.ps1

# Linux / macOS
cd mcp-servers
./build.sh
```

Expected output:
```
Compiling 150 source files  ->  out/
BUILD SUCCESS -- compiled 150 files
```

### Step 3 — Run (Learning Resources server — no credentials needed)

```powershell
java -cp out server.learningresources.LearningResourcesServer
```

The Learning Resources server has **zero external dependencies** — no API keys,
no network calls on startup. It runs entirely from the built-in resource vault
of 48+ learning resources.

### What you now have

```
mcp-servers/
├── build.env.local          ← ✅ your file (gitignored)
└── out/                     ← ✅ build output (gitignored)
    ├── server/
    │   ├── learningresources/…
    │   └── atlassian/…
    └── config/…
```

---

## 4. 🟡 Tier 2 — Service Configuration

### 4.1 Main Config — `mcp-config.local.properties`

This file holds your API keys and overrides for any external services.

**Create it:**
```bash
# Windows
copy mcp-servers\user-config\mcp-config.local.example.properties mcp-servers\user-config\mcp-config.local.properties

# Linux / macOS
cp mcp-servers/user-config/mcp-config.local.example.properties mcp-servers/user-config/mcp-config.local.properties
```

**Content reference — fill in what you need:**

```properties
# ─── GitHub MCP Server ─────────────────────────────────────────────────────
# Generate at: https://github.com/settings/tokens
# Scopes needed: repo (private) OR public_repo (public only)
apiKeys.github=ghp_YourGitHubPersonalAccessTokenHere
server.github.env.GITHUB_TOKEN=ghp_YourGitHubPersonalAccessTokenHere

# ─── OpenAI ────────────────────────────────────────────────────────────────
# Generate at: https://platform.openai.com/api-keys
apiKeys.openai=sk-proj-YourOpenAIKeyHere

# ─── Machine-specific overrides ────────────────────────────────────────────
# Only needed if the committed default doesn't match your machine

# browser.executable=C:\Program Files\Google\Chrome\Application\chrome.exe
# server.filesystem.args=-y,@modelcontextprotocol/server-filesystem,C:\Users\me\myproject
# location.timezone=Europe/London
```

**Keys reference:**

| Key | Where to get the value | Notes |
|-----|----------------------|-------|
| `apiKeys.github` | [github.com/settings/tokens](https://github.com/settings/tokens) | Scopes: `repo` or `public_repo` |
| `apiKeys.openai` | [platform.openai.com/api-keys](https://platform.openai.com/api-keys) | Pay-as-you-go |
| `apiKeys.slack` | Slack App → OAuth & Permissions → Bot Token | `xoxb-…` prefix |
| `apiKeys.database` | Your DBA / connection string | `postgresql://user:pass@host:5432/db` |
| `browser.executable` | Auto-detected by default | Only set if Chrome isn't found |
| `location.timezone` | [IANA timezone list](https://en.wikipedia.org/wiki/List_of_tz_database_time_zones) | e.g., `Europe/London` |


### 4.2 Atlassian Config — `atlassian-config.local.properties`

The Atlassian server (Jira + Confluence + Bitbucket) has its own config file
because it has more complex auth (Cloud vs Data Center vs Server) and multiple
product URLs.

**Create it:**
```bash
# Windows
copy mcp-servers\user-config\servers\atlassian\atlassian-config.local.example.properties ^
     mcp-servers\user-config\servers\atlassian\atlassian-config.local.properties

# Linux / macOS
cp mcp-servers/user-config/servers/atlassian/atlassian-config.local.example.properties \
   mcp-servers/user-config/servers/atlassian/atlassian-config.local.properties
```

**Scenario A — Atlassian Cloud (most common):**

```properties
atlassian.instance.name=my-work
atlassian.variant=cloud

# Your atlassian.net subdomain
atlassian.jira.baseUrl=https://your-company.atlassian.net
atlassian.confluence.baseUrl=https://your-company.atlassian.net/wiki
atlassian.bitbucket.baseUrl=https://api.bitbucket.org

# API Token — generate at https://id.atlassian.com/manage-profile/security/api-tokens
atlassian.auth.type=api_token
atlassian.auth.email=you@yourcompany.com
atlassian.auth.token=ATATT3xFfGF0YourActualAPITokenHere

atlassian.product.jira.enabled=true
atlassian.product.confluence.enabled=true
atlassian.product.bitbucket.enabled=false
```

**Scenario B — Atlassian Data Center (self-hosted):**

```properties
atlassian.instance.name=company-dc
atlassian.variant=data_center

atlassian.jira.baseUrl=https://jira.yourcompany.com
atlassian.confluence.baseUrl=https://confluence.yourcompany.com
atlassian.bitbucket.baseUrl=https://bitbucket.yourcompany.com

# Personal Access Token — Jira: Profile → Personal Access Tokens → Create
atlassian.auth.type=pat
atlassian.auth.email=
atlassian.auth.token=YourPersonalAccessTokenHere
```

**Scenario C — Legacy Atlassian Server (on-premises, single node):**

```properties
atlassian.instance.name=legacy-server
atlassian.variant=server

atlassian.jira.baseUrl=https://jira.yourcompany.com
atlassian.confluence.baseUrl=https://confluence.yourcompany.com

# For Jira >= 8.14: use PAT. For older: use api_token with username:password
atlassian.auth.type=api_token
atlassian.auth.email=your_username
atlassian.auth.token=your_password_or_PAT
```

**Generate your Atlassian API token:**
> Go to [id.atlassian.com/manage-profile/security/api-tokens](https://id.atlassian.com/manage-profile/security/api-tokens)
> → Create API token → Give it a name (e.g., "MCP Local Dev") → Copy the token

### 4.3 VS Code / AI Client Config

For the MCP servers to connect to your AI client (Claude Desktop, VS Code Copilot,
Continue.dev), you need to register them in the AI client's config file.

**Claude Desktop** (`%APPDATA%\Claude\claude_desktop_config.json` on Windows):
```json
{
  "mcpServers": {
    "learning-resources": {
      "command": "java",
      "args": ["-cp", "E:/path/to/mcp-servers/out", "server.learningresources.LearningResourcesServer"]
    },
    "atlassian": {
      "command": "java",
      "args": ["-cp", "E:/path/to/mcp-servers/out", "server.atlassian.AtlassianServer"]
    }
  }
}
```

**VS Code** (`.vscode/mcp.json`, already configured in this repo):
```json
{
  "servers": {
    "learning-resources": {
      "type": "stdio",
      "command": "java",
      "args": ["-cp", "${workspaceFolder}/mcp-servers/out", "server.learningresources.LearningResourcesServer"]
    }
  }
}
```

---

## 5. 🔴 Tier 3 — Advanced Setup

### 5.1 Multiple Atlassian Instances Side-by-Side

To run two different Atlassian instances (e.g., your company Cloud + a colleague's DC):

**Step 1 — Create a second config directory:**
```
mcp-servers/user-config/servers/
├── atlassian/                      ← primary (Cloud)
│   ├── atlassian-config.properties
│   └── atlassian-config.local.properties   ← your Cloud credentials (gitignored)
└── atlassian-dc/                   ← new directory for DC instance
    ├── atlassian-config.properties          ← copy from atlassian/
    └── atlassian-config.local.properties   ← DC credentials (gitignored)
```

**Step 2 — Register second instance in `mcp-config.local.properties`:**
```properties
server.atlassian-dc.name=Atlassian DC
server.atlassian-dc.enabled=true
server.atlassian-dc.transport=stdio
server.atlassian-dc.command=java
server.atlassian-dc.args=-cp,out,server.atlassian.AtlassianServer
# Points to the new directory
server.atlassian-dc.env.ATLASSIAN_CONFIG_DIR=user-config/servers/atlassian-dc
```

**Step 3 — Register both in your AI client:**
```json
{
  "mcpServers": {
    "atlassian-cloud": { "command": "java", "args": ["-cp", "out", "server.atlassian.AtlassianServer"] },
    "atlassian-dc":    { "command": "java", "args": ["-cp", "out", "server.atlassian.AtlassianServer",
                         "--config", "user-config/servers/atlassian-dc"] }
  }
}
```

### 5.2 Adding a Brand-New Server with Its Own Config

When you add a new server (e.g., `server.github` in Java), create its own
config directory following the same three-file pattern:

```
user-config/servers/github/
├── github-config.properties              ← committed (safe defaults)
├── github-config.local.properties        ← gitignored (your token)
└── github-config.local.example.properties ← committed (template for new devs)
```

Add to your `.gitignore`:
```
mcp-servers/user-config/servers/github/*.local.properties
```

### 5.3 CI/CD — Using Environment Variables (No Files)

In GitHub Actions, Jenkins, or any CI system, use environment variables instead
of `.local.properties` files. The config loader reads `MCP_*` env vars at
**highest priority**, overriding all file-based config.

```yaml
# GitHub Actions example
env:
  MCP_APIKEYS_GITHUB: ${{ secrets.GITHUB_TOKEN }}
  MCP_SERVER_ATLASSIAN_ENV_ATLASSIAN_API_TOKEN: ${{ secrets.ATLASSIAN_TOKEN }}
  MCP_SERVER_ATLASSIAN_ENV_ATLASSIAN_EMAIL: ${{ secrets.ATLASSIAN_EMAIL }}
  MCP_SERVER_ATLASSIAN_ENV_ATLASSIAN_JIRA_BASE_URL: ${{ secrets.JIRA_URL }}
  JAVA_HOME: /usr/lib/jvm/java-21-openjdk-amd64
```

**Environment variable naming:**
```
Property key:          apiKeys.github
→ Env var:             MCP_APIKEYS_GITHUB
  Rule: prefix MCP_ + replace dots with underscores + uppercase

Property key:          server.atlassian.env.ATLASSIAN_TOKEN
→ Env var:             MCP_SERVER_ATLASSIAN_ENV_ATLASSIAN_TOKEN
```

### 5.4 Config for a New Developer on the Team

When a new developer joins, here's the complete onboarding checklist:

```
□ 1. Clone the repo
□ 2. Create build.env.local                  (copy build.env.example, set JAVA_HOME)
□ 3. Run: cd mcp-servers && .\build.ps1       (verify BUILD SUCCESS)
□ 4. Create mcp-config.local.properties       (copy example, add API keys you need)
□ 5. Create atlassian-config.local.properties (copy example, add Atlassian creds)
□ 6. Register servers in your AI client config
□ 7. Test: java -cp out server.learningresources.LearningResourcesServer
```

---

## 6. Config Precedence — How Values Are Resolved

When the Java server starts, `ConfigManager` loads configuration in this order
(highest wins):

```
┌─────────────────────────────────────────────────────────────────┐
│  Priority 1 (HIGHEST)                                           │
│  Environment variables: MCP_APIKEYS_GITHUB, MCP_BROWSER_*, etc. │
│  → Best for CI/CD, containers, 12-Factor deployments           │
├─────────────────────────────────────────────────────────────────┤
│  Priority 2                                                     │
│  mcp-config.local.properties  (GITIGNORED)                      │
│  → Your personal secrets, overrides, machine-specific paths     │
│  → Also: servers/atlassian/atlassian-config.local.properties    │
├─────────────────────────────────────────────────────────────────┤
│  Priority 3 (LOWEST)                                            │
│  mcp-config.properties  (COMMITTED)                             │
│  → Shared team defaults, safe placeholders                      │
│  → Also: servers/atlassian/atlassian-config.properties          │
└─────────────────────────────────────────────────────────────────┘
```

**Example — how `atlassian.auth.token` gets resolved:**

```
1. Is MCP_SERVER_ATLASSIAN_ENV_ATLASSIAN_AUTH_TOKEN set?  → Use it
2. Is atlassian-config.local.properties present?           → Use its value
3. Fall back to atlassian-config.properties value          → (blank = error on startup)
```

---

## 7. Secret Management — Industry Best Practices

### What NEVER goes in git

```
✗ Real API tokens, passwords, PATs
✗ Connection strings with credentials (postgresql://user:pass@…)
✗ .pem files, .key files, .jks keystores
✗ .env files (unless .env.example / .env.template)
✗ build.env.local (machine-specific)
✗ *.local.properties files (per-developer secrets)
```

### Local secret storage options (choose one)

| Approach | Best for | How |
|----------|---------|-----|
| `.local.properties` | Individual developers | Copy example → fill in → gitignored |
| Environment variables | CI/CD, Docker, k8s | `export MCP_APIKEYS_GITHUB=…` |
| OS Keychain / Credential Manager | High-security local | Read via shell into env var |
| AWS Parameter Store / Secrets Manager | Cloud deployments | Inject at container start |
| HashiCorp Vault | Enterprise | Agent sidecar → env var injection |
| dotenv (`.env` file) | Node/Python projects | `require('dotenv').config()` |

### Rotating compromised secrets

If a secret is accidentally committed to git:

```bash
# 1. Immediately revoke the exposed token (GitHub, Atlassian, OpenAI portal)
# 2. Generate a new token
# 3. Update your .local.properties with the new token
# 4. Purge from git history (if you need to):
git filter-branch --force --index-filter \
  "git rm --cached --ignore-unmatch path/to/secretfile" \
  --prune-empty --tag-name-filter cat -- --all
git push --force-with-lease
# 5. Consider the old token permanently burned even after purge
#    (GitHub caches, forks, bots may have scraped it)
```

---

## 8. Troubleshooting — Common Setup Problems

### Build fails: `javac: command not found` or `JAVA_HOME is not set`

```bash
# Check if JAVA_HOME is set
echo $JAVA_HOME   # Linux/macOS
echo %JAVA_HOME%  # Windows CMD

# Fix: create build.env.local and set JAVA_HOME
# OR set JAVA_HOME as a system env var and reopen your terminal
```

### Server starts but Atlassian tools fail with 401 Unauthorized

```
Cause:  Wrong token, wrong email, or wrong auth.type for your variant.
Fix 1:  Check atlassian.auth.type matches your instance:
          Cloud       → api_token (email + API token from id.atlassian.com)
          Data Center → pat       (PAT from Jira/Confluence profile settings)
Fix 2:  Confirm atlassian.jira.baseUrl has no trailing slash
Fix 3:  Regenerate your API token — they expire or get revoked
```

### Server starts but can't find `atlassian-config.local.properties`

```
Error: Config file not found: user-config/servers/atlassian/atlassian-config.local.properties

Fix:  cp atlassian-config.local.example.properties atlassian-config.local.properties
      (from inside user-config/servers/atlassian/)
```

### AI client doesn't show MCP tools

```
1. Verify the server process starts without errors:
   java -cp out server.atlassian.AtlassianServer

2. Check the AI client config JSON is valid (no trailing commas)

3. Restart the AI client after config changes

4. Check the client's MCP log for connection errors
   Claude Desktop: ~/Library/Logs/Claude/ (macOS) or %APPDATA%\Claude\logs\ (Windows)
```

### `mcp-config.local.properties` not being picked up

```
Cause:  Wrong working directory when starting the server.
Fix:    Start the server from mcp-servers/  directory, not from learning-assistant/
        cd mcp-servers && java -cp out server.atlassian.AtlassianServer
```

---

## Quick Reference Card

```
File                                              Committed?   You Create?   Contains
────────────────────────────────────────────────────────────────────────────────────────
mcp-config.properties                             ✅ Yes       ❌ No        Safe defaults
mcp-config.local.properties                       ❌ No        ✅ Yes       Your API keys
mcp-config.local.example.properties               ✅ Yes       ❌ No        Template
atlassian-config.properties                       ✅ Yes       ❌ No        Atlassian defaults
atlassian-config.local.properties                 ❌ No        ✅ Yes       Your Atlassian creds
atlassian-config.local.example.properties         ✅ Yes       ❌ No        Template
build.env.local                                   ❌ No        ✅ Maybe     Your JAVA_HOME
build.env.example                                 ✅ Yes       ❌ No        Template
mcp-servers/out/                                  ❌ No        ✅ Auto      Compiled classes
brain/ai-brain/inbox/                             ❌ No        Optional     Session scratch notes
brain/ai-brain/notes/                             ✅ Yes       ❌ No        Curated knowledge notes (committed)
brain/ai-brain/library/                           ✅ Yes       ❌ No        Permanent reference library (project-organized)
.idea/                                            ❌ No        ✅ Auto (IDE) IntelliJ workspace
```

---

*See also:*
- [mcp-how-it-works.md](mcp-how-it-works.md) — how MCP protocol works
- [versioning-guide.md](versioning-guide.md) — adding v2 server versions
- [mcp-ecosystem.md](mcp-ecosystem.md) — combining servers, LLM APIs, LangChain, agents
- [mcp-server-setup.md](mcp-server-setup.md) — complete first-time setup walkthrough
