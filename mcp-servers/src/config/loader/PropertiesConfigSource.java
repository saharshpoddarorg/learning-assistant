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
 * hierarchical configuration. See {@code user-config/mcp-config.example.properties} for
 * the full key reference.
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

    /**
     * Creates a source backed by the given properties file.
     *
     * @param propertiesFilePath absolute or relative path to the {@code .properties} file
     */
    public PropertiesConfigSource(final Path propertiesFilePath) {
        this.propertiesFilePath = Objects.requireNonNull(propertiesFilePath,
                "Properties file path must not be null");
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
            throw new ConfigLoadException(
                    "Config file not found: " + propertiesFilePath.toAbsolutePath()
                            + ". Copy mcp-config.example.properties to mcp-config.properties and fill in your values.");
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
