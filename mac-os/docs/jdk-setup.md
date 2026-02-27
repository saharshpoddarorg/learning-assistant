# ☕ JDK Setup on macOS — Open-Source Java Distributions

> Install and manage Java Development Kits on macOS using Homebrew.  
> Covers Eclipse Temurin, Amazon Corretto, Azul Zulu, GraalVM — and multi-JDK management.

---

## 📑 Table of Contents

- [Which JDK to Choose?](#-which-jdk-to-choose)
- [🟢 Newbie — Install Your First JDK](#-newbie--install-your-first-jdk)
- [🟡 Amateur — Multiple Versions & Switching](#-amateur--multiple-versions--switching)
- [🔴 Pro — jenv, GraalVM & CI/CD](#-pro--jenv-graalvm--cicd)
- [JDK Distribution Comparison](#-jdk-distribution-comparison)
- [Troubleshooting](#-troubleshooting)

---

## 🔍 Which JDK to Choose?

All listed distributions are **free, open-source, production-ready** OpenJDK builds:

| Distribution | Recommended For | Notes |
|---|---|---|
| **Eclipse Temurin** | Most developers (default choice) | Formerly AdoptOpenJDK; backed by the Adoptium Working Group |
| **Amazon Corretto** | AWS users or Amazon-ecosystem shops | Free LTS, Amazon runs it in production |
| **Azul Zulu** | Flexibility; oldest macOS support | Free builds for every major Java version |
| **GraalVM** | Native image, polyglot, or performance tuning | Builds native executables; larger install |
| **Microsoft OpenJDK** | Azure/Microsoft ecosystem | Free, TCK-verified, good Windows cross-compat |
| **Oracle JDK** | Oracle-specific features (requires license for prod) | Not recommended for general use |

**TL;DR — Default recommendation: Eclipse Temurin (LTS version).**

---

## 🟢 Newbie — Install Your First JDK

### Step 1 — Make sure Homebrew is installed

```zsh
brew --version    # if this errors, install Homebrew first → see homebrew-guide.md
```

### Step 2 — Install Eclipse Temurin (recommended)

```zsh
brew install --cask temurin          # installs the latest LTS (currently Java 21)
```

### Step 3 — Verify the installation

```zsh
java --version
# Expected output:
# openjdk 21.x.x 2024-xx-xx
# OpenJDK Runtime Environment Temurin-21.x.x+x

javac --version
# Expected:
# javac 21.x.x
```

### Step 4 — Set JAVA_HOME (usually automatic with Temurin)

```zsh
echo $JAVA_HOME         # check if already set
# If blank, add to ~/.zshrc:
export JAVA_HOME=$(/usr/libexec/java_home)
```

### Common first program

```zsh
# Create Hello.java
cat > Hello.java << 'EOF'
public class Hello {
    public static void main(String[] args) {
        System.out.println("Hello from Java " + Runtime.version().feature() + " on macOS!");
    }
}
EOF

# Compile and run
javac Hello.java
java Hello
```

---

## 🟡 Amateur — Multiple Versions & Switching

### Install specific Java versions (side-by-side)

```zsh
brew install --cask temurin@21       # Java 21 LTS
brew install --cask temurin@17       # Java 17 LTS
brew install --cask temurin@11       # Java 11 LTS (legacy)
brew install --cask temurin@8        # Java 8 (very legacy)
```

### List installed JDKs (macOS built-in utility)

```zsh
/usr/libexec/java_home -V
# Output:
# 21.0.3(arm64) "Eclipse Adoptium" - "OpenJDK 21" /Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home
# 17.0.11(arm64) "Eclipse Adoptium" - "OpenJDK 17" /Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home
```

### Switch Java version manually (per-terminal session)

```zsh
export JAVA_HOME=$(/usr/libexec/java_home -v 21)   # switch to Java 21
export JAVA_HOME=$(/usr/libexec/java_home -v 17)   # switch to Java 17
java --version                                       # verify
```

### Add a convenience alias to `~/.zshrc`

```zsh
# Add to ~/.zshrc
alias java21='export JAVA_HOME=$(/usr/libexec/java_home -v 21) && java --version'
alias java17='export JAVA_HOME=$(/usr/libexec/java_home -v 17) && java --version'
alias java11='export JAVA_HOME=$(/usr/libexec/java_home -v 11) && java --version'
alias javaVersions='/usr/libexec/java_home -V'
```

### Install Amazon Corretto (alternative)

```zsh
brew install --cask amazon-corretto     # latest LTS
brew install --cask amazon-corretto@21
brew install --cask amazon-corretto@17
```

### Install Azul Zulu (alternative)

```zsh
brew install --cask zulu                # latest
brew install --cask zulu@21
brew install --cask zulu@17
```

---

## 🔴 Pro — jenv, GraalVM & CI/CD

### jenv — Automatic JDK Switching Per Project

`jenv` manages JDK versions like `nvm` does for Node or `pyenv` for Python.  
It reads a `.java-version` file in your project root and switches automatically.

```zsh
# Install jenv
brew install jenv

# Add to ~/.zshrc
export PATH="$HOME/.jenv/bin:$PATH"
eval "$(jenv init -)"

# Register JDKs with jenv
jenv add /Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home
jenv add /Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home

# List registered versions
jenv versions

# Set global default
jenv global 21

# Set local version for a project (creates .java-version file)
cd ~/projects/my-project
jenv local 17

# Verify
jenv version
java --version
```

### Enable jenv plugins (for Maven/Gradle integration)

```zsh
jenv enable-plugin maven
jenv enable-plugin gradle
jenv enable-plugin export        # sets JAVA_HOME based on jenv version
```

### GraalVM — Native Image & Polyglot

```zsh
brew install --cask graalvm-jdk          # GraalVM JDK (includes native-image)
brew install --cask graalvm-jdk@21       # specific Java version

# After installation, verify native-image is available
native-image --version

# Compile a Java class to a native binary (no JVM at runtime!)
javac Hello.java
native-image Hello                        # produces ./hello binary
./hello                                   # runs instantly — no JVM startup
```

### SDKMAN — Alternative to jenv (manages SDKs beyond Java)

```zsh
# Install SDKMAN
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

# List available Java distributions
sdk list java

# Install a specific distribution
sdk install java 21.0.3-tem              # Temurin via SDKMAN
sdk install java 21.0.3-amzn            # Corretto via SDKMAN

# Switch
sdk use java 21.0.3-tem
sdk default java 21.0.3-tem
```

### CI/CD — Set Java in GitHub Actions

```yaml
# .github/workflows/build.yml
- name: Set up JDK 21
  uses: actions/setup-java@v4
  with:
    java-version: '21'
    distribution: 'temurin'      # or 'corretto', 'zulu', 'graalvm'
    cache: 'maven'               # or 'gradle'
```

---

## 📊 JDK Distribution Comparison

| Feature | Temurin | Corretto | Zulu | GraalVM |
|---|---|---|---|---|
| Free | ✅ | ✅ | ✅ | ✅ |
| LTS versions | ✅ | ✅ | ✅ | ✅ |
| TCK certified | ✅ | ✅ | ✅ | ✅ |
| Native image | ❌ | ❌ | ❌ | ✅ |
| Polyglot | ❌ | ❌ | ❌ | ✅ |
| ARM64 (M1–M4) | ✅ | ✅ | ✅ | ✅ |
| Security patches | Fast | Fast | Fast | Fast |
| Backed by | Eclipse Foundation | Amazon | Azul Systems | Oracle |
| Brew cask name | `temurin` | `amazon-corretto` | `zulu` | `graalvm-jdk` |

---

## 🔧 Troubleshooting

| Symptom | Fix |
|---|---|
| `java: command not found` | Ensure Homebrew is on PATH; re-run `brew install --cask temurin` |
| Wrong Java version after install | `export JAVA_HOME=$(/usr/libexec/java_home -v 21)` in current session |
| Maven/Gradle using wrong JDK | `jenv enable-plugin maven` or set `JAVA_HOME` explicitly |
| Multiple Java versions, wrong default | `jenv global 21` or check System Preferences → Java |
| IntelliJ IDEA can't find JDK | In IDEA: **File → Project Structure → SDKs → +** and point to `/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home` |
| `native-image: command not found` | Install GraalVM: `brew install --cask graalvm-jdk` |
| `JAVA_HOME` not persisting | Add `export JAVA_HOME=...` to `~/.zshrc` (not just `~/.zprofile`) |

---

**Navigation:** [← Homebrew Guide](homebrew-guide.md) · [npm on Mac →](npm-on-mac.md) · [Dev Tools →](dev-tools-guide.md) · [← START-HERE](START-HERE.md)
