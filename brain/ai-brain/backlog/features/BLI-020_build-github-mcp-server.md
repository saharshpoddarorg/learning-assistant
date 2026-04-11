---
id: BLI-020
title: Build GitHub MCP server
status: todo
priority: high
type: feature
created: 2026-04-11
updated: 2026-04-11
started: null
completed: null
blocked-since: null
review-since: null
epic: null
sprint: null
parent: null
sub-items: []
origin: null
estimated-effort: L
actual-effort: null
tags: [mcp, github, api, integration, automation]
origin-type: file-import
import-batch: IMP-001
source-file: "D:\\workdir\\MG_FTE\\notepad\\personal dev\\learning-assistant\\gpt.txt"
---

# BLI-020: Build GitHub MCP server

## Description

Build a GitHub MCP server alongside the existing Atlassian MCP server. This server
should expose GitHub API capabilities as MCP tools — repos, issues, PRs, actions,
search, and user management. Scaffolding and scripts already exist at
`mcp-servers/scripts/server-specific/github/`. The server would allow Copilot to
interact with GitHub repositories, issues, and pull requests programmatically.

## Future Considerations

- GitHub Actions workflow management (trigger, cancel, re-run)
- GitHub Packages and release management
- Integration with the existing Atlassian server for cross-platform project tracking
- Webhook support for real-time event handling

## Sub-Items

| ID | Title | Status | Effort |
|---|---|---|---|

## Acceptance Criteria

- [ ] GitHub Personal Access Token or OAuth authentication working
- [ ] Repository tools — list repos, get repo details, create repo
- [ ] Issue tools — search, get, create, update, close issues
- [ ] PR tools — list PRs, get PR details, create PR, merge PR, review
- [ ] Search tools — search repositories, code, issues, users
- [ ] All tools registered and discoverable via `list-tools`
- [ ] Configuration via `mcp-config.local.properties`
- [ ] Build passes (`.\mcp-servers\build.ps1`)

## Attachments & References

| Type | Path / URL | Added | Notes |
|---|---|---|---|
| Source file | `D:\workdir\MG_FTE\notepad\personal dev\learning-assistant\gpt.txt` | 2026-04-11 | Extracted from file import IMP-001 |
| Existing scripts | `mcp-servers/scripts/server-specific/github/` | 2026-04-11 | GitHub server setup scaffolding already exists |

## Related

- [BLI-001](../features/BLI-001_build-production-atlassian-mcp-server.md) — sibling MCP server

## Activity Log

| Date | Time | Actor | Action | Details |
|---|---|---|---|---|
| 2026-04-11 | 10:08 PM | system | created | Created via /read-file-jot from IMP-001 |

## Notes

- Raw input: "github" listed under "// mcp servers" section in source file
- GitHub REST API v3 and GraphQL API v4 both available
- Consider using existing `mcp-servers/scripts/server-specific/github/` setup scripts
- Auth: GitHub PAT (simplest) or GitHub App (for org-level access)
