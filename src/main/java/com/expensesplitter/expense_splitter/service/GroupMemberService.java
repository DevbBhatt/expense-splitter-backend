package com.expensesplitter.expense_splitter.service;

import com.expensesplitter.expense_splitter.entity.Group;
import com.expensesplitter.expense_splitter.entity.GroupMember;
import com.expensesplitter.expense_splitter.entity.User;
import com.expensesplitter.expense_splitter.exception.BadRequestException;
import com.expensesplitter.expense_splitter.exception.ResourceNotFoundException;
import com.expensesplitter.expense_splitter.repository.GroupMemberRepository;
import com.expensesplitter.expense_splitter.repository.GroupRepository;
import com.expensesplitter.expense_splitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GroupMemberService {

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    public GroupMember addMemberToGroup(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(()-> new ResourceNotFoundException("Group Not Found"));
        if(group.isDeleted()) throw new ResourceNotFoundException("Group is deleted");

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User Not Found"));
        if(user.isDeleted()) throw new ResourceNotFoundException("User is deleted");

        Optional<GroupMember> existsMember = groupMemberRepository
                .findByGroupIdAndUserId(groupId, userId);

        if(existsMember.isPresent()){
            GroupMember member = existsMember.get();

            if(!member.isDeleted()){
                throw new BadRequestException("User Already in Group");
            }

            member.setDeleted(false);
            member.setJoinedAt(LocalDateTime.now());
            return groupMemberRepository.save(member);
        }


        // New Member
        GroupMember member = new GroupMember();

        member.setUser(user);
        member.setGroup(group);
        member.setJoinedAt(LocalDateTime.now());

        return groupMemberRepository.save(member);
    }


    public Page<GroupMember> getGroupMembers(Long groupId,int page,int size) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(()->new ResourceNotFoundException("Group Not Found"));
        if(group.isDeleted()) throw new ResourceNotFoundException("Group is Deleted");

        Pageable pageable = PageRequest.of(page, size);

        return groupMemberRepository.findByGroupAndIsDeletedFalseAndUser_IsDeletedFalse(group,pageable);


//        return groupMemberRepository.findByGroup(group)
//                .stream().filter(m -> !m.isDeleted()).toList();
    }

    public void removeMemberFromGroup(Long groupId,Long userId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(()->new ResourceNotFoundException("Group Not Found"));

        if(group.isDeleted()) throw new ResourceNotFoundException("Group is Deleted");

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User Not Found"));

        if(user.isDeleted()) throw new ResourceNotFoundException("User is Deleted");


        GroupMember member = groupMemberRepository
                .findByGroupAndUser(group,user).orElseThrow(()->new ResourceNotFoundException("Member Not Found"));

        if(member.isDeleted()){
            throw new ResourceNotFoundException("Member already removed");
        }

        member.setDeleted(true);
        groupMemberRepository.save(member);
    }
}
