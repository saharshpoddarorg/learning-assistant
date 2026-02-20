package server.learningresources.vault;

import server.learningresources.model.ConceptArea;
import server.learningresources.model.ContentFreshness;
import server.learningresources.model.DifficultyLevel;
import server.learningresources.model.LearningResource;
import server.learningresources.model.ResourceCategory;

import java.util.List;
import java.util.Objects;

/**
 * Computes relevance scores for learning resources across multiple dimensions.
 *
 * <p>Each scoring method applies a different strategy depending on query intent:
 * <ul>
 *   <li><strong>Specific</strong> — heavy weight on exact text/title match</li>
 *   <li><strong>Vague</strong> — balanced across text, concept, and category alignment</li>
 *   <li><strong>Concept</strong> — concept match + official/freshness boost</li>
 *   <li><strong>Exploration</strong> — difficulty fit + official + beginner-friendly bias</li>
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

    /** Resource is an official/authoritative source. */
    static final int OFFICIAL_BOOST = 15;

    /** Resource is actively maintained. */
    static final int FRESHNESS_BOOST = 10;

    /** Resource difficulty matches the target range. */
    static final int DIFFICULTY_FIT = 10;

    private RelevanceScorer() {
        // Static utility — no instances
    }

    // ─── Scoring Functions ──────────────────────────────────────────

    /**
     * Scores a resource for a specific, targeted text query.
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

        for (final var word : input.split("\\s+")) {
            if (word.length() < 3) {
                continue;
            }
            if (titleLower.contains(word)) {
                score += TAG_MATCH;
            }
            if (resource.tags().stream().anyMatch(tag -> tag.contains(word))) {
                score += TAG_MATCH;
            }
        }

        if (resource.isOfficial()) {
            score += OFFICIAL_BOOST;
        }

        return new ScoredResource(resource, score);
    }

    /**
     * Scores a resource for a vague, topic-level query with inferred enums.
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

        for (final var word : input.split("\\s+")) {
            if (word.length() < 3) {
                continue;
            }
            if (searchable.contains(word)) {
                score += TAG_MATCH;
            }
        }

        for (final var concept : inferredConcepts) {
            if (resource.hasConcept(concept)) {
                score += CONCEPT_MATCH;
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

        return new ScoredResource(resource, score);
    }

    /**
     * Scores a resource for a concept-targeted query with optional difficulty range.
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
        var score = CONCEPT_MATCH;

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

        return new ScoredResource(resource, score);
    }

    /**
     * Scores a resource for exploration — favours beginner-friendly, official,
     * getting-started resources.
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
        } else if (resource.difficulty().getLevel() <= targetDifficulty.getLevel() + 1) {
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

        return new ScoredResource(resource, score);
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
