---
name: jira
description: "Jira operations via PAT-authenticated REST APIs. Use whenever the user asks about Jira tickets, JQL queries, sprints, boards, worklogs, issue transitions, labels, watchers, bulk issue operations, or cross-account Jira operations (copying issues between accounts/instances)."
metadata:
  allowed-tools:
    - run_in_terminal
  output-format: markdown
---

# Jira Tools - Agent Skill

## Purpose

Use the bundled PAT-authenticated CLI for Jira operations. This skill covers issue CRUD, JQL search, sprint/board management, worklogs, labels, watchers, links, and bulk operations.

## Read Only What You Need

- `references/action-catalog.md` - exact action names, required args, optional args, response shapes
- `references/usage-recipes.md` - concrete examples, bulk-operation patterns, troubleshooting
- `references/tone-and-disclaimer.md` - enterprise tone guidelines and AI-generated content disclaimer templates

## Architecture

```text
Copilot / Claude Code Agent
  | run_in_terminal
  v
scripts/jira_cli.js  (Node 18+, primary)
  OR
scripts/jira_cli.ps1  (PowerShell 5.1+, fallback)
  | PAT token from .env
  v
Jira REST API
```

## Agent Compatibility

This skill works with any AI coding agent that can execute terminal commands:

| Agent | How to invoke |
|-------|---------------|
| **GitHub Copilot** | Matches via `skills-manifest.json` description, uses `run_in_terminal` |
| **Claude Code** | Read this file as instructions, execute CLI via terminal |
| **Cursor / Other** | Same as Claude Code - read instructions, call CLI |

The CLI contract is agent-agnostic: set `$env:CLI_JSON_ARGS`, run the script, parse JSON output.

## Setup

The CLI reads PAT tokens from `.env` files. It searches these locations in order (later files override earlier):

1. `<workspace>/.env` - workspace root (primary)
2. `<skill>/.env` - skill-local override
3. `$env:ENV_FILE` - explicit override (highest priority)

Required variables:

```properties
JIRA_PAT_TOKEN=<your-token-without-quotes>
JIRA_BASE_URL=https://your-jira-instance.example.com
```

Store token values without surrounding quotes. PAT tokens containing `+`, `/`, or `=` (common in Base64) are valid raw values.

## Execution Contract

**Runtime detection** - run once per session:

```powershell
$nodeAvailable = $null -ne (Get-Command node -ErrorAction SilentlyContinue)
```

### Node CLI (preferred)

```powershell
$env:CLI_JSON_ARGS = '{"issueKey":"PROJ-123"}'; node "<workspace>/skills/_modular/jira/scripts/jira_cli.js" fetch_jira_issue
```

### PowerShell CLI (fallback)

```powershell
$env:CLI_JSON_ARGS = '{"issueKey":"PROJ-123"}'; & "<workspace>/skills/_modular/jira/scripts/jira_cli.ps1" fetch_jira_issue
```

### Rules

- Always use single quotes around static JSON in PowerShell.
- Put `CLI_JSON_ARGS` assignment and script invocation on the same line.
- Never call the CLI without setting `CLI_JSON_ARGS`, even if it is `'{}'`.
- Always parse the `success` field before presenting results.

## Content Tone & AI Disclaimer

When publishing Jira comments or descriptions, read `references/tone-and-disclaimer.md`. Summary:

- Professional, concise, objective. Active voice. No slang, emojis, idioms.
- Jira comments: append italic AI disclaimer footer at the end.

## Defaults and Guardrails

- Retry once with adjusted parameters before asking the user on transient failures.
- Complete full bulk-operation loops and then summarize successes and failures.
- Default `maxResults` is 25; set intentionally for larger result sets.
- If action names or args are unclear, read `references/action-catalog.md`.
- If you need examples or troubleshooting, read `references/usage-recipes.md`.

## Multi-Account Support

This skill supports multiple Jira accounts/instances via profile-scoped credentials.

### Profile setup

Create a `.env.{profileId}` file at the workspace root for each account:

```markdown
# .env.work.primary
JIRA_PAT_TOKEN=your-work-token
JIRA_BASE_URL=https://work-jira.example.com

# .env.personal-work.primary
JIRA_PAT_TOKEN=your-personal-work-token
JIRA_BASE_URL=https://personal-jira.example.com
```

See `.env.example.work.primary` for a full template.

### Single-account with explicit profile

```powershell
$env:CLI_JSON_ARGS = '{"account":"work.primary","issueKey":"PROJ-123"}'
node "skills/_modular/jira/scripts/jira_cli.js" fetch_jira_issue
```

### Cross-account operations

Use `atlassian-common/jira-cross-account.js` to copy issues between accounts:

```powershell
# Copy a single issue
$env:CLI_JSON_ARGS = '{"sourceAccount":"work.primary","targetAccount":"personal-work.primary","issueKey":"PROJ-123","targetProjectKey":"MYPROJ"}'
node "skills/_modular/atlassian-common/jira-cross-account.js" copy_issue

# Bulk copy via JQL
$env:CLI_JSON_ARGS = '{"sourceAccount":"work.primary","targetAccount":"personal-work.primary","jql":"project=PROJ AND sprint in openSprints()","targetProjectKey":"MYPROJ"}'
node "skills/_modular/atlassian-common/jira-cross-account.js" copy_issues_by_jql
```

### Account selection priority

1. `account` arg in `CLI_JSON_ARGS` (highest)
2. `SESSION_ACTIVE_PROFILE` env var (set by `switchProfile()`)
3. `DEFAULT_ATLASSIAN_PROFILE` in `.env` (defaults to `work`)

### Profile naming convention

| Shorthand | Resolves to | Credentials file |
|---|---|---|
| `work` | `work.primary` | `.env.work.primary` |
| `work.secondary` | `work.secondary` | `.env.work.secondary` |
| `work.client-acme` | `work.client-acme` | `.env.work.client-acme` |
| `personal-work` | `personal-work.primary` | `.env.personal-work.primary` |
| `personal` | `personal.primary` | `.env.personal.primary` |
