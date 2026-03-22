---
date: YYYY-MM-DD
time: "HH:MM AM/PM"
kind: intent-capture
domain: work | personal
category: design | requirements | research | feature-exploration
project: project-name
subject: kebab-case-subject-slug
tags: [intent, primitive-selection, migration]
status: draft
version: 1
parent: null
complexity: high
outcomes: []
source: copilot
scope: project | feature
scope-project: project-name
scope-feature: null
scope-transitions: []
scope-refs: []
---

# Intent Capture — Feature / System / Primitive Title

> **Context:** What prompted this intent capture — the decision, migration, or design
> being documented.

---

## Intent Statement

<!-- One clear paragraph answering: WHAT are we building/migrating, WHY, and for WHOM?
     This is the single source of truth for the intent behind this work.
     Future sessions, migrations, or versions should reference this statement. -->

---

## Problem Space

### Current State

<!-- What exists today? How does it work? What are the pain points? -->

| Aspect | Current Implementation | Notes |
|---|---|---|
| | | |

### Desired State

<!-- What should exist after the change? What does success look like? -->

| Aspect | Target Implementation | Notes |
|---|---|---|
| | | |

### Gap Analysis

<!-- What's missing between current and desired state? -->

| Gap | Impact | Priority |
|---|---|---|
| | | |

---

## Capability Inventory

### Capabilities to Preserve (MUST migrate)

<!-- Every feature, mechanism, resource, or behavior that MUST survive the migration.
     This is the completeness checklist — nothing listed here can be lost. -->

| ID | Capability | Current Location | Migration Target | Status |
|---|---|---|---|---|
| C-001 | | | | Not started |

### Capabilities to Enhance (SHOULD improve)

<!-- Things that work today but could be better in the new approach. -->

| ID | Capability | Current Limitation | Enhancement | Priority |
|---|---|---|---|---|
| E-001 | | | | |

### Capabilities to Deprecate (WILL remove)

<!-- Things that exist today but are intentionally NOT migrated. Document WHY. -->

| ID | Capability | Reason for Deprecation |
|---|---|---|
| D-001 | | |

---

## Primitive Selection Rationale

<!-- When migrating between GHCP primitives (MCP → Skill, Prompt → Agent, etc.),
     document the decision rationale here. -->

### Evaluated Options

| Primitive | Fit Score (1-5) | Strengths | Weaknesses | Verdict |
|---|---|---|---|---|
| MCP Server | | | | |
| SKILL.md | | | | |
| .prompt.md | | | | |
| .agent.md | | | | |
| .instructions.md | | | | |
| Hybrid | | | | |

### Decision

<!-- Which primitive(s) were chosen and WHY? Reference the 5-sentence rule:
     1. "Copilot must ALWAYS do this"                → instruction
     2. "Copilot should KNOW this when relevant"      → skill
     3. "I want to TRIGGER a specific task"           → prompt
     4. "I want Copilot to BECOME someone different"  → agent
     5. "I need LIVE data from an external system"    → MCP server -->

---

## Content Inventory

### Resources / Data to Migrate

<!-- For vault migrations: every resource, record, or data point that must transfer. -->

| # | Resource ID | Title | Verified | Migrated |
|---|---|---|---|---|
| 1 | | | [ ] | [ ] |

### Mechanisms / Logic to Migrate

<!-- Search algorithms, scoring logic, classification systems, etc. -->

| # | Mechanism | Current Implementation | Migration Approach | Status |
|---|---|---|---|---|
| 1 | | | | Not started |

### Metadata / Taxonomy to Migrate

<!-- Enums, categories, tags, keyword mappings, etc. -->

| # | Taxonomy Element | Values | Migration Target | Status |
|---|---|---|---|---|
| 1 | | | | Not started |

---

## Migration Plan

### Phase 1 — Preparation

- [ ] Complete capability inventory
- [ ] Verify all content is cataloged
- [ ] Create target file structure

### Phase 2 — Content Migration

- [ ] Migrate all resources / data
- [ ] Migrate taxonomy / metadata
- [ ] Migrate discovery mechanisms

### Phase 3 — Integration

- [ ] Update dependent prompts / agents
- [ ] Update cross-references (slash-commands, docs)
- [ ] Verify activation and discoverability

### Phase 4 — Deprecation

- [ ] Mark old implementation as deprecated
- [ ] Remove old implementation (when ready)
- [ ] Update all documentation references

### Phase 5 — Verification

- [ ] Test every capability from the MUST-preserve list
- [ ] Build passes
- [ ] No orphan references

---

## Traceability

<!-- Link this intent document to implementation sessions. -->

| Phase | Session | Date | Status |
|---|---|---|---|
| Design | [this document] | | In progress |
| Implementation | | | Not started |
| Verification | | | Not started |

---

## Version History

| Version | Date | Change | Author |
|---|---|---|---|
| v1 | | Initial intent capture | |

---

## Key Outcomes

- Outcome 1
- Outcome 2

---

## Follow-Up / Next Steps

- [ ] Action item 1
- [ ] Action item 2

---

## Session Metadata

| Property | Value |
|---|---|
| Duration | ~X exchanges |
| Files touched | |
| Related sessions | none |
