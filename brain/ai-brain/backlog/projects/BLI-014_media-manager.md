---
id: BLI-014
title: Media manager — YouTube, Spotify consolidation
status: todo
priority: low
type: feature
created: 2026-03-28
updated: 2026-03-28
epic: EPIC-003
parent: null
sub-items: []
origin: null
tags: [project:media-manager, media, youtube, spotify, podcasts, playlists, consolidation]
---

# BLI-014: Media manager — YouTube, Spotify consolidation

## Description

Build a personal media manager application that consolidates content from YouTube
(playlists, liked videos, watch history, subscriptions), Spotify (playlists, liked
songs, podcasts), and other media sources into a unified dashboard. Enable tagging,
categorization, search, and cross-referencing of media across platforms.

## Acceptance Criteria

- [ ] YouTube API integration — fetch playlists, liked videos, subscriptions, watch history
- [ ] Spotify API integration — fetch playlists, liked songs, followed artists, podcasts
- [ ] Unified media data model — normalize videos, songs, podcasts into common schema
- [ ] Tagging system — add custom tags across media types (learning, entertainment, etc.)
- [ ] Categorized views — filter by platform, tag, media type, date added
- [ ] Cross-platform playlists — create collections mixing YouTube videos and Spotify tracks
- [ ] Search across platforms — keyword search across all consolidated media
- [ ] Podcast tracker — track listening progress across podcast episodes
- [ ] Recommendation engine — suggest related media based on tags and history
- [ ] Export/backup — export consolidated library as JSON, CSV, or OPML
- [ ] Offline metadata — keep metadata locally even when APIs are unreachable
- [ ] Rate and review — personal ratings and notes on media items
- [ ] Learning content extraction — identify technical learning videos and link to vault

## Notes

- YouTube Data API v3 — quota limited (10,000 units/day), plan calls carefully
- Spotify Web API — OAuth 2.0 PKCE flow for desktop apps
- Consider RSS feeds for podcast tracking (standard format)
- Tech stack candidates: Java + Spring Boot backend, React frontend
- Alternative: CLI-first approach, add UI later
- Could integrate with Plex/Jellyfin for self-hosted media management
- Privacy: store API tokens securely, never commit credentials
