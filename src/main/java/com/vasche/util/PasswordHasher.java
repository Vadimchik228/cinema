package com.vasche.util;

import lombok.experimental.UtilityClass;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@UtilityClass
public class PasswordHasher {
    private static final SecretKeyFactory SECRET_KEY_FACTORY;

    static {
        try {
            SECRET_KEY_FACTORY = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String hash(String pass, String salt) {
        KeySpec spec = new PBEKeySpec(pass.toCharArray(), salt.getBytes(StandardCharsets.UTF_8),
                65536, 1024);
        try {
            return Base64.getEncoder().encodeToString(SECRET_KEY_FACTORY.generateSecret(spec).getEncoded());
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean check(String candidate, String salt, String hashedPass) {
        KeySpec spec = new PBEKeySpec(candidate.toCharArray(), salt.getBytes(StandardCharsets.UTF_8),
                65536, 1024);
        try {
            return Base64.getEncoder()
                    .encodeToString(SECRET_KEY_FACTORY.generateSecret(spec).getEncoded())
                    .equals(hashedPass);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
