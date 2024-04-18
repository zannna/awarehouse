package com.example.awarehouse.module.product.integration;

import com.example.awarehouse.common.config.TestBaseConfiguration;
import com.example.awarehouse.module.auth.Worker;
import com.example.awarehouse.module.product.ProductWarehouse;
import com.example.awarehouse.module.product.ProductWarehouseRepository;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.sql.DataSource;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;

import static com.example.awarehouse.module.product.util.ProductFactory.createMoveProductsDto;
import static com.example.awarehouse.util.Constants.*;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductIntegrationTest extends TestBaseConfiguration {

    @Autowired
    ProductWarehouseRepository productWarehouseRepository;
    @BeforeAll
    static void setUp(@Autowired DataSource dataSource) throws SQLException {
        insertInitDataOnce(dataSource, "sql/product/productInitData.sql");
    }

    @Test
    void moveProducts_whenAllDataAreValid_shouldMoveProducts() throws ParseException, URISyntaxException {
        // given
        Worker worker =keycloakUserCreation.createAdmin(keycloak.getAuthServerUrl());
        String jwt = keycloakUserCreation.headerJwt();

        // when
        given()
                .contentType(ContentType.JSON)
                .body(createMoveProductsDto())
                .header("Authorization", jwt)
                .when()
                .patch(URI_VERSION_V1+URI_PRODUCT+URI_MOVE)
                .then()
                .log()
                .ifValidationFails()
                .statusCode(HttpStatus.OK.value());
        // then
        List<ProductWarehouse> productWarehouse1 = productWarehouseRepository.findAllByTierId(UUID.fromString("b0f8f917-6cd2-4433-b1a2-db99f0b93a21"));
        assertEquals(1, productWarehouse1.size());
        assertEquals(   2, productWarehouse1.get(0).getNumberOfProducts());
        List<ProductWarehouse> productWarehouse2 = productWarehouseRepository.findAllByTierId(UUID.fromString("15a73cfc-e584-465a-ad94-a159144685ce"));
        assertEquals(2, productWarehouse2.size());
        boolean exist = productWarehouse2.stream().anyMatch(pw->pw.getProduct().getId().equals(UUID.fromString("d8296d44-55f0-4a4a-9fd4-a02fe9f64df1")));
        assertTrue(exist);
        exist = productWarehouse2.stream().anyMatch(pw->pw.getProduct().getId().equals(UUID.fromString("7e11e9ca-d04f-4081-9a26-89c381834e73")));
        assertTrue(exist);
        List<ProductWarehouse> productWarehouse3 = productWarehouseRepository.findAllByTierId(UUID.fromString("45e264a7-f1f2-4b02-8d25-47b036799da9"));
        assertTrue(productWarehouse3.isEmpty());
    }

}
