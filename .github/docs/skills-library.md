# Skills Library — Organization Guide & Creation Reference

> **Audience:** Anyone adding new skills or navigating the skills hierarchy
> **Purpose:** Define the taxonomy, creation guidelines, and planned roadmap for the `.github/skills/` library
> **When to use:** Before creating a new skill; when deciding which skill to use; when planning the skills backlog

---

## 📑 Table of Contents

- [What Are Skills?](#what-are-skills)
- [The Skills Hierarchy](#the-skills-hierarchy)
- [Current Skills Inventory](#current-skills-inventory)
- [How to Create a New Skill](#how-to-create-a-new-skill)
- [The Description Field — Most Important Part](#the-description-field--most-important-part)
- [3-Tier Content Structure](#3-tier-content-structure)
- [Naming Conventions](#naming-conventions)
- [Skills Roadmap](#skills-roadmap)
- [Anti-Patterns to Avoid](#anti-patterns-to-avoid)

---

## What Are Skills?

A **skill** is a Copilot customization file (`.github/skills/<name>/SKILL.md`) that:

1. Contains **domain-specific knowledge** that Copilot doesn't have natively (or has incompletely)
2. **Auto-activates** when the user asks about the skill's domain (based on the `description` field)
3. Is **read once** when a relevant request is detected — not loaded on every message
4. Contains **3-tier content** (Newbie → Amateur → Pro) that Copilot uses to give appropriately-levelled responses

**Skills vs. other customization primitives:**

| Primitive | When to use |
|---|---|
| `copilot-instructions.md` | Global rules that apply to EVERY message |
| `.instructions.md` | File-type-specific rules (e.g., `applyTo: **/*.java`) |
| `.prompt.md` | User-invoked slash commands (`/command`) |
| `.agent.md` | Persistent AI persona with tool access |
| `SKILL.md` | **On-demand domain knowledge** — loaded when relevant |
| MCP Server | Dynamic data lookup, external APIs, real-time context |

> **Key insight:** If the knowledge is static and domain-specific, use a skill.
> If you need fresh data or tool execution, use an MCP server.

---

## The Skills Hierarchy

This library organises skills into three axes:

### Axis 1 — Domain / Practice (vertical)
```
Software Engineering (broadest domain)
│
├── Software Development (practice domain)
│   ├── Requirements (requirements-research/SKILL.md)
│   ├── Design (design-patterns/SKILL.md)
│   ├── Implementation (java-build/SKILL.md, java-debugging/SKILL.md)
│   └── Testing/QA [planned]
│
├── Systems & Infrastructure
│   ├── DevOps & CI/CD [covered via software-engineering-resources]
│   └── MCP & Agentic AI (mcp-development/SKILL.md)
│
├── Version Control (git-vcs/SKILL.md)
│
├── Career & Growth
│   ├── Career Paths (career-resources/SKILL.md)
│   └── Interview Prep [via career-resources]
│
└── Resources & Discovery
    ├── General SE Resources (software-engineering-resources/SKILL.md)
    └── Java Resources (java-learning-resources/SKILL.md)
```

### Axis 2 — Roles (cross-cutting)
```
Team Roles (cross-cutting across all domains)
├── Product Owner (software-development-roles/SKILL.md)
├── Developer (software-development-roles/SKILL.md)
└── QA / Tester (software-development-roles/SKILL.md)
```

### Axis 3 — Methods (cross-cutting)
```
Research & Analysis Methods (cross-cutting)
├── Deep Research / Investigation (deep-research/SKILL.md)
└── Requirements Research (requirements-research/SKILL.md)  ← also in Axis 1

Environment & Tooling
├── macOS Dev Setup (mac-dev/SKILL.md)
└── Java Build & Run (java-build/SKILL.md)

Knowledge Management
├── Digital Note-Taking & PKM (digital-notetaking/SKILL.md)
└── ai-brain Workspace Management (brain-management/SKILL.md)

Copilot Customization
└── Customizing Copilot (copilot-customization/SKILL.md)
```

---

## Current Skills Inventory

| Skill | Domain | Axis | Status | Lines |
|---|---|---|---|---|
| `java-build` | Java compilation & execution | Domain/Impl | Active | ~200 |
| `java-debugging` | Java exceptions & debugging | Domain/Impl | Active | ~300 |
| `java-learning-resources` | Java resources index | Resources | Active | ~500 |
| `design-patterns` | OOP patterns (GoF, SOLID) | Domain/Design | Active | ~400 |
| `software-engineering-resources` | Broad SE/CS resources | Resources | Active | ~600 |
| `career-resources` | Tech career paths & data | Career | Active | ~400 |
| `git-vcs` | Git workflows & conventions | VCS | Active | ~350 |
| `digital-notetaking` | PKM, Obsidian, Notion, PARA | Knowledge Mgmt | Active | ~500 |
| `mac-dev` | macOS dev environment | Tooling/macOS | Active | ~400 |
| `mcp-development` | MCP protocol & server dev | MCP/Agentic | Active | ~1,980 |
| `brain-management` | ai-brain naming & routing | Knowledge Mgmt | Active | ~300 |
| `copilot-customization` | GitHub Copilot primitives | Copilot | Active | ~500 |
| `daily-assistant-resources` | Daily productivity & finance | Daily Life | Active | ~300 |
| `deep-research` | Investigation & analysis methodology | Method (cross-cutting) | Active | ~350 |
| `requirements-research` | Requirements gathering & analysis | SE > SD > Req | Active | ~400 |
| `software-development-roles` | PO, Developer, QA workflows | Roles (cross-cutting) | Active | ~500 |

**Total: 16 skills**

---

## How to Create a New Skill

### Step 1 — Decide if a skill is the right primitive

Ask:
- Is the knowledge domain-specific and stable (not needing real-time lookup)? → Skill
- Is it knowledge Copilot already has well? → Skip (don't duplicate)
- Is it invoked by a slash command? → Prompt, not skill
- Does it need fresh data from an API? → MCP server

### Step 2 — Place it in the hierarchy

Before creating, answer:
1. **Which Domain axis?** (SE > SD > Requirements, or Tooling, or Methods, etc.)
2. **Which Axis?** (Domain/Practice? Role? Methodology?)
3. **Does it belong under an existing skill** or is it a new branch?

If it's narrower than an existing skill → consider expanding that skill first.
If it's a genuinely new domain → create a new skill directory.

### Step 3 — Create the file

```
.github/skills/<name>/SKILL.md
```

Naming: all lowercase, hyphenated (`requirements-research`, not `RequirementsResearch`).

### Step 4 — Write the file (see format below)

### Step 5 — Register it

After creating:
- [ ] Add to `copilot-instructions.md` OS-Specific Skill Routing table (or general routing)
- [ ] Add to `navigation-index.md` Skills Quick Reference table + File Map
- [ ] Add to this file's **Current Skills Inventory** table
- [ ] Add to `hub.prompt.md` navigation tree (appropriate category)
- [ ] Run `.\mcp-servers\build.ps1` to verify no build break

---

## The Description Field — Most Important Part

The `description` field in YAML frontmatter determines **when Copilot activates your skill**.
Getting this right is the single most important part of a skill file.

### Rules for a good description field

1. **Exhaustive trigger list** — include all synonyms, related terms, domain vocabulary
2. **"Use when asked about:"** — explicit activation triggers in natural language
3. **"Also activates for:"** — list specific keywords and phrases that should trigger it
4. **Domain context** — state the domain chain (e.g., SE > SD > Requirements)
5. **No duplication** — don't include triggers that belong to another skill

### Template

```yaml
---
name: your-skill-name
description: >
  [2-3 sentence overview of what the skill contains and covers.]
  Domain: [e.g., Software Engineering > Software Development > Requirements]
  Covers: [comma-separated list of specific concepts covered]
  Use when asked about: [natural language triggers — 5-10 phrases]
  Also activates for: [keyword list — specific terms, tools, concepts, commands]
---
```

### Example (good vs. bad)

```yaml
# ❌ BAD — too vague, won't activate reliably
description: Helps with Java stuff.

# ✅ GOOD — exhaustive, precise, activates reliably
description: >
  Java build, compile, and run guidance for command-line and IDE workflows.
  Covers javac, java, classpath, JAR creation, module-path, JDK setup, SDKMAN!,
  compilation errors, manifest files, multi-module projects, and Maven/Gradle basics.
  Use when asked about: how to compile Java, how to run a Java program, classpath errors,
  JAR packaging, JDK installation, Java version issues.
  Also activates for: javac, java.exe, ClassNotFoundException, NoClassDefFoundError,
  module-info.java, java --module-path, jlink, jpackage, SDKMAN!, JAVA_HOME.
```

---

## 3-Tier Content Structure

Every skill must have 3 tiers. No exceptions.

### Tier 1 — Newbie ("What is this? How do I start?")

- **Audience:** Someone new to the domain
- **Format:** Short explanations, simple examples, quick-start commands
- **Length:** 20-50 lines
- **Includes:**
  - Core concept in 1-2 sentences
  - Most important example (the simplest useful one)
  - A quick-start checklist or sequence of steps
  - Common confusion to clear up

### Tier 2 — Amateur ("How do I use this in practice?")

- **Audience:** Someone familiar with basics, now doing real work
- **Format:** Tables, workflows, decision guides, templates
- **Length:** 50-150 lines
- **Includes:**
  - Key techniques, patterns, or options — in a table or structured list
  - When to use X vs. Y (decision guidance)
  - A workflow or process with steps
  - Common pitfalls and how to avoid them

### Tier 3 — Pro ("How do I master this?")

- **Audience:** Someone who knows the domain and wants depth
- **Format:** Deep-dive theory, advanced patterns, architecture decisions
- **Length:** 50-200 lines
- **Includes:**
  - Advanced concepts not covered in Tier 1/2
  - Architecture or design implications
  - Industry-standard practices and why they exist
  - Specific metrics, evaluation rubrics, or decision frameworks

### Format Template

```markdown
---
name: <skill-name>
description: > ...
---

# <Skill Name> Skill

> **Domain:** ... > ...
> **Related skills:** `skill-a`, `skill-b`

---

## Tier 1 — Newbie: <Topic> Fundamentals

### Core concept
...

### Quick start
```
code example or checklist
```

---

## Tier 2 — Amateur: <Topic> in Practice

### Key techniques
...table or structured list...

---

## Tier 3 — Pro: Advanced <Topic>

### Deep dive
...advanced content...
```

---

## Naming Conventions

| Rule | Example |
|---|---|
| All lowercase, hyphenated | `requirements-research` not `RequirementsResearch` |
| Descriptive, not branded | `git-vcs` not `github-skill` |
| Domain-first where applicable | `java-build`, `java-debugging` share the `java-` prefix |
| Method-first for cross-cutting | `deep-research`, `requirements-research` |
| Role-plural for role skills | `software-development-roles` not `developer-role` |
| No version in name | `java-build` not `java17-build` |
| Subdirectory = skill name | `skills/java-build/SKILL.md` — directory name matches `name:` field |

---

## Skills Roadmap

Skills planned for future creation (in priority order):

### High Priority

| Skill | Domain | Description |
|---|---|---|
| `qa-testing` | SE > SD > Testing | Test strategy, TDD, test pyramid, BDD, automation (Selenium, Playwright) |
| `software-design` | SE > SD > Design | LLD, class design, SOLID applied, package design, DDD patterns |
| `distributed-systems` | SE > Systems | CAP, Paxos, Raft, eventual consistency, replication, partitioning |
| `security-engineering` | SE > Security | OWASP Top 10, threat modeling, secrets management, zero trust |

### Medium Priority

| Skill | Domain | Description |
|---|---|---|
| `product-management` | SE > PM | OKRs, roadmapping, user research, product discovery, Lean Startup |
| `data-engineering` | SE > Data | Pipelines, Spark, dbt, Kafka, data quality, lakehouse patterns |
| `api-design` | SE > SD > Design | REST, gRPC, GraphQL API design — contracts, versioning, OpenAPI |
| `python-build` | Python | Python environment, pip, venv, pyproject.toml, uv, ruff |
| `javascript-build` | JavaScript | Node.js, npm/pnpm, ESM, TypeScript compilation, bundling |

### Planned (future iteration)

| Skill | Domain | Description |
|---|---|---|
| `technical-writing` | Cross-cutting | ADRs, RFC process, runbooks, onboarding docs, commit messages |
| `observability` | SE > Infrastructure | Metrics, traces, logs, SLI/SLO/SLA, OpenTelemetry |
| `cloud-aws` | SE > Infrastructure | AWS services, CDK/CloudFormation, IAM, networking, cost optimisation |
| `agile-coaching` | Roles > Agile | Scrum, Kanban, SAFe, retrospectives, estimation, velocity |

---

## Anti-Patterns to Avoid

| Anti-pattern | Why it's a problem | Fix |
|---|---|---|
| **Vague description field** | Skill never activates because Copilot can't match it | Rewrite with exhaustive trigger list + "Use when asked about:" |
| **Monolithic skill** ("software engineering") | Too broad — dilutes focus, hard to maintain | Split by subdomain (design vs. testing vs. requirements) |
| **Duplication with existing skills** | Two skills fight for activation; context bloat | Merge or clearly delineate triggers |
| **Missing Tier 1** | Intimidates newcomers; no entry point | Always start with the simplest possible example |
| **All prose, no tables** | Hard to scan at skill-read speed | Convert comparison content to tables |
| **No "Related skills"** | User can't navigate to adjacent knowledge | Add cross-references to related skills in the header |
| **Hardcoded tool versions** | Skill goes stale | Refer to major versions, not exact (Java 21+, not Java 21.0.2) |
| **Forgetting copilot-instructions.md update** | Skill exists but Copilot doesn't know to activate it | Always update routing table + navigation-index |
