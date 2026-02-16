---
name: software-engineering-resources
description: >
  Comprehensive curated index of software engineering learning resources — not restricted to any language.
  Covers OOP, OS, networking, protocols (TCP/UDP/HTTP/gRPC/REST/RPC), DSA (data structures, algorithms, patterns, interview prep),
  system design (HLD/LLD), DBMS, multithreading/concurrency, testing (TDD/BDD/ATDD, unit/integration/e2e),
  SDLC methodologies (Agile/Scrum/Kanban/XP/SAFe), design patterns, SOLID, clean code, refactoring, architecture,
  build tools (Maven, Gradle, Ant, Bazel), version control (Git commands, branching strategies, Git Flow, trunk-based),
  DevOps & CI/CD (Jenkins, GitHub Actions, GitLab CI, deployment strategies, GitOps, feature flags),
  containers & orchestration (Docker, Kubernetes, Docker Compose, Dockerfile best practices),
  infrastructure as code (Terraform, Ansible), cloud platforms (AWS, GCP, Azure), monitoring & observability,
  distributed systems (replication, consensus, consistency models, CAP theorem),
  industry-used concepts & real-world systems (rate limiting, circuit breakers, event-driven architecture,
  microservices patterns, Twelve-Factor App, Netflix/Google/Amazon/Uber/Spotify engineering practices),
  security (OWASP, zero trust, supply chain, secrets management),
  tech trends 2025-2026 (AI coding assistants, agentic AI, platform engineering, Rust, WebAssembly, eBPF,
  vector databases, data lakehouse, cell-based architecture, edge computing),
  and general computer science fundamentals.
  Use when asked about learning resources, book recommendations, tutorials, documentation, interview preparation,
  CS fundamentals, engineering concepts, development methodologies, build tools, Git, DevOps, CI/CD,
  industry practices, tech trends, security, observability, or when building a study plan for any SE topic.
---

# Software Engineering Learning Resources — Comprehensive Index

## Foundational Books (The Canon)

### Clean Code & Design
| Book | Author | Core Topics |
|---|---|---|
| **Clean Code** | Robert C. Martin | Naming, functions, comments, formatting, error handling, unit tests |
| **The Clean Coder** | Robert C. Martin | Professionalism, time management, estimation, TDD discipline |
| **Clean Architecture** | Robert C. Martin | Component principles, architecture patterns, boundaries, frameworks |
| **Refactoring (2nd Ed.)** | Martin Fowler | Systematic code improvement, catalog of refactorings, code smells |
| **Design Patterns (GoF)** | Gamma, Helm, Johnson, Vlissides | 23 classic OOP patterns: creational, structural, behavioral |
| **Head First Design Patterns** | Freeman & Robson | Visual, beginner-friendly design patterns with analogies |
| **Patterns of Enterprise Application Architecture** | Martin Fowler | Domain model, data mapper, unit of work, repository, service layer |
| **Working Effectively with Legacy Code** | Michael Feathers | Seams, characterization tests, breaking dependencies safely |

### Software Engineering Practices
| Book | Author | Core Topics |
|---|---|---|
| **The Pragmatic Programmer** | Hunt & Thomas | Tips, DRY, orthogonality, tracer bullets, pragmatic thinking |
| **Code Complete (2nd Ed.)** | Steve McConnell | Construction practices, variables, control flow, code quality |
| **A Philosophy of Software Design** | John Ousterhout | Complexity, deep vs shallow modules, information hiding |
| **Domain-Driven Design** | Eric Evans | Ubiquitous language, bounded contexts, aggregates, value objects |
| **Implementing Domain-Driven Design** | Vaughn Vernon | Practical DDD with real code examples |
| **Release It! (2nd Ed.)** | Michael Nygard | Stability patterns, circuit breakers, bulkheads, production readiness |

### Testing
| Book | Author | Core Topics |
|---|---|---|
| **Test Driven Development: By Example** | Kent Beck | Red-green-refactor, TDD rhythms, money example |
| **Growing Object-Oriented Software, Guided by Tests** | Freeman & Pryce | Outside-in TDD, mocking, test doubles |
| **xUnit Test Patterns** | Gerard Meszaros | Test smells, fixtures, result verification, test organization |
| **The Art of Unit Testing (3rd Ed.)** | Roy Osherove | Isolation frameworks, trustworthy tests, maintainability |
| **BDD in Action** | John Ferguson Smart | Behavior-driven development, Gherkin, living documentation |

### Algorithms & Data Structures
| Book | Author | Core Topics |
|---|---|---|
| **Introduction to Algorithms (CLRS)** | Cormen, Leiserson, Rivest, Stein | Comprehensive: sorting, graphs, DP, greedy, NP |
| **Algorithm Design Manual** | Steven Skiena | Practical algorithm design, war stories, catalog |
| **Grokking Algorithms** | Aditya Bhargava | Visual, beginner-friendly: BFS, DP, greedy, hash tables |
| **Cracking the Coding Interview** | Gayle Laakmann McDowell | Interview patterns, 189 problems, data structures review |
| **Elements of Programming Interviews** | Aziz, Lee, Prakash | Advanced interview problems with detailed solutions |

### System Design
| Book | Author | Core Topics |
|---|---|---|
| **Designing Data-Intensive Applications (DDIA)** | Martin Kleppmann | Storage, replication, partitioning, consistency, stream processing |
| **System Design Interview (Vol 1 & 2)** | Alex Xu | Step-by-step system designs, scale estimation, trade-offs |
| **Building Microservices (2nd Ed.)** | Sam Newman | Service decomposition, communication, deployment, testing |
| **Software Architecture: The Hard Parts** | Ford, Richards, Sadalage, Dehghani | Architecture decisions, trade-off analysis, modularity |

### Operating Systems & Networking
| Book | Author | Core Topics |
|---|---|---|
| **Operating System Concepts (Silberschatz)** | Silberschatz, Galvin, Gagne | Processes, threads, scheduling, memory, file systems |
| **Modern Operating Systems** | Andrew Tanenbaum | Process management, deadlocks, I/O, virtualization |
| **Computer Networking: A Top-Down Approach** | Kurose & Ross | Application → transport → network → link layers |
| **TCP/IP Illustrated (Vol 1)** | W. Richard Stevens | TCP, UDP, IP, ICMP, routing — packet-level detail |
| **High Performance Browser Networking** | Ilya Grigorik | HTTP/2, WebSocket, TLS, TCP optimization (free online) |

### Databases
| Book | Author | Core Topics |
|---|---|---|
| **Database Internals** | Alex Petrov | Storage engines, B-trees, LSM, distributed databases |
| **SQL Performance Explained** | Markus Winand | Indexes, execution plans, joins, query optimization |
| **Designing Data-Intensive Applications** | Martin Kleppmann | (also covers distributed data, consistency, transactions) |

### Concurrency & Multithreading
| Book | Author | Core Topics |
|---|---|---|
| **Java Concurrency in Practice** | Brian Goetz | Thread safety, synchronization, concurrent collections, executors |
| **The Art of Multiprocessor Programming** | Herlihy & Shavit | Lock-free algorithms, concurrent data structures, linearizability |
| **Seven Concurrency Models in Seven Weeks** | Paul Butcher | Threads, STM, actors, CSP, data parallelism, GPU, lambda architecture |

---

## Online Resources by Domain

### Object-Oriented Programming (OOP)
| Resource | URL | Best For |
|---|---|---|
| **Refactoring.Guru — OOP Basics** | `https://refactoring.guru/oop-basics` | Visual OOP concepts |
| **Refactoring.Guru — Design Patterns** | `https://refactoring.guru/design-patterns` | Interactive pattern catalog |
| **SOLID Principles (Baeldung)** | `https://www.baeldung.com/solid-principles` | Practical SOLID with Java |
| **SourceMaking** | `https://sourcemaking.com/` | Patterns, anti-patterns, refactoring |
| **Wikipedia — OOP** | `https://en.wikipedia.org/wiki/Object-oriented_programming` | Conceptual overview, history |

### Data Structures & Algorithms (DSA)

#### Online Resources
| Resource | URL | Best For |
|---|---|---|
| **VisuAlgo** | `https://visualgo.net/` | Animated algorithm visualizations |
| **Big-O Cheat Sheet** | `https://www.bigocheatsheet.com/` | Time/space complexity reference |
| **NeetCode** | `https://neetcode.io/` | Curated LeetCode roadmap, pattern-based |
| **LeetCode** | `https://leetcode.com/` | Coding problems, company tags |
| **HackerRank** | `https://www.hackerrank.com/` | Skill-based challenges |
| **GeeksforGeeks DSA** | `https://www.geeksforgeeks.org/data-structures/` | In-depth articles per structure |
| **Blind 75** | `https://neetcode.io/practice` | Core 75 problems covering all patterns |
| **AlgoExpert** | `https://www.algoexpert.io/` | Video explanations + coding |
| **CP-Algorithms** | `https://cp-algorithms.com/` | Competitive programming algorithms |
| **MIT OpenCourseWare 6.006** | `https://ocw.mit.edu/courses/6-006-introduction-to-algorithms-spring-2020/` | Academic algorithms course (free videos + notes) |
| **Stanford Algorithms (Coursera)** | `https://www.coursera.org/specializations/algorithms` | Tim Roughgarden's algorithms specialization |

#### Data Structures — Time & Space Complexity

##### Core Data Structures at a Glance
| Data Structure | Access | Search | Insert | Delete | Space | Notes |
|---|---|---|---|---|---|---|
| **Array** | O(1) | O(n) | O(n) | O(n) | O(n) | Contiguous memory; O(1) append if not full |
| **Dynamic Array** (ArrayList) | O(1) | O(n) | O(1)* | O(n) | O(n) | *Amortized O(1) append; O(n) on resize |
| **Singly Linked List** | O(n) | O(n) | O(1)† | O(1)† | O(n) | †If at known position; O(n) to find |
| **Doubly Linked List** | O(n) | O(n) | O(1)† | O(1)† | O(n) | Bidirectional traversal |
| **Stack** | O(n) | O(n) | O(1) | O(1) | O(n) | LIFO — push/pop at top |
| **Queue** | O(n) | O(n) | O(1) | O(1) | O(n) | FIFO — enqueue/dequeue |
| **Deque** | O(n) | O(n) | O(1) | O(1) | O(n) | Insert/remove both ends |
| **HashMap / HashSet** | — | O(1)* | O(1)* | O(1)* | O(n) | *Average; O(n) worst case (collisions) |
| **TreeMap / TreeSet (BST)** | O(log n) | O(log n) | O(log n) | O(log n) | O(n) | Sorted order guaranteed |
| **AVL Tree** | O(log n) | O(log n) | O(log n) | O(log n) | O(n) | Strictly balanced (height diff ≤ 1) |
| **Red-Black Tree** | O(log n) | O(log n) | O(log n) | O(log n) | O(n) | Relaxed balance; used in Java TreeMap |
| **B-Tree / B+ Tree** | O(log n) | O(log n) | O(log n) | O(log n) | O(n) | Disk-optimized; used in databases |
| **Min/Max Heap** | O(1)‡ | O(n) | O(log n) | O(log n) | O(n) | ‡O(1) for min/max; O(log n) extract |
| **Trie** | O(m) | O(m) | O(m) | O(m) | O(Σ·m) | m = key length; Σ = alphabet size |
| **Segment Tree** | O(log n) | O(log n) | O(log n) | — | O(n) | Range queries + point updates |
| **Fenwick Tree (BIT)** | O(log n) | O(log n) | O(log n) | — | O(n) | Prefix sums; less memory than seg tree |
| **Union-Find (DSU)** | — | O(α(n))* | O(α(n))* | — | O(n) | *Near O(1) with path compression + rank |
| **Bloom Filter** | — | O(k) | O(k) | — | O(m) | Probabilistic; false positives possible |
| **Skip List** | O(log n) | O(log n) | O(log n) | O(log n) | O(n log n) | Probabilistic balanced list; used in Redis |

##### Hash Collision Strategies
| Strategy | How It Works | Avg Lookup | Worst Lookup | Used In |
|---|---|---|---|---|
| **Separate Chaining** | Each bucket is a linked list (or tree at threshold) | O(1) | O(n) | Java HashMap (list → tree at 8) |
| **Open Addressing — Linear Probing** | Try next slot linearly | O(1) | O(n) | Python dict (variant), Go map |
| **Open Addressing — Quadratic Probing** | Try i², skip quadratically | O(1) | O(n) | Some C++ implementations |
| **Open Addressing — Double Hashing** | Use second hash for step size | O(1) | O(n) | Academic, some production systems |
| **Robin Hood Hashing** | Steal from rich (low displacement) | O(1) | O(log n) | Rust HashMap |
| **Cuckoo Hashing** | Two hash functions, displacing on collision | O(1) | O(1) amortized | Network hardware, MemC3 |

**Industry note:** Java's `HashMap` switches from linked list to red-black tree per bucket when a bucket reaches 8 entries (treeify threshold), giving O(log n) worst-case per-bucket lookup instead of O(n).

##### Trees — Deep Dive with Industry Use
| Tree Type | Balance | Typical Use | Industry Examples |
|---|---|---|---|
| **BST** | None (can degrade to O(n)) | Teaching, simple ordered data | Rarely used raw in production |
| **AVL Tree** | Strict (height ≤ 1 diff) | Read-heavy, in-memory lookups | C++ std::map (some impls) |
| **Red-Black Tree** | Relaxed (2x height guarantee) | General-purpose balanced map | Java TreeMap/TreeSet, Linux CFS scheduler, C++ std::map |
| **B-Tree** | M-way, minimizes disk I/O | Database indexes, file systems | PostgreSQL, MySQL InnoDB, NTFS, ext4 |
| **B+ Tree** | Leaf-linked B-Tree variant | Range scans on disk | Almost all RDBMS indexes, SQLite |
| **Trie** | N/A (prefix structure) | Autocomplete, spell check, IP routing | Google search suggestions, DNS lookups |
| **Segment Tree** | Perfect binary tree | Range queries (min/max/sum) | Competitive programming, interval scheduling |
| **Merkle Tree** | Hash tree | Data integrity verification | Git, blockchain, Cassandra anti-entropy |
| **LSM Tree** | Log-Structured Merge | Write-heavy workloads | Cassandra, RocksDB, LevelDB, HBase |
| **R-Tree** | Spatial indexing | Geospatial queries | PostGIS, MongoDB geospatial, Google Maps |

##### Heaps — Applications & Patterns
| Application | Data Structure | Complexity | Industry Use |
|---|---|---|---|
| **Top-K elements** | Min-heap of size K | O(n log K) | Twitter trending topics, search ranking |
| **K-th largest/smallest** | Min/max heap or quickselect | O(n) avg with quickselect | Analytics dashboards |
| **Running median** | Two heaps (max + min) | O(log n) per element | Real-time sensor data, streaming analytics |
| **Priority scheduling** | Min-heap (by priority/deadline) | O(log n) insert/extract | OS process scheduler, K8s pod scheduling |
| **Merge K sorted lists** | Min-heap of size K | O(N log K) | Database merge sort, multi-way external sort |
| **Dijkstra's algorithm** | Min-heap (priority queue) | O((V+E) log V) | Google Maps, network routing protocols |
| **Event-driven simulation** | Min-heap (by time) | O(log n) | Discrete event simulation, game loops |
| **Huffman coding** | Min-heap (by frequency) | O(n log n) | ZIP compression, JPEG, HTTP/2 HPACK |

#### Algorithms — Time & Space Complexity

##### Sorting Algorithms
| Algorithm | Best | Average | Worst | Space | Stable? | In-Place? | Industry Notes |
|---|---|---|---|---|---|---|---|
| **Bubble Sort** | O(n) | O(n²) | O(n²) | O(1) | ✅ | ✅ | Teaching only; never in production |
| **Selection Sort** | O(n²) | O(n²) | O(n²) | O(1) | ❌ | ✅ | Minimizes swaps; embedded systems with limited writes |
| **Insertion Sort** | O(n) | O(n²) | O(n²) | O(1) | ✅ | ✅ | Fast for small/nearly-sorted; used as base in Timsort |
| **Merge Sort** | O(n log n) | O(n log n) | O(n log n) | O(n) | ✅ | ❌ | Java `Arrays.sort()` for objects; external sort for large files |
| **Quick Sort** | O(n log n) | O(n log n) | O(n²) | O(log n) | ❌ | ✅ | C `qsort`, most general-purpose sorts; median-of-3 avoids worst case |
| **Heap Sort** | O(n log n) | O(n log n) | O(n log n) | O(1) | ❌ | ✅ | Linux kernel sort; guaranteed O(n log n) in-place |
| **Tim Sort** | O(n) | O(n log n) | O(n log n) | O(n) | ✅ | ❌ | Python `sorted()`, Java `Arrays.sort()`, Rust `sort()` — hybrid merge/insertion |
| **Counting Sort** | O(n+k) | O(n+k) | O(n+k) | O(k) | ✅ | ❌ | k = range; useful for small range integers (e.g., sorting exam scores) |
| **Radix Sort** | O(d·(n+k)) | O(d·(n+k)) | O(d·(n+k)) | O(n+k) | ✅ | ❌ | d = digits; sorting strings, IP addresses, fixed-length keys |
| **Bucket Sort** | O(n+k) | O(n+k) | O(n²) | O(n) | ✅ | ❌ | Uniformly distributed floats; MapReduce partitioning |
| **Intro Sort** | O(n log n) | O(n log n) | O(n log n) | O(log n) | ❌ | ✅ | C++ `std::sort` — hybrid quick/heap/insertion |

**Interview tip:** Know that `Arrays.sort()` in Java uses dual-pivot Quicksort for primitives and TimSort for objects. Python uses TimSort. C++ `std::sort` uses IntroSort. Knowing **why** each language chose its default matters in interviews.

##### Searching Algorithms
| Algorithm | Best | Average | Worst | Space | Prerequisite | Industry Use |
|---|---|---|---|---|---|---|
| **Linear Search** | O(1) | O(n) | O(n) | O(1) | None | Small unsorted data, fallback |
| **Binary Search** | O(1) | O(log n) | O(log n) | O(1) | Sorted data | Database index lookup, `Arrays.binarySearch()` |
| **Interpolation Search** | O(1) | O(log log n) | O(n) | O(1) | Sorted, uniform distribution | Phone book lookup, in-memory sorted arrays |
| **Exponential Search** | O(1) | O(log n) | O(log n) | O(1) | Sorted, unbounded | Infinite/streaming sorted data |
| **Hash Lookup** | O(1) | O(1) | O(n) | O(n) | Hash table | `HashMap.get()`, DNS cache, symbol tables |
| **Ternary Search** | O(1) | O(log n) | O(log n) | O(1) | Unimodal function | Function optimization, ML hyperparameter tuning |

**Binary search variants used in industry:**
- **Lower bound / Upper bound:** Database range queries, Java `Collections.binarySearch()`
- **Search on answer space:** Rate limiter tuning, capacity planning, "minimum maximum" problems
- **Rotated sorted array:** Time-series data wrap, circular buffer search

##### Graph Algorithms
| Algorithm | Time | Space | Graph Type | Industry Use |
|---|---|---|---|---|
| **BFS** | O(V+E) | O(V) | Unweighted | Social network "degrees of separation", web crawlers, shortest path |
| **DFS** | O(V+E) | O(V) | Any | Cycle detection, topological sort, maze solving, garbage collection (mark phase) |
| **Dijkstra** | O((V+E) log V) | O(V) | Non-negative weights | Google Maps, network routing (OSPF), GPS navigation |
| **Bellman-Ford** | O(V·E) | O(V) | Any (neg. weights OK) | Arbitrage detection (negative cycles), distance-vector routing (RIP) |
| **Floyd-Warshall** | O(V³) | O(V²) | All-pairs | Small networks, transitive closure, network distance matrices |
| **A*** | O(E) | O(V) | Weighted + heuristic | Game pathfinding, robotics, GPS with traffic |
| **Kruskal** | O(E log E) | O(V) | Undirected, weighted | Network design (min-cost wiring), cluster analysis |
| **Prim** | O((V+E) log V) | O(V) | Undirected, weighted | Dense network MST, image segmentation |
| **Topological Sort** | O(V+E) | O(V) | DAG only | Build systems (Make, Gradle), course prereqs, task scheduling |
| **Tarjan (SCC)** | O(V+E) | O(V) | Directed | Compiler optimization, deadlock detection, social network analysis |
| **Kahn's Algorithm** | O(V+E) | O(V) | DAG (BFS topo sort) | Package managers (npm), CI/CD pipeline ordering |
| **Johnson's** | O(V²·log V + V·E) | O(V²) | Sparse, neg. weights | Sparse graph all-pairs shortest path |

**Interview tip:** Always state the graph representation you're using. Adjacency list gives O(V+E) for BFS/DFS; adjacency matrix gives O(V²). Real systems almost always use adjacency lists (or edge lists for sparse graphs).

##### Dynamic Programming Patterns
| Pattern | Example Problems | Key Insight | Time | Space (optimized) |
|---|---|---|---|---|
| **1D DP** | Climbing Stairs, House Robber, Coin Change | Current state depends on previous 1-2 states | O(n) | O(1) with rolling variables |
| **2D DP** | Longest Common Subsequence, Edit Distance, Grid Paths | Two dimensions of subproblems | O(m·n) | O(min(m,n)) with rolling array |
| **0/1 Knapsack** | Knapsack, Target Sum, Partition Equal Subset | Include or exclude each item | O(n·W) | O(W) with 1D array |
| **Unbounded Knapsack** | Coin Change, Rod Cutting | Items can be reused | O(n·W) | O(W) |
| **Interval DP** | Matrix Chain, Burst Balloons, Palindrome Partitioning | Merge intervals, try all split points | O(n³) | O(n²) |
| **DP on Trees** | Diameter, Max Path Sum, House Robber III | Post-order DFS, return state from children | O(n) | O(h) stack depth |
| **Bitmask DP** | TSP, Assign Tasks, Subset covering | State = bitmask of visited/used elements | O(2ⁿ·n²) | O(2ⁿ·n) |
| **DP on Strings** | Regex Matching, Wildcard, Distinct Subsequences | Character-by-character comparison | O(m·n) | O(n) with rolling |
| **State Machine DP** | Buy/Sell Stock (with cooldown/fee/k txns) | States = holding, not holding, cooldown | O(n·k) | O(k) |
| **Digit DP** | Count numbers with property in range [L, R] | Digit by digit with tight/loose bound | O(d·states) | O(d·states) |

**Optimization techniques:**
- **Memoization → Tabulation:** Memoization = top-down recursion + cache; tabulation = bottom-up iteration. Tabulation avoids stack overflow for large inputs.
- **Rolling array:** If DP[i] depends only on DP[i-1], use 2 rows (or variables) instead of full table. Reduces O(m·n) space to O(n).
- **State reduction:** Identify which states are actually needed. Example: Stock Buy/Sell — only 2-3 states instead of 2D table.

#### Real-World Industry Applications

##### Where Each Data Structure Lives in Production
| Data Structure | Industry System | How It's Used |
|---|---|---|
| **Array / Buffer** | Kafka, network stack | Ring buffers for message partitions, TCP receive buffers |
| **HashMap** | Redis, Memcached, every web server | Cache, session store, request routing table |
| **B+ Tree** | PostgreSQL, MySQL, SQLite | All index lookups, range scans |
| **LSM Tree** | Cassandra, RocksDB, LevelDB | Write-optimized storage (append → merge) |
| **Skip List** | Redis sorted set, LevelDB memtable | Concurrent ordered data without complex tree rotations |
| **Trie** | DNS resolution, autocomplete | IP routing tables, T9 keyboard, search suggest |
| **Bloom Filter** | Cassandra, Chrome safe browsing, Spark | Quickly check "definitely not in set" before expensive lookup |
| **Priority Queue (Heap)** | OS scheduler, Kubernetes, Netty | Thread scheduling, pod priority, event loop |
| **Graph (adjacency list)** | Social networks, Google Maps, package managers | Friend recommendations, routing, dependency resolution |
| **Merkle Tree** | Git, blockchain, Cassandra | Data integrity verification, efficient sync |
| **HyperLogLog** | Redis, Google Analytics | Approximate distinct count (cardinality estimation) |
| **Count-Min Sketch** | Network monitoring, ad-tech | Approximate frequency counting for streaming data |
| **Segment Tree / BIT** | Analytics engines, game engines | Range aggregation queries, real-time leaderboards |
| **Union-Find** | Network connectivity, image processing | Connected components, Kruskal's MST, percolation |
| **Consistent Hashing** | DynamoDB, Cassandra, CDN routing | Distributed shard assignment with minimal remapping |

##### Where Each Algorithm Lives in Production
| Algorithm | Industry System | How It's Used |
|---|---|---|
| **Binary Search** | Database query optimizer, git bisect | Index lookup, finding bug-introducing commit |
| **BFS** | LinkedIn, Facebook | "People you may know", shortest connection path |
| **DFS** | Garbage collectors, web crawlers | Mark-and-sweep GC, site indexing |
| **Dijkstra / A*** | Google Maps, Uber, game engines | Shortest path with real-time traffic, NPC pathfinding |
| **Topological Sort** | Webpack, Gradle, Make, Airflow | Build dependency order, DAG task scheduling |
| **Dynamic Programming** | Spell checkers, bioinformatics | Edit distance for "did you mean?", DNA sequence alignment |
| **Merge Sort** | External sort in databases | Sorting data larger than memory (sort-merge join) |
| **Quick Select** | Database Top-K queries | Finding k-th smallest without full sort |
| **Consistent Hashing** | Load balancers, distributed caches | Sharding with minimal redistribution on node changes |
| **PageRank (graph)** | Google Search, citation analysis | Web page importance ranking |
| **MapReduce (graph + sort)** | Hadoop, Spark | Distributed data processing at scale |
| **Huffman Coding (greedy)** | ZIP, GZIP, JPEG, HTTP/2 HPACK | Data compression |
| **Rabin-Karp (rolling hash)** | Plagiarism detection, git diff | Efficient pattern matching in text |
| **Aho-Corasick** | Intrusion detection, content filtering | Multi-pattern string matching |

#### Optimization Techniques & Patterns

##### Time Complexity Optimization Strategies
| Strategy | Before | After | Example |
|---|---|---|---|
| **Sorting first** | O(n²) brute force pair search | O(n log n) sort + O(n) two pointers | Two Sum (sorted variant) |
| **Hash table** | O(n²) nested loop | O(n) hash lookup | Two Sum, anagram grouping |
| **Binary search** | O(n) linear scan | O(log n) on sorted/monotonic data | Search in sorted array, capacity planning |
| **Prefix sum** | O(n) per query, O(q·n) total | O(1) per query after O(n) preprocess | Subarray sum queries |
| **Sliding window** | O(n·k) recompute window | O(n) maintain running window | Max sum subarray of size k |
| **Two pointers** | O(n²) all pairs | O(n) converging pointers | Container with most water, 3Sum |
| **Monotonic stack** | O(n²) check all previous | O(n) amortized | Next greater element, largest rectangle |
| **Bit manipulation** | O(n) extra space | O(1) space | Find single number (XOR), power of 2 |
| **Divide & conquer** | O(n²) brute force | O(n log n) | Merge sort, closest pair of points |
| **Memoization/DP** | O(2ⁿ) recursive | O(n·k) with memo table | Fibonacci, knapsack, edit distance |

##### Space Complexity Optimization Strategies
| Strategy | Before | After | Example |
|---|---|---|---|
| **In-place operations** | O(n) extra array | O(1) | Reverse array, Dutch national flag, rotate |
| **Rolling array** | O(m·n) full DP table | O(n) two rows | LCS, edit distance, grid DP |
| **Bit array / bitmask** | O(n) boolean array | O(n/8) or O(1) | Sieve of Eratosthenes, visited set |
| **Two variables** | O(n) array for DP | O(1) two vars | Fibonacci, max subarray (Kadane) |
| **Morris traversal** | O(h) stack for tree traversal | O(1) with thread pointers | Inorder tree traversal without recursion or stack |
| **Floyd's cycle detection** | O(n) visited set | O(1) two pointers | Linked list cycle detection |
| **Iterator / generator** | O(n) materialized list | O(1) per yield | Java Streams, Python generators for large datasets |

##### Common Optimization Anti-Patterns (What NOT to Do)
| Anti-Pattern | Problem | Fix |
|---|---|---|
| **Premature optimization** | Optimizing before profiling | Profile first, optimize bottlenecks |
| **String concatenation in loop** | O(n²) due to immutable strings | Use StringBuilder (Java), join (Python) |
| **Nested loops when hash works** | O(n²) when O(n) is possible | Use HashMap for lookups |
| **Sorting when not needed** | O(n log n) when O(n) suffices | Use counting/bucket for limited range |
| **Full sort for top-K** | O(n log n) to get K items | Use heap O(n log K) or quickselect O(n) avg |
| **Computing same value repeatedly** | Exponential tree of calls | Memoize / DP |
| **Using LinkedList for random access** | O(n) per access vs O(1) for ArrayList | Use ArrayList unless frequent insert/delete at middle |
| **Autoboxing in hot loop** | Integer vs int — object creation overhead | Use primitives in performance-critical code |

#### Interview Perspective — What Interviewers Look For

##### How to Approach a DSA Problem (UMPIRE Framework)
```
U — Understand the problem
    → Restate in your own words
    → Clarify edge cases (empty, single element, duplicates, negative)
    → Ask about constraints (input size, value range, sorted?)

M — Match to known patterns
    → What data structure fits? (hash, heap, tree, graph?)
    → What pattern applies? (two pointers, sliding window, DP?)
    → Have you seen a similar problem?

P — Plan
    → Describe your approach in plain English
    → Walk through a small example
    → State the time and space complexity BEFORE coding

I — Implement
    → Write clean, readable code (variable names matter!)
    → Use helper functions to keep it modular
    → Don't try to be clever — correct and readable first

R — Review
    → Trace through your code with the example
    → Check edge cases
    → Verify complexity matches your plan

E — Evaluate
    → Can you optimize? (time? space?)
    → What are the trade-offs?
    → Is there a different approach entirely?
```

##### What Each Level of DSA Mastery Looks Like
| Level | What You Know | What You Can Solve | Interview Readiness |
|---|---|---|---|
| **Beginner** | Arrays, strings, basic sorting, linear/binary search | Easy problems with brute force | Not ready — build foundations first |
| **Intermediate** | HashMap, stack/queue, BFS/DFS, basic DP (1D), two pointers, sliding window | Most Easy + some Medium problems | Ready for mid-level SDE roles |
| **Advanced** | Segment tree, trie, advanced DP (interval, bitmask), union-find, graph algorithms | Most Medium + some Hard problems | Ready for FAANG/top-tier roles |
| **Expert** | Persistent data structures, suffix arrays, advanced graph theory, math/number theory | Hard + competitive programming | Ready for L5+ / Principal / Research roles |

##### Complexity Analysis — How to Think About It
```
O(1)       → Hash lookup, array access, stack push/pop
O(log n)   → Binary search, balanced BST ops, heap insert/extract
O(n)       → Single array pass, hash table build, BFS/DFS
O(n log n) → Efficient sorting, building a balanced BST
O(n²)      → Nested loops (brute force pair comparisons)
O(n³)      → Triple nested loops, Floyd-Warshall, naive matrix multiply
O(2ⁿ)      → All subsets, recursive without memoization
O(n!)      → All permutations, brute force TSP

Rule of thumb for interview constraint → expected complexity:
  n ≤ 10       → O(n!) or O(2ⁿ) — brute force/backtracking OK
  n ≤ 20       → O(2ⁿ) — bitmask DP
  n ≤ 500      → O(n³) — DP, Floyd-Warshall
  n ≤ 10,000   → O(n²) — DP, basic nested loops
  n ≤ 1,000,000 → O(n log n) — sorting-based, binary search
  n ≤ 10⁸      → O(n) — linear scan, two pointers, sliding window
  n > 10⁸      → O(log n) or O(1) — binary search, math, hashing
```

##### DSA Patterns for Interviews (Expanded)
| # | Pattern | Signal Words in Problem | Example Problems | Optimal Complexity |
|---|---|---|---|---|
| 1 | **Two Pointers** | "sorted array", "pair", "remove duplicates" | Two Sum II, 3Sum, Container With Most Water | O(n) |
| 2 | **Sliding Window** | "contiguous subarray/substring", "window size k" | Max Sum Subarray, Longest Substring Without Repeating | O(n) |
| 3 | **Fast & Slow Pointers** | "cycle", "middle of linked list" | Linked List Cycle, Happy Number, Palindrome LL | O(n) |
| 4 | **Binary Search** | "sorted", "minimum/maximum that satisfies", "monotonic" | Search Rotated Array, Koko Eating Bananas | O(log n) |
| 5 | **BFS** | "shortest path", "level by level", "minimum steps" | Word Ladder, Rotten Oranges, 01 Matrix | O(V+E) |
| 6 | **DFS** | "all paths", "permutations", "islands", "tree traversal" | Number of Islands, Path Sum, Clone Graph | O(V+E) |
| 7 | **Backtracking** | "all combinations", "generate all", "valid arrangements" | N-Queens, Sudoku Solver, Subsets, Permutations | Varies |
| 8 | **DP** | "minimum/maximum", "count ways", "can you reach", "longest/shortest" | Longest Increasing Subsequence, Coin Change | Varies |
| 9 | **Greedy** | "most/fewest intervals", "maximum profit", "huffman" | Activity Selection, Jump Game, Meeting Rooms | O(n log n) |
| 10 | **Monotonic Stack** | "next greater/smaller", "largest rectangle", "stock span" | Daily Temperatures, Largest Rectangle in Histogram | O(n) |
| 11 | **Heap / Top-K** | "k-th largest/smallest", "top K", "merge K sorted" | Kth Largest Element, Merge K Sorted Lists | O(n log K) |
| 12 | **Topological Sort** | "ordering", "prerequisites", "dependencies", DAG | Course Schedule, Alien Dictionary | O(V+E) |
| 13 | **Union-Find** | "connected", "groups", "redundant connection" | Number of Connected Components, Redundant Connection | O(V·α(V)) |
| 14 | **Trie** | "prefix", "word dictionary", "autocomplete" | Implement Trie, Word Search II | O(m) per op |
| 15 | **Interval** | "merge intervals", "overlapping", "insert interval" | Merge Intervals, Non-Overlapping Intervals | O(n log n) |
| 16 | **Matrix** | "spiral", "rotate", "search in 2D" | Spiral Order, Rotate Image, Search 2D Matrix | Varies |
| 17 | **Bit Manipulation** | "single number", "power of 2", "count bits" | Single Number, Counting Bits | O(n) or O(1) |

#### When to Use Which Data Structure (Expanded)
| Need | Use | Why | Alternative | Trade-off |
|---|---|---|---|---|
| Fast lookup by key | HashMap / HashSet | O(1) average | TreeMap O(log n) | Hash loses ordering |
| Sorted order iteration | TreeMap / TreeSet | O(log n) ops, sorted | SortedList, Skip List | Slower than hash for lookup |
| FIFO processing | Queue / ArrayDeque | O(1) enqueue/dequeue | LinkedList | ArrayDeque is cache-friendly |
| LIFO / undo / parsing | Stack / ArrayDeque | O(1) push/pop | — | Consider monotonic stack for range problems |
| Priority-based access | PriorityQueue (heap) | O(log n) insert/extract | Balanced BST O(log n) | BST supports arbitrary delete; heap doesn't |
| Range queries | Segment Tree / BIT | O(log n) per query/update | Sqrt decomposition O(√n) | Segment tree is more flexible |
| Prefix matching | Trie | O(m) per operation | HashMap with key iteration | Trie uses more memory but is faster for prefix ops |
| Connected components | Union-Find (DSU) | Near O(1) with optimizations | BFS/DFS O(V+E) | Union-Find better for incremental connectivity |
| Approximate counting | HyperLogLog | O(1) per add, fixed memory | Exact HashSet O(n) | Trades accuracy for massive memory savings |
| Approximate membership | Bloom Filter | O(k) per query, fixed memory | HashSet O(n) | False positives possible, no false negatives |
| Weighted shortest path | Dijkstra + min-heap | O((V+E) log V) | Bellman-Ford O(V·E) | Dijkstra requires non-negative weights |
| All-pairs shortest | Floyd-Warshall | O(V³) | V × Dijkstra O(V(V+E) log V) | Floyd-Warshall simpler for dense graphs |

### System Design

#### Online Resources
| Resource | URL | Best For |
|---|---|---|
| **System Design Primer** | `https://github.com/donnemartin/system-design-primer` | Comprehensive free guide |
| **ByteByteGo** | `https://bytebytego.com/` | Visual system design explanations |
| **Grokking System Design** | `https://www.designgurus.io/course/grokking-the-system-design-interview` | Interview-focused designs |
| **High Scalability** | `http://highscalability.com/` | Real-world architecture case studies |
| **Martin Fowler's Blog** | `https://martinfowler.com/` | Architectural patterns, enterprise design |
| **InfoQ Architecture** | `https://www.infoq.com/architecture-design/` | Conference talks, architecture trends |
| **Awesome System Design** | `https://github.com/madd86/awesome-system-design` | Curated links, papers, talks |
| **System Design 101 (ByteByteGo)** | `https://github.com/ByteByteGoHq/system-design-101` | Visual cheat sheets for system design topics |
| **Educative.io System Design** | `https://www.educative.io/courses/grokking-modern-system-design-interview-for-engineers-managers` | Modern interactive system design course |

#### System Design Topics Map
```
HLD (High-Level Design):
  Load Balancing → Caching → CDN → Database Sharding →
  Message Queues → Microservices → API Gateway →
  Rate Limiting → Consistent Hashing → CAP Theorem →
  Replication → Partitioning → Consensus (Raft/Paxos)

LLD (Low-Level Design):
  Class Design → SOLID → Design Patterns → OOP Modeling →
  API Design (REST/gRPC) → Schema Design → State Machines →
  Concurrency Control → Error Handling Strategy → Logging
```

#### HLD — High-Level Design Components (Deep Dive)

##### Load Balancing
| Concept | Description | Examples |
|---|---|---|
| **L4 (Transport)** | Routes by IP/port, no payload inspection | AWS NLB, HAProxy (TCP mode) |
| **L7 (Application)** | Routes by HTTP headers, URL, cookies | Nginx, AWS ALB, Envoy |
| **Algorithms** | Round Robin, Least Connections, IP Hash, Weighted, Consistent Hashing | — |
| **Health Checks** | Active (ping), Passive (track errors) | Heartbeat, TCP check, HTTP 200 |
| **Global (DNS-based)** | Geo-routing via DNS | Route 53, Cloudflare LB |
| **Service Mesh LB** | Sidecar proxy handles LB per-service | Istio, Linkerd |

##### Caching Strategies
```
Caching Layers:
  Client Cache (browser, app)
    → CDN Cache (edge, static assets)
      → API Gateway Cache (response cache)
        → Application Cache (in-process, e.g., Guava, Caffeine)
          → Distributed Cache (Redis, Memcached)
            → Database Cache (query cache, materialized views)
```

| Strategy | Description | When to Use |
|---|---|---|
| **Cache-Aside (Lazy)** | App checks cache → miss → reads DB → writes cache | General purpose, read-heavy |
| **Write-Through** | App writes cache + DB simultaneously | Consistency-critical, slower writes |
| **Write-Behind (Back)** | App writes cache → async flush to DB | Write-heavy, eventual consistency OK |
| **Read-Through** | Cache itself fetches from DB on miss | Transparent to application |
| **Refresh-Ahead** | Pre-fetch before expiry based on access patterns | Predictable access, low latency |

| Eviction Policy | How It Works | Use Case |
|---|---|---|
| **LRU** | Evict least recently used | General purpose |
| **LFU** | Evict least frequently used | Hot-spot heavy workloads |
| **TTL** | Expire after time-to-live | Time-sensitive data |
| **FIFO** | Evict oldest entry | Simple, predictable |

##### Content Delivery Network (CDN)
| Concept | Description |
|---|---|
| **Push CDN** | Origin proactively pushes content to edge | 
| **Pull CDN** | Edge fetches from origin on first request, then caches |
| **Edge Computing** | Run logic at CDN edge (Cloudflare Workers, Lambda@Edge) |
| **Cache Invalidation** | Purge by URL, tag, or prefix; versioned filenames (`app.v2.js`) |
| **Providers** | Cloudflare, AWS CloudFront, Akamai, Fastly |

##### Database Sharding & Partitioning
| Strategy | How It Works | Pros | Cons |
|---|---|---|---|
| **Horizontal (Sharding)** | Split rows across DB instances by shard key | Scales writes, each shard smaller | Cross-shard queries hard |
| **Vertical** | Split columns into separate tables/services | Isolate hot columns | Joins across partitions |
| **Hash-Based** | Hash(key) % N → shard | Even distribution | Rebalancing on shard add/remove |
| **Range-Based** | Key ranges → shards (A-M, N-Z) | Range queries easy | Hot spots if uneven |
| **Consistent Hashing** | Hash ring with virtual nodes | Minimal redistribution on changes | Complexity |
| **Directory-Based** | Lookup table maps key → shard | Flexible | Directory is SPOF |

##### Message Queues & Event-Driven Architecture
```
Point-to-Point (Queue):
  Producer → [Queue] → Consumer (one consumer gets each message)
  Used: Task distribution, order processing
  Examples: SQS, RabbitMQ (default)

Pub/Sub (Fan-out):
  Publisher → [Topic] → Subscriber A
                      → Subscriber B
                      → Subscriber N
  Used: Notifications, event broadcasting
  Examples: SNS, Kafka topics, Redis Pub/Sub

Event Streaming:
  Producer → [Log/Partition] → Consumer Group A (parallel consumers)
                              → Consumer Group B
  Used: Real-time analytics, audit trail, replay
  Examples: Kafka, Pulsar, Kinesis
```

| Pattern | Description | Use Case |
|---|---|---|
| **CQRS** | Separate read/write models | Read-heavy with different query needs |
| **Event Sourcing** | Store events, not state; replay to reconstruct | Audit trail, temporal queries |
| **Outbox Pattern** | Write to DB + outbox table → relay to queue | Reliable event publishing |
| **Saga Pattern** | Distributed transaction via compensating events | Cross-service workflows |
| **Dead Letter Queue** | Failed messages go to DLQ for inspection | Error handling, debugging |

##### Microservices Architecture
| Concept | Description |
|---|---|
| **Service Decomposition** | Split by bounded context (DDD), not by technical layer |
| **API Gateway** | Single entry point: routing, auth, rate limiting, aggregation |
| **Service Mesh** | Sidecar proxies handle networking (mTLS, retries, LB) — Istio, Linkerd |
| **Service Discovery** | Dynamic lookup: Consul, Eureka, K8s DNS |
| **Saga vs 2PC** | Saga (eventual) for most distributed txns; 2PC (strong) for critical atomicity |
| **Sidecar Pattern** | Co-deploy helper process alongside main service (logging, proxy) |
| **Strangler Fig** | Incrementally replace monolith by routing to new services |
| **Backend for Frontend (BFF)** | Separate API layers per client type (mobile, web, IoT) |

##### Rate Limiting
| Algorithm | How It Works | Pros | Cons |
|---|---|---|---|
| **Token Bucket** | Tokens added at fixed rate, consumed per request | Allows bursts, simple | Burst size tuning |
| **Leaky Bucket** | Requests processed at fixed rate, overflow dropped | Smooth output | No burst support |
| **Fixed Window** | Count requests per time window | Simple | Burst at window edges |
| **Sliding Window Log** | Track timestamp of each request, count in window | Accurate | Memory-heavy |
| **Sliding Window Counter** | Weighted count across current + previous window | Good balance | Approximation |

##### Reliability & Resilience Patterns
| Pattern | Description | Example |
|---|---|---|
| **Circuit Breaker** | Stop calling failing service → fallback → retry after timeout | Netflix Hystrix, Resilience4j |
| **Bulkhead** | Isolate resources (thread pools, connections) per service | Separate pools per dependency |
| **Retry with Backoff** | Exponential backoff + jitter on transient failures | `retry(3, backoff=2^n + jitter)` |
| **Timeout** | Bound waiting time for external calls | HTTP client timeout, gRPC deadline |
| **Idempotency** | Same request = same result, safe to retry | Idempotency keys (Stripe API) |
| **Failover** | Active-passive or active-active redundancy | Database failover, DNS failover |
| **Graceful Degradation** | Reduce features instead of total failure | Show cached data when DB is down |
| **Health Checks** | Liveness (alive?) + Readiness (ready to serve?) | K8s probes, ELB health checks |

#### LLD — Low-Level Design Patterns (Deep Dive)

##### SOLID Principles Quick Reference
| Principle | Full Name | One-Liner | Violation Sign |
|---|---|---|---|
| **S** | Single Responsibility | One class = one reason to change | "and" in class description |
| **O** | Open/Closed | Open for extension, closed for modification | Modifying existing code to add features |
| **L** | Liskov Substitution | Subtypes must be substitutable for base types | Overriding methods that break contracts |
| **I** | Interface Segregation | Many specific interfaces > one fat interface | Implementing methods that throw `UnsupportedOperationException` |
| **D** | Dependency Inversion | Depend on abstractions, not concretions | `new ConcreteClass()` inside business logic |

##### Design Pattern Categories for LLD
| Category | Patterns | When to Use |
|---|---|---|
| **Creational** | Singleton, Factory Method, Abstract Factory, Builder, Prototype | Object creation complexity, hide instantiation details |
| **Structural** | Adapter, Bridge, Composite, Decorator, Facade, Flyweight, Proxy | Compose objects, interface adaptation |
| **Behavioral** | Strategy, Observer, Command, State, Template Method, Iterator, Chain of Responsibility, Mediator, Visitor | Object interaction, algorithms, state changes |

##### API Design Checklist
| Aspect | REST Best Practice | gRPC Best Practice |
|---|---|---|
| **Naming** | Nouns for resources (`/users`), verbs via HTTP methods | Service + Method (`UserService.GetUser`) |
| **Versioning** | URL (`/v2/users`) or header (`Accept-Version: v2`) | Package version (`user.v2.UserService`) |
| **Errors** | Standard HTTP status codes + error body | gRPC status codes + `google.rpc.Status` |
| **Pagination** | `?page=2&size=20` or cursor-based (`?cursor=abc`) | `page_token` + `page_size` in request message |
| **Filtering** | Query params (`?status=active&sort=name`) | Repeated fields or `FieldMask` |
| **Auth** | Bearer token (JWT), OAuth2, API key | Metadata headers, mutual TLS |
| **Rate Limits** | `X-RateLimit-*` headers, `429 Too Many Requests` | Deadline propagation, backpressure |

##### State Machine Design
```
Example: Order State Machine

  [CREATED] ──place──▶ [PLACED] ──pay──▶ [PAID] ──ship──▶ [SHIPPED] ──deliver──▶ [DELIVERED]
       │                   │                │                   │
       └──cancel──▶ [CANCELLED]  ◀──cancel──┘      ◀──return──┘──▶ [RETURNED]

Rules:
  - Define all STATES (enum)
  - Define all EVENTS/TRANSITIONS
  - Guard conditions (can cancel only if not shipped)
  - Side effects per transition (send email, update inventory)
  - Use State Pattern or state machine library (Spring StateMachine, XState)
```

##### Schema Design Principles
| Concept | Relational (SQL) | Document (NoSQL) |
|---|---|---|
| **Normalize** | 3NF+ — eliminate redundancy, use JOINs | Denormalize — embed related data |
| **When to denormalize** | Read-heavy + joins are bottleneck | Always start denormalized |
| **Relationships** | Foreign keys + junction tables | Embed (1:few) or reference (1:many, many:many) |
| **Indexing** | B-Tree (default), Hash, GIN (full-text), partial indexes | Compound indexes, TTL indexes |
| **Migration** | Schema migrations (Flyway, Liquibase, Alembic) | Schema-less, validation at app layer |

#### Classic System Design Case Studies

##### HLD Case Studies — Approach Template
```
For each system:
1. Requirements     → Functional + Non-functional
2. Estimation       → QPS, Storage, Bandwidth, Memory
3. API Design       → Core endpoints / RPCs
4. High-Level Arch  → ASCII diagram with all components
5. Data Model       → Schema + storage choice (SQL/NoSQL/both)
6. Deep Dive        → 2-3 most interesting components
7. Scaling          → How to handle 10x → 100x → 1000x
8. Trade-offs       → Key decisions with alternatives
9. Bottlenecks      → Failure modes + mitigations
```

| Case Study | Key Components | Key Concepts |
|---|---|---|
| **URL Shortener** | Hash generation, KGS, DB, cache, analytics | Base62 encoding, collision handling, read-heavy |
| **Rate Limiter** | Token bucket, sliding window, distributed counter | Redis atomic ops, race conditions, distributed sync |
| **Chat System** | WebSocket servers, presence, message store, push | Fan-out, delivery guarantees, offline queuing |
| **News Feed** | Fan-out service, ranking, cache, notification | Fan-out-on-write vs fan-out-on-read, celebrity problem |
| **Video Streaming** | Transcoding, CDN, chunk delivery, recommendation | Adaptive bitrate, DAG task queue, cold storage |
| **Web Crawler** | Frontier, fetcher, parser, deduplication, DNS resolver | Politeness, URL prioritization, trap detection |
| **Notification System** | Priority queue, template engine, delivery service | Multi-channel (push/SMS/email), throttling, retry |
| **Search Autocomplete** | Trie, top-K, data collection, aggregation pipeline | Trie pruning, frequency updates, shard by prefix |
| **Distributed Cache** | Consistent hashing, cache cluster, eviction | Cache stampede, hot key, replication |
| **Payment System** | Payment gateway, ledger, reconciliation, idempotency | Double-entry bookkeeping, exactly-once, PCI compliance |
| **Ride-Sharing** | Geo-index, matching, trip, ETA, surge pricing | Geohash/S2/H3, supply-demand, real-time location |
| **Distributed File Storage** | Metadata service, chunk servers, replication | GFS/HDFS model, heartbeats, chunk checksums |

##### LLD Case Studies — Approach Template
```
For each system:
1. Use Cases        → Actors + user stories
2. Class Diagram    → Entities + relationships (UML)
3. Design Patterns  → Which patterns + why
4. SOLID Check      → Verify each principle
5. Code Skeleton    → Key classes/interfaces
6. State Diagrams   → For stateful entities
7. Extensibility    → How to add features
8. Testing Plan     → What + how to test
```

| Case Study | Key Classes | Key Patterns |
|---|---|---|
| **Parking Lot** | ParkingLot, Floor, Slot, Vehicle, Ticket | Strategy (pricing), Factory (vehicle), Observer (display) |
| **LRU Cache** | LRUCache, DoublyLinkedList, HashMap | — (data structure design) |
| **Elevator System** | Elevator, Floor, Request, Scheduler | Strategy (scheduling), State (elevator), Observer (display) |
| **Library Management** | Library, Book, Member, Loan, Fine | Observer (due date), Strategy (fine calc) |
| **Online Shopping** | Cart, Order, Product, Payment, User | Strategy (payment), Observer (notifications), State (order) |
| **Tic-Tac-Toe / Chess** | Board, Player, Piece, Move, Game | Command (moves), Strategy (AI), Template Method (game flow) |
| **Hotel Booking** | Hotel, Room, Booking, Guest, Payment | Strategy (pricing by season), State (room status) |
| **Vending Machine** | VendingMachine, Inventory, Coin, Product | State (idle/selecting/dispensing), Strategy (payment) |

#### Architecture Styles Comparison
| Style | When to Use | Pros | Cons | Examples |
|---|---|---|---|---|
| **Monolith** | Small team, early stage, simple domain | Simple deploy, easy debugging | Scaling limits, tight coupling | Early-stage startups |
| **Microservices** | Large team, complex domain, independent scaling | Scale independently, tech diversity | Operational complexity, distributed debugging | Netflix, Uber, Amazon |
| **Serverless** | Event-driven, variable load, rapid prototyping | Zero ops, auto-scale, pay-per-use | Cold starts, vendor lock-in, time limits | Lambda, Cloud Functions |
| **Event-Driven** | Async workflows, loose coupling, audit trails | Decoupled, replay, temporal queries | Eventual consistency, debugging complexity | Event sourcing systems |
| **Hexagonal (Ports & Adapters)** | Domain-centric, testable, swappable infra | Domain isolation, easy testing | More boilerplate | DDD applications |
| **CQRS** | Separate read/write scaling, complex queries | Optimized read models, event replay | Eventual consistency, complexity | High-read analytics systems |
| **Service-Oriented (SOA)** | Enterprise integration, reusable services | Reuse, standards (WSDL/SOAP) | Heavyweight, ESB complexity | Enterprise IT |

#### Estimation Cheat Sheet (Back-of-Envelope)
| Metric | Quick Estimate |
|---|---|
| **Seconds in a day** | ~86,400 ≈ ~100K |
| **Seconds in a month** | ~2.5M |
| **Seconds in a year** | ~31.5M ≈ ~32M |
| **1 million requests/day** | ~12 QPS |
| **1 billion requests/day** | ~12K QPS |
| **1 char** | 1 byte (ASCII) / 2-4 bytes (UTF-8) |
| **1 image (average)** | ~300 KB |
| **1 short video (1 min)** | ~5 MB |
| **1 TB** | 1,000 GB = 1,000,000 MB |
| **Read:Write ratio (social)** | ~100:1 or higher |
| **80/20 rule (caching)** | 20% of data serves 80% of reads |

#### Common System Design Interview Framework
```
Step 1: Clarify Requirements (5 min)
  → Functional requirements (what the system does)
  → Non-functional requirements (scale, latency, availability, durability)
  → Out of scope (explicitly state what you won't cover)

Step 2: Estimate Scale (5 min)
  → DAU / MAU → QPS (read + write)
  → Storage (per record × records × retention period)
  → Bandwidth (QPS × avg response size)
  → Memory for cache (80/20 rule: 20% of daily data)

Step 3: High-Level Design (10-15 min)
  → Draw architecture diagram (clients, LB, servers, DB, cache, queues)
  → Identify core APIs (REST / gRPC)
  → Choose database (SQL vs NoSQL, justify)
  → Show data flow for main use cases

Step 4: Deep Dive (10-15 min)
  → Pick 2-3 most interesting/challenging components
  → Discuss trade-offs (consistency vs availability, latency vs throughput)
  → Show scaling strategy (sharding, caching, replication)
  → Address failure modes (what if X goes down?)

Step 5: Wrap Up (5 min)
  → Summarize key design decisions
  → Mention what you'd add with more time (monitoring, analytics, ML)
  → Discuss potential improvements and scaling path
```

### Networking & Protocols
| Resource | URL | Best For |
|---|---|---|
| **MDN Web Docs — HTTP** | `https://developer.mozilla.org/en-US/docs/Web/HTTP` | HTTP protocol deep-dive |
| **gRPC Official Docs** | `https://grpc.io/docs/` | gRPC concepts, protobuf, streaming |
| **REST API Tutorial** | `https://restfulapi.net/` | REST principles, best practices |
| **High Performance Browser Networking** | `https://hpbn.co/` | Free book: TCP, TLS, HTTP/2, WebSocket |
| **Beej's Guide to Network Programming** | `https://beej.us/guide/bgnet/` | Sockets, TCP/UDP from scratch |
| **Wikipedia — OSI model** | `https://en.wikipedia.org/wiki/OSI_model` | Network layers reference |

#### Protocol Comparison Quick Reference
| Protocol | Type | Connection | Use Case | Format |
|---|---|---|---|---|
| **HTTP/REST** | Request-Response | Stateless | CRUD APIs, web services | JSON/XML |
| **gRPC** | RPC | Multiplexed (HTTP/2) | Microservice-to-microservice, low latency | Protobuf (binary) |
| **GraphQL** | Query language | Stateless | Flexible client-driven queries | JSON |
| **WebSocket** | Bidirectional | Stateful | Real-time: chat, gaming, live feeds | Any |
| **TCP** | Stream | Stateful | Reliable ordered delivery | Bytes |
| **UDP** | Datagram | Stateless | Low-latency: video, DNS, gaming | Bytes |
| **RPC (general)** | Remote procedure call | Varies | Cross-process/machine function calls | Varies |
| **MQTT** | Pub/Sub | Stateful | IoT, lightweight messaging | Binary |
| **AMQP** | Message queue | Stateful | Enterprise messaging (RabbitMQ) | Binary |

#### Stateful vs Stateless
| Aspect | Stateless | Stateful |
|---|---|---|
| Server memory | No client state between requests | Server tracks client state |
| Scalability | Easy — any server can handle any request | Hard — client tied to specific server (sticky sessions) |
| Examples | REST, HTTP, DNS, UDP | WebSocket, TCP connections, gRPC streams, database sessions |
| Trade-off | Client sends more data per request | Server uses more memory |

### Operating Systems
| Resource | URL | Best For |
|---|---|---|
| **OSTEP (free book)** | `https://pages.cs.wisc.edu/~remzi/OSTEP/` | Free, excellent OS textbook |
| **MIT 6.S081 (xv6)** | `https://pdos.csail.mit.edu/6.S081/` | Hands-on OS course with real kernel code |
| **OSDev Wiki** | `https://wiki.osdev.org/` | OS development from scratch |
| **Linux Kernel Docs** | `https://www.kernel.org/doc/html/latest/` | Linux kernel internals |
| **Julia Evans' Zines** | `https://wizardzines.com/` | Visual, fun explanations of OS/networking |

#### OS Core Concepts
```
Processes → Threads → Scheduling (Round Robin, MLFQ, CFS) →
Memory Management (Paging, Segmentation, Virtual Memory) →
Concurrency (Locks, Semaphores, Monitors, Deadlocks) →
File Systems (Inodes, Journaling, VFS) →
I/O (Blocking, Non-blocking, Async, Polling, Epoll) →
IPC (Pipes, Shared Memory, Message Queues, Sockets)
```

### DBMS & Databases
| Resource | URL | Best For |
|---|---|---|
| **Use The Index, Luke** | `https://use-the-index-luke.com/` | SQL indexing and performance |
| **CMU Database Course** | `https://15445.courses.cs.cmu.edu/` | Academic database internals |
| **SQLZoo** | `https://sqlzoo.net/` | Interactive SQL tutorial |
| **DB-Engines** | `https://db-engines.com/` | Database comparison and ranking |
| **PostgreSQL Docs** | `https://www.postgresql.org/docs/current/` | Excellent reference for RDBMS concepts |

#### Database Concepts Map
```
ACID → Transactions → Isolation Levels (Read Uncommitted → Serializable) →
Indexing (B-Tree, Hash, GIN, GiST) → Query Optimization → Joins →
Normalization (1NF → BCNF) → Denormalization → Sharding →
Replication (Leader-Follower, Multi-Leader, Leaderless) →
CAP Theorem → Eventual Consistency → Distributed Transactions (2PC, Saga)
```

### Testing & Quality
| Resource | URL | Best For |
|---|---|---|
| **Martin Fowler — Testing** | `https://martinfowler.com/testing/` | Test pyramid, test doubles, strategies |
| **Testing Trophy (Kent C. Dodds)** | `https://kentcdodds.com/blog/the-testing-trophy-and-testing-classifications` | Modern testing philosophy |
| **Google Testing Blog** | `https://testing.googleblog.com/` | Industry testing practices |
| **Test Desiderata (Kent Beck)** | `https://kentbeck.github.io/TestDesiderata/` | Properties of good tests |
| **xUnit Patterns** | `http://xunitpatterns.com/` | Catalog of test patterns and smells |

#### Testing Types Map
```
Unit Testing → Integration Testing → Contract Testing →
Component Testing → End-to-End Testing → Performance Testing →
Load Testing → Stress Testing → Chaos Testing →
Security Testing → Mutation Testing → Property-Based Testing →
Smoke Testing → Regression Testing → Acceptance Testing
```

#### Development Methodologies
| Methodology | Full Name | Key Idea | Cycle |
|---|---|---|---|
| **TDD** | Test-Driven Development | Write test first, then code, then refactor | Red → Green → Refactor |
| **BDD** | Behavior-Driven Development | Describe behavior in business language (Given/When/Then) | Scenario → Steps → Implementation |
| **ATDD** | Acceptance Test-Driven Development | Write acceptance tests with stakeholders first | Discuss → Distill → Develop → Demo |
| **DDD** | Domain-Driven Design | Model code around the business domain | Ubiquitous Language → Bounded Contexts |

### SDLC & Methodologies
| Resource | URL | Best For |
|---|---|---|
| **Agile Manifesto** | `https://agilemanifesto.org/` | Original agile principles |
| **Scrum Guide** | `https://scrumguides.org/` | Official Scrum framework |
| **Atlassian Agile Coach** | `https://www.atlassian.com/agile` | Practical Agile/Scrum/Kanban guides |
| **Martin Fowler — Agile** | `https://martinfowler.com/agile.html` | Thoughtful agile engineering practices |
| **Shape Up (Basecamp)** | `https://basecamp.com/shapeup` | Alternative to Scrum — free book |
| **The Twelve-Factor App** | `https://12factor.net/` | SaaS best practices (methodology) |

#### SDLC Models
| Model | Flow | Best For | Risk |
|---|---|---|---|
| **Waterfall** | Linear: Requirements → Design → Build → Test → Deploy | Stable, well-known requirements | Late feedback |
| **Agile/Scrum** | Iterative sprints with continuous feedback | Evolving requirements | Scope discipline needed |
| **Kanban** | Continuous flow, WIP limits | Operations, maintenance | Can lack long-term planning |
| **Spiral** | Iterative with risk analysis each cycle | Large, risky projects | Complex process |
| **XP** | Pair programming, CI, TDD, short iterations | High-quality, changing requirements | Discipline-intensive |
| **SAFe** | Scaled Agile Framework for large orgs | Enterprise-scale agile | Heavyweight, bureaucratic |
| **DevOps** | Continuous loop: plan → code → build → test → deploy → monitor | Fast delivery, SRE | Cultural shift required |
| **Lean** | Eliminate waste, optimize flow, continuous improvement | Startup/lean teams | Requires measurement culture |

#### End-to-End Software Development Lifecycle — Complete Reference

##### Phase 1: Planning & Requirements
```
Activities:
  - Stakeholder interviews, market research
  - User stories / Use cases / Epics
  - Acceptance criteria definition
  - Feasibility study (technical, financial, operational)
  - Architecture Decision Records (ADRs)
  - Risk assessment and mitigation plan
  - Project timeline and resource estimation

Artifacts:
  - PRD (Product Requirements Document)
  - BRD (Business Requirements Document)
  - SRS (Software Requirements Specification)
  - User story map / Story point estimates
  - Sprint backlog / Product backlog
  - Wireframes / Low-fidelity prototypes

Tools:
  Jira, Linear, Notion, Confluence, Miro, Figma (wireframes)

Key Roles:
  Product Manager, Business Analyst, Tech Lead, Architect
```

##### Phase 2: Design & Architecture
```
Activities:
  - System architecture (monolith vs microservices vs serverless)
  - Database schema design (ER diagrams, normalization)
  - API contract design (OpenAPI/Swagger, gRPC protobuf)
  - UI/UX design (wireframes → mockups → prototypes)
  - Security architecture (threat modeling, auth flow)
  - Infrastructure design (cloud architecture, networking)
  - Design reviews and RFC/ADR process

Artifacts:
  - HLD (High-Level Design) document
  - LLD (Low-Level Design) document
  - ER diagrams / Data model
  - API specifications (OpenAPI YAML)
  - Sequence diagrams / Flow diagrams
  - Architecture Decision Records (ADRs)
  - UI/UX design system / Figma designs

Design Patterns to Consider:
  - MVC / MVVM / Clean Architecture
  - Repository pattern, Service layer
  - Event-driven, CQRS, Event Sourcing
  - Circuit breaker, Saga, Outbox

Tools:
  Figma, Miro, Lucidchart, draw.io, Excalidraw, PlantUML, Mermaid
```

##### Phase 3: Development
```
Activities:
  - Environment setup (local dev, devcontainers, codespaces)
  - Version control setup (Git, branching strategy)
  - Code implementation (feature branches, PRs)
  - Code reviews (pair programming, PR reviews)
  - Unit testing & integration testing (TDD/BDD)
  - Documentation (inline comments, API docs, README)
  - Dependency management & vulnerability scanning

Best Practices:
  - Feature flags for in-progress work
  - Trunk-based development or GitHub Flow
  - Conventional commits for auto-changelogs
  - Pre-commit hooks (lint, format, test)
  - CI pipeline runs on every push/PR
  - Code coverage gates (minimum threshold)

Tools:
  VS Code, IntelliJ, Git, GitHub/GitLab, SonarQube, pre-commit
```

##### Phase 4: Testing
```
Testing Pyramid (from fast/cheap to slow/expensive):
  ┌───────────────────┐
  │    E2E Tests      │  ← Few, expensive, slow (Cypress, Playwright)
  ├───────────────────┤
  │ Integration Tests │  ← More (Testcontainers, WireMock)
  ├───────────────────┤
  │   Unit Tests      │  ← Many, fast, cheap (JUnit, Jest, pytest)
  └───────────────────┘

Testing Types:
  Functional:
    - Unit Testing — individual methods/classes
    - Integration Testing — component interactions, database, APIs
    - Contract Testing — API compatibility (Pact, Spring Cloud Contract)
    - E2E Testing — full user workflows (Cypress, Playwright, Selenium)
    - Smoke Testing — critical path after deployment
    - Regression Testing — existing features still work
    - Acceptance Testing — meets business requirements (UAT)

  Non-Functional:
    - Performance Testing — response times under normal load (k6, Gatling)
    - Load Testing — behavior under expected load
    - Stress Testing — behavior beyond capacity limits
    - Chaos Testing — resilience to failures (Chaos Monkey, Litmus)
    - Security Testing — OWASP ZAP, Burp Suite, SAST/DAST
    - Accessibility Testing — WCAG compliance (axe, Lighthouse)
    - Compatibility Testing — browser/device/OS matrix

  Specialized:
    - Mutation Testing — test quality validation (PIT, Stryker)
    - Property-Based Testing — random inputs (QuickCheck, jqwik)
    - Snapshot Testing — UI regression (Jest snapshots)
    - Visual Regression — screenshot comparison (Percy, Chromatic)
    - API Testing — endpoint validation (Postman, REST Assured)

Tools:
  JUnit 5, TestNG, Mockito, Testcontainers, WireMock,
  Jest, Vitest, Cypress, Playwright, Selenium,
  k6, Gatling, JMeter, Locust, Artillery,
  SonarQube, OWASP ZAP, Snyk, Trivy, Checkmarx
```

##### Phase 5: Build & Packaging
```
Activities:
  - Compile source code
  - Run lint/format checks
  - Run static analysis (SAST)
  - Execute automated tests
  - Build deployable artifacts (JAR, Docker image, binary)
  - Publish artifacts to registry
  - Generate documentation (Javadoc, API docs)

Artifact Types:
  - JAR / WAR / EAR (Java)
  - Docker image (containerized apps)
  - npm package (JavaScript libraries)
  - Python wheel / sdist
  - Helm chart (Kubernetes deployments)
  - Binary executable (Go, Rust, C++)
  - Static site bundle (frontend SPA)

Registries:
  - Docker Hub, GitHub Container Registry (GHCR), AWS ECR
  - Maven Central, JFrog Artifactory, Nexus
  - npm Registry, PyPI, crates.io
  - Helm Chart repositories

Tools:
  Maven, Gradle, npm, Docker, Buildah, Kaniko (CI container builds)
```

##### Phase 6: Deployment & Release
```
Deployment Pipeline:
  Build → Test → Stage → Approval → Production

Environment Ladder:
  Local Dev → Dev/Sandbox → Integration/QA → Staging/Pre-prod → Production

Deployment Strategies:
  - Rolling update (gradual replacement)
  - Blue/Green (instant switchover)
  - Canary (progressive traffic shift)
  - Feature flags (deploy ≠ release)
  - Shadow/dark launch (mirror traffic)

Database Migrations:
  - Flyway / Liquibase (Java), Alembic (Python), Prisma Migrate (Node.js)
  - Forward-only migrations (never edit existing migrations)
  - Backward-compatible schema changes (expand-contract pattern)
  - Zero-downtime migration: add column → backfill → switch code → drop old

Infrastructure:
  - Terraform / Pulumi for cloud provisioning
  - Ansible for configuration management
  - Kubernetes + Helm for container orchestration
  - ArgoCD / Flux for GitOps deployments
  - Serverless: AWS Lambda, Google Cloud Functions, Azure Functions

Tools:
  Terraform, Ansible, Helm, ArgoCD, Flux, Spinnaker,
  Flyway, Liquibase, AWS CDK, Pulumi
```

##### Phase 7: Monitoring & Observability
```
Three Pillars of Observability:
  1. Logs    — What happened? (structured text/JSON events)
  2. Metrics — How much? How fast? (numerical time-series data)
  3. Traces  — How did a request flow? (distributed request path)

Key Metrics (RED / USE methods):
  RED Method (for services):
    - Rate      — requests per second
    - Errors    — error rate per second
    - Duration  — latency distribution (p50, p95, p99)

  USE Method (for resources):
    - Utilization — % resource busy (CPU, memory, disk)
    - Saturation  — work queued (request queue depth)
    - Errors      — error count

SLIs, SLOs, SLAs:
  SLI (Service Level Indicator) — measured metric (e.g., 99.2% requests < 200ms)
  SLO (Service Level Objective) — target (e.g., 99.9% of requests < 200ms)
  SLA (Service Level Agreement) — contract with consequences for missing SLO

Alerting Best Practices:
  - Alert on SLO violations, not individual metrics
  - Use burn-rate alerts (sustained error rate, not spikes)
  - Runbooks for every alert (what to do when it fires)
  - Avoid alert fatigue — fewer, actionable alerts

Tools:
  Prometheus, Grafana, ELK/OpenSearch, Jaeger, Zipkin,
  Datadog, New Relic, PagerDuty, OpenTelemetry, Loki
```

##### Phase 8: Maintenance & Operations
```
Activities:
  - Incident management (detect → triage → fix → postmortem)
  - On-call rotation and escalation
  - Technical debt management (track, prioritize, budget)
  - Dependency updates (Dependabot, Renovate)
  - Security patching (CVE monitoring)
  - Capacity planning and scaling
  - Cost optimization (cloud cost analysis)
  - Documentation updates

Incident Management flow:
  Detect → Alert → Acknowledge → Triage → Mitigate → 
  Resolve → Postmortem → Action Items → Done

Postmortem (Blameless):
  - Timeline of events
  - Root cause analysis (5 Whys)
  - Impact assessment
  - What went well / what didn't
  - Action items with owners and deadlines

Tools:
  PagerDuty, OpsGenie, Statuspage, Dependabot, Renovate,
  AWS Cost Explorer, Kubecost, Datadog, Jira
```

#### Frontend / UI/UX Aspects
| Aspect | Key Concepts | Tools |
|---|---|---|
| **Design Systems** | Component library, tokens, guidelines | Storybook, Figma, Tailwind |
| **Responsive Design** | Mobile-first, flexbox, CSS grid, media queries | Chrome DevTools, BrowserStack |
| **Accessibility (a11y)** | WCAG 2.1, ARIA roles, keyboard navigation, screen readers | axe, Lighthouse, NVDA |
| **Performance** | Core Web Vitals (LCP, FID, CLS), lazy loading, code splitting | Lighthouse, WebPageTest |
| **State Management** | Client state vs server state, caching, optimistic updates | Redux, Zustand, TanStack Query |
| **SSR / SSG / ISR** | Server-side rendering, static generation, incremental regen | Next.js, Nuxt, SvelteKit |
| **PWA** | Service workers, offline-first, push notifications | Workbox, web manifest |
| **Micro-Frontends** | Independent frontend modules, runtime composition | Module Federation, single-spa |
| **Design Tokens** | Color, spacing, typography as variables | Style Dictionary, Figma Tokens |
| **Testing** | Component tests, visual regression, E2E | Storybook, Chromatic, Playwright |

#### Backend Aspects
| Aspect | Key Concepts | Tools |
|---|---|---|
| **API Design** | REST conventions, OpenAPI, versioning, pagination, rate limiting | Swagger, Postman, Stoplight |
| **Authentication** | OAuth 2.0, OIDC, JWT, session-based, SAML, passkeys | Keycloak, Auth0, Firebase Auth |
| **Authorization** | RBAC, ABAC, policy engines, permission models | OPA, Casbin, Spring Security |
| **Data Validation** | Input sanitization, schema validation, DTOs | Hibernate Validator, Zod, Joi |
| **Error Handling** | Structured errors, error codes, retry semantics, idempotency | RFC 7807 (Problem Details JSON) |
| **Caching** | Redis, Memcached, CDN caching, HTTP cache headers | Redis, Varnish, Cloudflare |
| **Background Jobs** | Async processing, task queues, scheduled jobs | Celery, Sidekiq, Spring Batch |
| **File Storage** | Object storage, signed URLs, multipart upload | S3, GCS, MinIO |
| **Search** | Full-text search, faceted search, autocomplete | Elasticsearch, Typesense, Meilisearch |
| **Webhooks** | Event-driven integrations, retry logic, HMAC verification | Custom, Svix, Hookdeck |

#### Database Aspects
| Aspect | Key Concepts | Industry Standard |
|---|---|---|
| **Schema Design** | Normalization (1NF-BCNF), denormalization, ER modeling | dbdiagram.io, DataGrip |
| **Indexing** | B-Tree, Hash, GIN, GiST, covering indexes, composite indexes | `EXPLAIN ANALYZE` |
| **Query Optimization** | Query plans, N+1 problem, join optimization, query caching | pg_stat_statements, slow query log |
| **Migrations** | Version-controlled schema changes, expand-contract pattern | Flyway, Liquibase, Alembic |
| **Connection Pooling** | Reuse connections, prevent exhaustion | HikariCP, PgBouncer |
| **Replication** | Leader-follower, multi-leader, read replicas | PostgreSQL streaming replication |
| **Sharding** | Horizontal partitioning by key, consistent hashing | Vitess, Citus, application-level |
| **Backup & Recovery** | Point-in-time recovery, WAL archiving, disaster recovery | pg_dump, pg_basebackup, AWS RDS |
| **Data Modeling** | Relational, document, graph, time-series, wide-column | Choose based on access patterns |
| **ACID vs BASE** | Strong consistency vs eventual consistency | RDBMS (ACID) vs NoSQL (BASE) |
| **ORM vs Raw SQL** | Type-safe queries, migration generation, performance | Hibernate, SQLAlchemy, Prisma, JOOQ |

### Multithreading & Concurrency
| Resource | URL | Best For |
|---|---|---|
| **Jenkov Concurrency** | `https://jenkov.com/tutorials/java-concurrency/index.html` | Deep Java concurrency tutorial |
| **Baeldung Concurrency** | `https://www.baeldung.com/java-concurrency` | Practical concurrency guides |
| **LMAX Disruptor** | `https://lmax-exchange.github.io/disruptor/` | High-performance concurrent patterns |
| **deadlocks.dev** | `https://deadlocks.dev/` | Concurrency puzzles and challenges |

#### Concurrency Concepts Map
```
Threads → Runnable/Callable → Thread Lifecycle → Synchronization →
Locks (ReentrantLock, ReadWriteLock, StampedLock) →
Atomic Variables → Volatile → Happens-Before →
Executors → Thread Pools → CompletableFuture →
Fork/Join → Parallel Streams → Virtual Threads (Project Loom) →
Producer-Consumer → Readers-Writers → Dining Philosophers →
Lock-Free Data Structures → CAS Operations
```

---

## Distributed Systems

### Books
| Book | Author | Core Topics |
|---|---|---|
| **Designing Data-Intensive Applications** | Martin Kleppmann | Replication, partitioning, consistency, stream processing |
| **Distributed Systems (3rd Ed.)** | Tanenbaum & Van Steen | Architectures, processes, communication, naming, consistency |
| **Understanding Distributed Systems** | Roberto Vitillo | Practical guide, communication, coordination, scalability |
| **Database Internals** | Alex Petrov | Storage engines, distributed transactions, consensus |

### Online Resources
| Resource | URL | Best For |
|---|---|---|
| **Distributed Systems for Fun and Profit** | `http://book.mixu.net/distsys/` | Free, concise distributed systems intro |
| **MIT 6.824 (Distributed Systems)** | `https://pdos.csail.mit.edu/6.824/` | Academic course with labs (Raft, MapReduce) |
| **Jepsen** | `https://jepsen.io/` | Distributed systems correctness testing |
| **The Morning Paper** | `https://blog.acolyer.org/` | CS paper summaries (archived, still valuable) |
| **Martin Kleppmann's Blog** | `https://martin.kleppmann.com/` | Distributed data, CRDTs, formal verification |

### Replication Topologies
```
Single-Leader (Master-Slave / Primary-Replica)
────────────────────────────────────────────
  Write → [Leader] → replicates to → [Follower 1]
                                    → [Follower 2]
                                    → [Follower N]
  Read  ← [any node]

  ✅ Simple, strong consistency on leader
  ❌ Leader is SPOF, writes don't scale
  📌 Used by: PostgreSQL streaming replication, MySQL

Multi-Leader (Master-Master / Active-Active)
────────────────────────────────────────────
  Write → [Leader A] ←→ sync ←→ [Leader B]
  Read  ← [any leader]

  ✅ Writes scale, geo-distributed writes
  ❌ Conflict resolution is hard
  📌 Used by: CouchDB, multi-region setups

Leaderless (Peer-to-Peer / Dynamo-style)
────────────────────────────────────────
  Write → [Node A, Node B, Node C]   (W quorum)
  Read  ← [Node A, Node B, Node C]   (R quorum)
  Rule:   W + R > N  for consistency

  ✅ High availability, no SPOF
  ❌ Eventual consistency, read repair needed
  📌 Used by: Cassandra, DynamoDB, Riak
```

### Consensus Algorithms
| Algorithm | Approach | Used By |
|---|---|---|
| **Raft** | Leader-based, understandable | etcd, CockroachDB, Consul |
| **Paxos** | Quorum-based, theoretical foundation | Chubby (Google), original academia |
| **Multi-Paxos** | Paxos optimized for repeated decisions | Spanner (Google) |
| **ZAB** | Zookeeper Atomic Broadcast | Apache ZooKeeper |
| **PBFT** | Byzantine fault tolerant | Blockchain, adversarial environments |
| **Viewstamped Replication** | View-change based consensus | Research, some production systems |

### Consistency Models
```
Strong Consistency
  └── Linearizability — operations appear instantaneous, globally ordered
  └── Sequential Consistency — all processes see same order

Weak Consistency
  └── Eventual Consistency — all replicas converge eventually
  └── Causal Consistency — causally related ops in order, concurrent ops unordered
  └── Read-Your-Writes — a client always sees its own writes
  └── Monotonic Reads — a client never sees older data after seeing newer data
```

### Distributed Systems Concepts Map
```
Communication:
  RPC → REST → gRPC → Message Queues → Event Streaming →
  Pub/Sub → Request-Reply → Async Messaging

Replication:
  Single-Leader → Multi-Leader → Leaderless →
  Synchronous vs Async → Quorum (W + R > N) →
  Chain Replication → Log Replication

Consistency & Ordering:
  Linearizability → Sequential → Causal → Eventual →
  Vector Clocks → Lamport Timestamps → Happens-Before

Fault Tolerance:
  Failure Detection (heartbeats, φ accrual) →
  Leader Election → Fencing → Split-Brain Prevention →
  Circuit Breaker → Bulkhead → Retry with Backoff

Coordination:
  Consensus (Raft/Paxos) → Distributed Locks →
  Leader Election → Barrier → Two-Phase Commit (2PC) →
  Saga Pattern → Outbox Pattern

Partitioning (Sharding):
  Hash Partitioning → Range Partitioning →
  Consistent Hashing → Virtual Nodes →
  Rebalancing → Hot Spots
```

### Key Theorems & Trade-offs
| Theorem | Statement | Implication |
|---|---|---|
| **CAP** | Choose 2 of: Consistency, Availability, Partition tolerance | In network partitions, choose C or A |
| **PACELC** | If Partition → choose A or C; Else → choose Latency or Consistency | Extends CAP to normal operation trade-offs |
| **FLP Impossibility** | No deterministic consensus in async systems with even 1 failure | Consensus algorithms use timeouts/randomization |
| **Two Generals** | Cannot guarantee agreement over unreliable channel | At-least-once / idempotency needed |
| **Byzantine Generals** | Agreement possible if > 2/3 nodes are honest | Foundation for BFT algorithms |

---

## DevOps, CI/CD & Infrastructure

### CI/CD Tools
| Tool | Type | Best For | Docs |
|---|---|---|---|
| **GitHub Actions** | Cloud-native CI/CD | GitHub-hosted projects, full workflow automation | `https://docs.github.com/en/actions` |
| **Jenkins** | Self-hosted CI/CD | Enterprise pipelines, extensive plugin ecosystem | `https://www.jenkins.io/doc/` |
| **GitLab CI/CD** | Integrated CI/CD | GitLab-hosted projects, built-in registry & security | `https://docs.gitlab.com/ee/ci/` |
| **CircleCI** | Cloud CI/CD | Fast builds, Docker-first, config-as-code | `https://circleci.com/docs/` |
| **Travis CI** | Cloud CI/CD | Open-source projects, simple YAML config | `https://docs.travis-ci.com/` |
| **Azure DevOps Pipelines** | Cloud CI/CD | Microsoft ecosystem, multi-stage pipelines | `https://learn.microsoft.com/en-us/azure/devops/pipelines/` |
| **ArgoCD** | GitOps CD | Kubernetes-native continuous delivery | `https://argo-cd.readthedocs.io/` |
| **Tekton** | Cloud-native CI/CD | Kubernetes-native pipeline resources | `https://tekton.dev/docs/` |
| **Drone CI** | Container-native CI | Docker-based pipelines, YAML config | `https://docs.drone.io/` |
| **Spinnaker** | Multi-cloud CD | Netflix's open-source CD platform, canary deployments | `https://spinnaker.io/docs/` |

#### CI/CD Pipeline — Anatomy of a Modern Pipeline
```
┌─────────────────────────────────────────────────────────────────────────┐
│                         CI/CD PIPELINE                                  │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│  Source        Build          Test           Deploy         Monitor      │
│  ┌───────┐   ┌──────────┐   ┌───────────┐  ┌───────────┐  ┌─────────┐ │
│  │ Git   │──→│ Compile  │──→│ Unit      │──→│ Staging   │──→│ Metrics │ │
│  │ Push  │   │ Lint     │   │ Integ     │  │ Canary    │  │ Alerts  │ │
│  │ PR    │   │ SAST     │   │ E2E       │  │ Blue/Green│  │ Logs    │ │
│  │ Tag   │   │ Build    │   │ Perf      │  │ Rolling   │  │ Traces  │ │
│  └───────┘   │ Image    │   │ Security  │  │ Prod      │  └─────────┘ │
│              └──────────┘   └───────────┘  └───────────┘               │
│                                                                         │
│  Triggers:  push, PR, cron, tag, manual, webhook, API                  │
│  Artifacts: JAR, Docker image, Helm chart, binary, report              │
│  Gates:     approval, tests passing, coverage threshold, security scan │
└─────────────────────────────────────────────────────────────────────────┘
```

#### CI/CD Stages Deep Dive
| Stage | What Happens | Tools | Gate/Criteria |
|---|---|---|---|
| **Source** | Code pushed, PR created, tag pushed | Git, GitHub/GitLab webhooks | Branch protection rules |
| **Lint & Format** | Code style enforcement | ESLint, Prettier, Checkstyle, Spotless, Black | Zero violations |
| **SAST (Static Analysis)** | Find vulnerabilities in code | SonarQube, Semgrep, CodeQL, SpotBugs | No critical/high findings |
| **Build** | Compile, resolve deps, create artifact | Maven, Gradle, npm, Docker | Build succeeds |
| **Unit Test** | Test individual units in isolation | JUnit, pytest, Jest, Go testing | >80% coverage, all pass |
| **Integration Test** | Test component interactions | Testcontainers, WireMock | All pass |
| **SCA (Dependency Scan)** | Check deps for known CVEs | Dependabot, Snyk, Trivy, OWASP Dep-Check | No critical CVEs |
| **Container Build** | Build & push Docker image | Docker, Buildah, Kaniko | Image built, scanned |
| **Container Scan** | Scan image for vulnerabilities | Trivy, Grype, Clair | No critical CVEs |
| **Deploy to Staging** | Deploy to non-prod environment | Helm, ArgoCD, Terraform, kubectl | Health checks pass |
| **E2E / Smoke Test** | Full workflow testing in staging | Cypress, Playwright, Selenium, Postman | Critical paths pass |
| **Performance Test** | Load & stress testing | k6, Gatling, JMeter, Locust | Latency < threshold, no errors |
| **Approval Gate** | Human review before production | Manual approval, policy engines | Approved by reviewer |
| **Deploy to Production** | Prod deployment (canary/blue-green/rolling) | ArgoCD, Spinnaker, Flux, Helm | Health checks, rollback ready |
| **Post-Deploy Verify** | Verify production health | Synthetic checks, Datadog, Grafana | SLO metrics within bounds |
| **Notify** | Alert team of deployment | Slack, Teams, PagerDuty, email | — |

#### GitHub Actions — Example Workflow
```yaml
# .github/workflows/ci.yml
name: CI Pipeline
on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '21'
          distribution: 'temurin'
      - name: Build with Gradle
        run: ./gradlew build
      - name: Run tests
        run: ./gradlew test
      - name: Upload test report
        uses: actions/upload-artifact@v4
        if: always()
        with:
          name: test-report
          path: build/reports/tests/

  docker:
    needs: build
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Build Docker image
        run: docker build -t myapp:${{ github.sha }} .
      - name: Push to GHCR
        run: |
          echo "${{ secrets.GITHUB_TOKEN }}" | docker login ghcr.io -u ${{ github.actor }} --password-stdin
          docker push ghcr.io/${{ github.repository }}:${{ github.sha }}
```

#### Deployment Strategies
| Strategy | How It Works | Downtime | Rollback | Risk | Used By |
|---|---|---|---|---|---|
| **Recreate** | Stop old → start new | ✅ Yes | Slow (redeploy) | High | Dev/test environments only |
| **Rolling Update** | Replace instances one-by-one | ❌ Zero | Medium | Low-medium | Kubernetes default, AWS ECS |
| **Blue/Green** | Two identical envs; switch traffic | ❌ Zero | Instant (switch back) | Low | Netflix, Amazon, banks |
| **Canary** | Route small % of traffic to new version | ❌ Zero | Instant (route back) | Very low | Google, Netflix, Spotify |
| **A/B Testing** | Route by user segment (feature flags) | ❌ Zero | Instant (flag toggle) | Very low | Facebook, LinkedIn, Uber |
| **Shadow (Dark Launch)** | Mirror traffic to new version (no user impact) | ❌ Zero | N/A (read-only) | None | Twitter, Divert traffic for comparison |
| **Feature Flags** | Deploy code, toggle feature on/off separately | ❌ Zero | Instant (toggle off) | Very low | LaunchDarkly, Unleash, all FAANG |

#### Feature Flags — Industry Standard
```
Deployment ≠ Release

Deploy: Code is on production servers
Release: Feature is visible to users

Feature flags decouple deployment from release:
  - Deploy with flag OFF → flag ON for internal → flag ON for 1% → 10% → 100%
  - Instant rollback: just toggle flag OFF
  - A/B testing: flag ON for segment A, OFF for segment B

Tools: LaunchDarkly, Unleash, Flagsmith, AWS AppConfig, Flipt
Industry: All major tech companies use feature flags extensively
```

#### GitOps — Declarative Infrastructure
```
GitOps Principles:
  1. Declarative — desired state described in Git (YAML, HCL)
  2. Versioned — all changes are Git commits (auditable)
  3. Automated — agents pull desired state and apply it
  4. Self-healing — drift detected and corrected automatically

Flow:
  Developer → Git commit → GitOps agent detects change →
  Agent applies change to cluster → Cluster converges to desired state

Tools:
  ArgoCD       — most popular K8s GitOps tool
  Flux         — CNCF graduated GitOps tool
  Crossplane   — GitOps for cloud infrastructure
```

### Containers & Orchestration
| Tool | Purpose | Docs |
|---|---|---|
| **Docker** | Container runtime — package apps with dependencies | `https://docs.docker.com/` |
| **Docker Compose** | Multi-container local environments | `https://docs.docker.com/compose/` |
| **Kubernetes (K8s)** | Container orchestration — scheduling, scaling, self-healing | `https://kubernetes.io/docs/` |
| **Helm** | K8s package manager — templated deployments | `https://helm.sh/docs/` |
| **Podman** | Rootless container runtime (Docker alternative) | `https://podman.io/docs` |
| **Containerd** | Industry-standard container runtime (powers Docker & K8s) | `https://containerd.io/docs/` |

#### Docker — Deep Dive

##### Docker Essential Commands
| Command | Purpose |
|---|---|
| `docker build -t myapp:1.0 .` | Build image from Dockerfile |
| `docker run -d -p 8080:8080 myapp:1.0` | Run container (detached, port mapping) |
| `docker run --rm -it myapp:1.0 /bin/sh` | Interactive shell in container |
| `docker ps` | List running containers |
| `docker ps -a` | List all containers (including stopped) |
| `docker logs -f <container>` | Follow container logs |
| `docker exec -it <container> /bin/sh` | Shell into running container |
| `docker stop <container>` | Graceful stop (SIGTERM → SIGKILL) |
| `docker rm $(docker ps -aq)` | Remove all stopped containers |
| `docker images` | List local images |
| `docker rmi <image>` | Remove image |
| `docker system prune -a` | Remove all unused data (images, containers, volumes) |
| `docker network create mynet` | Create custom network |
| `docker volume create mydata` | Create named volume |
| `docker inspect <container>` | Show low-level container info |
| `docker stats` | Live resource usage (CPU, memory, I/O) |
| `docker compose up -d` | Start multi-container app |
| `docker compose down -v` | Stop and remove containers + volumes |
| `docker compose logs -f service` | Follow logs for specific service |

##### Dockerfile Best Practices
```dockerfile
# 1. Use specific base image tags (never :latest in production)
FROM eclipse-temurin:21-jre-alpine AS runtime

# 2. Multi-stage builds (keep final image small)
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
RUN ./gradlew dependencies --no-daemon    # Cache dependencies layer
COPY src ./src
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

# 3. Run as non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# 4. Health check
HEALTHCHECK --interval=30s --timeout=3s CMD wget -qO- http://localhost:8080/actuator/health || exit 1

# 5. Use ENTRYPOINT for the main process
ENTRYPOINT ["java", "-jar", "app.jar"]
```

##### Docker Compose Example
```yaml
# docker-compose.yml
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=dev
      - DB_HOST=postgres
    depends_on:
      postgres:
        condition: service_healthy
    networks: [backend]

  postgres:
    image: postgres:16-alpine
    environment:
      POSTGRES_DB: myapp
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: secret
    volumes:
      - pgdata:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U admin"]
      interval: 10s
      timeout: 5s
      retries: 5
    networks: [backend]

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
    networks: [backend]

volumes:
  pgdata:

networks:
  backend:
```

##### Docker Image Optimization
| Technique | Before | After | Impact |
|---|---|---|---|
| **Alpine base** | `eclipse-temurin:21-jdk` (400MB) | `eclipse-temurin:21-jre-alpine` (80MB) | 5x smaller |
| **Multi-stage build** | Build tools in final image | Only runtime in final image | Much smaller, more secure |
| **Layer caching** | COPY all files, then build | COPY deps first, then source | Faster rebuilds |
| **`.dockerignore`** | Copies `.git`, `node_modules` | Excludes unnecessary files | Faster builds |
| **Distroless images** | Alpine (has shell) | `gcr.io/distroless/java21` | No shell = smaller attack surface |

#### Kubernetes — Deep Dive

##### Kubernetes Architecture
```
┌─────────────────────────────────────────────────────────┐
│                    CONTROL PLANE                         │
│  ┌──────────────┐  ┌───────────┐  ┌──────────────────┐ │
│  │ API Server   │  │ Scheduler │  │ Controller Mgr   │ │
│  │ (kube-apiserver)│ (kube-scheduler)│ (kube-controller-manager)│ │
│  └──────────────┘  └───────────┘  └──────────────────┘ │
│  ┌──────────────┐                                       │
│  │ etcd         │  ← distributed key-value store        │
│  └──────────────┘    (source of truth for cluster state) │
└─────────────────────────────────────────────────────────┘
         │
┌─────────────────────────────────────────────────────────┐
│                    WORKER NODES                          │
│  ┌──────────────┐  ┌───────────┐  ┌──────────────────┐ │
│  │ kubelet      │  │ kube-proxy│  │ container runtime│ │
│  │ (node agent) │  │ (networking)  │ (containerd/CRI-O)│ │
│  └──────────────┘  └───────────┘  └──────────────────┘ │
│                                                         │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐                  │
│  │  Pod 1  │ │  Pod 2  │ │  Pod 3  │  ← workloads     │
│  └─────────┘ └─────────┘ └─────────┘                  │
└─────────────────────────────────────────────────────────┘
```

##### Kubernetes Essential Commands (kubectl)
| Command | Purpose |
|---|---|
| `kubectl get pods` | List pods in current namespace |
| `kubectl get pods -A` | List pods across all namespaces |
| `kubectl get all` | List all resources |
| `kubectl describe pod <name>` | Detailed pod info (events, conditions) |
| `kubectl logs <pod> -f` | Follow pod logs |
| `kubectl logs <pod> -c <container>` | Logs from specific container |
| `kubectl exec -it <pod> -- /bin/sh` | Shell into pod |
| `kubectl apply -f manifest.yaml` | Apply manifest (create/update) |
| `kubectl delete -f manifest.yaml` | Delete resources from manifest |
| `kubectl rollout status deployment/<name>` | Watch deployment rollout |
| `kubectl rollout undo deployment/<name>` | Rollback to previous version |
| `kubectl scale deployment <name> --replicas=5` | Scale deployment |
| `kubectl top pods` | Resource usage per pod |
| `kubectl port-forward svc/<name> 8080:80` | Forward local port to service |
| `kubectl get events --sort-by=.metadata.creationTimestamp` | Recent cluster events |
| `kubectl config get-contexts` | List available clusters |
| `kubectl config use-context <name>` | Switch cluster context |

##### Kubernetes Resource Types
| Resource | Purpose | Controller |
|---|---|---|
| **Pod** | Smallest deployable unit (1+ containers) | — |
| **ReplicaSet** | Ensures N pod replicas running | ReplicaSet controller |
| **Deployment** | Declarative pod management + rolling updates | Deployment controller |
| **StatefulSet** | Stateful apps (databases) — stable network IDs, persistent storage | StatefulSet controller |
| **DaemonSet** | One pod per node (log collectors, monitoring agents) | DaemonSet controller |
| **Job** | Run-to-completion workload (batch processing) | Job controller |
| **CronJob** | Scheduled Jobs (backups, reports) | CronJob controller |
| **Service** | Stable network endpoint for pods (ClusterIP, NodePort, LoadBalancer) | — |
| **Ingress** | HTTP/HTTPS routing rules (domain → service) | Ingress controller (nginx, Traefik) |
| **ConfigMap** | Non-sensitive configuration data | — |
| **Secret** | Sensitive data (passwords, tokens, certs) — base64 encoded | — |
| **PersistentVolume (PV)** | Cluster-level storage resource | — |
| **PersistentVolumeClaim (PVC)** | Pod's request for storage | — |
| **HorizontalPodAutoscaler (HPA)** | Auto-scale pods based on CPU/memory/custom metrics | HPA controller |
| **NetworkPolicy** | Pod-level firewall rules | Network plugin (Calico, Cilium) |
| **ServiceAccount** | Identity for pods (RBAC) | — |
| **Namespace** | Virtual cluster isolation | — |

##### Kubernetes Concepts Map (Expanded)
```
Pod → ReplicaSet → Deployment → Service → Ingress →
ConfigMap → Secret → PersistentVolume → StatefulSet →
DaemonSet → Job/CronJob → HPA → VPA → PDB →
Namespace → RBAC (Role/ClusterRole/Binding) →
NetworkPolicy → ResourceQuota → LimitRange →
Helm Chart → Kustomize → Operator → CRD →
Service Mesh (Istio/Linkerd) → Gateway API →
Cert-Manager → External-DNS → Sealed Secrets
```

### Infrastructure as Code (IaC)
| Tool | Type | Docs |
|---|---|---|
| **Terraform** | Declarative IaC — cloud-agnostic provisioning | `https://developer.hashicorp.com/terraform/docs` |
| **Ansible** | Configuration management — agentless, YAML playbooks | `https://docs.ansible.com/` |
| **Pulumi** | IaC using real programming languages (TypeScript, Python, Go) | `https://www.pulumi.com/docs/` |
| **CloudFormation** | AWS-native IaC — JSON/YAML templates | `https://docs.aws.amazon.com/cloudformation/` |
| **Chef / Puppet** | Configuration management — agent-based | `https://docs.chef.io/` / `https://www.puppet.com/docs` |

### Cloud Platforms
| Platform | Docs | Key Services |
|---|---|---|
| **AWS** | `https://docs.aws.amazon.com/` | EC2, S3, RDS, Lambda, ECS/EKS, SQS, SNS, DynamoDB |
| **Google Cloud (GCP)** | `https://cloud.google.com/docs` | Compute Engine, GKE, Cloud Run, BigQuery, Pub/Sub |
| **Microsoft Azure** | `https://learn.microsoft.com/en-us/azure/` | VMs, AKS, App Service, Cosmos DB, Service Bus |
| **DigitalOcean** | `https://docs.digitalocean.com/` | Droplets, K8s, App Platform — simpler, dev-friendly |
| **Vercel / Netlify** | `https://vercel.com/docs` | Frontend/JAMstack deployment, serverless functions |

### Monitoring & Observability
| Tool | Purpose | Docs |
|---|---|---|
| **Prometheus** | Metrics collection & alerting (pull-based) | `https://prometheus.io/docs/` |
| **Grafana** | Visualization dashboards (works with Prometheus, etc.) | `https://grafana.com/docs/` |
| **ELK Stack** | Elasticsearch + Logstash + Kibana — log aggregation | `https://www.elastic.co/guide/` |
| **Jaeger / Zipkin** | Distributed tracing | `https://www.jaegertracing.io/docs/` |
| **Datadog** | Full-stack monitoring (commercial) | `https://docs.datadoghq.com/` |
| **PagerDuty** | Incident management & on-call scheduling | `https://support.pagerduty.com/docs` |
| **OpenTelemetry** | Vendor-neutral observability framework (traces, metrics, logs) | `https://opentelemetry.io/docs/` |

#### Observability Pillars
```
Logs   — What happened? (text records of events)
Metrics — How much? How fast? (numerical measurements over time)
Traces  — How did a request flow through services? (distributed context)
```

### Version Control & Collaboration

#### VCS Tools
| Tool | Purpose | Docs |
|---|---|---|
| **Git** | Distributed version control | `https://git-scm.com/doc` |
| **GitHub** | Hosting + collaboration + CI/CD + code review | `https://docs.github.com/` |
| **GitLab** | Hosting + full DevOps platform | `https://docs.gitlab.com/` |
| **Bitbucket** | Hosting + Jira integration | `https://support.atlassian.com/bitbucket-cloud/` |

#### Git — Comprehensive Command Reference

##### Setup & Configuration
| Command | Purpose |
|---|---|
| `git config --global user.name "Name"` | Set author name |
| `git config --global user.email "email"` | Set author email |
| `git config --global core.editor "code --wait"` | Set VS Code as default editor |
| `git config --global init.defaultBranch main` | Set default branch name |
| `git config --global pull.rebase true` | Default pull to rebase (cleaner history) |
| `git config --global rerere.enabled true` | Remember merge conflict resolutions |
| `git config --list --show-origin` | Show all configs and where they're set |

##### Daily Workflow Commands
| Command | Purpose |
|---|---|
| `git init` | Initialize new repository |
| `git clone <url>` | Clone remote repository |
| `git status` | Show working tree status |
| `git add <file>` | Stage specific file |
| `git add -A` | Stage all changes (new, modified, deleted) |
| `git add -p` | Interactive staging (choose hunks) — great for clean commits |
| `git commit -m "message"` | Commit staged changes |
| `git commit --amend` | Modify last commit (message or content) |
| `git commit --amend --no-edit` | Add staged changes to last commit silently |
| `git push` | Push to remote |
| `git push -u origin branch-name` | Push and set upstream tracking |
| `git push --force-with-lease` | Safe force push (fails if remote has new commits) |
| `git pull` | Fetch + merge from remote |
| `git pull --rebase` | Fetch + rebase (cleaner than merge) |
| `git fetch --all --prune` | Fetch all remotes, remove deleted remote branches |

##### Branching & Merging
| Command | Purpose |
|---|---|
| `git branch` | List local branches |
| `git branch -a` | List all branches (local + remote) |
| `git branch feature/xyz` | Create new branch |
| `git checkout -b feature/xyz` | Create and switch to new branch |
| `git switch -c feature/xyz` | Modern equivalent of checkout -b |
| `git merge feature/xyz` | Merge branch into current |
| `git merge --no-ff feature/xyz` | Merge with explicit merge commit (recommended) |
| `git rebase main` | Rebase current branch onto main |
| `git rebase -i HEAD~3` | Interactive rebase last 3 commits (squash, reorder, edit) |
| `git cherry-pick <commit-hash>` | Apply specific commit to current branch |
| `git branch -d feature/xyz` | Delete branch (safe — only if merged) |
| `git branch -D feature/xyz` | Force delete branch |
| `git push origin --delete branch-name` | Delete remote branch |

##### Stashing
| Command | Purpose |
|---|---|
| `git stash` | Stash working changes |
| `git stash push -m "description"` | Stash with message |
| `git stash list` | List all stashes |
| `git stash pop` | Apply most recent stash and remove it |
| `git stash apply stash@{2}` | Apply specific stash (keep it) |
| `git stash drop stash@{0}` | Delete specific stash |
| `git stash clear` | Delete all stashes |
| `git stash -u` | Include untracked files |
| `git stash branch new-branch` | Create branch from stash |

##### History & Inspection
| Command | Purpose |
|---|---|
| `git log --oneline --graph --all` | Visual commit graph |
| `git log --author="name"` | Filter by author |
| `git log --since="2 weeks ago"` | Filter by date |
| `git log -p <file>` | Show patches for a file |
| `git log --stat` | Show file change stats per commit |
| `git diff` | Show unstaged changes |
| `git diff --staged` | Show staged changes |
| `git diff main..feature` | Compare two branches |
| `git show <commit>` | Show commit details |
| `git blame <file>` | Show who changed each line |
| `git reflog` | Show all HEAD movements (recover lost commits) |
| `git bisect start` | Binary search for bug-introducing commit |
| `git bisect good/bad` | Mark commits during bisect |
| `git shortlog -sn` | Commit count by author |

##### Undoing & Recovery
| Command | Purpose | Danger Level |
|---|---|---|
| `git checkout -- <file>` | Discard working directory changes | ⚠️ Destructive |
| `git restore <file>` | Modern equivalent of checkout -- | ⚠️ Destructive |
| `git restore --staged <file>` | Unstage file (keep changes) | Safe |
| `git reset HEAD~1` | Undo last commit (keep changes staged) | Safe |
| `git reset --soft HEAD~1` | Undo last commit (keep changes staged) | Safe |
| `git reset --mixed HEAD~1` | Undo last commit (keep changes unstaged) | Safe |
| `git reset --hard HEAD~1` | Undo last commit and discard changes | 🔴 Destructive |
| `git revert <commit>` | Create new commit that undoes a commit | Safe (doesn't rewrite history) |
| `git clean -fd` | Remove untracked files and directories | ⚠️ Destructive |
| `git reflog + git reset --hard <hash>` | Recover "lost" commits | Recovery tool |

##### Tags
| Command | Purpose |
|---|---|
| `git tag v1.0.0` | Lightweight tag |
| `git tag -a v1.0.0 -m "Release 1.0"` | Annotated tag (recommended) |
| `git push origin v1.0.0` | Push specific tag |
| `git push origin --tags` | Push all tags |
| `git tag -d v1.0.0` | Delete local tag |
| `git push origin :refs/tags/v1.0.0` | Delete remote tag |

##### Submodules & Subtrees
| Command | Purpose |
|---|---|
| `git submodule add <url> path/` | Add submodule |
| `git submodule update --init --recursive` | Initialize and update all submodules |
| `git subtree add --prefix=lib <url> main` | Add subtree (simpler alternative to submodules) |

#### Git Branching Strategies
| Strategy | Flow | Best For | Used By |
|---|---|---|---|
| **Git Flow** | `main` ← `develop` ← `feature/*`, `release/*`, `hotfix/*` | Large teams, release cycles | Enterprise, open source with versioned releases |
| **GitHub Flow** | `main` ← `feature/*` (PR-based, deploy from main) | Continuous deployment, small teams | GitHub, most SaaS startups |
| **GitLab Flow** | `main` ← `feature/*` + environment branches (`staging`, `production`) | Multi-environment deployments | GitLab, teams with staging env |
| **Trunk-Based** | Direct commits to `main` with short-lived feature branches (< 1 day) | High-performing teams, CI/CD | Google, Facebook, Netflix |
| **Ship/Show/Ask** | Ship (merge w/o review), Show (merge + notify), Ask (PR for review) | Mixed-experience teams | Community projects |

##### Git Flow Diagram
```
main ─────●─────────────────●────────── (releases only)
           \               /
develop ────●──●──●──●──●──●──●──── (integration branch)
              \   /     \  /
feature/x ─────●─●       ●──● feature/y
                      \
hotfix/z ──────────────●──── (emergency fix to main)
```

##### Trunk-Based Development
```
main ──●──●──●──●──●──●──●──●──●──── (always deployable)
        \  /     \  /
  feat-a ●       feat-b ●   (short-lived, < 1 day)
                               ↓
                Feature Flags for in-progress features
```

#### Git Best Practices
| Practice | Why |
|---|---|
| **Write meaningful commit messages** | `fix: resolve NPE in UserService.findById()` > `fix bug` |
| **One logical change per commit** | Makes reverts, bisects, and reviews easier |
| **Use conventional commits** | `feat:`, `fix:`, `docs:`, `refactor:`, `test:`, `chore:` — enables auto-changelogs |
| **Rebase before merging** | Keeps history linear and clean |
| **Never force-push to shared branches** | Use `--force-with-lease` if you must |
| **Use `.gitignore` from day one** | Avoid committing build artifacts, secrets, IDE configs |
| **Sign commits with GPG** | Verify authorship (`git commit -S`) |
| **Protect `main` branch** | Require PR reviews, CI passing, no direct pushes |
| **Squash WIP commits** | `git rebase -i` to clean up before merge |
| **Tag releases semantically** | `v1.2.3` = MAJOR.MINOR.PATCH (SemVer) |

#### Semantic Versioning (SemVer)
```
MAJOR.MINOR.PATCH  (e.g., 2.4.1)

MAJOR  — Breaking changes (incompatible API changes)
MINOR  — New features (backwards compatible)
PATCH  — Bug fixes (backwards compatible)

Pre-release: 1.0.0-alpha.1, 1.0.0-beta.2, 1.0.0-rc.1
Build metadata: 1.0.0+build.123

Examples:
  0.x.x     — Initial development (anything may change)
  1.0.0     — First stable release (public API declared)
  1.1.0     — New feature added (backwards compatible)
  1.1.1     — Bug fix
  2.0.0     — Breaking change introduced
```

#### Git Internals — How Git Actually Works
```
Git Object Model:
  blob    — file content (just the bytes, no filename)
  tree    — directory listing (maps names → blobs/trees)
  commit  — snapshot + metadata (parent, author, message, tree pointer)
  tag     — named reference to a commit (annotated tags have their own object)

Storage:
  .git/objects/    — all objects (compressed, content-addressed by SHA-1/SHA-256)
  .git/refs/       — branch & tag pointers
  .git/HEAD        — current branch pointer
  .git/index       — staging area (next commit snapshot)

Key Insight: branches are just pointers (40-char SHA references)
  - Creating a branch = writing 41 bytes to a file
  - Switching branches = updating HEAD + checking out files
  - Merging = creating a commit with two parents
```

---

## Frameworks & Tech Stacks

### Backend Frameworks
| Framework | Language | Type | Docs |
|---|---|---|---|
| **Spring Boot** | Java | Full-featured, enterprise | `https://docs.spring.io/spring-boot/docs/current/reference/html/` |
| **Quarkus** | Java | Cloud-native, fast startup | `https://quarkus.io/guides/` |
| **Micronaut** | Java | Lightweight, compile-time DI | `https://docs.micronaut.io/` |
| **Django** | Python | Batteries-included, ORM, admin | `https://docs.djangoproject.com/` |
| **FastAPI** | Python | Modern, async, type hints, OpenAPI | `https://fastapi.tiangolo.com/` |
| **Flask** | Python | Minimal, flexible | `https://flask.palletsprojects.com/` |
| **Express.js** | Node.js | Minimal, widely used | `https://expressjs.com/` |
| **NestJS** | Node.js/TS | Structured, Angular-inspired | `https://docs.nestjs.com/` |
| **Gin** | Go | Lightweight, high-perf | `https://gin-gonic.com/docs/` |
| **Actix Web** | Rust | High-performance, actor-based | `https://actix.rs/docs/` |
| **ASP.NET Core** | C# | Microsoft ecosystem, cross-platform | `https://learn.microsoft.com/en-us/aspnet/core/` |
| **Ruby on Rails** | Ruby | Convention over configuration | `https://guides.rubyonrails.org/` |

### Frontend Frameworks
| Framework | Language | Docs |
|---|---|---|
| **React** | JavaScript/TSX | `https://react.dev/` |
| **Vue.js** | JavaScript/SFC | `https://vuejs.org/guide/` |
| **Angular** | TypeScript | `https://angular.dev/` |
| **Svelte / SvelteKit** | JavaScript | `https://svelte.dev/docs/` |
| **Next.js** | React/SSR | `https://nextjs.org/docs` |
| **Nuxt** | Vue/SSR | `https://nuxt.com/docs` |

### Databases
| Database | Type | Best For | Docs |
|---|---|---|---|
| **PostgreSQL** | Relational | General purpose, advanced features | `https://www.postgresql.org/docs/` |
| **MySQL** | Relational | Web apps, widely deployed | `https://dev.mysql.com/doc/` |
| **MongoDB** | Document | Flexible schema, rapid prototyping | `https://www.mongodb.com/docs/` |
| **Redis** | Key-Value / Cache | Caching, sessions, pub/sub | `https://redis.io/docs/` |
| **Cassandra** | Wide-Column | Write-heavy, high availability | `https://cassandra.apache.org/doc/` |
| **Neo4j** | Graph | Relationship-heavy data | `https://neo4j.com/docs/` |
| **DynamoDB** | Key-Value (AWS) | Serverless, auto-scaling | `https://docs.aws.amazon.com/dynamodb/` |
| **SQLite** | Embedded Relational | Local/embedded apps, testing | `https://www.sqlite.org/docs.html` |

### Message Brokers & Queues
| System | Type | Docs |
|---|---|---|
| **Apache Kafka** | Distributed event streaming | `https://kafka.apache.org/documentation/` |
| **RabbitMQ** | Message broker (AMQP) | `https://www.rabbitmq.com/docs` |
| **Apache Pulsar** | Multi-tenant messaging | `https://pulsar.apache.org/docs/` |
| **AWS SQS / SNS** | Managed queue / pub-sub | `https://docs.aws.amazon.com/sqs/` |
| **NATS** | Lightweight messaging | `https://docs.nats.io/` |

### Build Tools

#### Build Tools Overview
| Tool | Language/Ecosystem | Type | Docs |
|---|---|---|---|
| **Maven** | Java/JVM | Declarative (XML — `pom.xml`) | `https://maven.apache.org/guides/` |
| **Gradle** | Java/Kotlin/Android | Hybrid (Groovy/Kotlin DSL — `build.gradle`) | `https://docs.gradle.org/` |
| **Ant** | Java (legacy) | Imperative (XML — `build.xml`) | `https://ant.apache.org/manual/` |
| **npm / pnpm / yarn** | JavaScript/Node.js | Package manager + scripts | `https://docs.npmjs.com/` |
| **pip / Poetry / uv** | Python | Package manager + virtualenv | `https://pip.pypa.io/en/stable/` |
| **Cargo** | Rust | All-in-one build + pkg mgr | `https://doc.rust-lang.org/cargo/` |
| **CMake** | C/C++ | Meta-build system (generates Makefiles/VS projects) | `https://cmake.org/documentation/` |
| **Make** | C/C++/general | Classic rule-based build | `https://www.gnu.org/software/make/manual/` |
| **Go modules** | Go | Built-in dependency management | `https://go.dev/ref/mod` |
| **Bazel** | Multi-language (Google) | Hermetic, reproducible builds at scale | `https://bazel.build/docs` |
| **sbt** | Scala/Java | Interactive build tool (Scala ecosystem) | `https://www.scala-sbt.org/1.x/docs/` |

#### Maven — Deep Dive
```
Maven Lifecycle Phases:
  validate → compile → test → package → verify → install → deploy

Key Concepts:
  POM (Project Object Model)    — pom.xml describes project metadata, deps, plugins
  Convention over Configuration  — standard directory layout (src/main/java, src/test/java)
  Dependency Management          — Central repository (Maven Central), transitive deps
  Multi-module Projects          — Parent POM + child modules
  Plugins & Goals                — Each plugin provides goals (compiler:compile, surefire:test)
  Profiles                       — Environment-specific configurations (dev, staging, prod)
  BOM (Bill of Materials)        — Dependency version management across modules
```

##### Maven Essential Commands
| Command | Purpose |
|---|---|
| `mvn clean` | Delete target/ directory |
| `mvn compile` | Compile source code |
| `mvn test` | Run unit tests (Surefire plugin) |
| `mvn package` | Create JAR/WAR |
| `mvn install` | Install to local `.m2` repository |
| `mvn deploy` | Deploy to remote repository |
| `mvn dependency:tree` | Show dependency tree (find conflicts) |
| `mvn dependency:analyze` | Find unused / undeclared dependencies |
| `mvn versions:display-dependency-updates` | Check for newer dependency versions |
| `mvn clean install -DskipTests` | Build without running tests |
| `mvn -pl module-name -am` | Build specific module + its dependencies |
| `mvn site` | Generate project documentation site |
| `mvn archetype:generate` | Create project from template |

##### Maven POM Structure (Key Elements)
```xml
<project>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.company</groupId>
  <artifactId>my-app</artifactId>
  <version>1.0.0-SNAPSHOT</version>
  <packaging>jar</packaging>    <!-- jar, war, pom, ear -->

  <properties>
    <java.version>21</java.version>
    <spring-boot.version>3.3.0</spring-boot.version>
  </properties>

  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
      <scope>compile</scope>   <!-- compile, test, provided, runtime, system -->
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <configuration>
          <source>21</source>
          <target>21</target>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>
```

##### Maven Dependency Scopes
| Scope | Compile Classpath | Test Classpath | Runtime Classpath | Example |
|---|---|---|---|---|
| **compile** (default) | ✅ | ✅ | ✅ | Spring, Guava |
| **provided** | ✅ | ✅ | ❌ | Servlet API (provided by container) |
| **runtime** | ❌ | ✅ | ✅ | JDBC drivers |
| **test** | ❌ | ✅ | ❌ | JUnit, Mockito |
| **system** | ✅ | ✅ | ❌ | Local JARs (avoid — not portable) |

#### Gradle — Deep Dive
```
Gradle Build Phases:
  Initialization → Configuration → Execution

Key Concepts:
  Groovy/Kotlin DSL       — build.gradle (Groovy) or build.gradle.kts (Kotlin)
  Tasks & Task Graph      — Directed acyclic graph (DAG) of tasks
  Plugins                 — java, application, spring-boot, kotlin, etc.
  Dependency Configurations — implementation, api, compileOnly, runtimeOnly, testImplementation
  Build Cache              — Local & remote caching for faster rebuilds
  Incremental Compilation  — Only recompile changed files
  Composite Builds         — Include other Gradle projects as dependencies
  Gradle Wrapper           — gradlew — ensures consistent Gradle version across team
```

##### Gradle Essential Commands
| Command | Purpose |
|---|---|
| `./gradlew build` | Compile + test + assemble |
| `./gradlew clean` | Delete build/ directory |
| `./gradlew test` | Run tests |
| `./gradlew bootRun` | Run Spring Boot app (with plugin) |
| `./gradlew jar` | Create JAR |
| `./gradlew dependencies` | Show dependency tree |
| `./gradlew tasks` | List all available tasks |
| `./gradlew build --scan` | Generate build scan (performance analysis) |
| `./gradlew build -x test` | Build without tests |
| `./gradlew projects` | List all subprojects |
| `./gradlew wrapper --gradle-version 8.10` | Update Gradle wrapper version |
| `./gradlew build --parallel` | Parallel project builds |

##### Gradle Dependency Configurations
| Configuration | Compile Classpath | Runtime Classpath | Transitive? | Use For |
|---|---|---|---|---|
| **implementation** | ✅ | ✅ | ❌ to consumers | Internal dependencies (most common) |
| **api** | ✅ | ✅ | ✅ to consumers | Public API dependencies (library projects) |
| **compileOnly** | ✅ | ❌ | ❌ | Annotations, provided-scope equivalents |
| **runtimeOnly** | ❌ | ✅ | ❌ | JDBC drivers, SLF4J backends |
| **testImplementation** | ❌ (test only) | ❌ (test only) | ❌ | JUnit, Mockito |
| **annotationProcessor** | ✅ | ❌ | ❌ | Lombok, MapStruct |

#### Maven vs Gradle — Comparison
| Aspect | Maven | Gradle |
|---|---|---|
| **Config format** | XML (`pom.xml`) | Groovy/Kotlin DSL (`build.gradle`) |
| **Philosophy** | Convention over configuration | Convention + flexibility |
| **Build speed** | Slower (no caching by default) | Faster (daemon, incremental, cache) |
| **Dependency mgmt** | Mature, well-understood | More flexible (api vs implementation) |
| **Learning curve** | Lower (rigid structure) | Higher (DSL flexibility) |
| **Multi-module** | Parent POM + modules | Settings + multi-project |
| **Plugin ecosystem** | Extensive (mostly XML config) | Extensive (scriptable plugins) |
| **IDE support** | Excellent (all major IDEs) | Excellent (all major IDEs) |
| **Android** | ❌ Not used | ✅ Official Android build tool |
| **Enterprise adoption** | Very high (banking, insurance) | Growing fast (Android, Spring) |
| **Reproducibility** | Via `maven-enforcer-plugin` | Via Gradle wrapper + lock files |

**Industry note:** Most new Java projects in 2025-2026 use Gradle (especially Spring Boot + Android). Maven remains dominant in legacy enterprise systems. Google's Bazel is used for massive monorepos (Google, Uber, Dropbox).

#### Ant — Legacy Reference
```
Apache Ant (Another Neat Tool):
  - Imperative build system (you define every step)
  - XML-based build.xml
  - No convention — full control, but verbose
  - Ivy for dependency management (bolt-on, not built-in)
  - Still found in legacy enterprise Java codebases
  - Largely replaced by Maven (2004) and Gradle (2012)

Key Ant commands:
  ant compile     — compile sources
  ant test        — run tests
  ant jar         — create JAR
  ant clean       — clean build artifacts
  ant -buildfile other.xml   — use specific build file
```

### API Styles Comparison
| Style | Protocol | Data Format | Best For |
|---|---|---|---|
| **REST** | HTTP | JSON | Standard CRUD APIs |
| **GraphQL** | HTTP | JSON | Flexible client queries, reducing over-fetching |
| **gRPC** | HTTP/2 | Protobuf (binary) | Microservices, low-latency, streaming |
| **WebSocket** | TCP (upgraded HTTP) | Any | Real-time bidirectional (chat, live data) |
| **SOAP** | HTTP/SMTP | XML | Legacy enterprise integrations |

---

## Open-Source Projects to Study (Language-Agnostic Value)

| Project | Language | What You'll Learn |
|---|---|---|
| **Redis** | C | Efficient data structures, event loop, protocol design |
| **SQLite** | C | Database internals in one file, testing discipline |
| **Linux Kernel** | C | OS concepts in practice, concurrency, memory management |
| **Nginx** | C | Event-driven architecture, high-performance I/O |
| **Go standard library** | Go | Excellent API design, minimal dependencies |
| **Flask / FastAPI** | Python | Clean web framework design, middleware patterns |
| **Kubernetes** | Go | Distributed systems, reconciliation loops, CRDs |
| **Tokio** | Rust | Async runtime, futures, task scheduling |
| **Spring Framework** | Java | Enterprise patterns, DI, AOP, convention over configuration |
| **JUnit 5** | Java | Test framework design, extension model, annotations |
| **Kafka** | Java/Scala | Distributed streaming, log-based architecture |
| **Envoy Proxy** | C++ | Service mesh, L7 proxy, extensible filter chains |
| **Backstage** | TypeScript | Developer portal platform (Spotify), plugin architecture |
| **OPA (Open Policy Agent)** | Go | Policy-as-code, declarative authorization |
| **Prometheus** | Go | Metrics collection, time-series DB, alerting |

---

## Industry-Used Concepts & Real-World Systems

### Architectural Patterns Used by Top Companies

#### Rate Limiting — Netflix, Cloudflare, Stripe
```
Purpose: Protect services from abuse, ensure fair usage, prevent cascading failures

Algorithms:
  Token Bucket      — Tokens refill at fixed rate; request consumes a token
                      Used by: AWS API Gateway, Stripe API
                      Pros: Allows bursts up to bucket size
                      Cons: Needs token tracking per client

  Leaky Bucket      — Requests processed at fixed rate; excess queued or dropped
                      Used by: Nginx rate limiting, Shopify
                      Pros: Smooth output rate
                      Cons: No burst tolerance

  Fixed Window      — Count requests per time window (e.g., 100 req/minute)
                      Problem: Boundary burst (200 requests in 2 seconds across boundary)

  Sliding Window Log — Track exact timestamp of each request
                      Most accurate but memory-intensive

  Sliding Window Counter — Hybrid: weighted combination of current + previous window
                      Used by: Redis-based rate limiters
                      Good balance of accuracy and efficiency

Netflix Approach:
  - Zuul gateway with adaptive rate limiting
  - Uses Hystrix (now Resilience4j) for circuit breaking
  - Concurrency-based limits (not just rate-based)
  - Per-service, per-endpoint, per-client granularity

Cloudflare Approach:
  - Edge-based rate limiting (at CDN level, before origin)
  - Bot detection + challenge pages (Turnstile CAPTCHA)
  - DDoS protection at L3/L4/L7

Stripe Approach:
  - Token bucket per API key
  - Returns rate limit headers: X-RateLimit-Limit, X-RateLimit-Remaining
  - 429 Too Many Requests with Retry-After header
  - Separate limits for test vs live mode

Implementation (Redis):
  - Use INCR + EXPIRE for fixed window
  - Use sorted sets (ZRANGEBYSCORE) for sliding window
  - Lua script for atomic check-and-increment
```

#### Circuit Breaker — Netflix Hystrix / Resilience4j
```
Purpose: Prevent cascading failures in distributed systems

States:
  CLOSED    → Normal operation; monitor failure rate
  OPEN      → Too many failures; reject requests immediately (fail fast)
  HALF-OPEN → After timeout, allow a few test requests
              If they succeed → CLOSED
              If they fail → OPEN again

Netflix Hystrix (original, now in maintenance):
  - Java library for latency and fault tolerance
  - Thread pool isolation per dependency
  - Fallback methods when circuit opens

Resilience4j (modern replacement):
  - Lightweight, functional, Java 8+ compatible
  - Circuit Breaker, Rate Limiter, Retry, Bulkhead, TimeLimiter
  - Integrates with Spring Boot, Micronaut
  - Decorator pattern: supplier → circuitBreaker → retry → rateLimiter

Industry Usage:
  - Netflix: Every service-to-service call wrapped in circuit breaker
  - Amazon: Service mesh (App Mesh) with circuit breaking
  - Spotify: Resilience4j in Java services
  - All major cloud providers: Built into service mesh (Istio, Linkerd)
```

#### Transformer Architecture — Google / OpenAI
```
Origin: "Attention Is All You Need" (Google, 2017)
Architecture: Encoder-Decoder with self-attention mechanism

Key Innovation: Self-Attention
  - Every token attends to every other token
  - Parallelizable (unlike RNNs/LSTMs which are sequential)
  - Captures long-range dependencies

Evolution:
  2017 — Transformer (Google) — machine translation
  2018 — BERT (Google) — bidirectional encoder (NLU tasks)
  2018 — GPT-1 (OpenAI) — decoder-only (text generation)
  2019 — GPT-2 (OpenAI) — larger, zero-shot capabilities
  2020 — GPT-3 (OpenAI) — 175B parameters, few-shot learning
  2020 — ViT (Google) — Vision Transformer (images, not just text)
  2021 — DALL-E (OpenAI) — text-to-image generation
  2022 — ChatGPT (OpenAI) — RLHF-tuned GPT-3.5
  2023 — GPT-4 (OpenAI) — multimodal (text + image)
  2023 — Llama 2 (Meta) — open-source LLM
  2024 — Gemini (Google) — multimodal, long context
  2024 — Claude 3 (Anthropic) — instruction following, safety
  2024 — Llama 3 (Meta) — improved open-source
  2025 — GPT-4.5 / o3 (OpenAI) — reasoning models
  2025 — Claude 3.5/4 (Anthropic) — agentic coding
  2025 — Gemini 2.0 (Google) — agentic AI, Project Astra

Industry Applications:
  - Code generation: GitHub Copilot, Cursor, Cody
  - Search: Google SGE, Bing Chat, Perplexity
  - Customer support: Klarna AI chatbot (replaced 700 agents)
  - Content creation: Jasper, Copy.ai, Notion AI
  - Code review: CodeRabbit, Qodo (formerly CodiumAI)
```

#### Event-Driven Architecture — Uber, LinkedIn, Netflix
```
Pattern: Services communicate via events (async/decoupled)

Components:
  Producer   → publishes events to a topic/queue
  Broker     → stores and routes events (Kafka, RabbitMQ, Pulsar)
  Consumer   → subscribes and processes events

Event Patterns:
  Event Notification  — "Something happened" (minimal data, fetch if needed)
  Event-Carried State — "Something happened, here's the data" (self-contained)
  Event Sourcing      — Store all events as source of truth (append-only log)
  CQRS                — Separate read and write models

Uber:
  - Apache Kafka for all inter-service communication
  - Event sourcing for trip state (requested → accepted → started → completed)
  - Billions of events per day

LinkedIn:
  - Invented Apache Kafka (2011, open-sourced)
  - Uses Kafka for activity streams, metrics, logs
  - Feed generation via event-driven pipeline

Netflix:
  - Apache Kafka + custom event bus
  - Event-driven analytics pipeline
  - Chaos engineering events for resiliency testing
```

#### Microservices Patterns in Practice
| Pattern | What It Solves | Used By | Implementation |
|---|---|---|---|
| **API Gateway** | Single entry point, routing, auth, rate limiting | Netflix Zuul/Spring Cloud Gateway, Kong, AWS API Gateway | Reverse proxy with routing rules |
| **Service Mesh** | Service-to-service communication (mTLS, observability, retries) | Istio + Envoy (Google, Lyft), Linkerd (Buoyant) | Sidecar proxy per pod |
| **Saga Pattern** | Distributed transactions across services | Uber (Cadence/Temporal), Axon Framework | Orchestration or choreography |
| **Outbox Pattern** | Reliable event publishing with DB transactions | Debezium CDC, custom implementations | Write to outbox table → CDC → Kafka |
| **Strangler Fig** | Incremental monolith-to-microservices migration | Shopify, Segment, many migrations | Route traffic gradually to new service |
| **Sidecar Pattern** | Add functionality without changing service code | Envoy, Dapr, service mesh | Separate container in same pod |
| **Backend for Frontend (BFF)** | Tailored API per client type (web, mobile, IoT) | SoundCloud, Netflix, Spotify | One gateway per frontend |
| **CQRS** | Separate read/write models for different optimization | Event Store, Axon, custom | Write → event store → read projections |

#### Observability at Scale — How Big Tech Does It
| Company | Metrics | Logs | Traces | Incident Mgmt |
|---|---|---|---|---|
| **Google** | Monarch (internal) | Custom (Dapper-based) | Dapper → OpenTelemetry | Borgmon → SRE on-call |
| **Netflix** | Atlas (open-source) | ELK + custom | Zipkin (co-created) | PagerDuty + custom |
| **Uber** | M3 (open-source) | ELK | Jaeger (created by Uber) | Custom on-call platform |
| **Meta** | Scuba (internal) | LogDevice + Scribe | Canopy | SEV system + on-call |
| **Amazon** | CloudWatch | CloudWatch Logs | X-Ray | Custom escalation |
| **Spotify** | Prometheus + Grafana | ELK | Custom distributed tracing | PagerDuty + Backstage |
| **Twitter/X** | Custom metrics | Splunk | Zipkin + custom | Custom SEV process |
| **LinkedIn** | InGraphs (internal) | Custom + ELK | Custom | Custom on-call |

### The Twelve-Factor App (Industry Standard for SaaS)
| Factor | Principle | Modern Implementation |
|---|---|---|
| **1. Codebase** | One codebase tracked in VCS, many deploys | Git repo → CI/CD → dev/staging/prod |
| **2. Dependencies** | Explicitly declare and isolate | Maven/Gradle, npm, Docker multi-stage |
| **3. Config** | Store config in the environment | Env vars, ConfigMap, AWS SSM, Vault |
| **4. Backing Services** | Treat as attached resources | Database URLs, Redis, S3 via env vars |
| **5. Build, Release, Run** | Strictly separate stages | CI builds → CD releases → K8s runs |
| **6. Processes** | Execute as stateless processes | Stateless containers, external session store |
| **7. Port Binding** | Export services via port binding | Docker EXPOSE, K8s Service |
| **8. Concurrency** | Scale out via process model | K8s HPA, multiple pods, not bigger VMs |
| **9. Disposability** | Fast startup, graceful shutdown | Health checks, preStop hooks, SIGTERM handling |
| **10. Dev/Prod Parity** | Keep environments similar | Docker, Testcontainers, Nix |
| **11. Logs** | Treat as event streams | stdout → log aggregator (Fluentd, Loki) |
| **12. Admin Processes** | Run admin tasks as one-off processes | K8s Jobs, one-off containers, scripts |

### Security in Production
| Area | Concepts | Tools |
|---|---|---|
| **Authentication** | OAuth 2.0 + OIDC, JWT, SAML, passkeys, MFA | Keycloak, Auth0, Okta, Firebase Auth |
| **Authorization** | RBAC, ABAC, policy-as-code, least privilege | OPA, Casbin, AWS IAM, Spring Security |
| **Secrets Management** | Never in code, rotate regularly, fine-grained access | HashiCorp Vault, AWS Secrets Manager, SOPS |
| **Supply Chain Security** | SBOMs, signed images, provenance, dep scanning | Sigstore, Cosign, SLSA, Snyk, Trivy |
| **Container Security** | Non-root, distroless images, read-only fs, seccomp | Falco, Trivy, Docker Scout |
| **Network Security** | mTLS, network policies, WAF, zero-trust | Istio, Cilium, Cloudflare, AWS WAF |
| **SAST / DAST** | Static & dynamic application security testing | SonarQube, Semgrep, CodeQL, OWASP ZAP |
| **OWASP Top 10** | Injection, broken auth, XSS, CSRF, SSRF, etc. | OWASP guides, security training |
| **Compliance** | SOC 2, GDPR, HIPAA, PCI-DSS | Vanta, Drata, custom controls |

---

## Tech Trends & Emerging Technology (2025–2026)

### AI & Machine Learning in Software Engineering
| Trend | What It Is | Status (2025-26) | Impact |
|---|---|---|---|
| **AI Coding Assistants** | LLM-powered code generation in IDEs | Mainstream | GitHub Copilot, Cursor, Cody, Windsurf |
| **Agentic AI** | AI agents that autonomously plan, execute, iterate | Emerging rapidly | Devin, SWE-Agent, Copilot agent mode, Spotify background coding agents |
| **AI Code Review** | Automated PR reviews using LLMs | Growing | CodeRabbit, Qodo, GitHub Copilot code review |
| **Vibe Coding** | Natural language to full application development | Experimental | Bolt.new, Lovable, Replit Agent |
| **RAG (Retrieval-Augmented Generation)** | LLMs grounded with retrieved context for accuracy | Production | Perplexity, Copilot workspace, custom enterprise RAG |
| **Fine-Tuning & Distillation** | Custom models trained on proprietary data | Growing | Enterprises training domain-specific models |
| **Multi-Modal AI** | Models handling text, code, images, audio, video | Mainstream | GPT-4o, Gemini, Claude (vision) |
| **AI Testing** | LLM-generated test cases, mutation testing | Emerging | Copilot test generation, CodiumAI |

### Infrastructure & Platform Trends
| Trend | What It Is | Status (2025–26) | Key Players |
|---|---|---|---|
| **Platform Engineering** | Internal developer platforms (IDPs) for self-service | Mainstream | Backstage (Spotify), Port, Humanitec |
| **Cloud Development Environments** | Remote dev environments over SSH/containers | Growing fast | GitHub Codespaces, Gitpod, Coder, Devbox |
| **eBPF** | Linux kernel programmability without kernel modules | Production | Cilium (networking), Falco (security), Pixie (observability) |
| **WebAssembly (Wasm)** | Run near-native code in browsers & edge/server | Growing | Fermyon Spin, WasmCloud, Cloudflare Workers |
| **Serverless v2** | Event-driven compute with better cold starts | Mainstream | AWS Lambda SnapStart, Cloudflare Workers, Deno Deploy |
| **GitOps** | Git as single source of truth for infrastructure | Mainstream | ArgoCD, Flux, Crossplane |
| **Service Mesh** | L7 networking for microservices (mTLS, observability) | Maturing | Istio, Linkerd, Cilium (eBPF-based, no sidecar) |
| **Nix / Devcontainers** | Reproducible development environments | Growing | Nix, devcontainer.json, Docker in development |
| **Internal Developer Portals** | Centralized catalog of services, docs, APIs | Rapid adoption | Backstage, Port, OpsLevel |
| **FinOps** | Cloud cost management as engineering practice | Growing importance | Kubecost, Infracost, AWS CUR, spot instances |

### Programming Language & Runtime Trends
| Trend | What It Is | Status (2025–26) |
|---|---|---|
| **Rust adoption** | Systems programming with memory safety (no GC) | Growing (Linux kernel, Android, Cloudflare, Discord) |
| **Go dominance in infra** | Cloud-native tooling language (K8s, Docker, Terraform) | Dominant |
| **TypeScript everywhere** | Full-stack type safety (frontend + backend + infra) | Mainstream |
| **Java modernization** | Virtual threads (Loom), pattern matching, records, sealed classes | Active (Java 21+ LTS) |
| **Python for AI/ML** | De facto language for ML/AI pipelines | Dominant |
| **Zig** | C replacement with better safety, comptime, no hidden control flow | Emerging (Bun runtime written in Zig) |
| **Kotlin Multiplatform** | Share code between Android, iOS, web, server | Growing |
| **Swift on Server** | Apple's language for backend (Vapor framework) | Niche but growing |

### Data & Analytics Trends
| Trend | What It Is | Key Tech |
|---|---|---|
| **Data Lakehouse** | Combines data lake + data warehouse | Apache Iceberg, Delta Lake, Apache Hudi |
| **Streaming-First** | Real-time over batch processing | Kafka, Flink, Spark Structured Streaming |
| **Vector Databases** | Store embeddings for AI/ML similarity search | Pinecone, Weaviate, Milvus, pgvector |
| **Data Contracts** | Schema agreements between producers and consumers | Protobuf, Avro, JSON Schema, DataHub |
| **dbt (Data Build Tool)** | SQL-first data transformation in the warehouse | dbt Core, dbt Cloud |
| **Feature Stores** | Manage ML features for training and serving | Feast, Tecton, SageMaker Feature Store |

### Architecture Trends
| Trend | What It Is | When to Use |
|---|---|---|
| **Modular Monolith** | Well-structured monolith with clear module boundaries | Most startups (don't start with microservices!) |
| **Cell-Based Architecture** | Isolate blast radius by deploying in independent cells | Netflix, AWS, Uber — high availability |
| **Edge Computing** | Process data closer to users (edge servers, CDN workers) | Low-latency, IoT, CDN, Cloudflare Workers |
| **Event Mesh** | Multi-cloud event routing across event brokers | Enterprise integration, Solace |
| **Micro-Frontends** | Independent frontend modules deployed separately | Large teams with autonomous frontend squads |
| **Islands Architecture** | Server-rendered HTML with interactive "islands" | Astro, content-heavy sites with interactive widgets |
| **Zero Trust Architecture** | Never trust, always verify — regardless of network position | Enterprise security, remote-first orgs |

### Industry-Specific Engineering Insights

#### What Top Companies Do Differently
| Company | Notable Engineering Practices |
|---|---|
| **Google** | Monorepo (billions of lines), Borg (K8s predecessor), SRE culture, Spanner (globally distributed DB), MapReduce → Dataflow |
| **Netflix** | Chaos Engineering (Chaos Monkey), microservices at scale, Zuul gateway, open-source culture, blameless postmortems |
| **Amazon** | Two-pizza teams, "Working Backwards" (PR/FAQ), single-threaded leaders, service-oriented architecture since 2002 |
| **Meta** | Move fast (with stable infra), massive monorepo, React/GraphQL creators, Cassandra → TAO, large-scale ML |
| **Spotify** | Squad model, Backstage (developer portal), background coding agents, experimentation platform (Confidence) |
| **Uber** | Cadence/Temporal (workflow engine), H3 (hexagonal geospatial index), Jaeger (distributed tracing), microservices at extreme scale |
| **Stripe** | Exceptional API design, Ruby → Java/Go migration, backward-compatible APIs, TDD culture |
| **Cloudflare** | Edge computing, Workers (V8 isolates), global config change caution (multiple outages from instant rollouts) |
| **LinkedIn** | Created Kafka, economic graph, large-scale A/B testing, Samza stream processing |
| **Apple** | Vertical integration, extreme privacy focus, SwiftUI/Metal, custom silicon (M-series) |

#### Engineering Blogs to Follow (Knowledge Sources)
| Blog | URL | Focus |
|---|---|---|
| **Netflix Tech Blog** | `https://netflixtechblog.com/` | Distributed systems, microservices, chaos engineering |
| **Uber Engineering** | `https://www.uber.com/en-US/blog/engineering/` | Geo systems, ML, large-scale architecture |
| **Spotify Engineering** | `https://engineering.atspotify.com/` | Developer experience, ML, platform engineering |
| **Stripe Engineering** | `https://stripe.com/blog/engineering` | Payments, API design, distributed systems |
| **Meta Engineering** | `https://engineering.fb.com/` | Social scale, ML/AI, infrastructure |
| **Google Cloud Blog** | `https://cloud.google.com/blog/products/` | Cloud architecture, SRE, AI |
| **AWS Architecture Blog** | `https://aws.amazon.com/blogs/architecture/` | Cloud patterns, well-architected framework |
| **Cloudflare Blog** | `https://blog.cloudflare.com/` | Networking, security, edge computing |
| **The Pragmatic Engineer** | `https://blog.pragmaticengineer.com/` | Industry insights, career, engineering culture |
| **Martin Fowler** | `https://martinfowler.com/` | Software design, refactoring, architecture |
| **ByteByteGo** | `https://blog.bytebytego.com/` | System design, visual explanations |
| **InfoQ** | `https://www.infoq.com/` | Conferences, trends, emerging tech |
| **CNCF Blog** | `https://www.cncf.io/blog/` | Cloud-native ecosystem, graduated projects |

#### Open Standards & Initiatives to Know
| Standard | What It Is | Why It Matters |
|---|---|---|
| **OpenTelemetry (OTel)** | Unified observability framework (traces, metrics, logs) | Vendor-neutral — instrument once, export anywhere |
| **OpenAPI / Swagger** | API specification standard | Auto-generate clients, docs, test stubs |
| **gRPC + Protobuf** | High-performance RPC with schema-first development | Microservices communication (Google-created) |
| **CloudEvents** | Standard format for event data | Interoperable event-driven systems |
| **SLSA (Supply-Chain Levels)** | Software supply chain security framework | Protect against build tampering |
| **SBOM (Software Bill of Materials)** | List of all components in software | US Executive Order requires for government software |
| **OCI (Open Container Initiative)** | Container image and runtime standards | Docker, Podman, containerd all conform |
| **Gateway API** | Next-gen Kubernetes ingress specification | Replaces Ingress resource |
| **WebAssembly (WASI)** | System interface for Wasm outside browsers | Portable, sandboxed, near-native performance |
| **JSON Schema / AsyncAPI** | Schema for JSON data / async APIs | Contract-first design for APIs and events |

---

## How to Use This Skill

When a learner asks about **any** software engineering concept:
1. **Identify the domain** — is it DSA, system design, networking, OS, testing, patterns, etc.?
2. **Recommend the best resource** from the relevant section above
3. **Provide the canonical book reference** if one exists
4. **Point to hands-on practice** — exercises, projects, or code to study
5. **Use `fetch` to retrieve content** from official documentation websites when the learner wants to study specific pages

When recommending resources:
- Start with **1 official doc + 1 tutorial + 1 hands-on** — never overwhelm with 10 links
- Explain **why** each resource is recommended for the learner's current level
- Sequence resources: foundational → practical → advanced
