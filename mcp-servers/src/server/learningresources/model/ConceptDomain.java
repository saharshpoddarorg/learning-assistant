package server.learningresources.model;

/**
 * High-level knowledge domains that group related {@link ConceptArea} values.
 *
 * <p>While {@code ConceptArea} provides fine-grained classification (45 values),
 * {@code ConceptDomain} arranges them into 9 broad domains — enabling domain-level
 * browsing, filtering, and curriculum design.
 *
 * <p>{@code ConceptArea} supports a sub-hierarchy via {@link ConceptArea#getParentArea()}.
 * For example, {@code GARBAGE_COLLECTION}, {@code CLASS_LOADING}, {@code SERIALIZATION},
 * and {@code JVM_LANGUAGES} are children of {@code JVM_INTERNALS}.
 *
 * <h2>Domain → Concept Mapping</h2>
 * <table>
 *   <tr><th>Domain</th><th>Concepts</th></tr>
 *   <tr><td>PROGRAMMING_FUNDAMENTALS</td><td>LANGUAGE_BASICS, OOP, FUNCTIONAL_PROGRAMMING, LANGUAGE_FEATURES, GENERICS</td></tr>
 *   <tr><td>CORE_CS</td><td>CONCURRENCY, DATA_STRUCTURES, ALGORITHMS, MATHEMATICS, COMPLEXITY_ANALYSIS, MEMORY_MANAGEMENT, JVM_INTERNALS (→ GARBAGE_COLLECTION, CLASS_LOADING, SERIALIZATION, JVM_LANGUAGES)</td></tr>
 *   <tr><td>SOFTWARE_ENGINEERING</td><td>DESIGN_PATTERNS, CLEAN_CODE, TESTING, API_DESIGN, ARCHITECTURE</td></tr>
 *   <tr><td>SYSTEM_DESIGN</td><td>SYSTEM_DESIGN, DATABASES, DISTRIBUTED_SYSTEMS, NETWORKING, OPERATING_SYSTEMS</td></tr>
 *   <tr><td>DEVOPS_TOOLING</td><td>CI_CD, CONTAINERS, VERSION_CONTROL, BUILD_TOOLS, INFRASTRUCTURE, OBSERVABILITY</td></tr>
 *   <tr><td>SECURITY</td><td>WEB_SECURITY, CRYPTOGRAPHY</td></tr>
 *   <tr><td>AI_DATA</td><td>MACHINE_LEARNING, DEEP_LEARNING, LLM_AND_PROMPTING</td></tr>
 *   <tr><td>CAREER_META</td><td>INTERVIEW_PREP, CAREER_DEVELOPMENT, GETTING_STARTED, KNOWLEDGE_MANAGEMENT</td></tr>
 *   <tr><td>PERSONAL_DEVELOPMENT</td><td>SELF_IMPROVEMENT, COMMUNICATION_SKILLS, FINANCIAL_LITERACY, PRODUCTIVITY_HABITS</td></tr>
 * </table>
 *
 * @see ConceptArea#getDomain()
 */
public enum ConceptDomain {

    /** Language syntax, OOP, FP, generics, language features. */
    PROGRAMMING_FUNDAMENTALS("Programming Fundamentals"),

    /** Algorithms, data structures, complexity, concurrency, memory, JVM internals (GC, class loading, serialization, JVM languages). */
    CORE_CS("Core CS"),

    /** Design patterns, clean code, testing, API design, architecture. */
    SOFTWARE_ENGINEERING("Software Engineering"),

    /** System design, databases, distributed systems, networking, OS. */
    SYSTEM_DESIGN("System Design & Infrastructure"),

    /** CI/CD, containers, version control, build tools, IaC, observability. */
    DEVOPS_TOOLING("DevOps & Tooling"),

    /** Web security, cryptography. */
    SECURITY("Security"),

    /** Machine learning, deep learning, LLMs, prompt engineering. */
    AI_DATA("AI & Data"),

    /** Interview prep, career development, getting started. */
    CAREER_META("Career & Meta"),

    /** Self-improvement, communication, financial literacy, productivity habits. */
    PERSONAL_DEVELOPMENT("Personal Development");

    private final String displayName;

    ConceptDomain(final String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the human-readable display name.
     *
     * @return the display name (e.g., "Core CS", "DevOps & Tooling")
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Resolves a {@link ConceptDomain} from a string (case-insensitive).
     *
     * @param value the string to parse
     * @return the matching concept domain
     * @throws IllegalArgumentException if no match is found
     */
    public static ConceptDomain fromString(final String value) {
        EnumParseUtils.requireNonBlank(value, "Concept domain");
        final var normalized = EnumParseUtils.toUnderscored(value).replace("&", "");
        for (final ConceptDomain domain : values()) {
            if (domain.name().equalsIgnoreCase(normalized)
                    || domain.displayName.equalsIgnoreCase(value.strip())) {
                return domain;
            }
        }
        // Try partial match on display name
        final var lowerInput = EnumParseUtils.normalize(value);
        for (final ConceptDomain domain : values()) {
            if (domain.displayName.toLowerCase().contains(lowerInput)) {
                return domain;
            }
        }
        throw new IllegalArgumentException("Unknown concept domain: '" + value + "'");
    }
}
