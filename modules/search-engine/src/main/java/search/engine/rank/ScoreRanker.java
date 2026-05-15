package search.engine.rank;

import search.api.core.ScoredItem;
import search.api.core.SearchContext;
import search.api.rank.RankingStrategy;

import java.util.Comparator;
import java.util.List;

/**
 * The default {@link RankingStrategy}: sorts scored items by score descending (stable sort).
 *
 * <p>Items with equal scores preserve their relative order from the scoring phase.
 * This is the standard ranking strategy and is almost always a good default.
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * // In SearchEngineConfig:
 * .ranker(ScoreRanker.instance())
 *
 * // Compose with recency boost:
 * .ranker(ScoreRanker.<Article>instance().thenRank(new RecencyBoostRanker<>(Article::updatedAt)))
 * }</pre>
 *
 * <p>This class is a stateless singleton.
 *
 * @param <T> the document type
 *
 * @see RecencyBoostRanker
 */
public final class ScoreRanker<T> implements RankingStrategy<T> {

    @SuppressWarnings("rawtypes")
    private static final ScoreRanker INSTANCE = new ScoreRanker<>();

    private ScoreRanker() {}

    /**
     * Returns the singleton instance cast to the requested type.
     *
     * @param <T> the document type
     * @return the singleton ranker
     */
    @SuppressWarnings("unchecked")
    public static <T> ScoreRanker<T> instance() { return (ScoreRanker<T>) INSTANCE; }

    @Override
    public List<ScoredItem<T>> rank(final List<ScoredItem<T>> items, final SearchContext context) {
        if (items == null || items.isEmpty()) return List.of();
        return items.stream()
                .sorted(Comparator.comparingInt(ScoredItem<T>::score).reversed())
                .toList();
    }
}
