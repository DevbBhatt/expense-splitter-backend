package com.expensesplitter.expense_splitter.controller;

import com.expensesplitter.expense_splitter.dto.CreateGroupRequest;
import com.expensesplitter.expense_splitter.dto.GroupResponse;
import com.expensesplitter.expense_splitter.entity.Group;
import com.expensesplitter.expense_splitter.service.GroupService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
@SecurityRequirement(name = "bearerAuth")
public class GroupController {


    @Autowired
    private GroupService groupService;

    @PostMapping
    public GroupResponse createGroup(@Valid @RequestBody CreateGroupRequest request) {
        return groupService.createGroup(request);
    }


    @GetMapping
    public Page<GroupResponse> getAllGroups(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size){

        return groupService.getAllGroups(page, size);
    }



    @GetMapping("/{id}")
    public GroupResponse getGroupById(@PathVariable Long id){
        return groupService.getGroupById(id);
    }

    @PutMapping("/{id}")
    public GroupResponse updateGroup(
            @PathVariable Long id,
            @Valid @RequestBody CreateGroupRequest request){

        return groupService.updateGroup(id, request);
    }


    @DeleteMapping("/{id}")
    public GroupResponse deleteGroup(@PathVariable Long id){
        return groupService.deleteGroup(id);
    }
}
