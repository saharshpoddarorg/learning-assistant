package server.atlassian.v2.config;

import server.atlassian.v2.model.AuthMethod;
import server.atlassian.v2.model.DeploymentVariant;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Loads {@link ServerConfigV2} from layered property sources.
 *
 * <h3>Load order (later overrides earlier):</h3>
 * <ol>
 *   <li>Base config file — {@code atlassian-v2-config.properties} (committed)</li>
 *   <li>Local overrides — {@code atlassian-v2-config.local.properties} (gitignored)</li>
 *   <li>Environment variables — {@code ATLASSIAN_*} prefix</li>
 * </ol>
 */
public final class ConfigLoaderV2 {

    private static final Logger LOGGER = Logger.getLogger(ConfigLoaderV2.class.getName());

    private static final String BASE_CONFIG_FILE = "atlassian-v2-config.properties";
    private static final String LOCAL_CONFIG_FILE = "atlassian-v2-config.local.properties";
    private static final String DEFAULT_CONFIG_DIR = "user-config/servers/atlassian-v2";
    private static final String DEFAULT_CREDENTIAL_DIR = "credentials";

    private final Path configDir;

    private ConfigLoaderV2(final Path configDir) {
        this.configDir = configDir;
    }

    /** Loader using the default config directory. */
    public static ConfigLoaderV2 withDefaults() {
        final var envDir = System.getenv("ATLASSIAN_V2_CONFIG_DIR");
        final Path dir = envDir != null && !envDir.isBlank()
                ? Path.of(envDir)
                : Path.of(System.getProperty("user.dir"), DEFAULT_CONFIG_DIR);
        return new ConfigLoaderV2(dir);
    }

    /** Loader using an explicit config directory. */
    public static ConfigLoaderV2 withConfigDir(final Path configDir) {
        return new ConfigLoaderV2(java.util.Objects.requireNonNull(configDir));
    }

    /**
     * Loads and returns the fully merged config.
     */
    public ServerConfigV2 load() {
        final var merged = new Properties();
        loadFromFile(merged, configDir.resolve(BASE_CONFIG_FILE));
        loadFromFile(merged, configDir.resolve(LOCAL_CONFIG_FILE));
        applyEnvironmentOverrides(merged);
        return buildConfig(merged);
    }

    /**
     * Loads config in safe mode (no exceptions) for --list-tools, --demo.
     */
    public ServerConfigV2 loadSafe() {
        try {
            return load();
        } catch (Exception ex) {
            LOGGER.warning("Config load failed (using safe defaults): " + ex.getMessage());
            return safePlaceholder();
        }
    }

    // ── File loading ─────────────────────────────────────────────────────────

    private void loadFromFile(final Properties target, final Path filePath) {
        if (!Files.exists(filePath)) {
            LOGGER.fine("Config file not found (skipping): " + filePath);
            return;
        }
        try (final InputStream stream = Files.newInputStream(filePath)) {
            final var fileProps = new Properties();
            fileProps.load(stream);
            fileProps.forEach((key, value) ->
                    target.setProperty(key.toString(), value.toString()));
            LOGGER.info("Loaded config from: " + filePath);
        } catch (final IOException ex) {
            LOGGER.warning("Failed to load config: " + filePath + " — " + ex.getMessage());
        }
    }

    // ── Environment variable overrides ───────────────────────────────────────

    private void applyEnvironmentOverrides(final Properties props) {
        env(props, "ATLASSIAN_INSTANCE_NAME",        "atlassian.instance.name");
        env(props, "ATLASSIAN_VARIANT",              "atlassian.variant");
        env(props, "ATLASSIAN_JIRA_URL",             "atlassian.jira.baseUrl");
        env(props, "ATLASSIAN_CONFLUENCE_URL",       "atlassian.confluence.baseUrl");
        env(props, "ATLASSIAN_BITBUCKET_URL",        "atlassian.bitbucket.baseUrl");
        env(props, "ATLASSIAN_EMAIL",                "atlassian.auth.email");
        env(props, "ATLASSIAN_TOKEN",                "atlassian.auth.token");
        env(props, "ATLASSIAN_AUTH_TYPE",            "atlassian.auth.type");
        env(props, "ATLASSIAN_OAUTH_CLIENT_ID",      "atlassian.oauth.clientId");
        env(props, "ATLASSIAN_OAUTH_CLIENT_SECRET",  "atlassian.oauth.clientSecret");
        env(props, "ATLASSIAN_CONNECT_TIMEOUT",      "atlassian.http.connectTimeoutMs");
        env(props, "ATLASSIAN_READ_TIMEOUT",         "atlassian.http.readTimeoutMs");
        env(props, "ATLASSIAN_JIRA_ENABLED",         "atlassian.product.jira.enabled");
        env(props, "ATLASSIAN_CONFLUENCE_ENABLED",   "atlassian.product.confluence.enabled");
        env(props, "ATLASSIAN_BITBUCKET_ENABLED",    "atlassian.product.bitbucket.enabled");
    }

    private void env(final Properties props, final String envKey, final String propKey) {
        final var value = System.getenv(envKey);
        if (value != null && !value.isBlank()) {
            props.setProperty(propKey, value.strip());
        }
    }

    // ── Build config record ──────────────────────────────────────────────────

    private ServerConfigV2 buildConfig(final Properties props) {
        final var instanceName = prop(props, "atlassian.instance.name", "atlassian-v2");
        final var variant = DeploymentVariant.fromConfigKey(
                prop(props, "atlassian.variant", "cloud"));

        final var jiraUrl = ServerConfigV2.normalize(prop(props, "atlassian.jira.baseUrl", ""));
        final var confluenceUrl = ServerConfigV2.normalize(prop(props, "atlassian.confluence.baseUrl", ""));
        final var bitbucketUrl = ServerConfigV2.normalize(prop(props, "atlassian.bitbucket.baseUrl", ""));

        final var authTypeStr = prop(props, "atlassian.auth.type", "");
        final var authMethod = AuthMethod.fromConfigKey(authTypeStr);
        final var authEmail = prop(props, "atlassian.auth.email", "");
        final var authToken = prop(props, "atlassian.auth.token", "");

        final var oauthClientId = prop(props, "atlassian.oauth.clientId", "");
        final var oauthSecret = prop(props, "atlassian.oauth.clientSecret", "");

        final int connectTimeout = parseInt(props, "atlassian.http.connectTimeoutMs",
                ServerConfigV2.DEFAULT_CONNECT_TIMEOUT_MS);
        final int readTimeout = parseInt(props, "atlassian.http.readTimeoutMs",
                ServerConfigV2.DEFAULT_READ_TIMEOUT_MS);

        final boolean jiraEnabled = parseBool(props, "atlassian.product.jira.enabled", true);
        final boolean confluenceEnabled = parseBool(props, "atlassian.product.confluence.enabled", true);
        final boolean bitbucketEnabled = parseBool(props, "atlassian.product.bitbucket.enabled", false);

        final var credentialDir = configDir.resolve(DEFAULT_CREDENTIAL_DIR);

        return new ServerConfigV2(
                instanceName,
                variant,
                jiraEnabled && jiraUrl != null ? jiraUrl : null,
                confluenceEnabled && confluenceUrl != null ? confluenceUrl : null,
                bitbucketEnabled && bitbucketUrl != null ? bitbucketUrl : null,
                authMethod,
                blankToNull(authEmail),
                blankToNull(authToken),
                blankToNull(oauthClientId),
                blankToNull(oauthSecret),
                credentialDir,
                connectTimeout,
                readTimeout,
                jiraEnabled && jiraUrl != null,
                confluenceEnabled && confluenceUrl != null,
                bitbucketEnabled && bitbucketUrl != null
        );
    }

    private ServerConfigV2 safePlaceholder() {
        return new ServerConfigV2(
                "atlassian-v2-placeholder",
                DeploymentVariant.CLOUD,
                null, null, null,
                null, null, null, null, null,
                configDir.resolve(DEFAULT_CREDENTIAL_DIR),
                ServerConfigV2.DEFAULT_CONNECT_TIMEOUT_MS,
                ServerConfigV2.DEFAULT_READ_TIMEOUT_MS,
                false, false, false
        );
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private static String prop(final Properties props, final String key,
                               final String defaultValue) {
        return props.getProperty(key, defaultValue).strip();
    }

    private static String blankToNull(final String value) {
        return (value == null || value.isBlank()) ? null : value;
    }

    private static int parseInt(final Properties props, final String key,
                                final int defaultValue) {
        try {
            return Integer.parseInt(props.getProperty(key, String.valueOf(defaultValue)).strip());
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    private static boolean parseBool(final Properties props, final String key,
                                     final boolean defaultValue) {
        final var raw = props.getProperty(key);
        if (raw == null) return defaultValue;
        return "true".equalsIgnoreCase(raw.strip()) || "1".equals(raw.strip());
    }
}
