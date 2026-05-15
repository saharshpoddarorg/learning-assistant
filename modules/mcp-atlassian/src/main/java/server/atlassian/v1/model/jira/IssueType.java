package server.atlassian.v1.model.jira;

/**
 * Jira issue types — covers Jira Software, Jira Service Management, and legacy Server schemes.
 *
 * <p>Values are grouped by the Jira product / project type they belong to:
 * <ul>
 *   <li><strong>Universal</strong> — present in all project types</li>
 *   <li><strong>Jira Software</strong> — Software development projects</li>
 *   <li><strong>Jira Service Management</strong> — Service desk / ITSM projects</li>
 *   <li><strong>Classic / Server</strong> — Older Jira Server / Data Center schemes</li>
 * </ul>
 *
 * <p>When calling the Jira REST API the {@link #getDisplayName()} value is sent as-is
 * in the {@code issuetype.name} field. Custom types not covered here can be passed
 * as a free-text string directly in the request body.
 */
public enum IssueType {

    // -------------------------------------------------------------------------
    // Universal — appear in both Software and Service Management projects
    // -------------------------------------------------------------------------

    /** General unit of work; default type for most project templates. */
    TASK("Task"),

    /** A piece of work that is part of a larger task or story. */
    SUB_TASK("Sub-task"),

    /** A large body of work that groups stories, tasks, and bugs. */
    EPIC("Epic"),

    // -------------------------------------------------------------------------
    // Jira Software — Scrum / Kanban boards (software development)
    // -------------------------------------------------------------------------

    /** A user-facing feature or requirement written from the user's perspective. */
    STORY("Story"),

    /** A software defect reproducing unintended behavior. */
    BUG("Bug"),

    /**
     * A very large initiative spanning multiple epics.
     * Requires Jira Portfolio / Advanced Roadmaps.
     */
    INITIATIVE("Initiative"),

    // -------------------------------------------------------------------------
    // Jira Service Management — ITSM / service desk projects
    // -------------------------------------------------------------------------

    /** An unplanned interruption or degradation of a service. */
    INCIDENT("Incident"),

    /** A request for information or assistance raised by a customer. */
    SERVICE_REQUEST("Service Request"),

    /**
     * An underlying cause of one or more incidents; tracked for root-cause analysis.
     * Jira SM maps this to Problem Management in the ITIL workflow.
     */
    PROBLEM("Problem"),

    /**
     * A controlled modification to infrastructure, service, or process.
     * Follows an approval workflow in Jira Service Management.
     */
    CHANGE("Change"),

    /** An internal IT support request (e.g., hardware, access, onboarding). */
    IT_HELP("IT Help"),

    /**
     * A retrospective review after a major incident.
     * Also called "post-mortem" or "post-incident review" in some templates.
     */
    POST_INCIDENT_REVIEW("Post-incident Review"),

    // -------------------------------------------------------------------------
    // Classic / legacy — common in Jira Server and older Cloud projects
    // -------------------------------------------------------------------------

    /** An enhancement to an existing feature (used in classic projects). */
    IMPROVEMENT("Improvement"),

    /** A net-new product capability (used in classic projects). */
    NEW_FEATURE("New Feature"),

    /** A support or clarification request (used in classic / SD projects). */
    QUESTION("Question"),

    /** A change request following the classic project change workflow. */
    CHANGE_REQUEST("Change Request"),

    /** A test case or test execution record (Jira Test Execution plugins). */
    TEST("Test");

    private final String displayName;

    IssueType(final String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the Jira display name used in REST API calls.
     *
     * @return the display name (e.g., "Story", "Incident")
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Resolves an issue type from a display name or enum name string.
     *
     * <p>Falls back to {@link #TASK} if the value matches none of the known types,
     * since Jira supports custom issue types — callers should treat an empty result
     * as "pass the raw string to the API".
     *
     * @param value the display name or enum name (case-insensitive)
     * @return the matching type, or empty if no built-in match
     */
    public static java.util.Optional<IssueType> fromDisplayName(final String value) {
        for (final IssueType type : values()) {
            if (type.displayName.equalsIgnoreCase(value)
                    || type.name().equalsIgnoreCase(value)
                    || type.name().replace("_", " ").equalsIgnoreCase(value)) {
                return java.util.Optional.of(type);
            }
        }
        return java.util.Optional.empty();
    }

    /**
     * Returns all issue type display names as a formatted list string.
     *
     * @return comma-separated display names
     */
    public static String validValues() {
        final var sb = new StringBuilder();
        for (final IssueType type : values()) {
            if (!sb.isEmpty()) sb.append(", ");
            sb.append(type.displayName);
        }
        return sb.toString();
    }
}
