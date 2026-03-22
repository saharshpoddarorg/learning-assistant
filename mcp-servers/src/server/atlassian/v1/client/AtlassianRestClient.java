package server.atlassian.v1.client;

import server.atlassian.v1.model.ConnectionConfig;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Shared HTTP client for communicating with Atlassian REST APIs.
 *
 * <p>Provides authenticated GET, POST, and PUT operations with
 * consistent error handling, timeout management, and JSON content
 * type headers. Product-specific clients (Jira, Confluence, Bitbucket)
 * delegate their HTTP calls through this class.
 *
 * <p><strong>Thread Safety:</strong> This class is thread-safe.
 * The underlying {@link HttpClient} is shared across requests.
 */
public class AtlassianRestClient {

    private static final Logger LOGGER = Logger.getLogger(AtlassianRestClient.class.getName());
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String ACCEPT_HEADER = "Accept";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final ConnectionConfig config;
    private final HttpClient httpClient;

    /**
     * Creates a REST client with the given connection configuration.
     *
     * @param config the connection settings (base URL, credentials, timeout)
     */
    public AtlassianRestClient(final ConnectionConfig config) {
        this.config = Objects.requireNonNull(config, "ConnectionConfig must not be null");
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(config.timeoutSeconds()))
                .build();
    }

    /**
     * Sends an authenticated GET request to the specified API path.
     *
     * @param apiPath the REST API path (appended to the base URL)
     * @return the response body as a string
     * @throws IOException          if the request fails
     * @throws InterruptedException if the request is interrupted
     */
    public String get(final String apiPath) throws IOException, InterruptedException {
        final var url = config.buildUrl(apiPath);
        LOGGER.fine("GET " + url);

        final var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header(ACCEPT_HEADER, CONTENT_TYPE_JSON)
                .header(AUTHORIZATION_HEADER, config.credentials().toAuthorizationHeader())
                .timeout(Duration.ofSeconds(config.timeoutSeconds()))
                .GET()
                .build();

        return executeRequest(request);
    }

    /**
     * Sends an authenticated POST request with a JSON body.
     *
     * @param apiPath  the REST API path
     * @param jsonBody the JSON request body
     * @return the response body as a string
     * @throws IOException          if the request fails
     * @throws InterruptedException if the request is interrupted
     */
    public String post(final String apiPath, final String jsonBody)
            throws IOException, InterruptedException {
        final var url = config.buildUrl(apiPath);
        LOGGER.fine("POST " + url);

        final var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header(ACCEPT_HEADER, CONTENT_TYPE_JSON)
                .header(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON)
                .header(AUTHORIZATION_HEADER, config.credentials().toAuthorizationHeader())
                .timeout(Duration.ofSeconds(config.timeoutSeconds()))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return executeRequest(request);
    }

    /**
     * Sends an authenticated PUT request with a JSON body.
     *
     * @param apiPath  the REST API path
     * @param jsonBody the JSON request body
     * @return the response body as a string
     * @throws IOException          if the request fails
     * @throws InterruptedException if the request is interrupted
     */
    public String put(final String apiPath, final String jsonBody)
            throws IOException, InterruptedException {
        final var url = config.buildUrl(apiPath);
        LOGGER.fine("PUT " + url);

        final var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header(ACCEPT_HEADER, CONTENT_TYPE_JSON)
                .header(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON)
                .header(AUTHORIZATION_HEADER, config.credentials().toAuthorizationHeader())
                .timeout(Duration.ofSeconds(config.timeoutSeconds()))
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return executeRequest(request);
    }

    /**
     * Sends an authenticated DELETE request to the specified API path.
     *
     * @param apiPath the REST API path (appended to the base URL)
     * @return the response body as a string (usually empty for 204 No Content)
     * @throws IOException          if the request fails
     * @throws InterruptedException if the request is interrupted
     */
    public String delete(final String apiPath) throws IOException, InterruptedException {
        final var url = config.buildUrl(apiPath);
        LOGGER.fine("DELETE " + url);

        final var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header(ACCEPT_HEADER, CONTENT_TYPE_JSON)
                .header(AUTHORIZATION_HEADER, config.credentials().toAuthorizationHeader())
                .timeout(Duration.ofSeconds(config.timeoutSeconds()))
                .DELETE()
                .build();

        return executeDeleteRequest(request);
    }

    /**
     * Executes an HTTP request and returns the response body.
     *
     * @param request the HTTP request to execute
     * @return the response body string
     * @throws IOException if the response indicates an error (4xx/5xx)
     * @throws InterruptedException if the request is interrupted
     */
    private String executeRequest(final HttpRequest request)
            throws IOException, InterruptedException {
        final var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        final int statusCode = response.statusCode();

        if (statusCode >= 200 && statusCode < 300) {
            return response.body();
        }

        LOGGER.log(Level.WARNING, "HTTP {0} from {1}: {2}",
                new Object[]{statusCode, request.uri(), response.body()});

        throw new IOException("Atlassian API returned HTTP " + statusCode
                + " for " + request.method() + " " + request.uri()
                + ": " + truncateBody(response.body()));
    }

    /**
     * Executes a DELETE request, accepting 204 No Content as success.
     *
     * @param request the HTTP DELETE request
     * @return the response body string (empty string for 204)
     * @throws IOException          if the response is an error
     * @throws InterruptedException if the request is interrupted
     */
    private String executeDeleteRequest(final HttpRequest request)
            throws IOException, InterruptedException {
        final var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        final int statusCode = response.statusCode();

        if (statusCode == 204 || (statusCode >= 200 && statusCode < 300)) {
            return response.body() != null ? response.body() : "";
        }

        LOGGER.log(Level.WARNING, "HTTP {0} from {1}: {2}",
                new Object[]{statusCode, request.uri(), response.body()});

        throw new IOException("Atlassian API returned HTTP " + statusCode
                + " for DELETE " + request.uri()
                + ": " + truncateBody(response.body()));
    }

    /**
     * Truncates a response body for error logging — avoids flooding logs.
     *
     * @param body the response body
     * @return the truncated body (max 500 chars)
     */
    private String truncateBody(final String body) {
        final int maxLength = 500;
        if (body == null || body.length() <= maxLength) {
            return body;
        }
        return body.substring(0, maxLength) + "... (truncated)";
    }
}
