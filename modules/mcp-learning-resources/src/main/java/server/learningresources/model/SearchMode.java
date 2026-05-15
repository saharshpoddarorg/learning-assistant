package server.learningresources.model;

/**
 * Classifies user search intent into one of three discovery modes.
 *
 * <p>The discovery engine ({@code ResourceDiscovery}) automatically classifies
 * every free-form query into one of these modes and applies a tailored search
 * strategy, scoring algorithm, and suggestion set for each.
 *
 * <h2>Modes at a Glance</h2>
 * <table>
 *   <tr><th>Mode</th><th>When</th><th>Strategy</th></tr>
 *   <tr><td>SPECIFIC</td><td>"JUnit 5 docs", quoted text, URLs</td><td>Exact title/URL/ID match</td></tr>
 *   <tr><td>VAGUE</td><td>"java testing", "concurrency patterns"</td><td>Keyword-to-concept inference + multi-dim scoring</td></tr>
 *   <tr><td>EXPLORATORY</td><td>"learn", "beginner", "recommend"</td><td>Beginner-friendly, official-first, suggestions</td></tr>
 * </table>
 *
 * @see server.learningresources.vault.ResourceDiscovery
 * @see server.learningresources.vault.DiscoveryResult
 */
public enum SearchMode {

    /**
     * User knows exactly what they want.
     *
     * <p>Triggered by quoted terms, URLs, "docs for …", "reference for …",
     * or "official …" keywords. The scoring heavily weights exact title
     * and ID matches.
     */
    SPECIFIC("specific", "Exact match — user knows exactly what they want"),

    /**
     * User knows the topic area but not the specific resource.
     *
     * <p>Triggered when the query contains topic keywords without exploratory
     * language. The engine infers concept areas and categories from keywords,
     * then applies balanced multi-dimensional scoring.
     */
    VAGUE("vague", "Topic match — user knows the area but not the resource"),

    /**
     * User wants to learn but isn't sure what.
     *
     * <p>Triggered by exploratory language ("learn", "beginner help",
     * "getting started", "recommend"). The engine returns beginner-friendly,
     * official-first resources with follow-up suggestions.
     */
    EXPLORATORY("exploratory", "Explore — user wants guidance and suggestions");

    private final String displayName;
    private final String description;

    SearchMode(final String displayName, final String description) {
        this.displayName = displayName;
        this.description = description;
    }

    /**
     * Returns the human-readable display name.
     *
     * @return the display name (e.g., "specific", "vague", "exploratory")
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns a brief description of this search mode.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Resolves a {@link SearchMode} from a string (case-insensitive).
     *
     * @param value the string to parse
     * @return the matching search mode
     * @throws IllegalArgumentException if no match is found
     */
    public static SearchMode fromString(final String value) {
        EnumParseUtils.requireNonBlank(value, "Search mode");
        final var normalized = EnumParseUtils.normalize(value);
        for (final SearchMode mode : values()) {
            if (mode.displayName.equals(normalized) || mode.name().equalsIgnoreCase(normalized)) {
                return mode;
            }
        }
        throw new IllegalArgumentException(
                "Unknown search mode: '" + value + "'. Valid: specific, vague, exploratory");
    }
}
