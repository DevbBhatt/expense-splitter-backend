package com.expensesplitter.expense_splitter.service;


import com.expensesplitter.expense_splitter.dto.CreateGroupRequest;
import com.expensesplitter.expense_splitter.dto.GroupResponse;
import com.expensesplitter.expense_splitter.dto.UserResponse;
import com.expensesplitter.expense_splitter.entity.Group;
import com.expensesplitter.expense_splitter.entity.GroupMember;
import com.expensesplitter.expense_splitter.entity.User;
import com.expensesplitter.expense_splitter.exception.ResourceNotFoundException;
import com.expensesplitter.expense_splitter.mapper.GroupMapper;
import com.expensesplitter.expense_splitter.repository.GroupMemberRepository;
import com.expensesplitter.expense_splitter.repository.GroupRepository;
import com.expensesplitter.expense_splitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private CurrentUserService currentUserService;


    public GroupResponse createGroup(CreateGroupRequest request) {

        User user = currentUserService.getCurrentUser();

        Group group = new Group();

        group.setName(request.getName());
        group.setCreatedBy(user);
        group.setCreatedAt(LocalDateTime.now());


        Group savedGroup = groupRepository.save(group);

        GroupMember member = new GroupMember();
        member.setGroup(savedGroup);
        member.setUser(user);
        member.setJoinedAt(LocalDateTime.now());

        groupMemberRepository.save(member);

        return groupMapper.toResponse(savedGroup);
    }

    public Page<GroupResponse> getAllGroups(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Group> groups = groupRepository.findByIsDeletedFalse(pageable);

        return groups.map(groupMapper::toResponse);
    }

    public GroupResponse getGroupById(Long id) {

        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group Not Found"));

        if(group.isDeleted()){
            throw new ResourceNotFoundException("Group is Deleted");
        }

        return groupMapper.toResponse(group);
    }

    public GroupResponse updateGroup(Long id, CreateGroupRequest request) {

        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group Not Found"));

        if(group.isDeleted()){
            throw new ResourceNotFoundException("Group is Deleted");
        }

        group.setName(request.getName());

        Group updatedGroup = groupRepository.save(group);

        return groupMapper.toResponse(updatedGroup);
    }


    public GroupResponse deleteGroup(Long id) {

        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group Not Found"));

        if(group.isDeleted()){
            throw new ResourceNotFoundException("Group already deleted");
        }

        List<GroupMember> members = groupMemberRepository.findByGroup(group);

        for(GroupMember member : members){
            member.setDeleted(true);
        }

        groupMemberRepository.saveAll(members);

        group.setDeleted(true);

        Group deletedGroup = groupRepository.save(group);

        return groupMapper.toResponse(deletedGroup);
    }


}
