package server.learningresources.handler;

import server.learningresources.model.ConceptArea;
import server.learningresources.model.ContentFreshness;
import server.learningresources.model.DifficultyLevel;
import server.learningresources.model.LanguageApplicability;
import server.learningresources.model.LearningResource;
import server.learningresources.model.ResourceCategory;
import server.learningresources.model.ResourceType;
import server.learningresources.scraper.ContentExtractor;
import server.learningresources.scraper.ScraperException;
import server.learningresources.scraper.WebScraper;
import server.learningresources.vault.ResourceVault;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Smart resource addition via URL scraping — fetches a URL, auto-extracts metadata,
 * infers categorization, and adds the resource to the vault.
 *
 * <p>The pipeline:
 * <ol>
 *   <li>Scrape the URL (HTTP GET, extract HTML)</li>
 *   <li>Extract: title, description, author from HTML meta tags</li>
 *   <li>Infer: type, categories, concepts from URL patterns and content</li>
 *   <li>Build a {@link LearningResource} with extracted + inferred metadata</li>
 *   <li>Add to the vault and return confirmation with details</li>
 * </ol>
 *
 * <p>Users can override any inferred field via explicit arguments.
 *
 * @see ResourceVault
 * @see WebScraper
 */
public final class UrlResourceHandler {

    private static final Logger LOGGER = Logger.getLogger(UrlResourceHandler.class.getName());

    private static final Pattern META_DESCRIPTION =
            Pattern.compile("<meta[^>]*name=[\"']description[\"'][^>]*content=[\"']([^\"']*)[\"']",
                    Pattern.CASE_INSENSITIVE);
    private static final Pattern META_AUTHOR =
            Pattern.compile("<meta[^>]*name=[\"']author[\"'][^>]*content=[\"']([^\"']*)[\"']",
                    Pattern.CASE_INSENSITIVE);
    private static final Pattern OG_DESCRIPTION =
            Pattern.compile("<meta[^>]*property=[\"']og:description[\"'][^>]*content=[\"']([^\"']*)[\"']",
                    Pattern.CASE_INSENSITIVE);
    private static final Pattern OG_SITE_NAME =
            Pattern.compile("<meta[^>]*property=[\"']og:site_name[\"'][^>]*content=[\"']([^\"']*)[\"']",
                    Pattern.CASE_INSENSITIVE);
    private static final Pattern LINK_PATTERN =
            Pattern.compile("<a[^>]*href=[\"']([^\"']*)[\"'][^>]*>",
                    Pattern.CASE_INSENSITIVE);

    /** Known official domains for automatic isOfficial detection. */
    private static final Map<String, String> OFFICIAL_DOMAINS = Map.ofEntries(
            Map.entry("docs.oracle.com", "Oracle"),
            Map.entry("dev.java", "Oracle / dev.java"),
            Map.entry("inside.java", "Oracle / Inside.java"),
            Map.entry("openjdk.org", "OpenJDK"),
            Map.entry("developer.mozilla.org", "Mozilla"),
            Map.entry("docs.python.org", "Python Software Foundation"),
            Map.entry("kubernetes.io", "CNCF / Kubernetes"),
            Map.entry("docs.docker.com", "Docker"),
            Map.entry("spring.io", "VMware / Spring"),
            Map.entry("junit.org", "JUnit Team"),
            Map.entry("typescriptlang.org", "Microsoft / TypeScript"),
            Map.entry("go.dev", "Go Team"),
            Map.entry("rust-lang.org", "Rust Foundation"),
            Map.entry("learn.microsoft.com", "Microsoft"),
            Map.entry("cloud.google.com", "Google Cloud"),
            Map.entry("aws.amazon.com", "Amazon Web Services")
    );

    /**
     * URL patterns → ResourceType inference.
     *
     * <p>Uses {@link Map#ofEntries} (no upper-bound on entry count, unlike {@link Map#of}).
     * Patterns are matched via {@code containsIgnoreCase} on the full URL string.
     */
    private static final Map<String, ResourceType> URL_TYPE_HINTS = Map.ofEntries(
            Map.entry("/docs/", ResourceType.DOCUMENTATION),
            Map.entry("/tutorial", ResourceType.TUTORIAL),
            Map.entry("/guide", ResourceType.TUTORIAL),
            Map.entry("/blog", ResourceType.BLOG),
            Map.entry("/api/", ResourceType.API_REFERENCE),
            Map.entry("/reference", ResourceType.API_REFERENCE),
            Map.entry("/book", ResourceType.BOOK),
            Map.entry("/video", ResourceType.VIDEO),
            Map.entry("playlist", ResourceType.PLAYLIST),
            Map.entry("/course", ResourceType.VIDEO_COURSE),
            Map.entry("github.com", ResourceType.REPOSITORY)
    );

    private final WebScraper scraper;
    private final ContentExtractor extractor;
    private final ResourceVault vault;

    /**
     * Creates a URL resource handler with the given dependencies.
     *
     * @param vault the resource vault to add resources to
     */
    public UrlResourceHandler(final ResourceVault vault) {
        this.vault = Objects.requireNonNull(vault, "ResourceVault must not be null");
        this.scraper = new WebScraper();
        this.extractor = new ContentExtractor();
    }

    /**
     * Scrapes a URL, infers metadata, and adds the resource to the vault.
     *
     * <p>Explicit arguments override auto-detected values.
     *
     * @param arguments the tool arguments (required: "url"; optional: id, title,
     *                  description, type, category, difficulty, tags, author,
     *                  language_applicability)
     * @return confirmation message with extracted details, or error message
     */
    public String addFromUrl(final Map<String, String> arguments) {
        final var url = arguments.get("url");
        if (url == null || url.isBlank()) {
            return "Missing required argument: 'url'";
        }

        try {
            final var result = scraper.fetch(url);
            if (!result.isSuccessful()) {
                return "Failed to fetch URL: HTTP " + result.statusCode() + " for " + url;
            }

            final var html = result.rawHtml();
            final var extractedTitle = extractor.extractTitle(result);
            final var extractedText = extractor.extractText(result);

            // Extract or use explicit overrides
            final var title = arguments.getOrDefault("title", extractedTitle);
            final var description = resolveDescription(arguments, html, extractedText);
            final var author = resolveAuthor(arguments, html, url);
            final var resourceId = resolveId(arguments, url, title);
            final var type = resolveType(arguments, url);
            final var category = resolveCategory(arguments, url, extractedText);
            final var difficulty = resolveDifficulty(arguments);
            final var langApplicability = resolveLanguageApplicability(arguments, category);
            final var isOfficial = isOfficialDomain(url);
            final var tags = resolveTags(arguments, url);
            final var subPages = discoverSubPages(html, url);

            final var resource = new LearningResource(
                    resourceId, title, url, description, type,
                    List.of(category), inferConcepts(extractedText, category),
                    tags, author, difficulty,
                    ContentFreshness.ACTIVELY_MAINTAINED,
                    isOfficial, true, langApplicability, Instant.now()
            );

            vault.add(resource);

            return formatConfirmation(resource, subPages);

        } catch (ScraperException scraperException) {
            LOGGER.log(Level.WARNING, "Scrape failed for " + url, scraperException);
            return "Error scraping " + url + ": " + scraperException.getMessage();
        } catch (IllegalArgumentException validationError) {
            return "Invalid input: " + validationError.getMessage();
        }
    }

    // ─── Metadata Extraction ────────────────────────────────────────

    private String resolveDescription(final Map<String, String> args, String html, String text) {
        if (args.containsKey("description")) {
            return args.get("description");
        }

        // Try meta description
        final var metaMatcher = META_DESCRIPTION.matcher(html);
        if (metaMatcher.find()) {
            return metaMatcher.group(1).trim();
        }

        // Try OG description
        final var ogMatcher = OG_DESCRIPTION.matcher(html);
        if (ogMatcher.find()) {
            return ogMatcher.group(1).trim();
        }

        // Fall back to first 200 chars of extracted text
        if (text.length() > 200) {
            return text.substring(0, 197) + "...";
        }
        return text.isEmpty() ? "No description available" : text;
    }

    private String resolveAuthor(final Map<String, String> args, String html, String url) {
        if (args.containsKey("author")) {
            return args.get("author");
        }

        // Try meta author
        final var metaMatcher = META_AUTHOR.matcher(html);
        if (metaMatcher.find()) {
            return metaMatcher.group(1).trim();
        }

        // Try OG site name
        final var ogMatcher = OG_SITE_NAME.matcher(html);
        if (ogMatcher.find()) {
            return ogMatcher.group(1).trim();
        }

        // Check known official domains
        for (final var entry : OFFICIAL_DOMAINS.entrySet()) {
            if (url.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return "";
    }

    private String resolveId(final Map<String, String> args, String url, String title) {
        if (args.containsKey("id")) {
            return args.get("id");
        }
        // Generate slug from title, then truncate to the slug's own length
        final var slug = title.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
        return slug.substring(0, Math.min(slug.length(), 50));
    }

    private ResourceType resolveType(final Map<String, String> args, String url) {
        if (args.containsKey("type")) {
            return ResourceType.fromDisplayName(args.get("type"));
        }
        final var lowerUrl = url.toLowerCase();
        for (final var entry : URL_TYPE_HINTS.entrySet()) {
            if (lowerUrl.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return ResourceType.ARTICLE;
    }

    private ResourceCategory resolveCategory(final Map<String, String> args, String url, String text) {
        if (args.containsKey("category")) {
            return ResourceCategory.fromDisplayName(args.get("category"));
        }
        final var combined = (url + " " + text).toLowerCase();
        if (combined.contains("java") && !combined.contains("javascript")) {
            return ResourceCategory.JAVA;
        }
        if (combined.contains("python")) {
            return ResourceCategory.PYTHON;
        }
        if (combined.contains("javascript") || combined.contains("typescript") || combined.contains("react")) {
            return ResourceCategory.JAVASCRIPT;
        }
        if (combined.contains("kubernetes") || combined.contains("docker") || combined.contains("ci/cd")) {
            return ResourceCategory.DEVOPS;
        }
        if (combined.contains("algorithm") || combined.contains("data structure")) {
            return ResourceCategory.ALGORITHMS;
        }
        if (combined.contains("security") || combined.contains("owasp")) {
            return ResourceCategory.SECURITY;
        }
        if (combined.contains("machine learning") || combined.contains("ai ") || combined.contains("llm")) {
            return ResourceCategory.AI_ML;
        }
        return ResourceCategory.GENERAL;
    }

    private DifficultyLevel resolveDifficulty(final Map<String, String> args) {
        if (args.containsKey("difficulty")) {
            return DifficultyLevel.fromString(args.get("difficulty"));
        }
        return DifficultyLevel.INTERMEDIATE;
    }

    private LanguageApplicability resolveLanguageApplicability(final Map<String, String> args,
                                                               final ResourceCategory category) {
        if (args.containsKey("language_applicability")) {
            return LanguageApplicability.fromString(args.get("language_applicability"));
        }
        return switch (category) {
            case JAVA -> LanguageApplicability.JAVA_CENTRIC;
            case PYTHON -> LanguageApplicability.PYTHON_SPECIFIC;
            case JAVASCRIPT, WEB -> LanguageApplicability.WEB_SPECIFIC;
            case ALGORITHMS, SOFTWARE_ENGINEERING, TESTING -> LanguageApplicability.UNIVERSAL;
            default -> LanguageApplicability.MULTI_LANGUAGE;
        };
    }

    private boolean isOfficialDomain(final String url) {
        return OFFICIAL_DOMAINS.keySet().stream().anyMatch(url::contains);
    }

    private List<String> resolveTags(final Map<String, String> args, String url) {
        if (args.containsKey("tags")) {
            return java.util.Arrays.stream(args.get("tags").split(","))
                    .map(String::strip)
                    .toList();
        }
        final var tags = new ArrayList<String>();
        tags.add("user-added");
        if (isOfficialDomain(url)) {
            tags.add("official");
        }
        return tags;
    }

    private List<ConceptArea> inferConcepts(final String text, final ResourceCategory category) {
        final var concepts = new ArrayList<ConceptArea>();
        final var lowerText = text.toLowerCase();

        if (lowerText.contains("concurrency") || lowerText.contains("thread")) {
            concepts.add(ConceptArea.CONCURRENCY);
        }
        if (lowerText.contains("design pattern") || lowerText.contains("factory") || lowerText.contains("singleton")) {
            concepts.add(ConceptArea.DESIGN_PATTERNS);
        }
        if (lowerText.contains("test") || lowerText.contains("junit") || lowerText.contains("mock")) {
            concepts.add(ConceptArea.TESTING);
        }
        if (lowerText.contains("algorithm") || lowerText.contains("sorting") || lowerText.contains("search")) {
            concepts.add(ConceptArea.ALGORITHMS);
        }
        if (lowerText.contains("api") || lowerText.contains("rest") || lowerText.contains("endpoint")) {
            concepts.add(ConceptArea.API_DESIGN);
        }
        if (lowerText.contains("security") || lowerText.contains("authentication") || lowerText.contains("encryption")) {
            concepts.add(ConceptArea.WEB_SECURITY);
        }

        if (concepts.isEmpty()) {
            concepts.add(ConceptArea.GETTING_STARTED);
        }
        return concepts;
    }

    // ─── Sub-Page Discovery ─────────────────────────────────────────

    /**
     * Discovers sub-pages linked from the scraped page.
     *
     * <p>Finds internal links (same domain) that likely represent sub-sections,
     * chapters, or child pages of the main resource.
     *
     * @param html the raw HTML content
     * @param url  the base URL for resolving relative links
     * @return list of discovered sub-page URLs (max 20)
     */
    private List<String> discoverSubPages(final String html, final String url) {
        final var subPages = new ArrayList<String>();
        final var domain = extractDomain(url);
        final var matcher = LINK_PATTERN.matcher(html);

        while (matcher.find() && subPages.size() < 20) {
            final var href = matcher.group(1);
            if (isRelevantSubPage(href, domain, url)) {
                final var resolved = resolveUrl(href, url);
                if (!subPages.contains(resolved)) {
                    subPages.add(resolved);
                }
            }
        }
        return subPages;
    }

    private boolean isRelevantSubPage(final String href, final String domain, final String baseUrl) {
        if (href.startsWith("#") || href.startsWith("mailto:") || href.startsWith("javascript:")) {
            return false;
        }
        // Must be same domain (absolute) or relative path
        if (href.startsWith("http")) {
            return href.contains(domain)
                    && !href.equals(baseUrl)
                    && !href.contains("login")
                    && !href.contains("signup");
        }
        // Relative paths are same domain
        return href.startsWith("/") && !href.equals("/");
    }

    private String extractDomain(final String url) {
        try {
            final var uri = java.net.URI.create(url);
            return uri.getHost();
        } catch (IllegalArgumentException invalidUri) {
            return "";
        }
    }

    private String resolveUrl(final String href, final String baseUrl) {
        if (href.startsWith("http")) {
            return href;
        }
        try {
            final var base = java.net.URI.create(baseUrl);
            final var authority = base.getAuthority() != null ? base.getAuthority() : base.getHost();
            return base.getScheme() + "://" + authority + href;
        } catch (IllegalArgumentException invalidUri) {
            return href;
        }
    }

    // ─── Output Formatting ──────────────────────────────────────────

    private String formatConfirmation(final LearningResource resource, final List<String> subPages) {
        final var builder = new StringBuilder();
        builder.append("✅ Resource added to vault!\n\n");
        builder.append("  ID:          ").append(resource.id()).append("\n");
        builder.append("  Title:       ").append(resource.title()).append("\n");
        builder.append("  URL:         ").append(resource.url()).append("\n");
        builder.append("  Type:        ").append(resource.type().getDisplayName()).append("\n");
        builder.append("  Category:    ").append(resource.categories().stream()
                .map(ResourceCategory::getDisplayName).findFirst().orElse("")).append("\n");
        builder.append("  Difficulty:  ").append(resource.difficulty().getDisplayName()).append("\n");
        builder.append("  Language:    ").append(resource.languageApplicability().getDisplayName()).append("\n");
        builder.append("  Official:    ").append(resource.isOfficial() ? "Yes" : "No").append("\n");
        builder.append("  Description: ").append(truncate(resource.description(), 120)).append("\n");

        if (!subPages.isEmpty()) {
            builder.append("\n📄 Discovered ").append(subPages.size()).append(" sub-pages:\n");
            for (final var page : subPages.subList(0, Math.min(subPages.size(), 10))) {
                builder.append("  • ").append(page).append("\n");
            }
            if (subPages.size() > 10) {
                builder.append("  ... and ").append(subPages.size() - 10).append(" more\n");
            }
            builder.append("\nTip: Use 'add_resource_from_url' on any sub-page to add it individually.");
        }

        return builder.toString();
    }

    private String truncate(final String text, final int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }
}
