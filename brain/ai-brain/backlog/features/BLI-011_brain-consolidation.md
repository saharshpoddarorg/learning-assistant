---
id: BLI-011
title: Brain consolidation — tabs, bookmarks, workspace, notes
status: todo
priority: high
type: feature
created: 2026-03-28
updated: 2026-03-28
epic: EPIC-002
parent: null
sub-items: []
origin: null
tags: [brain, consolidation, onenote, notepad, browser, bookmarks, tabs, notion, obsidian, confluence]
---

# BLI-011: Brain consolidation — tabs, bookmarks, workspace, notes

## Description

Consolidate all scattered personal knowledge sources into the brain workspace.
Currently information is fragmented across OneNote notebooks, Notepad++ session files,
browser open tabs and bookmarks, VS Code workspaces, browser tab groups, Notion pages,
Obsidian vaults, Google Docs, Confluence pages, GitHub repos, ebooks, PDFs, and this
repo itself. Build a unified ingestion pipeline to bring everything into one place.

## Acceptance Criteria

- [ ] OneNote export — extract notebooks and sections into Markdown
- [ ] Notepad++ session import — parse saved sessions, extract useful content
- [ ] Browser bookmark import — Chrome/Edge bookmarks to resource catalog
- [ ] Browser open tabs capture — snapshot current open tabs as a resource list
- [ ] Tab group export — preserve browser tab group organization
- [ ] VS Code workspace consolidation — extract workspace settings, extensions, snippets
- [ ] Notion content pull — sync relevant Notion pages to brain
- [ ] Obsidian vault merge — integrate external Obsidian vaults with brain
- [ ] Google Docs import — pull documents into library tier
- [ ] Confluence content pull — selective pages from personal/work spaces
- [ ] GitHub repo content indexing — index README, docs, wikis from starred/owned repos
- [ ] Ebook/PDF cataloging — index metadata of all ebooks and PDFs
- [ ] This repo content map — auto-generate a knowledge map of everything in this repo
- [ ] Deduplication across sources — detect same content captured in multiple places
- [ ] Priority tagging — mark which content is high-value vs. "just saved in case"

## Notes

- This is the meta-consolidation item — it depends on BLI-007 (platform connectors)
- Start with a manual audit: list all sources and estimate content volume
- Chrome bookmarks are stored in `~/.config/google-chrome/Default/Bookmarks` (JSON)
- Edge bookmarks: `%LOCALAPPDATA%\Microsoft\Edge\User Data\Default\Bookmarks`
- OneNote: Microsoft Graph API or OneNote export to `.one` files
- Consider a "knowledge map" visualization showing all sources and connections
