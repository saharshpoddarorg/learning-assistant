```prompt
---
name: reading-plan
description: 'Generate a structured reading and learning plan for any CS/SE topic — with curated resources, milestones, and practice checkpoints'
agent: learning-mentor
tools: ['codebase', 'fetch']
---

## Topic or Goal
${input:topic:What do you want to learn? (e.g., system design, OS internals, networking protocols, DSA for interviews, concurrency, design patterns, clean code, databases, testing, Java, Python, distributed systems)}

## Time Commitment
${input:time:How much time can you dedicate? (e.g., 1 hour/day for 2 weeks, weekend project, 30 min/day)}

## Current Level
${input:level:Where are you starting from? (beginner / know-the-basics / intermediate / want-to-go-deeper)}

## Goal Type (optional)
${input:goal:What's driving you? (learning / interview-prep / work-project / curiosity — or leave blank)}

## Instructions

Create a **structured learning plan** with curated resources. The plan should be realistic, progressive, and action-oriented.

### 1. Learning Roadmap

Create a visual progression:
```
Phase 1: Foundation → Phase 2: Core Skills → Phase 3: Practice → Phase 4: Mastery
```

For each phase:
- **Goal** — what you'll understand by the end
- **Duration** — realistic time estimate
- **Prerequisites** — what you need before starting this phase

### 2. Resource Schedule

For each phase, provide a day-by-day or week-by-week schedule:

| Day/Week | Activity | Resource | Type | Time |
|---|---|---|---|---|
| Day 1 | Read: Introduction | [Resource Name](URL) | Official docs | 30 min |
| Day 1 | Practice: First exercise | Exercism track | Hands-on | 30 min |
| Day 2 | Read: Core concepts | [Resource Name](URL) | Tutorial | 45 min |
| ... | ... | ... | ... | ... |

#### Resource Selection Criteria
For each resource, explain **why** it was chosen over alternatives:
- **Official docs / specs / RFCs** — for authoritative, specification-level understanding
- **Textbooks** — for structured, deep learning (e.g., CLRS for algorithms, OSTEP for OS, DDIA for distributed systems)
- **Tutorials** (Baeldung, GeeksforGeeks, MDN, etc.) — for practical, example-driven learning
- **Blog posts** — for real-world perspectives and hard-won lessons
- **Open-source code** — for seeing concepts applied in production
- **Video / courses** — for visual learners or complex topics that benefit from walkthroughs
- **Exercises / platforms** (LeetCode, Exercism, HackerRank) — for active recall and practice

### 3. Practice Checkpoints

After each phase, include:
- **Self-assessment questions** — "Can I explain X without looking at notes?"
- **Coding exercise** — a small task that proves understanding
- **Code review** — read a piece of open-source code and identify the concepts in action
- **Teach-back** — explain the concept as if teaching someone else (use `/teach` prompt)

### 4. Open-Source Study List

Curate 3-5 open-source projects relevant to this topic:

| Project | What to Study | Specific Files/Packages | Difficulty |
|---|---|---|---|
| Project Name | What concept it demonstrates | `com.example.package` | Beginner-friendly |
| ... | ... | ... | ... |

For each: explain what to look for and why it's instructive.

### 5. Common Pitfalls in Learning This Topic

- What learners typically get stuck on
- Concepts that seem similar but are different
- When to move on vs. when to go deeper
- Signs you've truly understood vs. just memorized

### 6. Supplementary Resources

| Type | Resource | When to Use |
|---|---|---|
| Quick reference | Cheat sheet / API docs | During coding, when you forget syntax |
| Deep reference | Spec / RFC / Book chapter | When you need the precise specification |
| Community | Stack Overflow tag / Reddit / Discord | When stuck on a specific problem |
| Video | Conference talk / YouTube / Course | When you want a different perspective |
| Newsletter / Blog | Engineering blogs, weekly digests | For ongoing learning after the plan |
| Practice platform | LeetCode / Exercism / HackerRank | For hands-on skill building |

### Rules
- Be realistic about time — don't pack 10 hours into a "30 min/day" plan
- Prioritize doing over reading — at least 40% of time should be hands-on
- Include official documentation as a primary source, not an afterthought
- Sequence resources so earlier ones build foundation for later ones
- Include at least one open-source project to study per phase
- Every phase should end with a concrete deliverable (code written, concept explained, exercise completed)
- Adjust depth based on the learner's declared level — don't repeat basics for intermediates
```
