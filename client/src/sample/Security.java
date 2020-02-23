package sample;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Security {

    public static String getMd5String(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] hashInBytes = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : hashInBytes) {
            stringBuilder.append(String.format("%02x", b));
        }
        return stringBuilder.toString();
    }
}
