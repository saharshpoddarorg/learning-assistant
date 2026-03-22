package server.atlassian.v2.auth;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Persists OAuth tokens to a local file so they survive server restarts.
 *
 * <p>Tokens are stored in a simple properties file inside the user's
 * gitignored credential directory. The file is never committed.
 *
 * <p>Stored fields: {@code access_token}, {@code refresh_token},
 * {@code expires_at} (epoch seconds), {@code scope}.
 */
public final class CredentialStore {

    private static final Logger LOGGER = Logger.getLogger(CredentialStore.class.getName());
    private static final String STORE_FILE = "oauth-tokens.properties";

    private final Path storeDir;

    public CredentialStore(final Path storeDir) {
        this.storeDir = Objects.requireNonNull(storeDir, "Store directory must not be null");
    }

    /**
     * Saves OAuth tokens to the credential store.
     *
     * @param accessToken  the OAuth access token
     * @param refreshToken the OAuth refresh token (may be null)
     * @param expiresAt    the token expiry time
     * @param scope        the granted scopes
     * @throws IOException if writing fails
     */
    public void saveTokens(final String accessToken, final String refreshToken,
                           final Instant expiresAt, final String scope) throws IOException {
        Files.createDirectories(storeDir);
        final var props = new Properties();
        props.setProperty("access_token", accessToken);
        if (refreshToken != null) {
            props.setProperty("refresh_token", refreshToken);
        }
        props.setProperty("expires_at", String.valueOf(expiresAt.getEpochSecond()));
        if (scope != null) {
            props.setProperty("scope", scope);
        }
        props.setProperty("saved_at", Instant.now().toString());

        final var file = storeDir.resolve(STORE_FILE);
        try (var writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            props.store(writer, "Atlassian OAuth tokens — auto-generated, do not edit");
        }
        LOGGER.info("OAuth tokens saved to: " + file);
    }

    /**
     * Loads stored OAuth tokens.
     *
     * @return the stored tokens, or {@code null} if no tokens are stored
     */
    public StoredTokens loadTokens() {
        final var file = storeDir.resolve(STORE_FILE);
        if (!Files.exists(file)) {
            return null;
        }
        try (var reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            final var props = new Properties();
            props.load(reader);
            final var accessToken = props.getProperty("access_token");
            if (accessToken == null || accessToken.isBlank()) return null;

            return new StoredTokens(
                    accessToken,
                    props.getProperty("refresh_token"),
                    Instant.ofEpochSecond(Long.parseLong(
                            props.getProperty("expires_at", "0"))),
                    props.getProperty("scope", "")
            );
        } catch (IOException | NumberFormatException ex) {
            LOGGER.warning("Failed to load stored tokens: " + ex.getMessage());
            return null;
        }
    }

    /**
     * Deletes stored tokens (for logout).
     */
    public void clear() {
        final var file = storeDir.resolve(STORE_FILE);
        try {
            Files.deleteIfExists(file);
            LOGGER.info("Credential store cleared.");
        } catch (IOException ex) {
            LOGGER.warning("Failed to clear credential store: " + ex.getMessage());
        }
    }

    /**
     * Stored OAuth token set.
     */
    public record StoredTokens(String accessToken, String refreshToken,
                                Instant expiresAt, String scope) {

        public boolean isExpired() {
            return Instant.now().isAfter(expiresAt);
        }

        public boolean hasRefreshToken() {
            return refreshToken != null && !refreshToken.isBlank();
        }
    }
}
