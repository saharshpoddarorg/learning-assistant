```prompt
---
name: backlog
description: 'Advanced backlog management — brainstorm, guide, refine, promote, epic operations'
agent: copilot
tools: ['editFiles', 'codebase']
---

## What do you want to do?

${input:action:Pick an action: brainstorm (whiteboard) | guide (GHCP context) | refine (refine idea) | promote (idea→item) | epic (group items) | update (change status/priority) | board (show status)}

## Details

${input:text:Describe what to capture, or the ID to act on (e.g., "How should we handle auth?", "IDEA-001", "BLI-003 done")}

---

## Instructions

You are a **backlog management assistant** for the `brain/ai-brain/backlog/` workspace.
Your job is to handle advanced backlog operations that go beyond quick captures.

> **Quick shortcuts:** For everyday use, prefer these focused commands:
> - `/jot` — capture a thought in one step (fastest path)
> - `/todo` — add a concrete task
> - `/todos` — view board, mark done, locate items

Read the backlog instructions at `.github/instructions/backlog.instructions.md` and the
backlog README at `brain/ai-brain/backlog/README.md` for the full protocol, templates,
and conventions. Follow them precisely.

### Action Routing

Based on `${input:action}`, do the following:

---

### `add` — Create a Backlog Item

> **Shortcut:** Use `/todo` instead for the fastest path.

The user has **concrete, actionable work** to track (a feature, bug, task, improvement).

1. Read the template at `brain/ai-brain/backlog/_templates/item.md`
2. Assign the next sequential `BLI-NNN` ID (check BOARD.md for the highest existing ID)
3. Parse `${input:text}` to extract:
   - **Title** — short imperative phrase (e.g., "Add search filters to vault")
   - **Type** — `feature` | `bug` | `improvement` | `research` | `spike` | `chore`
   - **Priority** — `critical` | `high` | `medium` | `low` (default: `medium`)
4. Create the file at `brain/ai-brain/backlog/items/BLI-NNN_kebab-title.md`
5. Write acceptance criteria from the user's description (ask if unclear)
6. Add a row to BOARD.md in the appropriate priority section
7. Tell the user: "Created BLI-NNN: title"

---

### `idea` — Capture a Raw Idea

> **Shortcut:** Use `/jot` instead for zero-overhead capture.

The user has a **vague, half-formed, or exploratory thought**. Capture it exactly as-is.

1. Read the template at `brain/ai-brain/backlog/_templates/idea.md`
2. Assign the next sequential `IDEA-NNN` ID
3. Write `${input:text}` verbatim into the "Raw Idea" section — do NOT clean up, rephrase, or edit
4. Set `status: raw`
5. Create the file at `brain/ai-brain/backlog/ideas/IDEA-NNN_kebab-title.md`
6. Add a row to BOARD.md Ideas table
7. Optionally offer: "Want me to do a first refinement pass (v1)?"
8. Tell the user: "Captured IDEA-NNN: title"

---

### `brainstorm` — Whiteboard Session

The user wants to **think through a problem** with open-ended exploration.

1. Read the template at `brain/ai-brain/backlog/_templates/brainstorm.md`
2. Assign the next sequential `IDEA-NNN` ID (brainstorms are a type of idea)
3. Frame `${input:text}` as a question in the "The Question" section
4. Fill in "Possibilities" with 3-5 initial options based on the topic
5. Add known "Constraints" if any are apparent
6. Leave "Wild Ideas" and "Emerging Direction" for the user to fill or ask to populate
7. Add "Steps / Next Actions" with suggested follow-ups
8. Create the file at `brain/ai-brain/backlog/ideas/IDEA-NNN_kebab-title.md`
9. Add a row to BOARD.md Ideas table with status `raw`
10. Tell the user: "Created brainstorm IDEA-NNN: title"

---

### `guide` — Create a GHCP Context Guide

The user wants to create a **reference document that GHCP can use as context** when
working on a specific task or domain. Think of it as a lightweight playbook.

1. Read the template at `brain/ai-brain/backlog/_templates/guide.md`
2. Assign the next sequential `GUIDE-NNN` ID
3. Parse `${input:text}` for the guide's purpose and scope
4. Fill in the Context, Rules, and Examples sections from the user's description
5. Set `status: draft`
6. Create the file at `brain/ai-brain/backlog/guides/GUIDE-NNN_kebab-title.md`
7. Add a row to BOARD.md Guides table
8. Tell the user: "Created GUIDE-NNN: title"

---

### `refine` — Refine an Existing Idea

The user wants to add a **refinement pass** to an existing idea.

1. Parse `${input:text}` for the IDEA-NNN ID
2. Read the existing idea file
3. Add a new `### vN — YYYY-MM-DD` subsection under Refinements
4. Write the refined version based on user input — sharper scope, clearer requirements
5. Update `status` to `refining` if it was `raw`
6. Update the `updated` date
7. **Never modify the Raw Idea section**
8. Tell the user: "Refined IDEA-NNN (vN)"

---

### `promote` — Promote an Idea to a Backlog Item

The user wants to turn a **refined idea into an actionable task**.

1. Parse `${input:text}` for the IDEA-NNN ID
2. Read the existing idea file
3. Create a new backlog item in `items/` with `origin: IDEA-NNN`
4. Update the idea: `status: promoted`, `promoted-to: BLI-NNN`
5. Update BOARD.md: move the idea row, add the new item row
6. Tell the user: "Promoted IDEA-NNN → BLI-NNN"

---

### `board` — Show the Board

> **Shortcut:** Use `/todos` instead for a richer board view with status updates.

Display the current status of all tracked work.

1. Read `brain/ai-brain/backlog/BOARD.md`
2. Present the board to the user in a readable format
3. Optionally summarise: X items, Y ideas, Z guides

---

### `epic` — Create or Manage an Epic

The user wants to **group related items** under a theme.

1. Read the template at `brain/ai-brain/backlog/_templates/epic.md`
2. Assign the next sequential `EPIC-NNN` ID
3. Parse `${input:text}` for the epic's vision and scope
4. Create the file at `brain/ai-brain/backlog/epics/EPIC-NNN_kebab-title.md`
5. Add a row to BOARD.md Epics table
6. Tell the user: "Created EPIC-NNN: title"

---

### `update` — Update Status or Priority

> **Shortcut:** Use `/todos` for quick status changes like "done BLI-003".

The user wants to **change the status, priority, or other metadata** of an existing entry.

1. Parse `${input:text}` for the ID and the change (e.g., "BLI-003 done", "IDEA-001 parked")
2. Read the existing file
3. Update the relevant frontmatter fields
4. Update the `updated` date
5. Move the row in BOARD.md to the correct section
6. Tell the user what changed

---

### General Rules

- **When in doubt between item and idea, create an idea.** Ideas can be promoted; items can't be demoted.
- **Capture raw text exactly as the user gives it.** Don't clean up vague ideas.
- **Always update BOARD.md** when creating or modifying entries.
- **IDs are never reused** — even for archived or discarded entries.
- **Today's date** must be obtained from the system clock, not guessed.
- **File names** use kebab-case: `BLI-001_add-search-filters.md`
- **Multiple entries at once** — if the user gives a list, create each as a separate file with sequential IDs, then update BOARD.md once with all entries.

### Quick Examples

| User says | Action | Result |
|---|---|---|
| "brainstorm: how should we handle auth?" | `brainstorm` | IDEA-NNN with brainstorm template |
| "guide: how GHCP should handle error messages" | `guide` | GUIDE-NNN, context guide |
| "refine IDEA-001" | `refine` | Adds v1 refinement pass |
| "promote IDEA-001" | `promote` | Creates BLI-NNN from idea |
| "epic: Atlassian v2 migration" | `epic` | EPIC-NNN grouping |

> **For everyday use, prefer the shortcuts:**
> - `/jot "some thought"` — fastest capture
> - `/todo "fix the bug"` — add a task
> - `/todos` — view board, mark done, locate items
```
