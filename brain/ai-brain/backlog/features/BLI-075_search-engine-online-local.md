---
id: BLI-075
title: Search engine — online and local knowledge search
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

## Activity Log

| Date | Time | Actor | Action | Details |
|---|---|---|---|---|
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
