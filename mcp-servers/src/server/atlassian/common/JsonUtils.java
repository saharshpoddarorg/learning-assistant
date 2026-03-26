package server.atlassian.common;

/**
 * Shared JSON string-escaping utilities for the Atlassian MCP servers.
 *
 * <p>Consolidates the JSON escape logic previously duplicated in:
 * <ul>
 *   <li>{@code server.atlassian.v1.handler.HandlerUtils#escapeJson}</li>
 *   <li>{@code server.atlassian.v2.handler.HandlerUtils#escapeJson}</li>
 *   <li>{@code server.atlassian.v1.AtlassianServerV1#escapeJson} (private)</li>
 *   <li>{@code server.atlassian.v2.AtlassianServerV2#escapeJson} (private)</li>
 * </ul>
 *
 * <p>All methods are pure functions with no side effects.
 */
public final class JsonUtils {

    private JsonUtils() {
        // Utility class — no instances
    }

    /**
     * Escapes special characters so that a string can be safely embedded
     * inside a JSON string literal.
     *
     * <p>Handles the five characters that must be escaped in JSON strings:
     * backslash, double-quote, newline, carriage return, and tab.
     *
     * @param raw the raw string value; {@code null} returns an empty string
     * @return the JSON-safe escaped string
     */
    public static String escapeJson(final String raw) {
        if (raw == null) return "";
        return raw.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }

    /**
     * Escapes a string for use inside JQL or CQL queries.
     *
     * <p>Only escapes backslashes and double-quotes — the two characters
     * that need escaping in Atlassian query language string literals.
     *
     * @param query the raw query string; {@code null} returns an empty string
     * @return the query-safe escaped string
     */
    public static String escapeQueryLiteral(final String query) {
        if (query == null) return "";
        return query.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
