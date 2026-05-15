# search-engine

A standalone, generic, zero-dependency search engine library for Java 21+.

## Purpose

Provides a pluggable search infrastructure used by MCP servers (Learning Resources,
Atlassian) for resource discovery and content search.

## Key Features

- **Multi-mode search** — specific, vague, and exploratory query classification
- **BM25 scoring** — industry-standard relevance ranking
- **Fuzzy matching** — Levenshtein distance for typo tolerance
- **In-memory index** — fast startup, no external dependencies
- **Generic `<T>`** — parameterized for any domain object

## Package Structure

```text
search/
├── api/           Public interfaces (SearchEngine, SearchIndex, Scorer, etc.)
│   ├── algorithm/ Scoring strategies, tokenizers
│   ├── classify/  Query classification
│   ├── core/      SearchEngine, SearchResult, ScoreBreakdown
│   ├── filter/    Search filters
│   ├── index/     SearchIndex interface
│   └── rank/      Ranking strategies
└── engine/        Implementation
    ├── algorithm/ BM25, fuzzy matcher, composite scorer, tag scorer
    ├── classify/  Keyword-based query classifier
    ├── config/    SearchEngineConfig builder
    ├── core/      ConfigurableSearchEngine
    ├── filter/    FilterChain
    ├── index/     InMemoryIndex, KeywordRegistry
    └── rank/      ScoreRanker, RecencyBoostRanker
```

## Dependencies

None — pure Java 21+.

## Build

```bash
./gradlew :modules:search-engine:build
```
