```prompt
---
name: interview-prep
description: 'Prepare for technical interviews — DSA patterns, system design (HLD/LLD), behavioral questions, and coding problem strategies'
agent: learning-mentor
tools: ['codebase', 'fetch']
---

## Interview Type
${input:type:What kind of interview are you preparing for? (DSA / system-design-HLD / system-design-LLD / behavioral / mixed / mock-question)}

## Topic or Problem (optional)
${input:topic:Specific topic or problem? (e.g., sliding window, design URL shortener, LRU cache, rate limiter — or leave blank for general guidance)}

## Target Level
${input:level:What level are you targeting? (new-grad / mid-level / senior / staff+)}

## Time Available
${input:time:How long until your interview? (1 week / 2 weeks / 1 month / 3 months / just exploring)}

## Instructions

Based on the interview type selected, follow the appropriate section below.

---

### If DSA Interview:

#### 1. Pattern Identification
- **Identify the pattern** — which algorithmic pattern does this problem (or topic) belong to?
- **Pattern family:** show where it fits in the DSA pattern map:
  ```
  Arrays/Strings → Two Pointers → Sliding Window → Binary Search →
  Linked Lists → Stacks/Queues → Trees → Graphs (BFS/DFS) →
  Backtracking → Dynamic Programming → Greedy → Trie →
  Union-Find → Topological Sort → Heap/Priority Queue →
  Monotonic Stack → Segment Tree → Bit Manipulation
  ```
- **Signal words** — what clues in a problem statement suggest this pattern?

#### 2. Core Technique
- **Algorithm walkthrough** — step-by-step explanation of the technique
- **Template code** — a reusable code template for this pattern
- **Time & space complexity** — analyze precisely, explain WHY (not just state O(n))
- **Visual trace** — trace through a small example input step by step

#### 3. Problem Progression
Provide 3-5 problems in increasing difficulty:
| # | Problem | Difficulty | Key Twist | LeetCode # |
|---|---|---|---|---|
| 1 | ... | Easy | Basic application | ... |
| 2 | ... | Medium | Added constraint | ... |
| 3 | ... | Medium | Requires modification of template | ... |
| 4 | ... | Hard | Combines patterns | ... |

#### 4. Interview Strategy
- **How to approach** — first 2 minutes (clarify, examples, edge cases)
- **How to communicate** — what to say while solving
- **Common mistakes** — what trips people up on this pattern
- **Optimization path** — brute force → better → optimal

---

### If System Design — HLD (High-Level Design):

#### 1. Framework
Walk through the standard system design framework:
1. **Requirements Clarification** — functional & non-functional requirements, constraints
2. **Back-of-Envelope Estimation** — QPS, storage, bandwidth calculations
3. **High-Level Architecture** — component diagram with data flow
4. **Detailed Component Design** — deep-dive into 2-3 critical components
5. **Trade-offs & Bottlenecks** — what could go wrong, how to mitigate

#### 2. Core Building Blocks
For the topic, explain the relevant building blocks:
| Component | Purpose | Options | Trade-offs |
|---|---|---|---|
| Load Balancer | Distribute traffic | L4/L7, round-robin, consistent hashing | Latency vs fairness |
| Cache | Reduce latency | Redis, Memcached, CDN | Consistency vs speed |
| Database | Persist data | SQL vs NoSQL, sharding strategy | ACID vs scalability |
| Message Queue | Async processing | Kafka, RabbitMQ, SQS | Ordering vs throughput |
| ... | ... | ... | ... |

#### 3. Design Walkthrough
- **ASCII architecture diagram** showing all components
- **Data flow** — trace one request end-to-end
- **Data model** — key tables/collections and relationships
- **API design** — key endpoints with request/response

#### 4. Scale & Reliability
- How to handle 10x growth
- Single points of failure and mitigation
- Consistency model: strong vs eventual, and why

---

### If System Design — LLD (Low-Level Design):

#### 1. Requirements
- List functional requirements as user stories or use cases
- Identify key entities and their relationships

#### 2. Object-Oriented Design
- **Class diagram** — key classes with attributes and methods
- **SOLID analysis** — how does the design follow SOLID principles?
- **Design patterns** — which patterns apply and why?
- **Interface design** — what should be abstracted?

#### 3. Code Skeleton
- Provide core class/interface definitions
- Show key method implementations with clear reasoning
- Include relevant enums, constants, and configuration
- Demonstrate extensibility — how easy is it to add a new feature?

#### 4. Discussion Points
- Alternative designs and their trade-offs
- What changes if requirements evolve?
- Edge cases and error handling

---

### If Mock Question:

Present a realistic interview question and then:
1. **Give the learner 2 minutes** to think about their approach
2. **Walk through the optimal solution** step by step
3. **Show the code** with detailed comments
4. **Discuss follow-ups** — how an interviewer would extend this question
5. **Scoring rubric** — what interviewers typically look for in the answer

---

### Study Plan (when time is specified)

Create a schedule tailored to the available time:

| Week | Focus | Topics | Practice |
|---|---|---|---|
| 1 | Foundations | Arrays, strings, hash maps | 3 easy + 2 medium per day |
| 2 | Core patterns | Two pointers, sliding window, BFS/DFS | ... |
| ... | ... | ... | ... |

Include:
- **Daily breakdown** for short timelines (1-2 weeks)
- **Weekly breakdown** for longer timelines (1-3 months)
- **Review strategy** — spaced repetition for previously solved problems

### Rules
- Be practical and interview-focused — optimize for what interviewers actually test
- For DSA: always analyze time AND space complexity explicitly
- For system design: always draw ASCII architecture diagrams
- Don't overwhelm — focus on the most impactful topics for the target level
- When referencing problems, prefer LeetCode numbers for easy lookup
- For senior/staff+, emphasize trade-off discussion and architectural thinking over raw coding
- Use `fetch` to retrieve problem descriptions or design references when helpful
```
