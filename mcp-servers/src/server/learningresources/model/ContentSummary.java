package server.learningresources.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Holds the summarized content extracted from a learning resource URL.
 *
 * <p>Produced by the scraper pipeline: fetch → extract → summarize.
 * Contains both the raw extracted text and a condensed summary suitable
 * for quick consumption.
 *
 * @param resourceUrl    the URL that was scraped
 * @param pageTitle      the HTML {@code <title>} or heading
 * @param extractedText  the full meaningful text content (HTML stripped)
 * @param summary        a condensed summary of the extracted content
 * @param wordCount      approximate word count of the extracted text
 * @param readingTimeMinutes estimated reading time in minutes
 * @param difficulty     estimated difficulty level based on content analysis
 * @param scrapedAt      when the content was fetched
 */
public record ContentSummary(
        String resourceUrl,
        String pageTitle,
        String extractedText,
        String summary,
        int wordCount,
        int readingTimeMinutes,
        String difficulty,
        Instant scrapedAt
) {

    private static final int AVERAGE_WORDS_PER_MINUTE = 200;

    /**
     * Creates a {@link ContentSummary} with validation.
     *
     * @param resourceUrl        source URL
     * @param pageTitle          page title
     * @param extractedText      full text content
     * @param summary            condensed summary
     * @param wordCount          word count
     * @param readingTimeMinutes reading time estimate
     * @param difficulty         difficulty level
     * @param scrapedAt          scrape timestamp
     */
    public ContentSummary {
        Objects.requireNonNull(resourceUrl, "Resource URL must not be null");
        Objects.requireNonNull(pageTitle, "Page title must not be null");
        Objects.requireNonNull(extractedText, "Extracted text must not be null");
        Objects.requireNonNull(summary, "Summary must not be null");
        Objects.requireNonNull(difficulty, "Difficulty must not be null");
        Objects.requireNonNull(scrapedAt, "ScrapedAt timestamp must not be null");

        if (wordCount < 0) {
            throw new IllegalArgumentException("Word count cannot be negative: " + wordCount);
        }
        if (readingTimeMinutes < 0) {
            throw new IllegalArgumentException("Reading time cannot be negative: " + readingTimeMinutes);
        }
    }

    /**
     * Calculates reading time from a word count.
     *
     * @param wordCount the number of words
     * @return estimated minutes to read at average speed
     */
    public static int estimateReadingTime(final int wordCount) {
        return Math.max(1, (wordCount + AVERAGE_WORDS_PER_MINUTE - 1) / AVERAGE_WORDS_PER_MINUTE);
    }

    /**
     * Checks whether the extracted content is substantial enough to summarize.
     *
     * @return {@code true} if the content has at least 50 words
     */
    public boolean hasSubstantialContent() {
        final int minimumWordCount = 50;
        return wordCount >= minimumWordCount;
    }
}
