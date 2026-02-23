package search.api.core;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Explains how a document's relevance score was computed — for developer/debug mode.
 *
 * <p>A {@code ScoreBreakdown} records the contribution of each scoring dimension
 * (exact title, tags, concepts, official boost, etc.) so developers can understand
 * why a document ranked the way it did.
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * ScoreBreakdown bd = ScoreBreakdown.of("exactTitle", 100, "tagMatch", 30, "officialBoost", 15);
 * // bd.total() == 145
 * // bd.componentNames() == ["exactTitle", "tagMatch", "officialBoost"]
 * // bd.get("tagMatch") == 30
 * }</pre>
 *
 * <p>Attach to a {@link ScoredItem} only in debug builds to avoid memory overhead.
 *
 * @see ScoredItem
 */
public final class ScoreBreakdown {

    private final Map<String, Integer> components;
    private final int total;

    private ScoreBreakdown(final Map<String, Integer> components) {
        this.components = Collections.unmodifiableMap(new LinkedHashMap<>(components));
        this.total = components.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Creates a breakdown from an ordered list of (name, points) pairs.
     *
     * @param namePointPairs alternating name (String) and points (int) entries
     * @return a new breakdown
     * @throws IllegalArgumentException if the array length is not even
     */
    public static ScoreBreakdown of(final Object... namePointPairs) {
        if (namePointPairs.length % 2 != 0) {
            throw new IllegalArgumentException("Parameters must be (String name, int points) pairs");
        }
        final var map = new LinkedHashMap<String, Integer>();
        for (var i = 0; i < namePointPairs.length; i += 2) {
            final var name   = Objects.toString(namePointPairs[i]);
            final var points = (Integer) namePointPairs[i + 1];
            map.merge(name, points, Integer::sum);
        }
        return new ScoreBreakdown(map);
    }

    /** Returns a mutable builder for this breakdown. */
    public static Builder builder() { return new Builder(); }

    /** Returns the points contributed by the named component, or 0 if absent. */
    public int get(final String componentName) {
        return components.getOrDefault(componentName, 0);
    }

    /** Returns all component names in insertion order. */
    public Iterable<String> componentNames() { return components.keySet(); }

    /** Returns the total relevance score (sum of all components). */
    public int total() { return total; }

    /** Returns all components as an unmodifiable map. */
    public Map<String, Integer> all() { return components; }

    @Override
    public String toString() {
        final var sb = new StringBuilder("ScoreBreakdown{total=").append(total).append(": ");
        components.forEach((name, pts) -> sb.append(name).append('=').append(pts).append(' '));
        return sb.append('}').toString();
    }

    /** Fluent builder for {@link ScoreBreakdown}. Thread-safe only if not shared across threads. */
    public static final class Builder {

        private final LinkedHashMap<String, Integer> components = new LinkedHashMap<>();

        private Builder() {}

        /**
         * Adds points to a named component (accumulated if the name is used twice).
         *
         * @param name   the component name (e.g., {@code "exactTitle"})
         * @param points the points to add
         * @return this builder
         */
        public Builder add(final String name, final int points) {
            Objects.requireNonNull(name, "Component name must not be null");
            if (points != 0) {
                components.merge(name, points, Integer::sum);
            }
            return this;
        }

        /** @return a new immutable {@link ScoreBreakdown} */
        public ScoreBreakdown build() {
            return new ScoreBreakdown(components);
        }

        /** Returns the current accumulated total. */
        public int currentTotal() {
            return components.values().stream().mapToInt(Integer::intValue).sum();
        }
    }
}
