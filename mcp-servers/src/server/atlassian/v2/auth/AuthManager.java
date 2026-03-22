package server.atlassian.v2.auth;

import server.atlassian.v2.model.AuthMethod;
import server.atlassian.v2.model.DeploymentVariant;

import java.nio.file.Path;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Central authentication manager that selects and manages the active auth strategy.
 *
 * <p>Supports two modes:
 * <ul>
 *   <li><strong>Token (Option B):</strong> API token or PAT from config files</li>
 *   <li><strong>OAuth 2.0 (Option A):</strong> Browser-based login with token persistence</li>
 * </ul>
 *
 * <p>The auth method is determined by configuration. If OAuth credentials
 * (client_id, client_secret) are present, OAuth is available. If an API token
 * or PAT is present, token auth is available. Both can coexist — the configured
 * {@link AuthMethod} determines the default.
 */
public final class AuthManager {

    private static final Logger LOGGER = Logger.getLogger(AuthManager.class.getName());

    private final AuthProvider activeProvider;
    private final OAuthProvider oauthProvider;  // may be null if OAuth not configured
    private final AuthMethod activeMethod;

    private AuthManager(final AuthProvider activeProvider,
                        final OAuthProvider oauthProvider,
                        final AuthMethod activeMethod) {
        this.activeProvider = Objects.requireNonNull(activeProvider);
        this.oauthProvider = oauthProvider;
        this.activeMethod = activeMethod;
    }

    /**
     * Creates an auth manager from configuration values.
     *
     * @param authMethod     the configured auth method (null = auto-detect)
     * @param variant        the deployment variant
     * @param email          user email (for API token auth)
     * @param token          API token or PAT (for token auth)
     * @param oauthClientId  OAuth client ID (null if OAuth not configured)
     * @param oauthSecret    OAuth client secret (null if OAuth not configured)
     * @param credentialDir  directory for storing OAuth tokens
     * @return the configured auth manager
     */
    public static AuthManager create(final AuthMethod authMethod,
                                     final DeploymentVariant variant,
                                     final String email,
                                     final String token,
                                     final String oauthClientId,
                                     final String oauthSecret,
                                     final Path credentialDir) {

        // Build OAuth provider if credentials are available
        OAuthProvider oauthProv = null;
        if (oauthClientId != null && !oauthClientId.isBlank()
                && oauthSecret != null && !oauthSecret.isBlank()) {
            final var store = new CredentialStore(credentialDir);
            oauthProv = new OAuthProvider(oauthClientId, oauthSecret, store);
        }

        // Determine active method
        final AuthMethod method;
        if (authMethod != null) {
            method = authMethod;
        } else if (oauthProv != null && oauthProv.isAuthenticated()) {
            method = AuthMethod.OAUTH2_BROWSER;
        } else if (token != null && !token.isBlank()) {
            method = variant.isSelfManaged() ? AuthMethod.PAT : AuthMethod.API_TOKEN;
        } else if (oauthProv != null) {
            method = AuthMethod.OAUTH2_BROWSER;
        } else {
            // Placeholder — auth tools will guide the user
            method = AuthMethod.API_TOKEN;
        }

        // Build active provider
        final AuthProvider active = switch (method) {
            case OAUTH2_BROWSER -> {
                if (oauthProv != null) {
                    yield oauthProv;
                }
                // Fallback to a no-op provider that tells user to configure OAuth
                yield new UnconfiguredAuthProvider("OAuth not configured — "
                        + "set oauth.client_id and oauth.client_secret in your config file, "
                        + "or use auth.type=api_token with your API token.");
            }
            case API_TOKEN -> {
                if (token != null && !token.isBlank()) {
                    yield new TokenAuthProvider(email, token, true);
                }
                yield new UnconfiguredAuthProvider("API token not configured — "
                        + "set auth.token and auth.email in your local config file.");
            }
            case PAT -> {
                if (token != null && !token.isBlank()) {
                    yield new TokenAuthProvider(null, token, false);
                }
                yield new UnconfiguredAuthProvider("PAT not configured — "
                        + "set auth.token in your local config file.");
            }
        };

        LOGGER.info("Auth method: " + method.configKey()
                + " — " + active.statusDescription());

        return new AuthManager(active, oauthProv, method);
    }

    /** Returns the active auth provider. */
    public AuthProvider getProvider() { return activeProvider; }

    /** Returns the OAuth provider (for browser login tool), or {@code null} if not configured. */
    public OAuthProvider getOAuthProvider() { return oauthProvider; }

    /** Returns the active auth method. */
    public AuthMethod getActiveMethod() { return activeMethod; }

    /** Returns the Authorization header from the active provider. */
    public String getAuthorizationHeader() throws AuthException {
        return activeProvider.getAuthorizationHeader();
    }

    /** Returns whether the active provider is authenticated. */
    public boolean isAuthenticated() {
        return activeProvider.isAuthenticated();
    }

    /** Returns a status description. */
    public String statusDescription() {
        return activeProvider.statusDescription();
    }

    /**
     * Initiates the browser-based OAuth 2.0 login flow.
     *
     * @throws AuthException if OAuth is not configured or login fails
     */
    public void loginViaBrowser() throws AuthException {
        if (oauthProvider == null) {
            throw new AuthException("OAuth not configured — set oauth.client_id and "
                    + "oauth.client_secret in your config file.");
        }
        oauthProvider.loginViaBrowser();
    }

    /**
     * Clears stored credentials from the active provider.
     */
    public void clearCredentials() {
        activeProvider.clearCredentials();
        if (oauthProvider != null) {
            oauthProvider.clearCredentials();
        }
    }

    /**
     * Advises the user on how to switch authentication method.
     *
     * <p>Actual switching requires a config change and server restart because
     * the provider infrastructure is built at startup.
     *
     * @param target the desired auth method
     * @throws AuthException always, with instructions to update config
     */
    public void switchMethod(final AuthMethod target) throws AuthException {
        if (target == activeMethod) {
            throw new AuthException("Already using " + target.configKey());
        }
        throw new AuthException("To switch to " + target.configKey()
                + ", update 'auth.type=" + target.configKey()
                + "' in your local config file and restart the server.");
    }

    // ── Placeholder provider for unconfigured auth ───────────────────────────

    private static final class UnconfiguredAuthProvider implements AuthProvider {
        private final String message;

        UnconfiguredAuthProvider(final String message) { this.message = message; }

        @Override
        public String getAuthorizationHeader() throws AuthException {
            throw new AuthException(message);
        }

        @Override
        public boolean isAuthenticated() { return false; }

        @Override
        public String statusDescription() { return message; }

        @Override
        public void clearCredentials() { /* nothing to clear */ }
    }
}
