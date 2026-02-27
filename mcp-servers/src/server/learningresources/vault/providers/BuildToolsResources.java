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
 * Curated resources for build automation tools across major ecosystems.
 *
 * <p>Covers the full spectrum of build tooling:
 * <ul>
 *   <li>Maven: lifecycle, POM, dependency management, multi-module</li>
 *   <li>Gradle: Kotlin DSL, dependency management, multi-project builds</li>
 *   <li>Make: GNU Makefiles for C/C++ and scripting</li>
 *   <li>Bazel: Google's multi-language build system</li>
 *   <li>npm/yarn/pnpm: JavaScript package management</li>
 * </ul>
 *
 * <p>Resources span three difficulty tiers:
 * {@link DifficultyLevel#BEGINNER} (quickstarts, first builds),
 * {@link DifficultyLevel#INTERMEDIATE} (lifecycle, dependency management),
 * {@link DifficultyLevel#ADVANCED} (multi-module, plugins, remote caching).
 *
 * @see VcsResources
 * @see DevOpsResources
 */
public final class BuildToolsResources implements ResourceProvider {

    @Override
    public List<LearningResource> resources() {
        final var now = Instant.now();
        return List.of(

                // ─── Maven ─────────────────────────────────────────────────────────

                new LearningResource(
                        "maven-getting-started",
                        "Maven Getting Started Guide",
                        "https://maven.apache.org/guides/getting-started/",
                        "Official Apache Maven tutorial: setting up Maven, creating your first "
                                + "project with archetypes, POM structure (groupId, artifactId, version), "
                                + "compile → test → package → install lifecycle, dependency management, "
                                + "plugin configuration, resource filtering, and multi-module builds.",
                        ResourceType.TUTORIAL,
                        List.of(ResourceCategory.DEVOPS, ResourceCategory.JAVA),
                        List.of(ConceptArea.BUILD_TOOLS, ConceptArea.GETTING_STARTED),
                        List.of("official", "maven", "pom", "build", "dependency-management",
                                "lifecycle", "archetype", "compile", "package", "install",
                                "multi-module", "java"),
                        "Apache Maven Project",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_SPECIFIC, now
                ),

                new LearningResource(
                        "maven-in-five-minutes",
                        "Maven in 5 Minutes",
                        "https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html",
                        "The fastest Maven introduction: install, create a project, build, and test "
                                + "in five commands. Perfect starting point for developers new to Maven "
                                + "who want to hit the ground running before reading deeper documentation.",
                        ResourceType.TUTORIAL,
                        List.of(ResourceCategory.DEVOPS, ResourceCategory.JAVA),
                        List.of(ConceptArea.BUILD_TOOLS, ConceptArea.GETTING_STARTED),
                        List.of("official", "maven", "quickstart", "pom", "mvn",
                                "beginner", "5-minutes", "java"),
                        "Apache Maven Project",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_SPECIFIC, now
                ),

                new LearningResource(
                        "maven-pom-reference",
                        "Maven POM Reference",
                        "https://maven.apache.org/pom.html",
                        "Complete reference for Maven's Project Object Model (pom.xml). "
                                + "Covers all POM elements: coordinates, dependencies, dependency "
                                + "management, plugins, pluginManagement, build lifecycle, profiles, "
                                + "repositories, distributionManagement, properties, reporting, "
                                + "parent POM inheritance, and BOM (Bill of Materials) usage.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.DEVOPS, ResourceCategory.JAVA),
                        List.of(ConceptArea.BUILD_TOOLS),
                        List.of("official", "maven", "pom.xml", "pom", "dependency-management",
                                "bom", "parent-pom", "plugins", "profiles", "properties", "java"),
                        "Apache Maven Project",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_SPECIFIC, now
                ),

                new LearningResource(
                        "maven-build-lifecycle",
                        "Maven Build Lifecycle",
                        "https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html",
                        "Deep explanation of Maven's three built-in lifecycles (default, clean, site) "
                                + "and their phases: validate → compile → test → package → verify → "
                                + "install → deploy. How goals bind to lifecycle phases, how to "
                                + "configure plugin execution in specific phases, and how the "
                                + "lifecycle connects to CI/CD pipelines.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.DEVOPS, ResourceCategory.JAVA),
                        List.of(ConceptArea.BUILD_TOOLS, ConceptArea.CI_CD),
                        List.of("official", "maven", "lifecycle", "phases", "goals",
                                "compile", "test", "package", "install", "deploy", "java"),
                        "Apache Maven Project",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_SPECIFIC, now
                ),

                new LearningResource(
                        "maven-dependency-mechanism",
                        "Maven Dependency Mechanism",
                        "https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html",
                        "How Maven resolves dependencies: transitive dependencies, dependency "
                                + "scopes (compile, test, runtime, provided, system, import), "
                                + "exclusions, optional dependencies, version conflict resolution, "
                                + "dependency trees (mvn dependency:tree), and BOM imports for "
                                + "centralized version management across multi-module projects.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.DEVOPS, ResourceCategory.JAVA),
                        List.of(ConceptArea.BUILD_TOOLS),
                        List.of("official", "maven", "dependency", "transitive", "scope",
                                "bom", "exclusion", "version-conflict", "classpath", "java"),
                        "Apache Maven Project",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_SPECIFIC, now
                ),

                // ─── Gradle ────────────────────────────────────────────────────────

                new LearningResource(
                        "gradle-kotlin-dsl-primer",
                        "Gradle Kotlin DSL Primer",
                        "https://docs.gradle.org/current/userguide/kotlin_dsl.html",
                        "Official guide to using Kotlin DSL (build.gradle.kts) instead of Groovy "
                                + "for Gradle build scripts. Covers IDE support, type-safe accessors, "
                                + "extension functions, precompiled script plugins, and migration "
                                + "from Groovy DSL to Kotlin DSL. Modern default for new projects.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.DEVOPS, ResourceCategory.JAVA),
                        List.of(ConceptArea.BUILD_TOOLS),
                        List.of("official", "gradle", "kotlin-dsl", "build.gradle.kts",
                                "type-safe", "ide-support", "groovy", "migration"),
                        "Gradle Inc.",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_SPECIFIC, now
                ),

                new LearningResource(
                        "gradle-dependency-management",
                        "Gradle Dependency Management",
                        "https://docs.gradle.org/current/userguide/dependency_management.html",
                        "Official Gradle dependency management guide: declaring dependencies, "
                                + "configurations, resolution strategies, version catalogs (libs.versions.toml), "
                                + "dependency verification, and Bill of Materials (BOM) support. "
                                + "Covers multi-project dependency sharing and dependency constraints.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.DEVOPS, ResourceCategory.JAVA),
                        List.of(ConceptArea.BUILD_TOOLS),
                        List.of("official", "gradle", "dependency-management", "version-catalog",
                                "libs.versions.toml", "bom", "configurations", "resolution"),
                        "Gradle Inc.",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_SPECIFIC, now
                ),

                new LearningResource(
                        "gradle-multi-project-builds",
                        "Gradle Multi-Project Build Guide",
                        "https://docs.gradle.org/current/userguide/multi_project_builds.html",
                        "How to structure and configure multi-project (multi-module) Gradle builds: "
                                + "settings.gradle.kts, project dependencies, subproject configuration, "
                                + "convention plugins, composite builds, and sharing build logic. "
                                + "Essential for microservice mono-repos and large Java/Kotlin projects.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.DEVOPS, ResourceCategory.JAVA),
                        List.of(ConceptArea.BUILD_TOOLS, ConceptArea.ARCHITECTURE),
                        List.of("official", "gradle", "multi-project", "multi-module",
                                "subproject", "composite", "mono-repo", "java"),
                        "Gradle Inc.",
                        DifficultyLevel.ADVANCED,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_SPECIFIC, now
                ),

                // ─── Make ──────────────────────────────────────────────────────────

                new LearningResource(
                        "makefile-tutorial",
                        "Makefile Tutorial",
                        "https://makefiletutorial.com/",
                        "Comprehensive tutorial on GNU Make and Makefiles — the original build "
                                + "automation tool. Covers variables, automatic variables ($@, $<, $^), "
                                + "implicit rules, pattern rules, phony targets, functions, and practical "
                                + "examples. Essential for understanding legacy C/C++ projects and "
                                + "many DevOps/infrastructure automation scripts.",
                        ResourceType.TUTORIAL,
                        List.of(ResourceCategory.DEVOPS, ResourceCategory.TOOLS),
                        List.of(ConceptArea.BUILD_TOOLS, ConceptArea.INFRASTRUCTURE),
                        List.of("make", "makefile", "gnu-make", "build-automation",
                                "c", "cpp", "targets", "variables", "devops"),
                        "Chase Lambert",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                // ─── Bazel ─────────────────────────────────────────────────────────

                new LearningResource(
                        "bazel-docs",
                        "Bazel Documentation",
                        "https://bazel.build/docs",
                        "Official documentation for Bazel — Google's open-source, multi-language "
                                + "build and test tool. Covers Starlark build language, BUILD files, "
                                + "Bazel rules (Java, C++, Python, Go), remote execution, caching, "
                                + "and incremental builds. Used at massive scale by Google, Stripe, "
                                + "and other large engineering organizations.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.DEVOPS, ResourceCategory.TOOLS),
                        List.of(ConceptArea.BUILD_TOOLS, ConceptArea.INFRASTRUCTURE),
                        List.of("official", "bazel", "starlark", "build", "monorepo",
                                "remote-execution", "caching", "google", "multi-language"),
                        "Google / Bazel Community",
                        DifficultyLevel.ADVANCED,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                // ─── npm / JavaScript Package Management ───────────────────────────

                new LearningResource(
                        "npm-docs",
                        "npm Documentation",
                        "https://docs.npmjs.com/",
                        "Official npm documentation: installing and managing packages, package.json "
                                + "structure (scripts, dependencies, devDependencies, peerDependencies), "
                                + "npm workspaces, npx, versioning, publishing packages, and npm CI "
                                + "for reproducible installs. Also covers yarn and pnpm alternatives.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.DEVOPS, ResourceCategory.JAVASCRIPT),
                        List.of(ConceptArea.BUILD_TOOLS, ConceptArea.INFRASTRUCTURE),
                        List.of("official", "npm", "package-manager", "package.json",
                                "node", "workspaces", "javascript", "typescript",
                                "yarn", "pnpm", "scripts"),
                        "npm, Inc.",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.WEB_SPECIFIC, now
                )
        );
    }
}
