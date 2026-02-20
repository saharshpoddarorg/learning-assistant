package server.learningresources.content;

import server.learningresources.model.ContentSummary;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * Presents scraped content in a readable, structured format.
 *
 * <p>Takes a {@link ContentSummary} and formats it for different output
 * contexts: full text reading, summary-only, or structured metadata display.
 * Acts as the "read this for me" capability of the learning assistant.
 */
public class ContentReader {

    private static final Logger LOGGER = Logger.getLogger(ContentReader.class.getName());

    private static final int PREVIEW_WORD_LIMIT = 300;
    private static final String SEPARATOR = "─".repeat(60);

    /**
     * Formats the content summary for full reading presentation.
     *
     * @param summary the content summary to present
     * @return formatted text with metadata header and full content
     */
    public String formatFullContent(final ContentSummary summary) {
        Objects.requireNonNull(summary, "ContentSummary must not be null");

        return new StringBuilder()
                .append(SEPARATOR).append("\n")
                .append("📖  ").append(summary.pageTitle()).append("\n")
                .append(SEPARATOR).append("\n")
                .append("URL:         ").append(summary.resourceUrl()).append("\n")
                .append("Words:       ").append(summary.wordCount()).append("\n")
                .append("Read time:   ~").append(summary.readingTimeMinutes()).append(" min\n")
                .append("Difficulty:  ").append(summary.difficulty()).append("\n")
                .append(SEPARATOR).append("\n\n")
                .append(summary.extractedText()).append("\n")
                .toString();
    }

    /**
     * Formats a summary-only view (no full content).
     *
     * @param summary the content summary
     * @return a compact summary with metadata
     */
    public String formatSummary(final ContentSummary summary) {
        Objects.requireNonNull(summary, "ContentSummary must not be null");

        return new StringBuilder()
                .append("📋  ").append(summary.pageTitle()).append("\n")
                .append("URL: ").append(summary.resourceUrl()).append("\n")
                .append("Difficulty: ").append(summary.difficulty())
                .append(" | ~").append(summary.readingTimeMinutes()).append(" min read")
                .append(" | ").append(summary.wordCount()).append(" words\n\n")
                .append("Summary:\n").append(summary.summary()).append("\n")
                .toString();
    }

    /**
     * Formats a short preview — first N words of the content.
     *
     * @param summary the content summary
     * @return a brief preview snippet
     */
    public String formatPreview(final ContentSummary summary) {
        Objects.requireNonNull(summary, "ContentSummary must not be null");

        final var words = summary.extractedText().split("\\s+");
        final var previewBuilder = new StringBuilder();
        final var limit = Math.min(PREVIEW_WORD_LIMIT, words.length);

        for (var index = 0; index < limit; index++) {
            if (!previewBuilder.isEmpty()) {
                previewBuilder.append(" ");
            }
            previewBuilder.append(words[index]);
        }

        if (words.length > PREVIEW_WORD_LIMIT) {
            previewBuilder.append("...");
        }

        return new StringBuilder()
                .append(summary.pageTitle()).append("\n")
                .append(previewBuilder).append("\n")
                .toString();
    }
}
