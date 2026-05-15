package server.atlassian;

import java.util.Arrays;
import java.util.Optional;

/**
 * Enumerates the known versions of the Atlassian MCP server.
 *
 * <p>Each constant captures the metadata needed to select, instantiate, and
 * describe a specific server generation. The enum is the single source of
 * truth for version identity — external configuration (mcp.json, server.ps1)
 * and the {@link AtlassianServerFactory} both delegate to it.
 *
 * <h2>Deprecation Model</h2>
 * <p>A deprecated version still compiles and runs — it is not removed. The
 * {@link #deprecated} flag is advisory: tooling can warn operators, and the
 * factory can refuse to create deprecated instances unless explicitly requested.
 *
 * <h2>Adding a New Version</h2>
 * <ol>
 *   <li>Add a new constant (e.g. {@code V3}) with an incremented major version.</li>
 *   <li>Implement the server class in a new sibling package ({@code server.atlassian.v3}).</li>
 *   <li>Update {@link AtlassianServerFactory} to wire the new version.</li>
 *   <li>Optionally mark the previous version as deprecated.</li>
 * </ol>
 *
 * @see AtlassianServerFactory
 * @see server.McpServer
 */
public enum AtlassianServerVersion {

    /**
     * Version 1 — the original 27-tool server with API token / PAT authentication.
     *
     * <p>Deprecated in favour of {@link #V2}. Still functional; kept for
     * backwards compatibility.
     *
     * @see server.atlassian.v1.AtlassianServerV1
     */
    V1("1.0.0",
       true,
       "server.atlassian.v1.AtlassianServerV1",
       "Original 27-tool server with API token/PAT auth"),

    /**
     * Version 2 — comprehensive 93-tool server with dual authentication
     * (OAuth 2.0 + API token / PAT).
     *
     * @see server.atlassian.v2.AtlassianServerV2
     */
    V2("2.0.0",
       false,
       "server.atlassian.v2.AtlassianServerV2",
       "Comprehensive 93-tool server with OAuth 2.0 + API token/PAT auth");

    // ── fields ──────────────────────────────────────────────────────────

    private final String version;
    private final boolean deprecated;
    private final String mainClass;
    private final String description;

    AtlassianServerVersion(final String version,
                           final boolean deprecated,
                           final String mainClass,
                           final String description) {
        this.version     = version;
        this.deprecated  = deprecated;
        this.mainClass   = mainClass;
        this.description = description;
    }

    // ── accessors ───────────────────────────────────────────────────────

    /** Semantic version string (e.g. {@code "2.0.0"}). */
    public String version()     { return version; }

    /** {@code true} if this version is deprecated and should not be used for new deployments. */
    public boolean deprecated() { return deprecated; }

    /**
     * Fully-qualified class name of the server entry point.
     *
     * <p>Useful for reflective instantiation or for external tooling that
     * needs to launch a specific version (e.g. {@code server.ps1}).
     */
    public String mainClass()   { return mainClass; }

    /** Human-readable summary of this version's capabilities. */
    public String description() { return description; }

    /** Major version number extracted from the semantic version string. */
    public int major() {
        return Integer.parseInt(version.split("\\.")[0]);
    }

    // ── lookups ─────────────────────────────────────────────────────────

    /**
     * Returns the latest non-deprecated version.
     *
     * <p>If all versions are deprecated the most recent one is returned.
     */
    public static AtlassianServerVersion latest() {
        final var values = values();
        for (int i = values.length - 1; i >= 0; i--) {
            if (!values[i].deprecated) {
                return values[i];
            }
        }
        return values[values.length - 1];
    }

    /**
     * Resolves a version by its semantic version string (e.g. {@code "2.0.0"}).
     *
     * @param versionString the version to look up
     * @return the matching version, or empty if not found
     */
    public static Optional<AtlassianServerVersion> fromVersion(final String versionString) {
        return Arrays.stream(values())
                     .filter(v -> v.version.equals(versionString))
                     .findFirst();
    }

    /**
     * Resolves a version by its enum key (e.g. {@code "V1"}, {@code "V2"}).
     *
     * <p>Case-insensitive for convenience.
     *
     * @param key the enum constant name
     * @return the matching version, or empty if not found
     */
    public static Optional<AtlassianServerVersion> fromKey(final String key) {
        try {
            return Optional.of(valueOf(key.toUpperCase()));
        } catch (final IllegalArgumentException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public String toString() {
        return name() + " (" + version + ")" + (deprecated ? " [DEPRECATED]" : "");
    }
}
