package server.atlassian.v2.handler;

import server.atlassian.v2.client.BitbucketClientV2;
import server.atlassian.v2.model.Product;
import server.atlassian.v2.model.ToolResult;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static server.atlassian.v2.handler.HandlerUtils.*;

/**
 * Handles all Bitbucket MCP tool calls (30 tools).
 */
public final class BitbucketToolHandler {

    private static final Logger LOGGER = Logger.getLogger(BitbucketToolHandler.class.getName());
    private static final Product P = Product.BITBUCKET;

    private final BitbucketClientV2 client;

    public BitbucketToolHandler(final BitbucketClientV2 client) {
        this.client = Objects.requireNonNull(client);
    }

    public ToolResult handle(final String tool, final Map<String, String> args) {
        return switch (tool) {
            case "bitbucket_list_repos"            -> listRepos(args);
            case "bitbucket_get_repo"              -> getRepo(args);
            case "bitbucket_list_pull_requests"    -> listPullRequests(args);
            case "bitbucket_get_pull_request"      -> getPullRequest(args);
            case "bitbucket_create_pull_request"   -> createPullRequest(args);
            case "bitbucket_get_pull_request_diff" -> getPullRequestDiff(args);
            case "bitbucket_get_pr_comments"       -> getPrComments(args);
            case "bitbucket_add_pr_comment"        -> addPrComment(args);
            case "bitbucket_approve_pull_request"  -> approvePullRequest(args);
            case "bitbucket_decline_pull_request"  -> declinePullRequest(args);
            case "bitbucket_merge_pull_request"    -> mergePullRequest(args);
            case "bitbucket_search_code"           -> searchCode(args);
            case "bitbucket_list_branches"         -> listBranches(args);
            case "bitbucket_create_branch"         -> createBranch(args);
            case "bitbucket_delete_branch"         -> deleteBranch(args);
            case "bitbucket_list_tags"             -> listTags(args);
            case "bitbucket_create_tag"            -> createTag(args);
            case "bitbucket_get_commits"           -> getCommits(args);
            case "bitbucket_get_commit"            -> getCommit(args);
            case "bitbucket_get_diff"              -> getDiff(args);
            case "bitbucket_get_file_content"      -> getFileContent(args);
            case "bitbucket_list_projects"         -> listProjects(args);
            case "bitbucket_list_pipelines"        -> listPipelines(args);
            case "bitbucket_get_pipeline"          -> getPipeline(args);
            case "bitbucket_trigger_pipeline"      -> triggerPipeline(args);
            case "bitbucket_stop_pipeline"         -> stopPipeline(args);
            case "bitbucket_list_environments"     -> listEnvironments(args);
            case "bitbucket_list_deployments"      -> listDeployments(args);
            case "bitbucket_list_snippets"         -> listSnippets(args);
            case "bitbucket_create_snippet"        -> createSnippet(args);
            case "bitbucket_whoami"                -> whoami();
            default -> ToolResult.error(P, tool, "Unknown Bitbucket tool: " + tool);
        };
    }

    // ── Repositories ─────────────────────────────────────────────────────────

    private ToolResult listRepos(final Map<String, String> args) {
        final var ws = requireArg(args, "workspace");
        if (ws == null) return ToolResult.error(P, "bitbucket_list_repos", "Missing required: 'workspace'");
        try { return ToolResult.success(P, "bitbucket_list_repos", client.listRepos(ws)); }
        catch (IOException | InterruptedException ex) { return apiError("bitbucket_list_repos", ex); }
    }

    private ToolResult getRepo(final Map<String, String> args) {
        final var ws = requireArg(args, "workspace");
        final var slug = requireArg(args, "repoSlug");
        if (ws == null || slug == null) return ToolResult.error(P, "bitbucket_get_repo", "Missing required: 'workspace' and 'repoSlug'");
        try { return ToolResult.success(P, "bitbucket_get_repo", client.getRepo(ws, slug)); }
        catch (IOException | InterruptedException ex) { return apiError("bitbucket_get_repo", ex); }
    }

    // ── Pull Requests ────────────────────────────────────────────────────────

    private ToolResult listPullRequests(final Map<String, String> args) {
        final var ws = requireArg(args, "workspace");
        final var slug = requireArg(args, "repoSlug");
        if (ws == null || slug == null) return ToolResult.error(P, "bitbucket_list_pull_requests", "Missing required: 'workspace' and 'repoSlug'");
        try { return ToolResult.success(P, "bitbucket_list_pull_requests", client.listPullRequests(ws, slug, args.get("state"))); }
        catch (IOException | InterruptedException ex) { return apiError("bitbucket_list_pull_requests", ex); }
    }

    private ToolResult getPullRequest(final Map<String, String> args) {
        final var ws = requireArg(args, "workspace");
        final var slug = requireArg(args, "repoSlug");
        final int prId = parseInt(requireArg(args, "pullRequestId"), -1);
        if (ws == null || slug == null || prId < 0) return ToolResult.error(P, "bitbucket_get_pull_request", "Missing required: 'workspace', 'repoSlug', 'pullRequestId'");
        try { return ToolResult.success(P, "bitbucket_get_pull_request", client.getPullRequest(ws, slug, prId)); }
        catch (IOException | InterruptedException ex) { return apiError("bitbucket_get_pull_request", ex); }
    }

    private ToolResult createPullRequest(final Map<String, String> args) {
        final var ws = requireArg(args, "workspace");
        final var slug = requireArg(args, "repoSlug");
        final var title = requireArg(args, "title");
        final var source = requireArg(args, "sourceBranch");
        final var target = args.getOrDefault("targetBranch", "main");
        if (ws == null || slug == null || title == null || source == null) return ToolResult.error(P, "bitbucket_create_pull_request", "Missing required: 'workspace', 'repoSlug', 'title', 'sourceBranch'");
        final var desc = args.getOrDefault("description", "");
        final var body = "{\"title\":\"" + escapeJson(title) + "\",\"source\":{\"branch\":{\"name\":\"" + escapeJson(source) + "\"}},\"destination\":{\"branch\":{\"name\":\"" + escapeJson(target) + "\"}}}" + (desc.isBlank() ? "" : "");
        try { return ToolResult.success(P, "bitbucket_create_pull_request", client.createPullRequest(ws, slug, body)); }
        catch (IOException | InterruptedException ex) { return apiError("bitbucket_create_pull_request", ex); }
    }

    private ToolResult getPullRequestDiff(final Map<String, String> args) {
        final var ws = requireArg(args, "workspace");
        final var slug = requireArg(args, "repoSlug");
        final int prId = parseInt(requireArg(args, "pullRequestId"), -1);
        if (ws == null || slug == null || prId < 0) return ToolResult.error(P, "bitbucket_get_pull_request_diff", "Missing required fields");
        try { return ToolResult.success(P, "bitbucket_get_pull_request_diff", client.getPullRequestDiff(ws, slug, prId)); }
        catch (IOException | InterruptedException ex) { return apiError("bitbucket_get_pull_request_diff", ex); }
    }

    private ToolResult getPrComments(final Map<String, String> args) {
        final var ws = requireArg(args, "workspace");
        final var slug = requireArg(args, "repoSlug");
        final int prId = parseInt(requireArg(args, "pullRequestId"), -1);
        if (ws == null || slug == null || prId < 0) return ToolResult.error(P, "bitbucket_get_pr_comments", "Missing required fields");
        try { return ToolResult.success(P, "bitbucket_get_pr_comments", client.getPrComments(ws, slug, prId)); }
        catch (IOException | InterruptedException ex) { return apiError("bitbucket_get_pr_comments", ex); }
    }

    private ToolResult addPrComment(final Map<String, String> args) {
        final var ws = requireArg(args, "workspace");
        final var slug = requireArg(args, "repoSlug");
        final int prId = parseInt(requireArg(args, "pullRequestId"), -1);
        final var content = requireArg(args, "content");
        if (ws == null || slug == null || prId < 0 || content == null) return ToolResult.error(P, "bitbucket_add_pr_comment", "Missing required fields");
        try { return ToolResult.success(P, "bitbucket_add_pr_comment", client.addPrComment(ws, slug, prId, "{\"content\":{\"raw\":\"" + escapeJson(content) + "\"}}")); }
        catch (IOException | InterruptedException ex) { return apiError("bitbucket_add_pr_comment", ex); }
    }

    private ToolResult approvePullRequest(final Map<String, String> args) {
        final var ws = requireArg(args, "workspace");
        final var slug = requireArg(args, "repoSlug");
        final int prId = parseInt(requireArg(args, "pullRequestId"), -1);
        if (ws == null || slug == null || prId < 0) return ToolResult.error(P, "bitbucket_approve_pull_request", "Missing required fields");
        try { return ToolResult.success(P, "bitbucket_approve_pull_request", client.approvePullRequest(ws, slug, prId)); }
        catch (IOException | InterruptedException ex) { return apiError("bitbucket_approve_pull_request", ex); }
    }

    private ToolResult declinePullRequest(final Map<String, String> args) {
        final var ws = requireArg(args, "workspace");
        final var slug = requireArg(args, "repoSlug");
        final int prId = parseInt(requireArg(args, "pullRequestId"), -1);
        if (ws == null || slug == null || prId < 0) return ToolResult.error(P, "bitbucket_decline_pull_request", "Missing required fields");
        try { return ToolResult.success(P, "bitbucket_decline_pull_request", client.declinePullRequest(ws, slug, prId)); }
        catch (IOException | InterruptedException ex) { return apiError("bitbucket_decline_pull_request", ex); }
    }

    private ToolResult mergePullRequest(final Map<String, String> args) {
        final var ws = requireArg(args, "workspace");
        final var slug = requireArg(args, "repoSlug");
        final int prId = parseInt(requireArg(args, "pullRequestId"), -1);
        if (ws == null || slug == null || prId < 0) return ToolResult.error(P, "bitbucket_merge_pull_request", "Missing required fields");
        try { return ToolResult.success(P, "bitbucket_merge_pull_request", client.mergePullRequest(ws, slug, prId)); }
        catch (IOException | InterruptedException ex) { return apiError("bitbucket_merge_pull_request", ex); }
    }

    // ── Code Search ──────────────────────────────────────────────────────────

    private ToolResult searchCode(final Map<String, String> args) {
        final var ws = requireArg(args, "workspace");
        final var query = requireArg(args, "query");
        if (ws == null || query == null) return ToolResult.error(P, "bitbucket_search_code", "Missing required: 'workspace' and 'query'");
        try { return ToolResult.success(P, "bitbucket_search_code", client.searchCode(ws, query)); }
        catch (IOException | InterruptedException ex) { return apiError("bitbucket_search_code", ex); }
    }

    // ── Branches ─────────────────────────────────────────────────────────────

    private ToolResult listBranches(final Map<String, String> args) {
        final var ws = requireArg(args, "workspace");
        final var slug = requireArg(args, "repoSlug");
        if (ws == null || slug == null) return ToolResult.error(P, "bitbucket_list_branches", "Missing required: 'workspace' and 'repoSlug'");
        try { return ToolResult.success(P, "bitbucket_list_branches", client.listBranches(ws, slug, parseMaxResults(args.get("maxResults")))); }
        catch (IOException | InterruptedException ex) { return apiError("bitbucket_list_branches", ex); }
    }

    private ToolResult createBranch(final Map<String, String> args) {
        final var ws = requireArg(args, "workspace");
        final var slug = requireArg(args, "repoSlug");
        final var name = requireArg(args, "name");
        final var target = args.getOrDefault("target", "main");
        if (ws == null || slug == null || name == null) return ToolResult.error(P, "bitbucket_create_branch", "Missing required: 'workspace', 'repoSlug', 'name'");
        final var body = "{\"name\":\"" + escapeJson(name) + "\",\"target\":{\"hash\":\"" + escapeJson(target) + "\"}}";
        try { return ToolResult.success(P, "bitbucket_create_branch", client.createBranch(ws, slug, body)); }
        catch (IOException | InterruptedException ex) { return apiError("bitbucket_create_branch", ex); }
    }

    private ToolResult deleteBranch(final Map<String, String> args) {
        final var ws = requireArg(args, "workspace");
        final var slug = requireArg(args, "repoSlug");
        final var name = requireArg(args, "name");
        if (ws == null || slug == null || name == null) return ToolResult.error(P, "bitbucket_delete_branch", "Missing required fields");
        try { return ToolResult.success(P, "bitbucket_delete_branch", client.deleteBranch(ws, slug, name)); }
        catch (IOException | InterruptedException ex) { return apiError("bitbucket_delete_branch", ex); }
    }

    // ── Tags ─────────────────────────────────────────────────────────────────

    private ToolResult listTags(final Map<String, String> args) {
        final var ws = requireArg(args, "workspace");
        final var slug = requireArg(args, "repoSlug");
        if (ws == null || slug == null) return ToolResult.error(P, "bitbucket_list_tags", "Missing required fields");
        try { return ToolResult.success(P, "bitbucket_list_tags", client.listTags(ws, slug)); }
        catch (IOException | InterruptedException ex) { return apiError("bitbucket_list_tags", ex); }
    }

    private ToolResult createTag(final Map<String, String> args) {
        final var ws = requireArg(args, "workspace");
        final var slug = requireArg(args, "repoSlug");
        final var name = requireArg(args, "name");
        final var target = requireArg(args, "target");
        if (ws == null || slug == null || name == null || target == null) return ToolResult.error(P, "bitbucket_create_tag", "Missing required fields");
        final var body = "{\"name\":\"" + escapeJson(name) + "\",\"target\":{\"hash\":\"" + escapeJson(target) + "\"}}";
        try { return ToolResult.success(P, "bitbucket_create_tag", client.createTag(ws, slug, body)); }
        catch (IOException | InterruptedException ex) { return apiError("bitbucket_create_tag", ex); }
    }

    // ── Commits ──────────────────────────────────────────────────────────────

    private ToolResult getCommits(final Map<String, String> args) {
        final var ws = requireArg(args, "workspace");
        final var slug = requireArg(args, "repoSlug");
        if (ws == null || slug == null) return ToolResult.error(P, "bitbucket_get_commits", "Missing required fields");
        try { return ToolResult.success(P, "bitbucket_get_commits", client.getCommits(ws, slug, args.get("branch"), parseMaxResults(args.get("maxResults")))); }
        catch (IOException | InterruptedException ex) { return apiError("bitbucket_get_commits", ex); }
    }

    private ToolResult getCommit(final Map<String, String> args) {
        final var ws = requireArg(args, "workspace");
        final var slug = requireArg(args, "repoSlug");
        final var sha = requireArg(args, "sha");
        if (ws == null || slug == null || sha == null) return ToolResult.error(P, "bitbucket_get_commit", "Missing required fields");
        try { return ToolResult.success(P, "bitbucket_get_commit", client.getCommit(ws, slug, sha)); }
        catch (IOException | InterruptedException ex) { return apiError("bitbucket_get_commit", ex); }
    }

    private ToolResult getDiff(final Map<String, String> args) {
        final var ws = requireArg(args, "workspace");
        final var slug = requireArg(args, "repoSlug");
        final var spec = requireArg(args, "spec");
        if (ws == null || slug == null || spec == null) return ToolResult.error(P, "bitbucket_get_diff", "Missing required: 'workspace', 'repoSlug', 'spec'");
        try { return ToolResult.success(P, "bitbucket_get_diff", client.getDiff(ws, slug, spec)); }
        catch (IOException | InterruptedException ex) { return apiError("bitbucket_get_diff", ex); }
    }

    // ── File Content ─────────────────────────────────────────────────────────

    private ToolResult getFileContent(final Map<String, String> args) {
        final var ws = requireArg(args, "workspace");
        final var slug = requireArg(args, "repoSlug");
        final var path = requireArg(args, "filePath");
        if (ws == null || slug == null || path == null) return ToolResult.error(P, "bitbucket_get_file_content", "Missing required fields");
        try { return ToolResult.success(P, "bitbucket_get_file_content", client.getFileContent(ws, slug, path, args.get("branch"))); }
        catch (IOException | InterruptedException ex) { return apiError("bitbucket_get_file_content", ex); }
    }

    // ── Projects ─────────────────────────────────────────────────────────────

    private ToolResult listProjects(final Map<String, String> args) {
        final var ws = requireArg(args, "workspace");
        if (ws == null) return ToolResult.error(P, "bitbucket_list_projects", "Missing required: 'workspace'");
        try { return ToolResult.success(P, "bitbucket_list_projects", client.listProjects(ws)); }
        catch (IOException | InterruptedException ex) { return apiError("bitbucket_list_projects", ex); }
    }

    // ── Pipelines ────────────────────────────────────────────────────────────

    private ToolResult listPipelines(final Map<String, String> args) {
        final var ws = requireArg(args, "workspace");
        final var slug = requireArg(args, "repoSlug");
        if (ws == null || slug == null) return ToolResult.error(P, "bitbucket_list_pipelines", "Missing required fields");
        try { return ToolResult.success(P, "bitbucket_list_pipelines", client.listPipelines(ws, slug)); }
        catch (IOException | InterruptedException ex) { return apiError("bitbucket_list_pipelines", ex); }
    }

    private ToolResult getPipeline(final Map<String, String> args) {
        final var ws = requireArg(args, "workspace");
        final var slug = requireArg(args, "repoSlug");
        final var uuid = requireArg(args, "pipelineUuid");
        if (ws == null || slug == null || uuid == null) return ToolResult.error(P, "bitbucket_get_pipeline", "Missing required fields");
        try { return ToolResult.success(P, "bitbucket_get_pipeline", client.getPipeline(ws, slug, uuid)); }
        catch (IOException | InterruptedException ex) { return apiError("bitbucket_get_pipeline", ex); }
    }

    private ToolResult triggerPipeline(final Map<String, String> args) {
        final var ws = requireArg(args, "workspace");
        final var slug = requireArg(args, "repoSlug");
        final var branch = args.getOrDefault("branch", "main");
        if (ws == null || slug == null) return ToolResult.error(P, "bitbucket_trigger_pipeline", "Missing required fields");
        final var body = "{\"target\":{\"ref_type\":\"branch\",\"type\":\"pipeline_ref_target\",\"ref_name\":\"" + escapeJson(branch) + "\"}}";
        try { return ToolResult.success(P, "bitbucket_trigger_pipeline", client.triggerPipeline(ws, slug, body)); }
        catch (IOException | InterruptedException ex) { return apiError("bitbucket_trigger_pipeline", ex); }
    }

    private ToolResult stopPipeline(final Map<String, String> args) {
        final var ws = requireArg(args, "workspace");
        final var slug = requireArg(args, "repoSlug");
        final var uuid = requireArg(args, "pipelineUuid");
        if (ws == null || slug == null || uuid == null) return ToolResult.error(P, "bitbucket_stop_pipeline", "Missing required fields");
        try { return ToolResult.success(P, "bitbucket_stop_pipeline", client.stopPipeline(ws, slug, uuid)); }
        catch (IOException | InterruptedException ex) { return apiError("bitbucket_stop_pipeline", ex); }
    }

    private ToolResult listEnvironments(final Map<String, String> args) {
        final var ws = requireArg(args, "workspace");
        final var slug = requireArg(args, "repoSlug");
        if (ws == null || slug == null) return ToolResult.error(P, "bitbucket_list_environments", "Missing required fields");
        try { return ToolResult.success(P, "bitbucket_list_environments", client.listEnvironments(ws, slug)); }
        catch (IOException | InterruptedException ex) { return apiError("bitbucket_list_environments", ex); }
    }

    private ToolResult listDeployments(final Map<String, String> args) {
        final var ws = requireArg(args, "workspace");
        final var slug = requireArg(args, "repoSlug");
        if (ws == null || slug == null) return ToolResult.error(P, "bitbucket_list_deployments", "Missing required fields");
        try { return ToolResult.success(P, "bitbucket_list_deployments", client.listDeployments(ws, slug)); }
        catch (IOException | InterruptedException ex) { return apiError("bitbucket_list_deployments", ex); }
    }

    // ── Snippets ─────────────────────────────────────────────────────────────

    private ToolResult listSnippets(final Map<String, String> args) {
        final var ws = requireArg(args, "workspace");
        if (ws == null) return ToolResult.error(P, "bitbucket_list_snippets", "Missing required: 'workspace'");
        try { return ToolResult.success(P, "bitbucket_list_snippets", client.listSnippets(ws)); }
        catch (IOException | InterruptedException ex) { return apiError("bitbucket_list_snippets", ex); }
    }

    private ToolResult createSnippet(final Map<String, String> args) {
        final var ws = requireArg(args, "workspace");
        final var title = requireArg(args, "title");
        final var content = requireArg(args, "content");
        if (ws == null || title == null || content == null) return ToolResult.error(P, "bitbucket_create_snippet", "Missing required fields");
        final var body = "{\"title\":\"" + escapeJson(title) + "\",\"is_private\":true,\"files\":{\"snippet.txt\":{\"content\":\"" + escapeJson(content) + "\"}}}";
        try { return ToolResult.success(P, "bitbucket_create_snippet", client.createSnippet(ws, body)); }
        catch (IOException | InterruptedException ex) { return apiError("bitbucket_create_snippet", ex); }
    }

    private ToolResult whoami() {
        try { return ToolResult.success(P, "bitbucket_whoami", client.getCurrentUser()); }
        catch (IOException | InterruptedException ex) { return apiError("bitbucket_whoami", ex); }
    }

    private ToolResult apiError(final String tool, final Exception ex) {
        LOGGER.log(Level.WARNING, "Bitbucket API error in " + tool, ex);
        return ToolResult.error(P, tool, "Bitbucket API error: " + ex.getMessage());
    }
}
