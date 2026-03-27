package server.learningresources.model;

/**
 * Classifies a resource's publication and authoring format.
 *
 * <p>Orthogonal to {@link ResourceType} (which classifies <em>content format</em>
 * — book, tutorial, video), {@code ContentFormat} classifies <em>how</em> the
 * resource is published and authored. A resource is exactly one {@code ContentFormat}.
 *
 * <h2>Usage examples</h2>
 * <ul>
 *   <li>An O'Reilly ebook → {@link #PUBLISHED_BOOK}</li>
 *   <li>Google SRE Book (free online) → {@link #OPEN_BOOK}</li>
 *   <li>MDN Web Docs → {@link #WEB_RESOURCE}</li>
 *   <li>A GitHub learning repo → {@link #WEB_RESOURCE}</li>
 * </ul>
 *
 * @see ResourceType
 * @see LearningResource#contentFormat()
 */
public enum ContentFormat
{

    /**
     * Web-native content: documentation, tutorials, blogs, repositories,
     * videos, interactive tools, and other content created for online
     * consumption. This is the default for the vast majority of resources.
     */
    WEB_RESOURCE("web-resource"),

    /**
     * Commercially published book (print and/or ebook) — typically has an
     * ISBN, a publisher, and known author(s). Usually requires purchase.
     *
     * <p>Examples: <em>Effective Java</em> (Bloch), <em>Clean Architecture</em>
     * (Martin), <em>Designing Data-Intensive Applications</em> (Kleppmann).
     */
    PUBLISHED_BOOK("published-book"),

    /**
     * Book whose full text is freely available online. May also exist in
     * print, but the canonical version is openly accessible.
     *
     * <p>Examples: <em>Pro Git</em> (git-scm.com/book), <em>Google SRE Book</em>
     * (sre.google), <em>Structure and Interpretation of Computer Programs</em>.
     */
    OPEN_BOOK("open-book");

    private final String displayName;

    ContentFormat(final String displayName)
    {
        this.displayName = displayName;
    }

    /**
     * Returns the human-readable display name.
     *
     * @return the display name (e.g., "published-book")
     */
    public String getDisplayName()
    {
        return displayName;
    }

    /**
     * Resolves a {@link ContentFormat} from a display name string.
     *
     * @param value the display name (case-insensitive)
     * @return the matching format
     * @throws IllegalArgumentException if no match is found
     */
    public static ContentFormat fromDisplayName(final String value)
    {
        for (final ContentFormat format : values()) {
            if (format.displayName.equalsIgnoreCase(value)) {
                return format;
            }
        }
        throw new IllegalArgumentException("Unknown content format: " + value);
    }
}
