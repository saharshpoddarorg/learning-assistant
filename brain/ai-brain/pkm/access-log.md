# PKM Access Log — Audit Trail

> **Purpose:** Chronological audit trail of every content access event related to
> the capture sources documented in [capture-sources-inventory.md](capture-sources-inventory.md).
>
> **Policy:** All access events are governed by [access-policy.md](access-policy.md).
> This log is append-only — entries are never deleted or modified after creation.
>
> **Format:** Each entry records what was accessed, when, from where, why, and the outcome.

---

## Log Format

Each entry follows this structure:

```text
### YYYY-MM-DD HH:MM AM/PM — <SOURCE> — <ACTION>

- **Source:** <capture source name>
- **Content:** <what was accessed or requested>
- **Action:** FETCH | READ | SCRAPE | IMPORT | DENIED
- **Sensitivity:** L0 | L1 | L2 | L3
- **Permission:** granted | denied | not-required | policy-denied
- **Outcome:** success | failure | denied
- **Reason:** <why the access was requested>
- **Session:** <link to session file if applicable>
```

### Action Types

| Action | Description |
|---|---|
| `FETCH` | Retrieved content via API or web request |
| `READ` | Read a local file or pasted content from a capture source |
| `SCRAPE` | Web-scraped a URL associated with a capture source |
| `IMPORT` | Imported content into brain/ai-brain (inbox, notes, library, backlog) |
| `DENIED` | Access was requested but denied per policy |

### Outcome Types

| Outcome | Description |
|---|---|
| `success` | Content was accessed and used as intended |
| `failure` | Access was attempted but failed (technical error) |
| `denied` | Access was denied per policy or user refusal |

---

## Access Log Entries

*No entries yet. This log is populated as content access events occur.*

| Date | Time | Source | Content | Action | Level | Permission | Outcome | Reason |
|---|---|---|---|---|---|---|---|---|
| *(none yet)* | | | | | | | | |

---

## Detailed Entries

*Detailed log entries will appear below as access events occur. Each entry uses the
format defined above.*

*(No entries yet.)*

---

## Summary Statistics

| Metric | Count |
|---|---|
| Total access events | 0 |
| Successful accesses | 0 |
| Denied accesses | 0 |
| Failed accesses | 0 |
| L0 accesses | 0 |
| L1 accesses | 0 |
| L2 access attempts | 0 |
| L3 access attempts | 0 |

*Updated after each access event.*

---

## Related Documents

- [access-policy.md](access-policy.md) — Access control policy
- [capture-sources-inventory.md](capture-sources-inventory.md) — Source registry
