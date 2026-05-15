package server.atlassian;

import server.McpServer;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * Factory that creates {@link McpServer} instances for a requested
 * {@link AtlassianServerVersion}.
 *
 * <p>This is the recommended entry point for any code that needs to start
 * an Atlassian MCP server without hard-coding a specific version. External
 * tooling can resolve the desired version from configuration and then call
 * {@link #create(AtlassianServerVersion)} to obtain a ready-to-start server.
 *
 * <h2>Deprecation Guard</h2>
 * <p>Deprecated versions are <em>still instantiable</em> via
 * {@link #create(AtlassianServerVersion)} — the factory logs a warning but
 * does not refuse. The {@link #createLatest()} convenience method always
 * returns the latest non-deprecated version.
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * // Always get the latest version
 * McpServer server = AtlassianServerFactory.createLatest();
 * server.start();
 *
 * // Explicitly request a specific version
 * McpServer v1 = AtlassianServerFactory.create(AtlassianServerVersion.V1);
 * }</pre>
 *
 * @see AtlassianServerVersion
 * @see server.McpServerRegistry
 */
public final class AtlassianServerFactory {

    private static final Logger LOGGER = Logger.getLogger(AtlassianServerFactory.class.getName());

    private AtlassianServerFactory() {
        // utility class — no instances
    }

    /**
     * Creates an {@link McpServer} for the given version.
     *
     * <p>If the version is deprecated a warning is logged. The returned server
     * is fully wired and ready for {@link McpServer#start()}.
     *
     * @param version the version to instantiate; must not be {@code null}
     * @return a ready-to-start server instance
     * @throws NullPointerException     if {@code version} is {@code null}
     * @throws IllegalStateException    if the server cannot be created
     */
    public static McpServer create(final AtlassianServerVersion version) {
        Objects.requireNonNull(version, "version must not be null");

        if (version.deprecated()) {
            LOGGER.warning("Creating deprecated Atlassian server " + version
                    + " — consider upgrading to " + AtlassianServerVersion.latest());
        }

        LOGGER.info("Creating Atlassian server " + version);

        return switch (version) {
            case V1 -> createV1();
            case V2 -> createV2();
        };
    }

    /**
     * Convenience method that creates the latest non-deprecated version.
     *
     * @return a ready-to-start server for the latest version
     * @see AtlassianServerVersion#latest()
     */
    public static McpServer createLatest() {
        return create(AtlassianServerVersion.latest());
    }

    /**
     * Resolves a version from a string key (enum name or semantic version)
     * and creates the corresponding server.
     *
     * <p>Accepts formats like {@code "V2"}, {@code "v2"}, or {@code "2.0.0"}.
     *
     * @param versionKey the version identifier
     * @return a ready-to-start server
     * @throws IllegalArgumentException if the key cannot be resolved
     */
    public static McpServer createFromKey(final String versionKey) {
        Objects.requireNonNull(versionKey, "versionKey must not be null");

        final var version = AtlassianServerVersion.fromKey(versionKey)
                .or(() -> AtlassianServerVersion.fromVersion(versionKey))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown Atlassian server version: " + versionKey
                                + ". Available: " + java.util.Arrays.toString(AtlassianServerVersion.values())));

        return create(version);
    }

    // ── private wiring methods ──────────────────────────────────────────

    private static McpServer createV1() {
        return server.atlassian.v1.AtlassianServerV1.wireServer();
    }

    private static McpServer createV2() {
        return server.atlassian.v2.AtlassianServerV2.wireServer();
    }
}
