package com.example.awarehouse.module.warehouse;

import com.example.awarehouse.common.util.ContextMock;
import com.example.awarehouse.module.warehouse.dto.WarehouseCreation;
import com.example.awarehouse.module.warehouse.dto.WarehouseResponseDto;
import com.example.awarehouse.module.warehouse.group.WarehouseGroup;
import com.example.awarehouse.module.token.SharingTokenService;
import com.example.awarehouse.module.warehouse.group.WarehouseGroupService;
import com.example.awarehouse.module.warehouse.util.factory.GroupFactory;
import com.example.awarehouse.module.warehouse.util.factory.WarehouseDtoFactory;

import jakarta.validation.Validator;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.swing.*;
import java.util.*;

import static com.example.awarehouse.common.util.Constants.WORKER_ID;
import static com.example.awarehouse.module.warehouse.util.WarehouseConstants.WAREHOUSE_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WarehouseServiceTest {
    ContextMock contextMock = new ContextMock();
    WarehouseRepository warehouseRepository = mock(WarehouseRepository.class);
    WarehouseGroupService groupService = mock(WarehouseGroupService.class);
    Validator validator = mock(Validator.class);
    WorkerWarehouseService workerWarehouseService = mock(WorkerWarehouseService.class);
    SharingTokenService sharingTokenService = mock(SharingTokenService.class);

    @ParameterizedTest
    @MethodSource("groups")
    void createWarehouse_whenWarehouseCreationIsValid_thenSaveWarehouse(Set<WarehouseGroup> warehouseGroups) {
        //given
        contextMock.setContext();
        when(warehouseRepository.save(any(Warehouse.class))).thenAnswer(a -> {
            Warehouse warehouse = (Warehouse) a.getArgument(0);
            warehouse.setId(WAREHOUSE_ID);
            return warehouse;
        });
        when(groupService.getGroups(any(Set.class))).thenReturn(warehouseGroups);
        WarehouseService warehouseService = new WarehouseService(workerWarehouseService, sharingTokenService,
                warehouseRepository, groupService, validator);
        WarehouseCreation warehouseCreation = WarehouseDtoFactory.createWarehouseCreation();
        //when
        WarehouseResponseDto result= warehouseService.createWarehouse(warehouseCreation);

        //then
        verify(warehouseRepository).save(any( Warehouse.class));
        verify(workerWarehouseService).newRelation(WAREHOUSE_ID, WORKER_ID, Role.ADMIN);
        verify(sharingTokenService).createSharingToken(any(Warehouse.class));
        assertThat(result.id()).isEqualTo(WAREHOUSE_ID);
        assertThat(result.name()).isEqualTo(warehouseCreation.name());
        assertThat(result.unit()).isEqualTo(warehouseCreation.unit().name());
        assertThat(result.rowsNumber()).isEqualTo(warehouseCreation.numberOfRows());
        assertThat(result.groups()).isEqualTo(GroupFactory.createSetOfGroupResponseDto(Optional.ofNullable(warehouseGroups)));
    }
    @Test
    void addWarehouseToGroup_whenDataAreValid_thenAddWarehouse(){
        //given
        contextMock.setContext();
        Optional<Warehouse> warehouse = Optional.of(Warehouse.builder().warehouseGroups(WarehouseGroup).build());
        Optional<WarehouseGroup> group = Optional.of(new WarehouseGroup());
        when(warehouseRepository.findById(any(UUID.class))).thenReturn(warehouse);
        when(groupService.getGroup(any(Long.class))).thenReturn();
    }

    static List<Set<WarehouseGroup>> groups() {
        List<Set<WarehouseGroup>> result = new ArrayList<>();
        result.add(GroupFactory.createListOfGroups());
        result.add(null);
        return result;
    }

}