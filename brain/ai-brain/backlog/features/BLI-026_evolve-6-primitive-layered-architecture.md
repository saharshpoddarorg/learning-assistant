---
id: BLI-026
title: Evolve 6-primitive layered architecture — understand, audit, and enhance
status: in-progress
priority: high
type: feature
created: 2026-04-21
updated: 2026-04-21
started: 2026-04-21
completed: null
blocked-since: null
review-since: null
epic: EPIC-001
sprint: null
parent: null
sub-items: []
origin: null
estimated-effort: L
actual-effort: null
tags: [copilot-customization, primitives, layered-architecture, skills, prompts, instructions, agents, mcp, framework-evolution]
origin-type: manual
import-batch: null
source-file: null
---

# BLI-026: Evolve 6-primitive layered architecture — understand, audit, and enhance

## Description

Build deep understanding of GitHub Copilot's 6 customization primitives (instructions,
skills, prompts, agents, MCP servers) and iteratively evolve the learning-assistant's
framework into a coherent, well-documented layered architecture.

This is an **iterative learning + enhancement** feature — each session deepens understanding
and produces actionable improvements. The work spans:

1. **Understanding** — how each primitive works, how they compose, what they can/can't do
2. **Auditing** — current state of the framework (23 skills, 65 prompts, 9 instructions,
   8 agents, MCP configured) — identify gaps, overlaps, and misplacements
3. **Evolving** — fill dual-pattern gaps, strengthen layers, add MCP where needed,
   compose full-stack workflows

### Scope Distinction from BLI-002

BLI-002 focuses on the **learning resources system** specifically (hybrid skills + MCP
for resource discovery). This BLI is **framework-wide** — understanding and evolving
the entire 6-primitive architecture across ALL domains, not just learning resources.

### Future Considerations

- Phase 1: Fill dual-pattern gaps (jvm-platform, java-build, software-dev-roles)
- Phase 2: Strengthen instruction/agent layers
- Phase 3: MCP integration where live data is needed
- Phase 4: Full-stack composition recipes
- `#file:` prompt composition (Recipe 3) for DRY across overlapping prompts
- Quarterly framework fitness review cadence

## Acceptance Criteria

- [x] Understand prompts vs skills distinction (dual pattern, activation, composition)
- [x] Understand skill structure — flat, not hierarchical; TAXONOMY.md is logical only
- [x] Understand prompt composition — standalone, no inheritance; `#file:` for composition
- [x] Complete framework audit (23 skills, 65 prompts, 9 instructions, 8 agents)
- [x] Identify dual-pattern gaps (skills without prompts, prompts without skills)
- [x] Document 5-layer architecture model
- [ ] Phase 1: Create missing prompts for orphan skills (jvm-platform, java-build, etc.)
- [ ] Phase 2: Review instruction/agent layer for misplaced content
- [ ] Phase 3: Evaluate MCP integration opportunities
- [ ] Phase 4: Build full-stack composition recipes
- [ ] Document the evolution protocol in `.github/docs/`
- [ ] Establish quarterly framework fitness review

## Attachments & References

| Type | Path / URL | Added | Notes |
|---|---|---|---|
| session | `sessions/personal/personal-work/learning/2026-04-21_learning_copilot-prompts-vs-skills.md` | 2026-04-21 | Prompts vs skills — dual pattern, activation, fitness scorecard |
| session | `sessions/personal/personal-work/learning/2026-04-21_learning_framework-evolution-guide.md` | 2026-04-21 | Full audit, 5-layer architecture, gap analysis, 4-phase roadmap |
| session | `sessions/personal/personal-work/learning/2026-04-21_learning_prompt-composition-superset.md` | 2026-04-21 | Prompt composition — standalone, #file: references, overlap patterns |
| doc | `.github/docs/copilot-customization-deep-dive.md` | — | 18-part reference, Part 4 (Composition), Part 5 (Anti-Patterns) |
| doc | `.github/docs/customization-evolution-guide.md` | — | Fitness scorecard, dual pattern, import/merge protocols |
| doc | `.github/skills/TAXONOMY.md` | — | 4 domains, 10 categories, 23 skills — logical hierarchy |

## Related

- **BLI-002** — Hybrid skills + MCP + 5 primitives combo (learning-resources-scoped;
  this BLI is framework-wide)
- **BLI-005** — Chat session orchestration (agent coordination patterns are Phase 4 here)
- **EPIC-001** — Full-fledged learning resources system (parent epic)

## Activity Log

| Date | Time | Actor | Action | Details |
|---|---|---|---|---|
| 2026-04-21 | 04:20 PM | system | created | Item created from iterative learning sessions |
| 2026-04-21 | 04:20 PM | system | started | AC 1-6 completed via 3 learning sessions |

## Notes

- This is iterative — more sessions will add understanding and concrete improvements
- Sessions are captured in `personal/personal-work/learning/` (global scope, not project-specific)
- The 5-layer architecture model: Instructions → Skills → Prompts → Agents → MCP
- Key finding: ALL 6 primitives are flat and standalone — no inheritance, no nesting
- Composition mechanisms vary: stacking (instructions), sub-files (skills), `#file:` (prompts),
  handoff (agents), all-available (MCP)

## Time Tracking

| Date | Duration | Activity | Notes |
|---|---|---|---|
| 2026-04-21 | ~2h | Learning sessions (3) | Prompts vs skills, framework audit, prompt composition |
