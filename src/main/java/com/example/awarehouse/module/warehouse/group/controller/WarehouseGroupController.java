package com.example.awarehouse.module.warehouse.group.controller;

import com.example.awarehouse.module.warehouse.group.WarehouseGroupService;
import com.example.awarehouse.module.warehouse.group.dto.GroupRequest;
import com.example.awarehouse.module.warehouse.group.dto.GroupResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.awarehouse.util.Constants.*;

@RestController
@RequestMapping(URI_VERSION_V1+URI_WAREHOUSE+URI_GROUP)
@AllArgsConstructor
public class WarehouseGroupController {
    WarehouseGroupService groupService;

    @PostMapping
    ResponseEntity<GroupResponse> createGroup(GroupRequest group){
        GroupResponse groupResponse = groupService.createGroup(group);
        return ResponseEntity.status(HttpStatus.CREATED).body(groupResponse);
    }
}
