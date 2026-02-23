package search.engine.index;

import search.api.index.SearchIndex;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Thread-safe in-process {@link SearchIndex} backed by {@link ConcurrentHashMap}.
 *
 * <p>Designed for MCP servers where the document corpus is loaded at startup and
 * held entirely in memory. Supports concurrent reads with consistent writes.
 *
 * <h2>Recommended for</h2>
 * <ul>
 *   <li>Corpora up to ~50,000 documents (RAM permitting).</li>
 *   <li>Single-JVM processes (no cross-node consistency requirements).</li>
 *   <li>Read-heavy workloads (frequent search, infrequent add/remove).</li>
 * </ul>
 *
 * <h2>Scaling beyond InMemoryIndex</h2>
 * Implement {@link SearchIndex} and plug in Postgres full-text search
 * or Elasticsearch by overriding {@link #all()} to call an external query.
 *
 * @param <T> the document type
 *
 * @see SearchIndex
 */
public final class InMemoryIndex<T> implements SearchIndex<T> {

    private static final Logger LOGGER = Logger.getLogger(InMemoryIndex.class.getName());
    private final ConcurrentHashMap<String, T> store = new ConcurrentHashMap<>();

    /** Creates an empty index. */
    public InMemoryIndex() {}

    @Override
    public void add(final String id, final T item) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Document ID must not be null or blank");
        }
        if (item == null) {
            throw new NullPointerException("Document item must not be null");
        }
        store.put(id, item);
        LOGGER.fine(() -> "Index: added/updated document '" + id + "' (total: " + store.size() + ")");
    }

    @Override
    public void remove(final String id) {
        if (id != null && store.remove(id) != null) {
            LOGGER.fine(() -> "Index: removed document '" + id + "'");
        }
    }

    @Override
    public Collection<T> all() {
        return Collections.unmodifiableCollection(store.values());
    }

    @Override
    public Optional<T> findById(final String id) {
        return Optional.ofNullable(id != null ? store.get(id) : null);
    }

    @Override
    public int size() { return store.size(); }

    /** Logs a diagnostic summary of the index size to the INFO log. */
    public void logStats() {
        LOGGER.info(() -> "InMemoryIndex: " + store.size() + " documents.");
    }

    /** Removes all documents from the index. */
    public void clear() {
        store.clear();
        LOGGER.info("InMemoryIndex cleared.");
    }
}
