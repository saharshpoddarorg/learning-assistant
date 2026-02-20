package config.loader;

import java.util.Properties;

/**
 * Abstraction for a configuration source.
 *
 * <p>Implementations load raw key-value configuration from different
 * backends (properties files, environment variables, etc.). The
 * {@link config.ConfigManager} composes multiple sources with a
 * defined precedence order.
 */
public interface ConfigSource {

    /**
     * Loads configuration data as a flat key-value map.
     *
     * <p>Keys use a dot-separated hierarchical convention, e.g.:
     * <pre>
     *   config.activeProfile=development
     *   apiKeys.github=ghp_xxxxx
     *   server.github.transport=stdio
     * </pre>
     *
     * @return a mutable {@link Properties} containing all loaded key-value pairs
     */
    Properties load();

    /**
     * Returns a human-readable name for this config source (used in error messages).
     *
     * @return the source name (e.g., "file: mcp-config.properties", "environment variables")
     */
    String sourceName();
}
