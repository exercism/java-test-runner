import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

// This exercise is run once while the Docker image is built, to record which classes
// the test runner loads and how it uses them (see bin/create-aot-cache.sh).
// Its tests cover the paths a typical run takes: passing, failing and erroring tests,
// parameterized tests, printed output and task tags. Some tests fail on purpose.
public class TrainingTest {

    private Training training;

    @BeforeEach
    public void setup() {
        training = new Training();
    }

    @Test
    @Tag("task:1")
    @DisplayName("greets by name")
    public void greetsByName() {
        assertThat(training.greet("Alice")).isEqualTo("Hello, Alice!");
    }

    @Test
    @Tag("task:1")
    public void failsAnAssertJAssertion() {
        assertThat(training.greet("Bob")).isEqualTo("Hi, Bob!");
    }

    @Test
    @Tag("task:2")
    public void failsAJUnitAssertion() {
        assertEquals(3, training.divide(7, 3));
    }

    @Test
    @Tag("task:2")
    public void throwsAnUnexpectedException() {
        assertThat(training.divide(1, 0)).isZero();
    }

    @Test
    public void checksAnExpectedException() {
        assertThatThrownBy(() -> training.reject(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Input must not be empty");
        assertThrows(IllegalArgumentException.class, () -> training.reject(""));
    }

    @Test
    public void comparesLists() {
        assertThat(training.shout(List.of("a", "b"))).containsExactly("A", "B");
        assertThat(training.shout(List.of("a", "b"))).containsExactly("B", "A");
    }

    @ParameterizedTest
    @MethodSource("divisions")
    public void dividesFromAMethodSource(int a, int b, int expected) {
        assertThat(training.divide(a, b)).isEqualTo(expected);
    }

    private static Stream<Arguments> divisions() {
        return Stream.of(Arguments.of(6, 3, 2), Arguments.of(9, 3, 4));
    }

    @ParameterizedTest
    @CsvSource({"Alice, 'Hello, Alice!'", "Bob, 'Hello, Bob!'"})
    public void greetsFromACsvSource(String name, String expected) {
        assertThat(training.greet(name)).isEqualTo(expected);
    }

    @ParameterizedTest(name = "{0} is not empty")
    @ValueSource(strings = {"a", "b"})
    public void acceptsFromAValueSource(String input) {
        training.reject(input);
    }
}
