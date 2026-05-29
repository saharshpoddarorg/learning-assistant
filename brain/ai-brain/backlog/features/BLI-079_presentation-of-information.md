---
id: BLI-079
title: Presentation of information — structured and distributable output
status: todo
priority: medium
type: feature
created: 2026-05-29
updated: 2026-05-29
started: null
completed: null
blocked-since: null
review-since: null
epic: EPIC-006
sprint: null
parent: null
sub-items: []
origin: null
estimated-effort: L
actual-effort: null
tags: [presentation, formatting, notes-conversion, distributable, shareable, output]
origin-type: manual
import-batch: null
source-file: null
---

# BLI-079: Presentation of information — structured and distributable output

## Description

Information from GHCP should be presented in a **meaningful, easy-to-understand
format** that is also easy to convert to notes and distribute to others. This
feature covers output formatting, structure, and exportability — ensuring that
research, explanations, and analyses are not just accurate but also presentable
and shareable. Think structured sections, visual aids (tables, diagrams), clean
markdown, and export-ready formats.

### Future Considerations

- Output templates per content type (research report, tutorial, cheatsheet, etc.)
- Automatic table-of-contents generation for long outputs
- Diagram generation (Mermaid, PlantUML) embedded in responses
- Export to presentation formats (slides, PDF handouts)
- Collaborative annotation — others can comment on shared outputs
- Configurable verbosity levels (summary vs detailed vs exhaustive)

## Acceptance Criteria

- [ ] Information is presented with clear structure (headings, sections, hierarchy)
- [ ] Complex topics use visual aids (tables, diagrams, code blocks) appropriately
- [ ] Output is clean markdown that can be directly saved as a brain note
- [ ] Output is presentable enough to share with others without reformatting
- [ ] Supports conversion to multiple formats (markdown, plain text, HTML)
- [ ] Easy to distinguish key takeaways from supporting details

## Related

- BLI-077: PKM/brain/notes manager — notes created from well-presented output
- BLI-078: Question refinement — better questions lead to better-structured answers

## Activity Log

| Date | Time | Actor | Action | Details |
|---|---|---|---|---|
| 2026-05-29 | 08:58 PM | system | created | Brainstormed as part of EPIC-006 iterative feature ideation |

## Notes

- The existing `md-formatting.instructions.md` enforces markdown quality — this
  feature goes beyond formatting into **information architecture**
- Could be implemented as a combination of skills (output templates) and
  instructions (always-on formatting rules)
- First step might be defining 3-5 output templates (research report, tutorial,
  cheatsheet, deep-dive summary, comparison table) and routing to them automatically

## Time Tracking

| Metric | Value |
|---|---|
| Estimated effort | L |
| Actual effort | — |
| Created | 2026-05-29 |
| Started | — |
| Completed | — |
| Cycle time | — |
