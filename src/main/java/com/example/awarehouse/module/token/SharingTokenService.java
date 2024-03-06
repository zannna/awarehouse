package com.example.awarehouse.module.token;


import com.example.awarehouse.module.group.GroupWorkerService;
import com.example.awarehouse.module.token.dto.SharingTokenDto;
import com.example.awarehouse.module.token.exception.exceptions.SharingTokenNotExist;
import com.example.awarehouse.module.token.exception.exceptions.WarehouseNotHasSharingToken;
import com.example.awarehouse.module.token.mapper.SharingTokenMapper;
import com.example.awarehouse.module.warehouse.Role;
import com.example.awarehouse.module.warehouse.WorkerWarehouseService;
import com.example.awarehouse.module.auth.WorkerService;
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

    private final GroupWorkerService groupWorkerService;


    public SharingToken createSharingToken(UUID warehouseId, OwnerType ownerType) {
        String salt = tokenGenerator.generateSalt();
        String token = tokenGenerator.generateToken(warehouseId.toString(), salt);
        SharingToken sharingToken = new SharingToken(token, salt, warehouseId, ownerType);
        sharingToken = sharingTokenRepository.save(sharingToken);
        return sharingToken;
    }


    public SharingTokenDto getSharingToken(UUID tokenOwnerId) {
        workerService.validateIfWorkerIsAdmin(workerIdSupplier.getUserId());
        return sharingTokenRepository.findByTokenOwnerId(tokenOwnerId)
                .map(SharingTokenMapper::toDto)
                .orElseThrow(() ->new WarehouseNotHasSharingToken(WAREHOUSE_NOT_CONTAIN_SHARINGTOKEN));
    }

    public void  joinWarehouse(SharingTokenDto sharingTokenDto) {
       SharingToken sharingToken = sharingTokenRepository.findBySharingToken( sharingTokenDto.sharingToken()).orElseThrow(()->
                new SharingTokenNotExist(SHARINGTOKEN_NOT_EXIST));
       if(sharingToken.getOwnerType().equals(OwnerType.WAREHOUSE)) {
            workerWarehouseService.newRelation(sharingToken.getTokenOwnerId(), workerIdSupplier.getUserId(), Role.WORKER);
       }
       else if(sharingToken.getOwnerType().equals(OwnerType.GROUP)){
           groupWorkerService.addWorkerToGroup(sharingToken.getTokenOwnerId(), workerIdSupplier.getUserId());
       }
    }

}
