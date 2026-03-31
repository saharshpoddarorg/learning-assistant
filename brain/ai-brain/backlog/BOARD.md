# Backlog Board

> Personal agile board — kanban + scrum-style tracking for all work items, ideas, and projects.
>
> **Quick commands:** `/jot` (capture thought) | `/todo` (add task) | `/todos` (this board)
>
> **Views:** [By Status](views/by-status.md) | [By Project](views/by-project.md) |
> [By Priority](views/by-priority.md) | [Activity Log](CHANGELOG.md)

---

## Dashboard

```text
┌─────────────────────────────────────────────────────────────────────────┐
│  BACKLOG DASHBOARD                                        2026-03-31   │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│  Items     ██████████████████████████████████░░░░░░░░  18 total         │
│  todo      ████████████████████████████████░░░░░░░░░░  17               │
│  progress  ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░   0               │
│  blocked   ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░   0               │
│  done      ██░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░   1               │
│                                                                         │
│  Priority  critical: 0 | high: 7 | medium: 6 | low: 5                  │
│  Epics     3 draft | 0 active | 0 done                                 │
│  Ideas     0 raw | 0 refining | 0 promoted                             │
│  Sprints   0 active | 0 completed                                       │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

### Summary

| Type | Count | Folder |
|---|---|---|
| Features | 11 | `features/` — learning-assistant enhancements |
| Projects | 6 | `projects/` — standalone personal software projects |
| Items | 1 | `items/` — general (BLI-006, done) |
| Ideas | 0 | `ideas/` |
| Epics | 3 | `epics/` — EPIC-001, EPIC-002, EPIC-003 |
| Guides | 0 | `guides/` |
| Sprints | 0 | `sprints/` |
| **Total** | **21** | |

---

## Active Sprint

> **No active sprint.** Use `/backlog sprint start "sprint goal"` to begin one.
>
> See [sprints/](sprints/) for sprint history.

---

## Kanban Board

```text
┌──────────────┬──────────────┬──────────────┬──────────────┬──────────────┐
│   BACKLOG    │  IN PROGRESS │   BLOCKED    │   IN REVIEW  │     DONE     │
│   (17)       │   (0)        │   (0)        │   (0)        │     (1)      │
├──────────────┼──────────────┼──────────────┼──────────────┼──────────────┤
│ BLI-001 high │              │              │              │ BLI-006 med  │
│ BLI-002 high │              │              │              │              │
│ BLI-003 high │              │              │              │              │
│ BLI-007 high │              │              │              │              │
│ BLI-008 high │              │              │              │              │
│ BLI-011 high │              │              │              │              │
│ BLI-012 high │              │              │              │              │
│ BLI-004 med  │              │              │              │              │
│ BLI-005 med  │              │              │              │              │
│ BLI-009 med  │              │              │              │              │
│ BLI-010 med  │              │              │              │              │
│ BLI-013 med  │              │              │              │              │
│ BLI-014 low  │              │              │              │              │
│ BLI-015 low  │              │              │              │              │
│ BLI-016 low  │              │              │              │              │
│ BLI-017 low  │              │              │              │              │
│ BLI-018 low  │              │              │              │              │
└──────────────┴──────────────┴──────────────┴──────────────┴──────────────┘
```

---

## Epics

### EPIC-001: Full-fledged learning resources system

> **Status:** draft | **Priority:** high | **Progress:** 0/4 (0%)
>
> **Folder:** `features/`

| ID | Title | Priority | Status | Started | Completed |
|---|---|---|---|---|---|
| BLI-002 | Hybrid skills + MCP server + 5 primitives combo | high | todo | — | — |
| BLI-003 | Module hierarchies for learning resources | high | todo | — | — |
| BLI-004 | Brain integration with learning resources | medium | todo | — | — |
| BLI-005 | Chat session orchestration — scoped sessions, agents | medium | todo | — | — |

### EPIC-002: Knowledge consolidation & cross-platform brain

> **Status:** draft | **Priority:** high | **Progress:** 0/6 (0%)
>
> **Folder:** `features/`

| ID | Title | Priority | Status | Started | Completed |
|---|---|---|---|---|---|
| BLI-007 | Cross-platform note integration mechanism | high | todo | — | — |
| BLI-009 | Third-party libs, JDK 26, SDK tools & annotations | medium | todo | — | — |
| BLI-010 | Personal Confluence — migrate office resources | medium | todo | — | — |
| BLI-011 | Brain consolidation — tabs, bookmarks, workspace, notes | high | todo | — | — |
| BLI-012 | Enhanced web scraping & search engine | high | todo | — | — |
| BLI-013 | Book consolidation — PDFs, ebooks, cloud readers | medium | todo | — | — |

### EPIC-003: Personal software development projects

> **Status:** draft | **Priority:** medium | **Progress:** 0/6 (0%)
>
> **Folder:** `projects/` | **View:** [Project Board](views/by-project.md)

| ID | Title | Priority | Status | Started | Completed |
|---|---|---|---|---|---|
| BLI-008 | Scheduler, calendar, timeline & todo tracking | high | todo | — | — |
| BLI-014 | Media manager — YouTube, Spotify consolidation | low | todo | — | — |
| BLI-015 | Family tree application | low | todo | — | — |
| BLI-016 | Cloud & ebook reader | low | todo | — | — |
| BLI-017 | Developer productivity dashboard | low | todo | — | — |
| BLI-018 | Code snippet & template manager | low | todo | — | — |

---

## Standalone Items

| ID | Title | Type | Priority | Status | Created | Started | Completed |
|---|---|---|---|---|---|---|---|
| BLI-001 | Build production Atlassian MCP server | feature | high | todo | 2026-03-26 | — | — |
| BLI-006 | Create code formatting instructions and skill | feature | medium | done | 2026-03-26 | 2026-03-27 | 2026-03-27 |

---

## In Progress

| ID | Title | Type | Priority | Epic | Started | Elapsed |
|---|---|---|---|---|---|---|

> No items in progress. Use `start BLI-NNN` to begin work.

---

## Blocked

| ID | Title | Priority | Blocked Since | Blocker |
|---|---|---|---|---|

> No blocked items.

---

## In Review

| ID | Title | Priority | Review Since | Reviewer Notes |
|---|---|---|---|---|

> No items in review.

---

## Done (Recent)

> Last 10 completed items. Full history: [By Status → Done](views/by-status.md#done)

| ID | Title | Type | Priority | Started | Completed | Cycle Time |
|---|---|---|---|---|---|---|
| BLI-006 | Create code formatting instructions and skill | feature | medium | 2026-03-27 | 2026-03-27 | < 1 day |

---

## Ideas

| ID | Title | Status | Created | Tags |
|---|---|---|---|---|

> No ideas captured yet. Use `/jot "your thought"` to add one.

---

## Guides

| ID | Title | Status | Scope | Created |
|---|---|---|---|---|

> No guides created yet. Use `/backlog guide "topic"` to create one.

---

## Recent Activity

> Last 5 changes. Full log: [CHANGELOG.md](CHANGELOG.md)

| Date | Time | ID | Action | Details |
|---|---|---|---|---|
| 2026-03-28 | — | EPIC-003 | created | Personal software development projects |
| 2026-03-28 | — | EPIC-002 | created | Knowledge consolidation & cross-platform brain |
| 2026-03-28 | — | EPIC-001 | created | Full-fledged learning resources system |
| 2026-03-27 | — | BLI-006 | done | Code formatting instructions and skill completed |
| 2026-03-26 | — | BLI-001–018 | created | Initial backlog items created |

---

## Quick Navigation

| View | Description | Link |
|---|---|---|
| By Status | All items grouped by status (todo/progress/blocked/done) | [views/by-status.md](views/by-status.md) |
| By Project | Per-project mini-boards for personal s/w dev projects | [views/by-project.md](views/by-project.md) |
| By Priority | All items ranked by priority tier | [views/by-priority.md](views/by-priority.md) |
| Activity Log | Full audit trail of all status changes | [CHANGELOG.md](CHANGELOG.md) |
| Sprint History | Past and current sprint tracking | [sprints/](sprints/) |

---

> **Last updated:** 2026-03-31
