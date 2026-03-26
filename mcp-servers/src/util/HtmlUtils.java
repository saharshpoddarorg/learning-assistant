package util;

/**
 * Cross-cutting HTML text utilities for stripping tags and decoding entities.
 *
 * <p>Used by both {@code server.learningresources} (content scraping) and
 * {@code server.atlassian} (Confluence page content processing) to convert
 * raw HTML fragments into readable plain text.
 *
 * @see util.StringUtils
 */
public final class HtmlUtils
{

    private HtmlUtils()
    {
        // Utility class — no instances
    }

    /**
     * Decodes common HTML character entities to their literal equivalents.
     *
     * <p>Handles the most frequently encountered entities:
     * {@code &amp;}, {@code &lt;}, {@code &gt;}, {@code &quot;},
     * {@code &apos;}, {@code &nbsp;}, {@code &#34;}, and {@code &#39;}.
     *
     * @param text text potentially containing HTML entities;
     *             {@code null} returns an empty string
     * @return text with entities decoded
     */
    public static String decodeEntities(final String text)
    {
        if (text == null) return "";
        return text.replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&quot;", "\"")
                .replace("&#34;", "\"")
                .replace("&apos;", "'")
                .replace("&#39;", "'")
                .replace("&nbsp;", " ");
    }

    /**
     * Strips HTML tags from a string and decodes common entities.
     *
     * <p>Performs a simple regex-based tag removal ({@code <[^>]+>}), followed
     * by {@link #decodeEntities(String)} to convert remaining character
     * references. Does <b>not</b> normalize whitespace — callers should apply
     * their own whitespace rules after this method.
     *
     * @param html raw HTML content; {@code null} or blank returns an empty string
     * @return plain text with tags removed and entities decoded
     */
    public static String stripTags(final String html)
    {
        if (html == null || html.isBlank()) return "";
        return decodeEntities(html.replaceAll("<[^>]+>", ""));
    }
}
