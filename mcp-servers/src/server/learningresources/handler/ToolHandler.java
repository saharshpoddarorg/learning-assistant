package server.learningresources.handler;

import server.learningresources.model.LearningResource;
import server.learningresources.model.ResourceCategory;
import server.learningresources.model.ResourceQuery;
import server.learningresources.model.ResourceType;
import server.learningresources.vault.ResourceVault;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Routes incoming MCP tool calls to the appropriate handler.
 *
 * <p>Acts as the dispatcher between MCP protocol messages and the
 * server's business logic. Each tool name maps to a specific handler method.
 *
 * <p><strong>Registered Tools:</strong>
 * <ul>
 *   <li>{@code search_resources} — search the vault by text, category, type, or tags</li>
 *   <li>{@code browse_vault} — browse resources by category or type</li>
 *   <li>{@code get_resource} — get details of a specific resource</li>
 *   <li>{@code list_categories} — list available categories with counts</li>
 *   <li>{@code scrape_url} — scrape and summarize a URL</li>
 *   <li>{@code read_url} — scrape and return full content of a URL</li>
 *   <li>{@code add_resource} — add a custom resource to the vault</li>
 * </ul>
 */
public class ToolHandler {

    private static final Logger LOGGER = Logger.getLogger(ToolHandler.class.getName());

    private final SearchHandler searchHandler;
    private final ScrapeHandler scrapeHandler;
    private final ResourceVault vault;

    /**
     * Creates a {@link ToolHandler} with the given dependencies.
     *
     * @param vault the resource vault
     */
    public ToolHandler(final ResourceVault vault) {
        this.vault = Objects.requireNonNull(vault, "ResourceVault must not be null");
        this.searchHandler = new SearchHandler(vault);
        this.scrapeHandler = new ScrapeHandler();
    }

    /**
     * Dispatches an MCP tool call to the appropriate handler.
     *
     * @param toolName  the name of the tool being invoked
     * @param arguments the tool arguments as a key-value map
     * @return the tool response as a string
     */
    public String handleToolCall(final String toolName, final Map<String, String> arguments) {
        Objects.requireNonNull(toolName, "Tool name must not be null");
        Objects.requireNonNull(arguments, "Arguments must not be null");

        LOGGER.info("Handling tool call: " + toolName);

        return switch (toolName) {
            case "search_resources" -> handleSearch(arguments);
            case "browse_vault" -> handleBrowse(arguments);
            case "get_resource" -> handleGetResource(arguments);
            case "list_categories" -> searchHandler.listCategories();
            case "scrape_url" -> handleScrapeUrl(arguments);
            case "read_url" -> handleReadUrl(arguments);
            case "add_resource" -> handleAddResource(arguments);
            default -> "Unknown tool: '" + toolName + "'. Available tools: "
                    + "search_resources, browse_vault, get_resource, list_categories, "
                    + "scrape_url, read_url, add_resource";
        };
    }

    /**
     * Handles the {@code search_resources} tool call.
     *
     * @param arguments must contain "query"; optionally "category", "type", "difficulty"
     * @return formatted search results
     */
    private String handleSearch(final Map<String, String> arguments) {
        final var searchText = arguments.getOrDefault("query", "");
        final var categoryArg = arguments.get("category");
        final var typeArg = arguments.get("type");
        final var difficultyArg = arguments.get("difficulty");
        final var freeOnlyArg = arguments.getOrDefault("free_only", "false");

        ResourceCategory category = null;
        if (categoryArg != null && !categoryArg.isBlank()) {
            try {
                category = ResourceCategory.fromDisplayName(categoryArg);
            } catch (IllegalArgumentException ignored) {
                return "Invalid category: '" + categoryArg + "'";
            }
        }

        ResourceType type = null;
        if (typeArg != null && !typeArg.isBlank()) {
            try {
                type = ResourceType.fromDisplayName(typeArg);
            } catch (IllegalArgumentException ignored) {
                return "Invalid type: '" + typeArg + "'";
            }
        }

        final var query = new ResourceQuery(
                searchText, type, category, difficultyArg,
                List.of(), Boolean.parseBoolean(freeOnlyArg), 25
        );

        return searchHandler.search(query);
    }

    /**
     * Handles the {@code browse_vault} tool call.
     *
     * @param arguments must contain either "category" or "type"
     * @return formatted browse results
     */
    private String handleBrowse(final Map<String, String> arguments) {
        final var categoryArg = arguments.get("category");
        final var typeArg = arguments.get("type");

        if (categoryArg != null && !categoryArg.isBlank()) {
            return searchHandler.browseByCategory(categoryArg);
        }
        if (typeArg != null && !typeArg.isBlank()) {
            return searchHandler.browseByType(typeArg);
        }
        return searchHandler.listCategories();
    }

    /**
     * Handles the {@code get_resource} tool call.
     *
     * @param arguments must contain "id"
     * @return formatted resource details
     */
    private String handleGetResource(final Map<String, String> arguments) {
        final var resourceId = arguments.get("id");
        if (resourceId == null || resourceId.isBlank()) {
            return "Missing required argument: 'id'";
        }
        return searchHandler.getResourceDetails(resourceId);
    }

    /**
     * Handles the {@code scrape_url} tool call.
     *
     * @param arguments must contain "url"
     * @return formatted summary of scraped content
     */
    private String handleScrapeUrl(final Map<String, String> arguments) {
        final var url = arguments.get("url");
        if (url == null || url.isBlank()) {
            return "Missing required argument: 'url'";
        }
        return scrapeHandler.scrapeAndFormatSummary(url);
    }

    /**
     * Handles the {@code read_url} tool call.
     *
     * @param arguments must contain "url"
     * @return full formatted content of the scraped URL
     */
    private String handleReadUrl(final Map<String, String> arguments) {
        final var url = arguments.get("url");
        if (url == null || url.isBlank()) {
            return "Missing required argument: 'url'";
        }
        return scrapeHandler.scrapeAndFormatFullContent(url);
    }

    /**
     * Handles the {@code add_resource} tool call.
     *
     * @param arguments must contain "id", "title", "url", "description", "type"
     * @return confirmation message
     */
    private String handleAddResource(final Map<String, String> arguments) {
        final var requiredFields = List.of("id", "title", "url", "description", "type");
        for (final var field : requiredFields) {
            if (!arguments.containsKey(field) || arguments.get(field).isBlank()) {
                return "Missing required argument: '" + field + "'";
            }
        }

        try {
            final var type = ResourceType.fromDisplayName(arguments.get("type"));
            final var categoryArg = arguments.getOrDefault("category", "general");
            final var category = ResourceCategory.fromDisplayName(categoryArg);
            final var difficulty = arguments.getOrDefault("difficulty", "intermediate");
            final var tagsArg = arguments.getOrDefault("tags", "");
            final var tags = tagsArg.isBlank() ? List.<String>of() : List.of(tagsArg.split(","));

            final var resource = new LearningResource(
                    arguments.get("id"),
                    arguments.get("title"),
                    arguments.get("url"),
                    arguments.get("description"),
                    type,
                    List.of(category),
                    tags,
                    arguments.getOrDefault("author", ""),
                    difficulty,
                    true,
                    Instant.now()
            );

            vault.add(resource);
            return "Added resource: " + resource.title() + " (ID: " + resource.id() + ")";

        } catch (IllegalArgumentException validationError) {
            return "Invalid input: " + validationError.getMessage();
        }
    }
}
