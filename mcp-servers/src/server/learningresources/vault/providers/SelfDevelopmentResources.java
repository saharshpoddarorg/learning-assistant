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
 * Resources for personal development, self-improvement, and non-professional learning.
 *
 * <p>Covers curated GitHub repositories, ebook collections, professional programming
 * guides, and self-improvement resources that go beyond technical skills — including
 * communication, habits, mindset, financial literacy, and productivity.
 */
public final class SelfDevelopmentResources implements ResourceProvider {

    @Override
    public List<LearningResource> resources() {
        final var now = Instant.now();
        return List.of(

                // ─── Curated Learning Repositories ──────────────────────────

                new LearningResource(
                        "build-your-own-x",
                        "Build Your Own X (codecrafters-io)",
                        "https://github.com/codecrafters-io/build-your-own-x",
                        "Compilation of well-written, step-by-step guides for re-creating "
                                + "favorite technologies from scratch — databases, Docker, Git, "
                                + "compilers, neural networks, OS, and 30+ more categories. "
                                + "482k+ stars. 'What I cannot create, I do not understand.'",
                        ResourceType.REPOSITORY,
                        List.of(ResourceCategory.SOFTWARE_ENGINEERING, ResourceCategory.GENERAL),
                        List.of(ConceptArea.GETTING_STARTED, ConceptArea.ARCHITECTURE,
                                ConceptArea.ALGORITHMS),
                        List.of("build-your-own", "hands-on", "learn-by-doing", "projects",
                                "compilers", "databases", "docker", "git", "os"),
                        "codecrafters-io / Community",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                new LearningResource(
                        "project-based-learning",
                        "Project Based Learning",
                        "https://github.com/practical-tutorials/project-based-learning",
                        "Curated list of project-based tutorials organized by programming "
                                + "language — Python, JavaScript, Java, C/C++, Go, Rust, and more. "
                                + "261k+ stars. Learn by building real projects.",
                        ResourceType.REPOSITORY,
                        List.of(ResourceCategory.SOFTWARE_ENGINEERING, ResourceCategory.GENERAL),
                        List.of(ConceptArea.GETTING_STARTED, ConceptArea.LANGUAGE_BASICS),
                        List.of("project-based", "tutorials", "hands-on", "learn-by-doing",
                                "multi-language"),
                        "practical-tutorials / Community",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                new LearningResource(
                        "professional-programming",
                        "Professional Programming — charlax",
                        "https://github.com/charlax/professional-programming",
                        "Curated collection of full-stack resources for programmers — must-read "
                                + "books, articles, and 60+ topics from algorithms to writing. "
                                + "Covers attitude, career growth, code reviews, system design, "
                                + "PKM, and communication. 47k+ stars.",
                        ResourceType.REPOSITORY,
                        List.of(ResourceCategory.SOFTWARE_ENGINEERING,
                                ResourceCategory.PERSONAL_DEVELOPMENT),
                        List.of(ConceptArea.CAREER_DEVELOPMENT, ConceptArea.CLEAN_CODE,
                                ConceptArea.SELF_IMPROVEMENT, ConceptArea.COMMUNICATION_SKILLS),
                        List.of("professional", "career", "must-read", "books", "articles",
                                "full-stack", "attitude", "mindset"),
                        "charlax / Community",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                new LearningResource(
                        "coding-interview-university",
                        "Coding Interview University",
                        "https://github.com/jwasham/coding-interview-university",
                        "A complete CS study plan to become a software engineer at top companies. "
                                + "Covers data structures, algorithms, system design, networking, "
                                + "OS, and more — a multi-month structured learning path. 312k+ stars.",
                        ResourceType.REPOSITORY,
                        List.of(ResourceCategory.ALGORITHMS, ResourceCategory.SOFTWARE_ENGINEERING),
                        List.of(ConceptArea.INTERVIEW_PREP, ConceptArea.ALGORITHMS,
                                ConceptArea.DATA_STRUCTURES, ConceptArea.SYSTEM_DESIGN),
                        List.of("interview-prep", "study-plan", "cs-fundamentals",
                                "algorithms", "data-structures", "system-design"),
                        "John Washam / Community",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                new LearningResource(
                        "ossu-computer-science",
                        "Open Source Society University — CS",
                        "https://github.com/ossu/computer-science",
                        "Path to a free self-taught education in Computer Science using "
                                + "online courses from top universities. Structured like a "
                                + "4-year degree: intro CS, core, advanced, and final project. "
                                + "180k+ stars.",
                        ResourceType.REPOSITORY,
                        List.of(ResourceCategory.GENERAL, ResourceCategory.SOFTWARE_ENGINEERING),
                        List.of(ConceptArea.GETTING_STARTED, ConceptArea.ALGORITHMS,
                                ConceptArea.OPERATING_SYSTEMS, ConceptArea.NETWORKING),
                        List.of("self-taught", "cs-degree", "free-education", "curriculum",
                                "computer-science", "university"),
                        "Open Source Society University / Community",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                new LearningResource(
                        "awesome-lists",
                        "Awesome Lists — sindresorhus",
                        "https://github.com/sindresorhus/awesome",
                        "The master list of awesome lists — 447k+ stars. Curated collections "
                                + "covering every technology, language, framework, and topic "
                                + "imaginable: platforms, programming languages, CS, big data, "
                                + "security, gaming, learn, books, and more.",
                        ResourceType.REPOSITORY,
                        List.of(ResourceCategory.GENERAL),
                        List.of(ConceptArea.GETTING_STARTED, ConceptArea.CAREER_DEVELOPMENT),
                        List.of("awesome", "curated-list", "meta-resource", "comprehensive",
                                "community-driven"),
                        "Sindre Sorhus / Community",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                new LearningResource(
                        "book-of-secret-knowledge",
                        "The Book of Secret Knowledge",
                        "https://github.com/trimstray/the-book-of-secret-knowledge",
                        "Collection of inspiring lists, manuals, cheatsheets, blogs, hacks, "
                                + "one-liners, CLI/web tools — covering Linux, networking, "
                                + "security, DevOps, containers, and more. 211k+ stars.",
                        ResourceType.REPOSITORY,
                        List.of(ResourceCategory.GENERAL, ResourceCategory.DEVOPS,
                                ResourceCategory.SECURITY),
                        List.of(ConceptArea.GETTING_STARTED, ConceptArea.WEB_SECURITY,
                                ConceptArea.CONTAINERS),
                        List.of("cheatsheets", "cli-tools", "security", "devops", "linux",
                                "networking", "hacking"),
                        "trimstray / Community",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                // ─── Self-Improvement & Personal Growth ─────────────────────

                new LearningResource(
                        "mind-expanding-books",
                        "Mind Expanding Books",
                        "https://github.com/hackerkid/Mind-Expanding-Books",
                        "Curated list of mind-expanding books across categories: startups, "
                                + "philosophy, psychology, economics, autobiography, history, "
                                + "science, and self-help. Community-curated with 11k+ stars.",
                        ResourceType.REPOSITORY,
                        List.of(ResourceCategory.PERSONAL_DEVELOPMENT),
                        List.of(ConceptArea.SELF_IMPROVEMENT, ConceptArea.COMMUNICATION_SKILLS),
                        List.of("books", "self-help", "psychology", "philosophy",
                                "mind-expanding", "reading-list"),
                        "Vishnu S. Iyengar / Community",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.EVERGREEN,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                new LearningResource(
                        "every-programmer-should-know",
                        "Every Programmer Should Know",
                        "https://github.com/mtdvio/every-programmer-should-know",
                        "Collection of (mostly) technical things every software developer "
                                + "should know about — algorithms, data structures, distributed "
                                + "systems, security, SEO, architecture, and engineering culture. "
                                + "86k+ stars.",
                        ResourceType.REPOSITORY,
                        List.of(ResourceCategory.SOFTWARE_ENGINEERING, ResourceCategory.GENERAL),
                        List.of(ConceptArea.CAREER_DEVELOPMENT, ConceptArea.ARCHITECTURE,
                                ConceptArea.DISTRIBUTED_SYSTEMS),
                        List.of("must-know", "fundamentals", "engineering-culture",
                                "best-practices", "curated"),
                        "mtdvio / Community",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.EVERGREEN,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                // ─── Ebooks & Free Book Collections ─────────────────────────

                new LearningResource(
                        "free-science-books",
                        "Free Science Books (GitHub)",
                        "https://github.com/EbookFoundation/free-programming-books/blob/main/books/free-programming-books-subjects.md",
                        "Subject-organized index of free programming books from the Ebook "
                                + "Foundation mega-collection — covering algorithms, AI, databases, "
                                + "compilers, networking, OS, and 50+ CS subjects.",
                        ResourceType.REPOSITORY,
                        List.of(ResourceCategory.GENERAL, ResourceCategory.SOFTWARE_ENGINEERING),
                        List.of(ConceptArea.GETTING_STARTED, ConceptArea.ALGORITHMS,
                                ConceptArea.DATABASES),
                        List.of("free-books", "ebooks", "subjects", "cs-books",
                                "programming-books"),
                        "Ebook Foundation / Community",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                new LearningResource(
                        "free-courses-collection",
                        "Free Programming Courses (GitHub)",
                        "https://github.com/EbookFoundation/free-programming-books/blob/main/courses/free-courses-en.md",
                        "Curated list of free online programming courses in English — from "
                                + "the Ebook Foundation's mega-collection. Covers algorithms, "
                                + "databases, cloud, security, web, mobile, and more.",
                        ResourceType.REPOSITORY,
                        List.of(ResourceCategory.GENERAL),
                        List.of(ConceptArea.GETTING_STARTED, ConceptArea.CAREER_DEVELOPMENT),
                        List.of("free-courses", "online-learning", "mooc", "self-paced",
                                "programming-courses"),
                        "Ebook Foundation / Community",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                // ─── Productivity & Deep Work ───────────────────────────────

                new LearningResource(
                        "awesome-productivity",
                        "Awesome Productivity",
                        "https://github.com/jyguyomarch/awesome-productivity",
                        "Curated list of delightful productivity resources — tools, articles, "
                                + "books and techniques covering time management, focus, habits, "
                                + "and personal effectiveness.",
                        ResourceType.REPOSITORY,
                        List.of(ResourceCategory.PRODUCTIVITY,
                                ResourceCategory.PERSONAL_DEVELOPMENT),
                        List.of(ConceptArea.PRODUCTIVITY_HABITS, ConceptArea.SELF_IMPROVEMENT),
                        List.of("productivity", "time-management", "habits", "focus",
                                "deep-work", "tools"),
                        "jyguyomarch / Community",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                // ─── Writing & Communication ────────────────────────────────

                new LearningResource(
                        "awesome-speaking",
                        "Awesome Speaking",
                        "https://github.com/matteofigus/awesome-speaking",
                        "Curated collection of resources about public speaking — talks, "
                                + "articles, and books on presentation skills, storytelling, "
                                + "and conference speaking for developers.",
                        ResourceType.REPOSITORY,
                        List.of(ResourceCategory.PERSONAL_DEVELOPMENT),
                        List.of(ConceptArea.COMMUNICATION_SKILLS, ConceptArea.CAREER_DEVELOPMENT),
                        List.of("public-speaking", "presentations", "storytelling",
                                "conferences", "soft-skills"),
                        "matteofigus / Community",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.EVERGREEN,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                // ─── Financial Literacy ─────────────────────────────────────

                new LearningResource(
                        "awesome-personal-finance",
                        "awesome-personal-finance (GitHub)",
                        "https://github.com/ashishb/awesome-personal-finance",
                        "Curated list of personal finance resources — budgeting tools, "
                                + "investment guides, tax resources, and financial literacy "
                                + "articles for tech professionals.",
                        ResourceType.REPOSITORY,
                        List.of(ResourceCategory.PERSONAL_DEVELOPMENT),
                        List.of(ConceptArea.FINANCIAL_LITERACY),
                        List.of("personal-finance", "budgeting", "investing", "tax",
                                "financial-literacy", "money"),
                        "Ashish Bansal / Community",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.UNIVERSAL, now
                )
        );
    }
}
