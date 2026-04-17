# Capture Sources Inventory — Where My Ideas Live

> **Purpose:** Exhaustive registry of every tool, platform, medium, and location where
> I capture ideas, notes, todos, reference material, and working knowledge. This is the
> single source of truth for "where do I look?" and "where should I put this?"
>
> **Maintenance:** Update this document whenever a new capture source is added or retired.
> Mark retired sources with `Status: retired` rather than deleting them.
>
> **Owner:** Saharsh Poddar (saharshpoddar123@gmail.com)
> **Employer:** Siemens — DISW LCS Division
>
> **Security:** This file contains account identifiers but **no passwords or tokens**.
> Sensitive content locations are documented but **never accessed without explicit permission**.
> See [Access Policy](access-policy.md) and [Access Log](access-log.md) for the full protocol.

---

## Table of Contents

- [Overview — Capture Source Map](#overview--capture-source-map)
- [Work Tools (Siemens / DISW LCS)](#work-tools-siemens--disw-lcs)
  - [Jira (Work)](#jira-work)
  - [Confluence (Work)](#confluence-work)
  - [Notepad++ (Work)](#notepad-work)
  - [OneNote (Work)](#onenote-work)
  - [Outlook (Work)](#outlook-work)
  - [Microsoft Teams — Self-Chat (Work)](#microsoft-teams--self-chat-work)
  - [GitHub (Work)](#github-work)
- [Personal Tools — Digital](#personal-tools--digital)
  - [Google Keep](#google-keep)
  - [Notion (Personal)](#notion-personal)
  - [Confluence (Personal)](#confluence-personal)
  - [Gmail (Personal)](#gmail-personal)
  - [Apple Notes](#apple-notes)
  - [Google Docs / Sheets](#google-docs--sheets)
  - [VS Code](#vs-code)
  - [GitHub (Personal)](#github-personal)
  - [brain/ai-brain (This Repo)](#brainai-brain-this-repo)
- [Personal Tools — Mobile](#personal-tools--mobile)
  - [ike (Mobile Task Tracker)](#ike-mobile-task-tracker)
  - [WhatsApp — Self-Chat](#whatsapp--self-chat)
  - [Phone Camera / Screenshots](#phone-camera--screenshots)
- [Browser-Based Capture](#browser-based-capture)
  - [Microsoft Edge](#microsoft-edge)
  - [Google Chrome / Chrome Canary](#google-chrome--chrome-canary)
  - [Mozilla Firefox](#mozilla-firefox)
- [Physical / Offline](#physical--offline)
  - [Office Diaries & Notepads](#office-diaries--notepads)
  - [A4 Paper / Loose Sheets](#a4-paper--loose-sheets)
  - [Office Whiteboard](#office-whiteboard)
- [Cross-Source Workflows](#cross-source-workflows)
- [Account & Credentials Registry](#account--credentials-registry)
- [Content Sensitivity Classification](#content-sensitivity-classification)
- [Do Not Access — Restricted Content](#do-not-access--restricted-content)
- [Access Logging & Policy](#access-logging--policy)
- [PKM ↔ Backlog Integration](#pkm--backlog-integration)
- [Unused / Available Tools](#unused--available-tools)
- [Source Retirement Log](#source-retirement-log)

---

## Overview — Capture Source Map

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

### Quick Lookup — Where Does This Type of Content Go?

| Content type | Primary source | Fallback | Context |
|---|---|---|---|
| Quick thought / fleeting idea | Google Keep | WhatsApp self-chat | Fastest capture, sort later |
| Work todo / task | Jira | Teams self-chat | Official sprint tracking |
| Personal todo / task | ike (mobile) | Google Keep | Mobile-first, reminders |
| Work scratch notes / analysis | Notepad++ | OneNote | Scratch pad, ephemeral |
| Work meeting notes | OneNote | Confluence | Structured notebook |
| Work documentation (team) | Confluence (work) | OneNote | Team-visible, searchable |
| Personal software dev projects | Confluence (personal) | brain/ai-brain/notes | Long-form design docs |
| Learning notes / concepts | Google Keep / Teams self-chat | brain/ai-brain/notes | Quick capture → promote |
| Code & version control | GitHub | VS Code | Tracked, branched |
| AI session / deep research | brain/ai-brain/sessions | brain/ai-brain/notes | Auto-captured by Copilot |
| Personal backlog / ideas | brain/ai-brain/backlog | Google Keep | Agile board |
| Long-form personal writing | Notion | Google Docs | Cloud, databases |
| Article / resource link | Browser bookmarks | Teams/WhatsApp self-chat | URL + context |
| Confidential / sensitive info | Google Keep (labelled) | Physical notebook | Not in Git-tracked files |
| Whiteboard diagram | Phone camera → Google Keep | OneNote | Always digitise same day |
| Work links / resources | Teams self-chat | Outlook flagged | Quick reference |
| Personal documents | Gmail (starred) | Google Docs | Archived threads |

---

## Work Tools (Siemens / DISW LCS)

### Jira (Work)

| Property | Details |
|---|---|
| **Type** | Issue tracking and project management (Atlassian) |
| **Status** | `active` |
| **Context** | `work` — Siemens DISW LCS |
| **Platform** | Web, mobile |
| **Cost** | Enterprise (employer-provided) |
| **URL** | Siemens internal Jira instance |
| **Account** | Siemens corporate SSO |
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

### Confluence (Work)

| Property | Details |
|---|---|
| **Type** | Team wiki and documentation (Atlassian) |
| **Status** | `active` |
| **Context** | `work` — Siemens DISW LCS |
| **Platform** | Web |
| **Cost** | Enterprise (employer-provided) |
| **URL** | [https://ies-iesd-conf.ies.mentorg.com/](https://ies-iesd-conf.ies.mentorg.com/) |
| **Account** | Siemens corporate SSO |
| **Offline** | No |

**What I capture here:**

- Team documentation, runbooks, and onboarding guides
- Architecture decision records (ADRs) and design specs
- Project documentation shared with the team
- Knowledge base articles for the division
- Retrospective notes and meeting summaries

---

### Notepad++ (Work)

| Property | Details |
|---|---|
| **Type** | Local text editor — scratch pad (Windows) |
| **Status** | `active` |
| **Context** | `work` — primary scratch pad |
| **Platform** | Windows desktop |
| **Cost** | Free / open-source |
| **URL** | [https://notepad-plus-plus.org](https://notepad-plus-plus.org) |
| **Install path** | `C:\Program Files\Notepad++\notepad++.exe` |
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

### OneNote (Work)

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

### Outlook (Work)

| Property | Details |
|---|---|
| **Type** | Email and calendar (Microsoft 365) |
| **Status** | `active` |
| **Context** | `work` |
| **Platform** | Windows (desktop), Web, mobile |
| **URL** | [https://outlook.office.com](https://outlook.office.com) |
| **Account** | Siemens corporate email |
| **Sync** | Exchange / M365 cloud |

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

### Microsoft Teams — Self-Chat (Work)

| Property | Details |
|---|---|
| **Type** | Self-messaging channel in Microsoft Teams |
| **Status** | `active` |
| **Context** | `work` — quick work capture |
| **Platform** | Windows, Web, mobile |
| **URL** | [https://teams.microsoft.com](https://teams.microsoft.com) |
| **Account** | Siemens corporate M365 account |
| **Sync** | Microsoft cloud (automatic) |

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

### GitHub (Work)

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
| **Offline** | Yes (local clone) |

**What I capture here:**

- Source code and version history (work projects)
- Pull request descriptions and code review comments
- Issue tracking for work repositories
- CI/CD workflow definitions

---

## Personal Tools — Digital

### Google Keep

| Property | Details |
|---|---|
| **Type** | Quick notes, lists, and labels (Google) |
| **Status** | `active` |
| **Context** | `personal` + `work` (cross-cutting) |
| **Platform** | Web, Android, iOS |
| **Cost** | Free |
| **URL** | [https://keep.google.com](https://keep.google.com) |
| **Account** | saharshpoddar123@gmail.com |
| **Sync** | Google Cloud (automatic) |
| **Offline** | Yes (mobile app) |

**What I capture here (multi-purpose — label-separated):**

Google Keep serves multiple roles. **Not all notes here are PKM-relevant.** The labels
below separate work/learning capture from personal-only content.

#### PKM-Relevant Labels (promote to brain/ai-brain or other tools)

| Label | Purpose | Promote to |
|---|---|---|
| Work | Work-related quick notes, ideas, reminders | Jira / Confluence / OneNote |
| Learning | Concepts, resources, topics to study | brain/ai-brain/notes or backlog |
| Ideas | Feature ideas, project concepts, brainstorms | brain/ai-brain/backlog/ideas |
| Todos | Actionable items across work and personal | ike / Jira / brain/ai-brain/backlog |

#### Personal-Only Labels (NOT part of PKM — stay in Google Keep)

| Label | Purpose | Notes |
|---|---|---|
| Shopping | Grocery lists, purchase tracking | Pure personal — not promoted |
| Confidential | Passwords, sensitive details, private info | **Never** in Git-tracked files |
| Personal | Personal reminders, family, health, appointments | Stays in Google Keep |
| Finance | Bills, payments, financial reminders | Stays in Google Keep |

#### Unlabelled Notes

Some notes may be unlabelled. **Weekly review rule:** Every unlabelled note gets either
labelled and routed, or deleted. No note should remain unlabelled for more than 7 days.

**Mobile-specific workflows:**

- Voice notes while commuting (auto-transcribed)
- Camera capture of whiteboards, receipts, handwritten notes
- Widget on home screen for instant capture
- Reminders (location-based and time-based)

---

### Notion (Personal)

| Property | Details |
|---|---|
| **Type** | All-in-one workspace (notes, wikis, databases, tasks) |
| **Status** | `active` |
| **Context** | `personal` + `work` (cross-cutting) |
| **Platform** | Web, Windows, macOS, iOS, Android |
| **Cost** | Free (personal plan) |
| **URL** | [https://www.notion.so](https://www.notion.so) |
| **Account** | saharshpoddar123@gmail.com |
| **Sync** | Cloud (Notion servers) |
| **Offline** | Limited (cached pages only) |

**What I capture here:**

- **Work pages** — structured work documentation, project tracking
- **Personal pages** — personal projects, life planning, goals
- **Learning notes** — long-form learning material, course notes
- Linked databases (project tracker, reading list, etc.)
- Templates and repeating workflows

**Top-level structure:**

| Page / Area | Purpose |
|---|---|
| Work | Work-related structured notes and documentation |
| Personal | Personal projects, goals, life planning |
| Learning | Courses, tutorials, concept deep-dives |
| <!-- TODO: add specific pages/databases --> | |

**PKM relevance:** Notion is my **long-form structured personal workspace**. Where
Google Keep captures fleeting thoughts, Notion holds developed, structured content
that benefits from databases, linked pages, and rich formatting.

---

### Confluence (Personal)

| Property | Details |
|---|---|
| **Type** | Personal wiki and documentation (Atlassian Cloud) |
| **Status** | `active` |
| **Context** | `personal` |
| **Platform** | Web |
| **Cost** | Free (up to 10 users) |
| **URL** | [https://saharshpoddarorg.atlassian.net/wiki/home](https://saharshpoddarorg.atlassian.net/wiki/home) |
| **Account** | saharshpoddar060699@gmail.com |
| **Sync** | Cloud (Atlassian servers) |
| **Offline** | No |

**What I capture here:**

- **Personal software dev projects** — architecture docs, design specs, project wikis
- **Learning notes** — structured technical deep-dives, concept documentation
- Personal knowledge base for software engineering topics
- Project documentation for personal GitHub repos

**PKM relevance:** Personal Confluence is my **personal engineering wiki** — the
place for structured, long-form technical documentation about personal projects that
benefits from Confluence's page hierarchy, macros, and search.

---

### Gmail (Personal)

| Property | Details |
|---|---|
| **Type** | Email (personal) |
| **Status** | `active` |
| **Context** | `personal` |
| **Platform** | Web, mobile |
| **URL** | [https://mail.google.com](https://mail.google.com) |
| **Accounts** | saharshpoddar123@gmail.com (primary), saharshpoddar060699@gmail.com (dev/work) |
| **Sync** | Google Cloud |

**What I capture here:**

- Starred emails for follow-up (personal)
- Important threads archived with labels
- Newsletter subscriptions (dev newsletters, tech updates)
- Account confirmations and service communications
- Email drafts as temporary note staging (rarely)

---

### Apple Notes

| Property | Details |
|---|---|
| **Type** | Built-in notes app (Apple ecosystem) |
| **Status** | `active` |
| **Context** | `personal` |
| **Platform** | macOS, iOS, iPadOS, Web (iCloud.com) |
| **Cost** | Free |
| **URL** | [https://www.icloud.com/notes](https://www.icloud.com/notes) |
| **Sync** | iCloud (automatic) |
| **Offline** | Yes |

**What I capture here:**

- Quick notes on iPhone/iPad when Google Keep isn't convenient
- Shared notes with family/friends (iCloud sharing)
- Scanned documents (built-in scanner on iOS)

---

### Google Docs / Sheets

| Property | Details |
|---|---|
| **Type** | Cloud document editing (Google Workspace) |
| **Status** | `active` |
| **Context** | `personal` |
| **Platform** | Web, iOS, Android |
| **Cost** | Free with Google account |
| **URL** | [https://docs.google.com](https://docs.google.com) / [https://sheets.google.com](https://sheets.google.com) |
| **Account** | saharshpoddar123@gmail.com |
| **Sync** | Google Drive (automatic) |
| **Offline** | Yes (Chrome extension for offline editing) |

**What I capture here:**

- Shared documents and collaborative writing
- Spreadsheets for data tracking, budgeting, comparison tables
- Long-form drafts that need cloud collaboration or sharing
- Export-ready documents (PDF, DOCX)

---

### VS Code

| Property | Details |
|---|---|
| **Type** | Code editor / IDE |
| **Status** | `active` |
| **Context** | `personal` + `work` |
| **Platform** | Windows, macOS, Linux |
| **Cost** | Free / open-source |
| **URL** | [https://code.visualstudio.com](https://code.visualstudio.com) |
| **Settings sync** | GitHub account sync (saharshpoddar) |
| **Offline** | Yes |

**What I capture here:**

- Code snippets (User Snippets per language)
- TODO/FIXME comments in codebases
- Copilot Chat sessions (transient — promote to brain/ai-brain/sessions)
- Workspace-specific `.vscode/` settings and tasks
- Markdown editing for brain/ai-brain notes

**Key workspaces:**

| Workspace | Purpose |
|---|---|
| `learning-assistant.code-workspace` | This repo — learning + PKM + Copilot customization |
| <!-- TODO: add other workspaces --> | |

---

### GitHub (Personal)

| Property | Details |
|---|---|
| **Type** | Code hosting, version control, issues, discussions |
| **Status** | `active` |
| **Context** | `personal` |
| **Platform** | Web, CLI (`gh`), VS Code extension |
| **Cost** | Free |
| **URL** | [https://github.com](https://github.com) |
| **Account** | `saharshpoddar` |
| **Profile URL** | [https://github.com/saharshpoddar](https://github.com/saharshpoddar) |
| **Organisation** | `saharshpoddarorg` — [https://github.com/saharshpoddarorg](https://github.com/saharshpoddarorg) |
| **Sync** | Git (push/pull) |
| **Offline** | Yes (local clone) |

**What I capture here:**

- Source code and version history (personal projects)
- Issues (feature requests, bugs, task tracking)
- Pull request descriptions and code review comments
- README documentation and project wikis
- GitHub Actions workflows

**Key repositories:**

| Repository | Purpose | URL |
|---|---|---|
| `learning-assistant` | Learning + Copilot customization + PKM | [saharshpoddarorg/learning-assistant](https://github.com/saharshpoddarorg/learning-assistant) |
| <!-- TODO: add more repos --> | | |

---

### brain/ai-brain (This Repo)

| Property | Details |
|---|---|
| **Type** | Structured PKM workspace (Markdown + Git) |
| **Status** | `active` |
| **Context** | `personal` — primary knowledge workspace |
| **Platform** | Local files (Git-tracked in `learning-assistant` repo) |
| **Cost** | Free |
| **Path** | `brain/ai-brain/` |
| **Sync** | Git → GitHub (saharshpoddarorg/learning-assistant) |
| **Offline** | Yes (local files) |

**What I capture here:**

- Distilled learning notes and insights (`notes/`)
- Imported reference materials (`library/`)
- Captured AI chat sessions worth revisiting (`sessions/`)
- Personal agile backlog — todos, ideas, features, sprints (`backlog/`)
- Raw capture and works-in-progress (`inbox/` — gitignored)
- PKM reference docs like this inventory (`pkm/`)

**Tier routing:**

| Tier | Path | Content type |
|---|---|---|
| Inbox | `brain/ai-brain/inbox/` | Raw capture, not ready (gitignored) |
| Notes | `brain/ai-brain/notes/` | Authored writing — tracked |
| Library | `brain/ai-brain/library/` | Imported external sources — tracked |
| Sessions | `brain/ai-brain/sessions/` | Captured AI conversations — tracked |
| Backlog | `brain/ai-brain/backlog/` | Todos, features, ideas, sprints — tracked |
| PKM | `brain/ai-brain/pkm/` | PKM reference docs (this file) — tracked |

---

## Personal Tools — Mobile

### ike (Mobile Task Tracker)

| Property | Details |
|---|---|
| **Type** | Task tracker and reminder app (mobile) |
| **Status** | `active` |
| **Context** | `personal` — primary personal task manager |
| **Platform** | Mobile (iOS / Android) |
| **Cost** | Free |
| **Sync** | Cloud (app-specific) |
| **Offline** | Yes (mobile app) |

**What I capture here:**

- **Personal tasks** — day-to-day action items, errands, chores
- **Reminders** — time-based and recurring reminders
- **Learning work** — study tasks, courses to complete, topics to research
- **Todos** — general personal todos that need tracking and deadlines

**PKM relevance:** ike is my **mobile-first personal task manager** — the personal
equivalent of Jira. Tasks that grow into projects get promoted to brain/ai-brain/backlog
or Notion. Simple one-off tasks stay in ike until completed.

---

### WhatsApp — Self-Chat

| Property | Details |
|---|---|
| **Type** | Self-messaging in WhatsApp (message yourself) |
| **Status** | `active` |
| **Context** | `personal` + `work` (cross-cutting) |
| **Platform** | Mobile, Windows desktop, Web |
| **Cost** | Free |
| **URL** | [https://web.whatsapp.com](https://web.whatsapp.com) |
| **Sync** | WhatsApp cloud (end-to-end encrypted) |
| **Offline** | Yes (mobile app) |

**What I capture here:**

- **Work todos** — quick work reminders when not at the desk
- **Learning todos** — concepts to study, resources to check out
- **Concepts** — quick explanations or notes-to-self about a topic
- **Quick notes** — anything that needs capturing in under 10 seconds
- **Personal documents** — photos of documents, PDFs, receipts
- **Links** — articles, videos, resources shared with myself for later

**PKM relevance:** WhatsApp self-chat is my **ultra-fast cross-context capture** —
available on both phone and desktop, end-to-end encrypted. It's the most frictionless
capture tool I have but the **worst for retrieval** (no labels, no structure, linear
scroll only). Items must be promoted to proper tools within 48 hours.

**Promotion rule:** Review WhatsApp self-chat every 2-3 days. Promote to:

| Content type | Promote to |
|---|---|
| Work task | Jira or Teams self-chat |
| Personal task | ike |
| Learning resource | brain/ai-brain/backlog or Google Keep (Learning label) |
| Document / receipt | Google Drive or Google Keep |
| Quick note worth keeping | Google Keep or brain/ai-brain/inbox |

---

### Phone Camera / Screenshots

| Property | Details |
|---|---|
| **Type** | Visual capture (photos, screenshots) |
| **Status** | `active` |
| **Context** | `personal` + `work` |
| **Platform** | Mobile |
| **Sync** | Google Photos / iCloud Photos |

**What I capture here:**

- Whiteboard photos after meetings
- Screenshots of error messages and UI states
- Photos of physical notebook pages to digitise
- Architecture diagrams drawn on paper
- Business cards and receipts

**Workflow:** Capture → Google Keep (annotate + label) → brain/ai-brain or OneNote (permanent)

---

## Browser-Based Capture

I use **multiple browsers** simultaneously for different contexts. Each has its own
bookmarks, workspaces, tab groups, and profiles.

### Microsoft Edge

| Property | Details |
|---|---|
| **Type** | Primary browser (Chromium-based) |
| **Status** | `active` |
| **Context** | `work` + `personal` |
| **Sync** | Microsoft account |
| **Profiles** | <!-- TODO: list profiles if using Edge profiles (e.g., Work, Personal) --> |

**What I capture here:**

- Bookmarks organised by topic (work tools, dev resources, learning)
- Workspaces for different project contexts
- Tab groups for active research threads
- Collections (Edge feature) for curated link sets

**Bookmark structure:**

| Folder | Purpose |
|---|---|
| <!-- TODO: e.g., "Work > Internal Tools" --> | <!-- TODO --> |
| <!-- TODO: e.g., "Dev > Java" --> | <!-- TODO --> |
| <!-- TODO: e.g., "Learning > Courses" --> | <!-- TODO --> |
| <!-- TODO: add your actual folders --> | |

---

### Google Chrome / Chrome Canary

| Property | Details |
|---|---|
| **Type** | Browser (stable + bleeding-edge) |
| **Status** | `active` |
| **Context** | `personal` (primarily) |
| **Sync** | Google account (saharshpoddar123@gmail.com) |

**What I capture here:**

- Personal bookmarks and saved pages
- Chrome Canary for testing bleeding-edge web features
- Tab groups for personal research contexts
- Extensions for web clipping (if any)

**Bookmark structure:**

| Folder | Purpose |
|---|---|
| <!-- TODO: list your Chrome bookmark folders --> | |

---

### Mozilla Firefox

| Property | Details |
|---|---|
| **Type** | Browser (privacy-focused) |
| **Status** | `active` |
| **Context** | varies |
| **Sync** | Firefox account (if configured) |

**What I capture here:**

- Bookmarks for specific contexts
- Tab groups / containers for isolated browsing

**Bookmark structure:**

| Folder | Purpose |
|---|---|
| <!-- TODO: list your Firefox bookmark folders --> | |

---

### Browser Capture Tips (All Browsers)

- **Name tab groups** by project or investigation topic
- **Collapse unused tab groups** to reduce clutter
- **Weekly cleanup:** Bookmark stale tabs or close them — open tabs are not a storage system
- **Bookmark bar:** Keep only the 5-10 most frequently used links visible
- **Cross-browser problem:** Bookmarks scattered across 3+ browsers are hard to find.
  Consider consolidating key bookmarks into a single tool (Google Keep, Notion, or
  a dedicated bookmarking service like Raindrop.io)

---

## Physical / Offline

### Office Diaries & Notepads

| Property | Details |
|---|---|
| **Type** | Paper notebooks, office diaries, notepads |
| **Status** | `active` |
| **Context** | `work` |
| **Location** | Office desk |
| **Sync** | None — digitise manually |

**What I capture here:**

- Meeting notes when laptop isn't open
- Quick diagrams and flowcharts
- Brainstorming during whiteboard sessions
- Daily task lists and priorities
- Phone call notes

**Digitisation workflow:**

1. Capture important pages with phone camera
2. Upload to Google Keep with "Work" label
3. Promote to OneNote (work) or brain/ai-brain (personal) if worth keeping
4. Write a 1-line summary in the digital copy

**Rule:** Digitise any important handwritten notes within 24 hours. Paper is for
capture speed, not long-term storage.

---

### A4 Paper / Loose Sheets

| Property | Details |
|---|---|
| **Type** | Loose paper sheets for sketching and quick notes |
| **Status** | `active` |
| **Context** | `work` |
| **Location** | Office desk, meeting rooms |

**What I capture here:**

- System architecture sketches during design discussions
- Quick calculations and data flow diagrams
- Throwaway notes during phone calls or stand-ups

**Rule:** Same as diaries — digitise or discard within 24 hours. Loose sheets are
even more ephemeral than notebooks.

---

### Office Whiteboard

| Property | Details |
|---|---|
| **Type** | Physical whiteboard |
| **Status** | `active` |
| **Context** | `work` |
| **Location** | Office / meeting rooms |

**What I capture here:**

- Architecture diagrams and system design sketches
- Sprint planning boards (physical Kanban)
- Brainstorming sessions and mind maps
- Flow charts and sequence diagrams
- Team discussion visualisations

**Digitisation workflow:**

1. Photo the whiteboard **before erasing** (phone camera)
2. Upload to Google Keep or OneNote
3. If the diagram is significant, redraw digitally (Excalidraw, draw.io, Mermaid)

---

## Cross-Source Workflows

These workflows describe how content flows between multiple sources:

### Workflow 1 — Quick Idea Capture Pipeline

```text
Fleeting idea (anytime, anywhere)
    ↓
Google Keep / WhatsApp self-chat / ike     ← capture in < 30 seconds
    ↓  review within 48 hours
Actionable?
  ├── Work task → Jira
  ├── Personal task → ike (with reminder)
  ├── Learning topic → brain/ai-brain/backlog or Google Keep (Learning)
  ├── Project idea → brain/ai-brain/backlog/ideas
  └── Not worth keeping → delete
```

### Workflow 2 — Work Day Pipeline

```text
Start of day
    ↓
Check: Jira sprint board → what's assigned
Check: Teams self-chat → any overnight notes
Check: Outlook flagged → any follow-ups
    ↓
During the day
    ↓
Quick capture → Notepad++ (scratch) / Teams self-chat (links)
Meetings → OneNote (structured notes)
Tasks completed → update Jira
    ↓
End of day
    ↓
Process Notepad++ scratch → Jira / Confluence / discard
Process Teams self-chat → starred or promoted
```

### Workflow 3 — Learning Session Pipeline

```text
Copilot Chat / research session
    ↓
brain/ai-brain/sessions/ (auto-captured)   ← AI conversation
    ↓  distill
brain/ai-brain/notes/ (your synthesis)     ← your understanding
    ↓  if resource is reusable
brain/ai-brain/library/ (imported source)  ← external reference
```

### Workflow 4 — Physical-to-Digital Pipeline

```text
Physical notebook / whiteboard / A4 paper
    ↓  photo with phone (within 24 hours)
Google Keep (label: Work or relevant label)
    ↓  process within 48 hours
OneNote (work context)
    OR
brain/ai-brain/notes/ (personal learning)
    OR
Discard (if ephemeral)
```

### Workflow 5 — Cross-Platform Todo Pipeline

```text
Todo emerges (meeting, idea, reading, etc.)
    ↓
Where does it belong?
  ├── Work sprint item → Jira (official)
  ├── Work quick reminder → Teams self-chat
  ├── Personal task → ike (with deadline/reminder)
  ├── Personal software project task → brain/ai-brain/backlog
  ├── Learning goal → brain/ai-brain/backlog or Google Keep (Learning)
  └── Grocery / errand → Google Keep (Shopping)
```

### Workflow 6 — PKM-to-Backlog Pipeline (Jot Down Integration)

```text
Content discovered in any capture source
    ↓
Is it actionable work to track?
  ├── Yes → /jot "description" — creates BLI/IDEA in brain/ai-brain/backlog
  │         (auto-classifies, dedup-checks, enhances, cross-refs)
  ├── Import from file? → /read-file-jot "path" — extracts all items from file
  └── No → stays in capture source or promoted to notes/library
    ↓
Backlog item created → BOARD.md + views + CHANGELOG updated
    ↓
Sprint planning → items pulled into active sprint
```

### Workflow 7 — Content Fetch from Capture Sources

```text
Need to consolidate notes from external tools?
    ↓
Check access-policy.md → is the source accessible?
  ├── RESTRICTED → do NOT access (see Do Not Access section)
  ├── ASK-FIRST → ask user for explicit permission before reading
  └── OPEN → proceed with fetch
    ↓
Fetch content (web scrape, API, file read)
    ↓
Log access in access-log.md (timestamp, source, what was accessed, why)
    ↓
Route content:
  ├── Raw import → brain/ai-brain/inbox/
  ├── Reference material → brain/ai-brain/library/
  ├── Actionable items → /jot (creates backlog items)
  └── Learning notes → brain/ai-brain/notes/
```

---

## Account & Credentials Registry

> **Security note:** This section contains **account identifiers only** — usernames,
> email addresses, and URLs. **No passwords, tokens, API keys, or secrets** are stored here.
> Credentials are managed in secure password managers (not in Git-tracked files).

### Personal Accounts

| Service | Account identifier | Email | URL | Notes |
|---|---|---|---|---|
| GitHub (personal) | `saharshpoddar` | saharshpoddar123@gmail.com | [github.com/saharshpoddar](https://github.com/saharshpoddar) | Personal repos |
| GitHub (org) | `saharshpoddarorg` | saharshpoddar123@gmail.com | [github.com/saharshpoddarorg](https://github.com/saharshpoddarorg) | Personal org |
| Confluence (personal) | — | saharshpoddar060699@gmail.com | [saharshpoddarorg.atlassian.net/wiki](https://saharshpoddarorg.atlassian.net/wiki/home) | Personal wiki |
| Gmail (primary) | — | saharshpoddar123@gmail.com | [mail.google.com](https://mail.google.com) | Primary personal email |
| Gmail (dev/work) | — | saharshpoddar060699@gmail.com | [mail.google.com](https://mail.google.com) | Dev + personal Confluence |
| Google Keep | — | saharshpoddar123@gmail.com | [keep.google.com](https://keep.google.com) | Multi-purpose notes |
| Google Docs/Sheets | — | saharshpoddar123@gmail.com | [docs.google.com](https://docs.google.com) | Cloud documents |
| Notion | — | saharshpoddar123@gmail.com | [notion.so](https://www.notion.so) | Personal workspace |
| Apple Notes | — | <!-- TODO: Apple ID email --> | [icloud.com/notes](https://www.icloud.com/notes) | iOS/macOS notes |
| ike | — | <!-- TODO: ike account email --> | — | Mobile task tracker |
| WhatsApp | — | <!-- TODO: phone number --> | [web.whatsapp.com](https://web.whatsapp.com) | Self-chat capture |

### Work Accounts (Siemens — DISW LCS)

| Service | Account identifier | Email / SSO | URL | Notes |
|---|---|---|---|---|
| GitHub (work) | `saharsh-poddar_SAGCP` | Siemens corporate SSO | [github.com](https://github.com) | Work org repos |
| Confluence (work) | — | Siemens corporate SSO | [ies-iesd-conf.ies.mentorg.com](https://ies-iesd-conf.ies.mentorg.com/) | Team wiki |
| Jira (work) | — | Siemens corporate SSO | Siemens internal instance | Sprint tracking |
| Outlook (work) | — | Siemens corporate email | [outlook.office.com](https://outlook.office.com) | Work email + calendar |
| Teams (work) | — | Siemens corporate M365 | [teams.microsoft.com](https://teams.microsoft.com) | Self-chat + meetings |
| OneNote (work) | — | Siemens corporate M365 | [onenote.com](https://www.onenote.com) | Notebook: "Saharsh @ Siemens AG" |

### Browser Accounts

| Browser | Sync account | Context |
|---|---|---|
| Microsoft Edge | <!-- TODO: Edge sync account --> | Work + personal |
| Google Chrome | saharshpoddar123@gmail.com | Personal |
| Chrome Canary | <!-- TODO: if different --> | Bleeding-edge testing |
| Mozilla Firefox | <!-- TODO: Firefox account email --> | Varies |

---

## Content Sensitivity Classification

Every capture source contains content at different sensitivity levels. This classification
determines **what can be accessed, fetched, or scraped** by AI assistants and automation.

### Sensitivity Levels

| Level | Label | Description | AI access | Examples |
|---|---|---|---|---|
| **L0** | `public` | Publicly available content | ✅ Freely accessible | Public GitHub repos, blog posts, documentation |
| **L1** | `internal` | Non-public but non-sensitive | ⚠️ Ask before accessing | Work documentation, internal wikis, meeting notes |
| **L2** | `confidential` | Sensitive personal or work content | ❌ Do NOT access | Bank details, passwords, health info, HR docs |
| **L3** | `restricted` | Legally or contractually restricted | ❌ NEVER access | NDA content, proprietary code, trade secrets |

### Per-Source Sensitivity Map

| Source | Default level | Has L2+ content? | Restricted labels/areas |
|---|---|---|---|
| **Jira (work)** | `L1 internal` | Possible | Ticket content may contain proprietary details |
| **Confluence (work)** | `L1 internal` | Yes | All work pages — corporate IP |
| **Notepad++ (work)** | `L1 internal` | Possible | Local scratch — may contain work secrets |
| **OneNote (work)** | `L1 internal` | Possible | All sections — work context |
| **Outlook (work)** | `L1 internal` | Yes | Email content — corporate communications |
| **Teams self-chat** | `L1 internal` | Yes | May contain work-sensitive links and notes |
| **GitHub (work)** | `L3 restricted` | Yes | All repos — proprietary source code |
| **Google Keep** | `mixed` | **Yes** | See label-level breakdown below |
| **Notion** | `L1 internal` | Possible | Work pages may contain sensitive content |
| **Confluence (personal)** | `L0 public` | No | Personal projects — non-sensitive |
| **Gmail** | `L2 confidential` | Yes | Email content — personal communications |
| **Apple Notes** | `L1 internal` | Possible | Personal notes — assess per note |
| **Google Docs/Sheets** | `L1 internal` | Possible | May contain budgets, personal data |
| **VS Code** | `L0 public` | No | Code editing — content varies by project |
| **GitHub (personal)** | `L0 public` | No | Personal repos — public or private-non-sensitive |
| **brain/ai-brain** | `L0 public` | No | Git-tracked — no secrets here |
| **ike** | `L1 internal` | No | Personal tasks — non-sensitive |
| **WhatsApp self-chat** | `L2 confidential` | **Yes** | Personal documents, photos, sensitive notes |
| **Phone camera** | `L2 confidential` | **Yes** | May contain personal/work-sensitive photos |
| **Browser bookmarks** | `L0 public` | No | URLs only — non-sensitive |
| **Physical notebooks** | `L1 internal` | Possible | Handwritten — may contain sensitive notes |
| **Office whiteboard** | `L1 internal` | Possible | May contain work-sensitive diagrams |

### Google Keep — Label-Level Sensitivity

Google Keep has mixed sensitivity because different labels contain different content types:

| Label | Sensitivity | AI access | Reason |
|---|---|---|---|
| Work | `L1 internal` | ⚠️ Ask first | May contain work-sensitive quick notes |
| Learning | `L0 public` | ✅ Open | Educational content — non-sensitive |
| Ideas | `L0 public` | ✅ Open | Creative ideas — non-sensitive |
| Todos | `L1 internal` | ⚠️ Ask first | May mix work and personal tasks |
| Shopping | `L0 public` | ✅ Open | Grocery lists — non-sensitive |
| **Confidential** | **`L2 confidential`** | **❌ NEVER** | Passwords, bank details, private info |
| **Finance** | **`L2 confidential`** | **❌ NEVER** | Bills, payments, financial data |
| Personal | `L1 internal` | ⚠️ Ask first | Family, health, appointments — ask first |
| *(unlabelled)* | `L1 internal` | ⚠️ Ask first | Unknown sensitivity — treat as internal |

---

## Do Not Access — Restricted Content

> **CRITICAL:** The following content areas must **NEVER** be accessed, read, fetched,
> scraped, or summarised by any AI assistant, automation tool, or script — regardless
> of context. This is a hard security boundary.

### Permanently Restricted (L2 + L3 — NEVER access)

| Source | Restricted area | Reason |
|---|---|---|
| Google Keep | Notes labelled **"Confidential"** | Passwords, sensitive personal info |
| Google Keep | Notes labelled **"Finance"** | Bank details, bills, financial data |
| Gmail | Email content (body, attachments) | Personal communications — private |
| Outlook (work) | Email content (body, attachments) | Corporate communications — NDA |
| GitHub (work) | All repositories and source code | Proprietary — Siemens IP |
| Confluence (work) | All page content | Corporate documentation — NDA |
| Jira (work) | All ticket content | Corporate project data — NDA |
| WhatsApp self-chat | Messages containing personal documents | Personal privacy |
| Phone camera | Photos/screenshots not explicitly shared | Personal privacy |
| OneNote (work) | All notebook content | Corporate context — NDA |
| Teams self-chat | All message content | Corporate context — may contain NDA content |

### Conditionally Restricted (L1 — ask before accessing)

| Source | Area | Access rule |
|---|---|---|
| Google Keep — "Work" label | All notes | Ask user before reading any note |
| Google Keep — "Personal" label | All notes | Ask user before reading any note |
| Google Keep — "Todos" label | All notes | Ask user before reading any note |
| Google Keep — unlabelled | All notes | Ask user before reading any note |
| Notion — Work pages | All content | Ask user before reading |
| Notion — Personal pages | All content | Ask user before reading |
| Google Docs/Sheets | All documents | Ask user before reading |
| Apple Notes | All notes | Ask user before reading |
| Physical notebooks | Digitised scans | Ask user before reading |

### Always Accessible (L0 — open)

| Source | Area | Notes |
|---|---|---|
| Google Keep — "Learning" label | All notes | Educational content |
| Google Keep — "Ideas" label | All notes | Creative brainstorms |
| Google Keep — "Shopping" label | All notes | Non-sensitive lists |
| GitHub (personal) — public repos | Public content | Already public |
| brain/ai-brain | All tiers | Git-tracked, designed for AI access |
| Confluence (personal) | Personal wiki pages | Personal projects |
| Browser bookmarks | All URLs | Non-sensitive metadata |
| VS Code workspaces | Open files/settings | Code being actively edited |

### Access Decision Flowchart

```text
Content access request
    ↓
Is the source in "Permanently Restricted" list?
  ├── Yes → DENY. Log denial in access-log.md. Stop.
  └── No
       ↓
Is the source in "Conditionally Restricted" list?
  ├── Yes → ASK user for explicit permission.
  │         User grants? → Log grant + proceed.
  │         User denies? → Log denial. Stop.
  └── No → Source is "Always Accessible" → proceed.
       ↓
Fetch/read the content.
    ↓
Log access in access-log.md (timestamp, source, what, why, outcome).
```

---

## Access Logging & Policy

All content access by AI assistants is logged in [access-log.md](access-log.md).
The full policy governing access decisions is in [access-policy.md](access-policy.md).

### Logging Requirements

Every access (successful or denied) creates a log entry:

```text
| Timestamp | Source | Content accessed | Action | Reason | Outcome |
```

**What gets logged:**

- ✅ Web scraping of any URL associated with a capture source
- ✅ File reads of content from external capture sources
- ✅ API calls to capture source services (if any)
- ✅ Access denials (when restricted content is requested)
- ✅ Permission grants (when user explicitly allows conditional access)
- ✅ Content imports via `/jot` or `/read-file-jot` that reference external sources

**What does NOT get logged:**

- Reading files within this Git repo (brain/ai-brain/*)
- Normal Copilot Chat interactions about code in the workspace
- Web searches for general information unrelated to capture sources

### Quick Policy Summary

| Principle | Rule |
|---|---|
| **Default deny for L2+** | Never access confidential or restricted content |
| **Ask for L1** | Always ask before reading internal/personal content |
| **Open for L0** | Freely access public content |
| **Always log** | Every access attempt is logged (success and denial) |
| **Never scrape until asked** | Do not proactively fetch content from capture sources |
| **User controls access** | User can grant or revoke access at any time |
| **Inform on access** | Always tell the user what was accessed, when, and why |

Full policy: [access-policy.md](access-policy.md)
Full log: [access-log.md](access-log.md)

---

## PKM ↔ Backlog Integration

The `pkm/` folder and the `backlog/` folder in brain/ai-brain serve complementary roles.
They are **siblings, not nested** — PKM is about where knowledge lives and how it flows;
backlog is about what work to track and do.

### How They Connect

```text
pkm/                              backlog/
├── capture-sources-inventory.md  ├── BOARD.md (kanban)
├── access-policy.md              ├── items/ features/ projects/
├── access-log.md                 ├── ideas/
│                                 ├── epics/
│   "Where do I capture?"         │   "What do I need to do?"
│   "What tools do I use?"        │   "What's the priority?"
│   "What's restricted?"          │   "When is the sprint?"
└── ──────────────────────        └── ────────────────────────
          │                                    │
          └──────────────┬─────────────────────┘
                         │
               Integration points:
               1. /jot captures from any source → backlog
               2. /read-file-jot imports from files → backlog
               3. Content fetch from sources → logged in pkm/
               4. Backlog items reference source tools
               5. Access policy governs what can be fetched
```

### Feature Map — PKM Management Capabilities

| Feature | Location | Commands | Description |
|---|---|---|---|
| **Jot Down** | `backlog/` | `/jot`, `/todo`, `/read-file-jot` | Capture ideas, todos, items from any source into backlog |
| **Kanban / Scrum** | `backlog/` | `/todos`, `/backlog sprint` | Visual board, sprint tracking, status management |
| **Source Inventory** | `pkm/` | (this document) | Registry of all capture sources, accounts, sensitivity |
| **Access Policy** | `pkm/` | (reference doc) | Rules for what can/cannot be accessed |
| **Access Logging** | `pkm/` | (auto-populated) | Audit trail of all content access |
| **Content Fetch** | `pkm/` → `backlog/` or `notes/` | `/fetch-source` (future) | Pull content from capture sources into brain |
| **Content Push** | `backlog/` → external | `/push-source` (future) | Update external sources from brain (future) |
| **Source Sync** | `pkm/` ↔ external | `/sync-source` (future) | Bidirectional sync with capture sources (future) |

### Jot Down Integration

The existing `/jot` and `/read-file-jot` commands already bridge PKM sources and backlog:

| Source | How to import | Example |
|---|---|---|
| Notepad++ scratch file | `/read-file-jot "C:\notes\todos.txt"` | Extracts items from work scratch |
| Google Keep note (text) | `/jot "pasted content from Keep"` | User pastes, system classifies |
| Teams self-chat (text) | `/jot "copied from Teams"` | User copies, system classifies |
| WhatsApp self-chat (text) | `/jot "copied from WhatsApp"` | User copies, system classifies |
| Browser bookmark | `/jot "https://example.com — topic"` | Creates reference-type item |
| Physical notebook (photo) | Phone photo → `/jot "description"` | Manual description of captured image |

**Future integrations** (not yet implemented — tracked in backlog):

- Direct Google Keep API access (with permission)
- Confluence page import
- Notion database sync
- Browser bookmark export/import

---

## Unused / Available Tools

Tools I'm aware of but **do not currently use** for capture or note-taking.
Kept here for reference in case I want to adopt them in the future.

### Desktop / Cloud Note-Taking

| Tool | Type | Why not using | Potential use case |
|---|---|---|---|
| **Obsidian** | Local Markdown editor with graph view | Already using brain/ai-brain for Markdown notes; Notion for structured content | If I want local-first, plugin-rich PKM with bidirectional links |
| **Logseq** | Open-source, outliner-style notes | Similar to Obsidian — already covered by brain/ai-brain + Notion | If I want open-source, block-based, daily-journal-first workflow |
| **Evernote** | Cloud note-taking (legacy) | Notion and Google Keep cover this space better | Legacy migration source if switching from Evernote |
| **Roam Research** | Networked thought / outliner | Expensive ($15/mo); Obsidian/Logseq do the same for free | If I need heavy bidirectional linking and block references |
| **Bear** | Apple-native Markdown notes | Apple-only; Obsidian is cross-platform | If I go all-in on Apple ecosystem |
| **Craft** | Apple-native documents | Apple-only; Notion is cross-platform | If I want beautiful Apple-native documents |
| **Simplenote** | Plain text sync | Too basic; Google Keep + Notepad++ cover this | If I want ultra-minimal cross-platform text sync |

### Task Management

| Tool | Type | Why not using | Potential use case |
|---|---|---|---|
| **Microsoft To Do** | Task lists (Microsoft) | Using ike for personal + Jira for work | If I want Outlook integration and Microsoft ecosystem tasks |
| **Todoist** | Cross-platform task manager | ike covers personal tasks; Jira covers work | If I want a more powerful task manager with projects and labels |
| **TickTick** | Task + habit tracker | Not needed alongside ike | If I want habit tracking integrated with tasks |
| **Any.do** | Simple task lists | Not needed — ike, Google Keep cover this | If I want a simpler alternative to ike |
| **Trello** | Kanban boards (Atlassian) | brain/ai-brain/backlog is my kanban; Jira for work | If I want visual Kanban for personal projects |
| **Asana** | Project management | Overkill for personal use; Jira for work | If I join a team using Asana |
| **Linear** | Modern issue tracking | Jira for work; brain/ai-brain/backlog for personal | If I want a faster, more modern alternative to Jira |

### Code Snippet & Developer Tools

| Tool | Type | Why not using | Potential use case |
|---|---|---|---|
| **GitHub Gists** | Code snippet sharing | Not actively using — snippets stay in repos | If I want to share standalone code snippets publicly |
| **Stack Overflow** | Q&A community / saved answers | Not actively bookmarking answers | If I want to build a personal library of useful SO answers |
| **Dash / Zeal** | Offline API documentation browser | Using web docs directly | If I need offline API docs during travel |
| **DevDocs.io** | Unified API docs in browser | Using official docs directly | If I want a single interface for all API docs |
| **Snippet managers** | SnippetsLab, Lepton, massCode | VS Code user snippets cover this | If I need a dedicated cross-editor snippet library |

### Read-Later / Bookmarking

| Tool | Type | Why not using | Potential use case |
|---|---|---|---|
| **Pocket** | Read-later service (Mozilla) | Browser bookmarks + Google Keep cover this | If I want offline reading and article archiving |
| **Instapaper** | Read-later (clean reading view) | Same as Pocket | If I want distraction-free article reading |
| **Raindrop.io** | Visual bookmarking + collections | Browser bookmarks across 3 browsers are scattered | **Strong candidate** — could consolidate cross-browser bookmarks |
| **Wallabag** | Self-hosted read-later | Not needed unless privacy-critical | If I want self-hosted article archiving |
| **Pinboard** | No-frills bookmarking | $22/year; free alternatives exist | If I want fast, searchable, tag-based bookmarks |

### Writing & Documentation

| Tool | Type | Why not using | Potential use case |
|---|---|---|---|
| **Typora** | Distraction-free Markdown editor | VS Code handles Markdown well | If I want a dedicated Markdown writing experience |
| **iA Writer** | Focused writing app | Not needed alongside VS Code | If I want ultra-focused long-form writing |
| **HackMD / CodiMD** | Collaborative Markdown | Confluence and Notion handle collaboration | If I need real-time collaborative Markdown editing |
| **Docusaurus / MkDocs** | Static documentation sites | README + Confluence sufficient for now | If I need to publish documentation as a website |

### Communication & Self-Chat Alternatives

| Tool | Type | Why not using | Potential use case |
|---|---|---|---|
| **Slack** | Team messaging | Not using a personal Slack workspace | If I join a team/community using Slack |
| **Telegram — Saved Messages** | Self-chat (encrypted) | WhatsApp self-chat covers this | If I want better search and file organization than WhatsApp |
| **Discord** | Community messaging | Not needed for personal PKM | If I join dev communities on Discord |
| **Signal** | Encrypted messaging (Note to Self) | WhatsApp covers this | If I want stronger privacy for self-notes |

### Physical / Analog

| Tool | Type | Why not using | Potential use case |
|---|---|---|---|
| **Index cards (Leitner)** | Spaced repetition flashcards | Not actively doing flashcard study | If I want physical spaced repetition for interview prep |
| **Bullet Journal** | Structured analog journaling (BuJo) | Using digital tools primarily | If I want a more structured approach to physical journaling |
| **Rocketbook** | Reusable smart notebook | Interesting but not needed | If I want OCR-scannable handwritten notes that auto-sync |
| **Sticky Notes (Windows)** | Desktop sticky notes | Using Notepad++ and Teams self-chat instead | If I want persistent desktop reminders visible at all times |
| **Windows Notepad** | Plain text editor | Notepad++ is a strict superset of Notepad | If Notepad++ is unavailable (fresh Windows install) |

### AI & Knowledge Tools

| Tool | Type | Why not using | Potential use case |
|---|---|---|---|
| **Mem.ai** | AI-powered notes with auto-linking | Notion + brain/ai-brain cover this | If I want AI-assisted note organization |
| **Reflect** | AI-native note-taking | Too new; waiting for maturity | If I want GPT-integrated notes with graph view |
| **Readwise / Reader** | Highlight syncing from books/articles | Not reading enough highlights to justify | If I start heavy reading and want highlight sync |
| **Capacities** | Object-based note-taking | Unique approach but Notion is established | If I want entity-based rather than page-based notes |

---

## Source Retirement Log

When a tool or source is no longer in use, move its entry's status to `retired` and
log it here for historical reference. Do not delete the section — mark it
`Status: retired` instead.

| Source | Retired date | Reason | Data migrated to |
|---|---|---|---|
| *(none yet)* | | | |

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
| **Quarterly** | Review Unused Tools list — adopt or remove candidates |

---

## Related Documents

- [access-policy.md](access-policy.md) — Full access control policy for capture sources
- [access-log.md](access-log.md) — Audit trail of all content access events
- [pkm-philosophy.md](../pkm-philosophy.md) — Why ai-brain is structured the way it is
- [digitalnotetaking/START-HERE.md](../../digitalnotetaking/START-HERE.md) — Getting started with digital PKM
- [digitalnotetaking/tools-comparison.md](../../digitalnotetaking/tools-comparison.md) — Tool comparison matrix
- [digitalnotetaking/para-method.md](../../digitalnotetaking/para-method.md) — PARA method deep dive
- [digitalnotetaking/code-method.md](../../digitalnotetaking/code-method.md) — CODE method deep dive
- [ai-brain/README.md](../README.md) — How to use the ai-brain workspace
- [backlog/README.md](../backlog/README.md) — Backlog system documentation
