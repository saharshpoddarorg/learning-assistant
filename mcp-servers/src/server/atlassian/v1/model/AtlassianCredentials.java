package server.atlassian.v1.model;

import java.util.Objects;

/**
 * Holds authentication credentials for connecting to Atlassian APIs.
 *
 * <p>Supports two authentication modes:
 * <ul>
 *   <li><strong>API Token</strong> (Cloud) — email + API token, sent as Basic Auth</li>
 *   <li><strong>Personal Access Token</strong> (Data Center / Bitbucket) — bearer token</li>
 * </ul>
 *
 * @param email    the Atlassian account email (required for API token auth; empty string for PAT)
 * @param token    the API token or Personal Access Token
 * @param authType the authentication method to use
 */
public record AtlassianCredentials(
        String email,
        String token,
        AuthType authType
) {

    /**
     * Compact constructor — validates all fields.
     */
    public AtlassianCredentials {
        Objects.requireNonNull(email, "Email must not be null (use empty string for PAT auth)");
        Objects.requireNonNull(token, "Token must not be null");
        Objects.requireNonNull(authType, "Auth type must not be null");

        if (token.isBlank()) {
            throw new IllegalArgumentException("Token must not be blank");
        }

        if (authType == AuthType.API_TOKEN && email.isBlank()) {
            throw new IllegalArgumentException(
                    "Email is required for API_TOKEN authentication");
        }
    }

    /**
     * Creates API token credentials for Atlassian Cloud.
     *
     * @param email    the account email
     * @param apiToken the API token (from id.atlassian.com)
     * @return credentials configured for Basic auth
     */
    public static AtlassianCredentials apiToken(final String email, final String apiToken) {
        return new AtlassianCredentials(email, apiToken, AuthType.API_TOKEN);
    }

    /**
     * Creates Personal Access Token credentials for Data Center or Bitbucket.
     *
     * @param pat the personal access token
     * @return credentials configured for Bearer auth
     */
    public static AtlassianCredentials personalAccessToken(final String pat) {
        return new AtlassianCredentials("", pat, AuthType.PERSONAL_ACCESS_TOKEN);
    }

    /**
     * Builds the HTTP {@code Authorization} header value for this credential.
     *
     * @return the encoded header (e.g., {@code "Basic dXNlcjp0b2tlbg=="} or {@code "Bearer token"})
     */
    public String toAuthorizationHeader() {
        return switch (authType) {
            case API_TOKEN -> {
                final var combined = email + ":" + token;
                yield "Basic " + java.util.Base64.getEncoder()
                        .encodeToString(combined.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            }
            case PERSONAL_ACCESS_TOKEN -> "Bearer " + token;
        };
    }

    @Override
    public String toString() {
        // Never expose token in logs
        return "AtlassianCredentials[email=" + email
                + ", authType=" + authType
                + ", token=***]";
    }
}
