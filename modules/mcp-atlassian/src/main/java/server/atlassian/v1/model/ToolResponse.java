package server.atlassian.v1.model;

import java.util.List;
import java.util.Objects;

/**
 * Standardized response wrapper for MCP tool results.
 *
 * <p>Wraps the output of any Atlassian tool call with metadata
 * indicating success/failure, the originating product, and
 * structured content blocks.
 *
 * @param product    the Atlassian product that produced this response
 * @param toolName   the MCP tool that was invoked
 * @param isError    whether the response represents an error
 * @param content    the response content (text blocks)
 * @param itemCount  number of items returned (0 for errors or single-item responses)
 */
public record ToolResponse(
        AtlassianProduct product,
        String toolName,
        boolean isError,
        List<String> content,
        int itemCount
) {

    /**
     * Creates a tool response with validation and defensive copying.
     *
     * @param product   originating product
     * @param toolName  tool name
     * @param isError   error flag
     * @param content   content blocks
     * @param itemCount result count
     */
    public ToolResponse {
        Objects.requireNonNull(product, "Product must not be null");
        Objects.requireNonNull(toolName, "Tool name must not be null");
        Objects.requireNonNull(content, "Content must not be null");
        content = List.copyOf(content);
    }

    /**
     * Creates a successful single-text response.
     *
     * @param product  the originating product
     * @param toolName the tool name
     * @param text     the response text
     * @return a success response
     */
    public static ToolResponse success(final AtlassianProduct product,
                                       final String toolName,
                                       final String text) {
        return new ToolResponse(product, toolName, false, List.of(text), 1);
    }

    /**
     * Creates a successful multi-item response.
     *
     * @param product   the originating product
     * @param toolName  the tool name
     * @param text      the formatted result text
     * @param itemCount the number of items in the result
     * @return a success response with item count
     */
    public static ToolResponse successWithCount(final AtlassianProduct product,
                                                final String toolName,
                                                final String text,
                                                final int itemCount) {
        return new ToolResponse(product, toolName, false, List.of(text), itemCount);
    }

    /**
     * Creates an error response.
     *
     * @param product      the originating product
     * @param toolName     the tool name
     * @param errorMessage the error description
     * @return an error response
     */
    public static ToolResponse error(final AtlassianProduct product,
                                     final String toolName,
                                     final String errorMessage) {
        return new ToolResponse(product, toolName, true, List.of(errorMessage), 0);
    }

    /**
     * Returns the primary text content of this response.
     *
     * @return the first content block, or empty string if none
     */
    public String text() {
        return content.isEmpty() ? "" : content.getFirst();
    }
}
