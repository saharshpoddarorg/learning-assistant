package server.atlassian.v2.handler;

import server.atlassian.v2.client.JiraClientV2;
import server.atlassian.v2.model.Product;
import server.atlassian.v2.model.ToolResult;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static server.atlassian.v2.handler.HandlerUtils.*;

/**
 * Handles all Jira MCP tool calls (34 tools).
 */
public final class JiraToolHandler {

    private static final Logger LOGGER = Logger.getLogger(JiraToolHandler.class.getName());
    private static final Product P = Product.JIRA;

    private final JiraClientV2 client;

    public JiraToolHandler(final JiraClientV2 client) {
        this.client = Objects.requireNonNull(client);
    }

    /**
     * Dispatches a Jira tool call.
     */
    public ToolResult handle(final String tool, final Map<String, String> args) {
        return switch (tool) {
            case "jira_search_issues"     -> searchIssues(args);
            case "jira_get_issue"         -> getIssue(args);
            case "jira_create_issue"      -> createIssue(args);
            case "jira_update_issue"      -> updateIssue(args);
            case "jira_transition_issue"  -> transitionIssue(args);
            case "jira_list_projects"     -> listProjects();
            case "jira_get_project"       -> getProject(args);
            case "jira_get_sprint"        -> getActiveSprint(args);
            case "jira_add_comment"       -> addComment(args);
            case "jira_get_comments"      -> getComments(args);
            case "jira_assign_issue"      -> assignIssue(args);
            case "jira_get_sprint_issues" -> getSprintIssues(args);
            case "jira_get_transitions"   -> getTransitions(args);
            case "jira_delete_issue"      -> deleteIssue(args);
            case "jira_search_users"      -> searchUsers(args);
            case "jira_link_issues"       -> linkIssues(args);
            case "jira_add_worklog"       -> addWorklog(args);
            case "jira_get_boards"        -> listBoards(args);
            case "jira_get_components"    -> getComponents(args);
            case "jira_create_component"  -> createComponent(args);
            case "jira_get_versions"      -> getVersions(args);
            case "jira_create_version"    -> createVersion(args);
            case "jira_get_watchers"      -> getWatchers(args);
            case "jira_add_watcher"       -> addWatcher(args);
            case "jira_remove_watcher"    -> removeWatcher(args);
            case "jira_get_issue_types"   -> getIssueTypes(args);
            case "jira_get_priorities"    -> getPriorities();
            case "jira_get_statuses"      -> getStatuses();
            case "jira_bulk_create_issues" -> bulkCreateIssues(args);
            case "jira_list_dashboards"   -> listDashboards(args);
            case "jira_get_filters"       -> getFilters();
            case "jira_get_my_issues"     -> getMyIssues(args);
            case "jira_get_attachments"   -> getAttachments(args);
            case "jira_whoami"            -> whoami();
            default -> ToolResult.error(P, tool, "Unknown Jira tool: " + tool);
        };
    }

    // ── Implementation ───────────────────────────────────────────────────────

    private ToolResult searchIssues(final Map<String, String> args) {
        final var query = requireArg(args, "query");
        if (query == null) return ToolResult.error(P, "jira_search_issues", "Missing required: 'query'");
        final int max = parseMaxResults(args.get("maxResults"));
        final var jql = looksLikeJql(query) ? query : "text ~ \"" + escapeJql(query) + "\" ORDER BY updated DESC";
        try {
            return ToolResult.success(P, "jira_search_issues", client.searchIssues(jql, max));
        } catch (IOException | InterruptedException ex) {
            return apiError("jira_search_issues", ex);
        }
    }

    private ToolResult getIssue(final Map<String, String> args) {
        final var key = requireArg(args, "issueKey");
        if (key == null) return ToolResult.error(P, "jira_get_issue", "Missing required: 'issueKey'");
        try {
            return ToolResult.success(P, "jira_get_issue", client.getIssue(key));
        } catch (IOException | InterruptedException ex) {
            return apiError("jira_get_issue", ex);
        }
    }

    private ToolResult createIssue(final Map<String, String> args) {
        final var projectKey = requireArg(args, "projectKey");
        final var summary = requireArg(args, "summary");
        if (projectKey == null) return ToolResult.error(P, "jira_create_issue", "Missing required: 'projectKey'");
        if (summary == null) return ToolResult.error(P, "jira_create_issue", "Missing required: 'summary'");
        final var issueType = args.getOrDefault("issueType", "Task");
        final var desc = args.getOrDefault("description", "");
        final var body = buildCreateIssueJson(projectKey, summary, issueType, desc);
        try {
            return ToolResult.success(P, "jira_create_issue", client.createIssue(body));
        } catch (IOException | InterruptedException ex) {
            return apiError("jira_create_issue", ex);
        }
    }

    private ToolResult updateIssue(final Map<String, String> args) {
        final var key = requireArg(args, "issueKey");
        if (key == null) return ToolResult.error(P, "jira_update_issue", "Missing required: 'issueKey'");
        final var body = buildUpdateIssueJson(args);
        try {
            return ToolResult.success(P, "jira_update_issue", client.updateIssue(key, body));
        } catch (IOException | InterruptedException ex) {
            return apiError("jira_update_issue", ex);
        }
    }

    private ToolResult transitionIssue(final Map<String, String> args) {
        final var key = requireArg(args, "issueKey");
        final var transId = requireArg(args, "transitionId");
        if (key == null) return ToolResult.error(P, "jira_transition_issue", "Missing required: 'issueKey'");
        if (transId == null) return ToolResult.error(P, "jira_transition_issue", "Missing required: 'transitionId'");
        try {
            return ToolResult.success(P, "jira_transition_issue",
                    client.transitionIssue(key, "{\"transition\":{\"id\":\"" + escapeJson(transId) + "\"}}"));
        } catch (IOException | InterruptedException ex) {
            return apiError("jira_transition_issue", ex);
        }
    }

    private ToolResult listProjects() {
        try { return ToolResult.success(P, "jira_list_projects", client.listProjects()); }
        catch (IOException | InterruptedException ex) { return apiError("jira_list_projects", ex); }
    }

    private ToolResult getProject(final Map<String, String> args) {
        final var key = requireArg(args, "projectKey");
        if (key == null) return ToolResult.error(P, "jira_get_project", "Missing required: 'projectKey'");
        try { return ToolResult.success(P, "jira_get_project", client.getProject(key)); }
        catch (IOException | InterruptedException ex) { return apiError("jira_get_project", ex); }
    }

    private ToolResult getActiveSprint(final Map<String, String> args) {
        final int boardId = parseInt(requireArg(args, "boardId"), -1);
        if (boardId < 0) return ToolResult.error(P, "jira_get_sprint", "Missing required: 'boardId'");
        try { return ToolResult.success(P, "jira_get_sprint", client.getActiveSprint(boardId)); }
        catch (IOException | InterruptedException ex) { return apiError("jira_get_sprint", ex); }
    }

    private ToolResult addComment(final Map<String, String> args) {
        final var key = requireArg(args, "issueKey");
        final var comment = requireArg(args, "comment");
        if (key == null) return ToolResult.error(P, "jira_add_comment", "Missing required: 'issueKey'");
        if (comment == null) return ToolResult.error(P, "jira_add_comment", "Missing required: 'comment'");
        try {
            final var body = "{\"body\":{\"type\":\"doc\",\"version\":1,\"content\":[{\"type\":\"paragraph\",\"content\":[{\"type\":\"text\",\"text\":\"" + escapeJson(comment) + "\"}]}]}}";
            return ToolResult.success(P, "jira_add_comment", client.addComment(key, body));
        } catch (IOException | InterruptedException ex) { return apiError("jira_add_comment", ex); }
    }

    private ToolResult getComments(final Map<String, String> args) {
        final var key = requireArg(args, "issueKey");
        if (key == null) return ToolResult.error(P, "jira_get_comments", "Missing required: 'issueKey'");
        try { return ToolResult.success(P, "jira_get_comments", client.getIssueComments(key, parseMaxResults(args.get("maxResults")))); }
        catch (IOException | InterruptedException ex) { return apiError("jira_get_comments", ex); }
    }

    private ToolResult assignIssue(final Map<String, String> args) {
        final var key = requireArg(args, "issueKey");
        if (key == null) return ToolResult.error(P, "jira_assign_issue", "Missing required: 'issueKey'");
        final var accountId = args.getOrDefault("accountId", "");
        final var body = accountId.isBlank() ? "{\"accountId\":null}" : "{\"accountId\":\"" + escapeJson(accountId) + "\"}";
        try { return ToolResult.success(P, "jira_assign_issue", client.assignIssue(key, body)); }
        catch (IOException | InterruptedException ex) { return apiError("jira_assign_issue", ex); }
    }

    private ToolResult getSprintIssues(final Map<String, String> args) {
        final int boardId = parseInt(requireArg(args, "boardId"), -1);
        if (boardId < 0) return ToolResult.error(P, "jira_get_sprint_issues", "Missing required: 'boardId'");
        try { return ToolResult.success(P, "jira_get_sprint_issues", client.getSprintIssues(boardId, parseMaxResults(args.get("maxResults")))); }
        catch (IOException | InterruptedException ex) { return apiError("jira_get_sprint_issues", ex); }
    }

    private ToolResult getTransitions(final Map<String, String> args) {
        final var key = requireArg(args, "issueKey");
        if (key == null) return ToolResult.error(P, "jira_get_transitions", "Missing required: 'issueKey'");
        try { return ToolResult.success(P, "jira_get_transitions", client.getIssueTransitions(key)); }
        catch (IOException | InterruptedException ex) { return apiError("jira_get_transitions", ex); }
    }

    private ToolResult deleteIssue(final Map<String, String> args) {
        final var key = requireArg(args, "issueKey");
        if (key == null) return ToolResult.error(P, "jira_delete_issue", "Missing required: 'issueKey'");
        try { return ToolResult.success(P, "jira_delete_issue", client.deleteIssue(key)); }
        catch (IOException | InterruptedException ex) { return apiError("jira_delete_issue", ex); }
    }

    private ToolResult searchUsers(final Map<String, String> args) {
        final var query = requireArg(args, "query");
        if (query == null) return ToolResult.error(P, "jira_search_users", "Missing required: 'query'");
        try { return ToolResult.success(P, "jira_search_users", client.searchUsers(query, parseMaxResults(args.get("maxResults")))); }
        catch (IOException | InterruptedException ex) { return apiError("jira_search_users", ex); }
    }

    private ToolResult linkIssues(final Map<String, String> args) {
        final var inward = requireArg(args, "inwardIssueKey");
        final var outward = requireArg(args, "outwardIssueKey");
        final var linkType = args.getOrDefault("linkType", "Relates");
        if (inward == null || outward == null) return ToolResult.error(P, "jira_link_issues", "Missing required: 'inwardIssueKey' and 'outwardIssueKey'");
        final var body = "{\"type\":{\"name\":\"" + escapeJson(linkType) + "\"},\"inwardIssue\":{\"key\":\"" + escapeJson(inward) + "\"},\"outwardIssue\":{\"key\":\"" + escapeJson(outward) + "\"}}";
        try { return ToolResult.success(P, "jira_link_issues", client.linkIssues(body)); }
        catch (IOException | InterruptedException ex) { return apiError("jira_link_issues", ex); }
    }

    private ToolResult addWorklog(final Map<String, String> args) {
        final var key = requireArg(args, "issueKey");
        final var timeSpent = requireArg(args, "timeSpent");
        if (key == null || timeSpent == null) return ToolResult.error(P, "jira_add_worklog", "Missing required: 'issueKey' and 'timeSpent'");
        final var comment = args.getOrDefault("comment", "");
        final var body = "{\"timeSpent\":\"" + escapeJson(timeSpent) + "\"" + (comment.isBlank() ? "" : ",\"comment\":{\"type\":\"doc\",\"version\":1,\"content\":[{\"type\":\"paragraph\",\"content\":[{\"type\":\"text\",\"text\":\"" + escapeJson(comment) + "\"}]}]}") + "}";
        try { return ToolResult.success(P, "jira_add_worklog", client.addWorklog(key, body)); }
        catch (IOException | InterruptedException ex) { return apiError("jira_add_worklog", ex); }
    }

    private ToolResult listBoards(final Map<String, String> args) {
        try { return ToolResult.success(P, "jira_get_boards", client.listBoards(parseMaxResults(args.get("maxResults")))); }
        catch (IOException | InterruptedException ex) { return apiError("jira_get_boards", ex); }
    }

    private ToolResult getComponents(final Map<String, String> args) {
        final var key = requireArg(args, "projectKey");
        if (key == null) return ToolResult.error(P, "jira_get_components", "Missing required: 'projectKey'");
        try { return ToolResult.success(P, "jira_get_components", client.getProjectComponents(key)); }
        catch (IOException | InterruptedException ex) { return apiError("jira_get_components", ex); }
    }

    private ToolResult createComponent(final Map<String, String> args) {
        final var project = requireArg(args, "projectKey");
        final var name = requireArg(args, "name");
        if (project == null || name == null) return ToolResult.error(P, "jira_create_component", "Missing required: 'projectKey' and 'name'");
        final var body = "{\"project\":\"" + escapeJson(project) + "\",\"name\":\"" + escapeJson(name) + "\"}";
        try { return ToolResult.success(P, "jira_create_component", client.createComponent(body)); }
        catch (IOException | InterruptedException ex) { return apiError("jira_create_component", ex); }
    }

    private ToolResult getVersions(final Map<String, String> args) {
        final var key = requireArg(args, "projectKey");
        if (key == null) return ToolResult.error(P, "jira_get_versions", "Missing required: 'projectKey'");
        try { return ToolResult.success(P, "jira_get_versions", client.getProjectVersions(key)); }
        catch (IOException | InterruptedException ex) { return apiError("jira_get_versions", ex); }
    }

    private ToolResult createVersion(final Map<String, String> args) {
        final var project = requireArg(args, "projectKey");
        final var name = requireArg(args, "name");
        if (project == null || name == null) return ToolResult.error(P, "jira_create_version", "Missing required: 'projectKey' and 'name'");
        final var body = "{\"project\":\"" + escapeJson(project) + "\",\"name\":\"" + escapeJson(name) + "\"}";
        try { return ToolResult.success(P, "jira_create_version", client.createVersion(body)); }
        catch (IOException | InterruptedException ex) { return apiError("jira_create_version", ex); }
    }

    private ToolResult getWatchers(final Map<String, String> args) {
        final var key = requireArg(args, "issueKey");
        if (key == null) return ToolResult.error(P, "jira_get_watchers", "Missing required: 'issueKey'");
        try { return ToolResult.success(P, "jira_get_watchers", client.getIssueWatchers(key)); }
        catch (IOException | InterruptedException ex) { return apiError("jira_get_watchers", ex); }
    }

    private ToolResult addWatcher(final Map<String, String> args) {
        final var key = requireArg(args, "issueKey");
        final var accountId = requireArg(args, "accountId");
        if (key == null || accountId == null) return ToolResult.error(P, "jira_add_watcher", "Missing required: 'issueKey' and 'accountId'");
        try { return ToolResult.success(P, "jira_add_watcher", client.addWatcher(key, accountId)); }
        catch (IOException | InterruptedException ex) { return apiError("jira_add_watcher", ex); }
    }

    private ToolResult removeWatcher(final Map<String, String> args) {
        final var key = requireArg(args, "issueKey");
        final var accountId = requireArg(args, "accountId");
        if (key == null || accountId == null) return ToolResult.error(P, "jira_remove_watcher", "Missing required: 'issueKey' and 'accountId'");
        try { return ToolResult.success(P, "jira_remove_watcher", client.removeWatcher(key, accountId)); }
        catch (IOException | InterruptedException ex) { return apiError("jira_remove_watcher", ex); }
    }

    private ToolResult getIssueTypes(final Map<String, String> args) {
        final var key = requireArg(args, "projectKey");
        if (key == null) return ToolResult.error(P, "jira_get_issue_types", "Missing required: 'projectKey'");
        try { return ToolResult.success(P, "jira_get_issue_types", client.getIssueTypes(key)); }
        catch (IOException | InterruptedException ex) { return apiError("jira_get_issue_types", ex); }
    }

    private ToolResult getPriorities() {
        try { return ToolResult.success(P, "jira_get_priorities", client.getPriorities()); }
        catch (IOException | InterruptedException ex) { return apiError("jira_get_priorities", ex); }
    }

    private ToolResult getStatuses() {
        try { return ToolResult.success(P, "jira_get_statuses", client.getStatuses()); }
        catch (IOException | InterruptedException ex) { return apiError("jira_get_statuses", ex); }
    }

    private ToolResult bulkCreateIssues(final Map<String, String> args) {
        final var issuesJson = requireArg(args, "issuesJson");
        if (issuesJson == null) return ToolResult.error(P, "jira_bulk_create_issues", "Missing required: 'issuesJson'");
        try { return ToolResult.success(P, "jira_bulk_create_issues", client.bulkCreateIssues(issuesJson)); }
        catch (IOException | InterruptedException ex) { return apiError("jira_bulk_create_issues", ex); }
    }

    private ToolResult listDashboards(final Map<String, String> args) {
        try { return ToolResult.success(P, "jira_list_dashboards", client.listDashboards(parseMaxResults(args.get("maxResults")))); }
        catch (IOException | InterruptedException ex) { return apiError("jira_list_dashboards", ex); }
    }

    private ToolResult getFilters() {
        try { return ToolResult.success(P, "jira_get_filters", client.getFilters()); }
        catch (IOException | InterruptedException ex) { return apiError("jira_get_filters", ex); }
    }

    private ToolResult getMyIssues(final Map<String, String> args) {
        final int max = parseMaxResults(args.get("maxResults"));
        try { return ToolResult.success(P, "jira_get_my_issues", client.searchIssues("assignee = currentUser() ORDER BY updated DESC", max)); }
        catch (IOException | InterruptedException ex) { return apiError("jira_get_my_issues", ex); }
    }

    private ToolResult getAttachments(final Map<String, String> args) {
        final var key = requireArg(args, "issueKey");
        if (key == null) return ToolResult.error(P, "jira_get_attachments", "Missing required: 'issueKey'");
        try { return ToolResult.success(P, "jira_get_attachments", client.getIssueAttachments(key)); }
        catch (IOException | InterruptedException ex) { return apiError("jira_get_attachments", ex); }
    }

    private ToolResult whoami() {
        try { return ToolResult.success(P, "jira_whoami", client.getCurrentUser()); }
        catch (IOException | InterruptedException ex) { return apiError("jira_whoami", ex); }
    }

    // ── JSON builders ────────────────────────────────────────────────────────

    private static String buildCreateIssueJson(final String project, final String summary,
                                               final String type, final String description) {
        final var sb = new StringBuilder("{\"fields\":{");
        sb.append("\"project\":{\"key\":\"").append(escapeJson(project)).append("\"},");
        sb.append("\"summary\":\"").append(escapeJson(summary)).append("\",");
        sb.append("\"issuetype\":{\"name\":\"").append(escapeJson(type)).append("\"}");
        if (!description.isBlank()) {
            sb.append(",\"description\":{\"type\":\"doc\",\"version\":1,\"content\":[{\"type\":\"paragraph\",\"content\":[{\"type\":\"text\",\"text\":\"")
                    .append(escapeJson(description)).append("\"}]}]}");
        }
        sb.append("}}");
        return sb.toString();
    }

    private static String buildUpdateIssueJson(final Map<String, String> args) {
        final var sb = new StringBuilder("{\"fields\":{");
        var first = true;
        if (args.containsKey("summary")) {
            sb.append("\"summary\":\"").append(escapeJson(args.get("summary"))).append("\"");
            first = false;
        }
        if (args.containsKey("description")) {
            if (!first) sb.append(",");
            sb.append("\"description\":{\"type\":\"doc\",\"version\":1,\"content\":[{\"type\":\"paragraph\",\"content\":[{\"type\":\"text\",\"text\":\"")
                    .append(escapeJson(args.get("description"))).append("\"}]}]}");
        }
        sb.append("}}");
        return sb.toString();
    }

    private ToolResult apiError(final String tool, final Exception ex) {
        LOGGER.log(Level.WARNING, "Jira API error in " + tool, ex);
        return ToolResult.error(P, tool, "Jira API error: " + ex.getMessage());
    }
}
