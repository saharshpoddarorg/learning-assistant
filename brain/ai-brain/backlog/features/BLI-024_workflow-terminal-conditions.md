---
id: BLI-024
title: Create workflow and terminal conditions framework
status: todo
priority: medium
type: feature
created: 2026-04-11
updated: 2026-04-11
started: null
completed: null
blocked-since: null
review-since: null
epic: EPIC-001
sprint: null
parent: null
sub-items: []
origin: null
estimated-effort: M
actual-effort: null
tags: [workflow, automation, terminal-conditions, pipeline, state-machine]
origin-type: file-import
import-batch: IMP-001
source-file: "D:\\workdir\\MG_FTE\\notepad\\personal dev\\learning-assistant\\gpt.txt"
---

# BLI-024: Create workflow and terminal conditions framework

## Description

Define explicit workflow state machines with terminal conditions for key processes
in the learning-assistant system — backlog item lifecycle, session capture, brain
note promotion, import workflows, and sprint management. Each workflow should have
clearly defined states, transitions, guards, and terminal conditions (what constitutes
"done" or "failed"). This improves predictability and reduces ambiguity in how items
move through their lifecycle.

## Future Considerations

- Visual workflow editor (Mermaid diagrams auto-generated from definitions)
- Custom workflows per epic or project
- Workflow metrics — average time in each state, bottleneck detection
- Notification triggers at state transitions

## Sub-Items

| ID | Title | Status | Effort |
|---|---|---|---|

## Acceptance Criteria

- [ ] Backlog item lifecycle — define all states, transitions, and terminal conditions
- [ ] Session capture workflow — define capture gate → write → log → notify flow
- [ ] Brain note promotion — define inbox → notes lifecycle with conditions
- [ ] Import workflow — define file-import pipeline states and completion criteria
- [ ] Sprint workflow — define sprint start/end conditions and rollover rules
- [ ] Documentation — each workflow has a Mermaid state diagram and textual description
- [ ] Validation — scripts or prompts can check if current state is valid

## Attachments & References

| Type | Path / URL | Added | Notes |
|---|---|---|---|
| Source file | `D:\workdir\MG_FTE\notepad\personal dev\learning-assistant\gpt.txt` | 2026-04-11 | Extracted from file import IMP-001 |

## Related

- [EPIC-001](../epics/EPIC-001_learning-resources-system.md) — parent epic

## Activity Log

| Date | Time | Actor | Action | Details |
|---|---|---|---|---|
| 2026-04-11 | 10:08 PM | system | created | Created via /read-file-jot from IMP-001 |

## Notes

- Raw input: "proper workflow, terminal condition" from "// jot-todos"
- Existing workflows are implicit (defined in instructions.md prose) — this makes them explicit
- Mermaid state diagrams already used in capture-workflow.md — extend that pattern
