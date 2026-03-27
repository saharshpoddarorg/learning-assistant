package server.learningresources.model;

/**
 * Notable authors whose works appear in the learning resources vault.
 *
 * <p>This enum provides structured metadata for well-known authors, enabling
 * cross-referencing, filtering by author, and improved search discoverability.
 * Only authors with curated resources in the vault are included.
 *
 * <p><strong>Set field on {@link LearningResource}.</strong> Most resources
 * (official docs, community tutorials, organisational guides) do not have
 * a notable author. Use an empty set when no specific author applies.
 *
 * <h2>When to assign a {@code ResourceAuthor}</h2>
 * <ul>
 *   <li>Published books — always (the author is core metadata)</li>
 *   <li>Blogs by a recognised author — when the author is a primary reason
 *       users would seek the resource (e.g., Martin Fowler's architecture blog)</li>
 *   <li>Video series by a known creator — when the creator's identity is
 *       part of the brand (e.g., 3Blue1Brown)</li>
 *   <li>Official docs / community content — typically empty set</li>
 *   <li>Multi-author works — use {@code Set.of(A, B)} (e.g., JCIP)</li>
 * </ul>
 *
 * <h2>Constructors</h2>
 * <ul>
 *   <li>{@code ResourceAuthor(displayName)} — convenience for authors with
 *       no alias, website, or blog</li>
 *   <li>{@code ResourceAuthor(displayName, alias, website, blog)} — full
 *       constructor for authors with optional metadata</li>
 * </ul>
 *
 * @see LearningResource#resourceAuthors()
 * @see ContentFormat
 */
public enum ResourceAuthor
{

    // ─── Clean Code / Architecture Series ────────────────────────────

    /** Robert C. Martin — Clean Code, Clean Architecture, Clean Agile. */
    ROBERT_C_MARTIN("Robert C. Martin", "Uncle Bob",
            "https://cleancoder.com", "https://blog.cleancoder.com"),

    /** Martin Fowler — Refactoring, PoEAA, architecture bliki. */
    MARTIN_FOWLER("Martin Fowler", null,
            "https://martinfowler.com", null),

    // ─── Java Authorities ────────────────────────────────────────────

    /** Joshua Bloch — Effective Java, Java Collections Framework. */
    JOSHUA_BLOCH("Joshua Bloch"),

    /** Brian Goetz — Java Concurrency in Practice, Java Language Architect. */
    BRIAN_GOETZ("Brian Goetz"),

    // ─── System Design / Architecture Authors ────────────────────────

    /** Martin Kleppmann — Designing Data-Intensive Applications. */
    MARTIN_KLEPPMANN("Martin Kleppmann", null,
            "https://martin.kleppmann.com", null),

    /** Juval Löwy — Righting Software (volatility-based decomposition). */
    JUVAL_LOWY("Juval Löwy"),

    // ─── Software Engineering Authors ────────────────────────────────

    /** Adam Tornhill — Your Code as a Crime Scene, founder of CodeScene. */
    ADAM_TORNHILL("Adam Tornhill"),

    /** John Ousterhout — A Philosophy of Software Design. */
    JOHN_OUSTERHOUT("John Ousterhout", null,
            "https://web.stanford.edu/~ouster/cgi-bin/home.php", null),

    /** Vladimir Khorikov — Unit Testing: Principles, Practices, and Patterns. */
    VLADIMIR_KHORIKOV("Vladimir Khorikov", null,
            "https://enterprisecraftsmanship.com", null),

    // ─── VCS / Open-Source Authors ───────────────────────────────────

    /** Scott Chacon — Pro Git, co-founder of GitHub, creator of GitButler. */
    SCOTT_CHACON("Scott Chacon", null,
            "https://scottchacon.com", null),

    /** Steven S. Skiena — The Algorithm Design Manual. */
    STEVEN_SKIENA("Steven S. Skiena", null,
            "https://www3.cs.stonybrook.edu/~skiena/", null),

    // ─── Educators / Creators ────────────────────────────────────────

    /** Alexander Shvets — Refactoring.Guru (design patterns & refactoring). */
    ALEXANDER_SHVETS("Alexander Shvets", null,
            "https://refactoring.guru", null),

    /** Grant Sanderson — 3Blue1Brown (mathematics video series). */
    GRANT_SANDERSON("Grant Sanderson", "3Blue1Brown",
            "https://www.3blue1brown.com", null);

    private final String displayName;
    private final String alias;
    private final String website;
    private final String blog;

    ResourceAuthor(final String displayName, final String alias,
                   final String website, final String blog)
    {
        this.displayName = displayName;
        this.alias = alias;
        this.website = website;
        this.blog = blog;
    }

    /**
     * Convenience constructor for authors with no alias, website, or blog.
     *
     * @param displayName the author's full canonical name
     */
    ResourceAuthor(final String displayName)
    {
        this(displayName, null, null, null);
    }

    /**
     * Returns the author's full canonical name.
     *
     * @return the display name (e.g., "Robert C. Martin")
     */
    public String getDisplayName()
    {
        return displayName;
    }

    /**
     * Returns the author's common alias, or {@code null} if none exists.
     *
     * @return the alias (e.g., "Uncle Bob", "3Blue1Brown"), or {@code null}
     */
    public String getAlias()
    {
        return alias;
    }

    /**
     * Returns {@code true} if this author has a commonly known alias.
     *
     * @return whether an alias is defined
     */
    public boolean hasAlias()
    {
        return alias != null;
    }

    /**
     * Returns the author's personal or professional website, or {@code null}.
     *
     * @return the website URL (e.g., "https://martinfowler.com"), or {@code null}
     */
    public String getWebsite()
    {
        return website;
    }

    /**
     * Returns the author's blog URL if separate from the website, or {@code null}.
     *
     * @return the blog URL (e.g., "https://blog.cleancoder.com"), or {@code null}
     */
    public String getBlog()
    {
        return blog;
    }

    /**
     * Returns {@code true} if this author has a known website.
     *
     * @return whether a website URL is defined
     */
    public boolean hasWebsite()
    {
        return website != null;
    }

    /**
     * Returns {@code true} if this author has a separate blog URL.
     *
     * @return whether a blog URL is defined
     */
    public boolean hasBlog()
    {
        return blog != null;
    }

    /**
     * Returns a space-separated string of all searchable author names
     * (display name + alias if present), lowercased.
     *
     * @return searchable name string for full-text indexing
     */
    public String getSearchableNames()
    {
        if (alias != null) {
            return displayName.toLowerCase() + " " + alias.toLowerCase();
        }
        return displayName.toLowerCase();
    }

    /**
     * Resolves a {@link ResourceAuthor} from a display name or alias.
     *
     * @param value the name to look up (case-insensitive)
     * @return the matching author
     * @throws IllegalArgumentException if no match is found
     */
    public static ResourceAuthor fromName(final String value)
    {
        for (final ResourceAuthor author : values()) {
            if (author.displayName.equalsIgnoreCase(value)) {
                return author;
            }
            if (author.alias != null && author.alias.equalsIgnoreCase(value)) {
                return author;
            }
        }
        throw new IllegalArgumentException("Unknown resource author: " + value);
    }
}
