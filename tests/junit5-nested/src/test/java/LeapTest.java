import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LeapTest {

    private Leap leap;

    @BeforeEach
    public void setup() {
        leap = new Leap();
    }

    @Nested
    class NotLeapYearsTest {
        @Test
        public void testYearNotDivBy4InCommonYear() {
            assertThat(leap.isLeapYear(2015)).isFalse();
        }

        @Disabled("Remove to run test")
        @Test
        public void testYearDivBy2NotDivBy4InCommonYear() {
            assertThat(leap.isLeapYear(1970)).isFalse();
        }

        @Disabled("Remove to run test")
        @Test
        public void testYearDivBy100NotDivBy400InCommonYear() {
            assertThat(leap.isLeapYear(2100)).isFalse();
        }

        @Disabled("Remove to run test")
        @Test
        public void testYearDivBy100NotDivBy3IsNotLeapYear() {
            assertThat(leap.isLeapYear(1900)).isFalse();
        }

        @Disabled("Remove to run test")
        @Test
        public void testYearDivBy200NotDivBy400InCommonYear() {
            assertThat(leap.isLeapYear(1800)).isFalse();
        }
    }

    @Nested
    class LeapYearsTest {
        @Disabled("Remove to run test")
        @Test
        public void testYearDivBy4NotDivBy100InLeapYear() {
            assertThat(leap.isLeapYear(1996)).isTrue();
        }

        @Disabled("Remove to run test")
        @Test
        public void testYearDivBy4And5InLeapYear() {
            assertThat(leap.isLeapYear(1960)).isTrue();
        }

        @Disabled("Remove to run test")
        @Test
        public void testYearDivBy400InLeapYear() {
            assertThat(leap.isLeapYear(2000)).isTrue();
        }

        @Disabled("Remove to run test")
        @Test
        public void testYearDivBy400NotDivBy125IsLeapYear() {
            assertThat(leap.isLeapYear(2400)).isTrue();
        }
    }
}
