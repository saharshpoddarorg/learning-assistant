package search.api.classify;

/**
 * The three fundamental modes of user search intent.
 *
 * <h2>Mode semantics</h2>
 * <ul>
 *   <li>{@link #SPECIFIC} — the user knows exactly what they want (exact lookup, URL, quoted phrase).
 *       Score: high weight on exact/partial title match, ID match. Filter: keep only high-confidence hits.</li>
 *   <li>{@link #VAGUE} — the user knows the topic but not the exact resource (topic-based search).
 *       Score: keyword matching against all fields + concept/category inference.</li>
 *   <li>{@link #EXPLORATORY} — the user wants guided recommendations (browse, curated list).
 *       Score: quality signals (official, freshness, free) + difficulty fit. Filter: lax.</li>
 * </ul>
 *
 * <p>The mode is either auto-classified by a {@link QueryClassifier} or forced by the caller
 * via {@link search.api.core.SearchContext#forcedMode()}.
 *
 * @see QueryClassifier
 * @see search.engine.classify.KeywordQueryClassifier
 */
public enum SearchMode {

    /** Exact lookup — user knows precisely what they want. */
    SPECIFIC("specific",       "Exact match — user knows exactly what they want"),
    /** Topic search — user knows the area, not the specific resource. */
    VAGUE("vague",             "Topic match — user knows the area but not the resource"),
    /** Guided browsing — user wants curation and suggestions. */
    EXPLORATORY("exploratory", "Explore — user wants guidance and curated recommendations");

    private final String displayName;
    private final String description;

    SearchMode(final String displayName, final String description) {
        this.displayName = displayName;
        this.description = description;
    }

    /** Returns the lowercase display label for this mode. */
    public String getDisplayName() { return displayName; }

    /** Returns a user-facing description of when this mode applies. */
    public String getDescription() { return description; }

    /**
     * Parses a mode from a string (case-insensitive).
     *
     * @param value the raw string (e.g., {@code "specific"}, {@code "VAGUE"})
     * @return the matching mode
     * @throws IllegalArgumentException if no match is found
     */
    public static SearchMode fromString(final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("SearchMode value must not be null or blank");
        }
        final var normalised = value.strip().toLowerCase();
        for (final var mode : values()) {
            if (mode.displayName.equals(normalised) || mode.name().equalsIgnoreCase(normalised)) {
                return mode;
            }
        }
        throw new IllegalArgumentException(
                "Unknown search mode: '" + value + "'. Valid values: specific, vague, exploratory");
    }
}
