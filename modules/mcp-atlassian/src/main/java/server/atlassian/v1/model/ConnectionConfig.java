package server.atlassian.v1.model;

import java.util.Objects;

/**
 * Connection settings for an Atlassian product instance.
 *
 * <p>Encapsulates the base URL, credentials, and timeout configuration
 * needed to connect to a specific Atlassian Cloud or Data Center instance.
 *
 * @param baseUrl     the instance base URL (e.g., "https://mycompany.atlassian.net")
 * @param credentials the authentication credentials
 * @param timeoutSeconds  the HTTP request timeout in seconds
 */
public record ConnectionConfig(
        String baseUrl,
        AtlassianCredentials credentials,
        int timeoutSeconds
) {

    /** Default timeout for API requests (30 seconds). */
    public static final int DEFAULT_TIMEOUT_SECONDS = 30;

    /** Minimum allowed timeout. */
    private static final int MIN_TIMEOUT_SECONDS = 1;

    /** Maximum allowed timeout. */
    private static final int MAX_TIMEOUT_SECONDS = 300;

    /**
     * Creates a connection config with validation.
     *
     * @param baseUrl        instance base URL
     * @param credentials    authentication credentials
     * @param timeoutSeconds request timeout
     */
    public ConnectionConfig {
        Objects.requireNonNull(baseUrl, "Base URL must not be null");
        Objects.requireNonNull(credentials, "Credentials must not be null");

        if (baseUrl.isBlank()) {
            throw new IllegalArgumentException("Base URL must not be blank");
        }

        // Strip trailing slash for consistent URL construction
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }

        if (timeoutSeconds < MIN_TIMEOUT_SECONDS || timeoutSeconds > MAX_TIMEOUT_SECONDS) {
            throw new IllegalArgumentException(
                    "Timeout must be between " + MIN_TIMEOUT_SECONDS
                            + " and " + MAX_TIMEOUT_SECONDS + " seconds, got: " + timeoutSeconds);
        }
    }

    /**
     * Creates a connection config with the default timeout.
     *
     * @param baseUrl     the instance base URL
     * @param credentials the authentication credentials
     * @return a new connection config with default timeout
     */
    public static ConnectionConfig withDefaults(final String baseUrl,
                                                final AtlassianCredentials credentials) {
        return new ConnectionConfig(baseUrl, credentials, DEFAULT_TIMEOUT_SECONDS);
    }

    /**
     * Creates a connection config with a timeout specified in milliseconds.
     * The value is converted to whole seconds (minimum 1).
     *
     * @param baseUrl      the instance base URL
     * @param credentials  the authentication credentials
     * @param timeoutMs    the HTTP read timeout in milliseconds
     * @return a new connection config
     */
    public static ConnectionConfig withTimeoutMs(final String baseUrl,
                                                 final AtlassianCredentials credentials,
                                                 final int timeoutMs) {
        return new ConnectionConfig(baseUrl, credentials, Math.max(1, timeoutMs / 1_000));
    }

    /**
     * Builds a full API URL by appending the given path to the base URL.
     *
     * @param apiPath the REST API path (e.g., "/rest/api/3/issue/PROJ-123")
     * @return the full URL
     */
    public String buildUrl(final String apiPath) {
        Objects.requireNonNull(apiPath, "API path must not be null");
        final var path = apiPath.startsWith("/") ? apiPath : "/" + apiPath;
        return baseUrl + path;
    }
}
