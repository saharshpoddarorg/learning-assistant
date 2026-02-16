```prompt
---
name: dsa
description: 'Data Structures & Algorithms — learn by pattern, topic, or difficulty with visual explanations, complexity analysis, and practice problems'
agent: learning-mentor
tools: ['codebase', 'fetch']
---

## Topic
${input:topic:What DSA topic? (e.g., two-pointers, binary-search, dynamic-programming, graphs, trees, sliding-window, backtracking, trie, heap, linked-list, sorting, union-find)}

## Mode
${input:mode:What do you want to do? (learn-concept / solve-pattern / practice-problems / compare-structures / complexity-analysis / interview-roadmap)}

## Preferred Language
${input:language:Code examples in which language? (java / python / cpp / go / javascript / pseudocode)}

## Current Level
${input:level:Your DSA level? (beginner / intermediate / advanced / contest-level)}

## Instructions

### DSA Complete Hierarchy
```
Data Structures & Algorithms
│
├── DATA STRUCTURES
│   │
│   ├── Linear
│   │   ├── Arrays & Strings (contiguous memory, O(1) access)
│   │   ├── Linked Lists (singly, doubly, circular)
│   │   ├── Stacks (LIFO — parsing, undo, DFS)
│   │   ├── Queues (FIFO — BFS, scheduling)
│   │   └── Deque (double-ended — sliding window)
│   │
│   ├── Hashing
│   │   ├── HashMap / HashSet (O(1) avg lookup)
│   │   ├── Collision handling (chaining, open addressing)
│   │   └── Consistent Hashing (distributed systems)
│   │
│   ├── Trees
│   │   ├── Binary Tree (traversals: inorder, preorder, postorder, level-order)
│   │   ├── BST — Binary Search Tree (search, insert, delete)
│   │   ├── AVL / Red-Black Tree (self-balancing)
│   │   ├── B-Tree / B+ Tree (database indexing)
│   │   ├── Segment Tree (range queries)
│   │   ├── Fenwick Tree / BIT (prefix sums)
│   │   └── Trie (prefix tree — autocomplete, spell check)
│   │
│   ├── Heaps
│   │   ├── Min-Heap / Max-Heap (priority queue)
│   │   ├── Heap operations (insert, extract, heapify)
│   │   └── Applications (top-K, median, scheduling)
│   │
│   ├── Graphs
│   │   ├── Representations (adjacency list, matrix, edge list)
│   │   ├── Directed vs undirected, weighted vs unweighted
│   │   ├── Special graphs (DAG, bipartite, tree)
│   │   └── Disjoint Set / Union-Find
│   │
│   └── Advanced
│       ├── Monotonic Stack / Queue
│       ├── Sparse Table (static range queries)
│       ├── Suffix Array / Tree
│       └── Bloom Filter (probabilistic membership)
│
├── ALGORITHMS
│   │
│   ├── Sorting
│   │   ├── Comparison: Merge Sort, Quick Sort, Heap Sort — O(n log n)
│   │   ├── Non-comparison: Counting Sort, Radix Sort, Bucket Sort — O(n)
│   │   └── Stability, in-place, adaptive properties
│   │
│   ├── Searching
│   │   ├── Binary Search (sorted array, answer space)
│   │   ├── Linear Search, Sentinel Search
│   │   └── Ternary Search (unimodal functions)
│   │
│   ├── Graph Algorithms
│   │   ├── BFS (shortest path unweighted, level-order)
│   │   ├── DFS (connectivity, cycle detection, topological sort)
│   │   ├── Dijkstra (shortest path, non-negative weights)
│   │   ├── Bellman-Ford (handles negative weights)
│   │   ├── Floyd-Warshall (all-pairs shortest path)
│   │   ├── Kruskal / Prim (minimum spanning tree)
│   │   ├── Topological Sort (DAG ordering)
│   │   └── Tarjan / Kosaraju (strongly connected components)
│   │
│   ├── Dynamic Programming
│   │   ├── 1D DP (climbing stairs, house robber, coin change)
│   │   ├── 2D DP (grid paths, LCS, edit distance, knapsack)
│   │   ├── Interval DP (matrix chain, burst balloons)
│   │   ├── DP on trees (diameter, max path sum)
│   │   ├── Bitmask DP (TSP, subset problems)
│   │   ├── Memoization vs tabulation
│   │   └── State optimization (rolling array)
│   │
│   ├── Greedy
│   │   ├── Activity selection, interval scheduling
│   │   ├── Huffman coding
│   │   ├── Fractional knapsack
│   │   └── Greedy proof techniques (exchange argument)
│   │
│   ├── Backtracking
│   │   ├── N-Queens, Sudoku solver
│   │   ├── Permutations, combinations, subsets
│   │   ├── Word search, palindrome partitioning
│   │   └── Pruning strategies
│   │
│   ├── String Algorithms
│   │   ├── KMP (pattern matching)
│   │   ├── Rabin-Karp (rolling hash)
│   │   ├── Z-Algorithm
│   │   └── Manacher (palindromes)
│   │
│   └── Math & Bit Manipulation
│       ├── GCD (Euclidean), LCM, modular arithmetic
│       ├── Sieve of Eratosthenes (primes)
│       ├── Fast exponentiation
│       ├── Bit tricks (XOR, masks, Brian Kernighan)
│       └── Combinatorics (nCr, Pascal's triangle)
│
└── PATTERNS (Interview Focus)
    │
    ├── Two Pointers (sorted arrays, pairs, palindromes)
    ├── Sliding Window (subarrays, substrings with constraints)
    ├── Fast & Slow Pointers (cycle detection, middle of list)
    ├── Binary Search variants (search space reduction)
    ├── BFS / DFS (graph traversal, tree traversal, grid problems)
    ├── Backtracking (constraint satisfaction, combinatorial)
    ├── Dynamic Programming (overlapping subproblems, optimal substructure)
    ├── Greedy (local optimum → global optimum)
    ├── Monotonic Stack (next greater/smaller element)
    ├── Topological Sort (dependency ordering)
    ├── Union-Find (connected components, redundant connections)
    ├── Trie (prefix problems, word dictionary)
    ├── Heap / Top-K (k-th largest, merge k sorted)
    ├── Interval problems (merge, insert, overlap)
    └── Matrix traversal (spiral, diagonal, rotate)
```

### Response by Mode

#### If mode = learn-concept:
1. **What is it?** — Plain definition
2. **Visual representation** — ASCII diagram of the data structure or algorithm step-by-step
3. **Core operations** — With time/space complexity for each
4. **Code implementation** — Clean, fully commented implementation in the chosen language
5. **When to use** — Problem signal words and scenarios
6. **Common mistakes** — What trips people up
7. **Related** — Similar structures/algorithms and how they differ
8. **Practice** — 3 problems (easy → medium → hard) with LeetCode numbers

#### If mode = solve-pattern:
1. **Pattern description** — When to recognize this pattern
2. **Signal words** — Clues in problem statements (e.g., "contiguous subarray" → sliding window)
3. **Template code** — Reusable skeleton in the chosen language
4. **Step-by-step trace** — Walk through a small example
5. **Complexity** — Time and space analysis (explain WHY, not just state O(n))
6. **Variations** — How the pattern adapts for different problems
7. **Problem progression** — 5 problems from easy to hard

#### If mode = practice-problems:
Curate problems by topic and difficulty:
| # | Problem | Difficulty | Pattern | LeetCode # | Key Insight |
|---|---|---|---|---|---|
| 1 | ... | Easy | ... | ... | ... |

#### If mode = complexity-analysis:
Deep walkthrough of Big-O for the given topic, including:
- Best, average, worst case
- Space complexity (auxiliary vs total)
- Amortized analysis where applicable
- Comparison with alternatives

#### If mode = interview-roadmap:
Structured study plan:
```
Week 1: Arrays, Strings, Two Pointers, Sliding Window
Week 2: Linked Lists, Stacks, Queues, Binary Search
Week 3: Trees (BFS, DFS), BST, Heaps
Week 4: Graphs (BFS, DFS, Dijkstra), Topological Sort
Week 5: Dynamic Programming (1D → 2D)
Week 6: Backtracking, Greedy, Trie, Union-Find
Week 7: Advanced + revision + mock interviews
```

### Rules
- Always include Big-O analysis (time AND space)
- Always provide code in the learner's chosen language (not pseudocode unless requested)
- Always trace through an example step by step
- For patterns: always show the signal words that hint at the pattern
- Reference LeetCode problem numbers for practice
- Show both brute force AND optimal approaches — explain the optimization journey
- Use visual traces (step-by-step tables or diagrams) for complex algorithms
```
