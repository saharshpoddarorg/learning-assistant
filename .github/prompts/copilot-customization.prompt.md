```prompt
---
name: copilot-customization
description: 'Create, review, compare, or compose GitHub Copilot customization files — instructions, prompts, agents, skills, or MCP server configs'
agent: copilot
tools: ['codebase', 'editFiles', 'search']
---

## What do you want to do?
${input:goal:What's your goal? (create-new / review-existing / compare-types / plan-composition / explain-concept / audit-repo)}

## Which type? (if create-new or review-existing)
${input:type:Which customization type? (copilot-instructions / instructions / prompt / agent / skill / mcp / all-types / not-sure)}

## Domain or topic
${input:domain:What domain or topic is this for? (e.g. java, security, devops, mcp, git, or a custom domain)}

## Depth level
${input:level:What depth? (newbie / amateur / pro)}

---

## Instructions

Help the user with GitHub Copilot customization. Adapt the response based on `goal`, `type`, `domain`, and `level`.

Use the Copilot Customization skill for reference material on all types, frontmatter, and composition patterns.

---

### When `goal` is `explain-concept` or `type` is `not-sure` or `all-types`:

Teach the 6 Copilot customization primitives in a structured way. Adapt to `level`.

#### 🟢 Newbie: The 6 Things You Can Do

```

1. copilot-instructions.md  → Project-wide rules. Always active. One file.
2. .instructions.md         → File-type rules. Auto-active when file matches.
3. .prompt.md               → Slash commands (/my-cmd). On-demand workflows.
4. .agent.md                → AI personas. Manual dropdown selection.
5. SKILL.md                 → Domain knowledge. Auto-loads for relevant questions.
6. MCP Server               → External tools. GitHub, Jira, databases, APIs.

```text

**The 1-sentence rule:**
- **Instructions** = rules Copilot MUST follow
- **Prompts** = workflows you trigger with `/cmd`
- **Agents** = expert personas you select from dropdown
- **Skills** = reference knowledge Copilot knows
- **MCP servers** = external systems Copilot can interact with

#### 🟡 Amateur: Comparison Table

| Type | File location | Activation | What it changes |
|---|---|---|---|
| `copilot-instructions.md` | `.github/` | Always on | How Copilot writes everything |
| `.instructions.md` | `.github/instructions/` | Auto (glob match) | How Copilot writes THAT file type |
| `.prompt.md` | `.github/prompts/` | Manual `/cmd` | What Copilot does for THAT task |
| `.agent.md` | `.github/agents/` | Manual dropdown | Who Copilot becomes |
| `SKILL.md` | `.github/skills/<domain>/` | Auto (semantic) | What Copilot knows about a domain |
| MCP Server | `.vscode/mcp.json` + code | Auto (agent mode) | What Copilot can DO externally |

**Decision guide:**
- Need a RULE that always applies? → `copilot-instructions.md` or `.instructions.md`
- Need a WORKFLOW you trigger? → `.prompt.md`
- Need an EXPERT PERSONA? → `.agent.md`
- Need DOMAIN KNOWLEDGE? → `SKILL.md`
- Need to CALL EXTERNAL APIs? → MCP Server

#### 🔴 Pro: Priority, Stacking, and Composition

```

Priority (highest wins on conflict):
  1 — Your message      (always wins)
  2 — .prompt.md        (when /cmd invoked)
  3 — .agent.md         (when agent selected)
  4 — SKILL.md          (when topic matches)
  5 — .instructions.md  (when file matches applyTo)
  6 — copilot-instructions.md (always, base layer)

Stacking:
  copilot-instructions     → never stacks (one file)
  .instructions.md         → stacks (multiple files, same applyTo = all apply)
  SKILL.md                 → stacks (multiple can activate simultaneously)
  MCP servers              → stack (all registered servers active)
  .agent.md                → does NOT stack (one at a time)
  .prompt.md               → does NOT stack (one at a time)

Cross-type composition (most powerful):
  Instructions + Skill        → always-on rules + always-on knowledge
  Agent + Prompt              → "who" + "what workflow"
  Agent + MCP                 → expert persona + live external data
  Instructions + Agent + MCP  → rules + persona + real tools (full power)

```text

See `.github/docs/copilot-customization-deep-dive.md` for the complete composition pattern library.

---

### When `goal` is `create-new`:

Scaffold the complete file for the requested `type`. Ask clarifying questions if `domain` is vague.
Always produce a **complete, ready-to-use file** — not a template with placeholders.

#### Creating `copilot-instructions.md`

Generate the project base instruction file. Structure as:

```markdown

# Project Instructions — [PROJECT NAME]

## Overview

- Language: [language + version]
- Build: [build tool]
- Purpose: [one sentence]

## Naming Conventions

- Classes: UpperCamelCase
- Methods: lowerCamelCase
- Constants: UPPER_SNAKE_CASE
- Packages: all lowercase

## Code Style

- [Specific rules for the language]
- [Line length, brace style, etc.]

## Methods

- [Size limits, single responsibility, etc.]

## Error Handling

- [Specific exception strategy]

## Do's and Don'ts

### Do:

- [Rule 1]

### Don't:

- [Rule 1]

## Commit Guidelines

[Conventional commits format or team convention]

```text

**Rules for writing good copilot-instructions.md:**
- Keep under 2 000 tokens — it's sent on EVERY request
- Only include rules that apply EVERYWHERE (not file-type-specific rules)
- Write as imperatives: "Use X", "Never Y" — not suggestions
- No large reference tables (those go in SKILL.md)

---

#### Creating `.instructions.md`

Generate a scoped instruction file. Always include the `applyTo` glob.

```markdown

---
applyTo: "[GLOB PATTERN]"
---

# [Domain] Instructions

## [Category 1]

- [Specific, actionable rule 1]
- [Specific, actionable rule 2]

## [Category 2]

- [Rule]

```text

**Glob pattern guide:**
| Pattern | Matches |
|---|---|
| `**/*.java` | All Java source files |
| `**/*Test.java` | Java test files only |
| `src/**/*.ts` | TypeScript under src/ |
| `**/*.{md,mdx}` | Markdown files |
| `**/pom.xml` | Maven POM files |
| `**` | Every file (steering mode) |

**Size limit:** Each instruction file should be <500 tokens. Split concerns across multiple files.

---

#### Creating `.prompt.md`

Generate a complete slash command prompt. Identify 3+ input variables for the domain.

```markdown

---
name: [command-name]
description: '[Brief description — shown in Copilot Chat autocomplete]'
agent: [optional-agent-name]
tools: ['codebase', 'search', 'usages']
---

## [Primary Input]

${input:topic:What topic? ([option1] / [option2] / [option3])}

## [Secondary Input]

${input:goal:What's your goal? ([option1] / [option2])}

## [Level]

${input:level:Your level? (beginner / intermediate / advanced)}

## Instructions

[Main instructions for Copilot — what to do for each topic/goal combination]

### When `topic` is `[option1]`:

[Instructions and structure for this topic]

### When `topic` is `[option2]`:

[Instructions and structure for this topic]

### Rules

- Always provide working code examples
- Adapt depth to `level`
- [Domain-specific rule]

```text

**Key prompt design principles:**
1. `${input:}` variables — collect the minimum necessary context upfront
2. `### When X is Y:` sections — cover each combination of inputs
3. `### Rules` — non-negotiable response requirements at the end
4. Reference skills with `#file:` or by mentioning the domain (skill auto-activates)
5. If the prompt is domain-specific, a corresponding SKILL.md should exist

---

#### Creating `.agent.md`

Generate a complete agent persona file. Think about: mindset, vocabulary, tool access, what it never does.

```markdown

---
description: >
  [WHO THIS AGENT IS — one line persona].
  Use when: [use case 1], [use case 2], or [use case 3].
tools:
  - codebase
  - search
  - usages
  [add editFiles only if the agent needs to edit]
  [add runCommands only if the agent needs terminal]
model: gpt-4o   [optional — remove line to use default]
---

You are a [ROLE]. Your expertise is [DOMAIN], [SUBDOMAIN], and [ADJACENT KNOWLEDGE].

## How You Think

- [Primary thinking approach — e.g., "Always look for root causes before suggesting fixes"]
- [Communication style — e.g., "Be concise and cite evidence from the codebase"]
- [What you prioritize — e.g., "Correctness over conciseness"]

## Your Workflow

When the user asks you to [PRIMARY TASK]:
1. [Step 1 — what you do first]
2. [Step 2]
3. [Step 3 — final output format]

## What You Never Do

- Never [rule 1]
- Never [rule 2]
- Never make changes without confirming with the user first (if applicable)

```text

**Available tools (use only what the agent needs):**
```

codebase      — Read project files (non-editable view)
search        — Search workspace
usages        — Find symbol usages
editFiles     — Create and edit files ← add only if agent must write
runCommands   — Run terminal commands ← add only if agent must execute
problems      — Read VS Code diagnostics
fetch         — Fetch URLs and web content
findTestFiles — Find test files for a source file (2025+)
terminalLastCommand  — Read last terminal output (2025+)
terminalSelection    — Read selected text in terminal (2025+)
testFailure          — Read test failure details (2025+)

```yaml

---

#### Creating `SKILL.md`

Generate a complete skill file. The description field is the most important part — write it carefully.

```markdown

---
name: [domain-name]
description: >
  [THIS IS THE ACTIVATION TRIGGER — the most important field.]
  [Start with "Comprehensive guide to X" or "Use when asked about X, Y, Z".]
  Use when asked about [TERM 1], [TERM 2], [USE CASE 1], [USE CASE 2].
  Covers: [subtopic A], [subtopic B], [subtopic C].
---

# [Domain] — Skill Reference

---

## 🟢 Quick Reference

[Cheatsheet — most common commands, patterns, facts]
[Should answer 80% of questions without reading further]

---

## 🟡 [Topic Area 1]

[Full explanation, patterns, examples]

---

## 🟡 [Topic Area 2]

[Full explanation]

---

## 🔴 Advanced: [Pro Topic]

[Deep dives, edge cases, performance, architecture]

---

## Learning Resources

| Resource | What It Covers |
|---|---|
| [Official docs](...) | ... |
| [Tutorial](...) | ... |

```text

**Description field formula:**
```

"Comprehensive guide to [DOMAIN X].
 Covers [SUBTOPIC A], [SUBTOPIC B], [SUBTOPIC C].
 Use when asked about [TERM 1], [TERM 2], [USE CASE 1], or [USE CASE 2].
 Also useful for [ADJACENT USE CASE]."

```yaml

---

#### Creating MCP Server Config + Server

Generate the `.vscode/mcp.json` entry AND the minimal server scaffold.

**Config entry (`.vscode/mcp.json`):**
```jsonc

{
  "inputs": [
    {
      "id": "[secretId]",
      "type": "promptString",
      "description": "[What is this secret?]",
      "password": true
    }
  ],
  "servers": {
    "[server-name]": {
      "type": "stdio",
      "command": "node",
      "args": ["${workspaceFolder}/mcp-servers/[server-name]/dist/index.js"],
      "env": {
        "[API_KEY]": "${input:[secretId]}"
      }
    }
  }
}

```text

**TypeScript server scaffold:**
```typescript

import { McpServer } from "@modelcontextprotocol/sdk/server/mcp.js";
import { StdioServerTransport } from "@modelcontextprotocol/sdk/server/stdio.js";
import { z } from "zod";

const server = new McpServer({
  name: "[server-name]",
  version: "1.0.0",
  description: "[What this server gives Copilot access to]"
});

server.tool(
  "[tool_name]",
  "[What this tool does — AI uses this to decide when to call it]",
  {
    [param]: z.string().describe("[What this parameter is]")
  },
  {
    readOnlyHint: true,   // true if no side effects
    idempotentHint: true  // true if calling twice = calling once
  },
  async ({ [param] }) => {
    // Implementation
    return {
      content: [{ type: "text", text: JSON.stringify(result, null, 2) }]
    };
  }
);

const transport = new StdioServerTransport();
await server.connect(transport);

```yaml

---

### When `goal` is `review-existing`:

Review the existing customization file the user provides. Check:

**For `.instructions.md`:**
- [ ] Is `applyTo` glob correct? (test at globtester.com)
- [ ] Are rules behavioral (not informational)?
- [ ] Is it under 500 tokens?
- [ ] No duplication with `copilot-instructions.md`?

**For `.agent.md`:**
- [ ] Is `description` clear about WHEN to use this agent?
- [ ] Is `tools:` list explicitly set (no accidental all-tools)?
- [ ] Body reads as a direct system prompt (not a user-facing guide)?
- [ ] Persona is consistent throughout?

**For `.prompt.md`:**
- [ ] Do `${input:}` variables collect all needed context?
- [ ] Does each input have clear options? (users don't know what to type)
- [ ] Does the body have `### When X is Y:` sections for each input combo?
- [ ] Is there a `### Rules` section with non-negotiable requirements?
- [ ] Is `name:` matching the `/command-name` the user will type?

**For `SKILL.md`:**
- [ ] Is `description` specific enough to activate for relevant questions?
- [ ] Does it follow 3-tier structure (quick reference → detail → advanced)?
- [ ] Is it informational (not behavioral directives — those go in instructions)?
- [ ] Is it focused on one domain (not a catch-all)?

**For `copilot-instructions.md`:**
- [ ] Is it under 2 000 tokens?
- [ ] Does it contain only truly universal rules?
- [ ] No large tables or reference blocks (those go in SKILL.md)?
- [ ] Written as imperatives, not suggestions?

---

### When `goal` is `compare-types`:

Show the comparison table based on what types the user wants to compare. Use the full
comparison matrix from the `copilot-customization` skill.

Key dimensions to always include:
- Activation (automatic vs manual)
- Scope (project / file-type / topic / session)
- Content type (behavioral / informational / workflow / persona / tool)
- Stacks (yes/no)
- Size guidance
- Best for (top 3 use cases)

---

### When `goal` is `plan-composition`:

Help the user plan a composition of multiple customization types for their use case.

Ask clarifying questions first:
1. What domain/topic is this for?
2. Who will use this? (team, solo, onboarding)
3. What's the trigger? (always needed, specific workflow, ad-hoc)
4. Do you need external data access?

Then recommend a composition recipe from:

```

Tier 1 — Simple (1-2 types):
  ├── Rules always enforced                → copilot-instructions.md
  ├── File-type specific rules             → .instructions.md
  ├── Domain knowledge always available    → SKILL.md
  └── Repeatable slash command             → .prompt.md

Tier 2 — Medium (2-3 types):
  ├── Expert always-on enrichment          → .instructions.md + SKILL.md
  ├── Expert workflow on demand            → .agent.md + .prompt.md
  └── Workflow with domain depth           → .prompt.md + SKILL.md

Tier 3 — Full Power (4+ types):
  ├── Domain mastery                       → copilot-instructions + .instructions + SKILL + .prompt
  ├── Expert + live data                   → .agent + MCP server
  └── Everything                           → all 6 types active

```yaml

Output: A concrete file list the user needs to create, with one-line description of each.

---

### When `goal` is `audit-repo`:

Search the `.github/` directory for the current customization setup. Then:

1. **List what exists** (all instructions, prompts, agents, skills)
2. **Identify gaps** — what domains are missing skills or instructions?
3. **Identify redundancy** — same rules in multiple places?
4. **Check SKILL.md descriptions** — are they specific enough to activate?
5. **Check agent tool lists** — any agents with all-tools (missing explicit restriction)?
6. **Check composition** — are instructions + skills aligned?
7. **Output: Prioritized action plan** — what to create/fix first

Use `codebase` and `search` tools to discover the actual state.

---

### Composition Examples for Specific Domains

When `domain` is **java** or **backend**:
```

copilot-instructions.md            → Java version, naming, methods, commits
java.instructions.md               → Java-specific rules (var, final, @Override)
clean-code.instructions.md         → Code quality rules
java-learning-resources/SKILL.md   → Java API reference, patterns, resources
learning-mentor.agent.md           → Teaching persona for Java learning sessions
/learn-concept prompt              → Java concept deep dives

```text

When `domain` is **devops** or **ci-cd**:
```

copilot-instructions.md            → Commit format, branch naming
devops.instructions.md             → IaC rules, Docker best practices, k8s conventions
software-engineering-resources/SKILL.md → DevOps resources
/devops prompt                     → DevOps learning and reference
docker-mcp-server                  → Live container management
kubernetes-mcp-server              → Live cluster operations

```text

When `domain` is **security**:
```

security.instructions.md (applyTo: **) → OWASP rules, input validation, no hardcoded secrets
security-reviewer.agent.md             → Threat modeling mindset, OWASP vocabulary
/security-review prompt                → Structured audit workflow
security-mcp-server (optional)         → SAST tool integration

```text

When `domain` is **mcp** or **ai-tools**:
```

mcp-development/SKILL.md           → Full MCP protocol reference
/mcp prompt                        → Learn and build MCP servers
learning-mentor.agent.md           → Teaching persona for explanations
github-mcp-server                  → GitHub integration
.vscode/mcp.json                   → Register all your MCP servers

```yaml

---

### Rules
- Always scaffold COMPLETE, READY-TO-USE files — no placeholder text like "[INSERT RULE]"
- Always include the correct frontmatter for the type being created
- Always check for common anti-patterns (bloated instructions, vague skill descriptions, agents without tool lists)
- For `create-new`: output the actual file content in a code block, then explain what to do with it
- For `review-existing`: be specific about what to improve and why (not just "it could be better")
- For `compare-types`: always show examples, not just theory
- For `plan-composition`: output a concrete file list, not just abstract advice
- Always reference `copilot-customization-deep-dive.md` for the full composition pattern library
```
