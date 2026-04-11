```prompt
---
name: todo
description: 'Alias for /jot — adds a concrete task to the backlog. Use /jot directly for full capture power.'
agent: copilot
tools: ['editFiles', 'codebase', 'terminalLastCommand', 'runInTerminal']
---

## What needs to be done?

${input:task:Describe the task (e.g., "Fix search bug in vault", "Add dark mode to dashboard")}

---

## Instructions

**`/todo` is an alias for `/jot`.** Route the user's input through the unified `/jot`
capture-and-enhance pipeline.

Read `.github/prompts/jot.prompt.md` for the full protocol.

Treat `${input:task}` as task-classified input — skip Phase 1 classification (it's a
task), go directly to Phase 2 enhancement (title, type, priority, tags, epic, effort,
AC, breakdown) → Phase 3 cross-reference → Phase 4 confirm → Phase 5 board sync.

All rules from `/jot` apply: auto-enhance, auto-breakdown, file reading, cross-referencing,
and full board sync (BOARD.md + views/ + CHANGELOG.md).
```
