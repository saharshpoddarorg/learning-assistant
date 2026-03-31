# sprints/ — Sprint & Iteration Tracking

> Time-boxed iterations for focused, planned work. Each sprint has a goal,
> committed items, daily progress, and a retrospective.
>
> **Parent:** [BOARD.md](../BOARD.md) | **Template:** [sprint.md](../_templates/sprint.md)

---

## Sprint History

| Sprint | Goal | Status | Start | End | Committed | Completed | Velocity |
|---|---|---|---|---|---|---|---|

> No sprints yet. Use `/backlog sprint start "goal"` to begin one.

---

## How Sprints Work

### Sprint Lifecycle

```text
planned → active → completed
                 → cancelled (if abandoned)
```

### Commands

| Command | Effect |
|---|---|
| `/backlog sprint start "goal"` | Create a new sprint with a goal |
| `/backlog sprint add BLI-NNN` | Add an item to the active sprint |
| `/backlog sprint remove BLI-NNN` | Remove an item from the active sprint |
| `/backlog sprint status` | Show active sprint progress |
| `/backlog sprint end` | Complete the sprint, trigger retrospective |

### Rules

- Only **one active sprint** at a time
- Sprint duration: flexible (1-2 weeks recommended)
- Items not completed carry over to the next sprint
- Always fill in the retrospective when ending a sprint

---

## Naming

```text
SPRINT-NNN_kebab-title.md
```

Example: `SPRINT-001_initial-mcp-server-buildout.md`
