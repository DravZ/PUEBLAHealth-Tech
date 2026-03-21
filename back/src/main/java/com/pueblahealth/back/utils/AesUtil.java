package com.pueblahealth.back.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;
import java.util.Base64;

public class AesUtil {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    public static String encrypt(String data, String secretKey) throws Exception {

        byte[] keyBytes = secretKey.getBytes();
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

        // IV aleatorio
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

        byte[] encrypted = cipher.doFinal(data.getBytes());

        // Combinar IV + data cifrada
        byte[] combined = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    public static String decrypt(String encryptedData, String secretKey) throws Exception {

        byte[] decoded = Base64.getDecoder().decode(encryptedData);

        byte[] iv = new byte[16];
        byte[] encrypted = new byte[decoded.length - 16];

        System.arraycopy(decoded, 0, iv, 0, 16);
        System.arraycopy(decoded, 16, encrypted, 0, encrypted.length);

        SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

        return new String(cipher.doFinal(encrypted));
    }
}