package search.api.core;

/**
 * Top-level contract for the pluggable search engine.
 *
 * <p>A {@code SearchEngine<T>} is a stateless, thread-safe service that accepts a
 * {@link SearchContext} and returns a {@link SearchResult}. Implementations are free
 * to classify the query, score candidates, apply filters, and rank results in any way.
 *
 * <h2>Tier model</h2>
 * <pre>
 * search.api.core.SearchEngine&lt;T&gt;          ← this interface (Tier 0 — API)
 *   └── search.engine.core.ConfigurableSearchEngine&lt;T&gt;  (Tier 1 — base impl)
 *         └── server.learningresources.search.LearningSearchEngine  (Tier 2 — domain)
 *               └── server.learningresources.search.OfficialDocsSearchEngine  (Tier 3 — specialized)
 * </pre>
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * SearchEngine<LearningResource> engine = new LearningSearchEngine(vault);
 * SearchResult<LearningResource> result = engine.search("java concurrency beginner");
 * result.items().forEach(si -> System.out.printf("[%3d] %s%n", si.score(), si.item().title()));
 * }</pre>
 *
 * @param <T> the document type (e.g., {@code LearningResource})
 *
 * @see SearchContext
 * @see SearchResult
 * @see search.engine.core.ConfigurableSearchEngine
 */
public interface SearchEngine<T> {

    /**
     * Executes a search and returns scored, ranked, summarised results.
     *
     * @param context the search context (never null)
     * @return a non-null result; may be empty but never null
     */
    SearchResult<T> search(SearchContext context);

    /**
     * Convenience overload: creates a {@link SearchContext} with all defaults.
     *
     * @param rawInput the user's query (never null)
     * @return a non-null result
     */
    default SearchResult<T> search(final String rawInput) {
        return search(SearchContext.of(rawInput));
    }
}
