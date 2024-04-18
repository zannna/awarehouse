package com.example.awarehouse.module.product.controller;

import com.example.awarehouse.module.product.ProductService;
import com.example.awarehouse.module.warehouse.controller.WarehouseController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.awarehouse.module.product.util.ProductFactory.createMoveProductsDto;
import static com.example.awarehouse.module.warehouse.util.factory.WarehouseJsonFactory.createWarehouseCreationJson;
import static com.example.awarehouse.util.Constants.URI_MOVE;
import static com.example.awarehouse.util.Constants.URI_PRODUCT;
import static com.example.awarehouse.util.Constants.URI_VERSION_V1;
import static com.example.awarehouse.util.Constants.URI_WAREHOUSE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = { WarehouseController.class })
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    ProductService productService;

    @Test
    void moveProduct_whenAllDataAreValid_shouldMoveAllData() throws Exception {
        mvc
                // when
                .perform(
                        post(URI_VERSION_V1 + URI_PRODUCT+ URI_MOVE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createMoveProductsDto())
                )
                // then
                .andExpect(status().isCreated());
    }
}
