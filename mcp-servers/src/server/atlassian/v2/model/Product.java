package server.atlassian.v2.model;

/**
 * Atlassian product identifiers.
 */
public enum Product {

    JIRA("Jira", "jira"),
    CONFLUENCE("Confluence", "confluence"),
    BITBUCKET("Bitbucket", "bitbucket"),
    CROSS_PRODUCT("Atlassian", "atlassian");

    private final String displayName;
    private final String toolPrefix;

    Product(final String displayName, final String toolPrefix) {
        this.displayName = displayName;
        this.toolPrefix = toolPrefix;
    }

    public String displayName() { return displayName; }

    public String toolPrefix() { return toolPrefix; }

    /**
     * Infers the product from a tool name prefix.
     *
     * @param toolName the MCP tool name (e.g. {@code "jira_search_issues"})
     * @return the matching product, or {@link #CROSS_PRODUCT} if no prefix matches
     */
    public static Product fromToolName(final String toolName) {
        if (toolName == null) return CROSS_PRODUCT;
        if (toolName.startsWith("jira_")) return JIRA;
        if (toolName.startsWith("confluence_")) return CONFLUENCE;
        if (toolName.startsWith("bitbucket_")) return BITBUCKET;
        return CROSS_PRODUCT;
    }
}
