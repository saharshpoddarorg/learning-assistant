---
id: BLI-001
title: Build production Atlassian MCP server
status: todo
priority: high
type: feature
created: 2026-03-26
updated: 2026-07-31
origin: null
tags: [mcp, atlassian, jira, confluence, bitbucket, multi-account, pat]
---

# BLI-001: Build production Atlassian MCP server

## Description

Create a proper, production-quality Atlassian MCP server — not a stub or prototype.
The current Atlassian server scaffolding exists but needs to be built out into a
fully functional MCP server with real Jira and Confluence integration, proper auth
(OAuth 2.0), error handling, and comprehensive tool coverage.

## Acceptance Criteria

- [ ] OAuth 2.0 authentication flow working for Atlassian Cloud
- [ ] Jira tools: search issues, get issue details, create/update issues, transitions
- [ ] Confluence tools: search pages, get page content, create/update pages
- [ ] Proper error handling with descriptive messages
- [ ] Configuration via `mcp-config.local.properties`
- [ ] All tools registered and discoverable via `list-tools`
- [ ] README documentation with setup instructions
- [ ] Build passes (`.\mcp-servers\build.ps1`)

## Notes

- Current scaffolding: `mcp-servers/src/server/atlassian/`
- Scripts exist: `mcp-servers/scripts/server-specific/` has some Atlassian setup
- Auth helpers: `mcp-servers/scripts/common/auth/` has OAuth flow scripts
- Config: `mcp-servers/user-config/servers/atlassian/` and `atlassian-v2/`

### 2026-07-31 Brainstorm Snapshot — Multi-Account Extrapolation

**Context captured from pair-programming brainstorm:**

- Primary work repo for execution: `E:\mgcnoscan\iesd-26`
- Existing single-account setup source:
	- `E:\mgcnoscan\iesd-26\.env` (placeholders for PAT tokens)
	- skills copied from colleague:
		- `E:\mgcnoscan\iesd-26\.github\skills\jira`
		- `E:\mgcnoscan\iesd-26\.github\skills\bitbucket`
		- `E:\mgcnoscan\iesd-26\.github\skills\confluence`

**Decisions locked:**

1. Treat Atlassian as one umbrella capability, but keep Jira/Bitbucket/Confluence as separate service adapters internally.
2. Use iterative migration, not all-at-once replacement.
3. Prefer profile selection per session (not mandatory per command).
4. Enable non-strict mode with fallback to a configurable default profile.
5. Support different base URLs per profile (work vs personal-work definitely different).

**Resolution strategy (runtime):**

1. If command-level profile override is present, use it.
2. Else if a session profile is set, use it.
3. Else use configurable default profile.
4. Else fallback to `work`.

**Multi-account model:**

- One logical profile per account (`work`, `personal-work`, `personal`, etc.)
- Each profile can define service-specific pairs:
	- `JIRA_BASE_URL` + `JIRA_PAT_TOKEN`
	- `BITBUCKET_BASE_URL` + `BITBUCKET_PAT_TOKEN`
	- `CONFLUENCE_BASE_URL` + `CONFLUENCE_PAT_TOKEN`
- Missing service variables fail fast only for that requested service call.

**Recommended migration order:**

1. Build common profile loader + default profile switch first.
2. Wire Jira and validate end-to-end.
3. Reuse for Bitbucket and validate.
4. Reuse for Confluence and validate.
5. Implement cross-account workflows only after all 3 pass.

**Guardrails required:**

1. Never print PAT token values.
2. Print active profile name + target base URL before each call.
3. Require explicit `source-profile` and `target-profile` for cross-account operations.
4. Provide identity verification command per service (who-am-I check).

**Validation matrix (definition of done for this slice):**

1. `work` profile: Jira/Bitbucket/Confluence checks pass.
2. `personal-work` profile: Jira/Bitbucket/Confluence checks pass.
3. Session fallback behavior verified.
4. Command override behavior verified.
5. Cross-account source/target safety checks verified.

**Deferred inputs (to be provided later):**

1. Personal-work base URLs.
2. Personal-work PAT tokens.
3. Final profile naming convention confirmation.

## Attachments & References

| Type | Path / URL | Added | Notes |
|---|---|---|---|
| Source file | `D:\workdir\MG_FTE\notepad\personal dev\learning-assistant\gpt.txt` | 2026-04-11 | IMP-001: "atlassian" mentioned under mcp-servers section |
| session | `../../sessions/work/requirements/2026-07-31_11-10pm_requirements_atlassian-multi-account-profile-strategy.md` | 2026-07-31 | Detailed brainstorm and requirements capture for multi-account profile extrapolation (work repo: iesd-26) |

## Related

- **BLI-027** — Build code analysis context enhancement chain (depends on this BLI for Bitbucket/Jira/Confluence API access)
- **Session** — `2026-07-31_11-10pm_requirements_atlassian-multi-account-profile-strategy.md` (work requirements capture, full decision trail)

## Activity Log

| Date | Time | Actor | Action | Details |
|---|---|---|---|---|
| 2026-07-31 | 11:10 PM | user+copilot | linked | Added bidirectional cross-reference to work requirements session capture for Atlassian multi-account planning continuity |
| 2026-07-31 | 10:20 PM | user+copilot | refined | Captured multi-account Atlassian migration decisions: session-level profile selection, configurable default fallback, per-service adapters, iterative rollout (Jira → Bitbucket → Confluence), and cross-account guardrails |
| 2026-04-11 | 10:08 PM | system | noted | IMP-001: "atlassian" mentioned in source file — already covered by this BLI, no changes needed |
