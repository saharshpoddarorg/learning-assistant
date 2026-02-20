package server.learningresources.handler;

import server.learningresources.model.ConceptArea;
import server.learningresources.model.LearningResource;
import server.learningresources.model.ResourceCategory;
import server.learningresources.vault.DiscoveryResult;
import server.learningresources.vault.RelevanceScorer.ScoredResource;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Formats discovery and search results into exportable document formats.
 *
 * <p>Currently supports:
 * <ul>
 *   <li><strong>Markdown</strong> — full-featured, ready for GitHub/docs rendering</li>
 * </ul>
 *
 * <p>Future enhancements will add PDF and Word output via external libraries.
 *
 * @see DiscoveryResult
 */
public final class ExportHandler {

    private static final Logger LOGGER = Logger.getLogger(ExportHandler.class.getName());
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());

    /**
     * Supported export formats.
     */
    public enum OutputFormat {
        /** GitHub-flavored Markdown. */
        MARKDOWN("md"),
        /** PDF document (planned — not yet implemented). */
        PDF("pdf"),
        /** Word document (planned — not yet implemented). */
        WORD("docx");

        private final String extension;

        OutputFormat(final String extension) {
            this.extension = extension;
        }

        /**
         * Returns the file extension for this format.
         *
         * @return file extension without dot (e.g., "md", "pdf")
         */
        public String getExtension() {
            return extension;
        }

        /**
         * Resolves an {@link OutputFormat} from a string (case-insensitive).
         *
         * @param value the format name or extension
         * @return the matching format
         * @throws IllegalArgumentException if unsupported
         */
        public static OutputFormat fromString(final String value) {
            if (value == null || value.isBlank()) {
                return MARKDOWN;
            }
            final var normalized = value.strip().toLowerCase();
            return switch (normalized) {
                case "md", "markdown" -> MARKDOWN;
                case "pdf" -> PDF;
                case "docx", "word", "doc" -> WORD;
                default -> throw new IllegalArgumentException(
                        "Unsupported format: '" + value + "'. Supported: md, pdf (planned), word (planned)");
            };
        }
    }

    /**
     * Exports a {@link DiscoveryResult} in the requested format.
     *
     * @param result the discovery result to export
     * @param format the desired output format
     * @return the formatted content as a string
     */
    public String export(final DiscoveryResult result, final OutputFormat format) {
        Objects.requireNonNull(result, "DiscoveryResult must not be null");
        Objects.requireNonNull(format, "OutputFormat must not be null");

        return switch (format) {
            case MARKDOWN -> exportAsMarkdown(result);
            case PDF -> "PDF export is planned for a future release. " +
                    "Use 'md' format and convert with pandoc: `pandoc output.md -o output.pdf`";
            case WORD -> "Word export is planned for a future release. " +
                    "Use 'md' format and convert with pandoc: `pandoc output.md -o output.docx`";
        };
    }

    /**
     * Exports a flat list of resources as Markdown.
     *
     * @param resources the resources to export
     * @param title     the document title
     * @return Markdown string
     */
    public String exportResourceList(final List<LearningResource> resources, final String title) {
        Objects.requireNonNull(resources, "Resource list must not be null");

        final var builder = new StringBuilder();
        builder.append("# ").append(title != null ? title : "Learning Resources").append("\n\n");
        builder.append("> Generated on ").append(DATE_FORMAT.format(Instant.now())).append("\n\n");

        appendResourceTable(builder, resources);
        appendDetailSections(builder, resources);

        return builder.toString();
    }

    // ─── Markdown Export ────────────────────────────────────────────

    private String exportAsMarkdown(final DiscoveryResult result) {
        final var builder = new StringBuilder();

        builder.append("# Discovery Results\n\n");
        builder.append("> **Query type:** ").append(result.queryType()).append("  \n");
        builder.append("> **Results:** ").append(result.count()).append("  \n");
        builder.append("> **Generated:** ").append(DATE_FORMAT.format(Instant.now())).append("\n\n");

        if (!result.summary().isBlank()) {
            builder.append("## Summary\n\n").append(result.summary()).append("\n\n");
        }

        if (!result.isEmpty()) {
            builder.append("## Results\n\n");
            appendScoredTable(builder, result.results());
            builder.append("\n---\n\n");
            appendScoredDetails(builder, result.results());
        }

        if (!result.suggestions().isEmpty()) {
            builder.append("## Suggestions\n\n");
            for (final var suggestion : result.suggestions()) {
                builder.append("- ").append(suggestion).append("\n");
            }
        }

        return builder.toString();
    }

    private void appendScoredTable(final StringBuilder builder, final List<ScoredResource> scored) {
        builder.append("| # | Resource | Type | Difficulty | Score | Official |\n");
        builder.append("|---|----------|------|------------|-------|----------|\n");

        var rank = 1;
        for (final var entry : scored) {
            final var resource = entry.resource();
            builder.append("| ").append(rank++).append(" | ")
                    .append("[").append(resource.title()).append("](").append(resource.url()).append(") | ")
                    .append(resource.type().getDisplayName()).append(" | ")
                    .append(resource.difficulty().getDisplayName()).append(" | ")
                    .append(entry.score()).append(" | ")
                    .append(resource.isOfficial() ? "Yes" : "").append(" |\n");
        }
    }

    private void appendScoredDetails(final StringBuilder builder, final List<ScoredResource> scored) {
        builder.append("## Resource Details\n\n");
        for (final var entry : scored) {
            appendResourceDetail(builder, entry.resource());
        }
    }

    private void appendResourceTable(final StringBuilder builder, final List<LearningResource> resources) {
        builder.append("| Resource | Type | Difficulty | Categories | Official | Free |\n");
        builder.append("|----------|------|------------|------------|----------|------|\n");

        for (final var resource : resources) {
            builder.append("| [").append(resource.title()).append("](").append(resource.url()).append(") | ")
                    .append(resource.type().getDisplayName()).append(" | ")
                    .append(resource.difficulty().getDisplayName()).append(" | ")
                    .append(resource.categories().stream()
                            .map(ResourceCategory::getDisplayName)
                            .collect(Collectors.joining(", "))).append(" | ")
                    .append(resource.isOfficial() ? "Yes" : "").append(" | ")
                    .append(resource.isFree() ? "Yes" : "").append(" |\n");
        }
        builder.append("\n");
    }

    private void appendDetailSections(final StringBuilder builder, final List<LearningResource> resources) {
        builder.append("---\n\n## Details\n\n");
        for (final var resource : resources) {
            appendResourceDetail(builder, resource);
        }
    }

    private void appendResourceDetail(final StringBuilder builder, final LearningResource resource) {
        builder.append("### ").append(resource.title());
        if (resource.isOfficial()) {
            builder.append(" ✅");
        }
        builder.append("\n\n");

        builder.append("- **URL:** ").append(resource.url()).append("\n");
        builder.append("- **Type:** ").append(resource.type().getDisplayName()).append("\n");
        builder.append("- **Difficulty:** ").append(resource.difficulty().getDisplayName()).append("\n");
        builder.append("- **Freshness:** ").append(resource.freshness()).append("\n");
        builder.append("- **Language Scope:** ").append(resource.languageApplicability().getDisplayName()).append("\n");

        if (!resource.author().isEmpty()) {
            builder.append("- **Author:** ").append(resource.author()).append("\n");
        }

        builder.append("- **Free:** ").append(resource.isFree() ? "Yes" : "No").append("\n");

        if (!resource.categories().isEmpty()) {
            builder.append("- **Categories:** ").append(resource.categories().stream()
                    .map(ResourceCategory::getDisplayName)
                    .collect(Collectors.joining(", "))).append("\n");
        }

        if (!resource.conceptAreas().isEmpty()) {
            builder.append("- **Concepts:** ").append(resource.conceptAreas().stream()
                    .map(ConceptArea::getDisplayName)
                    .collect(Collectors.joining(", "))).append("\n");
        }

        if (!resource.tags().isEmpty()) {
            builder.append("- **Tags:** ").append(String.join(", ", resource.tags())).append("\n");
        }

        builder.append("\n").append(resource.description()).append("\n\n");
    }
}
