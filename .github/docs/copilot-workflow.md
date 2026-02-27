# 🔄 Copilot Workflow Guide — Chat Patterns & Instruction Queuing

> **Goal:** Understand how Copilot chat actually works and how to give complex multi-step instructions without losing your progress.  
> **Covers:** Chat queuing, multi-turn patterns, agent mode vs ask mode, token limits, and best practices per experience level.

---

## 📑 Table of Contents

- [How Copilot Chat Actually Works](#how-copilot-chat-actually-works)
- [The Queue Question — Reading This First](#the-queue-question--reading-this-first)
- [Steering — Ensuring Request Completion Order](#steering--ensuring-request-completion-order)
- [Pattern 1: Front-Load Everything](#pattern-1-front-load-everything-newbie-recommended)
- [Pattern 2: Multi-Turn Task Lists](#pattern-2-multi-turn-task-lists)
- [Pattern 3: /multi-session — Save and Resume](#pattern-3-multi-session--save-and-resume)
- [Pattern 4: Brain Notes as a Queue](#pattern-4-brain-notes-as-a-queue)
- [Agent Mode vs Ask Mode](#agent-mode-vs-ask-mode)
- [Understanding Token Limits](#understanding-token-limits)
- [Strategies for Long Sessions](#strategies-for-long-session-work)
- [Slash Command Quick Reference](#slash-command-quick-reference)
- [Common Mistakes](#common-mistakes-and-how-to-avoid-them)

---

## How Copilot Chat Actually Works

> 🟢 **Newbie:** This section explains the mental model. Read it first.

Copilot Chat is not a queue. There is no background task runner. Here is what actually happens:

```
You press Enter
      │
      ▼
Copilot reads the ENTIRE conversation history (all messages above)
      │
      +── but only up to a TOKEN LIMIT (roughly 100k chars)
      │
      ▼
Copilot generates ONE response
      │
      ▼
Copilot STOPS. It is now WAITING for your next message.
```

**Key insight:** Copilot does NOT:
- Run tasks in the background while you type your next message
- Remember tasks you mentioned in a different VS Code window
- Continue working after it responds
- Keep a queue of pending instructions

**Copilot DOES:**
- Remember everything in the CURRENT chat window (up to the token limit)
- Execute all the steps you give it in one message (if you list them clearly)
- Resume exactly where you left off in the same conversation thread

---

## The Queue Question — Reading This First

**User question:** *"How do I add instructions to the pipeline instead of eliminating work in progress?"*

**Direct answer:** Copilot does not have an instruction pipeline. The correct approach depends on what you are trying to do:

| Situation | Solution |
|---|---|
| You want to give 5 tasks but Copilot finishes 3 and seems to stop | List ALL 5 tasks upfront in one message with checkboxes |
| You thought of more tasks WHILE Copilot is responding | Wait for Copilot to finish → then send a follow-up message |
| You want to continue work in a NEW session without losing context | Use `/multi-session` to save + reload state |
| You want to plan tasks before working on them | Write them in a brain note first, then paste into chat |
| You are worried Copilot will miss a task | Use Pattern 2 (task lists) — Copilot tracks checkboxes |

The fundamental model: **Copilot is a powerful assistant, not a parallel background agent.** You remain in control of the work order. The strategies below let you direct it efficiently.

---

## Steering — Ensuring Request Completion Order

> 🟢🟡🔴 **All levels:** This is the most common Copilot Chat pain point. Read this before sending complex multi-request messages.

**The problem:** You send a request. Copilot responds (or is mid-response). You send another request. Copilot can then:
- Abandon the first request mid-way
- Mix work from both requests in one response
- Silently skip parts of the first request entirely

**The solution:** Use explicit completion gates, sequential ordering phrases, and confirmation questions embedded directly in your messages — and optionally, persist those rules in the instruction files that Copilot reads on every message.

### What Is Steering?

**Steering** is the practice of injecting control phrases and questions into your messages so Copilot finishes one task completely before picking up the next. It simulates a queue because Copilot has no built-in queue.

The primary tool for steering is your **message text**. There is no UI queue, no background job list, no settings panel. You steer through words — and through the instruction files that run on every message automatically.

### The Files That Control Copilot Behavior

These are the MD files that shape how Copilot processes your requests. Edit them to make steering rules permanent across all sessions:

| File | What it does | How to use it for steering |
|------|-------------|----------------------------|
| [`.github/copilot-instructions.md`](../copilot-instructions.md) | **Always-on rules** — injected into every Copilot message automatically, no action needed | Add completion enforcement rules here — they apply to every chat, always |
| [`.github/instructions/java.instructions.md`](../instructions/java.instructions.md) | Java-specific rules, auto-applied to all `*.java` file edits | Add rules like "Do not move to the next file until the current file compiles" |
| [`.github/instructions/clean-code.instructions.md`](../instructions/clean-code.instructions.md) | Clean code rules applied to all files | Add ordering rules like "Complete and confirm one change before suggesting the next" |
| [`.github/prompts/`](../prompts/) | One `.prompt.md` per slash command — defines its behavior | Embed step-gate instructions in each prompt so commands self-sequence |
| [`.github/agents/`](../agents/) | One `.agent.md` per AI persona | Make agents announce completion after every step |

**Add permanent steering rules to `.github/copilot-instructions.md`:**

Open [`.github/copilot-instructions.md`](../copilot-instructions.md) and append this block. It applies to every Copilot conversation automatically:

```markdown
## Task Execution Rules
- When given multiple tasks, do them ONE AT A TIME in the order listed.
- After each task, explicitly state: "✓ Task [N] complete: [one-line description]"
- Do NOT start the next task until you have stated the current task's completion.
- If you encounter a blocker on any task, state it explicitly and WAIT for my instruction before continuing.
- Never silently skip a task. If something cannot be done, say so immediately.
```

### Completion Gates — Exact Phrases That Work

These are the specific context questions and phrases you embed in your messages to force Copilot to stop and confirm before proceeding to the next item:

#### Gate 1 — Explicit stop marker
```
Do TASK A completely.
— STOP —
Only AFTER you confirm Task A is done, begin TASK B.
```

#### Gate 2 — Required confirmation question
```
After completing TASK A, ask me: "Task A is done. Ready for Task B?"
Wait for my reply before continuing to Task B.
```

#### Gate 3 — Numbered checklist with live completion display
```
Here are 3 tasks. Do them in order.
After each one, show the updated checklist with [x] before continuing.

- [ ] 1. Refactor ConfigManager.java — extract loadFromFile()
- [ ] 2. Update ConfigParser.java — call the new method
- [ ] 3. Add a unit test for loadFromFile()

Do NOT move to the next task until you show me the updated list.
```

#### Gate 4 — The HOLD pattern (for new requests added after work started)
When you think of a new request AFTER Copilot has already started or just responded:
```
HOLD. Before picking up anything new:
1. Confirm you fully completed [brief description of the earlier request].
2. List every file you changed in that earlier request.

Once confirmed, I will give you the new task.
```

#### Gate 5 — Complete-or-abort checkpoint
```
After finishing Task 1:
- If it is complete → say so explicitly, then move to Task 2.
- If anything is incomplete → list exactly what is missing and ASK ME before proceeding.
Do not silently skip or partially complete anything.
```

### How to Queue a New Request Without Disrupting In-Progress Work

**Wrong way** — interrupts the earlier request:
```
[Copilot is mid-response on Task A]
You: "Also, can you do Task B while you're at it?"
```
This risks Copilot mixing both tasks, or abandoning Task A entirely.

**Right way — wait, then send a sequenced follow-up:**
```
Step 1: Let Copilot finish its current response completely. Do not type.
Step 2: Read the response and confirm Task A is done.
Step 3: Send a new message:

"Good — Task A is confirmed complete.

Before starting anything new:
[address any follow-up from Task A if needed]

Next task: [Task B description]
Start Task B only after confirming Task A."
```

**Right way — prepend a completion audit question:**
```
"Before starting Task B, answer these:
1. Did you fully complete [specific expected output of Task A]?
2. Are all files from Task A written / saved?

→ If yes to both: proceed with Task B.
→ If no: finish Task A first, then confirm."
```

### Detecting When Earlier Work Was Abandoned

Copilot sometimes silently skips parts of your instructions. Watch for these signals and the questions to ask:

| Signal in Copilot's response | What it likely means | What to ask |
|------------------------------|---------------------|-------------|
| "I'll handle that in the next step" | Task was deferred, not done | "Do it now, fully, before responding about anything else." |
| Code shown in chat but no files written | Only Ask mode was used, not Agent | Switch to Agent mode, or: "Apply this code directly to the file." |
| Very short response to a multi-part request | Parts were skipped | "You were asked to do N things. List all N with their status: done / skipped / in progress." |
| "Here's a summary..." with no actual code or diff shown | Work may be incomplete | "Show me the actual file contents and the diff." |
| Copilot says "Done" but files look unchanged | Agent tool may have failed silently | "Confirm: did you actually write to the file system? Open the file and show me its current contents." |
| Next task started without mentioning the previous | First task was silently abandoned | "Stop. What is the status of [Task A]? List every file changed for it before continuing." |

### Injecting Steering Context Mid-Session

If you are deep into a long session and lose confidence in ordering, use the **audit-and-continue** pattern:

```
Pause. Before continuing:

1. List every file you have modified in this session so far.
2. For every task from my original request, show its status:
   ✓ done  /  ↻ in progress  /  ✗ not started
3. Confirm what the NEXT task is.

Then resume.
```

This resets the risk of drift mid-session and gives you a full accountability checkpoint.

> 🟡 **Tip:** If you send this and Copilot's status list disagrees with what you see in git (`git diff --name-only`), trust git. Files in the response but not in the diff were likely described but not written.

---

## Pattern 1: Front-Load Everything (🟢 Newbie Recommended)

**When to use:** Any time you have a multi-step task.

**What NOT to do:**
```
Message 1: "Create a UserService class"
Message 2: "Now add a getById method"
Message 3: "Also add error handling"
Message 4: "And write a unit test"
```
This loses context as the conversation grows. Each follow-up is less accurate.

**What TO do — front-load all instructions in one message:**
```
Please do all of the following:

1. Create `src/service/UserService.java` with:
   - A `getById(long id)` method that throws `EntityNotFoundException` if not found
   - Javadoc on all public methods
   - Follow the coding conventions in copilot-instructions.md

2. Create `src/service/exception/EntityNotFoundException.java`

3. Create `test/UserServiceTest.java` with:
   - A test for the happy-path getById
   - A test for the not-found exception

4. Update `src/Main.java` to wire up UserService

Do them in order. Confirm each step as you complete it.
```

> 🟢 **Tip:** The phrase "Confirm each step as you complete it" makes Copilot narrate progress, so you can see where it is.

---

## Pattern 2: Multi-Turn Task Lists

**When to use:** Long sessions where you need to add tasks as you think of them.

**Step 1: Create a task list at the start of the session:**
```
We are going to build a complete user management feature. 
Here is the task list:

- [ ] Task 1: UserService with CRUD methods
- [ ] Task 2: UserRepository interface
- [ ] Task 3: Unit tests for UserService
- [ ] Task 4: REST controller
- [ ] Task 5: OpenAPI documentation

Start with Task 1. When done, mark it [x] and show me the updated list.
```

**Step 2: When Copilot finishes Task 1, it will show:**
```
- [x] Task 1: UserService with CRUD methods ✓
- [ ] Task 2: UserRepository interface         ← next
...
```

**Step 3: Add tasks without losing progress — just append:**
```
Great. Before starting Task 2, add these to the list:
- [ ] Task 2b: Add pagination to the list method
- [ ] Task 5b: Postman collection export

Now continue from Task 2.
```

> 🟡 **Why this works:** Copilot can see the task list in the conversation history. As long as you stay in the same chat, it tracks what has been done. New tasks drop into the bottom of the list — nothing gets lost.

**Step 4: Resume a paused session:**
```
We we building the user management feature. The task list was:
[paste the most recent task list from earlier in the conversation]

Continue from where we left off (Task 3 is next).
```

---

## Pattern 3: /multi-session — Save and Resume

> Use the `/multi-session` slash command from this repo.

**When to use:**
- You are closing VS Code and want to resume work tomorrow
- You are switching to a different machine
- The chat has grown long and Copilot is losing early context

**Save state (at end of session):**
```
/multi-session save

Task: Build RecipeService with CRUD
Status: 
  ✓ RecipeService.java created (src/service/RecipeService.java)
  ✓ Repository interface done
  ✗ Unit tests NOT started
  ✗ REST controller NOT started

Next action: Create test/RecipeServiceTest.java — start with getById happy path
Files modified: src/service/RecipeService.java, src/repository/RecipeRepository.java
Blockers: None
```

This writes the state to a file (`.github/session-state.md`).

**Reload state (at start of new session):**
```
/multi-session restore
```
Copilot reads the saved state file and picks up where it left off.

> 🟢 **Newbie:** Think of this like a save point in a video game. You save before closing, load before continuing.

> 🔴 **Pro note:** The session state file is committed to git (`.github/session-state.md`). This is intentional — it lets you resume from a different machine. If you want it private, add `session-state.md` to `.gitignore`.

---

## Pattern 4: Brain Notes as a Queue

**When to use:** Planning tasks before starting, or capturing ideas while Copilot is working.

**Workflow:**
```
Step 1: Open brain note for today's session
/brain-new
```

In the note, write your full task list before starting:
```markdown
# Session: User Management Feature
Date: 2026-03-01

## Queue
- [ ] UserService CRUD
- [ ] Tests
- [ ] REST controller  
- [ ] API docs

## Ideas that came up while working
- Could use Spring Data instead of raw JDBC
- Pagination needed in list endpoint
- Add rate limiting to the auth endpoint
```

When you get an idea while Copilot is working — jot it in the brain note. Don't interrupt Copilot.

When Copilot finishes and shows its response — add the new ideas to your next message along with the remaining tasks.

**Benefit:** Brain notes are persistent. Even if you close VS Code, your task queue exists in `brain/inbox/` or `brain/notes/`.

```
/brain-search user management
```
Finds all notes related to the feature so you can re-load context.

---

## Agent Mode vs Ask Mode

> 🟢 **Newbie:** Understand this before starting any session.

| | **Ask Mode** | **Agent Mode** |
|---|---|---|
| **What it does** | Answers questions, explains code, suggests snippets | Reads files, writes files, runs commands, makes real changes |
| **VS Code trigger** | Default chat panel | "Agent" dropdown in chat, or use `@workspace` |
| **Best for** | "How do I...?", "Explain this...", "What should I do?" | "Create this file", "Fix this bug", "Refactor this" |
| **File edits** | Suggests code in chat (you copy-paste) | Applies edits directly using file tools |
| **Reversible?** | Always (nothing changed) | Yes, via git — ALWAYS have git clean before agent mode |
| **Slash commands** | Work in both | Some commands (like `/fix`, `/tests`) work best in agent mode |

**Rule of thumb:**
- **Planning, learning, reviewing?** → Ask mode
- **Building, fixing, refactoring, building?** → Agent mode

> 🟢 **Newbie safety tip:** Before any agent-mode session that will edit multiple files, run:  
> ```
> git status
> git stash  ← if you have unsaved changes
> ```
> This gives you a clean `git stash pop` / `git reset` escape hatch.

---

## Understanding Token Limits

> 🟡 **Amateur:** Know this exists. 🔴 **Pro:** Optimize for it.

Copilot reads the conversation history on every message — but it has a **context window limit** (approximately 100,000–200,000 tokens depending on model).

**What a token is:** Roughly 4 characters = 1 token. A 1,000-line Java file ≈ 5,000–10,000 tokens.

**Signs you're hitting the limit:**
- Copilot "forgets" what files it created 50 messages ago
- Copilot contradicts itself between earlier and later messages
- Responses become vaguer or miss details from the beginning of the conversation

**Strategies:**

| Strategy | How |
|---|---|
| **Compact the conversation** | Use `/compact` command — summarizes old messages |
| **Partition by feature** | Start a NEW chat for each major feature area |
| **Use /multi-session** | Save state, start fresh chat, restore state |
| **Keep context focused** | Use `/scope src/service/` to limit file context |
| **Reference files directly** | Instead of long conversation, say "Read `UserService.java` and tell me..." |

> 🔴 **Pro pattern:** Large codebase work → one chat per component. Keep each chat under 20 messages. Use `/multi-session` to chain them. Name your brain notes by feature for cross-session retrieval.

---

## Strategies for Long-Session Work

**A. The 20-message rule:**
Start a new chat after approximately 20 messages for any feature that is still in progress. Use `/multi-session save` before switching.

**B. The scope-first approach:**
Every time you start a chat, tell Copilot exactly what files are relevant:
```
We are working on UserService. The relevant files are:
- src/service/UserService.java
- src/repository/UserRepository.java
- test/UserServiceTest.java

Ignore all other files unless I specifically reference them.
```
This prevents Copilot from "browsing" unrelated files and wasting context budget.

**C. The handoff pattern (Pro):**
When a long session ends, generate a structured handoff before closing:
```
Before I close this session, generate a handoff note in this format:

## Session Handoff
**Feature:** [name]
**Completed:** [list with files changed]
**In Progress:** [current state]  
**Next Steps:** [numbered list]
**Decisions Made:** [key architectural choices, with reasoning]
**Watch Out For:** [any gotchas discovered]
```
Paste this handoff into your brain note or `/multi-session save`.

**D. The "replay" pattern:**
If context is stale, don't fight it — start fresh and replay the key decisions:
```
New chat. Context: We are midway through building UserService.
Key decisions already made:
- We chose Spring Data JPA (not raw JDBC)
- Error handling uses EntityNotFoundException (not Optional return)
- All tests use JUnit 5 + Mockito

Files created so far: [list]
Continue from: [next task]
```

---

## Slash Command Quick Reference

> Commands relevant to workflow management:

| Command | What it does | Best mode |
|---|---|---|
| `/multi-session save` | Saves session state to `.github/session-state.md` | Either |
| `/multi-session restore` | Loads saved state and resumes | Either |
| `/brain-new` | Creates a new brain note for today | Either |
| `/brain-publish` | Promotes inbox note to permanent notes | Either |
| `/brain-search` | Searches brain notes by topic | Ask |
| `/scope` | Limits Copilot's attention to specific files/folders | Agent |
| `/compact` | Summarizes long conversation to reduce token usage | Either |
| `/fix` | Analyzes and fixes errors in selected code | Agent |
| `/tests` | Generates tests for selected code | Agent |
| `/refactor` | Refactors selected code | Agent |
| `/explain` | Explains what selected code does | Ask |
| `/design` | Designs a component from a description | Ask |
| `/mcp` | Tests MCP server connection and lists tools | Either |

---

## Common Mistakes and How to Avoid Them

| Mistake | What happens | Fix |
|---|---|---|
| Sending one task at a time in separate messages | Context fills up, early tasks are forgotten | Front-load all tasks in one message |
| Starting a new chat mid-feature without saving state | Lose all context about what was done | Use `/multi-session save` before starting new chat |
| Asking Copilot to "queue" the next task | Copilot ignores it or forgets | Append next task to the end of the CURRENT message |
| Not doing `git add/commit` between agent sessions | Hard to tell what changed if something goes wrong | Commit after every Copilot session that modifies files |
| Not specifying full context in a new chat | Copilot makes different assumptions each time | Always open new chats with a context paragraph |
| Using agent mode without a clean git state | Edits interleave with manual changes, hard to review | Always `git stash` before agent mode if you have uncommitted changes |
| Very long messages with nested lists | Copilot may miss items buried in the middle | Use numbered flat lists, not nested bullets |

---

## Worked Examples

### Example 1: Adding a feature across 3 files (Pattern 1)

```
I need to add a "favorite" feature to the Learning Resources server.

Please do ALL of the following in order:

1. Create `src/server/learningresources/model/FavoriteItem.java`
   - Fields: resourceId (String), addedAt (Instant), note (String)
   - Use Java Records
   - Javadoc on all fields

2. Create `src/server/learningresources/vault/FavoritesVault.java`
   - Methods: addFavorite(FavoriteItem), removeFavorite(String resourceId), listFavorites()
   - Persist to JSON file in user-config/favorites.json
   - Javadoc on all public methods

3. Update `src/server/learningresources/handler/` — add a FavoritesHandler
   - Handle MCP commands: add-favorite, remove-favorite, list-favorites

4. Register the new tools in `LearningResourcesServer.java`

Use the code conventions in .github/copilot-instructions.md.
Confirm after each file.
```

### Example 2: Resuming after a break (Pattern 3)

```
/multi-session restore

[Copilot reads session-state.md and confirms: "I see we were in the middle of 
adding the favorites feature. Tests are not yet done. Next: FavoritesHandlerTest.java"]

Good. Continue from the tests.
```

### Example 3: Adding to the queue mid-session (Pattern 2)

Current conversation has a task list with Tasks 1–4. Copilot just finished Task 2.

```
Before Task 3, please add these to the list and then continue:
- [ ] Task 3b: Add a helper method extractResourceId() to FavoritesHandler
- [ ] Task 4b: Integration test (not just unit test)

Now do Task 3, then 3b, then 4, then 4b.
```

---

**Navigation:** [← Phase Guide](phase-guide.md) · [← START HERE](START-HERE.md) · [Export Guide →](export-guide.md) · [MCP Setup →](mcp-server-setup.md)
