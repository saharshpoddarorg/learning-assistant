# Workflow Playbooks

Load this file when executing end-to-end workflows that span multiple Atlassian tools or involve multi-step processes.

---

## 1. Sprint Planning

End-to-end sprint planning ceremony support.

### Steps

1. **Get active sprints** — identify the target sprint

   ```powershell
   $env:CLI_JSON_ARGS = '{"boardId":"42"}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" get_jira_sprints
   ```

2. **Review current sprint** — see what is already committed

   ```powershell
   $env:CLI_JSON_ARGS = '{"sprintId":"123","maxResults":50}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" get_sprint_issues
   ```

3. **Find backlog candidates** — search for ready items

   ```powershell
   $env:CLI_JSON_ARGS = '{"jql":"project = PROJ AND sprint is EMPTY AND status = \"Ready\" ORDER BY priority ASC, rank ASC","maxResults":30}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" search_jira_issues
   ```

4. **Move selected issues to sprint**

   ```powershell
   $env:CLI_JSON_ARGS = '{"sprintId":"123","issueKeys":["PROJ-101","PROJ-102","PROJ-103"]}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" move_to_sprint
   ```

5. **Add sprint goal label** to all sprint items

   ```powershell
   foreach ($key in @("PROJ-101","PROJ-102","PROJ-103")) {
       $env:CLI_JSON_ARGS = "{`"issueKey`":`"$key`",`"labels`":[`"sprint-24`"]}"; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" add_jira_labels | Out-Null
   }
   ```

---

## 2. Sprint Review / Retrospective Documentation

Create a Confluence page summarizing sprint outcomes.

### Steps

1. **Gather completed issues** from the sprint

   ```powershell
   $env:CLI_JSON_ARGS = '{"jql":"sprint = 123 AND status = Done ORDER BY issuetype ASC","maxResults":50}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" search_jira_issues
   ```

2. **Gather incomplete issues** (spillover)

   ```powershell
   $env:CLI_JSON_ARGS = '{"jql":"sprint = 123 AND status != Done ORDER BY priority ASC","maxResults":50}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" search_jira_issues
   ```

3. **Generate Confluence page** with review content

   Use `contentFile` pattern. Structure the page with:
   - AI disclaimer (note macro at bottom)
   - TOC macro
   - Executive summary (info panel with key metrics)
   - Completed items table (issue key, summary, type, assignee)
   - Spillover items table with reasons
   - Retrospective notes (what went well, what to improve, action items)

4. **Create the page**

   ```powershell
   $env:CLI_JSON_ARGS = '{"title":"Sprint 24 Review — 2026-04-18","spaceKey":"ENG","parentPageId":"602112114","contentFile":"<workspace>/temp-atlassian-tools/cli_content.html"}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" create_confluence_page
   ```

---

## 3. Release Management

Document and track a release across Jira and Confluence.

### Steps

1. **Get release issues** — all issues in the target version

   ```powershell
   $env:CLI_JSON_ARGS = '{"jql":"fixVersion = \"v2.1.0\" AND project = PROJ ORDER BY issuetype ASC, priority ASC","maxResults":100}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" search_jira_issues
   ```

2. **Check release readiness** — find incomplete items

   ```powershell
   $env:CLI_JSON_ARGS = '{"jql":"fixVersion = \"v2.1.0\" AND project = PROJ AND status != Done ORDER BY priority ASC","maxResults":50}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" search_jira_issues
   ```

3. **Create release notes page** in Confluence

   Structure: version summary, features (stories), bug fixes, known issues, breaking changes, upgrade instructions.

4. **Bulk-transition completed items** to Released status

   ```powershell
   $env:CLI_JSON_ARGS = '{"issueKeys":["PROJ-101","PROJ-102","PROJ-103"],"transition":"Released"}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" bulk_transition_jira_issues
   ```

---

## 4. Incident Post-Mortem

Document and track incidents across Jira and Confluence.

### Steps

1. **Create incident ticket** in Jira

   ```powershell
   $env:CLI_JSON_ARGS = '{"projectKey":"PROJ","issueType":"Bug","summary":"[INCIDENT] Production API timeout — 2026-04-18","description":"Production API returning 504 errors starting 14:30 UTC. Affecting /api/v2/orders endpoint.","priority":"Blocker","labels":["incident","production"]}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" create_jira_issue
   ```

2. **Link related issues** (root cause, affected services)

   ```powershell
   $env:CLI_JSON_ARGS = '{"inwardIssueKey":"PROJ-500","outwardIssueKey":"PROJ-450","linkType":"Causes"}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" link_jira_issues
   ```

3. **Create post-mortem Confluence page**

   Structure: incident timeline, root cause analysis, impact assessment, resolution steps, action items, prevention measures.

4. **Log investigation time**

   ```powershell
   $env:CLI_JSON_ARGS = '{"issueKey":"PROJ-500","timeSpent":"4h","comment":"Root cause investigation and resolution"}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" add_jira_worklog
   ```

---

## 5. Code Review Documentation

Bridge Bitbucket PRs with Confluence documentation.

### Steps

1. **Fetch PR details and changed files**

   ```powershell
   $env:CLI_JSON_ARGS = '{"project":"IESD","repo":"iesd-26","prId":"17905"}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" fetch_bitbucket_pr
   $env:CLI_JSON_ARGS = '{"project":"IESD","repo":"iesd-26","prId":"17905"}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" fetch_bitbucket_pr_files
   ```

2. **Get PR diff for specific files of interest**

   ```powershell
   $env:CLI_JSON_ARGS = '{"project":"IESD","repo":"iesd-26","prId":"17905","filePath":"src/main/java/App.java","contextLines":10}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" fetch_bitbucket_pr_diff
   ```

3. **Review existing comments**

   ```powershell
   $env:CLI_JSON_ARGS = '{"project":"IESD","repo":"iesd-26","prId":"17905"}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" get_bitbucket_pr_comments
   ```

4. **Create review summary Confluence page** with findings, patterns, risk areas

5. **Post review findings back to PR**

   ```powershell
   $env:CLI_JSON_ARGS = '{"project":"IESD","repo":"iesd-26","prId":"17905","comment":"Review summary posted to Confluence: https://..."}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" add_bitbucket_pr_comment
   ```

---

## 6. Status Reporting

Generate weekly or sprint status reports in Confluence from Jira data.

### Steps

1. **Gather metrics** — issues completed, in progress, blocked

   ```powershell
   # Completed this week
   $env:CLI_JSON_ARGS = '{"jql":"project = PROJ AND status CHANGED TO Done DURING (startOfWeek(), now())","maxResults":50}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" search_jira_issues

   # Currently in progress
   $env:CLI_JSON_ARGS = '{"jql":"project = PROJ AND status = \"In Progress\" ORDER BY priority ASC","maxResults":50}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" search_jira_issues

   # Blocked items
   $env:CLI_JSON_ARGS = '{"jql":"project = PROJ AND status = Blocked ORDER BY priority ASC","maxResults":50}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" search_jira_issues
   ```

2. **Build status page** with tables showing:
   - Summary metrics (completed count, in-progress count, blocked count)
   - Completed items table
   - In-progress items with progress notes
   - Blocked items with blockers identified
   - Risks and escalations
   - Next week's plan

3. **Create or update the status page** in Confluence

---

## 7. Onboarding Documentation

Create team onboarding pages from existing Jira project and Confluence space data.

### Steps

1. **Get project components and versions**

   ```powershell
   $env:CLI_JSON_ARGS = '{"projectKey":"PROJ"}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" get_jira_components
   $env:CLI_JSON_ARGS = '{"projectKey":"PROJ"}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" get_jira_versions
   ```

2. **Get space structure** — understand the documentation layout

   ```powershell
   $env:CLI_JSON_ARGS = '{"pageId":"602112114","depth":2}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" get_confluence_page_tree
   ```

3. **Get issue types and statuses** — understand the workflow

   ```powershell
   $env:CLI_JSON_ARGS = '{"projectKey":"PROJ"}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" get_jira_issue_types
   $env:CLI_JSON_ARGS = '{"projectKey":"PROJ"}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" get_jira_statuses
   ```

4. **Create onboarding page** with project overview, team structure, workflows, key pages, getting started steps

---

## 8. Bulk Triage

Efficiently triage a backlog of unassigned, unlabelled issues.

### Steps

1. **Find untriaged issues**

   ```powershell
   $env:CLI_JSON_ARGS = '{"jql":"project = PROJ AND labels is EMPTY AND assignee is EMPTY AND status = Open ORDER BY created DESC","maxResults":50}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" search_jira_issues
   ```

2. **For each issue, assign labels and component**

   ```powershell
   $env:CLI_JSON_ARGS = '{"issueKey":"PROJ-200","labels":["backend","api"]}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" add_jira_labels
   ```

3. **Assign to team members**

   ```powershell
   $env:CLI_JSON_ARGS = '{"issueKey":"PROJ-200","assignee":"john.doe"}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" update_jira_issue
   ```

4. **Add triage comment**

   ```powershell
   $env:CLI_JSON_ARGS = '{"issueKey":"PROJ-200","comment":"Triaged: backend API issue, assigned to John for Sprint 25"}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" add_jira_comment
   ```

5. **Summarize** — report total triaged, assigned, labelled

---

## Cross-Tool Linking Patterns

| From | To | How |
|---|---|---|
| Jira issue → Confluence page | Add comment with page URL | `add_jira_comment` with Confluence page link |
| Confluence page → Jira issue | Embed Jira macro in page HTML | `<ac:structured-macro ac:name="jira">` with JQL |
| Bitbucket PR → Jira issue | PR title contains issue key | Bitbucket auto-links `PROJ-123` in PR titles |
| Jira issue → Bitbucket PR | Add comment with PR URL | `add_jira_comment` with Bitbucket PR link |
| Confluence page → Bitbucket | Link to repo/file in page content | Standard HTML link in page body |

---

## Multi-Instance Setup (Cross-Account Operations)

The CLI reads tokens from environment variables. To operate across **two different Atlassian instances**
(e.g., copy a Confluence page from Account A to Account B), swap the environment variables between calls.

### Pattern: .env files per instance

Create separate `.env` files for each instance:

```text
<workspace>/.env                    ← default instance (Account A)
<workspace>/.env.account-b          ← second instance (Account B)
```

### Pattern: Inline env-var swap in PowerShell

```powershell
# --- Read from Account A (default .env) ---
$env:CLI_JSON_ARGS = '{"pageId":"12345"}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" fetch_confluence_page

# --- Switch to Account B ---
$env:CONFLUENCE_PAT_TOKEN = "TOKEN_FOR_ACCOUNT_B"
$env:CONFLUENCE_BASE_URL = "https://other-instance.atlassian.net"

# --- Write to Account B ---
$env:CLI_JSON_ARGS = '{"title":"Migrated Page","spaceKey":"TARGET","parentPageId":"67890","contentFile":"<workspace>/temp-atlassian-tools/migrated_page.html"}'
node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" create_confluence_page

# --- Restore Account A (reload from .env or set explicitly) ---
$env:CONFLUENCE_PAT_TOKEN = "TOKEN_FOR_ACCOUNT_A"
$env:CONFLUENCE_BASE_URL = "https://original-instance.example.com"
```

### Helper: Load env file function

```powershell
function Load-EnvFile($path) {
    Get-Content $path | ForEach-Object {
        if ($_ -match '^\s*([^#][^=]+)=(.*)$') {
            [System.Environment]::SetEnvironmentVariable($Matches[1].Trim(), $Matches[2].Trim(), "Process")
        }
    }
}

# Usage
Load-EnvFile "<workspace>/.env"             # Account A
Load-EnvFile "<workspace>/.env.account-b"   # Account B (overrides A's vars)
```

### Security rules for multi-instance

- Never log or echo token values
- Restore the original `.env` after cross-account operations
- Use `$env:ENV_FILE` override if the CLI supports explicit `.env` path
- Store `.env.account-b` in the same gitignored location as `.env`

---

## 9. Cross-Account Confluence Page Migration

Copy or migrate Confluence pages (including page trees) between different Atlassian instances or between
spaces/pages within the same instance. Supports: single page, page with children, and full subtree migration.

### Scenario A — Single page: Account A → Account B

**Inputs from user:**

- Source page URL or page ID (Account A)
- Target space key and parent page ID (Account B)
- Account B credentials (base URL + PAT)

#### Steps

1. **Extract page ID from URL** (if user provides a URL)

   Confluence URLs follow the pattern: `https://instance/pages/viewpage.action?pageId=12345`
   or `https://instance/spaces/SPACE/pages/12345/Page+Title`. Extract the numeric page ID.

2. **Fetch source page content** from Account A

   ```powershell
   $env:CLI_JSON_ARGS = '{"pageId":"12345"}'; $srcPage = (node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" fetch_confluence_page) | ConvertFrom-Json
   ```

3. **Save HTML content to file** (avoids mojibake on cross-instance transfer)

   ```powershell
   $srcPage.data.body.storage.value | Out-File -Encoding utf8 "<workspace>/temp-atlassian-tools/migrated_page.html"
   ```

4. **Switch to Account B credentials**

   ```powershell
   $env:CONFLUENCE_PAT_TOKEN = "ACCOUNT_B_TOKEN"
   $env:CONFLUENCE_BASE_URL = "https://account-b-instance.example.com"
   ```

5. **Create page in Account B**

   ```powershell
   $env:CLI_JSON_ARGS = '{"title":"Migrated: Original Page Title","spaceKey":"TARGET","parentPageId":"67890","contentFile":"<workspace>/temp-atlassian-tools/migrated_page.html"}'
   node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" create_confluence_page
   ```

6. **Copy labels** (if needed)

   ```powershell
   # Fetch labels from source (switch back to Account A first)
   $env:CONFLUENCE_PAT_TOKEN = "ACCOUNT_A_TOKEN"
   $env:CONFLUENCE_BASE_URL = "https://account-a-instance.example.com"
   $env:CLI_JSON_ARGS = '{"pageId":"12345"}'; $labels = (node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" get_confluence_page_labels) | ConvertFrom-Json

   # Apply labels to target (switch to Account B)
   $env:CONFLUENCE_PAT_TOKEN = "ACCOUNT_B_TOKEN"
   $env:CONFLUENCE_BASE_URL = "https://account-b-instance.example.com"
   $labelNames = ($labels.data.results | ForEach-Object { $_.name }) -join '","'
   $env:CLI_JSON_ARGS = "{`"pageId`":`"NEW_PAGE_ID`",`"labels`":[`"$labelNames`"]}"
   node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" add_confluence_page_labels
   ```

7. **Restore original credentials**

### Scenario B — Page tree: migrate root + all children

#### Steps

1. **Fetch the page tree** from source instance

   ```powershell
   $env:CLI_JSON_ARGS = '{"pageId":"12345","depth":3}'; $tree = (node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" get_confluence_page_tree) | ConvertFrom-Json
   ```

2. **For each page in the tree** (breadth-first to preserve parent-child relationships):

   a. Fetch page content from source
   b. Save to temp file
   c. Switch to target instance
   d. Create page under the correct parent (map source parent ID → target parent ID)
   e. Record the mapping: `{ sourceId → targetId }`

3. **Build an ID mapping table** as you go:

   ```powershell
   $idMap = @{}  # sourcePageId → targetPageId

   foreach ($page in $flattenedTree) {
       # Fetch from source
       Load-EnvFile "<workspace>/.env"  # Account A
       $env:CLI_JSON_ARGS = "{`"pageId`":`"$($page.id)`"}"
       $content = (node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" fetch_confluence_page) | ConvertFrom-Json
       $content.data.body.storage.value | Out-File -Encoding utf8 "<workspace>/temp-atlassian-tools/migrated_page.html"

       # Create in target
       Load-EnvFile "<workspace>/.env.account-b"  # Account B
       $targetParent = if ($idMap.ContainsKey($page.parentId)) { $idMap[$page.parentId] } else { "ROOT_TARGET_ID" }
       $env:CLI_JSON_ARGS = "{`"title`":`"$($content.data.title)`",`"spaceKey`":`"TARGET`",`"parentPageId`":`"$targetParent`",`"contentFile`":`"<workspace>/temp-atlassian-tools/migrated_page.html`"}"
       $result = (node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" create_confluence_page) | ConvertFrom-Json
       $idMap[$page.id] = $result.data.id
   }
   ```

4. **Post-migration**: rewrite internal links in migrated pages (replace source page IDs/URLs with target ones)

5. **Summarize**: report total pages migrated, any failures, ID mapping table

### Scenario C — Same instance: copy page to different space or parent

Use the built-in `copy_confluence_page` action directly:

```powershell
$env:CLI_JSON_ARGS = '{"pageId":"12345","newTitle":"Copied Page","targetSpaceKey":"NEW_SPACE","targetParentId":"67890"}'
node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" copy_confluence_page
```

For moving (not copying):

```powershell
$env:CLI_JSON_ARGS = '{"pageId":"12345","targetParentId":"67890","targetSpaceKey":"NEW_SPACE"}'
node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" move_confluence_page
```

### Scenario D — Merge content from multiple source pages into one target page

1. Fetch content from each source page
2. Concatenate the HTML bodies with section dividers (`<h2>From: Source Page Title</h2><hr/>`)
3. Write merged HTML to `contentFile`
4. Create or update the target page with the merged content

### Caveats

- **Attachments** are not migrated by the CLI — download and re-upload manually or via REST
- **Embedded macros** (Jira issue lists, dynamic content) may not render on the target instance
  if the target lacks the same Jira integration or macro apps
- **Page permissions** are not copied — set permissions manually on the target
- **Internal links** (`/pages/viewpage.action?pageId=XXXXX`) will point to the old instance — run
  a find-and-replace pass on the migrated HTML to update links

---

## 10. Resume / Work Analysis from Jira + Bitbucket

Deep analysis of your Jira tickets, comments, PR contributions, and code changes to generate
resume-ready bullet points, contribution summaries, and impact statements.

### Use cases

- Update your resume with recent work accomplishments
- Prepare for performance reviews with quantified contributions
- Generate a portfolio of work for career conversations
- Summarize your contributions for a specific time period or project

### Steps

1. **Gather all issues you worked on** (assigned, reported, commented)

   ```powershell
   # Issues assigned to me (resolved in last 6 months)
   $env:CLI_JSON_ARGS = '{"jql":"assignee = currentUser() AND status CHANGED TO Done DURING (-180d, now()) ORDER BY resolved DESC","maxResults":100}'
   node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" search_jira_issues

   # Issues I reported
   $env:CLI_JSON_ARGS = '{"jql":"reporter = currentUser() AND created >= -180d ORDER BY created DESC","maxResults":50}'
   node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" search_jira_issues

   # Issues I commented on (broader participation)
   $env:CLI_JSON_ARGS = '{"jql":"issueFunction in commented(\"by currentUser()\") AND updated >= -180d ORDER BY updated DESC","maxResults":50}'
   node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" search_jira_issues
   ```

   > Note: `issueFunction` requires ScriptRunner plugin. If unavailable, search by project
   > and filter comments locally.

2. **For high-impact issues, fetch full details and comments**

   ```powershell
   foreach ($key in @("PROJ-101","PROJ-205","PROJ-310")) {
       $env:CLI_JSON_ARGS = "{`"issueKey`":`"$key`"}"
       node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" fetch_jira_issue

       $env:CLI_JSON_ARGS = "{`"issueKey`":`"$key`",`"maxResults`":20}"
       node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" fetch_jira_comments
   }
   ```

3. **Fetch change history** for issues where you drove status transitions

   ```powershell
   $env:CLI_JSON_ARGS = '{"issueKey":"PROJ-101","maxResults":50}'
   node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" get_jira_issue_changelog
   ```

4. **Get Bitbucket PR contributions** — PRs authored, reviewed, code volume

   ```powershell
   # Your PRs
   $env:CLI_JSON_ARGS = '{"project":"IESD","repo":"iesd-26","author":"your.username","state":"MERGED","maxResults":50}'
   node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" search_bitbucket_prs

   # Contribution summary (aggregated stats)
   $env:CLI_JSON_ARGS = '{"project":"IESD","repo":"iesd-26","person":"your.username","months":6}'
   node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" summarize_bitbucket_contributions
   ```

5. **For key PRs, fetch diff stats and review comments**

   ```powershell
   $env:CLI_JSON_ARGS = '{"project":"IESD","repo":"iesd-26","prId":"17905"}'
   node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" fetch_bitbucket_pr_files

   $env:CLI_JSON_ARGS = '{"project":"IESD","repo":"iesd-26","prId":"17905"}'
   node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" get_bitbucket_pr_comments
   ```

6. **Fetch worklog data** — hours invested per project area

   ```powershell
   # Fetch worklogs for key issues
   $env:CLI_JSON_ARGS = '{"issueKey":"PROJ-101"}'
   node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" fetch_jira_issue
   # Parse the worklog entries from the issue response
   ```

7. **Synthesize into resume bullets** — analyze all gathered data and produce:

   - **STAR format** (Situation, Task, Action, Result) bullet points
   - **Quantified impact** (e.g., "Resolved 47 issues across 3 sprints, reducing bug backlog by 60%")
   - **Technical depth** (e.g., "Designed and implemented OAuth2 integration — 12 PRs, 3,400+ LOC")
   - **Leadership signals** (e.g., "Led incident response for 3 production outages, authored post-mortem docs")
   - **Collaboration metrics** (e.g., "Reviewed 28 PRs across 4 team members, 150+ review comments")

### Output format

Generate a structured markdown document with:

```markdown
## Work Contribution Summary — [Period]

### Key Achievements (resume-ready bullets)

- Bullet 1 (STAR format, quantified)
- Bullet 2
- ...

### By Project / Epic

| Project | Issues Resolved | PRs Merged | LOC Changed | Key Contributions |
|---|---|---|---|---|
| PROJ-A | 23 | 15 | 4,200 | OAuth2, API redesign |
| PROJ-B | 12 | 8 | 1,800 | Performance optimization |

### Technical Skills Demonstrated

- Languages/frameworks used (from PR file extensions)
- Architecture patterns (from issue descriptions and PR content)
- Tools and infrastructure (from labels, components)

### Collaboration & Leadership

- PRs reviewed: N
- Comments authored: N
- Issues triaged: N
- Documentation pages created: N
```

### Caveats

- `issueFunction` JQL requires ScriptRunner — fall back to project-scoped search if unavailable
- Worklog data may be incomplete if the team does not consistently log time
- PR contribution counts may span multiple repos — run `summarize_bitbucket_contributions`
  per repo and aggregate
- For confidential projects, sanitize company-specific details before adding to a public resume

---

## 11. Bitbucket ↔ GitHub Cross-Platform Code Migration

Move or mirror code between Bitbucket Server/Data Center and GitHub repositories.
Combines the Atlassian CLI (for Bitbucket metadata) with `git` and `gh` CLI (for GitHub operations).

### Prerequisites

- `git` installed and configured
- `gh` CLI installed and authenticated (`gh auth login`)
- Bitbucket PAT configured in `.env`
- GitHub PAT or SSH key configured for `git push`

### Scenario A — Mirror a Bitbucket repo to GitHub (full history)

#### Steps

1. **Clone the Bitbucket repo** (bare clone preserves all branches and tags)

   ```powershell
   git clone --bare "https://your-bitbucket.example.com/scm/PROJ/repo-name.git" repo-name.git
   cd repo-name.git
   ```

2. **Create a GitHub repo** (using `gh` CLI)

   ```powershell
   gh repo create your-org/repo-name --private --description "Migrated from Bitbucket PROJ/repo-name"
   ```

3. **Push all branches and tags to GitHub**

   ```powershell
   git push --mirror "https://github.com/your-org/repo-name.git"
   ```

4. **Verify** on GitHub

   ```powershell
   gh repo view your-org/repo-name --web
   ```

5. **Migrate open PRs** (metadata only — code is already pushed)

   ```powershell
   # Fetch open PRs from Bitbucket
   $env:CLI_JSON_ARGS = '{"project":"PROJ","repo":"repo-name","state":"OPEN","maxResults":50}'
   $prs = (node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" search_bitbucket_prs) | ConvertFrom-Json

   # For each open PR, create a GitHub PR (if the source branch exists)
   foreach ($pr in $prs.data.values) {
       $fromBranch = $pr.fromRef.displayId
       $toBranch = $pr.toRef.displayId
       $title = $pr.title
       $desc = $pr.description
       gh pr create --repo "your-org/repo-name" --base $toBranch --head $fromBranch --title $title --body $desc
   }
   ```

### Scenario B — Copy specific files from Bitbucket to a GitHub repo

#### Steps

1. **Fetch file content from Bitbucket**

   ```powershell
   $env:CLI_JSON_ARGS = '{"project":"IESD","repo":"iesd-26","filePath":"src/main/java/App.java","branch":"master"}'
   $file = (node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" fetch_bitbucket_file) | ConvertFrom-Json
   $file.data.content | Out-File -Encoding utf8 "<workspace>/temp-atlassian-tools/App.java"
   ```

2. **Clone the target GitHub repo, add the file, commit, push**

   ```powershell
   gh repo clone your-org/target-repo -- --depth 1
   Copy-Item "<workspace>/temp-atlassian-tools/App.java" "target-repo/src/main/java/App.java"
   cd target-repo
   git add src/main/java/App.java
   git commit -m "feat: Migrate App.java from Bitbucket IESD/iesd-26"
   git push origin main
   ```

### Scenario C — GitHub → Bitbucket (reverse direction)

The CLI does not have Bitbucket write operations (create repo, push code). Use `git` directly:

1. **Clone from GitHub**

   ```powershell
   gh repo clone your-org/source-repo
   ```

2. **Add Bitbucket as a remote and push**

   ```powershell
   cd source-repo
   git remote add bitbucket "https://your-bitbucket.example.com/scm/PROJ/target-repo.git"
   git push bitbucket --all
   git push bitbucket --tags
   ```

   > The Bitbucket repo must already exist. Create it via Bitbucket UI or REST API.

### Scenario D — Selective branch migration

```powershell
# Clone source (Bitbucket)
git clone "https://your-bitbucket.example.com/scm/PROJ/repo.git" repo-migrate
cd repo-migrate

# Add GitHub remote
git remote add github "https://github.com/your-org/repo.git"

# Push only specific branches
git push github feature/auth-module
git push github release/v2.0
git push github main
```

### Scenario E — PR history export (Bitbucket → markdown archive)

Export all merged PRs as a markdown archive for reference after migration:

```powershell
$env:CLI_JSON_ARGS = '{"project":"PROJ","repo":"repo-name","state":"MERGED","maxResults":100}'
$prs = (node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" search_bitbucket_prs) | ConvertFrom-Json

$archive = "# PR Archive — PROJ/repo-name`n`n"
$archive += "| PR | Title | Author | Merged | Branch |`n|---|---|---|---|---|`n"

foreach ($pr in $prs.data.values) {
    $archive += "| #$($pr.id) | $($pr.title) | $($pr.author.displayName) | $($pr.closedDate) | $($pr.fromRef.displayId) → $($pr.toRef.displayId) |`n"
}

$archive | Out-File -Encoding utf8 "<workspace>/temp-atlassian-tools/pr-archive.md"
```

### Caveats

- **Bitbucket Server LFS**: if the repo uses Git LFS, install `git-lfs` and run `git lfs fetch --all`
  before mirroring
- **PR comments and tasks**: these live in Bitbucket's REST API, not in `git` — export them separately
  using the CLI before decommissioning the source
- **CI/CD pipelines**: Bitbucket Pipelines (`bitbucket-pipelines.yml`) won't work on GitHub —
  convert to GitHub Actions (`.github/workflows/`)
- **Webhooks and integrations**: re-configure on the target platform
- **Access permissions**: set up team access on the new GitHub repo separately
- **Large repos**: for repos > 1 GB, use `git clone --bare` and push incrementally by branch

---

## 12. Confluence Content Operations (Advanced)

Detailed patterns for common Confluence content manipulation tasks beyond basic CRUD.

### Fetch and display page content

```powershell
$env:CLI_JSON_ARGS = '{"pageId":"12345"}'
$page = (node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" fetch_confluence_page) | ConvertFrom-Json

# Display metadata
Write-Host "Title: $($page.data.title)"
Write-Host "Version: $($page.data.version.number)"
Write-Host "URL: $($page.data._links.base)$($page.data._links.webui)"

# Save HTML content for inspection or editing
$page.data.body.storage.value | Out-File -Encoding utf8 "<workspace>/temp-atlassian-tools/page_content.html"
```

### Append content to an existing page (without overwriting)

```powershell
$env:CLI_CONTENT = '<h2>New Section</h2><p>This content is appended to the existing page.</p>'
$env:CLI_JSON_ARGS = '{"pageId":"12345","contentFromEnv":true}'
node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" append_to_confluence_page
```

### Clone a page tree within the same space

```powershell
# Get the source tree
$env:CLI_JSON_ARGS = '{"pageId":"12345","depth":2}'
$tree = (node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" get_confluence_page_tree) | ConvertFrom-Json

# Copy each page maintaining hierarchy (use same-instance copy_confluence_page)
foreach ($child in $tree.data.children) {
    $env:CLI_JSON_ARGS = "{`"pageId`":`"$($child.id)`",`"newTitle`":`"COPY: $($child.title)`",`"targetParentId`":`"67890`"}"
    node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" copy_confluence_page
}
```

### Search and bulk-update pages by label

```powershell
# Find all pages with a label
$env:CLI_JSON_ARGS = '{"label":"needs-review","spaceKey":"ENG","maxResults":50}'
$pages = (node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" search_confluence_by_label) | ConvertFrom-Json

# Append a review banner to each page
foreach ($page in $pages.data.results) {
    $env:CLI_CONTENT = '<ac:structured-macro ac:name="warning"><ac:rich-text-body><p>This page is under review. Content may change.</p></ac:rich-text-body></ac:structured-macro>'
    $env:CLI_JSON_ARGS = "{`"pageId`":`"$($page.id)`",`"contentFromEnv`":true}"
    node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" append_to_confluence_page
}
```

### Export page to PDF

```powershell
$env:CLI_JSON_ARGS = '{"pageId":"12345"}'
$result = (node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" export_confluence_page_pdf) | ConvertFrom-Json
# Open the PDF URL in a browser (requires authenticated session)
Write-Host "PDF URL: $($result.data.url)"
```

### Page version comparison and rollback

```powershell
# Get version history
$env:CLI_JSON_ARGS = '{"pageId":"12345","maxResults":10}'
node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" get_confluence_page_versions

# Restore to a specific version
$env:CLI_JSON_ARGS = '{"pageId":"12345","versionNumber":5}'
node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" restore_confluence_page_version
```

---

## 13. Jira Deep Analysis Workflows

Advanced Jira query and analysis patterns for project intelligence and reporting.

### Sprint velocity analysis

```powershell
# Get all sprints for a board
$env:CLI_JSON_ARGS = '{"boardId":"42","state":"closed"}'
$sprints = (node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" get_jira_sprints) | ConvertFrom-Json

# For each recent sprint, count completed issues
foreach ($sprint in $sprints.data.values | Select-Object -Last 5) {
    $env:CLI_JSON_ARGS = "{`"jql`":`"sprint = $($sprint.id) AND status = Done`",`"maxResults`":1}"
    $result = (node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" search_jira_issues) | ConvertFrom-Json
    Write-Host "Sprint $($sprint.name): $($result.data.total) completed"
}
```

### Component health check — find neglected areas

```powershell
# Issues per component, unresolved, oldest first
$env:CLI_JSON_ARGS = '{"projectKey":"PROJ"}'
$components = (node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" get_jira_components) | ConvertFrom-Json

foreach ($comp in $components.data) {
    $env:CLI_JSON_ARGS = "{`"jql`":`"project = PROJ AND component = \`"$($comp.name)\`" AND status != Done ORDER BY created ASC`",`"maxResults`":1}"
    $result = (node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" search_jira_issues) | ConvertFrom-Json
    Write-Host "$($comp.name): $($result.data.total) open issues"
}
```

### Issue dependency mapping

```powershell
# Get all links for an issue and traverse
$env:CLI_JSON_ARGS = '{"issueKey":"PROJ-100"}'
$links = (node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" get_jira_issue_links) | ConvertFrom-Json

# For each linked issue, fetch its status
foreach ($link in $links.data) {
    $linkedKey = if ($link.outwardIssue) { $link.outwardIssue.key } else { $link.inwardIssue.key }
    $env:CLI_JSON_ARGS = "{`"issueKey`":`"$linkedKey`",`"fields`":`"summary,status`"}"
    node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" fetch_jira_issue
}
```
