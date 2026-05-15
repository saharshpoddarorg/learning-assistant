/**
 * Web scraping pipeline — HTTP fetching and HTML-to-text extraction.
 *
 * <p>This package handles the raw web interaction layer:
 * <ul>
 *   <li>{@link server.learningresources.scraper.WebScraper} — fetches web pages via Java
 *       {@code HttpClient} with sensible defaults (timeouts, user-agent, redirect following)</li>
 *   <li>{@link server.learningresources.scraper.ContentExtractor} — regex-based HTML stripping:
 *       removes scripts, styles, nav elements, and preserves paragraph structure</li>
 *   <li>{@link server.learningresources.scraper.ScraperResult} — immutable record holding raw
 *       HTTP response metadata and body (consumed by
 *       {@link server.learningresources.content.ContentSummarizer})</li>
 *   <li>{@link server.learningresources.scraper.ScraperException} — thrown on HTTP errors,
 *       I/O failures, timeouts, or invalid URLs</li>
 * </ul>
 *
 * @see server.learningresources.content.ContentSummarizer
 * @see server.learningresources.handler.ScrapeHandler
 */
package server.learningresources.scraper;
