package config.loader;

import config.exception.ConfigLoadException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Properties;

/**
 * Loads configuration from a {@code .properties} file on disk.
 *
 * <p>Properties files use a flat, dot-separated key naming convention to represent
 * hierarchical configuration. See {@code user-config/mcp-config.properties} for
 * the full key reference.
 *
 * <p>When {@code optional} is set to {@code true}, a missing file is silently ignored
 * and an empty {@link Properties} is returned. This is used for the local overrides
 * file ({@code mcp-config.local.properties}) which only exists when the developer
 * has secrets or machine-specific settings to configure.
 *
 * <p>Example properties:
 * <pre>
 *   config.activeProfile=development
 *   apiKeys.github=ghp_xxxxx
 *   server.github.transport=stdio
 *   server.github.command=npx
 * </pre>
 */
public class PropertiesConfigSource implements ConfigSource {

    private final Path propertiesFilePath;
    private final boolean optional;

    /**
     * Creates a required source backed by the given properties file.
     * Throws {@link ConfigLoadException} if the file does not exist.
     *
     * @param propertiesFilePath absolute or relative path to the {@code .properties} file
     */
    public PropertiesConfigSource(final Path propertiesFilePath) {
        this(propertiesFilePath, false);
    }

    /**
     * Creates a source backed by the given properties file.
     *
     * @param propertiesFilePath absolute or relative path to the {@code .properties} file
     * @param optional           if {@code true}, returns empty properties when the file is
     *                           missing instead of throwing. Useful for the local overrides
     *                           file ({@code mcp-config.local.properties}) which may not exist.
     */
    public PropertiesConfigSource(final Path propertiesFilePath, final boolean optional) {
        this.propertiesFilePath = Objects.requireNonNull(propertiesFilePath,
                "Properties file path must not be null");
        this.optional = optional;
    }

    /**
     * Loads all key-value pairs from the properties file.
     *
     * @return a {@link Properties} populated from the file
     * @throws ConfigLoadException if the file cannot be found or read
     */
    @Override
    public Properties load() {
        if (!Files.exists(propertiesFilePath)) {
            if (optional) {
                return new Properties();
            }
            throw new ConfigLoadException(
                    "Config file not found: " + propertiesFilePath.toAbsolutePath()
                            + ". This is the base config file — it should be committed to version control.");
        }

        final var properties = new Properties();
        try (InputStream inputStream = Files.newInputStream(propertiesFilePath)) {
            properties.load(inputStream);
        } catch (IOException ioException) {
            throw new ConfigLoadException(
                    "Failed to read config file: " + propertiesFilePath.toAbsolutePath(), ioException);
        }

        return properties;
    }

    /**
     * Returns the source name including the file path.
     *
     * @return a descriptive source name
     */
    @Override
    public String sourceName() {
        return "file: " + propertiesFilePath;
    }
}
