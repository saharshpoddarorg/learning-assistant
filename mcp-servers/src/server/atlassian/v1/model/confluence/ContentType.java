package server.atlassian.v1.model.confluence;

/**
 * Confluence content types as defined by the REST API.
 *
 * <p>Maps to the {@code type} field in Confluence REST API v1 and the entity type
 * in v2. Use {@link #getApiValue()} when filtering search results or constructing
 * API requests.
 *
 * <p>Note: Confluence v2 API uses separate endpoints per type (e.g., {@code /pages},
 * {@code /blogposts}). The v1 API uses the generic {@code /content} endpoint with a
 * {@code type} query parameter matching {@link #getApiValue()}.
 *
 * @see <a href="https://developer.atlassian.com/cloud/confluence/rest/v2/">
 *     Confluence REST API v2</a>
 */
public enum ContentType {

    /**
     * A standard wiki page within a Confluence space.
     * Pages can be nested (parent/child) and form a hierarchy within a space.
     * v2 endpoint: {@code /wiki/api/v2/pages}
     */
    PAGE("page", "Page", "/wiki/api/v2/pages"),

    /**
     * A blog post, tied to a specific date. Blog posts appear in the space's
     * blog section rather than the page hierarchy.
     * v2 endpoint: {@code /wiki/api/v2/blogposts}
     */
    BLOGPOST("blogpost", "Blog Post", "/wiki/api/v2/blogposts"),

    /**
     * A file or media attachment on a page or blog post.
     * Attachments include documents, images, videos, etc.
     * v2 endpoint: {@code /wiki/api/v2/attachments}
     */
    ATTACHMENT("attachment", "Attachment", "/wiki/api/v2/attachments"),

    /**
     * A comment on a page, blog post, or another comment.
     * Inline comments (anchored to selected text) are also returned with this type.
     * v2 endpoint: Accessed via parent resource; inline: {@code /wiki/api/v2/inline-comments}
     */
    COMMENT("comment", "Comment", "/wiki/api/v2/footer-comments"),

    /**
     * A whiteboard — an infinite canvas for visual collaboration.
     * Whiteboards are supported in Confluence Cloud only.
     * v2 endpoint: {@code /wiki/api/v2/whiteboards}
     */
    WHITEBOARD("whiteboard", "Whiteboard", "/wiki/api/v2/whiteboards"),

    /**
     * A Confluence database — structured data table embedded in a space.
     * Databases are supported in Confluence Cloud only (newer feature).
     * v2 endpoint: {@code /wiki/api/v2/databases}
     */
    DATABASE("database", "Database", "/wiki/api/v2/databases"),

    /**
     * A smart link or embed within Confluence.
     * v2 endpoint: {@code /wiki/api/v2/embeds}
     */
    EMBED("embed", "Embed", "/wiki/api/v2/embeds"),

    /**
     * A space — the top-level container that holds pages, blogs, and other content.
     * Returned in search results; not directly under {@code /content}.
     * v2 endpoint: {@code /wiki/api/v2/spaces}
     */
    SPACE("space", "Space", "/wiki/api/v2/spaces");

    // -------------------------------------------------------------------------

    private final String apiValue;
    private final String displayName;
    private final String v2Endpoint;

    ContentType(final String apiValue, final String displayName, final String v2Endpoint) {
        this.apiValue = apiValue;
        this.displayName = displayName;
        this.v2Endpoint = v2Endpoint;
    }

    /**
     * Returns the exact type string used in Confluence REST API v1 calls (e.g., in
     * {@code ?type=page} query parameters).
     *
     * @return the API type value (e.g., {@code "page"}, {@code "blogpost"})
     */
    public String getApiValue() {
        return apiValue;
    }

    /**
     * Returns a human-readable label for this content type.
     *
     * @return the display name (e.g., "Page", "Blog Post")
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the Confluence REST API v2 endpoint path for this content type.
     * Append to the base Confluence URL (e.g., {@code https://domain.atlassian.net}).
     *
     * @return the v2 endpoint path (e.g., {@code "/wiki/api/v2/pages"})
     */
    public String getV2Endpoint() {
        return v2Endpoint;
    }

    /**
     * Resolves a content type from its API value or enum name.
     *
     * @param value the API type string (case-insensitive)
     * @return the matching content type, or empty if unrecognized
     */
    public static java.util.Optional<ContentType> fromApiValue(final String value) {
        if (value == null || value.isBlank()) {
            return java.util.Optional.empty();
        }
        for (final ContentType type : values()) {
            if (type.apiValue.equalsIgnoreCase(value)
                    || type.name().equalsIgnoreCase(value)) {
                return java.util.Optional.of(type);
            }
        }
        return java.util.Optional.empty();
    }
}
