package server.learningresources.model;

/**
 * Immutable model records for the Learning Resources MCP Server.
 *
 * <p>This package contains:
 * <ul>
 *   <li>{@link LearningResource} — core resource with metadata (title, URL, type, category, tags)</li>
 *   <li>{@link ResourceType} — classification of content format (documentation, tutorial, blog, etc.)</li>
 *   <li>{@link ResourceCategory} — topic categories (Java, web, DevOps, etc.)</li>
 *   <li>{@link ContentSummary} — scraped and summarized content from a URL</li>
 *   <li>{@link ResourceQuery} — search and filter criteria for vault queries</li>
 * </ul>
 *
 * <p>All models are Java records — immutable and with defensive copies of collections.
 */
