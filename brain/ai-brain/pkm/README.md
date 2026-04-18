# PKM — Personal Knowledge Management Hub

> **Purpose:** Central index for the PKM system — capture source registry, access control,
> workflows, and integration with the brain/ai-brain backlog.
>
> **Owner:** Saharsh Poddar (saharshpoddar123@gmail.com)
> **Employer:** Siemens — DISW LCS Division
>
> **Security:** Sensitive content locations are documented but **never accessed without
> explicit permission**. See [Access Policy](access-policy.md) for the full protocol.

---

## PKM File Map

| File | Purpose | Quick link |
|---|---|---|
| **This file** | Hub index — start here | You are here |
| [sources-work.md](sources-work.md) | Work capture tools (Siemens / DISW LCS) | 7 tools |
| [sources-personal.md](sources-personal.md) | Personal digital, mobile, browser, physical tools | 18 tools |
| [accounts-and-credentials.md](accounts-and-credentials.md) | Account identifiers (no secrets) | All accounts |
| [sensitivity-and-access-control.md](sensitivity-and-access-control.md) | Content classification + Do Not Access rules | L0–L3 levels |
| [workflows.md](workflows.md) | Cross-source content flow pipelines | 7 workflows |
| [unused-tools.md](unused-tools.md) | Tools not currently in use + retirement log | 30+ tools |
| [access-policy.md](access-policy.md) | Full access control policy | Core principles |
| [access-log.md](access-log.md) | Audit trail of all content access events | Append-only log |
| [capture-sources-inventory.md](capture-sources-inventory.md) | Consolidated single-file reference (all-in-one) | 1090+ lines |

---

## Capture Source Universe — Overview

```text
              ┌──────────────────────────────────────────────────┐
              │          MY CAPTURE SOURCES UNIVERSE              │
              │  Saharsh Poddar · Siemens DISW LCS               │
              └─────────────────────┬────────────────────────────┘
                                    │
       ┌────────────────────────────┼────────────────────────────────┐
       │                            │                                │
 ┌─────┴──────────┐        ┌───────┴────────┐            ┌──────────┴────────┐
 │  WORK (Siemens) │        │    PERSONAL    │            │  PHYSICAL/OFFLINE │
 └─────┬──────────┘        └───────┬────────┘            └──────────┬────────┘
       │                           │                                │
 Jira               Google Keep (multi-purpose)       Office diaries
 Confluence (work)   Notion                           A4 paper / loose sheets
 Notepad++           Confluence (personal)            Office whiteboard
 OneNote             Gmail
 Outlook             Apple Notes
 Teams self-chat     Google Docs / Sheets
 GitHub (work org)   VS Code
                     GitHub (personal)
                     brain/ai-brain
                     ike (mobile)
                     WhatsApp self-chat
                     Browser bookmarks (Edge, Chrome, Firefox)
                     Phone camera / screenshots
```

---

## Quick Lookup — Where Does This Type of Content Go?

| Content type | Primary source | Fallback | Details in |
|---|---|---|---|
| Quick thought / fleeting idea | Google Keep | WhatsApp self-chat | [sources-personal.md](sources-personal.md) |
| Work todo / task | Jira | Teams self-chat | [sources-work.md](sources-work.md) |
| Personal todo / task | ike (mobile) | Google Keep | [sources-personal.md](sources-personal.md) |
| Work scratch notes | Notepad++ | OneNote | [sources-work.md](sources-work.md) |
| Work meeting notes | OneNote | Confluence | [sources-work.md](sources-work.md) |
| Work documentation (team) | Confluence (work) | OneNote | [sources-work.md](sources-work.md) |
| Personal software dev projects | Confluence (personal) | brain/ai-brain/notes | [sources-personal.md](sources-personal.md) |
| Learning notes / concepts | Google Keep / Teams | brain/ai-brain/notes | [sources-personal.md](sources-personal.md) |
| Code & version control | GitHub | VS Code | [sources-personal.md](sources-personal.md) / [sources-work.md](sources-work.md) |
| AI session / deep research | brain/ai-brain/sessions | brain/ai-brain/notes | [sources-personal.md](sources-personal.md) |
| Personal backlog / ideas | brain/ai-brain/backlog | Google Keep | [sources-personal.md](sources-personal.md) |
| Long-form personal writing | Notion | Google Docs | [sources-personal.md](sources-personal.md) |
| Confidential / sensitive info | Google Keep (labelled) | Physical notebook | [sensitivity-and-access-control.md](sensitivity-and-access-control.md) |

---

## Sensitivity Quick Reference

| Level | Label | AI Access | Full details |
|---|---|---|---|
| **L0** | `public` | ✅ Freely accessible | [sensitivity-and-access-control.md](sensitivity-and-access-control.md) |
| **L1** | `internal` | ⚠️ Ask before accessing | [sensitivity-and-access-control.md](sensitivity-and-access-control.md) |
| **L2** | `confidential` | ❌ Do NOT access | [sensitivity-and-access-control.md](sensitivity-and-access-control.md) |
| **L3** | `restricted` | ❌ NEVER access | [sensitivity-and-access-control.md](sensitivity-and-access-control.md) |

---

## PKM ↔ Backlog Integration

The `pkm/` folder and the `backlog/` folder in brain/ai-brain are **siblings, not nested**:

- **PKM** = where knowledge lives, how it flows, access rules
- **Backlog** = what work to track, priority, sprint planning

### Integration Points

| Integration | Direction | Commands |
|---|---|---|
| Jot down from source | Source → Backlog | `/jot`, `/todo`, `/read-file-jot` |
| Content fetch (first-time) | Source → inbox/ | `/brain fetch <source>` |
| Content pull (update) | Source → existing brain files | `/brain pull <source>` |
| Full import (clone) | Source → library/ or notes/ | `/brain clone <source>` |
| Selective import | Source → inbox/ | `/brain cherry-pick <source> <item>` |
| Route inbox items | inbox/ → correct tier | `/brain merge <item>` |
| Content export | brain → external | `/brain push <source>` |
| Compare versions | brain vs. source | `/brain diff <source>` |
| Access logging | All → PKM | All access logged in access-log.md |
| List sources | PKM → display | `/brain remote` |

### Feature Map

| Feature | Location | Commands | Status |
|---|---|---|---|
| Jot Down | `backlog/` | `/jot`, `/todo`, `/read-file-jot` | ✅ Active |
| Kanban / Scrum | `backlog/` | `/todos`, `/backlog sprint` | ✅ Active |
| Source Inventory | `pkm/` | `/brain remote` | ✅ Active |
| Access Policy | `pkm/` | (reference doc) | ✅ Active |
| Access Logging | `pkm/` | (auto-populated) | ✅ Active |
| Content Fetch | Source → inbox/ | `/brain fetch` | ✅ Active |
| Content Pull | Source → update existing | `/brain pull` | ✅ Active |
| Content Clone | Source → structured import | `/brain clone` | ✅ Active |
| Cherry-Pick | Source → one specific item | `/brain cherry-pick` | ✅ Active |
| Content Merge | inbox/ → correct tier | `/brain merge` | ✅ Active |
| Content Stash | inbox/ → park for later | `/brain stash`, `/brain stash pop` | ✅ Active |
| Content Diff | brain vs. source comparison | `/brain diff` | ✅ Active |
| Content Push | brain → external | `/brain push` | ✅ Active |
| Brain Consolidation | External → brain (migration) | `/brain consolidate` | ✅ Active |

---

## Maintenance Schedule

| Frequency | Action |
|---|---|
| **Daily** | Process physical notes → digital (24-hour rule for paper) |
| **Every 2-3 days** | Review WhatsApp self-chat → promote or delete |
| **Weekly** | Review Google Keep unlabelled notes → label and route or delete |
| **Weekly** | Review Teams self-chat → star important items or promote |
| **Weekly** | Close stale browser tab groups → bookmark or discard |
| **Monthly** | Audit this inventory — any new sources? Any retired? |
| **Quarterly** | Review cross-source workflows — simplify if possible |
| **Quarterly** | Review unused-tools.md — adopt or remove candidates |

---

## Related Documents

- [capture-sources-inventory.md](capture-sources-inventory.md) — Consolidated single-file reference
- [pkm-philosophy.md](../pkm-philosophy.md) — Why ai-brain is structured this way
- [digitalnotetaking/START-HERE.md](../../digitalnotetaking/START-HERE.md) — Getting started with digital PKM
- [digitalnotetaking/tools-comparison.md](../../digitalnotetaking/tools-comparison.md) — Tool comparison matrix
- [ai-brain/README.md](../README.md) — How to use the ai-brain workspace
- [backlog/README.md](../backlog/README.md) — Backlog system documentation
