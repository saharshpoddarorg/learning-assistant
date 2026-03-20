# 🚀 Getting Started — Hands-On Tutorial

> **Time:** ~30 minutes
> **Goal:** Understand the customization system and try every primitive hands-on.
> **Prerequisites:** VS Code with GitHub Copilot extension installed.

| Audience | Recommendation |
|---|---|
| 🟢 **Newbie** | Follow every step in order — don’t skip |
| 🟡 **Amateur** | Do Steps 1–4, skim Steps 5–7 |
| 🔴 **Pro** | Jump to [Step 7: Create Your Own](#step-7-create-your-own) or [Customization Guide](customization-guide.md) |

> **Not sure where to start?** See [START-HERE.md](START-HERE.md) first.

---

## 📑 Table of Contents

- [Step 1: Open the Project](#step-1-open-the-project)
- [Step 2: Understand the Structure](#step-2-understand-the-structure)
- [Step 3: Test Instructions (Auto-Loading)](#step-3-test-instructions-auto-loading)
- [Step 4: Try an Agent](#step-4-try-an-agent)
- [Step 5: Try a Prompt](#step-5-try-a-prompt)
- [Step 6: Try a Skill](#step-6-try-a-skill)
- [Step 7: Create Your Own](#step-7-create-your-own)
- [Troubleshooting](#-troubleshooting)
- [What's Next?](#-whats-next)

---

## Step 1: Open the Project

1. Open VS Code
2. **File → Open Folder** → select the `learning-assistant` folder
3. Open `src/Main.java` to have a Java file active

> 💡 Copilot reads the `.github/` folder automatically when you open the workspace. No setup needed.

---

## Step 2: Understand the Structure

Your `.github/` folder contains everything Copilot uses to customize its behavior:

```text
.github/
│
├── copilot-instructions.md              ← Project-wide rules (always active)
│
├── instructions/                        ← Path-scoped coding standards
│   ├── java.instructions.md             ← Java conventions (applyTo: **/*.java)
│   └── clean-code.instructions.md       ← Clean code rules (applyTo: **/*.java)
│
├── agents/                              ← Specialist AI personas
│   ├── designer.agent.md                ← Architecture & design reviewer
│   ├── debugger.agent.md                ← Root cause analyst
│   ├── impact-analyzer.agent.md         ← Change impact assessor
│   ├── learning-mentor.agent.md         ← Concept teacher
│   ├── code-reviewer.agent.md           ← Code quality reviewer
│   ├── daily-assistant.agent.md            ← Daily life assistant (non-SE)
│   └── Thinking-Beast-Mode.agent.md        ← Deep research agent (autonomous)
│
├── prompts/                             ← Reusable slash commands
│   ├── hub.prompt.md                    ← /hub (master navigation)
│   ├── dsa.prompt.md                    ← /dsa (data structures & algorithms)
│   ├── system-design.prompt.md          ← /system-design (HLD/LLD)
│   ├── devops.prompt.md                 ← /devops (CI/CD, Docker, K8s)
│   ├── language-guide.prompt.md         ← /language-guide (any language)
│   ├── tech-stack.prompt.md             ← /tech-stack (frameworks, DBs)
│   ├── sdlc.prompt.md                   ← /sdlc (phases & methodologies)
│   ├── daily-assist.prompt.md           ← /daily-assist (finance, productivity)
│   ├── career-roles.prompt.md           ← /career-roles (job roles, pay)
│   ├── multi-session.prompt.md          ← /multi-session (cross-session state)
│   ├── design-review.prompt.md          ← /design-review
│   ├── debug.prompt.md                  ← /debug
│   ├── impact.prompt.md                 ← /impact
│   ├── teach.prompt.md                  ← /teach
│   ├── refactor.prompt.md               ← /refactor
│   ├── explain.prompt.md                ← /explain
│   ├── composite.prompt.md              ← /composite (combine modes)
│   ├── context.prompt.md                ← /context (continue/fresh)
│   ├── scope.prompt.md                  ← /scope (generic/specific)
│   ├── learn-from-docs.prompt.md        ← /learn-from-docs (official docs)
│   ├── explore-project.prompt.md        ← /explore-project (OSS study)
│   ├── deep-dive.prompt.md              ← /deep-dive (concept mastery)
│   ├── reading-plan.prompt.md           ← /reading-plan (study plan)
│   ├── learn-concept.prompt.md          ← /learn-concept (any CS/SE concept)
│   ├── interview-prep.prompt.md         ← /interview-prep (DSA/system design)
│   ├── mcp.prompt.md                    ← /mcp (MCP protocol & server development)
│   ├── brain-new.prompt.md              ← /brain-new (create inbox/notes note)
│   ├── brain-publish.prompt.md          ← /brain-publish (publish & commit to archive)
│   └── brain-search.prompt.md           ← /brain-search (search across tiers)
│
├── skills/                              ← Auto-loaded tool kits
│   ├── java-build/SKILL.md              ← Compile & run help
│   ├── design-patterns/SKILL.md         ← Pattern decision guide
│   ├── java-debugging/SKILL.md          ← Exception diagnosis
│   ├── java-learning-resources/SKILL.md ← Curated Java learning resource index
│   ├── software-engineering-resources/SKILL.md ← Comprehensive SE/CS resource index (DSA, system design, DevOps, Git, industry, trends)
│   ├── daily-assistant-resources/SKILL.md ← Daily life resources (finance, productivity)
│   ├── career-resources/SKILL.md        ← Career data (roles, skills, pay)
│   └── mcp-development/SKILL.md         ← MCP protocol, server building, agent patterns (1,980 lines)
│
└── docs/                                ← Documentation (you are here)
    ├── getting-started.md               ← This tutorial
    ├── customization-guide.md           ← Architecture deep-dive
    ├── file-reference.md                ← Who reads what (🤖 vs 👤)
    ├── navigation-index.md              ← Master index of all commands & files
    └── slash-commands.md                ← Developer slash command reference
```

> 💡 **Important distinction:** Not all files in `.github/` are read by Copilot. Files like `README.md` and `docs/*.md` are for **you** (the developer). Files like `*.agent.md`, `*.prompt.md`, `*.instructions.md`, and `SKILL.md` are read by **Copilot** (the AI). See [File Reference](file-reference.md) for the complete breakdown.

**Quick mental model:**

| What | When | How |
|---|---|---|
| **Instructions** | Auto (by file type) | Copilot follows your coding rules |
| **Agents** | You pick from dropdown | Copilot becomes a specialist |
| **Prompts** | You type `/command` | Runs a pre-built workflow |
| **Skills** | Auto (by topic) | Copilot gets extra tools & knowledge |

---

## Step 3: Test Instructions (Auto-Loading)

Instructions load automatically when you edit a matching file. Let's verify:

### Test 3a: Naming Conventions

1. Open `src/Main.java`
2. Open Copilot Chat (Ctrl+Shift+I)
3. Type: *"Write a method that calculates the area of a circle"*
4. ✅ **Expected:** Method named `calculateCircleArea` (lowerCamelCase), with Javadoc, using `final` for pi constant

### Test 3b: Error Handling

1. Still in `Main.java`
2. Ask: *"Add file reading with error handling"*
3. ✅ **Expected:** try-with-resources, specific exception (e.g., `IOException`), helpful error message — NOT `catch (Exception e) {}`

### Test 3c: Variable Names

1. Ask: *"Create a method to calculate total price with tax"*
2. ✅ **Expected:** Variables like `totalPrice`, `taxRate` — NOT `tp`, `t`, or `x`

> 💡 If these aren't working, check that your `.java` instructions have `applyTo: "**/*.java"` in their frontmatter.

---

## Step 4: Try an Agent

Agents are specialist personas you select from the Chat dropdown.

### Try the Designer Agent

1. Open Copilot Chat
2. Click the **agent dropdown** (top of chat panel) → select **Designer**
3. Type: *"Review Main.java for SOLID principle violations"*
4. ✅ **Expected:** A structured design analysis with SOLID breakdown, actionable suggestions, and optionally a handoff offer to the Impact Analyzer

### Try the Learning Mentor Agent

1. Switch agent → select **Learning Mentor**
2. Type: *"Explain the Strategy pattern with a Java example"*
3. ✅ **Expected:** A patient, multi-part explanation with theory, analogy, code example, and a practice exercise

### Try the Debugger Agent

1. Switch agent → select **Debugger**
2. Type: *"I'm getting a NullPointerException in my code, how do I approach debugging it?"*
3. ✅ **Expected:** Systematic hypothesis-driven debugging approach, stack trace analysis guidance

---

## Step 5: Try a Prompt

Prompts are reusable workflows you invoke with `/command`.

### Try the Teach Prompt

1. Open Copilot Chat (in any agent mode)
2. Type: `/teach` and press Tab to autocomplete
3. When prompted, enter a topic: *"dependency injection"*
4. ✅ **Expected:** A structured lesson about dependency injection

### Try the Design Review Prompt

1. Open `src/Main.java` (have it in focus)
2. In chat, type: `/design-review`
3. ✅ **Expected:** A design-level review of your current file

### Other Prompts to Try

| Command | What It Does |
|---|---|
| `/debug` | Guided debugging workflow |
| `/impact` | Analyze impact of a proposed change |
| `/refactor` | Suggest refactoring for current file |
| `/explain` | Explain the current file's purpose |
| `/composite` | Combine multiple modes in one session |
| `/context` | Continue prior conversation or start fresh |
| `/scope` | Generic learning vs code/domain-specific |

---

## Step 6: Try a Skill

Skills load automatically based on your question topic.

### Try the Build Skill

1. In Copilot Chat, ask: *"How do I compile and run Main.java?"*
2. ✅ **Expected:** Copilot uses the `java-build` skill to give you specific compile/run commands

### Try the Design Patterns Skill

1. Ask: *"Which design pattern should I use for handling different payment methods?"*
2. ✅ **Expected:** Copilot loads the `design-patterns` skill and provides a structured pattern recommendation (likely Strategy pattern)

### Try the Debugging Skill

1. Ask: *"I'm getting a ClassCastException, what should I check?"*
2. ✅ **Expected:** Copilot loads the `java-debugging` skill with exception-specific diagnosis steps

---

## Step 7: Try the Meta-Prompts

Three special prompts control **how** Copilot works, not just what it works on.

### Try /composite — Combine Modes

1. Open `src/Main.java`
2. Type: `/composite`
3. When prompted for modes, enter: `refactor, design-review`
4. ✅ **Expected:** A unified report combining refactoring suggestions AND design review findings, deduplicated and prioritized

### Try /context — Continue or Start Fresh

1. After the composite analysis above, type: `/context`
2. Enter `continue` for context mode
3. Summarize: "We just did a composite refactor + design review of Main.java"
4. Enter a follow-up task: "Now implement the top-priority refactoring"
5. ✅ **Expected:** Copilot acknowledges the prior analysis and builds on it without re-analyzing

### Try /scope — Generic vs Specific

1. Type: `/scope`
2. Enter `generic`
3. Enter topic: "What is the Strategy pattern?"
4. ✅ **Expected:** A conceptual explanation with standalone examples, NOT referencing your project code
5. Now try again with `specific` and topic: "Apply the Strategy pattern here"
6. ✅ **Expected:** Concrete suggestions anchored to your actual codebase

---

## Step 8: Try the Learning Prompts

Four prompts are designed for **learning from external resources** — documentation, open-source projects, and structured study.

### Try /learn-from-docs

1. Type: `/learn-from-docs`
2. Enter concept: `sealed classes`
3. ✅ **Expected:** Explanation grounded in official Oracle/JEP documentation, with simplified translations of formal language, practical code examples, and links to the original docs

### Try /explore-project

1. Type: `/explore-project`
2. Enter project: `Guava`
3. Enter focus: `API design`
4. ✅ **Expected:** Architecture walkthrough, design patterns identified with specific class references, coding practices to learn from, and files to read first

### Try /deep-dive

1. Type: `/deep-dive`
2. Enter concept: `generics`
3. Enter level: `intermediate`
4. ✅ **Expected:** Multi-layered explanation starting from your level, progressing through official docs, real-world patterns, edge cases, and a practice exercise

### Try /reading-plan

1. Type: `/reading-plan`
2. Enter topic: `design patterns`
3. Enter time: `30 min/day for 2 weeks`
4. Enter level: `beginner`
5. ✅ **Expected:** A phased learning roadmap with a day-by-day schedule, curated resources (official docs, tutorials, books, OSS projects), and practice checkpoints

### Try /learn-concept

1. Type: `/learn-concept`
2. Enter concept: `CAP theorem` (or any CS/SE concept like `deadlocks`, `B-trees`, `TCP handshake`)
3. Enter domain: `system design` (or leave blank for auto-detect)
4. Enter level: `beginner`
5. ✅ **Expected:** Language-agnostic explanation with analogy, visual representation, real-world systems, connections, and a hands-on exercise

### Try /interview-prep

1. Type: `/interview-prep`
2. Enter type: `DSA` (or `system-design-HLD`, `system-design-LLD`)
3. Enter topic: `sliding window` (or `design URL shortener`, etc.)
4. Enter level: `mid-level`
5. ✅ **Expected:** Pattern identification, template code, problem progression with LeetCode references, and interview strategy tips

---

## Step 10: Try the Domain-Specific Commands

Eight specialized prompts provide **hierarchical navigation** across all learning domains.

### Try /hub — Navigation Index

1. Type: `/hub`
2. Enter category: `se` (or `daily`, or leave blank for full index)
3. ✅ **Expected:** A complete navigation tree showing all available commands organized by category

### Try /dsa — Data Structures & Algorithms

1. Type: `/dsa`
2. Enter topic: `binary trees` (or `sliding window`, `dynamic programming`)
3. Enter goal: `learn` (or `interview-prep`, `compare`)
4. ✅ **Expected:** Hierarchical topic breakdown, explanation with complexities, pattern identification, and practice problems

### Try /system-design — HLD/LLD

1. Type: `/system-design`
2. Enter level: `HLD` (or `LLD`)
3. Enter topic: `design URL shortener` (or `class diagram for parking lot`)
4. ✅ **Expected:** Structured design walkthrough with internal hierarchy — for HLD: requirements → estimation → architecture; for LLD: class design → API → schema

### Try /devops — CI/CD, Docker, Cloud

1. Type: `/devops`
2. Enter topic: `Docker` (or `GitHub Actions`, `Kubernetes`, `Terraform`)
3. Enter goal: `learn-concept` (or `setup-pipeline`, `compare-tools`)
4. ✅ **Expected:** Concept explanation with architecture context, tool comparison, and practical setup guidance

### Try /language-guide — Language Learning

1. Type: `/language-guide`
2. Enter language: `Rust` (or `Go`, `Python`, `C++`, `Java`)
3. Enter level: `beginner`
4. ✅ **Expected:** 6-level learning hierarchy from foundations to real-world, with a language quick-reference card

### Try /tech-stack — Frameworks & Databases

1. Type: `/tech-stack`
2. Enter category: `backend` (or `frontend`, `database`, `messaging`)
3. Enter goal: `compare Spring Boot vs FastAPI`
4. ✅ **Expected:** Structured comparison table with trade-offs, use cases, and learning resources

### Try /sdlc — SDLC Phases & Methodologies

1. Type: `/sdlc`
2. Enter focus: `testing` (or `deployment`, `agile vs waterfall`, `overview`)
3. ✅ **Expected:** Phase-by-phase breakdown with activities, deliverables, and methodology comparison

### Try /daily-assist — Daily Life Assistant

1. Type: `/daily-assist`
2. Enter category: `finance` (or `productivity`, `news`, `research`)
3. Enter request: `explain SIP vs lumpsum investing` (or `create a weekly study plan`)
4. ✅ **Expected:** Structured, actionable guidance with frameworks and tools

### Try /career-roles — Tech Career Exploration

1. Type: `/career-roles`
2. Enter role: `MLE` (or `SDE`, `DevOps`, `Software Architect`, `Tech Lead`)
3. Enter goal: `overview` (or `skills`, `pay`, `compare`, `roadmap`)
4. Enter level: `mid`
5. ✅ **Expected:** Full role profile with skills matrix, pay ranges, synonymous titles, and career roadmap

### Try /multi-session — Cross-Session State

1. Type: `/multi-session`
2. Enter action: `save-state`
3. Enter task: describe your current work
4. ✅ **Expected:** A session state file or handoff summary that you can paste into a new chat to resume work

---

## Step 9: Create Your Own

Now that you've tried everything, create your own customization! Pick one:

<details>
<summary><strong>Option A: Create an Instruction File</strong></summary>

Create `.github/instructions/logging.instructions.md`:

```markdown
---
applyTo: "**/*.java"
---

# Logging Standards

- Use SLF4J Logger, not System.out.println
- Log at appropriate levels: ERROR for exceptions, INFO for business events, DEBUG for diagnostic
- Include context in log messages: "Failed to load user [userId={}]"
- Never log sensitive data (passwords, tokens, PII)
```

Test it: Open `Main.java` → ask *"Add logging to this class"*

</details>

<details>
<summary><strong>Option B: Create a Prompt</strong></summary>

Create `.github/prompts/document.prompt.md`:

```markdown
---
agent: ask
description: Generate Javadoc for all public methods in the current file
---

Review the active file and add comprehensive Javadoc comments to every public method.
Include @param, @return, and @throws tags where applicable.
Follow the project's Javadoc conventions from the instructions.
```

Test it: Open `Main.java` → type `/document`

</details>

<details>
<summary><strong>Option C: Create a Simple Agent</strong></summary>

Create `.github/agents/explainer.agent.md`:

```markdown
---
description: Explains code concepts using simple analogies and real-world comparisons
tools:
  - search
  - codebase
---

You are a patient code explainer. When the user asks about any concept:

1. Start with a real-world analogy
2. Show the simplest possible code example
3. Explain each line
4. Connect it back to the analogy
5. Suggest what to learn next

Always use Java examples from this project when possible.
Keep explanations beginner-friendly — no jargon without explanation.
```

Test it: Select **Explainer** from the agent dropdown → ask about any concept

</details>

---

## 🔧 Troubleshooting

<details>
<summary><strong>Agents don't appear in the dropdown</strong></summary>

- ✅ Verify the file ends with `.agent.md`
- ✅ Check YAML frontmatter has `description:`
- ✅ Reload VS Code: `Ctrl+Shift+P` → "Developer: Reload Window"
- ✅ Confirm files are in `.github/agents/`

</details>

<details>
<summary><strong>Prompts don't show up as slash commands</strong></summary>

- ✅ Verify the file ends with `.prompt.md`
- ✅ Check it's in `.github/prompts/`
- ✅ Ensure YAML frontmatter is valid
- ✅ Try typing `/` then the first few letters and wait for autocomplete

</details>

<details>
<summary><strong>Instructions aren't being followed</strong></summary>

- ✅ Check the `applyTo` glob — try `**/*.java` first
- ✅ Open a file that matches the pattern
- ✅ Ask Copilot: *"What instructions are active for this file?"*
- ✅ Keep instructions concise — very long instructions get diluted

</details>

<details>
<summary><strong>Skills don't seem to load</strong></summary>

- ✅ Check that `SKILL.md` exists (exact filename, uppercase)
- ✅ Verify the `description` frontmatter includes keywords matching your question
- ✅ Try adding synonyms to the description: "build", "compile", "run"
- ✅ Right-click in Chat → Diagnostics to see what loaded

</details>

---

## 🗺️ What's Next?

| Your Goal | Where to Go |
|---|---|
| Quick 5-minute overview of all types | [Customization 5-Minute Guide](copilot-customization-newbie.md) |
| All 6 types compared with migration paths | [Customization Deep Dive](copilot-customization-deep-dive.md) |
| Latest Copilot features (AGENTS.md, handoffs, etc.) | [Deep Dive §Latest Features](copilot-customization-deep-dive.md#part-11-latest-features--api-updates-2026) |
| Why each type exists (FAQ) | [Deep Dive §Why Not Just Skills?](copilot-customization-deep-dive.md#part-9-why-not-just-skills--the-faq) |
| Migrate content between types | [Deep Dive §Migration Guide](copilot-customization-deep-dive.md#part-7-migration--interchange-guide) |
| Official Microsoft/GitHub docs | [Deep Dive §Official Resources](copilot-customization-deep-dive.md#part-10-official-resources--standards) |
| Understand how everything connects | [Customization Guide](customization-guide.md) |
| Copy to your work/personal project (newbie) | [Newbie Export Guide](export-newbie-guide.md) |
| Full export with all options | [Export Guide](export-guide.md) |
| See which files Copilot reads vs developer docs | [File Reference](file-reference.md) |
| Learn about agents in depth | [Agents Guide](../agents/README.md) |
| Learn about prompts in depth | [Prompts Guide](../prompts/README.md) |
| Learn about skills in depth | [Skills Guide](../skills/README.md) |
| Learn about instructions in depth | [Instructions Guide](../instructions/README.md) |
| See the full project overview | [Main README](../README.md) |

---

<p align="center">

[← Back to main guide](../README.md) · [Customization Guide](customization-guide.md) · [File Reference](file-reference.md) · [Slash Commands](slash-commands.md) · [Agents](../agents/README.md) · [Prompts](../prompts/README.md) · [Skills](../skills/README.md) · [Instructions](../instructions/README.md)

</p>
