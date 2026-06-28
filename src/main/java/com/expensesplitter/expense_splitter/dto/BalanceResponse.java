package com.expensesplitter.expense_splitter.dto;

import lombok.Data;

@Data
public class BalanceResponse {

    private Long userId;

    private String userName;

    private Double balance;

}