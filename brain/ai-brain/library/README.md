# ai-brain/library/ -- Permanent Reference Library

**Tracked by git. Pushed to the repo. Permanent.**

Content here is the permanent reference library -- formally tagged, project-attributed
notes you want available from any machine, searchable across sessions, and worth
having in the project history. Never old, never obsolete: a library grows and is consulted.

---

## Hierarchy

Notes are organised by **project bucket** then **month**. Created automatically
by `brain publish` -- you never create folders manually.

```
library/
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

Project names are free-form strings you provide at save time.
Common ones: `mcp-servers`, `java`, `learning-assistant`, `general`.

---

## Publishing a Note

```powershell
# Interactive (asks for project, confirms tags, commits)
.\brain\ai-brain\scripts\brain.ps1 publish brain\ai-brain\inbox\draft.md

# With options
.\brain\ai-brain\scripts\brain.ps1 publish brain\ai-brain\inbox\draft.md --project mcp-servers --commit
```

The publish workflow:
1. Shows current frontmatter
2. Asks which project bucket
3. Suggests tags (from existing frontmatter + filename keywords)
4. You confirm or adjust
5. Updates frontmatter, moves file to `library/<project>/<YYYY-MM>/`
6. Runs `git add` + `git commit`

---

## Frontmatter Template

```markdown
---
date: YYYY-MM-DD
kind: note | decision | session | resource | snippet | ref
project: <project-bucket>
tags: [tag1, tag2, tag3]
status: draft | final | archived
source: copilot | manual | mcp
---

# Title
```

---

## Commit Message Format

```
brain: publish <topic-slug> [tag1, tag2, tag3]

Project: <project>  Kind: <kind>  Status: final

-- created by gpt
```

---

## Search Library Notes

```powershell
.\brain\ai-brain\scripts\brain.ps1 list --tier library
.\brain\ai-brain\scripts\brain.ps1 list --tier library --project mcp-servers
.\brain\ai-brain\scripts\brain.ps1 search generics --tier library
.\brain\ai-brain\scripts\brain.ps1 search --kind decision --tier library
.\brain\ai-brain\scripts\brain.ps1 search --date 2026-02 --tier library
```
