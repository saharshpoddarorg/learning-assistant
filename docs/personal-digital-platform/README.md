# Personal Digital Platform — discovery and design records

Status: discovery baseline; not a finalized SRS or approved implementation architecture.
Updated: 2026-10-27.

This directory is the durable planning record for the Personal Digital Platform.
The existing repository is an experimental reference, not the architectural authority.
PKM and Learning are initial priorities; Productivity and Planning are future candidates.

## Read in this order

1. [Current checkpoint](reviews/current-checkpoint.md) — progress and pending reviews.
2. [Idea and capability inventory](discovery/source-inventory.md) — 62 groups with stable IDs and evidence.
3. [Audit coverage](discovery/audit-coverage.md) — inspection scope, findings and limits.
4. [Requirements register](requirements/requirements-register.md) — accepted direction and open questions.
5. [Import-review decision](design/decisions/001-import-review.md) — provisionally accepted behavior.
6. [Vault/platform boundaries](design/decisions/002-vault-platform-boundaries.md) — accepted direction with explicit open points.
7. [Historical alternatives](design/explorations/historical-alternatives.md) — options to reconsider.
8. [Evidence guide](discovery/evidence/README.md) — source index and recovery instructions.

## Authority and record types

- Requirements describe user needs and acceptance status, with verification links added when implemented.
- Inventory inclusion preserves an idea for evaluation, not a commitment to implement it.
- Explorations retain alternatives, trade-offs and unresolved questions.
- Decision records state status, context, rationale and consequences; supersede them explicitly.
- Old source text, including words such as final or locked, does not override current user instructions.
- Current explicit user direction governs. Document updates must not silently broaden approval.

## Working sequence

Complete extraction and obtain user confirmation -> brainstorm remaining requirements/questions ->
review a starting draft design -> obtain explicit authorization for a new implementation repository and IntelliJ setup.

## Maintenance

Discuss ideas in chat and retain substantive outcomes here. Update affected records and the checkpoint after each session.
Connect requirements, inventory IDs, decisions and later implementation/tests.
Record retain/refactor/merge/redesign/migrate/defer/remove dispositions with rationale and user review.

This is repository-level documentation, not a Java package.
Its location does not determine implementation organization. Decision 002 records the subsequently accepted separate platform/vault repository direction; detailed architecture remains open.
