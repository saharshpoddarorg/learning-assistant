---
name: deep-research
description: >
  Deep investigation, research, and analysis methodology for software engineering contexts.
  Covers hypothesis-driven research, requirements gathering and analysis, technical
  investigation workflows, evidence synthesis, root-cause analysis, feasibility studies,
  competitive analysis, technology evaluation, and decision documentation.
  Use when asked about: how to research a topic thoroughly, how to investigate an unknown
  problem or technology, how to gather requirements, how to evaluate a technical decision,
  how to write a spike/POC investigation plan, how to analyse trade-offs, how to document
  research findings, or any request involving systematic investigation before implementation.
  Also activates for: research plan, investigation framework, technical assessment,
  feasibility study, technology radar, spike story, discovery session, due diligence.
---

# Deep Research & Investigation Skill

> **Domain:** Software Engineering > Cross-Cutting Methodology
> **Applies to:** Any role — Developer, PO, Architect, QA, Tech Lead
> **Related skills:** `requirements-research`, `software-development-roles`

---

## Tier 1 — Newbie: Investigation Quick-Start

### The 5-Step Investigation Loop

```
1. DEFINE    → What exactly is the unknown? Write one clear question.
2. HYPOTHESIZE → What do you already believe? State your best guess.
3. GATHER    → Where will you look? List 3-5 sources before you start.
4. SYNTHESIZE → What does the evidence say? Summarise in your own words.
5. DECIDE    → What's your conclusion? What action follows?
```

### Quick-Start Commands (Copilot)

```
/deep-dive       → <topic>          # Progressive multi-layer Copilot exploration
/learn-from-docs → <technology>     # Official docs parsing
/resources       → search → <topic> # Curated resource vault lookup
/brain-new       → decision-<topic> → notes   # Capture your decision
```

### When to Research vs When to Code

| Situation | Action |
|---|---|
| "I don't know if this library exists" | 30-min research spike first |
| "I know the problem; I know the solution" | Code directly |
| "The requirement is unclear" | Requirements investigation (→ `requirements-research` skill) |
| "Two approaches seem equally valid" | Trade-off analysis research |
| "There's a production bug I can't reproduce" | Root-cause investigation |

---

## Tier 2 — Amateur: Research Methodologies

### Research Types & When to Use Each

| Type | Use when | Output |
|---|---|---|
| **Spike / POC** | Technical unknown before a feature | 1-2 day prototype + findings doc |
| **Requirements Investigation** | Goal/scope unclear | Requirements doc, user stories |
| **Trade-off Analysis** | Choosing between viable options | Decision matrix + ADR |
| **Root-Cause Analysis** | Production issue, regression | RCA doc with timeline + fix |
| **Competitive Analysis** | Evaluating tools/libraries | Comparison table + recommendation |
| **Feasibility Study** | "Can we even do this?" | Feasibility report with risk assessment |

### Hypothesis-Driven Research Framework (HDF)

Every investigation should start with explicit hypotheses. This prevents scope creep
and makes findings verifiable.

```
TRIGGER      What question prompted this investigation?
HYPOTHESES   List 2-4 candidate explanations or answers, ranked by believability
EXPERIMENTS  For each hypothesis, what would prove/disprove it?
EVIDENCE     What you found (data, code, docs, tests)
VERDICT      Which hypothesis survived? What did you learn?
DECISION     What action (if any) follows?
```

#### Example: "Our API sometimes returns 503 — why?"

```
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
DECISION:     Implement exponential backoff + cache payment responses for idempotent calls
```

### Research Source Hierarchy

Rate your sources — don't treat all evidence equally.

| Tier | Source type | Trust level | Examples |
|---|---|---|---|
| **P1** | Primary source | Highest | Official docs, RFC/JEP, source code, production metrics |
| **P2** | Verified seconday | High | Published research papers, official blog posts, well-cited books |
| **P3** | Community | Medium | StackOverflow (accepted answers), GitHub issues, team Slack |
| **P4** | Opinion | Low | Blog posts, tweets, Reddit, Medium articles |
| **P5** | LLM output | Verify required | Copilot responses (always verify P1/P2 against these) |

> **Rule:** Never make an architecture decision based only on P3-P5 sources.
> Always cross-reference with P1 or P2.

### Trade-Off Analysis Template

Use when comparing 2-4 options for a decision:

```markdown
## Decision: <What are you choosing?>

### Options
| Criterion    | Option A | Option B | Option C | Weight |
|---|---|---|---|---|
| Performance  |    ⭐⭐⭐   |   ⭐⭐    |   ⭐⭐⭐⭐  |  25%   |
| Complexity   |    ⭐⭐    |   ⭐⭐⭐⭐  |   ⭐⭐    |  20%   |
| Team skills  |    ⭐⭐⭐⭐  |   ⭐     |   ⭐⭐⭐   |  30%   |
| Cost         |    ⭐⭐⭐   |   ⭐⭐⭐   |   ⭐      |  25%   |
| **Weighted** | **2.95** | **2.45** | **2.80** |        |

### Recommendation
Option A — highest weighted score; team skills advantage reduces risk.

### Risks & Mitigations
- Option A risk: Performance ceiling at 10K req/s → mitigation: benchmark at 8K
```

### Spike Story Format (Agile)

```
Title: Spike: Investigate <topic>
Goal: Understand <specific unknown> so we can <decision this enables>
Timebox: <N hours/days maximum>
Questions to Answer:
  1. ...
  2. ...
Definition of Done:
  - Research summary written (≤2 pages)
  - Recommendation made (proceed/don't/requires more info)
  - Risks listed
  - ADR or brain note created
Not in Scope: <explicitly exclude tangential topics>
```

---

## Tier 3 — Pro: Advanced Investigation Patterns

### Deep Dive Methodology for Unknown Technologies

When investigating a technology you've never used:

```
Phase 1 — LANDSCAPE (1h)
  - What category does this technology belong to? (DB, queue, framework, protocol?)
  - What alternatives exist? What does the ecosystem look like?
  - What is the primary use case and who uses it at scale?
  Sources: Official website overview, Wikipedia, ThoughtWorks Tech Radar, CNCF landscape

Phase 2 — FUNDAMENTALS (2-4h)
  - Official documentation (Quick Start + Concepts + Architecture)
  - "Getting Started" tutorial (do it, don't just read it)
  - Fundamental abstractions: what are the core 5-10 concepts this tech is built on?
  Sources: Official docs, official YouTube channel, original paper/spec if available

Phase 3 — DEPTH (1-2 days)
  - Source code reading (understand internals, not just API)
  - Real production case studies (how do others use this at scale?)
  - Failure modes: what goes wrong? How is it debugged? What are the anti-patterns?
  - Community health: GitHub stars/forks/issues age, maintainer responsiveness
  Sources: GitHub repo issues, company engineering blogs, conference talks (InfoQ, QCon)

Phase 4 — HANDS-ON (1-2 days)
  - Build a minimal prototype that exercises the core use case
  - Deliberately break it (test failure modes)
  - Benchmark if performance matters
  - Write your findings doc
```

### Technology Evaluation Criteria (Full Rubric)

| Category | Criterion | What to check |
|---|---|---|
| **Functional** | Feature completeness | Does it do what we need? Gap list? |
| **Functional** | Correctness | Known bugs? Data loss risks? |
| **Operational** | Performance | Latency, throughput, resource usage. Benchmarks available? |
| **Operational** | Scalability | Horizontal/vertical? Limits? Sharding support? |
| **Operational** | Reliability | HA support? Backup/restore? Disaster recovery? |
| **Security** | Auth & AuthZ | OAuth/OIDC? RBAC? CVE history? |
| **Security** | Encryption | At rest, in transit? Compliance? |
| **Ecosystem** | Maturity | Version 1.x or 0.x? GA or preview? |
| **Ecosystem** | Community | GitHub stars, contributors, Slack/Discord activity |
| **Ecosystem** | Support | Commercial support available? SLAs? |
| **Ecosystem** | Integrations | Works with your stack? SDK languages? |
| **Team** | Learning curve | How long to productive? Prior team experience? |
| **Team** | Hiring pool | Talent market size? Certification availability? |
| **Financial** | License | Open source? Commercial? Usage-based? |
| **Financial** | Cost at scale | What does it cost at 10x current load? |

### Root-Cause Analysis (Production Issues)

```
Step 1 — PRESERVE EVIDENCE
  Export logs, metrics, traces for the incident window before they rotate.
  Snapshot state (heap dumps, thread dumps, DB explain plans).

Step 2 — TIMELINE CONSTRUCTION
  Build a timeline of events with exact timestamps.
  "At 14:32:01 — first error in logs"
  "At 14:32:15 — alert fired"
  "At 14:33:00 — on-call acknowledged"
  Correlate with recent deployments, config changes, traffic spikes.

Step 3 — HYPOTHESIS GENERATION
  For each symptom, generate ≥2 explanations.
  Use the "5 Whys" technique: keep asking "why" until you reach root cause.

Step 4 — HYPOTHESIS ELIMINATION
  Test each hypothesis against the preserved evidence.
  Rule out until only the root cause remains.

Step 5 — FIX & VERIFY
  Apply fix. Observe the same metrics that were anomalous — confirm resolution.
  Write regression test if applicable.

Step 6 — POST-MORTEM
  Document: what happened, timeline, root cause, fix, follow-ups.
  Blameless: focus on process/systems, not individuals.
  Action items: with owners and deadlines.
```

### Research Documentation Patterns

**Architecture Decision Record (ADR)** — for technical decisions:
```markdown
---
date: YYYY-MM-DD
kind: decision
project: <project>
tags: [architecture, decision, <technology>]
status: final
---

# ADR: <Short decision title>

## Status: Accepted | Proposed | Deprecated | Superseded by ADR-XXX

## Context
What is the situation that requires a decision? What forces are at play?

## Decision
What was decided?

## Consequences
What are the trade-offs? What become easier? What becomes harder?

## Alternatives Considered
Why were the alternatives rejected?
```

**Spike Findings Doc** — after a spike story:
```markdown
---
date: YYYY-MM-DD
kind: session
tags: [spike, <topic>, research]
status: final
---

# Spike: <Topic>

## Goal
Understand [X] to decide [Y].

## TL;DR
[One paragraph summary of what you found and your recommendation]

## Findings
### What we investigated
### What we learned
### What surprised us
### What we did not investigate (out of scope)

## Recommendation
[Proceed | Don't proceed | Needs more investigation] because [reason].

## Risks & Open Questions
## Follow-up Actions
```

### Copilot-Assisted Research Workflow

Combine Copilot commands to build a complete research workflow:

```
Step 1  /learn-concept → <topic>        # Concept fundamentals
          ↓
Step 2  /deep-dive → <topic>            # Progressive layers
          ↓
Step 3  /resources → search → <topic>  # Curated resources
          ↓
Step 4  /learn-from-docs → <official>  # Official source
          ↓
Step 5  /design-review (with POC code) # Architecture check
          ↓
Step 6  /brain-new → decision-<topic>  # Capture ADR/findings
          ↓
Step 7  /check-standards → brain-naming # Verify our note
```

**Prompts for Copilot research assistance:**
```
"Research [topic] using the hypothesis-driven framework. 
 List 3 hypotheses, evidence sources per hypothesis, 
 and a weighted trade-off table for [options A, B, C]."

"Perform a technology evaluation of [tech] using the full 
 rubric: functional, operational, security, ecosystem, team, financial."

"Build a research plan for a spike on [topic]. 
 Timebox: 2 days. Questions to answer: [...]"
```
