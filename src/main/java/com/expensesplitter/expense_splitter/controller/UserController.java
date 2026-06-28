package com.expensesplitter.expense_splitter.controller;

import com.expensesplitter.expense_splitter.dto.UserResponse;
import com.expensesplitter.expense_splitter.entity.User;
import com.expensesplitter.expense_splitter.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public UserResponse getCurrentUser() {
        return userService.getCurrentUser();
    }


    @PostMapping()
    public UserResponse createUser(@Valid @RequestBody User user) { return userService.createUser(user); }

    @GetMapping()
    public Page<UserResponse> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "5") int size){

        return userService.getAllUsers(page,size);
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(@Valid @PathVariable Long id,
                           @RequestBody User user){

        return userService.updateUser(id,user);
    }

    @DeleteMapping("/{id}")
    public UserResponse deleteById(@PathVariable Long id){
        return userService.deleteById(id);
    }

}
