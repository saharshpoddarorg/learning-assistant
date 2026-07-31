#!/usr/bin/env node
/**
 * jira-cross-account.js — Cross-account Jira operations.
 *
 * Usage:
 *   node jira-cross-account.js <action>
 *
 * Arguments via $env:CLI_JSON_ARGS (JSON). Every action requires:
 *   sourceAccount  — profile ID to read from  (e.g. "work.primary")
 *   targetAccount  — profile ID to write to   (e.g. "personal-work.primary")
 *
 * Result printed to stdout as JSON:
 *   { "success": true,  "data": {...} }
 *   { "success": false, "error": "..." }
 */

import { loadProfileCredentials } from './account-manager.js';
import { RestClient, enc } from './rest-client.js';

// ═══════════════════════════════════════════════════════════════════════════════
// Helpers
// ═══════════════════════════════════════════════════════════════════════════════

function mapIssueFields(fields) {
  const mapped = {
    project:   { key: fields.project?.key },
    issuetype: { name: fields.issuetype?.name || 'Task' },
    summary:   fields.summary,
  };
  if (fields.description)    mapped.description = fields.description;
  if (fields.priority?.name) mapped.priority    = { name: fields.priority.name };
  if (fields.assignee?.name) mapped.assignee    = { name: fields.assignee.name };
  if (fields.labels?.length) mapped.labels      = fields.labels;
  if (fields.parent?.key)    mapped.parent      = { key: fields.parent.key };
  return mapped;
}

// ═══════════════════════════════════════════════════════════════════════════════
// Action Handlers
// ═══════════════════════════════════════════════════════════════════════════════

/**
 * copy_issue — Fetch issue from source account, create equivalent in target account.
 * The target project key defaults to the source project key; override with targetProjectKey.
 */
async function copyIssue(sourceClient, targetClient, args) {
  const fetchFields = 'summary,issuetype,project,description,priority,assignee,labels,parent';
  const source = await sourceClient.get(`/rest/api/2/issue/${enc(args.issueKey)}?fields=${fetchFields}`);

  const fields = mapIssueFields(source.fields);
  if (args.targetProjectKey) fields.project = { key: args.targetProjectKey };

  const created = await targetClient.post('/rest/api/2/issue', { fields });
  return {
    sourceKey:    args.issueKey,
    sourceAccount: args.sourceAccount,
    targetKey:    created.key,
    targetAccount: args.targetAccount,
    summary:      source.fields.summary,
  };
}

/**
 * copy_issues_by_jql — Copy all issues matching a JQL query from source to target account.
 */
async function copyIssuesByJql(sourceClient, targetClient, args) {
  const fetchFields = 'summary,issuetype,project,description,priority,assignee,labels,parent';
  const max = args.maxResults || 20;
  const search = await sourceClient.get(
    `/rest/api/2/search?jql=${encodeURIComponent(args.jql)}&maxResults=${max}&fields=${fetchFields}`
  );
  const results = [];
  for (const issue of search.issues || []) {
    try {
      const fields = mapIssueFields(issue.fields);
      if (args.targetProjectKey) fields.project = { key: args.targetProjectKey };
      const created = await targetClient.post('/rest/api/2/issue', { fields });
      results.push({ success: true, sourceKey: issue.key, targetKey: created.key, summary: issue.fields.summary });
    } catch (e) {
      results.push({ success: false, sourceKey: issue.key, summary: issue.fields?.summary, error: e.message });
    }
  }
  return {
    total: search.total,
    copied: results.filter(r => r.success).length,
    failed: results.filter(r => !r.success).length,
    results,
  };
}

// ═══════════════════════════════════════════════════════════════════════════════
// Action Registry
// ═══════════════════════════════════════════════════════════════════════════════

const REQUIRED_ARGS = {
  copy_issue:          ['sourceAccount', 'targetAccount', 'issueKey'],
  copy_issues_by_jql:  ['sourceAccount', 'targetAccount', 'jql'],
};

const ACTION_ROUTER = {
  copy_issue:          (sc, tc, a) => copyIssue(sc, tc, a),
  copy_issues_by_jql:  (sc, tc, a) => copyIssuesByJql(sc, tc, a),
};

// ═══════════════════════════════════════════════════════════════════════════════
// Main
// ═══════════════════════════════════════════════════════════════════════════════

async function main() {
  const action  = process.argv[2];
  const rawArgs = process.argv[3] || process.env.CLI_JSON_ARGS || '{}';
  delete process.env.CLI_JSON_ARGS;

  if (!action) {
    console.log(JSON.stringify({
      success: false,
      error: 'Usage: node jira-cross-account.js <action>\nSet $env:CLI_JSON_ARGS with JSON arguments.',
      available_actions: Object.keys(ACTION_ROUTER),
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

  let args;
  try {
    args = JSON.parse(rawArgs);
  } catch (e) {
    console.log(JSON.stringify({ success: false, error: `Invalid JSON arguments: ${e.message}` }));
    process.exit(1);
  }

  const required = REQUIRED_ARGS[action];
  if (required) {
    const missing = required.filter(k => !args[k] && args[k] !== 0);
    if (missing.length) {
      console.log(JSON.stringify({
        success: false,
        error: `Missing required argument(s) for "${action}": ${missing.join(', ')}`,
      }));
      process.exit(1);
    }
  }

  let sourceCreds, targetCreds;
  try {
    sourceCreds = loadProfileCredentials(args.sourceAccount);
    targetCreds = loadProfileCredentials(args.targetAccount);
  } catch (e) {
    console.log(JSON.stringify({ success: false, error: e.message }));
    process.exit(1);
  }

  for (const [label, creds] of [['source', sourceCreds], ['target', targetCreds]]) {
    if (!creds.jiraToken) {
      console.log(JSON.stringify({ success: false, error: `Missing JIRA_PAT_TOKEN in .env.${creds.profileId} (${label})` }));
      process.exit(1);
    }
    if (!creds.jiraUrl) {
      console.log(JSON.stringify({ success: false, error: `Missing JIRA_BASE_URL in .env.${creds.profileId} (${label})` }));
      process.exit(1);
    }
  }

  const sourceClient = new RestClient(sourceCreds.jiraUrl, sourceCreds.jiraToken);
  const targetClient = new RestClient(targetCreds.jiraUrl, targetCreds.jiraToken);

  try {
    const result = await ACTION_ROUTER[action](sourceClient, targetClient, args);
    console.log(JSON.stringify({ success: true, data: result }));
  } catch (e) {
    console.log(JSON.stringify({ success: false, error: e.message || String(e) }));
    process.exit(1);
  }
}

main();
