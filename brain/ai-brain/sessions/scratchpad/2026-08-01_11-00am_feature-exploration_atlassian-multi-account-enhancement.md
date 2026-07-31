---
date: 2026-08-01
time: "11:00 AM"
kind: session-capture
domain: personal
category: feature-exploration
project: learning-assistant
subject: atlassian-multi-account-enhancement
tags: [atlassian, jira, bitbucket, confluence, multi-account, profile-switching, credential-management, mcp-tools]
status: draft
version: 1
parent: null
complexity: high
outcomes: []
source: copilot
scope: project
scope-project: learning-assistant
scope-feature: null
scope-transitions: []
scope-refs: []
---

# Atlassian Multi-Account Enhancement — Pair Programming Session

> **Context:** The iesd-26 repo has single-account Atlassian skills (Bitbucket, Jira, Confluence).
> We're planning an enhanced multi-account version for learning-assistant that supports work,
> personal work, personal, and other profiles — with seamless cross-account operations.

---

## User's Vision (Summarized)

- **Source:** Existing iesd-26 skills (bitbucket, jira, confluence) built by colleague
- **Current limitation:** Single-account setup (work account hardcoded as default)
- **Goal:** Multi-account version supporting work, personal-work, personal, etc. profiles
- **Default:** Work profile (changeable later)
- **Features:** Cross-account actions, profile setup wizard, account switching

---

## Final Architecture Decisions (Locked In)

### Q1: Account Profiles ✅
**User:** 3 types (work/personal-work/personal), design for unbounded accounts per type if easy
**Decision:** ✅ **Go with 3 types + unbounded support**
- Most users have 1 account per type
- Advanced users can add secondary accounts: `work|primary`, `work|client-a`, `work|client-b`
- Keep common case simple, scale elegantly

---

### Q2: Profile Activation & Switching ✅
**User:** Option C (hybrid) + priority hierarchy
**Decision:** ✅ **Option C — Hybrid with Priority Order**
```
Account Selection Priority (highest to lowest):
  1. Command-specific override    ("fetch issue from work-acme")
  2. Chat session context override ("switch to personal-work")
  3. DEFAULT_ATLASSIAN_PROFILE    ("work" by default)
```
**Example:**
```
# Scenario 1: Command override wins
User: "List issues from personal-work"
Even if session is "work" → uses personal-work

# Scenario 2: Session context applies
User: "Switch to personal-work" → "Now list my issues"
Session becomes "personal-work" → all ops use it until switched

# Scenario 3: Default fallback
No override, no session switch → uses DEFAULT_ATLASSIAN_PROFILE=work
```
**Rationale:** Removes friction for single-account work, flexibility for advanced ops, clear priority eliminates ambiguity

---

### Q3: Environment Variable Naming ✅
**User:** Unsure — noted A is hard to maintain, B could confuse, C seems good
**Recommendation:** **Option C (Profile-scoped .env files)** ✅
```
.env                       ← Global defaults + DEFAULT_ATLASSIAN_PROFILE=work
.env.work                  ← Profile-specific: JIRA_PAT_TOKEN, JIRA_BASE_URL, BITBUCKET_PAT_TOKEN, etc.
.env.personal-work         ← Personal work account credentials
.env.personal              ← Personal account credentials
.env.work.client-a         ← (Optional) Secondary work account
```
**Why:** Clean separation, GitIgnore-friendly (ignore `.env.local` and profile overrides), mirrors Rails/Django conventions

---

### Q4: Cross-Account Operations ✅
**User:** YES — support copying/migrating from account X to Y
**Decision:** ✅ **Full cross-account support**
```
Example commands:
- Copy Jira issue PROJ-123 from work → personal-work
- Mirror Bitbucket PR from work to personal-work
- Migrate Confluence page tree from work-cloud to work-server
```
**CLI signature:** `--source-account work --target-account personal-work`

---

### Q5: Default Profile Behavior ✅
**User:** Unsure — A (global default) is simpler, B (context-aware) good but risky
**Recommendation:** **Option A (Global default) for MVP** ✅
```
DEFAULT_ATLASSIAN_PROFILE=work  (in .env)
→ Can be changed later to Option B if inference proves useful
```
**Why:** Simpler, configurable, less error-prone. Context-aware inference → v2

---

### Q6: File Organization ✅
**User:** Noted iesd-26 has separate skill folders
**Decision:** ✅ **Option C — Hybrid (Separate Skills + Shared Account Manager)**
```
learning-assistant/.github/skills/
├── bitbucket/
│   ├── SKILL.md
│   ├── scripts/
│   │   ├── bitbucket_cli.js
│   │   ├── bitbucket_cli.ps1
│   │   └── (imports account-manager.js from ../atlassian-common/)
│   └── references/
├── jira/
│   ├── SKILL.md
│   ├── scripts/
│   │   ├── jira_cli.js
│   │   ├── jira_cli.ps1
│   │   └── (imports account-manager.js from ../atlassian-common/)
│   └── references/
├── confluence/
│   ├── SKILL.md
│   ├── scripts/
│   │   ├── confluence_cli.js
│   │   ├── confluence_cli.ps1
│   │   └── (imports account-manager.js from ../atlassian-common/)
│   └── references/
└── atlassian-common/                     ← NEW: Shared utilities
    ├── account-manager.js                (profile loading, credential resolution)
    ├── env-loader.js                     (profile-scoped .env reading)
    └── shared-utils.js                   (common helpers)
```
**Why:** Mirrors iesd-26 structure, shared multi-account logic, no duplication, cross-account ops easy

---

### Q7: CLI/Account Management Commands ✅
**User:** Env vars sound good, don't overcomplicate
**Decision:** ✅ **Env vars only (no new CLI commands)**
- Account selection via `CLI_JSON_ARGS --account work`
- Credential setup via `.env` files + optional setup wizard (PowerShell script)
- Keep it minimal: env vars + Copilot's natural language context

---

### Q8: Backwards Compatibility 🤔
**User:** Not sure
**Recommendation:** **NOT for MVP** ✅
- Provide migration helper script: `./migrate-single-to-multi.ps1`
- Cleans up logic, avoids edge cases
- Users explicitly migrate (one-time)

---

## Architecture Decision Summary

| Decision | Choice | Why |
|----------|--------|-----|
| **Profiles** | 3 types + unbounded per type | Scales gracefully |
| **Activation** | Hybrid (default + per-request override) | Friction-free for common case, flexible for advanced |
| **Credentials** | Profile-scoped .env files | Clean, maintainable, secure |
| **Cross-account** | Fully supported | Copy, migrate, mirror workflows |
| **Default** | Global DEFAULT_ATLASSIAN_PROFILE | Simple, configurable |
| **Organization** | Unified atlassian-tools skill | Consistent multi-account handling |
| **Env selection** | Via env vars + Copilot context | Keep it simple |
| **Old format** | Not supported (migration helper) | Cleaner code, fewer edge cases |

---

## Implementation Roadmap

### Phase 0: Architecture & Design (Now)
- [ ] Define profile structure and naming
- [ ] Design CLI JSON schema for multi-account params
- [ ] Create .env structure + sample files
- [ ] Design account-manager.js API

### Phase 1: MVP (v0.1)
- [ ] Copy iesd-26 scripts, refactor for multi-account
- [ ] Implement profile-scoped .env reading
- [ ] Add `account-manager.js` (profile selection logic)
- [ ] Update SKILL.md with multi-account primer
- [ ] Create `.env.example.work`, `.env.example.personal-work`

### Phase 2: Enhancement (v0.2)
- [ ] Cross-account action recipes (copy, migrate)
- [ ] Setup wizard script (interactive account config)
- [ ] Migration helper (single → multi)

### Phase 3: Polish (v1.0)
- [ ] Integration tests for profile switching
- [ ] Documentation overhaul
- [ ] Optional: Context-aware inference (Q5 Option B)

---

---

## Exchange 2: Detailed Architecture Design (Iterative)

### Section A: Profile & Account Structure

### Section A: Profile & Account Structure

**A1: Profile ID Naming Convention ✅**

**User:** Hierarchical sounds good
**Decision:** ✅ **Option A1-i — Type-based hierarchical naming**
```
work.primary              ← Main work account (default for "work" type)
work.secondary            ← Secondary work account (e.g., different Jira instance)
work.client-acme          ← Named secondary (client-specific)
work.client-microsoft     ← Another client account

personal-work.primary     ← Main personal-work account
personal-work.secondary   ← Secondary personal-work account

personal.primary          ← Main personal account
personal.secondary        ← Secondary personal account (if needed)
```
**Usage in conversation:**
```
"Fetch from work.primary"
"Copy from work.client-acme to personal-work.primary"
"Switch to personal-work.secondary"
```
**Default resolution:** `work` → `work.primary`, `personal-work` → `personal-work.primary`, `personal` → `personal.primary`

---

**A2: Multiple Accounts Per Type ✅**

**User:** Can have multiple, e.g., work.primary, work.secondary, personal-work.secondary
**Decision:** ✅ **Full support for multiple accounts per type**
```
Profile structure:
├── work
│   ├── .primary       (Jira: work.atlassian.net, Bitbucket: work repo)
│   ├── .secondary     (Jira: another workspace, different Bitbucket instance)
│   └── .client-acme   (client-specific accounts)
├── personal-work
│   ├── .primary       (self-hosted Jira, personal GitHub Org)
│   └── .secondary     (backup account, side-project workspace)
└── personal
    └── .primary       (personal Jira Cloud, hobby projects)
```
**Account lookup:** If user says "work" → resolves to "work.primary". If they say "work.client-acme" → uses that exact account.

---

### Section B: .env Structure (Profile-Scoped Files)

### Section B: .env Structure (Profile-Scoped Files)

**B1: Variable Naming in Profile .env Files ✅**

**User:** Profile-agnostic variables make sense (same vars across all profiles, only different files for different accounts)
**Recommendation:** ✅ **Option B1-ii — Profile-agnostic variables**
```
# .env.work, .env.personal-work, .env.personal all use the SAME variable names:
JIRA_PAT_TOKEN=abc123...
JIRA_BASE_URL=https://work-jira.atlassian.net
BITBUCKET_PAT_TOKEN=xyz789...
BITBUCKET_BASE_URL=https://work-bitbucket.example.com
CONFLUENCE_PAT_TOKEN=def456...
CONFLUENCE_BASE_URL=https://work-confluence.example.com

# .env.work.secondary (for secondary work account)
JIRA_PAT_TOKEN=different-token...
JIRA_BASE_URL=https://secondary-jira.atlassian.net
...
```
**Why:** 
- Cleaner code in `account-manager.js` — load `.env.{profile}` once, get all creds
- No special naming per tool (not `JIRA_PAT_TOKEN_WORK` vs `BITBUCKET_PAT_TOKEN_WORK`)
- Easy to duplicate structure when adding new account
- Profile context is already in the filename (`.env.work`, `.env.personal-work`)

---

**B2: Global .env Configuration ✅**

**User:** Comprehensive approach with default and hierarchical, aligning with earlier discussion
**Recommendation:** ✅ **Option B2-ii — Comprehensive global .env + profile-scoped credentials**
```
# .env (global defaults at workspace root)
DEFAULT_ATLASSIAN_PROFILE=work                   ← Default account type
DEFAULT_ACCOUNT_FOR_TYPE_work=primary            ← Default for work type
DEFAULT_ACCOUNT_FOR_TYPE_personal_work=primary   ← Default for personal-work type
DEFAULT_ACCOUNT_FOR_TYPE_personal=primary        ← Default for personal type
SESSION_ACTIVE_PROFILE=work                      ← Changes during session (can be overridden)

# .env.work (account-specific credentials)
JIRA_PAT_TOKEN=work-jira-token...
JIRA_BASE_URL=https://work-jira.atlassian.net
BITBUCKET_PAT_TOKEN=work-bb-token...
BITBUCKET_BASE_URL=https://work-bitbucket.example.com
CONFLUENCE_PAT_TOKEN=work-conf-token...
CONFLUENCE_BASE_URL=https://work-confluence.example.com

# .env.work.secondary
JIRA_PAT_TOKEN=secondary-work-jira-token...
JIRA_BASE_URL=https://secondary-jira.atlassian.net
BITBUCKET_PAT_TOKEN=secondary-work-bb-token...
BITBUCKET_BASE_URL=https://secondary-bitbucket.example.com
CONFLUENCE_PAT_TOKEN=secondary-work-conf-token...
CONFLUENCE_BASE_URL=https://secondary-confluence.example.com

# .env.personal-work
JIRA_PAT_TOKEN=personal-work-jira-token...
JIRA_BASE_URL=https://personal-jira.example.com
...

# .env.personal
JIRA_PAT_TOKEN=personal-jira-token...
...
```
**Why:**
- Secrets stay in profile files (never in root .env)
- Configuration hierarchy clear and maintainable
- Easy to add/remove profiles by adding/removing `.env.{profile}` files
- GitIgnore: ignore all `.env.*` files to protect secrets

**GitIgnore rule:**
```
.env
.env.work
.env.personal-work
.env.personal
.env.*.secondary
.env.*.client-*
.env.local
```

---

### Section C: account-manager.js API (Core Utility)

**What is account-manager.js?** It's a shared Node.js module that all three CLIs (jira_cli.js, bitbucket_cli.js, confluence_cli.js) import and use to:
1. Determine which account to use (which .env.{profile} file to load)
2. Load credentials from the correct .env file
3. Handle profile switching

---

**C1: Core API Functions (What should account-manager.js export?)**

**User:** Didn't understand — let me clarify with examples

```javascript
// account-manager.js exports these functions:

// 1. Get the currently active profile for this session
getActiveProfile()
→ Returns: "work" or "personal-work" or "personal" or "work.secondary"
→ Example: "work.primary"

// 2. Load ALL credentials for a profile
loadProfileCredentials(profileId)
→ Example: loadProfileCredentials("work.primary")
→ Returns: {
    jiraToken: "abc123...",
    jiraUrl: "https://work-jira.atlassian.net",
    bitbucketToken: "xyz789...",
    bitbucketUrl: "https://work-bitbucket.example.com",
    confluenceToken: "def456...",
    confluenceUrl: "https://work-confluence.example.com"
  }

// 3. Switch active profile (changes session state)
switchProfile(profileId)
→ Example: switchProfile("personal-work.primary")
→ Updates internal state; all subsequent CLI calls use this profile

// 4. Validate credentials for a profile (test connectivity)
validateProfileCredentials(profileId)
→ Returns: true/false
→ Useful for setup wizard: "Testing credentials for work.primary..."

// 5. List all available profiles detected from .env files
listAvailableProfiles()
→ Returns: ["work.primary", "work.secondary", "personal-work.primary", "personal.primary"]
→ Scans .env.* files to find what accounts are configured

// 6. Get detailed info about a profile
getProfileInfo(profileId)
→ Returns: {
    type: "work",
    name: "primary",
    fullId: "work.primary",
    credentials: {...},
    isValid: true,
    lastUsed: "2026-08-01T11:30Z"
  }
```

**Recommendation:** ✅ **Export all 6 functions above**

Why? Each one is used by the CLIs or setup wizard for different purposes. Simple, modular, testable.

---

**C2: Error Handling Strategy (What happens when things go wrong?)**

**User:** Didn't understand the options — here are 3 scenarios with recommendations:

**Scenario 1: Profile doesn't exist**
```
User says: "Fetch issue from work.nonexistent"
→ account-manager.js doesn't find .env.work.nonexistent

Option C2-i (Fail fast):
  Throw error → "ERROR: Profile 'work.nonexistent' not configured"
  → User fixes .env, retries

Option C2-ii (Fallback):
  Warn user → "Profile 'work.nonexistent' not found, using 'work.primary' instead"
  → Might silently use wrong account (BAD)

Option C2-iii (Interactive):
  Launch setup wizard → "Profile not found. Set up now? [Y/n]"
  → User configures it on-the-fly
```
**Recommendation:** ✅ **C2-i (Fail fast) for MVP**
- User explicitly configured wrong account name
- Clear error message helps debug
- Future: add setup wizard in Phase 2

---

**Scenario 2: Credentials are invalid (wrong token, expired)**
```
User: "Fetch issue PROJ-123 from work.primary"
→ account-manager loads .env.work
→ Tries to call Jira API
→ Gets 401 Unauthorized (bad token)

Option C2-i (Fail fast):
  Throw error → "ERROR: Jira credentials invalid for work.primary. Check JIRA_PAT_TOKEN in .env.work"
  → User fixes token

Option C2-ii (Fallback):
  Not applicable here (no fallback account)

Option C2-iii (Interactive):
  Prompt user → "Credentials invalid. Re-enter token? [Y/n]"
```
**Recommendation:** ✅ **C2-i (Fail fast) with helpful message**
- Tell user exactly which profile failed and which variable to check
- User updates .env and retries

---

### Section D: CLI Architecture & Cross-Account Operations

**What's the difference between the two approaches?** Let me show with code:

---

**D1: Who reads .env files? (Responsibility division)**

**Current iesd-26 (single-account):**
```javascript
// jira_cli.js
const token = process.env.JIRA_PAT_TOKEN;      // reads from .env directly
const url = process.env.JIRA_BASE_URL;
const issue = await jiraApi.getIssue(url, token, issueKey);
```

**New multi-account Question: Should account-manager.js handle .env reading?**

---

**Option D1-i: account-manager handles all .env reading (recommended) ✅**
```javascript
// jira_cli.js (cleaner)
const accountMgr = require('../atlassian-common/account-manager.js');
const profileId = accountMgr.getActiveProfile();           // e.g., "work.primary"
const creds = accountMgr.loadProfileCredentials(profileId); // loads .env.work
const issue = await jiraApi.getIssue(creds.jiraUrl, creds.jiraToken, issueKey);
```

**Pros:** 
- jira_cli.js doesn't care about .env structure or profile logic
- account-manager is the ONLY place that reads .env files
- DRY: three CLIs don't repeat the same .env loading code

---

**Option D1-ii: account-manager handles only profile selection**
```javascript
// jira_cli.js (more manual)
const accountMgr = require('../atlassian-common/account-manager.js');
const profileId = accountMgr.getActiveProfile();  // e.g., "work.primary"
// jira_cli.js reads .env.work itself
const token = process.env.JIRA_PAT_TOKEN;
const url = process.env.JIRA_BASE_URL;
const issue = await jiraApi.getIssue(url, token, issueKey);
```

**Cons:** 
- Each CLI needs to know how to read .env files
- Duplication across jira_cli.js, bitbucket_cli.js, confluence_cli.js
- Changes to .env loading logic need updates in 3 places

---

**Recommendation:** ✅ **Option D1-i — account-manager handles all .env reading**

**Why:** Follows Single Responsibility Principle — account-manager is responsible for "managing accounts and loading their credentials."

---

**D2: Cross-Account Operations (Copy/Migrate between accounts)**

**User said:** "Cross-account operations definitely to be supported (from profile A to profile B), but not clear exactly what you're asking... maybe account-mgr.js should handle it? Or too much, breaks SRP?"

**Let me clarify with examples:**

**Use Case: Copy a Jira issue from work.primary to personal-work.primary**

The user would say: "Copy issue PROJ-123 from work.primary to personal-work.primary"

---

**Option D2-i: Separate CLI calls (user orchestrates)**
```javascript
// Step 1: Fetch issue from work account
cli_output: jira_cli.js fetch_issue --account work.primary --key PROJ-123
→ Returns issue JSON

// Step 2: User manually creates issue in personal-work account
cli_output: jira_cli.js create_issue --account personal-work.primary --title "..." --description "..."
→ Returns new issue key

// Issue: User has to manually transform data between formats
```
**Cons:** Tedious, error-prone, users have to do the work

---

**Option D2-ii: Dedicated cross-account action in jira_cli.js**
```javascript
// Single CLI call handles source + target account
jira_cli.js copy_issue \
  --source-account work.primary \
  --source-key PROJ-123 \
  --target-account personal-work.primary \
  --target-project MYPROJ

→ CLI: 
    1. Loads work.primary creds
    2. Fetches PROJ-123 from work Jira
    3. Loads personal-work.primary creds
    4. Creates new issue in personal-work Jira
    5. Maps fields intelligently
    6. Returns: "Issue created: MYPROJ-456"
```

**Pros:** 
- Single command, handles everything
- CLI knows how to map fields between Jira instances
- User-friendly

---

**Option D2-iii: account-manager handles cross-account routing (breaks SRP?)**
```javascript
// account-manager tries to do too much:
accountMgr.copyIssueAcrossAccounts({
  source: "work.primary",
  target: "personal-work.primary",
  issueKey: "PROJ-123"
})
```
**Cons:** 
- account-manager becomes "copy business logic" — not just "manage accounts"
- Breaks Single Responsibility Principle
- Couples account management to Jira-specific operations

---

**Recommendation:** ✅ **Option D2-ii — Dedicated cross-account actions in each CLI**

**Why:**
- account-manager.js stays focused: "Load credentials for a profile" ✅
- jira_cli.js handles: "Copy issue across accounts (using account-mgr for credentials)" ✅
- Each tool can implement cross-account ops for its own domain (copy Jira issues, mirror Bitbucket PRs, migrate Confluence pages)
- Cleaner responsibility boundaries

**Example with account-manager:**
```javascript
// jira_cli.js: copy_issue action
const accountMgr = require('../atlassian-common/account-manager.js');

async function copyIssue(sourceAccount, sourceKey, targetAccount, targetProject) {
  // Load source and target credentials
  const sourceCreds = accountMgr.loadProfileCredentials(sourceAccount);
  const targetCreds = accountMgr.loadProfileCredentials(targetAccount);
  
  // Fetch from source
  const issue = await jiraApi.getIssue(sourceCreds.jiraUrl, sourceCreds.jiraToken, sourceKey);
  
  // Map fields intelligently (Jira-specific logic)
  const mappedFields = mapJiraFields(issue);
  
  // Create in target
  const newIssue = await jiraApi.createIssue(targetCreds.jiraUrl, targetCreds.jiraToken, {
    project: targetProject,
    ...mappedFields
  });
  
  return { sourceKey, newKey: newIssue.key };
}
```

**account-manager.js is ONLY:** Load credentials, manage profiles, that's it. ✅

---

## Exchange 2 Summary: Architecture Decisions Locked In ✅

| Decision | Choice | Rationale |
|----------|--------|-----------|
| **Profile Naming** | Hierarchical: `work.primary`, `work.secondary`, `work.client-acme` | Scales gracefully, intuitive for conversation |
| **Multiple Accounts** | Yes — unlimited per type | Supports various use cases (clients, secondaries) |
| **Variable Naming** | Profile-agnostic (same `JIRA_PAT_TOKEN` across `.env.work`, `.env.personal-work`) | Simpler for users, easier code in account-manager.js |
| **Global .env** | Comprehensive with defaults + hierarchical | Centralized config, secrets in profile files |
| **account-manager.js API** | 6 core functions (getActiveProfile, loadProfileCredentials, switchProfile, validateProfileCredentials, listAvailableProfiles, getProfileInfo) | Modular, testable, covers all use cases |
| **Error Handling** | Fail fast with helpful messages | Clear feedback, user fixes .env explicitly |
| **.env Reading** | account-manager.js handles it (D1-i) | DRY, single source of truth, each CLI doesn't repeat logic |
| **Cross-Account Ops** | Dedicated actions per CLI (D2-ii) | Respects SRP, account-manager stays focused on credentials |

---

## Architecture Summary Diagram

```
User Chat
    ↓
Copilot/Copilot detects intent: "Copy issue from work.primary to personal-work"
    ↓
Invokes: jira_cli.js copy_issue --source-account work.primary --source-key PROJ-123 --target-account personal-work.primary
    ↓
jira_cli.js
    ├─ account-manager.loadProfileCredentials("work.primary") 
    │  └─ reads .env.work, returns {jiraToken, jiraUrl, bitbucketToken, ...}
    ├─ account-manager.loadProfileCredentials("personal-work.primary")
    │  └─ reads .env.personal-work, returns {jiraToken, jiraUrl, ...}
    ├─ Fetch PROJ-123 from work Jira
    ├─ Map fields (Jira-specific logic)
    └─ Create issue in personal-work Jira → returns MYPROJ-456
    ↓
Result: "Issue copied: PROJ-123 → MYPROJ-456"
```

---

---

## Implementation Strategy: Two-Phase Approach ✅

**User Preference:** 
- **Q1:** Option A — Copy iesd-26 as-is first to understand existing code (ensures no regression)
- **Q2:** Option B — Refactor immediately BUT want to ensure it works properly like iesd-26

**Recommendation:** Hybrid two-phase to satisfy both concerns:

### **Phase 1a: Copy & Baseline (Days 1-2)**
Goal: Verify iesd-26 code works in learning-assistant without changes

```
✅ Copy .github/skills/bitbucket to learning-assistant
✅ Copy .github/skills/jira to learning-assistant
✅ Copy .github/skills/confluence to learning-assistant
✅ Test each CLI in isolation with iesd-26 accounts
✅ Verify: jira_cli.js, bitbucket_cli.js, confluence_cli.js all work as-is
```

**Outcome:** Working baseline identical to iesd-26 (zero regression risk)

---

### **Phase 1b: Build account-manager.js & Integrate (Days 3-5)**
Goal: Refactor CLIs to use multi-account infrastructure

```
✅ Create atlassian-common/ folder with:
   ├─ account-manager.js (6 core functions)
   ├─ env-loader.js (profile-scoped .env reading)
   └─ shared-utils.js (common helpers)
   
✅ Refactor jira_cli.js to use account-manager.js
✅ Refactor bitbucket_cli.js to use account-manager.js
✅ Refactor confluence_cli.js to use account-manager.js

✅ Create .env.example files (work, personal-work, personal)
✅ Test each CLI with multiple profiles
✅ Test priority order: command override > session context > default
```

**Outcome:** Multi-account support with clean architecture

---

### **Phase 1c: Cross-Account Operations & SKILL.md (Days 6-7)**
Goal: Add cross-account copy/migrate actions

```
✅ Add copy_issue action to jira_cli.js (work → personal-work)
✅ Add mirror_pr action to bitbucket_cli.js
✅ Add migrate_page action to confluence_cli.js
✅ Update SKILL.md for each tool with multi-account primer
✅ Document cross-account workflows
```

**Outcome:** Full MVP feature set

---

## Implementation Checklist (Phase 1a — Start Here)

**Phase 1a: Copy & Baseline**

- [ ] Copy `E:\mgcnoscan\iesd-26\.github\skills\bitbucket` → `learning-assistant\.github\skills\bitbucket`
- [ ] Copy `E:\mgcnoscan\iesd-26\.github\skills\jira` → `learning-assistant\.github\skills\jira`
- [ ] Copy `E:\mgcnoscan\iesd-26\.github\skills\confluence` → `learning-assistant\.github\skills\confluence`
- [ ] Test: `jira_cli.js fetch_issue --key PROJ-123` (with iesd-26 .env)
- [ ] Test: `bitbucket_cli.js fetch_pr --project IESD --repo iesd-26 --prId 123`
- [ ] Test: `confluence_cli.js fetch_page --page-id 12345`
- [ ] Verify all three CLIs work identically to iesd-26
- [ ] Commit: `feat(atlassian): Copy single-account skills from iesd-26`

**Next:** Once Phase 1a passes, we start Phase 1b (account-manager.js)

---

---

## Complete Architecture Summary (All Decisions Finalized)

### Profile & Account Management ✅
- 3 types + unbounded per type (hierarchical IDs: `work.primary`, `work.secondary`, `work.client-acme`)
- Multiple accounts per type fully supported
- Priority order: **command override > session context > global default**

### Credentials & Configuration ✅
- Profile-scoped `.env` files (`.env.work`, `.env.personal-work`, `.env.personal`, `.env.work.secondary`, etc.)
- Profile-agnostic variables in each file (same `JIRA_PAT_TOKEN` across profiles, only env file differs)
- Comprehensive global `.env` with defaults + hierarchical settings

### Architecture ✅
- Hybrid structure: separate skills (`bitbucket/`, `jira/`, `confluence/`) + shared `atlassian-common/`
- `account-manager.js`: 6 core functions (getActiveProfile, loadProfileCredentials, switchProfile, validateProfileCredentials, listAvailableProfiles, getProfileInfo)
- Error handling: Fail fast with clear messages
- Cross-account operations: Dedicated actions in each CLI (copy_issue, mirror_pr, migrate_page, etc.)

### Refactoring Approach ✅
- **Phase 1a:** Copy iesd-26 as-is, verify baseline (zero regression risk)
- **Phase 1b:** Build account-manager.js, refactor CLIs to use it (clean architecture)
- **Phase 1c:** Add cross-account operations, update SKILL.md docs

---

## Ready to Build! 🚀

**Next step:** Execute Phase 1a checklist above (copy iesd-26 skills to learning-assistant)

Once Phase 1a is complete and tested, we move to Phase 1b (account-manager.js)

---

**Session Status:** ✅ All decisions finalized, ready for implementation
