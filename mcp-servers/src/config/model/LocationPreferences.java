package config.model;

import java.util.Objects;

/**
 * Location and locale preferences for MCP server operations.
 *
 * <p>Used to configure timezone, locale, and cloud region for services
 * that need geographic or locale awareness.
 *
 * @param timezone the IANA timezone identifier (e.g., "America/New_York")
 * @param locale   the BCP 47 locale tag (e.g., "en-US")
 * @param region   the cloud/infrastructure region (e.g., "us-east-1")
 */
public record LocationPreferences(String timezone, String locale, String region) {

    /** Default location preferences using UTC and English (US). */
    public static final LocationPreferences DEFAULT = new LocationPreferences("UTC", "en-US", "");

    /**
     * Creates a {@link LocationPreferences} instance.
     *
     * @param timezone the IANA timezone identifier
     * @param locale   the BCP 47 locale tag
     * @param region   the cloud/infrastructure region (empty string if not applicable)
     */
    public LocationPreferences {
        Objects.requireNonNull(timezone, "Timezone must not be null");
        Objects.requireNonNull(locale, "Locale must not be null");
        Objects.requireNonNull(region, "Region must not be null");
    }
}
