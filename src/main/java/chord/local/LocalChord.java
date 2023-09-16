package chord.local;

import chord.base.Chord;
import chord.base.Node;

import java.util.ArrayList;
import java.util.Random;

public class LocalChord<T extends Comparable<T>, V> implements Chord<T, V> {
    private final ArrayList<Node<T, V>> nodes = new ArrayList<>();
    private final Random random = new Random();

    @Override
    public void join(final Node<T, V> node) {
        if (!nodes.isEmpty()) {
            final var randomNode = nodes.get(Math.abs(random.nextInt()) % nodes.size());
            node.join(randomNode);
        }
        nodes.add(node);
    }

    @Override
    public void leave(final Node<T, V> node) {
        node.leave();
        nodes.remove(node);
    }

    @Override
    public void stabilize() {
        final var randomNode = nodes.get(random.nextInt() % nodes.size());
        randomNode.stabilize();
    }

    @Override
    public String toString() {
        return "Local Chord\n---\n" + nodes + "\n---\n\n";
    }
}
