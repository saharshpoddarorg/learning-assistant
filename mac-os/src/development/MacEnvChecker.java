import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

/**
 * MacEnvChecker — macOS Dev Environment Diagnostic Tool
 *
 * <p>Checks and reports the state of key development tools on macOS:
 * Java, Homebrew, Node.js, Docker, Git, and environment variables.
 *
 * <p>Use this to verify your Mac dev environment is set up correctly.
 * Pair with the guides in mac-os/docs/ for installation instructions.
 *
 * <p>Run: {@code javac MacEnvChecker.java && java MacEnvChecker}
 */
public class MacEnvChecker {

    private static final String PASS = "✅";
    private static final String FAIL = "❌";
    private static final String INFO = "ℹ️ ";

    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════");
        System.out.println("  🍎 macOS Dev Environment Checker");
        System.out.println("═══════════════════════════════════════════════════\n");

        checkJava();
        checkEnvironmentVariable("JAVA_HOME");
        checkCommand("Homebrew", List.of("brew", "--version"));
        checkCommand("Git", List.of("git", "--version"));
        checkCommand("Node.js", List.of("node", "--version"));
        checkCommand("npm", List.of("npm", "--version"));
        checkCommand("Docker", List.of("docker", "--version"));
        checkCommand("Python", List.of("python3", "--version"));
        checkCommand("Go", List.of("go", "version"));
        checkEnvironmentVariable("GOPATH");
        checkEnvironmentVariable("NVM_DIR");
        checkCommand("VS Code", List.of("code", "--version"));
        checkCommand("nvm", List.of("bash", "-c", "source ~/.nvm/nvm.sh && nvm --version 2>/dev/null || echo N/A"));

        System.out.println("\n═══════════════════════════════════════════════════");
        System.out.println("  📚 Setup Guides: mac-os/docs/");
        System.out.println("     START-HERE.md → homebrew-guide.md → jdk-setup.md");
        System.out.println("═══════════════════════════════════════════════════");
    }

    /**
     * Check Java runtime and print current version + JAVA_HOME.
     */
    private static void checkJava() {
        final String version = Runtime.version().toString();
        final int major = Runtime.version().feature();
        final String status = major >= 21 ? PASS : "⚠️ ";
        System.out.printf("  %s Java:      %s (feature release %d)%n", status, version, major);

        if (major < 21) {
            System.out.printf("      %s Java 21+ recommended — install via: brew install --cask temurin%n", INFO);
        }
    }

    /**
     * Print an environment variable's value, or note if it's unset.
     *
     * @param name the environment variable name
     */
    private static void checkEnvironmentVariable(final String name) {
        final String value = System.getenv(name);
        if (value != null && !value.isBlank()) {
            System.out.printf("  %s %-10s %s%n", PASS, name + ":", value);
        } else {
            System.out.printf("  %s %-10s (not set)%n", FAIL, name + ":");
        }
    }

    /**
     * Run a command and print its output or report failure.
     *
     * @param name    display name for the tool
     * @param command command + arguments to execute
     */
    private static void checkCommand(final String name, final List<String> command) {
        final Optional<String> output = runCommand(command);
        if (output.isPresent()) {
            // Take only the first line so output stays compact
            final String firstLine = output.get().lines().findFirst().orElse("").strip();
            System.out.printf("  %s %-10s %s%n", PASS, name + ":", firstLine);
        } else {
            System.out.printf("  %s %-10s not found%n", FAIL, name + ":");
        }
    }

    /**
     * Execute a process and return stdout, or empty if the process fails.
     *
     * @param command the command and its arguments
     * @return output string or empty on error
     */
    private static Optional<String> runCommand(final List<String> command) {
        try {
            final var process = new ProcessBuilder(command)
                .redirectErrorStream(true)
                .start();

            try (var reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                final String output = reader.lines()
                    .reduce("", (a, b) -> a + (a.isEmpty() ? "" : "\n") + b);
                final int exitCode = process.waitFor();
                return exitCode == 0 || !output.isBlank() ? Optional.of(output) : Optional.empty();
            }
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
