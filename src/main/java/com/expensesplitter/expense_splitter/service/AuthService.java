package com.expensesplitter.expense_splitter.service;

import com.expensesplitter.expense_splitter.dto.LoginRequest;
import com.expensesplitter.expense_splitter.entity.User;
import com.expensesplitter.expense_splitter.exception.InvalidCredentialsException;
import com.expensesplitter.expense_splitter.exception.ResourceNotFoundException;
import com.expensesplitter.expense_splitter.repository.UserRepository;
import com.expensesplitter.expense_splitter.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public String login(LoginRequest request) {

        // 1. user fetch
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        // 2. password match
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }

        // 3. token generate
        return jwtUtil.generateToken(user.getEmail());
    }
}