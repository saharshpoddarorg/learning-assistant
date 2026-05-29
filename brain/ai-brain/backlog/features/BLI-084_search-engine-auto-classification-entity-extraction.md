---
id: BLI-084
title: Search engine — auto-classification and entity extraction
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
estimated-effort: L
actual-effort: null
tags: [search, auto-classification, entity-extraction, nlp, keyword-extraction, taxonomy-mapping]
origin-type: manual
import-batch: null
source-file: null
---

# BLI-084: Search engine — auto-classification and entity extraction

## Description

Automatically classify and tag new content as it enters the search engine's
knowledge base. The system extracts **entities** (technologies, authors, concepts,
tools) and **topics** from text, then maps them to existing label taxonomies
(BLI-080) and generates tags (BLI-081). This reduces the manual effort of
organizing knowledge and ensures consistent classification across the corpus.

Auto-classification runs when content is first indexed and can be re-run when
taxonomies evolve or extraction algorithms improve.

### Future Considerations

- LLM-assisted classification — use Copilot/GPT for nuanced topic detection
- Confidence scoring — indicate how certain the auto-classification is
- Human-in-the-loop — suggest classifications for user approval before applying
- Custom entity types — project names, internal tools, team members
- Learning from corrections — improve future classifications based on user overrides
- Multi-language support — extract entities from content in different natural languages

## Acceptance Criteria

- [ ] Extract technology entities from content (e.g., "Java", "Spring Boot", "Docker")
- [ ] Extract author/person entities from content
- [ ] Extract concept/topic entities and map to ConceptArea enum values
- [ ] Map extracted entities to labels from the labelling system (BLI-080)
- [ ] Generate free-form tags from extracted keywords (feed into BLI-081 tagging system)
- [ ] Auto-classification runs on new content at indexing time
- [ ] Leverage existing `KeywordQueryClassifier` and `KeywordRegistry` as extraction foundation
- [ ] Classification results are stored alongside indexed content for search/filtering

## Related

- BLI-075: Search engine — online and local knowledge search (parent)
- BLI-080: Labelling system (sibling — auto-classification assigns labels)
- BLI-081: Tagging system (sibling — auto-classification generates tags)
- BLI-082: Full-text indexing engine (sibling — extraction runs at index time)
- BLI-083: Metadata index and faceted search (sibling — extracted metadata feeds facets)
- BLI-012: Enhanced web scraping & search engine (EPIC-002 — auto-tagging and auto-categorization ACs)

## Activity Log

| Date | Time | Actor | Action | Details |
|---|---|---|---|---|
| 2026-05-29 | 09:21 PM | system | created | Sub-item of BLI-075 — auto-classification and entity extraction |

## Notes

- The existing `KeywordQueryClassifier` classifies search queries into `SearchMode`
  values — a similar approach can classify content into categories
- `KeywordRegistry` maps keywords to concepts — this can be reversed to extract
  concepts from keyword-rich content
- Consider TF-IDF or RAKE algorithms for keyword extraction as a first pass,
  with LLM-assisted classification as an enhancement layer
- The vault's `KeywordIndex.java` already has comprehensive keyword → ConceptArea
  and keyword → ResourceCategory mappings — reuse these for classification

## Time Tracking

| Metric | Value |
|---|---|
| Estimated effort | L |
| Actual effort | — |
| Created | 2026-05-29 |
| Started | — |
| Completed | — |
| Cycle time | — |
