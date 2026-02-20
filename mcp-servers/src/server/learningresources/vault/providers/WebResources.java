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
 * Curated web-development learning resources — official docs, language references,
 * community tutorials.
 */
public final class WebResources implements ResourceProvider {

    @Override
    public List<LearningResource> resources() {
        final var now = Instant.now();
        return List.of(

                new LearningResource(
                        "mdn-web-docs",
                        "MDN Web Docs",
                        "https://developer.mozilla.org/",
                        "Mozilla's comprehensive reference for HTML, CSS, JavaScript, Web APIs, "
                                + "and HTTP. The de-facto standard documentation for web technologies.",
                        ResourceType.API_REFERENCE,
                        List.of(ResourceCategory.WEB, ResourceCategory.JAVASCRIPT),
                        List.of(ConceptArea.LANGUAGE_BASICS, ConceptArea.API_DESIGN,
                                ConceptArea.WEB_SECURITY, ConceptArea.NETWORKING),
                        List.of("official", "html", "css", "javascript", "web-api", "http",
                                "reference", "accessibility"),
                        "Mozilla",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.WEB_SPECIFIC, now
                ),

                new LearningResource(
                        "javascript-info",
                        "The Modern JavaScript Tutorial (javascript.info)",
                        "https://javascript.info/",
                        "In-depth JavaScript tutorial covering the language from basics to "
                                + "advanced topics: closures, prototypes, async/await, DOM, events, "
                                + "network requests, and browser internals.",
                        ResourceType.TUTORIAL,
                        List.of(ResourceCategory.JAVASCRIPT, ResourceCategory.WEB),
                        List.of(ConceptArea.LANGUAGE_BASICS, ConceptArea.OOP,
                                ConceptArea.FUNCTIONAL_PROGRAMMING, ConceptArea.CONCURRENCY),
                        List.of("javascript", "closures", "prototypes", "async", "dom",
                                "events", "promises"),
                        "Ilya Kantor",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.WEB_SPECIFIC, now
                ),

                new LearningResource(
                        "typescript-handbook",
                        "TypeScript Handbook",
                        "https://www.typescriptlang.org/docs/handbook/intro.html",
                        "Official TypeScript handbook — type system, generics, classes, modules, "
                                + "utility types, declaration files, and project configuration.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.JAVASCRIPT, ResourceCategory.WEB),
                        List.of(ConceptArea.LANGUAGE_BASICS, ConceptArea.GENERICS,
                                ConceptArea.OOP, ConceptArea.LANGUAGE_FEATURES),
                        List.of("official", "typescript", "type-system", "generics", "modules"),
                        "Microsoft",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.WEB_SPECIFIC, now
                ),

                new LearningResource(
                        "w3c-web-standards",
                        "W3C Web Standards & Specifications",
                        "https://www.w3.org/standards/",
                        "The World Wide Web Consortium maintains standards for HTML, CSS, "
                                + "accessibility (WCAG), SVG, and more. The definitive specs.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.WEB),
                        List.of(ConceptArea.API_DESIGN, ConceptArea.NETWORKING),
                        List.of("official", "standards", "html", "css", "accessibility", "wcag",
                                "svg", "specification"),
                        "W3C",
                        DifficultyLevel.ADVANCED,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.WEB_SPECIFIC, now
                ),

                new LearningResource(
                        "web-dev-google",
                        "web.dev — Google's Web Development Guidance",
                        "https://web.dev/",
                        "Google's guidance for modern web development. Performance, "
                                + "accessibility, PWAs, Core Web Vitals, best practices, and case studies.",
                        ResourceType.TUTORIAL,
                        List.of(ResourceCategory.WEB),
                        List.of(ConceptArea.ARCHITECTURE, ConceptArea.TESTING,
                                ConceptArea.OBSERVABILITY),
                        List.of("performance", "pwa", "core-web-vitals", "lighthouse",
                                "accessibility", "best-practices"),
                        "Google Chrome Team",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.WEB_SPECIFIC, now
                )
        );
    }
}
