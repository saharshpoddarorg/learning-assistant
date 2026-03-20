---
applyTo: "**"
---

# Chat-Session Capture — Auto-Capture Policy & Protocol

> **Purpose:** Define when and how AI chat sessions are automatically captured into
> the `brain/ai-brain/sessions/` tier. This ensures valuable conversations are preserved
> without cluttering the workspace with trivial interactions.
>
> **When active:** Always — this instruction applies to every file via `applyTo: "**"`.

---

## The Capture Decision — Ask Before Responding

Before formulating a response, evaluate whether this conversation qualifies for capture.
Use the **Capture Gate** below. Do NOT announce the capture decision to the user — capture
silently when criteria are met.

---

## Capture Gate — When to Capture

### CAPTURE (any ONE of these triggers is sufficient)

| Trigger | Examples |
|---|---|
| **Research work** | Technology evaluation, trade-off analysis, competitive analysis, spike investigation |
| **Code analysis** | Reviewing complex code, architecture analysis, pattern identification, code review with analysis |
| **Requirements gathering** | User stories, acceptance criteria, BDD scenarios, feature scoping |
| **Performance analysis** | Profiling strategies, optimization approaches, benchmarking, bottleneck investigation |
| **Feature development exploration** | Design alternatives, POC planning, feasibility assessment |
| **Complex debugging** | Multi-step investigation, root cause analysis, hypothesis-driven debugging |
| **Documentation** | API docs, design docs, architecture overviews, technical writing |
| **Learning sessions** | Concept explanations, tutorial walkthroughs, deep-dives, skill building |
| **Financial / personal advisory** | Budgeting analysis, investment strategies, tax planning |
| **Architecture / design decisions** | System design, component design, pattern selection, ADRs |
| **Multi-exchange analytical depth** | 3+ substantive exchanges producing insights, not just commands |
| **Lengthy descriptive output** | Response exceeds ~500 words of analytical, explanatory, or research content |

### DO NOT CAPTURE (skip silently)

| Skip condition | Examples |
|---|---|
| Simple refactoring | Rename variable, extract method, inline function |
| One-line fixes | Quick bug fix, typo correction, missing import |
| Formatting / linting | Run linter, fix whitespace, reformat code |
| Simple file operations | Create file, move file, delete file |
| Quick factual answers | "What's the return type of X?", "How do I import Y?" |
| Build / compile commands | Run build, fix compilation error (unless complex) |
| Simple git operations | Commit, push, create branch |
| Routine tasks | No analytical depth, no learning value |
| Explicit user opt-out | User says "don't capture this" or "no session log" |

### Edge Cases

- **Starts simple, becomes complex:** Begin capture mid-conversation when complexity emerges.
  Retroactively include the earlier exchanges that led to the complex discussion.
- **User explicitly requests capture:** Always honour `/capture` or "capture this session."
- **User explicitly declines capture:** Always honour "don't capture" or "skip session log."

---

## Domain Classification

Every captured session belongs to exactly **one domain**:

| Domain | Folder | When to use |
|---|---|---|
| `work` | `sessions/work/` | Corporate tasks, job-related code, employer projects, team work |
| `personal` | `sessions/personal/` | Personal projects, self-learning, side projects, personal finance, hobbies |

**Heuristic:** If the code, project, or topic is something you'd discuss with your manager
or team, it's `work`. If it's something you'd do on your own time, it's `personal`.
When ambiguous, default to `work`.

---

## Category Classification

Within each domain, sessions are filed into a **category** folder:

### Work Categories

| Category | Folder | Use for |
|---|---|---|
| `code-analysis` | `work/code-analysis/` | Code review, architecture review, pattern identification, refactoring analysis |
| `debugging` | `work/debugging/` | Complex bug investigation, RCA, hypothesis-driven debugging |
| `requirements` | `work/requirements/` | User stories, acceptance criteria, BDD, requirements research |
| `performance` | `work/performance/` | Profiling, optimization, benchmarking, load testing analysis |
| `feature-exploration` | `work/feature-exploration/` | Design alternatives, POC planning, feasibility, spike stories |
| `documentation` | `work/documentation/` | API docs, design docs, architecture overviews, runbooks |
| `research` | `work/research/` | Technology evaluation, library comparison, protocol analysis |
| `general` | `work/general/` | Anything that doesn't fit the above categories |

### Personal Categories

| Category | Folder | Use for |
|---|---|---|
| `learning` | `personal/learning/` | Concept deep-dives, tutorials, skill-building, interview prep |
| `project-dev` | `personal/project-dev/` | Personal project development, side-project architecture |
| `requirements` | `personal/requirements/` | User stories, acceptance criteria, BDD, feature scoping for personal projects |
| `financial` | `personal/financial/` | Budgeting, investment analysis, tax strategies, financial planning |
| `research` | `personal/research/` | Personal interest research, tool evaluation, comparison analysis |
| `general` | `personal/general/` | Anything that doesn't fit the above categories |

**New categories can be created** when 3+ sessions don't fit existing ones. Follow
kebab-case naming and add a README.md to the new folder.

---

## Requirements Gathering — Enhanced Capture Protocol

Requirements sessions for personal software development projects use the specialised
`requirements-capture.md` template (in `sessions/_templates/`) instead of the generic
session template. This ensures structured capture of user stories, acceptance criteria,
NFRs, and scope definitions.

### When Is a Session a Requirements Session?

A session is classified as `requirements` (not `project-dev`) when the primary focus is
understanding **WHAT to build** rather than **HOW to build it**:

| Requirements (`requirements`) | Development (`project-dev`) |
|---|---|
| Defining user stories and acceptance criteria | Implementing a feature |
| Scoping a feature (in/out, MoSCoW) | Choosing between design patterns |
| Writing BDD scenarios (Given/When/Then) | Writing code, debugging |
| Identifying NFRs (performance, security) | Optimising existing code |
| Story mapping or user journey analysis | Architecture or component design |
| Discovery sessions (problem exploration) | POC or prototype implementation |

### Requirements Capture Structure

Requirements sessions include these additional sections beyond the standard session
template (see `_templates/requirements-capture.md`):

1. **Project Overview** — project name, domain, target user, stage
2. **Problem Statement** — user-perspective description of the problem
3. **User Stories** — As/I want/So that format with Gherkin acceptance criteria
4. **Non-Functional Requirements** — measurable targets per FURPS+ category
5. **Scope Definition** — explicit in-scope / out-of-scope lists
6. **Dependencies & Constraints** — blockers, technical or external constraints
7. **Open Questions** — unresolved items needing follow-up

### Requirements Session Frontmatter

Requirements sessions use `category: requirements` and should include requirements-specific
tags from this vocabulary:

```text
Tags: requirements, user-stories, acceptance-criteria, bdd, nfr, scope,
      discovery, story-mapping, prioritisation, stakeholder-analysis,
      domain-modelling, event-storming, spike
```

### Requirements Session Naming Examples

```text
sessions/personal/requirements/
  2026-03-20_02-15pm_requirements_task-manager-mvp-scope.md
  2026-03-21_10-00am_requirements_task-manager-recurring-tasks.md
  2026-03-22_04-30pm_requirements_expense-tracker-budget-rules.md
  2026-03-25_09-00am_requirements_portfolio-site-content-model.md

sessions/work/requirements/
  2026-03-20_11-00am_requirements_user-auth-flow-oauth2.md
  2026-03-21_03-00pm_requirements_order-service-cancellation-policy.md
```

### Requirements Versioning Pattern

Requirements evolve across multiple sessions. Use versioning when refining the same
feature scope:

```text
# v1 — initial scoping
2026-03-20_02-15pm_requirements_task-manager-mvp-scope.md

# v2 — refined after technical spike
2026-03-22_10-00am_requirements_task-manager-mvp-scope_v2.md
  parent: 2026-03-20_02-15pm_requirements_task-manager-mvp-scope.md

# v3 — final scope after user feedback
2026-03-25_01-00pm_requirements_task-manager-mvp-scope_v3.md
  parent: 2026-03-22_10-00am_requirements_task-manager-mvp-scope_v2.md
```

### Requirements to Implementation Traceability

After requirements sessions are captured, link forward to implementation:

```markdown
## Traceability

| Story | Session | Implementation | Tests | Status |
|---|---|---|---|---|
| US-001 | [v1](../requirements/2026-03-20_...) | `src/task/TaskService.java` | `TaskServiceTest` | In progress |
| US-002 | [v2](../requirements/2026-03-22_...) | — | — | Not started |
```

This enables the full chain: **requirements session → implementation → tests**.

---

## File Naming Protocol

### Standard Pattern

```text
YYYY-MM-DD_HH-MMtt_<category>_<subject>[_v<N>].md
```

| Segment | Format | Example |
|---|---|---|
| Date | `YYYY-MM-DD` (ISO 8601) | `2026-03-20` |
| Time | `HH-MMtt` (12-hour, lowercase am/pm) | `10-30am`, `04-12pm` |
| Category | kebab-case, matches folder name | `code-analysis`, `research` |
| Subject | kebab-case, descriptive (3-8 words) | `OrderService-calculateTotal` |
| Version | `_v<N>` suffix, only for continuations (v2+) | `_v2`, `_v3` |

### Subject Naming Rules

1. **Lowercase kebab-case** — `order-service-validation`, not `OrderService_Validation`
2. **Class references** — use kebab-case: `order-service-calculate-total` (not dots or camelCase)
3. **3-8 words** — descriptive but concise
4. **Most specific first** — `hashmap-vs-treemap-performance`, not `performance-comparison-hashmap-treemap`
5. **No filler words** — drop "the", "a", "an", "for", "and" unless critical for meaning
6. **Method-level specificity** — include method name when the session is about a specific method

### Full Examples

```text
2026-03-20_10-30am_code-analysis_order-service-calculate-total.md
2026-03-20_02-15pm_research_mcp-transport-sse-vs-stdio.md
2026-03-20_04-12pm_requirements_user-auth-flow-oauth2.md
2026-03-20_11-00am_performance_database-query-n-plus-one.md
2026-03-20_03-45pm_feature-exploration_chat-session-capture-system.md
2026-03-20_09-15am_debugging_npe-in-config-loader-init.md
2026-03-20_01-30pm_financial_tax-optimization-freelance-income.md
2026-03-20_05-00pm_learning_java-virtual-threads-deep-dive.md
2026-03-21_10-00am_code-analysis_order-service-calculate-total_v2.md
```

---

## Sub-Package Escalation

When **5+ session files** accumulate for the **same subject** within a category, create
a sub-package (sub-directory) to group them:

### Before escalation (flat)

```text
work/code-analysis/
  2026-03-20_10-30am_code-analysis_order-service-calculate-total.md
  2026-03-21_02-15pm_code-analysis_order-service-calculate-total_v2.md
  2026-03-22_09-00am_code-analysis_order-service-validate-order.md
  2026-03-23_11-30am_code-analysis_order-service-process-payment.md
  2026-03-24_10-00am_code-analysis_order-service-cancel-order.md
```

### After escalation (sub-package)

```text
work/code-analysis/order-service/
  2026-03-20_10-30am_calculate-total.md
  2026-03-21_02-15pm_calculate-total_v2.md
  2026-03-22_09-00am_validate-order.md
  2026-03-23_11-30am_process-payment.md
  2026-03-24_10-00am_cancel-order.md
```

**Rules:**

- Sub-package name = kebab-case subject grouping (e.g., `order-service`)
- Files inside drop the category and subject prefix (already implied by folder)
- Add a `README.md` to the sub-package listing its contents
- Move existing files when escalating (update SESSION-LOG.md paths)

---

## Versioning Protocol

When a session **continues analysis on the same subject** from a previous session:

| Version | Convention | When |
|---|---|---|
| v1 (implicit) | No `_v<N>` suffix | First session on this subject |
| v2 | `_v2` suffix | Second session continuing from v1 |
| v3+ | `_v3`, `_v4`, etc. | Further continuations |

### Frontmatter versioning fields

```yaml
version: 2
parent: 2026-03-20_10-30am_code-analysis_order-service-calculate-total.md
```

### When to version vs. create new

- **Same subject, continued analysis** → version (v2, v3)
- **Same class/area, different method/aspect** → new file (not a version)
- **Same topic, weeks later, fresh perspective** → new file (too stale for versioning)

---

## Frontmatter Schema

Every captured session file uses this frontmatter:

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
  - performance impact analysis completed
source: copilot
---
```

### Field Reference

| Field | Required | Values | Purpose |
|---|---|---|---|
| `date` | Yes | `YYYY-MM-DD` | Session date |
| `time` | Yes | `"HH:MM AM/PM"` (quoted) | Session start time |
| `kind` | Yes | `session-capture` | Always this value for captured sessions |
| `domain` | Yes | `work` or `personal` | Domain classification |
| `category` | Yes | See category tables above | Category classification |
| `project` | Yes | kebab-case project name | Project or context bucket |
| `subject` | Yes | kebab-case subject slug | What the session is about |
| `tags` | Yes | 3-7 tags, lowercase-hyphens | Searchable metadata |
| `status` | Yes | `draft`, `final`, `archived` | Lifecycle state |
| `version` | Yes | Integer starting at 1 | Version number |
| `parent` | Yes | `null` or filename of previous version | Version chain |
| `complexity` | Yes | `high` or `medium` | Complexity signal |
| `outcomes` | No | List of key outcomes | What was learned/decided/produced |
| `source` | Yes | `copilot` | Always copilot for captured sessions |
| `scope` | Yes | `global`, `project`, `feature` | Applicability level — see session-scoping instructions |
| `scope-project` | Conditional | `null` or kebab-case | Required when scope is `project` or `feature` |
| `scope-feature` | Conditional | `null` or kebab-case | Required when scope is `feature` |
| `scope-transitions` | Yes | List (can be empty) | Log of scope changes during the session |
| `scope-refs` | Yes | List (can be empty) | Cross-references to sessions at different scopes |

> **Session Scoping:** Sessions can operate at global, project, or feature scope.
> Scope can change mid-session (widen, narrow, switch, or fork). See
> `.github/instructions/session-scoping.instructions.md` for the full scoping protocol,
> including transition logging, cross-reference relationships, and the `/session-scope` command.

---

## Captured Session Content Structure

Every captured session file follows this structure:

```markdown
---
(frontmatter)
---

# <Session Title — Human-Readable>

> **Context:** Brief 1-2 sentence context of what prompted this session.

---

## Request

<The user's original request or question — paraphrased or quoted.>

---

## Analysis / Response

<The substantive content of the AI response. This is the main body.
Use appropriate headings (H3, H4) to structure the content.
Include code blocks, tables, diagrams as produced.>

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
| Related sessions | [link to related session if any] |
```

### Multi-Exchange Sessions

For sessions with multiple distinct exchanges (Q&A pairs), use numbered sections:

```markdown
## Exchange 1 — <Topic>

### Request

...

### Response

...

## Exchange 2 — <Topic>

### Request

...

### Response

...
```

---

## Session Log — Append-Only Index

Every captured session is logged in `brain/ai-brain/sessions/SESSION-LOG.md`.
Append a new row to the table after creating each session file.

```markdown
| Date | Time | Domain | Category | Subject | Ver | Complexity | File |
|---|---|---|---|---|---|---|---|
| 2026-03-20 | 10:30 AM | work | code-analysis | order-service-calculate-total | v1 | high | [View](work/code-analysis/...) |
```

---

## Capture Execution Protocol

When the Capture Gate triggers, execute these steps:

1. **Classify** — determine domain + category from conversation context
2. **Name** — construct filename using the naming protocol
3. **Create directory** — ensure the category folder exists under the domain
4. **Write file** — create the session capture file with frontmatter + content structure
5. **Log** — append entry to SESSION-LOG.md
6. **Notify** — briefly inform the user: "Session captured to `sessions/<path>`"

### Timing

- **Capture at end of response** — write the file after the substantive response is complete
- **Multi-exchange sessions** — create the file after the first qualifying exchange, then
  append subsequent exchanges to the same file
- **Retroactive capture** — if the conversation becomes complex mid-way, create the file
  and include earlier relevant exchanges

---

## User Controls

| Command | Effect |
|---|---|
| "capture this session" or `/capture` | Force capture regardless of gate criteria |
| "don't capture this" or "no session log" | Suppress capture for this conversation |
| "capture to work/research" | Force capture to a specific domain/category |
| "capture to personal/learning" | Force capture to a specific domain/category |

---

## Integration with Existing Brain Tiers

The `sessions/` tier is the **4th tier** alongside inbox/notes/library:

```text
ai-brain/
  inbox/       TEMP       raw capture — gitignored
  notes/       YOURS      your distilled writing — tracked
  library/     SOURCES    imported materials — tracked
  sessions/    CAPTURED   AI conversation captures — tracked
```

### Routing Extension

> **Did you write it yourself?** → notes/
> **Did you import it?** → library/
> **Was it a captured AI conversation worth preserving?** → sessions/
> **Not ready yet?** → inbox/

### Promotion Path

Captured sessions can be **promoted** to notes/ when you distil them:
- Read the session capture
- Write your own synthesis/insight note in notes/
- Link back to the session: `Source: [session](../sessions/work/...)`
- The session capture stays as a reference; the note is your knowledge

---

## Git Tracking

The `sessions/` tier is **git-tracked** (like notes/ and library/).
Captured sessions are committed as part of normal workflow.

### Commit Convention

```text
brain(sessions): capture <category> — <subject>

Captured <domain>/<category> session on <subject>.
Complexity: <high|medium>. Version: v<N>.

— created by gpt
```
