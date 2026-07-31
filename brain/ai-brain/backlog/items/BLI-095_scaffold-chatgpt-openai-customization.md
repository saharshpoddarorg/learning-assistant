---
id: BLI-095
title: Scaffold ChatGPT / OpenAI Assistants customization pattern (.chatgpt/ directory)
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
tags: [implementation, chatgpt, openai, assistants, gpt-builder, customization, scaffold]
origin-type: manual
import-batch: null
source-file: null
---

# BLI-095: Feature — Scaffold ChatGPT / OpenAI Assistants customization pattern

## Description

Create a `.chatgpt/` directory structure that demonstrates how to use learning-assistant
customization content with ChatGPT and OpenAI Assistants. Like Gemini, ChatGPT has
different customization patterns (Custom Instructions, GPT Builder, Actions/Plugins).
This BLI scaffolds templates and documentation showing how users can adapt our content.

### Goals

1. Document how ChatGPT supports customization (Custom Instructions, GPT Builder, Actions)
2. Create `.chatgpt/` directory with template files and configuration examples
3. Show how to adapt instructions/prompts to ChatGPT's format
4. Provide JSON examples for GPT Builder and OpenAI Assistant API
5. Document ChatGPT Actions pattern (integrating external APIs)
6. Provide setup guide for ChatGPT users

### Scope

- ✅ `.chatgpt/` directory structure (template scaffolding)
- ✅ Custom Instructions templates (text format for ChatGPT settings)
- ✅ GPT Builder configuration templates (if JSON-based)
- ✅ OpenAI Assistant API examples (JSON config)
- ✅ Actions/Plugins integration guide
- ✅ `.chatgpt/README-chatgpt-setup.md` — user-friendly setup guide
- 🔲 Full content migration (template-based, manual adaptation)
- 🔲 Sync scripts (limited; ChatGPT is API-driven, less file-based)

---

## Understanding ChatGPT Customization

**From BLI-091 research:**

ChatGPT offers multiple customization approaches:

1. **Custom Instructions** — text field in settings (system prompts, goals, tone, constraints)
2. **GPT Builder** — visual UI to create custom GPTs with instructions, context, files, actions
3. **OpenAI Assistants API** — programmatic API to create assistants with tools, system prompts, knowledge files
4. **Actions** — define custom actions (REST API calls) that a GPT can invoke
5. **File Upload** — include knowledge files (PDFs, code, docs) in a GPT

### Why Multiple Approaches?

- **Custom Instructions** — simple, always available, for personal use
- **GPT Builder** — easy to share (publish as a public GPT or internal team version)
- **Assistants API** — for building custom applications and integrations
- **Actions** — advanced: integrating with external services (Jira, Slack, etc.)

---

## Directory Structure

```text
.chatgpt/
├── custom-instructions/
│   ├── general.txt                    (general coding instructions)
│   ├── java-development.txt           (Java-specific rules)
│   ├── architecture-review.txt        (for code architecture analysis)
│   ├── requirements-gathering.txt     (for requirements/BDD work)
│   └── README.md                      (guide: how to use each)
│
├── gpt-builder/
│   ├── learning-assistant-gpt.json   (full GPT config for GPT Builder)
│   ├── code-reviewer-gpt.json        (specialized GPT for code review)
│   ├── requirements-gpt.json         (specialized GPT for requirements)
│   └── README.md                      (how to import JSON into GPT Builder)
│
├── assistants-api/
│   ├── learning-assistant-config.json (Assistants API config)
│   ├── code-reviewer-assistant.json  (specialized assistant)
│   ├── integration-example.py        (Python example using Assistants API)
│   ├── integration-example.js        (Node.js example using Assistants API)
│   └── README.md                      (API integration guide)
│
├── actions/
│   ├── jira-action.yaml              (OpenAPI spec for Jira action)
│   ├── github-action.yaml            (OpenAPI spec for GitHub action)
│   ├── confluence-action.yaml        (OpenAPI spec for Confluence action)
│   └── README.md                      (how to create actions)
│
├── knowledge-files/
│   ├── codebase-overview.md          (what to include as knowledge)
│   ├── architecture-guide.md         (generated from repo docs)
│   └── README.md                      (how to prepare knowledge files)
│
├── setup-templates/
│   ├── setup-step-1-custom-instructions.md
│   ├── setup-step-2-gpt-builder.md
│   ├── setup-step-3-assistants-api.md
│   ├── setup-step-4-actions.md
│   └── setup-step-5-team-gpt.md
│
├── README-chatgpt-setup.md             (main entry point for users)
└── IMPLEMENTATION-NOTES.md             (for developers)
```

---

## Content Examples

### Example 1 — Custom Instructions for ChatGPT

**Source:** `.chatgpt/custom-instructions/java-development.txt`

```text
# Java Development Assistant

## Goals
You are an expert Java software engineer. Help me write clean, maintainable,
performant Java code following industry best practices.

## Code Quality Standards
- Follow Google Java Style Guide
- Apply SOLID principles (Single Responsibility, Open/Closed, Liskov, Interface Segregation, Dependency Inversion)
- Use Java 21+ modern features (records, sealed classes, text blocks, pattern matching)
- Prefer composition over inheritance
- Keep methods under 30 lines; each method does one thing

## Testing Approach
- Write tests alongside code (TDD)
- Aim for 70%+ code coverage
- Test edge cases, null inputs, and error paths
- Use descriptive test names following given-when-then pattern

## Communication Style
- Explain the WHY behind recommendations, not just the WHAT
- When suggesting a change, show a before-after code example
- Mention trade-offs (performance, readability, complexity)
- Reference industry standards (Google Java Style, Effective Java, etc.)
- Assume the user is intelligent but may not know Java latest features

## Project Context
This is the learning-assistant project — a Java Gradle multi-module project for
learning GitHub Copilot customization. Main modules:
- modules/search-engine — search indexing library
- modules/mcp-common — shared MCP server infrastructure
- modules/mcp-learning-resources — MCP server for learning resources
- modules/app — entry point and operational scripts

Use this context when answering questions about the codebase.
```

### Example 2 — GPT Builder Configuration (JSON)

**Source:** `.chatgpt/gpt-builder/learning-assistant-gpt.json`

```json
{
  "name": "Learning Assistant Code Expert",
  "description": "Expert help with the learning-assistant Java project. Provides code review, architecture guidance, and best practices.",
  "instructions": "You are an expert Java software engineer specializing in the learning-assistant project...",
  "tools": [
    {
      "type": "retrieval",
      "retrieval": {
        "max_chunks_per_query": 10
      }
    },
    {
      "type": "code_interpreter"
    }
  ],
  "file_ids": [
    "file-abc123...",
    "file-def456..."
  ],
  "model": "gpt-4-turbo",
  "top_p": 1.0,
  "temperature": 0.7,
  "functions": [
    {
      "name": "query_codebase",
      "description": "Search the learning-assistant codebase for files matching a pattern",
      "parameters": {
        "type": "object",
        "properties": {
          "pattern": {
            "type": "string",
            "description": "File name or path pattern (e.g., *.java, SearchEngine)"
          }
        }
      }
    }
  ]
}
```

### Example 3 — Assistants API Configuration

**Source:** `.chatgpt/assistants-api/learning-assistant-config.json`

```json
{
  "name": "Learning Assistant Code Expert",
  "description": "Analyze and improve learning-assistant code",
  "instructions": "You are an expert Java developer...",
  "model": "gpt-4-turbo-preview",
  "tools": [
    {
      "type": "retrieval"
    },
    {
      "type": "code_interpreter"
    }
  ],
  "file_ids": [],
  "metadata": {
    "project": "learning-assistant",
    "version": "1.0",
    "created_date": "2026-07-31"
  }
}
```

**How to use (Python):**

```python
from openai import OpenAI

client = OpenAI(api_key="sk-...")

# Load config
with open('.chatgpt/assistants-api/learning-assistant-config.json') as f:
    config = json.load(f)

# Create assistant
assistant = client.beta.assistants.create(**config)

# Use the assistant
thread = client.beta.threads.create()
message = client.beta.threads.messages.create(
    thread_id=thread.id,
    role="user",
    content="Review this Java code for SOLID violations"
)
```

### Example 4 — Actions Configuration (OpenAPI Spec)

**Source:** `.chatgpt/actions/jira-action.yaml`

```yaml
openapi: 3.0.0
info:
  title: Jira Integration
  version: 1.0.0
  description: Create issues, search, update tickets in Jira

servers:
  - url: https://your-jira-instance.atlassian.net

paths:
  /rest/api/3/issues:
    post:
      summary: Create a Jira issue
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              properties:
                fields:
                  type: object
                  properties:
                    summary:
                      type: string
                    description:
                      type: string
                    issuetype:
                      type: object
                      properties:
                        name:
                          type: string
      responses:
        '201':
          description: Issue created successfully

  /rest/api/3/search:
    get:
      summary: Search for Jira issues
      parameters:
        - name: jql
          in: query
          schema:
            type: string
      responses:
        '200':
          description: Search results
```

---

## Setup Guide Outline

**`.chatgpt/README-chatgpt-setup.md`** contains:

1. **Overview** — ChatGPT customization options, when to use each
2. **Approach 1 — Custom Instructions (Easiest)**
   - Screenshots showing settings location
   - Copy-paste example from `.chatgpt/custom-instructions/`
   - Limitations (applies to all chats, no sharing)

3. **Approach 2 — GPT Builder (Easy + Sharable)**
   - Step-by-step: create custom GPT, copy instructions, upload files
   - How to use JSON configs (import into GPT Builder)
   - How to share the GPT (public or team/internal)

4. **Approach 3 — Assistants API (For Developers)**
   - Requires OpenAI API key
   - Examples in Python and JavaScript
   - How to build an application using Assistants API

5. **Approach 4 — Actions (Advanced Integration)**
   - Create custom actions (Jira, GitHub, Confluence)
   - OpenAPI spec basics
   - How to test actions in GPT Builder

6. **Approach 5 — Team GPT (Enterprise)**
   - Sharing GPTs within organization
   - Managing team custom GPTs
   - Versioning and updates

7. **Troubleshooting**
   - Custom instructions not working?
   - Files not uploading?
   - Actions not triggering?
   - API rate limits?

---

## Implementation Tasks

### Phase 1 — Scaffolding (This BLI)

- [ ] Create `.chatgpt/` directory structure
- [ ] Write `.chatgpt/README-chatgpt-setup.md` (all 5 approaches)
- [ ] Create custom instruction templates (3+ specializations)
- [ ] Create GPT Builder config examples (JSON)
- [ ] Create Assistants API examples (JSON + Python/JS code)
- [ ] Create sample Actions configs (OpenAPI YAML)
- [ ] Write setup step-by-step guides
- [ ] Write IMPLEMENTATION-NOTES.md

### Phase 2 — Content Population & Testing (Future)

- [ ] Test custom instructions in ChatGPT
- [ ] Create and publish a public Learning Assistant GPT
- [ ] Test Assistants API examples with real API
- [ ] Create sample actions and test in GPT Builder

---

## Acceptance Criteria

- [ ] `.chatgpt/` directory structure created with all template folders
- [ ] `README-chatgpt-setup.md` written (≥2000 words, all 5 approaches covered)
- [ ] At least 3 custom instruction templates created
- [ ] At least 2 GPT Builder config examples (JSON)
- [ ] Assistants API examples in Python and Node.js
- [ ] Sample Actions (at least 2) in OpenAPI format
- [ ] Knowledge file templates documented
- [ ] All markdown files pass formatting validation
- [ ] Links to OpenAI docs are accurate and current
- [ ] Setup guide can be followed without prior OpenAI/ChatGPT API experience
- [ ] IMPLEMENTATION-NOTES.md explains how to extend

---

## Resources & Links

- [ChatGPT](https://chatgpt.com/)
- [OpenAI Assistants API](https://platform.openai.com/docs/assistants)
- [GPT Builder](https://openai.com/blog/gpts/)
- [OpenAI Actions](https://platform.openai.com/docs/guides/gpts/actions)
- [Custom Instructions Help](https://help.openai.com/en/articles/8770868-custom-instructions)
- [OpenAPI 3.0 Specification](https://spec.openapis.org/oas/v3.0.3)

---

## Notes

- ChatGPT is evolving; stay current with OpenAI announcements
- Custom Instructions are simpler; GPT Builder adds shareability
- Assistants API is most powerful but requires development
- Actions require OpenAPI knowledge; provide good examples
- Consider creating one public "Learning Assistant" GPT as a showcase
