package com.expensesplitter.expense_splitter.controller;

import com.expensesplitter.expense_splitter.dto.AuthResponse;
import com.expensesplitter.expense_splitter.dto.LoginRequest;
import com.expensesplitter.expense_splitter.dto.RegisterRequest;
import com.expensesplitter.expense_splitter.entity.User;
import com.expensesplitter.expense_splitter.service.AuthService;
import com.expensesplitter.expense_splitter.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody RegisterRequest request) {
        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(request.getPassword());

        // Ab is newUser ko service mein pass karein
        userService.createUser(newUser);
        return ResponseEntity.status(HttpStatus.OK).body("User registered successfully");
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

        String token = authService.login(request);

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setMessage("Login successful");

        return ResponseEntity.ok(response);
    }





















}
