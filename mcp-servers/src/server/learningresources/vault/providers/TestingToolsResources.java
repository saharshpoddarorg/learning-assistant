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
 * Curated resources for testing frameworks and tools across languages.
 *
 * <p>Covers the major testing ecosystem beyond JUnit 5 (which lives in
 * {@link JavaResources}):
 * <ul>
 *   <li>Java: Mockito, Testcontainers</li>
 *   <li>JavaScript: Cypress</li>
 *   <li>Python: pytest</li>
 *   <li>Cross-platform: Selenium WebDriver</li>
 * </ul>
 *
 * <p>Resources span multiple tiers:
 * {@link DifficultyLevel#BEGINNER} (getting-started with test tools),
 * {@link DifficultyLevel#INTERMEDIATE} (official reference docs),
 * {@link DifficultyLevel#ADVANCED} (advanced patterns, custom extensions).
 *
 * @see JavaResources
 * @see GeneralResources
 * @see EngineeringResources
 */
public final class TestingToolsResources implements ResourceProvider {

    @Override
    public List<LearningResource> resources() {
        final var now = Instant.now();
        return List.of(

                // ─── Java Testing Tools ─────────────────────────────────────

                new LearningResource(
                        "mockito-docs",
                        "Mockito — Tasty Mocking Framework for Java",
                        "https://site.mockito.org/",
                        "Official Mockito documentation — the most popular Java mocking "
                                + "framework. Covers mock creation, stubbing, verification, "
                                + "argument matchers, BDD-style API (given/when/then), annotations, "
                                + "and integration with JUnit 5.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.TESTING, ResourceCategory.JAVA),
                        List.of(ConceptArea.TESTING, ConceptArea.CLEAN_CODE,
                                ConceptArea.DESIGN_PATTERNS),
                        List.of("official", "mockito", "mocking", "java", "junit",
                                "stubbing", "verification", "bdd"),
                        "Mockito Contributors",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_SPECIFIC, now
                ),

                new LearningResource(
                        "testcontainers-docs",
                        "Testcontainers — Integration Testing with Real Services",
                        "https://testcontainers.com/guides/",
                        "Official Testcontainers guides — run real databases, message brokers, "
                                + "and services in Docker containers during integration tests. "
                                + "Covers PostgreSQL, MySQL, Kafka, Redis, Elasticsearch modules, "
                                + "and custom container definitions. Java, Node.js, Go, and .NET SDKs.",
                        ResourceType.TUTORIAL,
                        List.of(ResourceCategory.TESTING, ResourceCategory.JAVA,
                                ResourceCategory.DEVOPS),
                        List.of(ConceptArea.TESTING, ConceptArea.CONTAINERS,
                                ConceptArea.DATABASES),
                        List.of("official", "testcontainers", "integration-testing", "docker",
                                "postgresql", "kafka", "redis", "java", "containers"),
                        "AtomicJar / Docker",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                // ─── Browser / E2E Testing ──────────────────────────────────

                new LearningResource(
                        "selenium-docs",
                        "Selenium WebDriver Documentation",
                        "https://www.selenium.dev/documentation/",
                        "Official Selenium documentation — the industry-standard browser "
                                + "automation framework. WebDriver API, locators, waits, page "
                                + "object model, Selenium Grid, and cross-browser testing with "
                                + "Java, Python, JavaScript, C#, and Ruby bindings.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.TESTING, ResourceCategory.WEB),
                        List.of(ConceptArea.TESTING, ConceptArea.DESIGN_PATTERNS),
                        List.of("official", "selenium", "webdriver", "browser-testing",
                                "e2e", "page-object", "grid", "cross-browser"),
                        "Selenium Project",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                new LearningResource(
                        "cypress-docs",
                        "Cypress Documentation",
                        "https://docs.cypress.io/",
                        "Official Cypress documentation — modern end-to-end testing for web "
                                + "applications. Time-travel debugging, automatic waiting, real-time "
                                + "reloads, network stubbing, component testing, and Cypress Cloud "
                                + "for CI integration.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.TESTING, ResourceCategory.JAVASCRIPT,
                                ResourceCategory.WEB),
                        List.of(ConceptArea.TESTING, ConceptArea.CI_CD),
                        List.of("official", "cypress", "e2e", "end-to-end", "component-testing",
                                "javascript", "frontend-testing", "time-travel"),
                        "Cypress.io",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.WEB_SPECIFIC, now
                ),

                // ─── Python Testing ─────────────────────────────────────────

                new LearningResource(
                        "pytest-docs",
                        "pytest Documentation",
                        "https://docs.pytest.org/en/stable/",
                        "Official pytest documentation — the most popular Python testing "
                                + "framework. Fixtures, parametrize, markers, plugins, assertion "
                                + "introspection, and configuration. Scales from simple unit tests "
                                + "to complex integration suites.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.TESTING, ResourceCategory.PYTHON),
                        List.of(ConceptArea.TESTING, ConceptArea.CLEAN_CODE),
                        List.of("official", "pytest", "python", "fixtures", "parametrize",
                                "markers", "plugins", "unit-testing"),
                        "pytest Contributors",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.PYTHON_SPECIFIC, now
                )
        );
    }
}
