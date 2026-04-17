---
name: pkm-management
description: >
  PKM (Personal Knowledge Management) system management — capture source inventory,
  content access control, access logging, content fetch/push operations, and integration
  with the backlog jot-down system. Use when asked about: managing capture sources,
  what tools are used for note-taking, accessing content from external tools (Google Keep,
  Notion, Confluence, etc.), content sensitivity and access restrictions, logging content
  access, fetching or importing content from capture sources, PKM access policy,
  which content is restricted or confidential, how capture sources connect to backlog,
  jot-down feature integration with PKM sources, or any brain/ai-brain/pkm/ management.
---

# PKM Management Skill — Capture Sources, Access Control & Logging

> Manages the personal knowledge management infrastructure: source inventory,
> content sensitivity classification, access control policy, audit logging,
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

### Key Commands

| Command | Effect |
|---|---|
| `/jot "text"` | Capture idea/todo/item into backlog from any source |
| `/read-file-jot "path"` | Import items from a local file into backlog |
| `/todos` | View the backlog board |
| `/fetch-source` | (future) Pull content from capture sources |
| `/push-source` | (future) Update external sources from brain |

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
| Content fetch | Source → Brain | Read from source → route to inbox/notes/library |
| Access governance | PKM → All | Access policy controls all content reads |
| Audit trail | All → PKM | All access logged in access-log.md |

### Content Routing After Fetch

```text
Content fetched from capture source
    ↓
What kind of content is it?
  ├── Raw, unsorted → brain/ai-brain/inbox/
  ├── Reference material → brain/ai-brain/library/
  ├── Actionable task/idea → /jot → brain/ai-brain/backlog/
  ├── Learning note → brain/ai-brain/notes/
  └── Session context → brain/ai-brain/sessions/
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

| Phase | Feature | Status |
|---|---|---|
| **Phase 0** (current) | Inventory + policy + logging framework | ✅ Done |
| **Phase 1** | Manual paste-and-jot (user copies content, `/jot` processes) | ✅ Done |
| **Phase 2** | File-based import (`/read-file-jot`) | ✅ Done |
| **Phase 3** | Direct source API access (Keep, Confluence, Notion) | 📋 Planned |
| **Phase 4** | Bidirectional sync (read + write back to sources) | 📋 Future |
| **Phase 5** | Automated periodic fetch with scheduled logging | 📋 Future |

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
