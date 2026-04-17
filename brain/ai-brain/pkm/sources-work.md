# Capture Sources — Work Tools (Siemens / DISW LCS)

> **Context:** All tools provided by or used within the Siemens DISW LCS division.
> **Sensitivity:** All work tools default to L1 (internal) or L3 (restricted).
> See [sensitivity-and-access-control.md](sensitivity-and-access-control.md) for full classification.
>
> **Back to:** [PKM Hub](README.md) | [Consolidated inventory](capture-sources-inventory.md)

---

## Work Sources Summary

| # | Tool | Type | Primary use | Sensitivity |
|---|---|---|---|---|
| 1 | Jira | Issue tracking | Sprint work items, bugs, backlog | L1 internal |
| 2 | Confluence (work) | Team wiki | Documentation, ADRs, runbooks | L1 internal |
| 3 | Notepad++ | Local text editor | Scratch pad, work todos, log analysis | L1 internal |
| 4 | OneNote | Digital notebook | Meeting notes, structured work knowledge | L1 internal |
| 5 | Outlook | Email + calendar | Flagged follow-ups, newsletters | L1 internal |
| 6 | Teams self-chat | Self-messaging | Work todos, links, meeting follow-ups | L1 internal |
| 7 | GitHub (work) | Code hosting | Source code, PRs, CI/CD | L3 restricted |

---

## Jira (Work)

| Property | Details |
|---|---|
| **Type** | Issue tracking and project management (Atlassian) |
| **Status** | `active` |
| **Context** | `work` — Siemens DISW LCS |
| **Platform** | Web, mobile |
| **Cost** | Enterprise (employer-provided) |
| **URL** | Siemens internal Jira instance |
| **Account** | Siemens corporate SSO |
| **Sensitivity** | L1 internal (ticket content may contain proprietary details) |
| **Offline** | No |

**What I capture here:**

- Sprint work items — user stories, tasks, bugs
- Backlog grooming notes and estimation details
- Technical details and acceptance criteria in ticket comments
- Epic-level requirements and feature scoping
- Sprint retrospective action items

**PKM relevance:** Primary source of truth for *what I'm working on at the office*.
Action items and decisions from Jira sometimes get promoted to OneNote or Confluence
for deeper documentation.

---

## Confluence (Work)

| Property | Details |
|---|---|
| **Type** | Team wiki and documentation (Atlassian) |
| **Status** | `active` |
| **Context** | `work` — Siemens DISW LCS |
| **Platform** | Web |
| **Cost** | Enterprise (employer-provided) |
| **URL** | [https://ies-iesd-conf.ies.mentorg.com/](https://ies-iesd-conf.ies.mentorg.com/) |
| **Account** | Siemens corporate SSO |
| **Sensitivity** | L1 internal (all pages — corporate IP) |
| **Offline** | No |

**What I capture here:**

- Team documentation, runbooks, and onboarding guides
- Architecture decision records (ADRs) and design specs
- Project documentation shared with the team
- Knowledge base articles for the division
- Retrospective notes and meeting summaries

---

## Notepad++ (Work)

| Property | Details |
|---|---|
| **Type** | Local text editor — scratch pad (Windows) |
| **Status** | `active` |
| **Context** | `work` — primary scratch pad |
| **Platform** | Windows desktop |
| **Cost** | Free / open-source |
| **URL** | [https://notepad-plus-plus.org](https://notepad-plus-plus.org) |
| **Install path** | `C:\Program Files\Notepad++\notepad++.exe` |
| **Sensitivity** | L1 internal (may contain work secrets) |
| **Offline** | Yes — fully offline |

**What I capture here:**

- **Work todos** — quick task lists during the day (not in Jira yet)
- **Scratch pad** — temporary text, clipboard staging, throwaway notes
- **Ideas** — quick thoughts about work improvements, features, process
- **Learning items** — concepts to revisit, topics to study
- **Log analysis** — grepping through log files, regex search
- **Data inspection** — CSV/JSON/XML quick viewing

**How it fits in my PKM:** Notepad++ is my **lowest-friction work capture tool**. Notes
here are ephemeral — they get promoted to Jira (tasks), Confluence (docs), OneNote
(structured notes), or discarded. Nothing lives in Notepad++ permanently.

**Tips:**

- Use Session Manager plugin to save groups of related scratch files
- `Ctrl+D` duplicates current line — useful for templating
- Tab-based editing for multiple scratch contexts simultaneously

---

## OneNote (Work)

| Property | Details |
|---|---|
| **Type** | Digital notebook (Microsoft 365) |
| **Status** | `active` |
| **Context** | `work` — structured work notebook |
| **Platform** | Windows, Web, mobile |
| **Cost** | Included with M365 (employer-provided) |
| **URL** | [https://www.onenote.com](https://www.onenote.com) |
| **Account** | Siemens corporate M365 account |
| **Sync** | OneDrive (automatic) |
| **Sensitivity** | L1 internal (all sections — work context) |
| **Offline** | Yes (desktop app caches locally) |

**Primary notebook:** Saharsh @ Siemens AG

**Sections (current structure):**

| Section | Purpose |
|---|---|
| Quick Notes | Fast capture during meetings or calls |
| Work | Day-to-day work notes, ticket context, technical details |
| Personal | Non-work notes captured during work hours |
| Projects | Project-specific deep notes, design thinking |
| Learning | Concepts, tutorials, course notes, tech research |

**What I capture here:**

- Meeting notes (live during meetings)
- Work context that doesn't fit in Jira comments
- Project research and technical deep-dives
- Screen clippings and annotated screenshots
- Web clippings (OneNote Clipper extension)

**PKM relevance:** OneNote is my **structured work knowledge base**. It's where rough
Notepad++ notes get promoted when they need structure and persistence.

---

## Outlook (Work)

| Property | Details |
|---|---|
| **Type** | Email and calendar (Microsoft 365) |
| **Status** | `active` |
| **Context** | `work` |
| **Platform** | Windows (desktop), Web, mobile |
| **URL** | [https://outlook.office.com](https://outlook.office.com) |
| **Account** | Siemens corporate email |
| **Sync** | Exchange / M365 cloud |
| **Sensitivity** | L1 internal (email content — corporate communications) |

**What I capture here:**

- Flagged emails for follow-up (syncs to Microsoft To Do)
- Important threads archived in folders
- Calendar events with meeting notes links
- Email drafts as temporary staging (rarely)
- Newsletter subscriptions (dev newsletters, tech updates)

**PKM relevance:** Outlook is a **capture trigger**, not a storage destination. Flagged
items get transferred to Jira (work tasks) or OneNote (reference). Emails are not a
reliable long-term note store.

---

## Microsoft Teams — Self-Chat (Work)

| Property | Details |
|---|---|
| **Type** | Self-messaging channel in Microsoft Teams |
| **Status** | `active` |
| **Context** | `work` — quick work capture |
| **Platform** | Windows, Web, mobile |
| **URL** | [https://teams.microsoft.com](https://teams.microsoft.com) |
| **Account** | Siemens corporate M365 account |
| **Sync** | Microsoft cloud (automatic) |
| **Sensitivity** | L1 internal (may contain work-sensitive links and notes) |

**What I capture here:**

- **Work todos** — quick action items that need tracking
- **Learning resources** — links to courses, articles, documentation
- **Course todos** — training courses to complete, certifications to pursue
- **Work resources and links** — URLs to internal tools, dashboards, wikis
- **Reference notes** — quick technical details, commands, configs
- **Meeting follow-ups** — things I promised to do after a call

**PKM relevance:** Teams self-chat is my **work-context quick capture** — like Google
Keep but within the work ecosystem. Items here get promoted to Jira, Confluence, or
OneNote. The search in Teams makes these notes findable long-term.

---

## GitHub (Work)

| Property | Details |
|---|---|
| **Type** | Code hosting, version control (work org) |
| **Status** | `active` |
| **Context** | `work` |
| **Platform** | Web, CLI (`gh`), VS Code extension |
| **URL** | [https://github.com](https://github.com) |
| **Account** | `saharsh-poddar_SAGCP` |
| **Organisation** | Siemens GitHub Enterprise (via corporate SSO) |
| **Sync** | Git (push/pull) |
| **Sensitivity** | L3 restricted (all repos — proprietary source code) |
| **Offline** | Yes (local clone) |

**What I capture here:**

- Source code and version history (work projects)
- Pull request descriptions and code review comments
- Issue tracking for work repositories
- CI/CD workflow definitions

---

## Related Documents

- [PKM Hub](README.md) — Central index
- [sources-personal.md](sources-personal.md) — Personal capture tools
- [accounts-and-credentials.md](accounts-and-credentials.md) — All account details
- [sensitivity-and-access-control.md](sensitivity-and-access-control.md) — Access control rules
