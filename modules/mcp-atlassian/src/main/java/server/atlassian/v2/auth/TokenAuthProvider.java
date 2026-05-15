package server.atlassian.v2.auth;

import java.util.Base64;
import java.util.Objects;

/**
 * Auth provider for config-based credentials (API tokens and PATs).
 *
 * <p>This is <strong>Option B</strong> — credentials stored in a local
 * gitignored properties file.
 *
 * <ul>
 *   <li><strong>API Token (Cloud):</strong> {@code Basic base64(email:token)}</li>
 *   <li><strong>PAT (Data Center):</strong> {@code Bearer token}</li>
 * </ul>
 */
public final class TokenAuthProvider implements AuthProvider {

    private final String email;
    private final String token;
    private final boolean isApiToken;

    /**
     * Creates a token auth provider.
     *
     * @param email      the user email (required for API token, empty for PAT)
     * @param token      the API token or PAT value
     * @param isApiToken {@code true} for API token (Basic auth), {@code false} for PAT (Bearer)
     */
    public TokenAuthProvider(final String email, final String token, final boolean isApiToken) {
        this.email = email != null ? email : "";
        this.token = Objects.requireNonNull(token, "Token must not be null");
        this.isApiToken = isApiToken;

        if (token.isBlank()) {
            throw new IllegalArgumentException("Token must not be blank");
        }
        if (isApiToken && this.email.isBlank()) {
            throw new IllegalArgumentException("Email is required for API token auth");
        }
    }

    @Override
    public String getAuthorizationHeader() {
        if (isApiToken) {
            final var credentials = email + ":" + token;
            return "Basic " + Base64.getEncoder()
                    .encodeToString(credentials.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        }
        return "Bearer " + token;
    }

    @Override
    public boolean isAuthenticated() {
        return !token.isBlank();
    }

    @Override
    public String statusDescription() {
        if (isApiToken) {
            return "API token for " + email;
        }
        return "Personal Access Token (PAT)";
    }

    @Override
    public void clearCredentials() {
        // Token auth is config-based — cannot clear at runtime.
        // User must edit the config file to remove credentials.
    }
}
