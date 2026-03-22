package server.atlassian.v1.formatter;

import server.atlassian.v1.model.bitbucket.BitbucketPullRequest;

import java.util.List;
import java.util.Objects;

/**
 * Formats Bitbucket pull requests into clean, readable text for AI assistant consumption.
 *
 * <p>Converts {@link BitbucketPullRequest} records into structured text
 * output suitable for MCP tool responses.
 */
public class PullRequestFormatter {

    /**
     * Formats a single pull request into a readable text block.
     *
     * @param pullRequest the pull request to format
     * @return the formatted text
     */
    public String formatPullRequest(final BitbucketPullRequest pullRequest) {
        Objects.requireNonNull(pullRequest, "Pull request must not be null");

        final var builder = new StringBuilder();
        builder.append("## PR #").append(pullRequest.id())
                .append(" — ").append(pullRequest.title()).append("\n\n");
        builder.append("**State:** ").append(pullRequest.state().getApiValue()).append("\n");
        builder.append("**Author:** ").append(pullRequest.authorName()).append("\n");
        builder.append("**Branch:** ").append(pullRequest.sourceBranch())
                .append(" → ").append(pullRequest.targetBranch()).append("\n");
        builder.append("**Reviewers:** ").append(pullRequest.reviewers()).append("\n");
        builder.append("**Comments:** ").append(pullRequest.commentCount()).append("\n");
        builder.append("**Created:** ").append(pullRequest.created()).append("\n");
        builder.append("**Updated:** ").append(pullRequest.updated()).append("\n");

        if (!pullRequest.webUrl().isBlank()) {
            builder.append("**URL:** ").append(pullRequest.webUrl()).append("\n");
        }

        if (!pullRequest.description().isBlank()) {
            builder.append("\n### Description\n\n")
                    .append(pullRequest.description()).append("\n");
        }

        return builder.toString();
    }

    /**
     * Formats a list of pull requests as a summary table.
     *
     * @param pullRequests the list of pull requests
     * @param title        the section title
     * @return the formatted text
     */
    public String formatPullRequestList(final List<BitbucketPullRequest> pullRequests,
                                        final String title) {
        Objects.requireNonNull(pullRequests, "Pull requests list must not be null");

        final var builder = new StringBuilder();
        builder.append("## ").append(title).append(" (")
                .append(pullRequests.size()).append(" results)\n\n");
        builder.append("| # | Title | State | Author | Branch |\n");
        builder.append("|---|-------|-------|--------|--------|\n");

        for (final BitbucketPullRequest pullRequest : pullRequests) {
            builder.append("| ").append(pullRequest.id())
                    .append(" | ").append(truncate(pullRequest.title(), 40))
                    .append(" | ").append(pullRequest.state().getApiValue())
                    .append(" | ").append(pullRequest.authorName())
                    .append(" | ").append(pullRequest.sourceBranch())
                    .append(" → ").append(pullRequest.targetBranch())
                    .append(" |\n");
        }

        return builder.toString();
    }

    /**
     * Truncates text to a maximum length with ellipsis.
     */
    private String truncate(final String text, final int maxLength) {
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }
}
