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
 * Curated algorithms and data-structures learning resources — visualizers,
 * cheat-sheets, and classic references.
 */
public final class AlgorithmsResources implements ResourceProvider {

    @Override
    public List<LearningResource> resources() {
        final var now = Instant.now();
        return List.of(

                new LearningResource(
                        "algorithms-visualgo",
                        "VisuAlgo — Algorithm Visualizations",
                        "https://visualgo.net/",
                        "Interactive visualizations of sorting, searching, graph, and tree "
                                + "algorithms. Excellent for building intuition about how "
                                + "algorithms work step by step.",
                        ResourceType.INTERACTIVE,
                        List.of(ResourceCategory.ALGORITHMS, ResourceCategory.GENERAL),
                        List.of(ConceptArea.ALGORITHMS, ConceptArea.DATA_STRUCTURES),
                        List.of("visualization", "sorting", "searching", "graph", "tree",
                                "interactive"),
                        "Steven Halim",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                new LearningResource(
                        "big-o-cheatsheet",
                        "Big-O Cheat Sheet",
                        "https://www.bigocheatsheet.com/",
                        "Quick reference for time and space complexity of common algorithms "
                                + "and data structures. Includes comparison charts and a clear "
                                + "complexity table for sorting algorithms.",
                        ResourceType.CHEAT_SHEET,
                        List.of(ResourceCategory.ALGORITHMS, ResourceCategory.GENERAL),
                        List.of(ConceptArea.COMPLEXITY_ANALYSIS, ConceptArea.ALGORITHMS,
                                ConceptArea.DATA_STRUCTURES),
                        List.of("big-o", "complexity", "time-complexity", "space-complexity",
                                "sorting", "cheat-sheet"),
                        "Eric Rowell",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.EVERGREEN,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                new LearningResource(
                        "cp-algorithms",
                        "CP-Algorithms (Competitive Programming)",
                        "https://cp-algorithms.com/",
                        "Collection of algorithms and data structures commonly used in "
                                + "competitive programming — with detailed explanations, proofs, "
                                + "and implementations in C++. Covers graph theory, number theory, "
                                + "geometry, strings, and more.",
                        ResourceType.TUTORIAL,
                        List.of(ResourceCategory.ALGORITHMS, ResourceCategory.GENERAL),
                        List.of(ConceptArea.ALGORITHMS, ConceptArea.DATA_STRUCTURES,
                                ConceptArea.COMPLEXITY_ANALYSIS),
                        List.of("competitive-programming", "graph-theory", "number-theory",
                                "dynamic-programming", "geometry", "string-algorithms"),
                        "CP-Algorithms Contributors",
                        DifficultyLevel.ADVANCED,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.UNIVERSAL, now
                )
        );
    }
}
