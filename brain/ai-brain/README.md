# ai-brain/ -- Personal Knowledge Workspace

A structured workspace for notes, decisions, and references created during
development and learning sessions -- with Copilot, MCP servers, or manually.

---

## Three Tiers

```
ai-brain/
  inbox/     TEMP      quick capture -- clear when session ends     [gitignored]
  notes/     KEEP      curated notes that stay on this machine      [gitignored]
  archive/   PUBLISH   committed to the repo, permanent reference   [tracked]
  scripts/   TOOLS     one dispatcher + module + aliases + VS Code tasks
```

The three words match what you would actually say:
- **"Just capturing this"** → drop it in `inbox/`
- **"I want to keep this"** → move it to `notes/`
- **"This belongs in the repo"** → `brain publish` promotes it to `archive/` and commits

---

## Getting Started (30 seconds)

```powershell
# Windows -- load aliases for this terminal session
. .\brain\ai-brain\scripts\brain-module.psm1

# Then:
brain status                              # see what is in each tier
brain new                                 # create a note (interactive)
brain publish brain\ai-brain\inbox\draft.md        # publish to repo (asks project, tags, commits)
brain search java --tag generics          # search across all tiers
brain list --tier archive                 # list committed notes
brain clear                               # preview inbox contents
brain clear --force                       # delete inbox without prompt
brain move brain\ai-brain\inbox\draft.md --tier notes   # move to notes
```

```bash
# Bash -- load aliases for this terminal session
source ./brain/ai-brain/scripts/.brain-aliases.sh

# Same commands, same flags:
brain status
brain new --tier inbox --project mcp-servers
brain publish brain/ai-brain/inbox/draft.md --project java
brain search --kind decision --tier archive
brain move brain/ai-brain/inbox/draft.md --tier notes
```

### VS Code Tasks

`Ctrl+Shift+P` → **Tasks: Run Task** → pick any `brain:` task

### Copilot Slash Commands

In Copilot Chat (agent mode), use `/brain-new`, `/brain-publish`, `/brain-search`

---

## archive/ Hierarchy

Committed notes are organised by **project bucket** + **month**. This is
created automatically at publish time -- you never create folders manually.

```
archive/
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

When you run `brain publish`, it asks which project bucket to use (default: `general`).

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

The `publish` command auto-suggests tags from your existing frontmatter + filename
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

See [`brain/ai-brain/scripts/README.md`](scripts/README.md) for the complete dispatcher
reference, PowerShell module, bash aliases, VS Code tasks, and Copilot prompts.

---

## Quick Lifecycle

```
Session starts  -> use inbox/ freely, no structure needed
During session  -> brain new creates a note with frontmatter ready
Session ends    -> brain status shows what is in each tier
Worth keeping?  -> yes: brain publish <file>  |  no: brain clear
Move to notes?  -> brain move <file> --tier notes
```