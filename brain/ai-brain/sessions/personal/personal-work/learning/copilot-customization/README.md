# copilot-customization — Learning Sessions

> Captured AI sessions exploring GitHub Copilot's 6 customization primitives,
> layered architecture, and framework evolution for the learning-assistant project.

## Sessions

| Date | Subject | Complexity | Key Outcomes |
|---|---|---|---|
| 2026-04-21 | prompts-vs-skills | high | Dual pattern, fitness scorecard, creation framework, 5-layer architecture |
| 2026-04-21 | framework-evolution-guide | high | Full audit (23 skills, 65 prompts), gap analysis, 4-phase roadmap, skill flatness confirmed |
| 2026-04-21 | prompt-composition-superset | high | Prompts are standalone, `#file:` composition (Recipe 3), overlap patterns |

## Key Findings

- ALL 6 primitives are **flat and standalone** — no inheritance, no nesting
- Composition mechanisms: stacking (instructions), sub-files (skills), `#file:` (prompts), handoff (agents)
- 40/59 prompts follow the **dual pattern** (skill + prompt pair)
- 5-layer architecture: Instructions → Skills → Prompts → Agents → MCP

## Related

- **Backlog:** [BLI-026 — Evolve 6-primitive layered architecture](../../../../backlog/features/BLI-026_evolve-6-primitive-layered-architecture.md)
- **Backlog:** [BLI-002 — Hybrid skills + MCP + 5 primitives combo](../../../../backlog/features/BLI-002_hybrid-skills-mcp-primitives-combo.md)
