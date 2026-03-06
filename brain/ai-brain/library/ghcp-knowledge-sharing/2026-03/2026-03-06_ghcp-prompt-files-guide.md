---
date: 2026-03-06
kind: ref
project: ghcp-knowledge-sharing
tags: [ghcp, prompt-files, slash-commands, team-adoption]
status: archived
source: imported
---

# GitHub Copilot Prompt Files — Complete Guide

> **For:** Knowledge Sharing Session — Developers & QA on Capital IESD-24  
> **Date:** February 2026

---

## TABLE OF CONTENTS

1. [What Are Prompt Files?](#1-what-are-prompt-files)
2. [How to Use a Prompt File](#2-how-to-use-a-prompt-file)
3. [Prompt Files vs Custom Instructions vs Skills — Key Differences](#3-prompt-files-vs-custom-instructions-vs-skills)
4. [Prompt Files in This Repo (Capital IESD-24)](#4-prompt-files-in-this-repo)
5. [Real Use Cases with Examples](#5-real-use-cases-with-examples)
6. [How to Create Your Own Prompt File](#6-how-to-create-your-own-prompt-file)
7. [Tips for Writing Effective Prompts](#7-tips-for-writing-effective-prompts)

---

## 1. WHAT ARE PROMPT FILES?

### Slide: Definition

**Bullet Points:**
- Prompt files are **reusable Markdown templates** stored in `.github/prompts/`
- Each file represents a **slash command** you type in the Copilot chat (e.g., `/history`, `/cof-model`)
- They pre-load a context-rich system instruction into the conversation **on demand**
- Think of them as **bookmarks to expert knowledge** — one slash command, full context injected
- They are **committed to the repo** — the whole team benefits automatically

### Slide: The Problem They Solve

Without prompt files, every developer writes the same context over and over:

```
❌ "I want to add a property to Device.java. 
    Make sure to call premodify() before the setter, 
    fire a PropertyChangeEvent, use @NotNull, 
    and follow the pattern in the existing setters..."
```

With a prompt file:

```
✅ /cof-model  Add property "description" to Device.java
```

The prompt file carries **all the rules** — you only describe **the task**.

---

## 2. HOW TO USE A PROMPT FILE

### Slide: Three Ways to Invoke

#### Method 1 — Slash Command in Chat (Most Common)
Type `/` in the Copilot Chat input box — a picker appears with all available prompts:

```
/history
/cof-model   Add a new property to IDevice
/java-dev    Fix null pointer exception in DeviceManager
/caplet-action   Create a refresh connectivity action
/build-workflow  I changed files in charness_src, what to build?
```

#### Method 2 — Command Palette
1. Press `Ctrl+Shift+P`
2. Type **"Chat: Run Prompt"**
3. Select the prompt file
4. Enter your task description

#### Method 3 — With File Context
Open the file you want to work on, then use the prompt:

```
[Device.java is open in editor]
/cof-model   Add a setDescription() setter following existing patterns
```

Copilot now has the file content + the prompt rules simultaneously.

---

## 3. PROMPT FILES vs CUSTOM INSTRUCTIONS vs SKILLS

### Slide: Comparison Table

| Feature | Custom Instructions | Skills | Prompt Files |
|---------|-------------------|--------|--------------|
| **When loaded** | Always / on file open | Auto-detected by keyword | On demand (you type `/`) |
| **Trigger** | `applyTo` glob pattern | Keywords in user message | `/slash-command` |
| **Scope** | Background rules | Procedural workflows | Focused task context |
| **User action needed** | None — automatic | None — automatic | Yes — type the slash command |
| **Best for** | Coding standards, patterns | Build steps, git commands, review templates | Repeatable tasks, checklists, personal shortcuts |
| **Location** | `.github/instructions/` | `.github/skills/` | `.github/prompts/` |
| **Example** | "Always use `premodify()`" | "Use `iesd25` not `origin`" | `/cof-model  Add property to Device` |

### Slide: They Work Together

```
You type:  /cof-model  Add description property to Device.java

┌──────────────────────────────────────────────────────┐
│  Custom Instructions (background, auto-loaded)        │
│  → datamodel.instructions.md (file is datamodel_src) │
│  → global copilot-instructions.md                    │
├──────────────────────────────────────────────────────┤
│  Prompt File (you triggered it)                       │
│  → /cof-model gives step-by-step COF patterns        │
│  → PropertyChange, premodify(), VaultKey examples    │
├──────────────────────────────────────────────────────┤
│  Skills (auto-detected if keywords match)            │
│  → No skill triggered for this task                  │
└──────────────────────────────────────────────────────┘
                        ↓
           Perfect Capital-aware output
```

---

## 4. PROMPT FILES IN THIS REPO

### Slide: Available Prompts

| Command | File | Purpose |
|---------|------|---------|
| `/history` | `history.prompt.md` | Show last 10 prompts from your chat history |
| `/help-me-choose` | `help-me-choose.prompt.md` | Not sure which prompt? Ask this one first |
| `/java-dev` | `java-development.prompt.md` | General Java coding — nullability, logging, EDT |
| `/cof-model` | `cof-model-development.prompt.md` | COF entity development — interfaces + implementations |
| `/caplet-action` | `caplet-action-development.prompt.md` | CAF actions, caplets, UI components |
| `/persistence-grpc` | `persistence-grpc-development.prompt.md` | PersistenceSession + gRPC service patterns |
| `/build-workflow` | `build-git-workflow.prompt.md` | Gradle builds, git workflow with `iesd25` remote |
| `/nx-integration` | `nx-immersed-integration.prompt.md` | Capital-NX immersed mode integration |
| `/analyze-perf` | `analyze-performance.prompt.md` | Performance analysis and optimization |

### Slide: Directory Structure

```
.github/prompts/
├── history.prompt.md                    ← /history  — chat history viewer
├── help-me-choose.prompt.md             ← /help-me-choose
├── java-development.prompt.md           ← /java-dev
├── cof-model-development.prompt.md      ← /cof-model
├── caplet-action-development.prompt.md  ← /caplet-action
├── persistence-grpc-development.prompt.md ← /persistence-grpc
├── build-git-workflow.prompt.md         ← /build-workflow
├── nx-immersed-integration.prompt.md    ← /nx-integration
├── analyze-performance.prompt.md        ← /analyze-perf
├── README.md                            ← Full docs
└── SUMMARY.md                           ← Quick reference
```

---

## 5. REAL USE CASES WITH EXAMPLES

### USE CASE 1: Enforcing Team Coding Standards Consistently

**Problem:** Every dev writes their own prompt context. Output quality is inconsistent.

**Solution:** `/cof-model` carries all COF rules — everyone gets the same expert output.

```
/cof-model  Add a new property "wireGauge" to the IWireConductor interface
             and implement it in WireConductor.java
```

**What the prompt file injects automatically:**
- `premodify()` before every setter
- `PropertyChangeEvent` firing
- `@NotNull`/`@Nullable` annotations
- `VaultKey<T>` for lazy properties
- Correct base class hierarchy

---

### USE CASE 2: Onboarding New Developers Fast

**Problem:** New joiner doesn't know Capital patterns, asks senior dev 10 times a day.

**Solution:** First day, show them these slash commands. They self-serve from day one.

```
New dev types: /help-me-choose  I need to add a button that opens a dialog
```

Response guides them to `/caplet-action`, which then generates correct `AppAction` code with:
- `@ApplicationSpecification` annotation
- `IFIB` constructor
- `WaitCursor` pattern
- `isEnabled()` guard

---

### USE CASE 3: Build Workflow — Never Forget Steps

**Problem:** Developer changes `interfaces_src` and only builds `capitalcaf` — breaks other modules.

**Solution:**
```
/build-workflow  I changed IDevice.java and Device.java. What should I build?
```

**Agent Response (with prompt's knowledge):**
> ⚠️ You changed `interfaces_src` — this is the contract layer.  
> Interface changes ripple through **all** downstream modules.
> ```bash
> source setup.env
> gradle clean build -x test --parallel --max-workers=8
> ```
> Run full tests before raising a PR:
> ```bash
> gradle test
> ```

---

### USE CASE 4: Code Review Checklist (Create This!)

**Proposed prompt:** `/review`

```
/review  Check my changes in Device.java before I raise a PR
```

**Prompt file would check:**
- [ ] All setters call `premodify()` 
- [ ] All setters fire `PropertyChangeEvent`
- [ ] `@NotNull`/`@Nullable` on all parameters
- [ ] No direct UI code in `datamodel_src`
- [ ] No business logic in `cmanager_src`
- [ ] Correct base class used
- [ ] Tests written / updated

---

### USE CASE 5: Unit Test Generation

**Problem:** QA/dev writes a test, but uses JUnit 5 syntax — project uses JUnit 4.

**Solution:**
```
/java-dev  Generate unit tests for ImportMessageUtils following existing test patterns
```

**Prompt file injects:**
- JUnit 4 (`@Test`, `@Before`, `@RunWith`)
- `MockitoJUnitRunner.Silent.class`
- `@FixMethodOrder(MethodSorters.NAME_ASCENDING)`
- `test_` prefix naming convention
- `// Setup / // Execute / // Assert` AAA comment blocks
- `org.junit.Assert.*` (no Hamcrest)
- Correct base test class per module (`HarnessTestCase`, `DataModelTestCase`)

**Output:**
```java
@RunWith(MockitoJUnitRunner.Silent.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ImportMessageUtilsTest {

    @Test
    public void test_constructMessage_ValidProject_ReturnsMsgFromBundle() {
        // Setup
        ImportMessageUtils utils = new ImportMessageUtils();
        String projName = "TestProject";

        // Execute
        IMessageContent result = utils.constructMessageContentForDupPrjName(projName);

        // Assert
        Assert.assertEquals("Import Project", result.context());
        Assert.assertEquals("Duplicate project name", result.message());
    }
}
```

---

### USE CASE 6: gRPC Service Generation

**Problem:** Developer creates a gRPC service but misses the `IGrpcService` contract.

```
/persistence-grpc  Create a new gRPC service for validating design integrity
```

**Prompt ensures:**
- Extends correct `GrpcXxxServiceGrpc.GrpcXxxServiceImplBase`
- Implements `IGrpcService`
- Constructor takes `IManagerLogin`
- Override `getServiceDefinition()` returning `bindService()`
- Proper `StreamObserver` pattern

---

### USE CASE 7: JIRA Bug Creation (Create This!)

**Proposed prompt:** `/create-bug`

```
/create-bug  NPE in CrossSelectionClient.sendRequest() when no design is loaded.
             Steps: Open Capital, don't open any design, try cross-selection.
```

**Prompt would auto:**
- Use `jira-confluence-mcp` tool
- Run Chrome debug preflight
- Format with correct project key, priority, description template
- Link to affected files in `interfaces_src/.../immersed/`

---

### USE CASE 8: Chat History Viewer — `/history`

**Problem:** "What did I ask 20 messages ago?" — Scrolling back is tedious.

```
/history
```

**Agent reads `.github/chat_md/my_userprompt.md` and shows:**

| # | Date & Time | Prompt Summary |
|---|-------------|----------------|
| 1 | 2026-02-28 10:32 | lets change the instructions to save all my user prompt... |
| 2 | 2026-02-28 10:31 | do it mean you saved this prompt also... |
| 3 | 2026-02-28 10:30 | why you store and created file my_userprompt.md |
| 4 | 2026-02-28 10:00 | i want to give the knowledge sharing session... |

```
/history 5    ← Show last 5 instead of 10
```

---

### USE CASE 9: NX Integration Work

**Problem:** Immersed mode development has very specific patterns — EDT safety, window tracking, REST payloads.

```
/nx-integration  Add cross-selection support when a new NX window is acquired
```

**Prompt ensures:**
- `SwingUtilities.invokeLater()` for all UI changes
- Window tracking in `Map<Component, Long>`
- `ImmersedModeServices.requireExtension()` vs `queryExtension()`
- JSON payload via `AbstractExternalServiceClient`
- `IGuard` pattern for temporary disable

---

### USE CASE 10: Performance Investigation

```
/analyze-perf  getDeviceConnectors() takes 3 seconds with 1000+ connectors
```

**Prompt guides:**
- Profiling approach for the specific code path
- Lazy loading via UID pattern review
- Cache analysis using `ICacheMgr`
- Iterator vs Stream performance tradeoffs

---

## 6. HOW TO CREATE YOUR OWN PROMPT FILE

### Slide: Minimal Template

Create a file in `.github/prompts/` named `my-task.prompt.md`:

```markdown
---
description: One-line description shown in the prompt picker
name: my-task
argument-hint: What to include after the slash command (e.g., "describe the task")
---

# My Task Title

You are helping with [specific domain] in the Capital IESD-24 project.

## Context
- Key rule 1
- Key rule 2
- Key rule 3

## Patterns to Follow
[Code examples here]

## Steps
1. Step 1
2. Step 2
3. Step 3
```

### Slide: Naming Convention

| Element | Convention | Example |
|---------|-----------|---------|
| File name | `kebab-case.prompt.md` | `my-feature.prompt.md` |
| `name` in frontmatter | `kebab-case` (no spaces) | `my-feature` |
| Slash command | `/name` | `/my-feature` |
| `description` | Short, clear sentence | "Generate tests for Capital modules" |
| `argument-hint` | What user types after command | "module name or task description" |

### Slide: Pro Tips for Writing Prompt Files

1. **Be specific** — Include exact class/method names, not generic patterns
2. **Add code examples** — Show the exact output you want, not just rules
3. **Include Do's and Don'ts** — Explicit anti-patterns prevent wrong output
4. **Reference real files** — "Follow the pattern in `AiUtilsTest.java`"
5. **Keep focused** — One prompt per domain (don't mix UI + persistence)
6. **Test iteratively** — Use the prompt, see the output, refine the file

---

## 7. TIPS FOR WRITING EFFECTIVE PROMPTS (When Using Them)

### Slide: Good vs Bad Prompts

| ❌ Vague Prompt | ✅ Specific Prompt |
|----------------|-------------------|
| `/java-dev  Fix this method` | `/java-dev  Fix the NullPointerException in Device.setName() when name is empty` |
| `/cof-model  Add something` | `/cof-model  Add property "description" to IDevice interface and Device implementation` |
| `/build-workflow  Build` | `/build-workflow  I changed IDevice.java and BundleImpl.java — what to build and in what order?` |
| `/caplet-action  Create action` | `/caplet-action  Create a ViewActionRT action for interactively placing a splice connector` |

### Slide: Always Provide

1. **What** — what you want to create/change/fix
2. **Where** — class name, file, module
3. **Why** — optional but helps with logic decisions
4. **Reference** — "like the existing `AddPinAction`"

### Slide: Power Combos

```bash
# Open the file first, then use the prompt
[Device.java open] → /cof-model  Add wireGauge property

# Combine with chat context
/cof-model  Add this property [paste interface snippet]

# Chain prompts
/cof-model  Add property
# Then:
/java-dev  Add null safety and logging to the setter I just created
```

---

## QUICK REFERENCE CARD (Session Handout)

```
┌─────────────────────────────────────────────────────────────┐
│         GHCP PROMPT FILES — Capital IESD-24                  │
├─────────────────────────────────────────────────────────────┤
│ HOW TO USE:  Type / in chat → pick prompt → add your task   │
│                                                              │
│ AVAILABLE PROMPTS:                                           │
│   /history         → Last 10 prompts from your history       │
│   /help-me-choose  → Not sure which prompt? Start here       │
│   /java-dev        → Java coding standards (@NotNull, EDT)   │
│   /cof-model       → COF entity: premodify(), PropertyChange │
│   /caplet-action   → AppAction, ViewActionRT, WaitCursor     │
│   /persistence-grpc→ PersistenceSession + gRPC services      │
│   /build-workflow  → Gradle commands + module mapping         │
│   /nx-integration  → Immersed mode, window tracking          │
│   /analyze-perf    → Performance profiling guidance           │
│                                                              │
│ THREE-PART FORMULA FOR GREAT RESULTS:                        │
│   1. Open the file you're working on                         │
│   2. Pick the right /prompt for the domain                   │
│   3. Describe specifically WHAT + WHERE + reference example  │
│                                                              │
│ EXAMPLE:                                                     │
│   /cof-model  Add "wireGauge" property to IWireConductor     │
│               interface in interfaces_src and implement it   │
│               in WireConductor.java following Device.java    │
│                                                              │
│ CREATE YOUR OWN:                                             │
│   → Add .prompt.md to .github/prompts/                       │
│   → Use frontmatter: name, description, argument-hint        │
│   → Commit to repo — whole team gets it instantly            │
└─────────────────────────────────────────────────────────────┘
```

---

*Document prepared by GHCP Agent | February 2026*
