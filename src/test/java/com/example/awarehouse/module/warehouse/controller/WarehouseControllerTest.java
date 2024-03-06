package com.example.awarehouse.module.warehouse.controller;

import com.example.awarehouse.module.warehouse.WarehouseService;
import com.example.awarehouse.module.warehouse.dto.WarehouseIdDto;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.GroupNotExistException;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.WarehouseNotExistException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.example.awarehouse.module.warehouse.util.WarehouseConstants.GROUP_NOT_EXIST;
import static com.example.awarehouse.module.warehouse.util.WarehouseConstants.WAREHOUSE_NOT_EXIST;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.doThrow;
import static com.example.awarehouse.module.warehouse.util.factory.WarehouseJsonFactory.*;
import static com.example.awarehouse.util.Constants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    void createWarehouse_whenValidInput_thenReturnOk() throws Exception {

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
    @Test
    void addWarehouseToGroup_whenValidInput_thenReturnsOk() throws Exception {
        //given
        mvc
                // when
                .perform(
                        get(URI_VERSION_V1+URI_WAREHOUSE+"/{warehouseId}"+URI_GROUP+"/{groupId}", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createWarehouseIdDto())
                )
                // then
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("addWarehouseToGroupException")
    void addWarehouseToGroup_whenInValidInput_thenThrowException(RuntimeException exception, String exceptionMessage) throws Exception {
        //given
//        doThrow(exception).when(warehouseService).addWarehouseToGroup(any(UUID.class), any(WarehouseIdDto.class));
//        mvc
//                // when
//                .perform(
//                        get(URI_VERSION_V1+URI_WAREHOUSE+URI_GROUP+"/{groupId}", 1)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(createWarehouseIdDto())
//                )
//                // then
//                .andExpect(status().isBadRequest())
//                .andExpect((MockMvcResultMatchers.jsonPath("$.message").value(exceptionMessage)));
    }

    static List<String> invalidWarehouseCreation(){
        return List.of(createWarehouseCreationJsonWithInvalidName(),
                createWarehouseCreationJsonWithInvalidUnit(),
                createWarehouseCreationJsonWithInvalidNumberOfRows());
    }

    static Stream<Arguments> addWarehouseToGroupException(){
        return Stream.of(
                arguments(new WarehouseNotExistException(WAREHOUSE_NOT_EXIST), WAREHOUSE_NOT_EXIST),
                arguments(new GroupNotExistException(GROUP_NOT_EXIST),GROUP_NOT_EXIST )
        );

    }
}