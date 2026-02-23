package search.api.classify;

/**
 * Maps a normalised user query string to a {@link SearchMode}.
 *
 * <p>Implement this interface to control how the engine interprets the user's intent.
 * The classifier is invoked in Phase 1 of the
 * {@link search.engine.core.ConfigurableSearchEngine} pipeline, before any scoring.
 *
 * <h2>Built-in implementations</h2>
 * <ul>
 *   <li>{@link search.engine.classify.KeywordQueryClassifier} — keyword-list-driven, configurable</li>
 *   <li>{@link #alwaysVague()} — treats all queries as topic searches</li>
 *   <li>{@link #fixed(SearchMode)} — locks a specific mode (useful for single-mode engines)</li>
 * </ul>
 *
 * <h2>Composing classifiers</h2>
 * <pre>{@code
 * QueryClassifier composite = input ->
 *         input.startsWith("http") ? SearchMode.SPECIFIC : classifier.classify(input);
 * }</pre>
 *
 * @see KeywordQueryClassifier
 * @see SearchMode
 */
@FunctionalInterface
public interface QueryClassifier {

    /**
     * Classifies the normalised input.
     *
     * @param normalizedInput the user's query, already lowercased and stripped
     * @return the inferred search mode (never null)
     */
    SearchMode classify(String normalizedInput);

    // ─── Built-in factory classifiers ─────────────────────────────

    /** A classifier that always returns {@link SearchMode#VAGUE}. */
    static QueryClassifier alwaysVague() { return input -> SearchMode.VAGUE; }

    /** A classifier that always returns {@link SearchMode#SPECIFIC}. */
    static QueryClassifier alwaysSpecific() { return input -> SearchMode.SPECIFIC; }

    /** A classifier that always returns {@link SearchMode#EXPLORATORY}. */
    static QueryClassifier alwaysExploratory() { return input -> SearchMode.EXPLORATORY; }

    /** A classifier that always returns the given fixed mode. */
    static QueryClassifier fixed(final SearchMode mode) {
        if (mode == null) throw new IllegalArgumentException("mode must not be null");
        return input -> mode;
    }
}
