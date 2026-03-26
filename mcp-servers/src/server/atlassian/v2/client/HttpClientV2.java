package server.atlassian.v2.client;

import server.atlassian.v2.auth.AuthException;
import server.atlassian.v2.auth.AuthManager;
import util.StringUtils;

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
 * Shared HTTP client for Atlassian REST APIs with auth-provider integration.
 *
 * <p>Unlike v1's {@code AtlassianRestClient}, this client obtains the
 * Authorization header from the {@link AuthManager} on each request,
 * supporting auto-refresh of OAuth tokens.
 */
public class HttpClientV2 {

    private static final Logger LOGGER = Logger.getLogger(HttpClientV2.class.getName());
    private static final String CONTENT_TYPE_JSON = "application/json";

    private final String baseUrl;
    private final AuthManager authManager;
    private final HttpClient httpClient;
    private final int timeoutSeconds;

    /**
     * Creates an HTTP client for a specific product base URL.
     *
     * @param baseUrl     the product base URL (e.g., {@code https://myco.atlassian.net})
     * @param authManager the auth manager for obtaining Authorization headers
     * @param timeoutMs   the HTTP read timeout in milliseconds
     */
    public HttpClientV2(final String baseUrl, final AuthManager authManager,
                        final int timeoutMs) {
        this.baseUrl = Objects.requireNonNull(baseUrl).replaceAll("/+$", "");
        this.authManager = Objects.requireNonNull(authManager);
        this.timeoutSeconds = Math.max(1, timeoutMs / 1000);
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(timeoutSeconds))
                .build();
    }

    /** Sends an authenticated GET request. */
    public String get(final String apiPath) throws IOException, InterruptedException {
        final var request = authorizedRequest(apiPath)
                .GET()
                .build();
        return execute(request);
    }

    /** Sends an authenticated POST request with a JSON body. */
    public String post(final String apiPath, final String jsonBody)
            throws IOException, InterruptedException {
        final var request = authorizedRequest(apiPath)
                .header("Content-Type", CONTENT_TYPE_JSON)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        return execute(request);
    }

    /** Sends an authenticated PUT request with a JSON body. */
    public String put(final String apiPath, final String jsonBody)
            throws IOException, InterruptedException {
        final var request = authorizedRequest(apiPath)
                .header("Content-Type", CONTENT_TYPE_JSON)
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        return execute(request);
    }

    /** Sends an authenticated DELETE request. */
    public String delete(final String apiPath) throws IOException, InterruptedException {
        final var request = authorizedRequest(apiPath)
                .DELETE()
                .build();
        return executeAllowNoContent(request);
    }

    /** Sends an authenticated PATCH request with a JSON body (uses POST with override header). */
    public String patch(final String apiPath, final String jsonBody)
            throws IOException, InterruptedException {
        // Java HttpClient doesn't have native PATCH; use method override
        final var request = authorizedRequest(apiPath)
                .header("Content-Type", CONTENT_TYPE_JSON)
                .header("X-HTTP-Method-Override", "PATCH")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        return execute(request);
    }

    // ── Internals ─────────────────────────────────────────────────────────────

    private HttpRequest.Builder authorizedRequest(final String apiPath) throws IOException {
        final var url = baseUrl + (apiPath.startsWith("/") ? apiPath : "/" + apiPath);
        try {
            final var authHeader = authManager.getAuthorizationHeader();
            return HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", CONTENT_TYPE_JSON)
                    .header("Authorization", authHeader)
                    .timeout(Duration.ofSeconds(timeoutSeconds));
        } catch (AuthException ex) {
            throw new IOException("Authentication failed: " + ex.getMessage(), ex);
        }
    }

    private String execute(final HttpRequest request) throws IOException, InterruptedException {
        final var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        final int status = response.statusCode();

        if (status >= 200 && status < 300) {
            return response.body();
        }

        LOGGER.log(Level.WARNING, "HTTP {0} from {1}: {2}",
                new Object[]{status, request.uri(), truncate(response.body())});

        throw new IOException("Atlassian API returned HTTP " + status
                + " for " + request.method() + " " + request.uri()
                + ": " + truncate(response.body()));
    }

    private String executeAllowNoContent(final HttpRequest request)
            throws IOException, InterruptedException {
        final var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        final int status = response.statusCode();

        if (status == 204 || (status >= 200 && status < 300)) {
            return response.body() != null ? response.body() : "";
        }

        throw new IOException("Atlassian API returned HTTP " + status
                + " for " + request.method() + " " + request.uri()
                + ": " + truncate(response.body()));
    }

    private static String truncate(final String body) {
        if (body == null) return "(empty)";
        return StringUtils.truncate(body, 500);
    }
}
