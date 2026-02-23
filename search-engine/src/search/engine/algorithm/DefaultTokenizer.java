package search.engine.algorithm;

import search.api.algorithm.Tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A production-ready tokenizer with stop-word removal and pattern normalisation.
 *
 * <h2>Processing pipeline</h2>
 * <ol>
 *   <li>Lowercase the text.</li>
 *   <li>Split on non-word characters (whitespace, punctuation).</li>
 *   <li>Discard tokens shorter than {@code minLength} (default 2).</li>
 *   <li>Discard English stop words.</li>
 * </ol>
 *
 * <h2>Why stop words matter</h2>
 * Without stop-word removal, terms like "the", "is", "for" appear in almost every
 * document and add noise to frequency-based scoring (especially BM25).
 *
 * <h2>Example</h2>
 * <pre>{@code
 * Tokenizer t = new DefaultTokenizer();
 * t.tokenize("Learn Java concurrency for beginners")
 * // → ["learn", "java", "concurrency", "beginners"]
 * }</pre>
 *
 * @see search.api.algorithm.Tokenizer
 * @see Bm25Scorer
 */
public final class DefaultTokenizer implements Tokenizer {

    /** Common English stop words that carry no retrieval signal. */
    private static final Set<String> STOP_WORDS = Set.of(
            "a", "an", "the", "and", "or", "but", "in", "on", "at", "to",
            "for", "of", "with", "by", "from", "up", "as", "is", "are",
            "was", "were", "be", "been", "being", "have", "has", "had",
            "do", "does", "did", "will", "would", "could", "should", "may",
            "might", "shall", "can", "not", "it", "its", "this", "that",
            "these", "those", "how", "what", "when", "where", "who", "which",
            "i", "you", "he", "she", "we", "they", "my", "your", "our",
            "their", "all", "any", "more", "also", "very", "about", "into",
            "other", "than", "then", "so", "if", "no", "get", "use",
            "used", "using", "just", "some", "like", "well", "new"
    );

    private final int minTokenLength;

    /** Creates a tokenizer with default minimum token length of 2. */
    public DefaultTokenizer() {
        this(2);
    }

    /**
     * Creates a tokenizer with a custom minimum token length.
     *
     * @param minTokenLength tokens shorter than this are discarded (minimum 1)
     */
    public DefaultTokenizer(final int minTokenLength) {
        this.minTokenLength = Math.max(1, minTokenLength);
    }

    @Override
    public List<String> tokenize(final String text) {
        if (text == null || text.isBlank()) return List.of();

        final var result = new ArrayList<String>();
        for (final var token : text.toLowerCase().split("[\\W_]+")) {
            if (token.length() >= minTokenLength && !STOP_WORDS.contains(token)) {
                result.add(token);
            }
        }
        return List.copyOf(result);
    }

    /** Returns the set of stop words this tokenizer removes. */
    public Set<String> stopWords() { return STOP_WORDS; }
}
