package server.atlassian.v2.handler;

import server.atlassian.v2.model.Product;
import server.atlassian.v2.model.ToolResult;

import java.util.Map;
import java.util.Objects;

/**
 * Central dispatcher that routes incoming MCP tool calls to the correct
 * product-specific handler based on the tool name prefix.
 *
 * <p>Routing rules:
 * <ul>
 *   <li>{@code jira_*}       → {@link JiraToolHandler}</li>
 *   <li>{@code confluence_*} → {@link ConfluenceToolHandler}</li>
 *   <li>{@code bitbucket_*}  → {@link BitbucketToolHandler}</li>
 *   <li>{@code atlassian_*}  → {@link CrossProductHandler}</li>
 *   <li>{@code auth_*}       → {@link AuthToolHandler}</li>
 * </ul>
 */
public final class ToolDispatcher {

    private final JiraToolHandler jiraHandler;
    private final ConfluenceToolHandler confluenceHandler;
    private final BitbucketToolHandler bitbucketHandler;
    private final CrossProductHandler crossProductHandler;
    private final AuthToolHandler authHandler;

    public ToolDispatcher(final JiraToolHandler jiraHandler,
                          final ConfluenceToolHandler confluenceHandler,
                          final BitbucketToolHandler bitbucketHandler,
                          final CrossProductHandler crossProductHandler,
                          final AuthToolHandler authHandler) {
        this.jiraHandler = Objects.requireNonNull(jiraHandler);
        this.confluenceHandler = Objects.requireNonNull(confluenceHandler);
        this.bitbucketHandler = Objects.requireNonNull(bitbucketHandler);
        this.crossProductHandler = Objects.requireNonNull(crossProductHandler);
        this.authHandler = Objects.requireNonNull(authHandler);
    }

    /**
     * Dispatches a tool call to the appropriate handler.
     *
     * @param toolName the MCP tool name (e.g. {@code jira_search_issues})
     * @param args     the tool arguments as key→value pairs
     * @return a {@link ToolResult} with the response
     */
    public ToolResult dispatch(final String toolName, final Map<String, String> args) {
        if (toolName == null || toolName.isBlank()) {
            return ToolResult.error(Product.CROSS_PRODUCT, "unknown", "Tool name is required");
        }

        final Product product = Product.fromToolName(toolName);
        if (product == null) {
            return ToolResult.error(Product.CROSS_PRODUCT, toolName, "Unknown tool: " + toolName);
        }

        // Auth tools have their own prefix
        if (toolName.startsWith("auth_")) {
            return authHandler.handle(toolName, args);
        }

        return switch (product) {
            case JIRA          -> jiraHandler.handle(toolName, args);
            case CONFLUENCE    -> confluenceHandler.handle(toolName, args);
            case BITBUCKET     -> bitbucketHandler.handle(toolName, args);
            case CROSS_PRODUCT -> crossProductHandler.handle(toolName, args);
        };
    }
}
