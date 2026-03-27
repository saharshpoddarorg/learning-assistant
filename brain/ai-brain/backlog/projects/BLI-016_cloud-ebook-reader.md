---
id: BLI-016
title: Cloud & ebook reader
status: todo
priority: low
type: feature
created: 2026-03-28
updated: 2026-03-28
epic: EPIC-003
parent: null
sub-items: []
origin: null
tags: [project:ebook-reader, cloud, ebook, pdf, epub, reading, sync, annotations]
---

# BLI-016: Cloud & ebook reader

## Description

Build or adopt a cloud-synced ebook reader that supports PDF, EPUB, and MOBI formats
across devices (desktop, tablet, phone). Key features include cross-device reading
progress sync, highlighting and annotation, bookmarking, and a personal cloud library.
Evaluate build-vs-buy: building a custom reader vs. using an existing solution
(Calibre-Web, Kavita, Komga) with customizations.

## Acceptance Criteria

- [ ] Format support — read PDF, EPUB, MOBI, and plain text files
- [ ] Cross-device sync — reading position, bookmarks, highlights synced across devices
- [ ] Cloud storage backend — self-hosted (NAS, cloud VM) or cloud provider (S3, GDrive)
- [ ] Library management — organize books by shelves, tags, series, author
- [ ] Highlighting & annotations — highlight text and add notes, export as Markdown
- [ ] Search within books — full-text search across all books in the library
- [ ] Reading progress — track completion percentage, time spent per book
- [ ] Offline mode — download books for offline reading on any device
- [ ] Metadata auto-fetch — pull book metadata from OpenLibrary, Google Books, ISBN lookup
- [ ] OPDS catalog — expose library as OPDS feed for compatible reader apps
- [ ] Build-vs-buy evaluation — compare Calibre-Web, Kavita, Komga, custom solution
- [ ] Web reader UI — browser-based reader with adjustable fonts, themes, night mode
- [ ] Import pipeline — bulk import from local folders, cloud drives, email attachments

## Notes

- Build-vs-buy is the first decision:
  - Calibre-Web: mature, Python, Docker-based, good UI
  - Kavita: modern .NET, fast, great OPDS support
  - Komga: Kotlin/Spring Boot, originally for comics but supports books
  - Custom: full control but high effort
- OPDS (Open Publication Distribution System) enables interop with KOReader, Moon+ Reader, etc.
- If building custom: consider pdf.js (Mozilla) for PDF rendering, epub.js for EPUB
- Annotation export as Markdown — integrate with brain/ai-brain/ notes workflow
- Related to BLI-013 (book consolidation) — this is the reader, that is the catalog
