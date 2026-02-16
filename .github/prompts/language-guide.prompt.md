```prompt
---
name: language-guide
description: 'Language-specific learning — deep-dive into any programming language with ecosystem, idioms, best practices, and migration guides'
agent: learning-mentor
tools: ['codebase', 'fetch']
---

## Language
${input:language:Which language? (java / python / cpp / go / rust / javascript / typescript / c / csharp / kotlin / scala / ruby / swift / or any other)}

## Topic
${input:topic:What about this language? (getting-started / core-concepts / advanced-features / ecosystem / idioms / compare-with / migrate-from / interview-prep / project-setup)}

## Current Level
${input:level:Your level with THIS language? (never-used / beginner / intermediate / advanced / coming-from-another-language)}

## Instructions

### Language Learning Framework
```
Any Programming Language — Learning Hierarchy
│
├── 1. FOUNDATIONS
│   ├── Syntax & basics (variables, types, operators, control flow)
│   ├── Functions / methods
│   ├── Data structures (built-in: arrays, lists, maps, sets)
│   ├── Strings & I/O
│   ├── Error handling (exceptions, result types, error codes)
│   └── Modules / packages / imports
│
├── 2. OOP / TYPE SYSTEM
│   ├── Classes, objects, interfaces
│   ├── Inheritance, composition, polymorphism
│   ├── Generics / templates
│   ├── Type system depth (static vs dynamic, strong vs weak)
│   ├── Enums, sealed types, algebraic data types
│   └── Language-specific paradigm (functional, OOP, multi-paradigm)
│
├── 3. INTERMEDIATE
│   ├── Collections framework / standard library deep-dive
│   ├── Lambdas, closures, higher-order functions
│   ├── Iterators, generators, streams
│   ├── Pattern matching
│   ├── Annotations / decorators / macros
│   └── File I/O, serialization, networking
│
├── 4. ADVANCED
│   ├── Concurrency & parallelism (threads, async, channels, actors)
│   ├── Memory model (GC, ownership, borrowing, manual management)
│   ├── Metaprogramming (reflection, code generation, macros)
│   ├── Performance optimization & profiling
│   ├── FFI / interop with other languages
│   └── Language internals (compiler, runtime, VM)
│
├── 5. ECOSYSTEM
│   ├── Build tools & package managers
│   ├── Testing frameworks
│   ├── Linting & formatting
│   ├── Popular frameworks (web, CLI, data, systems)
│   ├── IDE & tooling
│   └── Community (conferences, blogs, Discord/forums)
│
└── 6. REAL-WORLD
    ├── Idiomatic code (the "right way" in this language)
    ├── Design patterns adapted for this language
    ├── Common pitfalls & anti-patterns
    ├── Production best practices
    └── Open-source projects to study
```

### Language Quick-Reference Cards

When teaching a specific language, provide a condensed reference:

#### For each language, know:
| Aspect | Detail |
|---|---|
| **Paradigm** | OOP / functional / multi-paradigm / systems |
| **Type system** | Static/dynamic, strong/weak, inferred? |
| **Memory** | GC / reference counting / ownership / manual |
| **Concurrency** | Threads / goroutines / async-await / actors |
| **Package manager** | Maven/Gradle / pip / npm / cargo / go mod |
| **Build tool** | javac/Maven / setuptools / webpack / cargo |
| **Test framework** | JUnit / pytest / Jest / go test / cargo test |
| **Linter** | Checkstyle / pylint / ESLint / golangci-lint |
| **Key frameworks** | Spring / Django / Express / Gin / Actix |
| **Official docs** | URL to best starting point |
| **REPL** | JShell / python / node / `go run` |
| **Killer feature** | What makes this language uniquely valuable |

### Response by Topic

#### If topic = getting-started:
1. **Why this language?** — What it's great for, who uses it
2. **Setup** — Install, IDE, first project
3. **Hello World++** — Beyond hello world: a small but real program
4. **Core syntax** — Variables, types, control flow, functions (with examples)
5. **Key differences** — If coming from another language, highlight what's different
6. **Learning path** — What to learn next, in order
7. **Resources** — Official tutorial + 1 best online resource + 1 book

#### If topic = compare-with:
Side-by-side comparison showing the SAME concept in both languages with idiomatic code.

#### If topic = idioms:
Show 10-15 idiomatic patterns that mark the difference between "writing Java in Python" vs "writing Pythonic Python" (adapted for the actual language).

#### If topic = ecosystem:
Deep map of the language's ecosystem — frameworks, tools, libraries for every common need.

#### If topic = migrate-from:
Translation guide: "In Language A you do X, in Language B the equivalent is Y, but note Z."

### Rules
- Always use idiomatic code for the target language — no "Java written in Python"
- Show the language's REPL or playground for experimentation when available
- Compare with languages the user already knows when relevant
- Point to official documentation as the primary reference
- Include the language's unique features (e.g., Go's goroutines, Rust's ownership, Java's virtual threads)
- Use `fetch` to retrieve official docs and tutorials when beneficial
```
