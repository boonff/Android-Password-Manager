package com.example.passwords.key;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.util.Base64;

public class EncryptionUtil {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int KEY_SIZE = 256;
    private static final int TAG_LENGTH_BIT = 128;
    private static final int IV_LENGTH_BYTE = 12;

    public static String encrypt(String plaintext, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(plaintext.getBytes());
        byte[] encryptedIVAndText = new byte[IV_LENGTH_BYTE + encrypted.length];
        System.arraycopy(cipher.getIV(), 0, encryptedIVAndText, 0, IV_LENGTH_BYTE); // 将IV复制到结果数组的开头
        System.arraycopy(encrypted, 0, encryptedIVAndText, IV_LENGTH_BYTE, encrypted.length); // 将加密后的数据复制到结果数组
        return Base64.getEncoder().encodeToString(encryptedIVAndText); // 返回Base64编码的结果
    }

    public static String decrypt(String encryptedText, SecretKey key) throws Exception {
        byte[] encryptedIvTextBytes = Base64.getDecoder().decode(encryptedText);
        byte[] iv = new byte[IV_LENGTH_BYTE];
        System.arraycopy(encryptedIvTextBytes, 0, iv, 0, IV_LENGTH_BYTE); // 从加密文本中提取IV
        GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec);
        byte[] encryptedBytes = new byte[encryptedIvTextBytes.length - IV_LENGTH_BYTE];
        System.arraycopy(encryptedIvTextBytes, IV_LENGTH_BYTE, encryptedBytes, 0, encryptedBytes.length); // 从加密文本中提取加密后的数据
        byte[] decrypted = cipher.doFinal(encryptedBytes);
        return new String(decrypted); // 返回解密后的文本
    }

}
