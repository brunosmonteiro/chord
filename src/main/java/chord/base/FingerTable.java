package chord.base;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface FingerTable<T extends Comparable<T>, V> {
    Map<BigInteger, Node<T, V>> get();
    void put(final BigInteger key, final Node<T, V> value);
    void remove(final BigInteger key);
}
