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
    conceptAreas, tags, author, difficulty, freshness, isOfficial, isFree, languageApplicability,
    addedAt, contentFormat, resourceAuthors)
  - IDs are unique, lowercase, hyphenated
  - No duplicate URLs with existing providers (check `VcsResources.java`, `BuildToolsResources.java`, `DevOpsResources.java`, `JavaResources.java`, etc.)
  - **ContentFormat is set correctly** — book-type resources must use `PUBLISHED_BOOK` or
    `OPEN_BOOK`; web-native resources default to `WEB_RESOURCE`
  - **ResourceAuthor is set when applicable** — books always set `resourceAuthors`
    (use `Set.of(ResourceAuthor.X)` or `Set.of(A, B)` for multi-author works); blogs
    and video series by notable authors should set it; official docs and community content
    typically use `Set.of()` (empty)
- [ ] **Web research for accuracy** — before adding or updating learning resources, verify
  metadata by fetching the source (GitHub repo pages, documentation sites, etc.):
  - Star counts, descriptions, and author names must reflect current reality
  - URLs must be valid and reachable (no 404s, no redirects to unrelated pages)
  - When adding GitHub repos, fetch the repo page to confirm stars, description, and activity
  - When in doubt about a resource's quality or accuracy, research it — don't guess
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

- [ ] **Industry standards** — verify the approach aligns with established conventions,
  official documentation, and community best practices before implementing. Consult the
  Standards Quick-Reference table in `copilot-instructions.md` for authoritative sources
  per domain. When in doubt, research the standard — don't guess.
- [ ] **Markdown formatting** — run `.\__md_lint.ps1` from repo root; must exit with **0 issues**
  (see Section G below and `.github/instructions/md-formatting.instructions.md` for full rules)
- [ ] **Build passes** — `.\mcp-servers\build.ps1` — 0 compile errors, 0 warnings (where possible)
- [ ] **No regression in existing behaviour** — manually verify existing slash commands still work
- [ ] **Cross-references complete** — verify all related docs link to each other
  (see Section H below for the documentation cross-reference checklist)
- [ ] **Commit message follows Conventional Commits** (`feat:`, `fix:`, `docs:`, `chore:`)
- [ ] **Commit attribution** — append `— created by gpt` or `— assisted by gpt` as applicable
- [ ] **Single logical commit** — don't mix unrelated changes

---

### G — Pre-Commit Formatting Gates (applies to ALL changes)

Before committing ANY change that touches `.md` files:

#### Auto-fixed by `.\.\__md_lint.ps1` (must be run first)

- [ ] **Trailing whitespace** — no spaces at end of lines
- [ ] **Blank line BEFORE every heading** — `## Heading` must be preceded by a blank line
- [ ] **Blank line AFTER every heading** — `## Heading` must be followed by a blank line
- [ ] **Blank line BEFORE opening code fence** — ` ``` ` preceded by blank line
- [ ] **Blank line AFTER closing code fence** — ` ``` ` followed by blank line
- [ ] **3+ consecutive blank lines** — collapsed to 1
- [ ] **End of file** — exactly one `\n` after last content line

#### Requires human review (reported by `__md_lint.ps1 -Check`, not auto-fixed)

- [ ] **Heading hierarchy** — no level skips (H1→H2→H3, never H1→H3)
- [ ] **Single H1 per file** — every file has exactly one `# Title`
- [ ] **Code fence language tags** — every ` ``` ` has a lang tag (` ```java `, ` ```bash `, etc.)
- [ ] **List marker consistency** — use only `-` or only `*` in each list block
- [ ] **Ordered list numbering** — `1. 2. 3.` not `1. 1. 1.`
- [ ] **Tables** — header row + separator row (`|---|`); each row starts and ends with `|`
- [ ] **Link format** — `[text](url)` with NO space between `]` and `(`
- [ ] **Graphic/box representations** — tables drawn with `|` must have correct pipe spacing;
  ASCII diagrams in code blocks must use the correct fence and lang tag (`text`)

---

### H — Documentation Cross-References (applies to doc/skill/guide changes)

When adding or modifying documentation, skills, or guide files in `.github/docs/` or `.github/skills/`:

- [ ] **START-HERE.md** — verify the newbie/amateur/pro reading paths include the new content
  where appropriate; update the Documentation Map table if a new doc file was created
- [ ] **Navigation links** — new docs must have a footer navigation linking back to START-HERE
  and forward to at least one related doc
- [ ] **Cross-references from existing docs** — if the new content relates to an existing doc,
  add a link from that doc's "Further Reading" or "What's Next?" table to the new content
- [ ] **SKILL.md description** — if a skill's coverage area expanded, update its `description`
  field in YAML frontmatter so it activates on the new keywords
- [ ] **getting-started.md** — if a new primitive or feature was added, update the "What's Next?"
  table to point to the relevant detailed doc
- [ ] **customization-guide.md** — if a new customization primitive or pattern was documented,
  update the Further Reading table to include it
- [ ] **copilot-customization-deep-dive.md** — if the change adds new migration paths, official
  resources, or FAQ entries, update the relevant Part and TOC
- [ ] **No orphan docs** — every `.md` file in `.github/docs/` must be reachable from at least
  one other file (either START-HERE, navigation-index, or a related doc's links)
- [ ] **Official resources** — if a change references new external tools or standards, add
  the official documentation link to the relevant "Resources" or "Further Reading" section
- [ ] **export-guide.md** — if a new config file, credential, or environment variable was added,
  update the Config Files and API Keys Reference tables in export-guide.md
- [ ] **export-newbie-guide.md** — if a new exportable feature was added, verify the newbie
  export guide covers how to copy it and whether it's safe to skip

---

### I — Todo & Task Tracking (applies to ALL multi-step work)

When performing any multi-step work, follow these task tracking rules rigorously:

- [ ] **Create todos at the start** — break work into specific, actionable items before starting
- [ ] **One in-progress at a time** — never have more than one todo marked `in-progress`
- [ ] **Mark completed immediately** — mark each todo as `completed` the moment it finishes;
  never batch completions
- [ ] **New request during in-progress work** — if the user gives additional instructions while
  earlier work is still running, create new todos for the new requests and append them to the
  existing todo list (do NOT replace or rewrite existing todos)
- [ ] **Sub-agent work** — when delegating to a sub-agent, mark the parent todo as in-progress,
  and when the sub-agent completes, mark the parent todo as completed
- [ ] **Final state verification** — before committing, verify every todo is either `completed`
  or explicitly cancelled (none left as `not-started` or `in-progress`)
- [ ] **Comprehensive coverage** — create additional todos whenever new sub-tasks are discovered
  during work; do not skip discovered tasks just because they were not in the original plan

---

### J — Commit Message & Pull Request Standards (applies to ALL commits)

Every commit and push must follow these rules:

#### Commit Messages

- [ ] **Conventional Commits format** — `type(scope): subject` (see `copilot-instructions.md`)
- [ ] **Subject is accurate** — the subject line describes ALL significant changes in the commit,
  not just the first or most obvious change
- [ ] **Body lists all changes** — the commit body must enumerate all files and categories of
  changes made (use bullet points for 3+ files changed)
- [ ] **Descriptive, not generic** — never use vague subjects like "update files", "fix stuff",
  "various changes"; be specific about what was added/changed/fixed
- [ ] **Attribution footer** — include `— created by gpt` or `— assisted by gpt` as the last line
- [ ] **Single logical unit** — one commit = one logical change; do not mix unrelated changes

#### Pull Request Suggestions

- [ ] **Suggest PR title after push** — when pushing to a feature branch (e.g., `saharsh1`),
  always suggest an appropriate PR title and description for merging into the default branch
- [ ] **Re-check full commit list** — before suggesting a PR title, run
  `git log origin/master..HEAD --oneline` (or equivalent for the default branch) to enumerate
  ALL commits that will be part of the PR — never base the suggestion on only the latest commit
- [ ] **PR title format** — use the same Conventional Commits format: `type(scope): description`;
  when the PR spans multiple types, use the dominant type or `feat` and summarise in the subject
- [ ] **PR description** — include:
  - Summary of ALL changes across every commit (1-3 sentences)
  - Bullet list of key modifications grouped by commit or area
  - Total files changed count (use `git diff --stat origin/master..HEAD`)
  - Any breaking changes or migration notes
- [ ] **Branch context** — mention the source and target branches (e.g., `saharsh1 → master`)

---

### K — Comprehensive Repo Maintenance (applies to ALL changes)

Every change must maintain the repo's overall consistency and completeness:

#### Cross-References & Links

- [ ] **Cross-references current** — when adding or editing docs, verify all related documents
  link to each other; never leave a new doc unreachable from existing navigation
- [ ] **Numbers and counts accurate** — when adding resources, update all resource counts
  mentioned in documentation (README, slash-commands, USAGE.md, etc.)
- [ ] **Links functional** — every internal link added must point to an existing file; do not
  link to files that do not exist

#### Newbie & 3-Tier Content

- [ ] **Newbie files updated** — when adding features, ensure newbie-specific docs
  (START-HERE.md, getting-started.md, export-newbie-guide.md, copilot-customization-newbie.md)
  include or reference the new content at the appropriate level
- [ ] **3-tier consistency** — skill files and prompt files must maintain newbie/amateur/pro
  sections; new content must be added at the appropriate tier(s)
- [ ] **Slash commands current** — when adding prompts, update `slash-commands.md` quick lookup
  table AND the detailed command section

#### Developer Documentation

- [ ] **START-HERE updated** — if a new doc was created, add it to the Documentation Map
- [ ] **Navigation links exist** — every doc in `.github/docs/` must have footer navigation
  back to START-HERE and forward to at least one related doc
- [ ] **README reflects reality** — root README and module READMEs must describe actual
  capabilities, not outdated or aspirational features

---

### L — Semantic Build Safety (applies to ALL changes)

Beyond compiler/runtime build success, ensure logical and semantic correctness:

- [ ] **Semantic consistency** — enum values, keyword mappings, and resource metadata must
  be semantically correct (e.g., don't assign `ALGORITHMS` concept area to a UI framework)
- [ ] **No dead references** — no file references, resource IDs, or cross-links pointing to
  content that has been moved, renamed, or deleted
- [ ] **Configuration coherence** — if adding new config keys, env vars, or properties, ensure
  all config files (examples, docs, and actual configs) are updated in sync
- [ ] **Template consistency** — if modifying a template or schema (e.g., frontmatter fields),
  verify all existing files using that template still conform

---

### M — No Regression / No Information Loss (applies to ALL edits)

When editing existing files, protect existing content:

- [ ] **Never remove useful information** — when updating a section, preserve all existing
  useful content; add to it rather than replacing it (unless the original is factually wrong)
- [ ] **Preserve examples** — if a file has working code examples, keep them intact when
  adding new examples alongside them
- [ ] **Preserve cross-references** — when restructuring content, ensure all existing inbound
  and outbound links continue to work (update them if targets move)
- [ ] **Test existing behaviour** — verify that existing slash commands, skills, and agents
  still work after changes; don't break what already works
- [ ] **Additive by default** — treat edits as additive operations; only remove content when
  it is provably wrong, outdated, or duplicated

---

### N — Cohesive Resource Placement (applies to learning resource changes)

When adding, moving, or updating learning resources, ensure content is placed cohesively:

- [ ] **Provider cohesion** — each provider class should contain resources that share a logical
  domain. Don't scatter related resources across unrelated providers (e.g., all SE books
  belong in `SoftwareEngineeringBooksResources`, not split across `EngineeringResources`
  and `GeneralResources`)
- [ ] **Skill sub-file cohesion** — resources in each skill sub-file (e.g.,
  `resources-software-engineering.md`) should follow a logical grouping (SE tools, SE books,
  testing frameworks) with clear section headers separating them
- [ ] **Enum cross-referencing** — every book-type resource (`ResourceType.BOOK`) must set
  `ContentFormat` (PUBLISHED_BOOK or OPEN_BOOK) and `resourceAuthors` set (when authors are
  known notable authors in the `ResourceAuthor` enum — use `Set.of(A, B)` for multi-author).
  Blog/tutorial resources by notable authors should also set `resourceAuthors`
- [ ] **No orphan enum values** — every `ResourceAuthor` value must be referenced by at least
  one resource; every `ContentFormat` value must be documented; new enum values must have
  corresponding keyword mappings in `KeywordIndex.java`
- [ ] **Consistent metadata** — resources by the same author should use the same String for
  the `author` field (e.g., always "Robert C. Martin (Uncle Bob)", not sometimes "Uncle Bob"
  and sometimes "Robert Martin")
- [ ] **Skill sub-file sections match provider structure** — if a provider has inner classes
  or logical groupings (e.g., `SystemDesignResources` has HLD, LLD, Database, Distributed),
  the corresponding skill sub-file should mirror this structure with matching section headers
- [ ] **Resource count accuracy** — after adding resources, update ALL count references:
  SKILL.md Quick Index, SKILL.md description frontmatter, SKILL.md title, sub-file title,
  taxonomy-reference.md ContentFormat/ResourceAuthor counts

---

### O — Primitive Fitness Review (applies to skills, prompts, MCP, instructions changes)

When adding, importing, or significantly modifying any Copilot customization primitive:

- [ ] **Right primitive type** — confirm the content matches the primitive type:
  rules → instruction, knowledge → skill, workflow → prompt, persona → agent, live data → MCP
  (see fitness scorecard in `customization-evolution-guide.md`)
- [ ] **Dual pattern check** — if adding a skill, does it need a matching prompt (slash command)?
  If adding a prompt, is it backed by a skill for auto-detection?
- [ ] **No activation conflicts** — verify no two skills have overlapping `description` trigger
  keywords that would cause both to activate on the same query
- [ ] **Source attribution** — if imported from external source (awesome-copilot, colleague,
  skills.sh), record origin in frontmatter comment or footer
- [ ] **Regression check** — run the Regression Prevention Checklist from
  `customization-evolution-guide.md` after any import or merge
- [ ] **Framework metrics** — after adding primitives, verify counts match in:
  TAXONOMY.md (skill count), slash-commands.md (command count), skills-library.md (inventory),
  navigation-index.md (tables)
- [ ] **Evolution guide consulted** — for imports/merges, follow the Import Protocol or
  Merge Protocol in `.github/docs/customization-evolution-guide.md`

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
- Documentation cross-references verified (Section H checklist complete)
- No orphan resources (all accessible via at least one discovery path)
- No orphan docs (all `.md` files reachable from START-HERE or navigation index)
- Documentation reflects actual capabilities (no false advertising in slash-commands.md)
- Todo tracking rules followed throughout (Section I complete)
- Commit message is descriptive and accurate (Section J complete)
- PR title and description suggested after push (Section J complete)
- All cross-references, numbers, and links verified (Section K complete)
- Semantic build safety verified (Section L complete)
- No regression or information loss (Section M complete)
- Cohesive resource placement verified (Section N complete)
- Primitive fitness review passed (Section O complete — for skills/prompts/MCP/instructions changes)
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
| `.github/docs/export-guide.md` | UPDATE — if new config files, env vars, or credentials added |
| `.github/docs/export-newbie-guide.md` | UPDATE — if new exportable features added |
| `.github/docs/customization-evolution-guide.md` | CONSULT — import/merge protocol for external primitives |
| `.github/skills/TAXONOMY.md` | UPDATE — taxonomy tree, category tables, cross-refs |
| `.github/docs/skills-library.md` | UPDATE — inventory table, hierarchy, roadmap |
| `.github/docs/navigation-index.md` | UPDATE — skills table, file map, slash command table |

---

## Verification Commands

### Markdown Formatting

```powershell
# Auto-fix all .md files in the repo, then report remaining issues
.\__md_lint.ps1

# Check only — report without writing (useful for CI / review)
.\__md_lint.ps1 -Check

# Check a single file
.\__md_lint.ps1 -Target "README.md"
```

Must exit with **0** before committing. If it exits with 1, fix the reported issues
and re-run. See `.github/docs/md-formatting-guide.md` for the full rule set.

### Java Build

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
