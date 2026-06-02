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
│  BACKLOG DASHBOARD                                        2026-06-02   │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│  Items     ██████████████████████████████████████░░░░  85 total         │
│  todo      ████████████████████████████████████░░░░░░  83               │
│  progress  ██░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░   1               │
│  blocked   ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░   0               │
│  done      ██░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░   1               │
│                                                                         │
│  Priority  critical: 4 | high: 22 | medium: 33 | low: 20               │
│  Epics     3 draft | 2 active | 1 draft (EPIC-005) | 0 done            │
│  Ideas     1 raw | 0 refining | 0 promoted                             │
│  Sprints   0 active | 0 completed                                       │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

### Summary

| Type | Count | Folder |
|---|---|---|
| Features | 78 | `features/` — learning-assistant enhancements (33 existing + 34 EPIC-005 + 11 EPIC-006) |
| Projects | 7 | `projects/` — standalone personal software projects |
| Items | 1 | `items/` — general (BLI-006, done) |
| Ideas | 1 | `ideas/` |
| Epics | 6 | `epics/` — EPIC-001 through EPIC-006 |
| Guides | 0 | `guides/` |
| Sprints | 0 | `sprints/` |
| **Total** | **93** | |

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
│   (84)       │   (1)        │   (0)        │   (0)        │     (1)      │
├──────────────┼──────────────┼──────────────┼──────────────┼──────────────┤
│ BLI-028 crit │ BLI-026 high │              │              │ BLI-006 med  │
│ BLI-029 crit │              │              │              │              │
│ BLI-030 crit │              │              │              │              │
│ BLI-031 crit │              │              │              │              │
│ BLI-001 high │              │              │              │              │
│ BLI-002 high │              │              │              │              │
│ BLI-003 high │              │              │              │              │
│ BLI-007 high │              │              │              │              │
│ BLI-008 high │              │              │              │              │
│ BLI-011 high │              │              │              │              │
│ BLI-012 high │              │              │              │              │
│ BLI-020 high │              │              │              │              │
│ BLI-027 high │              │              │              │              │
│ BLI-032 high │              │              │              │              │
│ BLI-033 high │              │              │              │              │
│ BLI-034 high │              │              │              │              │
│ BLI-035 high │              │              │              │              │
│ BLI-036 high │              │              │              │              │
│ BLI-004 med  │              │              │              │              │
│ BLI-005 med  │              │              │              │              │
│ BLI-009 med  │              │              │              │              │
│ BLI-010 med  │              │              │              │              │
│ BLI-013 med  │              │              │              │              │
│ BLI-021 med  │              │              │              │              │
│ BLI-022 med  │              │              │              │              │
│ BLI-023 med  │              │              │              │              │
│ BLI-024 med  │              │              │              │              │
│ BLI-037 med  │              │              │              │              │
│ BLI-038 med  │              │              │              │              │
│ BLI-014 low  │              │              │              │              │
│ BLI-015 low  │              │              │              │              │
│ BLI-016 low  │              │              │              │              │
│ BLI-017 low  │              │              │              │              │
│ BLI-018 low  │              │              │              │              │
│ BLI-019 low  │              │              │              │              │
│ BLI-025 low  │              │              │              │              │
│ BLI-039 low  │              │              │              │              │
│ BLI-040 low  │              │              │              │              │
│ BLI-041 high │              │              │              │              │
│ BLI-042 high │              │              │              │              │
│ BLI-043 high │              │              │              │              │
│ BLI-044 med  │              │              │              │              │
│ BLI-045 med  │              │              │              │              │
│ BLI-046 med  │              │              │              │              │
│ BLI-047 low  │              │              │              │              │
│ BLI-048 high │              │              │              │              │
│ BLI-049 high │              │              │              │              │
│ BLI-050 med  │              │              │              │              │
│ BLI-051 med  │              │              │              │              │
│ BLI-052 med  │              │              │              │              │
│ BLI-053 low  │              │              │              │              │
│ BLI-054 high │              │              │              │              │
│ BLI-055 high │              │              │              │              │
│ BLI-056 med  │              │              │              │              │
│ BLI-057 low  │              │              │              │              │
│ BLI-058 high │              │              │              │              │
│ BLI-059 high │              │              │              │              │
│ BLI-060 med  │              │              │              │              │
│ BLI-061 med  │              │              │              │              │
│ BLI-062 low  │              │              │              │              │
│ BLI-063 high │              │              │              │              │
│ BLI-064 med  │              │              │              │              │
│ BLI-065 med  │              │              │              │              │
│ BLI-066 med  │              │              │              │              │
│ BLI-067 med  │              │              │              │              │
│ BLI-068 med  │              │              │              │              │
│ BLI-069 low  │              │              │              │              │
│ BLI-070 low  │              │              │              │              │
│ BLI-071 low  │              │              │              │              │
│ BLI-072 med  │              │              │              │              │
│ BLI-073 med  │              │              │              │              │
│ BLI-074 low  │              │              │              │              │
│ BLI-075 high │              │              │              │              │
│ BLI-076 high │              │              │              │              │
│ BLI-077 high │              │              │              │              │
│ BLI-078 med  │              │              │              │              │
│ BLI-079 med  │              │              │              │              │
│ BLI-080 med  │              │              │              │              │
│ BLI-081 med  │              │              │              │              │
│ BLI-082 high │              │              │              │              │
│ BLI-083 med  │              │              │              │              │
│ BLI-084 med  │              │              │              │              │
│ BLI-085 med  │              │              │              │              │
│ BLI-086 med  │              │              │              │              │
└──────────────┴──────────────┴──────────────┴──────────────┴──────────────┘
```

---

## Epics

### EPIC-001: Full-fledged learning resources system

> **Status:** draft | **Priority:** high | **Progress:** 0/6 (0%)
>
> **Folder:** `features/`

| ID | Title | Priority | Status | Started | Completed |
|---|---|---|---|---|---|
| BLI-002 | Hybrid skills + MCP server + 5 primitives combo | high | todo | — | — |
| BLI-003 | Module hierarchies for learning resources | high | todo | — | — |
| BLI-004 | Brain integration with learning resources | medium | todo | — | — |
| BLI-005 | Chat session orchestration — scoped sessions, agents | medium | todo | — | — |
| BLI-024 | Workflow and terminal conditions framework | medium | todo | — | — |
| BLI-025 | Library/glossary for abbreviations and aliases | low | todo | — | — |
| BLI-026 | Evolve 6-primitive layered architecture | high | in-progress | 2026-04-21 | — |

### EPIC-002: Knowledge consolidation & cross-platform brain

> **Status:** draft | **Priority:** high | **Progress:** 0/9 (0%)
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
| BLI-021 | Reusable content templates system | medium | todo | — | — |
| BLI-022 | Export and migration support for portability | medium | todo | — | — |
| BLI-023 | Cross-repo item merge and sync | medium | todo | — | — |

### EPIC-003: Personal software development projects

> **Status:** draft | **Priority:** medium | **Progress:** 0/7 (0%)
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
| BLI-019 | Tally-Vyapaar accounting application | low | todo | — | — |

### EPIC-004: Repo modularization and stale reference cleanup

> **Status:** active | **Priority:** high | **Progress:** 0/13 (0%)
>
> **Folder:** `features/`

| ID | Title | Priority | Status | Started | Completed |
|---|---|---|---|---|---|
| BLI-028 | Fix copilot-instructions.md build description and stale paths | critical | todo | — | — |
| BLI-029 | Fix stale paths in mcp-server-setup.md | critical | todo | — | — |
| BLI-030 | Fix stale paths in configuration-reference.md | critical | todo | — | — |
| BLI-031 | Fix stale path in mcp-to-skill.prompt.md | critical | todo | — | — |
| BLI-032 | Fix stale paths in search-engine docs | high | todo | — | — |
| BLI-033 | Fix stale paths in USAGE.md | high | todo | — | — |
| BLI-034 | Fix brain model path references | high | todo | — | — |
| BLI-035 | Fix stale paths in versioning, mcp-vs-skills, phase-guide | high | todo | — | — |
| BLI-036 | Fix stale paths in slash-commands, nav-index, evolution-guide | high | todo | — | — |
| BLI-037 | Fix stale paths in EPIC-001 and EPIC-002 notes | medium | todo | — | — |
| BLI-038 | Fix stale paths in MCP-README.md and SETUP.md | medium | todo | — | — |
| BLI-039 | Add module-specific README.md to each modules directory | low | todo | — | — |
| BLI-040 | Consolidate configuration documentation | low | todo | — | — |

### EPIC-006: Iterative feature brainstorming — learning-assistant

> **Status:** active | **Priority:** medium | **Progress:** 0/11 (0%)
>
> **Folder:** `features/` | **Type:** Open-ended ideation — items added iteratively

| ID | Title | Priority | Status | Started | Completed |
|---|---|---|---|---|---|
| BLI-075 | Search engine — online and local knowledge search | high | todo | — | — |
| BLI-076 | Web scraper — intelligent content extraction and filtering | high | todo | — | — |
| BLI-077 | PKM/brain/notes manager — full knowledge lifecycle | high | todo | — | — |
| BLI-078 | Question refinement — smarter query formulation for GHCP | medium | todo | — | — |
| BLI-079 | Presentation of information — structured and distributable output | medium | todo | — | — |
| BLI-080 | Search engine — labelling system for structured classification | medium | todo | — | — |
| BLI-081 | Search engine — tagging system for flexible categorization | medium | todo | — | — |
| BLI-082 | Search engine — full-text indexing engine for local knowledge | high | todo | — | — |
| BLI-083 | Search engine — metadata index and faceted search | medium | todo | — | — |
| BLI-084 | Search engine — auto-classification and entity extraction | medium | todo | — | — |
| BLI-086 | Resume & LinkedIn skill — auto-update from work history | medium | todo | — | — |

### EPIC-005: Full repo revamp — modularize prompts, skills, agents, MCP, brain

> **Status:** draft | **Priority:** high | **Progress:** 0/34 (0%)
>
> **Folder:** `features/` | **Depends on:** EPIC-004

#### Phase 1 — Prompts (7 items)

| ID | Title | Priority | Status | Started | Completed |
|---|---|---|---|---|---|
| BLI-041 | Audit prompt frontmatter consistency | high | todo | — | — |
| BLI-042 | Extract inline domain knowledge from prompts into skills | high | todo | — | — |
| BLI-043 | Standardize prompt composition patterns | high | todo | — | — |
| BLI-044 | Refactor meta prompts for clarity | medium | todo | — | — |
| BLI-045 | Consolidate brain prompts | medium | todo | — | — |
| BLI-046 | Standardize prompt input variables | medium | todo | — | — |
| BLI-047 | Create prompt composition map document | low | todo | — | — |

#### Phase 2 — Skills (6 items)

| ID | Title | Priority | Status | Started | Completed |
|---|---|---|---|---|---|
| BLI-048 | Audit skill activation keyword overlaps | high | todo | — | — |
| BLI-049 | Standardize skill file structure | high | todo | — | — |
| BLI-050 | Split oversized skills into sub-files | medium | todo | — | — |
| BLI-051 | Extract prompt-embedded knowledge into new skills | medium | todo | — | — |
| BLI-052 | Verify skill ↔ prompt ↔ agent cross-references | medium | todo | — | — |
| BLI-053 | Create skill activation map document | low | todo | — | — |

#### Phase 3 — Agents (4 items)

| ID | Title | Priority | Status | Started | Completed |
|---|---|---|---|---|---|
| BLI-054 | Standardize agent template and structure | high | todo | — | — |
| BLI-055 | Document complete agent handoff graph | high | todo | — | — |
| BLI-056 | Review and normalize agent tool restrictions | medium | todo | — | — |
| BLI-057 | Create agent selection guide | low | todo | — | — |

#### Phase 4 — MCP Servers (5 items)

| ID | Title | Priority | Status | Started | Completed |
|---|---|---|---|---|---|
| BLI-058 | Deprecate/remove Atlassian v1 server | high | todo | — | — |
| BLI-059 | Unify MCP server configuration pattern | high | todo | — | — |
| BLI-060 | Create skill wrappers for all MCP tool categories | medium | todo | — | — |
| BLI-061 | Auto-generate MCP tool documentation from Java source | medium | todo | — | — |
| BLI-062 | Add MCP server health check and status tasks | low | todo | — | — |

#### Phase 5 — Instructions (4 items)

| ID | Title | Priority | Status | Started | Completed |
|---|---|---|---|---|---|
| BLI-063 | Measure instruction context overhead | high | todo | — | — |
| BLI-064 | Split chat-capture into core + reference | medium | todo | — | — |
| BLI-065 | Split change-completeness into core + checklists | medium | todo | — | — |
| BLI-066 | Resolve instruction overlaps | medium | todo | — | — |

#### Phase 6 — Brain Workspace (5 items)

| ID | Title | Priority | Status | Started | Completed |
|---|---|---|---|---|---|
| BLI-067 | Audit brain PKM policy files for overlap | medium | todo | — | — |
| BLI-068 | Consolidate brain scripts | medium | todo | — | — |
| BLI-069 | Create brain workspace export template | low | todo | — | — |
| BLI-070 | Document brain tier boundaries | low | todo | — | — |
| BLI-071 | Review session escalation protocol complexity | low | todo | — | — |

#### Phase 7 — Documentation (3 items)

| ID | Title | Priority | Status | Started | Completed |
|---|---|---|---|---|---|
| BLI-072 | Audit docs for content duplication | medium | todo | — | — |
| BLI-073 | Verify all cross-links and navigation | medium | todo | — | — |
| BLI-074 | Create documentation hierarchy diagram | low | todo | — | — |

---

## Standalone Items

| ID | Title | Type | Priority | Status | Created | Started | Completed |
|---|---|---|---|---|---|---|---|
| BLI-001 | Build production Atlassian MCP server | feature | high | todo | 2026-03-26 | — | — |
| BLI-006 | Create code formatting instructions and skill | feature | medium | done | 2026-03-26 | 2026-03-27 | 2026-03-27 |
| BLI-020 | Build GitHub MCP server | feature | high | todo | 2026-04-11 | — | — |
| BLI-027 | Build code analysis context enhancement chain | feature | high | todo | 2026-04-21 | — | — |

---

## In Progress

| ID | Title | Type | Priority | Epic | Started | Elapsed |
|---|---|---|---|---|---|---|
| BLI-026 | Evolve 6-primitive layered architecture | feature | high | EPIC-001 | 2026-04-21 | < 1 day |

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
| IDEA-001 | Standardize purpose headers in prompts and skills | raw | 2026-04-11 | templates, prompts, skills |

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
| 2026-06-02 | 05:00 PM | BLI-086 | created | Resume & LinkedIn skill — auto-update from work history, composes atlassian/github skills (EPIC-006) |
| 2026-06-02 | 04:49 PM | BLI-085 | created | Skill-importer versioning + cross-repo re-import enhancements |
| 2026-05-29 | 09:21 PM | BLI-080–084 | created | 5 sub-items of BLI-075 for search engine (labelling, tagging, indexing, faceted search, auto-classification) |
| 2026-05-29 | 08:58 PM | BLI-075–079 | created | 5 brainstormed features for EPIC-006 (search, scraper, PKM, refinement, presentation) |
| 2026-05-29 | 08:49 PM | EPIC-006 | created | Iterative feature brainstorming epic — open-ended ideation space |
| 2026-04-11 | 10:08 PM | IMP-001 | file-import | Imported gpt.txt: 7 BLIs created, 1 IDEA, 4 merged, 2 noted |

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

> **Last updated:** 2026-06-02
