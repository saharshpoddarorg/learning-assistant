package server.atlassian.v1.handler;

import server.atlassian.common.JsonUtils;
import util.StringUtils;

/**
 * Shared utility methods for Atlassian MCP handler classes.
 *
 * <p>Consolidates common formatting and parsing helpers previously duplicated
 * across {@link JiraHandler}, {@link ConfluenceHandler}, and {@link BitbucketHandler}.
 *
 * <ul>
 *   <li>{@link #escapeJson(String)} — escape a string for safe JSON embedding</li>
 *   <li>{@link #truncate(String, int)} — truncate with ellipsis for display tables</li>
 *   <li>{@link #parseMaxResults(String, int)} — parse an int arg with a safe default</li>
 *   <li>{@link #looksLikeJql(String)} — heuristic: does the query look like raw JQL?</li>
 *   <li>{@link #looksLikeCql(String)} — heuristic: does the query look like raw CQL?</li>
 * </ul>
 */
final class HandlerUtils {

    /** Default number of results when the caller does not specify {@code maxResults}. */
    static final int DEFAULT_MAX_RESULTS = 25;

    private HandlerUtils() {
        // Utility class — no instances
    }

    /**
     * Escapes special characters so that a string can be safely embedded
     * inside a JSON string literal.
     *
     * @param text the raw string value; {@code null} returns an empty string
     * @return the JSON-safe escaped string
     */
    static String escapeJson(final String text) {
        return JsonUtils.escapeJson(text);
    }

    /**
     * Truncates {@code text} to {@code maxLength} characters, appending
     * {@code "..."} when the string is cut.
     *
     * @param text      the value to truncate; {@code null} returns an empty string
     * @param maxLength maximum number of characters to keep (must be ≥ 3)
     * @return the truncated string
     */
    static String truncate(final String text, final int maxLength) {
        return StringUtils.truncate(text, maxLength);
    }

    /**
     * Parses {@code value} as a positive integer for use as a result-count limit,
     * using {@link #DEFAULT_MAX_RESULTS} as the fallback.
     *
     * @param value the raw string argument (may be {@code null})
     * @return the parsed integer or {@link #DEFAULT_MAX_RESULTS}
     */
    static int parseMaxResults(final String value) {
        return parseMaxResults(value, DEFAULT_MAX_RESULTS);
    }

    /**
     * Parses {@code value} as a positive integer for use as a result-count limit.
     *
     * <p>Returns {@code defaultValue} when {@code value} is {@code null}, blank,
     * or not a valid integer.
     *
     * @param value        the raw string argument (may be {@code null})
     * @param defaultValue the fallback value
     * @return the parsed integer or the default
     */
    static int parseMaxResults(final String value, final int defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.strip());
        } catch (NumberFormatException ignored) {
            return defaultValue;
        }
    }

    /**
     * Heuristic: does the query string look like raw JQL (vs. free text)?
     *
     * @param query the user's search query
     * @return {@code true} if it appears to be JQL
     */
    static boolean looksLikeJql(final String query) {
        final var upper = query.toUpperCase();
        return upper.contains("=") || upper.contains("~")
                || upper.contains(" AND ") || upper.contains(" OR ")
                || upper.contains("ORDER BY") || upper.contains("PROJECT ")
                || upper.contains("STATUS ");
    }

    /**
     * Heuristic: does the query string look like raw CQL (vs. free text)?
     *
     * @param query the user's search query
     * @return {@code true} if it appears to be CQL
     */
    static boolean looksLikeCql(final String query) {
        final var upper = query.toUpperCase();
        return upper.contains("=") || upper.contains("~")
                || upper.contains(" AND ") || upper.contains(" OR ")
                || upper.contains("ORDER BY") || upper.contains("TYPE ")
                || upper.contains("SPACE ");
    }
}
