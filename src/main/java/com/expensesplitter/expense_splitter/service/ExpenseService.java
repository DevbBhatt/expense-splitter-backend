package com.expensesplitter.expense_splitter.service;


import com.expensesplitter.expense_splitter.dto.ExpenseRequest;
import com.expensesplitter.expense_splitter.dto.ExpenseResponse;
import com.expensesplitter.expense_splitter.dto.SplitRequest;
import com.expensesplitter.expense_splitter.entity.Expense;
import com.expensesplitter.expense_splitter.entity.Group;
import com.expensesplitter.expense_splitter.entity.GroupMember;
import com.expensesplitter.expense_splitter.entity.User;
import com.expensesplitter.expense_splitter.exception.BadRequestException;
import com.expensesplitter.expense_splitter.exception.ResourceNotFoundException;
import com.expensesplitter.expense_splitter.mapper.ExpenseMapper;
import com.expensesplitter.expense_splitter.repository.ExpenseRepository;
import com.expensesplitter.expense_splitter.repository.GroupMemberRepository;
import com.expensesplitter.expense_splitter.repository.GroupRepository;
import com.expensesplitter.expense_splitter.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private SplitService splitService;

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private ExpenseMapper expenseMapper;



        @Transactional
        public ExpenseResponse addExpense(Long groupId,
                                          ExpenseRequest request) {

            Group group = groupRepository.findById(groupId)
                    .orElseThrow(()-> new ResourceNotFoundException("Group Not Found"));

            User user = currentUserService.getCurrentUser();

            if(group.isDeleted()){
                throw new ResourceNotFoundException("Group is deleted");
            }

            GroupMember member = groupMemberRepository.findByGroupAndUser(group,user)
                    .orElseThrow(()-> new ResourceNotFoundException("Member Not found"));
            if(member.isDeleted()) throw new ResourceNotFoundException("Member is Deleted");

            if (!"EQUAL".equalsIgnoreCase(request.getSplitType()) &&
                    !"EXACT".equalsIgnoreCase(request.getSplitType())) {
                throw new BadRequestException("Invalid split type");
            }


            // Important Validation
            List<GroupMember> members = groupMemberRepository.findByGroup(group)
                    .stream().filter(m-> !m.isDeleted()).toList();

            if("EXACT".equalsIgnoreCase(request.getSplitType()) &&
                    members.size() != request.getSplits().size()) {
                throw new BadRequestException("Splits size must match number of users in the group for EXACT type");
            }

            // ✅ VALIDATION FIRST
            validateSplits(request,group);

            Expense expense = new Expense();

            expense.setCreatedAt(LocalDateTime.now());
            expense.setDescription(request.getDescription());
            expense.setAmount(request.getAmount());
            expense.setSplitType(request.getSplitType());

            expense.setGroup(group);
            expense.setPaidBy(user);



            expense = expenseRepository.save(expense);

            splitService.splitExpense(expense, request.getSplits());


            return expenseMapper.toResponse(expense);
        }

    private void validateSplits(ExpenseRequest request, Group group) {

        if ("EXACT".equalsIgnoreCase(request.getSplitType())) {

            if (request.getSplits() == null || request.getSplits().isEmpty()) {
                throw new BadRequestException("Splits required for EXACT type");
            }

            Set<Long> userIds = new HashSet<>();
            double sum = 0.0;

            for (SplitRequest s : request.getSplits()) {

                // ❗ Duplicate user check
                if (!userIds.add(s.getUserId())) {
                    throw new BadRequestException("Duplicate user in splits: " + s.getUserId());
                }

                // ❗ Amount validation
                if (s.getAmount() == null || s.getAmount() <= 0) {
                    throw new BadRequestException("Invalid split amount for user: " + s.getUserId());
                }

                // ❗ User exist check
                User user = userRepository.findById(s.getUserId())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found: " + s.getUserId()));

                // ❗ Deleted user check
                if (user.isDeleted()) {
                    throw new ResourceNotFoundException("User is deleted: " + s.getUserId());
                }

                // ❗ Group membership check
                groupMemberRepository.findByGroupAndUser(group, user)
                        .orElseThrow(() -> new ResourceNotFoundException("User is not member of group: " + s.getUserId()));

                sum += s.getAmount();
            }

            // ❗ Floating precision safe check
            if (Math.abs(sum - request.getAmount()) > 0.01) {
                throw new BadRequestException("Split total must match expense amount");
            }
        }
    }


    public Page<ExpenseResponse> getGroupExpenses(Long groupId, int page, int size) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group Not Found"));

        if (group.isDeleted()) {
            throw new ResourceNotFoundException("Group is Deleted");
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<Expense> expenses =
                expenseRepository.findByGroupAndIsDeletedFalse(group, pageable);

        return expenses.map(expenseMapper::toResponse);
    }

    public ExpenseResponse getExpenseById(Long expenseId) {

        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense Not Found"));

        if (expense.isDeleted()) {
            throw new ResourceNotFoundException("Expense is Deleted");
        }

        return expenseMapper.toResponse(expense);
    }




    public ExpenseResponse deleteExpense(Long expenseId) {

        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense Not Found"));

        if (expense.isDeleted()) {
            throw new ResourceNotFoundException("Expense already deleted");
        }

        expense.setDeleted(true);

        Expense deletedExpense = expenseRepository.save(expense);

        return expenseMapper.toResponse(deletedExpense);
    }


//    public Expense updateExpense(Long expenseId) {
//
//        // Later
//
//    }
}
