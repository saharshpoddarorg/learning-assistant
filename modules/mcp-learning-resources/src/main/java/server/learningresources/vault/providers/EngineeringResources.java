package server.learningresources.vault.providers;

import server.learningresources.model.*;
import server.learningresources.vault.ResourceProvider;

import java.time.Instant;
import java.util.List;
import java.util.Set;

/**
 * Curated software engineering resources — design patterns, clean code,
 * architecture, and mathematics foundations.
 *
 * <p>System design resources (HLD, LLD, databases, distributed systems)
 * have been moved to {@link SystemDesignResources}.
 *
 * @see SystemDesignResources
 */
public final class EngineeringResources implements ResourceProvider
{

    @Override
    public List<LearningResource> resources()
    {
        final var now = Instant.now();
        return List.of(

                new LearningResource(
                        "refactoring-guru",
                        "Refactoring.Guru — Design Patterns & Refactoring",
                        "https://refactoring.guru/",
                        "Visual catalog of design patterns and refactoring techniques. Each pattern "
                                + "includes UML diagrams, intent, applicability, code examples in "
                                + "multiple languages, and real-world analogies.",
                        ResourceType.TUTORIAL,
                        Set.of(ResourceCategory.SOFTWARE_ENGINEERING),
                        Set.of(ConceptArea.DESIGN_PATTERNS, ConceptArea.CLEAN_CODE, ConceptArea.OOP),
                        Set.of("design-patterns", "refactoring", "gang-of-four", "oop", "solid",
                                "code-smells"),
                        "Alexander Shvets",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.EVERGREEN,
                        false, true, LanguageApplicability.UNIVERSAL, now,
                        ContentFormat.WEB_RESOURCE,
                        Set.of(ResourceAuthor.ALEXANDER_SHVETS)
                ),

                new LearningResource(
                        "clean-code-summary",
                        "Clean Code — Summary & Principles",
                        "https://gist.github.com/wojteklu/73c6914cc446146b8b533c0988cf8d29",
                        "Community-curated summary of key principles from Robert C. Martin's "
                                + "'Clean Code'. Covers naming, functions, comments, formatting, "
                                + "error handling, and object vs. data structures.",
                        ResourceType.CHEAT_SHEET,
                        Set.of(ResourceCategory.SOFTWARE_ENGINEERING),
                        Set.of(ConceptArea.CLEAN_CODE, ConceptArea.DESIGN_PATTERNS),
                        Set.of("clean-code", "naming", "functions", "formatting", "solid",
                                "error-handling", "best-practices"),
                        "Community (Wojtek Lukaszuk)",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.EVERGREEN,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                new LearningResource(
                        "twelve-factor-app",
                        "The Twelve-Factor App",
                        "https://12factor.net/",
                        "Methodology for building modern, scalable, maintainable SaaS applications. "
                                + "Twelve principles covering config, dependencies, backing services, "
                                + "build/release/run, processes, port binding, and more.",
                        ResourceType.DOCUMENTATION,
                        Set.of(ResourceCategory.SOFTWARE_ENGINEERING, ResourceCategory.DEVOPS),
                        Set.of(ConceptArea.ARCHITECTURE,
                                ConceptArea.SYSTEM_DESIGN,
                                ConceptArea.CI_CD,
                                ConceptArea.CONTAINERS),
                        Set.of("saas", "cloud-native", "microservices", "config", "twelve-factor",
                                "deployment", "scalability"),
                        "Adam Wiggins / Heroku",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.EVERGREEN,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                new LearningResource(
                        "martin-fowler-architecture",
                        "Martin Fowler — Architecture & Patterns",
                        "https://martinfowler.com/architecture/",
                        "Thought leadership on software architecture from Martin Fowler. "
                                + "Covers microservices, event-driven architecture, domain-driven "
                                + "design, refactoring, and enterprise integration patterns.",
                        ResourceType.BLOG,
                        Set.of(ResourceCategory.SOFTWARE_ENGINEERING),
                        Set.of(ConceptArea.ARCHITECTURE,
                                ConceptArea.DESIGN_PATTERNS,
                                ConceptArea.SYSTEM_DESIGN,
                                ConceptArea.HIGH_LEVEL_DESIGN,
                                ConceptArea.RELIABILITY_PATTERNS,
                                ConceptArea.ASYNC_MESSAGING),
                        Set.of("microservices", "ddd", "event-driven", "refactoring",
                                "enterprise-patterns", "continuous-delivery",
                                "async-messaging", "integration-patterns",
                                "circuit-breaker", "hld"),
                        "Martin Fowler",
                        DifficultyLevel.ADVANCED,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.UNIVERSAL, now,
                        ContentFormat.WEB_RESOURCE,
                        Set.of(ResourceAuthor.MARTIN_FOWLER)
                ),

                // ─── Mathematics & Foundations ──────────────────────────────

                new LearningResource(
                        "3b1b-linear-algebra",
                        "3Blue1Brown — Essence of Linear Algebra (Video Series)",
                        "https://www.3blue1brown.com/topics/linear-algebra",
                        "Grant Sanderson's 16-video animated series on linear algebra. "
                                + "Covers vectors, linear transformations, matrix multiplication, "
                                + "determinants, eigenvectors and eigenvalues, and abstract vector "
                                + "spaces — all through geometric intuition rather than formula "
                                + "memorization. Essential prerequisite for ML/deep learning and "
                                + "a gold-standard example of visual mathematical teaching.",
                        ResourceType.PLAYLIST,
                        Set.of(ResourceCategory.SOFTWARE_ENGINEERING,
                                ResourceCategory.AI_ML),
                        Set.of(ConceptArea.MATHEMATICS, ConceptArea.MACHINE_LEARNING),
                        Set.of("3blue1brown", "linear-algebra", "vectors", "matrices",
                                "eigenvalues", "eigenvectors", "determinants", "transformations",
                                "math", "visualizations", "grant-sanderson", "ml-prerequisites"),
                        "Grant Sanderson (3Blue1Brown)",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.EVERGREEN,
                        false, true, LanguageApplicability.UNIVERSAL, now,
                        ContentFormat.WEB_RESOURCE,
                        Set.of(ResourceAuthor.GRANT_SANDERSON)
                )
        );
    }
}
