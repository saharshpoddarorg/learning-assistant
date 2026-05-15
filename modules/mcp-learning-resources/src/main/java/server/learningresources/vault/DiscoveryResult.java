package server.learningresources.vault;

import server.learningresources.model.SearchMode;
import server.learningresources.vault.RelevanceScorer.ScoredResource;

import java.util.List;
import java.util.Objects;

/**
 * The complete result of a resource discovery query.
 *
 * <p>Bundles the classified search mode, scored results, follow-up suggestions,
 * and a human-readable summary. This is the primary return type from
 * {@link ResourceDiscovery#discover(String)}.
 *
 * @param searchMode  how the query was classified (specific, vague, exploratory)
 * @param results     scored resources sorted by relevance (descending)
 * @param suggestions "did you mean?" or follow-up suggestions
 * @param summary     human-readable summary of the search
 * @see SearchMode
 */
public record DiscoveryResult(
        SearchMode searchMode,
        List<ScoredResource> results,
        List<String> suggestions,
        String summary
) {

    /**
     * Creates a discovery result with defensive copies.
     *
     * @param searchMode  the classified search mode
     * @param results     scored and sorted results
     * @param suggestions follow-up suggestions
     * @param summary     human-readable summary
     */
    public DiscoveryResult {
        Objects.requireNonNull(searchMode, "SearchMode must not be null");
        Objects.requireNonNull(results, "Results list must not be null");
        Objects.requireNonNull(suggestions, "Suggestions list must not be null");
        Objects.requireNonNull(summary, "Summary must not be null");
        results = List.copyOf(results);
        suggestions = List.copyOf(suggestions);
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
}
