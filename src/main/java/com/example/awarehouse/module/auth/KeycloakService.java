package com.example.awarehouse.module.auth;

import com.example.awarehouse.module.auth.dto.LoginDto;
import com.example.awarehouse.module.auth.dto.UserCreationDto;
import com.example.awarehouse.util.configuration.KeycloakProvider;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;

import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    private final KeycloakProvider keycloakProvider;
    @Value("${keycloak.realm}")
    public String realm;

    public UUID createUserInKeycloak(UserCreationDto userCreation) {
        Keycloak keycloak = keycloakProvider.getInstance();
        UserRepresentation user = createUser( userCreation);
        Response response =keycloak.realm(realm).users().create(user);
        int status = response.getStatus();
        if (status == Response.Status.CREATED.getStatusCode()) {
            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
            return UUID.fromString(userId);
        }
        else {
            throw new RuntimeException("Could not create user");
        }
    }

    private UserRepresentation createUser(UserCreationDto userCreation){
        UserRepresentation user = new UserRepresentation();
        user.setEmail(userCreation.email());
        user.setUsername(userCreation.email());
        user.setFirstName(userCreation.firstName());
        user.setLastName(userCreation.surname());
        CredentialRepresentation password = createPassword(userCreation.password());
        user.setCredentials(Collections.singletonList(password));
        user.setEnabled(true);
        return user;
    }

    private  CredentialRepresentation createPassword(String password){
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return  passwordCredentials;
    }

    public String login(LoginDto loginDto) {
        Keycloak keycloak = keycloakProvider.getUserInstance(loginDto.email(), loginDto.password());
        AccessTokenResponse tokenResponse = keycloak.tokenManager().getAccessToken();
        return tokenResponse.getToken();
    }


}
