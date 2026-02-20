package server.learningresources.vault;

import server.learningresources.vault.RelevanceScorer.ScoredResource;

import java.util.List;
import java.util.Objects;

/**
 * The complete result of a resource discovery query.
 *
 * <p>Bundles the classified query type, scored results, follow-up suggestions,
 * and a human-readable summary. This is the primary return type from
 * {@link ResourceDiscovery#discover(String)}.
 *
 * @param queryType   how the query was classified (specific, vague, exploratory)
 * @param results     scored resources sorted by relevance (descending)
 * @param suggestions "did you mean?" or follow-up suggestions
 * @param summary     human-readable summary of the search
 */
public record DiscoveryResult(
        QueryType queryType,
        List<ScoredResource> results,
        List<String> suggestions,
        String summary
) {

    /**
     * Creates a discovery result with defensive copies.
     *
     * @param queryType   the classified query type
     * @param results     scored and sorted results
     * @param suggestions follow-up suggestions
     * @param summary     human-readable summary
     */
    public DiscoveryResult {
        Objects.requireNonNull(queryType, "QueryType must not be null");
        results = List.copyOf(results);
        suggestions = List.copyOf(suggestions);
        Objects.requireNonNull(summary, "Summary must not be null");
    }

    /**
     * Returns {@code true} if this result has no matching resources.
     *
     * @return true if empty
     */
    public boolean isEmpty() {
        return results.isEmpty();
    }

    /**
     * Returns the number of matched resources.
     *
     * @return result count
     */
    public int count() {
        return results.size();
    }

    // ─── Query Type ─────────────────────────────────────────────────

    /**
     * Classifies the type of user query.
     */
    public enum QueryType {
        /** User knows exactly what they want. */
        SPECIFIC,
        /** User knows the topic area but not the specific resource. */
        VAGUE,
        /** User wants to learn but isn't sure what. */
        EXPLORATORY
    }
}
