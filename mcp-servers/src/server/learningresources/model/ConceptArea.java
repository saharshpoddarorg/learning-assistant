package server.learningresources.model;

/**
 * Fine-grained concept areas for learning resources.
 *
 * <p>While {@link ResourceCategory} groups by broad technology domain (Java, Python,
 * DevOps), {@code ConceptArea} classifies by the specific CS/SE concept being taught.
 * A resource can belong to multiple concept areas — for example, a tutorial on
 * "Java virtual threads" would have concepts {@code CONCURRENCY} and {@code LANGUAGE_FEATURES}.
 *
 * <p>Each concept belongs to exactly one {@link ConceptDomain} — a high-level grouping
 * of related concepts. Use {@link #getDomain()} to navigate from concept to domain.
 *
 * <p><b>Sub-hierarchy support:</b> A concept can optionally declare a parent
 * {@code ConceptArea} via {@link #getParentArea()}, enabling tree-structured
 * concept taxonomies. For example, {@code GARBAGE_COLLECTION}, {@code CLASS_LOADING},
 * {@code SERIALIZATION}, and {@code JVM_LANGUAGES} are children of
 * {@code JVM_INTERNALS}. Use {@link #hasParent()} and {@link #isChildOf(ConceptArea)}
 * to navigate the hierarchy.
 *
 * <p>This enables concept-based discovery: a user asking "I want to learn about
 * concurrency" gets resources from Java, Python, and general CS — not just one language.
 *
 * @see ConceptDomain
 */
public enum ConceptArea
{

    // ─── Programming Fundamentals ───────────────────────────────────

    /**
     * Language syntax, semantics, core features (loops, types, operators).
     */
    LANGUAGE_BASICS("language-basics", ConceptDomain.PROGRAMMING_FUNDAMENTALS),

    /**
     * Object-oriented concepts: classes, inheritance, polymorphism, encapsulation.
     */
    OOP("oop", ConceptDomain.PROGRAMMING_FUNDAMENTALS),

    /**
     * Functional programming: lambdas, higher-order functions, immutability.
     */
    FUNCTIONAL_PROGRAMMING("functional-programming", ConceptDomain.PROGRAMMING_FUNDAMENTALS),

    /**
     * New language features, version upgrades, migration guides.
     */
    LANGUAGE_FEATURES("language-features", ConceptDomain.PROGRAMMING_FUNDAMENTALS),

    /**
     * Generic types, type parameters, variance, wildcards, type erasure.
     */
    GENERICS("generics", ConceptDomain.PROGRAMMING_FUNDAMENTALS),

    // ─── Core CS Concepts ───────────────────────────────────────────

    /**
     * Threads, executors, locks, synchronization, async patterns (virtual threads, coroutines).
     */
    CONCURRENCY("concurrency", ConceptDomain.CORE_CS),

    /**
     * Data structures: arrays, lists, trees, graphs, heaps, tries, hash tables.
     */
    DATA_STRUCTURES("data-structures", ConceptDomain.CORE_CS),

    /**
     * Algorithms: sorting, searching, dynamic programming, greedy, backtracking.
     */
    ALGORITHMS("algorithms", ConceptDomain.CORE_CS),

    /**
     * Mathematical foundations: linear algebra, calculus, probability, discrete math, statistics.
     */
    MATHEMATICS("mathematics", ConceptDomain.CORE_CS),

    /**
     * Big-O analysis, time/space complexity, benchmarking, optimization.
     */
    COMPLEXITY_ANALYSIS("complexity-analysis", ConceptDomain.CORE_CS),

    /**
     * Memory management, garbage collection, allocation strategies.
     */
    MEMORY_MANAGEMENT("memory-management", ConceptDomain.CORE_CS),

    /**
     * JVM internals: architecture, bytecode, JIT compilation, runtime data areas. Parent of GC, class loading, serialization, JVM languages.
     */
    JVM_INTERNALS("jvm-internals", ConceptDomain.CORE_CS),

    /**
     * Garbage collection: algorithms (G1, ZGC, Shenandoah), tuning, diagnostics, GC roots, generations.
     */
    GARBAGE_COLLECTION("garbage-collection", ConceptDomain.CORE_CS, JVM_INTERNALS),

    /**
     * Class loading: bootstrap/extension/app loaders, delegation model, dynamic loading, linking.
     */
    CLASS_LOADING("class-loading", ConceptDomain.CORE_CS, JVM_INTERNALS),

    /**
     * Serialization and deserialization: ObjectOutputStream, Externalizable, serialVersionUID, custom protocols.
     */
    SERIALIZATION("serialization", ConceptDomain.CORE_CS, JVM_INTERNALS),

    /**
     * JVM languages: Kotlin, Scala, Groovy, Clojure — polyglot programming on the JVM platform.
     */
    JVM_LANGUAGES("jvm-languages", ConceptDomain.CORE_CS, JVM_INTERNALS),

    // ─── Software Engineering ───────────────────────────────────────

    /**
     * Design patterns: creational, structural, behavioral (GoF, enterprise).
     */
    DESIGN_PATTERNS("design-patterns", ConceptDomain.SOFTWARE_ENGINEERING),

    /**
     * SOLID, GRASP, DRY, KISS, clean code, code smells, refactoring.
     */
    CLEAN_CODE("clean-code", ConceptDomain.SOFTWARE_ENGINEERING),

    /**
     * Unit testing, integration testing, TDD, BDD, mocking, test pyramid.
     */
    TESTING("testing", ConceptDomain.SOFTWARE_ENGINEERING),

    /**
     * REST, GraphQL, gRPC, WebSocket, API versioning, OpenAPI.
     */
    API_DESIGN("api-design", ConceptDomain.SOFTWARE_ENGINEERING),

    /**
     * Application architecture, layers, hexagonal, microservices, monolith.
     */
    ARCHITECTURE("architecture", ConceptDomain.SOFTWARE_ENGINEERING),

    /**
     * Asynchronous messaging, event-driven architecture, message queues, pub/sub,
     * CQRS, event sourcing, saga pattern, message brokers (Kafka, RabbitMQ, SQS/SNS).
     */
    ASYNC_MESSAGING("async-messaging", ConceptDomain.SOFTWARE_ENGINEERING),

    // ─── System Design & Infrastructure ─────────────────────────────

    /**
     * High-level system design: scalability, load balancing, caching, CDN, sharding.
     * Parent of HIGH_LEVEL_DESIGN and LOW_LEVEL_DESIGN sub-concepts.
     */
    SYSTEM_DESIGN("system-design", ConceptDomain.SYSTEM_DESIGN),

    /**
     * High-level design (HLD): architecture decisions, component decomposition,
     * capacity planning, back-of-envelope estimation, trade-off analysis,
     * scalability patterns (horizontal/vertical), API gateway, service mesh.
     */
    HIGH_LEVEL_DESIGN("high-level-design", ConceptDomain.SYSTEM_DESIGN, SYSTEM_DESIGN),

    /**
     * Low-level design (LLD): class/object design, OOD interview problems,
     * component interfaces, code-level design, UML diagrams, SOLID applied to
     * class structure, design patterns in context, state machines.
     */
    LOW_LEVEL_DESIGN("low-level-design", ConceptDomain.SYSTEM_DESIGN, SYSTEM_DESIGN),

    /**
     * Databases: SQL, NoSQL, indexing, query optimization, replication, schemas.
     * Parent of DATABASE_INTERNALS and DATABASE_SCALING sub-concepts.
     */
    DATABASES("databases", ConceptDomain.SYSTEM_DESIGN),

    /**
     * Database internals: storage engines, B-trees, LSM trees, write-ahead log (WAL),
     * MVCC, indexing internals, query planning and optimization, buffer pools.
     */
    DATABASE_INTERNALS("database-internals", ConceptDomain.SYSTEM_DESIGN, DATABASES),

    /**
     * Database scaling: sharding strategies, partitioning, replication (master-slave,
     * master-master), read replicas, connection pooling, federation, denormalization.
     */
    DATABASE_SCALING("database-scaling", ConceptDomain.SYSTEM_DESIGN, DATABASES),

    /**
     * Distributed systems: consensus, CAP, eventual consistency, leader election.
     * Parent of CONSENSUS_COORDINATION and RELIABILITY_PATTERNS sub-concepts.
     */
    DISTRIBUTED_SYSTEMS("distributed-systems", ConceptDomain.SYSTEM_DESIGN),

    /**
     * Consensus and coordination: Raft, Paxos, ZooKeeper, etcd, leader election,
     * distributed locks, distributed transactions, two-phase commit, three-phase commit.
     */
    CONSENSUS_COORDINATION("consensus-coordination", ConceptDomain.SYSTEM_DESIGN, DISTRIBUTED_SYSTEMS),

    /**
     * Reliability patterns: circuit breaker, retry with backoff, bulkhead, rate limiting,
     * fault tolerance, chaos engineering, SRE practices, SLOs/SLIs/SLAs, graceful degradation.
     */
    RELIABILITY_PATTERNS("reliability-patterns", ConceptDomain.SYSTEM_DESIGN, DISTRIBUTED_SYSTEMS),

    /**
     * Networking: TCP/IP, HTTP, DNS, TLS, sockets, protocols.
     * Parent of PROTOCOLS sub-concept.
     */
    NETWORKING("networking", ConceptDomain.SYSTEM_DESIGN),

    /**
     * Network protocols: HTTP/1.1, HTTP/2, HTTP/3 (QUIC), gRPC, WebSocket,
     * protocol buffers, TLS handshake, DNS resolution, REST vs RPC.
     */
    PROTOCOLS("protocols", ConceptDomain.SYSTEM_DESIGN, NETWORKING),

    /**
     * Operating systems: processes, scheduling, file systems, I/O, kernels.
     */
    OPERATING_SYSTEMS("operating-systems", ConceptDomain.SYSTEM_DESIGN),

    // ─── DevOps & Tooling ───────────────────────────────────────────

    /**
     * Continuous integration and continuous delivery/deployment pipelines.
     */
    CI_CD("ci-cd", ConceptDomain.DEVOPS_TOOLING),

    /**
     * Containers: Docker, Kubernetes, orchestration, images, registries.
     */
    CONTAINERS("containers", ConceptDomain.DEVOPS_TOOLING),

    /**
     * Git, version control, branching strategies, merge procedures.
     */
    VERSION_CONTROL("version-control", ConceptDomain.DEVOPS_TOOLING),

    /**
     * Build systems: Maven, Gradle, Ant, Bazel, Make, npm scripts.
     */
    BUILD_TOOLS("build-tools", ConceptDomain.DEVOPS_TOOLING),

    /**
     * Infrastructure as Code: Terraform, Ansible, CloudFormation, Pulumi.
     */
    INFRASTRUCTURE("infrastructure", ConceptDomain.DEVOPS_TOOLING),

    /**
     * Monitoring, observability, logging, tracing, alerting, SLI/SLO.
     */
    OBSERVABILITY("observability", ConceptDomain.DEVOPS_TOOLING),

    // ─── Security ───────────────────────────────────────────────────

    /**
     * Web security: XSS, CSRF, SQL injection, OWASP, authentication, OAuth.
     */
    WEB_SECURITY("web-security", ConceptDomain.SECURITY),

    /**
     * Cryptography, encryption, hashing, digital signatures, TLS internals.
     */
    CRYPTOGRAPHY("cryptography", ConceptDomain.SECURITY),

    // ─── AI & Data ──────────────────────────────────────────────────

    /**
     * Machine learning: supervised/unsupervised learning, models, training, inference.
     */
    MACHINE_LEARNING("machine-learning", ConceptDomain.AI_DATA),

    /**
     * Deep learning: neural networks (CNNs, RNNs, transformers), backpropagation, gradient descent.
     */
    DEEP_LEARNING("deep-learning", ConceptDomain.AI_DATA),

    /**
     * LLMs, prompt engineering, RAG, fine-tuning, AI agents.
     */
    LLM_AND_PROMPTING("llm-and-prompting", ConceptDomain.AI_DATA),

    // ─── Career & Meta ──────────────────────────────────────────────

    /**
     * Interview preparation: coding challenges, system design interviews.
     */
    INTERVIEW_PREP("interview-prep", ConceptDomain.CAREER_META),

    /**
     * Career growth, learning paths, roadmaps, skill mapping.
     */
    CAREER_DEVELOPMENT("career-development", ConceptDomain.CAREER_META),

    /**
     * Getting started: environment setup, first project, "hello world".
     */
    GETTING_STARTED("getting-started", ConceptDomain.CAREER_META),

    /**
     * Personal knowledge management: digital note-taking, CODE method, PARA method, PKM tools.
     */
    KNOWLEDGE_MANAGEMENT("knowledge-management", ConceptDomain.CAREER_META),

    // ─── Personal Development ───────────────────────────────────────

    /**
     * Self-improvement: habits, mindset, growth, motivation, discipline, reading.
     */
    SELF_IMPROVEMENT("self-improvement", ConceptDomain.PERSONAL_DEVELOPMENT),

    /**
     * Communication skills: writing, public speaking, storytelling, feedback.
     */
    COMMUNICATION_SKILLS("communication-skills", ConceptDomain.PERSONAL_DEVELOPMENT),

    /**
     * Financial literacy: budgeting, investing, personal finance, tax basics.
     */
    FINANCIAL_LITERACY("financial-literacy", ConceptDomain.PERSONAL_DEVELOPMENT),

    /**
     * Productivity habits: time management, deep work, focus, routines.
     */
    PRODUCTIVITY_HABITS("productivity-habits", ConceptDomain.PERSONAL_DEVELOPMENT);

    private final String displayName;
    private final ConceptDomain domain;
    private final ConceptArea parentArea;

    ConceptArea(final String displayName, final ConceptDomain domain)
    {
        this(displayName, domain, null);
    }

    ConceptArea(final String displayName, final ConceptDomain domain, final ConceptArea parentArea)
    {
        this.displayName = displayName;
        this.domain = domain;
        this.parentArea = parentArea;
    }

    /**
     * Returns the human-readable display name.
     *
     * @return the display name (e.g., "concurrency", "design-patterns")
     */
    public String getDisplayName()
    {
        return displayName;
    }

    /**
     * Returns the high-level domain this concept belongs to.
     *
     * @return the parent {@link ConceptDomain}
     */
    public ConceptDomain getDomain()
    {
        return domain;
    }

    /**
     * Returns the parent concept area, or {@code null} if this is a root concept.
     *
     * @return the parent {@link ConceptArea}, or {@code null}
     */
    public ConceptArea getParentArea()
    {
        return parentArea;
    }

    /**
     * Returns {@code true} if this concept has a parent concept area.
     *
     * @return {@code true} if a parent exists
     */
    public boolean hasParent()
    {
        return parentArea != null;
    }

    /**
     * Returns {@code true} if this concept is a descendant of the given concept.
     * Walks up the parent chain to check ancestry.
     *
     * @param ancestor the candidate ancestor concept
     * @return {@code true} if {@code ancestor} is a parent (direct or transitive)
     */
    public boolean isChildOf(final ConceptArea ancestor)
    {
        if (ancestor == null) {
            return false;
        }
        var current = this.parentArea;
        while (current != null) {
            if (current == ancestor) {
                return true;
            }
            current = current.parentArea;
        }
        return false;
    }

    /**
     * Resolves a {@link ConceptArea} from a string (case-insensitive, supports hyphens and spaces).
     *
     * @param value the string to parse
     * @return the matching concept area
     * @throws IllegalArgumentException if no match is found
     */
    public static ConceptArea fromString(final String value)
    {
        EnumParseUtils.requireNonBlank(value, "Concept area");
        final var normalized = EnumParseUtils.toHyphenated(value);
        for (final ConceptArea area : values()) {
            if (area.displayName.equals(normalized)
                    || area.name().equalsIgnoreCase(EnumParseUtils.toEnumName(value))) {
                return area;
            }
        }
        throw new IllegalArgumentException("Unknown concept area: '" + value + "'");
    }
}
