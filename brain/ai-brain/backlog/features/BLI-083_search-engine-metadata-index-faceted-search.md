---
id: BLI-083
title: Search engine — metadata index and faceted search
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
parent: BLI-075
sub-items: []
origin: null
estimated-effort: M
actual-effort: null
tags: [search, metadata, faceted-search, filtering, catalog, structured-query]
origin-type: manual
import-batch: null
source-file: null
---

# BLI-083: Search engine — metadata index and faceted search

## Description

A **structured metadata index** that enables faceted search — filtering results
by structured attributes like author, date range, resource type, category,
concept area, difficulty level, source (online vs local), and content format.
This complements the full-text index (BLI-082) by providing precise, structured
query capabilities on top of keyword search.

Faceted search allows queries like: "Find all official documentation about
distributed systems published after 2024, difficulty: intermediate, free resources
only" — combining keyword search with multiple metadata filters.

### Future Considerations

- Dynamic facet discovery — automatically detect available facets from indexed content
- Facet counts — show how many results exist for each facet value (like e-commerce filters)
- Saved filters / search presets — remember frequently used facet combinations
- Date-range facets with relative ranges ("last 30 days", "this year")
- Nested facets — drill down from broad to specific (e.g., Java → Spring → Spring Boot)

## Acceptance Criteria

- [ ] Build a structured metadata catalog from indexed content frontmatter and properties
- [ ] Support faceted filtering by: author, date range, type, category, concept area, difficulty
- [ ] Support combining multiple facets with AND logic
- [ ] Faceted search integrates with full-text search (filters narrow keyword results)
- [ ] Extend existing `FilterChain` and `SearchFilter` APIs for metadata-based filtering
- [ ] Return facet counts alongside search results (how many items per facet value)
- [ ] Metadata index updates incrementally when content changes

## Related

- BLI-075: Search engine — online and local knowledge search (parent)
- BLI-080: Labelling system (sibling — labels are a type of facet)
- BLI-081: Tagging system (sibling — tags are a type of facet)
- BLI-082: Full-text indexing engine (sibling — full-text index + metadata index = complete search)
- BLI-084: Auto-classification and entity extraction (sibling — auto-classification populates metadata)

## Activity Log

| Date | Time | Actor | Action | Details |
|---|---|---|---|---|
| 2026-05-29 | 09:21 PM | system | created | Sub-item of BLI-075 — metadata index and faceted search |

## Notes

- The existing `FilterChain` and `SearchFilter` APIs in `modules/search-engine/`
  provide the extension point for faceted filtering
- Brain sessions and vault resources already have rich frontmatter (date, tags,
  category, difficulty, etc.) — the metadata index should ingest this directly
- Consider whether facets should be pre-computed (fast lookup, stale risk) or
  computed on query (always fresh, slower for large datasets)
- The `ConfigurableSearchEngine` already supports chaining filters — faceted
  search adds structured filter generation from user-facing facet selections

## Time Tracking

| Metric | Value |
|---|---|
| Estimated effort | M |
| Actual effort | — |
| Created | 2026-05-29 |
| Started | — |
| Completed | — |
| Cycle time | — |
