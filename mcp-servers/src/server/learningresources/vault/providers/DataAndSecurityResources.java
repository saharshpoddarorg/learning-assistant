package server.learningresources.vault.providers;

import server.learningresources.model.LearningResource;
import server.learningresources.model.ResourceCategory;
import server.learningresources.model.ResourceType;
import server.learningresources.vault.ResourceProvider;

import java.time.Instant;
import java.util.List;

/**
 * Curated database and security learning resources.
 *
 * <p>Includes: Use The Index Luke (SQL indexing), OWASP Top 10 (web security).
 */
public final class DataAndSecurityResources implements ResourceProvider {

    @Override
    public List<LearningResource> resources() {
        final var now = Instant.now();
        return List.of(

                new LearningResource(
                        "use-the-index-luke",
                        "Use The Index, Luke — SQL Indexing & Tuning",
                        "https://use-the-index-luke.com/",
                        "Free web book explaining SQL indexing in depth. Covers B-tree indexes, "
                                + "query execution plans, join optimization, sorting, and partial indexes "
                                + "across PostgreSQL, MySQL, Oracle, and SQL Server.",
                        ResourceType.BOOK,
                        List.of(ResourceCategory.DATABASE),
                        List.of("sql", "indexing", "performance", "postgresql", "mysql"),
                        "Markus Winand",
                        "intermediate",
                        true, now
                ),

                new LearningResource(
                        "owasp-top-ten",
                        "OWASP Top 10 — Web Application Security Risks",
                        "https://owasp.org/www-project-top-ten/",
                        "The definitive list of the most critical web application security risks. "
                                + "Covers injection, broken auth, XSS, insecure deserialization, "
                                + "and security misconfiguration with prevention guides.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.SECURITY, ResourceCategory.WEB),
                        List.of("owasp", "security", "xss", "injection", "authentication"),
                        "OWASP Foundation",
                        "intermediate",
                        true, now
                )
        );
    }
}
