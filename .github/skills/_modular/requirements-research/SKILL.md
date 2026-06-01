---
name: requirements-research
description: >
  Requirements gathering, analysis, and specification for software projects.
  Activates for: user stories, acceptance criteria, BDD, Gherkin, NFRs,
  elicitation, discovery sessions, story mapping, stakeholder analysis,
  prioritisation (MoSCoW, RICE), traceability (RTM), INVEST, FURPS+,
  Definition of Ready / Done, backlog refinement, requirements conflicts.
  Use when the question is about understanding WHAT to build (not HOW).
  Related skills: `software-development-roles` (role workflows), `deep-research`
  (investigation method).
---

# Requirements Research & Analysis

## Core Question

> **What does "done" look like from the user's perspective?**

Requirements answer: *Who* needs it? *What* do they need? *Why*? *How* will they
know it's working? They do not answer *how to implement*.

## User Story Format

```text
As a <persona/role>
I want to <action/capability>
So that <benefit/outcome>
```

```text
✅ As a customer, I want to reset my password by email so that I can regain
   access without calling support.
❌ As a user, I want a password reset button.
   (missing: who is "user"? what does "reset" mean? why?)
```

## Acceptance Criteria — Gherkin

```gherkin
Scenario: Successful password reset
  Given the user has a registered email address
  And the user is on the login page
  When the user clicks "Forgot Password"
  And enters their registered email
  Then a reset link is sent within 60 seconds
  And the link expires after 24 hours
```

## Pre-Story Elicitation Checklist

- [ ] Who is the *actual* user? (not "the system")
- [ ] What is the user trying to accomplish?
- [ ] What is the business benefit?
- [ ] What does success look like? (measurable)
- [ ] What are the edge cases?
- [ ] What NFRs apply? (performance, security, accessibility)
- [ ] What dependencies exist?
- [ ] What is explicitly out of scope?

## Elicitation Techniques

| Technique | Best for | Duration | Participants |
|---|---|---|---|
| **Interview** | Deep understanding of one perspective | 30–60 min | 1 stakeholder + analyst |
| **Workshop / JAD** | Multi-stakeholder consensus | 2–4 hours | 5–12 mixed |
| **Observation / Job shadow** | Actual vs. assumed workflows | Half/full day | 1 user + observer |
| **Survey** | Large group, quantitative | Async | Many stakeholders |
| **Document analysis** | Existing system or process | Self-paced | Analyst |
| **Prototype feedback** | Validating UX understanding | 1–2 hours | Users + designer |
| **Story Mapping** | End-to-end user journey | Half day | Team + PO |
| **Event Storming** | Domain discovery, complex rules | Full day | Dev + domain experts |

## Requirements Categories (FURPS+)

| Category | Questions to ask |
|---|---|
| **Functional** | What can the user DO with the system? |
| **Usability** | How easy is it? Accessibility? Training needed? |
| **Reliability** | Uptime? Graceful failure? |
| **Performance** | Latency? Throughput? Data volumes? |
| **Supportability** | Maintainability? Debuggable? Monitored? |
| **+Constraints** | Tech, regulatory, budget, timeline, team skill? |

## NFR Specification — Vague → Measurable

NFRs must be measurable; vague NFRs are unverifiable.

| ❌ Vague | ✅ Measurable |
|---|---|
| "Must be fast" | "P95 API response < 200 ms under 1,000 concurrent users" |
| "Must be secure" | "Passwords bcrypt-hashed; sessions expire after 30 min idle" |
| "Must be reliable" | "99.9% uptime (≤ 8.7 h/year); RPO < 1 h; RTO < 4 h" |
| "Must be scalable" | "Handle 10× current load (100K → 1M DAU) without re-architecture" |
| "Must be accessible" | "Meet WCAG 2.1 Level AA" |

## Prioritisation — MoSCoW

```text
MUST    Without this, the product fails. Non-negotiable.
SHOULD  High value; include if capacity allows.
COULD   Nice to have. Low effort or urgency.
WON'T   Explicitly out of scope (documented, not forgotten).
```

**Rule:** if everything is a MUST, prioritisation has failed.
Healthy split: ~40% Must / 30% Should / 20% Could / 10% Won't.

## Prioritisation — RICE

```text
RICE = (Reach × Impact × Confidence) / Effort

Reach       Users affected per quarter (absolute number)
Impact      0.25 / 0.5 / 1 / 2 / 3 (massive)
Confidence  80% / 50% / 20%
Effort      Person-months (lower = better)
```

## Stakeholder Analysis (Power × Interest)

```text
High Power + High Interest   → MANAGE CLOSELY  (key stakeholders, involve always)
High Power + Low Interest    → KEEP SATISFIED  (execs — inform, don't overload)
Low Power  + High Interest   → KEEP INFORMED   (end users, dev team)
Low Power  + Low Interest    → MONITOR         (minimal engagement)
```

## Story Map Structure

```text
User Goal → Activities → User Tasks → User Stories (detail rows)

Goal: "Buy a product online"
  Browse        → Search           → filter by category
                → View details     → see images/reviews
  Purchase      → Add to cart      → update quantities
                → Checkout         → guest checkout
  Post-purchase → Track order      → email updates
```

## Requirements Traceability Matrix (RTM)

Every requirement traces forward (to implementation and tests) and backward
(to business objective) so you can answer "why does this code exist?"

```text
Business Goal → Requirement → User Story → Test Cases → Implementation

BG-01: Reduce customer support contacts by 30%
  → REQ-014: Self-service password reset
    → US-47: As a customer, I want to reset password via email
      → TC-201, TC-202, TC-203 (happy path, expired link, invalid email)
        → src/auth/PasswordResetService.java
```

| Req ID | Description | Story | Test Cases | Status |
|---|---|---|---|---|
| REQ-014 | Password reset via email | US-47 | TC-201–203 | ✅ Done |

## Conflict Resolution Patterns

When stakeholders want contradictory things:

| Pattern | Move |
|---|---|
| **Clarify scope** | Is the conflict real, or are they solving different problems? |
| **Find the upstream goal** | What business outcome does each want? Goals often align even when solutions conflict. |
| **Articulate trade-offs** | Make the cost of each option explicit. "We can have X or Y but not both this sprint because…" |
| **Prototype & test** | Let users try both; measure preference. |
| **Defer** | If genuinely equal, "Won't" for now with a review date. |

## BDD — Specification by Example

Write scenarios *before* implementation. Include negative and security-sensitive paths.

```gherkin
Feature: Password Reset

  Scenario: Requesting reset for unknown email (no enumeration)
    When the user requests a password reset for "unknown@example.com"
    Then the same success message is shown (don't reveal if email exists)
    And no email is sent
```

## Documentation Levels

| Level | Document | Audience | Detail |
|---|---|---|---|
| Strategic | Vision / Goal doc | Executives, investors | Why? What outcome? |
| Business | BRD | Business stakeholders | What must the system achieve? |
| Functional | PRD / Functional spec | PO, Dev, QA | What does it do? Stories + AC |
| Technical | Tech spec / ADR | Dev, Architect | How does it work? Design decisions |
| Test | Test cases / BDD scenarios | QA, Dev | How do we verify it? |

## Definition of Ready (DoR) — start gate

- [ ] Story in correct format (As/I want/So that)
- [ ] Acceptance criteria written (Given/When/Then)
- [ ] NFRs specified (or explicitly N/A)
- [ ] Dependencies identified and resolved or tracked
- [ ] Design / wireframe available (UI stories)
- [ ] Estimated by the team
- [ ] No open blocking questions

## Definition of Done (DoD) — complete gate

- [ ] All AC pass (automated or manual)
- [ ] Unit tests written and passing (≥ 80% coverage on changed paths)
- [ ] NFRs verified (perf, accessibility scan)
- [ ] Code reviewed and approved
- [ ] Documentation updated (API/behaviour changes)
- [ ] Business stakeholder demo and accept

## INVEST — Good Story Test

| Letter | Criterion | Test |
|---|---|---|
| **I** | Independent | Can be built & deployed without waiting for another story? |
| **N** | Negotiable | Scope/solution open for discussion? |
| **V** | Valuable | Delivers value to a real user/stakeholder? |
| **E** | Estimable | Team can estimate? (if not → split or research) |
| **S** | Small | ≤ 1 sprint? (epics fail this — split) |
| **T** | Testable | Clear way to verify done? |
