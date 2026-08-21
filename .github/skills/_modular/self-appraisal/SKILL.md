---
name: self-appraisal
description: >
  Builds two Markdown reference/draft files — growth-talk.md and self-appraisal.md — from
  a developer's Jira, Bitbucket, Confluence, and Git commit-history evidence (local and
  remote-tracking branches), matching the real company-portal fields. Use for appraisal,
  self-review, performance review, growth talk, achievements, impact statement,
  self-assessment, promotion evidence, or annual review requests.
---

# Self-Appraisal & Growth Talk Builder

## Purpose

Gather a developer's delivery evidence from Jira, Bitbucket, Confluence, and Git history
— across both local and remote-tracking branches — actively surface quantifiable impact,
and render two Markdown files that mirror the real company-portal fields:

- **`growth-talk.md`** — Goals-Behaviors, Goals-Results (each a list of Goal + Comment /
  Continuous-Dialogue pairs), and a flat-bullet Year End Summary.
- **`self-appraisal.md`** — Major Results vs. Objectives, Areas of Strengths, Areas for
  Growth, Professional Challenges you are seeking, Action for Next Review.

The developer uses these files as reference while filling in the growth-talk portal
fields by hand, and uses `self-appraisal.md` directly as the draft they upload for the
self-appraisal submission. Never assign ratings, and never publish or submit anything
back to any source system or the company portal.

## Workflow

1. **Gather evidence** from Jira/Bitbucket/Confluence and Git history (local and
   remote-tracking branches) into a working ledger; confirm it with the developer before
   drafting anything.
2. **Draft each output section** from the ledger, leading with quantified impact wherever
   the evidence supports it, following the section-specific rules below.
3. **Ask the developer directly** for the sections that can't come from past evidence —
   Growth Talk Goal statements, Areas for Growth, Professional Challenges, Action for Next
   Review — never infer these.
4. **Render** `growth-talk.md` and `self-appraisal.md` with the real section headers.

Use the existing [`jira`](../jira/SKILL.md),
[`bitbucket`](../bitbucket/SKILL.md), and
[`confluence`](../confluence/SKILL.md) skills for their authenticated CLI
contracts. Do not build a separate client or duplicate their credentials handling.
Use [`git-vcs`](../git-vcs/SKILL.md) for local Git command semantics; this skill only
adds the read-only traversal queries needed for evidence gathering across local and
remote-tracking refs.

## Inputs

Collect these inputs before querying. Ask only for values that are missing.

| Input | Required | Rule |
|---|---|---|
| Review period | Yes | Accept `FYyyyy-yy`, explicit dates, or a natural-language period that can be resolved unambiguously. |
| Jira identity | Yes for Jira discovery | Account ID, username, or email used to find assigned work. |
| Bitbucket identity | Yes for Bitbucket discovery | Author name or account identifier used by the configured Bitbucket instance. |
| Confluence identity | Yes for Confluence discovery | Username or account identifier used in page version metadata. |
| Git identity | Yes for Git traversal | Commit author name and/or email as it appears in `git log`. Ask for every alias the developer has used (work email, personal email, renamed accounts). |
| Repository working copies | Yes for Git traversal | Absolute paths to the clones to traverse. Confirm each path is a Git repository, and confirm which remotes (`origin`, forks, mirrors) are in scope. |
| Remote refresh approval | Yes when remote branches matter | Explicit approval before running `git fetch --all --tags --prune` so remote-tracking refs are current. Without it, state that remote evidence may be stale as of the last local fetch. |
| Repositories / spaces | Conditional | Ask when the configured profile covers more than one relevant repository or Confluence space. |
| Evidence links | Optional | Jira issue, Bitbucket PR, Confluence page links, or commit SHAs supplied by the developer. |

### Financial-Year Convention

This skill uses the **US-style financial year** (October–September), matching the
company's own review cycle — not the Indian financial year (April–March). Interpret
`FY2025-26` as `2025-10-01` through `2026-09-30`.

For any other financial-year label, resolve the first year as the October start and the
second year as the September end. Repeat the resolved dates back to the developer before
querying, and confirm explicitly if the developer's own organization/locale might expect
the April–March convention instead. Explicit `from` and `to` dates always override this
convention.

### Evidence-Source Choice

Live discovery is the default. When the developer supplies links, ask one question before
collecting evidence:

> Should I use only these links, or combine them with live discovery for the review period?

Use supplied links only when the developer explicitly chooses that option. Never silently
search a wider period than the approved date range.

## Gathering Evidence

1. Confirm the identities, repositories/spaces, review dates, and evidence-source choice.
2. Query Jira using **every** involvement signal, not just current assignee — see
   Multi-Signal Involvement Discovery below. A single-signal query systematically
   undercounts anyone who works inside epics owned by other people.
3. Query Bitbucket per approved repository with `search_bitbucket_prs` and, where useful,
   `summarize_bitbucket_contributions`. Both have known defects — read Known Tool Defects
   below before trusting a zero or a suspiciously round result.
4. Query Confluence with `search_confluence_cql` or `search_confluence`, then filter
   results using page metadata such as `version.by.username` and review dates.
5. Traverse each approved repository's Git history — local branches and remote-tracking
   branches alike — for the resolved date range and confirmed author aliases (see Git
   History Traversal below).
6. Fetch detail only for selected PRs and pages, and add verified facts to the ledger.
   While fetching detail, actively look for quantifiable impact facts specifically —
   before/after numbers, percentages, counts (coverage, complexity, defect/backlog counts,
   latency, duration) — in PR descriptions, commit messages, code review comments, and
   Confluence pages, not only whatever surfaces passively. Quantified impact is
   especially persuasive to a promotion committee, so treat finding it as a priority, not
   an afterthought — while never inventing a number a source doesn't support.
7. Deduplicate artefacts linked across systems, such as Jira work referenced by a PR, or
   commits that were merged through a PR already in the ledger.
8. Present the working ledger, coverage, and evidence gaps, and confirm with the developer
   before drafting any output file.

## Multi-Signal Involvement Discovery

**`assignee = <user>` is never sufficient on its own.** Senior engineers routinely
implement child scope inside epics owned by other people, hand issues off mid-flight, and
raise work that someone else finishes. A current-assignee query sees none of that. In one
real run this single-signal approach reported 24 issues where the true involvement set was
130 — a 5x undercount that would have made a strong year look ordinary.

Always build the involvement set as the **union of all five signals**, deduplicated by
issue key.

| # | Signal | Query | Catches |
|---|---|---|---|
| 1 | Current assignee | `assignee = "<user>"` | Work still owned today |
| 2 | **Historical assignee** | `assignee WAS "<user>"` | Work carried then handed off — usually the largest hidden bucket |
| 3 | Reporter | `reporter = "<user>"` | Backlog shaping, decomposition, debt raised |
| 4 | Code-referenced | Jira keys parsed from authored PR titles, branch names, and commit messages | Implementation under someone else's ticket |
| 5 | Changelog actor | Issues whose changelog contains an entry authored by the user | Issues driven through the workflow without ever being assigned |

Supporting queries worth running once each: `worklogAuthor = "<user>"` and
`comment ~ "<user>"`.

### Protocol

1. Run signals 1–3 as JQL; export each result set.
2. Extract every `[A-Z]+-\d+` token from authored PR titles, source branch names, and
   commit subjects/bodies, then resolve each key via `fetch_jira_issue`.
3. For each issue in the union, pull `get_jira_issue_changelog` and record: date first
   assigned to the user, status transitions the user performed, and how many the user
   moved to a resolved/closed state.
4. Deduplicate by issue key and record which signals produced each issue.
5. Publish an **Involvement Reconciliation** table (see Working Ledger) so the developer
   can see the union and its components side by side.

### Interpreting the Signals

- **Handoffs are collaboration evidence, not abandonment.** Report who received the work.
  A large handoff count across many named engineers reads as shared ownership and
  knowledge transfer.
- **"Ever assigned" alone does not prove completion.** Only issues the user personally
  transitioned to Resolved/Closed are unambiguous — count those separately and prefer them
  for delivery claims.
- **Epic-child mapping understates contribution.** Branch names and PR titles frequently
  carry the *epic* key rather than a child key, so a child-coverage ratio can read 0-of-14
  for an epic with a dozen merged PRs. Report both the child ratio and the raw
  PR/commit reference count, and never present the child ratio alone.
- **Not every referenced token is a Jira key.** Static-analysis defect IDs (Coverity
  `CID-…`), build IDs, and release tags share the shape. Resolve each key; unresolvable
  prefixes are a finding in their own right (see Recognising Non-Jira Identifiers).

### Recognising Non-Jira Identifiers

When a referenced key fails to resolve in Jira, classify it before discarding it — it is
often the quantified metric the ledger is missing.

| Prefix pattern | Usually means | Evidence value |
|---|---|---|
| `CID-<6-9 digits>` | Coverity static-analysis defect | Count **distinct** CIDs remediated, and group by defect category quoted in the commit message |
| `V<digits>.<digits>…` | Release/build tag | Release reach — pair with `git tag --contains` |
| Unknown project prefix | Another tracker, or a false regex match | Record in Evidence gaps; do not silently drop |

## Git History Traversal

Git history is the highest-fidelity evidence source available: it survives after a
Bitbucket instance is decommissioned, covers repositories that were never mirrored to a
queryable remote, and exposes churn metrics that PR search never returns. Traverse it in
addition to — never instead of — the Atlassian sources.

### Local vs. Remote Refs

`--all` covers every ref the clone knows about: local branches, remote-tracking branches
(`refs/remotes/*`), and tags. That is the default for evidence gathering, but the two
kinds of ref carry different evidential weight, so keep them distinguishable.

| Ref kind | Selector | Evidential meaning |
|---|---|---|
| Local-only | `--branches` minus what remotes contain | Work never pushed — real, but unreviewed and unshared |
| Remote-tracking | `--remotes` | Work pushed to a shared remote — visible to the team, usually PR-backed |
| Both | `--all` | Full picture; the default traversal scope |

Before traversing, refresh remote-tracking refs so remote evidence isn't stale — but only
with the developer's explicit approval, since it touches the network:

```sh
git remote -v                       # confirm which remotes are in scope
git fetch --all --tags --prune      # refresh remote-tracking refs (needs approval)
```

If approval is withheld or the remote is unreachable, still traverse, and record
"remote-tracking refs last updated <date>" in the ledger's **Evidence gaps**.

### Traversal Queries

Run every command from inside the approved repository, bounded by both the resolved review
dates and the confirmed author aliases.

```sh
# 1. Commit inventory across all refs (local + remote-tracking + tags)
git log --all --author="<alias>" --since=<from> --until=<to> \
  --date=short --pretty=format:"%h|%ad|%an|%d|%s"

# 2. Remote-only view — work that reached a shared remote
git log --remotes --author="<alias>" --since=<from> --until=<to> --oneline

# 3. Local-only view — commits on no remote-tracking branch (flag as unreviewed)
git log --branches --not --remotes --author="<alias>" \
  --since=<from> --until=<to> --oneline

# 4. Volume and churn totals — the raw quantification inputs
git log --all --author="<alias>" --since=<from> --until=<to> --shortstat
git log --all --author="<alias>" --since=<from> --until=<to> --numstat

# 5. Commit-type mix (Conventional Commits: feat/fix/refactor/test/perf/docs)
git log --all --author="<alias>" --since=<from> --until=<to> --pretty=format:"%s"

# 6. Areas of ownership — which modules the work concentrated in
git log --all --author="<alias>" --since=<from> --until=<to> --name-only --pretty=format:

# 7. Merge and release participation
git log --all --author="<alias>" --since=<from> --until=<to> --merges --oneline
git branch --remotes --contains <sha>   # which shared branches carry the work
git tag --contains <sha>                # which releases shipped it

# 8. Collaboration signals — co-authored and jointly-touched work
git log --all --since=<from> --until=<to> --grep="Co-authored-by: <alias>" --oneline

# 9. Detail for a selected commit only
git show --stat <sha>
```

Repeat the whole set once per confirmed alias and once per approved repository, then merge
the results and de-duplicate by commit SHA before adding anything to the ledger. A commit
reachable from both a local branch and a remote-tracking branch is one commit, not two.

### What Git Evidence Yields Per Competency

| Competency | Git signal |
|---|---|
| Delivery | Commit count, `feat:`/`fix:` mix, merge commits, remote branches and tags containing the commit |
| Technical quality | `refactor:`/`test:`/`perf:` commits, test-file churn, files deleted vs. added |
| Collaboration | `Co-authored-by:` trailers, files co-owned with other authors, commits pushed to shared branches |
| Influence | Commits touching shared/core modules, config or convention changes adopted repo-wide, work merged into release branches |
| Innovation | Spike/prototype branches, first commits introducing a new tool or module |

### Git Interpretation Rules

- **Never treat commit count or lines changed as a standalone achievement.** Volume is
  context for a claim, not a claim. "Delivered X — 42 commits across 3 modules" is valid;
  "wrote 12,000 lines" is not.
- **Exclude non-authored churn** from any number quoted in output: vendored dependencies,
  generated code, bulk formatting/import-reordering commits, and merge-conflict resolutions.
  State the exclusion in the ledger when it materially changed a count.
- **Rank churn per file before quoting a total.** Exported test fixtures, data dumps, and
  schema files routinely dwarf hand-written code by one to two orders of magnitude — in one
  run a single 59,700-line `SymbolLibrary.xml` appeared seven times and inflated the raw
  total to 1.57M insertions against ~44k lines of actual Java. Always list the top churn
  files first, exclude the non-authored bulk, and say which extensions the quoted figure
  covers (for example "Java only").
- **Split production from test churn** and report the test share as a percentage. A high
  test share is a defensible quality claim that a single combined number hides.
- **Prefer the merged artefact over the raw commit.** When commits belong to a PR already
  in the ledger, keep the PR as the ledger entry and attach the commit metrics to it
  rather than creating duplicate entries.
- **Distinguish pushed from unpushed work.** Commits reachable only from local branches are
  still evidence, but flag them — they were never shared or peer-reviewed, so they cannot
  support a collaboration or review-contribution claim. Commits on remote-tracking branches
  can.
- **Remote-tracking refs are only as fresh as the last fetch.** If the refresh was skipped
  or failed, say so in the ledger rather than presenting the remote view as complete.
- **Rewritten history is a gap, not a fact.** If a repository was squash-merged or
  force-pushed, the surviving history under-reports the work; record this in the ledger's
  **Evidence gaps** rather than quoting a number you know is depressed.
- **All Git operations are read-only.** `fetch` (with approval) is the only command that
  touches the network, and it updates remote-tracking refs only. Never check out, reset,
  rebase, stash, clean, pull, commit, or push in the developer's working copies.

## Known Tool Defects

Verified against a live Bitbucket Server instance. Check these before accepting a zero or
an implausibly small result — a silently wrong answer is more dangerous than an error,
because it looks like a valid finding.

| Tool | Defect | Symptom | Workaround |
|---|---|---|---|
| `search_bitbucket_prs` | Sends `author=<user>`; Bitbucket Server requires `role.1=AUTHOR&username.1=<user>` | Filter silently ignored — returns everyone's PRs | Call the REST endpoint directly with `role.1`/`username.1`, or filter client-side on `author.user.name` |
| `search_bitbucket_prs` | `getPaginated(path, 3)` caps at 3 pages | Large result sets truncated with no warning | Page manually using `start` / `nextPageStart` until `isLastPage` |
| `summarize_bitbucket_contributions` | Inherits the `author` defect | Reported **0 PRs** for a 158-PR year | Do not use for appraisal counts; derive totals from a corrected PR scan |

**Sanity rule:** if any source returns zero contributions for a period in which the
developer demonstrably shipped work, treat it as a tool failure until proven otherwise,
and say so in the ledger rather than recording "no evidence".

### Cross-Fork Commit Deduplication

Where repositories are forks of one another (a per-release fork chain such as
`repo-24 → repo-25 → repo-26` is common), the same commit SHA appears in several clones.
Always deduplicate by SHA across all clones before quoting a commit count, and state the
raw-versus-unique figures.

## PR Work-Mix Classification

Raw PR counts hide what kind of engineer someone was that year. Classify authored PRs by
title and branch into these buckets, and report the mix as percentages.

| Bucket | Signals in title/branch | Why it matters |
|---|---|---|
| `feature/fix` | Story/bug key, feature name | Visible delivery |
| `test/automation` | `UT`, `test`, `coverage`, `AutoFlow` | Quality investment |
| `refactoring` | `refactor`, `cleanup`, `javadoc`, `redundant` | Maintainability |
| `static-analysis` | `Coverity`, `CID-` | Defect prevention |
| `integration/rebase` | `rebase`, `merge conflict`, `DEV to INT`, `sync` | **Branch stewardship** |

**Branch stewardship is real work that no other source records.** Keeping parallel epic
branches mergeable leaves no Jira ticket and no Confluence page — only PRs. When it is a
material share of the PR mix, it belongs in the ledger as a delivery item.

Combine `test/automation + refactoring + static-analysis` into a single
**quality-directed share** — a mix weighted toward quality is a stronger claim than a raw
PR count, and it is defensible from the titles alone.


## Evidence Interpretation Rules

- State facts only when a source directly supports them.
- Quantify delivery using available counts, story points, dates, merged PRs, commits,
  files changed, comments, or pages. Do not invent business, revenue, availability,
  quality, or time-saving outcomes.
- **Actively search for quantifiable impact, don't just record what happens to surface.**
  Before/after metrics (test coverage, cyclomatic complexity, defect counts, performance
  numbers, backlog size) are the evidence most likely to persuade a promotion committee —
  when a PR, commit, or page references one, capture it explicitly in the ledger's
  **Quantified evidence** column rather than folding it into a general description.
- A technical interpretation is allowed only when clearly labelled `inference` and paired
  with the source facts it relies on.
- Missing metrics are gaps, not negative evidence.
- **An undercount is as wrong as an overstatement.** Understating verified contribution is
  a failure of the gathering process, not caution. If a query returns fewer artefacts than
  the developer's own account suggests, widen the signals and re-run before presenting the
  ledger.
- **Separate diagnosis from improvement.** A root-cause analysis that names bottlenecks
  with measured costs is a strong claim on its own. It is *not* a performance improvement
  unless a post-change measurement exists. Never let a baseline number ("a 632 s save")
  imply a fix that was never measured.
- **Distinguish being listed from acting.** Reviewer lists, watcher lists, and epic
  membership record association, not contribution. Claim only entries with an explicit
  action — an approval, a change request, a comment, a transition, or a commit.
- Do not infer that a PR review was given by the developer from a PR they authored. Review
  contribution requires explicit comment, activity, or supplied-PR evidence.
- Never assign a self-score.

### Review Depth, Not Just Review Count

Approval counts overstate engagement on their own — an approval can be a rubber stamp.
Pull each acted-on PR's activity feed and count comments authored by the developer, then
report both figures: PRs acted on, and comments across how many of them. Report the
denominator too (PRs merely listed as reviewer) so the ratio is visible and honest.


## Competency Map

Use this default map until the developer supplies an organization-specific framework.

| Competency | Source signals | Do not claim without evidence |
|---|---|---|
| Delivery | Completed Jira work, story points, sprints, merged feature or hotfix PRs, `feat:`/`fix:` commits, release tags, **branch-integration PRs**, **issues personally transitioned to Resolved/Closed** | On-time delivery, business value, or ownership beyond the artefacts |
| Technical quality | Tests, review discussion, defect fixes, refactoring, design decisions, `refactor:`/`test:`/`perf:` commits, test-file churn, **static-analysis defects remediated**, **refactoring done to enable testing** | Defect reduction, reliability improvement, or maintainability outcomes without measurements |
| Collaboration | Review comments, shared issue work, co-authored documents, feedback, `Co-authored-by:` trailers, co-owned files, **issues handed off to named owners** | Mentoring, stakeholder alignment, or team-wide influence without explicit evidence |
| Influence | ADRs, design documents, cross-team work, adopted decisions, commits to shared/core modules or repo-wide conventions, **issues raised and decomposed for others**, **work landing in another team's epic** | Organization-wide impact or technical leadership without evidence of adoption |
| Innovation | Spikes, prototypes, research tickets, new tooling, experiments, first commits introducing a new module or tool, **reusable test frameworks**, **analysis not traceable to an assigned ticket** | Production adoption or value of an experiment without follow-on evidence |

The map is a working default, not a performance framework. Ask for the organization's
competency headings before drafting output content if they are available. This grouping
is used internally to organize the working ledger — it is not one of the real output
section headers (see Output Files below).

## Working Ledger

The ledger opens with an **Involvement Reconciliation** table, so the developer can see
the union of signals and challenge any component before the evidence table is read.

| Signal | Count | Why it matters |
|---|---|---|
| Union of all signals | 130 issues across 9 projects | True involvement footprint |
| Ever assigned (`assignee WAS`) | 95 | Includes work since handed off |
| Still assigned today | 34 | What a naive query would have found |
| Handed off to teammates | 61 | Shared ownership, not abandonment |
| Raised by the developer | 18 | Backlog shaping |
| Reached only via PR/commit | 23 | Implemented under someone else's ticket |
| Personally moved to Resolved/Closed | 30 | Actioned, not merely held |

Then return the evidence table using this schema. Keep source URLs intact so the developer
can verify every claim later. For Git evidence, put the short SHA (or SHA range) plus the
repository in the **Source** column, note whether the work is on a remote-tracking branch
or local-only, and use the browsable remote commit URL when one exists.

| ID | Competency | Source | Date | Verified fact | Quantified evidence | Inference | URL |
|---|---|---|---|---|---|---|---|
| E-001 | Delivery | Jira `PROJ-123` | 2026-02-14 | Completed the migration story | 8 story points | — | `https://...` |
| E-002 | Technical quality | Git `a1b2c3d..e4f5g6h` (`core-svc`, `origin/main`) | 2026-03-02 | Extracted the shared validator and backfilled its tests | 14 commits, 9 test files added, 3 duplicated blocks removed | — | `https://.../commits/e4f5g6h` |
| E-003 | Innovation | Git `9f8e7d6` (`core-svc`, local-only) | 2026-04-11 | Prototyped the batch importer; never pushed | 6 commits, 1 new module | — | `local: e:\repos\core-svc` |

Follow the table with these sections:

- **Coverage:** evidence count per competency and source, reported per repository for Git,
  split between remote-tracking and local-only commits.
- **Potential duplicates:** artefacts that appear to describe the same delivery outcome,
  including commits already represented by a merged PR entry.
- **Evidence gaps:** claims that need links, metrics, or context before drafting, plus any
  repository whose history was squashed, rewritten, or unavailable for the period, and any
  remote whose tracking refs could not be refreshed.

Once the developer approves the ledger, draft the output files below directly from it.

## Drafting Rules

Apply these rules to every evidence-derived section in both output files (see Output
Files below for which sections those are).

### Quantification Rule

State impact numerically whenever the ledger (or a fact the developer confirms) supports
it — for example, "reduced the save-action p95 latency by 11%" or "cut module coupling by
2% by extracting the shared validator." Never invent a number. If a claim is qualitative
only, write it qualitatively and leave the number out rather than estimating one.

This is a direct continuation of the rule to actively gather quantifiable impact while
gathering evidence: if a before/after metric was found for a piece of evidence, lead with
it here rather than mentioning it only in passing — quantified impact is what a promotion
committee weighs most heavily, so it earns priority placement in the sentence/bullet, not
just inclusion somewhere in it.

### No Framing, No Trade-Off Commentary

- Do not add promotion-readiness framing (e.g., "demonstrates readiness for L+1"). State
  achievements and let the reviewing committee draw conclusions.
- Do not editorialize about uneven coverage across sections (e.g., "less collaboration
  evidence because focus was elsewhere"). Present each section strictly from its own
  evidence; any imbalance stays visible implicitly, and gaps remain in the ledger's
  **Evidence gaps** list rather than in the drafted prose.

### No Inline Citations in the Rendered Files

The working ledger is the traceable artefact (keep its IDs and source URLs for the
developer's own reference); the rendered `growth-talk.md`/`self-appraisal.md` read as
clean prose/bullets with no `[E-004]`-style citation markers, matching the real portal
style found in the developer's own past submissions (see Style Reference below).

## Style Reference

The section styles below were extracted once from the developer's own past submissions,
converted from `.docx` with the [`document-converter`](../document-converter/SKILL.md)
skill and read from [`references/`](references/README.md). Never read anything outside
`references/private/converted/`, and never commit anything under `references/private/` —
it is git-ignored because the source documents are personal HR data.

Getting new source files in: place originals under `references/private/raw/growth-talk/`
and `references/private/raw/self-appraisal-form/`, named by cycle (e.g., `FY2023-24.docx`),
then convert each with `document-converter/scripts/convert-document.ps1` targeting `.md`
(inside a timeout-guarded job — see that skill's Operational Notes) into
`references/private/converted/<category>/`.

**Self-Appraisal Form** is a fixed 5-section portal form, stable across 4 years reviewed
(2022-2025):

| # | Section | Style |
|---|---|---|
| 1 | Major Results vs. Objectives | Dash bullets, 4-5 items. `Action verb + what – method/tool used`. |
| 2 | Areas of Strengths | Dash bullets, 3-5 items. Skills/methods used, tied to impact. |
| 3 | Areas for Growth | Dash bullets, 2 items. Reflective — developer-supplied. |
| 4 | Professional Challenges you are seeking | Dash bullets, 2-3 items. Aspirational — developer-supplied. |
| 5 | Action for Next Review | Dash bullets, 2 items. Forward-looking — developer-supplied. |

Cross-cutting style: no prose paragraphs, ever — dash bullets only. Impersonal, verb-first
voice (never "I"). Em-dash (–) joins a clause naming the method/technique/tool inside the
same bullet. Historically, quantification was rare — named principles/tools (SOLID, DRY,
YAGNI, CPD, cyclomatic complexity, JUnit) and named projects substituted for percentages
more often than hard numbers — but the Quantification Rule above intentionally goes
further than that history wherever the current ledger actually supports a number.

**Growth Talk** is a 3-section portal form, stable across 3 years reviewed
(FY2023-FY2025-26):

| # | Section | Style |
|---|---|---|
| 1 | Goals – Behaviors | N × (Goal bullet + indented Comment (Continuous Dialogue) sub-bullet, written as a short paragraph). |
| 2 | Goals – Results | Same Goal + Comment pairing as section 1. |
| 3 | Year End Summary | Flat bullet list, no Goal/Comment pairing, outcome-only phrasing. |

One cycle (FY2025-26) also contained an unstructured **"Tasks worked on"** block before
the three sections — a raw list of tickets/technical items. Treat any such extra content
as noise to set aside, never as a fourth section.

**Goals vs. Comments:** **Goal** text is largely an organization-provided behavior/result
expectation, reused near-verbatim across cycles (e.g., "Upskilling and training using
MyLearningWorld, SABA and other resources..." appears in all 3 cycles almost unchanged) —
never draft Goals from evidence; the developer selects/supplies them (often copied from
the prior cycle). **Comment (Continuous Dialogue)** is personalized evidence prose against
a specific Goal — this is the part drafted from the ledger. **Year End Summary** overlaps
in spirit with the Self-Appraisal Form's "Major Results vs. Objectives" — both are flat,
outcome-oriented accomplishment bullets, and can share a common drafting approach.

Do not copy content, project names, or claims from prior cycles into the current output —
references inform **style only**, not substance for the current period. If a stylistic
pattern from the references conflicts with the Drafting Rules above (e.g., an old document
used promotion-readiness framing), the Drafting Rules win — ask the developer if a real
conflict shows up.

## Output Files

Render exactly two files, using the real portal section names as Markdown headers, split
into evidence-derived (drafted from the ledger) and developer-supplied (always asked for
directly, never inferred) — see the Evidence-Derived vs. Developer-Supplied table:

| Kind | Sections | Rule |
|---|---|---|
| Evidence-derived | `self-appraisal.md`: Major Results, Areas of Strengths. `growth-talk.md`: Comment (Continuous Dialogue) under each Goal, Year End Summary. | Draft from the working ledger, following the Style Reference patterns for that section. |
| Developer-supplied | `self-appraisal.md`: Areas for Growth, Professional Challenges, Action for Next Review. `growth-talk.md`: the Goal statements themselves. | **Never infer these from past evidence.** Always ask the developer directly (or offer the prior cycle's values as a starting point to edit, since Goals are frequently reused near-verbatim). |

### `growth-talk.md`

```markdown
## Goals – Behaviors

- Goal – <developer-supplied>
  - Comment (Continuous Dialogue) – <drafted from evidence, method/practice-focused>

## Goals – Results

- Goal – <developer-supplied>
  - Comment (Continuous Dialogue) – <drafted from evidence, leads with quantification>

## Year End Summary

- <drafted from evidence, flat outcome bullets>
```

1. Ask the developer for this cycle's **Goal** statements for both sections (offer the
   most recent cycle's goals from `references/private/converted/growth-talk/` as a
   starting point).
2. Draft each **Comment (Continuous Dialogue)** from the ledger evidence most relevant to
   that Goal's topic, as a short paragraph (not dash bullets — Comments read as prose in
   every reference reviewed). Goals-Results Comments lead with quantification (e.g.,
   "Reduced CPD by 5%, reduced cyclomatic complexity from 6 to 3 on average, reduced
   coupling by 2%"); Goals-Behaviors Comments stay method/practice-focused.
3. Draft **Year End Summary** as a flat bullet list of outcomes — no Goal/Comment pairing;
   include a number when the ledger supports one, but don't force it.
4. Treat any "Tasks worked on"-style content supplied alongside references as optional
   extra evidence context only, never as a section to render.


### `self-appraisal.md`

```markdown
## Major Results vs. Objectives

- <drafted from evidence, leads with a number when the ledger supports one>

## Areas of Strengths

- <drafted from evidence, skills/methods used and their impact>

## Areas for Growth

- <developer-supplied>

## Professional Challenges you are seeking

- <developer-supplied>

## Action for Next Review

- <developer-supplied>
```

1. Draft **Major Results vs. Objectives** from Delivery evidence: dash bullets,
   `Action verb + what – method/tool used`, leading with a quantified number when the
   ledger supports one and falling back to named technique/tool specificity otherwise.
2. Draft **Areas of Strengths** from Technical Quality + Collaboration evidence, phrased
   as skills/methods used and their impact, same dash-bullet style.
3. Ask the developer directly for **Areas for Growth**, **Professional Challenges you are
   seeking**, and **Action for Next Review**. Offer to proof-read/restyle whatever they
   supply to match the observed 2-3-bullet, dash-bullet, impersonal-voice pattern, but
   never originate the content.

Both files are ready for the developer to copy into the portal by hand (`growth-talk.md`)
or upload directly as the draft (`self-appraisal.md`) — portal automation itself is out
of scope (see Guardrails).

## Three-Tier Usage

### Newbie

Ask for the review period, identities (including every Git author alias), repositories/
spaces, repository working copies, and whether remote-tracking refs may be refreshed with
a fetch. Also ask whether supplied links should be used alone or combined with live
discovery. Explain the resolved date range and return the working
ledger with gaps. Once approved, draft each output section straight
from the ledger and flag anything that needs a style or density decision. If reference
documents are available under `references/private/converted/`, use the Style Reference
patterns rather than guessing.

### Amateur

Use targeted Jira JQL across **all five involvement signals**, repository-specific
Bitbucket searches corrected for the known `role.1`/`username.1` and pagination defects,
Confluence CQL/text search, and date-and-author-bounded `git log` traversals per repository
across both local and remote-tracking refs. Group the
normalized ledger by the five default competency dimensions and identify
where the evidence is too weak for an impact statement. Balance evidence density per
output section and surface any quantification that needs the developer to confirm a
number before it is stated.

### Pro

Run approved source queries in sequence, inspect selected PR, page, and commit details,
cross-link artefacts, deduplicate outcomes (commits against their merged PRs, local refs
against remote-tracking refs, aliases against each other, SHAs across forked clones), and
build a traceable working ledger. Reconcile the five involvement signals into one union
and publish the reconciliation. Derive churn and
commit-type-mix metrics from `--shortstat`/`--numstat` output, excluding generated,
vendored, and bulk-formatting churn, and separate pushed from unpushed work. Classify the
PR mix to surface branch stewardship and the quality-directed share. Keep all
operations read-only and preserve raw source URLs. Mine `growth-talk` and
`self-appraisal-form`
references separately for structure, bullet style, and quantification conventions, and
render both `growth-talk.md` and `self-appraisal.md` from the same evidence base, keeping
evidence-derived and developer-supplied sections clearly separated and never inferring the
latter.

## Guardrails

- Never create or update Jira issues, Bitbucket PRs/comments, or Confluence pages, and
  never publish or submit drafted content anywhere — including never submitting to the
  company appraisal portal directly. Everything stays local until the developer copies it
  in or uploads it themselves.
- Never assign a rating, score, or promotion-readiness verdict.
- Never infer **Areas for Growth**, **Professional Challenges you are seeking**, **Action
  for Next Review**, or Growth Talk **Goal** statements from past delivery evidence —
  these are reflective/aspirational/organization-provided and must come from the developer.
- Never read or write anything outside `references/private/converted/`, and never commit
  anything under `references/private/` — it is git-ignored because the source documents
  are personal HR data.
- Do not expose credentials, tokens, or unrelated work artefacts.
- Do not use an inferred account identity when the developer has not confirmed it — this
  includes Git author aliases, which must be confirmed rather than guessed from
  `git config user.name`.
- Git traversal is strictly read-only: `log`, `show`, `shortlog`, `blame`, `branch`,
  `tag`, `remote`, and `diff --stat`. `fetch --all --tags --prune` is permitted only with
  the developer's explicit approval, to refresh remote-tracking refs. Never check out,
  reset, rebase, stash, clean, pull, commit, or push in the developer's working copies.
- Never quote raw commit counts or line-change totals as an achievement on their own —
  they are supporting context for a named outcome only.
- Never present an involvement set built from `assignee =` alone. All five signals must be
  run, and the reconciliation published, before the ledger is shown to the developer.
- Never report a baseline measurement in a way that implies an improvement that was never
  measured.
- If a query returns incomplete data, report the limitation and ask for a narrower query,
  an additional repository/space, or direct evidence links.
