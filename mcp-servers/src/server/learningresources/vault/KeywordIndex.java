package server.learningresources.vault;

import server.learningresources.model.ConceptArea;
import server.learningresources.model.DifficultyLevel;
import server.learningresources.model.ResourceCategory;

import java.util.HashMap;
import java.util.Map;

/**
 * Static keyword-to-enum mappings used by the discovery engine for intent inference.
 *
 * <p>When a user types a vague query like "java concurrency beginner", the keyword
 * index resolves individual words and phrases into structured enums:
 * <ul>
 *   <li>{@code "concurrency"} → {@link ConceptArea#CONCURRENCY}</li>
 *   <li>{@code "java"} → {@link ResourceCategory#JAVA}</li>
 *   <li>{@code "beginner"} → {@link DifficultyLevel#BEGINNER}</li>
 * </ul>
 *
 * <p>Extracted from {@link ResourceDiscovery} for maintainability — this file is
 * the single place to add new keyword synonyms without touching the scoring or
 * query-handling logic.
 */
public final class KeywordIndex {

    private KeywordIndex() {
        // Static utility — no instances
    }

    // ─── Keyword → Concept ──────────────────────────────────────────

    /** Immutable keyword-to-concept map (80+ entries). */
    private static final Map<String, ConceptArea> CONCEPT_MAP = buildConceptMap();

    /**
     * Returns the keyword-to-concept mapping.
     *
     * @return unmodifiable map from lowercase keyword to {@link ConceptArea}
     */
    public static Map<String, ConceptArea> conceptMap() {
        return CONCEPT_MAP;
    }

    private static Map<String, ConceptArea> buildConceptMap() {
        final var map = new HashMap<String, ConceptArea>();

        // Programming fundamentals
        map.put("oop", ConceptArea.OOP);
        map.put("object-oriented", ConceptArea.OOP);
        map.put("classes", ConceptArea.OOP);
        map.put("inheritance", ConceptArea.OOP);
        map.put("polymorphism", ConceptArea.OOP);
        map.put("encapsulation", ConceptArea.OOP);
        map.put("functional", ConceptArea.FUNCTIONAL_PROGRAMMING);
        map.put("lambda", ConceptArea.FUNCTIONAL_PROGRAMMING);
        map.put("lambdas", ConceptArea.FUNCTIONAL_PROGRAMMING);
        map.put("streams", ConceptArea.FUNCTIONAL_PROGRAMMING);
        map.put("generics", ConceptArea.GENERICS);
        map.put("type-system", ConceptArea.GENERICS);
        map.put("wildcards", ConceptArea.GENERICS);

        // Core CS
        map.put("concurrency", ConceptArea.CONCURRENCY);
        map.put("threads", ConceptArea.CONCURRENCY);
        map.put("async", ConceptArea.CONCURRENCY);
        map.put("parallel", ConceptArea.CONCURRENCY);
        map.put("virtual threads", ConceptArea.CONCURRENCY);
        map.put("synchronization", ConceptArea.CONCURRENCY);
        map.put("data structures", ConceptArea.DATA_STRUCTURES);
        map.put("collections", ConceptArea.DATA_STRUCTURES);
        map.put("list", ConceptArea.DATA_STRUCTURES);
        map.put("map", ConceptArea.DATA_STRUCTURES);
        map.put("tree", ConceptArea.DATA_STRUCTURES);
        map.put("trees", ConceptArea.DATA_STRUCTURES);
        map.put("graph", ConceptArea.DATA_STRUCTURES);
        map.put("graphs", ConceptArea.DATA_STRUCTURES);
        map.put("heap", ConceptArea.DATA_STRUCTURES);
        map.put("trie", ConceptArea.DATA_STRUCTURES);
        map.put("stack", ConceptArea.DATA_STRUCTURES);
        map.put("queue", ConceptArea.DATA_STRUCTURES);
        map.put("deque", ConceptArea.DATA_STRUCTURES);
        map.put("linked list", ConceptArea.DATA_STRUCTURES);
        map.put("linked-list", ConceptArea.DATA_STRUCTURES);
        map.put("hash table", ConceptArea.DATA_STRUCTURES);
        map.put("hashmap", ConceptArea.DATA_STRUCTURES);
        map.put("bst", ConceptArea.DATA_STRUCTURES);
        map.put("binary search tree", ConceptArea.DATA_STRUCTURES);
        map.put("avl", ConceptArea.DATA_STRUCTURES);
        map.put("avl tree", ConceptArea.DATA_STRUCTURES);
        map.put("red-black tree", ConceptArea.DATA_STRUCTURES);
        map.put("union-find", ConceptArea.DATA_STRUCTURES);
        map.put("disjoint set", ConceptArea.DATA_STRUCTURES);
        map.put("neetcode", ConceptArea.DATA_STRUCTURES);
        map.put("java collections", ConceptArea.DATA_STRUCTURES);
        map.put("algorithms", ConceptArea.ALGORITHMS);
        map.put("sorting", ConceptArea.ALGORITHMS);
        map.put("searching", ConceptArea.ALGORITHMS);
        map.put("dynamic programming", ConceptArea.ALGORITHMS);
        map.put("big-o", ConceptArea.COMPLEXITY_ANALYSIS);
        map.put("complexity", ConceptArea.COMPLEXITY_ANALYSIS);
        map.put("time complexity", ConceptArea.COMPLEXITY_ANALYSIS);
        map.put("memory", ConceptArea.MEMORY_MANAGEMENT);
        map.put("heap", ConceptArea.MEMORY_MANAGEMENT);

        // JVM internals (umbrella)
        map.put("jvm", ConceptArea.JVM_INTERNALS);
        map.put("jvm internals", ConceptArea.JVM_INTERNALS);
        map.put("jvm-internals", ConceptArea.JVM_INTERNALS);
        map.put("bytecode", ConceptArea.JVM_INTERNALS);
        map.put("jit", ConceptArea.JVM_INTERNALS);
        map.put("jit compiler", ConceptArea.JVM_INTERNALS);
        map.put("jit compilation", ConceptArea.JVM_INTERNALS);
        map.put("hotspot", ConceptArea.JVM_INTERNALS);
        map.put("jmx", ConceptArea.JVM_INTERNALS);
        map.put("safepoint", ConceptArea.JVM_INTERNALS);
        map.put("safepoints", ConceptArea.JVM_INTERNALS);
        map.put("tlab", ConceptArea.JVM_INTERNALS);
        map.put("metaspace", ConceptArea.JVM_INTERNALS);
        map.put("jvm tuning", ConceptArea.JVM_INTERNALS);
        map.put("jvm performance", ConceptArea.JVM_INTERNALS);
        map.put("mechanical sympathy", ConceptArea.JVM_INTERNALS);

        // Garbage collection (sub-concept of JVM_INTERNALS)
        map.put("garbage collection", ConceptArea.GARBAGE_COLLECTION);
        map.put("gc", ConceptArea.GARBAGE_COLLECTION);
        map.put("gc tuning", ConceptArea.GARBAGE_COLLECTION);
        map.put("g1", ConceptArea.GARBAGE_COLLECTION);
        map.put("zgc", ConceptArea.GARBAGE_COLLECTION);
        map.put("shenandoah", ConceptArea.GARBAGE_COLLECTION);
        map.put("gc roots", ConceptArea.GARBAGE_COLLECTION);
        map.put("gc logging", ConceptArea.GARBAGE_COLLECTION);
        map.put("gc pause", ConceptArea.GARBAGE_COLLECTION);
        map.put("gc algorithm", ConceptArea.GARBAGE_COLLECTION);
        map.put("generational gc", ConceptArea.GARBAGE_COLLECTION);
        map.put("young generation", ConceptArea.GARBAGE_COLLECTION);
        map.put("old generation", ConceptArea.GARBAGE_COLLECTION);
        map.put("serial gc", ConceptArea.GARBAGE_COLLECTION);
        map.put("parallel gc", ConceptArea.GARBAGE_COLLECTION);
        map.put("epsilon gc", ConceptArea.GARBAGE_COLLECTION);
        map.put("gc ergonomics", ConceptArea.GARBAGE_COLLECTION);

        // Class loading (sub-concept of JVM_INTERNALS)
        map.put("class loading", ConceptArea.CLASS_LOADING);
        map.put("classloader", ConceptArea.CLASS_LOADING);
        map.put("class-loading", ConceptArea.CLASS_LOADING);
        map.put("class loader", ConceptArea.CLASS_LOADING);
        map.put("bootstrap classloader", ConceptArea.CLASS_LOADING);
        map.put("delegation model", ConceptArea.CLASS_LOADING);
        map.put("dynamic loading", ConceptArea.CLASS_LOADING);
        map.put("class linking", ConceptArea.CLASS_LOADING);
        map.put("class initialization", ConceptArea.CLASS_LOADING);

        // Serialization (sub-concept of JVM_INTERNALS)
        map.put("serialization", ConceptArea.SERIALIZATION);
        map.put("deserialization", ConceptArea.SERIALIZATION);
        map.put("serializable", ConceptArea.SERIALIZATION);
        map.put("externalizable", ConceptArea.SERIALIZATION);
        map.put("serialVersionUID", ConceptArea.SERIALIZATION);
        map.put("object stream", ConceptArea.SERIALIZATION);
        map.put("transient", ConceptArea.SERIALIZATION);
        map.put("writeObject", ConceptArea.SERIALIZATION);
        map.put("readObject", ConceptArea.SERIALIZATION);
        map.put("java serialization", ConceptArea.SERIALIZATION);

        // JVM languages (sub-concept of JVM_INTERNALS)
        map.put("kotlin", ConceptArea.JVM_LANGUAGES);
        map.put("scala", ConceptArea.JVM_LANGUAGES);
        map.put("groovy", ConceptArea.JVM_LANGUAGES);
        map.put("clojure", ConceptArea.JVM_LANGUAGES);
        map.put("jvm languages", ConceptArea.JVM_LANGUAGES);
        map.put("jvm-languages", ConceptArea.JVM_LANGUAGES);
        map.put("jvm language", ConceptArea.JVM_LANGUAGES);
        map.put("polyglot jvm", ConceptArea.JVM_LANGUAGES);
        map.put("coroutines", ConceptArea.JVM_LANGUAGES);

        // GraalVM (spans JVM_INTERNALS umbrella — architecture + native image)
        map.put("graalvm", ConceptArea.JVM_INTERNALS);
        map.put("native image", ConceptArea.JVM_INTERNALS);

        // JVM profiling tools
        map.put("jfr", ConceptArea.JVM_INTERNALS);
        map.put("java flight recorder", ConceptArea.JVM_INTERNALS);
        map.put("jmh", ConceptArea.JVM_INTERNALS);

        // Software engineering
        map.put("design patterns", ConceptArea.DESIGN_PATTERNS);
        map.put("patterns", ConceptArea.DESIGN_PATTERNS);
        map.put("singleton", ConceptArea.DESIGN_PATTERNS);
        map.put("factory", ConceptArea.DESIGN_PATTERNS);
        map.put("observer", ConceptArea.DESIGN_PATTERNS);
        map.put("strategy", ConceptArea.DESIGN_PATTERNS);
        map.put("clean code", ConceptArea.CLEAN_CODE);
        map.put("refactoring", ConceptArea.CLEAN_CODE);
        map.put("best practices", ConceptArea.CLEAN_CODE);
        map.put("solid", ConceptArea.CLEAN_CODE);
        map.put("clean architecture", ConceptArea.CLEAN_CODE);
        map.put("uncle bob", ConceptArea.CLEAN_CODE);
        map.put("robert c martin", ConceptArea.CLEAN_CODE);
        map.put("code smells", ConceptArea.CLEAN_CODE);
        map.put("code forensics", ConceptArea.CLEAN_CODE);
        map.put("crime scene", ConceptArea.CLEAN_CODE);
        map.put("complexity management", ConceptArea.CLEAN_CODE);
        map.put("testing", ConceptArea.TESTING);
        map.put("unit test", ConceptArea.TESTING);
        map.put("unit testing", ConceptArea.TESTING);
        map.put("test pyramid", ConceptArea.TESTING);
        map.put("test anti-patterns", ConceptArea.TESTING);
        map.put("vladimir khorikov", ConceptArea.TESTING);
        map.put("tdd", ConceptArea.TESTING);
        map.put("junit", ConceptArea.TESTING);
        map.put("mocking", ConceptArea.TESTING);
        map.put("api", ConceptArea.API_DESIGN);
        map.put("rest", ConceptArea.API_DESIGN);
        map.put("graphql", ConceptArea.API_DESIGN);
        map.put("architecture", ConceptArea.ARCHITECTURE);
        map.put("microservices", ConceptArea.ARCHITECTURE);
        map.put("hexagonal", ConceptArea.ARCHITECTURE);
        map.put("enterprise patterns", ConceptArea.ARCHITECTURE);
        map.put("poeaa", ConceptArea.ARCHITECTURE);
        map.put("ports and adapters", ConceptArea.ARCHITECTURE);
        map.put("onion architecture", ConceptArea.ARCHITECTURE);
        map.put("volatility-based decomposition", ConceptArea.ARCHITECTURE);
        map.put("volatility decomposition", ConceptArea.ARCHITECTURE);
        map.put("righting software", ConceptArea.ARCHITECTURE);
        map.put("juval lowy", ConceptArea.ARCHITECTURE);
        map.put("service decomposition", ConceptArea.ARCHITECTURE);
        map.put("clean agile", ConceptArea.CLEAN_CODE);

        // System design & infra
        map.put("system design", ConceptArea.SYSTEM_DESIGN);
        map.put("scalability", ConceptArea.SYSTEM_DESIGN);
        map.put("load balancing", ConceptArea.SYSTEM_DESIGN);

        // High-level design (sub-concept of SYSTEM_DESIGN)
        map.put("hld", ConceptArea.HIGH_LEVEL_DESIGN);
        map.put("high level design", ConceptArea.HIGH_LEVEL_DESIGN);
        map.put("high-level design", ConceptArea.HIGH_LEVEL_DESIGN);
        map.put("capacity planning", ConceptArea.HIGH_LEVEL_DESIGN);
        map.put("back-of-envelope", ConceptArea.HIGH_LEVEL_DESIGN);
        map.put("back of envelope", ConceptArea.HIGH_LEVEL_DESIGN);
        map.put("trade-off analysis", ConceptArea.HIGH_LEVEL_DESIGN);
        map.put("api gateway", ConceptArea.HIGH_LEVEL_DESIGN);
        map.put("service mesh", ConceptArea.HIGH_LEVEL_DESIGN);
        map.put("horizontal scaling", ConceptArea.HIGH_LEVEL_DESIGN);
        map.put("vertical scaling", ConceptArea.HIGH_LEVEL_DESIGN);
        map.put("cdn", ConceptArea.HIGH_LEVEL_DESIGN);
        map.put("content delivery network", ConceptArea.HIGH_LEVEL_DESIGN);
        map.put("reverse proxy", ConceptArea.HIGH_LEVEL_DESIGN);
        map.put("caching", ConceptArea.HIGH_LEVEL_DESIGN);
        map.put("cache invalidation", ConceptArea.HIGH_LEVEL_DESIGN);
        map.put("bytebytego", ConceptArea.HIGH_LEVEL_DESIGN);

        // Low-level design (sub-concept of SYSTEM_DESIGN)
        map.put("lld", ConceptArea.LOW_LEVEL_DESIGN);
        map.put("low level design", ConceptArea.LOW_LEVEL_DESIGN);
        map.put("low-level design", ConceptArea.LOW_LEVEL_DESIGN);
        map.put("ood", ConceptArea.LOW_LEVEL_DESIGN);
        map.put("object-oriented design", ConceptArea.LOW_LEVEL_DESIGN);
        map.put("class design", ConceptArea.LOW_LEVEL_DESIGN);
        map.put("class diagram", ConceptArea.LOW_LEVEL_DESIGN);
        map.put("uml", ConceptArea.LOW_LEVEL_DESIGN);
        map.put("sequence diagram", ConceptArea.LOW_LEVEL_DESIGN);
        map.put("state machine", ConceptArea.LOW_LEVEL_DESIGN);
        map.put("machine coding", ConceptArea.LOW_LEVEL_DESIGN);
        map.put("component interface", ConceptArea.LOW_LEVEL_DESIGN);
        map.put("deep module", ConceptArea.LOW_LEVEL_DESIGN);
        map.put("information hiding", ConceptArea.LOW_LEVEL_DESIGN);

        map.put("database", ConceptArea.DATABASES);
        map.put("sql", ConceptArea.DATABASES);
        map.put("postgresql", ConceptArea.DATABASES);
        map.put("indexing", ConceptArea.DATABASES);

        // Database internals (sub-concept of DATABASES)
        map.put("database internals", ConceptArea.DATABASE_INTERNALS);
        map.put("storage engine", ConceptArea.DATABASE_INTERNALS);
        map.put("storage engines", ConceptArea.DATABASE_INTERNALS);
        map.put("b-tree", ConceptArea.DATABASE_INTERNALS);
        map.put("b-trees", ConceptArea.DATABASE_INTERNALS);
        map.put("lsm tree", ConceptArea.DATABASE_INTERNALS);
        map.put("lsm-tree", ConceptArea.DATABASE_INTERNALS);
        map.put("write-ahead log", ConceptArea.DATABASE_INTERNALS);
        map.put("wal", ConceptArea.DATABASE_INTERNALS);
        map.put("mvcc", ConceptArea.DATABASE_INTERNALS);
        map.put("query planning", ConceptArea.DATABASE_INTERNALS);
        map.put("query optimizer", ConceptArea.DATABASE_INTERNALS);
        map.put("buffer pool", ConceptArea.DATABASE_INTERNALS);

        // Database scaling (sub-concept of DATABASES)
        map.put("database scaling", ConceptArea.DATABASE_SCALING);
        map.put("database sharding", ConceptArea.DATABASE_SCALING);
        map.put("sharding", ConceptArea.DATABASE_SCALING);
        map.put("partitioning", ConceptArea.DATABASE_SCALING);
        map.put("replication", ConceptArea.DATABASE_SCALING);
        map.put("read replica", ConceptArea.DATABASE_SCALING);
        map.put("read replicas", ConceptArea.DATABASE_SCALING);
        map.put("master-slave", ConceptArea.DATABASE_SCALING);
        map.put("master-master", ConceptArea.DATABASE_SCALING);
        map.put("connection pooling", ConceptArea.DATABASE_SCALING);
        map.put("federation", ConceptArea.DATABASE_SCALING);
        map.put("denormalization", ConceptArea.DATABASE_SCALING);

        map.put("distributed", ConceptArea.DISTRIBUTED_SYSTEMS);
        map.put("consensus", ConceptArea.DISTRIBUTED_SYSTEMS);

        // Consensus & coordination (sub-concept of DISTRIBUTED_SYSTEMS)
        map.put("raft", ConceptArea.CONSENSUS_COORDINATION);
        map.put("raft consensus", ConceptArea.CONSENSUS_COORDINATION);
        map.put("paxos", ConceptArea.CONSENSUS_COORDINATION);
        map.put("zookeeper", ConceptArea.CONSENSUS_COORDINATION);
        map.put("etcd", ConceptArea.CONSENSUS_COORDINATION);
        map.put("leader election", ConceptArea.CONSENSUS_COORDINATION);
        map.put("distributed lock", ConceptArea.CONSENSUS_COORDINATION);
        map.put("distributed locks", ConceptArea.CONSENSUS_COORDINATION);
        map.put("two-phase commit", ConceptArea.CONSENSUS_COORDINATION);
        map.put("2pc", ConceptArea.CONSENSUS_COORDINATION);
        map.put("distributed consensus", ConceptArea.CONSENSUS_COORDINATION);
        map.put("distributed coordination", ConceptArea.CONSENSUS_COORDINATION);

        // Reliability patterns (sub-concept of DISTRIBUTED_SYSTEMS)
        map.put("circuit breaker", ConceptArea.RELIABILITY_PATTERNS);
        map.put("circuit-breaker", ConceptArea.RELIABILITY_PATTERNS);
        map.put("retry", ConceptArea.RELIABILITY_PATTERNS);
        map.put("retry pattern", ConceptArea.RELIABILITY_PATTERNS);
        map.put("exponential backoff", ConceptArea.RELIABILITY_PATTERNS);
        map.put("bulkhead", ConceptArea.RELIABILITY_PATTERNS);
        map.put("bulkhead pattern", ConceptArea.RELIABILITY_PATTERNS);
        map.put("rate limiting", ConceptArea.RELIABILITY_PATTERNS);
        map.put("rate limiter", ConceptArea.RELIABILITY_PATTERNS);
        map.put("rate-limiting", ConceptArea.RELIABILITY_PATTERNS);
        map.put("fault tolerance", ConceptArea.RELIABILITY_PATTERNS);
        map.put("chaos engineering", ConceptArea.RELIABILITY_PATTERNS);
        map.put("chaos monkey", ConceptArea.RELIABILITY_PATTERNS);
        map.put("sre", ConceptArea.RELIABILITY_PATTERNS);
        map.put("site reliability", ConceptArea.RELIABILITY_PATTERNS);
        map.put("site reliability engineering", ConceptArea.RELIABILITY_PATTERNS);
        map.put("slo", ConceptArea.RELIABILITY_PATTERNS);
        map.put("sli", ConceptArea.RELIABILITY_PATTERNS);
        map.put("sla", ConceptArea.RELIABILITY_PATTERNS);
        map.put("graceful degradation", ConceptArea.RELIABILITY_PATTERNS);
        map.put("failover", ConceptArea.RELIABILITY_PATTERNS);
        map.put("cloud design patterns", ConceptArea.RELIABILITY_PATTERNS);

        map.put("networking", ConceptArea.NETWORKING);
        map.put("http", ConceptArea.NETWORKING);
        map.put("tcp", ConceptArea.NETWORKING);
        map.put("dns", ConceptArea.NETWORKING);

        // Protocols (sub-concept of NETWORKING)
        map.put("http2", ConceptArea.PROTOCOLS);
        map.put("http/2", ConceptArea.PROTOCOLS);
        map.put("http3", ConceptArea.PROTOCOLS);
        map.put("http/3", ConceptArea.PROTOCOLS);
        map.put("quic", ConceptArea.PROTOCOLS);
        map.put("grpc", ConceptArea.PROTOCOLS);
        map.put("websocket", ConceptArea.PROTOCOLS);
        map.put("websockets", ConceptArea.PROTOCOLS);
        map.put("protobuf", ConceptArea.PROTOCOLS);
        map.put("protocol buffers", ConceptArea.PROTOCOLS);
        map.put("tls handshake", ConceptArea.PROTOCOLS);
        map.put("rest vs rpc", ConceptArea.PROTOCOLS);
        map.put("udp", ConceptArea.PROTOCOLS);

        map.put("operating systems", ConceptArea.OPERATING_SYSTEMS);
        map.put("processes", ConceptArea.OPERATING_SYSTEMS);

        // DevOps & tooling
        map.put("ci/cd", ConceptArea.CI_CD);
        map.put("ci-cd", ConceptArea.CI_CD);
        map.put("pipeline", ConceptArea.CI_CD);
        map.put("github actions", ConceptArea.CI_CD);
        map.put("docker", ConceptArea.CONTAINERS);
        map.put("kubernetes", ConceptArea.CONTAINERS);
        map.put("containers", ConceptArea.CONTAINERS);
        map.put("k8s", ConceptArea.CONTAINERS);
        map.put("git", ConceptArea.VERSION_CONTROL);
        map.put("version control", ConceptArea.VERSION_CONTROL);
        map.put("branching", ConceptArea.VERSION_CONTROL);
        map.put("conventional commits", ConceptArea.VERSION_CONTROL);
        map.put("conventional-commits", ConceptArea.VERSION_CONTROL);
        map.put("commit message", ConceptArea.VERSION_CONTROL);
        map.put("commit convention", ConceptArea.VERSION_CONTROL);
        map.put("gitflow", ConceptArea.VERSION_CONTROL);
        map.put("trunk-based", ConceptArea.VERSION_CONTROL);
        map.put("semver", ConceptArea.VERSION_CONTROL);
        map.put("semantic versioning", ConceptArea.VERSION_CONTROL);
        map.put("rebase", ConceptArea.VERSION_CONTROL);
        map.put("merge", ConceptArea.VERSION_CONTROL);
        map.put("stash", ConceptArea.VERSION_CONTROL);
        map.put("cherry-pick", ConceptArea.VERSION_CONTROL);
        map.put("hooks", ConceptArea.VERSION_CONTROL);
        map.put("pull requests", ConceptArea.VERSION_CONTROL);
        map.put("gradle", ConceptArea.BUILD_TOOLS);
        map.put("maven", ConceptArea.BUILD_TOOLS);
        map.put("build", ConceptArea.BUILD_TOOLS);
        map.put("pom", ConceptArea.BUILD_TOOLS);
        map.put("pom.xml", ConceptArea.BUILD_TOOLS);
        map.put("mvn", ConceptArea.BUILD_TOOLS);
        map.put("bazel", ConceptArea.BUILD_TOOLS);
        map.put("make", ConceptArea.BUILD_TOOLS);
        map.put("makefile", ConceptArea.BUILD_TOOLS);
        map.put("npm", ConceptArea.BUILD_TOOLS);
        map.put("yarn", ConceptArea.BUILD_TOOLS);
        map.put("pnpm", ConceptArea.BUILD_TOOLS);
        map.put("ant", ConceptArea.BUILD_TOOLS);
        map.put("package manager", ConceptArea.BUILD_TOOLS);
        map.put("build tool", ConceptArea.BUILD_TOOLS);
        map.put("dependency", ConceptArea.BUILD_TOOLS);
        map.put("monitoring", ConceptArea.OBSERVABILITY);
        map.put("logging", ConceptArea.OBSERVABILITY);
        map.put("tracing", ConceptArea.OBSERVABILITY);
        map.put("prometheus", ConceptArea.OBSERVABILITY);
        map.put("grafana", ConceptArea.OBSERVABILITY);
        map.put("elk", ConceptArea.OBSERVABILITY);
        map.put("alerting", ConceptArea.OBSERVABILITY);
        map.put("dashboards", ConceptArea.OBSERVABILITY);

        // Infrastructure as Code
        map.put("terraform", ConceptArea.INFRASTRUCTURE);
        map.put("ansible", ConceptArea.INFRASTRUCTURE);
        map.put("iac", ConceptArea.INFRASTRUCTURE);
        map.put("infrastructure as code", ConceptArea.INFRASTRUCTURE);
        map.put("cloudformation", ConceptArea.INFRASTRUCTURE);
        map.put("pulumi", ConceptArea.INFRASTRUCTURE);

        // Cloud platforms
        map.put("aws", ConceptArea.INFRASTRUCTURE);
        map.put("gcp", ConceptArea.INFRASTRUCTURE);
        map.put("azure", ConceptArea.INFRASTRUCTURE);
        map.put("cloud", ConceptArea.INFRASTRUCTURE);
        map.put("ec2", ConceptArea.INFRASTRUCTURE);
        map.put("s3", ConceptArea.INFRASTRUCTURE);
        map.put("lambda", ConceptArea.INFRASTRUCTURE);

        // Frameworks
        map.put("spring boot", ConceptArea.ARCHITECTURE);
        map.put("spring-boot", ConceptArea.ARCHITECTURE);
        map.put("react", ConceptArea.ARCHITECTURE);
        map.put("angular", ConceptArea.ARCHITECTURE);
        map.put("vue", ConceptArea.ARCHITECTURE);
        map.put("vuejs", ConceptArea.ARCHITECTURE);
        map.put("nextjs", ConceptArea.ARCHITECTURE);
        map.put("next.js", ConceptArea.ARCHITECTURE);
        map.put("express", ConceptArea.API_DESIGN);
        map.put("expressjs", ConceptArea.API_DESIGN);
        map.put("django", ConceptArea.ARCHITECTURE);
        map.put("flask", ConceptArea.API_DESIGN);
        map.put("hibernate", ConceptArea.DATABASES);
        map.put("jpa", ConceptArea.DATABASES);
        map.put("orm", ConceptArea.DATABASES);
        map.put("nodejs", ConceptArea.ARCHITECTURE);
        map.put("node.js", ConceptArea.ARCHITECTURE);

        // Async messaging & event-driven architecture
        map.put("messaging", ConceptArea.ASYNC_MESSAGING);
        map.put("message queue", ConceptArea.ASYNC_MESSAGING);
        map.put("message-queue", ConceptArea.ASYNC_MESSAGING);
        map.put("message broker", ConceptArea.ASYNC_MESSAGING);
        map.put("message-broker", ConceptArea.ASYNC_MESSAGING);
        map.put("event-driven", ConceptArea.ASYNC_MESSAGING);
        map.put("event driven", ConceptArea.ASYNC_MESSAGING);
        map.put("event-driven architecture", ConceptArea.ASYNC_MESSAGING);
        map.put("eda", ConceptArea.ASYNC_MESSAGING);
        map.put("pub/sub", ConceptArea.ASYNC_MESSAGING);
        map.put("pub-sub", ConceptArea.ASYNC_MESSAGING);
        map.put("publish-subscribe", ConceptArea.ASYNC_MESSAGING);
        map.put("cqrs", ConceptArea.ASYNC_MESSAGING);
        map.put("event sourcing", ConceptArea.ASYNC_MESSAGING);
        map.put("event-sourcing", ConceptArea.ASYNC_MESSAGING);
        map.put("saga pattern", ConceptArea.ASYNC_MESSAGING);
        map.put("saga", ConceptArea.ASYNC_MESSAGING);
        map.put("kafka", ConceptArea.ASYNC_MESSAGING);
        map.put("rabbitmq", ConceptArea.ASYNC_MESSAGING);
        map.put("amqp", ConceptArea.ASYNC_MESSAGING);
        map.put("sqs", ConceptArea.ASYNC_MESSAGING);
        map.put("sns", ConceptArea.ASYNC_MESSAGING);
        map.put("eventbridge", ConceptArea.ASYNC_MESSAGING);
        map.put("event streaming", ConceptArea.ASYNC_MESSAGING);
        map.put("event-streaming", ConceptArea.ASYNC_MESSAGING);
        map.put("integration patterns", ConceptArea.ASYNC_MESSAGING);
        map.put("enterprise integration", ConceptArea.ASYNC_MESSAGING);
        map.put("transactional outbox", ConceptArea.ASYNC_MESSAGING);
        map.put("async communication", ConceptArea.ASYNC_MESSAGING);
        map.put("asynchronous messaging", ConceptArea.ASYNC_MESSAGING);

        // Data stores
        map.put("redis", ConceptArea.DATABASES);
        map.put("mongodb", ConceptArea.DATABASES);
        map.put("nosql", ConceptArea.DATABASES);
        map.put("elasticsearch", ConceptArea.DATABASES);
        map.put("mysql", ConceptArea.DATABASES);
        map.put("dynamodb", ConceptArea.DATABASES);

        // Testing tools
        map.put("mockito", ConceptArea.TESTING);
        map.put("selenium", ConceptArea.TESTING);
        map.put("cypress", ConceptArea.TESTING);
        map.put("pytest", ConceptArea.TESTING);
        map.put("testcontainers", ConceptArea.TESTING);
        map.put("e2e", ConceptArea.TESTING);
        map.put("end-to-end", ConceptArea.TESTING);

        // Security
        map.put("security", ConceptArea.WEB_SECURITY);
        map.put("owasp", ConceptArea.WEB_SECURITY);
        map.put("xss", ConceptArea.WEB_SECURITY);
        map.put("injection", ConceptArea.WEB_SECURITY);
        map.put("authentication", ConceptArea.WEB_SECURITY);
        map.put("cryptography", ConceptArea.CRYPTOGRAPHY);
        map.put("encryption", ConceptArea.CRYPTOGRAPHY);
        map.put("hashing", ConceptArea.CRYPTOGRAPHY);

        // AI & data
        map.put("machine learning", ConceptArea.MACHINE_LEARNING);
        map.put("deep learning", ConceptArea.MACHINE_LEARNING);
        map.put("neural", ConceptArea.MACHINE_LEARNING);
        map.put("ai", ConceptArea.MACHINE_LEARNING);
        map.put("llm", ConceptArea.LLM_AND_PROMPTING);
        map.put("prompt", ConceptArea.LLM_AND_PROMPTING);
        map.put("gpt", ConceptArea.LLM_AND_PROMPTING);
        map.put("rag", ConceptArea.LLM_AND_PROMPTING);

        // Career & meta
        map.put("interview", ConceptArea.INTERVIEW_PREP);
        map.put("leetcode", ConceptArea.INTERVIEW_PREP);
        map.put("career", ConceptArea.CAREER_DEVELOPMENT);
        map.put("roadmap", ConceptArea.CAREER_DEVELOPMENT);
        map.put("getting started", ConceptArea.GETTING_STARTED);
        map.put("beginner", ConceptArea.GETTING_STARTED);
        map.put("hello world", ConceptArea.GETTING_STARTED);

        // Knowledge management & digital note-taking
        map.put("note-taking", ConceptArea.KNOWLEDGE_MANAGEMENT);
        map.put("notetaking", ConceptArea.KNOWLEDGE_MANAGEMENT);
        map.put("notes", ConceptArea.KNOWLEDGE_MANAGEMENT);
        map.put("notion", ConceptArea.KNOWLEDGE_MANAGEMENT);
        map.put("obsidian", ConceptArea.KNOWLEDGE_MANAGEMENT);
        map.put("onenote", ConceptArea.KNOWLEDGE_MANAGEMENT);
        map.put("logseq", ConceptArea.KNOWLEDGE_MANAGEMENT);
        map.put("roam", ConceptArea.KNOWLEDGE_MANAGEMENT);
        map.put("pkm", ConceptArea.KNOWLEDGE_MANAGEMENT);
        map.put("second brain", ConceptArea.KNOWLEDGE_MANAGEMENT);
        map.put("second-brain", ConceptArea.KNOWLEDGE_MANAGEMENT);
        map.put("para method", ConceptArea.KNOWLEDGE_MANAGEMENT);
        map.put("para", ConceptArea.KNOWLEDGE_MANAGEMENT);
        map.put("code method", ConceptArea.KNOWLEDGE_MANAGEMENT);
        map.put("zettelkasten", ConceptArea.KNOWLEDGE_MANAGEMENT);
        map.put("tiago forte", ConceptArea.KNOWLEDGE_MANAGEMENT);
        map.put("knowledge management", ConceptArea.KNOWLEDGE_MANAGEMENT);
        map.put("digital notes", ConceptArea.KNOWLEDGE_MANAGEMENT);
        map.put("gtd", ConceptArea.KNOWLEDGE_MANAGEMENT);
        map.put("getting things done", ConceptArea.KNOWLEDGE_MANAGEMENT);
        map.put("david allen", ConceptArea.KNOWLEDGE_MANAGEMENT);
        map.put("basb", ConceptArea.KNOWLEDGE_MANAGEMENT);
        map.put("progressive summarization", ConceptArea.KNOWLEDGE_MANAGEMENT);
        map.put("progressive-summarization", ConceptArea.KNOWLEDGE_MANAGEMENT);
        map.put("foam", ConceptArea.KNOWLEDGE_MANAGEMENT);
        map.put("adr", ConceptArea.KNOWLEDGE_MANAGEMENT);
        map.put("architecture decision record", ConceptArea.KNOWLEDGE_MANAGEMENT);
        map.put("daily notes", ConceptArea.KNOWLEDGE_MANAGEMENT);
        map.put("daily journal", ConceptArea.KNOWLEDGE_MANAGEMENT);
        map.put("obsidian plugins", ConceptArea.KNOWLEDGE_MANAGEMENT);
        map.put("dataview", ConceptArea.KNOWLEDGE_MANAGEMENT);
        map.put("templater", ConceptArea.KNOWLEDGE_MANAGEMENT);
        map.put("migration", ConceptArea.KNOWLEDGE_MANAGEMENT);
        map.put("todoist", ConceptArea.KNOWLEDGE_MANAGEMENT);
        // Playlist / video series
        map.put("playlist", ConceptArea.GETTING_STARTED);
        map.put("video series", ConceptArea.GETTING_STARTED);
        map.put("youtube playlist", ConceptArea.GETTING_STARTED);
        // CS50
        map.put("cs50", ConceptArea.GETTING_STARTED);
        map.put("harvard cs", ConceptArea.GETTING_STARTED);
        // 3Blue1Brown
        map.put("3blue1brown", ConceptArea.DEEP_LEARNING);
        map.put("3b1b", ConceptArea.DEEP_LEARNING);
        map.put("linear algebra", ConceptArea.MATHEMATICS);
        map.put("calculus", ConceptArea.MATHEMATICS);
        map.put("statistics", ConceptArea.MATHEMATICS);
        map.put("probability", ConceptArea.MATHEMATICS);
        map.put("discrete math", ConceptArea.MATHEMATICS);
        map.put("neural network", ConceptArea.DEEP_LEARNING);
        map.put("neural networks", ConceptArea.DEEP_LEARNING);
        map.put("deep learning", ConceptArea.DEEP_LEARNING);
        map.put("gradient descent", ConceptArea.DEEP_LEARNING);
        map.put("backpropagation", ConceptArea.DEEP_LEARNING);
        map.put("transformers", ConceptArea.DEEP_LEARNING);

        // Personal development
        map.put("self-improvement", ConceptArea.SELF_IMPROVEMENT);
        map.put("self improvement", ConceptArea.SELF_IMPROVEMENT);
        map.put("personal development", ConceptArea.SELF_IMPROVEMENT);
        map.put("personal growth", ConceptArea.SELF_IMPROVEMENT);
        map.put("habits", ConceptArea.SELF_IMPROVEMENT);
        map.put("atomic habits", ConceptArea.SELF_IMPROVEMENT);
        map.put("mindset", ConceptArea.SELF_IMPROVEMENT);
        map.put("growth mindset", ConceptArea.SELF_IMPROVEMENT);
        map.put("motivation", ConceptArea.SELF_IMPROVEMENT);
        map.put("discipline", ConceptArea.SELF_IMPROVEMENT);
        map.put("self-help", ConceptArea.SELF_IMPROVEMENT);
        map.put("reading list", ConceptArea.SELF_IMPROVEMENT);
        map.put("communication", ConceptArea.COMMUNICATION_SKILLS);
        map.put("public speaking", ConceptArea.COMMUNICATION_SKILLS);
        map.put("writing", ConceptArea.COMMUNICATION_SKILLS);
        map.put("storytelling", ConceptArea.COMMUNICATION_SKILLS);
        map.put("presentation", ConceptArea.COMMUNICATION_SKILLS);
        map.put("soft skills", ConceptArea.COMMUNICATION_SKILLS);
        map.put("financial literacy", ConceptArea.FINANCIAL_LITERACY);
        map.put("personal finance", ConceptArea.FINANCIAL_LITERACY);
        map.put("budgeting", ConceptArea.FINANCIAL_LITERACY);
        map.put("investing", ConceptArea.FINANCIAL_LITERACY);
        map.put("tax", ConceptArea.FINANCIAL_LITERACY);
        map.put("deep work", ConceptArea.PRODUCTIVITY_HABITS);
        map.put("time management", ConceptArea.PRODUCTIVITY_HABITS);
        map.put("focus", ConceptArea.PRODUCTIVITY_HABITS);
        map.put("pomodoro", ConceptArea.PRODUCTIVITY_HABITS);
        map.put("routine", ConceptArea.PRODUCTIVITY_HABITS);
        return Map.copyOf(map);
    }

    // ─── Keyword → Category ─────────────────────────────────────────

    /** Immutable keyword-to-category map. */
    private static final Map<String, ResourceCategory> CATEGORY_MAP = buildCategoryMap();

    /**
     * Returns the keyword-to-category mapping.
     *
     * @return unmodifiable map from lowercase keyword to {@link ResourceCategory}
     */
    public static Map<String, ResourceCategory> categoryMap() {
        return CATEGORY_MAP;
    }

    private static Map<String, ResourceCategory> buildCategoryMap() {
        final var map = new HashMap<String, ResourceCategory>();
        map.put("java", ResourceCategory.JAVA);
        map.put("spring", ResourceCategory.JAVA);
        map.put("jdk", ResourceCategory.JAVA);
        map.put("jvm", ResourceCategory.JAVA);
        map.put("python", ResourceCategory.PYTHON);
        map.put("django", ResourceCategory.PYTHON);
        map.put("flask", ResourceCategory.PYTHON);
        map.put("javascript", ResourceCategory.JAVASCRIPT);
        map.put("typescript", ResourceCategory.JAVASCRIPT);
        map.put("node", ResourceCategory.JAVASCRIPT);
        map.put("react", ResourceCategory.JAVASCRIPT);
        map.put("npm", ResourceCategory.JAVASCRIPT);
        map.put("yarn", ResourceCategory.JAVASCRIPT);
        map.put("pnpm", ResourceCategory.JAVASCRIPT);
        map.put("web", ResourceCategory.WEB);
        map.put("html", ResourceCategory.WEB);
        map.put("css", ResourceCategory.WEB);
        map.put("frontend", ResourceCategory.WEB);
        map.put("devops", ResourceCategory.DEVOPS);
        map.put("docker", ResourceCategory.DEVOPS);
        map.put("kubernetes", ResourceCategory.DEVOPS);
        map.put("git", ResourceCategory.DEVOPS);
        map.put("maven", ResourceCategory.DEVOPS);
        map.put("gradle", ResourceCategory.DEVOPS);
        map.put("bazel", ResourceCategory.DEVOPS);
        map.put("security", ResourceCategory.SECURITY);
        map.put("owasp", ResourceCategory.SECURITY);
        map.put("data", ResourceCategory.DATABASE);
        map.put("database", ResourceCategory.DATABASE);
        map.put("sql", ResourceCategory.DATABASE);
        map.put("ai", ResourceCategory.AI_ML);
        map.put("ml", ResourceCategory.AI_ML);
        map.put("machine learning", ResourceCategory.AI_ML);
        map.put("deep learning", ResourceCategory.AI_ML);
        map.put("algorithm", ResourceCategory.ALGORITHMS);
        map.put("algorithms", ResourceCategory.ALGORITHMS);
        map.put("data structures", ResourceCategory.ALGORITHMS);
        map.put("data-structures", ResourceCategory.ALGORITHMS);
        map.put("ds", ResourceCategory.ALGORITHMS);
        map.put("dsa", ResourceCategory.ALGORITHMS);
        map.put("linked list", ResourceCategory.ALGORITHMS);
        map.put("trees", ResourceCategory.ALGORITHMS);
        map.put("graphs", ResourceCategory.ALGORITHMS);
        map.put("heaps", ResourceCategory.ALGORITHMS);
        map.put("trie", ResourceCategory.ALGORITHMS);
        map.put("hash table", ResourceCategory.ALGORITHMS);
        map.put("neetcode", ResourceCategory.ALGORITHMS);
        map.put("testing", ResourceCategory.TESTING);
        map.put("junit", ResourceCategory.TESTING);
        map.put("engineering", ResourceCategory.SOFTWARE_ENGINEERING);
        map.put("software engineering book", ResourceCategory.SOFTWARE_ENGINEERING);
        map.put("clean architecture", ResourceCategory.SOFTWARE_ENGINEERING);
        map.put("refactoring book", ResourceCategory.SOFTWARE_ENGINEERING);
        map.put("poeaa", ResourceCategory.SOFTWARE_ENGINEERING);
        map.put("uncle bob", ResourceCategory.SOFTWARE_ENGINEERING);
        map.put("martin fowler", ResourceCategory.SOFTWARE_ENGINEERING);
        map.put("adam tornhill", ResourceCategory.SOFTWARE_ENGINEERING);
        map.put("juval lowy", ResourceCategory.SOFTWARE_ENGINEERING);
        map.put("vladimir khorikov", ResourceCategory.SOFTWARE_ENGINEERING);
        map.put("righting software", ResourceCategory.SOFTWARE_ENGINEERING);
        map.put("volatility decomposition", ResourceCategory.SOFTWARE_ENGINEERING);
        map.put("clean agile", ResourceCategory.SOFTWARE_ENGINEERING);
        map.put("clean code book", ResourceCategory.SOFTWARE_ENGINEERING);
        map.put("unit testing book", ResourceCategory.SOFTWARE_ENGINEERING);
        map.put("ebook", ResourceCategory.SOFTWARE_ENGINEERING);
        map.put("notion", ResourceCategory.PRODUCTIVITY);
        map.put("obsidian", ResourceCategory.PRODUCTIVITY);
        map.put("onenote", ResourceCategory.PRODUCTIVITY);
        map.put("logseq", ResourceCategory.PRODUCTIVITY);
        map.put("pkm", ResourceCategory.PRODUCTIVITY);
        map.put("note-taking", ResourceCategory.PRODUCTIVITY);
        map.put("productivity", ResourceCategory.PRODUCTIVITY);
        map.put("gtd", ResourceCategory.PRODUCTIVITY);
        map.put("para", ResourceCategory.PRODUCTIVITY);
        map.put("second-brain", ResourceCategory.PRODUCTIVITY);
        map.put("todoist", ResourceCategory.PRODUCTIVITY);
        map.put("foam", ResourceCategory.PRODUCTIVITY);
        map.put("zettelkasten", ResourceCategory.PRODUCTIVITY);
        // CS50 / Harvard
        map.put("cs50", ResourceCategory.GENERAL);
        map.put("harvard", ResourceCategory.GENERAL);
        // Playlist / video series
        map.put("playlist", ResourceCategory.GENERAL);
        map.put("video series", ResourceCategory.GENERAL);
        // 3Blue1Brown / math
        map.put("3blue1brown", ResourceCategory.AI_ML);
        map.put("3b1b", ResourceCategory.AI_ML);
        map.put("linear algebra", ResourceCategory.AI_ML);
        // Cloud
        map.put("aws", ResourceCategory.CLOUD);
        map.put("amazon web services", ResourceCategory.CLOUD);
        map.put("gcp", ResourceCategory.CLOUD);
        map.put("google cloud", ResourceCategory.CLOUD);
        map.put("azure", ResourceCategory.CLOUD);
        map.put("cloud", ResourceCategory.CLOUD);
        map.put("terraform", ResourceCategory.CLOUD);
        map.put("ansible", ResourceCategory.CLOUD);
        map.put("iac", ResourceCategory.CLOUD);
        map.put("ec2", ResourceCategory.CLOUD);
        map.put("s3", ResourceCategory.CLOUD);
        map.put("lambda", ResourceCategory.CLOUD);
        // Data stores
        map.put("redis", ResourceCategory.DATABASE);
        map.put("mongodb", ResourceCategory.DATABASE);
        map.put("nosql", ResourceCategory.DATABASE);
        map.put("elasticsearch", ResourceCategory.DATABASE);
        map.put("mysql", ResourceCategory.DATABASE);
        map.put("dynamodb", ResourceCategory.DATABASE);
        map.put("postgresql", ResourceCategory.DATABASE);
        // Messaging & event-driven
        map.put("kafka", ResourceCategory.SOFTWARE_ENGINEERING);
        map.put("rabbitmq", ResourceCategory.SOFTWARE_ENGINEERING);
        map.put("messaging", ResourceCategory.SOFTWARE_ENGINEERING);
        map.put("message queue", ResourceCategory.SOFTWARE_ENGINEERING);
        map.put("event-driven", ResourceCategory.SOFTWARE_ENGINEERING);
        map.put("cqrs", ResourceCategory.SOFTWARE_ENGINEERING);
        map.put("event sourcing", ResourceCategory.SOFTWARE_ENGINEERING);
        map.put("pub-sub", ResourceCategory.SOFTWARE_ENGINEERING);
        map.put("amqp", ResourceCategory.SOFTWARE_ENGINEERING);
        map.put("sqs", ResourceCategory.CLOUD);
        map.put("sns", ResourceCategory.CLOUD);
        map.put("eventbridge", ResourceCategory.CLOUD);
        // Observability
        map.put("prometheus", ResourceCategory.DEVOPS);
        map.put("grafana", ResourceCategory.DEVOPS);
        map.put("monitoring", ResourceCategory.DEVOPS);
        map.put("elk", ResourceCategory.DEVOPS);
        // Testing tools
        map.put("mockito", ResourceCategory.TESTING);
        map.put("selenium", ResourceCategory.TESTING);
        map.put("cypress", ResourceCategory.TESTING);
        map.put("pytest", ResourceCategory.TESTING);
        map.put("testcontainers", ResourceCategory.TESTING);
        map.put("e2e", ResourceCategory.TESTING);
        // Frameworks (additional)
        map.put("angular", ResourceCategory.JAVASCRIPT);
        map.put("vue", ResourceCategory.JAVASCRIPT);
        map.put("vuejs", ResourceCategory.JAVASCRIPT);
        map.put("nextjs", ResourceCategory.JAVASCRIPT);
        map.put("expressjs", ResourceCategory.JAVASCRIPT);
        map.put("spring-boot", ResourceCategory.JAVA);
        map.put("spring boot", ResourceCategory.JAVA);
        map.put("hibernate", ResourceCategory.JAVA);
        map.put("jpa", ResourceCategory.JAVA);
        // JVM languages & ecosystem
        map.put("kotlin", ResourceCategory.JAVA);
        map.put("scala", ResourceCategory.JAVA);
        map.put("groovy", ResourceCategory.JAVA);
        map.put("clojure", ResourceCategory.JAVA);
        map.put("graalvm", ResourceCategory.JAVA);
        map.put("jvm languages", ResourceCategory.JAVA);
        map.put("jvm-languages", ResourceCategory.JAVA);
        map.put("hotspot", ResourceCategory.JAVA);
        map.put("bytecode", ResourceCategory.JAVA);
        map.put("jfr", ResourceCategory.JAVA);
        map.put("jmh", ResourceCategory.JAVA);
        map.put("native image", ResourceCategory.JAVA);
        // Personal development
        map.put("personal development", ResourceCategory.PERSONAL_DEVELOPMENT);
        map.put("self-improvement", ResourceCategory.PERSONAL_DEVELOPMENT);
        map.put("self-help", ResourceCategory.PERSONAL_DEVELOPMENT);
        map.put("personal growth", ResourceCategory.PERSONAL_DEVELOPMENT);
        map.put("habits", ResourceCategory.PERSONAL_DEVELOPMENT);
        map.put("mindset", ResourceCategory.PERSONAL_DEVELOPMENT);
        map.put("soft skills", ResourceCategory.PERSONAL_DEVELOPMENT);
        map.put("communication", ResourceCategory.PERSONAL_DEVELOPMENT);
        map.put("public speaking", ResourceCategory.PERSONAL_DEVELOPMENT);
        map.put("financial literacy", ResourceCategory.PERSONAL_DEVELOPMENT);
        map.put("personal finance", ResourceCategory.PERSONAL_DEVELOPMENT);
        map.put("budgeting", ResourceCategory.PERSONAL_DEVELOPMENT);
        map.put("investing", ResourceCategory.PERSONAL_DEVELOPMENT);
        map.put("deep work", ResourceCategory.PERSONAL_DEVELOPMENT);
        map.put("time management", ResourceCategory.PERSONAL_DEVELOPMENT);
        return Map.copyOf(map);
    }

    // ─── Keyword → Difficulty ───────────────────────────────────────

    /** Immutable keyword-to-difficulty map. */
    private static final Map<String, DifficultyLevel> DIFFICULTY_MAP = buildDifficultyMap();

    /**
     * Returns the keyword-to-difficulty mapping.
     *
     * @return unmodifiable map from lowercase keyword to {@link DifficultyLevel}
     */
    public static Map<String, DifficultyLevel> difficultyMap() {
        return DIFFICULTY_MAP;
    }

    private static Map<String, DifficultyLevel> buildDifficultyMap() {
        final var map = new HashMap<String, DifficultyLevel>();
        map.put("beginner", DifficultyLevel.BEGINNER);
        map.put("new", DifficultyLevel.BEGINNER);
        map.put("start", DifficultyLevel.BEGINNER);
        map.put("easy", DifficultyLevel.BEGINNER);
        map.put("intro", DifficultyLevel.BEGINNER);
        map.put("basic", DifficultyLevel.BEGINNER);
        map.put("intermediate", DifficultyLevel.INTERMEDIATE);
        map.put("moderate", DifficultyLevel.INTERMEDIATE);
        map.put("mid", DifficultyLevel.INTERMEDIATE);
        map.put("advanced", DifficultyLevel.ADVANCED);
        map.put("deep", DifficultyLevel.ADVANCED);
        map.put("hard", DifficultyLevel.ADVANCED);
        map.put("expert", DifficultyLevel.EXPERT);
        map.put("master", DifficultyLevel.EXPERT);
        return Map.copyOf(map);
    }
}
