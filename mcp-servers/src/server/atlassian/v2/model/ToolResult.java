package server.atlassian.v2.model;

import java.util.List;
import java.util.Objects;

/**
 * Standardized result wrapper for MCP tool responses.
 *
 * @param product    the Atlassian product that produced this response
 * @param toolName   the MCP tool that was invoked
 * @param isError    whether the response represents an error
 * @param content    the response content blocks
 * @param itemCount  number of items returned (0 for errors or single-item responses)
 */
public record ToolResult(
        Product product,
        String toolName,
        boolean isError,
        List<String> content,
        int itemCount
) {

    public ToolResult {
        Objects.requireNonNull(product, "Product must not be null");
        Objects.requireNonNull(toolName, "Tool name must not be null");
        Objects.requireNonNull(content, "Content must not be null");
        content = List.copyOf(content);
    }

    /**
     * Creates a successful single-text response.
     */
    public static ToolResult success(final Product product, final String toolName,
                                     final String text) {
        return new ToolResult(product, toolName, false, List.of(text), 1);
    }

    /**
     * Creates a successful response with multiple items.
     */
    public static ToolResult successWithCount(final Product product, final String toolName,
                                              final String text, final int itemCount) {
        return new ToolResult(product, toolName, false, List.of(text), itemCount);
    }

    /**
     * Creates an error response.
     */
    public static ToolResult error(final Product product, final String toolName,
                                   final String errorMessage) {
        return new ToolResult(product, toolName, true, List.of(errorMessage), 0);
    }

    /** Returns the primary text content. */
    public String text() {
        return content.isEmpty() ? "" : content.getFirst();
    }
}
