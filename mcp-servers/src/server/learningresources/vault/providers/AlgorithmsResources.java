package server.learningresources.vault.providers;

import server.learningresources.model.LearningResource;
import server.learningresources.model.ResourceCategory;
import server.learningresources.model.ResourceType;
import server.learningresources.vault.ResourceProvider;

import java.time.Instant;
import java.util.List;

/**
 * Curated algorithms and data structures learning resources.
 *
 * <p>Includes: VisuAlgo (interactive visualizations), Big-O Cheat Sheet.
 */
public final class AlgorithmsResources implements ResourceProvider {

    @Override
    public List<LearningResource> resources() {
        final var now = Instant.now();
        return List.of(

                new LearningResource(
                        "algorithms-visualgo",
                        "VisuAlgo — Visualizing Algorithms",
                        "https://visualgo.net/en",
                        "Interactive visualizations of sorting, searching, graph, and tree "
                                + "algorithms. See each step animate in real-time. Excellent for "
                                + "building intuition about algorithmic behavior.",
                        ResourceType.INTERACTIVE,
                        List.of(ResourceCategory.ALGORITHMS),
                        List.of("visualization", "sorting", "graphs", "trees", "interactive"),
                        "Steven Halim",
                        "beginner",
                        true, now
                ),

                new LearningResource(
                        "big-o-cheatsheet",
                        "Big-O Cheat Sheet",
                        "https://www.bigocheatsheet.com/",
                        "Quick reference for time and space complexity of common "
                                + "data structures and algorithms. Covers arrays, linked lists, "
                                + "trees, graphs, sorting, and searching.",
                        ResourceType.CHEAT_SHEET,
                        List.of(ResourceCategory.ALGORITHMS),
                        List.of("big-o", "complexity", "cheatsheet", "reference"),
                        "Eric Rowell",
                        "beginner",
                        true, now
                )
        );
    }
}
