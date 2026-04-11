# Backlog Activity Log

> Append-only audit trail of all backlog changes — status transitions, priority changes,
> item creation, and sprint events. Like a JIRA activity stream for your personal board.
>
> **Board:** [BOARD.md](BOARD.md) | **Views:** [By Status](views/by-status.md) |
> [By Project](views/by-project.md) | [By Priority](views/by-priority.md)

---

## Log Format

Each entry records: **when** (date + time), **what** (item ID), **action** (verb),
and **details** (context). Entries are newest-first within each month.

---

## 2026-04

| Date | Time | ID | Action | From | To | Details |
|---|---|---|---|---|---|---|
| 2026-04-11 | 10:08 PM | IMP-001 | file-import | — | — | Imported `gpt.txt`: 7 BLIs created, 1 IDEA, 4 merged, 2 noted |
| 2026-04-11 | 10:08 PM | BLI-019 | created | — | todo | Tally-Vyapaar accounting app (IMP-001) |
| 2026-04-11 | 10:08 PM | BLI-020 | created | — | todo | Build GitHub MCP server (IMP-001) |
| 2026-04-11 | 10:08 PM | BLI-021 | created | — | todo | Reusable content templates system (IMP-001) |
| 2026-04-11 | 10:08 PM | BLI-022 | created | — | todo | Export/migration support for portability (IMP-001) |
| 2026-04-11 | 10:08 PM | BLI-023 | created | — | todo | Cross-repo item merge and sync (IMP-001) |
| 2026-04-11 | 10:08 PM | BLI-024 | created | — | todo | Workflow and terminal conditions framework (IMP-001) |
| 2026-04-11 | 10:08 PM | BLI-025 | created | — | todo | Library/glossary for abbreviations and aliases (IMP-001) |
| 2026-04-11 | 10:08 PM | IDEA-001 | created | — | raw | Standardize purpose headers in prompts/skills (IMP-001) |
| 2026-04-11 | 10:08 PM | BLI-007 | merged | — | — | IMP-001: added Google Keep, phone/Ike, browser tabs, office laptop ACs |
| 2026-04-11 | 10:08 PM | BLI-008 | merged | — | — | IMP-001: added kanban/scrum boards, reminder ACs |
| 2026-04-11 | 10:08 PM | BLI-010 | merged | — | — | IMP-001: added Atlassian-like products context |
| 2026-04-11 | 10:08 PM | BLI-012 | merged | — | — | IMP-001: added cross-linking, labelling, enums ACs |
| 2026-04-11 | 10:08 PM | BLI-001 | noted | — | — | IMP-001: "atlassian" mentioned — already covered |
| 2026-04-11 | 10:08 PM | BLI-014 | noted | — | — | IMP-001: "media manager" mentioned — already covered |

---

## 2026-03

| Date | Time | ID | Action | From | To | Details |
|---|---|---|---|---|---|---|
| 2026-03-31 | — | — | system | — | — | Enhanced backlog with views/, sprints/, CHANGELOG, time tracking |
| 2026-03-28 | — | EPIC-003 | created | — | draft | Personal software development projects |
| 2026-03-28 | — | EPIC-002 | created | — | draft | Knowledge consolidation & cross-platform brain |
| 2026-03-28 | — | EPIC-001 | created | — | draft | Full-fledged learning resources system |
| 2026-03-28 | — | BLI-014–018 | created | — | todo | 5 personal software project items (EPIC-003) |
| 2026-03-27 | — | BLI-006 | status | todo | done | Code formatting instructions and skill completed |
| 2026-03-26 | — | BLI-001–013 | created | — | todo | Initial backlog — 13 items across features/projects/items |

---

## Legend

| Action | Meaning |
|---|---|
| **created** | New item, idea, epic, or guide added |
| **status** | Status transition (from → to) |
| **priority** | Priority change (from → to) |
| **assigned** | Item assigned to a sprint |
| **unassigned** | Item removed from a sprint |
| **promoted** | Idea promoted to backlog item |
| **blocked** | Item blocked — blocker described in details |
| **unblocked** | Blocker resolved |
| **archived** | Item moved to archived status |
| **sprint-start** | Sprint started |
| **sprint-end** | Sprint completed |
| **moved** | Item moved between folders |
| **system** | Structural/system change to the backlog itself |

---

## Stats

### Monthly Activity

| Month | Changes | Items Created | Items Completed | Sprint Completions |
|---|---|---|---|---|
| 2026-04 | 15 | 8 | 0 | 0 |
| 2026-03 | 7 | 21 | 1 | 0 |

### Cumulative

```text
Total items created:     29
Total items completed:    1
Completion rate:          3%
Active sprints:           0
Completed sprints:        0
```

---

> **Last updated:** 2026-04-11
