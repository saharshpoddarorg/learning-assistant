/**
 * REST API clients for Atlassian Cloud products.
 *
 * <p>Contains:
 * <ul>
 *   <li>{@link server.atlassian.client.AtlassianRestClient} — shared HTTP client with auth headers</li>
 *   <li>{@link server.atlassian.client.JiraClient} — Jira REST API v3 operations</li>
 *   <li>{@link server.atlassian.client.ConfluenceClient} — Confluence REST API v2 operations</li>
 *   <li>{@link server.atlassian.client.BitbucketClient} — Bitbucket REST API 2.0 operations</li>
 * </ul>
 *
 * <p>All clients return raw JSON strings. Parsing and formatting is handled
 * by the handler and formatter layers.
 *
 * @see server.atlassian.handler.ToolHandler
 */
package server.atlassian.v1.client;
