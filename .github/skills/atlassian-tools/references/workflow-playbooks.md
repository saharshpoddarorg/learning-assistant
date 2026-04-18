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
