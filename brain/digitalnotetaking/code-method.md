# CODE Method — Capture, Organize, Distill, Express

> **Framework by:** Tiago Forte (Building a Second Brain)
> **Companion to:** [PARA Method](para-method.md) — PARA is your *structure*, CODE is your *workflow*
> **Quick nav:** [README](README.md) · [PARA](para-method.md) · [Templates](templates.md) · [ai-brain Integration](ai-brain-integration.md)

---

## What Is CODE?

CODE is a **four-stage workflow** for turning raw information into usable knowledge.
Where PARA tells you *where* to put things, CODE tells you *what to do* with them.

```
C → Capture    Don't filter — grab it now, judge later
O → Organize   Put it where it belongs (PARA structure)
D → Distill    Extract what matters — highlight, summarize, compress
E → Express    Create something from what you've captured
```

---

## Stage 1: Capture — "Grab it now, judge later"

### What to capture

Capture anything that resonates, surprises, or might be useful — even if you can't
articulate why yet. The key insight: **the value of a note comes from re-reading it later,
not from the act of capturing it.**

As a developer, capture:
- Code snippets that solved a hard problem
- Answers from Copilot / Stack Overflow that you'll need again
- Architecture decisions and the reasoning behind them
- Conference talk summaries, blog post highlights
- Gotchas, edge cases, "I spent 3 hours on this"
- Learning session outputs (what you understood, what confused you)

### Capture tools

| Source | Capture to |
|---|---|
| Copilot/AI session output | `brain new --kind session` → `brain/ai-brain/inbox/` |
| Browser article | Web clipper (Notion clipper, Obsidian Web Clipper) |
| Terminal output / error | `brain new --kind snippet` |
| Meeting / pair-programming | `brain new --kind note` in real time |
| Architecture choice | `brain new --kind decision` → ADR format |
| URL to read later | `brain new --kind resource` |

### The 1-minute rule

> If capture takes more than 1 minute, you won't do it consistently.

- Brain workspace: `brain new` → fill in title → write → done
- Copilot output: copy → paste into inbox → add a title → close file
- Link: `brain new --kind resource` → paste URL → one-line summary

### Capture in the brain workspace

```powershell
# Grab anything, right now, no structure needed
brain new --tier inbox --title "Copilot session on Java generics"

# From a specific project
brain new --tier inbox --project java --kind note --title "Virtual threads gotcha"
```

---

## Stage 2: Organize — "Put it where it belongs"

After capture, route notes into your PARA structure:

```
Projects  → time-bounded, active work: "Migrate to JDK 25"
Areas     → ongoing responsibilities: "Java expertise", "Team onboarding"
Resources → reference material for future use: "Concurrency patterns"
Archives  → completed or inactive: "2025 Q4 sprint notes"
```

### Organize checklist (after a session)

```
1. brain status                  # see what's in inbox
2. brain list --tier inbox       # read each note title
3. For each note:
   - Useful later?        → brain move <file> --tier notes
   - Belongs in repo?     → brain publish <file> --project <bucket>
   - One-off scratch?     → leave in inbox (gitignored, clear later)
4. brain clear --force           # clean up what's left
```

### Organization heuristics

- **Move to notes** if you'll re-read it within the next month
- **Publish to archive** if it should survive across machines / be shareable
- **Discard** if the value was in the act of writing (thinking out loud)

### In Obsidian / Notion / Logseq

See [PARA Method](para-method.md) for tool-specific folder structures.
After capture into inbox, drag/move files to the correct PARA folder.

---

## Stage 3: Distill — "Extract the essence"

Raw notes are noisy. Distillation compresses them into the minimum effective content.

### Progressive Summarization (layers)

Distill in passes — each pass reduces further:

```
Layer 0:  Full article / session transcript           (raw capture)
Layer 1:  Bold the most interesting sentences         (15 min per note)
Layer 2:  Highlight the bolded text further (yellow)  (5 min)
Layer 3:  Write an executive summary paragraph        (2 min)
Layer 4+: Use as an input in a future creative work   (express stage)
```

### For developer notes

| Note kind | Distillation technique |
|---|---|
| `session` | Add a "TL;DR" section at the top after the session |
| `note` | Bold the key insight; add a "So what?" line at the end |
| `resource` | Write 3 bullet points: What? Why? How do I use it? |
| `decision` | Compress the ADR to: Context (1 line) + Decision (1 line) + Consequence (1 line) |
| `snippet` | Add a comment above the code: "Use when: ___. Gotcha: ___." |
| `ref` | Create a TL;DR table row: concept | when-to-use | example |

### Bad vs good distillation

```
❌ Bad:   Keep pasting long AI outputs without editing
✅ Good:  Read the AI output, write your own understanding in 3 sentences

❌ Bad:   Keep every code snippet from a tutorial
✅ Good:  Keep only the snippets you'll adapt; add a "gotcha" comment

❌ Bad:   Title noting just the source: "From Copilot session"
✅ Good:  Title capturing the insight: "Java generics: why ? super T is needed"
```

---

## Stage 4: Express — "Create something from what you know"

Expression is how captured knowledge pays off. You don't just consume — you create.

### What expression looks like for developers

| Expression form | Example |
|---|---|
| **Code** | Implement a feature using concepts you captured |
| **ADR** | Write a decision record based on your research notes |
| **Blog / doc** | Write a team doc explaining what you learned |
| **PR description** | Write an unusually thorough PR body from your session notes |
| **Teaching** | Explain a concept to a teammate using your distilled notes |
| **Cheatsheet** | Condense a reference note into a table |
| **Prompt** | Turn your learning notes into a better Copilot prompt |
| **Commit message** | Write a rich, contextual commit from your decision notes |

### The expression loop

```
Capture → Organize → Distill → Express → (new capture from what you created)
```

Expression generates new questions and observations — which you capture back in.

---

## CODE + PARA Together

```
CODE stage    PARA destination
──────────    ────────────────
Capture    →  inbox/ (always start here)
Organize   →  Projects / Areas / Resources / Archives
Distill    →  Upgrade the note in-place (bold, summary)
Express    →  Create a new artifact (code, doc, PR, ADR)
```

| Scenario | CODE flow |
|---|---|
| Copilot helps me understand generics | Capture → inbox | Organize → notes/java | Distill → bold key lines + summary | Express → write a Java snippet |
| Sprint retro notes | Capture → inbox --kind session | Organize → archive/mcp-servers/YYYY-MM | Distill → TL;DR table | Express → add to team wiki |
| Architecture choice | Capture → inbox --kind decision | Organize → publish to archive | Distill → trim to 3-line ADR | Express → put it in the PR description |

---

## Quick Reference

```
CODE at a glance:

C  Capture    brain new --tier inbox         → grab it, no judgment
O  Organize   brain move / brain publish     → PARA destination
D  Distill    edit in-place → bold, TL;DR    → extract the signal
E  Express    code, doc, PR, ADR, commit     → ship it
```

---

## Related Resources in This Repo

| File | Content |
|---|---|
| [PARA Method](para-method.md) | Structure guide — where to put things |
| [Templates](templates.md) | Ready-made frontmatter + body for each note kind |
| [ai-brain Integration](ai-brain-integration.md) | How to feed AI sessions into CODE workflow |
| [Migration Guide](migration-guide.md) | Move from your current tool into this system |
| [START-HERE](START-HERE.md) | 4-path onboarding guide |

---

## Learning Resources

- **Building a Second Brain** (book) — Tiago Forte — the origin of CODE and PARA
- **Forte Labs Blog** — https://fortelabs.com/ — original CODE articles and updates
- **Progressive Summarization** — https://fortelabs.com/blog/progressive-summarization-a-practical-technique-for-designing-discoverable-notes/
- **BASB Online Course** — https://www.buildingasecondbrain.com/ — structured course
