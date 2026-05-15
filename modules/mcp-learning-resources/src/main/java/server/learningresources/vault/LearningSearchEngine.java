package server.learningresources.vault;

import server.learningresources.model.ConceptArea;
import server.learningresources.model.ConceptDomain;
import server.learningresources.model.DifficultyLevel;
import server.learningresources.model.LearningResource;
import server.learningresources.model.ResourceCategory;
import search.api.classify.SearchMode;
import search.api.core.ScoredItem;
import search.api.core.SearchContext;
import search.api.core.SearchEngine;
import search.api.core.SearchResult;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * A {@link SearchEngine}{@code <LearningResource>} that wraps the existing
 * {@link ResourceDiscovery} engine and exposes it through the generic
 * {@code search.*} API contract.
 *
 * <h2>Role in the architecture</h2>
 * <pre>
 * Generic API                     Learning Resources domain
 * ─────────────────────────────   ──────────────────────────────────────
 * SearchEngine&lt;LearningResource&gt; ← LearningSearchEngine
 *   SearchContext                    delegates to → ResourceDiscovery
 *   SearchResult&lt;LearningResource&gt;              uses → ResourceVault
 *                                               uses → KeywordIndex
 *                                               uses → RelevanceScorer
 * </pre>
 *
 * <p>This adapter pattern means:
 * <ul>
 *   <li>All existing {@link ResourceDiscovery} logic is preserved with zero risk of regression.</li>
 *   <li>Callers that know only about {@link search.api.core.SearchEngine} can use this engine
 *       without importing any {@code server.learningresources.*} types.</li>
 *   <li>Future migrations can gradually move scoring logic into the generic
 *       {@link search.api.algorithm} and {@link search.api.index} layers.</li>
 * </ul>
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * ResourceVault vault = new ResourceVault().loadBuiltInResources();
 * SearchEngine<LearningResource> engine = new LearningSearchEngine(vault);
 *
 * // Generic search API
 * SearchResult<LearningResource> result = engine.search("java concurrency beginner");
 * result.items().forEach(si -> System.out.println(si.score() + " " + si.item().title()));
 *
 * // Typed concept-based discovery (still available through cast)
 * LearningSearchEngine lr = (LearningSearchEngine) engine;
 * DiscoveryResult byConceptResult = lr.discoverByConcept(ConceptArea.CONCURRENCY, null, null);
 * }</pre>
 *
 * @see ResourceDiscovery
 * @see ResourceVault
 * @see search.api.core.SearchEngine
 * @see search.engine.core.ConfigurableSearchEngine
 */
public final class LearningSearchEngine implements SearchEngine<LearningResource> {

    private static final Logger LOGGER = Logger.getLogger(LearningSearchEngine.class.getName());

    private final ResourceDiscovery discovery;

    /**
     * Creates a {@code LearningSearchEngine} backed by the given vault.
     *
     * @param vault the resource vault to search (must not be null)
     */
    public LearningSearchEngine(final ResourceVault vault) {
        Objects.requireNonNull(vault, "ResourceVault must not be null");
        this.discovery = new ResourceDiscovery(vault);
        LOGGER.info("LearningSearchEngine initialised with vault (" + vault.size() + " resources).");
    }

    /**
     * Creates a {@code LearningSearchEngine} around an already-built {@link ResourceDiscovery}.
     *
     * @param discovery the discovery engine to delegate to (must not be null)
     */
    public LearningSearchEngine(final ResourceDiscovery discovery) {
        this.discovery = Objects.requireNonNull(discovery, "ResourceDiscovery must not be null");
    }

    // ─── SearchEngine<LearningResource> ─────────────────────────────

    /**
     * Executes a smart discovery search and adapts the result to the generic API.
     *
     * <p>The {@link SearchContext#forcedMode()} is honoured if present,
     * mapped from the generic {@link search.classify.SearchMode} to the LR-specific
     * {@link server.learningresources.model.SearchMode}.
     *
     * @param context the search context
     * @return a generic {@link SearchResult} wrapping the discovery result
     */
    @Override
    public SearchResult<LearningResource> search(final SearchContext context) {
        Objects.requireNonNull(context, "SearchContext must not be null");

        final DiscoveryResult discoveryResult;

        if (context.hasForcedMode()) {
            final var lrMode = toLrSearchMode(context.forcedMode());
            discoveryResult = discovery.discover(context.rawInput(), lrMode);
        } else {
            discoveryResult = discovery.discover(context.rawInput());
        }

        return toGenericResult(discoveryResult, context);
    }

    // ─── Domain-Specific Discovery ──────────────────────────────────

    /**
     * Discovers resources by concept area with optional difficulty range.
     * Delegates directly to {@link ResourceDiscovery#discoverByConcept}.
     *
     * @param concept       the concept area to find resources for (must not be null)
     * @param minDifficulty minimum difficulty, or null for no lower bound
     * @param maxDifficulty maximum difficulty, or null for no upper bound
     * @return the domain-specific discovery result
     */
    public DiscoveryResult discoverByConcept(
            final ConceptArea concept,
            final DifficultyLevel minDifficulty,
            final DifficultyLevel maxDifficulty) {
        return discovery.discoverByConcept(concept, minDifficulty, maxDifficulty);
    }

    /**
     * Discovers resources by concept domain with optional difficulty range.
     * Delegates directly to {@link ResourceDiscovery#discoverByDomain}.
     *
     * @param domain        the concept domain to explore (must not be null)
     * @param minDifficulty minimum difficulty, or null for no lower bound
     * @param maxDifficulty maximum difficulty, or null for no upper bound
     * @return the domain-specific discovery result
     */
    public DiscoveryResult discoverByDomain(
            final ConceptDomain domain,
            final DifficultyLevel minDifficulty,
            final DifficultyLevel maxDifficulty) {
        return discovery.discoverByDomain(domain, minDifficulty, maxDifficulty);
    }

    /**
     * Returns curated beginner-friendly resources for a given category.
     * Delegates directly to {@link ResourceDiscovery#exploreCategory}.
     *
     * @param category the category to explore (must not be null)
     * @return the domain-specific discovery result
     */
    public DiscoveryResult exploreCategory(
            final ResourceCategory category) {
        return discovery.exploreCategory(category);
    }

    // ─── Adapters ───────────────────────────────────────────────────

    /**
     * Converts a {@link DiscoveryResult} to a generic {@link SearchResult}.
     */
    private SearchResult<LearningResource> toGenericResult(
            final DiscoveryResult discoveryResult,
            final SearchContext context) {

        final var genericMode = toGenericSearchMode(discoveryResult.searchMode());

        final List<ScoredItem<LearningResource>> items = discoveryResult.results().stream()
                .<ScoredItem<LearningResource>>map(sr -> new ScoredItem<>(sr.resource(), sr.score()))
                .toList();

        return new SearchResult<>(
                genericMode,
                items,
                discoveryResult.suggestions(),
                discoveryResult.summary()
        );
    }

    /**
     * Maps the generic {@link SearchMode} to the LR-specific
     * {@link server.learningresources.model.SearchMode}.
     */
    private server.learningresources.model.SearchMode toLrSearchMode(final SearchMode mode) {
        return switch (mode) {
            case SPECIFIC    -> server.learningresources.model.SearchMode.SPECIFIC;
            case VAGUE       -> server.learningresources.model.SearchMode.VAGUE;
            case EXPLORATORY -> server.learningresources.model.SearchMode.EXPLORATORY;
        };
    }

    /**
     * Maps the LR-specific {@link server.learningresources.model.SearchMode} to the
     * generic {@link search.classify.SearchMode}.
     */
    private SearchMode toGenericSearchMode(
            final server.learningresources.model.SearchMode lrMode) {
        return switch (lrMode) {
            case SPECIFIC    -> SearchMode.SPECIFIC;
            case VAGUE       -> SearchMode.VAGUE;
            case EXPLORATORY -> SearchMode.EXPLORATORY;
        };
    }
}
