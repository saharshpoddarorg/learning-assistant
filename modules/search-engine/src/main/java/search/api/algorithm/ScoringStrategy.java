package search.api.algorithm;

import search.api.core.SearchContext;

/**
 * Computes a non-negative integer relevance score for a single document.
 *
 * <p>A {@code ScoringStrategy} is {@code @FunctionalInterface} — it can be a lambda, a method
 * reference, or a named class. Implementations must be stateless and thread-safe.
 *
 * <h2>Composing strategies</h2>
 * <pre>{@code
 * ScoringStrategy<Article> combined = CompositeScorer.<Article>builder()
 *         .add(titleScorer, 2.0)    // 2x weight for title matches
 *         .add(tagScorer,   1.0)
 *         .add(recencyBonus, 0.5)
 *         .build();
 * }</pre>
 *
 * <h2>Score conventions</h2>
 * <ul>
 *   <li>Must return ≥ 0; negative values are treated as 0 by callers.</li>
 *   <li>A score of 0 means "not relevant for this context". The engine drops such items.</li>
 *   <li>The scale is relative — there is no required maximum.</li>
 * </ul>
 *
 * @param <T> the document type
 *
 * @see search.engine.algorithm.CompositeScorer
 * @see search.engine.algorithm.TextMatchScorer
 * @see search.engine.algorithm.Bm25Scorer
 */
@FunctionalInterface
public interface ScoringStrategy<T> {

    /**
     * Scores a document against the search context.
     *
     * @param item    the document to score (never null)
     * @param context the search context (never null)
     * @return the relevance score (≥ 0)
     */
    int score(T item, SearchContext context);

    // ─── Factory helpers ───────────────────────────────────────────

    /** A strategy that always returns 0 (useful as a no-op default). */
    static <T> ScoringStrategy<T> zero() { return (item, ctx) -> 0; }

    /** A strategy that always returns a constant score (useful for baseline boost). */
    static <T> ScoringStrategy<T> constant(final int points) { return (item, ctx) -> points; }

    /**
     * Returns a new strategy that adds a constant to this strategy's score.
     *
     * @param constant the bonus points (may be negative for a penalty)
     * @return the boosted strategy
     */
    default ScoringStrategy<T> withConstantBoost(final int constant) {
        return (item, ctx) -> Math.max(0, this.score(item, ctx) + constant);
    }

    /**
     * Returns a new strategy that multiplies this strategy's score by a factor.
     *
     * @param factor the multiplier (&gt; 0)
     * @return the scaled strategy
     */
    default ScoringStrategy<T> scaledBy(final double factor) {
        return (item, ctx) -> (int) Math.max(0, this.score(item, ctx) * factor);
    }
}
