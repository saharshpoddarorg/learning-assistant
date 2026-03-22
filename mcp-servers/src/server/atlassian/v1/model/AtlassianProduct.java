package server.atlassian.v1.model;

/**
 * Enumerates the Atlassian products supported by this MCP server.
 *
 * <p>Each product corresponds to a distinct REST API surface and
 * set of MCP tools exposed by the server.
 */
public enum AtlassianProduct {

    /** Jira — issue tracking, project management, sprint planning. */
    JIRA("jira", "Jira"),

    /** Confluence — team wiki, documentation, knowledge base. */
    CONFLUENCE("confluence", "Confluence"),

    /** Bitbucket — Git hosting, pull requests, CI/CD pipelines. */
    BITBUCKET("bitbucket", "Bitbucket");

    private final String key;
    private final String displayName;

    AtlassianProduct(final String key, final String displayName) {
        this.key = key;
        this.displayName = displayName;
    }

    /**
     * Returns the lowercase key used in configuration and API paths.
     *
     * @return the product key (e.g., "jira", "confluence")
     */
    public String getKey() {
        return key;
    }

    /**
     * Returns the human-readable product name.
     *
     * @return the display name (e.g., "Jira", "Confluence")
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Resolves a product from its key string.
     *
     * @param value the key (case-insensitive)
     * @return the matching product
     * @throws IllegalArgumentException if no match is found
     */
    public static AtlassianProduct fromKey(final String value) {
        for (final AtlassianProduct product : values()) {
            if (product.key.equalsIgnoreCase(value)) {
                return product;
            }
        }
        throw new IllegalArgumentException("Unknown Atlassian product: '" + value
                + "'. Valid values: jira, confluence, bitbucket");
    }
}
