package server.learningresources.content;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Estimates content difficulty based on text analysis heuristics.
 *
 * <p>Uses a combination of signals to classify content as beginner,
 * intermediate, or advanced:
 * <ul>
 *   <li>Average sentence length</li>
 *   <li>Presence of advanced technical keywords</li>
 *   <li>Code block density</li>
 *   <li>Overall text complexity</li>
 * </ul>
 */
public class ReadabilityScorer {

    private static final Logger LOGGER = Logger.getLogger(ReadabilityScorer.class.getName());

    /** Difficulty level constants. */
    public static final String BEGINNER = "beginner";
    public static final String INTERMEDIATE = "intermediate";
    public static final String ADVANCED = "advanced";

    // Scoring thresholds
    private static final double ADVANCED_THRESHOLD = 0.65;
    private static final double INTERMEDIATE_THRESHOLD = 0.35;

    // Weights for composite score
    private static final double KEYWORD_WEIGHT = 0.4;
    private static final double SENTENCE_LENGTH_WEIGHT = 0.3;
    private static final double VOCABULARY_WEIGHT = 0.3;

    // Reference values
    private static final double LONG_SENTENCE_THRESHOLD = 25.0;
    private static final double SHORT_SENTENCE_REFERENCE = 12.0;

    private static final String[] ADVANCED_KEYWORDS = {
            "concurrency", "thread-safe", "lock-free", "memory model", "garbage collector",
            "jit compiler", "bytecode", "classloader", "metaclass", "monomorphism",
            "polymorphic inline cache", "escape analysis", "safepoint", "intrinsic",
            "reification", "covariance", "contravariance", "higher-kinded",
            "monad", "functor", "continuation", "coroutine", "fiber",
            "distributed consensus", "raft", "paxos", "linearizability",
            "eventual consistency", "cqrs", "event sourcing", "saga pattern",
            "backpressure", "circuit breaker", "bulkhead", "idempotent"
    };

    private static final String[] INTERMEDIATE_KEYWORDS = {
            "generics", "lambda", "stream", "optional", "record",
            "sealed", "pattern matching", "virtual thread", "structured concurrency",
            "dependency injection", "inversion of control", "repository pattern",
            "factory method", "builder pattern", "observer pattern",
            "rest api", "microservice", "docker", "kubernetes",
            "transaction", "index", "query optimization", "connection pool"
    };

    /**
     * Estimates the difficulty level of the given text content.
     *
     * @param text the text to analyze
     * @return difficulty level: "beginner", "intermediate", or "advanced"
     */
    public String estimateDifficulty(final String text) {
        Objects.requireNonNull(text, "Text must not be null");

        if (text.isBlank()) {
            return BEGINNER;
        }

        final var lowerText = text.toLowerCase();
        final var compositeScore = calculateCompositeScore(lowerText);

        final var difficulty = classifyScore(compositeScore);
        LOGGER.fine("Difficulty score: " + String.format("%.2f", compositeScore) + " → " + difficulty);

        return difficulty;
    }

    /**
     * Calculates a composite difficulty score from multiple signals.
     *
     * @param lowerText the lowercase text to analyze
     * @return a score between 0.0 (easiest) and 1.0 (hardest)
     */
    private double calculateCompositeScore(final String lowerText) {
        final var keywordScore = calculateKeywordScore(lowerText);
        final var sentenceLengthScore = calculateSentenceLengthScore(lowerText);
        final var vocabularyScore = calculateVocabularyScore(lowerText);

        return (keywordScore * KEYWORD_WEIGHT)
                + (sentenceLengthScore * SENTENCE_LENGTH_WEIGHT)
                + (vocabularyScore * VOCABULARY_WEIGHT);
    }

    /**
     * Scores based on presence of advanced and intermediate keywords.
     *
     * @param lowerText the text to scan (lowercase)
     * @return score between 0.0 and 1.0
     */
    private double calculateKeywordScore(final String lowerText) {
        var advancedCount = 0;
        for (final var keyword : ADVANCED_KEYWORDS) {
            if (lowerText.contains(keyword)) {
                advancedCount++;
            }
        }

        var intermediateCount = 0;
        for (final var keyword : INTERMEDIATE_KEYWORDS) {
            if (lowerText.contains(keyword)) {
                intermediateCount++;
            }
        }

        // Normalize: even 3+ advanced keywords → high score
        final var advancedNormalized = Math.min(1.0, advancedCount / 3.0);
        final var intermediateNormalized = Math.min(1.0, intermediateCount / 5.0);

        return (advancedNormalized * 0.7) + (intermediateNormalized * 0.3);
    }

    /**
     * Scores based on average sentence length (longer = more complex).
     *
     * @param lowerText the text to analyze
     * @return score between 0.0 and 1.0
     */
    private double calculateSentenceLengthScore(final String lowerText) {
        final var sentences = lowerText.split("[.!?]+");
        if (sentences.length == 0) {
            return 0.0;
        }

        var totalWords = 0;
        for (final var sentence : sentences) {
            totalWords += sentence.trim().split("\\s+").length;
        }

        final var averageSentenceLength = (double) totalWords / sentences.length;

        // Sentences averaging >25 words = complex; <12 words = simple
        return Math.min(1.0, Math.max(0.0,
                (averageSentenceLength - SHORT_SENTENCE_REFERENCE)
                        / (LONG_SENTENCE_THRESHOLD - SHORT_SENTENCE_REFERENCE)));
    }

    /**
     * Scores based on unique word ratio (vocabulary richness).
     *
     * @param lowerText the text to analyze
     * @return score between 0.0 and 1.0
     */
    private double calculateVocabularyScore(final String lowerText) {
        final var words = lowerText.split("\\s+");
        if (words.length == 0) {
            return 0.0;
        }

        final var uniqueWords = new HashSet<>(Arrays.asList(words)).size();
        final var ratio = (double) uniqueWords / words.length;

        // Higher unique-word ratio → more complex vocabulary
        // Typical range: 0.3 (repetitive) to 0.8 (diverse)
        return Math.min(1.0, Math.max(0.0, (ratio - 0.3) / 0.5));
    }

    /**
     * Classifies a composite score into a difficulty label.
     *
     * @param score the composite score (0.0–1.0)
     * @return the difficulty label
     */
    private String classifyScore(final double score) {
        if (score >= ADVANCED_THRESHOLD) {
            return ADVANCED;
        }
        if (score >= INTERMEDIATE_THRESHOLD) {
            return INTERMEDIATE;
        }
        return BEGINNER;
    }
}
