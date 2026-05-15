/**
 * Content processing pipeline — summarization, formatting, and difficulty estimation.
 *
 * <p>This package transforms raw scraped content into structured, readable output:
 * <ul>
 *   <li>{@link server.learningresources.content.ContentSummarizer} — full pipeline: extract text →
 *       get title → count words → estimate reading time → generate summary → estimate difficulty.
 *       Produces a {@link server.learningresources.model.ContentSummary}</li>
 *   <li>{@link server.learningresources.content.ContentReader} — formats a {@code ContentSummary}
 *       for display (full reading, summary-only, or metadata view)</li>
 *   <li>{@link server.learningresources.content.ReadabilityScorer} — estimates content difficulty
 *       via text analysis heuristics (sentence length, advanced keywords, code block density)</li>
 * </ul>
 *
 * @see server.learningresources.scraper
 * @see server.learningresources.model.ContentSummary
 */
package server.learningresources.content;
