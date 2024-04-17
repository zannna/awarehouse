package com.example.awarehouse.module.token;

import com.example.awarehouse.module.group.GroupWorkerService;
import com.example.awarehouse.module.token.dto.SharingTokenDto;
import com.example.awarehouse.module.token.exception.exceptions.SharingTokenNotExist;
import com.example.awarehouse.module.token.exception.exceptions.WarehouseNotHasSharingToken;
import com.example.awarehouse.module.warehouse.Role;
import com.example.awarehouse.module.warehouse.Warehouse;
import com.example.awarehouse.module.warehouse.WorkerWarehouseService;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.WorkerNotHaveAccess;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.WorkerWarehouseRelationNotExist;
import com.example.awarehouse.module.warehouse.util.factory.WarehouseFactory;
import com.example.awarehouse.module.auth.WorkerService;
import com.example.awarehouse.util.UserIdSupplier;
import org.junit.jupiter.api.Test;


import java.util.Optional;
import java.util.UUID;

import static com.example.awarehouse.exception.util.constants.ExceptionConstants.SHARINGTOKEN_NOT_EXIST;
import static com.example.awarehouse.exception.util.constants.ExceptionConstants.WAREHOUSE_NOT_CONTAIN_SHARINGTOKEN;
import static com.example.awarehouse.module.auth.util.WorkerConstants.WORKER_ID;
import static com.example.awarehouse.module.token.util.SharingTokenFactory.createSharingTokenForGroup;
import static com.example.awarehouse.module.token.util.SharingTokenFactory.createSharingTokenForWarehouse;
import static com.example.awarehouse.module.warehouse.util.WarehouseTestConstants.WAREHOUSE_ID;
import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SharingTokenServiceTest {

    TokenGenerator tokenGenerator = new BasicTokenGenerator();

    SharingTokenRepository sharingTokenRepository = mock(SharingTokenRepository.class);

    WorkerService workerService = mock(WorkerService.class);

    WorkerWarehouseService workerWarehouseService = mock(WorkerWarehouseService.class);

    GroupWorkerService groupWorkerService = mock(GroupWorkerService.class);

    UserIdSupplier workerIdSupplier = () -> WORKER_ID;

    @Test
    void createSharingToken_whenDataAreCorrect_shouldSaveSharingToken() {
        //given
        when(sharingTokenRepository.save(any(SharingToken.class))).thenAnswer(a -> a.getArgument(0));
        SharingTokenService sharingTokenService = new SharingTokenService(tokenGenerator, sharingTokenRepository,
                workerService, workerWarehouseService, workerIdSupplier, groupWorkerService);
        Warehouse warehouse = WarehouseFactory.createWarehouse();

        //when
        SharingToken sharingToken = sharingTokenService.createSharingToken(warehouse.getId(), OwnerType.WAREHOUSE);

        //then
        verify(sharingTokenRepository).save(any(SharingToken.class));
        assertThat(sharingToken.getSharingToken()).isNotNull();
        assertThat(sharingToken.getSalt()).isNotNull();
    }

    @Test
    void getSharingToken_whenDataAreCorrect_shouldFindSharingToken() {
        //given
        when(sharingTokenRepository.findByTokenOwnerId(any(UUID.class))).thenReturn(Optional.of(createSharingTokenForWarehouse()));
        SharingTokenService sharingTokenService = new SharingTokenService(tokenGenerator, sharingTokenRepository,
                workerService, workerWarehouseService, workerIdSupplier, groupWorkerService);

        //when
        SharingTokenDto sharingToken = sharingTokenService.getSharingToken(WAREHOUSE_ID);

        //then
        assertThat(sharingToken.sharingToken()).isNotNull();
    }

    @Test
    void getSharingToken_whenSharingTokenNotExist_shouldThrowException() {
        //given
        doThrow(new WarehouseNotHasSharingToken(WAREHOUSE_NOT_CONTAIN_SHARINGTOKEN)).when(sharingTokenRepository)
                .findByTokenOwnerId(any(UUID.class));

        SharingTokenService sharingTokenService = new SharingTokenService(tokenGenerator, sharingTokenRepository,
                workerService, workerWarehouseService, workerIdSupplier, groupWorkerService);

        // when-then
        assertThrows(
                WarehouseNotHasSharingToken.class,
                () -> sharingTokenService.getSharingToken(WAREHOUSE_ID)
        );
    }

    @Test
    void getSharingToken_whenSWorkerWarehouseRelationNotExist_shouldThrowException() {
        //given
        when(sharingTokenRepository.findByTokenOwnerId(any(UUID.class))).thenReturn(Optional.of(createSharingTokenForWarehouse()));
        doThrow(new WorkerWarehouseRelationNotExist(WAREHOUSE_NOT_CONTAIN_SHARINGTOKEN)).when(workerWarehouseService)
                .validateWorkerWarehouseRelation(any(UUID.class), any(UUID.class));

        SharingTokenService sharingTokenService = new SharingTokenService(tokenGenerator, sharingTokenRepository,
                workerService, workerWarehouseService, workerIdSupplier, groupWorkerService);

        // when-then
        assertThrows(
                WorkerWarehouseRelationNotExist.class,
                () -> sharingTokenService.getSharingToken(WAREHOUSE_ID)
        );
    }

    @Test
    void getSharingToken_whenWorkerNotHaveAccess_shouldThrowException() {
        //given
        when(sharingTokenRepository.findByTokenOwnerId(any(UUID.class))).thenReturn(Optional.of(createSharingTokenForGroup()));
        doThrow(new WorkerNotHaveAccess("Worker does not have access to group with id ")).when( groupWorkerService)
                .validateWorkerGroupRelation(any(UUID.class));
        SharingTokenService sharingTokenService = new SharingTokenService(tokenGenerator, sharingTokenRepository,
                workerService, workerWarehouseService, workerIdSupplier, groupWorkerService);

        // when-then
        assertThrows(
                WorkerNotHaveAccess.class,
                () -> sharingTokenService.getSharingToken(WAREHOUSE_ID)
        );
    }

    @Test
    void joinWarehouse_whenDataAreCorrect() {
        //given
        when(sharingTokenRepository.findBySharingToken(any(String.class))).thenReturn(Optional.of(createSharingTokenForWarehouse()));

        SharingTokenService sharingTokenService = new SharingTokenService(tokenGenerator, sharingTokenRepository,
                workerService, workerWarehouseService, workerIdSupplier, groupWorkerService);
        //when
          sharingTokenService.join(new SharingTokenDto("token"));

        //then
        verify( workerWarehouseService).newRelation(UUID.fromString("48d0c5d7-8e25-4ccd-b23f-b748137a0dc6"),  WORKER_ID, Role.WORKER);
    }

    @Test
    void joinGroup_whenDataAreCorrect() {
        //given
        when(sharingTokenRepository.findBySharingToken(any(String.class))).thenReturn(Optional.of(createSharingTokenForGroup()));

        SharingTokenService sharingTokenService = new SharingTokenService(tokenGenerator, sharingTokenRepository,
                workerService, workerWarehouseService, workerIdSupplier, groupWorkerService);
        //when
        sharingTokenService.join(new SharingTokenDto("token"));

        //then
        verify(groupWorkerService).addWorkerToGroup(UUID.fromString("48d0c5d7-8e25-4ccd-b23f-b748137a0dc6"),  WORKER_ID);
    }

    @Test
    void joinWarehouse_whenTokenNotExist_shouldThrowException(){
        //given
        doThrow(new SharingTokenNotExist(SHARINGTOKEN_NOT_EXIST)).when(sharingTokenRepository)
                .findBySharingToken(any(String.class));
        SharingTokenService sharingTokenService = new SharingTokenService(tokenGenerator, sharingTokenRepository,
        workerService, workerWarehouseService, workerIdSupplier, groupWorkerService);

        // when-then
        assertThrows(
                SharingTokenNotExist.class,
                () -> sharingTokenService.join(new SharingTokenDto("token"))
        );
    }

}