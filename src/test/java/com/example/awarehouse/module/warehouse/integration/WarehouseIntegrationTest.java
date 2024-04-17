package com.example.awarehouse.module.warehouse.integration;

import com.example.awarehouse.common.config.TestBaseConfiguration;
import com.example.awarehouse.module.auth.Worker;
import com.example.awarehouse.module.token.SharingTokenRepository;
import com.example.awarehouse.module.warehouse.*;
import com.example.awarehouse.module.warehouse.dto.BasicWarehouseInfoDto;
import com.example.awarehouse.module.warehouse.dto.WarehouseIdDto;
import com.example.awarehouse.module.group.WarehouseGroup;
import com.example.awarehouse.module.warehouse.util.factory.WarehouseJsonFactory;
import com.example.awarehouse.module.auth.WorkerRepository;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.sql.DataSource;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.example.awarehouse.common.util.Constants.WORKER_ID;
import static com.example.awarehouse.module.warehouse.util.WarehouseTestConstants.WAREHOUSE_ID;
import static com.example.awarehouse.util.Constants.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

public class WarehouseIntegrationTest extends TestBaseConfiguration {

    @Autowired
    SharingTokenRepository sharingTokenRepository;

    @Autowired
    WarehouseRepository warehouseRepository;

    @Autowired
    WorkerWarehouseRepository workerWarehouse;

    @Autowired
    WorkerRepository workerRepository;

    @Autowired
    WorkerWarehouseRepository workerWarehouseRepository;

    @BeforeAll
    static void setUp(@Autowired DataSource dataSource) throws SQLException {
        insertInitDataOnce(dataSource, "sql/warehouse/createWarehouse.sql");
    }

    @Test
    void createWarehouse_whenAllDataValid_thenSaveWarehouseEntity() throws ParseException, URISyntaxException {
        keycloakUserCreation.createAdmin(keycloak.getAuthServerUrl());
        String jwt = keycloakUserCreation.headerJwt();

        UUID warehouseId = given()
                .contentType(ContentType.JSON)
                .header("Authorization", jwt)
                .body(WarehouseJsonFactory.createWarehouseCreationJson())
                .when()
                .post(URI_VERSION_V1 + URI_WAREHOUSE)
                .then()
                .log()
                .ifValidationFails()
                .statusCode(HttpStatus.CREATED.value())
                .body("name", is("warehouse1"))
                .body("unit", is("METER"))
                .body("rowsNumber", is(2))
                .body("groups.id", containsInAnyOrder("d985e5e4-948d-11ee-b9d1-0242ac120002", "c6e1b3aa-948d-11ee-b9d1-0242ac120002"))
                .body("groups.name", containsInAnyOrder("clothes", "toys"))
                .extract()
                .jsonPath()
                .getUUID("id");

        assertThat(warehouseId).isNotNull();
        assertThat(warehouseRepository.findById(warehouseId)).isPresent();
        assertThat(sharingTokenRepository.findByTokenOwnerId(warehouseId)).isPresent();
        WorkerWarehouse workerWarehouseEntity = workerWarehouse.findByWarehouseIdAndWorkerId(warehouseId, WORKER_ID).get();
        assertThat(workerWarehouseEntity.getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    void getWarehouses_whenDataAreValid_shouldReturnUUIDList() throws ParseException, URISyntaxException {
        Worker worker = keycloakUserCreation.createBasicUser(keycloak.getAuthServerUrl());
        workerWarehouseRepository.createWorkerWarehouseAssociation(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), worker.getId(), "WORKER");
        workerWarehouseRepository.createWorkerWarehouseAssociation(UUID.fromString("16aecbfa-7807-11ee-b962-0242ac120002"), worker.getId(), "WORKER");
        String jwt = keycloakUserCreation.headerJwt();

        List<BasicWarehouseInfoDto> warehouses = given()
                .contentType(ContentType.JSON)
                .header("Authorization", jwt)
                .when()
                .get(URI_VERSION_V1 + URI_WAREHOUSE)
                .then()
                .log()
                .ifValidationFails()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList("$", BasicWarehouseInfoDto.class);

        warehouses.stream().forEach(warehouse -> System.out.println(warehouse.id() + " " + warehouse.name()));
        assertTrue(warehouses.stream().anyMatch(warehouse -> warehouse.id().equals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                && warehouse.name().equals("warehouse1")));
        assertTrue(warehouses.stream().anyMatch(warehouse -> warehouse.id().equals(UUID.fromString("16aecbfa-7807-11ee-b962-0242ac120002"))
                && warehouse.name().equals("warehouse2")));
        assertThat(warehouses.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    void addWarehouseToGroup_whenDataAreValid_shouldStatusBeOk() throws ParseException, URISyntaxException {
        keycloakUserCreation.createBasicUser(keycloak.getAuthServerUrl());
        String jwt = keycloakUserCreation.headerJwt();
        given()
                .pathParam("warehouseId", WAREHOUSE_ID)
                .contentType(ContentType.JSON)
                .header("Authorization", jwt)
                .body("""
                              {
                                "id": "c6e1b3aa-948d-11ee-b9d1-0242ac120002"
                              }
                        """)
                .when()
                .post(URI_VERSION_V1 + URI_WAREHOUSE + "/{warehouseId}" + URI_GROUP)
                .then()
                .log()
                .ifValidationFails()
                .statusCode(HttpStatus.OK.value());

        Warehouse warehouse = warehouseRepository.findById(WAREHOUSE_ID).get();
        Set<WarehouseGroup> groups = warehouse.getWarehouseGroups();
        assertThat(groups.size()).isEqualTo(1);
        assertThat(groups).allMatch(g -> g.getId().equals(UUID.fromString("c6e1b3aa-948d-11ee-b9d1-0242ac120002")));

    }
}
