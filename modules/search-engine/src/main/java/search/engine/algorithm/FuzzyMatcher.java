package search.engine.algorithm;

/**
 * Pure-static fuzzy text matching utilities used by scoring strategies.
 *
 * <p>All methods are null-safe, stateless, and thread-safe.
 *
 * <h2>Matching hierarchy (strictest → loosest)</h2>
 * <ol>
 *   <li>Exact substring — query word appears as-is in target</li>
 *   <li>Prefix match — query word shares a prefix with a target word</li>
 *   <li>Substring match — a target word contains the query word (or vice-versa)</li>
 * </ol>
 *
 * @see TextMatchScorer
 * @see Bm25Scorer
 */
public final class FuzzyMatcher {

    /** Minimum query-word length required for fuzzy prefix matching. */
    public static final int DEFAULT_MIN_WORD_LENGTH = 4;

    /** Number of characters at the start of a word used for prefix comparison. */
    public static final int DEFAULT_PREFIX_LENGTH = 3;

    private FuzzyMatcher() {}

    // ─── Prefix matching ──────────────────────────────────────────

    /**
     * Returns {@code true} if any word in {@code target} starts with the first
     * {@link #DEFAULT_PREFIX_LENGTH} characters of {@code queryWord}.
     *
     * @param queryWord the query term
     * @param target    the text to search within
     * @return true on a prefix match
     */
    public static boolean hasPrefixMatch(final String queryWord, final String target) {
        return hasPrefixMatch(queryWord, target, DEFAULT_MIN_WORD_LENGTH, DEFAULT_PREFIX_LENGTH);
    }

    /**
     * Returns {@code true} if any word in {@code target} starts with the first
     * {@code prefixLen} characters of {@code queryWord}.
     *
     * @param queryWord  the query term
     * @param target     the text to search within
     * @param minWordLen minimum query-word length to attempt prefix matching
     * @param prefixLen  prefix length to compare
     * @return true on a prefix match
     */
    public static boolean hasPrefixMatch(final String queryWord, final String target,
                                          final int minWordLen, final int prefixLen) {
        if (queryWord == null || queryWord.length() < minWordLen) return false;
        if (target == null || target.isBlank()) return false;
        final var prefix = queryWord.substring(0, Math.min(prefixLen, queryWord.length()));
        for (final var targetWord : target.split("\\W+")) {
            if (targetWord.length() >= prefixLen && targetWord.startsWith(prefix)) return true;
        }
        return false;
    }

    // ─── Substring matching ───────────────────────────────────────

    /**
     * Returns {@code true} if any word in {@code target} contains {@code queryWord}
     * as a substring.
     *
     * @param queryWord the query term (minimum length 3)
     * @param target    the text to search within
     * @return true on a substring match
     */
    public static boolean hasSubstringMatch(final String queryWord, final String target) {
        if (queryWord == null || queryWord.length() < 3) return false;
        if (target == null || target.isBlank()) return false;
        for (final var targetWord : target.split("\\W+")) {
            if (targetWord.contains(queryWord)) return true;
        }
        return false;
    }

    // ─── Utility scoring ──────────────────────────────────────────

    /**
     * Returns a score based on the best fuzzy match between a query word and
     * the full target text. Awards points in declining order: exact → prefix → fuzzy.
     *
     * @param queryWord      the query term (minimum length 2)
     * @param target         the text to match against
     * @param exactPoints    points for an exact substring hit
     * @param prefixPoints   points for a prefix hit
     * @param fuzzyPoints    points for a substring hit
     * @return the best match score, or 0 if no match
     */
    public static int scoreWord(final String queryWord, final String target,
                                 final int exactPoints, final int prefixPoints,
                                 final int fuzzyPoints) {
        if (queryWord == null || queryWord.length() < 2 || target == null) return 0;
        if (target.contains(queryWord)) return exactPoints;
        if (hasPrefixMatch(queryWord, target)) return prefixPoints;
        if (hasSubstringMatch(queryWord, target)) return fuzzyPoints;
        return 0;
    }

    /**
     * Returns the count of times {@code term} appears (as a complete word) in {@code text}.
     * Used by BM25 term-frequency computation.
     *
     * @param term the search term (lowercased)
     * @param text the document text (lowercased)
     * @return term frequency (≥ 0)
     */
    public static int termFrequency(final String term, final String text) {
        if (term == null || text == null || term.isBlank() || text.isBlank()) return 0;
        var count = 0;
        var start = 0;
        while ((start = text.indexOf(term, start)) != -1) {
            count++;
            start += term.length();
        }
        return count;
    }
}
