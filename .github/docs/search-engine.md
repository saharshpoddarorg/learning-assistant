# Search Engine — Developer Guide

> **The pluggable search engine powering all MCP servers. Start here.**  
> File: `.github/docs/search-engine.md`

---

## 🚦 Pick Your Tier

| Experience | Reading path |
|---|---|
| 🟢 **Newbie** — "I want to use search, not build it" | [Newbie path ↓](#-newbie-using-search-from-mcp-tools) |
| 🟡 **Amateur** — "I want to add keywords or tune scoring" | [Amateur path ↓](#-amateur-configuring-and-extending-the-engine) |
| 🔴 **Pro** — "I want to build a new domain-specific engine" | [Pro path ↓](#-pro-building-a-custom-search-engine) |

---

---

# 🟢 Newbie: Using Search from MCP Tools

> Prerequisite: you can build and run the MCP servers. See [mcp-server-setup.md](mcp-server-setup.md).

## What the search engine does

The search engine is what makes the Learning Resources and Atlassian MCP servers "smart". When you type a query like `"learn java concurrency beginner"`, the engine:

1. **Classifies** your intent — are you looking for something specific, browsing a topic, or just exploring?
2. **Scores** every resource in the library against your query across multiple dimensions (title, tags, concepts, difficulty, freshness).
3. **Ranks** the results highest-first.
4. **Returns** a structured list with a human-readable summary.

## Searching via MCP tools

Both servers expose search as MCP tools you can call from Copilot Chat:

**Learning Resources server tools:**

| Tool | What it does | Example |
|------|-------------|---------|
| `search_resources` | Full-text search with filters | `query: "java concurrency"` |
| `discover_resources` | Smart intent-based discovery | `query: "beginner stuff"` |
| `browse_vault` | Browse by category or type | `category: "java"` |

**Example MCP tool call (in Copilot Chat):**
```
/resources search java concurrency beginner
/resources discover design patterns
/resources browse category:testing
```

## The three search modes

| Mode | When it applies | Example queries |
|------|----------------|-----------------|
| **Specific** | You know the exact resource name or URL | `"JUnit 5 documentation"`, `"official Java tutorials"` |
| **Vague** | You know the topic, not the specific resource | `"java testing"`, `"design patterns intermediate"` |
| **Exploratory** | You want curated recommendations | `"help me get started"`, `"beginner stuff"`, `"what should I learn"` |

The engine **auto-detects** which mode fits your query — or you can force it:
```
/resources discover mode:specific JUnit 5 docs
/resources discover mode:exploratory java
```

## Understanding difficulty levels

| Level | What it means | Good for |
|-------|--------------|---------|
| `beginner` | No prior knowledge assumed | Just starting out |
| `intermediate` | Assumes basics | Know the language, learning patterns |
| `advanced` | Deep dives, internals | Solid foundation, going deeper |
| `expert` | Research papers, formal specs | Mastery |

```
# Filter by difficulty in search
/resources search java concurrency difficulty:beginner
/resources discover java design:advanced
```

## Understanding the result format

Each result shows:
- **Score** — relative relevance (not absolute; compare within the same query only)
- **Title** — resource name
- **Difficulty** — `beginner` / `intermediate` / `advanced` / `expert`
- **Type** — `documentation`, `tutorial`, `blog`, `book`, `video`, `interactive`, etc.
- **Official** — ✓ if from an authoritative source (Oracle, MDN, Docker, etc.)

---

**→ Next:** [Amateur path: configuring the engine](#-amateur-configuring-and-extending-the-engine)  
**→ See also:** [MCP Server Setup](mcp-server-setup.md) · [Slash Commands](slash-commands.md)

---
---

# 🟡 Amateur: Configuring and Extending the Engine

> Prerequisite: you can read and modify Java code. See [getting-started.md](getting-started.md).

## Where the code lives

The search engine is split across three IntelliJ modules. See [architecture-overview.md](architecture-overview.md) for the full module dependency graph.

```
search-api/src/search/api/           ← CONTRACTS (interfaces, no logic)
├── core/    SearchEngine, SearchContext, SearchResult, ScoredItem, ScoreBreakdown
├── classify/ SearchMode, QueryClassifier
├── algorithm/ ScoringStrategy, Tokenizer
├── filter/  SearchFilter
├── rank/    RankingStrategy
└── index/   SearchIndex

search-engine/src/search/engine/     ← ALGORITHM IMPLEMENTATIONS
├── core/    ConfigurableSearchEngine (5-phase pipeline)
├── algorithm/ Bm25Scorer, TextMatchScorer, CompositeScorer, TagScorer, FuzzyMatcher, DefaultTokenizer
├── classify/ KeywordQueryClassifier
├── config/  SearchEngineConfig (wires all components)
├── filter/  FilterChain
├── rank/    ScoreRanker, RecencyBoostRanker
└── index/   InMemoryIndex, KeywordRegistry

mcp-servers/src/server/learningresources/   ← DOMAIN ENGINES
├── vault/LearningSearchEngine.java    ← Tier-2: all learning resources
└── search/OfficialDocsSearchEngine.java ← Tier-3: official docs only
```


## Adding keywords to the search vocabulary

**File:** `mcp-servers/src/server/learningresources/vault/KeywordIndex.java`

The keyword index maps user words to concepts, categories, and difficulty levels.
Add a new entry to the relevant map:

```java
// In buildConceptMap():
map.put("kafka", ConceptArea.DISTRIBUTED_SYSTEMS);
map.put("event streaming", ConceptArea.DISTRIBUTED_SYSTEMS);
map.put("message queue", ConceptArea.DISTRIBUTED_SYSTEMS);

// In buildCategoryMap():
map.put("kafka", ResourceCategory.DEVOPS);
map.put("rabbitmq", ResourceCategory.DEVOPS);
```

After adding: rebuild `→ mcp-servers/build.ps1` and test with:
```
/resources discover "kafka beginner"
```

## Tuning scoring constants

**File:** `mcp-servers/src/server/learningresources/vault/RelevanceScorer.java`

The scoring constants control how much each match dimension contributes:

```java
static final int EXACT_TITLE_MATCH  = 100;  // Full query matches title exactly
static final int PARTIAL_TITLE_MATCH =  40;  // Title contains the query
static final int DESCRIPTION_MATCH  =  20;  // Description contains the query
static final int TAG_MATCH          =  15;  // A query word appears in tags
static final int CONCEPT_MATCH      =  25;  // Resource covers an inferred concept
static final int OFFICIAL_BOOST     =  15;  // Official/authoritative source
static final int DIFFICULTY_FIT     =  10;  // Difficulty matches target range
```

**To boost official sources more:** raise `OFFICIAL_BOOST` from 15 to 25.  
**To downweight difficulty:** lower `DIFFICULTY_FIT` from 10 to 5.  
**General rule:** change one constant at a time, test with 3-5 varied queries.

## Adding a new resource to the vault

**File:** `mcp-servers/src/server/learningresources/vault/providers/[Category]Resources.java`

```java
// In JavaResources.java, inside the all() method:
resources.add(new LearningResource.Builder()
    .id("project-reactor-docs")
    .title("Project Reactor Reference Guide")
    .url("https://projectreactor.io/docs/core/release/reference/")
    .description("Official reference for reactive programming with Spring WebFlux.")
    .type(ResourceType.DOCUMENTATION)
    .categories(List.of(ResourceCategory.JAVA))
    .conceptAreas(List.of(ConceptArea.CONCURRENCY, ConceptArea.API_DESIGN))
    .tags(List.of("reactive", "webflux", "spring", "reactor", "async"))
    .difficulty(DifficultyLevel.INTERMEDIATE)
    .freshness(ContentFreshness.ACTIVELY_MAINTAINED)
    .isOfficial(true)
    .isFree(true)
    .languageApplicability(LanguageApplicability.JAVA_CENTRIC)
    .build());
```

## Adding a new filter (e.g., "language-specific only")

Filters run before scoring to prune the candidate set. Use them for hard constraints:

```java
// In LearningSearchEngine or SearchEngineConfig
SearchFilter<LearningResource> javaOnly =
    (resource, ctx) -> resource.categories().contains(ResourceCategory.JAVA);

// Wire the filter in:
SearchEngineConfig<LearningResource> config = SearchEngineConfig.<LearningResource>builder()
    .filter(FilterChain.of(javaOnly, notArchived))
    // ...
```

## Using the new generic engine API

The new `LearningSearchEngine` exposes the generic `SearchEngine<LearningResource>` interface:

```java
// Old way (still works, unchanged):
ResourceDiscovery discovery = new ResourceDiscovery(vault);
DiscoveryResult result = discovery.discover("java concurrency");

// New way (generic API):
SearchEngine<LearningResource> engine = new LearningSearchEngine(vault);
SearchResult<LearningResource> result = engine.search("java concurrency");

// Using SearchContext for more control:
SearchResult<LearningResource> specific = engine.search(
    SearchContext.of("JUnit 5 docs", SearchMode.SPECIFIC));
```

---

**→ Next:** [Pro path: building a custom engine](#-pro-building-a-custom-search-engine)  
**→ See also:** [Module technical reference](../../mcp-servers/src/search/README.md) · [Java instructions](../instructions/java.instructions.md)

---
---

# 🔴 Pro: Building a Custom Search Engine

> Prerequisite: comfortable with Java generics, functional interfaces, and builder pattern.

## Architecture overview

The search engine is built from 8 pluggable layers:

```
SearchContext (input)
     │
┌────▼─────────────────────────────────────────────────────────────┐
│  ConfigurableSearchEngine (orchestrates the 5-phase pipeline)   │
│                                                                   │
│  Phase 1   QueryClassifier ──────────────────── SearchMode        │
│  Phase 2   SearchFilter<T> ──────────────────── prune candidates  │
│  Phase 3   ScoringStrategy<T>[mode] ─────────── score survivors  │
│  Phase 4   RankingStrategy<T> ───────────────── reorder scores   │
│  Phase 5   summaryBuilder + suggestionProvider ─ wrap result      │
│                                                                   │
│  Backed by: SearchIndex<T>                                        │
└──────────────────────────────────────────────────────────────────┘
     │
SearchResult<T> (output)
```

Each layer is an interface (or functional interface). Swap any layer without touching others.

## Interface summary

| Layer | Interface | Default impl |
|-------|-----------|-------------|
| Index | `SearchIndex<T>` | `InMemoryIndex<T>` |
| Classify | `QueryClassifier` | `KeywordQueryClassifier` |
| Score | `ScoringStrategy<T>` | `TextMatchScorer<T>` / `CompositeScorer<T>` |
| Filter | `SearchFilter<T>` | `SearchFilter.allowAll()` |
| Rank | `RankingStrategy<T>` | `ScoreRanker<T>` |
| Config | `SearchEngineConfig<T>` | Via `SearchEngineConfig.builder()` |
| Engine | `SearchEngine<T>` | `ConfigurableSearchEngine<T>` |

## Building an engine for a new MCP server

### 1. Define your document type

```java
// Use a Java record (immutable, naturally comparable)
public record ConfluencePage(
        String id,
        String title,
        String body,
        String space,
        List<String> labels,
        Instant lastModified,
        String author
) {}
```

### 2. Implement `SearchIndex<T>`

```java
// Option A: InMemoryIndex (good for < 5000 docs)
var index = new InMemoryIndex<ConfluencePage>();
pages.forEach(p -> index.add(p.id(), p));

// Option B: Custom index for live API calls
public class ConfluenceSearchIndex implements SearchIndex<ConfluencePage> {
    private final ConfluenceClient client;

    @Override
    public Collection<ConfluencePage> all() {
        return client.fetchAllPages(); // Live API call
    }
    // ... other methods
}
```

### 3. Define `ScoringStrategy<T>` per mode

```java
// SPECIFIC mode: weight title match heavily
ScoringStrategy<ConfluencePage> specificScorer = CompositeScorer.<ConfluencePage>builder()
    .add(TextMatchScorer.<ConfluencePage>builder()
            .titleExtractor(ConfluencePage::title)
            .bodyExtractor(ConfluencePage::body)
            .tagsExtractor(ConfluencePage::labels)
            .scores(new TextMatchScorer.Scores(150, 50, 25, 20, 10))
            .build(), 1.0)
    .build();

// VAGUE mode: add space-context and recency
ScoringStrategy<ConfluencePage> vagueScorer = CompositeScorer.<ConfluencePage>builder()
    .add(specificScorer, 1.0)
    .add((page, ctx) -> {
        // Recency bonus
        var ageDays = Duration.between(page.lastModified(), Instant.now()).toDays();
        return ageDays < 30 ? 20 : ageDays < 90 ? 10 : 0;
    }, 0.5)
    .build();

// EXPLORATORY mode: pick curated starting pages
ScoringStrategy<ConfluencePage> explorationScorer =
    (page, ctx) -> page.labels().contains("getting-started") ? 50 : 0;
```

### 4. Configure `QueryClassifier`

```java
var classifier = KeywordQueryClassifier.builder()
    .specificKeywords(List.of("PROJ-", "confluence page", "official",
                               "http", "runbook", "ADR"))
    .exploratoryKeywords(List.of("overview", "getting started",
                                  "what is", "introduction to"))
    .build();
```

### 5. Configure filters

```java
// Only show pages from specific spaces (context-driven)
SearchFilter<ConfluencePage> spaceFilter = (page, ctx) -> {
    var allowedSpace = ctx.getFilter("space", String.class);
    return allowedSpace == null || page.space().equals(allowedSpace);
};
```

### 6. Wire everything into `SearchEngineConfig`

```java
SearchEngineConfig<ConfluencePage> config = SearchEngineConfig.<ConfluencePage>builder()
    .index(new ConfluenceSearchIndex(client))
    .classifier(classifier)
    .scorer(SearchMode.SPECIFIC,    specificScorer)
    .scorer(SearchMode.VAGUE,       vagueScorer)
    .scorer(SearchMode.EXPLORATORY, explorationScorer)
    .filter(spaceFilter)
    .ranker(ScoreRanker.instance())
    .summaryBuilder((ctx, n) ->
        "Found " + n + " Confluence pages for '" + ctx.rawInput() + "'")
    .suggestions(ctx -> List.of(
        "Try searching a specific space: add space:MYSPACE",
        "Use search mode specific for PROJ-123 lookups"))
    .maxResults(10)
    .build();
```

### 7. Create the engine and use it

```java
SearchEngine<ConfluencePage> engine = new ConfigurableSearchEngine<>(config);

// From a tool handler:
SearchResult<ConfluencePage> result = engine.search(context);
result.items().forEach(si ->
    System.out.printf("[%3d] [%s] %s%n",
        si.score(), si.item().space(), si.item().title()));
```

## Implementing a domain-specific engine class

For complex domains with additional methods (like `LearningSearchEngine`):

```java
public final class ConfluenceSearchEngine implements SearchEngine<ConfluencePage> {

    private final ConfigurableSearchEngine<ConfluencePage> inner;

    public ConfluenceSearchEngine(final ConfluenceClient client) {
        this.inner = new ConfigurableSearchEngine<>(buildConfig(client));
    }

    @Override
    public SearchResult<ConfluencePage> search(final SearchContext context) {
        return inner.search(context);
    }

    // Domain-specific extras
    public List<ConfluencePage> searchBySpace(final String space, final String query) {
        var ctx = new SearchContext(query, null,
            Map.of("space", space), SearchContext.DEFAULT_MAX_RESULTS);
        return inner.search(ctx).items().stream()
            .map(ScoredItem::item).toList();
    }

    private static SearchEngineConfig<ConfluencePage> buildConfig(ConfluenceClient client) {
        // ... build and return config
    }
}
```

## Advanced: implementing BM25 scoring

BM25 (Best Match 25) is the gold standard for full-text search. Sketch implementation:

```java
public final class Bm25Scorer<T> implements ScoringStrategy<T> {
    private final Function<T, String> bodyExtractor;
    private final Map<String, Double> idfCache;    // precomputed inverse document frequency
    private static final double K1 = 1.5;
    private static final double B  = 0.75;
    private final double avgDocLength;

    @Override
    public int score(final T item, final SearchContext context) {
        final var body = bodyExtractor.apply(item).toLowerCase();
        final var words = body.split("\\W+");
        final var docLen = words.length;

        var total = 0.0;
        for (final var term : context.normalizedInput().split("\\s+")) {
            final var tf = countOccurrences(term, words);
            final var idf = idfCache.getOrDefault(term, 1.0);
            total += idf * (tf * (K1 + 1))
                    / (tf + K1 * (1 - B + B * docLen / avgDocLength));
        }
        return (int) (total * 10);  // scale to integer points
    }
}
```

## Testing your engine

```java
// Unit test: verify scoring order
SearchEngine<Article> engine = new ConfigurableSearchEngine<>(config);
SearchResult<Article> result = engine.search("java concurrency");

assertFalse(result.isEmpty());
assertEquals(SearchMode.VAGUE, result.classifiedMode());
assertEquals("Java Concurrency in Practice", result.items().get(0).item().title());
assertTrue(result.items().get(0).score() > result.items().get(1).score());

// Integration test: verify filter + rank together
result = engine.search(SearchContext.of("testing", SearchMode.SPECIFIC));
result.items().forEach(si ->
    assertTrue(si.item().tags().contains("testing"),
        "Unexpected item: " + si.item().title()));
```

---

## Cross-Reference

| Topic | Where |
|-------|-------|
| Full package Javadoc | `mcp-servers/src/search/README.md` |
| Learning Resources engine | `vault/LearningSearchEngine.java`, `vault/ResourceDiscovery.java` |
| LR keyword vocabulary | `vault/KeywordIndex.java` |
| LR scoring constants | `vault/RelevanceScorer.java` |
| Atlassian unified search | `handler/UnifiedSearchHandler.java` |
| Build instructions | `mcp-servers/README.md` → Quick Start |
| Java coding standards | `.github/instructions/java.instructions.md` |
| MCP server internals | `.github/docs/mcp-server-setup.md` |
| Navigation index | `.github/docs/navigation-index.md` |
