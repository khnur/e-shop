package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {
    public static byte[] hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        return messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
    }

    public static boolean isValid(String password, byte[] hash) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        byte[] hashPassword = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
        return MessageDigest.isEqual(hashPassword, hash);
    }
}
