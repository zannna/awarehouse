package com.example.awarehouse.common.config;

import com.example.awarehouse.common.util.KeycloakUserCreation;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class TestBaseConfiguration {

    @LocalServerPort
    int localPort;

    @Autowired
    protected KeycloakUserCreation keycloakUserCreation;

   // public static KeycloakContainer keycloak =  new KeycloakContainer().withRealmImportFile("keycloak/realm-export.json");
   public static  KeycloakContainer keycloak = new KeycloakContainer().withRealmImportFile("keycloak/realm-export.json");

    @BeforeEach
    void init() {
        RestAssured.port = localPort;
    }

    public static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.1")
            .withDatabaseName("awarehouseTest")
            .withPassword("awarehouse")
            .withUsername("awarehouse");


    @DynamicPropertySource
    public static void containerConfig(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> keycloak.getAuthServerUrl() + "/realms/awarehouse");

    }

    @BeforeAll
    public static void startContainers() {
        postgreSQLContainer.start();
        keycloak.start();
    }

    @AfterAll
    public static void stopContainers() {
        postgreSQLContainer.stop();
       keycloak.stop();
    }
    public static void insertInitDataOnce(DataSource dataSource, String sqlSource) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
         //   ScriptUtils.executeSqlScript(conn, new ClassPathResource("sql/clearData.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource(sqlSource));
        }
    }
}