package server.atlassian.v1.formatter;

import server.atlassian.v1.model.confluence.ConfluencePage;

import java.util.List;
import java.util.Objects;

/**
 * Formats Confluence pages into clean, readable text for AI assistant consumption.
 *
 * <p>Converts {@link ConfluencePage} records into structured text output
 * suitable for MCP tool responses, with HTML-to-text conversion for
 * storage-format page bodies.
 */
public class PageFormatter {

    /**
     * Formats a single Confluence page into a readable text block.
     *
     * @param page the Confluence page to format
     * @return the formatted text
     */
    public String formatPage(final ConfluencePage page) {
        Objects.requireNonNull(page, "Page must not be null");

        final var builder = new StringBuilder();
        builder.append("## ").append(page.title()).append("\n\n");
        builder.append("**Space:** ").append(page.spaceKey()).append("\n");
        builder.append("**Author:** ").append(page.authorName()).append("\n");
        builder.append("**Version:** ").append(page.version()).append("\n");
        builder.append("**Status:** ").append(page.status()).append("\n");

        if (!page.labels().isEmpty()) {
            builder.append("**Labels:** ").append(String.join(", ", page.labels())).append("\n");
        }

        builder.append("**Updated:** ").append(page.updated()).append("\n");

        if (!page.webUrl().isBlank()) {
            builder.append("**URL:** ").append(page.webUrl()).append("\n");
        }

        if (!page.body().isBlank()) {
            builder.append("\n### Content\n\n")
                    .append(stripHtmlTags(page.body())).append("\n");
        }

        return builder.toString();
    }

    /**
     * Formats a list of pages as a summary table.
     *
     * @param pages the list of pages
     * @param title the section title
     * @return the formatted text
     */
    public String formatPageList(final List<ConfluencePage> pages, final String title) {
        Objects.requireNonNull(pages, "Pages list must not be null");

        final var builder = new StringBuilder();
        builder.append("## ").append(title).append(" (").append(pages.size()).append(" results)\n\n");
        builder.append("| ID | Title | Space | Author | Updated |\n");
        builder.append("|----|-------|-------|--------|---------|\n");

        for (final ConfluencePage page : pages) {
            builder.append("| ").append(page.id())
                    .append(" | ").append(truncate(page.title(), 40))
                    .append(" | ").append(page.spaceKey())
                    .append(" | ").append(page.authorName())
                    .append(" | ").append(page.updated())
                    .append(" |\n");
        }

        return builder.toString();
    }

    /**
     * Basic HTML tag stripping — removes all tags and decodes common entities.
     *
     * <p>This is a simplified implementation. A production version would
     * use a proper HTML parser for reliable conversion.
     *
     * @param html the HTML content
     * @return the plain text content
     */
    private String stripHtmlTags(final String html) {
        return html.replaceAll("<[^>]+>", "")
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&quot;", "\"")
                .replace("&#39;", "'")
                .replace("&nbsp;", " ")
                .replaceAll("\\s+\n", "\n")
                .trim();
    }

    /**
     * Truncates text to a maximum length with ellipsis.
     */
    private String truncate(final String text, final int maxLength) {
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }
}
