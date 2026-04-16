package com.expensesplitter.expense_splitter.service;

import com.expensesplitter.expense_splitter.entity.User;
import com.expensesplitter.expense_splitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    public User createUser(User user)  {


        User existingUser = userRepository.findByEmail(user.getEmail());

        if(existingUser != null){

            if(existingUser.isDeleted()){
                existingUser.setDeleted(false);
                return userRepository.save(existingUser);
            }

            throw new RuntimeException("Email already exists");
        }

        user.setPassword(encoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);


    }


    public List<User> getAllUsers() {
        return userRepository.findByIsDeletedFalse();
    }


    public User getUserById(Long id) {

      User user = userRepository.findById(id)
              .orElseThrow(() -> new RuntimeException("User Not found"));

      if(user.isDeleted()) throw new RuntimeException("User is Deleted");

      return user;
    }

    public User updateUser(Long id, User user) {

        User user1 = userRepository.findById(id).orElseThrow(() ->new RuntimeException("User Not found"));

        if(user1.isDeleted()) throw new RuntimeException("User is Deleted");
       user1.setName(user.getName());
       user1.setEmail(user.getEmail());

       return userRepository.save(user1);
    }

    public User deleteById(Long id) {
        User user = userRepository.findById(id).
                orElseThrow(() -> new RuntimeException("User Not found"));
        if(user.isDeleted()){
            throw new RuntimeException("User already deleted");
        }
        user.setDeleted(true);
         userRepository.save(user);
         return user;
    }
}
