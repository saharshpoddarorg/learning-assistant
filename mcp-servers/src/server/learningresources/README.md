# Learning Resources MCP Server

> **A curated vault of worldwide learning resources with web scraping & summarization.**

---

## Overview

This MCP server acts as a **learning assistant** — it maintains a curated library of famous,
high-quality learning resources and can scrape, extract, and summarize content from any URL
on the internet.

**What it does:**

- **Vault** — 30+ pre-loaded resources: official docs, tutorials, blogs, books, and interactive
  tools from Oracle, Mozilla, Baeldung, Spring, OWASP, Docker, and more
- **Search** — full-text search with filters by category, type, difficulty, and tags
- **Scrape** — fetches any URL, strips HTML, extracts meaningful text content
- **Summarize** — generates condensed summaries with word count, reading time, and difficulty
- **Read** — presents full extracted content in a clean, readable format
- **Extend** — add custom resources to the vault at runtime

---

## Quick Start

```bash
# From the mcp-servers directory:

# Compile
javac -d out src/server/learningresources/**/*.java src/server/learningresources/*.java

# Run demo (no stdin needed)
java -cp out server.learningresources.LearningResourcesServer --demo

# List available tools
java -cp out server.learningresources.LearningResourcesServer --list-tools

# Start as MCP server (STDIO transport)
java -cp out server.learningresources.LearningResourcesServer
```

---

## Available Tools

| Tool | Description | Required Args |
|------|-------------|---------------|
| `search_resources` | Search vault by text, category, type, or difficulty | `query` |
| `browse_vault` | Browse resources by category or type | `category` or `type` |
| `get_resource` | Get detailed info about a specific resource | `id` |
| `list_categories` | List all categories with resource counts | _(none)_ |
| `scrape_url` | Scrape a URL and return a summary | `url` |
| `read_url` | Scrape a URL and return full text content | `url` |
| `add_resource` | Add a custom resource to the vault | `id`, `title`, `url`, `description`, `type` |

### Tool Arguments

**search_resources:**
- `query` — free-text search (searches title, description, tags, author)
- `category` — filter: java, python, javascript, web, database, devops, cloud, algorithms, software-engineering, testing, security, ai-ml, tools, systems, general
- `type` — filter: documentation, tutorial, blog, article, video, book, interactive, api-reference, cheat-sheet, repository
- `difficulty` — filter: beginner, intermediate, advanced
- `free_only` — "true" to show only free resources

**add_resource:**
- `id` — unique slug (e.g., "my-tutorial")
- `title` — display title
- `url` — full URL
- `description` — brief description
- `type` — one of the resource types listed above
- `category` — (optional) default: general
- `difficulty` — (optional) default: intermediate
- `tags` — (optional) comma-separated tags
- `author` — (optional) author name

---

## Built-In Resource Vault

The vault comes pre-loaded with curated resources across these categories:

### Java
| Resource | Type | Difficulty |
|----------|------|------------|
| The Java Tutorials (dev.java) | tutorial | beginner |
| JDK API Documentation (Javadoc) | api-reference | intermediate |
| The Java Language Specification | documentation | advanced |
| Inside.java Blog | blog | intermediate |
| Baeldung | tutorial | intermediate |
| Jenkov.com | tutorial | intermediate |
| Effective Java (3rd Ed.) | book | intermediate |
| Java Concurrency in Practice | book | advanced |
| Spring Boot Guides | tutorial | intermediate |
| Google Guava Wiki | documentation | intermediate |
| JUnit 5 User Guide | documentation | intermediate |

### Web & JavaScript
| Resource | Type | Difficulty |
|----------|------|------------|
| MDN Web Docs | documentation | beginner |
| javascript.info | tutorial | beginner |
| TypeScript Handbook | documentation | intermediate |

### Python
| Resource | Type | Difficulty |
|----------|------|------------|
| Python Official Tutorial | tutorial | beginner |
| Real Python | tutorial | intermediate |

### Algorithms & Data Structures
| Resource | Type | Difficulty |
|----------|------|------------|
| VisuAlgo | interactive | beginner |
| Big-O Cheat Sheet | cheat-sheet | beginner |

### Software Engineering & Design
| Resource | Type | Difficulty |
|----------|------|------------|
| Refactoring.Guru | tutorial | intermediate |
| Clean Code Summary | article | beginner |
| The Twelve-Factor App | article | intermediate |
| System Design Primer | repository | advanced |

### DevOps, Security, AI/ML, Testing, Tools, Databases
| Resource | Type | Difficulty |
|----------|------|------------|
| Docker Getting Started | tutorial | beginner |
| Kubernetes Docs | documentation | intermediate |
| Pro Git Book | book | beginner |
| GitHub Skills | interactive | beginner |
| Use The Index, Luke | book | intermediate |
| OWASP Top 10 | documentation | intermediate |
| fast.ai | tutorial | intermediate |
| Prompt Engineering Guide | tutorial | intermediate |
| Testing Trophy (Kent C. Dodds) | article | intermediate |
| roadmap.sh | interactive | beginner |
| Free Programming Books | repository | beginner |

---

## Architecture

```
server/learningresources/
├── LearningResourcesServer.java    ← STDIO entry point, CLI flags
├── package-info.java               ← Package documentation
│
├── model/                          ← Immutable data records
│   ├── LearningResource.java       ← Core resource (id, title, URL, type, tags)
│   ├── ResourceType.java           ← Enum: documentation, tutorial, blog, ...
│   ├── ResourceCategory.java       ← Enum: java, python, web, devops, ...
│   ├── ContentSummary.java         ← Scraped content with metadata
│   ├── ResourceQuery.java          ← Search/filter criteria
│   └── package-info.java
│
├── vault/                          ← Curated resource library
│   ├── ResourceVault.java          ← In-memory store with search
│   └── BuiltInResources.java       ← 30+ pre-loaded famous resources
│
├── scraper/                        ← Web scraping pipeline
│   ├── WebScraper.java             ← Java HttpClient-based fetcher
│   ├── ContentExtractor.java       ← HTML → clean text extraction
│   ├── ScraperResult.java          ← Raw HTTP response record
│   └── ScraperException.java       ← Scraper-specific errors
│
├── content/                        ← Content processing
│   ├── ContentSummarizer.java      ← Extract + summarize pipeline
│   ├── ContentReader.java          ← Format content for display
│   └── ReadabilityScorer.java      ← Difficulty estimation heuristics
│
└── handler/                        ← MCP tool dispatch
    ├── ToolHandler.java            ← Routes tool calls to handlers
    ├── SearchHandler.java          ← Vault search & browse
    └── ScrapeHandler.java          ← URL scrape & summarize
```

### Data Flow

```
User → MCP Client → STDIO → LearningResourcesServer
                              ├── ToolHandler (dispatch)
                              │   ├── SearchHandler → ResourceVault
                              │   └── ScrapeHandler → WebScraper
                              │                        → ContentExtractor
                              │                        → ContentSummarizer
                              │                        → ContentReader
                              └── Response → STDIO → MCP Client → User
```

### Scrape Pipeline

```
URL → WebScraper (HTTP GET)
    → ScraperResult (raw HTML + metadata)
    → ContentExtractor (strip HTML → clean text)
    → ContentSummarizer (summarize + word count + reading time)
    → ReadabilityScorer (estimate difficulty)
    → ContentSummary (final output)
    → ContentReader (format for display)
```

---

## Configuration

Add the learning-resources server to your `mcp-config.properties`:

```properties
server.learning-resources.name=Learning Resources
server.learning-resources.enabled=true
server.learning-resources.transport=stdio
server.learning-resources.command=java
server.learning-resources.args=-cp,out,server.learningresources.LearningResourcesServer
```

---

## Future Enhancements

- [ ] Persistent vault storage (save custom resources to disk)
- [ ] Full JSON-RPC MCP protocol compliance
- [ ] Jsoup integration for better HTML parsing
- [ ] LLM-powered abstractive summarization (via OpenAI API key)
- [ ] Bookmark/favorite resources
- [ ] Learning progress tracking
- [ ] RSS/Atom feed monitoring for new content
- [ ] Resource recommendations based on reading history
