package server.learningresources.vault.providers;

import server.learningresources.model.LearningResource;
import server.learningresources.model.ResourceCategory;
import server.learningresources.model.ResourceType;
import server.learningresources.vault.ResourceProvider;

import java.time.Instant;
import java.util.List;

/**
 * Curated Python learning resources — official documentation and community tutorials.
 *
 * <p>Includes: The Python Tutorial (official), Real Python.
 */
public final class PythonResources implements ResourceProvider {

    @Override
    public List<LearningResource> resources() {
        final var now = Instant.now();
        return List.of(

                new LearningResource(
                        "python-docs-tutorial",
                        "The Python Tutorial (Official)",
                        "https://docs.python.org/3/tutorial/",
                        "Official Python tutorial from python.org. Covers data structures, "
                                + "control flow, functions, modules, I/O, classes, and the standard library.",
                        ResourceType.TUTORIAL,
                        List.of(ResourceCategory.PYTHON),
                        List.of("official", "python3", "language-basics", "stdlib"),
                        "Python Software Foundation",
                        "beginner",
                        true, now
                ),

                new LearningResource(
                        "real-python",
                        "Real Python — Tutorials & Articles",
                        "https://realpython.com/",
                        "High-quality Python tutorials covering web development, data science, "
                                + "automation, testing, and best practices. Mix of free and premium content.",
                        ResourceType.TUTORIAL,
                        List.of(ResourceCategory.PYTHON, ResourceCategory.WEB),
                        List.of("python", "django", "flask", "data-science", "automation"),
                        "Real Python",
                        "intermediate",
                        true, now
                )
        );
    }
}
