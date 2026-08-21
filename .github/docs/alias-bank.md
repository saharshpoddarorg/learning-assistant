# Query Alias Bank

> **Purpose:** Maintain the shorthand vocabulary that Copilot should understand when
> interpreting user requests in this workspace.
> **Runtime hook:** The compact always-on instruction lives in
> [`query-aliases.instructions.md`](../instructions/query-aliases.instructions.md).

---

## How This Works

The alias framework has two layers:

| Layer | File | Role |
|---|---|---|
| Always-on behavior | [`query-aliases.instructions.md`](../instructions/query-aliases.instructions.md) | Tells Copilot to resolve known shorthand before acting |
| Maintained reference | `alias-bank.md` | Stores the fuller alias dictionary and update rules |

Keep the instruction file small because it is loaded broadly. Put new aliases, notes,
examples, and ambiguity rules in this file.

---

## Resolution Policy

1. Expand aliases only when the surrounding natural language supports the expansion.
2. Ask a clarifying question when an alias is ambiguous and the decision changes the task.
3. Preserve exact strings in code, commands, file names, branch names, issue keys, URLs,
   quoted text, and search queries.
4. Prefer domain-specific meanings over generic meanings when the request is about software
   engineering, Copilot customization, learning resources, or delivery workflows.
5. When adding an alias, include an expansion, domain, and ambiguity note.

---

## Core Software Engineering Aliases

| Alias | Expansion | Domain | Notes |
|---|---|---|---|
| `dev` | developer or development | General software work | Resolve from sentence role: "dev workflow" means development workflow; "ask dev" means developer. |
| `se` | software engineering | Software engineering | Use this for engineering practice, architecture, process, and learning contexts. |
| `pp` | pair programming | Collaboration | Use for driver/navigator workflows, joint problem solving, and pairing prompts. |
| `sdlc` | software development lifecycle | Process | Covers requirements, design, development, testing, release, and maintenance. |
| `qa` | quality assurance | Testing | Prefer tester/quality meaning in delivery contexts. |
| `po` | product owner | Product/process | Prefer agile role meaning in requirements and backlog contexts. |
| `ba` | business analyst | Product/process | Use for requirements elicitation and stakeholder analysis. |

---

## Design And Architecture Aliases

| Alias | Expansion | Domain | Notes |
|---|---|---|---|
| `hld` | high-level design | Architecture | System context, components, integrations, scaling, and deployment view. |
| `lld` | low-level design | Architecture | Classes, APIs, data structures, method contracts, and component internals. |
| `adr` | architecture decision record | Architecture | Use when documenting decisions, options, trade-offs, and consequences. |
| `api` | application programming interface | Architecture | Preserve literal casing when referring to API names or paths. |
| `db` | database | Data | Use for schema, persistence, indexing, and query design. |
| `nfr` | non-functional requirement | Requirements | Performance, security, reliability, usability, maintainability, and similar qualities. |

---

## Learning And Interview Aliases

| Alias | Expansion | Domain | Notes |
|---|---|---|---|
| `dsa` | data structures and algorithms | CS fundamentals | Use for interview prep, complexity, problem patterns, and practice. |
| `oop` | object-oriented programming | Programming | Classes, interfaces, encapsulation, inheritance, polymorphism, and composition. |
| `os` | operating systems | CS fundamentals | Ambiguous with "open source"; ask when the context is unclear. |
| `dbms` | database management systems | CS fundamentals | Broader than a single database product. |
| `cn` | computer networks | CS fundamentals | Ask when context could mean company-specific naming. |

---

## Delivery And Investigation Aliases

| Alias | Expansion | Domain | Notes |
|---|---|---|---|
| `rca` | root cause analysis | Debugging/investigation | Use for defect analysis, incidents, and production issue reviews. |
| `poc` | proof of concept | Research/delivery | Small experiment proving feasibility. |
| `mvp` | minimum viable product | Product/delivery | Smallest coherent product scope that validates value. |
| `ac` | acceptance criteria | Requirements/testing | Given/When/Then or checklist conditions for done behavior. |
| `dod` | definition of done | Agile delivery | Quality bar before work is considered complete. |
| `dor` | definition of ready | Agile delivery | Entry criteria before work starts. |

---

## Copilot And Knowledge-Workspace Aliases

| Alias | Expansion | Domain | Notes |
|---|---|---|---|
| `ghcp` | GitHub Copilot | Copilot customization | Use for Copilot product and customization discussions. |
| `mcp` | Model Context Protocol | Agent tooling | Use for MCP servers, tools, transport, config, and integrations. |
| `pkm` | personal knowledge management | Knowledge management | Notes, capture, organization, and retrieval systems. |
| `kb` | knowledge base | Knowledge management | Shared or personal reference corpus. |

---

## Ambiguous Aliases

| Alias | Possible Meanings | Default | Clarify When |
|---|---|---|---|
| `dev` | developer, development, development environment | Resolve from sentence role | The request could mean a person or an environment. |
| `os` | operating systems, open source | Operating systems in CS-learning contexts | The request mentions repositories, licensing, or community projects. |
| `qa` | quality assurance, question answering | Quality assurance in delivery contexts | The request is about AI evaluation or natural-language datasets. |
| `ac` | acceptance criteria, alternating current | Acceptance criteria | The request is outside software/product delivery. |

---

## Maintenance Rules

- Keep aliases lowercase unless the shorthand is conventionally uppercase.
- Prefer one canonical expansion per alias, with ambiguity notes for alternatives.
- Add aliases only when they are likely to recur in real requests.
- Remove aliases that become misleading or unused.
- Keep the always-on instruction limited to high-frequency aliases and behavior rules.
