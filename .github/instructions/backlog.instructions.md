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
- Wants to brainstorm or think through options
- Wants to create a context guide for GHCP to reference
- Asks to update the status or priority of a backlog item
- Requests a board view or status summary
- Mentions promoting an idea to a concrete task
- Uses `/jot`, `/todo`, `/todos`, `/backlog`, or related commands

---

## Core Principle

> **Capture first, refine later.** Never reject a vague idea — record it exactly
> as stated, then offer to refine. The raw capture is the most valuable part.

---

## Creating Items

### When the User Describes Work to Do

If the user describes something **concrete and actionable** (a feature, a bug, a task):

1. Route to the correct folder (see **Folder Routing** below)
2. Assign the next sequential `BLI-NNN` ID
3. Set appropriate `status`, `priority`, and `type`
4. Write concise description and acceptance criteria
5. Add a row to [BOARD.md](../../brain/ai-brain/backlog/BOARD.md) in the correct section
6. Inform the user: "Created BLI-NNN: title"

### Folder Routing for Items

All actionable items use the **BLI** prefix. The folder determines the context:

| Folder | Route when | Examples |
|---|---|---|
| `features/` | Enhancement or feature for the **learning-assistant** project | MCP tools, vault providers, brain features, skill files |
| `projects/` | **Standalone personal software project** (separate app/product) | Media manager, family tree, ebook reader |
| `items/` | General work that doesn't fit features/ or projects/ | One-off tasks, misc fixes, cross-cutting chores |

**Decision heuristic:**

- Is it a feature *of this repo*? → `features/`
- Is it a *separate application* the user wants to build? → `projects/`
- Neither? → `items/`

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
| "Remember to check..." | **idea** (status: raw, quick capture via `/jot`) |
| "How should we handle X?" | **brainstorm** (idea with brainstorm template) |
| "GHCP should follow these rules for..." | **guide** (context for GHCP) |
| Clear acceptance criteria given | **item** |
| No clear scope or outcome | **idea** |
| Needs exploration of options | **brainstorm** |
| Written FOR the AI to reference | **guide** |

**When in doubt, create an idea.** Ideas can be promoted; items can't be demoted.

> **Slash command shortcuts:**
> - `/jot` — fastest idea capture (zero overhead)
> - `/todo` — fastest item creation
> - `/todos` — view board, mark done, locate items
> - `/backlog` — advanced operations (brainstorm, refine, promote, guide, epic)

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

| Type | Pattern | Folder | Example |
|---|---|---|---|
| Feature | `BLI-NNN_kebab-title.md` | `features/` | `BLI-007_cross-platform-note-integration.md` |
| Project | `BLI-NNN_kebab-title.md` | `projects/` | `BLI-014_media-manager.md` |
| Item | `BLI-NNN_kebab-title.md` | `items/` | `BLI-006_code-formatting-instructions-skill.md` |
| Idea | `IDEA-NNN_kebab-title.md` | `ideas/` | `IDEA-001_voice-search-for-vault.md` |
| Epic | `EPIC-NNN_kebab-title.md` | `epics/` | `EPIC-001_atlassian-v2-migration.md` |
| Guide | `GUIDE-NNN_kebab-title.md` | `guides/` | `GUIDE-001_error-message-conventions.md` |

### ID Prefix Reference

| Prefix | Stands for | Scope |
|---|---|---|
| **BLI** | **B**ack**L**og **I**tem | Any actionable work — features, projects, or general items |
| **IDEA** | Idea | Raw/exploratory thoughts in `ideas/` |
| **EPIC** | Epic | Grouping themes in `epics/` |
| **GUIDE** | Guide | GHCP context guides in `guides/` |

**BLI** stands for **B**ack**L**og **I**tem. It is a universal prefix used for all
actionable work items regardless of which folder they reside in (`features/`,
`projects/`, or `items/`). The folder provides organizational context (what kind of
work), while the BLI ID provides stable, unique, never-reused identity across the
entire backlog.

---

## Sub-Items (Parent–Child Hierarchy)

Backlog items can be decomposed into **sub-items** for large or multi-faceted work.
Sub-items are regular BLI files that reference their parent via frontmatter.

### When to Use Sub-Items

| Situation | Approach |
|---|---|
| Quick checklist of steps | Use acceptance criteria checkboxes (no sub-items) |
| 3+ distinct workstreams within one item | Create sub-items (separate BLI files) |
| Work is large enough that sub-parts need their own status tracking | Create sub-items |
| Mixed priorities within one item | Create sub-items with individual priorities |

### Creating Sub-Items

1. Create the parent item first (or update an existing item to be a parent)
2. Add `sub-items: [BLI-NNN, BLI-NNN]` to the parent's frontmatter
3. Add a **Sub-Items** table section in the parent item's body
4. Create each sub-item file with `parent: BLI-NNN` in its frontmatter
5. Sub-items get their own sequential BLI IDs — they are full items
6. Sub-items appear in BOARD.md indented under their parent (or grouped in the epic)

### Frontmatter Fields

**Parent item:**

```yaml
sub-items: [BLI-010, BLI-011, BLI-012]
```

**Child item:**

```yaml
parent: BLI-007
```

### Rules

- A sub-item inherits the parent's `epic` if it has none of its own
- Sub-items can have different priorities and statuses from their parent
- When all sub-items are `done`, suggest marking the parent as `done`
- Sub-items can themselves have sub-items (max 2 levels deep to avoid complexity)
- The parent's Sub-Items table must stay in sync with the sub-item files

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
| `/jot "quick thought"` | Capture via slash command (fastest path) |
| `/todo "fix search bug"` | Create item via slash command |
| `/todos` | View the board |
| `/todos "done BLI-003"` | Update status via slash command |
| `/backlog brainstorm "auth approach"` | Open a brainstorm via slash command |
| `/backlog guide "error conventions"` | Create a GHCP guide via slash command |
| `/backlog refine IDEA-001` | Refine an idea via slash command |
| `/backlog promote IDEA-001` | Promote idea to item via slash command |
