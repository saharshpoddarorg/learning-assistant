package server.atlassian.v2.handler;

import server.atlassian.v2.auth.AuthManager;
import server.atlassian.v2.client.BitbucketClientV2;
import server.atlassian.v2.client.ConfluenceClientV2;
import server.atlassian.v2.client.JiraClientV2;
import server.atlassian.v2.model.Product;
import server.atlassian.v2.model.ToolResult;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles cross-product Atlassian tools (unified search, status, whoami).
 */
public final class CrossProductHandler {

    private static final Logger LOGGER = Logger.getLogger(CrossProductHandler.class.getName());
    private static final Product P = Product.CROSS_PRODUCT;

    private final JiraClientV2 jira;
    private final ConfluenceClientV2 confluence;
    private final BitbucketClientV2 bitbucket;
    private final AuthManager authManager;

    public CrossProductHandler(final JiraClientV2 jira,
                               final ConfluenceClientV2 confluence,
                               final BitbucketClientV2 bitbucket,
                               final AuthManager authManager) {
        this.jira = Objects.requireNonNull(jira);
        this.confluence = Objects.requireNonNull(confluence);
        this.bitbucket = Objects.requireNonNull(bitbucket);
        this.authManager = Objects.requireNonNull(authManager);
    }

    public ToolResult handle(final String tool, final Map<String, String> args) {
        return switch (tool) {
            case "atlassian_unified_search" -> unifiedSearch(args);
            case "atlassian_status"         -> status();
            case "atlassian_whoami"         -> whoami();
            default -> ToolResult.error(P, tool, "Unknown cross-product tool: " + tool);
        };
    }

    /**
     * Searches Jira (via JQL) and Confluence (via CQL) simultaneously and
     * merges results into a combined JSON response.
     */
    private ToolResult unifiedSearch(final Map<String, String> args) {
        final var query = args.get("query");
        if (query == null || query.isBlank()) {
            return ToolResult.error(P, "atlassian_unified_search", "Missing required: 'query'");
        }

        final var sb = new StringBuilder(2048);
        sb.append("{\"query\":\"").append(HandlerUtils.escapeJson(query)).append("\"");

        // Jira search
        sb.append(",\"jira\":");
        try {
            final var jql = HandlerUtils.looksLikeJql(query) ? query : "text ~ \"" + HandlerUtils.escapeJql(query) + "\"";
            final var jiraResult = jira.searchIssues(jql, HandlerUtils.parseMaxResults(args.get("maxResults")));
            sb.append(jiraResult);
        } catch (IOException | InterruptedException ex) {
            LOGGER.log(Level.WARNING, "Jira search failed in unified search", ex);
            sb.append("{\"error\":\"").append(HandlerUtils.escapeJson(ex.getMessage())).append("\"}");
        }

        // Confluence search
        sb.append(",\"confluence\":");
        try {
            final var cql = HandlerUtils.looksLikeCql(query) ? query : "text ~ \"" + HandlerUtils.escapeJson(query) + "\"";
            final var confResult = confluence.searchPages(cql, HandlerUtils.parseMaxResults(args.get("maxResults")));
            sb.append(confResult);
        } catch (IOException | InterruptedException ex) {
            LOGGER.log(Level.WARNING, "Confluence search failed in unified search", ex);
            sb.append("{\"error\":\"").append(HandlerUtils.escapeJson(ex.getMessage())).append("\"}");
        }

        sb.append('}');
        return ToolResult.success(P, "atlassian_unified_search", sb.toString());
    }

    /**
     * Reports connectivity / authentication status for all configured products.
     */
    private ToolResult status() {
        final var sb = new StringBuilder(512);
        sb.append("{\"auth\":\"").append(HandlerUtils.escapeJson(authManager.statusDescription())).append("\"");

        // Jira
        sb.append(",\"jira\":");
        try {
            jira.searchIssues("created >= -1d", 1);
            sb.append("{\"connected\":true}");
        } catch (Exception ex) {
            sb.append("{\"connected\":false,\"error\":\"").append(HandlerUtils.escapeJson(ex.getMessage())).append("\"}");
        }

        // Confluence
        sb.append(",\"confluence\":");
        try {
            confluence.listSpaces();
            sb.append("{\"connected\":true}");
        } catch (Exception ex) {
            sb.append("{\"connected\":false,\"error\":\"").append(HandlerUtils.escapeJson(ex.getMessage())).append("\"}");
        }

        // Bitbucket
        sb.append(",\"bitbucket\":");
        try {
            bitbucket.getCurrentUser();
            sb.append("{\"connected\":true}");
        } catch (Exception ex) {
            sb.append("{\"connected\":false,\"error\":\"").append(HandlerUtils.escapeJson(ex.getMessage())).append("\"}");
        }

        sb.append('}');
        return ToolResult.success(P, "atlassian_status", sb.toString());
    }

    /**
     * Returns the current user across all configured products.
     */
    private ToolResult whoami() {
        final var sb = new StringBuilder(512);
        sb.append('{');

        // Jira
        sb.append("\"jira\":");
        try {
            sb.append(jira.getCurrentUser());
        } catch (Exception ex) {
            sb.append("{\"error\":\"").append(HandlerUtils.escapeJson(ex.getMessage())).append("\"}");
        }

        // Confluence
        sb.append(",\"confluence\":");
        try {
            sb.append(confluence.getCurrentUser());
        } catch (Exception ex) {
            sb.append("{\"error\":\"").append(HandlerUtils.escapeJson(ex.getMessage())).append("\"}");
        }

        // Bitbucket
        sb.append(",\"bitbucket\":");
        try {
            sb.append(bitbucket.getCurrentUser());
        } catch (Exception ex) {
            sb.append("{\"error\":\"").append(HandlerUtils.escapeJson(ex.getMessage())).append("\"}");
        }

        sb.append('}');
        return ToolResult.success(P, "atlassian_whoami", sb.toString());
    }
}
