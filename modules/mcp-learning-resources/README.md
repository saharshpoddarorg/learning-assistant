# mcp-learning-resources

Learning Resources MCP server — curated resource vault, search, and discovery tools.

## Purpose

Provides 10 MCP tools for discovering, searching, and exporting curated learning
resources across 10+ domains (DSA, system design, DevOps, Java, AI/ML, etc.).

## Key Features

- **176+ curated resources** via 20 resource providers
- **Multi-mode search** — specific, vague, and exploratory
- **Keyword-to-concept mapping** — natural language queries resolve to concept areas
- **Content scraping** — fetch and summarize web pages
- **Export** — generate resource lists in Markdown format

## Package Structure

```text
server/learningresources/
├── LearningResourcesServer.java    Server implementation
├── content/                        Content reading, summarizing, scoring
├── handler/                        10 MCP tool handlers
├── model/                          Resource metadata (ConceptArea, DifficultyLevel, etc.)
├── scraper/                        Web content extraction
├── search/                         Official docs search engine
└── vault/                          Resource providers and discovery
    ├── BuiltInResources.java       Provider registry
    ├── KeywordIndex.java           Keyword → ConceptArea mapping
    ├── LearningSearchEngine.java   Search implementation
    └── providers/                  20 domain-specific resource providers
```

## Dependencies

- `:modules:mcp-common` — config system, base server interface, utilities
- `:modules:search-engine` — search infrastructure

## Build

```bash
./gradlew :modules:mcp-learning-resources:build
```
