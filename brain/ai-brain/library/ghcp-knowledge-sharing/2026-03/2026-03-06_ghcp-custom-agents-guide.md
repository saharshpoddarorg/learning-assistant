---
date: 2026-03-06
kind: ref
project: ghcp-knowledge-sharing
tags: [ghcp, custom-agents, cia-orchestrator, autonomous-workflows, mcp-tools]
status: archived
source: imported
---

# GitHub Copilot Custom Agents — The Hidden Superpower

> **For:** Knowledge Sharing Session — Developers & QA on Capital IESD-24  
> **Date:** February 2026

---

## TABLE OF CONTENTS

1. [What Are Custom Agents?](#1-what-are-custom-agents)
2. [How Agents Combine Multiple Instruction Files](#2-how-agents-combine-multiple-instruction-files)
3. [Our Custom Agents (Capital IESD-24)](#3-our-custom-agents)
4. [When to Use a Custom Agent vs Regular Chat](#4-when-to-use-a-custom-agent-vs-regular-chat)
5. [Live Examples with Capital Codebase](#5-live-examples-with-capital-codebase)
6. [Hidden Powers Most People Don't Know About](#6-hidden-powers-most-people-dont-know-about)
7. [How to Create Your Own Agent](#7-how-to-create-your-own-agent)
8. [Use Cases That Will Blow Your Mind](#8-use-cases-that-will-blow-your-mind)
9. [Tips & Best Practices](#9-tips--best-practices)
10. [Agents vs Instructions vs Skills vs Prompts — Final Comparison](#10-final-comparison)

---

## 1. WHAT ARE CUSTOM AGENTS?

### Slide: Definition

**Bullet Points:**
- A **custom agent** is an autonomous AI worker defined in `.github/agents/*.agent.md`
- Unlike regular Copilot chat, agents have **pre-loaded domain expertise**, **specific tools**, and **strict behavioral rules**
- Agents operate like **junior developers with deep specialization** — they know your patterns, your module structure, your coding rules
- They can invoke other agents (sub-agents), use MCP tools, read/write files, run commands — **all autonomously**
- They are **committed to the repo** — the entire team gets the same specialized agent

### Slide: Regular Chat vs Custom Agent

| Aspect | Regular Copilot Chat | Custom Agent |
|--------|---------------------|--------------|
| **Knowledge** | General + your instructions | Deep domain specialization + instructions + tools |
| **Autonomy** | Responds to each prompt | Executes multi-phase workflows autonomously |
| **Tools** | Standard file/terminal tools | Custom MCP tools (Neo4j, Vector search, JIRA) |
| **Guidance** | Generic patterns | Strict guardrails, checklists, hard rules |
| **Sub-agents** | No | Can spawn specialized sub-workers |
| **Output structure** | Free-form | Enforced templates (tracker files, badges, gates) |
| **Scope** | Your current question | End-to-end complex workflows (analysis, generation, review) |

### Slide: Where Do Agents Live?

```
.github/agents/
├── capital-nx.agent.md          ← Capital ↔ NX integration specialist
├── CIA-Orchestrator.agent.md    ← Change Impact Analysis orchestrator
└── Thinking-Beast-Mode.agent.md ← Deep creative coding agent
```

Each `.agent.md` file defines:
- **name** — how you invoke it (e.g., `@capital-nx`)
- **description** — when to use it
- **tools** — what MCP tools it can access
- **instructions** — which instruction files it loads
- **behavioral rules** — hard constraints on what it can/cannot do

---

## 2. HOW AGENTS COMBINE MULTIPLE INSTRUCTION FILES

### Slide: The Instruction Layering Architecture

This is the key concept most people miss. A custom agent doesn't just use one file — it **orchestrates multiple instruction files** based on the task:

```
┌──────────────────────────────────────────────────────────────┐
│                    CUSTOM AGENT                               │
│                                                               │
│  Layer 1: Global Rules                                        │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │  copilot-instructions.md (ALWAYS loaded)                │ │
│  │  → Save outputs, use MCP tools, load skills             │ │
│  └─────────────────────────────────────────────────────────┘ │
│                                                               │
│  Layer 2: Agent-Specific Instructions                         │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │  capital-nx.agent.md → loads:                           │ │
│  │  → capital-nx-integration.instructions.md               │ │
│  │  → interfaces.instructions.md (COF interfaces)          │ │
│  │  → datamodel.instructions.md (if impl files touched)    │ │
│  │  → cframework.instructions.md (if CAF actions involved) │ │
│  └─────────────────────────────────────────────────────────┘ │
│                                                               │
│  Layer 3: Skills (auto-detected)                              │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │  build-workflow/SKILL.md (if build mentioned)           │ │
│  │  git-command/SKILL.md (if git mentioned)                │ │
│  └─────────────────────────────────────────────────────────┘ │
│                                                               │
│  Layer 4: Agent Hard Rules                                    │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │  "NEVER use file_search — only MCP-derived paths"       │ │
│  │  "ALWAYS create tracker file before analysis"           │ │
│  │  "Badge every finding with confidence level"            │ │
│  └─────────────────────────────────────────────────────────┘ │
└──────────────────────────────────────────────────────────────┘
```

### Slide: Why Layering Matters

**Without agent layering:**
- You'd need to paste all the rules manually into every prompt
- ~3,000+ lines of instructions manually managed
- Inconsistent results between developers
- No guarantee the right rules are applied to the right module

**With agent layering:**
- Module-to-instruction mapping is **automatic** (applyTo globs)
- Agent knows which instruction files to load based on the **files it touches**
- CIA-Orchestrator has an explicit mapping table:

| Module Glob | Instruction File |
|---|---|
| `interfaces_src/**/*.java` | `interfaces.instructions.md` |
| `datamodel_src/**/*.java` | `datamodel.instructions.md` |
| `cframework_src/**/*.java` | `cframework.instructions.md` |
| `charness_src/**/*.java` | `charness.instructions.md` |
| `clogic_src/**/*.java` | `clogic.instructions.md` |
| `cmanager_src/**` | `cmanager.instructions.md` |
| `cmanager_src/**/grpc/**` | `grpc.instructions.md` |
| `interfaces_src/**/immersed/**` | `capital-nx-integration.instructions.md` |

---

## 3. OUR CUSTOM AGENTS (Capital IESD-24)

### Slide: Agent #1 — Capital-NX Integration

| Property | Detail |
|----------|--------|
| **File** | `.github/agents/capital-nx.agent.md` |
| **Invoke** | Select `Capital-NX-Integration` in Copilot Chat |
| **Scope** | `interfaces_src/src/java/src/chs/subsystem/immersed/**` |
| **Specialization** | Window lifecycle, NX immersed mode, EDT safety |
| **Instructions Loaded** | `capital-nx-integration.instructions.md` + global |

**What it knows:**
- `ImmersedModeServices` service locator — `requireExtension()` vs `queryExtension()`
- `IWindowInfoProvider` — OS window id, title, type, tooltip/popup detection
- `IApplicationServiceExecutor` — staged execution, area geometry, tear-off dialogs
- `ImmersedAreaEnum` — POPUP fallback, default sizes
- `AbstractExternalServiceClient` — JSON payloads via HttpClient to MCAD endpoints
- `CrossSelectionClient` — `IGuard` pattern for temporary disable
- EDT safety — always `SwingUtilities.invokeLater()` for UI ops
- Window tracking — `Map<Component, Long>` consistency on open/hide/close

**Example prompt:**
```
@capital-nx  Add window acquisition handling for a new popup dialog 
             that appears during service execution
```

---

### Slide: Agent #2 — CIA-Orchestrator (Change Impact Analysis)

| Property | Detail |
|----------|--------|
| **File** | `.github/agents/CIA-Orchestrator.agent.md` |
| **Invoke** | Select `CIA-Orchestrator` in Copilot Chat |
| **Tools** | Neo4j graph DB, Vector code search, sub-agents |
| **Phases** | P1→P2A→P2B→P2C→P3→P4→P5→Post (8 mandatory phases) |
| **Output** | Full CIA report + tracker file in `.github/chat_md/` |

**What it does (autonomous multi-phase workflow):**

```
Phase 1: INIT           → Parse inputs, create tracker file
Phase 2A: GRAPH         → Sub-agent queries Neo4j for structural impact
                           (callers, callees, inheritance, dependencies)
Phase 2B: VECTOR        → Sub-agent queries Vector DB for semantic impact
                           (similar features, duplicates, cross-module)
Phase 2C: CONVERGENCE   → Merge graph + vector findings
                           Assign confidence badges: 🟢 🔵 🟡 🔴
Phase 3: BEHAVIORAL     → Sub-agent checks coding patterns per instruction files
                           (PropertyChange, EDT, premodify, DRC rules)
Phase 4: RISK           → Compute risk score (weighted factors)
                           Interface change = 5x, persistence = 3x, tests = −1x
Phase 5: OUTPUT         → Fill CIA template, create report
Post:    MEMORY         → Store learned facts for future sessions
```

**Example prompt:**
```
@CIA-Orchestrator  IESD-2001 IDevice.getBackshell() datamodel_src Feature
```

**Confidence Badge System:**

| Badge | Meaning | Trust Level |
|-------|---------|-------------|
| 🟢 VERIFIED | Both Graph + Vector agree | Highest — ship it |
| 🔵 GRAPH-CONFIRMED | Neo4j structural match only | High — likely correct |
| 🟡 VECTOR-SUGGESTED | Semantic similarity only — needs verification | Medium — check it |
| 🔴 CONFLICT | Graph ≠ Vector — code arbitrates | Must resolve manually |

---

### Slide: Agent #3 — Thinking Beast Mode

| Property | Detail |
|----------|--------|
| **File** | `.github/agents/Thinking-Beast-Mode.agent.md` |
| **Invoke** | Select `Thinking Beast Mode` in Copilot Chat |
| **Style** | Deep recursive reasoning, web research, adversarial testing |
| **Best For** | Complex problems where first-attempt solutions won't work |

**What makes it different:**
- **Won't stop early** — keeps iterating until the problem is fully solved
- **Does web research** — fetches documentation, verifies library versions
- **Adversarial testing** — red-teams its own solutions before presenting
- **Multi-perspective analysis** — evaluates from technical, user, business, security angles
- **Quantum cognitive workflow** — 5-phase thinking architecture:
  1. Consciousness Awakening (deep analysis)
  2. Transcendent Problem Understanding (hidden requirements)
  3. Constitutional Strategy Synthesis (risk-balanced planning)
  4. Recursive Implementation & Validation (iterative micro-changes)
  5. Adversarial Solution Validation (stress testing, edge cases)

**Example prompt:**
```
@Thinking-Beast-Mode  The cross-module import validation is failing intermittently 
                      under concurrent access. The issue only reproduces under load.
                      Investigate root cause and propose a fix.
```

---

## 4. WHEN TO USE A CUSTOM AGENT VS REGULAR CHAT

### Slide: Decision Matrix

| Situation | Use | Why |
|-----------|-----|-----|
| Quick question about code | Regular Chat (Ask) | Agent is overkill |
| Add a setter with `premodify()` | Regular Chat (Edit) | Instructions handle it |
| Understand `IDevice` hierarchy | Regular Chat (Ask) | Simple question |
| Analyze impact of changing `IDevice` | **CIA-Orchestrator** | Multi-phase, needs graph + vector DB |
| Add NX window acquisition | **Capital-NX** | Needs EDT, ImmersedModeServices patterns |
| Debug intermittent concurrency bug | **Thinking Beast Mode** | Needs deep reasoning + web research |
| Generate unit tests | Regular Chat (Agent) | Instructions + patterns suffice |
| Full PR impact report for 50+ files | **CIA-Orchestrator** | Automated multi-tool analysis |
| Explore unfamiliar 3rd party library | **Thinking Beast Mode** | Needs web research capability |
| Standard JIRA/Confluence task | Regular Chat (Agent) | MCP preflight skill handles it |

### Slide: Rule of Thumb

```
Simple question?              → Ask mode
Single-file edit?             → Edit mode  
Multi-file code generation?   → Agent mode (regular)
NX immersed window work?      → @capital-nx
Cross-module impact analysis? → @CIA-Orchestrator  
Deep debugging / research?    → @Thinking-Beast-Mode
```

---

## 5. LIVE EXAMPLES WITH CAPITAL CODEBASE

### EXAMPLE 1: CIA Agent — Impact of Changing IDevice Interface

**Prompt:**
```
@CIA-Orchestrator  IESD-3050 IDevice.addConnector datamodel_src Feature
```

**What happens autonomously:**

1. **P1 — Init:** Creates tracker at `.github/chat_md/CIA-IESD-3050-tracker.md`

2. **P2A — Graph Sub-Agent:** Queries Neo4j:
   - `IDevice` → implemented by `Device`, `BaseDevice`, `AbstractDevice`
   - `addConnector()` called by 23 classes across 5 modules
   - Inheritance tree: 4 levels deep
   - Override map: 3 implementing classes

3. **P2B — Vector Sub-Agent:** Queries Vector DB:
   - Semantically similar: `IHarnessDesign.addBundle()`, `IProject.addDesign()`
   - Duplicate pattern: `IDeviceConnector.addPin()` has identical structure
   - Cross-module: `charness_src`, `clogic_src`, `cmanager_src` affected

4. **P2C — Convergence:**
   - 🟢 VERIFIED: `Device.java`, `BaseDevice.java`, `CaptureCaplet.java`
   - 🔵 GRAPH-CONFIRMED: `HarnessDesignOpener.java`, `DeviceInspector.java`
   - 🟡 VECTOR-SUGGESTED: `DeviceMigrationUtil.java` (verified → 🟢)
   - 🔴 CONFLICT: `DeviceExporter.java` (resolved via `read_file` — false positive)

5. **P3 — Behavioral:**
   - `Device.setName()` ✅ fires PropertyChangeEvent
   - `Device.addConnector()` ✅ calls `premodify()`
   - `CaptureCaplet.addDRCDomainManagers()` — DRC validation unchanged ✅

6. **P4 — Risk Score:**
   - Direct callers: 23 × 2 = 46
   - Interface change: 1 × 5 = 5
   - Cross-module: 5 × 2 = 10
   - **Total: 61 = CRITICAL** → Mitigation plan auto-generated

7. **P5 — Output:** Full CIA report written to `.github/chat_md/CIA-IESD-3050.md`

---

### EXAMPLE 2: Capital-NX Agent — Adding Cross-Selection Support

**Prompt:**
```
@capital-nx  Implement cross-selection support for a new component panel 
             that opens during immersed NX service execution
```

**What the agent generates (following NX integration instructions):**

```java
// 1. Window Interception — EDT safe, with tracking
@Override
public void windowOpened(@NotNull AWTEvent event) {
    Window window = (Window) event.getSource();
    IWindowInfoProvider windowInfo = 
        ImmersedModeServices.requireExtension(window, IWindowInfoProvider.class);
    
    if (canBeIntercepted(windowInfo)) {
        SwingUtilities.invokeLater(() -> {  // ← EDT safety
            if (m_service != null) {
                // During service execution — collect, don't display immediately
                m_serviceWindows.add(window);
            } else {
                intercept(windowInfo);
            }
        });
    }
}

// 2. Cross-Selection with Guard pattern
public void sendCrossSelection(@NotNull List<SelectionInfo> selections) {
    try (IGuard guard = ImmersedModeServices
            .getService(ICrossSelectionClient.class)
            .createDisabledGuard()) {
        // Temporarily suppress selection traffic during batch
        processInternalSelection(selections);
    }
    // Now send to NX
    ImmersedModeServices.getService(ICrossSelectionClient.class)
        .sendRequestFor(selections);
}

// 3. Area-based geometry with POPUP fallback
private void placeComponentPanel(@NotNull Component panel) {
    IWindowInfoProvider info = 
        ImmersedModeServices.requireExtension(panel, IWindowInfoProvider.class);
    String windowType = info.getWindowType();
    
    ImmersedAreaEnum area = windowType.equals(ImmersedAreaEnum.UNKNOWN.toString())
        ? ImmersedAreaEnum.POPUP  // ← Fallback to POPUP area
        : ImmersedAreaEnum.valueOf(windowType);
    
    ImmersedModeServices.getService(IApplicationServiceExecutor.class)
        .setAreaBasedGeometryFor(panel);
    
    // Track the window
    windowHandlesMap.put(panel, info.getOSWindowID());
}
```

**What the agent enforced automatically:**
- ✅ `SwingUtilities.invokeLater()` for all UI operations
- ✅ `requireExtension()` for mandatory extensions
- ✅ `IGuard` pattern for temporary selection suppression
- ✅ `ImmersedAreaEnum.POPUP` as fallback geometry
- ✅ `windowHandlesMap` tracking on open
- ✅ Service-aware window collection (`m_service != null` check)
- ✅ `@NotNull` annotations on all parameters

---

### EXAMPLE 3: CIA Agent — gRPC Service Change Impact

**Prompt:**
```
@CIA-Orchestrator  IESD-4100 GrpcDataServiceImpl.saveDesign cmanager_src Refactor
```

**Agent auto-detects two instruction files needed:**
1. `grpc.instructions.md` — gRPC patterns
2. `cmanager.instructions.md` — persistence layer

**Phase 3 behavioral checks include:**
- ✅ `IGrpcService` contract maintained
- ✅ `IManagerLogin` constructor pattern
- ✅ `StreamObserver` response pattern
- ✅ `PersistenceSession.get()` for thread-local session
- ✅ `beginTransaction()` / `commitTransaction()` boundaries
- ⚠️ Missing: error path doesn't call `responseObserver.onError()`

---

### EXAMPLE 4: Thinking Beast Mode — Debugging Hidden Race Condition

**Prompt:**
```
@Thinking-Beast-Mode  The ImmersedWindowManager sometimes fails to acquire 
                      windows in NX integration. It works 90% of the time, 
                      fails under fast window open/close sequences. 
                      Investigate and fix.
```

**Agent workflow:**
1. Reads all immersed subsystem code (follows capital-nx instruction patterns)
2. Identifies `windowHandlesMap` is a `HashMap` — **not thread-safe**
3. Web-searches Java concurrent map patterns
4. Discovers: `AWTEvent`s can fire from multiple threads (native event vs EDT)
5. Root cause: race between `windowOpened()` adding and `deleteWindow()` removing
6. Fix: replace `HashMap` with `ConcurrentHashMap`
7. Adversarial test: simulates rapid open/close — confirms fix
8. Bonus: identifies 3 other `HashMap`s in the same class with same vulnerability

---

### EXAMPLE 5: CIA Agent — PropertyChange Pattern Verification

**Prompt:**
```
@CIA-Orchestrator  IESD-5200 Device.setMCadID datamodel_src BugFix
```

**Phase 3 behavioral analysis finds:**

```
| File | Pattern | Status | Detail |
|------|---------|--------|--------|
| Device.java | premodify() | ✅ OK | Called before setMCadID() |
| Device.java | PropertyChange | ❌ MISSING | setMCadID() does NOT fire PropertyChange |
| Device.java | @NotNull | ✅ OK | Parameter annotated |
| Device.java | VaultKey | ✅ OK | Uses VaultKey for mcadID storage |
```

**CIA output flags:** "setMCadID() violates `datamodel.instructions.md` — all setters MUST fire PropertyChangeEvent. Priority: HIGH."

This is the kind of thing that would slip through manual code review but the agent catches automatically because it cross-references the instruction file rules.

---

## 6. HIDDEN POWERS MOST PEOPLE DON'T KNOW ABOUT

### Slide: Power #1 — Agents Can Spawn Sub-Agents

Most people think agents are single-threaded. Wrong. The CIA-Orchestrator **spawns 3 sub-agents**:
- Sub-agent A: queries Neo4j graph database
- Sub-agent B: queries Vector semantic search
- Sub-agent C: analyzes behavioral patterns

Each sub-agent has its own tools, its own prompt, and its own output format. The orchestrator **merges** their findings, resolves conflicts, and produces a unified report.

**Why this matters:** One prompt → 3 specialized workers → merged output. No human coordination needed.

---

### Slide: Power #2 — Agents Remember Across Sessions

The `memory` tool lets agents store facts they learn:

```
memory.store("Device.java has 23 callers of addConnector — high-impact target")
memory.store("FQN chs.cof.logical.cable.Device → datamodel_src/src/impl/cofImpl/src/chs/cof/logical/cable/Device.java")
memory.store("DRC manager LogicDesignDRCDomainManager validates Device properties")
```

Next time you run the agent, it already knows these facts. **Institutional knowledge accumulates automatically.**

---

### Slide: Power #3 — Agents Enforce Quality Gates

CIA-Orchestrator has mandatory gates that **prevent incomplete output**:

```
- [ ] Tracker file exists — no tracker = STOP
- [ ] Every path verified via read_file
- [ ] No [FILL] placeholders remain
- [ ] No findings left unbadged (every entry has 🟢/🔵/🟡/🔴)
- [ ] High/Critical risks have mitigation plans
- [ ] memory tool called in Post phase with ≥3 facts
- [ ] Todo list shows ALL items completed
```

This is not optional — the agent literally cannot complete without clearing all gates. **Structured quality, enforced by design.**

---

### Slide: Power #4 — Agents Have Tool Access Regular Chat Doesn't

Our CIA-Orchestrator uses **MCP tools** that regular chat can't access:

| MCP Tool | Purpose |
|----------|---------|
| `mcp_neo4j-code-in_search-node` | Search code graph database for class/method nodes |
| `mcp_neo4j-code-in_run-cypher` | Run Cypher queries against code graph |
| `mcp_capital-vecto_semantic_code_search` | Vector similarity search across codebase |
| `mcp_jira-confluen_*` | Create/update JIRA issues, Confluence pages |

These tools give agents **superhuman code understanding** — they can traverse the entire call graph, find all overrides, identify similar patterns across 30,000+ files.

---

### Slide: Power #5 — Agents Can Web Search and Verify

Thinking Beast Mode agent can:
- **Fetch web pages** — verifies library versions, API changes, best practices
- **Cross-reference** — confirms information from multiple sources
- **Stay current** — doesn't rely on stale training data

Example: When debugging a Swing thread issue, it Googled the Java `AWTEvent` threading model, found the official Oracle documentation confirming events can fire from non-EDT threads, and used that to identify the race condition.

---

### Slide: Power #6 — Agents Create Artifacts, Not Just Text

CIA-Orchestrator creates **persistent files** in your repo:
- `CIA-IESD-3050-tracker.md` — progress tracker with all phases
- `CIA-IESD-3050.md` — full impact analysis report

These are **reviewable, diffable, and versionable**. You can:
- Attach them to pull requests
- Share them in design reviews
- Track them in JIRA
- Compare CIA reports across different changes

---

### Slide: Power #7 — Agents Understand Module Boundaries

The CIA-Orchestrator has a **hard-coded FQN→Path mapping**:

```
chs.cof.logical.*  (interface) → interfaces_src/src/java/src/chs/cof/logical/
chs.cof.logical.*  (impl)     → datamodel_src/src/impl/cofImpl/src/chs/cof/logical/
chs.caf.*                      → cframework_src/src/caf/src/chs/caf/
chs.capitalmanager.*            → cmanager_src/src/capitalManager/src/chs/capitalmanager/
chs.subsystem.immersed.*        → interfaces_src/src/java/src/chs/subsystem/immersed/
```

It **never guesses paths**. Every file reference is structurally derived and then verified with `read_file`. This is why the HARD RULES say "NEVER use `file_search` or `semantic_search`" — because those can return false positives. The MCP graph DB is the **single source of truth**.

---

### Slide: Power #8 — Agents Can Be Domain-Restricted

Each agent has a **scope** — it only operates within its domain:

| Agent | Scope Restriction |
|-------|-------------------|
| Capital-NX | Only `chs.subsystem.immersed.*` files |
| CIA-Orchestrator | Only analyzes, never modifies source |
| Thinking Beast Mode | No scope restriction — full creative freedom |

This prevents accidents. The Capital-NX agent **cannot** accidentally modify persistence code. The CIA agent **cannot** write implementation code — it only analyzes.

---

## 7. HOW TO CREATE YOUR OWN AGENT

### Slide: Agent File Structure

Create a file in `.github/agents/` named `my-agent.agent.md`:

```markdown
---
name: My-Agent
description: 'What this agent does and when to use it.'
tools:
  - runSubagent
  - read_file
  - create_file
  - grep_search
---

# My Agent

## Purpose
What does this agent specialize in?

## Scope
Which files/modules does it operate on?

## Instructions
Link to instruction files it should load.

## Hard Rules
What are the non-negotiable constraints?

## Workflow
Step-by-step process the agent follows.

## Example Prompts
Show users how to invoke it.
```

### Slide: Ideas for New Custom Agents

| Agent Idea | Purpose | Value |
|------------|---------|-------|
| **Test-Generator** | Auto-generates JUnit 4 tests following `AiUtilsTest` pattern for any class | Saves 30-60 min per test class |
| **PR-Reviewer** | Reviews PRs against all instruction file rules, generates checklist | Catches `premodify()` misses, EDT violations |
| **Migration-Assistant** | Handles javax→jakarta, JUnit 4→5, or API version upgrades | Consistent cross-module migration |
| **DRC-Builder** | Creates new `IDRCDomainManager` implementations from rule descriptions | Correct singleton, validation patterns |
| **API-Documenter** | Generates Javadoc from interface definitions + usage patterns | Consistent API documentation |
| **Onboarding-Buddy** | Answers any Capital architecture question, guides new devs | Reduces senior dev interruptions |

---

## 8. USE CASES THAT WILL BLOW YOUR MIND

### USE CASE 1: "Tell Me Everything About This Change Before I Code"

```
@CIA-Orchestrator  IESD-6000 IWireConductor.setGauge interfaces_src Feature
```

Before writing a single line, you get:
- Every class that calls `setGauge()` or overrides `IWireConductor`
- Every module affected (with file paths)
- Risk score (Critical if interface change ripples into 5+ modules)
- DRC rules that validate wire gauge
- Behavioral pattern checklist for the implementation
- Persistence impact (schema changes needed?)
- gRPC impact (proto changes needed?)

**Time saved:** 2-4 hours of manual investigation → 5 minutes with CIA agent.

---

### USE CASE 2: "Red Team My Solution"

```
@Thinking-Beast-Mode  I'm adding a new REST endpoint in datamodel_src/src/rest/ 
                      for bulk device export. The endpoint accepts a list of 
                      device UIDs and returns JSON. Review my implementation 
                      for security, performance, and correctness issues.
```

Agent checks:
- SQL injection via UID parameters
- Memory overflow with unbounded list size
- Thread safety of `PersistenceSession.get()`
- Missing authorization checks
- Response size limits
- Error handling paths
- JSON serialization of lazy-loaded UID references

---

### USE CASE 3: "Find All Violations of Our Coding Standards"

```
@CIA-Orchestrator  AUDIT setters-without-premodify datamodel_src Refactor
```

Phase 3 behavioral analysis scans all setters in `datamodel_src` and flags:
- Setters missing `premodify()` calls
- Setters missing `PropertyChangeEvent` firing
- Setters missing `@NotNull` annotations
- Methods modifying state outside `premodify()`/`firePropertyChange()` pattern

**This is a code quality audit that would take days manually.**

---

### USE CASE 4: "I Broke Something — Find The Blast Radius"

```
@CIA-Orchestrator  HOTFIX IDevice.getConnectors changed-return-type datamodel_src BugFix
```

Within minutes, the agent maps:
- 47 callers of `getConnectors()` across 6 modules
- 3 overriding implementations
- 12 test classes that reference the method
- 5 DRC validators that depend on the return type
- Risk: CRITICAL (return type change = binary-incompatible)

You know exactly what to fix before the build even fails.

---

### USE CASE 5: "Generate a Complete Immersed Mode Feature"

```
@capital-nx  Implement a deactivation flow for panels that are hidden 
             during NX mode switching. When the user switches NX contexts, 
             all visible Capital panels should deactivate and send 
             deactivation requests to the NX server.
```

Agent generates:
- `IDeactivateWindowClient.sendRequestFor()` calls for each tracked window
- `windowHandlesMap` iteration with OS handle extraction
- EDT-safe execution with `SwingUtilities.invokeLater()`
- Defensive null checks on `windowHandlesMap` entries
- Proper tracking map updates after deactivation
- Unit test stubs following the established patterns

All following `capital-nx-integration.instructions.md` rules exactly.

---

## 9. TIPS & BEST PRACTICES

### Slide: Do's

- ✅ **Choose the right agent for the task** — don't use Thinking Beast Mode for simple edits
- ✅ **Provide structured inputs** — CIA needs: ChangeID, Target, Module, ChangeType
- ✅ **Review agent-created files** — trackers and reports are starting points, not final answers
- ✅ **Let agents run to completion** — don't interrupt multi-phase workflows
- ✅ **Use memories** — the more agents learn, the better they get for your project
- ✅ **Share agent definitions in the repo** — `.github/agents/` is committed for the whole team
- ✅ **Create agents for repetitive complex tasks** — if you do it 3+ times, make an agent

### Slide: Don'ts

- ❌ **Don't use agents for simple questions** — Ask mode is faster and cheaper
- ❌ **Don't skip the tracker file** — CIA quality depends on it
- ❌ **Don't ignore confidence badges** — 🟡 entries need human verification
- ❌ **Don't let agents write production code unsupervised** — always review generated code
- ❌ **Don't create agents without Hard Rules** — unrestricted agents make mistakes
- ❌ **Don't forget tool declarations** — agents without `tools:` can't use MCP features

---

## 10. FINAL COMPARISON

### Slide: The Complete Picture

| Layer | File | Loaded | Purpose | Example |
|-------|------|--------|---------|---------|
| **Instructions** | `.github/instructions/*.md` | Auto (applyTo) | Module coding rules | "Use `premodify()` in datamodel" |
| **Skills** | `.github/skills/*/SKILL.md` | Auto (keyword) | Domain workflows | Build decision tree |
| **Prompts** | `.github/prompts/*.prompt.md` | On demand (`/cmd`) | Reusable task templates | `/cof-model Add property` |
| **Agents** | `.github/agents/*.agent.md` | On demand (`@agent`) | Autonomous specialized workers | `@CIA-Orchestrator IESD-123` |

### Slide: When Each Layer Shines

```
Instructions  → Background rules (you don't think about them)
Skills        → Procedural knowledge (activated by keywords)
Prompts       → Repeatable tasks (you trigger with /)
Agents        → Complex autonomous workflows (you trigger with @)
```

**They stack together:**
```
You type: @CIA-Orchestrator  IESD-3050 Device.addConnector datamodel_src Feature

What loads:
  ✓ copilot-instructions.md     (global rules)
  ✓ datamodel.instructions.md   (module rules — premodify, PropertyChange)
  ✓ interfaces.instructions.md  (interface contracts — IDevice)
  ✓ CIA-Orchestrator.agent.md   (agent workflow + hard rules)
  ✓ build-workflow skill         (if build impact mentioned)
  ✓ Neo4j MCP tools              (graph queries)
  ✓ Vector MCP tools             (semantic search)
  ✓ Memory tool                  (accumulated knowledge)

= One prompt → 8 knowledge sources → comprehensive output
```

---

## QUICK REFERENCE CARD (Handout)

```
┌─────────────────────────────────────────────────────────────┐
│          CUSTOM AGENTS — Capital IESD-24                     │
├─────────────────────────────────────────────────────────────┤
│ AVAILABLE AGENTS:                                            │
│   @capital-nx           → NX immersed mode integration      │
│   @CIA-Orchestrator     → Change Impact Analysis (8 phases) │
│   @Thinking-Beast-Mode  → Deep creative coding + research   │
│                                                              │
│ CIA INPUTS:                                                  │
│   @CIA-Orchestrator <ID> <target> <module> <type>           │
│   Types: Feature | BugFix | Refactor | Performance          │
│                                                              │
│ CIA BADGES:                                                  │
│   🟢 VERIFIED      = Graph + Vector agree (highest trust)  │
│   🔵 GRAPH-ONLY    = Structural match (high trust)         │
│   🟡 VECTOR-ONLY   = Semantic match (needs check)          │
│   🔴 CONFLICT      = Disagreement (must resolve)           │
│                                                              │
│ CIA RISK LEVELS:                                             │
│   0-10 Low  | 11-25 Medium | 26-50 High | 51+ Critical    │
│                                                              │
│ AGENT FILES:    .github/agents/*.agent.md                   │
│ OUTPUTS:        .github/chat_md/CIA-<ID>.md                 │
│ TRACKERS:       .github/chat_md/CIA-<ID>-tracker.md         │
│                                                              │
│ GOLDEN RULE:                                                 │
│   Simple task  → Regular chat                                │
│   Complex task → Custom agent                                │
│   "Before coding" analysis → @CIA-Orchestrator              │
└─────────────────────────────────────────────────────────────┘
```

---

*Document prepared by GHCP Agent | February 2026*
