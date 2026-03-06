---
name: digital-notetaking
description: >
  Digital note-taking, personal knowledge management (PKM), and productivity tool guidance.
  Use when asked about Notion, Obsidian, OneNote, Logseq, Google Docs, PARA method,
  CODE method, Zettelkasten, second brain, note-taking for developers, tool migration,
  or maintaining todos across platforms (Windows / macOS / iOS / Android).
---

# Digital Note-Taking & PKM Skill

> Cross-platform knowledge management for software engineers. Covers tools, methods,
> migration guides, and developer-specific note-taking workflows.

---

## Core Methodologies (Tool-Agnostic)

### CODE Method (Tiago Forte)

| Step | Action | Developer Example |
|---|---|---|
| **Capture** | Save anything worth keeping | Bookmark article, clip API doc, save SO answer |
| **Organize** | File into PARA structure | Move to Projects/, Areas/, Resources/, Archives/ |
| **Distill** | Highlight key insight | Bold key method signature, summarize in 1 sentence |
| **Express** | Produce a deliverable | Write ADR, blog post, PR description, wiki page |

### PARA Method (Tiago Forte)

| Folder | Definition | Development Example |
|---|---|---|
| **Projects** | Active goals with deadlines | `mcp-servers v2`, `JDK 25 migration`, `interview prep` |
| **Areas** | Ongoing responsibilities | `Java`, `System Design`, `Career`, `Health` |
| **Resources** | Topics of interest | `Distributed Systems`, `Functional Programming`, `Books` |
| **Archives** | Completed / inactive | `old-job`, `finished-courses`, `deprecated-tech` |

> **Tip:** Use PARA as your folder structure in ANY tool — Notion, Obsidian,
> Google Drive, or even your file system.

### Zettelkasten (Slip-Box)

- **Fleeting notes** — rough captures (inbox)
- **Literature notes** — summaries of sources (in your own words)
- **Permanent notes** — atomic, link-rich, standalone ideas
- **Links over hierarchy** — connect notes, don't nest them

---

## Newbie — Setting Up Your First System

### Option A: Notion (Cloud, Cross-Platform)

```
1. Create a free account at notion.so
2. Create one top-level page: "My Second Brain"
3. Add 4 sub-pages: Projects / Areas / Resources / Archives (PARA)
4. Start capturing everything in the Inbox page
5. Use the Web Clipper browser extension for articles
```

**Best for:** Beginners, team wikis, iOS/Android users, enterprise

### Option B: Obsidian (Local, Offline-First)

```
1. Download from obsidian.md (free for personal use)
2. Create a vault in a synced folder (iCloud / Dropbox / Obsidian Sync)
3. Install community plugins: Dataview, Templater, Calendar
4. Create folders: Inbox/, Projects/, Areas/, Resources/, Archives/
5. Use Daily Notes as your capture mechanism
```

**Best for:** Privacy-conscious, developers, Markdown lovers, offline use

### Option C: Logseq (Open-Source)

```
1. Download from logseq.com (free + open-source)
2. Choose a local folder or GitHub repo for storage
3. Use the Daily Journal page to capture throughout the day
4. Add #tags and [[links]] to connect notes
5. Use queries to surface related content
```

**Best for:** Open-source advocates, version-controllable notes, Zettelkasten style

---

## Amateur — Daily Developer Workflow

### Note-Taking Templates for Engineers

#### Architecture Decision Record (ADR) Template

```markdown
# ADR-001: Use Hexagonal Architecture

**Status:** Accepted
**Date:** 2026-02-27
**Context:** Need to decouple the MCP server from transport implementations.
**Decision:** Adopt ports & adapters (hexagonal) architecture.
**Consequences:** More files; cleaner testing; transport-agnostic core.
```

#### Sprint / Learning Journal Template

```markdown
# 2026-02-27 Log

## Learned Today
- Virtual threads in JDK 21 — structured concurrency API
- SDKMAN! can pin JDK per repo via `.sdkmanrc`

## Questions
- How does Loom interact with existing ExecutorService code?

## Tomorrow
- Read JDK 25 migration guide (oracle.com/migrate)
- Write ADR for transport layer choice
```

#### Code Snippet Note Template

```markdown
# Java: Stream groupingBy

**Tags:** #java #streams #collectors

```java

Map<String, List<Person>> byDept =
    people.stream()
          .collect(Collectors.groupingBy(Person::getDepartment));

```

**Source:** https://docs.oracle.com/en/java/javase/21/docs/api/
**Date:** 2026-02-27
```

### Maintaining Todos in PARA

- **Projects** → your active project page = task list (or link to Todoist/Linear)
- **Inbox** → capture random tasks here, review weekly, promote to Projects or delete
- **Not in Archives** → a task you "archived" is done, delete it or move to log

---

## Pro — Advanced Developer PKM

### Obsidian Power Setup

```
vault/
├── Inbox/              ← Daily capture, unfiled
├── Projects/
│   ├── mcp-servers/    ← One folder per active project
│   └── jdk25-upgrade/
├── Areas/
│   ├── Java/           ← Permanent notes on Java concepts
│   └── Architecture/
├── Resources/          ← Book summaries, article notes
├── Archives/           ← Completed projects
└── Templates/          ← Templater templates
```

**Power plugins:**
- **Dataview** — query your vault like a database: `TABLE file.tags FROM ...`
- **Templater** — dynamic templates with dates, prompts, JS logic
- **Git** — commit your vault to GitHub automatically (enable version history)
- **Excalidraw** — draw architecture diagrams inside Obsidian

### Notion Power Setup for Teams

```
Workspace Root
├── 📋 Projects DB   ← Linked database: status, owner, deadline, PARA link
├── 📚 Resources DB  ← Tag-filtered knowledge base
├── 📓 Daily Notes   ← Daily journal pages
├── 🗂 Team Wiki    ← Living docs, ADRs, onboarding
└── 🗃 Archives     ← Completed sprints, old wikis
```

### Tool Migration Path

```
Notion → Obsidian:
  1. Export from Notion (Settings → Export as Markdown & CSV)
  2. Use Obsidian's built-in "Import from Notion" plugin
  3. Convert databases → Dataview tables

Logseq → Obsidian:
  1. Copy the .md files directly (format is compatible)
  2. Resolve [[page-name]] links (usually no changes needed)

OneNote → Notion:
  1. OneNote → Word → Markdown (use Pandoc)
  2. Import Markdown into Notion via "Import" button
```

### JDK Version Management (cross-platform)

```bash
# Install SDKMAN! (Linux/macOS/WSL on Windows)
curl -s "https://get.sdkman.io" | bash

# List available JDK distributions
sdk list java

# Install JDK 25 (Eclipse Temurin LTS)
sdk install java 25-tem

# Pin JDK to a project (create .sdkmanrc)
sdk env init        # creates .sdkmanrc with current version
sdk env             # apply .sdkmanrc in this shell

# Switch back to JDK 21 globally
sdk default java 21.0.7-tem
```

**Windows (without WSL):** Download directly from adoptium.net

```powershell
# Set JAVA_HOME manually (PowerShell)
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-25"
$env:PATH = "$env:JAVA_HOME\bin;" + $env:PATH
java --version     # should show 25
```

---

## Learning Resources

| Resource | Tier | URL |
|---|---|---|
| Building a Second Brain (BASB) | Newbie | https://www.buildingasecondbrain.com/ |
| PARA Method — Forte Labs | Newbie | https://fortelabs.com/blog/para/ |
| CODE Method — 4 Levels of PKM | Intermediate | https://fortelabs.com/blog/the-4-levels-of-personal-knowledge-management/ |
| Progressive Summarization | Intermediate | https://fortelabs.com/blog/progressive-summarization-a-practical-technique-for-designing-discoverable-notes/ |
| Zettelkasten Introduction | Intermediate | https://zettelkasten.de/introduction/ |
| Getting Things Done (GTD) | Newbie | https://gettingthingsdone.com/what-is-gtd/ |
| Obsidian Documentation | Newbie | https://help.obsidian.md/ |
| Obsidian Community Plugins | Intermediate | https://obsidian.md/plugins |
| Notion Help Center | Newbie | https://www.notion.so/help |
| Logseq Documentation | Newbie | https://docs.logseq.com/ |
| Foam — VS Code PKM | Newbie | https://foambubble.github.io/foam/ |
| PKM for Software Engineers | Intermediate | https://fortelabs.com/blog/how-to-take-smart-notes-for-software-engineers/ |
| Notion → Obsidian Migration | Intermediate | https://help.obsidian.md/import/notion |
| Todoist + PARA Guide | Newbie | https://app.todoist.com/app/getting-started |
| SDKMAN! — JDK Version Manager | Beginner | https://sdkman.io/ |
| Eclipse Temurin (Adoptium) | Beginner | https://adoptium.net/ |
| JDK 25 Release Notes | Intermediate | https://openjdk.org/projects/jdk/25/ |

---

## Related Docs in This Repo

| Guide | What it covers |
|---|---|
| [brain/digitalnotetaking/START-HERE.md](../../../brain/digitalnotetaking/START-HERE.md) | Pick a tool, set up PARA, capture your first note |
| [brain/digitalnotetaking/tools-comparison.md](../../../brain/digitalnotetaking/tools-comparison.md) | Notion vs Obsidian vs Logseq vs OneNote — decision guide |
| [brain/digitalnotetaking/para-method.md](../../../brain/digitalnotetaking/para-method.md) | PARA method applied to every tool |
| [brain/digitalnotetaking/templates.md](../../../brain/digitalnotetaking/templates.md) | ADR, daily log, snippet vault, resource, debug, meeting templates |
| [brain/digitalnotetaking/migration-guide.md](../../../brain/digitalnotetaking/migration-guide.md) | Notion→Obsidian, OneNote→Notion, Logseq→Obsidian, Any→Markdown |
| [brain/ai-brain/README.md](../../../brain/ai-brain/README.md) | Live ai-brain workspace (inbox, notes, archive, scripts) |
