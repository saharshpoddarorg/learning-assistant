package server.learningresources.vault.providers;

import server.learningresources.model.ConceptArea;
import server.learningresources.model.ContentFreshness;
import server.learningresources.model.DifficultyLevel;
import server.learningresources.model.LanguageApplicability;
import server.learningresources.model.LearningResource;
import server.learningresources.model.ResourceCategory;
import server.learningresources.model.ResourceType;
import server.learningresources.vault.ResourceProvider;

import java.time.Instant;
import java.util.List;

/**
 * Curated data-structures learning resources — from interactive visualizers
 * and Java Collections deep-dives to open textbooks and structured practice.
 *
 * <p>Complements {@link AlgorithmsResources}, which focuses on algorithm analysis
 * and competitive-programming references. This provider covers the building blocks:
 * arrays, linked lists, trees, heaps, hash tables, graphs, and the Java/Python
 * interfaces that expose them.
 */
public final class DataStructuresResources implements ResourceProvider {

    @Override
    public List<LearningResource> resources() {
        final var now = Instant.now();
        return List.of(

                new LearningResource(
                        "java-collections-docs",
                        "Java Collections Framework — Official Docs",
                        "https://docs.oracle.com/en/java/docs/books/tutorial/collections/",
                        "Oracle's authoritative tutorial on the Java Collections Framework. "
                                + "Covers interfaces (List, Set, Map, Queue, Deque), concrete "
                                + "implementations (ArrayList, LinkedList, HashMap, TreeMap, "
                                + "PriorityQueue), and algorithms (sort, shuffle, binarySearch). "
                                + "Essential reading for all Java developers.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.ALGORITHMS, ResourceCategory.JAVA),
                        List.of(ConceptArea.DATA_STRUCTURES, ConceptArea.LANGUAGE_FEATURES),
                        List.of("java", "collections", "list", "map", "set", "queue",
                                "arraylist", "hashmap", "treemap", "linkedlist",
                                "java-collections"),
                        "Oracle",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_SPECIFIC, now
                ),

                new LearningResource(
                        "open-dsa",
                        "OpenDSA — Interactive Data Structures & Algorithms",
                        "https://opendsa-server.cs.vt.edu/",
                        "Free, interactive e-textbook developed by Virginia Tech covering "
                                + "data structures (arrays, linked lists, stacks, queues, trees, "
                                + "heaps, hash tables, graphs) and algorithms with built-in "
                                + "exercises, visualizations, and auto-graded practice. "
                                + "Used in university CS courses worldwide.",
                        ResourceType.INTERACTIVE,
                        List.of(ResourceCategory.ALGORITHMS, ResourceCategory.GENERAL),
                        List.of(ConceptArea.DATA_STRUCTURES, ConceptArea.ALGORITHMS,
                                ConceptArea.COMPLEXITY_ANALYSIS),
                        List.of("data-structures", "algorithms", "interactive", "textbook",
                                "open-source", "university", "trees", "heaps", "graphs",
                                "linked-lists", "stacks", "queues"),
                        "Virginia Tech",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                new LearningResource(
                        "usfcs-ds-visualizer",
                        "USFCS Data Structures Visualizer",
                        "https://www.cs.usfca.edu/~galles/visualization/Algorithms.html",
                        "Step-by-step visualizations from the University of San Francisco "
                                + "covering BSTs, AVL trees, Red-Black trees, B-trees, hash "
                                + "tables, heaps, sorting algorithms, and graph traversals. "
                                + "Each animation shows exactly how data is inserted, deleted, "
                                + "and balanced — ideal for building mental models.",
                        ResourceType.INTERACTIVE,
                        List.of(ResourceCategory.ALGORITHMS, ResourceCategory.GENERAL),
                        List.of(ConceptArea.DATA_STRUCTURES, ConceptArea.ALGORITHMS),
                        List.of("visualizer", "bst", "avl-tree", "red-black-tree", "b-tree",
                                "hash-table", "heap", "tree-rotation", "animation",
                                "data-structures"),
                        "David Galles, USFCA",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.EVERGREEN,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                new LearningResource(
                        "neetcode-dsa",
                        "Neetcode.io — Structured DSA Roadmap",
                        "https://neetcode.io/roadmap",
                        "A well-organized roadmap of data structures and algorithms patterns, "
                                + "each backed by a curated LeetCode problem list and video "
                                + "explanations. Covers Arrays, Linked Lists, Trees, Heaps, "
                                + "Tries, Graphs, DP, Backtracking, Sliding Window, Two Pointers, "
                                + "and Bit Manipulation. Heavily used for interview preparation.",
                        ResourceType.TUTORIAL,
                        List.of(ResourceCategory.ALGORITHMS, ResourceCategory.GENERAL),
                        List.of(ConceptArea.DATA_STRUCTURES, ConceptArea.ALGORITHMS,
                                ConceptArea.INTERVIEW_PREP),
                        List.of("neetcode", "roadmap", "leetcode", "interview", "patterns",
                                "linked-list", "trees", "graphs", "dp", "backtracking",
                                "two-pointers", "sliding-window", "trie", "heap"),
                        "Neetcode",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                new LearningResource(
                        "geeksforgeeks-ds",
                        "GeeksforGeeks — Data Structures Portal",
                        "https://www.geeksforgeeks.org/data-structures/",
                        "Comprehensive reference portal covering all major data structures with "
                                + "explanations, implementations in C++, Java, Python, and "
                                + "practice problems. Topics include arrays, strings, linked "
                                + "lists, stacks, queues, trees (binary, BST, AVL, trie), "
                                + "heaps, hashing, graphs, and disjoint sets.",
                        ResourceType.TUTORIAL,
                        List.of(ResourceCategory.ALGORITHMS, ResourceCategory.GENERAL),
                        List.of(ConceptArea.DATA_STRUCTURES, ConceptArea.ALGORITHMS),
                        List.of("geeksforgeeks", "data-structures", "arrays", "linked-list",
                                "stack", "queue", "tree", "trie", "graph", "hashing",
                                "disjoint-set", "union-find"),
                        "GeeksforGeeks",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                new LearningResource(
                        "mit-6006-algorithms",
                        "MIT OpenCourseWare 6.006 — Intro to Algorithms",
                        "https://ocw.mit.edu/courses/6-006-introduction-to-algorithms-fall-2011/",
                        "MIT's foundational algorithms and data structures course. Lectures "
                                + "cover sequences and sets (arrays, lists, hash tables), trees "
                                + "(AVL, binary heaps), graphs (BFS, DFS, shortest paths, MST), "
                                + "and dynamic programming. Includes lecture notes, problem sets, "
                                + "exams with solutions — all freely available.",
                        ResourceType.VIDEO_COURSE,
                        List.of(ResourceCategory.ALGORITHMS, ResourceCategory.GENERAL),
                        List.of(ConceptArea.DATA_STRUCTURES, ConceptArea.ALGORITHMS,
                                ConceptArea.COMPLEXITY_ANALYSIS),
                        List.of("mit", "algorithms", "university", "avl", "hash-tables",
                                "graphs", "bfs", "dfs", "dynamic-programming", "mst",
                                "sorting", "heaps", "trees"),
                        "Erik Demaine, Srini Devadas",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.EVERGREEN,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                new LearningResource(
                        "python-ds-official",
                        "Python — Data Structures & Algorithms (Official Docs)",
                        "https://docs.python.org/3/tutorial/datastructures.html",
                        "Official Python tutorial covering built-in data structures: lists, "
                                + "tuples, sets, and dictionaries. Also documents the `collections` "
                                + "module (deque, namedtuple, Counter, defaultdict, OrderedDict) "
                                + "and the `heapq` and `bisect` modules. Essential reference for "
                                + "Python developers working with data structures.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.ALGORITHMS, ResourceCategory.PYTHON),
                        List.of(ConceptArea.DATA_STRUCTURES, ConceptArea.LANGUAGE_FEATURES),
                        List.of("python", "data-structures", "list", "dict", "set", "tuple",
                                "collections", "deque", "heapq", "counter", "defaultdict"),
                        "Python Software Foundation",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.PYTHON_SPECIFIC, now
                ),

                new LearningResource(
                        "ds-handbook",
                        "The Algorithm Design Manual — Skiena (2nd Ed.)",
                        "https://www.algorist.com/",
                        "One of the most practical books on algorithms and data structures, "
                                + "by Steven Skiena (Stony Brook University). Combines textbook "
                                + "theory with real-world war stories and an extensive catalog "
                                + "of algorithmic problems with data-structure solution patterns. "
                                + "Website includes slides and implementation notes.",
                        ResourceType.BOOK,
                        List.of(ResourceCategory.ALGORITHMS, ResourceCategory.GENERAL),
                        List.of(ConceptArea.DATA_STRUCTURES, ConceptArea.ALGORITHMS,
                                ConceptArea.COMPLEXITY_ANALYSIS),
                        List.of("skiena", "algorithm-design-manual", "practical", "book",
                                "war-stories", "problem-catalog", "data-structures"),
                        "Steven S. Skiena",
                        DifficultyLevel.ADVANCED,
                        ContentFreshness.EVERGREEN,
                        false, false, LanguageApplicability.UNIVERSAL, now
                )
        );
    }
}
