---
name: atlassian-tools
description: "Jira, Confluence, and Bitbucket operations via the bundled Node CLI. Use whenever the user asks about Jira tickets, JQL, sprints, worklogs, Confluence pages/wiki/blogs/templates/PDF export, or Bitbucket PRs, diffs, comments, tasks, and file lookups. Prefer this skill over browser-driven Atlassian workflows in this repo."
metadata:
  allowed-tools:
    - run_in_terminal
  output-format: markdown
---

## Purpose

Use the bundled PAT-authenticated CLI for Jira, Confluence, and Bitbucket operations in this workspace. Keep the main skill lean and read only the reference file that matches the current task.

## Read Only What You Need

- `references/action-catalog.md`: exact action names, required args, optional args, response shapes
- `references/confluence-formatting.md`: Confluence storage-format rules, macros, Mermaid rendering, HTML payload patterns
- `references/usage-recipes.md`: concrete examples, bulk-operation patterns, server quirks, pagination, troubleshooting
- `references/tone-and-disclaimer.md`: enterprise tone guidelines, word-choice rules, and AI-generated content disclaimer templates

## Architecture

```
Agent (GitHub Copilot)
  ↓ run_in_terminal
atlassian_cli.js
  ↓ PAT token loaded from .env file
  ↓ Authorization: Bearer <token>
Jira / Confluence / Bitbucket REST APIs
```

Zero external dependencies. Requires only Node.js 18+.

## Setup

The CLI reads PAT tokens from `.env` files. It searches these locations **in order** (later files override earlier):

1. `<workspace>/.env` — the workspace root (primary location)
2. `<skill>/.env` — skill-local override
3. `$env:ENV_FILE` — explicit override (highest priority)

Required variables:

- `JIRA_PAT_TOKEN`
- `CONFLUENCE_PAT_TOKEN`
- `BITBUCKET_PAT_TOKEN`
- `JIRA_BASE_URL`
- `CONFLUENCE_BASE_URL`
- `BITBUCKET_BASE_URL`

**Important:** Store token values **without** surrounding quotes. PAT tokens that contain `+`, `/`, or `=` (common in Base64) are valid and should be stored as raw values.

Example `.env`:

```
JIRA_PAT_TOKEN=NjU0OTcyNTY3N+Dg4NTkw...
JIRA_BASE_URL=https://ies-iesd-jira.ies.mentorg.com
CONFLUENCE_PAT_TOKEN=MTg0NjUwMjA5O+Dg4...
CONFLUENCE_BASE_URL=https://ies-iesd-conf.ies.mentorg.com
BITBUCKET_PAT_TOKEN=MTg0NjUwMjA5ODc1...
BITBUCKET_BASE_URL=https://ies-iesd-bitbucket.ies.mentorg.com
```

If `.env` is missing or a token is absent, stop and tell the user exactly which variable is missing.

## Workspace Scratch Policy

For large HTML content or other generated intermediary files, use `<workspace>/temp-atlassian-tools/`.

- Create the folder on demand.
- Prefer stable filenames like `cli_content.html` and `confluence-mermaid.html`.
- Overwrite reusable files instead of creating timestamped duplicates.
- Do not write temp files inside `.github/skills/atlassian-tools/`.
- Do not delete scratch files immediately unless they contain sensitive content or the user explicitly asks for cleanup.

## Execution Contract

Set JSON arguments in `CLI_JSON_ARGS`, then invoke the CLI.

```powershell
$env:CLI_JSON_ARGS = '{"issueKey":"PROJ-123"}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" fetch_jira_issue
```

Rules:

- Always use single quotes around static JSON in PowerShell.
- Put `CLI_JSON_ARGS` assignment and `node ...` invocation on the same line.
- Never call the CLI without setting `CLI_JSON_ARGS`, even if it is `'{}'`.
- Always parse the `success` field before presenting results.
- For short HTML payloads, prefer `contentFromEnv` with `CLI_CONTENT`.
- For larger HTML payloads, use `contentFile` plus `<workspace>/temp-atlassian-tools/`.
- On Windows PowerShell, do not round-trip fetched Confluence page JSON through `Out-File`, `Get-Content`, or `ConvertFrom-Json` and then re-upload the resulting HTML. Non-ASCII punctuation like `—`, `→`, `↓`, and `’` can be mojibake-corrupted. Prefer a UTF-8 `contentFile` you generated locally, or fetch page HTML with `Invoke-RestMethod` before re-uploading.

## Content Tone & AI Disclaimer

Read `references/tone-and-disclaimer.md` before generating any published content. Summary:

- Professional, concise, objective. Active voice. No slang, emojis, idioms, or casual phrasing.
- Global audience: sentences ≤ 25 words, no culture-specific metaphors.
- Sentence-case headings; abbreviations spelled out on first use; dates as `03 Apr 2026`.
- **`{{AUTHOR_NAME}}`** — resolve from user memory (`/memories/user-info.md` → `Name:` field), then session context, then ask the user before publishing.
- **Confluence pages:** insert the `note` macro disclaimer at the top (template in reference file).
- **Jira/Bitbucket comments:** append italic footer at the end (template in reference file).
- Never remove an existing disclaimer on AI edits — update the date only.

## Defaults And Guardrails

- Prefer Confluence page IDs over URLs.
- Default Bitbucket calls to `project=IESD` and `repo=iesd-26` unless the user asks otherwise.
- Retry once with adjusted parameters before asking the user for help on a transient failure.
- Complete full bulk-operation loops and then summarize successes and failures.
- If action names or args are unclear, read `references/action-catalog.md`.
- If generating Confluence HTML or Mermaid, read `references/confluence-formatting.md`.
- If you need examples, quirks, or troubleshooting, read `references/usage-recipes.md`.
