---
name: code-investigation
description: Methodology for reading, analyzing, and deeply understanding existing code — code reviews, structural analysis, deep-dives, virtual refactoring, data-flow tracing, method extraction trees, error mapping, design rationale, and dependency/coupling assessment. Use when reviewing code for smells/bugs/patterns, onboarding to unfamiliar code, tracing how data moves through a method, understanding why code was designed a certain way, or producing a developer-facing walkthrough. Activates on keywords like code analysis, code review, understand code, walk through, trace flow, data flow, deep dive, code internals, virtual refactoring, method extraction, design rationale, coupling, code smells, refactor planning.
---

# Code Investigation

> **Knowledge layer skill.** Techniques for reading, decomposing, and explaining existing
> code at two depths — **structural analysis** (high-level review with findings) and
> **deep-dive** (developer-facing walkthrough that reads like virtually refactored code).
> Workflow prompts `/code-analysis` and `/code-analysis-deep-dive` consume this knowledge
> — this skill explains the *why* behind the techniques those prompts mechanize.

## Core Question

> **What does this code do, why was it built this way, and what would change if I touched it?**

Good code investigation answers all three. Stopping at "what" produces shallow analysis;
omitting "why" makes refactoring decisions unsafe; ignoring impact (coupling, callers,
data flow) turns the analysis into trivia.

## Folded vs Unfolded — Pick Your Zoom

Think of the analysis output like IDE code folding:

```text
Folded view    = signatures + 1-line purpose
                 (Quick Scan, Refactored View §2a, Cheat Sheet)
                 → the architect's perspective on the same code

Unfolded view  = full bodies + inline annotations
                 (Method Extraction Tree §3a)
                 → the developer's perspective on the same code
```

You are the same engineer at both zoom levels — **fold to navigate, unfold to understand**.
A good analysis lets the reader choose; every technique below is designed so the reader
can stay folded until they decide to unfold a specific piece.

## Two Depths — Pick the Right One

| Depth | Use when | Time budget | Output |
|---|---|---|---|
| **Analysis** (`/code-analysis`) | Reviewing code for issues, identifying patterns, planning refactors, doing PR-style review | 5-15 min | Structured findings table + block breakdown + proposed changes |
| **Deep-Dive** (`/code-analysis-deep-dive`) | Onboarding to complex code, understanding non-obvious internals, building durable reference docs, post-incident comprehension | 30-90 min | Virtual refactoring document with method tree, data pipeline, error map, design rationale |

If unsure: start with analysis. If the analysis surfaces "I still don't understand why
this works," escalate to deep-dive.

## Virtual Refactoring — The Core Idea (Deep-Dive)

> **Think like a developer doing extract-method, but stop at the thinking stage.**

The deep-dive output is **virtually refactored code** — you decompose the method on
paper into smaller named methods (B1, B2, B3 …), each with a real Java signature and
the actual source verbatim. The reader navigates the extracted tree like real refactored
code, without any change to the codebase. The annotations live in the analysis document.

| Real refactoring | Virtual refactoring |
|---|---|
| Modifies the source file | Source file untouched |
| Risk of behaviour change | Zero risk — analysis only |
| Cost: tests, review, merge | Cost: just authoring time |
| Output: cleaner code | Output: a readable walkthrough |

### Anatomy of a B*n* Entry — Folded and Unfolded

```text
Folded view (the shape, 5 seconds):
  B1  validateOrder(Order o) → void              # null + state checks
  B2  applyDiscounts(Order o) → BigDecimal       # walks rules engine
  B3  calculateTax(BigDecimal subtotal) → BigDecimal
  B4  persist(Order o, BigDecimal total) → void

Unfolded view of B2 (the detail, when needed):

  // virtual signature
  private BigDecimal applyDiscounts(Order o)

  // verbatim source (lines L42-L57)
  BigDecimal total = o.subtotal();
  for (Rule r : rules) {                  // ← rules order: bulk before promo (E1)
    if (r.applies(o)) total = r.apply(total);
  }
  if (total.compareTo(ZERO) < 0)          // ← floor-clamp added in commit a3f4b21 (R2)
    total = ZERO;
  return total;

  // annotations
  E1: rules ordering — promo-before-bulk gives wrong total for combo orders
  R2: clamp introduced to handle negative refunds (post-incident-2025-03-12)
```

The folded view reads like an architect's mental model. The unfolded view reads like a
senior dev's annotated walkthrough. **Both views describe the same code** — the reader
picks the zoom they need.

## Structural Analysis — Six-Section Pattern

| # | Section | Purpose |
|---|---|---|
| 1 | Target summary | Class, method, package, file, one-sentence purpose |
| 2 | Code structure overview | Organization, dependencies, patterns, responsibility verdict |
| 3 | Code block breakdown | 3-8 cohesion-based blocks (name / lines / code / purpose / connection). **Find boundaries at:** blank lines, variable scope endings, data-shape changes, try/catch perimeters, early returns |
| 4 | Findings table | # / Finding / Severity / Category (smell, bug, pattern, perf, security) / Lines |
| 5 | Proposed changes | For each finding: change / impact / effort |
| 6 | Key takeaways | 3-5 bullets, overall verdict, one recommended next action |

**The most valuable section is #3 — Code Block Breakdown.** It forces you to identify
cohesion boundaries inside a long method, which is the prerequisite for everything else
(naming the blocks reveals the implicit design).

**Severity calibration** — `high` = shipping-blocker or architectural violation (broken
invariant, security, design rule broken); `medium` = code-quality (smell, weak boundary,
fragile test surface); `low` = polish (naming, comments, minor duplication).

## Deep-Dive — Four-Phase Walkthrough

```text
1. Quick Scan          — Role (system) / What (does) / Where (deps) / Errors (top 2-3)  [30 sec]
2. Data Flow & Structure
   2a. Refactored View — virtual extracted-method calls (the shape — folded)
   2b. Data Pipeline   — every transformation, type, risk
3. Code Internals
   3a. Method Extraction Tree — each Bn with source + annotations (the core — unfolded)
   3b. Error & Exception Map  — all failure modes in one table
   3c. Design Rationale       — why this pattern, what was rejected
4. Context & Reference
   4a. Dependencies & Coupling — outgoing/incoming, testability verdict
   4b. Cheat Sheet            — 5-bullet summary + debugging quick-start
```

> **§2b in dev terms:** stage-by-stage table of `in → transform → out` types, per-variable
> birth/death lines, and a risk indicator per stage. Answers *"where does this value come
> from and where can it go null?"*
>
> **§3a in dev terms:** for each B*n* — virtual signature + verbatim source lines + 2-5
> inline annotations for the non-obvious bits (preconditions, gotchas, E-refs, R-refs).

### Non-Linear Reading

A deep-dive is **not** read top-to-bottom. The output must support targeted access by
need — this is the **unfold-on-demand interface**: start folded (Quick Scan), unfold only
the part the reader actually needs.

| Reader's need | Entry point |
|---|---|
| Understand in 30 sec | Quick Scan |
| Trace data through code | Pipeline Diagram |
| Find failure modes | Error Map |
| Understand why this exists | Design Rationale |
| Debug a specific issue | Cheat Sheet → breakpoint targets |
| Assess change risk | Dependencies + Design Rationale → Evolution Risk |

## Cross-Reference ID System

Use stable IDs so sections link to one another instead of repeating content:

| Tag | Meaning |
|---|---|
| **B*n*** | Extracted method / code block (B1, B2, B3 …) |
| **L*n*** | Source file line number (L42, L47) |
| **E*n*** | Edge case / error scenario (E1, E2 …) |
| **R*n*** | Recent commit / PR change item (R1, R2 …) |

Every E-ref in the Method Tree must appear in the Error Map; every B-ref in the
Refactored View must be expanded in the Method Tree. The ID system enforces completeness.

## Design Rationale — The Hardest Section

A senior architect explains **why** the code is shaped this way, not just what it does.
Capture five fields:

| Field | Question it answers |
|---|---|
| Why this pattern | Why Strategy / Template / Service / Orchestrator instead of alternatives |
| Key trade-off | What was sacrificed for what (e.g., simplicity over performance) |
| Constraint | What forced this design (e.g., backward compat, SLO, deadline) |
| Alternative rejected | What was NOT done and why (event sourcing too complex, CQRS no benefit) |
| Evolution risk | What change will break this design (adding a 4th discount type requires modifying 3 methods) |

**Skip this section for trivial code** (simple CRUD, straightforward utilities). It is
mandatory wherever a non-obvious design choice was made.

> *Write it as the note you'd have wanted from the previous owner — what would have saved
> you the last hour of head-scratching?*

## Dependencies & Coupling

Two directions, two tables:

**Outgoing** — what this code depends on, with coupling and testability verdict per dep
(`Interface vs Concrete vs Static`, `Easy to mock vs Must use PowerMock`).

**Incoming** — who calls this code, frequency, and breakage risk if the signature changes.

**Coupling verdict** for the unit as a whole: `isolated / manageable / tangled / dangerous`.
This single word is often the most actionable output of the whole analysis.

## Anti-Patterns

| Anti-pattern | Why it fails |
|---|---|
| Describe code in prose without showing source | Reader cannot verify; analysis decays as code evolves |
| Single huge "block" instead of cohesion-based decomposition | Hides the implicit structure that is the whole point of the analysis |
| Findings without severity or category | Reader cannot triage; nothing actionable |
| Skip design rationale on non-trivial code | Future maintainer has no idea why; refactors break invariants |
| Linear narrative deep-dive (no navigation aids) | Reader cannot find the section they need; doc becomes unread |
| Conflate "what the code does" with "what the code should do" | Analysis sneaks in opinions as facts; loses trust |

## Workflow Prompts That Apply This Skill

| Prompt | When to invoke | When NOT to invoke |
|---|---|---|
| `/code-analysis` | PR-style review, structural assessment, refactor planning, finding smells/bugs | You need a durable onboarding doc — use deep-dive instead |
| `/code-analysis-deep-dive` | Onboarding to gnarly code, building a permanent reference, post-incident comprehension | The code is trivial CRUD or a 10-line utility — too much overhead |
| `/explore-project` | You don't yet know where the target code lives in the repo | You already have the file/class path |
| `/debug` | You're chasing a specific reproducible bug, not understanding the code as a whole | You want a structured walkthrough — use analysis or deep-dive |
| `/brain-new` | Capturing the analysis as a session/note for future retrieval | The analysis is throwaway scratch work |

### Sequencing Tip

```text
/explore-project → /code-analysis → (if still confused) /code-analysis-deep-dive → /brain-new
```

Always run `/code-analysis` first when in doubt. Escalate to deep-dive only if the
analysis didn't answer "why does this work" or "what would break if I change it".

## Related Skills

- `investigation-methodology` — hypothesis-driven framework for the wider research around code
- `root-cause-analysis` — when code investigation is in service of an incident
- `decision-records` — the design rationale captured in a deep-dive often deserves an ADR
