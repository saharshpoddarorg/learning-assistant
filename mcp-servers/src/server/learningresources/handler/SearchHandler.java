package server.learningresources.handler;

import server.learningresources.model.LearningResource;
import server.learningresources.model.ResourceCategory;
import server.learningresources.model.ResourceQuery;
import server.learningresources.model.ResourceType;
import server.learningresources.vault.ResourceVault;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Handles search and browse tool calls against the resource vault.
 *
 * <p>Supports text search, category/type filtering, and vault browsing.
 * Returns formatted results suitable for MCP tool responses.
 *
 * <p><strong>MCP Tools:</strong>
 * <ul>
 *   <li>{@code search_resources} — full-text search with filters</li>
 *   <li>{@code browse_vault} — list resources by category or type</li>
 *   <li>{@code get_resource} — get details of a specific resource by ID</li>
 * </ul>
 */
public class SearchHandler {

    private static final Logger LOGGER = Logger.getLogger(SearchHandler.class.getName());

    private final ResourceVault vault;

    /**
     * Creates a {@link SearchHandler} with the given vault.
     *
     * @param vault the resource vault to search
     */
    public SearchHandler(final ResourceVault vault) {
        this.vault = Objects.requireNonNull(vault, "ResourceVault must not be null");
    }

    /**
     * Searches the vault with a free-text query.
     *
     * @param searchText the text to search for
     * @return formatted search results
     */
    public String search(final String searchText) {
        final var query = ResourceQuery.byText(searchText);
        final var results = vault.search(query);
        return formatResults(results, "Search: \"" + searchText + "\"");
    }

    /**
     * Searches with full query criteria.
     *
     * @param query the search query
     * @return formatted search results
     */
    public String search(final ResourceQuery query) {
        final var results = vault.search(query);
        return formatResults(results, "Search results");
    }

    /**
     * Lists all resources in a specific category.
     *
     * @param categoryName the category display name (e.g., "java", "web")
     * @return formatted category listing
     */
    public String browseByCategory(final String categoryName) {
        try {
            final var category = ResourceCategory.fromDisplayName(categoryName);
            final var query = ResourceQuery.byCategory(category);
            final var results = vault.search(query);
            return formatResults(results, "Category: " + category.getDisplayName());
        } catch (IllegalArgumentException invalidCategory) {
            return "Unknown category: '" + categoryName + "'. Available: "
                    + vault.availableCategories().stream()
                    .map(ResourceCategory::getDisplayName)
                    .collect(Collectors.joining(", "));
        }
    }

    /**
     * Lists all resources of a specific type.
     *
     * @param typeName the type display name (e.g., "tutorial", "documentation")
     * @return formatted type listing
     */
    public String browseByType(final String typeName) {
        try {
            final var type = ResourceType.fromDisplayName(typeName);
            final var query = ResourceQuery.byType(type);
            final var results = vault.search(query);
            return formatResults(results, "Type: " + type.getDisplayName());
        } catch (IllegalArgumentException invalidType) {
            return "Unknown type: '" + typeName + "'. Valid types: documentation, tutorial, "
                    + "blog, article, video, book, interactive, api-reference, cheat-sheet, repository";
        }
    }

    /**
     * Gets detailed information about a specific resource by ID.
     *
     * @param resourceId the resource identifier
     * @return formatted resource details, or an error message if not found
     */
    public String getResourceDetails(final String resourceId) {
        return vault.findById(resourceId)
                .map(this::formatResourceDetail)
                .orElse("Resource not found: '" + resourceId + "'. Use search_resources to find resources.");
    }

    /**
     * Lists all available categories with resource counts.
     *
     * @return formatted category summary
     */
    public String listCategories() {
        final var builder = new StringBuilder("📚 Available Categories\n\n");

        for (final var category : vault.availableCategories()) {
            final var count = vault.search(ResourceQuery.byCategory(category)).size();
            builder.append("  • ").append(category.getDisplayName())
                    .append(" (").append(count).append(" resources)\n");
        }

        builder.append("\nTotal: ").append(vault.size()).append(" resources in vault");
        return builder.toString();
    }

    /**
     * Formats a list of resources into a readable output.
     *
     * @param resources the resources to format
     * @param heading   the section heading
     * @return formatted string
     */
    private String formatResults(final List<LearningResource> resources, final String heading) {
        if (resources.isEmpty()) {
            return heading + "\nNo resources found matching your criteria.";
        }

        final var builder = new StringBuilder(heading)
                .append(" (").append(resources.size()).append(" results)\n\n");

        for (final var resource : resources) {
            builder.append("  ").append(resource.isFree() ? "🆓" : "💰").append(" ")
                    .append(resource.title())
                    .append("  [").append(resource.type().getDisplayName()).append("]")
                    .append("  (").append(resource.difficulty()).append(")\n")
                    .append("     ").append(resource.url()).append("\n")
                    .append("     ").append(truncateDescription(resource.description())).append("\n")
                    .append("     ID: ").append(resource.id()).append("\n\n");
        }

        return builder.toString();
    }

    /**
     * Formats detailed information about a single resource.
     *
     * @param resource the resource to format
     * @return formatted detail string
     */
    private String formatResourceDetail(final LearningResource resource) {
        return new StringBuilder()
                .append("📖 ").append(resource.title()).append("\n")
                .append("─".repeat(50)).append("\n")
                .append("URL:        ").append(resource.url()).append("\n")
                .append("Type:       ").append(resource.type().getDisplayName()).append("\n")
                .append("Difficulty: ").append(resource.difficulty()).append("\n")
                .append("Author:     ").append(resource.author().isEmpty() ? "(unknown)" : resource.author()).append("\n")
                .append("Free:       ").append(resource.isFree() ? "Yes" : "No").append("\n")
                .append("Categories: ").append(resource.categories().stream()
                        .map(ResourceCategory::getDisplayName)
                        .collect(Collectors.joining(", "))).append("\n")
                .append("Tags:       ").append(String.join(", ", resource.tags())).append("\n\n")
                .append(resource.description()).append("\n")
                .toString();
    }

    /**
     * Truncates a description to a reasonable display length.
     *
     * @param description the full description
     * @return truncated description (max 120 chars)
     */
    private String truncateDescription(final String description) {
        final int maxLength = 120;
        if (description.length() <= maxLength) {
            return description;
        }
        return description.substring(0, maxLength - 3) + "...";
    }
}
