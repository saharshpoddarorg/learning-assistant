---
name: confluence
description: "Confluence operations via PAT-authenticated REST APIs. Use whenever the user asks about Confluence pages, wiki content, blogs, templates, PDF export, page labels, comments, CQL search, content management, or cross-account Confluence operations (migrating pages between instances)."
metadata:
  allowed-tools:
    - run_in_terminal
  output-format: markdown
---

# Confluence Tools - Agent Skill

## Purpose

Use the bundled PAT-authenticated CLI for Confluence operations. This skill covers page CRUD, content formatting, CQL search, blogs, templates, comments, labels, page tree navigation, PDF export, and version management.

## Read Only What You Need

- `references/action-catalog.md` - exact action names, required args, optional args, response shapes
- `references/confluence-formatting.md` - Confluence storage-format rules, macros, Mermaid rendering, HTML payload patterns
- `references/usage-recipes.md` - concrete examples, CQL recipes, pagination, troubleshooting
- `references/tone-and-disclaimer.md` - enterprise tone guidelines and AI-generated content disclaimer templates

## Architecture

```text
Copilot / Claude Code Agent
  | run_in_terminal
  v
scripts/confluence_cli.js  (Node 18+, primary)
  OR
scripts/confluence_cli.ps1  (PowerShell 5.1+, fallback - READ ONLY)
  | PAT token from .env
  v
Confluence REST API
```

**Important:** The PowerShell fallback blocks Confluence HTML-write actions due to PowerShell 5.1 UTF-8 encoding limitations. These actions require Node.js: `create_confluence_page`, `update_confluence_page`, `append_to_confluence_page`, `add_confluence_comment`, `reply_to_confluence_comment`, `add_confluence_inline_comment`, `create_confluence_blog_post`, `copy_confluence_page`, `move_confluence_page`.

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
CONFLUENCE_PAT_TOKEN=<your-token-without-quotes>
CONFLUENCE_BASE_URL=https://your-confluence-instance.example.com
```

Store token values without surrounding quotes. Node.js 18+ is required for write operations.

## Workspace Scratch Policy

For large HTML content or generated intermediary files, use `<workspace>/temp-confluence/`.

- Create the folder on demand.
- Prefer stable filenames like `cli_content.html` and `confluence-mermaid.html`.
- Overwrite reusable files instead of creating timestamped duplicates.
- Do not write temp files inside skill folders.

## Execution Contract

**Runtime detection** - run once per session:

```powershell
$nodeAvailable = $null -ne (Get-Command node -ErrorAction SilentlyContinue)
```

### Node CLI (preferred - required for write operations)

```powershell
$env:CLI_JSON_ARGS = '{"pageId":"123456"}'; node "<workspace>/skills/_modular/confluence/scripts/confluence_cli.js" fetch_confluence_page
```

### PowerShell CLI (fallback - read operations only)

```powershell
$env:CLI_JSON_ARGS = '{"pageId":"123456"}'; & "<workspace>/skills/_modular/confluence/scripts/confluence_cli.ps1" fetch_confluence_page
```

### Content delivery

- For short HTML: use `contentFromEnv` with `$env:CLI_CONTENT`
- For larger HTML: use `contentFile` plus `<workspace>/temp-confluence-tools/`
- On Windows PowerShell, do NOT round-trip fetched Confluence page JSON through `Out-File`, `Get-Content`, or `ConvertFrom-Json` and then re-upload. Non-ASCII punctuation can be corrupted.

### Rules

- Always use single quotes around static JSON in PowerShell.
- Put `CLI_JSON_ARGS` assignment and script invocation on the same line.
- Never call the CLI without setting `CLI_JSON_ARGS`, even if it is `'{}'`.
- Always parse the `success` field before presenting results.

## Content Tone & AI Disclaimer

Read `references/tone-and-disclaimer.md` before generating published content. Summary:

- Professional, concise, objective. Active voice. No slang, emojis, idioms.
- Confluence pages: insert the `note` macro disclaimer at the bottom.
- Never remove an existing disclaimer on AI edits - update the date only.

## Formatting Rules

When creating or updating any Confluence page, always read `references/confluence-formatting.md` and follow:

- The Modern Page System (TOC, executive snapshot, visual diagram, primary evidence)
- The Visual Diagram Requirement (at least one Mermaid diagram per page)
- The lighter table styling rules (no dark navy headers)
- Macro rules (correct body tags and CDATA wrapping)

## Defaults and Guardrails

- Prefer Confluence page IDs over URLs.
- Retry once with adjusted parameters before asking the user on transient failures.
- Complete full bulk-operation loops and then summarize successes and failures.
- If action names or args are unclear, read `references/action-catalog.md`.
- If you need CQL recipes or troubleshooting, read `references/usage-recipes.md`.

## Multi-Account Support

This skill supports multiple Confluence instances via profile-scoped credentials.

### Profile setup

Create a `.env.{profileId}` file at the workspace root for each account:

```markdown
# .env.work.primary
CONFLUENCE_PAT_TOKEN=your-work-token
CONFLUENCE_BASE_URL=https://work-confluence.example.com

# .env.personal-work.primary
CONFLUENCE_PAT_TOKEN=your-personal-work-token
CONFLUENCE_BASE_URL=https://personal-confluence.example.com
```

See `.env.example.work.primary` for a full template.

### Single-account with explicit profile

```powershell
$env:CLI_JSON_ARGS = '{"account":"work.primary","pageId":"12345"}'
node "skills/_modular/confluence/scripts/confluence_cli.js" fetch_confluence_page
```

### Cross-account operations

Use `atlassian-common/confluence-cross-account.js` to migrate pages between instances:

```powershell
# Migrate a single page
$env:CLI_JSON_ARGS = '{"sourceAccount":"work.primary","targetAccount":"personal-work.primary","pageId":"12345","targetSpaceKey":"MYSPACE"}'
node "skills/_modular/atlassian-common/confluence-cross-account.js" migrate_page

# Migrate a page and all its children recursively
$env:CLI_JSON_ARGS = '{"sourceAccount":"work.primary","targetAccount":"personal-work.primary","pageId":"12345","targetSpaceKey":"MYSPACE","targetParentId":"67890"}'
node "skills/_modular/atlassian-common/confluence-cross-account.js" migrate_page_tree
```

### Account selection priority

1. `account` arg in `CLI_JSON_ARGS` (highest)
2. `SESSION_ACTIVE_PROFILE` env var
3. `DEFAULT_ATLASSIAN_PROFILE` in `.env` (defaults to `work`)
