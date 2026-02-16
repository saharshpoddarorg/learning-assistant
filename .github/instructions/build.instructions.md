---
applyTo: "**/*.gradle"
---

# Build Instructions for Capital Project

## Project Overview
**Build System:** Gradle (with legacy Ant support)  
**Root Project:** `iesmodules`  
**Build Files:** `build.gradle`, `settings.gradle`, `iesinit.gradle`  
**Git Remote:** `iesd25` (NOT `origin`)

**Key Principle:** This is a multi-module Gradle project with 200+ modules. Use targeted module builds whenever possible instead of full project builds.

---

## Git Configuration

### Git Remote
**IMPORTANT:** This workspace uses `iesd25` as the remote name, NOT `origin`.

**Common Git Commands:**
```bash
# Fetch updates
git fetch iesd25

# Pull latest changes
git pull iesd25 main

# Push changes
git push iesd25 your-branch-name

# View remote configuration
git remote -v

# Check current branch
git branch

# Checkout existing branch
git checkout branch-name

# Create and checkout new branch
git checkout -b feature/my-new-feature

# View commit history
git log --oneline -10

# View changes
git status
git diff

# Stage and commit
git add .
git commit -m "Your commit message"

# Push new branch to remote
git push -u iesd25 feature/my-new-feature
```

---

## Project Module Structure

The project contains 200+ Gradle modules organized into major source directories:

### Major Module Categories

#### Core Framework Modules
- **`capitalcaf`** - Capital Application Framework (CAF)
- **`capitalcof`** - Capital Object Framework interfaces
- **`capitalcofimpl`** - COF implementations
- **`capitalcofutils`** - COF utilities
- **`capitalcommon`** - Common utilities

#### Manager & Persistence
- **`capitalcapmanimpl`** - Capital Manager implementation
- **`capitalpostgres`** - PostgreSQL support
- **`capitalddl`** - Database DDL scripts
- **`capitalpof`** - Persistence Object Framework

#### Domain Modules
- **`capitallogic`** - Logical design (electrical schematics)
- **`capitalharness`** - Physical harness design
- **`capitaltopo`** - Topology management
- **`capitalans`** - Analysis services
- **`capitaldrafting`** - Drafting functionality

#### Bridge Adapters (CAD/PLM Integration)
- **`capitalbridges`** - Bridge implementation framework
- **`capitalbridgessdk`** - Bridge SDK
- **`capitalkbl`** - KBL adapter
- **`capitalugsnx2`** - NX adapter
- **`capitaltc`** - Teamcenter adapter
- **`capitalenovia`** - Enovia adapter
- **`capitalproe`** - ProE adapter

#### Tools & Services
- **`capitalboot`** - Boot/launcher
- **`capitalclaunch`** - Client launcher
- **`capitalrestserver`** - REST server
- **`capitalwebservices`** - Web services
- **`capitalreporterserver`** - Reporting server

#### Testing Modules
- **`capitaltestscof`** - COF tests
- **`capitaltestscaf`** - CAF tests
- **`capitaltestlogic`** - Logic tests
- **`capitaltestharness`** - Harness tests

---

## Common Build Commands

### Full Project Build

**Build Everything (WARNING: Takes 30-60+ minutes):**
```bash
gradle build
```

**Clean and Build Everything:**
```bash
gradle clean build
```

**Build Without Tests:**
```bash
gradle build -x test
```

**Parallel Build (Faster):**
```bash
gradle build --parallel --max-workers=8
```

### Module-Specific Builds

**Build Single Module:**
```bash
gradle :module-name:build
```

**Examples of Module Builds:**
```bash
# Core framework modules
gradle :capitalcof:build
gradle :capitalcofimpl:build
gradle :capitalcofutils:build
gradle :capitalcaf:build

# Domain modules
gradle :capitallogic:build
gradle :capitalharness:build
gradle :capitaltopo:build

# Manager module
gradle :capitalcapmanimpl:build

# Bridge modules
gradle :capitalbridges:build
gradle :capitalkbl:build
gradle :capitalugsnx2:build

# Testing modules
gradle :capitaltestscof:build
gradle :capitaltestlogic:build
```

**Build Multiple Specific Modules:**
```bash
gradle :capitalcof:build :capitalcofimpl:build :capitalcofutils:build
```

**Clean and Build Specific Module:**
```bash
gradle :capitalharness:clean :capitalharness:build
```

### Compile Only (No JAR Creation)

**Compile Single Module:**
```bash
gradle :module-name:compileJava
```

**Example:**
```bash
gradle :capitallogic:compileJava
gradle :capitalharness:compileJava
gradle :capitalcofutils:compileJava
```

### Testing

**Run Tests for Specific Module:**
```bash
gradle :module-name:test
```

**Examples:**
```bash
gradle :capitaltestscof:test
gradle :capitaltestlogic:test
gradle :capitaltestharness:test
```

**Run Tests with Detailed Output:**
```bash
gradle :module-name:test --info
```

**Run Single Test Class:**
```bash
gradle :module-name:test --tests com.example.MyTestClass
```

**Run Tests Matching Pattern:**
```bash
gradle :module-name:test --tests "*Integration*"
```

**Skip Tests:**
```bash
gradle build -x test
```

### JAR Tasks

**Create JAR for Specific Module:**
```bash
gradle :module-name:jar
```

**Create All JARs:**
```bash
gradle jar
```

**List JAR Contents:**
```bash
jar -tf build/libs/module-name.jar
```

### Dependency Management

**Show Dependencies for Module:**
```bash
gradle :module-name:dependencies
```

**Show Runtime Dependencies:**
```bash
gradle :module-name:dependencies --configuration runtimeClasspath
```

**Show Compile Dependencies:**
```bash
gradle :module-name:dependencies --configuration compileClasspath
```

**Dependency Insight (Why is this dependency included?):**
```bash
gradle :module-name:dependencyInsight --dependency library-name
```

**Refresh Dependencies (Force Download):**
```bash
gradle build --refresh-dependencies
```

### Clean Tasks

**Clean Specific Module:**
```bash
gradle :module-name:clean
```

**Clean Everything:**
```bash
gradle clean
```

**Clean Multiple Modules:**
```bash
gradle :capitalcof:clean :capitalcofimpl:clean :capitalcofutils:clean
```

### IntelliJ IDEA Integration

**Generate IntelliJ IDEA Project Files:**
```bash
gradle ideaProject
```

**Generate Module Files:**
```bash
gradle ideaModule
```

**Run Complete IDEA Setup:**
```bash
gradle runIdeaModules
```

**Clean IDEA Files:**
```bash
gradle cleanIdea
```

### Information & Diagnostics

**List All Tasks:**
```bash
gradle tasks
```

**List All Tasks Including Internal:**
```bash
gradle tasks --all
```

**Show Project Structure:**
```bash
gradle projects
```

**Show Build Information:**
```bash
gradle --version
```

**Show Properties:**
```bash
gradle properties
```

**Dry Run (Show What Would Execute):**
```bash
gradle build --dry-run
```

**Build with Debug Logging:**
```bash
gradle build --debug
```

**Build with Info Logging:**
```bash
gradle build --info
```

**Build Scan (Performance Analysis):**
```bash
gradle build --scan
```

---

## Finding the Right Module for a File

### Method 1: Check settings.gradle

Open `settings.gradle` and search for the directory path. For example:
```groovy
includeBuild("charness_src/src/capitalharness") {name="capitalharness"}
```

This means files in `charness_src/src/capitalharness/` belong to module `:capitalharness`

### Method 2: Module Name Mapping Pattern

**Source Directory → Module Name Pattern:**

| Source Path | Module Name | Command |
|-------------|-------------|---------|
| `cframework_src/src/caf/` | `:capitalcaf` | `gradle :capitalcaf:build` |
| `clogic_src/src/capitallogic/` | `:capitallogic` | `gradle :capitallogic:build` |
| `charness_src/src/capitalharness/` | `:capitalharness` | `gradle :capitalharness:build` |
| `ctopology_src/src/capitaltopo/` | `:capitaltopo` | `gradle :capitaltopo:build` |
| `datamodel_src/src/impl/cofImpl/` | `:capitalcofimpl` | `gradle :capitalcofimpl:build` |
| `datamodel_src/src/utils/` | `:capitalcofutils` | `gradle :capitalcofutils:build` |
| `interfaces_src/src/java/` | `:capitalcof` | `gradle :capitalcof:build` |
| `interfaces_src/src/utils/` | `:capitalutils` | `gradle :capitalutils:build` |
| `cmanager_src/src/capitalManager/` | `:capitalcapmanimpl` | `gradle :capitalcapmanimpl:build` |
| `cbridges_src/src/impl/` | `:capitalbridges` | `gradle :capitalbridges:build` |
| `cbridges_src/src/adpt/kbl/` | `:capitalkbl` | `gradle :capitalkbl:build` |
| `cbridges_src/src/adpt/ugsnx2/` | `:capitalugsnx2` | `gradle :capitalugsnx2:build` |
| `cbridges_src/src/adpt/tceng/` | `:capitaltc` | `gradle :capitaltc:build` |

### Method 3: Search settings.gradle Programmatically

**PowerShell Command:**
```powershell
# Find module for a file path
$filePath = "charness_src/src/capitalharness/MyFile.java"
$directory = Split-Path $filePath -Parent
Select-String -Path settings.gradle -Pattern $directory
```

**Bash/Linux:**
```bash
# Find module for a file path
FILE_PATH="charness_src/src/capitalharness/MyFile.java"
DIR_PATH=$(dirname "$FILE_PATH")
grep "$DIR_PATH" settings.gradle
```

### Method 4: Common Module Groups

**If file is in:**
- `cframework_src/` → Likely `:capitalcaf` or `:capitalcofcafimpl`
- `clogic_src/` → Likely `:capitallogic`
- `charness_src/` → Likely `:capitalharness`
- `ctopology_src/` → Likely `:capitaltopo`
- `cmanager_src/` → Likely `:capitalcapmanimpl`, `:capitalpostgres`, or `:capitalddl`
- `datamodel_src/src/impl/cofImpl/` → `:capitalcofimpl`
- `datamodel_src/src/utils/` → `:capitalcofutils`
- `interfaces_src/src/java/` → `:capitalcof`
- `interfaces_src/src/grpc/` → `:capitalgrpc`
- `cbridges_src/src/impl/` → `:capitalbridges`
- `cbridges_src/src/adpt/*` → Bridge-specific module (e.g., `:capitalkbl`, `:capitalugsnx2`)

---

## Workflow: Compile and Build a Specific File

### Step 1: Identify the Module

**Example:** You modified `clogic_src/src/capitallogic/src/chs/caplets/logic/actions/MyAction.java`

**Check settings.gradle:**
```bash
grep "clogic_src/src/capitallogic" settings.gradle
```

**Output:**
```groovy
includeBuild("clogic_src/src/capitallogic") {name="capitallogic"}
```

**Module Name:** `:capitallogic`

### Step 2: Compile the Module

```bash
gradle :capitallogic:compileJava
```

This compiles Java source files without creating JAR.

### Step 3: Build the Module (Compile + Create JAR)

```bash
gradle :capitallogic:build
```

This compiles and creates the JAR file in `clogic_src/src/capitallogic/build/libs/`

### Step 4: Verify Build Output

```bash
# Check build directory
ls clogic_src/src/capitallogic/build/libs/

# Check classes directory
ls clogic_src/src/capitallogic/build/classes/java/main/
```

---

## Common Build Scenarios

### Scenario 1: Modified a File in capitalharness Module

**File:** `charness_src/src/capitalharness/src/chs/harness/HarnessUtils.java`

**Commands:**
```bash
# Just compile
gradle :capitalharness:compileJava

# Compile and build JAR
gradle :capitalharness:build

# Clean, compile, and build
gradle :capitalharness:clean :capitalharness:build
```

### Scenario 2: Modified Multiple Related Files

**Files in:**
- `interfaces_src/src/java/` (`:capitalcof`)
- `datamodel_src/src/impl/cofImpl/` (`:capitalcofimpl`)
- `datamodel_src/src/utils/` (`:capitalcofutils`)

**Commands:**
```bash
# Build all three modules
gradle :capitalcof:build :capitalcofimpl:build :capitalcofutils:build

# Or with parallel execution
gradle :capitalcof:build :capitalcofimpl:build :capitalcofutils:build --parallel
```

### Scenario 3: Modified gRPC Proto File

**File:** `interfaces_src/src/grpc/proto/capitalManager.proto`

**Commands:**
```bash
# Build gRPC module (generates Java from proto)
gradle :capitalgrpc:build

# Then build server implementation
gradle :capitalcapmanimpl:build

# Then build client utilities
gradle :capitalcofutils:build
```

### Scenario 4: Modified Bridge Adapter

**File:** `cbridges_src/src/adpt/kbl/KBLAdapter.java`

**Commands:**
```bash
# Build KBL adapter module
gradle :capitalkbl:build

# Also build bridges SDK if interfaces changed
gradle :capitalbridgessdk:build
```

### Scenario 5: Running Tests After Changes

**File:** `clogic_src/src/capitallogic/src/chs/caplets/logic/LogicCaplet.java`

**Commands:**
```bash
# Build the module
gradle :capitallogic:build

# Run tests
gradle :capitaltestlogic:test

# Or do both
gradle :capitallogic:build :capitaltestlogic:test
```

### Scenario 6: Database Schema Changes

**File:** `cmanager_src/src/postgres/ddl/create_tables.sql`

**Commands:**
```bash
# Build postgres module
gradle :capitalpostgres:build

# Build DDL module
gradle :capitalddl:build

# Build manager implementation
gradle :capitalcapmanimil:build
```

---

## Build Performance Optimization

### Use Parallel Builds

```bash
gradle build --parallel --max-workers=8
```

### Use Build Cache

```bash
gradle build --build-cache
```

### Build Only What Changed

```bash
# Gradle automatically detects changes, but you can force incremental
gradle build --continuous
```

### Use Gradle Daemon

The daemon is enabled by default. Check status:
```bash
gradle --status
```

Stop daemon (if needed):
```bash
gradle --stop
```

### Skip Unnecessary Tasks

```bash
# Skip tests
gradle build -x test

# Skip specific tasks
gradle build -x :module-name:someTask
```

---

## Common Build Patterns

### Pattern 1: Interface → Implementation → UI

When changing interfaces that have implementations:

```bash
# 1. Build interface module
gradle :capitalcof:build

# 2. Build implementation module
gradle :capitalcofimpl:build

# 3. Build dependent modules (e.g., CAF, Logic, Harness)
gradle :capitalcaf:build :capitallogic:build :capitalharness:build
```

### Pattern 2: Framework → Caplet → Tests

When changing framework or caplet code:

```bash
# 1. Build framework
gradle :capitalcaf:build

# 2. Build caplet
gradle :capitallogic:build

# 3. Run tests
gradle :capitaltestlogic:test
```

### Pattern 3: Proto → Server → Client

When changing gRPC proto definitions:

```bash
# 1. Generate proto classes
gradle :capitalgrpc:build

# 2. Build server implementation
gradle :capitalcapmanimpl:build

# 3. Build client utilities
gradle :capitalcofutils:build
```

---

## Troubleshooting Build Issues

### Issue: "Could not find or load main class"

**Solution:**
```bash
# Clean and rebuild
gradle clean build

# Or specific module
gradle :module-name:clean :module-name:build
```

### Issue: "Dependency not found"

**Solution:**
```bash
# Refresh dependencies
gradle build --refresh-dependencies

# Or clear cache
rm -rf ~/.gradle/caches/
gradle build
```

### Issue: "Out of memory"

**Solution:**
Edit `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4g -XX:MaxMetaspaceSize=512m
```

### Issue: "Task execution failed"

**Solution:**
```bash
# Run with stack trace
gradle build --stacktrace

# Run with full debug output
gradle build --debug > build-debug.log 2>&1
```

### Issue: "Compilation failed with errors"

**Solution:**
```bash
# Check specific module compilation
gradle :module-name:compileJava --info

# Check for conflicting dependencies
gradle :module-name:dependencies
```

### Issue: "Tests failing"

**Solution:**
```bash
# Run single test
gradle :module-name:test --tests "TestClassName"

# Run with verbose output
gradle :module-name:test --info

# Skip tests temporarily
gradle build -x test
```

---

## Gradle Configuration Files

### gradle.properties
Location: `gradle.properties` (root directory)

**Common Settings:**
```properties
org.gradle.jvmargs=-Xmx4g -XX:MaxMetaspaceSize=512m
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.daemon=true
```

### build.gradle
Location: `build.gradle` (root directory)

**Key Configurations:**
- Plugin applications
- Default tasks
- IntelliJ IDEA integration
- Custom tasks

### settings.gradle
Location: `settings.gradle` (root directory)

**Contains:**
- Root project name
- All module includes (200+ modules)
- Build exclusions
- Multi-project configuration

### iesinit.gradle
Location: `iesinit.gradle` (root directory)

**Contains:**
- Project-wide initialization
- Common properties
- Path configurations
- Environment setup

---

## Do's and Don'ts

### Do:
✅ Use module-specific builds when changing individual files  
✅ Use parallel builds for faster compilation (`--parallel`)  
✅ Check `settings.gradle` to find correct module name  
✅ Use `git fetch iesd25` and `git push iesd25` (NOT `origin`)  
✅ Clean before building if you encounter strange errors  
✅ Use `compileJava` for quick syntax checking  
✅ Run tests after making changes to ensure nothing broke  
✅ Use `--info` or `--debug` flags when debugging build issues  
✅ Build dependent modules when changing interfaces  
✅ Use build cache for faster builds (`--build-cache`)  

### Don't:
❌ Don't run `gradle build` for the entire project unless necessary (takes 30-60+ minutes)  
❌ Don't use `git push origin` - use `git push iesd25` instead  
❌ Don't modify `settings.gradle` without understanding module dependencies  
❌ Don't skip tests if you're pushing to shared branches  
❌ Don't commit build artifacts (`build/` directories, `.class` files)  
❌ Don't run multiple gradle commands simultaneously in same workspace  
❌ Don't ignore dependency errors - they cascade into other modules  
❌ Don't forget to build proto modules before building implementation  
❌ Don't use outdated cached dependencies - refresh if needed  
❌ Don't mix Ant and Gradle builds - prefer Gradle for everything  

---

## Quick Reference: File Path → Module Name → Build Command

| File Path | Module | Build Command |
|-----------|--------|---------------|
| `cframework_src/src/caf/` | `:capitalcaf` | `gradle :capitalcaf:build` |
| `clogic_src/src/capitallogic/` | `:capitallogic` | `gradle :capitallogic:build` |
| `charness_src/src/capitalharness/` | `:capitalharness` | `gradle :capitalharness:build` |
| `ctopology_src/src/capitaltopo/` | `:capitaltopo` | `gradle :capitaltopo:build` |
| `cmanager_src/src/capitalManager/` | `:capitalcapmanimpl` | `gradle :capitalcapmanimpl:build` |
| `interfaces_src/src/java/` | `:capitalcof` | `gradle :capitalcof:build` |
| `interfaces_src/src/utils/` | `:capitalutils` | `gradle :capitalutils:build` |
| `interfaces_src/src/grpc/` | `:capitalgrpc` | `gradle :capitalgrpc:build` |
| `datamodel_src/src/impl/cofImpl/` | `:capitalcofimpl` | `gradle :capitalcofimpl:build` |
| `datamodel_src/src/utils/` | `:capitalcofutils` | `gradle :capitalcofutils:build` |
| `cbridges_src/src/impl/` | `:capitalbridges` | `gradle :capitalbridges:build` |
| `cbridges_src/src/adpt/kbl/` | `:capitalkbl` | `gradle :capitalkbl:build` |
| `cbridges_src/src/adpt/ugsnx2/` | `:capitalugsnx2` | `gradle :capitalugsnx2:build` |
| `cbridges_src/src/adpt/tceng/` | `:capitaltc` | `gradle :capitaltc:build` |

---

## Advanced Commands

### Custom Task Execution

**Run Custom Task:**
```bash
gradle taskName
```

**Run Task for Specific Module:**
```bash
gradle :module-name:taskName
```

### Build Specific Configuration

**Build for Debug:**
```bash
gradle build -Pdebug=true
```

**Build for Release:**
```bash
gradle build -Prelease=true
```

### Continuous Build (Watch Mode)

**Auto-rebuild on changes:**
```bash
gradle build --continuous
```

**Watch specific module:**
```bash
gradle :capitallogic:build --continuous
```

### Offline Build (No Network)

**Build without downloading dependencies:**
```bash
gradle build --offline
```

### Profile Build Performance

**Generate build report:**
```bash
gradle build --profile
```

Report saved to: `build/reports/profile/`

### Composite Build Commands

**Build included builds:**
```bash
gradle :included-build-name:taskName
```

---

## Environment Variables

**Common environment variables used by the build:**

```bash
# Java home
JAVA_HOME=/path/to/jdk-21

# Gradle options
GRADLE_OPTS="-Xmx4g -XX:MaxMetaspaceSize=512m"

# Capital-specific
CHS_HOME=/path/to/chs_home
CHS_BUILD_TOOLS_HOME=/path/to/build/tools
```

---

## Useful Aliases (Optional PowerShell/Bash Setup)

### PowerShell Aliases

Add to your PowerShell profile (`$PROFILE`):

```powershell
# Gradle shortcuts
function gb { gradle build }
function gbc { gradle build --continuous }
function gcb { gradle clean build }
function gt { gradle test }
function gm { param($module) gradle ":$module:build" }

# Git shortcuts for iesd25 remote
function gf { git fetch iesd25 }
function gp { git pull iesd25 }
function gpu { param($branch) git push iesd25 $branch }
function gs { git status }
function gd { git diff }
```

### Bash Aliases

Add to your `~/.bashrc` or `~/.bash_profile`:

```bash
# Gradle shortcuts
alias gb='gradle build'
alias gbc='gradle build --continuous'
alias gcb='gradle clean build'
alias gt='gradle test'
alias gm='function _gm(){ gradle ":$1:build"; }; _gm'

# Git shortcuts for iesd25 remote
alias gf='git fetch iesd25'
alias gp='git pull iesd25'
alias gpu='function _gpu(){ git push iesd25 "$1"; }; _gpu'
alias gs='git status'
alias gd='git diff'
```

---

## Additional Resources

- **Gradle Documentation:** https://docs.gradle.org/
- **Multi-Project Builds:** https://docs.gradle.org/current/userguide/multi_project_builds.html
- **Dependency Management:** https://docs.gradle.org/current/userguide/dependency_management.html
- **Build Performance:** https://docs.gradle.org/current/userguide/performance.html
