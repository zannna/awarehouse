package com.example.awarehouse.module.token;


import com.example.awarehouse.module.token.dto.SharingTokenResponse;
import com.example.awarehouse.module.token.dto.WarehouseId;
import com.example.awarehouse.module.token.exception.exceptions.SharingTokenNotExist;
import com.example.awarehouse.module.token.exception.exceptions.WarehouseNotHasSharingToken;
import com.example.awarehouse.module.token.mapper.SharingTokenMapper;
import com.example.awarehouse.module.warehouse.Role;
import com.example.awarehouse.module.warehouse.Warehouse;
import com.example.awarehouse.module.warehouse.WorkerWarehouse;
import com.example.awarehouse.module.warehouse.WorkerWarehouseService;
import com.example.awarehouse.module.worker.WorkerService;
import com.example.awarehouse.util.UserIdSupplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.example.awarehouse.exception.util.constants.ExceptionConstants.SHARINGTOKEN_NOT_EXIST;
import static com.example.awarehouse.exception.util.constants.ExceptionConstants.WAREHOUSE_NOT_CONTAIN_SHARINGTOKEN;

@Service
@RequiredArgsConstructor
public class SharingTokenService {
    private final TokenGenerator tokenGenerator;

    private final SharingTokenRepository sharingTokenRepository;

    private final WorkerService workerService;

    private final WorkerWarehouseService workerWarehouseService;

    private final UserIdSupplier workerIdSupplier;


    public SharingToken createSharingToken(Warehouse warehouse) {
        String salt = tokenGenerator.generateSalt();
        String token = tokenGenerator.generateToken(warehouse.getId().toString(), salt);
        SharingToken sharingToken = new SharingToken(token, salt, warehouse);
        sharingToken = sharingTokenRepository.save(sharingToken);
        return sharingToken;
    }

    public SharingTokenResponse getSharingToken(UUID warehouseId) {
        workerService.validateIfWorkerIsAdmin(workerIdSupplier.getUserId());
        return sharingTokenRepository.findByWarehouseId(warehouseId)
                .map(SharingTokenMapper::toDto)
                .orElseThrow(() ->new WarehouseNotHasSharingToken(WAREHOUSE_NOT_CONTAIN_SHARINGTOKEN));
    }

    public WarehouseId joinWarehouse(String sharingTokenValue) {
       SharingToken sharingToken = sharingTokenRepository.findBySharingToken(sharingTokenValue).orElseThrow(()->
                new SharingTokenNotExist(SHARINGTOKEN_NOT_EXIST));
        WorkerWarehouse workerWarehouse = workerWarehouseService.newRelation(sharingToken.getWarehouse().getId(), workerIdSupplier.getUserId(), Role.WORKER);
        return new WarehouseId(workerWarehouse.getWarehouse().getId());
    }

}
