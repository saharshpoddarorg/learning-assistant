/**
 * Atlassian-specific configuration loading for the Atlassian MCP Server.
 *
 * <p>This package is self-contained — it does <strong>not</strong> import
 * from the shared {@code config.*} package used by the MCP framework config.
 * Each Atlassian server instance loads its own config independently.
 *
 * <h3>Key types:</h3>
 * <ul>
 *   <li>{@link server.atlassian.config.AtlassianVariant} — deployment flavour
 *       (Cloud, Data Center, Server, Custom)</li>
 *   <li>{@link server.atlassian.config.AtlassianServerConfig} — immutable record
 *       holding all settings for one instance</li>
 *   <li>{@link server.atlassian.config.AtlassianConfigLoader} — reads and merges
 *       layered properties files and environment variables</li>
 * </ul>
 *
 * <h3>Config file locations:</h3>
 * <pre>
 *   user-config/servers/atlassian/
 *   ├── atlassian-config.properties               ← defaults (committed)
 *   └── atlassian-config.local.properties         ← secrets (gitignored)
 * </pre>
 */
package server.atlassian.v1.config;
