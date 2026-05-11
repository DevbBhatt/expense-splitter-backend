package com.expensesplitter.expense_splitter.controller;

import com.expensesplitter.expense_splitter.dto.ExpenseRequest;
import com.expensesplitter.expense_splitter.entity.Expense;
import com.expensesplitter.expense_splitter.entity.ExpenseSplit;
import com.expensesplitter.expense_splitter.service.ExpenseService;
import com.expensesplitter.expense_splitter.service.SplitService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private SplitService splitService;

    @PostMapping("/groups/{groupId}/expenses/{userId}")
    public Expense addExpense(@Valid @PathVariable Long groupId,
                              @PathVariable Long userId,
                              @RequestBody ExpenseRequest request){
        return expenseService.addExpense(groupId,userId,request);
    }


    @GetMapping("/groups/{groupId}/expenses")
    public Page<Expense> getGroupExpenses(@PathVariable Long groupId,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "5") int size){
        return expenseService.getGroupExpenses(groupId,page,size);
    }

    @GetMapping("/{expenseId}")
    public Expense getExpenseById(@PathVariable Long expenseId){
        return expenseService.getExpenseById(expenseId);
    }


    @GetMapping("/{expenseId}/splits")
    public List<ExpenseSplit> getExpenseSplits(@PathVariable Long expenseId){
        return splitService.getExpenseSplits(expenseId);
    }

    // Later
//    @PutMapping("/{expenseID}")
//    public Expense updateExpense(@valid @PathVariable Long expenseId){
//        return expenseService.updateExpense(expenseId);
//    }

    @DeleteMapping("{expenseId}")
    public Expense deleteExpense(@PathVariable Long expenseId){
        return expenseService.deleteExpense(expenseId);
    }



}
