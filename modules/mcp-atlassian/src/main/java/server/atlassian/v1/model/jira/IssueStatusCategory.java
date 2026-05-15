package server.atlassian.v1.model.jira;

/**
 * Common Jira issue status categories.
 *
 * <p>Jira statuses belong to one of three status categories that control
 * workflow column mapping. Individual status names vary by project, but
 * these categories are universal.
 */
public enum IssueStatusCategory {

    /** Work has not started (e.g., "To Do", "Open", "Backlog"). */
    TO_DO("To Do", "new"),

    /** Work is actively in progress (e.g., "In Progress", "In Review"). */
    IN_PROGRESS("In Progress", "indeterminate"),

    /** Work is complete (e.g., "Done", "Closed", "Resolved"). */
    DONE("Done", "done");

    private final String displayName;
    private final String jiraKey;

    IssueStatusCategory(final String displayName, final String jiraKey) {
        this.displayName = displayName;
        this.jiraKey = jiraKey;
    }

    /**
     * Returns the human-readable category name.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the Jira REST API key for this category.
     *
     * @return the API key (e.g., "new", "indeterminate", "done")
     */
    public String getJiraKey() {
        return jiraKey;
    }

    /**
     * Resolves a status category from a Jira API key.
     *
     * @param key the Jira status category key (case-insensitive)
     * @return the matching category
     * @throws IllegalArgumentException if no match is found
     */
    public static IssueStatusCategory fromJiraKey(final String key) {
        for (final IssueStatusCategory category : values()) {
            if (category.jiraKey.equalsIgnoreCase(key)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown status category key: '" + key
                + "'. Valid keys: new, indeterminate, done");
    }
}
