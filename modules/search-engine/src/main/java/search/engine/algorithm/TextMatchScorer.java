package search.engine.algorithm;

import search.api.algorithm.ScoringStrategy;
import search.api.core.SearchContext;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

/**
 * Scores documents based on text field matching across title, body, and tags.
 *
 * <h2>Scoring tiers</h2>
 * <ol>
 *   <li><strong>Exact title match</strong> — full query equals normalised title.</li>
 *   <li><strong>Partial title match</strong> — title contains the full query string.</li>
 *   <li><strong>Body match</strong> — body/description contains the query string.</li>
 *   <li><strong>Per-word title match</strong> — each query word found in title (FIXED: separate score from tag).</li>
 *   <li><strong>Per-word tag match</strong> — each query word found in any tag.</li>
 *   <li><strong>Fuzzy prefix match</strong> — query word shares a prefix with a title word.</li>
 * </ol>
 *
 * <p>Previously this scorer used {@code tagMatch} for both per-word title hits and tag hits
 * (a semantic bug). This version separates them into distinct {@link Scores} fields.
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * ScoringStrategy<Article> scorer = TextMatchScorer.<Article>builder()
 *         .titleExtractor(Article::title)
 *         .bodyExtractor(Article::body)
 *         .tagsExtractor(Article::tags)
 *         .scores(TextMatchScorer.Scores.defaults())
 *         .build();
 * }</pre>
 *
 * @param <T> the document type
 *
 * @see CompositeScorer
 * @see FuzzyMatcher
 * @see Bm25Scorer
 */
public final class TextMatchScorer<T> implements ScoringStrategy<T> {

    private final Function<T, String> titleExtractor;
    private final Function<T, String> bodyExtractor;
    private final Function<T, Collection<String>> tagsExtractor;
    private final Scores scores;

    private TextMatchScorer(final Builder<T> builder) {
        this.titleExtractor = builder.titleExtractor;
        this.bodyExtractor  = builder.bodyExtractor;
        this.tagsExtractor  = builder.tagsExtractor;
        this.scores         = builder.scores;
    }

    @Override
    public int score(final T item, final SearchContext context) {
        Objects.requireNonNull(item,    "item must not be null");
        Objects.requireNonNull(context, "context must not be null");

        final var input = context.normalizedInput();
        if (input.isBlank()) return 0;

        final var title = titleExtractor.apply(item).toLowerCase();
        final var body  = bodyExtractor.apply(item).toLowerCase();
        final var tags  = tagsExtractor.apply(item);

        var total = 0;

        // Phase 1: full-query title match
        if (title.equals(input)) {
            total += scores.exactTitleMatch();
        } else if (title.contains(input)) {
            total += scores.partialTitleMatch();
        }

        // Phase 2: full-query body match
        if (body.contains(input)) {
            total += scores.bodyMatch();
        }

        // Phase 3: per-word matching — title words and tag words are scored separately (bug fix)
        for (final var word : input.split("\\s+")) {
            if (word.length() < 2) continue;

            // Per-word title match (previously used tagMatch score — now uses its own weight)
            if (title.contains(word)) {
                total += scores.wordInTitleMatch();
            } else if (FuzzyMatcher.hasPrefixMatch(word, title)) {
                total += scores.fuzzyMatch();
            }

            // Per-word tag match (semantically different from title-word match)
            if (tags.stream().anyMatch(tag -> tag.toLowerCase().contains(word))) {
                total += scores.tagMatch();
            }
        }

        return total;
    }

    public static <T> Builder<T> builder() { return new Builder<>(); }

    // ─── Builder ────────────────────────────────────────────────────

    /** Fluent builder for {@link TextMatchScorer}. */
    public static final class Builder<T> {

        private Function<T, String> titleExtractor = item -> "";
        private Function<T, String> bodyExtractor  = item -> "";
        private Function<T, Collection<String>> tagsExtractor = item -> java.util.List.of();
        private Scores scores = Scores.defaults();

        private Builder() {}

        public Builder<T> titleExtractor(final Function<T, String> extractor) {
            this.titleExtractor = Objects.requireNonNull(extractor); return this;
        }
        public Builder<T> bodyExtractor(final Function<T, String> extractor) {
            this.bodyExtractor = Objects.requireNonNull(extractor); return this;
        }
        public Builder<T> tagsExtractor(final Function<T, Collection<String>> extractor) {
            this.tagsExtractor = Objects.requireNonNull(extractor); return this;
        }
        public Builder<T> scores(final Scores scores) {
            this.scores = Objects.requireNonNull(scores); return this;
        }

        public TextMatchScorer<T> build() { return new TextMatchScorer<>(this); }
    }

    // ─── Score weights record ────────────────────────────────────────

    /**
     * Configurable point values for each matching tier.
     *
     * <p>Default profile (balanced):
     * <table>
     *   <tr><th>Dimension</th><th>Default points</th></tr>
     *   <tr><td>Exact title match</td><td>100</td></tr>
     *   <tr><td>Partial title match</td><td>40</td></tr>
     *   <tr><td>Body match</td><td>20</td></tr>
     *   <tr><td>Word in title</td><td>12</td></tr>
     *   <tr><td>Tag match</td><td>15</td></tr>
     *   <tr><td>Fuzzy prefix</td><td>8</td></tr>
     * </table>
     *
     * @param exactTitleMatch   full query equals title
     * @param partialTitleMatch title contains query
     * @param bodyMatch         body contains query
     * @param wordInTitleMatch  per-word title hit (NOTE: separate from tagMatch)
     * @param tagMatch          per-word tag hit
     * @param fuzzyMatch        prefix/fuzzy title hit
     */
    public record Scores(
            int exactTitleMatch,
            int partialTitleMatch,
            int bodyMatch,
            int wordInTitleMatch,
            int tagMatch,
            int fuzzyMatch
    ) {
        /** Balanced default profile. */
        public static Scores defaults() {
            return new Scores(100, 40, 20, 12, 15, 8);
        }

        /** Title-heavy profile — useful for specific/exact searches. */
        public static Scores titleHeavy() {
            return new Scores(150, 60, 15, 18, 12, 5);
        }

        /** Full-text profile — equally weights title and body. */
        public static Scores fullText() {
            return new Scores(80, 35, 35, 10, 10, 5);
        }
    }
}
