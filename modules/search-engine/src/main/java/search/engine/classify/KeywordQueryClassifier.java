package search.engine.classify;

import search.api.classify.QueryClassifier;
import search.api.classify.SearchMode;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A {@link QueryClassifier} driven by configurable keyword lists.
 *
 * <h2>Classification pipeline (first matching rule wins)</h2>
 * <ol>
 *   <li><strong>URL / quoted</strong> — {@code http} or {@code "} in input → SPECIFIC.</li>
 *   <li><strong>Specific trigger phrases</strong> — any configured specific keyword → SPECIFIC.</li>
 *   <li><strong>Exploratory keyword + short</strong> — query ≤ {@code exploratoryWordLimit} words
 *       AND contains an exploratory keyword → EXPLORATORY.</li>
 *   <li><strong>Unknown vocabulary</strong> — very short query (≤ 2 words) with no hit in
 *       the known vocabulary → EXPLORATORY.</li>
 *   <li><strong>Difficulty-only word</strong> — single word that is a difficulty marker → EXPLORATORY.</li>
 *   <li><strong>Default</strong> — everything else → VAGUE.</li>
 * </ol>
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * var classifier = KeywordQueryClassifier.builder()
 *         .specificKeywords(List.of("docs for", "official", "api reference", "javadoc"))
 *         .exploratoryKeywords(List.of("learn", "start", "beginner", "what should"))
 *         .difficultyKeywords(Set.of("beginner", "intermediate", "advanced", "expert"))
 *         .knownVocabulary(conceptMap.keySet(), categoryMap.keySet())
 *         .exploratoryWordLimit(5)
 *         .build();
 * }</pre>
 *
 * @see QueryClassifier
 * @see SearchMode
 */
public final class KeywordQueryClassifier implements QueryClassifier {

    private final List<String> specificKeywords;
    private final List<String> exploratoryKeywords;
    private final Set<String> difficultyKeywords;
    private final Set<String> knownVocabulary;
    private final int exploratoryWordLimit;

    private KeywordQueryClassifier(final Builder builder) {
        this.specificKeywords     = List.copyOf(builder.specificKeywords);
        this.exploratoryKeywords  = List.copyOf(builder.exploratoryKeywords);
        this.difficultyKeywords   = Set.copyOf(builder.difficultyKeywords);
        this.knownVocabulary      = Set.copyOf(builder.knownVocabulary);
        this.exploratoryWordLimit = builder.exploratoryWordLimit;
    }

    @Override
    public SearchMode classify(final String normalizedInput) {
        Objects.requireNonNull(normalizedInput, "normalizedInput must not be null");

        // Rule 1: URL or quoted exact term → SPECIFIC
        if (normalizedInput.contains("\"") || normalizedInput.contains("http")) {
            return SearchMode.SPECIFIC;
        }

        // Rule 2: explicit specific trigger word/phrase → SPECIFIC
        if (specificKeywords.stream().anyMatch(normalizedInput::contains)) {
            return SearchMode.SPECIFIC;
        }

        final var words    = normalizedInput.strip().split("\\s+");
        final var wordCount = words.length;
        final var hasExploratoryKeyword = exploratoryKeywords.stream()
                .anyMatch(normalizedInput::contains);

        // Rule 3: short query with an exploratory keyword → EXPLORATORY
        if (wordCount <= exploratoryWordLimit && hasExploratoryKeyword) {
            return SearchMode.EXPLORATORY;
        }

        // Rule 4: very short query with no hit in known vocabulary → EXPLORATORY
        // (checks: any known keyword equals the whole input, OR the input contains a known keyword)
        if (wordCount <= 2 && knownVocabulary.stream()
                .noneMatch(kw -> kw.equals(normalizedInput) || normalizedInput.contains(kw))) {
            return SearchMode.EXPLORATORY;
        }

        // Rule 5: single difficulty-level word → EXPLORATORY
        if (wordCount == 1 && difficultyKeywords.contains(normalizedInput)) {
            return SearchMode.EXPLORATORY;
        }

        return SearchMode.VAGUE;
    }

    public static Builder builder() { return new Builder(); }

    /** Fluent builder for {@link KeywordQueryClassifier}. */
    public static final class Builder {

        private List<String> specificKeywords    = List.of();
        private List<String> exploratoryKeywords = List.of();
        private Set<String>  difficultyKeywords  = Set.of();
        private Set<String>  knownVocabulary     = Set.of();
        private int exploratoryWordLimit         = 5;

        private Builder() {}

        public Builder specificKeywords(final List<String> keywords) {
            this.specificKeywords = Objects.requireNonNull(keywords); return this;
        }
        public Builder exploratoryKeywords(final List<String> keywords) {
            this.exploratoryKeywords = Objects.requireNonNull(keywords); return this;
        }
        public Builder difficultyKeywords(final Set<String> keywords) {
            this.difficultyKeywords = Objects.requireNonNull(keywords); return this;
        }

        /** @param vocabularySets one or more keyword sets to combine as the known vocabulary */
        @SafeVarargs
        public final Builder knownVocabulary(final Set<String>... vocabularySets) {
            final var combined = new java.util.HashSet<String>();
            for (final var set : vocabularySets) combined.addAll(set);
            this.knownVocabulary = combined; return this;
        }

        /** Maximum number of words in a query that can still be classified as EXPLORATORY. */
        public Builder exploratoryWordLimit(final int limit) {
            this.exploratoryWordLimit = limit; return this;
        }

        public KeywordQueryClassifier build() { return new KeywordQueryClassifier(this); }
    }
}
