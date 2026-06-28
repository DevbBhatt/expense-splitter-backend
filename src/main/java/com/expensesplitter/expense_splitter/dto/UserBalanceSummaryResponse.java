package com.expensesplitter.expense_splitter.dto;

import lombok.Data;

@Data
public class UserBalanceSummaryResponse {

    private String groupName;

    private Double balance;

}
