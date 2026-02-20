package server.learningresources.vault.providers;

import server.learningresources.model.LearningResource;
import server.learningresources.model.ResourceCategory;
import server.learningresources.model.ResourceType;
import server.learningresources.vault.ResourceProvider;

import java.time.Instant;
import java.util.List;

/**
 * Curated DevOps, cloud, and developer tooling resources — containerization,
 * orchestration, version control, and CI/CD.
 *
 * <p>Includes: Docker Getting Started, Kubernetes Docs, Pro Git, GitHub Skills.
 */
public final class DevOpsResources implements ResourceProvider {

    @Override
    public List<LearningResource> resources() {
        final var now = Instant.now();
        return List.of(

                new LearningResource(
                        "docker-docs-get-started",
                        "Docker — Getting Started Guide",
                        "https://docs.docker.com/get-started/",
                        "Official Docker tutorial: containers, images, Dockerfiles, "
                                + "volumes, networks, and Docker Compose. Start here for "
                                + "containerization fundamentals.",
                        ResourceType.TUTORIAL,
                        List.of(ResourceCategory.DEVOPS, ResourceCategory.CLOUD),
                        List.of("docker", "containers", "dockerfile", "compose"),
                        "Docker Inc.",
                        "beginner",
                        true, now
                ),

                new LearningResource(
                        "kubernetes-docs",
                        "Kubernetes Documentation",
                        "https://kubernetes.io/docs/home/",
                        "Official Kubernetes documentation — concepts, tutorials, tasks, "
                                + "and reference. Covers pods, services, deployments, config maps, "
                                + "networking, and cluster administration.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.DEVOPS, ResourceCategory.CLOUD),
                        List.of("kubernetes", "k8s", "orchestration", "pods", "services"),
                        "CNCF / Kubernetes",
                        "intermediate",
                        true, now
                ),

                new LearningResource(
                        "pro-git-book",
                        "Pro Git (2nd Edition) — Free Online Book",
                        "https://git-scm.com/book/en/v2",
                        "The complete Git book, freely available online. Covers basics, "
                                + "branching, merging, rebasing, distributed workflows, internals, "
                                + "and advanced topics like reflog and bisect.",
                        ResourceType.BOOK,
                        List.of(ResourceCategory.TOOLS),
                        List.of("git", "version-control", "branching", "merging"),
                        "Scott Chacon & Ben Straub",
                        "beginner",
                        true, now
                ),

                new LearningResource(
                        "github-skills",
                        "GitHub Skills — Interactive Courses",
                        "https://skills.github.com/",
                        "Hands-on GitHub courses: intro to GitHub, GitHub Pages, "
                                + "GitHub Actions, pull requests, code review, and security. "
                                + "Learn by doing in real repositories.",
                        ResourceType.INTERACTIVE,
                        List.of(ResourceCategory.TOOLS, ResourceCategory.DEVOPS),
                        List.of("github", "github-actions", "ci-cd", "pull-requests"),
                        "GitHub",
                        "beginner",
                        true, now
                )
        );
    }
}
