package server.atlassian.v1.model.confluence;

/**
 * Publication status of a Confluence page or blog post.
 *
 * <p>Maps to the {@code status} field in the Confluence REST API v2 response.
 * Use {@link #getApiValue()} when constructing query parameters or request bodies
 * for the Confluence API.
 *
 * @see <a href="https://developer.atlassian.com/cloud/confluence/rest/v2/api-group-page/">
 *     Confluence REST API v2 — Pages</a>
 */
public enum ContentStatus {

    /**
     * The page is published and visible to users with read permission.
     * This is the normal, final state for published content.
     * API value: {@code "current"}
     */
    CURRENT("current", "Published"),

    /**
     * The page has been saved as a draft and is not yet visible to general users.
     * Only the author and administrators can view draft content.
     * API value: {@code "draft"}
     */
    DRAFT("draft", "Draft"),

    /**
     * The page has been archived. It remains accessible but is hidden from
     * normal space navigation. Useful for outdated content.
     * API value: {@code "archived"}
     */
    ARCHIVED("archived", "Archived"),

    /**
     * The page has been moved to the trash. It may be restored by an admin
     * within the retention window before permanent deletion.
     * API value: {@code "trashed"}
     */
    TRASHED("trashed", "Trashed"),

    /**
     * A historical version of a page that has been superseded.
     * Returned when fetching version history; not filterable in list calls.
     * API value: {@code "historical"}
     */
    HISTORICAL("historical", "Historical"),

    /**
     * The content has been permanently deleted and cannot be recovered.
     * API value: {@code "deleted"}
     */
    DELETED("deleted", "Deleted");

    // -------------------------------------------------------------------------

    private final String apiValue;
    private final String displayName;

    ContentStatus(final String apiValue, final String displayName) {
        this.apiValue = apiValue;
        this.displayName = displayName;
    }

    /**
     * Returns the exact string value used in Confluence REST API calls.
     *
     * @return the API status value (e.g., {@code "current"}, {@code "draft"})
     */
    public String getApiValue() {
        return apiValue;
    }

    /**
     * Returns a human-readable label for display in UI or formatted output.
     *
     * @return the display name (e.g., "Published", "Draft")
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Resolves a content status from its API value or enum name.
     *
     * @param value the API value (case-insensitive, e.g., {@code "current"})
     * @return the matching status, or empty if unrecognized
     */
    public static java.util.Optional<ContentStatus> fromApiValue(final String value) {
        if (value == null || value.isBlank()) {
            return java.util.Optional.empty();
        }
        for (final ContentStatus status : values()) {
            if (status.apiValue.equalsIgnoreCase(value)
                    || status.name().equalsIgnoreCase(value)) {
                return java.util.Optional.of(status);
            }
        }
        return java.util.Optional.empty();
    }
}
