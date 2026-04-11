---
id: BLI-025
title: Build library and glossary for abbreviations and aliases
status: todo
priority: low
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
estimated-effort: S
actual-effort: null
tags: [glossary, abbreviations, aliases, acronyms, library, reference]
origin-type: file-import
import-batch: IMP-001
source-file: "D:\\workdir\\MG_FTE\\notepad\\personal dev\\learning-assistant\\gpt.txt"
---

# BLI-025: Build library and glossary for abbreviations and aliases

## Description

Create a centralized glossary/library that defines all abbreviations, acronyms,
aliases, and domain-specific terms used across the learning-assistant workspace.
Examples: BLI, EPIC, PARA, CODE, PKM, MCP, GHCP, AC, NFR, etc. This improves
onboarding, documentation clarity, and enables Copilot to resolve ambiguous
abbreviations when processing user input.

## Future Considerations

- Auto-linking glossary terms in documentation (like a wiki)
- Glossary validation — check docs for undefined abbreviations
- Multi-language glossary (e.g., Hindi-English technical terms)
- Integration with MCP server for term lookup tool

## Sub-Items

| ID | Title | Status | Effort |
|---|---|---|---|

## Acceptance Criteria

- [ ] Glossary file — centralized `.md` file with all terms, abbreviations, acronyms
- [ ] Alphabetically sorted — easy to scan and find terms
- [ ] Each entry has: term, full form, brief definition, usage context
- [ ] Covers all repo-specific abbreviations (BLI, EPIC, PARA, CODE, PKM, MCP, etc.)
- [ ] Covers common SE abbreviations used in the project (AC, NFR, TDD, BDD, etc.)
- [ ] Cross-referenced from main docs (README, USAGE, copilot-instructions)

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

- Raw input: "library for abbrev, aliases etc" from "// jot-todos"
- Should be a simple .md file at `brain/ai-brain/` or `.github/docs/` level
- Could evolve into an MCP tool (glossary lookup) later
