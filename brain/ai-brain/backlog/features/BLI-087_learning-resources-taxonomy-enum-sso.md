---
id: BLI-087
title: Learning resources taxonomy — enum single-source-of-truth + query model
status: todo
priority: high
type: feature
created: 2026-06-03
updated: 2026-06-03
started: null
completed: null
blocked-since: null
review-since: null
epic: EPIC-001
sprint: null
parent: null
sub-items: []
origin: null
estimated-effort: L
actual-effort: null
tags: [learning-resources, taxonomy, enums, annotations, code-generation, single-source-of-truth, faceted-query, mcp, skills]
origin-type: manual
import-batch: null
source-file: null
---

# BLI-087: Learning resources taxonomy — enum single-source-of-truth + query model

> **READ THIS FIRST (resume protocol).** This file is the **single source of truth** for
> resuming work in a fresh session. It contains the complete design, rationale, phased
> execution plan, design gaps, query-model enrichment, file inventory, and open questions.
> Nothing else is required to continue. Module under work:
> `modules/mcp-learning-resources`. Skill being revamped:
> `.github/skills/learning-resources/`. Epic: `EPIC-001`.
>
> **Before coding:** re-read the enums in `model/`, `vault/KeywordIndex.java`,
> `handler/SearchHandler.java`, and the skill `taxonomy-reference.md`. Honor
> `.github/instructions/change-completeness.instructions.md` (Sections A, E, F, G, H, N, O, P)
> and `md-formatting.instructions.md`. Verify with `.\gradlew.bat build` (0 errors) and
> `.\__md_lint.ps1` (0 issues).

---

## 1. Problem Statement

The learning-resources taxonomy is the controlled vocabulary used to classify every curated
resource. It is currently expressed as Java enums in
`modules/mcp-learning-resources/src/main/java/server/learningresources/model/`:

| Enum | Values (approx) | Notable structure |
|---|---|---|
| `ConceptArea` | ~48 | Fine-grained concept; has `displayName`, `ConceptDomain domain`, optional `parentArea` (tree hierarchy), `fromString` |
| `ConceptDomain` | 9 | Top-level grouping; `displayName`, `fromString` |
| `ResourceCategory` | ~17 | Technology/ecosystem; `displayName`, `fromDisplayName` |
| `ResourceType` | 13 | Content format (docs/tutorial/blog/video/playlist/video-course/book/interactive/course/api-reference/cheat-sheet/repository) |
| `DifficultyLevel` | 4 | `displayName` + `ordinalLevel` (1..4) + `isInRange(min,max)` + `fromString` with abbreviations |
| `ContentFormat` | 3 | WEB_RESOURCE / PUBLISHED_BOOK / OPEN_BOOK |
| `LanguageApplicability` | ~6 | Programming-language scope |
| `ContentFreshness` | ~5 | Maintenance/currency status |
| `ResourceAuthor` | ~13 | Notable known authors (has `getSearchableNames()`) |
| `SearchMode` | 3 | Query-intent classification |

### The core defect — the same vocabulary lives in THREE hand-maintained places that drift

1. **Java enums** — the real source of truth.
2. **`vault/KeywordIndex.java`** — 80+ hand-typed synonym→enum mappings, e.g.
   `map.put("threads", ConceptArea.CONCURRENCY)`, `map.put("streams", FUNCTIONAL_PROGRAMMING)`.
   Adding/renaming a concept requires remembering to edit this file.
3. **Skill markdown** —
   `.github/skills/learning-resources/learning-resources-vault/taxonomy-reference.md`,
   which **hand-copies counts** ("Concept Area **48**", "Total distinct enum values: **121**",
   per-domain area counts) and full tables.

### Evidence the drift is already real (not hypothetical)

- `taxonomy-reference.md` lists `CORE_CS` as **6** areas, but the enum gained the
  `JVM_INTERNALS` sub-hierarchy (`JVM_INTERNALS` + children `GARBAGE_COLLECTION`,
  `CLASS_LOADING`, `SERIALIZATION`, `JVM_LANGUAGES`) — so the real count is higher.
- Hardcoded totals (48 / 121 / 176) are stale the instant any enum changes.
- The markdown header itself admits it "was originally implemented as Java enums... and is now
  embedded directly in the skill files as structured Markdown tables" — a **hand-fork**. That
  fork is precisely the anti-pattern this item removes.

### Secondary defect — query model cannot express real learning requests

See §6 for the full analysis. Briefly, `ResourceQuery` cannot represent: multiple concepts in
one query, a "current level → target level" span, a specific authority/source ("Oracle",
"Javadoc"), or a media-format preference ("text/pdf, not video").

---

## 2. Goal & Guiding Principle

> **One source of truth (the annotated enum), many generated projections.**
> The enum is the *what* (vocabulary). The skill is the *how to ask* (natural-language → facet
> mapping). A generator binds them so they cannot drift.

**Outcome:** collapse 3 drift surfaces → 1. The enum (with annotations) becomes the only place a
human authors taxonomy metadata. The runtime synonym map and the skill markdown/JSON are
**derived**, never hand-written.

---

## 3. Decisions Locked (from the brainstorm — do not relitigate without reason)

| # | Decision | Choice | Rationale |
|---|---|---|---|
| D1 | Vocabulary representation | **Keep Java enums as canonical source of truth** | Type safety, IDE refactoring, hierarchy already modeled; user works in Java daily |
| D2 | Sync mechanism | **Annotation-driven (`@TaxonomyTerm`) + generator** | Enum stays the single authoring surface; all else derived |
| D3 | Synonym map (`KeywordIndex`) | **Refactor to read `@TaxonomyTerm.synonyms()` reflectively at runtime** | Runtime map is always in sync; zero generation; kills the biggest drift surface for free |
| D4 | Skill markdown + JSON | **Generated by a Gradle task** | LLM/Copilot consumes files (not the JVM), so a committed file is required → generate it |
| D5 | Generator type | **Reflection-based Gradle task** `generateTaxonomyDocs` | Simpler than annotation processor / doclet; produces rich prose markdown; runs on demand |
| D6 | Staleness enforcement | **Manual now; add `--check` gate later** (mirrors `__md_lint.ps1 -Check`) | Run monthly / after enum edits; later wire into pre-commit or CI |
| D7 | Query model enrichment | **Phase 2 (after the SSO refactor)** | Keep concerns separate; don't muddy the sync work |
| D8 | Rejected alternatives | YAML/JSON-as-source (generate enums); markdown-as-source + linter; drop-markdown-runtime-only | See §10 for why each was rejected |

---

## 4. Target Architecture

```text
                         @TaxonomyTerm on enum constants
              (displayName + domain + parentArea ALREADY exist on the enum;
                       ADD: synonyms[], description, examples[])
                                      │
            ┌─────────────────────────┼─────────────────────────────┐
            │                         │                              │
   RUNTIME (JVM)              BUILD-TIME GENERATOR            BUILD-TIME GENERATOR
   KeywordIndex reads         (Gradle: generateTaxonomyDocs)  (same Gradle task)
   @TaxonomyTerm.synonyms()   writes taxonomy-reference.md    writes taxonomy.json
   reflectively at startup    + COMPUTED counts + tables      + MCP tool enum schema lists
   → ALWAYS in sync           (consumed by Copilot/skill)     (machine-readable projection)
   → no generation needed
```

- **Runtime path** never needs regeneration (reflection at startup) → the `KeywordIndex` drift
  surface disappears entirely.
- **Doc path** is the *only* thing the generator emits, because the LLM reads files, not the JVM.
- **Counts are computed** by the generator, never hardcoded → fixes the 48/121/176 staleness.

### Drift surfaces: before → after

| Before (3 hand-maintained) | After (1 authored, 2 derived) |
|---|---|
| Enum definitions | Enum definitions **+ `@TaxonomyTerm`** (the only authoring surface) |
| `KeywordIndex.java` synonym map (hand-typed) | `KeywordIndex` built **reflectively at runtime** (derived) |
| `taxonomy-reference.md` (hand-forked) | `taxonomy-reference.md` + `taxonomy.json` **generated** (derived) |

---

## 5. Annotation Design (`@TaxonomyTerm`)

New annotation. Suggested package `server.learningresources.model.taxonomy` (or `model`).
Enum constants are fields, so target `FIELD`; must be readable at runtime, so retention `RUNTIME`.

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TaxonomyTerm {
    /** Synonyms / aliases that map to this term. Migrated out of KeywordIndex. */
    String[] synonyms() default {};
    /** Structured natural-language description (mirrors the Javadoc intent). */
    String description() default "";
    /** Concrete examples to aid the skill + LLM disambiguation. */
    String[] examples() default {};
}
```

Applied to a constant (example — `ConceptArea.CONCURRENCY`):

```java
@TaxonomyTerm(
    synonyms    = {"multi-threading", "multithreading", "threads", "async", "parallel",
                   "virtual threads", "synchronization"},
    description = "Threads, executors, locks, synchronization, async patterns",
    examples    = {"virtual threads", "ExecutorService", "CompletableFuture"})
CONCURRENCY("concurrency", ConceptDomain.CORE_CS),
```

Reading reflectively (used by both `KeywordIndex` and the generator):

```java
TaxonomyTerm term = ConceptArea.class
        .getField(area.name())
        .getAnnotation(TaxonomyTerm.class);
```

### Keep what the enums already model (do NOT duplicate into the annotation)

- `ConceptArea`: `displayName`, `ConceptDomain domain`, optional `parentArea` (hierarchy via
  `getParentArea()` / `hasParent()` / `isChildOf()`), `fromString`.
- `DifficultyLevel`: `displayName`, `ordinalLevel` (1..4), `isInRange(min,max)`,
  `fromString` (handles abbreviations: beg/int/adv/exp, newbie, guru, etc.).
- Other enums: `displayName` + `fromString`/`fromDisplayName`.

The annotation **adds** synonyms/description/examples — it does **not** replace the existing
constructor fields.

---

## 6. Design Gaps in Current Query Model (Phase 2 driver)

Three motivating user questions exposed concrete gaps. Decomposition:

| Request | concept(s) | level range | source/authority | format |
|---|---|---|---|---|
| "Learn multi-threading, amateur, scratch→in-depth, from official Oracle docs" | `CONCURRENCY` | BEGINNER→ADVANCED | official + author=Oracle, cat=JAVA | docs |
| "Learn Streams API, already using it, want in-depth, from official Javadocs" | `FUNCTIONAL_PROGRAMMING` (+tag `streams`) | INTERMEDIATE→EXPERT | official, `type=API_REFERENCE` | docs |
| "Learn async communication + network protocols, amateur, basics, text/pdf human-readable" | `ASYNC_MESSAGING` **+** `PROTOCOLS` | BEGINNER→INTERMEDIATE | any | text/pdf, exclude video |

### Gap inventory

| # | Gap | Current state | Proposed fix | Decision |
|---|---|---|---|---|
| G1 | **Multi-concept queries** | `ResourceQuery.conceptArea` is a single `ConceptArea` | Change to **`Set<ConceptArea>`** with OR logic (mirror how `categories` works) | Do in Phase 2 |
| G2 | **Current→target level span** | `minDifficulty`/`maxDifficulty` exist and `isInRange` works; the *mapping vocabulary* is missing | Map phrases ("amateur"→BEGINNER, "in-depth"→ADVANCED, "from scratch"→BEGINNER, "already using"→INTERMEDIATE) in the skill playbook; optionally back with `@TaxonomyTerm.synonyms()` on `DifficultyLevel` | Skill-led; enum synonyms optional |
| G3 | **Authority/source too coarse** | `officialOnly` is a boolean | (a) Compose `officialOnly` + `type=API_REFERENCE` + author match, OR (b) add a `SourceAuthority` enum | **Lean (a) first**, revisit if insufficient |
| G4 | **Media-format preference** | `ContentFormat` has no PDF; `ResourceType` distinguishes video/book/etc. | (a) Add small **`MediaFormat`** enum (TEXT / PDF / VIDEO / INTERACTIVE), OR (b) express via `type` include/exclude sets | Decide in Phase 2 |

### The NL→facet "mapping playbook" (the skill's job)

The skill documents decision rules + a decision table + a Mermaid flow so Copilot fills the
`search_resources` MCP tool params. The server validates inputs via the `*.fromString` parsers
and uses the **reflective `KeywordIndex`** as fallback enrichment (e.g. "threads"→CONCURRENCY).
This is what makes the three example questions resolve end-to-end. Overlaps **BLI-078**
(question refinement) and **BLI-083** (faceted search).

### Worked examples — questions mapped to queries (target Phase 2 shape)

> These show how each natural-language request should resolve to a concrete `ResourceQuery`
> (Java) and the equivalent `search_resources` MCP tool call (JSON). They assume the Phase 2
> enrichments: `Set<ConceptArea> conceptAreas`, current→target difficulty mapping, source/
> authority composition (G3 option a), and a media-format preference (G4). Field names marked
> `// NEW` do not exist yet and are introduced in Phase 2.

#### Example 1 — "Learn multi-threading, I'm an amateur, scratch→in-depth, from official Oracle docs"

NL → facets: concept `CONCURRENCY` (via synonym "multi-threading"); level "amateur" + "scratch"
→ BEGINNER, "in-depth" → ADVANCED ⇒ range BEGINNER..ADVANCED; "official Oracle docs" ⇒
`officialOnly=true`, category JAVA, type DOCUMENTATION, author≈"Oracle".

```java
new ResourceQuery(
    "multi-threading",                 // searchText (raw phrase; KeywordIndex enriches)
    ResourceType.DOCUMENTATION,        // type
    Set.of(ResourceCategory.JAVA),     // categories
    Set.of(ConceptArea.CONCURRENCY),   // conceptAreas   // NEW: was single conceptArea (G1)
    DifficultyLevel.BEGINNER,          // minDifficulty  (from "amateur"/"scratch")
    DifficultyLevel.ADVANCED,          // maxDifficulty  (from "in-depth")
    null,                              // freshness
    true,                              // officialOnly   (G3: official + type/author compose)
    Set.of(),                          // tags
    true,                              // freeOnly
    25);                               // maxResults
```

```json
{
  "name": "search_resources",
  "arguments": {
    "searchText": "multi-threading",
    "type": "documentation",
    "categories": ["java"],
    "conceptAreas": ["concurrency"],
    "minDifficulty": "beginner",
    "maxDifficulty": "advanced",
    "officialOnly": true,
    "authorHint": "Oracle"
  }
}
```

#### Example 2 — "Learn Streams API, already using it, want in-depth, from official Javadocs"

NL → facets: concept `FUNCTIONAL_PROGRAMMING` (synonym "streams") + tag `streams`; "already
using" → INTERMEDIATE, "in-depth" → EXPERT ⇒ range INTERMEDIATE..EXPERT; "official Javadocs" ⇒
`officialOnly=true`, type `API_REFERENCE`, category JAVA.

```java
new ResourceQuery(
    "streams api",                                  // searchText
    ResourceType.API_REFERENCE,                     // type  ("Javadocs")
    Set.of(ResourceCategory.JAVA),                  // categories
    Set.of(ConceptArea.FUNCTIONAL_PROGRAMMING),     // conceptAreas   // NEW (G1)
    DifficultyLevel.INTERMEDIATE,                   // minDifficulty  ("already using")
    DifficultyLevel.EXPERT,                         // maxDifficulty  ("in-depth")
    null,                                           // freshness
    true,                                           // officialOnly
    Set.of("streams"),                              // tags
    true,                                           // freeOnly
    25);                                            // maxResults
```

```json
{
  "name": "search_resources",
  "arguments": {
    "searchText": "streams api",
    "type": "api-reference",
    "categories": ["java"],
    "conceptAreas": ["functional-programming"],
    "minDifficulty": "intermediate",
    "maxDifficulty": "expert",
    "tags": ["streams"],
    "officialOnly": true
  }
}
```

#### Example 3 — "Async communication + network protocols, amateur, basics, text/pdf human-readable"

NL → facets: **two** concepts `ASYNC_MESSAGING` **+** `PROTOCOLS` (demonstrates G1 multi-concept
OR); "amateur" + "basics" → BEGINNER, mild ceiling INTERMEDIATE ⇒ range BEGINNER..INTERMEDIATE;
"text/pdf human-readable, not video" ⇒ media-format preference TEXT/PDF, exclude VIDEO (G4).

```java
new ResourceQuery(
    "async communication network protocols",                 // searchText
    null,                                                    // type (any readable doc/book/article)
    Set.of(),                                                // categories (language-agnostic)
    Set.of(ConceptArea.ASYNC_MESSAGING, ConceptArea.PROTOCOLS), // conceptAreas  // NEW: OR (G1)
    DifficultyLevel.BEGINNER,                                // minDifficulty
    DifficultyLevel.INTERMEDIATE,                            // maxDifficulty
    null,                                                    // freshness
    false,                                                   // officialOnly
    Set.of(),                                                // tags
    true,                                                    // freeOnly
    25);                                                     // maxResults
// + mediaFormats = Set.of(MediaFormat.TEXT, MediaFormat.PDF)   // NEW (G4)
// + excludeTypes  = Set.of(ResourceType.VIDEO, ResourceType.VIDEO_COURSE, ResourceType.PLAYLIST) // NEW (G4)
```

```json
{
  "name": "search_resources",
  "arguments": {
    "searchText": "async communication network protocols",
    "conceptAreas": ["async-messaging", "protocols"],
    "minDifficulty": "beginner",
    "maxDifficulty": "intermediate",
    "mediaFormats": ["text", "pdf"],
    "excludeTypes": ["video", "video-course", "playlist"]
  }
}
```

> **Note on today's vs target shape:** with the *current* model, Example 3 cannot be expressed in
> one query (single `conceptArea` only) and has no media-format filter — it is the strongest
> driver for G1 and G4. Examples 1–2 are mostly expressible today except they collapse the
> author/source intent into `officialOnly` + free-text (G3).

---

## 7. Generator Design (`generateTaxonomyDocs`)

- **Location:** new tool, e.g.
  `modules/mcp-learning-resources/src/main/java/server/learningresources/tools/TaxonomyDocGenerator.java`
  with a `main`, invoked by a Gradle task `generateTaxonomyDocs` (and a `--check` variant later).
- **Inputs:** all taxonomy enums via `.values()` + reflective `@TaxonomyTerm`.
- **Outputs:**
  1. `.github/skills/learning-resources/learning-resources-vault/taxonomy-reference.md`
     — domains, concept areas with hierarchy, every enum, **computed counts**, synonym tables.
  2. `taxonomy.json` — machine-readable projection (location open — see §9).
  3. MCP `search_resources` tool input-schema enum lists (so the LLM sees valid facet values).
- **Counts are computed**, never hardcoded.
- **Markdown must pass `__md_lint.ps1`** — the generator emits lint-clean markdown (blank lines
  around headings/tables/fences, computed counts, one H1, sequential lists).

### Staleness check (Phase 1.5 / later)

- `generateTaxonomyDocs --check` regenerates into memory, diffs against the committed files,
  exits non-zero on mismatch — mirroring `__md_lint.ps1 -Check`.
- Run cadence: **manual / monthly / after any annotated-enum edit**. Later wire into pre-commit
  or CI so "edited an annotated enum but forgot to regenerate" becomes a hard failure.

---

## 8. Phased Execution Plan

> Each phase ends green: `.\gradlew.bat build` (0 errors) and `.\__md_lint.ps1` (0 issues).

### Phase 1 — Single source of truth (core)

1. Add `@TaxonomyTerm` annotation type (`@Retention(RUNTIME)`, `@Target(FIELD)`).
2. Annotate `ConceptArea` constants — migrate synonyms out of `KeywordIndex`; add description +
   examples. (Use existing Javadoc text as the description seed.)
3. Annotate the other taxonomy enums where synonyms/examples add value: `DifficultyLevel`,
   `ResourceType`, `ResourceCategory`, `ConceptDomain`, `ContentFormat`, `LanguageApplicability`.
4. Refactor `KeywordIndex` to build its maps reflectively from `@TaxonomyTerm.synonyms()`.
   **Keep the public API stable** (`conceptMap()`, etc.) so `ResourceDiscovery` and
   `RelevanceScorer` are unaffected.
5. Verify discovery/search behaviour unchanged (existing tests + manual MCP tool checks:
   `search_resources`, `browse_vault`, `get_resource`).

### Phase 1.5 — Generator + staleness

6. Build `TaxonomyDocGenerator` + Gradle task `generateTaxonomyDocs`.
7. Generate `taxonomy-reference.md` (computed counts) + `taxonomy.json` + tool-schema enum lists.
8. Add `--check` mode; document the monthly/manual run; (later) wire into pre-commit/CI.

### Phase 2 — Query model enrichment (design gaps §6)

9. `ResourceQuery.conceptArea` → **`Set<ConceptArea>`** (OR logic). Update `ResourceVault`
   filtering, `ResourceQuery.byConcept`, `hasFilters()`, `ALL`, and all callers (`SearchHandler`,
   discovery).
10. Difficulty current→target mapping vocabulary (skill playbook + optional enum synonyms).
11. Authority/source preference — start with `officialOnly` + `type`/`author` composition;
    reassess whether a `SourceAuthority` enum is warranted.
12. Media-format facet decision — new `MediaFormat` enum vs `ResourceType` include/exclude sets.
13. Update the `search_resources` MCP tool schema + the skill NL→facet mapping playbook + Mermaid.

### Phase 3 — Skill revamp + completeness ripple

14. Regenerate the skill taxonomy; rewrite the NL→facet mapping playbook under
    `.github/skills/learning-resources/`.
15. Run the **Change Completeness Checklist** (Sections A, E, F, G, H, N, O, P): update
    `KeywordIndex` notes, `slash-commands.md`, prompts, all counts, cross-references, and Mermaid
    diagrams that touch routing/decision logic.
16. Final verification: `.\gradlew.bat build` (0 errors) + `.\__md_lint.ps1` (0 issues).

---

## 9. File Inventory (where work lands)

| File / area | Action |
|---|---|
| `model/taxonomy/TaxonomyTerm.java` (new) | Annotation type |
| `model/ConceptArea.java` + other taxonomy enums | Add `@TaxonomyTerm` to constants |
| `vault/KeywordIndex.java` | Refactor to reflective build from annotations (keep public API) |
| `tools/TaxonomyDocGenerator.java` (new) | Generator `main` |
| `modules/mcp-learning-resources/build.gradle.kts` | `generateTaxonomyDocs` (+ `--check`) task |
| `model/ResourceQuery.java` | Phase 2: `Set<ConceptArea>`, new facets |
| `vault/ResourceVault.java` (+ `ResourceDiscovery`, `RelevanceScorer`) | Phase 2: multi-concept filtering |
| `handler/SearchHandler.java` + MCP tool schema | Phase 2: expose new facets |
| `.github/skills/learning-resources/.../taxonomy-reference.md` | GENERATED (stop hand-editing) |
| `.github/skills/learning-resources/` mapping playbook | NL→facet decision rules + Mermaid |
| `taxonomy.json` (new) | GENERATED machine-readable taxonomy |
| `.github/docs/slash-commands.md`, prompts, counts | Completeness ripple |

---

## 10. Rejected Alternatives (and why)

| Alternative | Why rejected |
|---|---|
| **YAML/JSON as source, generate enums from it** | User explicitly wants to retain enums as primary and is fluent in Java; enum-as-source keeps type safety + IDE refactoring as the authoring experience |
| **Keep markdown hand-written, add a linter** | Doesn't fix root cause; humans still transcribe enum→markdown by hand |
| **Pure runtime, drop the markdown entirely** | The skill/LLM needs a readable projection; the markdown cannot be dropped |
| **Annotation processor (APT) for generation** | Can't easily write into `.github/`; awkward for rich prose; overkill for a monthly run |
| **Javadoc doclet** | Slow, clunky toolchain for what a small reflective `main` does cleanly |

---

## 11. Future Improvements (out of scope now, capture for later)

- **Generated `KeywordIndexGenerated.java`** instead of runtime reflection — only if reflection
  startup cost or test ergonomics ever justify it (enum set is small → currently negligible).
- **Auto-run the generator** on annotated-enum edits via a pre-commit hook or annotation
  processor check (promote the `--check` gate from manual to automatic).
- **Facet counts** (e-commerce style — how many resources per facet value) once faceted query
  lands (overlaps BLI-083).
- **Dynamic facet discovery** — detect available facets from indexed content (BLI-083 synergy).
- **Multi-turn refinement dialogue** for ambiguous requests ("Did you mean ASYNC_MESSAGING or
  PROTOCOLS?") — overlaps BLI-078.
- **Glossary/alias integration** — `ResourceAuthor` aliases + abbreviation expansion could feed
  the same annotation mechanism (overlaps BLI-025).
- **Nested/drill-down facets** — Java → Spring → Spring Boot (overlaps BLI-083 future notes).

---

## Acceptance Criteria

### Phase 1 — Single source of truth

- [ ] `@TaxonomyTerm` annotation (RUNTIME, FIELD) carrying synonyms, description, examples
- [ ] Taxonomy enums annotated; `ConceptArea` synonyms migrated out of `KeywordIndex`
- [ ] `KeywordIndex` builds maps reflectively from `@TaxonomyTerm.synonyms()` with stable public API
- [ ] Existing discovery/search behaviour preserved (build + tests green; MCP tools verified)

### Phase 1.5 — Generator + staleness

- [ ] `generateTaxonomyDocs` Gradle task regenerates `taxonomy-reference.md` with **computed** counts
- [ ] Generator also emits `taxonomy.json` + MCP `search_resources` enum schema lists
- [ ] Generated markdown passes `__md_lint.ps1` (0 issues)
- [ ] `--check` mode diffs generated vs committed and fails on mismatch (CI/pre-commit wiring later)

### Phase 2 — Query model enrichment

- [ ] `ResourceQuery.conceptArea` becomes `Set<ConceptArea>` (OR logic); all callers updated
- [ ] Current→target level mapping vocabulary ("amateur", "in-depth", "from scratch", "already using")
- [ ] Source/authority preference expressible (official + type/author, or a new facet)
- [ ] Media-format preference expressible (text/pdf vs video)
- [ ] All three motivating questions map end-to-end to a valid `search_resources` call

### Phase 3 — Completeness ripple

- [ ] Skill NL→facet mapping playbook + Mermaid added/updated under `.github/skills/learning-resources/`
- [ ] Change Completeness Checklist (A/E/F/G/H/N/O/P) satisfied
- [ ] `.\gradlew.bat build` 0 errors; `.\__md_lint.ps1` 0 issues

## Attachments & References

| Type | Path / URL | Added | Notes |
|---|---|---|---|
| Code | `modules/mcp-learning-resources/src/main/java/server/learningresources/model/` | 2026-06-03 | Taxonomy enums (source of truth) |
| Code | `.../model/ConceptArea.java` | 2026-06-03 | Has domain + parentArea hierarchy + fromString |
| Code | `.../model/DifficultyLevel.java` | 2026-06-03 | ordinalLevel + isInRange (range queries already work) |
| Code | `.../model/ResourceQuery.java` | 2026-06-03 | Single `conceptArea` → needs `Set` (G1); has min/max difficulty |
| Code | `.../vault/KeywordIndex.java` | 2026-06-03 | 80+ hand-typed synonym map to refactor reflectively |
| Code | `.../handler/SearchHandler.java` | 2026-06-03 | MCP search/browse/get handlers; caller of ResourceQuery |
| Skill | `.github/skills/learning-resources/learning-resources-vault/taxonomy-reference.md` | 2026-06-03 | Hand-forked counts (48/121) — to become generated |
| Skill | `.github/skills/learning-resources/learning-resources-vault/SKILL.md` | 2026-06-03 | Vault skill (176 resources) |
| Rules | `.github/instructions/change-completeness.instructions.md` | 2026-06-03 | Sections A/E/F/G/H/N/O/P must be honored |
| Rules | `.github/instructions/md-formatting.instructions.md` | 2026-06-03 | Generated markdown must comply |

## Related

- EPIC-001: Full-fledged learning resources system (parent epic)
- BLI-003: Module hierarchies for learning resources (sibling — same module)
- BLI-083: Search engine — metadata index and faceted search (query-model overlap — G1/G3/G4)
- BLI-078: Question refinement — smarter query formulation (NL→facet mapping overlap — G2)
- BLI-025: Library/glossary for abbreviations and aliases (synonyms/alias overlap)

## Activity Log

| Date | Time | Actor | Action | Details |
|---|---|---|---|---|
| 2026-06-03 | 08:35 PM | system | created | Brainstormed enum SSO + query-model design; created tracker under EPIC-001 |
| 2026-06-03 | 08:56 PM | system | updated | Expanded into full self-contained design doc (phases, gaps, query model, future work) |
| 2026-06-03 | 09:05 PM | system | updated | Added worked query examples (3 questions → ResourceQuery + search_resources calls) |

## Notes

- **Core principle:** one source of truth (annotated enum), many generated projections.
  Enum = vocabulary (*what*); skill = NL→facet mapping (*how to ask*); generator binds them.
- **Why reflection for `KeywordIndex`:** the JVM consumes synonyms at runtime, so reflection
  keeps it perpetually in sync with zero generation. The LLM consumes markdown/JSON, which the
  JVM can't hand to it, so those are the only generated artifacts.
- **Open questions (resolve during implementation):**
  - [ ] `taxonomy.json` location — module `build/` (ephemeral) vs committed `docs/` (consumable)?
  - [ ] Keep `officialOnly` boolean + author/type, or introduce a `SourceAuthority` enum? (G3)
  - [ ] New `MediaFormat` enum vs `ResourceType` include/exclude for "text/pdf vs video"? (G4)
  - [ ] Should `DifficultyLevel`/`ResourceType` also carry `@TaxonomyTerm.synonyms()` so the
        generator emits their synonym tables too? (Leaning yes for consistency.)
  - [ ] Pure runtime reflection for `KeywordIndex` vs a generated `.java` — currently runtime.
  - [ ] Where the NL→facet mapping playbook lives — existing `learning-resources-vault` SKILL.md
        vs a new dedicated skill (coordinate with BLI-078 / BLI-083).

## Time Tracking

| Metric | Value |
|---|---|
| Estimated effort | L |
| Actual effort | — |
| Created | 2026-06-03 |
| Started | — |
| Completed | — |
| Cycle time | — |
