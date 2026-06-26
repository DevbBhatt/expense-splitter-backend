package com.expensesplitter.expense_splitter.dto;

import lombok.Data;

import java.time.LocalDateTime;


public class ExpenseResponse {

    private Long id;

    private String description;

    private Double amount;

    private String splitType;

    private LocalDateTime createdAt;

    private UserResponse paidBy;

    private GroupResponse group;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getSplitType() {
        return splitType;
    }

    public void setSplitType(String splitType) {
        this.splitType = splitType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UserResponse getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(UserResponse paidBy) {
        this.paidBy = paidBy;
    }

    public GroupResponse getGroup() {
        return group;
    }

    public void setGroup(GroupResponse group) {
        this.group = group;
    }
}
