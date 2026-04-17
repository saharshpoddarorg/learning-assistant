# Account & Credentials Registry

> **Security note:** This file contains **account identifiers only** — usernames,
> email addresses, and URLs. **No passwords, tokens, API keys, or secrets** are stored here.
> Credentials are managed in secure password managers (not in Git-tracked files).
>
> **Back to:** [PKM Hub](README.md) | [Consolidated inventory](capture-sources-inventory.md)

---

## Personal Accounts

| Service | Account identifier | Email | URL | Notes |
|---|---|---|---|---|
| GitHub (personal) | `saharshpoddar` | saharshpoddar123@gmail.com | [github.com/saharshpoddar](https://github.com/saharshpoddar) | Personal repos |
| GitHub (org) | `saharshpoddarorg` | saharshpoddar123@gmail.com | [github.com/saharshpoddarorg](https://github.com/saharshpoddarorg) | Personal org |
| Confluence (personal) | — | saharshpoddar060699@gmail.com | [saharshpoddarorg.atlassian.net/wiki](https://saharshpoddarorg.atlassian.net/wiki/home) | Personal wiki |
| Gmail (primary) | — | saharshpoddar123@gmail.com | [mail.google.com](https://mail.google.com) | Primary personal email |
| Gmail (dev/work) | — | saharshpoddar060699@gmail.com | [mail.google.com](https://mail.google.com) | Dev + personal Confluence |
| Google Keep | — | saharshpoddar123@gmail.com | [keep.google.com](https://keep.google.com) | Multi-purpose notes |
| Google Docs/Sheets | — | saharshpoddar123@gmail.com | [docs.google.com](https://docs.google.com) | Cloud documents |
| Notion | — | saharshpoddar123@gmail.com | [notion.so](https://www.notion.so) | Personal workspace |
| Apple Notes | — | <!-- TODO: Apple ID email --> | [icloud.com/notes](https://www.icloud.com/notes) | iOS/macOS notes |
| ike | — | <!-- TODO: ike account email --> | — | Mobile task tracker |
| WhatsApp | — | <!-- TODO: phone number --> | [web.whatsapp.com](https://web.whatsapp.com) | Self-chat capture |

---

## Work Accounts (Siemens — DISW LCS)

| Service | Account identifier | Email / SSO | URL | Notes |
|---|---|---|---|---|
| GitHub (work) | `saharsh-poddar_SAGCP` | Siemens corporate SSO | [github.com](https://github.com) | Work org repos |
| Confluence (work) | — | Siemens corporate SSO | [ies-iesd-conf.ies.mentorg.com](https://ies-iesd-conf.ies.mentorg.com/) | Team wiki |
| Jira (work) | — | Siemens corporate SSO | Siemens internal instance | Sprint tracking |
| Outlook (work) | — | Siemens corporate email | [outlook.office.com](https://outlook.office.com) | Work email + calendar |
| Teams (work) | — | Siemens corporate M365 | [teams.microsoft.com](https://teams.microsoft.com) | Self-chat + meetings |
| OneNote (work) | — | Siemens corporate M365 | [onenote.com](https://www.onenote.com) | Notebook: "Saharsh @ Siemens AG" |

---

## Browser Accounts

| Browser | Sync account | Context |
|---|---|---|
| Microsoft Edge | <!-- TODO: Edge sync account --> | Work + personal |
| Google Chrome | saharshpoddar123@gmail.com | Personal |
| Chrome Canary | <!-- TODO: if different --> | Bleeding-edge testing |
| Mozilla Firefox | <!-- TODO: Firefox account email --> | Varies |

---

## Multi-Account Services

Some services are used with multiple accounts for different purposes:

| Service | Account 1 | Account 2 | Routing rule |
|---|---|---|---|
| GitHub | `saharshpoddar` (personal, L0) | `saharsh-poddar_SAGCP` (work, L3) | Work = restricted, Personal = open |
| Confluence | saharshpoddarorg.atlassian.net (personal, L0) | ies-iesd-conf.ies.mentorg.com (work, L1) | Work = ask first, Personal = open |
| Gmail | saharshpoddar123@gmail.com (primary) | saharshpoddar060699@gmail.com (dev) | Both L2 — never access email content |

---

## Credential Management Policy

| Content type | Storage location | In Git? |
|---|---|---|
| Usernames, email addresses | This file | ✅ Yes |
| Service URLs | This file | ✅ Yes |
| API keys, tokens, secrets | Secure password manager | ❌ Never |
| OAuth refresh tokens | Secure credential store / env vars | ❌ Never |
| Service passwords | Secure password manager | ❌ Never |
| 2FA recovery codes | Secure offline storage | ❌ Never |

---

## Related Documents

- [PKM Hub](README.md) — Central index
- [sensitivity-and-access-control.md](sensitivity-and-access-control.md) — What can/cannot be accessed
- [access-policy.md](access-policy.md) — Full access control policy
