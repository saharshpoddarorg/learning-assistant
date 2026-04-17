# Content Sensitivity & Access Control

> **Purpose:** Classifies all capture source content by sensitivity level and defines
> what AI assistants and automation can or cannot access.
>
> **Back to:** [PKM Hub](README.md) | [Consolidated inventory](capture-sources-inventory.md)
>
> **Full policy:** [access-policy.md](access-policy.md)
> **Audit log:** [access-log.md](access-log.md)

---

## Sensitivity Levels

| Level | Label | Description | AI access | Examples |
|---|---|---|---|---|
| **L0** | `public` | Publicly available content | ✅ Freely accessible | Public GitHub repos, blog posts, documentation |
| **L1** | `internal` | Non-public but non-sensitive | ⚠️ Ask before accessing | Work documentation, internal wikis, meeting notes |
| **L2** | `confidential` | Sensitive personal or work content | ❌ Do NOT access | Bank details, passwords, health info, HR docs |
| **L3** | `restricted` | Legally or contractually restricted | ❌ NEVER access | NDA content, proprietary code, trade secrets |

---

## Per-Source Sensitivity Map

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

---

## Google Keep — Label-Level Sensitivity

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

---

## Access Decision Flowchart

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

## Quick Policy Summary

| Principle | Rule |
|---|---|
| **Default deny for L2+** | Never access confidential or restricted content |
| **Ask for L1** | Always ask before reading internal/personal content |
| **Open for L0** | Freely access public content |
| **Always log** | Every access attempt is logged (success and denial) |
| **Never scrape until asked** | Do not proactively fetch content from capture sources |
| **User controls access** | User can grant or revoke access at any time |
| **Inform on access** | Always tell the user what was accessed, when, and why |

---

## Related Documents

- [PKM Hub](README.md) — Central index
- [access-policy.md](access-policy.md) — Full access control policy with protocols
- [access-log.md](access-log.md) — Audit trail of all access events
- [accounts-and-credentials.md](accounts-and-credentials.md) — Account identifiers
