package search.api.index;

import java.util.Collection;
import java.util.Optional;

/**
 * Minimal document storage abstraction.
 *
 * <p>The {@code SearchIndex} stores the corpus — the set of documents an engine can search.
 * The engine itself handles scoring, filtering, and ranking; the index is responsible only
 * for storing documents and providing access to all of them.
 *
 * <h2>Implementations</h2>
 * <ul>
 *   <li>{@link search.engine.index.InMemoryIndex} — thread-safe in-process store (default)</li>
 *   <li>Custom: wrap a database query, Elasticsearch client, or SQLite store</li>
 * </ul>
 *
 * <h2>Live index (custom implementation)</h2>
 * <pre>{@code
 * public class ConfluenceIndex implements SearchIndex<ConfluencePage> {
 *     @Override
 *     public Collection<ConfluencePage> all() {
 *         return confluenceClient.fetchAllCachedPages(); // periodic refresh
 *     }
 *     // ... other methods
 * }
 * }</pre>
 *
 * @param <T> the document type
 *
 * @see search.engine.index.InMemoryIndex
 */
public interface SearchIndex<T> {

    /**
     * Adds or replaces a document in the index.
     *
     * @param id   the unique document identifier (never null or blank)
     * @param item the document (never null)
     */
    void add(String id, T item);

    /**
     * Removes a document from the index, if present.
     *
     * @param id the unique document identifier
     */
    void remove(String id);

    /**
     * Returns all documents currently in the index.
     * The returned collection should be unmodifiable.
     *
     * @return all documents (never null; may be empty)
     */
    Collection<T> all();

    /**
     * Looks up a document by its unique identifier.
     *
     * @param id the document identifier
     * @return an {@code Optional} containing the document, or empty if not found
     */
    Optional<T> findById(String id);

    /** Returns the number of documents currently in the index. */
    int size();

    /** Returns {@code true} if the index contains no documents. */
    default boolean isEmpty() { return size() == 0; }
}
