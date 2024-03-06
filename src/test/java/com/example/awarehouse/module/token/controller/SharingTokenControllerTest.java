package com.example.awarehouse.module.token.controller;

import com.example.awarehouse.module.token.SharingTokenService;
import com.example.awarehouse.module.token.dto.SharingTokenDto;
import com.example.awarehouse.module.token.exception.exceptions.SharingTokenNotExist;
import com.example.awarehouse.module.token.exception.exceptions.WarehouseNotHasSharingToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.awarehouse.exception.util.constants.ExceptionConstants.SHARINGTOKEN_NOT_EXIST;
import static com.example.awarehouse.exception.util.constants.ExceptionConstants.WAREHOUSE_NOT_CONTAIN_SHARINGTOKEN;
import static com.example.awarehouse.module.token.util.SharingTestConstants.SHARING_TOKEN;
import static com.example.awarehouse.module.warehouse.util.WarehouseTestConstants.WAREHOUSE_ID;
import static com.example.awarehouse.util.Constants.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = { SharingTokenController.class })
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
                .perform(
                        get(URI_VERSION_V1+ URI_SHARING_TOKEN+URI_WAREHOUSE+"/{warehouseId}", WAREHOUSE_ID)
                )
                // then
                .andExpect(status().isOk());
    }

    @Test
    void getSharingToken_whenWarehouseNotHaveToken_shouldthrowException() throws Exception {
        when(sharingTokenService.getSharingToken(WAREHOUSE_ID)).thenThrow(new WarehouseNotHasSharingToken(WAREHOUSE_NOT_CONTAIN_SHARINGTOKEN));
        mvc
                // when
                .perform(
                        get(URI_VERSION_V1+ URI_SHARING_TOKEN +URI_WAREHOUSE+"/{warehouseId}", WAREHOUSE_ID)
                )
                // then
                .andExpect(jsonPath("$.errors.title").value(WAREHOUSE_NOT_CONTAIN_SHARINGTOKEN));

    }

    @Test
    void joinWarehouse_whenDataAreCorrect_shouldReturnWarehouseId() throws Exception {
        when(sharingTokenService.joinWarehouse(SHARING_TOKEN)).thenReturn(new WarehouseId(WAREHOUSE_ID));
        mvc
                // when
                .perform(
                        get(URI_VERSION_V1+ URI_SHARING_TOKEN+"/{sharingTokenId}", SHARING_TOKEN)
                )
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.warehouseId").value(WAREHOUSE_ID.toString()));
    }

    @Test
    void joinWarehouse_whenSharingTokenNotExist_shouldThrowException() throws Exception {
        when(sharingTokenService.joinWarehouse(SHARING_TOKEN)).thenThrow(new SharingTokenNotExist(SHARINGTOKEN_NOT_EXIST));
        mvc
                // when
                .perform(
                        get(URI_VERSION_V1+ URI_SHARING_TOKEN+"/{sharingTokenId}", SHARING_TOKEN)
                )
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.warehouseId").value(WAREHOUSE_ID.toString()));
    }


}