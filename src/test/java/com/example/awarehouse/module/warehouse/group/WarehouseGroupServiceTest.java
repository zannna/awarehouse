package com.example.awarehouse.module.warehouse.group;

import com.example.awarehouse.common.util.ContextMock;
import com.example.awarehouse.module.auth.WorkerService;
import com.example.awarehouse.module.auth.util.WorkerConstants;
import com.example.awarehouse.module.auth.util.factory.WorkerFactory;
import com.example.awarehouse.module.group.GroupWorkerRepository;
import com.example.awarehouse.module.group.GroupWorkerService;
import com.example.awarehouse.module.group.WarehouseGroup;
import com.example.awarehouse.module.group.WarehouseGroupRepository;
import com.example.awarehouse.module.group.WarehouseGroupService;
import com.example.awarehouse.module.group.dto.BasicGroupInfoDto;
import com.example.awarehouse.module.group.dto.GroupRequest;
import com.example.awarehouse.module.token.SharingTokenService;
import com.example.awarehouse.module.warehouse.WorkerWarehouseService;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.GroupDuplicateException;
import com.example.awarehouse.util.UserIdSupplier;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.example.awarehouse.module.auth.util.WorkerConstants.WORKER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WarehouseGroupServiceTest {

    ContextMock contextMock = new ContextMock();
    WarehouseGroupRepository warehouseGroupRepository = mock(WarehouseGroupRepository.class);
    UserIdSupplier workerIdSupplier =  () -> WorkerConstants.WORKER_ID;
    WorkerWarehouseService workerWarehouseService = mock(WorkerWarehouseService.class);
    WorkerService workerService = mock(WorkerService.class);
    GroupWorkerRepository groupWorkerRepository = mock( GroupWorkerRepository.class);
    SharingTokenService sharingTokenService = mock(SharingTokenService.class);
    GroupWorkerService groupWorkerService = mock(GroupWorkerService.class);

    @Test
    public void createGroup_whenGroupRequestIsValid_thenSaveGroup() {
        //given
        contextMock.setContext();
        GroupRequest groupRequest = new GroupRequest("toys");
        when(warehouseGroupRepository.checkIfNameExists("toys", WORKER_ID)).thenReturn(false);
        when(workerService.getWorker()).thenReturn(WorkerFactory.createWorker());
        when(warehouseGroupRepository.save(any(WarehouseGroup.class))).thenAnswer(a -> {
            WarehouseGroup group= (WarehouseGroup) a.getArgument(0);
            group.setId(UUID.fromString("c6e1b3aa-948d-11ee-b9d1-0242ac120002"));
            return group;
        });
        WarehouseGroupService warehouseGroupService = new WarehouseGroupService(warehouseGroupRepository, workerIdSupplier,
                workerService, workerWarehouseService, groupWorkerRepository,  sharingTokenService, groupWorkerService );

        //when
        BasicGroupInfoDto basicGroupInfoDto = warehouseGroupService.createGroup(groupRequest);

        //then
        verify(warehouseGroupRepository, times(1)).checkIfNameExists("toys", WORKER_ID);
        verify(warehouseGroupRepository).save(any(WarehouseGroup.class));
        assertEquals(basicGroupInfoDto.name(), groupRequest.name());
        assertThat(basicGroupInfoDto.id()).isNotNull();

    }

    @Test
    public void createGroup_whenGroupRequestIsInvalid_thenThrowException() {
        //given
        contextMock.setContext();
        GroupRequest groupRequest = new GroupRequest("toys");
        when(warehouseGroupRepository.checkIfNameExists(anyString(), any(UUID.class))).thenReturn(true);
        when(workerService.getWorker()).thenReturn(WorkerFactory.createWorker());
        WarehouseGroupService warehouseGroupService = new WarehouseGroupService(warehouseGroupRepository, workerIdSupplier,
                workerService, workerWarehouseService, groupWorkerRepository,  sharingTokenService, groupWorkerService );
        //when-then
        assertThrows(
                GroupDuplicateException.class,
                () -> warehouseGroupService.createGroup(groupRequest)
        );
        verify(warehouseGroupRepository, never()).createGroup(any(String.class), any(UUID.class));

    }

}