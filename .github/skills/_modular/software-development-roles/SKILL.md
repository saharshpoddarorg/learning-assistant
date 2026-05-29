---
name: software-development-roles
description: >
  Role-based guidance for Product Owner (PO), Developer, and QA/Tester roles.
  Activates on: PO workflow, product owner, developer workflow, QA strategy, test types,
  sprint roles, three amigos, acceptance criteria demo, backlog management, bug reports,
  cognitive mode switch, role collaboration, shift-left testing, WSJF, OKRs backlog.
  Delegates to: git-vcs (branching conventions).
---

# Software Development Roles

## The Three Roles at a Glance

| Role | Primary Question | Key Artifact | Definition of "Done" |
|---|---|---|---|
| **Product Owner** | *What* should we build and *why*? | Product backlog · User stories · Acceptance criteria | Story accepted by PO after demo |
| **Developer** | *How* should we build it? | Source code · Design docs · Pull requests | Code reviewed, tests pass, deployed to staging |
| **QA / Tester** | *Does* it work correctly? | Test plan · Bug reports · Automated tests | All scenarios pass, no critical defects open |

### Cognitive Mode Switch

```text
PO hat:
  → Think user outcomes, not implementation details
  → Ask: "Does this deliver value to the customer?"
  → Communicate in user language, not technical language

Developer hat:
  → Think design, structure, correctness, performance, maintainability
  → Ask: "Is this the simplest implementation that meets the requirements?"
  → Communicate trade-offs to the team

QA hat:
  → Think adversarially — try to break the thing
  → Ask: "Under what conditions does this NOT work?"
  → Communicate findings with clear reproduction steps
```

### Three Amigos

```text
Product Owner brings: the WHY (user goal, business value, acceptance criteria)
Developer brings:     the HOW (tech approach, feasibility, edge cases from implementation)
QA brings:            the "WHAT IF" (failure modes, edge cases, testability questions)

Outcome: shared understanding BEFORE code is written
```

---

## Product Owner Workflow

**Core responsibilities:**

- Own and prioritise the product backlog
- Represent the voice of the customer
- Accept or reject completed user stories
- Break down Epics into Stories and Enablers
- Participate in Sprint Planning, Review, Retrospective

**Backlog lifecycle:**

```text
Discovery → Initiative → Epic → Feature → User Story → Task

Discovery: raw idea or problem statement from customers or strategy
Initiative: group of related Epics driving toward a product goal (OKR)
Epic: large body of work spanning multiple sprints (too big to estimate directly)
Feature: meaningful vertical slice of an Epic (can ship independently)
User Story: smallest unit of user-facing value (done in one sprint, INVEST-compliant)
Task: technical sub-unit of a story (internal to dev team)
```

**Sprint cadence (PO perspective):**

```text
Week before sprint: refinement — review top-of-backlog stories, confirm DoR met
Sprint Day 1:      planning — commit to sprint goal, clarify stories with team
During sprint:     answer questions same day; keep backlog up to date
Sprint last day:   review/demo — accept stories (or reject with clear, specific feedback)
Post-review:       retrospective; update backlog based on sprint learnings
```

**Acceptance testing script:**

```text
1. Open the story card (confirm acceptance criteria visible)
2. Walk through each AC with the developer demonstrating
3. Ask "what happens if…" for at least one edge case per story
4. Verify NFRs if applicable (load, accessibility, security)
5. Accept ✅ or Reject ❌ with specific criterion that failed
```

**PO Anti-patterns:**

- Writing implementation-focused stories ("make the DB query use an index")
- Changing priorities mid-sprint without impact analysis
- Accepting stories without actually testing them
- Treating the backlog as a to-do list (it's a prioritised queue with a WHY)

---

## Developer Workflow

**Core responsibilities:**

- Design and implement features that meet acceptance criteria
- Write tests alongside code (TDD / test-first where practical)
- Review peers' code constructively and promptly
- Maintain code quality (SOLID, clean code, refactoring)
- Participate in Three Amigos, Sprint Planning, Architecture reviews

**Feature development workflow:**

```text
1. Read: read the story, AC, NFRs — ask questions BEFORE starting
2. Design: think about the approach; sketch if complex; propose in Three Amigos
3. Branch: create feature branch (see git-vcs skill for branching conventions)
4. TDD loop:
   Red   → write failing test for the next smallest piece of behaviour
   Green → write minimal code to pass the test
   Refactor → clean up, commit, repeat
5. Self-review: before PR — read your own diff, catch obvious issues
6. PR: open PR, link story, write clear description, request reviewers
7. Review response: treat review feedback as questions, not attacks
8. Merge: squash or merge commit per project convention; delete branch
9. Demo: demo to QA and PO before sprint review
```

**Developer Anti-patterns:**

- Gold-plating (building more than the story asks)
- Starting coding without understanding the AC
- Skipping tests to "ship faster" (always slower in the medium term)
- PRs with no description or context
- Not flagging when a story needs more work (silently accumulating debt)

---

## QA / Tester Workflow

**Core responsibilities:**

- Define test strategy for each sprint / release
- Write and maintain test cases (manual and automated)
- Perform exploratory testing beyond written test cases
- Report defects with clear reproduction steps and severity
- Advocate for testability in design (Three Amigos, design reviews)
- Own the CI test suite health

**Test pyramid:**

```text
        ╱─────╲       UI / E2E Tests (few, slow, fragile — critical journeys only)
       ╱───────╲      Integration Tests (moderate — service-to-service contracts)
      ╱─────────╲     Unit Tests (many, fast, isolated — all edge cases of logic)
```

**Test types:**

| Type | What it verifies | Who writes it | When run |
|---|---|---|---|
| **Unit** | A single method/class in isolation | Developer (TDD) | Every commit |
| **Integration** | Component interactions (DB, APIs) | Developer + QA | Every PR |
| **Contract** | API contracts between services | Developer + QA | Every PR |
| **E2E / UI** | Critical user journeys end-to-end | QA | Daily |
| **Exploratory** | Heuristic, unscripted discovery testing | QA | Sprint |
| **Performance** | Response time, throughput, concurrency | QA + Dev | Pre-release |
| **Security** | Auth, injection, exposure, dependency scan | Dev + Security | Pre-release |

**Bug report format:**

```markdown
## Summary
Short, precise description of the defect.

## Environment
- Version / Build: v2.3.1 (commit abc123)
- OS / Browser: Windows 11 / Chrome 124
- User role: Standard customer (not admin)

## Steps to Reproduce
1. Navigate to /login
2. Click "Forgot Password"
3. Enter email "test@example.com"
4. Click "Send Reset Link"

## Expected Result
Email is sent within 30 seconds.

## Actual Result
Error: "Unexpected error occurred" — no email sent.

## Severity
P2 — High (core feature broken; workaround: contact support)
```

**Severity guide:**

| Severity | Meaning | Ship decision |
|---|---|---|
| P1 – Critical | System crash, data loss, security breach | Do not ship |
| P2 – High | Core feature broken, no usable workaround | Fix before release |
| P3 – Medium | Feature impaired, viable workaround | Plan fix in next sprint |
| P4 – Low | Cosmetic or edge-case annoyance | Backlog, prioritise with PO |

**QA Anti-patterns:**

- Testing only the happy path explicitly listed in AC
- Filing bugs without reproduction steps
- Blocking a release over P4 defects when P2s are fixed
- Writing brittle UI tests that break on every layout change
- Treating QA as a last-minute gate (not a continuous activity)

---

## Advanced — Shift-Left Testing

**Principle:** Move quality activities as early in the lifecycle as possible.

```text
Traditional:
Dev [ Code ] → QA [ Test ] → Found bugs → Dev [ Fix ] → QA [ Retest ]

Shift-left:
Three Amigos → TDD → Pair/Ensemble → Continuous Integration

Layers:
  1. Requirements: QA reviews AC for testability gaps
  2. Design: QA asks "how will we test this?" before coding starts
  3. Coding: Dev writes tests first (TDD) and/or pairs with QA
  4. CI: every commit runs full unit + integration suite
  5. CD: every merge to main deployable to staging for exploratory testing
```

---

## Advanced — OKRs + Backlog Alignment (PO)

```yaml
Objective: Improve customer account self-service (reduce support contacts 30% by Q3)
  KR1: Password reset completion rate > 80%
  KR2: Account details self-editable
  KR3: Order history accessible without login (guest order lookup)

Backlog filter:
  For every Epic/Story/Bug, ask: "Does this contribute to OKR progress?"
  If NO → park it; if YES → prioritise by impact per KR.
```

**WSJF (Weighted Shortest Job First):**

```text
WSJF = Cost of Delay / Job Duration

Cost of Delay = User-Business Value + Time Criticality + Risk Reduction + Opportunity Enablement
Job Duration  = relative effort estimate (Fibonacci or T-shirt)

Higher WSJF → do first.
```

---

## Advanced — Test Architecture

```text
Design tests at the right layer:
  Domain logic (pure functions)        → unit tests (no mocks)
  Application layer (orchestration)    → unit tests with mocks at boundaries
  Adapters (repos, HTTP clients, DB)   → integration tests with real or in-memory DB
  API / Contract                       → consumer-driven contract tests (Pact, Spring Cloud Contract)
  UI flows                             → Playwright/Selenium for ≤10 critical journeys

Test code quality rules:
  - Deterministic (no random data, no time dependencies without freezing clock)
  - Independent (no shared mutable state between tests)
  - Readable — failing test tells you WHAT failed and WHY instantly
  - Behaviour-coupled, not implementation-coupled
```

---

## Role Metrics

**PO:** Feature lead time · Story cycle time · Backlog age · NPS/CSAT

**Developer:** Deployment frequency · Change failure rate · MTTR · PR review latency

**QA:** Defect escape rate · Automated journey coverage · Test execution time · Flakiness rate (target 0%)
