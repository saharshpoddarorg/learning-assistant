---
name: root-cause-analysis
description: Production incident root-cause analysis (RCA), post-mortems, and blameless retrospectives. Use when investigating a production bug, outage, regression, or recurring incident — covers evidence preservation, timeline construction, hypothesis elimination, the 5 Whys technique, fix verification, and blameless post-mortem authoring. Activates on keywords like RCA, root cause, post-mortem, postmortem, incident, outage, production bug, regression, 5 whys, timeline, blameless, retrospective, what went wrong.
---

# Root-Cause Analysis

> **Knowledge layer skill.** Methodology for investigating production incidents end-to-end —
> from "the alert just fired" to a written post-mortem with action items.
> For broader investigation methodology (hypothesis-driven framework, source hierarchy),
> see the `investigation-methodology` skill. For documenting the outcome, see `decision-records`.

## Core Question

> **What actually caused this — and how do we prevent the next one of its kind?**

RCA goes beyond "the bug is fixed" to identify the systemic, process, or design cause
so the same class of incident does not recur.

## The Six-Step RCA Process

| Step | Action | Output |
|---|---|---|
| 1. Preserve evidence | Export logs, metrics, traces for the incident window **before they rotate**. Snapshot heap/thread dumps, DB explain plans, config at incident time. | Archived evidence bundle |
| 2. Construct timeline | Build minute-by-minute (or second-by-second) timeline. Correlate with deployments, config changes, traffic, dependency incidents. | Timeline document |
| 3. Generate hypotheses | For each symptom, list ≥2 candidate causes. Apply the **5 Whys** to drill from symptom to root. | Hypothesis list |
| 4. Eliminate | Test each hypothesis against preserved evidence. Rule out — do not jump to the first plausible cause. | Surviving root cause(s) |
| 5. Fix & verify | Apply fix. Watch the **same metrics that were anomalous** return to baseline. Add a regression test. | Verified fix |
| 6. Post-mortem | Write up: what happened, timeline, root cause, fix, action items with owners and due dates. | Post-mortem doc |

## The 5 Whys — Minimal Pattern

Keep asking "why?" until you stop hitting code-level answers and reach a process, design,
or systemic cause. Stop when the next "why" would be philosophical, not actionable.

```text
Symptom: API returned 500s for 12 minutes.
Why? → Database connection pool was exhausted.
Why? → A slow query held connections for ~8s each.
Why? → A new index was dropped in last night's migration.
Why? → Migration review did not include a query-plan diff.
Root cause (actionable): No pre-merge query-plan diff in DB migration checklist.
```

## Blameless Post-Mortem Principles

| Do | Don't |
|---|---|
| Focus on systems, processes, missing guard-rails | Name individuals as the cause |
| Assume people did the best they could with available info | Assign blame for honest mistakes |
| Capture **what surprised you** — that's where the gap is | Skip surprises that "should have been obvious" |
| Generate action items with owners + deadlines | End with "we'll be more careful next time" |
| Share the post-mortem widely | Hide it from the team that caused it |

## Post-Mortem Document Skeleton

```text
# Incident YYYY-MM-DD — <short title>

## Summary           (3-5 sentences: what, when, impact, status)
## Impact            (users affected, revenue, SLO breach, duration)
## Timeline          (timestamped events, deploy/config correlations)
## Root Cause        (drilled via 5 Whys — process/design, not "the bug")
## Resolution        (fix applied, verification evidence)
## What Went Well    (detection time, rollback worked, comms)
## What Went Poorly  (gaps in alerts, runbooks, review process)
## Action Items      (table: item | owner | due date | tracking link)
```

## Anti-Patterns

| Anti-pattern | Why it fails |
|---|---|
| Stop at first plausible cause | Often a contributing factor, not the root — incident recurs |
| Skip evidence preservation | Logs/metrics rotate; you lose ability to test hypotheses |
| Treat the **bug fix** as the action item | Doesn't prevent the class of incident — file process/tooling items too |
| Name-and-shame post-mortem | Future incidents get hidden, not investigated |
| No timeline | Can't correlate cause-and-effect; hypotheses are guesses |
| Skip the "5 Whys" | Surface fix without systemic improvement |

## Workflow Prompts That Apply This Skill

| Prompt | When to invoke | When NOT to invoke |
|---|---|---|
| `/debug` | A specific bug/error is reproducible and needs hypothesis-driven debugging | Pure outage triage with no reproducible case — do timeline + evidence first |
| `/code-analysis-deep-dive` | Root cause sits in a specific class/method and you need to understand it deeply | Incident is infra/config, not code logic |
| `/brain-new` | Capturing the post-mortem document as a permanent brain note | Triage is still in progress — wait until fix is verified |
| `/explore-project` | Need to locate the suspect module/service in an unfamiliar codebase | You already know the affected component |

## Related Skills

- `investigation-methodology` — hypothesis-driven framework underpins steps 3-4
- `decision-records` — ADR format for "we are changing X to prevent recurrence" follow-ups
- `code-investigation` — when the root cause lives in code that needs deep reading
