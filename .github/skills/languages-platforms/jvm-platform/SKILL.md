---
name: jvm-platform
description: >
  JVM platform internals, architecture, and ecosystem. Covers the complete JVM sub-hierarchy:
  JVM architecture (bytecode, JIT compilation, runtime data areas, HotSpot, safepoints),
  garbage collection (G1, ZGC, Shenandoah, GC tuning, GC logging, generational collection),
  class loading (bootstrap/extension/app loaders, delegation model, dynamic loading, linking),
  serialization/deserialization (Serializable, Externalizable, ObjectOutputStream, serialVersionUID),
  JVM memory (heap, stack, metaspace, runtime data areas, OOM troubleshooting),
  JVM performance (JMH benchmarking, JFR profiling, JMC, JVM flags, tuning),
  JVM languages (Kotlin, Scala, Groovy, Clojure — polyglot programming on the JVM),
  and GraalVM (native image, AOT compilation, polyglot runtime).
  Use when asked about JVM internals, garbage collection, class loaders, Java serialization,
  JVM tuning, JVM flags, JVM memory structure, JVM languages, GraalVM, bytecode, JIT,
  HotSpot, or any JVM platform topic beyond the Java language itself.
---

# JVM Platform — Skill Reference

> **Domain:** Core CS > JVM Platform
> **Sub-hierarchy:** JVM_INTERNALS → GARBAGE_COLLECTION, CLASS_LOADING, SERIALIZATION, JVM_LANGUAGES
> **Related skills:** `java-build`, `java-debugging`, `java-learning-resources`

---

## JVM Sub-Hierarchy Map

```text
JVM_INTERNALS (umbrella — architecture, bytecode, JIT, runtime)
├── GARBAGE_COLLECTION — GC algorithms, tuning, diagnostics
│     G1, ZGC, Shenandoah, Serial, Parallel, Epsilon
│     GC roots, generations, pause-time goals, GC logging
├── CLASS_LOADING — class loaders, delegation, linking
│     Bootstrap / Extension / Application class loaders
│     Loading → Linking → Initialization phases
├── SERIALIZATION — object serialization & deserialization
│     Serializable, Externalizable, serialVersionUID
│     ObjectOutputStream / ObjectInputStream, transient
├── JVM_LANGUAGES — polyglot JVM programming
│     Kotlin (coroutines, null safety, Android)
│     Scala (FP + OOP, Akka, Spark)
│     Groovy (dynamic, Gradle DSL, Spock)
│     Clojure (Lisp, immutability, REPL)
└── (cross-cutting)
      MEMORY_MANAGEMENT — heap, stack, metaspace, OOM
      CONCURRENCY — threads, locks, virtual threads
```

---

## Quick Reference by Tier

### Newbie — "I need to understand the basics"

#### What Is the JVM?

```text
Source Code (.java)
    ↓  javac (compiler)
Bytecode (.class)
    ↓  JVM (runtime)
Machine Code (executed by CPU)
```

The JVM is a virtual machine that runs Java bytecode. It provides:

- **Platform independence** — write once, run anywhere
- **Memory management** — automatic garbage collection
- **Security** — bytecode verification, sandboxing
- **Performance** — JIT compilation to native code

#### JVM Memory Areas

| Area | Stores | Shared? |
|---|---|---|
| **Heap** | Objects, arrays | Yes (all threads) |
| **Stack** | Local variables, method frames | No (per thread) |
| **Metaspace** | Class metadata, method info | Yes |
| **PC Register** | Current instruction address | No (per thread) |
| **Native Stack** | Native method frames | No (per thread) |

#### Basic GC Concepts

```text
Young Generation          Old Generation
┌─────────┬─────┬─────┐  ┌──────────────┐
│  Eden   │ S0  │ S1  │  │   Tenured    │
│ (new)   │     │     │  │  (long-lived) │
└─────────┴─────┴─────┘  └──────────────┘
     Minor GC ──────────→  Major GC
```

- **Eden:** where new objects are allocated
- **Survivor (S0/S1):** objects that survived minor GC
- **Old/Tenured:** long-lived objects promoted from young gen

#### Key JVM Flags

```sh
# Check Java version
java --version

# Set heap size
java -Xms256m -Xmx1g -cp src Main

# Show GC activity
java -verbose:gc -cp src Main

# Show NullPointerException details (Java 14+)
java -XX:+ShowCodeDetailsInExceptionMessages -cp src Main
```

---

### Amateur — "I need to tune and understand deeper"

#### Garbage Collectors — When to Use Each

| Collector | Flag | Best For | Pause Goal |
|---|---|---|---|
| **G1** (default) | `-XX:+UseG1GC` | General-purpose, balanced | < 200ms |
| **ZGC** | `-XX:+UseZGC` | Ultra-low latency | < 1ms |
| **Shenandoah** | `-XX:+UseShenandoahGC` | Low latency (Red Hat) | < 10ms |
| **Parallel** | `-XX:+UseParallelGC` | Maximum throughput | Higher pauses OK |
| **Serial** | `-XX:+UseSerialGC` | Small heaps, single CPU | N/A |
| **Epsilon** | `-XX:+UseEpsilonGC` | No GC (testing only) | N/A |

#### GC Tuning Essentials

```sh
# G1 with 200ms pause target
java -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Xmx4g -cp src Main

# ZGC for low latency
java -XX:+UseZGC -Xmx4g -cp src Main

# Enable GC logging (JDK 9+)
java -Xlog:gc*:file=gc.log:time,uptime,level,tags -cp src Main

# Print GC details to console
java -Xlog:gc*=info -cp src Main
```

#### Class Loading Deep-Dive

```text
Bootstrap ClassLoader (rt.jar, java.base)
    ↓ delegates to
Extension/Platform ClassLoader (jdk.* modules)
    ↓ delegates to
Application ClassLoader (classpath)
    ↓ delegates to
Custom ClassLoaders (plugins, OSGi, app servers)
```

**Parent delegation model:** child asks parent first → prevents duplicates.

```java
// Check which classloader loaded a class
System.out.println(String.class.getClassLoader());      // null (bootstrap)
System.out.println(Main.class.getClassLoader());         // AppClassLoader
```

#### Serialization Essentials

```java
// Basic serialization
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private transient String password;  // excluded from serialization
}

// Write object
try (var oos = new ObjectOutputStream(new FileOutputStream("emp.ser"))) {
    oos.writeObject(employee);
}

// Read object
try (var ois = new ObjectInputStream(new FileInputStream("emp.ser"))) {
    Employee emp = (Employee) ois.readObject();
}
```

**Security warning:** Never deserialize untrusted data — deserialization attacks are
in the OWASP Top 10. Prefer JSON/protobuf for external data exchange.

#### JVM Languages Overview

| Language | Paradigm | Killer Feature | Used For |
|---|---|---|---|
| **Kotlin** | OOP + FP | Null safety, coroutines | Android, server-side |
| **Scala** | FP + OOP | Type system, pattern matching | Data engineering (Spark) |
| **Groovy** | Dynamic + static | Concise syntax | Gradle, Jenkins, Spock |
| **Clojure** | Functional (Lisp) | Immutability, STM | Data-driven apps, REPL |

---

### Pro — "I need deep internals and production tuning"

#### JIT Compilation Tiers

```text
Interpreter (cold code)
    ↓ method invocation count hits threshold
C1 Compiler (fast compilation, basic optimizations)
    ↓ profiling data collected
C2 Compiler (aggressive optimizations, peak performance)
```

```sh
# Print JIT compilation events
java -XX:+PrintCompilation -cp src Main

# Disable tiered compilation (use C2 only)
java -XX:-TieredCompilation -cp src Main

# Print assembly for specific method (requires hsdis)
java -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly \
     -XX:CompileCommand=dontinline,*Main.hotMethod -cp src Main
```

#### JVM Anatomy Quarks (Key Topics)

| Quark | Topic | Key Insight |
|---|---|---|
| #1 | Lock coarsening | JVM merges adjacent synchronized blocks |
| #4 | TLAB allocation | Thread-local allocation buffers avoid contention |
| #7 | Object alignment | Objects aligned to 8-byte boundaries (+ padding) |
| #10 | String interning | String.intern() across GCs and generations |
| #22 | Safepoints | Where threads can be stopped for GC |
| #24 | Object alignment (revisited) | Compressed oops, object headers |
| #25 | Implicit null checks | OS signals for null pointer detection |

#### JMH Benchmarking

```java
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(2)
public class MyBenchmark {

    @Benchmark
    public void measure(Blackhole bh) {
        bh.consume(doWork());
    }
}
```

```sh
# Run JMH benchmarks
java -jar benchmarks.jar -f 2 -wi 5 -i 5
```

#### JFR Profiling

```sh
# Start recording with JFR
java -XX:StartFlightRecording=duration=60s,filename=recording.jfr \
     -cp src Main

# Continuous recording (ring buffer)
java -XX:StartFlightRecording=disk=true,maxsize=500m,maxage=1h \
     -cp src Main

# Dump from running JVM
jcmd <pid> JFR.start duration=30s filename=dump.jfr
```

#### GraalVM Native Image

```sh
# Build native image (requires GraalVM)
native-image -cp src Main

# Spring Boot native
./gradlew nativeCompile

# Key flags
native-image --no-fallback --static -H:+ReportExceptionStackTraces
```

#### Memory Troubleshooting

```sh
# Heap dump on OOM
java -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp -cp src Main

# Manual heap dump
jmap -dump:format=b,file=heap.hprof <pid>

# JVM memory summary
jcmd <pid> VM.native_memory summary

# Thread dump
jstack <pid>
```

---

## Curated Resources

| Resource | Type | Difficulty | Key Topics |
|---|---|---|---|
| [JVMS SE 21](https://docs.oracle.com/javase/specs/jvms/se21/html/index.html) | Spec | Expert | Bytecode, class file, runtime |
| [JVM Anatomy Quarks](https://shipilev.net/jvm/anatomy-quarks/) | Articles | Advanced | TLABs, safepoints, locks |
| [GC Tuning Guide](https://docs.oracle.com/en/java/javase/21/gctuning/) | Docs | Intermediate | G1, ZGC, tuning |
| [Baeldung JVM GC](https://www.baeldung.com/jvm-garbage-collectors) | Article | Intermediate | Collector comparison |
| [JMH](https://github.com/openjdk/jmh) | Repo | Advanced | Benchmarking |
| [GraalVM](https://www.graalvm.org/) | Docs | Intermediate | Native image, polyglot |
| [Kotlin Docs](https://kotlinlang.org/docs/home.html) | Docs | Beginner | Coroutines, null safety |
| [Scala Docs](https://docs.scala-lang.org/) | Docs | Intermediate | FP, type system |
| [Groovy Docs](https://groovy-lang.org/documentation.html) | Docs | Beginner | Gradle DSL, scripting |
| [Clojure Guide](https://clojure.org/guides/getting_started) | Docs | Intermediate | Lisp, immutability |
| [Serialization Spec](https://docs.oracle.com/en/java/javase/21/docs/specs/serialization/index.html) | Spec | Advanced | Object streams, versioning |
| [Awesome JVM](https://github.com/deephacks/awesome-jvm) | Repo | Intermediate | Curated JVM resources |
| [Mechanical Sympathy](https://mechanical-sympathy.blogspot.com/) | Blog | Expert | Hardware-aware JVM |
| [JFR Guide](https://docs.oracle.com/en/java/javase/21/jfapi/) | Docs | Intermediate | Profiling, diagnostics |
| [Baeldung Class Loading](https://www.baeldung.com/java-classloaders) | Article | Intermediate | ClassLoaders, delegation |

---

## Learning Path

### Phase 1 — JVM Basics (1-2 weeks)

1. Understand JVM architecture: bytecode, runtime data areas
2. Learn memory model: heap vs stack vs metaspace
3. Run with basic JVM flags (`-Xms`, `-Xmx`, `-verbose:gc`)

### Phase 2 — GC & Class Loading (2-3 weeks)

1. Study generational GC model (young/old generation)
2. Compare G1, ZGC, Shenandoah collectors
3. Understand class loading delegation model
4. Learn GC logging and basic tuning

### Phase 3 — Performance & Profiling (2-4 weeks)

1. Write JMH benchmarks for micro-benchmarking
2. Use JFR and JMC for production profiling
3. Analyze heap dumps with VisualVM or Eclipse MAT
4. Understand JIT compilation tiers

### Phase 4 — JVM Languages & GraalVM (ongoing)

1. Try Kotlin for server-side development
2. Explore Scala for functional programming on JVM
3. Experiment with GraalVM native image
4. Learn serialization security considerations
