package server.learningresources.scraper;

import java.time.Instant;
import java.util.Objects;

/**
 * Raw output from a web scrape operation before content extraction.
 *
 * <p>Captures the HTTP response metadata alongside the raw HTML body.
 * This intermediate record is consumed by {@link ContentExtractor} to
 * produce clean text content.
 *
 * @param url            the URL that was fetched
 * @param statusCode     HTTP status code (e.g., 200, 404)
 * @param contentType    the Content-Type header value (e.g., "text/html; charset=utf-8")
 * @param rawHtml        the full HTML response body
 * @param fetchedAt      when the page was fetched
 * @param responseTimeMs the HTTP response time in milliseconds
 */
public record ScraperResult(
        String url,
        int statusCode,
        String contentType,
        String rawHtml,
        Instant fetchedAt,
        long responseTimeMs
) {

    /**
     * Creates a {@link ScraperResult} with validation.
     *
     * @param url            fetched URL
     * @param statusCode     HTTP status code
     * @param contentType    response content type
     * @param rawHtml        full HTML body
     * @param fetchedAt      fetch timestamp
     * @param responseTimeMs response duration
     */
    public ScraperResult {
        Objects.requireNonNull(url, "URL must not be null");
        Objects.requireNonNull(contentType, "Content type must not be null");
        Objects.requireNonNull(rawHtml, "Raw HTML must not be null");
        Objects.requireNonNull(fetchedAt, "FetchedAt timestamp must not be null");
    }

    /**
     * Checks whether the HTTP response was successful (2xx status).
     *
     * @return {@code true} if status code is in the 200–299 range
     */
    public boolean isSuccessful() {
        final int successRangeStart = 200;
        final int successRangeEnd = 299;
        return statusCode >= successRangeStart && statusCode <= successRangeEnd;
    }

    /**
     * Checks whether the response content appears to be HTML.
     *
     * @return {@code true} if the Content-Type contains "text/html"
     */
    public boolean isHtmlContent() {
        return contentType.toLowerCase().contains("text/html");
    }
}
