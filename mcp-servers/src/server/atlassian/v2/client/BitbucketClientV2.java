package server.atlassian.v2.client;

import server.atlassian.v2.auth.AuthManager;

import java.io.IOException;

/**
 * Bitbucket REST API 2.0 client — comprehensive coverage including pipelines.
 */
public final class BitbucketClientV2 {

    private final HttpClientV2 http;

    public BitbucketClientV2(final String baseUrl, final AuthManager auth, final int timeoutMs) {
        this.http = new HttpClientV2(baseUrl, auth, timeoutMs);
    }

    // ── Repositories ─────────────────────────────────────────────────────────

    public String listRepos(final String workspace) throws IOException, InterruptedException {
        return http.get("/2.0/repositories/" + encode(workspace) + "?pagelen=50");
    }

    public String getRepo(final String workspace, final String slug) throws IOException, InterruptedException {
        return http.get("/2.0/repositories/" + encode(workspace) + "/" + encode(slug));
    }

    // ── Pull Requests ────────────────────────────────────────────────────────

    public String listPullRequests(final String workspace, final String slug, final String state) throws IOException, InterruptedException {
        final var stateParam = (state != null && !state.isBlank()) ? "&state=" + encode(state) : "";
        return http.get("/2.0/repositories/" + encode(workspace) + "/" + encode(slug) + "/pullrequests?pagelen=25" + stateParam);
    }

    public String getPullRequest(final String workspace, final String slug, final int prId) throws IOException, InterruptedException {
        return http.get("/2.0/repositories/" + encode(workspace) + "/" + encode(slug) + "/pullrequests/" + prId);
    }

    public String createPullRequest(final String workspace, final String slug, final String jsonBody) throws IOException, InterruptedException {
        return http.post("/2.0/repositories/" + encode(workspace) + "/" + encode(slug) + "/pullrequests", jsonBody);
    }

    public String getPullRequestDiff(final String workspace, final String slug, final int prId) throws IOException, InterruptedException {
        return http.get("/2.0/repositories/" + encode(workspace) + "/" + encode(slug) + "/pullrequests/" + prId + "/diff");
    }

    public String getPrComments(final String workspace, final String slug, final int prId) throws IOException, InterruptedException {
        return http.get("/2.0/repositories/" + encode(workspace) + "/" + encode(slug) + "/pullrequests/" + prId + "/comments");
    }

    public String addPrComment(final String workspace, final String slug, final int prId, final String jsonBody) throws IOException, InterruptedException {
        return http.post("/2.0/repositories/" + encode(workspace) + "/" + encode(slug) + "/pullrequests/" + prId + "/comments", jsonBody);
    }

    public String approvePullRequest(final String workspace, final String slug, final int prId) throws IOException, InterruptedException {
        return http.post("/2.0/repositories/" + encode(workspace) + "/" + encode(slug) + "/pullrequests/" + prId + "/approve", "{}");
    }

    public String declinePullRequest(final String workspace, final String slug, final int prId) throws IOException, InterruptedException {
        return http.post("/2.0/repositories/" + encode(workspace) + "/" + encode(slug) + "/pullrequests/" + prId + "/decline", "{}");
    }

    public String mergePullRequest(final String workspace, final String slug, final int prId) throws IOException, InterruptedException {
        return http.post("/2.0/repositories/" + encode(workspace) + "/" + encode(slug) + "/pullrequests/" + prId + "/merge", "{}");
    }

    // ── Branches ─────────────────────────────────────────────────────────────

    public String listBranches(final String workspace, final String slug, final int maxResults) throws IOException, InterruptedException {
        return http.get("/2.0/repositories/" + encode(workspace) + "/" + encode(slug) + "/refs/branches?pagelen=" + maxResults);
    }

    public String createBranch(final String workspace, final String slug, final String jsonBody) throws IOException, InterruptedException {
        return http.post("/2.0/repositories/" + encode(workspace) + "/" + encode(slug) + "/refs/branches", jsonBody);
    }

    public String deleteBranch(final String workspace, final String slug, final String branchName) throws IOException, InterruptedException {
        return http.delete("/2.0/repositories/" + encode(workspace) + "/" + encode(slug) + "/refs/branches/" + encode(branchName));
    }

    // ── Tags ─────────────────────────────────────────────────────────────────

    public String listTags(final String workspace, final String slug) throws IOException, InterruptedException {
        return http.get("/2.0/repositories/" + encode(workspace) + "/" + encode(slug) + "/refs/tags?pagelen=50");
    }

    public String createTag(final String workspace, final String slug, final String jsonBody) throws IOException, InterruptedException {
        return http.post("/2.0/repositories/" + encode(workspace) + "/" + encode(slug) + "/refs/tags", jsonBody);
    }

    // ── Commits ──────────────────────────────────────────────────────────────

    public String getCommits(final String workspace, final String slug, final String branch, final int maxResults) throws IOException, InterruptedException {
        final var branchParam = (branch != null && !branch.isBlank()) ? "/" + encode(branch) : "";
        return http.get("/2.0/repositories/" + encode(workspace) + "/" + encode(slug) + "/commits" + branchParam + "?pagelen=" + maxResults);
    }

    public String getCommit(final String workspace, final String slug, final String sha) throws IOException, InterruptedException {
        return http.get("/2.0/repositories/" + encode(workspace) + "/" + encode(slug) + "/commit/" + encode(sha));
    }

    public String getDiff(final String workspace, final String slug, final String spec) throws IOException, InterruptedException {
        return http.get("/2.0/repositories/" + encode(workspace) + "/" + encode(slug) + "/diff/" + encode(spec));
    }

    // ── Code Search ──────────────────────────────────────────────────────────

    public String searchCode(final String workspace, final String query) throws IOException, InterruptedException {
        return http.get("/2.0/workspaces/" + encode(workspace) + "/search/code?search_query=" + encode(query));
    }

    // ── File Content ─────────────────────────────────────────────────────────

    public String getFileContent(final String workspace, final String slug, final String path, final String branch) throws IOException, InterruptedException {
        final var ref = (branch != null && !branch.isBlank()) ? branch : "main";
        return http.get("/2.0/repositories/" + encode(workspace) + "/" + encode(slug) + "/src/" + encode(ref) + "/" + path);
    }

    // ── Projects ─────────────────────────────────────────────────────────────

    public String listProjects(final String workspace) throws IOException, InterruptedException {
        return http.get("/2.0/workspaces/" + encode(workspace) + "/projects?pagelen=50");
    }

    // ── Pipelines ────────────────────────────────────────────────────────────

    public String listPipelines(final String workspace, final String slug) throws IOException, InterruptedException {
        return http.get("/2.0/repositories/" + encode(workspace) + "/" + encode(slug) + "/pipelines/?sort=-created_on&pagelen=20");
    }

    public String getPipeline(final String workspace, final String slug, final String pipelineUuid) throws IOException, InterruptedException {
        return http.get("/2.0/repositories/" + encode(workspace) + "/" + encode(slug) + "/pipelines/" + encode(pipelineUuid));
    }

    public String triggerPipeline(final String workspace, final String slug, final String jsonBody) throws IOException, InterruptedException {
        return http.post("/2.0/repositories/" + encode(workspace) + "/" + encode(slug) + "/pipelines/", jsonBody);
    }

    public String stopPipeline(final String workspace, final String slug, final String pipelineUuid) throws IOException, InterruptedException {
        return http.post("/2.0/repositories/" + encode(workspace) + "/" + encode(slug) + "/pipelines/" + encode(pipelineUuid) + "/stopPipeline", "{}");
    }

    // ── Deployments & Environments ───────────────────────────────────────────

    public String listEnvironments(final String workspace, final String slug) throws IOException, InterruptedException {
        return http.get("/2.0/repositories/" + encode(workspace) + "/" + encode(slug) + "/environments/");
    }

    public String listDeployments(final String workspace, final String slug) throws IOException, InterruptedException {
        return http.get("/2.0/repositories/" + encode(workspace) + "/" + encode(slug) + "/deployments/?sort=-created_on&pagelen=20");
    }

    // ── Snippets ─────────────────────────────────────────────────────────────

    public String listSnippets(final String workspace) throws IOException, InterruptedException {
        return http.get("/2.0/snippets/" + encode(workspace) + "?pagelen=50");
    }

    public String createSnippet(final String workspace, final String jsonBody) throws IOException, InterruptedException {
        return http.post("/2.0/snippets/" + encode(workspace), jsonBody);
    }

    // ── User ─────────────────────────────────────────────────────────────────

    public String getCurrentUser() throws IOException, InterruptedException {
        return http.get("/2.0/user");
    }

    private static String encode(final String value) {
        return java.net.URLEncoder.encode(value, java.nio.charset.StandardCharsets.UTF_8);
    }
}
