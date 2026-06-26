package com.expensesplitter.expense_splitter.dto;

import java.time.LocalDateTime;

public class GroupResponse {

    private Long id;

    private String name;

    private LocalDateTime createdAt;

    private UserResponse createdBy;

    // getters setters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public UserResponse getCreatedBy() {
        return createdBy;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setCreatedBy(UserResponse createdBy) {
        this.createdBy = createdBy;
    }
}