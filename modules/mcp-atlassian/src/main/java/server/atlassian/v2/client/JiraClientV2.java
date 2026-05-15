package server.atlassian.v2.client;

import server.atlassian.v2.auth.AuthManager;

import java.io.IOException;

/**
 * Jira REST API v3 client — comprehensive coverage.
 *
 * <p>All methods return raw JSON strings. Formatting is done by the handler layer.
 */
public final class JiraClientV2 {

    private final HttpClientV2 http;

    public JiraClientV2(final String baseUrl, final AuthManager auth, final int timeoutMs) {
        this.http = new HttpClientV2(baseUrl, auth, timeoutMs);
    }

    // ── Search & Read ────────────────────────────────────────────────────────

    public String searchIssues(final String jql, final int maxResults) throws IOException, InterruptedException {
        return http.get("/rest/api/3/search?jql=" + encode(jql) + "&maxResults=" + maxResults);
    }

    public String getIssue(final String key) throws IOException, InterruptedException {
        return http.get("/rest/api/3/issue/" + encode(key));
    }

    public String getIssueComments(final String key, final int max) throws IOException, InterruptedException {
        return http.get("/rest/api/3/issue/" + encode(key) + "/comment?maxResults=" + max);
    }

    public String getIssueTransitions(final String key) throws IOException, InterruptedException {
        return http.get("/rest/api/3/issue/" + encode(key) + "/transitions");
    }

    public String getIssueWatchers(final String key) throws IOException, InterruptedException {
        return http.get("/rest/api/3/issue/" + encode(key) + "/watchers");
    }

    public String getIssueAttachments(final String key) throws IOException, InterruptedException {
        return http.get("/rest/api/3/issue/" + encode(key) + "?fields=attachment");
    }

    // ── Create & Update ──────────────────────────────────────────────────────

    public String createIssue(final String jsonBody) throws IOException, InterruptedException {
        return http.post("/rest/api/3/issue", jsonBody);
    }

    public String updateIssue(final String key, final String jsonBody) throws IOException, InterruptedException {
        return http.put("/rest/api/3/issue/" + encode(key), jsonBody);
    }

    public String transitionIssue(final String key, final String jsonBody) throws IOException, InterruptedException {
        return http.post("/rest/api/3/issue/" + encode(key) + "/transitions", jsonBody);
    }

    public String assignIssue(final String key, final String jsonBody) throws IOException, InterruptedException {
        return http.put("/rest/api/3/issue/" + encode(key) + "/assignee", jsonBody);
    }

    public String deleteIssue(final String key) throws IOException, InterruptedException {
        return http.delete("/rest/api/3/issue/" + encode(key));
    }

    // ── Comments ─────────────────────────────────────────────────────────────

    public String addComment(final String key, final String jsonBody) throws IOException, InterruptedException {
        return http.post("/rest/api/3/issue/" + encode(key) + "/comment", jsonBody);
    }

    // ── Watchers ─────────────────────────────────────────────────────────────

    public String addWatcher(final String key, final String accountId) throws IOException, InterruptedException {
        return http.post("/rest/api/3/issue/" + encode(key) + "/watchers", "\"" + accountId + "\"");
    }

    public String removeWatcher(final String key, final String accountId) throws IOException, InterruptedException {
        return http.delete("/rest/api/3/issue/" + encode(key) + "/watchers?accountId=" + encode(accountId));
    }

    // ── Issue Links ──────────────────────────────────────────────────────────

    public String linkIssues(final String jsonBody) throws IOException, InterruptedException {
        return http.post("/rest/api/3/issueLink", jsonBody);
    }

    // ── Worklogs ─────────────────────────────────────────────────────────────

    public String addWorklog(final String key, final String jsonBody) throws IOException, InterruptedException {
        return http.post("/rest/api/3/issue/" + encode(key) + "/worklog", jsonBody);
    }

    // ── Projects ─────────────────────────────────────────────────────────────

    public String listProjects() throws IOException, InterruptedException {
        return http.get("/rest/api/3/project?expand=lead");
    }

    public String getProject(final String keyOrId) throws IOException, InterruptedException {
        return http.get("/rest/api/3/project/" + encode(keyOrId));
    }

    public String getProjectComponents(final String key) throws IOException, InterruptedException {
        return http.get("/rest/api/3/project/" + encode(key) + "/components");
    }

    public String createComponent(final String jsonBody) throws IOException, InterruptedException {
        return http.post("/rest/api/3/component", jsonBody);
    }

    public String getProjectVersions(final String key) throws IOException, InterruptedException {
        return http.get("/rest/api/3/project/" + encode(key) + "/versions");
    }

    public String createVersion(final String jsonBody) throws IOException, InterruptedException {
        return http.post("/rest/api/3/version", jsonBody);
    }

    // ── Boards & Sprints ─────────────────────────────────────────────────────

    public String listBoards(final int maxResults) throws IOException, InterruptedException {
        return http.get("/rest/agile/1.0/board?maxResults=" + maxResults);
    }

    public String getActiveSprint(final int boardId) throws IOException, InterruptedException {
        return http.get("/rest/agile/1.0/board/" + boardId + "/sprint?state=active");
    }

    public String getSprintIssues(final int boardId, final int maxResults) throws IOException, InterruptedException {
        return http.get("/rest/agile/1.0/board/" + boardId + "/sprint?state=active&maxResults=" + maxResults);
    }

    // ── Users ────────────────────────────────────────────────────────────────

    public String searchUsers(final String query, final int maxResults) throws IOException, InterruptedException {
        return http.get("/rest/api/3/user/search?query=" + encode(query) + "&maxResults=" + maxResults);
    }

    public String getCurrentUser() throws IOException, InterruptedException {
        return http.get("/rest/api/3/myself");
    }

    // ── Metadata ─────────────────────────────────────────────────────────────

    public String getIssueTypes(final String projectKey) throws IOException, InterruptedException {
        return http.get("/rest/api/3/issue/createmeta/" + encode(projectKey) + "/issuetypes");
    }

    public String getPriorities() throws IOException, InterruptedException {
        return http.get("/rest/api/3/priority");
    }

    public String getStatuses() throws IOException, InterruptedException {
        return http.get("/rest/api/3/status");
    }

    // ── Dashboards & Filters ─────────────────────────────────────────────────

    public String listDashboards(final int maxResults) throws IOException, InterruptedException {
        return http.get("/rest/api/3/dashboard?maxResults=" + maxResults);
    }

    public String getFilters() throws IOException, InterruptedException {
        return http.get("/rest/api/3/filter/favourite");
    }

    // ── Bulk Operations ──────────────────────────────────────────────────────

    public String bulkCreateIssues(final String jsonBody) throws IOException, InterruptedException {
        return http.post("/rest/api/3/issue/bulk", jsonBody);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private static String encode(final String value) {
        return java.net.URLEncoder.encode(value, java.nio.charset.StandardCharsets.UTF_8);
    }
}
