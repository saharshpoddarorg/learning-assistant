package server.atlassian.v1.model;

/**
 * Authentication methods supported by Atlassian product APIs.
 *
 * <p>Use {@link #API_TOKEN} for Atlassian Cloud (email + token pair sent as Basic auth).
 * Use {@link #PERSONAL_ACCESS_TOKEN} for Data Center, Server 8.14+, and Bitbucket (bearer token).
 */
public enum AuthType {

    /**
     * Basic auth with {@code email:apiToken}, base64-encoded.
     * Used by Atlassian Cloud — generate tokens at id.atlassian.com.
     */
    API_TOKEN,

    /**
     * Bearer token authentication.
     * Used by Jira/Confluence Data Center, Server 8.14+, and Bitbucket.
     * Generate personal access tokens in the product's user profile settings.
     */
    PERSONAL_ACCESS_TOKEN
}

