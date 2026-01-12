package com.example.pharmacy.designpatterns.factory;

import com.example.pharmacy.models.User;
import com.example.pharmacy.models.Role;
import org.springframework.stereotype.Component;

@Component
public class AdminUserFactory extends AbstractUserFactory {
    @Override
    protected User createUserInternal(String username, String password, Role role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        // Admin specific initialization bisa ditambahkan di sini
        // tanpa mengubah schema database
        return user;
    }
}