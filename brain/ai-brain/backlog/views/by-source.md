# By Source — Items Grouped by Origin

> **Purpose:** View backlog items grouped by how they were created — manual capture
> vs file import. File-import items are further grouped by import batch (IMP-NNN).
>
> **Updated by:** `/jot`, `/read-file-jot`, `/todos` (on status changes)

---

## Manual Capture (`/jot`, `/todo`)

Items created through direct chat input (origin-type: manual).

| ID | Title | Status | Priority | Created |
|---|---|---|---|---|
| BLI-027 | Build code analysis context enhancement chain | todo | high | 2026-04-21 |

---

## AI Session Planning

Items created through AI-assisted backlog planning sessions (epics, audits, inventories).

### EPIC-004 — Stale Reference Cleanup (2026-05-15)

> **Source:** AI session — features inventory audit + Gradle migration gap analysis
> **Extracted:** 13 BLIs created (BLI-028–040)

| ID | Title | Status | Priority | Created |
|---|---|---|---|---|
| BLI-028 | Fix copilot-instructions.md build description and stale paths | todo | critical | 2026-05-15 |
| BLI-029 | Fix stale paths in mcp-server-setup.md | todo | critical | 2026-05-15 |
| BLI-030 | Fix stale paths in configuration-reference.md | todo | critical | 2026-05-15 |
| BLI-031 | Fix stale path in mcp-to-skill.prompt.md | todo | critical | 2026-05-15 |
| BLI-032 | Fix stale paths in search-engine docs | todo | high | 2026-05-15 |
| BLI-033 | Fix stale paths in USAGE.md | todo | high | 2026-05-15 |
| BLI-034 | Fix brain model path references | todo | high | 2026-05-15 |
| BLI-035 | Fix stale paths in versioning, mcp-vs-skills, phase-guide | todo | high | 2026-05-15 |
| BLI-036 | Fix stale paths in slash-commands, nav-index, evolution-guide | todo | high | 2026-05-15 |
| BLI-037 | Fix stale paths in EPIC-001 and EPIC-002 notes | todo | medium | 2026-05-15 |
| BLI-038 | Fix stale paths in MCP-README.md and SETUP.md | todo | medium | 2026-05-15 |
| BLI-039 | Add module-specific README.md to each modules directory | todo | low | 2026-05-15 |
| BLI-040 | Consolidate configuration documentation | todo | low | 2026-05-15 |

### EPIC-005 — Full Repo Revamp (2026-05-15)

> **Source:** AI session — features inventory analysis + modularization planning
> **Extracted:** 34 BLIs created (BLI-041–074)

| ID | Title | Status | Priority | Created |
|---|---|---|---|---|
| BLI-041 | Audit prompt frontmatter consistency | todo | high | 2026-05-15 |
| BLI-042 | Extract inline domain knowledge from prompts into skills | todo | high | 2026-05-15 |
| BLI-043 | Standardize prompt composition patterns | todo | high | 2026-05-15 |
| BLI-044 | Refactor meta prompts for clarity | todo | medium | 2026-05-15 |
| BLI-045 | Consolidate brain prompts | todo | medium | 2026-05-15 |
| BLI-046 | Standardize prompt input variables | todo | medium | 2026-05-15 |
| BLI-047 | Create prompt composition map document | todo | low | 2026-05-15 |
| BLI-048 | Audit skill activation keyword overlaps | todo | high | 2026-05-15 |
| BLI-049 | Standardize skill file structure | todo | high | 2026-05-15 |
| BLI-050 | Split oversized skills into sub-files | todo | medium | 2026-05-15 |
| BLI-051 | Extract prompt-embedded knowledge into new skills | todo | medium | 2026-05-15 |
| BLI-052 | Verify skill ↔ prompt ↔ agent cross-references | todo | medium | 2026-05-15 |
| BLI-053 | Create skill activation map document | todo | low | 2026-05-15 |
| BLI-054 | Standardize agent template and structure | todo | high | 2026-05-15 |
| BLI-055 | Document complete agent handoff graph | todo | high | 2026-05-15 |
| BLI-056 | Review and normalize agent tool restrictions | todo | medium | 2026-05-15 |
| BLI-057 | Create agent selection guide | todo | low | 2026-05-15 |
| BLI-058 | Deprecate/remove Atlassian v1 server | todo | high | 2026-05-15 |
| BLI-059 | Unify MCP server configuration pattern | todo | high | 2026-05-15 |
| BLI-060 | Create skill wrappers for all MCP tool categories | todo | medium | 2026-05-15 |
| BLI-061 | Auto-generate MCP tool documentation from Java source | todo | medium | 2026-05-15 |
| BLI-062 | Add MCP server health check and status tasks | todo | low | 2026-05-15 |
| BLI-063 | Measure instruction context overhead | todo | high | 2026-05-15 |
| BLI-064 | Split chat-capture into core + reference | todo | medium | 2026-05-15 |
| BLI-065 | Split change-completeness into core + checklists | todo | medium | 2026-05-15 |
| BLI-066 | Resolve instruction overlaps | todo | medium | 2026-05-15 |
| BLI-067 | Audit brain PKM policy files for overlap | todo | medium | 2026-05-15 |
| BLI-068 | Consolidate brain scripts | todo | medium | 2026-05-15 |
| BLI-069 | Create brain workspace export template | todo | low | 2026-05-15 |
| BLI-070 | Document brain tier boundaries | todo | low | 2026-05-15 |
| BLI-071 | Review session escalation protocol complexity | todo | low | 2026-05-15 |
| BLI-072 | Audit docs for content duplication | todo | medium | 2026-05-15 |
| BLI-073 | Verify all cross-links and navigation | todo | medium | 2026-05-15 |
| BLI-074 | Create documentation hierarchy diagram | todo | low | 2026-05-15 |

---

## File Imports (`/read-file-jot`)

Items extracted from external files, grouped by import batch.

### IMP-001 — gpt.txt (2026-04-11)

> **Source:** `D:\workdir\MG_FTE\notepad\personal dev\learning-assistant\gpt.txt`
> **Extracted:** 7 BLIs + 1 IDEA created, 4 merged, 2 noted

#### New Items Created

| ID | Title | Status | Priority | Created |
|---|---|---|---|---|
| BLI-019 | Tally-Vyapaar accounting application | todo | low | 2026-04-11 |
| BLI-020 | Build GitHub MCP server | todo | high | 2026-04-11 |
| BLI-021 | Reusable content templates system | todo | medium | 2026-04-11 |
| BLI-022 | Export and migration support for portability | todo | medium | 2026-04-11 |
| BLI-023 | Cross-repo item merge and sync | todo | medium | 2026-04-11 |
| BLI-024 | Workflow and terminal conditions framework | todo | medium | 2026-04-11 |
| BLI-025 | Library/glossary for abbreviations and aliases | todo | low | 2026-04-11 |
| IDEA-001 | Standardize purpose headers in prompts and skills | raw | — | 2026-04-11 |

#### Merged Into Existing Items

| ID | Title | What Was Merged |
|---|---|---|
| BLI-007 | Cross-platform note integration mechanism | Google Keep, phone (Ike), browser tabs, office laptop |
| BLI-008 | Scheduler, calendar, timeline & todo tracking | Kanban/scrum boards, reminder as digital note-taking |
| BLI-010 | Personal Confluence — migrate office resources | Atlassian-like products (Jira/Confluence clones) |
| BLI-012 | Enhanced web scraping & search engine | Cross-linking, labelling, enums, skills/prompts |

#### Near-Duplicates Noted (not skipped)

| ID | Title | Source Mention |
|---|---|---|
| BLI-001 | Build production Atlassian MCP server | "atlassian" under mcp-servers |
| BLI-014 | Media manager — YouTube, Spotify consolidation | "media manager" under projects |

---

## Summary

```text
Manual items:     1
AI session items: 47 items across 2 epics (EPIC-004: 13 + EPIC-005: 34)
File imports:     14 items across 1 batch (8 new + 4 merged + 2 noted)
Total:            62
```
