package search.engine.filter;

import search.api.core.SearchContext;
import search.api.filter.SearchFilter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * AND-chain of {@link SearchFilter}s that short-circuits on the first failing filter.
 *
 * <p>Prefer this over manually chaining {@code filter1.and(filter2).and(filter3)} when
 * you have more than 2 filters — the chain is more readable and produces better
 * {@code toString} output for debugging.
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * SearchFilter<Article> combined = FilterChain.of(
 *         notArchived,
 *         officialOnly,
 *         difficultyFilter
 * );
 * // Equivalent to notArchived.and(officialOnly).and(difficultyFilter)
 * }</pre>
 *
 * @param <T> the document type
 *
 * @see SearchFilter
 */
public final class FilterChain<T> implements SearchFilter<T> {

    private final List<SearchFilter<T>> filters;

    @SafeVarargs
    private FilterChain(final SearchFilter<T>... filters) {
        this.filters = Arrays.stream(filters).map(Objects::requireNonNull).toList();
    }

    private FilterChain(final List<SearchFilter<T>> filters) {
        this.filters = filters.stream().map(Objects::requireNonNull).toList();
    }

    /**
     * Creates an AND-chain from a varargs array. Returns {@link SearchFilter#allowAll()}
     * if the array is empty.
     *
     * @param filters the filters to chain (none may be null)
     * @param <T>     the document type
     * @return a filter chain (or allowAll if empty)
     */
    @SafeVarargs
    public static <T> SearchFilter<T> of(final SearchFilter<T>... filters) {
        if (filters == null || filters.length == 0) return SearchFilter.allowAll();
        if (filters.length == 1) return Objects.requireNonNull(filters[0]);
        return new FilterChain<>(filters);
    }

    /**
     * Creates an AND-chain from a list. Returns {@link SearchFilter#allowAll()}
     * if the list is empty.
     *
     * @param filters the filters to chain
     * @param <T>     the document type
     * @return a filter chain (or allowAll if empty)
     */
    public static <T> SearchFilter<T> ofList(final List<SearchFilter<T>> filters) {
        if (filters == null || filters.isEmpty()) return SearchFilter.allowAll();
        if (filters.size() == 1) return Objects.requireNonNull(filters.get(0));
        return new FilterChain<>(filters);
    }

    @Override
    public boolean test(final T item, final SearchContext context) {
        for (final var filter : filters) {
            if (!filter.test(item, context)) return false; // short-circuit
        }
        return true;
    }

    /** Returns the number of filters in this chain. */
    public int size() { return filters.size(); }
}
