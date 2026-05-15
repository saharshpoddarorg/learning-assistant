package server.atlassian.v1.model.bitbucket;

import java.time.Instant;
import java.util.Objects;

/**
 * Represents a Bitbucket pull request summary.
 *
 * @param id           the PR numeric ID
 * @param title        the PR title
 * @param description  the PR description
 * @param state        the PR state (e.g., "OPEN", "MERGED", "DECLINED")
 * @param authorName   the author display name
 * @param sourceBranch the source branch name
 * @param targetBranch the target/destination branch name
 * @param reviewers    number of reviewers
 * @param commentCount number of comments
 * @param webUrl       the web URL to view this PR
 * @param created      when the PR was created
 * @param updated      when the PR was last updated
 */
public record BitbucketPullRequest(
        int id,
        String title,
        String description,
        PullRequestState state,
        String authorName,
        String sourceBranch,
        String targetBranch,
        int reviewers,
        int commentCount,
        String webUrl,
        Instant created,
        Instant updated
) {

    /**
     * Creates a {@link BitbucketPullRequest} with validation.
     *
     * @param id           PR ID
     * @param title        PR title
     * @param description  PR description
     * @param state        PR state
     * @param authorName   author name
     * @param sourceBranch source branch
     * @param targetBranch target branch
     * @param reviewers    reviewer count
     * @param commentCount comment count
     * @param webUrl       web URL
     * @param created      creation timestamp
     * @param updated      last update timestamp
     */
    public BitbucketPullRequest {
        Objects.requireNonNull(title, "Title must not be null");
        Objects.requireNonNull(description, "Description must not be null (use empty string)");
        Objects.requireNonNull(state, "State must not be null");
        Objects.requireNonNull(authorName, "Author name must not be null (use empty string)");
        Objects.requireNonNull(sourceBranch, "Source branch must not be null");
        Objects.requireNonNull(targetBranch, "Target branch must not be null");
        Objects.requireNonNull(webUrl, "Web URL must not be null (use empty string)");
        Objects.requireNonNull(created, "Created timestamp must not be null");
        Objects.requireNonNull(updated, "Updated timestamp must not be null");
    }

    /**
     * Checks whether this PR is still open for review.
     *
     * @return {@code true} if the state is OPEN
     */
    public boolean isOpen() {
        return state == PullRequestState.OPEN;
    }
}
