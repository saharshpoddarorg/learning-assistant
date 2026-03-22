package server.atlassian.v1.client;

import server.atlassian.v1.model.ConnectionConfig;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * REST API client for Confluence Cloud (API v2).
 *
 * <p>Provides methods for searching, reading, creating, and updating
 * Confluence pages and spaces. All methods return raw JSON strings
 * parsed by the handler layer.
 *
 * <p><strong>API Reference:</strong>
 * <a href="https://developer.atlassian.com/cloud/confluence/rest/v2/">Confluence REST API v2</a>
 *
 * @see AtlassianRestClient
 */
public class ConfluenceClient {

    private static final Logger LOGGER = Logger.getLogger(ConfluenceClient.class.getName());
    private static final String API_V2_BASE = "/api/v2";
    private static final String API_V1_BASE = "/rest/api";

    private final AtlassianRestClient restClient;

    /**
     * Creates a Confluence client with the given connection configuration.
     *
     * @param config the connection settings
     */
    public ConfluenceClient(final ConnectionConfig config) {
        Objects.requireNonNull(config, "ConnectionConfig must not be null");
        this.restClient = new AtlassianRestClient(config);
    }

    /**
     * Searches for pages using CQL (Confluence Query Language).
     *
     * @param cql        the CQL query string
     * @param maxResults maximum number of results
     * @return the raw JSON response
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String searchPages(final String cql, final int maxResults)
            throws IOException, InterruptedException {
        Objects.requireNonNull(cql, "CQL query must not be null");
        final var encodedCql = java.net.URLEncoder.encode(cql, java.nio.charset.StandardCharsets.UTF_8);
        final var path = API_V1_BASE + "/content/search?cql=" + encodedCql + "&limit=" + maxResults;
        LOGGER.info("Searching Confluence pages: " + cql);
        return restClient.get(path);
    }

    /**
     * Gets a single page by its ID, including the body content.
     *
     * @param pageId the page ID
     * @return the raw JSON response
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String getPage(final String pageId) throws IOException, InterruptedException {
        Objects.requireNonNull(pageId, "Page ID must not be null");
        final var path = API_V2_BASE + "/pages/" + pageId + "?body-format=storage";
        LOGGER.info("Getting Confluence page: " + pageId);
        return restClient.get(path);
    }

    /**
     * Creates a new page in a space.
     *
     * @param requestBody the JSON body with page title, space, and content
     * @return the raw JSON response with the created page
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String createPage(final String requestBody)
            throws IOException, InterruptedException {
        Objects.requireNonNull(requestBody, "Request body must not be null");
        final var path = API_V2_BASE + "/pages";
        LOGGER.info("Creating Confluence page");
        return restClient.post(path, requestBody);
    }

    /**
     * Updates an existing page.
     *
     * @param pageId      the page ID to update
     * @param requestBody the JSON body with updated title, body, and version
     * @return the raw JSON response
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String updatePage(final String pageId, final String requestBody)
            throws IOException, InterruptedException {
        Objects.requireNonNull(pageId, "Page ID must not be null");
        Objects.requireNonNull(requestBody, "Request body must not be null");
        final var path = API_V2_BASE + "/pages/" + pageId;
        LOGGER.info("Updating Confluence page: " + pageId);
        return restClient.put(path, requestBody);
    }

    /**
     * Lists all accessible spaces.
     *
     * @return the raw JSON response with space list
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String listSpaces() throws IOException, InterruptedException {
        final var path = API_V2_BASE + "/spaces";
        LOGGER.info("Listing Confluence spaces");
        return restClient.get(path);
    }

    /**
     * Gets the child pages of a parent page.
     *
     * @param pageId     the parent page ID
     * @param maxResults maximum number of child pages to return
     * @return the raw JSON response with child pages
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String getPageChildren(final String pageId, final int maxResults)
            throws IOException, InterruptedException {
        Objects.requireNonNull(pageId, "Page ID must not be null");
        final var path = API_V2_BASE + "/pages/" + pageId + "/children?limit=" + maxResults;
        LOGGER.info("Getting children of Confluence page: " + pageId);
        return restClient.get(path);
    }

    /**
     * Deletes a Confluence page.
     *
     * <p><strong>Warning:</strong> This operation is irreversible.
     *
     * @param pageId the page ID to delete
     * @return the raw response (usually empty for 204)
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String deletePage(final String pageId)
            throws IOException, InterruptedException {
        Objects.requireNonNull(pageId, "Page ID must not be null");
        final var path = API_V2_BASE + "/pages/" + pageId;
        LOGGER.info("Deleting Confluence page: " + pageId);
        return restClient.delete(path);
    }

    /**
     * Gets all labels attached to a page.
     *
     * @param pageId the page ID
     * @return the raw JSON response with labels
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String getPageLabels(final String pageId)
            throws IOException, InterruptedException {
        Objects.requireNonNull(pageId, "Page ID must not be null");
        final var path = API_V1_BASE + "/content/" + pageId + "/label";
        LOGGER.info("Getting labels for Confluence page: " + pageId);
        return restClient.get(path);
    }

    /**
     * Adds a label to a Confluence page.
     *
     * @param pageId the page ID
     * @param label  the label name to add
     * @return the raw JSON response with the added label
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String addPageLabel(final String pageId, final String label)
            throws IOException, InterruptedException {
        Objects.requireNonNull(pageId, "Page ID must not be null");
        Objects.requireNonNull(label, "Label must not be null");
        final var path = API_V1_BASE + "/content/" + pageId + "/label";
        final var body = "[{\"prefix\":\"global\",\"name\":\""
                + label.replace("\\", "\\\\").replace("\"", "\\\"") + "\"}]";
        LOGGER.info("Adding label '" + label + "' to Confluence page: " + pageId);
        return restClient.post(path, body);
    }

    /**
     * Gets comments on a Confluence page.
     *
     * @param pageId     the page ID
     * @param maxResults maximum number of comments to return
     * @return the raw JSON response with comments
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String getPageComments(final String pageId, final int maxResults)
            throws IOException, InterruptedException {
        Objects.requireNonNull(pageId, "Page ID must not be null");
        final var path = API_V2_BASE + "/pages/" + pageId + "/footer-comments?limit=" + maxResults;
        LOGGER.info("Getting comments for Confluence page: " + pageId);
        return restClient.get(path);
    }

    /**
     * Adds a comment to a Confluence page.
     *
     * @param pageId  the page ID
     * @param comment the comment text (stored as storage-format HTML)
     * @return the raw JSON response with the created comment
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String addPageComment(final String pageId, final String comment)
            throws IOException, InterruptedException {
        Objects.requireNonNull(pageId, "Page ID must not be null");
        Objects.requireNonNull(comment, "Comment must not be null");
        final var path = API_V2_BASE + "/pages/" + pageId + "/footer-comments";
        final var escapedComment = comment.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r");
        final var body = """
                {
                  "body": {
                    "representation": "storage",
                    "value": "<p>%s</p>"
                  }
                }
                """.formatted(escapedComment);
        LOGGER.info("Adding comment to Confluence page: " + pageId);
        return restClient.post(path, body);
    }
}
