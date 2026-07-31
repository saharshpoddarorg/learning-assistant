---
name: jira
description: "Jira operations via PAT-authenticated REST APIs. Use whenever the user asks about Jira tickets, JQL queries, sprints, boards, worklogs, issue transitions, labels, watchers, or bulk issue operations."
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

```
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

```
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
$env:CLI_JSON_ARGS = '{"issueKey":"PROJ-123"}'; node "<workspace>/skills/jira/scripts/jira_cli.js" fetch_jira_issue
```

### PowerShell CLI (fallback)

```powershell
$env:CLI_JSON_ARGS = '{"issueKey":"PROJ-123"}'; & "<workspace>/skills/jira/scripts/jira_cli.ps1" fetch_jira_issue
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
