package config.model;

import java.util.Map;
import java.util.Objects;

/**
 * Root configuration record for the entire MCP servers system.
 *
 * <p>Aggregates all configuration concerns into a single immutable structure:
 * <ul>
 *   <li><strong>Active profile</strong> — which named profile is currently in use</li>
 *   <li><strong>API keys</strong> — credentials for external services</li>
 *   <li><strong>Location</strong> — timezone, locale, region preferences</li>
 *   <li><strong>User preferences</strong> — theme, logging, timeouts, etc.</li>
 *   <li><strong>Browser preferences</strong> — browser executable, profile, launch mode</li>
 *   <li><strong>Server definitions</strong> — map of server name to its config</li>
 *   <li><strong>Profiles</strong> — named sets of overrides for different environments</li>
 * </ul>
 *
 * @param activeProfile the name of the currently active profile (empty string if none)
 * @param apiKeys       the API key store for external services
 * @param location      location and locale preferences
 * @param preferences   general user preferences
 * @param browser       browser preferences for MCP servers that launch a browser
 * @param servers       map of server name to server definition
 * @param profiles      map of profile name to profile definition
 */
public record McpConfiguration(
        String activeProfile,
        ApiKeyStore apiKeys,
        LocationPreferences location,
        UserPreferences preferences,
        BrowserPreferences browser,
        Map<String, ServerDefinition> servers,
        Map<String, ProfileDefinition> profiles
) {

    /**
     * Creates an {@link McpConfiguration} with validation and defensive copying.
     *
     * @param activeProfile the active profile name
     * @param apiKeys       API key store
     * @param location      location preferences
     * @param preferences   user preferences
     * @param browser       browser preferences
     * @param servers       server definitions (defensively copied)
     * @param profiles      profile definitions (defensively copied)
     */
    public McpConfiguration {
        Objects.requireNonNull(activeProfile, "Active profile must not be null (use empty string for none)");
        Objects.requireNonNull(apiKeys, "API keys must not be null");
        Objects.requireNonNull(location, "Location preferences must not be null");
        Objects.requireNonNull(preferences, "User preferences must not be null");
        Objects.requireNonNull(browser, "Browser preferences must not be null");
        Objects.requireNonNull(servers, "Servers map must not be null (use empty map)");
        Objects.requireNonNull(profiles, "Profiles map must not be null (use empty map)");

        servers = Map.copyOf(servers);
        profiles = Map.copyOf(profiles);
    }

    /**
     * Retrieves a server definition by name.
     *
     * @param serverName the server identifier
     * @return the server definition, or {@code null} if not found
     */
    public ServerDefinition getServer(final String serverName) {
        return servers.get(serverName);
    }

    /**
     * Retrieves a profile definition by name.
     *
     * @param profileName the profile identifier
     * @return the profile definition, or {@code null} if not found
     */
    public ProfileDefinition getProfile(final String profileName) {
        return profiles.get(profileName);
    }

    /**
     * Returns the currently active profile definition, if one is set.
     *
     * @return the active profile, or {@code null} if no profile is active
     */
    public ProfileDefinition resolveActiveProfile() {
        if (activeProfile.isEmpty()) {
            return null;
        }
        return profiles.get(activeProfile);
    }

    /**
     * Checks whether a named profile is currently active.
     *
     * @return {@code true} if an active profile is configured
     */
    public boolean hasActiveProfile() {
        return !activeProfile.isEmpty() && profiles.containsKey(activeProfile);
    }
}
