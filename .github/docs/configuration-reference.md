# Configuration Reference

> **The single source of truth** for every config file, environment variable,
> configurable path, and credential in this project.
>
> **Read level:** 🟢 All levels — structured from essentials to advanced.

---

## Table of Contents

- [Quick Start — What Must I Configure?](#quick-start--what-must-i-configure)
- [Configuration Hierarchy](#configuration-hierarchy)
- [1. Required Configuration](#1-required-configuration)
  - [1a. Copilot Instructions](#1a-copilot-instructions)
- [2. Feature Configuration (enable what you need)](#2-feature-configuration-enable-what-you-need)
  - [2a. MCP Servers — Build](#2a-mcp-servers--build)
  - [2b. MCP Servers — Registry](#2b-mcp-servers--registry)
  - [2c. Brain Workspace — Path](#2c-brain-workspace--path)
  - [2d. Skills & Prompts — Zero Config](#2d-skills--prompts--zero-config)
  - [2e. Session Capture — Structure](#2e-session-capture--structure)
- [3. Optional — Atlassian Integration](#3-optional--atlassian-integration)
  - [3a. Atlassian v1 (API Token)](#3a-atlassian-v1-api-token)
  - [3b. Atlassian v2 (OAuth 2.0 / API Token)](#3b-atlassian-v2-oauth-20--api-token)
  - [3c. Atlassian Skill CLI (PAT — Server/Data Center)](#3c-atlassian-skill-cli-pat--serverdata-center)
- [4. Optional — GitHub MCP Server](#4-optional--github-mcp-server)
- [5. Optional — MCP Advanced Config](#5-optional--mcp-advanced-config)
- [6. Config Precedence Rules](#6-config-precedence-rules)
- [7. Environment Variables — Complete Reference](#7-environment-variables--complete-reference)
- [8. Config Files — Complete Inventory](#8-config-files--complete-inventory)
- [9. Security Rules](#9-security-rules)
- [10. Export Checklist — Migrating to Another Project](#10-export-checklist--migrating-to-another-project)

---

## Quick Start — What Must I Configure?

**If you just want Copilot customization** (no MCP servers, no brain):

| Step | What | Time |
|---|---|---|
| 1 | Edit `.github/copilot-instructions.md` for your project | 5 min |

**If you want Copilot + MCP servers:**

| Step | What | Time |
|---|---|---|
| 1 | Edit `.github/copilot-instructions.md` for your project | 5 min |
| 2 | Set `JAVA_HOME` if `java` is not on PATH | 2 min |
| 3 | Run `.\mcp-servers\build.ps1` | 30 sec |
| 4 | Enable servers in `.vscode/mcp.json` (`disabled: false`) | 1 min |

**If you want everything:**

| Step | What | Time |
|---|---|---|
| 1-4 | All of the above | ~8 min |
| 5 | Configure brain path (if not at `brain/ai-brain`) | 2 min |
| 6 | Add Atlassian credentials (if using Jira/Confluence) | 5 min |
| 7 | Add GitHub PAT (if using GitHub MCP server) | 3 min |

---

## Configuration Hierarchy

All configuration follows a layered hierarchy. Higher layers override lower layers.

```text
┌──────────────────────────────────────────────────────────────┐
│  LAYER 3 (highest): Environment Variables                    │
│  MCP_*, BRAIN_PATH, JAVA_HOME, GITHUB_TOKEN                 │
│  → Set in shell profile, .env, or system settings            │
├──────────────────────────────────────────────────────────────┤
│  LAYER 2: Local Config Files (gitignored)                    │
│  *.local.properties, build.env.local                         │
│  → User-specific: credentials, machine paths, overrides      │
├──────────────────────────────────────────────────────────────┤
│  LAYER 1 (lowest): Committed Config Files                    │
│  *.properties, .vscode/mcp.json, .vscode/tasks.json          │
│  → Shared defaults: safe to commit, no secrets                │
└──────────────────────────────────────────────────────────────┘
```

**Key principle:** Committed files contain safe defaults. Local files add secrets.
Environment variables override everything (useful for CI/CD or per-session overrides).

---

## 1. Required Configuration

### 1a. Copilot Instructions

| Item | Value |
|---|---|
| **File** | `.github/copilot-instructions.md` |
| **Purpose** | Project-wide Copilot coding rules, conventions, structure |
| **Action** | Edit to match your project's language, conventions, and team rules |
| **Sensitive** | No |
| **Tracked** | Yes — committed to git |

This is the only truly required configuration. Everything else is opt-in.

---

## 2. Feature Configuration (enable what you need)

### 2a. MCP Servers — Build

**Only needed if you use MCP servers (Atlassian, GitHub, Learning Resources).**

| Item | Value |
|---|---|
| **File** | `mcp-servers/build.env.local` |
| **Template** | `mcp-servers/build.env.example` |
| **Purpose** | Set `JAVA_HOME` for the MCP server Java build |
| **When needed** | Only if `java` is not on your system PATH |
| **Sensitive** | No (filesystem path only) |
| **Tracked** | No — gitignored |

**Setup:**

```powershell
Copy-Item mcp-servers\build.env.example mcp-servers\build.env.local
# Edit build.env.local: set JAVA_HOME=C:\path\to\jdk-21
```

**Alternative:** Set `JAVA_HOME` as a system environment variable instead.

### 2b. MCP Servers — Registry

| Item | Value |
|---|---|
| **File** | `.vscode/mcp.json` |
| **Purpose** | Registers MCP servers with VS Code — which servers run, their paths, env vars |
| **Action** | Set `"disabled": false` for servers you want to use |
| **Sensitive** | No — secrets come from VS Code input prompts, not this file |
| **Tracked** | Yes — committed to git |

**Servers defined:**

| Server | Default State | Credentials | Enable When |
|---|---|---|---|
| `learning-resources` | Disabled | None needed | Always safe to enable |
| `atlassian` | Disabled | API token in `.local.properties` | When using Jira/Confluence (v1) |
| `atlassian-v2` | Disabled | OAuth or API token in `.local.properties` | When using Jira/Confluence (v2, recommended) |
| `github` | Disabled | PAT via VS Code prompt | When using GitHub search/issues |

### 2c. Brain Workspace — Path

| Item | Value |
|---|---|
| **Env var** | `BRAIN_PATH` |
| **Default** | `brain/ai-brain` (relative to repo root) |
| **Purpose** | Location of the brain knowledge workspace |
| **When needed** | Only if brain is at a non-default location (e.g., after export) |
| **Sensitive** | No |
| **Technology** | Neutral — markdown + shell scripts only (not a Java/npm/Python module) |

> **Module note:** In this repo, the brain workspace lives inside a Java module
> (`brain/`). When exported, only `brain/ai-brain/` is needed — it is technology-neutral
> and works in any project structure. See
> [Export Guide § 3](export-guide.md#3-brain-workspace) for copy instructions.

**The brain path affects these files** (all need updating if path changes):

| File | What to change |
|---|---|
| `.github/copilot-instructions.md` | Configurable Paths table + Project Structure diagram |
| `.github/instructions/backlog.instructions.md` | `applyTo` pattern |
| `.vscode/tasks.json` | Brain task command paths |
| `.gitignore` | Inbox exclusion pattern |

**Scripts auto-detect:** Brain scripts find their own root from their filesystem location
via `git rev-parse`. The `BRAIN_PATH` env var is an optional override, not required.

See [copilot-instructions.md § Brain Workspace in Different Project Structures](../copilot-instructions.md)
for module/package/monorepo scenarios.

### 2d. Skills & Prompts — Zero Config

Skills (`.github/skills/*/SKILL.md`) and prompts (`.github/prompts/*.prompt.md`) have
**no config files** — they activate automatically by file presence. Configuration is
simply which files you keep after export.

| Item | Action when exporting |
|---|---|
| **Skills** | Delete skill folders you don't need (e.g., `mac-dev/` for non-Mac teams) |
| **Prompts** | Delete `.prompt.md` files for commands you don't use |
| **Agents** | Delete `.agent.md` files for personas you don't need |
| **Instructions** | Delete `.instructions.md` for languages/frameworks you don't use |

**No credentials, no env vars, no config files.** Just copy `.github/` and prune.

See [Export Guide § 7](export-guide.md#7-what-you-can-safely-skip) for what to keep
vs. remove for work and personal repos.

### 2e. Session Capture — Structure

If you use the brain workspace for AI session capture, the folder structure is
configurable per project type. See
[chat-capture.instructions.md](../instructions/chat-capture.instructions.md) for the
full capture protocol.

| Item | Value |
|---|---|
| **Config location** | `.github/instructions/chat-capture.instructions.md` |
| **Purpose** | Rules for when and how AI sessions are captured to brain |
| **Key settings** | Domain (work/personal), categories, escalation thresholds, folder structure, hierarchical grouping |
| **Customizable** | Yes — edit the instruction file to add categories, change thresholds, or define domain-specific hierarchies |
| **Path override** | `SESSION_CAPTURE_PATH` env var — relative to brain root or absolute |
| **Default path** | `<brain-root>/sessions/` |
| **Logs** | `SESSION-LOG.md` (session index), `CAPTURE-LOG.md` (structural operations) |

**Escalation patterns** (auto-organize sessions into sub-folders):

| Pattern | Trigger | Levels | Default Threshold |
|---|---|---|---|
| **Pattern 1** — Subject grouping | Same subject prefix | 1 level | 5+ files |
| **Pattern 2** — Project grouping | Same project in software-dev | 1 level | 3+ files |
| **Pattern 3a** — Code analysis | Same class, then same method | 2 levels (class → method) | 3+ files / 2+ files |
| **Pattern 3b** — Design / approach | Same component, then same aspect | 2 levels (component → aspect) | 3+ files / 2+ files |
| **Pattern 3c** — Debugging | Same service, then same issue | 2 levels (service → issue) | 3+ files / 2+ files |

**Common customizations:**

- **Add categories** — add rows to the work or personal category tables
- **Change escalation thresholds** — adjust the file count that triggers subfolder creation
- **Add domain-specific escalation** — define class/method or component/feature hierarchies
  (Pattern 3d extensibility template in chat-capture instructions)
- **Change structure profiles** — swap work or personal SE profiles to match your layout
- **Change naming conventions** — modify the file naming protocol

---

## 3. Optional — Atlassian Integration

### 3a. Atlassian v1 (API Token)

| Item | Value |
|---|---|
| **Config dir** | `mcp-servers/user-config/servers/atlassian/` |
| **Committed config** | `atlassian-config.properties` (shared defaults) |
| **Template** | `atlassian-config.local.example.properties` |
| **Secret file** | `atlassian-config.local.properties` (gitignored) |
| **Env var (auto)** | `ATLASSIAN_CONFIG_DIR` — set automatically by `.vscode/mcp.json` |

**Setup:**

```powershell
cd mcp-servers\user-config\servers\atlassian
Copy-Item atlassian-config.local.example.properties atlassian-config.local.properties
```

**Required fields in `atlassian-config.local.properties`:**

```properties
atlassian.jira.baseUrl=https://your-company.atlassian.net
atlassian.confluence.baseUrl=https://your-company.atlassian.net/wiki
atlassian.auth.email=your.name@company.com
atlassian.auth.token=ATATT3xFfGF0...
```

**Get your token:** [id.atlassian.com/manage-profile/security/api-tokens](https://id.atlassian.com/manage-profile/security/api-tokens)

### 3b. Atlassian v2 (OAuth 2.0 / API Token)

| Item | Value |
|---|---|
| **Config dir** | `mcp-servers/user-config/servers/atlassian-v2/` |
| **Committed config** | `atlassian-v2-config.properties` (shared defaults) |
| **Template** | `atlassian-v2-config.local.example.properties` |
| **Secret file** | `atlassian-v2-config.local.properties` (gitignored) |
| **Env var (auto)** | `ATLASSIAN_V2_CONFIG_DIR` — set automatically by `.vscode/mcp.json` |

**Auth options:**

| Method | Best for | Setup complexity |
|---|---|---|
| **API token** | Quick setup, personal use | Low — same as v1 |
| **OAuth 2.0** | Team use, scoped permissions | Medium — register an app first |
| **PAT (Data Center)** | Self-managed Atlassian instances | Low |

**Setup (API token — simplest):**

```powershell
cd mcp-servers\user-config\servers\atlassian-v2
Copy-Item atlassian-v2-config.local.example.properties atlassian-v2-config.local.properties
```

**Required fields in `atlassian-v2-config.local.properties`:**

```properties
atlassian.variant=cloud
atlassian.jira.baseUrl=https://your-company.atlassian.net
atlassian.confluence.baseUrl=https://your-company.atlassian.net/wiki
atlassian.auth.type=token
atlassian.auth.email=your.name@company.com
atlassian.auth.token=ATATT3xFfGF0...
```

### 3c. Atlassian Skill CLI (PAT — Server/Data Center)

The Atlassian Tools **skill** (`.github/skills/devops-tooling/atlassian-tools/`) provides a separate,
Node.js-based CLI for Atlassian Server/Data Center instances. This is independent of the
MCP-server-based integration (3a/3b above) and uses a different credential store.

| Item | Value |
|---|---|
| **Skill dir** | `.github/skills/devops-tooling/atlassian-tools/` |
| **CLI** | `scripts/atlassian_cli.js` (89 actions, zero npm deps) |
| **Runtime** | Node.js 18+ (built-in `fetch`) |
| **Template** | `.env.example` (committed) |
| **Multi-account template** | `.env.accounts.example` (committed) |
| **Secret file(s)** | `.env`, `.env.<account-name>` (gitignored) |
| **Scratch dir** | `temp-atlassian-tools/` at workspace root (gitignored) |

**Setup:**

```powershell
cd .github\skills\devops-tooling\atlassian-tools
Copy-Item .env.example .env
# Edit .env — paste your PAT tokens and base URLs
```

**Required fields in `.env`:**

```properties
JIRA_PAT_TOKEN=your-jira-pat-token
JIRA_BASE_URL=https://your-jira.example.com
CONFLUENCE_PAT_TOKEN=your-confluence-pat-token
CONFLUENCE_BASE_URL=https://your-confluence.example.com
BITBUCKET_PAT_TOKEN=your-bitbucket-pat-token
BITBUCKET_BASE_URL=https://your-bitbucket.example.com
```

**Verify connectivity:**

```powershell
$env:CLI_JSON_ARGS = '{}'; node ".github/skills/devops-tooling/atlassian-tools/scripts/atlassian_cli.js" get_current_jira_user
```

**Token generation:** See `GUIDE.md` in the skill directory for step-by-step instructions
for each service (Jira, Confluence, Bitbucket).

**Multi-account support:** For cross-instance workflows (e.g., copying Confluence pages
between two servers), create additional `.env.<account>` files using
`.env.accounts.example` as a template. See GUIDE.md § Multi-Account Setup for details.

**CLI `.env` load order (later overrides earlier):**

| Priority | Location | Purpose |
|---|---|---|
| 1 (lowest) | `<workspace>/.env` | Workspace root — primary credentials |
| 2 | `<skill>/.env` | Skill-local — overrides workspace root |
| 3 (highest) | `$env:ENV_FILE` | Explicit path — per-call override |

> **Note:** Process environment variables (`$env:JIRA_PAT_TOKEN`, etc.) always override
> `.env` file values. This enables Strategy 1 (env-var swap) for multi-account work.

---

## 4. Optional — GitHub MCP Server

| Item | Value |
|---|---|
| **Credential** | GitHub Personal Access Token (PAT) |
| **Storage** | VS Code input prompt — never stored in a file |
| **Enable** | Set `"disabled": false` for `github` in `.vscode/mcp.json` |
| **Scope** | `public_repo` (public only) or `repo` (all your repos) |

**Get your token:** [github.com/settings/tokens](https://github.com/settings/tokens)
→ Fine-grained token (recommended).

VS Code prompts for the token at runtime and caches it securely. You do not need
to create any config files.

---

## 5. Optional — MCP Advanced Config

### Global MCP Overrides

| Item | Value |
|---|---|
| **File** | `mcp-servers/user-config/mcp-config.local.properties` |
| **Template** | `mcp-servers/user-config/mcp-config.local.example.properties` |
| **Purpose** | Override MCP-wide settings: API keys, browser path, timezone, log level |
| **Sensitive** | **Yes** — may contain API keys |
| **Tracked** | No — gitignored |

**Common overrides:**

```properties
# GitHub PAT (alternative to VS Code input prompt)
apiKeys.github=ghp_your_token_here

# Browser path (for browser-based MCP tools)
browser.executable=C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe

# Timezone / location
location.timezone=America/New_York

# Log verbosity
preferences.logLevel=DEBUG
```

### Environment Variable Override Pattern

Any MCP config key can be overridden via an environment variable using the `MCP_` prefix:

| Config key | Environment variable |
|---|---|
| `apiKeys.github` | `MCP_APIKEYS_GITHUB` |
| `browser.executable` | `MCP_BROWSER_EXECUTABLE` |
| `location.timezone` | `MCP_LOCATION_TIMEZONE` |
| `preferences.logLevel` | `MCP_PREFERENCES_LOGLEVEL` |

**Rule:** Replace dots with underscores, uppercase everything, prefix with `MCP_`.

---

## 6. Config Precedence Rules

### MCP Global Config

```text
ENV vars (MCP_*)  >  mcp-config.local.properties  >  mcp-config.properties
    (highest)              (layer 2)                     (lowest — defaults)
```

### Atlassian Config (v1 and v2)

```text
ENV vars (ATLASSIAN_*)  >  *-config.local.properties  >  *-config.properties
      (highest)                 (layer 2)                   (lowest — defaults)
```

### Brain Path

```text
BRAIN_PATH env var  >  auto-detect from script location  >  default (brain/ai-brain)
    (highest)                   (automatic)                      (fallback)
```

### Session Capture Path

```text
SESSION_CAPTURE_PATH env var  >  default (<brain-root>/sessions)
       (highest)                        (fallback)
```

### Java Build

```text
build.env.local  >  JAVA_HOME env var  >  java on system PATH  >  common JDK locations
   (highest)           (layer 2)             (layer 1)               (fallback search)
```

---

## 7. Environment Variables — Complete Reference

| Variable | Purpose | Required? | Sensitive? | Default | Set Where |
|---|---|---|---|---|---|
| `JAVA_HOME` | JDK path for MCP build | Only if `java` not on PATH | No | System search | `build.env.local` or system env |
| `BRAIN_PATH` | Brain workspace path (relative to repo root) | Only if not at default | No | `brain/ai-brain` | System env or shell profile |
| `SESSION_CAPTURE_PATH` | Session capture directory (relative to brain root, or absolute) | Only if not at default | No | `sessions` | System env or shell profile |
| `ATLASSIAN_CONFIG_DIR` | Atlassian v1 config directory | Auto-set | No | Set by `.vscode/mcp.json` | Don't set manually |
| `ATLASSIAN_V2_CONFIG_DIR` | Atlassian v2 config directory | Auto-set | No | Set by `.vscode/mcp.json` | Don't set manually |
| `GITHUB_TOKEN` | GitHub PAT | Only if using GitHub MCP | **Yes** | VS Code prompts at runtime | VS Code input prompt |
| `MCP_*` | Any MCP config override | Never required | Varies | None | System env or shell profile |
| `CLI_JSON_ARGS` | Atlassian CLI tool arguments | Auto-set by Copilot | No | Set at runtime | Don't set manually |
| `JIRA_PAT_TOKEN` | Jira PAT for skill CLI | Only if using skill CLI | **Yes** | None | `.github/skills/devops-tooling/atlassian-tools/.env` |
| `JIRA_BASE_URL` | Jira instance URL for skill CLI | Only if using skill CLI | No | None | `.github/skills/devops-tooling/atlassian-tools/.env` |
| `CONFLUENCE_PAT_TOKEN` | Confluence PAT for skill CLI | Only if using skill CLI | **Yes** | None | `.github/skills/devops-tooling/atlassian-tools/.env` |
| `CONFLUENCE_BASE_URL` | Confluence instance URL for skill CLI | Only if using skill CLI | No | None | `.github/skills/devops-tooling/atlassian-tools/.env` |
| `BITBUCKET_PAT_TOKEN` | Bitbucket PAT for skill CLI | Only if using skill CLI | **Yes** | None | `.github/skills/devops-tooling/atlassian-tools/.env` |
| `BITBUCKET_BASE_URL` | Bitbucket instance URL for skill CLI | Only if using skill CLI | No | None | `.github/skills/devops-tooling/atlassian-tools/.env` |
| `ENV_FILE` | Override `.env` file path for skill CLI | Never required | No | None | Shell session (per-call) |

---

## 8. Config Files — Complete Inventory

### Committed (safe — no secrets)

| File | Category | Purpose | Export action |
|---|---|---|---|
| `.github/copilot-instructions.md` | copilot | Project-wide Copilot rules | **Edit** for your project |
| `.github/instructions/*.instructions.md` | copilot | File-type-specific rules | Keep or remove per need |
| `.vscode/mcp.json` | mcp | MCP server registry | Enable servers you want |
| `.vscode/tasks.json` | vscode | Build/run task definitions | Update paths if moved |
| `.vscode/launch.json` | vscode | Debug/run configurations | Update paths if moved |
| `mcp-servers/build.env.example` | build | Template for `build.env.local` | Copy as-is (reference) |
| `mcp-servers/user-config/mcp-config.properties` | mcp | Shared MCP defaults | Copy as-is |
| `mcp-servers/user-config/mcp-config.local.example.properties` | mcp | Template for local overrides | Copy as-is (reference) |
| `mcp-servers/user-config/servers/atlassian/atlassian-config.properties` | atlassian | Atlassian v1 shared defaults | Copy as-is |
| `mcp-servers/user-config/servers/atlassian/atlassian-config.local.example.properties` | atlassian | Atlassian v1 template | Copy as-is (reference) |
| `mcp-servers/user-config/servers/atlassian-v2/atlassian-v2-config.properties` | atlassian | Atlassian v2 shared defaults | Copy as-is |
| `mcp-servers/user-config/servers/atlassian-v2/atlassian-v2-config.local.example.properties` | atlassian | Atlassian v2 template | Copy as-is (reference) |
| `.github/skills/devops-tooling/atlassian-tools/.env.example` | skill-cli | Skill CLI PAT template (single account) | Copy as-is (reference) |
| `.github/skills/devops-tooling/atlassian-tools/.env.accounts.example` | skill-cli | Skill CLI multi-account template | Copy as-is (reference) |

### Gitignored (user-created from templates — may contain secrets)

| File | Category | Sensitive? | Template | Export action |
|---|---|---|---|---|
| `mcp-servers/build.env.local` | build | No (path only) | `build.env.example` | Recreate from template (machine-specific) |
| `mcp-servers/user-config/mcp-config.local.properties` | mcp | **Yes** | `mcp-config.local.example.properties` | Recreate from template with your keys |
| `mcp-servers/user-config/servers/atlassian/atlassian-config.local.properties` | atlassian | **Yes** | `atlassian-config.local.example.properties` | Recreate from template with your credentials |
| `mcp-servers/user-config/servers/atlassian-v2/atlassian-v2-config.local.properties` | atlassian | **Yes** | `atlassian-v2-config.local.example.properties` | Recreate from template with your credentials |
| `.github/skills/devops-tooling/atlassian-tools/.env` | skill-cli | **Yes** | `.env.example` | Recreate from template with your PAT tokens |
| `.github/skills/devops-tooling/atlassian-tools/.env.<account>` | skill-cli | **Yes** | `.env.accounts.example` | Recreate per account for multi-instance work |

---

## 9. Security Rules

- **Never commit** `.local.properties` files — they are gitignored by default
- **Never commit** API tokens in `.vscode/mcp.json` — use VS Code input variables instead
- **Never hardcode** secrets in scripts, instructions, or skill files
- **Rotate tokens** periodically — Atlassian tokens don't expire automatically; GitHub
  tokens can be set to expire
- **Minimal scope** — give tokens the minimum permissions needed (read-only where possible)
- **Template files** (`.example`) are safe to commit — they contain placeholder values only

---

## 10. Export Checklist — Migrating to Another Project

When exporting this project's features to another repo, use this checklist.
Items are ordered by importance; stop at the level you need.

### Level 1: Copilot Only (1 min)

- [ ] Copy `.github/` folder to target project
- [ ] Edit `.github/copilot-instructions.md` — update project name, language, conventions

### Level 2: Copilot + MCP Servers (5 min)

- [ ] Level 1 above
- [ ] Copy `mcp-servers/` folder to target project
- [ ] Copy `.vscode/mcp.json` and `.vscode/tasks.json` to target
- [ ] Run `.\mcp-servers\build.ps1` — if it fails, create `build.env.local` with `JAVA_HOME`
- [ ] In `.vscode/mcp.json`: set `"disabled": false` for servers you want
- [ ] Add gitignore entries (see [export-guide.md § 5](export-guide.md#5-gitignore-entries-required-in-target-project))

### Level 3: Full Export with Brain (10 min)

- [ ] Level 2 above
- [ ] Copy `brain/` folder (or just `brain/ai-brain/`) to your preferred location
- [ ] **If brain is at the default path (`brain/ai-brain`):** no extra config needed
- [ ] **If brain is at a different path:**
  - [ ] Set `BRAIN_PATH` env var to the path relative to repo root
  - [ ] Update `.github/copilot-instructions.md` § Configurable Paths
  - [ ] Find-replace `brain/ai-brain` in `.vscode/tasks.json` brain task commands
  - [ ] Update `.gitignore` inbox pattern
  - [ ] Update `applyTo` in `.github/instructions/backlog.instructions.md`
- [ ] **If brain is inside a module/package:** exclude from build tool (see
  [export-guide.md § 3d](export-guide.md#3d-brain-inside-a-module--package--monorepo-package))

### Level 4: Atlassian Integration (+ 5–10 min)

**Option A — Skill CLI (simpler, for Server/Data Center):**

- [ ] Verify Node.js 18+ is installed (`node --version`)
- [ ] Copy `.github/skills/devops-tooling/atlassian-tools/.env.example` to `.env`
- [ ] Fill in PAT tokens and base URLs for Jira, Confluence, and/or Bitbucket
- [ ] Verify: `$env:CLI_JSON_ARGS = '{}'; node scripts\atlassian_cli.js get_current_jira_user`
- [ ] For multi-account: copy `.env.accounts.example` and create `.env.<account>` files

**Option B — MCP Server (for Atlassian Cloud):**

- [ ] Create `atlassian-config.local.properties` (v1) or
  `atlassian-v2-config.local.properties` (v2) from the `.example` template
- [ ] Fill in your instance URL, email, and API token
- [ ] In `.vscode/mcp.json`: set `"disabled": false` for `atlassian` or `atlassian-v2`

### Level 5: GitHub MCP Server (+ 2 min)

- [ ] In `.vscode/mcp.json`: set `"disabled": false` for `github`
- [ ] VS Code will prompt for your PAT at runtime — no file to create

### Level 6: Advanced MCP Overrides (optional)

- [ ] Create `mcp-config.local.properties` from the `.example` template
- [ ] Add API keys, browser path, timezone, or log level overrides

---

## Config File Tree

```text
project-root/
├── .github/
│   ├── copilot-instructions.md          ← REQUIRED: project conventions
│   ├── instructions/
│   │   └── backlog.instructions.md      ← UPDATE applyTo if brain path changed
│   └── skills/
│       └── atlassian-tools/
│           ├── .env.example             ← Template: PAT tokens (committed)
│           ├── .env.accounts.example    ← Template: multi-account (committed)
│           ├── .env                     ← YOUR: PAT tokens (gitignored)
│           └── scripts/
│               └── atlassian_cli.js     ← 89-action CLI (committed)
├── .vscode/
│   ├── mcp.json                         ← MCP server registry (enable/disable)
│   ├── tasks.json                       ← Build/run tasks (update brain paths)
│   └── launch.json                      ← Debug configs (optional)
├── .gitignore                           ← UPDATE inbox pattern if brain path changed
├── mcp-servers/
│   ├── build.env.example                ← Template: JAVA_HOME
│   ├── build.env.local                  ← YOUR: JAVA_HOME (gitignored)
│   └── user-config/
│       ├── mcp-config.properties        ← Shared MCP defaults (committed)
│       ├── mcp-config.local.example.properties  ← Template: API keys
│       ├── mcp-config.local.properties  ← YOUR: API keys (gitignored)
│       └── servers/
│           ├── atlassian/
│           │   ├── atlassian-config.properties              ← Shared defaults
│           │   ├── atlassian-config.local.example.properties ← Template
│           │   └── atlassian-config.local.properties         ← YOUR credentials (gitignored)
│           └── atlassian-v2/
│               ├── atlassian-v2-config.properties            ← Shared defaults
│               ├── atlassian-v2-config.local.example.properties ← Template
│               └── atlassian-v2-config.local.properties       ← YOUR credentials (gitignored)
└── brain/ai-brain/                      ← BRAIN_PATH default (configurable)
    ├── inbox/                           ← Gitignored scratchpad
    ├── notes/                           ← Tracked notes
    ├── library/                         ← Tracked references
    ├── sessions/                        ← Tracked AI session captures
    ├── backlog/                         ← Tracked kanban board
    ├── pkm/                             ← Tracked PKM infra
    └── scripts/                         ← Brain management scripts
```

---

## Further Reading

| Topic | Doc |
|---|---|
| Full export walkthrough | [Export Guide](export-guide.md) |
| Simplified 10-minute export | [Export Newbie Guide](export-newbie-guide.md) |
| Brain path configuration details | [copilot-instructions.md § Configurable Paths](../copilot-instructions.md) |
| Brain inside a module/package | [Export Guide § 3d](export-guide.md#3d-brain-inside-a-module--package--monorepo-package) |
| MCP server setup | [MCP Servers SETUP](../../mcp-servers/SETUP.md) |
| Getting started | [START-HERE](START-HERE.md) |
