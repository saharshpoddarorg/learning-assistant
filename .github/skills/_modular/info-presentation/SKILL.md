---
name: info-presentation
description: >
  Information presentation framework for making learning content capture-ready.
  Defines structured output templates for concept explanations, how-it-works breakdowns,
  comparison analyses, Q&A responses, and resource recommendations — formatted so the
  output is immediately usable as a tagged, indexed, searchable note.
  Use when: presenting concept explanations, answering "how does X work" questions,
  explaining technical topics, comparing technologies or approaches, recommending
  resources with context, or any response where the user needs to understand, retain,
  and later retrieve the information. Also activates for: explain, teach, compare,
  summarize, break down, walk through, overview, cheat sheet, quick reference,
  presentable output, note-ready, capture-ready formatting.
---

# Info Presentation — Making Knowledge Capture-Ready

> **Domain:** Learning Resources > Information Presentation
> **Applies to:** Any knowledge output — concept explanations, research answers,
> comparisons, resource recommendations, technical breakdowns
> **Related skills:** `learning-resources-vault`, `brain-management`, `deep-research`

---

## Core Principle

> **Every response that teaches something should be structured for capture.**
> The reader should be able to skim the headings, grab the metadata, and file it as a
> note — without reformatting. If the output needs editing before it's useful as a note,
> the presentation failed.

---

## The Presentation Stack

Every presentable response is built from these layers:

```text
┌─────────────────────────────────────────────┐
│  1. HEADER        Title + context sentence  │
│  2. METADATA      Tags, difficulty, domain  │
│  3. TL;DR         One-paragraph summary     │
│  4. BODY          Structured content         │
│  5. KEY TAKEAWAYS Bullet list of essentials │
│  6. CONNECTIONS   Related concepts + links  │
└─────────────────────────────────────────────┘
```

### Layer Details

| Layer | Purpose | Required? | Format |
|---|---|---|---|
| Header | Anchor the topic — what is this about? | Always | H2 heading + 1-sentence context |
| Metadata | Enable tagging, indexing, search | Always | Inline metadata block (see below) |
| TL;DR | Let the reader decide depth needed | Always | Blockquote, 2-4 sentences max |
| Body | The actual explanation | Always | Template-specific (see Content Types) |
| Key Takeaways | Crystallize the essentials | Always | 3-7 bullet points, no fluff |
| Connections | Link to related knowledge | When applicable | Table or bullet list of related topics |

---

## Metadata Block

Every presentable response includes an inline metadata block immediately after the
header. This block enables tagging, searching, and filing.

### Format

```markdown
> **Domain:** <broad area>  •  **Concept:** <specific topic>
> **Difficulty:** <beginner / intermediate / advanced / expert>
> **Tags:** `tag-1`, `tag-2`, `tag-3`, `tag-4`
> **Prerequisites:** <what you need to know first> (or "None")
```

### Rules

- **Domain** — use the broadest applicable area: Java, System Design, Networking,
  Data Structures, DevOps, etc.
- **Concept** — the specific thing being explained: "Virtual Threads", "B-Tree Indexing",
  "OAuth 2.0 Authorization Code Flow"
- **Difficulty** — honest assessment of the content level, not the topic level
  (a beginner explanation of a hard topic is still "beginner")
- **Tags** — 3-7 lowercase kebab-case tags for search; include the domain, the concept,
  and 1-2 cross-cutting concerns (e.g., `concurrency`, `performance`, `security`)
- **Prerequisites** — what the reader must already understand; link to other notes
  when possible

---

## Content Types — Structured Templates

### Type 1: Concept Explanation ("What is X?")

Use when the user asks what something is, how it works conceptually, or wants to
understand a topic from scratch.

```text
Structure:
  1. Header + Metadata
  2. TL;DR (what it is in plain English)
  3. The Problem It Solves (WHY it exists)
  4. How It Works (the mechanism — use analogies)
  5. Visual Model (diagram, table, or ASCII art)
  6. Concrete Example (real code or real scenario)
  7. Common Misconceptions (what people get wrong)
  8. Key Takeaways
  9. Connections (related concepts, deeper reading)
```

#### Presentation Rules for Concepts

- **Lead with WHY** — before explaining the mechanism, explain the problem it solves.
  "Virtual threads exist because platform threads are expensive" beats jumping straight
  into `Thread.ofVirtual()`
- **Use one analogy** — pick a single real-world analogy and stick with it through the
  explanation. Don't mix metaphors
- **Show the visual model** — every concept has a mental model; draw it. Use ASCII art,
  a Mermaid diagram, or a structured table. The reader should be able to picture it
- **One concrete example** — not a toy example, but not production complexity either.
  The example should demonstrate the core concept with no distracting boilerplate
- **Name the misconceptions** — what do people commonly get wrong? This is the highest
  retention-value section

#### Example Skeleton

```markdown
## Virtual Threads in Java

> **Domain:** Java  •  **Concept:** Virtual Threads (Project Loom)
> **Difficulty:** intermediate
> **Tags:** `java`, `concurrency`, `virtual-threads`, `project-loom`, `jdk-21`
> **Prerequisites:** Thread basics, blocking vs non-blocking I/O

> **TL;DR:** Virtual threads are lightweight threads managed by the JVM (not the OS)
> that make blocking code scalable. You write normal synchronous code, but the JVM
> multiplexes thousands of virtual threads onto a small pool of platform threads.

### The Problem

Platform threads map 1:1 to OS threads. Each costs ~1MB of stack memory...

### How It Works

[ASCII diagram of virtual threads mounted on carrier threads]

### Example

[Minimal but real code showing Thread.ofVirtual().start()]

### Common Misconceptions

| Misconception | Reality |
|---|---|
| "Virtual threads are faster" | They're not faster — they're cheaper... |

### Key Takeaways

- Virtual threads solve the thread-per-request scalability wall
- ...

### Connections

| Related Concept | Why It Matters |
|---|---|
| Structured Concurrency | Manages virtual thread lifecycles... |
```

---

### Type 2: How-It-Works Breakdown ("How does X work?")

Use when the user wants to understand the internal mechanism, flow, or process of
something — deeper than a concept explanation.

```text
Structure:
  1. Header + Metadata
  2. TL;DR (the flow in one sentence)
  3. High-Level Flow (numbered steps or flowchart)
  4. Deep-Dive per Step (expandable sections)
  5. Data Flow Diagram (what moves where)
  6. Edge Cases & Failure Modes
  7. Key Takeaways
  8. Connections
```

#### Presentation Rules for Breakdowns

- **Start with the happy path** — explain the normal flow first, then edge cases
- **Number the steps** — every flow should be a numbered sequence the reader can follow
- **Show data flow** — what goes in, what comes out, what transforms in between.
  Use a table: `Input → Process → Output`
- **Call out the non-obvious** — mark steps where something surprising happens with
  ⚠️ or 💡 callouts
- **Separate layers** — if the mechanism spans multiple layers (app → framework → OS),
  show each layer's responsibility distinctly

---

### Type 3: Comparison / Analysis ("X vs Y")

Use when comparing technologies, patterns, approaches, or trade-offs.

```text
Structure:
  1. Header + Metadata
  2. TL;DR (one-sentence verdict)
  3. Quick Decision Table (when to use which)
  4. Comparison Matrix (feature-by-feature)
  5. Detailed Analysis per Dimension
  6. Real-World Considerations
  7. Verdict / Recommendation
  8. Key Takeaways
  9. Connections
```

#### Presentation Rules for Comparisons

- **Lead with the decision** — the TL;DR should state the recommendation. Readers
  who trust you can stop there; skeptics read on for evidence
- **Quick decision table first** — a 3-row table: "Use X when...", "Use Y when...",
  "Consider Z when..."
- **Feature matrix uses symbols** — ✅ supported, ⚠️ partial, ❌ not supported,
  ➖ not applicable. Never use just text in comparison cells
- **Quantify when possible** — "X is faster" is useless; "X handles 10K req/s vs
  Y's 2K req/s under the same conditions" is useful
- **State the trade-off explicitly** — every choice has a cost. Name it

#### Comparison Matrix Template

```markdown
| Dimension | Option A | Option B | Option C |
|---|---|---|---|
| Performance | ✅ High (10K rps) | ⚠️ Medium (2K rps) | ✅ High (8K rps) |
| Learning curve | ✅ Gentle | ❌ Steep | ⚠️ Moderate |
| Community | ✅ Large | ⚠️ Growing | ❌ Small |
| Cost | 🆓 Free | 💰 Paid | 🆓 Free |
```

---

### Type 4: Q&A Response ("Quick answer to a specific question")

Use for focused answers to specific technical questions — not a full concept
explanation, but more than a one-liner.

```text
Structure:
  1. Header + Metadata
  2. Direct Answer (1-3 sentences, no preamble)
  3. Why / Because (the reasoning)
  4. Example (if applicable)
  5. Watch Out (gotcha or caveat, if any)
  6. Key Takeaways (2-3 bullets max)
```

#### Presentation Rules for Q&A

- **Answer first, explain second** — never bury the answer after three paragraphs
  of context. The first sentence should BE the answer
- **Keep it tight** — Q&A is not a concept explanation. If the question deserves a
  full explanation, switch to Type 1
- **One example max** — short, pointed, directly illustrating the answer
- **Flag the gotcha** — if there's a common mistake related to this answer, mention it

---

### Type 5: Resource Recommendation ("Best resources for X")

Use when recommending learning materials, docs, tutorials, or books.

```text
Structure:
  1. Header + Metadata
  2. TL;DR (top 1-2 picks with one sentence each)
  3. Learning Path Table (ordered by difficulty)
  4. Resource Cards (grouped by type or approach)
  5. What to Skip (overrated or outdated resources)
  6. Study Strategy (how to use these resources effectively)
  7. Key Takeaways
```

#### Presentation Rules for Recommendations

- **Order by learning path** — beginner → intermediate → advanced. The reader should
  be able to start at the top and work down
- **One sentence per resource** — why THIS resource and not the hundred others.
  "Covers X better than anything else because Y" — not "great resource, highly recommended"
- **Distinguish formats** — 📖 book, 📺 video, 📝 tutorial, 📚 docs, 🎮 interactive,
  🔧 tool. Readers have format preferences
- **Name what to skip** — negative recommendations save time. "Skip resource Z because
  it's outdated / too shallow / not worth the price"

#### Learning Path Table Template

```markdown
| # | Resource | Format | Difficulty | Why This One |
|---|---|---|---|---|
| 1 | [Resource Name](url) | 📝 Tutorial | Beginner | Best intro — covers X without assuming Y |
| 2 | [Resource Name](url) | 📖 Book | Intermediate | Deep treatment of Z with real examples |
| 3 | [Resource Name](url) | 📺 Video | Advanced | Only resource that covers W properly |
```

---

## Visual Aids — When to Use What

| Content Pattern | Visual Aid | Why |
|---|---|---|
| Sequential process (A then B then C) | Numbered list or flowchart | Shows order and dependencies |
| Parts of a whole (components, layers) | ASCII box diagram or table | Shows structure and relationships |
| Feature comparison | Comparison matrix with symbols | Enables scanning and quick decisions |
| Data transformation | Input → Process → Output table | Shows what changes and what doesn't |
| Decision logic | Decision table or flowchart | Makes branching explicit |
| Hierarchy or taxonomy | Indented tree (`text` code block) | Shows containment and levels |
| Quantity or measurement | Table with numbers | Enables comparison |
| Timeline or evolution | Numbered list with dates/versions | Shows progression |

### Visual Aid Rules

- **Every explanation should have at least one visual** — if you can't draw it, you
  might not understand it well enough to explain it
- **Use ASCII/text for diagrams in markdown** — Mermaid is nice but not universally
  rendered. ASCII art in a `text` code block works everywhere
- **Tables over bullet lists when comparing** — if you have 2+ dimensions, use a table.
  Bullet lists hide structure
- **Label everything** — diagrams without labels are puzzles, not aids

---

## Information Density Levels

Not every response needs the same depth. Match the density to the ask:

### Compact (one-screen)

Use for: quick reference, cheat sheets, refresher notes

```text
Max length:  ~20 lines
Sections:    Header + Metadata + TL;DR + Key Takeaways
Visuals:     1 table or 1 short code block max
Skip:        Detailed explanations, examples, connections
```

### Standard (default)

Use for: concept explanations, comparisons, how-it-works — the default level

```text
Max length:  ~80-150 lines
Sections:    Full template for the content type
Visuals:     1-3 (diagram + table + code example)
Include:     Everything in the template
```

### Detailed (deep-dive)

Use for: thorough explorations, deep-dives, study material

```text
Max length:  No hard limit (but break into sections)
Sections:    Full template + expanded sub-sections
Visuals:     As many as needed
Include:     Edge cases, failure modes, history, multiple examples
```

### Density Selection Heuristic

| Signal from User | Density |
|---|---|
| "quick", "briefly", "TL;DR", "summary", "remind me" | Compact |
| "explain", "how does", "what is", "compare" | Standard |
| "deep-dive", "thoroughly", "everything about", "in detail" | Detailed |
| No signal (default) | Standard |

---

## Capture-Readiness Checklist

Before finalizing any presentable response, verify:

- [ ] **Scannable** — can the reader understand the topic from headings + TL;DR alone?
- [ ] **Tagged** — does the metadata block have 3-7 searchable tags?
- [ ] **Self-contained** — can this note stand alone without the chat context?
- [ ] **One concept per response** — does this explain exactly one thing (not a grab bag)?
- [ ] **Visual anchor** — is there at least one diagram, table, or code block?
- [ ] **Actionable takeaways** — do the key takeaways tell the reader what to remember?
- [ ] **Connected** — are related concepts named (even if not linked)?

---

## Anti-Patterns — What Makes Info UN-Presentable

| Anti-Pattern | Problem | Fix |
|---|---|---|
| Wall of text | No structure, impossible to scan | Break into sections with headings |
| Buried answer | Context before conclusion | Answer first, explain second |
| No metadata | Can't tag, can't search, can't file | Add the metadata block |
| Vague takeaways | "X is important" tells nothing | Be specific: "X solves Y by doing Z" |
| Missing visuals | Reader can't form a mental model | Add one diagram or table minimum |
| Mixed concerns | One response covers 3 topics | Split into separate responses |
| Jargon without definition | Assumes knowledge not stated | Define terms or link prerequisites |
| Example without context | Code with no setup or explanation | Show the problem, then the solution |

---

## Format Symbol Reference

Use these consistently across all presentable output:

| Symbol | Meaning | Use in |
|---|---|---|
| ✅ | Supported / Yes / Advantage | Comparison matrices, feature lists |
| ❌ | Not supported / No / Disadvantage | Comparison matrices |
| ⚠️ | Partial / Caveat / Warning | Comparisons, gotchas |
| ➖ | Not applicable | Comparison matrices |
| 💡 | Insight / Non-obvious point | Callouts in explanations |
| 🔑 | Key point / Critical | Emphasis in takeaways |
| 📖 | Book | Resource type indicator |
| 📺 | Video / Playlist | Resource type indicator |
| 📝 | Tutorial / Article | Resource type indicator |
| 📚 | Documentation | Resource type indicator |
| 🎮 | Interactive / Hands-on | Resource type indicator |
| 🔧 | Tool / Repository | Resource type indicator |
| 🆓 | Free | Resource cost indicator |
| 💰 | Paid | Resource cost indicator |

---

## Connecting to the Brain Tier System

Presentable output is designed to flow into the brain's note system:

```text
Copilot Response (presentable)
    │
    ├── Direct capture → sessions/ (AI conversation capture)
    │
    ├── Distill into → notes/ (your own synthesis)
    │     └── Metadata block → frontmatter tags
    │     └── Key Takeaways → note summary
    │     └── Connections → wiki-links
    │
    └── Reference → library/ (imported materials)
```

### Mapping Presentation Layers to Note Fields

| Presentation Layer | Note Frontmatter Field | Purpose |
|---|---|---|
| Header | `title`, `subject` | What the note is about |
| Metadata: Domain | `category`, `domain` | Filing and routing |
| Metadata: Concept | `concept-area`, `scope-feature` | Specificity |
| Metadata: Difficulty | `difficulty` | Skill-level filtering |
| Metadata: Tags | `tags` | Cross-cutting search |
| Metadata: Prerequisites | `prerequisites`, `scope-refs` | Learning path ordering |
| TL;DR | `summary` (in note body) | Quick recall |
| Key Takeaways | `outcomes` | What was learned |
| Connections | `scope-refs`, inline links | Knowledge graph |
