package server.atlassian.v1.client;

import server.atlassian.common.JsonUtils;
import server.atlassian.v1.model.ConnectionConfig;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * REST API client for Jira Cloud (API v3).
 *
 * <p>Provides methods for searching, reading, creating, updating,
 * and transitioning Jira issues. All methods return raw JSON strings
 * that are parsed by the handler layer.
 *
 * <p><strong>API Reference:</strong>
 * <a href="https://developer.atlassian.com/cloud/jira/platform/rest/v3/">Jira REST API v3</a>
 *
 * @see AtlassianRestClient
 */
public class JiraClient {

    private static final Logger LOGGER = Logger.getLogger(JiraClient.class.getName());
    private static final String API_BASE = "/rest/api/3";

    private final AtlassianRestClient restClient;

    /**
     * Creates a Jira client with the given connection configuration.
     *
     * @param config the connection settings
     */
    public JiraClient(final ConnectionConfig config) {
        Objects.requireNonNull(config, "ConnectionConfig must not be null");
        this.restClient = new AtlassianRestClient(config);
    }

    /**
     * Searches for issues using JQL (Jira Query Language).
     *
     * @param jql       the JQL query string
     * @param maxResults maximum number of results to return
     * @return the raw JSON response
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String searchIssues(final String jql, final int maxResults)
            throws IOException, InterruptedException {
        Objects.requireNonNull(jql, "JQL query must not be null");
        final var encodedJql = java.net.URLEncoder.encode(jql, java.nio.charset.StandardCharsets.UTF_8);
        final var path = API_BASE + "/search?jql=" + encodedJql + "&maxResults=" + maxResults;
        LOGGER.info("Searching Jira issues: " + jql);
        return restClient.get(path);
    }

    /**
     * Gets a single issue by its key.
     *
     * @param issueKey the issue key (e.g., "PROJ-123")
     * @return the raw JSON response
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String getIssue(final String issueKey) throws IOException, InterruptedException {
        Objects.requireNonNull(issueKey, "Issue key must not be null");
        final var path = API_BASE + "/issue/" + issueKey;
        LOGGER.info("Getting Jira issue: " + issueKey);
        return restClient.get(path);
    }

    /**
     * Creates a new issue.
     *
     * @param requestBody the JSON request body containing issue fields
     * @return the raw JSON response with the created issue
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String createIssue(final String requestBody)
            throws IOException, InterruptedException {
        Objects.requireNonNull(requestBody, "Request body must not be null");
        final var path = API_BASE + "/issue";
        LOGGER.info("Creating Jira issue");
        return restClient.post(path, requestBody);
    }

    /**
     * Updates an existing issue's fields.
     *
     * @param issueKey    the issue key
     * @param requestBody the JSON body with fields to update
     * @return the raw JSON response
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String updateIssue(final String issueKey, final String requestBody)
            throws IOException, InterruptedException {
        Objects.requireNonNull(issueKey, "Issue key must not be null");
        Objects.requireNonNull(requestBody, "Request body must not be null");
        final var path = API_BASE + "/issue/" + issueKey;
        LOGGER.info("Updating Jira issue: " + issueKey);
        return restClient.put(path, requestBody);
    }

    /**
     * Transitions an issue to a new status.
     *
     * @param issueKey     the issue key
     * @param transitionId the target transition ID
     * @return the raw JSON response
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String transitionIssue(final String issueKey, final String transitionId)
            throws IOException, InterruptedException {
        Objects.requireNonNull(issueKey, "Issue key must not be null");
        Objects.requireNonNull(transitionId, "Transition ID must not be null");
        final var path = API_BASE + "/issue/" + issueKey + "/transitions";
        final var body = "{\"transition\":{\"id\":\"" + transitionId + "\"}}";
        LOGGER.info("Transitioning issue " + issueKey + " with transition " + transitionId);
        return restClient.post(path, body);
    }

    /**
     * Lists available transitions for an issue.
     *
     * @param issueKey the issue key
     * @return the raw JSON response with available transitions
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String getTransitions(final String issueKey)
            throws IOException, InterruptedException {
        Objects.requireNonNull(issueKey, "Issue key must not be null");
        final var path = API_BASE + "/issue/" + issueKey + "/transitions";
        return restClient.get(path);
    }

    /**
     * Lists all accessible projects.
     *
     * @return the raw JSON response with project list
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String listProjects() throws IOException, InterruptedException {
        final var path = API_BASE + "/project";
        LOGGER.info("Listing Jira projects");
        return restClient.get(path);
    }

    /**
     * Gets the active sprint for a board.
     *
     * @param boardId the agile board ID
     * @return the raw JSON response with sprint details
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String getActiveSprint(final int boardId)
            throws IOException, InterruptedException {
        // Agile API uses a different base path
        final var path = "/rest/agile/1.0/board/" + boardId + "/sprint?state=active";
        LOGGER.info("Getting active sprint for board " + boardId);
        return restClient.get(path);
    }

    /**
     * Gets all issues in the active sprint for a board.
     *
     * @param boardId    the agile board ID
     * @param maxResults maximum number of results
     * @return the raw JSON response with sprint issues
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String getSprintIssues(final int boardId, final int maxResults)
            throws IOException, InterruptedException {
        final var path = "/rest/agile/1.0/board/" + boardId
                + "/issue?state=active&maxResults=" + maxResults;
        LOGGER.info("Getting sprint issues for board " + boardId);
        return restClient.get(path);
    }

    /**
     * Adds a comment to an issue.
     *
     * @param issueKey   the issue key (e.g., "PROJ-123")
     * @param commentBody the comment text (plain text — wrapped in ADF format)
     * @return the raw JSON response with the created comment
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String addComment(final String issueKey, final String commentBody)
            throws IOException, InterruptedException {
        Objects.requireNonNull(issueKey, "Issue key must not be null");
        Objects.requireNonNull(commentBody, "Comment body must not be null");
        final var path = API_BASE + "/issue/" + issueKey + "/comment";
        final var body = """
                {
                  "body": {
                    "type": "doc",
                    "version": 1,
                    "content": [{
                      "type": "paragraph",
                      "content": [{ "type": "text", "text": %s }]
                    }]
                  }
                }
                """.formatted(toJsonString(commentBody));
        LOGGER.info("Adding comment to issue: " + issueKey);
        return restClient.post(path, body);
    }

    /**
     * Gets the comments for an issue.
     *
     * @param issueKey   the issue key
     * @param maxResults maximum number of comments to return
     * @return the raw JSON response with comments
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String getComments(final String issueKey, final int maxResults)
            throws IOException, InterruptedException {
        Objects.requireNonNull(issueKey, "Issue key must not be null");
        final var path = API_BASE + "/issue/" + issueKey + "/comment?maxResults=" + maxResults;
        LOGGER.info("Getting comments for issue: " + issueKey);
        return restClient.get(path);
    }

    /**
     * Assigns an issue to a user.
     *
     * @param issueKey  the issue key
     * @param accountId the account ID of the user to assign (null to unassign)
     * @return the raw JSON response
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String assignIssue(final String issueKey, final String accountId)
            throws IOException, InterruptedException {
        Objects.requireNonNull(issueKey, "Issue key must not be null");
        final var path = API_BASE + "/issue/" + issueKey + "/assignee";
        final var body = accountId == null
                ? "{\"accountId\": null}"
                : "{\"accountId\": " + toJsonString(accountId) + "}";
        LOGGER.info("Assigning issue " + issueKey + " to " + accountId);
        return restClient.put(path, body);
    }

    /**
     * Deletes a Jira issue.
     *
     * <p><strong>Warning:</strong> This operation is irreversible.
     * Subtasks are not automatically deleted.
     *
     * @param issueKey the issue key (e.g., "PROJ-123")
     * @return the raw response (usually empty)
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String deleteIssue(final String issueKey)
            throws IOException, InterruptedException {
        Objects.requireNonNull(issueKey, "Issue key must not be null");
        final var path = API_BASE + "/issue/" + issueKey;
        LOGGER.info("Deleting Jira issue: " + issueKey);
        return restClient.delete(path);
    }

    /**
     * Searches for Jira users matching a query string.
     *
     * @param query      the query string (display name, email, or username fragment)
     * @param maxResults maximum number of results
     * @return the raw JSON response with matched users
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String searchUsers(final String query, final int maxResults)
            throws IOException, InterruptedException {
        Objects.requireNonNull(query, "Query must not be null");
        final var encoded = java.net.URLEncoder.encode(query, java.nio.charset.StandardCharsets.UTF_8);
        final var path = API_BASE + "/user/search?query=" + encoded + "&maxResults=" + maxResults;
        LOGGER.info("Searching Jira users: " + query);
        return restClient.get(path);
    }

    /**
     * Lists all agile boards accessible to the user.
     *
     * @param maxResults maximum number of boards to return
     * @return the raw JSON response with board list
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String listBoards(final int maxResults)
            throws IOException, InterruptedException {
        final var path = "/rest/agile/1.0/board?maxResults=" + maxResults;
        LOGGER.info("Listing agile boards");
        return restClient.get(path);
    }

    /**
     * Links two Jira issues together.
     *
     * @param inwardIssueKey  the inward issue key (e.g., "PROJ-123")
     * @param outwardIssueKey the outward issue key (e.g., "PROJ-456")
     * @param linkType        the link type name (e.g., "Blocks", "Relates", "Duplicate")
     * @return the raw response (usually empty for 201)
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String linkIssues(final String inwardIssueKey,
                             final String outwardIssueKey,
                             final String linkType)
            throws IOException, InterruptedException {
        Objects.requireNonNull(inwardIssueKey, "Inward issue key must not be null");
        Objects.requireNonNull(outwardIssueKey, "Outward issue key must not be null");
        Objects.requireNonNull(linkType, "Link type must not be null");
        final var path = API_BASE + "/issueLink";
        final var body = """
                {
                  "type": { "name": %s },
                  "inwardIssue": { "key": %s },
                  "outwardIssue": { "key": %s }
                }
                """.formatted(toJsonString(linkType),
                toJsonString(inwardIssueKey), toJsonString(outwardIssueKey));
        LOGGER.info("Linking issues: " + inwardIssueKey + " → " + outwardIssueKey
                + " (type: " + linkType + ")");
        return restClient.post(path, body);
    }

    /**
     * Adds a worklog entry to an issue.
     *
     * @param issueKey  the issue key (e.g., "PROJ-123")
     * @param timeSpent the time spent in Jira format (e.g., "2h 30m", "1d")
     * @param comment   an optional comment describing the work (may be null)
     * @return the raw JSON response with the created worklog
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String addWorklog(final String issueKey,
                             final String timeSpent,
                             final String comment)
            throws IOException, InterruptedException {
        Objects.requireNonNull(issueKey, "Issue key must not be null");
        Objects.requireNonNull(timeSpent, "Time spent must not be null");
        final var path = API_BASE + "/issue/" + issueKey + "/worklog";
        final var body = comment == null || comment.isBlank()
                ? "{\"timeSpent\": " + toJsonString(timeSpent) + "}"
                : "{\"timeSpent\": " + toJsonString(timeSpent)
                  + ", \"comment\": {\"type\":\"doc\",\"version\":1,"
                  + "\"content\":[{\"type\":\"paragraph\",\"content\":"
                  + "[{\"type\":\"text\",\"text\":" + toJsonString(comment) + "}]}]}}";
        LOGGER.info("Adding worklog to " + issueKey + ": " + timeSpent);
        return restClient.post(path, body);
    }

    /**
     * Wraps a string value in JSON quotes with proper escaping.
     */
    private String toJsonString(final String value) {
        if (value == null) return "null";
        return "\"" + JsonUtils.escapeJson(value) + "\"";
    }
}
