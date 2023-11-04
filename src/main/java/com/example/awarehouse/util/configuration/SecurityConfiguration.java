package com.example.awarehouse.util.configuration;


import static com.example.awarehouse.util.Constants.URI_VERSION_V1;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.example.awarehouse.util.UserIdSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;

import java.util.UUID;
import java.util.function.Supplier;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfiguration {

    private final JwtAuthConverter jwtAuthConverter;

    public SecurityConfiguration(JwtAuthConverter jwtAuthConverter) {
        this.jwtAuthConverter = jwtAuthConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requests ->
                requests
                        .requestMatchers(URI_VERSION_V1+"/**").authenticated()
        );

        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)));

        http.sessionManagement(session -> session.sessionCreationPolicy(STATELESS));

        return http.build();
    }

    @Bean
    public UserIdSupplier workerIdFromSecurityContext() {
        return () ->{ Jwt token = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return UUID.fromString(token.getClaim("sub"));};
    }

}
