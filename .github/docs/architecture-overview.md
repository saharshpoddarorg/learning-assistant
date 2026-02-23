# Architecture Overview — Learning Assistant

> **Who this is for:** Developers learning how this project is structured and why
> each architectural decision was made. Covering module design, design patterns,
> industry standards, and the philosophy behind the search engine hierarchy.

---

## Table of Contents

1. [Module Architecture — The "interfaces_src" Pattern](#1-module-architecture--the-interfaces_src-pattern)
2. [Module Dependency Graph](#2-module-dependency-graph)
3. [The 3-Tier Search Architecture](#3-the-3-tier-search-architecture)
4. [Design Patterns Reference](#4-design-patterns-reference)
5. [Industry Standards and Approaches](#5-industry-standards-and-approaches)
6. [Generic Types vs Wildcards — Design Decision](#6-generic-types-vs-wildcards--design-decision)
7. [MCP Protocol Architecture](#7-mcp-protocol-architecture)
8. [Package Structure Reference](#8-package-structure-reference)
9. [How to Extend the System](#9-how-to-extend-the-system)

---

## 1. Module Architecture — The "interfaces_src" Pattern

### The Pattern

A proven enterprise architecture pattern separates **contracts** (interfaces) from
**implementations**:

```
interfaces_src  ←  pure contracts; zero business logic
      ↑
  module-a      ←  implementation A (e.g., in-memory search)
      ↑
  module-b      ←  implementation B (e.g., domain search, MCP servers)
```

**Benefits:**
- **Stable contracts:** callers depend only on interfaces, so implementations can be
  swapped without changing the callers
- **Testability:** mock any interface in unit tests without pulling in implementation classes
- **Layered compilation:** `interfaces_src` compiles independently; implementations can be
  rebuilt and upgraded in isolation
- **Dependency Inversion Principle (DIP):** high-level policy code (`mcp-servers`) depends
  on abstractions (`search-api`), not on concretions (`search-engine`)

### Applied to this project

This project applies the pattern **within a single module** rather than across
separate modules. `search-engine` contains both layers in two distinct packages:

| Role | Module | Java packages |
|------|--------|---------------|
| **interfaces_src** (contracts) | `search-engine` | `search.api.*` — 12 interfaces/records |
| **implementation** | `search-engine` | `search.engine.*` — 14 implementation classes |
| **domain consumer** | `mcp-servers` | `server.learningresources.*`, `server.atlassian.*` |

> **Why one module, not two?**
> A separate `search-api` module was prototyped then consciously merged back. The
> package boundary (`search.api.*` vs `search.engine.*`) already enforces the
> discipline; a separate module would add build complexity without benefit for a
> learning project. This matches how major libraries (Spring, Jackson) organise
> their own API vs implementation packages.

---

## 2. Module Dependency Graph

```
┌─────────────────────────────────────────────────────────────┐
│                    learning-assistant (root)                 │
│  IntelliJ project container — no source of its own          │
└────────────────────────────┬────────────────────────────────┘
                             │ contains
                  ┌──────────┴───────────┐
                  ▼                      ▼
         ┌──────────────────┐    ┌───────────────┐
         │  search-engine   │◄───│  mcp-servers  │
         │                  │    │               │
         │  search.api.*    │    │ server.*      │
         │  search.engine.* │    │ config.*      │
         └──────────────────┘    └───────────────┘
          Contracts + Impls        Domain engines
          Self-contained           Depends on
          (no deps)                search-engine
```

**Dependency rules:**
- `search-engine` → **nothing** (leaf node; compiles standalone, 26 files)
- `mcp-servers` → `search-engine` (gets both `search.api.*` and `search.engine.*`)

**Why `mcp-servers` only needs `search-engine`:**
- `search.api.*` (interface types like `SearchEngine<T>`, `SearchResult<T>`) live _inside_ `search-engine`
- `search.engine.*` (base classes like `ConfigurableSearchEngine<T>`) also live in `search-engine`
- One module dependency covers everything

---

## 3. The 3-Tier Search Architecture

Each tier is a more domain-specific refinement of the one below it:

```
Tier 1 — CONTRACTS (search-engine / search.api.*)
─────────────────────────────────────────────────────────────
  SearchEngine<T>          QueryClassifier       SearchFilter<T>
  SearchResult<T>          ScoringStrategy<T>    RankingStrategy<T>
  SearchContext            Tokenizer             SearchIndex<T>
  ScoredItem<T>            ScoreBreakdown
  SearchMode (enum)

Tier 2 — ALGORITHM IMPLEMENTATIONS (search-engine / search.engine.*)
─────────────────────────────────────────────────────────────
  ConfigurableSearchEngine<T>   ← orchestrates the 5-phase pipeline
  Bm25Scorer<T>                 ← industry-standard full-text ranking
  TextMatchScorer<T>            ← title / body / tag matching
  CompositeScorer<T>            ← weighted combination of strategies
  TagScorer<T>                  ← faceted tag matching
  FuzzyMatcher                  ← prefix / substring tolerance
  DefaultTokenizer              ← stop-word removal + normalisation
  KeywordQueryClassifier        ← 5-rule intent detection
  FilterChain<T>                ← AND-chain of SearchFilters
  ScoreRanker<T>                ← score-descending sort
  RecencyBoostRanker<T>         ← time-decay recency boost
  InMemoryIndex<T>              ← ConcurrentHashMap document store
  KeywordRegistry<V>            ← query-to-domain inference
  SearchEngineConfig<T>         ← wires all components together

Tier 3 — DOMAIN-SPECIFIC ENGINES (mcp-servers)
─────────────────────────────────────────────────────────────
  LearningSearchEngine          ← Tier-2 engine for all learning resources
  OfficialDocsSearchEngine      ← Tier-3 engine for official docs only
  (future: AtlassianSearchEngine, ...)
```

### The 5-Phase Pipeline (`ConfigurableSearchEngine`)

Every search runs through these phases in order:

```
Phase 1:  classifyQuery()      rawInput → SearchMode (SPECIFIC / VAGUE / EXPLORATORY)
              ↓
Phase 2:  filterDocuments()    removes items that don't pass SearchFilter<T>
              ↓
Phase 3:  scoreDocuments()     applies ScoringStrategy per SearchMode, drops score ≤ 0
              ↓
Phase 4:  rank()               RankingStrategy orders results (default: score desc)
              ↓
Phase 5:  buildResult()        trims to maxResults, builds SearchResult<T>
```

**Hooks for customisation:**
```java
// Override in a subclass to run code before/after the pipeline:
protected SearchResult<T> preSearch(SearchContext context)  { /* ... */ return null; }
protected SearchResult<T> postSearch(SearchContext context, SearchResult<T> result) { return result; }
```

`OfficialDocsSearchEngine` uses `postSearch` to inject "did you mean?" suggestions
when the result is empty.

---

## 4. Design Patterns Reference

### Strategy Pattern
**Where:** `ScoringStrategy<T>`, `RankingStrategy<T>`, `SearchFilter<T>`, `QueryClassifier`

**What:** Defines a family of algorithms, encapsulates each, and makes them interchangeable.

```java
// Swap scoring strategy at runtime — caller never knows which one
SearchEngineConfig.<Article>builder()
    .scorer(SearchMode.SPECIFIC, new Bm25Scorer<>(...))     // Tier 3: precise
    .scorer(SearchMode.VAGUE,    new TextMatchScorer<>(...)) // Tier 3: broad
    .scorer(SearchMode.EXPLORATORY, ScoringStrategy.constant(50)) // Tier 3: equal
    .build();
```

### Template Method Pattern
**Where:** `ConfigurableSearchEngine<T>`

**What:** Defines the skeleton of the search pipeline (`final search()` method) and
lets subclasses provide optional customisation via `preSearch()` / `postSearch()` hooks.

The `final` modifier on `search()` ensures the 5-phase contract is always honoured —
subclasses can only plug into the defined extension points.

### Builder Pattern
**Where:** `SearchEngineConfig`, `Bm25Scorer`, `CompositeScorer`, `KeywordQueryClassifier`, `TextMatchScorer`, `TagScorer`, `KeywordRegistry`

**What:** Assembles complex objects step-by-step without telescoping constructors.
Builders enforce required fields at `build()` time, making misconfigured engines
impossible to create.

```java
// All args optional except textExtractor — enforced at build() time
Bm25Scorer.<Resource>builder()
    .textExtractor(Resource::body)  // required
    .tokenizer(new DefaultTokenizer())
    .k1(1.2)
    .b(0.8)
    .build();
```

### Chain of Responsibility Pattern
**Where:** `FilterChain<T>`, `RankingStrategy.thenRank()`

**What:** Passes a request through a chain of handlers. `FilterChain` short-circuits
on the first failing filter (AND semantics). `thenRank()` composes rankers left-to-right.

```java
FilterChain.of(
    (item, ctx) -> item.difficulty() <= maxDifficulty,
    (item, ctx) -> item.isActive(),
    (item, ctx) -> !item.isDeprecated()
)
```

### Composite Pattern
**Where:** `CompositeScorer<T>`

**What:** A tree of scorers where the composite scores the same way as a single leaf.
Callers call `.score(item, ctx)` — they don't know if they're talking to a simple scorer
or a weighted composite of five scorers.

### Functional Interface / Lambda Pattern
**Where:** `ScoringStrategy<T>`, `SearchFilter<T>`, `QueryClassifier`, `Tokenizer`

All four are `@FunctionalInterface` — they can be implemented as lambdas, improving
readability and reducing boilerplate:

```java
// No class required — express logic inline
SearchFilter<Resource> officialOnly = (resource, ctx) -> resource.isOfficial();
QueryClassifier forcedSpecific       = query -> SearchMode.SPECIFIC;
ScoringStrategy<Resource> zero       = (resource, ctx) -> 0;
```

### Singleton (stateless)
**Where:** `ScoreRanker<T>`

A stateless comparator with no mutable state — safe to share across threads.
Uses the classic `INSTANCE` field with an `instance()` factory method.

```java
// Type-safe singleton access without unchecked casts at the call site
ScoreRanker<Resource> ranker = ScoreRanker.instance();
```

### Factory Method (static)
**Where:** `SearchContext.of()`, `SearchResult.empty()`, `FilterChain.of()`,
`ScoringStrategy.zero()`, `QueryClassifier.alwaysVague()`, etc.

Static factory methods over constructors provide:
- Descriptive names (`empty()`, `alwaysSpecific()`)
- Return-type covariance (can return a subtype)
- Caching / singleton management

---

## 5. Industry Standards and Approaches

### BM25 — The Information Retrieval Standard

Used by: **Elasticsearch**, **Apache Solr**, **Apache Lucene**, **Whoosh**, **SQLite FTS5**

BM25 (Okapi Best Match 25) is the default ranking function in most modern search
infrastructure. This project implements it from scratch in `Bm25Scorer` to provide
full understanding of how it works. See [search-engine-algorithms.md](search-engine-algorithms.md)
for the full formula and parameter guide.

### MCP — Model Context Protocol

Used by: **GitHub Copilot**, **Claude**, other AI assistants

MCP is a JSON-RPC 2.0 protocol that lets AI models call server-defined **tools**.
Each server publishes a list of tool schemas (name, description, input JSON Schema),
and the AI calls them like functions. See [mcp-servers-architecture.md](mcp-servers-architecture.md)
for a complete protocol breakdown.

### Dependency Inversion Principle (DIP)
From **SOLID** principles. The `search-api` module IS the application of DIP:
- High-level policy: `mcp-servers` (uses search to serve AI queries)
- Abstraction: `search-api` (`SearchEngine<T>`, `ScoringStrategy<T>`, ...)
- Low-level detail: `search-engine` (BM25, TextMatchScorer, pipelines)

Policy code (`mcp-servers`) depends ONLY on abstractions (`search-api`).
Low-level detail (`search-engine`) also depends on the same abstractions.

### Interface Segregation Principle (ISP)
From **SOLID** principles. The search API is split into many small, focused interfaces
rather than one large `SearchEngine` god-interface:
- `ScoringStrategy<T>` — scoring only
- `SearchFilter<T>` — filtering only
- `RankingStrategy<T>` — ranking only
- `Tokenizer` — tokenisation only
- `QueryClassifier` — classification only
- `SearchIndex<T>` — storage only

A component implementing one of these doesn't need to know about the others.

### Open/Closed Principle (OCP)
From **SOLID** principles. `ConfigurableSearchEngine<T>` is:
- **Closed for modification:** the 5-phase pipeline is `final` — you can't break it
- **Open for extension:** add `preSearch` / `postSearch` hooks, plug in new strategies,
  implement new `SearchFilter` or `ScoringStrategy` without touching base classes

### Zero-Dependency Architecture
The entire `search-api` and `search-engine` modules use **only the Java 21 standard
library** — no Maven, no Gradle, no third-party JARs. This is intentional:
- Educational clarity: understand the algorithm, not the framework
- Zero transitive dependency conflicts
- Portable: runs anywhere a JDK 21 is installed

---

## 6. Generic Types vs Wildcards — Design Decision

### The question

Should the API be `SearchEngine<T>` or `SearchEngine<?>`?
Should collections use `List<ScoredItem<T>>` or `List<ScoredItem<?>>`?

### Answer: `<T>` on definitions, `<?>` at use-sites

**Rule 1: Interface and class definitions always use `<T>` (named type parameter)**

```java
// ✅ CORRECT — type parameter provides compile-time safety for callers
public interface SearchEngine<T> {
    SearchResult<T> search(SearchContext context);
}

public record ScoredItem<T>(T item, int score) { }
```

**Rule 2: `<?>` is a _use-site_ wildcard for heterogeneous collections**

```java
// ✅ Use <?> when you have a registry of engines of different types
Map<String, SearchEngine<?>> engines = new HashMap<>();
engines.put("resources", new LearningSearchEngine(vault));
engines.put("issues",    new AtlassianSearchEngine(client));

// The caller can call .search() but cannot access the typed items
// (because the type is unknown — this is correct when mixing types)
SearchResult<?> result = engines.get("resources").search(ctx);
```

**Rule 3: Callers that know the type use `<T>` directly**

```java
// ✅ Concrete domain code knows its type — use explicit T
SearchEngine<LearningResource> engine = new LearningSearchEngine(vault);
SearchResult<LearningResource> result = engine.search(ctx);
List<ScoredItem<LearningResource>> items = result.items(); // type-safe!
```

### Why not `<?>` everywhere?

If `SearchEngine<?>` were the base interface, you'd lose type safety at every boundary:
```java
SearchEngine<?> engine = ...;
SearchResult<?> result = engine.search(ctx);
List<ScoredItem<?>> items = result.items();
ScoredItem<?> item = items.get(0);
Object value = item.item(); // forced to Object — unusable without cast
```

The `<T>` generic preserves the type all the way through the pipeline.

### Summary

| Context | Use |
|---|---|
| Interface/class definition | `SearchEngine<T>`, `SearchResult<T>`, `ScoredItem<T>` |
| Known concrete type (caller knows `T`) | `SearchEngine<LearningResource>` |
| Registry of mixed-type engines | `Map<String, SearchEngine<?>>` |
| Utility that works on any `SearchResult` | `void printSummary(SearchResult<?> result)` |

---

## 7. MCP Protocol Architecture

See [mcp-servers-architecture.md](mcp-servers-architecture.md) for a complete breakdown
of how MCP servers work, how tools are registered, and how requests flow from the AI
assistant through to domain services.

---

## 8. Package Structure Reference

```
learning-assistant/                    ← IntelliJ project root
│
├── search-api/                        ← MODULE: Pure interface contracts
│   └── src/search/api/
│       ├── core/       SearchEngine, SearchContext, SearchResult, ScoredItem, ScoreBreakdown
│       ├── classify/   SearchMode (enum), QueryClassifier
│       ├── algorithm/  ScoringStrategy, Tokenizer
│       ├── filter/     SearchFilter
│       ├── rank/       RankingStrategy
│       └── index/      SearchIndex
│
├── search-engine/                     ← MODULE: Algorithm implementations
│   └── src/search/engine/
│       ├── core/       ConfigurableSearchEngine (5-phase pipeline)
│       ├── algorithm/  Bm25Scorer, TextMatchScorer, CompositeScorer, TagScorer,
│       │               FuzzyMatcher, DefaultTokenizer
│       ├── classify/   KeywordQueryClassifier
│       ├── config/     SearchEngineConfig (wires all components)
│       ├── filter/     FilterChain
│       ├── rank/       ScoreRanker, RecencyBoostRanker
│       └── index/      InMemoryIndex, KeywordRegistry
│
├── mcp-servers/                       ← MODULE: Domain engines + MCP protocol
│   └── src/
│       ├── Main.java                  ← Entry point, server registry
│       ├── config/                    ← Config loading, validation, models
│       └── server/
│           ├── learningresources/     ← Learning Resources MCP server
│           │   ├── LearningResourcesServer.java
│           │   ├── model/             LearningResource, SearchMode (domain)
│           │   ├── vault/             ResourceVault, LearningSearchEngine (Tier-2)
│           │   ├── search/            OfficialDocsSearchEngine (Tier-3)
│           │   ├── handler/           Tool handlers (search, discover, browse)
│           │   ├── content/           Resource definitions
│           │   └── scraper/           Resource scrapers
│           └── atlassian/             ← Atlassian MCP server (Jira, Confluence, Bitbucket)
│               ├── AtlassianServer.java
│               ├── client/            REST clients per product
│               ├── handler/           Tool handlers
│               ├── model/             Domain models
│               └── formatter/         Response formatters
│
├── brain/                             ← Personal note-taking system
├── .github/                           ← Copilot customisation, docs, skills
└── .idea/                             ← IntelliJ project config
```

---

## 9. How to Extend the System

### Add a new scoring strategy

```java
// In mcp-servers or search-engine
public final class DifficultyScorer<T> implements ScoringStrategy<T> {

    private final Function<T, DifficultyLevel> difficultyExtractor;
    private final DifficultyLevel targetLevel;

    public DifficultyScorer(Function<T, DifficultyLevel> extractor, DifficultyLevel target) {
        this.difficultyExtractor = extractor;
        this.targetLevel = target;
    }

    @Override
    public int score(T item, SearchContext context) {
        var level = difficultyExtractor.apply(item);
        return level == targetLevel ? 30 : (Math.abs(level.ordinal() - targetLevel.ordinal()) == 1 ? 10 : 0);
    }
}
```

### Add a new domain-specific engine (Tier-3)

```java
// Extend ConfigurableSearchEngine from search-engine module
public final class BookSearchEngine extends ConfigurableSearchEngine<LearningResource> {

    public BookSearchEngine(ResourceVault vault) {
        super(buildConfig(vault));
    }

    @Override
    protected SearchResult<LearningResource> preSearch(SearchContext context) {
        // Return a cached result, circuit-break, or add audit logging
        return null; // null means "continue with pipeline"
    }

    @Override
    protected SearchResult<LearningResource> postSearch(SearchContext context,
                                                         SearchResult<LearningResource> result) {
        if (result.isEmpty()) {
            return SearchResult.emptyWithSuggestions(
                result.classifiedMode(),
                "No books found. Try broader terms.",
                List.of("java fundamentals", "design patterns", "clean code")
            );
        }
        return result;
    }

    private static SearchEngineConfig<LearningResource> buildConfig(ResourceVault vault) {
        var index = new InMemoryIndex<LearningResource>();
        vault.listAll().stream()
             .filter(r -> r.type() == ResourceType.BOOK)
             .forEach(r -> index.add(r.id(), r));

        return SearchEngineConfig.<LearningResource>builder()
            .index(index)
            .classifier(new KeywordQueryClassifier.Builder().build())
            .defaultScorer(new TextMatchScorer.Builder<LearningResource>()
                .titleExtractor(LearningResource::title)
                .bodyExtractor(LearningResource::description)
                .build())
            .ranker(ScoreRanker.instance())
            .build();
    }
}
```

### Add a new MCP server

1. Create `mcp-servers/src/server/myserver/MyServer.java` implementing the MCP server contract
2. Add tool handlers in `mcp-servers/src/server/myserver/handler/`
3. Register the server in `Main.java`
4. Add user-config in `mcp-servers/user-config/servers/myserver/`

See [mcp-servers-architecture.md](mcp-servers-architecture.md) for the full protocol.

---

*This document covers the architectural decisions of the Learning Assistant project.
For algorithm details, see [search-engine-algorithms.md](search-engine-algorithms.md).
For MCP protocol internals, see [mcp-servers-architecture.md](mcp-servers-architecture.md).*
