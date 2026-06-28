package com.expensesplitter.expense_splitter.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupMemberResponse {

    private Long id;

    private Long userId;

    private String userName;

    private String userEmail;

    private LocalDateTime joinedAt;


}
