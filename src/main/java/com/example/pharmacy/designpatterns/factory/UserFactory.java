package com.example.pharmacy.designpatterns.factory;

import com.example.pharmacy.models.User;
import com.example.pharmacy.models.Role;
import org.springframework.beans.factory.annotation.Autowired;

public class UserFactory {
    private static IUserFactory factory;

    @Autowired
    public static void setFactory(IUserFactory factory) {
        UserFactory.factory = factory;
    }

    public static User createUser(String username, String password, Role role) {
        if (factory == null) {
            // Fallback ke implementasi lama jika factory belum di-set
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setRole(role);
            return user;
        }
        return factory.createUser(username, password, role);
    }
}