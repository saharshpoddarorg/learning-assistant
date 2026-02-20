package server.learningresources.vault.providers;

import server.learningresources.model.LearningResource;
import server.learningresources.model.ResourceCategory;
import server.learningresources.model.ResourceType;
import server.learningresources.vault.ResourceProvider;

import java.time.Instant;
import java.util.List;

/**
 * Curated software engineering and design resources — patterns, principles,
 * architecture, and system design.
 *
 * <p>Includes: Refactoring.Guru, Clean Code Summary, Twelve-Factor App,
 * System Design Primer.
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
                        "Visual catalog of 22 GoF design patterns and refactoring techniques. "
                                + "Each pattern includes UML diagrams, real-world analogies, "
                                + "code examples in multiple languages, and when to use/avoid.",
                        ResourceType.TUTORIAL,
                        List.of(ResourceCategory.SOFTWARE_ENGINEERING, ResourceCategory.JAVA),
                        List.of("design-patterns", "refactoring", "solid", "oop", "gof"),
                        "Refactoring.Guru",
                        "intermediate",
                        true, now
                ),

                new LearningResource(
                        "clean-code-summary",
                        "Clean Code (Robert C. Martin) — Summary & Key Takeaways",
                        "https://gist.github.com/wojteklu/73c6914cc446146b8b533c0988cf8d29",
                        "Community-curated summary of Clean Code principles: meaningful names, "
                                + "small functions, single responsibility, DRY, error handling, "
                                + "and writing code that reads like prose.",
                        ResourceType.ARTICLE,
                        List.of(ResourceCategory.SOFTWARE_ENGINEERING),
                        List.of("clean-code", "best-practices", "naming", "solid", "dry"),
                        "Robert C. Martin (summary by community)",
                        "beginner",
                        true, now
                ),

                new LearningResource(
                        "twelve-factor-app",
                        "The Twelve-Factor App",
                        "https://12factor.net/",
                        "Methodology for building modern, scalable, maintainable SaaS applications. "
                                + "Covers config, dependencies, backing services, build/release/run, "
                                + "concurrency, and disposability.",
                        ResourceType.ARTICLE,
                        List.of(ResourceCategory.SOFTWARE_ENGINEERING, ResourceCategory.DEVOPS, ResourceCategory.CLOUD),
                        List.of("12-factor", "saas", "cloud-native", "methodology"),
                        "Adam Wiggins / Heroku",
                        "intermediate",
                        true, now
                ),

                new LearningResource(
                        "system-design-primer",
                        "The System Design Primer (GitHub)",
                        "https://github.com/donnemartin/system-design-primer",
                        "Comprehensive guide to system design interview preparation. Covers "
                                + "scalability, load balancing, caching, databases, microservices, "
                                + "message queues, and real-world architecture examples.",
                        ResourceType.REPOSITORY,
                        List.of(ResourceCategory.SOFTWARE_ENGINEERING, ResourceCategory.SYSTEMS),
                        List.of("system-design", "scalability", "architecture", "interview"),
                        "Donne Martin",
                        "advanced",
                        true, now
                )
        );
    }
}
