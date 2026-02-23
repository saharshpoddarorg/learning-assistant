package server;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Central registry that manages the active {@link McpServer} instances.
 *
 * <p>The registry is the single source of truth for which server implementation
 * is currently active for a given server name. This makes it straightforward to
 * swap in a new version of a server (e.g., an improved {@code atlassian} v2)
 * without touching routing or startup code — just call {@link #register(McpServer)}
 * with the new implementation.
 *
 * <h2>Versioning Pattern</h2>
 * <p>Servers are keyed by {@link McpServer#name()} only. When a v2 of a server
 * arrives, the new implementation:
 * <ol>
 *   <li>Lives in a sibling package (e.g. {@code server.atlassian.v2})</li>
 *   <li>Returns the <em>same name</em> as v1 (e.g. {@code "atlassian"})</li>
 *   <li>Returns a higher {@link McpServer#version()} (e.g. {@code "2.0.0"})</li>
 *   <li>Is registered <em>after</em> v1 — it overwrites the active entry</li>
 * </ol>
 *
 * <pre>{@code
 * var registry = new McpServerRegistry();
 * registry.register(new AtlassianServer(config));      // v1 — active
 * // ... later, when colleague's v2 is available:
 * registry.register(new AtlassianServerV2(config));    // v2 overwrites v1
 * }</pre>
 *
 * <h2>Starting All Servers</h2>
 * <pre>{@code
 * registry.all().forEach(server -> new Thread(server::start).start());
 * }</pre>
 */
public final class McpServerRegistry {

    private static final Logger LOGGER = Logger.getLogger(McpServerRegistry.class.getName());

    /** Active server keyed by {@link McpServer#name()}. Insertion-ordered for predictable iteration. */
    private final Map<String, McpServer> activeServers = new LinkedHashMap<>();

    /**
     * Registers a server as the active implementation for its name.
     *
     * <p>If a server with the same name is already registered, it is replaced.
     * This is the intentional versioning mechanism: register a v2 implementation
     * to override v1 without changing any other code.
     *
     * @param server the server to register; must not be {@code null}
     * @return this registry, for fluent chaining
     * @throws NullPointerException if {@code server} is {@code null}
     */
    public McpServerRegistry register(final McpServer server) {
        Objects.requireNonNull(server, "server must not be null");
        final var previous = activeServers.put(server.name(), server);
        if (previous != null) {
            LOGGER.info("Replaced " + server.name() + " v" + previous.version()
                    + " → v" + server.version());
        } else {
            LOGGER.info("Registered " + server.name() + " v" + server.version());
        }
        return this;
    }

    /**
     * Returns the active server registered under the given name.
     *
     * @param name the server name (e.g. {@code "atlassian"})
     * @return an {@link Optional} containing the active server, or empty if none registered
     */
    public Optional<McpServer> find(final String name) {
        return Optional.ofNullable(activeServers.get(name));
    }

    /**
     * Returns an unmodifiable view of all currently active servers.
     *
     * <p>The iteration order matches the registration order (first registered = first).
     * If a server was replaced by a newer version, the newer version appears at
     * the <em>original</em> registration position.
     *
     * @return all registered servers, never {@code null}
     */
    public Collection<McpServer> all() {
        return Collections.unmodifiableCollection(activeServers.values());
    }

    /**
     * Returns {@code true} if no servers have been registered yet.
     *
     * @return {@code true} if the registry is empty
     */
    public boolean isEmpty() {
        return activeServers.isEmpty();
    }

    /**
     * Returns a short summary string listing registered server names and versions.
     *
     * @return human-readable registry summary
     */
    @Override
    public String toString() {
        if (activeServers.isEmpty()) {
            return "McpServerRegistry[empty]";
        }
        final var sb = new StringBuilder("McpServerRegistry[");
        activeServers.forEach((name, srv) ->
                sb.append(name).append("=v").append(srv.version()).append(", "));
        sb.setLength(sb.length() - 2); // trim trailing ", "
        sb.append("]");
        return sb.toString();
    }
}
