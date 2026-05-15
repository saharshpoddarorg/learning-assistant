package search.api.core;

import search.api.classify.SearchMode;

import java.util.Map;
import java.util.Objects;

/**
 * Encapsulates everything a {@link SearchEngine} needs to execute a search:
 * the raw user input, an optional forced mode, typed filter parameters, and a result limit.
 *
 * <h2>Building a context</h2>
 * <pre>{@code
 * // Defaults: auto-classify mode, 15 results, no filters
 * SearchContext.of("java concurrency beginner")
 *
 * // Force a mode, override limit
 * SearchContext.of("JUnit 5 docs", SearchMode.SPECIFIC, 5)
 *
 * // Full control with filters
 * new SearchContext("design patterns", SearchMode.VAGUE,
 *                   Map.of("difficulty", "INTERMEDIATE", "officialOnly", true), 20)
 * }</pre>
 *
 * <h2>Filters map</h2>
 * Arbitrary key → value pairs. {@link search.api.filter.SearchFilter} implementations
 * read them via {@link #getFilter(String, Class)}. The key/type contract between
 * the engine and its filters is established per-domain.
 *
 * @param rawInput    the original user query (never null)
 * @param forcedMode  when non-null, skips auto-classification
 * @param filters     optional filter parameters; immutable copy is made
 * @param maxResults  the result-count ceiling (clamped to &gt; 0)
 */
public record SearchContext(
        String rawInput,
        SearchMode forcedMode,
        Map<String, Object> filters,
        int maxResults
) {

    /** Default maximum results when none are specified. */
    public static final int DEFAULT_MAX_RESULTS = 15;

    /** Compact canonical constructor — validates and defensively copies inputs. */
    public SearchContext {
        Objects.requireNonNull(rawInput, "rawInput must not be null");
        filters = (filters != null) ? Map.copyOf(filters) : Map.of();
        if (maxResults <= 0) {
            maxResults = DEFAULT_MAX_RESULTS;
        }
    }

    // ─── Factory shortcuts ──────────────────────────────────────────

    /** Creates a context with auto-classification and default limits. */
    public static SearchContext of(final String rawInput) {
        return new SearchContext(rawInput, null, Map.of(), DEFAULT_MAX_RESULTS);
    }

    /** Creates a context with a forced mode and default limits. */
    public static SearchContext of(final String rawInput, final SearchMode forcedMode) {
        return new SearchContext(rawInput, forcedMode, Map.of(), DEFAULT_MAX_RESULTS);
    }

    /** Creates a context with a forced mode and custom limit. */
    public static SearchContext of(final String rawInput, final SearchMode forcedMode,
                                   final int maxResults) {
        return new SearchContext(rawInput, forcedMode, Map.of(), maxResults);
    }

    /** Creates a context with no forced mode but a custom limit. */
    public static SearchContext of(final String rawInput, final int maxResults) {
        return new SearchContext(rawInput, null, Map.of(), maxResults);
    }

    // ─── Convenience accessors ──────────────────────────────────────

    /**
     * Returns a normalised (trimmed, lowercased) view of the raw input.
     * This is what classifiers and scorers should work with.
     *
     * @return normalised input
     */
    public String normalizedInput() {
        return rawInput.strip().toLowerCase();
    }

    /** Returns {@code true} if a mode has been forced (skip auto-classification). */
    public boolean hasForcedMode() {
        return forcedMode != null;
    }

    /**
     * Retrieves a typed filter value by key, or {@code null} if absent or wrong type.
     *
     * @param key          the filter key
     * @param expectedType the expected value type
     * @param <V>          the value type
     * @return the value cast to V, or null
     */
    @SuppressWarnings("unchecked")
    public <V> V getFilter(final String key, final Class<V> expectedType) {
        final var value = filters.get(key);
        return expectedType.isInstance(value) ? (V) value : null;
    }
}
