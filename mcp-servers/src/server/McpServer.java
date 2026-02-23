package server;

import java.util.Map;

/**
 * Contract for every MCP server in this project.
 *
 * <p>Each implementation represents one logical MCP server that communicates
 * with an AI assistant over JSON-RPC 2.0 / STDIO transport. Implementations
 * expose a set of <em>tools</em> — named functions the AI can invoke — and
 * handle the full server lifecycle (start / stop).
 *
 * <h2>Design Rationale</h2>
 * <p>This interface intentionally stays thin. Every server in this project
 * has a different construction strategy (some need config files, others are
 * self-contained), so construction is deliberately left out of the contract.
 * What all servers share is:
 * <ul>
 *   <li>Stable identity — {@link #name()} + {@link #version()}</li>
 *   <li>Tool catalogue — {@link #toolDefinitions()} for discovery</li>
 *   <li>Tool dispatch — {@link #handleToolCall(String, Map)}</li>
 *   <li>Lifecycle — {@link #start()} / {@link #stop()}</li>
 * </ul>
 *
 * <h2>Versioning</h2>
 * <p>When a second version of a server is introduced the convention is:
 * <ul>
 *   <li>Original code stays in its current package (e.g. {@code server.atlassian})</li>
 *   <li>New version lives in a sibling package (e.g. {@code server.atlassian.v2})</li>
 *   <li>{@link McpServerRegistry} selects the active version via a simple name look-up</li>
 * </ul>
 * See {@code versioning-guide.md} in {@code .github/docs/} for the full guide.
 *
 * <h2>Implementing a New Server</h2>
 * <pre>{@code
 * public class MyServer implements McpServer {
 *
 *     @Override public String name()    { return "my-server"; }
 *     @Override public String version() { return "1.0.0"; }
 *
 *     @Override
 *     public Map<String, String> toolDefinitions() {
 *         return Map.of("my_tool", "Does something useful.");
 *     }
 *
 *     @Override
 *     public String handleToolCall(String toolName, Map<String, String> args) {
 *         return switch (toolName) {
 *             case "my_tool" -> runMyTool(args);
 *             default        -> "{\"error\":\"Unknown tool: " + toolName + "\"}";
 *         };
 *     }
 *
 *     @Override public void start() { /* STDIO loop *\/ }
 *     @Override public void stop()  { /* signal loop to exit *\/ }
 * }
 * }</pre>
 */
public interface McpServer {

    /**
     * Returns the stable machine-readable name of this server.
     *
     * <p>This value is used by the AI assistant to identify the server during
     * the MCP handshake. It must be unique within a running assistant session
     * and should use kebab-case (e.g. {@code "atlassian"}, {@code "learning-resources"}).
     *
     * @return the server name, never {@code null}
     */
    String name();

    /**
     * Returns the semantic version string (e.g. {@code "1.0.0"}).
     *
     * <p>Increment the minor version for backwards-compatible changes,
     * the major version for breaking changes.
     *
     * @return the version string, never {@code null}
     */
    String version();

    /**
     * Returns the tool catalogue exposed by this server.
     *
     * <p>Keys are tool names (snake_case, e.g. {@code "search_issues"});
     * values are human-readable descriptions that the AI uses to decide
     * which tool to invoke. This map is returned during the MCP
     * {@code tools/list} request.
     *
     * @return an unmodifiable map of tool name → description
     */
    Map<String, String> toolDefinitions();

    /**
     * Dispatches a tool call and returns the JSON result string.
     *
     * <p>Called for every {@code tools/call} JSON-RPC request received on stdin.
     * Implementations should return a valid JSON string (object or array).
     * On error, return a JSON error object rather than throwing an exception,
     * so the AI can handle the failure gracefully.
     *
     * @param toolName the name of the tool being invoked (e.g. {@code "search_issues"})
     * @param args     the tool arguments extracted from the JSON-RPC params
     * @return a JSON string containing the tool result or error
     */
    String handleToolCall(String toolName, Map<String, String> args);

    /**
     * Starts the server, typically entering a STDIO read loop.
     *
     * <p>This method blocks until {@link #stop()} is called or EOF is reached
     * on stdin.
     */
    void start();

    /**
     * Signals the server to stop gracefully.
     *
     * <p>Implementations should set a volatile flag so the {@link #start()}
     * loop exits cleanly on the next iteration.
     */
    void stop();
}
