---
date: 2026-07-31
time: "09:33 PM"
kind: session-capture
domain: personal
category: design
project: learning-assistant
subject: copilot-output-format-primitive
tags: [project:learning-assistant, copilot-customization, output-format, instructions, response-structure, developer-experience, design-decision]
status: draft
version: 1
parent: null
complexity: medium
outcomes:
  - identified gap in primitive stack between md-formatting and steering-modes
  - mapped 3 implementation options with trade-offs (Option A/B/C)
  - identified 4 key output dimensions (verbosity, format, code prominence, depth)
  - documented request-intent to format contract candidates
  - deferred implementation pending pain-point pre-work
  - created BLI-088 to track this work
source: copilot
scope: feature
scope-project: learning-assistant
scope-feature: output-format-rules
scope-transitions: []
scope-refs:
  - file: "../../../../../../backlog/items/BLI-088_output-format-response-structure-rules.md"
    relationship: spawned
    note: "BLI created from this brainstorming session"
design-target:
  component: output-format-rules
  aspect: proposal
  level: hld
---

# Design — Copilot Output Format: Response Structure & Format-by-Intent

> **Context:** Brainstorming session to decide whether and how to define explicit output
> format rules for Copilot responses in this project. Explored the gap in the customization
> primitive stack and evaluated implementation approaches.

---

## Component Overview

| Property | Value |
|---|---|
| Component | `output-format-rules` |
| Aspect | `proposal` — evaluating whether to build and how |
| Level | HLD |
| Related Components | `md-formatting.instructions.md`, `steering-modes.instructions.md`, `copilot-instructions.md` |

---

## Intent & Purpose

- **Intent:** Determine whether explicit output format rules are needed as a customization
  primitive, and if so, which primitive type and structure to use
- **Constraints:** System prompt already covers basics; new rules must have measurable delta
  over existing behavior or they're not worth adding
- **Success criteria:** Rules change actual Copilot behavior in at least 3 identifiable
  response patterns; ruleset is maintainable without creating conflicts with existing instructions

---

## The Gap in the Primitive Stack

The current instruction layer has a **missing tier** between two existing primitives:

| Layer | File | Covers |
|---|---|---|
| Raw markdown style | `md-formatting.instructions.md` | How `.md` files look on disk |
| Behavioral modes | `steering-modes.instructions.md` | How Copilot **works** |
| **Output format** | **MISSING** | How responses are **structured and presented** in chat |

The system prompt already provides a baseline:

- Brief by default (1-3 sentences for simple answers)
- No filler preambles ("Here's the answer:", "I will now...")
- Skip intros/conclusions
- Confirm briefly after file operations

The question is whether there's enough gap between that baseline and what the user
actually wants to justify adding more rules.

---

## The Two Distinct Concerns (They're Orthogonal)

Before deciding anything, the concern was split into two independent dimensions:

**1. Response structure** — what shape does a response take?

> Code block vs. table vs. numbered steps vs. prose vs. mermaid vs. mixed

**2. Response density** — how much is said?

> One-liner vs. full analysis vs. teaching-depth explanation

These are orthogonal. A response can be dense + structured (full architecture table),
sparse + structured (one-row table), or dense + prose (deep explanation paragraph).

---

## Key Output Dimensions Identified

Four axes govern output format decisions:

```text
Axis 1: VERBOSITY
  Terse ◄──────────────────────────────► Verbose
  (command fixes, quick lookups)          (architecture, learning, deep-dives)

Axis 2: FORMAT TYPE
  prose → bullets → numbered → table → code block → mermaid → mixed

Axis 3: CODE PROMINENCE
  Code-first ◄──────────────────────► Prose-first
  (implement/fix/write)                  (explain/should I/why)

Axis 4: RESPONSE DEPTH
  Answer only → Answer + why → Explore options + trade-offs
```

---

## Request-Intent → Format Contract Candidates

The most valuable part of any output format rule would be this mapping:

| Request intent | Format contract |
|---|---|
| "Fix this bug / write this code" | Code block first — minimal prose before it |
| "Explain X" | Prose-first, examples inline, no tables unless comparing |
| "Compare A vs B" | Table, then recommendation |
| "How do I do X step by step" | Numbered steps, code inline |
| "Should I use X or Y?" | Structured analysis → recommendation |
| "Quick answer: X?" | 1-3 sentences, no structure at all |
| Architecture / design question | Headers + table + optional mermaid |
| Learning / deep-dive | Progressive: overview → detail → examples |
| Debugging session | Hypothesis → evidence → fix → prevention |

---

## Approach / Proposal — Three Options

### Option A — Dedicated instruction file

**File:** `.github/instructions/output-format.instructions.md`, `applyTo: **`

| Pros | Cons |
|---|---|
| Clean separation of concerns | Another always-on layer |
| Inspectable, version-controlled standalone | Risk of conflicting with system prompt |
| Easy to find and edit | Overhead without a clear gap to fill |
| Can grow independently | Justified only if ruleset is large (>15 rules) |

### Option B — New section in `copilot-instructions.md` *(recommended)*

**Location:** New `## Output Format` heading in the existing project config file

| Pros | Cons |
|---|---|
| No new primitive — reuses existing home | Makes that file slightly longer |
| Already the "home base" for project behavior | Slightly less discoverable than dedicated file |
| Same effect, less overhead | |
| Right size for ~10-15 rules | |

### Option C — Per-skill/per-mode output sections

**Approach:** Each steering mode and relevant skill gets its own output format note

| Pros | Cons |
|---|---|
| Context-aware — format travels with domain | Duplication across files |
| Learning mode can have richer format rules | Drift over time — rules get out of sync |
| Debug mode can enforce hypothesis/evidence/fix | Hard to get a consistent overview |

### Recommendation

**Option B** — start with a section in `copilot-instructions.md`. Only promote to Option A
(dedicated file) if the ruleset exceeds ~20 rules or if it becomes a frequent editing target.

Option C is rejected — it scales badly.

---

## Open Questions (Unanswered — Pre-Work Required)

These questions were asked during the session but **not yet answered**. They define the
pre-work gate that must be cleared before implementation begins:

### Q1 — Scope of the rules

> Is this purely about how Copilot responds in chat, or also about how generated files
> (docs, READMEs, skill files) should be structured?

*Impact:* If it covers generated files too, the ruleset is substantially larger and
Option A (dedicated file) becomes more justified.

### Q2 — "Dev-friendly" specifically means what?

> Which of these are the actual pain points?
>
> - No motivational preambles ("Great question!")
> - No summary at the end repeating what was just said?
> - Code before explanation (not explanation before code)?
> - Something else not listed here?

*Impact:* Defines the high-value rules vs. rules that would just restate the system prompt.

### Q3 — Mode interaction

> Should output format change per steering mode?
>
> - `learning` mode → teaching-depth output with richer examples?
> - `completeness` mode → structured checklists and analysis tables?
> - `debug` mode → hypothesis/evidence/fix template enforced?
> - Default (no mode) → concise, dev-first?

*Impact:* If yes, this connects output format rules to steering-modes — may need to update
both files simultaneously, or mode-overrides become part of the output format instruction.

### Q4 — User-level overrides

> Should the user be able to say "give me just the code" or "explain step by step" and
> override the default format for that request? Or should the default always hold?

*Impact:* If yes, the instruction needs an explicit "user override beats default rule"
statement. If no, the rules are stricter and more predictable.

### Q5 — What's actually broken today?

> What specific response pattern is most annoying in practice?
>
> - Too verbose for a simple fix?
> - Wrong format for the request type (table when you wanted prose, or vice versa)?
> - Preamble fluff before the code?
> - Summary at the end repeating what was said?
> - Headers used when the response is short enough to not need them?
> - Something else?

*Impact:* This is the gating question. Rules derived from real friction are valuable.
Speculative rules derived from theory risk adding noise without changing behavior.

---

## Mode-Specific Output Format (Future Consideration)

If Q3 is answered as "yes, modes should have different output formats", the integration
would look like this:

| Mode | Output format override |
|---|---|
| `learning` | Richer explanations, more examples, progressive disclosure (overview → detail → examples) |
| `completeness` | Structured analysis, checklist-style, tables for comparisons |
| `debug` | Enforced: Hypothesis → evidence → fix → prevention |
| `beast` | Dense, comprehensive, multi-section with trade-offs |
| `focused` | Minimal — code or answer only, no structure |
| Default | concise, dev-first, format-by-intent table |

---

## Acceptance Criteria

- [ ] Pre-work: Q5 answered with 3+ concrete pain points identified
- [ ] Q1-Q4 answered to define the scope and behavior of the rules
- [ ] Decision confirmed: Option B (section) vs Option A (file)
- [ ] Request-intent → format contract table finalized (8+ request types)
- [ ] Verbosity rules defined
- [ ] Code-first vs. prose-first default defined
- [ ] No-fluff zones defined
- [ ] Mode interaction scoped (in or out for this iteration)
- [ ] Every rule passes the "does it change current behavior?" gate — speculative rules cut

---

## Key Outcomes

- The gap is real: no explicit response-structure rules exist as a customization primitive
- System prompt already covers basics — new rules need clear delta to justify addition
- Three options evaluated; Option B (section in `copilot-instructions.md`) is the right starting point
- Four output dimensions identified: verbosity, format type, code prominence, response depth
- Request-intent → format contract is the highest-value piece of any future implementation
- Five open questions defined the pre-work gate — none answered yet
- BLI-088 created to track this work for a future session

---

## Follow-Up / Next Steps

- [ ] Answer Q5 first — collect 3+ real pain point examples from actual usage
- [ ] Answer Q1-Q4 (scope, dev-friendly definition, mode interaction, overrides)
- [ ] Return to BLI-088 when pre-work is done
- [ ] Decide Option A vs B based on ruleset size estimate after pre-work
- [ ] Check BLI-079 (presentation/distributable output) for any overlap to resolve

---

## Cross-References

| Relationship | Session | Note |
|---|---|---|
| related | `learning/copilot-customization/2026-04-21_02-30pm_prompts-vs-skills.md` | Prior session on copilot primitive design decisions |

---

## Session Metadata

| Property | Value |
|---|---|
| Duration | ~5 exchanges |
| Files touched | `backlog/items/BLI-088_...md`, `BOARD.md`, `CHANGELOG.md`, `views/by-status.md` |
| Related BLI | [BLI-088](../../../../../backlog/items/BLI-088_output-format-response-structure-rules.md) |
| Related BLI | [BLI-079](../../../../../backlog/features/BLI-079_presentation-of-information.md) — adjacent concern |
