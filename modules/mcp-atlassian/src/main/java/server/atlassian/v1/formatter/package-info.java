/**
 * Response formatters that convert Atlassian API data into clean,
 * readable text suitable for AI assistant consumption.
 *
 * <p>Contains:
 * <ul>
 *   <li>{@link server.atlassian.formatter.IssueFormatter} — Jira issues → Markdown text</li>
 *   <li>{@link server.atlassian.formatter.PageFormatter} — Confluence pages → Markdown text</li>
 *   <li>{@link server.atlassian.formatter.PullRequestFormatter} — Bitbucket PRs → Markdown text</li>
 * </ul>
 *
 * <p>Each formatter produces Markdown-formatted output with tables for
 * list views and structured sections for detail views.
 */
package server.atlassian.v1.formatter;
