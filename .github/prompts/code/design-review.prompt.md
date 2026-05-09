```prompt
---
name: design-review
description: 'Perform a thorough design review of a file or component using SOLID, GRASP, and clean code principles'
agent: designer
tools: ['codebase', 'usages', 'search']
---

Perform a comprehensive design review of:
${file}

Evaluate the following dimensions:
1. **SOLID compliance** — check each principle (SRP, OCP, LSP, ISP, DIP)
2. **Coupling & cohesion** — how tightly connected? How focused are responsibilities?
3. **Design smells** — rigidity, fragility, immobility, viscosity, needless complexity
4. **Naming & abstractions** — do names reveal intent? Are abstraction levels consistent?
5. **Pattern opportunities** — where would a design pattern solve an existing problem?
6. **Testability** — can this be tested in isolation? What dependencies make it hard?

Rate each finding as Critical / Recommended / Consider.
Show concrete code improvements for each issue.
```
