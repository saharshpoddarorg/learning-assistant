package config.model;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * A named configuration profile that groups related settings.
 *
 * <p>Profiles allow switching between configurations for different environments
 * (e.g., development, production, testing) or different use cases. Each profile
 * can override the global preferences and server definitions.
 *
 * @param name                the profile name (e.g., "development", "production")
 * @param description         a human-readable description of the profile's purpose
 * @param preferences         user preferences specific to this profile
 * @param locationPreferences location overrides for this profile
 * @param browserPreferences  browser overrides for this profile
 * @param serverOverrides     server definitions that override or extend the base config
 */
public record ProfileDefinition(
        String name,
        String description,
        UserPreferences preferences,
        LocationPreferences locationPreferences,
        BrowserPreferences browserPreferences,
        Map<String, ServerDefinition> serverOverrides
) {

    /**
     * Creates a {@link ProfileDefinition} with validation.
     *
     * @param name                the profile name
     * @param description         profile description
     * @param preferences         user preferences for this profile
     * @param locationPreferences location settings for this profile
     * @param browserPreferences  browser settings for this profile
     * @param serverOverrides     server overrides (defensively copied)
     */
    public ProfileDefinition {
        Objects.requireNonNull(name, "Profile name must not be null");
        Objects.requireNonNull(description, "Profile description must not be null");
        Objects.requireNonNull(preferences, "Preferences must not be null");
        Objects.requireNonNull(locationPreferences, "Location preferences must not be null");
        Objects.requireNonNull(browserPreferences, "Browser preferences must not be null");
        Objects.requireNonNull(serverOverrides, "Server overrides must not be null (use empty map)");

        serverOverrides = Collections.unmodifiableMap(Map.copyOf(serverOverrides));
    }

    /**
     * Checks whether this profile has a server override for the given server name.
     *
     * @param serverName the server identifier to look up
     * @return {@code true} if an override exists
     */
    public boolean hasServerOverride(final String serverName) {
        return serverOverrides.containsKey(serverName);
    }
}
