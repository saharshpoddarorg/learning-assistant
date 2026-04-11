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

## Unified Capture — The `/jot` and `/read-file-jot` Protocol

`/jot` is the **single entry point** for all backlog capture. It replaces the old
separate idea and task commands. `/todo` is now an alias that routes through `/jot`.
`/read-file-jot` is a dedicated command for reading external files and extracting
backlog items from their contents.

For the full protocol (9 phases: Read File (imports only) → Parse & Detect → Classify →
Dedup & Merge Check → Enhance → Refine & Gap Analysis → Cross-Reference → Confirm →
Board Sync → Completeness Check), see the **capture workflow** at
`brain/ai-brain/backlog/guides/capture-workflow.md` (the definitive reference with all
Mermaid diagrams), and the **jot-down guide** at
`brain/ai-brain/backlog/guides/jot-down-guide.md` (classification rules and enhancement
patterns).

### Key Protocol Points

1. **Classify, don't ask** — infer idea vs task from content signals
2. **Read attached files automatically** — local file paths are read and extracted
3. **Dedup before creating** — scan existing backlog for duplicates and enhancement
   opportunities before creating any new item (see Dedup & Merge section below)
4. **Enhance everything** — tags, priority, effort, epic, AC are always inferred
5. **Refine automatically** — gap analysis, future considerations, implied dependencies
6. **Auto-breakdown** — L/XL tasks automatically decompose into sub-items
7. **Cross-reference aggressively** — link to related BLIs, IDEAs, EPICs, notes, sessions
8. **Confirm with user** — present summary, offer refinement, then finalize
9. **Board sync is mandatory** — BOARD.md + views/ + CHANGELOG.md on every creation
10. **Completeness check is mandatory** — never exit without verifying all items, boards,
    cross-refs, timestamps, and IDs are correct

### File-to-Backlog Extraction (`/read-file-jot`)

When the user provides a file path via `/read-file-jot`:

1. **Read the file** immediately — don't ask "should I read it?"
2. **Check IMPORT-LOG.md** — if this file was imported before, warn and offer options
3. **Assign import batch** — `IMP-NNN` (next sequential from IMPORT-LOG.md)
4. **Parse for items** — bullets, numbered lists, TODOs, headings, paragraphs
5. **Classify each item** independently using the standard rules
6. **Dedup check** — scan existing backlog for duplicates before enhancing
7. **Enhance each item** with full protocol (title, type, priority, tags, effort, AC)
8. **Set origin tracking** — `origin-type: file-import`, `import-batch: IMP-NNN`
9. **Refine** — gap analysis, future considerations, grouping analysis
10. **Present summary** — show new items, merged items, skipped duplicates
11. **Create and sync** — create files, update all boards + IMPORT-LOG.md
12. **Completeness check** — verify all items, boards, import log, and dedup results

### Dedup & Merge Protocol

Before creating ANY new item (via `/jot` or `/read-file-jot`), check for duplicates:

1. **Scan existing items** in `features/`, `items/`, `projects/`, and `ideas/`
2. **Compare** title keywords (≥ 70% overlap), tags (3+ shared), descriptions
3. **Classify the match:**
   - Exact duplicate → skip, note in summary
   - Partial overlap → create + cross-reference
   - Enhancement opportunity → merge new details into existing item
4. **For merges:** add AC, union tags, enrich description, log in Activity Log
5. **Never lose information** — everything the user provided is preserved
6. **Present results** with clear new/merged/skipped categories

See `capture-workflow.md` Phase 3 for the full dedup flowchart and decision matrix.

### File Import Tracking

File imports are tracked separately from manual captures:

- **`origin-type`** field in templates: `manual` (from `/jot`) or `file-import`
  (from `/read-file-jot`)
- **`import-batch`** field: `IMP-NNN` — groups all items from one file read
- **IMPORT-LOG.md** — append-only log of all file imports with batch IDs, counts
- **views/by-source.md** — view items grouped by manual vs file-import origin
- **Re-import detection** — IMPORT-LOG.md is checked before reading a file to
  detect if it was previously imported

### Attachments & References

Items can reference external files, URLs, and other backlog/brain artifacts:

- **Local file paths** (e.g., `C:\notes\spec.txt`) — read and extract, store as reference
- **URLs** (e.g., `https://...`) — record and use for context enrichment
- **Backlog cross-refs** (e.g., `BLI-003`, `IDEA-001`) — bidirectional linking
- **Brain notes/sessions** — link when topically related

All references go in the item's `## Attachments & References` section (4-column table:
Type, Path / URL, Added, Notes). Cross-references are **bidirectional** — when A links
to B, B links back to A.

### Completeness Check Protocol

Before finishing ANY `/jot` or `/read-file-jot` invocation, verify:

- Every item from the input was classified and created (nothing missed)
- Every task has all required fields (title, type, priority, tags, effort, AC)
- Every idea has Raw Idea with user's verbatim text
- `origin-type` set correctly (`manual` or `file-import`)
- `import-batch` set for file-import items
- Existing backlog scanned for duplicates before creating
- Duplicates handled (skipped, merged, or cross-referenced)
- Merges logged in Activity Log of existing items
- All attachments recorded in Attachments & References tables
- All cross-references are bidirectional
- All boards updated (BOARD.md, views/, CHANGELOG.md, epic files)
- IMPORT-LOG.md updated (file-import only)
- views/by-source.md updated (both manual and file-import)
- All timestamps from system clock, all IDs sequential
- Summary presented to user (new / merged / skipped counts)

---

## Creating Items

### When the User Describes Work to Do

If the user describes something **concrete and actionable** (a feature, a bug, a task):

1. Route to the correct folder (see **Folder Routing** below)
2. Assign the next sequential `BLI-NNN` ID
3. Set appropriate `status`, `priority`, and `type`
4. **Enhance the user's input** — always infer and populate:
   - **Rich description** (3-5 sentences): what, why, and key considerations
   - **3-5 concrete acceptance criteria** (checkbox format, testable, specific)
   - **3-7 tags** (lowercase kebab-case, matching technologies and domains)
   - **Effort estimate** (XS/S/M/L/XL — always set, infer from complexity)
   - **Epic assignment** — check existing epics for a match, set if clear
5. **Auto-breakdown if large** — if estimated L/XL or 3+ distinct workstreams:
   - Create parent BLI with `sub-items: [BLI-NNN, ...]` in frontmatter
   - Create each sub-item as a separate BLI file with `parent: BLI-NNN`
   - Each sub-item gets its own title, AC, type, priority, effort, and tags
   - Add a **Sub-Items** table in the parent item body
6. **Update ALL boards and logs** (mandatory — in this order):
   a. Add row(s) to [BOARD.md](../../brain/ai-brain/backlog/BOARD.md)
   b. Add to [views/by-status.md](../../brain/ai-brain/backlog/views/by-status.md) (Todo)
   c. Add to [views/by-priority.md](../../brain/ai-brain/backlog/views/by-priority.md)
   d. Add to [views/by-project.md](../../brain/ai-brain/backlog/views/by-project.md) (if epic-linked)
   e. Add to [views/by-source.md](../../brain/ai-brain/backlog/views/by-source.md) (Manual Capture section)
   f. Append creation entry to [CHANGELOG.md](../../brain/ai-brain/backlog/CHANGELOG.md),
      update monthly and cumulative stats
   f. If epic assigned, update the epic file's Items table
7. Inform the user with a rich summary:
   "Created BLI-NNN: title (type, priority, effort, N AC, M sub-items, tags)"

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
4. **Infer 2-5 tags** from the content (lowercase, kebab-case)
5. Set `status: raw`
6. **Update ALL boards and logs:**
   a. Add a row to [BOARD.md](../../brain/ai-brain/backlog/BOARD.md) Ideas table
   b. Append creation entry to [CHANGELOG.md](../../brain/ai-brain/backlog/CHANGELOG.md),
      update stats
7. Optionally offer to do a first refinement pass (v1)
8. Inform the user: "Captured IDEA-NNN: title"

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

When the user says they're starting, finishing, blocking, or reviewing an item:

1. Get current date and time from the system clock
2. Update the `status` field in the item's frontmatter
3. Update the relevant date field (`started`, `completed`, `blocked-since`, `review-since`)
4. Update the `updated` date
5. Append an entry to the item's Activity Log section
6. Update the item's Time Tracking section (if marking done — calculate cycle time)
7. **Update ALL boards and logs** (mandatory):
   a. Move the row in BOARD.md to the correct section
   b. Move item in views/by-status.md between status groups
   c. Update status indicator in views/by-priority.md
   d. Update status in views/by-project.md (if project-linked)
   e. Append entry to CHANGELOG.md, update monthly and cumulative stats
   f. If item has an epic: update the epic file's Progress section
      (recalculate completed count and percentage)
8. If marking parent done: check all sub-items are done — warn if not
9. Inform the user of the change with timestamp and cycle time (if done)

### Status Values

| Status | Meaning | Date field set |
|---|---|---|
| `todo` | Ready to be picked up | `created` |
| `in-progress` | Actively being worked on | `started` |
| `blocked` | Cannot proceed — blocker exists | `blocked-since` |
| `in-review` | Done but awaiting verification | `review-since` |
| `done` | Completed — all acceptance criteria met | `completed` |
| `archived` | Removed from active tracking | `updated` |

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

## BOARD.md & Views Maintenance — Full Board Sync Protocol

> **Golden Rule:** No creation, status change, or priority change happens without
> updating the complete chain: **BOARD.md → views/ → CHANGELOG.md**.

The [BOARD.md](../../brain/ai-brain/backlog/BOARD.md) file is the main kanban board.
It must be updated whenever:

- A new item or idea is created (add row)
- Status changes (move row between sections)
- Priority changes (move between Critical/High and Medium/Low)
- An item is completed (move to Done with completion date)
- A sprint is started or ended

### Board Sections

```text
Dashboard       ← ASCII visual summary with counts and progress bars
Active Sprint   ← current sprint goal, committed items, progress
Kanban Board    ← ASCII kanban columns (Backlog | In Progress | Blocked | Review | Done)
Epics           ← epic overview with progress percentages
Standalone      ← items not in an epic
In Progress     ← items currently being worked on (with elapsed time)
Blocked         ← blocked items with blocker description
In Review       ← items awaiting review/verification
Done (Recent)   ← last 10 completed items with cycle time
Ideas           ← all ideas and brainstorms (raw, refining, refined)
Guides          ← GHCP context guides (draft, active)
Recent Activity ← last 5 CHANGELOG entries
Quick Nav       ← links to views/ and CHANGELOG
```

### Views (Derived Boards)

Three filtered views live in `views/` — they are derived from BOARD.md and must
be updated in sync:

| View | File | Shows |
|---|---|---|
| By Status | `views/by-status.md` | All items grouped by status (todo/progress/blocked/review/done/archived) |
| By Project | `views/by-project.md` | Per-project mini-boards with kanban swimlanes |
| By Priority | `views/by-priority.md` | All items ranked by priority tier |

**Update protocol:** When any status, priority, or item change happens, update
**ALL** of the following (no exceptions):

1. **BOARD.md** (main board — move rows, update dashboard counts)
2. **views/by-status.md** (move item between status groups)
3. **views/by-priority.md** (update status indicator, or reposition on priority change)
4. **views/by-project.md** (update if item is epic-linked or project-scoped)
5. **CHANGELOG.md** (append activity entry, update monthly + cumulative stats)
6. **Epic file** (if item is epic-linked — update Progress section counts and %)

### CHANGELOG.md (Activity Log)

The [CHANGELOG.md](../../brain/ai-brain/backlog/CHANGELOG.md) is an append-only
audit trail. **Every** status change, priority change, item creation, sprint event,
or structural change gets a row:

```markdown
| Date | Time | ID | Action | From | To | Details |
|---|---|---|---|---|---|---|
| 2026-03-27 | 10:30 AM | BLI-006 | status | todo | done | Completed code formatting skill |
```

**Rules:**

- Always query the system clock for the real timestamp
- Newest entries at the top of each month section
- Group by month with `## YYYY-MM` headings
- Update the monthly and cumulative stats at the bottom

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
| Sprint | `SPRINT-NNN_kebab-title.md` | `sprints/` | `SPRINT-001_mcp-server-buildout.md` |
| Guide | `GUIDE-NNN_kebab-title.md` | `guides/` | `GUIDE-001_error-message-conventions.md` |

### ID Prefix Reference

| Prefix | Stands for | Scope |
|---|---|---|
| **BLI** | **B**ack**L**og **I**tem | Any actionable work — features, projects, or general items |
| **IDEA** | Idea | Raw/exploratory thoughts in `ideas/` |
| **EPIC** | Epic | Grouping themes in `epics/` |
| **SPRINT** | Sprint | Time-boxed iterations in `sprints/` |
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

## Time Tracking Protocol

Every backlog item now tracks timestamps for its lifecycle. When a status
change occurs, update the relevant date fields in the item's frontmatter
**and** append an entry to the item's Activity Log section.

### Item Frontmatter Fields (Time Tracking)

```yaml
created: YYYY-MM-DD          # when the item was created
updated: YYYY-MM-DD          # last modification date
started: null                 # when work began (status → in-progress)
completed: null               # when item was marked done
blocked-since: null           # when item was blocked (cleared on unblock)
review-since: null            # when item entered review
sprint: null                  # sprint assignment (SPRINT-NNN or null)
estimated-effort: null        # T-shirt size: XS, S, M, L, XL
actual-effort: null           # actual time: "2h", "1d", "3d"
```

### Status Transitions (State Machine)

```text
                    ┌──────────┐
  ┌────────────────→│ ARCHIVED │
  │                 └──────────┘
  │                      ▲
  │    ┌──────┐    ┌─────┴──────┐    ┌───────────┐    ┌──────┐
  │    │ TODO │ →  │IN PROGRESS │ →  │ IN REVIEW │ →  │ DONE │
  │    └──────┘    └────────────┘    └───────────┘    └──────┘
  │                     │ ▲
  │                     ▼ │
  │               ┌───────────┐
  └───────────────│  BLOCKED  │
                  └───────────┘
```

| Transition | Set field | Action |
|---|---|---|
| → `in-progress` | `started: YYYY-MM-DD` | Begin work |
| → `blocked` | `blocked-since: YYYY-MM-DD` | Record blocker in notes |
| `blocked` → `in-progress` | clear `blocked-since` | Blocker resolved |
| → `in-review` | `review-since: YYYY-MM-DD` | Ready for verification |
| → `done` | `completed: YYYY-MM-DD` | Calculate cycle time |
| → `archived` | `updated: YYYY-MM-DD` | Removed from active tracking |

### Cycle Time Calculation

When marking an item `done`:

- **Cycle time** = `completed` date – `started` date
- Record in the item's Time Tracking section and in the Done table on BOARD.md
- If the item was never explicitly started, cycle time = `completed` – `created`

### Per-Item Activity Log

Every BLI file has an Activity Log section. Append a row for each event:

```markdown
| Date | Time | Action | Details |
|---|---|---|---|
| 2026-03-27 | 10:30 AM | status → in-progress | Started work |
| 2026-03-27 | 04:15 PM | status → done | Completed — all acceptance criteria met |
```

### Effort Estimation

| Size | Meaning | Rough Duration |
|---|---|---|
| **XS** | Trivial — config change, typo fix | < 1 hour |
| **S** | Small — single file, straightforward | 1–4 hours |
| **M** | Medium — multi-file, some complexity | 0.5–2 days |
| **L** | Large — significant feature, research needed | 2–5 days |
| **XL** | Extra large — epic-level, multi-week | 1–3 weeks |

---

## Sprint Management

Sprints are optional time-boxed iterations for focused work. They live in
`sprints/` and follow the sprint template.

### Sprint Lifecycle

```text
planned → active → completed
                 → cancelled (if abandoned mid-sprint)
```

### Starting a Sprint

1. Create a sprint file in `sprints/` using the sprint template
2. Assign the next sequential `SPRINT-NNN` ID
3. Set a clear, measurable sprint goal
4. Pull items from the backlog into the sprint (update `sprint:` in each item)
5. Set `status: active` and record start date
6. Update BOARD.md Active Sprint section
7. Log `sprint-start` in CHANGELOG.md

### During a Sprint

- Track daily progress in the sprint file (optional but recommended)
- Update item statuses as work progresses (also updates BOARD.md + views)
- New items discovered mid-sprint can be added if capacity allows
- Blocked items should be flagged immediately with blocker description

### Ending a Sprint

1. Review all committed items — mark completed or carry forward
2. Set `status: completed` on the sprint
3. Fill in the Retrospective section
4. Carry-over items get `sprint:` updated to the next sprint (or cleared)
5. Update BOARD.md, views, and CHANGELOG.md
6. Calculate velocity (items completed / items committed)

### Rules

- Only **one active sprint** at a time
- Sprint duration: flexible, 1–2 weeks recommended
- Sprint ID format: `SPRINT-NNN_kebab-title.md`
- Sprint files are never deleted — they form the velocity history

---

## User Commands

### Core Commands

| Command | Effect |
|---|---|
| `/jot "thought"` | Capture anything — auto-classifies, enhances, creates items/ideas |
| `/jot "fix bug, add tests"` | Batch capture — creates multiple items from one input |
| `/jot "see E:\specs\plan.txt"` | File capture — reads file, extracts, creates items |
| `/read-file-jot "C:\notes\ideas.txt"` | File-to-backlog — reads file, extracts all items, creates in batch |
| `/todo "fix search bug"` | Alias for `/jot` — routes through unified capture |
| `/todos` | View the board |
| `/todos "done BLI-003"` | Update status via slash command |
| `/backlog brainstorm "auth approach"` | Open a brainstorm via slash command |
| `/backlog guide "error conventions"` | Create a GHCP guide via slash command |
| `/backlog refine IDEA-001` | Refine an idea via slash command |
| `/backlog promote IDEA-001` | Promote idea to item via slash command |

### View Commands

| Command | Effect |
|---|---|
| `/todos status` | Show the By Status view |
| `/todos projects` | Show the By Project view |
| `/todos priority` | Show the By Priority view |
| `/todos log` | Show recent CHANGELOG entries |
| `/todos stats` | Show velocity and completion metrics |

### Sprint Commands

| Command | Effect |
|---|---|
| `/backlog sprint start "goal"` | Create and start a new sprint |
| `/backlog sprint add BLI-NNN` | Add item to active sprint |
| `/backlog sprint remove BLI-NNN` | Remove item from active sprint |
| `/backlog sprint status` | Show active sprint progress |
| `/backlog sprint end` | Complete sprint, trigger retrospective |
| `/backlog sprint history` | Show past sprint summaries |
