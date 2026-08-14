---
id: BLI-093
title: Implement Cursor/.claude/ directory support with full customization parity
status: todo
priority: high
type: feature
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
estimated-effort: L
actual-effort: null
tags: [implementation, cursor, claude, customization, instructions, prompts, skills, .claude-directory]
origin-type: manual
import-batch: null
source-file: null
---

# BLI-093: Feature — Implement Cursor/.claude/ directory support

## Description

Create a fully functional `.claude/` directory alongside `.github/` that mirrors all
Copilot customization content in a format compatible with Cursor and Claude. This enables
users to use the same instructions, prompts, and skills in both Copilot and Cursor
without manual duplication.

### Goals

1. Create `.claude/` directory structure matching `.github/` hierarchy
2. Implement tool-specific adapters (YAML → Claude format)
3. Add all current Copilot instructions and prompts in Claude format
4. Document setup for Cursor and Claude
5. Create validation to ensure `.claude/` stays in sync with `.github/`

### Scope

- ✅ `.claude/instructions/` — all applicable instructions adapted for Claude XML syntax
- ✅ `.claude/prompts/` — slash commands adapted for Claude format
- ✅ `.claude/skills/` — all skills documented for Claude
- ✅ `.claude/agents/` — personas/agents in Claude format
- 🔲 `.claude/mcp/` — Claude Tools integration (future BLI)
- ✅ `.claude/README-setup.md` — step-by-step Cursor/Claude setup guide
- ✅ Sync scripts — validate and update `.claude/` when `.github/` changes

---

## Implementation Approach

### Step 1 — Understand Cursor/Claude Customization

**Research deliverable from BLI-091 must be complete first.**

Key questions to answer:
- Where does Cursor look for customization files? (`.cursor/`, `.claude/`, config directory?)
- What file format does Claude/Cursor use? (Markdown, YAML, JSON, XML?)
- How does Cursor load/reload customization? (On startup, on demand, API call?)
- What limits exist? (File size, token limit on instructions, number of files?)

### Step 2 — Create Directory Structure

```text
.claude/
├── instructions/
│   ├── change-completeness.instructions.md
│   ├── md-formatting.instructions.md
│   ├── java.instructions.md
│   ├── session-scoping.instructions.md
│   ├── chat-capture.instructions.md
│   └── ... (all .github/instructions copied + adapted)
├── prompts/
│   ├── hub.prompt.md
│   ├── research.prompt.md
│   ├── learning.prompt.md
│   └── ... (all .github/prompts adapted)
├── skills/
│   ├── java-build/SKILL.md
│   ├── digital-notetaking/SKILL.md
│   └── ... (all skill files)
├── agents/
│   ├── researcher.md
│   ├── designer.md
│   └── ... (agent personas)
├── README-claude-setup.md
└── claude-config.json (if Cursor supports JSON config)
```

### Step 3 — Implement Format Adapters

**Instruction Adaptation (YAML → Claude XML):**

From:

```yaml
---
applyTo: "**/*.java"
---
# Java Code Style

Follow these rules...
```

To:

```markdown
# Java Code Style

<cursor-config>
<applies-to>*.java</applies-to>
<priority>high</priority>
</cursor-config>

Follow these rules...
```

**Prompt Adaptation:**

Convert `.github/prompts/*.prompt.md` (with Copilot YAML frontmatter) to plain Markdown
prompts compatible with Claude's system prompt format.

**Skill Adaptation:**

Flatten skill hierarchy into markdown documents with section headers (no subdirectories
in `.claude/skills/` if Claude doesn't support nested organization).

### Step 4 — Manual Content Migration (Phase 1)

1. Copy all `.github/instructions/*.md` → `.claude/instructions/`
2. Adapt YAML frontmatter to Claude format (no applyTo equivalent; add as comment or metadata)
3. Convert `.github/prompts/*.prompt.md` → `.claude/prompts/` (remove YAML, keep markdown)
4. Copy skill files with adapted formatting
5. Test each file in Cursor/Claude to ensure proper loading

### Step 5 — Create Setup Documentation

**`.claude/README-claude-setup.md`** with:

1. **For Cursor users:**
   - Download/install Cursor
   - Clone the repo (or add as workspace)
   - Enable Claude mode
   - Point Cursor to `.claude/` directory (if manual config needed)
   - Verify instructions/prompts are loaded

2. **For Claude web/desktop:**
   - Copy content from `.claude/instructions/` into Custom Instructions
   - Paste prompts into chat or GPT Builder
   - Set system prompt from `.claude/agents/`

3. **For both:** Links to official docs + screenshots

### Step 6 — Create Sync & Validation Scripts

**`.claude-sync.ps1`** — Propagate changes from `.github/` → `.claude/`:

```powershell
# Pseudocode:
foreach file in .github/instructions/*.md {
    adapt-for-claude(file) | write-to .claude/instructions/
}
foreach file in .github/prompts/*.prompt.md {
    adapt-for-claude(file) | write-to .claude/prompts/
}
# Validate all files
validate-claude-config()
```

**`.claude-validate.ps1`** — Check consistency:

```powershell
# Verify:
# - Every .github/ file has a .claude/ equivalent
# - File timestamps match (or .claude is newer from a sync)
# - No orphan files in .claude/ without .github/ source
# - All markdown links are valid
# - No syntax errors in metadata/config
```

### Step 7 — Testing

- [ ] Open `.claude/instructions/` in Cursor → verify loads without errors
- [ ] Test a Cursor session with custom instructions loaded
- [ ] Verify sync script updates files correctly
- [ ] Validate script catches inconsistencies
- [ ] Test with Claude web interface (if possible)

---

## Acceptance Criteria

- [ ] `.claude/` directory structure created
- [ ] All `.github/instructions/` adapted and placed in `.claude/instructions/`
- [ ] All `.github/prompts/` adapted and placed in `.claude/prompts/`
- [ ] All `.github/skills/` copied to `.claude/skills/` with appropriate formatting
- [ ] Agent/persona files created in `.claude/agents/`
- [ ] Claude-specific format adapters working correctly
- [ ] `.claude-setup.md` written with step-by-step instructions
- [ ] `.claude-sync.ps1` script created and tested
- [ ] `.claude-validate.ps1` script created and tested
- [ ] All files pass validation (0 errors)
- [ ] Cursor can load and use `.claude/` customization (manual testing)
- [ ] CI/CD integration ready for BLI-098

---

## Testing Checklist

- [ ] **Cursor test 1:** Load instructions and verify they apply
- [ ] **Cursor test 2:** Use a slash command from `.claude/prompts/` and verify output
- [ ] **Cursor test 3:** Switch between `.github/` (Copilot) and `.claude/` and verify content differs appropriately
- [ ] **Sync test 1:** Modify a file in `.github/`, run sync script, verify `.claude/` updates
- [ ] **Sync test 2:** Run sync script with no changes; verify script is idempotent
- [ ] **Validation test 1:** Manually break a file, run validate script, verify it detects the error
- [ ] **Validation test 2:** Run validation on a fresh `.claude/` directory; verify 0 errors

---

## Resources & Dependencies

- **Blocked by:** BLI-091 (tool research), BLI-092 (design)
- **Unblocks:** BLI-098 (validation tests)
- **Cursor docs:** https://cursor.sh/
- **Claude custom instructions:** https://support.anthropic.com/en/articles/8487941-claude-custom-instructions

---

## Notes

- **Phase 1 (this BLI):** Full manual adaptation of existing content
- **Phase 2 (future):** Automate adaptation in sync scripts using BLI-092 rules
- Keep `.claude/README-setup.md` user-friendly; assume user has never customized AI before
- Test with multiple versions of Cursor/Claude if possible (may have different behavior)
