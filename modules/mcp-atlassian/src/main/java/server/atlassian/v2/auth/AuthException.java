package server.atlassian.v2.auth;

/**
 * Exception thrown when authentication fails.
 */
public class AuthException extends Exception {

    public AuthException(final String message) {
        super(message);
    }

    public AuthException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
