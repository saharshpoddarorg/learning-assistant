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
- [Content Depth Structure](#content-depth-structure)
- [Naming Conventions](#naming-conventions)
- [Skills Roadmap](#skills-roadmap)
- [Anti-Patterns to Avoid](#anti-patterns-to-avoid)

---

## What Are Skills?

A **skill** is a Copilot customization file (`.github/skills/<name>/SKILL.md`) that:

1. Contains **domain-specific knowledge** that Copilot doesn't have natively (or has incompletely)
2. **Auto-activates** when the user asks about the skill's domain (based on the `description` field)
3. Is **read once** when a relevant request is detected — not loaded on every message
4. Contains **three depth levels** (Essentials → Patterns & Workflows → Advanced Reference) that Copilot selects from based on the complexity of the request

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

```text
Software Engineering (broadest domain)
│
├── Languages & Platforms
│   ├── Java (java-build, java-debugging, java-formatting)
│   ├── JVM Platform (jvm-platform)
│   └── [Other languages — planned: python-build, javascript-build]
│
├── Design & Architecture
│   ├── Design Patterns & SOLID (design-patterns)
│   └── [planned: software-design, api-design]
│
├── Development Process
│   ├── Requirements (requirements-research)
│   ├── Roles (software-development-roles)
│   ├── Deep Research (deep-research)
│   └── [planned: qa-testing]
│
├── DevOps & Tooling
│   ├── Git VCS (git-vcs)
│   ├── GitHub Workflow (github-workflow)
│   ├── MCP Development (mcp-development)
│   ├── macOS Dev (mac-dev)
│   └── Copilot Customization (copilot-customization)
│
├── Learning Resources
│   ├── Learning Resources Vault (learning-resources-vault)
│   ├── SE Resources (software-engineering-resources)
│   └── Java Resources (java-learning-resources)
│
├── Knowledge Management
│   ├── Brain Management (brain-management)
│   ├── PKM Management (pkm-management)
│   └── Digital Notetaking (digital-notetaking)
│
├── Career & Professional Development
│   └── Career Resources (career-resources)
│
└── Daily Life & Productivity
    └── Daily Assistant (daily-assistant-resources)
```

### Axis 2 — Roles (cross-cutting)

```text
Team Roles (cross-cutting across all domains)
├── Product Owner (software-development-roles/SKILL.md)
├── Developer (software-development-roles/SKILL.md)
└── QA / Tester (software-development-roles/SKILL.md)
```

### Axis 3 — Methods (cross-cutting)

```text
Research & Analysis Methods (cross-cutting)
├── Deep Research / Investigation (deep-research/SKILL.md)
└── Requirements Research (requirements-research/SKILL.md)  ← also in Axis 1

Environment & Tooling
├── macOS Dev Setup (mac-dev/SKILL.md)
├── Java Build & Run (java-build/SKILL.md)
└── GitHub Workflow (github-workflow/SKILL.md)

Knowledge Management
├── Digital Note-Taking & PKM (digital-notetaking/SKILL.md)
├── ai-brain Workspace Management (brain-management/SKILL.md)
└── PKM Capture & Operations (pkm-management/SKILL.md)

Copilot Customization
└── Customizing Copilot (copilot-customization/SKILL.md)
```

---

## Current Skills Inventory

| # | Skill | Domain | Category | Status | Lines |
|---|---|---|---|---|---|
| 1 | `java-build` | SE > Languages & Platforms | Java | Active | ~200 |
| 2 | `java-debugging` | SE > Languages & Platforms | Java | Active | ~300 |
| 3 | `java-formatting` | SE > Languages & Platforms | Java (opt-in) | Active | ~250 |
| 4 | `java-learning-resources` | SE > Learning Resources | Java | Active | ~500 |
| 5 | `jvm-platform` | SE > Languages & Platforms | JVM | Active | ~400 |
| 6 | `design-patterns` | SE > Design & Architecture | Patterns | Active | ~400 |
| 7 | `deep-research` | SE > Development Process | Method (cross-cutting) | Active | ~350 |
| 8 | `requirements-research` | SE > Development Process | Requirements | Active | ~400 |
| 9 | `software-development-roles` | SE > Development Process | Roles (cross-cutting) | Active | ~500 |
| 10 | `git-vcs` | SE > DevOps & Tooling | VCS | Active | ~350 |
| 11 | `github-workflow` | SE > DevOps & Tooling | GitHub | Active | ~450 |
| 12 | `mcp-development` | SE > DevOps & Tooling | MCP/Agentic | Active | ~1,980 |
| 13 | `mac-dev` | SE > DevOps & Tooling | macOS | Active | ~400 |
| 14 | `copilot-customization` | SE > DevOps & Tooling | Copilot | Active | ~500 |
| 15 | `software-engineering-resources` | SE > Learning Resources | Broad SE/CS | Active | ~600 |
| 16 | `learning-resources-vault` | SE > Learning Resources | Master vault | Active | ~800 |
| 17 | `brain-management` | Knowledge Management | ai-brain workspace | Active | ~300 |
| 18 | `pkm-management` | Knowledge Management | PKM capture & ops | Active | ~400 |
| 19 | `digital-notetaking` | Knowledge Management | PKM tools | Active | ~500 |
| 20 | `career-resources` | Career & Professional Dev | Career paths | Active | ~400 |
| 21 | `daily-assistant-resources` | Daily Life & Productivity | Productivity/Finance | Active | ~300 |
| 22 | `atlassian-tools` | SE > DevOps & Tooling | Atlassian | Active | ~350 |

**Total: 22 skills** — see [TAXONOMY.md](../skills/TAXONOMY.md) for the full hierarchical index.

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

```text
.github/skills/<name>/SKILL.md
```

Naming: all lowercase, hyphenated (`requirements-research`, not `RequirementsResearch`).

### Step 4 — Write the file (see format below)

### Step 5 — Register it

After creating:
- [ ] Add to `copilot-instructions.md` OS-Specific Skill Routing table (or general routing)
- [ ] Add to `navigation-index.md` Skills Quick Reference table + File Map
- [ ] Add to this file's **Current Skills Inventory** table
- [ ] Add to `TAXONOMY.md` — taxonomy tree + category table + cross-reference matrix
- [ ] Add to `hub.prompt.md` navigation tree (appropriate category)
- [ ] Create a `.prompt.md` slash command if the skill warrants direct invocation
- [ ] Add to `slash-commands.md` quick lookup table + detailed entry (if prompt created)
- [ ] Run `.\mcp-servers\build.ps1` to verify no build break
- [ ] Run `.\__md_lint.ps1` to verify markdown formatting

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

## Content Depth Structure

Every skill must have three depth levels. No exceptions.

### Essentials ("What is this? Core concepts and quick answers")

- **Purpose:** Core vocabulary, fundamental concepts, and immediate answer templates — always consulted first
- **Format:** Short explanations, simple examples, quick-start commands or checklists
- **Length:** 20-50 lines
- **Includes:**
  - Core concept in 1-2 sentences
  - Most important example (the simplest useful one)
  - A quick-start checklist or decision table
  - Common confusion to clear up

### Patterns & Workflows ("How do I apply this in practice?")

- **Purpose:** Applied methodologies, step-by-step workflows, and decision frameworks
- **Format:** Tables, workflows, decision guides, templates
- **Length:** 50-150 lines
- **Includes:**
  - Key techniques, patterns, or options — in a table or structured list
  - When to use X vs. Y (decision guidance)
  - A workflow or process with steps
  - Common pitfalls and how to avoid them

### Advanced Reference ("What are the deep frameworks and edge cases?")

- **Purpose:** Expert-level patterns, full rubrics, and advanced decision frameworks
- **Format:** Deep-dive theory, advanced patterns, architecture decisions, full templates
- **Length:** 50-200 lines
- **Includes:**
  - Advanced concepts not covered in Essentials or Patterns & Workflows
  - Architecture or design implications
  - Industry-standard practices and why they exist
  - Specific metrics, evaluation rubrics, or full decision frameworks

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

## Essentials — <Topic> Fundamentals

### Core concept
...

### Quick start
```

code example or checklist

```yaml

---

## Patterns & Workflows — <Topic> in Practice

### Key techniques
...table or structured list...

---

## Advanced Reference — <Topic> Deep Patterns

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

### Community-Informed (from awesome-copilot, skills.sh research)

These skills were identified from community sources and align with gaps in the current
taxonomy. Import using the protocol in
[customization-evolution-guide.md](customization-evolution-guide.md).

| Skill | Domain | Source Inspiration | Import Strategy |
|---|---|---|---|
| `spring-boot-testing` | SE > Java > Testing | awesome-copilot `java-springboot` | Import + merge with planned `qa-testing` |
| `java-junit` | SE > Java > Testing | awesome-copilot `java-junit` | Import new — unit testing specifics |
| `quality-playbook` | SE > SD > Quality | awesome-copilot `quality-playbook` | Import + merge with planned `software-design` |
| `security-review` | SE > Security | awesome-copilot `security-review` | Merge with planned `security-engineering` |
| `agent-governance` | Copilot | awesome-copilot `agent-governance` | Import new — agent security and guardrails |
| `cloud-design-patterns` | SE > Systems | awesome-copilot `cloud-design-patterns` | Merge with planned `distributed-systems` |
| `code-tour` | Cross-cutting | awesome-copilot `code-tour` | Import new — codebase walkthrough |

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

---

## Further Reading

| Document | What It Covers |
|---|---|
| [TAXONOMY.md](../skills/TAXONOMY.md) | Hierarchical skill index, cross-refs, adding + importing skills |
| [customization-evolution-guide.md](customization-evolution-guide.md) | Import, merge, evolve primitives — framework growth guide |
| [copilot-primitives-crosswalk.md](copilot-primitives-crosswalk.md) | Primitive comparison, conversion paths, edge cases |
| [navigation-index.md](navigation-index.md) | Master navigation for all files, commands, skills |
| [change-completeness.instructions.md](../instructions/change-completeness.instructions.md) | Completeness checklist including Section O (Primitive Fitness Review) |
