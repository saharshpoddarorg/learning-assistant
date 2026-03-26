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
│   ├── item.md             ← Template for backlog items (features, bugs, tasks)
│   ├── idea.md             ← Template for raw ideas (brainstorming, whiteboard)
│   └── epic.md             ← Template for epics (grouping related items)
├── items/
│   └── (BLI-NNN_title.md)  ← Backlog items
├── ideas/
│   └── (IDEA-NNN_title.md) ← Raw ideas with refinement history
└── epics/
    └── (EPIC-NNN_title.md) ← Epics grouping related items
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

### Update Status

```text
1. Change the status field in the item's frontmatter
2. Set updated date
3. Move the row in BOARD.md to the new status column
```

---

## ID Assignment

IDs are sequential within each type:

- `BLI-001`, `BLI-002`, ... (backlog items)
- `IDEA-001`, `IDEA-002`, ... (ideas)
- `EPIC-001`, `EPIC-002`, ... (epics)

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

---

## Commit Convention

```text
backlog(items): Add BLI-NNN — short title
backlog(ideas): Capture IDEA-NNN — short title
backlog(ideas): Refine IDEA-NNN — v2 refinement
backlog(ideas): Promote IDEA-NNN → BLI-NNN
backlog(board): Update board — 2 items moved to done
```
