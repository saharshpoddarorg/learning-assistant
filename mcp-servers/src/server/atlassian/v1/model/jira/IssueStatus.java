package server.atlassian.v1.model.jira;

/**
 * Common Jira workflow statuses with their status categories.
 *
 * <p>Jira instances can define custom statuses, so this enum covers the
 * built-in statuses that appear across Jira Software, Jira Service Management,
 * and classic projects. Each status belongs to one of three immutable categories:
 * {@link IssueStatusCategory#TO_DO}, {@link IssueStatusCategory#IN_PROGRESS}, or
 * {@link IssueStatusCategory#DONE}.
 *
 * <p>Use {@link #fromDisplayName(String)} to resolve a raw API status string to a known value.
 * If the status is project-specific or custom, the result will be empty — callers should
 * fall back to storing the raw string and inferring the category separately.
 */
public enum IssueStatus {

    // -------------------------------------------------------------------------
    // TO_DO category — work has not started
    // -------------------------------------------------------------------------

    /** Default starting status for most issue types. */
    TO_DO("To Do", IssueStatusCategory.TO_DO),

    /** Classic Jira status — issue is acknowledged but not yet started. */
    OPEN("Open", IssueStatusCategory.TO_DO),

    /** Issue is queued for a future sprint or iteration. */
    BACKLOG("Backlog", IssueStatusCategory.TO_DO),

    /** Issue was previously closed/resolved but has been re-opened. */
    REOPENED("Reopened", IssueStatusCategory.TO_DO),

    /** JSM: Customer request is waiting in the service desk queue. */
    WAITING_FOR_SUPPORT("Waiting for Support", IssueStatusCategory.TO_DO),

    /** JSM: Waiting for the customer to respond before work can continue. */
    WAITING_FOR_CUSTOMER("Waiting for Customer", IssueStatusCategory.TO_DO),

    // -------------------------------------------------------------------------
    // IN_PROGRESS category — work is actively being done
    // -------------------------------------------------------------------------

    /** Issue is actively being worked on. */
    IN_PROGRESS("In Progress", IssueStatusCategory.IN_PROGRESS),

    /** Code is complete; awaiting peer review/approval. */
    IN_REVIEW("In Review", IssueStatusCategory.IN_PROGRESS),

    /** Code review specifically (used in some Software workflows). */
    CODE_REVIEW("Code Review", IssueStatusCategory.IN_PROGRESS),

    /** Issue is in QA or UAT testing. */
    TESTING("Testing", IssueStatusCategory.IN_PROGRESS),

    /** Issue is being validated after a fix/deployment. */
    VERIFICATION("Verification", IssueStatusCategory.IN_PROGRESS),

    /** Work is paused due to an external dependency or impediment. */
    BLOCKED("Blocked", IssueStatusCategory.IN_PROGRESS),

    /** JSM: Agent has responded and is actively handling the request. */
    IN_PROGRESS_JSM("In Progress", IssueStatusCategory.IN_PROGRESS),

    /** JSM: An on-call engineer is actively investigating the incident. */
    INVESTIGATING("Investigating", IssueStatusCategory.IN_PROGRESS),

    // -------------------------------------------------------------------------
    // DONE category — work is complete
    // -------------------------------------------------------------------------

    /** Work is complete and accepted. Most common done status. */
    DONE("Done", IssueStatusCategory.DONE),

    /** Classic Jira: issue is fully closed — no further action. */
    CLOSED("Closed", IssueStatusCategory.DONE),

    /** Issue has been fixed or the goal has been achieved. */
    RESOLVED("Resolved", IssueStatusCategory.DONE),

    /** Issue will not be addressed; intentionally rejected. */
    WONT_DO("Won't Do", IssueStatusCategory.DONE),

    /** Issue is a duplicate of another issue. */
    DUPLICATE("Duplicate", IssueStatusCategory.DONE),

    /** Fix has been deployed to production. */
    RELEASED("Released", IssueStatusCategory.DONE),

    /** Issue has been cancelled before work began or was completed. */
    CANCELLED("Cancelled", IssueStatusCategory.DONE),

    /** JSM: Incident has been resolved and service is restored. */
    INCIDENT_RESOLVED("Resolved", IssueStatusCategory.DONE),

    /** Change Management: Change was approved and successfully implemented. */
    IMPLEMENTED("Implemented", IssueStatusCategory.DONE),

    /** Change Management: Change was rejected or not approved. */
    REJECTED("Rejected", IssueStatusCategory.DONE);

    // -------------------------------------------------------------------------

    private final String displayName;
    private final IssueStatusCategory category;

    IssueStatus(final String displayName, final IssueStatusCategory category) {
        this.displayName = displayName;
        this.category = category;
    }

    /**
     * Returns the status name exactly as returned by the Jira REST API.
     *
     * @return the display name (e.g., "In Progress", "Won't Do")
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the status category this status belongs to.
     *
     * @return one of {@link IssueStatusCategory#TO_DO}, {@link IssueStatusCategory#IN_PROGRESS},
     *         or {@link IssueStatusCategory#DONE}
     */
    public IssueStatusCategory getCategory() {
        return category;
    }

    /**
     * Resolves a status from its display name or enum name.
     *
     * <p>Custom or project-specific statuses (e.g., "Deployed", "PO Review") will
     * return empty — callers should store the raw string in that case.
     *
     * @param value the status name from the Jira API (case-insensitive)
     * @return the matching status, or empty if not a known built-in value
     */
    public static java.util.Optional<IssueStatus> fromDisplayName(final String value) {
        if (value == null || value.isBlank()) {
            return java.util.Optional.empty();
        }
        for (final IssueStatus status : values()) {
            if (status.displayName.equalsIgnoreCase(value)
                    || status.name().equalsIgnoreCase(value)
                    || status.name().replace("_", " ").equalsIgnoreCase(value)) {
                return java.util.Optional.of(status);
            }
        }
        return java.util.Optional.empty();
    }

    /**
     * Returns all statuses that belong to the given category.
     *
     * @param category the status category to filter by
     * @return a list of statuses in that category
     */
    public static java.util.List<IssueStatus> forCategory(final IssueStatusCategory category) {
        final var result = new java.util.ArrayList<IssueStatus>();
        for (final IssueStatus status : values()) {
            if (status.category == category) {
                result.add(status);
            }
        }
        return java.util.List.copyOf(result);
    }
}
