package jason;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class AnotherJasonTest {
    
    @Test
    public void testHelloAgain() {
        assertThat(Jason.hello()).isEqualTo("hello");
    }
}
