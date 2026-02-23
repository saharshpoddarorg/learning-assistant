package search.engine.core;

import search.api.classify.SearchMode;
import search.api.core.ScoredItem;
import search.api.core.SearchContext;
import search.api.core.SearchEngine;
import search.api.core.SearchResult;
import search.engine.config.SearchEngineConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Tier-1 reference implementation of {@link SearchEngine} — a fully configurable,
 * five-phase search pipeline.
 *
 * <h2>Pipeline phases</h2>
 * <ol>
 *   <li><strong>Classify</strong> — determines the {@link SearchMode} (SPECIFIC / VAGUE /
 *       EXPLORATORY), honouring any forced mode in the {@link SearchContext}.</li>
 *   <li><strong>Filter</strong> — discards documents that fail the configured
 *       {@link search.api.filter.SearchFilter}.</li>
 *   <li><strong>Score</strong> — runs the mode-specific {@link search.api.algorithm.ScoringStrategy}
 *       over every surviving document; documents with score ≤ 0 are dropped.</li>
 *   <li><strong>Rank</strong> — passes the scored list through the configured
 *       {@link search.api.rank.RankingStrategy} (default: score-descending).</li>
 *   <li><strong>Trim & wrap</strong> — trims to {@code maxResults}, builds the summary
 *       string and assembles the final {@link SearchResult}.</li>
 * </ol>
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * // 1. Build the config
 * SearchEngineConfig<Article> config = SearchEngineConfig.<Article>builder()
 *         .classifier(myClassifier)
 *         .scorer(SearchMode.SPECIFIC,    TextMatchScorer.titleOnly(Article::title))
 *         .scorer(SearchMode.VAGUE,       compositeScorer)
 *         .scorer(SearchMode.EXPLORATORY, ScoringStrategy.constant(50))
 *         .ranker(ScoreRanker.<Article>instance()
 *                     .thenRank(new RecencyBoostRanker<>(Article::updatedAt)))
 *         .build();
 *
 * // 2. Load documents
 * config.index().add("a-001", articleA);
 * config.index().add("a-002", articleB);
 *
 * // 3. Query
 * SearchEngine<Article> engine = new ConfigurableSearchEngine<>(config);
 * SearchResult<Article> result = engine.search("java concurrency");
 * }</pre>
 *
 * <h2>Extension (Tier 2+)</h2>
 * Subclass this engine and override {@link #preSearch(SearchContext)} /
 * {@link #postSearch(SearchContext, SearchResult)} hooks to add domain-specific behaviour
 * without reimplementing the full pipeline.
 *
 * @param <T> the document type
 *
 * @see SearchEngineConfig
 * @see search.api.core.SearchEngine
 */
public class ConfigurableSearchEngine<T> implements SearchEngine<T> {

    private static final Logger LOGGER = Logger.getLogger(ConfigurableSearchEngine.class.getName());

    /** The wired configuration driving this engine. */
    protected final SearchEngineConfig<T> config;

    /**
     * Creates a new engine wired to the given configuration.
     *
     * @param config the fully-built engine configuration
     */
    public ConfigurableSearchEngine(final SearchEngineConfig<T> config) {
        this.config = Objects.requireNonNull(config, "config must not be null");
    }

    @Override
    public final SearchResult<T> search(final SearchContext context) {
        Objects.requireNonNull(context, "context must not be null");

        // Allow subclasses to intercept before the pipeline
        final var effectiveContext = preSearch(context);

        final var result = runPipeline(effectiveContext);

        // Allow subclasses to post-process the result
        return postSearch(effectiveContext, result);
    }

    // -------------------------------------------------------------------------
    // Pipeline
    // -------------------------------------------------------------------------

    private SearchResult<T> runPipeline(final SearchContext context) {
        // Phase 1: classify
        final var mode = classifyQuery(context);
        LOGGER.fine(() -> "Search [" + mode + "] for '" + context.normalizedInput() + "'");

        // Phase 2: filter
        final var candidates = filterDocuments(context);
        if (candidates.isEmpty()) {
            return buildEmptyResult(mode, context);
        }

        // Phase 3: score  (drop zero-score documents)
        final var scored = scoreDocuments(candidates, context, mode);
        if (scored.isEmpty()) {
            return buildEmptyResult(mode, context);
        }

        // Phase 4: rank
        final var ranked = config.ranker().rank(scored, context);

        // Phase 5: trim + wrap
        return buildResult(mode, ranked, context);
    }

    // -------------------------------------------------------------------------
    // Phase implementations
    // -------------------------------------------------------------------------

    /** Phase 1: resolve query mode (forced or classified). */
    private SearchMode classifyQuery(final SearchContext context) {
        if (context.hasForcedMode()) return context.forcedMode();
        return config.classifier().classify(context.normalizedInput());
    }

    /** Phase 2: collect documents that pass the configured filter. */
    private List<T> filterDocuments(final SearchContext context) {
        final var filter = config.filter();
        final var all    = config.index().all();
        if (all.isEmpty()) return List.of();

        final var survivors = new ArrayList<T>(Math.min(all.size(), 512));
        for (final var doc : all) {
            if (filter.test(doc, context)) survivors.add(doc);
        }
        return survivors;
    }

    /** Phase 3: score and discard zero-score documents. */
    private List<ScoredItem<T>> scoreDocuments(final List<T> documents,
                                                final SearchContext context,
                                                final SearchMode mode) {
        final var scorer = config.scorerFor(mode);
        final var result = new ArrayList<ScoredItem<T>>(documents.size());
        for (final var doc : documents) {
            final var score = scorer.score(doc, context);
            if (score > 0) result.add(new ScoredItem<>(doc, score));
        }
        return result;
    }

    /** Phase 5: trim to maxResults and wrap into a {@link SearchResult}. */
    private SearchResult<T> buildResult(final SearchMode mode,
                                         final List<ScoredItem<T>> ranked,
                                         final SearchContext context) {
        final int limit = Math.min(config.maxResults(), context.maxResults());
        final var trimmed = ranked.size() > limit ? ranked.subList(0, limit) : ranked;
        final var summary = config.summaryBuilder().apply(context, trimmed.size());
        return new SearchResult<>(mode, List.copyOf(trimmed), List.of(), summary);
    }

    private SearchResult<T> buildEmptyResult(final SearchMode mode, final SearchContext context) {
        final var suggestions = config.suggestionProvider().apply(context);
        final var summary     = config.summaryBuilder().apply(context, 0);
        return SearchResult.emptyWithSuggestions(mode, summary, suggestions);
    }

    // -------------------------------------------------------------------------
    // Extension hooks
    // -------------------------------------------------------------------------

    /**
     * Hook called before the pipeline starts.
     * Default implementation returns {@code context} unchanged.
     * Subclasses may mutate (wrap) the context to inject domain-specific data.
     *
     * @param context the original search context
     * @return the context to use for the remainder of the pipeline
     */
    protected SearchContext preSearch(final SearchContext context) { return context; }

    /**
     * Hook called after the pipeline completes.
     * Default implementation returns {@code result} unchanged.
     * Subclasses may enrich the result (e.g., add domain-specific suggestions).
     *
     * @param context the effective context used during the pipeline
     * @param result  the pipeline output
     * @return the final result returned to the caller
     */
    protected SearchResult<T> postSearch(final SearchContext context,
                                          final SearchResult<T> result) {
        return result;
    }

    /** Returns the engine configuration (read-only access for subclasses). */
    protected SearchEngineConfig<T> config() { return config; }
}
