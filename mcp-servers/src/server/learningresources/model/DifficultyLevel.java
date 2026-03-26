package server.learningresources.model;

/**
 * Difficulty level of a learning resource.
 *
 * <p>Replaces free-form string difficulty with a type-safe enum enabling
 * accurate filtering, ordering, and range queries (e.g., "beginner to intermediate").
 */
public enum DifficultyLevel {

    /** No prior knowledge assumed — fundamentals and getting-started content. */
    BEGINNER("beginner", 1),

    /** Assumes basic knowledge — intermediate concepts and patterns. */
    INTERMEDIATE("intermediate", 2),

    /** Assumes solid understanding — deep dives, edge cases, internals. */
    ADVANCED("advanced", 3),

    /** Expert-level — research papers, formal specs, contest-level content. */
    EXPERT("expert", 4);

    private final String displayName;
    private final int ordinalLevel;

    DifficultyLevel(final String displayName, final int ordinalLevel) {
        this.displayName = displayName;
        this.ordinalLevel = ordinalLevel;
    }

    /**
     * Returns the human-readable display name.
     *
     * @return the display name (e.g., "beginner", "intermediate")
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the numeric ordinal for range comparisons.
     *
     * @return ordinal (1=beginner, 2=intermediate, 3=advanced, 4=expert)
     */
    public int getOrdinalLevel() {
        return ordinalLevel;
    }

    /**
     * Checks whether this level falls within the given range (inclusive).
     *
     * @param min the minimum difficulty
     * @param max the maximum difficulty
     * @return {@code true} if this is between min and max inclusive
     */
    public boolean isInRange(final DifficultyLevel min, final DifficultyLevel max) {
        return this.ordinalLevel >= min.ordinalLevel && this.ordinalLevel <= max.ordinalLevel;
    }

    /**
     * Resolves a {@link DifficultyLevel} from a string (case-insensitive).
     *
     * <p>Accepts display names ("beginner"), enum names ("BEGINNER"),
     * and common abbreviations ("beg", "int", "adv", "exp").
     *
     * @param value the string to parse
     * @return the matching difficulty level
     * @throws IllegalArgumentException if no match is found
     */
    public static DifficultyLevel fromString(final String value) {
        EnumParseUtils.requireNonBlank(value, "Difficulty level");
        final var normalized = EnumParseUtils.normalize(value);
        for (final DifficultyLevel level : values()) {
            if (level.displayName.equals(normalized) || level.name().equalsIgnoreCase(normalized)) {
                return level;
            }
        }
        // Common abbreviations
        return switch (normalized) {
            case "beg", "begin", "starter", "newbie", "entry" -> BEGINNER;
            case "int", "inter", "mid", "medium" -> INTERMEDIATE;
            case "adv", "hard", "deep", "senior" -> ADVANCED;
            case "exp", "master", "guru", "elite" -> EXPERT;
            default -> throw new IllegalArgumentException(
                    "Unknown difficulty: '" + value + "'. Valid: beginner, intermediate, advanced, expert");
        };
    }
}
