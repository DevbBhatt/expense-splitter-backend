package com.expensesplitter.expense_splitter.repository;

import com.expensesplitter.expense_splitter.entity.Group;
import com.expensesplitter.expense_splitter.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface GroupRepository extends JpaRepository<Group,Long> {

    Page<Group> findByIsDeletedFalse(Pageable pageable);
    List<Group> findByIsDeletedFalse();
    List<Group> findByCreatedByAndIsDeletedFalse(User createdBy);
}
