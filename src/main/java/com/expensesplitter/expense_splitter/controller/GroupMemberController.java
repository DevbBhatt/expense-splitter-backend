package com.expensesplitter.expense_splitter.controller;

import com.expensesplitter.expense_splitter.entity.GroupMember;
import com.expensesplitter.expense_splitter.service.GroupMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupMemberController {

    @Autowired
    private GroupMemberService groupMemberService;

    @PostMapping("/{groupId}/members/{userId}")
    public GroupMember addMemberToGroup(@PathVariable Long groupId,@PathVariable Long userId){
        return groupMemberService.addMemberToGroup(groupId,userId);
    }

    @GetMapping("members/{groupId}")
    public Page<GroupMember> getGroupMembers(@PathVariable Long groupId,
                                             @RequestParam int page,
                                             @RequestParam int size){

        return groupMemberService.getGroupMembers(groupId,page,size);
    }

    @DeleteMapping("/{groupId}/members/{userId}")
    public void removeMemberFromGroup(@PathVariable Long groupId,
                                             @PathVariable Long userId){
         groupMemberService.removeMemberFromGroup(groupId,userId);
    }


}
