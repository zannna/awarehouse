package com.example.awarehouse.module.product.controller;

import com.example.awarehouse.module.product.ProductProviderService;
import com.example.awarehouse.module.product.ProductService;
import com.example.awarehouse.module.product.dto.MoveProductsDto;
import com.example.awarehouse.module.warehouse.controller.WarehouseController;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.ShelveNotExist;
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

import static com.example.awarehouse.module.product.util.ProductFactory.*;
import static com.example.awarehouse.module.warehouse.util.factory.WarehouseJsonFactory.createWarehouseCreationJson;
import static com.example.awarehouse.util.Constants.URI_MOVE;
import static com.example.awarehouse.util.Constants.URI_PRODUCT;
import static com.example.awarehouse.util.Constants.URI_VERSION_V1;
import static com.example.awarehouse.util.Constants.URI_WAREHOUSE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = { ProductController.class })
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    ProductService productService;

    @MockBean
    ProductProviderService productProviderService;

    @Test
    void moveProduct_whenAllDataAreValid_shouldMoveAllData() throws Exception {
        mvc
                // when
                .perform(
                        patch(URI_VERSION_V1 + URI_PRODUCT+ URI_MOVE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createMoveProductsDtoJson())
                )
                // then
                .andExpect(status().isOk());
    }

    @Test
    void moveProduct_whenShelfNotExist_shouldThrowException() throws Exception {
        doThrow(new ShelveNotExist("error")).when(productService).moveProducts(any(MoveProductsDto.class));

        mvc
                // when
                .perform(
                        patch(URI_VERSION_V1 + URI_PRODUCT+ URI_MOVE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createMoveProductsDtoJson())
                )
                // then
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @MethodSource("invalidMoveProductJson")
    void moveProduct_whenInvalidDto_shouldThrowException(String json) throws Exception {
        mvc
                // when
                .perform(
                        patch(URI_VERSION_V1 + URI_PRODUCT+ URI_MOVE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                // then
                .andExpect(status().isBadRequest());
    }

    private static List<String> invalidMoveProductJson() {
        return List.of(
                createMoveProductsDtoJsonWithNullWarehouseId(),
                createMoveProductsDtoJsonWithNullShelf(),
                createMoveProductsDtoJsonWithNullTier()
                       );
    }


}
