/**
 * Authentication framework for the Atlassian MCP Server v2.
 *
 * <p>Supports two authentication strategies:
 * <ul>
 *   <li><strong>Token-based</strong> — API tokens (Cloud) or PATs (Data Center)
 *       loaded from local config files</li>
 *   <li><strong>OAuth 2.0 browser flow</strong> — opens a browser for user consent,
 *       captures tokens via a localhost callback, stores and auto-refreshes them</li>
 * </ul>
 *
 * <p>The {@link server.atlassian.v2.auth.AuthManager} selects the appropriate
 * strategy based on configuration.
 */
package server.atlassian.v2.auth;
