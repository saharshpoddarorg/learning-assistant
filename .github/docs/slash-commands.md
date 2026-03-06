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

```
You type:    /dsa → binary-search → learn-concept → python → intermediate
Copilot:     Uses Learning-Mentor agent + DSA prompt template
Result:      Full binary search lesson with Python code, complexity analysis, practice problems
```

> **Under the hood:** Each `/command` is a `.prompt.md` file in `.github/prompts/`. The YAML frontmatter selects the agent and tools. The body contains the task instructions that Copilot follows.

---

## 📋 All Commands at a Glance

### Quick Lookup (39 commands)

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
| 27 | `/resources` | Domain | Search, browse & scrape curated learning resources | Learning-Mentor |
| 28 | `/daily-assist` | Daily Life | Finance, productivity, news, research | Daily-Assistant |
| 29 | `/brain-new` | Brain Workspace | Create a new knowledge note (inbox or notes tier) | Copilot |
| 30 | `/brain-publish` | Brain Workspace | Publish an imported source to library/ with tag prompting and git commit | Copilot |
| 31 | `/brain-search` | Brain Workspace | Search notes by tag, project, kind, date, or full text | Copilot |
| 32 | `/brain-capture-session` | Brain Workspace | Convert current AI session into a structured session note | Copilot |
| 33 | `/git-vcs` | Domain | Git workflows, branching strategies, commit conventions, semver | Learning-Mentor |
| 34 | `/build-tools` | Domain | Maven, Gradle, Make, Bazel, npm — lifecycle & dependency management | Learning-Mentor |
| 35 | `/mac-dev` | Domain | macOS dev environment — Homebrew, JDK, npm, IDEs, Docker, shell, dotfiles | Learning-Mentor |
| 36 | `/digital-notetaking` | Domain | PKM systems (PARA, CODE, Zettelkasten), tools (Notion, Obsidian, Logseq, OneNote), migration & JDK upgrade | Learning-Mentor |
| 37 | `/create-agent` | Customization | Scaffold a new Copilot custom agent (.agent.md) with guided inputs | Copilot |
| 38 | `/copilot-customization` | Customization | Create, review, compare, or compose any Copilot customization file (instructions/prompts/skills/agents/MCP) | Copilot |
| 39 | `/write-docs` | Meta | Create or update any doc, guide, brain-note, cheatsheet, start-here, skill, or slash command from provided content | Copilot |

> **What's New (March 2026 — Open Preview):** GitHub Copilot MCP is now in **open preview** for all subscribers.
> VS Code also gained a **built-in `/create-agent` wizard** in Copilot Chat. See [copilot-mcp-preview.md](copilot-mcp-preview.md) for the full changelog.

---

## 📖 Command Details by Category

### Navigation & Meta

#### `/hub` — Master Navigation Index
```
Inputs:   category (e.g., se, dsa, system-design, devops, daily, career)
Agent:    Learning-Mentor
Tools:    codebase, fetch
Use:      Browse all available commands organized by category tree
Tip:      Start here if you're lost — type /hub and pick a branch
```

#### `/composite` — Combine Multiple Modes
```
Inputs:   modes (e.g., refactor, design-review, impact)
Agent:    Agent
Use:      Run 2+ analysis modes in one session with deduplicated output
Example:  /composite → refactor, design-review → unified analysis
Tip:      Great for code quality passes — avoids repeating analysis
```

#### `/context` — Continue or Start Fresh
```
Inputs:   action (continue / fresh)
Agent:    Agent
Use:      Reconnect to a prior conversation's decisions, or explicitly reset
Tip:      Use "continue" after switching files or taking a break
```

#### `/scope` — Generic vs Code-Specific
```
Inputs:   scope (generic / specific)
Agent:    Agent
Use:      Tell Copilot whether you want pure theory or code-applied learning
Example:  /scope → generic + /learn-concept → SOLID = theory lesson
          /scope → specific + /learn-concept → SOLID = applied to your codebase
```

#### `/multi-session` — Cross-Session State
```
Inputs:   action (save-state / resume / handoff / status)
Agent:    Agent
Use:      Persist context across multiple chat sessions via checkpoint file
Tip:      Always save-state before a long session hits token limits
File:     Creates/updates .github/session-state.md
```

#### `/steer` — Steering Mode Navigator
```
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
```
Inputs:   concept, language (optional), depth (overview/detailed/exhaustive)
Agent:    Learning-Mentor
Tools:    codebase, fetch
Use:      Learn any CS/SE concept from scratch — language-agnostic
Example:  /learn-concept → deadlocks → java → detailed
Output:   Definition → Why → Analogy → How → Code → Anti-example → Mistakes → Next
```

#### `/deep-dive` — Progressive Exploration
```
Inputs:   concept, starting-level
Agent:    Learning-Mentor
Tools:    codebase, fetch
Use:      Peel back layers — each round goes deeper
Example:  /deep-dive → generics
          Layer 1: What are generics? Layer 2: Type erasure. Layer 3: Wildcards. Layer 4: Variance.
```

#### `/learn-from-docs` — Official Documentation
```
Inputs:   concept, docs-source (optional)
Agent:    Learning-Mentor
Tools:    codebase, fetch
Use:      Copilot fetches + translates official docs into plain language
Example:  /learn-from-docs → sealed classes → JEP 409
Tip:      Specify the exact doc/JEP/RFC for better results
```

#### `/reading-plan` — Structured Study Plan
```
Inputs:   topic, duration, level
Agent:    Learning-Mentor
Tools:    codebase, fetch
Use:      Get a week-by-week study plan with curated resources
Example:  /reading-plan → design patterns → 4 weeks → intermediate
Output:   Sequenced plan with books, tutorials, exercises, milestones
```

#### `/teach` — Learn From Code
```
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
```
Inputs:   topic, mode, language, level
Modes:    learn-concept | solve-pattern | practice-problems | compare-structures |
          complexity-analysis | interview-roadmap
Agent:    Learning-Mentor
Tools:    codebase, fetch
Example:  /dsa → sliding-window → solve-pattern → python → intermediate
Output:   Pattern description → signal words → template → trace → complexity → problems
```

#### `/system-design` — System Design (HLD/LLD)
```
Inputs:   topic, level (hld/lld/both/concept/interview-practice), depth
Agent:    Learning-Mentor
Tools:    codebase, fetch
Example:  /system-design → url-shortener → hld → detailed
Output:   Requirements → estimation → architecture diagram → deep-dive → trade-offs
```

#### `/devops` — DevOps & Infrastructure
```
Inputs:   topic, depth
Agent:    Learning-Mentor
Tools:    codebase, fetch
Example:  /devops → Docker → detailed
Topics:   CI/CD, containers, Kubernetes, cloud, IaC, monitoring,
          build tools (Maven/Gradle/Ant), Git commands & branching strategies,
          deployment strategies, GitOps, feature flags
```

#### `/language-guide` — Language Learning
```
Inputs:   language, focus-area, level
Agent:    Learning-Mentor
Tools:    codebase, fetch
Example:  /language-guide → Rust → ownership → beginner
Languages: Java, Python, C++, Go, Rust, JavaScript/TypeScript, and more
```

#### `/tech-stack` — Frameworks & Tools
```
Inputs:   topic, action (learn/compare/evaluate)
Agent:    Learning-Mentor
Tools:    codebase, fetch
Example:  /tech-stack → compare Spring vs FastAPI
```

#### `/sdlc` — Software Development Lifecycle
```
Inputs:   topic, depth
Agent:    Learning-Mentor
Tools:    codebase, fetch
Example:  /sdlc → testing → TDD
Topics:   Agile, Scrum, Kanban, XP, SAFe, test pyramid, CI/CD practices,
          E2E phases (planning, design, dev, testing, deployment, monitoring, maintenance),
          frontend/UI/UX aspects, backend aspects, database aspects
```

#### `/explore-project` — Open Source Study
```
Inputs:   project, focus
Agent:    Learning-Mentor
Tools:    codebase, fetch
Example:  /explore-project → Redis → event loop architecture
Output:   Repository overview → architecture map → key patterns → lessons
```

#### `/resources` — Learning Resource Vault
```
Inputs:   action (search/browse/scrape/recommend/add/details/discover/export), topic/URL, filters (optional)
Agent:    Learning-Mentor
Tools:    codebase, fetch
Example:  /resources → search → java concurrency
          /resources → discover → learn testing → (auto: exploratory mode)
          /resources → discover → mode=specific → "JUnit 5 docs"
          /resources → discover → domain=core-cs
          /resources → export → java → format=pdf
Actions:  search (keyword/tag), browse (by category), scrape (any URL),
          recommend (topic-based), add (new resource), details (deep-dive),
          discover (3 modes: specific/vague/exploratory), export (md/pdf/word)
Vault:    ~100+ curated resources across Java, Python, Web, AI/ML, DevOps,
          Git/VCS, build tools, algorithms & data structures (8 DS-specific
          resources: Java Collections, OpenDSA, VisuAlgo, Neetcode, MIT 6.006,
          GeeksforGeeks, Python DS docs, Algorithm Design Manual),
          engineering, databases, security,
          digital note-taking & PKM (15 resources incl. BASB, PARA, Zettelkasten,
          Obsidian, Notion, Logseq, Foam, GTD, Progressive Summarization), and more
MCP:      Backed by 10 MCP tools: search_resources, browse_vault, get_resource,
          list_categories, discover_resources, scrape_url, read_url, add_resource,
          add_resource_from_url, export_results
Enums:    ResourceType (11 types incl. PLAYLIST), SearchMode (specific/vague/exploratory),
          ConceptDomain (8 domains), ConceptArea (36 concepts), DifficultyLevel, LanguageApplicability
```

#### `/git-vcs` — Git & Version Control
```
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

#### `/build-tools` — Build Automation
```
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
```
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
```
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
```
Inputs:   (uses current file)
Agent:    Designer
Tools:    search, codebase, usages, fetch
Use:      SOLID/GRASP design review with severity ratings
Output:   Findings table → recommendations → refactoring suggestions
Tip:      Open the file first, then run /design-review
```

#### `/refactor` — Refactoring
```
Inputs:   (uses current file or selection)
Agent:    Designer
Tools:    editFiles, codebase
Use:      Identify code smells and apply refactorings
Output:   Before/after code → explanation of each change
```

#### `/explain` — File Explanation
```
Inputs:   (uses current file)
Agent:    Ask
Tools:    codebase
Use:      Beginner-friendly walkthrough of the file's purpose and structure
Output:   Purpose → structure → key concepts → improvements
```

#### `/debug` — Bug Investigation
```
Inputs:   (uses current file + error context)
Agent:    Debugger
Tools:    search, codebase, debugger, terminal
Use:      Hypothesis-driven debugging with root cause analysis
Output:   Hypotheses → root cause → fix → prevention
```

#### `/impact` — Change Impact Analysis
```
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
```
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

#### `/copilot-customization` — Create, Review, or Compose Any Customization File
```
Inputs:   goal    (create-new / review-existing / compare-types / plan-composition /
                   explain-concept / audit-repo)
          type    (copilot-instructions / instructions / prompt / agent / skill / mcp /
                   all-types / not-sure)
          domain  (java, security, devops, mcp, git, or any custom topic)
          level   (newbie / amateur / pro)
Agent:    Copilot
Tools:    codebase, editFiles, search
Use:      Swiss-army tool for all Copilot customization work:
            - compare-types   → Show the 6-primitive comparison table + decision matrix
            - create-new      → Scaffold a complete, ready-to-use file of any type
            - review-existing → Audit an existing customization file for common issues
            - plan-composition → Recommend which types to combine for a use case
            - explain-concept → Teach the 6 primitives at newbie/amateur/pro depth
            - audit-repo      → Scan .github/ and produce a prioritized action plan
Example:  /copilot-customization → create-new → skill → devops → amateur
Output:   A complete, paste-ready .github/skills/devops/SKILL.md file
File:     .github/prompts/copilot-customization.prompt.md
Docs:     .github/docs/copilot-customization-deep-dive.md (full reference)
          .github/skills/copilot-customization/SKILL.md (domain knowledge)
Tips:     - Use 'all-types' or 'not-sure' as type when unsure which primitive to use
          - Use 'audit-repo' to get a full inventory of your current customizations
          - Use 'plan-composition' before building a complex stack of multiple types
```

---

#### `/write-docs` — Create or Update Any Documentation
```
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
```
Inputs:   role, action (overview/skills/pay/compare/roadmap/interview-prep)
Agent:    Learning-Mentor
Tools:    codebase, fetch
Example:  /career-roles → MLE → overview
Roles:    SDE, MLE, DevOps, SRE, Architect, Tech Lead, EM, TPM, QA, and more
Pay:      Uses web scraping (levels.fyi, glassdoor) for live data
```

#### `/interview-prep` — Interview Preparation
```
Inputs:   type (dsa/system-design/behavioral), topic, company (optional)
Agent:    Learning-Mentor
Tools:    codebase, fetch
Example:  /interview-prep → DSA → sliding-window → Google
Output:   Patterns → template solutions → company-specific tips → mock questions
```

---

### Daily Life

#### `/daily-assist` — Daily Productivity
```
Inputs:   category (finance/productivity/news/research)
Agent:    Daily-Assistant
Tools:    search, fetch
Example:  /daily-assist → finance → budget tracking
Topics:   Budget, investments, habit tracking, time mgmt, tech news, web research
```

---

### Brain Workspace

Commands for managing the `brain/ai-brain/` personal knowledge workspace — three-tier note system (inbox → notes → library).

#### `/brain-new` — Create a Note
```
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
```
Inputs:   file (relative to brain/ai-brain/, e.g. inbox/GHCP_Agents_Guide.md), project bucket
Agent:    Copilot
Tools:    editFiles, codebase, runCommands
Use:      Publish an imported external source from inbox/ to library/ → add frontmatter → tag it → git commit
Example:  /brain-publish → inbox/GHCP_Agents_Guide.md → ghcp-knowledge-sharing
Output:   File archived to brain/ai-brain/library/<project>/<YYYY-MM>/YYYY-MM-DD_slug.md, committed
Tip:      For notes YOU wrote, use /brain-new → notes/ instead
```

#### `/brain-search` — Search Notes
```
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
```
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

#### `/mcp` — Learn & Build MCP Servers
```
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

```
/scope → generic     (theory mode)
/context → continue  (reconnect to prior)
/dsa → binary trees  (now taught as pure theory, building on prior context)
```

```
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

```
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
