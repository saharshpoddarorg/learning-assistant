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
 * Curated resources for major application frameworks across languages.
 *
 * <p>Covers the full spectrum of modern framework ecosystems:
 * <ul>
 *   <li>Java: Spring Framework (reference), Hibernate / JPA</li>
 *   <li>JavaScript/TypeScript: React, Angular, Vue.js, Next.js, Express.js, Node.js</li>
 *   <li>Python: Django, Flask</li>
 * </ul>
 *
 * <p>Resources span three difficulty tiers:
 * {@link DifficultyLevel#BEGINNER} (getting-started guides),
 * {@link DifficultyLevel#INTERMEDIATE} (official reference docs),
 * {@link DifficultyLevel#ADVANCED} (internals, advanced patterns).
 *
 * @see JavaResources
 * @see WebResources
 * @see PythonResources
 */
public final class FrameworksResources implements ResourceProvider {

    @Override
    public List<LearningResource> resources() {
        final var now = Instant.now();
        return List.of(

                // ─── Java Frameworks ────────────────────────────────────────

                new LearningResource(
                        "spring-framework-reference",
                        "Spring Framework Reference Documentation",
                        "https://docs.spring.io/spring-framework/reference/",
                        "Official Spring Framework reference — the most widely-used Java framework. "
                                + "Covers IoC container, dependency injection, AOP, data access, "
                                + "transaction management, MVC, WebFlux, testing integration, "
                                + "and Spring Expression Language (SpEL).",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.JAVA, ResourceCategory.WEB),
                        List.of(ConceptArea.ARCHITECTURE, ConceptArea.API_DESIGN,
                                ConceptArea.DESIGN_PATTERNS, ConceptArea.TESTING),
                        List.of("official", "spring", "spring-framework", "dependency-injection",
                                "ioc", "aop", "mvc", "webflux", "transaction"),
                        "VMware / Broadcom",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_SPECIFIC, now
                ),

                new LearningResource(
                        "spring-boot-reference",
                        "Spring Boot Reference Documentation",
                        "https://docs.spring.io/spring-boot/reference/",
                        "Official Spring Boot reference — opinionated framework for building "
                                + "production-ready Java applications. Auto-configuration, embedded "
                                + "servers, actuator endpoints, externalized configuration, profiles, "
                                + "security, data access, messaging, and deployment.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.JAVA, ResourceCategory.WEB),
                        List.of(ConceptArea.ARCHITECTURE, ConceptArea.API_DESIGN,
                                ConceptArea.GETTING_STARTED, ConceptArea.CI_CD),
                        List.of("official", "spring-boot", "auto-configuration", "actuator",
                                "embedded-server", "profiles", "microservices"),
                        "VMware / Broadcom",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_SPECIFIC, now
                ),

                new LearningResource(
                        "hibernate-orm-docs",
                        "Hibernate ORM User Guide",
                        "https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html",
                        "Official Hibernate ORM documentation — the dominant JPA implementation. "
                                + "Covers entity mapping, associations, inheritance, HQL/JPQL, "
                                + "Criteria API, caching, batching, and performance tuning.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.JAVA, ResourceCategory.DATABASE),
                        List.of(ConceptArea.DATABASES, ConceptArea.DESIGN_PATTERNS,
                                ConceptArea.ARCHITECTURE),
                        List.of("official", "hibernate", "jpa", "orm", "entity-mapping",
                                "hql", "jpql", "caching", "persistence"),
                        "Red Hat / Hibernate Team",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_SPECIFIC, now
                ),

                // ─── JavaScript / TypeScript Frameworks ─────────────────────

                new LearningResource(
                        "react-docs",
                        "React Documentation",
                        "https://react.dev/",
                        "Official React documentation — the most popular frontend library. "
                                + "Learn React fundamentals, hooks (useState, useEffect, useContext), "
                                + "component patterns, state management, server components, and "
                                + "escape hatches. Includes interactive code examples.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.JAVASCRIPT, ResourceCategory.WEB),
                        List.of(ConceptArea.LANGUAGE_FEATURES, ConceptArea.ARCHITECTURE,
                                ConceptArea.DESIGN_PATTERNS, ConceptArea.GETTING_STARTED),
                        List.of("official", "react", "hooks", "components", "state-management",
                                "jsx", "server-components", "frontend"),
                        "Meta (Facebook)",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.WEB_SPECIFIC, now
                ),

                new LearningResource(
                        "nextjs-docs",
                        "Next.js Documentation",
                        "https://nextjs.org/docs",
                        "Official Next.js documentation — the React framework for production. "
                                + "App Router, server-side rendering (SSR), static site generation "
                                + "(SSG), API routes, middleware, image optimization, and deployment.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.JAVASCRIPT, ResourceCategory.WEB),
                        List.of(ConceptArea.ARCHITECTURE, ConceptArea.API_DESIGN,
                                ConceptArea.GETTING_STARTED),
                        List.of("official", "nextjs", "next.js", "ssr", "ssg", "react",
                                "app-router", "fullstack", "vercel"),
                        "Vercel",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.WEB_SPECIFIC, now
                ),

                new LearningResource(
                        "angular-docs",
                        "Angular Documentation",
                        "https://angular.dev/",
                        "Official Angular documentation — Google's full-featured TypeScript "
                                + "framework. Components, templates, dependency injection, routing, "
                                + "forms, HTTP client, RxJS observables, testing, and CLI tooling.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.JAVASCRIPT, ResourceCategory.WEB),
                        List.of(ConceptArea.ARCHITECTURE, ConceptArea.DESIGN_PATTERNS,
                                ConceptArea.TESTING),
                        List.of("official", "angular", "typescript", "components", "rxjs",
                                "dependency-injection", "cli", "routing"),
                        "Google",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.WEB_SPECIFIC, now
                ),

                new LearningResource(
                        "vuejs-docs",
                        "Vue.js Documentation",
                        "https://vuejs.org/guide/introduction",
                        "Official Vue.js guide — the progressive JavaScript framework. "
                                + "Reactivity fundamentals, components, composables, Composition API, "
                                + "Options API, routing (Vue Router), state management (Pinia), "
                                + "and TypeScript support.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.JAVASCRIPT, ResourceCategory.WEB),
                        List.of(ConceptArea.LANGUAGE_FEATURES, ConceptArea.ARCHITECTURE,
                                ConceptArea.GETTING_STARTED),
                        List.of("official", "vue", "vuejs", "composition-api", "reactivity",
                                "pinia", "vue-router", "frontend"),
                        "Evan You / Vue.js Community",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.WEB_SPECIFIC, now
                ),

                new LearningResource(
                        "nodejs-docs",
                        "Node.js Documentation",
                        "https://nodejs.org/docs/latest/api/",
                        "Official Node.js API documentation — server-side JavaScript runtime. "
                                + "Covers modules (fs, http, path, crypto, stream, child_process), "
                                + "event loop, async patterns, ESM/CJS modules, and worker threads.",
                        ResourceType.API_REFERENCE,
                        List.of(ResourceCategory.JAVASCRIPT, ResourceCategory.WEB),
                        List.of(ConceptArea.LANGUAGE_FEATURES, ConceptArea.CONCURRENCY,
                                ConceptArea.NETWORKING),
                        List.of("official", "nodejs", "node", "server-side", "event-loop",
                                "streams", "modules", "runtime"),
                        "OpenJS Foundation",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.WEB_SPECIFIC, now
                ),

                new LearningResource(
                        "expressjs-docs",
                        "Express.js — Fast, Minimal Node.js Framework",
                        "https://expressjs.com/",
                        "Official Express.js documentation — the most popular Node.js web "
                                + "framework. Routing, middleware, request/response handling, "
                                + "template engines, error handling, and API best practices.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.JAVASCRIPT, ResourceCategory.WEB),
                        List.of(ConceptArea.API_DESIGN, ConceptArea.ARCHITECTURE,
                                ConceptArea.GETTING_STARTED),
                        List.of("official", "express", "expressjs", "node", "middleware",
                                "routing", "rest-api", "backend"),
                        "OpenJS Foundation",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.WEB_SPECIFIC, now
                ),

                // ─── Python Frameworks ──────────────────────────────────────

                new LearningResource(
                        "django-docs",
                        "Django Documentation",
                        "https://docs.djangoproject.com/en/stable/",
                        "Official Django documentation — Python's batteries-included web "
                                + "framework. ORM, admin interface, URL routing, templates, forms, "
                                + "authentication, middleware, and security best practices.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.PYTHON, ResourceCategory.WEB),
                        List.of(ConceptArea.ARCHITECTURE, ConceptArea.API_DESIGN,
                                ConceptArea.DATABASES, ConceptArea.WEB_SECURITY),
                        List.of("official", "django", "python", "orm", "admin", "templates",
                                "authentication", "web-framework"),
                        "Django Software Foundation",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.PYTHON_SPECIFIC, now
                ),

                new LearningResource(
                        "flask-docs",
                        "Flask Documentation",
                        "https://flask.palletsprojects.com/en/stable/",
                        "Official Flask documentation — Python's lightweight micro-framework. "
                                + "Routing, templates (Jinja2), request handling, blueprints, "
                                + "extensions, testing, and deployment patterns.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.PYTHON, ResourceCategory.WEB),
                        List.of(ConceptArea.API_DESIGN, ConceptArea.ARCHITECTURE,
                                ConceptArea.GETTING_STARTED),
                        List.of("official", "flask", "python", "micro-framework", "jinja2",
                                "blueprints", "rest-api", "lightweight"),
                        "Pallets Projects",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.PYTHON_SPECIFIC, now
                )
        );
    }
}
