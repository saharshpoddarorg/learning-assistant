#!/usr/bin/env node
/**
 * bitbucket-cross-account.js — Cross-account Bitbucket operations.
 *
 * Usage:
 *   node bitbucket-cross-account.js <action>
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
import { BitbucketRestClient as RestClient, enc } from './rest-client.js';

// ═══════════════════════════════════════════════════════════════════════════════
// Action Handlers
// ═══════════════════════════════════════════════════════════════════════════════

/**
 * mirror_pr — Copy a PR's title, description, and reviewer list from one
 * Bitbucket instance to another. Useful when the same code lives in multiple
 * Bitbucket instances (e.g. work server + personal Bitbucket Cloud).
 * The target branch names must already exist in the target repo.
 */
async function mirrorPr(sourceClient, targetClient, args) {
  const pr = await sourceClient.get(
    `/rest/api/1.0/projects/${enc(args.sourceProject)}/repos/${enc(args.sourceRepo)}/pull-requests/${args.prId}`
  );

  const targetProject = args.targetProject || args.sourceProject;
  const targetRepo    = args.targetRepo    || args.sourceRepo;

  const payload = {
    title:       args.title       || pr.title,
    description: args.description || pr.description || '',
    fromRef: {
      id:         args.sourceBranch || pr.fromRef.displayId,
      repository: { slug: targetRepo, project: { key: targetProject } },
    },
    toRef: {
      id:         args.targetBranch || pr.toRef.displayId,
      repository: { slug: targetRepo, project: { key: targetProject } },
    },
    reviewers: (pr.reviewers || []).map(r => ({ user: { name: r.user.name } })),
  };

  const created = await targetClient.post(
    `/rest/api/1.0/projects/${enc(targetProject)}/repos/${enc(targetRepo)}/pull-requests`,
    payload
  );

  return {
    sourcePrId:    args.prId,
    sourceAccount: args.sourceAccount,
    targetPrId:    created.id,
    targetAccount: args.targetAccount,
    title:         created.title,
    targetUrl:     created.links?.self?.[0]?.href,
  };
}

/**
 * copy_pr_comments — Copy all inline and general comments from one PR to another.
 * Useful for preserving review context when mirroring a PR across instances.
 */
async function copyPrComments(sourceClient, targetClient, args) {
  const path = `/rest/api/1.0/projects/${enc(args.sourceProject)}/repos/${enc(args.sourceRepo)}/pull-requests/${args.sourcePrId}/activities`;
  const data = await sourceClient.getPaginated(path);

  const comments = (data.values || []).filter(a => a.action === 'COMMENTED' && a.comment);
  const results = [];

  for (const activity of comments) {
    try {
      const payload = { text: activity.comment.text };
      if (activity.commentAnchor) payload.anchor = activity.commentAnchor;
      await targetClient.post(
        `/rest/api/1.0/projects/${enc(args.targetProject)}/repos/${enc(args.targetRepo)}/pull-requests/${args.targetPrId}/comments`,
        payload
      );
      results.push({ success: true, text: activity.comment.text.slice(0, 60) });
    } catch (e) {
      results.push({ success: false, text: activity.comment.text.slice(0, 60), error: e.message });
    }
  }

  return {
    total:   comments.length,
    copied:  results.filter(r => r.success).length,
    failed:  results.filter(r => !r.success).length,
    results,
  };
}

// ═══════════════════════════════════════════════════════════════════════════════
// Action Registry
// ═══════════════════════════════════════════════════════════════════════════════

const REQUIRED_ARGS = {
  mirror_pr:          ['sourceAccount', 'targetAccount', 'sourceProject', 'sourceRepo', 'prId'],
  copy_pr_comments:   ['sourceAccount', 'targetAccount', 'sourceProject', 'sourceRepo', 'sourcePrId', 'targetProject', 'targetRepo', 'targetPrId'],
};

const ACTION_ROUTER = {
  mirror_pr:        (sc, tc, a) => mirrorPr(sc, tc, a),
  copy_pr_comments: (sc, tc, a) => copyPrComments(sc, tc, a),
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
      error: 'Usage: node bitbucket-cross-account.js <action>\nSet $env:CLI_JSON_ARGS with JSON arguments.',
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
    if (!creds.bitbucketToken) {
      console.log(JSON.stringify({ success: false, error: `Missing BITBUCKET_PAT_TOKEN in .env.${creds.profileId} (${label})` }));
      process.exit(1);
    }
    if (!creds.bitbucketUrl) {
      console.log(JSON.stringify({ success: false, error: `Missing BITBUCKET_BASE_URL in .env.${creds.profileId} (${label})` }));
      process.exit(1);
    }
  }

  const sourceClient = new RestClient(sourceCreds.bitbucketUrl, sourceCreds.bitbucketToken);
  const targetClient = new RestClient(targetCreds.bitbucketUrl, targetCreds.bitbucketToken);

  try {
    const result = await ACTION_ROUTER[action](sourceClient, targetClient, args);
    console.log(JSON.stringify({ success: true, data: result }));
  } catch (e) {
    console.log(JSON.stringify({ success: false, error: e.message || String(e) }));
    process.exit(1);
  }
}

main();
