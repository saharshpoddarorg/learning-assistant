package util;

/**
 * Cross-cutting string utilities used throughout the MCP server codebase.
 *
 * <p>This class provides common string operations that are needed by multiple
 * packages ({@code server.learningresources}, {@code server.atlassian}, etc.)
 * and therefore live at the root {@code util} level rather than inside any
 * single server package.
 *
 * @see util.HtmlUtils
 */
public final class StringUtils
{

    private StringUtils()
    {
        // Utility class — no instances
    }

    /**
     * Truncates {@code text} to {@code maxLength} characters, appending
     * {@code "..."} when the string is cut.
     *
     * @param text      the value to truncate; {@code null} returns an empty string
     * @param maxLength maximum number of characters to keep (must be &gt; 3)
     * @return the truncated string
     */
    public static String truncate(final String text, final int maxLength)
    {
        return truncate(text, maxLength, "...");
    }

    /**
     * Truncates {@code text} to {@code maxLength} characters, appending the
     * given {@code suffix} when the string is cut.
     *
     * @param text      the value to truncate; {@code null} returns an empty string
     * @param maxLength maximum number of characters to keep (must be &gt; suffix length)
     * @param suffix    the text to append when truncation occurs (e.g., {@code "..."})
     * @return the truncated string
     */
    public static String truncate(final String text, final int maxLength,
                                  final String suffix)
    {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - suffix.length()) + suffix;
    }
}
