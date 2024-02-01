import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.ParameterizedTest.ARGUMENTS_WITH_NAMES_PLACEHOLDER;


public class LeapTest {

    private Leap leap;

    @BeforeEach
    public void setup() {
        leap = new Leap();
    }

    @Disabled("Remove to run test")
    @ParameterizedTest(name = "customName " + ARGUMENTS_WITH_NAMES_PLACEHOLDER)
    @MethodSource("params")
    public void testIsLeapYear(int year, boolean expected) {
        assertThat(leap.isLeapYear(year)).isEqualTo(expected);
    }

    public static Stream<Arguments> params() {
        return Stream.of(
            Arguments.of(2015, false),
            Arguments.of(1970, false),
            Arguments.of(1960, true),
            Arguments.of(2100, false),
            Arguments.of(2000, true),
            Arguments.of(2400, true),
            Arguments.of(1800, false)
        );
    }

}
