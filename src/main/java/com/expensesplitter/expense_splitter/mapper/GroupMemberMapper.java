package com.expensesplitter.expense_splitter.mapper;

import com.expensesplitter.expense_splitter.dto.GroupMemberResponse;
import com.expensesplitter.expense_splitter.entity.GroupMember;
import org.springframework.stereotype.Component;

@Component
public class GroupMemberMapper {

    public GroupMemberResponse toResponse(GroupMember member) {

        GroupMemberResponse response = new GroupMemberResponse();

        response.setId(member.getId());

        response.setUserId(member.getUser().getId());

        response.setUserName(member.getUser().getName());

        response.setUserEmail(member.getUser().getEmail());

        response.setJoinedAt(member.getJoinedAt());

        return response;
    }
}
