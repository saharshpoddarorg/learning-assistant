---
name: decision-records
description: Documenting technical decisions and spike outcomes as durable artifacts — Architecture Decision Records (ADRs) and Spike Findings docs. Use when capturing WHY a technical choice was made (architecture, library, pattern, framework), recording spike/POC outcomes, superseding a prior decision, or building an ADR log. Activates on keywords like ADR, architecture decision, decision record, spike findings, spike outcome, technical decision, record decision, capture decision, document architecture, decision log.
---

# Decision Records

> **Knowledge layer skill.** Templates and conventions for capturing technical decisions
> (ADRs) and spike outcomes (Spike Findings) as durable, searchable artifacts.
> For the upstream evaluation work, see `technology-evaluation`. For the post-mortem
> companion format, see `root-cause-analysis`.

## Core Question

> **Six months from now, will someone reading this know WHY we chose this — and what we rejected?**

A good decision record captures **context + decision + consequences + rejected alternatives**.
Without rejected alternatives it's just a changelog entry.

## When to Write Which Doc

| Doc | Use when |
|---|---|
| **ADR** | A binding architecture / design / tooling choice was made (library, pattern, framework, protocol, schema convention). Lives forever; superseded, not deleted. |
| **Spike Findings** | A time-boxed investigation finished and produced a recommendation (proceed / don't / need more info). May feed into an ADR. |
| Neither | The change is reversible in minutes and has no design implications (e.g., bumping a patch version). |

## ADR — Architecture Decision Record

```markdown
---
date: YYYY-MM-DD
kind: decision
project: <project>
tags: [architecture, decision, <technology>]
status: Proposed | Accepted | Deprecated | Superseded by ADR-XXX
---

# ADR-NNN: <Short decision title>

## Context
What situation forces a decision? What constraints are in play
(SLOs, team skills, deadlines, existing systems)?

## Decision
What was decided? State it as a single declarative sentence.

## Consequences
What becomes easier? What becomes harder? What new obligations,
risks, or follow-up work does this create?

## Alternatives Considered
For each rejected option: what it was and why it lost.
Without this section the ADR has no teeth.
```

### ADR Status Lifecycle

```text
Proposed  →  Accepted  →  Deprecated
                ↓
            Superseded by ADR-XXX   (never delete — link forward)
```

### ADR Numbering

- Sequential, zero-padded: `ADR-001`, `ADR-002`, …
- Filename mirrors title: `ADR-014-use-postgres-for-event-store.md`
- Never reuse a number, even if an ADR is deleted in draft

## Spike Findings Doc

```markdown
---
date: YYYY-MM-DD
kind: session
tags: [spike, <topic>, research]
status: final
---

# Spike: <Topic>

## Goal
Understand [X] to decide [Y]. Timebox: [N days].

## TL;DR
One paragraph: what you found and your recommendation.

## Findings
### What we investigated
### What we learned
### What surprised us            ← often the most valuable section
### What we did not investigate  ← explicit out-of-scope

## Recommendation
[Proceed | Don't proceed | Need more investigation] — because [reason].

## Risks & Open Questions

## Follow-up Actions
(checkbox list with owners)
```

## Anti-Patterns

| Anti-pattern | Why it fails |
|---|---|
| Omit "Alternatives Considered" | Reader can't tell if rejection was deliberate or unaware |
| Delete a superseded ADR | Loses the history of why we changed our mind |
| Mix decision + implementation log | ADRs become noise; keep "how we built it" separate |
| Write the ADR after the system ships | No longer a decision — becomes a justification |
| Spike doc with no recommendation | Defeats the purpose of timeboxing the investigation |
| Skip "What surprised us" in spike | Surprises are the highest-signal findings — name them |

## Filing & Discoverability

- Store ADRs in a single folder (`docs/adr/` or brain `notes/decisions/`)
- Maintain an `ADR index` README listing number, title, status, supersession links
- Tag spike findings with the originating ticket/epic for traceability
- Cross-reference: ADR links back to the spike findings that informed it

## Workflow Prompts That Apply This Skill

| Prompt | When to invoke | When NOT to invoke |
|---|---|---|
| `/brain-new` | Capturing an ADR or spike findings into the brain as a permanent note | The decision is still being debated — capture in `inbox/` first |
| `/check-standards` | Verifying brain-note frontmatter / naming on a freshly written ADR | The doc is not yet ready for "final" status |
| `/deep-dive` | Need to revisit a topic before writing the ADR's "Alternatives Considered" | All alternatives are already understood |
| `/learn-from-docs` | Pulling official source material to cite as evidence in Context | The ADR is internal-only (e.g., codebase convention) |

## Related Skills

- `technology-evaluation` — produces the rubric output that informs ADR Context + Alternatives
- `investigation-methodology` — spike stories feed Spike Findings docs
- `root-cause-analysis` — post-mortems often trigger ADRs ("we are now requiring X")
