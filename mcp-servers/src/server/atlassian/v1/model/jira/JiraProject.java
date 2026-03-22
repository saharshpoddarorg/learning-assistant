package server.atlassian.v1.model.jira;

import java.util.Objects;

/**
 * Represents a Jira project summary.
 *
 * @param key         the project key (e.g., "PROJ")
 * @param name        the project display name
 * @param projectType the project type (e.g., "software", "business")
 * @param lead        the project lead display name
 * @param description a brief project description
 */
public record JiraProject(
        String key,
        String name,
        String projectType,
        String lead,
        String description
) {

    /**
     * Creates a {@link JiraProject} with validation.
     *
     * @param key         project key
     * @param name        project name
     * @param projectType project type
     * @param lead        project lead
     * @param description project description
     */
    public JiraProject {
        Objects.requireNonNull(key, "Project key must not be null");
        Objects.requireNonNull(name, "Project name must not be null");
        Objects.requireNonNull(projectType, "Project type must not be null");
        Objects.requireNonNull(lead, "Lead must not be null (use empty string)");
        Objects.requireNonNull(description, "Description must not be null (use empty string)");
    }
}
