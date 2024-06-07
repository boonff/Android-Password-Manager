package com.example.passwords.key;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // 生成密码哈希
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // 验证密码
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
