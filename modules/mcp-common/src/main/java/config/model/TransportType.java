package config.model;

/**
 * Supported MCP transport mechanisms.
 *
 * <p>Each transport type defines how the MCP client communicates with the MCP server:
 * <ul>
 *   <li>{@link #STDIO} — Standard input/output (most common for local servers)</li>
 *   <li>{@link #SSE} — Server-Sent Events over HTTP</li>
 *   <li>{@link #STREAMABLE_HTTP} — Streamable HTTP (newer MCP transport)</li>
 * </ul>
 */
public enum TransportType {

    /** Standard input/output — launches a subprocess and communicates via stdin/stdout. */
    STDIO("stdio"),

    /** Server-Sent Events — connects to an HTTP endpoint with SSE streaming. */
    SSE("sse"),

    /** Streamable HTTP — newer HTTP-based transport with bidirectional streaming. */
    STREAMABLE_HTTP("streamable-http");

    private final String configValue;

    TransportType(final String configValue) {
        this.configValue = configValue;
    }

    /**
     * Returns the config-file-friendly string representation.
     *
     * @return the transport type as a lowercase string for config files
     */
    public String getConfigValue() {
        return configValue;
    }

    /**
     * Resolves a {@link TransportType} from a config string value.
     *
     * @param value the string value from a config file (case-insensitive)
     * @return the matching {@link TransportType}
     * @throws IllegalArgumentException if no matching transport type is found
     */
    public static TransportType fromConfigValue(final String value) {
        for (final TransportType type : values()) {
            if (type.configValue.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown transport type: " + value
                + ". Supported values: stdio, sse, streamable-http");
    }
}
