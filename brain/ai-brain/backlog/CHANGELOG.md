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
| 2026-03 | 7 | 21 | 1 | 0 |

### Cumulative

```text
Total items created:     21
Total items completed:    1
Completion rate:          5%
Active sprints:           0
Completed sprints:        0
```

---

> **Last updated:** 2026-03-31
