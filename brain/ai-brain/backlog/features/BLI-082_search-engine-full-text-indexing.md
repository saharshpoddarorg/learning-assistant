---
id: BLI-082
title: Search engine — full-text indexing engine for local knowledge
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
parent: BLI-075
sub-items: []
origin: null
estimated-effort: L
actual-effort: null
tags: [search, indexing, full-text, lucene, inverted-index, brain, notes, sessions, vault]
origin-type: manual
import-batch: null
source-file: null
---

# BLI-082: Search engine — full-text indexing engine for local knowledge

## Description

Build and maintain a **full-text search index** over local knowledge — brain
notes, captured sessions, library resources, vault entries, and backlog items.
This is the core data structure that makes the search engine fast and effective.

The existing `InMemoryIndex` in `modules/search-engine/` provides a basic in-memory
index skeleton. This BLI extends it into a production-quality indexing engine that
can handle the full knowledge base with support for incremental updates, persistence,
and efficient retrieval using inverted indexes and BM25 scoring.

### Future Considerations

- Apache Lucene integration for persistent, high-performance full-text indexing
- Incremental index updates — re-index only changed files, not the entire corpus
- Index compaction and optimization for large knowledge bases
- Multi-field indexing — index title, body, tags, labels, metadata separately
- Stemming and language analysis for better recall
- Embedding-based semantic index (vector search) as a complement to keyword search
- Index snapshots for backup and restore

## Acceptance Criteria

- [ ] Index all local knowledge tiers: notes, sessions, library, vault, backlog
- [ ] Support incremental indexing — detect file changes and re-index only what changed
- [ ] Implement inverted index for efficient keyword lookup
- [ ] Integrate with existing `Bm25Scorer` for relevance scoring
- [ ] Support multi-field indexing (title, body, tags, labels, metadata)
- [ ] Index is persistent across application restarts (file-based or embedded DB)
- [ ] Index build/rebuild is performant (< 30s for the entire knowledge base)
- [ ] Extend existing `InMemoryIndex` API or provide a compatible replacement

## Related

- BLI-075: Search engine — online and local knowledge search (parent)
- BLI-080: Labelling system (sibling — index stores label data)
- BLI-081: Tagging system (sibling — index stores tag data)
- BLI-083: Metadata index and faceted search (sibling — metadata index complements full-text)
- BLI-084: Auto-classification and entity extraction (sibling — runs on indexed content)
- BLI-012: Enhanced web scraping & search engine (EPIC-002 — full-text search engine AC)

## Activity Log

| Date | Time | Actor | Action | Details |
|---|---|---|---|---|
| 2026-05-29 | 09:21 PM | system | created | Sub-item of BLI-075 — full-text indexing engine for local knowledge |

## Notes

- The existing `InMemoryIndex` and `SearchIndex` API in `modules/search-engine/`
  provide the foundation — evaluate extending vs. replacing
- `Bm25Scorer` already implements the industry-standard BM25 ranking algorithm
- `DefaultTokenizer` handles basic tokenization — consider adding stemming
- Brain notes and sessions are markdown files — indexing should parse frontmatter
  separately from body content
- Consider Apache Lucene (embedded, no server needed) for production-grade indexing
  if the in-memory approach proves insufficient for larger knowledge bases

## Time Tracking

| Metric | Value |
|---|---|
| Estimated effort | L |
| Actual effort | — |
| Created | 2026-05-29 |
| Started | — |
| Completed | — |
| Cycle time | — |
