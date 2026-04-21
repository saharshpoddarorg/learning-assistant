---
date: YYYY-MM-DD
time: "HH:MM AM/PM"
kind: session-capture
domain: work | personal
category: code-analysis
project: project-name
subject: kebab-case-subject-slug
tags: [tag1, tag2, tag3]
  # Tag guidance: 3-7 tags. Include project:<name> when project-scoped.
  # Use deep-dive + activity tags (code-analysis, internals, flow) + tech tags (java, spring-boot).
status: draft
version: 1
parent: null
complexity: high
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
deep-dive:
  level: method | class | feature   # scope of the deep-dive
  focus: internals | flow | state | recent-changes | all   # what aspect to emphasize
recent-changes:                      # populated when focus includes recent-changes
  enabled: false                     # set to true when analysing recent commit impact
  vcs-source: git | bitbucket        # where commits were read from (local git or Bitbucket API)
  commit-range: null                 # e.g. "abc123..HEAD" or "last 5 commits" or PR ID
  pr-id: null                        # Bitbucket PR ID if applicable
  bitbucket-project: null            # Bitbucket project key (e.g. IESD)
  bitbucket-repo: null               # Bitbucket repo slug (e.g. iesd-26)
  jira-issues: []                    # Jira keys extracted from commits (e.g. [PROJ-123, PROJ-456])
  confluence-pages: []               # Confluence page IDs fetched from Jira links (e.g. [12345678])
---

# Code Deep-Dive — <ClassName>.<methodName> (or Class/Feature Overview)

> **Context:** Brief 1-2 sentence context — what code is being deep-dived, why the user
> wants to understand it (learning, onboarding, pre-refactoring, code review prep, etc.).

---

## Target Code

| Property | Value |
|---|---|
| Class | `ClassName` |
| Method | `methodName` (or "class-level" / "feature-level") |
| Package | `com.example.package` |
| File | `src/path/to/File.java` |
| Deep-Dive Level | method / class / feature |

---

## 1. High-Level Overview

<!-- PURPOSE: What does this code do? What is its responsibility in the system?
     Keep this section to 3-5 sentences maximum. -->

- **Purpose:** What this code exists to do — one sentence
- **Responsibility:** Where it sits in the architecture (layer, module, domain)
- **Design Role:** Pattern it implements (e.g., Service, Repository, Strategy, Handler)
- **Callers:** Who calls this code? What triggers its execution?

---

## 2. Data Flow

<!-- INPUTS → TRANSFORMATIONS → OUTPUTS. Trace what data enters, how it changes,
     and what comes out. Include types. -->

### Inputs

| Parameter / Source | Type | Description |
|---|---|---|
| `paramName` | `Type` | What this input represents |

### Transformations

<!-- Describe each major transformation step. Use numbered steps or a flow diagram. -->

1. Step 1 — what happens first
2. Step 2 — what happens next
3. Step 3 — final transformation

### Outputs

| Return / Side-Effect | Type | Description |
|---|---|---|
| Return value | `Type` | What the caller receives |
| Side-effect | Description | External state changes (DB, cache, file, event) |

---

## 3. Call Stack / Method Flow

<!-- WHO CALLS WHAT. Trace the sequence of method calls from entry point through
     all significant internal calls. Use indentation to show nesting. -->

```text
EntryPoint.method()
  → ClassA.methodOne()
    → ClassB.methodTwo()
      → ClassC.helperMethod()
    → ClassB.methodThree()
  → ClassA.methodFour()
```

### Call Sequence Detail

| # | Caller | Callee | Purpose | Returns |
|---|---|---|---|---|
| 1 | `EntryPoint.method()` | `ClassA.methodOne()` | Description | `Type` |
| 2 | `ClassA.methodOne()` | `ClassB.methodTwo()` | Description | `Type` |

---

## 4. Code Block Breakdown

<!-- FUNCTIONAL COHESION. Split the code into logical blocks based on what each
     section does. Each block gets a name, line range, and explanation. -->

### Block 1 — <Block Name> (lines X-Y)

```java
// paste the relevant code block here
```

**What it does:** Explanation of this block's purpose and logic.

**Why it's here:** How this block fits into the overall method/class flow.

### Block 2 — <Block Name> (lines X-Y)

```java
// paste the relevant code block here
```

**What it does:** Explanation of this block's purpose and logic.

**Why it's here:** How this block fits into the overall method/class flow.

<!-- Repeat for each functional block. Aim for 3-8 blocks per method. -->

---

## 5. Line-by-Line Walkthrough

<!-- DETAILED ANALYSIS of key logic lines. Skip boilerplate (imports, getters/setters,
     standard constructor assignments). Focus on lines where decisions happen,
     algorithms execute, or subtle behaviour occurs. -->

| Line | Code | Explanation |
|---|---|---|
| 42 | `if (order.isValid())` | Guards against processing invalid orders — delegates to `Order.isValid()` |
| 43 | `var total = items.stream()...` | Streams over order items to calculate subtotal |
| 47 | `total = applyDiscount(total, order.getDiscount())` | Applies percentage discount — can return negative if discount > 100% (bug?) |

---

## 6. State Changes

<!-- HOW STATE EVOLVES. Track fields, local variables, and external state through
     the execution. Use a table or timeline. -->

| Point in Execution | Variable / Field | Before | After | Why |
|---|---|---|---|---|
| After line 42 | `isProcessing` | `false` | `true` | Guard passed, processing begins |
| After line 47 | `total` | `100.00` | `90.00` | 10% discount applied |

---

## 7. Edge Cases & Error Paths

<!-- WHAT CAN GO WRONG. Enumerate edge cases, exception paths, and boundary
     conditions. For each, explain what happens. -->

| Edge Case | Input / Condition | Behaviour | Handled? |
|---|---|---|---|
| Null input | `order == null` | `NullPointerException` at line 42 | No — missing null check |
| Empty items | `order.getItems().isEmpty()` | Returns 0.0 | Yes — stream returns identity |
| Negative discount | `discount > 1.0` | Negative total returned | No — not validated |

---

## 8. Dependencies & Coupling

<!-- WHAT THIS CODE DEPENDS ON, AND WHAT DEPENDS ON IT. -->

### Depends On (outgoing)

| Dependency | Type | Coupling | Notes |
|---|---|---|---|
| `OrderRepository` | Interface | Loose | Injected via constructor |
| `DiscountService` | Concrete class | Tight | Direct method call — not abstracted |

### Depended On By (incoming)

| Dependent | How | Notes |
|---|---|---|
| `OrderController` | Calls `processOrder()` | Entry point from HTTP layer |
| `BatchProcessor` | Calls `processOrder()` in loop | Batch job — performance-sensitive |

---

## 9. Recent Changes Impact Analysis

<!-- RECENT COMMITS / PRs. Populated when deep-dive.focus includes recent-changes.
     Analyses how recent commits affected the target code — what changed, what was
     impacted, and what risks the changes introduce.
     Skip this section entirely if recent-changes analysis was not requested. -->

### Commit / PR Summary

| # | Commit / PR | Author | Date | Jira Key | Summary |
|---|---|---|---|---|---|
| 1 | `abc1234` or PR #42 | Author name | YYYY-MM-DD | PROJ-123 or — | One-line description |

### Change Intent & Context

<!-- For each commit/PR with a Jira key, synthesise the intent, purpose, and approach
     from the Jira issue and any linked Confluence pages. This section explains WHY the
     code changed — the business motivation, acceptance criteria, and design decisions.
     Skip this subsection if no Jira keys were found. -->

#### Commit 1 — PROJ-123: <Jira Summary>

**Intent (from Jira):** What the Jira issue describes as the goal / problem statement.

**Acceptance Criteria (from Jira):**

- AC1: Expected behaviour or requirement
- AC2: Edge case or constraint to handle

**Design Approach (from Confluence):**
Page: "<Page Title>" (page ID: NNNNN)

- Chosen approach and rationale
- Rejected alternatives and why
- Key constraints or non-functional requirements

**Algorithm Context (from Confluence or Jira description):**
Relevant algorithm details, data flow expectations, or processing rules
documented in linked design pages.

**PR Review Feedback (from Bitbucket PR comments):**

- Key reviewer questions and answers that explain design choices

<!-- When no Jira/Confluence context is available: -->

#### Commit N — No Jira Key: <Commit Summary> (`sha`)

**Intent (from commit message):** Inferred intent based on commit message and diff.

**Context:** No Jira issue linked. Analysis based on commit message and code diff only.

---

### Changes to Target Code

<!-- For each relevant commit, show WHAT changed in the target class/method/feature.
     Focus on: new/removed/modified lines, signature changes, logic changes.
     When Jira context is available, annotate whether the change aligns with ACs. -->

#### Commit `abc1234` — <Summary>

```diff
- old line of code
+ new line of code
```

**What changed:** Describe the modification in terms of the code's purpose.

**Why it changed:** Commit message context, PR description, or inferred reason.

**Aligns with intent:** Yes/No — reference specific Jira AC if available.

### Impact Assessment

<!-- Analyse HOW the recent changes impact the target code's behaviour.
     Map each change to its effect on the dimensions from earlier sections. -->

| Change | Intent (from Jira) | Algorithm Impact | Data / Variables Impact | Flow Impact | Risk | Intent Aligned? |
|---|---|---|---|---|---|---|
| Description of change | Jira summary or — | How the algorithm was affected | Fields, variables, types changed | Call chain / control flow changes | Regression risk level | ✅ / ❌ / N/A |

### Variable / Field Impact Detail

<!-- When the user is concerned about data impact, trace each variable or field
     that was added, removed, renamed, or had its type/usage changed. -->

| Variable / Field | Before | After | Affected Methods | Ripple Risk |
|---|---|---|---|---|
| `fieldName` | Previous type / value / usage | New type / value / usage | Methods that read/write this | low / medium / high |

### Flow Impact Detail

<!-- When the user is concerned about flow impact, show how the call chain,
     control flow, or data pipeline changed. -->

```text
Before:
  EntryPoint.method()
    → ClassA.oldFlow()
      → ClassB.helper()

After:
  EntryPoint.method()
    → ClassA.newFlow()          ← CHANGED
      → ClassC.newHelper()      ← NEW dependency
      → ClassB.helper()         ← still called, but order changed
```

### Regression Risks

<!-- Identify specific regression risks introduced by the recent changes. -->

| Risk | Source Change | Affected Area | Severity | Jira AC Status | Mitigation |
|---|---|---|---|---|---|
| Description | Which commit/line introduced this risk | What could break | high / medium / low | AC met / at risk / N/A | How to verify or prevent |

### Data Source

<!-- How were the recent changes gathered? List all tools and sources used. -->

| Property | Value |
|---|---|
| VCS Source | git / Bitbucket API |
| Commit Range | `abc123..HEAD` or PR #42 |
| Bitbucket Project | IESD (if applicable) |
| Bitbucket Repo | iesd-26 (if applicable) |
| Jira Issues Fetched | PROJ-123, PROJ-456 (or — if none) |
| Confluence Pages Fetched | Page ID 12345 "Page Title" (or — if none) |
| Commands Used | `git log`, `fetch_bitbucket_pr_diff`, `fetch_jira_issue`, `fetch_confluence_page`, etc. |

---

## 10. Key Takeaways

<!-- SUMMARY for future reference. What did you learn? What's important to remember? -->

- Takeaway 1
- Takeaway 2
- Takeaway 3

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
| Files analysed | `file1.java`, `file2.java` |
| Lines covered | ~X lines of source code |
| Related sessions | none |
