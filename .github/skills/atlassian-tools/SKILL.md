---
name: atlassian-tools
description: >
  Jira, Confluence, and Bitbucket operations via the bundled PAT-authenticated Node CLI
  (89 actions, zero external dependencies). Use whenever the user asks about: Jira tickets,
  JQL queries, sprints, boards, epics, worklogs, issue transitions, bulk operations, labels,
  watchers, issue links, subtasks, cloning issues, Confluence pages, wiki content, blogs,
  templates, PDF export, CQL search, page trees, labels, inline comments, page versioning,
  Mermaid diagrams in Confluence, Bitbucket PRs, diffs, file lookups, PR comments, tasks,
  contribution summaries, or any Atlassian Server/Data Center REST API task. Also activates
  when the user mentions JQL, CQL, sprint planning, release management, retrospective
  documentation, cross-tool workflows (Jira-to-Confluence, PR-to-Jira), cross-account
  Confluence migration (copy/move pages between instances), resume building from Jira work
  history, work contribution analysis, Bitbucket-to-GitHub code migration, repo mirroring,
  PR history export, page tree cloning, bulk page updates, or any cross-platform Atlassian
  workflow. This skill is the **universal handler for ALL Atlassian-related requests** —
  if the task involves Jira, Confluence, or Bitbucket in any capacity, this skill applies.
  Prefer this skill over browser-driven Atlassian workflows in this repo.
metadata:
  allowed-tools:
    - run_in_terminal
  output-format: markdown
---

# Atlassian Tools Skill

> **Scope:** Jira, Confluence, and Bitbucket Server/Data Center operations via PAT-authenticated CLI.
> **Complements:** `github-workflow` (GitHub-specific PRs/issues), `software-development-roles` (role workflows).
> **Boundary:** This skill covers Atlassian Server REST APIs. For GitHub operations, see `github-workflow`.
> For local Git commands, see `git-vcs`.

---

## Quick Decision Card

| I want to... | Action / Reference |
|---|---|
| Fetch or search Jira issues | `fetch_jira_issue`, `search_jira_issues` (JQL) |
| Create/update Jira tickets | `create_jira_issue`, `update_jira_issue` |
| Transition issue status | `transition_jira_issue` |
| Log time on a ticket | `add_jira_worklog` |
| Manage sprints & epics | `get_jira_sprints`, `get_sprint_issues`, `get_epic_issues` |
| Bulk-create or bulk-transition | `bulk_create_jira_issues`, `bulk_transition_jira_issues` |
| Create/update Confluence pages | `create_confluence_page`, `update_confluence_page` |
| Search Confluence content | `search_confluence`, `search_confluence_cql` |
| Add Mermaid diagrams to Confluence | Read `references/confluence-formatting.md` |
| Review a Bitbucket PR | `fetch_bitbucket_pr`, `fetch_bitbucket_pr_diff` |
| Comment on a Bitbucket PR | `add_bitbucket_pr_comment` |
| Know exact args for an action | Read `references/action-catalog.md` |
| Follow workflow recipes | Read `references/usage-recipes.md` |
| Write enterprise-tone content | Read `references/tone-and-disclaimer.md` |
| Write JQL/CQL queries | Read `references/jql-cql-cheatsheet.md` |
| Run end-to-end workflows | Read `references/workflow-playbooks.md` |
| Copy/move pages between Confluence instances | Playbook 9 — cross-account migration |
| Copy/move pages within same instance | `copy_confluence_page`, `move_confluence_page` |
| Merge content from multiple pages into one | Playbook 9 Scenario D |
| Build resume from Jira + Bitbucket history | Playbook 10 — resume / work analysis |
| Analyse my work contributions | `search_jira_issues` + `summarize_bitbucket_contributions` |
| Mirror Bitbucket repo to GitHub | Playbook 11 — `git clone --bare` + `git push --mirror` |
| Copy files from Bitbucket to GitHub | Playbook 11 Scenario B — `fetch_bitbucket_file` + `gh` |
| Export PR history before migration | Playbook 11 Scenario E — PR archive |
| Bulk-update Confluence pages by label | Playbook 12 — search-and-update pattern |
| Analyse sprint velocity | Playbook 13 — sprint velocity analysis |
| Map issue dependencies | Playbook 13 — `get_jira_issue_links` traversal |

---

## Purpose

This skill is the **universal handler for all Atlassian-related tasks**. Any request involving
Jira, Confluence, or Bitbucket — regardless of complexity, direction, or number of instances —
should be routed through this skill and its reference files.

Use the bundled PAT-authenticated CLI for Jira, Confluence, and Bitbucket operations in this
workspace. Keep the main skill lean and read only the reference file that matches the current task.

### General Routing — How to Handle Any Atlassian Request

When the user asks for something not explicitly listed in the Quick Decision Card:

1. **Identify the services involved** — which of Jira, Confluence, Bitbucket are needed?
2. **Read `references/action-catalog.md`** — scan the 89 actions to find the closest match
3. **Check `references/workflow-playbooks.md`** — see if a multi-step playbook already covers it
4. **Compose a workflow** — chain CLI actions together; if cross-instance, use the multi-instance
   env-swap pattern from the playbooks
5. **If the CLI lacks a needed action** — fall back to direct REST API calls via `Invoke-RestMethod`
   using the same PAT token from `.env` (Bearer auth header)

**The 89 CLI actions and 13 playbooks are starting points, not limits.** The CLI wraps REST APIs,
and any Atlassian Server REST endpoint can be called directly when the CLI does not have a
dedicated action. Common extension patterns:

| Need | Approach |
|---|---|
| Action not in CLI | `Invoke-RestMethod` with Bearer token from `.env` |
| Cross-instance operation | Swap env vars between calls (see Multi-Instance Setup) |
| Cross-platform (Atlassian + GitHub) | Combine CLI with `git`/`gh` CLI |
| Cross-platform (Atlassian + other) | CLI for Atlassian side, appropriate tool for the other side |
| Bulk operation not built in | Loop over search results and call per-item actions |
| Custom reporting / analysis | Fetch data via CLI, process in PowerShell, output markdown |

## Read Only What You Need

- `references/action-catalog.md`: exact action names, required args, optional args, response shapes (87 actions)
- `references/confluence-formatting.md`: Confluence storage-format rules, macros, Mermaid rendering, HTML payload patterns
- `references/usage-recipes.md`: concrete examples, bulk-operation patterns, server quirks, pagination, troubleshooting
- `references/tone-and-disclaimer.md`: enterprise tone guidelines, word-choice rules, and AI-generated content disclaimer templates
- `references/jql-cql-cheatsheet.md`: 30+ JQL templates and 15+ CQL templates for common queries
- `references/workflow-playbooks.md`: end-to-end workflow patterns — sprint ceremonies, release mgmt,
  incident docs, status reporting, cross-account Confluence migration, resume/work analysis,
  Bitbucket↔GitHub migration, advanced Confluence content ops, Jira deep analysis, and more

## Architecture

```text
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

```properties
JIRA_PAT_TOKEN=NjU0OTcyNTY3N+Dg4NTkw...
JIRA_BASE_URL=https://ies-iesd-jira.ies.mentorg.com
CONFLUENCE_PAT_TOKEN=MTg0NjUwMjA5O+Dg4...
CONFLUENCE_BASE_URL=https://ies-iesd-conf.ies.mentorg.com
BITBUCKET_PAT_TOKEN=MTg0NjUwMjA5ODc1...
BITBUCKET_BASE_URL=https://ies-iesd-bitbucket.ies.mentorg.com
```

If `.env` is missing or a token is absent, stop and tell the user exactly which variable is missing.

### Multi-Instance Setup (Cross-Account Operations)

To operate across two different Atlassian instances (e.g., copy Confluence pages from Account A to
Account B), swap environment variables between CLI calls. Create separate `.env` files per instance:

```text
<workspace>/.env                    ← default instance (Account A)
<workspace>/.env.account-b          ← second instance (Account B)
```

Use the `Load-EnvFile` helper and env-var swap patterns described in `references/workflow-playbooks.md`
(Multi-Instance Setup section). Key rules:

- Always restore original credentials after cross-account operations
- Never log or echo token values
- Store extra `.env` files in the same gitignored location as `.env`

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
- If writing JQL/CQL queries, read `references/jql-cql-cheatsheet.md`.
- If running end-to-end workflows, read `references/workflow-playbooks.md`.

---

## JQL Quick Reference (top 10)

Most common JQL patterns — for the full cheatsheet (30+), see `references/jql-cql-cheatsheet.md`.

```text
# My open issues
assignee = currentUser() AND status != Done ORDER BY updated DESC

# Sprint backlog
sprint in openSprints() AND project = PROJ ORDER BY rank ASC

# Overdue issues
duedate < now() AND status != Done ORDER BY duedate ASC

# Created this week
created >= startOfWeek() AND project = PROJ ORDER BY created DESC

# Unassigned bugs
issuetype = Bug AND assignee is EMPTY AND status != Done

# Issues in epic
"Epic Link" = PROJ-100 ORDER BY rank ASC

# Updated in last 24 hours
updated >= -24h AND project = PROJ ORDER BY updated DESC

# Blockers and criticals
priority in (Blocker, Critical) AND status != Done ORDER BY priority ASC

# Issues I'm watching
watcher = currentUser() ORDER BY updated DESC

# Issues with no labels
labels is EMPTY AND project = PROJ ORDER BY created DESC
```

---

## CQL Quick Reference (top 5)

```text
# Pages I recently modified
contributor = currentUser() AND lastModified >= now("-7d") ORDER BY lastModified DESC

# Pages in a space by label
space = "ENG" AND label = "architecture" ORDER BY lastModified DESC

# Full-text search in a space
text ~ "deployment guide" AND space = "ENG"

# Recently created pages
created >= now("-30d") AND type = page ORDER BY created DESC

# Blog posts in a space
type = blogpost AND space = "ENG" ORDER BY created DESC
```

---

## Tier-Based Reference

### Newbie — Getting Started

If you are new to the Atlassian CLI skill:

1. **Set up `.env`** — create `<workspace>/.env` with your PAT tokens and base URLs (see Setup above)
2. **Test connectivity** — run `get_current_jira_user` to verify your token works
3. **Fetch an issue** — try `fetch_jira_issue` with a known issue key
4. **Search** — try `search_jira_issues` with simple JQL like `project = PROJ`
5. **Read a page** — try `fetch_confluence_page` with a known page ID

Key concepts:

- Every CLI call needs `CLI_JSON_ARGS` set as an environment variable
- Results are JSON with `success: true/false`
- Use single quotes around JSON in PowerShell
- Page IDs are preferred over URLs for Confluence

### Amateur — Daily Workflows

Once comfortable with basics:

- **Sprint planning**: `get_jira_sprints` → `get_sprint_issues` → `search_jira_issues` → `move_to_sprint`
- **Bulk operations**: `bulk_create_jira_issues` and `bulk_transition_jira_issues` for batch work
- **Cross-tool workflows**: fetch PR details from Bitbucket, create Confluence review pages
- **JQL power queries**: combine fields, functions, and ordering (see JQL cheatsheet above)
- **Confluence page creation**: use `contentFile` pattern for HTML with macros and Mermaid
- **Labels and watchers**: organize issues with `add_jira_labels`, track with `add_jira_watcher`
- **Issue links and subtasks**: structure work with `link_jira_issues`, `create_jira_issue` (with `parentKey`)

### Pro — Advanced Patterns

For power users:

- **Workflow playbooks**: end-to-end patterns in `references/workflow-playbooks.md`
- **Confluence HTML macros**: code blocks, info panels, Mermaid diagrams, TOC, expand sections
- **Bulk automation loops**: iterate over search results, transition/comment/label in bulk
- **Page versioning**: `get_confluence_page_versions`, `restore_confluence_page_version`
- **Contribution analysis**: `summarize_bitbucket_contributions` for team metrics
- **Page tree management**: `get_confluence_page_tree`, `move_confluence_page`, `copy_confluence_page`
- **CQL advanced queries**: combine space, label, date, contributor, and content type filters
- **Enterprise tone enforcement**: AI disclaimer templates, professional formatting standards
- **UTF-8 safety**: always use `contentFile` for non-ASCII content to avoid PowerShell mojibake
- **Cross-account Confluence migration**: copy/move pages between instances (Playbook 9)
- **Resume / work analysis**: build resume bullets from Jira + Bitbucket data (Playbook 10)
- **Bitbucket ↔ GitHub migration**: mirror repos, migrate PRs, export PR history (Playbook 11)
- **Bulk page operations**: search-and-update by label, clone page trees (Playbook 12)
- **Sprint velocity analysis**: closed-sprint metrics across boards (Playbook 13)
- **Issue dependency mapping**: traverse `get_jira_issue_links` graph (Playbook 13)
- **Multi-instance env switching**: `Load-EnvFile` helper for cross-account workflows

---

## CLI Action Count Summary

| Service | Category | Actions |
|---|---|---|
| Jira | Core (CRUD, search, comments, worklogs) | 10 |
| Jira | Links & Relations | 4 |
| Jira | Bulk Operations | 2 |
| Jira | Labels | 2 |
| Jira | Watchers | 3 |
| Jira | Metadata (types, statuses, components, users) | 7 |
| Jira | Agile / Sprint | 6 |
| Confluence | Core (pages, search, comments) | 11 |
| Confluence | Content Management (copy, move, labels, versions) | 12 |
| Confluence | Navigation & Structure | 4 |
| Confluence | Spaces, Blogs & More | 7 |
| Bitbucket | Core (PRs, diffs, files, contributions) | 7 |
| Bitbucket | PR Comments | 5 |
| Bitbucket | Tasks | 6 |
| Bitbucket | File Operations | 3 |
| **Total** | | **89** |
