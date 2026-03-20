# sessions/ — Captured AI Chat Sessions

> **Tier 4** of the ai-brain workspace. Preserves valuable AI conversations that contain
> research, analysis, explanations, and learning outcomes worth referencing later.

---

## What Is This Tier?

The `sessions/` tier captures AI chat conversations that produce substantive, referenceable
content. Unlike `notes/` (your distilled writing) or `library/` (imported sources), sessions
preserve the **request → response** flow of AI-assisted exploration.

---

## Routing — How Sessions Differ from Other Tiers

| Tier | Routing question | Content |
|---|---|---|
| **inbox/** | Not ready yet? | Raw capture, drafts, unprocessed |
| **notes/** | Did you write it yourself? | Your distilled insights, decisions, synthesis |
| **library/** | Did you import it from an external source? | Slide decks, guides, external references |
| **sessions/** | Was it a valuable AI conversation? | Captured request/response pairs with analytical depth |

### When does a conversation become a session capture?

A session is captured when the conversation involves **research, analysis, complex debugging,
architecture decisions, learning deep-dives, or any exchange producing substantive content**
worth referencing later. Simple refactoring, quick fixes, and routine tasks are NOT captured.

See `.github/instructions/chat-capture.instructions.md` for the full capture gate criteria.

---

## Hierarchy

```text
sessions/
├── README.md                           ← This file
├── SESSION-LOG.md                      ← Append-only index of all captured sessions
├── work/                               ← Corporate / job-specific sessions
│   ├── README.md
│   ├── code-analysis/                  ← Code review, architecture analysis
│   ├── debugging/                      ← Complex bug investigation, RCA
│   ├── requirements/                   ← User stories, acceptance criteria, BDD
│   ├── performance/                    ← Profiling, optimization, benchmarking
│   ├── feature-exploration/            ← Design alternatives, POC, feasibility
│   ├── documentation/                  ← API docs, design docs, runbooks
│   ├── research/                       ← Technology evaluation, protocol analysis
│   └── general/                        ← Work sessions not fitting above
├── personal/                           ← Personal projects & learning
│   ├── README.md
│   ├── learning/                       ← Concept deep-dives, tutorials, skill-building
│   ├── project-dev/                    ← Personal project development
│   ├── requirements/                   ← User stories, acceptance criteria, feature scoping
│   ├── financial/                      ← Budgeting, investment, tax strategies
│   ├── research/                       ← Personal interest research, tool evaluation
│   └── general/                        ← Personal sessions not fitting above
└── _templates/
    ├── session-capture.md              ← Generic frontmatter + content template
    └── requirements-capture.md         ← Requirements-specific template (user stories, BDD, NFRs)
```

Category folders are created **on demand** — only when the first session of that category
is captured. You never create folders manually.

---

## Naming Convention

### Standard Pattern

```text
YYYY-MM-DD_HH-MMtt_<category>_<subject>[_v<N>].md
```

| Segment | Format | Example |
|---|---|---|
| Date | `YYYY-MM-DD` (ISO 8601) | `2026-03-20` |
| Time | `HH-MMtt` (12-hour, lowercase am/pm) | `10-30am`, `04-12pm` |
| Category | kebab-case, matches folder name | `code-analysis` |
| Subject | kebab-case, 3-8 words, descriptive | `order-service-calculate-total` |
| Version | `_v<N>` suffix, only for v2+ | `_v2`, `_v3` |

### Examples

```text
sessions/work/code-analysis/
  2026-03-20_10-30am_code-analysis_order-service-calculate-total.md
  2026-03-21_02-15pm_code-analysis_order-service-calculate-total_v2.md

sessions/work/research/
  2026-03-20_02-15pm_research_mcp-transport-sse-vs-stdio.md

sessions/personal/learning/
  2026-03-20_05-00pm_learning_java-virtual-threads-deep-dive.md

sessions/personal/financial/
  2026-03-20_01-30pm_financial_tax-optimization-freelance-income.md
```

---

## Subject Naming Rules

1. **Lowercase kebab-case** — `order-service-validation`, not `OrderService_Validation`
2. **3-8 words** — descriptive but concise
3. **Most specific first** — `hashmap-vs-treemap-performance` not `performance-comparison`
4. **No filler words** — drop "the", "a", "an" unless critical
5. **Method-level specificity** when applicable — include method name for method-specific analysis

---

## Frontmatter Schema

```yaml
---
date: 2026-03-20
time: "10:30 AM"
kind: session-capture
domain: work
category: code-analysis
project: learning-assistant
subject: order-service-calculate-total
tags: [java, refactoring, order-service, clean-code]
status: draft
version: 1
parent: null
complexity: high
outcomes:
  - identified 3 code smells in calculateTotal()
  - proposed extract-method refactoring
source: copilot
---
```

| Field | Required | Values | Purpose |
|---|---|---|---|
| `date` | Yes | `YYYY-MM-DD` | Session date |
| `time` | Yes | `"HH:MM AM/PM"` (quoted) | Session start time |
| `kind` | Yes | `session-capture` | Always this value |
| `domain` | Yes | `work`, `personal` | Domain classification |
| `category` | Yes | See category tables below | Folder classification |
| `project` | Yes | kebab-case | Project or context bucket |
| `subject` | Yes | kebab-case | What the session is about |
| `tags` | Yes | 3-7 lowercase-hyphens | Searchable metadata |
| `status` | Yes | `draft`, `final`, `archived` | Lifecycle state |
| `version` | Yes | Integer (starts at 1) | Version number |
| `parent` | Yes | `null` or parent filename | Version chain |
| `complexity` | Yes | `high`, `medium` | Complexity signal |
| `outcomes` | No | List of strings | Key outcomes from the session |
| `source` | Yes | `copilot` | Always copilot for captures |

---

## Versioning

When continuing analysis on the same subject from a previous session:

| Scenario | Action |
|---|---|
| First session on a subject | v1 (no suffix in filename) |
| Continuing from v1 | v2 (`_v2` suffix, `parent:` points to v1 file) |
| Further continuations | v3, v4, etc. |
| Same area, different aspect | New file (not a version) |
| Same topic, weeks later | New file (too stale for versioning) |

---

## Sub-Package Escalation

When **5+ files** accumulate for the same subject within a category, group them
into a sub-directory:

```text
# Before (flat)
work/code-analysis/
  2026-03-20_10-30am_code-analysis_order-service-calculate-total.md
  2026-03-21_..._order-service-validate-order.md
  ... (5+ files about order-service)

# After (sub-package)
work/code-analysis/order-service/
  README.md
  2026-03-20_10-30am_calculate-total.md
  2026-03-21_..._validate-order.md
  ...
```

Inside sub-packages, filenames drop the category and subject prefix (already implied
by the folder path).

---

## Session Content Structure

```markdown
---
(frontmatter)
---

# <Session Title>

> **Context:** Brief 1-2 sentence context.

---

## Request

<User's request — paraphrased or quoted.>

---

## Analysis / Response

<Substantive content. Use H3/H4 for structure.
Include code blocks, tables, diagrams as produced.>

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
| Files touched | list of files |
| Related sessions | links to related captures |
```

---

## Requirements Sessions — Enhanced Capture

Requirements gathering sessions for personal software development projects use a
specialised template (`_templates/requirements-capture.md`) that includes:

- **Project Overview** — project name, domain, target user, stage
- **Problem Statement** — user-perspective problem description
- **User Stories** — As/I want/So that with Gherkin acceptance criteria
- **NFRs** — measurable targets per FURPS+ category
- **Scope Definition** — explicit in/out-of-scope
- **Dependencies & Constraints** — blockers, technical constraints
- **Open Questions** — unresolved items

### When to use `requirements` vs `project-dev`

| Requirements | Project-Dev |
|---|---|
| Defining WHAT to build | Deciding HOW to build it |
| User stories, acceptance criteria | Design patterns, architecture |
| Feature scoping (MoSCoW) | Code implementation |
| BDD scenarios | Debugging, optimization |

### Requirements versioning

Requirements evolve across sessions. Use `_v2`, `_v3` suffixes when refining
the same feature scope, with `parent:` linking to the previous version.

```text
2026-03-20_02-15pm_requirements_task-manager-mvp-scope.md      ← v1
2026-03-22_10-00am_requirements_task-manager-mvp-scope_v2.md   ← refined
2026-03-25_01-00pm_requirements_task-manager-mvp-scope_v3.md   ← final
```

---

## Session Log

All captures are indexed in [SESSION-LOG.md](SESSION-LOG.md) — an append-only table
for quick lookup and auditing.

---

## Promotion to Notes

Captured sessions can be **promoted** to `notes/` through distillation:

1. Read the session capture
2. Write your own synthesis in `notes/YYYY-MM-DD_slug.md`
3. Link back: `Source: [session](../sessions/work/code-analysis/...)`
4. The session remains as reference; the note is your authored knowledge

---

## User Controls

| Command | Effect |
|---|---|
| "capture this session" | Force capture regardless of criteria |
| "don't capture this" | Suppress capture for this conversation |
| "capture to work/research" | Force capture to specific domain/category |

---

## Git Commit Convention

```text
brain(sessions): capture <category> — <subject>
```
