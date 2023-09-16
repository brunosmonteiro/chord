package chord.local;

import chord.base.FingerTable;
import chord.base.Node;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;

public class LocalFingerTable<T extends Comparable<T>, V> implements FingerTable<T, V> {
    private final HashMap<BigInteger, Node<T, V>> localStorage = new HashMap<>();

    @Override
    public Map<BigInteger, Node<T, V>> get() {
        return this.localStorage;
    }

    @Override
    public void put(final BigInteger key, final Node<T, V> value) {
        this.localStorage.put(key, value);
    }

    @Override
    public void remove(final BigInteger key) {
        this.localStorage.remove(key);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        localStorage.forEach((k, v) -> builder.append(
            String.format("%d:%d, ", k, ofNullable(v).map(Node::getId).orElse(BigInteger.valueOf(-1)))));
        return builder.toString();
    }
}
