package server.learningresources.model;

import java.util.List;
import java.util.Objects;

/**
 * Criteria for searching and filtering learning resources in the vault.
 *
 * <p>All fields are optional — an empty query returns all resources.
 * Non-null fields are combined with AND logic.
 *
 * @param searchText free-text search across title, description, tags
 * @param type       filter by resource type (null = any)
 * @param category   filter by topic category (null = any)
 * @param difficulty filter by difficulty level (null = any)
 * @param tags       filter by tags — resource must have ALL listed tags
 * @param freeOnly   if {@code true}, only return free resources
 * @param maxResults maximum number of results to return (0 = unlimited)
 */
public record ResourceQuery(
        String searchText,
        ResourceType type,
        ResourceCategory category,
        String difficulty,
        List<String> tags,
        boolean freeOnly,
        int maxResults
) {

    /** Default query that returns all resources. */
    public static final ResourceQuery ALL = new ResourceQuery("", null, null, null, List.of(), false, 0);

    private static final int DEFAULT_MAX_RESULTS = 25;

    /**
     * Creates a {@link ResourceQuery} with validation and defensive copying.
     *
     * @param searchText free-text search query
     * @param type       resource type filter
     * @param category   category filter
     * @param difficulty difficulty filter
     * @param tags       required tags (defensively copied)
     * @param freeOnly   free-only filter
     * @param maxResults result limit
     */
    public ResourceQuery {
        Objects.requireNonNull(searchText, "Search text must not be null (use empty string for no filter)");
        Objects.requireNonNull(tags, "Tags list must not be null (use empty list for no filter)");

        if (maxResults < 0) {
            throw new IllegalArgumentException("maxResults cannot be negative: " + maxResults);
        }

        tags = List.copyOf(tags);
    }

    /**
     * Creates a simple text search query with default limits.
     *
     * @param searchText the text to search for
     * @return a query filtered by the search text
     */
    public static ResourceQuery byText(final String searchText) {
        return new ResourceQuery(searchText, null, null, null, List.of(), false, DEFAULT_MAX_RESULTS);
    }

    /**
     * Creates a query filtering by category.
     *
     * @param category the topic category
     * @return a query filtered by category
     */
    public static ResourceQuery byCategory(final ResourceCategory category) {
        return new ResourceQuery("", null, category, null, List.of(), false, DEFAULT_MAX_RESULTS);
    }

    /**
     * Creates a query filtering by resource type.
     *
     * @param type the resource type
     * @return a query filtered by type
     */
    public static ResourceQuery byType(final ResourceType type) {
        return new ResourceQuery("", type, null, null, List.of(), false, DEFAULT_MAX_RESULTS);
    }

    /**
     * Checks whether this query has any active filters.
     *
     * @return {@code true} if at least one filter is set
     */
    public boolean hasFilters() {
        return !searchText.isBlank()
                || type != null
                || category != null
                || difficulty != null
                || !tags.isEmpty()
                || freeOnly;
    }
}
