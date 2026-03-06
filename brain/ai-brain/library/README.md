# ai-brain/library/ -- Preserved Source Materials

**Tracked by git. Pushed to the repo. Permanent source archive.**

The library holds external content you imported and want to preserve: slide decks, reference
guides, presenter notes, raw session documents — anything you did **not** write yourself.
Sources are kept verbatim (or near-verbatim) with frontmatter added for searchability.

## notes/ vs library/ — the core rule

| Tier | Rule | Examples |
|---|---|---|
| `notes/` | **I wrote this** — your synthesis, your session log, your decisions | distilled insights, session changelogs, ADRs, how-tos you authored |
| `library/` | **I preserved this** — external source you imported | slide decks, presenter guides, external reference docs, imported session transcripts |

If you're unsure: *Did you write it?* → `notes/`. *Did you import it?* → `library/`.

---

## Hierarchy

Organised by **project bucket** then **month**. Created automatically by `brain publish` —
you never create folders manually.

```
library/
  <project>/
    <YYYY-MM>/
      YYYY-MM-DD_slug.md
  ghcp-knowledge-sharing/
    2026-03/
      2026-03-06_ghcp-ks-source-index.md
      2026-03-06_ghcp-knowledge-sharing-session.md
      2026-03-06_ghcp-combined-session-deck.md
      2026-03-06_ghcp-custom-agents-guide.md
      2026-03-06_ghcp-mermaid-diagrams-session.md
      2026-03-06_ghcp-prompt-files-guide.md
      2026-03-06_ghcp-presenter-notes.md
```

---

## How to Add a Source to Library

### Method 1 — brain publish (interactive)

```powershell
# From inbox: interactive publish (asks project, confirms tags, commits)
.\brain\ai-brain\scripts\brain.ps1 publish brain\ai-brain\inbox\draft.md

# With options
.\brain\ai-brain\scripts\brain.ps1 publish brain\ai-brain\inbox\draft.md --project ghcp-knowledge-sharing --commit
```

The publish workflow:
1. Shows current frontmatter
2. Asks which project bucket
3. Suggests tags (from existing frontmatter + filename keywords)
4. You confirm or adjust
5. Updates frontmatter, moves file to `library/<project>/<YYYY-MM>/`
6. Runs `git add` + `git commit`

### Method 2 — manual placement with frontmatter

Create the file directly in `library/<project>/<YYYY-MM>/` with this frontmatter:

```markdown
---
date: YYYY-MM-DD
kind: ref
project: <project-bucket>
tags: [tag1, tag2, tag3]
status: archived
source: manual | copilot | imported
---

# Title
```

---

## Frontmatter: kind values for library content

| kind | Use for |
|------|---------|
| `ref` | Source reference — imported slide deck, external guide, presenter notes |
| `resource` | Links, reading material, curated resource list |
| `snippet` | Code or command reference imported from external source |

> `note`, `session`, `decision` belong in `notes/` — those represent your own writing.

---

## Search Library

```powershell
.\brain\ai-brain\scripts\brain.ps1 list --tier library
.\brain\ai-brain\scripts\brain.ps1 list --tier library --project ghcp-knowledge-sharing
.\brain\ai-brain\scripts\brain.ps1 search mermaid --tier library
.\brain\ai-brain\scripts\brain.ps1 search --kind ref --tier library
.\brain\ai-brain\scripts\brain.ps1 search --date 2026-03 --tier library
```
