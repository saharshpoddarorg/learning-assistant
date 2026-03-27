package server.learningresources.vault.providers;

import server.learningresources.model.*;
import server.learningresources.vault.ResourceProvider;

import java.time.Instant;
import java.util.List;
import java.util.Set;

/**
 * Curated collection of influential software engineering books.
 *
 * <p>These are published books (print and ebook) by recognised authorities
 * in software craftsmanship. Each entry uses
 * {@link ContentFormat#PUBLISHED_BOOK} to distinguish it from web-native
 * resources.
 *
 * <h2>Authors represented</h2>
 * <ul>
 *   <li><strong>Robert C. Martin (Uncle Bob)</strong> — Clean Architecture, Clean Code, Clean Agile</li>
 *   <li><strong>Martin Fowler</strong> — Refactoring, PoEAA</li>
 *   <li><strong>Adam Tornhill</strong> — Your Code as a Crime Scene</li>
 *   <li><strong>John Ousterhout</strong> — A Philosophy of Software Design</li>
 *   <li><strong>Juval Löwy</strong> — Righting Software</li>
 *   <li><strong>Vladimir Khorikov</strong> — Unit Testing: Principles, Practices, and Patterns</li>
 * </ul>
 *
 * <p>NOTE: <em>Clean Code</em> (Robert C. Martin) already exists as a
 * community summary cheat-sheet in {@link EngineeringResources}
 * ({@code clean-code-summary}).
 *
 * @see EngineeringResources
 * @see ContentFormat#PUBLISHED_BOOK
 */
public final class SoftwareEngineeringBooksResources implements ResourceProvider
{

    @Override
    public List<LearningResource> resources()
    {
        final var now = Instant.now();
        return List.of(

                // ─── Robert C. Martin ────────────────────────────────

                new LearningResource(
                        "clean-architecture-book",
                        "Clean Architecture — Robert C. Martin",
                        "https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html",
                        "Uncle Bob's definitive guide to software architecture. Introduces "
                                + "the Dependency Rule, concentric-circle architecture, and how "
                                + "to separate policy from detail. Covers SOLID at the component "
                                + "and architectural level, package cohesion and coupling "
                                + "principles (REP, CCP, CRP, ADP, SDP, SAP), and boundaries "
                                + "between frameworks, UI, databases, and business rules.",
                        ResourceType.BOOK,
                        Set.of(ResourceCategory.SOFTWARE_ENGINEERING),
                        Set.of(ConceptArea.ARCHITECTURE,
                                ConceptArea.CLEAN_CODE,
                                ConceptArea.DESIGN_PATTERNS,
                                ConceptArea.LOW_LEVEL_DESIGN),
                        Set.of("clean-architecture", "uncle-bob", "robert-c-martin",
                                "dependency-rule", "solid", "hexagonal", "onion",
                                "ports-and-adapters", "component-principles",
                                "published-book", "ebook"),
                        "Robert C. Martin (Uncle Bob)",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.EVERGREEN,
                        false, false, LanguageApplicability.UNIVERSAL, now,
                        ContentFormat.PUBLISHED_BOOK,
                        Set.of(ResourceAuthor.ROBERT_C_MARTIN)
                ),

                // ─── Martin Fowler ──────────────────────────────────

                new LearningResource(
                        "refactoring-book",
                        "Refactoring: Improving the Design of Existing Code — Martin Fowler",
                        "https://refactoring.com/",
                        "The canonical reference on refactoring. Catalog of 60+ named "
                                + "refactorings (Extract Method, Move Field, Replace Conditional "
                                + "with Polymorphism, etc.) with motivation, mechanics, and "
                                + "examples. 2nd edition uses JavaScript; concepts apply to any "
                                + "language. Companion site includes the full refactoring catalog "
                                + "and articles on preparatory refactoring.",
                        ResourceType.BOOK,
                        Set.of(ResourceCategory.SOFTWARE_ENGINEERING),
                        Set.of(ConceptArea.CLEAN_CODE,
                                ConceptArea.DESIGN_PATTERNS,
                                ConceptArea.LOW_LEVEL_DESIGN),
                        Set.of("refactoring", "martin-fowler", "extract-method",
                                "code-smells", "technical-debt", "catalog",
                                "published-book", "ebook"),
                        "Martin Fowler",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.EVERGREEN,
                        false, false, LanguageApplicability.UNIVERSAL, now,
                        ContentFormat.PUBLISHED_BOOK,
                        Set.of(ResourceAuthor.MARTIN_FOWLER)
                ),

                new LearningResource(
                        "poeaa-book",
                        "Patterns of Enterprise Application Architecture — Martin Fowler",
                        "https://martinfowler.com/eaaCatalog/",
                        "Catalog of 50+ enterprise application patterns: domain logic "
                                + "(Transaction Script, Domain Model, Service Layer), data source "
                                + "(Active Record, Data Mapper, Repository), object-relational "
                                + "mapping (Identity Map, Unit of Work, Lazy Load), web "
                                + "presentation (MVC, Front Controller), distribution (Remote "
                                + "Facade, DTO), and offline concurrency (Optimistic/Pessimistic "
                                + "Lock). Online catalog freely available.",
                        ResourceType.BOOK,
                        Set.of(ResourceCategory.SOFTWARE_ENGINEERING,
                                ResourceCategory.JAVA),
                        Set.of(ConceptArea.ARCHITECTURE,
                                ConceptArea.DESIGN_PATTERNS,
                                ConceptArea.DATABASES,
                                ConceptArea.LOW_LEVEL_DESIGN),
                        Set.of("poeaa", "enterprise-patterns", "martin-fowler",
                                "active-record", "data-mapper", "repository",
                                "unit-of-work", "domain-model", "service-layer",
                                "published-book", "ebook"),
                        "Martin Fowler",
                        DifficultyLevel.ADVANCED,
                        ContentFreshness.EVERGREEN,
                        false, false, LanguageApplicability.JAVA_CENTRIC, now,
                        ContentFormat.PUBLISHED_BOOK,
                        Set.of(ResourceAuthor.MARTIN_FOWLER)
                ),

                // ─── Adam Tornhill ──────────────────────────────────

                new LearningResource(
                        "code-as-crime-scene-book",
                        "Your Code as a Crime Scene — Adam Tornhill",
                        "https://pragprog.com/titles/atcrime2/your-code-as-a-crime-scene-second-edition/",
                        "Applies forensic psychology and criminology techniques to software "
                                + "analysis. Uses version-control history to identify hotspots, "
                                + "detect coupling between files, uncover knowledge silos, and "
                                + "prioritise refactoring. Introduces CodeScene analysis "
                                + "techniques: change frequency maps, temporal coupling, code "
                                + "age, complexity trends. 2nd edition (2024).",
                        ResourceType.BOOK,
                        Set.of(ResourceCategory.SOFTWARE_ENGINEERING,
                                ResourceCategory.TOOLS),
                        Set.of(ConceptArea.CLEAN_CODE,
                                ConceptArea.ARCHITECTURE,
                                ConceptArea.VERSION_CONTROL),
                        Set.of("crime-scene", "adam-tornhill", "codescene", "hotspots",
                                "temporal-coupling", "code-forensics", "refactoring",
                                "complexity-trends", "published-book", "ebook"),
                        "Adam Tornhill",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.PERIODICALLY_UPDATED,
                        false, false, LanguageApplicability.UNIVERSAL, now,
                        ContentFormat.PUBLISHED_BOOK,
                        Set.of(ResourceAuthor.ADAM_TORNHILL)
                ),

                // ─── John Ousterhout ────────────────────────────────

                new LearningResource(
                        "philosophy-of-software-design-book",
                        "A Philosophy of Software Design — John Ousterhout",
                        "https://web.stanford.edu/~ouster/cgi-bin/book.php",
                        "Stanford professor's opinionated guide to managing complexity in "
                                + "software. Core themes: deep vs. shallow modules, information "
                                + "hiding, tactical vs. strategic programming, when comments are "
                                + "vital, why clean-code advice on small methods can backfire, "
                                + "designing classes for minimal interface / maximal "
                                + "functionality. 2nd edition (2021).",
                        ResourceType.BOOK,
                        Set.of(ResourceCategory.SOFTWARE_ENGINEERING),
                        Set.of(ConceptArea.CLEAN_CODE,
                                ConceptArea.ARCHITECTURE,
                                ConceptArea.LOW_LEVEL_DESIGN,
                                ConceptArea.DESIGN_PATTERNS),
                        Set.of("philosophy-of-software-design", "john-ousterhout",
                                "complexity", "deep-modules", "information-hiding",
                                "strategic-programming", "published-book", "ebook"),
                        "John Ousterhout",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.EVERGREEN,
                        false, false, LanguageApplicability.UNIVERSAL, now,
                        ContentFormat.PUBLISHED_BOOK,
                        Set.of(ResourceAuthor.JOHN_OUSTERHOUT)
                ),

                // ─── Robert C. Martin — additional titles ───────────────────

                new LearningResource(
                        "clean-code-book",
                        "Clean Code: A Handbook of Agile Software Craftsmanship — Robert C. Martin",
                        "https://www.oreilly.com/library/view/clean-code-a/9780136083238/",
                        "The foundational clean code handbook. Covers meaningful names, "
                                + "functions (small, single-purpose), comments (when and how), "
                                + "formatting, objects vs. data structures, error handling, unit "
                                + "testing, classes (SRP, cohesion), systems, emergence, and "
                                + "concurrency. Part II is a series of increasingly complex "
                                + "cleanup case studies. Part III is a catalog of code smells "
                                + "and heuristics. 2008 classic.",
                        ResourceType.BOOK,
                        Set.of(ResourceCategory.SOFTWARE_ENGINEERING),
                        Set.of(ConceptArea.CLEAN_CODE,
                                ConceptArea.DESIGN_PATTERNS,
                                ConceptArea.TESTING),
                        Set.of("clean-code", "uncle-bob", "robert-c-martin",
                                "code-smells", "solid", "naming", "functions",
                                "formatting", "error-handling", "tdd",
                                "published-book", "ebook"),
                        "Robert C. Martin (Uncle Bob)",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.EVERGREEN,
                        false, false, LanguageApplicability.UNIVERSAL, now,
                        ContentFormat.PUBLISHED_BOOK,
                        Set.of(ResourceAuthor.ROBERT_C_MARTIN)
                ),

                new LearningResource(
                        "clean-agile-book",
                        "Clean Agile: Back to Basics — Robert C. Martin",
                        "https://www.oreilly.com/library/view/clean-agile-back/9780135782002/",
                        "Uncle Bob reintroduces Agile values and principles for a new "
                                + "generation. Strips away misunderstandings accumulated since "
                                + "the Agile Manifesto was signed. Covers the origins of Agile, "
                                + "proper Scrum practice, business-facing practices (small "
                                + "releases, acceptance tests), technical practices (TDD, "
                                + "refactoring, simple design, pair programming), and the "
                                + "role of craftsmanship. 2019.",
                        ResourceType.BOOK,
                        Set.of(ResourceCategory.SOFTWARE_ENGINEERING),
                        Set.of(ConceptArea.CLEAN_CODE,
                                ConceptArea.TESTING,
                                ConceptArea.ARCHITECTURE),
                        Set.of("clean-agile", "uncle-bob", "robert-c-martin",
                                "agile", "scrum", "tdd", "pair-programming",
                                "craftsmanship", "published-book", "ebook"),
                        "Robert C. Martin (Uncle Bob)",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.EVERGREEN,
                        false, false, LanguageApplicability.UNIVERSAL, now,
                        ContentFormat.PUBLISHED_BOOK,
                        Set.of(ResourceAuthor.ROBERT_C_MARTIN)
                ),

                // ─── Juval Löwy ───────────────────────────────────────────

                new LearningResource(
                        "righting-software-book",
                        "Righting Software — Juval Löwy",
                        "https://rightingsoftware.org/",
                        "Presents a structured, engineering-based approach to system "
                                + "and project design. Core innovation: volatility-based "
                                + "decomposition — decompose systems into services based on "
                                + "axes of change, not functional areas. Covers system design "
                                + "(service boundaries, callchains, composition), project "
                                + "design (duration/cost/risk calculation), and execution "
                                + "strategies. Applicable regardless of technology, platform, "
                                + "or company size. 2020.",
                        ResourceType.BOOK,
                        Set.of(ResourceCategory.SOFTWARE_ENGINEERING),
                        Set.of(ConceptArea.ARCHITECTURE,
                                ConceptArea.SYSTEM_DESIGN,
                                ConceptArea.LOW_LEVEL_DESIGN,
                                ConceptArea.DESIGN_PATTERNS),
                        Set.of("righting-software", "juval-lowy",
                                "volatility-based-decomposition", "service-decomposition",
                                "system-design", "project-design", "idesign",
                                "published-book", "ebook"),
                        "Juval Löwy",
                        DifficultyLevel.ADVANCED,
                        ContentFreshness.EVERGREEN,
                        false, false, LanguageApplicability.UNIVERSAL, now,
                        ContentFormat.PUBLISHED_BOOK,
                        Set.of(ResourceAuthor.JUVAL_LOWY)
                ),

                // ─── Vladimir Khorikov ──────────────────────────────────

                new LearningResource(
                        "unit-testing-book",
                        "Unit Testing: Principles, Practices, and Patterns — Vladimir Khorikov",
                        "https://www.oreilly.com/library/view/unit-testing-principles/9781617296277/",
                        "A pragmatic guide to writing effective unit tests. Teaches how "
                                + "to assess test quality, avoid anti-patterns (brittle tests, "
                                + "testing implementation details), and focus on behaviour "
                                + "verification. Covers the test pyramid, London vs. Classical "
                                + "schools, mocks vs. stubs, integration testing boundaries, "
                                + "and refactoring tests alongside production code. Manning, "
                                + "2020.",
                        ResourceType.BOOK,
                        Set.of(ResourceCategory.SOFTWARE_ENGINEERING,
                                ResourceCategory.TESTING),
                        Set.of(ConceptArea.TESTING,
                                ConceptArea.CLEAN_CODE,
                                ConceptArea.DESIGN_PATTERNS),
                        Set.of("unit-testing", "vladimir-khorikov", "test-pyramid",
                                "mocks", "stubs", "tdd", "integration-testing",
                                "test-anti-patterns", "behaviour-verification",
                                "published-book", "ebook"),
                        "Vladimir Khorikov",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.EVERGREEN,
                        false, false, LanguageApplicability.UNIVERSAL, now,
                        ContentFormat.PUBLISHED_BOOK,
                        Set.of(ResourceAuthor.VLADIMIR_KHORIKOV)
                )
        );
    }
}
