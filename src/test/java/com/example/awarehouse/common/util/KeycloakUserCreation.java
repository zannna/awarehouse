package com.example.awarehouse.common.util;

import com.example.awarehouse.module.auth.Worker;
import com.example.awarehouse.module.auth.WorkerRepository;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Collections;
import java.util.UUID;

@Component
public class KeycloakUserCreation {

    JacksonJsonParser jsonParser = new JacksonJsonParser();
    String accessToken;
    @Autowired
    WorkerRepository workerRepository;


    public Worker createAdmin(String keycloakServerUrl) throws ParseException, URISyntaxException {
        return createUser(keycloakServerUrl, createAdminDataForKeycloak());
    }

    public Worker createBasicUser(String keycloakServerUrl) throws ParseException, URISyntaxException {
        return createUser(keycloakServerUrl,createBasicWorkerDataForKeycloak());
    }

    private Worker createUser(String keycloakServerUrl, MultiValueMap<String, String> authenticationData) throws ParseException, URISyntaxException {
        String keycloakResponse = communicateWithKeycloak(keycloakServerUrl, authenticationData);
        extractAccessToken(keycloakResponse);
        Worker worker = createWorker();
        workerRepository.save(worker);
        return worker;
    }


    private MultiValueMap<String, String> createAdminDataForKeycloak(){
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("grant_type", Collections.singletonList("password"));
        formData.put("client_id", Collections.singletonList("awarehouse-rest-api"));
        formData.put("username", Collections.singletonList("jane.doe@baeldung.com"));
        formData.put("password", Collections.singletonList("password"));
        return formData;
    }

    private MultiValueMap<String, String> createBasicWorkerDataForKeycloak(){
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("grant_type", Collections.singletonList("password"));
        formData.put("client_id", Collections.singletonList("awarehouse-rest-api"));
        formData.put("username", Collections.singletonList("carl.smith@baeldung.com"));
        formData.put("password", Collections.singletonList("haslo"));
        return formData;
    }

    private String communicateWithKeycloak(String keycloakServerUrl, MultiValueMap<String, String> formData) throws URISyntaxException {
        URI authorizationURI = authorizationURI(keycloakServerUrl);
        WebClient webclient = WebClient.builder().build();
        return webclient.post()
                .uri(authorizationURI)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
    private URI authorizationURI(String keycloakServerUrl) throws URISyntaxException {
        return new URIBuilder(keycloakServerUrl + "/realms/awarehouse/protocol/openid-connect/token").build();
    }

    private String extractAccessToken(String keycloakResponse){
       accessToken= jsonParser.parseMap(keycloakResponse)
                .get("access_token")
                .toString();
       return accessToken;
    }
    public String headerJwt(){
        return "Bearer " + accessToken;
    }
    private JWT jwt() throws ParseException {
        return JWTParser.parse(accessToken);
    }
    private Worker createWorker() throws ParseException {
        UUID id = getWorkerIdFromJwt();
        return new Worker(id,"Jane", "Doe");
    }
    private UUID getWorkerIdFromJwt() throws ParseException {
        JWT jwt =jwt();
        String id = (String) jwt.getJWTClaimsSet().getClaim("sub");
        return UUID.fromString(id);
    }

}
