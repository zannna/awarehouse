package com.example.awarehouse.module.group;

import com.example.awarehouse.module.auth.Worker;
import com.example.awarehouse.module.auth.WorkerService;
import com.example.awarehouse.module.warehouse.Role;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.GroupNotExistException;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.WorkerNotHaveAccess;
import com.example.awarehouse.util.UserIdSupplier;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class GroupWorkerService {
    private final WarehouseGroupRepository warehouseGroupRepository;
    private final GroupWorkerRepository groupWorkerRepository;
    private final WorkerService workerService;
    private  final UserIdSupplier workerIdSupplier;

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

    public Optional<WarehouseGroup> getGroup(UUID userId, UUID groupId) {
        return groupWorkerRepository.findByGroupIdAndWorkerId(groupId, userId).map(GroupWorker::getGroup);
    }
    public List<WarehouseGroup> getGroups(UUID workerId, Set<UUID> groupsIds) {
        if(groupsIds==null || groupsIds.isEmpty()){
            return Collections.emptyList();
        }
        return groupWorkerRepository.findAllDistinctByWorkerIdAndGroupIdIn(workerId, groupsIds).stream().map(GroupWorker::getGroup).toList();
    }

    public Optional<WarehouseGroup> findByGroupName(String group) {
        return groupWorkerRepository.findByWorkerIdAndGroupName(workerIdSupplier.getUserId(), group).map(GroupWorker::getGroup);
    }
    public GroupWorker validateWorkerGroupRelation(UUID groupId){
       GroupWorker groupWorker = groupWorkerRepository.findByGroupIdAndWorkerId(groupId, workerIdSupplier.getUserId()).
                orElseThrow(()->new WorkerNotHaveAccess("Worker does not have access to group with id "+groupId));
       return groupWorker;
    }

}
