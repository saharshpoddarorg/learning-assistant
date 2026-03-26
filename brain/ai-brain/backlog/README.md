# backlog/ — Project Backlog & Idea Tracker

> **Tier 5** of the ai-brain workspace. A lightweight, git-tracked backlog for
> managing work items, capturing raw ideas, and refining them into actionable tasks.

---

## Quick Start — 3 Commands

| Command | What it does | Example |
|---|---|---|
| `/jot` | Capture any thought instantly | `/jot "voice search for vault?"` |
| `/todo` | Add a concrete task | `/todo "fix search bug in vault"` |
| `/todos` | View board, mark done, find items | `/todos` or `/todos "done BLI-003"` |

That's it for daily use. For advanced operations (brainstorm, refine, promote, guide,
epic), use `/backlog`.

---

## What Is This Tier?

The `backlog/` tier is a **personal kanban + ideation system** that lives in
the repo alongside your notes, library, and sessions. It tracks:

- **Ideas** — raw, unrefined thoughts captured exactly as-is, with a refinement trail
- **Backlog items** — concrete tasks, features, bugs, improvements with priority and status
- **Guides** — structured context docs written for GHCP to reference
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
│   └── guide.md            ← Template: GHCP context guides / playbooks
├── items/
│   └── (BLI-NNN_title.md)  ← Backlog items
├── ideas/
│   └── (IDEA-NNN_title.md) ← Raw ideas and brainstorms with refinement history
├── epics/
│   └── (EPIC-NNN_title.md) ← Epics grouping related items
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

Raw, unrefined thoughts with a built-in refinement trail. This is where
**everything vague goes** — quick one-liners, half-formed thoughts, "what if"
questions, reminders. Capture first, refine later.

| Field | Values |
|---|---|
| **Status** | `raw` · `refining` · `refined` · `promoted` · `parked` · `discarded` |

**File naming:** `IDEA-NNN_short-title.md` (e.g., `IDEA-001_voice-search-for-vault.md`)

Ideas preserve the original thought verbatim and track refinement history.
When an idea becomes actionable, it's **promoted** to a backlog item (the idea
file gets `status: promoted` and `promoted-to: BLI-NNN`).

> **Quick capture:** Use `/jot` to create an idea in one step with zero overhead.

### Epics (`epics/`)

Larger themes grouping related backlog items.

| Field | Values |
|---|---|
| **Status** | `draft` · `active` · `done` · `archived` |

**File naming:** `EPIC-NNN_short-title.md` (e.g., `EPIC-001_atlassian-v2-migration.md`)

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

### Jot a Quick Thought

```text
1. Type /jot "your thought here"
2. Done. It's captured as IDEA-NNN with status: raw.
3. Later, refine with /backlog refine IDEA-NNN
```

### Create a Backlog Item

```text
1. Type /todo "describe the task"
2. Done. It's created as BLI-NNN with priority and type.
3. View it with /todos
```

### Work Through Your Backlog

```text
1. Type /todos to see the board
2. "start BLI-003" to begin work
3. "done BLI-003" when finished
4. BOARD.md is updated automatically
```

### Promote an Idea to a Backlog Item

```text
1. /backlog promote IDEA-NNN
2. A new BLI-NNN is created with origin link
3. The idea is marked "promoted"
```

### Jot a Quick Note

```text
Just type /jot — same as capturing a vague idea, but emphasises speed.
No template, no fields — your words go in verbatim.
```

### Create a GHCP Guide

```text
1. /backlog guide "topic"
2. Fill in context, rules, examples, anti-patterns
3. Set status: draft (→ active when ready for GHCP to use)
```

### Update Status

```text
/todos "done BLI-003"    or    /todos "start BLI-005"
```

---

## Slash Commands

### Daily Use (simple, fast)

| Command | What it does | When to use |
|---|---|---|
| `/jot` | Capture a thought instantly | Any vague idea, reminder, "what if" |
| `/todo` | Add a concrete task | Work you can define with a clear outcome |
| `/todos` | View board, update status, find items | Daily standup, mark done, check progress |

### Advanced (full management)

| Command | What it does | When to use |
|---|---|---|
| `/backlog brainstorm` | Whiteboard-style exploration | Need to think through options |
| `/backlog guide` | Create a GHCP context guide | Teach GHCP rules/patterns/examples |
| `/backlog refine` | Add refinement pass to an idea | Sharpen a raw idea |
| `/backlog promote` | Promote idea → backlog item | Idea is now actionable |
| `/backlog epic` | Create an epic grouping | Group related items |
| `/backlog update` | Change status/priority | Any metadata update |

---

## ID Assignment

IDs are sequential within each type:

- `IDEA-001`, `IDEA-002`, ... (ideas and brainstorms — everything in ideas/)
- `BLI-001`, `BLI-002`, ... (backlog items — concrete work)
- `EPIC-001`, `EPIC-002`, ... (epics — grouping themes)
- `GUIDE-001`, `GUIDE-002`, ... (guides — GHCP context)

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
| **backlog/guides/** → **.github/instructions/** | A guide becomes permanent | Graduate to an instruction file |

---

## Commit Convention

```text
backlog(items): Add BLI-NNN — short title
backlog(ideas): Capture IDEA-NNN — short title
backlog(ideas): Refine IDEA-NNN — v2 refinement
backlog(ideas): Promote IDEA-NNN → BLI-NNN
backlog(board): Update board — 2 items moved to done
backlog(guides): Add GUIDE-NNN — short title
backlog(guides): Activate GUIDE-NNN — now in use
```

---

## Entry Complexity Spectrum

Use the lightest-weight format that fits your thought:

```text
Simplest                                                    Most Detailed
   │                                                              │
   ▼                                                              ▼
  /jot               /backlog brainstorm       /todo         /backlog guide
   │                        │                    │                │
 IDEA-NNN              IDEA-NNN              BLI-NNN        GUIDE-NNN
 (1 sentence,          (structured            (priority,    (rules,
  raw capture)          exploration)           AC, type)     examples)
```

| What you have | Use | Creates | Detail level |
|---|---|---|---|
| Quick thought / reminder | `/jot` | IDEA-NNN | 1-2 sentences, raw |
| Need to explore options | `/backlog brainstorm` | IDEA-NNN | Multi-section whiteboard |
| Concrete task with outcome | `/todo` | BLI-NNN | Acceptance criteria + priority |
| GHCP needs to learn a pattern | `/backlog guide` | GUIDE-NNN | Rules + examples + anti-patterns |
| Group of related items | `/backlog epic` | EPIC-NNN | Vision + linked items |

### The Lifecycle

```text
/jot "what if..."   →   /backlog refine IDEA-001   →   /backlog promote IDEA-001   →   /todos "done BLI-001"
     (capture)               (sharpen)                     (make actionable)              (complete)
```
