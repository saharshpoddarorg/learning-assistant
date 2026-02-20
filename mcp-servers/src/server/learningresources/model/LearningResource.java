package server.learningresources.model;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * A curated learning resource with rich metadata for discovery and consumption.
 *
 * <p>Each resource represents a single learning material — an article, tutorial,
 * documentation page, video, course, or book — with enough metadata to search,
 * filter, rank, and present it to the user.
 *
 * <h2>Categorization Dimensions</h2>
 * <ul>
 *   <li><strong>type</strong> — content format (tutorial, documentation, book, etc.)</li>
 *   <li><strong>categories</strong> — broad technology domains (Java, DevOps, AI_ML)</li>
 *   <li><strong>conceptAreas</strong> — fine-grained CS/SE concepts (CONCURRENCY, DESIGN_PATTERNS)</li>
 *   <li><strong>difficulty</strong> — type-safe difficulty level with range queries</li>
 *   <li><strong>freshness</strong> — content maintenance status (evergreen, actively maintained)</li>
 *   <li><strong>isOfficial</strong> — marks official/authoritative sources (Oracle, MDN, etc.)</li>
 *   <li><strong>tags</strong> — free-form tags for keyword filtering</li>
 * </ul>
 *
 * @param id            unique identifier (URL-safe slug, e.g., "oracle-java-tutorials")
 * @param title         human-readable title
 * @param url           canonical URL to the resource
 * @param description   brief description of what the resource covers
 * @param type          content format (documentation, tutorial, blog, etc.)
 * @param categories    broad topic categories this resource belongs to
 * @param conceptAreas  fine-grained CS/SE concepts covered by this resource
 * @param tags          free-form tags for keyword filtering
 * @param author        author or organization name (empty string if unknown)
 * @param difficulty    type-safe difficulty level
 * @param freshness     content maintenance/freshness status
 * @param isOfficial             whether this is an official/authoritative source
 * @param isFree                 whether the resource is freely accessible
 * @param languageApplicability  how the resource relates to programming languages
 * @param addedAt                when this resource was added to the vault
 */
public record LearningResource(
        String id,
        String title,
        String url,
        String description,
        ResourceType type,
        List<ResourceCategory> categories,
        List<ConceptArea> conceptAreas,
        List<String> tags,
        String author,
        DifficultyLevel difficulty,
        ContentFreshness freshness,
        boolean isOfficial,
        boolean isFree,
        LanguageApplicability languageApplicability,
        Instant addedAt
) {

    /**
     * Creates a {@link LearningResource} with validation and defensive copying.
     */
    public LearningResource {
        Objects.requireNonNull(id, "Resource ID must not be null");
        Objects.requireNonNull(title, "Title must not be null");
        Objects.requireNonNull(url, "URL must not be null");
        Objects.requireNonNull(description, "Description must not be null");
        Objects.requireNonNull(type, "Resource type must not be null");
        Objects.requireNonNull(categories, "Categories must not be null");
        Objects.requireNonNull(conceptAreas, "Concept areas must not be null");
        Objects.requireNonNull(tags, "Tags must not be null");
        Objects.requireNonNull(author, "Author must not be null (use empty string)");
        Objects.requireNonNull(difficulty, "Difficulty must not be null");
        Objects.requireNonNull(freshness, "Freshness must not be null");
        Objects.requireNonNull(languageApplicability, "Language applicability must not be null");
        Objects.requireNonNull(addedAt, "AddedAt timestamp must not be null");

        if (id.isBlank()) {
            throw new IllegalArgumentException("Resource ID must not be blank");
        }
        if (title.isBlank()) {
            throw new IllegalArgumentException("Resource title must not be blank");
        }
        if (url.isBlank()) {
            throw new IllegalArgumentException("Resource URL must not be blank");
        }

        categories = List.copyOf(categories);
        conceptAreas = List.copyOf(conceptAreas);
        tags = List.copyOf(tags);
    }

    /**
     * Checks whether this resource matches the given category.
     *
     * @param category the category to check
     * @return {@code true} if this resource belongs to the category
     */
    public boolean hasCategory(final ResourceCategory category) {
        return categories.contains(category);
    }

    /**
     * Checks whether this resource covers the given concept area.
     *
     * @param area the concept area to check
     * @return {@code true} if this resource covers the concept
     */
    public boolean hasConcept(final ConceptArea area) {
        return conceptAreas.contains(area);
    }

    /**
     * Checks whether this resource has the given tag (case-insensitive).
     *
     * @param tag the tag to search for
     * @return {@code true} if the tag is present
     */
    public boolean hasTag(final String tag) {
        return tags.stream().anyMatch(existingTag -> existingTag.equalsIgnoreCase(tag));
    }

    /**
     * Checks whether the difficulty falls within the given range (inclusive).
     *
     * @param min the minimum difficulty
     * @param max the maximum difficulty
     * @return {@code true} if within range
     */
    public boolean isDifficultyInRange(final DifficultyLevel min, final DifficultyLevel max) {
        return difficulty.isInRange(min, max);
    }

    /**
     * Returns a combined searchable text block for full-text matching.
     *
     * @return concatenation of title, description, tags, author, concepts (lowercased)
     */
    public String searchableText() {
        final var builder = new StringBuilder()
                .append(title).append(' ')
                .append(description).append(' ')
                .append(author).append(' ');
        tags.forEach(tag -> builder.append(tag).append(' '));
        conceptAreas.forEach(area -> builder.append(area.getDisplayName()).append(' '));
        categories.forEach(cat -> builder.append(cat.getDisplayName()).append(' '));
        return builder.toString().toLowerCase();
    }
}
