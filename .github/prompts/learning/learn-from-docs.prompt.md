```prompt
---
name: learn-from-docs
description: 'Learn any concept by studying official documentation — language specs, RFCs, API docs, standards — with simplified explanations and practical examples'
agent: learning-mentor
tools: ['codebase', 'fetch']
---

## Concept to Learn
${input:concept:What concept do you want to learn? (e.g., sealed classes, TCP handshake, B-tree indexing, HTTP/2, virtual memory, CompletableFuture)}

## Domain (optional)
${input:domain:Which domain? (e.g., Java, networking, OS, databases, web, protocols — or leave blank for auto-detect)}

## Instructions

Teach me the concept above by **grounding the explanation in official documentation**. Follow this structure:

### 1. Official Source
- Identify the **best official documentation** for this concept:
  - Language specs (JLS, Python docs, Go spec, Rust reference, etc.)
  - Standards / RFCs (IETF RFCs for protocols, W3C specs for web, IEEE for hardware/OS)
  - API docs (Javadoc, Python stdlib, MDN, man pages)
  - Official tutorials / guides (Oracle tutorials, dev.java, docs.python.org tutorial, Go tour)
  - Feature proposals (JEPs, PEPs, Go proposals, TC39 proposals)
- Provide the direct URL
- If a formal proposal exists (e.g., JEP 395, PEP 484, RFC 7540), reference it by number

### 2. What the Docs Say (Simplified)
- Summarize the official documentation's explanation **in plain language**
- Translate jargon — if the docs use terms like "nominally typed" or "product type," explain what they mean
- Highlight the **key points** the docs emphasize and why they matter

### 3. What the Docs Don't Tell You
- Practical gaps — things developers need to know that official docs gloss over
- Common gotchas that only appear in real usage
- Edge cases, limitations, or behavioral surprises
- Performance implications the docs don't cover

### 4. Code Examples — Docs vs Reality
- Show the **official example** from the docs (or a close reproduction)
- Then show a **realistic, practical example** of how this concept is actually used in real projects
- Explain the difference — why the doc examples are simplified and what changes in production code

### 5. How It Looks in Open Source
- Point to 1-2 well-known open-source projects that use this concept effectively
- Explain **what** they do with it and **why** their usage is exemplary
- Suggest specific files or packages to explore
- Projects can be in any language — choose ones that best demonstrate the concept

### 6. Related Official Documentation
- Link to related concepts in the official docs that pair with this one
- Suggest a reading order: "Read X first, then Y, then Z"

### 7. Practice
- One small coding exercise to apply the concept
- One challenge that pushes beyond the basics

### Rules
- Always cite the specific official documentation source
- When the official docs are unclear or overly formal, translate — don't just quote
- Distinguish between normative specs (JLS, RFCs, standards) vs informal guides (tutorials, blogs)
- Use working code in the relevant language, or pseudocode for theory-heavy concepts
- If the concept spans multiple technologies or protocols, cover the most relevant official source first
- If the concept has evolved over time (across language versions, protocol versions, or RFC revisions), note what changed and when
- Use `fetch` to retrieve official documentation pages when available
```
