package digitalnotetaking;

/**
 * Classifies the kind of a note in the {@code brain/ai-brain/} workspace.
 *
 * <p>Matches the {@code kind} field in a note's YAML frontmatter:
 * <pre>
 * ---
 * kind: decision
 * ---
 * </pre>
 *
 * <p>Use {@link #frontmatterValue()} when writing frontmatter and
 * {@link #from(String)} when parsing it.
 *
 * @see NoteMetadata
 * @see NoteTemplate
 */
public enum NoteKind {

    /** A curated, permanent note on a concept or topic. */
    NOTE("note", "General Note", "A curated note on a concept, idea, or topic."),

    /**
     * An Architecture Decision Record — documents the context, decision,
     * and consequences of a technical choice.
     */
    DECISION("decision", "Architecture Decision Record",
            "Records why a technical decision was made (ADR format)."),

    /**
     * A session log — captures what happened during a work or learning session
     * (daily log, sprint retrospective, onboarding walkthrough, etc.).
     */
    SESSION("session", "Session Log",
            "Log of a work or learning session (daily log, sprint log)."),

    /**
     * A resource note — summarises an external source such as a book, article,
     * video, or official documentation page.
     */
    RESOURCE("resource", "Resource Summary",
            "Summary of an external source: book, article, video, or docs page."),

    /** A code snippet — a reusable pattern or idiom with context. */
    SNIPPET("snippet", "Code Snippet",
            "A reusable code pattern or idiom with explanation and usage context."),

    /**
     * A reference note — a quick-lookup item like a cheatsheet, command table,
     * or API reference index.
     */
    REF("ref", "Quick Reference",
            "Quick-lookup reference: cheatsheet, command table, API index.");

    // ─── Fields ─────────────────────────────────────────────────────

    private final String frontmatterValue;
    private final String displayName;
    private final String description;

    // ─── Constructor ─────────────────────────────────────────────────

    NoteKind(final String frontmatterValue,
             final String displayName,
             final String description) {
        this.frontmatterValue = frontmatterValue;
        this.displayName = displayName;
        this.description = description;
    }

    // ─── Accessors ───────────────────────────────────────────────────

    /**
     * Returns the lowercase frontmatter value used in YAML (e.g., {@code "decision"}).
     *
     * @return the YAML frontmatter string for this kind
     */
    public String frontmatterValue() {
        return frontmatterValue;
    }

    /**
     * Returns a human-readable display name (e.g., {@code "Architecture Decision Record"}).
     *
     * @return the display name
     */
    public String displayName() {
        return displayName;
    }

    /**
     * Returns a one-sentence description of when this kind should be used.
     *
     * @return the description
     */
    public String description() {
        return description;
    }

    // ─── Factory ─────────────────────────────────────────────────────

    /**
     * Resolves a {@code NoteKind} from a frontmatter string (case-insensitive).
     *
     * @param value the frontmatter value to look up (e.g., {@code "decision"})
     * @return the matching {@code NoteKind}
     * @throws IllegalArgumentException if the value does not match any kind;
     *         callers should default to {@link #NOTE} if the value is absent
     */
    public static NoteKind from(final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Note kind value must not be null or blank. Valid values: "
                            + validValues());
        }
        final String normalised = value.strip().toLowerCase();
        for (final NoteKind kind : values()) {
            if (kind.frontmatterValue.equals(normalised)) {
                return kind;
            }
        }
        throw new IllegalArgumentException(
                "Unknown note kind: '" + value + "'. Valid values: " + validValues());
    }

    // ─── Helpers ─────────────────────────────────────────────────────

    /** Returns a comma-separated string of all valid frontmatter values. */
    private static String validValues() {
        final var sb = new StringBuilder();
        for (final NoteKind kind : values()) {
            if (!sb.isEmpty()) {
                sb.append(", ");
            }
            sb.append(kind.frontmatterValue);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return frontmatterValue;
    }
}
