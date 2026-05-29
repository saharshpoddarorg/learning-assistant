---
id: BLI-081
title: Search engine — tagging system for flexible categorization
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
tags: [search, tagging, keywords, metadata, free-form, user-tags, auto-tagging]
origin-type: manual
import-batch: null
source-file: null
---

# BLI-081: Search engine — tagging system for flexible categorization

## Description

A free-form tagging system that complements the structured labelling system
(BLI-080). Tags are **user-defined or auto-generated keywords** that enable
cross-cutting discovery across the knowledge base. Unlike labels (which follow
a fixed taxonomy), tags are open-ended — users can tag content with any keyword,
and the system can auto-suggest tags based on content analysis.

Tags enable discovery patterns like "find everything I've tagged as 'interview-prep'"
or "show all content related to 'spring-boot' across notes, sessions, and vault."

### Future Considerations

- Tag normalization — merge synonyms (e.g., "k8s" and "kubernetes")
- Tag cloud / frequency analysis — visualize most-used tags
- Tag co-occurrence — discover which tags appear together frequently
- Hierarchical tags with namespace prefixes (e.g., `tech:java`, `project:task-manager`)
- Auto-tag suggestions from content using keyword extraction (TF-IDF, RAKE)
- Tag-based recommendations — "users who tagged X also tagged Y"

## Acceptance Criteria

- [ ] Support assigning free-form tags to any searchable item (notes, sessions, vault resources)
- [ ] Tags are persisted alongside indexed content
- [ ] Search engine supports filtering and boosting by tag(s)
- [ ] Auto-tagging extracts keywords from content using existing `KeywordRegistry` or TF-IDF
- [ ] Tag normalization handles case-insensitivity and basic synonyms
- [ ] Existing `TagScorer` in search-engine module is extended to use the tag system
- [ ] Provide a tag management API (add, remove, list, search, suggest)

## Related

- BLI-075: Search engine — online and local knowledge search (parent)
- BLI-080: Labelling system — structured classification (sibling — labels are structured, tags are free-form)
- BLI-082: Full-text indexing engine (sibling — index stores tag metadata)
- BLI-084: Auto-classification and entity extraction (sibling — auto-classification generates tags)
- BLI-012: Enhanced web scraping & search engine (EPIC-002 — automatic tagging AC)

## Activity Log

| Date | Time | Actor | Action | Details |
|---|---|---|---|---|
| 2026-05-29 | 09:21 PM | system | created | Sub-item of BLI-075 — tagging system for flexible categorization |

## Notes

- The existing `search-engine` module already has a `TagScorer` algorithm — the
  tagging system should feed tagged content into this scorer
- `KeywordRegistry` in the index package provides keyword → concept mapping that
  can serve as a foundation for auto-tagging
- Brain sessions already use tags in YAML frontmatter — the tagging system should
  ingest and index these existing tags
- Consider tag vocabulary governance — prevent tag explosion while keeping flexibility

## Time Tracking

| Metric | Value |
|---|---|
| Estimated effort | M |
| Actual effort | — |
| Created | 2026-05-29 |
| Started | — |
| Completed | — |
| Cycle time | — |
