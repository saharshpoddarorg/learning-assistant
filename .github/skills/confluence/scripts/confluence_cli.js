#!/usr/bin/env node
/**
 * Confluence CLI - PAT-authenticated REST API client.
 *
 * Usage:
 *   node scripts/confluence_cli.js <action>
 *
 * Arguments are read from:
 *   1. $env:CLI_JSON_ARGS (recommended - avoids PowerShell quoting bugs)
 *   2. process.argv[3] (fallback)
 *
 * Authentication:
 *   Reads PAT tokens from a .env file found by walking up from the workspace root.
 *   Required variables: CONFLUENCE_PAT_TOKEN, CONFLUENCE_BASE_URL
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
// TLS Configuration - handle corporate/self-signed certificates
// ═══════════════════════════════════════════════════════════════════════════════
{
  if (!process.env.NODE_EXTRA_CA_CERTS && !process.env.NODE_TLS_REJECT_UNAUTHORIZED) {
    process.env.NODE_TLS_REJECT_UNAUTHORIZED = '0';
  }
}

// ═══════════════════════════════════════════════════════════════════════════════
// .env Loader
// ═══════════════════════════════════════════════════════════════════════════════

function findEnvFiles() {
  const candidates = [
    resolve(__dirname, '..', '..', '..', '..', '.env'),   // <workspace>/.env
    resolve(__dirname, '..', '.env'),                // <skill>/.env
  ];
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
      let value = trimmed.substring(eqIdx + 1).trim();
      if ((value.startsWith('"') && value.endsWith('"')) || (value.startsWith("'") && value.endsWith("'"))) {
        value = value.slice(1, -1);
      }
      if (value) env[key] = value;
    }
  }
  return env;
}

// ═══════════════════════════════════════════════════════════════════════════════
// REST Client
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
}

// ═══════════════════════════════════════════════════════════════════════════════
// Helpers
// ═══════════════════════════════════════════════════════════════════════════════

function enc(s) { return encodeURIComponent(s); }

// ═══════════════════════════════════════════════════════════════════════════════
// Confluence Action Handlers
// ═══════════════════════════════════════════════════════════════════════════════

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
  const current = await client.get(`/rest/api/content/${args.pageId}?expand=version,space,body.storage`);
  const newTitle = args.title || current.title;
  const newContent = args.content || current.body?.storage?.value || '';
  return await client.put(`/rest/api/content/${args.pageId}`, {
    version: { number: current.version.number + 1 },
    title: newTitle,
    type: 'page',
    body: { storage: { value: newContent, representation: 'storage' } },
  });
}

async function appendToConfluencePage(client, args) {
  const current = await client.get(`/rest/api/content/${args.pageId}?expand=version,body.storage`);
  const currentBody = current.body?.storage?.value || '';
  const newContent = `${currentBody}\n<hr />\n${args.content}`;
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
  return await client.get(`/rest/experimental/content/${args.pageId}/version?limit=${max}`);
}

async function restoreConfluencePageVersion(client, args) {
  return await client.post(`/rest/experimental/content/${args.pageId}/version`, {
    operationKey: 'restore',
    params: { versionNumber: args.versionNumber },
  });
}

async function getConfluencePageTree(client, args) {
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
  const url = `${client.baseUrl}/spaces/flyingpdf/pdfpageexport.action?pageId=${args.pageId}`;
  console.error(`GET ${url} (PDF export)`);
  return { pdfUrl: url, note: 'Open this URL in a browser to download the PDF. Direct fetch requires session auth.' };
}

async function getCurrentConfluenceUser(client, _args) {
  return await client.get('/rest/api/user/current');
}

// ═══════════════════════════════════════════════════════════════════════════════
// Action Aliases - silently fixes common LLM hallucinations
// ═══════════════════════════════════════════════════════════════════════════════

const ACTION_ALIASES = {
  "search_confluence_pages":          "search_confluence",
  "search_confluence_page":           "search_confluence",
  "confluence_search":                "search_confluence",
  "confluence_cql_search":            "search_confluence_cql",
  "search_confluence_cql_pages":      "search_confluence_cql",
  "get_confluence_page":              "fetch_confluence_page",
  "read_confluence_page":             "fetch_confluence_page",
  "fetch_confluence":                 "fetch_confluence_page",
  "comment_confluence":               "add_confluence_comment",
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
};

// ═══════════════════════════════════════════════════════════════════════════════
// Required Arguments Validation
// ═══════════════════════════════════════════════════════════════════════════════

const REQUIRED_ARGS = {
  fetch_confluence_page:          ['pageId'],
  search_confluence:              ['query'],
  search_confluence_cql:          ['cql'],
  list_confluence_pages:          ['parentPageId'],
  create_confluence_page:         ['title', 'spaceKey'],
  update_confluence_page:         ['pageId'],
  append_to_confluence_page:      ['pageId', 'content'],
  add_confluence_comment:         ['pageId', 'comment'],
  get_confluence_comments:        ['pageId'],
  reply_to_confluence_comment:    ['pageId', 'parentCommentId', 'reply'],
  delete_confluence_page:         ['pageId'],
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
};

// ═══════════════════════════════════════════════════════════════════════════════
// Action Router
// ═══════════════════════════════════════════════════════════════════════════════

const ACTION_ROUTER = {
  fetch_confluence_page:          (c, a) => fetchConfluencePage(c, a),
  search_confluence:              (c, a) => searchConfluence(c, a),
  search_confluence_cql:          (c, a) => searchConfluenceCql(c, a),
  list_confluence_pages:          (c, a) => listConfluencePages(c, a),
  create_confluence_page:         (c, a) => createConfluencePage(c, a),
  update_confluence_page:         (c, a) => updateConfluencePage(c, a),
  append_to_confluence_page:      (c, a) => appendToConfluencePage(c, a),
  add_confluence_comment:         (c, a) => addConfluenceComment(c, a),
  get_confluence_comments:        (c, a) => getConfluenceComments(c, a),
  reply_to_confluence_comment:    (c, a) => replyToConfluenceComment(c, a),
  delete_confluence_page:         (c, a) => deleteConfluencePage(c, a),
  like_confluence_page:           (c, a) => likeConfluencePage(c, a),
  add_confluence_inline_comment:  (c, a) => addConfluenceInlineComment(c, a),
  create_confluence_blog_post:    (c, a) => createConfluenceBlogPost(c, a),
  get_confluence_blog_posts:      (c, a) => getConfluenceBlogPosts(c, a),
  copy_confluence_page:           (c, a) => copyConfluencePage(c, a),
  move_confluence_page:           (c, a) => moveConfluencePage(c, a),
  add_confluence_page_labels:     (c, a) => addConfluencePageLabels(c, a),
  get_confluence_page_labels:     (c, a) => getConfluencePageLabels(c, a),
  remove_confluence_page_label:   (c, a) => removeConfluencePageLabel(c, a),
  search_confluence_by_label:     (c, a) => searchConfluenceByLabel(c, a),
  get_confluence_page_property:   (c, a) => getConfluencePageProperty(c, a),
  set_confluence_page_property:   (c, a) => setConfluencePageProperty(c, a),
  get_confluence_page_versions:   (c, a) => getConfluencePageVersions(c, a),
  restore_confluence_page_version:(c, a) => restoreConfluencePageVersion(c, a),
  get_confluence_page_tree:       (c, a) => getConfluencePageTree(c, a),
  get_confluence_page_ancestors:  (c, a) => getConfluencePageAncestors(c, a),
  watch_confluence_page:          (c, a) => watchConfluencePage(c, { ...a, unwatch: false }),
  unwatch_confluence_page:        (c, a) => watchConfluencePage(c, { ...a, unwatch: true }),
  get_confluence_space:           (c, a) => getConfluenceSpace(c, a),
  get_space_content:              (c, a) => getSpaceContent(c, a),
  get_confluence_templates:       (c, a) => getConfluenceTemplates(c, a),
  export_confluence_page_pdf:     (c, a) => exportConfluencePagePdf(c, a),
  get_current_confluence_user:    (c, a) => getCurrentConfluenceUser(c, a),
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
      error: 'Usage: node confluence_cli.js <action>\nSet $env:CLI_JSON_ARGS with JSON arguments.',
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

  const env = loadEnv();
  if (!env.CONFLUENCE_PAT_TOKEN) {
    console.log(JSON.stringify({ success: false, error: 'Missing CONFLUENCE_PAT_TOKEN in .env file.' }));
    process.exit(1);
  }
  if (!env.CONFLUENCE_BASE_URL) {
    console.log(JSON.stringify({ success: false, error: 'Missing CONFLUENCE_BASE_URL in .env file.' }));
    process.exit(1);
  }

  const client = new RestClient(env.CONFLUENCE_BASE_URL, env.CONFLUENCE_PAT_TOKEN);

  try {
    const result = await ACTION_ROUTER[action](client, args);
    console.log(JSON.stringify({ success: true, data: result }));
  } catch (e) {
    console.log(JSON.stringify({ success: false, error: e.message || String(e) }));
    process.exit(1);
  }
}

main();
