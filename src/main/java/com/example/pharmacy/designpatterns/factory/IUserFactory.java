package com.example.pharmacy.designpatterns.factory;

import com.example.pharmacy.models.User;
import com.example.pharmacy.models.Role;

public interface IUserFactory {
    User createUser(String username, String password, Role role);
}