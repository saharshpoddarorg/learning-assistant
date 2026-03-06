---
date: 2026-03-06
kind: ref
project: ghcp-knowledge-sharing
tags: [ghcp, copilot-customization, presenter-guide, team-adoption]
status: archived
source: imported
---

# GHCP Knowledge Sharing — Presenter Notes

> These are your private talking points for each section.  
> Keep this file open on a second screen or print before the session.

---

## SECTION 1 — The Problem & The Solution

- **Opening hook:** _"Raise your hand if you ever had to tell Copilot the same thing twice in the same session — 'we use JUnit 4 not 5', 'extend AppAction not AbstractAction'... That pain is exactly what we're solving today."_

- Emphasise that the problem isn't Copilot being bad — it's that Copilot doesn't know YOUR codebase. It's a general-purpose tool. Custom instructions make it Capital-specific.

- When showing the four-layer table, say: _"Think of it as four gears — they all rotate together. You don't need to manually switch between them."_

- Closing line for this section: _"Think of it like onboarding a junior developer — except the onboarding document is always active in every conversation, for every developer on the team, forever."_

---

## SECTION 2 — The `.github` Knowledge Base

- Walk through the folder tree one level at a time. Don't rush.

- **`copilot-instructions.md`** — _"This is the global configuration. Every conversation, every file, every team member — this always loads."_

- **`instructions/`** — _"Eight files, one per module. The key magic is `applyTo` — the file activates itself based on what file you're editing."_

- **`skills/`** — _"These are procedural knowledge packages. When you mention 'build' or 'git', the right skill loads automatically."_

- **`prompts/`** — _"These are slash commands. You decide when to use them. Think of them as bookmarks to expert context."_

- **`agents/`** — _"We'll cover these later. These are your heavy-hitters for complex autonomous tasks."_

- For the `applyTo` table: _"You never think about which rules to load. Open the file, get the rules. It's fully automatic."_

- For the instruction flow diagram: Walk through it step by step. _"Your prompt arrives, GHCP pre-processes it, stacks the right rules, and what reaches the LLM is already Capital-aware."_

---

## SECTION 3 — Three Modes

- **Ask vs Edit vs Agent** — give a memorable heuristic: _"If it fits in one file, Ask or Edit. If it needs to cross files or use tools, Agent."_

- For Ask examples: _"These are architecture questions. The agent already read the instruction files — it has the domain knowledge."_

- For Agent examples: _"Notice the pattern — any time you say 'following the pattern in', Agent mode shines because it can read that reference file."_

- Optional challenge to the audience: _"Which mode would you use to check if all setters in Device.java are firing PropertyChangeEvents?" — Answer: Agent mode because it needs to read and analyze._

---

## SECTION 4 — Custom Instructions Deep Dive

- **Module table** — don't read every row. Pick two contrasting ones: `cframework` (UI layer) and `cmanager` (persistence layer). _"Notice how they have completely opposite restrictions. cframework is all about UI patterns. cmanager explicitly says NO UI code. The instructions enforce these boundaries."_

- **DRC Domain Manager example** — show the ❌ first. Ask the audience: _"What's wrong with this?" Let them answer. Then reveal the ✅ version._

- **gRPC example** — _"Notice the constructor. Everyone writing a gRPC service takes `IManagerLogin`. Without the instruction, people forget this. With it, they can't. The LLM will always include it."_

- **Domain Q&A example** — this is subtle but powerful. _"You can ask architecture questions and get accurate Capital-specific answers. The LLM isn't hallucinating — it's working from the instruction files which describe the real patterns."_

---

## SECTION 5 — Live Demo — Developer Workflow

- **This is your most important section.** Go slow. Let it sink in.

- **Setup the story:** _"Imagine a developer has been given a task to add `setDeviceType()` support to `DeviceTypeImpl.java`. They write the stub — just the method signature and a TODO. Nothing else. Watch what happens."_

- **Show Step 1 (the stub):** Point out what's missing and why each missing piece matters:
  - No `premodify()` → undo stack breaks
  - No `PropertyChangeEvent` → UI state is now stale — the panel won't refresh
  - No `@NotNull` → NPE waiting to happen

- **Step 2 (the prompt):** _"Short prompt. No context about premodify, no mention of PropertyChangeEvent. Just: implement this following our conventions."_

- **Step 3 (the output):** Go line by line through the correct output. Point at each fix and say which instruction file rule caused it.

- **Step 4 (test prompt):** _"Still no manual context. Still no mention of JUnit 4, test_ prefix, MockitoJUnitRunner, anything. Just: generate a test."_

- **Step 5 (the test):** Point out:
  - Three separate test methods — each testing ONE thing
  - `ArgumentCaptor` for verifying event payload — this is sophisticated Mockito usage
  - `premodify()` is verified via spy — the test knows the contract
  - All from a one-line prompt

- **Closing:** _"The developer wrote 4 lines. Copilot wrote a complete correct implementation plus 3 tests. That is the ROI of custom instructions."_

---

## SECTION 6 — Prompt Files

- _"Instructions are always-on background rules. Prompt files are on-demand power-ups that you trigger."_

- Show the contrast in text length — before (long paragraph) vs after (one line with /command). The audience will immediately understand the value.

- **Onboarding story** is strong — use it. _"Day one. New developer. They don't know whether to use AppAction or AbstractAction, they don't know what IFIB is. They type /help-me-choose. They self-serve. The team doesn't get interrupted."_

- For creating your own: _"If you find yourself writing the same context in prompts more than twice, it's time for a prompt file. 5 minutes to create it, everyone benefits."_

---

## SECTION 7 — Custom Agents

- **The table (Regular vs Custom)** — focus on two differences that matter most:
  - Autonomy: _"Regular chat answers one question at a time. A custom agent runs a whole workflow — 8 phases, spawning sub-agents, writing files — without you prompting each step."_
  - Tools: _"Custom agents have access to Neo4j graph database, vector search, JIRA/Confluence. Regular chat doesn't."_

- **capital-nx** — _"Very focused scope. It only knows about the immersed subsystem. But within that scope, it's an expert. It will never generate code that violates EDT safety rules or misses window tracking."_

- **CIA-Orchestrator** — This is the crowd-pleaser. Walk through the 8 phases slowly:
  - _"P1 creates a tracker file. This is quality control — it prevents incomplete output."_
  - _"P2A and P2B run sub-agents in parallel — one on the graph database, one on semantic search."_
  - _"P2C merges their findings. Where they agree, it's VERIFIED. Where they disagree, it flags for human review."_
  - _"P4 computes a risk score. An interface change is 5× weight. This tells you exactly what level of caution is needed."_
  - _"The whole thing runs from one prompt. Before you write a single line of code, you know every class, every module, every risk."_

- **Confidence badges** — make this concrete: _"🟡 VECTOR-SUGGESTED means: we found something semantically similar but couldn't confirm it structurally. You need to open that file and check. It might be a false positive. But it might also be the critical dependency you missed."_

- **Thinking Beast Mode** — _"This one is for the weird, hard problems. The race condition that only happens at load. The bug that disappears when you add logging. Normal debugging modes don't cut it — you want an agent that red-teams its own solutions."_

- **Instruction layering diagram** — _"This is the key architectural insight. One prompt automatically loads 6 different knowledge sources. That's the power of the layered system."_

---

## SECTION 8 — Mermaid Diagrams

- Open with the raw insight: _"LLMs don't see architecture. They see text tokens. A 500-line Java file is mostly syntax noise. A 20-line Mermaid diagram is pure semantic density."_

- **The technique** — pause on Step 2: _"The key is Step 2 — FEED. You generate the diagram once, then paste it into every subsequent prompt. Now the LLM has a structural map of your code without reading every file."_

- **gRPC sequence diagram** — _"Ask the audience: where would you add a validation step? They might say 'before commit' or 'after beginTransaction'. The diagram makes this unambiguous. The agent doesn't need to read 5 files to know the right place."_

- **NX state diagram** — _"This one is from our actual codebase. This is the real window lifecycle. Show it to any new developer and they understand the lifecycle in 30 seconds instead of reading WindowDisplayInterceptor for an hour."_

- **Caplet flowchart** — _"Now the audience can see: if they want to add a confirmation dialog for destructive actions, it goes between preAction and actionPerformed. The diagram made a complex lifecycle decision trivially obvious."_

- **Generate → Modify → Implement** pattern: _"This is the expert move. You generate the diagram, change it to show your desired end state, then tell the agent to implement the diff. The diagram IS the specification."_

- **12 architecture diagrams** — mention this briefly: _"We've already embedded 12 Capital architecture diagrams in our instructions. They auto-load on every Java file edit. The LLM always has the full picture."_

---

## SECTION 9 — Tips & Wrap-up

- For Don'ts, make the `origin` → `iesd25` mistake a short funny story: _"We have a git skill that catches this. If you type 'push to origin', it stops you and says 'this project uses iesd25, not origin'. The skill catches what your muscle memory forgets."_

- **Closing stack diagram** — read each line slowly. Let it land.

- **Final line:** _"Everything committed to `.github/`. Pull the branch and the whole team gets the same AI-powered development experience. No setup, no configuration, no onboarding. It just works."_

- **Q&A seed questions if audience is quiet:**
  - _"Who here worked on a module we haven't covered yet? Let's see what instructions exist for it."_
  - _"What's the most painful repetitive prompt you write every day? We could probably turn that into a prompt file in 5 minutes right now."_
  - _"Has anyone tried Agent mode for test generation? What happened?"_

---

*Presenter notes prepared by GHCP Agent | March 2026*
