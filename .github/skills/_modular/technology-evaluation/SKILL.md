---
name: technology-evaluation
description: >
  Evaluating and choosing technologies — libraries, frameworks, databases, queues,
  protocols, cloud services, build tools. Activates for: evaluate <tech>, compare
  <X> vs <Y>, technology radar, framework selection, library choice, build-vs-buy,
  feasibility study, technology deep-dive, vendor evaluation, POC plan, tech spike
  on a specific technology, weighted decision matrix, evaluation rubric.
  Use when the question is "should we adopt this technology?" or "which of these
  options is best for us?".
  Related skills: `investigation-methodology` (the underlying framework),
  `decision-records` (capturing the choice as an ADR), `requirements-research`
  (clarifying what the tech must satisfy).
---

# Technology Evaluation

## Core Question

> **Will this technology serve our use case at our scale, with our team, at acceptable cost?**

Technology choices are sticky — picking wrong is expensive to reverse. Evaluate
deliberately, against your context, not in the abstract.

## The Three-Phase Cadence

```text
LANDSCAPE (1 h)  → FUNDAMENTALS (2–4 h)  → HANDS-ON (1–2 d)
   ↓                    ↓                       ↓
"What is it,        "How does it           "Does it work for
 what competes,      really work?"          MY use case?"
 who uses it?"
```

Stop after any phase if the answer is already clear ("not a fit, moving on").

### Phase 1 — Landscape (≈ 1 hour)

- What category is this? (DB, queue, framework, protocol, …)
- What alternatives exist? Who uses each at scale?
- What is the primary use case?
- **Sources:** official overview, Wikipedia, ThoughtWorks Tech Radar, CNCF landscape.

### Phase 2 — Fundamentals (≈ 2–4 hours)

- Official docs: Quick Start + Concepts + Architecture
- Do the "Getting Started" tutorial — don't just read it
- List the 5–10 core abstractions the technology is built on
- **Sources:** official docs, official YouTube, original paper/spec.

### Phase 3 — Hands-on (≈ 1–2 days)

- Build the minimal prototype that exercises *your* core use case
- Deliberately break it — test the failure modes you'd encounter in production
- Benchmark if performance matters
- Read recent GitHub issues to see what real users hit
- Write the findings doc (see `decision-records` skill for format)

## Evaluation Rubric (Full)

Score each criterion 1–5; weight by your context.

| Category | Criterion | What to check |
|---|---|---|
| **Functional** | Feature completeness | Does it do what we need? Gap list? |
| **Functional** | Correctness | Known data-loss bugs? CVE history? |
| **Operational** | Performance | Latency, throughput, resource usage. Independent benchmarks? |
| **Operational** | Scalability | Horizontal/vertical limits? Sharding? |
| **Operational** | Reliability | HA support? Backup/restore? DR story? |
| **Security** | Auth & AuthZ | OAuth/OIDC? RBAC? CVE response time? |
| **Security** | Encryption | At rest, in transit? Compliance certifications? |
| **Ecosystem** | Maturity | 1.x or 0.x? GA or preview? |
| **Ecosystem** | Community | Contributors, issue age, Slack/Discord activity |
| **Ecosystem** | Commercial support | SLAs available? Vendor lock-in risk? |
| **Ecosystem** | Integrations | Works with your stack? SDK languages? |
| **Team** | Learning curve | Days/weeks to productive? |
| **Team** | Hiring pool | Talent market size? |
| **Financial** | License | OSS? Commercial? Usage-based? |
| **Financial** | Cost at scale | What does it cost at 10× current load? |

## Weighted Trade-Off Matrix

Use when comparing 2–4 options for a single decision.

```markdown
## Decision: <What are you choosing?>

| Criterion    | Weight | Option A | Option B | Option C |
|---           |---     |---       |---       |---       |
| Performance  |  25%   |    3     |    2     |    4     |
| Complexity   |  20%   |    2     |    4     |    2     |
| Team skills  |  30%   |    4     |    1     |    3     |
| Cost         |  25%   |    3     |    3     |    1     |
| **Weighted** |        | **2.95** | **2.45** | **2.80** |

### Recommendation
Option A — highest weighted score; team-skills advantage reduces delivery risk.

### Risks & Mitigations
- Option A risk: performance ceiling at 10K req/s → benchmark at 8K first.
```

> **Honesty rule:** if the weights are forcing a result you don't believe,
> the weights are wrong — not the result. Revise weights, don't reverse-engineer.

## Common Anti-Patterns

| Anti-pattern | Why it bites | Better approach |
|---|---|---|
| Hype-driven choice | The tech doesn't fit *your* scale or team | Score against your context, not Twitter |
| Single-criterion choice | "Fastest" might also be most complex | Use the full rubric |
| No hands-on | Docs lie about real-world friction | Always do Phase 3 |
| Comparing on equal weights | Hides what actually matters to *you* | Weight criteria deliberately |
| Skipping failure modes | You'll meet them in production | Deliberately break the POC |

## Workflow Prompts That Apply This Skill

| Prompt | When to invoke | When NOT to invoke |
|---|---|---|
| `/learn-concept` | Phase 1 landscape — you don't yet know what category the technology belongs to. | You already know the basics — jump to `/deep-dive`. |
| `/deep-dive` | Phase 2 fundamentals — progressive multi-layer exploration of the core abstractions. | You only need a one-sentence summary — `/learn-concept` is faster. |
| `/learn-from-docs` | Phase 2 fundamentals — extract structured knowledge from official specs/docs. | The technology has weak docs — fall back to `/deep-dive` or source code. |
| `/explore-project` | The technology *is* an open-source project and you want to study its internals (Phase 3 depth). | You're evaluating it for use, not contributing — `/deep-dive` is enough. |
| `/resources` | Find curated, vetted vault entries on the technology before going off-piste. | The vault has no coverage on this technology yet. |
| `/brain-new` | Capture the final evaluation outcome as an ADR (see `decision-records` skill). | The evaluation is mid-flight — wait until you have a decision. |

> **Sequencing tip:** `/learn-concept` → `/learn-from-docs` → `/deep-dive` → POC → `/brain-new` mirrors the three-phase cadence above.
