package server.learningresources.search;

import search.api.core.SearchContext;
import search.api.core.SearchResult;
import search.engine.algorithm.TextMatchScorer;
import search.engine.config.SearchEngineConfig;
import search.engine.core.ConfigurableSearchEngine;
import search.engine.rank.ScoreRanker;
import server.learningresources.model.LearningResource;
import server.learningresources.vault.ResourceVault;

/**
 * Tier-3 search engine specialised for <em>official documentation</em>.
 *
 * <h2>Position in the tier hierarchy</h2>
 * <pre>
 * Tier 0  SearchEngine&lt;T&gt;                      (API contract)
 * Tier 1  ConfigurableSearchEngine&lt;T&gt;           (generic 5-phase pipeline)
 * Tier 2  LearningSearchEngine                  (LR domain, wraps ResourceDiscovery)
 * Tier 3  OfficialDocsSearchEngine              ({@literal <}-- you are here)
 * </pre>
 *
 * <h2>What this tier adds over Tier 2</h2>
 * <ul>
 *   <li><strong>Pre-filter</strong> -- only resources with {@code isOfficial == true} survive.</li>
 *   <li><strong>Title-heavy scoring</strong> -- uses {@link TextMatchScorer.Scores#titleHeavy()}
 *       to strongly prefer exact/partial title matches.</li>
 *   <li><strong>Smaller default result cap</strong> -- at most 8 results.</li>
 * </ul>
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * ResourceVault vault = new ResourceVault().loadBuiltInResources();
 * OfficialDocsSearchEngine engine = new OfficialDocsSearchEngine(vault);
 * SearchResult<LearningResource> result = engine.search("spring boot reference");
 * }</pre>
 *
 * @see server.learningresources.vault.LearningSearchEngine
 * @see search.engine.core.ConfigurableSearchEngine
 * @see search.api.core.SearchEngine
 */
public final class OfficialDocsSearchEngine extends ConfigurableSearchEngine<LearningResource> {

    private static final int MAX_OFFICIAL_RESULTS = 8;

    /**
     * Creates an official-docs-only engine backed by the given vault.
     *
     * @param vault the resource vault to search (must not be null)
     */
    public OfficialDocsSearchEngine(final ResourceVault vault) {
        super(buildConfig(vault));
    }

    // --- Pipeline hooks -------------------------------------------------

    @Override
    protected SearchResult<LearningResource> postSearch(
            final SearchContext context,
            final SearchResult<LearningResource> result) {

        if (result.isEmpty()) {
            final var suggestions = result.suggestions().isEmpty()
                    ? java.util.List.of(
                        "No official documentation matched \"" + context.normalizedInput() + "\".",
                        "Tip: try searching without the 'official' qualifier for broader results.")
                    : result.suggestions();
            return SearchResult.emptyWithSuggestions(
                    result.classifiedMode(), result.summary(), suggestions);
        }
        return result;
    }

    // --- Static config builder ------------------------------------------

    private static SearchEngineConfig<LearningResource> buildConfig(final ResourceVault vault) {
        final var config = SearchEngineConfig.<LearningResource>builder()
                .filter((resource, ctx) -> resource.isOfficial())
                .defaultScorer(TextMatchScorer.<LearningResource>builder()
                        .titleExtractor(LearningResource::title)
                        .bodyExtractor(LearningResource::description)
                        .scores(TextMatchScorer.Scores.titleHeavy())
                        .build())
                .ranker(ScoreRanker.instance())
                .maxResults(MAX_OFFICIAL_RESULTS)
                .summaryBuilder((ctx, count) ->
                        count + " official doc(s) for '" + ctx.normalizedInput() + "'")
                .suggestionProvider(ctx -> java.util.List.of())
                .build();

        vault.listAll().forEach(resource -> config.index().add(resource.id(), resource));

        return config;
    }
}