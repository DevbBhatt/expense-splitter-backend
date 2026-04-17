package com.expensesplitter.expense_splitter.service;

import com.expensesplitter.expense_splitter.dto.SplitRequest;
import com.expensesplitter.expense_splitter.entity.*;
import com.expensesplitter.expense_splitter.exception.BadRequestException;
import com.expensesplitter.expense_splitter.exception.ResourceNotFoundException;
import com.expensesplitter.expense_splitter.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SplitService {

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private ExpenseSplitRepository expenseSplitRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    public void splitExpense(Expense expense, List<SplitRequest> splits){

        if ("EQUAL".equalsIgnoreCase(expense.getSplitType())) {
            equalSplit(expense);
        }
        else if ("EXACT".equalsIgnoreCase(expense.getSplitType())) {

            if (splits == null || splits.isEmpty()) {
                throw new BadRequestException("Splits required for EXACT split type");
            }

            exactSplit(expense, splits);
        }
        else {
            throw new BadRequestException("Invalid split type");
        }
    }



    private void equalSplit(Expense expense) {

        Group group = expense.getGroup();

        if(group.isDeleted()){
            throw new ResourceNotFoundException("Group is deleted");
        }

        List<GroupMember> members = groupMemberRepository
                .findByGroupAndIsDeletedFalseAndUser_IsDeletedFalse(group);

        int totalMembers = members.size();
        double amount = expense.getAmount();

        // Check Members is not 0
        if(totalMembers == 0){
            throw new ResourceNotFoundException("No members in group");
        }

        double share = amount / totalMembers;

        for(GroupMember member:members){
            ExpenseSplit split = new ExpenseSplit();

            split.setExpense(expense);
            split.setAmount(share);
            split.setUser(member.getUser());

            expenseSplitRepository.save(split);
        }
    }


    private void exactSplit(Expense expense,List<SplitRequest> splits) {

        double totalAmount = expense.getAmount();
        double sum = 0;

        for(SplitRequest split : splits){
            sum += split.getAmount();
        }

        if(Math.abs(sum - totalAmount) > 0.01){
            throw new BadRequestException("Split amount do not match Expense amount");
        }

        for(SplitRequest split:splits) {
            User user = userRepository.findById(split.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User Not found"));

            if (user.isDeleted()) {
                throw new BadRequestException("User is deleted: " + user.getId());
            }
           GroupMember member =  groupMemberRepository.findByGroupAndUser(expense.getGroup(), user)
                    .orElseThrow(() -> new ResourceNotFoundException("User not in group"));
            if(member.isDeleted()) throw new ResourceNotFoundException("Member not in Group");

            ExpenseSplit expenseSplit = new ExpenseSplit();

            expenseSplit.setExpense(expense);
            expenseSplit.setUser(user);
            expenseSplit.setAmount(split.getAmount());

            expenseSplitRepository.save(expenseSplit);

        }

    }

    public List<ExpenseSplit> getExpenseSplits(Long expenseId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense Not Found"));

        if (expense.isDeleted()) {
            throw new ResourceNotFoundException("Expense Not Found");
        }

        return expenseSplitRepository.findByExpense(expense);
    }
}