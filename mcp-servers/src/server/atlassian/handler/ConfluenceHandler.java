package server.atlassian.handler;

import server.atlassian.client.ConfluenceClient;
import server.atlassian.model.AtlassianProduct;
import server.atlassian.model.ToolResponse;
import server.atlassian.util.JsonExtractor;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles Confluence-related MCP tool calls.
 *
 * <p>Routes Confluence tool invocations to the appropriate
 * {@link ConfluenceClient} method and formats the results.
 *
 * <p><strong>Registered Tools:</strong>
 * <ul>
 *   <li>{@code confluence_search} — search pages using CQL or free text</li>
 *   <li>{@code confluence_get_page} — get full content of a page</li>
 *   <li>{@code confluence_create_page} — create a new page in a space</li>
 *   <li>{@code confluence_update_page} — update an existing page</li>
 *   <li>{@code confluence_list_spaces} — list accessible spaces</li>
 * </ul>
 */
public class ConfluenceHandler {

    private static final Logger LOGGER = Logger.getLogger(ConfluenceHandler.class.getName());

    private final ConfluenceClient confluenceClient;

    /**
     * Creates a Confluence handler with the given client.
     *
     * @param confluenceClient the Confluence REST API client
     */
    public ConfluenceHandler(final ConfluenceClient confluenceClient) {
        this.confluenceClient = Objects.requireNonNull(
                confluenceClient, "ConfluenceClient must not be null");
    }

    /**
     * Searches for Confluence pages using CQL or free text.
     *
     * @param arguments the tool arguments ({@code query}, optional {@code maxResults})
     * @return the tool response with search results
     */
    public ToolResponse searchPages(final Map<String, String> arguments) {
        final var query = arguments.get("query");
        if (query == null || query.isBlank()) {
            return ToolResponse.error(AtlassianProduct.CONFLUENCE, "confluence_search",
                    "Missing required argument: 'query'");
        }

        final int maxResults = parseMaxResults(arguments.get("maxResults"));

        // Auto-detect: if it looks like CQL, use as-is; otherwise wrap in text search
        final var cql = looksLikeCql(query)
                ? query
                : "type = page AND text ~ \"" + escapeCql(query) + "\" ORDER BY lastModified DESC";

        try {
            final var response = confluenceClient.searchPages(cql, maxResults);
            return ToolResponse.success(AtlassianProduct.CONFLUENCE, "confluence_search",
                    formatPageListFromJson(response));
        } catch (IOException | InterruptedException exception) {
            LOGGER.log(Level.WARNING, "Confluence search failed", exception);
            return ToolResponse.error(AtlassianProduct.CONFLUENCE, "confluence_search",
                    "Search failed: " + exception.getMessage());
        }
    }

    /**
     * Gets full content of a Confluence page.
     *
     * @param arguments the tool arguments ({@code pageId})
     * @return the tool response with page content
     */
    public ToolResponse getPage(final Map<String, String> arguments) {
        final var pageId = arguments.get("pageId");
        if (pageId == null || pageId.isBlank()) {
            return ToolResponse.error(AtlassianProduct.CONFLUENCE, "confluence_get_page",
                    "Missing required argument: 'pageId'");
        }

        try {
            final var response = confluenceClient.getPage(pageId);
            return ToolResponse.success(AtlassianProduct.CONFLUENCE, "confluence_get_page",
                    formatPageFromJson(response));
        } catch (IOException | InterruptedException exception) {
            LOGGER.log(Level.WARNING, "Failed to get page: " + pageId, exception);
            return ToolResponse.error(AtlassianProduct.CONFLUENCE, "confluence_get_page",
                    "Failed to get page " + pageId + ": " + exception.getMessage());
        }
    }

    /**
     * Creates a new Confluence page in a space.
     *
     * @param arguments the tool arguments ({@code spaceKey}, {@code title}, {@code body})
     * @return the tool response with the created page
     */
    public ToolResponse createPage(final Map<String, String> arguments) {
        final var spaceKey = arguments.get("spaceKey");
        final var title = arguments.get("title");
        final var body = arguments.get("body");

        if (spaceKey == null || spaceKey.isBlank()) {
            return ToolResponse.error(AtlassianProduct.CONFLUENCE, "confluence_create_page",
                    "Missing required argument: 'spaceKey'");
        }
        if (title == null || title.isBlank()) {
            return ToolResponse.error(AtlassianProduct.CONFLUENCE, "confluence_create_page",
                    "Missing required argument: 'title'");
        }
        if (body == null || body.isBlank()) {
            return ToolResponse.error(AtlassianProduct.CONFLUENCE, "confluence_create_page",
                    "Missing required argument: 'body'");
        }

        final var requestBody = buildCreatePageJson(spaceKey, title, body);

        try {
            final var response = confluenceClient.createPage(requestBody);
            return ToolResponse.success(AtlassianProduct.CONFLUENCE,
                    "confluence_create_page", response);
        } catch (IOException | InterruptedException exception) {
            LOGGER.log(Level.WARNING, "Failed to create page", exception);
            return ToolResponse.error(AtlassianProduct.CONFLUENCE, "confluence_create_page",
                    "Failed to create page: " + exception.getMessage());
        }
    }

    /**
     * Updates an existing Confluence page.
     *
     * @param arguments the tool arguments ({@code pageId}, {@code title}, {@code body}, {@code version})
     * @return the tool response
     */
    public ToolResponse updatePage(final Map<String, String> arguments) {
        final var pageId = arguments.get("pageId");
        final var title = arguments.get("title");
        final var body = arguments.get("body");
        final var versionStr = arguments.get("version");

        if (pageId == null || pageId.isBlank()) {
            return ToolResponse.error(AtlassianProduct.CONFLUENCE, "confluence_update_page",
                    "Missing required argument: 'pageId'");
        }
        if (title == null || title.isBlank()) {
            return ToolResponse.error(AtlassianProduct.CONFLUENCE, "confluence_update_page",
                    "Missing required argument: 'title'");
        }
        if (body == null || body.isBlank()) {
            return ToolResponse.error(AtlassianProduct.CONFLUENCE, "confluence_update_page",
                    "Missing required argument: 'body'");
        }

        final int version = parseVersion(versionStr);
        final var requestBody = buildUpdatePageJson(pageId, title, body, version);

        try {
            final var response = confluenceClient.updatePage(pageId, requestBody);
            return ToolResponse.success(AtlassianProduct.CONFLUENCE,
                    "confluence_update_page", response);
        } catch (IOException | InterruptedException exception) {
            LOGGER.log(Level.WARNING, "Failed to update page: " + pageId, exception);
            return ToolResponse.error(AtlassianProduct.CONFLUENCE, "confluence_update_page",
                    "Failed to update page " + pageId + ": " + exception.getMessage());
        }
    }

    /**
     * Lists all accessible Confluence spaces.
     *
     * @return the tool response with space list
     */
    public ToolResponse listSpaces() {
        try {
            final var response = confluenceClient.listSpaces();
            return ToolResponse.success(AtlassianProduct.CONFLUENCE,
                    "confluence_list_spaces", response);
        } catch (IOException | InterruptedException exception) {
            LOGGER.log(Level.WARNING, "Failed to list spaces", exception);
            return ToolResponse.error(AtlassianProduct.CONFLUENCE, "confluence_list_spaces",
                    "Failed to list spaces: " + exception.getMessage());
        }
    }

    /**
     * Heuristic: does the query look like raw CQL?
     */
    private boolean looksLikeCql(final String query) {
        final var upper = query.toUpperCase();
        return upper.contains("=") || upper.contains("~")
                || upper.contains(" AND ") || upper.contains(" OR ")
                || upper.contains("ORDER BY") || upper.contains("TYPE ")
                || upper.contains("SPACE ");
    }

    /**
     * Escapes special characters for CQL text search.
     */
    private String escapeCql(final String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    /**
     * Escapes special characters for JSON string values.
     * Delegated to {@link HandlerUtils#escapeJson(String)}.
     */
    private String escapeJson(final String text) {
        return HandlerUtils.escapeJson(text);
    }

    /**
     * Gets child pages of a Confluence page.
     *
     * @param arguments the tool arguments ({@code pageId}, optional {@code maxResults})
     * @return the tool response with child page list
     */
    public ToolResponse getPageChildren(final Map<String, String> arguments) {
        final var pageId = arguments.get("pageId");
        if (pageId == null || pageId.isBlank()) {
            return ToolResponse.error(AtlassianProduct.CONFLUENCE, "confluence_get_page_children",
                    "Missing required argument: 'pageId'");
        }

        final int maxResults = HandlerUtils.parseMaxResults(arguments.get("maxResults"));

        try {
            final var response = confluenceClient.getPageChildren(pageId, maxResults);
            return ToolResponse.success(AtlassianProduct.CONFLUENCE,
                    "confluence_get_page_children", formatPageListFromJson(response));
        } catch (IOException | InterruptedException exception) {
            LOGGER.log(Level.WARNING, "Failed to get children of page: " + pageId, exception);
            return ToolResponse.error(AtlassianProduct.CONFLUENCE, "confluence_get_page_children",
                    "Failed to get page children: " + exception.getMessage());
        }
    }

    /**
     * Deletes a Confluence page.
     *
     * @param arguments the tool arguments ({@code pageId})
     * @return the tool response
     */
    public ToolResponse deletePage(final Map<String, String> arguments) {
        final var pageId = arguments.get("pageId");
        if (pageId == null || pageId.isBlank()) {
            return ToolResponse.error(AtlassianProduct.CONFLUENCE, "confluence_delete_page",
                    "Missing required argument: 'pageId'");
        }

        try {
            confluenceClient.deletePage(pageId);
            return ToolResponse.success(AtlassianProduct.CONFLUENCE,
                    "confluence_delete_page", "Page " + pageId + " deleted successfully.");
        } catch (IOException | InterruptedException exception) {
            LOGGER.log(Level.WARNING, "Failed to delete page: " + pageId, exception);
            return ToolResponse.error(AtlassianProduct.CONFLUENCE, "confluence_delete_page",
                    "Failed to delete page: " + exception.getMessage());
        }
    }

    // ── Formatter helpers ────────────────────────────────────────────────────

    /**
     * Parses a Confluence page JSON response into a formatted markdown string.
     */
    private String formatPageFromJson(final String json) {
        final var id      = JsonExtractor.stringOrDefault(json, "id", "?");
        final var title   = JsonExtractor.stringOrDefault(json, "title", "");
        final var status  = JsonExtractor.stringOrDefault(json, "status", "");
        final var spaceKey = JsonExtractor.navigate(json, "space", "key").orElse(
                             JsonExtractor.navigate(json, "spaceId").orElse("-"));
        final var author  = JsonExtractor.navigate(json, "authorId").or(() ->
                            JsonExtractor.navigate(json, "version", "createdBy", "displayName"))
                            .orElse("-");
        final var version = JsonExtractor.intValue(json, "version", 0);
        final var updated = JsonExtractor.navigate(json, "version", "createdAt")
                            .or(() -> JsonExtractor.string(json, "lastModifiedDate"))
                            .orElse("-");
        final var webUrl  = JsonExtractor.navigate(json, "_links", "webui").orElse(
                            JsonExtractor.navigate(json, "links", "webUi").orElse(""));

        // Extract body content
        final var bodyBlock = JsonExtractor.navigateBlock(json, "body", "storage")
                .or(() -> JsonExtractor.navigateBlock(json, "body", "view"))
                .orElse("");
        final var bodyHtml = bodyBlock.isBlank() ? ""
                : JsonExtractor.stringOrDefault(bodyBlock, "value", "");
        final var bodyText = stripHtml(bodyHtml);

        final var sb = new StringBuilder();
        sb.append("## ").append(title).append("\n\n");
        sb.append("**ID:** ").append(id).append("\n");
        sb.append("**Space:** ").append(spaceKey).append("\n");
        sb.append("**Status:** ").append(status).append("\n");
        if (!author.equals("-")) sb.append("**Author:** ").append(author).append("\n");
        if (version > 0) sb.append("**Version:** ").append(version).append("\n");
        sb.append("**Updated:** ").append(updated).append("\n");
        if (!webUrl.isBlank()) sb.append("**URL:** ").append(webUrl).append("\n");
        if (!bodyText.isBlank()) {
            sb.append("\n### Content\n\n").append(bodyText).append("\n");
        }
        return sb.toString();
    }

    /**
     * Parses a Confluence search response into a formatted markdown table.
     */
    private String formatPageListFromJson(final String json) {
        // v1 API returns "results", v2 API returns "results" as well
        final var results = JsonExtractor.arrayBlocks(json, "results");
        final int total = JsonExtractor.intValue(json, "totalSize",
                JsonExtractor.intValue(json, "size", results.size()));

        if (results.isEmpty()) {
            return "No pages found.";
        }

        final var sb = new StringBuilder();
        sb.append("## Pages (").append(total).append(" total, showing ")
          .append(results.size()).append(")\n\n");
        sb.append("| ID | Title | Space | Updated |\n");
        sb.append("|----|-------|-------|---------|\n");

        for (final var page : results) {
            final var id      = JsonExtractor.stringOrDefault(page, "id", "?");
            final var title   = truncate(JsonExtractor.stringOrDefault(page, "title", ""), 40);
            final var spaceKey = JsonExtractor.navigate(page, "space", "key").orElse(
                                  JsonExtractor.stringOrDefault(page, "spaceId", "-"));
            final var updated = JsonExtractor.navigate(page, "version", "when")
                                .or(() -> JsonExtractor.string(page, "lastModifiedDate"))
                                .orElse("-");
            sb.append("| ").append(id)
              .append(" | ").append(title)
              .append(" | ").append(spaceKey)
              .append(" | ").append(updated)
              .append(" |\n");
        }
        return sb.toString();
    }

    /**
     * Strips HTML tags and decodes common entities.
     */
    private String stripHtml(final String html) {
        if (html == null || html.isBlank()) return "";
        return html.replaceAll("<[^>]+>", "")
                .replace("&amp;", "&").replace("&lt;", "<")
                .replace("&gt;", ">").replace("&quot;", "\"")
                .replace("&#39;", "'").replace("&nbsp;", " ")
                .replaceAll("\\s{2,}", " ").trim();
    }

    /**
     * Truncates text to a maximum length with ellipsis.
     * Delegated to {@link HandlerUtils#truncate(String, int)}.
     */
    private String truncate(final String text, final int maxLength) {
        return HandlerUtils.truncate(text, maxLength);
    }

    /**
     * Builds the JSON body for creating a Confluence page (v2 API).
     */
    private String buildCreatePageJson(final String spaceKey,
                                       final String title,
                                       final String body) {
        return """
                {
                  "spaceId": "%s",
                  "status": "current",
                  "title": "%s",
                  "body": {
                    "representation": "storage",
                    "value": "%s"
                  }
                }
                """.formatted(escapeJson(spaceKey), escapeJson(title), escapeJson(body));
    }

    /**
     * Builds the JSON body for updating a Confluence page (v2 API).
     */
    private String buildUpdatePageJson(final String pageId,
                                       final String title,
                                       final String body,
                                       final int version) {
        return """
                {
                  "id": "%s",
                  "status": "current",
                  "title": "%s",
                  "body": {
                    "representation": "storage",
                    "value": "%s"
                  },
                  "version": {
                    "number": %d,
                    "message": "Updated via MCP"
                  }
                }
                """.formatted(escapeJson(pageId), escapeJson(title),
                escapeJson(body), version);
    }

    /**
     * Parses maxResults from arguments with a safe default.
     * Delegated to {@link HandlerUtils#parseMaxResults(String)}.
     */
    private int parseMaxResults(final String value) {
        return HandlerUtils.parseMaxResults(value);
    }

    /**
     * Parses version number with a safe default of 1.
     */
    private int parseVersion(final String value) {
        if (value == null || value.isBlank()) {
            return 1;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ignored) {
            return 1;
        }
    }
}
