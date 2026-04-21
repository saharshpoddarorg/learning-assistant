---
date: YYYY-MM-DD
time: "HH:MM AM/PM"
kind: session-capture
domain: work | personal
category: debugging
project: project-name
subject: kebab-case-subject-slug
tags: [tag1, tag2, tag3]
  # Tag guidance: 3-7 tags. Include project:<name> when project-scoped.
  # Mix activity tags (debugging, rca) + technology tags (java, spring-boot).
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
debug-target:
  service: null            # affected service/component (kebab-case), e.g. order-service
  error: null              # error type or symptom, e.g. NullPointerException
  issue: null              # linked issue tracker ID (optional), e.g. JIRA-1234
---

# Debugging — <ServiceName>: <Error/Symptom>

> **Context:** Brief 1-2 sentence context — what error or unexpected behaviour was
> observed, where, and what impact it has.

---

## Problem Statement

| Property | Value |
|---|---|
| Service | `service-name` |
| Error | `ErrorType` or symptom description |
| Impact | severity / frequency / scope |
| Issue | `JIRA-1234` or N/A |
| Environment | dev / staging / production |

---

## Symptoms Observed

<!-- What was seen — error messages, logs, stack traces, unexpected behaviour. -->

```text
<!-- Paste relevant stack trace or error output here -->
```

---

## Hypothesis-Driven Investigation

### Hypothesis 1 — <Description>

- **Theory:** What might be causing the issue
- **Evidence for:** What supports this hypothesis
- **Evidence against:** What contradicts it
- **Verdict:** Confirmed / Rejected / Partial

### Hypothesis 2 — <Description>

- **Theory:** What might be causing the issue
- **Evidence for:** What supports this hypothesis
- **Evidence against:** What contradicts it
- **Verdict:** Confirmed / Rejected / Partial

---

## Root Cause

<!-- The confirmed root cause, explained clearly. -->

---

## Fix Applied

<!-- What was changed to fix the issue. Include code snippets if applicable. -->

---

## Prevention Measures

<!-- What should be done to prevent this class of bug from recurring. -->

- [ ] Prevention measure 1
- [ ] Prevention measure 2

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

## Cross-References

<!-- Related sessions in other categories or scopes. Update bidirectionally.
     Relationships: origin, spawned, narrows, widens, related, implements.
     See .github/instructions/session-scoping.instructions.md for details. -->

| Relationship | Session | Note |
|---|---|---|
| | | |

---

## Session Metadata

| Property | Value |
|---|---|
| Duration | ~X exchanges |
| Files touched | `file1.java`, `file2.md` |
| Related sessions | none |
