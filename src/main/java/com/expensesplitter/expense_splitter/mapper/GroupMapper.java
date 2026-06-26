package com.expensesplitter.expense_splitter.mapper;


import com.expensesplitter.expense_splitter.dto.GroupResponse;
import com.expensesplitter.expense_splitter.dto.UserResponse;
import com.expensesplitter.expense_splitter.entity.Group;
import org.springframework.stereotype.Component;

@Component
public class GroupMapper {

    public GroupResponse toResponse(Group group) {

        GroupResponse response = new GroupResponse();

        response.setId(group.getId());
        response.setName(group.getName());
        response.setCreatedAt(group.getCreatedAt());

        UserResponse userResponse = new UserResponse();
        userResponse.setId(group.getCreatedBy().getId());
        userResponse.setName(group.getCreatedBy().getName());
        userResponse.setEmail(group.getCreatedBy().getEmail());

        response.setCreatedBy(userResponse);

        return response;
    }
}


