---
id: BLI-013
title: Book consolidation — PDFs, ebooks, cloud readers
status: todo
priority: medium
type: feature
created: 2026-03-28
updated: 2026-03-28
epic: EPIC-002
parent: null
sub-items: []
origin: null
tags: [books, pdf, ebook, kindle, adobe, github-repos, reading-list, consolidation]
---

# BLI-013: Book consolidation — PDFs, ebooks, cloud readers

## Description

Consolidate all technical books, PDFs, and reading materials scattered across local
storage, GitHub repos (own and others'), cloud ebook services (Adobe Digital Editions,
Kindle, Google Books), and physical bookmarks into a unified, searchable library
within the brain workspace. Track reading progress, extract highlights, and link
book concepts to vault learning resources.

## Acceptance Criteria

- [ ] Inventory all locally-stored PDFs — scan known directories for `.pdf` files
- [ ] Catalog personal GitHub repo books — index technical book repos (owned + starred)
- [ ] Catalog other people's GitHub book repos — curated open-source book collections
- [ ] Cloud ebook reader evaluation — compare Adobe Digital Editions, Kindle, Google Books, Calibre
- [ ] Choose primary ebook management tool — Calibre for local, cloud reader for sync
- [ ] Reading list tracker — maintain a reading backlog with status (unread / in-progress / done)
- [ ] Book metadata database — title, author, format, location, tags, rating
- [ ] Highlight/annotation extraction — pull highlights from PDF readers and ebook apps
- [ ] Brain integration — link books to vault resources via ResourceAuthor and ContentFormat
- [ ] Search across books — full-text or metadata search across the consolidated library
- [ ] Deduplication — detect same book in multiple formats or locations
- [ ] Physical book tracking — catalog physical books with ISBN and shelf location
- [ ] GitHub Actions/automation — auto-sync reading list changes to brain workspace

## Notes

- ResourceAuthor enum already has 13 authors — books by known authors should cross-reference
- ContentFormat enum has PUBLISHED_BOOK, OPEN_BOOK — use for classification
- Calibre is the industry-standard open-source ebook manager (consider as primary tool)
- O'Reilly, Manning, Pragmatic Bookshelf — check existing digital purchases
- GitHub repos: awesome-books lists, free-programming-books, etc.
- Consider Zotero for academic/research paper management alongside books
