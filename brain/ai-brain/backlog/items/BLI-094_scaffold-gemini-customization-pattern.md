---
id: BLI-094
title: Scaffold Google Gemini customization pattern (.gemini/ directory)
status: todo
priority: medium
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
estimated-effort: M
actual-effort: null
tags: [implementation, gemini, google, customization, scaffold, instructions, extensions]
origin-type: manual
import-batch: null
source-file: null
---

# BLI-094: Feature — Scaffold Google Gemini customization pattern

## Description

Create a `.gemini/` directory structure that demonstrates how to use learning-assistant
customization content with Google Gemini. Unlike Cursor/Claude (BLI-093) which has
native customization files, Gemini is primarily web-based and has different integration
patterns. This BLI scaffolds templates and documentation showing how users can adapt
our instructions/prompts for Gemini use.

### Goals

1. Document how Gemini supports customization (custom instructions, extensions, context API)
2. Create `.gemini/` directory with template files and examples
3. Show how to adapt instructions/prompts to Gemini's format
4. Document Gemini API integration patterns
5. Provide setup guide for Gemini users

### Scope

- ✅ `.gemini/` directory structure (template scaffolding only)
- ✅ Gemini custom instructions templates (how to format for Gemini)
- ✅ Gemini system prompt examples
- ✅ Gemini extensions documentation (if supported)
- ✅ API context integration guide (if applicable)
- ✅ `.gemini/README-gemini-setup.md` — user-friendly setup guide
- 🔲 Full content migration (manual per-user choice, not automated)
- 🔲 Sync scripts (Gemini is web-based; limited automation possible)

---

## Understanding Gemini Customization

**From BLI-091 research:**

Gemini's customization approach differs significantly from Copilot/Cursor:

- **Primary interface:** Web-based chat (not IDE)
- **Custom instructions:** Text field in settings (≤1500 chars typical)
- **Context API:** Programmatic way to inject documents/code into chat
- **Extensions:** (If available in Gemini) way to integrate external APIs
- **No native file directory:** Unlike `.github/` or `.claude/`, Gemini doesn't load from a folder

### Customization Approaches for Gemini

1. **Manual copy-paste** — user copies instruction text from `.gemini/` into Gemini settings
2. **Chrome extension** (future) — extension reads `.gemini/` files and injects into Gemini
3. **API-based** — use Gemini API to programmatically set custom instructions
4. **Bookmarklet** (future) — browser bookmarklet to inject instructions

---

## Directory Structure

```text
.gemini/
├── custom-instructions/
│   ├── full-system-prompt.txt         (complete system context)
│   ├── core-principles.txt            (brief core rules)
│   ├── codebase-context.txt           (project overview)
│   └── README.md                      (how to use each)
│
├── system-prompts/
│   ├── default.txt                    (general-purpose system prompt)
│   ├── code-analysis.txt              (specialized for code review)
│   ├── requirements-gathering.txt     (specialized for BDD)
│   └── learning-tutor.txt             (specialized for teaching)
│
├── api-context/
│   ├── context-injection-example.js   (how to inject via Gemini API)
│   ├── codebase-summary.json          (metadata about the repo)
│   └── README.md                      (API integration guide)
│
├── extensions/
│   ├── custom-instruction-injector/   (if Gemini supports extensions)
│   └── README.md                      (how to build Gemini extensions)
│
├── setup-templates/
│   ├── setup-step-1-basic.md          (basic custom instructions)
│   ├── setup-step-2-advanced.md       (full system prompt)
│   ├── setup-step-3-api-context.md    (programmatic integration)
│   └── setup-step-4-extensions.md     (advanced: extensions)
│
├── README-gemini-setup.md             (main entry point for users)
└── IMPLEMENTATION-NOTES.md            (for developers)
```

---

## Content Examples

### Example 1 — Custom Instructions for Gemini

**Source:** `.gemini/custom-instructions/core-principles.txt`

```text
You are an expert software engineer. Follow these principles:

1. Code Quality
   - Always follow SOLID principles
   - Write self-documenting code (clear names, minimal comments)
   - Prefer composition over inheritance

2. Testing
   - Write tests alongside code
   - Aim for 70%+ coverage
   - Test edge cases and error paths

3. Communication
   - Explain the WHY, not just the WHAT
   - Assume the user is intelligent but may not know this domain
   - Provide examples and trade-off analysis

4. Standards
   - Use industry best practices (Conventional Commits, SemVer, RESTful API design)
   - Reference official documentation when available
   - Prefer boring technology over novel approaches
```

**How to use:** Copy this text into Gemini settings → "Custom Instructions"

### Example 2 — System Prompt for API Integration

**Source:** `.gemini/system-prompts/code-analysis.txt`

```markdown
# Code Analysis Expert System Prompt

You are analyzing code for quality, patterns, and potential improvements.

## Context
This is the learning-assistant project — a Java Gradle multi-module project for
learning GitHub Copilot customization.

## Key Files
- modules/search-engine/src/main/java/search/SearchEngine.java
- modules/mcp-common/src/main/java/mcp/McpServerRegistry.java
- modules/mcp-learning-resources/src/main/java/server/learningresources/LearningResourceServer.java

## Constraints
- Java 21+ (use modern features: records, sealed classes, text blocks)
- Follow Google Java Style Guide
- Maintain single responsibility principle
- No external dependencies except what's in build.gradle.kts

## Analysis Approach
1. Identify SOLID violations
2. Spot code smells (duplication, dead code, long methods)
3. Suggest patterns (Builder, Strategy, Observer, etc.)
4. Explain trade-offs for each suggestion
5. Provide code snippets showing improvements
```

### Example 3 — API Context Template

**Source:** `.gemini/api-context/context-injection-example.js`

```javascript
// Example: How to inject project context into Gemini via API
// (Requires Gemini API access and client library)

async function injectProjectContext() {
  const contextFiles = [
    'README.md',
    'modules/mcp-learning-resources/README.md',
    '.github/copilot-instructions.md',
    'brain/ai-brain/pkm-philosophy.md'
  ];

  const context = await readFiles(contextFiles);

  const message = {
    systemInstruction: {
      parts: [
        {
          text: readFile('.gemini/system-prompts/code-analysis.txt')
        },
        {
          text: `Project context:\n${context}`
        }
      ]
    },
    contents: [
      {
        parts: [{ text: userQuestion }]
      }
    ]
  };

  return await geminiClient.generateContent(message);
}
```

---

## Setup Guide Outline

**`.gemini/README-gemini-setup.md`** contains:

1. **Overview** — what Gemini is, why use it, what customization is possible
2. **Approach 1 — Manual Custom Instructions**
   - Screenshot showing settings location
   - Copy-paste example from `.gemini/custom-instructions/`
   - Verification steps
   - Limitations (character limit, frequency of updates)

3. **Approach 2 — Full System Prompt (Advanced)**
   - When to use vs. custom instructions
   - How to craft a comprehensive system prompt
   - Example from `.gemini/system-prompts/`

4. **Approach 3 — API Integration (For Developers)**
   - Requires Gemini API access
   - Example code from `.gemini/api-context/`
   - How to use with your own scripts

5. **Approach 4 — Extensions (If Available)**
   - Building a simple Gemini extension
   - Resources and documentation

6. **Troubleshooting**
   - Character limits exceeded?
   - Changes not taking effect?
   - API quota limits?

---

## Implementation Tasks

### Phase 1 — Scaffolding (This BLI)

- [ ] Create `.gemini/` directory structure
- [ ] Write `.gemini/README-gemini-setup.md` with all 4 approaches
- [ ] Create template files in `custom-instructions/` and `system-prompts/`
- [ ] Write setup step-by-step guides
- [ ] Create API context example with explanations
- [ ] Write IMPLEMENTATION-NOTES.md for future developers

### Phase 2 — Content Population (Future BLI or manual work)

- [ ] Adapt instructions from `.github/instructions/` to Gemini format
- [ ] Create specialized system prompts for different roles/tasks
- [ ] Develop Chrome extension (if feasible)
- [ ] Create Python script to sync content to Gemini via API

### Phase 3 — Testing & Documentation (BLI-098)

- [ ] Test custom instructions in Gemini web interface
- [ ] Verify API examples work with Gemini API
- [ ] Document any Gemini limitations or quirks
- [ ] Update setup guide based on testing

---

## Acceptance Criteria

- [ ] `.gemini/` directory structure created with all template folders
- [ ] `README-gemini-setup.md` written (≥1500 words, 4+ approaches covered)
- [ ] At least 3 custom instruction examples created (different specializations)
- [ ] System prompt templates created for code analysis and learning modes
- [ ] API context integration example documented and tested (pseudocode OK)
- [ ] All markdown files pass formatting validation (`__md_lint.ps1`)
- [ ] Links to Gemini official docs are accurate and current
- [ ] Setup guide can be followed without prior Gemini experience
- [ ] IMPLEMENTATION-NOTES.md explains how to add more content

---

## Resources & Links

- [Google Gemini](https://gemini.google.com/)
- [Gemini API Documentation](https://ai.google.dev/docs)
- [Gemini Custom Instructions](https://support.google.com/gemini) (if feature exists)
- [Gemini Extensions](https://support.google.com/chromebook/answer) (if applicable)

---

## Notes

- Gemini is a rapidly evolving product; this scaffolding is **template-based**, not automated
- No sync scripts in this BLI (Gemini is web-based, limited automation possible)
- Focus on **user education** — help users understand how to adapt our content for Gemini
- Keep examples practical and working (test before committing)
- Phase 2 (full content population) can be started only after Phase 1 is complete and reviewed
