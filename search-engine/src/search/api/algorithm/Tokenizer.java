package search.api.algorithm;

import java.util.List;

/**
 * Splits raw text into tokens for scoring and indexing.
 *
 * <p>A {@code Tokenizer} normalises, stop-word-filters, and optionally stems text
 * before it is used in {@link ScoringStrategy} implementations. Keeping tokenization
 * separate from scoring lets you swap normalization rules without touching scoring logic.
 *
 * <h2>Built-in implementations</h2>
 * <ul>
 *   <li>{@link search.engine.algorithm.DefaultTokenizer} — stop-word removal, lowercase, length filter</li>
 *   <li>{@link #identity()} — splits on whitespace only; no filtering</li>
 * </ul>
 *
 * <h2>Contract</h2>
 * <ul>
 *   <li>Input may be null or blank — return {@code List.of()} in that case.</li>
 *   <li>Returned list is unmodifiable.</li>
 *   <li>Implementations must be stateless and thread-safe.</li>
 * </ul>
 *
 * @see search.engine.algorithm.DefaultTokenizer
 */
@FunctionalInterface
public interface Tokenizer {

    /**
     * Tokenizes the given text.
     *
     * @param text raw text (may be null)
     * @return immutable list of meaningful tokens; never null
     */
    List<String> tokenize(String text);

    // ─── Built-in factory tokenizers ──────────────────────────────

    /**
     * Returns a tokenizer that just splits on whitespace and lowercases.
     * No stop-word removal or stemming.
     *
     * @return the identity tokenizer
     */
    static Tokenizer identity() {
        return text -> {
            if (text == null || text.isBlank()) return List.of();
            return List.of(text.strip().toLowerCase().split("\\s+"));
        };
    }
}
