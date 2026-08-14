---
id: BLI-097
title: Implement validation and sync scripts for universal framework consistency
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
tags: [automation, scripting, validation, sync, consistency, powershell, cli-tools]
origin-type: manual
import-batch: null
source-file: null
---

# BLI-097: Feature — Implement validation and sync scripts

## Description

Create automation scripts that:

1. **Validate** all tool-specific customization directories for consistency and correctness
2. **Sync** content from source (`.github/`) to tool-specific directories (`.claude/`, `.gemini/`, `.chatgpt/`)
3. **Detect drift** — flag when tool directories are out of sync with source
4. **Report issues** — provide actionable error messages when configs are broken
5. **Enable CI/CD** — scripts run as part of the build/validation pipeline

### Goals

1. Ensure all tool customizations remain consistent with source of truth
2. Reduce manual copy-paste errors and maintenance burden
3. Enable automated validation in CI/CD pipeline
4. Provide clear feedback when something is wrong
5. Support both one-time sync and continuous monitoring

---

## Scripts Overview

### Script 1 — `sync-customization.ps1` — Sync from .github/ → tool-specific dirs

**Purpose:** Propagate changes from `.github/` (source of truth) to `.claude/`, `.gemini/`, `.chatgpt/`

**When to run:**
- After adding/modifying instructions or prompts in `.github/`
- Before committing (to ensure tool directories are up-to-date)
- As part of pre-commit hooks (optional)

**Functionality:**

```powershell
.\sync-customization.ps1 [OPTIONS]

OPTIONS:
  -Source <path>        Source directory (default: .github/)
  -Target <path>        Target directory to sync to (e.g., .claude/, .gemini/)
                        If omitted, syncs ALL targets
  -DryRun               Preview what would change without writing
  -Force                Overwrite without prompting
  -Verbose              Show detailed operation log
  -Transformers <tool>  Apply tool-specific transformations (claude, gemini, chatgpt)
                        If omitted, copies with format adapters

EXAMPLES:
  # Sync .github/ to .claude/ (dry run)
  .\sync-customization.ps1 -Target .claude/ -DryRun

  # Sync .github/ to all tools (force, verbose)
  .\sync-customization.ps1 -Force -Verbose

  # Sync specific subdirectory
  .\sync-customization.ps1 -Source .github/instructions -Target .claude/instructions
```

**Transformation Logic (Adapters):**

For each file being synced:

1. **Read source file** (e.g., `.github/instructions/java.instructions.md`)
2. **Parse metadata** (YAML frontmatter, applyTo, tags)
3. **Apply format transformation** based on target tool:
   - `.claude/` — convert YAML to Claude XML metadata
   - `.gemini/` — convert to JSON or plain markdown
   - `.chatgpt/` — extract text, remove YAML, add as plain instruction
4. **Write to target** with appropriate naming/structure
5. **Log result** (success, skipped, error)

**Output:**

```text
Sync Report
===========
Source: .github/
Targets: .claude/, .gemini/, .chatgpt/

SYNC RESULTS
  .claude/:
    ✅ instructions/change-completeness.instructions.md
    ✅ instructions/md-formatting.instructions.md
    ⚠️ instructions/java.instructions.md (format adapted)
    ⏭️ skills/ (skipped — already up-to-date)

  .gemini/:
    ✅ custom-instructions/core-principles.txt
    ⚠️ system-prompts/default.txt (converted from markdown)

  .chatgpt/:
    ✅ custom-instructions/general.txt
    ⚠️ gpt-builder/learning-assistant-gpt.json (updated)

SUMMARY: 9 synced, 2 skipped, 0 errors
```

---

### Script 2 — `validate-customization.ps1` — Validate consistency across tools

**Purpose:** Check that all tool customization directories are valid and consistent

**When to run:**
- Before committing
- During CI/CD pipeline (gate for merging)
- Manual validation: `.\validate-customization.ps1 -All`

**Functionality:**

```powershell
.\validate-customization.ps1 [OPTIONS]

OPTIONS:
  -All                  Validate all tool directories
  -Tool <name>          Validate specific tool (github, claude, gemini, chatgpt)
  -Deep                 Perform deep validation (includes file content checks)
  -Strict               Fail on warnings; default is warnings-only
  -Report <path>        Write detailed HTML/JSON report
  -Verbose              Show all checks

EXAMPLES:
  # Quick validation of all tools
  .\validate-customization.ps1 -All

  # Deep validation with HTML report
  .\validate-customization.ps1 -All -Deep -Report ./validation-report.html

  # Validate one tool, fail on warnings
  .\validate-customization.ps1 -Tool claude -Strict
```

**Validation Checks:**

For each tool directory, validate:

1. **Structure** — expected directories exist (instructions/, prompts/, skills/, agents/)
2. **Files** — all expected files present and not corrupted
3. **Metadata** — required frontmatter fields present and valid
4. **Format** — files conform to tool-specific format (valid YAML, JSON, XML, etc.)
5. **Links** — all markdown links point to existing files
6. **Size** — files don't exceed tool-specific limits (token count, character count)
7. **Consistency** — same instruction exists in `.github/` AND tool directory
8. **Naming** — filenames follow expected conventions
9. **Content** — no obvious copy-paste errors or corrupted content
10. **Encoding** — files are UTF-8, line endings are LF (or CRLF on Windows)

**Output:**

```text
Validation Report
=================

✅ GITHUB (.github/)
  ✅ Structure: OK (9 instruction files, 8 prompt files, 12 skill files)
  ✅ Metadata: OK (all files have required frontmatter)
  ✅ Format: OK (valid YAML, markdown)
  ✅ Links: OK (all internal links valid)
  ✅ Size: OK (largest file: 12KB)
  SUMMARY: PASS

✅ CLAUDE (.claude/)
  ✅ Structure: OK (9 instruction files, 8 prompt files, 12 skill files)
  ✅ Metadata: OK (Claude XML format valid)
  ⚠️ Consistency: 1 warning
    - .claude/instructions/java.instructions.md
    - Different from source (.github/instructions/java.instructions.md)
    - Last synced: 2026-07-15 (16 days ago)
    - Recommendation: Run sync-customization.ps1 to update
  ✅ Format: OK (valid markdown + Claude XML)
  ✅ Size: OK (largest file: 15KB)
  SUMMARY: PASS (with warnings)

⚠️ GEMINI (.gemini/)
  ✅ Structure: OK
  ⚠️ Files: 2 warnings
    - custom-instructions/ has 5 files; expected 6
    - system-prompts/default.txt missing
  ⚠️ Metadata: OK (but some fields are tool-specific)
  ✅ Format: OK
  ✅ Links: OK
  ❌ Size: FAIL
    - custom-instructions/core-principles.txt: 2.5KB (over 1.5KB limit)
    - Recommendation: Shorten or split into multiple files
  SUMMARY: FAIL

❌ CHATGPT (.chatgpt/)
  ❌ Structure: FAIL
    - Missing: gpt-builder/ directory
    - Missing: setup-templates/ directory
  SUMMARY: FAIL (critical)

OVERALL: 2 PASS, 1 PASS-WARNINGS, 2 FAIL
```

---

### Script 3 — `check-drift.ps1` — Monitor sync drift over time

**Purpose:** Track whether tool directories are drifting from `.github/` source

**When to run:**
- Periodic check (e.g., weekly report)
- Pre-commit hook (optional)
- In CI/CD (warning only, doesn't fail)

**Functionality:**

```powershell
.\check-drift.ps1 [OPTIONS]

OPTIONS:
  -Days <n>             Flag files not synced in N days (default: 14)
  -Report <path>        Write drift report
  -Email <address>      Email report to this address
  -Slack <webhook>      Post summary to Slack

EXAMPLES:
  # Check for files not synced in 14 days
  .\check-drift.ps1

  # Check for files not synced in 7 days, email report
  .\check-drift.ps1 -Days 7 -Email team@example.com
```

**Output:**

```text
Drift Report
============
Generated: 2026-07-31 15:30:00
Threshold: 14 days (files not synced in this long are flagged)

⚠️ DRIFT DETECTED in .claude/

  instructions/java.instructions.md
    Last synced: 2026-07-15 (16 days ago)
    Last modified in .github/: 2026-07-28 (3 days ago)
    Recommendation: Run sync-customization.ps1

  prompts/research.prompt.md
    Last synced: 2026-07-10 (21 days ago)
    Last modified in .github/: 2026-07-10 (same)
    Recommendation: No changes needed, but consider a resync for consistency

✅ GEMINI (.gemini/)
  No drift detected (all files current)

❌ CHATGPT (.chatgpt/)
  Directory validation failed; run validate-customization.ps1 -Tool chatgpt

SUMMARY: 2 files drifting, 1 directory needs validation
```

---

### Script 4 — `transform-for-tool.ps1` — Apply format transformations

**Purpose:** Helper script for applying tool-specific format transformations

**Used by:** sync-customization.ps1 (internal), and manual use if needed

**Functionality:**

```powershell
.\transform-for-tool.ps1 -InputFile <path> -TargetTool <tool> [-OutputFile <path>]

TARGET TOOLS:
  - github    (identity transform — no-op)
  - claude    (YAML → Claude XML + markdown)
  - gemini    (Markdown → JSON schema)
  - chatgpt   (Markdown → plain text + JSON)

EXAMPLES:
  # Transform instruction for Claude
  .\transform-for-tool.ps1 -InputFile .github/instructions/java.md -TargetTool claude -OutputFile .claude/instructions/java.md

  # Transform prompt for ChatGPT, output to stdout
  .\transform-for-tool.ps1 -InputFile .github/prompts/research.md -TargetTool chatgpt
```

**Transformation Examples:**

**Input (GitHub/Copilot format):**

```markdown
---
applyTo: "**/*.java"
name: Java Code Style
---

# Java Code Style

Follow Google Java Style Guide...
```

**Output (Claude format):**

```markdown
# Java Code Style

<claude-config>
<applies-to>**/*.java</applies-to>
<format>copilot</format>
</claude-config>

Follow Google Java Style Guide...
```

---

## Implementation Plan

### Phase 1 — Core Scripts (This BLI)

- [ ] `sync-customization.ps1` — full implementation
- [ ] `validate-customization.ps1` — full implementation
- [ ] `check-drift.ps1` — full implementation
- [ ] `transform-for-tool.ps1` — helper/adapter functions
- [ ] `lib/Transformers.ps1` — modular transformation functions for each tool
- [ ] `lib/Validators.ps1` — modular validation functions

### Phase 2 — Testing & Integration (BLI-098)

- [ ] Unit tests for each script
- [ ] Integration tests (full workflow)
- [ ] CI/CD integration
- [ ] Documentation (built-in help, man pages)

### Phase 3 — Advanced Features (Future)

- [ ] Visual dashboard showing sync status
- [ ] Slack/email notifications
- [ ] Automated scheduled syncs
- [ ] Git pre-commit hooks
- [ ] Web UI for managing tool configurations

---

## Directory Structure for Scripts

```text
tools/
├── sync-customization.ps1
├── validate-customization.ps1
├── check-drift.ps1
├── transform-for-tool.ps1
├── lib/
│   ├── Transformers.ps1        (ClaudeTransformer, GeminiTransformer, ChatGptTransformer)
│   ├── Validators.ps1          (ValidateStructure, ValidateMetadata, CheckSize, etc.)
│   └── Common.ps1              (utility functions: logging, file I/O, config reading)
├── config/
│   ├── tool-specs.json         (tool-specific limits, naming rules, format specs)
│   └── transformation-rules.json (rules for YAML → tool-specific format)
├── tests/
│   ├── test-sync.ps1
│   ├── test-validate.ps1
│   └── fixtures/               (sample files for testing)
└── README.md                   (script documentation)
```

---

## Configuration File: tool-specs.json

```json
{
  "tools": {
    "github": {
      "name": "GitHub Copilot",
      "directory": ".github",
      "structureDirs": ["instructions", "prompts", "skills", "agents", "docs"],
      "format": "yaml+markdown",
      "customInstructionLimit": null,
      "fileFormat": ".md",
      "validation": {
        "requireFrontmatter": true,
        "maxFileSize": "100KB"
      }
    },
    "claude": {
      "name": "Cursor / Claude",
      "directory": ".claude",
      "structureDirs": ["instructions", "prompts", "skills", "agents"],
      "format": "markdown+claude-xml",
      "customInstructionLimit": 50000,
      "fileFormat": ".md",
      "validation": {
        "requireMetadata": true,
        "maxFileSize": "50KB"
      }
    },
    "gemini": {
      "name": "Google Gemini",
      "directory": ".gemini",
      "structureDirs": ["custom-instructions", "system-prompts", "api-context", "extensions"],
      "format": "json+markdown",
      "customInstructionLimit": 1500,
      "fileFormat": ".txt | .json",
      "validation": {
        "requireMetadata": false,
        "maxFileSize": "20KB"
      }
    },
    "chatgpt": {
      "name": "ChatGPT / OpenAI",
      "directory": ".chatgpt",
      "structureDirs": ["custom-instructions", "gpt-builder", "assistants-api", "actions"],
      "format": "json+yaml+markdown",
      "customInstructionLimit": 8000,
      "fileFormat": ".txt | .json | .yaml",
      "validation": {
        "requireMetadata": false,
        "maxFileSize": "50KB"
      }
    }
  }
}
```

---

## Acceptance Criteria

- [ ] `sync-customization.ps1` implemented and tested
  - [ ] Syncs files from .github to all tools
  - [ ] Applies format transformations correctly
  - [ ] Dry-run mode works
  - [ ] Verbose logging works
  - [ ] Handles errors gracefully

- [ ] `validate-customization.ps1` implemented and tested
  - [ ] Validates structure for all tools
  - [ ] Checks metadata and format
  - [ ] Reports file size violations
  - [ ] Flags consistency issues
  - [ ] Generates readable reports (text + optional HTML/JSON)

- [ ] `check-drift.ps1` implemented and tested
  - [ ] Detects files not synced in N days
  - [ ] Generates drift reports
  - [ ] Integrates with email/Slack (optional)

- [ ] `transform-for-tool.ps1` implemented and tested
  - [ ] Transforms YAML → Claude format
  - [ ] Transforms Markdown → Gemini JSON
  - [ ] Transforms Markdown → ChatGPT text
  - [ ] Handles edge cases gracefully

- [ ] `lib/Transformers.ps1` — all transformation functions
- [ ] `lib/Validators.ps1` — all validation functions
- [ ] `tool-specs.json` — configuration for all tools
- [ ] `tools/README.md` — script documentation
- [ ] All scripts have built-in help (`-Help` flag)
- [ ] All scripts are idempotent (safe to run multiple times)
- [ ] Error handling is comprehensive (no silent failures)
- [ ] Logging is clear and actionable

---

## Testing Checklist

- [ ] **Sync test 1:** Modify `.github/instructions/java.md`, run sync, verify `.claude/` updates
- [ ] **Sync test 2:** Run sync twice; verify idempotent (no unnecessary changes on 2nd run)
- [ ] **Sync test 3:** Add new file to `.github/instructions/`, run sync, verify appears in all tools
- [ ] **Validate test 1:** Run validation on fresh `.github/`; expect all pass
- [ ] **Validate test 2:** Intentionally break a file (invalid YAML), run validate, verify detected
- [ ] **Validate test 3:** Validate `.claude/` after drift; verify warnings reported
- [ ] **Drift test 1:** Flag files not synced in 7 days; verify accuracy
- [ ] **Transform test 1:** Transform instruction to Claude format, verify XML metadata added
- [ ] **Transform test 2:** Transform prompt to Gemini format, verify JSON output
- [ ] **Integration test:** Full workflow: modify `.github/`, run sync, run validate, check drift

---

## Dependencies & Blockers

- **Blocked by:** BLI-091, BLI-092, BLI-093, BLI-094, BLI-095 (tool directories must exist)
- **Unblocks:** BLI-098 (testing) and CI/CD integration

---

## Notes

- Use PowerShell for cross-platform compatibility (Windows, macOS, Linux)
- Scripts should be defensive (handle missing files, invalid JSON gracefully)
- Comprehensive error messages (point users to the relevant file, line, issue)
- Consider creating a CLI wrapper (e.g., `manage-customization.ps1` with subcommands)
- Logs should be machine-parseable (JSON) for CI/CD integration
