package com.example.pharmacy.designpatterns.factory;

import com.example.pharmacy.models.User;
import com.example.pharmacy.models.Role;

public abstract class AbstractUserFactory implements IUserFactory {
    protected void validateUserData(String username, String password, Role role) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
    }

    @Override
    public final User createUser(String username, String password, Role role) {
        validateUserData(username, password, role);
        return createUserInternal(username, password, role);
    }

    protected abstract User createUserInternal(String username, String password, Role role);
}