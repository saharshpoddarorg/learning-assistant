# ai-brain/notes/ -- Curated Knowledge Notes

**Git-tracked. Committed to the repo. Permanent developer knowledge.**

Content lives here when it is reviewed, refined, and worth sharing — complete enough
to stand on its own as a reference, but not yet formally published with full tags
and project attribution (that's `library/`).

---

## What Belongs Here

- Session notes from learning or dev work sessions (reviewed, not raw)
- Distilled insights from multiple sources
- In-progress research that has solidified into stable notes
- Conceptual cheatsheets and how-to references
- Notes you'd want accessible from another machine or to share with a teammate

## What Does NOT Belong Here

- Raw mid-session reasoning → use `inbox/`
- Half-finished drafts → use `inbox/` until ready
- Machine-specific configs or secrets → never commit

---

## Current Notes

| File | Topic | Date |
|---|---|---|
| [`2026-02-21_session-mcp-server-fixes-and-restructure.md`](2026-02-21_session-mcp-server-fixes-and-restructure.md) | MCP server bug fix, config restructure, output/ hierarchy | 2026-02-21 |
| [`2026-03-06_ghcp-knowledge-sharing-distilled.md`](2026-03-06_ghcp-knowledge-sharing-distilled.md) | GHCP customization KS session — 3-tier distilled insights | 2026-03-06 |

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
  2026-02-21_session-mcp-server-fixes-and-restructure.md   ← session notes
  2026-03-06_ghcp-knowledge-sharing-distilled.md           ← distilled KS insights
  2026-04-15_java-records-vs-classes.md                    ← concept notes
  2026-04-22_system-design-url-shortener.md                ← design notes
```

- Date prefix enables chronological sorting and cross-referencing
- Slug is descriptive — reader knows the topic without opening the file
- All lowercase, hyphens, no spaces

---

## When a Note Is Ready for Archive

```powershell
# Publish to library/ -- prompts for project, tags, then commits
.\brain\ai-brain\scripts\brain.ps1 publish brain\ai-brain\notes\file.md

# Or move to library/ without prompts (manual git add + commit separately)
.\brain\ai-brain\scripts\promote.ps1 notes\file.md library --project java
```

The difference between notes/ and library/:
- **notes/** — committed, searchable, good for reference; informal frontmatter
- **library/** — formal YAML frontmatter (kind, project, tags, status, source); appears in `brain list --tier library`