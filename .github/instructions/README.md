# 📐 Custom Instructions — Guide

> **What:** Markdown files with coding standards that Copilot auto-loads based on the file you're editing.  
> **Where:** `.github/instructions/*.instructions.md`  
> **How to use:** Automatic — Copilot matches the `applyTo` glob pattern to the file you have open.

---

## 📑 Table of Contents

- [What Are Custom Instructions?](#-what-are-custom-instructions)
- [Instructions in This Project](#-instructions-in-this-project)
- [File Format](#-file-format)
- [Glob Pattern Reference](#-glob-pattern-reference)
- [Examples](#-examples)
- [How to Create an Instruction File](#-how-to-create-an-instruction-file)
- [Verification & Troubleshooting](#-verification--troubleshooting)
- [Tips & Best Practices](#-tips--best-practices)
- [Experiments to Try](#-experiments-to-try)

---

## 📌 What Are Custom Instructions?

Custom instructions are **path-scoped coding standards**. They tell Copilot *how* to write code for specific file types — naming conventions, patterns, error handling, style rules, etc.

**Key characteristics:**
- 🎯 **Auto-activated** — no manual setup, no slash commands
- 🗂️ **Path-scoped** — only apply to files matching a glob pattern
- 📝 **Additive** — stack on top of `copilot-instructions.md` (project-wide rules)
- 🔄 **Always active** — apply in Chat, Inline Chat, Edits, and Agent modes

**How they differ from `copilot-instructions.md`:**

| Aspect | `copilot-instructions.md` | `*.instructions.md` |
|---|---|---|
| Scope | Entire project | Files matching a glob pattern |
| Location | `.github/copilot-instructions.md` | `.github/instructions/` |
| When active | Always | When editing a matching file |
| Purpose | Project-wide rules | File-type-specific rules |
| Stacking | Base layer | Adds on top of base layer |

---

## 🗂️ Instructions in This Project

| File | Applies To | What It Enforces |
|---|---|---|
| [`java.instructions.md`](java.instructions.md) | `**/*.java` | Java naming, structure, Javadoc, error handling |
| [`clean-code.instructions.md`](clean-code.instructions.md) | `**/*.java` | Code smells, SOLID hints, refactoring cues |

> Both files apply to `**/*.java` — they **stack together** when you edit any `.java` file.

---

## 📄 File Format

```markdown
---
applyTo: "glob/pattern/**/*.ext"
---

Your instructions go here in plain Markdown.
Be specific, concise, and actionable.
```

### Frontmatter Fields

| Field | Required? | Description | Example |
|---|---|---|---|
| `applyTo` | **Yes** | Glob pattern matching files this applies to | `**/*.java`, `src/test/**` |

### Rules for the Body

- ✅ Be **specific** — "Use `Objects.requireNonNull()` for null checks"
- ✅ Be **actionable** — instructions Copilot can follow immediately
- ✅ Be **concise** — shorter instructions = better compliance
- ❌ Don't be vague — "Write clean code" gives Copilot nothing to work with
- ❌ Don't write essays — long blocks get diluted in context

---

## 🔤 Glob Pattern Reference

| Pattern | Matches | Example Match |
|---|---|---|
| `**/*.java` | All `.java` files, any depth | `src/Main.java`, `src/com/pkg/Foo.java` |
| `src/**` | Everything under `src/` | `src/Main.java`, `src/pkg/Util.java` |
| `src/test/**` | Only test files | `src/test/FooTest.java` |
| `**/*.{xml,json}` | `.xml` and `.json` files | `pom.xml`, `config.json` |
| `**/README.md` | All README files | `README.md`, `docs/README.md` |
| `*.java` | Only root-level `.java` files | `Main.java` (NOT `src/Main.java`) |

### Common Gotchas

| Mistake | Problem | Fix |
|---|---|---|
| `*.java` | Only matches root directory | `**/*.java` |
| `src/` | Matches nothing (trailing slash) | `src/**` |
| Missing quotes | YAML parsing error | Always quote: `"**/*.java"` |

---

## ✍️ Examples

<details>
<summary><strong>Example 1: Java Source Files</strong></summary>

```markdown
---
applyTo: "**/*.java"
---

# Java Standards

- Use `final` for variables that don't change
- Prefer `var` for local variables when the type is obvious
- Use `Objects.requireNonNull()` for null-checking constructor parameters
- Add `@Override` on all overridden methods
- Use try-with-resources for closeable resources
- Add Javadoc to all public methods
```

</details>

<details>
<summary><strong>Example 2: Test Files Only</strong></summary>

```markdown
---
applyTo: "src/test/**/*.java"
---

# Test Conventions

- Class name: `<ClassName>Test.java`
- Method: `should_<expected>_when_<condition>()`
- Pattern: Arrange → Act → Assert (with blank-line separators)
- Use `@DisplayName` with human-readable descriptions
- One assertion concept per test
- Never catch exceptions in tests — let them propagate
```

</details>

<details>
<summary><strong>Example 3: Configuration Files</strong></summary>

```markdown
---
applyTo: "**/*.{xml,yaml,yml,json}"
---

# Config File Conventions

- Always add comments explaining non-obvious settings
- Group related settings together
- Use consistent indentation (2 spaces for YAML/JSON, 4 for XML)
- Include a brief header comment describing the file's purpose
```

</details>

<details>
<summary><strong>Example 4: Markdown Documentation</strong></summary>

```markdown
---
applyTo: "**/*.md"
---

# Documentation Standards

- Use ATX-style headings (# not underlines)
- Include a table of contents for files longer than 3 sections
- Use fenced code blocks with language identifiers
- Keep lines under 120 characters when possible
- Use relative links for internal references
```

</details>

---

## 📂 How to Create an Instruction File

### Step-by-Step

1. Create the folder (if missing): `.github/instructions/`
2. Create a file: `.github/instructions/<name>.instructions.md`
3. Add the `applyTo` frontmatter with a glob pattern
4. Write your instructions in the body (Markdown)
5. Save — instructions apply immediately

### Naming Convention

```
<scope>.instructions.md
```

Examples:
- `java.instructions.md` — Java coding standards
- `testing.instructions.md` — Testing conventions
- `api.instructions.md` — REST API design rules
- `clean-code.instructions.md` — Clean code practices

---

## ✅ Verification & Troubleshooting

### How to Verify Instructions Are Loading

1. Open a file that matches your glob pattern (e.g., `Main.java`)
2. Open GitHub Copilot Chat
3. Ask: *"What instructions are you following for this file?"*
4. Copilot should mention your specific rules

### Quick Tests

| Test | How To | Expected |
|---|---|---|
| Naming rules | Ask "Write a method to add two numbers" | Method named `addNumbers`, not `add` or `a` |
| Javadoc | Ask "Add javadoc to this class" | Full `@param`, `@return` tags |
| Error handling | Ask "Add error handling" | Specific exceptions, not `catch (Exception e)` |

### Troubleshooting

| Symptom | Likely Cause | Fix |
|---|---|---|
| Rules not applied | Glob pattern doesn't match | Test with `**/*.java` (broadest match) |
| Only some rules work | Instructions too long | Shorten — prioritize top rules |
| Conflicting behavior | Two instructions contradict | Review all `applyTo` overlaps |

---

## 💡 Tips & Best Practices

1. **Start broad, refine later** — begin with `**/*.java`, split into `src/main/**` and `src/test/**` when needed
2. **Keep it short** — 10-20 bullet points per file is ideal
3. **Prioritize top rules** — if context is tight, Copilot weighs earlier lines more
4. **Use concrete examples** — "Use `Objects.requireNonNull(param, "param must not be null")`" beats "Validate parameters"
5. **Don't duplicate `copilot-instructions.md`** — path-specific files add to it, not replace it
6. **Test after creating** — verify with the tests above

---

## 📌 File Audience Note

This folder contains two types of files:

| File | Audience | Purpose |
|---|---|---|
| `*.instructions.md` | 🤖 **Copilot** | Coding standards — auto-loaded when editing files matching the `applyTo` glob |
| `README.md` | 👤 **Developer** | This guide — Copilot does not read it |

When you edit an `.instructions.md` file, you change the coding rules Copilot follows. When you edit this README, nothing changes in Copilot — it's documentation for you.

> 📖 **Full breakdown:** [File Reference →](../docs/file-reference.md)

---

## 🧪 Experiments to Try

1. **Create a test instruction** → `testing.instructions.md` with `applyTo: "**/*Test.java"` → ask Copilot to write a test → does it follow your conventions?
2. **Check stacking** → open `Main.java` → verify both `java.instructions.md` AND `clean-code.instructions.md` apply
3. **Try a narrow glob** → create `controllers.instructions.md` with `applyTo: "src/controller/**"` → verify it only activates for controller files
4. **Break it on purpose** → use `*.java` (no `**`) → open `src/Main.java` → notice instructions don't load → fix to `**/*.java`

---

<p align="center">

[← Back to main guide](../README.md) · [Agents](../agents/README.md) · [Prompts](../prompts/README.md) · [Skills](../skills/README.md) · [File Reference](../docs/file-reference.md) · [Getting Started](../docs/getting-started.md) · [Customization Guide](../docs/customization-guide.md)

</p>
