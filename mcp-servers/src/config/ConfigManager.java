package config;

import config.exception.ConfigLoadException;
import config.exception.ConfigValidationException;
import config.loader.ConfigParser;
import config.loader.ConfigSource;
import config.loader.EnvironmentConfigSource;
import config.loader.PropertiesConfigSource;
import config.model.BrowserPreferences;
import config.model.LocationPreferences;
import config.model.McpConfiguration;
import config.model.UserPreferences;
import config.validation.ConfigValidator;
import config.validation.ValidationResult;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Facade for loading, merging, validating, and resolving MCP configuration.
 *
 * <p>The manager orchestrates the full configuration lifecycle:
 * <ol>
 *   <li><strong>Load</strong> — reads from one or more {@link ConfigSource}s (properties file, env vars)</li>
 *   <li><strong>Merge</strong> — combines sources with env vars taking highest precedence</li>
 *   <li><strong>Parse</strong> — converts flat properties into the {@link McpConfiguration} model</li>
 *   <li><strong>Validate</strong> — checks the configuration for correctness</li>
 *   <li><strong>Resolve</strong> — applies the active profile's overrides to produce the effective config</li>
 * </ol>
 *
 * <p><strong>Precedence (highest wins):</strong>
 * <pre>
 *   1. Environment variables (MCP_* prefix)
 *   2. Profile-specific overrides
 *   3. Base properties file (mcp-config.properties)
 * </pre>
 *
 * <p><strong>Usage:</strong>
 * <pre>
 *   var configManager = ConfigManager.fromDefaults();
 *   var config = configManager.loadAndValidate();
 *   var effectiveConfig = configManager.resolveEffectiveConfig(config);
 * </pre>
 */
public class ConfigManager {

    private static final Logger LOGGER = Logger.getLogger(ConfigManager.class.getName());

    private static final String DEFAULT_CONFIG_FILE = "user-config/mcp-config.properties";
    private static final String LOCAL_CONFIG_FILE = "user-config/mcp-config.local.properties";

    private final List<ConfigSource> sources;
    private final ConfigParser parser;
    private final ConfigValidator validator;

    /**
     * Creates a {@link ConfigManager} with the given sources, parser, and validator.
     *
     * @param sources   ordered list of config sources (later sources override earlier)
     * @param parser    the parser to convert properties into model objects
     * @param validator the validator to check configuration correctness
     */
    public ConfigManager(final List<ConfigSource> sources,
                         final ConfigParser parser,
                         final ConfigValidator validator) {
        this.sources = Objects.requireNonNull(sources, "Config sources must not be null");
        this.parser = Objects.requireNonNull(parser, "Config parser must not be null");
        this.validator = Objects.requireNonNull(validator, "Config validator must not be null");
    }

    /**
     * Creates a {@link ConfigManager} with default settings:
     * base properties file + environment variable overlay.
     *
     * @return a pre-configured manager ready to load
     */
    public static ConfigManager fromDefaults() {
        return fromConfigFile(Path.of(DEFAULT_CONFIG_FILE));
    }

    /**
     * Creates a {@link ConfigManager} targeting a specific base properties file.
     *
     * <p>Configuration is loaded in layers (highest precedence wins):
     * <ol>
     *   <li>Base properties file — committed defaults (required)</li>
     *   <li>Local properties file — developer secrets/overrides (optional, gitignored)</li>
     *   <li>Environment variables — {@code MCP_*} prefix (highest precedence)</li>
     * </ol>
     *
     * @param configFilePath the path to the base properties file
     * @return a configured manager
     */
    public static ConfigManager fromConfigFile(final Path configFilePath) {
        final var localFilePath = configFilePath.resolveSibling("mcp-config.local.properties");
        final var sources = List.<ConfigSource>of(
                new PropertiesConfigSource(configFilePath),
                new PropertiesConfigSource(localFilePath, true),
                new EnvironmentConfigSource()
        );
        return new ConfigManager(sources, new ConfigParser(), new ConfigValidator());
    }

    /**
     * Loads, merges, parses, and validates the configuration.
     *
     * @return the validated {@link McpConfiguration}
     * @throws ConfigLoadException       if any source fails to load
     * @throws ConfigValidationException if validation finds errors
     */
    public McpConfiguration loadAndValidate() {
        final var mergedProperties = loadAndMerge();
        final var configuration = parser.parse(mergedProperties);

        final var validationResult = validator.validate(configuration);
        if (!validationResult.isValid()) {
            LOGGER.severe(validationResult.formatReport());
            throw new ConfigValidationException(validationResult);
        }

        LOGGER.info("MCP configuration loaded successfully with "
                + configuration.servers().size() + " server(s) and "
                + configuration.profiles().size() + " profile(s).");

        return configuration;
    }

    /**
     * Loads without validation — useful for debugging or inspecting raw config.
     *
     * @return the parsed (but unvalidated) {@link McpConfiguration}
     * @throws ConfigLoadException if any source fails to load
     */
    public McpConfiguration loadWithoutValidation() {
        final var mergedProperties = loadAndMerge();
        return parser.parse(mergedProperties);
    }

    /**
     * Validates an already-loaded configuration.
     *
     * @param configuration the configuration to validate
     * @return the validation result
     */
    public ValidationResult validate(final McpConfiguration configuration) {
        return validator.validate(configuration);
    }

    /**
     * Resolves the effective configuration by applying the active profile's overrides.
     *
     * <p>If no profile is active, the base configuration is returned as-is.
     * Profile overrides merge server definitions — profile servers replace
     * base servers with the same name, and new profile servers are added.
     *
     * @param baseConfig the base configuration (typically from {@link #loadAndValidate()})
     * @return the effective configuration with profile overrides applied
     */
    public McpConfiguration resolveEffectiveConfig(final McpConfiguration baseConfig) {
        Objects.requireNonNull(baseConfig, "Base configuration must not be null");

        if (!baseConfig.hasActiveProfile()) {
            LOGGER.info("No active profile — using base configuration as-is.");
            return baseConfig;
        }

        final var profile = baseConfig.resolveActiveProfile();
        LOGGER.info("Applying profile: " + profile.name());

        // Merge servers: profile overrides take precedence
        final var effectiveServers = new HashMap<>(baseConfig.servers());
        effectiveServers.putAll(profile.serverOverrides());

        // Use profile preferences and location if they differ from defaults
        final var effectivePreferences = resolvePreferences(baseConfig.preferences(), profile.preferences());
        final var effectiveLocation = resolveLocation(baseConfig.location(), profile.locationPreferences());
        final var effectiveBrowser = resolveBrowser(baseConfig.browser(), profile.browserPreferences());

        return new McpConfiguration(
                baseConfig.activeProfile(),
                baseConfig.apiKeys(),
                effectiveLocation,
                effectivePreferences,
                effectiveBrowser,
                effectiveServers,
                baseConfig.profiles()
        );
    }

    /**
     * Loads all sources and merges their properties (later sources win on conflicts).
     *
     * @return the merged properties
     */
    private Properties loadAndMerge() {
        final var merged = new Properties();

        for (final var source : sources) {
            LOGGER.fine("Loading config from: " + source.sourceName());
            final var sourceProperties = source.load();
            merged.putAll(sourceProperties);
        }

        return merged;
    }

    /**
     * Resolves preferences — uses profile values over base when they differ from defaults.
     *
     * @param base    the base preferences
     * @param profile the profile preferences
     * @return the effective preferences
     */
    private UserPreferences resolvePreferences(final UserPreferences base, final UserPreferences profile) {
        // If the profile has non-default values, use them; otherwise keep base
        final var theme = isNonDefault(profile.theme(), UserPreferences.DEFAULT.theme())
                ? profile.theme() : base.theme();
        final var logLevel = isNonDefault(profile.logLevel(), UserPreferences.DEFAULT.logLevel())
                ? profile.logLevel() : base.logLevel();
        final var maxRetries = profile.maxRetries() != UserPreferences.DEFAULT.maxRetries()
                ? profile.maxRetries() : base.maxRetries();
        final var timeoutSeconds = profile.timeoutSeconds() != UserPreferences.DEFAULT.timeoutSeconds()
                ? profile.timeoutSeconds() : base.timeoutSeconds();
        final var autoConnect = profile.autoConnect() != UserPreferences.DEFAULT.autoConnect()
                ? profile.autoConnect() : base.autoConnect();

        return new UserPreferences(theme, logLevel, maxRetries, timeoutSeconds, autoConnect);
    }

    /**
     * Resolves location preferences — uses profile values over base when they differ from defaults.
     *
     * @param base    the base location preferences
     * @param profile the profile location preferences
     * @return the effective location preferences
     */
    private LocationPreferences resolveLocation(final LocationPreferences base,
                                                 final LocationPreferences profile) {
        final var timezone = isNonDefault(profile.timezone(), LocationPreferences.DEFAULT.timezone())
                ? profile.timezone() : base.timezone();
        final var locale = isNonDefault(profile.locale(), LocationPreferences.DEFAULT.locale())
                ? profile.locale() : base.locale();
        final var region = isNonDefault(profile.region(), LocationPreferences.DEFAULT.region())
                ? profile.region() : base.region();

        return new LocationPreferences(timezone, locale, region);
    }

    /**
     * Checks if a value differs from its default.
     *
     * @param value        the actual value
     * @param defaultValue the default value
     * @return {@code true} if the value is not the default
     */
    private boolean isNonDefault(final String value, final String defaultValue) {
        return !value.equals(defaultValue);
    }

    /**
     * Resolves browser preferences — uses profile values over base when they differ from defaults.
     *
     * @param base    the base browser preferences
     * @param profile the profile browser preferences
     * @return the effective browser preferences
     */
    private BrowserPreferences resolveBrowser(final BrowserPreferences base,
                                               final BrowserPreferences profile) {
        final var executable = isNonDefault(profile.browserExecutable(),
                BrowserPreferences.DEFAULT.browserExecutable())
                ? profile.browserExecutable() : base.browserExecutable();
        final var browserProfile = isNonDefault(profile.browserProfile(),
                BrowserPreferences.DEFAULT.browserProfile())
                ? profile.browserProfile() : base.browserProfile();
        final var account = isNonDefault(profile.browserAccount(),
                BrowserPreferences.DEFAULT.browserAccount())
                ? profile.browserAccount() : base.browserAccount();
        final var launchMode = isNonDefault(profile.launchMode(),
                BrowserPreferences.DEFAULT.launchMode())
                ? profile.launchMode() : base.launchMode();
        final var headless = profile.headless() != BrowserPreferences.DEFAULT.headless()
                ? profile.headless() : base.headless();
        final var debugPort = profile.remoteDebuggingPort() != BrowserPreferences.DEFAULT.remoteDebuggingPort()
                ? profile.remoteDebuggingPort() : base.remoteDebuggingPort();
        final var width = profile.windowWidth() != BrowserPreferences.DEFAULT.windowWidth()
                ? profile.windowWidth() : base.windowWidth();
        final var height = profile.windowHeight() != BrowserPreferences.DEFAULT.windowHeight()
                ? profile.windowHeight() : base.windowHeight();

        return new BrowserPreferences(executable, browserProfile, account, launchMode,
                headless, debugPort, width, height);
    }
}
