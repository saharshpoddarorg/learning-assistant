package digitalnotetaking;

/**
 * Lifecycle status of a note in the {@code brain/ai-brain/} workspace.
 *
 * <p>Maps to the {@code status} field in YAML frontmatter:
 * <pre>
 * ---
 * status: draft
 * ---
 * </pre>
 *
 * <p>Typical lifecycle:
 * <pre>
 * inbox (draft) → notes (draft or final) → archive (final or archived)
 * </pre>
 *
 * @see NoteMetadata
 * @see NoteKind
 */
public enum NoteStatus {

    /** Work in progress — not yet ready for publication. */
    DRAFT("draft"),

    /** Complete and ready, or published to archive. */
    FINAL("final"),

    /** Committed to the {@code archive/} tier — permanently stored in the repo. */
    ARCHIVED("archived");

    // ─── Fields ─────────────────────────────────────────────────────

    private final String frontmatterValue;

    // ─── Constructor ─────────────────────────────────────────────────

    NoteStatus(final String frontmatterValue) {
        this.frontmatterValue = frontmatterValue;
    }

    // ─── Accessors ───────────────────────────────────────────────────

    /**
     * Returns the lowercase frontmatter value (e.g., {@code "draft"}).
     *
     * @return the YAML frontmatter string for this status
     */
    public String frontmatterValue() {
        return frontmatterValue;
    }

    // ─── Factory ─────────────────────────────────────────────────────

    /**
     * Resolves a {@code NoteStatus} from its frontmatter string (case-insensitive).
     *
     * @param value the frontmatter value to look up
     * @return the matching {@code NoteStatus}
     * @throws IllegalArgumentException if the value does not match any status
     */
    public static NoteStatus from(final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Note status value must not be null or blank.");
        }
        final String normalised = value.strip().toLowerCase();
        for (final NoteStatus status : values()) {
            if (status.frontmatterValue.equals(normalised)) {
                return status;
            }
        }
        throw new IllegalArgumentException(
                "Unknown note status: '" + value + "'. Expected: draft, final, archived.");
    }

    @Override
    public String toString() {
        return frontmatterValue;
    }
}
