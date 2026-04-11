---
id: BLI-010
title: Personal Confluence — migrate office resources
status: todo
priority: medium
type: feature
created: 2026-03-28
updated: 2026-04-11
epic: EPIC-002
parent: null
sub-items: []
origin: null
tags: [confluence, migration, office, knowledge-base, atlassian, atlassian-clones, jira-alternatives]
---

# BLI-010: Personal Confluence — migrate office resources

## Description

Create a personal Confluence space (or equivalent) and migrate valuable resources,
notes, and documentation from work/office Confluence pages into a personal
knowledge base. This should preserve structure, formatting, and links while making
content portable and accessible outside the corporate environment.

Also consider building or evaluating Atlassian-like products (Jira/Confluence
clones or alternatives) for personal use — self-hosted project management and
knowledge-base tools that provide similar capabilities without corporate licensing.

## Acceptance Criteria

- [ ] Confluence export mechanism — extract pages via Confluence REST API or HTML export
- [ ] Content transformation — convert Confluence storage format to Markdown
- [ ] Personal space setup — set up a personal Confluence instance or alternative (Notion, wiki)
- [ ] Selective migration — choose which pages/spaces to migrate (not bulk dump)
- [ ] Link preservation — maintain internal cross-references between migrated pages
- [ ] Image/attachment handling — download and re-host embedded media
- [ ] Categorization — tag and organize migrated content by topic
- [ ] Integration with brain — import key resources into `brain/ai-brain/library/`
- [ ] Incremental sync — ability to pull updates from source pages periodically

## Notes

- The existing Atlassian MCP server (`mcp-servers/src/server/atlassian/`) can be leveraged
- Be mindful of corporate data policies — only migrate personal/learning content
- Confluence REST API v2 available; also consider Confluence Cloud export (HTML/PDF)
- Alternative targets: Notion (via API), Obsidian (as Markdown), or this repo's library tier
- Consider using the existing `UrlResourceHandler` for scraping public Confluence pages

## Attachments & References

| Type | Path / URL | Added | Notes |
|---|---|---|---|
| Source file | `D:\workdir\MG_FTE\notepad\personal dev\learning-assistant\gpt.txt` | 2026-04-11 | IMP-001: added Atlassian-like products context |

## Activity Log

| Date | Time | Actor | Action | Details |
|---|---|---|---|---|
| 2026-04-11 | 10:08 PM | system | merged | IMP-001: expanded description with Atlassian-like products / Jira-Confluence clones, added tags |
