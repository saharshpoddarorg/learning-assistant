---
id: BLI-086
title: Resume & LinkedIn skill — auto-update from work history
status: todo
priority: medium
type: feature
created: 2026-06-02
updated: 2026-06-02
started: null
completed: null
blocked-since: null
review-since: null
epic: EPIC-006
sprint: null
parent: null
sub-items: []
origin: null
estimated-effort: L
actual-effort: null
tags: [skill, resume, linkedin, career, work-history, atlassian, github-workflow, profile, professional]
origin-type: manual
import-batch: null
source-file: null
---

# BLI-086: Resume & LinkedIn skill — auto-update from work history

## Description

Create a new Copilot skill that helps maintain and update a professional resume
(CV) and LinkedIn profile, with a special focus on accurately reflecting work
done at the current/previous companies. The skill should be able to **mine
evidence of work** by composing other existing skills (Atlassian — Jira tickets,
Confluence pages, Bitbucket PRs/commits; GitHub workflow — PRs, issues, commits)
to surface what the user has actually shipped, then translate those raw
artefacts into resume-grade and LinkedIn-grade language.

Primary use cases:

- **Work experience section** of a resume — generate accomplishment bullets
  per company / role from Jira tickets, Confluence design docs, Bitbucket PRs,
  GitHub commits, and shipped features
- **LinkedIn profile updates** — headline, about, experience, skills, projects,
  recommendations prompts — kept in sync with resume content
- **Periodic refresh workflow** — quarterly / annual cadence to harvest new
  work since the last update
- **Tailoring** — adapt the master resume into role-specific variants
  (e.g., backend-engineer, platform-engineer, tech-lead)
- **Metric extraction** — pull numbers (PR counts, ticket counts, perf wins,
  scope) out of source systems instead of guessing

Composes existing skills:

- `atlassian-tools` — Jira tickets owned/closed, Confluence pages authored,
  Bitbucket PRs merged, cross-account migration for past employers
- `github-workflow` + `git-vcs` — PRs, issues, commits, contribution graphs
- `requirements-research` + `software-development-roles` — translate
  technical work into role-appropriate accomplishment language
- `pkm-management` + `brain-management` — persist drafts, versions, and
  source-of-truth resume content under brain/

### Future Considerations

- Optional cover letter generation tailored to a job description
- ATS (Applicant Tracking System) keyword optimisation against a target JD
- Interview prep deck generation from the same work-history evidence
- Multi-format export (PDF, DOCX, plain text, JSON Resume schema)
- Comparison mode — diff current resume vs evidence to surface stale or
  missing items
- Integration with a future `career-resources` content workflow (the
  `career-resources` skill currently covers paths/roles/compensation/interviews
  — this skill complements it with the *personal artefact* side)
- Privacy & redaction rules — strip internal codenames, customer names,
  confidential metrics before export
- LinkedIn API automation (if/when permitted) vs copy-paste assist

## Acceptance Criteria

- [ ] New skill file created at `.github/skills/<category>/resume-linkedin/SKILL.md`
  (category chosen from existing taxonomy — likely under a personal-productivity
  or career grouping; verify against `.github/skills/TAXONOMY.md` and add a new
  category there if none fits)
- [ ] Skill `description` frontmatter clearly triggers on: "resume", "CV",
  "LinkedIn", "work experience", "profile update", "accomplishments"
- [ ] Skill defines a structured workflow:
      gather evidence → group by role/company/time → draft bullets →
      tailor → review → export
- [ ] Skill explicitly delegates to `atlassian-tools` for Jira/Confluence/Bitbucket
      evidence and to `github-workflow`/`git-vcs` for GitHub evidence
- [ ] Skill defines a brain storage layout for resume artefacts
      (e.g., `brain/ai-brain/notes/career/resume/` with master, variants, and
      a `work-history-evidence.md` aggregation file) — coordinate with
      `brain-management` rules
- [ ] Companion slash-command prompt created
      (e.g., `.github/prompts/resume-update.prompt.md`) with input variables
      `mode` (refresh | tailor | linkedin-sync | export), `target-role`,
      `since-date`
- [ ] Registered in `copilot-instructions.md` skills block with routing notes
- [ ] Added to `.github/skills/TAXONOMY.md` and `.github/docs/skills-library.md`
- [ ] Added to `.github/docs/slash-commands.md` (quick lookup + details)
- [ ] Privacy/redaction guidance included (no employer-confidential data in
      exported artefacts; PAT-based read-only access to source systems)
- [ ] Newbie/amateur/pro tiers in the skill body following the 3-tier
      convention used by other skills

## Related

- `atlassian-tools` skill — Jira/Confluence/Bitbucket evidence gathering,
  cross-account migration, resume/work analysis (already noted as a use case
  in the skill description in `copilot-instructions.md`)
- `github-workflow` skill — PR/issue/commit evidence
- `career-resources` skill — career paths, roles, compensation, interviews
  (complements this skill on the *advisory* side)
- BLI-010: Personal Confluence — migrate office resources (work-history
  evidence persistence)
- BLI-077: PKM/brain/notes manager — full knowledge lifecycle (storage of
  resume drafts and evidence under brain/)
- EPIC-006: Iterative feature brainstorming — learning-assistant

## Activity Log

| Date | Time | Actor | Action | Details |
|---|---|---|---|---|
| 2026-06-02 | 05:00 PM | system | created | Captured via /jot — new skill idea for resume + LinkedIn auto-update composing atlassian/github skills (EPIC-006) |

## Notes

- The `atlassian-tools` skill description in `copilot-instructions.md` already
  lists "resume/work analysis" as one of its responsibilities — this BLI
  formalises that into a dedicated, composable skill rather than leaving it
  implicit inside `atlassian-tools`
- Decision needed: single skill `resume-linkedin` covering both artefacts vs
  two skills (`resume`, `linkedin-profile`) sharing an evidence-gathering
  sub-skill — lean towards single skill, since the underlying content is the
  same and only the formatting differs
- Storage decision needed: master resume as markdown under
  `brain/ai-brain/notes/career/` (portable, diffable, AI-friendly) with
  export adapters for PDF/DOCX, vs binary master — strongly prefer markdown
- This is a personal-productivity skill, not a learning-assistant content
  feature — confirm whether EPIC-006 is the right home or if a new
  `EPIC-007: personal-productivity skills` should be opened later

## Time Tracking

| Metric | Value |
|---|---|
| Estimated effort | L |
| Actual effort | — |
| Created | 2026-06-02 |
| Started | — |
| Completed | — |
| Cycle time | — |
