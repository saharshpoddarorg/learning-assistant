/**
 * Immutable model records and enums for the Learning Resources MCP Server.
 *
 * <p>This package contains:
 * <ul>
 *   <li>{@link server.learningresources.model.LearningResource} — core 15-field resource record (title, URL, type, category, tags, etc.)</li>
 *   <li>{@link server.learningresources.model.ResourceType} — content format: documentation, tutorial, blog, book, video, etc. (11 values, incl. PLAYLIST)</li>
 *   <li>{@link server.learningresources.model.ResourceCategory} — broad technology domain: java, python, web, devops, etc. (15 values)</li>
 *   <li>{@link server.learningresources.model.ConceptArea} — fine-grained CS/SE concepts: concurrency, testing, design-patterns, etc. (36 values)</li>
 *   <li>{@link server.learningresources.model.ConceptDomain} — high-level knowledge domains grouping ConceptArea values (8 domains)</li>
 *   <li>{@link server.learningresources.model.SearchMode} — user intent classification: specific, vague, exploratory</li>
 *   <li>{@link server.learningresources.model.DifficultyLevel} — beginner → expert with ordinal ranges</li>
 *   <li>{@link server.learningresources.model.ContentFreshness} — maintenance status: evergreen, actively-maintained, periodic, archived</li>
 *   <li>{@link server.learningresources.model.LanguageApplicability} — language scope: universal → language-specific (6 values)</li>
 *   <li>{@link server.learningresources.model.ContentSummary} — scraped and summarized content from a URL</li>
 *   <li>{@link server.learningresources.model.ResourceQuery} — search and filter criteria for vault queries</li>
 * </ul>
 *
 * <p>All models are Java records — immutable and with defensive copies of collections.
 * Enums support {@code fromString()} for flexible case-insensitive parsing.
 */
package server.learningresources.model;
