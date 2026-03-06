---
name: software-development-roles
description: >
  Role-based guidance for the three core software development roles: Product Owner (PO),
  Developer, and QA/Tester. Covers cognitive modes, primary responsibilities, workflows,
  tools, techniques, and how the roles collaborate. Activates when the user is working
  from a specific role perspective or asking about role-specific skills and workflows.
  Domain: Software Engineering > Team Roles > Development Roles.
  Related domains: requirements-research (PO), deep-research, java-build, git-vcs.
  Use when asked about: what a Product Owner does, how a PO writes a backlog or accepts stories,
  developer workflow (feature work, code review, branching), QA strategy and test types,
  how to run a sprint from each role's perspective, role-specific tools or artifacts,
  how roles collaborate (three-amigos, PR review, acceptance demo), or how to switch
  cognitive mode depending on your current role/hat.
---

# Software Development Roles Skill

> **Domain:** Software Engineering > Team Roles > Development Roles
> **Roles covered:** Product Owner (PO) · Developer · QA / Tester
> **Hierarchy position:** Cross-cutting across Software Development lifecycle
> **Related skills:** `requirements-research`, `deep-research`, `git-vcs`, `java-build`

---

## Essentials — The Three Roles at a Glance

### What Each Role Owns

| Role | Primary Question | Key Artifact | Definition of "Done" |
|---|---|---|---|
| **Product Owner** | *What* should we build and *why*? | Product backlog · User stories · Acceptance criteria | Story accepted by PO after demo |
| **Developer** | *How* should we build it? | Source code · Design docs · Pull requests | Code reviewed, tests pass, deployed to staging |
| **QA / Tester** | *Does* it work correctly? | Test plan · Bug reports · Automated tests | All scenarios pass, no critical defects open |

### Quick Cognitive Mode Switch

```text
Putting on the PO hat:
  → Think user outcomes, not implementation details
  → Ask: "Does this deliver value to the customer?"
  → Communicate in user language, not technical language

Putting on the Developer hat:
  → Think design, structure, correctness, performance, maintainability
  → Ask: "Is this the simplest implementation that meets the requirements?"
  → Communicate trade-offs to the team

Putting on the QA hat:
  → Think adversarially — try to break the thing
  → Ask: "Under what conditions does this NOT work?"
  → Communicate findings with clear reproduction steps
```

### The Three Amigos

Before building anything significant, three roles review the story together:

```text
Product Owner brings: the WHY (user goal, business value, acceptance criteria)
Developer brings:     the HOW (tech approach, feasibility, edge cases from implementation)
QA brings:            the "WHAT IF" (failure modes, edge cases, testability questions)

Outcome: shared understanding BEFORE code is written (much cheaper than finding gaps in review)
```

---

## Patterns & Workflows — Role Workflows

### Product Owner Workflow

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

**Acceptance testing script (during demo):**

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

### Developer Workflow

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
3. Branch: create feature branch from main (git flow or trunk-based per project)
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

**Branching conventions (examples):**

```bash
feature/US-47-password-reset       # standard feature branch
fix/TC-202-expired-link-404        # bug fix with ticket reference
spike/investigate-redis-caching    # technical investigation
chore/upgrade-java-21              # non-feature maintenance
release/v2.3.0                     # release branch (if using git-flow)
```

**Code review checklist (reviewer perspective):**
- [ ] Does it implement the story (AC-by-AC)?
- [ ] Are edge cases handled?
- [ ] Are there tests? Are they testing behaviour, not implementation?
- [ ] Is there any security concern? (input validation, auth, injection)
- [ ] Is the approach readable and maintainable?
- [ ] Does naming communicate intent?
- [ ] Any performance concern for the expected load?
- [ ] Is it backwards compatible? (if applicable)

**Developer Anti-patterns:**
- Gold-plating (building more than the story asks)
- Starting coding without understanding the AC
- Skipping tests to "ship faster" (always slower in the medium term)
- PRs with no description or context ("added thing")
- Not flagging when a story needs more work (silently accumulating debt)

---

### QA / Tester Workflow

**Core responsibilities:**
- Define test strategy for each sprint / release
- Write and maintain test cases (manual and automated)
- Perform exploratory testing beyond written test cases
- Report defects with clear reproduction steps and severity
- Advocate for testability in design (Three Amigos, design reviews)
- Own the CI test suite health

**Test pyramid:**

```text
        ╱─────╲       UI / E2E Tests (few, slow, fragile — cover critical journeys only)
       ╱───────╲      Integration Tests (moderate — service-to-service contracts)
      ╱─────────╲     Unit Tests (many, fast, isolated — all edge cases of logic)
```

**Test types and their purpose:**
| Type | What it verifies | Who writes it | When run |
|---|---|---|---|
| **Unit** | A single method/class in isolation | Developer (TDD) | Every commit |
| **Integration** | Component interactions (DB, APIs) | Developer + QA | Every PR |
| **Contract** | API contracts between services | Developer + QA | Every PR |
| **E2E / UI** | Critical user journeys end-to-end | QA | Daily |
| **Exploratory** | Heuristic, unscripted discovery testing | QA | Sprint |
| **Performance** | Response time, throughput, concurrency | QA + Dev | Pre-release |
| **Security** | Auth, injection, exposure, dependency scan | Dev + Security | Pre-release |
| **Accessibility** | WCAG compliance, screen reader support | QA | Milestone |

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

## Evidence
[screenshot or log attached]
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

## Advanced Reference — Advanced Role Craft

### Shift-Left Testing

**Principle:** Move quality activities as early in the lifecycle as possible.

```text
Traditional (waterfall):
Dev [ Code ] → QA [ Test ] → Found bugs → Dev [ Fix ] → QA [ Retest ]
(expensive: bugs found late, context lost, re-work is high)

Shift-left:
Three Amigos → TDD → Pair/Ensemble → Continuous Integration
(cheap: bugs prevented or caught within hours of introduction)

Layers of shift-left:
  1. Requirements: QA reviews AC for testability gaps
  2. Design: QA asks "how will we test this?" before coding starts
  3. Coding: Dev write tests first (TDD) and/or pair with QA
  4. CI: every commit runs full unit + integration suite
  5. CD: every merge to main deployable to staging for exploratory testing
```

### Role Collaboration Patterns

**Pair programming (Dev + Dev or Dev + QA):**
- One drives (writes code), one navigates (reviews, spots issues)
- Rotate roles every 25 minutes (Pomodoro)
- QA + Dev pairing: powerful for writing automated tests for complex scenarios

**Ensemble programming (mob programming — whole team):**
- Whole team on one problem
- Rotate driver every 4-7 minutes
- Use for: critical features, onboarding new members, breaking complex blockers

**Review culture — constructive feedback patterns:**

```text
❌ "This is wrong."
✅ "I don't see a null check here — what happens if X is null?"

❌ "Why did you do it like this?"
✅ "I would've used Y approach — are there trade-offs I should understand?"

❌ Blocking PR over style preferences
✅ File as optional comment: "nit: could also be written as X" (non-blocking)
```

### OKRs + Backlog Alignment (PO Advanced)

**Objective and Key Results as backlog filter:**

```yaml
Objective: Improve customer account self-service (reduce support contacts 30% by Q3)
  KR1: Password reset completion rate > 80% (currently 0% — not built)
  KR2: Account details self-editable (currently requires support)
  KR3: Order history accessible without login (guest order lookup)

Backlog filter:
  For every Epic/Story/Bug, ask: "Does this contribute to OKR progress?"
  If NO → park it (Won't for this quarter); if YES → prioritise by impact per KR.
```

**WSJF (Weighted Shortest Job First) scoring for SAFe teams:**

```properties
WSJF = Cost of Delay / Job Duration

Cost of Delay = User-Business Value + Time Criticality + Risk Reduction + Opportunity Enablement
Job Duration  = relative effort estimate (Fibonacci or T-shirt)

Higher WSJF → do first. Smaller tasks with high delay cost beat large low-urgency tasks.
```

### Test Architecture for Long-Lived Systems

```text
Design tests at the right layer:
  Domain logic (pure functions, algorithms) → unit tests (no mocks)
  Application layer (orchestration, use cases) → unit tests with mocks at boundaries
  Adapters (repos, HTTP clients, DB) → integration tests with real or in-memory DB
  API / Contract → consumer-driven contract tests (Pact, Spring Cloud Contract)
  UI flows → Playwright/Selenium for ≤10 critical journeys (no more)

Test code quality rules:
  - Tests must be deterministic (no random data, no time dependencies without freezing clock)
  - Tests must be independent (no shared mutable state between tests)
  - Tests must be readable — failing test must tell you WHAT failed and WHY instantly
  - Avoid implementation coupling — test behaviour, not private methods
```

### Metrics for Each Role

**PO metrics:**
- Feature lead time (idea to production)
- Story cycle time (started to accepted)
- Backlog age (how old are unstarted stories?)
- NPS / CSAT (user satisfaction after features ship)

**Developer metrics:**
- Deployment frequency (how often to production?)
- Change failure rate (% of deployments causing incidents?)
- Mean time to recover (MTTR) from incidents
- PR review latency (time from PR open to first review)
- Code coverage (unit + integration target)

**QA metrics:**
- Defect escape rate (bugs found in production vs. caught in QA)
- Automated test coverage of user journeys
- Test execution time (fast feedback loop)
- Defect age (time from file to fix)
- Test flakiness rate (non-deterministic test % → target 0)
