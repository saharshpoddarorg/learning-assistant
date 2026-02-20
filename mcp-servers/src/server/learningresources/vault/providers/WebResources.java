package server.learningresources.vault.providers;

import server.learningresources.model.LearningResource;
import server.learningresources.model.ResourceCategory;
import server.learningresources.model.ResourceType;
import server.learningresources.vault.ResourceProvider;

import java.time.Instant;
import java.util.List;

/**
 * Curated web technologies and JavaScript/TypeScript learning resources.
 *
 * <p>Includes: MDN Web Docs, The Modern JavaScript Tutorial, TypeScript Handbook.
 */
public final class WebResources implements ResourceProvider {

    @Override
    public List<LearningResource> resources() {
        final var now = Instant.now();
        return List.of(

                new LearningResource(
                        "mdn-web-docs",
                        "MDN Web Docs",
                        "https://developer.mozilla.org/en-US/",
                        "Mozilla's comprehensive web documentation — the de facto reference "
                                + "for HTML, CSS, JavaScript, Web APIs, HTTP, and accessibility. "
                                + "Includes tutorials, references, and browser compatibility data.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.WEB, ResourceCategory.JAVASCRIPT),
                        List.of("html", "css", "javascript", "web-apis", "http", "reference"),
                        "Mozilla",
                        "beginner",
                        true, now
                ),

                new LearningResource(
                        "javascript-info",
                        "The Modern JavaScript Tutorial (javascript.info)",
                        "https://javascript.info/",
                        "Comprehensive JavaScript tutorial from basics to advanced topics. "
                                + "Covers the language, browser APIs, and Node.js. Known for "
                                + "clear explanations with interactive examples.",
                        ResourceType.TUTORIAL,
                        List.of(ResourceCategory.JAVASCRIPT, ResourceCategory.WEB),
                        List.of("javascript", "dom", "async", "promises", "fetch"),
                        "Ilya Kantor",
                        "beginner",
                        true, now
                ),

                new LearningResource(
                        "typescript-handbook",
                        "TypeScript Handbook",
                        "https://www.typescriptlang.org/docs/handbook/intro.html",
                        "Official TypeScript handbook — type system, generics, utility types, "
                                + "modules, declaration files, and migration guides.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.JAVASCRIPT, ResourceCategory.WEB),
                        List.of("typescript", "types", "generics", "handbook"),
                        "Microsoft",
                        "intermediate",
                        true, now
                )
        );
    }
}
