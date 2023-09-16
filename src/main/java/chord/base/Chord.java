package chord.base;

public interface Chord<T extends Comparable<T>, V> {
    void join(final Node<T, V> node);
    void leave(final Node<T, V> node);
    void stabilize();
}
