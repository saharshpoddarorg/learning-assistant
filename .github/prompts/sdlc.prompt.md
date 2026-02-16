```prompt
---
name: sdlc
description: 'Learn SDLC phases, methodologies, and engineering practices — from requirements to deployment and maintenance'
agent: learning-mentor
tools: ['codebase', 'fetch']
---

## Topic
${input:topic:What SDLC topic? (e.g., agile, scrum, tdd, code-review, ci-cd, requirements, deployment, testing-strategy, sprint-planning, kanban, devops-culture)}

## Focus
${input:focus:What's your focus? (learn-phase / learn-methodology / compare-approaches / apply-to-project / interview-prep)}

## Current Level
${input:level:Your experience? (student / junior / mid / senior / lead)}

## Instructions

### SDLC Complete Map
```
Software Development Lifecycle — End to End
│
├── 1. PLANNING & REQUIREMENTS
│   ├── Requirements gathering (functional, non-functional)
│   ├── User stories, use cases, acceptance criteria
│   ├── Feasibility analysis, risk assessment
│   ├── Estimation (story points, T-shirt sizing, planning poker)
│   └── Roadmap & milestone planning
│
├── 2. DESIGN
│   ├── System design (HLD → LLD)
│   ├── Architecture decisions (ADRs)
│   ├── API design & contracts
│   ├── Database schema design
│   ├── UI/UX wireframes & prototypes
│   └── Design reviews & sign-off
│
├── 3. DEVELOPMENT
│   ├── Environment setup (local, dev, staging, prod)
│   ├── Branching strategy (GitFlow, trunk-based)
│   ├── Coding standards & linting
│   ├── Pair programming, mob programming
│   ├── Code review process
│   ├── Documentation (inline, API docs, README)
│   └── Feature flags & toggles
│
├── 4. TESTING
│   ├── Unit testing (TDD → Red-Green-Refactor)
│   ├── Integration testing
│   ├── End-to-end testing
│   ├── Acceptance testing (ATDD, UAT)
│   ├── BDD (Given/When/Then, Gherkin)
│   ├── Performance & load testing
│   ├── Security testing (SAST, DAST, penetration)
│   ├── Regression testing
│   ├── Test pyramid vs testing trophy
│   └── Mutation testing, property-based testing
│
├── 5. DEPLOYMENT & RELEASE
│   ├── CI/CD pipeline (build → test → deploy)
│   ├── Deployment strategies (blue-green, canary, rolling)
│   ├── Release management (semantic versioning, changelogs)
│   ├── Infrastructure as Code
│   ├── Containerization (Docker, K8s)
│   └── Environment promotion (dev → staging → prod)
│
├── 6. MONITORING & OPERATIONS
│   ├── Logging, metrics, tracing (observability)
│   ├── Alerting & incident response
│   ├── SRE practices (SLOs, SLIs, error budgets)
│   ├── On-call rotations
│   └── Post-mortem / retrospective
│
└── 7. MAINTENANCE & EVOLUTION
    ├── Bug fixing & hotfixes
    ├── Technical debt management
    ├── Refactoring sprints
    ├── Dependency updates & security patches
    ├── Feature deprecation
    └── Documentation updates
```

### Methodology Comparison
```
Waterfall:    Requirements ──→ Design ──→ Build ──→ Test ──→ Deploy (linear)
Agile/Scrum:  Sprint[Plan → Dev → Test → Review → Retro] × N (iterative)
Kanban:       Continuous flow with WIP limits (pull-based)
XP:           Short iterations + pair programming + TDD + CI (discipline)
Spiral:       Plan → Risk Analysis → Engineer → Evaluate (risk-driven)
SAFe:         Scaled Agile for large organizations (frameworks of frameworks)
```

### Response by Focus

#### If focus = learn-phase:
1. **Phase overview** — What happens here and why
2. **Inputs & outputs** — What enters this phase, what it produces
3. **Key activities** — Step-by-step within the phase
4. **Roles involved** — Who does what (developer, QA, PM, etc.)
5. **Tools commonly used** — Jira, GitHub, Jenkins, etc.
6. **Common mistakes** — What goes wrong in this phase
7. **Best practices** — How to do it well
8. **How it connects** — Upstream and downstream dependencies

#### If focus = learn-methodology:
1. **Core philosophy** — What drives this approach
2. **Ceremonies/events** — Meetings, rituals, cadence
3. **Roles** — Who has what responsibility
4. **Artifacts** — What documents/boards are produced
5. **Advantages vs disadvantages** — Honest trade-offs
6. **When to use** — Team size, project type, organization culture
7. **When NOT to use** — Situations where it fails
8. **Transition tips** — How to adopt if coming from another methodology

#### If focus = compare-approaches:
Comparison table + concrete recommendation based on team context.

### Rules
- Be practical — connect every concept to real team workflows
- Include specific tool examples (Jira, GitHub, Jenkins, etc.) but explain the concept first
- For testing topics, always show real code examples of the testing approach
- For methodology topics, include real sprint/board examples where relevant
- Acknowledge that most teams use hybrid approaches — pure methodology is rare
```
