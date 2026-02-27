---
applyTo: "**"
---

# Change Completeness — The Iterative Repo Checklist

> **Purpose:** Ensure every feature addition, enhancement, or change to this repo is
> comprehensive and iterative — built on top of existing patterns, leaving the repo in
> a working, consistent state after every commit.
>
> **When to apply:** Whenever you add new content, a new feature, a new prompt, a new
> resource provider, a change to existing behaviour, or any cross-cutting enhancement.

---

## The Golden Rule

> **Never add something in isolation.** Every change has a ripple.
> Follow all the checklist sections that apply, then build and verify before committing.

---

## Checklist by Change Type

### A — Adding New Learning Resources (vault providers)

When adding or expanding a `ResourceProvider` class (e.g., `VcsResources.java`, `BuildToolsResources.java`):

- [ ] **New provider class** in `mcp-servers/src/server/learningresources/vault/providers/`
  - Implements `ResourceProvider`, returns `List<LearningResource>`
  - Each resource: all required fields (id, title, url, description, type, categories,
    conceptAreas, tags, author, difficulty, freshness, isOfficial, isFree, languageApplicability, addedAt)
  - IDs are unique, lowercase, hyphenated
  - No duplicate URLs with existing providers (check `VcsResources.java`, `BuildToolsResources.java`, `DevOpsResources.java`, `JavaResources.java`, etc.)
- [ ] **Register in `BuiltInResources.java`** — add `new YourProvider()` to `PROVIDERS` and add `import`
- [ ] **Enum check** — verify `ConceptArea` and `ResourceCategory` have the values you need;
  if not, add them (and update all enum-related tests/indexes)
  - **Semantic correctness is mandatory** — read each enum value's Javadoc before assigning it;
    do NOT choose an enum simply because it sounds close. If no existing value fits the concept,
    create a new one rather than misusing an existing one.
  - Example anti-patterns: using `LLM_AND_PROMPTING` for a series about neural-network math;
    using `ALGORITHMS` for linear algebra / mathematical foundations.
- [ ] **`KeywordIndex.java`** — add keyword → ConceptArea and keyword → ResourceCategory mappings
  for every new concept or technology introduced
- [ ] **Skill file** — create or update `.github/skills/<domain>/SKILL.md` with:
  - Cheatsheet of key commands / patterns (3-tier: newbie / amateur / pro)
  - Curated resource links matching what was added to the vault
- [ ] **Prompt file** — create or update `.github/prompts/<domain>.prompt.md` with:
  - Input variables: topic, goal, level
  - Domain map showing concept hierarchy
  - 3-tier response structure
  - Quick reference table (commands, patterns, etc.)
  - Curated resources section linking to vault entries
- [ ] **`slash-commands.md`** — add new commands to:
  - Quick Lookup table (increment count, add row)
  - Domain-Specific Learning section (add full entry)
  - Update vault resource count in `/resources` entry
- [ ] **`copilot-instructions.md`** (if the skill changes Copilot's behavior globally)
- [ ] **Build and verify** — `.\mcp-servers\build.ps1` must succeed with 0 errors

---

### B — Adding a New Slash Command / Prompt

When creating `.github/prompts/<name>.prompt.md`:

- [ ] **Prompt file** with complete YAML frontmatter (`name`, `description`, `agent`, `tools`)
- [ ] **Input variables** — at least `topic`, `goal`/`mode`, `level`
- [ ] **Domain map** — ASCII tree or table showing concept coverage
- [ ] **3-tier structure** — newbie / amateur / pro response templates
- [ ] **Curated resources** — link to relevant vault entries
- [ ] **`slash-commands.md`** — add to Quick Lookup table + Command Details section
- [ ] **`hub.prompt.md`** — add to the category tree if it's a new domain
- [ ] **TOC in `slash-commands.md`** — add anchor if a new category heading was added
- [ ] **Build and verify** — `.\mcp-servers\build.ps1` must succeed with 0 errors

---

### C — Adding a New Skill File

When creating `.github/skills/<name>/SKILL.md`:

- [ ] **Register in copilot-instructions.md** `<skills>` block (if not auto-discovered)
- [ ] **`description` field** in YAML frontmatter — clearly states WHEN to activate the skill
- [ ] **3-tier content** — newbie / amateur / pro sections with appropriate depth
- [ ] **Command cheatsheet** — copy-pasteable, tested commands
- [ ] **Learning path** — link to vault resources per tier
- [ ] **Build and verify** — `.\mcp-servers\build.ps1` must succeed with 0 errors

---

### D — Adding a New MCP Tool or Handler

When adding an MCP tool handler (e.g., a new `handle_*` case):

- [ ] **Handler class/method** implementing the tool
- [ ] **Tool registration** in `McpServerRegistry` or the server's `handleCallTool`
- [ ] **Prompt update** — update the relevant `.prompt.md` to mention the new tool
- [ ] **`slash-commands.md`** — update `/resources` (or relevant command) MCP tools list
- [ ] **`mcp-servers/README.md`** — update tools table
- [ ] **`USAGE.md`** — add or update relevant section
- [ ] **Build and verify** — `.\mcp-servers\build.ps1` must succeed with 0 errors

---

### E — Adding a New Enum Value

When adding to `ConceptArea`, `ResourceCategory`, `DifficultyLevel`, etc.:

- [ ] **Enum value added** with correct domain assignment
- [ ] **`KeywordIndex.java`** — add at least 3 synonymous keyword mappings
- [ ] **At least one resource** using the new value so it's exercised
- [ ] **All existing providers** — check if any resources should retroactively use the new value
- [ ] **Build and verify** — `.\mcp-servers\build.ps1` must succeed with 0 errors

---

### F — Any Change — Universal Checklist

Run this for EVERY category of change:

- [ ] **Build passes** — `.\mcp-servers\build.ps1` — 0 compile errors, 0 warnings (where possible)
- [ ] **No regression in existing behaviour** — manually verify existing slash commands still work
- [ ] **Commit message follows Conventional Commits** (`feat:`, `fix:`, `docs:`, `chore:`)
- [ ] **Commit attribution** — append `— created by gpt` or `— assisted by gpt` as applicable
- [ ] **Single logical commit** — don't mix unrelated changes

---

## 3-Tier Completeness Guide

### Newbie — "I added a thing, and it works"
- New provider/prompt/skill created
- Registered in BuiltInResources / slash-commands
- Build passes

### Amateur — "I added a thing, it works, and it's discoverable"
- All of the above
- KeywordIndex updated (discoverability via search)
- Slash command entry added with example usage
- Skill file has the cheatsheet

### Pro — "I added a thing that is comprehensive, connected, and iterative"
- All of the above
- 3-tier content in skill and prompt files
- Vault resource count updated in documentation
- Cross-references between prompt ↔ skill ↔ vault ↔ keyword index
- No orphan resources (all accessible via at least one discovery path)
- Documentation reflects actual capabilities (no false advertising in slash-commands.md)
- Commit is a clean, standalone logical unit

---

## File Inventory — What to Check for Any Domain Addition

| File | Action when adding domain content |
|---|---|
| `mcp-servers/src/.../vault/providers/<Domain>Resources.java` | CREATE — the provider class |
| `mcp-servers/src/.../vault/BuiltInResources.java` | UPDATE — register provider |
| `mcp-servers/src/.../vault/KeywordIndex.java` | UPDATE — add keyword mappings |
| `mcp-servers/src/.../model/ConceptArea.java` | UPDATE if new concept needed |
| `mcp-servers/src/.../model/ResourceCategory.java` | UPDATE if new category needed |
| `.github/skills/<domain>/SKILL.md` | CREATE or UPDATE |
| `.github/skills/java-build/SKILL.md` | UPDATE if build tools or JDK changed |
| `.github/skills/digital-notetaking/SKILL.md` | UPDATE if PKM/note-taking content changed |
| `.github/prompts/<domain>.prompt.md` | CREATE — slash command |
| `.github/prompts/hub.prompt.md` | UPDATE — category tree |
| `.github/docs/slash-commands.md` | UPDATE — table + details |
| `mcp-servers/README.md` | UPDATE — resource count, features |
| `USAGE.md` | UPDATE — relevant section |
| `README.md` (root) | UPDATE if project-level docs changed |
| `copilot-instructions.md` | UPDATE — skills block, conventions |

---

## Build Verification Commands

```powershell
# Full build (Windows PowerShell)
.\mcp-servers\build.ps1

# Clean build (from scratch)
.\mcp-servers\build.ps1 -Clean

# Verify key classes compiled (examples — adjust to your new provider)
Test-Path mcp-servers\out\server\learningresources\vault\providers\VcsResources.class
Test-Path mcp-servers\out\server\learningresources\vault\providers\BuildToolsResources.class
```

If the build fails:
1. Read the compiler error message — it tells you the exact file and line
2. Fix the error (missing `)`, wrong type, missing import, etc.)
3. Re-run the build until it passes
4. Do NOT commit with a broken build
