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
 * Curated Python learning resources — official docs, community tutorials, references.
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
                        "Official Python tutorial from python.org. Walks through the language "
                                + "from basics to advanced topics: data structures, modules, I/O, "
                                + "errors, classes, and the standard library.",
                        ResourceType.TUTORIAL,
                        List.of(ResourceCategory.PYTHON),
                        List.of(ConceptArea.LANGUAGE_BASICS, ConceptArea.OOP,
                                ConceptArea.DATA_STRUCTURES, ConceptArea.GETTING_STARTED),
                        List.of("official", "language-basics", "data-structures", "classes",
                                "modules", "standard-library"),
                        "Python Software Foundation",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.PYTHON_SPECIFIC, now
                ),

                new LearningResource(
                        "python-docs-reference",
                        "Python Language Reference (Official)",
                        "https://docs.python.org/3/reference/",
                        "Official language reference — formal syntax, data model, execution model, "
                                + "import system, and expressions. The authoritative source for "
                                + "Python semantics.",
                        ResourceType.API_REFERENCE,
                        List.of(ResourceCategory.PYTHON),
                        List.of(ConceptArea.LANGUAGE_FEATURES, ConceptArea.OOP,
                                ConceptArea.MEMORY_MANAGEMENT),
                        List.of("official", "reference", "syntax", "data-model", "import-system"),
                        "Python Software Foundation",
                        DifficultyLevel.ADVANCED,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.PYTHON_SPECIFIC, now
                ),

                new LearningResource(
                        "python-pep-index",
                        "PEP Index — Python Enhancement Proposals",
                        "https://peps.python.org/",
                        "Complete index of Python Enhancement Proposals. Includes PEP 8 (style), "
                                + "PEP 20 (Zen of Python), PEP 484 (type hints), and the full "
                                + "language evolution history.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.PYTHON),
                        List.of(ConceptArea.CLEAN_CODE, ConceptArea.LANGUAGE_FEATURES,
                                ConceptArea.API_DESIGN),
                        List.of("official", "pep", "style-guide", "type-hints", "proposals"),
                        "Python Software Foundation",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.PYTHON_SPECIFIC, now
                ),

                new LearningResource(
                        "real-python",
                        "Real Python — Tutorials & Articles",
                        "https://realpython.com/",
                        "High-quality Python tutorials, articles, and video courses. Covers "
                                + "web development, data science, testing, async, packaging, "
                                + "and best practices.",
                        ResourceType.TUTORIAL,
                        List.of(ResourceCategory.PYTHON),
                        List.of(ConceptArea.LANGUAGE_BASICS, ConceptArea.TESTING,
                                ConceptArea.CONCURRENCY, ConceptArea.API_DESIGN),
                        List.of("tutorials", "web", "data-science", "testing", "async",
                                "best-practices", "packaging"),
                        "Real Python / Dan Bader",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.PYTHON_SPECIFIC, now
                )
        );
    }
}
