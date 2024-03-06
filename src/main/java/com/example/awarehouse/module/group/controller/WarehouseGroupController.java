package com.example.awarehouse.module.group.controller;

import com.example.awarehouse.module.group.WarehouseGroupService;
import com.example.awarehouse.module.group.dto.BasicGroupInfoDto;
import com.example.awarehouse.module.group.dto.GroupRequest;
import com.example.awarehouse.module.group.dto.GroupWithWarehouses;
import com.example.awarehouse.module.warehouse.dto.BasicInfo;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.example.awarehouse.util.Constants.*;

@RestController
@RequestMapping(URI_VERSION_V1+URI_WAREHOUSE+URI_GROUP)
@AllArgsConstructor
public class WarehouseGroupController {
    WarehouseGroupService groupService;

    @PostMapping
    ResponseEntity<BasicGroupInfoDto> createGroup(@RequestBody GroupRequest group){
        BasicGroupInfoDto basicGroupInfoDto = groupService.createGroup(group);
        return ResponseEntity.status(HttpStatus.CREATED).body(basicGroupInfoDto);
    }

    @DeleteMapping("/{groupIds}")
    ResponseEntity<HttpStatus> deleteGroup(@PathVariable List<UUID> groupIds){
        groupService.deleteGroup(groupIds);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    ResponseEntity<List<GroupWithWarehouses>> getAllGroupsWithWarehouses(){
        List<GroupWithWarehouses> groups = groupService.getAllGroupsWithWarehouses();
        return  ResponseEntity.status(HttpStatus.OK).body(groups);
    }

    @GetMapping("/admin")
    ResponseEntity<Set<BasicInfo>> getAllAdminGroups(){
        Set<BasicInfo> groups =groupService.getAllAdminGroups();
        return  ResponseEntity.status(HttpStatus.OK).body(groups);
    }
}
