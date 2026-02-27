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
 * Curated resources for digital note-taking, personal knowledge management (PKM),
 * and productivity tool migration.
 *
 * <p>Covers the full spectrum of modern PKM:
 * <ul>
 *   <li>Methodologies: PARA (Projects, Areas, Resources, Archives), CODE (Capture,
 *       Organize, Distill, Express), Zettelkasten, progressive summarization</li>
 *   <li>Cross-platform tools: Notion, Obsidian, Logseq, OneNote, Google Docs/Keep</li>
 *   <li>Developer-specific PKM: note-taking for software engineers, code snippets,
 *       architectural decision records, learning journals</li>
 *   <li>Tool migration: moving between Notion, Obsidian, and Logseq</li>
 *   <li>Maintaining todos and action management (GTD adjacent, PARA actions)</li>
 * </ul>
 *
 * <p>Works across platforms: Windows, macOS, iOS, Android.
 *
 * <p>Resources span three difficulty tiers:
 * {@link DifficultyLevel#BEGINNER} (first note-taking system setup),
 * {@link DifficultyLevel#INTERMEDIATE} (methodology adoption, tool migration),
 * {@link DifficultyLevel#ADVANCED} (PKM automation, plugin development, custom workflows).
 *
 * @see GeneralResources
 * @see EngineeringResources
 */
public final class DigitalNoteakingResources implements ResourceProvider {

    @Override
    public List<LearningResource> resources() {
        final var now = Instant.now();
        return List.of(

                // ─── Methodologies & Systems ───────────────────────────────────

                new LearningResource(
                        "building-a-second-brain",
                        "Building a Second Brain — Tiago Forte (BASB)",
                        "https://www.buildingasecondbrain.com/",
                        "The foundational PKM system by Tiago Forte. Introduces the concept of "
                                + "a 'second brain' — an external digital system for capturing, "
                                + "organizing, distilling, and expressing ideas. The book covers the "
                                + "CODE method and the PARA framework. Includes free resources, a "
                                + "podcast, and access to the BASB community.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.PRODUCTIVITY, ResourceCategory.GENERAL),
                        List.of(ConceptArea.KNOWLEDGE_MANAGEMENT, ConceptArea.CAREER_DEVELOPMENT),
                        List.of("second-brain", "pkm", "tiago-forte", "basb", "code-method",
                                "para-method", "note-taking", "digital-notes", "knowledge-management"),
                        "Tiago Forte / Forte Labs",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                new LearningResource(
                        "para-method",
                        "PARA Method — Projects, Areas, Resources, Archives",
                        "https://fortelabs.com/blog/para/",
                        "Tiago Forte's universal folder structure for digital organization. "
                                + "PARA (Projects = active goals with deadlines, Areas = ongoing "
                                + "responsibilities, Resources = topics of interest, Archives = "
                                + "completed items) works with any tool — Notion, Obsidian, Finder, "
                                + "Google Drive, VS Code, or even a physical notebook. Ideal for "
                                + "software engineers managing multiple projects and areas of learning.",
                        ResourceType.BLOG,
                        List.of(ResourceCategory.PRODUCTIVITY, ResourceCategory.GENERAL),
                        List.of(ConceptArea.KNOWLEDGE_MANAGEMENT, ConceptArea.CAREER_DEVELOPMENT),
                        List.of("para", "para-method", "projects", "areas", "resources", "archives",
                                "tiago-forte", "folder-structure", "organization", "digital-organization"),
                        "Tiago Forte / Forte Labs",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.EVERGREEN,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                new LearningResource(
                        "code-method-four-levels-pkm",
                        "CODE Method — The 4 Levels of Personal Knowledge Management",
                        "https://fortelabs.com/blog/the-4-levels-of-personal-knowledge-management/",
                        "Deep dive into the CODE workflow: Capture (save anything useful), "
                                + "Organize (move material into PARA structure), Distill (progressive "
                                + "summarization — bold key passages, highlight the essence, create "
                                + "executive summaries), Express (produce a deliverable). Every software "
                                + "engineer can apply this to technical reading, conference notes, and "
                                + "architectural decisions.",
                        ResourceType.BLOG,
                        List.of(ResourceCategory.PRODUCTIVITY, ResourceCategory.GENERAL),
                        List.of(ConceptArea.KNOWLEDGE_MANAGEMENT, ConceptArea.CAREER_DEVELOPMENT),
                        List.of("code-method", "capture", "organize", "distill", "express",
                                "progressive-summarization", "pkm", "tiago-forte", "second-brain"),
                        "Tiago Forte / Forte Labs",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.EVERGREEN,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                new LearningResource(
                        "zettelkasten-introduction",
                        "Zettelkasten — Introduction to the Method",
                        "https://zettelkasten.de/introduction/",
                        "Comprehensive guide to the Zettelkasten ('slip-box') method developed "
                                + "by sociologist Niklas Luhmann. Covers permanent notes, literature "
                                + "notes, fleeting notes, linking ideas, emergence of structure over "
                                + "time, and the difference between a Zettelkasten and a simple "
                                + "note archive. Popular among developers using Obsidian or Logseq.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.PRODUCTIVITY, ResourceCategory.GENERAL),
                        List.of(ConceptArea.KNOWLEDGE_MANAGEMENT),
                        List.of("zettelkasten", "slip-box", "permanent-notes", "literature-notes",
                                "note-taking", "pkm", "linking-notes", "obsidian", "logseq"),
                        "zettelkasten.de / Sascha Fast & Christian Tietze",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.EVERGREEN,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                // ─── Tools — Cross-Platform ────────────────────────────────────

                new LearningResource(
                        "obsidian-official",
                        "Obsidian — Official Documentation",
                        "https://help.obsidian.md/",
                        "Official Obsidian help documentation. Obsidian is a local-first, "
                                + "offline-capable Markdown-based note-taking app. Highlights: "
                                + "bidirectional links, graph view, plugins (Dataview, Templater, "
                                + "Git sync), Canvas for visual mapping. Runs on Windows, macOS, "
                                + "Linux, iOS, and Android. Free for personal use. Great for "
                                + "developers who want full control and local storage.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.PRODUCTIVITY, ResourceCategory.TOOLS),
                        List.of(ConceptArea.KNOWLEDGE_MANAGEMENT, ConceptArea.GETTING_STARTED),
                        List.of("obsidian", "markdown", "bidirectional-links", "graph-view",
                                "plugins", "local-first", "offline", "windows", "macos", "ios",
                                "android", "note-taking", "pkm"),
                        "Obsidian.md",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.UNIVERSAL, now
                ),

                new LearningResource(
                        "notion-help-center",
                        "Notion — Official Help Center",
                        "https://www.notion.so/help",
                        "Official Notion documentation covering pages, databases, templates, "
                                + "views (table, board, calendar, gallery, timeline), relations, "
                                + "rollups, linked databases, formulas, and AI features. Notion "
                                + "is cloud-based, works on all platforms (web, Windows, macOS, "
                                + "iOS, Android), and is widely used for project wikis, personal "
                                + "dashboards, and team knowledge bases.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.PRODUCTIVITY, ResourceCategory.TOOLS),
                        List.of(ConceptArea.KNOWLEDGE_MANAGEMENT, ConceptArea.GETTING_STARTED),
                        List.of("notion", "databases", "pages", "templates", "cloud-based",
                                "cross-platform", "windows", "macos", "ios", "android",
                                "collaboration", "wiki", "pkm", "note-taking"),
                        "Notion Labs",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.UNIVERSAL, now
                ),

                new LearningResource(
                        "logseq-official",
                        "Logseq — Official Documentation",
                        "https://docs.logseq.com/",
                        "Documentation for Logseq, an open-source, privacy-first outliner and "
                                + "knowledge graph tool. Stores notes as plain Markdown or Org-mode "
                                + "files locally. Features bidirectional links, daily journals, "
                                + "queries, spaced repetition, and a plugin ecosystem. Ideal for "
                                + "developers who want open-source, version-controllable notes. "
                                + "Available on Windows, macOS, Linux, iOS, and Android.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.PRODUCTIVITY, ResourceCategory.TOOLS),
                        List.of(ConceptArea.KNOWLEDGE_MANAGEMENT, ConceptArea.GETTING_STARTED),
                        List.of("logseq", "open-source", "outliner", "markdown", "org-mode",
                                "bidirectional-links", "knowledge-graph", "spaced-repetition",
                                "privacy", "local", "pkm", "note-taking"),
                        "Logseq Team",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.UNIVERSAL, now
                ),

                new LearningResource(
                        "onenote-quickstart",
                        "Microsoft OneNote — Getting Started",
                        "https://support.microsoft.com/en-us/onenote",
                        "Microsoft's digital notebook that integrates directly with Microsoft 365. "
                                + "Organized into notebooks, sections, and pages. Features: rich "
                                + "media embedding, pen/ink support (Windows/iPad), OCR scanning, "
                                + "co-authoring, Outlook integration, and seamless sync across "
                                + "Windows, macOS, iOS, Android, and the web. Best for Microsoft "
                                + "ecosystem users — enterprise-friendly and free with M365.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.PRODUCTIVITY, ResourceCategory.TOOLS),
                        List.of(ConceptArea.KNOWLEDGE_MANAGEMENT, ConceptArea.GETTING_STARTED),
                        List.of("onenote", "microsoft", "office", "notebook", "microsoft-365",
                                "cloud-sync", "windows", "macos", "ios", "android", "enterprise",
                                "co-authoring", "note-taking"),
                        "Microsoft",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.UNIVERSAL, now
                ),

                // ─── Developer-Specific PKM ────────────────────────────────────

                new LearningResource(
                        "pkm-for-software-engineers",
                        "Personal Knowledge Management for Software Engineers",
                        "https://fortelabs.com/blog/how-to-take-smart-notes-for-software-engineers/",
                        "Practical PKM guide tailored for software engineers: how to capture "
                                + "API discoveries, architectural decisions, debugging insights, "
                                + "code snippets, and reading notes. Covers mapping CODE+PARA onto "
                                + "a developer's daily workflow — sprint notes, ADRs (Architecture "
                                + "Decision Records), learning journals, and technical reading lists. "
                                + "Directly applicable to any engineering role.",
                        ResourceType.BLOG,
                        List.of(ResourceCategory.PRODUCTIVITY, ResourceCategory.SOFTWARE_ENGINEERING),
                        List.of(ConceptArea.KNOWLEDGE_MANAGEMENT, ConceptArea.CAREER_DEVELOPMENT,
                                ConceptArea.ARCHITECTURE),
                        List.of("pkm", "developer", "software-engineer", "adr", "learning-journal",
                                "code-snippets", "second-brain", "tiago-forte", "engineering-notes"),
                        "Forte Labs",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.EVERGREEN,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                // ─── Tool Migration & Comparison ───────────────────────────────

                new LearningResource(
                        "notion-to-obsidian-migration",
                        "Migrating from Notion to Obsidian — Community Guide",
                        "https://help.obsidian.md/import/notion",
                        "Official Obsidian guide for importing Notion content. Covers using the "
                                + "Notion-to-Obsidian importer plugin, preserving database structure, "
                                + "converting Notion blocks to Markdown, handling linked databases, "
                                + "and rebuilding your workspace in Obsidian's local-first model. "
                                + "Useful for developers switching from cloud-based to local-first tools.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.PRODUCTIVITY, ResourceCategory.TOOLS),
                        List.of(ConceptArea.KNOWLEDGE_MANAGEMENT),
                        List.of("migration", "notion-to-obsidian", "import", "obsidian", "notion",
                                "tool-switch", "markdown", "database-migration", "workflow"),
                        "Obsidian Community",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                // ─── Todo & Action Management ──────────────────────────────────

                new LearningResource(
                        "todoist-best-practices",
                        "Todoist — Getting Things Done with PARA",
                        "https://app.todoist.com/app/getting-started",
                        "Guide to using Todoist alongside PARA for action management. Covers "
                                + "project-based task organization, priority levels, filters, labels, "
                                + "recurring tasks, and integration with Notion/Obsidian via Zapier or "
                                + "native APIs. Cross-platform: web, Windows, macOS, iOS, Android. "
                                + "Shows how to separate 'tasks' (Todoist) from 'knowledge' (Notion/"
                                + "Obsidian) — a best practice recommended by Tiago Forte.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.PRODUCTIVITY, ResourceCategory.TOOLS),
                        List.of(ConceptArea.KNOWLEDGE_MANAGEMENT, ConceptArea.CAREER_DEVELOPMENT),
                        List.of("todoist", "tasks", "todos", "gtd", "para", "action-management",
                                "productivity", "cross-platform", "windows", "macos", "ios", "android"),
                        "Doist / Todoist",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.UNIVERSAL, now
                )
        );
    }
}
