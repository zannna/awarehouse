package com.example.awarehouse.module.token.integration;

import com.example.awarehouse.common.config.TestBaseConfiguration;
import com.example.awarehouse.module.warehouse.WorkerWarehouseRepository;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.sql.DataSource;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.UUID;

import static com.example.awarehouse.module.token.util.SharingTestConstants.SHARING_TOKEN;
import static com.example.awarehouse.module.warehouse.util.WarehouseConstants.WAREHOUSE_ID;
import static com.example.awarehouse.util.Constants.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class SharingTokenIntegrationTest extends TestBaseConfiguration {

    @Autowired
    WorkerWarehouseRepository workerWarehouseRepository;

    @BeforeAll
    static void setUp(@Autowired DataSource dataSource) throws SQLException {
        insertInitDataOnce(dataSource, "sql/token/sharingTokenInitData.sql");
    }

    @Test
    void createWarehouse_whenAllDataValid_thenSaveCWarehouseEntity() throws ParseException, URISyntaxException {
        keycloakUserCreation.createAdmin(keycloak.getAuthServerUrl());
        String jwt = keycloakUserCreation.headerJwt();

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", jwt)
                .when()
                .get(URI_VERSION_V1+URI_SHARING_TOKEN+URI_WAREHOUSE+"/{warehouseId}", WAREHOUSE_ID)
                .then()
                .log()
                .ifValidationFails()
                .statusCode(HttpStatus.OK.value())
                .body("sharingToken", is("LUR3G68pElA26X3yDw+9mA=="));

    }

    @Test
    void createWarehouse_whenUserIsNotAdmin_thenThrowException() throws ParseException, URISyntaxException {
        keycloakUserCreation.createBasicUser(keycloak.getAuthServerUrl());
        String jwt = keycloakUserCreation.headerJwt();

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", jwt)
                .when()
                .get(URI_VERSION_V1+URI_SHARING_TOKEN+URI_WAREHOUSE+"/{warehouseId}", WAREHOUSE_ID)
                .then()
                .log()
                .ifValidationFails()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void joinWarehouse_whenAllDataValid_thenJoinWarehouse() throws ParseException, URISyntaxException {
        keycloakUserCreation.createAdmin(keycloak.getAuthServerUrl());
        String jwt = keycloakUserCreation.headerJwt();

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", jwt)
                .when()
                .post(URI_VERSION_V1+URI_SHARING_TOKEN+"/{sharingId}", "LUR3G68pElA26X3yDw+9mA==")
                .then()
                .log()
                .ifValidationFails()
                .statusCode(HttpStatus.OK.value())
                .body("warehouseId", is("123e4567-e89b-12d3-a456-426614174000"));

        workerWarehouseRepository.findByWarehouseIdAndWorkerId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), UUID.fromString("6f1564b1-0cfb-4e13-94a1-6c10f6d9f8e8"));
    }



}
