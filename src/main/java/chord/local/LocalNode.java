package chord.local;

import chord.base.DataStore;
import chord.base.Node;

import java.math.BigInteger;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LocalNode<T extends Comparable<T>, V> extends Node<T, V> {
    public LocalNode(final BigInteger id) {
        super(id, new LocalDataStore<>(), new LocalFingerTable<>());
    }

    @Override
    protected void insertDatastore(final DataStore<T, V> dataStore) {
        this.dataStore.putAll(dataStore);
    }

    @Override
    protected SortedMap<T, V> getFromDatastore(final Predicate<T> predicate) {
        return this.dataStore.getAllValues().entrySet()
            .stream()
            .filter(entry -> predicate.test(entry.getKey()))
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                TreeMap::new
            ));
    }

    @Override
    protected Node<T, V> getNode(final Node<T, V> node) {
        return node;
    }

    @Override
    protected void removeFromDatastore(final Map<T, V> transferredData) {
        this.dataStore.removeAll(transferredData.keySet());
    }
}
