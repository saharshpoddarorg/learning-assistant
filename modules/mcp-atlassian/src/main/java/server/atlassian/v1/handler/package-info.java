/**
 * MCP tool dispatch layer — routes incoming tool calls to product-specific business logic.
 *
 * <p>Contains:
 * <ul>
 *   <li>{@link server.atlassian.handler.ToolHandler} — central dispatcher routing by tool name prefix</li>
 *   <li>{@link server.atlassian.handler.JiraHandler} — Jira tool implementations (7 tools)</li>
 *   <li>{@link server.atlassian.handler.ConfluenceHandler} — Confluence tool implementations (5 tools)</li>
 *   <li>{@link server.atlassian.handler.BitbucketHandler} — Bitbucket tool implementations (5 tools)</li>
 * </ul>
 *
 * <p>Registered tools (17 total):
 * <ul>
 *   <li>Jira: jira_search_issues, jira_get_issue, jira_create_issue, jira_update_issue,
 *       jira_transition_issue, jira_list_projects, jira_get_sprint</li>
 *   <li>Confluence: confluence_search, confluence_get_page, confluence_create_page,
 *       confluence_update_page, confluence_list_spaces</li>
 *   <li>Bitbucket: bitbucket_list_repos, bitbucket_get_repo, bitbucket_list_pull_requests,
 *       bitbucket_get_pull_request, bitbucket_search_code</li>
 * </ul>
 *
 * @see server.atlassian.AtlassianServer
 */
package server.atlassian.v1.handler;
