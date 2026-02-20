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
 * Curated software engineering resources — design patterns, clean code,
 * architecture, and system design.
 */
public final class EngineeringResources implements ResourceProvider {

    @Override
    public List<LearningResource> resources() {
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
                        List.of(ResourceCategory.SOFTWARE_ENGINEERING),
                        List.of(ConceptArea.DESIGN_PATTERNS, ConceptArea.CLEAN_CODE, ConceptArea.OOP),
                        List.of("design-patterns", "refactoring", "gang-of-four", "oop", "solid",
                                "code-smells"),
                        "Alexander Shvets",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.EVERGREEN,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                new LearningResource(
                        "clean-code-summary",
                        "Clean Code — Summary & Principles",
                        "https://gist.github.com/wojteklu/73c6914cc446146b8b533c0988cf8d29",
                        "Community-curated summary of key principles from Robert C. Martin's "
                                + "'Clean Code'. Covers naming, functions, comments, formatting, "
                                + "error handling, and object vs. data structures.",
                        ResourceType.CHEAT_SHEET,
                        List.of(ResourceCategory.SOFTWARE_ENGINEERING),
                        List.of(ConceptArea.CLEAN_CODE, ConceptArea.DESIGN_PATTERNS),
                        List.of("clean-code", "naming", "functions", "formatting", "solid",
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
                        List.of(ResourceCategory.SOFTWARE_ENGINEERING, ResourceCategory.DEVOPS),
                        List.of(ConceptArea.ARCHITECTURE, ConceptArea.SYSTEM_DESIGN,
                                ConceptArea.CI_CD, ConceptArea.CONTAINERS),
                        List.of("saas", "cloud-native", "microservices", "config", "twelve-factor",
                                "deployment", "scalability"),
                        "Adam Wiggins / Heroku",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.EVERGREEN,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                new LearningResource(
                        "system-design-primer",
                        "The System Design Primer",
                        "https://github.com/donnemartin/system-design-primer",
                        "Comprehensive guide to system design interviews and real-world "
                                + "architecture. Covers scalability, load balancing, caching, "
                                + "databases, async processing, CDNs, and microservices.",
                        ResourceType.REPOSITORY,
                        List.of(ResourceCategory.SOFTWARE_ENGINEERING, ResourceCategory.COMPUTER_SCIENCE),
                        List.of(ConceptArea.SYSTEM_DESIGN, ConceptArea.DISTRIBUTED_SYSTEMS,
                                ConceptArea.DATABASES, ConceptArea.NETWORKING,
                                ConceptArea.INTERVIEW_PREP),
                        List.of("system-design", "scalability", "load-balancing", "caching",
                                "databases", "microservices", "interview"),
                        "Donne Martin",
                        DifficultyLevel.ADVANCED,
                        ContentFreshness.PERIODICALLY_UPDATED,
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
                        List.of(ResourceCategory.SOFTWARE_ENGINEERING),
                        List.of(ConceptArea.ARCHITECTURE, ConceptArea.DESIGN_PATTERNS,
                                ConceptArea.SYSTEM_DESIGN),
                        List.of("microservices", "ddd", "event-driven", "refactoring",
                                "enterprise-patterns", "continuous-delivery"),
                        "Martin Fowler",
                        DifficultyLevel.ADVANCED,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.UNIVERSAL, now
                )
        );
    }
}
