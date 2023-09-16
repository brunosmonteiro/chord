package chord.helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

public class Encrypter {
    private Encrypter() {

    }

    public static BigInteger encrypt(final Object obj) {
        try {
            final var messageDigest = MessageDigest.getInstance(Constants.ENCRYPTION_ALGORITHM);
            final var hashBytes = messageDigest.digest(serialize(obj));
            return new BigInteger(1, hashBytes).mod(Constants.NODES);
        } catch (final Exception e) {
            throw new RuntimeException("Could not generate the encryption");
        }
    }

    private static byte[] serialize(final Object obj) throws IOException {
        var byteArray = new ByteArrayOutputStream();
        final var objectStream = new ObjectOutputStream(byteArray);
        objectStream.writeObject(obj);
        return byteArray.toByteArray();
    }
}
