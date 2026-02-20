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
 * Curated DevOps, CI/CD, and tooling resources — official docs, books,
 * community references.
 */
public final class DevOpsResources implements ResourceProvider {

    @Override
    public List<LearningResource> resources() {
        final var now = Instant.now();
        return List.of(

                new LearningResource(
                        "docker-docs-get-started",
                        "Docker Docs — Get Started",
                        "https://docs.docker.com/get-started/",
                        "Official Docker getting-started guide. Covers images, containers, "
                                + "Dockerfiles, multi-container apps with Compose, volumes, "
                                + "networking, and best practices.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.DEVOPS),
                        List.of(ConceptArea.CONTAINERS, ConceptArea.INFRASTRUCTURE,
                                ConceptArea.GETTING_STARTED),
                        List.of("official", "docker", "containers", "dockerfile", "compose",
                                "volumes", "networking"),
                        "Docker Inc.",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                new LearningResource(
                        "kubernetes-docs",
                        "Kubernetes Documentation",
                        "https://kubernetes.io/docs/home/",
                        "Official Kubernetes documentation. Concepts, tutorials, tasks, and "
                                + "API reference for container orchestration — pods, services, "
                                + "deployments, ConfigMaps, secrets, RBAC, and networking.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.DEVOPS),
                        List.of(ConceptArea.CONTAINERS, ConceptArea.INFRASTRUCTURE,
                                ConceptArea.NETWORKING, ConceptArea.DISTRIBUTED_SYSTEMS),
                        List.of("official", "kubernetes", "k8s", "pods", "services",
                                "deployments", "orchestration"),
                        "CNCF / Kubernetes Community",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                new LearningResource(
                        "pro-git-book",
                        "Pro Git Book (2nd Edition)",
                        "https://git-scm.com/book/en/v2",
                        "The complete Git book — free, official, and comprehensive. Covers "
                                + "basics, branching, distributed workflows, internals, hooks, "
                                + "and advanced topics like rebase, cherry-pick, and submodules.",
                        ResourceType.BOOK,
                        List.of(ResourceCategory.DEVOPS),
                        List.of(ConceptArea.VERSION_CONTROL, ConceptArea.GETTING_STARTED),
                        List.of("official", "git", "version-control", "branching", "rebase",
                                "workflows", "internals"),
                        "Scott Chacon & Ben Straub",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.EVERGREEN,
                        true, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                new LearningResource(
                        "github-skills",
                        "GitHub Skills — Interactive Courses",
                        "https://skills.github.com/",
                        "Official hands-on courses from GitHub. Learn Git, GitHub Actions, "
                                + "pull requests, code review, Pages, and more through interactive "
                                + "exercises in real repositories.",
                        ResourceType.INTERACTIVE,
                        List.of(ResourceCategory.DEVOPS),
                        List.of(ConceptArea.VERSION_CONTROL, ConceptArea.CI_CD,
                                ConceptArea.GETTING_STARTED),
                        List.of("official", "github", "github-actions", "ci-cd", "pull-requests",
                                "code-review", "interactive"),
                        "GitHub",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                new LearningResource(
                        "github-actions-docs",
                        "GitHub Actions Documentation",
                        "https://docs.github.com/en/actions",
                        "Official reference for GitHub Actions — workflows, triggers, jobs, "
                                + "runners, reusable workflows, composite actions, secrets, "
                                + "environments, and marketplace actions.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.DEVOPS),
                        List.of(ConceptArea.CI_CD, ConceptArea.INFRASTRUCTURE,
                                ConceptArea.BUILD_TOOLS),
                        List.of("official", "github-actions", "ci-cd", "workflows", "automation",
                                "runners", "yaml"),
                        "GitHub",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                new LearningResource(
                        "gradle-user-guide",
                        "Gradle User Manual",
                        "https://docs.gradle.org/current/userguide/userguide.html",
                        "Official Gradle documentation. Build scripts, plugins, dependency "
                                + "management, task configuration, multi-project builds, and "
                                + "performance optimization.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.DEVOPS, ResourceCategory.JAVA),
                        List.of(ConceptArea.BUILD_TOOLS, ConceptArea.CI_CD),
                        List.of("official", "gradle", "build-tool", "dependency-management",
                                "plugins", "groovy", "kotlin-dsl"),
                        "Gradle Inc.",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.MULTI_LANGUAGE, now
                )
        );
    }
}
