---
id: BLI-092
title: Design tool-agnostic abstraction layer for customization primitives
status: todo
priority: high
type: design
created: 2026-07-31
updated: 2026-07-31
started: null
completed: null
blocked-since: null
review-since: null
epic: BLI-090
sprint: null
parent: null
sub-items: []
origin: null
estimated-effort: L
actual-effort: null
tags: [design, architecture, abstraction-layer, tool-agnostic, primitives, mapping, schema]
origin-type: manual
import-batch: null
source-file: null
---

# BLI-092: Design — Tool-agnostic abstraction layer for customization primitives

## Description

This BLI produces an **architectural design document** that defines:

1. The **abstract model** of customization primitives (instructions, prompts, skills, agents, MCP)
2. **Tool-specific adapters** that map abstract primitives to each tool's native format
3. **Directory structure** for organizing tool-agnostic source and tool-specific outputs
4. **Transformation rules** showing how to convert between formats
5. **Validation schema** ensuring consistency across all tools

### Goals

1. Create a single "source of truth" for each customization primitive
2. Enable tool-specific adaptations without duplicating content
3. Support future tools with minimal new code (extensibility)
4. Provide clear mapping rules so humans can add content without scripts
5. Ensure cross-tool consistency (same instruction doesn't contradict itself in different tools)

---

## Core Concepts

### Primitive Types (Abstract Layer)

The abstract layer defines 5 primitive types that exist across all (or most) tools:

| Primitive | Abstract Concept | Copilot | Cursor | Claude | Gemini | ChatGPT |
|---|---|---|---|---|---|---|
| **Instruction** | Rules/guidelines that shape behavior system-wide | `.github/instructions/*.md` | `.claude/instructions/` | Custom Instructions | Custom Instructions | Custom Instructions |
| **Prompt** | Template/slash-command for multi-turn workflows | `.github/prompts/*.prompt.md` | `.claude/prompts/` | (File collections?) | (Saved chats?) | (GPT Instructions?) |
| **Skill** | Grouped knowledge + context for specific domains | `.github/skills/**/*.md` | `.claude/skills/` | (via long context?) | (Extensions?) | (GPT Actions?) |
| **Agent** | Persona/role with specific behavior and tools | `.github/agents/*.md` or agent dropdown | (Roles?) | (via system prompt?) | (Personas?) | (GPT Mode?) |
| **MCP Tool** | Integration with external services/APIs | MCP server + tool spec | (Claude Tools?) | Claude API tools | (Extensions?) | GPT Actions/Plugins |

### Tool-Specific Implementations

Not all tools support all primitives at the same level. The abstraction layer accounts
for this with **support levels**:

```text
Support Levels:

🟢 NATIVE    — Tool has built-in, first-class support
🟡 PARTIAL   — Tool supports it but with limitations (fewer features, token limits)
🔴 WORKAROUND — Requires creative implementation or manual steps
⚪ UNSUPPORTED — Tool doesn't support this primitive
```

---

## Proposed Directory Structure

### Conceptual Option A: Shared Core + Tool-Specific Adapters

```text
learning-assistant/
│
├── .common/                        ← Tool-agnostic source of truth
│   ├── instructions/
│   │   ├── change-completeness.instruction.md     (abstract)
│   │   ├── md-formatting.instruction.md           (abstract)
│   │   └── java.instruction.md                    (abstract)
│   ├── prompts/
│   │   ├── hub.prompt.md                          (abstraction)
│   │   ├── research.prompt.md                      (abstract)
│   │   └── ...
│   ├── skills/
│   │   ├── java-build/SKILL.md                    (abstract)
│   │   └── ...
│   ├── agents/                    (abstract personas/roles)
│   └── MAPPING.md                 (mapping rules: .common → tool-specific)
│
├── .github/                        ← GitHub Copilot (current)
│   ├── instructions/              (derived from .common/)
│   ├── prompts/
│   ├── skills/
│   └── agents/
│
├── .claude/                        ← Cursor / Claude
│   ├── instructions/              (adapted from .common/)
│   ├── prompts/
│   ├── skills/
│   └── README-claude-setup.md
│
├── .gemini/                        ← Google Gemini
│   ├── instructions/              (adapted from .common/)
│   ├── prompts/
│   └── README-gemini-setup.md
│
├── .chatgpt/                       ← ChatGPT / OpenAI Assistants
│   ├── system-prompts/
│   ├── gpt-config.json           (GPT Builder config)
│   └── README-chatgpt-setup.md
│
└── tools/
    ├── sync-customization.ps1     ← Propagate .common → tool-specific
    ├── validate-all-tools.ps1     ← Validate consistency
    └── adapters/
        ├── CopilotAdapter.ps1     (transforms .common → .github)
        ├── ClaudeAdapter.ps1      (transforms .common → .claude)
        ├── GeminiAdapter.ps1      (transforms .common → .gemini)
        └── ChatGptAdapter.ps1     (transforms .common → .chatgpt)
```

### Conceptual Option B: Tool-Specific Only (No Separate Core)

```text
learning-assistant/
│
├── .github/                        ← GitHub Copilot (primary)
│   ├── instructions/
│   ├── prompts/
│   ├── skills/
│   └── agents/
│
├── .claude/                        ← Cursor / Claude (synced from .github)
│   └── (adapted copies)
│
├── .gemini/                        ← Gemini (synced from .github)
│   └── (adapted copies)
│
└── tools/sync-*.ps1               ← One-way sync from .github → other tools
```

**Trade-off:**
- **Option A (Shared Core)** — purer architecture, true tool-agnosticism, but more overhead
- **Option B (Tool-Specific)** — simpler to implement, `.github/` is already complete, less duplication

**Recommendation:** **Start with Option B** — it's lower lift and `.github/` is production.
Migrate to Option A later if the volume of multi-tool content justifies it.

---

## Transformation Rules (Adapter Logic)

### Rule 1 — Instructions

**Abstract (Tool-Agnostic):**
```markdown
---
applyTo: "**/*.java"
---
# My Instruction

Content that applies universally...
```

**Copilot (.github/):** Kept as-is (YAML frontmatter + Markdown)

**Claude (.claude/):** Convert YAML to Markdown metadata block + Claude XML structure:
```markdown
# My Instruction

<configuration>
<applies-to>**/*.java</applies-to>
<applies-to-type>file-pattern</applies-to-type>
</configuration>

Content that applies universally...
```

**Gemini (.gemini/):** JSON format:
```json
{
  "id": "my-instruction",
  "title": "My Instruction",
  "appliesToPattern": "**/*.java",
  "content": "Content that applies universally..."
}
```

**ChatGPT (.chatgpt/):** Custom Instructions (plain text):
```
# My Instruction

Content that applies universally...

---
Applies to: Java files (*.java)
```

### Rule 2 — Prompts

Similar transformation logic: parse markdown frontmatter/metadata, adapt to tool-native format.

### Rule 3 — Skills

**Source:** Markdown SKILL.md file with sections (newbie/amateur/pro, commands, learning path)

**Copilot:** Kept as-is

**Claude:** Extract text sections, no direct skill equivalent → provide as markdown documentation

**Gemini:** Similar to Claude

**ChatGPT:** Flatten into GPT Instructions or Actions

### Rule 4 — MCP Tools

**Copilot:** MCP server + tool registration

**Claude:** Claude Tools API

**Others:** Unsupported or limited equivalent (custom integrations)

---

## Validation Schema

Each primitive must validate against:

1. **Format validation** — correct file structure for the tool
2. **Cross-tool consistency** — same instruction doesn't contradict itself in different tools
3. **Link validation** — all internal cross-references (`[text](path)`) are valid
4. **Metadata validation** — all required fields present (title, ID, tags)
5. **Token/size limits** — content fits within tool's limits

---

## Mapping Rules (MAPPING.md)

A reference document defining:

| From | To | Rule | Tool Support |
|---|---|---|---|
| `.common/instructions/*.md` | `.github/instructions/` | Copy as-is | Copilot ✅ |
| `.common/instructions/*.md` | `.claude/instructions/` | Convert YAML → Markdown + XML | Claude 🟡 |
| `.common/instructions/*.md` | `.gemini/instructions/` | Convert to JSON | Gemini 🟡 |
| `.common/instructions/*.md` | `.chatgpt/` | Extract text, add to Custom Instr. | ChatGPT 🟡 |
| `.common/prompts/*.md` | `.github/prompts/` | Copy + adapt applyTo | Copilot ✅ |
| `.common/prompts/*.md` | `.claude/prompts/` | Convert to markdown template | Claude 🟡 |
| ... | ... | ... | ... |

---

## Deliverables

### D1 — Architecture Design Document (`.github/docs/universal-framework-architecture.md`)

- Executive summary (1-2 pages)
- Core concepts (primitives, support levels, transformation rules)
- Directory structure (chosen option + rationale)
- Transformation rules per primitive type
- Validation schema
- Extensibility guidelines (adding a new tool)
- Implementation roadmap links (BLI-093 onwards)

### D2 — MAPPING.md (`.github/docs/tool-mapping-reference.md`)

Detailed transformation rules and examples for each primitive → tool combo.

### D3 — Adapter Pseudocode (`.github/docs/adapter-pseudocode.md`)

Pseudocode showing the logic for each tool-specific adapter (input, processing, output).
This guides implementation (BLI-097).

---

## Acceptance Criteria

- [ ] Architecture document written and reviewed (≥8 pages)
- [ ] All 5 primitive types defined with examples
- [ ] Support levels defined for each tool × primitive combo
- [ ] Directory structure chosen and justified
- [ ] Transformation rules documented for all combos
- [ ] Validation schema defined
- [ ] MAPPING.md created with ≥20 rule entries
- [ ] Adapter pseudocode written for all tools
- [ ] Design reviewed by codebase maintainers
- [ ] Design used as basis for BLI-093 (first implementation)

---

## Dependencies & Blockers

- **Blocked by:** BLI-091 (tool research must complete first)
- **Unblocks:** BLI-093, BLI-094, BLI-095, BLI-097 (all implementations depend on this design)

---

## Notes & Assumptions

- Design assumes all tools support text-based customization (not UI-only)
- Transformations are lossy in some cases (Copilot has more features than some tools)
- Design should be tool-agnostic; implementation is tool-specific
- Future tools can be added by creating a new adapter (extension point)
