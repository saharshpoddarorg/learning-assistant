# ai/ -- AI-Generated Content

This directory holds content produced by AI agents, Copilot, and prompts during
development and learning sessions. It uses three tiers with clear, unambiguous names.

---

## Tiers at a Glance

```
ai/scratch/     DISCARD   session working space -- clear when session ends
ai/local/       KEEP      survives between sessions, stays on your machine only
ai/saved/       PUSH      committed to the repository, long-term reference
```

The words map to what you would say:
- "This is scratch work" -> `scratch/`
- "I want to keep this" -> `local/`
- "Push this to the repo" -> `saved/`

---

## Topic Folders

The same six folders exist under both `local/` and `saved/`:

| Folder | What goes here |
|--------|----------------|
| `concepts/` | Concept explanations, deep-dives, how-it-works notes |
| `q-and-a/` | Q&A sessions, interview prep, problem walkthroughs |
| `resources/` | Resource discovery outputs, reading plans, link collections |
| `changelogs/` | Session work logs -- what changed, why, what was decided |
| `decisions/` | Architecture records, design choices, trade-off analyses |
| `reviews/` | Code review outputs, impact assessments, refactor plans |

`scratch/` has no sub-folders -- put anything there freely.

---

## Promotion Commands

**Keep a scratch file locally (survive past this session):**
```powershell
# Windows
Move-Item ai\scratch\myfile.md ai\local\concepts\2026-02-21_myfile.md

# Mac/Linux
mv ai/scratch/myfile.md ai/local/concepts/2026-02-21_myfile.md
```

**Push a local file to the repo:**
```powershell
# Windows
Move-Item ai\local\decisions\myfile.md ai\saved\decisions\myfile.md
git add ai/saved/decisions/myfile.md
git commit -m "Save decision: <topic>"

# Mac/Linux
mv ai/local/decisions/myfile.md ai/saved/decisions/myfile.md
git add ai/saved/decisions/myfile.md
git commit -m "Save decision: <topic>"
```

**Clear scratch at session end:**
```powershell
Remove-Item ai\scratch\* -Recurse   # Windows
rm -rf ai/scratch/*                 # Mac/Linux
```

---

## Naming Convention

```
{YYYY-MM-DD}_{topic-slug}.md             <- date-prefixed (most files)
{topic-slug}.md                          <- timeless reference docs
```

Examples:
- `ai/scratch/draft.md`
- `ai/local/concepts/2026-02-21_java-records.md`
- `ai/saved/decisions/atlassian-config-strategy.md`

---

## Gitignore Summary

| Path | Tracked? |
|------|----------|
| `ai/scratch/` | No -- always local |
| `ai/local/` | No -- always local |
| `ai/saved/` | Yes -- committed to repo |
