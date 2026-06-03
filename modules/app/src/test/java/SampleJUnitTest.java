import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Sample JUnit 6 (Jupiter) test demonstrating the basics:
 * plain assertions, exception assertions, and parameterized tests.
 *
 * <p>Run with: {@code .\gradlew.bat :modules:app:test}
 */
@DisplayName("Sample JUnit 6 sanity tests")
class SampleJUnitTest {

    @Test
    @DisplayName("Basic arithmetic assertion")
    void additionWorks() {
        assertEquals(4, 2 + 2, "2 + 2 should equal 4");
    }

    @Test
    @DisplayName("Exception is thrown for invalid input")
    void throwsOnNegativeIndex() {
        final var ex = assertThrows(
                ArrayIndexOutOfBoundsException.class,
                () -> {
                    final var arr = new int[3];
                    final var ignored = arr[-1];
                }
        );
        assertTrue(ex.getMessage().contains("-1"));
    }

    @ParameterizedTest(name = "[{index}] {0} is even")
    @ValueSource(ints = {0, 2, 4, 100, 2026})
    void evenNumbersAreEven(final int value) {
        assertEquals(0, value % 2);
    }
}
