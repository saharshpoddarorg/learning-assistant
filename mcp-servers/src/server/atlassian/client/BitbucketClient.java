package server.atlassian.client;

import server.atlassian.model.ConnectionConfig;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * REST API client for Bitbucket Cloud (API 2.0).
 *
 * <p>Provides methods for listing repositories, browsing pull requests,
 * and searching code. All methods return raw JSON strings parsed by
 * the handler layer.
 *
 * <p><strong>API Reference:</strong>
 * <a href="https://developer.atlassian.com/cloud/bitbucket/rest/intro/">Bitbucket REST API 2.0</a>
 *
 * <p><strong>Note:</strong> Bitbucket Cloud uses {@code https://api.bitbucket.org/2.0}
 * as its base URL, not the workspace URL.
 *
 * @see AtlassianRestClient
 */
public class BitbucketClient {

    private static final Logger LOGGER = Logger.getLogger(BitbucketClient.class.getName());
    private static final String API_BASE = "/2.0";

    private final AtlassianRestClient restClient;

    /**
     * Creates a Bitbucket client with the given connection configuration.
     *
     * <p>The base URL should be {@code https://api.bitbucket.org} for Cloud.
     *
     * @param config the connection settings
     */
    public BitbucketClient(final ConnectionConfig config) {
        Objects.requireNonNull(config, "ConnectionConfig must not be null");
        this.restClient = new AtlassianRestClient(config);
    }

    /**
     * Lists repositories in a workspace.
     *
     * @param workspace  the workspace slug
     * @param maxResults maximum number of results
     * @return the raw JSON response
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String listRepositories(final String workspace, final int maxResults)
            throws IOException, InterruptedException {
        Objects.requireNonNull(workspace, "Workspace must not be null");
        final var path = API_BASE + "/repositories/" + workspace + "?pagelen=" + maxResults;
        LOGGER.info("Listing repositories in workspace: " + workspace);
        return restClient.get(path);
    }

    /**
     * Gets details of a single repository.
     *
     * @param workspace the workspace slug
     * @param repoSlug  the repository slug
     * @return the raw JSON response
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String getRepository(final String workspace, final String repoSlug)
            throws IOException, InterruptedException {
        Objects.requireNonNull(workspace, "Workspace must not be null");
        Objects.requireNonNull(repoSlug, "Repository slug must not be null");
        final var path = API_BASE + "/repositories/" + workspace + "/" + repoSlug;
        LOGGER.info("Getting repository: " + workspace + "/" + repoSlug);
        return restClient.get(path);
    }

    /**
     * Lists pull requests for a repository.
     *
     * @param workspace  the workspace slug
     * @param repoSlug   the repository slug
     * @param state      the PR state filter (e.g., "OPEN", "MERGED", or empty for all)
     * @param maxResults maximum number of results
     * @return the raw JSON response
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String listPullRequests(final String workspace,
                                   final String repoSlug,
                                   final String state,
                                   final int maxResults)
            throws IOException, InterruptedException {
        Objects.requireNonNull(workspace, "Workspace must not be null");
        Objects.requireNonNull(repoSlug, "Repository slug must not be null");
        var path = API_BASE + "/repositories/" + workspace + "/" + repoSlug
                + "/pullrequests?pagelen=" + maxResults;
        if (state != null && !state.isBlank()) {
            path += "&state=" + state;
        }
        LOGGER.info("Listing PRs for " + workspace + "/" + repoSlug);
        return restClient.get(path);
    }

    /**
     * Gets details of a single pull request.
     *
     * @param workspace the workspace slug
     * @param repoSlug  the repository slug
     * @param prId      the pull request ID
     * @return the raw JSON response
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String getPullRequest(final String workspace,
                                 final String repoSlug,
                                 final int prId)
            throws IOException, InterruptedException {
        Objects.requireNonNull(workspace, "Workspace must not be null");
        Objects.requireNonNull(repoSlug, "Repository slug must not be null");
        final var path = API_BASE + "/repositories/" + workspace + "/" + repoSlug
                + "/pullrequests/" + prId;
        LOGGER.info("Getting PR #" + prId + " for " + workspace + "/" + repoSlug);
        return restClient.get(path);
    }

    /**
     * Searches code across repositories in a workspace.
     *
     * @param workspace   the workspace slug
     * @param searchQuery the code search query
     * @return the raw JSON response
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String searchCode(final String workspace, final String searchQuery)
            throws IOException, InterruptedException {
        Objects.requireNonNull(workspace, "Workspace must not be null");
        Objects.requireNonNull(searchQuery, "Search query must not be null");
        final var encodedQuery = java.net.URLEncoder.encode(
                searchQuery, java.nio.charset.StandardCharsets.UTF_8);
        final var path = API_BASE + "/workspaces/" + workspace
                + "/search/code?search_query=" + encodedQuery;
        LOGGER.info("Searching code in " + workspace + ": " + searchQuery);
        return restClient.get(path);
    }

    /**
     * Creates a new pull request.
     *
     * @param workspace   the workspace slug
     * @param repoSlug    the repository slug
     * @param requestBody the JSON body with title, source, destination branch info
     * @return the raw JSON response with the created PR
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String createPullRequest(final String workspace,
                                    final String repoSlug,
                                    final String requestBody)
            throws IOException, InterruptedException {
        Objects.requireNonNull(workspace, "Workspace must not be null");
        Objects.requireNonNull(repoSlug, "Repository slug must not be null");
        Objects.requireNonNull(requestBody, "Request body must not be null");
        final var path = API_BASE + "/repositories/" + workspace + "/" + repoSlug
                + "/pullrequests";
        LOGGER.info("Creating PR in " + workspace + "/" + repoSlug);
        return restClient.post(path, requestBody);
    }

    /**
     * Lists branches in a repository.
     *
     * @param workspace  the workspace slug
     * @param repoSlug   the repository slug
     * @param maxResults maximum number of results
     * @return the raw JSON response with branch list
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String listBranches(final String workspace,
                                final String repoSlug,
                                final int maxResults)
            throws IOException, InterruptedException {
        Objects.requireNonNull(workspace, "Workspace must not be null");
        Objects.requireNonNull(repoSlug, "Repository slug must not be null");
        final var path = API_BASE + "/repositories/" + workspace + "/" + repoSlug
                + "/refs/branches?pagelen=" + maxResults;
        LOGGER.info("Listing branches for " + workspace + "/" + repoSlug);
        return restClient.get(path);
    }

    /**
     * Gets commits for a repository, optionally filtered by branch.
     *
     * @param workspace  the workspace slug
     * @param repoSlug   the repository slug
     * @param branch     the branch to filter by (null for default branch)
     * @param maxResults maximum number of commits to return
     * @return the raw JSON response with commits
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String getCommits(final String workspace,
                              final String repoSlug,
                              final String branch,
                              final int maxResults)
            throws IOException, InterruptedException {
        Objects.requireNonNull(workspace, "Workspace must not be null");
        Objects.requireNonNull(repoSlug, "Repository slug must not be null");
        var path = API_BASE + "/repositories/" + workspace + "/" + repoSlug
                + "/commits?pagelen=" + maxResults;
        if (branch != null && !branch.isBlank()) {
            path += "&include=" + java.net.URLEncoder.encode(
                    branch, java.nio.charset.StandardCharsets.UTF_8);
        }
        LOGGER.info("Getting commits for " + workspace + "/" + repoSlug
                + (branch != null ? " branch:" + branch : ""));
        return restClient.get(path);
    }

    /**
     * Gets the diff for a pull request.
     *
     * @param workspace the workspace slug
     * @param repoSlug  the repository slug
     * @param prId      the pull request ID
     * @return the raw diff content as a string
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String getPullRequestDiff(final String workspace,
                                     final String repoSlug,
                                     final int prId)
            throws IOException, InterruptedException {
        Objects.requireNonNull(workspace, "Workspace must not be null");
        Objects.requireNonNull(repoSlug, "Repository slug must not be null");
        final var path = API_BASE + "/repositories/" + workspace + "/" + repoSlug
                + "/pullrequests/" + prId + "/diff";
        LOGGER.info("Getting diff for PR #" + prId + " in " + workspace + "/" + repoSlug);
        return restClient.get(path);
    }

    /**
     * Gets comments on a pull request.
     *
     * @param workspace the workspace slug
     * @param repoSlug  the repository slug
     * @param prId      the pull request ID
     * @return the raw JSON response with PR comments
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String getPrComments(final String workspace,
                                final String repoSlug,
                                final int prId)
            throws IOException, InterruptedException {
        Objects.requireNonNull(workspace, "Workspace must not be null");
        Objects.requireNonNull(repoSlug, "Repository slug must not be null");
        final var path = API_BASE + "/repositories/" + workspace + "/" + repoSlug
                + "/pullrequests/" + prId + "/comments";
        LOGGER.info("Getting comments for PR #" + prId + " in " + workspace + "/" + repoSlug);
        return restClient.get(path);
    }

    /**
     * Adds a comment to a pull request.
     *
     * @param workspace the workspace slug
     * @param repoSlug  the repository slug
     * @param prId      the pull request ID
     * @param content   the comment text (raw markup)
     * @return the raw JSON response with the created comment
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String addPrComment(final String workspace,
                               final String repoSlug,
                               final int prId,
                               final String content)
            throws IOException, InterruptedException {
        Objects.requireNonNull(workspace, "Workspace must not be null");
        Objects.requireNonNull(repoSlug, "Repository slug must not be null");
        Objects.requireNonNull(content, "Comment content must not be null");
        final var path = API_BASE + "/repositories/" + workspace + "/" + repoSlug
                + "/pullrequests/" + prId + "/comments";
        final var escapedContent = content.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r");
        final var body = "{\"content\":{\"raw\":\"" + escapedContent + "\"}}";
        LOGGER.info("Adding comment to PR #" + prId + " in " + workspace + "/" + repoSlug);
        return restClient.post(path, body);
    }

    /**
     * Approves a pull request.
     *
     * @param workspace the workspace slug
     * @param repoSlug  the repository slug
     * @param prId      the pull request ID
     * @return the raw JSON response
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String approvePullRequest(final String workspace,
                                     final String repoSlug,
                                     final int prId)
            throws IOException, InterruptedException {
        Objects.requireNonNull(workspace, "Workspace must not be null");
        Objects.requireNonNull(repoSlug, "Repository slug must not be null");
        final var path = API_BASE + "/repositories/" + workspace + "/" + repoSlug
                + "/pullrequests/" + prId + "/approve";
        LOGGER.info("Approving PR #" + prId + " in " + workspace + "/" + repoSlug);
        return restClient.post(path, "{}");
    }

    /**
     * Declines (rejects) a pull request.
     *
     * @param workspace the workspace slug
     * @param repoSlug  the repository slug
     * @param prId      the pull request ID
     * @return the raw JSON response
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String declinePullRequest(final String workspace,
                                     final String repoSlug,
                                     final int prId)
            throws IOException, InterruptedException {
        Objects.requireNonNull(workspace, "Workspace must not be null");
        Objects.requireNonNull(repoSlug, "Repository slug must not be null");
        final var path = API_BASE + "/repositories/" + workspace + "/" + repoSlug
                + "/pullrequests/" + prId + "/decline";
        LOGGER.info("Declining PR #" + prId + " in " + workspace + "/" + repoSlug);
        return restClient.post(path, "{}");
    }

    /**
     * Merges a pull request.
     *
     * @param workspace the workspace slug
     * @param repoSlug  the repository slug
     * @param prId      the pull request ID
     * @return the raw JSON response with the merged PR details
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String mergePullRequest(final String workspace,
                                   final String repoSlug,
                                   final int prId)
            throws IOException, InterruptedException {
        Objects.requireNonNull(workspace, "Workspace must not be null");
        Objects.requireNonNull(repoSlug, "Repository slug must not be null");
        final var path = API_BASE + "/repositories/" + workspace + "/" + repoSlug
                + "/pullrequests/" + prId + "/merge";
        LOGGER.info("Merging PR #" + prId + " in " + workspace + "/" + repoSlug);
        return restClient.post(path, "{}");
    }

    /**
     * Gets the content of a file from a repository.
     *
     * @param workspace the workspace slug
     * @param repoSlug  the repository slug
     * @param filePath  the file path within the repository
     * @param branch    the branch or commit to read from (null for default branch)
     * @return the raw file content as a string
     * @throws IOException          if the API call fails
     * @throws InterruptedException if the call is interrupted
     */
    public String getFileContent(final String workspace,
                                 final String repoSlug,
                                 final String filePath,
                                 final String branch)
            throws IOException, InterruptedException {
        Objects.requireNonNull(workspace, "Workspace must not be null");
        Objects.requireNonNull(repoSlug, "Repository slug must not be null");
        Objects.requireNonNull(filePath, "File path must not be null");
        var path = API_BASE + "/repositories/" + workspace + "/" + repoSlug
                + "/src/" + (branch != null && !branch.isBlank() ? branch : "HEAD")
                + "/" + filePath;
        LOGGER.info("Getting file content: " + workspace + "/" + repoSlug + "/" + filePath);
        return restClient.get(path);
    }
}
