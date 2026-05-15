# Atlassian MCP Server

> **Unified MCP server for Jira, Confluence, and Bitbucket — issue tracking, documentation, and code collaboration through a single AI-accessible interface.**

---

## Overview

This MCP server provides a **unified gateway** to Atlassian's core developer tools:

- **Jira** — create, search, update, and transition issues; manage sprints and boards
- **Confluence** — search, read, create, and update pages and spaces
- **Bitbucket** — browse repos, pull requests, commits, and code search

**What it does:**

- **Issue Management** — search, create, update, and transition Jira issues with JQL support
- **Sprint Tracking** — view active sprints, board summaries, and backlog items
- **Documentation** — search and read Confluence pages with CQL; create and update content
- **Code Collaboration** — browse Bitbucket repositories, review pull requests, view diffs
- **Cross-Product Linking** — follow links between Jira issues, Confluence pages, and Bitbucket PRs
- **Formatting** — clean, readable output suitable for AI assistant consumption

---

## Quick Start

```bash
# From the mcp-servers directory:

# Compile
javac -d out src/server/atlassian/**/*.java src/server/atlassian/*.java

# Run demo (no stdin needed)
java -cp out server.atlassian.AtlassianServer --demo

# List available tools
java -cp out server.atlassian.AtlassianServer --list-tools

# Start as MCP server (STDIO transport)
java -cp out server.atlassian.AtlassianServer
```

---

## Available Tools

### Jira Tools (11)

| Tool | Description | Required Args |
| ------ | ------------- | --------------- |
| `jira_search_issues` | Search issues using JQL or free text | `query` |
| `jira_get_issue` | Get full details of a specific issue | `issueKey` |
| `jira_create_issue` | Create a new issue | `projectKey`, `summary`, `issueType` |
| `jira_update_issue` | Update fields on an existing issue | `issueKey` |
| `jira_transition_issue` | Move issue to a new status | `issueKey`, `transitionId` |
| `jira_list_projects` | List accessible Jira projects | _(none)_ |
| `jira_get_sprint` | Get active sprint details for a board | `boardId` |
| `jira_add_comment` | Add a comment to an issue | `issueKey`, `comment` |
| `jira_get_comments` | List comments on an issue | `issueKey` |
| `jira_assign_issue` | Assign an issue to a user (omit accountId to unassign) | `issueKey` |
| `jira_get_sprint_issues` | Get all issues in a sprint | `boardId` |

### Confluence Tools (7)

| Tool | Description | Required Args |
| ------ | ------------- | --------------- |
| `confluence_search` | Search pages using CQL or free text | `query` |
| `confluence_get_page` | Get full content of a page | `pageId` |
| `confluence_create_page` | Create a new page in a space | `spaceKey`, `title`, `content` |
| `confluence_update_page` | Update an existing page | `pageId`, `title`, `content`, `version` |
| `confluence_list_spaces` | List accessible Confluence spaces | _(none)_ |
| `confluence_get_page_children` | List child pages of a page | `pageId` |
| `confluence_delete_page` | Delete a page permanently | `pageId` |

### Bitbucket Tools (8)

| Tool | Description | Required Args |
| ------ | ------------- | --------------- |
| `bitbucket_list_repos` | List repositories in a workspace | `workspace` |
| `bitbucket_get_repo` | Get repository details | `workspace`, `repoSlug` |
| `bitbucket_list_pull_requests` | List PRs for a repository | `workspace`, `repoSlug` |
| `bitbucket_get_pull_request` | Get full PR details with diff | `workspace`, `repoSlug`, `pullRequestId` |
| `bitbucket_search_code` | Search code across repositories | `workspace`, `query` |
| `bitbucket_create_pull_request` | Open a new pull request | `workspace`, `repoSlug`, `title`, `sourceBranch`, `targetBranch` |
| `bitbucket_list_branches` | List branches in a repository | `workspace`, `repoSlug` |
| `bitbucket_get_commits` | Get recent commits (optional branch filter) | `workspace`, `repoSlug` |

### Cross-product Tools (1)

| Tool | Description | Required Args |
| ------ | ------------- | --------------- |
| `atlassian_unified_search` | Search Jira + Confluence + Bitbucket in one call | `query` |

---

## Architecture

```text
server.atlassian
├── AtlassianServer.java              — STDIO server; JSON-RPC 2.0 dispatcher
├── model/                             — Shared enums & records
│   ├── AtlassianProduct.java          — Product enum (JIRA, CONFLUENCE, BITBUCKET)
│   ├── AtlassianCredentials.java      — Auth credentials record
│   ├── ConnectionConfig.java          — Base URL, auth, timeout settings
│   └── ToolResponse.java             — Standardized tool response wrapper
├── client/                            — REST API clients
│   ├── AtlassianRestClient.java       — Shared HTTP client (GET/POST/PUT/DELETE)
│   ├── JiraClient.java                — Jira REST API v3 client (11 methods)
│   ├── ConfluenceClient.java          — Confluence REST API v2 client (8 methods)
│   └── BitbucketClient.java           — Bitbucket REST API 2.0 client (9 methods)
├── handler/                           — MCP tool dispatch & response formatting
│   ├── ToolHandler.java               — Central router for all 27 tools
│   ├── JiraHandler.java               — Jira tool implementations + markdown formatters
│   ├── ConfluenceHandler.java         — Confluence tool implementations + markdown formatters
│   ├── BitbucketHandler.java          — Bitbucket tool implementations + markdown formatters
│   └── UnifiedSearchHandler.java      — Cross-product unified search
├── util/                              — Shared utilities
│   └── JsonExtractor.java             — Lightweight JSON parser (no external deps)
└── formatter/                         — Legacy formatter stubs (superseded by handler formatters)
    ├── IssueFormatter.java
    ├── PageFormatter.java
    └── PullRequestFormatter.java
```

---

## Authentication

The server supports two authentication methods:

1. **API Token** (Atlassian Cloud) — email + API token from [id.atlassian.com](https://id.atlassian.com/manage-profile/security/api-tokens)
2. **Personal Access Token** (Bitbucket / Data Center) — PAT from your Atlassian product settings

Configure credentials via (highest precedence wins):

1. **Environment variables:** `ATLASSIAN_TOKEN`, `ATLASSIAN_EMAIL`, `ATLASSIAN_JIRA_URL`, etc.
2. **Local config:** `user-config/servers/atlassian/atlassian-config.local.properties` (gitignored — copy from `.example.properties`)
3. **Base config:** `user-config/servers/atlassian/atlassian-config.properties` (committed defaults, no secrets)

---

## Configuration

Copy the example file and fill in your credentials:

```bash
cd mcp-servers/user-config/servers/atlassian
cp atlassian-config.local.example.properties atlassian-config.local.properties
# Edit atlassian-config.local.properties with your URLs and API token
```

Key settings in `atlassian-config.local.properties`:

```properties
# Instance identity
atlassian.instance.name=work-cloud
atlassian.variant=cloud               # cloud | data_center | server | custom

# Per-product base URLs
atlassian.jira.baseUrl=https://your-domain.atlassian.net
atlassian.confluence.baseUrl=https://your-domain.atlassian.net/wiki
atlassian.bitbucket.baseUrl=https://api.bitbucket.org

# Authentication (Cloud: api_token; Data Center: pat)
atlassian.auth.type=api_token
atlassian.auth.email=your.email@example.com
atlassian.auth.token=YOUR_API_TOKEN_HERE

# Enable/disable products
atlassian.product.jira.enabled=true
atlassian.product.confluence.enabled=true
atlassian.product.bitbucket.enabled=false
```

Then enable the server in `user-config/mcp-config.properties`:

```properties
server.atlassian.enabled=true
```

---

## Iterative Build Plan

This server is being built iteratively:

- [x] **Phase 1** — Project scaffolding, model enums, package structure
- [x] **Phase 2** — Jira client + handler (search, get, create, update, transition)
- [x] **Phase 3** — Confluence client + handler (search, get, create, update)
- [x] **Phase 4** — Bitbucket client + handler (repos, PRs, code search)
- [x] **Phase 5** — Cross-product linking and unified search
- [x] **Phase 6** — Response formatting and export (JsonExtractor + markdown formatters in handlers)
- [x] **Phase 7** — Extended tools: add/get comments, assign, sprint issues, page children, delete page, create PR, list branches, commits

---

## API References

- [Jira REST API v3](https://developer.atlassian.com/cloud/jira/platform/rest/v3/)
- [Confluence REST API v2](https://developer.atlassian.com/cloud/confluence/rest/v2/)
- [Bitbucket REST API 2.0](https://developer.atlassian.com/cloud/bitbucket/rest/intro/)
