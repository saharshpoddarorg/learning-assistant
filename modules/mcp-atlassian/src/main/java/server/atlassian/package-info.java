/**
 * Atlassian MCP Server family — unified gateway to Jira, Confluence, and
 * Bitbucket through the Model Context Protocol.
 *
 * <p>This top-level package hosts the <strong>versioning framework</strong>
 * that manages multiple server generations. Version-specific code lives in
 * sibling sub-packages; shared utilities live in {@code common/}.
 *
 * <h2>Package Structure</h2>
 * <pre>
 *   server.atlassian
 *   ├── {@link server.atlassian.AtlassianServerVersion}   — version enum (V1, V2, …)
 *   ├── {@link server.atlassian.AtlassianServerFactory}   — version-aware factory
 *   ├── common/                                           — shared cross-version utilities
 *   │   └── {@link server.atlassian.common.JsonExtractor} — JSON field extraction helper
 *   ├── v1/                                               — version 1 (27 tools, API token/PAT)
 *   │   └── {@link server.atlassian.v1.AtlassianServerV1} — v1 entry point [DEPRECATED]
 *   └── v2/                                               — version 2 (93 tools, OAuth 2.0 + token)
 *       └── {@link server.atlassian.v2.AtlassianServerV2} — v2 entry point
 * </pre>
 *
 * <h2>Versioning Model</h2>
 * <p>Each version is a self-contained implementation of {@link server.McpServer}
 * with its own client, handler, model, and config layers. Versions are true
 * siblings — neither depends on the other. The only shared dependency is the
 * {@code common} package.
 *
 * <p>Use {@link AtlassianServerVersion} to enumerate available versions and
 * check deprecation status. Use {@link AtlassianServerFactory} to create a
 * server instance for a specific version or for the latest non-deprecated one.
 *
 * <h2>Quick Start</h2>
 * <pre>{@code
 * // Always use the latest version
 * McpServer server = AtlassianServerFactory.createLatest();
 * server.start();
 *
 * // Or request a specific version
 * McpServer v1 = AtlassianServerFactory.create(AtlassianServerVersion.V1);
 * }</pre>
 *
 * @see server.atlassian.AtlassianServerVersion
 * @see server.atlassian.AtlassianServerFactory
 * @see server.McpServer
 */
package server.atlassian;
