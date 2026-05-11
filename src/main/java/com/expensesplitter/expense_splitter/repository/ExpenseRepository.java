package com.expensesplitter.expense_splitter.repository;

import com.expensesplitter.expense_splitter.entity.Expense;
import com.expensesplitter.expense_splitter.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense,Long> {

    List<Expense> findByGroup(Group group);

    Page<Expense> findByGroupAndIsDeletedFalse(Group group, Pageable pageable);

}
