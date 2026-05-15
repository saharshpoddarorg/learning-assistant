package server.atlassian.v2.model;

/**
 * Atlassian deployment variants.
 *
 * <p>Each variant determines default authentication scheme, URL structure,
 * and API behavior.
 */
public enum DeploymentVariant {

    /** Atlassian Cloud — hosted at {@code *.atlassian.net}. Uses API tokens or OAuth 2.0 (3LO). */
    CLOUD("cloud", false),

    /** Data Center — self-managed clustered deployment. Uses Personal Access Tokens. */
    DATA_CENTER("data_center", true),

    /** Server — legacy self-managed (EOL Feb 2024). Uses Basic auth. */
    SERVER("server", true),

    /** Custom — manual configuration of all settings. */
    CUSTOM("custom", false);

    private final String configKey;
    private final boolean selfManaged;

    DeploymentVariant(final String configKey, final boolean selfManaged) {
        this.configKey = configKey;
        this.selfManaged = selfManaged;
    }

    public String configKey() { return configKey; }

    public boolean isSelfManaged() { return selfManaged; }

    /**
     * Parses a variant from its config key string.
     *
     * @param key the config value (e.g. {@code "cloud"}, {@code "data_center"})
     * @return the matching variant, defaults to {@link #CLOUD}
     */
    public static DeploymentVariant fromConfigKey(final String key) {
        if (key == null) return CLOUD;
        return switch (key.strip().toLowerCase()) {
            case "data_center", "datacenter", "dc" -> DATA_CENTER;
            case "server" -> SERVER;
            case "custom" -> CUSTOM;
            default -> CLOUD;
        };
    }
}
