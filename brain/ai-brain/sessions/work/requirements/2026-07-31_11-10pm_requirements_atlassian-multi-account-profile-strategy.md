---
date: 2026-07-31
time: "11:10 PM"
kind: session-capture
domain: work
category: requirements
project: iesd-26
subject: atlassian-multi-account-profile-strategy
tags: [project:iesd-26, requirements, atlassian, multi-account, pat-auth, profile-resolution, cross-account]
status: draft
version: 1
parent: null
complexity: high
outcomes:
  - Locked architecture decision: one Atlassian umbrella with separate Jira/Bitbucket/Confluence adapters
  - Locked rollout strategy: iterative migration (foundation -> Jira -> Bitbucket -> Confluence)
  - Locked runtime profile resolution: override -> session -> configurable default -> work
source: copilot
scope: project
scope-project: iesd-26
scope-feature: multi-account-profile-extrapolation
scope-transitions: []
scope-refs:
  - file: "../../../../backlog/features/BLI-001_build-production-atlassian-mcp-server.md"
    relationship: related
    note: "Backlog item enriched with this brainstorming snapshot and implementation guidance"
---

# Requirements — iesd-26: Atlassian multi-account profile extrapolation

> **Context:** This session captures planning and decision-making for extrapolating
> single-account colleague-provided Jira/Bitbucket/Confluence skill setup into a
> multi-account model using PAT-based authentication.
>
> **Path:** `sessions/work/requirements/`

---

## Project Overview

| Property | Value |
|---|---|
| Project | iesd-26 |
| Domain | Atlassian automation via skills/CLI (Jira, Bitbucket, Confluence) |
| Target user | Repo owner maintaining work + personal-work Atlassian accounts |
| Stage | Enhancement of existing single-account setup |

---

## Problem Statement

The current setup in `E:\mgcnoscan\iesd-26` is based on colleague-provided
single-account patterns with placeholder PAT values in `.env`. The user needs
a reliable way to operate multiple Atlassian accounts (work and personal-work,
with different base URLs) without breaking existing flows or requiring noisy
per-command profile switching.

---

## User Stories

### US-001: Session-level account selection with fallback

```text
As a user working across Atlassian accounts
I want to choose a profile once per session
So that I can run multiple Jira/Bitbucket/Confluence actions without repeating profile context
```

**Acceptance Criteria:**

```gherkin
Scenario: Session profile is set
  Given a valid session profile is active
  When I run Atlassian actions without explicit profile
  Then credentials and base URLs are resolved from that session profile

Scenario: Session profile is not set
  Given no command override and no session profile
  When I run an Atlassian action
  Then the system uses a configurable default profile

Scenario: No explicit default configured
  Given no command override, no session profile, and no explicit default
  When I run an Atlassian action
  Then the system falls back to profile "work"
```

**Priority:** MUST

### US-002: Unified capability with separate service adapters

```text
As a maintainer
I want Jira, Bitbucket, and Confluence to share profile/auth foundations but keep separate adapters
So that each API can evolve independently without cross-service regressions
```

**Acceptance Criteria:**

```gherkin
Scenario: Service adapter boundaries
  Given the Atlassian capability is enabled
  When Jira and Confluence calls are executed
  Then each call uses a service-specific adapter and endpoint contract

Scenario: Shared profile resolution
  Given a selected profile with service credentials
  When any service call is made
  Then profile resolution logic is consistent across Jira, Bitbucket, and Confluence
```

**Priority:** MUST

### US-003: Safe cross-account operations

```text
As a user migrating/copying data across instances
I want explicit source and target profile declaration
So that I avoid accidental writes to the wrong Atlassian account
```

**Acceptance Criteria:**

```gherkin
Scenario: Cross-account action
  Given an operation that reads from one account and writes to another
  When source-profile or target-profile is missing
  Then the operation is blocked with a clear validation message

Scenario: Execution diagnostics
  Given source and target profiles are provided
  When the operation runs
  Then logs show active profile names and base URLs but never token values
```

**Priority:** MUST

---

## Non-Functional Requirements

| Category | Requirement | Measurable Target |
|---|---|---|
| Performance | Profile resolution overhead should be negligible | Profile lookup < 50 ms per call |
| Reliability | Partial profile support per service | Missing Jira vars fail only Jira calls |
| Usability | Minimal friction for daily use | One-time session profile set, optional per-command override |
| Security | No PAT leakage in logs/output | 0 token prints in stdout/stderr/log files |
| Supportability | Reusable migration pattern for all 3 services | Common resolver consumed by Jira/Bitbucket/Confluence modules |

---

## Scope Definition

### In Scope (this iteration)

- Profile model and resolution strategy definition
- Runtime precedence decision: override -> session -> default -> work
- Rollout sequence and validation matrix
- Cross-account guardrails for source/target profile handling
- Linking decisions to backlog item for later implementation continuity

### Out of Scope (documented, not forgotten)

- Immediate coding changes in `E:\mgcnoscan\iesd-26`
- Live PAT injection and account onboarding execution
- Full test implementation and CI integration in this session

---

## Dependencies & Constraints

| Dependency / Constraint | Impact | Status |
|---|---|---|
| Personal-work base URLs not yet provided | Blocks end-to-end profile validation on personal-work | Open |
| Personal-work PAT tokens not yet provided | Blocks credential verification and who-am-I checks | Open |
| Existing colleague folder structure should be preserved initially | Requires in-place iterative migration rather than destructive replacement | Accepted |

---

## Detailed Conversation Capture

## Exchange 1 — Replace existing skill vs evolve in place

### Request

User asked whether to delete existing `.github/skills/atlassian-tools` and start
fresh from colleague copy before multi-account extrapolation.

### Response Summary

Recommendation was to avoid hard delete and perform staged in-place migration:
keep stable entry points, validate progressively, then remove obsolete artifacts.

## Exchange 2 — Iterative-by-service vs all-at-once

### Request

User asked whether to extrapolate Jira/Bitbucket/Confluence separately or copy all
and extrapolate all at once; also whether to model as separate services or one unit.

### Response Summary

Decision guidance:

1. Treat as one Atlassian product capability for UX/routing.
2. Keep separate Jira/Bitbucket/Confluence adapters internally.
3. Roll out iteratively in vertical slices, not all-at-once.

## Exchange 3 — Work-repo concrete context and brainstorming mode

### Request

User provided work repo details (`E:\mgcnoscan\iesd-26`), current skill folders,
and asked for brainstorming only (no implementation yet).

### Response Summary

Provided multi-account architecture options, profile strategy, guardrails,
validation matrix, and phased migration order.

## Exchange 4 — Decision inputs from user

### User Inputs Locked

1. Per-command profile selection feels like overkill; prefers per-session.
2. Wants configurable default-profile fallback (switchable among work/personal-work/etc.).
3. No strict mode for now.
4. Base URLs differ between work and personal-work accounts.

### Resulting Decision Set

1. Session-first profile selection.
2. Optional per-command override for exceptions.
3. Non-strict fallback enabled.
4. Different base URLs per profile as first-class requirement.

## Exchange 5 — Persisting decisions for resume

### Request

User asked to gather details into backlog for future continuation.

### Response Summary

Brainstorm snapshot was captured in backlog item `BLI-001` and now this dedicated
session note captures full context + decision trail + requirement framing.

---

## Proposed Plan Snapshot (from brainstorming)

1. Build common profile loader and default-profile switch.
2. Wire Jira first and validate (who-am-I + read + safe write path).
3. Wire Bitbucket next using same loader.
4. Wire Confluence last (higher content-format complexity).
5. Add cross-account workflows after all three services pass profile validation.

---

## Open Questions

- [ ] Final profile file naming convention: `.env.work` / `.env.personal-work` / `.env.personal` vs alternative
- [ ] Where should default profile live: dedicated defaults file vs environment variable only
- [ ] Should session profile be persisted per shell session only or across terminal restarts
- [ ] Which operations are allowed in non-strict mode without explicit profile confirmation

---

## Key Outcomes

- Unified architecture boundary clarified: one Atlassian umbrella, three service adapters.
- Runtime profile resolution order finalized for implementation.
- Migration strategy de-risked through iterative rollout instead of big-bang replacement.
- Continuation path documented in both backlog and sessions.

---

## Follow-Up / Next Steps

- [ ] Confirm profile file naming convention and defaults location
- [ ] Confirm session-profile persistence behavior
- [ ] Provide personal-work base URLs and PAT tokens (securely, not in logs)
- [ ] Start implementation in `E:\mgcnoscan\iesd-26` after explicit green signal

---

## Cross-References

| Relationship | Session | Note |
|---|---|---|
| related | `../../../../backlog/features/BLI-001_build-production-atlassian-mcp-server.md` | Backlog item contains aligned brainstorm snapshot and implementation framing |

---

## Session Metadata

| Property | Value |
|---|---|
| Duration | ~10 substantive exchanges |
| Files touched | `brain/ai-brain/backlog/features/BLI-001_build-production-atlassian-mcp-server.md` |
| Related sessions | `personal/personal-work/software-dev/design/2026-07-31_09-48pm_design_backlog-workspace-cross-index-framework.md` (process-level continuity) |
