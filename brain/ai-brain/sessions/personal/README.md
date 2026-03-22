# personal/ — Personal Projects & Learning Sessions

> Captured AI chat sessions related to personal projects, self-directed learning,
> side projects, personal finance, hobbies, and non-work exploration.

---

## Categories

### Top-Level

| Category | Folder | Use for |
|---|---|---|
| `software-dev` | `software-dev/` | **Umbrella** — all personal software development (see sub-categories below) |
| `learning` | `learning/` | Concept deep-dives, tutorials, skill-building, interview prep (not project-specific) |
| `financial` | `financial/` | Budgeting, investment analysis, tax strategies, financial planning |
| `research` | `research/` | Personal interest research NOT about software development |
| `general` | `general/` | Sessions not fitting the above categories |

### Software Development Sub-Categories (`software-dev/`)

Personal software development is an umbrella covering the full lifecycle. Sessions are
filed by **activity phase**, not by project name:

| Sub-Category | Folder | Use for |
|---|---|---|
| `requirements` | `software-dev/requirements/` | User stories, acceptance criteria, BDD, feature scoping, discovery |
| `research` | `software-dev/research/` | Tech evaluation, library comparison, feasibility spikes |
| `design` | `software-dev/design/` | Architecture, component design, HLD/LLD, system design, ADRs |
| `implementation` | `software-dev/implementation/` | Coding sessions, feature building, debugging during dev |
| `testing` | `software-dev/testing/` | Test strategy, TDD/BDD setup, test plans, quality assurance |
| `code-review` | `software-dev/code-review/` | Code analysis, refactoring review, pattern identification |
| `devops` | `software-dev/devops/` | CI/CD, deployment, infrastructure, containerisation |
| `general` | `software-dev/general/` | Software dev sessions not fitting the above activities |

---

## Naming

```text
YYYY-MM-DD_HH-MMtt_<category>_<subject>[_v<N>].md
```

The `<category>` segment matches the **leaf folder** name (e.g., `requirements`,
`design`, `implementation`) — not the full path.

### Examples

```text
# Software-dev umbrella (category = leaf folder name)
software-dev/requirements/2026-03-20_02-15pm_requirements_task-manager-mvp-scope.md
software-dev/design/2026-03-20_03-00pm_design_task-manager-api-endpoints.md
software-dev/implementation/2026-03-20_11-00am_implementation_task-manager-crud-endpoints.md

# Stand-alone categories
learning/2026-03-20_05-00pm_learning_java-virtual-threads-deep-dive.md
financial/2026-03-20_01-30pm_financial_tax-optimization-freelance-income.md
```

---

## Routing Heuristic

- **Building / designing / testing something for a personal software project?** →
  `software-dev/<activity>`
- **Pure concept learning with no project context?** → `learning/`
- **Work-related tasks?** → `sessions/work/`
- **Personal interest research NOT about software?** → `research/`
- **Financial guidance?** → `financial/`
