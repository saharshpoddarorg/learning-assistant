package server.learningresources.content;

import server.learningresources.model.ContentSummary;
import server.learningresources.scraper.ContentExtractor;
import server.learningresources.scraper.ScraperResult;

import java.time.Instant;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Produces a {@link ContentSummary} from a raw {@link ScraperResult}.
 *
 * <p>Orchestrates the extraction and summarization pipeline:
 * <ol>
 *   <li>Extract clean text via {@link ContentExtractor}</li>
 *   <li>Extract page title</li>
 *   <li>Count words and estimate reading time</li>
 *   <li>Generate a condensed summary (extractive — first N sentences)</li>
 *   <li>Estimate difficulty via {@link ReadabilityScorer}</li>
 * </ol>
 */
public class ContentSummarizer {

    private static final Logger LOGGER = Logger.getLogger(ContentSummarizer.class.getName());

    private static final int DEFAULT_SUMMARY_SENTENCES = 5;
    private static final int MAX_SUMMARY_LENGTH = 1500;

    private final ContentExtractor extractor;
    private final ReadabilityScorer scorer;
    private final int summarySentences;

    /**
     * Creates a {@link ContentSummarizer} with default settings.
     */
    public ContentSummarizer() {
        this(new ContentExtractor(), new ReadabilityScorer(), DEFAULT_SUMMARY_SENTENCES);
    }

    /**
     * Creates a {@link ContentSummarizer} with custom dependencies.
     *
     * @param extractor        the text extractor
     * @param scorer           the readability scorer
     * @param summarySentences number of sentences for the summary
     */
    public ContentSummarizer(final ContentExtractor extractor,
                             final ReadabilityScorer scorer,
                             final int summarySentences) {
        this.extractor = Objects.requireNonNull(extractor, "ContentExtractor must not be null");
        this.scorer = Objects.requireNonNull(scorer, "ReadabilityScorer must not be null");
        this.summarySentences = summarySentences;
    }

    /**
     * Summarizes the content from a scraper result.
     *
     * @param result the raw HTML scraper result
     * @return a {@link ContentSummary} with extracted text and summary
     */
    public ContentSummary summarize(final ScraperResult result) {
        Objects.requireNonNull(result, "ScraperResult must not be null");

        final var pageTitle = extractor.extractTitle(result);
        final var extractedText = extractor.extractText(result);
        final var wordCount = extractor.countWords(extractedText);
        final var readingTime = ContentSummary.estimateReadingTime(wordCount);
        final var summary = generateSummary(extractedText);
        final var difficulty = scorer.estimateDifficulty(extractedText);

        LOGGER.fine("Summarized " + result.url() + ": " + wordCount + " words, "
                + readingTime + " min read, difficulty=" + difficulty);

        return new ContentSummary(result.url(), pageTitle, extractedText,
                summary, wordCount, readingTime, difficulty, Instant.now());
    }

    /**
     * Generates an extractive summary by taking the first N sentences.
     *
     * <p>This is a simple extractive approach — takes the opening sentences
     * which typically contain the most important information (inverted pyramid).
     * For abstractive summarization, integrate an LLM via the OpenAI API key.
     *
     * @param text the full text to summarize
     * @return a condensed summary
     */
    private String generateSummary(final String text) {
        if (text.isBlank()) {
            return "(No content extracted)";
        }

        final var sentences = text.split("(?<=[.!?])\\s+");
        final var summaryBuilder = new StringBuilder();
        final var sentenceLimit = Math.min(summarySentences, sentences.length);

        for (var index = 0; index < sentenceLimit; index++) {
            if (summaryBuilder.length() + sentences[index].length() > MAX_SUMMARY_LENGTH) {
                break;
            }
            if (!summaryBuilder.isEmpty()) {
                summaryBuilder.append(" ");
            }
            summaryBuilder.append(sentences[index].trim());
        }

        if (summaryBuilder.isEmpty() && sentences.length > 0) {
            // If first sentence is too long, truncate it
            final var truncated = sentences[0].substring(0, Math.min(MAX_SUMMARY_LENGTH, sentences[0].length()));
            summaryBuilder.append(truncated);
            if (truncated.length() < sentences[0].length()) {
                summaryBuilder.append("...");
            }
        }

        return summaryBuilder.toString();
    }
}
