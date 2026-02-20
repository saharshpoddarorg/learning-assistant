package server.learningresources.model;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * A curated learning resource with metadata required for discovery and consumption.
 *
 * <p>Each resource represents a single learning material — an article, tutorial,
 * documentation page, video, or book — with enough metadata to search, filter,
 * rank, and present it to the user.
 *
 * @param id          unique identifier (URL-safe slug, e.g., "oracle-java-tutorials")
 * @param title       human-readable title
 * @param url         canonical URL to the resource
 * @param description brief description of what the resource covers
 * @param type        content format (documentation, tutorial, blog, etc.)
 * @param categories  topic categories this resource belongs to
 * @param tags        free-form tags for fine-grained filtering (e.g., "streams", "concurrency")
 * @param author      author or organization name (empty string if unknown)
 * @param difficulty  estimated difficulty: "beginner", "intermediate", "advanced"
 * @param isFree      whether the resource is freely accessible
 * @param addedAt     when this resource was added to the vault
 */
public record LearningResource(
        String id,
        String title,
        String url,
        String description,
        ResourceType type,
        List<ResourceCategory> categories,
        List<String> tags,
        String author,
        String difficulty,
        boolean isFree,
        Instant addedAt
) {

    /**
     * Creates a {@link LearningResource} with validation and defensive copying.
     *
     * @param id          unique slug identifier
     * @param title       display title
     * @param url         canonical URL
     * @param description brief summary
     * @param type        resource format type
     * @param categories  topic categories (defensively copied)
     * @param tags        free-form tags (defensively copied)
     * @param author      author name (empty string if unknown)
     * @param difficulty  difficulty level
     * @param isFree      free access flag
     * @param addedAt     timestamp when added
     */
    public LearningResource {
        Objects.requireNonNull(id, "Resource ID must not be null");
        Objects.requireNonNull(title, "Title must not be null");
        Objects.requireNonNull(url, "URL must not be null");
        Objects.requireNonNull(description, "Description must not be null");
        Objects.requireNonNull(type, "Resource type must not be null");
        Objects.requireNonNull(categories, "Categories must not be null");
        Objects.requireNonNull(tags, "Tags must not be null");
        Objects.requireNonNull(author, "Author must not be null (use empty string)");
        Objects.requireNonNull(difficulty, "Difficulty must not be null");
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
     * Checks whether this resource has the given tag (case-insensitive).
     *
     * @param tag the tag to search for
     * @return {@code true} if the tag is present
     */
    public boolean hasTag(final String tag) {
        return tags.stream().anyMatch(existingTag -> existingTag.equalsIgnoreCase(tag));
    }
}
