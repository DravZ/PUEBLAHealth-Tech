package com.pueblahealth.back.utils;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class AES_GSMUtil {

    private static final String SECRET_KEY = "12345678901234567890123456789012";

    public static String decrypt(String encryptedData, String ivBase64) throws Exception {

        byte[] keyBytes = SECRET_KEY.getBytes();
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

        byte[] iv = Base64.getDecoder().decode(ivBase64);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding"); // 🔥 CLAVE

        GCMParameterSpec spec = new GCMParameterSpec(128, iv);

        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        byte[] decrypted = cipher.doFinal(encryptedBytes);

        return new String(decrypted);
    }
}