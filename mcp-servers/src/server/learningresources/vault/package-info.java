/**
 * Resource vault — in-memory library, smart discovery engine, and relevance scoring.
 *
 * <p>Core classes:
 * <ul>
 *   <li>{@link server.learningresources.vault.ResourceVault} — thread-safe in-memory store
 *       ({@code ConcurrentHashMap}) with composite search and filter</li>
 *   <li>{@link server.learningresources.vault.BuiltInResources} — pre-loaded library of ~100+ curated
 *       resources, composed from 9 domain-specific
 *       {@link server.learningresources.vault.ResourceProvider} implementations</li>
 * </ul>
 *
 * <p>Discovery engine:
 * <ul>
 *   <li>{@link server.learningresources.vault.ResourceDiscovery} — smart 3-mode engine
 *       (specific / vague / exploratory) that interprets user intent</li>
 *   <li>{@link server.learningresources.vault.RelevanceScorer} — 12-dimension scoring
 *       (text match, concept, category, domain, language alignment, difficulty fit, freshness, etc.)</li>
 *   <li>{@link server.learningresources.vault.KeywordIndex} — static keyword-to-enum mappings
 *       for intent inference (e.g., "concurrency" → {@code ConceptArea.CONCURRENCY})</li>
 *   <li>{@link server.learningresources.vault.DiscoveryResult} — complete discovery result record
 *       (classified mode, scored results, suggestions, summary)</li>
 * </ul>
 *
 * <p>Extension point:
 * <ul>
 *   <li>{@link server.learningresources.vault.ResourceProvider} — functional interface for adding
 *       new resource providers (see {@code vault.providers} sub-package)</li>
 * </ul>
 *
 * @see server.learningresources.vault.providers
 * @see server.learningresources.handler.SearchHandler
 */
package server.learningresources.vault;
