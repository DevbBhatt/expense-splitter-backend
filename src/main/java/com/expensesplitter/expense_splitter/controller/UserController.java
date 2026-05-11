package com.expensesplitter.expense_splitter.controller;

import com.expensesplitter.expense_splitter.entity.User;
import com.expensesplitter.expense_splitter.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping()
    public User createUser(@Valid @RequestBody User user) { return userService.createUser(user); }

    @GetMapping()
    public Page<User> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size){

        return userService.getAllUsers(page,size);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@Valid @PathVariable Long id,
                           @RequestBody User user){

        return userService.updateUser(id,user);
    }

    @DeleteMapping("/{id}")
    public User deleteById(@PathVariable Long id){
        return userService.deleteById(id);
    }

}
