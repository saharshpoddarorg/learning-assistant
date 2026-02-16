```prompt
---
name: learn-concept
description: 'Learn any software engineering or CS concept — language-agnostic, from fundamentals to advanced, with theory, practice, and real-world context'
agent: learning-mentor
tools: ['codebase', 'fetch']
---

## Concept
${input:concept:What concept do you want to learn? (e.g., TCP/IP, deadlocks, B-trees, CAP theorem, SOLID, event sourcing, virtual memory)}

## Domain (optional)
${input:domain:Which domain does this belong to? (e.g., DSA, system design, OS, networking, DBMS, testing, concurrency, OOP, architecture — or leave blank for auto-detect)}

## Current Level
${input:level:What's your current understanding? (beginner / intermediate / advanced / just-heard-of-it)}

## Preferred Language (optional)
${input:language:What language for code examples? (e.g., Java, Python, Go, C, Rust — or leave blank for pseudocode + best-fit language)}

## Instructions

Teach me this concept thoroughly, regardless of language or domain. Adapt the explanation to fit the concept's nature — some concepts are best explained with code, others with diagrams, and others with analogies.

### 1. Concept Introduction
- **One-sentence definition** — no jargon, no qualifications
- **Domain placement** — where does this concept fit? (e.g., "This is a networking concept in the transport layer" or "This is a creational design pattern")
- **Real-world analogy** — something from everyday life
- **Why it matters** — what breaks or becomes painful without this concept?

### 2. Core Explanation
- **How it works** — the mental model, step by step
- **Visual representation** — ASCII diagram, table, or flowchart if the concept is spatial/structural
- **Key terminology** — 3-5 essential terms with one-sentence definitions
- **The problem it solves** — show the "before" (pain) and "after" (with this concept)

### 3. Concrete Example
- **Code or demonstration** — working example in the learner's preferred language (or pseudocode if language-agnostic)
- **Step-by-step walkthrough** — explain what each part does and why
- **Anti-example** — what happens when you do it wrong (or don't use it at all)

### 4. Where You'll See This
- **Real-world systems** — which products, services, or architectures use this? (e.g., "Redis uses this for...", "Linux kernel implements this in...")
- **Industry context** — how do professionals talk about and apply this day-to-day?
- **Interview relevance** — is this commonly asked in interviews? If so, what angles?

### 5. Connections & Comparisons
- **Related concepts** — what should I learn before/after this?
- **Common confusions** — concepts that sound similar but are different (e.g., concurrency vs parallelism)
- **Trade-offs** — when would you choose an alternative approach?

### 6. Go Deeper
- **Recommended resource** — ONE best resource for learning more (official docs, book chapter, or tutorial)
- **Quick quiz** — 2-3 questions to test understanding
- **Hands-on exercise** — a small task that forces genuine application, not just recall
- **What to learn next** — exactly one follow-up concept

### Rules
- **Language-agnostic by default** — don't assume Java, Python, or any specific language unless the learner specifies one
- **Domain-aware** — adapt the teaching style to the domain (DSA needs complexity analysis, networking needs protocol diagrams, OS needs process/memory models)
- Concepts that span theory and practice should include BOTH conceptual explanation AND working code
- For concepts with historical context (e.g., why REST replaced SOAP, why NoSQL emerged), briefly explain the evolution
- Use `fetch` to retrieve authoritative documentation when beneficial
- Cite the best canonical source (RFC for protocols, textbook for CS theory, official docs for tools)
- Always end with exactly ONE "what to learn next" recommendation
```
