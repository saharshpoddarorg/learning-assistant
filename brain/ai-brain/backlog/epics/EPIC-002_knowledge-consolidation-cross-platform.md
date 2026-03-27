---
id: EPIC-002
title: Knowledge consolidation & cross-platform brain
status: draft
priority: high
created: 2026-03-28
updated: 2026-03-28
tags: [knowledge-management, cross-platform, brain, consolidation, note-taking, scraping]
---

# EPIC-002: Knowledge consolidation & cross-platform brain

## Vision

Build a unified knowledge consolidation layer that connects the learning assistant
to all personal knowledge sources — Notion, Obsidian, OneNote, Notepad++, browser
bookmarks/tabs, Confluence, Google Drive, PDFs, ebooks, and this repo itself. Users
should be able to read, write, search, and consolidate notes across platforms, manage
books and resources from a single system, and leverage a full-fledged web scraping and
search engine to discover and index new content. The brain workspace becomes the single
source of truth, with connectors pulling from and pushing to external platforms.

## Items

| ID | Title | Status | Priority |
|---|---|---|---|
| BLI-007 | Cross-platform note integration mechanism | todo | high |
| BLI-010 | Personal Confluence — migrate office resources | todo | medium |
| BLI-011 | Brain consolidation — tabs, bookmarks, workspace, notes | todo | high |
| BLI-012 | Enhanced web scraping & search engine | todo | high |
| BLI-013 | Book consolidation — PDFs, ebooks, cloud readers | todo | medium |

## Open Questions

- [ ] Which platform APIs are available and free for personal use? (Notion, OneNote, Google)
- [ ] Should connectors be MCP tools, standalone scripts, or Java modules?
- [ ] How do we handle conflicting content across platforms (merge strategy)?
- [ ] What is the indexing/search strategy for ingested content?
- [ ] How do we handle authentication for each platform?
- [ ] What is the privacy/security model for synced personal data?

## Notes

- Current brain workspace: `brain/ai-brain/` (inbox/notes/library/sessions tiers)
- Current scraping: `mcp-servers/src/server/learningresources/handler/UrlResourceHandler.java`
- Current search engine: `search-engine/src/search/`
- Related to EPIC-001 (learning resources system) — this epic covers the broader knowledge layer
