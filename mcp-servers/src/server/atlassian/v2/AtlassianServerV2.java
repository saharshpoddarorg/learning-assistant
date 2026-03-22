package server.atlassian.v2;

import server.atlassian.v2.auth.AuthManager;
import server.atlassian.v2.client.BitbucketClientV2;
import server.atlassian.v2.client.ConfluenceClientV2;
import server.atlassian.v2.client.JiraClientV2;
import server.atlassian.v2.config.ConfigLoaderV2;
import server.atlassian.v2.config.ServerConfigV2;
import server.atlassian.v2.handler.AuthToolHandler;
import server.atlassian.v2.handler.BitbucketToolHandler;
import server.atlassian.v2.handler.ConfluenceToolHandler;
import server.atlassian.v2.handler.CrossProductHandler;
import server.atlassian.v2.handler.JiraToolHandler;
import server.atlassian.v2.handler.ToolDispatcher;
import server.atlassian.v2.model.ToolResult;
import server.atlassian.common.JsonExtractor;
import server.McpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Entry point for the Atlassian MCP Server v2.
 *
 * <p>A comprehensive, exhaustive gateway to Jira, Confluence, and Bitbucket
 * via the Model Context Protocol. Replaces the v1 {@code AtlassianServer}
 * with expanded tool coverage, proper JSON input schemas, and dual auth
 * support (API token / PAT <em>and</em> browser-based OAuth 2.0).
 *
 * <p><strong>Capabilities (93 tools):</strong>
 * <ul>
 *   <li><strong>Jira (34):</strong> search, get, create, update, transition, delete,
 *       comments, watchers, links, worklogs, components, versions, boards, sprints,
 *       users, issue types, priorities, statuses, bulk create, dashboards, filters,
 *       my issues, attachments, whoami</li>
 *   <li><strong>Confluence (21):</strong> search, get page/body, create, update, delete,
 *       spaces, children, labels, comments, versions, move, attachments, restrictions,
 *       properties, templates, whoami</li>
 *   <li><strong>Bitbucket (31):</strong> repos, PRs (CRUD + diff + comments + approve +
 *       decline + merge), code search, branches, tags, commits, diffs, file content,
 *       projects, pipelines, environments, deployments, snippets, whoami</li>
 *   <li><strong>Cross-product (3):</strong> unified search, status, whoami</li>
 *   <li><strong>Auth (4):</strong> browser login, status, logout, switch method</li>
 * </ul>
 *
 * <p><strong>Transport:</strong> STDIO (JSON-RPC 2.0 on stdin/stdout).
 *
 * <p><strong>Usage:</strong>
 * <pre>
 *   java -cp out server.atlassian.v2.AtlassianServerV2
 *   java -cp out server.atlassian.v2.AtlassianServerV2 --list-tools
 *   java -cp out server.atlassian.v2.AtlassianServerV2 --demo
 * </pre>
 */
public final class AtlassianServerV2 implements McpServer {

    private static final Logger LOGGER = Logger.getLogger(AtlassianServerV2.class.getName());
    static final String SERVER_NAME = "atlassian";
    static final String SERVER_VERSION = "2.0.0";

    private final ToolDispatcher dispatcher;
    private final ServerConfigV2 config;
    private volatile boolean isRunning;

    /**
     * Creates the server from fully-resolved config and wired components.
     */
    public AtlassianServerV2(final ServerConfigV2 config, final ToolDispatcher dispatcher) {
        this.config = config;
        this.dispatcher = dispatcher;
        this.isRunning = false;
    }

    @Override
    public String name() { return SERVER_NAME; }

    @Override
    public String version() { return SERVER_VERSION; }

    @Override
    public Map<String, String> toolDefinitions() { return getToolDefinitions(); }

    @Override
    public String handleToolCall(final String toolName, final Map<String, String> args) {
        final var result = dispatcher.dispatch(toolName, args);
        return result.text();
    }

    // ── STDIO Loop ───────────────────────────────────────────────────────────

    /**
     * Starts the MCP server, listening on stdin for JSON-RPC 2.0 messages.
     */
    @Override
    public void start() {
        LOGGER.info("Starting " + SERVER_NAME + " v" + SERVER_VERSION
                + " — Jira + Confluence + Bitbucket MCP Server (v2)");
        isRunning = true;

        try (var reader = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            while (isRunning) {
                final var line = reader.readLine();
                if (line == null) {
                    LOGGER.info("EOF on stdin — shutting down.");
                    break;
                }
                if (line.isBlank()) continue;
                processMessage(line.trim());
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "I/O error reading from stdin", ex);
        }

        LOGGER.info(SERVER_NAME + " v" + SERVER_VERSION + " stopped.");
    }

    /** Signals the server to stop gracefully. */
    @Override
    public void stop() { isRunning = false; }

    // ── JSON-RPC Dispatch ────────────────────────────────────────────────────

    private void processMessage(final String message) {
        LOGGER.fine("Received: " + message);

        final var method = JsonExtractor.stringOrDefault(message, "method", "");
        final var id     = JsonExtractor.rawValue(message, "id");

        final String response = switch (method) {
            case "initialize" -> handleInitialize(id);
            case "tools/list" -> handleToolsList(id);
            case "tools/call" -> handleToolsCall(id, message);
            default           -> jsonRpcError(id, -32601, "Method not found: " + method);
        };

        System.out.println(response);
        System.out.flush();
    }

    private static String handleInitialize(final String id) {
        return String.format(
            "{\"jsonrpc\":\"2.0\",\"id\":%s,\"result\":{"
            + "\"protocolVersion\":\"2024-11-05\","
            + "\"capabilities\":{\"tools\":{}},"
            + "\"serverInfo\":{\"name\":\"%s\",\"version\":\"%s\"}}}",
            id, SERVER_NAME, SERVER_VERSION);
    }

    private String handleToolsList(final String id) {
        final var sb = new StringBuilder(16_384);
        sb.append("{\"jsonrpc\":\"2.0\",\"id\":").append(id)
          .append(",\"result\":{\"tools\":[");

        var first = true;
        for (final var schema : TOOL_SCHEMAS) {
            if (!first) sb.append(',');
            first = false;
            sb.append(schema);
        }

        sb.append("]}}");
        return sb.toString();
    }

    private String handleToolsCall(final String id, final String message) {
        final var params   = JsonExtractor.block(message, "params").orElse(null);
        final var toolName = params == null ? null
                : JsonExtractor.string(params, "name").orElse(null);

        if (toolName == null || toolName.isBlank()) {
            return jsonRpcError(id, -32602, "tools/call missing required param: name");
        }

        final Map<String, String> arguments = params == null
                ? Map.of()
                : JsonExtractor.block(params, "arguments")
                        .map(JsonExtractor::extractArgumentMap)
                        .orElse(Map.of());

        final ToolResult result = dispatcher.dispatch(toolName, arguments);
        final String text = escapeJson(result.text());

        return String.format(
            "{\"jsonrpc\":\"2.0\",\"id\":%s,\"result\":{\"content\":["
            + "{\"type\":\"text\",\"text\":\"%s\"}]}}",
            id, text);
    }

    // ── Tool Definitions (simple map for --list-tools display) ───────────────

    /**
     * Returns a description-only map for display purposes (--list-tools).
     */
    public Map<String, String> getToolDefinitions() {
        final var tools = new LinkedHashMap<String, String>();

        // ── Jira (34) ────────────────────────────────────────────────────────
        tools.put("jira_search_issues",     "Search Jira issues using JQL or free text");
        tools.put("jira_get_issue",         "Get full details of a Jira issue by key");
        tools.put("jira_create_issue",      "Create a new Jira issue");
        tools.put("jira_update_issue",      "Update fields on an existing Jira issue");
        tools.put("jira_transition_issue",  "Move a Jira issue to a new status");
        tools.put("jira_delete_issue",      "Delete a Jira issue permanently");
        tools.put("jira_list_projects",     "List all accessible Jira projects");
        tools.put("jira_get_project",       "Get details of a specific Jira project");
        tools.put("jira_get_sprint",        "Get the active sprint for a Jira board");
        tools.put("jira_get_sprint_issues", "Get all issues in a sprint");
        tools.put("jira_add_comment",       "Add a comment to a Jira issue");
        tools.put("jira_get_comments",      "Get comments on a Jira issue");
        tools.put("jira_assign_issue",      "Assign a Jira issue to a user");
        tools.put("jira_get_transitions",   "Get available workflow transitions for an issue");
        tools.put("jira_search_users",      "Search for Jira users by name or email");
        tools.put("jira_link_issues",       "Create a link between two Jira issues");
        tools.put("jira_add_worklog",       "Log time spent on a Jira issue");
        tools.put("jira_get_boards",        "List Jira boards (Scrum/Kanban)");
        tools.put("jira_get_components",    "List components for a Jira project");
        tools.put("jira_create_component",  "Create a new component in a Jira project");
        tools.put("jira_get_versions",      "List fix versions for a Jira project");
        tools.put("jira_create_version",    "Create a new fix version in a Jira project");
        tools.put("jira_get_watchers",      "Get all watchers on a Jira issue");
        tools.put("jira_add_watcher",       "Add a watcher to a Jira issue");
        tools.put("jira_remove_watcher",    "Remove a watcher from a Jira issue");
        tools.put("jira_get_issue_types",   "List available issue types for a project");
        tools.put("jira_get_priorities",    "List all priority levels");
        tools.put("jira_get_statuses",      "List all statuses");
        tools.put("jira_bulk_create_issues","Create multiple Jira issues in one call");
        tools.put("jira_list_dashboards",   "List Jira dashboards");
        tools.put("jira_get_filters",       "List shared/favourite Jira filters");
        tools.put("jira_get_my_issues",     "Get issues assigned to the current user");
        tools.put("jira_get_attachments",   "Get attachments on a Jira issue");
        tools.put("jira_whoami",            "Get current Jira user info");

        // ── Confluence (22) ──────────────────────────────────────────────────
        tools.put("confluence_search",             "Search Confluence pages using CQL or free text");
        tools.put("confluence_get_page",           "Get metadata of a Confluence page");
        tools.put("confluence_get_page_body",      "Get the body content of a Confluence page");
        tools.put("confluence_create_page",        "Create a new Confluence page");
        tools.put("confluence_update_page",        "Update an existing Confluence page");
        tools.put("confluence_delete_page",        "Delete a Confluence page");
        tools.put("confluence_list_spaces",        "List all accessible Confluence spaces");
        tools.put("confluence_get_space",          "Get details of a Confluence space");
        tools.put("confluence_get_page_children",  "Get child pages of a Confluence page");
        tools.put("confluence_get_page_labels",    "Get labels on a Confluence page");
        tools.put("confluence_add_page_label",     "Add a label to a Confluence page");
        tools.put("confluence_get_page_comments",  "Get comments on a Confluence page");
        tools.put("confluence_add_page_comment",   "Add a comment to a Confluence page");
        tools.put("confluence_get_page_versions",  "Get version history of a Confluence page");
        tools.put("confluence_move_page",          "Move a Confluence page to a new parent or space");
        tools.put("confluence_get_attachments",    "Get attachments on a Confluence page");
        tools.put("confluence_get_restrictions",   "Get access restrictions on a Confluence page");
        tools.put("confluence_get_properties",     "Get custom properties on a Confluence page");
        tools.put("confluence_set_property",       "Set a custom property on a Confluence page");
        tools.put("confluence_get_templates",      "List available Confluence page templates");
        tools.put("confluence_whoami",             "Get current Confluence user info");

        // ── Bitbucket (31) ───────────────────────────────────────────────────
        tools.put("bitbucket_list_repos",           "List repositories in a Bitbucket workspace");
        tools.put("bitbucket_get_repo",             "Get details of a Bitbucket repository");
        tools.put("bitbucket_list_pull_requests",   "List pull requests for a repository");
        tools.put("bitbucket_get_pull_request",     "Get full details of a pull request");
        tools.put("bitbucket_create_pull_request",  "Create a new pull request");
        tools.put("bitbucket_get_pull_request_diff","Get the diff of a pull request");
        tools.put("bitbucket_get_pr_comments",      "Get comments on a pull request");
        tools.put("bitbucket_add_pr_comment",       "Add a comment to a pull request");
        tools.put("bitbucket_approve_pull_request", "Approve a pull request");
        tools.put("bitbucket_decline_pull_request", "Decline a pull request");
        tools.put("bitbucket_merge_pull_request",   "Merge a pull request");
        tools.put("bitbucket_search_code",          "Search code across Bitbucket repositories");
        tools.put("bitbucket_list_branches",        "List branches in a repository");
        tools.put("bitbucket_create_branch",        "Create a new branch");
        tools.put("bitbucket_delete_branch",        "Delete a branch");
        tools.put("bitbucket_list_tags",            "List tags in a repository");
        tools.put("bitbucket_create_tag",           "Create a new tag");
        tools.put("bitbucket_get_commits",          "Get recent commits in a repository");
        tools.put("bitbucket_get_commit",           "Get details of a specific commit");
        tools.put("bitbucket_get_diff",             "Get diff between two commits or branches");
        tools.put("bitbucket_get_file_content",     "Get file content from a repository");
        tools.put("bitbucket_list_projects",        "List projects in a Bitbucket workspace");
        tools.put("bitbucket_list_pipelines",       "List pipeline runs for a repository");
        tools.put("bitbucket_get_pipeline",         "Get details of a specific pipeline run");
        tools.put("bitbucket_trigger_pipeline",     "Trigger a new pipeline run");
        tools.put("bitbucket_stop_pipeline",        "Stop a running pipeline");
        tools.put("bitbucket_list_environments",    "List deployment environments for a repository");
        tools.put("bitbucket_list_deployments",     "List deployments for a repository");
        tools.put("bitbucket_list_snippets",        "List code snippets in a workspace");
        tools.put("bitbucket_create_snippet",       "Create a new code snippet");
        tools.put("bitbucket_whoami",               "Get current Bitbucket user info");

        // ── Cross-product (3) ────────────────────────────────────────────────
        tools.put("atlassian_unified_search", "Search across Jira, Confluence, and Bitbucket");
        tools.put("atlassian_status",         "Check connectivity to all Atlassian products");
        tools.put("atlassian_whoami",         "Get current user across all products");

        // ── Auth (4) ─────────────────────────────────────────────────────────
        tools.put("auth_login_browser",  "Start browser-based OAuth 2.0 login");
        tools.put("auth_status",         "Check current authentication status");
        tools.put("auth_logout",         "Clear stored credentials");
        tools.put("auth_switch_method",  "Switch authentication method");

        return tools;
    }

    // ── JSON-RPC Utilities ───────────────────────────────────────────────────

    private static String jsonRpcError(final String id, final int code, final String message) {
        return String.format(
            "{\"jsonrpc\":\"2.0\",\"id\":%s,\"error\":{\"code\":%d,\"message\":\"%s\"}}",
            id, code, escapeJson(message));
    }

    private static String escapeJson(final String raw) {
        if (raw == null) return "";
        return raw.replace("\\", "\\\\").replace("\"", "\\\"")
                  .replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    }

    // ── CLI Entry Point ──────────────────────────────────────────────────────

    /**
     * CLI entry point.
     *
     * @param args {@code --list-tools} to print tool catalogue,
     *             {@code --demo} for connectivity test, or no args for STDIO server
     */
    public static void main(final String[] args) {
        final var config = ConfigLoaderV2.withDefaults().loadSafe();
        final var server = wireServer(config);

        if (args.length > 0 && "--list-tools".equals(args[0])) {
            printToolList(server);
            return;
        }
        if (args.length > 0 && "--demo".equals(args[0])) {
            runDemo(server);
            return;
        }

        server.start();
    }

    /**
     * Factory method that creates a fully wired v2 server using the standard
     * config-loading strategy.
     *
     * <p>Called by {@link server.atlassian.AtlassianServerFactory} — the
     * canonical way to create a versioned Atlassian server from outside
     * this package.
     *
     * @return a ready-to-start v2 server instance
     */
    public static AtlassianServerV2 wireServer() {
        return wireServer(ConfigLoaderV2.withDefaults().loadSafe());
    }

    /**
     * Wires the full server from configuration: auth → clients → handlers → dispatcher.
     */
    static AtlassianServerV2 wireServer(final ServerConfigV2 config) {
        // Auth
        final var authManager = AuthManager.create(
            config.authMethod(),
            config.variant(),
            config.authEmail(),
            config.authToken(),
            config.oauthClientId(),
            config.oauthClientSecret(),
            config.credentialDir()
        );

        // Clients (null-safe — use placeholder URLs for disabled products)
        final var timeoutMs = config.readTimeoutMs();
        final var jiraClient = new JiraClientV2(
            config.jiraBaseUrl() != null ? config.jiraBaseUrl() : "https://disabled.atlassian.net",
            authManager, timeoutMs);
        final var confluenceClient = new ConfluenceClientV2(
            config.confluenceBaseUrl() != null ? config.confluenceBaseUrl() : "https://disabled.atlassian.net",
            authManager, timeoutMs);
        final var bitbucketClient = new BitbucketClientV2(
            config.bitbucketBaseUrl() != null ? config.bitbucketBaseUrl() : "https://api.bitbucket.org",
            authManager, timeoutMs);

        // Handlers
        final var jiraHandler       = new JiraToolHandler(jiraClient);
        final var confluenceHandler = new ConfluenceToolHandler(confluenceClient);
        final var bitbucketHandler  = new BitbucketToolHandler(bitbucketClient);
        final var crossHandler      = new CrossProductHandler(jiraClient, confluenceClient, bitbucketClient, authManager);
        final var authHandler       = new AuthToolHandler(authManager);

        // Dispatcher
        final var dispatcher = new ToolDispatcher(
            jiraHandler, confluenceHandler, bitbucketHandler, crossHandler, authHandler);

        return new AtlassianServerV2(config, dispatcher);
    }

    private static void printToolList(final AtlassianServerV2 server) {
        final var defs = server.getToolDefinitions();
        System.out.println("Atlassian MCP Server v" + SERVER_VERSION
                + " — Available Tools (" + defs.size() + ")\n");

        printSection("Jira", "jira_", defs);
        printSection("Confluence", "confluence_", defs);
        printSection("Bitbucket", "bitbucket_", defs);
        printSection("Cross-product", "atlassian_", defs);
        printSection("Auth", "auth_", defs);
    }

    private static void printSection(final String label, final String prefix,
                                     final Map<String, String> defs) {
        final var matching = defs.entrySet().stream()
                .filter(e -> e.getKey().startsWith(prefix)).toList();
        System.out.println("=== " + label + " (" + matching.size() + ") ===");
        matching.forEach(e -> System.out.println("  " + e.getKey() + "\n    " + e.getValue() + "\n"));
    }

    private static void runDemo(final AtlassianServerV2 server) {
        System.out.println("=== Atlassian MCP Server v2 Demo ===\n");
        System.out.println("Server: " + SERVER_NAME + " v" + SERVER_VERSION);
        System.out.println("Tools:  " + server.getToolDefinitions().size() + " registered");
        System.out.println("Config: " + server.config.instanceName()
                + " (" + server.config.variant().configKey() + ")\n");

        System.out.println("--- Auth Status ---");
        final var authResult = server.dispatcher.dispatch("auth_status", Map.of());
        System.out.println(authResult.text());

        System.out.println("\n--- Connectivity Check ---");
        final var statusResult = server.dispatcher.dispatch("atlassian_status", Map.of());
        System.out.println(statusResult.text());

        System.out.println("\n=== Demo complete ===");
        System.out.println("Configure credentials in user-config/servers/atlassian-v2/");
    }

    // ── Tool Schemas (full MCP inputSchema) ──────────────────────────────────
    //
    // Each entry is a complete JSON tool object with name, description, and
    // inputSchema containing typed properties and required arrays.
    //
    // NOTE: This array is intentionally verbose — each tool gets an explicit
    // schema so the AI assistant can validate arguments before calling.

    private static final String[] TOOL_SCHEMAS = buildToolSchemas();

    @SuppressWarnings("java:S1192") // string duplication is acceptable for schema clarity
    private static String[] buildToolSchemas() {
        return new String[] {

        // ════════════════════════════════════════════════════════════════
        // JIRA (34)
        // ════════════════════════════════════════════════════════════════

        tool("jira_search_issues", "Search Jira issues using JQL or free text",
            props(prop("query", "string", "JQL query or free-text search term"),
                  prop("maxResults", "integer", "Maximum results to return (default 20)"),
                  prop("projectKey", "string", "Limit search to this project")),
            req("query")),

        tool("jira_get_issue", "Get full details of a Jira issue by key",
            props(prop("issueKey", "string", "Jira issue key (e.g. PROJ-123)")),
            req("issueKey")),

        tool("jira_create_issue", "Create a new Jira issue",
            props(prop("projectKey", "string", "Project key"),
                  prop("summary", "string", "Issue summary/title"),
                  prop("issueType", "string", "Issue type (e.g. Bug, Story, Task)"),
                  prop("description", "string", "Issue description")),
            req("projectKey", "summary", "issueType")),

        tool("jira_update_issue", "Update fields on an existing Jira issue",
            props(prop("issueKey", "string", "Jira issue key"),
                  prop("summary", "string", "New summary"),
                  prop("description", "string", "New description")),
            req("issueKey")),

        tool("jira_transition_issue", "Move a Jira issue to a new status via workflow transition",
            props(prop("issueKey", "string", "Jira issue key"),
                  prop("transitionId", "string", "Transition ID (use jira_get_transitions to find)")),
            req("issueKey", "transitionId")),

        tool("jira_delete_issue", "Delete a Jira issue permanently",
            props(prop("issueKey", "string", "Jira issue key to delete")),
            req("issueKey")),

        tool("jira_list_projects", "List all accessible Jira projects",
            props(), req()),

        tool("jira_get_project", "Get details of a specific Jira project",
            props(prop("projectKey", "string", "Project key")),
            req("projectKey")),

        tool("jira_get_sprint", "Get the active sprint for a Jira board",
            props(prop("boardId", "string", "Jira board ID")),
            req("boardId")),

        tool("jira_get_sprint_issues", "Get all issues in a sprint",
            props(prop("boardId", "string", "Jira board ID"),
                  prop("maxResults", "integer", "Maximum results")),
            req("boardId")),

        tool("jira_add_comment", "Add a comment to a Jira issue",
            props(prop("issueKey", "string", "Jira issue key"),
                  prop("comment", "string", "Comment text")),
            req("issueKey", "comment")),

        tool("jira_get_comments", "Get comments on a Jira issue",
            props(prop("issueKey", "string", "Jira issue key"),
                  prop("maxResults", "integer", "Maximum results")),
            req("issueKey")),

        tool("jira_assign_issue", "Assign a Jira issue to a user (omit accountId to unassign)",
            props(prop("issueKey", "string", "Jira issue key"),
                  prop("accountId", "string", "User account ID (omit to unassign)")),
            req("issueKey")),

        tool("jira_get_transitions", "Get available workflow transitions for an issue",
            props(prop("issueKey", "string", "Jira issue key")),
            req("issueKey")),

        tool("jira_search_users", "Search for Jira users by name or email",
            props(prop("query", "string", "Name or email to search for"),
                  prop("maxResults", "integer", "Maximum results")),
            req("query")),

        tool("jira_link_issues", "Create a link between two Jira issues",
            props(prop("inwardIssueKey", "string", "Inward issue key"),
                  prop("outwardIssueKey", "string", "Outward issue key"),
                  prop("linkType", "string", "Link type (e.g. Blocks, Relates)")),
            req("inwardIssueKey", "outwardIssueKey", "linkType")),

        tool("jira_add_worklog", "Log time spent on a Jira issue",
            props(prop("issueKey", "string", "Jira issue key"),
                  prop("timeSpent", "string", "Time spent (e.g. 2h, 30m, 1d)"),
                  prop("comment", "string", "Worklog comment")),
            req("issueKey", "timeSpent")),

        tool("jira_get_boards", "List Jira boards (Scrum/Kanban)",
            props(prop("maxResults", "integer", "Maximum results")),
            req()),

        tool("jira_get_components", "List components for a Jira project",
            props(prop("projectKey", "string", "Project key")),
            req("projectKey")),

        tool("jira_create_component", "Create a new component in a Jira project",
            props(prop("projectKey", "string", "Project key"),
                  prop("name", "string", "Component name"),
                  prop("description", "string", "Component description")),
            req("projectKey", "name")),

        tool("jira_get_versions", "List fix versions for a Jira project",
            props(prop("projectKey", "string", "Project key")),
            req("projectKey")),

        tool("jira_create_version", "Create a new fix version in a Jira project",
            props(prop("projectKey", "string", "Project key"),
                  prop("name", "string", "Version name"),
                  prop("description", "string", "Version description"),
                  prop("releaseDate", "string", "Release date (YYYY-MM-DD)")),
            req("projectKey", "name")),

        tool("jira_get_watchers", "Get all watchers on a Jira issue",
            props(prop("issueKey", "string", "Jira issue key")),
            req("issueKey")),

        tool("jira_add_watcher", "Add a watcher to a Jira issue",
            props(prop("issueKey", "string", "Jira issue key"),
                  prop("accountId", "string", "User account ID")),
            req("issueKey", "accountId")),

        tool("jira_remove_watcher", "Remove a watcher from a Jira issue",
            props(prop("issueKey", "string", "Jira issue key"),
                  prop("accountId", "string", "User account ID")),
            req("issueKey", "accountId")),

        tool("jira_get_issue_types", "List available issue types (optionally for a project)",
            props(prop("projectKey", "string", "Project key (optional)")),
            req()),

        tool("jira_get_priorities", "List all priority levels",
            props(), req()),

        tool("jira_get_statuses", "List all statuses",
            props(), req()),

        tool("jira_bulk_create_issues", "Create multiple Jira issues in one call",
            props(prop("projectKey", "string", "Project key"),
                  prop("issueType", "string", "Issue type for all issues"),
                  prop("summaries", "string", "Semicolon-separated list of summaries")),
            req("projectKey", "issueType", "summaries")),

        tool("jira_list_dashboards", "List Jira dashboards",
            props(), req()),

        tool("jira_get_filters", "List shared/favourite Jira filters",
            props(), req()),

        tool("jira_get_my_issues", "Get issues assigned to the current user",
            props(prop("maxResults", "integer", "Maximum results")),
            req()),

        tool("jira_get_attachments", "Get attachments on a Jira issue",
            props(prop("issueKey", "string", "Jira issue key")),
            req("issueKey")),

        tool("jira_whoami", "Get current Jira user info",
            props(), req()),

        // ════════════════════════════════════════════════════════════════
        // CONFLUENCE (22)
        // ════════════════════════════════════════════════════════════════

        tool("confluence_search", "Search Confluence pages using CQL or free text",
            props(prop("query", "string", "CQL query or free-text search"),
                  prop("maxResults", "integer", "Maximum results")),
            req("query")),

        tool("confluence_get_page", "Get metadata of a Confluence page",
            props(prop("pageId", "string", "Confluence page ID")),
            req("pageId")),

        tool("confluence_get_page_body", "Get the body content of a Confluence page",
            props(prop("pageId", "string", "Confluence page ID"),
                  prop("format", "string", "Body format: storage, atlas_doc_format, view (default: storage)")),
            req("pageId")),

        tool("confluence_create_page", "Create a new Confluence page",
            props(prop("spaceId", "string", "Space ID"),
                  prop("title", "string", "Page title"),
                  prop("content", "string", "Page content (storage format)"),
                  prop("parentId", "string", "Parent page ID (optional)")),
            req("spaceId", "title", "content")),

        tool("confluence_update_page", "Update an existing Confluence page",
            props(prop("pageId", "string", "Confluence page ID"),
                  prop("title", "string", "New page title"),
                  prop("content", "string", "New content (storage format)"),
                  prop("version", "integer", "Current version number (for optimistic locking)")),
            req("pageId", "title", "content", "version")),

        tool("confluence_delete_page", "Delete a Confluence page",
            props(prop("pageId", "string", "Confluence page ID")),
            req("pageId")),

        tool("confluence_list_spaces", "List all accessible Confluence spaces",
            props(prop("maxResults", "integer", "Maximum results")),
            req()),

        tool("confluence_get_space", "Get details of a Confluence space",
            props(prop("spaceId", "string", "Space ID")),
            req("spaceId")),

        tool("confluence_get_page_children", "Get child pages of a Confluence page",
            props(prop("pageId", "string", "Parent page ID"),
                  prop("maxResults", "integer", "Maximum results")),
            req("pageId")),

        tool("confluence_get_page_labels", "Get labels on a Confluence page",
            props(prop("pageId", "string", "Confluence page ID")),
            req("pageId")),

        tool("confluence_add_page_label", "Add a label to a Confluence page",
            props(prop("pageId", "string", "Confluence page ID"),
                  prop("label", "string", "Label text")),
            req("pageId", "label")),

        tool("confluence_get_page_comments", "Get comments on a Confluence page",
            props(prop("pageId", "string", "Confluence page ID"),
                  prop("maxResults", "integer", "Maximum results")),
            req("pageId")),

        tool("confluence_add_page_comment", "Add a comment to a Confluence page",
            props(prop("pageId", "string", "Confluence page ID"),
                  prop("comment", "string", "Comment text")),
            req("pageId", "comment")),

        tool("confluence_get_page_versions", "Get version history of a Confluence page",
            props(prop("pageId", "string", "Confluence page ID")),
            req("pageId")),

        tool("confluence_move_page", "Move a Confluence page to a new parent or space",
            props(prop("pageId", "string", "Confluence page ID"),
                  prop("targetParentId", "string", "Target parent page ID"),
                  prop("targetSpaceId", "string", "Target space ID (optional)")),
            req("pageId", "targetParentId")),

        tool("confluence_get_attachments", "Get attachments on a Confluence page",
            props(prop("pageId", "string", "Confluence page ID")),
            req("pageId")),

        tool("confluence_get_restrictions", "Get access restrictions on a Confluence page",
            props(prop("pageId", "string", "Confluence page ID")),
            req("pageId")),

        tool("confluence_get_properties", "Get custom properties on a Confluence page",
            props(prop("pageId", "string", "Confluence page ID")),
            req("pageId")),

        tool("confluence_set_property", "Set a custom property on a Confluence page",
            props(prop("pageId", "string", "Confluence page ID"),
                  prop("key", "string", "Property key"),
                  prop("value", "string", "Property value (JSON string)")),
            req("pageId", "key", "value")),

        tool("confluence_get_templates", "List available Confluence page templates",
            props(prop("spaceId", "string", "Space ID (optional, for space-specific templates)")),
            req()),

        tool("confluence_whoami", "Get current Confluence user info",
            props(), req()),

        // ════════════════════════════════════════════════════════════════
        // BITBUCKET (31)
        // ════════════════════════════════════════════════════════════════

        tool("bitbucket_list_repos", "List repositories in a Bitbucket workspace",
            props(prop("workspace", "string", "Bitbucket workspace slug")),
            req("workspace")),

        tool("bitbucket_get_repo", "Get details of a Bitbucket repository",
            props(prop("workspace", "string", "Workspace slug"),
                  prop("repoSlug", "string", "Repository slug")),
            req("workspace", "repoSlug")),

        tool("bitbucket_list_pull_requests", "List pull requests for a repository",
            props(prop("workspace", "string", "Workspace slug"),
                  prop("repoSlug", "string", "Repository slug"),
                  prop("state", "string", "PR state: OPEN, MERGED, DECLINED, SUPERSEDED")),
            req("workspace", "repoSlug")),

        tool("bitbucket_get_pull_request", "Get full details of a pull request",
            props(prop("workspace", "string", "Workspace slug"),
                  prop("repoSlug", "string", "Repository slug"),
                  prop("pullRequestId", "integer", "Pull request ID")),
            req("workspace", "repoSlug", "pullRequestId")),

        tool("bitbucket_create_pull_request", "Create a new pull request",
            props(prop("workspace", "string", "Workspace slug"),
                  prop("repoSlug", "string", "Repository slug"),
                  prop("title", "string", "PR title"),
                  prop("sourceBranch", "string", "Source branch name"),
                  prop("targetBranch", "string", "Target branch (default: main)"),
                  prop("description", "string", "PR description")),
            req("workspace", "repoSlug", "title", "sourceBranch")),

        tool("bitbucket_get_pull_request_diff", "Get the diff of a pull request",
            props(prop("workspace", "string", "Workspace slug"),
                  prop("repoSlug", "string", "Repository slug"),
                  prop("pullRequestId", "integer", "Pull request ID")),
            req("workspace", "repoSlug", "pullRequestId")),

        tool("bitbucket_get_pr_comments", "Get comments on a pull request",
            props(prop("workspace", "string", "Workspace slug"),
                  prop("repoSlug", "string", "Repository slug"),
                  prop("pullRequestId", "integer", "Pull request ID")),
            req("workspace", "repoSlug", "pullRequestId")),

        tool("bitbucket_add_pr_comment", "Add a comment to a pull request",
            props(prop("workspace", "string", "Workspace slug"),
                  prop("repoSlug", "string", "Repository slug"),
                  prop("pullRequestId", "integer", "Pull request ID"),
                  prop("content", "string", "Comment content")),
            req("workspace", "repoSlug", "pullRequestId", "content")),

        tool("bitbucket_approve_pull_request", "Approve a pull request",
            props(prop("workspace", "string", "Workspace slug"),
                  prop("repoSlug", "string", "Repository slug"),
                  prop("pullRequestId", "integer", "Pull request ID")),
            req("workspace", "repoSlug", "pullRequestId")),

        tool("bitbucket_decline_pull_request", "Decline a pull request",
            props(prop("workspace", "string", "Workspace slug"),
                  prop("repoSlug", "string", "Repository slug"),
                  prop("pullRequestId", "integer", "Pull request ID")),
            req("workspace", "repoSlug", "pullRequestId")),

        tool("bitbucket_merge_pull_request", "Merge a pull request",
            props(prop("workspace", "string", "Workspace slug"),
                  prop("repoSlug", "string", "Repository slug"),
                  prop("pullRequestId", "integer", "Pull request ID")),
            req("workspace", "repoSlug", "pullRequestId")),

        tool("bitbucket_search_code", "Search code across Bitbucket repositories",
            props(prop("workspace", "string", "Workspace slug"),
                  prop("query", "string", "Search query")),
            req("workspace", "query")),

        tool("bitbucket_list_branches", "List branches in a repository",
            props(prop("workspace", "string", "Workspace slug"),
                  prop("repoSlug", "string", "Repository slug"),
                  prop("maxResults", "integer", "Maximum results")),
            req("workspace", "repoSlug")),

        tool("bitbucket_create_branch", "Create a new branch",
            props(prop("workspace", "string", "Workspace slug"),
                  prop("repoSlug", "string", "Repository slug"),
                  prop("name", "string", "Branch name"),
                  prop("target", "string", "Source commit SHA or branch (default: main)")),
            req("workspace", "repoSlug", "name")),

        tool("bitbucket_delete_branch", "Delete a branch",
            props(prop("workspace", "string", "Workspace slug"),
                  prop("repoSlug", "string", "Repository slug"),
                  prop("name", "string", "Branch name to delete")),
            req("workspace", "repoSlug", "name")),

        tool("bitbucket_list_tags", "List tags in a repository",
            props(prop("workspace", "string", "Workspace slug"),
                  prop("repoSlug", "string", "Repository slug")),
            req("workspace", "repoSlug")),

        tool("bitbucket_create_tag", "Create a new tag",
            props(prop("workspace", "string", "Workspace slug"),
                  prop("repoSlug", "string", "Repository slug"),
                  prop("name", "string", "Tag name"),
                  prop("target", "string", "Commit SHA to tag")),
            req("workspace", "repoSlug", "name", "target")),

        tool("bitbucket_get_commits", "Get recent commits in a repository",
            props(prop("workspace", "string", "Workspace slug"),
                  prop("repoSlug", "string", "Repository slug"),
                  prop("branch", "string", "Branch name (optional)"),
                  prop("maxResults", "integer", "Maximum results")),
            req("workspace", "repoSlug")),

        tool("bitbucket_get_commit", "Get details of a specific commit",
            props(prop("workspace", "string", "Workspace slug"),
                  prop("repoSlug", "string", "Repository slug"),
                  prop("sha", "string", "Commit SHA")),
            req("workspace", "repoSlug", "sha")),

        tool("bitbucket_get_diff", "Get diff between two commits or branches (e.g. main..feature)",
            props(prop("workspace", "string", "Workspace slug"),
                  prop("repoSlug", "string", "Repository slug"),
                  prop("spec", "string", "Diff spec (e.g. main..feature, abc123..def456)")),
            req("workspace", "repoSlug", "spec")),

        tool("bitbucket_get_file_content", "Get file content from a repository",
            props(prop("workspace", "string", "Workspace slug"),
                  prop("repoSlug", "string", "Repository slug"),
                  prop("filePath", "string", "Path to file in repository"),
                  prop("branch", "string", "Branch name (optional)")),
            req("workspace", "repoSlug", "filePath")),

        tool("bitbucket_list_projects", "List projects in a Bitbucket workspace",
            props(prop("workspace", "string", "Workspace slug")),
            req("workspace")),

        tool("bitbucket_list_pipelines", "List pipeline runs for a repository",
            props(prop("workspace", "string", "Workspace slug"),
                  prop("repoSlug", "string", "Repository slug")),
            req("workspace", "repoSlug")),

        tool("bitbucket_get_pipeline", "Get details of a specific pipeline run",
            props(prop("workspace", "string", "Workspace slug"),
                  prop("repoSlug", "string", "Repository slug"),
                  prop("pipelineUuid", "string", "Pipeline UUID")),
            req("workspace", "repoSlug", "pipelineUuid")),

        tool("bitbucket_trigger_pipeline", "Trigger a new pipeline run on a branch",
            props(prop("workspace", "string", "Workspace slug"),
                  prop("repoSlug", "string", "Repository slug"),
                  prop("branch", "string", "Branch to run pipeline on (default: main)")),
            req("workspace", "repoSlug")),

        tool("bitbucket_stop_pipeline", "Stop a running pipeline",
            props(prop("workspace", "string", "Workspace slug"),
                  prop("repoSlug", "string", "Repository slug"),
                  prop("pipelineUuid", "string", "Pipeline UUID")),
            req("workspace", "repoSlug", "pipelineUuid")),

        tool("bitbucket_list_environments", "List deployment environments for a repository",
            props(prop("workspace", "string", "Workspace slug"),
                  prop("repoSlug", "string", "Repository slug")),
            req("workspace", "repoSlug")),

        tool("bitbucket_list_deployments", "List deployments for a repository",
            props(prop("workspace", "string", "Workspace slug"),
                  prop("repoSlug", "string", "Repository slug")),
            req("workspace", "repoSlug")),

        tool("bitbucket_list_snippets", "List code snippets in a workspace",
            props(prop("workspace", "string", "Workspace slug")),
            req("workspace")),

        tool("bitbucket_create_snippet", "Create a new code snippet",
            props(prop("workspace", "string", "Workspace slug"),
                  prop("title", "string", "Snippet title"),
                  prop("content", "string", "Snippet content")),
            req("workspace", "title", "content")),

        tool("bitbucket_whoami", "Get current Bitbucket user info",
            props(), req()),

        // ════════════════════════════════════════════════════════════════
        // CROSS-PRODUCT (3)
        // ════════════════════════════════════════════════════════════════

        tool("atlassian_unified_search", "Search across Jira, Confluence, and Bitbucket in one call",
            props(prop("query", "string", "Search query (JQL/CQL auto-detected, or free text)"),
                  prop("maxResults", "integer", "Max results per product")),
            req("query")),

        tool("atlassian_status", "Check connectivity to all configured Atlassian products",
            props(), req()),

        tool("atlassian_whoami", "Get current user info across all configured products",
            props(), req()),

        // ════════════════════════════════════════════════════════════════
        // AUTH (4)
        // ════════════════════════════════════════════════════════════════

        tool("auth_login_browser", "Start browser-based OAuth 2.0 login flow (opens default browser)",
            props(), req()),

        tool("auth_status", "Check current authentication status and method",
            props(), req()),

        tool("auth_logout", "Clear all stored credentials and OAuth tokens",
            props(), req()),

        tool("auth_switch_method", "Switch authentication method",
            props(prop("method", "string", "Auth method: api_token, pat, or oauth2_browser")),
            req("method")),

        };
    }

    // ── Schema Builder Helpers ────────────────────────────────────────────────

    private static String tool(final String name, final String desc,
                               final String properties, final String required) {
        return "{\"name\":\"" + name + "\",\"description\":\"" + escapeJson(desc)
                + "\",\"inputSchema\":{\"type\":\"object\",\"properties\":{"
                + properties + "}" + required + "}}";
    }

    private static String prop(final String name, final String type, final String desc) {
        return "\"" + name + "\":{\"type\":\"" + type
                + "\",\"description\":\"" + escapeJson(desc) + "\"}";
    }

    private static String props(final String... entries) {
        return String.join(",", entries);
    }

    private static String req(final String... names) {
        if (names.length == 0) return "";
        final var sb = new StringBuilder(",\"required\":[");
        for (int i = 0; i < names.length; i++) {
            if (i > 0) sb.append(',');
            sb.append('"').append(names[i]).append('"');
        }
        sb.append(']');
        return sb.toString();
    }
}
