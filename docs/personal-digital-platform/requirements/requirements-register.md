# Requirements register

Status: discovery baseline, not a finalized SRS.
Accepted preferences below still need measurable acceptance criteria.
No new-platform implementation or test coverage is claimed.

## Confirmed direction and boundaries

| ID | Record | Status / origin |
|---|---|---|
| U01 | Long-lived Personal Digital Platform; PKM and Learning first; Productivity and Planning evolve from real need. | User context transfer |
| U02 | Manual note editing and agent-assisted creation/import/organization/linking/enhancement. | User preference |
| U03 | Rich navigation, metadata, tags, relationships and lexical/semantic/AI retrieval. | User preference; implementation open |
| U04 | Desktop and Android, portability, privacy, backups, longevity. | User preference; measurable requirements pending |
| U05 | Learn software engineering through iterative architecture discussion and pair programming. | User preference |
| U06 | Existing repo is reference and evidence, not approved architecture; intentional review of retained/removed/rebuilt ideas. | Explicit user instruction |
| U07 | Complete extraction -> user confirms coverage -> further brainstorming/questions -> starting draft design -> authorization before new repo/IntelliJ/implementation. | Explicit user sequence |
| U08 | Save extraction and decision references for future design. | Documentation stored in learning-assistant; PR #91 merged |
| U09 | User will later create a separate repository named personal-digital-platform for the reviewed redesign. learning-assistant remains reference material, not an inherited architecture. | Explicit clarification, 2026-10-27; creation/implementation not yet authorized |

## Traceability

- U02 maps to inventory K01–K07 and review behavior R01–R10.
- U03 maps to K08–K11.
- U04 affects all domains; offline, privacy, backup and performance targets remain unspecified.
- U06–U07 map to G06–G08 and the completeness-review gate.
- Inventory candidates are not automatically accepted requirements.

## Deferred questions

- Initial concrete use cases and success criteria; product scope versus adjacent experiments.
- Agent count and orchestration; skill composition versus delegation; PKM/Learning overlap.
- Storage/vault ownership, internal module organization, deployment and technology stack. The separate successor repository name is confirmed; any additional vault repositories remain open.
- Manual editing, source retention, provenance, re-import, contradiction handling and cross-device concurrency.
- Granular review dependencies, rollback, interrupted/failed application and review persistence.
- Privacy boundaries, local/cloud processing and integration feasibility.
- Search quality/evaluation, learning progress/assessment and revision behavior.
- Backup/restore expectations, offline behavior, portability and accessibility.
- Which existing pieces to retain/refactor/merge/redesign/migrate/defer/intentionally remove.

See [inventory](../discovery/source-inventory.md), [review agreement](../design/decisions/001-import-review.md)
and [checkpoint](../reviews/current-checkpoint.md).
