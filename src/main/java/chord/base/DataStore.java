package chord.base;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public interface DataStore<T extends Comparable<T>, V> {
    void put(final T key, final V value);
    void putAll(final DataStore<T, V> dataStore);
    void putAll(final Map<T, V> sortedMap);
    void remove(T key);
    void removeAll(final Set<T> key);
    boolean containsKey(final T key);
    V get(final T key);
    SortedMap<T, V> getAllValues();
}
