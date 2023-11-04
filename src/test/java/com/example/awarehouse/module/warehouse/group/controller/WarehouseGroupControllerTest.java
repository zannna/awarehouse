package com.example.awarehouse.module.warehouse.group.controller;

import com.example.awarehouse.module.warehouse.group.WarehouseGroupService;
import com.example.awarehouse.module.warehouse.group.dto.GroupResponse;
import com.example.awarehouse.module.warehouse.util.WarehouseConstants;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.GroupDuplicateException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.awarehouse.module.warehouse.group.util.factory.WarehouseGroupFactory.createGroupResponse;
import static com.example.awarehouse.module.warehouse.group.util.factory.WarehouseGroupFactory.createWarehouseGroupJson;
import static com.example.awarehouse.module.warehouse.util.WarehouseConstants.GROUP_ALREADY_EXIST;
import static com.example.awarehouse.module.warehouse.util.WarehouseConstants.kurwa;
import static com.example.awarehouse.util.Constants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = { WarehouseGroupController.class })
@AutoConfigureMockMvc(addFilters = false)
public class WarehouseGroupControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    WarehouseGroupService warehouseGroupService;

    @Test
    void createGroup_whenDataAreValid_shouldCreateGroup() throws Exception {
        //given
        GroupResponse groupResponse = createGroupResponse();
        when(warehouseGroupService.createGroup(any())).thenReturn(groupResponse);
        mvc
                // when
                .perform(
                        post(URI_VERSION_V1+URI_WAREHOUSE+URI_GROUP)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createWarehouseGroupJson())
                )
                // then
                .andExpect(status().isCreated());
    }

    @Test
    void createGroup_whenGroupAlreadyExists_shouldThrowException() throws Exception {
        //given
        GroupResponse groupResponse = createGroupResponse();
        when(warehouseGroupService.createGroup(any())).thenThrow(new GroupDuplicateException(WarehouseConstants.GROUP_ALREADY_EXIST));

        mvc
                // when
                .perform(
                        post(URI_VERSION_V1+URI_WAREHOUSE+URI_GROUP)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createWarehouseGroupJson())
                )
                // then
                .andExpect(status().isBadRequest());
    }
}
