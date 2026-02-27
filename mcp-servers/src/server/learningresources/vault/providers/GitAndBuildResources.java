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
 * Curated resources for Git / VCS workflows, Maven, Gradle, and other build tools.
 *
 * <p>Covers the full stack of version control and build automation knowledge:
 * <ul>
 *   <li>Git internals, branching strategies, commit conventions</li>
 *   <li>VCS workflows: GitHub Flow, GitFlow, Trunk-Based Development</li>
 *   <li>Maven: lifecycle, POM, dependency management</li>
 *   <li>Gradle: multi-project builds, dependency management, Kotlin DSL</li>
 *   <li>Other build tools: Make, Bazel, npm, Ant</li>
 * </ul>
 *
 * <p>Resources are divided into three tiers matching the 3-tier learning model:
 * newbie (BEGINNER), amateur (INTERMEDIATE), pro (ADVANCED/EXPERT).
 */
public final class GitAndBuildResources implements ResourceProvider {

    @Override
    public List<LearningResource> resources() {
        final var now = Instant.now();
        return List.of(

                // ─── Git / VCS ─────────────────────────────────────────────────────

                new LearningResource(
                        "learngitbranching-interactive",
                        "Learn Git Branching — Visual & Interactive",
                        "https://learngitbranching.js.org/",
                        "The most visual and interactive way to learn Git on the web. "
                                + "Levels cover commits, branching, rebasing, cherry-pick, "
                                + "detached HEAD, remote tracking, and advanced Git operations. "
                                + "Ideal for beginners up to intermediate users who want to "
                                + "understand git history manipulation visually.",
                        ResourceType.INTERACTIVE,
                        List.of(ResourceCategory.DEVOPS, ResourceCategory.TOOLS),
                        List.of(ConceptArea.VERSION_CONTROL, ConceptArea.GETTING_STARTED),
                        List.of("git", "branching", "rebase", "merge", "cherry-pick",
                                "interactive", "visual", "beginner", "intermediate"),
                        "Peter Cottle",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                new LearningResource(
                        "git-scm-reference",
                        "Git Reference Manual",
                        "https://git-scm.com/docs",
                        "Official, authoritative reference for every Git command. "
                                + "Covers git init, clone, add, commit, push, pull, fetch, "
                                + "merge, rebase, stash, cherry-pick, bisect, reflog, hooks, "
                                + "config, and all plumbing commands. The definitive source "
                                + "for exact flag meanings and behavior.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.DEVOPS, ResourceCategory.TOOLS),
                        List.of(ConceptArea.VERSION_CONTROL),
                        List.of("official", "git", "reference", "commands", "flags",
                                "internals", "hooks", "plumbing", "porcelain"),
                        "The Git Project",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                new LearningResource(
                        "atlassian-git-tutorials",
                        "Atlassian Git Tutorials",
                        "https://www.atlassian.com/git/tutorials",
                        "Comprehensive, well-illustrated Git tutorial series from Atlassian. "
                                + "Covers beginner to advanced: getting started, saving changes, "
                                + "inspecting repos, undoing changes, rewriting history, syncing, "
                                + "using branches, comparing workflows (feature branch, gitflow, "
                                + "forking, trunk-based). Excellent diagrams for branching strategies.",
                        ResourceType.TUTORIAL,
                        List.of(ResourceCategory.DEVOPS, ResourceCategory.TOOLS),
                        List.of(ConceptArea.VERSION_CONTROL, ConceptArea.CI_CD),
                        List.of("git", "tutorial", "branching", "workflow", "gitflow",
                                "feature-branch", "forking", "rebase", "merge",
                                "atlassian", "trunk-based"),
                        "Atlassian",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                new LearningResource(
                        "gitflow-branching-model",
                        "A Successful Git Branching Model (GitFlow)",
                        "https://nvie.com/posts/a-successful-git-branching-model/",
                        "The original GitFlow article by Vincent Driessen. Describes the "
                                + "master/develop/feature/release/hotfix branching strategy, "
                                + "with merge rules and naming conventions. Widely adopted in "
                                + "versioned software teams. Includes 2020 author reflection on "
                                + "when to use GitFlow vs simpler workflows (GitHub Flow).",
                        ResourceType.BLOG,
                        List.of(ResourceCategory.DEVOPS, ResourceCategory.SOFTWARE_ENGINEERING),
                        List.of(ConceptArea.VERSION_CONTROL, ConceptArea.CI_CD),
                        List.of("git", "gitflow", "branching-strategy", "feature-branch",
                                "hotfix", "release-branch", "workflow", "team"),
                        "Vincent Driessen",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.EVERGREEN,
                        false, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                new LearningResource(
                        "github-flow-guide",
                        "GitHub Flow",
                        "https://docs.github.com/en/get-started/using-github/github-flow",
                        "GitHub's simplified branching workflow for continuous delivery. "
                                + "Single main branch + short-lived feature branches + pull requests. "
                                + "Contrasts with GitFlow: better for web apps and teams doing "
                                + "continuous deployment. Covers branch creation, commits, pull "
                                + "requests, review, and merge to main.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.DEVOPS),
                        List.of(ConceptArea.VERSION_CONTROL, ConceptArea.CI_CD),
                        List.of("official", "github", "github-flow", "pull-requests",
                                "continuous-delivery", "workflow", "branching"),
                        "GitHub",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                new LearningResource(
                        "conventional-commits",
                        "Conventional Commits Specification",
                        "https://www.conventionalcommits.org/en/v1.0.0/",
                        "A lightweight specification for structuring commit messages: "
                                + "feat/fix/docs/style/refactor/test/chore types, optional scope, "
                                + "BREAKING CHANGE footer, and semantic versioning alignment. "
                                + "Enables auto-generated CHANGELOGs and automated versioning. "
                                + "Widely adopted standard across open-source and enterprise projects.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.DEVOPS, ResourceCategory.SOFTWARE_ENGINEERING),
                        List.of(ConceptArea.VERSION_CONTROL, ConceptArea.CLEAN_CODE),
                        List.of("conventional-commits", "commit-message", "semver",
                                "changelog", "git", "commit-convention", "feat", "fix",
                                "breaking-change", "automated-versioning"),
                        "Conventional Commits Community",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.EVERGREEN,
                        false, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                new LearningResource(
                        "trunk-based-development",
                        "Trunk-Based Development",
                        "https://trunkbaseddevelopment.com/",
                        "Reference site for trunk-based development (TBD) — the practice of "
                                + "developers collaborating on code in a single branch called trunk, "
                                + "using short-lived feature branches (< 1 day). Covers feature flags, "
                                + "branch by abstraction, and how TBD enables continuous integration. "
                                + "Explains when TBD beats GitFlow for high-frequency deployment teams.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.DEVOPS),
                        List.of(ConceptArea.VERSION_CONTROL, ConceptArea.CI_CD),
                        List.of("trunk-based", "tbd", "continuous-integration", "feature-flags",
                                "branching-strategy", "git", "short-lived-branches"),
                        "Paul Hammant",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                new LearningResource(
                        "git-internals-deep-dive",
                        "Git Internals (Pro Git — Chapter 10)",
                        "https://git-scm.com/book/en/v2/Git-Internals-Plumbing-and-Porcelain",
                        "Chapter 10 of Pro Git: the git object model. How blobs, trees, commits, "
                                + "and tags are stored as SHA-1 objects in .git/objects. How refs, "
                                + "packfiles, reflog, and the index work. How git really thinks "
                                + "about history. Essential reading for understanding git at a "
                                + "deep level — resolving weird states, writing git hooks, or "
                                + "building tools on top of git.",
                        ResourceType.BOOK,
                        List.of(ResourceCategory.DEVOPS, ResourceCategory.TOOLS),
                        List.of(ConceptArea.VERSION_CONTROL),
                        List.of("official", "git", "internals", "object-model", "plumbing",
                                "sha1", "blobs", "trees", "packfiles", "hooks", "advanced"),
                        "Scott Chacon & Ben Straub",
                        DifficultyLevel.ADVANCED,
                        ContentFreshness.EVERGREEN,
                        true, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                new LearningResource(
                        "semantic-versioning",
                        "Semantic Versioning (SemVer) 2.0.0",
                        "https://semver.org/",
                        "The formal SemVer specification: MAJOR.MINOR.PATCH versioning rules, "
                                + "pre-release identifiers, build metadata, and precedence. "
                                + "Widely used by npm, Maven, Gradle, and GitHub Releases. "
                                + "Pairs directly with Conventional Commits for automated "
                                + "version bumping in CI/CD pipelines.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.DEVOPS, ResourceCategory.SOFTWARE_ENGINEERING),
                        List.of(ConceptArea.VERSION_CONTROL, ConceptArea.BUILD_TOOLS),
                        List.of("semver", "semantic-versioning", "versioning", "major",
                                "minor", "patch", "release", "changelog", "npm", "maven"),
                        "Tom Preston-Werner",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.EVERGREEN,
                        false, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

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

                // ─── Other Build Tools ─────────────────────────────────────────────

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
