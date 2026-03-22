package server.atlassian.v2.auth;

/**
 * Contract for authentication providers.
 *
 * <p>Each implementation knows how to produce an HTTP Authorization header
 * for Atlassian REST API calls. Implementations may cache tokens internally
 * and refresh them when expired.
 */
public interface AuthProvider {

    /**
     * Returns the HTTP Authorization header value (e.g., {@code "Basic ..."} or
     * {@code "Bearer ..."}).
     *
     * <p>Implementations may trigger a token refresh if the current token is expired.
     *
     * @return the Authorization header value, never {@code null}
     * @throws AuthException if authentication cannot be performed
     */
    String getAuthorizationHeader() throws AuthException;

    /**
     * Returns {@code true} if this provider has valid credentials ready to use.
     *
     * @return {@code true} if authenticated
     */
    boolean isAuthenticated();

    /**
     * Returns a human-readable description of the current auth state
     * (e.g., "API token for user@example.com", "OAuth — token expires in 47 min").
     *
     * @return status description
     */
    String statusDescription();

    /**
     * Clears any cached credentials (for logout).
     */
    void clearCredentials();
}
