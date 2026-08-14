---
name: appraisal
description: 'Build growth-talk.md and self-appraisal.md from delivery evidence'
agent: agent
tools: ['run_in_terminal', 'read_file']
argument-hint: 'Review cycle, Jira/Bitbucket/Confluence identities, and optional evidence links'
---

# Annual Self-Appraisal & Growth Talk

## Review Period

${input:reviewPeriod:e.g., FY2025-26, 2025-10-01 to 2026-09-30}

## Jira Identity

${input:jiraIdentity:Your Jira username, account ID, or email}

## Bitbucket Identity

${input:bitbucketIdentity:Your Bitbucket author name or account identifier}

## Confluence Identity

${input:confluenceIdentity:Your Confluence username or account identifier}

## Repositories and Spaces

${input:scope:Bitbucket projects/repos and Confluence spaces to search}

## Optional Evidence Links

${input:evidenceLinks:Jira issue, Bitbucket PR, or Confluence page links, if any}

## Instructions

Follow the `self-appraisal` skill's full workflow: gather evidence, draft each output
section, ask for developer-supplied content, then render the two output files.

1. Resolve the review period before querying. `FY2025-26` means `2025-10-01` through
   `2026-09-30`; explicit dates override this convention.
2. If evidence links were provided, ask whether to use only those links or combine them
   with live discovery. Default to live discovery only when no links were supplied.
3. Ask for an identity, repository, or Confluence space only when the supplied inputs are
   insufficient to run a bounded query.
4. Use the existing Jira, Bitbucket, and Confluence skills and their authenticated CLIs.
   Do not create, update, or publish any remote content.
5. Build a working ledger grouped by delivery, technical quality, collaboration,
   influence, and innovation. Actively look for quantifiable impact facts (before/after
   numbers, percentages, counts) while fetching detail, not only whatever surfaces
   passively — this is what a promotion committee weighs most heavily.
6. Present the ledger with coverage, duplicate candidates, and evidence gaps, and ask the
   developer to approve it before drafting.

Once the ledger is approved, draft and render the two output files per the skill's
Style Reference and Output Files sections:

7. `growth-talk.md` — ask the developer for this cycle's Goal statements (offering the
   most recent cycle's goals as a starting point), draft each Comment (Continuous
   Dialogue) from the ledger — leading with quantification for Goals-Results, staying
   method-focused for Goals-Behaviors — and draft Year End Summary as flat outcome bullets.
8. `self-appraisal.md` — draft Major Results vs. Objectives and Areas of Strengths from
   the ledger (leading with a number when one exists), then ask the developer directly for
   Areas for Growth, Professional Challenges you are seeking, and Action for Next Review.
9. Never invent a number the ledger doesn't support, never add promotion-readiness
   framing or self-scores, and never infer developer-supplied sections from evidence.
10. Render both files with the real portal section names as Markdown headers and no
    inline citation markers — nothing is published anywhere; the developer copies
    `growth-talk.md` into the portal by hand and uploads `self-appraisal.md` directly.
