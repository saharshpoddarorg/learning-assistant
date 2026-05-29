---
id: BLI-080
title: Search engine — labelling system for structured classification
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
tags: [search, labelling, classification, taxonomy, enums, resource-category, concept-area]
origin-type: manual
import-batch: null
source-file: null
---

# BLI-080: Search engine — labelling system for structured classification

## Description

A formalized labelling system for the search engine that applies **predefined
taxonomy labels** to searchable content. Labels are structured, enum-like
categories used for precise filtering and classification — not free-form like
tags. The system maps content to existing domain taxonomies (ResourceCategory,
ConceptArea, DifficultyLevel, ContentFormat) and supports creating new label
dimensions as the knowledge base grows.

Labels enable users to narrow search results using structured facets like
"show me only official documentation about system design at intermediate level."

### Future Considerations

- Multi-label support — content can belong to multiple categories simultaneously
- Label hierarchy — parent/child relationships (e.g., Java → JVM → GC)
- Label suggestions — recommend labels based on content analysis
- Label analytics — track label distribution to identify knowledge gaps
- Integration with vault's existing `ResourceCategory` and `ConceptArea` enums

## Acceptance Criteria

- [ ] Define a label taxonomy model with dimensions (category, concept, difficulty, format)
- [ ] Support assigning multiple labels per dimension to any searchable item
- [ ] Labels are persisted alongside indexed content
- [ ] Search engine supports filtering by label dimension(s)
- [ ] Labels integrate with existing vault enums (ResourceCategory, ConceptArea, etc.)
- [ ] Provide a label management API (add, remove, list, validate)

## Related

- BLI-075: Search engine — online and local knowledge search (parent)
- BLI-081: Tagging system — free-form metadata (sibling — labels are structured, tags are free-form)
- BLI-082: Full-text indexing engine (sibling — indexing stores label metadata)
- BLI-083: Metadata index and faceted search (sibling — faceted search queries labels)
- BLI-084: Auto-classification and entity extraction (sibling — auto-classification assigns labels)
- BLI-012: Enhanced web scraping & search engine (EPIC-002 — labelling/enum expansion AC)

## Activity Log

| Date | Time | Actor | Action | Details |
|---|---|---|---|---|
| 2026-05-29 | 09:21 PM | system | created | Sub-item of BLI-075 — labelling system for structured classification |

## Notes

- The existing `modules/search-engine/` has `FilterChain` and `SearchFilter` APIs
  that can be extended for label-based filtering
- Vault already uses `ResourceCategory`, `ConceptArea`, `DifficultyLevel` enums —
  the labelling system should reuse and extend these, not duplicate them
- BLI-012 (EPIC-002) has a related AC for "labelling/enum expansion" — this BLI
  provides the concrete implementation plan

## Time Tracking

| Metric | Value |
|---|---|
| Estimated effort | M |
| Actual effort | — |
| Created | 2026-05-29 |
| Started | — |
| Completed | — |
| Cycle time | — |
