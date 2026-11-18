import java.util.List;
import java.util.stream.Collectors;

class Training {

    String greet(String name) {
        System.out.println("Greeting " + name);
        return "Hello, " + name + "!";
    }

    int divide(int a, int b) {
        return a / b;
    }

    List<String> shout(List<String> words) {
        return words.stream().map(String::toUpperCase).collect(Collectors.toList());
    }

    void reject(String input) {
        if (input.isEmpty()) {
            throw new IllegalArgumentException("Input must not be empty");
        }
    }
}
