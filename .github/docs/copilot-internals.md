# Copilot Internals — Context Window, Todo Processing & Session Continuity

> **Purpose:** A practical reference for how GitHub Copilot works internally — what it
> knows, when it knows it, how context is built and carried, and how to make it plan
> complex multi-step work effectively. Includes 3-tier guidance for Newbie → Pro usage.

---

## Tier 1 — Newbie: What Copilot Knows in a Conversation

### The Context Window

Every Copilot response is generated from a **context window** — a fixed-size "reading window"
into which everything Copilot can see gets loaded before it responds.

Think of it like working memory: whatever fits in the window, Copilot "knows." Whatever
doesn't fit, it can't see — even if it was discussed earlier in a long conversation.

```text
┌─────────────────────────────────────────────────────┐
│                 CONTEXT WINDOW                       │
│                                                      │
│  1. System customizations (always loaded)           │
│     └── copilot-instructions.md                     │
│     └── active .instructions.md files               │
│     └── agent persona (if selected)                 │
│                                                      │
│  2. Skill content (loaded on demand)                │
│     └── SKILL.md files matching the query           │
│                                                      │
│  3. Prompt command content (when /command used)     │
│     └── the invoked .prompt.md file                 │
│                                                      │
│  4. Conversation history (most recent turns first)  │
│     └── trimmed from oldest when budget fills       │
│                                                      │
│  5. Referenced files (from codebase tool / #file)  │
│     └── file content read by tool calls             │
│                                                      │
│  6. Tool results (from MCP calls, search, etc.)     │
│                                                      │
└─────────────────────────────────────────────────────┘
```

### Key Facts for Beginners

- **Context does NOT persist across sessions.** Start a new chat → fresh context.
- **copilot-instructions.md is ALWAYS loaded.** It's your permanent project rules.
- **Skills are loaded on demand** — Copilot reads them when your query matches.
- **Prompts (`/commands`) inject their content** when you invoke them.
- **The more you chat, the older messages get pushed out** — long conversations lose early context.

---

## Tier 2 — Amateur: Loading Order & Activation Rules

### What Gets Loaded and When

| Source | When loaded | Priority | Token cost |
|---|---|---|---|
| `copilot-instructions.md` | Every request, always | Highest (system position) | Always paid |
| `.instructions.md` (matching `applyTo`) | When current file matches glob | High | Paid when active file matches |
| Agent persona (`.agent.md`) | When selected from dropdown | High (replaces default persona) | Paid while agent is selected |
| Skill (`SKILL.md`) | When Copilot determines query relevance | Medium | Paid only when skill activates |
| Prompt (`.prompt.md`) | When `/command` is invoked in chat | Medium | Paid only for that request |
| Conversation history | Every request | Lower (trimmed if budget full) | Paid per message in history |
| Referenced files (`#file`, `codebase` tool) | When tool reads them | Lower | Paid per file read |
| MCP tool results | When tool call completes | Lower | Paid per tool result |

### How Skills are Activated

Skills are **not loaded automatically for every request.** Copilot uses the `description`
field of each skill to decide when to load it:

```yaml
# .github/skills/brain-management/SKILL.md
---
description: >
  Use when asked about: where to put a note, how to name a file, naming conventions,
  brain workspace tier routing, frontmatter schema, timestamps...
---
```

Copilot reads all description fields (lightweight) and loads the full SKILL.md content
only when the query semantically matches. This is why **the `description` field is critical**
— it's the routing signal. A vague description = skill never activates.

**Test your skill:** Ask a question that should trigger it. If the skill content doesn't
appear in the response, the description field may not match your query pattern.

### How Prompts Inject Content

When you type `/brain-new`, Copilot:
1. Reads `brain-new.prompt.md` in full
2. Resolves any `${input:var:label}` variables (prompts you inline)
3. Injects the resolved prompt content as a new user message
4. Executes the instructions in the prompt

This means prompt content is **always fully loaded** when the command is invoked,
regardless of context budget. Use this for complex multi-step workflows.

### Context Window Sizes (Current Models)

| Model | Approx. context window | Practical max for conversation |
|---|---|---|
| Claude Sonnet 4.5 / 4.6 (current default) | ~200K tokens | ~150K tokens (system + history) |
| GPT-4o (optional) | ~128K tokens | ~90K tokens |
| GPT-4 Turbo | ~128K tokens | ~90K tokens |

**Token estimation:**

```text
1 token ≈ 4 characters of English text
1 line of markdown ≈ 10–30 tokens
1 page of text (500 words) ≈ 700 tokens
copilot-instructions.md (2KB) ≈ 500 tokens (always paid)
A SKILL.md (5KB) ≈ 1200 tokens (paid when activated)
A large code file (10KB) ≈ 2500 tokens (paid when read)
```

**Practical implication:** A 200K token window fits roughly 150,000 words — you'll
rarely run out in a single session. But very long conversations (100+ turns with large
file reads) may start dropping early context.

---

## Tier 3 — Pro: Todo Processing, Multi-Step Planning & Session Continuity

### How Copilot Tracks Todos (In Agent Mode)

In agent mode, Copilot uses a `manage_todo_list` tool to track multi-step work.
This is visible in the conversation as todo status updates.

**3-tier explanation of Copilot's internal planning:**

**Tier 1 (Simple tasks):** Copilot responds immediately without explicit planning.
"Fix this variable name" → Copilot makes the edit. No todo list needed.

**Tier 2 (Multi-step tasks):** Copilot creates a todo list at the start of the task,
marks each item as `in-progress` before working on it, and `completed` immediately after.

```yaml
User: "Update all the brain docs for the new tier structure"
Copilot planning:
  1. Create todo list [brain/README, notes/README, library/README, scripts/README, ...]
  2. Mark item 1 as in-progress → edit brain/README → mark completed
  3. Mark item 2 as in-progress → edit notes/README → mark completed
  (etc.)
```

**Tier 3 (Cross-session tasks):** Copilot saves progress to a brain session note,
and the next session starts by reading that note. This is how to bridge sessions.

### The Todo Lifecycle (Agent Mode)

```text
User gives multi-step request
         │
         ▼
Copilot creates todo list with manage_todo_list tool
  [item1: not-started]
  [item2: not-started]
  [item3: not-started]
         │
         ▼
For each item:
  Mark as in-progress → do the work → mark as completed
  (never mark multiple as in-progress simultaneously)
         │
         ▼
All items completed → no items remain not-started
         │
         ▼
Copilot summarises what was done
```

**Why this matters for you:** If you interrupt a complex task mid-way, the not-yet-completed
items remain in the conversation context. Resume by saying: "Continue the remaining items."

### How to Structure Requests for Best Planning

**Don't:**

```text
"Fix the brain docs, add new commands, update the hub, and also fix the skill file"
(Four unrelated things in one message → Copilot may batch badly)
```

**Do:**

```text
"I need to:
1. Fix the brain/ai-brain/README.md tier section
2. Add /check-standards to slash-commands.md table
3. Update hub.prompt.md with new commands
Do all three, one at a time, in order."
(Numbered list → Copilot creates a proper todo list and follows it)
```

**Best practice:** Use numbered lists for multi-step requests. One logical unit per number.

### Context Carrying Between Sessions

Context is **NOT persistent across sessions.** Every new Copilot chat window starts fresh.

What IS persistent across sessions:
- `copilot-instructions.md` — always loaded (your standing rules)
- All files in the repo — readable via `codebase` tool at any time
- `brain/ai-brain/notes/` — your session notes (if you save them)
- `.github/skills/` — all your skills (always available when relevant)
- `.github/prompts/` — all your slash commands (always available)

**Three strategies for session continuity:**

#### Strategy 1 — Save to brain/ai-brain/notes/ (recommended)

```text
At end of session:
  /brain-capture-session
  → Copilot creates a session note with: what was done, decisions made, next steps

At start of next session:
  "Read brain/ai-brain/notes/2026-03-06_session-<topic>.md and continue where we left off"
```

#### Strategy 2 — Explicit context handoff (fast)

```text
At end of session:
  "Summarise what's remaining from this task in a short paragraph I can paste next time"

At start of next session:
  Paste the summary → "Continuing from last session: [paste summary]"
```

#### Strategy 3 — Use documentation as persistent context

```text
After any architectural decision:
  Save it to brain/ai-brain/notes/YYYY-MM-DD_decision-<topic>.md

Copilot can discover this via codebase tool at any time:
  "Read my recent brain decision notes and apply the patterns we've established"
```

### Token Budget Management (Advanced)

**Tips for keeping context efficient:**

```text
1. Use /scope specific → tells Copilot to stay focused on one file
   Less tool calls = less token cost = longer effective conversation

2. Don't paste large files in chat — use #file reference instead
   #file: large-file.java → Copilot reads it via tool; doesn't fill chat history

3. For very long sessions, start a new chat and paste context summary
   New chat = fresh token budget

4. Skills load once per relevant query — not every message
   Well-described skills (specific description) load less often = less overhead

5. Use /brain-capture-session at session midpoints for complex work
   Saved note is a restore point that costs only a few tokens to load
```

### Copilot vs Agent Mode — Context Differences

| Mode | Context sources | Tools available | Best for |
|---|---|---|---|
| **Copilot Ask** | Instructions + active file + chat | Limited (read-only codebase) | Quick questions |
| **Copilot Edit** | Instructions + selected files | editFiles only | File edits |
| **Agent mode** | Full context (all sources above) | All tools incl. MCP | Complex multi-step work |

**Recommendation:** Use agent mode for anything that involves reading/writing files,
calling MCP tools, or doing more than 2 sequential steps.

---

## Quick Reference

### Context Loading Summary

```text
Always loaded:    copilot-instructions.md
                  matching .instructions.md files (based on applyTo glob)
                  selected agent persona (if any)

On demand:        SKILL.md files (description semantic match)
                  prompt files (when /command invoked)
                  referenced files (via #file or codebase tool)
                  MCP tool results (when tool called)

NOT loaded:       previous chat sessions
                  files not in workspace
                  environment variables / secrets
```

### Session Continuity Cheatsheet

```powershell
# Save session state to brain/
/brain-capture-session → gives it a topic name

# List recent session notes
brain list --tier notes --kind session

# Resume next session
"Read brain/ai-brain/notes/YYYY-MM-DD_session-<topic>.md and continue from step N"
```

### Effective Multi-Step Request Template

```text
I need to:
1. [First thing — one file or one concept]
2. [Second thing — one file or one concept]
3. [Third thing — one file or one concept]
...
Do all of these, one at a time, in order. After finishing each, confirm before moving to the next.
```

---

## Further Reading

| Topic | File |
|---|---|
| Context window (technical) | [mcp-how-it-works.md](mcp-how-it-works.md) |
| Copilot customization deep-dive | [copilot-customization-deep-dive.md](copilot-customization-deep-dive.md) |
| Prompt composition patterns | [prompt-composition.md](prompt-composition.md) |
| Brain session note workflow | [brain/ai-brain/README.md](../../brain/ai-brain/README.md) |
| Skill activation (description field) | [customization-guide.md](customization-guide.md) |
