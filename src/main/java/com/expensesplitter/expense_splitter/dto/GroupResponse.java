package com.expensesplitter.expense_splitter.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupResponse {

    private Long id;

    private String name;

    private LocalDateTime createdAt;

    private UserResponse createdBy;

}