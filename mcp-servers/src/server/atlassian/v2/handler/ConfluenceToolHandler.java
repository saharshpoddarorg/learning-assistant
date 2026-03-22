package server.atlassian.v2.handler;

import server.atlassian.v2.client.ConfluenceClientV2;
import server.atlassian.v2.model.Product;
import server.atlassian.v2.model.ToolResult;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static server.atlassian.v2.handler.HandlerUtils.*;

/**
 * Handles all Confluence MCP tool calls (22 tools).
 */
public final class ConfluenceToolHandler {

    private static final Logger LOGGER = Logger.getLogger(ConfluenceToolHandler.class.getName());
    private static final Product P = Product.CONFLUENCE;

    private final ConfluenceClientV2 client;

    public ConfluenceToolHandler(final ConfluenceClientV2 client) {
        this.client = Objects.requireNonNull(client);
    }

    public ToolResult handle(final String tool, final Map<String, String> args) {
        return switch (tool) {
            case "confluence_search"             -> search(args);
            case "confluence_get_page"           -> getPage(args);
            case "confluence_get_page_body"      -> getPageBody(args);
            case "confluence_create_page"        -> createPage(args);
            case "confluence_update_page"        -> updatePage(args);
            case "confluence_delete_page"        -> deletePage(args);
            case "confluence_list_spaces"        -> listSpaces();
            case "confluence_get_space"          -> getSpace(args);
            case "confluence_get_page_children"  -> getPageChildren(args);
            case "confluence_get_page_labels"    -> getPageLabels(args);
            case "confluence_add_page_label"     -> addPageLabel(args);
            case "confluence_get_page_comments"  -> getPageComments(args);
            case "confluence_add_page_comment"   -> addPageComment(args);
            case "confluence_get_page_versions"  -> getPageVersions(args);
            case "confluence_move_page"          -> movePage(args);
            case "confluence_get_attachments"    -> getAttachments(args);
            case "confluence_get_restrictions"   -> getRestrictions(args);
            case "confluence_get_properties"     -> getProperties(args);
            case "confluence_set_property"       -> setProperty(args);
            case "confluence_get_templates"      -> getTemplates(args);
            case "confluence_whoami"             -> whoami();
            default -> ToolResult.error(P, tool, "Unknown Confluence tool: " + tool);
        };
    }

    // ── Implementation ───────────────────────────────────────────────────────

    private ToolResult search(final Map<String, String> args) {
        final var query = requireArg(args, "query");
        if (query == null) return ToolResult.error(P, "confluence_search", "Missing required: 'query'");
        final int max = parseMaxResults(args.get("maxResults"));
        final var cql = looksLikeCql(query) ? query : "type = page AND text ~ \"" + escapeJson(query) + "\" ORDER BY lastmodified DESC";
        try { return ToolResult.success(P, "confluence_search", client.searchPages(cql, max)); }
        catch (IOException | InterruptedException ex) { return apiError("confluence_search", ex); }
    }

    private ToolResult getPage(final Map<String, String> args) {
        final var id = requireArg(args, "pageId");
        if (id == null) return ToolResult.error(P, "confluence_get_page", "Missing required: 'pageId'");
        try { return ToolResult.success(P, "confluence_get_page", client.getPage(id)); }
        catch (IOException | InterruptedException ex) { return apiError("confluence_get_page", ex); }
    }

    private ToolResult getPageBody(final Map<String, String> args) {
        final var id = requireArg(args, "pageId");
        if (id == null) return ToolResult.error(P, "confluence_get_page_body", "Missing required: 'pageId'");
        final var format = args.getOrDefault("format", "storage");
        try { return ToolResult.success(P, "confluence_get_page_body", client.getPageBody(id, format)); }
        catch (IOException | InterruptedException ex) { return apiError("confluence_get_page_body", ex); }
    }

    private ToolResult createPage(final Map<String, String> args) {
        final var spaceKey = requireArg(args, "spaceKey");
        final var title = requireArg(args, "title");
        final var content = requireArg(args, "content");
        if (spaceKey == null || title == null) return ToolResult.error(P, "confluence_create_page", "Missing required: 'spaceKey' and 'title'");
        final var body = "{\"spaceId\":\"" + escapeJson(spaceKey) + "\",\"title\":\"" + escapeJson(title)
                + "\",\"body\":{\"representation\":\"storage\",\"value\":\"" + escapeJson(content != null ? content : "") + "\"},\"status\":\"current\"}";
        try { return ToolResult.success(P, "confluence_create_page", client.createPage(body)); }
        catch (IOException | InterruptedException ex) { return apiError("confluence_create_page", ex); }
    }

    private ToolResult updatePage(final Map<String, String> args) {
        final var id = requireArg(args, "pageId");
        final var title = requireArg(args, "title");
        final var content = requireArg(args, "content");
        final var version = requireArg(args, "version");
        if (id == null || title == null || version == null) return ToolResult.error(P, "confluence_update_page", "Missing required: 'pageId', 'title', 'version'");
        final var body = "{\"id\":\"" + escapeJson(id) + "\",\"title\":\"" + escapeJson(title)
                + "\",\"body\":{\"representation\":\"storage\",\"value\":\"" + escapeJson(content != null ? content : "")
                + "\"},\"version\":{\"number\":" + version + "},\"status\":\"current\"}";
        try { return ToolResult.success(P, "confluence_update_page", client.updatePage(id, body)); }
        catch (IOException | InterruptedException ex) { return apiError("confluence_update_page", ex); }
    }

    private ToolResult deletePage(final Map<String, String> args) {
        final var id = requireArg(args, "pageId");
        if (id == null) return ToolResult.error(P, "confluence_delete_page", "Missing required: 'pageId'");
        try { return ToolResult.success(P, "confluence_delete_page", client.deletePage(id)); }
        catch (IOException | InterruptedException ex) { return apiError("confluence_delete_page", ex); }
    }

    private ToolResult listSpaces() {
        try { return ToolResult.success(P, "confluence_list_spaces", client.listSpaces()); }
        catch (IOException | InterruptedException ex) { return apiError("confluence_list_spaces", ex); }
    }

    private ToolResult getSpace(final Map<String, String> args) {
        final var key = requireArg(args, "spaceKey");
        if (key == null) return ToolResult.error(P, "confluence_get_space", "Missing required: 'spaceKey'");
        try { return ToolResult.success(P, "confluence_get_space", client.getSpace(key)); }
        catch (IOException | InterruptedException ex) { return apiError("confluence_get_space", ex); }
    }

    private ToolResult getPageChildren(final Map<String, String> args) {
        final var id = requireArg(args, "pageId");
        if (id == null) return ToolResult.error(P, "confluence_get_page_children", "Missing required: 'pageId'");
        try { return ToolResult.success(P, "confluence_get_page_children", client.getPageChildren(id, parseMaxResults(args.get("maxResults")))); }
        catch (IOException | InterruptedException ex) { return apiError("confluence_get_page_children", ex); }
    }

    private ToolResult getPageLabels(final Map<String, String> args) {
        final var id = requireArg(args, "pageId");
        if (id == null) return ToolResult.error(P, "confluence_get_page_labels", "Missing required: 'pageId'");
        try { return ToolResult.success(P, "confluence_get_page_labels", client.getPageLabels(id)); }
        catch (IOException | InterruptedException ex) { return apiError("confluence_get_page_labels", ex); }
    }

    private ToolResult addPageLabel(final Map<String, String> args) {
        final var id = requireArg(args, "pageId");
        final var label = requireArg(args, "label");
        if (id == null || label == null) return ToolResult.error(P, "confluence_add_page_label", "Missing required: 'pageId' and 'label'");
        try { return ToolResult.success(P, "confluence_add_page_label", client.addPageLabel(id, "[{\"prefix\":\"global\",\"name\":\"" + escapeJson(label) + "\"}]")); }
        catch (IOException | InterruptedException ex) { return apiError("confluence_add_page_label", ex); }
    }

    private ToolResult getPageComments(final Map<String, String> args) {
        final var id = requireArg(args, "pageId");
        if (id == null) return ToolResult.error(P, "confluence_get_page_comments", "Missing required: 'pageId'");
        try { return ToolResult.success(P, "confluence_get_page_comments", client.getPageComments(id, parseMaxResults(args.get("maxResults")))); }
        catch (IOException | InterruptedException ex) { return apiError("confluence_get_page_comments", ex); }
    }

    private ToolResult addPageComment(final Map<String, String> args) {
        final var id = requireArg(args, "pageId");
        final var comment = requireArg(args, "comment");
        if (id == null || comment == null) return ToolResult.error(P, "confluence_add_page_comment", "Missing required: 'pageId' and 'comment'");
        final var body = "{\"body\":{\"representation\":\"storage\",\"value\":\"" + escapeJson(comment) + "\"}}";
        try { return ToolResult.success(P, "confluence_add_page_comment", client.addPageComment(id, body)); }
        catch (IOException | InterruptedException ex) { return apiError("confluence_add_page_comment", ex); }
    }

    private ToolResult getPageVersions(final Map<String, String> args) {
        final var id = requireArg(args, "pageId");
        if (id == null) return ToolResult.error(P, "confluence_get_page_versions", "Missing required: 'pageId'");
        try { return ToolResult.success(P, "confluence_get_page_versions", client.getPageVersions(id)); }
        catch (IOException | InterruptedException ex) { return apiError("confluence_get_page_versions", ex); }
    }

    private ToolResult movePage(final Map<String, String> args) {
        final var id = requireArg(args, "pageId");
        final var targetId = requireArg(args, "targetPageId");
        if (id == null || targetId == null) return ToolResult.error(P, "confluence_move_page", "Missing required: 'pageId' and 'targetPageId'");
        final var position = args.getOrDefault("position", "append");
        final var body = "{\"pageId\":\"" + escapeJson(targetId) + "\",\"position\":\"" + escapeJson(position) + "\"}";
        try { return ToolResult.success(P, "confluence_move_page", client.movePage(id, body)); }
        catch (IOException | InterruptedException ex) { return apiError("confluence_move_page", ex); }
    }

    private ToolResult getAttachments(final Map<String, String> args) {
        final var id = requireArg(args, "pageId");
        if (id == null) return ToolResult.error(P, "confluence_get_attachments", "Missing required: 'pageId'");
        try { return ToolResult.success(P, "confluence_get_attachments", client.getPageAttachments(id)); }
        catch (IOException | InterruptedException ex) { return apiError("confluence_get_attachments", ex); }
    }

    private ToolResult getRestrictions(final Map<String, String> args) {
        final var id = requireArg(args, "pageId");
        if (id == null) return ToolResult.error(P, "confluence_get_restrictions", "Missing required: 'pageId'");
        try { return ToolResult.success(P, "confluence_get_restrictions", client.getPageRestrictions(id)); }
        catch (IOException | InterruptedException ex) { return apiError("confluence_get_restrictions", ex); }
    }

    private ToolResult getProperties(final Map<String, String> args) {
        final var id = requireArg(args, "pageId");
        if (id == null) return ToolResult.error(P, "confluence_get_properties", "Missing required: 'pageId'");
        try { return ToolResult.success(P, "confluence_get_properties", client.getContentProperties(id)); }
        catch (IOException | InterruptedException ex) { return apiError("confluence_get_properties", ex); }
    }

    private ToolResult setProperty(final Map<String, String> args) {
        final var id = requireArg(args, "pageId");
        final var key = requireArg(args, "key");
        final var value = requireArg(args, "value");
        if (id == null || key == null || value == null) return ToolResult.error(P, "confluence_set_property", "Missing required: 'pageId', 'key', 'value'");
        final var body = "{\"key\":\"" + escapeJson(key) + "\",\"value\":" + value + "}";
        try { return ToolResult.success(P, "confluence_set_property", client.setContentProperty(id, body)); }
        catch (IOException | InterruptedException ex) { return apiError("confluence_set_property", ex); }
    }

    private ToolResult getTemplates(final Map<String, String> args) {
        final var spaceKey = requireArg(args, "spaceKey");
        if (spaceKey == null) return ToolResult.error(P, "confluence_get_templates", "Missing required: 'spaceKey'");
        try { return ToolResult.success(P, "confluence_get_templates", client.getTemplates(spaceKey)); }
        catch (IOException | InterruptedException ex) { return apiError("confluence_get_templates", ex); }
    }

    private ToolResult whoami() {
        try { return ToolResult.success(P, "confluence_whoami", client.getCurrentUser()); }
        catch (IOException | InterruptedException ex) { return apiError("confluence_whoami", ex); }
    }

    private ToolResult apiError(final String tool, final Exception ex) {
        LOGGER.log(Level.WARNING, "Confluence API error in " + tool, ex);
        return ToolResult.error(P, tool, "Confluence API error: " + ex.getMessage());
    }
}
