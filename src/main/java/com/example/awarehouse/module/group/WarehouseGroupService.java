package com.example.awarehouse.module.group;

import com.example.awarehouse.module.group.dto.BasicGroupInfoDto;
import com.example.awarehouse.module.group.dto.GroupRequest;
import com.example.awarehouse.module.warehouse.Warehouse;
import com.example.awarehouse.module.warehouse.WorkerWarehouseService;
import com.example.awarehouse.module.warehouse.dto.BasicWarehouseInfoDto;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.GroupDuplicateException;
import com.example.awarehouse.module.group.mapper.WarehouseGroupMapper;
import com.example.awarehouse.module.worker.Worker;
import com.example.awarehouse.module.worker.WorkerService;
import com.example.awarehouse.util.UserIdSupplier;
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

    public Map<BasicGroupInfoDto, List<BasicWarehouseInfoDto>> getAllGroupsWithWarehouses() {
        UUID workerId = workerIdSupplier.getUserId();
        Map<BasicGroupInfoDto, List<BasicWarehouseInfoDto>> groupsWithWarehouses = getGroupsFromWorkerWarehouses(workerId);
        addGroupsWhichAreNotAssociatedWithAnyWarehouses(groupsWithWarehouses, workerId);
        return groupsWithWarehouses;
    }

    public Map<BasicGroupInfoDto, List<BasicWarehouseInfoDto>> getGroupsFromWorkerWarehouses(UUID workerId){
        Set<Warehouse> warehouses = workerWarehouseService.getWorkerWarehouses(workerId);
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



}
