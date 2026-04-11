---
id: BLI-023
title: Build cross-repo item merge and sync
status: todo
priority: medium
type: feature
created: 2026-04-11
updated: 2026-04-11
started: null
completed: null
blocked-since: null
review-since: null
epic: EPIC-002
sprint: null
parent: null
sub-items: []
origin: null
estimated-effort: L
actual-effort: null
tags: [merge, cross-repo, sync, dedup, session-capture, migration]
origin-type: file-import
import-batch: IMP-001
source-file: "D:\\workdir\\MG_FTE\\notepad\\personal dev\\learning-assistant\\gpt.txt"
---

# BLI-023: Build cross-repo item merge and sync

## Description

Build tooling and workflows to merge and synchronize captured items (backlog items,
session captures, notes, brain content) across multiple repositories and workspaces.
This addresses the scenario where ideas and notes are captured in different repos,
machines, or tools (Notepad++, Google Keep, phone notes) and need to be consolidated
into a single backlog without duplication. Extend the existing `/read-file-jot`
dedup workflow to handle cross-repo scenarios.

## Future Considerations

- Git-based sync between learning-assistant and other personal repos
- Conflict resolution UI for merge decisions
- Bi-directional sync (push changes back to source repo)
- Scheduled automated sync

## Sub-Items

| ID | Title | Status | Effort |
|---|---|---|---|

## Acceptance Criteria

- [ ] Cross-repo discovery — identify items in other repos that should be merged here
- [ ] Dedup engine — detect duplicates across repos using title/tag/content similarity
- [ ] Merge workflow — guided merge process with user confirmation
- [ ] Sync log — track what was merged from where and when
- [ ] Conflict resolution — handle cases where same item exists differently in two repos
- [ ] Audit trail — every merge action logged in CHANGELOG and IMPORT-LOG

## Attachments & References

| Type | Path / URL | Added | Notes |
|---|---|---|---|
| Source file | `D:\workdir\MG_FTE\notepad\personal dev\learning-assistant\gpt.txt` | 2026-04-11 | Extracted from file import IMP-001 |
| Existing workflow | `.github/docs/capture-workflow.md` | 2026-04-11 | Current jot capture and file-import workflow |

## Related

- [EPIC-002](../epics/EPIC-002_knowledge-consolidation.md) — parent epic

## Activity Log

| Date | Time | Actor | Action | Details |
|---|---|---|---|---|
| 2026-04-11 | 10:08 PM | system | created | Created via /read-file-jot from IMP-001 |

## Notes

- Raw input: "merge across diff repos as needed" from "// jot-todos"
- The `/read-file-jot` workflow already handles single-file imports with dedup
- This extends that to cross-repo scenarios (multiple source repos)
