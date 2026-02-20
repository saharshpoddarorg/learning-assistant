package server.learningresources.scraper;

import java.util.Objects;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Extracts meaningful text content from raw HTML.
 *
 * <p>Uses regex-based HTML stripping to extract readable text from
 * a {@link ScraperResult}. Removes scripts, styles, navigation elements,
 * and HTML tags while preserving paragraph structure and meaningful whitespace.
 *
 * <p><strong>Note:</strong> This is a lightweight extractor suitable for
 * most documentation and article pages. For complex JavaScript-rendered
 * pages, consider integrating a headless browser or library like Jsoup.
 */
public class ContentExtractor {

    private static final Logger LOGGER = Logger.getLogger(ContentExtractor.class.getName());

    // Patterns for stripping HTML elements — compiled once for reuse
    private static final Pattern SCRIPT_PATTERN =
            Pattern.compile("<script[^>]*>.*?</script>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    private static final Pattern STYLE_PATTERN =
            Pattern.compile("<style[^>]*>.*?</style>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    private static final Pattern NAV_PATTERN =
            Pattern.compile("<(nav|header|footer)[^>]*>.*?</\\1>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    private static final Pattern TAG_PATTERN =
            Pattern.compile("<[^>]+>");
    private static final Pattern ENTITY_PATTERN =
            Pattern.compile("&[a-zA-Z]+;|&#\\d+;");
    private static final Pattern WHITESPACE_PATTERN =
            Pattern.compile("[ \\t]+");
    private static final Pattern BLANK_LINES_PATTERN =
            Pattern.compile("\\n{3,}");
    private static final Pattern TITLE_PATTERN =
            Pattern.compile("<title[^>]*>(.*?)</title>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    private static final Pattern H1_PATTERN =
            Pattern.compile("<h1[^>]*>(.*?)</h1>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

    /**
     * Extracts clean text content from a scraper result.
     *
     * @param result the raw HTML scraper result
     * @return extracted plain text, or empty string if extraction fails
     */
    public String extractText(final ScraperResult result) {
        Objects.requireNonNull(result, "ScraperResult must not be null");

        if (!result.isSuccessful()) {
            LOGGER.warning("Cannot extract text — HTTP " + result.statusCode() + " for " + result.url());
            return "";
        }

        return stripHtml(result.rawHtml());
    }

    /**
     * Extracts the page title from HTML.
     *
     * <p>Preference order: {@code <title>} tag, then first {@code <h1>} tag,
     * then falls back to "Untitled".
     *
     * @param result the scraper result
     * @return the extracted page title
     */
    public String extractTitle(final ScraperResult result) {
        Objects.requireNonNull(result, "ScraperResult must not be null");

        final var titleMatcher = TITLE_PATTERN.matcher(result.rawHtml());
        if (titleMatcher.find()) {
            return TAG_PATTERN.matcher(titleMatcher.group(1)).replaceAll("").trim();
        }

        final var h1Matcher = H1_PATTERN.matcher(result.rawHtml());
        if (h1Matcher.find()) {
            return TAG_PATTERN.matcher(h1Matcher.group(1)).replaceAll("").trim();
        }

        return "Untitled";
    }

    /**
     * Counts words in the given text.
     *
     * @param text the text to count words in
     * @return the approximate word count
     */
    public int countWords(final String text) {
        if (text == null || text.isBlank()) {
            return 0;
        }
        return text.trim().split("\\s+").length;
    }

    /**
     * Strips HTML tags, scripts, styles, and entities from raw HTML.
     *
     * @param html raw HTML content
     * @return clean plain text
     */
    private String stripHtml(final String html) {
        var cleaned = html;

        // Remove script and style blocks first (they contain non-content text)
        cleaned = SCRIPT_PATTERN.matcher(cleaned).replaceAll("");
        cleaned = STYLE_PATTERN.matcher(cleaned).replaceAll("");

        // Remove navigation, header, footer blocks
        cleaned = NAV_PATTERN.matcher(cleaned).replaceAll("");

        // Convert block elements to newlines for paragraph structure
        cleaned = cleaned.replaceAll("<(br|p|div|li|h[1-6])[^>]*>", "\n");
        cleaned = cleaned.replaceAll("</(p|div|li|h[1-6])>", "\n");

        // Strip remaining HTML tags
        cleaned = TAG_PATTERN.matcher(cleaned).replaceAll("");

        // Decode common HTML entities
        cleaned = decodeEntities(cleaned);

        // Normalize whitespace
        cleaned = WHITESPACE_PATTERN.matcher(cleaned).replaceAll(" ");
        cleaned = BLANK_LINES_PATTERN.matcher(cleaned).replaceAll("\n\n");

        return cleaned.trim();
    }

    /**
     * Decodes common HTML entities to their character equivalents.
     *
     * @param text text potentially containing HTML entities
     * @return text with entities decoded
     */
    private String decodeEntities(final String text) {
        var decoded = text;
        decoded = decoded.replace("&amp;", "&");
        decoded = decoded.replace("&lt;", "<");
        decoded = decoded.replace("&gt;", ">");
        decoded = decoded.replace("&quot;", "\"");
        decoded = decoded.replace("&apos;", "'");
        decoded = decoded.replace("&nbsp;", " ");
        decoded = decoded.replace("&#39;", "'");
        decoded = decoded.replace("&#34;", "\"");

        // Remove any remaining entities
        decoded = ENTITY_PATTERN.matcher(decoded).replaceAll("");

        return decoded;
    }
}
