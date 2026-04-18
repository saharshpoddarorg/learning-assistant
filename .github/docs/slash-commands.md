# ⚡ Slash Commands — Developer Quick Reference

> **Audience:** 👤 Developer
> **Purpose:** One-stop reference for ALL slash commands — aliases, usage, inputs, composition, and tips.
> **When to use:** When you need to know which command to type, what inputs it expects, or how to chain commands.

---

## 📑 Table of Contents

- [How Slash Commands Work](#-how-slash-commands-work)
- [All Commands at a Glance](#-all-commands-at-a-glance)
- [Command Details by Category](#-command-details-by-category)
  - [Navigation & Meta](#navigation--meta)
  - [Learning & Concepts](#learning--concepts)
  - [Domain-Specific Learning](#domain-specific-learning)
  - [Code Quality & Analysis](#code-quality--analysis)
  - [Career & Interview](#career--interview)
  - [Daily Life](#daily-life)
  - [Brain PKM](#brain-pkm--git-inspired-content-operations)
  - [Copilot Customization](#copilot-customization)
- [Aliases & Shortcuts](#-aliases--shortcuts)
- [Input Parameters Reference](#-input-parameters-reference)
- [Composition Patterns](#-composition-patterns)
- [Agent Pairing Guide](#-agent-pairing-guide)
- [Tips & Best Practices](#-tips--best-practices)

---

## 🔧 How Slash Commands Work

1. Open VS Code Chat (`Ctrl+Shift+I` or `Ctrl+L`)
2. Type `/` — the command picker appears
3. Select or type the command name
4. Fill in the input prompts (topic, mode, language, etc.)
5. Press Enter — Copilot executes using the command's template + associated agent

```text
You type:    /dsa → binary-search → learn-concept → python → intermediate
Copilot:     Uses Learning-Mentor agent + DSA prompt template
Result:      Full binary search lesson with Python code, complexity analysis, practice problems
```

> **Under the hood:** Each `/command` is a `.prompt.md` file in `.github/prompts/`. The YAML frontmatter selects the agent and tools. The body contains the task instructions that Copilot follows.

---

## 📋 All Commands at a Glance

### Quick Lookup (59 commands)

| # | Command | Category | One-Liner | Agent |
|---|---|---|---|---|
| 1 | `/hub` | Navigation | Browse all commands by category | Learning-Mentor |
| 2 | `/composite` | Meta | Combine multiple modes in one session | Agent |
| 3 | `/context` | Meta | Continue prior conversation or start fresh | Agent |
| 4 | `/scope` | Meta | Set generic learning vs code-specific scope | Agent |
| 5 | `/multi-session` | Meta | Save/resume state across chat sessions | Agent |
| 6 | `/steer` | Meta | View or switch the active Copilot steering mode | Copilot |
| 7 | `/learn-concept` | Learning | Learn any CS/SE concept from scratch | Learning-Mentor |
| 8 | `/deep-dive` | Learning | Multi-layered progressive concept exploration | Learning-Mentor |
| 9 | `/learn-from-docs` | Learning | Learn via official documentation | Learning-Mentor |
| 10 | `/reading-plan` | Learning | Create a structured study plan | Learning-Mentor |
| 11 | `/teach` | Learning | Learn concepts from current file | Learning-Mentor |
| 12 | `/dsa` | Domain | Data structures & algorithms | Learning-Mentor |
| 13 | `/system-design` | Domain | HLD/LLD system design | Learning-Mentor |
| 14 | `/devops` | Domain | CI/CD, Docker, K8s, cloud, IaC, Git, build tools | Learning-Mentor |
| 15 | `/language-guide` | Domain | Language-specific learning path | Learning-Mentor |
| 16 | `/tech-stack` | Domain | Frameworks, databases — compare & learn | Learning-Mentor |
| 17 | `/sdlc` | Domain | SDLC phases, methodologies, practices | Learning-Mentor |
| 18 | `/mcp` | Domain | Learn & build MCP (Model Context Protocol) servers | Learning-Mentor |
| 19 | `/interview-prep` | Career | Interview preparation (DSA, system design, behavioral) | Learning-Mentor |
| 20 | `/career-roles` | Career | Job roles, skills, pay ranges, roadmaps | Learning-Mentor |
| 21 | `/design-review` | Code Quality | SOLID/GRASP design review of current file | Designer |
| 22 | `/refactor` | Code Quality | Identify and apply refactoring opportunities | Designer |
| 23 | `/explain` | Code Quality | Beginner-friendly file explanation | Ask |
| 24 | `/debug` | Code Quality | Systematic bug investigation | Debugger |
| 25 | `/impact` | Code Quality | Change impact & ripple effect analysis | Impact-Analyzer |
| 26 | `/explore-project` | Domain | Learn from open-source project architecture | Learning-Mentor |
| 27 | `/resources` | Domain | Search, browse & discover 138 curated learning resources | Learning-Mentor |
| 28 | `/daily-assist` | Daily Life | Finance, productivity, news, research | Daily-Assistant |
| 29 | `/brain-new` | Brain Workspace | Create a new knowledge note (inbox or notes tier) | Copilot |
| 30 | `/brain-publish` | Brain Workspace | Publish an imported source to library/ with tag prompting and git commit | Copilot |
| 31 | `/brain-search` | Brain Workspace | Search notes by tag, project, kind, date, or full text | Copilot |
| 32 | `/brain-capture-session` | Brain Workspace | Convert current AI session into a structured session note | Copilot |
| 33 | `/session-scope` | Brain Workspace | Manage session scope — widen, narrow, switch, split, cross-reference | Copilot |
| 34 | `/jot` | Backlog | Universal capture — auto-classifies, dedup-checks, enhances, cross-refs, syncs all boards | Copilot |
| 35 | `/read-file-jot` | Backlog | File-to-backlog — reads file, dedup-checks, extracts items, tracks import batch (IMP-NNN) | Copilot |
| 36 | `/todo` | Backlog | Alias for `/jot` — routes through unified capture as a task | Copilot |
| 37 | `/todos` | Backlog | View board, update status — syncs all boards + CHANGELOG | Copilot |
| 38 | `/backlog` | Backlog | Advanced: brainstorm, guide, refine, promote, epic, sprint — full board sync | Copilot |
| 39 | `/brain-fetch` | Brain PKM | Fetch content from an external source into inbox/ (git fetch analogy) | Copilot |
| 40 | `/brain-pull` | Brain PKM | Update previously fetched content from a source (git pull analogy) | Copilot |
| 41 | `/brain-clone` | Brain PKM | Full structured import of an entire source (git clone analogy) | Copilot |
| 42 | `/brain-merge` | Brain PKM | Route inbox item to its proper tier (git merge analogy) | Copilot |
| 43 | `/brain-cherry-pick` | Brain PKM | Import one specific item from a source (git cherry-pick analogy) | Copilot |
| 44 | `/brain-stash` | Brain PKM | Park inbox content for later processing (git stash analogy) | Copilot |
| 45 | `/brain-diff` | Brain PKM | Compare brain content vs external source (git diff analogy) | Copilot |
| 46 | `/brain-push` | Brain PKM | Export brain content to an external source (git push analogy) | Copilot |
| 47 | `/brain-remote` | Brain PKM | List all capture sources with sensitivity levels (git remote analogy) | Copilot |
| 48 | `/brain-consolidate` | Brain PKM | Plan/execute brain consolidation — migrate from external tools to brain | Copilot |
| 49 | `/git-vcs` | Domain | Git workflows, branching strategies, commit conventions, semver | Learning-Mentor |
| 50 | `/github-workflow` | Domain | GitHub PRs, issues, gh CLI, Actions, branch protection, repo management | Learning-Mentor |
| 51 | `/atlassian-tools` | Domain | Jira, Confluence, Bitbucket — JQL, CQL, sprints, wiki, PRs, PAT CLI workflows | Copilot |
| 52 | `/build-tools` | Domain | Maven, Gradle, Make, Bazel, npm — lifecycle & dependency management | Learning-Mentor |
| 53 | `/mac-dev` | Domain | macOS dev environment — Homebrew, JDK, npm, IDEs, Docker, shell, dotfiles | Learning-Mentor |
| 54 | `/digital-notetaking` | Domain | PKM systems (PARA, CODE, Zettelkasten), tools (Notion, Obsidian, Logseq, OneNote), migration & JDK upgrade | Learning-Mentor |
| 55 | `/create-agent` | Customization | Scaffold a new Copilot custom agent (.agent.md) with guided inputs | Copilot |
| 56 | `/copilot-customization` | Customization | Create, review, compare, or compose any Copilot customization file (instructions/prompts/skills/agents/MCP) | Copilot |
| 57 | `/write-docs` | Meta | Create or update any doc, guide, brain-note, cheatsheet, start-here, skill, or slash command from provided content | Copilot |
| 58 | `/check-standards` | Quality & Standards | Audit any file, folder, or filename against best practices and industry standards | Copilot |
| 59 | `/mcp-to-skill` | Customization | Analyse an MCP server/tool and generate a Copilot SKILL.md replacement | Copilot |

> **What's New (March 2026 — Open Preview):** GitHub Copilot MCP is now in **open preview** for all subscribers.
> VS Code also gained a **built-in `/create-agent` wizard** in Copilot Chat. See [copilot-mcp-preview.md](copilot-mcp-preview.md) for the full changelog.

---

## 📖 Command Details by Category

### Navigation & Meta

#### `/hub` — Master Navigation Index

```yaml
Inputs:   category (e.g., se, dsa, system-design, devops, daily, career)
Agent:    Learning-Mentor
Tools:    codebase, fetch
Use:      Browse all available commands organized by category tree
Tip:      Start here if you're lost — type /hub and pick a branch
```

#### `/composite` — Combine Multiple Modes

```yaml
Inputs:   modes (e.g., refactor, design-review, impact)
Agent:    Agent
Use:      Run 2+ analysis modes in one session with deduplicated output
Example:  /composite → refactor, design-review → unified analysis
Tip:      Great for code quality passes — avoids repeating analysis
```

#### `/context` — Continue or Start Fresh

```yaml
Inputs:   action (continue / fresh)
Agent:    Agent
Use:      Reconnect to a prior conversation's decisions, or explicitly reset
Tip:      Use "continue" after switching files or taking a break
```

#### `/scope` — Generic vs Code-Specific

```yaml
Inputs:   scope (generic / specific)
Agent:    Agent
Use:      Tell Copilot whether you want pure theory or code-applied learning
Example:  /scope → generic + /learn-concept → SOLID = theory lesson
          /scope → specific + /learn-concept → SOLID = applied to your codebase
```

#### `/multi-session` — Cross-Session State

```yaml
Inputs:   action (save-state / resume / handoff / status)
Agent:    Agent
Use:      Persist context across multiple chat sessions via checkpoint file
Tip:      Always save-state before a long session hits token limits
File:     Creates/updates .github/session-state.md
```

#### `/steer` — Steering Mode Navigator

```yaml
Inputs:   action (view / switch / explain / default), mode (completeness / beast / learning / design / debug / focused)
Agent:    Copilot
Tools:    codebase
Use:      View or switch the active behavioral steering mode for the current session
Default:  completeness — Change Completeness checklist always active (** glob)
Modes:    completeness (default) | beast | learning | design | debug | focused
Example:  /steer → view                 (see all modes + which is active)
          /steer → switch → beast       (switch to Thinking-Beast-Mode)
          /steer → default              (confirm completeness is active)
File:     .github/instructions/steering-modes.instructions.md
Tip:      After 'beast' or 'focused' mode work, always return to 'completeness'
          before committing to the repo
```

---

### Learning & Concepts

#### `/learn-concept` — Learn Any Concept

```yaml
Inputs:   concept, language (optional), depth (overview/detailed/exhaustive)
Agent:    Learning-Mentor
Tools:    codebase, fetch
Use:      Learn any CS/SE concept from scratch — language-agnostic
Example:  /learn-concept → deadlocks → java → detailed
Output:   Definition → Why → Analogy → How → Code → Anti-example → Mistakes → Next
```

#### `/deep-dive` — Progressive Exploration

```yaml
Inputs:   concept, starting-level
Agent:    Learning-Mentor
Tools:    codebase, fetch
Use:      Peel back layers — each round goes deeper
Example:  /deep-dive → generics
          Layer 1: What are generics? Layer 2: Type erasure. Layer 3: Wildcards. Layer 4: Variance.
```

#### `/learn-from-docs` — Official Documentation

```yaml
Inputs:   concept, docs-source (optional)
Agent:    Learning-Mentor
Tools:    codebase, fetch
Use:      Copilot fetches + translates official docs into plain language
Example:  /learn-from-docs → sealed classes → JEP 409
Tip:      Specify the exact doc/JEP/RFC for better results
```

#### `/reading-plan` — Structured Study Plan

```yaml
Inputs:   topic, duration, level
Agent:    Learning-Mentor
Tools:    codebase, fetch
Use:      Get a week-by-week study plan with curated resources
Example:  /reading-plan → design patterns → 4 weeks → intermediate
Output:   Sequenced plan with books, tutorials, exercises, milestones
```

#### `/teach` — Learn From Code

```yaml
Inputs:   (uses current open file automatically)
Agent:    Learning-Mentor
Tools:    search, codebase, usages
Use:      Identifies and teaches all concepts in the currently open file
Tip:      Open any .java file first, then type /teach
Output:   Concept list → explanation of each → practice suggestion
```

---

### Domain-Specific Learning

#### `/dsa` — Data Structures & Algorithms

```yaml
Inputs:   topic, mode, language, level
Modes:    learn-concept | solve-pattern | practice-problems | compare-structures |
          complexity-analysis | interview-roadmap
Agent:    Learning-Mentor
Tools:    codebase, fetch
Example:  /dsa → sliding-window → solve-pattern → python → intermediate
Output:   Pattern description → signal words → template → trace → complexity → problems
```

#### `/system-design` — System Design (HLD/LLD)

```yaml
Inputs:   topic, level (hld/lld/both/concept/interview-practice), depth
Agent:    Learning-Mentor
Tools:    codebase, fetch
Example:  /system-design → url-shortener → hld → detailed
Output:   Requirements → estimation → architecture diagram → deep-dive → trade-offs
```

#### `/devops` — DevOps & Infrastructure

```yaml
Inputs:   topic, depth
Agent:    Learning-Mentor
Tools:    codebase, fetch
Example:  /devops → Docker → detailed
Topics:   CI/CD, containers, Kubernetes, cloud, IaC, monitoring,
          build tools (Maven/Gradle/Ant), Git commands & branching strategies,
          deployment strategies, GitOps, feature flags
```

#### `/language-guide` — Language Learning

```yaml
Inputs:   language, focus-area, level
Agent:    Learning-Mentor
Tools:    codebase, fetch
Example:  /language-guide → Rust → ownership → beginner
Languages: Java, Python, C++, Go, Rust, JavaScript/TypeScript, and more
```

#### `/tech-stack` — Frameworks & Tools

```yaml
Inputs:   topic, action (learn/compare/evaluate)
Agent:    Learning-Mentor
Tools:    codebase, fetch
Example:  /tech-stack → compare Spring vs FastAPI
```

#### `/sdlc` — Software Development Lifecycle

```yaml
Inputs:   topic, depth
Agent:    Learning-Mentor
Tools:    codebase, fetch
Example:  /sdlc → testing → TDD
Topics:   Agile, Scrum, Kanban, XP, SAFe, test pyramid, CI/CD practices,
          E2E phases (planning, design, dev, testing, deployment, monitoring, maintenance),
          frontend/UI/UX aspects, backend aspects, database aspects
```

#### `/explore-project` — Open Source Study

```yaml
Inputs:   project, focus
Agent:    Learning-Mentor
Tools:    codebase, fetch
Example:  /explore-project → Redis → event loop architecture
Output:   Repository overview → architecture map → key patterns → lessons
```

#### `/resources` — Learning Resource Vault

```yaml
Inputs:   action (search/browse/scrape/recommend/details/discover), topic/URL, filters (optional)
Agent:    Learning-Mentor
Tools:    codebase, fetch
Example:  /resources → search → java concurrency
          /resources → discover → learn testing → (auto: exploratory mode)
          /resources → browse → devops
          /resources → recommend → beginner java
Actions:  search (keyword/tag), browse (by domain), scrape (any URL via fetch),
          recommend (topic-based), details (deep-dive), discover (smart 3-mode)
Vault:    138 curated resources across 10 domains: Java (20), Web/JS/TS (12),
          Python (6), Algorithms & DS (11), Software Engineering & Testing (11),
          DevOps/VCS/Build (25), Cloud/Infra/Data (15), AI/ML (4),
          PKM & Note-Taking (15), General & Career (19)
Skill:    Backed by learning-resources-vault skill (.github/skills/learning-resources-vault/)
          with 10 domain sub-files loaded on demand by semantic match.
          Works in ALL Copilot modes (Ask, Edit, Agent) — no MCP server required.
Badges:   🟢Beginner 🟡Intermediate 🔴Advanced ⚫Expert | 📖Docs 📝Tutorial
          📰Blog 📚Book 🎓Course 🎮Interactive 📋API Ref 💻Repo
```

#### `/git-vcs` — Git & Version Control

```yaml
Inputs:   topic, goal, level (newbie/amateur/pro)
Agent:    Learning-Mentor
Tools:    codebase, fetch
Example:  /git-vcs → rebase → understand-internals → pro
          /git-vcs → conventional-commits → learn-concept → amateur
Topics:   Git commands, branching, merging, rebasing, stash, cherry-pick,
          GitFlow, GitHub Flow, trunk-based development,
          conventional commits, semantic versioning, hooks, git internals
Levels:   newbie (commands) → amateur (workflows/conventions) → pro (internals/automation)
Resources: Learn Git Branching, Atlassian Git Tutorials, Pro Git Ch.10,
           Conventional Commits, GitFlow, Trunk-Based Development
```

#### `/github-workflow` — GitHub Platform Workflows

```yaml
Inputs:   topic, goal, level (newbie/amateur/pro)
Agent:    Learning-Mentor
Tools:    codebase, fetch
Example:  /github-workflow → pr-create → do-it-now → newbie
          /github-workflow → actions → automate → pro
Topics:   Pull requests (create, review, merge), issues (lifecycle, templates),
          gh CLI (auth, pr, issue, repo, run), GitHub Actions (workflows, CI),
          branch protection, CODEOWNERS, releases, repository management
Levels:   newbie (basic gh commands) → amateur (PR workflow/conventions) → pro (Actions/automation)
Related:  /git-vcs (local Git), copilot-customization (Copilot + GitHub)
Resources: GitHub CLI Manual, GitHub Docs (PRs, Issues, Actions)
```

#### `/atlassian-tools` — Jira, Confluence & Bitbucket Workflows

```yaml
Inputs:   task, service (jira/confluence/bitbucket/cross-tool), level (newbie/amateur/pro)
Agent:    Copilot
Tools:    codebase, terminal
Example:  /atlassian-tools → search my open bugs → jira → amateur
          /atlassian-tools → create sprint review page → cross-tool → pro
Topics:   Jira (issues, JQL, sprints, epics, bulk ops, worklogs, transitions),
          Confluence (pages, CQL, blogs, macros, Mermaid, labels, versioning),
          Bitbucket (PRs, diffs, comments, tasks, file lookup, contributions),
          Cross-tool (sprint planning, release notes, incident post-mortem, status reports)
Levels:   newbie (fetch/search basics) → amateur (JQL/CQL, workflows) → pro (bulk ops, playbooks)
Related:  /github-workflow (GitHub-specific), software-development-roles (role workflows)
Skill:    Backed by atlassian-tools/SKILL.md (89-action CLI, 6 reference files)
```

#### `/build-tools` — Build Automation

```yaml
Inputs:   tool (maven/gradle/make/bazel/npm), topic, level (newbie/amateur/pro)
Agent:    Learning-Mentor
Tools:    codebase, fetch
Example:  /build-tools → maven → dependency-management → amateur
          /build-tools → gradle → kotlin-dsl → pro
Tools covered: Maven (lifecycle, POM, BOM, multi-module),
               Gradle (Kotlin DSL, version catalog, multi-project),
               Make (Makefiles, targets, variables),
               Bazel (Starlark, BUILD files, remote execution),
               npm/yarn/pnpm (package.json, workspaces, scripts)
Levels:   newbie (first build) → amateur (day-to-day) → pro (multi-module/CI/advanced)
Resources: Maven Getting Started, Maven POM Reference, Gradle User Guide,
           Gradle Kotlin DSL, Bazel Docs, Makefile Tutorial, npm Docs
```

#### `/mac-dev` — macOS Development Environment

```yaml
Inputs:   topic (homebrew/npm/nvm/jdk/ide/docker/shell/aliases/dotfiles/brewfile/bootstrap),
          area (install/commands/switch-versions/configure/automate/troubleshoot),
          level (newbie/amateur/pro)
Agent:    Learning-Mentor
Tools:    codebase, fetch
Example:  /mac-dev → homebrew → install → newbie
          /mac-dev → jdk → switch-versions → amateur
          /mac-dev → dotfiles → bootstrap → pro
Topics:   Homebrew (formulae, casks, taps, Brewfile),
          JDK (Temurin, Corretto, Zulu, GraalVM, jenv, SDKMAN),
          npm/nvm (multi-version Node.js, global tools, package.json scripts),
          IDEs (IntelliJ IDEA, VS Code, PyCharm via Homebrew cask),
          Docker Desktop (install, compose, dev services),
          Shell/zsh (.zshrc, aliases, PATH, plugins, Starship, oh-my-zsh),
          Dotfiles (Git-tracked config, symlinks, bootstrap.sh)
Levels:   newbie (install & first use) → amateur (structure & configure) → pro (automate)
Docs:     mac-os/docs/ — START-HERE, homebrew-guide, jdk-setup, npm-on-mac,
          dev-tools-guide, mac-dev-environment
Skill:    Backed by mac-dev/SKILL.md (Homebrew, JDK, npm, Docker cheatsheets)
```

#### `/digital-notetaking` — Digital Note-Taking, PKM & JDK Upgrade

```yaml
Inputs:   topic (notion/obsidian/logseq/onenote/para-method/code-method/zettelkasten/
                 migration/jdk-upgrade/sdkman/temurin/todo-management),
          tool  (notion/obsidian/logseq/onenote/google-docs/any),
          level (newbie/intermediate/advanced),
          os    (windows/macos/linux/cross-platform)
Agent:    Learning-Mentor
Tools:    codebase, fetch
Example:  /digital-notetaking → para-method → obsidian → intermediate → windows
          /digital-notetaking → migration → notion-to-obsidian → advanced
          /digital-notetaking → jdk-upgrade → sdkman → beginner → macos
Topics:   Methodologies: PARA (Projects, Areas, Resources, Archives),
          CODE (Capture, Organize, Distill, Express), Zettelkasten,
          Tools: Notion (cloud, cross-platform), Obsidian (local-first, Markdown),
                 Logseq (open-source), Microsoft OneNote, Google Docs/Keep
          Developer PKM: ADRs, learning journals, code snippet vaults
          Migration: Notion→Obsidian, Logseq→Obsidian, OneNote→Notion
          Todo management: PARA actions, Todoist integration
          JDK upgrade: SDKMAN!, Eclipse Temurin, JDK 25 LTS migration from JDK 21
Platforms: Windows, macOS (see /mac-dev), Linux, iOS, Android
Skill:    Backed by digital-notetaking/SKILL.md (tool comparison, templates, JDK commands)
Docs:     brain/digitalnotetaking/ — START-HERE, tools-comparison, para-method,
          code-method (CODE: Capture/Organize/Distill/Express), templates,
          migration-guide (Notion→Obsidian, OneNote→Notion, Logseq→Obsidian),
          ai-brain-integration (linking AI sessions to your note system)
Java:     brain/src/digitalnotetaking/ — NoteKind, NoteStatus, NoteMetadata, NoteTemplate
```

---

### Code Quality & Analysis

#### `/design-review` — Architecture Review

```yaml
Inputs:   (uses current file)
Agent:    Designer
Tools:    search, codebase, usages, fetch
Use:      SOLID/GRASP design review with severity ratings
Output:   Findings table → recommendations → refactoring suggestions
Tip:      Open the file first, then run /design-review
```

#### `/refactor` — Refactoring

```yaml
Inputs:   (uses current file or selection)
Agent:    Designer
Tools:    editFiles, codebase
Use:      Identify code smells and apply refactorings
Output:   Before/after code → explanation of each change
```

#### `/explain` — File Explanation

```yaml
Inputs:   (uses current file)
Agent:    Ask
Tools:    codebase
Use:      Beginner-friendly walkthrough of the file's purpose and structure
Output:   Purpose → structure → key concepts → improvements
```

#### `/debug` — Bug Investigation

```yaml
Inputs:   (uses current file + error context)
Agent:    Debugger
Tools:    search, codebase, debugger, terminal
Use:      Hypothesis-driven debugging with root cause analysis
Output:   Hypotheses → root cause → fix → prevention
```

#### `/impact` — Change Impact Analysis

```yaml
Inputs:   (description of planned change)
Agent:    Impact-Analyzer
Tools:    search, codebase, usages, problems
Use:      Trace ripple effects of a code change
Output:   Affected files → risk level → breaking changes → test gaps
```

---

### Copilot Customization

> **Preview Feature (March 2026):** The `/create-agent` command uses both the **built-in VS Code wizard** and this project's prompt template. See [copilot-mcp-preview.md](copilot-mcp-preview.md) for all new Copilot features.

#### `/create-agent` — Scaffold a Custom Agent

```yaml
Inputs:   agentName (e.g., Security-Reviewer), purpose (one sentence),
          tools (search/codebase/editFiles/terminal/fetch/all),
          depth (focused/balanced/broad)
Agent:    Copilot
Tools:    editFiles, codebase
Use:      Generate a .github/agents/<name>.agent.md file with YAML frontmatter,
          tool restrictions, model pinning, handoff chains, and a persona scaffold
Example:  /create-agent → Security-Reviewer → "Review for OWASP Top 10" → search,codebase → focused
Output:   .github/agents/security-reviewer.agent.md with full persona instructions
Built-in: VS Code also has a native /create-agent wizard in Copilot Chat (Ctrl+Shift+I → /create-agent)
File:     .github/prompts/create-agent.prompt.md
Docs:     .github/agents/README.md, .github/docs/copilot-mcp-preview.md
After:    Add the new agent to agents/README.md table and copilot-instructions.md <agents> block
```

#### `/mcp-to-skill` — Migrate an MCP Tool to a Copilot Skill

```yaml
Inputs:   target     (MCP server name, Java file path, or tool description),
          mode       (analyse / generate / full),
          outputPath (output path for generated SKILL.md, default: .github/skills/<target>/SKILL.md)
Agent:    Copilot
Tools:    codebase, editFiles, search
Use:      Reads an MCP server implementation, applies the MCP-vs-Skill decision matrix,
          and either produces an analysis report or generates a complete SKILL.md replacement.
Modes:
  analyse  → Read implementation → output decision report + tool inventory table
  generate → Produce ready-to-commit SKILL.md (requires prior analyse or description)
  full     → analyse + generate + registration instructions in one pass
Example:  /mcp-to-skill → learning-resources → full
          /mcp-to-skill → mcp-servers/src/server/atlassian/ → analyse
Decision: Content static/repeatable? → Skill candidate
          External API / auth / computation required? → Keep as MCP
Output:   Decision report + generated SKILL.md + copilot-instructions.md registration steps
File:     .github/prompts/mcp-to-skill.prompt.md
Docs:     .github/docs/mcp-vs-skills.md — full decision guide + migration playbook
Tip:      Use 'analyse' first before committing to migration — some tools MUST stay as MCP
```

---

#### `/copilot-customization` — Create, Review, or Compose Any Customization File

```yaml
Inputs:   goal    (create-new / review-existing / compare-types / plan-composition /
                   explain-concept / audit-repo / token-audit / test-activation / port-to-repo)
          type    (copilot-instructions / instructions / prompt / agent / skill / mcp /
                   all-types / not-sure)
          domain  (java, security, devops, mcp, git, or any custom topic)
          level   (newbie / amateur / pro)
Agent:    Copilot
Tools:    codebase, editFiles, search
Use:      Swiss-army tool for all Copilot customization work:
            - compare-types    → Show the 6-primitive comparison table + decision matrix
            - create-new       → Scaffold a complete, ready-to-use file of any type
            - review-existing  → Audit an existing customization file for common issues
            - plan-composition → Recommend which types to combine for a use case
            - explain-concept  → Teach the 6 primitives at newbie/amateur/pro depth
            - audit-repo       → Scan .github/ and produce a prioritized action plan
            - token-audit      → Estimate token cost of your current customization stack
            - test-activation  → Generate test scripts to verify customizations activate correctly
            - port-to-repo     → Plan how to copy customizations to a different repository
Example:  /copilot-customization → create-new → skill → devops → amateur
Output:   A complete, paste-ready .github/skills/devops/SKILL.md file
File:     .github/prompts/copilot-customization.prompt.md
Docs:     .github/docs/copilot-customization-deep-dive.md (full 18-part reference, incl. latest 2026 features)
          .github/docs/copilot-primitives-crosswalk.md (quick side-by-side comparison + decision flowchart)
          .github/docs/primitives-at-a-glance.md (one-page cheatsheet)
          .github/skills/copilot-customization/SKILL.md (domain knowledge)
          .github/docs/export-guide.md (export to work/personal repos)
          .github/docs/export-newbie-guide.md (newbie export walkthrough)
Tips:     - Use 'all-types' or 'not-sure' as type when unsure which primitive to use
          - Use 'audit-repo' to get a full inventory of your current customizations
          - Use 'plan-composition' before building a complex stack of multiple types
          - Use 'token-audit' to check context window overhead of your stack
          - Use 'test-activation' to verify skills, instructions, and agents work properly
          - Use 'port-to-repo' to copy your setup to work or personal repos
```

---

#### `/write-docs` — Create or Update Any Documentation

```yaml
Inputs:   docType   (start-here / dev-doc / guide / cheatsheet / quick-guide / skill /
                     prompt / alias-command / readme / brain-note / all-of-above)
          source    (inbox-notes / session-notes / concept-description / code-analysis / url / i-will-describe)
          topic     (the subject, domain, or title for the doc)
          level     (solo-dev / team / newbie / amateur / pro / 3-tier)
Agent:    Copilot
Tools:    codebase, editFiles, search
Use:      Doc factory — turn any raw content (inbox notes, session slides, a concept description,
          code analysis, or a URL) into a properly structured markdown doc:
            - start-here      → Entry-point onboarding file with 🟢🟡🔴 quick wins
            - dev-doc         → Full 3-tier developer reference (Newbie/Amateur/Pro)
            - guide           → Task-oriented step-by-step guide with troubleshooting
            - cheatsheet      → Fast-reference tables + decision tree, no prose
            - quick-guide     → Compact single-tier "do this now" doc
            - skill           → SKILL.md with activation-optimized description field
            - prompt          → Slash command .prompt.md with ${input:} variables
            - readme          → Component/directory README.md
            - brain-note      → Structured note for brain/ai-brain/notes/
            - all-of-above    → Full stack: brain-note + dev-doc + cheatsheet + skill + prompt
Example:  /write-docs → all-of-above → GitHub Copilot Mermaid Technique → team → inbox-notes
Output:   Multiple ready-to-commit .md files covering the topic end-to-end
File:     .github/prompts/write-docs.prompt.md
Docs:     .github/skills/copilot-customization/SKILL.md (for skill/prompt types)
          .github/docs/copilot-customization-deep-dive.md (for full reference)
Tips:     - Use 'all-of-above' when processing inbox notes from a session or reading
          - Use 'brain-note' for quick capture after a learning session
          - Use 'skill' to teach Copilot a new domain permanently
          - The 'description' field in SKILL.md is the most important line — prompt will craft it carefully
```

---

### Career & Interview

#### `/career-roles` — Career Exploration

```yaml
Inputs:   role, action (overview/skills/pay/compare/roadmap/interview-prep)
Agent:    Learning-Mentor
Tools:    codebase, fetch
Example:  /career-roles → MLE → overview
Roles:    SDE, MLE, DevOps, SRE, Architect, Tech Lead, EM, TPM, QA, and more
Pay:      Uses web scraping (levels.fyi, glassdoor) for live data
```

#### `/interview-prep` — Interview Preparation

```yaml
Inputs:   type (dsa/system-design/behavioral), topic, company (optional)
Agent:    Learning-Mentor
Tools:    codebase, fetch
Example:  /interview-prep → DSA → sliding-window → Google
Output:   Patterns → template solutions → company-specific tips → mock questions
```

---

### Daily Life

#### `/daily-assist` — Daily Productivity

```yaml
Inputs:   category (finance/productivity/news/research)
Agent:    Daily-Assistant
Tools:    search, fetch
Example:  /daily-assist → finance → budget tracking
Topics:   Budget, investments, habit tracking, time mgmt, tech news, web research
```

---

### Brain Workspace

Commands for managing the `brain/ai-brain/` personal knowledge workspace — five-tier system (inbox → notes → library → sessions → backlog).

#### `/brain-new` — Create a Note

```yaml
Inputs:   topic (what to capture), tier (inbox/notes), project (e.g., mcp-servers, java, general)
Agent:    Copilot
Tools:    editFiles, codebase
Use:      Create a new markdown note with proper frontmatter in inbox/ or notes/
Example:  /brain-new → "Java generics cheatsheet" → notes → java
Output:   YYYY-MM-DD_<slug>.md created with kind, tags, status frontmatter
File:     brain/ai-brain/inbox/ or brain/ai-brain/notes/ (never creates library/ entries directly)
Tip:      Use /brain-publish to promote to library/ and commit
```

#### `/brain-publish` — Publish an Imported Source

```yaml
Inputs:   file (relative to brain/ai-brain/, e.g. inbox/GHCP_Agents_Guide.md), project bucket
Agent:    Copilot
Tools:    editFiles, codebase, runCommands
Use:      Publish an imported external source from inbox/ to library/ → add frontmatter → tag it → git commit
Example:  /brain-publish → inbox/GHCP_Agents_Guide.md → ghcp-knowledge-sharing
Output:   File archived to brain/ai-brain/library/<project>/<YYYY-MM>/YYYY-MM-DD_slug.md, committed
Tip:      For notes YOU wrote, use /brain-new → notes/ instead
```

#### `/brain-search` — Search Notes

```yaml
Inputs:   query (free text), filters: tag, project, kind, date (YYYY-MM), tier
Agent:    Copilot
Tools:    codebase, search
Use:      Find notes by tag, project, kind, date range, or full-text across all tiers
Example:  /brain-search → "generics" → tag=java → tier=library
Output:   Matching notes with frontmatter summary and file paths
Kinds:    note | decision | session | resource | snippet | ref
Tiers:    inbox | notes | library | all (default)
```

#### `/brain-capture-session` — Capture AI Session as a Note

```yaml
Inputs:   topic (what the session was about), project, depth (quick / full)
Agent:    Copilot
Tools:    editFiles, codebase
Use:      Convert the current Copilot session's outputs into a structured session note
          in brain/ai-brain/inbox/, ready to review and publish
Output:   YYYY-MM-DD_session-<slug>.md with TL;DR, Key Insights, Code Snippets,
          Decisions, Follow-ups — all scaffolded from session context
Example:  /brain-capture-session → "Java generics and wildcards" → java → full
Tip:      Run at the end of any substantial Copilot session to capture what was learned
Next:     brain publish brain\ai-brain\inbox\<file>.md --project <bucket>
File:     .github/prompts/brain-capture-session.prompt.md
```

#### `/session-scope` — Manage Session Scope

```yaml
Inputs:   action (status / widen / narrow / switch / split / link / history),
          project (for narrow/switch), feature (for narrow/switch to feature),
          target (session file path for link action)
Agent:    Copilot
Tools:    editFiles, codebase, search
Use:      Track, adjust, and cross-reference the scope level of chat sessions.
          Sessions can be global (general knowledge), project-scoped, or
          feature-scoped. Scope can change mid-session with transition logging.
Actions:  status — show current scope | widen — feature→project→global |
          narrow — global→project→feature | switch — jump to different context |
          split — fork scope segment into its own session file |
          link — cross-reference another session | history — show transitions
Example:  /session-scope → status
          /session-scope → widen
          /session-scope → narrow → abs-development → user-auth-flow
          /session-scope → split
File:     .github/prompts/session-scope.prompt.md
Ref:      .github/instructions/session-scoping.instructions.md
Tip:      Use when requirements gathering naturally evolves into general research —
          widen the scope so the learning is discoverable beyond the original project
```

#### `/backlog` — Manage the Backlog (Advanced Operations)

```yaml
Inputs:   action (brainstorm / guide / refine / promote / epic / update / sprint / board),
          text (what to capture or the ID to act on)
Agent:    Copilot
Tools:    editFiles, codebase
Use:      Advanced backlog operations beyond everyday capture:
          • brainstorm — whiteboard-style exploration (IDEA-NNN)
          • guide — GHCP context playbook (GUIDE-NNN)
          • refine — add refinement pass to an idea
          • promote — idea→item with full enhancement (tags, AC, effort, epic)
          • epic — group related items (EPIC-NNN)
          • update — change status/priority (full board + CHANGELOG sync)
          • sprint — create, manage, and close sprints
          Every creation and update syncs: BOARD.md + views/ + CHANGELOG.md
Example:  /backlog → brainstorm → "How should we handle auth?"
          /backlog → guide → "How GHCP should format error messages"
          /backlog → refine → "IDEA-001"
          /backlog → promote → "IDEA-001"
          /backlog → epic → "Atlassian v2 migration"
Updates:  BOARD.md, views/by-status.md, views/by-priority.md,
          views/by-project.md, CHANGELOG.md (entry + stats)
File:     .github/prompts/backlog.prompt.md
Ref:      .github/instructions/backlog.instructions.md,
          brain/ai-brain/backlog/guides/jot-down-guide.md
Tip:      Use /jot for capture, /todos for the board. Use /backlog only for
          advanced operations (refine, promote, brainstorm, guide, epic, sprint).
```

---

### Brain PKM — Git-Inspired Content Operations

Commands for managing content flow between external capture sources (Notion, Keep,
Confluence, etc.) and brain/ai-brain tiers. Uses git/VCS-inspired verbs.

#### `/brain-fetch` — Fetch Content from Source

```yaml
Inputs:   source (notion, keep, confluence-personal, etc.), scope (what to fetch)
Agent:    Copilot
Tools:    editFiles, codebase, fetch
Use:      First-time retrieval from an external capture source → lands in inbox/
Analogy:  git fetch — download without merging
Example:  /brain-fetch → notion → "all L0 content"
Output:   Markdown files in brain/ai-brain/inbox/ with origin frontmatter
Next:     /brain-merge to route items to proper tiers
File:     .github/prompts/brain-fetch.prompt.md
```

#### `/brain-pull` — Pull Updates from Source

```yaml
Inputs:   source (notion, keep, confluence-personal, etc.)
Agent:    Copilot
Tools:    editFiles, codebase, fetch
Use:      Update previously fetched content — re-reads source, merges changes into existing brain files
Analogy:  git pull — fetch + merge in one step
Example:  /brain-pull → notion
Output:   Updated brain files + new items in inbox/
File:     .github/prompts/brain-pull.prompt.md
```

#### `/brain-clone` — Full Import of Entire Source

```yaml
Inputs:   source (notion, confluence-personal, onenote, etc.), tier (library/notes)
Agent:    Copilot
Tools:    editFiles, codebase, fetch
Use:      Full structured import of an entire source — creates a mirror in library/ or notes/
Analogy:  git clone — complete copy with structure preserved
Example:  /brain-clone → notion → library
Output:   brain/ai-brain/library/<source>/ with structured markdown files + README.md index
File:     .github/prompts/brain-clone.prompt.md
```

#### `/brain-cherry-pick` — Import One Specific Item

```yaml
Inputs:   source, item (page title, URL, or identifier)
Agent:    Copilot
Tools:    editFiles, codebase, fetch
Use:      Selectively import ONE specific item from a source into inbox/
Analogy:  git cherry-pick — select one commit to apply
Example:  /brain-cherry-pick → confluence-personal → "API Design Notes"
Output:   Single file in inbox/ with cherry-picked marker
File:     .github/prompts/brain-cherry-pick.prompt.md
```

#### `/brain-merge` — Route Inbox Item to Tier

```yaml
Inputs:   item (filename or "all" to process entire inbox)
Agent:    Copilot
Tools:    editFiles, codebase
Use:      Route inbox items to their proper permanent tier after review
Analogy:  git merge — integrate changes into a branch
Example:  /brain-merge → "all"
Output:   Items moved from inbox/ to notes/, library/, or backlog/ with updated frontmatter
File:     .github/prompts/brain-merge.prompt.md
```

#### `/brain-stash` — Park Content for Later

```yaml
Inputs:   item (filename or "all"), action (stash/pop/list — default: stash)
Agent:    Copilot
Tools:    editFiles, codebase
Use:      Temporarily park inbox content that isn't ready for routing
Analogy:  git stash — save work-in-progress for later
Example:  /brain-stash → "some-note.md"
          /brain-stash → pop
Output:   Item stays in inbox/ with stashed marker, or unstashed for processing
File:     .github/prompts/brain-stash.prompt.md
```

#### `/brain-diff` — Compare Brain vs Source

```yaml
Inputs:   source (notion, keep, confluence-personal, etc.)
Agent:    Copilot
Tools:    editFiles, codebase, fetch
Use:      Show what's changed between brain content and external source since last pull
Analogy:  git diff — compare working directory with remote
Example:  /brain-diff → notion
Output:   Summary of modified, new, and deleted items (read-only — no changes made)
File:     .github/prompts/brain-diff.prompt.md
```

#### `/brain-push` — Export Brain Content to Source

```yaml
Inputs:   source, content (file path or "all modified since last push")
Agent:    Copilot
Tools:    editFiles, codebase, fetch
Use:      Export brain content back to an external destination
Analogy:  git push — send local changes to remote
Example:  /brain-push → notion → "notes/java-streams-cheatsheet.md"
Output:   Content formatted and exported (or copy-paste instructions provided)
File:     .github/prompts/brain-push.prompt.md
```

#### `/brain-remote` — List Capture Sources

```yaml
Inputs:   (none)
Agent:    Copilot
Tools:    codebase
Use:      List all configured capture sources with sensitivity levels and last-accessed dates
Analogy:  git remote -v — show all remotes
Example:  /brain-remote
Output:   Table of work + personal sources with sensitivity and access status
File:     .github/prompts/brain-remote.prompt.md
```

#### `/brain-consolidate` — Brain Consolidation Planner

```yaml
Inputs:   source (or "all" for overview), mode (plan/execute/status)
Agent:    Copilot
Tools:    editFiles, codebase, fetch
Use:      Plan and execute migration of scattered external notes into brain/ai-brain
Example:  /brain-consolidate → notion → plan
          /brain-consolidate → all → status
Output:   Migration plan, execution progress, or consolidation dashboard
File:     .github/prompts/brain-consolidate.prompt.md
```

---

### Backlog — Quick Commands

Daily-use shortcuts for the backlog system. These are the commands you'll use 90% of the time.

#### `/jot` — Universal Capture (Smart Classify + Enhance)

```yaml
Inputs:   thought (anything — idea, task, file path, URL, batch, or mix)
Agent:    Copilot
Tools:    editFiles, codebase, terminalLastCommand, runInTerminal
Use:      THE primary backlog capture command. Handles everything:
          • Auto-classifies: idea vs task vs bug vs research vs brainstorm vs batch
          • Reads local file paths (C:\notes\spec.txt) and extracts content
          • Records URLs as linked references
          • Dedup checks existing backlog before creating (skip/merge/cross-ref)
          • Enhances tasks with title, type, priority, tags, effort, AC, epic
          • Auto-breaks down L/XL tasks into sub-items
          • Cross-references related BLIs, IDEAs, EPICs, notes, sessions
          • Confirms with user before finalizing, offers refinement
          • Updates ALL boards: BOARD.md + views/ + CHANGELOG.md
          • Runs mandatory completeness check before finishing
Pipeline: Parse → Classify → Dedup/Merge → Enhance → Cross-Ref → Confirm → Sync → Check
Example:  /jot → "voice search for resource discovery"       (idea)
          /jot → "fix search bug"                            (task)
          /jot → "see E:\specs\auth-flow.md"                 (reads file, creates items)
          /jot → "add docker, CI, and k8s"                   (batch: 3 tasks)
          /jot → "research auth options https://oauth.net"   (task + URL)
          /jot → "how should we handle caching?"             (brainstorm)
Output:   Summary of all created items with classification, refs, and board sync
Updates:  BOARD.md, views/by-status.md, views/by-priority.md,
          views/by-project.md (if epic-linked), views/by-source.md,
          CHANGELOG.md, Epic files, cross-referenced items (bidirectional)
File:     .github/prompts/jot.prompt.md
Ref:      brain/ai-brain/backlog/guides/capture-workflow.md,
          brain/ai-brain/backlog/guides/jot-down-guide.md,
          .github/instructions/backlog.instructions.md
Tip:      This is your ONE command for capture. /todo is just an alias.
          Use /backlog only for advanced ops (refine, promote, etc.)
```

#### `/read-file-jot` — File-to-Backlog Extraction

```yaml
Inputs:   filepath (full path to a file — Notepad++, text, markdown, code)
          context (optional — project name, epic, priority override)
Agent:    Copilot
Tools:    editFiles, codebase, terminalLastCommand, runInTerminal
Use:      Dedicated file reading + extraction command:
          • Reads any local file (text, markdown, code, Notepad++ notes)
          • Checks IMPORT-LOG.md for re-import detection (warns if previously imported)
          • Assigns import batch (IMP-NNN) for tracking all items from the file
          • Parses bullets, numbered lists, headings, TODO markers, paragraphs
          • Extracts every actionable item and idea from the file content
          • Dedup checks existing backlog before creating (skip/merge/cross-ref)
          • Classifies each independently (task/bug/idea/brainstorm/research)
          • Enhances each with full protocol (title, type, priority, tags, AC)
          • Sets origin-type: file-import and import-batch: IMP-NNN on all items
          • Auto-refines: gap analysis, future considerations, grouping analysis
          • Presents summary — user can confirm, refine, select, or cancel
          • Creates all items + full board sync + IMPORT-LOG.md + completeness check
Workflow: Read → Dedup IMPORT-LOG → Parse → Classify → Dedup Backlog → Enhance → Refine → Confirm → Create → Sync → Check
Example:  /read-file-jot → "C:\notes\project-ideas.txt"
          /read-file-jot → "E:\specs\auth-requirements.md" + "for EPIC-001, all high"
          /read-file-jot → "~/todo.txt"
          /read-file-jot → "D:\code\app.java" (extracts TODO/FIXME comments)
Output:   Summary of extracted items (new/merged/skipped) with source reference
Updates:  Same as /jot — all boards, views, CHANGELOG, epics, cross-refs
          + IMPORT-LOG.md, views/by-source.md (import batch section)
File:     .github/prompts/read-file-jot.prompt.md
Ref:      brain/ai-brain/backlog/guides/capture-workflow.md,
          brain/ai-brain/backlog/guides/jot-down-guide.md,
          .github/instructions/backlog.instructions.md
Tip:      Use when you have a file full of ideas or TODOs. For inline text, use /jot.
```

#### `/todo` — Add a Task (Alias for `/jot`)

```yaml
Inputs:   task (describe what needs to be done)
Agent:    Copilot
Tools:    editFiles, codebase, terminalLastCommand, runInTerminal
Use:      Alias for /jot — pre-classified as a task. Routes through the unified
          capture pipeline with full enhancement: type, priority, tags, effort,
          AC, epic, auto-breakdown, cross-references, and board sync.
          Use /jot directly for the full experience (auto-classification).
Example:  /todo → "Fix search bug in vault"
          /todo → "Add docker, CI pipeline, and k8s deploy"
Updates:  Same as /jot — all boards, views, CHANGELOG, epics, cross-refs
File:     .github/prompts/todo.prompt.md
Ref:      .github/prompts/jot.prompt.md
Tip:      For anything beyond concrete tasks, use /jot instead — it auto-classifies.
```

#### `/todos` — View Board & Manage Status

```yaml
Inputs:   action (board by default, or a status command like "done BLI-003")
Agent:    Copilot
Tools:    editFiles, codebase
Use:      Your daily dashboard. View all tracked work, mark items done,
          start items, block items, search for entries.
          Every status change updates ALL boards + CHANGELOG + item Activity Log.
Commands: board — show full status | done BLI-NNN — mark complete (+ cycle time) |
          start BLI-NNN — begin work | block BLI-NNN — mark blocked |
          unblock BLI-NNN — resume | review BLI-NNN — send to review |
          archive BLI-NNN — archive | prioritise BLI-NNN high — change priority |
          park IDEA-NNN — shelve idea | find <text> — search entries |
          status — by-status view | projects — by-project view |
          priority — by-priority view | log — recent CHANGELOG | stats — velocity
Example:  /todos                          (show the board)
          /todos → "done BLI-003"         (mark complete, calc cycle time)
          /todos → "start BLI-005"        (begin work, record started date)
          /todos → "done BLI-003, BLI-004" (batch update all boards)
Updates:  Item file (frontmatter + Activity Log + Time Tracking),
          BOARD.md, views/by-status.md, views/by-priority.md,
          views/by-project.md, CHANGELOG.md (entry + stats),
          Epic progress (if linked)
File:     .github/prompts/todos.prompt.md
Tip:      Use at the start of a work session to see what's next, and at the end
          to mark what's done
```

---

### Quality & Standards

#### `/check-standards` — Audit Files Against Best Practices

```yaml
Inputs:   target (file path, folder, or filename to audit),
          domain (auto / brain-naming / markdown-frontmatter / file-structure /
                  git-commits / pkm / prompt-file / skill-file / java-code / all)
Agent:    Copilot
Tools:    codebase, search
Use:      Run a structured compliance audit against best practices and industry standards.
          Outputs a severity-tagged report: ❌ Error | ⚠️ Warning | 💡 Suggestion
Example:  /check-standards → brain/ai-brain/notes/ → brain-naming
          /check-standards → .github/skills/mac-dev/SKILL.md → skill-file
          /check-standards → .github/prompts/debug.prompt.md → prompt-file
          /check-standards → . → all    (audit entire workspace)
Domains:
  brain-naming       → ISO 8601 date prefix, lowercase-hyphens slug, kind prefix, ≤50 chars
  markdown-frontmatter → Required fields, allowed values, tag rules
  file-structure     → notes/ for writing, library/ for imports, README.md in each tier
  git-commits        → brain: prefix, ≤72 chars, imperative mood, attribution footer
  pkm                → Atomic notes (<300 lines), consistent tags, cross-references
  prompt-file        → Frontmatter completeness, description, input variables, agent/tools
  skill-file         → Frontmatter, 3-tier structure, cheatsheet, resources, file location
  java-code          → Naming, method length, Javadoc, exception specificity
Output:   Compliance report table + prioritised fix suggestions
File:     .github/prompts/check-standards.prompt.md
Skill:    brain-management/SKILL.md (standards reference)
Tip:      Run after creating any new file with /write-docs or /brain-new to catch violations
          before committing
```

#### `/mcp` — Learn & Build MCP Servers

```yaml
Inputs:   topic (overview/build-server/configure-agent/types-of-mcp/api-integration/
                  agent-patterns/protocol-spec/troubleshoot/real-world-examples),
          goal (learn-concept/build-my-own-mcp/configure-vscode/agent-architecture/
                compare-approaches/interview-prep),
          level (beginner/know-the-basics/intermediate/advanced),
          language (typescript/python/java/go/csharp/any)
Agent:    Learning-Mentor
Tools:    codebase, fetch
Use:      Comprehensive MCP (Model Context Protocol) guide — from protocol basics to building
          your own server, combining with REST/GraphQL APIs, multi-agent patterns, security, deployment
Example:  /mcp → build-server → java → intermediate
Output:   Protocol architecture → primitives (tools/resources/prompts) → full server code →
          transport configuration → testing with MCP Inspector → deployment
Skill:    Backed by mcp-development/SKILL.md (1,980 lines of reference material)
Local:    Project MCP servers live in mcp-servers/ — see mcp-servers/README.md
```

---

## 🏷️ Aliases & Shortcuts

Some commands serve as aliases or shorthand for common workflows:

### Conceptual Aliases (same underlying learning engine)

| Shorthand | Equivalent Long-Form | When to Use |
|---|---|---|
| `/dsa` | `/learn-concept` → DSA topic | When learning data structures & algorithms specifically |
| `/system-design` | `/learn-concept` → system design topic | When learning system design specifically |
| `/devops` | `/learn-concept` → DevOps topic | When learning DevOps tools specifically |
| `/teach` | `/learn-concept` (with current file as context) | When you want to learn from code, not theory |

### Meta Aliases (control behavior)

| Shorthand | What It Controls |
|---|---|
| `/scope generic` | Subsequent commands use theory-only mode |
| `/scope specific` | Subsequent commands apply to your codebase |
| `/context continue` | Reconnect to prior session's decisions |
| `/context fresh` | Start clean, ignore prior context |

### Composition Shortcuts

| Type This | What Happens |
|---|---|
| `/composite refactor, design-review` | Combined code quality analysis |
| `/composite debug, impact` | Debug + fix impact in one pass |
| `/composite refactor, impact, teach` | Refactor + impact + explain for learning |

### Hub Shortcuts (sub-categories)

| Type | What It Shows |
|---|---|
| `/hub se` | All software engineering commands |
| `/hub dsa` | DSA commands branch |
| `/hub system-design` | System design branch |
| `/hub devops` | DevOps branch (includes Git, build tools) |
| `/hub industry` | Industry concepts: rate limiting, circuit breakers, event-driven |
| `/hub trends` | Tech trends: AI coding, transformers, Wasm, platform eng |
| `/hub daily` | Daily assistant branch |
| `/hub resources` | Resources branch: search, browse, scrape, recommend |
| `/hub career` | Career commands branch |
| `/hub quality` | Code quality commands |
| `/hub debug` | Debugging commands |
| `/hub cs` | CS fundamentals branch |
| `/hub language` | Language learning branch |
| `/hub mac` | macOS dev environment branch (Homebrew, JDK, npm, IDEs, Docker) |

---

## 📝 Input Parameters Reference

Most commands prompt you for input values. Here's what each common parameter expects:

| Parameter | Accepted Values | Default |
|---|---|---|
| `topic` | Any concept name (free text) | — |
| `mode` | `learn-concept`, `solve-pattern`, `practice-problems`, `compare-structures`, `complexity-analysis`, `interview-roadmap` | `learn-concept` |
| `language` | `java`, `python`, `cpp`, `go`, `rust`, `javascript`, `typescript`, `pseudocode` | `java` |
| `level` | `beginner`, `intermediate`, `advanced`, `contest-level` | `intermediate` |
| `depth` | `overview`, `detailed`, `exhaustive` | `detailed` |
| `scope` | `generic`, `specific` | `generic` |
| `action` | Varies per command (e.g., `save-state`, `resume`, `continue`, `fresh`) | — |
| `category` | `se`, `dsa`, `system-design`, `devops`, `daily`, `career`, `quality`, `debug`, `cs`, `language` | — |

---

## 🔀 Composition Patterns

### By Goal

| Goal | Commands Chain | Description |
|---|---|---|
| **Learn → Practice → Interview** | `/learn-concept` → `/dsa` → `/interview-prep` | Concept → algorithm patterns → mock interview |
| **Career → Skills → Study** | `/career-roles` → `/language-guide` → `/reading-plan` | Identify role → learn skills → build study plan |
| **Understand → Improve → Verify** | `/explain` → `/refactor` → `/impact` | Read code → fix it → check impact |
| **Design → Review → Build** | `/system-design` → `/design-review` → `/refactor` | Design system → review → implement |
| **Debug → Analyze → Learn** | `/debug` → `/impact` → `/teach` | Fix bug → assess impact → understand why |
| **Compare → Choose → Learn** | `/tech-stack compare` → `/language-guide` → `/devops` | Compare tools → pick one → go deep |
| **Session 1 → Session 2** | `/multi-session save-state` → `/multi-session resume` | Persist and continue across chats |

### Combining with Meta Commands

```text
/scope → generic     (theory mode)
/context → continue  (reconnect to prior)
/dsa → binary trees  (now taught as pure theory, building on prior context)
```

```text
/scope → specific      (code mode)
/composite → refactor, design-review, impact
  → Applied to your codebase, combining three analyses
```

---

## 🤖 Agent Pairing Guide

Each command is pre-configured with the best agent, but you can override by selecting a different agent from the dropdown before typing the command.

| Agent | Best Paired With | Why |
|---|---|---|
| **Learning-Mentor** | `/learn-concept`, `/dsa`, `/system-design`, `/teach`, `/deep-dive` | Teaching persona — analogies, step-by-step, exercises |
| **Designer** | `/design-review`, `/refactor` | Architecture expertise — SOLID, patterns, clean code |
| **Debugger** | `/debug` | Hypothesis-driven debugging, terminal access |
| **Impact-Analyzer** | `/impact` | Ripple effect tracing, dependency mapping |
| **Code-Reviewer** | `/explain` (manual override) | Read-only review, style checks |
| **Daily-Assistant** | `/daily-assist` | Non-SE tasks — finance, productivity, news |
| **Thinking-Beast-Mode** | Complex research tasks (no dedicated prompt) | Autonomous deep research with web fetching |
| **Ask (default)** | `/explain`, quick questions | Lightweight, good for simple tasks |

---

## 💡 Tips & Best Practices

### Getting the Best Results

1. **Be specific with inputs** — `/dsa → sliding window → solve-pattern → python → intermediate` gets better results than just `/dsa`
2. **Use `/hub` when lost** — it shows the full command tree with examples
3. **Chain meta + task commands** — set `/scope` and `/context` before your main command
4. **Save state regularly** — `/multi-session save-state` before long sessions
5. **Open the right file first** — code quality commands (`/debug`, `/refactor`, `/design-review`) use the active file

### Common Workflows

| Scenario | What to Type |
|---|---|
| I want to learn something from scratch | `/learn-concept` → topic |
| I need to practice for interviews | `/interview-prep` → DSA → topic |
| I want to understand this code | `/explain` (with file open) |
| I'm debugging something | `/debug` (with file open + error context) |
| I want a study plan for the next month | `/reading-plan` → topic → 4 weeks |
| I want to compare React vs Vue | `/tech-stack` → compare React vs Vue |
| I want to know what MLE does | `/career-roles` → MLE → overview |
| I want to continue from yesterday | `/multi-session` → resume |

### Files Behind the Commands

Every slash command maps to a `.prompt.md` file in `.github/prompts/`:

```text
/dsa           ←→  .github/prompts/dsa.prompt.md
/system-design ←→  .github/prompts/system-design.prompt.md
/hub           ←→  .github/prompts/hub.prompt.md
...etc
```

To **customize a command**, edit its `.prompt.md` file. Changes take effect immediately on the next invocation.

---

## 🗺️ Related Docs

| Doc | What It Covers |
|---|---|
| [Navigation Index](navigation-index.md) | Master index — commands, agents, skills, file map |
| [Prompts Guide](../prompts/README.md) | How to create & edit prompt files |
| [Agents Guide](../agents/README.md) | How agents work, tools, handoffs |
| [Getting Started](getting-started.md) | Hands-on tutorial for beginners |
| [Customization Guide](customization-guide.md) | Architecture deep-dive |

---

<p align="center">

[← Main README](../README.md) · [Navigation Index](navigation-index.md) · [Prompts Guide](../prompts/README.md) · [Getting Started](getting-started.md)

</p>
