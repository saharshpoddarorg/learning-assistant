package server.learningresources.model;

/**
 * Classifies how a learning resource relates to programming languages.
 *
 * <p>Resources range from universal (algorithms apply to any language) to
 * language-specific (JVM internals are Java-only). This dimension lets the
 * discovery engine weight results based on the user's language focus — typically
 * Java — while still surfacing valuable cross-cutting content.
 *
 * <h2>Classification Guide</h2>
 * <table>
 *   <tr><th>Level</th><th>Example Topics</th></tr>
 *   <tr><td>UNIVERSAL</td><td>algorithms, design patterns, clean code, testing theory</td></tr>
 *   <tr><td>MULTI_LANGUAGE</td><td>REST APIs, containers, CI/CD, SQL</td></tr>
 *   <tr><td>JAVA_CENTRIC</td><td>OOP concepts illustrated with Java but transferable</td></tr>
 *   <tr><td>JAVA_SPECIFIC</td><td>JVM internals, Java modules, virtual threads, records</td></tr>
 *   <tr><td>PYTHON_SPECIFIC</td><td>Python-only features: decorators, GIL, asyncio</td></tr>
 *   <tr><td>WEB_SPECIFIC</td><td>JS/TS-only features: event loop, DOM, React hooks</td></tr>
 * </table>
 *
 * @see LearningResource
 */
public enum LanguageApplicability {

    /**
     * Language-agnostic — applies equally to any programming language.
     *
     * <p>Examples: algorithms, data structures, design patterns, SOLID principles,
     * clean code, testing theory, system design, math foundations.
     */
    UNIVERSAL("universal", "Applies to all languages equally"),

    /**
     * Applicable across multiple languages but not truly universal.
     *
     * <p>Examples: REST API design, SQL, containers, CI/CD pipelines,
     * web security basics (OWASP), version control concepts.
     */
    MULTI_LANGUAGE("multi-language", "Applies to multiple languages/platforms"),

    /**
     * Primarily taught through Java but concepts transfer to other OOP languages.
     *
     * <p>Examples: OOP patterns using Java syntax, Spring-style DI concepts,
     * JUnit testing patterns (applicable to NUnit, pytest), concurrency patterns
     * illustrated with Java threads but transferable to Go goroutines, etc.
     */
    JAVA_CENTRIC("java-centric", "Java-focused but concepts are transferable"),

    /**
     * Specific to Java / JVM — not directly applicable to other languages.
     *
     * <p>Examples: JVM garbage collection tuning, Java modules (JPMS),
     * virtual threads, records, sealed classes, pattern matching, Javadoc,
     * Maven/Gradle build systems, JMX, JFR.
     */
    JAVA_SPECIFIC("java-specific", "Java/JVM-only — not transferable"),

    /**
     * Specific to Python — not directly applicable to other languages.
     *
     * <p>Examples: Python decorators, GIL, asyncio, list comprehensions,
     * Jupyter notebooks, pip/conda, dunder methods.
     */
    PYTHON_SPECIFIC("python-specific", "Python-only — not transferable"),

    /**
     * Specific to JavaScript/TypeScript and the web platform.
     *
     * <p>Examples: event loop, DOM manipulation, React/Vue/Angular hooks,
     * npm/yarn, Service Workers, Web APIs, TypeScript type system.
     */
    WEB_SPECIFIC("web-specific", "JS/TS/Web-only — not transferable");

    private final String displayName;
    private final String description;

    LanguageApplicability(final String displayName, final String description) {
        this.displayName = displayName;
        this.description = description;
    }

    /**
     * Returns the kebab-case display name (e.g., "java-centric").
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns a short human-readable description of the applicability level.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Checks whether this level is transferable beyond a single language.
     *
     * @return {@code true} if concepts are transferable to other languages
     */
    public boolean isTransferable() {
        return this == UNIVERSAL || this == MULTI_LANGUAGE || this == JAVA_CENTRIC;
    }

    /**
     * Checks whether this level is Java-relevant (either Java-specific or Java-centric).
     *
     * @return {@code true} if the resource is relevant to Java learners
     */
    public boolean isJavaRelevant() {
        return this == UNIVERSAL || this == MULTI_LANGUAGE
                || this == JAVA_CENTRIC || this == JAVA_SPECIFIC;
    }

    /**
     * Resolves a {@link LanguageApplicability} from a string (case-insensitive).
     *
     * <p>Accepts display names ("java-centric"), enum names ("JAVA_CENTRIC"),
     * and common shorthands ("java", "universal", "python", "web").
     *
     * @param value the string to parse
     * @return the matching applicability level
     * @throws IllegalArgumentException if no match is found
     */
    public static LanguageApplicability fromString(final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("LanguageApplicability must not be null or blank");
        }
        final var normalized = value.strip().toLowerCase();

        for (final LanguageApplicability level : values()) {
            if (level.displayName.equals(normalized) || level.name().equalsIgnoreCase(normalized)) {
                return level;
            }
        }

        // Common shorthands
        return switch (normalized) {
            case "java" -> JAVA_SPECIFIC;
            case "jvm" -> JAVA_SPECIFIC;
            case "python" -> PYTHON_SPECIFIC;
            case "web", "js", "javascript", "typescript" -> WEB_SPECIFIC;
            case "all", "any", "general" -> UNIVERSAL;
            case "multi", "cross", "cross-language" -> MULTI_LANGUAGE;
            default -> throw new IllegalArgumentException(
                    "Unknown language applicability: '" + value + "'. "
                            + "Valid values: universal, multi-language, java-centric, "
                            + "java-specific, python-specific, web-specific");
        };
    }
}
