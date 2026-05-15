# View: By Project

> Per-project mini-boards for personal software development projects and learning-assistant
> features. Each project/epic gets its own kanban swimlane.
>
> **Navigation:** [Board](../BOARD.md) | [By Status](by-status.md) | **By Project** | [By Priority](by-priority.md)

---

## Project Overview

```text
┌─────────────────────────────────────────────────────────────────────────┐
│  PROJECT PORTFOLIO                                                      │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│  learning-assistant (features/)                                         │
│    EPIC-001  Learning Resources    ░░░░░░░░░░░░░░░░░░░░  0/7  (0%)    │
│    EPIC-002  Knowledge & Brain     ░░░░░░░░░░░░░░░░░░░░  0/9  (0%)    │
│    EPIC-004  Stale Ref Cleanup     ░░░░░░░░░░░░░░░░░░░░  0/13 (0%)    │
│    EPIC-005  Full Repo Revamp      ░░░░░░░░░░░░░░░░░░░░  0/34 (0%)    │
│    Standalone                      ░░░░░░░░░░░░░░░░░░░░  0/4  (0%)    │
│                                                                         │
│  personal projects (projects/)                                          │
│    EPIC-003  Personal S/W Dev      ░░░░░░░░░░░░░░░░░░░░  0/7  (0%)    │
│                                                                         │
│  general (items/)                                                       │
│    Misc items                      ████████████████████  1/1  (100%)   │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## Learning-Assistant — EPIC-001: Learning Resources System

> **Goal:** Full-fledged learning resources system with hybrid skills, MCP, and brain integration.
>
> **Progress:** 0/7 complete (0%) | **Priority:** high

```text
  BACKLOG (6)         IN PROGRESS (1)     DONE (0)
  ─────────────       ─────────────       ─────────────
  BLI-002 high        BLI-026 high
  BLI-003 high
  BLI-004 med
  BLI-005 med
  BLI-024 med
  BLI-025 low
```

| ID | Title | Priority | Status | Started | Completed |
|---|---|---|---|---|---|
| BLI-002 | Hybrid skills + MCP server + 5 primitives combo | high | todo | — | — |
| BLI-003 | Module hierarchies for learning resources | high | todo | — | — |
| BLI-004 | Brain integration with learning resources | medium | todo | — | — |
| BLI-005 | Chat session orchestration — scoped sessions, agents | medium | todo | — | — |
| BLI-024 | Workflow and terminal conditions framework | medium | todo | — | — |
| BLI-025 | Library/glossary for abbreviations and aliases | low | todo | — | — |
| BLI-026 | Evolve 6-primitive layered architecture | high | in-progress | 2026-04-21 | — |

---

## Learning-Assistant — EPIC-002: Knowledge Consolidation

> **Goal:** Cross-platform brain with Confluence migration, book consolidation, and web scraping.
>
> **Progress:** 0/9 complete (0%) | **Priority:** high

```text
  BACKLOG (9)         IN PROGRESS (0)     DONE (0)
  ─────────────       ─────────────       ─────────────
  BLI-007 high
  BLI-011 high
  BLI-012 high
  BLI-009 med
  BLI-010 med
  BLI-013 med
  BLI-021 med
  BLI-022 med
  BLI-023 med
```

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

---

## Learning-Assistant — Standalone Features

> Features not assigned to an epic.
>
> **Progress:** 0/4 active, 1/5 total complete

| ID | Title | Priority | Status | Started | Completed |
|---|---|---|---|---|---|
| BLI-001 | Build production Atlassian MCP server | high | todo | — | — |
| BLI-006 | Create code formatting instructions and skill | medium | done | 2026-03-27 | 2026-03-27 |
| BLI-020 | Build GitHub MCP server | high | todo | — | — |
| BLI-027 | Build code analysis context enhancement chain | high | todo | — | — |

---

## Learning-Assistant — EPIC-004: Stale Reference Cleanup

> **Goal:** Fix 40+ stale path references across 18+ files left from the Gradle migration.
>
> **Progress:** 0/13 complete (0%) | **Priority:** high | **Status:** active

```text
  BACKLOG (13)        IN PROGRESS (0)     DONE (0)
  ─────────────       ─────────────       ─────────────
  BLI-028 crit
  BLI-029 crit
  BLI-030 crit
  BLI-031 crit
  BLI-032 high
  BLI-033 high
  BLI-034 high
  BLI-035 high
  BLI-036 high
  BLI-037 med
  BLI-038 med
  BLI-039 low
  BLI-040 low
```

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

---

## Learning-Assistant — EPIC-005: Full Repo Revamp

> **Goal:** Modularize prompts, skills, agents, MCP servers, instructions, brain workspace,
> and documentation into composable, consistent, maintainable architecture.
>
> **Progress:** 0/34 complete (0%) | **Priority:** high | **Status:** draft
>
> **Depends on:** EPIC-004

### Phase 1 — Prompts (7 items)

| ID | Title | Priority | Status |
|---|---|---|---|
| BLI-041 | Audit prompt frontmatter consistency | high | todo |
| BLI-042 | Extract inline domain knowledge from prompts into skills | high | todo |
| BLI-043 | Standardize prompt composition patterns | high | todo |
| BLI-044 | Refactor meta prompts for clarity | medium | todo |
| BLI-045 | Consolidate brain prompts | medium | todo |
| BLI-046 | Standardize prompt input variables | medium | todo |
| BLI-047 | Create prompt composition map document | low | todo |

### Phase 2 — Skills (6 items)

| ID | Title | Priority | Status |
|---|---|---|---|
| BLI-048 | Audit skill activation keyword overlaps | high | todo |
| BLI-049 | Standardize skill file structure | high | todo |
| BLI-050 | Split oversized skills into sub-files | medium | todo |
| BLI-051 | Extract prompt-embedded knowledge into new skills | medium | todo |
| BLI-052 | Verify skill ↔ prompt ↔ agent cross-references | medium | todo |
| BLI-053 | Create skill activation map document | low | todo |

### Phase 3 — Agents (4 items)

| ID | Title | Priority | Status |
|---|---|---|---|
| BLI-054 | Standardize agent template and structure | high | todo |
| BLI-055 | Document complete agent handoff graph | high | todo |
| BLI-056 | Review and normalize agent tool restrictions | medium | todo |
| BLI-057 | Create agent selection guide | low | todo |

### Phase 4 — MCP Servers (5 items)

| ID | Title | Priority | Status |
|---|---|---|---|
| BLI-058 | Deprecate/remove Atlassian v1 server | high | todo |
| BLI-059 | Unify MCP server configuration pattern | high | todo |
| BLI-060 | Create skill wrappers for all MCP tool categories | medium | todo |
| BLI-061 | Auto-generate MCP tool documentation from Java source | medium | todo |
| BLI-062 | Add MCP server health check and status tasks | low | todo |

### Phase 5 — Instructions (4 items)

| ID | Title | Priority | Status |
|---|---|---|---|
| BLI-063 | Measure instruction context overhead | high | todo |
| BLI-064 | Split chat-capture into core + reference | medium | todo |
| BLI-065 | Split change-completeness into core + checklists | medium | todo |
| BLI-066 | Resolve instruction overlaps | medium | todo |

### Phase 6 — Brain Workspace (5 items)

| ID | Title | Priority | Status |
|---|---|---|---|
| BLI-067 | Audit brain PKM policy files for overlap | medium | todo |
| BLI-068 | Consolidate brain scripts | medium | todo |
| BLI-069 | Create brain workspace export template | low | todo |
| BLI-070 | Document brain tier boundaries | low | todo |
| BLI-071 | Review session escalation protocol complexity | low | todo |

### Phase 7 — Documentation (3 items)

| ID | Title | Priority | Status |
|---|---|---|---|
| BLI-072 | Audit docs for content duplication | medium | todo |
| BLI-073 | Verify all cross-links and navigation | medium | todo |
| BLI-074 | Create documentation hierarchy diagram | low | todo |

---

## Personal Projects — EPIC-003: Software Development Projects

> **Goal:** Portfolio of personal software projects for real daily use and learning.
>
> **Progress:** 0/7 complete (0%) | **Priority:** medium

```text
  BACKLOG (7)         IN PROGRESS (0)     DONE (0)
  ─────────────       ─────────────       ─────────────
  BLI-008 high
  BLI-014 low
  BLI-015 low
  BLI-016 low
  BLI-017 low
  BLI-018 low
  BLI-019 low
```

### BLI-008: Scheduler, Calendar, Timeline & Todo Tracking

> **Priority:** high | **Status:** todo
>
> The flagship personal project — a scheduling/calendar/todo system
> that feeds into productivity workflow for all other projects.

| Field | Value |
|---|---|
| Type | project |
| Epic | EPIC-003 |
| Tags | scheduler, calendar, todo, productivity |

### BLI-014: Media Manager

> **Priority:** low | **Status:** todo
>
> YouTube + Spotify consolidation — unified media consumption tracker.

### BLI-015: Family Tree Application

> **Priority:** low | **Status:** todo
>
> Genealogy/family history preservation app.

### BLI-016: Cloud & Ebook Reader

> **Priority:** low | **Status:** todo
>
> Unified digital reading experience — PDFs, ebooks, cloud readers.

### BLI-017: Developer Productivity Dashboard

> **Priority:** low | **Status:** todo
>
> Personal dev metrics — GitHub activity, coding time, learning progress.

### BLI-018: Code Snippet & Template Manager

> **Priority:** low | **Status:** todo
>
> Reusable code snippet library with smart search and categorisation.

### BLI-019: Tally-Vyapaar Accounting Application

> **Priority:** low | **Status:** todo
>
> Personal accounting app inspired by Tally/Vyapaar — bookkeeping, invoices, expenses.

---

## General Items

> Miscellaneous work not tied to a specific epic or project.

| ID | Title | Priority | Status | Created | Completed |
|---|---|---|---|---|---|
| BLI-006 | Create code formatting instructions and skill | medium | done | 2026-03-26 | 2026-03-27 |

---

> **Last updated:** 2026-05-15
