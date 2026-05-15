package digitalnotetaking;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Immutable value object representing the YAML frontmatter of a note in the
 * {@code brain/ai-brain/} workspace.
 *
 * <p>Every note in the workspace uses this frontmatter schema:
 * <pre>
 * ---
 * date: YYYY-MM-DD
 * kind: note | decision | session | resource | snippet | ref
 * project: mcp-servers | java | general | &lt;your-project&gt;
 * tags: [tag1, tag2, tag3]
 * status: draft | final | archived
 * ---
 * </pre>
 *
 * <p>Use {@link Builder} to construct instances:
 * <pre>
 * NoteMetadata metadata = NoteMetadata.builder()
 *         .date(LocalDate.now())
 *         .kind(NoteKind.DECISION)
 *         .project("mcp-servers")
 *         .tags(List.of("adr", "architecture", "transport"))
 *         .status(NoteStatus.DRAFT)
 *         .build();
 * </pre>
 *
 * <p>Use {@link #toFrontmatter()} to generate the YAML block for a new note file.
 *
 * @see NoteKind
 * @see NoteStatus
 * @see NoteTemplate
 */
public final class NoteMetadata {

    // ─── Fields ─────────────────────────────────────────────────────

    private final LocalDate date;
    private final NoteKind kind;
    private final String project;
    private final List<String> tags;
    private final NoteStatus status;

    // ─── Constructor ─────────────────────────────────────────────────

    private NoteMetadata(final Builder builder) {
        this.date = Objects.requireNonNull(builder.date, "date must not be null");
        this.kind = Objects.requireNonNull(builder.kind, "kind must not be null");
        this.project = Objects.requireNonNull(builder.project, "project must not be null");
        this.tags = List.copyOf(
                Objects.requireNonNull(builder.tags, "tags must not be null"));
        this.status = Objects.requireNonNull(builder.status, "status must not be null");
    }

    // ─── Accessors ───────────────────────────────────────────────────

    /**
     * Returns the note's creation date.
     *
     * @return the date
     */
    public LocalDate date() {
        return date;
    }

    /**
     * Returns the note's kind (note, decision, session, resource, snippet, ref).
     *
     * @return the kind
     */
    public NoteKind kind() {
        return kind;
    }

    /**
     * Returns the project bucket this note belongs to (e.g., {@code "mcp-servers"}).
     *
     * @return the project name
     */
    public String project() {
        return project;
    }

    /**
     * Returns an unmodifiable list of tags associated with this note.
     *
     * @return the tags
     */
    public List<String> tags() {
        return tags;
    }

    /**
     * Returns the note's status (draft, final, or archived).
     *
     * @return the status
     */
    public NoteStatus status() {
        return status;
    }

    // ─── Rendering ───────────────────────────────────────────────────

    /**
     * Renders this metadata as a YAML frontmatter block, suitable for prepending
     * to a Markdown note file.
     *
     * <pre>
     * ---
     * date: 2026-02-27
     * kind: decision
     * project: mcp-servers
     * tags: [adr, architecture, transport]
     * status: draft
     * ---
     * </pre>
     *
     * @return the YAML frontmatter string including the {@code ---} delimiters
     */
    public String toFrontmatter() {
        final var sb = new StringBuilder();
        sb.append("---\n");
        sb.append("date: ").append(date).append("\n");
        sb.append("kind: ").append(kind.frontmatterValue()).append("\n");
        sb.append("project: ").append(project).append("\n");
        sb.append("tags: [");
        for (int i = 0; i < tags.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(tags.get(i));
        }
        sb.append("]\n");
        sb.append("status: ").append(status.frontmatterValue()).append("\n");
        sb.append("---");
        return sb.toString();
    }

    // ─── Object ──────────────────────────────────────────────────────

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof NoteMetadata other)) {
            return false;
        }
        return date.equals(other.date)
                && kind == other.kind
                && project.equals(other.project)
                && tags.equals(other.tags)
                && status == other.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, kind, project, tags, status);
    }

    @Override
    public String toString() {
        return "NoteMetadata{date=" + date
                + ", kind=" + kind
                + ", project='" + project + "'"
                + ", tags=" + tags
                + ", status=" + status + "}";
    }

    // ─── Builder ─────────────────────────────────────────────────────

    /**
     * Returns a new builder for {@code NoteMetadata}.
     *
     * @return a fresh builder with today's date and sensible defaults
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Fluent builder for {@link NoteMetadata}.
     */
    public static final class Builder {

        private LocalDate date = LocalDate.now();
        private NoteKind kind = NoteKind.NOTE;
        private String project = "general";
        private List<String> tags = List.of();
        private NoteStatus status = NoteStatus.DRAFT;

        private Builder() {
        }

        /**
         * Sets the note's creation date (defaults to {@link LocalDate#now()}).
         *
         * @param date the date; must not be null
         * @return this builder
         */
        public Builder date(final LocalDate date) {
            this.date = Objects.requireNonNull(date, "date");
            return this;
        }

        /**
         * Sets the note's kind (defaults to {@link NoteKind#NOTE}).
         *
         * @param kind the kind; must not be null
         * @return this builder
         */
        public Builder kind(final NoteKind kind) {
            this.kind = Objects.requireNonNull(kind, "kind");
            return this;
        }

        /**
         * Sets the project bucket (defaults to {@code "general"}).
         *
         * @param project the project name; must not be null or blank
         * @return this builder
         */
        public Builder project(final String project) {
            if (project == null || project.isBlank()) {
                throw new IllegalArgumentException("project must not be null or blank");
            }
            this.project = project;
            return this;
        }

        /**
         * Sets the tags (defaults to an empty list).
         *
         * @param tags the tags; must not be null
         * @return this builder
         */
        public Builder tags(final List<String> tags) {
            this.tags = Objects.requireNonNull(tags, "tags");
            return this;
        }

        /**
         * Sets the note's status (defaults to {@link NoteStatus#DRAFT}).
         *
         * @param status the status; must not be null
         * @return this builder
         */
        public Builder status(final NoteStatus status) {
            this.status = Objects.requireNonNull(status, "status");
            return this;
        }

        /**
         * Constructs the {@link NoteMetadata}.
         *
         * @return a new immutable {@code NoteMetadata}
         */
        public NoteMetadata build() {
            return new NoteMetadata(this);
        }
    }
}
