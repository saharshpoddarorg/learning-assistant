---
id: EPIC-004
title: Repo modularization and stale reference cleanup
status: active
priority: high
created: 2026-05-15
updated: 2026-05-15
tags: [modularization, migration, cleanup, documentation, gradle]
---

# EPIC-004: Repo Modularization and Stale Reference Cleanup

## Vision

Complete the Gradle multi-module migration by fixing all stale path references,
updating all documentation to reflect the new `modules/` layout, and ensuring
every asset (prompts, skills, instructions, docs, brain, scripts) is consistent
with the current project structure. No information is lost — only paths and
references are corrected.

## Background

PR #45 migrated all Java code to 7 Gradle modules under `modules/`. PR #46
deleted legacy directories. However, 40+ stale path references remain scattered
across 18+ documentation, prompt, skill, backlog, and configuration files.
These references point to old paths like `mcp-servers/src/`, `search-engine/src/`,
and `brain/src/` that no longer exist.

## Phases

### Phase 1 — Critical Path Fixes (correctness)

Fix references that actively mislead users or break workflows.

| ID | Title | Status | Priority |
|---|---|---|---|
| BLI-028 | Fix copilot-instructions.md build description and stale paths | todo | critical |
| BLI-029 | Fix stale paths in mcp-server-setup.md (22+ references) | todo | critical |
| BLI-030 | Fix stale paths in configuration-reference.md (20+ references) | todo | critical |
| BLI-031 | Fix stale path in mcp-to-skill.prompt.md | todo | critical |

### Phase 2 — Documentation Consistency (high)

Update remaining docs to use correct module paths.

| ID | Title | Status | Priority |
|---|---|---|---|
| BLI-032 | Fix stale paths in search-engine.md and search-engine-algorithms.md | todo | high |
| BLI-033 | Fix stale paths in USAGE.md | todo | high |
| BLI-034 | Fix brain model path references (brain READMEs + digitalnotetaking docs) | todo | high |
| BLI-035 | Fix stale paths in versioning-guide.md, mcp-vs-skills.md, phase-guide.md | todo | high |
| BLI-036 | Fix stale paths in slash-commands.md, navigation-index.md, customization-evolution-guide.md | todo | high |

### Phase 3 — Backlog & Session Cleanup (medium)

Fix backlog items and mark historical sessions.

| ID | Title | Status | Priority |
|---|---|---|---|
| BLI-037 | Fix stale paths in EPIC-001 and EPIC-002 notes sections | todo | medium |
| BLI-038 | Fix stale paths in MCP-README.md and SETUP.md copy commands | todo | medium |

### Phase 4 — Structural Improvements (low, future)

Enhance documentation structure for long-term maintainability.

| ID | Title | Status | Priority |
|---|---|---|---|
| BLI-039 | Add module-specific README.md to each modules/* directory | todo | low |
| BLI-040 | Consolidate configuration documentation to single source of truth | todo | low |

## Scope

### In Scope

- Fix all 40+ stale path references across 18+ files
- Update copilot-instructions.md build description
- Update backlog epics with correct paths
- Ensure all prompts and skills reference correct module paths
- Update features-inventory.md as the living document

### Out of Scope

- New features or capabilities
- Restructuring brain workspace tiers
- Adding new prompts, skills, or agents
- Changing Java code or module boundaries

## Success Criteria

- [ ] Zero stale references to `mcp-servers/src/` in any active documentation
- [ ] Zero stale references to `search-engine/src/` (without `modules/` prefix) in docs
- [ ] Zero stale references to `brain/src/` in brain documentation
- [ ] `copilot-instructions.md` accurately describes Gradle build system
- [ ] All backlog epics reference correct module paths
- [ ] `.\gradlew.bat build` passes (no code changes, so should pass trivially)
- [ ] `.\__md_lint.ps1` passes on all modified files

## Open Questions

- [ ] Should historical session files be annotated with `[HISTORICAL]` or left as-is?
- [ ] Should module-specific READMEs be created as part of this epic or a future one?

## Notes

- **Safety principle:** No content deletion — only path corrections
- **Audit document:** `.github/docs/features-inventory.md` — comprehensive asset inventory
- Related: EPIC-001 (learning resources system), EPIC-002 (knowledge consolidation)
