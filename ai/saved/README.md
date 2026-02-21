# ai/saved/ -- Committed Reference

**Tracked by git. Pushed to the repo. Permanent.**

Content here is a real record -- something you would want available from
another machine, or something worth having in the project history.

---

## Hierarchy

Notes are organised by **project bucket** then **month**. Created automatically
by `ai save` -- you never create folders manually.

```
saved/
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

## Saving a Note

```powershell
# Interactive (asks for project, confirms tags, commits)
.\ai\scripts\ai.ps1 save ai\scratch\draft.md

# With options
.\ai\scripts\ai.ps1 save ai\scratch\draft.md --project mcp-servers --commit
```

The save workflow:
1. Shows current frontmatter
2. Asks which project bucket
3. Suggests tags (from existing frontmatter + filename keywords)
4. You confirm or adjust
5. Updates frontmatter, moves file to `saved/<project>/<YYYY-MM>/`
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
Save: <topic-slug> [tag1, tag2, tag3]

Project: <project>  Kind: <kind>  Status: final

-- created by gpt
```

---

## Search Saved Notes

```powershell
.\ai\scripts\ai.ps1 list --tier saved
.\ai\scripts\ai.ps1 list --tier saved --project mcp-servers
.\ai\scripts\ai.ps1 search generics --tier saved
.\ai\scripts\ai.ps1 search --kind decision --tier saved
.\ai\scripts\ai.ps1 search --date 2026-02 --tier saved
```
