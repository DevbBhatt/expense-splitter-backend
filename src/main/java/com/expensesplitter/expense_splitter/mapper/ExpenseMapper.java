package com.expensesplitter.expense_splitter.mapper;

import com.expensesplitter.expense_splitter.dto.ExpenseResponse;
import com.expensesplitter.expense_splitter.dto.UserResponse;
import com.expensesplitter.expense_splitter.entity.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapper {

    @Autowired
    private GroupMapper groupMapper;

    public ExpenseResponse toResponse(Expense expense) {

        ExpenseResponse response = new ExpenseResponse();

        response.setId(expense.getId());
        response.setDescription(expense.getDescription());
        response.setAmount(expense.getAmount());
        response.setSplitType(expense.getSplitType());
        response.setCreatedAt(expense.getCreatedAt());

        UserResponse paidBy = new UserResponse();

        paidBy.setId(expense.getPaidBy().getId());
        paidBy.setName(expense.getPaidBy().getName());
        paidBy.setEmail(expense.getPaidBy().getEmail());

        response.setPaidBy(paidBy);

        response.setGroup(
                groupMapper.toResponse(expense.getGroup())
        );

        return response;
    }
}