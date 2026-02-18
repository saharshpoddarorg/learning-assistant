# Java Blogs, Thought Leadership & Community Resources — Deep Reference

> **Purpose:** Self-contained reference for Java blogs, Inside.java content, and community resources.
> Use this instead of browsing the blog sites directly.

---

## 1. Inside.java — Oracle JDK Team Blog

**What it is:** News and views from members of the Java team at Oracle.
**URL:** `https://inside.java/`

### Content Types

| Format | Description | Best For |
|---|---|---|
| **Newscast** (`inside.java/newscast`) | Video series by Nicolai Parlog, bi-weekly | New JDK features, JEP deep-dives |
| **Podcast** (`inside.java/podcast`) | Audio interviews with JDK team | Deep technical discussions |
| **JEP Café** (`inside.java/jepcafe`) | Video series by José Paumard | Practical coding with new features |
| **Sip of Java** (`inside.java/sip`) | Short-form content | Quick tips, bite-sized learning |
| **Newsletter** (`inside.java/newsletter`) | Email roundup | Monthly summary of Java news |

### Key Topics Covered (Tag-Based)

| Tag | What You'll Find |
|---|---|
| **Amber** | Records, sealed classes, pattern matching, data-oriented programming |
| **Loom** | Virtual threads, structured concurrency, scoped values |
| **Panama** | Foreign Function & Memory API, vector API |
| **Valhalla** | Value types, primitive classes, specialized generics |
| **Leyden** | Ahead-of-time compilation, startup performance, CDS/AppCDS |
| **Babylon** | Code reflection, GPU programming from Java |
| **Performance** | GC improvements, JIT optimizations, benchmarks |
| **Security** | TLS updates, cryptographic algorithms, post-quantum |
| **Core Libraries** | java.lang, java.util, java.io improvements |
| **HotSpot** | JVM internals, compiler improvements |
| **Java Language** | Syntax changes, language specification updates |

### Notable Recent Articles (2026)

| Article | Author | Topic |
|---|---|---|
| Java's Plans for 2026 | Nicolai Parlog | Roadmap: Amber, Babylon, Leyden, Loom, Panama, Valhalla |
| LazyConstants in JDK 26 | Nicolai Parlog | New lazy initialization API |
| Carrier Classes; Beyond Records | Nicolai Parlog | Data-oriented programming evolution |
| Data-Oriented Programming: Beyond Records | Brian Goetz | Design notes on carrier classes |
| 1B Rows with the Memory API | José Paumard | Panama performance demo |
| Run Into the New Year with AOT Cache | Ana-Maria Mihalceanu | Project Leyden AOT optimizations |
| Quality Outreach: JDK 26 RC | David Delabassee | Testing upcoming releases |

### Inside.java Key Authors

| Author | Expertise |
|---|---|
| **Brian Goetz** | Java Language Architect — language design, specifications |
| **Nicolai Parlog** | Developer advocate — modern Java features, community |
| **José Paumard** | Developer advocate — Stream API, performance, JEP Café |
| **Mark Reinhold** | Chief Architect — platform strategy, release cadence |
| **Ana-Maria Mihalceanu** | Developer advocate — security, performance |
| **David Delabassee** | Community outreach, quality |

---

## 2. Key Java Blogs

### Baeldung (`baeldung.com`)

**What it is:** The largest Java tutorial site with 2000+ articles.
**Best for:** Practical, code-focused guides on specific topics.

**Key Article Categories:**

| Category | Coverage | Example Topics |
|---|---|---|
| **Core Java** | Language fundamentals | Strings, collections, I/O, generics, annotations |
| **Java Collections** | Deep collection guides | ArrayList internals, HashMap mechanics, ConcurrentHashMap |
| **Java Streams** | Stream API mastery | Collectors, groupingBy, flatMap patterns, parallel streams |
| **Java Concurrency** | Threading & async | ExecutorService, CompletableFuture, locks, atomics |
| **JUnit 5** | Testing framework | Assertions, parameterized tests, extensions |
| **Mockito** | Mocking framework | Argument matchers, verification, BDD style |
| **Spring** | Spring ecosystem | Spring Boot, Spring MVC, Spring Security, Spring Data |
| **JPA/Hibernate** | Persistence | Entity mapping, queries, performance, N+1 |
| **Design Patterns** | GoF patterns in Java | Builder, Factory, Strategy, Observer in practice |

**Why Baeldung Works:**
- Each article follows a consistent structure: Introduction → Problem → Solution → Code → Tests
- Always includes runnable code examples
- Updated for latest Java versions
- Peer-reviewed content

### Jenkov Tutorials (`jenkov.com`)

**What it is:** Deep technical articles by Jakob Jenkov, focused on fundamentals.
**Best for:** Understanding internals, concurrency, and low-level Java.

**Java Language Topics Covered:**
- Variables, Data Types, Math, Arrays, Strings
- Control flow: if, switch, for, while
- OOP: Classes, Fields, Methods, Constructors, Packages
- Advanced: Access Modifiers, Inheritance, Nested Classes, Interfaces, Abstract Classes
- Modern: Enums, Annotations, Lambda Expressions, Records, Modules

**Java API Tutorial Trails:**

| Trail | Topics | Depth |
|---|---|---|
| **Java Collections** | List, Set, Map, Queue, Deque, Iterator | Deep, 9 video companions |
| **Java Concurrency** | 40+ articles on threading | Most comprehensive free concurrency guide |
| **Java NIO** | Channels, Buffers, Selectors, FileChannel, Path | Deep non-blocking I/O coverage |
| **Java Networking** | Sockets, ServerSocket, UDP, HTTP | Network programming fundamentals |
| **Java IO** | Streams, Readers, Writers | Classic I/O patterns |
| **Java Reflection** | Class, Method, Field inspection | Runtime introspection |
| **Java Generics** | Type parameters, wildcards, erasure | Step-by-step with edge cases |
| **Java JDBC** | Connections, statements, transactions | Database access patterns |
| **Java JSON** | Parsing, generation | Working with JSON data |
| **Java Cryptography** | Encryption, hashing, signatures | Security programming |
| **Java Logging** | java.util.logging | Built-in logging framework |
| **Java Regular Expressions** | Pattern, Matcher | Text processing |
| **Java ZIP** | Compression, decompression | Archive handling |
| **JavaFX** | Desktop app development | GUI programming |

**Jenkov Concurrency Study Guide (Recommended Order):**

1. **Theory:** Benefits → Costs → Concurrency Models → Same-threading → Concurrency vs Parallelism
2. **Basics:** Creating Threads → Race Conditions → Thread Safety → Immutability → Java Memory Model → Happens-Before → Synchronized → Volatile → Cache Coherence → ThreadLocal → Thread Signaling
3. **Problems:** Deadlock → Deadlock Prevention → Starvation/Fairness → Nested Monitor Lockout → Slipped Conditions → False Sharing → Thread Congestion
4. **Solutions:** Locks → Read/Write Locks → Reentrance Lockout → Semaphores → Blocking Queues → Thread Pools → Compare and Swap
5. **Advanced:** Anatomy of a Synchronizer → Non-blocking Algorithms → Amdahl's Law

**Java Version Changes Covered (7–25):**
- Java 7: try-with-resources, multi-catch, ForkJoinPool, diamond operator
- Java 8: Lambdas, Streams, JavaFX bundled
- Java 9: Modules, JShell, Compact Strings
- Java 10: `var` type inference
- Java 11: HTTP Client, single-file execution
- Java 12–13: Switch expressions (preview)
- Java 14: Records (preview), NullPointerException improvements
- Java 15: Sealed classes (preview), Text blocks
- Java 16: Records (final), Pattern matching for instanceof (final)
- Java 17: Sealed classes (final), Pattern matching for switch (preview)
- Java 21: Virtual threads, Record patterns, Pattern matching for switch (final), Sequenced collections
- Java 22: Unnamed variables, launch multi-file programs, Foreign Function & Memory API (final)
- Java 23: Markdown doc comments, Primitive types in patterns (preview)
- Java 24: Stream Gatherers, Class-File API, Ahead-of-time class loading
- Java 25: Stable Values (preview), Scoped Values, Module imports, Compact source files, Compact object headers

### Vlad Mihalcea (`vladmihalcea.com`)

**Best for:** JPA, Hibernate, database performance
**Key Topics:** Entity mapping strategies, N+1 query detection, connection pooling, transaction management, DTO projections, batch processing

### Thorben Janssen (`thorben-janssen.com`)

**Best for:** JPA, Hibernate persistence best practices
**Key Topics:** Entity lifecycle, lazy loading, caching strategies, performance tuning, migration guides

### Marco Behler (`marcobehler.com`)

**Best for:** Modern Java ecosystem, practical guides
**Key Topics:** Build tools explained, Java versions explained, how Spring works internally, practical Maven/Gradle guides

### Nicolai Parlog (`nipafx.dev`)

**Best for:** Modern Java features, platform evolution
**Key Topics:** Pattern matching evolution, records design, sealed classes usage, module system migration, Java feature deep-dives

### Heinz Kabutz (`javaspecialists.eu`)

**Best for:** Concurrency, performance, Java puzzlers
**Key Topics:** Advanced threading, performance tricks, Java puzzlers newsletter, design patterns in concurrent Java

---

## 3. Community Aggregators

| Resource | URL | What It Provides |
|---|---|---|
| **DZone Java Zone** | `dzone.com/java` | Aggregated articles from practitioners |
| **InfoQ Java** | `infoq.com/java` | Conference talks, architecture, trends |
| **foojay.io** | `foojay.io` | Friends of OpenJDK — community tutorials, tooling |
| **Reddit r/java** | `reddit.com/r/java` | Community discussions, news |
| **dev.to #java** | `dev.to/t/java` | Community blog posts |

---

## 4. Java YouTube Channels

| Channel | Content | Best For |
|---|---|---|
| **Java** (`youtube.com/java`) | Official Oracle channel | Keynotes, conference talks, feature demos |
| **Inside Java Newscast** | Bi-weekly video series | Latest feature updates |
| **JEP Café** | Coding-focused videos | Hands-on new feature exploration |
| **Jakob Jenkov** | Tutorial videos | Collections (9 vids), Concurrency (26 vids), Language (10 vids) |
| **Devoxx** | Conference recordings | Expert talks from major Java conference |
| **Spring I/O** | Conference recordings | Spring ecosystem deep-dives |

---

## 5. Java Platform Evolution Summary

### Release Cadence
- **Since 2017:** 6-month time-based releases (March & September)
- **LTS versions:** Every 2 years (currently 25, 21, 17, 11, 8)
- **Preview system:** Features can be tested before becoming standard

### Active OpenJDK Projects

| Project | Focus | Key Deliverables |
|---|---|---|
| **Amber** | Language productivity | Records, sealed classes, pattern matching, string templates |
| **Loom** | Lightweight concurrency | Virtual threads, structured concurrency, scoped values |
| **Panama** | Foreign interop | Foreign Function & Memory API, Vector API |
| **Valhalla** | Advanced generics & value types | Value classes, primitive classes, specialized generics |
| **Leyden** | Startup/warmup | AOT compilation, CDS improvements, static images |
| **Babylon** | Code reflection | GPU programming, code transformation |

### Key Performance Improvements (8 → 17+)
- **Throughput:** Higher across all GC collectors
- **Pause times:** G1 in 17 ≈ 60% of pause time in 8; ZGC achieves sub-millisecond
- **Footprint:** G1 overhead from ~20% (Java 8) to ~10% (Java 17)
- **ZGC:** Constant pause times regardless of heap size (works with 128GB+ heaps)
- **Takeaway:** Upgrading JDK version = free performance improvement with no code changes

---

## 6. Key Books (Quick Reference of What They Cover)

| Book | Author | Key Content Summary |
|---|---|---|
| **Effective Java (3rd Ed.)** | Joshua Bloch | 90 items: creating objects, classes/interfaces, generics, lambdas/streams, methods, general programming, exceptions, concurrency, serialization |
| **Clean Code** | Robert C. Martin | Meaningful names, functions (small, one thing), comments (why not what), formatting, error handling, boundaries, unit tests, classes, systems |
| **Head First Design Patterns** | Freeman & Robson | Strategy, Observer, Decorator, Factory, Singleton, Command, Adapter, Facade, Template Method, Iterator, Composite, State, Proxy |
| **Java Concurrency in Practice** | Brian Goetz | Thread safety fundamentals, sharing objects, composing objects, task execution, cancellation/shutdown, thread pools, liveness/performance, testing concurrent programs |
| **Modern Java in Action** | Urma, Fusco, Mycroft | Lambda expressions, streams, data processing, collection enhancements, CompletableFuture, Optional, new Date/Time API, default methods, modules, functional programming techniques |
| **Refactoring (2nd Ed.)** | Martin Fowler | Extract Method, Inline, Move, Rename, Replace Temp with Query, Introduce Parameter Object, Encapsulate Collection, Replace Conditional with Polymorphism |

---

## 7. How to Use This Reference

**When learning a new concept:**
1. Check the **Oracle/dev.java tutorial** section first for the official explanation
2. Read the **Jenkov** article for deep technical understanding
3. Check **Baeldung** for practical code examples and patterns
4. Look at **Inside.java** for the latest updates and design rationale

**When debugging or problem-solving:**
1. Check **Baeldung** for specific how-to guides
2. Look at **Jenkov Concurrency** for threading issues
3. Check **Inside.java** for known issues and workarounds

**When keeping up with Java evolution:**
1. Follow **Inside.java Newscast** for bi-weekly updates
2. Check **Jenkov version summaries** for what's new in each release
3. Read **dev.java/evolution** for the big picture
