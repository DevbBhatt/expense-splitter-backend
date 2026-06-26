package com.expensesplitter.expense_splitter.service;

import com.expensesplitter.expense_splitter.entity.User;
import com.expensesplitter.expense_splitter.exception.ResourceNotFoundException;
import com.expensesplitter.expense_splitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new ResourceNotFoundException("User Not Found");
        }

        if (user.isDeleted()) {
            throw new ResourceNotFoundException("User is Deleted");
        }

        return user;
    }
}
