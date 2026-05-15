package server.learningresources.handler;

import server.learningresources.model.ConceptArea;
import server.learningresources.model.LearningResource;
import server.learningresources.model.ResourceCategory;
import server.learningresources.vault.DiscoveryResult;
import server.learningresources.vault.RelevanceScorer.ScoredResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
 * <p>Supports three output formats:
 * <ul>
 *   <li><strong>Markdown</strong> — full-featured, ready for GitHub/docs rendering</li>
 *   <li><strong>PDF</strong> — plain-text formatted for readability, saved as .pdf-ready text
 *       (or actual PDF if pandoc is available)</li>
 *   <li><strong>Word</strong> — plain-text formatted for readability, saved as .docx-ready text
 *       (or actual DOCX if pandoc is available)</li>
 * </ul>
 *
 * <p>PDF and Word exports first generate Markdown, then attempt conversion via
 * {@code pandoc} if available on the system PATH. If pandoc is unavailable,
 * the formatted content is returned as plain text with instructions.
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
        /** PDF document. */
        PDF("pdf"),
        /** Word document (.docx). */
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
                        "Unsupported format: '" + value + "'. Supported: md, pdf, word");
            };
        }
    }

    /**
     * Exports a {@link DiscoveryResult} in the requested format.
     *
     * <p>For PDF and Word, generates Markdown first, then attempts pandoc conversion.
     * If pandoc is unavailable, returns formatted plain text with a conversion hint.
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
            case PDF -> exportWithPandoc(result, "pdf");
            case WORD -> exportWithPandoc(result, "docx");
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
        builder.append("> **Search mode:** ").append(result.searchMode().getDisplayName()).append("  \n");
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

    // ─── PDF / Word Export via Pandoc ────────────────────────────────

    /**
     * Generates Markdown, writes to a temp file, attempts pandoc conversion.
     *
     * <p>If pandoc succeeds, returns a success message with the output file path.
     * If pandoc is unavailable, returns formatted plain text instead.
     */
    private String exportWithPandoc(final DiscoveryResult result, final String targetExtension) {
        final var markdownContent = exportAsMarkdown(result);
        final var plainText = convertMarkdownToPlainText(result);

        try {
            final var tempDir = Files.createTempDirectory("learning-export-");
            tempDir.toFile().deleteOnExit();
            final var mdFile = tempDir.resolve("discovery-results.md");
            mdFile.toFile().deleteOnExit();
            final var outputFile = tempDir.resolve("discovery-results." + targetExtension);
            outputFile.toFile().deleteOnExit();

            Files.writeString(mdFile, markdownContent);

            final var process = new ProcessBuilder(
                    "pandoc", mdFile.toString(), "-o", outputFile.toString(),
                    "--from=markdown", "--standalone"
            ).redirectErrorStream(true).start();

            final var exitCode = process.waitFor();
            final String processOutput;
            try (var processStream = process.getInputStream()) {
                processOutput = new String(processStream.readAllBytes());
            }

            if (exitCode == 0 && Files.exists(outputFile)) {
                final var fileSize = Files.size(outputFile);
                return "Exported to " + targetExtension.toUpperCase() + ": " + outputFile
                        + " (" + fileSize + " bytes)\n\n"
                        + "Markdown source also saved: " + mdFile;
            } else {
                LOGGER.warning("Pandoc conversion failed (exit=" + exitCode + "): " + processOutput);
                return buildFallbackResponse(plainText, mdFile, targetExtension);
            }

        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            LOGGER.fine("Export interrupted: " + interruptedException.getMessage());
            return buildNoPandocResponse(plainText, targetExtension);
        } catch (IOException ioException) {
            LOGGER.fine("Pandoc not available: " + ioException.getMessage());
            return buildNoPandocResponse(plainText, targetExtension);
        }
    }

    /**
     * Converts a discovery result to a clean plain-text format (no Markdown syntax).
     */
    private String convertMarkdownToPlainText(final DiscoveryResult result) {
        final var builder = new StringBuilder();
        final var separator = "=".repeat(60) + "\n";

        builder.append(separator);
        builder.append("  DISCOVERY RESULTS\n");
        builder.append(separator);
        builder.append("  Search mode : ").append(result.searchMode().getDisplayName()).append("\n");
        builder.append("  Results     : ").append(result.count()).append("\n");
        builder.append("  Generated   : ").append(DATE_FORMAT.format(Instant.now())).append("\n");
        builder.append(separator).append("\n");

        if (!result.summary().isBlank()) {
            builder.append("SUMMARY\n");
            builder.append("-".repeat(40)).append("\n");
            builder.append(result.summary()).append("\n\n");
        }

        if (!result.isEmpty()) {
            builder.append("RESULTS\n");
            builder.append("-".repeat(40)).append("\n\n");

            var rank = 1;
            for (final var entry : result.results()) {
                final var resource = entry.resource();
                builder.append(rank++).append(". ").append(resource.title());
                if (resource.isOfficial()) {
                    builder.append(" [OFFICIAL]");
                }
                builder.append("\n");
                builder.append("   Type       : ").append(resource.type().getDisplayName()).append("\n");
                builder.append("   Difficulty : ").append(resource.difficulty().getDisplayName()).append("\n");
                builder.append("   Score      : ").append(entry.score()).append("\n");
                builder.append("   URL        : ").append(resource.url()).append("\n");
                builder.append("   ").append(resource.description()).append("\n\n");
            }
        }

        if (!result.suggestions().isEmpty()) {
            builder.append("SUGGESTIONS\n");
            builder.append("-".repeat(40)).append("\n");
            for (final var suggestion : result.suggestions()) {
                builder.append("  * ").append(suggestion).append("\n");
            }
        }

        return builder.toString();
    }

    private String buildFallbackResponse(final String plainText, final Path mdFile,
                                          final String targetExtension) {
        return "Pandoc conversion to " + targetExtension.toUpperCase() + " failed.\n"
                + "Markdown source saved at: " + mdFile + "\n\n"
                + "To convert manually:\n"
                + "  pandoc " + mdFile + " -o output." + targetExtension + "\n\n"
                + "--- Plain Text Export ---\n\n" + plainText;
    }

    private String buildNoPandocResponse(final String plainText, final String targetExtension) {
        return "Pandoc is not installed — returning formatted plain text.\n\n"
                + "To enable " + targetExtension.toUpperCase() + " export:\n"
                + "  1. Install pandoc: https://pandoc.org/installing.html\n"
                + "  2. Ensure 'pandoc' is on your system PATH\n"
                + "  3. Re-run the export command\n\n"
                + "--- Plain Text Export ---\n\n" + plainText;
    }

    // ─── Table Renderers ────────────────────────────────────────────

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
        builder.append("- **Freshness:** ").append(resource.freshness().getDisplayName()).append("\n");
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
