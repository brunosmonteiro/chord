package chord.local;

import chord.base.DataStore;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class LocalDataStore<T extends Comparable<T>, V> implements DataStore<T, V> {
    private final Map<T, V> localStorage = new TreeMap<>();

    @Override
    public void put(final T key, final V value) {
        localStorage.put(key, value);
    }

    @Override
    public void putAll(final DataStore<T, V> dataStore) {
        this.localStorage.putAll(dataStore.getAllValues());
    }

    @Override
    public void putAll(final Map<T, V> sortedMap) {
        this.localStorage.putAll(sortedMap);
    }

    @Override
    public void remove(final T key) {
        this.localStorage.remove(key);
    }

    @Override
    public void removeAll(final Set<T> keys) {
        this.localStorage.keySet().removeAll(keys);
    }

    @Override
    public boolean containsKey(final T key) {
        return localStorage.containsKey(key);
    }

    @Override
    public V get(final T key) {
        return localStorage.get(key);
    }

    @Override
    public SortedMap<T, V> getAllValues() {
        return (SortedMap<T, V>) localStorage;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        localStorage.forEach((k, v) -> builder.append(String.format("\t\t%s:%s, \n", k, v)));
        return builder.toString();
    }
}
