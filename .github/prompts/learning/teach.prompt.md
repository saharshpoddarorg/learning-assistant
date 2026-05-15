```prompt
---
name: teach
description: 'Teach a Java/software engineering concept from the current file using theory, analogies, and code examples'
agent: learning-mentor
tools: ['codebase']
---

Teach me the concepts used in this file:
${file}

## Instructions
1. **Identify** all Java and software engineering concepts in this file
2. **Explain each** — what it is, why it exists, analogy, working code example
3. **Show the pain** — what would this code look like WITHOUT each concept?
4. **Common mistakes** — what do beginners get wrong with these concepts?
5. **Quiz** — 2-3 questions to check my understanding
6. **Next step** — one concept to learn next based on what's in this file
```
