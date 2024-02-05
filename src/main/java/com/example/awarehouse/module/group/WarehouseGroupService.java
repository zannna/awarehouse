package com.example.awarehouse.module.group;

import com.example.awarehouse.module.group.dto.BasicGroupInfoDto;
import com.example.awarehouse.module.group.dto.GroupRequest;
import com.example.awarehouse.module.group.dto.GroupWithWarehouses;
import com.example.awarehouse.module.warehouse.Role;
import com.example.awarehouse.module.warehouse.Warehouse;
import com.example.awarehouse.module.warehouse.WorkerWarehouseService;
import com.example.awarehouse.module.warehouse.dto.BasicWarehouseInfoDto;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.GroupDuplicateException;
import com.example.awarehouse.module.group.mapper.WarehouseGroupMapper;
import com.example.awarehouse.module.auth.Worker;
import com.example.awarehouse.module.auth.WorkerService;
import com.example.awarehouse.util.UserIdSupplier;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.awarehouse.module.warehouse.util.WarehouseConstants.GROUP_ALREADY_EXIST;

@Service
@AllArgsConstructor
public class WarehouseGroupService {
    private final WarehouseGroupRepository warehouseGroupRepository;
    private final UserIdSupplier workerIdSupplier;
    private final WorkerService workerService;
    private final WorkerWarehouseService workerWarehouseService;

    public BasicGroupInfoDto createGroup(GroupRequest groupRequest){
        Worker worker = workerService.getWorker();
        checkExistence(groupRequest.name(), worker.getId());
        WarehouseGroup warehouseGroup = new WarehouseGroup(groupRequest.name(), worker);
        warehouseGroupRepository.save(warehouseGroup);
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
    public Optional<WarehouseGroup> getGroup(UUID groupId){
        return warehouseGroupRepository.findById(groupId);
    }

    public List<GroupWithWarehouses> getAllGroupsWithWarehouses() {
        UUID workerId = workerIdSupplier.getUserId();
        Set<Warehouse> warehouses = workerWarehouseService.getWorkerWarehouses(workerId);
        Map<BasicGroupInfoDto, List<BasicWarehouseInfoDto>> basicInfoMap = getGroupsWithWarehouses(warehouses);
        addGroupsWhichAreNotAssociatedWithAnyWarehouses(basicInfoMap, workerId);
        List<GroupWithWarehouses> groupsWithWarehouses = WarehouseGroupMapper.toGroupWithWarehouses(basicInfoMap);
        addWarehouseWhichNotHaveGroup(warehouses, groupsWithWarehouses);
        return groupsWithWarehouses;
    }

    private void addWarehouseWhichNotHaveGroup( Set<Warehouse> warehouses, List<GroupWithWarehouses> groupsWithWarehouses) {
        List<BasicWarehouseInfoDto> addedWarehouses =  groupsWithWarehouses.stream()
                .flatMap(groupWithWarehouses -> groupWithWarehouses.warehouses().stream()).collect(Collectors.toList());
        for (Warehouse warehouse : warehouses) {
            boolean added= addedWarehouses.stream().anyMatch(w-> w.id().equals(warehouse.getId()));
            if(!added){
                groupsWithWarehouses.add(new GroupWithWarehouses(null, List.of(new BasicWarehouseInfoDto(warehouse.getId(), warehouse.getName()))));
            }
        }
    }

    public Map<BasicGroupInfoDto, List<BasicWarehouseInfoDto>> getGroupsWithWarehouses(Set<Warehouse> warehouses){
        Map<BasicGroupInfoDto, List<BasicWarehouseInfoDto>> groupsWithWarehouses = new HashMap<>();
        for (Warehouse warehouse: warehouses) {
            addWarehouseGroupsToGroupsWithWarehouses(groupsWithWarehouses, warehouse);
        }
        return  groupsWithWarehouses;
    }

    private void addWarehouseGroupsToGroupsWithWarehouses(Map<BasicGroupInfoDto, List<BasicWarehouseInfoDto>> groupsWithWarehouses,
                                                          Warehouse warehouse){
        Set<BasicGroupInfoDto> warehouseGroups = warehouse.getWarehouseGroups().stream()
                .map((g)->new BasicGroupInfoDto(g.getId(), g.getName())).collect(Collectors.toSet());

        for (BasicGroupInfoDto group : warehouseGroups) {
            if(groupsWithWarehouses.containsKey(group)){
                addWarehouseToGroupsWithWarehouses(groupsWithWarehouses, warehouse, group);
            }
            else{
                addGroupToGroupsWithWarehouses(groupsWithWarehouses, warehouse, group);
            }
        }

    }

    private void addWarehouseToGroupsWithWarehouses(Map<BasicGroupInfoDto, List<BasicWarehouseInfoDto>> groupsWithWarehouses,
                                                    Warehouse warehouse, BasicGroupInfoDto group){
        groupsWithWarehouses.get(group).add(new BasicWarehouseInfoDto(warehouse.getId(), warehouse.getName()));
    }

    private void addGroupToGroupsWithWarehouses(Map<BasicGroupInfoDto, List<BasicWarehouseInfoDto>> groupsWithWarehouses,
                                                Warehouse warehouse, BasicGroupInfoDto group){
        List<BasicWarehouseInfoDto> warehouses= new ArrayList<>();
        warehouses.add(new BasicWarehouseInfoDto(warehouse.getId(), warehouse.getName()));
        groupsWithWarehouses.put(group, warehouses);
    }

    private void addGroupsWhichAreNotAssociatedWithAnyWarehouses(Map<BasicGroupInfoDto, List<BasicWarehouseInfoDto>> groupsWithWarehouses, UUID workerId){
        Set<BasicGroupInfoDto> groups = warehouseGroupRepository.findByWorkerId(workerId);
        for (BasicGroupInfoDto group: groups) {
            if(!groupsWithWarehouses.containsKey(group)){
                groupsWithWarehouses.put(group, new ArrayList<>());
            }
        }
    }


    public Set<BasicGroupInfoDto> getAllAdminGroups() {
        UUID workerId = workerIdSupplier.getUserId();
        Set<BasicGroupInfoDto> groups = getGroupsFromWorkerWarehousesWhereRoleIsAdmin(workerId);
        addGroupsWhichAreNotAssociatedWithAnyWarehouses(groups, workerId);
        return  groups;
    }

    private Set<BasicGroupInfoDto> getGroupsFromWorkerWarehousesWhereRoleIsAdmin(UUID workerId){
        Set<Warehouse> warehouses = workerWarehouseService.getWorkerWarehouses(workerId, Role.ADMIN);
        return warehouses
                .stream()
                .flatMap((w)->w.getWarehouseGroups().stream())
                .map((g)->new BasicGroupInfoDto(g.getId(), g.getName()))
                .collect(Collectors.toSet());
    }
    private void addGroupsWhichAreNotAssociatedWithAnyWarehouses(Set<BasicGroupInfoDto> groups, UUID workerId){
        Set<BasicGroupInfoDto> groupsCreatedByWorker = warehouseGroupRepository.findByWorkerId(workerId);
        groups.addAll(groupsCreatedByWorker);
    }
}
