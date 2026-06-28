package com.expensesplitter.expense_splitter.repository;

import com.expensesplitter.expense_splitter.entity.Group;
import com.expensesplitter.expense_splitter.entity.GroupMember;
import com.expensesplitter.expense_splitter.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember,Long> {

    List<GroupMember> findByGroup(Group group);

    Optional<GroupMember> findByGroupAndUser(Group group, User user);


    boolean existsByGroupIdAndUserId(Long groupId, Long userId);

    List<GroupMember> findByGroupAndIsDeletedFalseAndUser_IsDeletedFalse(Group group);

    Page<GroupMember> findByGroupAndIsDeletedFalseAndUser_IsDeletedFalse(Group group, Pageable pageable);

    Optional<GroupMember> findByGroupIdAndUserId(Long groupId, Long userId);

    Page<GroupMember> findByUserAndIsDeletedFalseAndGroup_IsDeletedFalse(
            User user,
            Pageable pageable
    );
}
