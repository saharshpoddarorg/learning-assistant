package server.atlassian.v1.formatter;

import server.atlassian.v1.model.jira.JiraIssue;
import util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * Formats Jira issues into clean, readable text for AI assistant consumption.
 *
 * <p>Converts raw API data or {@link JiraIssue} records into structured
 * text output suitable for MCP tool responses.
 */
public class IssueFormatter {

    /**
     * Formats a single Jira issue into a readable text block.
     *
     * @param issue the Jira issue to format
     * @return the formatted text
     */
    public String formatIssue(final JiraIssue issue) {
        Objects.requireNonNull(issue, "Issue must not be null");

        final var builder = new StringBuilder();
        builder.append("## ").append(issue.key()).append(" — ").append(issue.summary()).append("\n\n");
        builder.append("**Status:** ").append(issue.status())
                .append(" (").append(issue.statusCategory().getDisplayName()).append(")\n");
        builder.append("**Type:** ").append(issue.issueType()).append("\n");
        builder.append("**Priority:** ").append(issue.priority()).append("\n");
        builder.append("**Assignee:** ").append(
                issue.isAssigned() ? issue.assignee() : "_Unassigned_").append("\n");
        builder.append("**Reporter:** ").append(issue.reporter()).append("\n");
        builder.append("**Project:** ").append(issue.projectKey()).append("\n");

        if (!issue.labels().isEmpty()) {
            builder.append("**Labels:** ").append(String.join(", ", issue.labels())).append("\n");
        }

        builder.append("**Created:** ").append(issue.created()).append("\n");
        builder.append("**Updated:** ").append(issue.updated()).append("\n");

        if (!issue.description().isBlank()) {
            builder.append("\n### Description\n\n").append(issue.description()).append("\n");
        }

        return builder.toString();
    }

    /**
     * Formats a list of issues as a summary table.
     *
     * @param issues the list of issues
     * @param title  the section title
     * @return the formatted text
     */
    public String formatIssueList(final List<JiraIssue> issues, final String title) {
        Objects.requireNonNull(issues, "Issues list must not be null");

        final var builder = new StringBuilder();
        builder.append("## ").append(title).append(" (").append(issues.size()).append(" results)\n\n");
        builder.append("| Key | Summary | Status | Type | Assignee |\n");
        builder.append("|-----|---------|--------|------|----------|\n");

        for (final JiraIssue issue : issues) {
            builder.append("| ").append(issue.key())
                    .append(" | ").append(truncate(issue.summary(), 50))
                    .append(" | ").append(issue.status())
                    .append(" | ").append(issue.issueType())
                    .append(" | ").append(issue.isAssigned() ? issue.assignee() : "-")
                    .append(" |\n");
        }

        return builder.toString();
    }

    /**
     * Truncates text to a maximum length with ellipsis.
     */
    private String truncate(final String text, final int maxLength) {
        return StringUtils.truncate(text, maxLength);
    }
}
