package server.atlassian.v1.model.confluence;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Confluence page with its core fields.
 *
 * <p>Simplified view optimized for AI assistant consumption — captures
 * title, body content, space, and key metadata.
 *
 * @param id        the page ID (numeric string)
 * @param title     the page title
 * @param spaceKey  the space key this page belongs to
 * @param body      the page body content (plain text or storage format)
 * @param status    the page status (e.g., "current", "draft")
 * @param authorName the author display name
 * @param version   the current version number
 * @param labels    labels attached to the page
 * @param webUrl    the web URL to view this page in browser
 * @param created   when the page was created
 * @param updated   when the page was last updated
 */
public record ConfluencePage(
        String id,
        String title,
        String spaceKey,
        String body,
        String status,
        String authorName,
        int version,
        List<String> labels,
        String webUrl,
        Instant created,
        Instant updated
) {

    /**
     * Creates a {@link ConfluencePage} with validation and defensive copying.
     *
     * @param id         page ID
     * @param title      page title
     * @param spaceKey   space key
     * @param body       page body
     * @param status     page status
     * @param authorName author name
     * @param version    version number
     * @param labels     page labels
     * @param webUrl     web URL
     * @param created    creation timestamp
     * @param updated    last update timestamp
     */
    public ConfluencePage {
        Objects.requireNonNull(id, "Page ID must not be null");
        Objects.requireNonNull(title, "Title must not be null");
        Objects.requireNonNull(spaceKey, "Space key must not be null");
        Objects.requireNonNull(body, "Body must not be null (use empty string)");
        Objects.requireNonNull(status, "Status must not be null");
        Objects.requireNonNull(authorName, "Author name must not be null (use empty string)");
        Objects.requireNonNull(labels, "Labels must not be null (use empty list)");
        Objects.requireNonNull(webUrl, "Web URL must not be null (use empty string)");
        Objects.requireNonNull(created, "Created timestamp must not be null");
        Objects.requireNonNull(updated, "Updated timestamp must not be null");

        labels = List.copyOf(labels);
    }
}
