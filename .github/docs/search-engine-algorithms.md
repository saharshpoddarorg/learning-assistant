# Search Engine Algorithms — Developer Learning Guide

> **Who this is for:** Developers who want to understand _how_ the search engine
> scores documents, and _why_ each algorithm was chosen.
>
> **Related docs:**
> - [Architecture Overview](architecture-overview.md) — module structure, design patterns
> - [MCP Servers Architecture](mcp-servers-architecture.md) — how the protocol layer works

---

## 0. Module Reference

All algorithms live in the `search-engine` IntelliJ module (`search.engine.*` packages).
Their contracts (interfaces they implement) live in the `search-api` module (`search.api.*`).

```
search-api/src/search/api/          ← interfaces (ScoringStrategy, Tokenizer, ...)
search-engine/src/search/engine/    ← implementations (Bm25Scorer, TextMatchScorer, ...)
mcp-servers/src/server/...          ← domain engines (LearningSearchEngine, OfficialDocsSearchEngine)
```

**Code location format** used in this document:  
`search.engine.algorithm.Bm25Scorer` → file is `search-engine/src/search/engine/algorithm/Bm25Scorer.java`

---

## 1. The Scoring Problem

A search engine must answer: **given a query and a corpus of documents,
which documents are most relevant, and in what order?**

"Relevant" is multi-dimensional:
- Does the document's title mention the query terms?
- Are query terms frequent in the body?
- Is the document from an authoritative source?
- Is it recent?
- Does it match the user's skill level?

Our engine combines multiple scoring signals into one integer score per document.
Higher score = more relevant.

---

## 2. TextMatchScorer — Exact and Partial Title/Body Matching

### What it does

Checks whether the query (or parts of it) appear in the document's title or body.

### Score fields (defaults)

| Signal | Points | When awarded |
|---|---|---|
| `exactTitleMatch` | 100 | Query is the entire title (case-insensitive) |
| `partialTitleMatch` | 40 | Query is a substring of the title |
| `wordInTitleMatch` | 12 | Per query word found in the title |
| `tagMatch` | 15 | Per query word matching a tag |
| `bodyMatch` | 20 | Query is a substring of the body |
| `fuzzyMatch` | 8 | Fuzzy/partial per-word match |

### Profiles

```java
Scores.defaults()    // balanced; good for general search
Scores.titleHeavy()  // double title weight; ideal for official doc search
Scores.fullText()    // emphasises body text; good for long-form articles
```

### Bug fixed (session note)

The original code awarded `tagMatch` points for both tag hits _and_ per-word title
hits — a semantic error. They are now separate signals with distinct point values,
because a word appearing in the title is a different strength of evidence than a tag
matching.

### Code location
`search/engine/algorithm/TextMatchScorer.java`

---

## 3. TagScorer — Faceted Tag Matching

### What it does

Checks how many of the document's tags match query words.
Tags are structured metadata (e.g., `["java", "streams", "intermediate"]`),
so a tag match is a strong signal of topical relevance.

### Scoring model

```
for each query word:
    if any tag starts with or contains the word → +hitPoints (default: 15)
    if the entire tag equals the word           → +wholeTagBonus (default: 10)
```

### When to use

Combine with `TextMatchScorer` in a `CompositeScorer` for best results.
Tags alone can't detect body relevance, but they're very precise for category-style
searches ("show me all Java tutorials").

### Code location
`search/engine/algorithm/TagScorer.java`

---

## 4. CompositeScorer — Weighted Signal Combination

### What it does

Applies multiple `ScoringStrategy` instances and returns a weighted sum.

```java
CompositeScorer.<Article>builder()
    .add(titleScorer, 2.0)  // title signals count double
    .add(tagScorer,   1.0)  // tag signals at normal weight
    .add(bm25Scorer,  0.5)  // BM25 body signal at half weight
    .build()
```

### Design note

Each strategy can be an expert at one thing (title matching, tag matching, BM25).
The composite combines them into one score without any strategy needing knowledge
of the others. This is the **Strategy pattern** applied to scoring.

### Code location
`search/engine/algorithm/CompositeScorer.java`

---

## 5. Okapi BM25 — Industry-Standard Full-Text Ranking

BM25 (Best Match 25) is the ranking function used by Elasticsearch, Solr, and Lucene.
It improves on TF-IDF by handling term saturation and document length normalisation.

### Intuition

- **TF (term frequency):** A document mentioning "concurrency" 10 times is more relevant
  than one mentioning it once — but only up to a point. The 50th mention adds almost no extra signal.
- **IDF (inverse document frequency):** Common words ("the", "a", "java") should count less
  than rare, specific terms ("ConcurrentHashMap"). IDF rewards rare terms.
- **Length normalisation:** A long document naturally contains more occurrences.
  BM25 penalises long documents to prevent them from dominating just because of length.

### Formula

$$score(d, q) = \sum_{t \in q} IDF(t) \cdot \frac{TF(t,d) \cdot (k_1 + 1)}{TF(t,d) + k_1 \cdot (1 - b + b \cdot \frac{|d|}{avgdl})}$$

Where:
- $TF(t, d)$ = term frequency of term $t$ in document $d$
- $|d|$ = document length (in tokens)
- $avgdl$ = average document length across the corpus
- $k_1 = 1.5$ — controls term frequency saturation (higher = more weight to TF)
- $b = 0.75$ — controls length normalisation (0 = none, 1 = full)

**IDF (Robertson-Spärck Jones variant):**
$$IDF(t) = \ln\!\left(\frac{N - df_t + 0.5}{df_t + 0.5} + 1\right)$$

Where $N$ = corpus size and $df_t$ = number of documents containing term $t$.

### Why this variant of IDF?

The +1 inside the logarithm prevents IDF from going negative for very common terms,
making the score always non-negative.

### Usage in code

```java
// 1. Must call computeStats() before first use (needs corpus statistics)
Bm25Scorer<Article> bm25 = Bm25Scorer.<Article>builder()
        .textExtractor(Article::body)
        .tokenizer(new DefaultTokenizer())
        .build();

bm25.computeStats(config.index().all());  // call once after loading

// 2. Use as a ScoringStrategy
config.scorer(SearchMode.VAGUE, bm25);
```

### Best for

Large corpora (hundreds to thousands of documents) with long bodies.
Less effective for very short descriptions or title-only matching.

### Code location
`search/engine/algorithm/Bm25Scorer.java`

---

## 6. DefaultTokenizer — Stop-Word Removal

### What it does

Converts raw text into a list of meaningful tokens by:
1. Lowercasing
2. Splitting on non-word characters (`[\W_]+`)
3. Removing stop words ("a", "the", "and", "is", "of", ...)
4. Dropping tokens shorter than 2 characters

### Why stop words matter

Without stop-word removal, a query like "what is the best java book" gives TF-IDF credit
to "what", "is", "the" — terms that appear in almost every document and carry no signal.
Removing them lets the meaningful terms ("best", "java", "book") dominate.

### Stop words included

~40 common English words: a, an, the, and, or, but, is, are, was, were, be, been, being,
have, has, had, do, does, did, will, would, could, should, may, might, shall, can, of, in,
on, at, to, for, with, by, from, up, as, into, through, about, than, then, so.

### Code location
`search/engine/algorithm/DefaultTokenizer.java`

---

## 7. FuzzyMatcher — Typo Tolerance

### What it does

Provides partial and prefix matching to handle:
- Abbreviations: "conc" → "concurrency"
- Prefixes: "stream" → "streams"
- Substrings: "hash" → "HashMap", "hashCode"

### Implementation

Uses simple string operations (no Levenshtein distance):
- `hasSubstringMatch(term, text)` — does `text` contain `term`?
- `hasPrefixMatch(term, text)` — does any word in `text` start with `term`?
- `termFrequency(term, text)` — count occurrences of `term` in `text` (for BM25)

For true edit-distance fuzzy matching, this could be extended with a Levenshtein
implementation (left as an exercise — see `FuzzyMatcher.scoreWord()` for the extension point).

### Code location
`search/engine/algorithm/FuzzyMatcher.java`

---

## 8. RecencyBoostRanker — Time-Decay Ranking

### What it does

Adds a score bonus to recently-updated documents, then re-sorts.
Documents published today get the full bonus; older documents get a linearly decaying bonus.

### Time-decay model

```
age ≤ freshDays  → bonus = freshBonus
age ≤ staleDays  → bonus = freshBonus × (1 - (age - freshDays) / (staleDays - freshDays))
age > staleDays  → bonus = 0
```

### Default parameters

| Parameter | Default | Meaning |
|---|---|---|
| `freshDays` | 30 | Full bonus for first 30 days |
| `staleDays` | 365 | No bonus after 1 year |
| `freshBonus` | 20 | Maximum score addition |

### When to use

Combine with `ScoreRanker` using `thenRank()`:
```java
.ranker(ScoreRanker.<Article>instance()
       .thenRank(new RecencyBoostRanker<>(Article::updatedAt)))
```

This first sorts by relevance score, then re-boosts (and re-sorts) for recency.

### Code location
`search/engine/rank/RecencyBoostRanker.java`

---

## 9. QueryClassifier — Understanding User Intent

### Three modes

| Mode | User intent | Example queries |
|---|---|---|
| `SPECIFIC` | Knows exactly what they want | "JUnit 5 official docs", "http://..." |
| `VAGUE` | Knows the topic area | "something about testing", "java streams tutorial" |
| `EXPLORATORY` | Wants to learn, not sure where to start | "beginner stuff", "learn java" |

### KeywordQueryClassifier rules (priority order)

1. URL or quoted string → **SPECIFIC**
2. Contains a specific trigger word ("docs for", "official", "api ref") → **SPECIFIC**
3. Short query (≤ 5 words) + exploratory keyword ("learn", "beginner", "guide me") → **EXPLORATORY**
4. Very short (≤ 2 words) + not in known vocabulary → **EXPLORATORY**
5. Single word that is a difficulty marker ("beginner", "advanced") → **EXPLORATORY**
6. Everything else → **VAGUE**

### Why this matters

The same query "java streams" should return full-text results in VAGUE mode,
but "java streams official docs" should prioritise official Oracle documentation
in SPECIFIC mode. The classifier routes each query to the right scoring strategy.

### Code location
`search/engine/classify/KeywordQueryClassifier.java`

---

## 10. ScoreBreakdown — Debugging Scores

In test or debug mode, attach a `ScoreBreakdown` to each `ScoredItem` to see
why a document received its score:

```java
ScoreBreakdown breakdown = ScoreBreakdown.builder()
        .add("exactTitle",    100)
        .add("tagMatch",       30)   // 2 tags × 15 pts
        .add("bm25",           42)
        .build();

// total() = 172
// get("bm25") = 42
// all() → {exactTitle: 100, tagMatch: 30, bm25: 42}
```

### In production

Set `scoreBreakdown = null` (the two-arg `ScoredItem` constructor) to skip
allocation of breakdown objects and avoid GC pressure.

---

## 11. InMemoryIndex — Document Store

A `ConcurrentHashMap`-backed document store. Thread-safe for concurrent reads.

### Scaling beyond InMemoryIndex

Implement `SearchIndex<T>` and delegate to:
- **PostgreSQL full-text search** (`to_tsvector` / `to_tsquery`)
- **Elasticsearch** (`_search` API)
- **SQLite FTS5** (embedded, zero-network)

```java
public final class PostgresIndex<T> implements SearchIndex<T> {
    @Override public Collection<T> all() { return jdbc.queryForList(SQL, mapper); }
    // ...
}
```

Plug it in via `SearchEngineConfig.builder().index(new PostgresIndex<>())`.

---

## 12. KeywordRegistry — Query-to-Domain Inference

Maps user query words to domain model values (enums, categories, concept areas).

```java
KeywordRegistry<ConceptArea> registry = KeywordRegistry.<ConceptArea>builder()
        .register("concurrency",      ConceptArea.CONCURRENCY)
        .register("threads",          ConceptArea.CONCURRENCY)
        .register("virtual threads",  ConceptArea.CONCURRENCY)  // phrase
        .register("design patterns",  ConceptArea.DESIGN_PATTERNS)
        .build();

registry.inferFromQuery("learn java virtual threads")
// → [ConceptArea.CONCURRENCY]
```

Three-pass inference: per-word exact → phrase → 3-char prefix fallback.

---

## Putting It All Together — A Mental Model

```
User types:  "learn java concurrency beginner"
                │
                ▼
     KeywordQueryClassifier
         rule 3: "learn" + ≤5 words → EXPLORATORY
                │
                ▼
     SearchFilter: isOfficial == false? skip
                │  difficulty ≤ INTERMEDIATE? keep
                ▼
     ScoringStrategy for EXPLORATORY:
         ScoringStrategy.constant(50)   ← all resources equal in exploratory mode
                │
                ▼
     RecencyBoostRanker: add 0-20 pts based on age
                │
                ▼
     trim to 15 results
                │
                ▼
     SearchResult {
       mode: EXPLORATORY,
       items: [ScoredItem{score=70, ...}, ScoredItem{score=65, ...}, ...],
       summary: "15 result(s) for \"learn java concurrency beginner\"",
       suggestions: []
     }
```
