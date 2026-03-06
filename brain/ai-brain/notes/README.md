# ai-brain/notes/ -- Your Own Writing

**Git-tracked. Committed to the repo. Permanent.**

Notes live here when **you wrote them** — your distilled insights, your session logs,
your decisions, your how-tos. This is not for imported external content (that goes to `library/`).

## The core distinction

| Tier | Rule |
|---|---|
| `notes/` | **I wrote this** — my synthesis, my session log, my decision |
| `library/` | **I preserved this** — external source I imported |

If you're unsure: *Did you write it from scratch?* → `notes/`. *Did you paste it from somewhere?* → `library/`.

---

## What Belongs Here

- Session logs — what you built/fixed today, decisions made, next steps
- Distilled insights — your synthesis from reading, learning, sessions
- Architecture decisions (ADR format) — your reasoning for choices made
- How-to notes — your own cheatsheets, workflows you authored
- Concept notes — explanations you wrote for future-you

## What Does NOT Belong Here

- Raw mid-session reasoning → `inbox/`
- Half-finished drafts → `inbox/` until ready
- External slide decks or imported documents → `library/`
- Machine-specific configs or secrets → never commit

---

## Current Notes

| File | Kind | Topic | Date |
|---|---|---|---|
| [`2026-02-21_session-mcp-server-fixes-and-restructure.md`](2026-02-21_session-mcp-server-fixes-and-restructure.md) | session | MCP server bug fix, config restructure, output/ hierarchy | 2026-02-21 |
| [`2026-03-06_ghcp-knowledge-sharing-distilled.md`](2026-03-06_ghcp-knowledge-sharing-distilled.md) | note | GHCP customization KS session — 3-tier distilled insights | 2026-03-06 |

---

## Creating Notes Here

```powershell
.\brain\ai-brain\scripts\brain.ps1 new --tier notes
.\brain\ai-brain\scripts\brain.ps1 new --tier notes --project mcp-servers --title "Auth design"
```

```bash
./brain/ai-brain/scripts/brain.sh new --tier notes
./brain/ai-brain/scripts/brain.sh new --tier notes --project java --title "Records vs classes"
```

---

## Naming Convention

```
YYYY-MM-DD_<kebab-case-descriptive-slug>.md

Examples:
  2026-02-21_session-mcp-server-fixes-and-restructure.md  ← session log
  2026-03-06_ghcp-knowledge-sharing-distilled.md          ← distilled insights
  2026-04-15_java-records-vs-classes.md                   ← concept note
  2026-04-22_decision-hashmap-over-treemap.md             ← decision ADR
```

- Date prefix enables chronological sorting
- Include `session-`, `decision-`, `concept-` prefixes where helpful for scanning
- All lowercase, hyphens only

---

## Moving a Note to Library

If you realise a note is actually imported source material (not your own writing), move it:

```powershell
# Move to library with prompts (publish workflow — project, tags, commit)
.\brain\ai-brain\scripts\brain.ps1 publish brain\ai-brain\notes\file.md

# Or move without prompts (manual git add + commit separately)
.\brain\ai-brain\scripts\promote.ps1 notes\file.md library --project java
```
