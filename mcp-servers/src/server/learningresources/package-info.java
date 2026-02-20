package server.learningresources;

/**
 * Learning Resources MCP Server — web scraping, content summarization,
 * and a curated vault of worldwide learning resources.
 *
 * <p>Package structure:
 * <pre>
 *   server.learningresources
 *   ├── {@link LearningResourcesServer}      — STDIO server entry point
 *   ├── model/                                — Immutable data records
 *   │   ├── {@link server.learningresources.model.LearningResource}   — Core resource
 *   │   ├── {@link server.learningresources.model.ResourceType}       — Format enum
 *   │   ├── {@link server.learningresources.model.ResourceCategory}   — Topic enum
 *   │   ├── {@link server.learningresources.model.ContentSummary}     — Scraped content
 *   │   └── {@link server.learningresources.model.ResourceQuery}      — Search criteria
 *   ├── vault/                                — Curated resource library
 *   │   ├── {@link server.learningresources.vault.ResourceVault}      — In-memory store
 *   │   └── {@link server.learningresources.vault.BuiltInResources}   — 30+ famous resources
 *   ├── scraper/                              — Web scraping pipeline
 *   │   ├── {@link server.learningresources.scraper.WebScraper}       — HTTP fetcher
 *   │   ├── {@link server.learningresources.scraper.ContentExtractor} — HTML → text
 *   │   └── {@link server.learningresources.scraper.ScraperResult}    — Raw fetch output
 *   ├── content/                              — Content processing
 *   │   ├── {@link server.learningresources.content.ContentSummarizer}— Summarize content
 *   │   ├── {@link server.learningresources.content.ContentReader}    — Format for reading
 *   │   └── {@link server.learningresources.content.ReadabilityScorer}— Difficulty estimation
 *   └── handler/                              — MCP tool dispatch
 *       ├── {@link server.learningresources.handler.ToolHandler}      — Tool router
 *       ├── {@link server.learningresources.handler.SearchHandler}    — Vault search
 *       └── {@link server.learningresources.handler.ScrapeHandler}    — URL scraping
 * </pre>
 */
