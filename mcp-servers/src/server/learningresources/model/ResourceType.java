package server.learningresources.model;

/**
 * Classification of learning resource formats.
 *
 * <p>Each type represents a distinct content format that affects how the
 * resource is scraped, parsed, and presented to the user.
 */
public enum ResourceType {

    /** Official language/framework documentation (e.g., docs.oracle.com, MDN). */
    DOCUMENTATION("documentation"),

    /** Step-by-step guided learning content with exercises. */
    TUTORIAL("tutorial"),

    /** Opinion or experience-based articles from community members. */
    BLOG("blog"),

    /** In-depth technical articles (e.g., Baeldung, InfoQ). */
    ARTICLE("article"),

    /** Video content — standalone talks, screencasts, or short clips (e.g., YouTube). */
    VIDEO("video"),

    /** Full video lecture course with a structured syllabus (e.g., MIT OCW, fast.ai). */
    VIDEO_COURSE("video-course"),

    /** Published books or book chapters available online. */
    BOOK("book"),

    /** Interactive coding exercises or sandboxes. */
    INTERACTIVE("interactive"),

    /** Structured courses with lessons, quizzes, and progression. */
    COURSE("course"),

    /** API reference documentation with method signatures. */
    API_REFERENCE("api-reference"),

    /** Cheat sheets, quick reference cards, one-pagers. */
    CHEAT_SHEET("cheat-sheet"),

    /** GitHub repositories, open-source projects to study. */
    REPOSITORY("repository");

    private final String displayName;

    ResourceType(final String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the human-readable display name.
     *
     * @return the display name (e.g., "documentation", "tutorial")
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Resolves a {@link ResourceType} from a display name string.
     *
     * @param value the display name (case-insensitive)
     * @return the matching type
     * @throws IllegalArgumentException if no match is found
     */
    public static ResourceType fromDisplayName(final String value) {
        for (final ResourceType type : values()) {
            if (type.displayName.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown resource type: '" + value
                + "'. Valid values: documentation, tutorial, blog, article, video, "
                + "video-course, book, interactive, course, api-reference, "
                + "cheat-sheet, repository");
    }
}
