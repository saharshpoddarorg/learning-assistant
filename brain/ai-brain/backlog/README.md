# backlog/ — Project Backlog & Idea Tracker

> **Tier 5** of the ai-brain workspace. A lightweight, git-tracked backlog for
> managing work items, capturing raw ideas, and refining them into actionable tasks.

---

## What Is This Tier?

The `backlog/` tier is a **personal kanban + ideation system** that lives in
the repo alongside your notes, library, and sessions. It tracks:

- **Backlog items** — concrete tasks, features, bugs, improvements with priority and status
- **Ideas** — raw, unrefined thoughts captured as-is, with a refinement trail
- **Epics** — larger themes that group related items

Unlike external issue trackers, everything here is version-controlled and
co-located with the code. You see the full history of every idea and decision.

---

## Routing — How Backlog Differs from Other Tiers

| Tier | Question | Content |
|---|---|---|
| **inbox/** | Not ready yet? | Raw capture, unprocessed |
| **notes/** | Did you write it? | Your insights, decisions |
| **library/** | Did you import it? | External references |
| **sessions/** | Valuable AI conversation? | Captured research, analysis |
| **backlog/** | Is it work to track? | Todos, features, ideas, brainstorming |

### When does something go in backlog/?

> **Is it something you want to DO (or might want to do)?** → backlog/
>
> Is it something you KNOW (or learned)? → notes/ or sessions/

---

## Structure

```text
backlog/
├── README.md               ← This file
├── BOARD.md                ← Kanban board — at-a-glance status view
├── _templates/
│   ├── item.md             ← Template: backlog items (features, bugs, tasks)
│   ├── idea.md             ← Template: raw ideas with refinement trail
│   ├── brainstorm.md       ← Template: whiteboard-style free-form thinking
│   ├── epic.md             ← Template: epics (grouping related items)
│   ├── note.md             ← Template: ultra-lightweight quick captures
│   └── guide.md            ← Template: GHCP context guides / playbooks
├── items/
│   └── (BLI-NNN_title.md)  ← Backlog items
├── ideas/
│   └── (IDEA-NNN_title.md) ← Raw ideas and brainstorms with refinement history
├── epics/
│   └── (EPIC-NNN_title.md) ← Epics grouping related items
├── notes/
│   └── (NOTE-NNN_title.md) ← Quick plain-text captures
└── guides/
    └── (GUIDE-NNN_title.md) ← Context guides for GHCP
```

---

## Item Types

### Backlog Items (`items/`)

Concrete, actionable work with defined acceptance criteria.

| Field | Values |
|---|---|
| **Status** | `todo` · `in-progress` · `blocked` · `done` · `archived` |
| **Priority** | `critical` · `high` · `medium` · `low` |
| **Type** | `feature` · `bug` · `improvement` · `research` · `spike` · `chore` |

**File naming:** `BLI-NNN_short-title.md` (e.g., `BLI-001_add-search-filters.md`)

### Ideas (`ideas/`)

Raw, unrefined thoughts with a built-in refinement trail.

| Field | Values |
|---|---|
| **Status** | `raw` · `refining` · `refined` · `promoted` · `parked` · `discarded` |

**File naming:** `IDEA-NNN_short-title.md` (e.g., `IDEA-001_voice-search-for-vault.md`)

Ideas preserve the original thought verbatim and track refinement history.
When an idea becomes actionable, it's **promoted** to a backlog item (the idea
file gets `status: promoted` and `promoted-to: BLI-NNN`).

### Epics (`epics/`)

Larger themes grouping related backlog items.

| Field | Values |
|---|---|
| **Status** | `draft` · `active` · `done` · `archived` |

**File naming:** `EPIC-NNN_short-title.md` (e.g., `EPIC-001_atlassian-v2-migration.md`)

### Notes (`notes/`)

Ultra-lightweight quick captures. Plain English, minimal structure.
No priority, no status — just text. If a note grows important, promote it
to an item or idea.

**File naming:** `NOTE-NNN_short-title.md` (e.g., `NOTE-001_check-api-rate-limits.md`)

### Brainstorms (in `ideas/`)

Whiteboard-style free-form thinking sessions. Brainstorms use the brainstorm
template but live in `ideas/` with an `IDEA-NNN` ID and `type: brainstorm`.
They include sections for Possibilities, Constraints, Wild Ideas, and
Emerging Direction.

### Guides (`guides/`)

Structured context documents written **for GHCP** to use as reference material.
Think of them as lightweight playbooks — rules, examples, and anti-patterns
that teach GHCP how to handle specific situations in your project.

| Field | Values |
|---|---|
| **Status** | `draft` · `active` · `archived` · `superseded` |

**File naming:** `GUIDE-NNN_short-title.md` (e.g., `GUIDE-001_error-message-conventions.md`)

---

## Workflows

### Capture a Vague Idea

```text
1. Create a file in ideas/ using the idea template
2. Write the raw thought in "Raw Idea" section — unedited, as-is
3. Status: raw
4. Later, add refinement passes as new subsections (v1, v2, ...)
5. When actionable, promote to a backlog item
```

### Create a Backlog Item

```text
1. Create a file in items/ using the item template
2. Set status, priority, and type
3. Write acceptance criteria
4. Update BOARD.md
```

### Promote an Idea to a Backlog Item

```text
1. Create the backlog item in items/ (set origin: IDEA-NNN)
2. Update the idea: status → promoted, promoted-to → BLI-NNN
3. Update BOARD.md
```

### Jot a Quick Note

```text
1. Create a file in notes/ using the note template
2. Write plain text — no structure required
3. Done. If it becomes important later, promote to an item or idea.
```

### Create a GHCP Guide

```text
1. Create a file in guides/ using the guide template
2. Write the context, rules, examples, and anti-patterns
3. Set status: draft (→ active when ready for GHCP to use)
4. Update BOARD.md
```

### Update Status

```text
1. Change the status field in the item's frontmatter
2. Set updated date
3. Move the row in BOARD.md to the new status column
```

---

## Slash Command

Use `/backlog` in Copilot Chat to manage the backlog without remembering file
paths or templates. It supports all actions:

| Action | What it does |
|---|---|
| `add` | Create a backlog item (feature, bug, task) |
| `idea` | Capture a raw idea exactly as-is |
| `brainstorm` | Open a whiteboard-style thinking session |
| `note` | Jot a quick plain-text note |
| `guide` | Create a GHCP context guide |
| `refine` | Add a refinement pass to an idea |
| `promote` | Promote a refined idea to a backlog item |
| `board` | Show the current board status |
| `update` | Change status or priority of any entry |

---

## ID Assignment

IDs are sequential within each type:

- `BLI-001`, `BLI-002`, ... (backlog items)
- `IDEA-001`, `IDEA-002`, ... (ideas)
- `EPIC-001`, `EPIC-002`, ... (epics)
- `NOTE-001`, `NOTE-002`, ... (notes)
- `GUIDE-001`, `GUIDE-002`, ... (guides)

To find the next ID, check BOARD.md or count existing files.

---

## Integration with Other Tiers

| From | To | When |
|---|---|---|
| **sessions/** → **backlog/** | A session identifies work to do | Create a backlog item, link to session |
| **backlog/** → **sessions/** | An item needs research | Capture the session, link from the item |
| **ideas/** → **items/** | An idea becomes actionable | Promote (status: promoted, create BLI) |
| **notes/** → **backlog/** | A note identifies a gap | Create an item referencing the note |
| **inbox/** → **ideas/** | A raw capture is an idea | Move to ideas, fill in the template |
| **backlog/notes/** → **items/** | A note becomes actionable | Promote to a backlog item |
| **backlog/guides/** → **.github/instructions/** | A guide becomes permanent | Graduate to an instruction file |

---

## Commit Convention

```text
backlog(items): Add BLI-NNN — short title
backlog(ideas): Capture IDEA-NNN — short title
backlog(ideas): Refine IDEA-NNN — v2 refinement
backlog(ideas): Promote IDEA-NNN → BLI-NNN
backlog(board): Update board — 2 items moved to done
backlog(notes): Add NOTE-NNN — short title
backlog(guides): Add GUIDE-NNN — short title
backlog(guides): Activate GUIDE-NNN — now in use
```

---

## Entry Complexity Spectrum

Entries range from dead-simple to richly structured. Use the lightest-weight
format that fits:

```text
Simplest                                                  Most Detailed
   │                                                           │
   ▼                                                           ▼
  NOTE         IDEA         BRAINSTORM       ITEM        GUIDE / EPIC
  (plain       (raw +       (structured      (priority,  (context,
   text)       refine)      exploration)     AC, type)   rules, examples)
```

| Format | When to use | Detail level |
|---|---|---|
| Note | Quick thought, reminder, observation | 1-2 sentences |
| Idea | Something you might want to do someday | A paragraph + refinement |
| Brainstorm | Need to think through options | Multi-section exploration |
| Item | Concrete task with defined outcome | Full AC + priority |
| Guide | GHCP needs to learn a pattern | Rules + examples + anti-patterns |
| Epic | Group of related items | Vision + linked items |
