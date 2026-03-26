package server.learningresources.model;

/**
 * Content freshness and maintenance status of a learning resource.
 *
 * <p>Helps users judge whether a resource reflects current best practices
 * or may contain outdated information.
 */
public enum ContentFreshness {

    /**
     * Timeless content that remains accurate regardless of version changes.
     * Examples: CLRS algorithms textbook, design pattern theory, Big-O guide.
     */
    EVERGREEN("evergreen"),

    /**
     * Actively maintained and updated — content tracks the latest versions.
     * Examples: MDN Web Docs, official Java tutorials on dev.java.
     */
    ACTIVELY_MAINTAINED("actively-maintained"),

    /**
     * Occasionally updated — content is mostly current but may lag behind.
     * Examples: Many blog posts, community tutorials.
     */
    PERIODICALLY_UPDATED("periodically-updated"),

    /**
     * Historical reference — content is accurate for its stated version
     * but has not been updated for the latest release.
     * Examples: Java 8 tutorials, Python 2 books.
     */
    HISTORICAL("historical"),

    /**
     * Archived or deprecated — kept for reference but may be misleading
     * if taken as current advice.
     */
    ARCHIVED("archived");

    private final String displayName;

    ContentFreshness(final String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the human-readable display name.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Resolves a {@link ContentFreshness} from a string (case-insensitive).
     *
     * @param value the string to parse
     * @return the matching freshness level
     * @throws IllegalArgumentException if no match is found
     */
    public static ContentFreshness fromString(final String value) {
        EnumParseUtils.requireNonBlank(value, "Freshness");
        final var normalized = EnumParseUtils.toHyphenated(value);
        for (final ContentFreshness freshness : values()) {
            if (freshness.displayName.equals(normalized)
                    || freshness.name().equalsIgnoreCase(value.strip())) {
                return freshness;
            }
        }
        throw new IllegalArgumentException("Unknown freshness: '" + value + "'");
    }
}
