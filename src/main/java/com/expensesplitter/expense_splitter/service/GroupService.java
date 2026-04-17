package com.expensesplitter.expense_splitter.service;


import com.expensesplitter.expense_splitter.entity.Group;
import com.expensesplitter.expense_splitter.entity.GroupMember;
import com.expensesplitter.expense_splitter.entity.User;
import com.expensesplitter.expense_splitter.exception.ResourceNotFoundException;
import com.expensesplitter.expense_splitter.repository.GroupMemberRepository;
import com.expensesplitter.expense_splitter.repository.GroupRepository;
import com.expensesplitter.expense_splitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;


    public Group createGroup(Group group) {

        User user = userRepository.findById(group.getCreatedBy().getId())
                .orElseThrow(()-> new ResourceNotFoundException("User Not Found"));

        if(user.isDeleted()) throw new ResourceNotFoundException("User is Deleted");

        group.setCreatedBy(user);    // optional
        group.setCreatedAt(LocalDateTime.now());


        Group savedGroup = groupRepository.save(group);

        GroupMember member = new GroupMember();
        member.setGroup(savedGroup);
        member.setUser(user);
        member.setJoinedAt(LocalDateTime.now());

        groupMemberRepository.save(member);
        return savedGroup;
    }

    public List<Group> getAllGroups() {

        return groupRepository.findByIsDeletedFalse();
    }

    public Group getGroupById(Long id) {
        Group group =  groupRepository.findById(id).
                orElseThrow(()-> new ResourceNotFoundException("Group Not Found"));

        if(group.isDeleted()) {
            throw new ResourceNotFoundException("Group is Deleted");
        }

        return group;
    }

    public Group updateGroup(Long id, Group group) {
        Group group1 = groupRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Group Not Found"));

        if(group1.isDeleted()) throw new ResourceNotFoundException("Group Not Found");

        group1.setName(group.getName());
        return groupRepository.save(group1);
    }

    public Group deleteGroup(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group Not found"));
        if(group.isDeleted()){
            throw new ResourceNotFoundException("Group already deleted");
        }

        List<GroupMember> members = groupMemberRepository.findByGroup(group);

        for (GroupMember m : members) {
            m.setDeleted(true);
        }

        groupMemberRepository.saveAll(members);

        group.setDeleted(true);
        groupRepository.save(group);
        return group;

    }
}
