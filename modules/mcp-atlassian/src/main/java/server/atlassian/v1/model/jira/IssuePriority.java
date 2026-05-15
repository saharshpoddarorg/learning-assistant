package server.atlassian.v1.model.jira;

/**
 * Jira issue priorities — covers both the default scheme and the classic (legacy) scheme.
 *
 * <p>Jira supports two common priority schemes:
 * <ul>
 *   <li><strong>Default scheme</strong> (Jira Cloud default): Highest, High, Medium, Low, Lowest</li>
 *   <li><strong>Classic scheme</strong> (Jira Server / older Cloud): Blocker, Critical, Major, Minor, Trivial</li>
 * </ul>
 *
 * <p>Both schemes use 5 levels. The {@link PriorityScheme} nested enum identifies which
 * scheme a value belongs to, allowing the API client to send the correct name.
 *
 * <p>Call {@link #getDisplayName()} to get the exact string to send in Jira API calls.
 */
public enum IssuePriority {

    // -------------------------------------------------------------------------
    // Default scheme — Jira Cloud out-of-the-box
    // -------------------------------------------------------------------------

    /** Highest urgency — service is down or severely impacted. */
    HIGHEST("Highest", 1, PriorityScheme.DEFAULT),

    /** Significant impact — major functionality affected, no workaround. */
    HIGH("High", 2, PriorityScheme.DEFAULT),

    /** Moderate impact — some functionality affected, workaround available. (Default for new issues.) */
    MEDIUM("Medium", 3, PriorityScheme.DEFAULT),

    /** Minor impact — limited effect on functionality. */
    LOW("Low", 4, PriorityScheme.DEFAULT),

    /** Cosmetic or trivial issue — no functional impact. */
    LOWEST("Lowest", 5, PriorityScheme.DEFAULT),

    // -------------------------------------------------------------------------
    // Classic scheme — Jira Server / Data Center / legacy Cloud projects
    // -------------------------------------------------------------------------

    /**
     * System is down or a critical workflow is completely unusable.
     * Treated as P1 — requires immediate response.
     */
    BLOCKER("Blocker", 1, PriorityScheme.CLASSIC),

    /**
     * Critical functionality is broken; significant business impact.
     * No workaround available. Treated as P2.
     */
    CRITICAL("Critical", 2, PriorityScheme.CLASSIC),

    /**
     * Important functionality is impacted. A workaround may exist.
     * Treated as P3 — standard high-priority fix.
     */
    MAJOR("Major", 3, PriorityScheme.CLASSIC),

    /**
     * Minor functionality is affected. Low business impact.
     * Treated as P4 — fix within normal cadence.
     */
    MINOR("Minor", 4, PriorityScheme.CLASSIC),

    /**
     * Cosmetic or negligible impact. Treated as P5.
     * This is the classic scheme equivalent of Lowest.
     */
    TRIVIAL("Trivial", 5, PriorityScheme.CLASSIC);

    // -------------------------------------------------------------------------

    /**
     * Identifies which priority naming scheme a value belongs to.
     * Useful when a Jira instance uses a non-standard scheme name.
     */
    public enum PriorityScheme {
        /** Highest / High / Medium / Low / Lowest (Jira Cloud default). */
        DEFAULT,
        /** Blocker / Critical / Major / Minor / Trivial (Jira Server classic). */
        CLASSIC
    }

    private final String displayName;
    private final int sortOrder;
    private final PriorityScheme scheme;

    IssuePriority(final String displayName, final int sortOrder, final PriorityScheme scheme) {
        this.displayName = displayName;
        this.sortOrder = sortOrder;
        this.scheme = scheme;
    }

    /**
     * Returns the exact priority name to send in Jira REST API calls.
     *
     * @return the display name (e.g., "High", "Critical")
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the numeric sort order (1 = most urgent, 5 = least urgent).
     *
     * @return the sort order
     */
    public int getSortOrder() {
        return sortOrder;
    }

    /**
     * Returns the priority scheme this value belongs to.
     *
     * @return {@link PriorityScheme#DEFAULT} or {@link PriorityScheme#CLASSIC}
     */
    public PriorityScheme getScheme() {
        return scheme;
    }

    /**
     * Resolves a priority from its display name or enum name.
     *
     * @param value the priority name (case-insensitive)
     * @return the matching priority, or empty if not a known built-in value
     */
    public static java.util.Optional<IssuePriority> fromDisplayName(final String value) {
        for (final IssuePriority priority : values()) {
            if (priority.displayName.equalsIgnoreCase(value)
                    || priority.name().equalsIgnoreCase(value)) {
                return java.util.Optional.of(priority);
            }
        }
        return java.util.Optional.empty();
    }

    /**
     * Returns all display names for a given scheme.
     *
     * @param scheme the priority scheme to filter by
     * @return comma-separated display names for that scheme
     */
    public static String validValuesForScheme(final PriorityScheme scheme) {
        final var sb = new StringBuilder();
        for (final IssuePriority priority : values()) {
            if (priority.scheme == scheme) {
                if (!sb.isEmpty()) sb.append(", ");
                sb.append(priority.displayName);
            }
        }
        return sb.toString();
    }
}
