package server.atlassian.v2.handler;

import java.util.Map;

/**
 * Shared utilities for tool handlers.
 */
public final class HandlerUtils {

    private HandlerUtils() {}

    /** Parses maxResults with a default of 25. */
    public static int parseMaxResults(final String value) {
        return parseInt(value, 25);
    }

    /** Parses an integer with a fallback default. */
    public static int parseInt(final String value, final int defaultValue) {
        if (value == null || value.isBlank()) return defaultValue;
        try {
            return Integer.parseInt(value.strip());
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    /** Gets a required argument or returns null with an error message. */
    public static String requireArg(final Map<String, String> args, final String name) {
        final var value = args.get(name);
        return (value != null && !value.isBlank()) ? value.strip() : null;
    }

    /** Escapes a string for use inside JQL queries. */
    public static String escapeJql(final String query) {
        if (query == null) return "";
        return query.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    /** Checks if a query looks like raw JQL (vs. free text). */
    public static boolean looksLikeJql(final String query) {
        if (query == null) return false;
        final var lower = query.toLowerCase().strip();
        return lower.contains(" = ") || lower.contains(" in ") || lower.contains(" ~ ")
                || lower.startsWith("project") || lower.startsWith("assignee")
                || lower.startsWith("status") || lower.startsWith("type")
                || lower.startsWith("priority") || lower.contains("order by");
    }

    /** Checks if a query looks like raw CQL (vs. free text). */
    public static boolean looksLikeCql(final String query) {
        if (query == null) return false;
        final var lower = query.toLowerCase().strip();
        return lower.contains(" = ") || lower.contains(" in ") || lower.contains(" ~ ")
                || lower.startsWith("type") || lower.startsWith("space")
                || lower.startsWith("title") || lower.contains("order by");
    }

    /** Escapes a string for JSON embedding. */
    public static String escapeJson(final String raw) {
        if (raw == null) return "";
        return raw.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    }
}
