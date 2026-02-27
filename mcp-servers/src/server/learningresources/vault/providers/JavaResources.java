package server.learningresources.vault.providers;

import server.learningresources.model.ConceptArea;
import server.learningresources.model.ContentFreshness;
import server.learningresources.model.LanguageApplicability;
import server.learningresources.model.DifficultyLevel;
import server.learningresources.model.LearningResource;
import server.learningresources.model.ResourceCategory;
import server.learningresources.model.ResourceType;
import server.learningresources.vault.ResourceProvider;

import java.time.Instant;
import java.util.List;

/**
 * Curated Java ecosystem learning resources — official docs, community, books, OSS.
 *
 * <p>Covers Oracle/dev.java documentation, JDK APIs, community tutorials (Baeldung,
 * Jenkov), landmark books (Effective Java, JCIP), and key open-source projects.
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
                        List.of(ConceptArea.LANGUAGE_BASICS, ConceptArea.OOP, ConceptArea.GENERICS,
                                ConceptArea.FUNCTIONAL_PROGRAMMING, ConceptArea.LANGUAGE_FEATURES,
                                ConceptArea.GETTING_STARTED),
                        List.of("official", "language-basics", "oop", "generics", "streams", "modules",
                                "records", "sealed-classes", "pattern-matching"),
                        "Oracle / dev.java",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_CENTRIC, now
                ),

                new LearningResource(
                        "jdk-javadoc",
                        "JDK API Documentation (Javadoc)",
                        "https://docs.oracle.com/en/java/javase/21/docs/api/",
                        "Official JDK 21 API documentation — the complete reference for every "
                                + "class, method, and package in the standard library.",
                        ResourceType.API_REFERENCE,
                        List.of(ResourceCategory.JAVA),
                        List.of(ConceptArea.LANGUAGE_FEATURES, ConceptArea.CONCURRENCY,
                                ConceptArea.DATA_STRUCTURES),
                        List.of("official", "api", "javadoc", "reference", "jdk21"),
                        "Oracle",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_CENTRIC, now
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
                        List.of(ConceptArea.LANGUAGE_FEATURES, ConceptArea.OOP, ConceptArea.GENERICS,
                                ConceptArea.CONCURRENCY, ConceptArea.MEMORY_MANAGEMENT),
                        List.of("official", "specification", "jls", "formal", "type-system"),
                        "Oracle",
                        DifficultyLevel.EXPERT,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_CENTRIC, now
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
                        List.of(ConceptArea.LANGUAGE_FEATURES, ConceptArea.CONCURRENCY,
                                ConceptArea.MEMORY_MANAGEMENT),
                        List.of("official", "blog", "jep", "new-features", "performance",
                                "virtual-threads", "project-loom", "project-amber"),
                        "Oracle Java Team",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_CENTRIC, now
                ),

                new LearningResource(
                        "jvm-specification",
                        "The JVM Specification (JVMS)",
                        "https://docs.oracle.com/javase/specs/jvms/se21/html/index.html",
                        "The formal specification of the Java Virtual Machine. Defines class file "
                                + "format, bytecode instructions, linking, loading, and execution. "
                                + "Essential for understanding JVM internals and performance tuning.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.JAVA),
                        List.of(ConceptArea.MEMORY_MANAGEMENT, ConceptArea.LANGUAGE_FEATURES),
                        List.of("official", "jvm", "bytecode", "class-file", "specification"),
                        "Oracle",
                        DifficultyLevel.EXPERT,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_CENTRIC, now
                ),

                new LearningResource(
                        "openjdk",
                        "OpenJDK — Source Code & JEP Process",
                        "https://openjdk.org/",
                        "The open-source JDK project. Browse source code, track JEP proposals, "
                                + "follow Project Loom (virtual threads), Amber (language features), "
                                + "Valhalla (value types), and Panama (native interop).",
                        ResourceType.REPOSITORY,
                        List.of(ResourceCategory.JAVA),
                        List.of(ConceptArea.LANGUAGE_FEATURES, ConceptArea.CONCURRENCY),
                        List.of("official", "open-source", "jep", "loom", "amber", "valhalla", "panama"),
                        "OpenJDK Community",
                        DifficultyLevel.ADVANCED,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_CENTRIC, now
                ),

                // ─── Community Resources ────────────────────────────────────

                new LearningResource(
                        "baeldung-java",
                        "Baeldung — Java Tutorials & Guides",
                        "https://www.baeldung.com/",
                        "Comprehensive tutorial site covering Spring, Java core, testing, "
                                + "persistence, security, and modern Java features. Practical, "
                                + "well-organized, and frequently updated.",
                        ResourceType.TUTORIAL,
                        List.of(ResourceCategory.JAVA),
                        List.of(ConceptArea.LANGUAGE_FEATURES, ConceptArea.TESTING,
                                ConceptArea.API_DESIGN, ConceptArea.CONCURRENCY),
                        List.of("spring", "testing", "persistence", "security", "rest-api",
                                "hibernate", "maven", "gradle"),
                        "Baeldung / Eugen Paraschiv",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.JAVA_CENTRIC, now
                ),

                new LearningResource(
                        "jenkov-tutorials",
                        "Jenkov.com — Java & Web Tutorials",
                        "https://jenkov.com/tutorials/java/index.html",
                        "Clear, standalone tutorials on Java concurrency, NIO, networking, "
                                + "JDBC, and web technologies. Excellent for deep-diving into "
                                + "specific Java APIs.",
                        ResourceType.TUTORIAL,
                        List.of(ResourceCategory.JAVA),
                        List.of(ConceptArea.CONCURRENCY, ConceptArea.NETWORKING,
                                ConceptArea.DATABASES, ConceptArea.LANGUAGE_FEATURES),
                        List.of("concurrency", "nio", "networking", "jdbc", "threads"),
                        "Jakob Jenkov",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.PERIODICALLY_UPDATED,
                        false, true, LanguageApplicability.JAVA_CENTRIC, now
                ),

                // ─── Books ──────────────────────────────────────────────────

                new LearningResource(
                        "effective-java",
                        "Effective Java (3rd Edition) — Joshua Bloch",
                        "https://www.oreilly.com/library/view/effective-java/9780134686097/",
                        "The essential best-practices book for Java. 90 items covering object "
                                + "creation, generics, enums, lambdas, streams, concurrency, and "
                                + "serialization. Written by a JDK architect.",
                        ResourceType.BOOK,
                        List.of(ResourceCategory.JAVA, ResourceCategory.SOFTWARE_ENGINEERING),
                        List.of(ConceptArea.CLEAN_CODE, ConceptArea.DESIGN_PATTERNS,
                                ConceptArea.CONCURRENCY, ConceptArea.GENERICS),
                        List.of("best-practices", "design-patterns", "concurrency", "generics",
                                "enums", "lambdas", "streams"),
                        "Joshua Bloch",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.EVERGREEN,
                        false, false, LanguageApplicability.JAVA_CENTRIC, now
                ),

                new LearningResource(
                        "java-concurrency-in-practice",
                        "Java Concurrency in Practice (JCIP)",
                        "https://jcip.net/",
                        "The definitive book on concurrent programming in Java. Covers thread "
                                + "safety, locks, executors, concurrent collections, the Java Memory "
                                + "Model, and composing concurrent objects.",
                        ResourceType.BOOK,
                        List.of(ResourceCategory.JAVA),
                        List.of(ConceptArea.CONCURRENCY, ConceptArea.MEMORY_MANAGEMENT,
                                ConceptArea.DESIGN_PATTERNS),
                        List.of("concurrency", "threads", "locks", "executors", "java-memory-model",
                                "thread-safety", "concurrent-collections"),
                        "Brian Goetz et al.",
                        DifficultyLevel.ADVANCED,
                        ContentFreshness.EVERGREEN,
                        false, false, LanguageApplicability.JAVA_CENTRIC, now
                ),

                // ─── Open Source Projects ───────────────────────────────────

                new LearningResource(
                        "spring-boot-guides",
                        "Spring Boot — Getting Started Guides",
                        "https://spring.io/guides",
                        "Official Spring Boot guides. Quick, hands-on tutorials for REST services, "
                                + "data access, security, testing, messaging, and more.",
                        ResourceType.TUTORIAL,
                        List.of(ResourceCategory.JAVA, ResourceCategory.WEB),
                        List.of(ConceptArea.API_DESIGN, ConceptArea.TESTING,
                                ConceptArea.ARCHITECTURE, ConceptArea.GETTING_STARTED),
                        List.of("spring", "spring-boot", "rest", "data", "security", "testing"),
                        "VMware / Broadcom",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_CENTRIC, now
                ),

                new LearningResource(
                        "guava-wiki",
                        "Google Guava — Wiki & User Guide",
                        "https://github.com/google/guava/wiki",
                        "Guide to Google Guava: collections, caching, primitives, concurrency "
                                + "utilities, strings, I/O, hashing, event bus, and math. "
                                + "A masterclass in API design.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.JAVA),
                        List.of(ConceptArea.DATA_STRUCTURES, ConceptArea.CONCURRENCY,
                                ConceptArea.CLEAN_CODE),
                        List.of("guava", "collections", "caching", "concurrency", "utility"),
                        "Google",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_CENTRIC, now
                ),

                new LearningResource(
                        "junit5-user-guide",
                        "JUnit 5 User Guide",
                        "https://junit.org/junit5/docs/current/user-guide/",
                        "Official guide to JUnit 5 — the current standard testing framework for Java. "
                                + "Covers annotations, assertions, parameterized tests, extensions, "
                                + "nested tests, and test lifecycle.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.JAVA, ResourceCategory.TESTING),
                        List.of(ConceptArea.TESTING, ConceptArea.CLEAN_CODE),
                        List.of("junit", "testing", "tdd", "unit-tests", "parameterized", "extensions"),
                        "JUnit Team",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_CENTRIC, now
                )
,
                // ─── JDK Upgrade & Version Management ──────────────────────────

                new LearningResource(
                        "jdk25-release-notes",
                        "JDK 25 Release Notes (OpenJDK)",
                        "https://openjdk.org/projects/jdk/25/",
                        "Official OpenJDK project page for JDK 25 (LTS, September 2025). Lists "
                                + "all JEPs (JDK Enhancement Proposals) included in the release: "
                                + "Project Loom completions, Valhalla value types, Amber pattern "
                                + "matching enhancements, Panama foreign function/memory API finalization, "
                                + "and more. The go-to reference before upgrading from JDK 21 or earlier.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.JAVA),
                        List.of(ConceptArea.LANGUAGE_FEATURES, ConceptArea.GETTING_STARTED),
                        List.of("jdk25", "java25", "release-notes", "jep", "lts", "upgrade",
                                "openjdk", "new-features", "migration"),
                        "OpenJDK / Oracle",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_CENTRIC, now
                ),

                new LearningResource(
                        "jdk25-migration-guide",
                        "JDK 25 Migration Guide",
                        "https://docs.oracle.com/en/java/javase/25/migrate/",
                        "Oracle's official guide for migrating applications to JDK 25. Covers "
                                + "incompatibilities, deprecated APIs to remove, module system changes, "
                                + "strong encapsulation updates, and performance improvements. "
                                + "Step-by-step upgrade path from JDK 21 (previous LTS) including "
                                + "jdeps analysis and multi-release JAR creation.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.JAVA),
                        List.of(ConceptArea.LANGUAGE_FEATURES, ConceptArea.BUILD_TOOLS),
                        List.of("jdk25", "migration", "upgrade", "java25", "jdk21-to-25",
                                "incompatibilities", "modules", "deprecated-apis"),
                        "Oracle",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_CENTRIC, now
                ),

                new LearningResource(
                        "sdkman-jdk-manager",
                        "SDKMAN! — JDK Version Manager",
                        "https://sdkman.io/",
                        "The de-facto tool for managing multiple JDK distributions on Linux, "
                                + "macOS, and Windows (Git Bash / WSL). Install and switch between "
                                + "OpenJDK, Eclipse Temurin, GraalVM, Corretto, and Zulu with one "
                                + "command: 'sdk install java 25-tem'. Supports .sdkmanrc for "
                                + "per-project JDK pinning — ideal for teams maintaining multiple "
                                + "Java versions across different microservices.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.JAVA, ResourceCategory.TOOLS),
                        List.of(ConceptArea.GETTING_STARTED, ConceptArea.BUILD_TOOLS,
                                ConceptArea.LANGUAGE_FEATURES),
                        List.of("sdkman", "jdk", "version-manager", "java-versions", "temurin",
                                "graalvm", "corretto", "zulu", "linux", "macos", "wsl", "upgrade"),
                        "SDKMAN! Community",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.JAVA_CENTRIC, now
                ),

                new LearningResource(
                        "eclipse-temurin",
                        "Eclipse Temurin — Open-Source JDK Builds (Adoptium)",
                        "https://adoptium.net/",
                        "Adoptium's Eclipse Temurin provides production-ready, open-source JDK "
                                + "builds for every major version (8, 11, 17, 21, 25). Download "
                                + "installers for Windows, macOS, Linux, or use via SDKMAN! / Docker "
                                + "images. Backed by Microsoft, IBM, Google, and Red Hat. The "
                                + "recommended open-source alternative to Oracle JDK.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.JAVA, ResourceCategory.TOOLS),
                        List.of(ConceptArea.GETTING_STARTED, ConceptArea.LANGUAGE_FEATURES),
                        List.of("temurin", "adoptium", "open-source-jdk", "jdk-download",
                                "jdk25", "jdk21", "production-ready", "windows", "macos", "linux",
                                "docker", "openjdk"),
                        "Eclipse Foundation / Adoptium",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_CENTRIC, now
                )
        );
    }
}
