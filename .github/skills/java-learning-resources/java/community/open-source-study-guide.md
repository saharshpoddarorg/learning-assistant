# Open Source Java Projects — Study Guide & Code Patterns

> **Purpose:** Self-contained reference for studying open-source Java projects.
> Use this instead of individually browsing GitHub repositories.

---

## 1. How to Study an Open-Source Project (Methodology)

### Step-by-Step Approach

1. **Read the README** — understand purpose, architecture, getting started
2. **Study the package structure** — how is responsibility organized?
3. **Read the public API** — how do consumers use this library?
4. **Read the tests** — tests show intended behavior, usage patterns, edge cases
5. **Trace an entry point** — pick one feature and follow the execution path
6. **Study the build system** — understand dependencies, plugins, configuration
7. **Read CONTRIBUTING.md** — understand project conventions, code style
8. **Pick a recent PR** — see how code changes are made and reviewed
9. **Check `good first issue` labels** — understand real-world bugs and improvements

### What to Look For

| Aspect | Questions to Ask |
|---|---|
| **Architecture** | What patterns are used? How are layers separated? |
| **Error Handling** | Custom exceptions? Validation strategy? |
| **Testing** | Unit vs integration? Mock strategy? Assertion style? |
| **API Design** | Builder pattern? Fluent APIs? Factory methods? |
| **Concurrency** | Thread safety approach? Immutability? Locking strategy? |
| **Configuration** | How is the project configured? Properties? Annotations? |

---

## 2. Project Deep Dives

### Google Guava (`google/guava`)

**What it is:** Core Java utility library by Google.
**Why study it:** Excellent API design, immutable collections, clean code.

**Key Packages & Patterns:**

| Package | What It Provides | Design Patterns |
|---|---|---|
| `com.google.common.collect` | Immutable collections, Multimap, BiMap, Table | Immutability, Builder |
| `com.google.common.base` | Preconditions, Optional (pre-Java 8), Strings | Guard clauses, Null safety |
| `com.google.common.cache` | Loading cache, eviction policies | Cache-aside, Decorator |
| `com.google.common.io` | I/O utilities, ByteSource, CharSource | Template Method |
| `com.google.common.util.concurrent` | ListenableFuture, RateLimiter | Promise, Token bucket |
| `com.google.common.hash` | Hashing utilities, BloomFilter | Strategy |
| `com.google.common.eventbus` | Pub-sub event system | Observer/Publish-Subscribe |

**Code Patterns to Study:**

```java
// Preconditions (guard clauses) — from com.google.common.base
public void process(String name, int count) {
    checkNotNull(name, "name must not be null");
    checkArgument(count > 0, "count must be positive, got: %s", count);
}

// Immutable collections — from com.google.common.collect
ImmutableList<String> names = ImmutableList.of("Alice", "Bob", "Charlie");
ImmutableMap<String, Integer> scores = ImmutableMap.of("Alice", 95, "Bob", 87);

// Multimap — one key → multiple values
Multimap<String, String> tags = HashMultimap.create();
tags.put("java", "language");
tags.put("java", "platform");
Collection<String> javaTags = tags.get("java"); // ["language", "platform"]

// LoadingCache — automatic population
LoadingCache<Key, Graph> cache = CacheBuilder.newBuilder()
    .maximumSize(1000)
    .expireAfterWrite(10, TimeUnit.MINUTES)
    .build(new CacheLoader<Key, Graph>() {
        public Graph load(Key key) { return createGraph(key); }
    });

// RateLimiter — controlling throughput
RateLimiter limiter = RateLimiter.create(5.0); // 5 permits per second
limiter.acquire(); // blocks until permit available
```

### JUnit 5 (`junit-team/junit5`)

**What it is:** The standard Java testing framework.
**Why study it:** Extension model, annotation processing, SPI usage.

**Architecture:**

| Module | Purpose |
|---|---|
| `junit-jupiter-api` | Annotations (`@Test`, `@BeforeEach`, etc.) and assertions |
| `junit-jupiter-engine` | Test discovery and execution engine |
| `junit-jupiter-params` | Parameterized test support |
| `junit-platform-launcher` | API for launching tests (IDE/build tool integration) |
| `junit-platform-engine` | SPI for test engine implementations |

**Key Patterns:**

```java
// Basic test lifecycle
@BeforeAll    // Once before all tests
@BeforeEach   // Before each test
@Test         // The test method
@AfterEach    // After each test
@AfterAll     // Once after all tests

// Assertions
assertEquals(expected, actual);
assertNotNull(object);
assertThrows(IllegalArgumentException.class, () -> method(-1));
assertAll(
    () -> assertEquals("Jane", person.getFirstName()),
    () -> assertEquals("Doe", person.getLastName())
);

// Parameterized tests
@ParameterizedTest
@ValueSource(strings = {"racecar", "radar", "level"})
void isPalindrome(String word) { assertTrue(StringUtils.isPalindrome(word)); }

@ParameterizedTest
@CsvSource({"1, 1, 2", "2, 3, 5", "10, 20, 30"})
void add(int a, int b, int expected) { assertEquals(expected, a + b); }

// Extensions (custom behavior)
@ExtendWith(MockitoExtension.class)
class MyServiceTest { ... }
```

### Spring Boot Samples (`spring-projects/spring-boot`)

**What it is:** Industry-standard Java framework for building applications.
**Why study it:** DI, AOP, REST, configuration patterns.

**Key Concepts to Study:**

| Concept | Where to Look | Pattern |
|---|---|---|
| Dependency Injection | `@Component`, `@Service`, `@Repository`, `@Controller` | Inversion of Control |
| Configuration | `@Configuration`, `@Bean`, `@Value`, `application.properties` | Factory Method |
| REST Controllers | `@RestController`, `@GetMapping`, `@PostMapping` | Front Controller |
| AOP | `@Aspect`, `@Before`, `@After`, `@Around` | Proxy, Decorator |
| Data Access | `@Repository`, `JpaRepository` | Repository Pattern |
| Validation | `@Valid`, `@NotNull`, `@Size` | Specification |
| Auto-configuration | `@ConditionalOnClass`, `@ConditionalOnProperty` | Convention over Configuration |

### Caffeine (`ben-manes/caffeine`)

**What it is:** High-performance caching library for Java.
**Why study it:** Concurrency patterns, eviction algorithms, weak references.

**Key Design Elements:**

| Element | What to Study |
|---|---|
| **W-TinyLfu eviction** | Near-optimal hit rate eviction policy |
| **Concurrent data structures** | Lock-free operations, CAS (compare-and-swap) |
| **Builder pattern** | Fluent cache configuration API |
| **Weak/Soft references** | Memory-sensitive caching |
| **Async loading** | `AsyncLoadingCache` with `CompletableFuture` |
| **Statistics** | Cache hit/miss rate monitoring |

```java
// Basic usage
Cache<Key, Graph> cache = Caffeine.newBuilder()
    .maximumSize(10_000)
    .expireAfterWrite(Duration.ofMinutes(5))
    .build();

// Loading cache (auto-populate)
LoadingCache<Key, Graph> cache = Caffeine.newBuilder()
    .maximumSize(10_000)
    .expireAfterWrite(Duration.ofMinutes(5))
    .build(key -> createExpensiveGraph(key));

Graph graph = cache.get(key); // loads if absent

// Async loading cache
AsyncLoadingCache<Key, Graph> cache = Caffeine.newBuilder()
    .maximumSize(10_000)
    .buildAsync(key -> createExpensiveGraph(key));

CompletableFuture<Graph> future = cache.get(key);
```

### Jackson (`FasterXML/jackson-core`)

**What it is:** JSON serialization/deserialization library.
**Why study it:** Streaming API, builder pattern, generics.

**Architecture:**

| Module | Purpose |
|---|---|
| `jackson-core` | Streaming API (`JsonParser`, `JsonGenerator`) |
| `jackson-databind` | Object mapping (`ObjectMapper`) |
| `jackson-annotations` | `@JsonProperty`, `@JsonIgnore`, etc. |

**Key Patterns:**

```java
// ObjectMapper — main entry point
ObjectMapper mapper = new ObjectMapper();

// Serialization
String json = mapper.writeValueAsString(myObject);

// Deserialization
MyClass obj = mapper.readValue(json, MyClass.class);

// Configuration via annotations
public class User {
    @JsonProperty("user_name")
    private String name;

    @JsonIgnore
    private String password;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
}

// Builder pattern for ObjectMapper configuration
ObjectMapper mapper = JsonMapper.builder()
    .enable(SerializationFeature.INDENT_OUTPUT)
    .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    .addModule(new JavaTimeModule())
    .build();
```

### Resilience4j (`resilience4j/resilience4j`)

**What it is:** Lightweight fault tolerance library.
**Why study it:** Decorator pattern, functional programming, resilience patterns.

**Key Modules:**

| Module | Pattern | What It Does |
|---|---|---|
| `CircuitBreaker` | Circuit Breaker | Prevents calls to failing services |
| `RateLimiter` | Rate Limiter | Limits call frequency |
| `Retry` | Retry | Retries failed operations |
| `Bulkhead` | Bulkhead | Limits concurrent calls |
| `TimeLimiter` | Timeout | Limits execution time |

```java
// Circuit Breaker
CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("backend");
Supplier<String> decoratedSupplier = CircuitBreaker
    .decorateSupplier(circuitBreaker, () -> backendService.call());
Try<String> result = Try.ofSupplier(decoratedSupplier);

// Retry
Retry retry = Retry.ofDefaults("backend");
Supplier<String> retrySupplier = Retry
    .decorateSupplier(retry, () -> backendService.call());

// Combining decorators (Decorator pattern)
Supplier<String> decoratedSupplier = Decorators.ofSupplier(() -> backendService.call())
    .withCircuitBreaker(circuitBreaker)
    .withRetry(retry)
    .withRateLimiter(rateLimiter)
    .decorate();
```

### Javalin (`javalin/javalin`)

**What it is:** Lightweight web framework for Java and Kotlin.
**Why study it:** Small codebase, modern Java, functional style.

**Key Concepts:**

```java
// Minimal web server
Javalin app = Javalin.create().start(7070);
app.get("/", ctx -> ctx.result("Hello World"));

// REST API
app.get("/users", UserController::getAll);
app.get("/users/{id}", UserController::getOne);
app.post("/users", UserController::create);
app.put("/users/{id}", UserController::update);
app.delete("/users/{id}", UserController::delete);

// Context pattern — access request/response
app.get("/hello/{name}", ctx -> {
    String name = ctx.pathParam("name");
    String greeting = ctx.queryParam("greeting", "Hello");
    ctx.json(Map.of("message", greeting + " " + name));
});
```

### MapStruct (`mapstruct/mapstruct`)

**What it is:** Compile-time code generator for mappings between Java beans.
**Why study it:** Annotation processing, APT, code generation.

**Key Concepts:**

```java
// Define a mapper interface
@Mapper
public interface CarMapper {
    CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

    @Mapping(source = "numberOfSeats", target = "seatCount")
    CarDto carToCarDto(Car car);
}

// Usage
CarDto dto = CarMapper.INSTANCE.carToCarDto(car);
// MapStruct generates the implementation at compile time
```

---

## 3. Design Patterns You'll Find in These Projects

| Pattern | Where You'll See It | Example |
|---|---|---|
| **Builder** | Guava caches, Jackson ObjectMapper, Caffeine | Fluent configuration APIs |
| **Factory Method** | Spring `@Bean`, JUnit engine discovery | Object creation delegation |
| **Strategy** | Guava hash functions, Resilience4j policies | Interchangeable algorithms |
| **Decorator** | Resilience4j, Jackson modules, Stream wrappers | Adding behavior dynamically |
| **Observer** | Guava EventBus, Spring events | Event notification |
| **Template Method** | JUnit lifecycle, Spring `JdbcTemplate` | Algorithm skeleton with hooks |
| **Proxy** | Spring AOP, Mockito mocks | Controlled access to objects |
| **Adapter** | Jackson serializers, MapStruct mappers | Interface conversion |
| **Repository** | Spring Data | Data access abstraction |
| **Chain of Responsibility** | Servlet filters, Spring interceptors | Sequential processing |
| **Immutability** | Guava collections, Records | Thread safety via immutability |

---

## 4. Starter Path for Each Project

### If you have 1 hour:
1. **Javalin** — Read the README, build a hello-world app, trace the request handling

### If you have 2-3 hours:
2. **Caffeine** — Read the README, study the `CacheBuilder` API, write a caching example
3. **JUnit 5** — Read `junit-jupiter-api` annotations, write parameterized tests

### If you have a day:
4. **Guava** — Study `ImmutableList`, `Preconditions`, `LoadingCache`
5. **Jackson** — Trace how `ObjectMapper.readValue()` works internally
6. **Resilience4j** — Build a circuit breaker around an HTTP call

### For ongoing study:
7. **Spring Boot** — Follow the guides at `spring.io/guides`, study auto-configuration
8. **MapStruct** — Study the annotation processor to understand compile-time code generation
