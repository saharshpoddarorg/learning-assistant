package config.exception;

/**
 * Thrown when configuration cannot be loaded from its source.
 *
 * <p>This covers file-not-found, malformed content, I/O errors,
 * and other issues that prevent reading the configuration.
 */
public class ConfigLoadException extends RuntimeException {

    /**
     * Creates a new config load exception with a descriptive message.
     *
     * @param message description of what went wrong during loading
     */
    public ConfigLoadException(final String message) {
        super(message);
    }

    /**
     * Creates a new config load exception with a message and root cause.
     *
     * @param message description of what went wrong during loading
     * @param cause   the underlying exception that caused the load failure
     */
    public ConfigLoadException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
