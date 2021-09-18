package jason;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class SucceedingJasonTest {
    
    @Test
    public void testHello2() {
        assertThat(Jason.hello()).isEqualTo("hello2");
    }
}
