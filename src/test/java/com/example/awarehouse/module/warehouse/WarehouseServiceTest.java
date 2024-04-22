package com.example.awarehouse.module.warehouse;

import com.example.awarehouse.common.util.ContextMock;
import com.example.awarehouse.module.group.dto.BasicGroupInfoDto;
import com.example.awarehouse.module.token.OwnerType;
import com.example.awarehouse.module.warehouse.dto.*;
import com.example.awarehouse.module.group.WarehouseGroup;
import com.example.awarehouse.module.token.SharingTokenService;
import com.example.awarehouse.module.group.WarehouseGroupService;
import com.example.awarehouse.module.warehouse.util.factory.GroupFactory;
import com.example.awarehouse.module.warehouse.util.factory.WarehouseDtoFactory;

import com.example.awarehouse.module.warehouse.util.factory.WorkerWarehouseFactory;
import com.example.awarehouse.module.auth.util.factory.WorkerFactory;
import com.example.awarehouse.util.UserIdSupplier;
import jakarta.validation.Validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static com.example.awarehouse.common.util.Constants.WORKER_ID;
import static com.example.awarehouse.module.warehouse.util.WarehouseTestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
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
    UserIdSupplier userIdSupplier = mock(UserIdSupplier.class);

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
                warehouseRepository, groupService, validator, userIdSupplier);
        WarehouseCreation warehouseCreation = WarehouseDtoFactory.createWarehouseCreation();

        //when
        WarehouseResponseDto result= warehouseService.createWarehouse(warehouseCreation);

        //then
        verify(warehouseRepository).save(any( Warehouse.class));
        verify(workerWarehouseService).newRelation(WAREHOUSE_ID, WORKER_ID, Role.ADMIN);
        verify(sharingTokenService).createSharingToken(any(UUID.class), any(OwnerType.class));
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
        Optional<WarehouseGroup> group = Optional.of(new WarehouseGroup(UUID.fromString("1b0cbc82-236f-4846-ac08-6d88baa91294"),
                "Group 2",WorkerFactory.createWorker(), new HashSet<>()));
        when(warehouseRepository.findById(any())).thenReturn(warehouse);
        when(groupService.getGroup(any(UUID.class))).thenReturn(group);
        WarehouseService warehouseService = new WarehouseService(workerWarehouseService, sharingTokenService,
                warehouseRepository, groupService, validator, userIdSupplier);

        //when
        warehouseService.addWarehouseToGroup(UUID.fromString("1b0cbc82-236f-4846-ac08-6d88baa91294"), warehouse.get().getId());

        //then
        Set<WarehouseGroup> receivedGroups = warehouse.get().getWarehouseGroups();
        givenGroups.add(group.get());
        assertThat(receivedGroups).isEqualTo(givenGroups);
    }

    @Test
    public void getGroupsAssociatedWithWarehouse_whenDataAreValid_thenReturnMapWithGroupsAndWarehouses(){
        contextMock.setContext();
        BasicGroupInfoDto group1 = new BasicGroupInfoDto(UUID.fromString("4a50b7ce-9a90-11ee-b9d1-0242ac120002"), "group 1", null);
        BasicGroupInfoDto group2 = new BasicGroupInfoDto(UUID.fromString("7c1a4bc6-9a90-11ee-b9d1-0242ac120002"), "group 2", null);
        BasicWarehouseInfoDto warehouse1 = new BasicWarehouseInfoDto(UUID.fromString("a8ad8360-9a90-11ee-b9d1-0242ac120002"), "warehouse 1", null);
        BasicWarehouseInfoDto warehouse2 = new BasicWarehouseInfoDto(UUID.fromString("d10e7fee-9a90-11ee-b9d1-0242ac120002"), "warehouse 2", null);
        BasicWarehouseInfoDto warehouse3 =new BasicWarehouseInfoDto(UUID.fromString("76cc2650-9ad4-11ee-b9d1-0242ac120002"),"warehouse 3", null );
        when(workerWarehouseService.getWarehouseGroups(any(UUID.class), any())).thenReturn(
                List.of(new GroupWarehouseDto(group1,warehouse1),
                        new GroupWarehouseDto(group1, warehouse2),
                        new GroupWarehouseDto(group2, warehouse1),
                        new GroupWarehouseDto(group1, warehouse3)
        ));
        WarehouseService warehouseService = new WarehouseService(workerWarehouseService, sharingTokenService,
                warehouseRepository, groupService, validator, userIdSupplier);
        Map<BasicGroupInfoDto, Set<BasicWarehouseInfoDto>> groupsWithWarehouses = warehouseService.getGroupsAssociatedWithWarehouse(UUID.fromString("4a50b7ce-9a90-11ee-b9d1-0242ac120002"));
        assertEquals(2, groupsWithWarehouses.keySet().size());
        assertEquals(Set.of(warehouse1, warehouse2, warehouse3), groupsWithWarehouses.get(group1));
        assertEquals(Set.of(warehouse1), groupsWithWarehouses.get(group2));
    }

    static List<Set<WarehouseGroup>> groups() {
        List<Set<WarehouseGroup>> result = new ArrayList<>();
        result.add(GroupFactory.createListOfGroups());
        result.add(null);
        return result;
    }
    static Stream<Arguments> warehouseProvider(){
        Set<WarehouseGroup> group1 = new HashSet<>();
        group1.add(new WarehouseGroup(UUID.fromString("1b0cbc82-236f-4846-ac08-6d88baa91294"),
                "Group 1", null, new HashSet<>()));
        Optional<Warehouse> warehouse1 =Optional.of(Warehouse.builder().warehouseGroups(group1)
                .workerWarehouses(Set.of(WorkerWarehouseFactory.createWorkerWarehouse())).build());

        Set<WarehouseGroup> group2 = new HashSet<>();
        group2.add(new WarehouseGroup(UUID.fromString("6b63c7bb-b829-45fb-8f84-460e4dfe2ef9"),
                "Group 2", null, new HashSet<>()));
        Optional<Warehouse> warehouse2 = Optional.of(Warehouse.builder().warehouseGroups(group2)
                .workerWarehouses(Set.of(WorkerWarehouseFactory.createSecondWorkerWarehouse()))
                .build());

        return Stream.of(
                arguments(warehouse1, group1),
                arguments(warehouse2, group2)
       );

    }



}