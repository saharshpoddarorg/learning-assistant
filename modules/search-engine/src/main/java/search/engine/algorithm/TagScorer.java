package search.engine.algorithm;

import search.api.algorithm.ScoringStrategy;
import search.api.core.SearchContext;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

/**
 * Scores documents by tag-set overlap with query words.
 *
 * <p>For each significant word in the query (≥ {@link #MIN_WORD_LENGTH} chars):
 * <ul>
 *   <li>If any tag <em>contains</em> the word → {@code hitPoints}.</li>
 *   <li>If a tag <em>exactly equals</em> the word → additional {@code wholeTagBonus}.</li>
 * </ul>
 *
 * <h2>Why use TagScorer separately from TextMatchScorer?</h2>
 * Tags are heavily curated metadata — an exact tag hit is a strong signal.
 * {@link TextMatchScorer} already awards points for per-word tag matches, but
 * {@code TagScorer} can be used standalone in a {@link CompositeScorer} with a
 * higher weight to amplify the tag-match signal.
 *
 * @param <T> the document type
 */
public final class TagScorer<T> implements ScoringStrategy<T> {

    /** Minimum query-word length for tag matching. */
    static final int MIN_WORD_LENGTH = 3;

    private final Function<T, Collection<String>> tagsExtractor;
    private final int hitPoints;
    private final int wholeTagBonus;

    private TagScorer(final Builder<T> builder) {
        this.tagsExtractor = builder.tagsExtractor;
        this.hitPoints     = builder.hitPoints;
        this.wholeTagBonus = builder.wholeTagBonus;
    }

    @Override
    public int score(final T item, final SearchContext context) {
        Objects.requireNonNull(item,    "item must not be null");
        Objects.requireNonNull(context, "context must not be null");

        final var input = context.normalizedInput();
        if (input.isBlank()) return 0;

        final var tags = tagsExtractor.apply(item);
        if (tags == null || tags.isEmpty()) return 0;

        var total = 0;
        for (final var word : input.split("\\s+")) {
            if (word.length() < MIN_WORD_LENGTH) continue;
            for (final var tag : tags) {
                final var tagLower = tag.toLowerCase();
                if (tagLower.contains(word)) {
                    total += hitPoints;
                    if (tagLower.equals(word)) total += wholeTagBonus;
                    break; // count each word once, even if it matches multiple tags
                }
            }
        }
        return total;
    }

    public static <T> Builder<T> builder() { return new Builder<>(); }

    /** Fluent builder for {@link TagScorer}. */
    public static final class Builder<T> {

        private Function<T, Collection<String>> tagsExtractor = item -> java.util.List.of();
        private int hitPoints     = 15;
        private int wholeTagBonus = 10;

        private Builder() {}

        /** Extracts the tag collection from a document. */
        public Builder<T> tagsExtractor(final Function<T, Collection<String>> extractor) {
            this.tagsExtractor = Objects.requireNonNull(extractor); return this;
        }

        /** Points awarded when a query word is found in any tag (default 15). */
        public Builder<T> hitPoints(final int points) {
            this.hitPoints = points; return this;
        }

        /** Additional points when a query word exactly matches a whole tag (default 10). */
        public Builder<T> wholeTagBonus(final int bonus) {
            this.wholeTagBonus = bonus; return this;
        }

        public TagScorer<T> build() { return new TagScorer<>(this); }
    }
}
