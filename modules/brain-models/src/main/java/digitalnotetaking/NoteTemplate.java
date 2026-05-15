package digitalnotetaking;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Factory that generates Markdown note templates for the {@code brain/ai-brain/}
 * workspace.
 *
 * <p>Each template corresponds to a {@link NoteKind} and produces a ready-to-edit
 * Markdown string with a valid YAML frontmatter block and a structured body.
 *
 * <p>Usage:
 * <pre>
 * // Generate an ADR template for the mcp-servers project
 * NoteMetadata metadata = NoteMetadata.builder()
 *         .kind(NoteKind.DECISION)
 *         .project("mcp-servers")
 *         .tags(List.of("adr", "architecture", "transport"))
 *         .build();
 *
 * String markdown = NoteTemplate.generate(metadata, "ADR-001: Use SSE transport");
 * System.out.println(markdown);
 * </pre>
 *
 * <p>The output matches the templates documented in
 * {@code brain/digitalnotetaking/templates.md}.
 *
 * @see NoteKind
 * @see NoteMetadata
 * @see NoteStatus
 */
public final class NoteTemplate {

    private NoteTemplate() {
        // Utility class — no instantiation
    }

    // ─── Public API ──────────────────────────────────────────────────

    /**
     * Generates a complete Markdown note (frontmatter + body) for the given metadata
     * and title.
     *
     * @param metadata the note metadata (date, kind, project, tags, status);
     *                 must not be null
     * @param title    the note title used as the H1 heading; must not be null or blank
     * @return the full Markdown content, ready to write to a {@code .md} file
     */
    public static String generate(final NoteMetadata metadata, final String title) {
        Objects.requireNonNull(metadata, "metadata must not be null");
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title must not be null or blank");
        }

        return metadata.toFrontmatter()
                + "\n\n"
                + "# " + title.strip()
                + "\n\n"
                + bodyFor(metadata.kind());
    }

    /**
     * Generates a note with today's date, {@link NoteStatus#DRAFT} status,
     * the {@code "general"} project bucket, and the provided kind and title.
     *
     * <p>Convenience factory for quick note creation from the terminal or Copilot.
     *
     * @param kind  the note kind; must not be null
     * @param title the H1 heading; must not be null or blank
     * @param tags  additional tags (may be empty); must not be null
     * @return the full Markdown content
     */
    public static String quickGenerate(final NoteKind kind,
                                       final String title,
                                       final List<String> tags) {
        final NoteMetadata metadata = NoteMetadata.builder()
                .date(LocalDate.now())
                .kind(Objects.requireNonNull(kind, "kind"))
                .project("general")
                .tags(Objects.requireNonNull(tags, "tags"))
                .status(NoteStatus.DRAFT)
                .build();
        return generate(metadata, title);
    }

    // ─── Body Templates ──────────────────────────────────────────────

    private static String bodyFor(final NoteKind kind) {
        return switch (kind) {
            case NOTE -> noteBody();
            case DECISION -> decisionBody();
            case SESSION -> sessionBody();
            case RESOURCE -> resourceBody();
            case SNIPPET -> snippetBody();
            case REF -> refBody();
        };
    }

    private static String noteBody() {
        return """
                ## Summary

                <!-- One-paragraph summary of the concept or idea -->

                ## Key Points

                -\s
                -\s
                -\s

                ## Code Example

                ```java
                // optional — remove if not applicable
                ```

                ## Questions / Follow-Up

                -\s

                ## Related Notes

                - [[]]
                """;
    }

    private static String decisionBody() {
        return """
                **Status:** Proposed | Accepted | Deprecated | Superseded by ADR-NNN\\s
                **Date:** YYYY-MM-DD\\s
                **Deciders:**\\s

                ## Context

                <!-- What situation or problem drove this decision? -->

                ## Decision

                <!-- What is the change we are proposing / making? -->

                ## Consequences

                ### Positive

                -\s

                ### Negative / Trade-offs

                -\s

                ## Alternatives Considered

                | Alternative | Why rejected |
                |---|---|
                |  |  |

                ## References

                -\s
                """;
    }

    private static String sessionBody() {
        return """
                ## Top 3 for Today

                - [ ]\s
                - [ ]\s
                - [ ]\s

                ## What I Worked On

                -\s

                ## What I Learned

                -\s

                ## Questions / Stuck Points

                -\s

                ## Tomorrow

                -\s

                ## Links & Resources

                -\s
                """;
    }

    private static String resourceBody() {
        return """
                **Type:** Book | Article | Course | Video | Documentation\\s
                **Author:**\\s
                **URL:**\\s
                **Date read:**\\s

                ## One-Line Summary

                <!-- Single most important takeaway -->

                ## Key Ideas

                ### 1.\s

                ### 2.\s

                ### 3.\s

                ## Best Quotes

                > "..."

                ## How I Will Apply This

                -\s

                ## Related Notes

                - [[]]
                """;
    }

    private static String snippetBody() {
        return """
                **Language:**\\s
                **Source:**\\s

                ## The Pattern

                ```java
                // paste code here
                ```

                ## When to Use

                -\s

                ## When NOT to Use

                -\s

                ## Key Points

                -\s

                ## See Also

                - [[]]
                """;
    }

    private static String refBody() {
        return """
                ## Quick Reference

                | Command / API | What it does |
                |---|---|
                |  |  |

                ## Notes

                -\s

                ## Source

                -\s
                """;
    }
}
