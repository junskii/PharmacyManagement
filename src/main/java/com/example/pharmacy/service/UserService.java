package com.example.pharmacy.service;

import com.example.pharmacy.mapper.UserMapper;
import com.example.pharmacy.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Arrays;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public List<User> findAllCashiers() {
        // Now using findByRoleNames to include both CASHIER and ADMIN roles
        return userMapper.findByRoleNames(Arrays.asList("CASHIER", "ADMIN"));
    }

    public User findById(Integer id) {
        return userMapper.findById(id);
    }

    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }
}