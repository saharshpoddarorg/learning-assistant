package search.api.rank;

import search.api.core.ScoredItem;
import search.api.core.SearchContext;

import java.util.List;

/**
 * Reorders scored items after scoring and before the result is assembled.
 *
 * <p>Ranking runs in Phase 4 of the engine pipeline. The default implementation
 * ({@link search.engine.rank.ScoreRanker}) sorts by score descending. Custom
 * rankers can boost recently-updated documents, prefer official sources, or
 * implement personalised ordering.
 *
 * <h2>Default</h2>
 * {@link search.engine.rank.ScoreRanker#instance()} is the stable score-descending sorter.
 * Add a recency boost after it:
 * <pre>{@code
 * RankingStrategy<Article> ranker =
 *         ScoreRanker.<Article>instance()
 *                    .thenRank(new RecencyBoostRanker<>(Article::lastModified));
 * }</pre>
 *
 * @param <T> the document type
 *
 * @see search.engine.rank.ScoreRanker
 * @see search.engine.rank.RecencyBoostRanker
 */
@FunctionalInterface
public interface RankingStrategy<T> {

    /**
     * Re-orders {@code items} in whatever way is appropriate for the engine.
     *
     * @param items   the scored items to rank (never null; may be modified in-place or a new list returned)
     * @param context the search context (never null)
     * @return an ordered list (never null)
     */
    List<ScoredItem<T>> rank(List<ScoredItem<T>> items, SearchContext context);

    /**
     * Returns a composed strategy that applies {@code this}, then applies {@code next}
     * to the result — enabling multi-step ranking.
     *
     * @param next the ranking step to apply after this one
     * @return the composed strategy
     */
    default RankingStrategy<T> thenRank(final RankingStrategy<T> next) {
        return (items, ctx) -> next.rank(this.rank(items, ctx), ctx);
    }
}
