package server.learningresources.handler;

import server.learningresources.model.ConceptArea;
import server.learningresources.model.ContentFreshness;
import server.learningresources.model.DifficultyLevel;
import server.learningresources.model.LanguageApplicability;
import server.learningresources.model.LearningResource;
import server.learningresources.model.ResourceCategory;
import server.learningresources.model.ResourceQuery;
import server.learningresources.model.ResourceType;
import server.learningresources.vault.DiscoveryResult;
import server.learningresources.vault.ResourceDiscovery;
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
 *   <li>{@code discover_resources} — smart discovery with intent classification</li>
 *   <li>{@code scrape_url} — scrape and summarize a URL</li>
 *   <li>{@code read_url} — scrape and return full content of a URL</li>
 *   <li>{@code add_resource} — add a custom resource to the vault</li>
 *   <li>{@code add_resource_from_url} — smart add via URL scraping and metadata inference</li>
 *   <li>{@code export_results} — export discovery/search results as Markdown</li>
 * </ul>
 */
public class ToolHandler {

    private static final Logger LOGGER = Logger.getLogger(ToolHandler.class.getName());

    private final SearchHandler searchHandler;
    private final ScrapeHandler scrapeHandler;
    private final ExportHandler exportHandler;
    private final UrlResourceHandler urlResourceHandler;
    private final ResourceDiscovery discovery;
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
        this.exportHandler = new ExportHandler();
        this.urlResourceHandler = new UrlResourceHandler(vault);
        this.discovery = new ResourceDiscovery(vault);
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
            case "discover_resources" -> handleDiscover(arguments);
            case "scrape_url" -> handleScrapeUrl(arguments);
            case "read_url" -> handleReadUrl(arguments);
            case "add_resource" -> handleAddResource(arguments);
            case "add_resource_from_url" -> urlResourceHandler.addFromUrl(arguments);
            case "export_results" -> handleExport(arguments);
            default -> "Unknown tool: '" + toolName + "'. Available tools: "
                    + "search_resources, browse_vault, get_resource, list_categories, "
                    + "discover_resources, scrape_url, read_url, add_resource, "
                    + "add_resource_from_url, export_results";
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
        final var conceptArg = arguments.get("concept");
        final var difficultyArg = arguments.get("difficulty");
        final var maxDifficultyArg = arguments.get("max_difficulty");
        final var freshnessArg = arguments.get("freshness");
        final var officialArg = arguments.getOrDefault("official_only", "false");
        final var freeOnlyArg = arguments.getOrDefault("free_only", "false");

        List<ResourceCategory> categories = List.of();
        if (categoryArg != null && !categoryArg.isBlank()) {
            try {
                categories = List.of(ResourceCategory.fromDisplayName(categoryArg));
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

        ConceptArea concept = null;
        if (conceptArg != null && !conceptArg.isBlank()) {
            try {
                concept = ConceptArea.fromString(conceptArg);
            } catch (IllegalArgumentException ignored) {
                return "Invalid concept: '" + conceptArg + "'";
            }
        }

        DifficultyLevel minDifficulty = null;
        if (difficultyArg != null && !difficultyArg.isBlank()) {
            try {
                minDifficulty = DifficultyLevel.fromString(difficultyArg);
            } catch (IllegalArgumentException ignored) {
                return "Invalid difficulty: '" + difficultyArg + "'";
            }
        }

        DifficultyLevel maxDifficulty = null;
        if (maxDifficultyArg != null && !maxDifficultyArg.isBlank()) {
            try {
                maxDifficulty = DifficultyLevel.fromString(maxDifficultyArg);
            } catch (IllegalArgumentException ignored) {
                return "Invalid max_difficulty: '" + maxDifficultyArg + "'";
            }
        }

        ContentFreshness freshness = null;
        if (freshnessArg != null && !freshnessArg.isBlank()) {
            try {
                freshness = ContentFreshness.fromString(freshnessArg);
            } catch (IllegalArgumentException ignored) {
                return "Invalid freshness: '" + freshnessArg + "'";
            }
        }

        final var query = new ResourceQuery(
                searchText, type, categories, concept,
                minDifficulty, maxDifficulty, freshness,
                Boolean.parseBoolean(officialArg),
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
            final var difficultyArg = arguments.getOrDefault("difficulty", "intermediate");
            final var difficulty = DifficultyLevel.fromString(difficultyArg);
            final var langArg = arguments.getOrDefault("language_applicability", "universal");
            final var langApplicability = LanguageApplicability.fromString(langArg);
            final var tagsArg = arguments.getOrDefault("tags", "");
            final var tags = tagsArg.isBlank() ? List.<String>of() : List.of(tagsArg.split(","));

            final var resource = new LearningResource(
                    arguments.get("id"),
                    arguments.get("title"),
                    arguments.get("url"),
                    arguments.get("description"),
                    type,
                    List.of(category),
                    List.of(),
                    tags,
                    arguments.getOrDefault("author", ""),
                    difficulty,
                    ContentFreshness.ACTIVELY_MAINTAINED,
                    false,
                    true,
                    langApplicability,
                    Instant.now()
            );

            vault.add(resource);
            return "Added resource: " + resource.title() + " (ID: " + resource.id() + ")";

        } catch (IllegalArgumentException validationError) {
            return "Invalid input: " + validationError.getMessage();
        }
    }

    /**
     * Handles the {@code discover_resources} tool call.
     *
     * <p>Uses the smart discovery engine to classify intent and find resources.
     *
     * @param arguments must contain "query"; optionally "concept", "min_difficulty", "max_difficulty"
     * @return formatted discovery results with suggestions
     */
    private String handleDiscover(final Map<String, String> arguments) {
        final var query = arguments.getOrDefault("query", "");
        final var conceptArg = arguments.get("concept");

        DiscoveryResult result;
        if (conceptArg != null && !conceptArg.isBlank()) {
            try {
                final var concept = ConceptArea.fromString(conceptArg);
                final var minArg = arguments.get("min_difficulty");
                final var maxArg = arguments.get("max_difficulty");
                final var minDiff = minArg != null ? DifficultyLevel.fromString(minArg) : null;
                final var maxDiff = maxArg != null ? DifficultyLevel.fromString(maxArg) : null;
                result = discovery.discoverByConcept(concept, minDiff, maxDiff);
            } catch (IllegalArgumentException invalidArg) {
                return "Invalid argument: " + invalidArg.getMessage();
            }
        } else {
            result = discovery.discover(query);
        }

        return formatDiscoveryResult(result);
    }

    /**
     * Handles the {@code export_results} tool call.
     *
     * @param arguments must contain "query"; optionally "format" (md, pdf, word)
     * @return exported content string
     */
    private String handleExport(final Map<String, String> arguments) {
        final var query = arguments.getOrDefault("query", "");
        final var formatArg = arguments.getOrDefault("format", "md");

        try {
            final var format = ExportHandler.OutputFormat.fromString(formatArg);
            final var result = discovery.discover(query);
            return exportHandler.export(result, format);
        } catch (IllegalArgumentException invalidFormat) {
            return "Invalid format: " + invalidFormat.getMessage();
        }
    }

    /**
     * Formats a discovery result for MCP response output.
     */
    private String formatDiscoveryResult(final DiscoveryResult result) {
        final var builder = new StringBuilder();
        builder.append("🔍 Discovery (").append(result.queryType()).append(")\n");
        builder.append(result.summary()).append("\n\n");

        if (!result.isEmpty()) {
            for (final var scored : result.results()) {
                final var resource = scored.resource();
                final var badge = (resource.isOfficial() ? "✅" : "") + (resource.isFree() ? "🆓" : "💰");
                builder.append("  ").append(badge).append(" ")
                        .append(resource.title())
                        .append("  [").append(resource.type().getDisplayName()).append("]")
                        .append("  (").append(resource.difficulty().getDisplayName()).append(")")
                        .append("  score=").append(scored.score()).append("\n")
                        .append("     ").append(resource.url()).append("\n\n");
            }
        }

        if (!result.suggestions().isEmpty()) {
            builder.append("💡 Suggestions:\n");
            for (final var suggestion : result.suggestions()) {
                builder.append("  • ").append(suggestion).append("\n");
            }
        }

        return builder.toString();
    }
}
