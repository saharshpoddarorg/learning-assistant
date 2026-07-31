---
date: 2026-07-31
time: "09:48 PM"
kind: session-capture
domain: personal
category: design
project: learning-assistant
subject: backlog-workspace-cross-index-framework
tags: [project:learning-assistant, backlog, brain, workspace, cross-linking, jira-confluence-model, two-way-index, promotion-path, lifecycle, ai-automation]
status: draft
version: 1
parent: null
complexity: high
outcomes:
  - confirmed Jira/Confluence mental model as the foundation
  - decided type-scoped workspace depth rules (flat vs. shallow by item type)
  - defined flat-workspace guardrail as maturity gate
  - decided two-way cross-index (by-backlog-item.md + SESSION-LOG BLI column)
  - defined 7 promotion maturity signals
  - committed AI automation as mandatory protocol (not future consideration)
  - designed structured item close checklist with workspace cleanup
  - created BLI-089 capturing all decisions
source: copilot
scope: feature
scope-project: learning-assistant
scope-feature: backlog-workspace-framework
scope-transitions: []
scope-refs:
  - file: "../../../../../../backlog/items/BLI-089_backlog-item-workspace-cross-index-promotion-path.md"
    relationship: spawned
    note: "BLI-089 created from this brainstorming session"
  - file: "2026-07-31_09-33pm_design_copilot-output-format-primitive.md"
    relationship: related
    note: "Earlier session same day — BLI-088 brainstorm; BLI-089 idea emerged from BLI-088's indexing discussion"
design-target:
  component: backlog-workspace-framework
  aspect: proposal
  level: hld
---

# Design — Backlog Workspace Framework: Type-Scoped Storage, Two-Way Cross-Index, and Lifecycle

> **Context:** Brainstorm on whether backlog items (BLIs, epics, features, projects) should
> have associated sub-folder workspaces for BLI-scoped content, alongside proper cross-linking
> between the backlog and the brain tier hierarchy. Evolved from a question about where the
> BLI-088 brainstorm session should live.

---

## Component Overview

| Property | Value |
|---|---|
| Component | `backlog-workspace-framework` |
| Aspect | `proposal` — evaluating approach and making decisions |
| Level | HLD |
| Related Components | `backlog/`, `brain/ai-brain/sessions/`, `brain/ai-brain/notes/`, `backlog.instructions.md` |

---

## Intent & Purpose

- **Intent:** Define a complete framework for managing two types of content that arise
  when working on a backlog item: knowledge that transcends the item (→ brain tiers) and
  ticket-scoped ephemeral working material (→ BLI workspace)
- **Constraints:** Must not duplicate the brain tier hierarchy inside the backlog;
  must not create maintenance overhead that makes cross-linking go stale
- **Success criteria:** Clear routing rule that can be applied in seconds; cross-index
  maintained automatically by AI; no accumulation of stale workspace folders

---

## Exchange 1 — Initial Question: Sub-Folders for BLI Content?

### Request

When a session was captured for BLI-088 and placed in `sessions/personal/.../design/`,
the question arose: should BLI items have their own sub-folder structure where related
content (sessions, notes, scratch) is stored alongside them, with proper interlinking?

### Analysis

**The core tension:**

The brain tier hierarchy (`sessions/`, `notes/`, `library/`) exists for **cross-cutting
discovery** — SESSION-LOG, escalation patterns, and skill-based navigation all depend on
sessions living in known paths.

If sessions move into `backlog/items/BLI-088/sessions/`, they:
- Fall off SESSION-LOG (or require double-maintenance)
- Break the escalation/sub-package protocol
- Become invisible to sessions/ navigation
- Create a competing hierarchy inside backlog

**Initial recommendation:** Keep physical separation, improve cross-linking only. Add a
`by-backlog-item` view and a richer BLI template `## Related Captures` section.

---

## Exchange 2 — Counter-Proposal: Both Approaches Together

### Request

"What about maintaining the brain structure (Confluence-like) AND having BLI-specific
local content (Jira-like), along with proper index/cross-linking at appropriate places?"

### Analysis

This prompted a deeper look. There are actually **two distinct content types**:

| Type | Example | Current home | Problem |
|---|---|---|---|
| Knowledge transcending the BLI | Design session, research note, ADR | Brain tiers ✓ | None — works well |
| BLI-only ephemeral material | Scratch notes, rough drafts, calculations | **Nowhere** | Has no good home today |

The second type is what brain tiers handle badly — too raw/small for a full session capture,
too BLI-specific for the full knowledge management treatment.

**The Jira/Confluence analogy:**

> **Brain tiers = Confluence** — shared, discoverable, reusable knowledge
>
> **BLI workspace = Jira attachments** — ticket-scoped, ephemeral, in-context working material

Conclusion: **Both approaches are right, for different content types.** The routing rule
is the single decision question:

```text
"Would this content be useful if the BLI were deleted tomorrow?"

YES → brain tiers (sessions/ notes/ library/) + cross-ref in BLI
NO  → BLI workspace sub-folder
```

---

## Exchange 3 — Workspace Depth: Flat or Structured?

### Question

If workspace sub-folders are allowed, do they allow further sub-folders? Risk: rebuilding
brain tiers inside the backlog.

### Decision: Type-Scoped Depth

Different item types warrant different workspace depth:

| Type | Workspace depth | Rationale |
|---|---|---|
| `items/` (BLI) | **Shallow** — max 1 level | Short-lived; basic grouping useful |
| `ideas/` | **Shallow** — max 1 level | Pre-BLI; can benefit from grouping |
| `features/` | **Shallow** — max 1 level | Medium scope |
| `sprints/` | **Shallow** — max 1 level | Time-boxed; grouping by type useful |
| `epics/` | **Deeper shallow** — max 2 levels | Long-lived, higher content volume |
| `projects/` | **Deeper shallow** — max 2 levels | Ongoing, broadest scope |

**Level 1 vocabulary (fixed for all types):**

```text
research/    decisions/    meeting-notes/    drafts/    attachments/
```

**Level 2 vocabulary (epics/projects only — flexible):** time-based (`phase-1/`, `2026-q3/`)
or topic-based (`api-design/`, `java-migration/`). No fixed vocabulary at level 2.

**The guardrail per tier:**

> **items/features/ideas/sprints:** needing 2+ levels = maturity signal → promote to brain tiers.
>
> **epics/projects:** needing 3+ levels = maturity signal → promote to brain tiers.

---

## Exchange 4 — Cross-Index: Single Index or Two-Way?

### Question

Should the cross-index live only in the backlog (`by-backlog-item` view), or also in the
brain tier (SESSION-LOG with a BLI column)?

### Decision: Two-Way (Option B)

A single index in one place is insufficient — links go stale when only maintained from one side.

**Direction 1 — Backlog → Brain:** `views/by-backlog-item.md`

> "What brain-tier content exists for this item?"

```markdown
| Item | Title | Sessions | Notes | Library | Workspace? |
|---|---|---|---|---|---|
| BLI-088 | Define output format rules | [design/2026-07-31...] | — | — | no |
| BLI-089 | Backlog workspace framework | [design/2026-07-31...] | — | — | no |
```

Covers all item types: BLIs, epics, features, ideas, sprints, projects.

**Direction 2 — Brain → Backlog:** BLI column in `SESSION-LOG.md`

> "Which item does this session relate to?"

```markdown
| Date | Time | Domain | Category | Subject | Ver | Complexity | Status | BLI | File |
| 2026-07-31 | 09:48 PM | personal | design | backlog-workspace... | v1 | high | draft | BLI-089 | [View] |
```

**Sync rule:** Both directions always update together — never one without the other.

---

## Exchange 5 — AI Automation

### Question

Should the cross-index updates be manual or automated by the AI?

### Decision: Automated (Mandatory Protocol)

When a session/note is captured with a `scope-refs` entry pointing to a backlog item,
the AI must **automatically** (same turn, never deferred):

1. Update the item's `## Related Captures` section
2. Update `views/by-backlog-item.md`
3. Update `SESSION-LOG.md` BLI column

**Trigger detection:** Scan `scope-refs[]` frontmatter. Any entry whose `file` path points
into `backlog/` triggers all three updates.

**Reverse trigger:** Manual update to a BLI's `## Related Captures` → also update
`by-backlog-item.md` in the same operation.

---

## Exchange 6 — Promotion Rules

### Question

What signals tell you workspace content is ready to promote to brain tiers?

### Decision: 7 Maturity Signals

Any ONE is sufficient to trigger promotion:

| Signal | Promote to |
|---|---|
| Want to reference from another BLI | `notes/` or `sessions/` |
| Cite it in conversation with someone | `notes/` |
| It answers a question you'll ask again | `notes/` or `sessions/` |
| Grows beyond 1 page of meaningful analysis | `sessions/` |
| You restructure or rewrite it more than once | `sessions/` or `notes/` |
| Represents a decision that should outlive the BLI | `sessions/` via `intent-capture.md` |
| You feel the need to add sub-folders in a flat workspace | Promote the sub-folder content |

**Promotion path:**

```text
Workspace file → inbox/ (initial landing) → brain tier (notes/ sessions/ library/)
```

On promotion: update the item's `## Related Captures` + `by-backlog-item.md`.

---

## Exchange 7 — Lifecycle: Informal or Structured?

### Question

Should workspace cleanup on BLI close be informal or part of a formal checklist?

### Decision: Structured Checklist

Workspace cleanup is a formal step in the item close flow — not optional. The close
checklist includes:

```text
- Scan workspace folder — list all files
- Apply maturity signals to each file
  → Mature: promote to inbox/ → brain tiers
  → Disposable: delete
  → Reference-only: move to library/
  → Relevant to parent epic: move to EPIC-NNN_workspace/
- Update ## Related Captures + by-backlog-item.md for promoted content
- Delete workspace folder once empty
```

**Stale workspace flag:** If a BLI has been `done` for >30 days and workspace folder
still exists → flag during board review commands (`/todos`, `/backlog`).

---

## Acceptance Criteria Summary

*(Full ACs in BLI-089 — this is the condensed implementation checklist view)*

- [ ] Type-depth workspace rules documented
- [ ] Flat guardrail rule stated
- [ ] 7 maturity signals documented
- [ ] `views/by-backlog-item.md` created
- [ ] `SESSION-LOG.md` BLI column added + backfilled
- [ ] AI automation encoded in `backlog.instructions.md` + `chat-capture.instructions.md`
- [ ] `_templates/item.md`, `epic.md`, `guide.md` updated
- [ ] Structured close checklist with workspace cleanup in backlog instructions

---

## Key Outcomes

- Jira/Confluence mental model confirmed as the right foundation — two separate worlds, properly bridged
- Type-scoped workspace depth: flat for items/features/ideas/sprints; shallow (fixed vocab) for epics/projects
- Flat guardrail is the key design insight — discomfort = maturity signal
- Two-way cross-index is necessary to prevent stale links over time
- AI automation makes the cross-index zero-maintenance — high-value, low-risk
- 7 promotion maturity signals give clear criteria instead of subjective "is it mature?"
- Structured close checklist prevents dead workspace folder accumulation
- BLI-089 created capturing all decisions and 10-item sub-item decomposition

---

## Follow-Up / Next Steps

- [ ] Implement BLI-089 (when sprint-ready, decompose into 10 sub-items)
- [ ] Pre-work: add BLI column to SESSION-LOG — even before full implementation
- [ ] Pre-work: retroactively add `## Related Captures` to BLI-088 and BLI-089

---

## Cross-References

| Relationship | Session | Note |
|---|---|---|
| related | `2026-07-31_09-33pm_design_copilot-output-format-primitive.md` | Same-day session — BLI-088 brainstorm that sparked BLI-089 |

---

## Session Metadata

| Property | Value |
|---|---|
| Duration | ~7 exchanges |
| Files touched | `backlog/items/BLI-089_...md`, `BOARD.md`, `CHANGELOG.md`, `views/by-status.md`, `items/BLI-088_...md` |
| Related BLI | [BLI-089](../../../../../backlog/items/BLI-089_backlog-item-workspace-cross-index-promotion-path.md) |
| Related session | [BLI-088 brainstorm](2026-07-31_09-33pm_design_copilot-output-format-primitive.md) |
