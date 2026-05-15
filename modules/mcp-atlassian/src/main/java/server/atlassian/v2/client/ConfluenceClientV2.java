package server.atlassian.v2.client;

import server.atlassian.v2.auth.AuthManager;

import java.io.IOException;

/**
 * Confluence REST API v2 client — comprehensive coverage.
 */
public final class ConfluenceClientV2 {

    private final HttpClientV2 http;

    public ConfluenceClientV2(final String baseUrl, final AuthManager auth, final int timeoutMs) {
        this.http = new HttpClientV2(baseUrl, auth, timeoutMs);
    }

    // ── Search ───────────────────────────────────────────────────────────────

    public String searchPages(final String cql, final int maxResults) throws IOException, InterruptedException {
        return http.get("/api/v2/search?cql=" + encode(cql) + "&limit=" + maxResults);
    }

    // ── Pages ────────────────────────────────────────────────────────────────

    public String getPage(final String pageId) throws IOException, InterruptedException {
        return http.get("/api/v2/pages/" + encode(pageId) + "?body-format=storage");
    }

    public String getPageBody(final String pageId, final String format) throws IOException, InterruptedException {
        return http.get("/api/v2/pages/" + encode(pageId) + "?body-format=" + encode(format));
    }

    public String createPage(final String jsonBody) throws IOException, InterruptedException {
        return http.post("/api/v2/pages", jsonBody);
    }

    public String updatePage(final String pageId, final String jsonBody) throws IOException, InterruptedException {
        return http.put("/api/v2/pages/" + encode(pageId), jsonBody);
    }

    public String deletePage(final String pageId) throws IOException, InterruptedException {
        return http.delete("/api/v2/pages/" + encode(pageId));
    }

    public String getPageChildren(final String pageId, final int maxResults) throws IOException, InterruptedException {
        return http.get("/api/v2/pages/" + encode(pageId) + "/children?limit=" + maxResults);
    }

    public String getPageVersions(final String pageId) throws IOException, InterruptedException {
        return http.get("/api/v2/pages/" + encode(pageId) + "/versions");
    }

    public String movePage(final String pageId, final String jsonBody) throws IOException, InterruptedException {
        return http.put("/api/v2/pages/" + encode(pageId) + "/move", jsonBody);
    }

    // ── Labels ───────────────────────────────────────────────────────────────

    public String getPageLabels(final String pageId) throws IOException, InterruptedException {
        return http.get("/api/v2/pages/" + encode(pageId) + "/labels");
    }

    public String addPageLabel(final String pageId, final String jsonBody) throws IOException, InterruptedException {
        return http.post("/api/v2/pages/" + encode(pageId) + "/labels", jsonBody);
    }

    // ── Comments ─────────────────────────────────────────────────────────────

    public String getPageComments(final String pageId, final int maxResults) throws IOException, InterruptedException {
        return http.get("/api/v2/pages/" + encode(pageId) + "/footer-comments?limit=" + maxResults);
    }

    public String addPageComment(final String pageId, final String jsonBody) throws IOException, InterruptedException {
        return http.post("/api/v2/pages/" + encode(pageId) + "/footer-comments", jsonBody);
    }

    // ── Spaces ───────────────────────────────────────────────────────────────

    public String listSpaces() throws IOException, InterruptedException {
        return http.get("/api/v2/spaces?limit=100");
    }

    public String getSpace(final String spaceKey) throws IOException, InterruptedException {
        return http.get("/api/v2/spaces?keys=" + encode(spaceKey));
    }

    // ── Attachments ──────────────────────────────────────────────────────────

    public String getPageAttachments(final String pageId) throws IOException, InterruptedException {
        return http.get("/api/v2/pages/" + encode(pageId) + "/attachments");
    }

    // ── Content Properties ───────────────────────────────────────────────────

    public String getContentProperties(final String pageId) throws IOException, InterruptedException {
        return http.get("/api/v2/pages/" + encode(pageId) + "/properties");
    }

    public String setContentProperty(final String pageId, final String jsonBody) throws IOException, InterruptedException {
        return http.post("/api/v2/pages/" + encode(pageId) + "/properties", jsonBody);
    }

    // ── Restrictions (Permissions) ───────────────────────────────────────────

    public String getPageRestrictions(final String pageId) throws IOException, InterruptedException {
        return http.get("/api/v2/pages/" + encode(pageId) + "/restrictions");
    }

    // ── Templates ────────────────────────────────────────────────────────────

    public String getTemplates(final String spaceKey) throws IOException, InterruptedException {
        return http.get("/api/v2/spaces/" + encode(spaceKey) + "/templates");
    }

    // ── User ─────────────────────────────────────────────────────────────────

    public String getCurrentUser() throws IOException, InterruptedException {
        return http.get("/api/v2/users/current");
    }

    private static String encode(final String value) {
        return java.net.URLEncoder.encode(value, java.nio.charset.StandardCharsets.UTF_8);
    }
}
