```prompt
---
name: deep-dive
description: 'Deep-dive into any CS/SE concept — from theory through official docs to practical real-world usage, with progressive depth'
agent: learning-mentor
tools: ['codebase', 'fetch']
---

## Concept
${input:concept:What concept do you want to deep-dive into? (e.g., dependency injection, B-trees, TCP congestion control, virtual memory, CAP theorem, Strategy pattern)}

## Domain (optional)
${input:domain:Which domain? (e.g., DSA, system design, OS, networking, DBMS, testing, concurrency, OOP, architecture — or leave blank)}

## Current Level
${input:level:What's your current understanding? (beginner / intermediate / advanced / just-heard-of-it)}

## Preferred Language (optional)
${input:language:What language for code examples? (e.g., Java, Python, Go, C — or leave blank for best-fit language)}

## Instructions

Perform a **multi-layered deep-dive** into this concept. Progressively build understanding from intuition to mastery.

### Layer 1: Intuition (30 seconds)
- **One-sentence definition** — no jargon, no qualifications
- **Real-world analogy** — something the learner already understands
- **When you'd use it** — one concrete scenario in plain language

### Layer 2: Core Mechanics (5 minutes)
- **How it works** — the mental model, with a simple diagram if helpful
- **Minimal code example** — the simplest possible working code that demonstrates the concept (in the preferred language, or pseudocode for pure theory)
- **Key vocabulary** — 3-5 terms you need to know, each defined in one sentence
- **The problem it solves** — show code WITHOUT this concept (the pain), then WITH it (the relief)

### Layer 3: Official Documentation (10 minutes)
- **What the specification/standard says** — cite the relevant spec, RFC, standard, or official API docs
- **Official tutorial/guide coverage** — what the official resources teach about this, simplified
- **API surface or key interfaces** — key classes/interfaces/methods/syscalls involved, with brief purpose of each
- **What the docs emphasize** vs **what they underplay** — fill the gaps

### Layer 4: Real-World Patterns (15 minutes)
- **How professionals use it** — patterns and idioms from production code
- **Common architectures** — how this concept fits into real application design
- **Open-source examples** — 2-3 well-known projects that use this concept effectively, with specific class/file references
- **Industry conventions** — how teams typically apply this (naming, structuring, testing)

### Layer 5: Edge Cases & Mastery (for advanced)
- **What can go wrong** — pitfalls, subtle bugs, performance traps
- **Common misuse** — how people get it wrong and why
- **How it interacts with other concepts** — composition, conflicts, synergies
- **Historical context** — why was it created? What did it replace? How has thinking evolved?
- **Debate & alternatives** — any controversy? Are there situations where an alternative is better?

### Layer 6: Practice & Retention
- **Quick quiz** — 3 questions of increasing difficulty
- **Hands-on exercise** — a small project or code task that forces real understanding (not just syntax)
- **Reading list** — ordered: what to read first, second, third for continued learning
  - One official doc / spec / RFC
  - One blog/tutorial
  - One book chapter
  - One open-source codebase to study

### Rules
- **Start at the learner's declared level** — skip layers they already know, but briefly validate understanding
- **Language-adaptive** — use the learner's preferred language for code examples; if none specified, choose the best-fit language for the concept or use pseudocode
- Cite official documentation by specific URL, section number, or RFC number
- Show real open-source usage, not just toy examples
- For concepts that span theory and code (e.g., design patterns, algorithms), include BOTH
- For concepts that are pure theory (e.g., CAP theorem, ACID), use diagrams and scenarios instead of code
- If the concept has evolved over time (e.g., HTTP/1.1 → HTTP/2 → HTTP/3, or Java generics evolution), trace the evolution
- Make each layer self-contained — the learner can stop at any layer and still have a complete understanding for that depth
- End with exactly one "what to learn next" recommendation
```
