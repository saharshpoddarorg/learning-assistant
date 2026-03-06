# IntelliJ IDEA — Comprehensive Java Developer Guide

> **Purpose:** Complete reference for IntelliJ IDEA setup, navigation, coding
> assistance, debugging, profiling, build tool integration, and productivity
> tips — tailored for Java development.

---

## Table of Contents

1. [Overview & Editions](#1-overview--editions)
2. [Installation & First Steps](#2-installation--first-steps)
3. [Project Structure & Setup](#3-project-structure--setup)
4. [Navigation & Search](#4-navigation--search)
5. [Coding Assistance](#5-coding-assistance)
6. [Code Quality & Inspections](#6-code-quality--inspections)
7. [Refactoring](#7-refactoring)
8. [Debugging](#8-debugging)
9. [Profiling](#9-profiling)
10. [Build Tools (Gradle & Maven)](#10-build-tools-gradle--maven)
11. [Version Control (Git)](#11-version-control-git)
12. [Terminal & Command-Line Tools](#12-terminal--command-line-tools)
13. [Testing](#13-testing)
14. [Spring & Jakarta EE Support](#14-spring--jakarta-ee-support)
15. [Database Tools & HTTP Client](#15-database-tools--http-client)
16. [Essential Plugins](#16-essential-plugins)
17. [Essential Keyboard Shortcuts](#17-essential-keyboard-shortcuts)
18. [Tips & Tricks](#18-tips--tricks)
19. [Learning Resources](#19-learning-resources)

---

## 1. Overview & Editions

**IntelliJ IDEA** is a JVM-oriented IDE by JetBrains. As of **2025.1**, JetBrains unified
the product — the former "Community Edition" vs "Ultimate" split no longer applies.
All users now get a single IntelliJ IDEA product.

**Supported languages & frameworks:**
- **Primary:** Java, Kotlin, Groovy, Scala
- **Web:** JavaScript, TypeScript, HTML, CSS
- **Backend:** Spring, Jakarta EE, Micronaut, Quarkus, Ktor
- **Build:** Gradle, Maven, Ant
- **Database:** SQL, with built-in database browser & query console
- **Other:** Python (plugin), Go (plugin), Ruby (plugin)

**System requirements (2025):**
- OS: Windows 10+, macOS 12+, Linux (glibc 2.17+)
- RAM: 8 GB minimum, 16 GB recommended
- Disk: 3.5 GB for IDE + 1 GB for caches
- JDK: Bundled JetBrains Runtime; can configure project JDKs separately

---

## 2. Installation & First Steps

### Installation

| Method | How |
|---|---|
| **JetBrains Toolbox** (recommended) | Install Toolbox App → install IDEA from it. Manages updates and multiple IDE versions. |
| **Direct download** | Download from [jetbrains.com/idea/download](https://www.jetbrains.com/idea/download/) |
| **Snap (Linux)** | `sudo snap install intellij-idea-ultimate --classic` |
| **WinGet (Windows)** | `winget install JetBrains.IntelliJIDEA.Ultimate` |
| **Homebrew (macOS)** | `brew install --cask intellij-idea` |

### First-time configuration

1. **Choose UI theme:** Light / Dark / IntelliJ Light / Darcula
2. **Configure keymap:** IntelliJ (default), Eclipse, VS Code, Visual Studio
3. **Install plugins:** Suggested based on your project stack
4. **Configure JDK:** File → Project Structure → SDKs → Add JDK

### Migrating from other IDEs

| From | Resources |
|---|---|
| **Eclipse** | Built-in keymap, import Eclipse projects directly, IntelliJ migration guide |
| **NetBeans** | Import NetBeans projects, keymap plugin available |
| **VS Code** | VS Code keymap plugin + IntelliJ migration guide |

---

## 3. Project Structure & Setup

### Project Structure dialog (`Ctrl+Alt+Shift+S`)

| Tab | Purpose |
|---|---|
| **Project** | Project SDK, language level, compiler output path |
| **Modules** | Source roots, dependencies, module settings |
| **Libraries** | External JARs and downloaded dependencies |
| **Facets** | Framework-specific config (Spring, JPA, Web) |
| **Artifacts** | Build outputs (JAR, WAR, etc.) |

### Source roots

```
Mark directory as:
  📁 Sources Root        (blue)   — main Java source code
  📁 Test Sources Root   (green)  — test code
  📁 Resources Root      (purple) — non-code resources
  📁 Test Resources Root (purple) — test resources
  📁 Excluded            (orange) — ignored by IDE indexing
```

### .idea directory & sharing

```
.idea/
├── .gitignore              ← tells Git what to ignore
├── modules.xml             ← ✅ share (project modules)
├── misc.xml                ← ✅ share (project SDK, language level)
├── vcs.xml                 ← ✅ share (VCS mappings)
├── codeStyles/             ← ✅ share (team code style)
├── inspectionProfiles/     ← ✅ share (team inspections)
├── workspace.xml           ← ❌ DON'T share (personal layout)
├── usage.statistics.xml    ← ❌ DON'T share
└── shelf/                  ← ❌ DON'T share (personal shelved changes)
```

---

## 4. Navigation & Search

### Essential navigation shortcuts

| Shortcut (Windows/Linux) | macOS | Action |
|---|---|---|
| `Shift Shift` | `Shift Shift` | **Search Everywhere** — files, classes, symbols, actions, settings |
| `Ctrl+N` | `Cmd+O` | **Go to Class** — by name, supports CamelCase abbrev (`HM` → `HashMap`) |
| `Ctrl+Shift+N` | `Cmd+Shift+O` | **Go to File** — by filename |
| `Ctrl+Alt+Shift+N` | `Cmd+Option+O` | **Go to Symbol** — methods, fields, constants |
| `Ctrl+Shift+A` | `Cmd+Shift+A` | **Find Action** — find any IDE action by name |
| `Ctrl+B` / `Ctrl+Click` | `Cmd+B` | **Go to Declaration** |
| `Ctrl+Alt+B` | `Cmd+Option+B` | **Go to Implementation** |
| `Ctrl+U` | `Cmd+U` | **Go to Super method/class** |
| `Alt+F7` | `Option+F7` | **Find Usages** — all references to a symbol |
| `Ctrl+E` | `Cmd+E` | **Recent Files** |
| `Ctrl+Shift+E` | `Cmd+Shift+E` | **Recent Locations** — code snippets you recently viewed/edited |
| `Ctrl+F12` | `Cmd+F12` | **File Structure** — methods and fields in current file |
| `Alt+↑/↓` | `Ctrl+Shift+↑/↓` | **Navigate between methods** |
| `Ctrl+G` | `Cmd+L` | **Go to Line:Column** |
| `Ctrl+]` / `Ctrl+[` | `Cmd+]` / `Cmd+[` | **Navigate to matching brace** |
| `Ctrl+Shift+Backspace` | `Cmd+Shift+Backspace` | **Last Edit Location** |
| `F2` / `Shift+F2` | `F2` / `Shift+F2` | **Next/Previous Error** |

### Search Everywhere tips

- **Prefix filters:** type `/` for files, `#` for symbols, `@` for actions
- **Include non-project items:** press `Shift Shift` twice (double-tap)
- **Abbreviations:** type capital letters of CamelCase names (e.g., `AIOOBE` → `ArrayIndexOutOfBoundsException`)
- **Actions:** search for and execute any action directly

### Breadcrumbs & Structure

- **Breadcrumbs** at the bottom of the editor → click to navigate hierarchy
- **Structure tool window** (`Alt+7`) → full tree of file structure
- **Call Hierarchy** (`Ctrl+Alt+H`) → who calls this method
- **Type Hierarchy** (`Ctrl+H`) → class/interface hierarchy

---

## 5. Coding Assistance

### Code Completion

| Type | Shortcut | Description |
|---|---|---|
| **Basic** | `Ctrl+Space` | Context-aware suggestions (variables, methods, classes) |
| **Type-matching / Smart** | `Ctrl+Shift+Space` | Filters suggestions by expected type |
| **Statement** | `Ctrl+Shift+Enter` | Completes current statement (adds `;`, braces, etc.) |
| **Full-line ML** | Automatic | AI-powered full-line completion (gray text inline) |
| **Postfix** | Type `.` after expression | `expr.if` → `if (expr) {}`, `expr.var` → `var x = expr;` |

**Postfix completion examples:**

```
myList.for        →  for (var item : myList) { }
myList.fori       →  for (int i = 0; i < myList.size(); i++) { }
value.null        →  if (value == null) { }
value.notnull     →  if (value != null) { }
value.var         →  var name = value;
value.return      →  return value;
value.sout        →  System.out.println(value);
value.try         →  try { value } catch (...) { }
value.cast        →  ((Type) value)
value.instanceof  →  if (value instanceof Type) { }
```

### Live Templates

| Abbreviation | Expands To |
|---|---|
| `sout` | `System.out.println();` |
| `soutv` | `System.out.println("var = " + var);` |
| `soutp` | `System.out.println("method params");` |
| `fori` | `for (int i = 0; i < ...; i++) { }` |
| `iter` | `for (var item : collection) { }` |
| `psvm` | `public static void main(String[] args) { }` |
| `ifn` | `if (var == null) { }` |
| `inn` | `if (var != null) { }` |
| `thr` | `throw new` |
| `lazy` | Lazy initialization pattern |
| `const` | `public static final` |

> Create custom templates: **Settings → Editor → Live Templates**

### Code Generation (`Alt+Insert`)

- **Constructor** — select which fields to include
- **Getter / Setter** — for selected fields
- **`equals()` and `hashCode()`** — with field selection, template choice (JDK 7+, Objects, etc.)
- **`toString()`** — various templates (StringJoiner, StringBuilder, etc.)
- **Override/Implement methods** — select methods from superclass/interface
- **Test method** — generates test method stub
- **Delegate methods** — delegation to a field

---

## 6. Code Quality & Inspections

### Inspections

IntelliJ ships **600+ built-in inspections** for Java. Inspections run in real-time and
are shown as highlights in the editor.

**Severity levels:** Error → Warning → Weak Warning → Information → Typo

**Quick-fix:** When you see a highlighted issue, press `Alt+Enter` to see available fixes.

**Common Java inspections:**

| Inspection | Example Fix |
|---|---|
| Unused variable | Remove or use the variable |
| Redundant cast | Remove unnecessary cast |
| Diamond operator `<>` | Replace `new ArrayList<String>()` → `new ArrayList<>()` |
| Convert to enhanced for | Replace index-based loop with for-each |
| Replace with `var` | Use `var` for local variable when type is obvious |
| Null dereference | Add null check or `@Nullable` annotation |
| String comparison with `==` | Replace with `.equals()` |
| Resource not closed | Add try-with-resources |
| Unused import | Remove or optimize imports |

### Inspection profiles

- **Default** — all inspections enabled at their default severity
- **Project-specific** — stored in `.idea/inspectionProfiles/`, shared with team
- **Custom** — create your own combinations

### Running inspections on demand

- **Single file:** `Ctrl+Alt+Shift+I` → type inspection name
- **Whole project:** Analyze → Inspect Code
- **Before commit:** enable inspections in commit dialog

---

## 7. Refactoring

### Refactoring shortcuts

| Shortcut | Refactoring |
|---|---|
| `Ctrl+Alt+Shift+T` | **Refactor This** — shows all applicable refactorings |
| `Shift+F6` | **Rename** — symbol, file, package (updates all usages) |
| `Ctrl+Alt+V` | **Extract Variable** |
| `Ctrl+Alt+F` | **Extract Field** |
| `Ctrl+Alt+C` | **Extract Constant** |
| `Ctrl+Alt+M` | **Extract Method** |
| `Ctrl+Alt+P` | **Extract Parameter** |
| `F6` | **Move** class/method/package |
| `Ctrl+F6` | **Change Signature** — add/remove/reorder parameters |
| `Ctrl+Alt+N` | **Inline** — inline variable/method/constant |
| `Ctrl+Shift+F6` | **Change Type** |

### Safe refactoring features

- **Preview window** — see all changes before applying
- **Conflict detection** — warns if refactoring would break code
- **Undo** — `Ctrl+Z` undoes the entire refactoring operation
- **Cross-language** — refactorings work across Java, Kotlin, Groovy, XML, etc.

### Common refactoring workflows

```
1. Extract Method:    Select code → Ctrl+Alt+M → name it → parameters auto-detected
2. Extract Interface: Refactor This → Extract Interface → select methods
3. Pull Members Up:   Move methods/fields from subclass to superclass
4. Push Members Down: Move methods/fields from superclass to subclass
5. Encapsulate Fields: Generate getters/setters, replace direct access
6. Replace Inheritance with Delegation: Convert "is-a" to "has-a"
```

---

## 8. Debugging

### Starting a debug session

| Action | How |
|---|---|
| **Debug main method** | Click green arrow in gutter → Debug, or `Shift+F9` |
| **Debug with custom config** | Run → Edit Configurations → Debug |
| **Attach to process** | Run → Attach to Process (for remote JVMs) |
| **Hot reload** | Modify code during debug → `Ctrl+F9` (Build) → classes are reloaded |

### Breakpoint types

| Type | Description | How to set |
|---|---|---|
| **Line** | Stops at a specific line | Click in the gutter |
| **Method** | Stops on method entry/exit | `Ctrl+F8` on method signature |
| **Field watchpoint** | Stops when a field is read/modified | `Ctrl+F8` on field |
| **Exception** | Stops when exception is thrown | Run → View Breakpoints → `+` → Exception |
| **Conditional** | Stops only when condition is true | Right-click breakpoint → add condition |
| **Log (non-suspending)** | Logs without stopping | Right-click breakpoint → uncheck "Suspend", enable "Log" |

### Debugging shortcuts

| Shortcut | Action |
|---|---|
| `F8` | **Step Over** — next line, skip method internals |
| `F7` | **Step Into** — enter method call |
| `Shift+F7` | **Smart Step Into** — choose which method to enter (chained calls) |
| `Shift+F8` | **Step Out** — finish current method and return to caller |
| `F9` | **Resume** — continue to next breakpoint |
| `Alt+F8` | **Evaluate Expression** — run arbitrary code in current context |
| `Alt+F9` | **Run to Cursor** — continue to the cursor position |
| `Ctrl+Alt+F9` | **Force Run to Cursor** |
| `Ctrl+F2` | **Stop** debugging |

### Advanced debugging features

- **Evaluate Expression** (`Alt+F8`): Execute any Java expression in the current context
- **Watches**: Add expressions to monitor in the Variables pane
- **Stream Debugger**: Debug Stream API chains step-by-step (click chain icon in debugger)
- **Memory View**: Analyze heap during debugging (Debug → Memory tab)
- **Async Stack Traces**: See full call chain across `CompletableFuture`, virtual threads
- **Drop Frame**: Go back to a previous frame (re-execute from that point)
- **Mark Object**: Label specific object instances during debugging for tracking

---

## 9. Profiling

IntelliJ IDEA integrates **Java Flight Recorder (JFR)** and **Async Profiler** for
performance analysis.

### Running the profiler

1. **From Run Configuration:** Run → Profile (or `Alt+Shift+F9` → Profile)
2. **Attach to running process:** Run → Profile → Attach to Process

### Profiler types

| Profiler | Best For |
|---|---|
| **IntelliJ Profiler** (default, Async Profiler) | CPU & memory profiling, flame graphs |
| **Java Flight Recorder** | Low-overhead production profiling, event recording |

### Reading results

- **Flame Graph** — bottom-up visualization, wider bar = more time spent
- **Call Tree** — top-down tree of method calls with timing
- **Method List** — flat list sorted by "own time" (wall-clock time in that specific method)
- **Timeline** — thread activity over time
- **Events** — GC, locks, I/O events (JFR only)

### Performance tips

- Look for **wide bars** in the flame graph (hot methods)
- Check **GC pauses** in the timeline
- Monitor **thread states** (RUNNING vs WAITING vs BLOCKED)

---

## 10. Build Tools (Gradle & Maven)

### Gradle integration

| Feature | How |
|---|---|
| **Import project** | Open `build.gradle` / `build.gradle.kts` → "Import as Gradle project" |
| **Gradle tool window** | View → Tool Windows → Gradle (or side panel icon) |
| **Run tasks** | Double-click task in Gradle window, or Run Anything (`Ctrl Ctrl`) |
| **Sync** | Refresh button in Gradle window or automatic after `build.gradle` changes |
| **Composite builds** | Supported — reference included builds in the Gradle window |

### Maven integration

| Feature | How |
|---|---|
| **Import project** | Open `pom.xml` → "Import as Maven project" |
| **Maven tool window** | View → Tool Windows → Maven |
| **Run goals** | Double-click goal or use Run Anything |
| **Dependency diagram** | Right-click module → Diagrams → Show Dependencies |

### Dependency management

- **Library suggestions:** IntelliJ can suggest adding libraries when you use unresolved classes
- **Dependency conflicts:** Maven → Analyze Dependencies → see conflict resolution
- **Version catalog (Gradle):** Full support for `libs.versions.toml`

---

## 11. Version Control (Git)

### Key Git features

| Feature | Shortcut / Access |
|---|---|
| **Commit** | `Ctrl+K` |
| **Push** | `Ctrl+Shift+K` |
| **Pull/Update** | `Ctrl+T` |
| **Branches** | `Ctrl+Shift+`` ` (backtick) |
| **Git Log** | Alt+9 or View → Tool Windows → Git → Log |
| **Diff** | `Ctrl+D` on file in Changes |
| **Annotate (Blame)** | Right-click gutter → Annotate with Git Blame |
| **Local History** | Right-click file → Local History → Show History |
| **Shelve/Stash** | Git → Shelve Changes / Stash Changes |
| **Cherry-pick** | Right-click commit in Log → Cherry-Pick |
| **Interactive rebase** | Right-click branch → Rebase interactively |

### Commit dialog features

- **Diff preview** — see changes before committing
- **Before commit checks:** Run tests, run inspections, optimize imports, reformat code
- **Amend commit** — modify the last commit
- **Sign-off** — `Signed-off-by` trailer
- **Partial commits** — select specific chunks/lines to commit (changelists)

---

## 12. Terminal & Command-Line Tools

- **Built-in terminal:** `Alt+F12` — full shell inside the IDE
- **Multi-tab terminals** — click `+` to open multiple terminal tabs
- **Split terminal** — right-click tab → Split Vertically/Horizontally
- **Run Anything** — `Ctrl Ctrl` (double Control) — run Gradle/Maven tasks, shell commands

---

## 13. Testing

### Supported test frameworks

- **JUnit 5** (Jupiter) — first-class support
- **JUnit 4** — supported
- **TestNG** — supported
- **Spock** (Groovy) — supported

### Testing shortcuts

| Shortcut | Action |
|---|---|
| `Ctrl+Shift+T` | **Go to Test / Create Test** — navigate between class and its test |
| `Ctrl+Shift+F10` | **Run current test** (cursor in test method/class) |
| `Alt+Insert → Test Method` | **Generate test method** |

### Testing features

- **Run gutter icons** — green play button next to each test method/class
- **Test runner** — built-in test results panel with pass/fail/skip counts
- **Re-run failed tests** — click "Rerun Failed Tests" button
- **Coverage** — Run → Run with Coverage → see line/branch coverage in editor gutter
- **Parameterized tests** — full support for `@ParameterizedTest`, `@MethodSource`, etc.

---

## 14. Spring & Jakarta EE Support

### Spring Boot

| Feature | Description |
|---|---|
| **Spring Initializr** | Create new Spring Boot project from within IDE |
| **Bean navigation** | Gutter icons → navigate to bean definitions and injections |
| **Endpoints** | View all REST endpoints in Endpoints tool window |
| **Configuration** | `application.properties` / `application.yml` completion & validation |
| **Profiles** | Autocomplete profile names, detect active profiles |
| **Spring Data** | JPA repository method name completion & query derivation |
| **Actuator** | Run dashboard shows actuator endpoints |

### Jakarta EE

| Feature | Description |
|---|---|
| **JSF, CDI, JPA** | Full support with navigation and completion |
| **Servlets** | Web deployment descriptor support |
| **Server integration** | Tomcat, WildFly, GlassFish, Liberty — deploy and debug |

---

## 15. Database Tools & HTTP Client

### Database tools

- **Data Sources:** Connect to PostgreSQL, MySQL, Oracle, SQL Server, SQLite, MongoDB, etc.
- **Query Console:** Write and execute SQL with completion, syntax highlighting, formatting
- **Data Editor:** Browse/edit table data in a spreadsheet-like view
- **Schema Compare:** Compare database schemas across environments
- **Diagrams:** Visualize table relationships

### HTTP Client

```http
### Get user by ID
GET https://api.example.com/users/{{userId}}
Accept: application/json
Authorization: Bearer {{authToken}}

### Create a new user
POST https://api.example.com/users
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com"
}

> {%
  client.test("Status is 201", function() {
    client.assert(response.status === 201);
  });
%}
```

- Create `.http` files in your project
- **Environment variables:** Use `http-client.env.json` for variables by environment
- **Response handling:** Assert response status, body, headers with JavaScript
- **History:** All requests are saved and replayable

---

## 16. Essential Plugins

| Plugin | Purpose |
|---|---|
| **Lombok** | Annotation processing for `@Data`, `@Builder`, etc. (bundled since 2020.3) |
| **Key Promoter X** | Shows keyboard shortcut when you use a mouse action — learn shortcuts faster |
| **CheckStyle-IDEA** | Run CheckStyle inspections in the IDE |
| **SonarLint** | Live SonarQube/SonarCloud analysis in the editor |
| **MapStruct Support** | Navigation and completion for MapStruct mappers |
| **JPA Buddy** | JPA entity generation, Flyway/Liquibase migrations |
| **Rainbow Brackets** | Color-coded matching brackets |
| **String Manipulation** | Case conversion, escaping, sorting lines |
| **GitToolBox** | Inline blame, status bar info, auto fetch |
| **Docker** | Dockerfile support, container management |

> Install plugins: **Settings → Plugins → Marketplace**

---

## 17. Essential Keyboard Shortcuts

### Editing

| Shortcut | Action |
|---|---|
| `Alt+Enter` | **Quick Fix / Show Intentions** |
| `Ctrl+Space` | **Basic Completion** |
| `Ctrl+Shift+Space` | **Smart Completion** |
| `Ctrl+Shift+Enter` | **Complete Statement** |
| `Ctrl+P` | **Parameter Info** (show method signature) |
| `Ctrl+Q` | **Quick Documentation** |
| `Ctrl+/` | **Line Comment** toggle |
| `Ctrl+Shift+/` | **Block Comment** toggle |
| `Ctrl+D` | **Duplicate Line/Selection** |
| `Ctrl+Y` | **Delete Line** |
| `Alt+Shift+↑/↓` | **Move Line Up/Down** |
| `Ctrl+Shift+↑/↓` | **Move Statement Up/Down** |
| `Ctrl+W` | **Extend Selection** (word → expression → line → block → method) |
| `Ctrl+Shift+W` | **Shrink Selection** |
| `Ctrl+Alt+L` | **Reformat Code** |
| `Ctrl+Alt+O` | **Optimize Imports** |
| `Ctrl+Shift+J` | **Join Lines** |

### Finding

| Shortcut | Action |
|---|---|
| `Ctrl+F` | **Find** in current file |
| `Ctrl+R` | **Replace** in current file |
| `Ctrl+Shift+F` | **Find in Path** (project-wide search) |
| `Ctrl+Shift+R` | **Replace in Path** |
| `Alt+F7` | **Find Usages** |
| `Ctrl+Shift+F7` | **Highlight Usages** in current file |

### Running & Debugging

| Shortcut | Action |
|---|---|
| `Shift+F10` | **Run** current configuration |
| `Shift+F9` | **Debug** current configuration |
| `Ctrl+Shift+F10` | **Run** context configuration (test at cursor, main method) |
| `Ctrl+F5` | **Rerun** last configuration |
| `F8` / `F7` | **Step Over** / **Step Into** |
| `Shift+F8` | **Step Out** |
| `Alt+F8` | **Evaluate Expression** |

---

## 18. Tips & Tricks

### Productivity tips

1. **Use Search Everywhere (`Shift Shift`) for everything** — it's faster than navigating menus
2. **Learn 5 shortcuts per week** — use Key Promoter X to track mouse actions
3. **Use `Alt+Enter` constantly** — it's context-aware and offers the best available fix
4. **Extend selection (`Ctrl+W`)** — faster than manual selection for refactoring
5. **Multiple cursors (`Alt+Click`)** — edit multiple locations simultaneously; also `Ctrl+Alt+Shift+Click` to add carets to matching text
6. **Postfix completion** — type `.var`, `.if`, `.for` after expressions instead of typing the structure first
7. **Evaluate Expression in debug** — test fixes without stopping the debugger
8. **Local History** — recover changes even if you forget to commit
9. **File templates** — Settings → Editor → File and Code Templates → customize `Class`, `Interface`, etc.
10. **Scratch files** (`Ctrl+Alt+Shift+Insert`) — temporary files for experiments, run without a project

### Performance tuning for large projects

- Increase IDE heap: **Help → Change Memory Settings** (default 2 GB, try 4 GB for large projects)
- Exclude directories: Mark build output, generated sources, node_modules as Excluded
- Disable unused plugins: Settings → Plugins → disable what you don't need
- Use **Power Save Mode** when on battery or working with huge files (disables background analysis)
- Clear caches if IDE is slow: **File → Invalidate Caches / Restart**

### Customization

- **Code Style:** Settings → Editor → Code Style → Java — configure tabs/spaces, braces, imports
- **Inspection Profiles:** Settings → Editor → Inspections — enable/disable/customize severity
- **Color Scheme:** Settings → Editor → Color Scheme — customize syntax colors
- **Keymap:** Settings → Keymap — rebind any shortcut
- **Shared settings:** Export/import settings via File → Manage IDE Settings, or use Settings Repository plugin

---

## 19. Learning Resources

### Official JetBrains resources

| Resource | URL | Format |
|---|---|---|
| **IntelliJ IDEA Guide** | [jetbrains.com/guide/java](https://www.jetbrains.com/guide/java/) | Tutorials, tips, screencasts |
| **IntelliJ IDEA Documentation** | [jetbrains.com/help/idea](https://www.jetbrains.com/help/idea/) | Full reference docs |
| **IntelliJ IDEA Blog** | [blog.jetbrains.com/idea](https://blog.jetbrains.com/idea/) | Release news, tips |
| **JetBrains YouTube** | [youtube.com/@intellijidea](https://www.youtube.com/@intellijidea) | Video tutorials |
| **IntelliJ IDEA Tips & Tricks** | [jetbrains.com/idea/features](https://www.jetbrains.com/idea/features/) | Feature highlights |
| **What's New** | [jetbrains.com/idea/whatsnew](https://www.jetbrains.com/idea/whatsnew/) | Latest version changes |
| **Keyboard Shortcuts PDF** | Help → Keyboard Shortcuts PDF (in IDE) | Printable reference card |

### Community & third-party

| Resource | Description |
|---|---|
| **Dariusz Mydlarz — IntelliJ Tips** | Popular YouTube channel with short tip videos |
| **Trisha Gee — JetBrains Developer Advocate** | Conference talks on IntelliJ productivity |
| **Marco Behler — IntelliJ IDEA guides** | Practical written tutorials |
| **Vlad Mihalcea — IntelliJ for Java** | Blog posts on IDE configuration for Java developers |

### Key YouTube playlists

- **IntelliJ IDEA. Tips & Tricks** — official playlist, short actionable tips
- **42 IntelliJ IDEA Tips and Tricks** — curated efficiency tips
- **IntelliJ IDEA Debugger** — deep dive into debugging features
- **IntelliJ IDEA for Beginners** — complete setup and first steps
