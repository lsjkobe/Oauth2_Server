package com.leoli.oauth2_server.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class PasswordUtil {

    public String getSalt(){
//        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return salt.toString();
    }
}
