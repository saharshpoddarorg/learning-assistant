# View: By Status

> All backlog items grouped by lifecycle status. Updated with every status change.
>
> **Navigation:** [Board](../BOARD.md) | **By Status** | [By Project](by-project.md) | [By Priority](by-priority.md)

---

## Status Flow

```text
  ┌──────┐    ┌────────────┐    ┌───────────┐    ┌───────────┐    ┌──────┐
  │ TODO │ →  │ IN PROGRESS│ →  │ IN REVIEW │ →  │   DONE    │    │ARCHVD│
  └──────┘    └────────────┘    └───────────┘    └───────────┘    └──────┘
                    │                                                  ▲
                    ▼                                                  │
              ┌───────────┐                                           │
              │  BLOCKED  │ ──────────────────────────────────────────┘
              └───────────┘       (unblocked → resumes flow)
```

---

## Todo (77)

> Items ready to be picked up. Ordered by priority (high → low), then by ID.

### Critical Priority

| ID | Title | Type | Epic | Created | Tags |
|---|---|---|---|---|---|
| BLI-028 | Fix copilot-instructions.md build description and stale paths | feature | EPIC-004 | 2026-05-15 | migration, paths |
| BLI-029 | Fix stale paths in mcp-server-setup.md | feature | EPIC-004 | 2026-05-15 | migration, paths |
| BLI-030 | Fix stale paths in configuration-reference.md | feature | EPIC-004 | 2026-05-15 | migration, paths |
| BLI-031 | Fix stale path in mcp-to-skill.prompt.md | feature | EPIC-004 | 2026-05-15 | migration, paths |

### High Priority

| ID | Title | Type | Epic | Created | Tags |
|---|---|---|---|---|---|
| BLI-001 | Build production Atlassian MCP server | feature | — | 2026-03-26 | mcp, atlassian |
| BLI-002 | Hybrid skills + MCP server + 5 primitives combo | feature | EPIC-001 | 2026-03-26 | mcp, skills |
| BLI-003 | Module hierarchies for learning resources | feature | EPIC-001 | 2026-03-26 | learning-resources |
| BLI-007 | Cross-platform note integration mechanism | feature | EPIC-002 | 2026-03-26 | brain, notes |
| BLI-008 | Scheduler, calendar, timeline & todo tracking | project | EPIC-003 | 2026-03-26 | scheduler, calendar |
| BLI-011 | Brain consolidation — tabs, bookmarks, workspace, notes | feature | EPIC-002 | 2026-03-26 | brain, consolidation |
| BLI-012 | Enhanced web scraping & search engine | feature | EPIC-002 | 2026-03-26 | search, scraping |
| BLI-020 | Build GitHub MCP server | feature | — | 2026-04-11 | mcp, github |
| BLI-027 | Build code analysis context enhancement chain | feature | — | 2026-04-21 | code-analysis, vcs |
| BLI-032 | Fix stale paths in search-engine docs | feature | EPIC-004 | 2026-05-15 | migration, paths |
| BLI-033 | Fix stale paths in USAGE.md | feature | EPIC-004 | 2026-05-15 | migration, paths |
| BLI-034 | Fix brain model path references | feature | EPIC-004 | 2026-05-15 | migration, paths |
| BLI-035 | Fix stale paths in versioning, mcp-vs-skills, phase-guide | feature | EPIC-004 | 2026-05-15 | migration, paths |
| BLI-036 | Fix stale paths in slash-commands, nav-index, evolution-guide | feature | EPIC-004 | 2026-05-15 | migration, paths |
| BLI-041 | Audit prompt frontmatter consistency | feature | EPIC-005 | 2026-05-15 | prompts, modularization |
| BLI-042 | Extract inline domain knowledge from prompts into skills | feature | EPIC-005 | 2026-05-15 | prompts, skills |
| BLI-043 | Standardize prompt composition patterns | feature | EPIC-005 | 2026-05-15 | prompts, composition |
| BLI-048 | Audit skill activation keyword overlaps | feature | EPIC-005 | 2026-05-15 | skills, modularization |
| BLI-049 | Standardize skill file structure | feature | EPIC-005 | 2026-05-15 | skills, modularization |
| BLI-054 | Standardize agent template and structure | feature | EPIC-005 | 2026-05-15 | agents, modularization |
| BLI-055 | Document complete agent handoff graph | feature | EPIC-005 | 2026-05-15 | agents, documentation |
| BLI-058 | Deprecate/remove Atlassian v1 server | feature | EPIC-005 | 2026-05-15 | mcp, deprecation |
| BLI-059 | Unify MCP server configuration pattern | feature | EPIC-005 | 2026-05-15 | mcp, configuration |
| BLI-063 | Measure instruction context overhead | feature | EPIC-005 | 2026-05-15 | instructions, measurement |
| BLI-075 | Search engine — online and local knowledge search | feature | EPIC-006 | 2026-05-29 | search, web-search, notes-search |
| BLI-076 | Web scraper — intelligent content extraction and filtering | feature | EPIC-006 | 2026-05-29 | web-scraper, content-extraction |
| BLI-077 | PKM/brain/notes manager — full knowledge lifecycle | feature | EPIC-006 | 2026-05-29 | pkm, brain, notes |

### Medium Priority

| ID | Title | Type | Epic | Created | Tags |
|---|---|---|---|---|---|
| BLI-004 | Brain integration with learning resources | feature | EPIC-001 | 2026-03-26 | brain, learning-resources |
| BLI-005 | Chat session orchestration — scoped sessions, agents | feature | EPIC-001 | 2026-03-26 | sessions, agents |
| BLI-009 | Third-party libs, JDK 26, SDK tools & annotations | feature | EPIC-002 | 2026-03-26 | jdk, libraries |
| BLI-010 | Personal Confluence — migrate office resources | feature | EPIC-002 | 2026-03-26 | confluence, migration |
| BLI-013 | Book consolidation — PDFs, ebooks, cloud readers | feature | EPIC-002 | 2026-03-26 | books, ebooks |
| BLI-021 | Reusable content templates system | feature | EPIC-002 | 2026-04-11 | templates, reusable |
| BLI-022 | Export and migration support for portability | feature | EPIC-002 | 2026-04-11 | export, migration |
| BLI-023 | Cross-repo item merge and sync | feature | EPIC-002 | 2026-04-11 | merge, cross-repo |
| BLI-024 | Workflow and terminal conditions framework | feature | EPIC-001 | 2026-04-11 | workflow, automation |
| BLI-037 | Fix stale paths in EPIC-001 and EPIC-002 notes | feature | EPIC-004 | 2026-05-15 | migration, paths |
| BLI-038 | Fix stale paths in MCP-README.md and SETUP.md | feature | EPIC-004 | 2026-05-15 | migration, paths |
| BLI-044 | Refactor meta prompts for clarity | feature | EPIC-005 | 2026-05-15 | prompts, modularization |
| BLI-045 | Consolidate brain prompts | feature | EPIC-005 | 2026-05-15 | prompts, brain |
| BLI-046 | Standardize prompt input variables | feature | EPIC-005 | 2026-05-15 | prompts, standardization |
| BLI-050 | Split oversized skills into sub-files | feature | EPIC-005 | 2026-05-15 | skills, splitting |
| BLI-051 | Extract prompt-embedded knowledge into new skills | feature | EPIC-005 | 2026-05-15 | skills, extraction |
| BLI-052 | Verify skill ↔ prompt ↔ agent cross-references | feature | EPIC-005 | 2026-05-15 | cross-references |
| BLI-056 | Review and normalize agent tool restrictions | feature | EPIC-005 | 2026-05-15 | agents, tools |
| BLI-060 | Create skill wrappers for all MCP tool categories | feature | EPIC-005 | 2026-05-15 | mcp, skills |
| BLI-061 | Auto-generate MCP tool documentation from Java source | feature | EPIC-005 | 2026-05-15 | mcp, documentation |
| BLI-064 | Split chat-capture into core + reference | feature | EPIC-005 | 2026-05-15 | instructions, splitting |
| BLI-065 | Split change-completeness into core + checklists | feature | EPIC-005 | 2026-05-15 | instructions, splitting |
| BLI-066 | Resolve instruction overlaps | feature | EPIC-005 | 2026-05-15 | instructions, cleanup |
| BLI-067 | Audit brain PKM policy files for overlap | feature | EPIC-005 | 2026-05-15 | brain, audit |
| BLI-068 | Consolidate brain scripts | feature | EPIC-005 | 2026-05-15 | brain, scripts |
| BLI-072 | Audit docs for content duplication | feature | EPIC-005 | 2026-05-15 | documentation, audit |
| BLI-073 | Verify all cross-links and navigation | feature | EPIC-005 | 2026-05-15 | documentation, links |
| BLI-078 | Question refinement — smarter query formulation for GHCP | feature | EPIC-006 | 2026-05-29 | question-refinement, query |
| BLI-079 | Presentation of information — structured and distributable output | feature | EPIC-006 | 2026-05-29 | presentation, formatting |

### Low Priority

| ID | Title | Type | Epic | Created | Tags |
|---|---|---|---|---|---|
| BLI-014 | Media manager — YouTube, Spotify consolidation | project | EPIC-003 | 2026-03-26 | media, youtube, spotify |
| BLI-015 | Family tree application | project | EPIC-003 | 2026-03-26 | family, genealogy |
| BLI-016 | Cloud & ebook reader | project | EPIC-003 | 2026-03-26 | ebooks, reader |
| BLI-017 | Developer productivity dashboard | project | EPIC-003 | 2026-03-26 | productivity, dashboard |
| BLI-018 | Code snippet & template manager | project | EPIC-003 | 2026-03-26 | snippets, templates |
| BLI-019 | Tally-Vyapaar accounting application | project | EPIC-003 | 2026-04-11 | accounting, finance |
| BLI-025 | Library/glossary for abbreviations and aliases | feature | EPIC-001 | 2026-04-11 | glossary, reference |
| BLI-039 | Add module-specific README.md to each modules directory | feature | EPIC-004 | 2026-05-15 | documentation |
| BLI-040 | Consolidate configuration documentation | feature | EPIC-004 | 2026-05-15 | documentation, config |
| BLI-047 | Create prompt composition map document | feature | EPIC-005 | 2026-05-15 | prompts, documentation |
| BLI-053 | Create skill activation map document | feature | EPIC-005 | 2026-05-15 | skills, documentation |
| BLI-057 | Create agent selection guide | feature | EPIC-005 | 2026-05-15 | agents, documentation |
| BLI-062 | Add MCP server health check and status tasks | feature | EPIC-005 | 2026-05-15 | mcp, health |
| BLI-069 | Create brain workspace export template | feature | EPIC-005 | 2026-05-15 | brain, export |
| BLI-070 | Document brain tier boundaries | feature | EPIC-005 | 2026-05-15 | brain, documentation |
| BLI-071 | Review session escalation protocol complexity | feature | EPIC-005 | 2026-05-15 | brain, simplification |
| BLI-074 | Create documentation hierarchy diagram | feature | EPIC-005 | 2026-05-15 | documentation, diagram |

---

## In Progress (1)

> Items currently being worked on.

| ID | Title | Priority | Epic | Started | Elapsed | Assignee |
|---|---|---|---|---|---|---|
| BLI-026 | Evolve 6-primitive layered architecture | high | EPIC-001 | 2026-04-21 | 24 days | — |

---

## Blocked (0)

> Items that cannot proceed due to a dependency or blocker.

| ID | Title | Priority | Blocked Since | Blocker Description | Depends On |
|---|---|---|---|---|---|

> No blocked items.

---

## In Review (0)

> Items completed but awaiting review, testing, or verification.

| ID | Title | Priority | Review Since | Review Notes |
|---|---|---|---|---|

> No items in review.

---

## Done (1)

> Completed items. Sorted newest-first.

| ID | Title | Type | Priority | Epic | Started | Completed | Cycle Time |
|---|---|---|---|---|---|---|---|
| BLI-006 | Create code formatting instructions and skill | feature | medium | — | 2026-03-27 | 2026-03-27 | < 1 day |

### Completion Stats

```text
Total completed:  1
Avg cycle time:   < 1 day
This month:       1
```

---

## Archived (0)

> Items removed from active tracking. Kept for historical reference.

| ID | Title | Archived Date | Reason |
|---|---|---|---|

> No archived items.

---

## Status Distribution

```text
Todo         ████████████████████████████████████████████  72  (97%)
In Progress  █░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░   1   (1%)
Blocked      ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░   0   (0%)
In Review    ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░   0   (0%)
Done         █░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░   1   (1%)
```

---

> **Last updated:** 2026-05-15
