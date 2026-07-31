---
id: BLI-089
title: Backlog item workspace — type-scoped workspace, two-way cross-index, promotion rules, and lifecycle checklist
status: todo
priority: medium
type: feature
created: 2026-07-31
updated: 2026-07-31
started: null
completed: null
blocked-since: null
review-since: null
epic: null
sprint: null
parent: null
sub-items: []
origin: null
estimated-effort: L
actual-effort: null
tags: [backlog, brain, workspace, cross-linking, indexing, promotion-path, instructions, template]
origin-type: manual
import-batch: null
source-file: null
---

# BLI-089: Backlog item workspace — type-scoped workspace, two-way cross-index, promotion rules, and lifecycle checklist

## Description

There are two types of content that arise when working on a backlog item:

1. **Knowledge that transcends the BLI** — a design session, a research note, an ADR.
   These have standalone value even if the BLI is deleted. They belong in the brain tiers
   (`sessions/`, `notes/`, `library/`) with a cross-reference back to the BLI.

2. **BLI-only ephemeral working material** — scratch notes jotted mid-sprint, a rough
   option comparison, an informal draft too raw for a session capture, reference files
   downloaded for this ticket. These are ticket-scoped ephemera. The brain tiers handle
   them badly (too much ceremony for too little value), and currently they have nowhere
   to go.

This BLI defines a **complete framework** for managing both types:

- Type-scoped workspace rules (items/features = flat; epics/projects = shallow structured)
- The flat-workspace guardrail — built-in maturity gate preventing brain-tier duplication
- Promotion maturity signals — explicit rules for when workspace content graduates
- Two-way cross-index (`by-backlog-item` view + BLI column in SESSION-LOG)
- AI automation — auto-update cross-index and BLI captures on session creation
- Enhanced BLI template with `## Related Captures` and `## Workspace` sections
- Structured BLI close checklist with workspace cleanup as a formal step
- Backlog instructions update encoding all protocol rules

### The Mental Model — Jira vs. Confluence

> **Brain tiers = Confluence** — shared, discoverable, reusable knowledge
>
> **BLI workspace = Jira attachments** — ticket-scoped, ephemeral, in-context working material

The brain tiers already work like Confluence. The backlog currently has no Jira-style
attachment support. This BLI adds that.

### The Routing Decision Rule

```text
Ask: "Would this content be useful if BLI-NNN were deleted tomorrow?"

YES → brain tiers (sessions/ notes/ library/) + cross-ref in BLI
NO  → BLI workspace sub-folder (BLI-NNN_title/)
```

Content in the BLI workspace can always be **promoted** to brain tiers when it matures —
the same path as `inbox/` → notes/sessions/library.

---

## Framework Components

### Component 1 — Type-Scoped Workspace Convention

**Naming rule:** workspace folder name = item filename without `.md` extension.

**Creation rule:** Never pre-create. Create on-demand only when ephemeral content needs
a home. Most items will never need a workspace folder.

#### Workspace Depth by Item Type

Different item types have different scopes and content volumes — workspace depth scales
accordingly:

| Type | Workspace depth | Rationale |
|---|---|---|
| `items/` (BLI) | **Shallow** — max 1 level of sub-folders | Short-lived; basic grouping useful |
| `ideas/` | **Shallow** — max 1 level of sub-folders | Pre-BLI; can benefit from grouping |
| `features/` | **Shallow** — max 1 level of sub-folders | Medium scope |
| `sprints/` | **Shallow** — max 1 level of sub-folders | Time-boxed; grouping by type useful |
| `epics/` | **Deeper shallow** — max 2 levels of sub-folders | Long-lived, higher volume |
| `projects/` | **Deeper shallow** — max 2 levels of sub-folders | Ongoing, broadest scope |

**Shallow structure for items/features/ideas/sprints (max 1 level deep):**

```text
backlog/items/BLI-089_workspace/
  scratch.md                     ← root-level files always allowed
  research/                      ← 1 level of grouping allowed
    competitor-analysis.md
  attachments/                   ← 1 level of grouping allowed
    design-mockup.png
```

**Deeper shallow structure for epics/projects (max 2 levels deep):**

```text
backlog/epics/EPIC-001_workspace/
  scratch.md                     ← root-level files always allowed
  research/                      ← level 1
    phase-1/                     ← level 2 (grouping within level 1)
      market-analysis.md
    phase-2/
      library-options.md
  decisions/                     ← level 1
    2026-q3/                     ← level 2
      adr-001-provider-pattern.md
```

**Level 1 vocabulary (fixed for all types):**

```text
research/    decisions/    meeting-notes/    drafts/    attachments/
```

**Level 2 vocabulary (epics/projects only — flexible naming within level 1):**

Level 2 names are context-driven — time-based (`phase-1/`, `2026-q3/`, `sprint-12/`)
or topic-based (`api-design/`, `java-migration/`). No fixed vocabulary at this level.

#### The Flat Guardrail (Updated per Tier)

> **For items/features/ideas/sprints:** needing 2+ levels = maturity signal → promote to brain tiers.
>
> **For epics/projects:** needing 3+ levels = maturity signal → promote to brain tiers.

The discomfort of "I need more structure here" is intentional — it means the content
has matured beyond ticket-scope and belongs in the knowledge hierarchy.

**Structure example for a BLI:**

```text
backlog/items/
  BLI-088_output-format-rules.md          ← the BLI (unchanged)
  BLI-088_output-format-rules/            ← workspace (flat, created on-demand)
    scratch.md                                raw working notes
    option-comparison.md                      informal draft, too rough for sessions/
    api-spec-draft.md                         working doc fragment
```

**Content types that belong in workspace:**

| Content | Example |
|---|---|
| Scratch notes | Quick "thinking out loud" while implementing |
| Informal comparisons | "Option A vs B rough notes" before a session is warranted |
| Working drafts | Document fragments not yet mature enough for brain tiers |
| Reference attachments | Downloaded specs, screenshots, design mockups |
| Calculation scratch | Quick estimates, sizing, rough math |
| Meeting/conversation notes | Notes from a standup or chat about this item |

**Content that does NOT belong in workspace** (use brain tiers instead):

| Content | Where instead |
|---|---|
| A full brainstorming session | `sessions/` + cross-ref in item |
| A distilled design decision | `notes/` + cross-ref in item |
| An imported reference doc | `library/` + cross-ref in item |
| An ADR | `sessions/` using `intent-capture.md` template |

### Component 2 — Promotion Path and Maturity Signals

Promotion is **explicit** — triggered by maturity signals, not by time or size.

#### Maturity Signals (any ONE is sufficient to trigger promotion)

| Signal | Meaning | Promote to |
|---|---|---|
| You want to **reference this from another BLI** | Content has cross-item value | `notes/` or `sessions/` |
| You **cite it in a conversation** with someone | Implies standalone value | `notes/` |
| The content **answers a question you'll ask again** | General reusability | `notes/` or `sessions/` |
| It grows **beyond 1 page** of meaningful analysis | Too rich for scratch | `sessions/` |
| You **restructure or rewrite** it more than once | Sign it's maturing | `sessions/` or `notes/` |
| It represents a **decision** that should outlive the BLI | ADR-level significance | `sessions/` using `intent-capture.md` |
| You feel the need to **add sub-folders** to a flat workspace | Complexity = maturity signal | Promote the sub-folder content |

#### Promotion Path

```text
Workspace file → inbox/ (initial landing) → brain tier (notes/ sessions/ library/)
```

**On promotion:**
1. Move content from workspace → `inbox/` using the standard inbox flow
2. Process from inbox → correct brain tier (notes/sessions/library)
3. Workspace file: delete OR replace with a one-line stub: `Promoted → [link]`
4. Update the item's `## Related Captures` section with the promoted brain-tier location
5. Update `views/by-backlog-item.md` with the new brain-tier entry

#### Promotion from Epic Workspace Sub-Folders

When an epic's workspace sub-folder content matures:

```text
EPIC-001_workspace/research/competitor-analysis.md
  → inbox/
  → sessions/personal/personal-work/software-dev/research/
```

The epic `## Related Captures` section is updated. The sub-folder file is deleted or stubbed.

### Component 3 — Two-Way Cross-Index

Navigation between the two worlds (backlog ↔ brain tiers) must work in both directions.
A single index in one place is not enough — links go stale when only maintained from one side.

#### Direction 1 — Backlog → Brain: `views/by-backlog-item.md`

New view file in `backlog/views/`. Answers: "What brain-tier content exists for this item?"

```markdown
# View: By Backlog Item

| Item | Title | Sessions | Notes | Library | Workspace? |
|---|---|---|---|---|---|
| BLI-088 | Define output format rules | [design/2026-07-31...](link) | — | — | no |
| BLI-089 | Backlog workspace framework | [design/2026-07-31...](link) | — | — | no |
| EPIC-001 | Learning resources system | [design/2026-03-22...](link) | — | — | no |
```

Covers ALL item types: BLIs, epics, features, ideas, sprints, projects.

#### Direction 2 — Brain → Backlog: BLI column in SESSION-LOG.md

A new `BLI` column added to `SESSION-LOG.md`. Answers: "Which item does this session relate to?"

```markdown
| Date | Time | Domain | Category | Subject | Ver | Complexity | Status | BLI | File |
|---|---|---|---|---|---|---|---|---|---|
| 2026-07-31 | 09:33 PM | personal | design | copilot-output-format-primitive | v1 | medium | draft | BLI-088 | [View](...) |
```

`BLI` column contains the primary backlog item this session relates to, or `—` if global/unlinked.

#### Sync Rule — Keeping Both in Sync

Both directions update together — never one without the other. The AI automation
(Component 5a) enforces this. Manual updates must also touch both.

```text
Capture session → update SESSION-LOG (add BLI column) AND by-backlog-item.md
Add cross-ref to BLI → update by-backlog-item.md AND item's ## Related Captures
```

### Component 4 — Enhanced BLI Template

Add two new sections to `_templates/item.md`:

```markdown
## Related Captures

<!-- Brain-tier content (sessions, notes, library) cross-referencing this BLI.
     These are knowledge artifacts that outlive the BLI.
     Add a row whenever a session or note is captured for this item. -->

| Type | Path | Date | Note |
|---|---|---|---|

## Workspace

<!-- BLI-local ephemeral content in the workspace sub-folder (BLI-NNN_title/).
     Only populated if a workspace folder was created for this item.
     Remove this section if no workspace folder exists. -->

| File | Purpose |
|---|---|
```

Apply retroactively to templates used by all backlog item types:
`item.md`, `epic.md`, `guide.md` — update all three.

### Component 5a — AI Automation Protocol

The highest-friction scenario today: a session is captured that cross-references a BLI, but
the BLI's `## Related Captures` and `by-backlog-item.md` require manual updates.

**Automation rule (encoded in `backlog.instructions.md` and `chat-capture.instructions.md`):**

> **Whenever a session or note is captured that contains a `scope-refs` entry pointing to
> a backlog item (BLI/EPIC/feature/etc.), the AI MUST automatically:**
>
> 1. Update the item's `## Related Captures` section with a new row
> 2. Update `views/by-backlog-item.md` with the new session/note link
> 3. Update `SESSION-LOG.md` BLI column for the new session row
>
> These three updates happen in the same turn as the session capture — never deferred.

**Trigger detection:** Scan frontmatter `scope-refs[]` entries. Any entry whose `file`
path points into `backlog/` (items/, features/, epics/, etc.) triggers the automation.

**Reverse trigger:** When a BLI's `## Related Captures` is updated manually,
`views/by-backlog-item.md` must also be updated in the same operation.

### Component 5b — Backlog Instructions Update

`backlog.instructions.md` needs these new protocol rules:

1. **Workspace creation rule** — when/how to create a type-scoped workspace sub-folder
2. **Workspace depth rule** — flat for items/features/ideas/sprints; shallow (1 level, fixed vocabulary) for epics/projects
3. **Flat guardrail rule** — need for sub-folders = maturity signal → promote instead
4. **Routing rule** — decision question: "Would this survive item deletion?" → brain tiers or workspace
5. **Promotion maturity signals** — the 7 signals that trigger promotion to brain tiers
6. **Promotion path** — workspace → inbox → brain tier, with cross-index update
7. **AI automation protocol** — three mandatory updates on every session/note capture with BLI ref
8. **BLI close checklist** — workspace cleanup as a formal step (Component 7)
9. **Two-way sync rule** — both cross-index directions always updated together

### Component 6 — Git Tracking Decision

**Decision to make:** Should workspace sub-folders be committed or `.gitignore`d?

| Option | Trade-off |
|---|---|
| **Commit workspace folders** | Scratch notes persist; shared across machines; but raw/informal content in repo history |
| **Gitignore workspace folders** (like `inbox/`) | Clean repo history; private scratch; but lost if not promoted |
| **Per-folder decision** via `.gitignore` | Flexible; add `BLI-NNN_title/.gitignore` to suppress specific folders |

**Recommended default:** Commit workspace folders but keep them lightweight. If scratch
notes are truly throwaway, delete them when the BLI closes. The `inbox/` comparison doesn't
fully apply because inbox is a capture landing zone; workspace is structured working space.

### Component 7 — Structured Item Close Checklist

Workspace cleanup is a **formal step** in the item close flow — not informal or optional.
Added to the definition of done for any item with a workspace folder.

**Item Close Checklist (workspace section):**

```markdown
### Workspace Cleanup (required if workspace folder exists)

- [ ] Scan workspace folder — list all files
- [ ] For each file, apply promotion maturity signals (Component 2)
      → Mature content: promote to inbox/ → brain tiers
      → Disposable scratch: delete
      → Reference-only: move to library/
      → Relevant to parent epic: move to EPIC-NNN_workspace/
- [ ] Update ## Related Captures in this item for any promoted content
- [ ] Update views/by-backlog-item.md for any promoted content
- [ ] Delete workspace folder once empty
- [ ] Confirm: no workspace sub-folders remain in backlog/ for this item ID
```

**For epics specifically** (close or phase-gate review):
- Sub-folder contents are reviewed before the epoch workspace is cleaned
- Decisions and ADRs in `decisions/` sub-folder are always candidates for `intent-capture.md` promotion
- Research in `research/` sub-folder reviewed for brain-tier value

**Accumulation prevention:** If a BLI has been `done` for more than 30 days and its
workspace folder still exists, flag it in `BOARD.md` as needing cleanup. The AI should
surf this during board review commands (`/todos`, `/backlog`).

---

## Sub-Item Decomposition (When Ready to Implement)

This is an L-effort item. Suggested decomposition when sprint-ready:

| # | Sub-Item | Effort | Depends on |
|---|---|---|---|
| 1 | Document workspace convention, type-depth rules, flat guardrail, routing rule | XS | — |
| 2 | Create `views/by-backlog-item.md` with initial BLI-088/BLI-089 entries | S | — |
| 3 | Add BLI column to `SESSION-LOG.md` + backfill existing sessions | S | — |
| 4 | Update `_templates/item.md` — add `## Related Captures` + `## Workspace` | S | 1 |
| 5 | Update `_templates/epic.md` and `_templates/guide.md` | XS | 4 |
| 6 | Update `backlog.instructions.md` — all 9 protocol rules | M | 1, 2, 3, 4 |
| 7 | Update `chat-capture.instructions.md` — AI automation trigger rule | S | 6 |
| 8 | Add workspace cleanup section to item close checklist in backlog instructions | XS | 6 |
| 9 | Decide git tracking + update `.gitignore` if needed | XS | 1 |
| 10 | Retroactively update existing items with known related captures | S | 2, 3, 4 |

### Future Considerations

- Workspace folder `scratch.md` starter template to make creation frictionless
- Search/grep across workspace folders independent of brain tier search
- Board review command (`/todos`) surfaces stale done-items with non-empty workspace folders
- Workspace file auto-stubbing on promotion (replace with one-liner pointing to brain tier)
- Epic phase-gate review checklist (mid-epic workspace review, not just at close)
- Cross-workspace search: find all workspace files that reference the same concept across BLIs

## Acceptance Criteria

**Workspace convention:**

- [ ] Type-depth rules documented: flat (items/features/ideas/sprints) vs. shallow (epics/projects)
- [ ] Shallow vocabulary fixed: `research/`, `decisions/`, `meeting-notes/`, `drafts/`, `attachments/`
- [ ] Flat guardrail rule stated: sub-folder need = maturity signal → promote instead
- [ ] Routing decision rule stated: single question ("would this survive item deletion?")
- [ ] Workspace naming convention documented (item filename without `.md`)
- [ ] Creation rule stated: on-demand only, never pre-created

**Promotion rules:**

- [ ] All 7 maturity signals documented
- [ ] Promotion path documented: workspace → inbox → brain tier
- [ ] Epic workspace sub-folder promotion path documented
- [ ] Cross-index update steps included in promotion path

**Two-way cross-index:**

- [ ] `views/by-backlog-item.md` created — covers all item types, initial entries for BLI-088/BLI-089
- [ ] `SESSION-LOG.md` updated with BLI column + existing sessions backfilled
- [ ] Two-way sync rule documented: both directions always updated together

**AI automation:**

- [ ] Automation rule encoded in `backlog.instructions.md`
- [ ] Automation rule encoded in `chat-capture.instructions.md` (trigger on `scope-refs` to backlog)
- [ ] Three mandatory auto-update steps defined (item Related Captures + by-backlog-item + SESSION-LOG)

**Templates:**

- [ ] `_templates/item.md` updated with `## Related Captures` and `## Workspace` sections
- [ ] `_templates/epic.md` updated
- [ ] `_templates/guide.md` updated

**Lifecycle:**

- [ ] Structured item close checklist with workspace cleanup section added to backlog instructions
- [ ] Epic phase-gate review checklist stub added
- [ ] Stale workspace flag rule documented (done >30 days + non-empty workspace = flag)

**Git tracking:**

- [ ] Git tracking decision made and `.gitignore` updated if needed

**Backfill:**

- [ ] All 9 protocol rules added to `backlog.instructions.md`
- [ ] Existing items with known related captures retroactively updated (minimum: BLI-088, BLI-089)

## Related Captures

| Type | Path | Date | Note |
|---|---|---|---|
| session | [`design/2026-07-31_09-33pm_design_copilot-output-format-primitive.md`](../sessions/personal/personal-work/software-dev/design/2026-07-31_09-33pm_design_copilot-output-format-primitive.md) | 2026-07-31 | Earlier session where Jira/Confluence model was first articulated (BLI-088 brainstorm) |
| session | [`design/2026-07-31_09-48pm_design_backlog-workspace-cross-index-framework.md`](../sessions/personal/personal-work/software-dev/design/2026-07-31_09-48pm_design_backlog-workspace-cross-index-framework.md) | 2026-07-31 | Full brainstorm — all 7 exchanges, all design decisions and rationale captured here |

## Attachments & References

| Type | Path / URL | Added | Notes |
|---|---|---|---|
| related-bli | `items/BLI-088_output-format-response-structure-rules.md` | 2026-07-31 | `by-backlog-item` view idea originated here — superseded by this BLI |
| instruction | `.github/instructions/backlog.instructions.md` | 2026-07-31 | File that needs updating (Component 5b) |
| template | `backlog/_templates/item.md` | 2026-07-31 | Template file that needs updating (Component 4) |
