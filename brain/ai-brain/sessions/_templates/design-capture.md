---
date: YYYY-MM-DD
time: "HH:MM AM/PM"
kind: session-capture
domain: work | personal
category: design | feature-exploration
project: project-name
subject: kebab-case-subject-slug
tags: [tag1, tag2, tag3]
  # Tag guidance: 3-7 tags. Include project:<name> when project-scoped.
  # Mix activity tags (architecture, api-design) + technology tags (java, spring-boot).
status: draft
version: 1
parent: null
complexity: high | medium
outcomes: []
source: copilot
scope: global | project | feature
scope-project: null
scope-feature: null
scope-transitions: []
scope-refs: []
design-target:
  component: null          # component or feature name (kebab-case), e.g. payment-gateway
  aspect: null             # aspect: api-design, schema, security, performance, patterns, etc.
  level: null              # hld or lld
---

# Design — <ComponentName>: <Aspect>

> **Context:** Brief 1-2 sentence context — what is being designed, at what level
> (HLD/LLD), and what prompted the design work.

---

## Component Overview

| Property | Value |
|---|---|
| Component | `component-name` |
| Aspect | `api-design` / `schema` / `security` / `performance` / `patterns` / etc. |
| Level | HLD / LLD |
| Related Components | list of affected components |

---

## Intent & Purpose

<!-- WHY is this design being created? What problem does it solve? -->

- **Intent:** What the design aims to achieve
- **Constraints:** Key technical or business constraints
- **Success criteria:** How we'll know the design is good

---

## Approach / Proposal

<!-- The main design proposal. Use H3/H4 headings for sub-aspects. -->

### Option A — <Name>

<!-- Description, pros, cons -->

| Pros | Cons |
|---|---|
| Pro 1 | Con 1 |
| Pro 2 | Con 2 |

### Option B — <Name>

<!-- Description, pros, cons -->

| Pros | Cons |
|---|---|
| Pro 1 | Con 1 |
| Pro 2 | Con 2 |

### Recommendation

<!-- Which option was chosen, and why. Link to ADR if applicable. -->

---

## Use Cases / Flows

<!-- Key use cases or flow diagrams the design must support. -->

### Use Case 1 — <Name>

<!-- Actor, preconditions, steps, postconditions -->

### Use Case 2 — <Name>

<!-- Actor, preconditions, steps, postconditions -->

---

## Acceptance Criteria

<!-- Measurable criteria that validate the design is complete and correct. -->

- [ ] Criterion 1
- [ ] Criterion 2
- [ ] Criterion 3

---

## Key Outcomes

- Outcome 1
- Outcome 2
- Outcome 3

---

## Follow-Up / Next Steps

- [ ] Action item 1
- [ ] Action item 2

---

## Session Metadata

| Property | Value |
|---|---|
| Duration | ~X exchanges |
| Files touched | `file1.java`, `file2.md` |
| Related sessions | none |
