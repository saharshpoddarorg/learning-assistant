package server.atlassian.v1.model.jira;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Jira issue with its core fields.
 *
 * <p>This is a simplified view of a Jira issue optimized for AI assistant
 * consumption. It captures the most commonly needed fields without
 * replicating the full Jira REST API response structure.
 *
 * @param key         the issue key (e.g., "PROJ-123")
 * @param summary     the issue title/summary
 * @param description the issue description (plain text or ADF-converted)
 * @param issueType   the issue type (Story, Bug, Task, etc.)
 * @param status      the current status name (e.g., "In Progress")
 * @param statusCategory the status category (To Do, In Progress, Done)
 * @param priority    the priority level
 * @param assignee    the assignee display name (empty if unassigned)
 * @param reporter    the reporter display name
 * @param projectKey  the project key (e.g., "PROJ")
 * @param labels      free-form labels attached to the issue
 * @param created     when the issue was created
 * @param updated     when the issue was last updated
 */
public record JiraIssue(
        String key,
        String summary,
        String description,
        String issueType,
        String status,
        IssueStatusCategory statusCategory,
        String priority,
        String assignee,
        String reporter,
        String projectKey,
        List<String> labels,
        Instant created,
        Instant updated
) {

    /**
     * Creates a {@link JiraIssue} with validation and defensive copying.
     *
     * @param key            issue key
     * @param summary        issue summary
     * @param description    issue description
     * @param issueType      issue type name
     * @param status         current status
     * @param statusCategory status category
     * @param priority       priority name
     * @param assignee       assignee name
     * @param reporter       reporter name
     * @param projectKey     project key
     * @param labels         labels list
     * @param created        creation timestamp
     * @param updated        last update timestamp
     */
    public JiraIssue {
        Objects.requireNonNull(key, "Issue key must not be null");
        Objects.requireNonNull(summary, "Summary must not be null");
        Objects.requireNonNull(description, "Description must not be null (use empty string)");
        Objects.requireNonNull(issueType, "Issue type must not be null");
        Objects.requireNonNull(status, "Status must not be null");
        Objects.requireNonNull(statusCategory, "Status category must not be null");
        Objects.requireNonNull(priority, "Priority must not be null");
        Objects.requireNonNull(assignee, "Assignee must not be null (use empty string)");
        Objects.requireNonNull(reporter, "Reporter must not be null (use empty string)");
        Objects.requireNonNull(projectKey, "Project key must not be null");
        Objects.requireNonNull(labels, "Labels must not be null (use empty list)");
        Objects.requireNonNull(created, "Created timestamp must not be null");
        Objects.requireNonNull(updated, "Updated timestamp must not be null");

        labels = List.copyOf(labels);
    }

    /**
     * Checks whether this issue is assigned to someone.
     *
     * @return {@code true} if an assignee is set
     */
    public boolean isAssigned() {
        return !assignee.isBlank();
    }

    /**
     * Checks whether this issue is in a completed state.
     *
     * @return {@code true} if the status category is DONE
     */
    public boolean isDone() {
        return statusCategory == IssueStatusCategory.DONE;
    }
}
