# PSI Tools — Usage Recipes

Concrete examples for all CLI actions using PowerShell.

> **Convention:** `<skill>` means the path to this skill folder (e.g., `.github/skills/_modular/psi-tools`).
> Replace with the actual path in your environment.

---

## Health & Diagnostics

```powershell
# Health check — verify both servers are up
$env:CLI_JSON_ARGS = '{}'; & "<skill>/scripts/psi_tools_cli.ps1" health

# Quick ping (echo round-trip)
$env:CLI_JSON_ARGS = '{}'; & "<skill>/scripts/psi_tools_cli.ps1" ping

# Show available actions and configuration
$env:CLI_JSON_ARGS = '{}'; & "<skill>/scripts/psi_tools_cli.ps1" help

# List all available tools registered on the server
$env:CLI_JSON_ARGS = '{}'; & "<skill>/scripts/psi_tools_cli.ps1" list_tools

# Echo test
$env:CLI_JSON_ARGS = '{"message":"hello"}'; & "<skill>/scripts/psi_tools_cli.ps1" echo

# Server info (IDE version, plugin version, project name)
$env:CLI_JSON_ARGS = '{}'; & "<skill>/scripts/psi_tools_cli.ps1" server_info
```

---

## Symbol Search

```powershell
# Find all classes matching "Service"
$env:CLI_JSON_ARGS = '{"query":"Service","kind":"class","limit":20}'; & "<skill>/scripts/psi_tools_cli.ps1" symbol_search

# Find all interfaces matching "Repository"
$env:CLI_JSON_ARGS = '{"query":"Repository","kind":"interface"}'; & "<skill>/scripts/psi_tools_cli.ps1" symbol_search

# Find methods named "process"
$env:CLI_JSON_ARGS = '{"query":"process","kind":"method","limit":10}'; & "<skill>/scripts/psi_tools_cli.ps1" symbol_search

# Find all symbols (any kind) matching "User"
$env:CLI_JSON_ARGS = '{"query":"User"}'; & "<skill>/scripts/psi_tools_cli.ps1" symbol_search
```

---

## File & Text Search

```powershell
# Find files named UserService
$env:CLI_JSON_ARGS = '{"pattern":"UserService"}'; & "<skill>/scripts/psi_tools_cli.ps1" file_search

# Find all XML files
$env:CLI_JSON_ARGS = '{"pattern":"*.xml"}'; & "<skill>/scripts/psi_tools_cli.ps1" file_search

# Find files excluding tests
$env:CLI_JSON_ARGS = '{"pattern":"*.java","includeTests":false}'; & "<skill>/scripts/psi_tools_cli.ps1" file_search

# Text search for TODO comments in Java files
$env:CLI_JSON_ARGS = '{"query":"TODO","filePattern":"*.java"}'; & "<skill>/scripts/psi_tools_cli.ps1" text_search

# Regex search for deprecated patterns
$env:CLI_JSON_ARGS = '{"query":"@Deprecated","filePattern":"*.java","caseSensitive":true}'; & "<skill>/scripts/psi_tools_cli.ps1" text_search

# Case-insensitive search
$env:CLI_JSON_ARGS = '{"query":"fixme","caseSensitive":false}'; & "<skill>/scripts/psi_tools_cli.ps1" text_search
```

---

## Class Structure & Annotations

```powershell
# Get full class structure
$env:CLI_JSON_ARGS = '{"className":"com.example.OrderService"}'; & "<skill>/scripts/psi_tools_cli.ps1" get_class_structure

# Get annotations on a class and all its members
$env:CLI_JSON_ARGS = '{"className":"com.example.OrderService"}'; & "<skill>/scripts/psi_tools_cli.ps1" get_annotations

# Get class-level annotations only (skip member annotations)
$env:CLI_JSON_ARGS = '{"className":"com.example.OrderService","includeMembers":false}'; & "<skill>/scripts/psi_tools_cli.ps1" get_annotations

# Get imports for a file
$env:CLI_JSON_ARGS = '{"filePath":"src/main/java/com/example/MyService.java"}'; & "<skill>/scripts/psi_tools_cli.ps1" get_imports
```

---

## Usage Finding

```powershell
# Find all usages of a class
$env:CLI_JSON_ARGS = '{"symbol":"com.example.OrderService"}'; & "<skill>/scripts/psi_tools_cli.ps1" find_usages

# Find usages of a method (with hierarchy search)
$env:CLI_JSON_ARGS = '{"symbol":"com.example.OrderService.processOrder"}'; & "<skill>/scripts/psi_tools_cli.ps1" find_usages

# Find usages of an overloaded method (disambiguate)
$env:CLI_JSON_ARGS = '{"symbol":"com.example.MyService.process(String, int)"}'; & "<skill>/scripts/psi_tools_cli.ps1" find_usages

# Find usages without hierarchy (exact method only)
$env:CLI_JSON_ARGS = '{"symbol":"com.example.MyService.process","includeHierarchy":false}'; & "<skill>/scripts/psi_tools_cli.ps1" find_usages

# Find usages scoped to a module
$env:CLI_JSON_ARGS = '{"symbol":"com.example.MyService","scope":"module"}'; & "<skill>/scripts/psi_tools_cli.ps1" find_usages
```

---

## Method Body

```powershell
# Get source code of a method
$env:CLI_JSON_ARGS = '{"method":"com.example.OrderService.processOrder"}'; & "<skill>/scripts/psi_tools_cli.ps1" get_method_body

# Get source of an overloaded method
$env:CLI_JSON_ARGS = '{"method":"com.example.MyService.process(String, int)"}'; & "<skill>/scripts/psi_tools_cli.ps1" get_method_body
```

---

## Call Graph

```powershell
# Who calls this method and what does it call? (default: both, depth 2)
$env:CLI_JSON_ARGS = '{"method":"com.example.OrderService.processOrder"}'; & "<skill>/scripts/psi_tools_cli.ps1" get_call_graph

# Only callers
$env:CLI_JSON_ARGS = '{"method":"com.example.MyService.process","direction":"callers"}'; & "<skill>/scripts/psi_tools_cli.ps1" get_call_graph

# Only callees, deeper traversal
$env:CLI_JSON_ARGS = '{"method":"com.example.MyService.process","direction":"callees","depth":3}'; & "<skill>/scripts/psi_tools_cli.ps1" get_call_graph

# Large call graph with higher node limit
$env:CLI_JSON_ARGS = '{"method":"com.example.MyService.process","depth":3,"maxNodes":100}'; & "<skill>/scripts/psi_tools_cli.ps1" get_call_graph
```

---

## Type Hierarchy

```powershell
# Full type hierarchy (ancestors + descendants)
$env:CLI_JSON_ARGS = '{"className":"com.example.repository.Repository"}'; & "<skill>/scripts/psi_tools_cli.ps1" get_type_hierarchy

# Ancestors only
$env:CLI_JSON_ARGS = '{"className":"com.example.repository.Repository","direction":"up"}'; & "<skill>/scripts/psi_tools_cli.ps1" get_type_hierarchy

# Descendants only
$env:CLI_JSON_ARGS = '{"className":"com.example.repository.Repository","direction":"down"}'; & "<skill>/scripts/psi_tools_cli.ps1" get_type_hierarchy

# Superclasses (inheritance chain)
$env:CLI_JSON_ARGS = '{"className":"com.example.MyServiceImpl"}'; & "<skill>/scripts/psi_tools_cli.ps1" show_superclasses

# Subclasses
$env:CLI_JSON_ARGS = '{"className":"com.example.BaseService","includeIndirect":true}'; & "<skill>/scripts/psi_tools_cli.ps1" show_subclasses

# Direct subclasses only
$env:CLI_JSON_ARGS = '{"className":"com.example.BaseService","includeIndirect":false}'; & "<skill>/scripts/psi_tools_cli.ps1" show_subclasses

# Find implementations of an interface
$env:CLI_JSON_ARGS = '{"interfaceName":"com.example.repository.Repository"}'; & "<skill>/scripts/psi_tools_cli.ps1" find_implementations

# Include abstract implementations
$env:CLI_JSON_ARGS = '{"interfaceName":"com.example.repository.Repository","includeAbstract":true}'; & "<skill>/scripts/psi_tools_cli.ps1" find_implementations
```

---

## Method Hierarchy

```powershell
# Full override hierarchy (base declarations + overriders)
$env:CLI_JSON_ARGS = '{"method":"com.example.repository.Repository.findById"}'; & "<skill>/scripts/psi_tools_cli.ps1" get_method_hierarchy

# What does this method override?
$env:CLI_JSON_ARGS = '{"method":"com.example.MyServiceImpl.process","direction":"up"}'; & "<skill>/scripts/psi_tools_cli.ps1" get_method_hierarchy

# Who overrides this method?
$env:CLI_JSON_ARGS = '{"method":"com.example.BaseService.process","direction":"down"}'; & "<skill>/scripts/psi_tools_cli.ps1" get_method_hierarchy
```

---

## Class Dependencies

```powershell
# Immediate dependencies (what it uses + what uses it)
$env:CLI_JSON_ARGS = '{"className":"com.example.OrderService","depth":1}'; & "<skill>/scripts/psi_tools_cli.ps1" explore_class_dependencies

# Only outgoing (what it depends on)
$env:CLI_JSON_ARGS = '{"className":"com.example.OrderService","direction":"outgoing"}'; & "<skill>/scripts/psi_tools_cli.ps1" explore_class_dependencies

# Include JDK and library classes
$env:CLI_JSON_ARGS = '{"className":"com.example.MyService","includeJdk":true,"includeLibraries":true}'; & "<skill>/scripts/psi_tools_cli.ps1" explore_class_dependencies

# Deeper analysis with higher limit
$env:CLI_JSON_ARGS = '{"className":"com.example.MyService","depth":3,"maxClasses":100}'; & "<skill>/scripts/psi_tools_cli.ps1" explore_class_dependencies
```

---

## Git Changes

```powershell
# All uncommitted changes
$env:CLI_JSON_ARGS = '{}'; & "<skill>/scripts/psi_tools_cli.ps1" get_changed_line_ranges

# Only unstaged changes
$env:CLI_JSON_ARGS = '{"diffMode":"working_tree_vs_staged"}'; & "<skill>/scripts/psi_tools_cli.ps1" get_changed_line_ranges

# Only staged changes
$env:CLI_JSON_ARGS = '{"diffMode":"staged_vs_head"}'; & "<skill>/scripts/psi_tools_cli.ps1" get_changed_line_ranges

# Changes between commits
$env:CLI_JSON_ARGS = '{"diffMode":"commit_range","commitRange":"main..feature-branch"}'; & "<skill>/scripts/psi_tools_cli.ps1" get_changed_line_ranges

# Only Java file changes
$env:CLI_JSON_ARGS = '{"filePattern":"*.java"}'; & "<skill>/scripts/psi_tools_cli.ps1" get_changed_line_ranges
```

---

## Inspections

> Only use when explicitly asked by the user.

```powershell
# Inspect a single file
$env:CLI_JSON_ARGS = '{"filePath":"src/main/java/com/example/MyService.java"}'; & "<skill>/scripts/psi_tools_cli.ps1" get_file_inspections

# Inspect only changed lines in a file
$env:CLI_JSON_ARGS = '{"filePath":"src/main/java/com/example/MyService.java","changedLineRanges":[[10,25],[40,55]]}'; & "<skill>/scripts/psi_tools_cli.ps1" get_file_inspections

# Inspect with higher severity threshold (errors only)
$env:CLI_JSON_ARGS = '{"filePath":"src/main/java/com/example/MyService.java","minSeverity":"ERROR"}'; & "<skill>/scripts/psi_tools_cli.ps1" get_file_inspections

# Inspect all uncommitted changes
$env:CLI_JSON_ARGS = '{}'; & "<skill>/scripts/psi_tools_cli.ps1" get_changeset_inspections

# Inspect staged changes only, Java files, errors only
$env:CLI_JSON_ARGS = '{"diffMode":"staged_vs_head","filePattern":"*.java","minSeverity":"ERROR"}'; & "<skill>/scripts/psi_tools_cli.ps1" get_changeset_inspections

# Inspect a commit range
$env:CLI_JSON_ARGS = '{"diffMode":"commit_range","commitRange":"HEAD~3..HEAD"}'; & "<skill>/scripts/psi_tools_cli.ps1" get_changeset_inspections
```

---

## Multi-Step Workflow: Understand Before Modifying

```powershell
# Step 1: Find the class
$env:CLI_JSON_ARGS = '{"query":"UserService","kind":"class"}'; & "<skill>/scripts/psi_tools_cli.ps1" symbol_search

# Step 2: Get its structure
$env:CLI_JSON_ARGS = '{"className":"com.example.service.UserService"}'; & "<skill>/scripts/psi_tools_cli.ps1" get_class_structure

# Step 3: See who uses it
$env:CLI_JSON_ARGS = '{"symbol":"com.example.service.UserService"}'; & "<skill>/scripts/psi_tools_cli.ps1" find_usages

# Step 4: Check dependencies
$env:CLI_JSON_ARGS = '{"className":"com.example.service.UserService","depth":1}'; & "<skill>/scripts/psi_tools_cli.ps1" explore_class_dependencies
```

---

## Multi-Step Workflow: Find Usages from a Simple Name

```powershell
# Step 1: Resolve the FQN from a simple method or class name
$env:CLI_JSON_ARGS = '{"query":"getDesignDescriptor","kind":"method"}'; & "<skill>/scripts/psi_tools_cli.ps1" symbol_search

# Step 2: Use the FQN from step 1 to find all usages
$env:CLI_JSON_ARGS = '{"symbol":"chs.common.IDesignMgr.getDesignDescriptor(String)"}'; & "<skill>/scripts/psi_tools_cli.ps1" find_usages
```

---

## Multi-Step Workflow: Inspect Changed Code

```powershell
# Step 1: Get changed files and ranges
$env:CLI_JSON_ARGS = '{"filePattern":"*.java"}'; & "<skill>/scripts/psi_tools_cli.ps1" get_changed_line_ranges

# Step 2: Inspect each changed file (use changedLineRanges from step 1)
$env:CLI_JSON_ARGS = '{"filePath":"src/main/java/com/example/MyService.java","changedLineRanges":[[10,25]]}'; & "<skill>/scripts/psi_tools_cli.ps1" get_file_inspections

# Or: inspect everything in one shot
$env:CLI_JSON_ARGS = '{"filePattern":"*.java"}'; & "<skill>/scripts/psi_tools_cli.ps1" get_changeset_inspections
```

---

## Troubleshooting

| Symptom | Fix |
|---------|-----|
| `Server not reachable` | Ensure IntelliJ IDEA is running with the project open. Verify the PSI Tools plugin is active. Run `health` to check. |
| `Server initialization failed` | The PSI Tools plugin may not be loaded. Restart IntelliJ or re-enable the plugin. |
| `Tool not found` | Run `list_tools` to see registered tools. The tool may not be available in your plugin version. |
| `Index not ready` | IntelliJ is still indexing the project. Wait for indexing to complete, then retry. |
| `Class not found` | Ensure you are using the fully qualified name. Run `symbol_search` to find the correct FQN. |
| `truncated: true` | Results exceeded the limit. Narrow your search using `scope`, `limit`, or `depth` parameters. |
| Wrong port | Check IntelliJ Settings → Tools → PSI Tools for the configured port. Update your `.env` file if needed. |
| `Invalid port` | Your `.env` file has a non-numeric or out-of-range port. Fix the value (must be 1–65535). |
