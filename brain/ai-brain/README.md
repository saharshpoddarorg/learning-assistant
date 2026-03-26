# ai-brain/ -- Personal Knowledge Workspace

A structured workspace for notes, decisions, and references created during
development and learning sessions — with Copilot, MCP servers, or manually.

---

## Five Tiers

```text
ai-brain/
  inbox/     TEMP       raw capture — drop anything here, clear when done   [gitignored]
  notes/     YOURS      your own writing — insights, sessions, decisions     [tracked]
  library/   SOURCES    imported source materials you want to preserve       [tracked]
  sessions/  CAPTURED   AI conversation captures — research, analysis, etc.  [tracked]
  backlog/   TRACKED    todos, ideas, features, brainstorming — kanban board [tracked]
  scripts/   TOOLS      one dispatcher + module + aliases + VS Code tasks
```

### The routing question

> **What kind of content is this?**
>
> - **Inbox** — not ready yet (raw paste, draft, anything goes — gitignored, cleared per session)
> - **Notes** — yes, I wrote it (your distilled insights, your session logs, your decisions, your how-tos)
> - **Library** — no, I imported it (external slide decks, presenter guides, external reference docs, AI session outputs you received)
> - **Sessions** — it was a valuable AI conversation (research, analysis, code review, learning deep-dives worth preserving)
> - **Backlog** — it's work to track (todos, features, ideas, brainstorming, requirements)

### Examples

| Content | Tier | Why |
|---|---|---|
| Raw paste of a slide deck | inbox → library | It's imported source material |
| KS session transcript you received | inbox → library | You didn't write it |
| Distilled insights you synthesised from a KS session | inbox → notes | You wrote it |
| Session log of what you fixed today | notes | Your writing, your session |
| Architecture decision you made | notes | Your decision, your reasoning |
| External reference guide you imported | library | Not your writing |
| Your own cheatsheet for a tool | notes | You authored it |
| Deep code analysis conversation | sessions | AI conversation with analytical depth |
| Research session on a technology | sessions | AI-assisted exploration worth referencing |
| Complex debugging investigation | sessions | Multi-step AI-assisted debugging |
| "Add filtering to search tool" | backlog/items | Concrete work to track |
| "What if vault had voice search..." | backlog/ideas | Vague idea to capture and refine |
| "Eventually refactor the v1 handlers" | backlog/ideas | Future intent, not yet actionable |
| "Remember to check API rate limits" | backlog/notes | Quick reminder, no structure needed |
| "How should we handle auth?" | backlog/ideas | Brainstorm — explore options |
| "GHCP should format errors like..." | backlog/guides | Context guide for GHCP to follow |

---

## Getting Started (30 seconds)

```powershell
# Windows — load aliases for this terminal session
. .\brain\ai-brain\scripts\brain-module.psm1

# Then:
brain status                                              # see what is in each tier
brain new                                                 # create a note (interactive)
brain new --tier notes --project mcp-servers              # create directly in notes
brain publish brain\ai-brain\inbox\draft.md               # publish to library (asks project, tags, commits)
brain search java --tag generics                          # search across all tiers
brain list --tier library                                 # list library sources
brain list --tier notes                                   # list your notes
brain clear                                               # preview inbox contents
brain clear --force                                       # delete inbox without prompt
brain move brain\ai-brain\inbox\draft.md --tier notes     # move to notes
brain move brain\ai-brain\inbox\draft.md --tier library   # move to library (manual, no prompts)
```

```bash
# Bash — load aliases for this terminal session
source ./brain/ai-brain/scripts/.brain-aliases.sh

# Same commands, same flags:
brain status
brain new --tier notes --project mcp-servers
brain publish brain/ai-brain/inbox/draft.md --project ghcp-knowledge-sharing
brain search --kind decision --tier notes
brain move brain/ai-brain/inbox/draft.md --tier notes
```

### VS Code Tasks

`Ctrl+Shift+P` → **Tasks: Run Task** → pick any `brain:` task

### Copilot Slash Commands

In Copilot Chat (agent mode), use `/brain-new`, `/brain-publish`, `/brain-search`

---

## library/ Hierarchy

Library content is organised by **project bucket** + **month**. Created automatically
at publish time — you never create folders manually.

```text
library/
  README.md
  <project>/
    <YYYY-MM>/
      YYYY-MM-DD_slug.md
  ghcp-knowledge-sharing/
    2026-03/
      2026-03-06_ghcp-ks-source-index.md
      2026-03-06_ghcp-knowledge-sharing-session.md
      2026-03-06_ghcp-custom-agents-guide.md
      2026-03-06_ghcp-mermaid-diagrams-session.md
      2026-03-06_ghcp-prompt-files-guide.md
      2026-03-06_ghcp-presenter-notes.md
      2026-03-06_ghcp-combined-session-deck.md
```

When you run `brain publish`, it asks which project bucket to use (default: `general`).

---

## sessions/ Hierarchy

Session captures are organised by **domain** (work / personal) and **category**:

```text
sessions/
  README.md
  SESSION-LOG.md              ← append-only index of all captures
  work/
    code-analysis/
    debugging/
    requirements/
    performance/
    feature-exploration/
    documentation/
    research/
    general/
  personal/
    learning/
    project-dev/
    financial/
    research/
    general/
  _templates/
    session-capture.md
```

Sessions are **auto-captured** by Copilot when conversations involve research, analysis,
complex debugging, learning deep-dives, or other substantive exchanges. Simple refactoring
and routine tasks are not captured. See `.github/instructions/chat-capture.instructions.md`
for the full capture policy.

### Session naming

```text
YYYY-MM-DD_HH-MMtt_<category>_<subject>[_v<N>].md
```

Example: `2026-03-20_10-30am_code-analysis_order-service-calculate-total.md`

---

## backlog/ Hierarchy

The backlog tier tracks todos, features, ideas, brainstorming, quick notes, and GHCP guides:

```text
backlog/
  README.md
  BOARD.md                    ← kanban board — at-a-glance status view
  items/                      ← concrete work (BLI-NNN_title.md)
  ideas/                      ← raw ideas & brainstorms (IDEA-NNN_title.md)
  epics/                      ← grouping themes (EPIC-NNN_title.md)
  notes/                      ← quick plain-text captures (NOTE-NNN_title.md)
  guides/                     ← GHCP context guides (GUIDE-NNN_title.md)
  _templates/
    item.md                   ← feature / bug / task template
    idea.md                   ← raw idea with refinement trail
    brainstorm.md             ← whiteboard-style exploration
    epic.md                   ← epic grouping template
    note.md                   ← ultra-lightweight quick capture
    guide.md                  ← GHCP context guide / playbook
```

Use `/backlog` in Copilot Chat to manage all entry types via a single command.

See `brain/ai-brain/backlog/README.md` for the full system documentation and
`.github/instructions/backlog.instructions.md` for the AI-assisted management protocol.

---

## Frontmatter

Every note uses YAML frontmatter for search, filtering, and future tooling:

```markdown
---
date: YYYY-MM-DD
kind: note | decision | session | resource | snippet | ref
project: mcp-servers | java | general | <your-project>
tags: [tag1, tag2, tag3]
status: draft | final | archived
source: copilot | manual | imported
---

# Title

Content here.
```

### `kind` values — and which tier they belong in

| kind | Tier | Use for |
|------|------|---------|
| `note` | notes/ | General notes, explanations, thoughts you authored |
| `decision` | notes/ | Architectural or design choices (ADR format) |
| `session` | notes/ | Log of what happened in a work/learning session |
| `ref` | library/ | Imported external reference, slide deck, source document |
| `resource` | library/ | Imported links, reading lists, curated resource collections |
| `snippet` | notes/ or library/ | Code or command reference (yours vs imported) |

The `publish` command auto-suggests tags from your existing frontmatter + filename
keywords. You confirm or adjust before committing.

---

## Naming Convention

```text
YYYY-MM-DD_topic-slug.md           ← date-prefixed (most files)
YYYY-MM-DD_session-<topic>.md      ← session log (what you built/did today)
YYYY-MM-DD_decision-<topic>.md     ← architectural decision log
YYYY-MM-DD_<source>-<topic>.md     ← imported source file in library/
topic-slug.md                      ← timeless reference docs (no date)
```

---

## Full Command Reference

See [`brain/ai-brain/scripts/README.md`](scripts/README.md) for the complete dispatcher
reference, PowerShell module, bash aliases, VS Code tasks, and Copilot prompts.

---

## Quick Lifecycle

```text
Session starts  -> inbox/ freely, no structure needed
During session  -> brain new --tier notes creates a note with frontmatter
Session ends    -> brain status shows what is in each tier
External source -> brain publish <inbox-file> --project <bucket>  (goes to library/)
Your own work  -> brain move <inbox-file> --tier notes             (goes to notes/)
Not worth keeping -> brain clear
```
