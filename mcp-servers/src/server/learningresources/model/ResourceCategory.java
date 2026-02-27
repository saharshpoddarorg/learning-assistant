package server.learningresources.model;

/**
 * Topic categories for organizing learning resources.
 *
 * <p>Categories group resources by subject area, enabling filtered
 * searches and organized vault browsing.
 */
public enum ResourceCategory {

    /** Java language, JVM, and ecosystem. */
    JAVA("java"),

    /** Python language and ecosystem. */
    PYTHON("python"),

    /** JavaScript / TypeScript and web frontend frameworks. */
    JAVASCRIPT("javascript"),

    /** General web development (HTML, CSS, HTTP, REST, GraphQL). */
    WEB("web"),

    /** SQL, NoSQL, data modeling, database engines. */
    DATABASE("database"),

    /** DevOps, CI/CD, containers, orchestration. */
    DEVOPS("devops"),

    /** Cloud platforms (AWS, Azure, GCP). */
    CLOUD("cloud"),

    /** Data structures, algorithms, complexity analysis. */
    ALGORITHMS("algorithms"),

    /** Design patterns, architecture, clean code, SOLID. */
    SOFTWARE_ENGINEERING("software-engineering"),

    /** Testing methodologies, frameworks, TDD, BDD. */
    TESTING("testing"),

    /** Security, cryptography, OWASP, authentication. */
    SECURITY("security"),

    /** AI, machine learning, LLMs, prompt engineering. */
    AI_ML("ai-ml"),

    /** Git, IDE, developer tools, productivity. */
    TOOLS("tools"),

    /** Productivity apps, note-taking tools, task managers, personal knowledge management. */
    PRODUCTIVITY("productivity"),

    /** Operating systems, networking, systems programming. */
    SYSTEMS("systems"),

    /** General or cross-cutting topics. */
    GENERAL("general");

    private final String displayName;

    ResourceCategory(final String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the human-readable display name.
     *
     * @return the display name (e.g., "java", "web", "devops")
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Resolves a {@link ResourceCategory} from a display name string.
     *
     * @param value the display name (case-insensitive)
     * @return the matching category
     * @throws IllegalArgumentException if no match is found
     */
    public static ResourceCategory fromDisplayName(final String value) {
        for (final ResourceCategory category : values()) {
            if (category.displayName.equalsIgnoreCase(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown resource category: '" + value + "'");
    }
}
