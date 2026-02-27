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
 * Cross-cutting, language-agnostic resources — roadmaps, testing, meta-learning,
 * and curated collections.
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
                        "Influential article on modern testing strategy. Introduces the 'testing "
                                + "trophy' model — static, unit, integration, and e2e tests — as an "
                                + "alternative to the testing pyramid.",
                        ResourceType.BLOG,
                        List.of(ResourceCategory.SOFTWARE_ENGINEERING, ResourceCategory.TESTING),
                        List.of(ConceptArea.TESTING, ConceptArea.CLEAN_CODE),
                        List.of("testing", "testing-trophy", "integration-tests", "unit-tests",
                                "e2e", "testing-strategy"),
                        "Kent C. Dodds",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.EVERGREEN,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                new LearningResource(
                        "developer-roadmaps",
                        "Developer Roadmaps (roadmap.sh)",
                        "https://roadmap.sh/",
                        "Community-driven, interactive roadmaps for every developer role: "
                                + "frontend, backend, DevOps, full-stack, Android, AI/ML, "
                                + "and more. Great for orientation and goal setting.",
                        ResourceType.INTERACTIVE,
                        List.of(ResourceCategory.SOFTWARE_ENGINEERING),
                        List.of(ConceptArea.GETTING_STARTED, ConceptArea.CAREER_DEVELOPMENT),
                        List.of("roadmap", "career", "learning-path", "frontend", "backend",
                                "devops", "full-stack", "interactive"),
                        "Kamran Ahmed / roadmap.sh",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                new LearningResource(
                        "free-programming-books",
                        "Free Programming Books (GitHub)",
                        "https://github.com/EbookFoundation/free-programming-books",
                        "Huge curated list of free programming books, courses, podcasts, and "
                                + "resources organized by language and topic. A meta-resource for "
                                + "finding learning materials.",
                        ResourceType.REPOSITORY,
                        List.of(ResourceCategory.SOFTWARE_ENGINEERING),
                        List.of(ConceptArea.GETTING_STARTED, ConceptArea.CAREER_DEVELOPMENT),
                        List.of("free", "books", "courses", "curated-list", "meta-resource",
                                "open-source"),
                        "Ebook Foundation / Community",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                new LearningResource(
                        "teach-yourself-cs",
                        "Teach Yourself Computer Science",
                        "https://teachyourselfcs.com/",
                        "Opinionated guide to self-studying CS — recommends the best book and "
                                + "video lecture for each of 9 subjects: programming, architecture, "
                                + "algorithms, math, OS, networking, databases, languages, and "
                                + "distributed systems.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.GENERAL, ResourceCategory.SOFTWARE_ENGINEERING),
                        List.of(ConceptArea.GETTING_STARTED, ConceptArea.ALGORITHMS,
                                ConceptArea.OPERATING_SYSTEMS, ConceptArea.NETWORKING,
                                ConceptArea.DATABASES, ConceptArea.DISTRIBUTED_SYSTEMS),
                        List.of("self-study", "cs-curriculum", "books", "video-lectures",
                                "computer-science", "learning-path"),
                        "Bradfield School of Computer Science",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.EVERGREEN,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                new LearningResource(
                        "cs50x",
                        "CS50x — Harvard's Introduction to Computer Science",
                        "https://cs50.harvard.edu/x/2026/",
                        "Harvard University's renowned introduction to the intellectual enterprises "
                                + "of computer science. Eleven weeks of lectures, problem sets, and "
                                + "sections covering C, Python, SQL, HTML, CSS, JavaScript, and "
                                + "algorithms. Free via OpenCourseWare; verified certificate available "
                                + "via edX. Suitable for complete beginners — two thirds of enrolled "
                                + "students have never programmed before.",
                        ResourceType.VIDEO_COURSE,
                        List.of(ResourceCategory.GENERAL, ResourceCategory.ALGORITHMS,
                                ResourceCategory.SOFTWARE_ENGINEERING),
                        List.of(ConceptArea.GETTING_STARTED, ConceptArea.ALGORITHMS,
                                ConceptArea.DATA_STRUCTURES, ConceptArea.LANGUAGE_BASICS),
                        List.of("cs50", "harvard", "computer-science", "beginner", "c",
                                "python", "sql", "algorithms", "data-structures", "free-course",
                                "david-malan", "opencourseware", "edx"),
                        "David J. Malan, Harvard University",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.UNIVERSAL, now
                )
        );
    }
}
