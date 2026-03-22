package server.atlassian.v1.config;

/**
 * Identifies which flavour/deployment of an Atlassian product is being targeted.
 *
 * <p>Different variants have different base URLs, authentication mechanisms,
 * and API endpoint paths. A single Atlassian server configuration must specify
 * a variant so that clients can correctly build request URLs and select the
 * right auth scheme.
 *
 * <h3>Common configurations:</h3>
 * <ul>
 *   <li>{@link #CLOUD} — {@code https://{workspace}.atlassian.net} — OAuth 2.0 / API token</li>
 *   <li>{@link #DATA_CENTER} — {@code https://jira.company.com} — PAT (Bearer token)</li>
 *   <li>{@link #SERVER} — {@code https://jira.company.com} — Basic auth (username:password)</li>
 *   <li>{@link #CUSTOM} — any URL — auth scheme configured explicitly</li>
 * </ul>
 */
public enum AtlassianVariant {

    /**
     * Atlassian Cloud \u2014 hosted at {@code atlassian.net}.
     *
     * <ul>
     *   <li>Jira: {@code https://{workspace}.atlassian.net}</li>
     *   <li>Confluence: {@code https://{workspace}.atlassian.net/wiki}</li>
     *   <li>Bitbucket: {@code https://api.bitbucket.org} (separate SaaS host)</li>
     *   <li>Auth: API token (email + token) or OAuth 2.0</li>
     *   <li>API: Jira REST v3, Confluence REST v2, Bitbucket REST 2.0</li>
     * </ul>
     */
    CLOUD("cloud", "Atlassian Cloud", true),

    /**
     * Atlassian Data Center \u2014 self-managed, clustered deployment.
     *
     * <ul>
     *   <li>URL: customer-controlled (e.g., {@code https://jira.company.com})</li>
     *   <li>Auth: Personal Access Token (PAT) as Bearer token</li>
     *   <li>API: Jira REST v2/v3, Confluence REST v1/v2, Bitbucket REST 1.0</li>
     *   <li>Note: Some v3 API features may not be available on older versions</li>
     * </ul>
     */
    DATA_CENTER("data_center", "Atlassian Data Center", false),

    /**
     * Atlassian Server \u2014 legacy self-managed, single-node deployment.
     *
     * <p><strong>End-of-life: February 15, 2024.</strong> No new Server licences
     * are sold. Existing instances may still be in use at some organisations.</p>
     *
     * <ul>
     *   <li>URL: customer-controlled</li>
     *   <li>Auth: Basic auth (username:password) or PAT (Jira 8.14+)</li>
     *   <li>API: Jira REST v2, Confluence REST v1</li>
     * </ul>
     */
    SERVER("server", "Atlassian Server (Legacy)", false),

    /**
     * Custom / unknown variant \u2014 for colleague copies, test instances,
     * sandboxes, or any non-standard deployment.
     *
     * <p>When using this variant, all URLs and auth settings must be
     * explicitly configured; no defaults will be inferred.</p>
     */
    CUSTOM("custom", "Custom Instance", false);

    // -------------------------------------------------------------------------

    private final String configKey;
    private final String displayName;
    private final boolean usesAtlassianNet;

    AtlassianVariant(final String configKey, final String displayName,
                     final boolean usesAtlassianNet) {
        this.configKey = configKey;
        this.displayName = displayName;
        this.usesAtlassianNet = usesAtlassianNet;
    }

    /**
     * Returns the string key used in config files.
     *
     * @return lower-case config key (e.g., {@code "cloud"}, {@code "data_center"})
     */
    public String getConfigKey() {
        return configKey;
    }

    /**
     * Returns a human-readable label for this variant.
     *
     * @return the display name (e.g., "Atlassian Cloud")
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns {@code true} for Cloud instances hosted at {@code atlassian.net}.
     * Use this to decide whether Bitbucket is at a separate API host.
     *
     * @return {@code true} if the instance is Atlassian-hosted Cloud
     */
    public boolean isAtlassianNetHosted() {
        return usesAtlassianNet;
    }

    /**
     * Returns {@code true} if this variant uses self-managed infrastructure
     * (Data Center or legacy Server).
     *
     * @return {@code true} for DATA_CENTER and SERVER variants
     */
    public boolean isSelfManaged() {
        return this == DATA_CENTER || this == SERVER;
    }

    /**
     * Resolves a variant from its config key or enum name.
     *
     * @param value the config key or enum name (case-insensitive)
     * @return the matching variant, or {@link #CUSTOM} if not recognized
     */
    public static AtlassianVariant fromConfigKey(final String value) {
        if (value == null || value.isBlank()) {
            return CUSTOM;
        }
        for (final AtlassianVariant variant : values()) {
            if (variant.configKey.equalsIgnoreCase(value)
                    || variant.name().equalsIgnoreCase(value)) {
                return variant;
            }
        }
        return CUSTOM;
    }
}
