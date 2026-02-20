---
name: java-learning-resources
description: >
  Comprehensive Java & IDE learning resource library — organized into a modular hierarchy covering
  official Oracle/dev.java documentation, JDK API deep dives (concurrency, streams, gatherers, exceptions),
  community blogs, open-source project studies, and IDE guides (IntelliJ IDEA).
  Use when asked about where to learn Java concepts, when referencing official documentation, when recommending
  tutorials or reading material, when suggesting open-source projects to study, when building a learning plan,
  or when asking about IDE setup/features/shortcuts.
  Deep-reference files contain scraped, self-contained content designed to be used INSTEAD of visiting those websites.
---

# Java & IDE Learning Resources — Master Index

> **Runtime vault:** Many resources listed here are also available in the **Learning Resources MCP server vault** (`/resources` command). Use `/resources search java` for keyword search, or `/resources browse` for the full categorized library. The vault provides programmatic access via 7 MCP tools (search, browse, scrape, recommend, add, details). This SKILL provides deeper, scraped reference content for offline use.

## Repository Structure

```
java-learning-resources/
├── SKILL.md                                ← YOU ARE HERE (master index)
├── java/                                   ← All Java resources
│   ├── README.md                           ← Java sub-index
│   ├── official/                           ← Oracle / JDK / dev.java
│   │   ├── oracle-tutorials-guide.md       ← Language, OOP, collections, streams, records, pattern matching, modules
│   │   └── jdk-apis-reference.md           ← Concurrency, exceptions, Stream API, Gatherers, Collections, I/O, Date-Time
│   └── community/                          ← Blogs, OSS, third-party
│       ├── blogs-and-community-guide.md    ← Inside.java, Baeldung, Jenkov, YouTube, books, Java evolution
│       └── open-source-study-guide.md      ← Guava, JUnit 5, Spring Boot, Caffeine, Jackson, Resilience4j, Javalin, MapStruct
└── ide/                                    ← IDE & tooling resources
    ├── README.md                           ← IDE sub-index
    └── intellij-idea-guide.md              ← Setup, navigation, shortcuts, debugging, profiling, plugins
```

## Quick Navigation

| File | Path | What It Contains |
|---|---|---|
| **Java Sub-Index** | `java/README.md` | Directory structure & navigation for all Java resources |
| **Oracle Tutorials** | `java/official/oracle-tutorials-guide.md` | Complete dev.java tutorial index (§ 1-9) — language basics, OOP, generics, lambdas, records, pattern matching, collections (14 parts), streams (12 parts), I/O, date-time (13 parts), virtual threads, modules (12 parts), security. Legacy Oracle tutorials (§ 10) — complete lesson-level TOCs for all trails including Learning the Java Language, Essential Java Classes, Collections, Date-Time, Networking, JDBC, Internationalization, Security, JMX, Reflection, and more |
| **JDK APIs Reference** | `java/official/jdk-apis-reference.md` | Deep dives with code — Concurrency & Multithreading (threads, synchronization, executors, virtual threads, structured concurrency), Exception Handling (hierarchy, try-with-resources, custom exceptions), Stream API (operations, collectors, optionals), Gatherer API (Java 24+), Collections, I/O, Date-Time, HTTP Client, Modules, GC, JFR |
| **Blogs & Community** | `java/community/blogs-and-community-guide.md` | Inside.java content & authors, Baeldung/Jenkov coverage maps, concurrency study guide, YouTube channels, Java version evolution (7-25), OpenJDK projects, book summaries |
| **Open Source Study** | `java/community/open-source-study-guide.md` | OSS study methodology + deep dives: Guava, JUnit 5, Spring Boot, Caffeine, Jackson, Resilience4j, Javalin, MapStruct — with code examples, design patterns, starter paths |
| **IDE Sub-Index** | `ide/README.md` | Directory structure & navigation for IDE resources |
| **IntelliJ IDEA Guide** | `ide/intellij-idea-guide.md` | Setup, navigation, code completion, inspections, refactoring, debugging, profiling, Gradle/Maven, Git, testing, Spring, database tools, HTTP client, plugins, keyboard shortcuts |

---

## Official Documentation

### Oracle / OpenJDK

| Resource | URL | Best For |
|---|---|---|
| **Java SE Documentation** | `https://docs.oracle.com/en/java/javase/21/docs/api/` | API reference, class/method details |
| **Java Language Specification** | `https://docs.oracle.com/javase/specs/jls/se21/html/index.html` | Definitive language semantics |
| **JVM Specification** | `https://docs.oracle.com/javase/specs/jvms/se21/html/index.html` | JVM internals, bytecode |
| **Java Tutorials (Oracle)** | `https://docs.oracle.com/javase/tutorial/` | Official step-by-step learning path |
| **Dev.java** | `https://dev.java/learn/` | Modern official learning hub |
| **Inside.java** | `https://inside.java/` | JDK team blog, JEP updates, deep technical posts |
| **OpenJDK** | `https://openjdk.org/` | Source code, JEPs, project proposals |

### Key Oracle Tutorial Trails (Legacy — JDK 8)

> **Full detailed TOCs:** `java/official/oracle-tutorials-guide.md` § 10

| Trail | What It Covers | Level |
|---|---|---|
| Learning the Java Language | OOP concepts, language basics, classes/objects, annotations, interfaces/inheritance, numbers/strings, generics (comprehensive with wildcards/erasure), packages | Beginner |
| Essential Java Classes | Exceptions (full hierarchy), Basic I/O (streams + NIO.2), Concurrency (threads → Fork/Join), Platform Environment, Regular Expressions | Intermediate |
| Collections Framework | Interfaces (Collection/Set/List/Queue/Deque/Map/SortedSet/SortedMap), Aggregate Operations, Implementations, Algorithms, Custom Collections, Interoperability | Intermediate |
| Date-Time API | java.time overview, standard calendar classes, temporal adjusters/queries, period/duration, legacy conversion | Intermediate |
| Generics (advanced) | Full type parameter, wildcard, erasure, restrictions coverage | Intermediate-Advanced |
| Concurrency | Threads, executors, synchronization, liveness, fork/join, concurrent collections | Advanced |
| JDBC | Database connectivity, ResultSets, PreparedStatements, transactions, RowSets, advanced data types, stored procedures | Intermediate |
| Custom Networking | URLs, sockets (client+server), datagrams, network interfaces, cookies | Intermediate |
| Internationalization | Locale, ResourceBundle, formatting (numbers/dates/messages), text/Unicode, bidirectional text | Intermediate |
| Security | Policy files, signing code, digital signatures, custom permissions | Intermediate-Advanced |
| JMX | MBeans, MXBeans, notifications, remote management with JConsole | Advanced |
| Reflection | Classes, fields, methods, constructors, arrays, enum types | Intermediate-Advanced |
| JavaBeans | Visual components, properties, events, persistence | Intermediate |
| JNDI | Naming and Directory Interface — LDAP, DNS | Advanced |
| RMI | Remote Method Invocation — distributed Java | Advanced |
| Swing GUI | Components, layout, events, painting, drag-and-drop | Intermediate |

**Learning Paths** (from Oracle's recommended sequences):
| Path | Recommended Trail Order |
|---|---|
| New to Java | Getting Started → Learning the Java Language → Essential Java Classes → Collections → Date-Time → Deployment |
| Building on Foundation | Custom Networking, Generics, Full-Screen, Internationalization |
| Client Development | JavaBeans → Swing GUI → Deployment |
| Server Development | JDBC → RMI → JNDI |

---

## JDK APIs — Key Topics

> **Full reference:** `java/official/jdk-apis-reference.md`

| API / Topic | Key Content | Java Version |
|---|---|---|
| **Concurrency & Multithreading** | Threads, Runnable, synchronization, locks, executors, Fork/Join, CompletableFuture | Core |
| **Virtual Threads** | Lightweight threads, unmounting, carrier threads, Semaphore rate-limiting | 21+ |
| **Structured Concurrency** | StructuredTaskScope, scoped values | 21+ (preview) |
| **Exception Handling** | Throwable hierarchy, checked vs unchecked, try-with-resources, custom exceptions | Core |
| **Stream API** | Map/filter/reduce, intermediate/terminal ops, collectors, parallel streams | 8+ |
| **Gatherer API** | Custom intermediate operations, integrators, finishers, built-in gatherers | 24+ |
| **Collections Framework** | List, Set, Map, Queue/Deque, immutable collections, sequenced collections | Core / 9+ / 21+ |
| **I/O API** | Files, Path, BufferedReader, stream-based I/O | Core / 11+ |
| **Date-Time API** | LocalDate/Time, ZonedDateTime, Instant, Duration, Period, formatters | 8+ |
| **HTTP Client** | HttpClient, HttpRequest, HttpResponse, async | 11+ |
| **Foreign Function & Memory** | Native interop without JNI, MemorySegment, Arena | 22+ |
| **Modules** | module-info.java, requires, exports, services | 9+ |
| **Garbage Collection** | G1, ZGC, Shenandoah, tuning | Core |

---

## Tutorials & Interactive Learning

### Free Tutorials

| Resource | URL | Style | Best For |
|---|---|---|---|
| **Baeldung** | `https://www.baeldung.com/` | Article-based, practical | Specific Java topics, Spring, testing |
| **Jenkov Tutorials** | `https://jenkov.com/tutorials/java/index.html` | Deep technical articles | Concurrency, NIO, design patterns |
| **DigitalOcean Java** | `https://www.digitalocean.com/community/tags/java` | Article-based | Beginner-intermediate topics |
| **GeeksforGeeks Java** | `https://www.geeksforgeeks.org/java/` | Article + practice | Data structures, algorithms in Java |
| **Java Code Geeks** | `https://www.javacodegeeks.com/` | Articles, examples | Real-world Java patterns |
| **Programiz Java** | `https://www.programiz.com/java-programming` | Step-by-step | Absolute beginners |
| **W3Schools Java** | `https://www.w3schools.com/java/` | Interactive, try-it | Quick syntax reference |

### Interactive / Hands-On

| Resource | URL | Style | Best For |
|---|---|---|---|
| **Exercism (Java Track)** | `https://exercism.org/tracks/java` | Mentored exercises | Practice with feedback |
| **HackerRank Java** | `https://www.hackerrank.com/domains/java` | Coding challenges | Skill assessment, practice |
| **LeetCode** | `https://leetcode.com/` | Algorithmic problems | Interview prep, algorithms |
| **Codewars** | `https://www.codewars.com/` | Kata challenges | Problem-solving practice |
| **JetBrains Academy** | `https://hyperskill.org/tracks` | Project-based | Structured learning path |
| **Codecademy Java** | `https://www.codecademy.com/learn/learn-java` | Interactive | Guided beginner course |

---

## IDE Resources

> **Full reference:** `ide/intellij-idea-guide.md`

### IntelliJ IDEA — Quick Reference

| Category | Key Shortcuts (Win/Linux) | Description |
|---|---|---|
| **Search Everywhere** | `Shift Shift` | Find files, classes, symbols, actions |
| **Go to Class** | `Ctrl+N` | Navigate by class name (CamelCase abbreviations) |
| **Go to File** | `Ctrl+Shift+N` | Navigate by filename |
| **Find Action** | `Ctrl+Shift+A` | Find any IDE action |
| **Quick Fix** | `Alt+Enter` | Context-aware fix suggestions |
| **Refactor This** | `Ctrl+Alt+Shift+T` | All applicable refactorings |
| **Rename** | `Shift+F6` | Rename symbol across all usages |
| **Debug** | `Shift+F9` | Start debugging |
| **Evaluate Expression** | `Alt+F8` | Run code in debug context |
| **Generate** | `Alt+Insert` | Constructor, getters, equals, toString |

---

## Blogs & Thought Leadership

### Individual Blogs

| Blog | Author / Org | Known For |
|---|---|---|
| **Inside.java** | Oracle JDK team | JEP deep-dives, new features, performance |
| **Vlad Mihalcea** | Vlad Mihalcea | JPA, Hibernate, database performance |
| **Thorben Janssen** | Thorben Janssen | JPA, Hibernate, persistence best practices |
| **Marco Behler** | Marco Behler | Modern Java ecosystem, build tools, practical guides |
| **Nicolai Parlog** | Nicolai Parlog | Modern Java features, Java platform evolution |
| **Heinz Kabutz** | Heinz Kabutz | Java concurrency, performance, puzzlers |
| **DZone Java Zone** | Community | Aggregated articles from Java practitioners |
| **InfoQ Java** | Community | Conference talks, architecture, trends |
| **foojay.io** | Friends of OpenJDK | Community hub, tutorials, tooling |

### Key Books (Reference)

| Book | Author | Best For |
|---|---|---|
| Effective Java (3rd Ed.) | Joshua Bloch | Best practices, idioms, API design |
| Clean Code | Robert C. Martin | Code quality, naming, methods, formatting |
| Head First Design Patterns | Freeman & Robson | Design patterns with visual learning |
| Java Concurrency in Practice | Brian Goetz | Thread safety, concurrent programming |
| Refactoring (2nd Ed.) | Martin Fowler | Systematic code improvement techniques |
| Modern Java in Action | Urma, Fusco, Mycroft | Lambdas, streams, functional Java |

---

## Open-Source Projects to Study

### Learning-Friendly Projects (Well-Documented, Clean Code)

| Project | GitHub | Why Study It | Key Concepts |
|---|---|---|---|
| **Spring Boot** (samples) | `spring-projects/spring-boot` | Industry-standard patterns, clean architecture | DI, AOP, REST, configuration |
| **Guava** | `google/guava` | Utility library, excellent API design | Collections, caching, immutability |
| **JUnit 5** | `junit-team/junit5` | Test framework internals, extension model | Annotations, reflection, SPI |
| **Jackson** | `FasterXML/jackson-core` | Serialization, streaming API | Parsing, builder pattern, generics |
| **Caffeine** | `ben-manes/caffeine` | High-performance caching | Concurrency, eviction, weak refs |
| **Spark** (Java web) | `perwendel/spark` | Minimal web framework, small codebase | HTTP, routing, lambdas |
| **Javalin** | `javalin/javalin` | Lightweight web framework, modern Java | Functional style, context pattern |
| **MapStruct** | `mapstruct/mapstruct` | Code generation, annotation processing | APT, code generation, mapping |
| **Resilience4j** | `resilience4j/resilience4j` | Circuit breaker, retry patterns | Decorator pattern, functional, resilience |
| **Dagger** | `google/dagger` | Compile-time DI | DI, annotation processing, graphs |

---

## Topic-Specific Resource Map

Use this to find the best resource for a specific concept:

### Java Language Fundamentals
| Concept | Best Resource | Why |
|---|---|---|
| Syntax & basics | Oracle Tutorial: Learning the Java Language | Official, comprehensive |
| OOP concepts | Head First OOP / dev.java | Visual learning, analogies |
| Collections | Baeldung Collections Guide | Practical examples with each type |
| Generics | Jenkov Generics Tutorial | Deep, step-by-step, covers wildcards |
| Exception handling | `java/official/jdk-apis-reference.md` § Exception Handling | Full hierarchy, code examples, best practices |

### JDK APIs
| Concept | Best Resource | Why |
|---|---|---|
| Concurrency & threading | `java/official/jdk-apis-reference.md` § Concurrency | Complete coverage: threads → virtual threads |
| Stream API | `java/official/jdk-apis-reference.md` § Stream API | Operations tables, collectors, parallel streams |
| Gatherer API | `java/official/jdk-apis-reference.md` § Gatherer API | Full integrator/finisher/combiner patterns |
| Collections Framework | `java/official/jdk-apis-reference.md` § Collections | Hierarchy diagram, immutable collections |
| I/O & Files | `java/official/jdk-apis-reference.md` § I/O API | Modern Files utility methods |
| Date-Time | `java/official/jdk-apis-reference.md` § Date-Time | java.time class summary |

### Modern Java (11-21+)
| Concept | Best Resource | Why |
|---|---|---|
| Records | Inside.java / JEP 395 | Direct from the designers |
| Sealed classes | Inside.java / JEP 409 | Explains the motivation and design |
| Pattern matching | Nicolai Parlog's blog | Progressive examples |
| Text blocks | dev.java / JEP 378 | Concise with edge cases |
| Virtual threads | `java/official/jdk-apis-reference.md` § Virtual Threads | Complete code examples & best practices |
| Switch expressions | Baeldung | Practical migration guide |
| Gatherers | `java/official/jdk-apis-reference.md` § Gatherer API | Full API with built-in gatherers |

### Design & Architecture
| Concept | Best Resource | Why |
|---|---|---|
| SOLID principles | Clean Code + Baeldung | Theory + practical Java examples |
| Design patterns | Head First + Refactoring.Guru | Visual + interactive catalog |
| Clean architecture | Robert C. Martin blog | Original source, high-level |
| Refactoring | Refactoring.Guru + Fowler | Interactive catalog + canonical book |
| API design | Effective Java chapters 4-9 | Industry bible for Java API design |

### Testing
| Concept | Best Resource | Why |
|---|---|---|
| JUnit 5 | Baeldung JUnit 5 Guide | Comprehensive, practical |
| Mockito | Baeldung Mockito Series | Step-by-step with real examples |
| TDD | Kent Beck "TDD by Example" | Foundational methodology |
| Test patterns | xUnit Patterns (xunitpatterns.com) | Catalog of test patterns |

### Concurrency
| Concept | Best Resource | Why |
|---|---|---|
| Thread basics | `java/official/jdk-apis-reference.md` § Concurrency | Full thread lifecycle, sync, locks |
| Executors & pools | `java/official/jdk-apis-reference.md` § High-Level Concurrency | ExecutorService, Fork/Join, CompletableFuture |
| CompletableFuture | Baeldung + Tomasz Nurkiewicz blog | Practical async patterns |
| Virtual threads | `java/official/jdk-apis-reference.md` § Virtual Threads | Official deep-dive with code |
| Structured concurrency | `java/official/jdk-apis-reference.md` § Structured Concurrency | StructuredTaskScope, scoped values |

### IDE & Tooling
| Concept | Best Resource | Why |
|---|---|---|
| IntelliJ IDEA setup | `ide/intellij-idea-guide.md` § Installation | Installation, first config, JDK setup |
| Navigation shortcuts | `ide/intellij-idea-guide.md` § Navigation | Search Everywhere, Go to Class/File/Symbol |
| Debugging | `ide/intellij-idea-guide.md` § Debugging | Breakpoints, step operations, evaluate, stream debugger |
| Refactoring | `ide/intellij-idea-guide.md` § Refactoring | Extract, rename, inline, change signature |
| Build tools | `ide/intellij-idea-guide.md` § Build Tools | Gradle & Maven integration |
| Git integration | `ide/intellij-idea-guide.md` § Version Control | Commit, push, log, branches, interactive rebase |

---

## How to Use This Skill

When a learner asks about a concept:
1. **Explain the concept** using the learning-mentor approach
2. **Reference the best resource** from the topic-specific map above
3. **Read the relevant deep-reference file** for self-contained content with code examples
4. **Point to a relevant open-source project** where the concept is used in practice
5. **Suggest a hands-on exercise** the learner can try

When a learner asks "where should I learn X?":
1. **Match the topic** to the resource map
2. **Recommend 2-3 resources** — one official (Oracle/dev.java), one tutorial (Baeldung/Jenkov), one practical (open-source project)
3. **Check deep-reference files** for detailed content they can study immediately
4. **Suggest a study order** — which to read first and why

When a learner asks about IDE features or setup:
1. **Read `ide/intellij-idea-guide.md`** for comprehensive IntelliJ IDEA reference
2. **Point to the specific section** (navigation, debugging, refactoring, etc.)
3. **Include the keyboard shortcut** from the shortcuts reference table

---

## Deep Reference Files — Navigation Guide

Navigate by topic to find the right deep-reference file:

### Java Language & Tutorials → `java/official/oracle-tutorials-guide.md`
- dev.java/learn complete tutorial index with all sub-topics (§ 1-9)
- Language basics, OOP, classes/objects, inheritance, interfaces, generics, lambdas
- Records (Java 16+) with code, Pattern Matching (Java 16-21+) with code
- Collections Framework (14-part series), Stream API (12-part series)
- Date-Time API (13-part series), I/O, Modules (12-part series), Security
- Virtual Threads with code, Legacy Oracle tutorial comparison
- **Legacy Oracle Tutorials (§ 10)** — complete lesson-level TOCs for ALL trails:
  - Learning the Java Language (OOP concepts, language basics, classes/objects, annotations, interfaces/inheritance, numbers/strings, generics, packages)
  - Essential Java Classes (exceptions, basic I/O with NIO.2, concurrency, platform environment, regular expressions)
  - Collections (interfaces, aggregate operations, implementations, algorithms, custom implementations, interoperability)
  - Date-Time, Custom Networking (URLs, sockets, datagrams, network interfaces, cookies)
  - JDBC (connections, SQL processing, ResultSets, transactions, RowSets, advanced data types, stored procedures)
  - Internationalization (locale, ResourceBundle, formatting, Unicode text, bidirectional text)
  - Security (policy files, signing code, digital signatures, custom permissions)
  - JMX (MBeans, MXBeans, notifications, remote management)
  - Reflection (classes, fields, methods, constructors, arrays)
  - Summary entries for JavaBeans, JAXB, JAXP, JNDI, RMI, Swing GUI, 2D Graphics, and more
  - Oracle's official Learning Paths (New to Java, Building on Foundation, Client, Server)

### JDK APIs & Deep Dives → `java/official/jdk-apis-reference.md`
- **Concurrency:** Thread creation, lifecycle, synchronization, liveness, executors, Fork/Join, CompletableFuture, virtual threads, structured concurrency — all with code
- **Exceptions:** Throwable hierarchy diagram, checked vs unchecked, try/catch/finally, multi-catch, try-with-resources, custom exceptions, best practices table
- **Stream API:** Operations tables, collectors (groupingBy, partitioning, teeing), optionals, parallel streams
- **Gatherer API (Java 24+):** Integrators, state management, stream interruption, finishers, parallel gatherers, built-in gatherers (fold, scan, mapConcurrent, windowFixed, windowSliding)
- **Collections:** Hierarchy diagram, immutable collections, sequenced collections (Java 21+), lambda-enhanced Map operations
- **I/O, Date-Time, HTTP Client, Foreign Function, Modules, GC, JFR**

### Blogs & Community → `java/community/blogs-and-community-guide.md`
- Inside.java content types, key authors, recent articles
- Jenkov complete topic index + concurrency study guide (40+ articles)
- Baeldung category coverage, other blogs, community aggregators
- Java version evolution (7-25), OpenJDK projects, performance improvements
- YouTube channels, book content summaries

### Open Source Study → `java/community/open-source-study-guide.md`
- 9-step methodology for studying any OSS project
- Deep dives: Guava, JUnit 5, Spring Boot, Caffeine, Jackson, Resilience4j, Javalin, MapStruct
- Design patterns found across projects with concrete examples
- Starter paths by time available (1 hour → 1 day)

### IntelliJ IDEA → `ide/intellij-idea-guide.md`
- Setup, installation, migration from Eclipse/VS Code
- Navigation shortcuts, Search Everywhere, structure views
- Code completion (basic, smart, statement, postfix, live templates)
- Inspections (600+ built-in), refactoring shortcuts
- Debugging (breakpoint types, step operations, evaluate, stream debugger)
- Profiling (JFR, Async Profiler, flame graphs)
- Gradle/Maven integration, Git/VCS, testing (JUnit 5, coverage)
- Spring/Jakarta EE support, database tools, HTTP client
- Essential plugins, performance tuning, customization

