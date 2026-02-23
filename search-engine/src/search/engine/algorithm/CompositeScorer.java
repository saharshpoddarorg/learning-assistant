package search.engine.algorithm;

import search.api.algorithm.ScoringStrategy;
import search.api.core.SearchContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Combines multiple {@link ScoringStrategy} instances into a single weighted sum.
 *
 * <p>Each strategy's raw score is multiplied by its weight. Zero-scoring strategies
 * contribute 0 regardless of weight. The final result is truncated to {@code int}.
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * ScoringStrategy<Article> combined = CompositeScorer.<Article>builder()
 *         .add(textMatchScorer, 1.5)        // 1.5x weight — text relevance matters most
 *         .add(tagScorer, 1.0)              // 1x weight for tag hits
 *         .add(bm25Scorer, 2.0)             // 2x weight for BM25 IR score
 *         .add(item -> item.isOfficial() ? 15 : 0, (i,ctx) -> ..., 0.8)
 *         .build();
 * }</pre>
 *
 * @param <T> the document type
 *
 * @see TextMatchScorer
 * @see TagScorer
 * @see Bm25Scorer
 */
public final class CompositeScorer<T> implements ScoringStrategy<T> {

    private final List<WeightedStrategy<T>> strategies;

    private CompositeScorer(final List<WeightedStrategy<T>> strategies) {
        this.strategies = List.copyOf(strategies);
    }

    @Override
    public int score(final T item, final SearchContext context) {
        Objects.requireNonNull(item,    "item must not be null");
        Objects.requireNonNull(context, "context must not be null");

        var total = 0.0;
        for (final var ws : strategies) {
            final var raw = ws.strategy().score(item, context);
            if (raw > 0) {
                total += raw * ws.weight();
            }
        }
        return (int) total;
    }

    /** Returns the number of child strategies in this composite. */
    public int strategyCount() { return strategies.size(); }

    public static <T> Builder<T> builder() { return new Builder<>(); }

    /** Fluent builder for {@link CompositeScorer}. */
    public static final class Builder<T> {

        private final List<WeightedStrategy<T>> strategies = new ArrayList<>();

        private Builder() {}

        /**
         * Adds a strategy with a custom weight.
         *
         * @param strategy the scoring strategy (must not be null)
         * @param weight   the multiplier (must be &gt; 0)
         * @return this builder
         */
        public Builder<T> add(final ScoringStrategy<T> strategy, final double weight) {
            Objects.requireNonNull(strategy, "strategy must not be null");
            if (weight <= 0) throw new IllegalArgumentException("Weight must be > 0, got: " + weight);
            strategies.add(new WeightedStrategy<>(strategy, weight));
            return this;
        }

        /** Adds a strategy with weight 1.0. */
        public Builder<T> add(final ScoringStrategy<T> strategy) {
            return add(strategy, 1.0);
        }

        /**
         * Builds the composite scorer. Returns {@link ScoringStrategy#zero()} if no strategies
         * were added (avoids NPE in the engine pipeline).
         */
        public ScoringStrategy<T> build() {
            if (strategies.isEmpty()) return ScoringStrategy.zero();
            return new CompositeScorer<>(strategies);
        }
    }

    private record WeightedStrategy<T>(ScoringStrategy<T> strategy, double weight) {}
}
