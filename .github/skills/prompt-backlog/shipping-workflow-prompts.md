# Prompt Backlog: shipping workflow prompts

## Source

- **Legacy skill:** `.github/skills/dev-process/github-workflow/SKILL.md`
  - Sections: cohesive commit automation one-liners (lines 379–428), PR automation bash snippets
- **Associated prompts (already in `.github/prompts/shipping/`):**
  - `github-push.prompt.md` — full shipping workflow (push/ship/full modes)
  - `ship.prompt.md` — simpler shipping workflow (commit/push/both/pr modes)

## What They Do

Both prompts handle the same domain (commit → push → PR), but at different complexity levels:

### `github-push.prompt.md` (comprehensive)

- Requires `${input:repo}` URL to extract `owner`, `repo`, and `base` branch
- Three modes: `push`, `ship` (single commit), `full` (cohesive split commits)
- **Full cohesive commit splitting** — splits changes by logical concern with per-group staging
- **State-aware PR check** — runs `gh pr list --state all` before any PR action (open/merged/closed handling)
- Pre-flight: lint + build checks before committing
- Contains a Mermaid decision flowchart

### `ship.prompt.md` (simple)

- No repo URL input — relies on `git push` to the current upstream
- Four modes: `commit`, `push`, `both`, `pr`
- **No cohesive splitting** — single commit per session
- **PR suggestion only** (not creation) — presents suggested title/description in copyable format
- No state-aware PR logic — users copy-paste the suggestion manually

## Known Issues

### `github-push.prompt.md` — build script bug (line 50)

```text
WRONG:   .\mcp-servers\build.ps1
CORRECT: .\gradlew.bat build
```

The file `mcp-servers\build.ps1` does not exist in the repository.
The correct Java build command is `.\gradlew.bat build`.

> **Status:** Fixed directly in the prompt file (see commits in this session).

### `ship.prompt.md` — build script bug (line 27)

```text
WRONG:   .\mcp-servers\build.ps1
CORRECT: .\gradlew.bat build
```

Same issue as above.

> **Status:** Fixed directly in the prompt file (see commits in this session).

### `github-push.prompt.md` — PR description format mismatch

The prompt generates a custom PR description format (`## Summary`, `## Changes`,
`## Commits`, `## Stats`). This does NOT match the structured `PULL_REQUEST_TEMPLATE.md`
which has:
- Type of Change (checkboxes)
- Related Issues
- What was changed
- How Has This Been Tested
- Breaking Changes
- Screenshots
- Checklist (lint, build, docs, branch up-to-date)

**Decision needed:** Should `/github-push` fill in the PR template, or is the custom
format acceptable for agent-generated PRs?

Arguments for aligning to template:
- Consistency — all PRs (human or agent) look the same
- Checklists ensure lint/build gates are communicated

Arguments against:
- `gh pr create --body` bypasses the GitHub template anyway
- The template is for human review; agents know lint/build passed before committing
- The PR template's checklist becomes redundant when the agent has already verified everything

**Recommendation:** Keep the current custom format for agent-created PRs. The template
is the default for human PRs created via the GitHub web UI.

## Overlap Analysis

| Feature | `github-push` | `ship` |
|---|---|---|
| Repo URL parsing | ✅ (required input) | ❌ |
| Cohesive splitting | ✅ (`full` mode) | ❌ |
| State-aware PR (open/merged/closed) | ✅ | ❌ |
| PR creation via `gh` | ✅ | ❌ (suggestion only) |
| Visual Mermaid flowchart | ✅ | ❌ |
| Multiple modes | ✅ 3 modes | ✅ 4 modes |
| Pre-flight checks | ✅ | ✅ |

**Verdict:** `github-push` is a strict superset of `ship`. `ship` is useful for simpler
cases where the user doesn't want to paste a repo URL. They serve different UX personas:
`ship` = quick, lightweight; `github-push` = full-power shipping assistant.

## Suggested Action

1. **Fix both build scripts** — already done in this session
2. **Keep both prompts** — they serve different UX levels (lightweight vs. full-power)
3. **Add a cross-reference** to each prompt pointing to the other ("for full-power shipping including cohesive splits, use `/github-push`")
4. **Enhance `ship.prompt.md`** with state-aware PR checking — run `gh pr list --state all` before
   suggesting a PR, so the suggestion adapts (new vs. update existing)
5. **Consider adding `base` branch input to `ship`** so it doesn't hardcode `origin/master`

## Cross-References

- `github-workflow` modular skill: `.github/skills/_modular/github-workflow/SKILL.md`
  - Section "PR Creation from Changes" covers the same state-aware logic
  - Section "Cohesive Multiple Commits" covers the same splitting logic
- Both shipping prompts align with the skill's workflows
