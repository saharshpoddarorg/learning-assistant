---
id: EPIC-006
title: Iterative feature brainstorming — learning-assistant
status: active
priority: medium
created: 2026-05-29
updated: 2026-05-29
target-date: null
tags: [brainstorming, features, iterative, learning-assistant, ideation, incremental]
---

# EPIC-006: Iterative feature brainstorming — learning-assistant

## Vision

A living, open-ended epic for brainstorming learning-assistant features iteratively.
Unlike fixed-scope epics with predefined items, this epic is a **continuous ideation
space** — come back at any time to brainstorm a new feature, flesh out an existing one,
promote an idea to implementation, or enhance something already built. No fixed end
state; the epic grows organically as ideas emerge across sessions.

## How to Use This Epic

This epic supports **any stage** of the feature lifecycle:

| Stage | What you do | How |
|---|---|---|
| **Ideate** | Brainstorm raw feature ideas | `/jot` or chat — auto-linked here |
| **Explore** | Research feasibility, alternatives | Refine an existing idea (v2, v3) |
| **Scope** | Define AC, effort, priority | Promote IDEA → BLI under this epic |
| **Build** | Implement the feature | Move BLI to in-progress |
| **Enhance** | Improve something already shipped | Create a new BLI referencing the original |
| **Revisit** | Re-brainstorm or pivot | Add refinement pass or new brainstorm |

### Entry Points

- **"I have a vague idea"** → captured as IDEA-NNN, tagged to this epic
- **"I want to build X"** → created as BLI-NNN in `features/`, linked here
- **"Let's brainstorm options for Y"** → brainstorm IDEA-NNN linked here
- **"Enhance the existing Z feature"** → new BLI referencing the original
- **"What should I build next?"** → review items below, pick one to scope

## Progress

```text
Total items:   11
Completed:      0  (0%)
In progress:    0
Blocked:        0
Remaining:     11

Ideas linked:   0
Brainstorms:    0
```

## Items

> **Folder:** `features/` — learning-assistant enhancements

| ID | Title | Status | Priority | Started | Completed |
|---|---|---|---|---|---|
| BLI-075 | Search engine — online and local knowledge search | todo | high | — | — |
| BLI-076 | Web scraper — intelligent content extraction and filtering | todo | high | — | — |
| BLI-077 | PKM/brain/notes manager — full knowledge lifecycle | todo | high | — | — |
| BLI-078 | Question refinement — smarter query formulation for GHCP | todo | medium | — | — |
| BLI-079 | Presentation of information — structured and distributable output | todo | medium | — | — |
| BLI-080 | Search engine — labelling system for structured classification | todo | medium | — | — |
| BLI-081 | Search engine — tagging system for flexible categorization | todo | medium | — | — |
| BLI-082 | Search engine — full-text indexing engine for local knowledge | todo | high | — | — |
| BLI-083 | Search engine — metadata index and faceted search | todo | medium | — | — |
| BLI-084 | Search engine — auto-classification and entity extraction | todo | medium | — | — |
| BLI-086 | Resume & LinkedIn skill — auto-update from work history | todo | medium | — | — |

## Linked Ideas & Brainstorms

> Ideas and brainstorms captured as part of iterative feature ideation.

| ID | Title | Status | Created | Tags |
|---|---|---|---|---|

## Open Questions

- [ ] What areas of learning-assistant are most ripe for new features?
- [ ] Should brainstormed features feed into existing epics or stay here until scoped?
- [ ] Preferred brainstorming format — freeform chat, structured brainstorm template, or both?

## Activity Log

| Date | Time | Actor | Event | Details |
|---|---|---|---|---|
| 2026-06-02 | 05:00 PM | system | item-added | Added BLI-086: Resume & LinkedIn skill — auto-update from work history (composes atlassian/github skills) |
| 2026-05-29 | 09:21 PM | system | items-added | Added 5 sub-items of BLI-075: BLI-080 (labelling), BLI-081 (tagging), BLI-082 (indexing), BLI-083 (faceted search), BLI-084 (auto-classification) |
| 2026-05-29 | 08:58 PM | system | items-added | Added 5 brainstormed features: BLI-075 through BLI-079 |
| 2026-05-29 | 08:49 PM | system | created | Epic created — open-ended iterative feature brainstorming space |

## Notes

- This epic intentionally has **no fixed item list at creation** — items are added
  iteratively as brainstorming sessions happen
- Features brainstormed here may eventually move to other epics (EPIC-001, EPIC-002,
  etc.) once they have clear scope and belong to an existing theme
- Use `/jot` with a mention of "brainstorm" or "feature idea" to auto-link here
- This epic is always `active` — it never completes because ideation is continuous
