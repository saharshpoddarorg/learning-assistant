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

See `.github/instructions/chat-capture.instructions.md` for the full capture gate criteria,
or read the [Session Capture Guide](../../../.github/docs/session-capture-guide.md) for a
friendly overview of the entire system.

---

## Hierarchy

```text
sessions/
├── README.md                           ← This file
├── SESSION-LOG.md                      ← Append-only index of all captured sessions
├── CAPTURE-LOG.md                      ← Escalation & structural operations log
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
│   ├── software-dev/                   ← Personal project development (umbrella)
│   │   ├── requirements/               ← Scoping, user stories for your projects
│   │   ├── research/                   ← Tech evaluation for your projects
│   │   ├── design/                     ← Architecture, API design, schemas
│   │   ├── implementation/             ← Coding sessions, feature building
│   │   ├── testing/                    ← Test strategy, TDD/BDD setup
│   │   ├── code-review/               ← Code analysis, refactoring review
│   │   ├── devops/                     ← CI/CD, Docker, deployment
│   │   └── general/                    ← Other software dev activities
│   ├── financial/                      ← Budgeting, investment, tax strategies
│   ├── research/                       ← Personal interest research, tool evaluation
│   └── general/                        ← Personal sessions not fitting above
└── _templates/
    ├── session-capture.md              ← Generic frontmatter + content template
    ├── code-analysis-capture.md        ← Code review: findings table, class/method target
    ├── design-capture.md               ← Design: approach/proposal, use cases, criteria
    ├── debugging-capture.md            ← Debugging: hypothesis tracking, RCA, prevention
    ├── requirements-capture.md         ← Requirements: user stories, BDD, NFRs
    └── intent-capture.md               ← Intent: design decisions, migrations
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
# Work domain (flat categories)
sessions/work/code-analysis/
  2026-03-20_10-30am_code-analysis_order-service-calculate-total.md
  2026-03-21_02-15pm_code-analysis_order-service-calculate-total_v2.md

sessions/work/research/
  2026-03-20_02-15pm_research_mcp-transport-sse-vs-stdio.md

# Personal domain — software-dev umbrella (category = leaf folder name)
sessions/personal/software-dev/requirements/
  2026-03-20_02-15pm_requirements_task-manager-mvp-scope.md

sessions/personal/software-dev/design/
  2026-03-20_03-00pm_design_task-manager-api-endpoints.md

sessions/personal/software-dev/implementation/
  2026-03-20_11-00am_implementation_task-manager-crud-endpoints.md

# Personal domain — stand-alone categories
sessions/personal/learning/
  2026-03-20_05-00pm_learning_java-virtual-threads-deep-dive.md

sessions/personal/financial/
  2026-03-20_01-30pm_financial_tax-optimization-freelance-income.md
```

---

## Tagging and Cross-Referencing

Tags enable cross-cutting discoverability (folders organize by activity, tags search
across activities). Every session uses **3-7 tags** in these categories:

| Tag type | Format | Example |
|---|---|---|
| **Project** | `project:<name>` | `project:task-manager` |
| **GitHub** | `gh:<owner/repo>` | `gh:saharshpoddarorg/task-manager` |
| **Activity** | plain kebab-case | `requirements`, `api-design`, `trade-off-analysis` |
| **Technology** | plain kebab-case | `java`, `spring-boot`, `react`, `docker` |

Cross-references between related sessions use `scope-refs` in frontmatter with
bidirectional links. See `.github/instructions/session-scoping.instructions.md`.

---

## Project-Aware Sessions

When starting a chat about a personal software project (including GitHub repos), the
system automatically detects the project and routes to `software-dev/<activity>/`.
Within a single session, activity can switch (requirements → design → implementation)
with transitions logged in `scope-transitions`.

See `.github/instructions/chat-capture.instructions.md` for the full project-aware
session protocol, automatic detection triggers, and context-switching rules.

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
| `scope` | Yes | `global`, `project`, `feature` | Applicability level |
| `scope-project` | Conditional | `null` or kebab-case | Required when scope is `project` or `feature` |
| `scope-feature` | Conditional | `null` or kebab-case | Required when scope is `feature` |
| `scope-transitions` | Yes | List (can be empty) | Scope changes during the session |
| `scope-refs` | Yes | List (can be empty) | Cross-references to related sessions |

---

## Session Scoping

Sessions operate at one of three scope levels:

| Level | Meaning | Example |
|---|---|---|
| `global` | General knowledge — not tied to any project | "How does OAuth 2.0 work?" |
| `project` | Tied to a specific project | "ABSDevelopment tech stack options" |
| `feature` | Tied to a feature within a project | "ABSDevelopment login-page auth" |

Scope can change mid-session. Use `/session-scope` to widen (feature → project → global),
narrow (global → project → feature), switch contexts, or split scope segments into
separate session files with bidirectional cross-references.

Full scoping protocol: `.github/instructions/session-scoping.instructions.md`

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

Sessions naturally cluster around shared subjects or projects. Three escalation patterns
keep folders navigable as volume grows.

### Pattern 1 — Subject-Based Sub-Package (5+ files)

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

### Pattern 2 — Project-Based Sub-Package (3+ files in same activity for same project)

Within `personal/software-dev/<activity>/`, when **3+ sessions** relate to the **same
project**, create a project sub-folder:

```text
personal/software-dev/requirements/task-manager/
  2026-03-20_02-15pm_mvp-scope.md
  2026-03-21_10-00am_recurring-tasks.md
  2026-03-22_04-30pm_notification-rules.md
```

### Pattern 3 — Domain-Specific Hierarchical Escalation

Certain categories support two-level sub-folder hierarchies:

| Pattern | Category | Level 1 | Level 2 | Thresholds |
|---|---|---|---|---|
| **3a** | code-analysis, code-review | class name | method name | 3+ / 2+ |
| **3b** | design, feature-exploration | component | aspect | 3+ / 2+ |
| **3c** | debugging | service | issue type | 3+ / 2+ |

**Code analysis example (class → method):**

```text
work/code-analysis/order-service/
  calculate-total/
    2026-04-01_..._calculate-total.md
    2026-04-07_..._calculate-total_v2.md
  2026-04-02_..._validate-order.md
  2026-04-03_..._process-payment.md
```

**Design example (component → aspect):**

```text
personal/software-dev/design/task-manager/
  api-design/
    2026-04-01_..._rest-endpoints.md
    2026-04-02_..._graphql-evaluation.md
  2026-04-03_..._database-schema.md
```

**Design aspects:** intent, approach, proposal, api-design, schema, use-case, criteria,
security, performance, patterns, trade-offs, migration, hld, lld.

Extended frontmatter fields (`code-target`, `design-target`, `debug-target`) enable
precise grouping. See `chat-capture.instructions.md` for the full protocol.

### Cross-Cutting Project Index

When a personal project spans **3+ activity categories** under `software-dev/`, create
a project index file at `personal/software-dev/<project-name>-INDEX.md`:

```markdown
# task-manager — Session Index

| Activity | Sessions | Latest |
|---|---|---|
| requirements | 3 | [notification-rules](requirements/task-manager/...) |
| design | 2 | [api-endpoints](design/task-manager/...) |
| implementation | 4 | [crud-endpoints-v2](implementation/task-manager/...) |
| testing | 1 | [e2e-strategy](testing/...) |
```

This provides a single entry point for all sessions related to one project.

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

### Activity routing within software-dev

| `software-dev/requirements` | `software-dev/design` | `software-dev/implementation` |
|---|---|---|
| Defining WHAT to build | Architecture, patterns | Writing code, building features |
| User stories, acceptance criteria | Component design, HLD/LLD | Debugging during dev |
| Feature scoping (MoSCoW) | ADRs, API contracts | POC or prototype implementation |
| BDD scenarios | Database schema design | Optimising existing code |

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

All structural operations (escalation, forks, moves) are logged in
[CAPTURE-LOG.md](CAPTURE-LOG.md) — created automatically on first use.

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
