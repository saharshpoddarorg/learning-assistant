package search.api.core;

import search.api.classify.SearchMode;

import java.util.List;
import java.util.Objects;

/**
 * The complete output of a {@link SearchEngine#search(SearchContext)} call.
 *
 * <p>Bundles:
 * <ul>
 *   <li>The {@link SearchMode} that was actually used (auto-classified or forced).</li>
 *   <li>A score-ordered list of {@link ScoredItem}s.</li>
 *   <li>Follow-up suggestions ("did you mean?", related topics, next steps).</li>
 *   <li>A human-readable {@code summary} sentence for display.</li>
 * </ul>
 *
 * <h2>Consuming results</h2>
 * <pre>{@code
 * SearchResult<LearningResource> result = engine.search("java concurrency");
 * if (result.isEmpty()) {
 *     System.out.println("Suggestions: " + result.suggestions());
 * } else {
 *     System.out.println(result.summary());
 *     result.items().forEach(si ->
 *         System.out.printf("[%3d] %s%n", si.score(), si.item().title()));
 * }
 * }</pre>
 *
 * @param classifiedMode the mode used for this search
 * @param items          scored documents, highest-first; immutable
 * @param suggestions    follow-up hints; immutable
 * @param summary        human-readable one-liner
 * @param <T>            the document type
 */
public record SearchResult<T>(
        SearchMode classifiedMode,
        List<ScoredItem<T>> items,
        List<String> suggestions,
        String summary
) {

    /** Validates and defensively copies all mutable collections. */
    public SearchResult {
        Objects.requireNonNull(classifiedMode, "classifiedMode must not be null");
        Objects.requireNonNull(items,          "items must not be null");
        Objects.requireNonNull(suggestions,    "suggestions must not be null");
        Objects.requireNonNull(summary,        "summary must not be null");
        items       = List.copyOf(items);
        suggestions = List.copyOf(suggestions);
    }

    // ─── Factory shortcuts ──────────────────────────────────────────

    /**
     * Creates an empty result with no suggestions.
     *
     * @param mode    the mode used
     * @param summary the display summary
     * @param <T>     the document type
     * @return an empty result
     */
    public static <T> SearchResult<T> empty(final SearchMode mode, final String summary) {
        return new SearchResult<>(mode, List.of(), List.of(), summary);
    }

    /**
     * Creates an empty result with follow-up suggestions.
     *
     * @param mode        the mode used
     * @param summary     the display summary
     * @param suggestions follow-up hints
     * @param <T>         the document type
     * @return an empty result with suggestions
     */
    public static <T> SearchResult<T> emptyWithSuggestions(final SearchMode mode,
                                                            final String summary,
                                                            final List<String> suggestions) {
        return new SearchResult<>(mode, List.of(), suggestions, summary);
    }

    // ─── Query methods ──────────────────────────────────────────────

    /** Returns {@code true} if no documents were found. */
    public boolean isEmpty() { return items.isEmpty(); }

    /** Returns the number of matched documents. */
    public int count() { return items.size(); }

    /** Returns the highest relevance score, or 0 if empty. */
    public int topScore() {
        return items.stream().mapToInt(ScoredItem::score).max().orElse(0);
    }
}
