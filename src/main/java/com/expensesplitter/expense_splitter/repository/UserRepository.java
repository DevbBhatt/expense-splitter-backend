package com.expensesplitter.expense_splitter.repository;

import com.expensesplitter.expense_splitter.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    User findByEmail(String email);

    Page<User> findByIsDeletedFalse(Pageable pageable);
}
