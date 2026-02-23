# search-engine — Algorithm Implementation Module

A standalone, generic, zero-dependency search engine for Java 21+ projects.

> **Module role:** Implements the contracts defined in `search-api`.
> Depends on `search-api`; provides implementations to `mcp-servers`.
>
> ```
> search-api  (interfaces)  ←  search-engine (this)  ←  mcp-servers (domain engines)
> ```
>
> See [architecture-overview.md](../.github/docs/architecture-overview.md) for the full module graph.

## Quick Start

```java
// 1. Build a config
SearchEngineConfig<Article> config = SearchEngineConfig.<Article>builder()
        .classifier(KeywordQueryClassifier.builder()
                .specificKeywords(List.of("docs", "official", "api ref"))
                .exploratoryKeywords(List.of("learn", "start", "beginner"))
                .build())
        .scorer(SearchMode.SPECIFIC,    TextMatchScorer.titleOnly(Article::title))
        .scorer(SearchMode.VAGUE,       CompositeScorer.<Article>builder()
                    .add(TextMatchScorer.defaults(Article::title, Article::body))
                    .add(TagScorer.defaults(Article::tags))
                    .build())
        .scorer(SearchMode.EXPLORATORY, ScoringStrategy.constant(50))
        .ranker(ScoreRanker.instance())
        .maxResults(15)
        .build();

// 2. Load your documents
config.index().add("a-001", articleA);
config.index().add("a-002", articleB);

// 3. Search
SearchEngine<Article> engine = new ConfigurableSearchEngine<>(config);
SearchResult<Article> result = engine.search("java concurrency");
result.items().forEach(si -> System.out.printf("%3d  %s%n", si.score(), si.item().title()));
```

---

## Module Layout

```
search-engine/
├── src/
│   ├── search/api/          ← PUBLIC API — depend only on these
│   │   ├── core/            SearchEngine, SearchContext, SearchResult, ScoredItem, ScoreBreakdown
│   │   ├── classify/        SearchMode, QueryClassifier
│   │   ├── algorithm/       ScoringStrategy, Tokenizer
│   │   ├── filter/          SearchFilter
│   │   ├── rank/            RankingStrategy
│   │   └── index/           SearchIndex
│   └── search/engine/       ← IMPLEMENTATIONS — do not import these from other modules
│       ├── core/            ConfigurableSearchEngine
│       ├── classify/        KeywordQueryClassifier
│       ├── algorithm/       TextMatchScorer, Bm25Scorer, TagScorer, CompositeScorer,
│       │                    FuzzyMatcher, DefaultTokenizer
│       ├── filter/          FilterChain
│       ├── rank/            ScoreRanker, RecencyBoostRanker
│       ├── index/           InMemoryIndex, KeywordRegistry
│       └── config/          SearchEngineConfig
```

**Key rule:** Callers (MCP servers, handlers) import from `search.api.*` only.
Implementations (`search.engine.*`) are internal and may change.

---

## The Five-Phase Pipeline

Every `ConfigurableSearchEngine.search(context)` call runs these phases in order:

```
User Input
    │
    ▼
[1] CLASSIFY — QueryClassifier.classify(normalizedInput)
               Determines SearchMode: SPECIFIC | VAGUE | EXPLORATORY
    │
    ▼
[2] FILTER   — SearchFilter.test(item, context)  [for each document]
               Discards items that don't belong in this search (e.g., wrong category)
    │
    ▼
[3] SCORE    — ScoringStrategy.score(item, context)  [for each item passing filter]
               Assigns a relevance score; items with score ≤ 0 are dropped
    │
    ▼
[4] RANK     — RankingStrategy.rank(scoredItems, context)
               Sorts and optionally re-ranks (e.g., adds recency boost)
    │
    ▼
[5] TRIM     — take top min(config.maxResults, context.maxResults) items
               Build SearchResult with summary string
    │
    ▼
SearchResult<T>
```

Subclasses can intercept via `preSearch(context)` and `postSearch(context, result)` hooks
without reimplementing the full pipeline.

---

## Tier Hierarchy

The engine is designed as a stacked tier system. Each tier adds domain-specific behaviour
on top of the generic tier below.

```
Tier 0  SearchEngine<T>             search.api.core       — pure interface; callers use this
Tier 1  ConfigurableSearchEngine<T> search.engine.core    — 5-phase pipeline; override hooks
Tier 2  LearningSearchEngine        server.learningresources.vault  — LR domain; wraps ResourceDiscovery
Tier 3  OfficialDocsSearchEngine    server.learningresources.search — official-only pre-filter
```

To create a new Tier-N engine:
1. Extend `ConfigurableSearchEngine<YourType>` (or any Tier above it)
2. Build a `SearchEngineConfig<YourType>` with your scorers / filters
3. Override `preSearch` / `postSearch` for domain-specific enrichment

---

## Key Components

### SearchContext

All searches start here. Build with:
```java
SearchContext.of("java streams")           // normalised, DEFAULT_MAX_RESULTS
SearchContext.of("java streams", 20)       // custom result limit
SearchContext.of("java streams", SearchMode.SPECIFIC, Map.of("official", true), 10)
```

Key accessors:
- `normalizedInput()` — lowercase, stripped query
- `hasForcedMode()` — `true` if caller forced a specific mode
- `getFilter("key", Boolean.class)` — type-safe filter map access

### SearchResult

Returned by every `SearchEngine.search()` call:
```java
result.items()           // List<ScoredItem<T>>, score-descending
result.classifiedMode()  // which SearchMode was used
result.suggestions()     // "did you mean?" list (populated on empty results)
result.summary()         // human-readable count string
result.isEmpty()         // convenience boolean
result.topScore()        // OptionalInt of highest score
```

### ScoredItem

A document paired with its score and optional debug breakdown:
```java
ScoredItem<Article> si = result.items().get(0);
si.item()           // the document
si.score()          // total relevance score (higher = more relevant)
si.hasBreakdown()   // true if ScoreBreakdown was attached (debug/test mode)
si.withBoost(10)    // creates a new ScoredItem with score + 10
```

---

## Scoring Algorithms

See [algorithms.md](../docs/search-engine-algorithms.md) for in-depth explanations.

| Algorithm | Class | Best for |
|---|---|---|
| Exact / partial title match | `TextMatchScorer` | Short, named queries |
| Tag / keyword overlap | `TagScorer` | Faceted / tagged content |
| Composite (weighted sum) | `CompositeScorer` | Combining multiple signals |
| Okapi BM25 | `Bm25Scorer` | Long-form bodies, large corpora |
| Constant / zero | `ScoringStrategy.constant(n)` | EXPLORATORY mode baseline |
| Recency decay | `RecencyBoostRanker` | Time-sensitive results |

---

## Building

```powershell
# Standalone (search-engine only)
cd search-engine
.\build.ps1

# Full project (includes mcp-servers)
cd mcp-servers
.\build.ps1            # automatically picks up ../search-engine/src/
.\build.ps1 -Clean     # clean rebuild
```
