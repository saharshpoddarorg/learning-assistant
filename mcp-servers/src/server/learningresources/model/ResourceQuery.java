package server.learningresources.model;

import java.util.Objects;
import java.util.Set;

/**
 * Criteria for searching and filtering learning resources in the vault.
 *
 * <p>All fields are optional — an empty query returns all resources.
 * Non-null fields are combined with AND logic. Supports composite filtering
 * across all categorization dimensions:
 * <ul>
 *   <li>{@code searchText} — free-text search across all searchable fields</li>
 *   <li>{@code type} — filter by content format</li>
 *   <li>{@code categories} — filter by one or more topic categories (OR logic within)</li>
 *   <li>{@code conceptArea} — filter by CS/SE concept area</li>
 *   <li>{@code minDifficulty}/{@code maxDifficulty} — difficulty range filter</li>
 *   <li>{@code freshness} — filter by content freshness/maintenance status</li>
 *   <li>{@code officialOnly} — restrict to official/authoritative sources</li>
 *   <li>{@code tags} — filter by tags (AND logic: resource must have ALL)</li>
 *   <li>{@code freeOnly} — restrict to free resources</li>
 * </ul>
 *
 * @param searchText    free-text search across title, description, tags, concepts
 * @param type          filter by resource type (null = any)
 * @param categories    filter by topic categories — resource must match ANY (empty = any)
 * @param conceptArea   filter by concept area (null = any)
 * @param minDifficulty minimum difficulty level inclusive (null = no lower bound)
 * @param maxDifficulty maximum difficulty level inclusive (null = no upper bound)
 * @param freshness     filter by content freshness (null = any)
 * @param officialOnly  if {@code true}, only return official/authoritative sources
 * @param tags          filter by tags — resource must have ALL listed tags
 * @param freeOnly      if {@code true}, only return free resources
 * @param maxResults    maximum number of results to return (0 = unlimited)
 */
public record ResourceQuery(
        String searchText,
        ResourceType type,
        Set<ResourceCategory> categories,
        ConceptArea conceptArea,
        DifficultyLevel minDifficulty,
        DifficultyLevel maxDifficulty,
        ContentFreshness freshness,
        boolean officialOnly,
        Set<String> tags,
        boolean freeOnly,
        int maxResults
) {

    /** Default query that returns all resources. */
    public static final ResourceQuery ALL = new ResourceQuery(
            "", null, Set.of(), null, null, null, null, false, Set.of(), false, 0);

    private static final int DEFAULT_MAX_RESULTS = 25;

    /**
     * Creates a {@link ResourceQuery} with validation and defensive copying.
     */
    public ResourceQuery {
        Objects.requireNonNull(searchText, "Search text must not be null (use empty string for no filter)");
        Objects.requireNonNull(categories, "Categories must not be null (use empty set for no filter)");
        Objects.requireNonNull(tags, "Tags set must not be null (use empty set for no filter)");

        if (maxResults < 0) {
            throw new IllegalArgumentException("maxResults cannot be negative: " + maxResults);
        }

        categories = Set.copyOf(categories);
        tags = Set.copyOf(tags);
    }

    /**
     * Creates a simple text search query with default limits.
     *
     * @param searchText the text to search for
     * @return a query filtered by the search text
     */
    public static ResourceQuery byText(final String searchText) {
        return new ResourceQuery(searchText, null, Set.of(), null, null, null,
                null, false, Set.of(), false, DEFAULT_MAX_RESULTS);
    }

    /**
     * Creates a query filtering by a single category.
     *
     * @param category the topic category
     * @return a query filtered by category
     */
    public static ResourceQuery byCategory(final ResourceCategory category) {
        return new ResourceQuery("", null, Set.of(category), null, null, null,
                null, false, Set.of(), false, DEFAULT_MAX_RESULTS);
    }

    /**
     * Creates a query filtering by resource type.
     *
     * @param type the resource type
     * @return a query filtered by type
     */
    public static ResourceQuery byType(final ResourceType type) {
        return new ResourceQuery("", type, Set.of(), null, null, null,
                null, false, Set.of(), false, DEFAULT_MAX_RESULTS);
    }

    /**
     * Creates a query filtering by concept area.
     *
     * @param concept the concept area
     * @return a query filtered by concept
     */
    public static ResourceQuery byConcept(final ConceptArea concept) {
        return new ResourceQuery("", null, Set.of(), concept, null, null,
                null, false, Set.of(), false, DEFAULT_MAX_RESULTS);
    }

    /**
     * Checks whether this query has any active filters.
     *
     * @return {@code true} if at least one filter is set
     */
    public boolean hasFilters() {
        return !searchText.isBlank()
                || type != null
                || !categories.isEmpty()
                || conceptArea != null
                || minDifficulty != null
                || maxDifficulty != null
                || freshness != null
                || officialOnly
                || !tags.isEmpty()
                || freeOnly;
    }
}
