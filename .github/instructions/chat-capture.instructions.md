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

#### Top-Level Personal Categories

| Category | Folder | Use for |
|---|---|---|
| `software-dev` | `personal/software-dev/` | **Umbrella** — all personal software development projects (see sub-categories below) |
| `learning` | `personal/learning/` | Concept deep-dives, tutorials, skill-building, interview prep (not tied to a specific project) |
| `financial` | `personal/financial/` | Budgeting, investment analysis, tax strategies, financial planning |
| `research` | `personal/research/` | Personal interest research NOT about software development (hobbies, tools, life decisions) |
| `general` | `personal/general/` | Anything that doesn't fit the above categories |

#### Software Development Sub-Categories (`personal/software-dev/`)

Personal software development is an umbrella that covers the full lifecycle. Sessions
are filed by **activity phase**, not by project name:

| Sub-Category | Folder | Use for |
|---|---|---|
| `requirements` | `software-dev/requirements/` | User stories, acceptance criteria, BDD, feature scoping, discovery sessions |
| `research` | `software-dev/research/` | Technology evaluation, library comparison, feasibility spikes for s/w projects |
| `design` | `software-dev/design/` | Architecture, component design, HLD/LLD, system design, pattern selection, ADRs |
| `implementation` | `software-dev/implementation/` | Coding sessions, feature building, complex debugging during dev |
| `testing` | `software-dev/testing/` | Test strategy, TDD/BDD setup, test plans, quality assurance |
| `code-review` | `software-dev/code-review/` | Code analysis, refactoring review, pattern identification |
| `devops` | `software-dev/devops/` | CI/CD, deployment, infrastructure, containerisation for personal projects |
| `general` | `software-dev/general/` | Software dev sessions not fitting the above activities |

**Routing heuristic for software-dev:** If the session is about building, designing,
testing, or researching something *for a personal software project*, it belongs under
`software-dev/<activity>`. If it's pure concept learning with no project context, it
belongs in `personal/learning/`.

**New categories can be created** when 3+ sessions don't fit existing ones. Follow
kebab-case naming and add a README.md to the new folder.

---

## Project-Aware Session Protocol

### Automatic Project Detection

When a user starts a chat that references a **personal software development project**,
the AI assistant should automatically detect and scope the session. Detection triggers:

| Signal | Example | Action |
|---|---|---|
| Mentions a GitHub repo name | "let's work on ABSDevelopment" | Set `scope: project`, `scope-project: abs-development` |
| Mentions a GitHub org/user repo | "my repo saharshpoddarorg/task-manager" | Set `scope: project`, `scope-project: task-manager` |
| Names a project explicitly | "for my expense tracker project" | Set `scope: project`, `scope-project: expense-tracker` |
| Describes a new project idea | "I want to build a recipe sharing app" | Set `scope: project`, `scope-project: recipe-sharing-app` |
| References existing session project | "back to the task-manager" | Inherit scope from prior sessions |
| Uses keywords: MVP, feature, epic | "let's scope the MVP for ..." | Set `scope: feature` if specific enough |

**Protocol when a project is detected:**

1. Set `scope: project` (or `feature` if a specific feature is named)
2. Set `scope-project` to kebab-case project name
3. Route to `personal/software-dev/<activity>/` based on the conversation's focus
4. If activity is ambiguous, ask: "Are we doing requirements, design, or implementation?"
5. Create the project index file (`<project>-INDEX.md`) if it doesn't already exist and
   the project has 3+ sessions across different activities

### Activity Context Switching Within a Project

Real conversations naturally move between activities. When the user context-switches
within the same project session:

| User says | Activity transition | Action |
|---|---|---|
| "now let's design the API" | requirements → design | Log `scope-transition`, continue in same file |
| "let's start coding this" | design → implementation | Log `scope-transition`, consider fork/split |
| "wait, I need to rethink the requirements" | implementation → requirements | Log `scope-transition`, continue |
| "what does the competitor do?" | any → research | Log `scope-transition`, widen if general |
| "let's write tests for this" | implementation → testing | Log `scope-transition` |
| "how should we deploy this?" | any → devops | Log `scope-transition` |
| "let me review what we have" | any → code-review | Log `scope-transition` |

**Context-switch protocol:**

1. **Log the transition** in `scope-transitions` with exchange number, from/to, and reason
2. **Annotate** with a scope boundary marker in the session body (see session-scoping instructions)
3. **Don't fork immediately** — keep the conversation in one file unless:
   - The new activity segment becomes substantial (3+ exchanges with depth)
   - The activity switch is to a completely different project
   - The user explicitly requests splitting
4. **Update the category field** to reflect the dominant activity when the session ends
5. **Cross-reference** — if a fork happens, add bidirectional `scope-refs` in both files

### Additional Activity Types for Project Work

Beyond the standard software-dev activities, project sessions may involve:

| Activity | Route to | Examples |
|---|---|---|
| Competitor analysis | `software-dev/research/` | "What does Todoist do differently?" |
| Customer requirements | `software-dev/requirements/` | "What do users expect from..." |
| Market research | `software-dev/research/` | "Is there demand for..." |
| Technology spikes | `software-dev/research/` | "Can we use WebSockets for..." |
| Architecture decisions | `software-dev/design/` | "Should we use microservices or monolith?" |
| Database schema design | `software-dev/design/` | "How should we model the data?" |
| API design | `software-dev/design/` | "What endpoints do we need?" |
| Security planning | `software-dev/design/` | "How do we handle auth?" |
| Performance planning | `software-dev/design/` | "How do we handle 10k concurrent users?" |
| Deployment strategy | `software-dev/devops/` | "Docker vs Kubernetes for this?" |
| Cost analysis | `software-dev/research/` | "What's the cloud hosting cost?" |

### Tagging and Keyword System

Every captured session uses tags for cross-cutting discoverability. Tags complement the
folder hierarchy — folders organize by activity, tags enable search across activities.

#### Tag Vocabulary by Domain

**Project tags** (always include when project-scoped):

```text
project:<project-name>    ← mandatory for project-scoped sessions
gh:<owner/repo>           ← when linked to a GitHub repository
```

**Activity tags** (2-3 per session, describing what was done):

```text
requirements, user-stories, acceptance-criteria, bdd, nfr, scope, discovery,
story-mapping, prioritisation, stakeholder-analysis, domain-modelling,
event-storming, spike, competitor-analysis, market-research, customer-needs,
architecture, system-design, hld, lld, api-design, database-design, adr,
component-design, pattern-selection, security-design, performance-design,
implementation, coding, feature-building, debugging, refactoring, integration,
tdd, bdd-testing, test-strategy, e2e-testing, unit-testing, test-plan,
code-review, code-analysis, refactoring-review, pattern-identification,
ci-cd, docker, kubernetes, deployment, infrastructure, monitoring,
tech-evaluation, library-comparison, feasibility, trade-off-analysis,
cost-analysis, poc, prototype
```

**Technology tags** (specific technologies discussed):

```text
java, spring-boot, react, typescript, python, docker, kubernetes,
postgresql, mongodb, redis, graphql, rest, grpc, websockets, oauth,
jwt, terraform, github-actions, etc.
```

#### Tag Rules

1. **3-7 tags per session** — enough for discoverability, not so many they lose meaning
2. **Always include `project:<name>`** when the session is project-scoped
3. **Include `gh:<owner/repo>`** when linked to a GitHub repository
4. **Mix activity + technology tags** — e.g., `[project:task-manager, requirements, api-design, rest, java]`
5. **Use the standard vocabulary** above — avoid inventing synonyms
6. **Tags are lowercase kebab-case** — no spaces, no camelCase

---

## Requirements Gathering — Enhanced Capture Protocol

Requirements sessions for personal software development projects use the specialised
`requirements-capture.md` template (in `sessions/_templates/`) instead of the generic
session template. This ensures structured capture of user stories, acceptance criteria,
NFRs, and scope definitions.

### When Is a Session a Requirements Session?

A session is classified as `requirements` when the primary focus is understanding
**WHAT to build** rather than **HOW to build it**. All personal software development
categories live under the `software-dev/` umbrella:

| `software-dev/requirements` | `software-dev/design` | `software-dev/implementation` |
|---|---|---|
| Defining user stories and acceptance criteria | Architecture, component design | Writing code, building features |
| Scoping a feature (in/out, MoSCoW) | Choosing between design patterns | Debugging during development |
| Writing BDD scenarios (Given/When/Then) | System design, HLD/LLD | Implementing a POC or prototype |
| Identifying NFRs (performance, security) | ADRs, pattern selection | Optimising existing code |
| Story mapping or user journey analysis | API contract design | Integration work |
| Discovery sessions (problem exploration) | Database schema design | Refactoring implementation |

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
sessions/personal/software-dev/requirements/
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

> **Timestamp must be real.** Always query the system clock (`Get-Date` / `date`) before
> naming a file. Never guess or round the time. See **Timestamp Accuracy** under
> Capture Execution Protocol for the full rules.

### Subject Naming Rules

1. **Lowercase kebab-case** — `order-service-validation`, not `OrderService_Validation`
2. **Class references** — use kebab-case: `order-service-calculate-total` (not dots or camelCase)
3. **3-8 words** — descriptive but concise
4. **Most specific first** — `hashmap-vs-treemap-performance`, not `performance-comparison-hashmap-treemap`
5. **No filler words** — drop "the", "a", "an", "for", "and" unless critical for meaning
6. **Method-level specificity** — include method name when the session is about a specific method

### Full Examples

```text
# Work domain (flat categories)
2026-03-20_10-30am_code-analysis_order-service-calculate-total.md
2026-03-20_02-15pm_research_mcp-transport-sse-vs-stdio.md
2026-03-20_04-12pm_requirements_user-auth-flow-oauth2.md
2026-03-20_11-00am_performance_database-query-n-plus-one.md
2026-03-20_03-45pm_feature-exploration_chat-session-capture-system.md
2026-03-20_09-15am_debugging_npe-in-config-loader-init.md
2026-03-21_10-00am_code-analysis_order-service-calculate-total_v2.md

# Personal domain — software-dev umbrella (category = leaf folder name)
2026-03-20_02-15pm_requirements_task-manager-mvp-scope.md
2026-03-20_03-00pm_design_task-manager-api-endpoints.md
2026-03-20_11-00am_implementation_task-manager-crud-endpoints.md
2026-03-20_01-00pm_testing_task-manager-e2e-strategy.md
2026-03-20_04-30pm_research_react-vs-svelte-frontend-choice.md
2026-03-21_09-00am_code-review_task-manager-service-layer-patterns.md
2026-03-21_02-00pm_devops_task-manager-docker-compose-setup.md

# Personal domain — stand-alone categories
2026-03-20_01-30pm_financial_tax-optimization-freelance-income.md
2026-03-20_05-00pm_learning_java-virtual-threads-deep-dive.md
```

---

## Sub-Package Escalation

Sessions naturally cluster around shared subjects or projects. Two escalation patterns
keep folders navigable as volume grows.

### Pattern 1 — Subject-Based Sub-Package (5+ files)

When **5+ session files** accumulate for the **same subject** within a category, create
a sub-package (sub-directory) to group them:

#### Before escalation (flat)

```text
work/code-analysis/
  2026-03-20_10-30am_code-analysis_order-service-calculate-total.md
  2026-03-21_02-15pm_code-analysis_order-service-calculate-total_v2.md
  2026-03-22_09-00am_code-analysis_order-service-validate-order.md
  2026-03-23_11-30am_code-analysis_order-service-process-payment.md
  2026-03-24_10-00am_code-analysis_order-service-cancel-order.md
```

#### After escalation (sub-package)

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

### Automatic Escalation Protocol

The AI assistant MUST proactively check escalation thresholds **every time a session
file is created**. Do not wait for the user to notice or request escalation.

**Escalation checklist (run after every session capture):**

1. **Count files** — count how many session files exist in the target category folder
   that share the same subject grouping (look at the subject slug prefix)
2. **Check Pattern 1 threshold** — if the count (including the new file) reaches **5+**
   files for the same subject, trigger subject-based sub-package escalation immediately
3. **Check Pattern 2 threshold** — if inside `personal/software-dev/<activity>/` and the
   count reaches **3+** files for the same project, trigger project-based sub-package
   escalation immediately
4. **Execute escalation** — when triggered:
   - Create the sub-directory
   - Move ALL matching files into the sub-directory with shortened names
   - Create `README.md` listing the sub-package contents
   - Update `SESSION-LOG.md` with new paths
   - Update all `scope-refs`, `parent`, and inline links in the moved files
   - Update any cross-references in files OUTSIDE the sub-package that point to moved files
5. **Log the escalation** — inform the user that escalation was performed

**Timing:** Escalation should happen in the same turn as the session capture. Do not
create a file in a flat folder and plan to escalate later — check and escalate immediately.

**Cross-reference updates are mandatory.** When files are moved during escalation, ALL of
the following must be updated:

- `parent` field in frontmatter (if pointing to a moved file)
- `scope-refs[].file` entries in frontmatter (paths to moved files)
- Inline markdown links `[text](path)` that reference moved files
- `SESSION-LOG.md` link paths
- Any `README.md` files that list or link to the moved files

### Pattern 2 — Project-Based Sub-Package (3+ files in same activity for same project)

Within `personal/software-dev/<activity>/`, when **3+ sessions** relate to the **same
project**, create a project sub-folder:

#### Before escalation (flat)

```text
personal/software-dev/requirements/
  2026-03-20_02-15pm_requirements_task-manager-mvp-scope.md
  2026-03-21_10-00am_requirements_task-manager-recurring-tasks.md
  2026-03-22_04-30pm_requirements_task-manager-notification-rules.md
  2026-03-25_09-00am_requirements_expense-tracker-budget-rules.md
```

#### After escalation (project sub-package)

```text
personal/software-dev/requirements/task-manager/
  2026-03-20_02-15pm_mvp-scope.md
  2026-03-21_10-00am_recurring-tasks.md
  2026-03-22_04-30pm_notification-rules.md

personal/software-dev/requirements/
  2026-03-25_09-00am_requirements_expense-tracker-budget-rules.md
```

**Rules:**

- Project sub-package name = kebab-case project name (e.g., `task-manager`)
- Files inside drop the category and project prefix (implied by folder path)
- Add a `README.md` to the project sub-package listing its contents
- Threshold is **3+ files** (lower than subject escalation because project cohesion
  is a stronger grouping signal)
- Move existing files when escalating (update SESSION-LOG.md paths)

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

This index provides a single entry point for all sessions related to one project,
without duplicating files across folders.

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
3. **Timestamp** — obtain the **actual current local time** (see Timestamp Accuracy below)
4. **Create directory** — ensure the category folder exists under the domain
5. **Escalation check** — check if this file triggers sub-package escalation (see below)
6. **Write file** — create the session capture file with frontmatter + content structure
7. **Log** — append entry to SESSION-LOG.md
8. **Notify** — briefly inform the user: "Session captured to `sessions/<path>`"

### Timestamp Accuracy

**The date and time in session filenames and frontmatter MUST reflect the actual current
local time when the file is created.** Never guess, estimate, or use a placeholder time.

- **Always query the system clock** before naming a session file (e.g., `Get-Date` on
  PowerShell, `date` on bash) to obtain the real local time
- **Filename timestamp** uses 12-hour format: `HH-MMtt` (e.g., `09-21pm`, `10-30am`)
- **Frontmatter `time` field** uses quoted 12-hour format: `"09:21 PM"`, `"10:30 AM"`
- **Frontmatter `date` field** uses ISO 8601: `YYYY-MM-DD`
- **Never round or approximate** — if the time is 9:21 PM, use `09-21pm`, not `09-00pm`
  or `10-00pm`
- **When updating an existing file's timestamp** (e.g., fixing an error), rename the file
  to match the corrected timestamp and update the frontmatter accordingly
- **Multi-exchange sessions** — use the timestamp of the first qualifying exchange as the
  session start time; do not update the timestamp when appending later exchanges

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
