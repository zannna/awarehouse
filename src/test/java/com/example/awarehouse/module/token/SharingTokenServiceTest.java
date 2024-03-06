package com.example.awarehouse.module.token;

import com.example.awarehouse.module.token.dto.SharingTokenDto;
import com.example.awarehouse.module.token.exception.exceptions.SharingTokenNotExist;
import com.example.awarehouse.module.token.exception.exceptions.WarehouseNotHasSharingToken;
import com.example.awarehouse.module.warehouse.Role;
import com.example.awarehouse.module.warehouse.Warehouse;
import com.example.awarehouse.module.warehouse.WorkerWarehouse;
import com.example.awarehouse.module.warehouse.WorkerWarehouseService;
import com.example.awarehouse.module.warehouse.util.factory.WarehouseFactory;
import com.example.awarehouse.module.auth.WorkerService;
import com.example.awarehouse.util.UserIdSupplier;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static com.example.awarehouse.exception.util.constants.ExceptionConstants.SHARINGTOKEN_NOT_EXIST;
import static com.example.awarehouse.exception.util.constants.ExceptionConstants.WAREHOUSE_NOT_CONTAIN_SHARINGTOKEN;
import static com.example.awarehouse.module.token.util.SharingTokenFactory.createSharingToken;
import static com.example.awarehouse.module.warehouse.util.WarehouseTestConstants.WAREHOUSE_ID;
import static com.example.awarehouse.module.warehouse.util.factory.WorkerWarehouseFactory.createWorkerWarehouse;
import static com.example.awarehouse.module.auth.util.WorkerConstants.WORKER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SharingTokenServiceTest {

    TokenGenerator tokenGenerator = new BasicTokenGenerator();

    SharingTokenRepository sharingTokenRepository = mock(SharingTokenRepository.class);

    WorkerService workerService = mock(WorkerService.class);

    WorkerWarehouseService workerWarehouseService = mock(WorkerWarehouseService.class);

    UserIdSupplier workerIdSupplier = () -> WORKER_ID;

    @Test
    void createSharingToken_whenDataAreCorrect_shouldSaveSharingToken() {
        //given
        when(sharingTokenRepository.save(any(SharingToken.class))).thenAnswer(a -> a.getArgument(0));
        SharingTokenService sharingTokenService = new SharingTokenService(tokenGenerator, sharingTokenRepository,
                workerService, workerWarehouseService, workerIdSupplier);
        Warehouse warehouse = WarehouseFactory.createWarehouse();

        //when
        SharingToken sharingToken = sharingTokenService.createSharingToken(warehouse);

        //then
        verify(sharingTokenRepository).save(any(SharingToken.class));
        assertThat(sharingToken.getSharingToken()).isNotNull();
        assertThat(sharingToken.getSalt()).isNotNull();
    }

    @Test
    void getSharingToken_whenDataAreCorrect_shouldFindSharingToken() {
        //given
        when(sharingTokenRepository.findByWarehouseId(any(UUID.class))).thenReturn(Optional.of(createSharingToken()));
        SharingTokenService sharingTokenService = new SharingTokenService(tokenGenerator, sharingTokenRepository,
                workerService, workerWarehouseService, workerIdSupplier);

        //when
        SharingTokenDto sharingToken = sharingTokenService.getSharingToken(WAREHOUSE_ID);

        //then
        assertThat(sharingToken.sharingToken()).isNotNull();
    }

    @Test
    void getSharingToken_whenSharingTokenNotExist_shouldThrowException() {
        //given
        doThrow(new WarehouseNotHasSharingToken(WAREHOUSE_NOT_CONTAIN_SHARINGTOKEN)).when(sharingTokenRepository)
                .findByWarehouseId(any(UUID.class));

        SharingTokenService sharingTokenService = new SharingTokenService(tokenGenerator, sharingTokenRepository,
                workerService, workerWarehouseService, workerIdSupplier);

        // when-then
        assertThrows(
                WarehouseNotHasSharingToken.class,
                () -> sharingTokenService.getSharingToken(WAREHOUSE_ID)
        );
    }

    @Test
    void joinWarehouse_whenDataAreCorrect_shouldReturnWarehouseId() {
        //given
        when(sharingTokenRepository.findBySharingToken(any(String.class))).thenReturn(Optional.of(createSharingToken()));
        WorkerWarehouse workerWarehouse = createWorkerWarehouse();
        when(workerWarehouseService.newRelation(any(UUID.class), any(UUID.class), any(Role.class))).thenReturn(workerWarehouse);
        SharingTokenService sharingTokenService = new SharingTokenService(tokenGenerator, sharingTokenRepository,
                workerService, workerWarehouseService, workerIdSupplier);

        //when
        WarehouseId warehouseId = sharingTokenService.joinWarehouse("48d0c5d7-8e25-4ccd-b23f-b748137a0dc6");

        //then
        assertThat(warehouseId.warehouseId()).isEqualTo(workerWarehouse.getWarehouse().getId());
    }

    @Test
    void joinWarehouse_whenTokenNotExist_shouldThrowException(){
        //given
        doThrow(new SharingTokenNotExist(SHARINGTOKEN_NOT_EXIST)).when(sharingTokenRepository)
                .findBySharingToken(any(String.class));
        SharingTokenService sharingTokenService = new SharingTokenService(tokenGenerator, sharingTokenRepository,
                workerService, workerWarehouseService, workerIdSupplier);

        // when-then
        assertThrows(
                SharingTokenNotExist.class,
                () -> sharingTokenService.joinWarehouse("48d0c5d7-8e25-4ccd-b23f-b748137a0dc6")
        );
    }

}