# JDK APIs — Comprehensive Reference Guide

> **Purpose:** Self-contained deep-dive reference for JDK standard library APIs.
> Covers Concurrency & Multithreading, Streams & Gatherers, Exception Handling,
> Collections, I/O, Date-Time, and more — with code examples and explanations scraped
> from Oracle/dev.java tutorials and official documentation.

---

## Table of Contents

1. [Concurrency & Multithreading](#1-concurrency--multithreading)
   - [1.1 Processes and Threads](#11-processes-and-threads)
   - [1.2 Creating and Starting Threads](#12-creating-and-starting-threads)
   - [1.3 Thread Lifecycle and Control](#13-thread-lifecycle-and-control)
   - [1.4 Synchronization](#14-synchronization)
   - [1.5 Liveness Problems](#15-liveness-problems)
   - [1.6 High-Level Concurrency APIs](#16-high-level-concurrency-apis)
   - [1.7 Virtual Threads (Java 21+)](#17-virtual-threads-java-21)
   - [1.8 Structured Concurrency (Preview)](#18-structured-concurrency-preview)
2. [Exception Handling](#2-exception-handling)
   - [2.1 Exception Hierarchy](#21-exception-hierarchy)
   - [2.2 Catching and Handling](#22-catching-and-handling)
   - [2.3 Throwing Exceptions](#23-throwing-exceptions)
   - [2.4 Creating Custom Exceptions](#24-creating-custom-exceptions)
   - [2.5 Best Practices](#25-best-practices)
3. [Stream API](#3-stream-api)
   - [3.1 Map/Filter/Reduce Pipeline](#31-mapfilterreduce-pipeline)
   - [3.2 Intermediate Operations](#32-intermediate-operations)
   - [3.3 Terminal Operations](#33-terminal-operations)
   - [3.4 Collectors](#34-collectors)
   - [3.5 Optionals](#35-optionals)
   - [3.6 Parallel Streams](#36-parallel-streams)
4. [Gatherer API (Java 24+)](#4-gatherer-api-java-24)
   - [4.1 Core Concepts](#41-core-concepts)
   - [4.2 Writing Integrators](#42-writing-integrators)
   - [4.3 Managing Internal State](#43-managing-internal-state)
   - [4.4 Interrupting Streams](#44-interrupting-streams)
   - [4.5 Finishers](#45-finishers)
   - [4.6 Parallel Gatherers](#46-parallel-gatherers)
   - [4.7 Built-in Gatherers](#47-built-in-gatherers)
5. [Collections Framework](#5-collections-framework)
6. [I/O API](#6-io-api)
7. [Date-Time API (java.time)](#7-date-time-api-javatime)
8. [Other Essential JDK APIs](#8-other-essential-jdk-apis)

---

## 1. Concurrency & Multithreading

### 1.1 Processes and Threads

**Process:** A self-contained execution environment with its own memory space. Most JVM implementations run as a single process.

**Thread:** A lightweight unit of execution within a process. Threads share the process's resources (memory, open files). Every Java application has at least one thread — the **main thread**.

**Why Concurrency Matters:**
- Modern apps must handle multiple tasks simultaneously (UI, I/O, computation)
- Java was designed from the ground up for concurrent programming
- JDK provides both low-level (`java.lang.Thread`) and high-level (`java.util.concurrent`) APIs

### 1.2 Creating and Starting Threads

**Two approaches to creating threads:**

```java
// Approach 1: Implement Runnable (PREFERRED — allows extending other classes)
public class HelloRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Hello from a thread!");
    }

    public static void main(String[] args) {
        new Thread(new HelloRunnable()).start();
    }
}

// Approach 2: Extend Thread (simpler but limits inheritance)
public class HelloThread extends Thread {
    @Override
    public void run() {
        System.out.println("Hello from a thread!");
    }

    public static void main(String[] args) {
        new HelloThread().start();
    }
}

// Modern approach: Lambda with Runnable
Thread thread = new Thread(() -> System.out.println("Hello via lambda!"));
thread.start();
```

**Key rules:**
- Always call `start()`, never `run()` directly (calling `run()` executes in the current thread)
- `Runnable` is preferred because it separates the task from the thread mechanism
- A thread can only be started once

### 1.3 Thread Lifecycle and Control

| Method | Purpose |
|---|---|
| `Thread.sleep(millis)` | Pauses current thread for specified time; throws `InterruptedException` |
| `thread.interrupt()` | Sets the thread's interrupt flag; sleeping/waiting threads throw `InterruptedException` |
| `Thread.interrupted()` | Returns and clears the interrupt status of the current thread |
| `thread.isInterrupted()` | Returns (but does not clear) the interrupt status |
| `thread.join()` | Waits for the thread to complete |
| `thread.join(millis)` | Waits up to `millis` milliseconds for the thread to complete |

```java
// Respecting interrupt requests
public void run() {
    while (!Thread.currentThread().isInterrupted()) {
        // do work
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // Restore interrupt status and exit
            Thread.currentThread().interrupt();
            return;
        }
    }
}

// Using join() to wait for completion
Thread worker = new Thread(() -> heavyComputation());
worker.start();
worker.join(); // blocks until worker finishes
System.out.println("Worker completed");
```

### 1.4 Synchronization

**Why synchronization is needed:**
- **Thread interference:** Two threads modifying the same data simultaneously produce unexpected results
- **Memory consistency errors:** Different threads have inconsistent views of shared memory

**Synchronized Methods:**
```java
public class Counter {
    private int count = 0;

    // Only one thread can execute this at a time per instance
    public synchronized void increment() {
        count++;
    }

    public synchronized int getCount() {
        return count;
    }
}
```

**Synchronized Blocks (finer-grained control):**
```java
public void addName(String name) {
    synchronized(this) {
        lastName = name;
        nameCount++;
    }
    nameList.add(name); // not synchronized — runs outside the lock
}

// Using a private lock object (recommended for encapsulation)
private final Object lock = new Object();

public void safeMethod() {
    synchronized(lock) {
        // critical section
    }
}
```

**Volatile Variables:**
```java
// Guarantees visibility: writes by one thread are immediately visible to others
// Does NOT guarantee atomicity of compound operations (like i++)
private volatile boolean running = true;
```

**Atomic Variables (`java.util.concurrent.atomic`):**
```java
import java.util.concurrent.atomic.AtomicInteger;

private final AtomicInteger count = new AtomicInteger(0);

public void increment() {
    count.incrementAndGet(); // thread-safe, lock-free
}

// Other atomic types:
// AtomicLong, AtomicBoolean, AtomicReference<T>
// AtomicIntegerArray, AtomicLongArray, AtomicReferenceArray<T>
```

### 1.5 Liveness Problems

| Problem | Description | Prevention |
|---|---|---|
| **Deadlock** | Two+ threads forever waiting for each other's locks | Always acquire locks in a consistent order |
| **Starvation** | Thread can't gain regular access to shared resources (greedy threads keep the lock) | Use fair locks (`new ReentrantLock(true)`) |
| **Livelock** | Threads keep responding to each other without making progress | Add randomness to retry strategies |

```java
// Deadlock example — DON'T DO THIS
class DeadlockExample {
    private final Object lockA = new Object();
    private final Object lockB = new Object();

    void method1() {
        synchronized(lockA) {          // acquires lockA
            synchronized(lockB) {      // waits for lockB
                // work
            }
        }
    }

    void method2() {
        synchronized(lockB) {          // acquires lockB
            synchronized(lockA) {      // waits for lockA → DEADLOCK
                // work
            }
        }
    }
}

// Fix: Always acquire locks in the same order (lockA, then lockB)
```

### 1.6 High-Level Concurrency APIs

#### Lock Objects (`java.util.concurrent.locks`)

```java
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

// ReentrantLock — more flexible than synchronized
ReentrantLock lock = new ReentrantLock();
lock.lock();
try {
    // critical section
} finally {
    lock.unlock(); // ALWAYS unlock in finally
}

// tryLock — attempt without blocking
if (lock.tryLock()) {
    try {
        // critical section
    } finally {
        lock.unlock();
    }
} else {
    // lock not available, do something else
}

// ReadWriteLock — multiple readers OR single writer
ReadWriteLock rwLock = new ReentrantReadWriteLock();
rwLock.readLock().lock();    // multiple threads can read simultaneously
rwLock.writeLock().lock();   // exclusive write access
```

#### Executors and Thread Pools

```java
import java.util.concurrent.*;

// Fixed thread pool — reuses a fixed number of threads
ExecutorService fixedPool = Executors.newFixedThreadPool(4);

// Cached thread pool — creates threads as needed, reuses idle ones
ExecutorService cachedPool = Executors.newCachedThreadPool();

// Single thread executor — guarantees sequential execution
ExecutorService singleThread = Executors.newSingleThreadExecutor();

// Scheduled executor — delayed/periodic tasks
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

// Submitting tasks
fixedPool.execute(() -> doWork());                     // no return value
Future<String> future = fixedPool.submit(() -> compute()); // returns Future

// Getting result from Future
String result = future.get();                          // blocks until complete
String result = future.get(5, TimeUnit.SECONDS);       // blocks with timeout

// ALWAYS shutdown executors
fixedPool.shutdown();                                  // waits for submitted tasks
fixedPool.shutdownNow();                               // attempts to cancel running tasks
```

#### CompletableFuture (Java 8+)

```java
import java.util.concurrent.CompletableFuture;

// Async computation
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> fetchData());

// Chaining transformations
CompletableFuture<Integer> result = future
    .thenApply(data -> parse(data))        // transform result
    .thenApply(parsed -> process(parsed))  // chain more transformations
    .exceptionally(ex -> handleError(ex)); // handle exceptions

// Combining futures
CompletableFuture<String> combined = CompletableFuture
    .allOf(future1, future2, future3)
    .thenApply(v -> combineResults());

// Running two futures and combining results
CompletableFuture<String> combined = future1
    .thenCombine(future2, (r1, r2) -> r1 + r2);
```

#### Concurrent Collections

| Collection | Description | Thread Safety |
|---|---|---|
| `ConcurrentHashMap` | High-concurrency hash map | Lock striping, no full locking |
| `CopyOnWriteArrayList` | List copied on each write | Fast reads, slow writes |
| `CopyOnWriteArraySet` | Set backed by CopyOnWriteArrayList | Fast iteration, slow mutation |
| `ConcurrentLinkedQueue` | Non-blocking unbounded queue | Lock-free CAS operations |
| `ConcurrentLinkedDeque` | Non-blocking unbounded deque | Lock-free CAS operations |
| `LinkedBlockingQueue` | Bounded blocking queue | Locks on put/take |
| `ArrayBlockingQueue` | Fixed-capacity blocking queue | Single lock |
| `PriorityBlockingQueue` | Priority-ordered blocking queue | Lock-based |
| `SynchronousQueue` | Zero-capacity handoff queue | Each put must wait for a take |
| `BlockingDeque` | Double-ended blocking queue | Locks on both ends |

```java
// ConcurrentHashMap — safe for concurrent access
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
map.put("key", 1);
map.compute("key", (k, v) -> v == null ? 1 : v + 1); // atomic compute
map.merge("key", 1, Integer::sum);                     // atomic merge

// BlockingQueue — producer-consumer pattern
BlockingQueue<Task> queue = new LinkedBlockingQueue<>(100);

// Producer
queue.put(new Task());       // blocks if queue is full

// Consumer
Task task = queue.take();    // blocks if queue is empty
```

#### Fork/Join Framework

```java
import java.util.concurrent.*;

// RecursiveTask<T> for tasks that return a value
class SumTask extends RecursiveTask<Long> {
    private static final int THRESHOLD = 1000;
    private final long[] array;
    private final int start, end;

    SumTask(long[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        if (end - start <= THRESHOLD) {
            long sum = 0;
            for (int i = start; i < end; i++) {
                sum += array[i];
            }
            return sum;
        }
        int mid = (start + end) / 2;
        SumTask left = new SumTask(array, start, mid);
        SumTask right = new SumTask(array, mid, end);
        left.fork();             // execute left in another thread
        long rightResult = right.compute(); // compute right in current thread
        long leftResult = left.join();      // wait for left
        return leftResult + rightResult;
    }
}

// Usage
ForkJoinPool pool = ForkJoinPool.commonPool();
long result = pool.invoke(new SumTask(array, 0, array.length));
```

### 1.7 Virtual Threads (Java 21+)

**Why virtual threads:**
- Platform threads are expensive (~1 MB stack, OS-level scheduling)
- Server apps spend most time blocking (DB, network I/O)
- Virtual threads are lightweight (~few KB), managed by the JVM
- When a virtual thread blocks, it's **unmounted** from the carrier (platform) thread

```java
// Creating virtual threads
// Preferred: Virtual thread executor
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    executor.submit(() -> handleRequest(request));
}

// Thread.Builder for named virtual threads
Thread.Builder builder = Thread.ofVirtual().name("worker-", 1);
Thread vt = builder.start(() -> doWork());

// Quick one-off
Thread t = Thread.startVirtualThread(() -> doWork());

// Factory for use with ExecutorService
ThreadFactory factory = Thread.ofVirtual().name("handler-", 0).factory();
```

**Best practices for virtual threads:**
- Do NOT pool virtual threads (create a new one per task)
- Use `Semaphore` for rate limiting instead of thread pools
- Avoid `synchronized` blocks during blocking operations (causes **pinning**)
- Use `ReentrantLock` instead of `synchronized` for lock-sensitive code
- Detect pinning: `-Djdk.tracePinnedThreads=short`
- Minimize `ThreadLocal` usage (virtual threads can be numerous)

```java
// Rate limiting with Semaphore instead of limited thread pool
Semaphore semaphore = new Semaphore(10); // max 10 concurrent operations

try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    for (var request : requests) {
        executor.submit(() -> {
            semaphore.acquire();
            try {
                callExternalService(request);
            } finally {
                semaphore.release();
            }
        });
    }
}

// Avoiding pinning: use ReentrantLock instead of synchronized
private final ReentrantLock lock = new ReentrantLock();

public void safeForVirtualThreads() {
    lock.lock();
    try {
        blockingIOOperation(); // virtual thread can unmount
    } finally {
        lock.unlock();
    }
}
```

**When to use virtual threads vs platform threads:**

| Use Virtual Threads | Use Platform Threads |
|---|---|
| I/O-bound tasks (HTTP, DB, file) | CPU-bound computation |
| High-concurrency servers | Tasks needing `synchronized` during blocking |
| Request-per-thread model | Tasks needing thread-level scheduling control |
| Replacing thread pools for I/O | Carrier threads for virtual threads |

### 1.8 Structured Concurrency (Preview)

```java
// Java 21+ (preview) — StructuredTaskScope
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Subtask<String> user = scope.fork(() -> fetchUser(userId));
    Subtask<Order> order = scope.fork(() -> fetchOrder(orderId));

    scope.join();           // wait for both
    scope.throwIfFailed();  // propagate exceptions

    return new Response(user.get(), order.get());
}
// If either subtask fails, the other is cancelled automatically
```

**Scoped Values (Preview, companion to virtual threads):**
```java
// Thread-local replacement for virtual threads
private static final ScopedValue<User> CURRENT_USER = ScopedValue.newInstance();

ScopedValue.where(CURRENT_USER, authenticatedUser).run(() -> {
    handleRequest(); // CURRENT_USER.get() returns authenticatedUser
});
```

---

## 2. Exception Handling

### 2.1 Exception Hierarchy

```
java.lang.Throwable
├── java.lang.Error                    ← JVM/system errors (don't catch)
│   ├── OutOfMemoryError
│   ├── StackOverflowError
│   └── VirtualMachineError
└── java.lang.Exception                ← Application errors
    ├── IOException                    ← CHECKED: must handle or declare
    ├── SQLException                   ← CHECKED
    ├── FileNotFoundException          ← CHECKED (extends IOException)
    └── java.lang.RuntimeException     ← UNCHECKED: optional to handle
        ├── NullPointerException
        ├── IllegalArgumentException
        ├── IndexOutOfBoundsException
        ├── ClassCastException
        ├── ArithmeticException
        └── UnsupportedOperationException
```

**Three kinds of exceptions:**

| Kind | Examples | Must Handle? | Typical Cause |
|---|---|---|---|
| **Checked exceptions** | `IOException`, `SQLException` | Yes — compile error otherwise | Recoverable external problems |
| **Runtime exceptions** | `NullPointerException`, `IllegalArgumentException` | No | Programming bugs |
| **Errors** | `OutOfMemoryError`, `StackOverflowError` | No — shouldn't catch | JVM/system failures |

### 2.2 Catching and Handling

**Basic try-catch-finally:**
```java
public void writeList() {
    PrintWriter out = null;
    try {
        out = new PrintWriter(new FileWriter("OutFile.txt"));
        for (int i = 0; i < SIZE; i++) {
            out.println("Value at: " + i + " = " + list.get(i));
        }
    } catch (IndexOutOfBoundsException e) {
        System.err.println("Index error: " + e.getMessage());
    } catch (IOException e) {
        System.err.println("I/O error: " + e.getMessage());
    } finally {
        if (out != null) {
            out.close(); // always close resources
        }
    }
}
```

**Multi-catch (Java 7+):**
```java
// Handle multiple exception types with a single handler
catch (IOException | SQLException ex) {
    logger.log(ex);
    throw ex;
}
// Note: ex is implicitly final — you cannot reassign it
// Note: exceptions in multi-catch cannot extend one another
```

**Try-with-resources (Java 7+):**
```java
// Resources implementing AutoCloseable are automatically closed
static String readFirstLineFromFile(String path) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
        return br.readLine();
    }
    // br.close() is called automatically, even if an exception occurs
}

// Multiple resources — closed in reverse order of declaration
try (
    ZipFile zf = new ZipFile(zipFileName);
    BufferedWriter writer = Files.newBufferedWriter(outputFilePath, charset)
) {
    // use both resources
}
// writer.close() called first, then zf.close()

// Suppressed exceptions
// If both the try block and close() throw, the close() exception is suppressed
// Retrieve suppressed exceptions: throwable.getSuppressed()
```

### 2.3 Throwing Exceptions

```java
// Throw statement
public Object pop() {
    if (size == 0) {
        throw new EmptyStackException();
    }
    return objectAt(--size);
}

// Declaring thrown checked exceptions
public void writeList() throws IOException {
    // IOException must be declared because it's checked
    // RuntimeExceptions need not be declared (but can be)
    PrintWriter out = new PrintWriter(new FileWriter("OutFile.txt"));
    // ...
}

// Chained exceptions — preserving the cause
try {
    // code that may throw IOException
} catch (IOException e) {
    throw new ApplicationException("Failed to process data", e);
    // e is preserved as the "cause" of the new exception
}

// Accessing stack trace information
catch (Exception e) {
    StackTraceElement[] elements = e.getStackTrace();
    for (StackTraceElement element : elements) {
        System.err.println(element.getFileName() + ":"
            + element.getLineNumber() + " >> "
            + element.getMethodName() + "()");
    }
}
```

### 2.4 Creating Custom Exceptions

```java
// Custom checked exception
public class InsufficientFundsException extends Exception {
    private final double amount;

    public InsufficientFundsException(double amount) {
        super("Insufficient funds: needed " + amount);
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}

// Custom unchecked exception
public class InvalidOrderException extends RuntimeException {
    private final String orderId;

    public InvalidOrderException(String orderId, String message) {
        super(message);
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}

// Exception hierarchy for a library
public class LinkedListException extends Exception { /* base */ }
public class InvalidIndexException extends LinkedListException { /* specific */ }
public class ObjectNotFoundException extends LinkedListException { /* specific */ }
```

**When to create custom exceptions:**
- When JDK exceptions don't represent your domain-specific error
- When users need to differentiate your package's errors from others
- When your code throws more than one related exception
- Convention: always append `Exception` to the class name

### 2.5 Best Practices

| Do | Don't |
|---|---|
| Catch specific exception types | Catch generic `Exception` or `Throwable` |
| Always include a meaningful message | Leave catch blocks empty |
| Use try-with-resources for `AutoCloseable` | Manually close resources in `finally` |
| Throw early, catch late | Swallow exceptions silently |
| Use unchecked for programming errors | Use checked for unrecoverable errors |
| Preserve the cause chain (`new Ex(msg, cause)`) | Lose the original exception |
| Log at the right level | Both log and rethrow (double logging) |
| Use `Objects.requireNonNull()` for null checks | Return null to indicate errors |

```java
// Good: specific, meaningful, with cause chain
try {
    return Integer.parseInt(input);
} catch (NumberFormatException e) {
    throw new InvalidInputException("Expected numeric value for field 'age', got: " + input, e);
}

// Good: Objects.requireNonNull for constructor validation
public Order(String orderId, Customer customer) {
    this.orderId = Objects.requireNonNull(orderId, "orderId must not be null");
    this.customer = Objects.requireNonNull(customer, "customer must not be null");
}
```

---

## 3. Stream API

### 3.1 Map/Filter/Reduce Pipeline

```java
// Basic pipeline: source → intermediate operations → terminal operation
List<String> names = List.of("Alice", "Bob", "Charlie", "Diana");

List<String> result = names.stream()
    .filter(name -> name.length() > 3)      // filter: keep names > 3 chars
    .map(String::toUpperCase)                // map: transform to uppercase
    .sorted()                                // sort: alphabetical
    .toList();                               // collect to list
// result = [ALICE, CHARLIE, DIANA]

// Reduce: combine all elements into a single result
int totalLength = names.stream()
    .mapToInt(String::length)
    .reduce(0, Integer::sum);
// totalLength = 18

// Creating streams
Stream.of("a", "b", "c");                    // from values
Arrays.stream(array);                         // from array
collection.stream();                          // from collection
Stream.generate(Math::random).limit(10);      // infinite, limited
Stream.iterate(0, n -> n + 2).limit(5);       // 0, 2, 4, 6, 8
Files.lines(Path.of("file.txt"));             // from file lines
"hello".chars();                              // IntStream from string
```

### 3.2 Intermediate Operations

| Operation | Description | Example |
|---|---|---|
| `filter(Predicate)` | Keep elements matching predicate | `.filter(s -> s.length() > 3)` |
| `map(Function)` | Transform each element | `.map(String::toUpperCase)` |
| `flatMap(Function)` | One-to-many mapping, flattens | `.flatMap(line -> Arrays.stream(line.split(" ")))` |
| `distinct()` | Remove duplicates (by `equals()`) | `.distinct()` |
| `sorted()` | Natural order sort | `.sorted()` |
| `sorted(Comparator)` | Custom sort | `.sorted(Comparator.comparing(String::length))` |
| `peek(Consumer)` | Debug/inspect (side effect) | `.peek(System.out::println)` |
| `limit(long)` | Take first N elements | `.limit(10)` |
| `skip(long)` | Skip first N elements | `.skip(5)` |
| `takeWhile(Predicate)` | Take while predicate is true (Java 9+) | `.takeWhile(n -> n < 10)` |
| `dropWhile(Predicate)` | Skip while predicate is true (Java 9+) | `.dropWhile(n -> n < 10)` |
| `mapToInt/Long/Double` | Map to a primitive stream | `.mapToInt(String::length)` |

### 3.3 Terminal Operations

| Operation | Description | Returns |
|---|---|---|
| `forEach(Consumer)` | Process each element | `void` |
| `toList()` | Collect to unmodifiable list (Java 16+) | `List<T>` |
| `toArray()` | Collect to array | `Object[]` or `T[]` |
| `collect(Collector)` | Mutable reduction | Depends on collector |
| `reduce(identity, op)` | Reduce to single value | `T` |
| `reduce(op)` | Reduce without identity | `Optional<T>` |
| `count()` | Count elements | `long` |
| `min(Comparator)` | Find minimum | `Optional<T>` |
| `max(Comparator)` | Find maximum | `Optional<T>` |
| `findFirst()` | First element | `Optional<T>` |
| `findAny()` | Any element (best for parallel) | `Optional<T>` |
| `anyMatch(Predicate)` | Any element matches? | `boolean` |
| `allMatch(Predicate)` | All elements match? | `boolean` |
| `noneMatch(Predicate)` | No element matches? | `boolean` |

### 3.4 Collectors

```java
import java.util.stream.Collectors;

// Basic collectors
.collect(Collectors.toList())                    // → ArrayList
.collect(Collectors.toUnmodifiableList())         // → unmodifiable List
.collect(Collectors.toSet())                      // → HashSet
.collect(Collectors.toMap(keyFn, valueFn))        // → HashMap
.collect(Collectors.joining(", "))                // → "a, b, c"
.collect(Collectors.joining(", ", "[", "]"))       // → "[a, b, c]"

// Grouping
Map<City, List<Person>> byCity = people.stream()
    .collect(Collectors.groupingBy(Person::getCity));

// Grouping with downstream collector
Map<City, Long> countByCity = people.stream()
    .collect(Collectors.groupingBy(Person::getCity, Collectors.counting()));

// Partitioning (boolean grouping)
Map<Boolean, List<Person>> adults = people.stream()
    .collect(Collectors.partitioningBy(p -> p.getAge() >= 18));

// Statistics
IntSummaryStatistics stats = people.stream()
    .collect(Collectors.summarizingInt(Person::getAge));
// stats.getAverage(), stats.getMax(), stats.getMin(), stats.getCount(), stats.getSum()

// Teeing (Java 12+) — combine two collectors into one result
var result = stream.collect(Collectors.teeing(
    Collectors.minBy(Comparator.naturalOrder()),
    Collectors.maxBy(Comparator.naturalOrder()),
    (min, max) -> new Range(min.orElseThrow(), max.orElseThrow())
));
```

### 3.5 Optionals

```java
// Creating Optionals
Optional<String> opt = Optional.of("value");           // must be non-null
Optional<String> maybe = Optional.ofNullable(value);   // null → empty
Optional<String> empty = Optional.empty();              // always empty

// Consuming values
opt.ifPresent(v -> System.out.println(v));
opt.ifPresentOrElse(
    v -> System.out.println(v),
    () -> System.out.println("empty")
);

// Extracting values
String value = opt.orElse("default");
String value = opt.orElseGet(() -> computeDefault());
String value = opt.orElseThrow();
String value = opt.orElseThrow(() -> new NotFoundException());

// Transforming
Optional<Integer> length = opt.map(String::length);
Optional<String> flat = opt.flatMap(this::findByName);  // avoids Optional<Optional<T>>
Optional<String> filtered = opt.filter(s -> s.length() > 3);

// Stream integration (Java 9+)
opt.stream(); // → Stream with 0 or 1 element
```

### 3.6 Parallel Streams

```java
// Creating parallel streams
list.parallelStream();           // from collection
stream.parallel();               // convert sequential → parallel

// When to use parallel streams:
// ✅ Large datasets (10,000+ elements)
// ✅ CPU-intensive per-element operations
// ✅ Independent (stateless) operations
// ✅ Operations that benefit from splitting (reduce, collect)

// When NOT to use parallel streams:
// ❌ Small datasets (overhead > benefit)
// ❌ Operations with shared mutable state
// ❌ I/O-bound operations (use virtual threads instead)
// ❌ Order-dependent operations with LinkedList
// ❌ Operations with side effects

// Stream characteristics affecting parallelism
// ORDERED, DISTINCT, SORTED, SIZED, NONNULL, IMMUTABLE, CONCURRENT, SUBSIZED
```

---

## 4. Gatherer API (Java 24+)

> The Gatherer API lets you create **custom intermediate operations** for the Stream API.
> It's analogous to the Collector API for terminal operations.

### 4.1 Core Concepts

**Architecture:**
- `Gatherer<T, A, R>` — interface modeling a custom intermediate operation
  - `T` = type of elements consumed from upstream
  - `A` = type of internal mutable state
  - `R` = type of elements produced to downstream
- `Gatherers` — factory class with built-in gatherers
- Used via `stream.gather(myGatherer)`

**When to use Gatherers:**
- Creating operations not available in the Stream API
- Fusing multiple operations into a single optimized step
- Creating windowed or stateful intermediate operations
- NOT for simple map/filter/flatMap — use the built-in methods for those

### 4.2 Writing Integrators

An **Integrator** consumes elements from upstream & pushes to downstream:

```java
// Simple mapping gatherer
static <T, R> Gatherer<T, ?, R> mapping(Function<T, R> mapper) {
    Gatherer.Integrator<Void, T, R> integrator =
        (_, element, downstream) -> downstream.push(mapper.apply(element));
    return Gatherer.of(integrator);
}

// Usage
var result = Stream.of("one", "two", "three")
    .gather(mapping(String::toUpperCase))
    .toList();
// result = [ONE, TWO, THREE]

// Filtering gatherer — conditionally push elements
static <T> Gatherer<T, ?, T> filtering(Predicate<T> predicate) {
    Gatherer.Integrator<Void, T, T> integrator =
        (_, element, downstream) -> {
            if (predicate.test(element)) {
                return downstream.push(element);
            }
            return true; // continue accepting elements
        };
    return Gatherer.of(integrator);
}
```

**Key rule on the integrator's return value:**
- Return `true` → continue accepting elements from upstream
- Return `false` → tell upstream to stop sending (short-circuit)
- Return `downstream.push(element)` → propagate downstream's rejection state

### 4.3 Managing Internal State

```java
// Distinct gatherer — uses a HashSet as internal mutable state
static <T> Gatherer<T, ?, T> distinct() {
    return Gatherer.ofSequential(
        HashSet::new,                                    // initializer: create state
        (Set<T> seen, T element, Gatherer.Downstream<? super T> downstream) -> {
            if (seen.add(element)) {                     // true if newly added
                return downstream.push(element);
            }
            return true;                                 // already seen, skip but continue
        }
    );
}

// dropWhile gatherer — uses a mutable boolean flag
static <T> Gatherer<T, ?, T> dropWhile(Predicate<T> predicate) {
    class Gate { boolean open = false; }                 // local mutable state class
    return Gatherer.ofSequential(
        Gate::new,
        (gate, element, downstream) -> {
            if (!gate.open && !predicate.test(element)) {
                gate.open = true;
            }
            if (gate.open) {
                return downstream.push(element);
            }
            return true;
        }
    );
}
```

**`Gatherer.of()` vs `Gatherer.ofSequential()`:**
- `of()` → gatherer supports parallel execution
- `ofSequential()` → gatherer is sequential only (but *doesn't* prevent the rest of the stream from being parallel)

### 4.4 Interrupting Streams

```java
// limit() gatherer — stops after N elements
static <E> Gatherer<E, ?, E> limit(long limit) {
    class Box { long counter = 0L; }
    return Gatherer.ofSequential(
        Box::new,
        (box, element, downstream) -> {
            if (box.counter < limit) {
                box.counter++;
                return downstream.push(element); // propagate rejection
            } else {
                return false;                    // signal upstream to stop
            }
        }
    );
}

// Key principles for downstream rejection:
// 1. A new downstream ALWAYS starts in non-rejecting state
// 2. A downstream can ONLY switch from non-rejecting → rejecting (never back)
// 3. A downstream can ONLY switch state when you push elements to it
// 4. Once you return false, your integrator is never called again
```

### 4.5 Finishers

A **finisher** runs after all upstream elements have been processed — for operations like sorting:

```java
// Sorting distinct gatherer — buffers all elements, pushes in order
static <E extends Comparable<E>> Gatherer<E, ?, E> sortedDistinct() {
    return Gatherer.ofSequential(
        TreeSet::new,                       // initializer: sorted set
        (set, element, downstream) -> {     // integrator: buffer elements
            set.add(element);
            return true;                    // never push here
        },
        (set, downstream) -> {              // finisher: push buffered results
            set.stream()
                .allMatch(downstream::push); // stops if downstream rejects
        }
    );
}

// Correct flat-mapper pattern (handles parallel streams, null, resource leaking)
static <T, R> Gatherer<T, ?, R> flatMapping(Function<T, Stream<R>> mapper) {
    return Gatherer.of(
        (_, element, downstream) -> {
            Stream<R> stream = mapper.apply(element);
            if (stream == null) return true;
            try (var s = stream.sequential()) {
                return s.allMatch(downstream::push);
            }
        }
    );
}
```

### 4.6 Parallel Gatherers

```java
// Parallel gatherer needs a COMBINER to merge state from different threads
static <E extends Comparable<E>> Gatherer<E, ?, E> parallelSortedDistinct() {
    return Gatherer.of(
        TreeSet::new,                                   // initializer
        (set, element, downstream) -> {                 // integrator
            set.add(element);
            return true;
        },
        (set1, set2) -> {                               // combiner: merge two sets
            set1.addAll(set2);
            return set1;
        },
        (set, downstream) ->                            // finisher
            set.stream().allMatch(downstream::push)
    );
}

// Key: sequential gatherers in parallel streams don't suppress parallelism
// Operations BEFORE the sequential gatherer run in parallel
// The gatherer itself runs sequentially
// Operations AFTER the gatherer run in parallel again
```

### 4.7 Built-in Gatherers

The `Gatherers` factory class provides ready-made gatherers:

| Gatherer | Description | Example |
|---|---|---|
| `Gatherers.fold(supplier, biFunction)` | Left-fold reduction as intermediate op; produces single element | `gather(Gatherers.fold(() -> "", (a, b) -> a + b))` |
| `Gatherers.scan(supplier, biFunction)` | Running accumulation; pushes each intermediate result | `gather(Gatherers.scan(() -> 0, Integer::sum))` |
| `Gatherers.mapConcurrent(max, mapper)` | Maps elements using virtual threads; limits concurrency | `gather(Gatherers.mapConcurrent(10, this::fetchUrl))` |
| `Gatherers.windowFixed(size)` | Groups into fixed-size non-overlapping windows | `gather(Gatherers.windowFixed(3))` → `[1,2,3], [4,5,6], [7]` |
| `Gatherers.windowSliding(size)` | Groups into sliding windows (overlap by 1) | `gather(Gatherers.windowSliding(3))` → `[1,2,3], [2,3,4], [3,4,5]` |

```java
// Chaining gatherers
var result = Stream.of("one", "two", "three", "four", "five", "six", "seven")
    .gather(
        filtering((String s) -> s.length() > 3)
            .andThen(mapping(String::toUpperCase))
    )
    .toList();
// result = [THREE, FOUR, FIVE, SEVEN]

// Sliding window with average calculation
var movingAverages = IntStream.rangeClosed(1, 9).boxed()
    .gather(Gatherers.windowSliding(3))
    .gather(mapping(window ->
        window.stream().mapToInt(Integer::intValue).average().orElse(0)))
    .toList();
// movingAverages = [2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0]

// mapConcurrent — parallel I/O with virtual threads
var results = urls.stream()
    .gather(Gatherers.mapConcurrent(10, url -> fetchContent(url)))
    .toList();
```

---

## 5. Collections Framework

> See also: `oracle-tutorials-guide.md` § Collections Framework for the full 14-part tutorial breakdown.

**Hierarchy Quick Reference:**

```
Iterable<T>
└── Collection<T>
    ├── List<T>           ← ordered, indexed, duplicates allowed
    │   ├── ArrayList     ← best general-purpose (array-backed)
    │   ├── LinkedList    ← fast insert/remove at both ends
    │   └── CopyOnWriteArrayList  ← concurrent, snapshot iteration
    ├── Set<T>            ← no duplicates
    │   ├── HashSet       ← unordered, O(1) operations
    │   ├── LinkedHashSet ← insertion-ordered
    │   ├── TreeSet       ← sorted (NavigableSet)
    │   └── EnumSet       ← specialized for enums
    └── Queue<T> / Deque<T>
        ├── PriorityQueue ← priority-ordered
        ├── ArrayDeque    ← double-ended queue (stack/queue)
        └── LinkedList    ← also implements Queue/Deque

Map<K,V>                  ← key-value pairs (not Collection)
├── HashMap               ← unordered, O(1) operations
├── LinkedHashMap          ← insertion or access order
├── TreeMap                ← sorted by keys (NavigableMap)
├── EnumMap                ← specialized for enum keys
├── ConcurrentHashMap      ← concurrent, high-performance
└── WeakHashMap            ← keys are weakly referenced
```

**Immutable Collections (Java 9+):**
```java
List<String> list = List.of("a", "b", "c");           // unmodifiable
Set<Integer> set = Set.of(1, 2, 3);                    // unmodifiable
Map<String, Integer> map = Map.of("a", 1, "b", 2);    // unmodifiable
List<String> copy = List.copyOf(mutableList);           // defensive copy

// Sequenced Collections (Java 21+)
SequencedCollection<E> — addFirst/Last, getFirst/Last, reversed()
SequencedSet<E>        — extends SequencedCollection
SequencedMap<K,V>      — firstEntry(), lastEntry(), reversed()
```

**Lambda-enhanced Map operations (Java 8+):**
```java
map.compute("key", (k, v) -> v == null ? 1 : v + 1);
map.computeIfAbsent("key", k -> expensiveComputation(k));
map.computeIfPresent("key", (k, v) -> v + 1);
map.merge("key", 1, Integer::sum);
map.getOrDefault("key", 0);
map.putIfAbsent("key", value);
map.replaceAll((k, v) -> v.toUpperCase());
```

---

## 6. I/O API

> See also: `oracle-tutorials-guide.md` § I/O API for the full tutorial breakdown.

**Quick Reference:**

| Task | Modern Java Approach |
|---|---|
| Read text file | `Files.readString(Path.of("file.txt"))` (Java 11+) |
| Read all lines | `Files.readAllLines(Path.of("file.txt"))` |
| Stream lines | `Files.lines(Path.of("file.txt"))` — lazy, use try-with-resources |
| Write text file | `Files.writeString(Path.of("file.txt"), content)` (Java 11+) |
| Write lines | `Files.write(path, lines)` |
| Read bytes | `Files.readAllBytes(Path.of("file.bin"))` |
| Copy file | `Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING)` |
| Move file | `Files.move(source, target, StandardCopyOption.ATOMIC_MOVE)` |
| Delete file | `Files.delete(path)` or `Files.deleteIfExists(path)` |
| Walk directory tree | `Files.walk(Path.of("dir"))` |
| Find files | `Files.find(startDir, maxDepth, matcher)` |
| File attributes | `Files.getLastModifiedTime()`, `Files.size()`, `Files.isReadable()` |
| Temp file | `Files.createTempFile(prefix, suffix)` |

```java
// Modern pattern: try-with-resources for stream-based I/O
try (Stream<String> lines = Files.lines(Path.of("data.txt"))) {
    long count = lines.filter(line -> line.contains("ERROR")).count();
}

// Buffered reading (large files)
try (BufferedReader reader = Files.newBufferedReader(Path.of("large.txt"))) {
    String line;
    while ((line = reader.readLine()) != null) {
        process(line);
    }
}
```

---

## 7. Date-Time API (java.time)

> See also: `oracle-tutorials-guide.md` § Date Time API for the full 13-part tutorial breakdown.

**Quick Reference:**

| Class | What It Represents | Example |
|---|---|---|
| `LocalDate` | Date without time | `2026-02-18` |
| `LocalTime` | Time without date | `14:30:00` |
| `LocalDateTime` | Date + time, no timezone | `2026-02-18T14:30:00` |
| `ZonedDateTime` | Date + time + timezone | `2026-02-18T14:30:00+05:30[Asia/Kolkata]` |
| `OffsetDateTime` | Date + time + UTC offset | `2026-02-18T14:30:00+05:30` |
| `Instant` | Machine timestamp (UTC) | `2026-02-18T09:00:00Z` |
| `Duration` | Time-based amount | `PT2H30M` (2 hours 30 minutes) |
| `Period` | Date-based amount | `P1Y2M3D` (1 year 2 months 3 days) |
| `DateTimeFormatter` | Parsing/formatting | Custom patterns |

```java
// Creating
LocalDate today = LocalDate.now();
LocalDate specific = LocalDate.of(2026, Month.FEBRUARY, 18);
Instant now = Instant.now();

// Arithmetic
LocalDate nextWeek = today.plusWeeks(1);
LocalDate lastYear = today.minusYears(1);
Duration between = Duration.between(start, end);
long days = ChronoUnit.DAYS.between(date1, date2);

// Formatting
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
String formatted = dateTime.format(formatter);
LocalDateTime parsed = LocalDateTime.parse("18/02/2026 14:30", formatter);

// Converting legacy ↔ modern
Instant instant = legacyDate.toInstant();
Date legacyDate = Date.from(instant);
```

---

## 8. Other Essential JDK APIs

### Reflection API (`java.lang.reflect`)

| Class | Purpose |
|---|---|
| `Class<T>` | Represents a class at runtime |
| `Method` | Represents a method |
| `Field` | Represents a field |
| `Constructor<T>` | Represents a constructor |

```java
Class<?> cls = obj.getClass();
Method[] methods = cls.getDeclaredMethods();
Field field = cls.getDeclaredField("name");
field.setAccessible(true);
Object value = field.get(obj);
```

### Method Handles (`java.lang.invoke`) — Modern alternative to reflection

```java
MethodHandles.Lookup lookup = MethodHandles.lookup();
MethodHandle handle = lookup.findVirtual(String.class, "length", MethodType.methodType(int.class));
int length = (int) handle.invoke("hello"); // 5
```

### Regular Expressions (`java.util.regex`)

```java
Pattern pattern = Pattern.compile("\\d{3}-\\d{4}");
Matcher matcher = pattern.matcher("Call 555-1234");
if (matcher.find()) {
    System.out.println(matcher.group()); // 555-1234
}

// Predicate for streams
Pattern.compile("[A-Z].*").asPredicate();
names.stream().filter(Pattern.compile("^A").asPredicate()).toList();
```

### Foreign Function & Memory API (Java 22+, final)

```java
// Call native C functions without JNI
try (Arena arena = Arena.ofConfined()) {
    MemorySegment cString = arena.allocateFrom("hello");
    long length = strlen.invokeExact(cString); // call C strlen()
}
```

### HTTP Client API (Java 11+)

```java
HttpClient client = HttpClient.newHttpClient();
HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("https://api.example.com/data"))
    .header("Accept", "application/json")
    .GET()
    .build();

HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
System.out.println(response.statusCode());
System.out.println(response.body());

// Async
CompletableFuture<HttpResponse<String>> future =
    client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
```

### Process API (Java 9+)

```java
ProcessHandle current = ProcessHandle.current();
System.out.println("PID: " + current.pid());

ProcessHandle.allProcesses()
    .filter(ph -> ph.info().command().isPresent())
    .forEach(ph -> System.out.println(ph.pid() + ": " + ph.info().command().get()));
```

### Modules (Java 9+)

> See `oracle-tutorials-guide.md` § Modules for the full 12-part tutorial breakdown.

```java
// module-info.java
module com.myapp {
    requires java.sql;               // dependency
    requires transitive java.logging; // transitive dependency
    exports com.myapp.api;           // make public API available
    opens com.myapp.internal to       // allow reflection from specific module
        com.google.guice;
    provides com.myapp.spi.Service   // service provider
        with com.myapp.impl.ServiceImpl;
    uses com.myapp.spi.Plugin;       // service consumer
}
```

### Garbage Collection Overview

| Collector | Best For | Pause Goals |
|---|---|---|
| **G1 GC** (default since JDK 9) | General purpose | Configurable pause target |
| **ZGC** (production since JDK 15) | Low latency | Sub-millisecond pauses |
| **Shenandoah** | Low latency | Concurrent compaction |
| **Parallel GC** | Throughput | Higher pauses, maximum throughput |
| **Serial GC** | Small heaps, single-core | Simple, predictable |

```bash
# JVM flags
-XX:+UseG1GC              # default
-XX:+UseZGC               # low latency
-XX:+UseShenandoahGC      # low latency (OpenJDK)
-XX:MaxGCPauseMillis=200  # G1 pause target
-Xms512m -Xmx4g           # heap size
```

### JDK Flight Recorder (JFR)

```bash
# Start recording
java -XX:StartFlightRecording=duration=60s,filename=recording.jfr MyApp

# Continuous recording
java -XX:StartFlightRecording=settings=default MyApp
```

```java
// Programmatic control
try (var recording = new Recording()) {
    recording.start();
    // ... application code ...
    recording.stop();
    recording.dump(Path.of("recording.jfr"));
}
```
