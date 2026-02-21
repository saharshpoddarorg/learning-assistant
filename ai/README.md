# ai/ -- AI-Generated Content Workspace

A structured workspace for content produced by AI agents, Copilot, custom prompts,
and MCP servers during development and learning sessions.

---

## Three Tiers

```
ai/
  scratch/   DISCARD   session scratchpad -- clear when done       [gitignored]
  local/     KEEP      survives sessions, stays on this machine    [gitignored]
  saved/     PUSH      committed to the repo, permanent reference  [tracked]
  scripts/   TOOLS     one dispatcher + module + aliases + VS Code tasks
```

The three words map to what you would actually say out loud:
- **"This is scratch work"** --> drop it in `scratch/`
- **"I want to keep this"** --> move it to `local/`
- **"This should go to the repo"** --> `ai save` promotes it to `saved/` and commits

---

## Getting Started (30 seconds)

```powershell
# Windows -- load aliases for this terminal session
. .\ai\scripts\ai-module.psm1

# Then:
ai-status                            # see what is in each tier
ai-new                               # create a note (interactive)
ai-save scratch\draft.md             # save to repo (asks for project, tags, commits)
ai-search java --tag generics        # search across all tiers
ai-list --tier saved                 # list committed notes
ai-clear                             # preview scratch contents
ai-clear --force                     # delete scratch without prompt
```

```bash
# Bash -- load aliases for this terminal session
source ./ai/scripts/.ai-aliases.sh

# Same commands, same flags:
ai-status
ai-new --tier local --project mcp-servers
ai-save scratch/draft.md --project java
ai-search --kind decision --tier saved
```

### VS Code Tasks

`Ctrl+Shift+P` → **Tasks: Run Task** → pick any `ai:` task

### Copilot Slash Commands

In Copilot Chat (agent mode), use `/ai-new`, `/ai-save`, `/ai-search`

---

## saved/ Hierarchy

Committed notes are organised by **project bucket** + **month**. This is
created automatically at save time -- you never create folders manually.

```
saved/
  README.md
  <project>/
    <YYYY-MM>/
      YYYY-MM-DD_slug.md
  mcp-servers/
    2026-02/
      2026-02-21_sse-transport-decision.md
  java/
    2026-02/
      2026-02-18_generics-cheatsheet.md
  general/
    2026-01/
      2026-01-15_git-rebase-notes.md
```

When you run `ai save`, it asks which project bucket to use (default: `general`).

---

## Frontmatter

Every note uses this template for search, filtering, and future tooling:

```markdown
---
date: YYYY-MM-DD
kind: note | decision | session | resource | snippet | ref
project: mcp-servers | java | general | <your-project>
tags: [tag1, tag2, tag3]
status: draft | final | archived
source: copilot | manual | mcp
---

# Title

Content here.
```

### `kind` values

| kind | Use for |
|------|---------|
| `note` | General notes, explanations, thoughts |
| `decision` | Architectural or design choices (ADR format) |
| `session` | Log of what happened in a work/learning session |
| `resource` | Links, references, reading material |
| `snippet` | Code or command reference |
| `ref` | Quick-reference card or cheatsheet |

The `save` command auto-suggests tags from your existing frontmatter + filename
keywords. You confirm or adjust before committing.

---

## Naming Convention

```
YYYY-MM-DD_topic-slug.md           <- date-prefixed (most files)
YYYY-MM-DD_project_topic.md        <- when project in name is helpful
topic-slug.md                      <- timeless reference docs (no date)
```

---

## Full Command Reference

See [`ai/scripts/README.md`](scripts/README.md) for the complete dispatcher
reference, PowerShell module, bash aliases, VS Code tasks, and Copilot prompts.

---

## Quick Lifecycle

```
Session starts  -> use scratch/ freely, no structure needed
During session  -> ai-new creates a note with frontmatter ready
Session ends    -> ai-status shows what is in each tier
Worth keeping?  -> yes: ai-save <file>  |  no: ai-clear
```
