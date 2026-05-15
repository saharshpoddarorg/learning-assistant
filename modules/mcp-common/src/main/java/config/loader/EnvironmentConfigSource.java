package config.loader;

import java.util.Map;
import java.util.Properties;

/**
 * Loads configuration overrides from environment variables.
 *
 * <p>Environment variables follow the convention of uppercased, underscore-separated
 * keys with an {@code MCP_} prefix. They are converted to the dot-separated property
 * key format used internally.
 *
 * <p>Mapping examples:
 * <pre>
 *   MCP_CONFIG_ACTIVEPROFILE  →  config.activeProfile
 *   MCP_APIKEYS_GITHUB        →  apiKeys.github
 *   MCP_SERVER_GITHUB_COMMAND  →  server.github.command
 *   MCP_PREFERENCES_LOGLEVEL  →  preferences.logLevel
 * </pre>
 *
 * <p>Environment variables take the <strong>highest precedence</strong> and override
 * values from properties files and profile configs.
 */
public class EnvironmentConfigSource implements ConfigSource {

    private static final String ENV_PREFIX = "MCP_";

    /**
     * Loads all {@code MCP_}-prefixed environment variables and converts them
     * to dot-separated property keys.
     *
     * @return a {@link Properties} containing the mapped environment variables
     */
    @Override
    public Properties load() {
        final var properties = new Properties();

        System.getenv().forEach((envKey, envValue) -> {
            if (envKey.startsWith(ENV_PREFIX)) {
                final var propertyKey = convertEnvKeyToPropertyKey(envKey);
                properties.setProperty(propertyKey, envValue);
            }
        });

        return properties;
    }

    /**
     * Returns the source name for this environment-based source.
     *
     * @return {@code "environment variables (MCP_* prefix)"}
     */
    @Override
    public String sourceName() {
        return "environment variables (MCP_* prefix)";
    }

    /**
     * Known camelCase segments that must be restored after lowercase conversion.
     *
     * <p>Environment variables are uppercase with underscores, so multi-word property
     * keys like {@code logLevel}, {@code activeProfile}, {@code timeoutSeconds} etc.
     * lose their casing. This map restores them.
     */
    private static final Map<String, String> CAMEL_CASE_SEGMENTS = Map.ofEntries(
            Map.entry("activeprofile", "activeProfile"),
            Map.entry("loglevel", "logLevel"),
            Map.entry("maxretries", "maxRetries"),
            Map.entry("timeoutseconds", "timeoutSeconds"),
            Map.entry("autoconnect", "autoConnect"),
            Map.entry("apikeys", "apiKeys"),
            Map.entry("launchmode", "launchMode"),
            Map.entry("remotedebuggingport", "remoteDebuggingPort"),
            Map.entry("windowwidth", "windowWidth"),
            Map.entry("windowheight", "windowHeight"),
            Map.entry("browserexecutable", "browserExecutable"),
            Map.entry("browserprofile", "browserProfile"),
            Map.entry("browseraccount", "browserAccount")
    );

    /**
     * Converts an environment variable key to a dot-separated property key.
     *
     * <p>The conversion process:
     * <ol>
     *   <li>Strip the {@code MCP_} prefix</li>
     *   <li>Convert to lowercase</li>
     *   <li>Replace underscores with dots</li>
     *   <li>Restore camelCase for known multi-word segments</li>
     * </ol>
     *
     * @param envKey the environment variable key (e.g., "MCP_APIKEYS_GITHUB")
     * @return the property key (e.g., "apiKeys.github")
     */
    private String convertEnvKeyToPropertyKey(final String envKey) {
        final var withoutPrefix = envKey.substring(ENV_PREFIX.length());
        final var dotSeparated = withoutPrefix.toLowerCase().replace('_', '.');

        // Restore camelCase for known multi-word segments
        var result = dotSeparated;
        for (final var entry : CAMEL_CASE_SEGMENTS.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
