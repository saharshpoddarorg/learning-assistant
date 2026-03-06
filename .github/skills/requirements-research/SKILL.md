---
name: requirements-research
description: >
  Requirements gathering, analysis, and research for software development projects.
  Domain: Software Engineering > Software Development > Requirements.
  Covers elicitation techniques (interviews, workshops, observation, surveys),
  requirements specification (user stories, use cases, acceptance criteria, BDD Gherkin),
  requirements analysis (gap analysis, impact analysis, dependency mapping, conflict resolution),
  stakeholder identification and management, non-functional requirements (NFRs),
  requirements traceability, prioritization frameworks (MoSCoW, WSJF, RICE, Kano),
  working with Product Owners and business analysts, discovery sessions, story mapping,
  and requirements documentation best practices.
  Use when asked about: how to gather requirements, how to write user stories or acceptance criteria,
  how to run a discovery session, how to prioritize features, how to handle unclear requirements,
  how to write NFRs, how to map user journeys, how to validate requirements with stakeholders,
  or any task involving understanding WHAT to build before HOW to build it.
  Also activates for: user stories, acceptance criteria, BDD, Gherkin, backlog refinement,
  story mapping, discovery, elicitation, stakeholder analysis, product backlog.
---

# Requirements Research & Analysis Skill

> **Domain:** Software Engineering > Software Development > Requirements
> **Hierarchy position:** Specific practice under Software Development
> **Related skills:** `deep-research`, `software-development-roles`

---

## Essentials — Requirements Fundamentals

### The Core Question

> **What does "done" look like from the user's perspective?**

Requirements answer: Who needs it? What do they need? Why do they need it? How will
they know it's working? NOT: How should we implement it?

### User Story Format (3-Part)

```
As a <persona/role>
I want to <action/capability>
So that <benefit/outcome>
```

**Examples:**

```
✅ As a customer, I want to reset my password by email so that I can regain access without calling support.
❌ As a user, I want a password reset button (missing: who is "user"? what does "reset" mean? why?)
```

### Acceptance Criteria — Given/When/Then (Gherkin)

```gherkin
Scenario: Successful password reset
  Given the user has a registered email address
  And the user is on the login page
  When the user clicks "Forgot Password"
  And enters their registered email
  Then a reset link is sent within 60 seconds
  And the link expires after 24 hours
```

### Quick Elicitation Checklist (Before Writing a Story)

- [ ] Who is the actual user? (Not "the system")
- [ ] What is the user trying to accomplish?
- [ ] What is the business benefit?
- [ ] What does success look like? (measurable)
- [ ] What are the edge cases?
- [ ] What are the NFRs? (performance, security, accessibility)
- [ ] What are the dependencies?
- [ ] What is out of scope?

---

## Patterns & Workflows — Elicitation & Analysis Methods

### Elicitation Techniques

| Technique | Best for | Duration | Participants |
|---|---|---|---|
| **Interview** | Deep understanding of one perspective | 30-60 min | 1 stakeholder + analyst |
| **Workshop / JAD session** | Multiple stakeholders, consensus building | 2-4 hours | Mixed group 5-12 |
| **Observation / Job shadowing** | Understanding actual workflows (vs. assumed) | Half/full day | 1 user + observer |
| **Survey / Questionnaire** | Large stakeholder group, quantitative data | Async | Many stakeholders |
| **Document Analysis** | Understanding existing system or process | Self-paced | Analyst only |
| **Prototype / Wireframe feedback** | Validating understanding of UX needs | 1-2 hours | Users + designer |
| **Story Mapping** | Visualising end-to-end user journey | Half day | Team + PO |
| **Event Storming** | Domain discovery, complex business rules | Full day | Dev + domain experts |

### Discovery Session Agenda Template

```
[15 min] Context setting — what problem are we here to solve?
[20 min] User journey mapping — walk through the current state
[20 min] Pain points — what frustrates users today?
[20 min] Desired outcomes — what does the ideal state look like?
[15 min] Scope definition — what is in/out for this iteration?
[10 min] Open questions — what do we not know yet?
[10 min] Next steps — owners, dates
```

### Requirements Categories (FURPS+)

| Category | Questions to ask |
|---|---|
| **Functional** | What can the user DO with the system? |
| **Usability** | How easy is it to use? Accessibility? Training needed? |
| **Reliability** | What uptime? How does it fail gracefully? |
| **Performance** | Latency targets? Throughput? Data volumes? |
| **Supportability** | How is it maintained? Debuggable? Monitored? |
| **+Constraints** | Tech stack, regulatory, budget, timeline, team skill? |

### Non-Functional Requirements (NFR) Specification

NFRs must be **measurable** — vague NFRs are unverifiable.

| ❌ Vague (bad) | ✅ Measurable (good) |
|---|---|
| "The system must be fast" | "P95 API response time < 200ms under 1,000 concurrent users" |
| "The system must be secure" | "Passwords must be bcrypt-hashed; sessions expire after 30 min idle" |
| "The system must be reliable" | "99.9% uptime (≤ 8.7h downtime/year); RPO < 1h; RTO < 4h" |
| "The system must be scalable" | "Must handle 10x current load (100K → 1M daily users) without re-architecture" |
| "The system must be accessible" | "Must meet WCAG 2.1 Level AA" |

### MoSCoW Prioritisation

```
MUST   → Without this, the product fails. Non-negotiable for this iteration.
SHOULD → High value; include if capacity allows. Strong business case.
COULD  → Nice to have. Low effort or low urgency.
WON'T  → Explicitly out of scope for this iteration (documented, not forgotten).
```

**Usage rule:** If everything is a MUST, prioritisation has failed.
Healthy MoSCoW: 40% Must, 30% Should, 20% Could, 10% Won't.

### RICE Prioritisation Score

```
RICE = (Reach × Impact × Confidence) / Effort

Reach:      How many users affected per quarter? (absolute number)
Impact:     How much does it improve their experience? (0.25 / 0.5 / 1 / 2 / 3)
Confidence: How confident are we? (80% / 50% / 20%)
Effort:     Person-months to implement (lower = better)
```

### Stakeholder Analysis

```
Map stakeholders on two axes:
  - Power (influence they have over the project)
  - Interest (how much they care about the outcome)

High Power + High Interest  → MANAGE CLOSELY   (key stakeholders, involve in every decision)
High Power + Low Interest   → KEEP SATISFIED   (executives — inform, don't overload)
Low Power + High Interest   → KEEP INFORMED    (end users, dev team)
Low Power + Low Interest    → MONITOR          (minimal engagement)
```

### Story Map Structure

```
User Goal (top row) → Activities → User Tasks → User Stories (detail rows)

Example:
Goal: "Buy a product online"
  Activity: Browse          → Task: Search → Story: filter by category
                             → Task: View details → Story: see images/reviews
  Activity: Purchase        → Task: Add to cart → Story: update quantities
                             → Task: Checkout → Story: guest checkout
  Activity: Post-purchase   → Task: Track order → Story: email updates
```

---

## Advanced Reference — Advanced Requirements Engineering

### Requirements Traceability Matrix (RTM)

Every requirement should trace forward (to implementation and tests) and backward
(to business objective), so you can answer "why does this code exist?"

```
Business Goal → Requirement → User Story → Test Cases → Implementation

Example:
BG-01: Reduce customer support contacts by 30%
  → REQ-014: Self-service password reset
    → US-47: As a customer, I want to reset password via email
      → TC-201, TC-202, TC-203 (happy path, expired link, invalid email)
        → src/auth/PasswordResetService.java
```

**RTM Template:**
| Req ID | Description | Story | Test Cases | Status | Notes |
|---|---|---|---|---|---|
| REQ-014 | Password reset via email | US-47 | TC-201-203 | ✅ Done | |

### Domain Modelling Before Stories

Before writing stories for complex domains, model the domain:

1. **Event Storming** — paste events on a timeline
   - Orange: Domain Events (past tense: "Order Placed")
   - Blue: Commands (imperative: "Place Order")
   - Yellow: Aggregates (nouns that accept commands: "Order")
   - Pink: External Systems ("Payment Gateway")

2. **Bounded Contexts** — identify where language changes meaning:
   - "Account" in billing ≠ "Account" in user management
   - Define explicit boundaries; each context has its own ubiquitous language

3. **Context Map** — draw relationships between bounded contexts:
   - Shared Kernel, Customer-Supplier, Conformist, Anti-Corruption Layer, Open Host

### Conflict Resolution in Requirements

When stakeholders want contradictory things:

```
Pattern 1 — Clarify scope: Is the conflict real or are they solving different problems?
Pattern 2 — Find the upstream goal: What business outcome does each stakeholder want?
             Often the goals align even when the solutions conflict.
Pattern 3 — Trade-off articulation: Make the cost of each option explicit.
             "We can have X or Y but not both in this sprint because..."
Pattern 4 — Prototype test: Let users try both options and measure preference.
Pattern 5 — Defer: If genuinely equal, defer. "Won't" for now with a decision review date.
```

### BDD Specification by Example

BDD bridges requirements and tests. Key rule: write scenarios **before** implementation.

```gherkin
Feature: Password Reset
  As a registered user
  I want to reset my password via email
  So that I can regain access when I forget my credentials

  Background:
    Given a user account with email "user@example.com" exists

  Scenario: Sending reset link to valid email
    When the user requests a password reset for "user@example.com"
    Then a reset email is sent to "user@example.com" within 30 seconds
    And the reset link expires in 24 hours

  Scenario: Requesting reset for unknown email (security: no enumeration)
    When the user requests a password reset for "unknown@example.com"
    Then the same success message is shown (don't reveal if email exists)
    And no email is sent

  Scenario: Using an expired reset link
    Given a reset link was generated 25 hours ago
    When the user clicks the reset link
    Then an error "This link has expired. Please request a new one." is shown
```

### Requirements Documentation Levels

| Level | Document | Audience | Detail |
|---|---|---|---|
| Strategic | Vision / Goal document | Executives, investors | Why are we building this? What outcome? |
| Business | Business Requirements Doc (BRD) | Business stakeholders | What must the system achieve? |
| Functional | Functional Spec / PRD | PO, Dev, QA | What does the system do? User stories + acceptance criteria |
| Technical | Technical Spec / ADR | Dev, Architect | How does it work? Design decisions |
| Test | Test Cases / BDD scenarios | QA, Dev | How do we verify it works? |

### Definition of Ready (DoR) & Definition of Done (DoD)

**Definition of Ready** — requirements quality gate for starting development:
- [ ] Story written in correct format (As/I want/So that)
- [ ] Acceptance criteria written (Given/When/Then)
- [ ] NFRs specified (or explicitly "N/A")
- [ ] Dependencies identified and resolved or tracked
- [ ] Design/wireframe available (for UI stories)
- [ ] Story estimated by the team
- [ ] No open blocking questions

**Definition of Done** — quality gate for marking a story complete:
- [ ] All acceptance criteria pass (automated or manual test)
- [ ] Unit tests written and passing (≥80% coverage for changed paths)
- [ ] Non-functional criteria verified (perf test, accessibility scan)
- [ ] Code reviewed and approved
- [ ] Documentation updated (if API/behaviour changed)
- [ ] Business stakeholder demos and accepts

### Requirements Research with Copilot

```
# Use the Thinking-Beast-Mode agent for deep domain research:
Ask: "Research the domain of [your area]. Identify key entities, relationships,
     events, and business rules using DDD vocabulary. Suggest 5 questions I should
     ask stakeholders to clarify requirements."

# Use /deep-dive for technology constraints:
/deep-dive → [technology] → what are the limitations that affect our requirements?

# Use /brain-new to capture a requirements decision:
/brain-new → "decision-password-reset-scope" → notes → ["requirements", "auth"]
```

### INVEST Criteria for Good Stories

| Letter | Criterion | Test |
|---|---|---|
| **I** | Independent | Can be built and deployed without waiting for another story? |
| **N** | Negotiable | Is the scope/solution open for discussion (not a fixed spec)? |
| **V** | Valuable | Does it deliver value to a real user or stakeholder? |
| **E** | Estimable | Can the team estimate it? (If not → needs splitting or research) |
| **S** | Small | Can it be done in ≤1 sprint? (Epics fail this → split them) |
| **T** | Testable | Is there a clear way to verify it's done? |
