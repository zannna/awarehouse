package com.example.awarehouse.module.token;

import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


import java.security.SecureRandom;

@Component
@Setter
public class BasicTokenGenerator implements  TokenGenerator {

    SecureRandom secureRandom = new SecureRandom();
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public String generateSalt(){
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return salt.toString();
    }
    public String generateToken(String tokenData, String salt){
        return bCryptPasswordEncoder.encode( tokenData +salt);
    }
}
