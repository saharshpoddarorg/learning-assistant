# Learning Resources MCP Server

> **A curated vault of 47+ learning resources with smart discovery, web scraping, and export.**

---

## Overview

This MCP server acts as a **learning assistant** — it maintains a curated library of famous,
high-quality learning resources, discovers relevant content via smart intent classification,
and can scrape, extract, and summarize content from any URL on the internet.

**What it does:**

- **Vault** — 47+ pre-loaded resources: official docs, tutorials, blogs, books, and interactive
  tools from Oracle, Mozilla, Baeldung, Spring, OWASP, Docker, and more
- **Smart Discovery** — three-mode engine (specific / vague / exploratory) with keyword-to-concept
  inference, multi-dimensional relevance scoring, and "did you mean?" suggestions
- **Search** — full-text search with filters by category, type, difficulty, concept, freshness,
  language applicability, and official-only mode
- **Scrape** — fetches any URL, strips HTML, extracts meaningful text content
- **Summarize** — generates condensed summaries with word count, reading time, and difficulty
- **Read** — presents full extracted content in a clean, readable format
- **Smart Add** — scrape a URL, auto-extract metadata (title, description, author, type),
  infer categories and concepts, discover sub-pages, and add to the vault
- **Export** — format discovery/search results as Markdown (PDF/Word planned)
- **Extend** — add custom resources manually or via URL scraping

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
| `discover_resources` | Smart discovery with intent classification & scoring | `query` |
| `scrape_url` | Scrape a URL and return a summary | `url` |
| `read_url` | Scrape a URL and return full text content | `url` |
| `add_resource` | Add a custom resource to the vault | `id`, `title`, `url`, `description`, `type` |
| `add_resource_from_url` | Scrape URL → auto-extract metadata → add to vault | `url` |
| `export_results` | Export discovery results as Markdown | `query` |

### Tool Arguments

**search_resources:**
- `query` — free-text search (searches title, description, tags, author)
- `category` — filter: java, python, javascript, web, database, devops, cloud, algorithms, software-engineering, testing, security, ai-ml, tools, systems, general
- `type` — filter: documentation, tutorial, blog, article, video, book, interactive, api-reference, cheat-sheet, repository
- `concept` — filter by concept area (see ConceptArea enum below)
- `difficulty` / `max_difficulty` — filter by difficulty range: beginner, intermediate, advanced, expert
- `freshness` — filter: evergreen, actively-maintained, periodic, archived
- `official_only` — "true" to show only official/authoritative sources
- `free_only` — "true" to show only free resources

**discover_resources:**
- `query` — free-form user query (e.g., "learn java concurrency", "beginner stuff", "official docs")
- `concept` — filter by concept area for concept-based discovery
- `min_difficulty` / `max_difficulty` — difficulty range for concept discovery

**add_resource_from_url:**
- `url` — the URL to scrape (required)
- `id`, `title`, `description`, `type`, `category`, `difficulty`, `tags`, `author`, `language_applicability` — optional overrides for auto-detected values

**export_results:**
- `query` — the discovery query to export
- `format` — output format: `md` (default), `pdf` (planned), `word` (planned)

**add_resource:**
- `id` — unique slug (e.g., "my-tutorial")
- `title` — display title
- `url` — full URL
- `description` — brief description
- `type` — one of the resource types listed above
- `category` — (optional) default: general
- `difficulty` — (optional) default: intermediate
- `language_applicability` — (optional) default: universal
- `tags` — (optional) comma-separated tags
- `author` — (optional) author name

---

## Model Enums — Developer Reference

The vault uses a rich, type-safe categorization model. All enums support `fromString()` / `fromDisplayName()` for flexible parsing.

### ResourceCategory
Broad technology domains for grouping resources.

| Value | Display Name | Description |
|-------|-------------|-------------|
| `JAVA` | java | Java language, JVM, and ecosystem |
| `PYTHON` | python | Python language and ecosystem |
| `JAVASCRIPT` | javascript | JavaScript/TypeScript and web frontend |
| `WEB` | web | General web development (HTML, CSS, HTTP, REST) |
| `DATABASE` | database | SQL, NoSQL, data modeling |
| `DEVOPS` | devops | DevOps, CI/CD, containers |
| `CLOUD` | cloud | Cloud platforms (AWS, Azure, GCP) |
| `ALGORITHMS` | algorithms | Data structures, algorithms, complexity |
| `SOFTWARE_ENGINEERING` | software-engineering | Design patterns, architecture, clean code |
| `TESTING` | testing | Testing methodologies, frameworks, TDD |
| `SECURITY` | security | Security, OWASP, authentication |
| `AI_ML` | ai-ml | AI, machine learning, LLMs |
| `TOOLS` | tools | Git, IDE, developer productivity |
| `SYSTEMS` | systems | OS, networking, systems programming |
| `GENERAL` | general | Cross-cutting or general topics |

### ConceptArea
Fine-grained CS/SE concept areas for precise resource matching (27 values).

| Value | Display Name | Example Topics |
|-------|-------------|----------------|
| `LANGUAGE_BASICS` | Language Basics | Variables, types, control flow, syntax |
| `OOP` | Object-Oriented Programming | Classes, inheritance, polymorphism, encapsulation |
| `FUNCTIONAL_PROGRAMMING` | Functional Programming | Lambdas, streams, higher-order functions |
| `CONCURRENCY` | Concurrency | Threads, locks, CompletableFuture, virtual threads |
| `DESIGN_PATTERNS` | Design Patterns | GoF patterns, factory, observer, strategy |
| `DATA_STRUCTURES` | Data Structures | Lists, maps, trees, graphs, hash tables |
| `ALGORITHMS` | Algorithms | Sorting, searching, graph algorithms, DP |
| `TESTING` | Testing | JUnit, Mockito, TDD, BDD, integration testing |
| `WEB_DEVELOPMENT` | Web Development | HTTP, REST, GraphQL, frontend frameworks |
| `API_DESIGN` | API Design | RESTful, versioning, documentation, HATEOAS |
| `DATABASE_DESIGN` | Database Design | Normalization, indexing, query optimization |
| `SYSTEM_DESIGN` | System Design | Distributed systems, scalability, load balancing |
| `CLEAN_CODE` | Clean Code | Naming, refactoring, SOLID, code smells |
| `BUILD_TOOLS` | Build Tools | Maven, Gradle, make, npm |
| `VERSION_CONTROL` | Version Control | Git, branching, merge strategies |
| `DEVOPS` | DevOps | Docker, Kubernetes, CI/CD, monitoring |
| `WEB_SECURITY` | Web Security | OWASP, XSS, CSRF, authentication |
| `PERFORMANCE` | Performance | Profiling, optimization, GC tuning |
| `MACHINE_LEARNING` | Machine Learning | Neural networks, training, inference |
| `GENERICS` | Generics | Type parameters, wildcards, type erasure |
| `LANGUAGE_FEATURES` | Language Features | Records, sealed classes, pattern matching |
| `NETWORKING` | Networking | Sockets, HTTP clients, DNS |
| `CONTAINERS` | Containers | Docker, Kubernetes, container orchestration |
| `CLOUD_SERVICES` | Cloud Services | AWS, Azure, GCP, serverless |
| `GETTING_STARTED` | Getting Started | Hello world, setup guides, first projects |
| `CAREER` | Career | Interviews, resumes, job search |
| `OPEN_SOURCE` | Open Source | Contributing, maintaining, community |

### DifficultyLevel
Type-safe difficulty with ordinal for range queries.

| Value | Display | Ordinal | Description |
|-------|---------|---------|-------------|
| `BEGINNER` | beginner | 1 | No prior knowledge assumed |
| `INTERMEDIATE` | intermediate | 2 | Assumes basic knowledge |
| `ADVANCED` | advanced | 3 | Deep dives, edge cases |
| `EXPERT` | expert | 4 | Research papers, formal specs |

### ContentFreshness
Content maintenance status.

| Value | Description |
|-------|-------------|
| `EVERGREEN` | Timeless content (books, fundamental concepts) |
| `ACTIVELY_MAINTAINED` | Regularly updated (official docs, live tutorials) |
| `PERIODIC` | Updated occasionally (annual reviews, version-specific) |
| `ARCHIVED` | No longer maintained but still valuable |

### LanguageApplicability _(new)_
How a resource relates to programming languages — enables Java-focused filtering.

| Value | Display | Description | Transferable? |
|-------|---------|-------------|---------------|
| `UNIVERSAL` | universal | Applies to all languages (algorithms, patterns) | Yes |
| `MULTI_LANGUAGE` | multi-language | Applies across multiple platforms (REST, SQL) | Yes |
| `JAVA_CENTRIC` | java-centric | Java-focused but concepts transfer | Yes |
| `JAVA_SPECIFIC` | java-specific | JVM-only (virtual threads, JPMS) | No |
| `PYTHON_SPECIFIC` | python-specific | Python-only (GIL, asyncio) | No |
| `WEB_SPECIFIC` | web-specific | JS/TS/Web-only (event loop, DOM) | No |

### ResourceType
Content format classification.

| Value | Display | Examples |
|-------|---------|---------|
| `DOCUMENTATION` | documentation | Official docs, specs |
| `TUTORIAL` | tutorial | Step-by-step guides |
| `BLOG` | blog | Blog posts, articles |
| `ARTICLE` | article | Technical articles |
| `VIDEO` | video | YouTube, courses |
| `BOOK` | book | Physical/digital books |
| `INTERACTIVE` | interactive | VisuAlgo, playgrounds |
| `API_REFERENCE` | api-reference | Javadoc, API specs |
| `CHEAT_SHEET` | cheat-sheet | Quick references |
| `REPOSITORY` | repository | GitHub repos, examples |

---

## Smart Discovery Engine

The discovery engine (`ResourceDiscovery`) classifies user intent into three modes:

| Mode | Triggers | Strategy |
|------|----------|----------|
| **Specific** | Quoted terms, URLs, "docs for X", "official" | Exact title/URL matching, high precision |
| **Vague** | Topic keywords ("java testing", "concurrency") | Keyword-to-concept inference, multi-dimensional scoring |
| **Exploratory** | "learn", "beginner", "getting started", "recommend" | Beginner-friendly, official-first, with suggestions |

### Relevance Scoring

The `RelevanceScorer` applies weighted scoring across 9 dimensions:

| Dimension | Weight | Description |
|-----------|--------|-------------|
| Exact title match | 100 | Title equals query text |
| Partial title match | 40 | Title contains query words |
| Description match | 20 | Description contains keywords |
| Tag match | 15 | Tags contain keywords |
| Concept match | 25 | Resource covers inferred concept |
| Category match | 20 | Resource in inferred category |
| Official boost | 15 | Official/authoritative source |
| Freshness boost | 10 | Actively maintained content |
| Difficulty fit | 10 | Matches target difficulty |

### Keyword Index

The `KeywordIndex` maps ~130 keywords/phrases to structured enums:
- **Concept map** (~90 entries): "thread" → CONCURRENCY, "factory" → DESIGN_PATTERNS
- **Category map** (~30 entries): "docker" → DEVOPS, "react" → JAVASCRIPT
- **Difficulty map** (~14 entries): "beginner" → BEGINNER, "expert" → EXPERT

---

## Architecture

```
server/learningresources/
├── LearningResourcesServer.java    ← STDIO entry point, CLI flags
├── package-info.java               ← Package documentation
│
├── model/                          ← Immutable data records & enums
│   ├── LearningResource.java       ← Core 15-field resource record
│   ├── ResourceType.java           ← Enum: documentation, tutorial, blog, ...
│   ├── ResourceCategory.java       ← Enum: java, python, web, devops, ...
│   ├── ConceptArea.java            ← Enum: 27 CS/SE concept areas
│   ├── DifficultyLevel.java        ← Enum: beginner → expert with ordinal ranges
│   ├── ContentFreshness.java       ← Enum: evergreen, actively-maintained, ...
│   ├── LanguageApplicability.java  ← Enum: universal → language-specific
│   ├── ContentSummary.java         ← Scraped content with metadata
│   ├── ResourceQuery.java          ← Search/filter criteria record
│   └── package-info.java
│
├── vault/                          ← Discovery engine & resource library
│   ├── ResourceVault.java          ← In-memory store with composite search
│   ├── BuiltInResources.java       ← Loads all providers into vault
│   ├── ResourceProvider.java       ← Provider interface
│   ├── ResourceDiscovery.java      ← Smart 3-mode discovery engine (~300 lines)
│   ├── RelevanceScorer.java        ← Multi-dimensional relevance scoring
│   ├── KeywordIndex.java           ← Keyword-to-enum intent inference maps
│   ├── DiscoveryResult.java        ← Discovery result record + QueryType enum
│   └── providers/                  ← Modular resource providers (9 files)
│       ├── JavaResources.java      ← 13 Java ecosystem resources
│       ├── WebResources.java       ← 5 web/JS/TS resources
│       ├── PythonResources.java    ← 4 Python resources
│       ├── AlgorithmsResources.java← 3 algorithms & DS resources
│       ├── EngineeringResources.java← 5 design/architecture resources
│       ├── DevOpsResources.java    ← 6 DevOps/cloud resources
│       ├── AiMlResources.java      ← 3 AI/ML resources
│       ├── DataAndSecurityResources.java ← 4 database/security resources
│       ├── GeneralResources.java   ← 4 general/cross-cutting resources
│       └── package-info.java
│
├── scraper/                        ← Web scraping pipeline
│   ├── WebScraper.java             ← Java HttpClient-based fetcher
│   ├── ContentExtractor.java       ← HTML → clean text + metadata extraction
│   ├── ScraperResult.java          ← Raw HTTP response record
│   └── ScraperException.java       ← Scraper-specific errors
│
├── content/                        ← Content processing
│   ├── ContentSummarizer.java      ← Extract + summarize pipeline
│   ├── ContentReader.java          ← Format content for display
│   └── ReadabilityScorer.java      ← Difficulty estimation heuristics
│
└── handler/                        ← MCP tool dispatch
    ├── ToolHandler.java            ← Routes 10 tools to handlers
    ├── SearchHandler.java          ← Vault search, browse, details
    ├── ScrapeHandler.java          ← URL scrape & summarize
    ├── ExportHandler.java          ← Markdown export + OutputFormat enum
    └── UrlResourceHandler.java     ← Smart add-from-URL with metadata inference
```

### Data Flow

```
User → MCP Client → STDIO → LearningResourcesServer
                              ├── ToolHandler (dispatch — 10 tools)
                              │   ├── SearchHandler → ResourceVault
                              │   ├── ResourceDiscovery → KeywordIndex
                              │   │                     → RelevanceScorer
                              │   │                     → ResourceVault
                              │   ├── ScrapeHandler → WebScraper
                              │   │                  → ContentExtractor
                              │   │                  → ContentSummarizer
                              │   ├── UrlResourceHandler → WebScraper → ResourceVault
                              │   └── ExportHandler → Markdown output
                              └── Response → STDIO → MCP Client → User
```

### Scrape Pipeline

```
URL → WebScraper (HTTP GET)
    → ScraperResult (raw HTML + metadata)
    → ContentExtractor (strip HTML → clean text, title, meta tags)
    → ContentSummarizer (summarize + word count + reading time)
    → ReadabilityScorer (estimate difficulty)
    → ContentSummary (final output)
    → ContentReader (format for display)
```

### Smart Add-from-URL Pipeline

```
URL → WebScraper → ContentExtractor
    → Extract: title (from <title>/<h1>), description (meta/OG tags), author
    → Infer: type (from URL patterns), category (from content), concepts
    → Detect: official domain, sub-pages (internal links)
    → Build LearningResource → Add to ResourceVault
    → Report: confirmation + discovered sub-pages
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
- [ ] PDF and Word export via Pandoc or Apache POI
- [ ] Bookmark/favorite resources
- [ ] Learning progress tracking
- [ ] RSS/Atom feed monitoring for new content
- [ ] Resource recommendations based on reading history
- [ ] Sub-page batch import (add all child pages of a resource)
- [ ] Language-specific learning paths (e.g., "Java beginner → advanced")
