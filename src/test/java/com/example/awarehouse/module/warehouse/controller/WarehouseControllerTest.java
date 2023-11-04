package com.example.awarehouse.module.warehouse.controller;

import com.example.awarehouse.module.warehouse.WarehouseService;
import com.example.awarehouse.module.warehouse.dto.WarehouseCreation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.example.awarehouse.module.warehouse.util.factory.WarehouseJsonFactory.*;
import static com.example.awarehouse.util.Constants.URI_VERSION_V1;
import static com.example.awarehouse.util.Constants.URI_WAREHOUSE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = { WarehouseController.class })
@AutoConfigureMockMvc(addFilters = false)
class WarehouseControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    WarehouseService warehouseService;

    @Test
    void createWarehouse_whenValidInput_thenReturnsOk() throws Exception {

        mvc
                // when
                .perform(
                        post(URI_VERSION_V1 + URI_WAREHOUSE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createWarehouseCreationJson())
                )
                // then
                .andExpect(status().isCreated());
    }

    @ParameterizedTest
    @MethodSource("invalidWarehouseCreation")
    void createWarehouse_whenInvalidInput_thenReturnsBadRequest(String warehouseCreation) throws Exception {

        mvc
                // when
                .perform(
                        post(URI_VERSION_V1 + URI_WAREHOUSE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(warehouseCreation)
                )
                // then
                .andExpect(status().isBadRequest());
    }

    static List<String> invalidWarehouseCreation(){
        return List.of(createWarehouseCreationJsonWithInvalidName(),
                createWarehouseCreationJsonWithInvalidUnit(),
                createWarehouseCreationJsonWithInvalidNumberOfRows());
    }
}