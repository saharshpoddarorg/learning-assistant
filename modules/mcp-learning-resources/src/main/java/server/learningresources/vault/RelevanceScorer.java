package server.learningresources.vault;

import server.learningresources.model.ConceptArea;
import server.learningresources.model.ConceptDomain;
import server.learningresources.model.ContentFreshness;
import server.learningresources.model.DifficultyLevel;
import server.learningresources.model.LanguageApplicability;
import server.learningresources.model.LearningResource;
import server.learningresources.model.ResourceCategory;

import java.util.List;
import java.util.Objects;

/**
 * Computes relevance scores for learning resources across multiple dimensions.
 *
 * <p>Each scoring method applies a different strategy depending on query intent:
 * <ul>
 *   <li><strong>Specific</strong> — heavy weight on exact text/title match + fuzzy fallback</li>
 *   <li><strong>Vague</strong> — balanced across text, concept, category, domain, and language alignment</li>
 *   <li><strong>Concept</strong> — concept match + domain affinity + official/freshness boost</li>
 *   <li><strong>Exploration</strong> — difficulty fit + official + beginner-friendly + language preference</li>
 * </ul>
 *
 * <p>Scoring constants are centralised here so tuning the ranking algorithm
 * does not require touching the discovery or query-handling logic.
 */
public final class RelevanceScorer {

    // ─── Scoring Weights ────────────────────────────────────────────

    /** Exact title string match. */
    static final int EXACT_TITLE_MATCH = 100;

    /** Title contains the search string. */
    static final int PARTIAL_TITLE_MATCH = 40;

    /** Description / full-text contains the search string. */
    static final int DESCRIPTION_MATCH = 20;

    /** Individual tag or word match. */
    static final int TAG_MATCH = 15;

    /** Resource covers an inferred concept area. */
    static final int CONCEPT_MATCH = 25;

    /** Resource belongs to an inferred category. */
    static final int CATEGORY_MATCH = 20;

    /** Resource concept is in the same domain as an inferred concept. */
    static final int DOMAIN_AFFINITY = 10;

    /** Resource is an official/authoritative source. */
    static final int OFFICIAL_BOOST = 15;

    /** Resource is actively maintained. */
    static final int FRESHNESS_BOOST = 10;

    /** Resource difficulty matches the target range. */
    static final int DIFFICULTY_FIT = 10;

    /** Resource's language applicability aligns with the user's likely preference. */
    static final int LANGUAGE_FIT = 12;

    /** Fuzzy substring match (partial word overlap). */
    static final int FUZZY_MATCH = 8;

    /** Minimum word length for fuzzy matching. */
    private static final int MIN_FUZZY_WORD_LENGTH = 4;

    /** Minimum shared prefix length to count as a fuzzy match. */
    private static final int MIN_FUZZY_PREFIX = 3;

    private RelevanceScorer() {
        // Static utility — no instances
    }

    // ─── Scoring Functions ──────────────────────────────────────────

    /**
     * Scores a resource for a specific, targeted text query.
     *
     * <p>Applies exact matching first, then falls back to fuzzy prefix matching
     * for typo tolerance. Also considers language applicability.
     *
     * @param resource the candidate resource
     * @param input    the normalised (lowercase) search text
     * @return a {@link ScoredResource} with computed relevance
     */
    public static ScoredResource scoreForSpecific(final LearningResource resource,
                                                  final String input) {
        var score = 0;
        final var searchable = resource.searchableText().toLowerCase();
        final var titleLower = resource.title().toLowerCase();

        // Exact and partial title matching
        if (titleLower.equals(input)) {
            score += EXACT_TITLE_MATCH;
        } else if (titleLower.contains(input)) {
            score += PARTIAL_TITLE_MATCH;
        }

        if (resource.id().toLowerCase().contains(input)) {
            score += PARTIAL_TITLE_MATCH;
        }

        if (searchable.contains(input)) {
            score += DESCRIPTION_MATCH;
        }

        // Per-word tag matching + fuzzy fallback
        for (final var word : input.split("\\s+")) {
            if (word.length() < 3) {
                continue;
            }
            if (titleLower.contains(word)) {
                score += TAG_MATCH;
            } else if (hasFuzzyMatch(word, titleLower)) {
                score += FUZZY_MATCH;
            }
            if (resource.tags().stream().anyMatch(tag -> tag.contains(word))) {
                score += TAG_MATCH;
            }
        }

        // Official + language bonuses
        if (resource.isOfficial()) {
            score += OFFICIAL_BOOST;
        }
        score += scoreLanguageApplicability(resource.languageApplicability());

        return new ScoredResource(resource, score);
    }

    /**
     * Scores a resource for a vague, topic-level query with inferred enums.
     *
     * <p>Uses keyword-to-concept inference and adds domain affinity scoring
     * when a resource's concepts share a domain with inferred concepts.
     *
     * @param resource           the candidate resource
     * @param input              the normalised search text
     * @param inferredConcepts   concepts inferred from the user's keywords
     * @param inferredCategories categories inferred from the user's keywords
     * @return a {@link ScoredResource} with computed relevance
     */
    public static ScoredResource scoreForVague(final LearningResource resource,
                                               final String input,
                                               final List<ConceptArea> inferredConcepts,
                                               final List<ResourceCategory> inferredCategories) {
        var score = 0;
        final var searchable = resource.searchableText().toLowerCase();

        // Per-word text matching + fuzzy fallback
        for (final var word : input.split("\\s+")) {
            if (word.length() < 3) {
                continue;
            }
            if (searchable.contains(word)) {
                score += TAG_MATCH;
            } else if (hasFuzzyMatch(word, searchable)) {
                score += FUZZY_MATCH;
            }
        }

        // Concept and domain affinity scoring
        for (final var concept : inferredConcepts) {
            if (resource.hasConcept(concept)) {
                score += CONCEPT_MATCH;
            } else if (hasConceptInSameDomain(resource, concept.getDomain())) {
                score += DOMAIN_AFFINITY;
            }
        }

        for (final var category : inferredCategories) {
            if (resource.categories().contains(category)) {
                score += CATEGORY_MATCH;
            }
        }

        if (resource.isOfficial()) {
            score += OFFICIAL_BOOST;
        }
        if (resource.freshness() == ContentFreshness.ACTIVELY_MAINTAINED) {
            score += FRESHNESS_BOOST;
        }
        score += scoreLanguageApplicability(resource.languageApplicability());

        return new ScoredResource(resource, score);
    }

    /**
     * Scores a resource for a concept-targeted query with optional difficulty range.
     *
     * <p>Adds domain affinity — resources in the same domain as the target concept
     * receive a partial bonus even if they don't cover the exact concept.
     *
     * @param resource      the candidate resource
     * @param concept       the target concept area
     * @param minDifficulty minimum difficulty (null = BEGINNER)
     * @param maxDifficulty maximum difficulty (null = EXPERT)
     * @return a {@link ScoredResource} with computed relevance
     */
    public static ScoredResource scoreForConcept(final LearningResource resource,
                                                 final ConceptArea concept,
                                                 final DifficultyLevel minDifficulty,
                                                 final DifficultyLevel maxDifficulty) {
        var score = 0;

        // Direct concept match vs domain affinity
        if (resource.hasConcept(concept)) {
            score += CONCEPT_MATCH * 2;
        } else if (hasConceptInSameDomain(resource, concept.getDomain())) {
            score += DOMAIN_AFFINITY;
        }

        if (resource.isOfficial()) {
            score += OFFICIAL_BOOST;
        }
        if (resource.freshness() == ContentFreshness.ACTIVELY_MAINTAINED) {
            score += FRESHNESS_BOOST;
        }

        final var effectiveMin = minDifficulty != null ? minDifficulty : DifficultyLevel.BEGINNER;
        final var effectiveMax = maxDifficulty != null ? maxDifficulty : DifficultyLevel.EXPERT;
        if (resource.isDifficultyInRange(effectiveMin, effectiveMax)) {
            score += DIFFICULTY_FIT;
        }

        score += scoreLanguageApplicability(resource.languageApplicability());

        return new ScoredResource(resource, score);
    }

    /**
     * Scores a resource for exploration — favours beginner-friendly, official,
     * getting-started resources with broad language applicability.
     *
     * @param resource         the candidate resource
     * @param targetDifficulty the preferred difficulty level
     * @return a {@link ScoredResource} with computed relevance
     */
    public static ScoredResource scoreForExploration(final LearningResource resource,
                                                     final DifficultyLevel targetDifficulty) {
        var score = 0;

        if (resource.difficulty() == targetDifficulty) {
            score += DIFFICULTY_FIT * 2;
        } else if (resource.difficulty().getOrdinalLevel() <= targetDifficulty.getOrdinalLevel() + 1) {
            score += DIFFICULTY_FIT;
        }

        if (resource.hasConcept(ConceptArea.GETTING_STARTED)) {
            score += CONCEPT_MATCH;
        }

        if (resource.isOfficial()) {
            score += OFFICIAL_BOOST * 2;
        }

        if (resource.freshness() == ContentFreshness.ACTIVELY_MAINTAINED) {
            score += FRESHNESS_BOOST;
        }

        if (resource.isFree()) {
            score += 5;
        }

        // Prefer universal/multi-language for explorers
        score += scoreLanguageApplicability(resource.languageApplicability());

        return new ScoredResource(resource, score);
    }

    // ─── Fuzzy Matching ─────────────────────────────────────────────

    /**
     * Checks for a fuzzy prefix match between a query word and target text.
     *
     * <p>Considers it a match if any word in the target shares at least
     * {@value MIN_FUZZY_PREFIX} leading characters with the query word.
     * This provides basic typo tolerance without full edit-distance computation.
     *
     * @param queryWord the user's search word
     * @param target    the text to match against
     * @return {@code true} if a fuzzy match is found
     */
    static boolean hasFuzzyMatch(final String queryWord, final String target) {
        if (queryWord.length() < MIN_FUZZY_WORD_LENGTH) {
            return false;
        }
        final var prefix = queryWord.substring(0, MIN_FUZZY_PREFIX);
        for (final var targetWord : target.split("\\W+")) {
            if (targetWord.length() >= MIN_FUZZY_PREFIX && targetWord.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    // ─── Language Applicability Scoring ──────────────────────────────

    /**
     * Returns a bonus score based on how broadly applicable a resource is.
     *
     * <p>Universal and multi-language resources get the highest bonus since
     * they benefit learners regardless of their language preference.
     *
     * @param applicability the resource's language applicability
     * @return the language bonus (0–{@value LANGUAGE_FIT})
     */
    private static int scoreLanguageApplicability(final LanguageApplicability applicability) {
        return switch (applicability) {
            case UNIVERSAL -> LANGUAGE_FIT;
            case MULTI_LANGUAGE -> LANGUAGE_FIT - 2;
            case JAVA_CENTRIC -> LANGUAGE_FIT - 4;
            case JAVA_SPECIFIC -> LANGUAGE_FIT - 6;
            case PYTHON_SPECIFIC, WEB_SPECIFIC -> 0;
        };
    }

    // ─── Domain Affinity ────────────────────────────────────────────

    /**
     * Checks if a resource covers any concept in the given domain.
     *
     * @param resource the resource to check
     * @param domain   the target domain
     * @return {@code true} if the resource has at least one concept in that domain
     */
    private static boolean hasConceptInSameDomain(final LearningResource resource,
                                                  final ConceptDomain domain) {
        return resource.conceptAreas().stream()
                .anyMatch(area -> area.getDomain() == domain);
    }

    // ─── Result Record ──────────────────────────────────────────────

    /**
     * A learning resource paired with a relevance score.
     *
     * @param resource the matched resource
     * @param score    the computed relevance (higher is more relevant)
     */
    public record ScoredResource(LearningResource resource, int score) {

        /**
         * Creates a scored resource.
         *
         * @param resource the matched resource
         * @param score    the relevance score
         */
        public ScoredResource {
            Objects.requireNonNull(resource, "Resource must not be null");
        }
    }
}
