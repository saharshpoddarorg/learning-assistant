---
applyTo: "brain/ai-brain/backlog/**"
---

# Backlog Management — AI-Assisted Protocol

> **Purpose:** Define how the AI assistant creates, updates, refines, and promotes
> backlog items and ideas. Applies to all files in `brain/ai-brain/backlog/`.

---

## When This Activates

This instruction activates whenever the user:

- Asks to create a todo, task, feature request, or work item
- Shares a vague idea, brainstorm, or "I should eventually..." thought
- Wants to jot a quick note or reminder
- Wants to brainstorm or think through options
- Wants to create a context guide for GHCP to reference
- Asks to update the status or priority of a backlog item
- Requests a board view or status summary
- Mentions promoting an idea to a concrete task
- Uses `/backlog` or related commands

---

## Core Principle

> **Capture first, refine later.** Never reject a vague idea — record it exactly
> as stated, then offer to refine. The raw capture is the most valuable part.

---

## Creating Items

### When the User Describes Work to Do

If the user describes something **concrete and actionable** (a feature, a bug, a task):

1. Create a file in `brain/ai-brain/backlog/items/` using the item template
2. Assign the next sequential `BLI-NNN` ID
3. Set appropriate `status`, `priority`, and `type`
4. Write concise description and acceptance criteria
5. Add a row to [BOARD.md](../../brain/ai-brain/backlog/BOARD.md) in the correct section
6. Inform the user: "Created BLI-NNN: title"

### When the User Shares a Vague Idea

If the user describes something **vague, half-formed, or exploratory**:

1. Create a file in `brain/ai-brain/backlog/ideas/` using the idea template
2. Assign the next sequential `IDEA-NNN` ID
3. Capture the raw idea **exactly as stated** — do not clean up, rephrase, or edit
4. Set `status: raw`
5. Add a row to [BOARD.md](../../brain/ai-brain/backlog/BOARD.md) Ideas table
6. Optionally offer to do a first refinement pass (v1)
7. Inform the user: "Captured IDEA-NNN: title"

### Deciding: Item vs Idea

| Signal | Route to |
|---|---|
| "I want to add X feature" | **item** (status: todo) |
| "Fix the bug where..." | **item** (type: bug) |
| "I should probably..." | **idea** (status: raw) |
| "What if we had..." | **idea** (status: raw) |
| "Here's a rough thought..." | **idea** (status: raw) |
| "Research whether..." | **item** (type: research) |
| "Eventually I'd like to..." | **idea** (status: raw) |
| "Remember to check..." | **note** (quick capture) |
| "How should we handle X?" | **brainstorm** (idea with brainstorm template) |
| "GHCP should follow these rules for..." | **guide** (context for GHCP) |
| Clear acceptance criteria given | **item** |
| No clear scope or outcome | **idea** |
| Just a quick reminder | **note** |
| Needs exploration of options | **brainstorm** |
| Written FOR the AI to reference | **guide** |

**When in doubt, create an idea.** Ideas can be promoted; items can't be demoted.
**When it's just a sentence, create a note.** Notes can become anything later.

---

## Updating Items

### Status Changes

When the user says they're starting, finishing, or blocking an item:

1. Update the `status` field in the item's frontmatter
2. Update the `updated` date
3. Move the row in BOARD.md to the correct section
4. Inform the user of the change

### Priority Changes

When the user reprioritises work:

1. Update the `priority` field in frontmatter
2. Update the `updated` date
3. Reposition the row in BOARD.md (Critical/High vs Medium/Low)

---

## Refining Ideas

When the user asks to refine an idea (or offers additional clarity):

1. Add a new `### vN — YYYY-MM-DD` subsection under Refinements
2. Write the refined version — sharper scope, clearer requirements
3. Update the `status` to `refining` (if first refinement) or keep as `refining`
4. Update the `updated` date
5. **Never modify the Raw Idea section** — the original capture is immutable

### Refinement Passes

Each refinement subsection should progressively clarify:

- **v1:** What does the idea actually mean? First interpretation.
- **v2:** Sharper scope, identified unknowns, rough effort estimate.
- **v3+:** Near-actionable: clear scope, acceptance criteria drafted.

When a refinement reaches the point of clear acceptance criteria, suggest promotion.

---

## Promoting Ideas to Items

When an idea is refined enough to be actionable:

1. Create a new backlog item in `items/` using the item template
2. Set `origin: IDEA-NNN` in the item's frontmatter
3. Update the idea file:
   - Set `status: promoted`
   - Set `promoted-to: BLI-NNN`
4. Update BOARD.md: move the idea to the correct section, add the new item
5. Inform the user: "Promoted IDEA-NNN → BLI-NNN"

---

## Creating Notes

When the user wants to jot something quick — a reminder, observation, or thought:

1. Read the template at `brain/ai-brain/backlog/_templates/note.md`
2. Assign the next sequential `NOTE-NNN` ID
3. Write the text as plain English — keep it simple, no structure required
4. Create the file at `brain/ai-brain/backlog/notes/NOTE-NNN_kebab-title.md`
5. Add a row to BOARD.md Notes table
6. Inform the user: "Noted NOTE-NNN: title"

Notes are the simplest entries. They have no status or priority. If a note
becomes important, promote it to an idea or item.

---

## Creating Brainstorms

When the user wants to think through a problem with open-ended exploration:

1. Read the template at `brain/ai-brain/backlog/_templates/brainstorm.md`
2. Assign the next sequential `IDEA-NNN` ID (brainstorms are a type of idea)
3. Frame the topic as a question in "The Question" section
4. Populate "Possibilities" with 3-5 initial options
5. Add known "Constraints" if apparent from context
6. Leave "Wild Ideas" and "Emerging Direction" for user input (or offer to populate)
7. Add "Steps / Next Actions" with suggested follow-ups
8. Create the file at `brain/ai-brain/backlog/ideas/IDEA-NNN_kebab-title.md`
9. Add a row to BOARD.md Ideas table with `type: brainstorm`
10. Inform the user: "Created brainstorm IDEA-NNN: title"

---

## Creating Guides

When the user wants to create a **reference document for GHCP** — rules,
patterns, examples, or anti-patterns that teach the AI how to handle
specific situations:

1. Read the template at `brain/ai-brain/backlog/_templates/guide.md`
2. Assign the next sequential `GUIDE-NNN` ID
3. Write a clear Purpose and "Use this when" trigger in the header
4. Fill in Context, Rules, Examples, and Anti-Patterns from the user's input
5. Set `status: draft`
6. Create the file at `brain/ai-brain/backlog/guides/GUIDE-NNN_kebab-title.md`
7. Add a row to BOARD.md Guides table
8. Inform the user: "Created GUIDE-NNN: title"

Guides go through a lifecycle: `draft` → `active` → `archived` | `superseded`.
When a guide is marked `active`, GHCP should reference it when the trigger
condition is met. When a guide graduates to a permanent rule, move it to
`.github/instructions/` as a proper instruction file.

---

## BOARD.md Maintenance

The [BOARD.md](../../brain/ai-brain/backlog/BOARD.md) file is the kanban view.
It must be updated whenever:

- A new item or idea is created (add row)
- Status changes (move row between sections)
- Priority changes (move between Critical/High and Medium/Low)
- An item is completed (move to Done with completion date)

### Board Sections

```text
Ideas           ← all ideas and brainstorms (raw, refining, refined)
Notes           ← quick plain-text captures
Backlog         ← todo items split by priority tier
In Progress     ← items currently being worked on
Done            ← completed items
Guides          ← GHCP context guides (draft, active)
Epics           ← epic overview with item counts
```

---

## ID Assignment

To determine the next ID:

1. Check BOARD.md for the highest existing ID of that type
2. If empty, start at 001
3. IDs are never reused — even if an item is archived or discarded

---

## File Naming

| Type | Pattern | Example |
|---|---|---|
| Item | `BLI-NNN_kebab-title.md` | `BLI-001_add-search-filters.md` |
| Idea | `IDEA-NNN_kebab-title.md` | `IDEA-001_voice-search-for-vault.md` |
| Epic | `EPIC-NNN_kebab-title.md` | `EPIC-001_atlassian-v2-migration.md` |
| Note | `NOTE-NNN_kebab-title.md` | `NOTE-001_check-api-limits.md` |
| Guide | `GUIDE-NNN_kebab-title.md` | `GUIDE-001_error-message-conventions.md` |

---

## Batch Operations

When the user provides multiple items or ideas at once:

1. Create separate files for each
2. Assign sequential IDs
3. Update BOARD.md once with all new entries
4. Provide a summary: "Created N items / ideas: ..."

---

## User Commands

| Command | Effect |
|---|---|
| "add a todo for X" | Create a backlog item |
| "jot down idea: X" | Create a raw idea |
| "what's on the board?" | Show BOARD.md summary |
| "refine IDEA-NNN" | Add a refinement pass |
| "promote IDEA-NNN" | Promote idea to backlog item |
| "start BLI-NNN" | Set status to in-progress |
| "done BLI-NNN" | Set status to done |
| "prioritise BLI-NNN high" | Change priority |
| `/backlog add "fix search bug"` | Create a backlog item via slash command |
| `/backlog idea "voice search"` | Capture a raw idea via slash command |
| `/backlog brainstorm "auth approach"` | Open a brainstorm via slash command |
| `/backlog note "check API limits"` | Quick note via slash command |
| `/backlog guide "error conventions"` | Create a GHCP guide via slash command |
| `/backlog board` | Show the board |
| `/backlog update "BLI-003 done"` | Update status via slash command |
