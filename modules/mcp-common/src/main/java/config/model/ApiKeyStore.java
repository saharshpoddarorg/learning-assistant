package config.model;

import java.util.Map;
import java.util.Objects;

/**
 * Stores API keys for external services used by MCP servers.
 *
 * <p>Keys are stored as a name-to-value map where the name identifies
 * the service (e.g., "github", "openai", "database") and the value
 * is the corresponding API key or token.
 *
 * @param keys unmodifiable map of service name to API key
 */
public record ApiKeyStore(Map<String, String> keys) {

    /** An empty store with no API keys configured. */
    public static final ApiKeyStore EMPTY = new ApiKeyStore(Map.of());

    /**
     * Creates an {@link ApiKeyStore} with the given keys.
     *
     * @param keys map of service name to API key (defensively copied)
     */
    public ApiKeyStore {
        Objects.requireNonNull(keys, "API keys map must not be null");
        keys = Map.copyOf(keys);
    }

    /**
     * Retrieves the API key for a specific service.
     *
     * @param serviceName the service identifier (e.g., "github")
     * @return the API key, or {@code null} if not configured
     */
    public String getKey(final String serviceName) {
        return keys.get(serviceName);
    }

    /**
     * Checks whether an API key exists for the given service.
     *
     * @param serviceName the service identifier
     * @return {@code true} if a key is configured for the service
     */
    public boolean hasKey(final String serviceName) {
        return keys.containsKey(serviceName);
    }

    /**
     * Returns the number of configured API keys.
     *
     * @return the count of stored keys
     */
    public int size() {
        return keys.size();
    }
}
