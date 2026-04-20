---
date: YYYY-MM-DD
time: "HH:MM AM/PM"
kind: session-capture
domain: work | personal
category: code-analysis | code-review
project: project-name
subject: kebab-case-subject-slug
tags: [tag1, tag2, tag3]
  # Tag guidance: 3-7 tags. Include project:<name> when project-scoped.
  # Mix activity tags (code-analysis, refactoring) + technology tags (java, spring-boot).
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
code-target:
  class: null              # Java/TS class name (PascalCase as-is), e.g. OrderService
  method: null             # method name (camelCase as-is), e.g. calculateTotal
  package: null            # optional: full package path, e.g. com.example.order
  file: null               # optional: source file path, e.g. src/order/OrderService.java
---

# Code Analysis — <ClassName>.<methodName> (or Class Overview)

> **Context:** Brief 1-2 sentence context — what code is being reviewed, why, and
> what triggered the analysis (code review, refactoring, bug hunt, etc.).

---

## Target Code

| Property | Value |
|---|---|
| Class | `ClassName` |
| Method | `methodName` (or "class-level" for full class review) |
| Package | `com.example.package` |
| File | `src/path/to/File.java` |

---

## Intent & Purpose

<!-- WHY is this code being analysed? What question are we trying to answer? -->

- **Intent:** What the analysis aims to discover or validate
- **Trigger:** What prompted this review (PR, bug, refactoring, learning)

---

## Analysis

<!-- The substantive code analysis. Use H3/H4 headings for structure. -->

### Code Structure

<!-- Overview of how the code is organized — classes, methods, dependencies -->

### Findings

<!-- Specific observations — code smells, patterns, bugs, improvements -->

| # | Finding | Severity | Category |
|---|---|---|---|
| 1 | Finding description | high/medium/low | smell/bug/pattern/performance |
| 2 | Finding description | high/medium/low | smell/bug/pattern/performance |

### Proposed Changes

<!-- Concrete refactoring or fix proposals -->

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
