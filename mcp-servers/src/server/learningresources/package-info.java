/**
 * Learning Resources MCP Server — web scraping, content summarization,
 * smart discovery, and a curated vault of ~100+ worldwide learning resources.
 *
 * <p>Package structure:
 * <pre>
 *   server.learningresources
 *   ├── {@link server.learningresources.LearningResourcesServer}         — STDIO server entry point
 *   ├── model/                                                            — Immutable data records &amp; enums
 *   │   ├── {@link server.learningresources.model.LearningResource}       — Core 15-field resource record
 *   │   ├── {@link server.learningresources.model.ResourceType}           — Content format enum (11 values, incl. PLAYLIST)
 *   │   ├── {@link server.learningresources.model.ResourceCategory}       — Topic domain enum (15 values)
 *   │   ├── {@link server.learningresources.model.ConceptArea}            — Fine-grained CS/SE concepts (36 values)
 *   │   ├── {@link server.learningresources.model.ConceptDomain}          — High-level knowledge domains (8 values)
 *   │   ├── {@link server.learningresources.model.SearchMode}             — User intent: specific/vague/exploratory
 *   │   ├── {@link server.learningresources.model.DifficultyLevel}        — Beginner → expert with ordinals
 *   │   ├── {@link server.learningresources.model.ContentFreshness}       — Maintenance status enum
 *   │   ├── {@link server.learningresources.model.LanguageApplicability}  — Language scope enum (6 values)
 *   │   ├── {@link server.learningresources.model.ContentSummary}         — Scraped content summary
 *   │   └── {@link server.learningresources.model.ResourceQuery}          — Search/filter criteria
 *   ├── vault/                                                            — Discovery engine &amp; resource library
 *   │   ├── {@link server.learningresources.vault.ResourceVault}          — In-memory store with composite search
 *   ├── {@link server.learningresources.vault.BuiltInResources}       — ~100+ curated resources (9 providers)
 *   │   ├── {@link server.learningresources.vault.ResourceDiscovery}      — Smart 3-mode discovery engine
 *   │   ├── {@link server.learningresources.vault.RelevanceScorer}        — 12-dimension relevance scoring
 *   │   ├── {@link server.learningresources.vault.KeywordIndex}           — Keyword-to-enum intent inference
 *   │   ├── {@link server.learningresources.vault.DiscoveryResult}        — Discovery result record
 *   │   ├── {@link server.learningresources.vault.ResourceProvider}       — Provider interface
 *   │   └── providers/                                                    — 9 modular resource providers
 *   ├── scraper/                                                          — Web scraping pipeline
 *   │   ├── {@link server.learningresources.scraper.WebScraper}           — Java HttpClient-based fetcher
 *   │   ├── {@link server.learningresources.scraper.ContentExtractor}     — HTML → clean text
 *   │   └── {@link server.learningresources.scraper.ScraperResult}        — Raw HTTP response record
 *   ├── content/                                                          — Content processing
 *   │   ├── {@link server.learningresources.content.ContentSummarizer}    — Summarize pipeline
 *   │   ├── {@link server.learningresources.content.ContentReader}        — Format for display
 *   │   └── {@link server.learningresources.content.ReadabilityScorer}    — Difficulty estimation
 *   └── handler/                                                          — MCP tool dispatch (10 tools)
 *       ├── {@link server.learningresources.handler.ToolHandler}          — Tool router (10 tools)
 *       ├── {@link server.learningresources.handler.SearchHandler}        — Vault search/browse
 *       ├── {@link server.learningresources.handler.ScrapeHandler}        — URL scraping
 *       ├── {@link server.learningresources.handler.ExportHandler}        — Markdown/PDF/Word export
 *       └── {@link server.learningresources.handler.UrlResourceHandler}   — Smart add-from-URL
 * </pre>
 */
package server.learningresources;
