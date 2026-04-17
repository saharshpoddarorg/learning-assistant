# Capture Sources — Personal Tools

> **Context:** All personal capture tools — digital, mobile, browser-based, and physical.
> **Sensitivity:** Varies per tool — see each entry's sensitivity field.
> See [sensitivity-and-access-control.md](sensitivity-and-access-control.md) for full classification.
>
> **Back to:** [PKM Hub](README.md) | [Consolidated inventory](capture-sources-inventory.md)

---

## Personal Sources Summary

### Digital (9 tools)

| # | Tool | Type | Primary use | Sensitivity |
|---|---|---|---|---|
| 1 | Google Keep | Quick notes + lists | Multi-purpose (labelled) | Mixed (L0–L2) |
| 2 | Notion | All-in-one workspace | Long-form structured content | L1 internal |
| 3 | Confluence (personal) | Personal wiki | Software project documentation | L0 public |
| 4 | Gmail | Email | Starred follow-ups, newsletters | L2 confidential |
| 5 | Apple Notes | Built-in notes | Quick capture on Apple devices | L1 internal |
| 6 | Google Docs / Sheets | Cloud documents | Shared docs, spreadsheets | L1 internal |
| 7 | VS Code | Code editor | Code, snippets, Copilot sessions | L0 public |
| 8 | GitHub (personal) | Code hosting | Source code, issues, PRs | L0 public |
| 9 | brain/ai-brain | Structured PKM | Notes, sessions, backlog, library | L0 public |

### Mobile (3 tools)

| # | Tool | Type | Primary use | Sensitivity |
|---|---|---|---|---|
| 10 | ike | Task tracker | Personal tasks + reminders | L1 internal |
| 11 | WhatsApp self-chat | Self-messaging | Ultra-fast cross-context capture | L2 confidential |
| 12 | Phone camera | Visual capture | Photos, screenshots, whiteboard shots | L2 confidential |

### Browser (3 tools)

| # | Tool | Type | Primary use | Sensitivity |
|---|---|---|---|---|
| 13 | Microsoft Edge | Primary browser | Bookmarks, workspaces, tab groups | L0 public |
| 14 | Chrome / Canary | Secondary browser | Personal bookmarks, testing | L0 public |
| 15 | Mozilla Firefox | Privacy browser | Specific-context bookmarks | L0 public |

### Physical (3 tools)

| # | Tool | Type | Primary use | Sensitivity |
|---|---|---|---|---|
| 16 | Office diaries | Paper notebooks | Meeting notes, daily task lists | L1 internal |
| 17 | A4 paper | Loose sheets | Architecture sketches, quick notes | L1 internal |
| 18 | Office whiteboard | Whiteboard | Diagrams, brainstorming, sprint boards | L1 internal |

---

## Digital Tools

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
| **Sensitivity** | Mixed — see label-level breakdown |
| **Offline** | Yes (mobile app) |

**What I capture here (multi-purpose — label-separated):**

Google Keep serves multiple roles. **Not all notes here are PKM-relevant.** The labels
below separate work/learning capture from personal-only content.

#### PKM-Relevant Labels (promote to brain/ai-brain or other tools)

| Label | Purpose | Promote to | Sensitivity |
|---|---|---|---|
| Work | Work-related quick notes, ideas, reminders | Jira / Confluence / OneNote | L1 internal |
| Learning | Concepts, resources, topics to study | brain/ai-brain/notes or backlog | L0 public |
| Ideas | Feature ideas, project concepts, brainstorms | brain/ai-brain/backlog/ideas | L0 public |
| Todos | Actionable items across work and personal | ike / Jira / brain/ai-brain/backlog | L1 internal |

#### Personal-Only Labels (NOT part of PKM — stay in Google Keep)

| Label | Purpose | Notes | Sensitivity |
|---|---|---|---|
| Shopping | Grocery lists, purchase tracking | Pure personal — not promoted | L0 public |
| **Confidential** | Passwords, sensitive details, private info | **Never** in Git-tracked files | **L2 confidential** |
| Personal | Personal reminders, family, health, appointments | Stays in Google Keep | L1 internal |
| **Finance** | Bills, payments, financial reminders | Stays in Google Keep | **L2 confidential** |

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
| **Sensitivity** | L1 internal (work pages may contain sensitive content) |
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
| **Sensitivity** | L0 public (personal projects — non-sensitive) |
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
| **Sensitivity** | L2 confidential (email content — personal communications) |

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
| **Account** | <!-- TODO: Apple ID email --> |
| **Sync** | iCloud (automatic) |
| **Sensitivity** | L1 internal (assess per note) |
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
| **Sensitivity** | L1 internal (may contain budgets, personal data) |
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
| **Sensitivity** | L0 public (content varies by project) |
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
| **Sensitivity** | L0 public (personal repos — public or private-non-sensitive) |
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
| **Sensitivity** | L0 public (Git-tracked — no secrets) |
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

## Mobile Tools

### ike (Mobile Task Tracker)

| Property | Details |
|---|---|
| **Type** | Task tracker and reminder app (mobile) |
| **Status** | `active` |
| **Context** | `personal` — primary personal task manager |
| **Platform** | Mobile (iOS / Android) |
| **Cost** | Free |
| **Account** | <!-- TODO: ike account email --> |
| **Sync** | Cloud (app-specific) |
| **Sensitivity** | L1 internal (personal tasks — non-sensitive) |
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
| **Account** | <!-- TODO: phone number --> |
| **Sync** | WhatsApp cloud (end-to-end encrypted) |
| **Sensitivity** | L2 confidential (personal documents, photos, sensitive notes) |
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
| **Sensitivity** | L2 confidential (may contain personal/work-sensitive photos) |

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
| **Sensitivity** | L0 public (bookmarks are URLs — non-sensitive) |
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
| **Sensitivity** | L0 public (bookmarks are URLs — non-sensitive) |

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
| **Sensitivity** | L0 public (bookmarks are URLs — non-sensitive) |

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
| **Sensitivity** | L1 internal (may contain sensitive handwritten notes) |

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
| **Sensitivity** | L1 internal |

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
| **Sensitivity** | L1 internal (may contain work-sensitive diagrams) |

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

## Related Documents

- [PKM Hub](README.md) — Central index
- [sources-work.md](sources-work.md) — Work capture tools (Siemens)
- [accounts-and-credentials.md](accounts-and-credentials.md) — All account details
- [sensitivity-and-access-control.md](sensitivity-and-access-control.md) — Access control rules
- [workflows.md](workflows.md) — Cross-source content flow pipelines
