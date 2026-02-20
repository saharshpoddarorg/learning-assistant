package server.learningresources.vault.providers;

import server.learningresources.model.LearningResource;
import server.learningresources.model.ResourceCategory;
import server.learningresources.model.ResourceType;
import server.learningresources.vault.ResourceProvider;

import java.time.Instant;
import java.util.List;

/**
 * General-purpose and cross-cutting learning resources.
 *
 * <p>Includes: roadmap.sh (developer roadmaps), Free Programming Books,
 * and the Testing Trophy (testing strategy).
 */
public final class GeneralResources implements ResourceProvider {

    @Override
    public List<LearningResource> resources() {
        final var now = Instant.now();
        return List.of(

                new LearningResource(
                        "testing-trophy",
                        "The Testing Trophy — Kent C. Dodds",
                        "https://kentcdodds.com/blog/the-testing-trophy-and-testing-classifications",
                        "Kent C. Dodds' influential testing strategy that prioritizes integration tests "
                                + "over unit tests. Explains the Testing Trophy as an evolution of the "
                                + "Test Pyramid, with practical guidance on test distribution.",
                        ResourceType.ARTICLE,
                        List.of(ResourceCategory.TESTING, ResourceCategory.JAVASCRIPT),
                        List.of("testing", "integration-tests", "tdd", "react-testing-library"),
                        "Kent C. Dodds",
                        "intermediate",
                        true, now
                ),

                new LearningResource(
                        "developer-roadmaps",
                        "roadmap.sh — Developer Roadmaps",
                        "https://roadmap.sh/",
                        "Community-driven roadmaps, guides, and educational content for developers. "
                                + "Interactive roadmaps for frontend, backend, DevOps, full-stack, "
                                + "Android, PostgreSQL, and many more tech career paths.",
                        ResourceType.INTERACTIVE,
                        List.of(ResourceCategory.GENERAL),
                        List.of("roadmap", "career", "learning-path", "frontend", "backend", "devops"),
                        "Kamran Ahmed & Community",
                        "beginner",
                        true, now
                ),

                new LearningResource(
                        "free-programming-books",
                        "Free Programming Books — EbookFoundation",
                        "https://ebookfoundation.github.io/free-programming-books/",
                        "Massive curated list of free learning resources: books, courses, podcasts, "
                                + "and interactive tutorials in virtually every programming language "
                                + "and technology. Community-maintained on GitHub.",
                        ResourceType.BOOK,
                        List.of(ResourceCategory.GENERAL),
                        List.of("books", "free", "open-source", "learning", "programming"),
                        "EbookFoundation & Contributors",
                        "beginner",
                        true, now
                )
        );
    }
}
