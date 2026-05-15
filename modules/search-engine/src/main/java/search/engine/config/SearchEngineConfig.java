package search.engine.config;

import search.api.algorithm.ScoringStrategy;
import search.api.classify.QueryClassifier;
import search.api.classify.SearchMode;
import search.api.core.SearchContext;
import search.api.filter.SearchFilter;
import search.api.index.SearchIndex;
import search.api.rank.RankingStrategy;
import search.engine.classify.KeywordQueryClassifier;
import search.engine.index.InMemoryIndex;
import search.engine.rank.ScoreRanker;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Wiring configuration for a {@link search.engine.core.ConfigurableSearchEngine}.
 *
 * <p>This class is the single place where all components of the search pipeline are
 * assembled. Parameters are immutable once built.
 *
 * <h2>Pipeline overview</h2>
 * <ol>
 *   <li>{@link #classifier()} classifies the query into a {@link SearchMode}.</li>
 *   <li>{@link #filter()} removes ineligible documents.</li>
 *   <li>{@link #scorerFor(SearchMode)} scores remaining documents.</li>
 *   <li>{@link #ranker()} sorts and optionally re-ranks the scored list.</li>
 *   <li>{@link #maxResults()} trims the result list.</li>
 *   <li>{@link #summaryBuilder()} / {@link #suggestionProvider()} produce metadata.</li>
 * </ol>
 *
 * <h2>Minimal usage</h2>
 * <pre>{@code
 * var config = SearchEngineConfig.<Article>builder()
 *         .scorer(SearchMode.SPECIFIC, TextMatchScorer.titleOnly(Article::title))
 *         .scorer(SearchMode.VAGUE,    CompositeScorer.<Article>builder()
 *                     .add(TextMatchScorer.defaults(Article::title, Article::body))
 *                     .add(TagScorer.defaults(Article::tags))
 *                     .build())
 *         .scorer(SearchMode.EXPLORATORY, ScoringStrategy.constant(50))
 *         .build();
 * }</pre>
 *
 * @param <T> the document type
 *
 * @see search.engine.core.ConfigurableSearchEngine
 */
public final class SearchEngineConfig<T> {

    private final SearchIndex<T>                       index;
    private final QueryClassifier                      classifier;
    private final Map<SearchMode, ScoringStrategy<T>>  scorers;
    private final SearchFilter<T>                      filter;
    private final RankingStrategy<T>                   ranker;
    private final int                                  maxResults;
    private final BiFunction<SearchContext, Integer, String> summaryBuilder;
    private final Function<SearchContext, List<String>>      suggestionProvider;

    private SearchEngineConfig(final Builder<T> b) {
        this.index             = b.index;
        this.classifier        = b.classifier;
        this.scorers           = Map.copyOf(b.scorers);
        this.filter            = b.filter;
        this.ranker            = b.ranker;
        this.maxResults        = b.maxResults;
        this.summaryBuilder    = b.summaryBuilder;
        this.suggestionProvider = b.suggestionProvider;
    }

    public SearchIndex<T>                       index()              { return index; }
    public QueryClassifier                      classifier()         { return classifier; }
    public SearchFilter<T>                      filter()             { return filter; }
    public RankingStrategy<T>                   ranker()             { return ranker; }
    public int                                  maxResults()         { return maxResults; }
    public BiFunction<SearchContext, Integer, String> summaryBuilder(){ return summaryBuilder; }
    public Function<SearchContext, List<String>> suggestionProvider() { return suggestionProvider; }

    /**
     * Returns the scoring strategy for the given mode. Falls back to
     * {@link ScoringStrategy#zero()} if no scorer was registered for that mode.
     *
     * @param mode the classified search mode
     * @return the scorer (never null)
     */
    public ScoringStrategy<T> scorerFor(final SearchMode mode) {
        return scorers.getOrDefault(mode, ScoringStrategy.zero());
    }

    /** Returns {@code true} if a custom scorer is registered for {@code mode}. */
    public boolean hasScorerFor(final SearchMode mode) { return scorers.containsKey(mode); }

    /** Creates a fresh builder. */
    public static <T> Builder<T> builder() { return new Builder<>(); }

    // -------------------------------------------------------------------------
    // Builder
    // -------------------------------------------------------------------------

    /**
     * Fluent builder for {@link SearchEngineConfig}.
     *
     * @param <T> the document type
     */
    public static final class Builder<T> {

        // defaults
        private SearchIndex<T>                       index             = new InMemoryIndex<>();
        private QueryClassifier                      classifier        = QueryClassifier.alwaysVague();
        private final Map<SearchMode, ScoringStrategy<T>> scorers      = new EnumMap<>(SearchMode.class);
        private SearchFilter<T>                      filter            = SearchFilter.allowAll();
        private RankingStrategy<T>                   ranker            = ScoreRanker.instance();
        private int                                  maxResults        = SearchContext.DEFAULT_MAX_RESULTS;
        private BiFunction<SearchContext, Integer, String> summaryBuilder
                = (ctx, count) -> count + " result(s) for '" + ctx.normalizedInput() + "'";
        private Function<SearchContext, List<String>> suggestionProvider = ctx -> List.of();

        private Builder() {}

        /** Replaces the default {@link InMemoryIndex} with a custom index implementation. */
        public Builder<T> index(final SearchIndex<T> index) {
            this.index = Objects.requireNonNull(index); return this;
        }

        /** Sets the query classifier. Default: {@link QueryClassifier#alwaysVague()}. */
        public Builder<T> classifier(final QueryClassifier classifier) {
            this.classifier = Objects.requireNonNull(classifier); return this;
        }

        /**
         * Registers a scorer for a specific {@link SearchMode}.
         * Multiple calls with the same mode override the previous value.
         */
        public Builder<T> scorer(final SearchMode mode, final ScoringStrategy<T> scorer) {
            scorers.put(Objects.requireNonNull(mode), Objects.requireNonNull(scorer)); return this;
        }

        /**
         * Convenience: registers the <em>same</em> scorer for <em>all</em> {@link SearchMode}s.
         * Subsequent calls to {@link #scorer(SearchMode, ScoringStrategy)} can override
         * individual modes.
         */
        public Builder<T> defaultScorer(final ScoringStrategy<T> scorer) {
            Objects.requireNonNull(scorer);
            for (final var mode : SearchMode.values()) scorers.put(mode, scorer);
            return this;
        }

        /** Sets the document filter chain. Default: {@link SearchFilter#allowAll()}. */
        public Builder<T> filter(final SearchFilter<T> filter) {
            this.filter = Objects.requireNonNull(filter); return this;
        }

        /** Sets the ranking strategy. Default: {@link ScoreRanker#instance()}. */
        public Builder<T> ranker(final RankingStrategy<T> ranker) {
            this.ranker = Objects.requireNonNull(ranker); return this;
        }

        /**
         * Sets the maximum number of results to return.
         * Default: {@link SearchContext#DEFAULT_MAX_RESULTS}.
         */
        public Builder<T> maxResults(final int maxResults) {
            if (maxResults < 1) throw new IllegalArgumentException("maxResults must be ≥ 1");
            this.maxResults = maxResults; return this;
        }

        /**
         * Sets a custom summary builder.
         * The function receives the {@link SearchContext} and the result count.
         */
        public Builder<T> summaryBuilder(final BiFunction<SearchContext, Integer, String> fn) {
            this.summaryBuilder = Objects.requireNonNull(fn); return this;
        }

        /**
         * Sets a provider for "did you mean?" suggestions shown when results are empty.
         */
        public Builder<T> suggestionProvider(final Function<SearchContext, List<String>> fn) {
            this.suggestionProvider = Objects.requireNonNull(fn); return this;
        }

        /**
         * Builds the immutable configuration.
         *
         * @return a fully wired {@link SearchEngineConfig}
         */
        public SearchEngineConfig<T> build() { return new SearchEngineConfig<>(this); }
    }
}
