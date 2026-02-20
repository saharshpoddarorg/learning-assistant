package server.learningresources.vault;

import server.learningresources.model.ConceptArea;
import server.learningresources.model.DifficultyLevel;
import server.learningresources.model.ResourceCategory;
import server.learningresources.model.ResourceQuery;
import server.learningresources.vault.DiscoveryResult.QueryType;
import server.learningresources.vault.RelevanceScorer.ScoredResource;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Smart discovery engine that interprets user intent and finds relevant resources.
 *
 * <p>Handles three query modes:
 * <ul>
 *   <li><strong>Specific</strong> — user knows exactly what they want ("JUnit 5 docs")</li>
 *   <li><strong>Vague</strong> — user knows the topic area ("something about testing")</li>
 *   <li><strong>Exploratory</strong> — user wants to learn but isn't sure what ("beginner stuff")</li>
 * </ul>
 *
 * <p>Delegates to:
 * <ul>
 *   <li>{@link KeywordIndex} — keyword-to-enum intent inference</li>
 *   <li>{@link RelevanceScorer} — multi-dimensional relevance scoring</li>
 *   <li>{@link DiscoveryResult} — structured result with suggestions</li>
 * </ul>
 *
 * @see KeywordIndex
 * @see RelevanceScorer
 * @see DiscoveryResult
 */
public final class ResourceDiscovery {

    private static final Logger LOGGER = Logger.getLogger(ResourceDiscovery.class.getName());

    /** Maximum results to return from a discovery query. */
    private static final int MAX_RESULTS = 15;

    private final ResourceVault vault;

    /**
     * Creates a discovery engine backed by the given vault.
     *
     * @param vault the resource vault to search
     */
    public ResourceDiscovery(final ResourceVault vault) {
        this.vault = Objects.requireNonNull(vault, "ResourceVault must not be null");
    }

    // ─── Public API ─────────────────────────────────────────────────

    /**
     * Discovers resources matching a free-form user query.
     *
     * <p>Automatically classifies the query as specific, vague, or exploratory
     * and applies the appropriate search strategy with relevance scoring.
     *
     * @param userInput the raw user query string
     * @return a {@link DiscoveryResult} with scored, sorted results and suggestions
     */
    public DiscoveryResult discover(final String userInput) {
        if (userInput == null || userInput.isBlank()) {
            return exploreDefault();
        }

        final var normalizedInput = userInput.strip().toLowerCase();
        final var queryType = classifyQuery(normalizedInput);

        LOGGER.fine("Query classified as: " + queryType + " for input: '" + normalizedInput + "'");

        return switch (queryType) {
            case SPECIFIC -> handleSpecificQuery(normalizedInput);
            case VAGUE -> handleVagueQuery(normalizedInput);
            case EXPLORATORY -> handleExploratoryQuery(normalizedInput);
        };
    }

    /**
     * Discovers resources by concept area, optionally filtered by difficulty range.
     *
     * @param concept       the concept area to search
     * @param minDifficulty minimum difficulty (may be null for no lower bound)
     * @param maxDifficulty maximum difficulty (may be null for no upper bound)
     * @return a {@link DiscoveryResult} with scored concept matches
     */
    public DiscoveryResult discoverByConcept(final ConceptArea concept,
                                              final DifficultyLevel minDifficulty,
                                              final DifficultyLevel maxDifficulty) {
        Objects.requireNonNull(concept, "Concept must not be null");

        final var candidates = vault.search(ResourceQuery.byConcept(concept));
        final var scored = candidates.stream()
                .map(r -> RelevanceScorer.scoreForConcept(r, concept, minDifficulty, maxDifficulty))
                .sorted(Comparator.comparingInt(ScoredResource::score).reversed())
                .limit(MAX_RESULTS)
                .toList();

        return new DiscoveryResult(QueryType.SPECIFIC, scored, List.of(),
                "Resources for concept: " + concept.name());
    }

    /**
     * Returns a curated "getting started" set for a given category.
     *
     * @param category the category to explore
     * @return a {@link DiscoveryResult} with beginner-friendly, official-first resources
     */
    public DiscoveryResult exploreCategory(final ResourceCategory category) {
        Objects.requireNonNull(category, "Category must not be null");

        final var candidates = vault.search(ResourceQuery.byCategory(category));
        final var scored = candidates.stream()
                .map(r -> RelevanceScorer.scoreForExploration(r, DifficultyLevel.BEGINNER))
                .sorted(Comparator.comparingInt(ScoredResource::score).reversed())
                .limit(MAX_RESULTS)
                .toList();

        return new DiscoveryResult(QueryType.EXPLORATORY, scored,
                suggestRelatedCategories(category),
                "Explore " + category.getDisplayName() + " — starting with beginner-friendly resources");
    }

    // ─── Query Classification ───────────────────────────────────────

    /**
     * Classifies a normalised query string into one of three query types.
     */
    private QueryType classifyQuery(final String input) {
        if (input.contains("\"") || input.contains("http") || input.contains("docs for")
                || input.contains("reference for") || input.contains("official")) {
            return QueryType.SPECIFIC;
        }

        final var exploratoryKeywords = List.of(
                "learn", "start", "beginner", "getting started", "new to",
                "don't know", "where to begin", "recommend", "suggest",
                "what should", "help me", "explore", "overview", "introduction"
        );
        final var isShort = input.split("\\s+").length <= 5;
        final var hasExploratoryKeyword = exploratoryKeywords.stream().anyMatch(input::contains);

        if (isShort && hasExploratoryKeyword) {
            return QueryType.EXPLORATORY;
        }
        if (input.split("\\s+").length <= 2
                && KeywordIndex.conceptMap().get(input) == null
                && KeywordIndex.categoryMap().get(input) == null) {
            return QueryType.EXPLORATORY;
        }
        return QueryType.VAGUE;
    }

    // ─── Query Handlers ─────────────────────────────────────────────

    private DiscoveryResult handleSpecificQuery(final String input) {
        final var scored = vault.search(ResourceQuery.ALL).stream()
                .map(r -> RelevanceScorer.scoreForSpecific(r, input))
                .filter(sr -> sr.score() > 0)
                .sorted(Comparator.comparingInt(ScoredResource::score).reversed())
                .limit(MAX_RESULTS)
                .toList();

        return new DiscoveryResult(QueryType.SPECIFIC, scored,
                scored.isEmpty() ? generateDidYouMean(input) : List.of(),
                scored.isEmpty()
                        ? "No exact matches found for: '" + input + "'"
                        : "Found " + scored.size() + " matching resources");
    }

    private DiscoveryResult handleVagueQuery(final String input) {
        final var inferredConcepts = inferConcepts(input);
        final var inferredCategories = inferCategories(input);

        final var scored = vault.search(ResourceQuery.ALL).stream()
                .map(r -> RelevanceScorer.scoreForVague(r, input, inferredConcepts, inferredCategories))
                .filter(sr -> sr.score() > 0)
                .sorted(Comparator.comparingInt(ScoredResource::score).reversed())
                .limit(MAX_RESULTS)
                .toList();

        return new DiscoveryResult(QueryType.VAGUE, scored,
                scored.isEmpty() ? generateDidYouMean(input) : List.of(),
                buildVagueSummary(input, inferredConcepts, inferredCategories, scored.size()));
    }

    private DiscoveryResult handleExploratoryQuery(final String input) {
        var targetDifficulty = DifficultyLevel.BEGINNER;
        for (final var entry : KeywordIndex.difficultyMap().entrySet()) {
            if (input.contains(entry.getKey())) {
                targetDifficulty = entry.getValue();
                break;
            }
        }

        ResourceCategory targetCategory = null;
        for (final var entry : KeywordIndex.categoryMap().entrySet()) {
            if (input.contains(entry.getKey())) {
                targetCategory = entry.getValue();
                break;
            }
        }

        final var effectiveCategory = targetCategory;
        final var effectiveDifficulty = targetDifficulty;

        final var scored = vault.search(ResourceQuery.ALL).stream()
                .map(r -> RelevanceScorer.scoreForExploration(r, effectiveDifficulty))
                .filter(sr -> effectiveCategory == null
                        || sr.resource().categories().contains(effectiveCategory))
                .sorted(Comparator.comparingInt(ScoredResource::score).reversed())
                .limit(MAX_RESULTS)
                .toList();

        final var suggestions = List.of(
                "Try 'browse java' to explore Java resources",
                "Try 'official docs' to see official documentation",
                "Try 'system design' for architecture resources",
                "Try 'testing' for testing resources"
        );

        return new DiscoveryResult(QueryType.EXPLORATORY, scored, suggestions,
                "Here are recommended resources for " + targetDifficulty + " level learners");
    }

    private DiscoveryResult exploreDefault() {
        final var scored = vault.search(ResourceQuery.ALL).stream()
                .map(r -> RelevanceScorer.scoreForExploration(r, DifficultyLevel.BEGINNER))
                .sorted(Comparator.comparingInt(ScoredResource::score).reversed())
                .limit(MAX_RESULTS)
                .toList();

        return new DiscoveryResult(QueryType.EXPLORATORY, scored,
                List.of("Try searching for a specific topic, category, or concept"),
                "Showing top recommended resources across all categories");
    }

    // ─── Intent Inference ───────────────────────────────────────────

    /**
     * Infers concept areas from the user's input using single-word and phrase matching.
     */
    private List<ConceptArea> inferConcepts(final String input) {
        final var result = new ArrayList<ConceptArea>();
        for (final var word : input.split("\\s+")) {
            final var concept = KeywordIndex.conceptMap().get(word);
            if (concept != null && !result.contains(concept)) {
                result.add(concept);
            }
        }
        for (final var entry : KeywordIndex.conceptMap().entrySet()) {
            if (input.contains(entry.getKey()) && !result.contains(entry.getValue())) {
                result.add(entry.getValue());
            }
        }
        return result;
    }

    /**
     * Infers resource categories from the user's input.
     */
    private List<ResourceCategory> inferCategories(final String input) {
        final var result = new ArrayList<ResourceCategory>();
        for (final var word : input.split("\\s+")) {
            final var category = KeywordIndex.categoryMap().get(word);
            if (category != null && !result.contains(category)) {
                result.add(category);
            }
        }
        for (final var entry : KeywordIndex.categoryMap().entrySet()) {
            if (input.contains(entry.getKey()) && !result.contains(entry.getValue())) {
                result.add(entry.getValue());
            }
        }
        return result;
    }

    // ─── Suggestion Engine ──────────────────────────────────────────

    private List<String> generateDidYouMean(final String input) {
        final var suggestions = new ArrayList<String>();

        for (final var category : ResourceCategory.values()) {
            if (category.getDisplayName().toLowerCase().contains(input)
                    || input.contains(category.getDisplayName().toLowerCase())) {
                suggestions.add("Browse category: " + category.getDisplayName());
            }
        }

        for (final var concept : ConceptArea.values()) {
            final var conceptName = concept.name().toLowerCase().replace('_', ' ');
            if (conceptName.contains(input) || input.contains(conceptName)) {
                suggestions.add("Search concept: " + concept.name());
            }
        }

        if (suggestions.isEmpty()) {
            suggestions.add("Try broader terms (e.g., 'java', 'testing', 'design patterns')");
            suggestions.add("Use 'list_categories' to see all available categories");
        }
        return suggestions;
    }

    private List<String> suggestRelatedCategories(final ResourceCategory category) {
        return switch (category) {
            case JAVA -> List.of(
                    "Also explore: software-engineering, testing",
                    "Concept: try 'concurrency' or 'design-patterns'"
            );
            case WEB, JAVASCRIPT -> List.of(
                    "Also explore: security, devops",
                    "Concept: try 'web-security' or 'api-design'"
            );
            case PYTHON -> List.of(
                    "Also explore: ai-ml, data",
                    "Concept: try 'machine-learning' or 'testing'"
            );
            case DEVOPS -> List.of(
                    "Also explore: software-engineering",
                    "Concept: try 'containers' or 'ci-cd'"
            );
            default -> List.of("Try searching for a specific concept area");
        };
    }

    private String buildVagueSummary(final String input,
                                     final List<ConceptArea> concepts,
                                     final List<ResourceCategory> categories,
                                     final int resultCount) {
        final var builder = new StringBuilder();
        builder.append("Found ").append(resultCount).append(" resources for '").append(input).append("'");

        if (!concepts.isEmpty()) {
            builder.append("\nInferred concepts: ").append(concepts.stream()
                    .map(ConceptArea::name).collect(Collectors.joining(", ")));
        }
        if (!categories.isEmpty()) {
            builder.append("\nInferred categories: ").append(categories.stream()
                    .map(ResourceCategory::getDisplayName).collect(Collectors.joining(", ")));
        }
        return builder.toString();
    }
}
