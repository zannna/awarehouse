package com.example.awarehouse.module.group;

import com.example.awarehouse.module.auth.Worker;
import com.example.awarehouse.module.auth.WorkerService;
import com.example.awarehouse.module.warehouse.Role;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.GroupNotExistException;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.WorkerNotHaveAccess;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GroupWorkerService {
    private final WarehouseGroupRepository warehouseGroupRepository;
    private final GroupWorkerRepository groupWorkerRepository;
    private final WorkerService workerService;

    public void addWorkerToGroup(UUID groupId, UUID userId) {
        Worker worker = workerService.getWorker();
        WarehouseGroup group = warehouseGroupRepository.findById(groupId).orElseThrow(()->new GroupNotExistException("Group with id "+groupId+" does not exist"));
        if(group.getGroupWorker().stream().anyMatch((gw)->gw.getWorker().getId().equals(userId))){
            throw new WorkerNotHaveAccess("Worker with id "+userId+" is already in group with id "+groupId);
        }
        GroupWorker groupWorker = new GroupWorker(worker, group, Role.WORKER);
        groupWorkerRepository.save(groupWorker);

    }

    public Set<GroupWorker> getGroupWhereWorkerIsAdmin(UUID workerId) {
        return groupWorkerRepository.findWorkers(workerId, Role.ADMIN);
    }

}
