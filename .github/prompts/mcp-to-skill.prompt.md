```prompt
---
name: mcp-to-skill
description: 'Analyse an MCP server tool or entire server and determine whether it can be migrated to a Copilot skill (SKILL.md). If migratable, generate the complete SKILL.md file. If not, explain why and what would need to change. Use when evaluating MCP vs skill trade-offs, planning migrations, or creating a skill from static MCP data.'
agent: copilot
tools: ['codebase', 'editFiles', 'usages']
---

## What to migrate

${input:target:MCP server name, tool name, or Java class path — e.g. "learning-resources", "search_resources tool", "src/server/learningresources/"}

## Migration mode

${input:mode:What do you want? analyse (assess feasibility only) | generate (produce SKILL.md) | full (analyse + generate):full}

## Output path (for generate mode)

${input:outputPath:Where to write the SKILL.md — e.g. .github/skills/learning-resources/SKILL.md:.github/skills/<name>/SKILL.md}

## Instructions

You are an MCP-to-skill migration specialist. Read the MCP server/tool code, evaluate
whether the capability should live in a Copilot skill, and (if requested) produce
a production-quality SKILL.md.

---

### Step 1 — Read the MCP Implementation

1. Locate and read the relevant Java source files in `mcp-servers/src/server/`
2. Identify all MCP tools exposed by the server (`handleCallTool` handler or registry)
3. For each tool, note:
   - Tool name and description
   - Input parameters
   - What the tool DOES (computation, API call, data lookup, file read?)
   - Where the data comes from (static Java constants, external API, database, file?)
   - Whether the output changes between calls with same input

---

### Step 2 — Migration Feasibility Analysis

For each tool, classify it using this decision matrix:

#### MCP Tool → Skill Decision Matrix

| Characteristic | → Skill ✅ | → Keep in MCP ❌ |
|---|---|---|
| Data source | Static / hardcoded Java | External API / live DB |
| Output variability | Same input = same output | Changes over time |
| Computation | Simple filtering/lookup | Complex scoring, ranking, aggregation |
| State | Stateless | Stateful (session, auth, CRUD) |
| Auth required | No | Yes (OAuth, API token, JWT) |
| Volume | Fits in a skill file (<500 lines) | Thousands of records |
| Update frequency | Rarely (manual update) | Continuously / real-time |
| Primary use | Teaching, reference, guidance | Operational task execution |

**Migration signals:**
- ✅ MIGRATE if: the tool returns a hardcoded list, reference table, or guide docs
- ✅ MIGRATE if: the MCP tool only does filtering of a small static set (<100 items)
- ✅ MIGRATE if: the content is educational/reference and doesn't change week to week
- ❌ KEEP MCP if: the tool calls an external API (Atlassian, GitHub, etc.)
- ❌ KEEP MCP if: the tool does non-trivial computation (BM25 search, scoring, ranking)
- ❌ KEEP MCP if: the tool reads/writes live data or requires authentication
- ❌ KEEP MCP if: the tool uses real-time data that would be stale in a skill

**Partial migration:** Some tools can PARTIALLY migrate — static reference content
becomes a skill section, while the dynamic search/filter capability stays in MCP.

---

### Step 3 — Produce the Analysis Report

```markdown

## MCP → Skill Analysis: <server/tool name>

### Tool Inventory

| Tool | Type | Data source | Migratable? | Reason |
|---|---|---|---|---|
| `search_resources` | Search | Static Java vault | ⚠️ Partial | Static data can be in skill; BM25 scoring must stay in MCP |
| `get_server_info` | Metadata | Hardcoded | ✅ Yes | Pure static info |
| `list_resources` | Listing | Static vault | ✅ Yes | Static curation |

### Recommendation

- **Migrate to skill:** <list of tools>
- **Keep in MCP:** <list of tools>
- **Partial migration:** <notes>

### Estimated effort: <Low / Medium / High>

```yaml

---

### Step 4 — Generate SKILL.md (if mode = generate or full)

Using the migratable content, produce a SKILL.md at `${input:outputPath}`:

```yaml

---
name: <derived from server/tool name>
description: >
  <One paragraph precisely describing WHEN Copilot should activate this skill.
   Include specific trigger words: what the user might ask about, what domain it covers,
   key concepts it teaches. This is how Copilot routes queries to this skill.
   Be specific — "Use when asked about X, Y, Z" patterns work best.>
---

```text

Content structure:
```

# <Skill Title>

## Tier 1 — Newbie: <Quick reference, essential commands/facts>

## Tier 2 — Amateur: <Deeper reference, common patterns, comparison tables>

## Tier 3 — Pro: <Advanced usage, edge cases, composition patterns>

## Quick Reference Cheatsheet

## Learning Resources

```text

**Translation patterns:**

| MCP tool capability | Skill equivalent |
|---|---|
| `list_resources(category)` | H2 section per category with bullet-list resources |
| `get_server_info()` | Metadata block at top of skill |
| `get_resource(id)` | Direct link in relevant section |
| `search_resources(query)` | Cannot fully migrate — Copilot's semantic matching partially replaces |
| Static configuration map | Table in skill |
| Enum values list | Table: value, description, when to use |

---

### Step 5 — Register the new skill (if generated)

After generating the SKILL.md, remind the user to:

1. **Add to `copilot-instructions.md`** skills block:
   ```

   <skill>
   <name><skill-name></name>
   <description>...</description>
   <file>e:\path\to\.github\skills\<name>\SKILL.md</file>
   </skill>

   ```text

2. **Update `.github/docs/slash-commands.md`** if a slash command wraps this skill

3. **Test activation** — ask Copilot a question that should trigger the skill; verify
   it loads the skill content

4. **Consider keeping MCP alive** if any tools were NOT migrated — partial migration
   is valid

---

### Important

- Never delete the MCP server code until you've verified the skill covers the use cases
- Skills are NOT a replacement for MCP when live data, auth, or computation is involved
- The `description` field in the skill frontmatter is CRITICAL — it determines when
  Copilot activates the skill. Test it by asking Copilot questions and checking if the
  skill surfaces
- Run `/check-standards skill-file` on the generated SKILL.md to verify it meets standards
```
