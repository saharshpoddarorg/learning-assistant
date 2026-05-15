```prompt
---
name: impact
description: 'Analyze the impact and ripple effects of changing a specific file or method'
agent: impact-analyzer
tools: ['codebase', 'usages', 'search', 'findTestFiles']
---

Analyze the impact of changes to:
${file}

## Instructions
1. **Map direct callers** — who directly uses this code?
2. **Map transitive dependencies** — go at least 2 levels deep
3. **Assess affected areas** — functionality, tests, APIs, data, config, docs
4. **Rate risks** — High / Medium / Low for each affected component
5. **Check test coverage** — which tests exist? which are missing?
6. **Recommend approach** — step-by-step implementation order that minimizes risk
```
