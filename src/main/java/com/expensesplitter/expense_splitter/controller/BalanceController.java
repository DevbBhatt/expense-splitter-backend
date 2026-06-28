package com.expensesplitter.expense_splitter.controller;

import com.expensesplitter.expense_splitter.dto.BalanceResponse;
import com.expensesplitter.expense_splitter.dto.SettlementDTO;
import com.expensesplitter.expense_splitter.dto.UserBalanceSummaryResponse;
import com.expensesplitter.expense_splitter.entity.User;
import com.expensesplitter.expense_splitter.service.BalanceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/balances")
@SecurityRequirement(name = "bearerAuth")
public class BalanceController {

    @Autowired
    private BalanceService balanceService;

    @GetMapping("/me")
    public List<UserBalanceSummaryResponse> getMyBalances() {

        return balanceService.getMyBalances();

    }


    @GetMapping("/groups/{groupId}/balances")
    public List<BalanceResponse> getGroupBalance(@PathVariable Long groupId){
        return balanceService.getGroupBalance(groupId);
    }

    @GetMapping("/groups/{groupId}/balances/{userId}")
    public  Double getUserBalanceInGroup(@PathVariable Long groupId,
                                                   @PathVariable Long userId){
        return balanceService.getUserBalanceInGroup(groupId,userId);
    }


    @GetMapping("/groups/{groupId}/settlements")
    public List<SettlementDTO> getSettleMents(@PathVariable Long groupId){
       return balanceService.getSettlements(groupId);
    }


}
