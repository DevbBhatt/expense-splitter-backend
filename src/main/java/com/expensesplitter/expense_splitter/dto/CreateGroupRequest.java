package com.expensesplitter.expense_splitter.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateGroupRequest {

    @NotBlank(message = "Group name is required")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}