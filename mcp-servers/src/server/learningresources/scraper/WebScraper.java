package server.learningresources.scraper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Fetches web pages via HTTP for content extraction.
 *
 * <p>Uses Java's built-in {@link HttpClient} to make GET requests with
 * sensible defaults (timeouts, user-agent, redirect following). The raw
 * HTML response is captured in a {@link ScraperResult} for downstream processing.
 *
 * <p><strong>Usage:</strong>
 * <pre>
 *   var scraper = new WebScraper();
 *   var result = scraper.fetch("https://docs.oracle.com/en/java/");
 *   if (result.isSuccessful()) {
 *       // pass to ContentExtractor
 *   }
 * </pre>
 */
public class WebScraper {

    private static final Logger LOGGER = Logger.getLogger(WebScraper.class.getName());

    private static final Duration DEFAULT_CONNECT_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration DEFAULT_REQUEST_TIMEOUT = Duration.ofSeconds(30);
    private static final String USER_AGENT = "MCP-LearningResources/1.0 (Java HttpClient)";

    private final HttpClient httpClient;
    private final Duration requestTimeout;

    /**
     * Creates a {@link WebScraper} with default settings.
     */
    public WebScraper() {
        this(DEFAULT_REQUEST_TIMEOUT);
    }

    /**
     * Creates a {@link WebScraper} with a custom request timeout.
     *
     * @param requestTimeout maximum time to wait for a response
     */
    public WebScraper(final Duration requestTimeout) {
        this.requestTimeout = Objects.requireNonNull(requestTimeout, "Request timeout must not be null");
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    /**
     * Fetches the HTML content of the given URL.
     *
     * @param url the URL to fetch
     * @return a {@link ScraperResult} containing the response data
     * @throws ScraperException if the request fails due to I/O or invalid URL
     */
    public ScraperResult fetch(final String url) {
        Objects.requireNonNull(url, "URL must not be null");

        LOGGER.fine("Fetching: " + url);
        final var startTime = Instant.now();

        try {
            final var request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", USER_AGENT)
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .timeout(requestTimeout)
                    .GET()
                    .build();

            final var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            final var endTime = Instant.now();
            final var responseTimeMs = Duration.between(startTime, endTime).toMillis();

            final var contentType = response.headers()
                    .firstValue("Content-Type")
                    .orElse("unknown");

            LOGGER.fine("Fetched " + url + " — status " + response.statusCode()
                    + " in " + responseTimeMs + "ms");

            return new ScraperResult(url, response.statusCode(), contentType,
                    response.body(), Instant.now(), responseTimeMs);

        } catch (IOException ioException) {
            LOGGER.log(Level.WARNING, "Failed to fetch " + url, ioException);
            throw new ScraperException("I/O error fetching " + url + ": " + ioException.getMessage(),
                    ioException);
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            throw new ScraperException("Request interrupted for " + url, interruptedException);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new ScraperException("Invalid URL: " + url, illegalArgumentException);
        }
    }
}
