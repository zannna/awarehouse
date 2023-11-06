package com.example.awarehouse.module.warehouse.group;

import com.example.awarehouse.module.warehouse.group.dto.GroupRequest;
import com.example.awarehouse.module.warehouse.group.dto.GroupResponse;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.GroupDuplicateException;
import com.example.awarehouse.module.warehouse.group.mapper.WarehouseGroupMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.awarehouse.module.warehouse.util.WarehouseConstants.GROUP_ALREADY_EXIST;

@Service
@AllArgsConstructor
public class WarehouseGroupService {
    private final GroupRepository groupRepository;

    public GroupResponse createGroup(GroupRequest groupRequest){
        UUID workerId =workerId();
        checkExistence(groupRequest.name(), workerId);
        WarehouseGroup warehouseGroup = groupRepository.createGroup(groupRequest.name(), workerId);
        return WarehouseGroupMapper.toDto(warehouseGroup);

    }
    private void  checkExistence(String name, UUID workerId){
        boolean exist = groupRepository.checkIfNameExists(name, workerId);
        if(exist){
           throw  new GroupDuplicateException(GROUP_ALREADY_EXIST);
        }
    }
    private UUID workerId(){
        Jwt token= (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return UUID.fromString(token.getClaim("sub"));
    }

    public Set<WarehouseGroup> getGroups(Set<Long> groupsId) {
        return Optional.ofNullable(groupRepository.findAllById(groupsId)).orElseGet(Collections::emptyList).stream().collect(Collectors.toSet());
    }
    public Optional<WarehouseGroup> getGroup(Long groupId){
        return groupRepository.findById(groupId);
    }

}
