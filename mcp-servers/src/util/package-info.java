/**
 * Cross-cutting utility classes shared by all MCP server packages.
 *
 * <p>This package sits at the root level (sibling to {@code config} and
 * {@code server}) because it contains generic helpers that are genuinely
 * needed by multiple, unrelated server packages. Placing them here avoids
 * artificial dependencies between server implementations.
 *
 * <p>Guidelines for what belongs here vs. elsewhere:
 * <ul>
 *   <li><b>{@code util}</b> — truly cross-cutting: used by 2+ top-level server packages</li>
 *   <li><b>{@code server.atlassian.common}</b> — shared only within Atlassian server versions</li>
 *   <li><b>{@code server.learningresources.model}</b> — shared only within Learning Resources</li>
 * </ul>
 *
 * @see util.StringUtils
 * @see util.HtmlUtils
 */
package util;
