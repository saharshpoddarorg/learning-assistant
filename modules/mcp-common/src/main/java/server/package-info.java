/**
 * Root package for all MCP server implementations.
 *
 * <h2>Structure</h2>
 * <pre>
 *   server/
 *   ├── {@link server.McpServer}          — Common contract for every server (lifecycle + tool dispatch)
 *   ├── {@link server.McpServerRegistry}  — Active server registry; supports version swapping
 *   ├── atlassian/                         — Atlassian gateway (Jira, Confluence, Bitbucket)
 *   └── learningresources/                 — Learning Resources discovery server
 * </pre>
 *
 * <h2>Versioning Architecture</h2>
 * <p>Each server family lives in its own sub-package. When a second version of a server
 * arrives it is placed in a sibling package following the {@code .v2} naming convention:
 * <pre>
 *   server.atlassian         ← v1 (stable, current)
 *   server.atlassian.v2      ← v2 (added when colleague's implementation arrives)
 * </pre>
 * <p>Both versions implement {@link server.McpServer}. The {@link server.McpServerRegistry}
 * selects the active version: the latest registered implementation wins.
 *
 * <h2>Adding a New Server</h2>
 * <ol>
 *   <li>Create a new sub-package, e.g. {@code server.github}</li>
 *   <li>Implement {@link server.McpServer} in the entry-point class</li>
 *   <li>Register the instance in {@code Main.java} via {@link server.McpServerRegistry#register(McpServer)}</li>
 * </ol>
 *
 * @see server.McpServer
 * @see server.McpServerRegistry
 */
package server;
