# Decision 002: independent Git vault and platform boundaries

Status: accepted planning direction, with provisional and open points below.
Recorded: 2026-10-27. Origin: resumed architecture discussion; explicit user "yes green signal" to record the two-repository direction and editing rules.
This authorizes recording the agreement, not repository creation, scaffolding, IntelliJ setup or implementation.

## Context and rationale

The user wants finalized PKM knowledge hosted in a Git remote, independently readable for understanding, learning and revision without the platform. Editing may be manual or performed through deterministic code/API calls, configured flows or AI agents. Formatted reading, hierarchical navigation and computed views are important; reading raw Markdown is not the desired primary experience.

Separate repositories allow knowledge and application code to have independent histories, branches, access and lifecycles. A repository boundary does not itself solve cross-device conflicts, synchronization or backup requirements.

## Accepted direction

- Two independent Git repositories: the future personal-digital-platform repository for application code/workflows, and a separate vault repository for canonical knowledge. The vault name, hosting provider, directory structure and internal modules are not selected.
- The platform accesses a configured local vault location. The vault remains independently usable through compatible tools without the platform.
- Git-backed vault knowledge is authoritative. A local save is not a remote update; uncommitted or unpushed changes are absent from the hosted copy. Pending changes and synchronization status should be visible. Detailed backup/restore guarantees remain open.
- Maintain one canonical note and link from other locations to avoid divergent duplicates. Folder-like hierarchy is a working preference; exact taxonomy and identity/link mechanics remain open.
- Distinguish authored knowledge from derived views such as child-page lists, backlinks and query results. Capable viewers should provide dynamic views with readable fallbacks; underlying notes remain understandable without macros. Fallback storage, refresh and commit policy remain open.

## Editing and authorization

These rules are mechanism-independent: manual editing, deterministic APIs/code, configured flows and AI-assisted flows are all in scope.

- Manual editing may change the local working copy directly.
- Applying proposed changes to accepted notes requires explicit authorization for the reviewed proposal version, following [Decision 001](001-import-review.md).
- Commit and push require review and explicit approval. Editing/application authorization does not imply commit/push authorization. No mandatory separate approval dialogs or Git UI have been selected.
- Preserve originals and expose meaningful conversion loss for explicit resolution; do not silently flatten or discard meaning. Integration fidelity may improve iteratively.
- Preserve conflicting versions and interrupt for review. This conflict direction is accepted for now; detection, merge handling, non-overlapping edits and recovery details remain provisional/open.

## Desired integrations and mobile priorities

- Notion/Confluence editing is desired early, but is not required for the initial capability. Plan for reconciliation into the authoritative vault without assuming those tools directly edit Git files or promising lossless round trips. Connector feasibility, format mapping and synchronization mechanics are unverified/open.
- Phone priorities: read/search existing notes, refresh stale content from the remote, and capture quick thoughts into an inbox for later laptop review come first.
- Reviewing/approving proposals, editing finalized notes and commit/push/sync actions on phones are lower-priority desired capabilities. Laptop commit/push is the primary expectation for now; an easy reviewed save/sync action is preferred.
- Offline-search scope and the detailed offline/mobile synchronization experience remain unanswered. Suggestions from the discussion are not approved requirements.

## Alternatives and limits

One repository with separate directories was considered but not selected as the current direction. A non-Git vault was considered; the user prefers Git history and a hosted remote.
External-tool editing remains desired, not silently deferred indefinitely or promoted to a proven capability.
Java/Gradle, Markdown/Obsidian, agents, module topology, transactions, rollback, automatic derived refresh and source retention details remain open.
This decision does not confirm exhaustive inventory coverage, approve a complete architecture, or assign migration/removal dispositions to old features.

## Traceability

User needs: U02–U04, U06, U09–U16 in the [requirements register](../../requirements/requirements-register.md).
Related inventory: K02, K04–K06, K09, K14–K15, K17; G08, G10–G11; E04, E16.
Historical alternatives H06/H09 remain evidence, not the authority for this decision; see [historical alternatives](../explorations/historical-alternatives.md).
