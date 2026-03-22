package server.atlassian.v1.model.confluence;

import java.util.Objects;

/**
 * Represents a Confluence space summary.
 *
 * @param key         the space key (e.g., "DEV", "HR")
 * @param name        the space display name
 * @param type        the space type (e.g., "global", "personal")
 * @param description a brief space description
 * @param webUrl      the web URL to view this space
 */
public record ConfluenceSpace(
        String key,
        String name,
        String type,
        String description,
        String webUrl
) {

    /**
     * Creates a {@link ConfluenceSpace} with validation.
     *
     * @param key         space key
     * @param name        space name
     * @param type        space type
     * @param description space description
     * @param webUrl      web URL
     */
    public ConfluenceSpace {
        Objects.requireNonNull(key, "Space key must not be null");
        Objects.requireNonNull(name, "Space name must not be null");
        Objects.requireNonNull(type, "Space type must not be null");
        Objects.requireNonNull(description, "Description must not be null (use empty string)");
        Objects.requireNonNull(webUrl, "Web URL must not be null (use empty string)");
    }
}
