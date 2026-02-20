package config.model;

import java.util.Objects;

/**
 * General user preferences for the MCP configuration system.
 *
 * <p>Controls behavior settings like logging, retry policies, timeouts,
 * and UI preferences that apply across all MCP servers.
 *
 * @param theme          the UI theme preference (e.g., "dark", "light")
 * @param logLevel       the logging level (e.g., "INFO", "DEBUG", "WARN")
 * @param maxRetries     the maximum number of retry attempts for failed operations
 * @param timeoutSeconds the default timeout in seconds for server operations
 * @param autoConnect    whether to automatically connect to servers on startup
 */
public record UserPreferences(
        String theme,
        String logLevel,
        int maxRetries,
        int timeoutSeconds,
        boolean autoConnect
) {

    /** Sensible default preferences for new users. */
    public static final UserPreferences DEFAULT = new UserPreferences(
            "dark", "INFO", 3, 30, true
    );

    private static final int MIN_TIMEOUT_SECONDS = 1;
    private static final int MAX_TIMEOUT_SECONDS = 300;
    private static final int MAX_RETRIES_LIMIT = 10;

    /**
     * Creates a {@link UserPreferences} instance with validation.
     *
     * @param theme          the UI theme
     * @param logLevel       the logging level
     * @param maxRetries     retry count (must be 0–10)
     * @param timeoutSeconds timeout in seconds (must be 1–300)
     * @param autoConnect    whether to auto-connect on startup
     */
    public UserPreferences {
        Objects.requireNonNull(theme, "Theme must not be null");
        Objects.requireNonNull(logLevel, "Log level must not be null");

        if (maxRetries < 0 || maxRetries > MAX_RETRIES_LIMIT) {
            throw new IllegalArgumentException(
                    "maxRetries must be between 0 and " + MAX_RETRIES_LIMIT + ", got: " + maxRetries);
        }
        if (timeoutSeconds < MIN_TIMEOUT_SECONDS || timeoutSeconds > MAX_TIMEOUT_SECONDS) {
            throw new IllegalArgumentException(
                    "timeoutSeconds must be between " + MIN_TIMEOUT_SECONDS
                            + " and " + MAX_TIMEOUT_SECONDS + ", got: " + timeoutSeconds);
        }
    }
}
