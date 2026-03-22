/**
 * Shared and product-specific model records and enums for the Atlassian MCP Server.
 *
 * <p>This package contains:
 * <ul>
 *   <li>{@link server.atlassian.model.AtlassianProduct} — product identifier enum</li>
 *   <li>{@link server.atlassian.model.AtlassianCredentials} — authentication credentials</li>
 *   <li>{@link server.atlassian.model.ConnectionConfig} — connection settings (URL, auth, timeout)</li>
 *   <li>{@link server.atlassian.model.ToolResponse} — standardized MCP tool response wrapper</li>
 * </ul>
 *
 * <p>Product-specific models are in sub-packages:
 * <ul>
 *   <li>{@code model.jira} — JiraIssue, JiraProject, IssueType, IssuePriority, IssueStatusCategory</li>
 *   <li>{@code model.confluence} — ConfluencePage, ConfluenceSpace</li>
 *   <li>{@code model.bitbucket} — BitbucketRepository, BitbucketPullRequest, PullRequestState</li>
 * </ul>
 *
 * <p>All models are Java records — immutable with defensive copies of collections.
 */
package server.atlassian.v1.model;
