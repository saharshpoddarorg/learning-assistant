package server.atlassian.v1.handler;

import server.atlassian.v1.client.BitbucketClient;
import server.atlassian.v1.client.ConfluenceClient;
import server.atlassian.v1.client.JiraClient;
import server.atlassian.v1.model.AtlassianProduct;
import server.atlassian.v1.model.ToolResponse;
import server.atlassian.common.JsonExtractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the {@code atlassian_unified_search} tool — a cross-product
 * search that queries Jira, Confluence, and optionally Bitbucket in
 * parallel and merges results into a single AI-readable response.
 *
 * <p>The unified search is the "killer feature" of having a single MCP
 * server for all Atlassian products: one query surfaces related issues,
 * documentation, and code all at once.
 *
 * <p><strong>Example queries:</strong>
 * <ul>
 *   <li>{@code "login timeout"} — finds Jira bugs, Confluence runbooks, and code</li>
 *   <li>{@code "authentication flow"} — finds design docs, related tickets, PRs</li>
 *   <li>{@code "PROJ-123"} — finds the issue plus all linked pages mentioning it</li>
 * </ul>
 *
 * <p><strong>Arguments:</strong>
 * <ul>
 *   <li>{@code query} (required) — the search term</li>
 *   <li>{@code maxResults} (optional) — results per product (default 10)</li>
 *   <li>{@code products} (optional) — comma-separated list to include: jira, confluence, bitbucket
 *       (default: jira,confluence)</li>
 * </ul>
 */
public class UnifiedSearchHandler {

    private static final Logger LOGGER = Logger.getLogger(UnifiedSearchHandler.class.getName());
    private static final int DEFAULT_MAX_RESULTS = 10;

    private final JiraClient jiraClient;
    private final ConfluenceClient confluenceClient;
    private final BitbucketClient bitbucketClient;

    /**
     * Creates a unified search handler with all three product clients.
     *
     * @param jiraClient        the Jira REST client (may be null if Jira is disabled)
     * @param confluenceClient  the Confluence REST client (may be null if disabled)
     * @param bitbucketClient   the Bitbucket REST client (may be null if disabled)
     */
    public UnifiedSearchHandler(final JiraClient jiraClient,
                                 final ConfluenceClient confluenceClient,
                                 final BitbucketClient bitbucketClient) {
        this.jiraClient       = jiraClient;
        this.confluenceClient = confluenceClient;
        this.bitbucketClient  = bitbucketClient;
    }

    /**
     * Executes a cross-product search across Jira, Confluence, and Bitbucket.
     *
     * @param arguments the tool arguments ({@code query}, optional {@code maxResults},
     *                  optional {@code products})
     * @return the tool response with merged, formatted results
     */
    public ToolResponse search(final Map<String, String> arguments) {
        Objects.requireNonNull(arguments, "Arguments must not be null");

        final var query = arguments.get("query");
        if (query == null || query.isBlank()) {
            return ToolResponse.error(AtlassianProduct.JIRA, "atlassian_unified_search",
                    "Missing required argument: 'query'");
        }

        final int maxResults = parseMaxResults(arguments.get("maxResults"));
        final var productsRaw = arguments.getOrDefault("products", "jira,confluence");
        final var products = List.of(productsRaw.toLowerCase().split(",\\s*"));

        LOGGER.info("Unified search for: \"" + query + "\" across " + productsRaw);

        final var sections = new ArrayList<String>();
        int totalHits = 0;

        // ── Jira search ──────────────────────────────────────────────────────
        if (products.contains("jira") && jiraClient != null) {
            try {
                final var jql = looksLikeJql(query)
                        ? query
                        : "text ~ \"" + escapeJql(query) + "\" ORDER BY updated DESC";
                final var jiraJson = jiraClient.searchIssues(jql, maxResults);
                final var jiraSection = formatJiraSection(jiraJson);
                if (!jiraSection.isBlank()) {
                    sections.add(jiraSection);
                    totalHits += JsonExtractor.intValue(jiraJson, "total", 0);
                }
            } catch (IOException | InterruptedException exception) {
                LOGGER.log(Level.WARNING, "Jira search error during unified search", exception);
                sections.add("### Jira\n_Search failed: " + exception.getMessage() + "_\n");
            }
        }

        // ── Confluence search ─────────────────────────────────────────────────
        if (products.contains("confluence") && confluenceClient != null) {
            try {
                final var cql = "type = page AND text ~ \"" + escapeCql(query)
                        + "\" ORDER BY lastModified DESC";
                final var confJson = confluenceClient.searchPages(cql, maxResults);
                final var confSection = formatConfluenceSection(confJson);
                if (!confSection.isBlank()) {
                    sections.add(confSection);
                    totalHits += JsonExtractor.intValue(confJson, "totalSize",
                            JsonExtractor.intValue(confJson, "size", 0));
                }
            } catch (IOException | InterruptedException exception) {
                LOGGER.log(Level.WARNING, "Confluence search error during unified search", exception);
                sections.add("### Confluence\n_Search failed: " + exception.getMessage() + "_\n");
            }
        }

        // ── Bitbucket code search ─────────────────────────────────────────────
        if (products.contains("bitbucket") && bitbucketClient != null) {
            final var workspace = arguments.get("workspace");
            if (workspace != null && !workspace.isBlank()) {
                try {
                    final var bbJson = bitbucketClient.searchCode(workspace, query);
                    final var bbSection = formatBitbucketSection(bbJson);
                    if (!bbSection.isBlank()) {
                        sections.add(bbSection);
                    }
                } catch (IOException | InterruptedException exception) {
                    LOGGER.log(Level.WARNING, "Bitbucket search error during unified search", exception);
                    sections.add("### Bitbucket Code\n_Search failed: " + exception.getMessage() + "_\n");
                }
            }
        }

        if (sections.isEmpty()) {
            return ToolResponse.success(AtlassianProduct.JIRA, "atlassian_unified_search",
                    "No results found for: \"" + query + "\"");
        }

        final var sb = new StringBuilder();
        sb.append("# Unified Search Results\n");
        sb.append("**Query:** \"").append(query).append("\"\n");
        sb.append("**Products searched:** ").append(productsRaw).append("\n");
        sb.append("**Total hits:** ").append(totalHits).append("\n\n");
        sb.append("---\n\n");
        sections.forEach(section -> sb.append(section).append("\n"));

        return ToolResponse.successWithCount(AtlassianProduct.JIRA, "atlassian_unified_search",
                sb.toString(), totalHits);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Section formatters
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Formats Jira search results as a markdown section.
     */
    private String formatJiraSection(final String json) {
        final int total = JsonExtractor.intValue(json, "total", 0);
        final var issues = JsonExtractor.arrayBlocks(json, "issues");

        if (issues.isEmpty()) return "";

        final var sb = new StringBuilder();
        sb.append("### Jira Issues (").append(total).append(" found)\n\n");
        sb.append("| Key | Summary | Status | Type |\n");
        sb.append("|-----|---------|--------|------|\n");

        for (final var issue : issues) {
            final var key = JsonExtractor.stringOrDefault(issue, "key", "?");
            final var fields = JsonExtractor.block(issue, "fields").orElse("{}");
            final var summary = truncate(JsonExtractor.stringOrDefault(fields, "summary", ""), 55);
            final var status  = JsonExtractor.navigate(fields, "status", "name").orElse("-");
            final var type    = JsonExtractor.navigate(fields, "issuetype", "name").orElse("-");
            sb.append("| ").append(key)
              .append(" | ").append(summary)
              .append(" | ").append(status)
              .append(" | ").append(type)
              .append(" |\n");
        }
        sb.append("\n");
        return sb.toString();
    }

    /**
     * Formats Confluence search results as a markdown section.
     */
    private String formatConfluenceSection(final String json) {
        final var results = JsonExtractor.arrayBlocks(json, "results");
        if (results.isEmpty()) return "";

        final var sb = new StringBuilder();
        sb.append("### Confluence Pages (").append(results.size()).append(" found)\n\n");

        for (final var page : results) {
            final var title    = JsonExtractor.stringOrDefault(page, "title", "?");
            final var spaceKey = JsonExtractor.navigate(page, "space", "key").orElse("-");
            final var updated  = JsonExtractor.navigate(page, "version", "when")
                                 .or(() -> JsonExtractor.string(page, "lastModifiedDate"))
                                 .orElse("-");
            final var excerpt  = truncate(
                    JsonExtractor.navigate(page, "excerpt").or(
                    () -> JsonExtractor.string(page, "bodyText")).orElse(""), 100);
            sb.append("**").append(title).append("** [").append(spaceKey).append("] — ").append(updated).append("\n");
            if (!excerpt.isBlank()) sb.append("> ").append(excerpt).append("\n");
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Formats Bitbucket code search results as a markdown section.
     */
    private String formatBitbucketSection(final String json) {
        final var values = JsonExtractor.arrayBlocks(json, "values");
        if (values.isEmpty()) return "";

        final var sb = new StringBuilder();
        sb.append("### Bitbucket Code (").append(values.size()).append(" matches)\n\n");

        for (final var match : values) {
            final var file = JsonExtractor.navigate(match, "file", "path").orElse(
                             JsonExtractor.stringOrDefault(match, "path", "?"));
            final var repo = JsonExtractor.navigate(match, "file", "commit", "repository", "name").orElse("-");
            sb.append("- `").append(file).append("` in **").append(repo).append("**\n");
        }
        sb.append("\n");
        return sb.toString();
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Shared helpers
    // ──────────────────────────────────────────────────────────────────────────

    private boolean looksLikeJql(final String query) {
        final var upper = query.toUpperCase();
        return upper.contains("=") || upper.contains("~")
                || upper.contains(" AND ") || upper.contains(" OR ")
                || upper.contains("ORDER BY") || upper.contains("PROJECT ")
                || upper.contains("STATUS ");
    }

    private String escapeJql(final String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String escapeCql(final String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String truncate(final String text, final int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }

    private int parseMaxResults(final String value) {
        if (value == null || value.isBlank()) return DEFAULT_MAX_RESULTS;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ignored) {
            return DEFAULT_MAX_RESULTS;
        }
    }
}
