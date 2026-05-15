package server.learningresources.vault;

import server.learningresources.model.ConceptArea;
import server.learningresources.model.ConceptDomain;
import server.learningresources.model.DifficultyLevel;
import server.learningresources.model.ResourceCategory;
import server.learningresources.model.ResourceQuery;
import server.learningresources.model.SearchMode;
import server.learningresources.vault.RelevanceScorer.ScoredResource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Smart discovery engine that interprets user intent and finds relevant resources.
 *
 * <p>Handles three search modes:
 * <ul>
 *   <li><strong>Specific</strong> — user knows exactly what they want ("JUnit 5 docs")</li>
 *   <li><strong>Vague</strong> — user knows the topic area ("something about testing")</li>
 *   <li><strong>Exploratory</strong> — user wants to learn but isn't sure what ("beginner stuff")</li>
 * </ul>
 *
 * <p>Delegates to:
 * <ul>
 *   <li>{@link KeywordIndex} — keyword-to-enum intent inference</li>
 *   <li>{@link RelevanceScorer} — multi-dimensional relevance scoring with fuzzy matching</li>
 *   <li>{@link DiscoveryResult} — structured result with suggestions</li>
 * </ul>
 *
 * @see SearchMode
 * @see KeywordIndex
 * @see RelevanceScorer
 * @see DiscoveryResult
 */
public final class ResourceDiscovery {

    private static final Logger LOGGER = Logger.getLogger(ResourceDiscovery.class.getName());

    /** Maximum results to return from a discovery query. */
    private static final int MAX_RESULTS = 15;

    /** Keywords that signal exploratory intent. */
    private static final List<String> EXPLORATORY_KEYWORDS = List.of(
            "learn", "start", "beginner", "getting started", "new to",
            "don't know", "where to begin", "recommend", "suggest",
            "what should", "help me", "explore", "overview", "introduction",
            "guide me", "teach me", "tutorial", "how to start", "first steps"
    );

    /** Keywords that signal specific intent. */
    private static final List<String> SPECIFIC_KEYWORDS = List.of(
            "docs for", "reference for", "official", "documentation",
            "api reference", "specification", "spec for", "javadoc"
    );

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
        final var searchMode = classifyQuery(normalizedInput);

        LOGGER.fine("Query classified as: " + searchMode + " for input: '" + normalizedInput + "'");

        return switch (searchMode) {
            case SPECIFIC -> handleSpecificQuery(normalizedInput);
            case VAGUE -> handleVagueQuery(normalizedInput);
            case EXPLORATORY -> handleExploratoryQuery(normalizedInput);
        };
    }

    /**
     * Discovers resources matching a free-form query with a forced search mode.
     *
     * <p>Skips automatic classification and uses the provided mode directly.
     *
     * @param userInput  the raw user query string
     * @param searchMode the search mode to apply
     * @return a {@link DiscoveryResult} with scored, sorted results and suggestions
     */
    public DiscoveryResult discover(final String userInput, final SearchMode searchMode) {
        Objects.requireNonNull(searchMode, "SearchMode must not be null");
        if (userInput == null || userInput.isBlank()) {
            return exploreDefault();
        }

        final var normalizedInput = userInput.strip().toLowerCase();

        LOGGER.fine("Forced mode: " + searchMode + " for input: '" + normalizedInput + "'");

        return switch (searchMode) {
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

        return new DiscoveryResult(SearchMode.SPECIFIC, scored, List.of(),
                "Resources for concept: " + concept.name()
                        + " (domain: " + concept.getDomain().getDisplayName() + ")");
    }

    /**
     * Discovers resources by concept domain, returning all concept areas in that domain.
     *
     * @param domain        the concept domain to explore
     * @param minDifficulty minimum difficulty (may be null)
     * @param maxDifficulty maximum difficulty (may be null)
     * @return a {@link DiscoveryResult} with resources across the domain
     */
    public DiscoveryResult discoverByDomain(final ConceptDomain domain,
                                            final DifficultyLevel minDifficulty,
                                            final DifficultyLevel maxDifficulty) {
        Objects.requireNonNull(domain, "Domain must not be null");

        final var domainConcepts = Arrays.stream(ConceptArea.values())
                .filter(area -> area.getDomain() == domain)
                .toList();

        final var scored = vault.search(ResourceQuery.ALL).stream()
                .filter(r -> r.conceptAreas().stream().anyMatch(domainConcepts::contains))
                .map(r -> {
                    var bestScore = 0;
                    for (final var concept : domainConcepts) {
                        final var candidate = RelevanceScorer.scoreForConcept(
                                r, concept, minDifficulty, maxDifficulty);
                        if (candidate.score() > bestScore) {
                            bestScore = candidate.score();
                        }
                    }
                    return new ScoredResource(r, bestScore);
                })
                .sorted(Comparator.comparingInt(ScoredResource::score).reversed())
                .limit(MAX_RESULTS)
                .toList();

        final var conceptNames = domainConcepts.stream()
                .map(ConceptArea::getDisplayName)
                .collect(Collectors.joining(", "));

        return new DiscoveryResult(SearchMode.VAGUE, scored, List.of(),
                "Resources in domain: " + domain.getDisplayName()
                        + " (concepts: " + conceptNames + ")");
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

        return new DiscoveryResult(SearchMode.EXPLORATORY, scored,
                suggestRelatedCategories(category),
                "Explore " + category.getDisplayName() + " — starting with beginner-friendly resources");
    }

    // ─── Query Classification ───────────────────────────────────────

    /**
     * Classifies a normalised query string into one of three search modes.
     *
     * <p>Uses multiple signals: quoted terms, URL patterns, keyword lists,
     * query length, and keyword-index matches.
     *
     * @param input the normalised query
     * @return the classified {@link SearchMode}
     */
    SearchMode classifyQuery(final String input) {
        // Quoted text, URLs, or known specific triggers → SPECIFIC
        if (input.contains("\"") || input.contains("http")) {
            return SearchMode.SPECIFIC;
        }
        if (SPECIFIC_KEYWORDS.stream().anyMatch(input::contains)) {
            return SearchMode.SPECIFIC;
        }

        final var wordCount = input.split("\\s+").length;
        final var hasExploratoryKeyword = EXPLORATORY_KEYWORDS.stream().anyMatch(input::contains);

        // Short query with exploratory language → EXPLORATORY
        if (wordCount <= 5 && hasExploratoryKeyword) {
            return SearchMode.EXPLORATORY;
        }

        // Very short query with no keyword-index hit → EXPLORATORY
        if (wordCount <= 2
                && KeywordIndex.conceptMap().get(input) == null
                && KeywordIndex.categoryMap().get(input) == null) {
            return SearchMode.EXPLORATORY;
        }

        // Single word that is a difficulty keyword → EXPLORATORY
        if (wordCount == 1 && KeywordIndex.difficultyMap().containsKey(input)) {
            return SearchMode.EXPLORATORY;
        }

        return SearchMode.VAGUE;
    }

    // ─── Query Handlers ─────────────────────────────────────────────

    private DiscoveryResult handleSpecificQuery(final String input) {
        final var scored = vault.search(ResourceQuery.ALL).stream()
                .map(r -> RelevanceScorer.scoreForSpecific(r, input))
                .filter(sr -> sr.score() > 0)
                .sorted(Comparator.comparingInt(ScoredResource::score).reversed())
                .limit(MAX_RESULTS)
                .toList();

        return new DiscoveryResult(SearchMode.SPECIFIC, scored,
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

        return new DiscoveryResult(SearchMode.VAGUE, scored,
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

        return new DiscoveryResult(SearchMode.EXPLORATORY, scored, suggestions,
                "Here are recommended resources for " + targetDifficulty.getDisplayName() + " level learners");
    }

    private DiscoveryResult exploreDefault() {
        final var scored = vault.search(ResourceQuery.ALL).stream()
                .map(r -> RelevanceScorer.scoreForExploration(r, DifficultyLevel.BEGINNER))
                .sorted(Comparator.comparingInt(ScoredResource::score).reversed())
                .limit(MAX_RESULTS)
                .toList();

        return new DiscoveryResult(SearchMode.EXPLORATORY, scored,
                List.of("Try searching for a specific topic, category, or concept"),
                "Showing top recommended resources across all categories");
    }

    // ─── Intent Inference ───────────────────────────────────────────

    /**
     * Infers concept areas from the user's input using single-word and phrase matching.
     */
    private List<ConceptArea> inferConcepts(final String input) {
        final var result = new ArrayList<ConceptArea>();

        // Single-word matches
        for (final var word : input.split("\\s+")) {
            final var concept = KeywordIndex.conceptMap().get(word);
            if (concept != null && !result.contains(concept)) {
                result.add(concept);
            }
        }

        // Multi-word phrase matches (e.g., "design patterns", "virtual threads")
        for (final var entry : KeywordIndex.conceptMap().entrySet()) {
            if (entry.getKey().contains(" ") && input.contains(entry.getKey())
                    && !result.contains(entry.getValue())) {
                result.add(entry.getValue());
            }
        }

        // Fallback: single-word substring matches on remaining words
        if (result.isEmpty()) {
            for (final var word : input.split("\\s+")) {
                if (word.length() < 3) {
                    continue;
                }
                for (final var entry : KeywordIndex.conceptMap().entrySet()) {
                    if (entry.getKey().startsWith(word) && !result.contains(entry.getValue())) {
                        result.add(entry.getValue());
                    }
                }
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
                suggestions.add("Search concept: " + concept.name()
                        + " (" + concept.getDomain().getDisplayName() + ")");
            }
        }

        if (suggestions.isEmpty()) {
            suggestions.add("Try broader terms (e.g., 'java', 'testing', 'design patterns')");
            suggestions.add("Use 'list_categories' to see all available categories");
            suggestions.add("Use search mode: specific, vague, or exploratory");
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
                    .map(c -> c.name() + " (" + c.getDomain().getDisplayName() + ")")
                    .collect(Collectors.joining(", ")));
        }
        if (!categories.isEmpty()) {
            builder.append("\nInferred categories: ").append(categories.stream()
                    .map(ResourceCategory::getDisplayName).collect(Collectors.joining(", ")));
        }
        return builder.toString();
    }
}
