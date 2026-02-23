package search.engine.algorithm;

import search.api.algorithm.ScoringStrategy;
import search.api.algorithm.Tokenizer;
import search.api.core.SearchContext;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Implements Okapi BM25 — the industry-standard probabilistic ranking function
 * used by Elasticsearch, Lucene, Solr, and most modern search engines.
 *
 * <h2>BM25 formula</h2>
 * <pre>
 *   score(D, Q) = Σ_t∈Q  IDF(t) × TF(t, D) × (k₁ + 1) /
 *                         (TF(t, D) + k₁ × (1 - b + b × |D| / avgdl))
 *
 *   IDF(t) = log( (N - df(t) + 0.5) / (df(t) + 0.5) + 1 )
 * </pre>
 *
 * <h2>Parameters</h2>
 * <ul>
 *   <li>{@code k₁ = 1.5} — term-frequency saturation (higher → term frequency matters more)</li>
 *   <li>{@code b = 0.75} — length normalisation (0 = no normalisation, 1 = full)</li>
 * </ul>
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * var bm25 = Bm25Scorer.<Article>builder()
 *         .textExtractor(a -> a.title() + " " + a.body())
 *         .tokenizer(new DefaultTokenizer())
 *         .build();
 *
 * // REQUIRED: compute IDF stats from corpus before first use
 * bm25.computeStats(index.all());
 *
 * int score = bm25.score(article, context);
 * }</pre>
 *
 * @param <T> the document type
 *
 * @see DefaultTokenizer
 * @see CompositeScorer
 */
public final class Bm25Scorer<T> implements ScoringStrategy<T> {

    /** BM25 term-saturation factor. */
    private static final double DEFAULT_K1 = 1.5;

    /** BM25 length-normalisation factor. */
    private static final double DEFAULT_B = 0.75;

    /** Scale factor to convert BM25 float score to int points. */
    private static final int SCALE_FACTOR = 10;

    private final Function<T, String> textExtractor;
    private final Tokenizer tokenizer;
    private final double k1;
    private final double b;

    // ─── Corpus statistics (computed lazily or explicitly) ─────────
    private volatile Map<String, Integer> documentFrequencies = Map.of();
    private volatile int totalDocuments;
    private volatile double avgDocLength;
    private volatile boolean statsComputed;

    private Bm25Scorer(final Builder<T> builder) {
        this.textExtractor = builder.textExtractor;
        this.tokenizer     = builder.tokenizer;
        this.k1            = builder.k1;
        this.b             = builder.b;
    }

    /**
     * Computes corpus statistics from a collection of documents.
     *
     * <p><strong>Call this method once after populating the index</strong>, before
     * performing any searches. BM25 requires per-term document frequencies and average
     * document length across the entire corpus.
     *
     * @param corpus all documents in the index
     */
    public void computeStats(final Collection<T> corpus) {
        Objects.requireNonNull(corpus, "corpus must not be null");
        final var dfMap      = new HashMap<String, Integer>();
        var docLengthTotal   = 0L;
        var totalDocs        = 0;

        for (final var item : corpus) {
            final var text   = textExtractor.apply(item);
            final var tokens = tokenizer.tokenize(text);
            docLengthTotal  += tokens.size();
            totalDocs++;
            // Each unique term in the document contributes 1 to df
            for (final var token : tokens) {
                dfMap.merge(token, 1, Integer::sum);
            }
        }

        this.documentFrequencies = Map.copyOf(dfMap);
        this.totalDocuments      = totalDocs;
        this.avgDocLength        = totalDocs > 0 ? (double) docLengthTotal / totalDocs : 1.0;
        this.statsComputed       = true;
    }

    @Override
    public int score(final T item, final SearchContext context) {
        Objects.requireNonNull(item,    "item must not be null");
        Objects.requireNonNull(context, "context must not be null");

        final var queryTokens = tokenizer.tokenize(context.normalizedInput());
        if (queryTokens.isEmpty()) return 0;

        final var docText   = textExtractor.apply(item).toLowerCase();
        final var docTokens = tokenizer.tokenize(docText);
        final var docLength = docTokens.size();
        if (docLength == 0) return 0;

        final double effectiveAvgdl = avgDocLength > 0 ? avgDocLength : 1.0;
        var totalScore = 0.0;

        for (final var term : queryTokens) {
            final var tf = countTermFrequency(term, docText);
            if (tf == 0) continue;

            final double idf = computeIdf(term);
            final double tfNorm = (tf * (k1 + 1))
                    / (tf + k1 * (1 - b + b * docLength / effectiveAvgdl));

            totalScore += idf * tfNorm;
        }

        return (int) (totalScore * SCALE_FACTOR);
    }

    // ─── Internal helpers ──────────────────────────────────────────

    private double computeIdf(final String term) {
        final int df = documentFrequencies.getOrDefault(term, 0);
        final int n  = Math.max(totalDocuments, 1);
        // Robertson-Spärck Jones IDF variant (avoids negatives)
        return Math.log((n - df + 0.5) / (df + 0.5) + 1.0);
    }

    private int countTermFrequency(final String term, final String text) {
        var count = 0;
        var start = 0;
        while ((start = text.indexOf(term, start)) != -1) {
            count++;
            start += term.length();
        }
        return count;
    }

    /** Returns {@code true} if {@link #computeStats(Collection)} has been called. */
    public boolean isStatsComputed() { return statsComputed; }

    /** Returns the number of documents used to compute stats, or 0 if not computed. */
    public int totalDocuments() { return totalDocuments; }

    public static <T> Builder<T> builder() { return new Builder<>(); }

    /** Fluent builder for {@link Bm25Scorer}. */
    public static final class Builder<T> {

        private Function<T, String> textExtractor = item -> "";
        private Tokenizer tokenizer = new DefaultTokenizer();
        private double k1 = DEFAULT_K1;
        private double b  = DEFAULT_B;

        private Builder() {}

        /**
         * Extracts the text to score from a document.
         * Multiple fields can be concatenated: {@code a -> a.title() + " " + a.body()}.
         */
        public Builder<T> textExtractor(final Function<T, String> extractor) {
            this.textExtractor = Objects.requireNonNull(extractor); return this;
        }

        /** Sets the tokenizer for both query and document text. */
        public Builder<T> tokenizer(final Tokenizer tokenizer) {
            this.tokenizer = Objects.requireNonNull(tokenizer); return this;
        }

        /** Sets the term-frequency saturation factor (default 1.5). */
        public Builder<T> k1(final double k1) {
            if (k1 < 0) throw new IllegalArgumentException("k1 must be ≥ 0");
            this.k1 = k1; return this;
        }

        /** Sets the length-normalisation factor (default 0.75). 0 = no normalisation. */
        public Builder<T> b(final double b) {
            if (b < 0 || b > 1) throw new IllegalArgumentException("b must be in [0, 1]");
            this.b = b; return this;
        }

        public Bm25Scorer<T> build() { return new Bm25Scorer<>(this); }
    }
}
