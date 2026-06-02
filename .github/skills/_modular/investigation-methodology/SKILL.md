---
name: investigation-methodology
description: >
  Core framework for any systematic investigation in a software engineering context.
  Activates for: research plan, investigation framework, hypothesis-driven research,
  evidence synthesis, source evaluation, when-to-research-vs-code, spike story,
  due diligence, discovery session, research types (spike, trade-off, competitive,
  feasibility), source hierarchy (P1–P5 primary vs opinion).
  Use when the question is "how should I investigate X?" rather than "what is X?".
  Related skills: `technology-evaluation` (choosing tech), `root-cause-analysis`
  (production issues), `decision-records` (capturing the outcome),
  `code-investigation` (reading existing code), `requirements-research` (WHAT to build).
---

# Investigation Methodology

## Core Question

> **What do I need to know before I can act, and how will I know I've learned it?**

A good investigation has an explicit question, ranked hypotheses, an evidence
plan, and a verdict. Without these, "research" devolves into reading until you
feel comfortable.

## When to Research vs When to Code

| Situation | Action |
|---|---|
| "I don't know if this library exists / fits" | Time-boxed research spike first |
| "I know the problem; I know the solution" | Code directly |
| "The requirement is unclear" | See `requirements-research` skill |
| "Two approaches seem equally valid" | Trade-off analysis — see `technology-evaluation` |
| "Production bug I can't reproduce" | See `root-cause-analysis` skill |
| "I need to understand this code" | See `code-investigation` skill |

## Research Types & When to Use Each

| Type | Use when | Output |
|---|---|---|
| **Spike / POC** | Technical unknown blocks a feature | 1–2 day prototype + findings doc |
| **Requirements investigation** | Goal or scope unclear | User stories, acceptance criteria |
| **Trade-off analysis** | Choosing between viable options | Decision matrix + ADR |
| **Root-cause analysis** | Production issue, regression | RCA doc with timeline + fix |
| **Competitive analysis** | Evaluating tools/libraries | Comparison table + recommendation |
| **Feasibility study** | "Can we even do this?" | Feasibility report with risk assessment |

## Hypothesis-Driven Framework (HDF)

Every investigation begins with explicit hypotheses. This prevents scope creep
and makes findings verifiable.

```text
TRIGGER      What question prompted this investigation?
HYPOTHESES   2–4 candidate explanations, ranked by believability (%)
EXPERIMENTS  For each hypothesis: what evidence would prove/disprove it?
EVIDENCE     What you actually found (data, code, docs, tests)
VERDICT      Which hypothesis survived? What did you learn?
DECISION     What action (if any) follows?
```

### Worked example — "Our API sometimes returns 503"

```yaml
TRIGGER:      Intermittent 503 on /checkout endpoint (3x in 2 days)
HYPOTHESES:
  H1 (60%): Downstream payment service rate-limiting us
  H2 (30%): Connection pool exhaustion under load
  H3 (10%): Kubernetes probe misconfiguration killing pods early
EXPERIMENTS:
  H1: Check payment service logs for 429 errors matching 503 windows
  H2: Monitor DB/HTTP connection pool metrics during 503 occurrence
  H3: Compare pod restarts with 503 timestamps in APM
EVIDENCE:     Payment service logs show 429s at matching timestamps
VERDICT:      H1 confirmed. H2 and H3 ruled out.
DECISION:     Exponential backoff + cache idempotent payment responses
```

## Research Source Hierarchy

Rate your sources — never treat all evidence equally.

| Tier | Source type | Trust | Examples |
|---|---|---|---|
| **P1** | Primary | Highest | Official docs, RFC/JEP, source code, production metrics |
| **P2** | Verified secondary | High | Peer-reviewed papers, official engineering blogs, well-cited books |
| **P3** | Community | Medium | StackOverflow (accepted answers), GitHub issues, team Slack |
| **P4** | Opinion | Low | Random blog posts, tweets, Reddit, Medium articles |
| **P5** | LLM output | Verify | AI assistant responses (always cross-check against P1/P2) |

> **Rule:** Never make an architectural decision based only on P3–P5.
> Always cross-reference with P1 or P2.

## Spike Story Format (Agile)

```yaml
Title:   Spike: Investigate <topic>
Goal:    Understand <specific unknown> so we can <decision this enables>
Timebox: <N hours/days maximum>
Questions to Answer:
  1. ...
  2. ...
Definition of Done:
  - Research summary written (≤ 2 pages)
  - Recommendation made (proceed / don't / needs more info)
  - Risks listed
  - ADR or brain note created (see `decision-records` skill)
Not in Scope: <explicitly exclude tangential topics>
```

## Workflow Prompts That Apply This Skill

Use these prompts when you want a structured, opinionated walk-through. Pick by
the *kind* of unknown you are investigating.

| Prompt | When to invoke | When NOT to invoke |
|---|---|---|
| `/learn-concept` | You don't yet have a mental model of an idea (e.g., "what is event sourcing?"). Best as the *first* step on an unfamiliar topic. | You already understand the concept and need depth — use `/deep-dive`. |
| `/deep-dive` | You know the basics and want progressive layers (intuition → mechanics → docs → real-world → edge cases). | You only need a one-liner — use `/learn-concept`. |
| `/learn-from-docs` | You need to extract structured knowledge from official documentation (specs, RFCs, API references). | The topic has no authoritative docs — fall back to `/deep-dive`. |
| `/explore-project` | The unknown is an open-source codebase's architecture or patterns. | The unknown is your *own* code — use `/code-analysis` instead. |
| `/code-analysis` | High-level review of a class/method/flow in your own codebase (structure, smells, design). | You need line-by-line trace — use `/code-analysis-deep-dive`. |
| `/code-analysis-deep-dive` | Line-by-line internals, data flow, call stack, virtual refactoring of your own code. Also for recent commit/PR impact analysis. | You only need a structural overview — use `/code-analysis`. |
| `/resources` | You want curated, vetted resources from the learning vault before going off-piste. | The topic isn't in the vault — search the web instead. |
| `/brain-new` | The investigation produced a decision worth capturing as a permanent note. | The output is transient (exploration scratch) — don't pollute the brain. |

> See also: `code-investigation` skill for the methodology behind `/code-analysis*`,
> and `decision-records` skill for what to write into `/brain-new`.
