package com.expensesplitter.expense_splitter.mapper;

import com.expensesplitter.expense_splitter.dto.UserResponse;
import com.expensesplitter.expense_splitter.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());

        return response;
    }
}