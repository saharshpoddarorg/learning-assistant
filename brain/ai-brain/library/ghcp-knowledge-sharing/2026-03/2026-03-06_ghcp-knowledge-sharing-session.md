---
date: 2026-03-06
kind: ref
project: ghcp-knowledge-sharing
tags: [ghcp, copilot-customization, custom-instructions, skills, capital-iesd24]
status: archived
source: imported
---

# GitHub Copilot (GHCP) Custom Instructions & Agent Mode — Knowledge Sharing Session

> **Target Audience:** Developers & QA Engineers working on Capital IESD-24
> **Duration:** ~60-90 minutes
> **Presenter:** Dhrumil
> **Date:** February 2026

---

## TABLE OF CONTENTS

1. [What is GHCP Custom Instructions & Why It Matters](#1-what-is-ghcp-custom-instructions--why-it-matters)
2. [Architecture: How Custom Instructions Feed the LLM](#2-architecture-how-custom-instructions-feed-the-llm)
3. [When to Use Which GHCP Mode — Ask / Agent / Edit](#3-when-to-use-which-ghcp-mode)
4. [Choosing the Right Model](#4-choosing-the-right-model)
5. [Custom Instructions Deep Dive (with Capital Examples)](#5-custom-instructions-deep-dive)
6. [Skills — Domain-Specific Power-Ups](#6-skills--domain-specific-power-ups)
7. [Agents — Specialized Autonomous Workers](#7-agents--specialized-autonomous-workers)
8. [Live Demo Examples (Capital-Specific)](#8-live-demo-examples)
9. [Additional Use Cases to Showcase](#9-additional-use-cases-to-showcase)
10. [Tips, Gotchas & Best Practices](#10-tips-gotchas--best-practices)
11. [Q&A Preparation](#11-qa-preparation)

---

## 1. WHAT IS GHCP CUSTOM INSTRUCTIONS & WHY IT MATTERS

### Slide: The Problem (Before Custom Instructions)

**Bullet Points:**
- Generic LLM outputs — doesn't know Capital's architecture, patterns, or conventions
- Every prompt requires re-explaining context: "We use JUnit 4, not JUnit 5", "Extend `AppAction`, not `AbstractAction`"
- Generated code doesn't follow team standards: wrong base classes, missing annotations, incorrect patterns
- Developer spends more time fixing Copilot's output than writing code manually
- No awareness of module boundaries (`cframework_src`, `datamodel_src`, `interfaces_src`)

### Slide: The Solution (With Custom Instructions)

**Bullet Points:**
- **Custom Instructions** = persistent rules that pre-load into every Copilot conversation
- LLM already knows: "Capital uses `premodify()` before every setter", "Actions extend `AppAction`", "Use `@NotNull`/`@Nullable` annotations"
- Generated code matches existing codebase patterns on first attempt
- **Skills** = specialized workflows (build, git, design review) loaded on demand
- **Agents** = autonomous sub-agents (Capital-NX, CIA-Orchestrator) for complex tasks
- Result: 60-80% reduction in prompt re-engineering; higher first-attempt accuracy

### Slide: The .github Folder — Our Knowledge Base

```text
.github/
├── copilot-instructions.md          ← Global rules for ALL files
├── instructions/                     ← Module-specific coding rules
│   ├── cframework.instructions.md   ← CAF UI framework patterns
│   ├── charness.instructions.md     ← Harness Designer patterns
│   ├── clogic.instructions.md       ← Capital Capture patterns
│   ├── cmanager.instructions.md     ← Persistence layer patterns
│   ├── datamodel.instructions.md    ← COF implementation patterns
│   ├── interfaces.instructions.md   ← Interface contract rules
│   ├── grpc.instructions.md         ← gRPC service layer patterns
│   └── capital-nx-integration.instructions.md ← NX immersed mode
├── skills/                           ← On-demand capabilities
│   ├── build-workflow/              ← Build decision tree
│   ├── git-command/                 ← Git reference (iesd25 remote!)
│   ├── confluence-design-review/    ← Design review page generation
│   ├── jira-confluence-mcp-preflight/ ← JIRA/Confluence tool access
│   ├── skill-creator/               ← How to create new skills
│   └── hello-world/                 ← Greeting handler
├── agents/                           ← Custom agents
└── chat_md/                          ← Saved conversation outputs
```

---

## 2. ARCHITECTURE: HOW CUSTOM INSTRUCTIONS FEED THE LLM

### Slide: How Instructions Flow into the LLM

```text
┌──────────────────────────────────────────────────────┐
│                    YOUR PROMPT                        │
│  "Add a new Action to refresh harness connectivity"   │
└──────────────────┬───────────────────────────────────┘
                   │
                   ▼
┌──────────────────────────────────────────────────────┐
│              GHCP PRE-PROCESSING                      │
│                                                       │
│  1. Load copilot-instructions.md        (ALWAYS)     │
│  2. Match file path → load instructions  (AUTO)      │
│     charness_src/** → charness.instructions.md       │
│  3. Match user intent → load skill       (AUTO)      │
│     "build" → build-workflow/SKILL.md                │
│  4. Combine into SYSTEM PROMPT                       │
└──────────────────┬───────────────────────────────────┘
                   │
                   ▼
┌──────────────────────────────────────────────────────┐
│                  LLM (Claude/GPT-4o)                 │
│                                                       │
│  System prompt now includes:                          │
│  ✓ "Extend AppAction for command actions"            │
│  ✓ "Use @ApplicationSpecification annotation"        │
│  ✓ "Fire PropertyChangeEvent on state changes"       │
│  ✓ "Use premodify() before setters"                  │
│  ✓ "JUnit 4 with Mockito, not JUnit 5"              │
│  ✓ "Remote is iesd25, not origin"                    │
└──────────────────┬───────────────────────────────────┘
                   │
                   ▼
┌──────────────────────────────────────────────────────┐
│              CAPITAL-AWARE OUTPUT                     │
│  Code that follows YOUR team's conventions!          │
└──────────────────────────────────────────────────────┘
```

### Slide: The `applyTo` Mechanism — Automatic Scope

**Bullet Points:**
- Each instruction file has an `applyTo` glob pattern in its YAML frontmatter
- When you open/edit a file matching the pattern, those rules auto-activate
- Example: editing `charness_src/src/capitalharness/src/chs/Caplet.java` → loads `charness.instructions.md`
- No manual selection needed — the right rules apply to the right files automatically

**Example from our codebase:**

```yaml
---
applyTo: "cframework_src/**/*.java"
---
# Module Instructions: cframework_src
# These rules activate ONLY when editing CAF framework files
```

```yaml
---
applyTo: "datamodel_src/**/*.java"
---
# Module Instructions: datamodel_src
# These rules activate ONLY when editing COF implementation files
```

---

## 3. WHEN TO USE WHICH GHCP MODE

### Slide: Three Modes — Pick the Right One

| Mode | Icon | Best For | Context | Example |
|------|------|----------|---------|---------|
| **Ask** | 💬 | Quick questions, explanations, small snippets | Current file + selection | "What does `premodify()` do in Device.java?" |
| **Edit** | ✏️ | Inline code edits, refactoring within a file | Current file | "Add `@NotNull` to all parameters in this method" |
| **Agent** | 🤖 | Multi-file tasks, test generation, research, complex changes | Full workspace + tools | "Generate unit tests for `AiUtils` class following existing patterns" |

### Slide: When to Use ASK Mode

**Use ASK when you need:**
- Quick explanations: "What is the `VaultKey` pattern in `Device.java`?"
- Code review: "Is this Action implementation correct per CAF conventions?"
- Small snippets: "Show me how to implement `isEnabled()` for this action"
- Architecture questions: "What's the relationship between `IDevice` and `IPrivilegedDevice`?"
- Understanding code: "Explain the Observer pattern in COF entities"

**Capital Examples:**

```yaml
ASK: "What is the difference between IDevice and IPrivilegedDevice?"
ASK: "How does the premodify() pattern work in datamodel_src?"
ASK: "What annotations are needed for a new persistent COF object?"
ASK: "Which module should I modify to add a new DRC validator?"
```

### Slide: When to Use EDIT Mode

**Use EDIT when you need:**
- Inline refactoring within the current file
- Adding annotations, imports, or boilerplate
- Renaming variables, methods
- Adding null checks, defensive code
- Small targeted changes in the open file

**Capital Examples:**

```yaml
EDIT: "Add @NotNull annotations to all method parameters"
EDIT: "Replace this raw iterator with a Stream pipeline"
EDIT: "Add PropertyChangeEvent firing to this setter"
EDIT: "Add @ApplicationSpecification for CapitalHarness"
```

### Slide: When to Use AGENT Mode

**Use AGENT when you need:**
- Multi-file operations (generate tests + update code)
- Research across the codebase ("find all usages of IDesignMgr.getDesignDescriptors")
- Generate entire test classes following existing patterns
- Create new Action classes with full lifecycle
- Build workflow decisions
- JIRA/Confluence operations (via MCP tools)
- Complex refactoring spanning multiple modules

**Capital Examples:**

```yaml
AGENT: "Generate a complete unit test class for ImportMessageUtils following the pattern in AiUtilsTest"
AGENT: "Create a new Action class for RefreshHarnessConnectivity in charness_src, following AddPinAction pattern"
AGENT: "Find all files that implement IDRCDomainManager and summarize their validation logic"
AGENT: "Build only the capitalcaf module — what's the command?"
AGENT: "Create a Confluence design review page for my PR changes"
```

---

## 4. CHOOSING THE RIGHT MODEL

### Slide: Model Selection Guide

| Model | Speed | Intelligence | Best For | Token Limit |
|-------|-------|-------------|----------|-------------|
| **GPT-4o** | ⚡ Fast | ★★★★ | Quick edits, ask mode, inline suggestions | 128K |
| **Claude Sonnet 4** | ⚡ Fast | ★★★★ | Balanced speed + quality, good for code generation | 200K |
| **Claude Opus 4** | 🐢 Slower | ★★★★★ | Complex multi-step agent tasks, deep reasoning | 200K |
| **o3-mini** | ⚡ Fast | ★★★★ | Reasoning-heavy tasks, math, logic | 128K |
| **Gemini 2.5 Pro** | ⚡ Fast | ★★★★ | Large context understanding, long files | 1M |

### Slide: Model Recommendation by Task

| Task | Recommended Model | Why |
|------|-------------------|-----|
| Quick inline edit | GPT-4o | Fast, good enough for small edits |
| Generate unit test | Claude Sonnet 4 | Good code quality, follows patterns well |
| Multi-file agent task | Claude Opus 4 | Best reasoning, handles complex instructions |
| Understand large file | Gemini 2.5 Pro | Huge context window |
| Debug complex logic | Claude Opus 4 / o3-mini | Deep reasoning capability |
| Explain architecture | Claude Sonnet 4 | Balance of depth and speed |
| Build/Git commands | GPT-4o | Speed matters, task is straightforward |
| Design review generation | Claude Opus 4 | Complex template + research across files |

### Slide: Rule of Thumb

> **Start with Claude Sonnet 4 for most tasks.
> Upgrade to Opus 4 when Agent mode needs deep reasoning.
> Use GPT-4o for speed-critical Ask/Edit tasks.**

---

## 5. CUSTOM INSTRUCTIONS DEEP DIVE (with Capital Examples)

### Slide: Global Instructions — `copilot-instructions.md`

These rules apply to **every** file in the workspace:

```markdown
# Agent Instructions
- Be concise and accurate. Admit uncertainty; ask for clarification only when needed.
- End each conversation with a brief summary. Save any created files under `.github/chat_md/`.
- Match user query keywords to available MCP tools and use them proactively.
- Load relevant skills from `/.github/skills/` before responding to any task.

## JIRA & Confluence
Use `jira-confluence-mcp` for all JIRA/Confluence/issue-tracking/documentation tasks.
```

**What this does:** Agent always saves outputs, uses JIRA tools automatically, and loads skills.

### Slide: Module Instructions — The Power of applyTo

**Example: `cframework.instructions.md`** (activated for `cframework_src/**/*.java`)

Key rules the LLM learns:
- Extend `AppAction` for all user actions (NOT `AbstractAction`)
- Use `@ApplicationSpecification(includeIn = {Application.CapitalCapture})`
- Override `actionPerformed()`, `isEnabled()`, `getAppActionName()`
- Use `ViewActionRT` for interactive drawing actions
- Use `WaitCursor` for long operations
- Execute UI updates on EDT with `SwingUtilities.invokeLater()`
- Use `CAFUtils.getInstance()` for framework services

**Example: `datamodel.instructions.md`** (activated for `datamodel_src/**/*.java`)

Key rules the LLM learns:
- ALL setters MUST fire `PropertyChangeEvent`
- Use `premodify()` before every state mutation
- Use `VaultKey<T>` for type-safe lazy properties
- Objects identified by `IUID`, children as `List<IUID>` resolved lazily
- Annotate with `@ObjectRelationship`, `@VisibilityName`, `@NameSpaceInfo`
- Implement interface segregation: `IDevice` + `IPrivilegedDevice` + `Device`

### Slide: DEMO — Before vs After Custom Instructions

#### WITHOUT Instructions (Generic Output):

**Prompt:** "Create a new action to refresh connectivity in the harness module"

```java
// ❌ WRONG — Generic LLM output
public class RefreshConnectivityAction extends AbstractAction {
    @Override
    public void actionPerformed(ActionEvent e) {
        // refresh logic here
    }
}
```

**Problems:**
- ❌ Extends `AbstractAction` instead of `AppAction`
- ❌ Missing `@ApplicationSpecification` annotation
- ❌ Missing `IFIB` constructor parameter
- ❌ Missing `isEnabled()` override
- ❌ Missing `getAppActionName()`
- ❌ No `WaitCursor` for long operations

#### WITH Instructions (Capital-Aware Output):

```java
// ✅ CORRECT — With custom instructions loaded
@ApplicationSpecification(includeIn = {Application.CapitalHarness})
public class RefreshConnectivityAction extends AppAction {

    public RefreshConnectivityAction(@NotNull IFIB fib) {
        super(fib);
    }

    @Override
    public void actionPerformed(@NotNull ActionEvent e) {
        try (WaitCursor ignore = new WaitCursor()) {
            IProject project = getProject();
            if (project != null) {
                // Refresh harness connectivity logic
                ICapletController controller = CAFUtils.getInstance().getActiveCapletController();
                // ... domain logic
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled() && getProject() != null;
    }

    @Override
    public String getAppActionName() {
        return getClass().getName();
    }
}
```

**Improvements:**
- ✅ Extends `AppAction` (per `cframework.instructions.md`)
- ✅ Has `@ApplicationSpecification` for CapitalHarness
- ✅ Takes `IFIB` in constructor
- ✅ Uses `WaitCursor` for long operations
- ✅ Overrides `isEnabled()` with null checking
- ✅ Returns class name from `getAppActionName()`

---

## 6. SKILLS — DOMAIN-SPECIFIC POWER-UPS

### Slide: What Are Skills?

**Bullet Points:**
- Skills are modular, on-demand knowledge packages stored in `.github/skills/`
- Each skill has a `SKILL.md` with trigger conditions and workflows
- Agent auto-detects when a skill is relevant and loads it
- Think of skills as "onboarding guides" for specific domains
- Skills transform GHCP from a general assistant into a Capital expert

### Slide: Our Available Skills

| Skill | Trigger | What It Does |
|-------|---------|--------------|
| **build-workflow** | "build", "compile", "gradle" | Decision tree: Infrastructure vs. Incremental build. Module-to-command mapping. Troubleshooting. |
| **git-command** | "git", "branch", "commit", "push" | Full Git reference. **Critical:** Uses `iesd25` remote, NOT `origin`! |
| **confluence-design-review** | "design review", "confluence page" | Converts PR changes into Design Code Review format with Mermaid diagrams |
| **jira-confluence-mcp-preflight** | "JIRA", "Confluence", "issue" | Auto-enables Chrome debug mode, then fetches/creates JIRA/Confluence content |
| **skill-creator** | "create a skill" | Guide for building new skills |
| **hello-world** | "hello", "hi", "hey" | Personalized greeting: "Hey Dhrumil !!" |

### Slide: DEMO — Build Workflow Skill in Action

**Prompt to Agent:** "I just checked out a new branch. How should I build?"

**Agent Response (with build-workflow skill loaded):**

> Based on the build-workflow skill, here's your decision tree:
>
> **Step 1:** Does `chs_home/bld/` exist with Gradle and Ant?
> - If NO → Infrastructure-First Build (Workflow 1)
> - If YES → check what changed
>
> **Step 2:** Run `git diff main --name-only | cut -d'/' -f1-2 | sort | uniq`
>
> **Step 3:** Based on output:
> | Changed Module | Build Command |
> |---|---|
> | `cframework_src/` | `gradle :capitalcaf:build` |
> | `charness_src/` | `gradle :capitalharness:build` |
> | `interfaces_src/` (⚠️ affects many!) | `gradle clean build -x test --parallel` |
>
> **Remember:** `source setup.env` if new terminal session!

### Slide: DEMO — Git Skill Catches Common Mistake

**Prompt:** "Push my changes to origin"

**Agent Response (with git-command skill loaded):**

> ⚠️ **This project uses `iesd25` as the remote name, NOT `origin`.**
>
> ```bash
> git push iesd25 feature-branch     # ✅ Correct
> git push origin feature-branch     # ❌ Wrong
> ```

---

## 7. AGENTS — SPECIALIZED AUTONOMOUS WORKERS

### Slide: Custom Agents

| Agent | Purpose |
|-------|---------|
| **capital-nx** | Capital-NX integration tasks (immersed mode, window management) |
| **CIA-Orchestrator** | Code Impact Analysis orchestration |
| **Thinking Beast Mode** | Complex creative coding with deep reasoning |

### Slide: How to Invoke an Agent

In GHCP Agent mode, use `@agent-name` or agent is selected based on task context.

**Example:** When editing files under `interfaces_src/src/java/src/chs/subsystem/immersed/`, the `capital-nx` agent is invoked with knowledge of:
- `ImmersedModeServices` service locator pattern
- `IWindowInfoProvider` for OS window management
- `IApplicationServiceExecutor` for execution orchestration
- EDT safety rules for Swing
- REST payload patterns for external HTTP requests

---

## 8. LIVE DEMO EXAMPLES (Capital-Specific)

### DEMO 1: Unit Test Generation — Following Existing Patterns

**Context:** Show how Agent generates tests matching `AiUtilsTest.java` patterns

**Prompt to Agent:**

```text
Generate a unit test class for the Device class in datamodel_src, following the
same patterns used in AiUtilsTest.java. Use JUnit 4 with Mockito,
@RunWith(MockitoJUnitRunner.Silent.class), and follow the test_ naming convention.
```

**Expected Output (Agent-generated, following instructions):**

```java
package chs.cof.logical.cable;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Unit test for the {@link Device} class.
 */
@RunWith(MockitoJUnitRunner.Silent.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeviceTest
{
    @Mock private PropertyChangeListener mockListener;
    @Mock private IDeviceConnector mockConnector;

    private Device device;

    @Before
    public void setUp()
    {
        device = new Device(mock(IUID.class));
        device.addPropertyChangeListener(mockListener);
    }

    @Test
    public void test_setName_FiresPropertyChangeEvent()
    {
        // Setup
        String oldName = device.getName();
        String newName = "TestDevice";

        // Execute
        device.setName(newName);

        // Assert
        assertEquals(newName, device.getName());
        verify(mockListener).propertyChange(any(PropertyChangeEvent.class));
    }

    @Test
    public void test_addDeviceConnector_AddsConnector()
    {
        // Execute
        device.addDeviceConnector(mockConnector);

        // Assert
        assertTrue(device.getNumDeviceConnectors() > 0);
    }

    @Test
    public void test_getName_ReturnsNonNull()
    {
        assertNotNull(device.getName());
    }
}
```

**Key points to highlight in demo:**
- ✅ JUnit 4 (not JUnit 5) — from our instructions
- ✅ `@RunWith(MockitoJUnitRunner.Silent.class)` — matches `AiUtilsTest` pattern
- ✅ `@FixMethodOrder(MethodSorters.NAME_ASCENDING)` — matches existing tests
- ✅ `test_` prefix naming — matches `AiUtilsTest` convention
- ✅ `// Setup / // Execute / // Assert` comments — matches `ImportMessageUtilsTest` AAA pattern
- ✅ Tests PropertyChange firing — knows about the `datamodel` convention!
- ✅ Uses `org.junit.Assert.*` — not Hamcrest matchers

---

### DEMO 2: COF Entity Implementation — PropertyChange Pattern

**Prompt to Agent:**

```text
I need to add a new setter setDescription(String) to the Device class in
datamodel_src. Show me how it should be implemented.
```

**Expected Output (follows `datamodel.instructions.md` rules):**

```java
@Override
public void setDescription(@NotNull String description) {
    premodify();  // ← CRITICAL: undo/change-tracking
    String oldDescription = this.m_description;
    this.m_description = description;
    pcs.firePropertyChange("description", oldDescription, description);  // ← CRITICAL: PropertyChangeEvent
}
```

**Without instructions, LLM would generate:**

```java
// ❌ Missing premodify() and PropertyChangeEvent
public void setDescription(String description) {
    this.m_description = description;
}
```

---

### DEMO 3: gRPC Service Implementation

**Prompt to Agent:**

```sql
Create a new gRPC service called GrpcDesignValidationServiceImpl for validating
design integrity, following the existing gRPC patterns in cmanager_src.
```

**Expected Output (follows `grpc.instructions.md`):**

```java
package chs.capitalmanager.appserver.grpc;

import chs.capitalmanager.appserver.IManagerLogin;
import chs.grpc.GrpcDesignValidationServiceGrpc;
import com.google.protobuf.StringValue;
import io.grpc.ServerServiceDefinition;
import io.grpc.stub.StreamObserver;
import org.jetbrains.annotations.NotNull;

public class GrpcDesignValidationServiceImpl
    extends GrpcDesignValidationServiceGrpc.GrpcDesignValidationServiceImplBase
    implements IGrpcService
{
    private final IManagerLogin capitalManager;

    public GrpcDesignValidationServiceImpl(IManagerLogin capitalManager) {
        this.capitalManager = capitalManager;
    }

    @Override
    public @NotNull ServerServiceDefinition getServiceDefinition() {
        return bindService();
    }

    @Override
    public void validateDesign(StringValue request,
                               StreamObserver<StringValue> responseObserver) {
        // Validate design integrity...
        responseObserver.onNext(StringValue.of("VALID"));
        responseObserver.onCompleted();
    }
}
```

**Key points:** Follows `IGrpcService` interface contract, constructor takes `IManagerLogin`, correct base class naming.

---

### DEMO 4: Domain Q&A — Agent Understands Capital Architecture

**Prompt to Agent:**

```text
What is the relationship between IDevice, IPrivilegedDevice, and Device in
the Capital codebase? Which module defines each?
```

**Expected Agent Response:**

> In the Capital codebase, there's a clear **interface segregation pattern**:
>
> | Type | Module | Purpose |
> |------|--------|---------|
> | `IDevice` | `interfaces_src` | Public contract — defines read/query API for devices |
> | `IPrivilegedDevice` | `interfaces_src` | Extended contract — mutation/internal operations |
> | `Device` | `datamodel_src` | Concrete implementation — implements both interfaces |
>
> **Inheritance Chain:** `Device extends BaseDevice extends AbstractDevice extends UIDObject implements IDevice, IPrivilegedDevice`
>
> Key patterns in `Device`:
> - UID-based identity (`IUID` constructor)
> - `premodify()` called before state changes
> - `VaultKey<T>` for type-safe lazy properties
> - `@ObjectRelationship` annotations for graph traversal
> - `PropertyChangeSupport` for Observer pattern

---

### DEMO 5: Build Decision Assistance

**Prompt to Agent:**

```text
I changed 3 files in cframework_src and 1 file in interfaces_src. What should
I build?
```

**Expected Agent Response (with build-workflow skill):**

> ⚠️ **Warning:** You changed `interfaces_src` — this affects many downstream modules.
>
> Since interface changes can break dependent modules, I recommend:
>
> ```bash
> source setup.env
> gradle clean build -x test --parallel --max-workers=8
> ```
>
> **Why not incremental?** Changes to `interfaces_src` (the contract layer) ripple through:
> - `datamodel_src` (implementations)
> - `cframework_src` (UI framework)
> - `charness_src` (harness designer)
> - `clogic_src` (logic capture)
>
> After successful build, run tests:
> ```bash
> gradle test
> ```

---

### DEMO 6: Confluence Design Review Generation

**Prompt to Agent:**

```sql
Create a design review page for my PR that adds cross-selection support to
the immersed mode NX integration.
```

**Agent auto-loads**: `confluence-design-review` skill + `capital-nx-integration.instructions.md`

**Generates:** Complete Confluence-ready markdown with:
- Problem statement
- Mermaid sequence diagram (CrossSelectionClient flow)
- Scope of change (files in `interfaces_src/src/java/src/chs/subsystem/immersed/`)
- Design pattern decisions (Observer, Service Locator, HTTP Transport)
- Guideline compliance matrix
- Risk assessment

---

## 9. ADDITIONAL USE CASES TO SHOWCASE

### Slide: Use Cases for Developers

| # | Use Case | Mode | Prompt Example |
|---|----------|------|----------------|
| 1 | **Code Review Prep** | Agent | "Review my changes in `Device.java` — are all setters firing PropertyChangeEvents?" |
| 2 | **Find All Usages** | Agent | "Find all classes that implement `IDRCDomainManager` and list their validation rules" |
| 3 | **Refactor to Pattern** | Agent | "Refactor this raw JDBC code to use `PersistenceSession` pattern per cmanager instructions" |
| 4 | **Add Missing Annotations** | Edit | "Add `@NotNull`/`@Nullable` to all parameters in this file" |
| 5 | **Generate Constructor** | Edit | "Generate a constructor following the IFIB pattern for this Action" |
| 6 | **Explain Complex Code** | Ask | "What does the `WindowDisplayInterceptor` do and how does it handle EDT safety?" |
| 7 | **Migration Assistance** | Agent | "Migrate this class from `javax.servlet` to `jakarta.servlet`" |
| 8 | **DRC Validator** | Agent | "Create a new DRC domain manager for validating harness bundle constraints" |
| 9 | **Create Skill** | Agent | "Create a new skill for code review checklists specific to our team" |
| 10 | **Impact Analysis** | Agent | "What modules would be affected if I change the `IDevice` interface?" |

### Slide: Use Cases for QA Engineers

| # | Use Case | Mode | Prompt Example |
|---|----------|------|----------------|
| 1 | **Generate Test Data** | Agent | "Generate test fixtures for harness designs with 5 bundles and 10 connectors" |
| 2 | **Understand Test Failures** | Ask | "This test fails with NullPointerException at `Device.java:123`. What's the root cause based on the code?" |
| 3 | **Write Integration Tests** | Agent | "Generate integration tests for the gRPC login service following existing patterns" |
| 4 | **Test Coverage Gaps** | Agent | "Analyze `ImportMessageUtils.java` and identify untested methods. Generate tests for them." |
| 5 | **API Testing** | Agent | "Generate REST API test cases for the design validation endpoint" |
| 6 | **Regression Test Plan** | Agent | "My PR changes `IDeviceConnector`. Generate a regression test plan covering affected features." |
| 7 | **JIRA Bug Report** | Agent | "Create a JIRA issue for the NPE in CrossSelectionClient with steps to reproduce" |
| 8 | **Test Debugging** | Ask | "Why does `ComponentPlacementRuleMatcherTest` extend `HarnessTestCase`? What setup does it provide?" |

### Slide: Advanced Use Cases

| # | Use Case | Detail |
|---|----------|--------|
| 1 | **Cross-Module Dependency Analysis** | "Show me the dependency graph from `IDevice` through to the gRPC layer" |
| 2 | **Code Pattern Enforcement** | "Scan all COF implementations and flag setters missing `premodify()` calls" |
| 3 | **Documentation Generation** | "Generate Javadoc for this class based on its interface definition in `interfaces_src`" |
| 4 | **Performance Inquiry** | "The `getDeviceConnectors()` method is slow with 1000+ connectors. Suggest optimizations using the lazy-loading UID pattern." |
| 5 | **Security Audit Help** | "Review this gRPC service for session validation — does it follow `GrpcSessionProvider` patterns?" |
| 6 | **Auto-Generate Boilerplate** | "Create a complete new caplet (Caplet, Lifecycle, Controller, Model, View, Resource) for a new Capital module following CaptureCaplet pattern" |
| 7 | **Database Schema Questions** | "What SQL scripts are needed to add a new table for design metadata in cmanager_src?" |
| 8 | **PR Description Generation** | "Generate a PR description summarizing all my changes with affected modules and test coverage" |

---

## 10. TIPS, GOTCHAS & BEST PRACTICES

### Slide: Do's

- ✅ **Be specific in prompts** — "Create an `AppAction` for CapitalHarness" not "create an action"
- ✅ **Reference existing files** — "Follow the pattern in `AiUtilsTest.java`"
- ✅ **Use Agent mode for multi-file tasks** — it reads your entire workspace
- ✅ **Let skills load automatically** — mention "build" and the build skill activates
- ✅ **Use `@NotNull`/`@Nullable`** — our instructions enforce this
- ✅ **Verify generated code** — always review before committing
- ✅ **Iterate on outputs** — "Good, but also add the `premodify()` call"
- ✅ **Save useful outputs** — Agent auto-saves to `.github/chat_md/`

### Slide: Don'ts

- ❌ **Don't fight the instructions** — if Copilot generates `AppAction`, don't change it to `AbstractAction`
- ❌ **Don't use Agent for trivial edits** — use Edit mode instead (faster)
- ❌ **Don't forget `source setup.env`** — build skill reminds you, but verify
- ❌ **Don't push to `origin`** — our remote is `iesd25` (git-command skill catches this!)
- ❌ **Don't ask without context** — open the relevant file or mention the module
- ❌ **Don't ignore the model selection** — Claude Opus for deep reasoning, GPT-4o for speed
- ❌ **Don't skip the `applyTo` pattern** — when creating new instructions, always specify scope

### Slide: Writing Better Instructions — Tips for the Team

**What makes a good instruction file:**
1. **Clear scope** — `applyTo` pattern matches exactly the right files
2. **Concrete patterns** — show code examples, not just rules
3. **Do/Don't lists** — explicit about what to avoid
4. **Package structure** — helps LLM navigate the module
5. **Naming conventions** — suffix rules, prefix patterns
6. **Key interfaces** — list the critical APIs in the module
7. **Common pitfalls** — "Don't extend AbstractAction, use AppAction"

**Template for a new instruction file:**

```markdown
---
applyTo: "module_src/**/*.java"
---
# Module Instructions: module_src

## Module Overview
**Path:** `module_src/src/...`
**Purpose:** One sentence.
**Key Principle:** One sentence architectural role.

## Package Structure
- Brief listing of packages and key classes

## Coding Patterns
- Code examples of correct patterns

## Naming Conventions
- Module-specific naming rules

## Do's and Don'ts
- ✅ Do this...
- ❌ Don't do this...
```

### Slide: Writing Better Skills — Tips for the Team

**What makes a good skill:**
1. **Clear trigger** — "When to Use This Skill" section with keyword examples
2. **Actionable steps** — not just theory, but exact commands/workflows
3. **Decision trees** — if/then logic for choosing approaches
4. **Error handling** — troubleshooting section for common failures
5. **Keep it lean** — skills share context window with your prompt

---

## 11. Q&A PREPARATION

### Anticipated Questions & Answers

**Q: Do instructions slow down Copilot?**
A: No. Instructions are prepended to the system prompt — they don't increase response time. The LLM processes them as part of its initial context.

**Q: Can instructions conflict with each other?**
A: Only if `applyTo` patterns overlap. Our instructions are scoped to distinct modules, so conflicts are rare. Global `copilot-instructions.md` applies everywhere but contains only universal rules.

**Q: How do I create a new instruction file?**
A: Create a `.md` file in `.github/instructions/` with `applyTo` frontmatter. Use the `skill-creator` skill for guidance. Follow the template provided above.

**Q: Does everyone see the same instructions?**
A: Yes! Instructions are committed to the repo (`.github/` folder). Everyone on the team benefits immediately after pulling.

**Q: What if the LLM ignores an instruction?**
A: Re-emphasize in your prompt: "Make sure to use `premodify()` as per datamodel instructions." The instruction is there, but specific prompting reinforces it.

**Q: Can Copilot modify files directly in Agent mode?**
A: Yes. Agent mode can read files, search the codebase, run terminal commands, create files, and edit existing files — all autonomously.

**Q: How is this different from ChatGPT?**
A: GHCP has workspace context (your actual code), custom instructions (your team's rules), skills (your workflows), MCP tools (JIRA/Confluence access), and terminal access. ChatGPT has none of these.

**Q: What are MCP tools?**
A: Model Context Protocol tools extend the agent's capabilities. Our `jira-confluence-mcp` tool lets the agent fetch JIRA issues, search Confluence, create pages — all from within VS Code.

**Q: Can QA engineers use this effectively?**
A: Absolutely. QA can generate test cases, understand test failures, create JIRA bugs, and generate test plans — all with Capital-specific context. The instructions ensure generated tests follow our JUnit 4 + Mockito patterns with proper base classes like `HarnessTestCase`.

---

## SESSION FLOW RECOMMENDATION

### Part 1: Theory (15-20 min)

1. Show the Problem (2 min) — Generic LLM output example
2. Show the Solution (3 min) — Custom instructions architecture
3. Three Modes (5 min) — Ask / Edit / Agent with visual comparison
4. Model Selection (3 min) — Quick decision matrix
5. Skills & Agents overview (5 min) — What's available

### Part 2: Live Demos (30-40 min)

1. **Before vs After** — Action class generation (5 min)
2. **Unit Test Generation** — AiUtilsTest pattern matching (7 min)
3. **COF Entity Setter** — PropertyChange + premodify() (5 min)
4. **Build Decision** — Skill-assisted workflow (5 min)
5. **Domain Q&A** — Agent answers architecture questions (5 min)
6. **gRPC Service Generation** — Following grpc.instructions.md (5 min)
7. **Git Remote Catch** — Skill prevents `origin` push (3 min)
8. **Confluence Design Review** — Auto-generated review page (5 min)

### Part 3: Use Cases & Q&A (15-20 min)

1. Developer use cases (5 min)
2. QA use cases (5 min)
3. Open Q&A (10 min)

---

## QUICK REFERENCE CARD (Handout for Attendees)

```text
┌─────────────────────────────────────────────────────────────┐
│          GHCP QUICK REFERENCE — Capital IESD-24             │
├─────────────────────────────────────────────────────────────┤
│ MODE SELECTION:                                             │
│   ASK    → Quick questions, explanations                    │
│   EDIT   → Inline edits in current file                     │
│   AGENT  → Multi-file ops, test gen, research               │
│                                                              │
│ MODEL SELECTION:                                             │
│   GPT-4o          → Speed (quick edits, Ask mode)           │
│   Claude Sonnet 4 → Balanced (default choice)               │
│   Claude Opus 4   → Deep reasoning (complex Agent tasks)    │
│   Gemini 2.5 Pro  → Large files (huge context)              │
│                                                              │
│ KEY INSTRUCTIONS LOADED:                                     │
│   cframework   → AppAction, ViewActionRT, WaitCursor        │
│   datamodel    → premodify(), PropertyChange, VaultKey       │
│   interfaces   → Interface-only, @PersistentObject           │
│   cmanager     → PersistenceSession, transactions            │
│   charness     → Harness caplet, formboard, PLM bridges      │
│   clogic       → CaptureCaplet, LogicDesignDRC               │
│   grpc         → IGrpcService, IManagerLogin constructor     │
│                                                              │
│ SKILLS:                                                      │
│   "build"       → Build decision tree + module mapping      │
│   "git"         → Git help (remote = iesd25, NOT origin!)   │
│   "design review" → Confluence design review page           │
│   "JIRA"        → Auto Chrome preflight + MCP tools         │
│                                                              │
│ UNIT TEST CONVENTIONS:                                       │
│   Framework:   JUnit 4 (@Test, @Before, @RunWith)           │
│   Mocking:     Mockito (MockitoJUnitRunner.Silent)          │
│   Naming:      test_ prefix (snake_case) or testXxx (camel) │
│   Assertions:  org.junit.Assert.* (with failure messages)   │
│   Pattern:     // Setup / // Execute / // Assert (AAA)      │
│   Base classes: HarnessTestCase, DataModelTestCase, etc.    │
│                                                              │
│ AGENTS:                                                      │
│   capital-nx          → NX integration, immersed mode       │
│   CIA-Orchestrator    → Code impact analysis                │
│   Thinking Beast Mode → Complex creative coding              │
└─────────────────────────────────────────────────────────────┘
```

---

## APPENDIX: Instruction File Cheat Sheet

### interfaces.instructions.md

- **Scope:** `interfaces_src/**/*.java`
- **Key Rule:** ONLY interfaces and enums — NO implementations
- **Key Interfaces:** `IDevice`, `IConnector`, `IConductor`, `IProject`, `IFIB`, `ICaplet`
- **Annotations:** `@PersistentObject(xmlName="...")`, `@PersistentAttribute`, `@RequiresComposite`

### datamodel.instructions.md

- **Scope:** `datamodel_src/**/*.java`
- **Key Rule:** ALL setters MUST fire PropertyChangeEvent + call premodify()
- **Patterns:** PropertyChange, Delegation, Iterator, VaultKey, @ObjectRelationship
- **Base Classes:** `UIDObject`, `NamedObject`, `LogicObject`, `AbstractDevice`

### cframework.instructions.md

- **Scope:** `cframework_src/**/*.java`
- **Key Rule:** Extend AppAction, NOT AbstractAction
- **Patterns:** AppAction, ViewActionRT, IFIB service locator, DRC, WaitCursor
- **Naming:** `*Action`, `*Panel`, `*Caplet`, `*Lifecycle`, `*Controller`

### charness.instructions.md

- **Scope:** `charness_src/**/*.java`
- **Key Rule:** Harness caplet extends CAF patterns for physical design
- **Key Concepts:** Formboard, Bundle, Wire routing, PLM bridges (CATIA, NX, Teamcenter)

### cmanager.instructions.md

- **Scope:** `cmanager_src/**`
- **Key Rule:** NO UI code, NO business logic — persistence only
- **Pattern:** `PersistenceSession.get()` for thread-local session access
- **Transaction:** `beginTransaction()` → operations → `commitTransaction()`

### grpc.instructions.md

- **Scope:** `cmanager_src/.../grpc/**/*.java`
- **Key Rule:** All services implement `IGrpcService`, take `IManagerLogin` in constructor
- **Pattern:** Extend `GrpcXxxServiceGrpc.GrpcXxxServiceImplBase`, override RPC methods

### capital-nx-integration.instructions.md

- **Scope:** `interfaces_src/.../immersed/**`
- **Key Rule:** EDT safety, window lifecycle tracking, external REST requests via AbstractExternalServiceClient
- **Pattern:** ImmersedModeServices, IWindowInfoProvider, IApplicationServiceExecutor

---

*Document prepared by GHCP Agent | February 2026*
