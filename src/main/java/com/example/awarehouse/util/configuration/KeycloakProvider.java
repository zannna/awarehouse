package com.example.awarehouse.util.configuration;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class KeycloakProvider {

    @Value("${keycloak.auth-server-url}")
    public String serverURL;
    @Value("${keycloak.realm}")
    public String realm;
    @Value("${keycloak.resource}")
    public String clientID;
    @Value("${keycloak.credentials.secret}")
    public String clientSecret;

    private Keycloak keycloak;


    public Keycloak getInstance() {
        if(keycloak == null) {
           keycloak = KeycloakBuilder.builder()
                   .serverUrl(serverURL)
                   .realm(realm)
                   .clientId(clientID)
                   .clientSecret(clientSecret)
                   .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                   .resteasyClient(
                           new ResteasyClientBuilder()
                                   .connectionPoolSize(10).build()
                   )
                   .build();
        }
        return keycloak;
    }

    public Keycloak getUserInstance(String email, String password) {
        if(keycloak == null) {
            keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverURL)
                    .realm(realm)
                    .clientId(clientID)
                    .clientSecret(clientSecret)
                    .username(email)
                    .password(password)
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .resteasyClient(
                            new ResteasyClientBuilder()
                                    .connectionPoolSize(10).build()
                    )
                    .build();
        }
        return keycloak;
    }
}
