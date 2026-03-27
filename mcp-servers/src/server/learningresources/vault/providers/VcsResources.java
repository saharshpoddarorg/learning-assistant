package server.learningresources.vault.providers;

import server.learningresources.model.ConceptArea;
import server.learningresources.model.ContentFormat;
import server.learningresources.model.ContentFreshness;
import server.learningresources.model.DifficultyLevel;
import server.learningresources.model.LanguageApplicability;
import server.learningresources.model.LearningResource;
import server.learningresources.model.ResourceAuthor;
import server.learningresources.model.ResourceCategory;
import server.learningresources.model.ResourceType;
import server.learningresources.vault.ResourceProvider;

import java.time.Instant;
import java.util.List;
import java.util.Set;

/**
 * Curated resources for Version Control Systems and Git workflows.
 *
 * <p>Covers the full spectrum of VCS knowledge:
 * <ul>
 *   <li>Git fundamentals and interactive learning</li>
 *   <li>Branching strategies: GitFlow, GitHub Flow, Trunk-Based Development</li>
 *   <li>Commit conventions: Conventional Commits specification</li>
 *   <li>Versioning: Semantic Versioning (SemVer 2.0.0)</li>
 *   <li>Git internals: object model, plumbing commands, hooks</li>
 * </ul>
 *
 * <p>Resources span three difficulty tiers:
 * {@link DifficultyLevel#BEGINNER} (interactive tools, workflow guides),
 * {@link DifficultyLevel#INTERMEDIATE} (branching strategies, references),
 * {@link DifficultyLevel#ADVANCED} (internals, plumbing, hooks).
 *
 * @see BuildToolsResources
 * @see DevOpsResources
 */
public final class VcsResources implements ResourceProvider {

    @Override
    public List<LearningResource> resources() {
        final var now = Instant.now();
        return List.of(

                // ─── Git Foundations ───────────────────────────────────────────────

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
                        Set.of(ResourceCategory.DEVOPS, ResourceCategory.TOOLS),
                        Set.of(ConceptArea.VERSION_CONTROL, ConceptArea.GETTING_STARTED),
                        Set.of("git", "branching", "rebase", "merge", "cherry-pick",
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
                        Set.of(ResourceCategory.DEVOPS, ResourceCategory.TOOLS),
                        Set.of(ConceptArea.VERSION_CONTROL),
                        Set.of("official", "git", "reference", "commands", "flags",
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
                        Set.of(ResourceCategory.DEVOPS, ResourceCategory.TOOLS),
                        Set.of(ConceptArea.VERSION_CONTROL, ConceptArea.CI_CD),
                        Set.of("git", "tutorial", "branching", "workflow", "gitflow",
                                "feature-branch", "forking", "rebase", "merge",
                                "atlassian", "trunk-based"),
                        "Atlassian",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                // ─── Branching Strategies ──────────────────────────────────────────

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
                        Set.of(ResourceCategory.DEVOPS, ResourceCategory.SOFTWARE_ENGINEERING),
                        Set.of(ConceptArea.VERSION_CONTROL, ConceptArea.CI_CD),
                        Set.of("git", "gitflow", "branching-strategy", "feature-branch",
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
                        Set.of(ResourceCategory.DEVOPS),
                        Set.of(ConceptArea.VERSION_CONTROL, ConceptArea.CI_CD),
                        Set.of("official", "github", "github-flow", "pull-requests",
                                "continuous-delivery", "workflow", "branching"),
                        "GitHub",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.MULTI_LANGUAGE, now
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
                        Set.of(ResourceCategory.DEVOPS),
                        Set.of(ConceptArea.VERSION_CONTROL, ConceptArea.CI_CD),
                        Set.of("trunk-based", "tbd", "continuous-integration", "feature-flags",
                                "branching-strategy", "git", "short-lived-branches"),
                        "Paul Hammant",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                // ─── Commit Conventions & Versioning ──────────────────────────────

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
                        Set.of(ResourceCategory.DEVOPS, ResourceCategory.SOFTWARE_ENGINEERING),
                        Set.of(ConceptArea.VERSION_CONTROL, ConceptArea.CLEAN_CODE),
                        Set.of("conventional-commits", "commit-message", "semver",
                                "changelog", "git", "commit-convention", "feat", "fix",
                                "breaking-change", "automated-versioning"),
                        "Conventional Commits Community",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.EVERGREEN,
                        false, true, LanguageApplicability.MULTI_LANGUAGE, now
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
                        Set.of(ResourceCategory.DEVOPS, ResourceCategory.SOFTWARE_ENGINEERING),
                        Set.of(ConceptArea.VERSION_CONTROL, ConceptArea.BUILD_TOOLS),
                        Set.of("semver", "semantic-versioning", "versioning", "major",
                                "minor", "patch", "release", "changelog", "npm", "maven"),
                        "Tom Preston-Werner",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.EVERGREEN,
                        false, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                // ─── Git Internals (Advanced) ──────────────────────────────────────

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
                        Set.of(ResourceCategory.DEVOPS, ResourceCategory.TOOLS),
                        Set.of(ConceptArea.VERSION_CONTROL),
                        Set.of("official", "git", "internals", "object-model", "plumbing",
                                "sha1", "blobs", "trees", "packfiles", "hooks", "advanced"),
                        "Scott Chacon & Ben Straub",
                        DifficultyLevel.ADVANCED,
                        ContentFreshness.EVERGREEN,
                        true, true, LanguageApplicability.MULTI_LANGUAGE, now,
                        ContentFormat.OPEN_BOOK,
                        Set.of(ResourceAuthor.SCOTT_CHACON)
                )
        );
    }
}
