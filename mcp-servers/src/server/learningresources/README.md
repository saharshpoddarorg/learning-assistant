# Learning Resources MCP Server

> **A curated vault of 68+ learning resources with smart discovery, web scraping, and multi-format export.**

---

## Overview

This MCP server acts as a **learning assistant** — it maintains a curated library of famous,
high-quality learning resources, discovers relevant content via smart intent classification,
and can scrape, extract, and summarize content from any URL on the internet.

**What it does:**

- **Vault** — 68+ pre-loaded resources: official docs, tutorials, blogs, books, and interactive
  tools from Oracle, Mozilla, Baeldung, Spring, OWASP, Docker, and more
- **Smart Discovery** — three-mode engine (specific / vague / exploratory) with keyword-to-concept
  inference, fuzzy matching, domain affinity, language-fit scoring, and "did you mean?" suggestions
- **Search** — full-text search with filters by category, type, difficulty, concept, freshness,
  language applicability, and official-only mode
- **Scrape** — fetches any URL, strips HTML, extracts meaningful text content
- **Summarize** — generates condensed summaries with word count, reading time, and difficulty
- **Read** — presents full extracted content in a clean, readable format
- **Smart Add** — scrape a URL, auto-extract metadata (title, description, author, type),
  infer categories and concepts, discover sub-pages, and add to the vault
- **Export** — format discovery/search results as Markdown, PDF, or Word (via pandoc with plain-text fallback)
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
| `discover_resources` | Smart discovery with intent classification, forced mode, and domain browsing | `query` |
| `scrape_url` | Scrape a URL and return a summary | `url` |
| `read_url` | Scrape a URL and return full text content | `url` |
| `add_resource` | Add a custom resource to the vault | `id`, `title`, `url`, `description`, `type` |
| `add_resource_from_url` | Scrape URL → auto-extract metadata → add to vault | `url` |
| `export_results` | Export discovery results as Markdown, PDF, or Word | `query` |

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
- `domain` — filter by concept domain for domain-level browsing (see ConceptDomain enum below)
- `mode` — force a search mode: `specific`, `vague`, or `exploratory` (see SearchMode enum below)
- `min_difficulty` / `max_difficulty` — difficulty range for concept/domain discovery

**add_resource_from_url:**
- `url` — the URL to scrape (required)
- `id`, `title`, `description`, `type`, `category`, `difficulty`, `tags`, `author`, `language_applicability` — optional overrides for auto-detected values

**export_results:**
- `query` — the discovery query to export
- `format` — output format: `md` (default), `pdf`, `word` (PDF/Word requires pandoc; falls back to plain text)

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
Fine-grained CS/SE concept areas for precise resource matching (33 values). Each concept belongs to a {@link ConceptDomain}.

| Value | Display Name | Domain | Example Topics |
|-------|-------------|--------|----------------|
| `LANGUAGE_BASICS` | language-basics | Programming Fundamentals | Variables, types, control flow, syntax |
| `OOP` | oop | Programming Fundamentals | Classes, inheritance, polymorphism, encapsulation |
| `FUNCTIONAL_PROGRAMMING` | functional-programming | Programming Fundamentals | Lambdas, streams, higher-order functions |
| `LANGUAGE_FEATURES` | language-features | Programming Fundamentals | Records, sealed classes, pattern matching |
| `GENERICS` | generics | Programming Fundamentals | Type parameters, wildcards, type erasure |
| `CONCURRENCY` | concurrency | Core CS | Threads, locks, CompletableFuture, virtual threads |
| `DATA_STRUCTURES` | data-structures | Core CS | Lists, maps, trees, graphs, hash tables |
| `ALGORITHMS` | algorithms | Core CS | Sorting, searching, graph algorithms, DP |
| `COMPLEXITY_ANALYSIS` | complexity-analysis | Core CS | Big-O analysis, benchmarking, optimization |
| `MEMORY_MANAGEMENT` | memory-management | Core CS | GC, allocation strategies, profiling |
| `DESIGN_PATTERNS` | design-patterns | Software Engineering | GoF patterns, factory, observer, strategy |
| `CLEAN_CODE` | clean-code | Software Engineering | SOLID, GRASP, refactoring, code smells |
| `TESTING` | testing | Software Engineering | JUnit, Mockito, TDD, BDD, integration testing |
| `API_DESIGN` | api-design | Software Engineering | REST, GraphQL, versioning, OpenAPI |
| `ARCHITECTURE` | architecture | Software Engineering | Hexagonal, microservices, layers, monolith |
| `SYSTEM_DESIGN` | system-design | System Design & Infrastructure | Scalability, load balancing, caching, sharding |
| `DATABASES` | databases | System Design & Infrastructure | SQL, NoSQL, indexing, replication, schemas |
| `DISTRIBUTED_SYSTEMS` | distributed-systems | System Design & Infrastructure | Consensus, CAP, eventual consistency |
| `NETWORKING` | networking | System Design & Infrastructure | TCP/IP, HTTP, DNS, TLS, sockets |
| `OPERATING_SYSTEMS` | operating-systems | System Design & Infrastructure | Processes, scheduling, file systems, kernels |
| `CI_CD` | ci-cd | DevOps & Tooling | CI/CD pipelines, Jenkins, GitHub Actions |
| `CONTAINERS` | containers | DevOps & Tooling | Docker, Kubernetes, orchestration |
| `VERSION_CONTROL` | version-control | DevOps & Tooling | Git, branching, merge strategies |
| `BUILD_TOOLS` | build-tools | DevOps & Tooling | Maven, Gradle, Bazel, npm |
| `INFRASTRUCTURE` | infrastructure | DevOps & Tooling | Terraform, Ansible, CloudFormation |
| `OBSERVABILITY` | observability | DevOps & Tooling | Monitoring, logging, tracing, alerting |
| `WEB_SECURITY` | web-security | Security | OWASP, XSS, CSRF, authentication |
| `CRYPTOGRAPHY` | cryptography | Security | Encryption, hashing, digital signatures |
| `MACHINE_LEARNING` | machine-learning | AI & Data | Neural networks, training, inference |
| `LLM_AND_PROMPTING` | llm-and-prompting | AI & Data | LLMs, prompt engineering, RAG, AI agents |
| `INTERVIEW_PREP` | interview-prep | Career & Meta | Coding challenges, system design interviews |
| `CAREER_DEVELOPMENT` | career-development | Career & Meta | Learning paths, roadmaps, skill mapping |
| `GETTING_STARTED` | getting-started | Career & Meta | Environment setup, first project, hello world |

### ConceptDomain _(new)_
High-level knowledge domains that group related ConceptArea values (8 domains).

| Value | Display Name | Concepts |
|-------|-------------|----------|
| `PROGRAMMING_FUNDAMENTALS` | Programming Fundamentals | LANGUAGE_BASICS, OOP, FUNCTIONAL_PROGRAMMING, LANGUAGE_FEATURES, GENERICS |
| `CORE_CS` | Core CS | CONCURRENCY, DATA_STRUCTURES, ALGORITHMS, COMPLEXITY_ANALYSIS, MEMORY_MANAGEMENT |
| `SOFTWARE_ENGINEERING` | Software Engineering | DESIGN_PATTERNS, CLEAN_CODE, TESTING, API_DESIGN, ARCHITECTURE |
| `SYSTEM_DESIGN` | System Design & Infrastructure | SYSTEM_DESIGN, DATABASES, DISTRIBUTED_SYSTEMS, NETWORKING, OPERATING_SYSTEMS |
| `DEVOPS_TOOLING` | DevOps & Tooling | CI_CD, CONTAINERS, VERSION_CONTROL, BUILD_TOOLS, INFRASTRUCTURE, OBSERVABILITY |
| `SECURITY` | Security | WEB_SECURITY, CRYPTOGRAPHY |
| `AI_DATA` | AI & Data | MACHINE_LEARNING, LLM_AND_PROMPTING |
| `CAREER_META` | Career & Meta | INTERVIEW_PREP, CAREER_DEVELOPMENT, GETTING_STARTED |

### SearchMode _(new)_
User intent classification for the discovery engine (3 modes).

| Value | Display Name | Description | Triggers |
|-------|-------------|-------------|----------|
| `SPECIFIC` | specific | Exact match — user knows exactly what they want | Quoted text, URLs, "docs for …", "official" |
| `VAGUE` | vague | Topic match — user knows the area but not the resource | Topic keywords ("java testing", "concurrency") |
| `EXPLORATORY` | exploratory | Explore — user wants guidance and suggestions | "learn", "beginner", "recommend", "getting started" |

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

### OutputFormat
Export format for discovery/search results (nested in `ExportHandler`).

| Value | Extension | Description |
|-------|-----------|-------------|
| `MARKDOWN` | `.md` | GitHub-flavored Markdown — full-featured, ready for docs rendering |
| `PDF` | `.pdf` | PDF document via pandoc (plain-text fallback if pandoc unavailable) |
| `WORD` | `.docx` | Word document via pandoc (plain-text fallback if pandoc unavailable) |

**Parsing:** `OutputFormat.fromString()` accepts: `md`, `markdown`, `pdf`, `docx`, `word`, `doc`.

**Pandoc dependency:** PDF and Word export attempt pandoc conversion first. If pandoc is not on the system PATH, the export falls back to formatted plain text with manual conversion instructions.

---

## Smart Discovery Engine

The discovery engine (`ResourceDiscovery`) classifies user intent into three modes:

| Mode | Triggers | Strategy |
|------|----------|----------|
| **Specific** | Quoted terms, URLs, "docs for X", "official" | Exact title/URL matching, high precision |
| **Vague** | Topic keywords ("java testing", "concurrency") | Keyword-to-concept inference, multi-dimensional scoring |
| **Exploratory** | "learn", "beginner", "getting started", "recommend" | Beginner-friendly, official-first, with suggestions |

**Forced mode:** Pass `mode=specific|vague|exploratory` to override automatic classification.

**Domain browsing:** Pass `domain=core-cs` (or any ConceptDomain) to discover all resources in a knowledge domain.

### Relevance Scoring

The `RelevanceScorer` applies weighted scoring across 12 dimensions:

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
| Domain affinity | 10 | Resource in same concept domain |
| Language fit | 12 | Matches language applicability (UNIVERSAL=12, JAVA_CENTRIC=8, etc.) |
| Fuzzy match | 8 | Prefix-based typo tolerance (3+ char prefix) |

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
│   ├── ConceptArea.java            ← Enum: 33 CS/SE concept areas (grouped by domain)
│   ├── ConceptDomain.java          ← Enum: 8 high-level knowledge domains
│   ├── SearchMode.java             ← Enum: specific / vague / exploratory intent
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
│   ├── ResourceDiscovery.java      ← Smart 3-mode discovery engine (~400 lines)
│   ├── RelevanceScorer.java        ← Multi-dimensional relevance scoring (12 dimensions)
│   ├── KeywordIndex.java           ← Keyword-to-enum intent inference maps
│   ├── DiscoveryResult.java        ← Discovery result record (SearchMode + scored results)
│   └── providers/                  ← Modular resource providers (11 files)
│       ├── JavaResources.java      ← 13 Java ecosystem resources
│       ├── WebResources.java       ← 5 web/JS/TS resources
│       ├── PythonResources.java    ← 4 Python resources
│       ├── AlgorithmsResources.java← 3 algorithms & DS resources
│       ├── EngineeringResources.java← 5 design/architecture resources
│       ├── DevOpsResources.java    ← 6 DevOps/cloud resources
│       ├── VcsResources.java       ← 9 VCS/Git workflow resources
│       ├── BuildToolsResources.java← 11 build automation resources (Maven, Gradle, Make, Bazel, npm)
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
    ├── ExportHandler.java          ← Markdown/PDF/Word export + OutputFormat enum
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
                              │   └── ExportHandler → Markdown/PDF/Word output
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
- [x] ~~PDF and Word export via Pandoc~~ — implemented with plain-text fallback
- [ ] Apache POI integration for richer Word/PDF output without external tools
- [ ] Bookmark/favorite resources
- [ ] Learning progress tracking
- [ ] RSS/Atom feed monitoring for new content
- [ ] Resource recommendations based on reading history
- [ ] Sub-page batch import (add all child pages of a resource)
- [ ] Language-specific learning paths (e.g., "Java beginner → advanced")
