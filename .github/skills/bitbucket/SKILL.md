---
name: bitbucket
description: "Bitbucket Server operations via PAT-authenticated REST APIs. Use whenever the user asks about Bitbucket pull requests, PR diffs, code review comments, inline comments, PR tasks, file lookups, branch operations, contribution summaries, or cross-account Bitbucket operations (mirroring PRs between instances)."
metadata:
  allowed-tools:
    - run_in_terminal
  output-format: markdown
---

# Bitbucket Tools - Agent Skill

## Purpose

Use the bundled PAT-authenticated CLI for Bitbucket Server operations. This skill covers pull requests, diffs, comments, inline comments, tasks, file lookups, and contribution summaries.

## Read Only What You Need

- `references/action-catalog.md` - exact action names, required args, optional args, response shapes
- `references/usage-recipes.md` - concrete examples, workflow patterns, troubleshooting
- `references/tone-and-disclaimer.md` - enterprise tone guidelines and AI-generated content disclaimer templates

## Architecture

```
Copilot / Claude Code Agent
  | run_in_terminal
  v
scripts/bitbucket_cli.js  (Node 18+, primary)
  OR
scripts/bitbucket_cli.ps1  (PowerShell 5.1+, fallback)
  | PAT token from .env
  v
Bitbucket Server REST API
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
BITBUCKET_PAT_TOKEN=<your-token-without-quotes>
BITBUCKET_BASE_URL=https://your-bitbucket-instance.example.com
```

Store token values without surrounding quotes.

## Execution Contract

**Runtime detection** - run once per session:

```powershell
$nodeAvailable = $null -ne (Get-Command node -ErrorAction SilentlyContinue)
```

### Node CLI (preferred)

```powershell
$env:CLI_JSON_ARGS = '{"project":"IESD","repo":"iesd-26","prId":"17905"}'; node "<workspace>/skills/bitbucket/scripts/bitbucket_cli.js" fetch_bitbucket_pr
```

### PowerShell CLI (fallback)

```powershell
$env:CLI_JSON_ARGS = '{"project":"IESD","repo":"iesd-26","prId":"17905"}'; & "<workspace>/skills/bitbucket/scripts/bitbucket_cli.ps1" fetch_bitbucket_pr
```

### Rules

- Always use single quotes around static JSON in PowerShell.
- Put `CLI_JSON_ARGS` assignment and script invocation on the same line.
- Never call the CLI without setting `CLI_JSON_ARGS`, even if it is `'{}'`.
- Always parse the `success` field before presenting results.

## Content Tone & AI Disclaimer

When publishing Bitbucket comments, read `references/tone-and-disclaimer.md`. Summary:

- Professional, concise, objective. Active voice. No slang, emojis, idioms.
- Bitbucket comments: append italic AI disclaimer footer at the end.

## Defaults and Guardrails

- Default Bitbucket calls to `project=IESD` and `repo=iesd-26` unless the user asks otherwise.
- Get comment ID before creating a task (Bitbucket tasks are anchored to comments).
- Retry once with adjusted parameters before asking the user on transient failures.
- If action names or args are unclear, read `references/action-catalog.md`.
- If you need examples or troubleshooting, read `references/usage-recipes.md`.

## Multi-Account Support

This skill supports multiple Bitbucket instances via profile-scoped credentials.

### Profile setup

Create a `.env.{profileId}` file at the workspace root for each account:

```
# .env.work.primary
BITBUCKET_PAT_TOKEN=your-work-token
BITBUCKET_BASE_URL=https://work-bitbucket.example.com

# .env.personal-work.primary
BITBUCKET_PAT_TOKEN=your-personal-work-token
BITBUCKET_BASE_URL=https://personal-bitbucket.example.com
```

See `.env.example.work.primary` for a full template.

### Single-account with explicit profile

```powershell
$env:CLI_JSON_ARGS = '{"account":"work.primary","project":"PROJ","repo":"my-repo","prId":42}'
node "skills/bitbucket/scripts/bitbucket_cli.js" fetch_bitbucket_pr
```

### Cross-account operations

Use `atlassian-common/bitbucket-cross-account.js` to mirror PRs between instances:

```powershell
# Mirror a PR to another Bitbucket instance
$env:CLI_JSON_ARGS = '{"sourceAccount":"work.primary","targetAccount":"personal-work.primary","sourceProject":"PROJ","sourceRepo":"my-repo","prId":42,"targetProject":"MYPROJ","targetRepo":"my-repo"}'
node "skills/atlassian-common/bitbucket-cross-account.js" mirror_pr

# Copy PR review comments to the mirrored PR
$env:CLI_JSON_ARGS = '{"sourceAccount":"work.primary","targetAccount":"personal-work.primary","sourceProject":"PROJ","sourceRepo":"my-repo","sourcePrId":42,"targetProject":"MYPROJ","targetRepo":"my-repo","targetPrId":1}'
node "skills/atlassian-common/bitbucket-cross-account.js" copy_pr_comments
```

### Account selection priority

1. `account` arg in `CLI_JSON_ARGS` (highest)
2. `SESSION_ACTIVE_PROFILE` env var
3. `DEFAULT_ATLASSIAN_PROFILE` in `.env` (defaults to `work`)
