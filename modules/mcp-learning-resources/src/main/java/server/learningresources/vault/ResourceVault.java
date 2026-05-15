package server.learningresources.vault;

import server.learningresources.model.LearningResource;
import server.learningresources.model.ResourceCategory;
import server.learningresources.model.ResourceQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * In-memory library of curated learning resources.
 *
 * <p>The vault stores {@link LearningResource} entries indexed by ID, and provides
 * composite search and filter capabilities across all categorization dimensions
 * (type, category, concept area, difficulty range, freshness, official status).
 *
 * <p>Pre-populated with built-in resources via {@link BuiltInResources} and
 * supports runtime additions. Thread-safe — uses a {@link ConcurrentHashMap}.
 */
public class ResourceVault {

    private static final Logger LOGGER = Logger.getLogger(ResourceVault.class.getName());

    private final Map<String, LearningResource> resources = new ConcurrentHashMap<>();

    /**
     * Creates an empty vault.
     */
    public ResourceVault() {
        // Empty — call loadBuiltInResources() to populate
    }

    /**
     * Loads the built-in curated resource library into this vault.
     *
     * @return this vault (for chaining)
     */
    public ResourceVault loadBuiltInResources() {
        final var builtIn = BuiltInResources.all();
        for (final var resource : builtIn) {
            resources.put(resource.id(), resource);
        }
        LOGGER.info("Loaded " + builtIn.size() + " built-in learning resources into vault.");
        return this;
    }

    /**
     * Adds a resource to the vault. Replaces any existing resource with the same ID.
     *
     * @param resource the resource to add
     */
    public void add(final LearningResource resource) {
        Objects.requireNonNull(resource, "Resource must not be null");
        resources.put(resource.id(), resource);
        LOGGER.fine("Added resource: " + resource.id());
    }

    /**
     * Retrieves a resource by its unique ID.
     *
     * @param resourceId the resource identifier
     * @return the resource, or empty if not found
     */
    public Optional<LearningResource> findById(final String resourceId) {
        return Optional.ofNullable(resources.get(resourceId));
    }

    /**
     * Searches the vault using composite query criteria across all dimensions.
     *
     * <p>Filters are combined with AND logic. Within multi-value fields
     * (categories), OR logic is used — a resource matching ANY listed
     * category qualifies.
     *
     * @param query the search and filter criteria
     * @return matching resources sorted by title (unmodifiable list)
     */
    public List<LearningResource> search(final ResourceQuery query) {
        Objects.requireNonNull(query, "Query must not be null");

        var stream = resources.values().stream();

        // Free-text search across all searchable fields
        if (!query.searchText().isBlank()) {
            final var searchLower = query.searchText().toLowerCase();
            stream = stream.filter(resource -> resource.searchableText().contains(searchLower));
        }

        // Type filter
        if (query.type() != null) {
            stream = stream.filter(resource -> resource.type() == query.type());
        }

        // Category filter (OR logic: match ANY listed category)
        if (!query.categories().isEmpty()) {
            stream = stream.filter(resource ->
                    query.categories().stream().anyMatch(resource::hasCategory));
        }

        // Concept area filter
        if (query.conceptArea() != null) {
            stream = stream.filter(resource -> resource.hasConcept(query.conceptArea()));
        }

        // Difficulty range filter
        if (query.minDifficulty() != null || query.maxDifficulty() != null) {
            final var min = query.minDifficulty() != null
                    ? query.minDifficulty()
                    : server.learningresources.model.DifficultyLevel.BEGINNER;
            final var max = query.maxDifficulty() != null
                    ? query.maxDifficulty()
                    : server.learningresources.model.DifficultyLevel.EXPERT;
            stream = stream.filter(resource -> resource.isDifficultyInRange(min, max));
        }

        // Freshness filter
        if (query.freshness() != null) {
            stream = stream.filter(resource -> resource.freshness() == query.freshness());
        }

        // Official-only filter
        if (query.officialOnly()) {
            stream = stream.filter(LearningResource::isOfficial);
        }

        // Tag filter (AND logic: resource must have ALL tags)
        if (!query.tags().isEmpty()) {
            stream = stream.filter(resource -> query.tags().stream().allMatch(resource::hasTag));
        }

        // Free-only filter
        if (query.freeOnly()) {
            stream = stream.filter(LearningResource::isFree);
        }

        var resultStream = stream.sorted((first, second) ->
                first.title().compareToIgnoreCase(second.title()));

        if (query.maxResults() > 0) {
            resultStream = resultStream.limit(query.maxResults());
        }

        return Collections.unmodifiableList(resultStream.toList());
    }

    /**
     * Returns all resources in the vault.
     *
     * @return unmodifiable list of all resources
     */
    public List<LearningResource> listAll() {
        return search(ResourceQuery.ALL);
    }

    /**
     * Returns the total number of resources in the vault.
     *
     * @return the resource count
     */
    public int size() {
        return resources.size();
    }

    /**
     * Returns all unique categories present in the vault.
     *
     * @return list of categories with at least one resource
     */
    public List<ResourceCategory> availableCategories() {
        final var categories = new ArrayList<ResourceCategory>();
        for (final ResourceCategory category : ResourceCategory.values()) {
            final var hasResources = resources.values().stream()
                    .anyMatch(resource -> resource.hasCategory(category));
            if (hasResources) {
                categories.add(category);
            }
        }
        return Collections.unmodifiableList(categories);
    }

    /**
     * Removes a resource by its ID.
     *
     * @param resourceId the resource to remove
     * @return {@code true} if the resource was present and removed
     */
    public boolean remove(final String resourceId) {
        return resources.remove(resourceId) != null;
    }
}
