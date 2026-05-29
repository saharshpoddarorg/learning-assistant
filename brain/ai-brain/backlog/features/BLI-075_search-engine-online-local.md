---
id: BLI-075
title: Search engine — online and local knowledge search
status: todo
priority: high
type: feature
created: 2026-05-29
updated: 2026-05-29
decomposed: 2026-05-29
started: null
completed: null
blocked-since: null
review-since: null
epic: EPIC-006
sprint: null
parent: null
sub-items: [BLI-080, BLI-081, BLI-082, BLI-083, BLI-084]
origin: null
estimated-effort: XL
actual-effort: null
tags: [search, web-search, notes-search, brain, pkm, web-scraper, delegation]
origin-type: manual
import-batch: null
source-file: null
---

# BLI-075: Search engine — online and local knowledge search

## Description

A unified search engine that can search across **two domains**: online resources
(web search, delegating to the web scraper for content extraction) and local
knowledge (brain/ai-brain notes, PKM resources, library, sessions, backlog).
The user should be able to search for a concept and get results from both the
internet and their personal knowledge base, with results ranked by relevance
and source trustworthiness.

### Future Considerations

- Semantic search (embeddings-based) vs keyword search trade-offs
- Search result caching to avoid redundant web fetches
- Federated search across multiple brain tiers (notes, sessions, library)
- Search history and saved queries
- Integration with the vault's existing keyword index for local resource discovery

## Sub-Items

| ID | Title | Priority | Status |
|---|---|---|---|
| BLI-080 | Labelling system — structured classification | medium | todo |
| BLI-081 | Tagging system — flexible categorization | medium | todo |
| BLI-082 | Full-text indexing engine — local knowledge | high | todo |
| BLI-083 | Metadata index and faceted search | medium | todo |
| BLI-084 | Auto-classification and entity extraction | medium | todo |

## Acceptance Criteria

- [ ] Can search online resources (web) for a given query
- [ ] Can search local brain/PKM/notes for a given query
- [ ] Delegates to web scraper when deeper content extraction is needed
- [ ] Results indicate source type (online vs local) clearly
- [ ] Supports filtering by resource category (official docs, blogs, tutorials, etc.)
- [ ] Supports filtering by concept area (system design, OS, DBMS, etc.)
- [ ] Returns results in a structured, readable format

## Related

- BLI-012: Enhanced web scraping & search engine (EPIC-002)
- BLI-076: Web scraper — intelligent content extraction
- BLI-080: Labelling system — structured classification (sub-item)
- BLI-081: Tagging system — flexible categorization (sub-item)
- BLI-082: Full-text indexing engine — local knowledge (sub-item)
- BLI-083: Metadata index and faceted search (sub-item)
- BLI-084: Auto-classification and entity extraction (sub-item)

## Activity Log

| Date | Time | Actor | Action | Details |
|---|---|---|---|---|
| 2026-05-29 | 09:21 PM | system | decomposed | Added 5 sub-items: BLI-080 (labelling), BLI-081 (tagging), BLI-082 (indexing), BLI-083 (faceted search), BLI-084 (auto-classification) |
| 2026-05-29 | 08:58 PM | system | created | Brainstormed as part of EPIC-006 iterative feature ideation |

## Notes

- Existing `modules/search-engine/` provides a generic search library — evaluate
  whether to extend it or build on top of it
- BLI-012 in EPIC-002 covers similar ground — this BLI may merge with or supersede
  it once scoped further
- The web scraper (BLI-076) is a natural dependency/companion

## Time Tracking

| Metric | Value |
|---|---|
| Estimated effort | XL |
| Actual effort | — |
| Created | 2026-05-29 |
| Started | — |
| Completed | — |
| Cycle time | — |
