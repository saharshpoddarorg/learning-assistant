---
id: BLI-076
title: Web scraper — intelligent content extraction and filtering
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
estimated-effort: L
actual-effort: null
tags: [web-scraper, content-extraction, filtering, resource-category, concept-area]
origin-type: manual
import-batch: null
source-file: null
---

# BLI-076: Web scraper — intelligent content extraction and filtering

## Description

A web scraper that goes beyond raw HTML fetching — it extracts only the **useful
information** from online resources, filtering out noise (ads, navigation, sidebars).
Supports filtering by resource category (official documentation, blog posts, tutorials,
academic papers, video transcripts) and by concept area (system design, OS, DBMS,
networking, etc.) so the user gets precisely the knowledge they need.

### Future Considerations

- Rate limiting and polite crawling (respect robots.txt)
- Content summarisation after extraction (pass to LLM for distillation)
- Structured output formats (markdown, JSON, plain text)
- Batch scraping of multiple URLs from a resource list
- Integration with the vault's `ResourceCategory` and `ConceptArea` enums for
  consistent classification

## Acceptance Criteria

- [ ] Can scrape a given URL and extract meaningful content (strip noise)
- [ ] Supports filtering by resource category (official docs, blogs, etc.)
- [ ] Supports filtering by concept area (system design, OS, DBMS, etc.)
- [ ] Output is clean, readable markdown suitable for note-taking
- [ ] Handles common site structures (docs sites, blogs, GitHub READMEs)
- [ ] Graceful error handling for unreachable or blocked URLs

## Related

- BLI-075: Search engine — online and local knowledge search
- BLI-012: Enhanced web scraping & search engine (EPIC-002)
- BLI-077: PKM/brain/notes manager

## Activity Log

| Date | Time | Actor | Action | Details |
|---|---|---|---|---|
| 2026-05-29 | 08:58 PM | system | created | Brainstormed as part of EPIC-006 iterative feature ideation |

## Notes

- The existing `web-reader` skill provides basic URL reading — this feature
  builds on that foundation with intelligent extraction and filtering
- Could be implemented as an MCP tool, a skill, or standalone Java code
- Natural companion to BLI-075 (search engine delegates here for deep content)

## Time Tracking

| Metric | Value |
|---|---|
| Estimated effort | L |
| Actual effort | — |
| Created | 2026-05-29 |
| Started | — |
| Completed | — |
| Cycle time | — |
