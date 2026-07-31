# PSI Tools — Action Catalog

Complete reference of all CLI actions, their parameters, and response shapes.

> **Note:** The server resolves the project path internally.
> You never need to set `projectPath` manually.

---

## Meta Actions

### `health`
Check if the PSI Tools server and inspection server are reachable. Sends an echo round-trip to each server.

**Args:** none (`'{}'`)

**Response:**
```json
{
  "psiServer": { "status": "up", "port": 3000 },
  "inspectionServer": { "status": "up", "port": 3001 },
  "summary": "All servers operational"
}
```

---

### `help`
Show available actions, version, and current configuration.

**Args:** none (`'{}'`)

---

### `ping`
Quick liveness check — sends an echo round-trip to the PSI Tools server.

**Args:** none

---

### `list_tools`
List all registered tools on the server.

**Args:** none

**Response:** Array of `{ name, description }`.

---

## Diagnostic Actions

### `echo`
Echo back a message. Useful for testing connectivity.

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `message` | string | ✅ | — | Message to echo |

---

### `server_info`
Get PSI Tools server and IntelliJ IDE information.

**Args:** none

---

## Search Actions

### `symbol_search`
Search for symbols (classes, methods, fields) by name. Supports partial matching.

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `query` | string | ✅ | — | Symbol name or partial name |
| `kind` | enum | ❌ | `"all"` | `"class"` \| `"interface"` \| `"enum"` \| `"annotation"` \| `"method"` \| `"field"` \| `"all"` |
| `limit` | integer | ❌ | `50` | Max results |

---

### `file_search`
Search for files by name or glob pattern.

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `pattern` | string | ✅ | — | File name or pattern. Supports `*` and `?` wildcards (e.g., `UserService`, `*.java`) |
| `includeTests` | boolean | ❌ | `true` | Include test source files |

**Response:** Array of file descriptors with size, language, package, test flag.

---

### `text_search`
Full-text search across project files using IntelliJ's indexed search.

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `query` | string | ✅ | — | Text string or regex pattern |
| `regex` | boolean | ❌ | `false` | Treat query as regex |
| `filePattern` | string | ❌ | all files | Glob filter (e.g., `*.java`, `*.xml`) |
| `caseSensitive` | boolean | ❌ | `true` | Case-sensitive match |

**Response:** Hierarchical results grouped by file.

---

## Structure Actions

### `get_class_structure`
Get complete class structure: fields, methods, constructors, inner classes, annotations, superclass, interfaces, modifiers.

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `className` | string | ✅ | — | Fully qualified name (e.g., `com.example.MyClass`) |

---

### `get_method_body`
Get full source code of a method (signature, annotations, body).

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `method` | string | ✅ | — | FQN. Append `(String, boolean)` for overloads. Use simple type names. |

---

### `get_imports`
Get all import statements from a Java file.

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `filePath` | string | ✅ | — | Relative or absolute path to `.java` file |

**Response:** Array of `{ fqn, isStatic, isWildcard }`.

---

### `get_annotations`
Get all annotations on a class and optionally its members.

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `className` | string | ✅ | — | Fully qualified name |
| `includeMembers` | boolean | ❌ | `true` | Include method/field/constructor/parameter annotations. `false` = class-level only. |

---

## Usage Actions

### `find_usages`
Find all references/usages of a symbol. Understands semantic references.

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `symbol` | string | ✅ | — | FQN. Append `(String, boolean)` for overloaded methods. |
| `scope` | enum | ❌ | `"project"` | `"project"` \| `"module"` \| `"file"` |
| `includeHierarchy` | boolean | ❌ | `true` | For methods: search full type hierarchy. Ignored for class/field. |

**Caps at 500 results.** Check `truncated` field.

---

## Call Graph & Hierarchy Actions

### `get_call_graph`
Get call graph for a method — callers, callees, or both.

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `method` | string | ✅ | — | FQN. Append `(String, boolean)` for overloads. |
| `direction` | enum | ❌ | `"both"` | `"callers"` \| `"callees"` \| `"both"` |
| `depth` | integer | ❌ | `2` | Traversal depth. Min 1, max 5. Higher = exponentially larger. |
| `maxNodes` | integer | ❌ | `50` | Max nodes. Min 1, max 200. Check `truncated` field. |

---

### `get_method_hierarchy`
Get override hierarchy — base declarations and overriders.

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `method` | string | ✅ | — | FQN. Append `(String, int)` for overloads. Use simple type names. |
| `direction` | enum | ❌ | `"both"` | `"up"` (base declarations) \| `"down"` (overriders) \| `"both"` |

---

### `show_subclasses`
Find all classes extending or implementing a class/interface.

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `className` | string | ✅ | — | FQN |
| `includeIndirect` | boolean | ❌ | `true` | Include transitive subclasses |
| `limit` | integer | ❌ | `50` | Max results |

---

### `show_superclasses`
Get inheritance chain (superclasses and interfaces).

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `className` | string | ✅ | — | FQN |

---

### `find_implementations`
Find concrete implementations of an interface or abstract class.

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `interfaceName` | string | ✅ | — | FQN |
| `includeAbstract` | boolean | ❌ | `false` | Include abstract implementors |
| `limit` | integer | ❌ | `50` | Max results |

---

### `get_type_hierarchy`
Get complete type hierarchy tree (both ancestors and descendants).

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `className` | string | ✅ | — | FQN |
| `direction` | enum | ❌ | `"both"` | `"up"` (ancestors) \| `"down"` (descendants) \| `"both"` |

---

## Dependency Actions

### `explore_class_dependencies`
Explore dependency relationships: what a class uses and what uses it.

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `className` | string | ✅ | — | FQN |
| `direction` | enum | ❌ | `"both"` | `"outgoing"` \| `"incoming"` \| `"both"` |
| `depth` | integer | ❌ | `2` | Min 1, max 3. Use 1 for immediate deps. |
| `includeJdk` | boolean | ❌ | `false` | Include JDK classes |
| `includeLibraries` | boolean | ❌ | `false` | Include third-party library classes |
| `maxClasses` | integer | ❌ | `50` | Min 1, max 200. Check `truncated` field. |

**Relationship types:** `EXTENDS`, `IMPLEMENTS`, `FIELD_TYPE`, `CONSTRUCTOR_CALL`, `STATIC_METHOD_CALL`, `METHOD_PARAMETER`, `METHOD_RETURN_TYPE`, `LOCAL_VARIABLE`, `THROWS`, `ANNOTATION`, `TYPE_ARGUMENT`, `CAST`, `INSTANCEOF`.

---

## Git Actions

### `get_changed_line_ranges`
Get changed files and line ranges from a git diff.

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `diffMode` | enum | ❌ | `"working_tree_vs_head"` | `"working_tree_vs_head"` \| `"working_tree_vs_staged"` \| `"staged_vs_head"` \| `"all_vs_head"` \| `"commit_range"` |
| `commitRange` | string | ❌ | — | Required when `diffMode="commit_range"`. e.g., `main..feature`, `HEAD~3..HEAD` |
| `filePattern` | string | ❌ | all files | Glob filter (e.g., `*.java`) |

**Response:** Array of `{ filePath, changedLineRanges: [[start, end], ...] }`. Ranges are 1-based inclusive.

---

## Inspection Actions

> **Important:** Only use inspection actions when the user **explicitly** asks for inspection reports.
> Never run them as automatic post-generation checks.
> These actions use the inspection server (port 3001) — the CLI routes automatically.

### `get_file_inspections`
Get IntelliJ inspection warnings/errors for a single file.

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `filePath` | string | ✅ | — | Relative or absolute path |
| `changedLineRanges` | array | ❌ | whole file | Array of `[startLine, endLine]` pairs (1-based inclusive). Only problems in these ranges. |
| `minSeverity` | enum | ❌ | `"WARNING"` | `"ERROR"` \| `"WARNING"` \| `"WEAK_WARNING"` \| `"INFO"` |
| `waitForAnalysisMs` | integer | ❌ | `5000` | Max ms to wait for analysis daemon. Min 0, max 30000. |

---

### `get_changeset_inspections`
Get inspections for all changed lines in a git changeset. Automatically computes files/ranges via git diff. Processes up to 50 files in batches of 10.

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `diffMode` | enum | ❌ | `"working_tree_vs_head"` | Same as `get_changed_line_ranges` |
| `commitRange` | string | ❌ | — | Required when `diffMode="commit_range"` |
| `filePattern` | string | ❌ | all files | Glob filter |
| `minSeverity` | enum | ❌ | `"WARNING"` | `"ERROR"` \| `"WARNING"` \| `"WEAK_WARNING"` \| `"INFO"` |
| `waitForAnalysisMs` | integer | ❌ | `5000` | Max ms per file. Min 0, max 30000. |

**Inspectable extensions:** `java`, `kt`, `kts`, `groovy`, `scala`, `xml`, `html`, `xhtml`, `json`, `yaml`, `yml`, `properties`, `sql`, `js`, `ts`, `jsx`, `tsx`, `py`, `gradle`.
