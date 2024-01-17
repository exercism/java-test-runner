import java.util.ArrayList;
import java.util.SequencedCollection;

class DoublyLinkedList<T> {
    private final SequencedCollection<T> items;

    DoublyLinkedList() {
        items = new ArrayList<>();
    }

    void push(T item) {
        items.addFirst(item);
    }

    T pop() {
        return items.isEmpty() ? null : items.removeFirst();
    }

    void unshift(T item) {
        items.addLast(item);
    }

    T shift() {
        return items.isEmpty() ? null : items.removeLast();
    }
}
