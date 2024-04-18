package com.example.awarehouse.module.token.controller;

import com.example.awarehouse.module.token.SharingTokenService;
import com.example.awarehouse.module.token.dto.SharingTokenDto;
import com.example.awarehouse.module.token.exception.exceptions.SharingTokenNotExist;
import com.example.awarehouse.module.token.exception.exceptions.WarehouseNotHasSharingToken;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.WorkerNotHaveAccess;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.WorkerWarehouseRelationNotExist;
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

import java.util.stream.Stream;

import static com.example.awarehouse.exception.util.constants.ExceptionConstants.SHARINGTOKEN_NOT_EXIST;
import static com.example.awarehouse.exception.util.constants.ExceptionConstants.WAREHOUSE_NOT_CONTAIN_SHARINGTOKEN;
import static com.example.awarehouse.module.warehouse.util.WarehouseTestConstants.WAREHOUSE_ID;
import static com.example.awarehouse.util.Constants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = {SharingTokenController.class})
@AutoConfigureMockMvc(addFilters = false)
class SharingTokenControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    SharingTokenService sharingTokenService;

    @Test
    void getSharingToken_whenDataAreCorrect_shouldReturnSharingToken() throws Exception {
        when(sharingTokenService.getSharingToken(WAREHOUSE_ID)).thenReturn(new SharingTokenDto("token"));
        mvc
                // when
                .perform(get(URI_VERSION_V1 + URI_SHARING_TOKEN + "/{warehouseId}", WAREHOUSE_ID))
                // then
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("exceptionsForGetSharingToken")
    void getSharingToken_whenWarehouseNotHaveToken_shouldthrowException(Exception exception, String exceptionMessage) throws Exception {
        when(sharingTokenService.getSharingToken(WAREHOUSE_ID)).thenThrow(exception);
        mvc
                // when
                .perform(get(URI_VERSION_V1 + URI_SHARING_TOKEN + "/{warehouseId}", WAREHOUSE_ID))
                // then
                .andExpect(jsonPath("$.message").value(exceptionMessage));

    }

    private static Stream<Arguments> exceptionsForGetSharingToken() {
        return Stream.of(Arguments.of(new WarehouseNotHasSharingToken(WAREHOUSE_NOT_CONTAIN_SHARINGTOKEN), WAREHOUSE_NOT_CONTAIN_SHARINGTOKEN), Arguments.of(new WorkerWarehouseRelationNotExist("Worker with id 1 does not have relation with warehouse with id 1"), "Worker with id 1 does not have relation with warehouse with id 1"), Arguments.of(new WorkerNotHaveAccess("Worker does not have access to group with id 1"), "Worker does not have access to group with id 1"));
    }

    @Test
    void joinWarehouse_whenDataAreCorrect_shouldReturnWarehouseId() throws Exception {

        mvc
                // when
                .perform(post(URI_VERSION_V1 + URI_SHARING_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "sharingToken": "token"
                                }
                                """))
                // then
                .andExpect(status().isOk());
    }

    @Test
    void joinWarehouse_whenSharingTokenNotExist_shouldThrowException() throws Exception {
        doThrow(new SharingTokenNotExist(SHARINGTOKEN_NOT_EXIST)).when(sharingTokenService).join(any(SharingTokenDto.class));

        mvc
                // when
                .perform(post(URI_VERSION_V1 + URI_SHARING_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "sharingToken": "token"
                                }
                                """))
                // then
                .andExpect(status().isForbidden()).andExpect(jsonPath("$.message").value(SHARINGTOKEN_NOT_EXIST));
    }


}