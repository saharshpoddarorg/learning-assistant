package server.learningresources.scraper;

/**
 * Thrown when a web scraping operation fails.
 *
 * <p>Covers HTTP errors, I/O failures, timeouts, and invalid URLs
 * encountered during the fetch phase of the scraper pipeline.
 */
public class ScraperException extends RuntimeException {

    /**
     * Creates a new scraper exception with a descriptive message.
     *
     * @param message description of what went wrong during scraping
     */
    public ScraperException(final String message) {
        super(message);
    }

    /**
     * Creates a new scraper exception with a message and root cause.
     *
     * @param message description of what went wrong
     * @param cause   the underlying exception
     */
    public ScraperException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
