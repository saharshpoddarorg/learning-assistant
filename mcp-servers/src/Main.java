import config.ConfigManager;
import config.exception.ConfigLoadException;
import config.exception.ConfigValidationException;
import config.model.McpConfiguration;

import java.util.logging.Logger;

/**
 * Entry point for the MCP Servers application.
 *
 * <p>Loads and validates the MCP configuration, then prints
 * a summary of the resolved effective configuration.
 */
public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    /**
     * Loads configuration, resolves the active profile, and prints a summary.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(final String[] args) {
        LOGGER.info("Starting MCP Servers...");

        try {
            final var configManager = ConfigManager.fromDefaults();
            final var baseConfig = configManager.loadAndValidate();
            final var effectiveConfig = configManager.resolveEffectiveConfig(baseConfig);

            printConfigSummary(effectiveConfig);
        } catch (ConfigLoadException configLoadException) {
            LOGGER.severe("Failed to load configuration: " + configLoadException.getMessage());
        } catch (ConfigValidationException configValidationException) {
            LOGGER.severe(configValidationException.getMessage());
        }
    }

    /**
     * Prints a human-readable summary of the effective configuration.
     *
     * @param config the resolved configuration to summarize
     */
    private static void printConfigSummary(final McpConfiguration config) {
        final var summary = new StringBuilder("\n")
                .append("=== MCP Configuration Summary ===\n")
                .append("Active Profile : ").append(
                        config.activeProfile().isEmpty() ? "(none)" : config.activeProfile()).append("\n")
                .append("Timezone       : ").append(config.location().timezone()).append("\n")
                .append("Locale         : ").append(config.location().locale()).append("\n")
                .append("Log Level      : ").append(config.preferences().logLevel()).append("\n")
                .append("Timeout        : ").append(config.preferences().timeoutSeconds()).append("s\n")
                .append("Browser        : ").append(
                        config.browser().hasCustomBrowser()
                                ? config.browser().browserExecutable() : "(system default)").append("\n")
                .append("Browser Profile: ").append(
                        config.browser().hasIsolatedProfile()
                                ? config.browser().browserProfile() : "(default)").append("\n")
                .append("Launch Mode    : ").append(config.browser().launchMode()).append("\n")
                .append("API Keys       : ").append(config.apiKeys().size()).append(" configured\n")
                .append("Servers        : ").append(config.servers().size()).append(" defined\n");

        config.servers().forEach((name, server) ->
                summary.append("  - ").append(name)
                        .append(" [").append(server.transport().getConfigValue()).append("]")
                        .append(server.enabled() ? " (enabled)" : " (disabled)")
                        .append("\n"));

        summary.append("=================================\n");
        LOGGER.info(summary.toString());
    }
}