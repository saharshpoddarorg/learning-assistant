# 🎯 Prompt Files — Guide

> **What:** Reusable task templates you invoke as **slash commands** in chat (e.g., `/explain`).  
> **Where:** `.github/prompts/*.prompt.md`  
> **How to use:** Type `/prompt-name` in VS Code Chat.

---

## 📑 Table of Contents

- [What Are Prompt Files?](#-what-are-prompt-files)
- [Prompts in This Project](#-prompts-in-this-project)
- [File Format](#-file-format)
  - [Frontmatter Fields](#frontmatter-fields)
  - [Agent Modes](#agent-modes)
- [Variables Reference](#-variables-reference)
- [Examples](#-examples)
- [How to Create a Prompt](#-how-to-create-a-prompt-file)
- [Tips](#-tips)
- [Prompt Ideas](#-prompt-ideas)
- [Meta-Prompts: Composite, Context & Scope](#-meta-prompts-composite-context--scope)
- [File Audience Note](#-file-audience-note)
- [Experiments to Try](#-experiments-to-try)

---

## 📌 What Are Prompt Files?

Prompt files are **recipes for common tasks.** Instead of typing detailed instructions every time, you save them as a file and invoke with a slash command.

| Without Prompts | With Prompts |
|---|---|
| Type 10 lines of instructions every time | Type `/review` — done |
| Forget a step every third time | Steps are baked into the prompt |
| Each team member uses different wording | Everyone uses the same file from Git |

### How It Looks

```
You type:    /explain
Copilot:     (uses current file automatically)
Result:      Structured explanation of Main.java
```

---

## 🗂️ Prompts in This Project

| Command | File | Agent Used | Purpose |
|---|---|---|---|
| `/design-review` | [`design-review.prompt.md`](design-review.prompt.md) | Designer | Full SOLID/GRASP design review |
| `/debug` | [`debug.prompt.md`](debug.prompt.md) | Debugger | Systematic bug investigation |
| `/impact` | [`impact.prompt.md`](impact.prompt.md) | Impact-Analyzer | Change ripple effect analysis |
| `/teach` | [`teach.prompt.md`](teach.prompt.md) | Learning-Mentor | Learn concepts from code |
| `/refactor` | [`refactor.prompt.md`](refactor.prompt.md) | Designer | Identify refactoring opportunities |
| `/explain` | [`explain.prompt.md`](explain.prompt.md) | Ask | Beginner-friendly file explanation |
| `/composite` | [`composite.prompt.md`](composite.prompt.md) | Agent | Combine multiple modes in one session |
| `/context` | [`context.prompt.md`](context.prompt.md) | Agent | Continue prior conversation or start fresh |
| `/scope` | [`scope.prompt.md`](scope.prompt.md) | Agent | Generic learning vs code/domain-specific |
| `/learn-from-docs` | [`learn-from-docs.prompt.md`](learn-from-docs.prompt.md) | Learning-Mentor | Learn concepts via official documentation |
| `/explore-project` | [`explore-project.prompt.md`](explore-project.prompt.md) | Learning-Mentor | Learn by studying open-source projects |
| `/deep-dive` | [`deep-dive.prompt.md`](deep-dive.prompt.md) | Learning-Mentor | Multi-layered progressive concept exploration |
| `/reading-plan` | [`reading-plan.prompt.md`](reading-plan.prompt.md) | Learning-Mentor | Structured reading/learning plan with resources |
| `/learn-concept` | [`learn-concept.prompt.md`](learn-concept.prompt.md) | Learning-Mentor | Learn any CS/SE concept (language-agnostic) |
| `/interview-prep` | [`interview-prep.prompt.md`](interview-prep.prompt.md) | Learning-Mentor | DSA patterns, system design, interview strategies |
| `/hub` | [`hub.prompt.md`](hub.prompt.md) | Ask | Master navigation index — browse all commands |
| `/dsa` | [`dsa.prompt.md`](dsa.prompt.md) | Learning-Mentor | Data structures & algorithms with pattern hierarchy |
| `/system-design` | [`system-design.prompt.md`](system-design.prompt.md) | Learning-Mentor | Unified HLD/LLD with full internal hierarchy |
| `/devops` | [`devops.prompt.md`](devops.prompt.md) | Learning-Mentor | CI/CD, Docker, Kubernetes, cloud, IaC, monitoring |
| `/language-guide` | [`language-guide.prompt.md`](language-guide.prompt.md) | Learning-Mentor | Language-specific learning framework |
| `/tech-stack` | [`tech-stack.prompt.md`](tech-stack.prompt.md) | Learning-Mentor | Frameworks, libraries, databases — compare & learn |
| `/sdlc` | [`sdlc.prompt.md`](sdlc.prompt.md) | Learning-Mentor | SDLC phases, methodologies, practices |
| `/daily-assist` | [`daily-assist.prompt.md`](daily-assist.prompt.md) | Daily-Assistant | Finance, productivity, news, daily life tasks |

---

## 📄 File Format

```markdown
---
name: explain
description: 'Explain the current file in plain language'
agent: ask
tools: ['search', 'codebase']
---

Your task instructions go here.
Can use variables like ${file} or ${input:question:What do you want to know?}
```

### Frontmatter Fields

| Field | Required? | Description | Example |
|---|---|---|---|
| `name` | No | Slash command name (defaults to filename) | `explain` |
| `description` | No | Short description in the prompt picker | `'Explain the current file'` |
| `agent` | No | Agent to run: `ask`, `agent`, `plan`, or custom | `agent` |
| `model` | No | AI model to use | `Claude Sonnet 4.5 (copilot)` |
| `tools` | No | Tools available during this prompt | `['editFiles', 'search']` |
| `argument-hint` | No | Hint text shown in chat input | `'What to explain?'` |

### Agent Modes

| Agent Value | Can Edit? | Can Run Terminal? | Best For |
|---|---|---|---|
| `ask` | ❌ | ❌ | Reading, explaining, reviewing |
| `agent` | ✅ | ✅ | Creating, editing, building |
| `plan` | ❌ | ❌ | Planning changes without applying |
| `your-agent-name` | Depends | Depends | Using a custom agent's persona |

---

## 🔤 Variables Reference

### User Input Variables

| Variable | What It Does | Example |
|---|---|---|
| `${input:name}` | Ask user for free-text input | `${input:className}` → user types "Calculator" |
| `${input:name:placeholder}` | Same, with hint text | `${input:module:e.g., service}` |

### File Context Variables

| Variable | What It Provides | Example Value |
|---|---|---|
| `${file}` | Full path of current file | `E:\learning\src\Main.java` |
| `${fileBasename}` | Just the filename | `Main.java` |
| `${fileDirname}` | Directory of current file | `E:\learning\src\` |
| `${fileBasenameNoExtension}` | Filename without extension | `Main` |
| `${selection}` / `${selectedText}` | Currently selected text | *(whatever you highlighted)* |
| `${workspaceFolder}` | Project root path | `E:\learning\learning-assistant` |

### Referencing Other Files

```markdown
Follow the conventions in [Java instructions](../instructions/java.instructions.md).
Base it on this example: [Main.java](../../src/Main.java)
```

### Referencing Tools

```markdown
Use #tool:search to find existing implementations.
Use #tool:codebase to understand the class hierarchy.
```

---

## ✍️ Examples

<details>
<summary><strong>Example 1: Explain Current File</strong></summary>

```markdown
---
name: explain
description: 'Explain the current file in plain language'
agent: ask
tools: ['codebase']
---

Explain this file in plain, beginner-friendly language.

## File
${file}

## Instructions
1. **Purpose:** What does this file do? (1-2 sentences)
2. **Structure:** Walk through the code section by section
3. **Key concepts:** Explain any Java concepts used
4. **How to run:** How would someone execute this code?
5. **What could be improved:** Suggest 2-3 improvements with code examples
```

> **Usage:** Open any `.java` file → type `/explain` → get a structured walkthrough

</details>

<details>
<summary><strong>Example 2: Create a Java Class</strong></summary>

```markdown
---
name: create-class
description: 'Scaffold a new Java class following project conventions'
agent: agent
tools: ['editFiles', 'search', 'codebase']
---

Create a new Java class in this project.

## Conventions
Follow the rules from [Java instructions](../instructions/java.instructions.md).

## Input
Class name: ${input:className:e.g., Calculator, StudentService}
Purpose: ${input:purpose:What should this class do?}

## Requirements
1. Create the class file in the `src/` directory
2. Add Javadoc class comment and constructor
3. Include at least one public method based on the purpose
4. Add a `main` method that demonstrates usage
```

> **Usage:** Type `/create-class` → enter name, purpose → Copilot creates the file

</details>

<details>
<summary><strong>Example 3: Code Review Checklist</strong></summary>

```markdown
---
name: review
description: 'Review the current file against a coding checklist'
agent: ask
tools: ['search', 'codebase']
---

Review this file for code quality issues.

## File to Review
${file}

## Checklist
1. **Naming:** Do names clearly describe their purpose?
2. **Length:** Methods under 30 lines? Class under 200 lines?
3. **SRP:** Does each method do exactly one thing?
4. **Error handling:** Exceptions caught specifically?
5. **Resources:** Closed properly (try-with-resources)?
6. **Magic numbers:** Literal values extracted to constants?
7. **Comments:** Explaining WHY, not WHAT?
8. **Edge cases:** Null checks and boundaries handled?

## Output
For each issue: Rule # · Line · Issue · Fix (with code)
```

</details>

<details>
<summary><strong>Example 4: Refactor Selection</strong></summary>

```markdown
---
name: refactor
description: 'Refactor the selected code with explanations'
agent: agent
tools: ['editFiles', 'codebase']
---

Refactor the selected code to improve quality.

## Code to Refactor
${selection}

## File Context
This code is from: ${file}

## Refactoring Goals
1. **Extract Method:** Break long methods into smaller ones
2. **Rename:** Improve variable/method names
3. **Simplify:** Reduce complexity
4. **DRY:** Remove duplication
5. **Modernize:** Use modern Java features (streams, var, records)

## Rules
- Explain each change and WHY
- Don't change behavior — only improve structure
```

</details>

<details>
<summary><strong>Example 5: Generate Tests</strong></summary>

```markdown
---
name: test
description: 'Generate JUnit 5 tests for the current file'
agent: agent
tools: ['editFiles', 'search', 'codebase']
---

Generate unit tests for the current file.

## Source File
${file}

## Test Conventions
- Use JUnit 5 (`@Test`, `@DisplayName`)
- Method name: `should_<expected>_when_<condition>`
- Follow Arrange-Act-Assert pattern
- Test happy path, edge cases, and error cases

## Output
Create a test file with tests for ALL public methods, including:
- Happy path test
- Edge case test (null, empty, boundary values)
- Error case test (if method can fail)
```

</details>

---

## 📂 How to Create a Prompt File

### Option A — VS Code Command (Recommended)

1. Press `Ctrl+Shift+P`
2. Type: **Chat: New Prompt File**
3. Choose **Workspace**
4. Enter filename → edit the generated template

### Option B — Manual

1. Create file: `.github/prompts/<task-name>.prompt.md`
2. Add YAML frontmatter with name, description, agent, tools
3. Write task instructions in the body
4. Use `${input:name:hint}` for dynamic inputs
5. Save — type `/task-name` in chat to use it

### Testing a Prompt

- **Quick test:** Open the `.prompt.md` file in the editor, click the **▶️ play button** in the title bar
- **Normal test:** In Chat, type `/` and select your prompt from the list

---

## 💡 Tips

- Use `agent: ask` for read-only prompts (explain, review) and `agent: agent` for editing prompts (create, refactor)
- **Iterate:** Prompts rarely work perfectly first time — run → review → tweak → repeat
- **Reference instructions:** Link to your instruction files instead of duplicating rules
- **Keep prompts focused:** One task per prompt — "Create class" and "Generate tests" should be separate
- Use `argument-hint` to tell users what to type when context is needed
- Test with different files to make sure the prompt works broadly

---

## 💡 Prompt Ideas

| Prompt | Description |
|---|---|
| `/explain` | Explain current file in plain language |
| `/create-class` | Scaffold a new Java class |
| `/review` | Code review checklist |
| `/refactor` | Refactor selected code with explanations |
| `/test` | Generate JUnit 5 tests |
| `/document` | Add Javadoc to all public methods |
| `/simplify` | Reduce complexity of selected code |
| `/pattern` | Identify design patterns in current file |
| `/debug` | Analyze a bug and suggest fixes |
| `/convert` | Modernize to Java 21+ features |
| `/composite` | Combine multiple analysis modes in one pass |
| `/context` | Continue from a prior conversation or start clean |
| `/scope` | Set generic-learning vs code-specific scope |
| `/learn-from-docs` | Learn a concept through official documentation |
| `/explore-project` | Study an open-source project's patterns & architecture |
| `/deep-dive` | Progressive multi-layer concept deep-dive |
| `/reading-plan` | Generate a structured study plan with curated resources |

---

## 🔀 Meta-Prompts: Composite, Context & Scope

Three special prompts let you **control how Copilot works**, not just what it works on:

| Prompt | Purpose | When to Use |
|---|---|---|
| `/composite` | Combine 2+ modes (e.g., refactor + design-review + impact) into a single unified analysis | You want a holistic view without running separate prompts |
| `/context` | Continue from a previous conversation or start fresh | You're resuming work from yesterday, or want a clean-slate re-analysis |
| `/scope` | Choose generic learning (concepts, theory) vs code-specific work | You want to learn about sealed classes in general vs. apply them to your codebase |

### How They Compose

These meta-prompts can be **chained mentally** — for example:
- `/scope` → `specific` + `/composite` → `refactor, design-review` = domain-specific combined refactoring + design review
- `/context` → `continue` + `/scope` → `generic` = resume a prior learning conversation
- `/composite` → `refactor, impact, teach` = refactor with impact analysis, explained for learning

---

## 📌 File Audience Note

This folder contains two types of files:

| File | Audience | Purpose |
|---|---|---|
| `*.prompt.md` | 🤖 **Copilot** | Slash command templates — loaded into AI context when invoked |
| `README.md` | 👤 **Developer** | This guide — Copilot does not read it |

When you edit a `.prompt.md` file, you change what Copilot does when you type `/command`. When you edit this README, nothing changes in Copilot — it's documentation for you.

> 📖 **Full breakdown:** [File Reference →](../docs/file-reference.md)

---

## 🧪 Experiments to Try

1. Create `/explain` and run it on `Main.java` — does the output make sense?
2. Create `/create-class` and scaffold a `Calculator` — does it follow your rules?
3. Create two prompts with different `agent` values (`ask` vs `agent`) — notice how one can edit and the other can't
4. Chain a prompt with an agent — set `agent: Learning-Mentor` in a prompt to use your custom persona
5. Compare models — duplicate a prompt with different `model` fields

---

<p align="center">

[← Back to main guide](../README.md) · [Instructions](../instructions/README.md) · [Agents](../agents/README.md) · [Skills](../skills/README.md) · [File Reference](../docs/file-reference.md) · [Getting Started](../docs/getting-started.md) · [Customization Guide](../docs/customization-guide.md)

</p>
