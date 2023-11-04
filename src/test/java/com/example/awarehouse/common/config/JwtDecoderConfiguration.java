package com.example.awarehouse.common.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.time.ZonedDateTime;
import java.util.function.Supplier;

@TestConfiguration
public class JwtDecoderConfiguration {

    private String jwkSetUri="http://localhost:8092/realms/awarehouse/protocol/openid-connect/certs";

    @Bean
    Supplier getTimeProvider() {
        return ZonedDateTime::now;
    }
}
