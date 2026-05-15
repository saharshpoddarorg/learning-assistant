package search.engine.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Generic keyword → value registry for query intent inference.
 *
 * <p>Used by domain engines to map user query words to domain-specific concepts
 * (e.g., "java" → {@code ResourceCategory.JAVA}, "concurrency" → {@code ConceptArea.CONCURRENCY}).
 *
 * <h2>Lookup strategies</h2>
 * <ul>
 *   <li>{@link #lookup(String)} — exact keyword match.</li>
 *   <li>{@link #inferFromQuery(String)} — extracts all matching values from a multi-word query:
 *     <ol>
 *       <li>Per-word exact match</li>
 *       <li>Multi-word phrase match (keyword contains space)</li>
 *       <li>Prefix fallback (first 3 chars)</li>
 *     </ol>
 *   </li>
 * </ul>
 *
 * <h2>Building a registry</h2>
 * <pre>{@code
 * KeywordRegistry<ConceptArea> registry = KeywordRegistry.<ConceptArea>builder()
 *         .register("concurrency", ConceptArea.CONCURRENCY)
 *         .register("threads",     ConceptArea.CONCURRENCY)
 *         .register("virtual threads", ConceptArea.CONCURRENCY)
 *         .register("java",        ConceptArea.OOP)
 *         .build();
 *
 * registry.inferFromQuery("learn java concurrency")
 * // → [ConceptArea.OOP, ConceptArea.CONCURRENCY]
 * }</pre>
 *
 * @param <V> the value type (e.g., a domain enum like {@code ConceptArea})
 */
public final class KeywordRegistry<V> {

    private final Map<String, V> keywordMap;

    private KeywordRegistry(final Map<String, V> keywordMap) {
        this.keywordMap = Map.copyOf(keywordMap);
    }

    /**
     * Returns the value for an exact keyword match, or {@code null} if not found.
     *
     * @param keyword the keyword to look up (normalised internally)
     * @return the mapped value or null
     */
    public V lookup(final String keyword) {
        if (keyword == null) return null;
        return keywordMap.get(keyword.strip().toLowerCase());
    }

    /**
     * Infers all matching values from a multi-word query using a three-pass strategy.
     *
     * @param query the raw or normalised query text
     * @return an unmodifiable list of inferred values, in discovery order
     */
    public List<V> inferFromQuery(final String query) {
        if (query == null || query.isBlank()) return List.of();
        final var normalised = query.strip().toLowerCase();
        final var result     = new ArrayList<V>();

        // Pass 1: per-word exact match
        for (final var word : normalised.split("\\s+")) {
            final var value = keywordMap.get(word);
            if (value != null && !result.contains(value)) result.add(value);
        }

        // Pass 2: multi-word phrase match (phrase keywords that appear in the query)
        for (final var entry : keywordMap.entrySet()) {
            if (entry.getKey().contains(" ") && normalised.contains(entry.getKey())
                    && !result.contains(entry.getValue())) {
                result.add(entry.getValue());
            }
        }

        // Pass 3: prefix fallback — if still empty, match via 3-char prefix
        if (result.isEmpty()) {
            for (final var word : normalised.split("\\s+")) {
                if (word.length() < 3) continue;
                final var prefix = word.substring(0, 3);
                for (final var entry : keywordMap.entrySet()) {
                    if (entry.getKey().startsWith(prefix) && !result.contains(entry.getValue())) {
                        result.add(entry.getValue());
                        break;
                    }
                }
            }
        }

        return List.copyOf(result);
    }

    /** Returns all registered keyword strings. */
    public Set<String> knownKeywords() { return keywordMap.keySet(); }

    /** Returns the underlying keyword → value map (unmodifiable). */
    public Map<String, V> asMap() { return keywordMap; }

    /** Returns the total number of registered keywords. */
    public int size() { return keywordMap.size(); }

    public static <V> Builder<V> builder() { return new Builder<>(); }

    /** Fluent builder for {@link KeywordRegistry}. */
    public static final class Builder<V> {

        private final Map<String, V> map = new HashMap<>();

        private Builder() {}

        /**
         * Registers a single keyword mapping (normalised to lowercase).
         *
         * @param keyword the keyword or phrase (never null/blank)
         * @param value   the domain value (never null)
         * @return this builder
         */
        public Builder<V> register(final String keyword, final V value) {
            Objects.requireNonNull(keyword, "keyword must not be null");
            Objects.requireNonNull(value,   "value must not be null");
            map.put(keyword.strip().toLowerCase(), value);
            return this;
        }

        /**
         * Registers all entries from a map (all keys normalised to lowercase).
         *
         * @param entries the entries to register (never null)
         * @return this builder
         */
        public Builder<V> registerAll(final Map<String, V> entries) {
            Objects.requireNonNull(entries, "entries must not be null");
            entries.forEach((k, v) -> map.put(k.strip().toLowerCase(), v));
            return this;
        }

        public KeywordRegistry<V> build() { return new KeywordRegistry<>(map); }
    }
}
