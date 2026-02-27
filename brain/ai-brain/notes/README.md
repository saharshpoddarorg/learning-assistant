# ai-brain/notes/ -- Curated Local Notes

**Gitignored. Stays on this machine. Survives sessions.**

Content lives here when it was useful, you may return to it, but it is not
ready or significant enough to commit to the repo yet.

---

## Typical content

- Session notes you want to reread but not necessarily share
- Research in progress -- not yet a clean permanent note
- Drafts that need more work before they are repo-worthy
- Machine-specific notes that should not be pushed

---

## Creating notes here

```powershell
.\brain\ai-brain\scripts\brain.ps1 new --tier notes
.\brain\ai-brain\scripts\brain.ps1 new --tier notes --project mcp-servers --title "Auth design"
```

```bash
./brain/ai-brain/scripts/brain.sh new --tier notes
./brain/ai-brain/scripts/brain.sh new --tier notes --project java --title "Records vs classes"
```

---

## Organise how you think

No required subdirectory structure. Add subdirs only when you feel the need:

```
notes/
  2026-02-21_mcp-servers-session.md    <- flat, date-prefixed
  java/
    generics-questions.md              <- subject subdirectory
  decisions/
    2026-02-22_auth-approach.md
```

---

## When a note is ready for the repo

```powershell
# Publish to archive/ -- prompts for project, tags, then commits
.\brain\ai-brain\scripts\brain.ps1 publish brain\ai-brain\notes\file.md

# Or move to archive/ without prompts (manual git add + commit separately)
.\brain\ai-brain\scripts\promote.ps1 notes\file.md archive --project java
```