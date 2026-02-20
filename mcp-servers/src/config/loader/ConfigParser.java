package config.loader;

import config.model.ApiKeyStore;
import config.model.BrowserPreferences;
import config.model.LocationPreferences;
import config.model.McpConfiguration;
import config.model.ProfileDefinition;
import config.model.ServerDefinition;
import config.model.TransportType;
import config.model.UserPreferences;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

/**
 * Parses flat {@link Properties} into structured {@link McpConfiguration} model objects.
 *
 * <p>This parser understands the hierarchical key convention used in
 * {@code mcp-config.properties} files and constructs the corresponding
 * immutable record hierarchy.
 *
 * <p>Key prefixes and their corresponding model:
 * <pre>
 *   config.*        → root-level settings (activeProfile)
 *   apiKeys.*       → {@link ApiKeyStore}
 *   location.*      → {@link LocationPreferences}
 *   preferences.*   → {@link UserPreferences}
 *   browser.*       → {@link BrowserPreferences}
 *   server.{name}.* → {@link ServerDefinition}
 *   profile.{name}.*→ {@link ProfileDefinition}
 * </pre>
 */
public class ConfigParser {

    private static final Logger LOGGER = Logger.getLogger(ConfigParser.class.getName());

    private static final String PREFIX_CONFIG = "config.";
    private static final String PREFIX_API_KEYS = "apiKeys.";
    private static final String PREFIX_LOCATION = "location.";
    private static final String PREFIX_PREFERENCES = "preferences.";
    private static final String PREFIX_BROWSER = "browser.";
    private static final String PREFIX_SERVER = "server.";
    private static final String PREFIX_PROFILE = "profile.";

    /**
     * Parses the given properties into a fully-constructed {@link McpConfiguration}.
     *
     * @param properties the flat key-value properties to parse
     * @return a populated, immutable configuration object
     */
    public McpConfiguration parse(final Properties properties) {
        Objects.requireNonNull(properties, "Properties must not be null");

        final var activeProfile = properties.getProperty("config.activeProfile", "");
        final var apiKeys = parseApiKeys(properties);
        final var location = parseLocation(properties);
        final var preferences = parsePreferences(properties);
        final var browser = parseBrowser(properties, PREFIX_BROWSER);
        final var servers = parseServers(properties);
        final var profiles = parseProfiles(properties);

        return new McpConfiguration(activeProfile, apiKeys, location, preferences, browser, servers, profiles);
    }

    /**
     * Extracts API keys from properties with the {@code apiKeys.} prefix.
     *
     * @param properties the source properties
     * @return an {@link ApiKeyStore} containing all discovered keys
     */
    private ApiKeyStore parseApiKeys(final Properties properties) {
        final var keys = new HashMap<String, String>();
        properties.forEach((rawKey, rawValue) -> {
            final var key = rawKey.toString();
            if (key.startsWith(PREFIX_API_KEYS)) {
                final var serviceName = key.substring(PREFIX_API_KEYS.length());
                keys.put(serviceName, rawValue.toString());
            }
        });
        return keys.isEmpty() ? ApiKeyStore.EMPTY : new ApiKeyStore(keys);
    }

    /**
     * Extracts location preferences from properties with the {@code location.} prefix.
     *
     * @param properties the source properties
     * @return a {@link LocationPreferences} (defaults used for missing values)
     */
    private LocationPreferences parseLocation(final Properties properties) {
        final var timezone = properties.getProperty("location.timezone", LocationPreferences.DEFAULT.timezone());
        final var locale = properties.getProperty("location.locale", LocationPreferences.DEFAULT.locale());
        final var region = properties.getProperty("location.region", LocationPreferences.DEFAULT.region());
        return new LocationPreferences(timezone, locale, region);
    }

    /**
     * Extracts user preferences from properties with the {@code preferences.} prefix.
     *
     * @param properties the source properties
     * @return a {@link UserPreferences} (defaults used for missing values)
     */
    private UserPreferences parsePreferences(final Properties properties) {
        final var theme = properties.getProperty("preferences.theme",
                UserPreferences.DEFAULT.theme());
        final var logLevel = properties.getProperty("preferences.logLevel",
                UserPreferences.DEFAULT.logLevel());
        final var maxRetries = parseIntOrDefault(properties, "preferences.maxRetries",
                UserPreferences.DEFAULT.maxRetries());
        final var timeoutSeconds = parseIntOrDefault(properties, "preferences.timeoutSeconds",
                UserPreferences.DEFAULT.timeoutSeconds());
        final var autoConnect = parseBooleanOrDefault(properties, "preferences.autoConnect",
                UserPreferences.DEFAULT.autoConnect());

        return new UserPreferences(theme, logLevel, maxRetries, timeoutSeconds, autoConnect);
    }

    /**
     * Extracts browser preferences from properties with the given prefix.
     *
     * @param properties the source properties
     * @param prefix     the browser key prefix (e.g., "browser." or "profile.dev.browser.")
     * @return a {@link BrowserPreferences} (defaults used for missing values)
     */
    private BrowserPreferences parseBrowser(final Properties properties, final String prefix) {
        final var executable = properties.getProperty(prefix + "executable",
                BrowserPreferences.DEFAULT.browserExecutable());
        final var profile = properties.getProperty(prefix + "profile",
                BrowserPreferences.DEFAULT.browserProfile());
        final var account = properties.getProperty(prefix + "account",
                BrowserPreferences.DEFAULT.browserAccount());
        final var launchMode = properties.getProperty(prefix + "launchMode",
                BrowserPreferences.DEFAULT.launchMode());
        final var headless = parseBooleanOrDefault(properties, prefix + "headless",
                BrowserPreferences.DEFAULT.headless());
        final var debugPort = parseIntOrDefault(properties, prefix + "remoteDebuggingPort",
                BrowserPreferences.DEFAULT.remoteDebuggingPort());
        final var width = parseIntOrDefault(properties, prefix + "windowWidth",
                BrowserPreferences.DEFAULT.windowWidth());
        final var height = parseIntOrDefault(properties, prefix + "windowHeight",
                BrowserPreferences.DEFAULT.windowHeight());

        return new BrowserPreferences(executable, profile, account, launchMode,
                headless, debugPort, width, height);
    }

    /**
     * Discovers and parses all server definitions from {@code server.{name}.*} keys.
     *
     * @param properties the source properties
     * @return a map of server name to its definition
     */
    private Map<String, ServerDefinition> parseServers(final Properties properties) {
        final var serverNames = discoverNames(properties, PREFIX_SERVER);
        final var servers = new HashMap<String, ServerDefinition>();

        for (final var serverName : serverNames) {
            final var prefix = PREFIX_SERVER + serverName + ".";
            servers.put(serverName, parseServerDefinition(properties, prefix, serverName));
        }

        return servers;
    }

    /**
     * Parses a single server definition from its prefixed properties.
     *
     * @param properties the source properties
     * @param prefix     the full prefix (e.g., "server.github.")
     * @param serverName the server identifier
     * @return a fully-constructed {@link ServerDefinition}
     */
    private ServerDefinition parseServerDefinition(final Properties properties,
                                                    final String prefix,
                                                    final String serverName) {
        final var displayName = properties.getProperty(prefix + "name", serverName);
        final var enabled = parseBooleanOrDefault(properties, prefix + "enabled", true);
        final var transportValue = properties.getProperty(prefix + "transport", "stdio");
        final var transport = TransportType.fromConfigValue(transportValue);
        final var command = properties.getProperty(prefix + "command", "");
        final var argsRaw = properties.getProperty(prefix + "args", "");
        final var url = properties.getProperty(prefix + "url", "");

        final var args = argsRaw.isEmpty()
                ? List.<String>of()
                : List.of(argsRaw.split(","));

        // Collect env vars with prefix: server.{name}.env.{VAR_NAME}
        final var envPrefix = prefix + "env.";
        final var envVars = new HashMap<String, String>();
        properties.forEach((rawKey, rawValue) -> {
            final var key = rawKey.toString();
            if (key.startsWith(envPrefix)) {
                final var varName = key.substring(envPrefix.length());
                envVars.put(varName, rawValue.toString());
            }
        });

        return new ServerDefinition(displayName, enabled, transport, command, args, url, envVars);
    }

    /**
     * Discovers and parses all profile definitions from {@code profile.{name}.*} keys.
     *
     * @param properties the source properties
     * @return a map of profile name to its definition
     */
    private Map<String, ProfileDefinition> parseProfiles(final Properties properties) {
        final var profileNames = discoverNames(properties, PREFIX_PROFILE);
        final var profiles = new HashMap<String, ProfileDefinition>();

        for (final var profileName : profileNames) {
            final var prefix = PREFIX_PROFILE + profileName + ".";
            profiles.put(profileName, parseProfileDefinition(properties, prefix, profileName));
        }

        return profiles;
    }

    /**
     * Parses a single profile definition, including its preference overrides.
     *
     * @param properties  the source properties
     * @param prefix      the full prefix (e.g., "profile.development.")
     * @param profileName the profile identifier
     * @return a fully-constructed {@link ProfileDefinition}
     */
    private ProfileDefinition parseProfileDefinition(final Properties properties,
                                                      final String prefix,
                                                      final String profileName) {
        final var description = properties.getProperty(prefix + "description", "");

        // Parse profile-level preference overrides (fall back to global defaults)
        final var theme = properties.getProperty(prefix + "preferences.theme",
                UserPreferences.DEFAULT.theme());
        final var logLevel = properties.getProperty(prefix + "preferences.logLevel",
                UserPreferences.DEFAULT.logLevel());
        final var maxRetries = parseIntOrDefault(properties, prefix + "preferences.maxRetries",
                UserPreferences.DEFAULT.maxRetries());
        final var timeoutSeconds = parseIntOrDefault(properties, prefix + "preferences.timeoutSeconds",
                UserPreferences.DEFAULT.timeoutSeconds());
        final var autoConnect = parseBooleanOrDefault(properties, prefix + "preferences.autoConnect",
                UserPreferences.DEFAULT.autoConnect());
        final var preferences = new UserPreferences(theme, logLevel, maxRetries, timeoutSeconds, autoConnect);

        // Parse profile-level location overrides
        final var timezone = properties.getProperty(prefix + "location.timezone",
                LocationPreferences.DEFAULT.timezone());
        final var locale = properties.getProperty(prefix + "location.locale",
                LocationPreferences.DEFAULT.locale());
        final var region = properties.getProperty(prefix + "location.region",
                LocationPreferences.DEFAULT.region());
        final var locationPreferences = new LocationPreferences(timezone, locale, region);

        // Parse profile-level browser overrides
        final var browserPreferences = parseBrowser(properties, prefix + "browser.");

        // Parse profile-level server overrides
        final var serverOverridePrefix = prefix + "server.";
        final var serverNames = discoverNames(properties, serverOverridePrefix);
        final var serverOverrides = new HashMap<String, ServerDefinition>();
        for (final var serverName : serverNames) {
            final var serverPrefix = serverOverridePrefix + serverName + ".";
            serverOverrides.put(serverName, parseServerDefinition(properties, serverPrefix, serverName));
        }

        return new ProfileDefinition(profileName, description, preferences, locationPreferences,
                browserPreferences, serverOverrides);
    }

    /**
     * Discovers unique entity names from a given key prefix pattern.
     *
     * <p>For prefix "server.", a key "server.github.transport" yields name "github".
     *
     * @param properties the source properties
     * @param prefix     the prefix to search for
     * @return a set of unique discovered names
     */
    private Set<String> discoverNames(final Properties properties, final String prefix) {
        final var names = new TreeSet<String>();
        properties.forEach((rawKey, rawValue) -> {
            final var key = rawKey.toString();
            if (key.startsWith(prefix)) {
                final var remainder = key.substring(prefix.length());
                final var dotIndex = remainder.indexOf('.');
                if (dotIndex > 0) {
                    names.add(remainder.substring(0, dotIndex));
                }
            }
        });
        return names;
    }

    /**
     * Parses an integer property, returning a default if missing or malformed.
     *
     * @param properties   the source properties
     * @param key          the property key
     * @param defaultValue the default value
     * @return the parsed integer or the default
     */
    private int parseIntOrDefault(final Properties properties, final String key, final int defaultValue) {
        final var value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException numberFormatException) {
            LOGGER.warning("Invalid integer for key '" + key + "': '" + value
                    + "' — using default: " + defaultValue);
            return defaultValue;
        }
    }

    /**
     * Parses a boolean property, returning a default if missing.
     *
     * @param properties   the source properties
     * @param key          the property key
     * @param defaultValue the default value
     * @return the parsed boolean or the default
     */
    private boolean parseBooleanOrDefault(final Properties properties, final String key,
                                           final boolean defaultValue) {
        final var value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value.trim());
    }
}
