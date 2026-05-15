package search.api.core;

import java.util.Objects;

/**
 * A document paired with its computed relevance score.
 *
 * <h2>Score conventions</h2>
 * <ul>
 *   <li>Scores are non-negative integers. Higher is better.</li>
 *   <li>Score 0 means "not relevant". Items with score 0 are dropped before ranking.</li>
 *   <li>Scores are comparable only within the same query (no fixed maximum).</li>
 *   <li>An optional {@link ScoreBreakdown} can be attached for debugging.</li>
 * </ul>
 *
 * @param item            the matched document (never null)
 * @param score           the relevance score (≥ 0; negative values are clamped to 0)
 * @param scoreBreakdown  optional scoring explanation; null in production mode
 * @param <T>             the document type
 *
 * @see SearchResult
 * @see search.api.algorithm.ScoringStrategy
 */
public record ScoredItem<T>(T item, int score, ScoreBreakdown scoreBreakdown) {

    /** Creates a scored item with no breakdown (production mode). */
    public ScoredItem(final T item, final int score) {
        this(item, score, null);
    }

    /** Validates and clamps the score to ≥ 0. */
    public ScoredItem {
        Objects.requireNonNull(item, "item must not be null");
        if (score < 0) {
            score = 0;
        }
    }

    /** Returns {@code true} if this item has a positive relevance score. */
    public boolean hasScore() { return score > 0; }

    /**
     * Returns a new {@code ScoredItem} with the score adjusted by {@code boost}.
     *
     * @param boost the points to add (may be negative for a penalty)
     * @return a new scored item (score clamped to ≥ 0)
     */
    public ScoredItem<T> withBoost(final int boost) {
        return new ScoredItem<>(item, Math.max(0, score + boost), scoreBreakdown);
    }

    /** Returns {@code true} if a score breakdown is attached. */
    public boolean hasBreakdown() { return scoreBreakdown != null; }
}
