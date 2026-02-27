package server.learningresources.vault.providers;

import server.learningresources.model.ConceptArea;
import server.learningresources.model.ContentFreshness;
import server.learningresources.model.DifficultyLevel;
import server.learningresources.model.LanguageApplicability;
import server.learningresources.model.LearningResource;
import server.learningresources.model.ResourceCategory;
import server.learningresources.model.ResourceType;
import server.learningresources.vault.ResourceProvider;

import java.time.Instant;
import java.util.List;

/**
 * Curated data-engineering and security learning resources — databases, indexing,
 * OWASP, and security best practices.
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
                        "Comprehensive guide to SQL indexing. Covers B-tree structure, "
                                + "WHERE clause optimization, joins, sorting, partial indexes, "
                                + "and database-specific advice for Oracle, PostgreSQL, MySQL, "
                                + "and SQL Server.",
                        ResourceType.TUTORIAL,
                        List.of(ResourceCategory.DATABASE, ResourceCategory.SOFTWARE_ENGINEERING),
                        List.of(ConceptArea.DATABASES, ConceptArea.COMPLEXITY_ANALYSIS),
                        List.of("sql", "indexing", "performance", "b-tree", "query-optimization",
                                "postgresql", "mysql"),
                        "Markus Winand",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.EVERGREEN,
                        false, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                new LearningResource(
                        "owasp-top-ten",
                        "OWASP Top Ten — Web Application Security Risks",
                        "https://owasp.org/www-project-top-ten/",
                        "The definitive list of the most critical web application security "
                                + "risks. Each risk includes description, examples, prevention "
                                + "techniques, and references. Standard for security audits.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.SECURITY, ResourceCategory.WEB),
                        List.of(ConceptArea.WEB_SECURITY, ConceptArea.API_DESIGN),
                        List.of("official", "owasp", "security", "injection", "xss", "csrf",
                                "authentication", "authorization"),
                        "OWASP Foundation",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.PERIODICALLY_UPDATED,
                        true, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                new LearningResource(
                        "postgresql-docs",
                        "PostgreSQL Official Documentation",
                        "https://www.postgresql.org/docs/current/",
                        "Complete PostgreSQL reference — SQL syntax, administration, "
                                + "performance tuning, replication, extensions, JSON/JSONB, "
                                + "full-text search, and PL/pgSQL.",
                        ResourceType.API_REFERENCE,
                        List.of(ResourceCategory.DATABASE),
                        List.of(ConceptArea.DATABASES, ConceptArea.API_DESIGN),
                        List.of("official", "postgresql", "sql", "database", "replication",
                                "json", "full-text-search"),
                        "PostgreSQL Global Development Group",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                new LearningResource(
                        "owasp-cheat-sheets",
                        "OWASP Cheat Sheet Series",
                        "https://cheatsheetseries.owasp.org/",
                        "Collection of concise, actionable security cheat sheets. Covers "
                                + "authentication, session management, input validation, "
                                + "cryptographic storage, REST security, and more.",
                        ResourceType.CHEAT_SHEET,
                        List.of(ResourceCategory.SECURITY),
                        List.of(ConceptArea.WEB_SECURITY, ConceptArea.CRYPTOGRAPHY,
                                ConceptArea.API_DESIGN),
                        List.of("official", "owasp", "security", "cheat-sheet", "authentication",
                                "session-management", "cryptography"),
                        "OWASP Foundation",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.MULTI_LANGUAGE, now
                )
        );
    }
}
