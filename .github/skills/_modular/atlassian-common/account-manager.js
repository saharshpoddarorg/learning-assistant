#!/usr/bin/env node
/**
 * account-manager.js — Multi-account profile & credential management.
 *
 * Provides 6 core functions used by jira_cli.js, bitbucket_cli.js, confluence_cli.js.
 * The CLIs never read .env files directly — all credential loading goes through here.
 *
 * Profile naming convention:
 *   work.primary, work.secondary, work.client-acme
 *   personal-work.primary, personal-work.secondary
 *   personal.primary
 *
 * .env file loading order (later files override earlier):
 *   .env               → global defaults (DEFAULT_ATLASSIAN_PROFILE etc.)
 *   .env.<profileId>   → profile credentials (JIRA_PAT_TOKEN, JIRA_BASE_URL, etc.)
 *   $ENV_FILE          → explicit override path (optional)
 *
 * Account selection priority (highest to lowest):
 *   1. Explicit profileId passed to loadProfileCredentials()
 *   2. SESSION_ACTIVE_PROFILE env var (set by switchProfile())
 *   3. DEFAULT_ATLASSIAN_PROFILE in .env (defaults to "work")
 *   4. Hardcoded fallback: "work"
 */

import { readFileSync, existsSync, readdirSync } from 'fs';
import { resolve, dirname } from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

// ═══════════════════════════════════════════════════════════════════════════════
// Workspace root detection — walk up from this file's location
// ═══════════════════════════════════════════════════════════════════════════════

function findWorkspaceRoot() {
  // account-manager.js lives at .github/skills/_modular/atlassian-common/
  // workspace root is 3 levels up
  const candidates = [
    resolve(__dirname, '..', '..', '..'),        // <workspace>/ (standard)
    resolve(__dirname, '..', '..', '..', '..'),  // one more level up (fallback)
  ];
  for (const dir of candidates) {
    if (existsSync(resolve(dir, '.git')) || existsSync(resolve(dir, '.github'))) {
      return dir;
    }
  }
  return candidates[0];
}

const WORKSPACE_ROOT = findWorkspaceRoot();

// ═══════════════════════════════════════════════════════════════════════════════
// Low-level .env file parser
// ═══════════════════════════════════════════════════════════════════════════════

function parseEnvFile(filePath) {
  if (!existsSync(filePath)) return {};
  const lines = readFileSync(filePath, 'utf8').split('\n');
  const env = {};
  for (const line of lines) {
    const trimmed = line.trim();
    if (!trimmed || trimmed.startsWith('#')) continue;
    const eqIdx = trimmed.indexOf('=');
    if (eqIdx === -1) continue;
    const key = trimmed.substring(0, eqIdx).trim();
    let value = trimmed.substring(eqIdx + 1).trim();
    if ((value.startsWith('"') && value.endsWith('"')) ||
        (value.startsWith("'") && value.endsWith("'"))) {
      value = value.slice(1, -1);
    }
    if (key && value) env[key] = value;
  }
  return env;
}

// ═══════════════════════════════════════════════════════════════════════════════
// Global .env (defaults only — no secrets)
// ═══════════════════════════════════════════════════════════════════════════════

function loadGlobalEnv() {
  const globalEnvPath = resolve(WORKSPACE_ROOT, '.env');
  return parseEnvFile(globalEnvPath);
}

// ═══════════════════════════════════════════════════════════════════════════════
// Profile ID resolution
// ═══════════════════════════════════════════════════════════════════════════════

function resolveProfileId(profileIdOverride) {
  if (profileIdOverride) return normalizeProfileId(profileIdOverride);

  // Check session-level override (set by switchProfile)
  if (process.env.SESSION_ACTIVE_PROFILE) {
    return normalizeProfileId(process.env.SESSION_ACTIVE_PROFILE);
  }

  // Read global .env for default
  const globalEnv = loadGlobalEnv();
  if (globalEnv.SESSION_ACTIVE_PROFILE) {
    return normalizeProfileId(globalEnv.SESSION_ACTIVE_PROFILE);
  }
  const defaultType = globalEnv.DEFAULT_ATLASSIAN_PROFILE || 'work';
  const defaultName = globalEnv[`DEFAULT_ACCOUNT_FOR_TYPE_${defaultType.replace(/-/g, '_')}`] || 'primary';
  return `${defaultType}.${defaultName}`;
}

// Normalise shorthand: "work" → "work.primary", "personal-work" → "personal-work.primary"
function normalizeProfileId(profileId) {
  if (!profileId.includes('.')) return `${profileId}.primary`;
  return profileId;
}

// ═══════════════════════════════════════════════════════════════════════════════
// 1. getActiveProfile() → string
// ═══════════════════════════════════════════════════════════════════════════════

export function getActiveProfile() {
  return resolveProfileId(null);
}

// ═══════════════════════════════════════════════════════════════════════════════
// 2. loadProfileCredentials(profileId?) → { jiraToken, jiraUrl, bitbucketToken,
//                                           bitbucketUrl, confluenceToken, confluenceUrl }
// ═══════════════════════════════════════════════════════════════════════════════

export function loadProfileCredentials(profileIdOverride) {
  const profileId = resolveProfileId(profileIdOverride);
  const profileEnvPath = resolve(WORKSPACE_ROOT, `.env.${profileId}`);

  if (!existsSync(profileEnvPath)) {
    throw new Error(
      `Profile '${profileId}' not configured.\n` +
      `Expected credentials file: ${profileEnvPath}\n` +
      `Create it with: JIRA_PAT_TOKEN, JIRA_BASE_URL, BITBUCKET_PAT_TOKEN, ` +
      `BITBUCKET_BASE_URL, CONFLUENCE_PAT_TOKEN, CONFLUENCE_BASE_URL`
    );
  }

  // Allow optional override via ENV_FILE env var
  const overridePath = process.env.ENV_FILE && existsSync(process.env.ENV_FILE)
    ? process.env.ENV_FILE : null;

  const env = {
    ...parseEnvFile(profileEnvPath),
    ...(overridePath ? parseEnvFile(overridePath) : {}),
  };

  return {
    profileId,
    jiraToken: env.JIRA_PAT_TOKEN || null,
    jiraUrl: env.JIRA_BASE_URL || null,
    bitbucketToken: env.BITBUCKET_PAT_TOKEN || null,
    bitbucketUrl: env.BITBUCKET_BASE_URL || null,
    confluenceToken: env.CONFLUENCE_PAT_TOKEN || null,
    confluenceUrl: env.CONFLUENCE_BASE_URL || null,
  };
}

// ═══════════════════════════════════════════════════════════════════════════════
// 3. switchProfile(profileId) → void
// ═══════════════════════════════════════════════════════════════════════════════

export function switchProfile(profileId) {
  const normalized = normalizeProfileId(profileId);
  // Validate the profile exists before switching
  const profileEnvPath = resolve(WORKSPACE_ROOT, `.env.${normalized}`);
  if (!existsSync(profileEnvPath)) {
    throw new Error(
      `Cannot switch to profile '${normalized}': credentials file not found.\n` +
      `Expected: ${profileEnvPath}`
    );
  }
  process.env.SESSION_ACTIVE_PROFILE = normalized;
}

// ═══════════════════════════════════════════════════════════════════════════════
// 4. validateProfileCredentials(profileId?) → { valid, profileId, missing, errors }
// ═══════════════════════════════════════════════════════════════════════════════

export async function validateProfileCredentials(profileIdOverride) {
  const profileId = resolveProfileId(profileIdOverride);
  let creds;
  try {
    creds = loadProfileCredentials(profileId);
  } catch (e) {
    return { valid: false, profileId, missing: ['credentials file'], errors: [e.message] };
  }

  const missing = [];
  if (!creds.jiraToken) missing.push('JIRA_PAT_TOKEN');
  if (!creds.jiraUrl) missing.push('JIRA_BASE_URL');
  if (!creds.bitbucketToken) missing.push('BITBUCKET_PAT_TOKEN');
  if (!creds.bitbucketUrl) missing.push('BITBUCKET_BASE_URL');
  if (!creds.confluenceToken) missing.push('CONFLUENCE_PAT_TOKEN');
  if (!creds.confluenceUrl) missing.push('CONFLUENCE_BASE_URL');

  return {
    valid: missing.length === 0,
    profileId,
    missing,
    errors: missing.length > 0
      ? [`Missing required variables in .env.${profileId}: ${missing.join(', ')}`]
      : [],
  };
}

// ═══════════════════════════════════════════════════════════════════════════════
// 5. listAvailableProfiles() → string[]
// ═══════════════════════════════════════════════════════════════════════════════

export function listAvailableProfiles() {
  let entries;
  try {
    entries = readdirSync(WORKSPACE_ROOT);
  } catch {
    return [];
  }
  return entries
    .filter(f => f.startsWith('.env.') && !f.endsWith('.example') && !f.endsWith('.example.work'))
    .map(f => f.replace(/^\.env\./, ''))
    .sort();
}

// ═══════════════════════════════════════════════════════════════════════════════
// 6. getProfileInfo(profileId?) → { type, name, fullId, envFilePath, exists }
// ═══════════════════════════════════════════════════════════════════════════════

export function getProfileInfo(profileIdOverride) {
  const profileId = resolveProfileId(profileIdOverride);
  const [type, ...nameParts] = profileId.split('.');
  const name = nameParts.join('.') || 'primary';
  const envFilePath = resolve(WORKSPACE_ROOT, `.env.${profileId}`);
  const exists = existsSync(envFilePath);

  return {
    type,
    name,
    fullId: profileId,
    envFilePath,
    exists,
    activeProfile: getActiveProfile(),
  };
}
