---
id: BLI-077
title: PKM/brain/notes manager — full knowledge lifecycle
status: todo
priority: high
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
estimated-effort: XL
actual-effort: null
tags: [pkm, brain, notes, markdown, consolidation, versioning, import-export, sharing, summarize]
origin-type: manual
import-batch: null
source-file: null
---

# BLI-077: PKM/brain/notes manager — full knowledge lifecycle

## Description

A comprehensive notes and knowledge management system that covers the full
lifecycle of personal knowledge. Take notes during learning sessions with GHCP,
convert information into structured markdown files in brain/PKM, consolidate
scattered notes, version-control changes, import/export between formats and
tools, share notes with others, summarise long notes, and enhance existing
notes with additional information. This is the **core knowledge workbench**
for the learning-assistant.

### Future Considerations

- Bi-directional sync with external tools (Notion, Obsidian, Logseq)
- Note templates per domain (code analysis, learning, research, etc.)
- Automatic linking between related notes (wiki-style backlinks)
- Spaced repetition integration for notes marked as "to learn"
- Conflict resolution for concurrent edits across tools
- Collaborative editing or shared brain workspaces

## Acceptance Criteria

- [ ] Can take notes from a GHCP conversation and save to brain/PKM as markdown
- [ ] Can convert existing markdown files into structured brain notes
- [ ] Can consolidate multiple notes on the same topic into a unified note
- [ ] Version control of notes — track changes, diff, revert
- [ ] Import notes from external sources (files, clipboard, URLs)
- [ ] Export notes in shareable formats (markdown, PDF, HTML)
- [ ] Summarise long or complex notes into digestible overviews
- [ ] Enhance existing notes by appending new information from GHCP sessions

## Related

- BLI-011: Brain consolidation — tabs, bookmarks, workspace, notes (EPIC-002)
- BLI-007: Cross-platform note integration mechanism (EPIC-002)
- BLI-075: Search engine — online and local knowledge search
- BLI-079: Presentation of information — structured and distributable output

## Activity Log

| Date | Time | Actor | Action | Details |
|---|---|---|---|---|
| 2026-05-29 | 08:58 PM | system | created | Brainstormed as part of EPIC-006 iterative feature ideation |

## Notes

- The existing brain workspace (`brain/ai-brain/`) with its 6 tiers (inbox, notes,
  library, sessions, backlog, pkm) is the foundation — this feature enriches
  the tooling around it
- Overlaps with BLI-011 and BLI-007 in EPIC-002 — may merge or supersede as
  scope becomes clearer
- Could leverage MCP tools for note operations + skills for workflow guidance
- The `pkm-management` skill already defines content operations (fetch, pull,
  clone, cherry-pick, merge, stash, diff, push, consolidate) — implement these

## Time Tracking

| Metric | Value |
|---|---|
| Estimated effort | XL |
| Actual effort | — |
| Created | 2026-05-29 |
| Started | — |
| Completed | — |
| Cycle time | — |
