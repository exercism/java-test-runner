package jason;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class JasonTest {
    
    @Test
    public void testHello() {
        assertThat(Jason.hello()).isEqualTo("hello");
    }
    
    @Test
    public void testNotHello() {
        assertThat(Jason.hello()).isNotEqualTo("hello");
    }
}
