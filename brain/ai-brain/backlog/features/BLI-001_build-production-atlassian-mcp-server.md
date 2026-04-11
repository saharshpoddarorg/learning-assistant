---
id: BLI-001
title: Build production Atlassian MCP server
status: todo
priority: high
type: feature
created: 2026-03-26
updated: 2026-04-11
origin: null
tags: [mcp, atlassian, jira, confluence]
---

# BLI-001: Build production Atlassian MCP server

## Description

Create a proper, production-quality Atlassian MCP server — not a stub or prototype.
The current Atlassian server scaffolding exists but needs to be built out into a
fully functional MCP server with real Jira and Confluence integration, proper auth
(OAuth 2.0), error handling, and comprehensive tool coverage.

## Acceptance Criteria

- [ ] OAuth 2.0 authentication flow working for Atlassian Cloud
- [ ] Jira tools: search issues, get issue details, create/update issues, transitions
- [ ] Confluence tools: search pages, get page content, create/update pages
- [ ] Proper error handling with descriptive messages
- [ ] Configuration via `mcp-config.local.properties`
- [ ] All tools registered and discoverable via `list-tools`
- [ ] README documentation with setup instructions
- [ ] Build passes (`.\mcp-servers\build.ps1`)

## Notes

- Current scaffolding: `mcp-servers/src/server/atlassian/`
- Scripts exist: `mcp-servers/scripts/server-specific/` has some Atlassian setup
- Auth helpers: `mcp-servers/scripts/common/auth/` has OAuth flow scripts
- Config: `mcp-servers/user-config/servers/atlassian/` and `atlassian-v2/`

## Attachments & References

| Type | Path / URL | Added | Notes |
|---|---|---|---|
| Source file | `D:\workdir\MG_FTE\notepad\personal dev\learning-assistant\gpt.txt` | 2026-04-11 | IMP-001: "atlassian" mentioned under mcp-servers section |

## Activity Log

| Date | Time | Actor | Action | Details |
|---|---|---|---|---|
| 2026-04-11 | 10:08 PM | system | noted | IMP-001: "atlassian" mentioned in source file — already covered by this BLI, no changes needed |
