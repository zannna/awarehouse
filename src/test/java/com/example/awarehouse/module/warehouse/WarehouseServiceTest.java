package com.example.awarehouse.module.warehouse;

import com.example.awarehouse.common.util.ContextMock;
import com.example.awarehouse.module.warehouse.dto.WarehouseCreation;
import com.example.awarehouse.module.warehouse.dto.WarehouseIdDto;
import com.example.awarehouse.module.warehouse.dto.WarehouseResponseDto;
import com.example.awarehouse.module.group.WarehouseGroup;
import com.example.awarehouse.module.token.SharingTokenService;
import com.example.awarehouse.module.group.WarehouseGroupService;
import com.example.awarehouse.module.warehouse.util.factory.GroupFactory;
import com.example.awarehouse.module.warehouse.util.factory.WarehouseDtoFactory;

import com.example.awarehouse.module.warehouse.util.factory.WorkerWarehouseFactory;
import com.example.awarehouse.module.worker.util.factory.WorkerFactory;
import jakarta.validation.Validator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static com.example.awarehouse.common.util.Constants.WORKER_ID;
import static com.example.awarehouse.module.warehouse.util.WarehouseTestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
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

    @ParameterizedTest
    @MethodSource("warehouseProvider")
    void addWarehouseToGroup_whenDataAreValid_thenAddWarehouse(Optional<Warehouse> warehouse, Set<WarehouseGroup> givenGroups){
        //given
        contextMock.setContext();
        Optional<WarehouseGroup> group = Optional.of(new WarehouseGroup(UUID.fromString("6b63c7bb-b829-45fb-8f84-460e4dfe2ef9"), "Group 2",WorkerFactory.createWorker()));
        when(warehouseRepository.findById(any(UUID.class))).thenReturn(warehouse);
        when(groupService.getGroup(any(UUID.class))).thenReturn(group);
        WarehouseService warehouseService = new WarehouseService(workerWarehouseService, sharingTokenService,
                warehouseRepository, groupService, validator);

        //when
        warehouseService.addWarehouseToGroup(UUID.fromString("6b63c7bb-b829-45fb-8f84-460e4dfe2ef9"), new WarehouseIdDto(warehouse.get().getId()));

        //then
        Set<WarehouseGroup> receivedGroups = warehouse.get().getWarehouseGroups();
        givenGroups.add(group.get());
        assertThat(receivedGroups).isEqualTo(givenGroups);
    }

    static List<Set<WarehouseGroup>> groups() {
        List<Set<WarehouseGroup>> result = new ArrayList<>();
        result.add(GroupFactory.createListOfGroups());
        result.add(null);
        return result;
    }
    static Stream<Arguments> warehouseProvider(){
        Set<WarehouseGroup> group1 = Set.of(new WarehouseGroup(UUID.fromString("c30e0c43-00af-4927-b15a-1383bd394e14"), "Group 1"));
        Optional<Warehouse> warehouse1 =Optional.of(Warehouse.builder().warehouseGroups(group1)
                .workerWarehouses(Set.of(WorkerWarehouseFactory.createWorkerWarehouse())).build());

        Set<WarehouseGroup> group2 = Set.of(new WarehouseGroup(UUID.fromString("6b63c7bb-b829-45fb-8f84-460e4dfe2ef9"), "Group 2"));
        Optional<Warehouse> warehouse2 = Optional.of(Warehouse.builder().warehouseGroups(group2)
                .workerWarehouses(Set.of(WorkerWarehouseFactory. createSecondWorkerWarehouse())).build());

        return Stream.of(
                arguments(warehouse1, group1),
                arguments(warehouse2, group2)
       );

    }

}