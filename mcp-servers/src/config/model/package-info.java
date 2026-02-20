package config.model;

/**
 * Package containing immutable configuration model records for the MCP system.
 *
 * <p>All models are Java records (immutable, with defensive copies of collections)
 * and follow a hierarchical structure rooted at {@link McpConfiguration}:
 *
 * <pre>
 *   McpConfiguration (root)
 *   ├── ApiKeyStore         — credentials for external services
 *   ├── LocationPreferences — timezone, locale, region
 *   ├── UserPreferences     — theme, logging, timeouts
 *   ├── BrowserPreferences  — browser executable, profile, launch mode
 *   ├── ServerDefinition    — individual MCP server config
 *   │   └── TransportType   — stdio / sse / streamable-http
 *   └── ProfileDefinition   — named override sets (dev, prod)
 * </pre>
 */
