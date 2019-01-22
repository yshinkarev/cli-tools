package xyz.moonrabbit.bittrex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.concurrent.atomic.AtomicLong;

public class BittrexUtil {

    private final static AtomicLong incremental = new AtomicLong(System.currentTimeMillis());

    public static void sleepSilent(long seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String calculateHash(String secret, String url, String encryption) throws GeneralSecurityException {
        Mac mac = Mac.getInstance(encryption);
        mac.init(new SecretKeySpec(secret.getBytes(), encryption));
        mac.update(url.getBytes());
        return String.format("%0128x", new BigInteger(1, mac.doFinal()));
    }

    public static String generateNonce() {
        return Long.toString(incremental.incrementAndGet());
    }
}