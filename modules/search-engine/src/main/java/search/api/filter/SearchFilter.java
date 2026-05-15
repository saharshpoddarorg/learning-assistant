package search.api.filter;

import search.api.core.SearchContext;

/**
 * Predicate that decides whether a document should be included in the candidate set.
 *
 * <p>Filters run in Phase 2 of the engine pipeline — <em>before</em> scoring — so they
 * are an efficient way to prune expensive scoring work. A filter should be fast and
 * side-effect-free.
 *
 * <h2>Composing filters</h2>
 * <pre>{@code
 * SearchFilter<Article> combined =
 *         officialOnly.and(notArchived).and(difficultyFilter);
 * }</pre>
 *
 * <h2>Context-aware filters</h2>
 * Filters receive the full {@link SearchContext} — they can read forced mode, filters map,
 * and input to make context-sensitive decisions:
 * <pre>{@code
 * SearchFilter<Article> spaceFilter = (article, ctx) -> {
 *     var allowedSpace = ctx.getFilter("space", String.class);
 *     return allowedSpace == null || article.space().equals(allowedSpace);
 * };
 * }</pre>
 *
 * @param <T> the document type
 *
 * @see search.engine.filter.FilterChain
 */
@FunctionalInterface
public interface SearchFilter<T> {

    /**
     * Tests whether the given item should be included in the search candidates.
     *
     * @param item    the document to test (never null)
     * @param context the search context (never null)
     * @return {@code true} to include; {@code false} to exclude
     */
    boolean test(T item, SearchContext context);

    // ─── Composition methods ───────────────────────────────────────

    /** Returns a filter that passes only when both {@code this} AND {@code other} pass. */
    default SearchFilter<T> and(final SearchFilter<T> other) {
        return (item, ctx) -> this.test(item, ctx) && other.test(item, ctx);
    }

    /** Returns a filter that passes when {@code this} OR {@code other} passes. */
    default SearchFilter<T> or(final SearchFilter<T> other) {
        return (item, ctx) -> this.test(item, ctx) || other.test(item, ctx);
    }

    /** Returns a filter that passes when {@code this} does NOT pass. */
    default SearchFilter<T> negate() {
        return (item, ctx) -> !this.test(item, ctx);
    }

    // ─── Factory filters ───────────────────────────────────────────

    /** A filter that always passes (includes everything). */
    static <T> SearchFilter<T> allowAll() { return (item, ctx) -> true; }

    /** A filter that always fails (excludes everything). */
    static <T> SearchFilter<T> rejectAll() { return (item, ctx) -> false; }
}
