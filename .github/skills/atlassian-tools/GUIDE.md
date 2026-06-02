# Atlassian Tools — Developer Guide

> **Audience:** Anyone setting up, using, or extending the Atlassian Tools skill.
> **Prerequisite:** Node.js 18+ installed. Access to at least one Atlassian Server/Data Center instance.

---

## What This Skill Does

The Atlassian Tools skill gives GitHub Copilot (or any AI agent) the ability to interact with
**Jira, Confluence, and Bitbucket Server/Data Center** through a single CLI. It is the universal
handler for all Atlassian-related tasks in this workspace.

**Key facts:**

- **89 CLI actions** across Jira (34), Confluence (34), and Bitbucket (21)
- **Zero npm dependencies** — uses only Node.js 18+ built-in `fetch`
- **PAT authentication** — Personal Access Tokens via Bearer header
- **Single file** — `scripts/atlassian_cli.js` (ESM module, ~1,400 lines)
- **JSON in, JSON out** — all actions accept JSON args and return `{ success, data/error }`

---

## Architecture

```text
User request
  ↓
Copilot reads SKILL.md + relevant reference file
  ↓
Copilot calls run_in_terminal:
  $env:CLI_JSON_ARGS = '{"issueKey":"PROJ-123"}'
  node .github/skills/atlassian-tools/scripts/atlassian_cli.js fetch_jira_issue
  ↓
CLI loads .env → reads PAT tokens → calls REST API → returns JSON
  ↓
Copilot parses JSON response → presents results to user
```

### File Layout

```text
.github/skills/atlassian-tools/
├── SKILL.md                          ← Main skill file (Copilot reads this first)
├── GUIDE.md                          ← This file — human-readable setup guide
├── package.json                      ← Node.js module metadata (type: module)
├── .env.example                      ← Template for single-account setup (committed)
├── .env.accounts.example             ← Template for multi-account setup (committed)
├── .env                              ← Your actual tokens (gitignored — never committed)
├── scripts/
│   └── atlassian_cli.js              ← The CLI (89 actions, ~1,400 lines)
└── references/
    ├── action-catalog.md             ← All 89 actions with args and response shapes
    ├── confluence-formatting.md      ← Confluence HTML storage format, macros, Mermaid
    ├── usage-recipes.md              ← Examples, bulk patterns, quirks, troubleshooting
    ├── tone-and-disclaimer.md        ← Enterprise tone, AI disclaimer templates
    ├── jql-cql-cheatsheet.md         ← 30+ JQL and 15+ CQL query templates
    └── workflow-playbooks.md         ← 13 end-to-end multi-step playbooks
```

---

## Setup — Single Account (Quick Start)

### Step 1 — Generate Personal Access Tokens

Generate a PAT for each Atlassian service you need. You only need tokens for services you use.

| Service | Where to generate | Required permission |
|---|---|---|
| Jira | `Profile → Personal Access Tokens → Create` | Project read/write |
| Confluence | `Profile → Personal Access Tokens → Create` | Space read/write |
| Bitbucket | `Profile → Manage Account → Personal Access Tokens` | Repository read (+ write for comments/tasks) |

> **Tip:** On Atlassian Server/Data Center, PATs are generated from your profile page.
> The exact URL depends on your instance (e.g., `https://your-jira.example.com/secure/ViewProfile.jspa`).

### Step 2 — Create the `.env` file

Copy the template and fill in your tokens:

```powershell
cd .github/skills/atlassian-tools
Copy-Item .env.example .env
# Edit .env — replace placeholder values with your actual PAT tokens and base URLs
```

Alternatively, create the `.env` file at the **workspace root** (same level as `README.md`)
or in the skill directory (`.github/skills/atlassian-tools/`). The CLI checks both locations.

```properties
# Jira
JIRA_PAT_TOKEN=YOUR_JIRA_PAT_HERE
JIRA_BASE_URL=https://your-jira.example.com

# Confluence
CONFLUENCE_PAT_TOKEN=YOUR_CONFLUENCE_PAT_HERE
CONFLUENCE_BASE_URL=https://your-confluence.example.com

# Bitbucket
BITBUCKET_PAT_TOKEN=YOUR_BITBUCKET_PAT_HERE
BITBUCKET_BASE_URL=https://your-bitbucket.example.com
```

**Rules for the `.env` file:**

- **No quotes** around values — store raw token strings
- PATs often contain `+`, `/`, `=` characters — this is normal (Base64 encoding)
- Lines starting with `#` are comments
- One `KEY=VALUE` per line, no spaces around the `=`
- The file is **gitignored** (`.env` and `.env.*` patterns in `.gitignore`)

### Step 3 — Verify connectivity

```powershell
$env:CLI_JSON_ARGS = '{}'; node ".github/skills/atlassian-tools/scripts/atlassian_cli.js" get_current_jira_user
```

Expected response:

```json
{ "success": true, "data": { "name": "your.username", "displayName": "Your Name", ... } }
```

If you see `success: false`, check the Troubleshooting section below.

### Step 4 — Try a basic operation

```powershell
# Fetch a Jira issue
$env:CLI_JSON_ARGS = '{"issueKey":"PROJ-123"}'; node ".github/skills/atlassian-tools/scripts/atlassian_cli.js" fetch_jira_issue

# Fetch a Confluence page (by page ID)
$env:CLI_JSON_ARGS = '{"pageId":"12345"}'; node ".github/skills/atlassian-tools/scripts/atlassian_cli.js" fetch_confluence_page

# Search Bitbucket PRs
$env:CLI_JSON_ARGS = '{"project":"PROJ","repo":"my-repo","state":"OPEN","maxResults":5}'; node ".github/skills/atlassian-tools/scripts/atlassian_cli.js" search_bitbucket_prs
```

---

## Setup — Multiple Accounts (Cross-Instance Operations)

For workflows that span two or more Atlassian instances (e.g., copying Confluence pages from one
server to another, comparing Jira data across instances, or using different credentials for
different services), you need credentials for each account.

### Quick Start — Multi-Account

```powershell
# 1. Copy the multi-account template
Copy-Item ".github/skills/atlassian-tools/.env.accounts.example" ".github/skills/atlassian-tools/.env.siemens-eda"

# 2. Edit the new file — paste only the variables that differ
# 3. Verify with:
$env:ENV_FILE = "$PWD/.github/skills/atlassian-tools/.env.siemens-eda"
$env:CLI_JSON_ARGS = '{}'; node ".github/skills/atlassian-tools/scripts/atlassian_cli.js" get_current_jira_user
Remove-Item Env:\ENV_FILE
```

### How the CLI loads credentials

The CLI reads `.env` files in this order (later files override earlier):

```text
1. <workspace>/.env                    ← workspace root (primary)
2. <skill>/.env                        ← .github/skills/atlassian-tools/.env (override)
3. $env:ENV_FILE                       ← explicit path (highest priority)
```

Additionally, **process environment variables** override `.env` file values. Any
`JIRA_PAT_TOKEN`, `CONFLUENCE_BASE_URL`, etc. set in the current PowerShell session
take precedence over everything.

### Account file naming conventions

Use descriptive kebab-case names that identify either the **instance** or the **purpose**:

| Pattern | When to use | Example |
|---|---|---|
| `.env.<org-name>` | Whole organisation instance | `.env.siemens-eda`, `.env.acme-corp` |
| `.env.<purpose>-<service>` | One service at a different instance | `.env.work-jira`, `.env.client-confluence` |
| `.env.<environment>` | Different environments (prod/staging) | `.env.staging`, `.env.prod` |
| `.env.account-b` | Generic second account | `.env.account-b` |

**File location:** Store `.env.*` files alongside your default `.env`:

```text
.github/skills/atlassian-tools/
├── .env                       ← default account (your primary instance)
├── .env.siemens-eda           ← second instance (different PATs/URLs)
├── .env.client-confluence     ← third instance (only Confluence vars)
├── .env.example               ← committed template (for new users)
├── .env.accounts.example      ← committed multi-account template
└── ...
```

All `.env` and `.env.*` files are gitignored (except `.env.example`, `.env.template`,
and `.env.accounts.example`).

### Per-service mixing — different accounts for different services

A common scenario: your Jira is on Instance A, but Confluence is on Instance B (or you
have a personal Confluence on a different server). You can mix credentials from different
instances in a single `.env` file:

```properties
# .env — mix: work Jira + personal Confluence + work Bitbucket
JIRA_PAT_TOKEN=work-jira-token
JIRA_BASE_URL=https://work-jira.example.com

CONFLUENCE_PAT_TOKEN=personal-confluence-token
CONFLUENCE_BASE_URL=https://personal-confluence.example.com

BITBUCKET_PAT_TOKEN=work-bitbucket-token
BITBUCKET_BASE_URL=https://work-bitbucket.example.com
```

Or create per-service override files that contain **only the variables for that service**:

```properties
# .env.personal-confluence — overrides only Confluence credentials
CONFLUENCE_PAT_TOKEN=personal-confluence-token
CONFLUENCE_BASE_URL=https://personal-confluence.example.com
```

Load the override to temporarily switch one service while keeping others at defaults:

```powershell
Load-EnvFile ".github/skills/atlassian-tools/.env.personal-confluence"
# Confluence now points to personal instance; Jira/Bitbucket unchanged
```

### Strategy 1 — Environment variable swap (recommended for one-off operations)

Swap environment variables between CLI calls in the same PowerShell session.

```powershell
# --- Save Account A credentials (already loaded from .env) ---
$acctA_token = $env:CONFLUENCE_PAT_TOKEN
$acctA_url = $env:CONFLUENCE_BASE_URL

# --- Fetch from Account A ---
$env:CLI_JSON_ARGS = '{"pageId":"12345"}'
node ".github/skills/atlassian-tools/scripts/atlassian_cli.js" fetch_confluence_page

# --- Switch to Account B ---
$env:CONFLUENCE_PAT_TOKEN = "ACCOUNT_B_PAT_TOKEN_HERE"
$env:CONFLUENCE_BASE_URL = "https://account-b-confluence.example.com"

# --- Create in Account B ---
$env:CLI_JSON_ARGS = '{"title":"Migrated Page","spaceKey":"TARGET","parentPageId":"67890","contentFile":"temp-atlassian-tools/page.html"}'
node ".github/skills/atlassian-tools/scripts/atlassian_cli.js" create_confluence_page

# --- Restore Account A ---
$env:CONFLUENCE_PAT_TOKEN = $acctA_token
$env:CONFLUENCE_BASE_URL = $acctA_url
```

**Best for:** Quick, manual one-off operations. The agent can do this automatically.

### Strategy 2 — Multiple `.env` files with `Load-EnvFile` (recommended for workflows)

Create separate `.env` files per account and load them with a helper function.

**Helper function (paste into your PowerShell session or profile):**

```powershell
function Load-EnvFile($path) {
    Get-Content $path | ForEach-Object {
        if ($_ -match '^\s*([^#][^=]+)=(.*)$') {
            [System.Environment]::SetEnvironmentVariable($Matches[1].Trim(), $Matches[2].Trim(), "Process")
        }
    }
}

# Usage
Load-EnvFile ".github/skills/atlassian-tools/.env"              # Load default account
# ... do operations on default instance ...

Load-EnvFile ".github/skills/atlassian-tools/.env.siemens-eda"   # Switch to second instance
# ... do operations on second instance ...

Load-EnvFile ".github/skills/atlassian-tools/.env"               # Restore default
```

**Best for:** Multi-step workflows (migrations, bulk copy, cross-instance reporting).

### Strategy 3 — `ENV_FILE` override (recommended for single CLI calls)

Point the CLI to a specific `.env` file using the `ENV_FILE` environment variable:

```powershell
# Use a specific env file for this call only
$env:ENV_FILE = "$PWD/.github/skills/atlassian-tools/.env.siemens-eda"
$env:CLI_JSON_ARGS = '{"pageId":"99999"}'
node ".github/skills/atlassian-tools/scripts/atlassian_cli.js" fetch_confluence_page

# Remove override (reverts to default .env loading)
Remove-Item Env:\ENV_FILE
```

**Best for:** One-off calls to a non-default instance without touching other env vars.

> `ENV_FILE` has the highest priority — it overrides both `<workspace>/.env` and `<skill>/.env`.

### Strategy decision guide

| Scenario | Recommended strategy |
|---|---|
| Quick one-off read from another instance | **Strategy 3** — `ENV_FILE` override |
| Copy a single page between instances | **Strategy 1** — env-var swap |
| Migrate 10+ pages between instances | **Strategy 2** — `Load-EnvFile` |
| Daily workflow mixing 2 instances | **Strategy 2** — `Load-EnvFile` |
| CI/CD pipeline with multiple instances | **Strategy 2** or **Strategy 3** |
| Agent (Copilot) doing cross-instance work | **Strategy 1** — agent manages env vars automatically |

### Account validation — always verify before writing

Before performing any **write** operation on a non-default instance, verify you are targeting
the correct account:

```powershell
# Verify Jira identity
$env:CLI_JSON_ARGS = '{}'; node ".github/skills/atlassian-tools/scripts/atlassian_cli.js" get_current_jira_user

# Verify Confluence identity
$env:CLI_JSON_ARGS = '{}'; node ".github/skills/atlassian-tools/scripts/atlassian_cli.js" get_current_confluence_user
```

Check the `displayName` and `name` fields in the response. If they don't match your expected
account, you have the wrong credentials loaded.

**Validation helper function:**

```powershell
function Verify-AtlassianAccount {
    Write-Host "--- Jira ---" -ForegroundColor Cyan
    $env:CLI_JSON_ARGS = '{}'
    $jira = (node ".github/skills/atlassian-tools/scripts/atlassian_cli.js" get_current_jira_user 2>$null) | ConvertFrom-Json
    if ($jira.success) {
        Write-Host "  User: $($jira.data.displayName) ($($jira.data.name))" -ForegroundColor Green
        Write-Host "  URL:  $env:JIRA_BASE_URL" -ForegroundColor Gray
    } else {
        Write-Host "  FAILED: $($jira.error)" -ForegroundColor Red
    }

    Write-Host "--- Confluence ---" -ForegroundColor Cyan
    $conf = (node ".github/skills/atlassian-tools/scripts/atlassian_cli.js" get_current_confluence_user 2>$null) | ConvertFrom-Json
    if ($conf.success) {
        Write-Host "  User: $($conf.data.displayName) ($($conf.data.username))" -ForegroundColor Green
        Write-Host "  URL:  $env:CONFLUENCE_BASE_URL" -ForegroundColor Gray
    } else {
        Write-Host "  FAILED: $($conf.error)" -ForegroundColor Red
    }
}

# Run after loading any .env file:
Load-EnvFile ".github/skills/atlassian-tools/.env.siemens-eda"
Verify-AtlassianAccount
```

### Security rules for multi-account

| Rule | Why |
|---|---|
| Never echo or log token values | Tokens are credentials — treat like passwords |
| Always restore original credentials | Prevents accidental writes to the wrong instance |
| Always verify before write operations | `get_current_*_user` confirms the active account |
| Store `.env` files alongside the skill or at workspace root | They are gitignored by `.env` and `.env.*` patterns |
| Use descriptive filenames | `.env.siemens-eda`, `.env.client-confluence` — not `.env.2` |
| Include only variables that differ | Minimises credential surface in each file |
| Don't commit `.env` files | Already handled by `.gitignore` |
| Rotate tokens periodically | PATs don't auto-expire on most Server instances |

---

## How the CLI Works (Internals)

### Invocation pattern

Every CLI call follows this pattern:

```powershell
$env:CLI_JSON_ARGS = '<json-string>'; node "<path>/atlassian_cli.js" <action_name>
```

| Part | Purpose |
|---|---|
| `CLI_JSON_ARGS` | JSON object with action-specific arguments |
| `<path>/atlassian_cli.js` | The CLI script |
| `<action_name>` | One of 89 supported actions (e.g., `fetch_jira_issue`) |

### Response format

All actions return JSON to stdout:

```json
{ "success": true, "data": { ... } }
```

or

```json
{ "success": false, "error": "Human-readable error message" }
```

**Always check `success` before using `data`.**

### Content delivery patterns

For actions that accept HTML content (Confluence page create/update):

| Pattern | When to use | How |
|---|---|---|
| `contentFromEnv` | Short HTML (< 1KB, ASCII only) | Set `$env:CLI_CONTENT = '<html>'`, pass `"contentFromEnv": true` |
| `contentFile` | Long HTML, non-ASCII, macros | Write HTML to file, pass `"contentFile": "path/to/file.html"` |

**Always prefer `contentFile`** for anything with non-ASCII characters (`—`, `→`, `'`, etc.)
to avoid PowerShell encoding corruption.

### TLS handling

The CLI sets `NODE_TLS_REJECT_UNAUTHORIZED=0` by default for corporate environments with
internal CAs. To use proper TLS verification, set `NODE_EXTRA_CA_CERTS` to your CA bundle:

```properties
# In .env
NODE_EXTRA_CA_CERTS=/path/to/corporate-ca-bundle.pem
```

---

## Workspace Scratch Directory

The CLI uses `<workspace>/temp-atlassian-tools/` for intermediate files (HTML content,
exported data, migration artifacts).

- Created on demand — does not need to exist beforehand
- Prefer stable filenames (`cli_content.html`, `migrated_page.html`) over timestamped ones
- Overwrite reusable files instead of creating duplicates
- Clean up sensitive content after use
- **Add to `.gitignore`** if you create it frequently (currently covered by `*.tmp` for files, but
  the directory itself should be explicitly ignored — see Configuration Checklist below)

---

## Configuration Checklist

Before first use, verify these items:

| Item | Status | Notes |
|---|---|---|
| Node.js 18+ installed | `node --version` | Built-in `fetch` required |
| `.env` file created (from `.env.example`) | See Setup above | Must contain PAT + base URL for each service |
| `.env` is gitignored | Check `.gitignore` has `.env` pattern | Already configured in this repo |
| Test connectivity | `get_current_jira_user` | Verifies token + URL + network |
| VPN connected (if corporate) | Required for internal Atlassian servers | Most corporate instances require VPN |
| `temp-atlassian-tools/` in `.gitignore` | Already configured in this repo | Prevents accidental commits of scratch files |

---

## Action Reference (Summary)

Full details in `references/action-catalog.md`. Here is the overview:

### Jira (34 actions)

| Category | Actions | Key operations |
|---|---|---|
| Core | 10 | CRUD issues, search (JQL), comments, worklogs, delete |
| Links & Relations | 4 | Link issues, get links, subtasks, clone |
| Bulk Operations | 2 | Bulk create, bulk transition |
| Labels | 2 | Add/remove labels |
| Watchers | 3 | Get/add/remove watchers |
| Metadata | 7 | Issue types, statuses, components, versions, users, changelog |
| Agile / Sprint | 6 | Sprints, sprint issues, epics, move to sprint/backlog |

### Confluence (34 actions)

| Category | Actions | Key operations |
|---|---|---|
| Core | 11 | Pages CRUD, search (text + CQL), comments, delete |
| Content Management | 12 | Copy, move, labels, properties, versions, restore, inline comments |
| Navigation | 4 | Page tree, ancestors, watch/unwatch |
| Spaces & Blogs | 7 | Space info, content listing, blogs, templates, PDF export, user |

### Bitbucket (21 actions)

| Category | Actions | Key operations |
|---|---|---|
| Core | 7 | PR details, files, diff, activities, search, file fetch, contributions |
| PR Comments | 5 | Add, reply, update, delete (general + inline) |
| Tasks | 6 | Create, list, update, delete, resolve, reopen |
| File Operations | 3 | File diff, check file in PR, get PR file content |

---

## Playbook Reference (Summary)

Full details in `references/workflow-playbooks.md`. 13 playbooks available:

| # | Playbook | Services | Use case |
|---|---|---|---|
| 1 | Sprint Planning | Jira | Find backlog items, move to sprint, label |
| 2 | Sprint Review Docs | Jira → Confluence | Generate review/retro page from sprint data |
| 3 | Release Management | Jira + Confluence | Release notes, readiness check, bulk transition |
| 4 | Incident Post-Mortem | Jira + Confluence | Timeline, RCA, action items, worklog |
| 5 | Code Review Docs | Bitbucket → Confluence | PR diff + comments → review summary page |
| 6 | Status Reporting | Jira → Confluence | Weekly metrics, progress tables |
| 7 | Onboarding Docs | Jira + Confluence | Project structure, workflows, key pages |
| 8 | Bulk Triage | Jira | Assign, label, comment on untriaged backlog |
| 9 | Cross-Account Migration | Confluence × 2 | Copy pages between instances (4 scenarios) |
| 10 | Resume / Work Analysis | Jira + Bitbucket | Build resume bullets from work history |
| 11 | Code Migration | Bitbucket ↔ GitHub | Mirror repos, migrate PRs, export history |
| 12 | Advanced Content Ops | Confluence | Clone trees, bulk label updates, version rollback |
| 13 | Deep Analysis | Jira | Sprint velocity, component health, dependency mapping |

---

## Common Workflows — Quick Examples

### Search Jira issues

```powershell
$env:CLI_JSON_ARGS = '{"jql":"assignee = currentUser() AND status != Done ORDER BY updated DESC","maxResults":20}'
node ".github/skills/atlassian-tools/scripts/atlassian_cli.js" search_jira_issues
```

### Create a Confluence page

```powershell
# Write HTML content to file (avoids encoding issues)
@"
<h2>My Page</h2>
<p>Content goes here.</p>
"@ | Out-File -Encoding utf8 "temp-atlassian-tools/cli_content.html"

$env:CLI_JSON_ARGS = '{"title":"My New Page","spaceKey":"ENG","parentPageId":"602112114","contentFile":"temp-atlassian-tools/cli_content.html"}'
node ".github/skills/atlassian-tools/scripts/atlassian_cli.js" create_confluence_page
```

### Copy a Confluence page (same instance)

```powershell
$env:CLI_JSON_ARGS = '{"pageId":"12345","newTitle":"Copy of Original","targetSpaceKey":"NEW","targetParentId":"67890"}'
node ".github/skills/atlassian-tools/scripts/atlassian_cli.js" copy_confluence_page
```

### Copy a Confluence page (cross-instance)

```powershell
# 1. Fetch from Account A (default .env)
$env:CLI_JSON_ARGS = '{"pageId":"12345"}'
$src = (node ".github/skills/atlassian-tools/scripts/atlassian_cli.js" fetch_confluence_page) | ConvertFrom-Json
$src.data.body.storage.value | Out-File -Encoding utf8 "temp-atlassian-tools/migrated.html"

# 2. Switch to Account B
Load-EnvFile ".env.account-b"

# 3. Create in Account B
$env:CLI_JSON_ARGS = '{"title":"Migrated Page","spaceKey":"TARGET","parentPageId":"67890","contentFile":"temp-atlassian-tools/migrated.html"}'
node ".github/skills/atlassian-tools/scripts/atlassian_cli.js" create_confluence_page

# 4. Restore Account A
Load-EnvFile ".env"
```

### Mirror a Bitbucket repo to GitHub

```powershell
git clone --bare "https://your-bitbucket.example.com/scm/PROJ/repo.git" repo.git
gh repo create your-org/repo --private --description "Migrated from Bitbucket"
cd repo.git
git push --mirror "https://github.com/your-org/repo.git"
```

### Build resume from work history

```powershell
# Get completed issues (last 6 months)
$env:CLI_JSON_ARGS = '{"jql":"assignee = currentUser() AND status CHANGED TO Done DURING (-180d, now())","maxResults":100}'
node ".github/skills/atlassian-tools/scripts/atlassian_cli.js" search_jira_issues

# Get PR contributions
$env:CLI_JSON_ARGS = '{"project":"IESD","repo":"iesd-26","person":"your.username","months":6}'
node ".github/skills/atlassian-tools/scripts/atlassian_cli.js" summarize_bitbucket_contributions
```

---

## Troubleshooting

### Authentication errors

| Error | Cause | Fix |
|---|---|---|
| `Missing .env file` | No `.env` at workspace root | Create `.env` with tokens and URLs |
| `Missing *_PAT_TOKEN` | Token line missing from `.env` | Add the missing token variable |
| `401 Unauthorized` | Invalid or expired token | Regenerate PAT and update `.env` |
| Token with quotes fails | Token wrapped in `"` or `'` | Remove quotes — store raw value |
| `ECONNREFUSED` | Network / VPN issue | Check VPN and verify base URLs |
| TLS certificate error | Corporate CA not trusted | Set `NODE_EXTRA_CA_CERTS` in `.env` |

### PowerShell issues

| Error | Cause | Fix |
|---|---|---|
| JSON parse error | PowerShell mangled quotes | Always use **single quotes** around JSON |
| Mojibake (`ΓÇö` instead of `—`) | Encoding corruption | Use `contentFile` instead of `contentFromEnv` |
| `node` not found | Node.js not in PATH | Install Node.js 18+ or add to PATH |

### Confluence issues

| Error | Cause | Fix |
|---|---|---|
| `404 Not Found` | Wrong page ID | Verify via search or URL (`pageId=XXXXX` in URL) |
| `409 Conflict` on update | Concurrent edit | Re-fetch page (gets latest version), then retry |
| PDF export no download | Needs browser auth | Open the returned URL in authenticated browser |

### Jira issues

| Error | Cause | Fix |
|---|---|---|
| Transition not found | Name mismatch | Fetch issue first; use exact transition name shown |
| Empty search results | Restrictive JQL | Try broader query or alternate status names |
| `maxResults` truncation | Default is 25 | Set `maxResults` explicitly |

### Cross-instance issues

| Error | Cause | Fix |
|---|---|---|
| Wrong instance targeted | Env vars not swapped | Verify with `get_current_confluence_user` before writing |
| Macros not rendering | Target lacks plugins | Remove or replace instance-specific macros |
| Internal links broken | Source page IDs in HTML | Find-and-replace page IDs/URLs after migration |

---

## Extending the CLI

### Calling REST APIs directly

If the CLI doesn't have an action for your need, call the REST API directly using the same PAT:

```powershell
# Load token from .env (or use the already-loaded env var)
$token = $env:CONFLUENCE_PAT_TOKEN
$baseUrl = $env:CONFLUENCE_BASE_URL

# Example: Get page attachments (no CLI action for this)
$headers = @{
    "Authorization" = "Bearer $token"
    "Accept" = "application/json"
}
$response = Invoke-RestMethod -Uri "$baseUrl/rest/api/content/12345/child/attachment" -Headers $headers
$response.results | Format-Table title, id, mediaType
```

### Atlassian REST API documentation

| Service | API docs |
|---|---|
| Jira Server | `<your-jira>/rest/api/2/` (self-documenting) |
| Confluence Server | `<your-confluence>/rest/api/content/` |
| Bitbucket Server | `<your-bitbucket>/rest/api/1.0/` |

---

## FAQ

**Q: Do I need all three tokens (Jira + Confluence + Bitbucket)?**
No. You only need tokens for the services you use. If you only work with Confluence, you only
need `CONFLUENCE_PAT_TOKEN` and `CONFLUENCE_BASE_URL`. The CLI will skip unavailable services.

**Q: Can I use this with Atlassian Cloud?**
The CLI is designed for Atlassian Server/Data Center (on-premise). Cloud uses different auth
(OAuth 2.0, API tokens) and different REST endpoints. The PAT Bearer auth may work for some
Cloud endpoints, but it is not tested or officially supported.

**Q: Where do I find a Confluence page ID?**
From the URL: `https://your-confluence/pages/viewpage.action?pageId=12345` → the ID is `12345`.
Or search with CQL: `search_confluence_cql` with `title = "Page Title"`.

**Q: How do I handle rate limiting?**
Atlassian Server does not typically enforce rate limits for PAT access. If you hit issues with
bulk operations, add a brief delay between calls. The CLI does not have built-in rate limiting.

**Q: Can multiple people share the same `.env` file?**
No. PATs are personal — each user must generate their own tokens. The `.env` file is gitignored
and should never be committed or shared.

**Q: What happens if my token expires?**
You'll get `401 Unauthorized` errors. Generate a new token from your Atlassian profile and
update the `.env` file. Token expiration depends on your instance's admin settings.
