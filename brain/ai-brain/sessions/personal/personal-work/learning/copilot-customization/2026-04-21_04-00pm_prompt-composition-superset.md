---
date: 2026-04-21
time: "04:00 PM"
kind: session-capture
domain: personal
category: learning
project: learning-assistant
subject: copilot-customization-prompt-composition-superset
tags: [copilot-customization, prompts, composition, superset-pattern, prompt-chain, ship, github-push]
status: draft
version: 1
parent: null
complexity: high
outcomes:
  - confirmed prompts cannot inherit or extend each other
  - documented Recipe 3 (#file: composition) as the only prompt-to-prompt mechanism
  - analysed /ship vs /github-push overlap (intentional, not a defect)
  - identified 3 patterns for handling prompt overlap
source: copilot
scope: global
scope-project: null
scope-feature: null
scope-transitions: []
scope-refs:
  - file: "../copilot-customization/2026-04-21_02-30pm_prompts-vs-skills.md"
    relationship: origin
    note: "prompts-vs-skills session that raised the composition question"
  - file: "../copilot-customization/2026-04-21_03-15pm_framework-evolution-guide.md"
    relationship: related
    note: "framework evolution guide that documents the flatness finding"
---

# SESSION: Can a Prompt Be a Superset of Another Prompt? — 2026-04-21

## TL;DR

Investigated whether Copilot prompts can have superset/subset relationships — e.g., `/ship`
as a superset of `/github-push`. **Answer: No, prompts cannot inherit from or extend other
prompts.** Each `.prompt.md` is a standalone, self-contained file. There is no `extends`,
`imports`, or parent-child mechanism between prompts. However, Copilot supports a
**Prompt Chain pattern** (`#file:` references) that achieves COMPOSITION (not inheritance).
The project's `/ship` and `/github-push` are **independent prompts with overlapping scope** —
not a superset/subset pair.

## What Was Done

- Compared `/ship` (commit/push/PR suggestion) vs `/github-push` (full shipping: cohesive
  split + push + create PR via API) side-by-side
- Studied Part 4 of the Copilot Customization Deep Dive: Composition Patterns (Recipes 1-12)
- Identified that Recipe 3 (Prompt Chain via `#file:`) is the closest to "superset" behavior
- Verified that zero prompts in this project currently use `#file:` prompt references
- Documented the correct mental model for prompt relationships
- Compared with the earlier skill nesting question — same pattern (flat, not hierarchical)

## Key Insights

### Objective Assessment: Can a Prompt Be a Superset of Another Prompt?

**Your idea:** `/ship` is a superset of `/github-push` — ship includes lint, build, commit,
push, AND PR suggestion, while github-push handles just the push + PR part. So ship "contains"
or "extends" github-push.

**Verdict: No — prompts cannot be supersets of each other. But the instinct reveals a real
design pattern problem worth understanding.**

#### How Prompts Actually Work (The Ground Truth)

**Prompts are STANDALONE.** Here is what actually happens:

1. **One `.prompt.md` = one `/slash-command`.** When you type `/ship`, Copilot loads
   `ship.prompt.md` and ONLY `ship.prompt.md`. It does NOT look at `github-push.prompt.md`
   or any other prompt. Each prompt is its own world.

2. **There is NO `extends` or `imports` mechanism.** Unlike Java classes, prompts cannot
   inherit from each other. There is no:

   ```yaml
   # ❌ THIS DOES NOT EXIST
   ---
   name: ship
   extends: github-push      ← NOT A REAL FIELD
   ---
   ```

3. **There is NO automatic nesting.** Copilot does not detect that `/ship` and
   `/github-push` share overlapping steps and compose them. Each prompt executes in
   complete isolation from other prompts.

4. **Only ONE prompt is active at a time.** Unlike skills (where multiple can auto-activate
   simultaneously), only the prompt you explicitly invoke runs.

```text
THIS IS HOW IT WORKS (isolated, standalone):

User types: /ship mode=both
  → Copilot loads ship.prompt.md
  → Executes: Pre-Flight → Commit → Push → PR Suggestion
  → github-push.prompt.md is NEVER consulted

User types: /github-push mode=full
  → Copilot loads github-push.prompt.md
  → Executes: Parse URL → Assess → Pre-Flight → Cohesive Split → Push → PR Create
  → ship.prompt.md is NEVER consulted
```

```text
THIS IS NOT HOW IT WORKS (your hypothesized superset model):

❌ /ship
    extends /github-push (inherits push + PR steps)
    adds: lint, build, commit (its own steps)

❌ /github-push
    extends /ship (inherits lint, build)
    adds: cohesive splitting, existing PR check, PR creation via API

Prompts do NOT extend, inherit, or compose from each other automatically.
```

#### What's Actually Happening: Overlapping Scope, Not Superset/Subset

Looking at the two prompts side-by-side:

| Capability | `/ship` | `/github-push` |
|---|---|---|
| **Lint markdown** | ✅ Pre-Flight | ✅ Step 2 |
| **Build Java** | ✅ Pre-Flight | ✅ Step 2 |
| **Stage files** | ✅ Explicit `git add` | ✅ Explicit `git add` |
| **Single commit** | ✅ Commit Workflow | ✅ Step 4 (mode: ship) |
| **Cohesive split** | ❌ No | ✅ Step 3 (mode: full) |
| **Push** | ✅ Push Workflow | ✅ Step 5 |
| **PR suggestion** | ✅ Text suggestion only | ❌ No — creates real PR |
| **PR creation via API** | ❌ No | ✅ Step 7 (via tool) |
| **Check existing/merged PR** | ❌ No | ✅ Step 6 |
| **Parse repo URL input** | ❌ No | ✅ Step 0 |
| **Tools** | runCommands, codebase, editFiles | runCommands, codebase, editFiles, **githubPR** |

**These are NOT superset/subset.** They are **overlapping but distinct** prompts:

- `/ship` is simpler: commit + push + text-based PR suggestion. No API calls.
- `/github-push` is more powerful: cohesive splitting, existing PR detection, actual PR
  creation via GitHub API, repo URL parsing. But it requires the `githubPR` tool.

Neither fully contains the other. They share common steps (lint, build, push) but diverge
in their approach to committing (single vs. cohesive split) and PR handling (suggest vs.
create via API).

#### The Correct Pattern: How to Actually Share Logic Between Prompts

Copilot supports ONE mechanism for prompt-to-prompt composition:

**Recipe 3: Prompt Chain (`#file:` references)**

```markdown
<!-- hypothetical composite.prompt.md -->
---
name: composite
description: 'Full shipping workflow with phases'
---
Phase 1 — Pre-Flight:
#file:.github/prompts/pre-flight.prompt.md

Phase 2 — Commit:
#file:.github/prompts/commit.prompt.md

Phase 3 — Push + PR:
#file:.github/prompts/github-push.prompt.md
```

This is **composition, not inheritance.** The composite prompt INCLUDES the content of
other prompts inline. It's like copy-paste at invocation time, not like class inheritance.

**Key limitations of `#file:` composition:**

| Feature | Supported? | Notes |
|---|---|---|
| Include another prompt's instructions | ✅ Yes | Content is inlined at that position |
| Override specific steps from the included prompt | ❌ No | You get ALL of it, or none |
| Pass variables between prompts | ❌ No | Each prompt's `${input:}` vars are independent |
| Selective step extraction | ❌ No | Can't say "import only Step 3 from github-push" |
| Tool union | ❌ No | Only the OUTER prompt's `tools:` field applies |

**Current state in this project:** Zero prompts use `#file:` references to compose with
other prompts. Each of the 65 prompts is fully self-contained.

#### Why This Project Has Two Overlapping Prompts (And That's OK)

The `/ship` and `/github-push` prompts evolved independently:

1. **`/ship`** came first — a simple commit + push helper with text-based PR suggestions
2. **`/github-push`** came later — a more powerful version with cohesive commit splitting,
   existing PR detection, and actual GitHub API PR creation

They overlap because they solve related problems differently, not because one extends the other.

**This is analogous to the skill situation:**

| Concept | Skills | Prompts |
|---|---|---|
| Can they nest/inherit? | ❌ No — flat, independent | ❌ No — standalone, one-at-a-time |
| How are they discovered? | Semantic match on `description` | Manual `/command` invocation |
| Can multiple be active? | ✅ Yes — skills stack | ❌ No — one prompt at a time |
| Composition mechanism | Sub-files within one skill | `#file:` references (Recipe 3) |
| Organizational tool | TAXONOMY.md (logical tree) | hub.prompt.md (navigation tree) |

#### Scoreboard: Your Understanding

| Idea | Correct? | Reality |
|---|---|---|
| "/ship is a superset of /github-push" | ❌ Not accurate | They overlap but neither contains the other |
| "A prompt can extend/inherit from another" | ❌ Not supported | Prompts are standalone; no `extends` mechanism |
| "One prompt can include another via `#file:`" | ✅ Correct (Recipe 3) | Composition, not inheritance — full inline inclusion |
| "Prompts with overlapping scope should be merged" | 🟡 Depends | Sometimes overlap is intentional (different complexity levels) |
| "Prompts are isolated — only one active at a time" | ✅ Correct | Unlike skills, prompts don't stack |

#### The Three Patterns for Handling Prompt Overlap

**Pattern 1: Accept the Overlap (current approach in this project)**

Keep `/ship` (simple) and `/github-push` (powerful) as separate prompts. Users choose
based on their need. Like having both `git commit` and `git commit --amend` — they overlap,
but serve different use cases.

```text
/ship         → Quick: commit + push + text PR suggestion
/github-push  → Full: cohesive split + push + create PR via API
```

**Pattern 2: Merge into One Prompt with Modes**

Combine into a single prompt with modes that cover all use cases:

```text
/ship mode=commit     → commit only (from current /ship)
/ship mode=push       → push only (from current /ship)
/ship mode=both       → commit + push (from current /ship)
/ship mode=full       → cohesive split + push + create PR (from current /github-push)
```

This eliminates overlap but makes one prompt very complex.

**Pattern 3: Decompose into Composable Phases**

Create small, focused prompts and compose them:

```text
/pre-flight       → lint + build checks
/commit           → single commit with Conventional Commits
/commit-split     → cohesive commit splitting
/push-pr          → push + PR creation

/ship = /pre-flight → /commit → /push-pr    (via #file: composition)
/full = /pre-flight → /commit-split → /push-pr (via #file: composition)
```

This is the most elegant but requires the `#file:` pattern that's not yet used in this project.

### Updated Primitive Composition Summary

```text
PRIMITIVE    | NESTING? | COMPOSITION MECHANISM      | MULTI-ACTIVE?
─────────────|──────────|────────────────────────────|──────────────
Instructions | ❌ flat   | Stacking (all matching)    | ✅ Yes (all match)
Skills       | ❌ flat   | Sub-files in one folder    | ✅ Yes (semantic)
Prompts      | ❌ flat   | #file: references (inline) | ❌ No (one at a time)
Agents       | ❌ flat   | Handoff chain              | ❌ No (one at a time)
MCP Servers  | ❌ flat   | All available in agent mode | ✅ Yes (all servers)
```

**Key insight:** NONE of the 6 primitives support inheritance or superset/subset
relationships. They are ALL flat and standalone. The composition mechanisms are:
inclusion (#file:), stacking (instructions/skills), and handoff (agents).

## Decisions Made

- `/ship` and `/github-push` serve different complexity levels — overlap is acceptable
- Prompts cannot extend each other — use `#file:` composition (Recipe 3) if DRY is needed
- The `#file:` pattern is worth exploring when prompt overlap grows beyond 2 prompts

## Open Questions / Follow-Ups

- [ ] Should `/ship` and `/github-push` be merged into one prompt with richer modes?
- [ ] Experiment with `#file:` prompt composition (Recipe 3) — create a small reusable
  `pre-flight.prompt.md` and compose it into both `/ship` and `/github-push`
- [ ] Audit all 65 prompts for overlapping scope — identify other candidates for merging
  or decomposition

## Resources Referenced

- [ship.prompt.md](.github/prompts/ship.prompt.md) — commit/push/PR suggestion workflow
- [github-push.prompt.md](.github/prompts/github-push.prompt.md) — full shipping with cohesive split + API PR
- [Copilot Deep Dive Part 4](.github/docs/copilot-customization-deep-dive.md#part-4-composition-patterns) — Recipe 3 (Prompt Chain via `#file:`)
- [hub.prompt.md](.github/prompts/hub.prompt.md) — navigation tree showing both `/ship` and `/github-push`
- Previous session: [Prompts vs Skills](2026-04-21_02-30pm_prompts-vs-skills.md) — foundational dual-pattern understanding
- Previous session: [Framework Evolution Guide](2026-04-21_03-15pm_framework-evolution-guide.md) — skill nesting analysis (same answer: flat, not hierarchical)

## Related Sessions

- **Previous:** [Prompts vs Skills](2026-04-21_02-30pm_prompts-vs-skills.md) — skills are flat, not hierarchical (same pattern)
- **Previous:** [Framework Evolution Guide](2026-04-21_03-15pm_framework-evolution-guide.md) — full audit + layered architecture
