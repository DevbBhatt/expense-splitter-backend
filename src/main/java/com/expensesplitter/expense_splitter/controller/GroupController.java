package com.expensesplitter.expense_splitter.controller;

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

    @PostMapping()
    public Group createGroup(@Valid @RequestBody Group group){

        return groupService.createGroup(group);
    }


    @GetMapping()
    public Page<Group> getAllGroups(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "5") int size){

        return groupService.getAllGroups(page,size);
    }



    @GetMapping("/{id}")
    public Group getGroupById(@PathVariable Long id){
        return groupService.getGroupById(id);
    }

    @PutMapping("/{id}")
    public Group updateGroup(@Valid @PathVariable Long id,
                             @RequestBody Group group){

       return groupService.updateGroup(id,group);
    }

    @DeleteMapping("/{id}")
    public Group deleteGroup(@PathVariable Long id){
        return groupService.deleteGroup(id);
    }

}
