package com.expensesplitter.expense_splitter.service;


import com.expensesplitter.expense_splitter.dto.BalanceResponse;
import com.expensesplitter.expense_splitter.dto.SettlementDTO;
import com.expensesplitter.expense_splitter.dto.UserBalanceSummaryResponse;
import com.expensesplitter.expense_splitter.entity.Expense;
import com.expensesplitter.expense_splitter.entity.ExpenseSplit;
import com.expensesplitter.expense_splitter.entity.Group;
import com.expensesplitter.expense_splitter.entity.User;
import com.expensesplitter.expense_splitter.exception.BadRequestException;
import com.expensesplitter.expense_splitter.exception.ResourceNotFoundException;
import com.expensesplitter.expense_splitter.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class BalanceService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseSplitRepository expenseSplitRepository;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    public List<UserBalanceSummaryResponse> getMyBalances() {

        User currentUser = currentUserService.getCurrentUser();

        List<Group> groups = groupRepository.findByIsDeletedFalse();

        List<UserBalanceSummaryResponse> response = new ArrayList<>();

        for (Group group : groups) {

            boolean isMember = groupMemberRepository
                    .existsByGroupIdAndUserId(group.getId(), currentUser.getId());

            if (!isMember) {
                continue;
            }

            Map<User, Double> balances = calculateBalance(group.getId());

            UserBalanceSummaryResponse dto = new UserBalanceSummaryResponse();

            dto.setGroupName(group.getName());

            dto.setBalance(
                    balances.getOrDefault(currentUser, 0D)
            );

            response.add(dto);

        }

        return response;

    }


    public List<BalanceResponse> getGroupBalance(Long groupId) {

        Map<User, Double> balances = calculateBalance(groupId);

        List<BalanceResponse> response = new ArrayList<>();

        for (Map.Entry<User, Double> entry : balances.entrySet()) {

            BalanceResponse dto = new BalanceResponse();

            dto.setUserId(entry.getKey().getId());
            dto.setUserName(entry.getKey().getName());
            dto.setBalance(entry.getValue());

            response.add(dto);
        }

        return response;
    }

    private Map<User, Double> calculateBalance(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(()->new ResourceNotFoundException("Group Not Found"));

        if(group.isDeleted()) throw new ResourceNotFoundException("Group is Deleted");


        User currentUser = currentUserService.getCurrentUser();

        boolean isMember = groupMemberRepository
                .existsByGroupIdAndUserId(groupId, currentUser.getId());

        if (!isMember) {
            throw new BadRequestException("You are not a member of this group");
        }

        List<Expense> expenses = expenseRepository.findByGroup(group);

        Map<User,Double> map = new HashMap<>();

        for(Expense expense:expenses){

            if(expense.isDeleted()) continue;

        List<ExpenseSplit> expenseSplits = expenseSplitRepository.findByExpense(expense);

            User user = expense.getPaidBy();
            Double totalAmount = expense.getAmount();

            if(map.containsKey(user)) map.put(user,map.get(user)+(totalAmount));
            else map.put(user,totalAmount);

        for(ExpenseSplit expenseSplit:expenseSplits){
            User user1 = expenseSplit.getUser();
            Double amount = expenseSplit.getAmount();
            map.put(user1, map.getOrDefault(user1, 0D) - amount);
          }
        }

        return map;
    }


    public Double getUserBalanceInGroup(Long groupId, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User Not Found"));


       Map<User,Double> map = calculateBalance(groupId);

       return map.getOrDefault(user,0d);

    }

    public List<SettlementDTO> getSettlements(Long groupId) {

        Map<User,Double> map = calculateBalance(groupId);

        List<Pair> creditors = new ArrayList<>();

        List<Pair> debtors = new ArrayList<>();

        for(User user:map.keySet()){
            if(map.get(user) > 0) creditors.add(new Pair(user,map.get(user)));
            if(map.get(user) < 0) debtors.add(new Pair(user,map.get(user)));
        }

        creditors.sort((p1, p2) -> Double.compare(p2.getAmount(), p1.getAmount()));
        debtors.sort((p1, p2) -> Double.compare(p1.getAmount(), p2.getAmount()));


        List<SettlementDTO> settle = new ArrayList<>();

        while(!creditors.isEmpty() && !debtors.isEmpty()){


            Double amount1 = creditors.get(0).getAmount();
            Double amount2 = debtors.get(0).getAmount();

            Double minAmount = Math.min(amount1,Math.abs(amount2));

            SettlementDTO settlementDTO = new SettlementDTO();

            settlementDTO.setFromUserId(debtors.get(0).getUser().getId());
            settlementDTO.setToUserId(creditors.get(0).getUser().getId());
            settlementDTO.setFromUserName(debtors.get(0).getUser().getName());
            settlementDTO.setToUserName(creditors.get(0).getUser().getName());
            settlementDTO.setAmount(minAmount);

            double creditAmount = creditors.get(0).getAmount();
            creditors.get(0).setAmount(creditAmount - minAmount);

            double debitAmount = debtors.get(0).getAmount();
            debtors.get(0).setAmount(debitAmount + minAmount);

            if (Math.abs(creditors.get(0).getAmount()) < 0.0001) creditors.remove(0);
            if (Math.abs(debtors.get(0).getAmount()) < 0.0001) debtors.remove(0);

            settle.add(settlementDTO);
        }

        return settle;
    }


    class Pair{
        User user;
        Double amount;

        public Pair(User user, Double amount) {
            this.user = user;
            this.amount = amount;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }
    }





}
