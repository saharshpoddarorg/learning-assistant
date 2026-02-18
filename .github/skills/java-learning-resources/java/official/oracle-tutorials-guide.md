# Oracle & dev.java Tutorials — Comprehensive Reference Guide

> **Purpose:** Self-contained reference so you never need to visit the websites.
> Use this instead of browsing Oracle/dev.java tutorials.

---

## 1. dev.java Tutorial Index (Modern Official Hub)

The **dev.java/learn** site is the modern replacement for the legacy Oracle tutorials.
It covers Java 21+ features and is actively maintained by the Oracle JDK team.

### 1.1 Getting Started

| Tutorial | Sub-Topics | Key Takeaways |
|---|---|---|
| **Getting Started with Java** | Elements of a Java App, Compilation Cycle, Creating First Program, JDK Setup, Running | Java source → bytecode → JVM execution; `java MyApp.java` runs single-file programs since Java 11; since Java 22 multi-file source programs are supported |
| **Launching Simple Source-Code Programs** | Direct execution, multi-file | Use `java` launcher directly on `.java` files |
| **JShell** | REPL, interactive evaluation | `jshell` for trying out declarations, statements, expressions interactively |
| **Building in VS Code** | Oracle Java Extension, Maven/Gradle | Oracle Java Platform extension for VS Code development |
| **Building in IntelliJ IDEA** | Code, run, test, debug, document | Full IDE workflow walkthrough |
| **Building in Eclipse IDE** | Install, setup, develop | Eclipse IDE for Java development |

### 1.2 Java Language Basics

| Tutorial | Sub-Topics | Key Concepts |
|---|---|---|
| **Creating Variables and Naming Them** | Naming rules, conventions | `lowerCamelCase` for variables, `UpperCamelCase` for classes |
| **Primitive Type Variables** | 8 primitive types | `byte`, `short`, `int`, `long`, `float`, `double`, `char`, `boolean` |
| **Arrays** | Fixed-length containers | Declaration, initialization, multi-dimensional arrays |
| **Using the Var Type Identifier** | Local variable type inference | `var list = new ArrayList<String>();` — Java 10+ |
| **Using Operators** | Arithmetic, relational, logical | Operator precedence, short-circuit evaluation |
| **Summary of Operators** | Complete reference | All Java operators in one place |
| **Expressions, Statements and Blocks** | Grouping code | Expression vs statement distinction |
| **Control Flow Statements** | Decision, looping, branching | `if-else`, `for`, `while`, `do-while`, `break`, `continue` |
| **Branching with Switch Statements** | Traditional switch | Fall-through behavior, break required |
| **Branching with Switch Expressions** | Modern switch (Java 14+) | Arrow syntax `->`, can return values, no fall-through |

### 1.3 OOP Concepts

| Tutorial | Sub-Topics Covered |
|---|---|
| **Objects, Classes, Interfaces, Packages, Inheritance** | What is an Object (state + behavior), What is a Class (blueprint), What is Inheritance (extends), What is an Interface (contract), What is a Package (namespace) |

**Key OOP Concepts from the Tutorial:**

- **Object** = state (fields) + behavior (methods); data encapsulation hides internal state
- **Class** = blueprint for objects; contains fields, methods, constructors
- **Benefits of OOP:** Modularity, Information-hiding, Code re-use, Pluggability
- **Inheritance:** `class MountainBike extends Bicycle {}` — single inheritance in Java
- **Interface:** group of related methods forming a contract; `class ACMEBicycle implements Bicycle {}`
- **Package:** namespace organizing related classes; Java API is a huge set of packages

### 1.4 Classes and Objects (Deep Dive)

| Tutorial | What It Covers |
|---|---|
| **Creating Classes** | Class definition syntax, fields, methods |
| **Defining Methods** | Method signatures, return types, parameters |
| **Providing Constructors** | Default, parameterized, constructor chaining |
| **Calling Methods and Constructors** | Passing information, varargs |
| **Creating and Using Objects** | `new` keyword, object lifecycle |
| **More on Classes** | Dot operator, `this` keyword, access control (`public`, `private`, `protected`, default) |
| **Nested Classes** | Static nested, inner, local, anonymous classes |
| **Enums** | Enum types, methods, constructors in enums |
| **Design Best Practices** | When to use nested classes, local classes, anonymous classes, lambdas |

### 1.5 Inheritance

| Tutorial | What It Covers |
|---|---|
| **Inheritance** | `extends` keyword, superclass/subclass relationships |
| **Overriding and Hiding Methods** | `@Override`, method resolution rules |
| **Polymorphism** | Runtime method dispatch, dynamic binding |
| **Object as a Superclass** | `toString()`, `equals()`, `hashCode()`, `clone()`, `getClass()`, `finalize()` |
| **Abstract Methods and Classes** | `abstract` keyword, partial implementation sharing |

### 1.6 Interfaces

| Tutorial | What It Covers |
|---|---|
| **Defining Interfaces** | Interface contracts, abstract methods, default methods, static methods |
| **Implementing an Interface** | `implements` keyword, multiple interface implementation |
| **Using an Interface as a Type** | Interface variables, polymorphism through interfaces |

### 1.7 Generics

| Tutorial | What It Covers |
|---|---|
| **Introducing Generics** | Type parameters `<T>`, generic classes, generic methods |
| **Type Inference** | Diamond operator `<>`, method type inference |
| **Wildcards** | `?`, `? extends T` (upper bound), `? super T` (lower bound), PECS principle |
| **Type Erasure** | How generics work at runtime, bridge methods, reification |
| **Restrictions on Generics** | Cannot instantiate `T`, no primitive type parameters, no `instanceof` with generics |

### 1.8 Lambda Expressions

| Tutorial | What It Covers |
|---|---|
| **Writing Your First Lambda** | Syntax `(params) -> expression`, finding the lambda type |
| **Using Lambdas in Your Application** | `Predicate<T>`, `Function<T,R>`, `Consumer<T>`, `Supplier<T>`, `UnaryOperator<T>`, `BinaryOperator<T>` |
| **Method References** | `ClassName::methodName`, `object::methodName`, constructor references `ClassName::new` |
| **Combining Lambda Expressions** | Default methods `and()`, `or()`, `negate()`, `compose()`, `andThen()` |
| **Writing and Combining Comparators** | `Comparator.comparing()`, `thenComparing()`, `reversed()` |

### 1.9 Records (Java 16+)

**Key Concepts from Scraped Content:**

```java
// Declares: private final fields, canonical constructor, accessors, toString, equals, hashCode
public record Point(int x, int y) {}

// Compact constructor for validation
public record Range(int start, int end) {
    public Range {  // No parameter list needed
        if (end <= start) throw new IllegalArgumentException("...");
    }
}

// Custom constructor must call canonical
public record State(String name, String capitalCity, List<String> cities) {
    public State {
        cities = List.copyOf(cities);  // defensive copy
    }
    public State(String name, String capitalCity) {
        this(name, capitalCity, List.of());
    }
}
```

**Record Rules:**
- Class is `final`, extends `java.lang.Record`
- Cannot declare instance fields beyond components
- Cannot define field initializers or instance initializers
- Can implement interfaces, have static fields/methods
- Can be serialized — deserialization always uses canonical constructor
- Can be local records (declared inside methods) for improving readability

### 1.10 Pattern Matching

**Pattern Matching for instanceof (Java 16+):**
```java
// Old way
if (o instanceof String) {
    String s = (String) o;
    System.out.println(s.length());
}

// New way — type pattern with pattern variable
if (o instanceof String s) {
    System.out.println(s.length());
}

// Can use in boolean expressions after &&
if (o instanceof String s && !s.isEmpty()) { ... }

// Cleaner equals() method
public boolean equals(Object o) {
    return o instanceof Point point && x == point.x && y == point.y;
}
```

**Pattern Matching for switch (Java 21+):**
```java
String formatted = switch(o) {
    case Integer i -> String.format("int %d", i);
    case Long l    -> String.format("long %d", l);
    case Double d  -> String.format("double %f", d);
    default        -> String.format("Object %s", o.toString());
};

// Guarded patterns with 'when'
String result = switch(o) {
    case String s when !s.isEmpty() -> "Non-empty: " + s;
    default -> "Other: " + o.toString();
};
```

**Record Patterns (Java 21+):**
```java
// Deconstruct records in instanceof
if (o instanceof Point(int x, int y)) { /* use x, y */ }

// Deconstruct in switch
switch (o) {
    case Box(String s)  -> println("String: " + s);
    case Box(Integer i) -> println("Integer: " + i);
    default -> println("Other");
}

// Nested record patterns
if (o instanceof Circle(Point(var x, var y), var radius)) { ... }
```

### 1.11 Exceptions

| Tutorial | What It Covers |
|---|---|
| **What Is an Exception?** | Exception hierarchy: `Throwable` → `Error` + `Exception` → `RuntimeException` |
| **Catching and Handling** | `try`, `catch`, `finally`, try-with-resources (Java 7+) |
| **Throwing Exceptions** | `throw`, `throws`, creating custom exceptions |
| **Unchecked Exceptions Controversy** | When to use checked vs unchecked exceptions |

---

## 2. Collections Framework

Complete tutorial series for storing and processing data:

| # | Tutorial | Key Concepts |
|---|---|---|
| 1 | **Intro: Why Collections over Arrays** | Resizable, type-safe, rich API |
| 2 | **Collection Hierarchy** | `Iterable` → `Collection` → `List`, `Set`, `Queue` |
| 3 | **Storing Elements** | `add()`, `remove()`, `contains()`, `size()` |
| 4 | **Iterating** | Enhanced for-loop, `Iterator`, `forEach()` |
| 5 | **Lists** | `ArrayList`, `LinkedList` — ordered, indexed, duplicates allowed |
| 6 | **Sets** | `HashSet`, `TreeSet`, `LinkedHashSet` — no duplicates |
| 7 | **Immutable Collections** | `List.of()`, `Set.of()`, `Map.of()`, `List.copyOf()` (Java 9+) |
| 8 | **Stacks and Queues** | `Deque`, `ArrayDeque`, `PriorityQueue` |
| 9 | **Maps** | `HashMap`, `TreeMap`, `LinkedHashMap` — key-value pairs |
| 10 | **Managing Map Content** | `put()`, `get()`, `getOrDefault()`, `putIfAbsent()` |
| 11 | **Map with Lambdas** | `compute()`, `computeIfAbsent()`, `computeIfPresent()`, `merge()` |
| 12 | **SortedMap & NavigableMap** | Key ordering, `headMap()`, `tailMap()`, `subMap()` |
| 13 | **Choosing Keys** | Immutable keys, `equals()`/`hashCode()` contract |
| 14 | **ArrayList vs LinkedList** | ArrayList wins in almost all cases; LinkedList only for frequent add/remove at both ends |

---

## 3. Stream API

Complete tutorial series for functional data processing:

| # | Tutorial | Key Concepts |
|---|---|---|
| 1 | **Map/Filter/Reduce** | `stream().filter().map().reduce()` pipeline |
| 2 | **Intermediate Operations** | `filter()`, `map()`, `flatMap()`, `distinct()`, `sorted()`, `peek()`, `limit()`, `skip()` |
| 3 | **Creating Streams** | `Collection.stream()`, `Stream.of()`, `Stream.generate()`, `Stream.iterate()`, `Files.lines()` |
| 4 | **Reducing** | `reduce(identity, accumulator)`, `reduce(accumulator)` |
| 5 | **Terminal Operations** | `forEach()`, `count()`, `min()`, `max()`, `findFirst()`, `findAny()`, `anyMatch()`, `allMatch()`, `noneMatch()` |
| 6 | **Stream Characteristics** | ORDERED, DISTINCT, SORTED, SIZED, NONNULL, IMMUTABLE, CONCURRENT, SUBSIZED |
| 7 | **Using Collectors** | `toList()`, `toSet()`, `toMap()`, `joining()`, `groupingBy()`, `partitioningBy()` |
| 8 | **Custom Collectors** | `Collectors` factory methods, downstream collectors |
| 9 | **Collector Interface** | `supplier()`, `accumulator()`, `combiner()`, `finisher()`, `characteristics()` |
| 10 | **Optionals** | `Optional.of()`, `ofNullable()`, `empty()`, `map()`, `flatMap()`, `orElse()`, `orElseGet()`, `orElseThrow()` |
| 11 | **Parallel Streams** | `parallelStream()`, thread safety concerns, when to use |
| 12 | **Gatherer API** (Java 24+) | Custom intermediate operations |

---

## 4. I/O API

| # | Tutorial | Key Concepts |
|---|---|---|
| 1 | **Main Concepts** | Path, File, streams of characters vs bytes |
| 2 | **File System Basics** | `Path`, `Files`, directory operations, file metadata |
| 3 | **File Operations** | Reading/writing text and binary files, `BufferedReader`, `BufferedWriter` |
| 4 | **Putting It All Together** | Complex I/O use cases combining all structures |

---

## 5. Date Time API (java.time)

| # | Tutorial | Key Concepts |
|---|---|---|
| 1 | **Overview** | Core concepts: human time vs machine time |
| 2 | **Standard Calendar** | Temporal-based classes overview |
| 3 | **DayOfWeek & Month** | `DayOfWeek`, `Month` enums |
| 4 | **Date** | `LocalDate`, `YearMonth`, `MonthDay`, `Year` |
| 5 | **Date and Time** | `LocalTime`, `LocalDateTime` |
| 6 | **Time Zone and Offset** | `ZonedDateTime`, `OffsetDateTime`, `ZoneId` |
| 7 | **Instant** | `Instant` — machine timestamp on timeline |
| 8 | **Parsing and Formatting** | `DateTimeFormatter`, custom patterns |
| 9 | **Temporal Package** | Temporal adjusters and queries |
| 10 | **Period and Duration** | `Period` (date-based), `Duration` (time-based), `ChronoUnit.between()` |
| 11 | **Clock** | Alternative clock for testing |
| 12 | **Non-ISO Conversion** | Japanese, Thai Buddhist calendars |
| 13 | **Legacy Code** | Converting from `java.util.Date`/`Calendar` |

---

## 6. Virtual Threads (Java 21+)

**Key Concepts from Scraped Content:**

**Why Virtual Threads:**
- Platform threads cost ~few thousand CPU instructions to start, ~few MB of memory
- Server apps have many concurrent requests that mostly block (waiting for DB, network)
- Virtual threads are lightweight — many run on one platform thread
- When a virtual thread blocks, it's unmounted and the carrier thread runs another
- Use for I/O-bound tasks, NOT CPU-bound (use parallel streams/fork-join for CPU)

**Creating Virtual Threads:**
```java
// Preferred: ExecutorService
ExecutorService service = Executors.newVirtualThreadPerTaskExecutor();
service.submit(() -> {
    long id = Thread.currentThread().threadId();
    LockSupport.parkNanos(1_000_000_000);
    System.out.println(id);
});
service.close();

// Thread.Builder for factories
Thread.Builder builder = Thread.ofVirtual().name("request-", 1);
ThreadFactory factory = builder.factory();

// Quick one-off
Thread t = Thread.startVirtualThread(myRunnable);
```

**Best Practices:**
- Don't pool virtual threads — use other mechanisms for rate limiting (e.g., `Semaphore`)
- Check for pinning: `synchronized` blocks pin virtual threads (use `ReentrantLock` instead)
- Minimize thread-local variables in virtual threads
- Use `-Djdk.tracePinnedThreads=short` to detect pinning issues

---

## 7. Modules (Java 9+)

| # | Tutorial | Key Concepts |
|---|---|---|
| 1 | **Introduction** | `module-info.java`, `requires`, `exports` |
| 2 | **Open Modules** | `open module`, reflective access |
| 3 | **Optional Dependencies** | `requires static` |
| 4 | **Implied Readability** | `requires transitive` |
| 5 | **Qualified Exports** | `exports ... to ...` |
| 6 | **Services** | `uses`, `provides ... with ...`, `ServiceLoader` |
| 7 | **Unnamed Module** | Class path JARs in unnamed module |
| 8 | **Automatic Modules** | Plain JARs on module path |
| 9 | **Building on CLI** | `javac`, `jar`, `java` commands |
| 10 | **Strong Encapsulation** | JDK internal APIs encapsulated |
| 11 | **--add-exports/--add-opens** | Circumventing encapsulation |
| 12 | **--add-modules/--add-reads** | Extending module graph |

---

## 8. Security

| # | Tutorial | What It Covers |
|---|---|---|
| 1 | **Encryption/Decryption** | JCA, basic encryption mechanisms |
| 2 | **Digital Signatures & Certificates** | Verifying signatures, inspecting certificates |
| 3 | **Monitoring with JDK Tools** | keytool, JFR, JDK Mission Control |
| 4 | **Safeguarding Applications** | JDK tools for app security |

---

## 9. Other dev.java Tutorials

| Tutorial | What It Covers |
|---|---|
| **Numbers and Strings** | Number classes, String methods, StringBuilder |
| **Annotations** | Built-in annotations, custom annotations, retention policies |
| **Packages** | Package declaration, import, classpath |
| **Regex** | `Pattern`, `Matcher`, character classes, quantifiers |
| **Reflection** | `Class`, `Method`, `Field`, inspecting and invoking at runtime |
| **Method Handles** | Modern alternative to reflection, `MethodHandle`, `VarHandle` |
| **Foreign Function & Memory API** | Interop with native code without JNI |
| **Refactoring to Functional Style** | Imperative → functional migration |
| **Modern I/O Tasks** | Reading text/JSON/images from web in modern Java |
| **JDK Flight Recorder** | Monitoring, profiling, testing |
| **Garbage Collection** | GC types, tuning, ZGC, G1, Shenandoah |

---

## 10. Legacy Oracle Tutorials (docs.oracle.com/javase/tutorial)

> **Note:** Written for JDK 8. Still the most comprehensive structured tutorial for foundational Java topics.
> Use dev.java (§ 1-9 above) for modern features (records, pattern matching, virtual threads, modules).

### When to Use Legacy vs dev.java

| Use Legacy Tutorials When | Use dev.java When |
|---|---|
| Need step-by-step fundamentals (language, OOP, I/O, concurrency) | Learning modern Java (11+) features |
| JDBC, JMX, JNDI, RMI, JavaBeans topics (not on dev.java) | Records, pattern matching, sealed classes |
| Internationalization, Security deep-dives | Virtual threads, Gatherer API, modules |
| Swing/AWT GUI development | Stream API modern patterns |
| Working with legacy codebase | New projects, modern patterns |

> **Cross-reference:** For JDK API deep-dives with modern code examples, see `jdk-apis-reference.md`.
> The section below provides the complete lesson-level table of contents for each Oracle tutorial trail.

---

### 10.1 Learning Paths (docs.oracle.com/javase/tutorial/tutorialLearningPaths.html)

| Learning Path | Trails Included |
|---|---|
| **New to Java** | Getting Started → Learning the Java Language → Essential Java Classes → Collections → Date-Time → Deployment |
| **Building on the Foundation** | Custom Networking, Generics (advanced), Full-Screen Exclusive, Internationalization |
| **Cherish the Client** | JavaBeans → Swing GUI → Deployment |
| **Fervor over Server** | JDBC → RMI → JNDI |

---

### 10.2 Trail: Learning the Java Language

> **URL:** `docs.oracle.com/javase/tutorial/java/`

#### 10.2.1 OOP Concepts
| Lesson | Topics |
|---|---|
| What Is an Object? | State (fields), behavior (methods), analogy with real-world objects |
| What Is a Class? | Blueprint, class declaration, fields, methods, constructors |
| What Is Inheritance? | Superclass/subclass, code reuse, `extends` |
| What Is an Interface? | Contracts, abstract methods, implementing interfaces |
| What Is a Package? | Namespaces, organizing related classes, API packages |
| Questions and Exercises | Review questions for OOP fundamentals |

#### 10.2.2 Language Basics
| Lesson | Sub-Topics |
|---|---|
| **Variables** | Primitive Data Types (`byte`, `short`, `int`, `long`, `float`, `double`, `char`, `boolean`), Arrays (declaring, creating, copying), Variable Summary (fields vs locals vs parameters) |
| **Operators** | Assignment & Arithmetic, Unary, Equality & Relational & Conditional, Bitwise & Bit Shift, Operator Expression Summary |
| **Expressions, Statements, Blocks** | Expression evaluation, statement types, block scope |
| **Control Flow Statements** | `if-then`, `if-then-else`, `switch`, `while`, `do-while`, `for` (standard & enhanced), Branching (`break`, `continue`, `return`) |

#### 10.2.3 Classes and Objects
| Lesson | Sub-Topics |
|---|---|
| **Classes** | Declaring Classes, Declaring Member Variables, Defining Methods, Providing Constructors, Passing Information to Method/Constructor (parameters, varargs) |
| **Objects** | Creating Objects (`new`), Using Objects (fields, methods, dot operator) |
| **More on Classes** | Returning a Value, `this` Keyword, Controlling Access (`public`/`private`/`protected`/default), Understanding Class Members (`static`), Initializing Fields |
| **Nested Classes** | Inner Classes, Local Classes, Anonymous Classes, Lambda Expressions, Method References, When to Use Which |
| **Enum Types** | Defining enums, enum methods, constructors in enums |

#### 10.2.4 Annotations
| Lesson | Content |
|---|---|
| Annotations Basics | What they are, where to use them |
| Declaring Annotations | `@interface`, elements, default values |
| Predefined Annotations | `@Override`, `@Deprecated`, `@SuppressWarnings`, `@SafeVarargs`, `@FunctionalInterface` |
| Type Annotations | Annotations on type use (Java 8+) |
| Repeating Annotations | `@Repeatable`, container annotations |

#### 10.2.5 Interfaces and Inheritance
| Lesson | Sub-Topics |
|---|---|
| **Interfaces** | Defining an Interface, Implementing an Interface, Using Interface as a Type, Evolving Interfaces, Default Methods |
| **Inheritance** | Multiple Inheritance of State/Implementation/Type, Overriding and Hiding Methods, Polymorphism, Hiding Fields, Using `super`, Object as a Superclass (`toString`/`equals`/`hashCode`/`clone`/`getClass`/`finalize`), Writing Final Classes and Methods, Abstract Methods and Classes |

#### 10.2.6 Numbers and Strings
| Lesson | Sub-Topics |
|---|---|
| **Numbers** | Number Classes (wrapper types), Formatting Numeric Print Output, Beyond Basic Arithmetic (`Math` class methods) |
| **Characters** | `char` type, `Character` class methods |
| **Strings** | Converting Between Numbers and Strings, Manipulating Characters in a String, Comparing Strings, `StringBuilder` |
| **Autoboxing and Unboxing** | Automatic conversion between primitives and wrappers |

#### 10.2.7 Generics (Comprehensive)
| Lesson | Sub-Topics |
|---|---|
| **Why Use Generics?** | Type safety, eliminating casts |
| **Generic Types** | Type parameter conventions (`T`, `E`, `K`, `V`, `N`), raw types |
| **Generic Methods** | Declaring and invoking generic methods |
| **Bounded Type Parameters** | `<T extends Number>`, multiple bounds |
| **Generics, Inheritance, and Subtypes** | Parameterized type relationships |
| **Type Inference** | Diamond operator, target types |
| **Wildcards** | Upper Bounded (`? extends T`), Unbounded (`?`), Lower Bounded (`? super T`), Wildcards and Subtyping, Wildcard Capture and Helper Methods, Guidelines for Wildcard Use (PECS) |
| **Type Erasure** | Erasure of Generic Types, Erasure of Generic Methods, Bridge Methods, Non-Reifiable Types |
| **Restrictions on Generics** | Cannot instantiate type parameters, no primitives, no `instanceof`, no arrays of parameterized types, no overload-by-erasure |

#### 10.2.8 Packages
| Lesson | Content |
|---|---|
| Creating a Package | `package` declaration, package hierarchy |
| Naming a Package | Reverse domain naming convention |
| Using Package Members | `import`, static import, fully qualified names |
| Managing Source and Class Files | Directory structure matching packages |

---

### 10.3 Trail: Essential Java Classes

> **URL:** `docs.oracle.com/javase/tutorial/essential/`

#### 10.3.1 Exceptions
| Lesson | Sub-Topics |
|---|---|
| **What Is an Exception?** | Exception hierarchy, call stack, exception object |
| **The Catch or Specify Requirement** | Three kinds: checked, error, runtime (unchecked) |
| **Catching and Handling Exceptions** | The `try` Block, The `catch` Blocks, The `finally` Block, The `try-with-resources` Statement, Putting It All Together |
| **Specifying Exceptions Thrown by a Method** | `throws` clause |
| **How to Throw Exceptions** | `throw` statement, Chained Exceptions, Creating Your Own Exception Classes |
| **Unchecked Exceptions — The Controversy** | When to use checked vs unchecked |
| **Advantages of Exceptions** | Separating error-handling from logic, propagating errors, grouping error types |

> **Cross-reference:** See `jdk-apis-reference.md` § Exception Handling for full hierarchy diagram, code examples, and best practices table.

#### 10.3.2 Basic I/O
| Lesson | Sub-Topics |
|---|---|
| **I/O Streams** | Byte Streams (`FileInputStream`/`FileOutputStream`), Character Streams (`FileReader`/`FileWriter`), Buffered Streams, Scanning and Formatting (`Scanner`, `Formatter`, `printf`), I/O from the Command Line (`Console`), Data Streams (`DataInputStream`/`DataOutputStream`), Object Streams (serialization/deserialization) |
| **File I/O (NIO.2)** | What Is a Path?, The Path Class, Path Operations, File Operations, Checking a File or Directory, Deleting a File or Directory, Copying a File or Directory, Moving a File or Directory, Managing Metadata (file attributes), Reading/Writing/Creating Files, Random Access Files, Creating and Reading Directories, Links (symbolic & hard), Walking the File Tree (`FileVisitor`), Finding Files (`PathMatcher`), Watching a Directory for Changes (`WatchService`), Legacy File I/O Code |

> **Cross-reference:** See `jdk-apis-reference.md` § I/O API for modern `Files` utility methods and code examples.

#### 10.3.3 Concurrency
| Lesson | Sub-Topics |
|---|---|
| **Processes and Threads** | Process vs thread, the Java threading model |
| **Thread Objects** | Defining and Starting a Thread (`Runnable` vs `Thread`), Pausing with `sleep()`, Interrupts, Joins, `SimpleThreads` Example |
| **Synchronization** | Thread Interference, Memory Consistency Errors, Synchronized Methods, Intrinsic Locks and Synchronization, Atomic Access |
| **Liveness** | Deadlock, Starvation and Livelock |
| **Guarded Blocks** | `wait()`/`notifyAll()` pattern |
| **Immutable Objects** | A Synchronized Class Example, Strategy for Defining Immutable Objects |
| **High-Level Concurrency Objects** | Lock Objects (`ReentrantLock`), Executors, Executor Interfaces, Thread Pools, Fork/Join Framework, Concurrent Collections, Atomic Variables, Concurrent Random Numbers (`ThreadLocalRandom`) |

> **Cross-reference:** See `jdk-apis-reference.md` § Concurrency for modern code examples including virtual threads, `CompletableFuture`, structured concurrency.

#### 10.3.4 The Platform Environment
| Lesson | Sub-Topics |
|---|---|
| **Configuration Utilities** | Properties (`java.util.Properties`), Command-Line Arguments, Environment Variables |
| **System Utilities** | CLI I/O (`System.in`/`out`/`err`), System Properties, The Security Manager, Miscellaneous System Methods |
| **PATH and CLASSPATH** | Setting PATH, setting CLASSPATH, class loading |

#### 10.3.5 Regular Expressions
| Lesson | Content |
|---|---|
| Introduction | What regex is, Java's `java.util.regex` package |
| Test Harness | `RegexTestHarness` utility for testing patterns |
| String Literals | Matching simple strings, metacharacters |
| Character Classes | `[abc]`, `[a-z]`, negation `[^abc]`, ranges, unions, intersections |
| Predefined Character Classes | `\d`, `\s`, `\w`, `.` and their negations |
| Quantifiers | Greedy (`*`, `+`, `?`), Reluctant (`*?`, `+?`, `??`), Possessive (`*+`, `++`, `?+`) |
| Capturing Groups | Numbered groups `()`, backreferences `\1` |
| Boundary Matchers | `^`, `$`, `\b` (word boundary) |
| Methods of `Pattern` | `compile()`, `matches()`, `split()`, flags |
| Methods of `Matcher` | `find()`, `start()`, `end()`, `group()`, `replaceAll()`, `appendReplacement()` |
| `PatternSyntaxException` | Error handling, understanding error messages |
| Unicode Support | Unicode categories, scripts, blocks |

---

### 10.4 Trail: Collections

> **URL:** `docs.oracle.com/javase/tutorial/collections/`

| Lesson | Sub-Topics |
|---|---|
| **Introduction** | What is a collection, benefits, design goals |
| **Interfaces: Collection** | Core `Collection` interface, iteration, bulk operations |
| **Interfaces: Set** | `Set` contract, `HashSet`, `TreeSet`, `LinkedHashSet`, set operations (union/intersection/difference) |
| **Interfaces: List** | `List` contract, positional access, `ListIterator`, range-view (`subList`) |
| **Interfaces: Queue** | `Queue` contract, `offer`/`poll`/`peek` |
| **Interfaces: Deque** | Double-ended queue, using as stack or queue |
| **Interfaces: Map** | `Map` contract, `HashMap`, `TreeMap`, `LinkedHashMap`, map views |
| **Interfaces: Object Ordering** | `Comparable` vs `Comparator`, natural ordering |
| **Interfaces: SortedSet** | Range-view, endpoints, `Comparator` access |
| **Interfaces: SortedMap** | Range-view, endpoints, key ordering |
| **Aggregate Operations** | Pipelines, Reduction (`reduce`, `collect`), Parallelism |
| **Implementations: Set** | General-purpose, special-purpose, `EnumSet`, `CopyOnWriteArraySet` |
| **Implementations: List** | `ArrayList` vs `LinkedList`, `CopyOnWriteArrayList` |
| **Implementations: Map** | `HashMap`, `TreeMap`, `LinkedHashMap`, `EnumMap`, `WeakHashMap`, `IdentityHashMap`, `ConcurrentHashMap` |
| **Implementations: Queue** | `LinkedList`, `PriorityQueue` |
| **Implementations: Deque** | `ArrayDeque`, `LinkedList` |
| **Implementations: Wrapper** | `Collections.unmodifiableList()`, `Collections.synchronizedList()`, `Collections.checkedList()` |
| **Implementations: Convenience** | `Arrays.asList()`, `Collections.singleton()`, `Collections.emptyList()`, `Collections.nCopies()` |
| **Algorithms** | Sorting, shuffling, searching, composition, min/max, frequency, `disjoint` |
| **Custom Collection Implementations** | When to write one, extending abstract implementations |
| **Interoperability** | Compatibility with legacy APIs, API Design guidelines |

> **Cross-reference:** See `jdk-apis-reference.md` § Collections Framework for hierarchy diagram, immutable collections (Java 9+), sequenced collections (Java 21+).

---

### 10.5 Trail: Date-Time

> **URL:** `docs.oracle.com/javase/tutorial/datetime/`

| Lesson | Sub-Topics |
|---|---|
| **Date-Time Overview** | Design Principles, The Date-Time Packages, Method Naming Conventions |
| **Standard Calendar** | Overview, `DayOfWeek` and `Month` Enums, Date Classes (`LocalDate`, `YearMonth`, `MonthDay`, `Year`), Date and Time Classes (`LocalTime`, `LocalDateTime`), Time Zone and Offset Classes (`ZonedDateTime`, `OffsetDateTime`, `OffsetTime`), `Instant` Class, Parsing and Formatting, The Temporal Package (Temporal Adjusters, Temporal Queries), Period and Duration, Clock, Non-ISO Date Conversion, Legacy Date-Time Code |

> **Cross-reference:** See `jdk-apis-reference.md` § Date-Time API for code examples and java.time class summary.

---

### 10.6 Trail: Custom Networking

> **URL:** `docs.oracle.com/javase/tutorial/networking/`

| Lesson | Sub-Topics |
|---|---|
| **Overview** | Networking Basics, what you may already know |
| **Working with URLs** | What Is a URL?, Creating a URL, Parsing a URL, Reading Directly from a URL, Connecting to a URL, Reading from and Writing to a URL Connection |
| **All About Sockets** | What Is a Socket?, Reading from and Writing to a Socket, Writing the Server Side of a Socket |
| **All About Datagrams** | What Is a Datagram?, Writing a Datagram Client and Server, Broadcasting to Multiple Recipients |
| **Programmatic Access to Network Parameters** | What Is a Network Interface?, Retrieving Network Interfaces, Listing Network Interface Addresses, Network Interface Parameters |
| **Working with Cookies** | HTTP State Management with Cookies, `CookieHandler`, Default `CookieManager`, Custom `CookieManager` |

---

### 10.7 Trail: JDBC Database Access

> **URL:** `docs.oracle.com/javase/tutorial/jdbc/`

| Lesson | Sub-Topics |
|---|---|
| **JDBC Introduction** | JDBC Architecture, Relational Database Overview |
| **JDBC Basics** | Getting Started, Processing SQL Statements, Establishing a Connection, Connecting with `DataSource`, Handling `SQLException`s, Setting Up Tables |
| **Working with Result Sets** | Using `ResultSet`, Retrieving Column Values, Cursors, Updating Rows |
| **Prepared Statements** | Parameterized SQL, batch updates |
| **Transactions** | Disabling auto-commit, commit/rollback, savepoints |
| **RowSet Objects** | Using `JdbcRowSet`, `CachedRowSet`, `JoinRowSet`, `FilteredRowSet`, `WebRowSet` |
| **Advanced Data Types** | Large Objects (BLOB/CLOB), SQLXML Objects, Array Objects, DISTINCT Type, Structured Objects, Customized Type Mappings, Datalink Objects, RowId Objects |
| **Stored Procedures** | Creating and calling stored procedures (Java DB, MySQL examples) |
| **Using JDBC in GUI** | Integrating JDBC with Swing applications |

---

### 10.8 Trail: Internationalization

> **URL:** `docs.oracle.com/javase/tutorial/i18n/`

| Lesson | Sub-Topics |
|---|---|
| **Introduction** | A Quick Example — internationalization in action |
| **Setting the Locale** | Creating a Locale, BCP 47 Extensions, Identifying Available Locales, Language Tag Filtering and Lookup, Scope of a Locale, Locale-Sensitive Services SPI |
| **Isolating Locale-Specific Data** | About `ResourceBundle`, Preparing to Use a `ResourceBundle`, Backing a `ResourceBundle` with Properties Files, Using `ListResourceBundle`, Customizing Resource Bundle Loading |
| **Formatting** | Numbers and Currencies, Dates and Times, Messages (Dealing with Compound Messages, Handling Plurals) |
| **Working with Text** | Checking Character Properties, Comparing Strings, Unicode (Terminology, Supplementary Characters, Character/String APIs, Design Considerations), Detecting Text Boundaries (Character/Word/Sentence/Line), Converting Non-Unicode Text, Normalizing Text, Working with Bidirectional Text |
| **Network Resources** | Internationalized Domain Names (IDN) |
| **Service Providers** | Installing and configuring locale-sensitive services |

---

### 10.9 Trail: Reflection

> **URL:** `docs.oracle.com/javase/tutorial/reflect/`

| Lesson | Sub-Topics |
|---|---|
| **Uses of Reflection** | Extensibility, class browsers/visual development, debuggers/test tools |
| **Drawbacks** | Performance overhead, security restrictions, exposure of internals |
| **Classes** | Retrieving `Class` objects, examining class modifiers/types, discovering class members |
| **Members: Fields** | Obtaining field types, retrieving/setting field values |
| **Members: Methods** | Obtaining method type information, invoking methods |
| **Members: Constructors** | Finding constructors, creating new class instances |
| **Arrays and Enumerated Types** | Creating/manipulating arrays, examining/getting/setting enum fields |

---

### 10.10 Trail: Security

> **URL:** `docs.oracle.com/javase/tutorial/security/`

| Lesson | Sub-Topics |
|---|---|
| **Security Features Overview** | Java platform security architecture |
| **Creating a Policy File** | Policy tool, granting permissions |
| **Quick Tour of Controlling Applications** | Observing application freedom, running with a Security Manager, setting up a policy file, seeing the policy file effects |
| **API and Tools Use for Secure Code and File Exchanges** | _Signing Code and Granting It Permissions:_ signer steps (signing JAR, exporting certificate) + receiver steps (importing certificate, setting policy, running). _Exchanging Files:_ sender steps (signing document) + receiver steps (verifying signature) |
| **Generating and Verifying Signatures** | Generating a Digital Signature (step-by-step: `KeyPairGenerator`, `Signature`, sign data, export), Verifying a Digital Signature (step-by-step: import key, verify) |
| **Implementing Your Own Permission** | ExampleGame, HighScore, HighScorePermission classes, setting up policy, putting it all together |

---

### 10.11 Trail: JMX (Java Management Extensions)

> **URL:** `docs.oracle.com/javase/tutorial/jmx/`

| Lesson | Sub-Topics |
|---|---|
| **Overview** | Why Use JMX?, JMX Architecture, Monitoring and Management of the JVM |
| **Introducing MBeans** | Standard MBeans, MXBeans |
| **Notifications** | JMX notification mechanism |
| **Remote Management** | Exposing for Remote Management with JConsole, Creating a Custom JMX Client |

---

### 10.12 Other Specialized Trails (Summary)

These trails cover topics available in the Oracle tutorials but not detailed above. They are listed as-is for reference:

| Trail | What It Covers | URL Path |
|---|---|---|
| **Getting Started** | First Java program, "Hello World" in NetBeans/CLI | `getStarted/` |
| **JavaBeans** | Visual components, properties, events, persistence, BeanInfo, customizers | `javabeans/` |
| **Generics (Extra)** | Standalone advanced generics trail (extends § 10.2.7) | `extra/generics/` |
| **JAXB** | XML binding, marshalling/unmarshalling Java objects and XML | `jaxb/` |
| **JAXP** | XML processing — DOM, SAX, StAX, XSLT | `jaxp/` |
| **JNDI** | Java Naming and Directory Interface — LDAP, DNS, naming operations | `jndi/` |
| **RMI** | Remote Method Invocation — distributed Java, stubs, registries | `rmi/` |
| **Sockets Direct Protocol** | High-performance networking using InfiniBand | `sdp/` |
| **Sound** | Java Sound API — MIDI, sampled audio, playback, capture | `sound/` |
| **2D Graphics** | Java 2D API — shapes, text, images, printing, advanced topics | `2d/` |
| **Full-Screen Exclusive Mode** | Full-screen applications, display modes, page flipping, double buffering | `extra/fullscreen/` |
| **Deployment** | JAR packaging, applets, Java Web Start, system properties | `deployment/` |
| **Creating GUI with Swing** | Components, layout, events, painting, data transfer, drag-and-drop | `uiswing/` |

---

### 10.13 Complete Trail Map

All trails organized by category from the official Oracle tutorial index:

| Category | Trails |
|---|---|
| **Basics** | Getting Started, Learning the Java Language, Essential Java Classes, Collections, Date-Time APIs, Deployment |
| **GUI (Client)** | Creating GUI with Swing, 2D Graphics, Full-Screen Exclusive Mode |
| **Specialized** | Custom Networking, Extension Mechanism, Full-Screen Exclusive Mode, Generics, Internationalization, JavaBeans, JAXB, JAXP, JDBC, JMX, JNDI, Reflection, RMI, Security, Sockets Direct Protocol, Sound |
