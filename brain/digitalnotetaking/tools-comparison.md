# Tools Comparison — Notion vs Obsidian vs Logseq vs OneNote

> Which tool should you use? Here's the honest breakdown for software engineers.

---

## TL;DR Decision Tree

```text
Do you need team collaboration?
  ├── Yes → Notion (cloud, real-time co-editing)
  └── No
       ├── Are you in the Microsoft 365 ecosystem?
       │    ├── Yes → OneNote (already paid for, great Windows/iOS)
       │    └── No
       │         ├── Do you want open-source + version-controlled notes?
       │         │    ├── Yes → Logseq (or Obsidian + Git plugin)
       │         │    └── No → Obsidian (best general-purpose for devs)
       └── (Developers default: Obsidian)
```

---

## Side-by-Side Comparison

| Feature | Obsidian | Notion | Logseq | OneNote |
|---|---|---|---|---|
| **Storage** | Local `.md` files | Cloud (Notion servers) | Local `.md` / `.org` | OneDrive (cloud) |
| **Offline** | ✅ Full | ⚠️ Limited | ✅ Full | ✅ Windows |
| **Free** | ✅ Personal | ✅ Personal | ✅ Always | ✅ M365 |
| **Sync** | DIY (iCloud/Dropbox/Git) or Obsidian Sync ($8/mo) | Built-in | DIY or GitHub | OneDrive |
| **Mobile** | ✅ iOS + Android | ✅ iOS + Android | ✅ iOS + Android | ✅ iOS + Android |
| **Markdown** | ✅ Native | ⚠️ Export only | ✅ Native | ❌ Proprietary |
| **Bidirectional links** | ✅ | ⚠️ Basic | ✅ | ❌ |
| **Graph view** | ✅ | ❌ | ✅ | ❌ |
| **Databases / tables** | ⚠️ Plugin (Dataview) | ✅ Native | ⚠️ Queries | ⚠️ Basic tables |
| **Plugin ecosystem** | ✅ 1000+ community | ✅ Limited integrations | ✅ Active | ❌ |
| **Git-friendly** | ✅ Plain files | ❌ | ✅ Plain files | ❌ |
| **Team / enterprise** | ⚠️ Obsidian for Teams | ✅ Excellent | ❌ Not designed for this | ✅ M365 enterprise |
| **Learning curve** | Medium | Low | Medium-High | Low |
| **Customisability** | ✅ Very high | Medium | High | Low |

---

## Deep Dive — Obsidian

**Who it's for:** Developers, researchers, Markdown lovers, privacy-conscious users.

**Strengths:**
- Your notes are plain `.md` files — you own them forever
- Bidirectional links (`[[note-name]]`) create a knowledge graph
- Graph view visualises connections between ideas
- 1000+ community plugins (Dataview, Templater, Git sync, Excalidraw, etc.)
- Works on all platforms including Linux
- Can version-control your entire vault with Git

**Weaknesses:**
- Sync between devices requires setup (iCloud / Dropbox / Obsidian Sync)
- No real-time collaboration (Obsidian for Teams exists but is paid)
- Database features require the Dataview plugin (learning curve)

**Essential plugins for developers:**

```text
Dataview      ← query your notes like a database (TABLE, LIST, TASK)
Templater     ← dynamic templates with JS logic and date formatting
Git           ← auto-commit your vault to GitHub (daily snapshots)
Excalidraw    ← draw architecture diagrams inside Obsidian
Calendar      ← navigate daily notes visually
Spaced Repetition ← flashcards from your notes
```

**Recommended vault structure:**

```text
vault/
├── Inbox/              ← capture here first, sort later
├── Projects/           ← one folder per active project
│   └── mcp-servers/
├── Areas/
│   ├── Java/
│   └── Architecture/
├── Resources/          ← book summaries, article notes
├── Archives/           ← completed projects
└── Templates/          ← Templater templates
```

---

## Deep Dive — Notion

**Who it's for:** Teams, people who need databases, beginners, iOS/Android-first users.

**Strengths:**
- Zero setup time — works in a browser immediately
- Powerful linked databases (table, board, calendar, gallery, timeline views)
- Real-time collaboration — excellent for team wikis & project docs
- Great mobile experience on iOS and Android
- AI features built in (Notion AI)

**Weaknesses:**
- Notes live on Notion's servers — not great for private dev notes
- Limited offline support (downloaded pages only)
- Not Markdown-native — exporting is lossy
- Can get slow with large workspaces

**Recommended workspace structure:**

```text
Workspace Root
├── 📋 Projects DB      ← active projects with status, deadlines
├── 📚 Resources DB     ← tagged reference material
├── 📓 Daily Notes      ← one page per day (capture)
├── 🗂 Team Wiki        ← living docs, ADRs, onboarding
└── 🗃 Archives         ← completed projects (filtered view)
```

---

## Deep Dive — Logseq

**Who it's for:** Open-source advocates, Zettelkasten practitioners, devs who want Git-stored notes.

**Strengths:**
- Fully open-source (AGPL)
- Notes stored as plain Markdown or Org-mode — version-control friendly
- Outliner-first structure with bidirectional links
- Built-in daily journal encourages consistent capture
- Spaced repetition cards from any block

**Weaknesses:**
- Outliner-based structure is unfamiliar to newcomers
- No cloud sync (use GitHub or manual folder sync)
- Smaller community than Obsidian
- Less polished UI on mobile

**Getting started:**

```text
1. Download: https://logseq.com/#download
2. Choose "Open a local folder" — pick a synced folder
3. Today's page opens automatically (daily journal)
4. Type anything — use #tags and [[page links]]
5. Use /template to insert a template
```

---

## Deep Dive — OneNote

**Who it's for:** Microsoft 365 / enterprise users, Windows-centric workflows, iPad pen input.

**Strengths:**
- Free with Microsoft 365 (most enterprises)
- Excellent pen/ink input on Windows + iPad
- Deep integration with Outlook, Teams, SharePoint
- great OCR (scan handwritten notes, images)
- Familiar notebook → section → page hierarchy
- Co-authoring in real time

**Weaknesses:**
- Proprietary format — hard to migrate away from
- No Markdown support (or bidirectional links)
- Limited developer-specific features
- Export quality is mediocre

---

## Choosing for Your OS

| OS | Top recommendation | Alternative |
|---|---|---|
| **Windows 11** | Obsidian | OneNote (M365) |
| **macOS** | Obsidian (see `/mac-dev`) | Notion |
| **Linux** | Obsidian | Logseq |
| **iOS/Android primary** | Notion | Obsidian Mobile |
| **All platforms equally** | Obsidian | Notion |

---

## Summary — The Honest Verdict

- **Obsidian**: Best all-around choice for devs. Slight setup cost, maximum flexibility.
- **Notion**: Best for team/wiki content or if you want zero setup time.
- **Logseq**: Best if you care deeply about open-source and Zettelkasten thinking.
- **OneNote**: Best if you're already in Microsoft 365 and need pen input.

---

*Related: [START-HERE.md](START-HERE.md) · [para-method.md](para-method.md) · [migration-guide.md](migration-guide.md)*
