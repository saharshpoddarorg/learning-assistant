package search.engine.rank;

import search.api.core.ScoredItem;
import search.api.core.SearchContext;
import search.api.rank.RankingStrategy;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * A post-scoring ranker that boosts recently-updated documents.
 *
 * <p>After the primary score is applied, this ranker adds a time-decay bonus
 * proportional to how recent the document is. Documents updated in the last
 * {@code freshDays} days receive the full {@code freshBonus}; older content
 * receives a linearly decayed bonus, reaching 0 at {@code staleDays}.
 *
 * <h2>Time-decay model</h2>
 * <pre>
 *   if ageDays ≤ freshDays  → effectiveBonus = freshBonus
 *   if ageDays ≤ staleDays  → effectiveBonus = freshBonus × (1 - (ageDays - freshDays) / (staleDays - freshDays))
 *   if ageDays &gt; staleDays → effectiveBonus = 0
 * </pre>
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * // Compose after ScoreRanker for a two-pass ranking
 * RankingStrategy<Article> ranker =
 *         ScoreRanker.<Article>instance()
 *                    .thenRank(new RecencyBoostRanker<>(Article::lastModified, 30, 90, 20));
 * }</pre>
 *
 * @param <T> the document type
 *
 * @see ScoreRanker
 * @see search.api.rank.RankingStrategy
 */
public final class RecencyBoostRanker<T> implements RankingStrategy<T> {

    private final Function<T, Instant> timestampExtractor;
    private final int freshDays;
    private final int staleDays;
    private final int freshBonus;

    /**
     * Creates a ranker with custom freshness parameters.
     *
     * @param timestampExtractor extracts the last-updated timestamp from a document
     * @param freshDays          documents newer than this many days receive the full bonus
     * @param staleDays          documents older than this many days receive no bonus
     * @param freshBonus         the maximum recency bonus in points
     */
    public RecencyBoostRanker(final Function<T, Instant> timestampExtractor,
                               final int freshDays, final int staleDays, final int freshBonus) {
        this.timestampExtractor = Objects.requireNonNull(timestampExtractor, "timestampExtractor must not be null");
        if (freshDays < 0 || staleDays <= freshDays) {
            throw new IllegalArgumentException("staleDays must be > freshDays ≥ 0");
        }
        this.freshDays  = freshDays;
        this.staleDays  = staleDays;
        this.freshBonus = Math.max(0, freshBonus);
    }

    /**
     * Creates a ranker with sensible defaults:
     * fresh ≤ 30 days, stale &gt; 365 days, bonus = 20 points.
     *
     * @param timestampExtractor extracts the last-updated timestamp
     */
    public RecencyBoostRanker(final Function<T, Instant> timestampExtractor) {
        this(timestampExtractor, 30, 365, 20);
    }

    @Override
    public List<ScoredItem<T>> rank(final List<ScoredItem<T>> items, final SearchContext context) {
        if (items == null || items.isEmpty()) return List.of();

        final var now = Instant.now();
        final var boosted = new ArrayList<ScoredItem<T>>(items.size());

        for (final var si : items) {
            final var timestamp = timestampExtractor.apply(si.item());
            final var bonus = timestamp != null ? computeBonus(timestamp, now) : 0;
            boosted.add(bonus > 0 ? si.withBoost(bonus) : si);
        }

        // Re-sort after boost application
        boosted.sort(Comparator.comparingInt(ScoredItem<T>::score).reversed());
        return List.copyOf(boosted);
    }

    private int computeBonus(final Instant timestamp, final Instant now) {
        final var ageDays = ChronoUnit.DAYS.between(timestamp, now);
        if (ageDays <= freshDays) return freshBonus;
        if (ageDays >= staleDays) return 0;
        // Linear decay from freshBonus to 0 between freshDays and staleDays
        final double decayRatio = (double) (ageDays - freshDays) / (staleDays - freshDays);
        return (int) (freshBonus * (1.0 - decayRatio));
    }
}
