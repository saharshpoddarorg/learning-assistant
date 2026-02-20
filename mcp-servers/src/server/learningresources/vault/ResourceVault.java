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
 * search and filter capabilities. It is pre-populated with built-in resources
 * via {@link BuiltInResources} and supports runtime additions.
 *
 * <p>Thread-safe — uses a {@link ConcurrentHashMap} for storage.
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
     * Searches the vault using the given query criteria.
     *
     * @param query the search and filter criteria
     * @return matching resources (unmodifiable list)
     */
    public List<LearningResource> search(final ResourceQuery query) {
        Objects.requireNonNull(query, "Query must not be null");

        var stream = resources.values().stream();

        if (!query.searchText().isBlank()) {
            final var searchLower = query.searchText().toLowerCase();
            stream = stream.filter(resource -> matchesSearchText(resource, searchLower));
        }

        if (query.type() != null) {
            stream = stream.filter(resource -> resource.type() == query.type());
        }

        if (query.category() != null) {
            stream = stream.filter(resource -> resource.hasCategory(query.category()));
        }

        if (query.difficulty() != null) {
            stream = stream.filter(resource -> resource.difficulty().equalsIgnoreCase(query.difficulty()));
        }

        if (!query.tags().isEmpty()) {
            stream = stream.filter(resource -> query.tags().stream().allMatch(resource::hasTag));
        }

        if (query.freeOnly()) {
            stream = stream.filter(LearningResource::isFree);
        }

        var resultStream = stream.sorted((first, second) -> first.title().compareToIgnoreCase(second.title()));

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

    /**
     * Checks whether the given text matches a resource's title, description, or tags.
     *
     * @param resource    the resource to check
     * @param searchLower the lowercase search text
     * @return {@code true} if any field matches
     */
    private boolean matchesSearchText(final LearningResource resource, final String searchLower) {
        return resource.title().toLowerCase().contains(searchLower)
                || resource.description().toLowerCase().contains(searchLower)
                || resource.tags().stream().anyMatch(tag -> tag.toLowerCase().contains(searchLower))
                || resource.author().toLowerCase().contains(searchLower);
    }
}
