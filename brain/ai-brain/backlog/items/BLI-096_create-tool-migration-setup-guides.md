---
id: BLI-096
title: Create tool migration and setup guides for universal customization
status: todo
priority: high
type: documentation
created: 2026-07-31
updated: 2026-07-31
started: null
completed: null
blocked-since: null
review-since: null
epic: BLI-090
sprint: null
parent: null
sub-items: []
origin: null
estimated-effort: M
actual-effort: null
tags: [documentation, setup-guides, migration, tutorial, user-friendly, onboarding]
origin-type: manual
import-batch: null
source-file: null
---

# BLI-096: Documentation — Create tool migration and setup guides

## Description

Create comprehensive, user-friendly documentation that helps users understand and adopt
the universal customization framework across different AI coding assistants. This BLI
produces:

1. **Overview document** — why universal customization matters, quick comparison table
2. **Per-tool setup guides** — step-by-step instructions for each tool (Copilot, Cursor, Gemini, ChatGPT)
3. **Migration guides** — how to switch from one tool to another without losing work
4. **Troubleshooting guides** — common issues and solutions per tool
5. **Developer guide** — how to add support for a new AI tool

### Goals

1. Make it easy for users to pick the right tool(s) for their workflow
2. Lower the barrier to trying new tools (no need to re-learn customization)
3. Document tool-specific gotchas and workarounds
4. Provide clear troubleshooting for common problems
5. Enable community contributions for new tools

### Scope

- ✅ `.github/docs/universal-framework-overview.md` — high-level guide
- ✅ `.github/docs/tool-setup-guides/` directory with per-tool guides:
  - `copilot-github-setup.md` (reference; already documented elsewhere)
  - `cursor-claude-setup.md`
  - `gemini-setup.md`
  - `chatgpt-openai-setup.md`
- ✅ `.github/docs/tool-migration-guides/` directory:
  - `migrate-from-copilot-to-cursor.md`
  - `migrate-from-copilot-to-gemini.md`
  - `migrate-to-multi-tool-setup.md`
- ✅ `.github/docs/troubleshooting/` directory:
  - `copilot-troubleshooting.md`
  - `cursor-claude-troubleshooting.md`
  - `gemini-troubleshooting.md`
  - `chatgpt-troubleshooting.md`
- ✅ `.github/docs/contributing-new-tool.md` — how to add a new AI tool

---

## Document Outline

### D1 — Universal Framework Overview

**Path:** `.github/docs/universal-framework-overview.md`

**Contents:**

1. **Why Universal Customization?** (1-2 pages)
   - Problem: managing separate customizations per tool is tedious
   - Solution: unified framework, tool-specific adapters
   - Benefits: consistency, maintainability, flexibility

2. **Quick Comparison Table** (visual)

   ```markdown
   | Tool | Free/Paid | Integration | Customization | Learning Curve |
   |---|---|---|---|---|
   | Copilot | Free+ | VS Code native | Full | Low (if using VS Code) |
   | Cursor | Free+ | IDE replacement | Full | Low (VS Code-like) |
   | Gemini | Free+ | Web + extensions | Partial | Medium |
   | ChatGPT | Free+ | Web + API | Partial | Low (web) / High (API) |
   ```

3. **Supported Customization Primitives** (matrix)
   - Which tools support instructions, prompts, skills, agents, MCP tools

4. **Quick Start Paths**
   - "I just want Copilot" → link to copilot-github-setup.md
   - "I want to try Cursor" → link to cursor-claude-setup.md
   - "I use multiple tools" → link to multi-tool-setup.md

5. **Navigation**
   - Links to all setup guides, migration guides, troubleshooting
   - Links to tool-specific directories (.github/, .claude/, .gemini/, .chatgpt/)

---

### D2 — Per-Tool Setup Guides

Each guide follows a consistent structure:

**Structure:**

1. **Tool Overview** (1-2 paragraphs)
   - What is this tool?
   - Who is it for? When to use it?
   - Free vs. paid features

2. **System Requirements**
   - OS support
   - Account/signup needed?
   - API key/credentials?
   - Storage space?

3. **Installation**
   - Download/signup links
   - Step-by-step installation (with screenshots if possible)
   - Verify installation

4. **Initial Setup**
   - Launch the tool
   - Basic configuration
   - Verify it works

5. **Customization Setup** (the main content)
   - Approach 1 (simplest)
   - Approach 2 (intermediate)
   - Approach 3 (advanced, if applicable)
   - Each approach:
     - What it is & when to use
     - Step-by-step instructions
     - Where to copy content from (e.g., `.claude/instructions/`)
     - Verification/testing

6. **Tips & Best Practices**
   - Common gotchas
   - Performance tips
   - How to keep customization in sync

7. **Next Steps**
   - Troubleshooting links
   - Links to reference docs
   - How to contribute improvements

8. **FAQ**
   - Common questions

---

### Example: `.github/docs/tool-setup-guides/cursor-claude-setup.md`

```markdown
# Cursor + Claude Setup Guide

## What is Cursor?

Cursor is a modern code editor based on VS Code that integrates Claude (Anthropic's LLM)
directly into the editor. Unlike Copilot (GitHub's LLM), Cursor uses Claude's language model
by default in Claude mode.

### Why use Cursor?

- Built-in Claude integration (no separate plugin needed)
- Powerful code understanding via Claude
- Similar to VS Code (familiar if you've used VS Code + Copilot)
- Competitive pricing

### Use Cursor if you...
- Prefer Claude's reasoning style over Copilot
- Want a seamless IDE + LLM experience
- Are migrating from VS Code + Copilot

## System Requirements

- OS: Windows, macOS, or Linux
- Disk space: ~500MB
- RAM: 4GB minimum, 8GB recommended
- Internet connection (required for Claude API calls)
- Cursor account (free or paid)

## Installation

1. Visit https://cursor.sh/
2. Download for your OS (Windows/macOS/Linux)
3. Run the installer
4. Launch Cursor
5. Sign in with your Cursor account
6. Agree to Claude usage terms

## Initial Verification

- [ ] Cursor opens without errors
- [ ] Settings dialog opens (Cmd/Ctrl + ,)
- [ ] Extensions tab visible
- [ ] You can see "Claude" in the bottom status bar

## Customization Setup

Cursor supports customization via `.claude/` directory and through Claude's custom instruction system.

### Approach 1 — Custom Instructions (Easiest, 5 minutes)

Custom Instructions tell Claude how to behave globally. They apply to all chats and
code interactions.

**Step 1:** Open Cursor Settings
- Windows/Linux: Ctrl + ,
- macOS: Cmd + ,

**Step 2:** Find "Custom Instructions" section
- Search for "custom" if you can't find it
- Look for a text area where you can enter instructions

**Step 3:** Copy instructions from our repo

Open `.claude/custom-instructions/core-principles.txt` from the learning-assistant repo
and copy the entire contents into Cursor's Custom Instructions field.

**Step 4:** Save settings (Ctrl+S or click Save)

**Step 5:** Test it

Open a file (e.g., `modules/search-engine/src/main/java/search/SearchEngine.java`)
and ask Claude: "Review this code for SOLID violations."

Claude should mention the principles you added in custom instructions.

### Approach 2 — Full System Prompt (Intermediate, 15 minutes)

For more control, replace the default Claude system prompt with our comprehensive
system prompt that includes project context.

**Prerequisites:** Cursor Pro or Team license (custom system prompts may require paid tier)

**Steps:**

1. Navigate to `.claude/system-prompts/` in our repo
2. Choose the appropriate system prompt:
   - `default.txt` — general purpose
   - `code-analysis.txt` — for code review
   - `learning.txt` — for learning/teaching mode
3. Copy the full content
4. In Cursor: Settings → System Prompt → Paste content
5. Save and restart Cursor

### Approach 3 — Load from .claude/ Directory (Advanced, 30 minutes)

If Cursor supports loading `.claude/` configurations directly:

**Steps:**

1. Add `learning-assistant` repo to your Cursor workspace
2. Cursor should auto-detect `.claude/` directory
3. Verify in Cursor settings that customization is loaded
4. Test by asking Claude to use specific skills from `.claude/skills/`

(Note: exact mechanics depend on Cursor version; adjust as needed)

## Keeping Sync

...

## Tips & Best Practices

- Start with Approach 1 (custom instructions) — it's simplest
- Test with actual code from the learning-assistant repo
- If behavior seems wrong, check Cursor logs (Help → Show Logs)
- Claude's reasoning improves with detailed instructions

## Next Steps

- Troubleshooting: see cursor-claude-troubleshooting.md
- Other tools: see universal-framework-overview.md
- Contribute: see CONTRIBUTING-NEW-TOOL.md (if you improve this guide)

## FAQ

**Q: Cursor is downloading a big model — is that normal?**
A: No — Cursor uses Claude API, not local models. If downloading, cancel and check
settings.

**Q: My custom instructions aren't working.**
A: See cursor-claude-troubleshooting.md § Custom Instructions Not Loading

**Q: Can I use .github/ (Copilot) instructions in Cursor?**
A: Partially. Our `.claude/` directory contains Cursor-optimized versions. Using
`.github/` files directly may not work as expected.

**Q: Is Cursor better than Copilot?**
A: Different tools for different needs. Cursor excels at reasoning; Copilot excels at
speed and IDE integration. Try both.
```

---

### D3 — Migration Guides

**Structure:**

1. **Overview** — what you'll need, time estimate, what's preserved
2. **Before You Start** — backup checklist, note what customizations you're using
3. **Step-by-Step Migration**
   - Export from old tool (if applicable)
   - Import/set up in new tool
   - Verify it works
4. **What's Different?** — feature gaps, workarounds
5. **Troubleshooting** — common migration issues
6. **Rollback Plan** — how to go back if needed

**Migration paths documented:**

- Copilot → Cursor (easiest; both use customization files)
- Copilot → Gemini (moderate; manual copy-paste of instructions)
- Copilot → ChatGPT (moderate; API-based or GPT Builder)
- Copilot → Multi-tool setup (advanced; using all at once)

---

### D4 — Troubleshooting Guides

Per-tool troubleshooting covering:

- Customization not loading
- API errors/auth issues
- Performance problems
- Specific feature issues
- How to get help (logs, issue templates, forums)

---

### D5 — Contributing New Tool Guide

**`.github/docs/contributing-new-tool.md`**

For developers who want to add support for a new AI tool:

1. **Framework requirements**
   - What patterns must the tool support?
   - How do we abstract it?

2. **Implementation checklist**
   - Research (add to BLI-091 output)
   - Design abstraction (add to BLI-092 design)
   - Create directory structure (e.g., `.newtool/`)
   - Write setup guide
   - Write validation tests (BLI-098)
   - Create PR and get reviewed

3. **Example**
   - Real example of adding a tool (Cursor/Claude)

4. **Review criteria**
   - Setup guide is comprehensive and tested
   - Customization loading works
   - Validation tests pass
   - Documentation is clear

---

## Implementation Tasks

1. **Create documentation structure**
   - Create `.github/docs/tool-setup-guides/` directory
   - Create `.github/docs/tool-migration-guides/` directory
   - Create `.github/docs/troubleshooting/` directory

2. **Write overview document** (`universal-framework-overview.md`)
   - High-level explanation
   - Comparison table
   - Navigation links

3. **Write per-tool setup guides**
   - copilot-github-setup.md (reference existing docs)
   - cursor-claude-setup.md
   - gemini-setup.md
   - chatgpt-openai-setup.md

4. **Write migration guides**
   - migrate-from-copilot-to-cursor.md
   - migrate-from-copilot-to-gemini.md
   - migrate-to-multi-tool-setup.md

5. **Write troubleshooting guides** (4 documents)

6. **Write contributing guide** (`contributing-new-tool.md`)

7. **Integrate into `.github/docs/` navigation**
   - Update README
   - Update START-HERE if applicable
   - Add links from hub.prompt.md or similar

---

## Acceptance Criteria

- [ ] All directory structures created
- [ ] Overview document written (≥1500 words)
- [ ] All 4 setup guides written (≥800 words each)
- [ ] All 3 migration guides written (≥600 words each)
- [ ] All 4 troubleshooting guides written (≥400 words each)
- [ ] Contributing guide written (≥500 words)
- [ ] All guides tested for clarity (ideally by non-experts)
- [ ] All links validated (no 404s)
- [ ] Formatting passes `__md_lint.ps1`
- [ ] Integrated into main navigation
- [ ] Screenshots or diagrams (if helpful for UX)

---

## Deliverables Summary

```text
.github/docs/
├── universal-framework-overview.md           (new)
├── tool-setup-guides/                        (new)
│   ├── copilot-github-setup.md
│   ├── cursor-claude-setup.md
│   ├── gemini-setup.md
│   └── chatgpt-openai-setup.md
├── tool-migration-guides/                    (new)
│   ├── migrate-from-copilot-to-cursor.md
│   ├── migrate-from-copilot-to-gemini.md
│   └── migrate-to-multi-tool-setup.md
├── troubleshooting/                          (new)
│   ├── copilot-troubleshooting.md
│   ├── cursor-claude-troubleshooting.md
│   ├── gemini-troubleshooting.md
│   └── chatgpt-troubleshooting.md
└── contributing-new-tool.md                  (new)
```

---

## Dependencies & Blockers

- **Blocked by:** BLI-091, BLI-092 (design and research must be complete)
- **Needs:** BLI-093, BLI-094, BLI-095 (directory structures and templates from implementation)
- **Unblocks:** BLI-097, BLI-098 (validation and scripts)

---

## Notes

- Keep tone approachable; assume users are smart but new to each tool
- Include screenshots where helpful (settings dialogs, copy-paste examples)
- Use consistent formatting across all guides
- Update guides when tools change (note: "As of [date]")
- Consider creating video tutorials (future phase)
