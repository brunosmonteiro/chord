package chord.base;

import chord.helper.Constants;
import chord.helper.Encrypter;

import java.math.BigInteger;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedMap;
import java.util.function.Predicate;

import static java.util.Optional.ofNullable;

public abstract class Node<T extends Comparable<T>, V> {
    protected final BigInteger id;
    protected Node<T, V> successor;
    protected Node<T, V> predecessor;
    protected final FingerTable<T, V> fingerTable;
    protected final DataStore<T, V> dataStore;

    public Node(
            final BigInteger id,
            final DataStore<T, V> datastore,
            final FingerTable<T, V> fingerTable) {
        this.id = id;
        this.successor = this;
        this.predecessor = this;
        this.dataStore = datastore;
        this.fingerTable = fingerTable;
        initializeFingerTable();
    }

    public BigInteger getId() {
        return id;
    }

    public void save(final T key, final V value) {
        findSuccessor(Encrypter.encrypt(key)).dataStore.put(key, value);
    }

    public void remove(final T key) {
        findSuccessor(Encrypter.encrypt(key)).dataStore.remove(key);
    }

    public V get(final T key) {
        return ofNullable(findSuccessor(Encrypter.encrypt(key)))
            .map(node -> node.dataStore.get(key))
            .orElse(null);
    }

    public boolean containsKey(final T key) {
        return ofNullable(findSuccessor(Encrypter.encrypt(key)))
            .map(node -> node.dataStore.containsKey(key))
            .orElse(false);
    }

    public void join(final Node<T, V> bootstrapNode) {
        this.successor = bootstrapNode.findSuccessor(id);
        this.successor.setPredecessor(this);
        this.predecessor = this.successor.predecessor;
        this.predecessor.setSuccessor(this);
        this.shareKeysWithSuccessor();
    }

    public void leave() {
        this.successor.setPredecessor(this.predecessor);
        this.predecessor.setSuccessor(this.successor);
        this.successor.insertDatastore(this.dataStore);
    }

    public void stabilize() {
        final var successorCandidate = ofNullable(getNode(this.successor))
            .map(node -> node.predecessor)
            .orElseGet(() -> fingerTable.get().values().stream()
                .map(this::getNode)
                .filter(Objects::nonNull)
                .map(fingerNode -> fingerNode.predecessor)
                .findFirst()
                .orElse(null)
            );
        if (successorCandidate != null && withinInterval(successorCandidate.id, this.id, this.successor.id)) {
            this.successor = successorCandidate;
        }
        this.successor.setPredecessor(this);
        fingerTable.get().keySet().forEach(start -> fingerTable.put(start, this.findSuccessor(start)));
    }

    private Node<T, V> findSuccessor(final BigInteger id) {
        if (withinInterval(id, this.id, this.successor.id)) {
            return getNode(this.successor);
        }
        return this.findClosestPrecedingNode(id).findSuccessor(id);
    }

    private Node<T, V> findClosestPrecedingNode(final BigInteger id) {
        return fingerTable.get().values().stream()
            .filter(finger -> withinInterval(finger.id, this.id, id))
            .findFirst()
            .map(this::getNode)
            .orElse(this);
    }

    private void shareKeysWithSuccessor() {
        final Map<T, V> transferredData = this.successor.getFromDatastore(
            key -> withinInterval(
                Encrypter.encrypt(key),
                this.successor.lowerBound(),
                this.id
            )
        );
        this.successor.removeFromDatastore(transferredData);
        this.dataStore.putAll(transferredData);
    }

    private boolean withinInterval(final BigInteger id, final BigInteger lower, final BigInteger upper) {
        if (lower.compareTo(upper) < 0) {
            return id.compareTo(lower) > 0 && id.compareTo(upper) <= 0;
        }
        return id.compareTo(lower) > 0 || id.compareTo(upper) <= 0;
    }

    private void initializeFingerTable() {
        for (int i = 0; i < Constants.BITS; i++) {
            final BigInteger start = this.id.add(BigInteger.valueOf(2).pow(i)).mod(Constants.NODES);
            this.fingerTable.put(start, null);
        }
    }

    protected void setPredecessor(final Node<T, V> predecessor) {
        this.predecessor = predecessor;
    }

    protected void setSuccessor(final Node<T, V> successor) {
        this.successor = successor;
    }

    protected abstract void removeFromDatastore(final Map<T, V> transferredData);

    protected abstract void insertDatastore(final DataStore<T, V> dataStore);

    protected abstract SortedMap<T, V> getFromDatastore(final Predicate<T> predicate);

    protected abstract Node<T, V> getNode(final Node<T, V> node);

    private BigInteger lowerBound() {
        return this.predecessor.id.add(BigInteger.ONE);
    }

    @Override
    public String toString() {
        return String.format("\nNode %d\n", this.id) +
            String.format("\tSuccessor -> %d\n",
                ofNullable(this.successor).map(node -> node.id).orElse(BigInteger.valueOf(-1))) +
            String.format("\tPredecessor -> %d\n",
                ofNullable(this.predecessor).map(node -> node.id).orElse(BigInteger.valueOf(-1))) +
            "\tFinger Table = " + this.fingerTable + "\n" +
            "\tDataStore = " + this.dataStore + "\n";
    }
}
