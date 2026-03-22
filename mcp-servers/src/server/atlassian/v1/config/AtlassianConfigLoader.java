package server.atlassian.v1.config;

import server.atlassian.v1.model.AtlassianCredentials;
import server.atlassian.v1.model.AuthType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Loads {@link AtlassianServerConfig} from a layered set of property sources.
 *
 * <h3>Load order (later sources override earlier ones):</h3>
 * <ol>
 *   <li><strong>Built-in defaults</strong> — safe fallback values (no secrets)</li>
 *   <li><strong>Base config file</strong> — {@code atlassian-config.properties}
 *       (committed to version control; no secrets)</li>
 *   <li><strong>Local overrides</strong> — {@code atlassian-config.local.properties}
 *       (gitignored; contains credentials and instance-specific URLs)</li>
 *   <li><strong>Environment variables</strong> — highest precedence; useful for CI/CD</li>
 * </ol>
 *
 * <h3>Config file location:</h3>
 * <p>By default, files are resolved relative to the working directory under
 * {@code user-config/servers/atlassian/}. This can be overridden by passing
 * an explicit config directory path to {@link #withConfigDir(Path)}.
 *
 * <h3>Environment variable mappings:</h3>
 * <pre>
 *   ATLASSIAN_INSTANCE_NAME   → atlassian.instance.name
 *   ATLASSIAN_VARIANT         → atlassian.variant
 *   ATLASSIAN_JIRA_URL        → atlassian.jira.baseUrl
 *   ATLASSIAN_CONFLUENCE_URL  → atlassian.confluence.baseUrl
 *   ATLASSIAN_BITBUCKET_URL   → atlassian.bitbucket.baseUrl
 *   ATLASSIAN_EMAIL           → atlassian.auth.email
 *   ATLASSIAN_TOKEN           → atlassian.auth.token
 *   ATLASSIAN_AUTH_TYPE       → atlassian.auth.type
 *   ATLASSIAN_CONNECT_TIMEOUT → atlassian.http.connectTimeoutMs
 *   ATLASSIAN_READ_TIMEOUT    → atlassian.http.readTimeoutMs
 *   ATLASSIAN_JIRA_ENABLED    → atlassian.product.jira.enabled
 *   ATLASSIAN_CONFLUENCE_ENABLED → atlassian.product.confluence.enabled
 *   ATLASSIAN_BITBUCKET_ENABLED  → atlassian.product.bitbucket.enabled
 * </pre>
 */
public final class AtlassianConfigLoader {

    private static final Logger LOGGER = Logger.getLogger(AtlassianConfigLoader.class.getName());

    private static final String BASE_CONFIG_FILE = "atlassian-config.properties";
    private static final String LOCAL_CONFIG_FILE = "atlassian-config.local.properties";
    private static final String DEFAULT_CONFIG_DIR = "user-config/servers/atlassian";

    private final Path configDir;

    private AtlassianConfigLoader(final Path configDir) {
        this.configDir = configDir;
    }

    /**
     * Returns a loader using the default config directory
     * ({@code user-config/servers/atlassian/} relative to the working directory).
     *
     * @return a new loader instance
     */
    public static AtlassianConfigLoader withDefaults() {
        return new AtlassianConfigLoader(
                Path.of(System.getProperty("user.dir"), DEFAULT_CONFIG_DIR));
    }

    /**
     * Returns a loader using an explicit config directory path.
     *
     * @param configDir the directory containing the config files
     * @return a new loader instance
     */
    public static AtlassianConfigLoader withConfigDir(final Path configDir) {
        return new AtlassianConfigLoader(
                java.util.Objects.requireNonNull(configDir, "configDir must not be null"));
    }

    /**
     * Loads and returns the merged {@link AtlassianServerConfig}.
     *
     * @return the fully resolved config for this Atlassian instance
     * @throws IllegalStateException if required config values are missing after all layers
     */
    public AtlassianServerConfig load() {
        final var merged = new Properties();

        loadFromFile(merged, configDir.resolve(BASE_CONFIG_FILE));
        loadFromFile(merged, configDir.resolve(LOCAL_CONFIG_FILE));
        applyEnvironmentOverrides(merged);

        return buildConfig(merged);
    }

    // -------------------------------------------------------------------------
    // Internal loading helpers
    // -------------------------------------------------------------------------

    private void loadFromFile(final Properties target, final Path filePath) {
        if (!Files.exists(filePath)) {
            LOGGER.fine("Config file not found (skipping): " + filePath);
            return;
        }
        try (final InputStream stream = Files.newInputStream(filePath)) {
            final var fileProps = new Properties();
            fileProps.load(stream);
            // putAll so all entries from the file get merged in
            fileProps.forEach((key, value) -> target.setProperty(key.toString(), value.toString()));
            LOGGER.info("Loaded Atlassian config from: " + filePath);
        } catch (final IOException ex) {
            LOGGER.warning("Failed to load config file: " + filePath + " — " + ex.getMessage());
        }
    }

    private void applyEnvironmentOverrides(final Properties props) {
        overrideFromEnv(props, "ATLASSIAN_INSTANCE_NAME",        "atlassian.instance.name");
        overrideFromEnv(props, "ATLASSIAN_VARIANT",              "atlassian.variant");
        overrideFromEnv(props, "ATLASSIAN_JIRA_URL",             "atlassian.jira.baseUrl");
        overrideFromEnv(props, "ATLASSIAN_CONFLUENCE_URL",       "atlassian.confluence.baseUrl");
        overrideFromEnv(props, "ATLASSIAN_BITBUCKET_URL",        "atlassian.bitbucket.baseUrl");
        overrideFromEnv(props, "ATLASSIAN_EMAIL",                "atlassian.auth.email");
        overrideFromEnv(props, "ATLASSIAN_TOKEN",                "atlassian.auth.token");
        overrideFromEnv(props, "ATLASSIAN_AUTH_TYPE",            "atlassian.auth.type");
        overrideFromEnv(props, "ATLASSIAN_CONNECT_TIMEOUT",      "atlassian.http.connectTimeoutMs");
        overrideFromEnv(props, "ATLASSIAN_READ_TIMEOUT",         "atlassian.http.readTimeoutMs");
        overrideFromEnv(props, "ATLASSIAN_JIRA_ENABLED",         "atlassian.product.jira.enabled");
        overrideFromEnv(props, "ATLASSIAN_CONFLUENCE_ENABLED",   "atlassian.product.confluence.enabled");
        overrideFromEnv(props, "ATLASSIAN_BITBUCKET_ENABLED",    "atlassian.product.bitbucket.enabled");
    }

    private void overrideFromEnv(final Properties props, final String envKey,
                                 final String propKey) {
        final var value = System.getenv(envKey);
        if (value != null && !value.isBlank()) {
            props.setProperty(propKey, value.strip());
        }
    }

    private AtlassianServerConfig buildConfig(final Properties props) {
        // Instance name has a safe default — does not block --list-tools mode
        final var instanceName = props.getProperty("atlassian.instance.name", "atlassian-instance").strip();
        final var variant = AtlassianVariant.fromConfigKey(props.getProperty("atlassian.variant", "cloud"));

        final var jiraUrl      = props.getProperty("atlassian.jira.baseUrl", "").strip();
        final var confluenceUrl = props.getProperty("atlassian.confluence.baseUrl", "").strip();
        final var bitbucketUrl = props.getProperty("atlassian.bitbucket.baseUrl", "").strip();

        final var credentials = buildCredentials(props, variant);

        final int connectTimeout = parseInt(props, "atlassian.http.connectTimeoutMs",
                AtlassianServerConfig.DEFAULT_CONNECT_TIMEOUT_MS);
        final int readTimeout = parseInt(props, "atlassian.http.readTimeoutMs",
                AtlassianServerConfig.DEFAULT_READ_TIMEOUT_MS);

        final boolean jiraEnabled       = parseBoolean(props, "atlassian.product.jira.enabled", true);
        final boolean confluenceEnabled = parseBoolean(props, "atlassian.product.confluence.enabled", true);
        final boolean bitbucketEnabled  = parseBoolean(props, "atlassian.product.bitbucket.enabled", false);

        return new AtlassianServerConfig(
                instanceName,
                variant,
                jiraEnabled && !jiraUrl.isBlank() ? jiraUrl : null,
                confluenceEnabled && !confluenceUrl.isBlank() ? confluenceUrl : null,
                bitbucketEnabled && !bitbucketUrl.isBlank() ? bitbucketUrl : null,
                credentials,
                connectTimeout,
                readTimeout,
                jiraEnabled && !jiraUrl.isBlank(),
                confluenceEnabled && !confluenceUrl.isBlank(),
                bitbucketEnabled && !bitbucketUrl.isBlank()
        );
    }

    private AtlassianCredentials buildCredentials(final Properties props,
                                                  final AtlassianVariant variant) {
        final var email = props.getProperty("atlassian.auth.email", "").strip();
        final var token = props.getProperty("atlassian.auth.token", "").strip();
        final var authTypeStr = props.getProperty("atlassian.auth.type", "").strip();

        if (token.isBlank()) {
            throw new IllegalStateException(
                    "Missing required config: atlassian.auth.token (or ATLASSIAN_TOKEN env var). "
                    + "See atlassian-config.local.example.properties for setup instructions.");
        }

        // Resolve auth type: explicit config wins, then infer from variant
        final AuthType authType;
        if ("pat".equalsIgnoreCase(authTypeStr) || "personal_access_token".equalsIgnoreCase(authTypeStr)) {
            authType = AuthType.PERSONAL_ACCESS_TOKEN;
        } else if ("api_token".equalsIgnoreCase(authTypeStr) || "basic".equalsIgnoreCase(authTypeStr)) {
            authType = AuthType.API_TOKEN;
        } else {
            // Infer from variant: Cloud uses API token, self-managed uses PAT
            authType = variant.isSelfManaged() ? AuthType.PERSONAL_ACCESS_TOKEN : AuthType.API_TOKEN;
        }

        if (authType == AuthType.API_TOKEN && email.isBlank()) {
            throw new IllegalStateException(
                    "API token auth requires atlassian.auth.email (or ATLASSIAN_EMAIL env var). "
                    + "Use auth.type=pat for Personal Access Token auth.");
        }

        return new AtlassianCredentials(email, token, authType);
    }

    private String require(final Properties props, final String key) {
        final var value = props.getProperty(key, "").strip();
        if (value.isBlank()) {
            throw new IllegalStateException(
                    "Missing required Atlassian config key: '" + key + "'. "
                    + "Set it in atlassian-config.local.properties or as an environment variable.");
        }
        return value;
    }

    private int parseInt(final Properties props, final String key, final int defaultValue) {
        try {
            return Integer.parseInt(props.getProperty(key, String.valueOf(defaultValue)).strip());
        } catch (final NumberFormatException ex) {
            LOGGER.warning("Invalid integer for '" + key + "' — using default: " + defaultValue);
            return defaultValue;
        }
    }

    private boolean parseBoolean(final Properties props, final String key,
                                 final boolean defaultValue) {
        final var raw = props.getProperty(key);
        if (raw == null) return defaultValue;
        return "true".equalsIgnoreCase(raw.strip()) || "1".equals(raw.strip());
    }
}
