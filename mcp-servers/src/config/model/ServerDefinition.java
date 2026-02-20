package config.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Defines the configuration for an individual MCP server.
 *
 * <p>Each server definition includes the server identity, transport settings,
 * connection details, and optional environment variables passed to the server process.
 *
 * @param name          the unique identifier / display name for this server
 * @param enabled       whether this server should be started
 * @param transport     the transport mechanism (stdio, sse, streamable-http)
 * @param command       the command to launch the server (for stdio transport)
 * @param args          command-line arguments passed to the server
 * @param url           the server URL (for SSE / Streamable HTTP transports)
 * @param environmentVariables  environment variables injected into the server process
 */
public record ServerDefinition(
        String name,
        boolean enabled,
        TransportType transport,
        String command,
        List<String> args,
        String url,
        Map<String, String> environmentVariables
) {

    /**
     * Creates a {@link ServerDefinition} with validation and defensive copying.
     *
     * @param name          unique server name
     * @param enabled       whether the server is active
     * @param transport     transport type
     * @param command       launch command (required for STDIO, empty for HTTP-based)
     * @param args          command arguments (defensively copied)
     * @param url           server URL (required for SSE/HTTP, empty for STDIO)
     * @param environmentVariables env vars for the server process (defensively copied)
     */
    public ServerDefinition {
        Objects.requireNonNull(name, "Server name must not be null");
        Objects.requireNonNull(transport, "Transport type must not be null");
        Objects.requireNonNull(command, "Command must not be null (use empty string if not applicable)");
        Objects.requireNonNull(args, "Args list must not be null (use empty list if none)");
        Objects.requireNonNull(url, "URL must not be null (use empty string if not applicable)");
        Objects.requireNonNull(environmentVariables, "Environment variables must not be null (use empty map)");

        args = Collections.unmodifiableList(List.copyOf(args));
        environmentVariables = Collections.unmodifiableMap(Map.copyOf(environmentVariables));
    }

    /**
     * Checks whether this server uses a subprocess-based transport (STDIO).
     *
     * @return {@code true} if the transport is STDIO
     */
    public boolean isSubprocessBased() {
        return transport == TransportType.STDIO;
    }

    /**
     * Checks whether this server uses an HTTP-based transport (SSE or Streamable HTTP).
     *
     * @return {@code true} if the transport is SSE or STREAMABLE_HTTP
     */
    public boolean isHttpBased() {
        return transport == TransportType.SSE || transport == TransportType.STREAMABLE_HTTP;
    }
}
