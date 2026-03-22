package server.atlassian;

import server.atlassian.config.AtlassianConfigLoader;
import server.atlassian.config.AtlassianServerConfig;
import server.atlassian.handler.ToolHandler;
import server.atlassian.model.ToolResponse;
import server.atlassian.util.JsonExtractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Entry point for the Atlassian MCP Server.
 *
 * <p>This server provides a unified gateway to Atlassian's core developer
 * tools — Jira, Confluence, and Bitbucket — through the Model Context
 * Protocol, enabling AI assistants to manage issues, documentation, and
 * code collaboration.
 *
 * <p><strong>Capabilities (44 tools):</strong>
 * <ul>
 *   <li><strong>Jira (17):</strong> search issues (JQL/text), get issue, create,
 *       update, transition, list projects, get active sprint, add comment, get comments,
 *       assign issue, get sprint issues, get transitions, delete issue, search users,
 *       link issues, add worklog, get boards</li>
 *   <li><strong>Confluence (11):</strong> search pages (CQL/text), get page,
 *       create page, update page, list spaces, get page children, delete page,
 *       get page labels, add page label, get page comments, add page comment</li>
 *   <li><strong>Bitbucket (15):</strong> list repos, get repo, list PRs,
 *       get PR details, search code, create PR, list branches, get commits,
 *       get PR diff, get PR comments, add PR comment, approve PR, decline PR,
 *       merge PR, get file content</li>
 *   <li><strong>Cross-product (1):</strong> unified search across Jira + Confluence + Bitbucket</li>
 * </ul>
 *
 * <p><strong>Transport:</strong> STDIO (reads JSON-RPC from stdin, writes to stdout).
 *
 * <p><strong>Usage:</strong>
 * <pre>
 *   java -cp out server.atlassian.AtlassianServer
 *   java -cp out server.atlassian.AtlassianServer --list-tools
 *   java -cp out server.atlassian.AtlassianServer --demo
 * </pre>
 */
public class AtlassianServer {

    private static final Logger LOGGER = Logger.getLogger(AtlassianServer.class.getName());
    private static final String SERVER_NAME = "atlassian";
    private static final String SERVER_VERSION = "1.0.0";

    private final ToolHandler toolHandler;
    private volatile boolean isRunning;

    /**
     * Creates the server from a fully loaded {@link AtlassianServerConfig}.
     *
     * <p>Config is loaded by {@link AtlassianConfigLoader} from layered properties
     * files under {@code user-config/servers/atlassian/}. Environment variables
     * ({@code ATLASSIAN_*}) override file values.
     *
     * @param serverConfig the loaded Atlassian server configuration
     */
    public AtlassianServer(final AtlassianServerConfig serverConfig) {
        this.toolHandler = ToolHandler.fromServerConfig(serverConfig);
        this.isRunning = false;
    }

    /**
     * Starts the MCP server, listening on stdin for JSON-RPC messages.
     *
     * <p>This is a simplified STDIO transport implementation. In production,
     * this would use a full MCP SDK library for proper JSON-RPC framing,
     * message parsing, and protocol compliance.
     */
    public void start() {
        LOGGER.info("Starting " + SERVER_NAME + " v" + SERVER_VERSION
                + " — Jira + Confluence + Bitbucket MCP Server");
        isRunning = true;

        try (var reader = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            while (isRunning) {
                final var line = reader.readLine();
                if (line == null) {
                    LOGGER.info("EOF on stdin — shutting down.");
                    break;
                }

                if (line.isBlank()) {
                    continue;
                }

                processMessage(line.trim());
            }
        } catch (IOException ioException) {
            LOGGER.log(Level.SEVERE, "I/O error reading from stdin", ioException);
        }

        LOGGER.info(SERVER_NAME + " stopped.");
    }

    /**
     * Stops the server gracefully.
     */
    public void stop() {
        isRunning = false;
    }

    /**
     * Returns the server's tool definitions for MCP capability negotiation.
     *
     * @return a map of tool name to description
     */
    public Map<String, String> getToolDefinitions() {
        // Use LinkedHashMap to preserve insertion order for stable tools/list output.
        final var tools = new LinkedHashMap<String, String>();

        // Jira (17)
        tools.put("jira_search_issues",
                "Search Jira issues using JQL or free text. Args: query, maxResults (opt), projectKey (opt)");
        tools.put("jira_get_issue",
                "Get full details of a specific Jira issue by key. Args: issueKey");
        tools.put("jira_create_issue",
                "Create a new Jira issue. Args: projectKey, summary, issueType, description (opt)");
        tools.put("jira_update_issue",
                "Update fields on an existing Jira issue. Args: issueKey, summary (opt), description (opt)");
        tools.put("jira_transition_issue",
                "Move a Jira issue to a new status. Args: issueKey, transitionId");
        tools.put("jira_list_projects",
                "List all accessible Jira projects. No required args.");
        tools.put("jira_get_sprint",
                "Get the active sprint for a Jira board. Args: boardId");
        tools.put("jira_add_comment",
                "Add a comment to a Jira issue. Args: issueKey, comment");
        tools.put("jira_get_comments",
                "Get comments on a Jira issue. Args: issueKey, maxResults (opt)");
        tools.put("jira_assign_issue",
                "Assign a Jira issue to a user. Args: issueKey, accountId (omit to unassign)");
        tools.put("jira_get_sprint_issues",
                "Get all issues in a sprint. Args: boardId, maxResults (opt)");
        tools.put("jira_get_transitions",
                "Get available transitions for a Jira issue. Args: issueKey");
        tools.put("jira_delete_issue",
                "Delete a Jira issue permanently. Args: issueKey");
        tools.put("jira_search_users",
                "Search for Jira users by name or email. Args: query, maxResults (opt)");
        tools.put("jira_link_issues",
                "Create a link between two Jira issues. Args: inwardIssueKey, outwardIssueKey, linkType");
        tools.put("jira_add_worklog",
                "Log time spent on a Jira issue. Args: issueKey, timeSpent, comment (opt)");
        tools.put("jira_get_boards",
                "List Jira boards (Scrum/Kanban). Args: maxResults (opt)");

        // Confluence (11)
        tools.put("confluence_search",
                "Search Confluence pages using CQL or free text. Args: query, maxResults (opt)");
        tools.put("confluence_get_page",
                "Get full content of a Confluence page. Args: pageId");
        tools.put("confluence_create_page",
                "Create a new Confluence page. Args: spaceKey, title, content");
        tools.put("confluence_update_page",
                "Update an existing Confluence page. Args: pageId, title, content, version");
        tools.put("confluence_list_spaces",
                "List all accessible Confluence spaces. No required args.");
        tools.put("confluence_get_page_children",
                "Get child pages of a Confluence page. Args: pageId, maxResults (opt)");
        tools.put("confluence_delete_page",
                "Delete a Confluence page. Args: pageId");
        tools.put("confluence_get_page_labels",
                "Get labels on a Confluence page. Args: pageId");
        tools.put("confluence_add_page_label",
                "Add a label to a Confluence page. Args: pageId, label");
        tools.put("confluence_get_page_comments",
                "Get comments on a Confluence page. Args: pageId, maxResults (opt)");
        tools.put("confluence_add_page_comment",
                "Add a comment to a Confluence page. Args: pageId, comment");

        // Bitbucket (15)
        tools.put("bitbucket_list_repos",
                "List repositories in a Bitbucket workspace. Args: workspace");
        tools.put("bitbucket_get_repo",
                "Get details of a specific Bitbucket repository. Args: workspace, repoSlug");
        tools.put("bitbucket_list_pull_requests",
                "List pull requests for a repository. Args: workspace, repoSlug, state (opt)");
        tools.put("bitbucket_get_pull_request",
                "Get full details of a Bitbucket pull request. Args: workspace, repoSlug, pullRequestId");
        tools.put("bitbucket_search_code",
                "Search code across Bitbucket repositories. Args: workspace, query");
        tools.put("bitbucket_create_pull_request",
                "Create a pull request. Args: workspace, repoSlug, title, sourceBranch, targetBranch, description (opt)");
        tools.put("bitbucket_list_branches",
                "List branches in a repository. Args: workspace, repoSlug, maxResults (opt)");
        tools.put("bitbucket_get_commits",
                "Get recent commits in a repository. Args: workspace, repoSlug, branch (opt), maxResults (opt)");
        tools.put("bitbucket_get_pull_request_diff",
                "Get the diff of a Bitbucket pull request. Args: workspace, repoSlug, pullRequestId");
        tools.put("bitbucket_get_pr_comments",
                "Get comments on a Bitbucket pull request. Args: workspace, repoSlug, pullRequestId");
        tools.put("bitbucket_add_pr_comment",
                "Add a comment to a Bitbucket pull request. Args: workspace, repoSlug, pullRequestId, content");
        tools.put("bitbucket_approve_pull_request",
                "Approve a Bitbucket pull request. Args: workspace, repoSlug, pullRequestId");
        tools.put("bitbucket_decline_pull_request",
                "Decline a Bitbucket pull request. Args: workspace, repoSlug, pullRequestId");
        tools.put("bitbucket_merge_pull_request",
                "Merge a Bitbucket pull request. Args: workspace, repoSlug, pullRequestId");
        tools.put("bitbucket_get_file_content",
                "Get the content of a file from a Bitbucket repository. Args: workspace, repoSlug, filePath, branch (opt)");

        // Cross-product (1)
        tools.put("atlassian_unified_search",
                "Search across Jira, Confluence, and Bitbucket in one call. Args: query, products (opt: jira,confluence,bitbucket), maxResults (opt), workspace (opt for Bitbucket)");

        return tools;
    }

    /**
     * Processes a single incoming JSON-RPC 2.0 message.
     *
     * <p>Handles three MCP lifecycle methods:
     * <ul>
     *   <li>{@code initialize} — responds with server capabilities</li>
     *   <li>{@code tools/list} — emits the full tool catalogue</li>
     *   <li>{@code tools/call} — dispatches to {@link ToolHandler}</li>
     * </ul>
     *
     * @param message the raw JSON-RPC message line
     */
    private void processMessage(final String message) {
        LOGGER.fine("Received: " + message);

        final var method = JsonExtractor.stringOrDefault(message, "method", "");
        final var id     = JsonExtractor.rawValue(message, "id");

        final String response = switch (method) {
            case "initialize"   -> handleInitialize(id);
            case "tools/list"   -> handleToolsList(id);
            case "tools/call"   -> handleToolsCall(id, message);
            default             -> handleUnknownMethod(id, method);
        };

        System.out.println(response);
        System.out.flush();
    }

    /**
     * Handles the MCP {@code initialize} handshake.
     *
     * @param id the JSON-RPC request id
     * @return a JSON-RPC success response advertising server capabilities
     */
    private static String handleInitialize(final String id) {
        return String.format(
                "{\"jsonrpc\":\"2.0\",\"id\":%s,\"result\":{" +
                "\"protocolVersion\":\"2024-11-05\"," +
                "\"capabilities\":{\"tools\":{}}," +
                "\"serverInfo\":{\"name\":\"atlassian\",\"version\":\"%s\"}}}",
                id, SERVER_VERSION);
    }

    /**
     * Handles the MCP {@code tools/list} request by returning all 44 tool schemas.
     *
     * @param id the JSON-RPC request id
     * @return a JSON-RPC response containing the tools array
     */
    private String handleToolsList(final String id) {
        final var sb = new StringBuilder();
        sb.append("{\"jsonrpc\":\"2.0\",\"id\":").append(id)
          .append(",\"result\":{\"tools\":[");

        final var defs = getToolDefinitions();
        var first = true;
        for (final var entry : defs.entrySet()) {
            if (!first) sb.append(',');
            first = false;
            sb.append(toolSchema(entry.getKey(), entry.getValue()));
        }

        sb.append("]}}");
        return sb.toString();
    }

    /**
     * Builds a minimal MCP tool schema JSON object.
     *
     * @param name        the tool name
     * @param description the tool description
     * @return the JSON schema fragment
     */
    private static String toolSchema(final String name, final String description) {
        return String.format(
                "{\"name\":\"%s\",\"description\":\"%s\"," +
                "\"inputSchema\":{\"type\":\"object\",\"properties\":{},\"additionalProperties\":true}}",
                name, escapeJson(description));
    }

    /**
     * Handles the MCP {@code tools/call} request by dispatching to the tool handler.
     *
     * @param id      the JSON-RPC request id
     * @param message the full raw JSON-RPC message
     * @return a JSON-RPC response containing the tool result
     */
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

        final ToolResponse result = toolHandler.handleToolCall(toolName, arguments);
        final String text = escapeJson(result.text());

        return String.format(
                "{\"jsonrpc\":\"2.0\",\"id\":%s,\"result\":{\"content\":[" +
                "{\"type\":\"text\",\"text\":\"%s\"}]}}",
                id, text);
    }

    /**
     * Handles any unrecognised JSON-RPC method.
     *
     * @param id     the JSON-RPC request id
     * @param method the unknown method name
     * @return a JSON-RPC method-not-found error
     */
    private static String handleUnknownMethod(final String id, final String method) {
        return jsonRpcError(id, -32601, "Method not found: " + method);
    }

    /**
     * Builds a JSON-RPC 2.0 error response.
     *
     * @param id      the request id
     * @param code    the JSON-RPC error code
     * @param message the human-readable error message
     * @return the serialised JSON-RPC error object
     */
    private static String jsonRpcError(final String id, final int code, final String message) {
        return String.format(
                "{\"jsonrpc\":\"2.0\",\"id\":%s,\"error\":{\"code\":%d,\"message\":\"%s\"}}",
                id, code, escapeJson(message));
    }

    /**
     * Escapes a string so it can be safely embedded inside a JSON string literal.
     *
     * @param raw the raw string
     * @return the JSON-safe escaped string
     */
    private static String escapeJson(final String raw) {
        if (raw == null) return "";
        return raw.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
                  .replace("\r", "\\r").replace("\t", "\\t");
    }

    /**
     * CLI entry point for the Atlassian MCP Server.
     *
     * @param args command-line arguments: {@code --list-tools} to print tools,
     *             {@code --demo} to run a demo, or no args for STDIO server
     */
    public static void main(final String[] args) {
        final AtlassianServerConfig serverConfig = loadConfig();
        final var server = new AtlassianServer(serverConfig);

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
     * Loads the Atlassian server configuration using the layered config loader.
     *
     * <p>Config files are read from {@code user-config/servers/atlassian/} relative
     * to the working directory. Environment variables ({@code ATLASSIAN_*}) override
     * file values. If loading fails, a safe placeholder config is used so the server
     * can start and print useful error messages via the demo/list-tools modes.
     *
     * @return the loaded server configuration
     */
    private static AtlassianServerConfig loadConfig() {
        try {
            // Allow override of the config directory for multi-instance setups.
            // Set ATLASSIAN_CONFIG_DIR to point to a different atlassian instance dir:
            //   user-config/servers/atlassian-dc/
            //   user-config/servers/atlassian-colleague/
            final var configDirEnv = System.getenv("ATLASSIAN_CONFIG_DIR");
            final var loader = (configDirEnv != null && !configDirEnv.isBlank())
                    ? AtlassianConfigLoader.withConfigDir(resolveConfigDir(configDirEnv))
                    : AtlassianConfigLoader.withDefaults();

            return loader.load();
        } catch (final IllegalStateException ex) {
            LOGGER.warning("Atlassian config incomplete: " + ex.getMessage()
                    + "\nStarting in unconfigured mode — tool calls will fail until credentials are set."
                    + "\nSee user-config/servers/atlassian/atlassian-config.local.example.properties");
            // Return a minimal placeholder so --list-tools and --demo can still run
            return AtlassianServerConfig.cloudDefaults(
                    "unconfigured",
                    "https://placeholder.atlassian.net",
                    new server.atlassian.model.AtlassianCredentials(
                            "", "placeholder-token",
                            server.atlassian.model.AuthType.PERSONAL_ACCESS_TOKEN));
        }
    }

    /**
     * Resolves a config directory path — absolute paths are used as-is;
     * relative paths are resolved against the process working directory.
     *
     * @param rawPath the path string from the env var (may be absolute or relative)
     * @return the resolved {@link java.nio.file.Path}
     */
    private static java.nio.file.Path resolveConfigDir(final String rawPath) {
        final var path = java.nio.file.Path.of(rawPath.strip());
        return path.isAbsolute()
                ? path
                : java.nio.file.Path.of(System.getProperty("user.dir")).resolve(path);
    }

    /**
     * Prints the list of available tools to stdout.
     *
     * @param server the server instance
     */
    private static void printToolList(final AtlassianServer server) {
        final var defs = server.getToolDefinitions();
        System.out.println("Atlassian MCP Server v" + SERVER_VERSION
                + " — Available Tools (" + defs.size() + ")\n");

        System.out.println("=== Jira (17) ===");
        defs.entrySet().stream()
                .filter(e -> e.getKey().startsWith("jira_"))
                .forEach(e -> System.out.println("  " + e.getKey() + "\n    " + e.getValue() + "\n"));

        System.out.println("=== Confluence (11) ===");
        defs.entrySet().stream()
                .filter(e -> e.getKey().startsWith("confluence_"))
                .forEach(e -> System.out.println("  " + e.getKey() + "\n    " + e.getValue() + "\n"));

        System.out.println("=== Bitbucket (15) ===");
        defs.entrySet().stream()
                .filter(e -> e.getKey().startsWith("bitbucket_"))
                .forEach(e -> System.out.println("  " + e.getKey() + "\n    " + e.getValue() + "\n"));

        System.out.println("=== Cross-product (1) ===");
        defs.entrySet().stream()
                .filter(e -> e.getKey().startsWith("atlassian_"))
                .forEach(e -> System.out.println("  " + e.getKey() + "\n    " + e.getValue() + "\n"));
    }

    /**
     * Runs a demo showing the server's capabilities.
     *
     * @param server the server instance
     */
    private static void runDemo(final AtlassianServer server) {
        System.out.println("=== Atlassian MCP Server Demo ===\n");
        System.out.println("Server: " + SERVER_NAME + " v" + SERVER_VERSION);
        System.out.println("Tools:  " + server.getToolDefinitions().size() + " registered\n");

        System.out.println("--- Listing Jira Projects (requires valid credentials) ---");
        final var result = server.toolHandler.handleToolCall(
                "jira_list_projects", Map.of());
        System.out.println(result.text());

        System.out.println("\n=== Demo complete ===");
        System.out.println("Configure credentials in "
                + "user-config/servers/atlassian/atlassian-config.local.properties "
                + "(copy from atlassian-config.local.example.properties) "
                + "or via ATLASSIAN_* environment variables.");
    }
}
