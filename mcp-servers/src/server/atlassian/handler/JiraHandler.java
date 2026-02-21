package server.atlassian.handler;

import server.atlassian.client.JiraClient;
import server.atlassian.model.AtlassianProduct;
import server.atlassian.model.ToolResponse;
import server.atlassian.util.JsonExtractor;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles Jira-related MCP tool calls.
 *
 * <p>Routes Jira tool invocations to the appropriate {@link JiraClient}
 * method and formats the results for MCP response.
 *
 * <p><strong>Registered Tools (11):</strong>
 * <ul>
 *   <li>{@code jira_search_issues} — search issues by JQL or free text</li>
 *   <li>{@code jira_get_issue} — get full details of a specific issue</li>
 *   <li>{@code jira_create_issue} — create a new issue</li>
 *   <li>{@code jira_update_issue} — update fields on an existing issue</li>
 *   <li>{@code jira_transition_issue} — move an issue to a new status</li>
 *   <li>{@code jira_list_projects} — list accessible projects</li>
 *   <li>{@code jira_get_sprint} — get active sprint for a board</li>
 *   <li>{@code jira_add_comment} — add a comment to an issue</li>
 *   <li>{@code jira_get_comments} — list comments on an issue</li>
 *   <li>{@code jira_assign_issue} — assign or unassign an issue</li>
 *   <li>{@code jira_get_sprint_issues} — get all issues in a sprint</li>
 * </ul>
 */
public class JiraHandler {

    private static final Logger LOGGER = Logger.getLogger(JiraHandler.class.getName());

    private final JiraClient jiraClient;

    /**
     * Creates a Jira handler with the given client.
     *
     * @param jiraClient the Jira REST API client
     */
    public JiraHandler(final JiraClient jiraClient) {
        this.jiraClient = Objects.requireNonNull(jiraClient, "JiraClient must not be null");
    }

    /**
     * Searches for Jira issues using JQL or free text.
     *
     * @param arguments the tool arguments ({@code query}, optional {@code maxResults})
     * @return the tool response with search results
     */
    public ToolResponse searchIssues(final Map<String, String> arguments) {
        final var query = arguments.get("query");
        if (query == null || query.isBlank()) {
            return ToolResponse.error(AtlassianProduct.JIRA, "jira_search_issues",
                    "Missing required argument: 'query'");
        }

        final int maxResults = HandlerUtils.parseMaxResults(arguments.get("maxResults"));

        // Auto-detect: if it looks like raw JQL, use as-is; otherwise wrap in text search
        final var jql = looksLikeJql(query)
                ? query
                : "text ~ \"" + escapeJql(query) + "\" ORDER BY updated DESC";

        try {
            final var response = jiraClient.searchIssues(jql, maxResults);
            return ToolResponse.success(AtlassianProduct.JIRA, "jira_search_issues",
                    formatIssueListFromJson(response));
        } catch (IOException | InterruptedException exception) {
            LOGGER.log(Level.WARNING, "Jira search failed", exception);
            return ToolResponse.error(AtlassianProduct.JIRA, "jira_search_issues",
                    "Search failed: " + exception.getMessage());
        }
    }

    /**
     * Gets full details of a specific Jira issue.
     *
     * @param arguments the tool arguments ({@code issueKey})
     * @return the tool response with issue details
     */
    public ToolResponse getIssue(final Map<String, String> arguments) {
        final var issueKey = arguments.get("issueKey");
        if (issueKey == null || issueKey.isBlank()) {
            return ToolResponse.error(AtlassianProduct.JIRA, "jira_get_issue",
                    "Missing required argument: 'issueKey'");
        }

        try {
            final var response = jiraClient.getIssue(issueKey);
            return ToolResponse.success(AtlassianProduct.JIRA, "jira_get_issue",
                    formatIssueFromJson(response));
        } catch (IOException | InterruptedException exception) {
            LOGGER.log(Level.WARNING, "Failed to get issue: " + issueKey, exception);
            return ToolResponse.error(AtlassianProduct.JIRA, "jira_get_issue",
                    "Failed to get issue " + issueKey + ": " + exception.getMessage());
        }
    }

    /**
     * Creates a new Jira issue.
     *
     * @param arguments the tool arguments ({@code projectKey}, {@code summary}, {@code issueType}, optional fields)
     * @return the tool response with the created issue
     */
    public ToolResponse createIssue(final Map<String, String> arguments) {
        final var projectKey = arguments.get("projectKey");
        final var summary = arguments.get("summary");
        final var issueType = arguments.getOrDefault("issueType", "Task");

        if (projectKey == null || projectKey.isBlank()) {
            return ToolResponse.error(AtlassianProduct.JIRA, "jira_create_issue",
                    "Missing required argument: 'projectKey'");
        }
        if (summary == null || summary.isBlank()) {
            return ToolResponse.error(AtlassianProduct.JIRA, "jira_create_issue",
                    "Missing required argument: 'summary'");
        }

        final var description = arguments.getOrDefault("description", "");
        final var priority = arguments.getOrDefault("priority", "Medium");

        final var requestBody = buildCreateIssueJson(projectKey, summary, issueType,
                description, priority);

        try {
            final var response = jiraClient.createIssue(requestBody);
            return ToolResponse.success(AtlassianProduct.JIRA, "jira_create_issue", response);
        } catch (IOException | InterruptedException exception) {
            LOGGER.log(Level.WARNING, "Failed to create issue", exception);
            return ToolResponse.error(AtlassianProduct.JIRA, "jira_create_issue",
                    "Failed to create issue: " + exception.getMessage());
        }
    }

    /**
     * Updates fields on an existing Jira issue.
     *
     * @param arguments the tool arguments ({@code issueKey}, plus fields to update)
     * @return the tool response
     */
    public ToolResponse updateIssue(final Map<String, String> arguments) {
        final var issueKey = arguments.get("issueKey");
        if (issueKey == null || issueKey.isBlank()) {
            return ToolResponse.error(AtlassianProduct.JIRA, "jira_update_issue",
                    "Missing required argument: 'issueKey'");
        }

        final var requestBody = buildUpdateIssueJson(arguments);

        try {
            final var response = jiraClient.updateIssue(issueKey, requestBody);
            return ToolResponse.success(AtlassianProduct.JIRA, "jira_update_issue",
                    "Issue " + issueKey + " updated successfully. " + response);
        } catch (IOException | InterruptedException exception) {
            LOGGER.log(Level.WARNING, "Failed to update issue: " + issueKey, exception);
            return ToolResponse.error(AtlassianProduct.JIRA, "jira_update_issue",
                    "Failed to update " + issueKey + ": " + exception.getMessage());
        }
    }

    /**
     * Transitions a Jira issue to a new status.
     *
     * @param arguments the tool arguments ({@code issueKey}, {@code transition})
     * @return the tool response
     */
    public ToolResponse transitionIssue(final Map<String, String> arguments) {
        final var issueKey = arguments.get("issueKey");
        final var transition = arguments.get("transition");

        if (issueKey == null || issueKey.isBlank()) {
            return ToolResponse.error(AtlassianProduct.JIRA, "jira_transition_issue",
                    "Missing required argument: 'issueKey'");
        }
        if (transition == null || transition.isBlank()) {
            return ToolResponse.error(AtlassianProduct.JIRA, "jira_transition_issue",
                    "Missing required argument: 'transition'");
        }

        try {
            final var response = jiraClient.transitionIssue(issueKey, transition);
            return ToolResponse.success(AtlassianProduct.JIRA, "jira_transition_issue",
                    "Issue " + issueKey + " transitioned successfully. " + response);
        } catch (IOException | InterruptedException exception) {
            LOGGER.log(Level.WARNING, "Failed to transition issue: " + issueKey, exception);
            return ToolResponse.error(AtlassianProduct.JIRA, "jira_transition_issue",
                    "Failed to transition " + issueKey + ": " + exception.getMessage());
        }
    }

    /**
     * Lists all accessible Jira projects.
     *
     * @return the tool response with project list
     */
    public ToolResponse listProjects() {
        try {
            final var response = jiraClient.listProjects();
            return ToolResponse.success(AtlassianProduct.JIRA, "jira_list_projects", response);
        } catch (IOException | InterruptedException exception) {
            LOGGER.log(Level.WARNING, "Failed to list projects", exception);
            return ToolResponse.error(AtlassianProduct.JIRA, "jira_list_projects",
                    "Failed to list projects: " + exception.getMessage());
        }
    }

    /**
     * Gets the active sprint for a board.
     *
     * @param arguments the tool arguments ({@code boardId})
     * @return the tool response with sprint details
     */
    public ToolResponse getActiveSprint(final Map<String, String> arguments) {
        final var boardIdStr = arguments.get("boardId");
        if (boardIdStr == null || boardIdStr.isBlank()) {
            return ToolResponse.error(AtlassianProduct.JIRA, "jira_get_sprint",
                    "Missing required argument: 'boardId'");
        }

        try {
            final int boardId = Integer.parseInt(boardIdStr);
            final var response = jiraClient.getActiveSprint(boardId);
            return ToolResponse.success(AtlassianProduct.JIRA, "jira_get_sprint", response);
        } catch (NumberFormatException numberException) {
            return ToolResponse.error(AtlassianProduct.JIRA, "jira_get_sprint",
                    "Invalid boardId — must be a number: " + boardIdStr);
        } catch (IOException | InterruptedException exception) {
            LOGGER.log(Level.WARNING, "Failed to get sprint for board " + boardIdStr, exception);
            return ToolResponse.error(AtlassianProduct.JIRA, "jira_get_sprint",
                    "Failed to get sprint: " + exception.getMessage());
        }
    }

    /**
     * Adds a comment to a Jira issue.
     *
     * @param arguments the tool arguments ({@code issueKey}, {@code comment})
     * @return the tool response
     */
    public ToolResponse addComment(final Map<String, String> arguments) {
        final var issueKey = arguments.get("issueKey");
        final var comment = arguments.get("comment");

        if (issueKey == null || issueKey.isBlank()) {
            return ToolResponse.error(AtlassianProduct.JIRA, "jira_add_comment",
                    "Missing required argument: 'issueKey'");
        }
        if (comment == null || comment.isBlank()) {
            return ToolResponse.error(AtlassianProduct.JIRA, "jira_add_comment",
                    "Missing required argument: 'comment'");
        }

        try {
            final var response = jiraClient.addComment(issueKey, comment);
            final var commentId = JsonExtractor.stringOrDefault(response, "id", "?");
            return ToolResponse.success(AtlassianProduct.JIRA, "jira_add_comment",
                    "Comment added to " + issueKey + " (id: " + commentId + ").");
        } catch (IOException | InterruptedException exception) {
            LOGGER.log(Level.WARNING, "Failed to add comment to: " + issueKey, exception);
            return ToolResponse.error(AtlassianProduct.JIRA, "jira_add_comment",
                    "Failed to add comment: " + exception.getMessage());
        }
    }

    /**
     * Gets comments on a Jira issue.
     *
     * @param arguments the tool arguments ({@code issueKey}, optional {@code maxResults})
     * @return the tool response with comments
     */
    public ToolResponse getComments(final Map<String, String> arguments) {
        final var issueKey = arguments.get("issueKey");
        if (issueKey == null || issueKey.isBlank()) {
            return ToolResponse.error(AtlassianProduct.JIRA, "jira_get_comments",
                    "Missing required argument: 'issueKey'");
        }

        final int maxResults = HandlerUtils.parseMaxResults(arguments.get("maxResults"));

        try {
            final var response = jiraClient.getComments(issueKey, maxResults);
            return ToolResponse.success(AtlassianProduct.JIRA, "jira_get_comments",
                    formatCommentsFromJson(issueKey, response));
        } catch (IOException | InterruptedException exception) {
            LOGGER.log(Level.WARNING, "Failed to get comments for: " + issueKey, exception);
            return ToolResponse.error(AtlassianProduct.JIRA, "jira_get_comments",
                    "Failed to get comments: " + exception.getMessage());
        }
    }

    /**
     * Assigns a Jira issue to a user.
     *
     * @param arguments the tool arguments ({@code issueKey}, {@code accountId})
     * @return the tool response
     */
    public ToolResponse assignIssue(final Map<String, String> arguments) {
        final var issueKey = arguments.get("issueKey");
        if (issueKey == null || issueKey.isBlank()) {
            return ToolResponse.error(AtlassianProduct.JIRA, "jira_assign_issue",
                    "Missing required argument: 'issueKey'");
        }

        // accountId is optional — null means unassign
        final var accountId = arguments.get("accountId");

        try {
            jiraClient.assignIssue(issueKey, accountId);
            final var msg = (accountId == null || accountId.isBlank())
                    ? issueKey + " unassigned."
                    : issueKey + " assigned to accountId: " + accountId;
            return ToolResponse.success(AtlassianProduct.JIRA, "jira_assign_issue", msg);
        } catch (IOException | InterruptedException exception) {
            LOGGER.log(Level.WARNING, "Failed to assign issue: " + issueKey, exception);
            return ToolResponse.error(AtlassianProduct.JIRA, "jira_assign_issue",
                    "Failed to assign issue: " + exception.getMessage());
        }
    }

    /**
     * Gets issues in the active sprint for a board.
     *
     * @param arguments the tool arguments ({@code boardId}, optional {@code maxResults})
     * @return the tool response with sprint issues
     */
    public ToolResponse getSprintIssues(final Map<String, String> arguments) {
        final var boardIdStr = arguments.get("boardId");
        if (boardIdStr == null || boardIdStr.isBlank()) {
            return ToolResponse.error(AtlassianProduct.JIRA, "jira_get_sprint_issues",
                    "Missing required argument: 'boardId'");
        }

        try {
            final int boardId = Integer.parseInt(boardIdStr);
            final int maxResults = HandlerUtils.parseMaxResults(arguments.get("maxResults"));
            final var response = jiraClient.getSprintIssues(boardId, maxResults);
            return ToolResponse.success(AtlassianProduct.JIRA, "jira_get_sprint_issues",
                    formatIssueListFromJson(response));
        } catch (NumberFormatException numberException) {
            return ToolResponse.error(AtlassianProduct.JIRA, "jira_get_sprint_issues",
                    "Invalid boardId — must be a number: " + boardIdStr);
        } catch (IOException | InterruptedException exception) {
            LOGGER.log(Level.WARNING, "Failed to get sprint issues for board " + boardIdStr, exception);
            return ToolResponse.error(AtlassianProduct.JIRA, "jira_get_sprint_issues",
                    "Failed to get sprint issues: " + exception.getMessage());
        }
    }

    // ── Formatter helpers ────────────────────────────────────────────────────

    /**
     * Parses a Jira issue JSON response into a formatted markdown string.
     */
    private String formatIssueFromJson(final String json) {
        final var key = JsonExtractor.stringOrDefault(json, "key", "?");
        final var fields = JsonExtractor.block(json, "fields").orElse("{}");

        final var summary     = JsonExtractor.stringOrDefault(fields, "summary", "");
        final var descBlock   = JsonExtractor.block(fields, "description").orElse("");
        final var description = descBlock.isBlank() ? "" : JsonExtractor.extractAdfText(descBlock);
        final var issueType   = JsonExtractor.navigate(fields, "issuetype", "name").orElse("-");
        final var status      = JsonExtractor.navigate(fields, "status", "name").orElse("-");
        final var statusCat   = JsonExtractor.navigate(fields, "status", "statusCategory", "name").orElse("");
        final var priority    = JsonExtractor.navigate(fields, "priority", "name").orElse("-");
        final var assignee    = JsonExtractor.isNull(fields, "assignee") ? "Unassigned"
                : JsonExtractor.navigate(fields, "assignee", "displayName").orElse("Unassigned");
        final var reporter    = JsonExtractor.navigate(fields, "reporter", "displayName").orElse("-");
        final var projectKey  = JsonExtractor.navigate(fields, "project", "key").orElse("-");
        final var labels      = JsonExtractor.stringList(fields, "labels");
        final var created     = JsonExtractor.stringOrDefault(fields, "created", "-");
        final var updated     = JsonExtractor.stringOrDefault(fields, "updated", "-");

        final var sb = new StringBuilder();
        sb.append("## ").append(key).append(" — ").append(summary).append("\n\n");
        sb.append("**Status:** ").append(status);
        if (!statusCat.isBlank()) sb.append(" (").append(statusCat).append(")");
        sb.append("\n");
        sb.append("**Type:** ").append(issueType).append("\n");
        sb.append("**Priority:** ").append(priority).append("\n");
        sb.append("**Assignee:** ").append(assignee).append("\n");
        sb.append("**Reporter:** ").append(reporter).append("\n");
        sb.append("**Project:** ").append(projectKey).append("\n");
        if (!labels.isEmpty()) {
            sb.append("**Labels:** ").append(String.join(", ", labels)).append("\n");
        }
        sb.append("**Created:** ").append(created).append("\n");
        sb.append("**Updated:** ").append(updated).append("\n");
        if (!description.isBlank()) {
            sb.append("\n### Description\n\n").append(description).append("\n");
        }
        return sb.toString();
    }

    /**
     * Parses a Jira search response (list of issues) into a formatted markdown table.
     */
    private String formatIssueListFromJson(final String json) {
        final int total = JsonExtractor.intValue(json, "total", 0);
        final var issues = JsonExtractor.arrayBlocks(json, "issues");

        if (issues.isEmpty()) {
            return "No issues found.";
        }

        final var sb = new StringBuilder();
        sb.append("## Issues (").append(total).append(" total, showing ")
          .append(issues.size()).append(")\n\n");
        sb.append("| Key | Summary | Status | Type | Assignee |\n");
        sb.append("|-----|---------|--------|------|----------|\n");

        for (final var issue : issues) {
            final var k = JsonExtractor.stringOrDefault(issue, "key", "?");
            final var f = JsonExtractor.block(issue, "fields").orElse("{}");
            final var sum    = HandlerUtils.truncate(JsonExtractor.stringOrDefault(f, "summary", ""), 50);
            final var stat   = JsonExtractor.navigate(f, "status", "name").orElse("-");
            final var type   = JsonExtractor.navigate(f, "issuetype", "name").orElse("-");
            final var assign = JsonExtractor.isNull(f, "assignee") ? "-"
                    : JsonExtractor.navigate(f, "assignee", "displayName").orElse("-");
            sb.append("| ").append(k)
              .append(" | ").append(sum)
              .append(" | ").append(stat)
              .append(" | ").append(type)
              .append(" | ").append(assign)
              .append(" |\n");
        }
        return sb.toString();
    }

    /**
     * Formats the comments on a Jira issue as readable markdown.
     */
    private String formatCommentsFromJson(final String issueKey, final String json) {
        final var comments = JsonExtractor.arrayBlocks(json, "comments");
        if (comments.isEmpty()) {
            return issueKey + " has no comments.";
        }

        final var sb = new StringBuilder();
        sb.append("## Comments on ").append(issueKey)
          .append(" (").append(comments.size()).append(")\n\n");

        for (final var comment : comments) {
            final var author  = JsonExtractor.navigate(comment, "author", "displayName").orElse("?");
            final var created = JsonExtractor.stringOrDefault(comment, "created", "");
            final var bodyBlock = JsonExtractor.block(comment, "body").orElse("");
            final var text = bodyBlock.isBlank() ? "" : JsonExtractor.extractAdfText(bodyBlock);

            sb.append("**").append(author).append("** — ").append(created).append("\n");
            if (!text.isBlank()) sb.append(text).append("\n");
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Truncates text to a maximum length with ellipsis.
     * Delegated to {@link HandlerUtils#truncate(String, int)}.
     */
    private String truncate(final String text, final int maxLength) {
        return HandlerUtils.truncate(text, maxLength);
    }

    /**
     * Heuristic: does the query look like raw JQL?
     *
     * @param query the user's query string
     * @return {@code true} if it appears to be JQL
     */
    private boolean looksLikeJql(final String query) {
        final var upper = query.toUpperCase();
        return upper.contains("=") || upper.contains("~")
                || upper.contains(" AND ") || upper.contains(" OR ")
                || upper.contains("ORDER BY") || upper.contains("PROJECT ")
                || upper.contains("STATUS ");
    }

    /**
     * Escapes special characters for JQL text search.
     *
     * @param text the raw text
     * @return the escaped text
     */
    private String escapeJql(final String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    /**
     * Builds the JSON body for creating a Jira issue.
     */
    private String buildCreateIssueJson(final String projectKey,
                                        final String summary,
                                        final String issueType,
                                        final String description,
                                        final String priority) {
        return """
                {
                  "fields": {
                    "project": { "key": "%s" },
                    "summary": "%s",
                    "issuetype": { "name": "%s" },
                    "description": {
                      "type": "doc",
                      "version": 1,
                      "content": [{ "type": "paragraph", "content": [{ "type": "text", "text": "%s" }] }]
                    },
                    "priority": { "name": "%s" }
                  }
                }
                """.formatted(projectKey, escapeJson(summary), issueType,
                escapeJson(description), priority);
    }

    /**
     * Builds the JSON body for updating a Jira issue.
     */
    private String buildUpdateIssueJson(final Map<String, String> arguments) {
        final var builder = new StringBuilder("{\"fields\":{");
        var first = true;

        if (arguments.containsKey("summary")) {
            builder.append("\"summary\":\"").append(escapeJson(arguments.get("summary"))).append("\"");
            first = false;
        }
        if (arguments.containsKey("priority")) {
            if (!first) builder.append(",");
            builder.append("\"priority\":{\"name\":\"")
                    .append(escapeJson(arguments.get("priority"))).append("\"}");
            first = false;
        }
        if (arguments.containsKey("assignee")) {
            if (!first) builder.append(",");
            builder.append("\"assignee\":{\"accountId\":\"")
                    .append(escapeJson(arguments.get("assignee"))).append("\"}");
        }

        builder.append("}}");
        return builder.toString();
    }

    /**
     * Escapes special characters for JSON string values.
     * Delegated to {@link HandlerUtils#escapeJson(String)}.
     */
    private String escapeJson(final String text) {
        return HandlerUtils.escapeJson(text);
    }

    /**
     * Parses maxResults from arguments with a safe default.
     * Delegated to {@link HandlerUtils#parseMaxResults(String)}.
     */
    private int parseMaxResults(final String value) {
        return HandlerUtils.parseMaxResults(value);
    }
}
