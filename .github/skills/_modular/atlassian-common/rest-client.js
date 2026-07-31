#!/usr/bin/env node
/**
 * rest-client.js — Shared Atlassian REST client.
 *
 * Exports:
 *   RestClient           — base PAT-authenticated HTTP client (all three tools)
 *   BitbucketRestClient  — extends RestClient with Bitbucket Server pagination
 *   enc                  — encodeURIComponent shorthand
 *
 * TLS: importing this module automatically suppresses TLS errors for
 * corporate/self-signed certificates (same behaviour as the original CLIs).
 */

// ═══════════════════════════════════════════════════════════════════════════════
// TLS Configuration - handle corporate/self-signed certificates
// ═══════════════════════════════════════════════════════════════════════════════
if (!process.env.NODE_EXTRA_CA_CERTS && !process.env.NODE_TLS_REJECT_UNAUTHORIZED) {
  process.env.NODE_TLS_REJECT_UNAUTHORIZED = '0';
}

// ═══════════════════════════════════════════════════════════════════════════════
// Helpers
// ═══════════════════════════════════════════════════════════════════════════════

export function enc(s) { return encodeURIComponent(s); }

// ═══════════════════════════════════════════════════════════════════════════════
// Base REST Client (Jira, Confluence, Bitbucket)
// ═══════════════════════════════════════════════════════════════════════════════

export class RestClient {
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
// Bitbucket REST Client — adds Bitbucket Server pagination
// ═══════════════════════════════════════════════════════════════════════════════

export class BitbucketRestClient extends RestClient {
  /** Paginated GET for Bitbucket Server REST API (uses isLastPage/nextPageStart). */
  async getPaginated(path, maxPages = 5) {
    let allValues = [];
    let start = 0;
    for (let page = 0; page < maxPages; page++) {
      const separator = path.includes('?') ? '&' : '?';
      const data = await this.get(`${path}${separator}start=${start}&limit=500`);
      if (!Array.isArray(data.values) && !data.hasOwnProperty('isLastPage')) return data;
      if (data.values) allValues = allValues.concat(data.values);
      if (data.isLastPage !== false) break;
      start = data.nextPageStart || (start + (data.limit || 500));
    }
    return { values: allValues, size: allValues.length };
  }
}
