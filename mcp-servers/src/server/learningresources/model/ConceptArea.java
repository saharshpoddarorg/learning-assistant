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
 * <p>This enables concept-based discovery: a user asking "I want to learn about
 * concurrency" gets resources from Java, Python, and general CS — not just one language.
 *
 * @see ConceptDomain
 */
public enum ConceptArea {

    // ─── Programming Fundamentals ───────────────────────────────────

    /** Language syntax, semantics, core features (loops, types, operators). */
    LANGUAGE_BASICS("language-basics", ConceptDomain.PROGRAMMING_FUNDAMENTALS),

    /** Object-oriented concepts: classes, inheritance, polymorphism, encapsulation. */
    OOP("oop", ConceptDomain.PROGRAMMING_FUNDAMENTALS),

    /** Functional programming: lambdas, higher-order functions, immutability. */
    FUNCTIONAL_PROGRAMMING("functional-programming", ConceptDomain.PROGRAMMING_FUNDAMENTALS),

    /** New language features, version upgrades, migration guides. */
    LANGUAGE_FEATURES("language-features", ConceptDomain.PROGRAMMING_FUNDAMENTALS),

    /** Generic types, type parameters, variance, wildcards, type erasure. */
    GENERICS("generics", ConceptDomain.PROGRAMMING_FUNDAMENTALS),

    // ─── Core CS Concepts ───────────────────────────────────────────

    /** Threads, executors, locks, synchronization, async patterns (virtual threads, coroutines). */
    CONCURRENCY("concurrency", ConceptDomain.CORE_CS),

    /** Data structures: arrays, lists, trees, graphs, heaps, tries, hash tables. */
    DATA_STRUCTURES("data-structures", ConceptDomain.CORE_CS),

    /** Algorithms: sorting, searching, dynamic programming, greedy, backtracking. */
    ALGORITHMS("algorithms", ConceptDomain.CORE_CS),

    /** Mathematical foundations: linear algebra, calculus, probability, discrete math, statistics. */
    MATHEMATICS("mathematics", ConceptDomain.CORE_CS),

    /** Big-O analysis, time/space complexity, benchmarking, optimization. */
    COMPLEXITY_ANALYSIS("complexity-analysis", ConceptDomain.CORE_CS),

    /** Memory management, garbage collection, allocation strategies. */
    MEMORY_MANAGEMENT("memory-management", ConceptDomain.CORE_CS),

    // ─── Software Engineering ───────────────────────────────────────

    /** Design patterns: creational, structural, behavioral (GoF, enterprise). */
    DESIGN_PATTERNS("design-patterns", ConceptDomain.SOFTWARE_ENGINEERING),

    /** SOLID, GRASP, DRY, KISS, clean code, code smells, refactoring. */
    CLEAN_CODE("clean-code", ConceptDomain.SOFTWARE_ENGINEERING),

    /** Unit testing, integration testing, TDD, BDD, mocking, test pyramid. */
    TESTING("testing", ConceptDomain.SOFTWARE_ENGINEERING),

    /** REST, GraphQL, gRPC, WebSocket, API versioning, OpenAPI. */
    API_DESIGN("api-design", ConceptDomain.SOFTWARE_ENGINEERING),

    /** Application architecture, layers, hexagonal, microservices, monolith. */
    ARCHITECTURE("architecture", ConceptDomain.SOFTWARE_ENGINEERING),

    // ─── System Design & Infrastructure ─────────────────────────────

    /** High-level system design: scalability, load balancing, caching, CDN, sharding. */
    SYSTEM_DESIGN("system-design", ConceptDomain.SYSTEM_DESIGN),

    /** Databases: SQL, NoSQL, indexing, query optimization, replication, schemas. */
    DATABASES("databases", ConceptDomain.SYSTEM_DESIGN),

    /** Distributed systems: consensus, CAP, eventual consistency, leader election. */
    DISTRIBUTED_SYSTEMS("distributed-systems", ConceptDomain.SYSTEM_DESIGN),

    /** Networking: TCP/IP, HTTP, DNS, TLS, sockets, protocols. */
    NETWORKING("networking", ConceptDomain.SYSTEM_DESIGN),

    /** Operating systems: processes, scheduling, file systems, I/O, kernels. */
    OPERATING_SYSTEMS("operating-systems", ConceptDomain.SYSTEM_DESIGN),

    // ─── DevOps & Tooling ───────────────────────────────────────────

    /** Continuous integration and continuous delivery/deployment pipelines. */
    CI_CD("ci-cd", ConceptDomain.DEVOPS_TOOLING),

    /** Containers: Docker, Kubernetes, orchestration, images, registries. */
    CONTAINERS("containers", ConceptDomain.DEVOPS_TOOLING),

    /** Git, version control, branching strategies, merge procedures. */
    VERSION_CONTROL("version-control", ConceptDomain.DEVOPS_TOOLING),

    /** Build systems: Maven, Gradle, Ant, Bazel, Make, npm scripts. */
    BUILD_TOOLS("build-tools", ConceptDomain.DEVOPS_TOOLING),

    /** Infrastructure as Code: Terraform, Ansible, CloudFormation, Pulumi. */
    INFRASTRUCTURE("infrastructure", ConceptDomain.DEVOPS_TOOLING),

    /** Monitoring, observability, logging, tracing, alerting, SLI/SLO. */
    OBSERVABILITY("observability", ConceptDomain.DEVOPS_TOOLING),

    // ─── Security ───────────────────────────────────────────────────

    /** Web security: XSS, CSRF, SQL injection, OWASP, authentication, OAuth. */
    WEB_SECURITY("web-security", ConceptDomain.SECURITY),

    /** Cryptography, encryption, hashing, digital signatures, TLS internals. */
    CRYPTOGRAPHY("cryptography", ConceptDomain.SECURITY),

    // ─── AI & Data ──────────────────────────────────────────────────

    /** Machine learning: supervised/unsupervised learning, models, training, inference. */
    MACHINE_LEARNING("machine-learning", ConceptDomain.AI_DATA),

    /** Deep learning: neural networks (CNNs, RNNs, transformers), backpropagation, gradient descent. */
    DEEP_LEARNING("deep-learning", ConceptDomain.AI_DATA),

    /** LLMs, prompt engineering, RAG, fine-tuning, AI agents. */
    LLM_AND_PROMPTING("llm-and-prompting", ConceptDomain.AI_DATA),

    // ─── Career & Meta ──────────────────────────────────────────────

    /** Interview preparation: coding challenges, system design interviews. */
    INTERVIEW_PREP("interview-prep", ConceptDomain.CAREER_META),

    /** Career growth, learning paths, roadmaps, skill mapping. */
    CAREER_DEVELOPMENT("career-development", ConceptDomain.CAREER_META),

    /** Getting started: environment setup, first project, "hello world". */
    GETTING_STARTED("getting-started", ConceptDomain.CAREER_META),

    /** Personal knowledge management: digital note-taking, CODE method, PARA method, PKM tools. */
    KNOWLEDGE_MANAGEMENT("knowledge-management", ConceptDomain.CAREER_META);

    private final String displayName;
    private final ConceptDomain domain;

    ConceptArea(final String displayName, final ConceptDomain domain) {
        this.displayName = displayName;
        this.domain = domain;
    }

    /**
     * Returns the human-readable display name.
     *
     * @return the display name (e.g., "concurrency", "design-patterns")
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the high-level domain this concept belongs to.
     *
     * @return the parent {@link ConceptDomain}
     */
    public ConceptDomain getDomain() {
        return domain;
    }

    /**
     * Resolves a {@link ConceptArea} from a string (case-insensitive, supports hyphens and spaces).
     *
     * @param value the string to parse
     * @return the matching concept area
     * @throws IllegalArgumentException if no match is found
     */
    public static ConceptArea fromString(final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Concept area must not be null or blank");
        }
        final var normalized = value.strip().toLowerCase().replace(' ', '-');
        for (final ConceptArea area : values()) {
            if (area.displayName.equals(normalized) || area.name().equalsIgnoreCase(value.strip().replace('-', '_'))) {
                return area;
            }
        }
        throw new IllegalArgumentException("Unknown concept area: '" + value + "'");
    }
}
