package server.learningresources.vault.providers;

import server.learningresources.model.*;
import server.learningresources.vault.ResourceProvider;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Curated system design resources organized by sub-domain.
 *
 * <p>Mirrors the {@link ConceptArea} hierarchy under
 * {@link ConceptDomain#SYSTEM_DESIGN}:
 * <ul>
 *   <li>{@link HldResources} — high-level design: scalability, architecture
 *       diagrams, capacity planning, real-world case studies</li>
 *   <li>{@link LldResources} — low-level design: OOD, class design, SOLID
 *       in context, design patterns applied, interview problems</li>
 *   <li>{@link DatabaseResources} — database internals, scaling, SQL/NoSQL
 *       documentation, search engines</li>
 *   <li>{@link DistributedSystemsResources} — consensus, coordination,
 *       reliability patterns, SRE</li>
 * </ul>
 *
 * @see ConceptArea#SYSTEM_DESIGN
 * @see ConceptArea#HIGH_LEVEL_DESIGN
 * @see ConceptArea#LOW_LEVEL_DESIGN
 * @see ConceptArea#DATABASES
 * @see ConceptArea#DISTRIBUTED_SYSTEMS
 */
public final class SystemDesignResources implements ResourceProvider
{

    private static final List<ResourceProvider> SUB_PROVIDERS = List.of(
            new HldResources(),
            new LldResources(),
            new DatabaseResources(),
            new DistributedSystemsResources()
    );

    @Override
    public List<LearningResource> resources()
    {
        final var all = new ArrayList<LearningResource>();
        for (final var provider : SUB_PROVIDERS) {
            all.addAll(provider.resources());
        }
        return Collections.unmodifiableList(all);
    }

    // ─── High-Level Design ──────────────────────────────────────────

    /**
     * Resources focused on high-level system design: architecture diagrams,
     * scalability, back-of-envelope estimation, and real-world case studies.
     *
     * <p>Also covers cross-cutting resources that span both HLD and LLD
     * (e.g., System Design Primer).
     *
     * @see ConceptArea#HIGH_LEVEL_DESIGN
     * @see ConceptArea#SYSTEM_DESIGN
     */
    static final class HldResources implements ResourceProvider
    {

        @Override
        public List<LearningResource> resources()
        {
            final var now = Instant.now();
            return List.of(

                    new LearningResource(
                            "system-design-primer",
                            "The System Design Primer",
                            "https://github.com/donnemartin/system-design-primer",
                            "Comprehensive guide to system design interviews and real-world "
                                    + "architecture. Covers scalability, load balancing, caching, "
                                    + "databases, async processing, CDNs, and microservices.",
                            ResourceType.REPOSITORY,
                            Set.of(ResourceCategory.SOFTWARE_ENGINEERING,
                                    ResourceCategory.GENERAL),
                            Set.of(ConceptArea.SYSTEM_DESIGN,
                                    ConceptArea.HIGH_LEVEL_DESIGN,
                                    ConceptArea.LOW_LEVEL_DESIGN,
                                    ConceptArea.DISTRIBUTED_SYSTEMS,
                                    ConceptArea.DATABASES,
                                    ConceptArea.DATABASE_SCALING,
                                    ConceptArea.NETWORKING,
                                    ConceptArea.RELIABILITY_PATTERNS,
                                    ConceptArea.INTERVIEW_PREP,
                                    ConceptArea.ASYNC_MESSAGING),
                            Set.of("system-design", "scalability", "load-balancing", "caching",
                                    "databases", "microservices", "interview",
                                    "async-processing", "message-queues", "hld", "lld",
                                    "sharding", "replication"),
                            "Donne Martin",
                            DifficultyLevel.ADVANCED,
                            ContentFreshness.PERIODICALLY_UPDATED,
                            false, true, LanguageApplicability.UNIVERSAL, now
                    ),

                    new LearningResource(
                            "bytebytego-system-design",
                            "ByteByteGo — System Design Interview & Architecture",
                            "https://blog.bytebytego.com/",
                            "Alex Xu's system design newsletter and reference. Visual, "
                                    + "diagram-driven explanations of real-world system architectures — "
                                    + "rate limiters, URL shorteners, notification systems, news feeds, "
                                    + "chat systems, search autocomplete. Essential for HLD interviews "
                                    + "and practical architecture thinking. 5M+ subscribers.",
                            ResourceType.BLOG,
                            Set.of(ResourceCategory.SOFTWARE_ENGINEERING),
                            Set.of(ConceptArea.SYSTEM_DESIGN,
                                    ConceptArea.HIGH_LEVEL_DESIGN,
                                    ConceptArea.DISTRIBUTED_SYSTEMS,
                                    ConceptArea.INTERVIEW_PREP),
                            Set.of("bytebytego", "alex-xu", "system-design-interview", "hld",
                                    "architecture-diagrams", "scalability", "load-balancing",
                                    "caching", "rate-limiter", "url-shortener"),
                            "Alex Xu",
                            DifficultyLevel.INTERMEDIATE,
                            ContentFreshness.ACTIVELY_MAINTAINED,
                            false, true, LanguageApplicability.UNIVERSAL, now
                    ),

                    new LearningResource(
                            "high-scalability-blog",
                            "High Scalability — Real Architecture Case Studies",
                            "https://highscalability.com/",
                            "Blog cataloguing real-world architectures of large-scale systems — "
                                    + "how companies like Netflix, Twitter, Instagram, WhatsApp, and "
                                    + "Pinterest handle millions of requests. Each post dissects tech "
                                    + "stack, scaling strategies, lessons learned. Invaluable for HLD "
                                    + "preparation and practical system design intuition.",
                            ResourceType.BLOG,
                            Set.of(ResourceCategory.SOFTWARE_ENGINEERING),
                            Set.of(ConceptArea.SYSTEM_DESIGN,
                                    ConceptArea.HIGH_LEVEL_DESIGN,
                                    ConceptArea.DISTRIBUTED_SYSTEMS,
                                    ConceptArea.DATABASE_SCALING),
                            Set.of("high-scalability", "architecture-case-studies", "netflix",
                                    "twitter", "instagram", "real-world-systems", "hld",
                                    "scaling", "millions-of-requests"),
                            "Todd Hoff",
                            DifficultyLevel.INTERMEDIATE,
                            ContentFreshness.PERIODICALLY_UPDATED,
                            false, true, LanguageApplicability.UNIVERSAL, now
                    )
            );
        }
    }

    // ─── Low-Level Design ───────────────────────────────────────────

    /**
     * Resources focused on low-level design (LLD): object-oriented design,
     * class-level architecture, SOLID applied to class structure, design
     * patterns in context, UML diagrams, and LLD interview problems.
     *
     * @see ConceptArea#LOW_LEVEL_DESIGN
     * @see ConceptArea#DESIGN_PATTERNS
     * @see ConceptArea#OOP
     */
    static final class LldResources implements ResourceProvider
    {

        @Override
        public List<LearningResource> resources()
        {
            final var now = Instant.now();
            return List.of(

                    new LearningResource(
                            "awesome-low-level-design",
                            "Awesome Low Level Design — LLD Interview & OOD",
                            "https://github.com/ashishps1/awesome-low-level-design",
                            "Comprehensive LLD resource: OOP fundamentals, SOLID principles, "
                                    + "all 23 GoF design patterns with code, UML diagram guides, "
                                    + "concurrency concepts, and 30+ graded interview problems "
                                    + "(parking lot, elevator, chess, ride-sharing, stock brokerage). "
                                    + "Solutions in Java, Python, C++, Go, and TypeScript. "
                                    + "22.9k+ stars on GitHub.",
                            ResourceType.REPOSITORY,
                            Set.of(ResourceCategory.SOFTWARE_ENGINEERING,
                                    ResourceCategory.GENERAL),
                            Set.of(ConceptArea.LOW_LEVEL_DESIGN,
                                    ConceptArea.DESIGN_PATTERNS,
                                    ConceptArea.OOP,
                                    ConceptArea.CLEAN_CODE,
                                    ConceptArea.SYSTEM_DESIGN,
                                    ConceptArea.INTERVIEW_PREP),
                            Set.of("lld", "low-level-design", "ood", "object-oriented-design",
                                    "solid", "design-patterns", "uml", "class-diagram",
                                    "interview", "parking-lot", "elevator", "chess",
                                    "machine-coding"),
                            "Ashish Pratap Singh (AlgoMaster)",
                            DifficultyLevel.INTERMEDIATE,
                            ContentFreshness.ACTIVELY_MAINTAINED,
                            false, true, LanguageApplicability.UNIVERSAL, now
                    )
            );
        }
    }

    // ─── Databases ──────────────────────────────────────────────────

    /**
     * Resources covering database internals, scaling, and documentation
     * for major database systems (SQL, NoSQL, search engines).
     *
     * <p>Sub-concepts covered:
     * <ul>
     *   <li>Database internals — storage engines, B-trees, LSM trees, WAL,
     *       MVCC, query planning, indexing deep-dives</li>
     *   <li>Database scaling — sharding, partitioning, replication, read
     *       replicas, connection pooling, federation</li>
     * </ul>
     *
     * @see ConceptArea#DATABASES
     * @see ConceptArea#DATABASE_INTERNALS
     * @see ConceptArea#DATABASE_SCALING
     */
    static final class DatabaseResources implements ResourceProvider
    {

        @Override
        public List<LearningResource> resources()
        {
            final var now = Instant.now();
            return List.of(

                    // ─── Indexing & Internals ────────────────────────────

                    new LearningResource(
                            "use-the-index-luke",
                            "Use The Index, Luke — SQL Indexing & Tuning",
                            "https://use-the-index-luke.com/",
                            "Comprehensive guide to SQL indexing. Covers B-tree structure, "
                                    + "WHERE clause optimization, joins, sorting, partial indexes, "
                                    + "and database-specific advice for Oracle, PostgreSQL, MySQL, "
                                    + "and SQL Server.",
                            ResourceType.TUTORIAL,
                            Set.of(ResourceCategory.DATABASE,
                                    ResourceCategory.SOFTWARE_ENGINEERING),
                            Set.of(ConceptArea.DATABASES,
                                    ConceptArea.DATABASE_INTERNALS,
                                    ConceptArea.COMPLEXITY_ANALYSIS),
                            Set.of("sql", "indexing", "performance", "b-tree",
                                    "query-optimization", "postgresql", "mysql",
                                    "storage-engine", "query-planning"),
                            "Markus Winand",
                            DifficultyLevel.INTERMEDIATE,
                            ContentFreshness.EVERGREEN,
                            false, true, LanguageApplicability.MULTI_LANGUAGE, now
                    ),

                    new LearningResource(
                            "ddia-companion",
                            "Designing Data-Intensive Applications — Martin Kleppmann",
                            "https://dataintensive.net/",
                            "Companion site for DDIA — the most recommended book on data systems "
                                    + "architecture. Covers storage engines (B-trees, LSM-trees), "
                                    + "data models, encoding formats, replication, partitioning, "
                                    + "transactions, consistency & consensus (Raft, Paxos, 2PC), "
                                    + "batch and stream processing. Endorsed by Jay Kreps (Kafka "
                                    + "creator) and the CTO of Microsoft Azure.",
                            ResourceType.BOOK,
                            Set.of(ResourceCategory.SOFTWARE_ENGINEERING,
                                    ResourceCategory.DATABASE),
                            Set.of(ConceptArea.DATABASE_INTERNALS,
                                    ConceptArea.DATABASE_SCALING,
                                    ConceptArea.DISTRIBUTED_SYSTEMS,
                                    ConceptArea.CONSENSUS_COORDINATION,
                                    ConceptArea.SYSTEM_DESIGN,
                                    ConceptArea.ASYNC_MESSAGING),
                            Set.of("ddia", "data-intensive", "martin-kleppmann",
                                    "storage-engines", "b-tree", "lsm-tree", "replication",
                                    "partitioning", "consensus", "stream-processing",
                                    "batch-processing"),
                            "Martin Kleppmann",
                            DifficultyLevel.ADVANCED,
                            ContentFreshness.EVERGREEN,
                            false, false, LanguageApplicability.UNIVERSAL, now,
                            ContentFormat.PUBLISHED_BOOK,
                            Set.of(ResourceAuthor.MARTIN_KLEPPMANN)
                    ),

                    // ─── Relational Databases ───────────────────────────

                    new LearningResource(
                            "postgresql-docs",
                            "PostgreSQL Official Documentation",
                            "https://www.postgresql.org/docs/current/",
                            "Complete PostgreSQL reference — SQL syntax, administration, "
                                    + "performance tuning, replication, extensions, JSON/JSONB, "
                                    + "full-text search, and PL/pgSQL.",
                            ResourceType.API_REFERENCE,
                            Set.of(ResourceCategory.DATABASE),
                            Set.of(ConceptArea.DATABASES,
                                    ConceptArea.DATABASE_INTERNALS,
                                    ConceptArea.DATABASE_SCALING),
                            Set.of("official", "postgresql", "sql", "database",
                                    "replication", "json", "full-text-search", "mvcc",
                                    "wal", "partitioning"),
                            "PostgreSQL Global Development Group",
                            DifficultyLevel.INTERMEDIATE,
                            ContentFreshness.ACTIVELY_MAINTAINED,
                            true, true, LanguageApplicability.MULTI_LANGUAGE, now
                    ),

                    // ─── NoSQL & Caching ────────────────────────────────

                    new LearningResource(
                            "redis-docs",
                            "Redis Documentation",
                            "https://redis.io/docs/latest/",
                            "Official Redis documentation — the most popular in-memory data "
                                    + "store. Covers data types (strings, hashes, lists, sets, "
                                    + "sorted sets, streams), persistence (RDB, AOF), replication, "
                                    + "clustering, Lua scripting, pub/sub, and Redis Stack (JSON, "
                                    + "search, time series).",
                            ResourceType.DOCUMENTATION,
                            Set.of(ResourceCategory.DATABASE, ResourceCategory.DEVOPS),
                            Set.of(ConceptArea.DATABASES,
                                    ConceptArea.SYSTEM_DESIGN,
                                    ConceptArea.DISTRIBUTED_SYSTEMS,
                                    ConceptArea.DATABASE_SCALING,
                                    ConceptArea.ASYNC_MESSAGING),
                            Set.of("official", "redis", "cache", "in-memory",
                                    "data-structures", "pub-sub", "clustering",
                                    "replication", "streams", "caching-strategy"),
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
                            Set.of(ResourceCategory.DATABASE),
                            Set.of(ConceptArea.DATABASES,
                                    ConceptArea.DATABASE_SCALING,
                                    ConceptArea.DISTRIBUTED_SYSTEMS,
                                    ConceptArea.SYSTEM_DESIGN),
                            Set.of("official", "mongodb", "nosql", "document-database",
                                    "aggregation", "sharding", "replica-sets", "atlas",
                                    "partitioning", "replication"),
                            "MongoDB Inc.",
                            DifficultyLevel.INTERMEDIATE,
                            ContentFreshness.ACTIVELY_MAINTAINED,
                            true, true, LanguageApplicability.MULTI_LANGUAGE, now
                    ),

                    // ─── Search Engines ─────────────────────────────────

                    new LearningResource(
                            "elasticsearch-docs",
                            "Elasticsearch Reference",
                            "https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html",
                            "Official Elasticsearch reference — distributed search and "
                                    + "analytics engine. Index management, mapping, analyzers, "
                                    + "query DSL, aggregations, full-text search, vector search, "
                                    + "and cluster administration. Core of the ELK Stack.",
                            ResourceType.DOCUMENTATION,
                            Set.of(ResourceCategory.DATABASE, ResourceCategory.DEVOPS),
                            Set.of(ConceptArea.DATABASES,
                                    ConceptArea.DATABASE_INTERNALS,
                                    ConceptArea.DISTRIBUTED_SYSTEMS,
                                    ConceptArea.OBSERVABILITY),
                            Set.of("official", "elasticsearch", "search", "elk-stack",
                                    "full-text-search", "analytics", "aggregations",
                                    "vector-search", "inverted-index"),
                            "Elastic",
                            DifficultyLevel.INTERMEDIATE,
                            ContentFreshness.ACTIVELY_MAINTAINED,
                            true, true, LanguageApplicability.MULTI_LANGUAGE, now
                    )
            );
        }
    }

    // ─── Distributed Systems ────────────────────────────────────────

    /**
     * Resources covering distributed systems fundamentals: consensus
     * algorithms, coordination services, reliability patterns, and SRE.
     *
     * <p>Sub-concepts covered:
     * <ul>
     *   <li>Consensus &amp; coordination — Raft, Paxos, ZooKeeper, etcd,
     *       leader election, distributed locks, 2PC</li>
     *   <li>Reliability patterns — circuit breaker, retry, bulkhead, rate
     *       limiting, chaos engineering, SLOs/SLIs/SLAs</li>
     * </ul>
     *
     * @see ConceptArea#DISTRIBUTED_SYSTEMS
     * @see ConceptArea#CONSENSUS_COORDINATION
     * @see ConceptArea#RELIABILITY_PATTERNS
     */
    static final class DistributedSystemsResources implements ResourceProvider
    {

        @Override
        public List<LearningResource> resources()
        {
            final var now = Instant.now();
            return List.of(

                    // ─── Consensus & Coordination ───────────────────────

                    new LearningResource(
                            "raft-consensus-visualization",
                            "The Secret Lives of Data — Raft Consensus Visualization",
                            "https://thesecretlivesofdata.com/raft/",
                            "Interactive animated visualization of the Raft consensus "
                                    + "algorithm. Step-by-step walkthrough of leader election, "
                                    + "log replication, term numbers, heartbeats, and split-brain "
                                    + "handling. The best visual explanation of distributed "
                                    + "consensus available online.",
                            ResourceType.INTERACTIVE,
                            Set.of(ResourceCategory.SOFTWARE_ENGINEERING),
                            Set.of(ConceptArea.CONSENSUS_COORDINATION,
                                    ConceptArea.DISTRIBUTED_SYSTEMS),
                            Set.of("raft", "consensus", "leader-election",
                                    "log-replication", "distributed-systems",
                                    "visualization", "interactive", "animation"),
                            "Ben Johnson",
                            DifficultyLevel.BEGINNER,
                            ContentFreshness.EVERGREEN,
                            false, true, LanguageApplicability.UNIVERSAL, now
                    ),

                    // ─── Reliability & SRE ──────────────────────────────

                    new LearningResource(
                            "google-sre-book",
                            "Google SRE Book — Site Reliability Engineering",
                            "https://sre.google/sre-book/table-of-contents/",
                            "The definitive guide to Site Reliability Engineering from "
                                    + "Google. 34 chapters covering risk management, "
                                    + "SLOs/SLIs/SLAs, monitoring, alerting, on-call, incident "
                                    + "response, load balancing, handling overload, cascading "
                                    + "failures, distributed consensus (Paxos & Chubby), data "
                                    + "integrity, and reliable product launches. Free online.",
                            ResourceType.BOOK,
                            Set.of(ResourceCategory.SOFTWARE_ENGINEERING,
                                    ResourceCategory.DEVOPS),
                            Set.of(ConceptArea.RELIABILITY_PATTERNS,
                                    ConceptArea.SYSTEM_DESIGN,
                                    ConceptArea.DISTRIBUTED_SYSTEMS,
                                    ConceptArea.CONSENSUS_COORDINATION,
                                    ConceptArea.OBSERVABILITY),
                            Set.of("sre", "site-reliability", "google", "slo", "sli",
                                    "sla", "monitoring", "incident-response", "on-call",
                                    "load-balancing", "cascading-failures",
                                    "chaos-engineering", "free-book"),
                            "Betsy Beyer, Chris Jones, Jennifer Petoff, "
                                    + "Niall Richard Murphy (Google)",
                            DifficultyLevel.ADVANCED,
                            ContentFreshness.EVERGREEN,
                            true, true, LanguageApplicability.UNIVERSAL, now,
                            ContentFormat.OPEN_BOOK
                    ),

                    new LearningResource(
                            "azure-cloud-design-patterns",
                            "Azure Cloud Design Patterns",
                            "https://learn.microsoft.com/en-us/azure/architecture/patterns/",
                            "Microsoft's catalog of 40+ cloud-native design patterns "
                                    + "organized by problem category — data management (CQRS, "
                                    + "Event Sourcing, Sharding), reliability (Circuit Breaker, "
                                    + "Retry, Bulkhead, Health Endpoint Monitoring), messaging "
                                    + "(Competing Consumers, Priority Queue, "
                                    + "Publisher/Subscriber), and performance (Cache-Aside, "
                                    + "Throttling). Cloud-agnostic principles, Azure-specific "
                                    + "examples.",
                            ResourceType.DOCUMENTATION,
                            Set.of(ResourceCategory.SOFTWARE_ENGINEERING,
                                    ResourceCategory.CLOUD),
                            Set.of(ConceptArea.RELIABILITY_PATTERNS,
                                    ConceptArea.HIGH_LEVEL_DESIGN,
                                    ConceptArea.DESIGN_PATTERNS,
                                    ConceptArea.ASYNC_MESSAGING,
                                    ConceptArea.SYSTEM_DESIGN),
                            Set.of("cloud-patterns", "circuit-breaker", "retry",
                                    "bulkhead", "cqrs", "event-sourcing", "saga",
                                    "ambassador", "sidecar", "throttling", "cache-aside",
                                    "azure", "microsoft"),
                            "Microsoft",
                            DifficultyLevel.INTERMEDIATE,
                            ContentFreshness.ACTIVELY_MAINTAINED,
                            true, true, LanguageApplicability.UNIVERSAL, now
                    )
            );
        }
    }
}
