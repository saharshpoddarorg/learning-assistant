---
id: BLI-021
title: Create reusable content templates system
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
estimated-effort: M
actual-effort: null
tags: [templates, confluence, brain-notes, session-capture, reusable, standardisation]
origin-type: file-import
import-batch: IMP-001
source-file: "D:\\workdir\\MG_FTE\\notepad\\personal dev\\learning-assistant\\gpt.txt"
---

# BLI-021: Create reusable content templates system

## Description

Build a system of reusable templates for different content creation scenarios:
Confluence page creation, brain note creation, chat-session capture, backlog items,
meeting notes, decision records, and more. Templates should be discoverable,
customizable, and auto-applied based on content type. Extend the existing
`_templates/` pattern used in the backlog to cover all brain and documentation
content types.

## Future Considerations

- Template marketplace — share templates across projects/repos
- Variable substitution in templates (e.g., `${date}`, `${project}`)
- Template versioning — track changes to templates over time
- AI-assisted template selection based on user intent

## Sub-Items

| ID | Title | Status | Effort |
|---|---|---|---|

## Acceptance Criteria

- [ ] Confluence page templates — standard layouts for different page types
- [ ] Brain note templates — structured formats for `brain/ai-brain/notes/`
- [ ] Session capture templates — beyond current `_templates/` in sessions/
- [ ] Template index — a discoverable registry of all available templates
- [ ] Auto-suggestion — when creating content, suggest the appropriate template
- [ ] Customization — users can modify templates and add their own
- [ ] Documentation — each template has usage instructions and examples

## Attachments & References

| Type | Path / URL | Added | Notes |
|---|---|---|---|
| Source file | `D:\workdir\MG_FTE\notepad\personal dev\learning-assistant\gpt.txt` | 2026-04-11 | Extracted from file import IMP-001 |
| Existing templates | `brain/ai-brain/backlog/_templates/` | 2026-04-11 | Current backlog templates (item, idea, brainstorm, epic, guide, sprint) |
| Session templates | `brain/ai-brain/sessions/_templates/` | 2026-04-11 | Current session capture templates |

## Related

- [EPIC-002](../epics/EPIC-002_knowledge-consolidation.md) — parent epic
- [BLI-010](../features/BLI-010_personal-confluence-migration.md) — Confluence templates needed

## Activity Log

| Date | Time | Actor | Action | Details |
|---|---|---|---|---|
| 2026-04-11 | 10:08 PM | system | created | Created via /read-file-jot from IMP-001 |

## Notes

- Raw input: "template for diff things -- like confluence creation, brain notes creation, chat-session capture...." listed under "// jot-todos" section
- Existing pattern: `backlog/_templates/` already works well — extend this approach
- Consider a `_templates/` folder at `brain/ai-brain/_templates/` for brain-wide templates
