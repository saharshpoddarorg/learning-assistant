---
date: 2026-03-06
kind: note
project: ghcp-knowledge-sharing
tags: [ghcp, copilot-customization, mermaid, team-adoption, agents, prompt-files, distilled]
status: final
source: copilot
---

# GHCP Knowledge Sharing — Distilled Insights

> **Session:** GitHub Copilot customization KS, Capital IESD-24, Feb 2026
> **Processed:** 2026-03-06 · **Tier:** notes (committed)
>
> **Source materials:** [library/ghcp-knowledge-sharing/2026-03/](../library/ghcp-knowledge-sharing/2026-03/)
> for the original source files (presenter notes, full slide decks, CIA agent architecture).

---

## Summary

Processed 6 inbox files from a GitHub Copilot knowledge sharing session (Capital IESD-24 project,
Feb 2026). Distilled the key techniques, patterns, and learnings into an actionable 3-tier reference.
Source files archived under `brain/ai-brain/library/ghcp-knowledge-sharing/2026-03/`.

---

## Source Files (inbox → archive)

| Source File | Topic |
|---|---|
| `GHCP_Combined_KnowledgeSharing_Session.md` | Full session slides deck |
| `GHCP_Knowledge_Sharing_Session.md` | Detailed session with examples |
| `GHCP_Custom_Agents_Guide.md` | Custom agents deep dive |
| `GHCP_Mermaid_Diagrams_Session.md` | Mermaid-as-context technique |
| `GHCP_Prompt_Files_Guide.md` | Prompt files complete guide |
| `GHCP_Session_PresenterNotes.md` | Presenter talking points |

---

## 🟢 TIER 1 — Newbie: Core Concepts

> Start here if you're new to GitHub Copilot customization.
> These two concepts give you 80% of the value immediately.

### Key Insight #1 — The Three Modes Rule

> **One file, one question? → Ask or Edit. Multiple files, tools, research? → Agent.**

| Mode | Icon | Best For |
|---|---|---|
| Ask | 💬 | Quick questions, architecture Q&A, small snippets |
| Edit | ✏️ | Inline edits, annotations, refactoring within ONE file |
| Agent | 🤖 | Multi-file tasks, test generation, build/git workflows, code analysis |

The instruction layering is what makes all three powerful:
1. `copilot-instructions.md` (ALWAYS loaded)
2. `.instructions.md` matching `applyTo` (AUTO on file open)
3. `SKILL.md` matching user intent (AUTO on keyword/semantic match)
4. Request / Prompt file (your typed message)

---

## � TIER 2 — Amateur: Getting Productive

> You've used Copilot. Now learn how to make it work consistently and efficiently as a team.

### Key Insight #2 — Prompt Files Are Team Multipliers

```
❌ Without prompt file (every dev rewrites context):
   "Add a property to Device.java. Call premodify() first, fire PropertyChangeEvent,
    use @NotNull, follow VaultKey pattern, add interface method in interfaces_src..."

✅ With prompt file (1 line):
   /cof-model  Add wireGauge property to IDevice
```

**ROI from the session:** ~60-80% reduction in prompt re-engineering. Higher first-attempt accuracy.

**When to create a new prompt file:** If you write the same context into prompts more than twice,
it belongs in a prompt file. 5 minutes to create it, the whole team benefits forever.

---

### Key Insight #3 — Model Selection

| Task | Best Model | Why |
|---|---|---|
| Quick inline edit | GPT-4o | Fast, good enough |
| Generate tests / code | Claude Sonnet 4 | Good quality, follows patterns |
| Multi-file agent task with deep reasoning | Claude Opus 4 | Best reasoning |
| Large file understanding | Gemini 2.5 Pro | 1M token context window |
| Debug complex logic | Claude Opus 4 / o3-mini | Deep reasoning |
| Build/Git commands | GPT-4o | Speed matters, simple task |

> **Rule of thumb:** Start with Sonnet 4 for most tasks. Upgrade to Opus 4 when Agent mode needs deep reasoning. Use GPT-4o for speed-critical Ask/Edit.

---

### Key Insight #4 — Team Adoption Playbook

The knowledge sharing session itself is a technique: structured slides + live demos + presenter notes.

**What made the KS session effective:**
- Opening hook: "Raise your hand if you've told Copilot the same thing twice in one session"
- Problem-first: show broken output before showing fixed output
- Live demos: show actual before/after code diffs
- Hard numbers: 60-80% reduction in re-prompting, developer wrote 4 lines → Copilot wrote full impl
- Presenter notes kept on second screen for natural delivery

**Skills to create (identified in the session):**
| Priority | Skill | Why High Value |
|---|---|---|
| High + Easy | `code-review-checklist` | Rules already exist in instruction files |
| High + Easy | `test-generation` | Patterns already exist in test files |
| High + Easy | `pr-description-generator` | Template + git diff analysis |
| High + Medium | `diagram-first-development` | Eliminates wrong first attempts |
| High + Medium | `onboarding-guide` | New devs productive in days |

---

## 🔴 TIER 3 — Pro: Advanced Techniques

> Power features that unlock significant productivity gains once you understand the fundamentals.

### Key Insight #5 — Mermaid Diagrams as LLM Context (⭐ Novel Technique)

**The problem:** 500 lines of Java code → ~2,000 tokens of syntax noise → LLM infers relationships.
**The solution:** 20 lines of Mermaid → ~200 tokens → LLM *knows* relationships explicitly. **10x compression.**

```
500 LOC of Device.java  → LLM guesses: "Device probably extends something"
20 lines of Mermaid     → LLM knows:   "Device extends AbstractDevice,
                                          implements IDevice + IPrivilegedDevice,
                                          fires PropertyChangeEvent on setters,
                                          used by CaptureCaplet, HarnessCaplet, GrpcDataService"
```

**The 3-step workflow:**
```
Step 1: GENERATE → "Generate a Mermaid class diagram showing IDevice hierarchy and all consumers"
Step 2: FEED     → Paste the diagram into your next prompt as context
Step 3: MODIFY   → Agent reasons over structure, not code text
```

**Best diagram types by task:**
| Task | Diagram Type |
|---|---|
| Add/modify a property | Class diagram (shows inheritance + consumers) |
| Understand a call flow | Sequence diagram (explicit call order) |
| Impact analysis | Dependency graph (module ripple effects) |
| Debug event flow | State diagram (transitions + edge cases) |
| Lifecycle changes | Flowchart (decision points + order) |
| API design | Class diagram (contracts + hierarchies) |

**Advanced techniques learned:**
1. **Generate → Modify → Re-generate** — Modify the diagram in editor, then "implement the diff"
2. **Diagram as specification** — Draw the design instead of writing English prose
3. **Sequence diagram as test specification** — Each arrow becomes a test assertion
4. **State diagram as FSM implementation** — Each state becomes an enum value
5. **Reverse engineering** — "Generate a sequence from click to DB commit" for unfamiliar code
6. **Embed in instruction files** — `applyTo: "**/*.java"` makes diagrams always-on context

---

### Key Insight #6 — Custom Agents are Autonomous Multi-Phase Workers

Regular Chat vs Custom Agent:
| Aspect | Regular Chat | Custom Agent |
|---|---|---|
| Knowledge | General + instructions | Pre-loaded domain specialization |
| Autonomy | Responds to each prompt | Executes multi-phase workflows end-to-end |
| Tools | Standard file/terminal | MCP tools (DB, vector search, JIRA) |
| Output | Free-form | Enforced templates, quality gates, badges |
| Sub-agents | No | Can spawn specialized sub-workers |

**Agent hidden powers most people don't know:**
1. **Sub-agents** — An orchestrator agent can spawn 3 workers with separate tools and prompts
2. **Quality gates** — Hard gates that prevent output unless all checklist items pass
3. **Memory** — Agents store learned facts; next run starts with institutional knowledge
4. **MCP tool access** — Graph DB queries, vector search, JIRA/Confluence — unavailable in chat
5. **Web search** — Fetch live docs, verify library versions, confirm best practices
6. **Persistent artifacts** — Creates tracker files, reports committed to repo (diff-able, review-able)
7. **Module boundary enforcement** — Hard-coded FQN→path mappings, never guesses paths

**Agent confidence badge system (CIA pattern):**
| Badge | Meaning |
|---|---|
| 🟢 VERIFIED | Multiple sources agree — ship it |
| 🔵 GRAPH-CONFIRMED | Structural match only — likely correct |
| 🟡 VECTOR-SUGGESTED | Semantic similarity only — verify manually |
| 🔴 CONFLICT | Sources disagree — must resolve |

---

## ✅ Action Items From This Note

- [x] Create `.github/docs/mermaid-as-context.md` — full Mermaid technique guide
- [x] Create `.github/docs/team-copilot-adoption.md` — team adoption playbook
- [x] Update `copilot-customization/SKILL.md` — add Mermaid + model selection sections
- [x] Create `/write-docs` prompt — meta-command for creating docs from content
- [ ] Consider creating a `diagram-first-development/SKILL.md` in `.github/skills/`
- [ ] Consider embedding Mermaid architecture diagrams in this project's instruction files

---

## Tags

`#ghcp` `#copilot-customization` `#mermaid` `#team-adoption` `#agents` `#prompt-files` `#distilled` `#3-tier`
