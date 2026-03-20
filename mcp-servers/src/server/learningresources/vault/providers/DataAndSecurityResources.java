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
 * Curated data-engineering, messaging, and security learning resources — databases,
 * indexing, caching, message brokers, OWASP, and security best practices.
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
                ),

                // ─── NoSQL & Caching ────────────────────────────────────────

                new LearningResource(
                        "redis-docs",
                        "Redis Documentation",
                        "https://redis.io/docs/latest/",
                        "Official Redis documentation — the most popular in-memory data store. "
                                + "Covers data types (strings, hashes, lists, sets, sorted sets, "
                                + "streams), persistence (RDB, AOF), replication, clustering, "
                                + "Lua scripting, pub/sub, and Redis Stack (JSON, search, time series).",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.DATABASE, ResourceCategory.DEVOPS),
                        List.of(ConceptArea.DATABASES, ConceptArea.SYSTEM_DESIGN,
                                ConceptArea.DISTRIBUTED_SYSTEMS),
                        List.of("official", "redis", "cache", "in-memory", "data-structures",
                                "pub-sub", "clustering", "replication"),
                        "Redis Ltd.",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                new LearningResource(
                        "mongodb-docs",
                        "MongoDB Documentation",
                        "https://www.mongodb.com/docs/manual/",
                        "Official MongoDB documentation — the leading document database. "
                                + "CRUD operations, aggregation pipeline, indexing strategies, "
                                + "replica sets, sharding, transactions, change streams, and "
                                + "Atlas (cloud-hosted) deployment.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.DATABASE),
                        List.of(ConceptArea.DATABASES, ConceptArea.DISTRIBUTED_SYSTEMS,
                                ConceptArea.SYSTEM_DESIGN),
                        List.of("official", "mongodb", "nosql", "document-database",
                                "aggregation", "sharding", "replica-sets", "atlas"),
                        "MongoDB Inc.",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                // ─── Messaging & Streaming ──────────────────────────────────

                new LearningResource(
                        "kafka-docs",
                        "Apache Kafka Documentation",
                        "https://kafka.apache.org/documentation/",
                        "Official Apache Kafka documentation — the distributed event streaming "
                                + "platform. Topics, partitions, consumer groups, producers, Kafka "
                                + "Streams, Kafka Connect, Schema Registry, exactly-once semantics, "
                                + "and cluster management.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.DEVOPS, ResourceCategory.DATABASE),
                        List.of(ConceptArea.DISTRIBUTED_SYSTEMS, ConceptArea.SYSTEM_DESIGN,
                                ConceptArea.ARCHITECTURE),
                        List.of("official", "kafka", "event-streaming", "pub-sub", "topics",
                                "partitions", "consumer-groups", "kafka-streams"),
                        "Apache Software Foundation",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                new LearningResource(
                        "elasticsearch-docs",
                        "Elasticsearch Reference",
                        "https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html",
                        "Official Elasticsearch reference — distributed search and analytics "
                                + "engine. Index management, mapping, analyzers, query DSL, "
                                + "aggregations, full-text search, vector search, and cluster "
                                + "administration. Core of the ELK Stack.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.DATABASE, ResourceCategory.DEVOPS),
                        List.of(ConceptArea.DATABASES, ConceptArea.DISTRIBUTED_SYSTEMS,
                                ConceptArea.OBSERVABILITY),
                        List.of("official", "elasticsearch", "search", "elk-stack",
                                "full-text-search", "analytics", "aggregations", "vector-search"),
                        "Elastic",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.MULTI_LANGUAGE, now
                )
        );
    }
}
