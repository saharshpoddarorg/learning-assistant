package server.learningresources;

import server.learningresources.handler.ToolHandler;
import server.learningresources.vault.ResourceVault;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Entry point for the Learning Resources MCP Server.
 *
 * <p>This server provides tools for discovering, scraping, summarizing,
 * and consuming learning resources from the internet. It acts as a
 * personal learning assistant with a curated vault of high-quality
 * worldwide resources.
 *
 * <p><strong>Capabilities:</strong>
 * <ul>
 *   <li>Search a curated vault of 48+ famous learning resources</li>
 *   <li>Smart discovery with intent classification (specific, vague, exploratory)</li>
 *   <li>Browse resources by category (Java, Python, web, DevOps, etc.)</li>
 *   <li>Scrape and summarize any URL — tutorials, docs, articles</li>
 *   <li>Read full content from URLs with metadata (word count, reading time)</li>
 *   <li>Estimate content difficulty (beginner / intermediate / advanced)</li>
 *   <li>Add custom resources to the vault — manually or via smart URL scraping</li>
 *   <li>Export results as Markdown (PDF/Word planned)</li>
 * </ul>
 *
 * <p><strong>Transport:</strong> STDIO (reads JSON-RPC from stdin, writes to stdout).
 *
 * <p><strong>Usage:</strong>
 * <pre>
 *   java -cp out server.learningresources.LearningResourcesServer
 * </pre>
 */
public class LearningResourcesServer {

    private static final Logger LOGGER = Logger.getLogger(LearningResourcesServer.class.getName());
    private static final String SERVER_NAME = "learning-resources";
    private static final String SERVER_VERSION = "1.0.0";

    private final ResourceVault vault;
    private final ToolHandler toolHandler;
    private volatile boolean isRunning;

    /**
     * Creates the server with a pre-loaded resource vault.
     */
    public LearningResourcesServer() {
        this.vault = new ResourceVault().loadBuiltInResources();
        this.toolHandler = new ToolHandler(vault);
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
                + " with " + vault.size() + " built-in resources.");
        isRunning = true;

        try (var reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
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
        return Map.ofEntries(
                Map.entry("search_resources", "Search the curated vault of learning resources by text, category, type, or difficulty"),
                Map.entry("browse_vault", "Browse the resource vault by category or type"),
                Map.entry("get_resource", "Get detailed information about a specific resource by ID"),
                Map.entry("list_categories", "List all available resource categories with counts"),
                Map.entry("discover_resources", "Smart discovery — classifies intent and finds relevant resources with scoring"),
                Map.entry("scrape_url", "Scrape a URL and return a summary with metadata (word count, reading time, difficulty)"),
                Map.entry("read_url", "Scrape a URL and return the full extracted text content"),
                Map.entry("add_resource", "Add a custom learning resource to the vault"),
                Map.entry("add_resource_from_url", "Scrape a URL, auto-extract metadata, and add as a resource"),
                Map.entry("export_results", "Export discovery/search results as Markdown (PDF/Word planned)")
        );
    }

    /**
     * Processes a single incoming message line.
     *
     * <p>This is a simplified message handler. A production implementation
     * would parse JSON-RPC messages, handle initialization, and maintain
     * proper request/response correlation.
     *
     * @param message the raw message line
     */
    private void processMessage(final String message) {
        LOGGER.fine("Received: " + message);

        // Simplified tool invocation: "tool_name arg1=val1 arg2=val2"
        // Production: parse JSON-RPC, extract method + params
        final var parts = message.split("\\s+", 2);
        final var toolName = parts[0];
        final var arguments = parseSimpleArguments(parts.length > 1 ? parts[1] : "");

        final var result = toolHandler.handleToolCall(toolName, arguments);
        System.out.println(result);
        System.out.flush();
    }

    /**
     * Parses simple key=value arguments from a string.
     *
     * @param argumentString space-separated key=value pairs
     * @return parsed argument map
     */
    private Map<String, String> parseSimpleArguments(final String argumentString) {
        final var arguments = new HashMap<String, String>();

        if (argumentString.isBlank()) {
            return arguments;
        }

        for (final var pair : argumentString.split("\\s+")) {
            final var equalsIndex = pair.indexOf('=');
            if (equalsIndex > 0) {
                final var key = pair.substring(0, equalsIndex);
                final var value = pair.substring(equalsIndex + 1);
                arguments.put(key, value);
            }
        }

        return arguments;
    }

    /**
     * CLI entry point for the Learning Resources MCP Server.
     *
     * @param args command-line arguments: {@code --list-tools} to print tools and exit,
     *             {@code --demo} to run a demo search, or no args to start STDIO server
     */
    public static void main(final String[] args) {
        final var server = new LearningResourcesServer();

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
     * Prints the list of available tools to stdout.
     *
     * @param server the server instance
     */
    private static void printToolList(final LearningResourcesServer server) {
        System.out.println("Learning Resources MCP Server — Available Tools\n");
        server.getToolDefinitions().forEach((name, description) ->
                System.out.println("  " + name + "\n    " + description + "\n"));
    }

    /**
     * Runs a demo showing the server's capabilities.
     *
     * @param server the server instance
     */
    private static void runDemo(final LearningResourcesServer server) {
        System.out.println("=== Learning Resources MCP Server Demo ===\n");
        System.out.println("Vault: " + server.vault.size() + " built-in resources\n");

        System.out.println("--- Listing categories ---\n");
        System.out.println(server.toolHandler.handleToolCall("list_categories", Map.of()));

        System.out.println("\n--- Searching for 'java' ---\n");
        System.out.println(server.toolHandler.handleToolCall("search_resources",
                Map.of("query", "java")));

        System.out.println("\n--- Browsing tutorials ---\n");
        System.out.println(server.toolHandler.handleToolCall("browse_vault",
                Map.of("type", "tutorial")));

        System.out.println("\n--- Getting resource detail ---\n");
        System.out.println(server.toolHandler.handleToolCall("get_resource",
                Map.of("id", "oracle-java-tutorials")));

        System.out.println("\n=== Demo complete ===");
    }
}
