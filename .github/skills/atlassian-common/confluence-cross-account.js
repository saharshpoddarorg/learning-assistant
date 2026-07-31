#!/usr/bin/env node
/**
 * confluence-cross-account.js — Cross-account Confluence operations.
 *
 * Usage:
 *   node confluence-cross-account.js <action>
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
// Action Handlers
// ═══════════════════════════════════════════════════════════════════════════════

/**
 * migrate_page — Fetch a page from source account and create it in target account.
 * Copies title and storage-format body. Labels are copied when present.
 */
async function migratePage(sourceClient, targetClient, args) {
  const source = await sourceClient.get(
    `/rest/api/content/${args.pageId}?expand=body.storage,metadata.labels,version`
  );

  const payload = {
    type:  'page',
    title: args.newTitle || source.title,
    space: { key: args.targetSpaceKey },
    body:  { storage: { value: source.body?.storage?.value || '', representation: 'storage' } },
  };
  if (args.targetParentId) payload.ancestors = [{ id: String(args.targetParentId) }];

  const created = await targetClient.post('/rest/api/content', payload);

  // Copy labels if present
  const labels = source.metadata?.labels?.results || [];
  if (labels.length) {
    try {
      await targetClient.post(
        `/rest/api/content/${created.id}/label`,
        labels.map(l => ({ prefix: l.prefix || 'global', name: l.name }))
      );
    } catch { /* labels are non-critical */ }
  }

  return {
    sourcePageId:  args.pageId,
    sourceAccount: args.sourceAccount,
    targetPageId:  created.id,
    targetAccount: args.targetAccount,
    title:         created.title,
    targetUrl:     created._links?.webui,
  };
}

/**
 * migrate_page_tree — Recursively migrate a page and all its children to
 * the target account. Returns a flat list of all source→target page mappings.
 */
async function migratePageTree(sourceClient, targetClient, args) {
  const results = [];

  async function migrateOne(pageId, targetParentId) {
    // Migrate this page
    let targetId;
    try {
      const r = await migratePage(sourceClient, targetClient, {
        ...args,
        pageId,
        targetParentId,
      });
      results.push({ success: true, sourcePageId: pageId, targetPageId: r.data?.targetPageId ?? r.targetPageId, title: r.data?.title ?? r.title });
      targetId = r.data?.targetPageId ?? r.targetPageId;
    } catch (e) {
      results.push({ success: false, sourcePageId: pageId, error: e.message });
      return;
    }

    // Fetch children and recurse
    const children = await sourceClient.get(`/rest/api/content/${pageId}/child/page?limit=50&expand=version`);
    for (const child of children.results || []) {
      await migrateOne(child.id, targetId);
    }
  }

  await migrateOne(args.pageId, args.targetParentId || null);

  return {
    total:   results.length,
    migrated: results.filter(r => r.success).length,
    failed:  results.filter(r => !r.success).length,
    results,
  };
}

// ═══════════════════════════════════════════════════════════════════════════════
// Action Registry
// ═══════════════════════════════════════════════════════════════════════════════

const REQUIRED_ARGS = {
  migrate_page:      ['sourceAccount', 'targetAccount', 'pageId', 'targetSpaceKey'],
  migrate_page_tree: ['sourceAccount', 'targetAccount', 'pageId', 'targetSpaceKey'],
};

const ACTION_ROUTER = {
  migrate_page:      (sc, tc, a) => migratePage(sc, tc, a),
  migrate_page_tree: (sc, tc, a) => migratePageTree(sc, tc, a),
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
      error: 'Usage: node confluence-cross-account.js <action>\nSet $env:CLI_JSON_ARGS with JSON arguments.',
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
    if (!creds.confluenceToken) {
      console.log(JSON.stringify({ success: false, error: `Missing CONFLUENCE_PAT_TOKEN in .env.${creds.profileId} (${label})` }));
      process.exit(1);
    }
    if (!creds.confluenceUrl) {
      console.log(JSON.stringify({ success: false, error: `Missing CONFLUENCE_BASE_URL in .env.${creds.profileId} (${label})` }));
      process.exit(1);
    }
  }

  const sourceClient = new RestClient(sourceCreds.confluenceUrl, sourceCreds.confluenceToken);
  const targetClient = new RestClient(targetCreds.confluenceUrl, targetCreds.confluenceToken);

  try {
    const result = await ACTION_ROUTER[action](sourceClient, targetClient, args);
    console.log(JSON.stringify({ success: true, data: result }));
  } catch (e) {
    console.log(JSON.stringify({ success: false, error: e.message || String(e) }));
    process.exit(1);
  }
}

main();
