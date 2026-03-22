# software-dev/ — Personal Software Development Sessions

> **Umbrella category** for all personal software development projects. Sessions are
> organised by **activity phase** (what you're doing), not by project name.

---

## Activity Sub-Categories

| Activity | Folder | Use for |
|---|---|---|
| `requirements` | `requirements/` | User stories, acceptance criteria, BDD, feature scoping, discovery sessions |
| `research` | `research/` | Technology evaluation, library comparison, feasibility spikes for s/w projects |
| `design` | `design/` | Architecture, component design, HLD/LLD, system design, pattern selection, ADRs |
| `implementation` | `implementation/` | Coding sessions, feature building, complex debugging during dev |
| `testing` | `testing/` | Test strategy, TDD/BDD setup, test plans, quality assurance |
| `code-review` | `code-review/` | Code analysis, refactoring review, pattern identification |
| `devops` | `devops/` | CI/CD, deployment, infrastructure, containerisation for personal projects |
| `general` | `general/` | Software dev sessions not fitting the above activities |

Activity folders are created **on demand** — only when the first session of that
activity is captured.

---

## Project-Aware Workflow

### Automatic Project Detection

When a chat references a personal software project (GitHub repo, project name, or
project idea), the session is automatically scoped to that project. The system:

1. Sets `scope: project` and `scope-project: <project-name>`
2. Routes to the appropriate activity folder based on conversation focus
3. Tags with `project:<project-name>` and optionally `gh:<owner/repo>`
4. Creates a project index file when the project spans 3+ activities

### Context Switching Between Activities

Real conversations jump between activities. Within a single project session:

```text
User: "Let's scope the MVP for task-manager"           → requirements
User: "Now design the API endpoints"                    → design
User: "Wait, what does Todoist do differently?"         → research
User: "OK back to requirements — add recurring tasks"   → requirements
User: "Let's start coding the TaskService"              → implementation
```

Each switch is logged in `scope-transitions`. The session stays in one file unless
a new activity becomes substantial (3+ exchanges), at which point it forks into
a separate file with bidirectional cross-references.

### Additional Activities for Project Work

| Activity | Route to | Examples |
|---|---|---|
| Competitor analysis | `research/` | "What does Todoist do differently?" |
| Customer requirements | `requirements/` | "What do users expect from..." |
| Market research | `research/` | "Is there demand for..." |
| Technology spikes | `research/` | "Can we use WebSockets for..." |
| Architecture decisions | `design/` | "Microservices or monolith?" |
| Database schema | `design/` | "How should we model the data?" |
| API design | `design/` | "What endpoints do we need?" |
| Security planning | `design/` | "How do we handle auth?" |
| Deployment strategy | `devops/` | "Docker vs Kubernetes?" |
| Cost analysis | `research/` | "Cloud hosting cost?" |

---

## Routing Heuristic

| If the session is about... | Route to |
|---|---|
| Defining WHAT to build (stories, criteria, scoping) | `requirements/` |
| Evaluating technology or libraries for a project | `research/` |
| Architecture, patterns, system design, ADRs | `design/` |
| Writing code, building features, debugging during dev | `implementation/` |
| Test strategy, TDD/BDD, test plans | `testing/` |
| Reviewing existing code, refactoring analysis | `code-review/` |
| CI/CD, Docker, deployment, infrastructure | `devops/` |
| Pure concept learning (no project context) | `../learning/` (parent level) |

---

## Project-Based Grouping

When **3+ sessions** for the **same project** accumulate in one activity folder,
create a project sub-folder to group them:

```text
# Before (flat)
requirements/
  2026-03-20_02-15pm_requirements_task-manager-mvp-scope.md
  2026-03-21_10-00am_requirements_task-manager-recurring-tasks.md
  2026-03-22_04-30pm_requirements_task-manager-notification-rules.md

# After (project sub-package)
requirements/task-manager/
  README.md
  2026-03-20_02-15pm_mvp-scope.md
  2026-03-21_10-00am_recurring-tasks.md
  2026-03-22_04-30pm_notification-rules.md
```

---

## Cross-Cutting Project Index

When a project spans **3+ activity categories**, create a project index file at
`software-dev/<project-name>-INDEX.md` for a unified view:

```markdown
# task-manager — Session Index

| Activity | Sessions | Latest |
|---|---|---|
| requirements | 3 | [notification-rules](requirements/task-manager/...) |
| design | 2 | [api-endpoints](design/task-manager/...) |
| implementation | 4 | [crud-endpoints-v2](implementation/task-manager/...) |
| testing | 1 | [e2e-strategy](testing/...) |
```

---

## Naming

```text
YYYY-MM-DD_HH-MMtt_<activity>_<subject>[_v<N>].md
```

The `<activity>` segment matches the leaf folder name (e.g., `requirements`, `design`).

### Examples

```text
requirements/2026-03-20_02-15pm_requirements_task-manager-mvp-scope.md
design/2026-03-20_03-00pm_design_task-manager-api-endpoints.md
implementation/2026-03-20_11-00am_implementation_task-manager-crud-endpoints.md
testing/2026-03-20_01-00pm_testing_task-manager-e2e-strategy.md
research/2026-03-20_04-30pm_research_react-vs-svelte-frontend-choice.md
code-review/2026-03-21_09-00am_code-review_task-manager-service-layer-patterns.md
devops/2026-03-21_02-00pm_devops_task-manager-docker-compose-setup.md
```
