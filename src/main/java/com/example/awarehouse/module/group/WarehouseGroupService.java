package com.example.awarehouse.module.group;

import com.example.awarehouse.module.administration.dto.AdminWorkersDto;
import com.example.awarehouse.module.group.dto.BasicGroupInfoDto;
import com.example.awarehouse.module.group.dto.GroupRequest;
import com.example.awarehouse.module.token.OwnerType;
import com.example.awarehouse.module.token.SharingTokenService;
import com.example.awarehouse.module.warehouse.Role;
import com.example.awarehouse.module.warehouse.WorkerWarehouseService;
import com.example.awarehouse.module.warehouse.dto.BasicInfo;
import com.example.awarehouse.module.warehouse.mapper.GroupMapper;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.GroupDuplicateException;
import com.example.awarehouse.module.group.mapper.WarehouseGroupMapper;
import com.example.awarehouse.module.auth.Worker;
import com.example.awarehouse.module.auth.WorkerService;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.WorkerNotHaveAccess;
import com.example.awarehouse.util.UserIdSupplier;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.awarehouse.module.warehouse.util.WarehouseConstants.GROUP_ALREADY_EXIST;

@Service
@AllArgsConstructor
public class WarehouseGroupService {
    private final WarehouseGroupRepository warehouseGroupRepository;
    private final UserIdSupplier workerIdSupplier;
    private final WorkerService workerService;
    private final WorkerWarehouseService workerWarehouseService;
    private final GroupWorkerRepository groupWorkerRepository;
    private final SharingTokenService sharingTokenService;
    private final GroupWorkerService groupWorkerService;

    @Transactional
    public BasicGroupInfoDto createGroup(GroupRequest groupRequest){
        Worker worker = workerService.getWorker();
        checkExistence(groupRequest.name(), worker.getId());
        WarehouseGroup warehouseGroup = new WarehouseGroup(groupRequest.name(), worker);
        GroupWorker groupWorker = new GroupWorker(worker, warehouseGroup, Role.ADMIN);
        warehouseGroup.getGroupWorker().add(groupWorker);
        warehouseGroup = warehouseGroupRepository.save(warehouseGroup);
        sharingTokenService.createSharingToken( warehouseGroup.getId(), OwnerType.GROUP);
        return WarehouseGroupMapper.toDto(warehouseGroup);

    }
    private void  checkExistence(String name, UUID workerId){
        boolean exist = warehouseGroupRepository.checkIfNameExists(name, workerId);
        if(exist){
           throw  new GroupDuplicateException(GROUP_ALREADY_EXIST);
        }
    }

    public Set<WarehouseGroup> getGroups(Set<UUID> groupsId) {
        return Optional.ofNullable(warehouseGroupRepository.findAllById(groupsId)).orElseGet(Collections::emptyList).stream().collect(Collectors.toSet());
    }
    public Set<BasicGroupInfoDto> getBasicInfoGroups(Set<UUID> groupsId){
        return warehouseGroupRepository.findAllById(groupsId).stream().map(GroupMapper::toBasicGroupInfoDto).collect(Collectors.toSet());
    }

    public Optional<WarehouseGroup> getGroup(UUID groupId){
        return warehouseGroupRepository.findById(groupId);
    }


    public Set<BasicInfo> getAllAdminGroups() {
        UUID workerId = workerIdSupplier.getUserId();
        Set<BasicInfo> groups =groupWorkerRepository.findGroupWhereWorkerHasRole(workerId, Role.ADMIN).stream()
                .map((g)->new BasicGroupInfoDto(g.getId(), g.getName(), true)).collect(Collectors.toSet());
        return  groups;
    }

    public List<AdminWorkersDto> getGroupUsers(UUID workerId){
        Set<GroupWorker> groupWorkers = groupWorkerService.getGroupWhereWorkerIsAdmin(workerId);
        List<AdminWorkersDto> adminWorkersDtos = groupWorkers.stream()
                .collect(Collectors.groupingBy(GroupWorker::getGroup, Collectors.toList()))
                .entrySet()
                .stream()
                .map(entry -> new AdminWorkersDto(
                        entry.getKey().getName(),
                        entry.getKey().getId(),
                        entry.getValue().stream()
                                .map(groupWorker -> new AdminWorkersDto.Worker(
                                        groupWorker.getWorker().getId(),
                                        groupWorker.getId(),
                                        groupWorker.getWorker().getFirstName(),
                                        groupWorker.getWorker().getLastName(),
                                        groupWorker.getRole().toString().toLowerCase()
                                ))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
        return adminWorkersDtos;

    }
    @Transactional
    public void deleteGroup(List<UUID> groupIds) {
        UUID workerId = workerIdSupplier.getUserId();
        warehouseGroupRepository.findAllById(groupIds).forEach((g)->{
            Role role = g.getGroupWorker().stream()
                    .filter((gw)->gw.getWorker().getId().equals(workerId))
                    .findFirst()
                    .map(GroupWorker::getRole)
                    .orElseThrow(()->new WorkerNotHaveAccess("Worker with id "+workerId+" does not have access to group with id "+g.getId()));
            if(!role.equals(Role.ADMIN)){
               throw new WorkerNotHaveAccess("Worker with id "+workerId+" does not have access to group with id "+g.getId());
            }
        });
        warehouseGroupRepository.deleteAllByIdInBatch(groupIds);
    }

    public Optional<GroupWorker> findGroupWorker(UUID entityId) {
        return groupWorkerRepository.findById(entityId);
    }

    public List<GroupWorker> findGroupWorker(List<UUID> workerEntityIds) {
        return groupWorkerRepository.findAllById(workerEntityIds);
    }

    public void deleteGroups(List<GroupWorker> groupWorkers) {
        groupWorkerRepository.deleteAllByIdInBatch(groupWorkers.stream().map(GroupWorker::getId).collect(Collectors.toList()));
    }

    public boolean isWorkerWithGroupConnected(WarehouseGroup group){
        UUID workerId = workerIdSupplier.getUserId();
        return group.getGroupWorker().stream().anyMatch((gw)->gw.getWorker().getId().equals(workerId));
    }

    public void validateWorkerGroupRelation(List<UUID> groupIds) {
        UUID workerId = workerIdSupplier.getUserId();
        Set<UUID> groupIdsSet = new HashSet<>(groupIds);
        List<GroupWorker> groupWorkers = groupWorkerRepository.findAllDistinctByWorkerIdAndGroupIdIn(workerId, groupIdsSet);
        if(groupIdsSet.size()!=groupWorkers.size()){
            throw new WorkerNotHaveAccess("Worker with id "+workerId+" does not have access to all groups");
        }
    }
    public List<WarehouseGroup> getWorkerGroups(UUID workerId) {
        return groupWorkerRepository.findWorkerGroups(workerId);
    }
}
