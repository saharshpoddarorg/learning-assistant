# 🛠️ Agent Skills — Guide

> **What:** Folders containing instructions + scripts + resources that Copilot loads automatically when the task matches.
> **Where:** `.github/skills/<skill-name>/SKILL.md`
> **How to use:** Just ask Copilot a matching question — skills load automatically.

---

## 📑 Table of Contents

- [What Are Agent Skills?](#-what-are-agent-skills)
- [Skills in This Project](#-skills-in-this-project) (21 skills, 4 domains)
  - [Software Engineering](#1-software-engineering-14-skills)
  - [Knowledge Management](#2-knowledge-management-3-skills)
  - [Career & Professional Development](#3-career--professional-development-1-skill)
  - [Daily Life & Productivity](#4-daily-life--productivity-1-skill)
- [Hierarchical Taxonomy](TAXONOMY.md) ← full tree + cross-reference matrix
- [How Skills Differ from Other Primitives](#how-skills-differ-from-other-primitives)
- [Directory Structure](#-directory-structure)
- [SKILL.md Format](#-skillmd-format)
- [How Copilot Uses Skills (3-Level Loading)](#-how-copilot-uses-skills-3-level-loading)
- [Examples](#-examples)
- [How to Create a Skill](#-how-to-create-a-skill)
  - [Writing Good Descriptions](#choosing-a-good-description)
- [Skill Ideas](#-skill-ideas)
- [Experiments to Try](#-experiments-to-try)

---

## 📌 What Are Agent Skills?

Skills are the most powerful customization primitive. Unlike instructions (text only), skills can include:

- **Instructions** (the `SKILL.md` file)
- **Scripts** (shell scripts, batch files)
- **Templates** (code templates the AI can use)
- **Examples** (example files for reference)
- **Any other resource** the AI might need

Skills are also an **open standard** ([agentskills.io](https://agentskills.io/)) — they work across VS Code, Copilot CLI, and Copilot coding agent.

**Analogy:**

| Type | Like... |
|---|---|
| Instructions | Telling someone how to cook (just words) |
| **Skills** | Giving them a recipe card + pre-measured ingredients + tools |

### How Skills Differ from Other Primitives

| Aspect | Instructions | Prompts | **Skills** |
|---|---|---|---|
| Includes scripts? | ❌ | ❌ | ✅ |
| Includes examples? | ❌ | Via links | ✅ In-directory |
| Activation | Glob pattern | Manual (`/command`) | **Auto** (description match) |
| Portable? | VS Code + GitHub | VS Code only | **Open standard** |
| Location | Single `.md` file | Single `.md` file | **Folder** with resources |

---

## 🗂️ Skills in This Project

> **23 skills** across 4 domains — see [TAXONOMY.md](TAXONOMY.md) for the full hierarchical index.

### 1. Software Engineering (14 skills)

#### Languages & Platforms

| Skill | Triggers On | What It Provides |
|---|---|---|
| [`java-build`](java-build/SKILL.md) | Compile, run, build, `javac`, classpath | Compile commands, common errors, JDK setup |
| [`java-debugging`](java-debugging/SKILL.md) | Exceptions, debugging, stack trace, breakpoints | Exception diagnosis, fix patterns, debugger usage |
| [`java-formatting`](java-formatting/SKILL.md) | Code style, formatting, inspections (**opt-in**) | IntelliJ style rules, brace placement, stream formatting |
| [`java-learning-resources`](java-learning-resources/SKILL.md) | Java tutorials, docs, blogs, Oracle, Baeldung | Curated index — Oracle docs, dev.java, blogs, OSS projects |
| [`jvm-platform`](jvm-platform/SKILL.md) | JVM, GC, class loading, bytecode, GraalVM, Kotlin | JVM architecture, GC tuning, profiling, JVM languages |
| [`mac-dev`](mac-dev/SKILL.md) | macOS, Homebrew, zsh, Mac setup | macOS dev environment — Homebrew, JDK, npm, Docker, dotfiles |

#### Design & Architecture

| Skill | Triggers On | What It Provides |
|---|---|---|
| [`design-patterns`](design-patterns/SKILL.md) | Design patterns, SOLID, GoF, creational, structural | Pattern decision guide, SOLID reference, GoF catalog |
| [`software-development-roles`](software-development-roles/SKILL.md) | PO, Developer, QA, Tester, sprint, role workflows | Role guidance — PO/Dev/QA responsibilities, collaboration |

#### Development Process

| Skill | Triggers On | What It Provides |
|---|---|---|
| [`deep-research`](deep-research/SKILL.md) | Investigation, spike, RCA, trade-off, feasibility | Research methodology, evidence synthesis, decision docs |
| [`requirements-research`](requirements-research/SKILL.md) | User stories, acceptance criteria, BDD, discovery | Elicitation techniques, story mapping, prioritization |
| [`github-workflow`](github-workflow/SKILL.md) | PR, pull request, issue, GitHub CLI, `gh`, PR title | PR management, issue workflows, `gh` CLI, branch ops |

#### DevOps & Tooling

| Skill | Triggers On | What It Provides |
|---|---|---|
| [`git-vcs`](git-vcs/SKILL.md) | Git commands, branching, merge, rebase, GitFlow | Git cheatsheet, branching strategies, Conventional Commits |
| [`mcp-development`](mcp-development/SKILL.md) | MCP, Model Context Protocol, MCP server, tools | MCP architecture, building servers, transport, testing |
| [`copilot-customization`](copilot-customization/SKILL.md) | Copilot instructions, prompts, agents, skills, `.github` | 6 customization primitives, activation rules, stacking |
| [`atlassian-tools`](atlassian-tools/SKILL.md) | Jira, Confluence, Bitbucket, JQL, CQL, PAT CLI, sprints | 89-action CLI, JQL/CQL cheatsheet, workflow playbooks |
| [`web-reader`](web-reader/SKILL.md) | URL, webpage, fetch page, summarize article, extract content | Webpage reading, content extraction, article summarization |

#### Learning Resources

| Skill | Triggers On | What It Provides |
|---|---|---|
| [`learning-resources-vault`](learning-resources-vault/SKILL.md) | Learning resources, tutorials, books, courses, study plan | **176 curated resources** across 10+ domains — the master vault |
| [`software-engineering-resources`](software-engineering-resources/SKILL.md) | DSA, system design, OS, networking, DBMS, testing, DevOps | Comprehensive SE/CS resource index — books, tools, frameworks |

### 2. Knowledge Management (3 skills)

| Skill | Triggers On | What It Provides |
|---|---|---|
| [`brain-management`](brain-management/SKILL.md) | brain/, notes, inbox, sessions, backlog, tier routing | brain/ai-brain/ naming, structure, frontmatter, agile board |
| [`pkm-management`](pkm-management/SKILL.md) | PKM, capture sources, consolidation, access control | Git-inspired content ops, brain consolidation, access policy |
| [`digital-notetaking`](digital-notetaking/SKILL.md) | Notion, Obsidian, Logseq, PARA, Zettelkasten, notes | Tool comparison, PARA/CODE methods, migration guide |

### 3. Career & Professional Development (1 skill)

| Skill | Triggers On | What It Provides |
|---|---|---|
| [`career-resources`](career-resources/SKILL.md) | Job roles, salary, career roadmap, interview, tech career | Role hierarchies, skills matrices, compensation data |

### 4. Daily Life & Productivity (1 skill)

| Skill | Triggers On | What It Provides |
|---|---|---|
| [`daily-assistant-resources`](daily-assistant-resources/SKILL.md) | Finance, budgeting, productivity, time management, news | Productivity methods, finance basics, news sources |

---

## 📁 Directory Structure

Each skill is a **folder** (not a single file):

```text
.github/skills/
│
├── java-build/                    ← Skill folder
│   ├── SKILL.md                   ← Required: skill definition
│   ├── build-verify.sh            ← Optional: helper script
│   └── common-errors.md           ← Optional: reference material
│
├── run-tests/                     ← Another skill
│   ├── SKILL.md
│   └── examples/
│       └── SampleTest.java        ← Optional: template/example
│
└── create-class/                  ← Another skill
    ├── SKILL.md
    └── ClassTemplate.java         ← Optional: template file
```

---

## 📄 SKILL.md Format

```markdown
---
name: skill-name
description: >
  Detailed description of what this skill does and when to use it.
  Be specific so Copilot knows when to load this skill.
---

# Skill Instructions

Detailed instructions, procedures, and guidelines go here.
You can reference files in this directory using relative paths:
[template](./ClassTemplate.java)
```

### Frontmatter Fields

| Field | Required? | Description | Constraints |
|---|---|---|---|
| `name` | **Yes** | Unique identifier for the skill | Lowercase, hyphens, max 64 chars |
| `description` | **Yes** | What the skill does and when to use it | Max 1024 chars — **be specific!** |

> ⚠️ The `description` is how Copilot decides whether to load the skill. Vague descriptions = skill never loads. Specific descriptions = loads at the right time.

---

## 🧠 How Copilot Uses Skills (3-Level Loading)

Skills use **progressive disclosure** — they don't dump everything into context immediately:

```text
Level 1: DISCOVERY (always loaded — very lightweight)
  ├── Copilot reads: name + description from frontmatter
  ├── Cost: ~10-20 tokens per skill
  └── Happens: Every request

Level 2: INSTRUCTIONS (loaded when task matches)
  ├── Copilot reads: Full SKILL.md body
  ├── Cost: Depends on file size
  └── Happens: Only when description matches your prompt

Level 3: RESOURCES (loaded on demand)
  ├── Copilot reads: Scripts, templates, examples in the directory
  ├── Cost: Only what it references
  └── Happens: Only when the AI decides it needs them
```

**This means you can have many skills installed without performance impact** — only relevant ones get loaded.

---

## ✍️ Examples

<details>
<summary><strong>Example 1: Java Build Skill</strong></summary>

**Directory:**

```text
java-build/
├── SKILL.md
└── common-errors.md
```

**SKILL.md:**

```markdown
---
name: java-build
description: >
  Compile and run Java source files from the command line.
  Use when asked to build, compile, run, or troubleshoot Java compilation errors.
---

# Java Build Skill

## Compile and Run
javac src/Main.java && java -cp src Main

## Common Errors
See [common-errors.md](./common-errors.md) for troubleshooting.
```

</details>

<details>
<summary><strong>Example 2: Run Tests Skill (with template file)</strong></summary>

**Directory:**

```text
run-tests/
├── SKILL.md
└── examples/
    └── SampleTest.java
```

**SKILL.md:**

```markdown
---
name: run-tests
description: >
  Run JUnit 5 tests for the Java learning project.
  Use when asked to run tests, fix failing tests, or create test files.
---

# Run Tests Skill

## Test Conventions
- File: `<ClassName>Test.java`
- Method: `should_<expected>_when_<condition>`
- Pattern: Arrange-Act-Assert

## Template
See [SampleTest.java](./examples/SampleTest.java) for standard structure.
```

</details>

<details>
<summary><strong>Example 3: Create Class Skill (with Java template)</strong></summary>

**Directory:**

```sql
create-class/
├── SKILL.md
└── ClassTemplate.java
```

**SKILL.md:**

```markdown
---
name: create-class
description: >
  Create a new Java class following project conventions.
  Use when asked to create, scaffold, or generate a new Java class.
---

# Create Class Skill

Use [ClassTemplate.java](./ClassTemplate.java) as the starting point.

## Structure Order
1. Fields → 2. Constructors → 3. Public methods → 4. Private methods → 5. toString/equals/hashCode
```

</details>

---

## 📂 How to Create a Skill

### Step-by-Step

1. Create the skills root (if missing): `.github/skills/`
2. Create a subdirectory: `.github/skills/<skill-name>/`
3. Create: `.github/skills/<skill-name>/SKILL.md`
4. Add YAML frontmatter with `name` (required) and `description` (required)
5. Write instructions in the body
6. *(Optional)* Add scripts, templates, or examples alongside SKILL.md
7. Save — skill is available immediately

### Choosing a Good Description

The description is **critical** — it determines when the skill gets loaded:

| ❌ Bad (Too Vague) | ✅ Good (Specific) |
|---|---|
| `Helps with building` | `Compile and run Java source files. Use when asked to build, compile, run, or troubleshoot compilation errors.` |
| `Testing stuff` | `Run JUnit 5 tests. Use when asked to run tests, fix failing tests, or create test files.` |
| `Class creation` | `Create a new Java class following project conventions. Use when asked to create, scaffold, or generate a new class.` |

**Tips:**
- Include **action verbs** users might say: "build", "compile", "run", "create", "test"
- Include **synonyms**: "build" AND "compile", "create" AND "scaffold"
- Be explicit: "Use when asked to..."

---

## 💡 Skill Ideas

| Skill Name | Description | Resources to Include |
|---|---|---|
| `java-build` | Compile and run Java files | Common errors doc |
| `run-tests` | Run JUnit tests | Test template |
| `create-class` | Scaffold a Java class | Class template |
| `create-interface` | Scaffold a Java interface | Interface template |
| `design-pattern` | Implement common design patterns | Pattern templates |
| `add-logging` | Add proper logging to a class | Logger setup example |
| `git-workflow` | Common Git operations | Command cheat sheet |

---

## 📌 File Audience Note

Each skill folder contains:

| File | Audience | Purpose |
|---|---|---|
| `SKILL.md` | 🤖 **Copilot** | Skill definition + instructions — auto-loaded when your question matches the description |
| Resource files (`.sh`, `.java`, `.md`) | 🤖 **Copilot** | Supporting resources the AI can read and use |
| `README.md` (skills root) | 👤 **Developer** | This guide — Copilot does not read it |

When you edit a `SKILL.md`, you change the knowledge Copilot has access to. When you edit this README, nothing changes in Copilot.

> 📖 **Full breakdown:** [File Reference →](../docs/file-reference.md)

---

## 🧪 Experiments to Try

1. **Create `java-build` skill** → ask *"How do I compile Main.java?"* → does it use your skill?
2. **Check skill loading** → right-click Chat → Diagnostics → verify skill appears
3. **Add a resource file** to your skill → ask Copilot about it → does it read the resource?
4. **Make a vague description** → notice skill doesn't load → make it specific → now it loads
5. **Create a skill with a script** → add a `.sh` or `.bat` file → ask Copilot to run it

---

<p align="center">

[← Back to main guide](../README.md) · [Instructions](../instructions/README.md) · [Agents](../agents/README.md) · [Prompts](../prompts/README.md) · [File Reference](../docs/file-reference.md) · [Getting Started](../docs/getting-started.md) · [Customization Guide](../docs/customization-guide.md)

</p>
