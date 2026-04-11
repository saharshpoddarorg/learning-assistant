---
id: BLI-012
title: Enhanced web scraping & search engine
status: todo
priority: high
type: feature
created: 2026-03-28
updated: 2026-04-11
epic: EPIC-002
parent: null
sub-items: []
origin: null
tags: [web-scraping, search-engine, tagging, indexing, ghcp, crawler, cross-linking, labelling, enums, skills-prompts]
---

# BLI-012: Enhanced web scraping & search engine

## Description

Evolve the existing web scraping mechanism (`UrlResourceHandler`) and search engine
(`search-engine/`) into a full-fledged content discovery and indexing system. Add
advanced features like automatic tagging, content categorization, link crawling,
relevance scoring, and tight integration with GitHub Copilot for AI-assisted
research and resource discovery.

## Acceptance Criteria

- [ ] Multi-page crawling — follow links from a seed URL to discover related pages
- [ ] Content extraction improvements — better parsing for diverse HTML structures
- [ ] Automatic tagging — extract keywords, topics, and entities from scraped content
- [ ] Auto-categorization — map scraped resources to existing ConceptArea/ResourceCategory
- [ ] Full-text search engine — index scraped content for keyword search
- [ ] Relevance ranking — score results by freshness, authority, topic match
- [ ] GHCP integration — use scraping results as context for Copilot conversations
- [ ] Scheduled scraping — periodically re-scrape resources to check for updates
- [ ] Scrape history — track what was scraped when, detect content changes
- [ ] Rate limiting & politeness — respect `robots.txt`, configurable delays
- [ ] Export formats — export scraped data as JSON, CSV, or Markdown
- [ ] Bookmark-to-resource pipeline — turn browser bookmarks into vault resources
- [ ] Search API as MCP tool — expose search capabilities to Copilot
- [ ] Content fingerprinting — detect duplicate or near-duplicate content
- [ ] Cross-linking system — bidirectional links between related resources, notes, and backlog items
- [ ] Labelling/enum expansion — enrich tagging with formalized enums for resource types, categories, and concept areas
- [ ] Skills/prompts auto-creation — when scraping a new domain, suggest creating matching `.prompt.md` and `SKILL.md` files

## Notes

- Current scraping: `UrlResourceHandler.java` — handles single-page scraping
- Current search engine: `search-engine/src/search/` — basic engine skeleton
- Consider Apache Lucene or embedded Elasticsearch for full-text indexing
- Jsoup library for better HTML parsing (currently using regex-based extraction)
- The search engine module already exists but needs significant enhancement
- Consider headless browser (Playwright/Selenium) for JavaScript-rendered pages

## Attachments & References

| Type | Path / URL | Added | Notes |
|---|---|---|---|
| Source file | `D:\workdir\MG_FTE\notepad\personal dev\learning-assistant\gpt.txt` | 2026-04-11 | IMP-001: added cross-linking, labelling, enums, skills-prompts |

## Activity Log

| Date | Time | Actor | Action | Details |
|---|---|---|---|---|
| 2026-04-11 | 10:08 PM | system | merged | IMP-001: added 3 new ACs (cross-linking, labelling/enums, skills/prompts auto-creation), added tags |
