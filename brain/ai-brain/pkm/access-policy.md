# PKM Access Policy — Content Access Control for Capture Sources

> **Purpose:** Define the rules and protocols governing when and how AI assistants,
> automation tools, and scripts may access content from the capture sources documented
> in [capture-sources-inventory.md](capture-sources-inventory.md).
>
> **Scope:** Applies to all AI interactions (Copilot Chat, agents, MCP tools), all
> automation scripts, and any future integrations that read from external capture sources.
>
> **Owner:** Saharsh Poddar
>
> **Status:** Active — enforced immediately. Placeholders marked with `<!-- TODO -->` will
> be filled by the owner over time.

---

## Core Principles

1. **Default deny** — Do NOT access any content from external capture sources unless
   explicitly instructed to do so by the user in the current conversation.
2. **Never proactively scrape** — AI assistants must never autonomously fetch, scrape,
   or read content from capture sources. All access is user-initiated.
3. **Always log** — Every access attempt (granted or denied) is recorded in
   [access-log.md](access-log.md) with timestamp, source, action, and outcome.
4. **Inform on access** — When content IS accessed, the AI must inform the user in the
   chat response: what was accessed, from where, when, and why.
5. **User controls everything** — The user can grant, revoke, or modify access permissions
   at any time. Verbal permission in chat is sufficient for one-time access.
6. **Credentials never in Git** — No passwords, tokens, API keys, or secrets are stored
   in any file within this repository. Account identifiers (usernames, emails, URLs) are
   permitted in the inventory.
7. **Respect sensitivity levels** — Content classification in the inventory determines
   the access protocol. Higher sensitivity = stricter controls.

---

## Sensitivity Levels — Access Protocols

### L0 — Public (Open Access)

| Property | Value |
|---|---|
| **Label** | `public` |
| **AI access** | ✅ Freely accessible |
| **User permission required** | No — accessible by default |
| **Logging** | Log access (not denials — there are none) |
| **Examples** | Public GitHub repos, browser bookmarks, brain/ai-brain content |

**Protocol:** Access freely when relevant to the user's request. Log the access.

### L1 — Internal (Ask Before Accessing)

| Property | Value |
|---|---|
| **Label** | `internal` |
| **AI access** | ⚠️ Ask before accessing |
| **User permission required** | Yes — explicit verbal or written permission per session |
| **Logging** | Log both the permission request and the access outcome |
| **Examples** | Google Keep (Work, Personal, Todos), Notion pages, Apple Notes |

**Protocol:**

1. Ask the user: "I'd like to read [specific content] from [source]. May I proceed?"
2. Wait for explicit confirmation.
3. If granted → proceed and log. If denied → log denial and stop.
4. Permission is valid only for the current chat session unless the user says otherwise.

### L2 — Confidential (Never Access)

| Property | Value |
|---|---|
| **Label** | `confidential` |
| **AI access** | ❌ Do NOT access |
| **User permission required** | N/A — access is prohibited regardless of permission |
| **Logging** | Log any access attempt as DENIED |
| **Examples** | Google Keep (Confidential, Finance), Gmail content, WhatsApp documents |

**Protocol:**

1. If the user asks to access L2 content, remind them of the restriction.
2. Log the request as DENIED with reason "L2 confidential — policy prohibits access."
3. Suggest the user access the content themselves and paste relevant non-sensitive excerpts.

### L3 — Restricted (Legally/Contractually Prohibited)

| Property | Value |
|---|---|
| **Label** | `restricted` |
| **AI access** | ❌ NEVER access |
| **User permission required** | N/A — access is absolutely prohibited |
| **Logging** | Log any access attempt as DENIED |
| **Examples** | All work GitHub repos, work Confluence, work Jira, work Outlook |

**Protocol:**

1. Under NO circumstances access L3 content, even if the user explicitly asks.
2. Explain that the content is contractually restricted (NDA/corporate IP).
3. Log the request as DENIED with reason "L3 restricted — NDA/corporate IP."
4. Suggest the user share non-proprietary excerpts if they need help with the topic.

---

## Access Decision Matrix

| Action | L0 Public | L1 Internal | L2 Confidential | L3 Restricted |
|---|---|---|---|---|
| Read/fetch content | ✅ Proceed | ⚠️ Ask first | ❌ Deny | ❌ Deny |
| Web scrape source URL | ✅ Proceed | ⚠️ Ask first | ❌ Deny | ❌ Deny |
| Summarise content | ✅ Proceed | ⚠️ Ask first | ❌ Deny | ❌ Deny |
| Import to brain | ✅ Proceed | ⚠️ Ask first | ❌ Deny | ❌ Deny |
| Reference source exists | ✅ Proceed | ✅ Proceed | ✅ Proceed | ✅ Proceed |
| Mention source in chat | ✅ Proceed | ✅ Proceed | ✅ Proceed | ✅ Proceed |

**Note:** Referencing that a source exists (e.g., "your Google Keep has a Finance label")
is always permitted. Only accessing the *content* is controlled.

---

## Credential Management Policy

### What Goes Where

| Content type | Storage location | In Git? |
|---|---|---|
| Usernames, email addresses | `capture-sources-inventory.md` (Account Registry) | ✅ Yes |
| Service URLs | `capture-sources-inventory.md` (Account Registry) | ✅ Yes |
| API keys, tokens, secrets | Secure password manager (e.g., 1Password, Bitwarden) | ❌ Never |
| OAuth refresh tokens | Secure credential store / env vars | ❌ Never |
| Service passwords | Secure password manager | ❌ Never |
| 2FA recovery codes | Secure offline storage | ❌ Never |

### Credential Placeholders

The inventory uses `<!-- TODO: ... -->` comments for credentials not yet filled in.
These placeholders should be filled **by the owner only** and only with identifiers
(not secrets). Secrets are never added to these files.

---

## Future Access Integration

When direct integrations are implemented (e.g., Google Keep API, Confluence API), each
integration must:

1. **Respect sensitivity levels** — Check the per-source sensitivity map before accessing.
2. **Authenticate securely** — Use OAuth or token-based auth with credentials stored
   outside the repo (env vars, credential store).
3. **Log all access** — Every API call that reads content must be logged in access-log.md.
4. **Operate read-only by default** — Write operations require explicit user approval
   per operation (not blanket permission).
5. **Rate limit** — Do not make excessive API calls; batch reads where possible.
6. **Handle failures gracefully** — Log failures; do not retry without user awareness.

---

## Policy Amendments

This policy can be amended by the owner at any time. Amendments are:

1. Documented as edits to this file with descriptive commit messages.
2. Effective immediately upon commit.
3. Retroactively applied — if a source's sensitivity level changes, the new level
   applies to all future access, regardless of prior permissions granted.

---

## Related Documents

- [capture-sources-inventory.md](capture-sources-inventory.md) — Source registry with sensitivity map
- [access-log.md](access-log.md) — Audit trail of all access events
- [pkm-philosophy.md](../pkm-philosophy.md) — Why ai-brain is structured this way
- [backlog/README.md](../backlog/README.md) — Backlog system (jot-down integration)
