package server.learningresources.handler;

import server.learningresources.content.ContentReader;
import server.learningresources.content.ContentSummarizer;
import server.learningresources.model.ContentSummary;
import server.learningresources.scraper.ScraperException;
import server.learningresources.scraper.WebScraper;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles web scraping tool calls — fetches URLs, extracts content, and summarizes.
 *
 * <p>The scrape pipeline: fetch → extract → summarize → format.
 * Supports full content reading, summary-only view, and preview.
 *
 * <p><strong>MCP Tool:</strong> {@code scrape_and_summarize}
 */
public class ScrapeHandler {

    private static final Logger LOGGER = Logger.getLogger(ScrapeHandler.class.getName());

    private final WebScraper scraper;
    private final ContentSummarizer summarizer;
    private final ContentReader reader;

    /**
     * Creates a {@link ScrapeHandler} with default dependencies.
     */
    public ScrapeHandler() {
        this(new WebScraper(), new ContentSummarizer(), new ContentReader());
    }

    /**
     * Creates a {@link ScrapeHandler} with custom dependencies.
     *
     * @param scraper    the web scraper for fetching pages
     * @param summarizer the content summarizer
     * @param reader     the content formatter/reader
     */
    public ScrapeHandler(final WebScraper scraper,
                         final ContentSummarizer summarizer,
                         final ContentReader reader) {
        this.scraper = Objects.requireNonNull(scraper, "WebScraper must not be null");
        this.summarizer = Objects.requireNonNull(summarizer, "ContentSummarizer must not be null");
        this.reader = Objects.requireNonNull(reader, "ContentReader must not be null");
    }

    /**
     * Scrapes a URL and returns a full content summary.
     *
     * @param url the URL to scrape
     * @return the content summary
     * @throws ScraperException if the scrape fails
     */
    public ContentSummary scrapeAndSummarize(final String url) {
        Objects.requireNonNull(url, "URL must not be null");
        LOGGER.info("Scraping: " + url);

        final var result = scraper.fetch(url);

        if (!result.isSuccessful()) {
            throw new ScraperException("HTTP " + result.statusCode() + " fetching " + url);
        }

        return summarizer.summarize(result);
    }

    /**
     * Scrapes a URL and returns formatted summary text.
     *
     * @param url the URL to scrape and summarize
     * @return formatted summary string
     */
    public String scrapeAndFormatSummary(final String url) {
        try {
            final var summary = scrapeAndSummarize(url);
            return reader.formatSummary(summary);
        } catch (ScraperException scraperException) {
            LOGGER.log(Level.WARNING, "Scrape failed for " + url, scraperException);
            return "Error scraping " + url + ": " + scraperException.getMessage();
        }
    }

    /**
     * Scrapes a URL and returns the full formatted content.
     *
     * @param url the URL to scrape and read
     * @return formatted full content string
     */
    public String scrapeAndFormatFullContent(final String url) {
        try {
            final var summary = scrapeAndSummarize(url);
            return reader.formatFullContent(summary);
        } catch (ScraperException scraperException) {
            LOGGER.log(Level.WARNING, "Scrape failed for " + url, scraperException);
            return "Error scraping " + url + ": " + scraperException.getMessage();
        }
    }

    /**
     * Scrapes a URL and returns a short preview.
     *
     * @param url the URL to scrape and preview
     * @return formatted preview string
     */
    public String scrapeAndFormatPreview(final String url) {
        try {
            final var summary = scrapeAndSummarize(url);
            return reader.formatPreview(summary);
        } catch (ScraperException scraperException) {
            LOGGER.log(Level.WARNING, "Scrape failed for " + url, scraperException);
            return "Error scraping " + url + ": " + scraperException.getMessage();
        }
    }
}
