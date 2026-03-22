/**
 * Atlassian MCP Server v2 — comprehensive gateway to Jira, Confluence, and Bitbucket.
 *
 * <h2>What's New in v2</h2>
 * <ul>
 *   <li><strong>90+ tools</strong> — expanded coverage across all three products</li>
 *   <li><strong>Browser OAuth 2.0</strong> — log in via browser, tokens managed automatically</li>
 *   <li><strong>Config-based auth</strong> — API tokens and PATs in local config files</li>
 *   <li><strong>Proper JSON schemas</strong> — each tool has typed input parameters</li>
 *   <li><strong>Credential store</strong> — tokens persisted and auto-refreshed locally</li>
 *   <li><strong>Versioning mechanism</strong> — clean deprecation path for future v3</li>
 * </ul>
 *
 * <h2>Authentication Options</h2>
 * <ol>
 *   <li><strong>Option A — Browser OAuth:</strong> Register an OAuth app at
 *       developer.atlassian.com, provide client_id/secret in config, then call
 *       {@code auth_login_browser}. A browser window opens for consent; tokens
 *       are stored locally and refreshed automatically.</li>
 *   <li><strong>Option B — Config credentials:</strong> Place API token (Cloud)
 *       or PAT (Data Center) in the gitignored local properties file.</li>
 * </ol>
 *
 * <h2>Package Layout</h2>
 * <pre>
 *   v2/
 *   ├── AtlassianServerV2.java   — Server entry point (STDIO JSON-RPC)
 *   ├── auth/                    — Authentication framework
 *   ├── client/                  — REST API HTTP clients
 *   ├── config/                  — Layered configuration
 *   ├── handler/                 — MCP tool handlers per product
 *   ├── model/                   — Domain enums and records
 *   └── util/                    — Shared utilities
 * </pre>
 *
 * <h2>Versioning</h2>
 * <p>This is version 2 of the Atlassian MCP server. The original v1 code lives
 * in {@code server.atlassian} (parent package). The v2 server registers under
 * the same MCP name ({@code "atlassian"}) with version {@code "2.0.0"}, so
 * {@link server.McpServerRegistry} replaces v1 when v2 is registered.
 *
 * @see server.atlassian.AtlassianServer the deprecated v1 server
 * @see server.McpServerRegistry version replacement mechanism
 */
package server.atlassian.v2;
