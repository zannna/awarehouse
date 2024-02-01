package com.example.awarehouse.module.warehouse.group;

import com.example.awarehouse.common.util.ContextMock;
import com.example.awarehouse.module.group.WarehouseGroupRepository;
import com.example.awarehouse.module.group.WarehouseGroup;
import com.example.awarehouse.module.group.WarehouseGroupService;
import com.example.awarehouse.module.warehouse.WorkerWarehouseService;
import com.example.awarehouse.module.warehouse.dto.BasicWarehouseInfoDto;
import com.example.awarehouse.module.warehouse.factory.WarehouseFactory;
import com.example.awarehouse.module.group.dto.GroupRequest;
import com.example.awarehouse.module.group.dto.BasicGroupInfoDto;
import com.example.awarehouse.module.warehouse.group.util.factory.WarehouseGroupFactory;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.GroupDuplicateException;
import com.example.awarehouse.module.auth.WorkerService;
import com.example.awarehouse.module.auth.util.WorkerConstants;
import com.example.awarehouse.util.UserIdSupplier;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.example.awarehouse.common.util.Constants.WORKER_ID;
import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class WarehouseGroupServiceTest {

    ContextMock contextMock = new ContextMock();
    WarehouseGroupRepository warehouseGroupRepository = mock(WarehouseGroupRepository.class);
    UserIdSupplier workerIdSupplier =  () -> WorkerConstants.WORKER_ID;
    WorkerWarehouseService workerWarehouseService = mock(WorkerWarehouseService.class);
    WorkerService workerService = mock(WorkerService.class);

    @Test
    public void createGroup_whenGroupRequestIsValid_thenSaveGroup() {
        //given
        contextMock.setContext();
        GroupRequest groupRequest = new GroupRequest("toys");
        when(warehouseGroupRepository.checkIfNameExists("toys", WORKER_ID)).thenReturn(false);
        when(warehouseGroupRepository.createGroup(any(String.class), any(UUID.class))).thenReturn(new WarehouseGroup(UUID.fromString("c6e1b3aa-948d-11ee-b9d1-0242ac120002"),"toys", null));
        WarehouseGroupService warehouseGroupService = new WarehouseGroupService(warehouseGroupRepository, workerIdSupplier, workerService, workerWarehouseService);

        //when
        BasicGroupInfoDto basicGroupInfoDto = warehouseGroupService.createGroup(groupRequest);

        //then
        verify(warehouseGroupRepository, times(1)).checkIfNameExists("toys", WORKER_ID);
        verify(warehouseGroupRepository).createGroup(any(String.class), any(UUID.class));
        assertEquals(basicGroupInfoDto.name(), groupRequest.name());
        assertThat(basicGroupInfoDto.id()).isNotNull();

    }

    @Test
    public void createGroup_whenGroupRequestIsInvalid_thenThrowException() {
        //given
        contextMock.setContext();
        GroupRequest groupRequest = new GroupRequest("toys");
        when(warehouseGroupRepository.checkIfNameExists("toys", WORKER_ID)).thenReturn(true);
        WarehouseGroupService warehouseGroupService = new WarehouseGroupService(warehouseGroupRepository, workerIdSupplier,  workerService, workerWarehouseService);

        //when-then
        assertThrows(
                GroupDuplicateException.class,
                () -> warehouseGroupService.createGroup(groupRequest)
        );
        verify(warehouseGroupRepository, never()).createGroup(any(String.class), any(UUID.class));

    }

    @Test
    void getAllGroupsWithWarehouses_whenDataAreValid() {
        //given
        contextMock.setContext();
        when(workerWarehouseService.getWorkerWarehouses(any(UUID.class))).thenReturn(WarehouseFactory.getSetOfWarehouses());
        when(warehouseGroupRepository.findByWorkerId(any(UUID.class))).thenReturn(WarehouseGroupFactory.createSetOfGroup());
        WarehouseGroupService warehouseGroupService = new WarehouseGroupService(warehouseGroupRepository, workerIdSupplier, workerService, workerWarehouseService);

        //when
        Map<BasicGroupInfoDto, List<BasicWarehouseInfoDto>> groupsWithWarehouses = warehouseGroupService.getAllGroupsWithWarehouses();

        //then
        assertThat(groupsWithWarehouses).contains(
                entry(new BasicGroupInfoDto(UUID.fromString("c6e1b3aa-948d-11ee-b9d1-0242ac120002"), "Group 1"),
                        List.of(new BasicWarehouseInfoDto(UUID.fromString("5d8a8b84-8227-11ee-b962-0242ac120002"),
                        "Warehouse 1"))),
                entry(new BasicGroupInfoDto(UUID.fromString("d985e5e4-948d-11ee-b9d1-0242ac120002"), "Group 2"),
                        List.of(new BasicWarehouseInfoDto(UUID.fromString("5d8a8b84-8227-11ee-b962-0242ac120002"), "Warehouse 1"),
                                new BasicWarehouseInfoDto(UUID.fromString("8c6e5ac0-8227-11ee-b962-0242ac120002"), "Warehouse 3"))),
                entry(new BasicGroupInfoDto(UUID.fromString("e2802bdc-948d-11ee-b9d1-0242ac120002"), "Group 3"), new ArrayList<>()));
    }
}