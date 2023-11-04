package com.example.awarehouse.module.warehouse.group;

import com.example.awarehouse.common.util.ContextMock;
import com.example.awarehouse.module.warehouse.group.dto.GroupRequest;
import com.example.awarehouse.module.warehouse.group.dto.GroupResponse;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.GroupDuplicateException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.example.awarehouse.common.util.Constants.WORKER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class WarehouseGroupServiceTest {
    GroupRepository groupRepository = mock(GroupRepository.class);
ContextMock contextMock = new ContextMock();

    @Test
    public void createGroup_whenGroupRequestIsValid_thenSaveGroup() {
        //given
        contextMock.setContext();
        GroupRequest groupRequest = new GroupRequest("toys");
        when(groupRepository.checkIfNameExists("toys", WORKER_ID)).thenReturn(false);
        when(groupRepository.createGroup(any(String.class), any(UUID.class))).thenReturn(new WarehouseGroup(1L,"toys", null));
        WarehouseGroupService warehouseGroupService = new WarehouseGroupService(groupRepository);

        //when
        GroupResponse groupResponse = warehouseGroupService.createGroup(groupRequest);

        //then
        verify(groupRepository, times(1)).checkIfNameExists("toys", WORKER_ID);
        verify(groupRepository).createGroup(any(String.class), any(UUID.class));
        assertEquals(groupResponse.name(), groupRequest.name());
        assertThat(groupResponse.id()).isNotNull();

    }

    @Test
    public void createGroup_whenGroupRequestIsInvalid_thenThrowException() {
        //given
        contextMock.setContext();
        GroupRequest groupRequest = new GroupRequest("toys");
        when(groupRepository.checkIfNameExists("toys", WORKER_ID)).thenReturn(true);
        WarehouseGroupService warehouseGroupService = new WarehouseGroupService(groupRepository);

        //when-then
        assertThrows(
                GroupDuplicateException.class,
                () -> warehouseGroupService.createGroup(groupRequest)
        );
        verify(groupRepository, never()).createGroup(any(String.class), any(UUID.class));

    }
}