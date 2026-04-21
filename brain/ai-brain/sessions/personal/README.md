# personal/ — Personal Sessions

> Captured AI chat sessions related to personal activities — growth, learning,
> side projects, personal finance, hobbies, and non-work exploration.

---

## Categories

### Top-Level

| Category | Folder | Use for |
|---|---|---|
| `personal-work` | `personal-work/` | **Umbrella** — personal growth, learning, and side projects (see sub-categories below) |
| `financial` | `financial/` | Budgeting, investment analysis, tax strategies, financial planning |
| `research` | `research/` | Personal interest research NOT about software development |
| `general` | `general/` | Sessions not fitting the above categories |

### Personal Work Sub-Categories (`personal-work/`)

Personal work covers learning, growth, skill-building, and side projects. It contains:

| Sub-Category | Folder | Use for |
|---|---|---|
| `software-dev` | `personal-work/software-dev/` | **Umbrella** — all personal software development (full lifecycle) |
| `learning` | `personal-work/learning/` | Concept deep-dives, tutorials, skill-building, interview prep (not project-specific) |

See [personal-work/README.md](personal-work/README.md) for full details including
software-dev activity sub-categories (requirements, design, implementation, etc.).

---

## Naming

```text
YYYY-MM-DD_HH-MMtt_<category>_<subject>[_v<N>].md
```

The `<category>` segment matches the **leaf folder** name (e.g., `requirements`,
`design`, `implementation`, `learning`) — not the full path.

### Examples

```text
# Software-dev umbrella (category = leaf folder name)
personal-work/software-dev/requirements/2026-03-20_02-15pm_requirements_task-manager-mvp-scope.md
personal-work/software-dev/design/2026-03-20_03-00pm_design_task-manager-api-endpoints.md
personal-work/software-dev/implementation/2026-03-20_11-00am_implementation_task-manager-crud-endpoints.md

# Learning (under personal-work)
personal-work/learning/2026-03-20_05-00pm_learning_java-virtual-threads-deep-dive.md

# Stand-alone categories (directly under personal/)
financial/2026-03-20_01-30pm_financial_tax-optimization-freelance-income.md
```

---

## Routing Heuristic

- **Building / designing / testing something for a personal software project?** →
  `personal-work/software-dev/<activity>`
- **Pure concept learning with no project context?** → `personal-work/learning/`
- **Work-related tasks (corporate)?** → `sessions/work/`
- **Personal interest research NOT about software?** → `research/`
- **Financial guidance?** → `financial/`
---

## Navigation

| Link | Description |
|---|---|
| [personal-work/](personal-work/README.md) | Umbrella: learning, growth, side projects |
| [financial/](financial/README.md) | Budgeting, investment, tax strategies |
| [research/](research/README.md) | Personal interest research (non-SW) |
| [general/](general/README.md) | Sessions not fitting other categories |
| [sessions/](../README.md) | Parent: sessions root |
