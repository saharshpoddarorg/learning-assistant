#!/usr/bin/env node
/**
 * Jira CLI - PAT-authenticated REST API client.
 *
 * Usage:
 *   node scripts/jira_cli.js <action>
 *
 * Arguments are read from:
 *   1. $env:CLI_JSON_ARGS (recommended - avoids PowerShell quoting bugs)
 *   2. process.argv[3] (fallback)
 *
 * Authentication:
 *   Reads PAT tokens from a .env file found by walking up from the workspace root.
 *   Required variables: JIRA_PAT_TOKEN, JIRA_BASE_URL
 *
 * Dependencies: Node.js 18+ (uses built-in fetch). No npm install needed.
 *
 * Result is printed to stdout as JSON:
 *   { "success": true, "data": {...} }
 *   { "success": false, "error": "..." }
 */

import { fileURLToPath } from 'url';
import { dirname, resolve } from 'path';
import { loadProfileCredentials } from '../../atlassian-common/account-manager.js';
import { RestClient, enc } from '../../atlassian-common/rest-client.js';

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

// TLS config and RestClient are provided by rest-client.js

function normalizeJql(jql) {
  if (typeof jql !== 'string') return jql;
  return jql.replace(
    /\b(assignee|reporter)\s*(=|!=|~|!~)\s*([^\s()"']+@[^\s()"']+)/gi,
    (_m, field, op, val) => `${field} ${op} "${val}"`
  );
}

// ═══════════════════════════════════════════════════════════════════════════════
// Jira Action Handlers
// ═══════════════════════════════════════════════════════════════════════════════

async function fetchJiraIssue(client, args) {
  const fields = args.fields || 'summary,status,assignee,reporter,priority,issuetype,created,updated,description,comment,parent,issuelinks,subtasks,labels,components,fixVersions';
  return await client.get(`/rest/api/2/issue/${enc(args.issueKey)}?fields=${fields}`);
}

async function fetchJiraComments(client, args) {
  const max = args.maxResults || 25;
  return await client.get(`/rest/api/2/issue/${enc(args.issueKey)}/comment?maxResults=${max}&orderBy=-created`);
}

async function searchJiraIssues(client, args) {
  const jql = normalizeJql(args.jql);
  const max = args.maxResults || 25;
  const startAt = args.startAt || 0;
  const fields = args.fields || 'summary,status,assignee,priority,issuetype,created,updated,reporter,labels';
  return await client.get(`/rest/api/2/search?jql=${encodeURIComponent(jql)}&maxResults=${max}&startAt=${startAt}&fields=${fields}`);
}

async function listJiraIssuesByProject(client, args) {
  const jql = `project = ${args.projectKey} ORDER BY updated DESC`;
  const max = args.maxResults || 25;
  const startAt = args.startAt || 0;
  return await client.get(`/rest/api/2/search?jql=${encodeURIComponent(jql)}&maxResults=${max}&startAt=${startAt}&fields=summary,status,assignee,priority,issuetype`);
}

async function createJiraIssue(client, args) {
  const payload = {
    fields: {
      project: { key: args.projectKey },
      issuetype: { name: args.issueType },
      summary: args.summary,
    },
  };
  if (args.description) payload.fields.description = args.description;
  if (args.priority) payload.fields.priority = { name: args.priority };
  if (args.assignee) payload.fields.assignee = { name: args.assignee };
  if (args.labels) payload.fields.labels = args.labels;
  if (args.components) payload.fields.components = args.components.map(c => ({ name: c }));
  if (args.parentKey) payload.fields.parent = { key: args.parentKey };
  return await client.post('/rest/api/2/issue', payload);
}

async function updateJiraIssue(client, args) {
  const fields = {};
  if (args.summary) fields.summary = args.summary;
  if (args.description) fields.description = args.description;
  if (args.priority) fields.priority = { name: args.priority };
  if (args.assignee) fields.assignee = { name: args.assignee };
  if (args.labels) fields.labels = args.labels;
  return await client.put(`/rest/api/2/issue/${enc(args.issueKey)}`, { fields });
}

async function transitionJiraIssue(client, args) {
  const transitions = await client.get(`/rest/api/2/issue/${enc(args.issueKey)}/transitions`);
  const match = (transitions.transitions || []).find(t =>
    t.name.toLowerCase() === args.transition.toLowerCase()
  );
  if (!match) {
    const available = (transitions.transitions || []).map(t => t.name).join(', ');
    throw new Error(`Transition "${args.transition}" not found. Available: ${available}`);
  }
  return await client.post(`/rest/api/2/issue/${enc(args.issueKey)}/transitions`, {
    transition: { id: match.id },
  });
}

async function addJiraComment(client, args) {
  return await client.post(`/rest/api/2/issue/${enc(args.issueKey)}/comment`, { body: args.comment });
}

async function addJiraWorklog(client, args) {
  const payload = { timeSpent: args.timeSpent };
  if (args.comment) payload.comment = args.comment;
  if (args.started) payload.started = args.started;
  return await client.post(`/rest/api/2/issue/${enc(args.issueKey)}/worklog`, payload);
}

async function deleteJiraIssue(client, args) {
  return await client.del(`/rest/api/2/issue/${enc(args.issueKey)}`);
}

async function linkJiraIssues(client, args) {
  return await client.post('/rest/api/2/issueLink', {
    type: { name: args.linkType || 'Relates' },
    inwardIssue: { key: args.inwardIssueKey },
    outwardIssue: { key: args.outwardIssueKey },
  });
}

async function getJiraIssueLinks(client, args) {
  const issue = await client.get(`/rest/api/2/issue/${enc(args.issueKey)}?fields=issuelinks`);
  return { issuelinks: issue.fields?.issuelinks || [] };
}

async function getJiraSubtasks(client, args) {
  const jql = `parent = ${args.issueKey} ORDER BY created ASC`;
  return await client.get(`/rest/api/2/search?jql=${encodeURIComponent(jql)}&maxResults=${args.maxResults || 50}&fields=summary,status,assignee,issuetype,priority`);
}

async function cloneJiraIssue(client, args) {
  const source = await client.get(`/rest/api/2/issue/${enc(args.issueKey)}?fields=summary,description,issuetype,priority,labels,components,assignee,project`);
  const f = source.fields;
  const payload = {
    fields: {
      project: { key: f.project?.key },
      issuetype: { name: f.issuetype?.name },
      summary: args.summary || `[Clone] ${f.summary}`,
      description: f.description || '',
    },
  };
  if (f.priority) payload.fields.priority = { name: f.priority.name };
  if (f.labels?.length) payload.fields.labels = f.labels;
  if (f.components?.length) payload.fields.components = f.components.map(c => ({ name: c.name }));
  if (f.assignee) payload.fields.assignee = { name: f.assignee.name };
  return await client.post('/rest/api/2/issue', payload);
}

async function bulkCreateJiraIssues(client, args) {
  const results = [];
  for (const issue of args.issues) {
    try {
      const payload = {
        fields: {
          project: { key: issue.projectKey },
          issuetype: { name: issue.issueType },
          summary: issue.summary,
        },
      };
      if (issue.description) payload.fields.description = issue.description;
      if (issue.priority) payload.fields.priority = { name: issue.priority };
      if (issue.assignee) payload.fields.assignee = { name: issue.assignee };
      if (issue.labels) payload.fields.labels = issue.labels;
      if (issue.parentKey) payload.fields.parent = { key: issue.parentKey };
      const r = await client.post('/rest/api/2/issue', payload);
      results.push({ success: true, key: r.key, id: r.id, summary: issue.summary });
    } catch (e) {
      results.push({ success: false, summary: issue.summary, error: e.message });
    }
  }
  return { created: results.filter(r => r.success).length, failed: results.filter(r => !r.success).length, results };
}

async function bulkTransitionJiraIssues(client, args) {
  const results = [];
  for (const key of args.issueKeys) {
    try {
      const tData = await client.get(`/rest/api/2/issue/${enc(key)}/transitions`);
      const match = (tData.transitions || []).find(t => t.name.toLowerCase() === args.transition.toLowerCase());
      if (!match) {
        results.push({ success: false, key, error: `Transition "${args.transition}" not available` });
        continue;
      }
      await client.post(`/rest/api/2/issue/${enc(key)}/transitions`, { transition: { id: match.id } });
      results.push({ success: true, key, transition: args.transition });
    } catch (e) {
      results.push({ success: false, key, error: e.message });
    }
  }
  return { transitioned: results.filter(r => r.success).length, failed: results.filter(r => !r.success).length, results };
}

async function addJiraLabels(client, args) {
  return await client.put(`/rest/api/2/issue/${enc(args.issueKey)}`, {
    update: { labels: args.labels.map(l => ({ add: l })) },
  });
}

async function removeJiraLabels(client, args) {
  return await client.put(`/rest/api/2/issue/${enc(args.issueKey)}`, {
    update: { labels: args.labels.map(l => ({ remove: l })) },
  });
}

async function getJiraWatchers(client, args) {
  return await client.get(`/rest/api/2/issue/${enc(args.issueKey)}/watchers`);
}

async function addJiraWatcher(client, args) {
  const url = `${client.baseUrl}/rest/api/2/issue/${enc(args.issueKey)}/watchers`;
  console.error(`POST ${url}`);
  const resp = await fetch(url, {
    method: 'POST',
    headers: { ...client._headers('application/json') },
    body: JSON.stringify(args.username),
  });
  if (!resp.ok) throw new Error(`HTTP ${resp.status}: ${await resp.text()}`);
  return { added: args.username };
}

async function removeJiraWatcher(client, args) {
  return await client.del(`/rest/api/2/issue/${enc(args.issueKey)}/watchers?username=${enc(args.username)}`);
}

async function getJiraIssueChangelog(client, args) {
  const max = args.maxResults || 50;
  return await client.get(`/rest/api/2/issue/${enc(args.issueKey)}?expand=changelog&fields=summary&maxResults=${max}`);
}

async function getJiraIssueTypes(client, args) {
  if (args.projectKey) {
    return await client.get(`/rest/api/2/project/${enc(args.projectKey)}`).then(p => ({ issueTypes: p.issueTypes }));
  }
  return await client.get('/rest/api/2/issuetype');
}

async function getJiraStatuses(client, args) {
  if (args.projectKey) {
    return await client.get(`/rest/api/2/project/${enc(args.projectKey)}/statuses`);
  }
  return await client.get('/rest/api/2/status');
}

async function getJiraComponents(client, args) {
  return await client.get(`/rest/api/2/project/${enc(args.projectKey)}/components`);
}

async function getJiraVersions(client, args) {
  return await client.get(`/rest/api/2/project/${enc(args.projectKey)}/versions`);
}

async function searchJiraUsers(client, args) {
  const max = args.maxResults || 20;
  return await client.get(`/rest/api/2/user/search?username=${enc(args.query)}&maxResults=${max}`);
}

async function getCurrentJiraUser(client, _args) {
  return await client.get('/rest/api/2/myself');
}

async function getEpicIssues(client, args) {
  const jql = `"Epic Link" = ${args.epicKey} ORDER BY rank ASC`;
  return await client.get(`/rest/api/2/search?jql=${encodeURIComponent(jql)}&maxResults=${args.maxResults || 50}&fields=summary,status,assignee,issuetype,priority`);
}

async function addIssuesToEpic(client, args) {
  const results = [];
  for (const key of args.issueKeys) {
    try {
      await client.put(`/rest/api/2/issue/${enc(key)}`, { fields: { customfield_10008: args.epicKey } });
      results.push({ success: true, key });
    } catch (e) {
      try {
        await client.put(`/rest/api/2/issue/${enc(key)}`, { fields: { 'Epic Link': args.epicKey } });
        results.push({ success: true, key });
      } catch (e2) {
        results.push({ success: false, key, error: e2.message });
      }
    }
  }
  return { results };
}

async function getJiraSprints(client, args) {
  return await client.get(`/rest/agile/1.0/board/${args.boardId}/sprint?state=${args.state || 'active,future'}`);
}

async function getSprintIssues(client, args) {
  const max = args.maxResults || 50;
  return await client.get(`/rest/agile/1.0/sprint/${args.sprintId}/issue?maxResults=${max}&fields=summary,status,assignee,issuetype,priority`);
}

async function moveToSprint(client, args) {
  return await client.post(`/rest/agile/1.0/sprint/${args.sprintId}/issue`, {
    issues: args.issueKeys,
  });
}

async function moveToBacklog(client, args) {
  return await client.post('/rest/agile/1.0/backlog/issue', {
    issues: args.issueKeys,
  });
}

// ═══════════════════════════════════════════════════════════════════════════════
// Action Aliases - silently fixes common LLM hallucinations
// ═══════════════════════════════════════════════════════════════════════════════

const ACTION_ALIASES = {
  "search_jira":                      "search_jira_issues",
  "jira_search":                      "search_jira_issues",
  "get_jira_issue":                   "fetch_jira_issue",
  "comment_jira":                     "add_jira_comment",
  "log_work":                         "add_jira_worklog",
  "jira_worklog":                     "add_jira_worklog",
  "add_worklog":                      "add_jira_worklog",
  "add_work_log":                     "add_jira_worklog",
  "jira_work_log":                    "add_jira_worklog",
  "log_jira_work":                    "add_jira_worklog",
  "create_jira_subtask":              "create_jira_issue",
  "get_subtasks":                     "get_jira_subtasks",
  "jira_subtasks":                    "get_jira_subtasks",
  "clone_issue":                      "clone_jira_issue",
  "link_issues":                      "link_jira_issues",
  "get_issue_links":                  "get_jira_issue_links",
  "jira_changelog":                   "get_jira_issue_changelog",
  "issue_changelog":                  "get_jira_issue_changelog",
  "jira_issue_types":                 "get_jira_issue_types",
  "issue_types":                      "get_jira_issue_types",
  "jira_statuses":                    "get_jira_statuses",
  "jira_components":                  "get_jira_components",
  "jira_versions":                    "get_jira_versions",
  "find_jira_user":                   "search_jira_users",
  "jira_users":                       "search_jira_users",
  "jira_myself":                      "get_current_jira_user",
  "current_user":                     "get_current_jira_user",
  "epic_issues":                      "get_epic_issues",
  "jira_sprints":                     "get_jira_sprints",
  "sprint_issues":                    "get_sprint_issues",
};

// ═══════════════════════════════════════════════════════════════════════════════
// Required Arguments Validation
// ═══════════════════════════════════════════════════════════════════════════════

const REQUIRED_ARGS = {
  fetch_jira_issue:               ['issueKey'],
  fetch_jira_comments:            ['issueKey'],
  search_jira_issues:             ['jql'],
  list_jira_issues_by_project:    ['projectKey'],
  create_jira_issue:              ['projectKey', 'issueType', 'summary'],
  update_jira_issue:              ['issueKey'],
  transition_jira_issue:          ['issueKey', 'transition'],
  add_jira_comment:               ['issueKey', 'comment'],
  add_jira_worklog:               ['issueKey', 'timeSpent'],
  delete_jira_issue:              ['issueKey'],
  link_jira_issues:               ['inwardIssueKey', 'outwardIssueKey'],
  get_jira_issue_links:           ['issueKey'],
  get_jira_subtasks:              ['issueKey'],
  clone_jira_issue:               ['issueKey'],
  bulk_create_jira_issues:        ['issues'],
  bulk_transition_jira_issues:    ['issueKeys', 'transition'],
  add_jira_labels:                ['issueKey', 'labels'],
  remove_jira_labels:             ['issueKey', 'labels'],
  get_jira_watchers:              ['issueKey'],
  add_jira_watcher:               ['issueKey', 'username'],
  remove_jira_watcher:            ['issueKey', 'username'],
  get_jira_issue_changelog:       ['issueKey'],
  get_jira_issue_types:           [],
  get_jira_statuses:              [],
  get_jira_components:            ['projectKey'],
  get_jira_versions:              ['projectKey'],
  search_jira_users:              ['query'],
  get_current_jira_user:          [],
  get_epic_issues:                ['epicKey'],
  add_issues_to_epic:             ['epicKey', 'issueKeys'],
  get_jira_sprints:               ['boardId'],
  get_sprint_issues:              ['sprintId'],
  move_to_sprint:                 ['sprintId', 'issueKeys'],
  move_to_backlog:                ['issueKeys'],
};

// ═══════════════════════════════════════════════════════════════════════════════
// Action Router
// ═══════════════════════════════════════════════════════════════════════════════

const ACTION_ROUTER = {
  fetch_jira_issue:               (c, a) => fetchJiraIssue(c, a),
  fetch_jira_comments:            (c, a) => fetchJiraComments(c, a),
  search_jira_issues:             (c, a) => searchJiraIssues(c, a),
  list_jira_issues_by_project:    (c, a) => listJiraIssuesByProject(c, a),
  create_jira_issue:              (c, a) => createJiraIssue(c, a),
  update_jira_issue:              (c, a) => updateJiraIssue(c, a),
  transition_jira_issue:          (c, a) => transitionJiraIssue(c, a),
  add_jira_comment:               (c, a) => addJiraComment(c, a),
  add_jira_worklog:               (c, a) => addJiraWorklog(c, a),
  delete_jira_issue:              (c, a) => deleteJiraIssue(c, a),
  link_jira_issues:               (c, a) => linkJiraIssues(c, a),
  get_jira_issue_links:           (c, a) => getJiraIssueLinks(c, a),
  get_jira_subtasks:              (c, a) => getJiraSubtasks(c, a),
  clone_jira_issue:               (c, a) => cloneJiraIssue(c, a),
  bulk_create_jira_issues:        (c, a) => bulkCreateJiraIssues(c, a),
  bulk_transition_jira_issues:    (c, a) => bulkTransitionJiraIssues(c, a),
  add_jira_labels:                (c, a) => addJiraLabels(c, a),
  remove_jira_labels:             (c, a) => removeJiraLabels(c, a),
  get_jira_watchers:              (c, a) => getJiraWatchers(c, a),
  add_jira_watcher:               (c, a) => addJiraWatcher(c, a),
  remove_jira_watcher:            (c, a) => removeJiraWatcher(c, a),
  get_jira_issue_changelog:       (c, a) => getJiraIssueChangelog(c, a),
  get_jira_issue_types:           (c, a) => getJiraIssueTypes(c, a),
  get_jira_statuses:              (c, a) => getJiraStatuses(c, a),
  get_jira_components:            (c, a) => getJiraComponents(c, a),
  get_jira_versions:              (c, a) => getJiraVersions(c, a),
  search_jira_users:              (c, a) => searchJiraUsers(c, a),
  get_current_jira_user:          (c, a) => getCurrentJiraUser(c, a),
  get_epic_issues:                (c, a) => getEpicIssues(c, a),
  add_issues_to_epic:             (c, a) => addIssuesToEpic(c, a),
  get_jira_sprints:               (c, a) => getJiraSprints(c, a),
  get_sprint_issues:              (c, a) => getSprintIssues(c, a),
  move_to_sprint:                 (c, a) => moveToSprint(c, a),
  move_to_backlog:                (c, a) => moveToBacklog(c, a),
};

// ═══════════════════════════════════════════════════════════════════════════════
// Main
// ═══════════════════════════════════════════════════════════════════════════════

async function main() {
  let action = process.argv[2];
  const rawArgs = process.argv[3] || process.env.CLI_JSON_ARGS || '{}';
  delete process.env.CLI_JSON_ARGS;

  if (action && ACTION_ALIASES[action]) {
    action = ACTION_ALIASES[action];
  }

  if (!action) {
    console.log(JSON.stringify({
      success: false,
      error: 'Usage: node jira_cli.js <action>\nSet $env:CLI_JSON_ARGS with JSON arguments.',
      available_actions: Object.keys(ACTION_ROUTER),
    }));
    process.exit(1);
  }

  let args;
  try {
    args = JSON.parse(rawArgs);
  } catch (e) {
    console.log(JSON.stringify({
      success: false,
      error: `Invalid JSON arguments: ${e.message}`,
    }));
    process.exit(1);
  }

  if (!ACTION_ROUTER[action]) {
    console.log(JSON.stringify({
      success: false,
      error: `Unknown action: "${action}". Available: ${Object.keys(ACTION_ROUTER).join(', ')}`,
    }));
    process.exit(1);
  }

  const required = REQUIRED_ARGS[action];
  if (required) {
    const missing = required.filter(k => !args[k] && args[k] !== 0);
    if (missing.length > 0) {
      console.log(JSON.stringify({
        success: false,
        error: `Missing required argument(s) for "${action}": ${missing.join(', ')}\nRequired: ${JSON.stringify(required)}\nProvided: ${JSON.stringify(Object.keys(args))}`,
      }));
      process.exit(1);
    }
  }

  // args.account selects the profile; falls back to active/default profile
  let creds;
  try {
    creds = loadProfileCredentials(args.account || null);
  } catch (e) {
    console.log(JSON.stringify({ success: false, error: e.message }));
    process.exit(1);
  }
  if (!creds.jiraToken) {
    console.log(JSON.stringify({ success: false, error: `Missing JIRA_PAT_TOKEN in .env.${creds.profileId}` }));
    process.exit(1);
  }
  if (!creds.jiraUrl) {
    console.log(JSON.stringify({ success: false, error: `Missing JIRA_BASE_URL in .env.${creds.profileId}` }));
    process.exit(1);
  }

  const client = new RestClient(creds.jiraUrl, creds.jiraToken);

  try {
    const result = await ACTION_ROUTER[action](client, args);
    console.log(JSON.stringify({ success: true, data: result }));
  } catch (e) {
    console.log(JSON.stringify({ success: false, error: e.message || String(e) }));
    process.exit(1);
  }
}

main();
