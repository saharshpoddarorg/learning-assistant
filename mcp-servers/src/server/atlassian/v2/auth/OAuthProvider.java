package server.atlassian.v2.auth;

import server.atlassian.v2.auth.CredentialStore.StoredTokens;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * OAuth 2.0 browser-based authentication provider.
 *
 * <p>This is <strong>Option A</strong> — the user logs in through their browser,
 * and the server captures tokens via a localhost callback without disturbing
 * existing browser tabs.
 *
 * <h3>Flow</h3>
 * <ol>
 *   <li>User calls {@code auth_login_browser} tool</li>
 *   <li>Server starts a localhost callback listener</li>
 *   <li>Server opens the browser to the Atlassian OAuth consent page</li>
 *   <li>User approves access in the browser</li>
 *   <li>Atlassian redirects to {@code http://localhost:PORT/callback}</li>
 *   <li>Server exchanges the auth code for access and refresh tokens</li>
 *   <li>Tokens are stored locally via {@link CredentialStore}</li>
 *   <li>Subsequent API calls use the stored access token</li>
 *   <li>When the access token expires, the refresh token is used automatically</li>
 * </ol>
 *
 * <h3>Prerequisites</h3>
 * <p>The user must register an OAuth 2.0 (3LO) app at
 * <a href="https://developer.atlassian.com/console/myapps/">developer.atlassian.com</a>
 * and provide the client ID and secret in the config file.
 *
 * <h3>Atlassian OAuth Endpoints</h3>
 * <ul>
 *   <li>Authorize: {@code https://auth.atlassian.com/authorize}</li>
 *   <li>Token: {@code https://auth.atlassian.com/oauth/token}</li>
 *   <li>Accessible resources: {@code https://api.atlassian.com/oauth/token/accessible-resources}</li>
 * </ul>
 */
public final class OAuthProvider implements AuthProvider {

    private static final Logger LOGGER = Logger.getLogger(OAuthProvider.class.getName());

    private static final String ATLASSIAN_AUTH_URL = "https://auth.atlassian.com/authorize";
    private static final String ATLASSIAN_TOKEN_URL = "https://auth.atlassian.com/oauth/token";
    private static final String ATLASSIAN_RESOURCES_URL =
            "https://api.atlassian.com/oauth/token/accessible-resources";

    private static final String DEFAULT_SCOPES = String.join(" ",
            "read:jira-work", "write:jira-work", "read:jira-user",
            "read:confluence-space.summary", "read:confluence-content.all",
            "write:confluence-content", "read:confluence-user",
            "offline_access"
    );

    private static final int CALLBACK_PORT = 0; // auto-assign available port

    private final String clientId;
    private final String clientSecret;
    private final String scopes;
    private final CredentialStore credentialStore;
    private final HttpClient httpClient;

    private String cachedAccessToken;
    private Instant cachedExpiresAt;

    /**
     * Creates an OAuth provider.
     *
     * @param clientId        the OAuth app client ID
     * @param clientSecret    the OAuth app client secret
     * @param credentialStore the store for persisting tokens
     */
    public OAuthProvider(final String clientId, final String clientSecret,
                         final CredentialStore credentialStore) {
        this(clientId, clientSecret, DEFAULT_SCOPES, credentialStore);
    }

    /**
     * Creates an OAuth provider with custom scopes.
     */
    public OAuthProvider(final String clientId, final String clientSecret,
                         final String scopes, final CredentialStore credentialStore) {
        this.clientId = Objects.requireNonNull(clientId, "Client ID must not be null");
        this.clientSecret = Objects.requireNonNull(clientSecret, "Client secret must not be null");
        this.scopes = scopes != null ? scopes : DEFAULT_SCOPES;
        this.credentialStore = Objects.requireNonNull(credentialStore);
        this.httpClient = HttpClient.newBuilder().build();

        // Try to load existing tokens from store
        loadCachedTokens();
    }

    @Override
    public String getAuthorizationHeader() throws AuthException {
        if (cachedAccessToken == null || isTokenExpired()) {
            if (!tryRefreshToken()) {
                throw new AuthException(
                        "Not authenticated. Run the 'auth_login_browser' tool to log in via browser.");
            }
        }
        return "Bearer " + cachedAccessToken;
    }

    @Override
    public boolean isAuthenticated() {
        if (cachedAccessToken != null && !isTokenExpired()) {
            return true;
        }
        // Try refresh
        return tryRefreshToken();
    }

    @Override
    public String statusDescription() {
        if (cachedAccessToken == null) {
            return "Not authenticated — run auth_login_browser to log in";
        }
        if (isTokenExpired()) {
            return "Access token expired — attempting refresh";
        }
        final long minutesLeft = java.time.Duration.between(Instant.now(), cachedExpiresAt).toMinutes();
        return "OAuth authenticated — token expires in " + minutesLeft + " min";
    }

    @Override
    public void clearCredentials() {
        cachedAccessToken = null;
        cachedExpiresAt = null;
        credentialStore.clear();
    }

    /**
     * Initiates the browser-based OAuth login flow.
     *
     * <p>Opens the user's default browser to the Atlassian consent page and
     * waits for the callback. This method blocks until login completes or times out.
     *
     * @return a status message describing the result
     * @throws AuthException if the login flow fails
     */
    public String loginViaBrowser() throws AuthException {
        LOGGER.info("Starting browser-based OAuth login flow...");

        final var callbackServer = new OAuthCallbackServer(CALLBACK_PORT);
        try {
            // Build the authorization URL
            final var callbackUrl = "http://localhost:" + CALLBACK_PORT + "/callback";
            // Start the callback server first to get the actual port
            // We need to start the flow in a specific order:
            // 1. Start callback server (to know the port)
            // 2. Open browser with the correct redirect URI
            // 3. Wait for callback

            // Use a temporary server to get the port, then build URL
            final var tempServer = new OAuthCallbackServer(CALLBACK_PORT);
            final var redirectUri = tempServer.getCallbackUrl();

            final var authUrl = ATLASSIAN_AUTH_URL
                    + "?audience=api.atlassian.com"
                    + "&client_id=" + urlEncode(clientId)
                    + "&scope=" + urlEncode(scopes)
                    + "&redirect_uri=" + urlEncode(redirectUri)
                    + "&response_type=code"
                    + "&prompt=consent";

            LOGGER.info("Opening browser for OAuth consent: " + authUrl);

            // Open browser — this should not disturb existing tabs
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(URI.create(authUrl));
            } else {
                // Fallback: try OS-specific command
                openBrowserFallback(authUrl);
            }

            // Wait for the authorization code
            final var code = tempServer.awaitAuthorizationCode();
            LOGGER.info("Authorization code received, exchanging for tokens...");

            // Exchange code for tokens
            exchangeCodeForTokens(code, redirectUri);

            return "Successfully authenticated via browser. OAuth tokens stored locally.";

        } catch (IOException ex) {
            throw new AuthException("Failed to start OAuth callback server: " + ex.getMessage(), ex);
        }
    }

    // ── Token Exchange & Refresh ──────────────────────────────────────────────

    private void exchangeCodeForTokens(final String code, final String redirectUri)
            throws AuthException {
        final var body = "grant_type=authorization_code"
                + "&client_id=" + urlEncode(clientId)
                + "&client_secret=" + urlEncode(clientSecret)
                + "&code=" + urlEncode(code)
                + "&redirect_uri=" + urlEncode(redirectUri);

        try {
            final var request = HttpRequest.newBuilder()
                    .uri(URI.create(ATLASSIAN_TOKEN_URL))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            final var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new AuthException("Token exchange failed (HTTP " + response.statusCode()
                        + "): " + response.body());
            }

            parseAndStoreTokens(response.body());

        } catch (IOException | InterruptedException ex) {
            throw new AuthException("Token exchange failed: " + ex.getMessage(), ex);
        }
    }

    private boolean tryRefreshToken() {
        final var stored = credentialStore.loadTokens();
        if (stored == null || !stored.hasRefreshToken()) return false;

        // If stored token is still valid, use it
        if (!stored.isExpired()) {
            cachedAccessToken = stored.accessToken();
            cachedExpiresAt = stored.expiresAt();
            return true;
        }

        // Try to refresh
        LOGGER.info("Access token expired, attempting refresh...");
        final var body = "grant_type=refresh_token"
                + "&client_id=" + urlEncode(clientId)
                + "&client_secret=" + urlEncode(clientSecret)
                + "&refresh_token=" + urlEncode(stored.refreshToken());

        try {
            final var request = HttpRequest.newBuilder()
                    .uri(URI.create(ATLASSIAN_TOKEN_URL))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            final var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                LOGGER.warning("Token refresh failed (HTTP " + response.statusCode() + ")");
                return false;
            }

            parseAndStoreTokens(response.body());
            return true;

        } catch (IOException | InterruptedException | AuthException ex) {
            LOGGER.log(Level.WARNING, "Token refresh failed", ex);
            return false;
        }
    }

    private void parseAndStoreTokens(final String jsonResponse) throws AuthException {
        // Lightweight JSON parsing (no external dependencies)
        final var accessToken = extractJsonString(jsonResponse, "access_token");
        final var refreshToken = extractJsonString(jsonResponse, "refresh_token");
        final var expiresIn = extractJsonNumber(jsonResponse, "expires_in", 3600);
        final var scope = extractJsonString(jsonResponse, "scope");

        if (accessToken == null || accessToken.isBlank()) {
            throw new AuthException("No access_token in token response");
        }

        cachedAccessToken = accessToken;
        cachedExpiresAt = Instant.now().plusSeconds(expiresIn);

        try {
            credentialStore.saveTokens(accessToken, refreshToken, cachedExpiresAt, scope);
        } catch (IOException ex) {
            LOGGER.warning("Failed to persist tokens: " + ex.getMessage());
            // Tokens are still usable in memory even if persistence fails
        }
    }

    private void loadCachedTokens() {
        final var stored = credentialStore.loadTokens();
        if (stored != null) {
            cachedAccessToken = stored.accessToken();
            cachedExpiresAt = stored.expiresAt();
        }
    }

    private boolean isTokenExpired() {
        return cachedExpiresAt == null || Instant.now().isAfter(cachedExpiresAt.minusSeconds(60));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static String urlEncode(final String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private static void openBrowserFallback(final String url) throws AuthException {
        final var os = System.getProperty("os.name", "").toLowerCase();
        try {
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "start", "", url).start();
            } else if (os.contains("mac")) {
                new ProcessBuilder("open", url).start();
            } else {
                new ProcessBuilder("xdg-open", url).start();
            }
        } catch (IOException ex) {
            throw new AuthException(
                    "Cannot open browser. Please manually visit:\n" + url, ex);
        }
    }

    /**
     * Simple JSON string extraction without external dependencies.
     */
    private static String extractJsonString(final String json, final String key) {
        final var pattern = "\"" + key + "\"";
        final int keyIdx = json.indexOf(pattern);
        if (keyIdx < 0) return null;

        final int colonIdx = json.indexOf(':', keyIdx + pattern.length());
        if (colonIdx < 0) return null;

        final int quoteStart = json.indexOf('"', colonIdx + 1);
        if (quoteStart < 0) return null;

        final int quoteEnd = json.indexOf('"', quoteStart + 1);
        if (quoteEnd < 0) return null;

        return json.substring(quoteStart + 1, quoteEnd);
    }

    private static int extractJsonNumber(final String json, final String key,
                                         final int defaultValue) {
        final var pattern = "\"" + key + "\"";
        final int keyIdx = json.indexOf(pattern);
        if (keyIdx < 0) return defaultValue;

        final int colonIdx = json.indexOf(':', keyIdx + pattern.length());
        if (colonIdx < 0) return defaultValue;

        final var sb = new StringBuilder();
        for (int i = colonIdx + 1; i < json.length(); i++) {
            final char ch = json.charAt(i);
            if (Character.isDigit(ch)) {
                sb.append(ch);
            } else if (!sb.isEmpty()) {
                break;
            }
        }
        if (sb.isEmpty()) return defaultValue;
        try {
            return Integer.parseInt(sb.toString());
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }
}
