/**
 * MCP tool dispatch layer — routes incoming tool calls to business logic.
 *
 * <p>This package contains the 10-tool handler architecture:
 * <ul>
 *   <li>{@link server.learningresources.handler.ToolHandler} — central dispatcher that routes
 *       MCP protocol messages to the appropriate handler</li>
 *   <li>{@link server.learningresources.handler.SearchHandler} — vault search, browse, get,
 *       list categories, and discovery tools</li>
 *   <li>{@link server.learningresources.handler.ScrapeHandler} — URL scraping: fetch → extract →
 *       summarize → format</li>
 *   <li>{@link server.learningresources.handler.ExportHandler} — formats results into Markdown,
 *       PDF, or Word via pandoc (with plain-text fallback)</li>
 *   <li>{@link server.learningresources.handler.UrlResourceHandler} — smart resource addition:
 *       scrape URL → auto-extract metadata → infer categorization → add to vault</li>
 * </ul>
 *
 * <p>Registered tools: {@code search_resources}, {@code browse_vault}, {@code get_resource},
 * {@code list_categories}, {@code discover_resources}, {@code scrape_url}, {@code read_url},
 * {@code add_resource}, {@code add_resource_from_url}, {@code export_results}.
 *
 * @see server.learningresources.LearningResourcesServer
 * @see server.learningresources.handler.ToolHandler
 */
package server.learningresources.handler;
