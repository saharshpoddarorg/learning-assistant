#!/usr/bin/env node
/**
 * Bitbucket Server CLI - PAT-authenticated REST API client.
 *
 * Usage:
 *   node scripts/bitbucket_cli.js <action>
 *
 * Arguments are read from:
 *   1. $env:CLI_JSON_ARGS (recommended - avoids PowerShell quoting bugs)
 *   2. process.argv[3] (fallback)
 *
 * Authentication:
 *   Reads PAT tokens from a .env file found by walking up from the workspace root.
 *   Required variables: BITBUCKET_PAT_TOKEN, BITBUCKET_BASE_URL
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
import { BitbucketRestClient as RestClient, enc } from '../../atlassian-common/rest-client.js';

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

// TLS config, RestClient (with getPaginated), and enc are provided by rest-client.js

// ═══════════════════════════════════════════════════════════════════════════════
// Bitbucket Action Handlers
// ═══════════════════════════════════════════════════════════════════════════════

async function fetchBitbucketPR(client, args) {
  const path = `/rest/api/1.0/projects/${enc(args.project)}/repos/${enc(args.repo)}/pull-requests/${args.prId}`;
  return await client.get(path);
}

async function fetchBitbucketPRFiles(client, args) {
  const path = `/rest/api/1.0/projects/${enc(args.project)}/repos/${enc(args.repo)}/pull-requests/${args.prId}/changes`;
  return await client.getPaginated(path);
}

async function fetchBitbucketPRDiff(client, args) {
  const ctx = args.contextLines || 5;
  let path = `/rest/api/1.0/projects/${enc(args.project)}/repos/${enc(args.repo)}/pull-requests/${args.prId}/diff`;
  if (args.filePath) path += `/${args.filePath}`;
  path += `?contextLines=${ctx}`;
  return await client.get(path);
}

async function fetchBitbucketPRActivities(client, args) {
  const path = `/rest/api/1.0/projects/${enc(args.project)}/repos/${enc(args.repo)}/pull-requests/${args.prId}/activities`;
  return await client.getPaginated(path);
}

async function searchBitbucketPRs(client, args) {
  let path = `/rest/api/1.0/projects/${enc(args.project)}/repos/${enc(args.repo)}/pull-requests?state=${args.state || 'ALL'}`;
  if (args.author) path += `&author=${encodeURIComponent(args.author)}`;
  if (args.maxResults) path += `&limit=${args.maxResults}`;
  return await client.getPaginated(path, 3);
}

async function fetchBitbucketFile(client, args) {
  let path = `/rest/api/1.0/projects/${enc(args.project)}/repos/${enc(args.repo)}/browse/${args.filePath}`;
  if (args.branch) path += `?at=${encodeURIComponent(args.branch)}`;
  return await client.get(path);
}

async function summarizeBitbucketContributions(client, args) {
  const months = args.months || 2;
  let path = `/rest/api/1.0/projects/${enc(args.project)}/repos/${enc(args.repo)}/pull-requests?state=ALL&limit=100`;
  const data = await client.getPaginated(path, 3);
  const prs = (data.values || []).filter(pr => {
    const author = pr.author?.user?.displayName || pr.author?.user?.name || '';
    const needle = args.person.toLowerCase();
    return author.toLowerCase().includes(needle);
  });
  const cutoff = Date.now() - (months * 30 * 24 * 60 * 60 * 1000);
  const recent = prs.filter(pr => (pr.createdDate || 0) >= cutoff);
  return {
    person: args.person,
    totalPRs: recent.length,
    merged: recent.filter(pr => pr.state === 'MERGED').length,
    open: recent.filter(pr => pr.state === 'OPEN').length,
    declined: recent.filter(pr => pr.state === 'DECLINED').length,
    prs: recent.map(pr => ({
      id: pr.id,
      title: pr.title,
      state: pr.state,
      created: pr.createdDate ? new Date(pr.createdDate).toISOString().substring(0, 10) : '',
      fromBranch: pr.fromRef?.displayId || '',
      toBranch: pr.toRef?.displayId || '',
    })),
  };
}

async function getBitbucketPRComments(client, args) {
  const path = `/rest/api/1.0/projects/${enc(args.project)}/repos/${enc(args.repo)}/pull-requests/${args.prId}/activities`;
  const data = await client.getPaginated(path);
  const comments = (data.values || [])
    .filter(a => a.action === 'COMMENTED' && a.comment)
    .map(a => a.comment);
  return { values: comments, size: comments.length };
}

async function addBitbucketPRComment(client, args) {
  const basePath = `/rest/api/1.0/projects/${enc(args.project)}/repos/${enc(args.repo)}/pull-requests/${args.prId}/comments`;
  const payload = { text: args.comment };
  if (args.parentId) payload.parent = { id: args.parentId };
  if (args.filePath) {
    payload.anchor = {
      path: args.filePath,
      lineType: args.lineType || 'ADDED',
      line: args.line || 1,
      fileType: args.fileType || 'TO',
    };
  }
  return await client.post(basePath, payload);
}

async function updateBitbucketPRComment(client, args) {
  const path = `/rest/api/1.0/projects/${enc(args.project)}/repos/${enc(args.repo)}/pull-requests/${args.prId}/comments/${args.commentId}`;
  return await client.put(path, { text: args.comment, version: args.version || 0 });
}

async function deleteBitbucketPRComment(client, args) {
  const version = args.version || 0;
  const path = `/rest/api/1.0/projects/${enc(args.project)}/repos/${enc(args.repo)}/pull-requests/${args.prId}/comments/${args.commentId}?version=${version}`;
  return await client.del(path);
}

async function replyBitbucketPRComment(client, args) {
  const path = `/rest/api/1.0/projects/${enc(args.project)}/repos/${enc(args.repo)}/pull-requests/${args.prId}/comments`;
  return await client.post(path, { text: args.comment, parent: { id: args.parentCommentId } });
}

async function createBitbucketTask(client, args) {
  return await client.post('/rest/api/1.0/tasks', {
    anchor: { id: args.commentId, type: 'COMMENT' },
    text: args.text,
  });
}

async function listBitbucketTasks(client, args) {
  const path = `/rest/api/1.0/projects/${enc(args.project)}/repos/${enc(args.repo)}/pull-requests/${args.prId}/tasks`;
  return await client.get(path);
}

async function updateBitbucketTask(client, args) {
  return await client.put(`/rest/api/1.0/tasks/${args.taskId}`, { text: args.text });
}

async function deleteBitbucketTask(client, args) {
  return await client.del(`/rest/api/1.0/tasks/${args.taskId}`);
}

async function resolveBitbucketTask(client, args) {
  return await client.put(`/rest/api/1.0/tasks/${args.taskId}`, { state: 'RESOLVED' });
}

async function reopenBitbucketTask(client, args) {
  return await client.put(`/rest/api/1.0/tasks/${args.taskId}`, { state: 'OPEN' });
}

async function getBitbucketFileDiff(client, args) {
  const ctx = args.contextLines || 5;
  let path = `/rest/api/1.0/projects/${enc(args.project)}/repos/${enc(args.repo)}/diff/${args.filePath}?contextLines=${ctx}`;
  if (args.since) path += `&since=${enc(args.since)}`;
  if (args.until) path += `&until=${enc(args.until)}`;
  return await client.get(path);
}

async function checkFileInBitbucketPR(client, args) {
  const path = `/rest/api/1.0/projects/${enc(args.project)}/repos/${enc(args.repo)}/pull-requests/${args.prId}/changes`;
  const data = await client.getPaginated(path);
  const files = (data.values || []).map(v => v.path?.toString || `${v.path?.parent || ''}/${v.path?.name || ''}`);
  const found = files.some(f => f.includes(args.filePath));
  return { found, filePath: args.filePath, totalChangedFiles: files.length, matchingFiles: files.filter(f => f.includes(args.filePath)) };
}

async function getBitbucketPRFile(client, args) {
  const pr = await client.get(`/rest/api/1.0/projects/${enc(args.project)}/repos/${enc(args.repo)}/pull-requests/${args.prId}`);
  const branch = pr.fromRef?.id || pr.fromRef?.displayId;
  if (!branch) throw new Error('Could not determine PR source branch');
  return await client.get(`/rest/api/1.0/projects/${enc(args.project)}/repos/${enc(args.repo)}/browse/${args.filePath}?at=${encodeURIComponent(branch)}`);
}

// ═══════════════════════════════════════════════════════════════════════════════
// Action Aliases - silently fixes common LLM hallucinations
// ═══════════════════════════════════════════════════════════════════════════════

const ACTION_ALIASES = {
  "bitbucket_pr":                     "fetch_bitbucket_pr",
  "bitbucket_pr_files":               "fetch_bitbucket_pr_files",
  "bitbucket_files":                  "fetch_bitbucket_pr_files",
  "bitbucket_diff":                   "fetch_bitbucket_pr_diff",
  "bitbucket_pr_activities":          "fetch_bitbucket_pr_activities",
  "bitbucket_search":                 "search_bitbucket_prs",
  "search_bitbucket":                 "search_bitbucket_prs",
  "bitbucket_file":                   "fetch_bitbucket_file",
  "fetch_bitbucket":                  "fetch_bitbucket_file",
  "bitbucket_contribution_summary":   "summarize_bitbucket_contributions",
  "summarize_bitbucket_contribution": "summarize_bitbucket_contributions",
  "pr_comments":                      "get_bitbucket_pr_comments",
  "add_pr_comment":                   "add_bitbucket_pr_comment",
  "bitbucket_tasks":                  "list_bitbucket_tasks",
  "pr_tasks":                         "list_bitbucket_tasks",
  "file_diff":                        "get_bitbucket_file_diff",
  "check_file_in_pr":                 "check_file_in_bitbucket_pr",
  "pr_file":                          "get_bitbucket_pr_file",
};

// ═══════════════════════════════════════════════════════════════════════════════
// Required Arguments Validation
// ═══════════════════════════════════════════════════════════════════════════════

const REQUIRED_ARGS = {
  fetch_bitbucket_pr:             ['project', 'repo', 'prId'],
  fetch_bitbucket_pr_files:       ['project', 'repo', 'prId'],
  fetch_bitbucket_pr_diff:        ['project', 'repo', 'prId'],
  fetch_bitbucket_pr_activities:  ['project', 'repo', 'prId'],
  search_bitbucket_prs:           ['project', 'repo'],
  fetch_bitbucket_file:           ['project', 'repo', 'filePath'],
  summarize_bitbucket_contributions: ['project', 'repo', 'person'],
  get_bitbucket_pr_comments:      ['project', 'repo', 'prId'],
  add_bitbucket_pr_comment:       ['project', 'repo', 'prId', 'comment'],
  update_bitbucket_pr_comment:    ['project', 'repo', 'prId', 'commentId', 'comment'],
  delete_bitbucket_pr_comment:    ['project', 'repo', 'prId', 'commentId'],
  reply_bitbucket_pr_comment:     ['project', 'repo', 'prId', 'parentCommentId', 'comment'],
  create_bitbucket_task:          ['commentId', 'text'],
  list_bitbucket_tasks:           ['project', 'repo', 'prId'],
  update_bitbucket_task:          ['taskId', 'text'],
  delete_bitbucket_task:          ['taskId'],
  resolve_bitbucket_task:         ['taskId'],
  reopen_bitbucket_task:          ['taskId'],
  get_bitbucket_file_diff:        ['project', 'repo', 'filePath'],
  check_file_in_bitbucket_pr:     ['project', 'repo', 'prId', 'filePath'],
  get_bitbucket_pr_file:          ['project', 'repo', 'prId', 'filePath'],
};

// ═══════════════════════════════════════════════════════════════════════════════
// Action Router
// ═══════════════════════════════════════════════════════════════════════════════

const ACTION_ROUTER = {
  fetch_bitbucket_pr:             (c, a) => fetchBitbucketPR(c, a),
  fetch_bitbucket_pr_files:       (c, a) => fetchBitbucketPRFiles(c, a),
  fetch_bitbucket_pr_diff:        (c, a) => fetchBitbucketPRDiff(c, a),
  fetch_bitbucket_pr_activities:  (c, a) => fetchBitbucketPRActivities(c, a),
  search_bitbucket_prs:           (c, a) => searchBitbucketPRs(c, a),
  fetch_bitbucket_file:           (c, a) => fetchBitbucketFile(c, a),
  summarize_bitbucket_contributions: (c, a) => summarizeBitbucketContributions(c, a),
  get_bitbucket_pr_comments:      (c, a) => getBitbucketPRComments(c, a),
  add_bitbucket_pr_comment:       (c, a) => addBitbucketPRComment(c, a),
  update_bitbucket_pr_comment:    (c, a) => updateBitbucketPRComment(c, a),
  delete_bitbucket_pr_comment:    (c, a) => deleteBitbucketPRComment(c, a),
  reply_bitbucket_pr_comment:     (c, a) => replyBitbucketPRComment(c, a),
  create_bitbucket_task:          (c, a) => createBitbucketTask(c, a),
  list_bitbucket_tasks:           (c, a) => listBitbucketTasks(c, a),
  update_bitbucket_task:          (c, a) => updateBitbucketTask(c, a),
  delete_bitbucket_task:          (c, a) => deleteBitbucketTask(c, a),
  resolve_bitbucket_task:         (c, a) => resolveBitbucketTask(c, a),
  reopen_bitbucket_task:          (c, a) => reopenBitbucketTask(c, a),
  get_bitbucket_file_diff:        (c, a) => getBitbucketFileDiff(c, a),
  check_file_in_bitbucket_pr:     (c, a) => checkFileInBitbucketPR(c, a),
  get_bitbucket_pr_file:          (c, a) => getBitbucketPRFile(c, a),
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
      error: 'Usage: node bitbucket_cli.js <action>\nSet $env:CLI_JSON_ARGS with JSON arguments.',
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
  if (!creds.bitbucketToken) {
    console.log(JSON.stringify({ success: false, error: `Missing BITBUCKET_PAT_TOKEN in .env.${creds.profileId}` }));
    process.exit(1);
  }
  if (!creds.bitbucketUrl) {
    console.log(JSON.stringify({ success: false, error: `Missing BITBUCKET_BASE_URL in .env.${creds.profileId}` }));
    process.exit(1);
  }

  const client = new RestClient(creds.bitbucketUrl, creds.bitbucketToken);

  try {
    const result = await ACTION_ROUTER[action](client, args);
    console.log(JSON.stringify({ success: true, data: result }));
  } catch (e) {
    console.log(JSON.stringify({ success: false, error: e.message || String(e) }));
    process.exit(1);
  }
}

main();
