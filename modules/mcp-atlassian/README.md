# mcp-atlassian

Atlassian MCP server — unified gateway for Jira, Confluence, and Bitbucket.

## Purpose

Provides 27 MCP tools for interacting with Atlassian products. Supports both
Cloud and Data Center deployments with multiple authentication methods.

## Key Features

- **27 MCP tools** across Jira, Confluence, and Bitbucket
- **v1 + v2 architecture** — v1 (legacy), v2 (current) with seamless version switching
- **Multi-auth** — PAT tokens, OAuth 2.0, API tokens
- **Cross-product search** — unified search across all Atlassian products

## Package Structure

```text
server/atlassian/
├── AtlassianServerFactory.java     Factory — creates v1 or v2 server
├── AtlassianServerVersion.java     Version enum
├── common/                         Shared Atlassian utilities (JSON parsing)
├── v1/                             Version 1 (legacy)
│   ├── AtlassianServerV1.java
│   ├── client/                     HTTP clients (Jira, Confluence, Bitbucket)
│   ├── config/                     Config loading
│   ├── formatter/                  Output formatting
│   ├── handler/                    Tool handlers
│   └── model/                      Data models (issues, pages, PRs)
└── v2/                             Version 2 (current)
    ├── AtlassianServerV2.java
    ├── auth/                       Auth providers (OAuth, Token, PAT)
    ├── client/                     HTTP clients (V2 enriched)
    ├── config/                     Config loading (V2)
    ├── handler/                    Tool handlers (27 tools)
    └── model/                      Enriched data models
```

## Dependencies

- `:modules:mcp-common` — config system, base server interface, utilities
- `:modules:search-engine` — search infrastructure

## Build

```bash
./gradlew :modules:mcp-atlassian:build
```
