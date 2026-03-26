package server.learningresources.model;

/**
 * Shared string-normalization helpers for enum {@code fromString()} methods.
 *
 * <p>Every display-name-based enum in this package follows the same parse pattern:
 * validate → normalize → match. This utility eliminates the duplicated
 * null-check and normalization logic across
 * {@link ConceptArea}, {@link ConceptDomain}, {@link ContentFreshness},
 * {@link DifficultyLevel}, {@link LanguageApplicability}, and {@link SearchMode}.
 *
 * <p>All methods are pure functions with no side effects.
 */
public final class EnumParseUtils {

    private EnumParseUtils() {
        // Utility class — no instances
    }

    /**
     * Validates that {@code value} is neither null nor blank and returns it.
     *
     * @param value    the raw user input
     * @param enumName human-readable enum name for the error message
     *                 (e.g., "Concept area", "Difficulty level")
     * @return the original {@code value}, guaranteed non-null and non-blank
     * @throws IllegalArgumentException if {@code value} is null or blank
     */
    public static String requireNonBlank(final String value, final String enumName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(enumName + " must not be null or blank");
        }
        return value;
    }

    /**
     * Basic normalization: strip whitespace and lowercase.
     *
     * <p>Used by enums whose display names are plain lowercase words
     * (e.g., {@link DifficultyLevel}: "beginner", {@link SearchMode}: "specific").
     *
     * @param value the raw input (must not be null)
     * @return stripped, lowercased string
     */
    public static String normalize(final String value) {
        return value.strip().toLowerCase();
    }

    /**
     * Normalizes to hyphenated-lowercase form: strip, lowercase, spaces → hyphens.
     *
     * <p>Used by enums whose display names are hyphenated
     * (e.g., {@link ConceptArea}: "design-patterns", {@link ContentFreshness}: "actively-maintained").
     *
     * @param value the raw input (must not be null)
     * @return stripped, lowercased, space-to-hyphen string
     */
    public static String toHyphenated(final String value) {
        return value.strip().toLowerCase().replace(' ', '-');
    }

    /**
     * Normalizes to underscored-lowercase form: strip, lowercase, spaces and hyphens → underscores.
     *
     * <p>Used for matching against Java enum constant names
     * (e.g., "design-patterns" → "design_patterns" for comparison with {@code DESIGN_PATTERNS}).
     *
     * @param value the raw input (must not be null)
     * @return stripped, lowercased, space-and-hyphen-to-underscore string
     */
    public static String toUnderscored(final String value) {
        return value.strip().toLowerCase().replace(' ', '_').replace('-', '_');
    }

    /**
     * Converts a user-input string to Java enum-name form: strip, hyphens → underscores.
     *
     * <p>Preserves original case for case-insensitive {@code equalsIgnoreCase} matching.
     * Used by {@link ConceptArea#fromString(String)} to match "jvm-internals" against
     * {@code JVM_INTERNALS}.
     *
     * @param value the raw input (must not be null)
     * @return stripped, hyphen-to-underscore string (case preserved)
     */
    public static String toEnumName(final String value) {
        return value.strip().replace('-', '_');
    }
}
