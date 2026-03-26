/**
 * Shared utilities used by multiple versions of the Atlassian MCP server.
 *
 * <p>Classes in this package are version-neutral — they are imported by both
 * {@code server.atlassian.v1} and {@code server.atlassian.v2} (and any future
 * versions). Changes here must be backwards-compatible.
 *
 * @see server.atlassian.common.JsonExtractor
 * @see server.atlassian.common.JsonUtils
 */
package server.atlassian.common;
