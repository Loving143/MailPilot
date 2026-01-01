package com.email.security;


import java.security.SecureRandom;
import java.util.Base64;
public class Keygenerator {

        public static  String generateKey() {
            SecureRandom random = new SecureRandom();
            byte[] key = new byte[32]; // 256 bits / 8 bits per byte = 32 bytes
            random.nextBytes(key);
            String encodedKey = Base64.getEncoder().encodeToString(key);
            System.out.println("Generated Key: " + encodedKey);
            return encodedKey;
        }
        public Keygenerator() {
            // TODO Auto-generated constructor stub
        }

    }

