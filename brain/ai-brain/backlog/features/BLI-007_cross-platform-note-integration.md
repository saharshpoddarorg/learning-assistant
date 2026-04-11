---
id: BLI-007
title: Cross-platform note integration mechanism
status: todo
priority: high
type: feature
created: 2026-03-28
updated: 2026-04-11
epic: EPIC-002
parent: null
sub-items: []
origin: null
tags: [notion, obsidian, onenote, notepad, pkm, google-drive, pdf, cross-platform, google-keep, phone-ike, word-files, google-docs]
---

# BLI-007: Cross-platform note integration mechanism

## Description

Build a unified mechanism to read from, write to, and consolidate notes across
multiple platforms and file formats: Notion, Obsidian, OneNote, Notepad++,
Notepad, Word files, PDFs, Pages files, Google Drive/Docs, Google Keep,
phone notes (Ike), browser tabs, office laptop notes, and other PKM tools.
The system should be able to extract resources, links, and structured content
from these platforms and feed them into the learning assistant's brain workspace.

## Acceptance Criteria

- [ ] Notion API connector — read pages, databases, and blocks via Notion API
- [ ] Obsidian vault reader — parse `.md` files, extract frontmatter, follow wikilinks
- [ ] Obsidian vault writer — create/update notes in an Obsidian vault from brain
- [ ] OneNote integration — read notebooks, sections, and pages via Microsoft Graph API
- [ ] File-based readers — parse `.txt` (Notepad/Notepad++), `.docx` (Word), `.pdf`, `.pages`
- [ ] Google Drive/Docs connector — read documents via Google Drive API
- [ ] Resource/link extraction — detect and catalogue URLs, references, citations from notes
- [ ] Unified search — search across all connected platforms from a single query
- [ ] Import pipeline — ingest notes into `brain/ai-brain/` with proper tier routing
- [ ] Export pipeline — push curated content from brain back to external platforms
- [ ] Deduplication — detect and merge duplicate content across platforms
- [ ] Google Keep connector — read notes, lists, and labels via Google Keep API
- [ ] Phone notes (Ike) — import notes captured on phone via Ike or similar mobile apps
- [ ] Browser tabs/bookmarks — extract and catalogue open tabs and bookmark collections
- [ ] Office laptop notes — import notes from work laptop environments

## Notes

- Consider building as MCP tools (one per platform) for modularity
- Authentication will vary per platform (OAuth for Notion/Google, file system for Obsidian)
- PDF parsing may require Apache PDFBox or similar library
- Word/DOCX parsing can use Apache POI
- Start with read-only connectors, add write support incrementally
- Privacy: all data stays local; no cloud relay except direct platform APIs

## Attachments & References

| Type | Path / URL | Added | Notes |
|---|---|---|---|
| Source file | `D:\workdir\MG_FTE\notepad\personal dev\learning-assistant\gpt.txt` | 2026-04-11 | IMP-001: added Google Keep, phone (Ike), browser tabs, office laptop |

## Activity Log

| Date | Time | Actor | Action | Details |
|---|---|---|---|---|
| 2026-04-11 | 10:08 PM | system | merged | IMP-001: added 4 new platform ACs (Google Keep, phone/Ike, browser tabs, office laptop), expanded description, added tags |
