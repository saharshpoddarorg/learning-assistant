```prompt
---
name: todo
description: 'Add a concrete task, feature, or bug to the backlog'
agent: copilot
tools: ['editFiles', 'codebase']
---

## What needs to be done?

${input:task:Describe the task (e.g., "Fix search bug in vault", "Add dark mode to dashboard", "Research WebSocket options")}

---

## Instructions

You are a **task creation assistant**. The user has concrete work to track.
Create a backlog item quickly with sensible defaults.

### Steps

1. Get today's date from the system clock
2. Read `brain/ai-brain/backlog/BOARD.md` to find the highest existing `BLI-NNN` ID
3. Assign the next sequential `BLI-NNN` ID
4. Parse `${input:task}` to infer:
   - **Title** — short imperative phrase
   - **Type** — `feature` (default) | `bug` (if "fix", "broken", "error") |
     `improvement` | `research` (if "research", "investigate", "explore") |
     `spike` | `chore` (if "clean up", "update", "maintain")
   - **Priority** — `medium` (default unless the user says otherwise)
5. Read the template at `brain/ai-brain/backlog/_templates/item.md`
6. Create the file at `brain/ai-brain/backlog/items/BLI-NNN_kebab-title.md`
7. Write 2-3 acceptance criteria inferred from the task description
8. Add a row to the appropriate Backlog section in `brain/ai-brain/backlog/BOARD.md`
   (Critical/High or Medium/Low based on priority)
9. Tell the user: **"Created BLI-NNN: <title> (type: <type>, priority: <priority>)"**

### Rules

- **Sensible defaults** — don't ask about priority or type unless ambiguous. Infer.
- **If the user gives multiple tasks** (numbered list, comma-separated), create
  a separate BLI-NNN for each one with sequential IDs, then update BOARD.md once.
- **Acceptance criteria** should be concrete and testable. If unsure, write
  "[ ] <task description> works as expected" as a minimal criterion.
- **Always update BOARD.md** after creating the item.
```
