# Features Inventory — Learning Assistant

> **Purpose:** Single source of truth for all features, capabilities, and assets in this
> repository. Used as the migration checklist when restructuring or modularizing.
>
> **Status:** Living document — update whenever features are added, moved, or removed.
>
> **Last audited:** 2026-05-15

---

## Table of Contents

- [Feature Summary](#feature-summary)
- [1. Java Modules](#1-java-modules-modules)
- [2. Prompts — 65 Slash Commands](#2-prompts--65-slash-commands)
- [3. Skills — 23 Knowledge Files](#3-skills--23-knowledge-files)
- [4. Agents — 8 Custom Personas](#4-agents--8-custom-personas)
- [5. Instructions — 9 Rule Files](#5-instructions--9-rule-files)
- [6. MCP Tools — 104 Tools](#6-mcp-tools--104-tools)
- [7. Brain Workspace — PKM System](#7-brain-workspace--pkm-system)
- [8. Documentation — 37 Files](#8-documentation--37-files)
- [9. Non-Java Content](#9-non-java-content)
- [10. Operational Infrastructure](#10-operational-infrastructure)
- [11. Composition Patterns](#11-composition-patterns--how-primitives-work-together)
- [12. User Entry Points](#12-user-entry-points)

---

## Feature Summary

| Category | Count | Location |
|---|---|---|
| Java Modules | 7 (206 files) | `modules/` |
| Prompts (slash commands) | 65 in 11 categories | `.github/prompts/` |
| Skills (auto-activated) | 23 in 8 domains | `.github/skills/` |
| Agents (personas) | 8 | `.github/agents/` |
| Instructions (rules) | 9 (5 always-on + 4 scoped) | `.github/instructions/` |
| MCP Tools | 104 (10 + 94) | 2 servers |
| Brain Workspace Tiers | 6 | `brain/ai-brain/` |
| Session Templates | 7 | `brain/ai-brain/sessions/_templates/` |
| Documentation | 37 files | `.github/docs/` |
| VS Code Tasks | 30+ | `.vscode/tasks.json` |
| Curated Learning Resources | 176+ | Vault providers |

---

## 1. Java Modules (`modules/`)

### Module Dependency Graph

```text
search-engine (0 deps)
    ↑
mcp-common (→ search-engine)
    ↑
mcp-learning-resources (→ mcp-common, search-engine)
mcp-atlassian          (→ mcp-common, search-engine)
    ↑
app (→ mcp-common, mcp-learning-resources, mcp-atlassian)

brain-models (0 deps)     ← standalone
mac-os       (0 deps)     ← standalone
```

### Module Details

| Module | Java Files | Dependencies | Purpose |
|---|---|---|---|
| `search-engine` | ~25 | None | Generic search library — BM25 scoring, fuzzy matching, keyword classification, in-memory indexing |
| `mcp-common` | ~15 | search-engine | Shared MCP infrastructure — layered config, validation, API keys, base server interface |
| `mcp-learning-resources` | ~65 | mcp-common, search-engine | Learning Resources MCP server — 176+ curated resources, 20 providers, 10 tool handlers |
| `mcp-atlassian` | ~80 | mcp-common, search-engine | Atlassian MCP server — Jira (34), Confluence (22), Bitbucket (31), auth (4), cross-product (3) tools |
| `app` | 1 | All above | Entry point (Main.java) + operational scripts, config, setup docs |
| `brain-models` | ~6 | None | Digital notetaking metadata — NoteKind, NoteStatus, NoteMetadata, NoteTemplate |
| `mac-os` | ~2 | None | macOS dev environment checker — Homebrew, npm, JDK, Docker detection |

### Key Components per Module

**search-engine** — Zero-dependency search library:

- BM25 relevance scoring with configurable k1/b parameters
- Levenshtein fuzzy matching for typo tolerance
- Keyword classification (maps free-text queries to enum categories)
- In-memory inverted index with TF-IDF weighting
- Query parser with boolean operators

**mcp-common** — Shared infrastructure:

- Layered config system (defaults → file → env → CLI overrides)
- API key validation and secure storage
- Base `McpServer` interface with stdio transport
- JSON-RPC message handler
- Common error types and validation utilities

**mcp-learning-resources** — Learning vault MCP server:

- 20 resource providers (Java, VCS, DevOps, SE books, system design, etc.)
- `KeywordIndex` — maps 500+ keywords to ConceptArea/ResourceCategory enums
- `RelevanceScorer` — combines BM25 + fuzzy + category boost
- 10 MCP tool handlers (search, browse, discover, scrape, export)
- Content scraping with metadata extraction (word count, reading time, difficulty)

**mcp-atlassian** — Enterprise integration:

- v1 server (27 tools, token auth, legacy)
- v2 server (94 tools, OAuth 2.0 + token, current)
- Jira: JQL search, issue CRUD, workflow transitions, sprints, boards, bulk operations
- Confluence: CQL search, page CRUD, versioning, labels, spaces
- Bitbucket: PRs, branches, pipelines, code search, deployments
- Cross-product unified search

---

## 2. Prompts — 65 Slash Commands

### 2a. Meta & Orchestration (9 prompts)

Workflow coordination, context management, and mode switching.

| Command | Agent | Description | Key Inputs | Composition |
|---|---|---|---|---|
| `/hub` | learning-mentor | Navigation hub — browse all assistants, commands, and learning paths by category | category | Routes to all domain-specific learning paths; the "home page" for discovery |
| `/context` | agent | Set context baseline — resume from prior session or start fresh | contextMode, priorSummary, task | Enables conversation continuity; reads/writes context summaries |
| `/composite` | agent | Combine multiple analysis modes into unified report | modes | Composes: refactor + design-review + impact + explain + debug + teach |
| `/steer` | agent | View or switch active steering mode | action, mode | Navigates: completeness, beast, learning, design, debug, focused |
| `/request-steering` | agent | Classify relationship of new request to in-progress work | request, current, type | Decision tree: independent / merge / sequential / supersede / park / split |
| `/session-scope` | agent | Manage session scope (global/project/feature) with widen/narrow/switch/fork | action, scope | Cross-references via `scope-refs`; logs `scope-transitions` in frontmatter |
| `/scope` | agent | Shorthand alias for `/session-scope` | action | Quick scope operations |
| `/multi-session` | agent | Continue work from a prior captured session | prior, context | Reads SESSION-LOG.md; bridges across conversations |
| `/write-docs` | agent | Write or improve documentation (README, API docs, guides) | topic, audience | Follows Diátaxis framework; markdown formatting rules |

### 2b. Learning & Concept Mastery (5 prompts)

Theory-first explanations with progressive depth.

| Command | Agent | Description | Key Inputs | Composition |
|---|---|---|---|---|
| `/learn-concept` | learning-mentor | Learn any CS/SE concept from fundamentals to advanced | concept, domain, level, language | 5-layer structure: intro → mechanics → official docs → real-world → edge cases |
| `/deep-dive` | learning-mentor | Multi-layered deep-dive (intuition → core → docs → patterns → mastery) | concept, domain, level, language | 5 progressive layers; references official specs/RFCs |
| `/teach` | learning-mentor | Explain concepts used in current file with theory, analogies, code | file | Auto-identifies concepts; includes "pain without this" + quiz |
| `/reading-plan` | learning-mentor | Structured study plan with curated resources, progression, milestones | topic, duration, level | Links to vault via `learning-resources-vault` skill |
| `/learn-from-docs` | learning-mentor | Learn via official documentation — parsing and synthesis | technology, focus | Extracts patterns from authoritative sources |

### 2c. Code Analysis & Debugging (8 prompts)

Code review, design review, debugging, and impact analysis.

| Command | Agent | Description | Key Inputs | Composition |
|---|---|---|---|---|
| `/debug` | debugger | Systematic bug investigation (7-step RCA) | file | gather → reproduce → hypothesis → investigate → root cause → fix → verify |
| `/design-review` | designer | SOLID/GRASP/clean-code review across 6 dimensions | file | Evaluates: SOLID, coupling/cohesion, design smells, naming, patterns, testability |
| `/refactor` | designer | Suggest and apply refactorings from standard catalog | file | Names patterns from Fowler's catalog; prioritizes by impact/risk |
| `/code-analysis` | agent | General code analysis for patterns and opportunities | file | Structural analysis without specific review lens |
| `/code-analysis-deep-dive` | agent | Internals deep-dive: data flow, call stack, line-by-line | file | Covers: flow, state changes, edge cases, dependencies, recent changes |
| `/explain` | agent | Beginner-friendly file explanation | file | 5-part: purpose, line-by-line, concepts, how-to-run, improvements |
| `/impact` | impact-analyzer | Trace ripple effects of changes (2+ levels deep) | file | Maps direct + transitive dependencies; risk-rates each affected area |
| `/check-standards` | agent | Verify compliance with industry standards and project conventions | file | References: Oracle, OWASP, 12-Factor, project-specific rules |

### 2d. Tools & External Integrations (6 prompts)

Interface with external tools, services, and platforms.

| Command | Agent | Description | Key Inputs | Composition |
|---|---|---|---|---|
| `/atlassian-tools` | agent | Universal Jira/Confluence/Bitbucket handler | task, service, level | Routes to `atlassian-tools` skill; 13 playbooks + 89 CLI actions + 94 MCP tools |
| `/read-url` | agent | Read, extract, summarize webpage content | url, mode, focus | Modes: read, summarize, extract-code, compare; cites sources |
| `/build-tools` | learning-mentor | Learn Maven, Gradle, Make, Bazel, npm build automation | tool, topic, level | Lifecycle, dependencies, multi-module, plugins, CI/CD integration |
| `/mac-dev` | learning-mentor | macOS dev environment setup | tool, task, level | Homebrew, JDK, npm, Docker; macOS-specific workflows |
| `/git-vcs` | learning-mentor | Git workflows, branching, Conventional Commits, SemVer | topic, mode, level | GitFlow, GitHub Flow, trunk-based development patterns |
| `/github-workflow` | learning-mentor | GitHub PR management, issues, CLI, Actions | task, scope, level | Integrates with `github-workflow` skill |

### 2e. Domain-Specific Learning (10 prompts)

Specialized knowledge areas with deep hierarchies.

| Command | Agent | Description | Key Inputs | Composition |
|---|---|---|---|---|
| `/dsa` | learning-mentor | Data structures and algorithms by pattern and difficulty | topic, mode, language, level | Hierarchy: linear, hashing, trees, graphs, DP, greedy, backtracking |
| `/system-design` | learning-mentor | System design (HLD + LLD) with internal hierarchy | topic, level, depth | 2-tier: HLD (scaling, data, communication) + LLD (OOP, API, patterns) |
| `/mcp` | learning-mentor | Model Context Protocol — build servers, transports, spec | topic, goal, level, language | 3 primitives (Tools, Resources, Prompts); stdio/SSE/Streamable HTTP |
| `/devops` | learning-mentor | DevOps tools and practices | topic, goal, level | Docker, K8s, Terraform, Ansible, CI/CD, monitoring, cloud platforms |
| `/tech-stack` | learning-mentor | Framework/library comparisons — learn, compare, choose | topic, category, level | Backend, frontend, database, messaging; trade-off tables |
| `/resources` | learning-mentor | Search, browse, discover curated learning resources | action, topic, filters | Actions: search, browse, discover, scrape, recommend, details |
| `/digital-notetaking` | learning-mentor | PKM tools and methodologies | tool, method, level | Notion, Obsidian, Logseq; PARA, CODE, Zettelkasten |
| `/language-guide` | learning-mentor | Language-specific guides (Java, Python, Go, Rust, TS) | language, topic, level | Syntax, idioms, ecosystem, performance per language |
| `/sdlc` | learning-mentor | Software development lifecycle — methodologies and processes | methodology, phase, level | Agile, Scrum, Kanban, V-model, DevOps culture |
| `/explore-project` | learning-mentor | Explore and understand an existing project | project, focus, level | Maps layout, entry points, key modules, dependencies |

### 2f. Career & Daily Life (3 prompts)

Career guidance, interview prep, and daily productivity.

| Command | Agent | Description | Key Inputs | Composition |
|---|---|---|---|---|
| `/career-roles` | learning-mentor | Tech career roles, skills, pay, growth paths | role, goal, level | IC vs. Management tracks; role ladders; compensation data |
| `/interview-prep` | learning-mentor | Technical interview prep (DSA, system design, behavioral) | type, topic, level, time | Pattern-based DSA; HLD/LLD frameworks; behavioral STAR method |
| `/daily-assist` | daily-assistant | Finance, productivity, news, research, comparisons | category, details | Not financial advice; uses web fetch for current data |

### 2g. Brain & Knowledge Management (12 prompts)

Git-inspired PKM operations on the brain workspace.

| Command | Agent | Description | Key Inputs | Composition |
|---|---|---|---|---|
| `/new` | agent | Create new note in brain with frontmatter (inbox or notes) | topic, tier, project | Routes to tier-specific templates (session, decision, note, resource) |
| `/search` | agent | Full-text search across brain tiers | query, tier | Returns structured results with tier indicators |
| `/publish` | agent | Publish/archive external source to library/ | source, tier | Routes imported content (articles, docs, decks) |
| `/fetch` | agent | Check external source for updates without merging | source | Git-like "fetch" — preview changes before pulling |
| `/pull` | agent | Update brain content from external source (fetch + merge) | source | Git-like "pull" — refreshes imported content |
| `/push` | agent | Export brain content to external tool (Notion, Keep) | source, items | Git-like "push" — sync outward |
| `/diff` | agent | Compare brain content with external source | source, items | Shows additions, changes, deletions |
| `/merge` | agent | Merge changes from external source into brain | source, strategy | Strategies: theirs, ours, manual merge |
| `/clone` | agent | Import entire external source into brain | source | Full initial import of a knowledge base |
| `/cherry-pick` | agent | Import specific items from external source | source, items | Selective import without full clone |
| `/remote` | agent | Manage external source connections | action, source | List, add, remove, verify remote connections |
| `/stash` | agent | Temporarily shelve in-progress brain work | action | Save/restore work-in-progress notes |

### 2h. Backlog & Task Tracking (5 prompts)

Agile board management with capture-first philosophy.

| Command | Agent | Description | Key Inputs | Composition |
|---|---|---|---|---|
| `/jot` | agent | Quick capture — thoughts, tasks, ideas, URLs into backlog | text, project | 9-phase protocol: parse → classify → dedup → enhance → refine → cross-ref → confirm → board sync |
| `/read-file-jot` | agent | Import items from local file into backlog | filePath | Reads file, extracts actionable lines as tasks |
| `/todos` | agent | View backlog board, filter, mark items done | action, filter | Kanban-style board with status, priority, effort |
| `/todo` | agent | Single-item todo — create or update | item, status | Quick single-item API |
| `/backlog` | agent | Advanced backlog management | action, text | Actions: brainstorm, guide, refine, promote, epic, sprint, board |

### 2i. Shipping & Git Operations (2 prompts)

Commit, push, and PR workflows with built-in quality gates.

| Command | Agent | Description | Key Inputs | Composition |
|---|---|---|---|---|
| `/ship` | agent | Commit, push, or both — with lint, build, PR suggestion | mode, hint | Modes: commit, push, both, pr; runs `__md_lint.ps1` + `gradlew build`; Conventional Commits |
| `/github-push` | agent | Push to GitHub with automatic PR creation | branch, message | GitHub-specific push; auto-creates/updates PR via `gh` CLI |

### 2j. Customization & Authoring (3 prompts)

Create and extend Copilot customization primitives.

| Command | Agent | Description | Key Inputs | Composition |
|---|---|---|---|---|
| `/copilot-customization` | agent | Create/review/compare any of the 6 Copilot primitives | goal, type, domain, level | Covers: instructions, prompts, agents, skills, MCP; composition patterns |
| `/create-agent` | agent | Scaffold a custom .agent.md with persona and tools | name, persona, tools | Generates frontmatter, instructions, handoff definitions |
| `/mcp-to-skill` | agent | Convert MCP server tools into Copilot skills | mcp, domain | Wrapper pattern; exposes MCP as auto-activating skill |

---

## 3. Skills — 23 Knowledge Files

Skills auto-activate when Copilot detects matching keywords in the conversation.
They provide domain knowledge without requiring a slash command.

### 3a. Learning Resources (2 skills)

| Skill | Activates On | Content |
|---|---|---|
| `learning-resources-vault` | "learning resources", "tutorials", "books", "courses", "where to learn" | Master discovery index — 176+ resources across 10+ domains with difficulty ratings, content format, authors |
| `software-engineering-resources` | "SE practices", "design patterns", "testing", "code quality" | SE/CS deep reference — Clean Code, Refactoring Guru, testing, patterns, architecture |

### 3b. Languages & Platforms (6 skills)

| Skill | Activates On | Content |
|---|---|---|
| `java-build` | "build", "compile", "run", "Maven", "Gradle" | 3-tier cheatsheet (newbie/amateur/pro); multi-module patterns; troubleshooting |
| `java-debugging` | "NullPointerException", "debugging Java", "stack traces" | Common exception catalog; JVM diagnostic tools; fix templates |
| `java-learning-resources` | "Java learning", "Java tutorials", "Baeldung", "Spring" | Oracle docs, Effective Java, Spring guides, JUnit, Baeldung |
| `java-formatting` | Only when explicitly invoked (opt-in) | IntelliJ formatter, import ordering, stream pipeline formatting |
| `jvm-platform` | "JVM internals", "garbage collection", "GraalVM" | Memory model, GC algorithms, class loading, JIT compilation, GraalVM native |
| `mac-dev` | "macOS", "Homebrew", "Mac development" | Homebrew, zsh, JDK on Mac, Finder paths, npm on Mac |

### 3c. DevOps & Tooling (5 skills)

| Skill | Activates On | Content |
|---|---|---|
| `atlassian-tools` | "Jira", "Confluence", "Bitbucket", "JQL", "CQL" | 94 MCP tools, 13 playbooks, 89 CLI actions; PAT + OAuth auth; cross-product |
| `git-vcs` | "git", "branching", "merge", "rebase", "SemVer" | GitFlow, GitHub Flow, trunk-based; Conventional Commits format |
| `mcp-development` | "MCP", "Model Context Protocol", "building MCP servers" | 3 primitives (Tools, Resources, Prompts); transports; building servers |
| `copilot-customization` | "Copilot customization", "instructions", "skills", "agents" | 6 primitives lifecycle; composition; when to use each |
| `web-reader` | "read URL", "fetch page", "summarize article" | Multi-URL support; content extraction patterns |

### 3d. Development Process (3 skills)

| Skill | Activates On | Content |
|---|---|---|
| `deep-research` | "research", "investigation", "spike", "feasibility" | Hypothesis-driven research framework; evidence gathering; trade-off analysis |
| `requirements-research` | "requirements", "user stories", "BDD", "acceptance criteria" | INVEST criteria; Gherkin scenarios; MoSCoW prioritization |
| `github-workflow` | "GitHub", "PR", "issues", "GitHub CLI" | PR lifecycle; issue templates; `gh` CLI commands |

### 3e. Design & Architecture (2 skills)

| Skill | Activates On | Content |
|---|---|---|
| `design-patterns` | "design patterns", "SOLID", "Factory", "Strategy" | GoF catalog with when-to-use; anti-patterns; real-world examples |
| `software-development-roles` | "PO", "developer role", "QA", "responsibilities" | Role definitions; team dynamics; handoff protocols |

### 3f. Knowledge Management (3 skills)

| Skill | Activates On | Content |
|---|---|---|
| `brain-management` | "brain workspace", "naming conventions", "tier routing" | 6-tier architecture; session naming protocol; cross-reference patterns |
| `pkm-management` | "PKM", "capture sources", "content consolidation" | 10 git-inspired verbs (fetch, pull, push, merge, diff, etc.) |
| `digital-notetaking` | "note-taking", "Notion", "Obsidian", "PARA", "CODE method" | Tool-agnostic methods; Notion/Obsidian/Logseq comparison |

### 3g. Career & Daily Life (2 skills)

| Skill | Activates On | Content |
|---|---|---|
| `career-resources` | "career", "compensation", "growth paths" | IC vs. Management; skills matrices; interview resources |
| `daily-assistant-resources` | "finance", "productivity", "planning", "wellness" | Personal finance tools; productivity systems; news sources |

---

## 4. Agents — 8 Custom Personas

Each agent has a distinct personality, tool restrictions, and handoff targets.

### Agent Details

| Agent | Persona | Tools Allowed | Handoffs To |
|---|---|---|---|
| **Thinking-Beast-Mode** | Transcendent coding agent — deep multi-dimensional analysis, adversarial intelligence, autonomous problem-solving | All tools | None (self-sufficient) |
| **Learning-Mentor** | Patient teacher (15+ yrs SE) — builds understanding over memorization; explains WHY + HOW; connects to prior knowledge | search, codebase, usages, fetch, findTestFiles | → Practice With Code (agent), → Review Understanding (code-reviewer) |
| **Designer** | Senior architect (15+ yrs) — thinks in abstractions and trade-offs; SOLID, GRASP, design patterns | search, codebase, usages, fetch, findTestFiles | → Analyze Impact (impact-analyzer), → Start Implementation (agent) |
| **Debugger** | Expert debugger — methodical, evidence-based; reproduces first; hypothesize then test; JVM expertise | search, codebase, usages, terminal, debugger, problems, findTestFiles | → Analyze Impact (impact-analyzer), → Review Fix (code-reviewer) |
| **Impact-Analyzer** | Traces every ripple — dependency graphs, blast radius, probability × impact risk scoring | search, codebase, usages, problems, findTestFiles | → Design Changes (designer), → Review After Changes (code-reviewer) |
| **Code-Reviewer** | Thorough but read-only — focuses on bugs, style, best practices; rates: Critical/Warning/Suggestion | search, codebase, usages | None (read-only) |
| **Daily-Assistant** | Practical, concise — handles finance, productivity, news, research; uses web fetch | fetch | → Switch to Learning (learning-mentor), → Deep Research (agent) |
| **My-Kanha** | Lord Krishna — spiritual mentor, wisdom of Bhagavad Gita; Nishkaam Karma, equanimity, divine vision | fetch | → Switch to Learning (learning-mentor), → Daily Assistant (daily-assistant) |

### Agent Handoff Chains

```text
debugger → impact-analyzer → designer → agent (implementation)
                ↓
         code-reviewer (read-only verification)

learning-mentor → agent (practice) → code-reviewer (review)

daily-assistant ↔ learning-mentor
my-kanha → learning-mentor | daily-assistant
```

---

## 5. Instructions — 9 Rule Files

### Always-On (applyTo: `**`)

| Instruction | Purpose | Key Mechanisms |
|---|---|---|
| `change-completeness` | Every change is a ripple — checklist A through P by change type | 16 checklist sections; build verification; cross-reference checks |
| `chat-capture` | Auto-capture AI conversations to `sessions/` | Capture Gate triggers; 7 templates; 14 categories; escalation protocol |
| `md-formatting` | Markdown formatting enforcement | 10 rule categories; `__md_lint.ps1` auto-fixer; pre-commit gate |
| `session-scoping` | Session scope management (global/project/feature) | Scope hierarchy; widen/narrow/switch/fork; `scope-transitions` logging |
| `steering-modes` | Behavioral profiles controlling thoroughness and depth | 6 modes: completeness (default), beast, learning, design, debug, focused |

### File-Scoped

| Instruction | applyTo | Purpose |
|---|---|---|
| `java` | `**/*.java` | Java naming, style, methods under 30 lines, javadoc, error handling |
| `clean-code` | (manual) | Methods do one thing; no nesting; top-down narrative; comments explain WHY |
| `build` | `**/*.gradle` | Gradle wrapper always; standard directory layout; dependency bounds |
| `backlog` | `brain/ai-brain/backlog/**` | Capture-first philosophy; 9-phase `/jot` protocol; board views; sprints |

---

## 6. MCP Tools — 104 Tools

### Learning Resources Server (10 tools)

| Tool | Description | Parameters |
|---|---|---|
| `search_resources` | Text search across 176+ curated resources | query, category, type, difficulty |
| `browse_vault` | Browse by category or type | category, type |
| `get_resource` | Get full resource metadata by ID | resourceId |
| `list_categories` | List all categories with counts | — |
| `discover_resources` | Smart discovery — classifies intent (specific/vague/exploratory) | intent, domain, mode |
| `scrape_url` | Scrape URL, return summary with metadata | url |
| `read_url` | Scrape URL, return full extracted text | url |
| `add_resource` | Add custom resource manually | title, url, description, type, difficulty, category |
| `add_resource_from_url` | Auto-extract metadata from URL and add as resource | url |
| `export_results` | Export results as Markdown, PDF, or Word | format, results |

### Atlassian Server (94 tools)

#### Jira — 34 tools

| Category | Tools | Description |
|---|---|---|
| Search & Query | `jira_search_issues`, `jira_get_my_issues`, `jira_get_filters` | JQL search, personal issues, saved filters |
| Issue CRUD | `jira_get_issue`, `jira_create_issue`, `jira_update_issue`, `jira_delete_issue`, `jira_bulk_create_issues` | Full issue lifecycle |
| Workflow | `jira_transition_issue`, `jira_get_transitions`, `jira_get_statuses` | Status transitions, workflow navigation |
| Comments & Watchers | `jira_add_comment`, `jira_get_comments`, `jira_get_watchers`, `jira_add_watcher`, `jira_remove_watcher` | Collaboration |
| Project & Board | `jira_list_projects`, `jira_get_project`, `jira_get_boards`, `jira_get_sprint`, `jira_get_sprint_issues` | Project/sprint navigation |
| Components & Versions | `jira_get_components`, `jira_create_component`, `jira_get_versions`, `jira_create_version` | Release management |
| Links & Worklogs | `jira_link_issues`, `jira_add_worklog`, `jira_assign_issue` | Time tracking, linking |
| Metadata | `jira_get_issue_types`, `jira_get_priorities`, `jira_search_users`, `jira_get_attachments`, `jira_list_dashboards`, `jira_whoami` | System metadata |

#### Confluence — 22 tools

| Category | Tools | Description |
|---|---|---|
| Search | `confluence_search` | CQL or free-text search |
| Page CRUD | `confluence_get_page`, `confluence_get_page_body`, `confluence_create_page`, `confluence_update_page`, `confluence_delete_page`, `confluence_move_page` | Full page lifecycle |
| Space | `confluence_list_spaces`, `confluence_get_space` | Space navigation |
| Hierarchy | `confluence_get_page_children`, `confluence_get_page_versions` | Page tree |
| Labels & Comments | `confluence_get_page_labels`, `confluence_add_page_label`, `confluence_get_page_comments`, `confluence_add_page_comment` | Metadata |
| Properties & Restrictions | `confluence_get_restrictions`, `confluence_get_properties`, `confluence_set_property` | Access control |
| Other | `confluence_get_attachments`, `confluence_get_templates`, `confluence_whoami` | Content management |

#### Bitbucket — 31 tools

| Category | Tools | Description |
|---|---|---|
| Repository | `bitbucket_list_repos`, `bitbucket_get_repo`, `bitbucket_list_projects` | Repo navigation |
| Pull Requests | `bitbucket_list_pull_requests`, `bitbucket_get_pull_request`, `bitbucket_create_pull_request`, `bitbucket_get_pull_request_diff`, `bitbucket_approve_pull_request`, `bitbucket_decline_pull_request`, `bitbucket_merge_pull_request`, `bitbucket_get_pr_comments`, `bitbucket_add_pr_comment` | PR lifecycle |
| Branches & Tags | `bitbucket_list_branches`, `bitbucket_create_branch`, `bitbucket_delete_branch`, `bitbucket_list_tags`, `bitbucket_create_tag` | Branch management |
| Code & Commits | `bitbucket_search_code`, `bitbucket_get_commits`, `bitbucket_get_commit`, `bitbucket_get_diff`, `bitbucket_get_file_content` | Code inspection |
| CI/CD | `bitbucket_list_pipelines`, `bitbucket_get_pipeline`, `bitbucket_trigger_pipeline`, `bitbucket_stop_pipeline`, `bitbucket_list_environments`, `bitbucket_list_deployments` | Pipeline management |
| Snippets | `bitbucket_list_snippets`, `bitbucket_create_snippet`, `bitbucket_whoami` | Code sharing |

#### Cross-Product & Auth — 7 tools

| Tool | Description |
|---|---|
| `atlassian_unified_search` | Search across all Atlassian products |
| `atlassian_status` | Check connectivity to all products |
| `atlassian_whoami` | Current user info across all products |
| `auth_login_browser` | Browser-based OAuth 2.0 login |
| `auth_status` | Check authentication status |
| `auth_logout` | Clear stored credentials |
| `auth_switch_method` | Switch between token and OAuth auth |

---

## 7. Brain Workspace — PKM System (`brain/ai-brain/`)

### Six-Tier Architecture

| Tier | Tracked | Purpose | Content |
|---|---|---|---|
| `inbox/` | Gitignored | Raw capture — triage queue | Temporary files; cleared via `brain.ps1 clear` |
| `notes/` | Tracked | Distilled personal insights | Synthesis notes, learning summaries |
| `library/` | Tracked | Imported external materials | Curated articles, docs, knowledge sharing |
| `sessions/` | Tracked | AI conversation captures | 7 templates, 14 categories, escalation protocol |
| `backlog/` | Tracked | Agile task tracking | Board, epics, features, sprints, ideas, views |
| `pkm/` | Tracked | PKM infrastructure | Access policy, capture sources, workflows, credentials |

### Session System

**7 templates** for different session types:

| Template | Use For | Key Sections |
|---|---|---|
| `session-capture.md` | Generic default | Request, Analysis, Key Outcomes, Follow-Up |
| `code-analysis-capture.md` | Code review, patterns | Findings table, code smells, recommendations |
| `code-analysis-deep-dive-capture.md` | Internals, flow, line-by-line | Data flow, call stack, code blocks, state changes |
| `design-capture.md` | Architecture, API design | Alternatives, trade-offs, decision matrix |
| `debugging-capture.md` | Bug investigation, RCA | Hypothesis tracking, evidence log, root cause |
| `requirements-capture.md` | User stories, BDD | User stories, acceptance criteria, NFRs, scope |
| `intent-capture.md` | Decision records | Why, alternatives considered, consequences |

**14 session categories** across 2 domains:

- **Work (8):** code-analysis, debugging, requirements, performance, feature-exploration, documentation, research, general
- **Personal (6):** personal-work (software-dev umbrella with 7 sub-activities), financial, research, learning, general

**Escalation protocol:** Sub-package creation when 3+ files share a subject prefix (Patterns 1–3).

### Backlog System

- **BOARD.md** — Kanban board with dashboard, priority lanes, epic tracking
- **epics/** — Large initiatives (EPIC-001 through EPIC-004)
- **features/** — Concrete deliverables (BLI-001 through BLI-040)
- **projects/** — Standalone personal projects (BLI-008, BLI-014 through BLI-019)
- **ideas/** — Raw thoughts awaiting refinement
- **sprints/** — Sprint planning and retrospectives
- **views/** — Custom board views (by-status, by-project, by-priority)
- **guides/** — Workflow documentation (capture-workflow, jot-down-guide)

### Brain Scripts

| Script | Purpose |
|---|---|
| `brain.ps1` / `brain.sh` | Main CLI — new, publish, search, list, status, clear, help |
| `promote.ps1` / `promote.sh` | Promote inbox items to notes or library |
| `clear-inbox.ps1` / `clear-inbox.sh` | Clear processed inbox items |
| `brain-module.psm1` | PowerShell module with shared functions |

---

## 8. Documentation — 37 Files (`.github/docs/`)

### By Purpose

| Category | Count | Key Files |
|---|---|---|
| Entry points & navigation | 4 | `START-HERE.md`, `getting-started.md`, `navigation-index.md`, `primitives-at-a-glance.md` |
| Copilot customization | 8 | internals, workflow, newbie guide, deep-dive, guide, evolution-guide, crosswalk, mcp-preview |
| MCP documentation | 7 | how-it-works, ecosystem, implementations, architecture, setup, dev-guide, vs-skills |
| Architecture & search | 4 | architecture-overview, search-engine, search-engine-algorithms, file-reference |
| Learning & reference | 3 | phase-guide, session-capture-guide, slash-commands |
| Export & setup | 4 | export-guide, export-newbie-guide, local-setup-guide, configuration-reference |
| Content & formatting | 3 | md-formatting-guide, mermaid-as-context, prompt-composition |
| Inventory & process | 4 | skills-library, features-inventory, versioning-guide, team-copilot-adoption |

---

## 9. Non-Java Content

### Digital Notetaking Guides (`brain/digitalnotetaking/` — 8 files)

PKM philosophy and tool-agnostic methodology guides:

| File | Topic | Content |
|---|---|---|
| `START-HERE.md` | Entry point | Reading path for PKM newcomers |
| `para-method.md` | PARA methodology | Projects, Areas, Resources, Archive — Tiago Forte's system |
| `code-method.md` | CODE method | Capture, Organize, Distill, Express — knowledge workflow |
| `tools-comparison.md` | Tool comparison | Obsidian vs Logseq vs Notion vs OneNote vs Roam |
| `ai-brain-integration.md` | AI brain integration | How external tools connect to brain/ai-brain/ |
| `templates.md` | Note templates | Session, book, article, project, snippet templates |
| `migration-guide.md` | PKM migration | Steps to migrate from one PKM system to another |

### macOS Guides (`mac-os/docs/` — 6 files)

| File | Topic |
|---|---|
| `START-HERE.md` | macOS setup entry point |
| `mac-dev-environment.md` | Complete dev environment setup |
| `homebrew-guide.md` | Homebrew installation and usage |
| `jdk-setup.md` | JDK on macOS (SDKMAN!, Temurin, Homebrew) |
| `dev-tools-guide.md` | Git, Docker, Node, IDE setup |
| `npm-on-mac.md` | npm and Node.js on macOS |

---

## 10. Operational Infrastructure

### VS Code Tasks (30+)

| Category | Tasks |
|---|---|
| Build | `mcp-servers: build`, `mcp-servers: build (clean)` — via Gradle |
| Server lifecycle | start, stop, restart, reset, demo, logs, validate, list-tools (× 2 servers) |
| Brain operations | new, publish, search, list, status, clear inbox, help |
| Setup | `mcp-servers: setup` — one-time onboarding wizard |

### VS Code MCP Registry (`.vscode/mcp.json`)

| Server | Status | Tools |
|---|---|---|
| `learning-resources` | Disabled (migrated to skill) | 10 tools |
| `atlassian` (v1) | Disabled (legacy) | 27 tools |
| `atlassian-v2` | Disabled (requires credentials) | 94 tools |
| `github` | Disabled (requires token) | Community server |
| `filesystem` | Disabled | Community server |

---

## 11. Composition Patterns — How Primitives Work Together

### Pattern 1: Learning Flow

```text
User asks → /hub (navigation)
  → /learn-concept (learning-mentor agent)
    → activates skill: design-patterns (auto)
    → activates skill: learning-resources-vault (auto)
  → /deep-dive (progressive layers)
  → /reading-plan (curated resources)
  → /interview-prep (for interview context)
```

### Pattern 2: Code Review Flow

```text
User opens file → /design-review (designer agent)
  → activates skill: java (auto for .java files)
  → activates instruction: clean-code (auto for .java files)
  → handoff → impact-analyzer agent (/impact)
    → handoff → designer agent (/refactor)
      → handoff → agent (implementation)
  → /composite (combines: refactor + design-review + impact)
```

### Pattern 3: Atlassian Integration Flow

```text
User mentions Jira → /atlassian-tools
  → activates skill: atlassian-tools (94 MCP tools)
  → routes to playbook (e.g., "Sprint Review" → 5 tool sequence)
  → cross-references → /github-workflow (for PR ↔ Jira linking)
```

### Pattern 4: Session Capture + PKM Flow

```text
Deep conversation qualifies → chat-capture.instructions.md (Capture Gate)
  → classifies domain + category
  → selects template (1 of 7)
  → /brain-capture-session (scaffolds frontmatter)
  → session-scoping.instructions.md (scope: global/project/feature)
  → logs to SESSION-LOG.md + CAPTURE-LOG.md
  → later: /brain-publish (promote inbox → notes/library)
```

### Pattern 5: Ship Flow

```text
User: /ship both
  → runs __md_lint.ps1 (markdown formatting gate)
  → runs ./gradlew build (Java compilation gate)
  → git add + git commit (Conventional Commits format)
  → git push
  → gh pr create (auto-PR creation)
  → change-completeness.instructions.md (checklist verification)
```

### Pattern 6: Steering Mode Overlay

```text
Base: copilot-instructions.md (always on)
  + change-completeness.instructions.md (always on)
  + md-formatting.instructions.md (always on)
  + chat-capture.instructions.md (always on)
  + session-scoping.instructions.md (always on)
  + steering-modes.instructions.md (always on)
  ─── File-scoped ───
  + java.instructions.md (when editing .java)
  + clean-code.instructions.md (when editing .java)
  + build.instructions.md (when editing .gradle)
  + backlog.instructions.md (when editing backlog/**)
  ─── Per-message ───
  + Agent persona (when agent selected)
  + Slash command context (per-message only)
```

---

## 12. User Entry Points

| User Need | Start With | Then Use |
|---|---|---|
| Learn a concept | `/hub` or `/learn-concept` | `/deep-dive`, `/teach`, `/resources`, `/reading-plan` |
| Review code | `/design-review` or `/code-analysis` | `/composite`, `/impact`, `/refactor` |
| Debug a bug | `/debug` | hypothesis → trace → fix → test chain |
| Work with Jira/Confluence | `/atlassian-tools` | 94 MCP tools via skill playbooks |
| Manage knowledge | `/new` or `/jot` | `/search`, `/publish`, `/pull`, `/push` |
| Ship code | `/ship` | Auto-lint → build → commit → push → PR |
| Understand a project | `/explore-project` | `/code-analysis-deep-dive`, `/design-review` |
| Interview prep | `/interview-prep` | `/dsa`, `/system-design`, `/career-roles` |
| Daily life help | `/daily-assist` | Finance, productivity, news, research |
| Build your own tools | `/copilot-customization` | `/create-agent`, `/mcp-to-skill`, `/mcp` |
