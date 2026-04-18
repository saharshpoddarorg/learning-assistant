---
name: pkm-management
description: >
  PKM (Personal Knowledge Management) system management — capture source inventory,
  content access control, access logging, git-inspired content operations (fetch, pull,
  cherry-pick, merge, push, stash, diff, log), brain consolidation from external sources
  (Notion, Confluence, Google Keep, etc.), and integration with the backlog jot-down system.
  Use when asked about: managing capture sources, consolidating notes from external tools into
  brain/ai-brain, fetching or importing content from capture sources, content sensitivity and
  access restrictions, logging content access, PKM access policy, which content is restricted
  or confidential, how capture sources connect to backlog, jot-down feature integration with
  PKM sources, migrating notes from Notion/Confluence/Keep into brain, or any brain/ai-brain/pkm/
  management.
---

# PKM Management Skill — Capture Sources, Access Control & Content Operations

> Manages the personal knowledge management infrastructure: source inventory,
> content sensitivity classification, access control policy, audit logging,
> git-inspired content operations, brain consolidation from external sources,
> and integration with the brain/ai-brain backlog system.

---

## Quick Reference

### Key Files

| File | Location | Purpose |
|---|---|---|
| PKM Hub (README) | `brain/ai-brain/pkm/README.md` | Central index — start here |
| Sources — Work | `brain/ai-brain/pkm/sources-work.md` | Work capture tools (Siemens) — 7 tools |
| Sources — Personal | `brain/ai-brain/pkm/sources-personal.md` | Personal, mobile, browser, physical — 18 tools |
| Accounts & Credentials | `brain/ai-brain/pkm/accounts-and-credentials.md` | Account identifiers (no secrets) |
| Sensitivity & Access | `brain/ai-brain/pkm/sensitivity-and-access-control.md` | Content classification + Do Not Access rules |
| Workflows | `brain/ai-brain/pkm/workflows.md` | Cross-source content flow pipelines |
| Unused Tools | `brain/ai-brain/pkm/unused-tools.md` | Tools not in use + retirement log |
| Access Policy | `brain/ai-brain/pkm/access-policy.md` | Rules for content access control |
| Access Log | `brain/ai-brain/pkm/access-log.md` | Audit trail of all access events |
| Consolidated Inventory | `brain/ai-brain/pkm/capture-sources-inventory.md` | All-in-one single-file reference |

### Key Commands — Git-Inspired Content Operations

The PKM system uses **git/VCS-inspired verbs** for content operations between external
capture sources and brain/ai-brain tiers.

#### Content Retrieval (Source → Brain)

| Command | Git analogy | What it does |
|---|---|---|
| `/brain fetch <source>` | `git fetch` | First-time retrieval from a source — copies content into `inbox/` for triage. Does NOT route or process. |
| `/brain pull <source>` | `git pull` | Update/refresh previously fetched content — re-reads from source, merges changes into existing brain files. Like `fetch + merge`. |
| `/brain clone <source>` | `git clone` | Full import of an entire source (e.g., all Notion pages, entire Confluence space) into brain. Creates a structured mirror in `library/` or `notes/`. |
| `/brain cherry-pick <source> <item>` | `git cherry-pick` | Selectively import ONE specific item (a single note, page, or entry) from a source into brain. |

#### Content Routing & Processing (Brain internal)

| Command | Git analogy | What it does |
|---|---|---|
| `/brain merge <inbox-item>` | `git merge` | Route an inbox item to its proper tier (notes/, library/, backlog/) after review. Resolves "where does this go?" |
| `/brain stash <item>` | `git stash` | Temporarily park content that isn't ready for routing — stays in inbox with a `stashed` marker for later review. |
| `/brain stash pop` | `git stash pop` | Retrieve the most recently stashed item for processing. |

#### Content Export (Brain → Source)

| Command | Git analogy | What it does |
|---|---|---|
| `/brain push <source>` | `git push` | Export brain content back to an external source (e.g., publish notes to Notion, update Confluence page). |

#### Inspection & Comparison

| Command | Git analogy | What it does |
|---|---|---|
| `/brain diff <source>` | `git diff` | Compare brain's version of content vs. the external source — show what's changed since last fetch/pull. |
| `/brain log <source>` | `git log` | Show the access history for a source — reads from `access-log.md`. |
| `/brain status` | `git status` | Show what's in inbox (unfetched), what's stashed, what's been modified since last pull. Already exists in brain CLI. |
| `/brain remote` | `git remote` | List all configured capture sources with their sensitivity levels and access status. |

#### Jot-Down Integration (Quick Capture)

| Command | Git analogy | What it does |
|---|---|---|
| `/jot "text"` | — | Capture idea/todo/item into backlog from any source (existing). |
| `/read-file-jot "path"` | — | Import items from a local file into backlog (existing). |
| `/brain pick <source>` | `git cherry-pick` (shorthand) | Interactive: browse a source, select items to jot into backlog. |

#### The Git → PKM Analogy Map

```text
Git concept              PKM equivalent
─────────────           ────────────────
remote                   capture source (Notion, Keep, Confluence, etc.)
local repo               brain/ai-brain/ (all 6 tiers)
working directory        brain/ai-brain/inbox/ (unprocessed content)
staging area             brain/ai-brain/inbox/ (reviewed, ready to route)
commit                   route to tier (merge inbox → notes/library/backlog)
branch                   tier (notes/, library/, sessions/, backlog/)
fetch                    first-time read from source → inbox
pull                     update existing content from source
push                     export brain content to source
cherry-pick              select specific items from source
stash                    park content for later
diff                     compare brain vs. source
log                      access history (access-log.md)
clone                    full import of entire source
```

---

## Sensitivity Levels — The Access Hierarchy

```text
L0  public       ✅ Freely accessible (brain/, public repos, bookmarks)
L1  internal     ⚠️ Ask before accessing (Keep-Work, Notion, Apple Notes)
L2  confidential ❌ NEVER access (Keep-Confidential, Keep-Finance, Gmail)
L3  restricted   ❌ NEVER access (all work repos, Confluence work, Jira)
```

### Access Decision Flowchart

```text
Content access request
    ↓
L2 or L3? → DENY. Log denial. Stop.
    ↓
L1? → ASK user. Granted? → Log + proceed. Denied? → Log denial. Stop.
    ↓
L0 → Proceed. Log access.
```

---

## Content Access Protocol

### Before Accessing Any Capture Source Content

1. **Check sensitivity** — look up the source in the inventory's Per-Source Sensitivity Map
2. **Check restrictions** — verify the specific area/label is not in "Do Not Access"
3. **Apply policy** — follow the protocol for that sensitivity level
4. **Log the attempt** — record in access-log.md regardless of outcome

### Access Logging Format

Every access event (granted or denied) must be logged:

```markdown
### YYYY-MM-DD HH:MM AM/PM — <SOURCE> — <ACTION>

- **Source:** Google Keep — Learning label
- **Content:** Notes about Java streams
- **Action:** READ
- **Sensitivity:** L0
- **Permission:** not-required
- **Outcome:** success
- **Reason:** User asked to consolidate learning notes
- **Session:** sessions/personal/learning/2026-03-20_...
```

### What to Say to the User

**When accessing L0 content:**

> "I'm reading your Google Keep 'Learning' notes. This is classified as public (L0)
> and doesn't require permission. Logging the access."

**When requesting L1 access:**

> "I'd like to read your Google Keep 'Work' notes to find the items you mentioned.
> This is classified as internal (L1) — may I proceed?"

**When denying L2/L3 access:**

> "I can't access your Google Keep 'Confidential' notes — this is classified as L2
> confidential per the access policy. If you'd like help with that content, please
> paste the relevant non-sensitive parts here."

---

## Capture Sources — Quick Index

### Active Sources (25 total)

| Category | Sources | Count |
|---|---|---|
| Work (Siemens) | Jira, Confluence, Notepad++, OneNote, Outlook, Teams, GitHub | 7 |
| Personal Digital | Keep, Notion, Confluence, Gmail, Apple Notes, Docs/Sheets, VS Code, GitHub, brain | 9 |
| Mobile | ike, WhatsApp self-chat, Phone camera | 3 |
| Browser | Edge, Chrome/Canary, Firefox | 3 |
| Physical | Office diaries, A4 paper, Whiteboard | 3 |

### Google Keep — Label Sensitivity Quick Reference

| Label | Level | AI Access |
|---|---|---|
| Learning | L0 | ✅ Open |
| Ideas | L0 | ✅ Open |
| Shopping | L0 | ✅ Open |
| Work | L1 | ⚠️ Ask first |
| Todos | L1 | ⚠️ Ask first |
| Personal | L1 | ⚠️ Ask first |
| *(unlabelled)* | L1 | ⚠️ Ask first |
| **Confidential** | **L2** | **❌ Never** |
| **Finance** | **L2** | **❌ Never** |

---

## PKM ↔ Backlog Integration

The PKM system (`pkm/`) and the backlog system (`backlog/`) are **sibling tiers** within
brain/ai-brain. They are complementary, not nested:

- **PKM** = where knowledge lives, how it flows, access rules
- **Backlog** = what work to track, priority, sprint planning

### Integration Points

| Integration | Direction | How |
|---|---|---|
| Jot down from source | Source → Backlog | `/jot "content from Keep/Teams/etc."` |
| File import | File → Backlog | `/read-file-jot "path"` |
| Content fetch | Source → Brain | `/brain fetch <source>` → route to tier |
| Content pull | Source → Brain | `/brain pull <source>` → update existing |
| Cherry-pick items | Source → Backlog | `/brain pick <source>` → select items |
| Access governance | PKM → All | Access policy controls all content reads |
| Audit trail | All → PKM | All access logged in access-log.md |

### Content Routing After Fetch

```text
/brain fetch <source>
    ↓
Content lands in brain/ai-brain/inbox/ (unprocessed)
    ↓
/brain merge <item>  (or manual review)
    ↓
What kind of content is it?
  ├── Reference material → brain/ai-brain/library/
  ├── Actionable task/idea → /jot → brain/ai-brain/backlog/
  ├── Learning note → brain/ai-brain/notes/
  ├── Session context → brain/ai-brain/sessions/
  └── Not ready → /brain stash (park for later)
```

---

## Brain Consolidation — Migrating External Sources to brain/ai-brain

> **Goal:** Progressively move notes from scattered external tools (Notion, Confluence
> personal, Google Keep, Apple Notes, etc.) into brain/ai-brain as the single source
> of truth for personal knowledge.

### Why Consolidate?

| Problem | Solution |
|---|---|
| Notes scattered across 5+ tools | brain/ai-brain as single source of truth |
| Can't search across all sources | brain/ is fully searchable via `brain search` |
| No version history for Notion/Keep notes | Git-tracked brain/ has full history |
| Different formats per tool | Everything in Markdown, consistent frontmatter |
| Access requires internet/login | brain/ works offline, locally on disk |

### Consolidation Strategy — Per Source

| Source | Command | Strategy | Target tier |
|---|---|---|---|
| **Notion** | `/brain clone notion` | Full export → structured import to library/ and notes/ | library/ (reference), notes/ (authored) |
| **Confluence (personal)** | `/brain clone confluence-personal` | Export pages → import as library/ or notes/ | library/ (team docs), notes/ (your pages) |
| **Google Keep** | `/brain fetch keep` | Per-label selective import (L0 labels only) | notes/ (ideas, learning), backlog/ (todos) |
| **Apple Notes** | `/brain fetch apple-notes` | Selective import of useful notes | notes/ |
| **Google Docs/Sheets** | `/brain cherry-pick gdocs <doc>` | Import specific documents | library/ |
| **OneNote** | `/brain fetch onenote` | Export → import meeting notes and work notes | notes/ (personal), library/ (work reference) |
| **Browser bookmarks** | `/brain fetch bookmarks` | Export → import as curated link collection | library/ |

### Consolidation Workflow

```text
Step 1: /brain remote                    ← list all sources + sensitivity
Step 2: /brain fetch <source>            ← first-time import → inbox/
Step 3: Review inbox/ items
Step 4: /brain merge <item>              ← route each to correct tier
   OR   /brain stash <item>              ← park if not ready
Step 5: /brain diff <source>             ← later, check for changes
Step 6: /brain pull <source>             ← update with changes
```

### Consolidation Rules

1. **Sensitivity first** — never fetch L2/L3 content. Check `sensitivity-and-access-control.md`
2. **Log everything** — every fetch/pull/clone is logged in `access-log.md`
3. **Preserve origin** — fetched content gets frontmatter with `origin: <source>` and `fetched-at: <date>`
4. **Don't delete originals** — brain is an addition, not a replacement (until you're confident)
5. **Incremental** — consolidate one source at a time, don't try to import everything at once
6. **Dedup** — before importing, check if the content already exists in brain via `brain search`

### Frontmatter for Imported Content

```yaml
---
origin: notion
origin-url: https://www.notion.so/page-id
origin-title: "Original page title in Notion"
fetched-at: 2026-04-18
last-pulled: 2026-04-18
tier: library
project: personal
tags: [imported, notion, topic-tag]
---
```

---

## Tier 3 — Pro Patterns

### Multi-Account Awareness

Many sources have multiple accounts (work + personal). Always check which account
context is relevant before accessing:

| Source | Account 1 | Account 2 | Rule |
|---|---|---|---|
| GitHub | `saharshpoddar` (personal) | `saharsh-poddar_SAGCP` (work) | Work = L3, Personal = L0 |
| Confluence | saharshpoddarorg.atlassian.net (personal) | ies-iesd-conf (work) | Work = L3, Personal = L0 |
| Gmail | saharshpoddar123@gmail.com | saharshpoddar060699@gmail.com | Both = L2 |

### Access Audit Review

Periodically review the access log for patterns:

- Are we accessing sources too frequently? → Consider caching
- Are there repeated denials for the same source? → User may want to adjust policy
- Are L1 sources being accessed without asking? → Fix the protocol

### Future Integration Roadmap

| Phase | Feature | Commands | Status |
|---|---|---|---|
| **Phase 0** | Inventory + policy + logging framework | (reference docs) | ✅ Done |
| **Phase 1** | Manual paste-and-jot | `/jot` | ✅ Done |
| **Phase 2** | File-based import | `/read-file-jot` | ✅ Done |
| **Phase 3** | User-assisted content operations | `/brain fetch`, `/brain merge`, `/brain stash` | ✅ Done |
| **Phase 4** | Brain consolidation from external sources | `/brain clone`, `/brain pull`, `/brain diff` | ✅ Done |
| **Phase 5** | Selective item import | `/brain cherry-pick`, `/brain pick` | ✅ Done |
| **Phase 6** | Export back to sources | `/brain push` | ✅ Done |
| **Phase 7** | Automated periodic sync with logging | `/brain pull --auto`, `/brain diff --all` | 📋 Future |

---

## Anti-Patterns

| Anti-pattern | Problem | Fix |
|---|---|---|
| Accessing L2/L3 content | Violates access policy, legal risk | Always check sensitivity first |
| Not logging access | No audit trail, policy unenforceable | Log every access event |
| Blanket permission for L1 | User may not want all L1 accessed | Ask per-source, per-session |
| Storing credentials in Git | Security vulnerability | Use password manager / env vars |
| Scraping without informing user | Trust violation | Always announce what was accessed |
| Ignoring multi-account context | Wrong account → wrong sensitivity | Check account before accessing |

---

## Related Skills

| Skill | Relationship |
|---|---|
| `brain-management` | Parent skill — covers all brain tiers, naming, routing |
| `digital-notetaking` | Covers PKM methodologies (PARA, CODE, Zettelkasten) and tool features |
| `deep-research` | Research methodology that may trigger content fetches from sources |
| `requirements-research` | Requirements gathering that may reference capture sources |

---

## Related Documents

- [PKM Hub (README)](../../../brain/ai-brain/pkm/README.md) — Central index for all PKM files
- [Sources — Work](../../../brain/ai-brain/pkm/sources-work.md) — Work capture tools
- [Sources — Personal](../../../brain/ai-brain/pkm/sources-personal.md) — Personal capture tools
- [Accounts & Credentials](../../../brain/ai-brain/pkm/accounts-and-credentials.md) — Account identifiers
- [Sensitivity & Access](../../../brain/ai-brain/pkm/sensitivity-and-access-control.md) — Access classification
- [Workflows](../../../brain/ai-brain/pkm/workflows.md) — Content flow pipelines
- [Unused Tools](../../../brain/ai-brain/pkm/unused-tools.md) — Tools not in use
- [access-policy.md](../../../brain/ai-brain/pkm/access-policy.md) — Access control policy
- [access-log.md](../../../brain/ai-brain/pkm/access-log.md) — Audit trail
- [capture-sources-inventory.md](../../../brain/ai-brain/pkm/capture-sources-inventory.md) — Consolidated reference
- [backlog/README.md](../../../brain/ai-brain/backlog/README.md) — Backlog system docs
- [brain-management SKILL.md](../brain-management/SKILL.md) — Parent brain management skill
