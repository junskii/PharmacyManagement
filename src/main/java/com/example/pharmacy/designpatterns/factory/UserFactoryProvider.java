package com.example.pharmacy.designpatterns.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserFactoryProvider {
    private final Map<String, IUserFactory> factoryMap;
    private final IUserFactory defaultFactory;

    @Autowired
    public UserFactoryProvider(List<IUserFactory> factories) {
        this.factoryMap = new HashMap<>();
        this.defaultFactory = factories.stream()
                .filter(f -> f instanceof DefaultUserFactory)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("DefaultUserFactory not found"));

        for (IUserFactory factory : factories) {
            String role = factory.getClass().getSimpleName()
                    .replace("UserFactory", "")
                    .toUpperCase();
            factoryMap.put(role, factory);
        }
    }

    public IUserFactory getFactory(String role) {
        return factoryMap.getOrDefault(role, defaultFactory);
    }
}