package server.learningresources.vault.providers;

import server.learningresources.model.LearningResource;
import server.learningresources.model.ResourceCategory;
import server.learningresources.model.ResourceType;
import server.learningresources.vault.ResourceProvider;

import java.time.Instant;
import java.util.List;

/**
 * Curated Java learning resources — official documentation, community tutorials,
 * recommended books, and open-source projects to study.
 *
 * <p>Includes: Oracle Tutorials, JDK Javadoc, JLS, Inside.java, Baeldung, Jenkov,
 * Effective Java, Java Concurrency in Practice, Spring Boot Guides, Guava, JUnit 5.
 */
public final class JavaResources implements ResourceProvider {

    @Override
    public List<LearningResource> resources() {
        final var now = Instant.now();
        return List.of(

                // ─── Official Documentation ─────────────────────────────────

                new LearningResource(
                        "oracle-java-tutorials",
                        "The Java Tutorials (dev.java)",
                        "https://dev.java/learn/",
                        "Oracle's official Java tutorials covering language basics, OOP, generics, "
                                + "lambdas, streams, collections, modules, records, sealed classes, "
                                + "pattern matching, and more. The authoritative starting point for Java.",
                        ResourceType.TUTORIAL,
                        List.of(ResourceCategory.JAVA),
                        List.of("official", "language-basics", "oop", "generics", "streams", "modules"),
                        "Oracle / dev.java",
                        "beginner",
                        true, now
                ),

                new LearningResource(
                        "jdk-javadoc",
                        "JDK API Documentation (Javadoc)",
                        "https://docs.oracle.com/en/java/javase/21/docs/api/",
                        "Official JDK 21 API documentation — the complete reference for every "
                                + "class, method, and package in the standard library.",
                        ResourceType.API_REFERENCE,
                        List.of(ResourceCategory.JAVA),
                        List.of("official", "api", "javadoc", "reference"),
                        "Oracle",
                        "intermediate",
                        true, now
                ),

                new LearningResource(
                        "java-language-spec",
                        "The Java Language Specification (JLS)",
                        "https://docs.oracle.com/javase/specs/jls/se21/html/index.html",
                        "The formal specification of the Java programming language. "
                                + "Defines the syntax, type system, and semantics. Essential for "
                                + "understanding edge cases and language guarantees.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.JAVA),
                        List.of("official", "specification", "jls", "formal"),
                        "Oracle",
                        "advanced",
                        true, now
                ),

                new LearningResource(
                        "inside-java",
                        "Inside.java — Official Java Blog",
                        "https://inside.java/",
                        "The official blog from the Java team at Oracle. Features JEP previews, "
                                + "deep dives into new features, performance analysis, and interviews "
                                + "with JDK engineers.",
                        ResourceType.BLOG,
                        List.of(ResourceCategory.JAVA),
                        List.of("official", "blog", "jep", "new-features", "performance"),
                        "Oracle Java Team",
                        "intermediate",
                        true, now
                ),

                // ─── Community Resources ────────────────────────────────────

                new LearningResource(
                        "baeldung-java",
                        "Baeldung — Java Tutorials & Guides",
                        "https://www.baeldung.com/",
                        "Comprehensive tutorial site covering Spring, Java core, testing, "
                                + "persistence, security, and modern Java features. Practical, "
                                + "example-driven articles with working code.",
                        ResourceType.TUTORIAL,
                        List.of(ResourceCategory.JAVA, ResourceCategory.WEB, ResourceCategory.TESTING),
                        List.of("spring", "tutorials", "practical", "spring-boot", "testing"),
                        "Baeldung",
                        "intermediate",
                        true, now
                ),

                new LearningResource(
                        "jenkov-tutorials",
                        "Jenkov.com — Java & Web Tutorials",
                        "https://jenkov.com/",
                        "In-depth tutorials on Java concurrency, networking, NIO, JDBC, "
                                + "design patterns, and web technologies. Known for exceptionally "
                                + "clear explanations of complex topics.",
                        ResourceType.TUTORIAL,
                        List.of(ResourceCategory.JAVA, ResourceCategory.WEB),
                        List.of("concurrency", "nio", "design-patterns", "networking"),
                        "Jakob Jenkov",
                        "intermediate",
                        true, now
                ),

                // ─── Books ──────────────────────────────────────────────────

                new LearningResource(
                        "effective-java",
                        "Effective Java (3rd Edition) — Joshua Bloch",
                        "https://www.oreilly.com/library/view/effective-java/9780134686097/",
                        "The definitive best-practices guide for Java. 90 items covering "
                                + "object creation, equality, generics, enums, lambdas, streams, "
                                + "concurrency, and serialization.",
                        ResourceType.BOOK,
                        List.of(ResourceCategory.JAVA, ResourceCategory.SOFTWARE_ENGINEERING),
                        List.of("best-practices", "design", "patterns", "joshua-bloch"),
                        "Joshua Bloch",
                        "intermediate",
                        false, now
                ),

                new LearningResource(
                        "java-concurrency-in-practice",
                        "Java Concurrency in Practice",
                        "https://jcip.net/",
                        "The gold standard reference for multithreaded Java programming. "
                                + "Covers thread safety, synchronization, the Java Memory Model, "
                                + "concurrent collections, and task execution frameworks.",
                        ResourceType.BOOK,
                        List.of(ResourceCategory.JAVA),
                        List.of("concurrency", "threads", "memory-model", "synchronization"),
                        "Brian Goetz",
                        "advanced",
                        false, now
                ),

                // ─── Open Source Projects ───────────────────────────────────

                new LearningResource(
                        "spring-boot-guides",
                        "Spring Boot — Getting Started Guides",
                        "https://spring.io/guides",
                        "Official Spring Boot guides covering REST APIs, data access, security, "
                                + "messaging, testing, and deployment. Each guide is a complete "
                                + "working project.",
                        ResourceType.TUTORIAL,
                        List.of(ResourceCategory.JAVA, ResourceCategory.WEB),
                        List.of("spring-boot", "rest-api", "microservices", "guides"),
                        "VMware / Spring Team",
                        "intermediate",
                        true, now
                ),

                new LearningResource(
                        "guava-wiki",
                        "Google Guava — Wiki & User Guide",
                        "https://github.com/google/guava/wiki",
                        "User guide for Google Guava — a widely-used utility library. "
                                + "Learn immutable collections, caching, hashing, string utilities, "
                                + "and functional idioms from world-class engineers.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.JAVA),
                        List.of("guava", "collections", "caching", "utilities", "google"),
                        "Google",
                        "intermediate",
                        true, now
                ),

                new LearningResource(
                        "junit5-user-guide",
                        "JUnit 5 User Guide",
                        "https://junit.org/junit5/docs/current/user-guide/",
                        "Official guide for JUnit 5 — the standard Java testing framework. "
                                + "Covers assertions, parameterized tests, extensions, nested tests, "
                                + "and migration from JUnit 4.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.JAVA, ResourceCategory.TESTING),
                        List.of("junit", "testing", "unit-tests", "tdd"),
                        "JUnit Team",
                        "intermediate",
                        true, now
                )
        );
    }
}
