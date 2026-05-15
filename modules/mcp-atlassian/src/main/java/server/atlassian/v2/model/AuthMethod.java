package server.atlassian.v2.model;

/**
 * Authentication method used by the server.
 */
public enum AuthMethod {

    /** API token with email — Atlassian Cloud (Basic auth header). */
    API_TOKEN("api_token"),

    /** Personal Access Token — Data Center / Server (Bearer header). */
    PAT("pat"),

    /** OAuth 2.0 Authorization Code flow — browser-based login. */
    OAUTH2_BROWSER("oauth2_browser");

    private final String configKey;

    AuthMethod(final String configKey) {
        this.configKey = configKey;
    }

    public String configKey() { return configKey; }

    /**
     * Parses an auth method from its config key.
     *
     * @param key the config value
     * @return the matching method, or {@code null} if unrecognised
     */
    public static AuthMethod fromConfigKey(final String key) {
        if (key == null || key.isBlank()) return null;
        return switch (key.strip().toLowerCase()) {
            case "api_token", "basic" -> API_TOKEN;
            case "pat", "personal_access_token", "bearer" -> PAT;
            case "oauth2_browser", "oauth2", "oauth", "browser" -> OAUTH2_BROWSER;
            default -> null;
        };
    }
}
