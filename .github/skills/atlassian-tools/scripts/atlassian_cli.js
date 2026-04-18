#!/usr/bin/env node
/**
 * JIRA / Confluence / Bitbucket CLI — PAT-authenticated REST API client.
 *
 * Usage:
 *   node scripts/atlassian_cli.js <action>
 *
 * Arguments are read from:
 *   1. $env:CLI_JSON_ARGS (recommended — avoids PowerShell quoting bugs)
 *   2. process.argv[3] (fallback)
 *
 * Authentication:
 *   Reads PAT tokens from a .env file found by walking up from the workspace root.
 *   Required variables: JIRA_PAT_TOKEN, CONFLUENCE_PAT_TOKEN, BITBUCKET_PAT_TOKEN
 *   Optional variables: JIRA_BASE_URL, CONFLUENCE_BASE_URL, BITBUCKET_BASE_URL
 *
 * Dependencies: Node.js 18+ (uses built-in fetch). No npm install needed.
 *
 * Result is printed to stdout as JSON:
 *   { "success": true, "data": {...} }
 *   { "success": false, "error": "..." }
 */

import { readFileSync, existsSync } from 'fs';
import { fileURLToPath } from 'url';
import { dirname, join, resolve } from 'path';

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

// ═══════════════════════════════════════════════════════════════════════════════
// TLS Configuration — handle corporate/self-signed certificates
// ═══════════════════════════════════════════════════════════════════════════════
// Corporate environments often use internal CAs that Node.js doesn't trust.
// If NODE_EXTRA_CA_CERTS is not set, allow the .env to configure TLS behavior.
// Setting NODE_TLS_REJECT_UNAUTHORIZED=0 is a pragmatic fallback for internal servers.
// This is set BEFORE any fetch calls so it takes effect globally.
{
  // Will be re-checked after loadEnv() — but if env already has it, honor it early
  if (!process.env.NODE_EXTRA_CA_CERTS && !process.env.NODE_TLS_REJECT_UNAUTHORIZED) {
    process.env.NODE_TLS_REJECT_UNAUTHORIZED = '0';
  }
}

// ═══════════════════════════════════════════════════════════════════════════════
// .env Loader — finds and parses the .env file
// ═══════════════════════════════════════════════════════════════════════════════

function findEnvFiles() {
  // Returns ALL .env files found (merged in order — later files override earlier).
  const candidates = [
    resolve(__dirname, '..', '..', '..', '..', '.env'),               // <workspace>/.env
    resolve(__dirname, '..', '.env'),                                  // <skill>/.env
  ];
  // ENV_FILE override takes highest priority
  if (process.env.ENV_FILE && existsSync(process.env.ENV_FILE)) {
    candidates.push(process.env.ENV_FILE);
  }
  return candidates.filter(f => existsSync(f));
}

function loadEnv() {
  const envFiles = findEnvFiles();
  if (envFiles.length === 0) return {};
  const env = {};
  for (const envPath of envFiles) {
    const lines = readFileSync(envPath, 'utf8').split('\n');
    for (const line of lines) {
      const trimmed = line.trim();
      if (!trimmed || trimmed.startsWith('#')) continue;
      const eqIdx = trimmed.indexOf('=');
      if (eqIdx === -1) continue;
      const key = trimmed.substring(0, eqIdx).trim();
      const value = trimmed.substring(eqIdx + 1).trim();
      if (value) env[key] = value; // later files override earlier
    }
  }
  return env;
}

// ═══════════════════════════════════════════════════════════════════════════════
// REST Client — thin wrapper around fetch with PAT auth
// ═══════════════════════════════════════════════════════════════════════════════

class RestClient {
  constructor(baseUrl, patToken) {
    this.baseUrl = baseUrl.replace(/\/+$/, '');
    this.patToken = patToken;
  }

  _headers(contentType = null) {
    const h = {
      'Authorization': `Bearer ${this.patToken}`,
      'Accept': 'application/json',
      'X-Atlassian-Token': 'no-check',
    };
    if (contentType) h['Content-Type'] = contentType;
    return h;
  }

  async get(path) {
    const url = `${this.baseUrl}${path}`;
    console.error(`GET ${url}`);
    const resp = await fetch(url, { headers: this._headers() });
    return this._handleResponse(resp);
  }

  async post(path, body) {
    const url = `${this.baseUrl}${path}`;
    console.error(`POST ${url}`);
    const resp = await fetch(url, {
      method: 'POST',
      headers: this._headers('application/json'),
      body: JSON.stringify(body),
    });
    return this._handleResponse(resp);
  }

  async put(path, body) {
    const url = `${this.baseUrl}${path}`;
    console.error(`PUT ${url}`);
    const resp = await fetch(url, {
      method: 'PUT',
      headers: this._headers('application/json'),
      body: JSON.stringify(body),
    });
    return this._handleResponse(resp);
  }

  async del(path) {
    const url = `${this.baseUrl}${path}`;
    console.error(`DELETE ${url}`);
    const resp = await fetch(url, {
      method: 'DELETE',
      headers: this._headers(),
    });
    if (resp.status === 204) return { deleted: true };
    return this._handleResponse(resp);
  }

  async _handleResponse(resp) {
    const text = await resp.text();
    if (!resp.ok) {
      let detail = text;
      try {
        const j = JSON.parse(text);
        // JIRA error format: errorMessages[], errors{}, message
        const msgs = [];
        if (Array.isArray(j.errorMessages)) msgs.push(...j.errorMessages.filter(Boolean));
        if (j.errors && typeof j.errors === 'object') {
          for (const [k, v] of Object.entries(j.errors)) { if (v) msgs.push(`${k}: ${v}`); }
        }
        if (j.message) msgs.push(j.message);
        if (msgs.length) detail = msgs.join(' | ');
      } catch { /* use raw text */ }
      throw new Error(`HTTP ${resp.status}: ${detail}`);
    }
    if (!text.trim()) return {};
    try { return JSON.parse(text); } catch { return { rawText: text }; }
  }

  /**
   * Paginated GET for Bitbucket Server REST API (uses isLastPage/nextPageStart).
   */
  async getPaginated(path, maxPages = 5) {
    let allValues = [];
    let start = 0;
    for (let page = 0; page < maxPages; page++) {
      const separator = path.includes('?') ? '&' : '?';
      const data = await this.get(`${path}${separator}start=${start}&limit=500`);
      // Single-object response (no pagination)
      if (!Array.isArray(data.values) && !data.hasOwnProperty('isLastPage')) return data;
      if (data.values) allValues = allValues.concat(data.values);
      if (data.isLastPage !== false) break;
      start = data.nextPageStart || (start + (data.limit || 500));
    }
    return { values: allValues, size: allValues.length };
  }
}

// ═══════════════════════════════════════════════════════════════════════════════
// JQL Normalization — auto-quote unquoted emails
// ═══════════════════════════════════════════════════════════════════════════════

function normalizeJql(jql) {
  if (typeof jql !== 'string') return jql;
  return jql.replace(
    /\b(assignee|reporter)\s*(=|!=|~|!~)\s*([^\s()"']+@[^\s()"']+)/gi,
    (_m, field, op, val) => `${field} ${op} "${val}"`
  );
}

// ═══════════════════════════════════════════════════════════════════════════════
// Action Handlers
// ═══════════════════════════════════════════════════════════════════════════════

// ─── JIRA ─────────────────────────────────────────────────────────────────────

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
  // Step 1: Get available transitions
  const transitions = await client.get(`/rest/api/2/issue/${enc(args.issueKey)}/transitions`);
  const match = (transitions.transitions || []).find(t =>
    t.name.toLowerCase() === args.transition.toLowerCase()
  );
  if (!match) {
    const available = (transitions.transitions || []).map(t => t.name).join(', ');
    throw new Error(`Transition "${args.transition}" not found. Available: ${available}`);
  }
  // Step 2: Perform transition
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
  // args.issues = array of {projectKey, issueType, summary, description?, ...}
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
  // args.issueKeys = array of keys, args.transition = target status name
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
  // Uses the JIRA Agile REST API
  const results = [];
  for (const key of args.issueKeys) {
    try {
      await client.put(`/rest/api/2/issue/${enc(key)}`, { fields: { customfield_10008: args.epicKey } });
      results.push({ success: true, key });
    } catch (e) {
      // Try alternate epic link field names
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

// ─── Confluence ───────────────────────────────────────────────────────────────

async function fetchConfluencePage(client, args) {
  return await client.get(`/rest/api/content/${args.pageId}?expand=version,space,body.storage,metadata.labels`);
}

async function searchConfluence(client, args) {
  const cql = `text ~ "${args.query.replace(/"/g, '\\"')}" OR title ~ "${args.query.replace(/"/g, '\\"')}"`;
  const max = args.maxResults || 10;
  const spaceFilter = args.spaceKey ? ` AND space = "${args.spaceKey}"` : '';
  return await client.get(`/rest/api/content/search?cql=${encodeURIComponent(cql + spaceFilter)}&limit=${max}&expand=space,version`);
}

async function searchConfluenceCql(client, args) {
  const max = args.maxResults || 10;
  return await client.get(`/rest/api/content/search?cql=${encodeURIComponent(args.cql)}&limit=${max}&expand=space,version,history.lastUpdated`);
}

async function listConfluencePages(client, args) {
  const max = args.maxResults || 50;
  return await client.get(`/rest/api/content/${args.parentPageId}/child/page?limit=${max}&expand=version`);
}

async function createConfluencePage(client, args) {
  const payload = {
    type: 'page',
    title: args.title,
    space: { key: args.spaceKey },
    body: { storage: { value: args.content || '', representation: 'storage' } },
  };
  if (args.parentPageId) payload.ancestors = [{ id: String(args.parentPageId) }];
  return await client.post('/rest/api/content', payload);
}

async function updateConfluencePage(client, args) {
  // Step 1: Fetch current page to get version number
  const current = await client.get(`/rest/api/content/${args.pageId}?expand=version,space,body.storage`);
  const newTitle = args.title || current.title;
  const newContent = args.content || current.body?.storage?.value || '';
  // Step 2: Update with incremented version
  return await client.put(`/rest/api/content/${args.pageId}`, {
    version: { number: current.version.number + 1 },
    title: newTitle,
    type: 'page',
    body: { storage: { value: newContent, representation: 'storage' } },
  });
}

async function appendToConfluencePage(client, args) {
  // Step 1: Fetch current page
  const current = await client.get(`/rest/api/content/${args.pageId}?expand=version,body.storage`);
  const currentBody = current.body?.storage?.value || '';
  // Step 2: Append new content with separator
  const newContent = `${currentBody}\n<hr />\n${args.content}`;
  // Step 3: Update
  return await client.put(`/rest/api/content/${args.pageId}`, {
    version: { number: current.version.number + 1 },
    title: current.title,
    type: 'page',
    body: { storage: { value: newContent, representation: 'storage' } },
  });
}

async function addConfluenceComment(client, args) {
  const isHtml = /^</.test(args.comment.trim());
  const storageValue = isHtml ? args.comment : `<p>${args.comment.replace(/\n/g, '</p><p>')}</p>`;
  return await client.post('/rest/api/content', {
    type: 'comment',
    container: { id: String(args.pageId), type: 'page' },
    body: { storage: { value: storageValue, representation: 'storage' } },
  });
}

async function getConfluenceComments(client, args) {
  const max = args.maxResults || 25;
  return await client.get(`/rest/api/content/${args.pageId}/child/comment?expand=body.storage,version,ancestors&limit=${max}&depth=all`);
}

async function replyToConfluenceComment(client, args) {
  const isHtml = /^</.test(args.reply.trim());
  const storageValue = isHtml ? args.reply : `<p>${args.reply.replace(/\n/g, '</p><p>')}</p>`;
  return await client.post('/rest/api/content', {
    type: 'comment',
    container: { id: String(args.pageId), type: 'page' },
    ancestors: [{ id: String(args.parentCommentId) }],
    body: { storage: { value: storageValue, representation: 'storage' } },
  });
}

async function deleteConfluencePage(client, args) {
  return await client.del(`/rest/api/content/${args.pageId}`);
}

async function likeConfluencePage(client, args) {
  const url = `${client.baseUrl}/rest/api/content/${args.pageId}/likes`;
  console.error(`POST ${url}`);
  const resp = await fetch(url, {
    method: args.unlike ? 'DELETE' : 'POST',
    headers: client._headers(),
  });
  if (resp.status === 204 || resp.status === 200) return { liked: !args.unlike, pageId: args.pageId };
  throw new Error(`HTTP ${resp.status}: ${await resp.text()}`);
}

async function addConfluenceInlineComment(client, args) {
  // Inline comment = comment with inline marker in body
  const body = args.comment || args.body || '';
  const storageValue = /^</.test(body.trim()) ? body : `<p>${body.replace(/\n/g, '</p><p>')}</p>`;
  const payload = {
    type: 'comment',
    container: { id: String(args.pageId), type: 'page' },
    body: { storage: { value: storageValue, representation: 'storage' } },
    extensions: { location: 'inline' },
  };
  if (args.originalSelection) {
    payload.extensions.inlineProperties = { originalSelection: args.originalSelection };
  }
  return await client.post('/rest/api/content', payload);
}

async function createConfluenceBlogPost(client, args) {
  const payload = {
    type: 'blogpost',
    title: args.title,
    space: { key: args.spaceKey },
    body: { storage: { value: args.content || '', representation: 'storage' } },
  };
  return await client.post('/rest/api/content', payload);
}

async function getConfluenceBlogPosts(client, args) {
  const max = args.maxResults || 10;
  const cql = `type = blogpost AND space = "${args.spaceKey}" ORDER BY created DESC`;
  return await client.get(`/rest/api/content/search?cql=${encodeURIComponent(cql)}&limit=${max}&expand=space,version,history.lastUpdated`);
}

async function copyConfluencePage(client, args) {
  // Fetch source page then create a new one with same content
  const source = await client.get(`/rest/api/content/${args.pageId}?expand=body.storage,space`);
  const payload = {
    type: 'page',
    title: args.newTitle || `Copy of ${source.title}`,
    space: { key: args.targetSpaceKey || source.space.key },
    body: { storage: { value: source.body?.storage?.value || '', representation: 'storage' } },
  };
  if (args.targetParentId) payload.ancestors = [{ id: String(args.targetParentId) }];
  return await client.post('/rest/api/content', payload);
}

async function moveConfluencePage(client, args) {
  // Move = update the page's ancestors to a new parent
  const current = await client.get(`/rest/api/content/${args.pageId}?expand=version,body.storage,space`);
  const targetSpace = args.targetSpaceKey || current.space.key;
  return await client.put(`/rest/api/content/${args.pageId}`, {
    version: { number: current.version.number + 1 },
    title: current.title,
    type: 'page',
    space: { key: targetSpace },
    ancestors: [{ id: String(args.targetParentId) }],
    body: { storage: { value: current.body?.storage?.value || '', representation: 'storage' } },
  });
}

async function addConfluencePageLabels(client, args) {
  const labels = args.labels.map(l => ({ prefix: 'global', name: l }));
  return await client.post(`/rest/api/content/${args.pageId}/label`, labels);
}

async function getConfluencePageLabels(client, args) {
  return await client.get(`/rest/api/content/${args.pageId}/label`);
}

async function removeConfluencePageLabel(client, args) {
  return await client.del(`/rest/api/content/${args.pageId}/label/${enc(args.label)}`);
}

async function searchConfluenceByLabel(client, args) {
  const max = args.maxResults || 25;
  let cql = `label = "${args.label}"`;
  if (args.spaceKey) cql += ` AND space = "${args.spaceKey}"`;
  return await client.get(`/rest/api/content/search?cql=${encodeURIComponent(cql)}&limit=${max}&expand=space,version`);
}

async function getConfluencePageProperty(client, args) {
  if (args.propertyKey) {
    return await client.get(`/rest/api/content/${args.pageId}/property/${enc(args.propertyKey)}`);
  }
  return await client.get(`/rest/api/content/${args.pageId}/property`);
}

async function setConfluencePageProperty(client, args) {
  // Try update first, then create
  try {
    const existing = await client.get(`/rest/api/content/${args.pageId}/property/${enc(args.propertyKey)}`);
    return await client.put(`/rest/api/content/${args.pageId}/property/${enc(args.propertyKey)}`, {
      key: args.propertyKey,
      value: args.value,
      version: { number: existing.version.number + 1 },
    });
  } catch {
    return await client.post(`/rest/api/content/${args.pageId}/property`, {
      key: args.propertyKey,
      value: args.value,
    });
  }
}

async function getConfluencePageVersions(client, args) {
  const max = args.maxResults || 25;
  // Confluence Server uses /rest/experimental/content/{id}/version (not /rest/api/)
  return await client.get(`/rest/experimental/content/${args.pageId}/version?limit=${max}`);
}

async function restoreConfluencePageVersion(client, args) {
  return await client.post(`/rest/experimental/content/${args.pageId}/version`, {
    operationKey: 'restore',
    params: { versionNumber: args.versionNumber },
  });
}

async function getConfluencePageTree(client, args) {
  // Recursive child listing up to given depth
  const depth = args.depth || 3;
  async function getChildren(pageId, level) {
    if (level >= depth) return [];
    const data = await client.get(`/rest/api/content/${pageId}/child/page?limit=100&expand=version`);
    const results = data.results || [];
    for (const child of results) {
      child.children = await getChildren(child.id, level + 1);
    }
    return results;
  }
  const tree = await getChildren(args.pageId, 0);
  return { pageId: args.pageId, depth, tree };
}

async function getConfluencePageAncestors(client, args) {
  const page = await client.get(`/rest/api/content/${args.pageId}?expand=ancestors`);
  return { pageId: args.pageId, title: page.title, ancestors: page.ancestors || [] };
}

async function watchConfluencePage(client, args) {
  const userId = args.username || 'current';
  const url = `${client.baseUrl}/rest/api/user/watch/content/${args.pageId}`;
  console.error(`POST ${url}`);
  const resp = await fetch(url, {
    method: args.unwatch ? 'DELETE' : 'POST',
    headers: client._headers(),
  });
  if (resp.status === 204 || resp.status === 200) return { watching: !args.unwatch, pageId: args.pageId };
  throw new Error(`HTTP ${resp.status}: ${await resp.text()}`);
}

async function getConfluenceSpace(client, args) {
  return await client.get(`/rest/api/space/${enc(args.spaceKey)}?expand=description.plain,homepage,metadata.labels`);
}

async function getSpaceContent(client, args) {
  const max = args.maxResults || 50;
  const type = args.contentType || 'page';
  return await client.get(`/rest/api/space/${enc(args.spaceKey)}/content/${type}?limit=${max}&expand=version`);
}

async function getConfluenceTemplates(client, args) {
  if (args.spaceKey) {
    return await client.get(`/rest/api/template/page?spaceKey=${enc(args.spaceKey)}&expand=body`);
  }
  return await client.get('/rest/api/template/blueprint?expand=body');
}

async function exportConfluencePagePdf(client, args) {
  // Confluence Server PDF export endpoint
  const url = `${client.baseUrl}/spaces/flyingpdf/pdfpageexport.action?pageId=${args.pageId}`;
  console.error(`GET ${url} (PDF export)`);
  return { pdfUrl: url, note: 'Open this URL in a browser to download the PDF. Direct fetch requires session auth.' };
}

async function getCurrentConfluenceUser(client, _args) {
  return await client.get('/rest/api/user/current');
}

// ─── Bitbucket ────────────────────────────────────────────────────────────────

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
  // Get recent PRs authored by the person
  let path = `/rest/api/1.0/projects/${enc(args.project)}/repos/${enc(args.repo)}/pull-requests?state=ALL&limit=100`;
  const data = await client.getPaginated(path, 3);
  const prs = (data.values || []).filter(pr => {
    const author = pr.author?.user?.displayName || pr.author?.user?.name || '';
    const needle = args.person.toLowerCase();
    return author.toLowerCase().includes(needle);
  });
  // Filter by date range
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
  // Bitbucket Server requires `path` for the /comments endpoint.
  // Use activities endpoint and filter for COMMENT activities instead.
  const path = `/rest/api/1.0/projects/${enc(args.project)}/repos/${enc(args.repo)}/pull-requests/${args.prId}/activities`;
  const data = await client.getPaginated(path);
  const comments = (data.values || [])
    .filter(a => a.action === 'COMMENTED' && a.comment)
    .map(a => a.comment);
  return { values: comments, size: comments.length };
}

async function addBitbucketPRComment(client, args) {
  // For file-level comments, use /comments?path=... endpoint
  // For general PR comments, use /comments with no anchor
  const basePath = `/rest/api/1.0/projects/${enc(args.project)}/repos/${enc(args.repo)}/pull-requests/${args.prId}/comments`;
  const payload = { text: args.comment };
  if (args.parentId) payload.parent = { id: args.parentId };
  if (args.filePath) {
    // Inline file comment
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
  // Reply = add comment with parent
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
  // Get a file's content at the PR source branch
  const pr = await client.get(`/rest/api/1.0/projects/${enc(args.project)}/repos/${enc(args.repo)}/pull-requests/${args.prId}`);
  const branch = pr.fromRef?.id || pr.fromRef?.displayId;
  if (!branch) throw new Error('Could not determine PR source branch');
  return await client.get(`/rest/api/1.0/projects/${enc(args.project)}/repos/${enc(args.repo)}/browse/${args.filePath}?at=${encodeURIComponent(branch)}`);
}

// ═══════════════════════════════════════════════════════════════════════════════
// Helpers
// ═══════════════════════════════════════════════════════════════════════════════

function enc(s) { return encodeURIComponent(s); }

// ═══════════════════════════════════════════════════════════════════════════════
// Action Alias Map — silently fixes common LLM hallucinations
// ═══════════════════════════════════════════════════════════════════════════════

const ACTION_ALIASES = {
  // Confluence search
  "search_confluence_pages":          "search_confluence",
  "search_confluence_page":           "search_confluence",
  "confluence_search":                "search_confluence",
  "confluence_cql_search":            "search_confluence_cql",
  "search_confluence_cql_pages":      "search_confluence_cql",
  // JIRA search
  "search_jira":                      "search_jira_issues",
  "jira_search":                      "search_jira_issues",
  // Fetch aliases
  "get_jira_issue":                   "fetch_jira_issue",
  "get_confluence_page":              "fetch_confluence_page",
  "read_confluence_page":             "fetch_confluence_page",
  "fetch_confluence":                 "fetch_confluence_page",
  // Comment aliases
  "comment_jira":                     "add_jira_comment",
  "comment_confluence":               "add_confluence_comment",
  // Worklog aliases
  "log_work":                         "add_jira_worklog",
  "jira_worklog":                     "add_jira_worklog",
  "add_worklog":                      "add_jira_worklog",
  "add_work_log":                     "add_jira_worklog",
  "jira_work_log":                    "add_jira_worklog",
  "log_jira_work":                    "add_jira_worklog",
  // Bitbucket aliases
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
  // New action aliases
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
  "confluence_blog":                  "create_confluence_blog_post",
  "create_blog_post":                 "create_confluence_blog_post",
  "blog_posts":                       "get_confluence_blog_posts",
  "copy_page":                        "copy_confluence_page",
  "move_page":                        "move_confluence_page",
  "add_labels":                       "add_confluence_page_labels",
  "page_labels":                      "get_confluence_page_labels",
  "remove_label":                     "remove_confluence_page_label",
  "page_tree":                        "get_confluence_page_tree",
  "page_ancestors":                   "get_confluence_page_ancestors",
  "page_versions":                    "get_confluence_page_versions",
  "confluence_space":                 "get_confluence_space",
  "space_content":                    "get_space_content",
  "confluence_templates":             "get_confluence_templates",
  "export_pdf":                       "export_confluence_page_pdf",
  "confluence_myself":                "get_current_confluence_user",
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
  // JIRA — Core
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
  // JIRA — Links & Relations
  link_jira_issues:               ['inwardIssueKey', 'outwardIssueKey'],
  get_jira_issue_links:           ['issueKey'],
  get_jira_subtasks:              ['issueKey'],
  clone_jira_issue:               ['issueKey'],
  // JIRA — Bulk
  bulk_create_jira_issues:        ['issues'],
  bulk_transition_jira_issues:    ['issueKeys', 'transition'],
  // JIRA — Labels
  add_jira_labels:                ['issueKey', 'labels'],
  remove_jira_labels:             ['issueKey', 'labels'],
  // JIRA — Watchers
  get_jira_watchers:              ['issueKey'],
  add_jira_watcher:               ['issueKey', 'username'],
  remove_jira_watcher:            ['issueKey', 'username'],
  // JIRA — Metadata
  get_jira_issue_changelog:       ['issueKey'],
  get_jira_issue_types:           [],
  get_jira_statuses:              [],
  get_jira_components:            ['projectKey'],
  get_jira_versions:              ['projectKey'],
  search_jira_users:              ['query'],
  get_current_jira_user:          [],
  // JIRA — Agile
  get_epic_issues:                ['epicKey'],
  add_issues_to_epic:             ['epicKey', 'issueKeys'],
  get_jira_sprints:               ['boardId'],
  get_sprint_issues:              ['sprintId'],
  move_to_sprint:                 ['sprintId', 'issueKeys'],
  move_to_backlog:                ['issueKeys'],
  // Confluence — Core
  fetch_confluence_page:          ['pageId'],
  search_confluence:              ['query'],
  search_confluence_cql:          ['cql'],
  list_confluence_pages:          ['parentPageId'],
  create_confluence_page:         ['title', 'spaceKey'],
  update_confluence_page:         ['pageId'],
  append_to_confluence_page:      ['pageId'],
  add_confluence_comment:         ['pageId', 'comment'],
  get_confluence_comments:        ['pageId'],
  reply_to_confluence_comment:    ['pageId', 'parentCommentId', 'reply'],
  delete_confluence_page:         ['pageId'],
  // Confluence — Extended
  like_confluence_page:           ['pageId'],
  add_confluence_inline_comment:  ['pageId', 'comment'],
  create_confluence_blog_post:    ['title', 'spaceKey'],
  get_confluence_blog_posts:      ['spaceKey'],
  copy_confluence_page:           ['pageId'],
  move_confluence_page:           ['pageId', 'targetParentId'],
  add_confluence_page_labels:     ['pageId', 'labels'],
  get_confluence_page_labels:     ['pageId'],
  remove_confluence_page_label:   ['pageId', 'label'],
  search_confluence_by_label:     ['label'],
  get_confluence_page_property:   ['pageId'],
  set_confluence_page_property:   ['pageId', 'propertyKey', 'value'],
  get_confluence_page_versions:   ['pageId'],
  restore_confluence_page_version: ['pageId', 'versionNumber'],
  get_confluence_page_tree:       ['pageId'],
  get_confluence_page_ancestors:  ['pageId'],
  watch_confluence_page:          ['pageId'],
  unwatch_confluence_page:        ['pageId'],
  get_confluence_space:           ['spaceKey'],
  get_space_content:              ['spaceKey'],
  get_confluence_templates:       [],
  export_confluence_page_pdf:     ['pageId'],
  get_current_confluence_user:    [],
  // Bitbucket — Core
  fetch_bitbucket_pr:             ['project', 'repo', 'prId'],
  fetch_bitbucket_pr_files:       ['project', 'repo', 'prId'],
  fetch_bitbucket_pr_diff:        ['project', 'repo', 'prId'],
  fetch_bitbucket_pr_activities:  ['project', 'repo', 'prId'],
  search_bitbucket_prs:           ['project', 'repo'],
  fetch_bitbucket_file:           ['project', 'repo', 'filePath'],
  summarize_bitbucket_contributions: ['project', 'repo', 'person'],
  // Bitbucket — PR Comments
  get_bitbucket_pr_comments:      ['project', 'repo', 'prId'],
  add_bitbucket_pr_comment:       ['project', 'repo', 'prId', 'comment'],
  update_bitbucket_pr_comment:    ['project', 'repo', 'prId', 'commentId', 'comment'],
  delete_bitbucket_pr_comment:    ['project', 'repo', 'prId', 'commentId'],
  reply_bitbucket_pr_comment:     ['project', 'repo', 'prId', 'parentCommentId', 'comment'],
  // Bitbucket — Tasks
  create_bitbucket_task:          ['commentId', 'text'],
  list_bitbucket_tasks:           ['project', 'repo', 'prId'],
  update_bitbucket_task:          ['taskId', 'text'],
  delete_bitbucket_task:          ['taskId'],
  resolve_bitbucket_task:         ['taskId'],
  reopen_bitbucket_task:          ['taskId'],
  // Bitbucket — Files
  get_bitbucket_file_diff:        ['project', 'repo', 'filePath'],
  check_file_in_bitbucket_pr:     ['project', 'repo', 'prId', 'filePath'],
  get_bitbucket_pr_file:          ['project', 'repo', 'prId', 'filePath'],
};

// ═══════════════════════════════════════════════════════════════════════════════
// Action Router
// ═══════════════════════════════════════════════════════════════════════════════

const ACTION_ROUTER = {
  // JIRA — Core
  fetch_jira_issue:               (c, a) => fetchJiraIssue(c.jira, a),
  fetch_jira_comments:            (c, a) => fetchJiraComments(c.jira, a),
  search_jira_issues:             (c, a) => searchJiraIssues(c.jira, a),
  list_jira_issues_by_project:    (c, a) => listJiraIssuesByProject(c.jira, a),
  create_jira_issue:              (c, a) => createJiraIssue(c.jira, a),
  update_jira_issue:              (c, a) => updateJiraIssue(c.jira, a),
  transition_jira_issue:          (c, a) => transitionJiraIssue(c.jira, a),
  add_jira_comment:               (c, a) => addJiraComment(c.jira, a),
  add_jira_worklog:               (c, a) => addJiraWorklog(c.jira, a),
  delete_jira_issue:              (c, a) => deleteJiraIssue(c.jira, a),
  // JIRA — Links & Relations
  link_jira_issues:               (c, a) => linkJiraIssues(c.jira, a),
  get_jira_issue_links:           (c, a) => getJiraIssueLinks(c.jira, a),
  get_jira_subtasks:              (c, a) => getJiraSubtasks(c.jira, a),
  clone_jira_issue:               (c, a) => cloneJiraIssue(c.jira, a),
  // JIRA — Bulk
  bulk_create_jira_issues:        (c, a) => bulkCreateJiraIssues(c.jira, a),
  bulk_transition_jira_issues:    (c, a) => bulkTransitionJiraIssues(c.jira, a),
  // JIRA — Labels
  add_jira_labels:                (c, a) => addJiraLabels(c.jira, a),
  remove_jira_labels:             (c, a) => removeJiraLabels(c.jira, a),
  // JIRA — Watchers
  get_jira_watchers:              (c, a) => getJiraWatchers(c.jira, a),
  add_jira_watcher:               (c, a) => addJiraWatcher(c.jira, a),
  remove_jira_watcher:            (c, a) => removeJiraWatcher(c.jira, a),
  // JIRA — Metadata
  get_jira_issue_changelog:       (c, a) => getJiraIssueChangelog(c.jira, a),
  get_jira_issue_types:           (c, a) => getJiraIssueTypes(c.jira, a),
  get_jira_statuses:              (c, a) => getJiraStatuses(c.jira, a),
  get_jira_components:            (c, a) => getJiraComponents(c.jira, a),
  get_jira_versions:              (c, a) => getJiraVersions(c.jira, a),
  search_jira_users:              (c, a) => searchJiraUsers(c.jira, a),
  get_current_jira_user:          (c, a) => getCurrentJiraUser(c.jira, a),
  // JIRA — Agile
  get_epic_issues:                (c, a) => getEpicIssues(c.jira, a),
  add_issues_to_epic:             (c, a) => addIssuesToEpic(c.jira, a),
  get_jira_sprints:               (c, a) => getJiraSprints(c.jira, a),
  get_sprint_issues:              (c, a) => getSprintIssues(c.jira, a),
  move_to_sprint:                 (c, a) => moveToSprint(c.jira, a),
  move_to_backlog:                (c, a) => moveToBacklog(c.jira, a),
  // Confluence — Core
  fetch_confluence_page:          (c, a) => fetchConfluencePage(c.confluence, a),
  search_confluence:              (c, a) => searchConfluence(c.confluence, a),
  search_confluence_cql:          (c, a) => searchConfluenceCql(c.confluence, a),
  list_confluence_pages:          (c, a) => listConfluencePages(c.confluence, a),
  create_confluence_page:         (c, a) => createConfluencePage(c.confluence, a),
  update_confluence_page:         (c, a) => updateConfluencePage(c.confluence, a),
  append_to_confluence_page:      (c, a) => appendToConfluencePage(c.confluence, a),
  add_confluence_comment:         (c, a) => addConfluenceComment(c.confluence, a),
  get_confluence_comments:        (c, a) => getConfluenceComments(c.confluence, a),
  reply_to_confluence_comment:    (c, a) => replyToConfluenceComment(c.confluence, a),
  delete_confluence_page:         (c, a) => deleteConfluencePage(c.confluence, a),
  // Confluence — Extended
  like_confluence_page:           (c, a) => likeConfluencePage(c.confluence, a),
  add_confluence_inline_comment:  (c, a) => addConfluenceInlineComment(c.confluence, a),
  create_confluence_blog_post:    (c, a) => createConfluenceBlogPost(c.confluence, a),
  get_confluence_blog_posts:      (c, a) => getConfluenceBlogPosts(c.confluence, a),
  copy_confluence_page:           (c, a) => copyConfluencePage(c.confluence, a),
  move_confluence_page:           (c, a) => moveConfluencePage(c.confluence, a),
  add_confluence_page_labels:     (c, a) => addConfluencePageLabels(c.confluence, a),
  get_confluence_page_labels:     (c, a) => getConfluencePageLabels(c.confluence, a),
  remove_confluence_page_label:   (c, a) => removeConfluencePageLabel(c.confluence, a),
  search_confluence_by_label:     (c, a) => searchConfluenceByLabel(c.confluence, a),
  get_confluence_page_property:   (c, a) => getConfluencePageProperty(c.confluence, a),
  set_confluence_page_property:   (c, a) => setConfluencePageProperty(c.confluence, a),
  get_confluence_page_versions:   (c, a) => getConfluencePageVersions(c.confluence, a),
  restore_confluence_page_version:(c, a) => restoreConfluencePageVersion(c.confluence, a),
  get_confluence_page_tree:       (c, a) => getConfluencePageTree(c.confluence, a),
  get_confluence_page_ancestors:  (c, a) => getConfluencePageAncestors(c.confluence, a),
  watch_confluence_page:          (c, a) => watchConfluencePage(c.confluence, { ...a, unwatch: false }),
  unwatch_confluence_page:        (c, a) => watchConfluencePage(c.confluence, { ...a, unwatch: true }),
  get_confluence_space:           (c, a) => getConfluenceSpace(c.confluence, a),
  get_space_content:              (c, a) => getSpaceContent(c.confluence, a),
  get_confluence_templates:       (c, a) => getConfluenceTemplates(c.confluence, a),
  export_confluence_page_pdf:     (c, a) => exportConfluencePagePdf(c.confluence, a),
  get_current_confluence_user:    (c, a) => getCurrentConfluenceUser(c.confluence, a),
  // Bitbucket — Core
  fetch_bitbucket_pr:             (c, a) => fetchBitbucketPR(c.bitbucket, a),
  fetch_bitbucket_pr_files:       (c, a) => fetchBitbucketPRFiles(c.bitbucket, a),
  fetch_bitbucket_pr_diff:        (c, a) => fetchBitbucketPRDiff(c.bitbucket, a),
  fetch_bitbucket_pr_activities:  (c, a) => fetchBitbucketPRActivities(c.bitbucket, a),
  search_bitbucket_prs:           (c, a) => searchBitbucketPRs(c.bitbucket, a),
  fetch_bitbucket_file:           (c, a) => fetchBitbucketFile(c.bitbucket, a),
  summarize_bitbucket_contributions: (c, a) => summarizeBitbucketContributions(c.bitbucket, a),
  // Bitbucket — PR Comments
  get_bitbucket_pr_comments:      (c, a) => getBitbucketPRComments(c.bitbucket, a),
  add_bitbucket_pr_comment:       (c, a) => addBitbucketPRComment(c.bitbucket, a),
  update_bitbucket_pr_comment:    (c, a) => updateBitbucketPRComment(c.bitbucket, a),
  delete_bitbucket_pr_comment:    (c, a) => deleteBitbucketPRComment(c.bitbucket, a),
  reply_bitbucket_pr_comment:     (c, a) => replyBitbucketPRComment(c.bitbucket, a),
  // Bitbucket — Tasks
  create_bitbucket_task:          (c, a) => createBitbucketTask(c.bitbucket, a),
  list_bitbucket_tasks:           (c, a) => listBitbucketTasks(c.bitbucket, a),
  update_bitbucket_task:          (c, a) => updateBitbucketTask(c.bitbucket, a),
  delete_bitbucket_task:          (c, a) => deleteBitbucketTask(c.bitbucket, a),
  resolve_bitbucket_task:         (c, a) => resolveBitbucketTask(c.bitbucket, a),
  reopen_bitbucket_task:          (c, a) => reopenBitbucketTask(c.bitbucket, a),
  // Bitbucket — Files
  get_bitbucket_file_diff:        (c, a) => getBitbucketFileDiff(c.bitbucket, a),
  check_file_in_bitbucket_pr:     (c, a) => checkFileInBitbucketPR(c.bitbucket, a),
  get_bitbucket_pr_file:          (c, a) => getBitbucketPRFile(c.bitbucket, a),
};

// ═══════════════════════════════════════════════════════════════════════════════
// Main
// ═══════════════════════════════════════════════════════════════════════════════

async function main() {
  let action = process.argv[2];
  const rawArgs = process.argv[3] || process.env.CLI_JSON_ARGS || '{}';
  delete process.env.CLI_JSON_ARGS;

  // Resolve aliases
  if (action && ACTION_ALIASES[action]) {
    action = ACTION_ALIASES[action];
  }

  // No action → print help
  if (!action) {
    console.log(JSON.stringify({
      success: false,
      error: 'Usage: node atlassian_cli.js <action>\nSet $env:CLI_JSON_ARGS with JSON arguments.',
      available_actions: Object.keys(ACTION_ROUTER),
    }));
    process.exit(1);
  }

  // Parse JSON args
  let args;
  try {
    args = JSON.parse(rawArgs);
  } catch (e) {
    console.log(JSON.stringify({
      success: false,
      error: `Invalid JSON arguments: ${e.message}\n\nUse single quotes around $env:CLI_JSON_ARGS.\nFor HTML content, use contentFromEnv or contentFile patterns.`,
    }));
    process.exit(1);
  }

  // Resolve content from env var or file (avoids JSON-encoding HTML)
  if (args.contentFromEnv) {
    const envContent = process.env.CLI_CONTENT;
    delete process.env.CLI_CONTENT;
    if (!envContent) {
      console.log(JSON.stringify({ success: false, error: 'contentFromEnv is true but $env:CLI_CONTENT is empty or not set.' }));
      process.exit(1);
    }
    args.content = envContent;
    delete args.contentFromEnv;
  }
  if (args.contentFile) {
    try {
      args.content = readFileSync(args.contentFile, 'utf8');
    } catch (e) {
      console.log(JSON.stringify({ success: false, error: `Cannot read contentFile "${args.contentFile}": ${e.message}` }));
      process.exit(1);
    }
    delete args.contentFile;
  }

  // Validate action exists
  if (!ACTION_ROUTER[action]) {
    console.log(JSON.stringify({
      success: false,
      error: `Unknown action: "${action}". Available: ${Object.keys(ACTION_ROUTER).join(', ')}`,
    }));
    process.exit(1);
  }

  // Validate required args
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

  // Load .env and build clients
  const env = loadEnv();
  if (!Object.keys(env).length) {
    const envPath = resolve(__dirname, '..', '..', '..', '..', '.env');
    console.log(JSON.stringify({
      success: false,
      error: `Missing .env file. Create ${envPath} with JIRA_PAT_TOKEN, CONFLUENCE_PAT_TOKEN, and BITBUCKET_PAT_TOKEN.\nSee .github/skills/atlassian-tools/SKILL.md → Setup section.`,
    }));
    process.exit(1);
  }

  // Determine which service this action needs
  const JIRA_ACTIONS = new Set([
    'get_epic_issues', 'add_issues_to_epic', 'get_sprint_issues',
    'move_to_sprint', 'move_to_backlog',
  ]);
  const service = JIRA_ACTIONS.has(action) ? 'jira'
    : action.includes('jira') ? 'jira'
    : action.includes('confluence') ? 'confluence'
    : action.includes('bitbucket') ? 'bitbucket'
    : null;

  const tokenKey = service === 'jira' ? 'JIRA_PAT_TOKEN'
    : service === 'confluence' ? 'CONFLUENCE_PAT_TOKEN'
    : service === 'bitbucket' ? 'BITBUCKET_PAT_TOKEN'
    : null;

  const urlKey = service === 'jira' ? 'JIRA_BASE_URL'
    : service === 'confluence' ? 'CONFLUENCE_BASE_URL'
    : service === 'bitbucket' ? 'BITBUCKET_BASE_URL'
    : null;

  if (tokenKey && !env[tokenKey]) {
    console.log(JSON.stringify({
      success: false,
      error: `Missing ${tokenKey} in .env file. Add it and retry.\nSee .github/skills/atlassian-tools/SKILL.md → Setup section.`,
    }));
    process.exit(1);
  }

  if (urlKey && !env[urlKey]) {
    console.log(JSON.stringify({
      success: false,
      error: `Missing ${urlKey} in .env file. Add your server URL (e.g., https://your-${service}-server.example.com) and retry.`,
    }));
    process.exit(1);
  }

  // Build clients (only the needed one)
  const clients = {};
  if (env.JIRA_PAT_TOKEN && env.JIRA_BASE_URL) {
    clients.jira = new RestClient(env.JIRA_BASE_URL, env.JIRA_PAT_TOKEN);
  }
  if (env.CONFLUENCE_PAT_TOKEN && env.CONFLUENCE_BASE_URL) {
    clients.confluence = new RestClient(env.CONFLUENCE_BASE_URL, env.CONFLUENCE_PAT_TOKEN);
  }
  if (env.BITBUCKET_PAT_TOKEN && env.BITBUCKET_BASE_URL) {
    clients.bitbucket = new RestClient(env.BITBUCKET_BASE_URL, env.BITBUCKET_PAT_TOKEN);
  }

  // Execute
  try {
    const result = await ACTION_ROUTER[action](clients, args);
    console.log(JSON.stringify({ success: true, data: result }));
  } catch (e) {
    console.log(JSON.stringify({ success: false, error: e.message || String(e) }));
    process.exit(1);
  }
}

main();
