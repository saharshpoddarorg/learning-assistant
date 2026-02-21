package server.atlassian.handler;

import server.atlassian.client.BitbucketClient;
import server.atlassian.model.AtlassianProduct;
import server.atlassian.model.ToolResponse;
import server.atlassian.util.JsonExtractor;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles Bitbucket-related MCP tool calls.
 *
 * <p>Routes Bitbucket tool invocations to the appropriate
 * {@link BitbucketClient} method and formats the results.
 *
 * <p><strong>Registered Tools:</strong>
 * <ul>
 *   <li>{@code bitbucket_list_repos} — list repositories in a workspace</li>
 *   <li>{@code bitbucket_get_repo} — get repository details</li>
 *   <li>{@code bitbucket_list_pull_requests} — list PRs for a repository</li>
 *   <li>{@code bitbucket_get_pull_request} — get full PR details</li>
 *   <li>{@code bitbucket_search_code} — search code across repositories</li>
 * </ul>
 */
public class BitbucketHandler {

    private static final Logger LOGGER = Logger.getLogger(BitbucketHandler.class.getName());

    private final BitbucketClient bitbucketClient;

    /**
     * Creates a Bitbucket handler with the given client.
     *
     * @param bitbucketClient the Bitbucket REST API client
     */
    public BitbucketHandler(final BitbucketClient bitbucketClient) {
        this.bitbucketClient = Objects.requireNonNull(
                bitbucketClient, "BitbucketClient must not be null");
    }

    /**
     * Lists repositories in a Bitbucket workspace.
     *
     * @param arguments the tool arguments ({@code workspace}, optional {@code maxResults})
     * @return the tool response with repository list
     */
    public ToolResponse listRepositories(final Map<String, String> arguments) {
        final var workspace = arguments.get("workspace");
        if (workspace == null || workspace.isBlank()) {
            return ToolResponse.error(AtlassianProduct.BITBUCKET, "bitbucket_list_repos",
                    "Missing required argument: 'workspace'");
        }

        final int maxResults = parseMaxResults(arguments.get("maxResults"));

        try {
            final var response = bitbucketClient.listRepositories(workspace, maxResults);
            return ToolResponse.success(AtlassianProduct.BITBUCKET,
                    "bitbucket_list_repos", response);
        } catch (IOException | InterruptedException exception) {
            LOGGER.log(Level.WARNING, "Failed to list repos in " + workspace, exception);
            return ToolResponse.error(AtlassianProduct.BITBUCKET, "bitbucket_list_repos",
                    "Failed to list repositories: " + exception.getMessage());
        }
    }

    /**
     * Gets details of a specific repository.
     *
     * @param arguments the tool arguments ({@code workspace}, {@code repoSlug})
     * @return the tool response with repository details
     */
    public ToolResponse getRepository(final Map<String, String> arguments) {
        final var workspace = arguments.get("workspace");
        final var repoSlug = arguments.get("repoSlug");

        if (workspace == null || workspace.isBlank()) {
            return ToolResponse.error(AtlassianProduct.BITBUCKET, "bitbucket_get_repo",
                    "Missing required argument: 'workspace'");
        }
        if (repoSlug == null || repoSlug.isBlank()) {
            return ToolResponse.error(AtlassianProduct.BITBUCKET, "bitbucket_get_repo",
                    "Missing required argument: 'repoSlug'");
        }

        try {
            final var response = bitbucketClient.getRepository(workspace, repoSlug);
            return ToolResponse.success(AtlassianProduct.BITBUCKET,
                    "bitbucket_get_repo", response);
        } catch (IOException | InterruptedException exception) {
            LOGGER.log(Level.WARNING,
                    "Failed to get repo " + workspace + "/" + repoSlug, exception);
            return ToolResponse.error(AtlassianProduct.BITBUCKET, "bitbucket_get_repo",
                    "Failed to get repository: " + exception.getMessage());
        }
    }

    /**
     * Lists pull requests for a repository.
     *
     * @param arguments the tool arguments ({@code workspace}, {@code repoSlug}, optional {@code state}, {@code maxResults})
     * @return the tool response with PR list
     */
    public ToolResponse listPullRequests(final Map<String, String> arguments) {
        final var workspace = arguments.get("workspace");
        final var repoSlug = arguments.get("repoSlug");

        if (workspace == null || workspace.isBlank()) {
            return ToolResponse.error(AtlassianProduct.BITBUCKET,
                    "bitbucket_list_pull_requests",
                    "Missing required argument: 'workspace'");
        }
        if (repoSlug == null || repoSlug.isBlank()) {
            return ToolResponse.error(AtlassianProduct.BITBUCKET,
                    "bitbucket_list_pull_requests",
                    "Missing required argument: 'repoSlug'");
        }

        final var state = arguments.getOrDefault("state", "OPEN");
        final int maxResults = parseMaxResults(arguments.get("maxResults"));

        try {
            final var response = bitbucketClient.listPullRequests(
                    workspace, repoSlug, state, maxResults);
            return ToolResponse.success(AtlassianProduct.BITBUCKET,
                    "bitbucket_list_pull_requests", response);
        } catch (IOException | InterruptedException exception) {
            LOGGER.log(Level.WARNING,
                    "Failed to list PRs for " + workspace + "/" + repoSlug, exception);
            return ToolResponse.error(AtlassianProduct.BITBUCKET,
                    "bitbucket_list_pull_requests",
                    "Failed to list pull requests: " + exception.getMessage());
        }
    }

    /**
     * Gets full details of a specific pull request.
     *
     * @param arguments the tool arguments ({@code workspace}, {@code repoSlug}, {@code prId})
     * @return the tool response with PR details
     */
    public ToolResponse getPullRequest(final Map<String, String> arguments) {
        final var workspace = arguments.get("workspace");
        final var repoSlug = arguments.get("repoSlug");
        final var prIdStr = arguments.get("prId");

        if (workspace == null || workspace.isBlank()) {
            return ToolResponse.error(AtlassianProduct.BITBUCKET,
                    "bitbucket_get_pull_request",
                    "Missing required argument: 'workspace'");
        }
        if (repoSlug == null || repoSlug.isBlank()) {
            return ToolResponse.error(AtlassianProduct.BITBUCKET,
                    "bitbucket_get_pull_request",
                    "Missing required argument: 'repoSlug'");
        }
        if (prIdStr == null || prIdStr.isBlank()) {
            return ToolResponse.error(AtlassianProduct.BITBUCKET,
                    "bitbucket_get_pull_request",
                    "Missing required argument: 'prId'");
        }

        try {
            final int prId = Integer.parseInt(prIdStr);
            final var response = bitbucketClient.getPullRequest(workspace, repoSlug, prId);
            return ToolResponse.success(AtlassianProduct.BITBUCKET,
                    "bitbucket_get_pull_request", formatPullRequestFromJson(response));
        } catch (NumberFormatException numberException) {
            return ToolResponse.error(AtlassianProduct.BITBUCKET,
                    "bitbucket_get_pull_request",
                    "Invalid prId — must be a number: " + prIdStr);
        } catch (IOException | InterruptedException exception) {
            LOGGER.log(Level.WARNING,
                    "Failed to get PR #" + prIdStr + " for " + workspace + "/" + repoSlug,
                    exception);
            return ToolResponse.error(AtlassianProduct.BITBUCKET,
                    "bitbucket_get_pull_request",
                    "Failed to get pull request: " + exception.getMessage());
        }
    }

    /**
     * Searches code across repositories in a workspace.
     *
     * @param arguments the tool arguments ({@code workspace}, {@code query})
     * @return the tool response with code search results
     */
    public ToolResponse searchCode(final Map<String, String> arguments) {
        final var workspace = arguments.get("workspace");
        final var query = arguments.get("query");

        if (workspace == null || workspace.isBlank()) {
            return ToolResponse.error(AtlassianProduct.BITBUCKET,
                    "bitbucket_search_code",
                    "Missing required argument: 'workspace'");
        }
        if (query == null || query.isBlank()) {
            return ToolResponse.error(AtlassianProduct.BITBUCKET,
                    "bitbucket_search_code",
                    "Missing required argument: 'query'");
        }

        try {
            final var response = bitbucketClient.searchCode(workspace, query);
            return ToolResponse.success(AtlassianProduct.BITBUCKET,
                    "bitbucket_search_code", response);
        } catch (IOException | InterruptedException exception) {
            LOGGER.log(Level.WARNING,
                    "Code search failed in " + workspace, exception);
            return ToolResponse.error(AtlassianProduct.BITBUCKET,
                    "bitbucket_search_code",
                    "Code search failed: " + exception.getMessage());
        }
    }

    /**
     * Parses maxResults from arguments with a safe default.
     * Delegated to {@link HandlerUtils#parseMaxResults(String)}.
     */
    private int parseMaxResults(final String value) {
        return HandlerUtils.parseMaxResults(value);
    }

    /**
     * Creates a new pull request in a repository.
     *
     * @param arguments the tool arguments ({@code workspace}, {@code repoSlug},
     *                  {@code title}, {@code sourceBranch}, {@code targetBranch},
     *                  optional {@code description})
     * @return the tool response with the created PR
     */
    public ToolResponse createPullRequest(final Map<String, String> arguments) {
        final var workspace   = arguments.get("workspace");
        final var repoSlug    = arguments.get("repoSlug");
        final var title       = arguments.get("title");
        final var sourceBranch = arguments.get("sourceBranch");
        final var targetBranch = arguments.getOrDefault("targetBranch", "main");
        final var description = arguments.getOrDefault("description", "");

        if (workspace == null || workspace.isBlank()) {
            return ToolResponse.error(AtlassianProduct.BITBUCKET, "bitbucket_create_pull_request",
                    "Missing required argument: 'workspace'");
        }
        if (repoSlug == null || repoSlug.isBlank()) {
            return ToolResponse.error(AtlassianProduct.BITBUCKET, "bitbucket_create_pull_request",
                    "Missing required argument: 'repoSlug'");
        }
        if (title == null || title.isBlank()) {
            return ToolResponse.error(AtlassianProduct.BITBUCKET, "bitbucket_create_pull_request",
                    "Missing required argument: 'title'");
        }
        if (sourceBranch == null || sourceBranch.isBlank()) {
            return ToolResponse.error(AtlassianProduct.BITBUCKET, "bitbucket_create_pull_request",
                    "Missing required argument: 'sourceBranch'");
        }

        final var requestBody = buildCreatePrJson(title, description, sourceBranch, targetBranch);

        try {
            final var response = bitbucketClient.createPullRequest(workspace, repoSlug, requestBody);
            final var prId = JsonExtractor.intValue(response, "id", 0);
            return ToolResponse.success(AtlassianProduct.BITBUCKET,
                    "bitbucket_create_pull_request",
                    "Pull request #" + prId + " created: " + title
                    + "\n" + sourceBranch + " → " + targetBranch);
        } catch (IOException | InterruptedException exception) {
            LOGGER.log(Level.WARNING, "Failed to create PR in " + workspace + "/" + repoSlug, exception);
            return ToolResponse.error(AtlassianProduct.BITBUCKET, "bitbucket_create_pull_request",
                    "Failed to create pull request: " + exception.getMessage());
        }
    }

    /**
     * Lists branches in a Bitbucket repository.
     *
     * @param arguments the tool arguments ({@code workspace}, {@code repoSlug},
     *                  optional {@code maxResults})
     * @return the tool response with branch list
     */
    public ToolResponse listBranches(final Map<String, String> arguments) {
        final var workspace = arguments.get("workspace");
        final var repoSlug  = arguments.get("repoSlug");

        if (workspace == null || workspace.isBlank()) {
            return ToolResponse.error(AtlassianProduct.BITBUCKET, "bitbucket_list_branches",
                    "Missing required argument: 'workspace'");
        }
        if (repoSlug == null || repoSlug.isBlank()) {
            return ToolResponse.error(AtlassianProduct.BITBUCKET, "bitbucket_list_branches",
                    "Missing required argument: 'repoSlug'");
        }

        final int maxResults = parseMaxResults(arguments.get("maxResults"));

        try {
            final var response = bitbucketClient.listBranches(workspace, repoSlug, maxResults);
            return ToolResponse.success(AtlassianProduct.BITBUCKET,
                    "bitbucket_list_branches", formatBranchListFromJson(response));
        } catch (IOException | InterruptedException exception) {
            LOGGER.log(Level.WARNING, "Failed to list branches for " + workspace + "/" + repoSlug, exception);
            return ToolResponse.error(AtlassianProduct.BITBUCKET, "bitbucket_list_branches",
                    "Failed to list branches: " + exception.getMessage());
        }
    }

    /**
     * Gets commits for a Bitbucket repository.
     *
     * @param arguments the tool arguments ({@code workspace}, {@code repoSlug},
     *                  optional {@code branch}, optional {@code maxResults})
     * @return the tool response with commit list
     */
    public ToolResponse getCommits(final Map<String, String> arguments) {
        final var workspace = arguments.get("workspace");
        final var repoSlug  = arguments.get("repoSlug");

        if (workspace == null || workspace.isBlank()) {
            return ToolResponse.error(AtlassianProduct.BITBUCKET, "bitbucket_get_commits",
                    "Missing required argument: 'workspace'");
        }
        if (repoSlug == null || repoSlug.isBlank()) {
            return ToolResponse.error(AtlassianProduct.BITBUCKET, "bitbucket_get_commits",
                    "Missing required argument: 'repoSlug'");
        }

        final var branch     = arguments.get("branch");
        final int maxResults = parseMaxResults(arguments.get("maxResults"));

        try {
            final var response = bitbucketClient.getCommits(workspace, repoSlug, branch, maxResults);
            return ToolResponse.success(AtlassianProduct.BITBUCKET,
                    "bitbucket_get_commits", formatCommitListFromJson(response));
        } catch (IOException | InterruptedException exception) {
            LOGGER.log(Level.WARNING, "Failed to get commits for " + workspace + "/" + repoSlug, exception);
            return ToolResponse.error(AtlassianProduct.BITBUCKET, "bitbucket_get_commits",
                    "Failed to get commits: " + exception.getMessage());
        }
    }

    // ── Formatter helpers ────────────────────────────────────────────────────

    /**
     * Formats a Bitbucket pull request JSON response into readable markdown.
     */
    private String formatPullRequestFromJson(final String json) {
        final int prId = JsonExtractor.intValue(json, "id", 0);
        final var title  = JsonExtractor.stringOrDefault(json, "title", "");
        final var state  = JsonExtractor.stringOrDefault(json, "state", "-");
        final var desc   = JsonExtractor.stringOrDefault(json, "description", "");
        final var author = JsonExtractor.navigate(json, "author", "display_name").orElse("-");
        final var source = JsonExtractor.navigate(json, "source", "branch", "name").orElse("-");
        final var target = JsonExtractor.navigate(json, "destination", "branch", "name").orElse("-");
        final int comments = JsonExtractor.intValue(json, "comment_count", 0);
        final var created = JsonExtractor.stringOrDefault(json, "created_on", "-");
        final var updated = JsonExtractor.stringOrDefault(json, "updated_on", "-");
        final var webUrl  = JsonExtractor.navigate(json, "links", "html", "href").orElse("");

        final var sb = new StringBuilder();
        sb.append("## PR #").append(prId).append(" — ").append(title).append("\n\n");
        sb.append("**State:** ").append(state).append("\n");
        sb.append("**Author:** ").append(author).append("\n");
        sb.append("**Branch:** ").append(source).append(" → ").append(target).append("\n");
        sb.append("**Comments:** ").append(comments).append("\n");
        sb.append("**Created:** ").append(created).append("\n");
        sb.append("**Updated:** ").append(updated).append("\n");
        if (!webUrl.isBlank()) sb.append("**URL:** ").append(webUrl).append("\n");
        if (!desc.isBlank()) sb.append("\n### Description\n\n").append(desc).append("\n");
        return sb.toString();
    }

    /**
     * Formats a Bitbucket PR list JSON response into a markdown table.
     */
    private String formatPullRequestListFromJson(final String json) {
        final var prs = JsonExtractor.arrayBlocks(json, "values");
        if (prs.isEmpty()) return "No pull requests found.";

        final var sb = new StringBuilder();
        sb.append("## Pull Requests (").append(prs.size()).append(")\n\n");
        sb.append("| # | Title | State | Author | Branch |\n");
        sb.append("|---|-------|-------|--------|--------|\n");

        for (final var pr : prs) {
            final int id = JsonExtractor.intValue(pr, "id", 0);
            final var title  = truncate(JsonExtractor.stringOrDefault(pr, "title", ""), 40);
            final var state  = JsonExtractor.stringOrDefault(pr, "state", "-");
            final var author = JsonExtractor.navigate(pr, "author", "display_name").orElse("-");
            final var source = JsonExtractor.navigate(pr, "source", "branch", "name").orElse("-");
            final var target = JsonExtractor.navigate(pr, "destination", "branch", "name").orElse("-");
            sb.append("| ").append(id)
              .append(" | ").append(title)
              .append(" | ").append(state)
              .append(" | ").append(author)
              .append(" | ").append(source).append(" → ").append(target)
              .append(" |\n");
        }
        return sb.toString();
    }

    /**
     * Formats a Bitbucket branch list JSON response into readable markdown.
     */
    private String formatBranchListFromJson(final String json) {
        final var branches = JsonExtractor.arrayBlocks(json, "values");
        if (branches.isEmpty()) return "No branches found.";

        final var sb = new StringBuilder();
        sb.append("## Branches (").append(branches.size()).append(")\n\n");

        for (final var branch : branches) {
            final var name = JsonExtractor.stringOrDefault(branch, "name", "-");
            final var hash = JsonExtractor.navigate(branch, "target", "hash").orElse("");
            final var message = truncate(
                    JsonExtractor.navigate(branch, "target", "message").orElse(""), 60);
            sb.append("- **").append(name).append("**");
            if (!hash.isBlank()) sb.append(" `").append(hash, 0, Math.min(8, hash.length())).append("`");
            if (!message.isBlank()) sb.append(" — ").append(message);
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Formats a Bitbucket commit list JSON response into readable markdown.
     */
    private String formatCommitListFromJson(final String json) {
        final var commits = JsonExtractor.arrayBlocks(json, "values");
        if (commits.isEmpty()) return "No commits found.";

        final var sb = new StringBuilder();
        sb.append("## Commits (").append(commits.size()).append(")\n\n");
        sb.append("| Hash | Author | Date | Message |\n");
        sb.append("|------|--------|------|---------|\n");

        for (final var commit : commits) {
            final var hash    = JsonExtractor.stringOrDefault(commit, "hash", "-");
            final var shortHash = hash.length() > 8 ? hash.substring(0, 8) : hash;
            final var author  = JsonExtractor.navigate(commit, "author", "user", "display_name")
                                .or(() -> JsonExtractor.string(commit, "author"))
                                .orElse("-");
            final var date    = JsonExtractor.stringOrDefault(commit, "date", "-");
            final var message = truncate(JsonExtractor.stringOrDefault(commit, "message", ""), 50);
            sb.append("| ").append(shortHash)
              .append(" | ").append(author)
              .append(" | ").append(date)
              .append(" | ").append(message)
              .append(" |\n");
        }
        return sb.toString();
    }

    /**
     * Builds the JSON body for creating a Bitbucket pull request.
     */
    private String buildCreatePrJson(final String title, final String description,
                                     final String sourceBranch, final String targetBranch) {
        return """
                {
                  "title": "%s",
                  "description": "%s",
                  "source": {"branch": {"name": "%s"}},
                  "destination": {"branch": {"name": "%s"}}
                }
                """.formatted(
                escapeJson(title), escapeJson(description),
                escapeJson(sourceBranch), escapeJson(targetBranch));
    }

    /**
     * Escapes a string for safe JSON embedding.
     * Delegated to {@link HandlerUtils#escapeJson(String)}.
     */
    private String escapeJson(final String text) {
        return HandlerUtils.escapeJson(text);
    }

    /**
     * Truncates text to a maximum length with ellipsis.
     * Delegated to {@link HandlerUtils#truncate(String, int)}.
     */
    private String truncate(final String text, final int maxLength) {
        return HandlerUtils.truncate(text, maxLength);
    }
}
