package chord;

import chord.base.Node;
import chord.helper.Encrypter;
import chord.local.LocalChord;
import chord.local.LocalNode;

import java.util.Random;

public class ChordApplication {
    public static void main(final String[] args) {
        final var random = new Random();
        final var localChord = new LocalChord<Integer, String>();

        for (int i = 0; i < 8; i++) {
            final Node<Integer, String> node = new LocalNode<>(Encrypter.encrypt(random.nextInt()));
            localChord.join(node);
            System.out.println(localChord);
        }
    }
}
