package server.atlassian.v1.model.bitbucket;

import java.time.Instant;
import java.util.Objects;

/**
 * Represents a Bitbucket repository summary.
 *
 * @param workspace   the workspace slug
 * @param slug        the repository slug
 * @param name        the repository display name
 * @param description repository description
 * @param language    the primary programming language
 * @param isPrivate   whether the repository is private
 * @param mainBranch  the default branch name (e.g., "main", "master")
 * @param webUrl      the web URL to view this repository
 * @param cloneUrl    the HTTPS clone URL
 * @param updated     when the repository was last updated
 */
public record BitbucketRepository(
        String workspace,
        String slug,
        String name,
        String description,
        String language,
        boolean isPrivate,
        String mainBranch,
        String webUrl,
        String cloneUrl,
        Instant updated
) {

    /**
     * Creates a {@link BitbucketRepository} with validation.
     *
     * @param workspace   workspace slug
     * @param slug        repo slug
     * @param name        repo name
     * @param description repo description
     * @param language    primary language
     * @param isPrivate   private flag
     * @param mainBranch  default branch
     * @param webUrl      web URL
     * @param cloneUrl    clone URL
     * @param updated     last update timestamp
     */
    public BitbucketRepository {
        Objects.requireNonNull(workspace, "Workspace must not be null");
        Objects.requireNonNull(slug, "Slug must not be null");
        Objects.requireNonNull(name, "Name must not be null");
        Objects.requireNonNull(description, "Description must not be null (use empty string)");
        Objects.requireNonNull(language, "Language must not be null (use empty string)");
        Objects.requireNonNull(mainBranch, "Main branch must not be null (use empty string)");
        Objects.requireNonNull(webUrl, "Web URL must not be null (use empty string)");
        Objects.requireNonNull(cloneUrl, "Clone URL must not be null (use empty string)");
        Objects.requireNonNull(updated, "Updated timestamp must not be null");
    }

    /**
     * Returns the full repository identifier ({workspace}/{slug}).
     *
     * @return the full repository path
     */
    public String fullName() {
        return workspace + "/" + slug;
    }
}
