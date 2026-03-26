package server.learningresources.vault.providers;

import server.learningresources.model.*;
import server.learningresources.vault.ResourceProvider;

import java.time.Instant;
import java.util.List;

/**
 * Curated JVM internals, performance, and ecosystem learning resources.
 *
 * <p>Covers the full spectrum of JVM knowledge beyond the Java language itself,
 * organized into a sub-hierarchy under {@link ConceptArea#JVM_INTERNALS}:
 * <ul>
 *   <li><b>JVM architecture</b> ({@link ConceptArea#JVM_INTERNALS}): specification, bytecode, JIT compilation, runtime data areas</li>
 *   <li><b>Garbage collection</b> ({@link ConceptArea#GARBAGE_COLLECTION}): algorithms (G1, ZGC, Shenandoah), tuning, diagnostics</li>
 *   <li><b>Class loading</b> ({@link ConceptArea#CLASS_LOADING}): bootstrap/extension/app loaders, delegation model, linking</li>
 *   <li><b>Serialization</b> ({@link ConceptArea#SERIALIZATION}): ObjectOutputStream, Externalizable, serialVersionUID</li>
 *   <li><b>JVM performance</b>: JMH benchmarking, JFR/JMC profiling, memory analysis</li>
 *   <li><b>JVM languages</b> ({@link ConceptArea#JVM_LANGUAGES}): Kotlin, Scala, Groovy, Clojure — polyglot on the JVM</li>
 *   <li><b>GraalVM</b>: native image, polyglot runtime, ahead-of-time compilation</li>
 * </ul>
 *
 * <p>Resources span all difficulty tiers:
 * {@link DifficultyLevel#BEGINNER} (JVM overview, GC basics),
 * {@link DifficultyLevel#INTERMEDIATE} (GC tuning guides, JVM specification),
 * {@link DifficultyLevel#ADVANCED} (JVM Anatomy Quarks, JIT internals),
 * {@link DifficultyLevel#EXPERT} (JVM specification, Mechanical Sympathy).
 *
 * @see JavaResources
 * @see EngineeringResources
 */
public final class JvmResources implements ResourceProvider {

    @Override
    public List<LearningResource> resources() {
        final var now = Instant.now();
        return List.of(

                // ─── JVM Specification & Architecture ───────────────────────

                new LearningResource(
                        "jvm-specification",
                        "The Java Virtual Machine Specification (JVMS) — SE 21",
                        "https://docs.oracle.com/javase/specs/jvms/se21/html/index.html",
                        "The formal specification of the JVM. Defines the class file format, "
                                + "bytecode instruction set, class loading and linking, runtime "
                                + "data areas (stack, heap, method area), and verification. "
                                + "Essential for understanding how the JVM executes code.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.JAVA, ResourceCategory.SYSTEMS),
                        List.of(ConceptArea.JVM_INTERNALS,
                                ConceptArea.CLASS_LOADING,
                                ConceptArea.MEMORY_MANAGEMENT,
                                ConceptArea.LANGUAGE_FEATURES),
                        List.of("official", "jvms", "specification", "bytecode", "class-file",
                                "class-loading", "instruction-set", "verification"),
                        "Oracle",
                        DifficultyLevel.EXPERT,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_SPECIFIC, now
                ),

                new LearningResource(
                        "jvm-anatomy-quarks",
                        "JVM Anatomy Quarks — Aleksey Shipilëv",
                        "https://shipilev.net/jvm/anatomy-quarks/",
                        "A series of 30 deep-dive mini-posts on JVM internals by Red Hat's "
                                + "JVM performance engineer. Each post covers one elementary "
                                + "topic: lock coarsening, TLABs, safepoints, compressed "
                                + "references, scalar replacement, JIT constants, and more. "
                                + "The gold standard for JVM internals education.",
                        ResourceType.ARTICLE,
                        List.of(ResourceCategory.JAVA, ResourceCategory.SYSTEMS),
                        List.of(ConceptArea.JVM_INTERNALS,
                                ConceptArea.GARBAGE_COLLECTION,
                                ConceptArea.MEMORY_MANAGEMENT,
                                ConceptArea.CONCURRENCY),
                        List.of("jvm-internals", "hotspot", "tlab", "safepoints", "gc",
                                "jit", "compressed-oops", "lock-elision", "scalar-replacement"),
                        "Aleksey Shipilëv",
                        DifficultyLevel.ADVANCED,
                        ContentFreshness.EVERGREEN,
                        false, true, LanguageApplicability.JAVA_SPECIFIC, now
                ),

                // ─── Garbage Collection ─────────────────────────────────────

                new LearningResource(
                        "oracle-gc-tuning-guide",
                        "HotSpot JVM Garbage Collection Tuning Guide",
                        "https://docs.oracle.com/en/java/javase/21/gctuning/",
                        "Official Oracle guide to garbage collection tuning in HotSpot. "
                                + "Covers GC fundamentals, ergonomics, G1 Garbage Collector, "
                                + "ZGC, parallel and serial collectors, heap sizing, pause-time "
                                + "goals, and GC logging. The authoritative reference for "
                                + "production GC tuning.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.JAVA, ResourceCategory.SYSTEMS),
                        List.of(ConceptArea.GARBAGE_COLLECTION,
                                ConceptArea.JVM_INTERNALS,
                                ConceptArea.MEMORY_MANAGEMENT),
                        List.of("official", "gc", "garbage-collection", "g1", "zgc",
                                "gc-tuning", "heap", "pause-time", "gc-logging"),
                        "Oracle",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_SPECIFIC, now
                ),

                new LearningResource(
                        "baeldung-jvm-gc",
                        "JVM Garbage Collection — Baeldung",
                        "https://www.baeldung.com/jvm-garbage-collectors",
                        "Comprehensive overview of JVM garbage collectors: Serial, Parallel, "
                                + "CMS, G1, ZGC, and Shenandoah. Explains generational "
                                + "collection, GC roots, young/old generation, minor/major/full "
                                + "GC, and how to choose the right collector for your workload.",
                        ResourceType.ARTICLE,
                        List.of(ResourceCategory.JAVA),
                        List.of(ConceptArea.GARBAGE_COLLECTION,
                                ConceptArea.JVM_INTERNALS,
                                ConceptArea.MEMORY_MANAGEMENT),
                        List.of("gc", "garbage-collection", "g1", "zgc", "shenandoah",
                                "serial-gc", "parallel-gc", "generational", "gc-roots"),
                        "Baeldung",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.JAVA_SPECIFIC, now
                ),

                // ─── JVM Performance & Profiling ────────────────────────────

                new LearningResource(
                        "jmh-openjdk",
                        "JMH — Java Microbenchmark Harness",
                        "https://github.com/openjdk/jmh",
                        "The official OpenJDK micro-benchmarking framework. Handles JVM "
                                + "warm-up, JIT compilation effects, dead-code elimination, "
                                + "and other benchmarking pitfalls. Essential for writing "
                                + "reliable Java performance benchmarks.",
                        ResourceType.REPOSITORY,
                        List.of(ResourceCategory.JAVA, ResourceCategory.TOOLS),
                        List.of(ConceptArea.JVM_INTERNALS, ConceptArea.COMPLEXITY_ANALYSIS,
                                ConceptArea.TESTING),
                        List.of("jmh", "benchmarking", "performance", "microbenchmark",
                                "warm-up", "jit", "openjdk"),
                        "OpenJDK",
                        DifficultyLevel.ADVANCED,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_SPECIFIC, now
                ),

                new LearningResource(
                        "baeldung-jvm-parameters",
                        "JVM Parameters Guide — Baeldung",
                        "https://www.baeldung.com/jvm-parameters",
                        "Practical guide to the most important JVM parameters: heap size "
                                + "(-Xms, -Xmx), GC selection (-XX:+UseG1GC, -XX:+UseZGC), "
                                + "stack size (-Xss), metaspace, GC logging, JIT flags, and "
                                + "diagnostic options. A handy reference for JVM tuning.",
                        ResourceType.ARTICLE,
                        List.of(ResourceCategory.JAVA),
                        List.of(ConceptArea.JVM_INTERNALS,
                                ConceptArea.GARBAGE_COLLECTION,
                                ConceptArea.MEMORY_MANAGEMENT),
                        List.of("jvm-flags", "jvm-parameters", "heap-size", "xmx", "xms",
                                "gc-flags", "metaspace", "jvm-tuning"),
                        "Baeldung",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.JAVA_SPECIFIC, now
                ),

                new LearningResource(
                        "baeldung-jvm-memory",
                        "JVM Memory Structure — Baeldung",
                        "https://www.baeldung.com/java-stack-heap",
                        "Explains JVM memory areas: stack vs heap, method area, runtime "
                                + "constant pool, program counter, and native method stacks. "
                                + "Covers how objects, local variables, and method frames are "
                                + "stored, and common OutOfMemoryError scenarios.",
                        ResourceType.ARTICLE,
                        List.of(ResourceCategory.JAVA),
                        List.of(ConceptArea.JVM_INTERNALS,
                                ConceptArea.MEMORY_MANAGEMENT,
                                ConceptArea.GARBAGE_COLLECTION),
                        List.of("stack", "heap", "method-area", "runtime-data-areas",
                                "memory-structure", "oom", "out-of-memory"),
                        "Baeldung",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.JAVA_SPECIFIC, now
                ),

                // ─── GraalVM ────────────────────────────────────────────────

                new LearningResource(
                        "graalvm",
                        "GraalVM — High-Performance Polyglot Runtime",
                        "https://www.graalvm.org/",
                        "Oracle's polyglot VM and platform. Features ahead-of-time "
                                + "compilation (Native Image) for instant startup, the Graal "
                                + "JIT compiler for peak throughput, and polyglot support "
                                + "(JavaScript, Python, Ruby, R, LLVM) on the JVM. Supported "
                                + "by Spring Boot, Micronaut, Helidon, and Quarkus.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.JAVA, ResourceCategory.SYSTEMS),
                        List.of(ConceptArea.JVM_INTERNALS, ConceptArea.ARCHITECTURE,
                                ConceptArea.CONTAINERS),
                        List.of("graalvm", "native-image", "aot", "ahead-of-time",
                                "polyglot", "jit-compiler", "graal", "truffle"),
                        "Oracle / GraalVM Team",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_SPECIFIC, now
                ),

                // ─── JVM Languages ──────────────────────────────────────────

                new LearningResource(
                        "kotlin-official",
                        "Kotlin Programming Language",
                        "https://kotlinlang.org/docs/home.html",
                        "Official Kotlin documentation. Kotlin is a modern, statically-typed "
                                + "JVM language by JetBrains. Features null safety, coroutines, "
                                + "extension functions, data classes, and full Java interop. "
                                + "The preferred language for Android development and "
                                + "increasingly popular for server-side JVM applications.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.JAVA, ResourceCategory.GENERAL),
                        List.of(ConceptArea.JVM_LANGUAGES,
                                ConceptArea.JVM_INTERNALS,
                                ConceptArea.LANGUAGE_FEATURES,
                                ConceptArea.CONCURRENCY,
                                ConceptArea.FUNCTIONAL_PROGRAMMING),
                        List.of("kotlin", "jvm-language", "coroutines", "null-safety",
                                "android", "jetbrains", "java-interop"),
                        "JetBrains",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_CENTRIC, now
                ),

                new LearningResource(
                        "scala-official",
                        "Scala Programming Language",
                        "https://docs.scala-lang.org/",
                        "Official Scala documentation. Scala combines object-oriented and "
                                + "functional programming on the JVM. Features pattern matching, "
                                + "algebraic data types, type inference, implicit conversions, "
                                + "and a powerful type system. Used extensively in data "
                                + "engineering (Apache Spark) and distributed systems (Akka).",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.JAVA, ResourceCategory.GENERAL),
                        List.of(ConceptArea.JVM_LANGUAGES,
                                ConceptArea.JVM_INTERNALS,
                                ConceptArea.LANGUAGE_FEATURES,
                                ConceptArea.FUNCTIONAL_PROGRAMMING,
                                ConceptArea.OOP),
                        List.of("scala", "jvm-language", "functional", "pattern-matching",
                                "akka", "spark", "type-system"),
                        "Scala Center / EPFL",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_CENTRIC, now
                ),

                new LearningResource(
                        "groovy-official",
                        "Apache Groovy Programming Language",
                        "https://groovy-lang.org/documentation.html",
                        "Official Groovy documentation. Groovy is a dynamic, optionally-typed "
                                + "JVM language with static compilation capabilities. Powers "
                                + "Gradle build scripts, Jenkins pipelines, and Spock testing "
                                + "framework. Seamless Java interop with concise syntax.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.JAVA, ResourceCategory.GENERAL),
                        List.of(ConceptArea.JVM_LANGUAGES,
                                ConceptArea.JVM_INTERNALS,
                                ConceptArea.LANGUAGE_FEATURES,
                                ConceptArea.BUILD_TOOLS),
                        List.of("groovy", "jvm-language", "dynamic", "gradle-dsl",
                                "jenkins", "spock", "scripting"),
                        "Apache Software Foundation",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_CENTRIC, now
                ),

                new LearningResource(
                        "clojure-official",
                        "Clojure — A Lisp for the JVM",
                        "https://clojure.org/guides/getting_started",
                        "Official Clojure getting started guide. Clojure is a dynamic, "
                                + "functional Lisp dialect running on the JVM. Emphasizes "
                                + "immutability, persistent data structures, and concurrency "
                                + "primitives (atoms, refs, agents, STM). Popular for "
                                + "data-driven applications and interactive development (REPL).",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.JAVA, ResourceCategory.GENERAL),
                        List.of(ConceptArea.JVM_LANGUAGES,
                                ConceptArea.JVM_INTERNALS,
                                ConceptArea.LANGUAGE_FEATURES,
                                ConceptArea.FUNCTIONAL_PROGRAMMING,
                                ConceptArea.CONCURRENCY),
                        List.of("clojure", "jvm-language", "lisp", "functional",
                                "immutability", "repl", "persistent-data-structures"),
                        "Rich Hickey / Nubank",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_CENTRIC, now
                ),

                // ─── Curated Collections ────────────────────────────────────

                new LearningResource(
                        "awesome-jvm",
                        "Awesome JVM — Curated JVM Resources",
                        "https://github.com/deephacks/awesome-jvm",
                        "Community-curated list of JVM performance and internals resources. "
                                + "Covers bytecode tools, garbage collectors (G1, ZGC, "
                                + "Shenandoah, Epsilon), profilers (async-profiler, JFR, JMH, "
                                + "JOL), JVM languages, memory/concurrency libraries, "
                                + "networking (Netty, Aeron), and metaprogramming. 2.2k+ stars.",
                        ResourceType.REPOSITORY,
                        List.of(ResourceCategory.JAVA, ResourceCategory.SYSTEMS),
                        List.of(ConceptArea.JVM_INTERNALS,
                                ConceptArea.GARBAGE_COLLECTION,
                                ConceptArea.MEMORY_MANAGEMENT,
                                ConceptArea.CONCURRENCY),
                        List.of("awesome-list", "jvm", "performance", "profiling",
                                "bytecode", "gc", "netty", "jvm-languages"),
                        "deephacks / Community",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.HISTORICAL,
                        false, true, LanguageApplicability.JAVA_SPECIFIC, now
                ),

                new LearningResource(
                        "mechanical-sympathy",
                        "Mechanical Sympathy — Martin Thompson",
                        "https://mechanical-sympathy.blogspot.com/",
                        "Blog by Martin Thompson exploring how hardware and software "
                                + "interact for high-performance computing on the JVM. "
                                + "Covers CPU caches, false sharing, lock-free algorithms, "
                                + "memory models, and writing JVM code that works with "
                                + "the hardware rather than against it.",
                        ResourceType.BLOG,
                        List.of(ResourceCategory.JAVA, ResourceCategory.SYSTEMS),
                        List.of(ConceptArea.JVM_INTERNALS, ConceptArea.CONCURRENCY,
                                ConceptArea.MEMORY_MANAGEMENT),
                        List.of("mechanical-sympathy", "performance", "cpu-cache",
                                "false-sharing", "lock-free", "memory-model", "low-latency"),
                        "Martin Thompson",
                        DifficultyLevel.EXPERT,
                        ContentFreshness.EVERGREEN,
                        false, true, LanguageApplicability.JAVA_SPECIFIC, now
                ),

                // ─── JVM Monitoring & Diagnostics ───────────────────────────

                new LearningResource(
                        "oracle-jfr-guide",
                        "Java Flight Recorder (JFR) — Getting Started",
                        "https://docs.oracle.com/en/java/javase/21/jfapi/",
                        "Official Oracle guide to Java Flight Recorder, the low-overhead "
                                + "profiling and diagnostics framework built into the JDK. "
                                + "Covers JFR events, recording configurations, JDK Mission "
                                + "Control (JMC) integration, and custom event creation. "
                                + "Essential for production JVM monitoring.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.JAVA, ResourceCategory.TOOLS),
                        List.of(ConceptArea.JVM_INTERNALS, ConceptArea.OBSERVABILITY),
                        List.of("official", "jfr", "java-flight-recorder", "jmc",
                                "mission-control", "profiling", "diagnostics", "monitoring"),
                        "Oracle",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_SPECIFIC, now
                ),

                new LearningResource(
                        "baeldung-class-loading",
                        "JVM Class Loading Mechanism — Baeldung",
                        "https://www.baeldung.com/java-classloaders",
                        "Explains the JVM class loading subsystem: bootstrap, extension, "
                                + "and application class loaders, parent delegation model, "
                                + "custom class loaders, class loading phases (loading, "
                                + "linking, initialization), and common ClassNotFoundException "
                                + "/ NoClassDefFoundError troubleshooting.",
                        ResourceType.ARTICLE,
                        List.of(ResourceCategory.JAVA),
                        List.of(ConceptArea.CLASS_LOADING,
                                ConceptArea.JVM_INTERNALS),
                        List.of("class-loading", "classloader", "bootstrap-classloader",
                                "delegation-model", "class-not-found", "linking"),
                        "Baeldung",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.JAVA_SPECIFIC, now
                ),

                // ─── Serialization / Deserialization ────────────────────────

                new LearningResource(
                        "java-serialization-spec",
                        "Java Object Serialization Specification — SE 21",
                        "https://docs.oracle.com/en/java/javase/21/docs/specs/serialization/index.html",
                        "The formal specification for Java object serialization. Covers "
                                + "the Serializable and Externalizable interfaces, "
                                + "ObjectOutputStream / ObjectInputStream, class descriptors, "
                                + "serialVersionUID, stream protocol, versioning of serializable "
                                + "objects, and security considerations. The authoritative "
                                + "reference for understanding Java's built-in serialization.",
                        ResourceType.DOCUMENTATION,
                        List.of(ResourceCategory.JAVA, ResourceCategory.SYSTEMS),
                        List.of(ConceptArea.SERIALIZATION,
                                ConceptArea.JVM_INTERNALS,
                                ConceptArea.WEB_SECURITY),
                        List.of("official", "serialization", "deserialization", "serializable",
                                "externalizable", "serialVersionUID", "object-stream",
                                "versioning"),
                        "Oracle",
                        DifficultyLevel.ADVANCED,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.JAVA_SPECIFIC, now
                ),

                new LearningResource(
                        "baeldung-java-serialization",
                        "Introduction to Java Serialization — Baeldung",
                        "https://www.baeldung.com/java-serialization",
                        "Practical guide to Java serialization: implementing Serializable, "
                                + "using ObjectOutputStream and ObjectInputStream, handling "
                                + "serialVersionUID, transient fields, custom serialization "
                                + "with writeObject/readObject, Externalizable interface, "
                                + "serialization proxies, and common pitfalls. Includes "
                                + "security considerations for deserialization.",
                        ResourceType.ARTICLE,
                        List.of(ResourceCategory.JAVA),
                        List.of(ConceptArea.SERIALIZATION, ConceptArea.JVM_INTERNALS),
                        List.of("serialization", "deserialization", "serializable",
                                "transient", "serialVersionUID", "writeObject", "readObject",
                                "externalizable"),
                        "Baeldung",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.JAVA_SPECIFIC, now
                )
        );
    }
}
