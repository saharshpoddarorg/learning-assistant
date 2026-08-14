---
name: self-appraisal
description: >
  Builds two Markdown reference/draft files — growth-talk.md and self-appraisal.md — from
  a developer's Jira, Bitbucket, and Confluence delivery evidence, matching the real
  company-portal fields. Use for appraisal, self-review, performance review, growth talk,
  achievements, impact statement, self-assessment, promotion evidence, or annual review
  requests.
---

# Self-Appraisal & Growth Talk Builder

## Purpose

Gather a developer's delivery evidence from Jira, Bitbucket, and Confluence, actively
surface quantifiable impact, and render two Markdown files that mirror the real
company-portal fields:

- **`growth-talk.md`** — Goals-Behaviors, Goals-Results (each a list of Goal + Comment /
  Continuous-Dialogue pairs), and a flat-bullet Year End Summary.
- **`self-appraisal.md`** — Major Results vs. Objectives, Areas of Strengths, Areas for
  Growth, Professional Challenges you are seeking, Action for Next Review.

The developer uses these files as reference while filling in the growth-talk portal
fields by hand, and uses `self-appraisal.md` directly as the draft they upload for the
self-appraisal submission. Never assign ratings, and never publish or submit anything
back to any source system or the company portal.

## Workflow

1. **Gather evidence** from Jira/Bitbucket/Confluence into a working ledger; confirm it
   with the developer before drafting anything.
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

## Inputs

Collect these inputs before querying. Ask only for values that are missing.

| Input | Required | Rule |
|---|---|---|
| Review period | Yes | Accept `FYyyyy-yy`, explicit dates, or a natural-language period that can be resolved unambiguously. |
| Jira identity | Yes for Jira discovery | Account ID, username, or email used to find assigned work. |
| Bitbucket identity | Yes for Bitbucket discovery | Author name or account identifier used by the configured Bitbucket instance. |
| Confluence identity | Yes for Confluence discovery | Username or account identifier used in page version metadata. |
| Repositories / spaces | Conditional | Ask when the configured profile covers more than one relevant repository or Confluence space. |
| Evidence links | Optional | Jira issue, Bitbucket PR, or Confluence page links supplied by the developer. |

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
2. Query Jira for work completed in the resolved period using `search_jira_issues`.
3. Query Bitbucket per approved repository with `search_bitbucket_prs` and, where useful,
   `summarize_bitbucket_contributions`.
4. Query Confluence with `search_confluence_cql` or `search_confluence`, then filter
   results using page metadata such as `version.by.username` and review dates.
5. Fetch detail only for selected PRs and pages, and add verified facts to the ledger.
   While fetching detail, actively look for quantifiable impact facts specifically —
   before/after numbers, percentages, counts (coverage, complexity, defect/backlog counts,
   latency, duration) — in PR descriptions, commit messages, code review comments, and
   Confluence pages, not only whatever surfaces passively. Quantified impact is
   especially persuasive to a promotion committee, so treat finding it as a priority, not
   an afterthought — while never inventing a number a source doesn't support.
6. Deduplicate artefacts linked across systems, such as Jira work referenced by a PR.
7. Present the working ledger, coverage, and evidence gaps, and confirm with the developer
   before drafting any output file.

## Evidence Interpretation Rules

- State facts only when a source directly supports them.
- Quantify delivery using available counts, story points, dates, merged PRs, files changed,
  comments, or pages. Do not invent business, revenue, availability, quality, or time-saving
  outcomes.
- **Actively search for quantifiable impact, don't just record what happens to surface.**
  Before/after metrics (test coverage, cyclomatic complexity, defect counts, performance
  numbers, backlog size) are the evidence most likely to persuade a promotion committee —
  when a PR, commit, or page references one, capture it explicitly in the ledger's
  **Quantified evidence** column rather than folding it into a general description.
- A technical interpretation is allowed only when clearly labelled `inference` and paired
  with the source facts it relies on.
- Missing metrics are gaps, not negative evidence.
- Do not infer that a PR review was given by the developer from a PR they authored. Review
  contribution requires explicit comment, activity, or supplied-PR evidence.
- Never assign a self-score.

## Competency Map

Use this default map until the developer supplies an organization-specific framework.

| Competency | Source signals | Do not claim without evidence |
|---|---|---|
| Delivery | Completed Jira work, story points, sprints, merged feature or hotfix PRs | On-time delivery, business value, or ownership beyond the artefacts |
| Technical quality | Tests, review discussion, defect fixes, refactoring, design decisions | Defect reduction, reliability improvement, or maintainability outcomes without measurements |
| Collaboration | Review comments, shared issue work, co-authored documents, feedback | Mentoring, stakeholder alignment, or team-wide influence without explicit evidence |
| Influence | ADRs, design documents, cross-team work, adopted decisions | Organization-wide impact or technical leadership without evidence of adoption |
| Innovation | Spikes, prototypes, research tickets, new tooling, experiments | Production adoption or value of an experiment without follow-on evidence |

The map is a working default, not a performance framework. Ask for the organization's
competency headings before drafting output content if they are available. This grouping
is used internally to organize the working ledger — it is not one of the real output
section headers (see Output Files below).

## Working Ledger

Return a Markdown table using this schema. Keep source URLs intact so the developer can
verify every claim later.

| ID | Competency | Source | Date | Verified fact | Quantified evidence | Inference | URL |
|---|---|---|---|---|---|---|---|
| E-001 | Delivery | Jira `PROJ-123` | 2026-02-14 | Completed the migration story | 8 story points | — | `https://...` |

Follow the table with these sections:

- **Coverage:** evidence count per competency and source.
- **Potential duplicates:** artefacts that appear to describe the same delivery outcome.
- **Evidence gaps:** claims that need links, metrics, or context before drafting.

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

Ask for the review period, identities, repositories/spaces, and whether supplied links
should be used alone or combined with live discovery. Explain the resolved date range and
return the working ledger with gaps. Once approved, draft each output section straight
from the ledger and flag anything that needs a style or density decision. If reference
documents are available under `references/private/converted/`, use the Style Reference
patterns rather than guessing.

### Amateur

Use targeted Jira JQL, repository-specific Bitbucket searches, and Confluence CQL/text
search. Group the normalized ledger by the five default competency dimensions and identify
where the evidence is too weak for an impact statement. Balance evidence density per
output section and surface any quantification that needs the developer to confirm a
number before it is stated.

### Pro

Run approved source queries in sequence, inspect selected PR and page details, cross-link
artefacts, deduplicate outcomes, and build a traceable working ledger. Keep all operations
read-only and preserve raw source URLs. Mine `growth-talk` and `self-appraisal-form`
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
- Do not use an inferred account identity when the developer has not confirmed it.
- If a query returns incomplete data, report the limitation and ask for a narrower query,
  an additional repository/space, or direct evidence links.
