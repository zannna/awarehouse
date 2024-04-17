package com.example.awarehouse.module.token.integration;

import com.example.awarehouse.common.config.TestBaseConfiguration;
import com.example.awarehouse.module.auth.Worker;
import com.example.awarehouse.module.group.GroupWorker;
import com.example.awarehouse.module.group.GroupWorkerRepository;
import com.example.awarehouse.module.token.dto.SharingTokenDto;
import com.example.awarehouse.module.warehouse.WorkerWarehouse;
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
import java.util.Optional;
import java.util.UUID;

import static com.example.awarehouse.module.warehouse.util.WarehouseTestConstants.WAREHOUSE_ID;
import static com.example.awarehouse.util.Constants.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class SharingTokenIntegrationTest extends TestBaseConfiguration {

    @Autowired
    WorkerWarehouseRepository workerWarehouseRepository;
    @Autowired
    GroupWorkerRepository workerGroupRepository;

    @BeforeAll
    static void setUp(@Autowired DataSource dataSource) throws SQLException {
        insertInitDataOnce(dataSource, "sql/token/sharingTokenInitData.sql");
    }

    @Test
    void getSharingToken_whenAllDataValid_thenGetSharingTokenEntity() throws ParseException, URISyntaxException {
         keycloakUserCreation.createAdmin(keycloak.getAuthServerUrl());
        String jwt = keycloakUserCreation.headerJwt();

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", jwt)
                .when()
                .get(URI_VERSION_V1+URI_SHARING_TOKEN+"/{warehouseId}", WAREHOUSE_ID)
                .then()
                .log()
                .ifValidationFails()
                .statusCode(HttpStatus.OK.value())
                .body("sharingToken", is("$2a$10$Hr33QvDgEBzirFfAqfVeTeOj4ycfLUR3G68pElA26X3yDw+9mA=="));

    }



    @Test
    void joinWarehouse_whenAllDataValid_thenJoinWarehouse() throws ParseException, URISyntaxException {
        Worker worker =keycloakUserCreation.createAdmin(keycloak.getAuthServerUrl());
        String jwt = keycloakUserCreation.headerJwt();

        given()
                .contentType(ContentType.JSON)
                .body(new SharingTokenDto("$2a$10$E8L1K02tIpE6aB7cHa+3qERm6cjhE8L1K02tIpE6aB7cHa+3qE=="))
                .header("Authorization", jwt)
                .when()
                .post(URI_VERSION_V1+URI_SHARING_TOKEN)
                .then()
                .log()
                .ifValidationFails()
                .statusCode(HttpStatus.OK.value());

        Optional<WorkerWarehouse> workerWarehouse =workerWarehouseRepository.findByWarehouseIdAndWorkerId(UUID.fromString("d17a22d4-f834-41f0-ac55-d8ad82a18964"), worker.getId());
        assertThat(workerWarehouse).isPresent();
    }

    @Test
    void joinGroup_whenAllDataValid_thenJoinGroup() throws ParseException, URISyntaxException {
        Worker worker =keycloakUserCreation.createAdmin(keycloak.getAuthServerUrl());
        String jwt = keycloakUserCreation.headerJwt();

        given()
                .contentType(ContentType.JSON)
                .body(new SharingTokenDto("$2a$10$D7K0J91sHoD59A6bGz+2pDQl5bifD7K0J91sHoD59A6bGz+2pD=="))
                .header("Authorization", jwt)
                .when()
                .post(URI_VERSION_V1+URI_SHARING_TOKEN)
                .then()
                .log()
                .ifValidationFails()
                .statusCode(HttpStatus.OK.value());

        Optional<GroupWorker> workerWarehouse =workerGroupRepository.findByGroupIdAndWorkerId(UUID.fromString("88e6e8c8-061c-47e9-861d-f56d7b6a9085"), worker.getId());
        assertThat(workerWarehouse).isPresent();
    }


}
