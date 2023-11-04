package com.example.awarehouse.module.token;

public interface TokenGenerator {
    String generateSalt();
    String generateToken(String tokenData, String salt);

}
