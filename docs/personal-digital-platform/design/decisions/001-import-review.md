# Decision 001: iterative import review

Status: provisionally accepted by the user; revisit with real examples.
Recorded: 2026-10-27. Origin: user architecture discussion in this project.

## Context

Imported material may enhance existing notes. The user wants to preserve desired content,
inspect differences and iterate selected changes before applying them.

## Decision

| ID | Behavior | Status |
|---|---|---|
| R01 | Inspect existing/related content before adding; propose enhancement when appropriate. | Agreed |
| R02 | May propose rewriting existing content, but show additions/modifications/removals and preserve desired meaning. | Agreed |
| R03 | Iterate discussion and revisions until approved; liking a direction does not authorize application. | Agreed |
| R04 | One checklist item per independently meaningful change, grouped under note/section. Small formatting changes may stay with parent item. | Accepted for now |
| R05 | Review selected items first; others remain pending. Example: 1 needs examples, 2 developer-friendly explanation, 3 best practices, 4–10 pending. | Agreed |
| R06 | Support applying an approved subset or the full import; offer subset application by default. | Agreed |
| R07 | Default subset path is not automatic execution. Preview combined result, flag dependencies/inconsistencies, then require explicit green signal to apply. | Agreed |
| R08 | Approval belongs to a specific proposal version. Revisions affecting accepted portions require renewed review; unrelated approvals remain valid. | Agreed |
| R09 | Later changes to applied content are new proposed amendments showing what/why, not silent overwrites. | Agreed |
| R10 | Track pending review, revision requested, approved, applied, deferred and rejected; retain feedback/history. | Provisional workflow vocabulary, not a finalized state-machine implementation |

The proposal workspace may change during iteration. Accepted knowledge changes only on explicit application authorization.
No data model, storage mechanism, UI, transactional behavior or multi-device conflict strategy has yet been chosen.

## Rationale and alternatives

Use one item per independently meaningful change, grouped by note or section.
This permits partial acceptance without a checkbox for every wording edit.
Both approved-subset and whole-import application are supported; offer the subset path by default.
Neither option authorizes automatic application.

## Consequences

Proposal versions, dependencies and approval history must be represented.
Storage, UI, rollback, failed application and concurrent manual edits remain unresolved.
This record accepts behavior in principle, not an implementation architecture.

Related: [requirements](../../requirements/requirements-register.md), G08 and K03–K05 in
[inventory](../../discovery/source-inventory.md).
