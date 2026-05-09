---
name: explain
description: 'Explain the current file in plain, beginner-friendly language'
agent: ask
tools: ['codebase']
---

Explain this file in plain, beginner-friendly language.

## File

${file}

## Instructions

Provide a structured explanation:

1. **Purpose:** What does this file do? (1-2 sentences, plain language)
2. **Line-by-line walkthrough:** Go through the code section by section, explaining what each part does and why
3. **Java concepts used:** List any Java concepts (loops, methods, classes, arrays, etc.) with a one-sentence explanation of each
4. **How to run:** How would someone compile and execute this code?
5. **Improvements:** Suggest 2-3 things that could make this code better, with code examples

Keep your language simple — assume the reader is learning Java for the first time.
