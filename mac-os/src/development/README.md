# mac-os/src/development — macOS Dev Setup Java Experiments

This package contains Java source files for experimenting with macOS development concepts,
system interaction examples, and learning exercises tied to the Mac Dev module.

## Purpose

Use this as a **sandbox** to:
- Try Java programs that spawn and read terminal commands (e.g., check `java --version`)
- Experiment with environment detection on macOS
- Build small utilities that complement the documentation guides in `mac-os/docs/`

## How to Run (from repo root)

```zsh
# Compile a single file
javac mac-os/src/development/MacEnvChecker.java

# Run it
java -cp mac-os/src MacEnvChecker

# Or compile all development sources
javac mac-os/src/development/*.java
```

## Files in This Package

| File | Purpose |
|---|---|
| `MacEnvChecker.java` | Checks/prints key environment info: Java version, JAVA_HOME, Homebrew status, Node, Docker |

## Learning Tier Guide

| Tier | What to do here |
|---|---|
| 🟢 Newbie | Run `MacEnvChecker.java` — see what's installed on your Mac |
| 🟡 Amateur | Modify to add more checks; learn `ProcessBuilder` for shell commands |
| 🔴 Pro | Build a full bootstrap checker that validates the entire dev environment |
