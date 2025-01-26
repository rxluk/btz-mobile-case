package com.example.btzmobileapp.security;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class EncryptionUtil {
    public static String encryptPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] salt = generateSalt();
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes());
        return Base64.encodeBase64String(salt) + ":" + Base64.encodeBase64String(hashedPassword);
    }

    public static boolean verifyPassword(String enteredPassword, String storedPassword) throws NoSuchAlgorithmException {
        String[] parts = storedPassword.split(":");
        byte[] salt = Base64.decodeBase64(parts[0]);
        byte[] hashedPassword = Base64.decodeBase64(parts[1]);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        byte[] enteredHashedPassword = md.digest(enteredPassword.getBytes());
        return MessageDigest.isEqual(hashedPassword, enteredHashedPassword);
    }

    private static byte[] generateSalt() {
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
}