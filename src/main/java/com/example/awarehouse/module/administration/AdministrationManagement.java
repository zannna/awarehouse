package com.example.awarehouse.module.administration;

import com.example.awarehouse.module.administration.dto.AdminWorkersDto;
import com.example.awarehouse.module.administration.dto.UpdateRoleDto;
import com.example.awarehouse.module.group.GroupWorker;
import com.example.awarehouse.module.group.WarehouseGroup;
import com.example.awarehouse.module.group.WarehouseGroupService;
import com.example.awarehouse.module.warehouse.Role;
import com.example.awarehouse.module.warehouse.Warehouse;
import com.example.awarehouse.module.warehouse.WorkerWarehouse;
import com.example.awarehouse.module.warehouse.WorkerWarehouseService;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.WorkerNotHaveAccess;
import com.example.awarehouse.util.UserIdSupplier;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AdministrationManagement {
    private final WarehouseGroupService groupService;
    private final WorkerWarehouseService workerWarehouseService;
    private  final UserIdSupplier workerIdSupplier;

    public List<AdminWorkersDto> getAdminData() {
        UUID workerId = workerIdSupplier.getUserId();
        List<AdminWorkersDto> basicInfos = groupService.getGroupUsers(workerId);
        basicInfos.addAll(workerWarehouseService.getAdminWarehouses(workerId));
        return basicInfos;
    }

    @Transactional
    public void updateRole(UpdateRoleDto updateRoleDto, UUID id) {
        UUID workerId = workerIdSupplier.getUserId();
        boolean updated = tryUpdateRoleInWorkerWarehouse(workerId, id, updateRoleDto.getRole());
        if(!updated){
            updated =tryUpdateRoleInGroupWorker(workerId, id, updateRoleDto.getRole());
        }
        if(!updated)
            throw new NoSuchElementException("Warehouse or group with id " + id + " does not exist");
    }
        private boolean tryUpdateRoleInWorkerWarehouse(UUID workerId, UUID entityId, Role role) {
            Optional<WorkerWarehouse> optionalWorkerWarehouse = workerWarehouseService.findWorkerWarehouse(entityId);
            if (optionalWorkerWarehouse.isPresent()) {
                WorkerWarehouse workerWarehouse = optionalWorkerWarehouse.get();
                Warehouse warehouse = workerWarehouse.getWarehouse();
                warehouse.getWorkerWarehouses()
                        .stream()
                        .filter(w -> w.getWorker().getId().equals(workerId) && w.getRole() == Role.ADMIN)
                        .findFirst()
                        .orElseThrow(() -> new WorkerNotHaveAccess("Worker with id " + workerId +
                                " does not have access to change data in warehouse"));
                    workerWarehouse.setRole(role);

                return true;
            }
            return false;
        }

    private boolean tryUpdateRoleInGroupWorker(UUID workerId, UUID entityId, Role role){
        Optional<GroupWorker> optionalGroupWorker = groupService.findGroupWorker(entityId);
        if(optionalGroupWorker.isPresent()){
            GroupWorker groupWorker = optionalGroupWorker.get();
            WarehouseGroup group = groupWorker.getGroup();
            group.getGroupWorker()
                    .stream()
                    .filter(g -> g.getWorker().getId().equals(workerId) && g.getRole() == Role.ADMIN)
                    .findFirst()
                    .orElseThrow(()->new WorkerNotHaveAccess("Worker with id " + workerId +
                            " does not have access to change data in group"));
            groupWorker.setRole(role);
            return true;
        }
      return false;
    }

    @Transactional
    public void deleteWorkerEntity(List<UUID> workerEntityIds) {
        UUID workerId = workerIdSupplier.getUserId();
        List<GroupWorker> groupWorkers = groupService.findGroupWorker(workerEntityIds);
        validateIfWorkerHasAccessToGroups(workerId, groupWorkers);
        groupService.deleteGroups(groupWorkers);
        List<WorkerWarehouse> workerWarehouses = workerWarehouseService.findWorkerWarehouse(workerEntityIds);
        validateIfWorkerHasAccessToWarehouses(workerId, workerWarehouses);
        workerWarehouseService.deleteWorkerWarehouses(workerWarehouses);
    }

    private void validateIfWorkerHasAccessToGroups(UUID workerId, List<GroupWorker> groupWorkers) {
        for (GroupWorker groupWorker : groupWorkers) {
            WarehouseGroup group = groupWorker.getGroup();
            group.getGroupWorker()
                    .stream()
                    .filter(g -> g.getWorker().getId().equals(workerId) && g.getRole() == Role.ADMIN)
                    .findFirst()
                    .orElseThrow(() -> new WorkerNotHaveAccess("Worker with id " + workerId +
                            " does not have access to change data in group"));
        }
    }

    private void validateIfWorkerHasAccessToWarehouses(UUID workerId, List<WorkerWarehouse> workerWarehouses) {
        for (WorkerWarehouse workerWarehouse : workerWarehouses) {
            Warehouse warehouse = workerWarehouse.getWarehouse();
            warehouse.getWorkerWarehouses()
                    .stream()
                    .filter(w -> w.getWorker().getId().equals(workerId) && w.getRole() == Role.ADMIN)
                    .findFirst()
                    .orElseThrow(() -> new WorkerNotHaveAccess("Worker with id " + workerId +
                            " does not have access to change data in warehouse"));
        }
    }
}
