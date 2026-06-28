package com.expensesplitter.expense_splitter.service;

import com.expensesplitter.expense_splitter.dto.UserResponse;
import com.expensesplitter.expense_splitter.entity.User;
import com.expensesplitter.expense_splitter.exception.BadRequestException;
import com.expensesplitter.expense_splitter.exception.ResourceNotFoundException;
import com.expensesplitter.expense_splitter.mapper.UserMapper;
import com.expensesplitter.expense_splitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CurrentUserService currentUserService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    public UserResponse getCurrentUser() {
        User currentUser = currentUserService.getCurrentUser();

        return userMapper.toResponse(currentUser);

    }

    public UserResponse createUser(User user)  {


        User existingUser = userRepository.findByEmail(user.getEmail());

        if(existingUser != null){

            if(existingUser.isDeleted()){
                existingUser.setDeleted(false);

                User savedUser = userRepository.save(existingUser);

                return userMapper.toResponse(savedUser);
            }

            throw new BadRequestException("Email already exists");
        }

        user.setPassword(encoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);


    }


    public Page<UserResponse> getAllUsers(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return userRepository
                .findByIsDeletedFalse(pageable)
                .map(userMapper::toResponse);
    }


    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Not found"));

        if(user.isDeleted()) {
            throw new ResourceNotFoundException("User is Deleted");
        }

        return userMapper.toResponse(user);
    }

    public UserResponse updateUser(Long id, User user) {

        User user1 = userRepository.findById(id).orElseThrow(() ->new ResourceNotFoundException("User Not found"));

        if(user1.isDeleted()) throw new ResourceNotFoundException("User is Deleted");
       user1.setName(user.getName());
       user1.setEmail(user.getEmail());

        User updatedUser = userRepository.save(user1);
        return userMapper.toResponse(updatedUser);
    }

    public UserResponse deleteById(Long id) {
        User user = userRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("User Not found"));
        if(user.isDeleted()){
            throw new ResourceNotFoundException("User already deleted");
        }
        user.setDeleted(true);
        User deletedUser = userRepository.save(user);

        return userMapper.toResponse(deletedUser);
    }


}
